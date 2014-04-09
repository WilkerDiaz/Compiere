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
import java.util.*;
import java.util.logging.*;

import org.compiere.common.constants.*;
import org.compiere.util.*;

/**
 *	Price List Version Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MPriceListVersion.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MPriceListVersion extends X_M_PriceList_Version
{
    /** Logger for class MPriceListVersion */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPriceListVersion.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Cinstructor
	 *	@param ctx context
	 *	@param M_PriceList_Version_ID id
	 *	@param trx transaction
	 */
	public MPriceListVersion(Ctx ctx, int M_PriceList_Version_ID, Trx trx)
	{
		super(ctx, M_PriceList_Version_ID, trx);
		if (M_PriceList_Version_ID == 0)
		{
		//	setName (null);	// @#Date@
		//	setM_PriceList_ID (0);
		//	setValidFrom (TimeUtil.getDay(null));	// @#Date@
		//	setM_DiscountSchema_ID (0);
		}
	}	//	MPriceListVersion

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MPriceListVersion(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MPriceListVersion

	/**
	 * 	Parent Constructor
	 *	@param pl parent
	 */
	public MPriceListVersion (MPriceList pl)
	{
		this (pl.getCtx(), 0, pl.get_Trx());
		setClientOrg(pl);
		setM_PriceList_ID(pl.getM_PriceList_ID());
	}	//	MPriceListVersion

	/** Product Prices			*/
	private MProductPrice[] m_pp = null;
	/** Price List				*/
	private MPriceList		m_pl = null;

	/**
	 * 	Get Parent PriceList
	 *	@return price List
	 */
	public MPriceList getPriceList()
	{
		if (m_pl == null && getM_PriceList_ID() != 0)
			m_pl = MPriceList.get (getCtx(), getM_PriceList_ID(), null);
		return m_pl;
	}	//	PriceList
	
	
	/**
	 * 	Get Product Price
	 * 	@param refresh true if refresh
	 *	@return product price
	 */
	public MProductPrice[] getProductPrice (boolean refresh)
	{
		if (m_pp != null && !refresh)
			return m_pp;
		m_pp = getProductPrice(null);
		return m_pp;
	}	//	getProductPrice
	
	/**
	 * 	Get Product Price
	 * 	@param whereClause optional where clause
	 *	@return product price
	 */
	public MProductPrice[] getProductPrice (String whereClause)
	{
		ArrayList<MProductPrice> list = new ArrayList<MProductPrice>();
		String sql = "SELECT * FROM M_ProductPrice WHERE M_PriceList_Version_ID=?";
		if (whereClause != null)
			sql += " " + whereClause;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx ());
			pstmt.setInt (1, getM_PriceList_Version_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MProductPrice(getCtx(), rs, get_Trx()));
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MProductPrice[] pp = new MProductPrice[list.size()];
		list.toArray(pp);
		return pp;
	}	//	getProductPrice
	
	/**
	 * 	Set Name to Valid From Date.
	 * 	If valid from not set, use today
	 */
	public void setName()
	{
		if (getValidFrom() == null)
			setValidFrom (TimeUtil.getDay(null));
		if (getName() == null)
		{
			String name = DisplayType.getDateFormat(DisplayTypeConstants.Date)
				.format(getValidFrom());
			setName(name);
		}
	}	//	setName

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		setName();
		
		Timestamp plvValidFrom = getValidFrom();
		MDiscountSchema ds = new MDiscountSchema (getCtx(), getM_DiscountSchema_ID(), get_Trx());
		Timestamp dsValidFrom = ds.getValidFrom();
		
		if (plvValidFrom.before(dsValidFrom))
		{
			log.saveError("Error", Msg.getMsg(getCtx(), "DiscountSchemaNotValid"));
			return false;						
		}
		
		return true;
	}	//	beforeSave
	
}	//	MPriceListVersion
