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
import java.text.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.common.constants.*;
import org.compiere.util.*;

/**
 * 	Click Count (header)
 *
 *  @author Jorg Janke
 *  @version $Id: MClickCount.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class MClickCount extends X_W_ClickCount
{
    /** Logger for class MClickCount */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MClickCount.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param W_ClickCount_ID id
	 *	@param trx transaction
	 */
	public MClickCount (Ctx ctx, int W_ClickCount_ID, Trx trx)
	{
		super (ctx, W_ClickCount_ID, trx);
		if (W_ClickCount_ID == 0)
		{
		//	setName (null);
		//	setTargetURL (null);
		}
	}	//	MClickCount
	
	/** 
	 * 	Load Constructor
	 * 	@param ctx context
	 * 	@param rs result set 
	 *	@param trx transaction
	 */
	public MClickCount (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MClickCount

	/** 
	 * 	Parent Constructor
	 * 	@param ad parent
	 */
	public MClickCount (MAdvertisement ad)
	{
		this (ad.getCtx(), 0, ad.get_Trx());
		setName(ad.getName());
		setTargetURL("#");
		setC_BPartner_ID(ad.getC_BPartner_ID());
	}	//	MClickCount
	
	private SimpleDateFormat		m_dateFormat = DisplayType.getDateFormat(DisplayTypeConstants.Date);
	private DecimalFormat			m_intFormat = DisplayType.getNumberFormat(DisplayTypeConstants.Integer);

	
	/**************************************************************************
	 * 	Get Clicks
	 *	@return clicks
	 */
	public MClick[] getMClicks()
	{
		ArrayList<MClick> list = new ArrayList<MClick>();
		/** @todo Clicks */
		//
		MClick[] retValue = new MClick[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getMClicks

	/**
	 * 	Get Count for date format
	 *	@param DateFormat valid TRUNC date format
	 *	@return count
	 */
	protected ValueNamePair[] getCount (String DateFormat)
	{
		ArrayList<ValueNamePair> list = new ArrayList<ValueNamePair>();
		String sql = "SELECT TRUNC(Created, '" + DateFormat + "'), Count(*) "
			+ "FROM W_Click "
			+ "WHERE W_ClickCount_ID=? "
			+ "GROUP BY TRUNC(Created, '" + DateFormat + "')";
		//
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getW_ClickCount_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				String value = m_dateFormat.format(rs.getTimestamp(1));
				String name = m_intFormat.format(rs.getInt(2));
				ValueNamePair pp = new ValueNamePair (value, name);
				list.add(pp);
			}
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql, ex);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//
		ValueNamePair[] retValue = new ValueNamePair[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getCount

	/**
	 * 	Get Monthly Count
	 *	@return monthly count
	 */
	public ValueNamePair[] getCountQuarter ()
	{
		return getCount("Q");
	}	//	getCountQuarter

	/**
	 * 	Get Monthly Count
	 *	@return monthly count
	 */
	public ValueNamePair[] getCountMonth ()
	{
		return getCount("MM");
	}	//	getCountMonth

	/**
	 * 	Get Weekly Count
	 *	@return weekly count
	 */
	public ValueNamePair[] getCountWeek ()
	{
		return getCount("DY");
	}	//	getCountWeek

	/**
	 * 	Get Daily Count
	 *	@return dailt count
	 */
	public ValueNamePair[] getCountDay ()
	{
		return getCount("J");
	}	//	getCountDay

}	//	MClickCount
