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
 * 	Data Migration Model
 *	@author Jorg Janke
 */
public class MDataMigration extends X_AD_DataMigration
{
    /** Logger for class MDataMigration */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MDataMigration.class);
	/** */
    private static final long serialVersionUID = -6005960095441778807L;

	/**
	 * 	Get MDataMigration from Cache
	 *	@param ctx context
	 *	@param AD_DataMigration_ID id
	 *	@return MDataMigration
	 */
	public static MDataMigration get(Ctx ctx, int AD_DataMigration_ID) 
	{
		Integer key = Integer.valueOf(AD_DataMigration_ID);
		MDataMigration retValue = s_cache.get(ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MDataMigration(ctx, AD_DataMigration_ID, null);
		if (retValue.get_ID() != 0)
			s_cache.put(key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static final CCache<Integer,MDataMigration> s_cache 
		= new CCache<Integer,MDataMigration>("AD_DataMigration", 20);
	
	/**
	 * 	Get Data Migrations with Entity Type
	 *	@param ctx ctx
	 *	@param entityType entity type
	 *	@return array of data migrations
	 */
	public static MDataMigration[] getWithEntityType (Ctx ctx, String entityType)
	{
		String sql = "SELECT * FROM AD_DataMigration "
			+ "WHERE EntityType=? AND IsActive='Y'";
		ArrayList<MDataMigration> list = new ArrayList<MDataMigration>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, entityType);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MDataMigration(ctx, rs, null));
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
		MDataMigration[] retValue = new MDataMigration[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getWithEntityType
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger(MDataMigration.class);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_DataMigration_ID id
	 *	@param trx p_trx
	 */
	public MDataMigration(Ctx ctx, int AD_DataMigration_ID, Trx trx)
	{
		super(ctx, AD_DataMigration_ID, trx);
		if (AD_DataMigration_ID == 0)
		{
			setDataMigrationType (DATAMIGRATIONTYPE_SystemOnly);
			setEntityType (ENTITYTYPE_UserMaintained);	// U
		//	setName (null);
		}
	}	//	MDataMigration

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MDataMigration(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MDataMigration

	/**	The Entries						*/
	private MDataMigrationEntry[]	m_entries	= null;

											/**
	 * 	Get active Entries
	 *	@param reload reload data
	 *	@return array of lines by table
	 */
	public MDataMigrationEntry[] getEntries(boolean reload)
	{
		if (m_entries != null && !reload)
			return m_entries;
		String sql = "SELECT * FROM AD_DataMigrationEntry "
			+ "WHERE AD_DataMigration_ID=? AND IsActive='Y' "
			+ "ORDER BY AD_Table_ID";
		ArrayList<MDataMigrationEntry> list = new ArrayList<MDataMigrationEntry>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getAD_DataMigration_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MDataMigrationEntry(getCtx(), rs, get_Trx()));
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
		//
		m_entries = new MDataMigrationEntry[list.size()];
		list.toArray(m_entries);
		return m_entries;
	}	//	getEntries
	
	/**
	 * 	Get Where Clause based on Security Settings
	 *	@return optional where clause with " AND "
	 */
	public String getSecurityWhereClause()
	{
		StringBuffer where = new StringBuffer();
		String type = getDataMigrationType();
		int AD_Client_ID = getAD_Client_ID();
		int AD_ClientInclude_ID = getAD_ClientInclude_ID();
		if (DATAMIGRATIONTYPE_SystemOnly.equals(type))
			where.append("AD_Client_ID=0");
		else if (DATAMIGRATIONTYPE_SystemAndTenant.equals(type))
		{
			if (AD_Client_ID == AD_ClientInclude_ID)
				where.append("AD_Client_ID=").append(AD_Client_ID);
			else
				where.append("AD_Client_ID IN (").append(AD_Client_ID)
					.append(",").append(AD_ClientInclude_ID).append(")");
		}
		else	//	Client
		{
			if (AD_Client_ID > 0)
				where.append("AD_Client_ID=").append(AD_Client_ID);
			else if (AD_ClientInclude_ID > 0)
				where.append("AD_ClientInclude_ID=").append(AD_ClientInclude_ID);
			else
			{
				where.append("AD_Client_ID=").append(AD_Client_ID);
				log.warning("No Client ID");
			}
		}
		
		if (where.length() > 0)
			where.insert(0, " AND ");
		return where.toString();
	}	//	getSecurityWhereClause
	
	/**
	 * 	Get the list of valid clients
	 *	@return list of AD_Client_ID
	 */
	public ArrayList<Integer> getClientList()
	{
		ArrayList<Integer> retValue = new ArrayList<Integer>();
		String dmt = getDataMigrationType();
		if (DATAMIGRATIONTYPE_SystemOnly.equals(dmt)
			|| DATAMIGRATIONTYPE_SystemAndTenant.equals(dmt))
			retValue.add(0);
		if (DATAMIGRATIONTYPE_TenantOnly.equals(dmt)
			|| DATAMIGRATIONTYPE_SystemAndTenant.equals(dmt))
		{
			if (getAD_Client_ID() != 0)
				retValue.add(getAD_Client_ID());
			if (getAD_ClientInclude_ID() != 0
				&& getAD_ClientInclude_ID() != getAD_Client_ID())
				retValue.add(getAD_ClientInclude_ID());
		}
		return retValue;
	}	//	getClientList
	

	/**
	 * 	Before Save
	 * 	@param newRecord new
	 * 	@return true if it can be saved
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		//	Temporary restrict to Tenant/System only
		int AD_Client_ID = getAD_Client_ID();
		String dmt = getDataMigrationType();
		if (AD_Client_ID == 0 && !DATAMIGRATIONTYPE_SystemOnly.equals(dmt))
			setDataMigrationType(DATAMIGRATIONTYPE_SystemOnly);
		if (AD_Client_ID != 0  && !DATAMIGRATIONTYPE_TenantOnly.equals(dmt))
			setDataMigrationType(DATAMIGRATIONTYPE_TenantOnly);
		
	    return true;
	}	//	beforeSave
	
	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MDataMigration[")
	    	.append(get_ID())
	        .append("-").append(getName());
	    sb.append("]");
	    return sb.toString();
    }	//	toString
}	//	MDataMigration
