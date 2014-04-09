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
 * 	Web Project Domain
 *	
 *  @author Jorg Janke
 *  @version $Id: MWebProjectDomain.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class MWebProjectDomain extends X_CM_WebProject_Domain
{
    /** Logger for class MWebProjectDomain */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MWebProjectDomain.class);
	/**	serialVersionUID	*/
	private static final long serialVersionUID = 5134789895039452551L;

	/** Logger */
	private static CLogger s_log = CLogger.getCLogger (MContainer.class);

	/**
	 * 	Web Project Domain Constructor
	 *	@param ctx context
	 *	@param CM_WebProject_Domain_ID id
	 *	@param trx transaction
	 */
	public MWebProjectDomain (Ctx ctx, int CM_WebProject_Domain_ID, Trx trx)
	{
		super (ctx, CM_WebProject_Domain_ID, trx);
	}	//	MWebProjectDomain
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MWebProjectDomain (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MWebProjectDomain

	/**
	 * 	get WebProjectDomain by Name
	 *	@param ctx
	 *	@param ServerName
	 *	@param trx
	 *	@return ContainerElement
	 */
	public static MWebProjectDomain get(Ctx ctx, String ServerName, Trx trx) {
		MWebProjectDomain thisWebProjectDomain = null;
		String sql = "SELECT * FROM CM_WebProject_Domain WHERE lower(FQDN) LIKE ? ORDER by CM_WebProject_Domain_ID DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setString(1, ServerName);
			rs = pstmt.executeQuery();
			if (rs.next())
				thisWebProjectDomain = (new MWebProjectDomain(ctx, rs, trx));
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
		return thisWebProjectDomain;
	}

	
}	//	MWebProjectDomain
