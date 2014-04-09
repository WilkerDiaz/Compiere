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
 *	POS Terminal definition
 *	
 *  @author Jorg Janke
 *  @version $Id: MPOS.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MPOS extends X_C_POS
{
    /** Logger for class MPOS */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPOS.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get POS from Cache
	 *	@param ctx context
	 *	@param C_POS_ID id
	 *	@return MPOS
	 */
	public static MPOS get (Ctx ctx, int C_POS_ID)
	{
		Integer key = Integer.valueOf (C_POS_ID);
		MPOS retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MPOS (ctx, C_POS_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static final CCache<Integer,MPOS> s_cache = new CCache<Integer,MPOS>("C_POS", 20);

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_POS_ID id
	 *	@param trx transaction
	 */
	public MPOS (Ctx ctx, int C_POS_ID, Trx trx)
	{
		super (ctx, C_POS_ID, trx);
		if (C_POS_ID == 0)
		{
		//	setName (null);
		//	setSalesRep_ID (0);
		//	setC_CashBook_ID (0);
		//	setM_PriceList_ID (0);
			setIsModifyPrice (false);	// N
		//	setM_Warehouse_ID (0);
		}	
	}	//	MPOS

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MPOS (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MPOS
	
	/**	Cash Business Partner			*/
	private MBPartner	m_template = null;
	
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		//	Org Consistency
		if (newRecord 
			|| is_ValueChanged("C_CashBook_ID") || is_ValueChanged("M_Warehouse_ID"))
		{
			MCashBook cb = MCashBook.get(getCtx(), getC_CashBook_ID());
			if (cb.getAD_Org_ID() != getAD_Org_ID())
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), "@AD_Org_ID@: @C_CashBook_ID@"));
				return false;
			}
			MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
			if (wh.getAD_Org_ID() != getAD_Org_ID())
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), "@AD_Org_ID@: @M_Warehouse_ID@"));
				return false;
			}
		}
		return true;
	}	//	beforeSave

	
	/**
	 * 	Get default Cash BPartner
	 *	@return BPartner
	 */
	public MBPartner getBPartner()
	{
		if (m_template == null)
		{
			if (getC_BPartnerCashTrx_ID() == 0)
				m_template = MBPartner.getBPartnerCashTrx (getCtx(), getAD_Client_ID());
			else
				m_template = new MBPartner(getCtx(), getC_BPartnerCashTrx_ID(), get_Trx());
			log.fine("getBPartner - " + m_template);
		}
		return m_template;
	}	//	getBPartner
	
}	//	MPOS
