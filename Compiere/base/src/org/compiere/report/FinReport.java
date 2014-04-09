/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.report;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.print.*;
import org.compiere.process.*;
import org.compiere.util.*;

/**
 *  Financial Report Engine
 *
 *  @author Jorg Janke
 *  @version $Id: FinReport.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class FinReport extends SvrProcess
{
	/**	Period Parameter				*/
	private int					p_C_Period_ID = 0;
	/**	Org Parameter					*/
	private int					p_Org_ID = 0;
	/**	BPartner Parameter				*/
	private int					p_C_BPartner_ID = 0;
	/**	Product Parameter				*/
	private int					p_M_Product_ID = 0;
	/**	Project Parameter				*/
	private int					p_C_Project_ID = 0;
	/**	Activity Parameter				*/
	private int					p_C_Activity_ID = 0;
	/**	SalesRegion Parameter			*/
	private int					p_C_SalesRegion_ID = 0;
	/**	Campaign Parameter				*/
	private int					p_C_Campaign_ID = 0;
	/** Update Balances Parameter		*/
	private boolean				p_UpdateBalances = true;
	/** Details before Lines			*/
	private boolean				p_DetailsSourceFirst = false;
	/** Hierarchy						*/
	private int					p_PA_Hierarchy_ID = 0;

	/**	Start Time						*/
	private long 				m_start = System.currentTimeMillis();

	/**	Report Definition				*/
	private MReport				m_report = null;
	/**	Periods in Calendar				*/
	private FinReportPeriod[]	m_periods = null;
	/**	Index of m_C_Period_ID in m_periods		**/
	private int					m_reportPeriod = -1;
	/**	Parameter Where Clause			*/
	private StringBuffer		m_parameterWhere = new StringBuffer();
	/**	The Report Columns				*/
	private MReportColumn[] 	m_columns;
	/** The Report Lines				*/
	private MReportLine[] 		m_lines;
	/** Balance Aggregation             */
	private int                 p_Fact_Accumulation_ID;
	
	private MFactAccumulation accum = null;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		StringBuffer sb = new StringBuffer ("Record_ID=")
			.append(getRecord_ID());
		//	Parameter
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("C_Period_ID"))
				p_C_Period_ID = element.getParameterAsInt();
			else if (name.equals("PA_Hierarchy_ID"))
				p_PA_Hierarchy_ID = element.getParameterAsInt();
			else if (name.equals("Org_ID"))
				p_Org_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("M_Product_ID"))
				p_M_Product_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("C_Project_ID"))
				p_C_Project_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("C_Activity_ID"))
				p_C_Activity_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("C_SalesRegion_ID"))
				p_C_SalesRegion_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("C_Campaign_ID"))
				p_C_Campaign_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("UpdateBalances"))
				p_UpdateBalances = "Y".equals(element.getParameter());
			else if (name.equals("DetailsSourceFirst"))
				p_DetailsSourceFirst = "Y".equals(element.getParameter());
			else if (name.equals("Fact_Accumulation_ID"))
				p_Fact_Accumulation_ID = ((BigDecimal)element.getParameter()).intValue();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}


		//	Load Report Definition
		m_report = new MReport (getCtx(), getRecord_ID(), null);
		sb.append(" - ").append(m_report);
		//
		setPeriods();
		sb.append(" - C_Period_ID=").append(p_C_Period_ID);
		//
		log.info(sb.toString());
	//	m_report.list();
	}	//	prepare

	/**
	 * 	Set Periods
	 */
	private void setPeriods()
	{
		log.info("C_Calendar_ID=" + m_report.getC_Calendar_ID());
		Timestamp today = TimeUtil.getDay(System.currentTimeMillis());
		ArrayList<FinReportPeriod> list = new ArrayList<FinReportPeriod>();

		String sql = "SELECT p.C_Period_ID, p.Name, p.StartDate, p.EndDate, MIN(p1.StartDate) "
			+ "FROM C_Period p "
			+ " INNER JOIN C_Year y ON (p.C_Year_ID=y.C_Year_ID),"
			+ " C_Period p1 "
			+ "WHERE y.C_Calendar_ID=?"
			+ " AND p.PeriodType='S' "
			+ " AND p1.C_Year_ID=y.C_Year_ID AND p1.PeriodType='S' "
			+ "GROUP BY p.C_Period_ID, p.Name, p.StartDate, p.EndDate "
			+ "ORDER BY p.StartDate";

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, m_report.getC_Calendar_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				FinReportPeriod frp = new FinReportPeriod (rs.getInt(1), rs.getString(2),
					rs.getTimestamp(3), rs.getTimestamp(4), rs.getTimestamp(5));
				list.add(frp);
				if (p_C_Period_ID == 0 && frp.inPeriod(today))
					p_C_Period_ID = frp.getC_Period_ID();
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}
		//	convert to Array
		m_periods = new FinReportPeriod[list.size()];
		list.toArray(m_periods);
		//	today after latest period
		if (p_C_Period_ID == 0)
		{
			m_reportPeriod = m_periods.length - 1;
			p_C_Period_ID = m_periods[m_reportPeriod].getC_Period_ID ();
		}
	}	//	setPeriods

	
	/**************************************************************************
	 *  Perform process.
	 *  @return Message to be translated
	 *  @throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("AD_PInstance_ID=" + getAD_PInstance_ID());
		//	** Create Temporary and empty Report Lines from PA_ReportLine
		//	- AD_PInstance_ID, PA_ReportLine_ID, 0, 0
		int PA_ReportLineSet_ID = m_report.getLineSet().getPA_ReportLineSet_ID();
		int level = 1;
		if (p_DetailsSourceFirst)					//	LevelNo
			level = -1;
		StringBuffer sql = new StringBuffer ("INSERT INTO T_Report ")
			.append("(AD_PInstance_ID, PA_ReportLine_ID, Record_ID,Fact_Acct_ID, SeqNo,LevelNo, Name,Description) ")
			.append("SELECT ").append(getAD_PInstance_ID()).append(", PA_ReportLine_ID, 0,0, SeqNo,")
			.append(level).append(", Name,Description ")
			.append("FROM PA_ReportLine ")
			.append("WHERE IsActive='Y' AND PA_ReportLineSet_ID=").append(PA_ReportLineSet_ID);

		int no = DB.executeUpdate(get_TrxName(), sql.toString());
		log.fine("Report Lines = " + no);	//	Level 1

		//	Update AcctSchema Balances
		if (p_UpdateBalances)
			FinBalance.updateBalance (getCtx(), 
				m_report.getC_AcctSchema_ID(), null, get_TrxName(), p_Fact_Accumulation_ID, this);

		//	** Get Data	** Segment Values
		m_columns = m_report.getColumnSet().getColumns();
		if (m_columns.length == 0)
			throw new CompiereUserException("@No@ @PA_ReportColumn_ID@");
		m_lines = m_report.getLineSet().getLiness();
		if (m_lines.length == 0)
			throw new CompiereUserException("@No@ @PA_ReportLine_ID@");
		
	
		accum = new MFactAccumulation(getCtx(),p_Fact_Accumulation_ID,get_Trx());
		//	Optional Org
		if (p_Org_ID != 0)
			m_parameterWhere.append(" AND ").append(MReportTree.getWhereClause(getCtx(), 
				p_PA_Hierarchy_ID, X_C_AcctSchema_Element.ELEMENTTYPE_Organization, p_Org_ID));
		//	Optional BPartner
		if (p_C_BPartner_ID != 0)
		{
			if(!accum.isBusinessPartner()){
				log.log(Level.SEVERE, "Balance aggregation not summarized by Business partner");
				throw new CompiereUserException("Invalid Balance Aggregation");
			}
			m_parameterWhere.append(" AND ").append(MReportTree.getWhereClause(getCtx(), 
				p_PA_Hierarchy_ID, X_C_AcctSchema_Element.ELEMENTTYPE_BPartner, p_C_BPartner_ID));
		}
		//	Optional Product
		if (p_M_Product_ID != 0)
		{
		    if(!accum.isProduct()){
		    	log.log(Level.SEVERE, "Balance aggregation not summarized by Product");
		    	throw new CompiereUserException("Invalid Balance Aggregation");
		    }
			m_parameterWhere.append(" AND ").append(MReportTree.getWhereClause(getCtx(), 
				p_PA_Hierarchy_ID, X_C_AcctSchema_Element.ELEMENTTYPE_Product, p_M_Product_ID));
		}
		//	Optional Project
		if (p_C_Project_ID != 0)
		{
			if(!accum.isProject()){
				log.log(Level.SEVERE, "Balance aggregation not summarized by Project");
				throw new CompiereUserException("Invalid Balance Aggregation");
			}
			m_parameterWhere.append(" AND ").append(MReportTree.getWhereClause(getCtx(), 
				p_PA_Hierarchy_ID, X_C_AcctSchema_Element.ELEMENTTYPE_Project, p_C_Project_ID));
		}
		//	Optional Activity
		if (p_C_Activity_ID != 0)
		{
			if(!accum.isActivity()){
				log.log(Level.SEVERE, "Balance aggregation not summarized by Activity");
				throw new CompiereUserException("Invalid Balance Aggregation");
			}
			m_parameterWhere.append(" AND ").append(MReportTree.getWhereClause(getCtx(), 
				p_PA_Hierarchy_ID, X_C_AcctSchema_Element.ELEMENTTYPE_Activity, p_C_Activity_ID));
		}
		//	Optional Campaign
		if (p_C_Campaign_ID != 0)
		{
			if(!accum.isCampaign()){
				log.log(Level.SEVERE, "Balance aggregation not summarized by Campaign");
				throw new CompiereUserException("Invalid Balance Aggregation");
			}
			m_parameterWhere.append(" AND C_Campaign_ID=").append(p_C_Campaign_ID);
		}
		//	m_parameterWhere.append(" AND ").append(MReportTree.getWhereClause(getCtx(), 
		//		MAcctSchemaElement.ELEMENTTYPE_Campaign, p_C_Campaign_ID));
		//	Optional Sales Region
		if (p_C_SalesRegion_ID != 0)
		{
			if(!accum.isSalesRegion()){
				log.log(Level.SEVERE, "Balance aggregation not summarized by Sales Region");
				throw new CompiereUserException("Invalid Balance Aggregation");
			}
			m_parameterWhere.append(" AND ").append(MReportTree.getWhereClause(getCtx(), 
				p_PA_Hierarchy_ID, X_C_AcctSchema_Element.ELEMENTTYPE_SalesRegion, p_C_SalesRegion_ID));
		}
		
		//	for all lines
		for (int line = 0; line < m_lines.length; line++)
		{
			//	Line Segment Value (i.e. not calculation)
			if (m_lines[line].isLineTypeSegmentValue())
				updateLine (line);
		}	//	for all lines

		insertLineDetail();		//	also clean up
		doCalculations();

		deleteUnprintedLines();

		//	Create Report
		if (Ini.isClient())
			getProcessInfo().setTransientObject (getPrintFormat());
		else
			getProcessInfo().setSerializableObject(getPrintFormat());

		log.fine((System.currentTimeMillis() - m_start) + " ms");
		return "";
	}	//	doIt

	
	/**************************************************************************
	 * 	For all columns (in a line) with relative period access update report lines.
	 * 	@param line report line
	 */
	private void updateLine (int line)
	{
		log.info("" + m_lines[line]);

		//	No source lines - Headings
		if (m_lines[line] == null || m_lines[line].getSources().length == 0)
		{
			log.warning ("No Source lines: " + m_lines[line]);
			return;
		}

		StringBuffer update = new StringBuffer();
		//	for all columns
		for (int col = 0; col < m_columns.length; col++)
		{
			//	Ignore calculation columns
			if (m_columns[col].isColumnTypeCalculation())
				continue;
			StringBuffer info = new StringBuffer();
			info.append("Line=").append(line).append(",Col=").append(col);

			//	SELECT SUM()
			StringBuffer select = new StringBuffer ("SELECT ");
			if (m_lines[line].getAmountType() != null)				//	line amount type overwrites column
			{
				String sql = m_lines[line].getSelectClause (true);
				select.append (sql);
				info.append(": LineAmtType=").append(m_lines[line].getAmountType());
			}
			else if (m_columns[col].getAmountType() != null)
			{
				String sql = m_columns[col].getSelectClause (true);
				select.append (sql);
				info.append(": ColumnAmtType=").append(m_columns[col].getAmountType());
			}
			else
			{
				log.warning("No Amount Type in line: " + m_lines[line] + " or column: " + m_columns[col]);
				continue;
			}

			//	Get Period/Date info
			select.append(" FROM Fact_Acct_Balance WHERE DateAcct ");
			BigDecimal relativeOffset = null;	//	current
			if (m_columns[col].isColumnTypeRelativePeriod())
				relativeOffset = m_columns[col].getRelativePeriod();
			FinReportPeriod frp = getPeriod (relativeOffset);
			if (m_lines[line].getAmountType() != null)			//	line amount type overwrites column
			{
				info.append(" - LineDateAcct=");
				if (m_lines[line].isPeriod())
				{
					String sql = frp.getPeriodWhere();
					info.append("Period");
					select.append(sql);
				}
				else if (m_lines[line].isYear())
				{
					String sql = frp.getYearWhere();
					info.append("Year");
					select.append(sql);
				}
				else if (m_lines[line].isTotal())
				{
					String sql = frp.getTotalWhere();
					info.append("Total");
					select.append(sql);
				}
				else
				{
					log.log(Level.SEVERE, "No valid Line AmountType");
					select.append("=0");	// valid sql	
				}
			}
			else if (m_columns[col].getAmountType() != null)
			{
				info.append(" - ColumnDateAcct=");
				if (m_columns[col].isPeriod())
				{
					String sql = frp.getPeriodWhere();
					info.append("Period");
					select.append(sql);
				}
				else if (m_columns[col].isYear())
				{
					String sql = frp.getYearWhere();
					info.append("Year");
					select.append(sql);
				}
				else if (m_columns[col].isTotal())
				{
					String sql = frp.getTotalWhere();
					info.append("Total");
					select.append(sql);
				}
				else
				{
					log.log(Level.SEVERE, "No valid Column AmountType");
					select.append("=0");	// valid sql	
				}
			}
				
			//	Line Where
			String s = m_lines[line].getWhereClause(p_PA_Hierarchy_ID);	//	(sources, posting type)
			if (s != null && s.length() > 0)
				select.append(" AND ").append(s);

			//	Report Where
			s = m_report.getWhereClause();
			if (s != null && s.length() > 0)
				select.append(" AND ").append(s);

			select.append(" AND Fact_Accumulation_ID = ").append(p_Fact_Accumulation_ID);
			//	PostingType
			if (!m_lines[line].isPostingType())		//	only if not defined on line
			{
				String PostingType = m_columns[col].getPostingType();
				if (PostingType != null && PostingType.length() > 0)
					select.append(" AND PostingType='").append(PostingType).append("'");
			}
			
			if (m_columns[col].isColumnTypeSegmentValue())
				select.append(m_columns[col].getWhereClause(p_PA_Hierarchy_ID));
			
			//	Parameter Where
			select.append(m_parameterWhere);
			log.finest("Line=" + line + ",Col=" + line + ": " + select);

			//	Update SET portion
			if (update.length() > 0)
				update.append(", ");
			update.append("Col_").append(col)
				.append(" = (").append(select).append(")");
			//
			log.finest(info.toString());
		}
		//	Update Line Values
		if (update.length() > 0)
		{
			update.insert (0, "UPDATE T_Report SET ");
			update.append(" WHERE AD_PInstance_ID=").append(getAD_PInstance_ID())
				.append(" AND PA_ReportLine_ID=").append(m_lines[line].getPA_ReportLine_ID())
				.append(" AND ABS(LevelNo)<3");		//	1=Line 2=Acct
			int no = DB.executeUpdate(get_TrxName(), update.toString());
			if (no != 1)
				log.log(Level.SEVERE, "#=" + no + " for " + update);
			log.finest(update.toString());
		}
	}	//	updateLine

	
	/**************************************************************************
	 *	Line + Column calculation
	 */
	private void doCalculations()
	{
		//	for all lines	***************************************************
		for (int line = 0; line < m_lines.length; line++)
		{
			if (!m_lines[line].isLineTypeCalculation ())
				continue;

			int oper_1 = m_lines[line].getOper_1_ID();
			int oper_2 = m_lines[line].getOper_2_ID();

			log.fine("Line " + line + " = #" + oper_1 + " " 
				+ m_lines[line].getCalculationType() + " #" + oper_2);

			//	Adding
			if (m_lines[line].isCalculationTypeAdd() 
				|| m_lines[line].isCalculationTypeRange())
			{
				
				StringBuffer sb = new StringBuffer ("UPDATE T_Report SET (");
				for (int col = 0; col < m_columns.length; col++)
				{
					if (col > 0)
						sb.append(",");
					sb.append ("Col_").append (col);
				}
				sb.append(") = (SELECT ");
				for (int col = 0; col < m_columns.length; col++)
				{
					if (col > 0)
						//jz for update sql translating sb.append(",");
						sb.append(", ");
					sb.append ("COALESCE(SUM(Col_").append (col).append("),0)");
				}
				sb.append(" FROM T_Report WHERE AD_PInstance_ID=").append(getAD_PInstance_ID())
					.append(" AND PA_ReportLine_ID IN (");
				if (m_lines[line].isCalculationTypeAdd())
					sb.append(oper_1).append(",").append(oper_2);
				else
					sb.append(getLineIDs (oper_1, oper_2));		//	list of columns to add up
				sb.append(") AND ABS(LevelNo)<2) "		//	1=Line 2=Acct
					+ "WHERE AD_PInstance_ID=").append(getAD_PInstance_ID())
					.append(" AND PA_ReportLine_ID=").append(m_lines[line].getPA_ReportLine_ID())
					.append(" AND ABS(LevelNo)<2");		//	not p_trx
				int no = DB.executeUpdate(get_TrxName(), sb.toString());
				if (no != 1)
					log.log(Level.SEVERE, "(+) #=" + no + " for " + m_lines[line] + " - " + sb.toString());
				else
				{
					log.fine("(+) Line=" + line + " - " + m_lines[line]);
					log.finest ("(+) " + sb.toString ());
				}
			}
			else if(m_lines[line].isCalculationTypeSubtract())	//	subtract
			{
				//	Step 1 - get First Value or 0 in there
				StringBuffer sb = new StringBuffer ("UPDATE T_Report SET (");
				for (int col = 0; col < m_columns.length; col++)
				{
					if (col > 0)
						sb.append(",");
					sb.append ("Col_").append (col);
				}
				sb.append(") = (SELECT ");
				for (int col = 0; col < m_columns.length; col++)
				{
					if (col > 0)
						sb.append(", "); //jz ", "
					sb.append ("COALESCE(r2.Col_").append (col).append(",0)");
				}
				sb.append(" FROM T_Report r2 WHERE r2.AD_PInstance_ID=").append(getAD_PInstance_ID())
					.append(" AND r2.PA_ReportLine_ID=").append(oper_1)
					.append(" AND r2.Record_ID=0 AND r2.Fact_Acct_ID=0) "
				//
					+ "WHERE AD_PInstance_ID=").append(getAD_PInstance_ID())
					   .append(" AND PA_ReportLine_ID=").append(m_lines[line].getPA_ReportLine_ID())
					.append(" AND ABS(LevelNo)<2");			//	1=Line 2=Acct
				int no = DB.executeUpdate(get_TrxName(), sb.toString());
				if (no != 1)
				{
					log.severe ("(x) #=" + no + " for " + m_lines[line] + " - " + sb.toString ());
					continue;
				}

				//	Step 2 - do Calculation with Second Value
				sb = new StringBuffer ("UPDATE T_Report r1 SET (");
				for (int col = 0; col < m_columns.length; col++)
				{
					if (col > 0)
						sb.append(",");
					sb.append ("Col_").append (col);
				}
				sb.append(") = (SELECT ");
				for (int col = 0; col < m_columns.length; col++)
				{
					if (col > 0)
						sb.append(",  ");//jz hard coded ", "
					sb.append ("COALESCE(r1.Col_").append (col).append(",0)");
					sb.append("-");
					sb.append ("COALESCE(r2.Col_").append (col).append(",0.000000001)");
					if (m_lines[line].isCalculationTypePercent())
						sb.append(" *100");
				}
				sb.append(" FROM T_Report r2 WHERE r2.AD_PInstance_ID=").append(getAD_PInstance_ID())
					.append(" AND r2.PA_ReportLine_ID=").append(oper_2)
					.append(" AND r2.Record_ID=0 AND r2.Fact_Acct_ID=0) "
				//
					+ "WHERE AD_PInstance_ID=").append(getAD_PInstance_ID())
					   .append(" AND PA_ReportLine_ID=").append(m_lines[line].getPA_ReportLine_ID())
					.append(" AND ABS(LevelNo)<2");			//	1=Line 2=Acct
				no = DB.executeUpdate(get_TrxName(), sb.toString());
				if (no != 1)
					log.severe ("(x) #=" + no + " for " + m_lines[line] + " - " + sb.toString ());
				else
				{
					log.fine("(x) Line=" + line + " - " + m_lines[line]);
					log.finest (sb.toString());
				}
			}
			else
				continue;
		}	//	for all lines


		//	for all columns		***********************************************
		for (int col = 0; col < m_columns.length; col++)
		{
			//	Only Calculations
			if (!m_columns[col].isColumnTypeCalculation ())
				continue;

			StringBuffer sb = new StringBuffer ("UPDATE T_Report SET ");
			//	Column to set
			sb.append ("Col_").append (col).append("=");
			//	First Operand
			int ii_1 = getColumnIndex(m_columns[col].getOper_1_ID());
			if (ii_1 < 0)
			{
				log.log(Level.SEVERE, "Column Index for Operator 1 not found - " + m_columns[col]);
				continue;
			}
			//	Second Operand
			int ii_2 = getColumnIndex(m_columns[col].getOper_2_ID());
			if (ii_2 < 0)
			{
				log.log(Level.SEVERE, "Column Index for Operator 2 not found - " + m_columns[col]);
				continue;
			}
			log.fine("Column " + col + " = #" + ii_1 + " " 
				+ m_columns[col].getCalculationType() + " #" + ii_2);
			//	Reverse Range
			if (ii_1 > ii_2 && m_columns[col].isCalculationTypeRange())
			{
				log.fine("Swap operands from " + ii_1 + " op " + ii_2);
				int temp = ii_1;
				ii_1 = ii_2;
				ii_2 = temp;
			}

			//	+
			if (m_columns[col].isCalculationTypeAdd())
				sb.append ("COALESCE(Col_").append (ii_1).append(",0)")
					.append("+")
					.append ("COALESCE(Col_").append (ii_2).append(",0)");
			//	-
			else if (m_columns[col].isCalculationTypeSubtract())
				sb.append ("COALESCE(Col_").append (ii_1).append(",0)")
					.append("-")
					.append ("COALESCE(Col_").append (ii_2).append(",0)");
			//	Range
			else if (m_columns[col].isCalculationTypeRange())
			{
				sb.append ("COALESCE(Col_").append (ii_1).append(",0)");
				for (int ii = ii_1+1; ii <= ii_2; ii++)
					sb.append("+COALESCE(Col_").append (ii).append(",0)");
			}
			else 
				continue;
			//
			sb.append(" WHERE AD_PInstance_ID=").append(getAD_PInstance_ID())
				.append(" AND ABS(LevelNo)<3");			//	1=Line 2=Acct
			int no = DB.executeUpdate(get_TrxName(), sb.toString());
			if (no < 1)
				log.severe ("#=" + no + " for " + m_columns[col] 
					+ " - " + sb.toString());
			else
			{
				log.fine("Col=" + col + " - " + m_columns[col]);
				log.finest (sb.toString ());
			}
		} 	//	for all columns
		
		// Do Percentage Calculation
		for(int l =0; l< m_lines.length;l++)
		{
			int oper_1 = m_lines[l].getOper_1_ID();
			int oper_2 = m_lines[l].getOper_2_ID();

			for(int c = 0; c <m_columns.length;c ++)
			{
				if(!m_lines[l].isLineTypeCalculation() && !m_columns[c].isColumnTypeCalculation())
					continue;
				
				if(m_lines[l].isLineTypeCalculation() && m_lines[l].isCalculationTypePercent())
				{
					StringBuffer sb = new StringBuffer ("UPDATE T_Report SET (");
					sb.append ("Col_").append (c);
					sb.append(") = (SELECT ");
					sb.append ("COALESCE(r2.Col_").append (c).append(",0)");
					sb.append(" FROM T_Report r2 WHERE r2.AD_PInstance_ID=").append(getAD_PInstance_ID())
						.append(" AND r2.PA_ReportLine_ID=").append(oper_1)
						.append(" AND r2.Record_ID=0 AND r2.Fact_Acct_ID=0) "
					//
						+ "WHERE AD_PInstance_ID=").append(getAD_PInstance_ID())
						   .append(" AND PA_ReportLine_ID=").append(m_lines[l].getPA_ReportLine_ID())
						.append(" AND ABS(LevelNo)<2");			//	1=Line 2=Acct
					int no = DB.executeUpdate(get_TrxName(), sb.toString());
					if (no != 1)
					{
						log.severe ("(x) #=" + no + " for " + m_lines[l] + " - " + sb.toString ());
						continue;
					}

					//	Step 2 - do Calculation with Second Value
					sb = new StringBuffer ("UPDATE T_Report r1 SET (");
					sb.append ("Col_").append (c);
					sb.append(") = (SELECT ");
					sb.append ("COALESCE(r1.Col_").append (c).append(",0)");
					sb.append("/");
					sb.append ("COALESCE(r2.Col_").append (c).append(",0.000000001)");
					sb.append(" *100");
					sb.append(" FROM T_Report r2 WHERE r2.AD_PInstance_ID=").append(getAD_PInstance_ID())
						.append(" AND r2.PA_ReportLine_ID=").append(oper_2)
						.append(" AND r2.Record_ID=0 AND r2.Fact_Acct_ID=0) "
					//
						+ "WHERE AD_PInstance_ID=").append(getAD_PInstance_ID())
						   .append(" AND PA_ReportLine_ID=").append(m_lines[l].getPA_ReportLine_ID())
						.append(" AND ABS(LevelNo)<2");			//	1=Line 2=Acct
					no = DB.executeUpdate(get_TrxName(), sb.toString());
					if (no != 1)
						log.severe ("(x) #=" + no + " for " + m_lines[l] + " - " + sb.toString ());
					else
					{
						log.fine("(x) Line=" + l + " - " + m_lines[l]);
						log.finest (sb.toString());
					}
				}
				else if (m_columns[c].isColumnTypeCalculation() && m_columns[c].isCalculationTypePercent())
				{
					StringBuffer sb = new StringBuffer ("UPDATE T_Report SET ");
					//	Column to set
					sb.append ("Col_").append (c).append("=");
					//	First Operand
					int ii_1 = getColumnIndex(m_columns[c].getOper_1_ID());
					if (ii_1 < 0)
					{
						log.log(Level.SEVERE, "Column Index for Operator 1 not found - " + m_columns[c]);
						continue;
					}
					//	Second Operand
					int ii_2 = getColumnIndex(m_columns[c].getOper_2_ID());
					if (ii_2 < 0)
					{
						log.log(Level.SEVERE, "Column Index for Operator 2 not found - " + m_columns[c]);
						continue;
					}
					log.fine("Column " + c + " = #" + ii_1 + " " 
						+ m_columns[c].getCalculationType() + " #" + ii_2);
					
					sb.append ("CASE WHEN COALESCE(Col_").append(ii_2)
					.append(",0)=0 THEN NULL ELSE ")
					.append("COALESCE(Col_").append (ii_1).append(",0)")
					.append("/")
					.append ("Col_").append (ii_2)
					.append("*100 END");	//	Zero Divide
					
					sb.append(" WHERE AD_PInstance_ID=").append(getAD_PInstance_ID())
					.append(" AND ABS(LevelNo)<3")			//	1=Line 2=Acct
					.append(" AND PA_ReportLine_ID=").append(m_lines[l].getPA_ReportLine_ID());
					int no = DB.executeUpdate(get_TrxName(), sb.toString());
					if (no < 1)
						log.severe ("#=" + no + " for " + m_columns[c] 
							+ " - " + sb.toString());
					else
					{
						log.fine("Col=" + c + " - " + m_columns[c]);
						log.finest (sb.toString ());
					}
				}
				else
					continue;
			}
		}

	}	//	doCalculations

	/**
	 * 	Get List of PA_ReportLine_ID from .. to
	 * 	@param fromID from ID
	 * 	@param toID to ID
	 * 	@return comma separated list
	 */
	private String getLineIDs (int fromID, int toID)
	{
		log.finest("From=" + fromID + " To=" + toID);
		StringBuffer sb = new StringBuffer();
		//sb.append(fromID);
		boolean addToList = false;
		boolean started = false;
		for (MReportLine element : m_lines) {
			int PA_ReportLine_ID = element.getPA_ReportLine_ID();
			log.finest( "Started=" + started 
						+ " Add=" + addToList 
						+ " ID=" + PA_ReportLine_ID + " - " + element);
			
			// In case to is greater than from
			if(!started && PA_ReportLine_ID==fromID)
				started=true;
			else if (!started && PA_ReportLine_ID==toID)
			{
					started=true;
					toID = fromID;
					log.fine("FromID greator than ToID");
			}
			
			if (started && addToList)
			{
				sb.append (",").append (PA_ReportLine_ID);
				if (PA_ReportLine_ID == toID)		//	done
					break;
			}
			else if (started)	
			{
				sb.append (PA_ReportLine_ID);
				addToList = true;
			}
		}
		
		return sb.toString();
	}	//	getLineIDs

	/**
	 * 	Get Column Index
	 * 	@param PA_ReportColumn_ID PA_ReportColumn_ID
	 * 	@return zero based index or if not found
	 */
	private int getColumnIndex (int PA_ReportColumn_ID)
	{
		for (int i = 0; i < m_columns.length; i++)
		{
			if (m_columns[i].getPA_ReportColumn_ID() == PA_ReportColumn_ID)
				return i;
		}
		return -1;
	}	//	getColumnIndex

	
	/**************************************************************************
	 * 	Get Financial Reporting Period based on reportong Period and offset.
	 * 	@param relativeOffset offset
	 * 	@return reporting period
	 */
	private FinReportPeriod getPeriod (BigDecimal relativeOffset)
	{
		if (relativeOffset == null)
			return getPeriod(0);
		return getPeriod(relativeOffset.intValue());
	}	//	getPeriod

	/**
	 * 	Get Financial Reporting Period based on reporting Period and offset.
	 * 	@param relativeOffset offset
	 * 	@return reporting period
	 */
	private FinReportPeriod getPeriod (int relativeOffset)
	{
		//	find current reporting period C_Period_ID
		if (m_reportPeriod < 0)
		{
			for (int i = 0; i < m_periods.length; i++)
			{
				if (p_C_Period_ID == m_periods[i].getC_Period_ID())
				{
					m_reportPeriod = i;
					break;
				}
			}
		}
		if (m_reportPeriod < 0 || m_reportPeriod >= m_periods.length)
			throw new UnsupportedOperationException ("Period index not found - ReportPeriod="
				+ m_reportPeriod + ", C_Period_ID=" + p_C_Period_ID);

		//	Bounds check
		int index = m_reportPeriod + relativeOffset;
		if (index < 0)
		{
			log.log(Level.SEVERE, "Relative Offset(" + relativeOffset 
				+ ") not valid for selected Period(" + m_reportPeriod + ")");
			index = 0;
		}
		else if (index >= m_periods.length)
		{
			log.log(Level.SEVERE, "Relative Offset(" + relativeOffset 
				+ ") not valid for selected Period(" + m_reportPeriod + ")");
			index = m_periods.length - 1;
		}
		//	Get Period
		return m_periods[index];
	}	//	getPeriod

	
	/**************************************************************************
	 *	Insert Detail Lines if enabled
	 */
	private void insertLineDetail()
	{
		if (!m_report.isListSources())
			return;
		log.info("");

		//	for all source lines
		for (int line = 0; line < m_lines.length; line++)
		{
			//	Line Segment Value (i.e. not calculation)
			if (m_lines[line].isLineTypeSegmentValue ())
				insertLineSource (line);
		}

		//	Clean up empty rows
		StringBuffer sql = new StringBuffer ("DELETE FROM T_Report WHERE ABS(LevelNo)<>1")
			.append(" AND Col_0 IS NULL AND Col_1 IS NULL AND Col_2 IS NULL AND Col_3 IS NULL AND Col_4 IS NULL AND Col_5 IS NULL AND Col_6 IS NULL AND Col_7 IS NULL AND Col_8 IS NULL AND Col_9 IS NULL")
			.append(" AND Col_10 IS NULL AND Col_11 IS NULL AND Col_12 IS NULL AND Col_13 IS NULL AND Col_14 IS NULL AND Col_15 IS NULL AND Col_16 IS NULL AND Col_17 IS NULL AND Col_18 IS NULL AND Col_19 IS NULL AND Col_20 IS NULL"); 
		int no = DB.executeUpdate(get_TrxName(), sql.toString());
		log.fine("Deleted empty #=" + no);
		
		//	Set SeqNo
		sql = new StringBuffer ("UPDATE T_Report r1 "
			+ "SET SeqNo = (SELECT SeqNo "
				+ "FROM T_Report r2 "
				+ "WHERE r1.AD_PInstance_ID=r2.AD_PInstance_ID AND r1.PA_ReportLine_ID=r2.PA_ReportLine_ID"
				+ " AND r2.Record_ID=0 AND r2.Fact_Acct_ID=0)"
			+ "WHERE SeqNo IS NULL");
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		log.fine("SeqNo #=" + no);

		if (!m_report.isListTrx())
			return;

		//	Set Name,Description - Indent Level 2
		String sql_select = "SELECT '__'||e.Name, fa.Description "
			+ "FROM Fact_Acct fa"
			+ " INNER JOIN AD_Table t ON (fa.AD_Table_ID=t.AD_Table_ID)"
			+ " INNER JOIN AD_Element e ON (t.TableName||'_ID'=e.ColumnName) "
			+ "WHERE r.Fact_Acct_ID=fa.Fact_Acct_ID";
		//	Translated Version ...
		sql = new StringBuffer ("UPDATE T_Report r SET (Name,Description)=(")
			.append(sql_select).append(") "
			+ "WHERE Fact_Acct_ID <> 0 AND AD_PInstance_ID=")
			.append(getAD_PInstance_ID());
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		if (CLogMgt.isLevelFinest())
			log.fine("Trx Name #=" + no + " - " + sql.toString());
	}	//	insertLineDetail

	/**
	 * 	Insert Detail Line per Source.
	 * 	For all columns (in a line) with relative period access
	 * 	- AD_PInstance_ID, PA_ReportLine_ID, variable, 0 - Level 1
	 * 	@param line line
	 */
	private void insertLineSource (int line)
	{
		log.info("Line=" + line + " - " + m_lines[line]);

		//	No source lines
		if (m_lines[line] == null || m_lines[line].getSources().length == 0)
			return;
		String variable = m_lines[line].getSourceColumnName();
		if (variable == null)
			return;
		log.fine("Variable=" + variable);
		

		//	Insert
		StringBuffer insert = new StringBuffer("INSERT INTO T_Report "
			+ "(AD_PInstance_ID, PA_ReportLine_ID, Record_ID,Fact_Acct_ID,LevelNo ");
		for (int col = 0; col < m_columns.length; col++)
			insert.append(",Col_").append(col);
		//	Select
		insert.append(") SELECT ")
			.append(getAD_PInstance_ID()).append(",")
			.append(m_lines[line].getPA_ReportLine_ID())
			.append(",").append(variable).append(",0,");	//	Record_ID, Fact_Acct_ID
		if (p_DetailsSourceFirst)							//	LevelNo
			insert.append("-");
		insert.append("2 ");

		//	for all columns create select statement
		for (MReportColumn element : m_columns) {
			insert.append(", ");
			//	No calculation
			if (element.isColumnTypeCalculation())
			{
				insert.append("NULL");
				continue;
			}

			//	SELECT SUM()
			StringBuffer select = new StringBuffer ("SELECT ");
			if (m_lines[line].getAmountType() != null)				//	line amount type overwrites column
				select.append (m_lines[line].getSelectClause (true));
			else if (element.getAmountType() != null)
				select.append (element.getSelectClause (true));
			else
			{
				insert.append("NULL");
				continue;
			}

			//	Get Period info
			select.append(" FROM Fact_Acct_Balance fb WHERE fb.Fact_Accumulation_ID = ")
				  .append(p_Fact_Accumulation_ID)
			      .append(" AND DateAcct ");
			FinReportPeriod frp = getPeriod (element.getRelativePeriod());
			if (m_lines[line].getAmountType() != null)			//	line amount type overwrites column
			{
				if (m_lines[line].isPeriod())
					select.append(frp.getPeriodWhere());
				else if (m_lines[line].isYear())
					select.append(frp.getYearWhere());
				else
					select.append(frp.getTotalWhere());
			}
			else if (element.getAmountType() != null)
			{
				if (element.isPeriod())
					select.append(frp.getPeriodWhere());
				else if (element.isYear())
					select.append(frp.getYearWhere());
				else
					select.append(frp.getTotalWhere());
			}
			//	Link
			select.append(" AND fb.").append(variable).append("=x.").append(variable);
			//	PostingType
			if (!m_lines[line].isPostingType())		//	only if not defined on line
			{
				String PostingType = element.getPostingType();
				if (PostingType != null && PostingType.length() > 0)
					select.append(" AND fb.PostingType='").append(PostingType).append("'");
			}
			//	Report Where
			String s = m_report.getWhereClause();
			if (s != null && s.length() > 0)
				select.append(" AND ").append(s);
			
			//	Limited Segment Values
			if (element.isColumnTypeSegmentValue())
				select.append(element.getWhereClause(p_PA_Hierarchy_ID));
			
			//	Parameter Where
			select.append(m_parameterWhere);
		//	System.out.println("    c=" + col + ", l=" + line + ": " + select);
			//
			insert.append("(").append(select).append(")");
		}
		//	WHERE (sources, posting type)
		StringBuffer where = new StringBuffer(m_lines[line].getWhereClause(p_PA_Hierarchy_ID));
		String s = m_report.getWhereClause();
		if (s != null && s.length() > 0)
		{
			if (where.length() > 0)
				where.append(" AND ");
			where.append(s);
		}
		if (where.length() > 0)
			where.append(" AND ");
		where.append(variable).append(" IS NOT NULL");
		
		// Balance aggregation
		where.append(" AND x.Fact_Accumulation_ID = ").append(p_Fact_Accumulation_ID);

		//	FROM .. WHERE
		insert.append(" FROM Fact_Acct_Balance x WHERE ").append(where);	
		//
		insert.append(m_parameterWhere)
			.append(" GROUP BY ").append(variable);

		int no = DB.executeUpdate(get_TrxName(), insert.toString());
		if (CLogMgt.isLevelFinest())
			log.fine("Source #=" + no + " - " + insert);
		if (no == 0)
			return;

		//	Set Name,Description
		StringBuffer sql = new StringBuffer ("UPDATE T_Report SET (Name,Description)=(")
			.append(m_lines[line].getSourceValueQuery()).append("Record_ID) ");
			//
		StringBuffer whereUpdate = new StringBuffer("WHERE Record_ID <> 0 AND AD_PInstance_ID=").append(getAD_PInstance_ID())
			.append(" AND PA_ReportLine_ID=").append(m_lines[line].getPA_ReportLine_ID())
			.append(" AND Fact_Acct_ID=0");
		sql.append(whereUpdate);
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		if (CLogMgt.isLevelFinest())
			log.fine("Name #=" + no + " - " + sql.toString());
		//	Indent Level 1
		StringBuffer sql2 = new StringBuffer ("UPDATE T_Report SET Name='_'||Name ");
		sql2.append(whereUpdate);
		no = DB.executeUpdate(get_TrxName(), sql2.toString());
		if (CLogMgt.isLevelFinest())
			log.fine("Name #=" + no + " - " + sql2.toString());
		
		//	Details
		if (m_report.isListTrx())
			insertLineTrx (line, variable);
	}	//	insertLineSource

	/**
	 * 	Create Trx Line per Source Detail.
	 * 	- AD_PInstance_ID, PA_ReportLine_ID, variable, Fact_Acct_ID - Level 2
	 * 	@param line line
	 * 	@param variable variable, e.g. Account_ID
	 */
	private void insertLineTrx (int line, String variable)
	{
		log.info("Line=" + line + " - Variable=" + variable);
		MReportLine rLine = m_lines[line];
		
		String lineWhere = rLine.getWhereClause(p_PA_Hierarchy_ID);
		
		//	Insert
		StringBuffer insert = new StringBuffer("INSERT INTO T_Report "
			+ "(AD_PInstance_ID, PA_ReportLine_ID, Record_ID,Fact_Acct_ID,LevelNo ");
		for (int col = 0; col < m_columns.length; col++)
			insert.append(",Col_").append(col);
		//	Select
		insert.append(") SELECT ")
			.append(getAD_PInstance_ID()).append(",")
			.append(rLine.getPA_ReportLine_ID()).append(",")
			.append(variable).append(",Fact_Acct_ID, ");
		if (p_DetailsSourceFirst)
			insert.append("-");
		insert.append("3 ");

		//	for all columns create select statement
		for (MReportColumn column : m_columns) {
			insert.append(", ");
			String sqlValue = "NULL";
			//	Amount Type ... Qty
			if (rLine.getAmountType() != null)				//	line amount type overwrites column
				sqlValue = rLine.getSelectClause (false);
			else if (column.getAmountType() != null)
				sqlValue = column.getSelectClause (false);
			//	Segment Values
			if (column.isColumnTypeSegmentValue())
			{
				String colWhere = column.getWhereClause(p_PA_Hierarchy_ID);
				sqlValue = "(SELECT " + sqlValue 
					+ " FROM Fact_Acct AA WHERE AA.Fact_Acct_ID=Fact_Acct.Fact_Acct_ID" 
					+ colWhere + ")";
			}
			//	Only relative Period (not calculation or segment value)
			else if (!(column.isColumnTypeRelativePeriod() 
				&& column.getRelativePeriodAsInt() == 0))
			{
			//	sqlValue = "NULL";
			}
			insert.append (sqlValue);
		}
		//	(sources, posting type)
		StringBuffer where = new StringBuffer(lineWhere);
		//	Report Where
		String s = m_report.getWhereClause();
		if (s != null && s.length() > 0)
		{
			if (where.length() > 0)
				where.append(" AND ");
			where.append(s);
		}
		//	Period restriction
		FinReportPeriod frp = getPeriod (0);
		if (where.length() > 0)
			where.append(" AND ");
		where.append(" DateAcct ").append(frp.getPeriodWhere());
		
		//	PostingType ??
//		if (!m_lines[line].isPostingType())		//	only if not defined on line
//		{
//	      String PostingType = m_columns[col].getPostingType();
//  	    if (PostingType != null && PostingType.length() > 0)
//      	  	where.append(" AND PostingType='").append(PostingType).append("'");
//		}
		where.append(" AND ").append(variable).append(" IS NOT NULL");
		
		//	Final FROM .. Where
		insert.append(" FROM Fact_Acct WHERE ").append(where);

		int no = DB.executeUpdate(get_TrxName(), insert.toString());
		log.finest("Trx #=" + no + " - " + insert);
		if (no == 0)
			return;
	}	//	insertLineTrx

	
	/**************************************************************************
	 *	Delete Unprinted Lines
	 */
	private void deleteUnprintedLines()
	{
		for (int line = 0; line < m_lines.length; line++)
		{
			//	Not Printed - Delete in T
			if (!m_lines[line].isPrinted())
			{
				String sql = "DELETE FROM T_Report WHERE AD_PInstance_ID=" + getAD_PInstance_ID()
					+ " AND PA_ReportLine_ID=" + m_lines[line].getPA_ReportLine_ID();
				int no = DB.executeUpdate(get_TrxName(), sql);
				if (no > 0)
					log.fine(m_lines[line].getName() + " - #" + no);
			}
		}	//	for all lines
	}	//	deleteUnprintedLines

	
	/**************************************************************************
	 *	Get/Create PrintFormat
	 * 	@return print format
	 */
	private MPrintFormat getPrintFormat()
	{
		int AD_PrintFormat_ID = m_report.getAD_PrintFormat_ID();
		log.info("AD_PrintFormat_ID=" + AD_PrintFormat_ID);
		MPrintFormat pf = null;
		boolean createNew = AD_PrintFormat_ID == 0;

		//	Create New
		if (createNew)
		{
			int AD_Table_ID = 544;		//	T_Report
			pf = MPrintFormat.createFromTable(Env.getCtx(), AD_Table_ID);
			AD_PrintFormat_ID = pf.getAD_PrintFormat_ID();
			m_report.setAD_PrintFormat_ID(AD_PrintFormat_ID);
			m_report.save();
		}
		else
			pf = MPrintFormat.get (getCtx(), AD_PrintFormat_ID, false);	//	use Cache

		//	Print Format Sync
		if (!m_report.getName().equals(pf.getName()))
			pf.setName(m_report.getName());
		if (m_report.getDescription() == null)
		{
			if (pf.getDescription () != null)
				pf.setDescription (null);
		}
		else if (!m_report.getDescription().equals(pf.getDescription()))
			pf.setDescription(m_report.getDescription());
		pf.save();
		log.fine(pf + " - #" + pf.getItemCount());

		//	Print Format Item Sync
		int count = pf.getItemCount();
		for (int i = 0; i < count; i++)
		{
			MPrintFormatItem pfi = pf.getItem(i);
			String ColumnName = pfi.getColumnName();
			//
			if (ColumnName == null)
			{
				log.log(Level.SEVERE, "No ColumnName for #" + i + " - " + pfi);
				if (pfi.isPrinted())
					pfi.setIsPrinted(false);
				if (pfi.isOrderBy())
					pfi.setIsOrderBy(false);
				if (pfi.getSortNo() != 0)
					pfi.setSortNo(0);
			}
			else if (ColumnName.startsWith("Col"))
			{
				int index = Integer.parseInt(ColumnName.substring(4));
				if (index < m_columns.length)
				{
					pfi.setIsPrinted(m_columns[index].isPrinted());
					String s = m_columns[index].getName();
					if (!pfi.getName().equals(s))
					{
						pfi.setName (s);
						pfi.setPrintName (s);
					}
					int seq = 30 + index;
					if (pfi.getSeqNo() != seq)
						pfi.setSeqNo(seq);
				}
				else	//	not printed
				{
					if (pfi.isPrinted())
						pfi.setIsPrinted(false);
				}
				//	Not Sorted
				if (pfi.isOrderBy())
					pfi.setIsOrderBy(false);
				if (pfi.getSortNo() != 0)
					pfi.setSortNo(0);
			}
			else if (ColumnName.equals("SeqNo"))
			{
				if (pfi.isPrinted())
					pfi.setIsPrinted(false);
				if (!pfi.isOrderBy())
					pfi.setIsOrderBy(true);
				if (pfi.getSortNo() != 10)
					pfi.setSortNo(10);
			}
			else if (ColumnName.equals("Record_ID"))
			{
				// 	Correct ORDER BY replaced in DataEngine.getPrintDataInfo
				//	CASE WHEN Record_ID=0 AND LevelNo=-1 THEN 99999 ELSE Record_ID END
				if (pfi.isPrinted())
					pfi.setIsPrinted(false);
				if (pfi.isOrderBy())
					pfi.setIsOrderBy(false);
				if (pfi.getSortNo() != 0)
					pfi.setSortNo(0);
			}
			else if (ColumnName.equals("LevelNo"))
			{
				if (pfi.isPrinted())
					pfi.setIsPrinted(false);
				if (!pfi.isOrderBy())
					pfi.setIsOrderBy(true);
				if (pfi.getSortNo() != 20)
					pfi.setSortNo(20);
			}
			else if (ColumnName.equals("Name"))
			{
				if (pfi.getSeqNo() != 10)
					pfi.setSeqNo(10);
				if (!pfi.isPrinted())
					pfi.setIsPrinted(true);
				if (!pfi.isOrderBy())
					pfi.setIsOrderBy(true);
				if (pfi.getSortNo() != 30)
					pfi.setSortNo(30);
			}
			else if (ColumnName.equals("Description"))
			{
				if (pfi.getSeqNo() != 20)
					pfi.setSeqNo(20);
				if (!pfi.isPrinted())
					pfi.setIsPrinted(true);
				if (pfi.isOrderBy())
					pfi.setIsOrderBy(false);
				if (pfi.getSortNo() != 0)
					pfi.setSortNo(0);
			}
			else	//	Not Printed, No Sort
			{
				if (pfi.isPrinted())
					pfi.setIsPrinted(false);
				if (pfi.isOrderBy())
					pfi.setIsOrderBy(false);
				if (pfi.getSortNo() != 0)
					pfi.setSortNo(0);
			}
			pfi.save();
			log.fine(pfi.toString());
		}
		//	set translated to original
		pf.setTranslation();
		//	First one is unsorted - just re-load
		if (createNew)
			pf = MPrintFormat.get (getCtx(), AD_PrintFormat_ID, false);	//	use Cache
		return pf;
	}	//	getPrintFormat

}	//	FinReport
