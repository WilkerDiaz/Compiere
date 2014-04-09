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

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 * 	Component Registration Trace
 * 	@author Jorg Janke
 */
public class MComponentRegUpdate extends X_AD_ComponentRegUpdate
{
    /** Logger for class MComponentRegUpdate */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MComponentRegUpdate.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Last update ID
	 *	@param AD_ComponentReg_ID registration
	 *	@return last AD_ComponentRegUpdate_ID or 0
	 */
	public static int getLastUpdate_ID (int AD_ComponentReg_ID)
	{
		int AD_ComponentRegUpdate_ID = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT MAX(AD_ComponentRegUpdate_ID) FROM AD_ComponentRegUpdate "
			+ "WHERE AD_ComponentReg_ID=?";
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_ComponentReg_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				AD_ComponentRegUpdate_ID = rs.getInt(1);
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
		return AD_ComponentRegUpdate_ID;
	}	//	getLastUpdate_ID

	/**	Logger						*/
	protected static final CLogger s_log = CLogger.getCLogger(MComponentRegUpdate.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 * 	@param AD_ComponentRegUpdate_ID id
	 * 	@param trx	 p_trx
	 */
	public MComponentRegUpdate(Ctx ctx, int AD_ComponentRegUpdate_ID,
			Trx trx)
	{
		super(ctx, AD_ComponentRegUpdate_ID, trx);
	}	//	MComponentRegUpdate

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MComponentRegUpdate(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MComponentRegUpdate

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 */
	public MComponentRegUpdate(MComponentReg parent, String email,
			int AD_User_ID, int C_BPartner_ID,
			String name, String description, String help, 
			String version, String requireCompiereVersion, 
			String requireComponentVersion,
			BigDecimal SuggestedPrice, String DocumentationText)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		if (parent.getAD_ComponentReg_ID() > 0)
			setAD_ComponentReg_ID(parent.getAD_ComponentReg_ID());
		setEMail(email);
		//
		setName(name);
		parent.setName(name);
		setDescription(description);
		parent.setDescription(description);
		setHelp(help);
		parent.setHelp(help);
		setVersion(version);
		parent.setVersion(version);
		setAD_User_ID(AD_User_ID);
		parent.setAD_User_ID(AD_User_ID);
		setRequireCompiereVersion(requireCompiereVersion);
		parent.setRequireCompiereVersion(requireCompiereVersion);
		setRequireComponentVersion(requireComponentVersion);
		parent.setRequireComponentVersion(requireComponentVersion);
		setSuggestedPrice(SuggestedPrice);
		parent.setSuggestedPrice(SuggestedPrice);
		setDocumentationText(DocumentationText);
		parent.setDocumentationText(DocumentationText);
		//
		parent.setC_BPartner_ID(C_BPartner_ID);
	}	//	MComponentRegUpdate
	
	
	
	
}	//	MComponentRegUpdate
