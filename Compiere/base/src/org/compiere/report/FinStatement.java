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
 *  Statement of Account
 *
 *  @author Jorg Janke
 *  @version $Id: FinStatement.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class FinStatement extends SvrProcess
{
	/** AcctSchame Parameter			*/
	private int					p_C_AcctSchema_ID = 0;
	/** Posting Type					*/
	private String				p_PostingType = "A";
	/**	Period Parameter				*/
	private int					p_C_Period_ID = 0;
	private Timestamp			p_DateAcct_From = null;
	private Timestamp			p_DateAcct_To = null;
	/**	Org Parameter					*/
	private int					p_AD_Org_ID = 0;
	/**	Account Parameter				*/
	private int					p_Account_ID = 0;
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
	/** Hierarchy						*/
	private int					p_PA_Hierarchy_ID = 0;

	/**	Parameter Where Clause			*/
	private StringBuffer		m_parameterWhere = new StringBuffer();
	/**	Account							*/ 
	private MElementValue 		m_acct = null;
	/** Accounting Schema				*/
	private MAcctSchema			m_acctSchema = null;
	/** Calendar						*/
	private int					m_C_Calendar_ID = 0;
	/** Balance Aggregation             */
	private int                 p_Fact_Accumulation_ID;
	/**	Start Time						*/
	private long 				m_start = System.currentTimeMillis();
	
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
			else if (name.equals("C_AcctSchema_ID"))
				p_C_AcctSchema_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("PostingType"))
				p_PostingType = (String)element.getParameter(); 
			else if (name.equals("C_Period_ID"))
				p_C_Period_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("DateAcct"))
			{
				p_DateAcct_From = (Timestamp)element.getParameter();
				p_DateAcct_To = (Timestamp)element.getParameter_To();
			}
			else if (name.equals("PA_Hierarchy_ID"))
				p_PA_Hierarchy_ID = element.getParameterAsInt();
			else if (name.equals("AD_Org_ID"))
				p_AD_Org_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("Account_ID"))
				p_Account_ID = ((BigDecimal)element.getParameter()).intValue();
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
			else if (name.equals("Fact_Accumulation_ID"))
				p_Fact_Accumulation_ID = ((BigDecimal)element.getParameter()).intValue();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
		//
		setDateAcct();
		sb.append(" - DateAcct ").append(p_DateAcct_From).append("-").append(p_DateAcct_To);
		log.fine(sb.toString());
	}	//	prepare

	/**
	 * 	Set Start/End Date of Report - if not defined current Month
	 */
	private void setDateAcct()
	{
		//	Get Default Calendar
		m_acctSchema = MAcctSchema.get(getCtx(), p_C_AcctSchema_ID);
	   	m_C_Calendar_ID = MClientInfo.get(getCtx(), m_acctSchema.getAD_Client_ID()).getC_Calendar_ID();
		//	Date defined
		if (p_DateAcct_From != null)
		{
			if (p_DateAcct_To == null)
				p_DateAcct_To = new Timestamp (System.currentTimeMillis());
			return;
		}
		//	Get Date from Period
		if (p_C_Period_ID == 0)
		{
			GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
			cal.setTimeInMillis(System.currentTimeMillis());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.DAY_OF_MONTH, 1);		//	set to first of month
			p_DateAcct_From = new Timestamp (cal.getTimeInMillis());
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DAY_OF_YEAR, -1);		//	last of month
			p_DateAcct_To = new Timestamp (cal.getTimeInMillis());
		}
		else
		{
			MPeriod period = MPeriod.get(getCtx(), p_C_Period_ID);
			p_DateAcct_From = period.getStartDate();
			p_DateAcct_To = period.getEndDate();
			m_C_Calendar_ID = period.getC_Calendar_ID();
		}
	}	//	setDateAcct

	
	
	/**************************************************************************
	 *  Perform process.
	 *  @return Message to be translated
	 */
	@Override
	protected String doIt() throws Exception
	{
		//	Update AcctSchema Balances
		if (p_UpdateBalances)
			FinBalance.updateBalance (getCtx(), p_C_AcctSchema_ID, 
					p_DateAcct_From, get_TrxName(),0, this);

		accum = new MFactAccumulation(getCtx(),p_Fact_Accumulation_ID,get_Trx());
		//	Mandatory C_AcctSchema_ID, PostingType, Fact_Accumulation_ID
		m_parameterWhere.append("C_AcctSchema_ID=").append(p_C_AcctSchema_ID)
			.append(" AND PostingType='").append(p_PostingType).append("'");
		//	Optional Account_ID
		if (p_Account_ID != 0)
			m_parameterWhere.append(" AND ").append(MReportTree.getWhereClause(getCtx(), 
				p_PA_Hierarchy_ID, X_C_AcctSchema_Element.ELEMENTTYPE_Account, p_Account_ID));
		//	Optional Org
		if (p_AD_Org_ID != 0)
			m_parameterWhere.append(" AND ").append(MReportTree.getWhereClause(getCtx(), 
				p_PA_Hierarchy_ID, X_C_AcctSchema_Element.ELEMENTTYPE_Organization, p_AD_Org_ID));
		//	Optional BPartner
		if (p_C_BPartner_ID != 0){
			if(!accum.isBusinessPartner()){
				log.log(Level.SEVERE, "Balance aggregation not summarized by Business partner");
				throw new CompiereUserException("Invalid Balance Aggregation");
			}
			m_parameterWhere.append(" AND ").append(MReportTree.getWhereClause(getCtx(), 
				p_PA_Hierarchy_ID, X_C_AcctSchema_Element.ELEMENTTYPE_BPartner, p_C_BPartner_ID));
		}
		//	Optional Product
		if (p_M_Product_ID != 0){
		    if(!accum.isProduct()){
		    	log.log(Level.SEVERE, "Balance aggregation not summarized by Product");
		    	throw new CompiereUserException("Invalid Balance Aggregation");
		    }
			m_parameterWhere.append(" AND ").append(MReportTree.getWhereClause(getCtx(), 
				p_PA_Hierarchy_ID, X_C_AcctSchema_Element.ELEMENTTYPE_Product, p_M_Product_ID));
		}
		//	Optional Project
		if (p_C_Project_ID != 0){
			if(!accum.isProject()){
				log.log(Level.SEVERE, "Balance aggregation not summarized by Project");
				throw new CompiereUserException("Invalid Balance Aggregation");
			}
			m_parameterWhere.append(" AND ").append(MReportTree.getWhereClause(getCtx(), 
				p_PA_Hierarchy_ID, X_C_AcctSchema_Element.ELEMENTTYPE_Project, p_C_Project_ID));
		}
		//	Optional Activity
		if (p_C_Activity_ID != 0){
			if(!accum.isActivity()){
				log.log(Level.SEVERE, "Balance aggregation not summarized by Activity");
				throw new CompiereUserException("Invalid Balance Aggregation");
			}
			m_parameterWhere.append(" AND ").append(MReportTree.getWhereClause(getCtx(), 
				p_PA_Hierarchy_ID, X_C_AcctSchema_Element.ELEMENTTYPE_Activity, p_C_Activity_ID));
		}
		//	Optional Campaign
		if (p_C_Campaign_ID != 0){
			if(!accum.isCampaign()){
				log.log(Level.SEVERE, "Balance aggregation not summarized by Campaign");
				throw new CompiereUserException("Invalid Balance Aggregation");
			}
			m_parameterWhere.append(" AND C_Campaign_ID=").append(p_C_Campaign_ID);
		}
		//	m_parameterWhere.append(" AND ").append(MReportTree.getWhereClause(getCtx(), 
		//		MAcctSchemaElement.ELEMENTTYPE_Campaign, p_C_Campaign_ID));
		//	Optional Sales Region
		if (p_C_SalesRegion_ID != 0){
			if(!accum.isSalesRegion()){
				log.log(Level.SEVERE, "Balance aggregation not summarized by Sales Region");
				throw new CompiereUserException("Invalid Balance Aggregation");
			}
			m_parameterWhere.append(" AND ").append(MReportTree.getWhereClause(getCtx(), 
				p_PA_Hierarchy_ID, X_C_AcctSchema_Element.ELEMENTTYPE_SalesRegion, p_C_SalesRegion_ID));
		}

		createBalanceLine();
		createDetailLines();

		int AD_PrintFormat_ID = 134;
		if (Ini.isClient())
			getProcessInfo().setTransientObject (MPrintFormat.get (getCtx(), AD_PrintFormat_ID, false));
		else
			getProcessInfo().setSerializableObject(MPrintFormat.get (getCtx(), AD_PrintFormat_ID, false));

		log.fine((System.currentTimeMillis() - m_start) + " ms");
		return "";
	}	//	doIt

	/**
	 * 	Create Beginning Balance Line
	 */
	private void createBalanceLine()
	{
		StringBuffer sb = new StringBuffer ("INSERT INTO T_ReportStatement "
			+ "(AD_PInstance_ID, Fact_Acct_ID, LevelNo,"
			+ "DateAcct, Name, Description,"
			+ "AmtAcctDr, AmtAcctCr, Balance, Qty) ");
		sb.append("SELECT ").append(getAD_PInstance_ID()).append(",0,1,")	//	LevelNo=1
			.append(DB.TO_DATE(p_DateAcct_From, true)).append(",")
			.append(DB.TO_STRING(Msg.getMsg(Env.getCtx(), "BeginningBalance"))).append(",NULL,"
			+ "COALESCE(SUM(AmtAcctDr),0), COALESCE(SUM(AmtAcctCr),0), COALESCE(SUM(AmtAcctDr-AmtAcctCr),0), COALESCE(SUM(Qty),0) "
			+ "FROM Fact_Acct_Balance "
			+ "WHERE ").append(m_parameterWhere).append(" AND Fact_Accumulation_ID = ").append(p_Fact_Accumulation_ID)
			.append(" AND DateAcct < ").append(DB.TO_DATE(p_DateAcct_From));			
		//	Start Beginning of Year
		if (p_Account_ID > 0)
		{
			m_acct = new MElementValue (getCtx(), p_Account_ID, get_TrxName());
			if (!m_acct.isBalanceSheet())
			{
				MPeriod first = MPeriod.getFirstInYear (getCtx(), m_C_Calendar_ID, p_DateAcct_From);
				if (first != null)
					sb.append(" AND DateAcct >= ").append(DB.TO_DATE(first.getStartDate()));
				else
					log.log(Level.SEVERE, "First period not found");
			}
		}
		//
		int no = DB.executeUpdate(get_TrxName(), sb.toString());
		log.fine("#" + no + " (Account_ID=" + p_Account_ID + ")");
		log.finest(sb.toString());
	}	//	createBalanceLine

	/**
	 * 	Create Beginning Balance Line
	 */
	private void createDetailLines()
	{
		StringBuffer sb = new StringBuffer ("INSERT INTO T_ReportStatement "
			+ "(AD_PInstance_ID, Fact_Acct_ID, LevelNo,"
			+ "DateAcct, Name, Description,"
			+ "AmtAcctDr, AmtAcctCr, Balance, Qty) ");
		sb.append("SELECT ").append(getAD_PInstance_ID()).append(",Fact_Acct_ID,2,")	//	LevelNo=2
			.append("DateAcct,NULL,NULL,"
			+ "AmtAcctDr, AmtAcctCr, AmtAcctDr-AmtAcctCr, Qty "
			+ "FROM Fact_Acct "
			+ "WHERE ").append(m_parameterWhere)
			.append(" AND DateAcct BETWEEN ").append(DB.TO_DATE(p_DateAcct_From))
			.append(" AND ").append(DB.TO_DATE(p_DateAcct_To));
		//
		int no = DB.executeUpdate(get_TrxName(), sb.toString());
		log.fine("#" + no);
		log.finest(sb.toString());

		//	Set Name,Description
		String sql_select = "SELECT e.Name, fa.Description "
			+ "FROM Fact_Acct fa"
			+ " INNER JOIN AD_Table t ON (fa.AD_Table_ID=t.AD_Table_ID)"
			+ " INNER JOIN AD_Element e ON (t.TableName||'_ID'=e.ColumnName) "
			+ "WHERE r.Fact_Acct_ID=fa.Fact_Acct_ID";
		//	Translated Version ...
		sb = new StringBuffer ("UPDATE T_ReportStatement r SET (Name,Description)=(")
			.append(sql_select).append(") "
			+ "WHERE Fact_Acct_ID <> 0 AND AD_PInstance_ID=").append(getAD_PInstance_ID());
		//
	   no = DB.executeUpdate(get_TrxName(), sb.toString());
	   log.fine("Name #" + no);
	   log.finest("Name - " + sb);

	}	//	createDetailLines

}	//	FinStatement
