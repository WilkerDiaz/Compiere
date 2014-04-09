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
 * 	Info Window Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MInfoWindow.java,v 1.2 2006/07/30 00:51:03 jjanke Exp $
 */
public class MInfoWindow extends X_AD_InfoWindow
{
    /** Logger for class MInfoWindow */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MInfoWindow.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_InfoWindow_ID id
	 *	@param trx transaction
	 */
	public MInfoWindow (Ctx ctx, int AD_InfoWindow_ID, Trx trx)
	{
		super (ctx, AD_InfoWindow_ID, trx);
		if (AD_InfoWindow_ID == 0)
		{
			setEntityType (ENTITYTYPE_UserMaintained);	// U
			setIsCustomDefault(false);
		}
	}	//	MInfoWindow

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MInfoWindow (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MInfoWindow
	
	
	/**	The Lines				*/
	private MInfoColumn[] 	m_lines = null;
	/** Table Name				*/
	private String			m_tableName = null;

	/**
	 * 	Get Lines
	 *	@param reload reload data
	 *	@return array of lines
	 */
	public MInfoColumn[] getLines(boolean reload)
	{
		if (m_lines != null && !reload)
			return m_lines;
		String sql = "SELECT * FROM AD_InfoColumn WHERE AD_InfoWindow_ID=? ORDER BY SeqNo";
		ArrayList<MInfoColumn> list = new ArrayList<MInfoColumn>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx ());
			pstmt.setInt (1, getAD_InfoWindow_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MInfoColumn (getCtx(), rs, get_Trx ()));
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		//
		m_lines = new MInfoColumn[list.size ()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getLines

	/**
	 * 	Get Table Name
	 *	@return table name
	 */
	protected String getTableName()
	{
		if (m_tableName == null)
		{
			MTable table = MTable.get (getCtx(), getAD_Table_ID());
			m_tableName = table.get_TableName();
		}
		return m_tableName;
	}	//	getTableName
	
	/**
	 * 	Get SQL for Role
	 *	@param role role
	 *	@return statement
	 */
	public String getSQL (MRole role)
	{
		if (m_lines == null)
			getLines(true);
		
		StringBuffer sql = new StringBuffer("SELECT ");
		for (int i = 0; i < m_lines.length; i++)
		{
			MInfoColumn col = m_lines[i];
			if (i > 0)
				sql.append(",");
			sql.append(col.getSelectClause());
		}
		sql.append(" FROM ").append(getFromClause());

		//	Access
		if (role == null)
			role = MRole.getDefault (getCtx(), false);
		String finalSQL = role.addAccessSQL (sql.toString(), 
			getTableName(), MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		log.finer(finalSQL);
		return finalSQL;
	}	//	getSQL
	
}	//	MInfoWindow
