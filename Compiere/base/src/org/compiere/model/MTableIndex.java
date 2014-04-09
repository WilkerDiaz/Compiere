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
 *	Table Index Model
 *	
 *  @author Jorg Janke
 *  @version $Id$
 */
public class MTableIndex extends X_AD_TableIndex
{
    /** Logger for class MTableIndex */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MTableIndex.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Active Indexes for Table
	 *	@param table table
	 *	@return array of index
	 */
	public static MTableIndex[] get (MTable table)
	{
		ArrayList<MTableIndex> list = new ArrayList<MTableIndex>();
		String sql = "SELECT * FROM AD_TableIndex WHERE AD_Table_ID=? AND IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, table.get_Trx());
			pstmt.setInt (1, table.getAD_Table_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MTableIndex (table.getCtx(), rs, table.get_Trx()));
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		MTableIndex[] retValue = new MTableIndex[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	get
	
	/**
	 * Get table indexes with input sql
	 * @param ctx user context
	 * @param sql query to get the indexes
	 * @return array of tables indexes
	 */
	public static ArrayList<MTableIndex> getTableIndexesByQuery(Ctx ctx, String sql)
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<MTableIndex> list = new ArrayList<MTableIndex>();
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MTableIndex tableIndex = new MTableIndex (ctx, rs, null);
				list.add(tableIndex);
			}
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return list;
	}	//	getTableIndexes
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MTableIndex.class);
	
	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param AD_TableIndex_ID id
	 *	@param trx p_trx
	 */
	public MTableIndex (Ctx ctx, int AD_TableIndex_ID, Trx trx)
	{
		super (ctx, AD_TableIndex_ID, trx);
		if (AD_TableIndex_ID == 0)
		{
		//	setAD_Table_ID (0);
		//	setName (null);
			setEntityType (ENTITYTYPE_UserMaintained);
			setIsUnique (false);
			setIsCreateConstraint(false);
		}
	}	//	MTableIndex
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MTableIndex (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
		m_ddl = createDDL();
	}	//	MTableIndex
	
	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param Name name
	 */
	public MTableIndex (MTable parent, String Name)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg (parent);
		setAD_Table_ID(parent.getAD_Table_ID());
		setEntityType (parent.getEntityType());
		setName(Name);
	}	//	MTableIndex
	
	
	/**	The Lines						*/
	private MIndexColumn[] m_columns = null;
	/** Index Create DDL	*/
	private String		m_ddl = null;

	/**
	 * 	Get Index Columns
	 *	@param reload reload data
	 *	@return array of lines
	 */
	public MIndexColumn[] getColumns(boolean reload)
	{
		if (m_columns != null && !reload)
			return m_columns;
		ArrayList<MIndexColumn> list = new ArrayList<MIndexColumn>();
		String sql = "SELECT * FROM AD_IndexColumn WHERE AD_TableIndex_ID=?"
			+ " ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getAD_TableIndex_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MIndexColumn (getCtx(), rs, get_Trx ()));
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//
		m_columns = new MIndexColumn[list.size ()];
		list.toArray (m_columns);
		return m_columns;
	}	//	getLines

	/**
	 * 	Get Table Name
	 *	@return table name
	 */
	public String getTableName()
	{
		int AD_Table_ID = getAD_Table_ID();
		return MTable.getTableName (getCtx(), AD_Table_ID);
	}	//	getTableName
	
	/**
	 * 	Get SQL DDL
	 *	@return ddl
	 */
	private String createDDL()
	{
		StringBuffer sql = null;
		if (!isCreateConstraint())
		{
			sql = new StringBuffer("CREATE ");
			if (isUnique())
				sql.append ("UNIQUE ");
			sql.append("INDEX ").append (getName())
				.append(" ON ").append(getTableName())
				.append(" (");
			//
			getColumns(false);
			for (int i = 0; i < m_columns.length; i++)
			{
				MIndexColumn ic = m_columns[i];
				if (i > 0)
					sql.append(",");
				sql.append (ic.getColumnName());
			}
			
			sql.append(")");
		}
		else if (isUnique())
		{
			sql = new StringBuffer("ALTER TABLE " + getTableName() + " ADD CONSTRAINT " + getName() + " UNIQUE (");
			getColumns(false);
			for (int i = 0; i < m_columns.length; i++)
			{
				MIndexColumn ic = m_columns[i];
				if (i > 0)
					sql.append(",");
				sql.append (ic.getColumnName());
			}
			
			sql.append(")");
		}
		else
		{
			log.severe("Neither index nor unique constraint with " + getTableName());
			return "";
		}
			
		return sql.toString();
	}	//	createDDL
	
	/**
	 * 	Get SQL Index cerate DDL
	 *	@return sql ddl
	 */
	public String getDDL()
	{
		if (m_ddl == null)
			m_ddl = createDDL();
		return m_ddl;
	}	//	getDDL
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MTableIndex[");
		sb.append (get_ID()).append ("-")
			.append (getName ())
			.append (",AD_Table_ID=").append (getAD_Table_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MTableIndex
