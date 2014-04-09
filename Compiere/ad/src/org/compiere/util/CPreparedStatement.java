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

import java.io.*;
import java.math.*;
import java.net.*;
import java.rmi.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.sql.*;

import org.compiere.common.CompiereStateException;
import org.compiere.db.*;
import org.compiere.interfaces.*;

/**
 *  Compiere Prepared Statement
 *
 *  @author Jorg Janke
 *  11/09/06 Jinglun Zhang  - add m_connRO logic to release share locks with DB2 database
 *  
 *  @version $Id: CPreparedStatement.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CPreparedStatement extends CStatement implements PreparedStatement
{
	/**
	 * Stores the borrowed connection.
	 * If trx is null, we borrow a connection from the pool, and close it when the statement is closed.
	 * 
	 */
	private final Connection m_conn;

	/**
	 * Records the stack trace where this CPreparedStatement borrowed its connection object.
	 */
	private final Throwable m_borrowStackTrace;


	/**
	 *	Prepared Statement Constructor
	 *
	 *  @param resultSetType - ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.TYPE_SCROLL_SENSITIVE
	 *  @param resultSetConcurrency - ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
	 * 	@param sql0 unconverted sql statement
	 *  @param trx transaction name or null
	 */
	CPreparedStatement(int resultSetType, int resultSetConcurrency, String sql0, Trx trx) 
	{
		if (sql0 == null || sql0.length() == 0)
			throw new IllegalArgumentException ("sql required");

		// if (CLogMgt.isLevelAll())
		log.finest("TrxName=" + trx + ",SQL=" + sql0 + ", resultSetConcurrency = " + resultSetConcurrency);
		String sql1 = DB.getDatabase().convertStatement(sql0);	//	conversion 
		p_vo = new CStatementVO (resultSetType, resultSetConcurrency, sql1);

		Connection conn = null;
		PreparedStatement pstmt = null;
		//	Local access
		
		Throwable borrowStackTrace = null;
		
		if (!DB.isRemoteObjects())
		{
			try
			{
				if (trx != null)
					conn = trx.getConnection();
				else
				{
					conn = DB.getCachedConnection();
					borrowStackTrace = new Throwable();
					if (resultSetConcurrency == ResultSet.CONCUR_UPDATABLE)
						conn.setAutoCommit(true);
					else
						conn.setAutoCommit(false);
				}
				if (conn == null)
					throw new DBException("No Connection");
				pstmt = conn.prepareStatement (p_vo.getSql(), resultSetType, resultSetConcurrency);
			}
			catch (SQLException e)
			{
				log.log(Level.WARNING, p_vo.getSql(), e);
			}
		}
		
		m_stmt = pstmt;

		if( trx == null )
			m_conn = conn;
		else
			m_conn = null;

		m_borrowStackTrace = borrowStackTrace;
	} // CPreparedStatement

	/**
	 * 	Remote Constructor
	 *	@param vo value object
	 */
	public CPreparedStatement (CStatementVO vo)
	{
		super(vo);
		m_conn = null;
		m_stmt = null;
		m_borrowStackTrace = null;
	}	//	CPreparedStatement


	/**
	 *	Close the statement
	 */
	@Override
	public void close() throws SQLException
	{
		super.close();

		if(m_conn != null && !m_conn.isClosed())
		{			
			m_conn.close();
			DB.returnCachedConnection(m_conn);
		}
	}	//	close


	/**
	 * 	Execute Query
	 * 	@return ResultSet or RowSet
	 * 	@throws SQLException
	 * @see java.sql.PreparedStatement#executeQuery()
	 */
	public ResultSet executeQuery () throws SQLException
	{
		if (m_stmt != null)	//	local
		{
			//jz we need to release share lock here
			//return ((PreparedStatement)p_stmt).executeQuery();
			ResultSet rs = ((PreparedStatement)m_stmt).executeQuery();
			if (DB.isDB2())
				// FIXME: DB2: We may be committing changes that shouldn't be committed  
				m_stmt.getConnection().commit();
			return rs;
		}
		//
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
					ResultSet rs = server.pstmt_getRowSet (p_vo);
					p_vo.clearParameters();		//	re-use of result set
					if (rs == null)
						log.warning("ResultSet is null - " + p_vo);
					else
						p_remoteErrors = 0;
					return rs;
				}
				else {
					log.log(Level.SEVERE, "AppsServer not found");
					p_remoteErrors++;
				}
			}
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "AppsServer error", ex);
			p_remoteErrors++;
		}
		//	Try locally
		log.warning("Execute locally");
		Trx trx = Trx.getAutoCommitTrx();
		PreparedStatement pstmt = local_getPreparedStatement(trx.getConnection()); // shared connection
		if (pstmt == null)
		{
			log.log(Level.SEVERE, " Error: could not get a valid preparedStatement in local.");
			return null;
		}
		p_vo.clearParameters();		//	re-use of result set
		ResultSet rs = pstmt.executeQuery();		
		RowSet rowSet = CCachedRowSet.getRowSet(rs);
		rs.close();
		pstmt.close();
		trx.close();
		return rowSet;
	}	//	executeQuery

	/**
	 * 	Execute Query
	 * 	@param sql0 unconverted SQL to execute
	 * 	@return ResultSet or RowSet
	 * 	@throws SQLException
	 * @see java.sql.Statement#executeQuery(String)
	 */
	@Override
	public ResultSet executeQuery (String sql0) throws SQLException
	{
		//	Convert SQL
		p_vo.setSql(DB.getDatabase().convertStatement(sql0));
		if (m_stmt != null)	//	local
			return m_stmt.executeQuery(p_vo.getSql());
		//
		return executeQuery();
	}	//	executeQuery


	/**************************************************************************
	 * 	Execute Update
	 *	@return no of updated rows
	 *	@throws SQLException
	 * @see java.sql.PreparedStatement#executeUpdate()
	 */
	public int executeUpdate () throws SQLException
	{
		if (m_stmt != null)
			return ((PreparedStatement)m_stmt).executeUpdate();
		//
		//	Client -> remote sever
		log.finest("server => " + p_vo + ", Remote=" + DB.isRemoteObjects());
		try
		{
			if (DB.isRemoteObjects() && CConnection.get().isAppsServerOK(false))
			{
				Server server = CConnection.get().getServer();
				if (server != null)
				{
					int result = server.stmt_executeUpdate (p_vo);
					p_vo.clearParameters();		//	re-use of result set
					return result;
				}
				log.log(Level.SEVERE, "AppsServer not found");
			}
		}
		catch (RemoteException ex)
		{
			log.log(Level.SEVERE, "AppsServer error", ex);
		}
		//	Try locally
		log.warning("execute locally");
		Trx trx = Trx.getAutoCommitTrx();
		PreparedStatement pstmt = local_getPreparedStatement (trx.getConnection());	//	shared connection
		p_vo.clearParameters();		//	re-use of result set
		int result = pstmt.executeUpdate();
		pstmt.close();
		trx.close();
		return result;
	}	//	executeUpdate

	/**
	 * 	Execute Update
	 *	@param sql0 unconverted sql
	 *	@return no of updated rows
	 *	@throws SQLException
	 * @see java.sql.Statement#executeUpdate(String)
	 */
	@Override
	public int executeUpdate (String sql0) throws SQLException
	{
		//	Convert SQL
		p_vo.setSql(DB.getDatabase().convertStatement(sql0));
		if (m_stmt != null)	//	local
			return m_stmt.executeUpdate (p_vo.getSql());
		return executeUpdate();
	}	//	executeUpdate


	/**
	 * Method execute
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#execute()
	 */
	public boolean execute () throws SQLException
	{
		if (m_stmt != null)
			return ((PreparedStatement)m_stmt).execute();
		throw new java.lang.UnsupportedOperationException ("Method execute() not yet implemented.");
	}


	/**
	 * Method getMetaData
	 * @return ResultSetMetaData
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#getMetaData()
	 */
	public ResultSetMetaData getMetaData () throws SQLException
	{
		if (m_stmt != null)
			return ((PreparedStatement)m_stmt).getMetaData ();
		else
			throw new java.lang.UnsupportedOperationException ("Method getMetaData() not yet implemented.");
	}

	/**
	 * Method getParameterMetaData
	 * @return ParameterMetaData
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#getParameterMetaData()
	 */
	public ParameterMetaData getParameterMetaData () throws SQLException
	{
		if (m_stmt != null)
			return ((PreparedStatement)m_stmt).getParameterMetaData();
		throw new java.lang.UnsupportedOperationException ("Method getParameterMetaData() not yet implemented.");
	}

	/**
	 * Method addBatch
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#addBatch()
	 */
	public void addBatch () throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).addBatch ();
		else
			throw new java.lang.UnsupportedOperationException ("Method addBatch() not yet implemented.");
	}

	/**************************************************************************
	 * 	Set Null
	 *	@param parameterIndex index
	 *	@param sqlType type
	 *	@throws SQLException
	 */
	public void setNull (int parameterIndex, int sqlType) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setNull (parameterIndex, sqlType);
		else
			p_vo.setParameter(parameterIndex, new NullParameter(sqlType));
	}	//	setNull

	/**
	 * Method setNull
	 * @param parameterIndex int
	 * @param sqlType int
	 * @param typeName String
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNull(int, int, String)
	 */
	public void setNull (int parameterIndex, int sqlType, String typeName) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setNull (parameterIndex, sqlType);
		else
			p_vo.setParameter(parameterIndex, new NullParameter(sqlType));
	}

	/**
	 * Method setBoolean
	 * @param parameterIndex int
	 * @param x boolean
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBoolean(int, boolean)
	 */
	public void setBoolean (int parameterIndex, boolean x) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setBoolean (parameterIndex, x);
		else
			p_vo.setParameter(parameterIndex, Boolean.valueOf(x));
	}

	/**
	 * Method setByte
	 * @param parameterIndex int
	 * @param x byte
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setByte(int, byte)
	 */
	public void setByte (int parameterIndex, byte x) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setByte (parameterIndex, x);
		else
			p_vo.setParameter(parameterIndex, new Byte(x));
	}

	/**
	 * Method setShort
	 * @param parameterIndex int
	 * @param x short
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setShort(int, short)
	 */
	public void setShort (int parameterIndex, short x) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setShort (parameterIndex, x);
		else
			p_vo.setParameter(parameterIndex, new Short(x));
	}

	/**
	 * Method setInt
	 * @param parameterIndex int
	 * @param x int
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setInt(int, int)
	 */
	public void setInt (int parameterIndex, int x) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setInt (parameterIndex, x);
		else
			p_vo.setParameter(parameterIndex, Integer.valueOf(x));
	}

	/**
	 * Method setLong
	 * @param parameterIndex int
	 * @param x long
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setLong(int, long)
	 */
	public void setLong (int parameterIndex, long x) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setLong (parameterIndex, x);
		else
			p_vo.setParameter(parameterIndex, new Long(x));
	}

	/**
	 * Method setFloat
	 * @param parameterIndex int
	 * @param x float
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setFloat(int, float)
	 */
	public void setFloat (int parameterIndex, float x) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setFloat (parameterIndex, x);
		else
			p_vo.setParameter(parameterIndex, new Float(x));
	}

	/**
	 * Method setDouble
	 * @param parameterIndex int
	 * @param x double
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setDouble(int, double)
	 */
	public void setDouble (int parameterIndex, double x) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setDouble (parameterIndex, x);
		else
			p_vo.setParameter(parameterIndex, new Double(x));
	}

	/**
	 * Method setBigDecimal
	 * @param parameterIndex int
	 * @param x BigDecimal
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBigDecimal(int, BigDecimal)
	 */
	public void setBigDecimal (int parameterIndex, BigDecimal x) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setBigDecimal (parameterIndex, x);
		else
			p_vo.setParameter(parameterIndex, x);
	}

	/**
	 * Method setString
	 * @param parameterIndex int
	 * @param x String
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setString(int, String)
	 */
	public void setString (int parameterIndex, String x) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setString (parameterIndex, x);
		else
			p_vo.setParameter(parameterIndex, x);
	}

	/**
	 * Method setBytes
	 * @param parameterIndex int
	 * @param x byte[]
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBytes(int, byte[])
	 */
	public void setBytes (int parameterIndex, byte[] x) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setBytes (parameterIndex, x);
		else
			p_vo.setParameter (parameterIndex, x);
	}

	/**
	 * Method setDate
	 * @param parameterIndex int
	 * @param x java.sql.Date
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setDate(int, java.sql.Date)
	 */
	public void setDate (int parameterIndex, java.sql.Date x) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setDate (parameterIndex, x);
		else
			p_vo.setParameter(parameterIndex, x);
	}

	/**
	 * Method setTime
	 * @param parameterIndex int
	 * @param x Time
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setTime(int, Time)
	 */
	public void setTime (int parameterIndex, Time x) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setTime (parameterIndex, x);
		else
			p_vo.setParameter(parameterIndex, x);
	}

	/**
	 * Method setTimestamp
	 * @param parameterIndex int
	 * @param x Timestamp
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setTimestamp(int, Timestamp)
	 */
	public void setTimestamp (int parameterIndex, Timestamp x) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setTimestamp (parameterIndex, x);
		else
			p_vo.setParameter(parameterIndex, x);
	}

	/**
	 * Method setAsciiStream
	 * @param parameterIndex int
	 * @param x InputStream
	 * @param length int
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setAsciiStream(int, InputStream, int)
	 */
	public void setAsciiStream (int parameterIndex, InputStream x, int length) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setAsciiStream (parameterIndex, x, length);
		else
			throw new java.lang.UnsupportedOperationException ("Method setAsciiStream() not yet implemented.");
	}

	/**
	 * @param parameterIndex the first parameter is 1, the second is 2, ...
	 * @param x a <code>java.io.InputStream</code> object that contains the
	 *        Unicode parameter value as two-byte Unicode characters
	 * @param length the number of bytes in the stream
	 * @exception SQLException if a database access error occurs
	 * see java.sql.PreparedStatement#setUnicodeStream(int, InputStream, int)
	 * @deprecated
	 */
	@Deprecated
	public void setUnicodeStream (int parameterIndex, InputStream x, int length) throws SQLException
	{
		throw new UnsupportedOperationException ("Method setUnicodeStream() not yet implemented.");
	}

	/**
	 * Method setBinaryStream
	 * @param parameterIndex int
	 * @param x InputStream
	 * @param length int
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBinaryStream(int, InputStream, int)
	 */
	public void setBinaryStream (int parameterIndex, InputStream x, int length) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setBinaryStream (parameterIndex, x, length);
		else
			throw new java.lang.UnsupportedOperationException ("Method setBinaryStream() not yet implemented.");
	}

	/**
	 * Method clearParameters
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#clearParameters()
	 */
	public void clearParameters () throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).clearParameters ();
		else
			p_vo.clearParameters();
	}

	/**
	 * Method setObject
	 * @param parameterIndex int
	 * @param x Object
	 * @param targetSqlType int
	 * @param scale int
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setObject(int, Object, int, int)
	 */
	public void setObject (int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setObject (parameterIndex, x, targetSqlType, scale);
		else
			throw new java.lang.UnsupportedOperationException ("Method setObject() not yet implemented.");
	}

	/**
	 * Method setObject
	 * @param parameterIndex int
	 * @param x Object
	 * @param targetSqlType int
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setObject(int, Object, int)
	 */
	public void setObject (int parameterIndex, Object x, int targetSqlType) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setObject (parameterIndex, x);
		else
			throw new java.lang.UnsupportedOperationException ("Method setObject() not yet implemented.");
	}

	/**
	 * Method setObject
	 * @param parameterIndex int
	 * @param x Object
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setObject(int, Object)
	 */
	public void setObject (int parameterIndex, Object x) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setObject (parameterIndex, x);
		else
			p_vo.setParameter(parameterIndex, x);
	}

	/**
	 * Method setCharacterStream
	 * @param parameterIndex int
	 * @param reader Reader
	 * @param length int
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setCharacterStream(int, Reader, int)
	 */
	public void setCharacterStream (int parameterIndex, Reader reader, int length) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setCharacterStream (parameterIndex, reader, length);
		else
			throw new java.lang.UnsupportedOperationException ("Method setCharacterStream() not yet implemented.");
	}

	/**
	 * Method setRef
	 * @param parameterIndex int
	 * @param x Ref
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setRef(int, Ref)
	 */
	public void setRef (int parameterIndex, Ref x) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setRef (parameterIndex, x);
		else
			p_vo.setParameter(parameterIndex, x);
	}

	/**
	 * Method setBlob
	 * @param parameterIndex int
	 * @param x Blob
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBlob(int, Blob)
	 */
	public void setBlob (int parameterIndex, Blob x) throws SQLException
	{
		if (m_stmt != null)
		{
			if (DB.isPostgreSQL()) //jz
			{
				((PreparedStatement)m_stmt).setBytes(parameterIndex,  x.getBytes(1, (int)x.length()));
			}
			else
				((PreparedStatement)m_stmt).setBlob (parameterIndex, x);
		}
		else
			p_vo.setParameter(parameterIndex, x);
	}

	/**
	 * Method setClob
	 * @param parameterIndex int
	 * @param x Clob
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setClob(int, Clob)
	 */
	public void setClob (int parameterIndex, Clob x) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setClob (parameterIndex, x);
		else
			p_vo.setParameter(parameterIndex, x);
	}

	/**
	 * Method setArray
	 * @param parameterIndex int
	 * @param x Array
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setArray(int, Array)
	 */
	public void setArray (int parameterIndex, Array x) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setArray (parameterIndex, x);
		else
			p_vo.setParameter(parameterIndex, x);
	}

	/**
	 * Method setDate
	 * @param parameterIndex int
	 * @param x java.sql.Date
	 * @param cal Calendar
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setDate(int, java.sql.Date, Calendar)
	 */
	public void setDate (int parameterIndex, java.sql.Date x, Calendar cal) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setDate (parameterIndex, x, cal);
		else
			throw new java.lang.UnsupportedOperationException ("Method setDate() not yet implemented.");
	}

	/**
	 * Method setTime
	 * @param parameterIndex int
	 * @param x Time
	 * @param cal Calendar
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setTime(int, Time, Calendar)
	 */
	public void setTime (int parameterIndex, Time x, Calendar cal) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setTime (parameterIndex, x, cal);
		else
			throw new java.lang.UnsupportedOperationException ("Method setTime() not yet implemented.");
	}

	/**
	 * Method setTimestamp
	 * @param parameterIndex int
	 * @param x Timestamp
	 * @param cal Calendar
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setTimestamp(int, Timestamp, Calendar)
	 */
	public void setTimestamp (int parameterIndex, Timestamp x, Calendar cal) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setTimestamp (parameterIndex, x, cal);
		else
			throw new java.lang.UnsupportedOperationException ("Method setTimestamp() not yet implemented.");
	}

	/**
	 * Method setURL
	 * @param parameterIndex int
	 * @param x URL
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setURL(int, URL)
	 */
	public void setURL (int parameterIndex, URL x) throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setObject (parameterIndex, x);
		else
			p_vo.setParameter(parameterIndex, x);
	}

	/**
	 * 	String representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		if (m_stmt != null)
			return "CPreparedStatement[Local=" + m_stmt + "]";
		return "CPreparedStatement[" + p_vo + "]";
	}	//	toString

	/**************************************************************************
	 * 	Get Prepared Statement to create RowSet and set parameters.
	 * 	Method called on Remote to execute locally.
	 * @param conn TODO
	 * 	@return Prepared Statement
	 */
	private PreparedStatement local_getPreparedStatement (Connection conn)
	{
		log.finest(p_vo.getSql());
		if (conn == null)
			throw new CompiereStateException("Local - No Connection");
		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(p_vo.getSql(), p_vo.getResultSetType(), p_vo.getResultSetConcurrency());
			//	Set Parameters
			ArrayList<?> parameters = p_vo.getParameters();
			for (int i = 0; i < parameters.size(); i++)
			{
				Object o = parameters.get(i);
				if (o == null)
					throw new IllegalArgumentException ("Local - Null Parameter #" + i);
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
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, "local", ex);
			try
			{
				if (pstmt != null)
					pstmt.close();
				pstmt = null;
			}
			catch (SQLException ex1)
			{
			}
		}
		return pstmt;
	}	//	local_getPreparedStatement


	/**
	 * 	Get Result as RowSet for local system.
	 * 	Get explicit connection as connection is closed when closing RowSet
	 *	@return result as RowSet
	 */
	public RowSet local_getRowSet()
	{
		log.finest("local");
		//	dedicated connection
		Connection conn = DB.createConnection (false, Connection.TRANSACTION_READ_COMMITTED);
		PreparedStatement pstmt = null;
		RowSet rowSet = null;
		try
		{
			//jz pstmt = conn.prepareStatement(p_vo.getSql(),
			pstmt = conn.prepareStatement(DB.getDatabase().convertStatement(p_vo.getSql()),
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
			//jz DB2 error: transaction is in active
			if (!DB.isDB2())
				conn.close();
			conn = null;
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, p_vo.toString(), ex);
			ex.printStackTrace();
			//throw new RuntimeException (ex);
		}
		//	Close Cursor
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
			//jz DB2 error: transaction is in active
			if (!DB.isDB2())
			{
				if (conn != null)
					conn.close();
			}
			conn = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "close", e);
		}
		return rowSet;
	}	//	local_getRowSet

	/*************************************************************************
	 * 	Get Result as RowSet for Remote.
	 * 	Get shared connection for RMI!
	 * 	If RowSet is transfred via RMI, closing the RowSet does not close the connection
	 *	@return result as RowSet
	 */
	@Override
	public RowSet remote_getRowSet()
	{
		log.finest("remote");
		//	shared connection
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

			if (trx != null)
				trx.close();
		}
		return rowSet;
	}	//	remote_getRowSet

	/*************************************************************************
	 * 	Execute Update.
	 *	@return row count
	 */
	@Override
	public int remote_executeUpdate()
	{
		log.finest("Update");
		try
		{
			CompiereDatabase db = CConnection.get().getDatabase();
			if (db == null)
				throw new NullPointerException("Remote - No Database");
			//
			Trx trx = Trx.getAutoCommitTrx();
			PreparedStatement pstmt = local_getPreparedStatement (trx.getConnection());	//	shared connection
			int result = pstmt.executeUpdate();
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
	 * 	setAsciiStream
	 *	@see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream)
	 *	@param parameterIndex
	 *	@param x
	 *	@throws SQLException
	 */
	public void setAsciiStream(int parameterIndex, InputStream x)
	throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setAsciiStream (parameterIndex, x);
		else
			throw new java.lang.UnsupportedOperationException ("Method setAsciiStream() not yet implemented.");
	}

	/**
	 * 	setAsciiStream
	 *	@see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, long)
	 *	@param parameterIndex
	 *	@param x
	 *	@param length
	 *	@throws SQLException
	 */
	public void setAsciiStream(int parameterIndex, InputStream x, long length)
	throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setAsciiStream (parameterIndex, x, length);
		else
			throw new java.lang.UnsupportedOperationException ("Method setAsciiStream() not yet implemented.");
	}

	/**
	 * 	setBinaryStream
	 *	@see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream)
	 *	@param parameterIndex
	 *	@param x
	 *	@throws SQLException
	 */
	public void setBinaryStream(int parameterIndex, InputStream x)
	throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setBinaryStream (parameterIndex, x);
		else
			throw new java.lang.UnsupportedOperationException ("Method setBinaryStream() not yet implemented.");
	}

	/**
	 * 	setBinaryStream
	 *	@see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, long)
	 *	@param parameterIndex
	 *	@param x
	 *	@param length
	 *	@throws SQLException
	 */
	public void setBinaryStream(int parameterIndex, InputStream x, long length)
	throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setBinaryStream (parameterIndex, x, length);
		else
			throw new java.lang.UnsupportedOperationException ("Method setBinaryStream() not yet implemented.");
	}

	/**
	 * 	setBlob
	 *	@see java.sql.PreparedStatement#setBlob(int, java.io.InputStream)
	 *	@param parameterIndex
	 *	@param inputStream
	 *	@throws SQLException
	 */
	public void setBlob(int parameterIndex, InputStream inputStream)
	throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setBlob (parameterIndex, inputStream);
		else
			throw new java.lang.UnsupportedOperationException ("Method setBlob() not implemented.");
	}

	/**
	 * 	setBlob
	 *	@see java.sql.PreparedStatement#setBlob(int, java.io.InputStream, long)
	 *	@param parameterIndex
	 *	@param inputStream
	 *	@param length
	 *	@throws SQLException
	 */
	public void setBlob(int parameterIndex, InputStream inputStream, long length)
	throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setBlob (parameterIndex, inputStream, length);
		else
			throw new java.lang.UnsupportedOperationException ("Method setBlob() not implemented.");
	}

	/**
	 * 	setCharacterStream
	 *	@see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader)
	 *	@param parameterIndex
	 *	@param reader
	 *	@throws SQLException
	 */
	public void setCharacterStream(int parameterIndex, Reader reader)
	throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setCharacterStream (parameterIndex, reader);
		else
			throw new java.lang.UnsupportedOperationException ("Method setCharacterStream() not implemented.");
	}

	/**
	 * 	setCharacterStream
	 *	@see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, long)
	 *	@param parameterIndex
	 *	@param reader
	 *	@param length
	 *	@throws SQLException
	 */
	public void setCharacterStream(int parameterIndex, Reader reader, long length)
	throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setCharacterStream (parameterIndex, reader, length);
		else
			throw new java.lang.UnsupportedOperationException ("Method setCharacterStream() not yet implemented.");
	}

	/**
	 * 	setClob
	 *	@see java.sql.PreparedStatement#setClob(int, java.io.Reader)
	 *	@param parameterIndex
	 *	@param reader
	 *	@throws SQLException
	 */
	public void setClob(int parameterIndex, Reader reader)
	throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setClob (parameterIndex, reader);
		else
			throw new java.lang.UnsupportedOperationException ("Method setClob() not implemented.");
	}

	/**
	 * 	setClob
	 *	@see java.sql.PreparedStatement#setClob(int, java.io.Reader, long)
	 *	@param parameterIndex
	 *	@param reader
	 *	@param length
	 *	@throws SQLException
	 */
	public void setClob(int parameterIndex, Reader reader, long length)
	throws SQLException
	{
		if (m_stmt != null)
			((PreparedStatement)m_stmt).setClob (parameterIndex, reader, length);
		else
			throw new java.lang.UnsupportedOperationException ("Method setClob() not implemented.");
	}

	/**
	 * 	setNCharacterStream
	 *	@see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader)
	 *	@param parameterIndex
	 *	@param value
	 *	@throws SQLException
	 */
	public void setNCharacterStream(int parameterIndex, Reader value)
	throws SQLException
	{
		// TODO Auto-generated method stub

	}

	/**
	 * 	setNCharacterStream
	 *	@see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader, long)
	 *	@param parameterIndex
	 *	@param value
	 *	@param length
	 *	@throws SQLException
	 */
	public void setNCharacterStream(int parameterIndex, Reader value, long length)
	throws SQLException
	{
		// TODO Auto-generated method stub

	}

	/**
	 * 	setNClob
	 *	@see java.sql.PreparedStatement#setNClob(int, java.sql.NClob)
	 *	@param parameterIndex
	 *	@param value
	 *	@throws SQLException
	 */
	public void setNClob(int parameterIndex, NClob value)
	throws SQLException
	{
		// TODO Auto-generated method stub

	}

	/**
	 * 	setNClob
	 *	@see java.sql.PreparedStatement#setNClob(int, java.io.Reader)
	 *	@param parameterIndex
	 *	@param reader
	 *	@throws SQLException
	 */
	public void setNClob(int parameterIndex, Reader reader)
	throws SQLException
	{
		// TODO Auto-generated method stub

	}

	/**
	 * 	setNClob
	 *	@see java.sql.PreparedStatement#setNClob(int, java.io.Reader, long)
	 *	@param parameterIndex
	 *	@param reader
	 *	@param length
	 *	@throws SQLException
	 */
	public void setNClob(int parameterIndex, Reader reader, long length)
	throws SQLException
	{
		// TODO Auto-generated method stub

	}

	/**
	 * 	setNString
	 *	@see java.sql.PreparedStatement#setNString(int, java.lang.String)
	 *	@param parameterIndex
	 *	@param value
	 *	@throws SQLException
	 */
	public void setNString(int parameterIndex, String value)
	throws SQLException
	{
		// TODO Auto-generated method stub

	}

	/**
	 * 	setRowId
	 *	@see java.sql.PreparedStatement#setRowId(int, java.sql.RowId)
	 *	@param parameterIndex
	 *	@param x
	 *	@throws SQLException
	 */
	public void setRowId(int parameterIndex, RowId x)
	throws SQLException
	{
		// TODO Auto-generated method stub

	}

	/**
	 * 	setSQLXML
	 *	@see java.sql.PreparedStatement#setSQLXML(int, java.sql.SQLXML)
	 *	@param parameterIndex
	 *	@param xmlObject
	 *	@throws SQLException
	 */
	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
	throws SQLException
	{
		// TODO Auto-generated method stub

	}



	@Override
	protected void finalize() throws Throwable {
		// finalize() is exceedingly inefficient, but it's better than having
		// connection or statement leaks.
		if( m_conn != null && !m_conn.isClosed() ){
			log.log(Level.WARNING, "Connection not properly closed; closing now; was borrowed from: ", m_borrowStackTrace);
			m_conn.close();
		}
		super.finalize();
	}





}	//	CPreparedStatement

