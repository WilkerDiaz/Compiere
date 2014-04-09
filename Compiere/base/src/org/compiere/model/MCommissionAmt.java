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
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 *	Commission Run Amounts
 *	
 *  @author Jorg Janke
 *  @version $Id: MCommissionAmt.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MCommissionAmt extends X_C_CommissionAmt
{
    /** Logger for class MCommissionAmt */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MCommissionAmt.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_CommissionAmt_ID id
	 *	@param trx transaction
	 */
	public MCommissionAmt(Ctx ctx, int C_CommissionAmt_ID, Trx trx)
	{
		super(ctx, C_CommissionAmt_ID, trx);
		if (C_CommissionAmt_ID == 0)
		{
		//	setC_CommissionRun_ID (0);
		//	setC_CommissionLine_ID (0);
			setActualQty (Env.ZERO);
			setCommissionAmt (Env.ZERO);
			setConvertedAmt (Env.ZERO);
		}
	}	//	MCommissionAmt

	/**
	 * 	Parent Constructor
	 *	@param run parent
	 *	@param C_CommissionLine_ID line
	 */
	public MCommissionAmt (MCommissionRun run, int C_CommissionLine_ID)
	{
		this (run.getCtx(), 0, run.get_Trx());
		setClientOrg (run);
		setC_CommissionRun_ID (run.getC_CommissionRun_ID());
		setC_CommissionLine_ID (C_CommissionLine_ID);
	}	//	MCommissionAmt

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MCommissionAmt(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MCommissionAmt

	/**
	 * 	Get Details
	 *	@return array of details
	 */
	public MCommissionDetail[] getDetails()
	{
		String sql = "SELECT * FROM C_CommissionDetail WHERE C_CommissionAmt_ID=?";
		ArrayList<MCommissionDetail> list = new ArrayList<MCommissionDetail>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_CommissionAmt_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MCommissionDetail(getCtx(), rs, get_Trx()));
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
		//	Convert
		MCommissionDetail[] retValue = new MCommissionDetail[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getDetails

	/**
	 * 	Calculate Commission
	 */
	public void calculateCommission()
	{
		MCommissionDetail[] details = getDetails();
		BigDecimal ConvertedAmt = Env.ZERO;
		BigDecimal ActualQty = Env.ZERO;
		for (MCommissionDetail detail : details) {
			BigDecimal amt = detail.getConvertedAmt();
			if (amt == null)
				amt = Env.ZERO;
			ConvertedAmt = ConvertedAmt.add(amt);
			ActualQty = ActualQty.add(detail.getActualQty());
		}
		setConvertedAmt(ConvertedAmt);
		setActualQty(ActualQty);
		//
		MCommissionLine cl = new MCommissionLine(getCtx(), getC_CommissionLine_ID(), get_Trx());
		//	Qty
		BigDecimal qty = getActualQty().subtract(cl.getQtySubtract());
		if (cl.isPositiveOnly() && qty.signum() < 0)
			qty = Env.ZERO;
		qty = qty.multiply(cl.getQtyMultiplier());
		//	Amt
		BigDecimal amt = getConvertedAmt().subtract(cl.getAmtSubtract());
		if (cl.isPositiveOnly() && amt.signum() < 0)
			amt = Env.ZERO;
		amt = amt.multiply(cl.getAmtMultiplier());
		//
		setCommissionAmt(amt.add(qty));
	}	//	calculateCommission
	
	
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
			updateRunHeader();
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
			updateRunHeader();
		return success;
	}	//	afterDelete
	
	/**
	 * 	Update Amt Header
	 */
	private void updateRunHeader()
	{
		MCommissionRun run = new MCommissionRun(getCtx(), getC_CommissionRun_ID(),get_Trx());
		run.updateFromAmt();
		run.save();
	}	//	updateRunHeader
	
	@Override
	protected boolean beforeDelete ()
	{
		//	Delete only if the Invoice is not generated
		
		MCommissionRun CommissionRun = new MCommissionRun (getCtx(), getC_CommissionRun_ID(),get_Trx());
		
		if(CommissionRun.getC_Invoice_ID()!=0)
		{
			//invoice exists, return false
			log.saveError("CommissionRunNotDeleted", Msg.translate(getCtx(),""));
			return false;
		}

		return true;
	}	//	beforeDelete

}	//	MCommissionAmt
