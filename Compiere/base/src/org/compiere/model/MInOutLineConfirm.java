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
import java.util.logging.Level;

import org.compiere.api.UICallout;
import org.compiere.framework.PO;
import org.compiere.util.*;

/**
 *	Ship Confirmation Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MInOutLineConfirm.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MInOutLineConfirm extends X_M_InOutLineConfirm
{
    /** Logger for class MInOutLineConfirm */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MInOutLineConfirm.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** Static Logger					*/
	private static CLogger		s_log = CLogger.getCLogger (PO.class);

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_InOutLineConfirm_ID id
	 *	@param trx transaction
	 */
	public MInOutLineConfirm (Ctx ctx, int M_InOutLineConfirm_ID, Trx trx)
	{
		super (ctx, M_InOutLineConfirm_ID, trx);
		if (M_InOutLineConfirm_ID == 0)
		{
		//	setM_InOutConfirm_ID (0);
		//	setM_InOutLine_ID (0);
		//	setTargetQty (Env.ZERO);
		//	setConfirmedQty (Env.ZERO);
			setDifferenceQty(Env.ZERO);
			setScrappedQty(Env.ZERO);
			setProcessed (false);
		}
	}	//	MInOutLineConfirm

	/**
	 * 	Load Construvtor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MInOutLineConfirm (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MInOutLineConfirm
	
	/**
	 * 	Parent Construvtor
	 *	@param header parent
	 */
	public MInOutLineConfirm (MInOutConfirm header)
	{
		this (header.getCtx(), 0, header.get_Trx());
		setClientOrg(header);
		setM_InOutConfirm_ID(header.getM_InOutConfirm_ID());
	}	//	MInOutLineConfirm
	

	

	/** Ship Line				*/
	private MInOutLine 	m_line = null;
	
	public static MInOutLineConfirm get (MInOutConfirm confirm, MInOutLine line)
	{
		MInOutLineConfirm cLine = null;
		String sql = "SELECT * "
			+ "FROM M_InOutLineConfirm "
			+ "WHERE M_InOutConfirm_ID = ? "
			+ "AND M_InOutLine_ID = ?";		//	1
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, confirm.get_Trx());
			pstmt.setInt(1, confirm.getM_InOutConfirm_ID());
			pstmt.setInt(2, line.getM_InOutLine_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				cLine = new MInOutLineConfirm(confirm.getCtx(),rs,confirm.get_Trx());
			}
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return cLine;
	}
	
	/**
	 * 	Set Shipment Line
	 *	@param line shipment line
	 */
	public void setInOutLine (MInOutLine line)
	{
		setM_InOutLine_ID(line.getM_InOutLine_ID());
		setTargetQty(line.getMovementQty());	//	Confirmations in Storage UOM	
		setConfirmedQty (getTargetQty());		//	suggestion
		m_line = line;
	}	//	setInOutLine

	/**
	 * 	Get Shipment Line
	 *	@return line
	 */
	public MInOutLine getLine()
	{
		if (m_line == null)
			m_line = new MInOutLine (getCtx(), getM_InOutLine_ID(), get_Trx());
		return m_line;
	}	//	getLine
	
	
	/**
	 * 	Process Confirmation Line.
	 * 	- Update InOut Line
	 * 	@param isSOTrx sales order
	 * 	@param confirmType type
	 *	@return success
	 */
	public boolean processLine (boolean isSOTrx, String confirmType)
	{
		MInOutLine line = getLine();
		
		// Set Confirmed quantity for all Confirmation types
		line.setConfirmedQty(getConfirmedQty());
		
		//	Pick or QA
		if (X_M_InOutConfirm.CONFIRMTYPE_PickQAConfirm.equals(confirmType))
		{
			line.setTargetQty(getTargetQty());
			line.setMovementQty(getConfirmedQty());	//	Entered NOT changed
			line.setPickedQty(getConfirmedQty());
			//
			line.setScrappedQty(getScrappedQty());
		}
		
		//	Ship or Receipt
		else if (X_M_InOutConfirm.CONFIRMTYPE_ShipReceiptConfirm.equals(confirmType))
		{
			line.setTargetQty(getTargetQty());
			BigDecimal qty = getConfirmedQty();
			Boolean isReturnTrx = line.getParent().isReturnTrx();
			
			/* In PO receipts and SO Returns, we have the responsibility 
			 * for scrapped quantity
			 */
			if ((!isSOTrx && !isReturnTrx) || (isSOTrx && isReturnTrx)) 
				qty = qty.add(getScrappedQty());
			line.setMovementQty(qty);				//	Entered NOT changed
			//
			line.setScrappedQty(getScrappedQty());
		}
		
		return line.save(get_Trx());
	}	//	processConfirmation
	
	/**
	 * 	Is Fully Confirmed
	 *	@return true if Target = Confirmed qty
	 */
	public boolean isFullyConfirmed()
	{
		return getTargetQty().compareTo(getConfirmedQty()) == 0;
	}	//	isFullyConfirmed
	
	
	/**
	 * 	Before Delete - do not delete
	 *	@return false 
	 */
	@Override
	protected boolean beforeDelete ()
	{
		log.saveError("Error", Msg.getMsg(getCtx(), "CannotDelete"));
		return false;
	}	//	beforeDelete
	

	/* 
	 * Calculate Difference = Target - Confirmed - Scrapped
	 */
	public void setDifferenceQty() 
	{
		BigDecimal difference = getTargetQty();
		difference = difference.subtract(getConfirmedQty());
		difference = difference.subtract(getScrappedQty());
		super.setDifferenceQty(difference);
	}

	@UICallout public void setScrappedQty (String oldScrappedQty,
			String newScrappedQty, int windowNo) throws Exception
	{
		if (newScrappedQty == null || newScrappedQty.length() == 0)
			return;
		BigDecimal ScrappedQty= new BigDecimal(newScrappedQty);
		super.setScrappedQty(ScrappedQty);
		setDifferenceQty();
	}
	
	@UICallout public void setConfirmedQty (String oldConfirmedQty,
			String newConfirmedQty, int windowNo) throws Exception
	{
		if (newConfirmedQty == null || newConfirmedQty.length() == 0)
			return;
		BigDecimal ConfirmedQty= new BigDecimal(newConfirmedQty);
		super.setConfirmedQty(ConfirmedQty);
		setDifferenceQty();
	}

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		//
		setDifferenceQty();
		return true;
	}	//	beforeSave
	
}	//	MInOutLineConfirm
