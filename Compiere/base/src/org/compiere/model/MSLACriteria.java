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

import org.compiere.sla.*;
import org.compiere.util.*;

/**
 *	Service Level Agreement Criteria Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MSLACriteria.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MSLACriteria extends X_PA_SLA_Criteria
{
    /** Logger for class MSLACriteria */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MSLACriteria.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get MSLACriteria from Cache
	 *	@param ctx context
	 *	@param PA_SLA_Criteria_ID id
	 *	@return MSLACriteria
	 */
	public static MSLACriteria get (Ctx ctx, int PA_SLA_Criteria_ID)
	{
		Integer key = Integer.valueOf (PA_SLA_Criteria_ID);
		MSLACriteria retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MSLACriteria (ctx, PA_SLA_Criteria_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static final CCache<Integer,MSLACriteria>	s_cache	= new CCache<Integer,MSLACriteria>("PA_SLA_Criteria", 20);
	
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param PA_SLA_Criteria_ID id
	 *	@param trx transaction
	 */
	public MSLACriteria (Ctx ctx, int PA_SLA_Criteria_ID, Trx trx)
	{
		super (ctx, PA_SLA_Criteria_ID, trx);
	}	//	MSLACriteria

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MSLACriteria (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MSLACriteria

	/**
	 * 	Get Goals of Criteria
	 *	@return array of Goals
	 */
	public MSLAGoal[] getGoals()
	{
		String sql = "SELECT * FROM PA_SLA_Goal "
			+ "WHERE PA_SLA_Criteria_ID=?";
		ArrayList<MSLAGoal> list = new ArrayList<MSLAGoal>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getPA_SLA_Criteria_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MSLAGoal(getCtx(), rs, get_Trx()));
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
		MSLAGoal[] retValue = new MSLAGoal[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getGoals
	
	
	/**
	 * 	Create New Instance of SLA Criteria
	 *	@return instanciated class
	 *	@throws Exception
	 */
	public SLACriteria newInstance() throws Exception
	{
		if (getClassname() == null || getClassname().length() == 0)
			throw new CompiereSystemException("No SLA Criteria Classname");
		
		try
		{
			Class<?> clazz = Class.forName(getClassname());
			SLACriteria retValue = (SLACriteria)clazz.newInstance();
			return retValue;
		}
		catch (Exception e)
		{
			throw new CompiereSystemException("Could not intsnciate SLA Criteria", e);
		}
	}	//	newInstance
	
}	//	MSLACriteria
