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
 *	Organization Info Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MOrgInfo.java 8755 2010-05-12 18:30:14Z nnayak $
 */
public class MOrgInfo extends X_AD_OrgInfo
{	
    /** Logger for class MOrgInfo */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MOrgInfo.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param AD_Org_ID id
	 *	@param p_trx transaction
	 *	@return Org Info
	 */
	public static MOrgInfo get (Ctx ctx, int AD_Org_ID, Trx p_trx)
	{
		MOrgInfo retValue = null;
		String sql = "SELECT * FROM AD_OrgInfo WHERE AD_Org_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, p_trx);
			pstmt.setInt(1, AD_Org_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = new MOrgInfo (ctx, rs, null);
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	get

	/** Static Logger					*/
	private static CLogger		s_log = CLogger.getCLogger (MOrgInfo.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Org_ID id 
	 *	@param trx p_trx
	 */
	public MOrgInfo (Ctx ctx, int AD_Org_ID, Trx trx)
	{
		super(ctx, AD_Org_ID, trx);
	}	//	MOrgInfo
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MOrgInfo (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MOrgInfo

	/**
	 * 	Organization constructor
	 *	@param org org
	 */
	public MOrgInfo (MOrg org)
	{
		super(org.getCtx(), 0, org.get_Trx());
		setClientOrg(org);
		setDUNS ("?");
		setTaxID ("?");
	}	//	MOrgInfo
	
	/**
	 * 	Get Default Org Warehouse
	 *	@return warehouse
	 */
	@Override
	public int getM_Warehouse_ID()
	{
		int M_Warehouse_ID = super.getM_Warehouse_ID();
		if (M_Warehouse_ID != 0)
			return M_Warehouse_ID;
		//
		MWarehouse[] whss = MWarehouse.getForOrg(getCtx(), getAD_Org_ID());
		if (whss.length > 0)
		{
			M_Warehouse_ID = whss[0].getM_Warehouse_ID();
			setM_Warehouse_ID(M_Warehouse_ID);
			return M_Warehouse_ID;
		}
		log.warning("No Warehouse for AD_Org_ID=" + getAD_Org_ID());
		return 0;
	}	//	getM_Warehouse_ID

}	//	MOrgInfo
