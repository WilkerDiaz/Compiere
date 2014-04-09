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
package org.compiere.process;

import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.common.constants.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Commission Calculation	
 *	
 *  @author Jorg Janke
 *  @version $Id: CommissionCalc.java,v 1.3 2006/09/25 00:59:41 jjanke Exp $
 */
public class CommissionCalc extends SvrProcess
{
	private Timestamp		p_StartDate;
	//
	private Timestamp		m_EndDate;
	private MCommission		m_com;
	//

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("StartDate"))
				p_StartDate = (Timestamp)element.getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message (text with variables)
	 *  @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("C_Commission_ID=" + getRecord_ID() + ", StartDate=" + p_StartDate);
		if (p_StartDate == null)
			p_StartDate = new Timestamp (System.currentTimeMillis());
		m_com = new MCommission (getCtx(), getRecord_ID(), get_TrxName());
		if (m_com.get_ID() == 0)
			throw new CompiereUserException ("No Commission");
			
		//	Create Commission	
		MCommissionRun comRun = new MCommissionRun (m_com);
		setStartEndDate();
		comRun.setStartDate(p_StartDate);		
		//	01-Jan-2000 - 31-Jan-2001 - USD
		SimpleDateFormat format = DisplayType.getDateFormat(DisplayTypeConstants.Date);
		String description = format.format(p_StartDate) 
			+ " - " + format.format(m_EndDate)
			+ " - " + MCurrency.getISO_Code(getCtx(), m_com.getC_Currency_ID());
		comRun.setDescription(description);
		if (!comRun.save())
			throw new CompiereSystemException ("Could not save Commission Run");
		
		MCommissionLine[] lines = m_com.getLines();
		for (MCommissionLine element : lines) {
			//	Amt for Line - Updated By Trigger
			MCommissionAmt comAmt = new MCommissionAmt (comRun, element.getC_CommissionLine_ID());
			if (!comAmt.save())
				throw new CompiereSystemException ("Could not save Commission Amt");
			//
			StringBuffer sql = new StringBuffer();
			if (X_C_Commission.DOCBASISTYPE_Receipt.equals(m_com.getDocBasisType()))
			{
				if (m_com.isListDetails())
				{
					sql.append("SELECT h.C_Currency_ID, (l.LineNetAmt*al.Amount/h.GrandTotal) AS Amt,"
						+ " (l.QtyInvoiced*al.Amount/h.GrandTotal) AS Qty,"
						+ " NULL, l.C_InvoiceLine_ID, p.DocumentNo||'_'||h.DocumentNo,"
						+ " COALESCE(prd.Value,l.Description), h.DateInvoiced "
						+ "FROM C_Payment p"
						+ " INNER JOIN C_AllocationLine al ON (p.C_Payment_ID=al.C_Payment_ID)"
						+ " INNER JOIN C_Invoice h ON (al.C_Invoice_ID = h.C_Invoice_ID)"
						+ " INNER JOIN C_InvoiceLine l ON (h.C_Invoice_ID = l.C_Invoice_ID) "
						+ " LEFT OUTER JOIN M_Product prd ON (l.M_Product_ID = prd.M_Product_ID) "
						+ "WHERE p.DocStatus IN ('CL','CO','RE')"
						+ " AND h.IsSOTrx='Y'"
						+ " AND p.AD_Client_ID = ?"
						+ " AND p.DateTrx BETWEEN ? AND ?");
				}
				else
				{
					sql.append("SELECT h.C_Currency_ID, SUM(l.LineNetAmt*al.Amount/h.GrandTotal) AS Amt,"
						+ " SUM(l.QtyInvoiced*al.Amount/h.GrandTotal) AS Qty,"
						+ " NULL, NULL, NULL, NULL, MAX(h.DateInvoiced) "
						+ "FROM C_Payment p"
						+ " INNER JOIN C_AllocationLine al ON (p.C_Payment_ID=al.C_Payment_ID)"
						+ " INNER JOIN C_Invoice h ON (al.C_Invoice_ID = h.C_Invoice_ID)"
						+ " INNER JOIN C_InvoiceLine l ON (h.C_Invoice_ID = l.C_Invoice_ID) "
						+ "WHERE p.DocStatus IN ('CL','CO','RE')"
						+ " AND h.IsSOTrx='Y'"
						+ " AND p.AD_Client_ID = ?"
						+ " AND p.DateTrx BETWEEN ? AND ?");
				}
			}
			else if (X_C_Commission.DOCBASISTYPE_Order.equals(m_com.getDocBasisType()))
			{
				if (m_com.isListDetails())
				{
					sql.append("SELECT h.C_Currency_ID, l.LineNetAmt, l.QtyOrdered, "
						+ "l.C_OrderLine_ID, NULL, h.DocumentNo,"
						+ " COALESCE(prd.Value,l.Description),h.DateOrdered "
						+ "FROM C_Order h"
						+ " INNER JOIN C_OrderLine l ON (h.C_Order_ID = l.C_Order_ID)"
						+ " LEFT OUTER JOIN M_Product prd ON (l.M_Product_ID = prd.M_Product_ID) "
						+ "WHERE h.DocStatus IN ('CL','CO')"
						+ " AND h.IsSOTrx='Y'"
						+ " AND h.AD_Client_ID = ?"
						+ " AND h.DateOrdered BETWEEN ? AND ?");
				}
				else
				{
					sql.append("SELECT h.C_Currency_ID, SUM(l.LineNetAmt) AS Amt,"
						+ " SUM(l.QtyOrdered) AS Qty, "
						+ "NULL, NULL, NULL, NULL, MAX(h.DateOrdered) "
						+ "FROM C_Order h"
						+ " INNER JOIN C_OrderLine l ON (h.C_Order_ID = l.C_Order_ID) "
						+ "WHERE h.DocStatus IN ('CL','CO')"
						+ " AND h.IsSOTrx='Y'"
						+ " AND h.AD_Client_ID = ?"
						+ " AND h.DateOrdered BETWEEN ? AND ?");
				}
			}
			else 	//	Invoice Basis
			{
				if (m_com.isListDetails())
				{
					sql.append("SELECT h.C_Currency_ID, l.LineNetAmt, l.QtyInvoiced, "
						+ "NULL, l.C_InvoiceLine_ID, h.DocumentNo,"
						+ " COALESCE(prd.Value,l.Description),h.DateInvoiced "
						+ "FROM C_Invoice h"
						+ " INNER JOIN C_InvoiceLine l ON (h.C_Invoice_ID = l.C_Invoice_ID)"
						+ " LEFT OUTER JOIN M_Product prd ON (l.M_Product_ID = prd.M_Product_ID) "
						+ "WHERE h.DocStatus IN ('CL','CO','VO','RE')"
						+ " AND h.IsSOTrx='Y'"
						+ " AND h.AD_Client_ID = ?"
						+ " AND h.DateInvoiced BETWEEN ? AND ?");
				}
				else
				{
					sql.append("SELECT h.C_Currency_ID, SUM(l.LineNetAmt) AS Amt,"
						+ " SUM(l.QtyInvoiced) AS Qty, "
						+ "NULL, NULL, NULL, NULL, MAX(h.DateInvoiced) "
						+ "FROM C_Invoice h"
						+ " INNER JOIN C_InvoiceLine l ON (h.C_Invoice_ID = l.C_Invoice_ID) "
						+ "WHERE h.DocStatus IN ('CL','CO','VO','RE')"
						+ " AND h.IsSOTrx='Y'"
						+ " AND h.AD_Client_ID = ?"
						+ " AND h.DateInvoiced BETWEEN ? AND ?");
				}
			}
			//	CommissionOrders/Invoices
			int SalesRep_ID = 0;
			int C_BPartner_ID_SalesRep = 0;
			if (element.isCommissionOrders())
			{
				MUser[] users = MUser.getOfBPartner(getCtx(), m_com.getC_BPartner_ID());
				if (users == null || users.length == 0)
					throw new CompiereUserException ("Commission Business Partner has no Users/Contact");
				if (users.length == 1)
				{
					SalesRep_ID = users[0].getAD_User_ID();
					sql.append(" AND h.SalesRep_ID= ? ");
				}
				else
				{
					log.warning("Not 1 User/Contact for C_BPartner_ID=" 
						+ m_com.getC_BPartner_ID() + " but " + users.length);
					C_BPartner_ID_SalesRep = m_com.getC_BPartner_ID();
					sql.append(" AND h.SalesRep_ID IN (SELECT AD_User_ID FROM AD_User WHERE C_BPartner_ID= ?)");
				}
			}
			//	Organization
			if (element.getOrg_ID() != 0)
				sql.append(" AND h.AD_Org_ID= ? ");
			//	BPartner
			if (element.getC_BPartner_ID() != 0)
				sql.append(" AND h.C_BPartner_ID= ? ");
			//	BPartner Group
			if (element.getC_BP_Group_ID() != 0)
				sql.append(" AND h.C_BPartner_ID IN "
					+ "(SELECT C_BPartner_ID FROM C_BPartner WHERE C_BP_Group_ID= ? )");
			//	Sales Region
			if (element.getC_SalesRegion_ID() != 0)
				sql.append(" AND h.C_BPartner_Location_ID IN "
					+ "(SELECT C_BPartner_Location_ID FROM C_BPartner_Location WHERE C_SalesRegion_ID= ?) ");
			//	Product
			if (element.getM_Product_ID() != 0)
				sql.append(" AND l.M_Product_ID= ? ");
			//	Product Category
			if (element.getM_Product_Category_ID() != 0)
				sql.append(" AND l.M_Product_ID IN "
					+ "(SELECT M_Product_ID FROM M_Product WHERE M_Product_Category_ID= ?) ");
			//	Grouping
			if (!m_com.isListDetails())
				sql.append(" GROUP BY h.C_Currency_ID");
			//
			log.fine("Line=" + element.getLine() + " - " + sql);
			//
			createDetail(sql.toString(), comAmt,SalesRep_ID,C_BPartner_ID_SalesRep,element.getOrg_ID(),
					element.getC_BPartner_ID(),element.getC_BP_Group_ID(),element.getC_SalesRegion_ID(),
					element.getM_Product_ID(),element.getM_Product_Category_ID());
			comAmt.calculateCommission();
			comAmt.save();
		}	//	for all commission lines
		
	//	comRun.updateFromAmt();
	//	comRun.save();
		
		//	Save Last Run
		m_com.setDateLastRun (p_StartDate);
		m_com.save();
		
		return "@C_CommissionRun_ID@ = " + comRun.getDocumentNo() 
			+ " - " + comRun.getDescription();
	}	//	doIt

	/**
	 * 	Set Start and End Date
	 */
	private void setStartEndDate()
	{
		GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
		cal.setTimeInMillis(p_StartDate.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//	Yearly
		if (X_C_Commission.FREQUENCYTYPE_Yearly.equals(m_com.getFrequencyType()))
		{
			cal.set(Calendar.DAY_OF_YEAR, 1);
			p_StartDate = new Timestamp (cal.getTimeInMillis());
			//
			cal.add(Calendar.YEAR, 1);
			cal.add(Calendar.DAY_OF_YEAR, -1); 
			m_EndDate = new Timestamp (cal.getTimeInMillis());
			
		}
		//	Quarterly
		else if (X_C_Commission.FREQUENCYTYPE_Quarterly.equals(m_com.getFrequencyType()))
		{
			cal.set(Calendar.DAY_OF_MONTH, 1);
			int month = cal.get(Calendar.MONTH);
			if (month < Calendar.APRIL)
				cal.set(Calendar.MONTH, Calendar.JANUARY);
			else if (month < Calendar.JULY)
				cal.set(Calendar.MONTH, Calendar.APRIL);
			else if (month < Calendar.OCTOBER)
				cal.set(Calendar.MONTH, Calendar.JULY);
			else
				cal.set(Calendar.MONTH, Calendar.OCTOBER);
			p_StartDate = new Timestamp (cal.getTimeInMillis());
			//
			cal.add(Calendar.MONTH, 3);
			cal.add(Calendar.DAY_OF_YEAR, -1); 
			m_EndDate = new Timestamp (cal.getTimeInMillis());
		}
		//	Weekly
		else if (X_C_Commission.FREQUENCYTYPE_Weekly.equals(m_com.getFrequencyType()))
		{
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			p_StartDate = new Timestamp (cal.getTimeInMillis());
			//
			cal.add(Calendar.DAY_OF_YEAR, 7); 
			m_EndDate = new Timestamp (cal.getTimeInMillis());
		}
		//	Monthly
		else
		{
			cal.set(Calendar.DAY_OF_MONTH, 1);
			p_StartDate = new Timestamp (cal.getTimeInMillis());
			//
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DAY_OF_YEAR, -1); 
			m_EndDate = new Timestamp (cal.getTimeInMillis());
		}
		log.fine("setStartEndDate = " + p_StartDate + " - " + m_EndDate);
	}	//	setStartEndDate

	/**
	 * 	Create Commission Detail
	 *	@param sql sql statement
	 *	@param comAmt parent
	 */
	private void createDetail (String sql, MCommissionAmt comAmt,int SalesRep_ID,int C_BPartner_ID_SalesRep, 
			int AD_Org_ID,int C_BPartner_ID,int C_BP_Group_ID,int C_SalesRegion_ID,int M_Product_ID, 
			int M_Product_Category_ID)
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int index=1;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(index++, m_com.getAD_Client_ID());
			pstmt.setTimestamp(index++, p_StartDate);
			pstmt.setTimestamp(index++, m_EndDate);
			if(SalesRep_ID !=0)
				pstmt.setInt(index++, SalesRep_ID);
			if(C_BPartner_ID_SalesRep!=0)
				pstmt.setInt(index++, C_BPartner_ID_SalesRep);
			if (AD_Org_ID != 0)
				pstmt.setInt(index++,AD_Org_ID);
			if (C_BPartner_ID != 0)
				pstmt.setInt(index++,C_BPartner_ID);
			if (C_BP_Group_ID != 0)
				pstmt.setInt(index++, C_BP_Group_ID);
			if (C_SalesRegion_ID!= 0)
				pstmt.setInt(index++, C_SalesRegion_ID);
			if (M_Product_ID!= 0)
				pstmt.setInt(index++, M_Product_ID);
			if (M_Product_Category_ID!= 0)
				pstmt.setInt(index++, M_Product_Category_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				//	CommissionAmount, C_Currency_ID, Amt, Qty,
				MCommissionDetail cd = new MCommissionDetail (comAmt,
					rs.getInt(1), rs.getBigDecimal(2), rs.getBigDecimal(3));
					
				//	C_OrderLine_ID, C_InvoiceLine_ID,
				cd.setLineIDs(rs.getInt(4), rs.getInt(5));
				
				//	Reference, Info,
				String s = rs.getString(6);
				if (s != null)
					cd.setReference(s);
				s = rs.getString(7);
				if (s != null)
					cd.setInfo(s);
				
				//	Date
				Timestamp date = rs.getTimestamp(8);
				cd.setConvertedAmt(date);
				
				//
				if (!cd.save())		//	creates memory leak
					throw new IllegalArgumentException ("CommissionCalc - Detail Not saved");
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "createDetail", e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}	//	createDetail

}	//	CommissionCalc
