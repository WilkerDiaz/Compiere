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
 *	Period Control Model	
 *	
 *  @author Jorg Janke
 *  @version $Id: MPeriodControl.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MPeriodControl extends X_C_PeriodControl
{
    /** Logger for class MPeriodControl */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPeriodControl.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_PeriodControl_ID 0
	 *	@param trx transaction
	 */
	public MPeriodControl (Ctx ctx, int C_PeriodControl_ID, Trx trx)
	{
		super(ctx, C_PeriodControl_ID, trx);
		if (C_PeriodControl_ID == 0)
		{
		//	setC_Period_ID (0);
		//	setDocBaseType (null);
			setPeriodAction (PERIODACTION_NoAction);
			setPeriodStatus (PERIODSTATUS_NeverOpened);
		}
	}	//	MPeriodControl

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MPeriodControl (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MPeriodControl

	/**
	 * 	Parent Constructor
	 *	@param period parent
	 *	@param DocBaseType doc base type
	 */
	public MPeriodControl (MPeriod period, String DocBaseType)
	{
		this (period.getCtx(), period.getAD_Client_ID(), period.getC_Period_ID(),  
			DocBaseType, period.get_Trx());
	}	//	MPeriodControl

	/**
	 * 	New Constructor
	 *	@param ctx context
	 *	@param AD_Client_ID client
	 *	@param C_Period_ID period
	 *	@param DocBaseType doc base type
	 *	@param trx transaction
	 */
	public MPeriodControl (Ctx ctx, int AD_Client_ID, int C_Period_ID, 
		String DocBaseType, Trx trx)
	{
		this (ctx, 0, trx);
		setClientOrg(AD_Client_ID, 0);
		setC_Period_ID (C_Period_ID);
		setDocBaseType (DocBaseType);
	}	//	MPeriodControl

	
	/**
	 * 	Is Period closed
	 *	@return true if closed or permanently closed
	 */
	public boolean isClosed()
	{
		return PERIODSTATUS_Closed.equals(getPeriodStatus())
			|| PERIODSTATUS_PermanentlyClosed.equals(getPeriodStatus());
	}	//	isOpen
	
	/**
	 * 	Is Period Open
	 *	@return true if open
	 */
	public boolean isOpen()
	{
		return PERIODSTATUS_Open.equals(getPeriodStatus());
	}	//	isOpen

	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MPeriodControl[");
		sb.append(get_ID()).append(",").append(getDocBaseType())
			.append(",Status=").append(getPeriodStatus())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MPeriodControl

