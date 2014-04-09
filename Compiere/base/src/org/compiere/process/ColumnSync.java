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
package org.compiere.process;

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.common.constants.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Synchronize Column with Database
 *
 *  @author Jorg Janke
 *  @version $Id: ColumnSync.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class ColumnSync extends SvrProcess
{
	/** The Column				*/
	private int			p_AD_Column_ID = 0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_AD_Column_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("C_Column_ID=" + p_AD_Column_ID);
		if (p_AD_Column_ID == 0)
			throw new CompiereUserException("@No@ @AD_Column_ID@");
		MColumn column = new MColumn (getCtx(), p_AD_Column_ID, get_TrxName());
		if (column.get_ID() == 0)
			throw new CompiereUserException("@NotFound@ @AD_Column_ID@ " + p_AD_Column_ID);

		MTable table = MTable.get(getCtx(), column.getAD_Table_ID());
		if (table.get_ID() == 0)
			throw new CompiereUserException("@NotFound@ @AD_Table_ID@ " + column.getAD_Table_ID());

		Trx trx = Trx.get("getDatabaseMetaData");

		//	Find Column in Database
		DatabaseMetaData md = trx.getConnection().getMetaData();
		String catalog = DB.getDatabase().getCatalog();
		String schema = DB.getDatabase().getSchema();
		String tableName = table.getTableName();
		if (md.storesUpperCaseIdentifiers())
			tableName = tableName.toUpperCase();
		if (md.storesLowerCaseIdentifiers())
			tableName = tableName.toLowerCase();
		int noColumns = 0;
		String sql = null;
		//
		ResultSet rs = md.getColumns(catalog, schema, tableName, null);
		while (rs.next())
		{
			noColumns++;
			String columnName = rs.getString ("COLUMN_NAME");
			if (!columnName.equalsIgnoreCase(column.getColumnName()))
				continue;

			//	update existing column
			boolean notNull = DatabaseMetaData.columnNoNulls == rs.getInt("NULLABLE");
			if (column.isVirtualColumn())
				sql = "ALTER TABLE " + table.getTableName()
					+ " DROP COLUMN " + columnName;
			else
				sql = column.getSQLModify(table, true, column.isMandatory() != notNull);
			break;
		}
		rs.close();
		rs = null;
		trx.close();

		//	No Table
		if (noColumns == 0)
			sql = table.getSQLCreate ();
		//	No existing column
		else if (sql == null)
		{
			if (column.isVirtualColumn())
				return "@IsVirtualColumn@";
			sql = column.getSQLAdd(table);
		}

		int no = 0;
		if (sql.indexOf(DB.SQLSTATEMENT_SEPARATOR) == -1)
		{
			no = DB.executeUpdate(get_TrxName(), sql);
			addLog (0, null, new BigDecimal(no), sql);
		}
		else
		{
			String statements[] = sql.split(DB.SQLSTATEMENT_SEPARATOR);
			for (String element : statements)
			{
				int count = DB.executeUpdate(get_TrxName(), element);
				addLog (0, null, new BigDecimal(count), element);
				no += count;
			}
		}

		if (no == -1)
		{
			String msg = "@Error@ ";
			ValueNamePair pp = CLogger.retrieveError();
			if (pp != null)
				msg = pp.getName() + " - ";
			msg += sql;
			throw new CompiereUserException (msg);
		}
		String r = createFK();
		return sql + "; " + r;
	}	//	doIt

	/**
	 * 	Create FK
	 *	@param fk fk
	 */
	private String createFK() throws Exception
	{
		String returnMessage = "";
		if (p_AD_Column_ID == 0)
			throw new CompiereUserException("@No@ @AD_Column_ID@");
		MColumn column = new MColumn (getCtx(), p_AD_Column_ID, get_TrxName());
		if (column.get_ID() == 0)
			throw new CompiereUserException("@NotFound@ @AD_Column_ID@ " + p_AD_Column_ID);

		MTable table = MTable.get(getCtx(), column.getAD_Table_ID());
		if (table.get_ID() == 0)
			throw new CompiereUserException("@NotFound@ @AD_Table_ID@ " + column.getAD_Table_ID());

		String fk;
		if ((column.getAD_Reference_ID() == DisplayTypeConstants.Account)
			&& !(column.getColumnName().equalsIgnoreCase("C_ValidCombination_ID")))
		{
			fk = "SELECT t.TableName, c.ColumnName, c.AD_Column_ID,"
				+ " cPK.AD_Column_ID, tPK.TableName, cPK.ColumnName, c.ConstraintType,"
				+ " 'FK' || t.AD_Table_ID || '_' || c.AD_Column_ID AS ConstraintName "
				+ "FROM AD_Table t"
				+ " INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID)"
				+ " INNER JOIN AD_Column cPK ON (cPK.AD_Column_ID=1014)"
				+ " INNER JOIN AD_Table tPK ON (cPK.AD_Table_ID=tPK.AD_Table_ID) "
				+ "WHERE c.IsKey='N' AND c.AD_Reference_ID=25 AND C.AD_Column_ID= ?"	//	Acct
				+ " AND c.ColumnName<>'C_ValidCombination_ID'"
				+ " AND t.IsView='N' "
				+ "ORDER BY t.TableName, c.ColumnName";
		}
		else if ((column.getAD_Reference_ID() == DisplayTypeConstants.PAttribute)
			&& !(column.getColumnName().equalsIgnoreCase("C_ValidCombination_ID")))
		{
			fk="SELECT t.TableName, c.ColumnName, c.AD_Column_ID,"
				+ " cPK.AD_Column_ID, tPK.TableName, cPK.ColumnName, c.ConstraintType,"
				+ " 'FK' || t.AD_Table_ID || '_' || c.AD_Column_ID AS ConstraintName "
				+ "FROM AD_Table t"
				+ " INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID)"
				+ " INNER JOIN AD_Column cPK ON (cPK.AD_Column_ID=8472)"
				+ " INNER JOIN AD_Table tPK ON (cPK.AD_Table_ID=tPK.AD_Table_ID) "
				+ "WHERE c.IsKey='N' AND c.AD_Reference_ID=35 AND C.AD_Column_ID=?"	//	Product Attribute
				+ " AND c.ColumnName<>'C_ValidCombination_ID'"
				+ " AND t.IsView='N' "
				+ "ORDER BY t.TableName, c.ColumnName";
		}
		else if (((column.getAD_Reference_ID() == DisplayTypeConstants.TableDir) || (column.getAD_Reference_ID() == DisplayTypeConstants.Search))
			&& (column.getAD_Reference_Value_ID() == 0))
		{
			fk="SELECT t.TableName, c.ColumnName, c.AD_Column_ID,"
				+ " cPK.AD_Column_ID, tPK.TableName, cPK.ColumnName, c.ConstraintType,"
				+ " 'FK' || t.AD_Table_ID || '_' || c.AD_Column_ID AS ConstraintName "
				+ "FROM AD_Table t"
				+ " INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID)"
				+ " INNER JOIN AD_Column cPK ON (c.AD_Element_ID=cPK.AD_Element_ID AND cPK.IsKey='Y')"
				+ " INNER JOIN AD_Table tPK ON (cPK.AD_Table_ID=tPK.AD_Table_ID AND tPK.IsView='N') "
				+ "WHERE c.IsKey='N' AND c.AD_Reference_Value_ID IS NULL AND C.AD_Column_ID=?"
				+ " AND t.IsView='N' AND c.ColumnSQL IS NULL "
				+ "ORDER BY t.TableName, c.ColumnName";
		}
		else //	Table
		{
			fk = "SELECT t.TableName, c.ColumnName, c.AD_Column_ID,"
				+ " cPK.AD_Column_ID, tPK.TableName, cPK.ColumnName, c.ConstraintType,"
				+ " 'FK' || t.AD_Table_ID || '_' || c.AD_Column_ID AS ConstraintName "
				+ "FROM AD_Table t"
				+ " INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID AND c.AD_Reference_Value_ID IS NOT NULL)"
				+ " INNER JOIN AD_Ref_Table rt ON (c.AD_Reference_Value_ID=rt.AD_Reference_ID)"
				+ " INNER JOIN AD_Column cPK ON (rt.Column_Key_ID=cPK.AD_Column_ID)"
				+ " INNER JOIN AD_Table tPK ON (cPK.AD_Table_ID=tPK.AD_Table_ID) "
				+ "WHERE c.IsKey='N'"
				+ " AND t.IsView='N' AND c.ColumnSQL IS NULL AND C.AD_Column_ID=?"
				+ "ORDER BY t.TableName, c.ColumnName";
		}

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			/*Find foreign key relation in Database
			 * */
			Trx trx = Trx.get("getDatabaseMetaData");
			DatabaseMetaData md = trx.getConnection().getMetaData();
			String catalog = DB.getDatabase().getCatalog();
			String schema = DB.getDatabase().getSchema();
			String tableName = table.getTableName();
			if (md.storesUpperCaseIdentifiers())
				tableName = tableName.toUpperCase();
			if (md.storesLowerCaseIdentifiers())
				tableName = tableName.toLowerCase();
			String dropsql = null;
			int no=0;

			String constraintNameDB = null;
			String PKTableNameDB = null;
			String PKColumnNameDB = null;
			int constraintTypeDB = 0;

			/* Get foreign key information from DatabaseMetadata
			 * */
			rs = md.getImportedKeys(catalog, schema, tableName);
			while (rs.next())
			{
				String fkcolumnName = rs.getString ("FKCOLUMN_NAME");

				if (fkcolumnName.equalsIgnoreCase(column.getColumnName()))
				{
					constraintNameDB = rs.getString("FK_NAME");
					PKTableNameDB = rs.getString("PKTABLE_NAME");
					PKColumnNameDB = rs.getString("PKCOLUMN_NAME");
					constraintTypeDB=rs.getShort("DELETE_RULE");
					break;
				}
			}
			rs.close();
			rs = null;
			trx.close();


			pstmt = DB.prepareStatement(fk,get_TrxName());
			pstmt.setInt (1, column.get_ID());
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				String TableName = rs.getString (1);
				String ColumnName = rs.getString (2);
			//	int AD_Column_ID = rs.getInt (3);
			//	int PK_Column_ID = rs.getInt (4);
				String PKTableName = rs.getString (5);
				String PKColumnName = rs.getString (6);
				String ConstraintType = rs.getString (7);
				String ConstraintName = rs.getString (8);

				/* verify if the constraint in DB is different than the one to be created */
				boolean modified = true;
				if(constraintNameDB != null)
				{
					if(((constraintNameDB.equalsIgnoreCase(ConstraintName))
							&& (PKTableNameDB != null) && (PKTableNameDB.equalsIgnoreCase(PKTableName))
							&& (PKColumnNameDB != null) && (PKColumnNameDB.equalsIgnoreCase(PKColumnName))
							&& ((constraintTypeDB==DatabaseMetaData.importedKeyRestrict)&&
									(X_AD_Column.CONSTRAINTTYPE_Restrict.equals(ConstraintType)
											|| X_AD_Column.CONSTRAINTTYPE_RestrictTrigger.equals(ConstraintType))))
											||
											((constraintTypeDB==DatabaseMetaData.importedKeyCascade)&&
													(X_AD_Column.CONSTRAINTTYPE_Cascade.equals(ConstraintType)
															|| X_AD_Column.CONSTRAINTTYPE_CascadeTrigger.equals(ConstraintType)))
															||
															((constraintTypeDB==DatabaseMetaData.importedKeySetNull)&&
																	(X_AD_Column.CONSTRAINTTYPE_Null.equals(ConstraintType)))

					)
					{
						modified = false;
					}

					else
					{
						dropsql = "ALTER TABLE " + table.getTableName()
						+ " DROP CONSTRAINT " + constraintNameDB;
					}
				}
				if(modified)
				{
					StringBuffer sql = null;
					try
					{
						if(dropsql != null)
						{
							/* Drop the existing constraint */
							no = DB.executeUpdate(get_TrxName(), dropsql);
							addLog (0, null, new BigDecimal(no), dropsql);
						}
						/* Now create the sql foreign key constraint */
						sql = new StringBuffer("ALTER TABLE ")
							.append(TableName)
							.append(" ADD CONSTRAINT ").append(ConstraintName)
							.append(" FOREIGN KEY (").append(ColumnName)
							.append(") REFERENCES ").append (PKTableName)
							.append(" (").append (PKColumnName).append(")");
						boolean createfk=true;
						if (ConstraintType != null)
						{
							if (X_AD_Column.CONSTRAINTTYPE_DoNOTCreate.equals(ConstraintType))
								createfk=false;
							else if (X_AD_Column.CONSTRAINTTYPE_Restrict.equals(ConstraintType)
								|| X_AD_Column.CONSTRAINTTYPE_RestrictTrigger.equals(ConstraintType))
								;
							else if (X_AD_Column.CONSTRAINTTYPE_Cascade.equals(ConstraintType)
								|| X_AD_Column.CONSTRAINTTYPE_CascadeTrigger.equals(ConstraintType))
								sql.append (" ON DELETE CASCADE");
							else if (X_AD_Column.CONSTRAINTTYPE_Null.equals(ConstraintType)
								|| X_AD_Column.CONSTRAINTTYPE_NullTrigger.equals(ConstraintType))
								sql.append (" ON DELETE SET NULL");
						}
						/* Create the constraint */
						if(createfk)
						{
							no = DB.executeUpdate (get_TrxName(),sql.toString());
							addLog (0, null, new BigDecimal(no), sql.toString());
							if (no != -1)
							{
								log.finer (ConstraintName + " - " + TableName + "." + ColumnName);
								returnMessage = sql.toString();
							}
							else
							{
								log.info(ConstraintName + " - " + TableName + "." + ColumnName
									+ " - ReturnCode=" + no);
								returnMessage = "FAILED:"+sql.toString();
							}
						} //if createfk

					}
					catch (Exception e)
					{
						log.log (Level.SEVERE, sql.toString() + " - " + e.toString ());

					}
				} // modified
			}	//	rs.next
			else
			{
				if(constraintNameDB != null && constraintNameDB.equalsIgnoreCase("FK"+column.getAD_Table_ID()+"_"+p_AD_Column_ID))
				{
					dropsql = "ALTER TABLE " + table.getTableName()
					+ " DROP CONSTRAINT " + constraintNameDB;
					
					/* Drop the existing constraint */
					no = DB.executeUpdate(get_TrxName(), dropsql);
					addLog (0, null, new BigDecimal(no), dropsql);
					returnMessage = dropsql.toString();					
				}
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, fk, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return returnMessage;
	}	//	createFK
}	//	ColumnSync
