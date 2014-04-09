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

import java.sql.*;

import org.compiere.util.*;

/**
 *	Payment Term Schedule Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MPaySchedule.java,v 1.3 2006/07/30 00:51:04 jjanke Exp $
 */
public class MPaySchedule extends X_C_PaySchedule
{
    /** Logger for class MPaySchedule */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPaySchedule.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_PaySchedule_ID id
	 *	@param trx transaction
	 */
	public MPaySchedule(Ctx ctx, int C_PaySchedule_ID, Trx trx)
	{
		super(ctx, C_PaySchedule_ID, trx);
		if (C_PaySchedule_ID == 0)
		{
		//	setC_PaymentTerm_ID (0);	//	Parent
			setPercentage (Env.ZERO);
			setDiscount (Env.ZERO);
			setDiscountDays (0);
			setGraceDays (0);
			setNetDays (0);
			setIsValid (false);
		}	
	}	//	MPaySchedule

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MPaySchedule(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MPaySchedule

	/**	Parent					*/
	public MPaymentTerm		m_parent = null;
	
	/**
	 * @return Returns the parent.
	 */
	public MPaymentTerm getParent ()
	{
		if (m_parent == null)
			m_parent = new MPaymentTerm (getCtx(), getC_PaymentTerm_ID(), get_Trx());
		return m_parent;
	}	//	getParent
	
	/**
	 * @param parent The parent to set.
	 */
	public void setParent (MPaymentTerm parent)
	{
		m_parent = parent;
	}	//	setParent
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (is_ValueChanged("Percentage"))
		{
			log.fine("beforeSave");
			setIsValid(false);
		}
		return true;
	}	//	beforeSave

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord || is_ValueChanged("Percentage"))
		{
			log.fine("afterSave");
			getParent();
			m_parent.validate();
			m_parent.save();
		}
		return success;
	}	//	afterSave
	
}	//	MPaySchedule
