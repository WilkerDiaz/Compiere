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

import org.compiere.util.*;

public class MDocTypeGroup  extends X_C_DocTypeGroup
{
    /** Logger for class MDocTypeGroup */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MDocTypeGroup.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_DocTypeCounter_ID id
	 *	@param trx transaction
	 */
	public MDocTypeGroup (Ctx ctx, int C_DocTypeGroup_ID, Trx trx)
	{
		super (ctx, C_DocTypeGroup_ID, trx);
	}	//	MDocTypeGroup

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MDocTypeGroup (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MDocTypeGroup
	
	
	public static boolean includesDocType (int C_DocTypeGroup_ID, int C_DocType_ID, Trx trx)
	{
		String sql = "SELECT 1 FROM C_DocTypeGroupLine WHERE C_DocTypeGroup_ID=? AND C_DocType_ID=? AND IsActive='Y'";
		if (QueryUtil.getSQLValue(trx, sql, C_DocTypeGroup_ID, C_DocType_ID) == 1)
			return true;
		else 
			return false;
	}
	
}
