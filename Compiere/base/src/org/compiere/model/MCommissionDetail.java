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
package org.compiere.model;

import java.math.*;
import java.sql.*;

import org.compiere.util.*;

/**
 *	Commission Run Amount Detail Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MCommissionDetail.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MCommissionDetail extends X_C_CommissionDetail
{
    /** Logger for class MCommissionDetail */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MCommissionDetail.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 *	@param trx transaction
	 */
	public MCommissionDetail (Ctx ctx, int C_CommissionDetail_ID, Trx trx)
	{
		super(ctx, C_CommissionDetail_ID, trx);
		if (C_CommissionDetail_ID == 0)
		{
		//	setC_CommissionRun_ID (0);
		//	setC_CommissionLine_ID (0);
			setActualQty (Env.ZERO);			
			setConvertedAmt (Env.ZERO);
		}
	}	//	MCommissionDetail

	/**
	 * 	Parent Constructor
	 *	@param amt commission amt
	 *	@param C_Currency_ID currency
	 *	@param Amt amount
	 *	@param Qty quantity
	 */
	public MCommissionDetail (MCommissionAmt amt, int C_Currency_ID,
		BigDecimal Amt, BigDecimal Qty)
	{
		super (amt.getCtx(), 0, amt.get_Trx());
		setClientOrg(amt);
		setC_CommissionAmt_ID(amt.getC_CommissionAmt_ID());
		setC_Currency_ID (C_Currency_ID);
		setActualAmt (Amt);
		setActualQty (Qty);
		setConvertedAmt (Env.ZERO);
	}	//	MCommissionDetail

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MCommissionDetail(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MCommissionDetail

	/**
	 * 	Set Line IDs
	 *	@param C_OrderLine_ID order
	 *	@param C_InvoiceLine_ID invoice
	 */
	public void setLineIDs (int C_OrderLine_ID, int C_InvoiceLine_ID)
	{
		if (C_OrderLine_ID != 0)
			setC_OrderLine_ID(C_OrderLine_ID);
		if (C_InvoiceLine_ID != 0)
			setC_InvoiceLine_ID(C_InvoiceLine_ID);
	}	//	setLineIDs

	
	/**
	 * 	Set Converted Amt
	 *	@param date for conversion
	 */
	public void setConvertedAmt (Timestamp date)
	{
		BigDecimal amt = MConversionRate.convertBase(getCtx(), 
			getActualAmt(), getC_Currency_ID(), date, 0, 	//	type
			getAD_Client_ID(), getAD_Org_ID());
		if (amt != null)
			setConvertedAmt(amt);
	}	//	setConvertedAmt

	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!newRecord)
			updateAmtHeader();
		return success;
	}	//	afterSave
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterDelete (boolean success)
	{
		if (success)
			updateAmtHeader();
		return success;
	}	//	afterDelete
	
	/**
	 * 	Update Amt Header
	 */
	private void updateAmtHeader()
	{
		MCommissionAmt amt = new MCommissionAmt(getCtx(), getC_CommissionAmt_ID(), get_Trx());
		amt.calculateCommission();
		amt.save();
	}	//	updateAmtHeader
	
	/**
	 * 	Before Delete
	 *	@return true if no invoice line exists
	 */
	@Override
	protected boolean beforeDelete ()
	{
		//	Delete only if the Invoice is not generated
		
		MCommissionAmt CommissionAmt = new MCommissionAmt(getCtx(),getC_CommissionAmt_ID(),get_Trx());
		MCommissionRun CommissionRun = new MCommissionRun (getCtx(), CommissionAmt.getC_CommissionRun_ID(),get_Trx());
		
		if(CommissionRun.getC_Invoice_ID()!=0)
		{
			//invoice exists, return false
			log.saveError("CommissionRunNotDeleted", Msg.translate(getCtx(),""));
			return false;
		}

		return true;
	}	//	beforeDelete

	
}	//	MCommissionDetail
