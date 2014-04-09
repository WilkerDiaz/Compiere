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

import javax.servlet.http.*;

import org.compiere.util.*;

/**
 * Container Model
 * 
 * @author Yves Sandfort
 * @version $Id$
 */
public class MAd extends X_CM_Ad
{
    /** Logger for class MAd */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAd.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Standard constructor for AD
	 * @param ctx Context
	 * @param CM_Ad_ID ID
	 * @param trx Transaction
	 */
	public MAd (Ctx ctx, int CM_Ad_ID, Trx trx)
	{
		super (ctx, CM_Ad_ID, trx);
	}
	
	/**
	 * Standard constructor for AD
	 * @param ctx Context
	 * @param rs ResultSet
	 * @param trx Transaction
	 */
	public MAd (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}

	/**
	 * Get's the relevant current Impression value which is Actual+Start
	 * @return int
	 */
	public int getCurrentImpression() {
		return getActualImpression() + getStartImpression();
	}
	
	/**
	 * Adds an Impression to the current Ad
	 * We will deactivate the Ad as soon as one of the Max Criterias are fullfiled
	 */
	public void addImpression() {
		setActualImpression(getActualImpression()+1);
		if (getMaxImpression()>0 && getCurrentImpression()>=getMaxImpression()) 
			setIsActive(false);
		save();
	}
	
	/**	Logger			*/
	private static CLogger s_log = CLogger.getCLogger(MAd.class);

	/**
	 * Get Next of this Category, this Procedure will return the next Ad in a category and expire it if needed
	 * @param ctx Context
	 * @param CM_Ad_Cat_ID Category
	 * @param trx Transaction
	 * @return MAd
	 */
	public static MAd getNext(Ctx ctx, int CM_Ad_Cat_ID, Trx trx) 
	{
		MAd thisAd = null;
		String sql = "SELECT * FROM CM_Ad WHERE IsActive='Y' AND (ActualImpression+StartImpression<MaxImpression OR MaxImpression=0) AND CM_Ad_Cat_ID=? ORDER BY ActualImpression+StartImpression";

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt(1, CM_Ad_Cat_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				thisAd = new MAd(ctx, rs, trx);
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		if (thisAd!=null) 
			thisAd.addImpression();
		return thisAd;
	}
	
	/**
	 * Add Click Record to Log
	 * @param request ServletReqeust
	 */
	public void addClick(HttpServletRequest request) {
		setActualClick(getActualClick()+1);
		if (getActualClick()>getMaxClick()) 
			setIsActive(true);
		save();
	}
} // MAd
