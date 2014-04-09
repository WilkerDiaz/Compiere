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

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Invoice Aging Report.
 *	Based on RV_Aging.
 *  @author Jorg Janke
 *  @version $Id: Aging.java,v 1.5 2006/10/07 00:58:44 jjanke Exp $
 */
public class Aging extends SvrProcess
{
	/** The date to calculate the days due from			*/
	private Timestamp	p_StatementDate = null;
	private boolean 	p_IsSOTrx = false;
	private int			p_AD_Org_ID = 0;
	private int			p_C_Currency_ID = 0;
	private int			p_C_BP_Group_ID = 0;
	private int			p_C_BPartner_ID = 0;
	private boolean		p_IsListInvoices = false;
	/** Number of days between today and statement date	*/
	private int			m_statementOffset = 0;
	
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
			else if (name.equals("StatementDate"))
				p_StatementDate = (Timestamp)element.getParameter();
			else if (name.equals("IsSOTrx"))
				p_IsSOTrx = "Y".equals(element.getParameter());
			else if (name.equals("C_Currency_ID"))
				p_C_Currency_ID = element.getParameterAsInt();
			else if (name.equals("AD_Org_ID"))
				p_AD_Org_ID = element.getParameterAsInt();
			else if (name.equals("C_BP_Group_ID"))
				p_C_BP_Group_ID = element.getParameterAsInt();
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = element.getParameterAsInt();
			else if (name.equals("IsListInvoices"))
				p_IsListInvoices = "Y".equals(element.getParameter());
			else if (name.equals("#AD_PrintFormat_ID"))
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if (p_StatementDate == null)
			p_StatementDate = new Timestamp (System.currentTimeMillis());
		else
			m_statementOffset = TimeUtil.getDaysBetween( 
				new Timestamp(System.currentTimeMillis()), p_StatementDate);
	}	//	prepare

	/**
	 * 	DoIt
	 *	@return Message
	 *	@throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("StatementDate=" + p_StatementDate + ", IsSOTrx=" + p_IsSOTrx
			+ ", C_Currency_ID=" + p_C_Currency_ID + ",AD_Org_ID=" + p_AD_Org_ID
			+ ", C_BP_Group_ID=" + p_C_BP_Group_ID + ", C_BPartner_ID=" + p_C_BPartner_ID
			+ ", IsListInvoices=" + p_IsListInvoices);
		//
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT bp.C_BP_Group_ID, i.C_BPartner_ID AS C_BPartner_ID,i.C_Invoice_ID AS C_Invoice_ID,NULL AS C_InvoicePaySchedule_ID, " 
			+ "i.C_Currency_ID AS C_Currency_ID, i.IsSOTrx, "								//	5..6
			+ "i.DateInvoiced, p.NetDays, "
			+ "paymentTermDueDate(i.C_PaymentTerm_ID,i.DateInvoiced) AS DueDate, "
			+ "paymentTermDueDays(i.C_PaymentTerm_ID,i.DateInvoiced,getDate()) AS DaysDue, ");		//	7..10
		if (p_C_Currency_ID == 0)
		{
			sql.append("i.GrandTotal, ")			//	11..13
			   .append("CASE WHEN i.IsSOTrx = 'Y' THEN invoicePaid(i.C_Invoice_ID,i.C_Currency_ID,1) ELSE  -invoicePaid(i.C_Invoice_ID,i.C_Currency_ID,1) END AS PaidAmt, ") // PaidAmt
			   .append("invoiceOpenAsOfDate(i.C_Invoice_ID,0,?)  AS OpenAmt"); // OpenAmt
		}
		else
		{
			String s = ",i.C_Currency_ID," + p_C_Currency_ID + ",i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)";
			sql.append("currencyConvert(i.GrandTotal").append(s)		//	11..
				.append(", CASE WHEN i.IsSOTrx = 'Y' THEN currencyConvert(invoicePaid(i.C_Invoice_ID,i.C_Currency_ID,1)").append(s)
				.append("ELSE  currencyConvert(-invoicePaid(i.C_Invoice_ID,i.C_Currency_ID,1) ").append(s).append(" END")
				.append(", currencyConvert(invoiceOpenAsOfDate(i.C_Invoice_ID,0,?)").append(s);
		}
		sql.append(",i.C_Activity_ID,i.C_Campaign_ID,i.C_Project_ID "	//	14
			+ "FROM RV_C_Invoice i"
			+ " INNER JOIN C_PaymentTerm p ON (i.C_PaymentTerm_ID=p.C_PaymentTerm_ID)"
			+ " INNER JOIN C_BPartner bp ON (i.C_BPartner_ID=bp.C_BPartner_ID) "
			+ "WHERE i.ISSoTrx=").append(p_IsSOTrx ? "'Y'" : "'N'")
			.append(" AND invoiceOpenAsOfDate(i.C_Invoice_ID,0,?) <> 0")     
			.append(" AND i.IsPayScheduleValid<>'Y'")     
			.append(" AND i.DocStatus<>'DR' AND i.DocStatus<>'IP' AND i.DocStatus<>'RE' AND i.DocStatus<>'VO'");

		if (p_AD_Org_ID > 0)
			sql.append(" AND i.AD_Org_ID= ? ");
		if (p_C_BPartner_ID > 0)
			sql.append(" AND i.C_BPartner_ID= ? ");
		else if (p_C_BP_Group_ID > 0)
			sql.append(" AND bp.C_BP_Group_ID= ? ");
		
		// UNION
		sql.append(" UNION ");

		sql.append("SELECT bp.C_BP_Group_ID, i.C_BPartner_ID AS C_BPartner_ID,i.C_Invoice_ID AS C_Invoice_ID,ips.C_InvoicePaySchedule_ID, " 
				+ "i.C_Currency_ID AS C_Currency_ID, i.IsSOTrx, "								//	5..6
				+ "i.DateInvoiced, getDaysBetween(ips.DueDate,i.DateInvoiced) AS NetDays, "
				+ "ips.DueDate, "
				+ "getDaysBetween(getDate(),ips.DueDate) AS DaysDue, ");		//	7..10

		if (p_C_Currency_ID == 0)
		{
			sql.append("ips.DueAmt as GrandTotal, ")			//	11..13
			   .append("CASE WHEN invoiceOpenAsOfDate(i.C_Invoice_ID,ips.C_InvoicePaySchedule_ID,?) > 0 " +
			   		"THEN (ips.DueAmt -invoiceOpenAsOfDate(i.C_Invoice_ID,ips.C_InvoicePaySchedule_ID,?)) " +
			   		"ELSE invoicePaid(i.C_Invoice_ID,i.C_Currency_ID,1) END AS PaidAmt, ") // PaidAmt
			   .append("invoiceOpenAsOfDate(i.C_Invoice_ID,ips.C_InvoicePaySchedule_ID,?) AS OpenAmt "); // OpenAmt
		}
		else
		{
			String s = ",i.C_Currency_ID," + p_C_Currency_ID + ",i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)";
			sql.append("currencyConvert(ips.DueAmt").append(s).append(" AS GrandTotal, ")		//	11..
			   .append("CASE WHEN invoiceOpenAsOfDate(i.C_Invoice_ID,ips.C_InvoicePaySchedule_ID,?) > 0" +
				   		"THEN currencyConvert(ips.DueAmt -invoiceOpenAsOfDate(i.C_Invoice_ID,ips.C_InvoicePaySchedule_ID,?)").append(s)
				   		.append(" ELSE currencyConvert(invoicePaid(i.C_Invoice_ID,i.C_Currency_ID,1)").append(s).append(" END AS PaidAmt, ") // PaidAmt
				   .append("currencyConvert(invoiceOpenAsOfDate(i.C_Invoice_ID,ips.C_InvoicePaySchedule_ID,?)").append(s).append(" AS OpenAmt"); // OpenAmt
		}

		sql.append(",i.C_Activity_ID,i.C_Campaign_ID,i.C_Project_ID "	//	14
				+ "FROM RV_C_Invoice i"
				+ " INNER JOIN C_InvoicePaySchedule ips ON (i.C_Invoice_ID=ips.C_Invoice_ID)"
				+ " INNER JOIN C_BPartner bp ON (i.C_BPartner_ID=bp.C_BPartner_ID) "
				+ "WHERE i.ISSoTrx=").append(p_IsSOTrx ? "'Y'" : "'N'")
				.append(" AND invoiceOpenAsOfDate(i.C_Invoice_ID,ips.C_InvoicePaySchedule_ID,?) <> 0")     
				.append(" AND i.IsPayScheduleValid='Y'")     
				.append(" AND i.DocStatus<>'DR' AND i.DocStatus<>'IP' AND i.DocStatus<>'RE' AND i.DocStatus<>'VO'");

			if (p_AD_Org_ID > 0)
				sql.append(" AND i.AD_Org_ID= ? ");
			if (p_C_BPartner_ID > 0)
				sql.append(" AND i.C_BPartner_ID= ? ");
			else if (p_C_BP_Group_ID > 0)
				sql.append(" AND bp.C_BP_Group_ID= ? ");

		sql.append(" ORDER BY C_BPartner_ID, C_Currency_ID, C_Invoice_ID");
		
		log.finest(sql.toString());
		String finalSql = MRole.getDefault(getCtx(), false).addAccessSQL(
			sql.toString(), "i", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);	
		log.finer(finalSql);

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MAging aging = null;
		int counter = 0;
		int index = 1;
		int rows = 0;
		int AD_PInstance_ID = getAD_PInstance_ID();
		try
		{
			pstmt = DB.prepareStatement(finalSql, get_TrxName());
			pstmt.setTimestamp(index++, p_StatementDate);
			pstmt.setTimestamp(index++, p_StatementDate);
			if(p_AD_Org_ID>0)
				pstmt.setInt(index++, p_AD_Org_ID);
			if(p_C_BPartner_ID >0)
				pstmt.setInt(index++, p_C_BPartner_ID);
			if(p_C_BP_Group_ID>0)
				pstmt.setInt(index++, p_C_BP_Group_ID);
			pstmt.setTimestamp(index++, p_StatementDate);
			pstmt.setTimestamp(index++, p_StatementDate);
			pstmt.setTimestamp(index++, p_StatementDate);
			pstmt.setTimestamp(index++, p_StatementDate);
			if(p_AD_Org_ID>0)
				pstmt.setInt(index++, p_AD_Org_ID);
			if(p_C_BPartner_ID >0)
				pstmt.setInt(index++, p_C_BPartner_ID);
			if(p_C_BP_Group_ID>0)
				pstmt.setInt(index++, p_C_BP_Group_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				int C_BP_Group_ID = rs.getInt(1);
				int C_BPartner_ID = rs.getInt(2);
				int C_Invoice_ID = p_IsListInvoices ? rs.getInt(3) : 0;
				int C_InvoicePaySchedule_ID = p_IsListInvoices ? rs.getInt(4) : 0;
				int C_Currency_ID = rs.getInt(5);
				boolean IsSOTrx = "Y".equals(rs.getString(6));
				Timestamp DueDate = rs.getTimestamp(9);
				//	Days Due
				int DaysDue = rs.getInt(10)		//	based on today
					+ m_statementOffset;
				//
				BigDecimal GrandTotal = rs.getBigDecimal(11);
				BigDecimal OpenAmt = rs.getBigDecimal(13);
				//
				int C_Activity_ID = p_IsListInvoices ? rs.getInt(14) : 0;
				int C_Campaign_ID = p_IsListInvoices ? rs.getInt(15) : 0;
				int C_Project_ID = p_IsListInvoices ? rs.getInt(16) : 0;
				
				rows++;
				//	New Aging Row
				if (aging == null 		//	Key
					|| AD_PInstance_ID != aging.getAD_PInstance_ID()
					|| C_BPartner_ID != aging.getC_BPartner_ID()
					|| C_Currency_ID != aging.getC_Currency_ID()
					|| C_Invoice_ID != aging.getC_Invoice_ID()
					|| C_InvoicePaySchedule_ID != aging.getC_InvoicePaySchedule_ID())
				{
					if (aging != null)
					{
						if (aging.save())
							log.fine("#" + ++counter + " - " + aging);
						else
						{
							log.log(Level.SEVERE, "Not saved " + aging);
							break;
						}
					}
					aging = new MAging (getCtx(), AD_PInstance_ID, p_StatementDate, 
						C_BPartner_ID, C_Currency_ID, 
						C_Invoice_ID, C_InvoicePaySchedule_ID, 
						C_BP_Group_ID, DueDate, IsSOTrx, get_TrxName());
					if (p_AD_Org_ID > 0)
						aging.setAD_Org_ID(p_AD_Org_ID);
					aging.setC_Activity_ID(C_Activity_ID);
					aging.setC_Campaign_ID(C_Campaign_ID);
					aging.setC_Project_ID(C_Project_ID);
				}
				//	Fill Buckets
				aging.add (DueDate, DaysDue, GrandTotal, OpenAmt);
			}
			if (aging != null)
			{
				if (aging.save())
					log.fine("#" + ++counter + " - " + aging);
				else
					log.log(Level.SEVERE, "Not saved " + aging);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, finalSql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//	
		log.info("#" + counter + " - rows=" + rows);
		return "";
	}	//	doIt

}	//	Aging

