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
 *	Commission Run
 *	
 *  @author Jorg Janke
 *  @version $Id: MCommissionRun.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class MCommissionRun extends X_C_CommissionRun
{ 
    /** Logger for class MCommissionRun */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MCommissionRun.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_CommissionRun_ID id
	 *	@param trx transaction
	 */
	public MCommissionRun (Ctx ctx, int C_CommissionRun_ID, Trx trx)
	{
		super(ctx, C_CommissionRun_ID, trx);
		if (C_CommissionRun_ID == 0)
		{
		//	setC_Commission_ID (0);
		//	setDocumentNo (null);
		//	setStartDate (new Timestamp(System.currentTimeMillis()));
			setGrandTotal (Env.ZERO);
			setProcessed (false);
		}
	}	//	MCommissionRun

	/**
	 * 	Parent Constructor
	 *	@param commission parent
	 */
	public MCommissionRun (MCommission commission)
	{
		this (commission.getCtx(), 0, commission.get_Trx());
		setClientOrg (commission);
		setC_Commission_ID (commission.getC_Commission_ID());
	}	//	MCommissionRun

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MCommissionRun(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MCommissionRun

	/**
	 * 	Get Amounts
	 *	@return array of amounts
	 */
	public MCommissionAmt[] getAmts()
	{
		String sql = "SELECT * FROM C_CommissionAmt WHERE C_CommissionRun_ID=?";
		ArrayList<MCommissionAmt> list = new ArrayList<MCommissionAmt>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_CommissionRun_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MCommissionAmt(getCtx(), rs, get_Trx()));
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
		MCommissionAmt[] retValue = new MCommissionAmt[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getAmts

	/**
	 * 	Update From Amt
	 */
	public void updateFromAmt()
	{
		MCommissionAmt[] amts = getAmts();
		BigDecimal GrandTotal = Env.ZERO;
		for (MCommissionAmt amt : amts) {
			GrandTotal = GrandTotal.add(amt.getCommissionAmt());
		}
		setGrandTotal(GrandTotal);
	}	//	updateFromAmt
	
	/**
	 * 	Before Delete
	 *	@return true if no invoice line exists
	 */
	@Override
	protected boolean beforeDelete ()
	{
		//	Delete only if the Invoice is not generated
		
		if(getC_Invoice_ID()!=0)
		{
			//invoice exists, return false
			log.saveError("CommissionRunNotDeleted", Msg.translate(getCtx(),""));
			return false;
		}
       
		return true;
	}	//	beforeDelete
	
	
}	//	MCommissionRun
