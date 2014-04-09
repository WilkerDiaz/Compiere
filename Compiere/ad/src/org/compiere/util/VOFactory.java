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
package org.compiere.util;

import java.sql.*;
import java.util.*;
import java.util.logging.*;


/**
 *	Value Object Factory base class
 *	
 *  @author Jorg Janke
 *  @version $Id: VOFactory.java 8756 2010-05-12 21:21:27Z nnayak $
 */
public abstract class VOFactory<VO>
{
	/**	Logger	*/
	protected CLogger log = CLogger.getCLogger (getClass());
	
	/**
	 * 	Get VO 
	 *	@param sql single row sql command
	 *	@param id key parameter
	 *	@return VO
	 */
	protected VO get (String sql, ArrayList<Object>params)
	{
		VO retValue = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			for(int i=0; i<params.size(); i++)
				DB.setParam(pstmt, params.get(i), i+1);
			rs = pstmt.executeQuery ();
			if (rs.next())
				retValue = load(rs);
		}
		catch (Exception e) {
			log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	get

	/**
	 * 	Get all VOs 
	 *	@param sql sql command
	 *	@param id key parameter
	 *	@return Array of VO
	 */
	public ArrayList<VO> getAll (String sql, ArrayList<Object>params)
	{
		ArrayList<VO> list = new ArrayList<VO>(); 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			for(int i=0; i<params.size(); i++)
				DB.setParam(pstmt, params.get(i), i+1);
			rs = pstmt.executeQuery ();
			while (rs.next())
				list.add (load(rs));
		}
		catch (Exception e) {
			log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		log.fine("#" + list.size());
		return list;
	}	//	getAll

	/** 
	 * 	Load from ResultSet
	 * 	@param rs result set 
	 */
	abstract protected VO load (ResultSet rs);

}	//	VOFactory
