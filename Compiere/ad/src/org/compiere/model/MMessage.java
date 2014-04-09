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
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Message Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MMessage.java 8755 2010-05-12 18:30:14Z nnayak $
 */
public class MMessage extends X_AD_Message
{
    /** Logger for class MMessage */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MMessage.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Message (cached)
	 *	@param ctx context
	 *	@param Value message value
	 *	@return message
	 */
	public static MMessage get (Ctx ctx, String Value)
	{
		if (Value == null || Value.length() == 0)
			return null;
		MMessage retValue = s_cache.get(ctx, Value);
		//
		if (retValue == null)
		{
			String sql = "SELECT * FROM AD_Message WHERE Value=?";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, (Trx) null);
				pstmt.setString(1, Value);
				rs = pstmt.executeQuery();
				if (rs.next())
					retValue = new MMessage (ctx, rs, null);
			}
			catch (Exception e) {
				s_log.log(Level.SEVERE, "get", e);
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			
			if (retValue != null)
				s_cache.put(Value, retValue);
		}
		return retValue;
	}	//	get

	/**
	 * 	Get Message (cached)
	 *	@param ctx context
	 *	@param AD_Message_ID id
	 *	@return message
	 */
	public static MMessage get (Ctx ctx, int AD_Message_ID)
	{
		String key = String.valueOf(AD_Message_ID);
		MMessage retValue = s_cache.get(ctx, key);
		if (retValue == null)
		{
			retValue = new MMessage (ctx, AD_Message_ID, null);
			s_cache.put(key, retValue);
		}
		return retValue;
	}	//	get
	
	/**
	 * 	Get Message ID (cached)
	 *	@param ctx context
	 *	@param Value message value
	 *	@return AD_Message_ID
	 */
	public static int getAD_Message_ID (Ctx ctx, String Value)
	{
		MMessage msg = get(ctx, Value);
		if (msg == null)
			return 0;
		return msg.getAD_Message_ID();
	}	//	getAD_Message_ID
	
	/**	Cache					*/
	static private final CCache<String,MMessage> s_cache = new CCache<String,MMessage>("AD_Message", 100);
	/** Static Logger					*/
	private static CLogger 	s_log = CLogger.getCLogger(MMessage.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Message_ID id
	 *	@param trx transaction
	 */
	public MMessage (Ctx ctx, int AD_Message_ID, Trx trx)
	{
		super(ctx, AD_Message_ID, trx);
	}	//	MMessage

	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MMessage(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MMessage

	
	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MMessage[").append(get_ID())
	        .append("-").append(getValue());
	    sb.append("]");
	    return sb.toString();
    }	//	toString
    
}	//	MMessage
