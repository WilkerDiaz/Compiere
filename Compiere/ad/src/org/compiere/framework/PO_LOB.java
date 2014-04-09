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
package org.compiere.framework;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;

import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.db.CConnection;
import org.compiere.interfaces.Server;
import org.compiere.util.CLogMgt;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Trx;

/**
 * 	Persistent Object LOB.
 * 	Allows to store LOB remotely
 * 	Currently Oracle specific!
 *
 *  @author Jorg Janke
 *  @version $Id: PO_LOB.java 8776 2010-05-19 17:50:56Z nnayak $
 */
public class PO_LOB implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Constructor
	 *	@param tableName table name
	 *	@param columnName column name
	 *	@param whereClause where
	 *	@param displayType display type
	 *	@param value value
	 */
	public PO_LOB (String tableName, String columnName, String whereClause,
		int displayType, Object value)
	{
		m_tableName = tableName;
		m_columnName = columnName;
		m_whereClause = whereClause;
		m_displayType = displayType;
		m_value = value;
	}	//	PO_LOB

	/**	Logger					*/
	protected static CLogger	log = CLogger.getCLogger (PO_LOB.class);
	/**	Table Name				*/
	private String 		m_tableName;
	/** Column Name				*/
	private String 		m_columnName;
	/** Where Clause			*/
	private String		m_whereClause;

	/** Display Type			*/
	private int			m_displayType;
	/** Data					*/
	private Object 		m_value;

	/**
	 * 	Save LOB
	 * 	@param whereClause clause
	 * 	@param trx p_trx name
	 *	@return true if saved
	 */
	public boolean save (String whereClause, Trx trx)
	{
		m_whereClause = whereClause;
		return save(trx);
	}	//	save

	/**
	 * 	Save LOB.
	 * 	see also org.compiere.session.ServerBean#updateLOB
	 * 	@param trx p_trx name
	 *	@return true if saved
	 */
	public boolean save (Trx trx)
	{
		if (m_value == null
			|| !(m_value instanceof String || m_value instanceof byte[])
			|| m_value instanceof String && m_value.toString().length() == 0
			|| m_value instanceof byte[] && ((byte[])m_value).length == 0
			)
		{
			String sql =   " UPDATE " + m_tableName
						 + " SET " + m_columnName + " = NULL "
						 + " WHERE " + m_whereClause;
			int no = DB.executeUpdate(trx, sql);
			log.fine("save [" + trx + "] #" + no + " - no data - set to null - " + m_value);
			if (no == 0)
				log.warning("[" + trx + "] - not updated - " + sql);
			return true;
		}

		String sql =  " UPDATE " + m_tableName
					+ " SET " + m_columnName +" = ? "
					+ " WHERE " + m_whereClause;

		boolean success = true;
		if (DB.isRemoteObjects())
		{
			log.fine("[" + trx + "] - Remote - " + m_value);
			Server server = CConnection.get().getServer();
			try
			{
				if (server != null)
				{	//	See ServerBean
					success = server.updateLOB (sql.toString(), m_displayType, m_value);
					if (CLogMgt.isLevelFinest())
						log.fine("server => " + success);
					if (success)
						return true;
				}
				log.log(Level.SEVERE, "AppsServer not found");
			}
			catch (RemoteException ex)
			{
				log.log(Level.SEVERE, "AppsServer error", ex);
			}
		}

		log.fine("[" + trx + "] - Local");
		log.finest("[" + trx + "] - Local - " + m_value);
		//	Connection
		Trx p_trx = null;
		if (trx != null)
			p_trx = trx;
		Connection con = null;
		//	Create Connection
		if (p_trx != null)
			con = p_trx.getConnection();
		if (con == null)
			con = DB.createConnection(false, Connection.TRANSACTION_READ_COMMITTED);
		if (con == null)
		{
			log.log(Level.SEVERE, "Could not get Connection");
			return false;
		}

		PreparedStatement pstmt = null;
		success = true;
		try
		{
			pstmt = con.prepareStatement(sql.toString());
			if (m_displayType == DisplayTypeConstants.TextLong)
				pstmt.setString(1, (String)m_value);
			else
				pstmt.setBytes(1, (byte[])m_value);
			int no = pstmt.executeUpdate();
			if (no != 1)
			{
				log.fine("[" + trx + "] - Not updated #" + no + " - " + sql);
				success = false;
			}
		}
		catch (Exception e)
		{
			log.log(Level.FINE, "[" + trx + "] - " + sql, e);
			success = false;
		}
		finally {
			DB.closeStatement(pstmt);
		}

		//	Success - commit local p_trx
		if (success)
		{
			if (p_trx != null)
			{
				p_trx = null;
				con = null;
			}
			else
			{
				try
				{
					con.commit();
					con.close();
					con = null;
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, "[" + trx + "] - commit " , e);
					success = false;
				}
			}
		}
		//	Error - roll back
		if (!success)
		{
			log.severe ("[" + trx + "] - rollback");
			if (p_trx != null)
			{
				p_trx.rollback();
				p_trx = null;
				con = null;
			}
			else
			{
				try
				{
					con.rollback();
					con.close();
					con = null;
				}
				catch (Exception ee)
				{
					log.log(Level.SEVERE, "[" + trx + "] - rollback" , ee);
				}
			}
		}

		//	Clean Connection
		try
		{
			if (con != null)
				con.close();
			con = null;
		}
		catch (Exception e)
		{
			con = null;
		}
		return success;
	}	//	save


	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		String sb = "PO_LOB[" 
					+ m_tableName + "." + m_columnName
					+ ",DisplayType=" + m_displayType
					+ "]";
		return sb.toString();
	}	//	toString

	/**
	 *  Get the lob object
	 *  @return m_value
	 */
	public Object getValue()
	{
		return m_value;
	}  // getValue()

	/**
	 *  Get the prepared statement sql
	 *  @return prepared statement sql string
	 */
	public String getSQL()
	{
		String sql = "UPDATE " + m_tableName + " SET " + m_columnName +
					"=? WHERE " + m_whereClause;
		return sql;
	}  // getSQL()

	/**
	 *  Get the display type
	 *  @return display type
	 */
	public int getDisplayType()
	{
		return m_displayType;
	}  // getDisplayType()

	/**
	 * Get the column name
	 * @return column name
	 */
	public String getColumnName()
	{
		return m_columnName;
	}  // getColumnName()
}	//	PO_LOB
