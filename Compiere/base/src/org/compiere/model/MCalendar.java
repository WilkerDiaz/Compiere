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

import org.compiere.util.*;

/**
 *	Calendar Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MCalendar.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MCalendar extends X_C_Calendar
{
    /** Logger for class MCalendar */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MCalendar.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get MCalendar from Cache
	 *	@param ctx context
	 *	@param C_Calendar_ID id
	 *	@return MCalendar
	 */
	public static MCalendar get (Ctx ctx, int C_Calendar_ID)
	{
		Integer key = Integer.valueOf (C_Calendar_ID);
		MCalendar retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MCalendar (ctx, C_Calendar_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get
	
	/**
	 * 	Get Default Calendar for Client
	 *	@param ctx context
	 *	@param AD_Client_ID id
	 *	@return MCalendar
	 */
	public static MCalendar getDefault (Ctx ctx, int AD_Client_ID)
	{
		MClientInfo info = MClientInfo.get(ctx, AD_Client_ID);
		return get (ctx, info.getC_Calendar_ID());
	}	//	getDefault
	
	/**
	 * 	Get Default Calendar for Client
	 *	@param ctx context
	 *	@return MCalendar
	 */
	public static MCalendar getDefault (Ctx ctx)
	{
		return getDefault(ctx, ctx.getAD_Client_ID());
	}	//	getDefault
	
	/**	Cache						*/
	private static final CCache<Integer,MCalendar> s_cache
		= new CCache<Integer,MCalendar>("C_Calendar", 20);
	
	
	/*************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Calendar_ID id
	 *	@param trx transaction
	 */
	public MCalendar (Ctx ctx, int C_Calendar_ID, Trx trx)
	{
		super(ctx, C_Calendar_ID, trx);
	}	//	MCalendar

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MCalendar (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MCalendar

	/**
	 * 	Parent Constructor
	 *	@param client parent
	 */
	public MCalendar (MClient client)
	{
		super(client.getCtx(), 0, client.get_Trx());
		setClientOrg(client);
		setName(client.getName() + " " + Msg.translate(client.getCtx(), "C_Calendar_ID"));
	}	//	MCalendar
	
	/**
	 * 	Create (current) Calendar Year
	 * 	@param locale locale
	 *	@return The Year
	 */
	public MYear createYear(Locale locale)
	{
		if (get_ID() == 0)
			return null;
		MYear year = new MYear (this);
		if (year.save())
			year.createStdPeriods(locale);
		//
		return year;
	}	//	createYear
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MCalendar[");
		sb.append(get_ID()).append("-")
			.append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	MCalendar
