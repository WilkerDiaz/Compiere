package org.compiere.util;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import org.compiere.common.CompiereSQLException;

public final class QueryUtil {

	public static final int MAX_ROWS = 1000000; 
	private static final CLogger log = CLogger.getCLogger(QueryUtil.class);

	/**
	 * Implement this callback class to generate the record for each row.  Typically used to cast row results.
	 * @author gwu
	 *
	 * @param <T>
	 */
	public interface Callback<T> {
		T cast(Object[] row);
	}

	/**
	 * Execute sql query with provided parameters
	 *  
	 * @param <T>
	 * @param trx
	 * @param SQL
	 * @param callback
	 * @param params
	 * @return
	 */
	public static <T> Iterable<T> executeQuery(Trx trx, String SQL, Callback<T> callback, 
			Object... params)  {
		Object[][] rows = executeQuery(trx, SQL, params);
		List<T> result = new ArrayList<T>();
		for( Object[] row : rows )
			result.add(callback.cast(row));
		
		return result;
	}

	/**
	 * Execute sql query with provided parameters
	 * @param trx
	 * @param SQL
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static Object[][] executeQuery(Trx trx, String SQL, Object... params ) {
		StringBuilder logBuffer = new StringBuilder();

		logBuffer.append("SQL: " + SQL + "\n");

		PreparedStatement pstmt = null;
		Object[][] result = new Object[0][];
		try {
			pstmt = DB.prepareStatement(SQL, trx);

			if (params != null) {
				int i = 1;
				for (Object obj : params) {
					logBuffer.append("  params[" + i + "]: " + params[i - 1]);
					if (params[i - 1] != null)
						logBuffer.append(" (" + params[i - 1].getClass().getSimpleName() + ")");
					logBuffer.append("\n ");

					if (obj instanceof Number) {
						BigDecimal n = new BigDecimal(((Number) obj).toString());
						try {
							pstmt.setInt(i, n.intValueExact());
						} catch (ArithmeticException e) {
							pstmt.setBigDecimal(i, n);
						}
					} else if (obj instanceof Date)
						pstmt.setTimestamp(i, new Timestamp(((Date) obj).getTime()));
					else if (obj instanceof Boolean)
						pstmt.setString(i, ((Boolean) obj).booleanValue() ? "Y" : "N");
					else if (obj instanceof String)
						pstmt.setString(i, (String) obj);
					else
						pstmt.setObject(i, obj);
					++i;
				}

			}
			log.log(Level.FINE, logBuffer.toString());
			result = executeQuery(pstmt);
		} 
		catch (SQLException e) {
			log.log(Level.SEVERE, logBuffer.toString());
			throw new CompiereSQLException(e);
		} 
		finally {
			DB.closeStatement(pstmt);
		}
		return result;
	}
	
	/**
	 * @param pstmt
	 * @return
	 * @throws SQLException
	 */
	private static Object[][] executeQuery(PreparedStatement pstmt) throws SQLException {
		ArrayList<Object[]> result = new ArrayList<Object[]>();
		ResultSet rs = null;
		try {
			rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				ArrayList<Object> row = new ArrayList<Object>();
				for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
					Object obj = rs.getObject(i);
					if (obj instanceof Number)
						row.add(rs.getBigDecimal(i));
					else if (obj instanceof Date)
						row.add(rs.getTimestamp(i));
					else
						row.add(rs.getString(i));
				}
				result.add(row.toArray());
			}
		} 
		finally {
			DB.closeResultSet(rs);
		}
		return result.toArray(new Object[0][]);
	}	
	

	/**
	 * 	Get Value from sql
	 * 	@param trx p_trx
	 * 	@param sql sql
	 * 	@return first value or -1
	 */
	public static int getSQLValue (Trx trx, String sql, Object... params)
	{
		if ((sql == null) || (sql.length() == 0))
			throw new IllegalArgumentException("Required parameter missing - " + sql);
	
		int retValue = -1;
		Object[][] rows = executeQuery(trx,sql, params);
		if(rows.length > 0 && rows[0][0] != null)
			retValue = ((BigDecimal)rows[0][0]).intValue();

		return retValue;
	}	

	/**
	 * 	Get String Value from sql
	 * 	@param trx p_trx
	 * 	@param sql sql
	 * 	@param params parameter 1
	 * 	@return first value or null
	 */
	public static String getSQLValueString (Trx trx, String sql, Object... params)
	{
		String retValue = null;
		Object[][] rows = executeQuery(trx,sql, params);
		if(rows.length > 0 && rows[0][0] != null)
			retValue = (String)rows[0][0];

		return retValue;
	}	

	/**
	 * 	Get BigDecimal Value from sql
	 * 	@param trx p_trx
	 * 	@param sql sql
	 * 	@param params parameter 1
	 * 	@return first value or null
	 */
	public static BigDecimal getSQLValueBD (Trx trx, String sql, Object... params)
	{
		BigDecimal retValue = null;
		Object[][] rows = executeQuery(trx, sql, params);
		if(rows.length > 0 && rows[0][0] != null)
			retValue = (BigDecimal)rows[0][0];

		return retValue;
	}
	
	/**
	 * Execute sql query with provided parameters - limit ResultSet to MAX_ROWS
	 * @param <T>
	 * @param trx
	 * @param SQL
	 * @param callback
	 * @param params
	 * @return
	 */
	public static <T> Iterable<T> executeQueryMaxRows(Trx trx, String SQL, Callback<T> callback,
			Object[] params) {
		Object[][] rows = executeQueryMaxRows(trx, SQL, params, 0, MAX_ROWS);
		List<T> result = new ArrayList<T>();
		for( Object[] row : rows )
			result.add(callback.cast(row));

		return result;
	}
	

	/**
	 * Execute sql query with provided parameters - limit ResultSet to MAX_ROWS
	 * @param trx
	 * @param SQL
	 * @param params
	 * @return
	 */
	public static Object[][] executeQueryMaxRows(Trx trx, String SQL,
			Object[] params) {
		return executeQueryMaxRows(trx, SQL, params, 0, MAX_ROWS);
	}

	/**
	 * Execute sql query with provided parameters - limit ResultSet to MAX_ROWS
	 * @param SQL
	 * @param params
	 * @return
	 */
	public static Object[][] executeQueryMaxRows(String SQL, Object[] params) {
		return executeQueryMaxRows((Trx) null, SQL, params);
	}

	/**
	 * 
	 * @param trx
	 * 			  The trx
	 * @param SQL
	 * @param params
	 * @param startRow
	 *            The row (zero-based) to start with
	 * @param rowCount
	 *            The number of rows to return
	 * @return
	 * @throws SQLException
	 */
	public static Object[][] executeQueryMaxRows(Trx trx, String SQL, Object[] params, int startRow, int rowCount) {
		StringBuilder logBuffer = new StringBuilder();

		logBuffer.append("SQL: " + SQL + "\n");

		PreparedStatement pstmt = null;
		Object[][] result = new Object[0][];
		try {
			pstmt = DB.prepareStatement(SQL, trx);
			pstmt.setMaxRows(startRow + rowCount);

			if (params != null) {
				int i = 1;
				for (Object obj : params) {
					logBuffer.append("  params[" + i + "]: " + params[i - 1]);
					if (params[i - 1] != null)
						logBuffer.append(" (" + params[i - 1].getClass().getSimpleName() + ")");
					logBuffer.append("\n ");

					if (obj instanceof Number) {
						BigDecimal n = new BigDecimal(((Number) obj).toString());
						try {
							pstmt.setInt(i, n.intValueExact());
						} catch (ArithmeticException e) {
							pstmt.setBigDecimal(i, n);
						}
					} else if (obj instanceof Date)
						pstmt.setTimestamp(i, new Timestamp(((Date) obj).getTime()));
					else if (obj instanceof Boolean)
						pstmt.setString(i, ((Boolean) obj).booleanValue() ? "Y" : "N");
					else if (obj instanceof String)
						pstmt.setString(i, (String) obj);
					else
						pstmt.setObject(i, obj);
					++i;
				}

			}
			log.log(Level.FINE, logBuffer.toString());
			result = executeQueryMaxRows(pstmt, startRow, rowCount);
		} 
		catch (SQLException e) {
			log.log(Level.SEVERE, logBuffer.toString());
			throw new CompiereSQLException(e);
		} 
		finally {
			DB.closeStatement(pstmt);
		}
		return result;

	}

	/**
	 * @param pstmt
	 * @param startRow
	 * @param rowCount
	 * @return
	 * @throws SQLException
	 */
	private static Object[][] executeQueryMaxRows(PreparedStatement pstmt, int startRow, int rowCount) throws SQLException {
		ArrayList<Object[]> result = new ArrayList<Object[]>();
		ResultSet rs = null;
		try {
			rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int rowNum = 0;
			while (rowNum < startRow && rs.next()) {
				++rowNum;
			}
			while (rowNum < startRow + rowCount && rs.next()) {
				ArrayList<Object> row = new ArrayList<Object>();
				for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
					Object obj = rs.getObject(i);
					if (obj instanceof Number)
						row.add(rs.getBigDecimal(i));
					else if (obj instanceof Date)
						row.add(rs.getTimestamp(i));
					else
						row.add(rs.getString(i));
				}
				result.add(row.toArray());
				++rowNum;
			}
		} 
		finally {
			DB.closeResultSet(rs);
		}
		return result.toArray(new Object[0][]);
	}
}
