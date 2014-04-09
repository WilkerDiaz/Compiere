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
 * 	Client Share Info
 *
 *  @author Jorg Janke
 *  @version $Id: MClientShare.java 8780 2010-05-19 23:08:57Z nnayak $
 */
public class MClientShare extends X_AD_ClientShare
{
    /** Logger for class MClientShare */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MClientShare.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Is Table Client Level Only
	 *	@param AD_Client_ID client
	 *	@param AD_Table_ID table
	 *	@return true if client level only (default false)
	 */
	public static boolean isClientLevelOnly (int AD_Client_ID, int AD_Table_ID)
	{
		Boolean share = isShared(AD_Client_ID, AD_Table_ID);
		if (share != null)
			return share.booleanValue();
		return false;
	}	//	isClientLevel

	/**
	 * 	Is Table Org Level Only
	 *	@param AD_Client_ID client
	 *	@param AD_Table_ID table
	 *	@return true if Org level only (default false)
	 */
	public static boolean isOrgLevelOnly (int AD_Client_ID, int AD_Table_ID)
	{
		Boolean share = isShared(AD_Client_ID, AD_Table_ID);
		if (share != null)
			return !share.booleanValue();
		return false;
	}	//	isOrgLevel

	/**
	 * 	Is Table Shared for Client
	 *	@param AD_Client_ID client
	 *	@param AD_Table_ID table
	 *	@return info or null
	 */
	public static Boolean isShared (int AD_Client_ID, int AD_Table_ID)
	{
		//	Load
		if (s_shares.isEmpty())
		{
			String sql = "SELECT AD_Client_ID, AD_Table_ID, ShareType "
				+ "FROM AD_ClientShare WHERE ShareType<>'x' AND IsActive='Y'";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, (Trx) null);
				rs = pstmt.executeQuery ();
				while (rs.next ())
				{
					int Client_ID = rs.getInt(1);
					int table_ID = rs.getInt(2);
					String key = Client_ID + "_" + table_ID;
					String ShareType = rs.getString(3);
					if (ShareType.equals(SHARETYPE_TenantAllShared))
						s_shares.put(key, Boolean.TRUE);	//	org=0
					else if (ShareType.equals(SHARETYPE_OrgNotShared))
						s_shares.put(key, Boolean.FALSE);	//	org<>0
					else if (ShareType.equals(SHARETYPE_TenantOrOrg))
						;
				}
			}
			catch (Exception e) {
				s_log.log (Level.SEVERE, sql, e);
			}
	        finally {
	        	DB.closeResultSet(rs);
	        	DB.closeStatement(pstmt);
	        }

			if (s_shares.isEmpty())		//	put in something
				s_shares.put("0_0", Boolean.TRUE);
		}	//	load
		String key = AD_Client_ID + "_" + AD_Table_ID;
		return s_shares.get(null, key);
	}	//	load

	/**	Shared Info								*/
	private static final CCache<String,Boolean>	s_shares
		= new CCache<String,Boolean>("AD_ClientShare", 10, 120);	//	2h
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MClientShare.class);

	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param AD_ClientShare_ID id
	 *	@param trx p_trx
	 */
	public MClientShare (Ctx ctx, int AD_ClientShare_ID, Trx trx)
	{
		super (ctx, AD_ClientShare_ID, trx);
	}	//	MClientShare

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MClientShare (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MClientShare

	/**	The Table				*/
	private MTable		m_table = null;

	/**
	 * 	Is Client Level Only
	 *	@return true if client level only (shared)
	 */
	public boolean isClientLevelOnly()
	{
		return getShareType().equals(SHARETYPE_TenantAllShared);
	}	//	isClientLevelOnly

	/**
	 * 	Is Org Level Only
	 *	@return true if org level only (not shared)
	 */
	public boolean isOrgLevelOnly()
	{
		return getShareType().equals(SHARETYPE_OrgNotShared);
	}	//	isOrgLevelOnly

	/**
	 * 	Get Table model
	 *	@return table
	 */
	public MTable getTable()
	{
		if (m_table == null)
			m_table = MTable.get(getCtx(), getAD_Table_ID());
		return m_table;
	}	//	getTable

	/**
	 * 	Get Table Name
	 *	@return table name
	 */
	public String getTableName()
	{
		return getTable().getTableName();
	}	//	getTableName

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (isActive())
		{
			setDataToLevel();
			listChildRecords();
		}
		return true;
	}	//	afterSave

	/**
	 * 	Set Data To Level
	 * 	@return info
	 */
	public String setDataToLevel()
	{
		String info = "-";
		if (isClientLevelOnly())
		{
			StringBuffer sql = new StringBuffer("UPDATE ")
				.append(getTableName())
				.append(" SET AD_Org_ID=0 WHERE AD_Org_ID<>0 AND AD_Client_ID=?");
			int no = DB.executeUpdate(get_Trx(), sql.toString(), getAD_Client_ID());
			info = getTableName() + " set to Shared #" + no;
			log.info(info);
		}
		else if (isOrgLevelOnly())
		{
			StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ")
				.append(getTableName())
				.append(" WHERE AD_Org_ID=0 AND AD_Client_ID=?");
			int no = QueryUtil.getSQLValue(get_Trx(), sql.toString(), getAD_Client_ID());
			info = getTableName() + " Shared records #" + no;
			log.info(info);
		}
		return info;
	}	//	setDataToLevel

	/**
	 * 	List Child Tables
	 *	@return child tables
	 */
	public String listChildRecords()
	{
		StringBuffer info = new StringBuffer();
		String sql = "SELECT AD_Table_ID, TableName "
			+ "FROM AD_Table t "
			+ "WHERE AccessLevel='3' AND IsView='N'"  //jz put quote for typing
			+ " AND EXISTS (SELECT * FROM AD_Column c "
				+ "WHERE t.AD_Table_ID=c.AD_Table_ID"
				+ " AND c.IsParent='Y'"
				+ " AND c.ColumnName IN (SELECT ColumnName FROM AD_Column cc "
					+ "WHERE cc.IsKey='Y' AND cc.AD_Table_ID=?))";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, getAD_Table_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				String TableName = rs.getString(2);
				if (info.length() != 0)
					info.append(", ");
				info.append(TableName);
			}
		}
		catch (Exception e) {
			log.log (Level.SEVERE, sql, e);
		}
        finally {
        	DB.closeResultSet(rs);
        	DB.closeStatement(pstmt);
        }

		log.info(info.toString());
		return info.toString();
	}	//	listChildRecords

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (getAD_Org_ID() != 0)
			setAD_Org_ID(0);

		// For BP table, dont allow Org Share Type (not shared) if BP with Org * already present.
		String tableName = getTableName();
		String shareType = getShareType();
		if("C_BPartner".equals(tableName) && SHARETYPE_OrgNotShared.equals(shareType))
		{
			String sql = "SELECT COUNT(*) FROM C_BPartner WHERE AD_Org_ID=0 AND AD_Client_ID=?";
			int no = QueryUtil.getSQLValue(get_Trx(), sql, getAD_Client_ID());
			if(no > 0)
			{
				//Prevent from saving(dont allow to put a restriction-"not shared" while there are already BPs with Org * present)
				log.saveError("Error", Msg.getMsg(getCtx(), "CannotSetBPToNotShared"));
				return false;
			}
		}

		return true;
	}	//	beforeSave

}	//	MClientShare
