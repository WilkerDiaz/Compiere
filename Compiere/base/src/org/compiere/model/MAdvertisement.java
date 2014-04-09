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
 *	Advertisement Model
 *
 *  @author Jorg Janke
 *  @version $Id: MAdvertisement.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MAdvertisement extends X_W_Advertisement
{
    /** Logger for class MAdvertisement */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAdvertisement.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param W_Advertisement_ID id
	 *	@param trx transaction
	 */
	public MAdvertisement (Ctx ctx, int W_Advertisement_ID, Trx trx)
	{
		super (ctx, W_Advertisement_ID, trx);
		/** if (W_Advertisement_ID == 0)
		{
			setC_BPartner_ID (0);
			setIsSelfService (false);
			setName (null);
			setW_Advertisement_ID (0);
		}
		**/
	}	//	MAdvertisement

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MAdvertisement (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAdvertisement

	/**	Click Count					*/
	private MClickCount		m_clickCount = null;
	/** Counter Count				*/
	private MCounterCount	m_counterCount = null;
	/**	Sales Rep					*/
	private int				m_SalesRep_ID = 0;

	
	/**************************************************************************
	 *	Get ClickCount
	 *	@return Click Count
	 */
	public MClickCount getMClickCount()
	{
		if (getW_ClickCount_ID() == 0)
			return null;
		if (m_clickCount == null)
			m_clickCount = new MClickCount (getCtx(), getW_ClickCount_ID(), get_Trx());
		return m_clickCount;
	}	//	MClickCount

	/**
	 * 	Get Click Target URL (from ClickCount) 
	 *	@return URL
	 */
	public String getClickTargetURL()
	{
		getMClickCount();
		if (m_clickCount == null)
			return "-";
		return m_clickCount.getTargetURL();
	}	//	getClickTargetURL

	/**
	 * 	Set Click Target URL (in ClickCount) 
	 *	@param TargetURL url
	 */
	public void setClickTargetURL(String TargetURL)
	{
		getMClickCount();
		if (m_clickCount == null)
			m_clickCount = new MClickCount(this);
		if (m_clickCount != null)
		{
			m_clickCount.setTargetURL(TargetURL);
			m_clickCount.save(get_Trx());
		}
	}	//	getClickTargetURL
	
	
	/**
	 * 	Get Weekly Count
	 *	@return weekly count
	 */
	public ValueNamePair[] getClickCountWeek ()
	{
		getMClickCount();
		if (m_clickCount == null)
			return new ValueNamePair[0];
		return m_clickCount.getCountWeek();
	}	//	getClickCountWeek


	/**
	 *	Get CounterCount
	 *	@return Counter Count
	 */
	public MCounterCount getMCounterCount()
	{
		if (getW_CounterCount_ID() == 0)
			return null;
		if (m_counterCount == null)
			m_counterCount = new MCounterCount (getCtx(), getW_CounterCount_ID(), get_Trx());
		return m_counterCount;
	}	//	MCounterCount

	/**
	 * 	Get Sales Rep ID.
	 * 	(AD_User_ID of oldest BP user)
	 *	@return Sales Rep ID
	 */
	public int getSalesRep_ID()
	{
		if (m_SalesRep_ID == 0)
		{
			m_SalesRep_ID = getAD_User_ID();
			if (m_SalesRep_ID == 0)
				m_SalesRep_ID = QueryUtil.getSQLValue(get_Trx(),
					"SELECT AD_User_ID FROM AD_User "
					+ "WHERE C_BPartner_ID=? AND IsActive='Y' ORDER BY 1", getC_BPartner_ID());
		}
		return m_SalesRep_ID;
	}	//	getSalesRep_ID

}	//	MAdvertisement
