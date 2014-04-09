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
 *	Validate Business Partner
 *	
 *  @author Jorg Janke
 *  @version $Id: BPartnerValidate.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class BPartnerValidate extends SvrProcess
{
	/**	BPartner ID			*/
	int p_C_BPartner_ID = 0;
	/** BPartner Group		*/
	int p_C_BP_Group_ID = 0;	
	/** BPartner Client ID	*/
	int p_AD_Client_ID = 0;

	/**
	 *	Prepare
	 */
	@Override
	protected void prepare ()
	{
		p_C_BPartner_ID = getRecord_ID();
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = element.getParameterAsInt();
			else if (name.equals("C_BP_Group_ID"))
				p_C_BP_Group_ID = element.getParameterAsInt();
			else if (name.equals("AD_Client_ID"))
				p_AD_Client_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	@Override
	protected String doIt () throws Exception
	{
		log.info("C_BPartner_ID=" + p_C_BPartner_ID + ", C_BP_Group_ID=" + p_C_BP_Group_ID); 
		if (p_C_BPartner_ID == 0 && p_C_BP_Group_ID == 0 && p_AD_Client_ID == 0)
			throw new CompiereUserException ("No Business Partner/Group selected");
		
		if (p_AD_Client_ID != 0)
		{
			String sql = "SELECT * FROM C_BPartner WHERE AD_Client_ID=? AND IsActive='Y'";
			checkBP(sql, p_AD_Client_ID);
		}
		else if (p_C_BP_Group_ID == 0)
		{
			MBPartner bp = new MBPartner (getCtx(), p_C_BPartner_ID, get_TrxName());
			if (bp.get_ID() == 0)
				throw new CompiereUserException ("Business Partner not found - C_BPartner_ID=" + p_C_BPartner_ID);
			checkBP (bp);
		}
		else
		{
			String sql = "SELECT * FROM C_BPartner WHERE C_BP_Group_ID=? AND IsActive='Y'";
			checkBP(sql, p_C_BP_Group_ID);
		}
		//
		return "OK";
	}	//	doIt

	/**
	 * Check BP
	 * @param sql prepared statement to get BP
	 * @param value value of the prepared statement parameter
	 */
	private void checkBP(String sql, int value)
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, value);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MBPartner bp = new MBPartner (getCtx(), rs, get_TrxName());
				checkBP (bp);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}  // checkBP()
	
	/**
	 * 	Check BP
	 *	@param bp bp
	 */
	private void checkBP (MBPartner bp)
	{
		addLog(0, null, null, bp.getName() + ":");
		//	See also VMerge.postMerge
		checkPayments(bp);
		checkInvoices(bp);
		//	
		bp.setTotalOpenBalance();
		bp.setActualLifeTimeValue();
		bp.save();
		//
	//	if (bp.getSO_CreditUsed().signum() != 0)
		addLog(0, null, bp.getSO_CreditUsed(), Msg.getElement(getCtx(), "SO_CreditUsed"));
		addLog(0, null, bp.getTotalOpenBalance(), Msg.getElement(getCtx(), "TotalOpenBalance"));
		addLog(0, null, bp.getActualLifeTimeValue(), Msg.getElement(getCtx(), "ActualLifeTimeValue"));
		//
		commit();
	}	//	checkBP
	
	
	/**
	 * 	Check Payments
	 *	@param bp business partner
	 */
	private void checkPayments (MBPartner bp)
	{
		//	See also VMerge.postMerge
		int changed = 0;
		MPayment[] payments = MPayment.getOfBPartner(getCtx(), bp.getC_BPartner_ID(), get_TrxName());
		for (MPayment payment : payments) {
			if (payment.testAllocation())
			{
				payment.save();
				changed++;
			}
		}
		if (changed != 0)
			addLog(0, null, new BigDecimal(payments.length), 
				Msg.getElement(getCtx(), "C_Payment_ID") + " - #" + changed);
	}	//	checkPayments

	/**
	 * 	Check Invoices
	 *	@param bp business partner
	 */
	private void checkInvoices (MBPartner bp)
	{
		//	See also VMerge.postMerge
		int changed = 0;
		MInvoice[] invoices = MInvoice.getOfBPartner(getCtx(), bp.getC_BPartner_ID(), get_TrxName());
		for (MInvoice invoice : invoices) {
			if (invoice.testAllocation())
			{
				invoice.save();
				changed++;
			}
		}
		if (changed != 0)
			addLog(0, null, new BigDecimal(invoices.length), 
				Msg.getElement(getCtx(), "C_Invoice_ID") + " - #" + changed);
	}	//	checkInvoices
	
}	//	BPartnerValidate
