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
 *	RfQ Topic Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRfQTopic.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MRfQTopic extends X_C_RfQ_Topic
{
    /** Logger for class MRfQTopic */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MRfQTopic.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_RfQ_Topic_ID id
	 *	@param trx transaction
	 */
	public MRfQTopic (Ctx ctx, int C_RfQ_Topic_ID, Trx trx)
	{
		super (ctx, C_RfQ_Topic_ID, trx);
	}	//	MRfQTopic

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MRfQTopic (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MRfQTopic

	/**
	 * 	Get Current Topic Subscribers
	 *	@return array subscribers
	 */
	public MRfQTopicSubscriber[] getSubscribers()
	{
		ArrayList<MRfQTopicSubscriber> list = new ArrayList<MRfQTopicSubscriber>();
		String sql = "SELECT * FROM C_RfQ_TopicSubscriber "
			+ "WHERE C_RfQ_Topic_ID=? AND IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getC_RfQ_Topic_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MRfQTopicSubscriber (getCtx(), rs, get_Trx()));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getSubscribers", e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		MRfQTopicSubscriber[] retValue = new MRfQTopicSubscriber[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getSubscribers
	
}	//	MRfQTopic

