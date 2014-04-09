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
 *	RfQ Topic Subscriber Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRfQTopicSubscriber.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MRfQTopicSubscriber extends X_C_RfQ_TopicSubscriber
{
    /** Logger for class MRfQTopicSubscriber */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MRfQTopicSubscriber.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_RfQ_TopicSubscriber_ID id
	 *	@param trx transaction
	 */
	public MRfQTopicSubscriber (Ctx ctx, int C_RfQ_TopicSubscriber_ID, Trx trx)
	{
		super (ctx, C_RfQ_TopicSubscriber_ID, trx);
	}	//	MRfQTopic_Subscriber

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MRfQTopicSubscriber (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MRfQTopic_Subscriber
	
	/**	Restrictions					*/
	private MRfQTopicSubscriberOnly[] m_restrictions = null;
	
	/**
	 * 	Get Restriction Records
	 *	@param requery requery
	 *	@return arry of onlys
	 */
	public MRfQTopicSubscriberOnly[] getRestrictions (boolean requery)
	{
		if (m_restrictions != null && !requery)
			return m_restrictions;
		
		ArrayList<MRfQTopicSubscriberOnly> list = new ArrayList<MRfQTopicSubscriberOnly>();
		String sql = "SELECT * FROM C_RfQ_TopicSubscriberOnly WHERE C_RfQ_TopicSubscriber_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getC_RfQ_TopicSubscriber_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MRfQTopicSubscriberOnly(getCtx(), rs, get_Trx()));
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
		
		m_restrictions = new MRfQTopicSubscriberOnly[list.size ()];
		list.toArray (m_restrictions);
		return m_restrictions;
	}	//	getRestrictions
	
	
	/**
	 * 	Is the product included?
	 *	@param M_Product_ID product
	 *	@return true if no restrictions or included in "positive" only list
	 */
	public boolean isIncluded (int M_Product_ID)
	{
		//	No restrictions
		if (getRestrictions(false).length == 0)
			return true;
		
		for (MRfQTopicSubscriberOnly restriction : m_restrictions) {
			if (!restriction.isActive())
				continue;
			//	Product
			if (restriction.getM_Product_ID() == M_Product_ID)
				return true;
			//	Product Category
			if (MProductCategory.isCategory(restriction.getM_Product_Category_ID(), M_Product_ID))
				return true;
		}
		//	must be on "positive" list
		return false;
	}	//	isIncluded
	
}	//	MRfQTopicSubscriber
