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
package org.compiere.util;

import java.math.*;
import java.rmi.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.sql.*;

import org.compiere.common.CompiereStateException;
import org.compiere.db.*;
import org.compiere.interfaces.*;

/**
 *	Compiere Statement
 *	
 *  @author Jorg Janke
 *  @version $Id: CStatement.java 8244 2009-12-04 23:25:29Z freyes $
 */
@Deprecated
public class CStatement implements Statement
{
	/**
	 * Stores the borrowed connection.
	 * If trx is null, we borrow a connection from the pool, and close it when the statement is closed.
	 * 
	 */
	private final Connection m_conn;
	
	
	/**
	 *	Prepared Statement Constructor
	 *
	 *  @param resultSetType - ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.TYPE_SCROLL_SENSITIVE
	 *  @param resultSetConcurrency - ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
	 * 	@param trx transaction name or null
	 */
	CStatement (int resultSetType, int resultSetConcurrency,
		Trx trx)
	{
		p_vo = new CStatementVO (resultSetType, resultSetConcurrency);

		Trx p_trx = trx;
		Connection conn = null;
		Statement stmt = null;
		//	Local access
		if (!DB.isRemoteObjects())
		{
			try
			{
				if (p_trx != null)
					conn = p_trx.getConnection();
				else
				{
					conn = DB.getCachedConnection();
					if (resultSetConcurrency == ResultSet.CONCUR_UPDATABLE)
						conn.setAutoCommit(true);
					else
						conn.setAutoCommit(false);
				}
				if (conn == null)
					throw new DBException("No Connection");
				stmt = conn.createStatement(resultSetType, resultSetConcurrency);
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, "CStatement", e);
			}
		}
		
		m_stmt = stmt;

		if( p_trx == null )
			m_conn = conn;
		else
			m_conn = null;
	}	//	CPreparedStatement

	/**
	 * 	Minimum Constructor for sub classes
	 */
	protected CStatement()
	{
		super();
		m_conn = null;
		m_stmt = null;
	}	//	CStatement

	/**
	 * 	Remote Constructor
	 *	@param vo value object
	 */
	public CStatement (CStatementVO vo)
	{
		p_vo = vo;
		m_conn = null;
		m_stmt = null;
	}	//	CPreparedStatement


	/**	Logger							*/
	protected transient CLogger			log = CLogger.getCLogger (getClass());
	/** Used if local					*/
	protected transient Statement	m_stmt ;
	/**	Value Object					*/
	protected CStatementVO				p_vo = null;
	/** Remote Errors					*/
	protected int						p_remoteErrors = 0;


	/**
	 * 	Execute Query
	 * 	@param sql0 unconverted SQL to execute
	 * 	@return ResultSet or RowSet
	 * 	@throws SQLException
	 * @see java.sql.Statement#executeQuery(String)
	 */
	public ResultSet executeQuery (String sql0) throws SQLException
	{
		//	Convert SQL
		p_vo.setSql(DB.getDatabase().convertStatement(sql0));
		if (m_stmt != null)	//	local
			return m_stmt.executeQuery(p_vo.getSql());
			
		//	Client -> remote sever
		log.finest("server => " + p_vo + ", Remote=" + DB.isRemoteObjects());
		try
		{
			boolean remote = DB.isRemoteObjects() && CConnection.get().isAppsServerOK(false);
			if (remote && p_remoteErrors > 1)
				remote = CConnection.get().isAppsServerOK(true);
			if (remote)
			{
				Server server = CConnection.get().getServer();
				if (server != null)
				{
					ResultSet rs = server.stmt_getRowSet (p_vo);
					if (rs == null)
						log.warning("ResultSet is null - " + p_vo);
					else
						p_remoteErrors = 0;
					return rs;
				}
				log.log(Level.SEVERE, "AppsServer not found");
				p_remoteErrors++;
			}
		}
		catch (RemoteException ex)
		{
			log.log(Level.SEVERE, "AppsServer error", ex);
			p_remoteErrors++;
		}
		//	Try locally
		log.warning("execute locally");
		Trx trx = Trx.getAutoCommitTrx();
		Statement stmt = local_getStatement (trx.getConnection());	// shared connection
		ResultSet rs = stmt.executeQuery(p_vo.getSql());
		RowSet rowSet = CCachedRowSet.getRowSet(rs);
		rs.close();
		stmt.close();
		trx.close();
		return rowSet;
	}	//	executeQuery


	/**
	 * 	Execute Update
	 *	@param sql0 unconverted sql
	 *	@return no of updated rows
	 *	@throws SQLException
	 * @see java.sql.Statement#executeUpdate(String)
	 */
	public int executeUpdate (String sql0) throws SQLException
	{
		//	Convert SQL
		p_vo.setSql(DB.getDatabase().convertStatement(sql0));
		if (m_stmt != null)	//	local
			return m_stmt.executeUpdate (p_vo.getSql());

		//	Client -> remote sever
		log.finest("server => " + p_vo + ", Remote=" + DB.isRemoteObjects());
		try
		{
			boolean remote = DB.isRemoteObjects() && CConnection.get().isAppsServerOK(false);
			if (remote && p_remoteErrors > 1)
				remote = CConnection.get().isAppsServerOK(true);
			if (remote)
			{
				Server server = CConnection.get().getServer();
				if (server != null)
				{
					int result = server.stmt_executeUpdate(p_vo);
					p_vo.clearParameters();		//	re-use of result set
					return result;
				}
				log.log(Level.SEVERE, "AppsServer not found");
				p_remoteErrors++;
			}
		}
		catch (RemoteException ex)
		{
			log.log(Level.SEVERE, "AppsServer error", ex);
			p_remoteErrors++;
		}
		//	Try locally
		log.warning("execute locally");
		Trx trx = Trx.getAutoCommitTrx();
		Statement pstmt = local_getStatement (trx.getConnection());	//	shared connection
		// FIXME:  looks like there is a statement leak here
		int result = pstmt.executeUpdate(p_vo.getSql());
		pstmt.close();
		trx.close();
		return result;
	}	//	executeUpdate

	/**
	 * 	Get Sql
	 *	@return sql
	 */
	public String getSql()
	{
		if (p_vo != null)
			return p_vo.getSql();
		return null;
	}	//	getSql


	/**
	 * 	Get Connection
	 *	@return connection for local - or null for remote
	 *	@throws SQLException
	 * @see java.sql.Statement#getConnection()
	 */
	public Connection getConnection () throws SQLException
	{
		if (m_stmt != null)
			return m_stmt.getConnection();
		return null;
	}	//	getConnection

	/**
	 * 	Commit (if local)
	 *	@throws SQLException
	 */
	public void commit() throws SQLException
	{
		Connection conn = getConnection();
		if (conn != null && !conn.getAutoCommit())
		{
			conn.commit();
			log.fine("commit");
		}
	}	//	commit


	/**
	 * Method executeUpdate
	 * @param sql0 String
	 * @param autoGeneratedKeys int
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#executeUpdate(String, int)
	 */
	public int executeUpdate (String sql0, int autoGeneratedKeys) throws SQLException
	{
		p_vo.setSql(DB.getDatabase().convertStatement(sql0));
		if (m_stmt != null)
			return m_stmt.executeUpdate(p_vo.getSql(), autoGeneratedKeys);
		throw new java.lang.UnsupportedOperationException ("Method executeUpdate() not yet implemented.");
	}

	/**
	 * Method executeUpdate
	 * @param sql0 String
	 * @param columnIndexes int[]
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#executeUpdate(String, int[])
	 */
	public int executeUpdate (String sql0, int[] columnIndexes) throws SQLException
	{
		p_vo.setSql(DB.getDatabase().convertStatement(sql0));
		if (m_stmt != null)
			return m_stmt.executeUpdate(p_vo.getSql(), columnIndexes);
		throw new java.lang.UnsupportedOperationException ("Method executeUpdate() not yet implemented.");
	}

	/**
	 * Method executeUpdate
	 * @param sql0 String
	 * @param columnNames String[]
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#executeUpdate(String, String[])
	 */
	public int executeUpdate (String sql0, String[] columnNames) throws SQLException
	{
		p_vo.setSql(DB.getDatabase().convertStatement(sql0));
		if (m_stmt != null)
			return m_stmt.executeUpdate(p_vo.getSql(), columnNames);
		throw new java.lang.UnsupportedOperationException ("Method executeUpdate() not yet implemented.");
	}


	/**
	 * Method execute
	 * @param sql0 String
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.Statement#execute(String)
	 */
	public boolean execute (String sql0) throws SQLException
	{
		p_vo.setSql(DB.getDatabase().convertStatement(sql0));
		if (m_stmt != null)
			return m_stmt.execute(p_vo.getSql());
		throw new java.lang.UnsupportedOperationException ("Method execute() not yet implemented.");
	}

	/**
	 * Method execute
	 * @param sql0 String
	 * @param autoGeneratedKeys int
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.Statement#execute(String, int)
	 */
	public boolean execute (String sql0, int autoGeneratedKeys) throws SQLException
	{
		p_vo.setSql(DB.getDatabase().convertStatement(sql0));
		if (m_stmt != null)
			return m_stmt.execute(p_vo.getSql(), autoGeneratedKeys);
		throw new java.lang.UnsupportedOperationException ("Method execute() not yet implemented.");
	}

	/**
	 * Method execute
	 * @param sql0 String
	 * @param columnIndexes int[]
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.Statement#execute(String, int[])
	 */
	public boolean execute (String sql0, int[] columnIndexes) throws SQLException
	{
		p_vo.setSql(DB.getDatabase().convertStatement(sql0));
		if (m_stmt != null)
			return m_stmt.execute(p_vo.getSql(), columnIndexes);
		throw new java.lang.UnsupportedOperationException ("Method execute() not yet implemented.");
	}

	/**
	 * Method execute
	 * @param sql0 String
	 * @param columnNames String[]
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.Statement#execute(String, String[])
	 */
	public boolean execute (String sql0, String[] columnNames) throws SQLException
	{
		p_vo.setSql(DB.getDatabase().convertStatement(sql0));
		if (m_stmt != null)
			return m_stmt.execute(p_vo.getSql(), columnNames);
		throw new java.lang.UnsupportedOperationException ("Method execute() not yet implemented.");
	}



	/**************************************************************************
	 * 	Get Max Field Size
	 * 	@return field size
	 * 	@throws SQLException
	 * @see java.sql.Statement#getMaxFieldSize()
	 */
	public int getMaxFieldSize () throws SQLException
	{
		if (m_stmt != null)
			return m_stmt.getMaxFieldSize();
		throw new java.lang.UnsupportedOperationException ("Method getMaxFieldSize() not yet implemented.");
	}

	/**
	 * Method setMaxFieldSize
	 * @param max int
	 * @throws SQLException
	 * @see java.sql.Statement#setMaxFieldSize(int)
	 */
	public void setMaxFieldSize (int max) throws SQLException
	{
		if (m_stmt != null)
			m_stmt.setMaxFieldSize(max);
		else
			throw new java.lang.UnsupportedOperationException ("Method setMaxFieldSize() not yet implemented.");
	}

	/**
	 * Method getMaxRows
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#getMaxRows()
	 */
	public int getMaxRows () throws SQLException
	{
		if (m_stmt != null)
			return m_stmt.getMaxRows();
		throw new java.lang.UnsupportedOperationException ("Method getMaxRows() not yet implemented.");
	}

	/**
	 * Method setMaxRows
	 * @param max int
	 * @throws SQLException
	 * @see java.sql.Statement#setMaxRows(int)
	 */
	public void setMaxRows (int max) throws SQLException
	{
		if (m_stmt != null)
			m_stmt.setMaxRows(max);
		else
			throw new java.lang.UnsupportedOperationException ("Method setMaxRows() not yet implemented.");
	}

	/*************************************************************************
	 * 	Add Batch
	 *	@param sql sql
	 *	@throws SQLException
	 * @see java.sql.Statement#addBatch(String)
	 */
	public void addBatch (String sql) throws SQLException
	{
		if (m_stmt != null)
			m_stmt.addBatch(sql);
		else
			throw new java.lang.UnsupportedOperationException ("Method addBatch() not yet implemented.");
	}

	/**
	 * Method clearBatch
	 * @throws SQLException
	 * @see java.sql.Statement#clearBatch()
	 */
	public void clearBatch () throws SQLException
	{
		if (m_stmt != null)
			m_stmt.clearBatch();
		else
			throw new java.lang.UnsupportedOperationException ("Method clearBatch() not yet implemented.");
	}

	/**
	 * Method executeBatch
	 * @return int[]
	 * @throws SQLException
	 * @see java.sql.Statement#executeBatch()
	 */
	public int[] executeBatch () throws SQLException
	{
		if (m_stmt != null)
			return m_stmt.executeBatch();
		throw new java.lang.UnsupportedOperationException ("Method executeBatch() not yet implemented.");
	}


	/**
	 * Method getMoreResults
	 * @param current int
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.Statement#getMoreResults(int)
	 */
	public boolean getMoreResults (int current) throws SQLException
	{
		if (m_stmt != null)
			return m_stmt.getMoreResults(current);
		throw new java.lang.UnsupportedOperationException ("Method getMoreResults() not yet implemented.");
	}


	/**
	 * Method getGeneratedKeys
	 * @return ResultSet
	 * @throws SQLException
	 * @see java.sql.Statement#getGeneratedKeys()
	 */
	public ResultSet getGeneratedKeys () throws SQLException
	{
		if (m_stmt != null)
			return m_stmt.getGeneratedKeys();
		throw new java.lang.UnsupportedOperationException ("Method getGeneratedKeys() not yet implemented.");
	}

	/**
	 * Method getResultSetHoldability
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSetHoldability()
	 */
	public int getResultSetHoldability () throws SQLException
	{
		if (m_stmt != null)
			return m_stmt.getResultSetHoldability();
		throw new java.lang.UnsupportedOperationException ("Method getResultSetHoldability() not yet implemented.");
	}

	/**
	 * Method setEscapeProcessing
	 * @param enable boolean
	 * @throws SQLException
	 * @see java.sql.Statement#setEscapeProcessing(boolean)
	 */
	public void setEscapeProcessing (boolean enable) throws SQLException
	{
		if (m_stmt != null)
			m_stmt.setEscapeProcessing(enable);
		else
			throw new java.lang.UnsupportedOperationException ("Method setEscapeProcessing() not yet implemented.");
	}

	/**
	 * Method getQueryTimeout
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#getQueryTimeout()
	 */
	public int getQueryTimeout () throws SQLException
	{
		if (m_stmt != null)
			return m_stmt.getQueryTimeout();
		throw new java.lang.UnsupportedOperationException ("Method getQueryTimeout() not yet implemented.");
	}

	/**
	 * Method setQueryTimeout
	 * @param seconds int
	 * @throws SQLException
	 * @see java.sql.Statement#setQueryTimeout(int)
	 */
	public void setQueryTimeout (int seconds) throws SQLException
	{
		if (m_stmt != null)
			m_stmt.setQueryTimeout (seconds);
		else
			throw new java.lang.UnsupportedOperationException ("Method setQueryTimeout() not yet implemented.");
	}

	/**
	 * Method cancel
	 * @throws SQLException
	 * @see java.sql.Statement#cancel()
	 */
	public void cancel () throws SQLException
	{
		if (m_stmt != null)
			m_stmt.cancel();
		else
			throw new java.lang.UnsupportedOperationException ("Method cancel() not yet implemented.");
	}

	/**
	 * Method getWarnings
	 * @return SQLWarning
	 * @throws SQLException
	 * @see java.sql.Statement#getWarnings()
	 */
	public SQLWarning getWarnings () throws SQLException
	{
		if (m_stmt != null)
			return m_stmt.getWarnings();
		throw new java.lang.UnsupportedOperationException ("Method getWarnings() not yet implemented.");
	}

	/**
	 * Method clearWarnings
	 * @throws SQLException
	 * @see java.sql.Statement#clearWarnings()
	 */
	public void clearWarnings () throws SQLException
	{
		if (m_stmt != null)
			m_stmt.clearWarnings();
		else
			throw new java.lang.UnsupportedOperationException ("Method clearWarnings() not yet implemented.");
	}

	/**
	 * Method setCursorName
	 * @param name String
	 * @throws SQLException
	 * @see java.sql.Statement#setCursorName(String)
	 */
	public void setCursorName (String name) throws SQLException
	{
		if (m_stmt != null)
			m_stmt.setCursorName(name);
		else
			throw new java.lang.UnsupportedOperationException ("Method setCursorName() not yet implemented.");
	}


	/**
	 * Method getResultSet
	 * @return ResultSet
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSet()
	 */
	public ResultSet getResultSet () throws SQLException
	{
		if (m_stmt != null)
			return m_stmt.getResultSet();
		throw new java.lang.UnsupportedOperationException ("Method getResultSet() not yet implemented.");
	}

	/**
	 * Method getUpdateCount
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#getUpdateCount()
	 */
	public int getUpdateCount () throws SQLException
	{
		if (m_stmt != null)
			return m_stmt.getUpdateCount();
		throw new java.lang.UnsupportedOperationException ("Method getUpdateCount() not yet implemented.");
	}

	/**
	 * Method getMoreResults
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.Statement#getMoreResults()
	 */
	public boolean getMoreResults () throws SQLException
	{
		if (m_stmt != null)
			return m_stmt.getMoreResults();
		throw new java.lang.UnsupportedOperationException ("Method getMoreResults() not yet implemented.");
	}

	/**
	 * Method setFetchDirection
	 * @param direction int
	 * @throws SQLException
	 * @see java.sql.Statement#setFetchDirection(int)
	 */
	public void setFetchDirection (int direction) throws SQLException
	{
		if (m_stmt != null)
			m_stmt.setFetchDirection(direction);
		else
			throw new java.lang.UnsupportedOperationException ("Method setFetchDirection() not yet implemented.");
	}

	/**
	 * Method getFetchDirection
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#getFetchDirection()
	 */
	public int getFetchDirection () throws SQLException
	{
		if (m_stmt != null)
			return m_stmt.getFetchDirection();
		throw new java.lang.UnsupportedOperationException ("Method getFetchDirection() not yet implemented.");
	}

	/**
	 * Method setFetchSize
	 * @param rows int
	 * @throws SQLException
	 * @see java.sql.Statement#setFetchSize(int)
	 */
	public void setFetchSize (int rows) throws SQLException
	{
		if (m_stmt != null)
			m_stmt.setFetchSize(rows);
		else
			throw new java.lang.UnsupportedOperationException ("Method setFetchSize() not yet implemented.");
	}

	/**
	 * Method getFetchSize
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#getFetchSize()
	 */
	public int getFetchSize () throws SQLException
	{
		if (m_stmt != null)
			return m_stmt.getFetchSize();
		throw new java.lang.UnsupportedOperationException ("Method getFetchSize() not yet implemented.");
	}

	/**
	 * Method getResultSetConcurrency
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSetConcurrency()
	 */
	public int getResultSetConcurrency () throws SQLException
	{
		if (m_stmt != null)
			return m_stmt.getResultSetConcurrency();
		throw new java.lang.UnsupportedOperationException ("Method getResultSetConcurrency() not yet implemented.");
	}

	/**
	 * Method getResultSetType
	 * @return int
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSetType()
	 */
	public int getResultSetType () throws SQLException
	{
		if (m_stmt != null)
			return m_stmt.getResultSetType();
		throw new java.lang.UnsupportedOperationException ("Method getResultSetType() not yet implemented.");
	}

	/**
	 * 	Close
	 * 	@throws SQLException
	 * @see java.sql.Statement#close()
	 */
	public void close () throws SQLException
	{
		if (m_stmt != null)
			m_stmt.close();
		if(m_conn != null && !m_conn.isClosed())
		{
			m_conn.close();
			DB.returnCachedConnection(m_conn);
		}
	}	//	close

	/*************************************************************************
	 * 	Execute Update.
	 *	@return row count
	 */
	public int remote_executeUpdate()
	{
		log.finest("");
		try
		{
			CompiereDatabase db = CConnection.get().getDatabase();
			if (db == null)
				throw new NullPointerException("Remote - No Database");
			//
			Trx trx = Trx.getAutoCommitTrx();
			Statement pstmt = local_getStatement (trx.getConnection());	//	shared connection
			int result = pstmt.executeUpdate(p_vo.getSql());
			pstmt.close();
			trx.close();
			//
			return result;
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, p_vo.toString(), ex);
			throw new RuntimeException (ex);
		}
	}	//	remote_executeUpdate

	/**************************************************************************
	 * 	Get Prepared Statement to create RowSet.
	 * 	Method called on Remote to execute locally.
	 * @param conn TODO
	 * 	@return Prepared Statement
	 */
	private Statement local_getStatement (Connection conn)
	{
		log.finest(p_vo.getSql());
		if (conn == null)
			throw new CompiereStateException("Local - No Connection");
		Statement stmt = null;
		try
		{
			stmt = conn.createStatement(p_vo.getResultSetType(), p_vo.getResultSetConcurrency());
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, "local", ex);
			try
			{
				if (stmt != null)
					stmt.close();
				stmt = null;
			}
			catch (SQLException ex1)
			{
			}
		}
		return stmt;
	}	//	local_getStatement


	/*************************************************************************
	 * 	Get Result as RowSet for Remote.
	 * 	Get shared connection for RMI!
	 * 	If RowSet is transfred via RMI, closing the RowSet does not close the connection
	 *	@return result as RowSet
	 */
	public RowSet remote_getRowSet()
	{
		log.finest("remote");
		/**
		try
		{
			CompiereDatabase db = CConnection.get().getDatabase();
			if (db == null)
			{
				log.log(Level.SEVERE, "No Database");
				throw new NullPointerException("Remote - No Database");
			}
			//
			Statement stmt = local_getStatement (false, null);	// shared connection
			ResultSet rs = stmt.executeQuery(p_vo.getSql());
			RowSet rowSet = db.getRowSet (rs);
			rs.close();
			stmt.close();
			//
			if (rowSet != null)
				return rowSet;
			else
				log.log(Level.SEVERE, "No RowSet");
			throw new NullPointerException("Remore - No RowSet");
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, p_vo.toString(), ex);
			throw new RuntimeException (ex);
		}
	//	return null;
	 	**/
		//	Shared Connection
		Trx trx = Trx.getAutoCommitTrx();
		PreparedStatement pstmt = null;
		RowSet rowSet = null;
		try
		{
			pstmt = trx.getConnection().prepareStatement(p_vo.getSql(),
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//	Set Parameters
			ArrayList<?> parameters = p_vo.getParameters();
			for (int i = 0; i < parameters.size(); i++)
			{
				Object o = parameters.get(i);
				if (o == null)
					throw new IllegalArgumentException ("Null Parameter #" + i);
				else if (o instanceof NullParameter)
				{
					int type = ((NullParameter)o).getType();
					pstmt.setNull(i+1, type);
					log.finest("#" + (i+1) + " - Null");
				}
				else if (o instanceof Integer)
				{
					pstmt.setInt(i+1, ((Integer)o).intValue());
					log.finest("#" + (i+1) + " - int=" + o);
				}
				else if (o instanceof String)
				{
					pstmt.setString(i+1, (String)o);
					log.finest("#" + (i+1) + " - String=" + o);
				}
				else if (o instanceof Timestamp)
				{
					pstmt.setTimestamp(i+1, (Timestamp)o);
					log.finest("#" + (i+1) + " - Timestamp=" + o);
				}
				else if (o instanceof BigDecimal)
				{
					pstmt.setBigDecimal(i+1, (BigDecimal)o);
					log.finest("#" + (i+1) + " - BigDecimal=" + o);
				}
				else
					throw new java.lang.UnsupportedOperationException ("Unknown Parameter Class=" + o.getClass());
			}
			//
			ResultSet rs = pstmt.executeQuery();
			rowSet = CCachedRowSet.getRowSet(rs);
			pstmt.close();
			pstmt = null;
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, p_vo.toString(), ex);
			throw new RuntimeException (ex);
		}
		//	Close Cursor
		finally {
			try {
				if (pstmt != null)
					pstmt.close();
				pstmt = null;
			} catch (Exception e) {
				log.log(Level.SEVERE, "close pstmt", e);
			}
			
			if(trx != null)
				trx.close();
		}
		return rowSet;
	}	//	remote_getRowSet

	/**
	 * 	isClosed
	 *	@see java.sql.Statement#isClosed()
	 *	@return
	 *	@throws SQLException
	 */
	public boolean isClosed()
		throws SQLException
	{
		if (m_stmt != null)
			return m_stmt.isClosed();
		throw new java.lang.UnsupportedOperationException ("Method isClosed() not implemented.");
	}

	/**
	 * 	isPoolable
	 *	@see java.sql.Statement#isPoolable()
	 *	@return
	 *	@throws SQLException
	 */
	public boolean isPoolable()
		throws SQLException
	{
		if (m_stmt != null)
			return m_stmt.isPoolable();
		throw new java.lang.UnsupportedOperationException ("Method isPoolable() not implemented.");
	}

	/**
	 * 	setPoolable
	 *	@see java.sql.Statement#setPoolable(boolean)
	 *	@param poolable
	 *	@throws SQLException
	 */
	public void setPoolable(boolean poolable)
		throws SQLException
	{
		if (m_stmt != null)
			m_stmt.setPoolable(poolable);
		else
			throw new java.lang.UnsupportedOperationException ("Method setPoolable() not implemented.");
	}

	/**
	 * 	isWrapperFor
	 *	@see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 *	@param iface
	 *	@return
	 *	@throws SQLException
	 */
	public boolean isWrapperFor(Class< ? > iface)
		throws SQLException
	{
		if (m_stmt != null)
			return m_stmt.isWrapperFor(iface);
		throw new java.lang.UnsupportedOperationException ("Method isWrapperFor() not implemented.");
	}

	/**
	 * 	unwrap
	 *	@see java.sql.Wrapper#unwrap(java.lang.Class)
	 *	@param <T>
	 *	@param iface
	 *	@return
	 *	@throws SQLException
	 */
	public <T> T unwrap(Class<T> iface)
		throws SQLException
	{
		if (m_stmt != null)
			return m_stmt.unwrap(iface);
		throw new java.lang.UnsupportedOperationException ("Method unwrap() not implemented.");
	}
	/** */
}	//	CStatement
