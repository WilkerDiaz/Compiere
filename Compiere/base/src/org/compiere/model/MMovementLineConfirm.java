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

import org.compiere.api.UICallout;
import org.compiere.util.*;

/**
 *	Inventory Movement Confirmation Line
 *	
 *  @author Jorg Janke
 *  @version $Id: MMovementLineConfirm.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MMovementLineConfirm extends X_M_MovementLineConfirm
{
    /** Logger for class MMovementLineConfirm */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MMovementLineConfirm.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx ctx
	 *	@param M_MovementLineConfirm_ID id
	 *	@param trx transaction
	 */
	public MMovementLineConfirm (Ctx ctx, int M_MovementLineConfirm_ID, Trx trx)
	{
		super (ctx, M_MovementLineConfirm_ID, trx);
		if (M_MovementLineConfirm_ID == 0)
		{
		//	setM_MovementConfirm_ID (0);	Parent
		//	setM_MovementLine_ID (0);
			setConfirmedQty (Env.ZERO);
			setDifferenceQty (Env.ZERO);
			setScrappedQty (Env.ZERO);
			setTargetQty (Env.ZERO);
			setProcessed (false);
		}	}	//	M_MovementLineConfirm

	/**
	 * 	M_MovementLineConfirm
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MMovementLineConfirm (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	M_MovementLineConfirm

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 */
	public MMovementLineConfirm (MMovementConfirm parent)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setM_MovementConfirm_ID(parent.getM_MovementConfirm_ID());
	}	//	MMovementLineConfirm

	/**	Movement Line			*/
	private MMovementLine 	m_line = null;
	
	/**
	 * 	Set Movement Line
	 *	@param line line
	 */
	public void setMovementLine (MMovementLine line)
	{
		setM_MovementLine_ID(line.getM_MovementLine_ID());
		setTargetQty(line.getMovementQty());
		setConfirmedQty(getTargetQty());	//	suggestion
		m_line = line;
	}	//	setMovementLine
	
	/**
	 * 	Get Movement Line
	 *	@return line
	 */
	public MMovementLine getLine()
	{
		if (m_line == null)
			m_line = new MMovementLine (getCtx(), getM_MovementLine_ID(), get_Trx());
		return m_line;
	}	//	getLine
	
	
	/**
	 * 	Process Confirmation Line.
	 * 	- Update Movement Line
	 *	@return success
	 */
	public boolean processLine ()
	{
		MMovementLine line = getLine();
		
		line.setTargetQty(getTargetQty());
		line.setMovementQty(getConfirmedQty());
		line.setConfirmedQty(getConfirmedQty());
		line.setScrappedQty(getScrappedQty());
		
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
		setDifferenceQty();
		//
		return true;
	}	//	beforeSave

	
}	//	M_MovementLineConfirm
