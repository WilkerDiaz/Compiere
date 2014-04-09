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
 * 	Business Partner Relation
 *	@author Jorg Janke
 */
public class MBPRelation extends X_C_BP_Relation
{
    /** Logger for class MBPRelation */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBPRelation.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Business Partner Relation
	 *	@param ctx context
	 *	@param C_BPartner_ID bp
	 *	@param C_BPartnerRelation_ID related or 0 for all
	 *	@return array of relations
	 */
	public static MBPRelation[] get (Ctx ctx, int C_BPartner_ID, int C_BPartnerRelation_ID)
	{
		ArrayList<MBPRelation> list = new ArrayList<MBPRelation>();
		String sql = "SELECT * FROM C_BP_Relation WHERE C_BPartner_ID=?";
		if (C_BPartnerRelation_ID != 0)	
			sql += " AND C_BPartnerRelation_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_BPartner_ID);
			if (C_BPartnerRelation_ID != 0)
				pstmt.setInt(2, C_BPartnerRelation_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MBPRelation(ctx, rs, null));
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
		MBPRelation[] retValue = new MBPRelation[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	get
	
	/**
	 * 	Get all Business Partner Relations
	 *	@param ctx context
	 *	@param C_BPartner_ID bp or related
	 *	@param C_BPartnerRelation_ID related or bp
	 *	@return array of relations
	 */
	public static MBPRelation[] getAll (Ctx ctx, int C_BPartner_ID, int C_BPartnerRelation_ID)
	{
		ArrayList<MBPRelation> list = new ArrayList<MBPRelation>();
		String sql = "SELECT * FROM C_BP_Relation WHERE C_BPartner_ID IN(?,?)"
			+ " AND C_BPartnerRelation_ID IN(?,?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_BPartner_ID);
			pstmt.setInt(2, C_BPartnerRelation_ID);
			pstmt.setInt(3, C_BPartner_ID);
			pstmt.setInt(4, C_BPartnerRelation_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MBPRelation(ctx, rs, null));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		MBPRelation[] retValue = new MBPRelation[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getAll

	
	/**	Logger						*/
	static CLogger s_log = CLogger.getCLogger(MBPRelation.class);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_BP_Relation_ID id
	 *	@param trx p_trx
	 */
	public MBPRelation(Ctx ctx, int C_BP_Relation_ID, Trx trx)
	{
		super(ctx, C_BP_Relation_ID, trx);
	}	//	MBPRelation

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MBPRelation(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MBPRelation

}	//	MBPRelation
