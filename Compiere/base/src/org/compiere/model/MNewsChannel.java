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

import org.compiere.util.*;

/**
 * News Channel Model
 * 
 * @author Yves Sandfort
 * @version $Id$
 */
public class MNewsChannel extends X_CM_NewsChannel
{
    /** Logger for class MNewsChannel */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MNewsChannel.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
     * Standard Constructor
     * 
     * @param ctx context
     * @param CM_NewsChannel_ID id
     * @param trx transaction
     */
	public MNewsChannel (Ctx ctx, int CM_NewsChannel_ID, Trx trx)
	{
		super (ctx, CM_NewsChannel_ID, trx);
	} // MNewsChannel

	/**
     * Load Constructor
     * 
     * @param ctx context
     * @param rs result set
     * @param trx transaction
     */
	public MNewsChannel (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	} // MNewsChannel
	
	/**
	 * 	Get News Items
	 *	@param where where clause
	 *	@return array of news items
	 */
	public MNewsItem[] getNewsItems(String where)
	{
		ArrayList<MNewsItem> list = new ArrayList<MNewsItem>();
		String sql = "SELECT * FROM CM_NewsItem WHERE CM_NewsChannel_ID=? AND IsActive='Y'";
		if (where != null && where.length() > 0)
			sql += " AND " + where;
		sql += " ORDER BY pubDate DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, get_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MNewsItem(getCtx(), rs, get_Trx()));
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
		MNewsItem[] retValue = new MNewsItem[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getNewsItems


	
	/**
	 * 	Get rss2 Channel Code
	 *	@param xmlCode xml
	 *	@param showFutureItems future
	 *	@return channel code
	 */
	public StringBuffer get_rss2ChannelCode(StringBuffer xmlCode, boolean showFutureItems) 
	{
		if (this != null)	//	never null ??
		{
			xmlCode.append ("<channel>");
			xmlCode.append ("  <title><![CDATA[" + this.getName ()
				+ "]]></title>");
			xmlCode.append ("  <link>" + this.getLink ()
				+ "</link>");
			xmlCode.append ("  <description><![CDATA["
				+ this.getDescription () + "]]></description>");
			xmlCode.append ("  <language>"
				+ this.getAD_Language () + "</language>");
			xmlCode.append ("  <copyright>" + "" + "</copyright>");
			xmlCode.append ("  <pubDate>"
				+ this.getCreated () + "</pubDate>");
			xmlCode.append ("  <image>");
			xmlCode.append ("    <url>" + "" + "</url>");
			xmlCode.append ("    <title><![CDATA[" + "" + "]]></title>");
			xmlCode.append ("    <link>" + "" + "</link>");
			xmlCode.append ("  </image>");

			String whereClause = "";
			//jz if (!showFutureItems) whereClause = "sysdate>pubdate"; 
			if (!showFutureItems) whereClause = " SYSDATE > pubdate"; 
			MNewsItem[] theseItems = getNewsItems(whereClause);
				
			for (MNewsItem element : theseItems)
				xmlCode=element.get_rss2ItemCode(xmlCode,this);
			xmlCode.append ("</channel>");
		}
		return xmlCode;
	}

	
	/**
	 * 	After Save.
	 * 	Insert
	 * 	- create / update index
	 *	@param newRecord insert
	 *	@param success save success
	 *	@return true if saved
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		reIndex(newRecord);
		return success;
	}	//	afterSave
	

	public void reIndex(boolean newRecord) 
	{
		String [] toBeIndexed = new String[2];
		toBeIndexed[0] = this.getName();
		toBeIndexed[1] = this.getDescription();
		MIndex.reIndex (newRecord, toBeIndexed, getCtx(), getAD_Client_ID(), get_Table_ID(), get_ID(), getCM_WebProject_ID(), this.getUpdated());
	}
}	//	
