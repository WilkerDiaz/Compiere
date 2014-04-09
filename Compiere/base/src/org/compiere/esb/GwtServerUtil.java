/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.
 * This program is free software; you can redistribute it and/or modify it
 * under the terms version 2 of the GNU General Public License as published
 * by the Free Software Foundation. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 * You may reach us at: ComPiere, Inc. - http://www.compiere.org/license.html
 * 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA or info@compiere.org 
 *****************************************************************************/
package org.compiere.esb;

import java.sql.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Server Utility Functions
 *	
 *  @author Jorg Janke
 *  @version $Id$
 */
class GwtServerUtil
{
	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger(GwtServerUtil.class);
	
	/**
	 *  Get Number of open Requests
	 *  @return number of requests
	 */
	static int getRequests(Ctx ctx)
	{
		int retValue = 0;
		String requestSQL = MRole.getDefault(ctx, false).addAccessSQL ("SELECT COUNT(1) FROM R_Request "
				+ "WHERE (SalesRep_ID=? OR AD_Role_ID=?) AND Processed='N'"
				+ " AND (DateNextAction IS NULL OR TRUNC(DateNextAction, 'DD') <= TRUNC(SysDate, 'DD'))"  //jz add 'DD' for default
				+ " AND (R_Status_ID IS NULL OR R_Status_ID IN (SELECT R_Status_ID FROM R_Status WHERE IsClosed='N'))",
					"R_Request", false, true);	//	not qualified - RW
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(requestSQL, (Trx) null);
			pstmt.setInt(1, ctx.getAD_User_ID());
			pstmt.setInt(2, ctx.getAD_Role_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = rs.getInt(1);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, requestSQL, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return retValue;
	}	//	getRequests
	
	
	/**
	 *  Get Number of open Notes
	 *  @return number of notes
	 */
	static int getNotes(Ctx ctx)
	{
		int retValue = 0;
		String sql = "SELECT COUNT(1) FROM AD_Note "
			+ "WHERE AD_Client_ID=? AND AD_User_ID IN (0,?)"
			+ " AND Processed='N'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, ctx.getAD_Client_ID());
			pstmt.setInt(2, ctx.getAD_User_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = rs.getInt(1);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return retValue;
	}	//	getNotes

}	//	
