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

package org.compiere.tools;

import java.sql.*;
import java.util.*;

import org.compiere.*;
import org.compiere.common.CompiereStateException;
import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Generate Delta or History Tables
 *	@author Jorg Janke
 */
public class SubTableUtil
{
	/**
	 * 	Generate Sub Table Infrastructure
	 * 	@param ctx ctx
	 *	@param tableName table Name
	 *	@param userDef if true create UserDef otherwise SysDef
	 */
	public SubTableUtil (Ctx ctx, String tableName, boolean userDef)
	{
		this(ctx, tableName, userDef, false);
	}	//	SubTableUtil

	/**
	 * 	Generate History Infrastructure
	 * 	@param ctx ctx
	 *	@param tableName table Name
	 */
	public SubTableUtil (Ctx ctx, String tableName)
	{
		this(ctx, tableName, false, true);
	}	//	SubTableUtil

	/**
	 * 	Generate Delta Infrastructure
	 * 	@param ctx ctx
	 *	@param tableName table Name
	 *	@param userDef if true create UserDef otherwise SysDef
	 *	@param history history structure
	 */
	private SubTableUtil (Ctx ctx, String tableName, boolean userDef, boolean history)
	{
		m_ctx = ctx;
		m_baseTable = MTable.get(ctx, tableName);
		if (m_baseTable == null)
			throw new IllegalArgumentException("Not found: " + tableName);
		m_userDef = userDef;
		m_history = history;
		int index = tableName.indexOf("_");
		m_dTableName = tableName.substring(0,index)
			+ (m_userDef ? "_UserDef" : "_SysDef")
			+ tableName.substring(index);
		if (history)	//	Default=Each
			m_dTableName = tableName + "H";
		else
			m_derivedTableType = userDef ? 
				X_AD_Table.SUBTABLETYPE_Delta_User : X_AD_Table.SUBTABLETYPE_Delta_System; 
		m_vTableName = tableName + "_v";
		log.info(tableName + " + " + m_dTableName + " = " + m_vTableName);
	}	//	SubTableUtil

	/** The base table		*/
	private Ctx			m_ctx;
	/** The base table		*/
	private MTable		m_baseTable;
	/** The derived table		*/
	private MTable		m_derivedTable;
	/**	Sub Table Type			*/
	private String		m_derivedTableType = X_AD_Table.SUBTABLETYPE_History_Each;
	/** The view table		*/
	private MTable		m_viewTable;
	/** UserDef or SysDef Delta	*/
	private boolean		m_userDef;
	/** History				*/
	private boolean		m_history;
	/** The delta table name	*/
	private String 		m_dTableName;
	/** The view		 name	*/
	private String 		m_vTableName;
	
	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger(SubTableUtil.class);

	
	/**
	 * 	Generate Table & View
	 *	@return true if generated
	 */
	boolean generate()
	{
		try
		{
			checkStandardColumns(m_ctx, m_baseTable.getTableName());
			generateTable();
			checkStandardColumns(m_ctx, m_derivedTable.getTableName());

			if (!m_history)
				generateView();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}	//	generate
	
	/**
	 * 	Generate Delta or History table
	 *	@return true
	 */
	private boolean generateTable() throws Exception
	{
		//	Table
		m_derivedTable = MTable.get(m_ctx, m_dTableName); 
		if (m_derivedTable == null)
			m_derivedTable = new MTable(m_ctx, 0, null);
		
		PO.copyValues(m_baseTable, m_derivedTable);
		m_derivedTable.setTableName(m_dTableName);
		m_derivedTable.setName(m_derivedTable.getName() + " SubTable");
		m_derivedTable.setSubTableType(m_derivedTableType);
		m_derivedTable.setBase_Table_ID(m_baseTable.getAD_Table_ID());
		if (!m_derivedTable.save())
			throw new CompiereStateException("Cannot save " + m_dTableName);

		MColumn[] dCols = syncMColumns(true);
		
		//	Sync Columns in Database
		ArrayList<MColumn> list = new ArrayList<MColumn>(dCols.length);
		for (MColumn element : dCols)
			list.add(element);
		Trx trx = Trx.get("getDatabaseMetaData");
		//
		DatabaseMetaData md = trx.getConnection().getMetaData();
		String catalog = DB.getDatabase().getCatalog();
		String schema = DB.getDatabase().getSchema();
		String tableName = m_dTableName;
		if (md.storesUpperCaseIdentifiers())
			tableName = tableName.toUpperCase();
		if (md.storesLowerCaseIdentifiers())
			tableName = tableName.toLowerCase();
		int noColumns = 0;
		//
		ResultSet rs = md.getColumns(catalog, schema, tableName, null);
		while (rs.next())
		{
			noColumns++;
			String columnName = rs.getString ("COLUMN_NAME");
			boolean found = false;
			for (int i = 0; i < list.size(); i++)
			{
				MColumn dCol = list.get(i);
				if (columnName.equalsIgnoreCase(dCol.getColumnName()))
				{
					String sql = dCol.getSQLModify(m_derivedTable, true, false);
					DB.executeUpdateMultiple(null, sql);
					found = true;
					list.remove(i);
					break;
				}
			}
			if (!found)
			{
				String sql = "ALTER TABLE " + m_dTableName	
					+ " DROP COLUMN " + columnName;
				DB.executeUpdate((Trx) null, sql);
			}
		}
		rs.close();
		trx.close();
		
		//	No Columns
		if (noColumns == 0)
		{
			String sql = m_derivedTable.getSQLCreate();
			return DB.executeUpdateMultiple(null, sql) >= 0;
		}
		//	New Columns
		for (int i = 0; i < list.size(); i++)
		{
			MColumn dCol = list.get(i);
			if (dCol.isVirtualColumn())
				continue;
			String sql = dCol.getSQLAdd(m_derivedTable);
			DB.executeUpdateMultiple(null, sql);
		}
		return true;
	}	//	generateTable
	
	
	/**
	 * 	Generate Delta View
	 *	@return  true if view created/updated
	 *	@throws Exception
	 */
	private boolean generateView() throws Exception
	{
		//	View
		m_viewTable = MTable.get(m_ctx, m_vTableName); 
		if (m_viewTable == null)
			m_viewTable = new MTable(m_ctx, 0, null);
		
		PO.copyValues(m_baseTable, m_viewTable);
		m_viewTable.setTableName(m_vTableName);
		m_viewTable.setName(m_baseTable.getName() + " View");
		m_viewTable.setIsView(true);
		if (!m_viewTable.save())
			throw new CompiereStateException("Cannot save " + m_vTableName);

		MColumn[] vCols = syncMColumns(false);
		
		//	Create View
		StringBuffer sql = new StringBuffer("CREATE OR REPLACE VIEW ")
			.append(m_vTableName)
			.append(" AS SELECT ");
		for (int i = 0; i < vCols.length; i++)
		{
			if (i > 0)
				sql.append(",");
			MColumn column = vCols[i];
			String columnName = column.getColumnName();
			if (column.isStandardColumn()
				|| column.isKey())
				sql.append("b.").append(columnName);
			else
				sql.append(",COALESCE(b.").append(columnName)
					.append("d.").append(columnName).append(") AS ").append(columnName);
		}
		//	From
		String keyColumnName = m_baseTable.getTableName() + "_ID";
		sql.append(" FROM ").append(m_baseTable.getTableName())
			.append(" b LEFT OUTER JOIN ").append(m_dTableName)
			.append(" d ON (b.").append(keyColumnName)
			.append("=d.").append(keyColumnName).append(")");
		//
		log.info(sql.toString());
		if (DB.executeUpdate((Trx) null, sql.toString()) < 0)
		{
			return false;
		}
		return true;	
	}	//	generateView
	

	/**
	 * 	Synchronize target table with base table in dictionary
	 *	@param derived target derived table - otherwise view 
	 *	@return array of target (derived or view) columns
	 *	@throws Exception
	 */
	private MColumn[] syncMColumns (boolean derived) throws Exception
	{
		MTable target = derived ? m_derivedTable : m_viewTable;
		MTable source = derived ? m_baseTable : m_derivedTable;
		MColumn[] sCols = source.getColumns(false);
		MColumn[] tCols = target.getColumns(false);
		//	Base Columns
		for (MColumn sCol : sCols) {
			MColumn tCol = null;
			for (MColumn column : tCols) {
				if (sCol.getColumnName().equals(column.getColumnName()))
				{
					tCol = column;
					break;
				}
			}
			if (tCol == null)
				tCol = new MColumn(target);
			PO.copyValues(sCol, tCol);
			tCol.setAD_Table_ID(target.getAD_Table_ID());	//	reset parent
			tCol.setIsCallout(false);
			tCol.setCallout(null);
			tCol.setIsMandatory(false);
			tCol.setIsMandatoryUI(false);
			tCol.setIsTranslated(false);
		//	tCol.setIsUpdateable(true);
			if (tCol.isKey())
			{
				tCol.setIsKey(false);
				tCol.setAD_Reference_ID(DisplayTypeConstants.TableDir);
			}
			if (tCol.save())
				throw new CompiereStateException("Cannot save column " + sCol.getColumnName());
		}
		//
		tCols = target.getColumns(true);
		ArrayList<String> addlColumns = new ArrayList<String>();
		if (derived && !m_history)	//	delta only
		{
			//	KeyColumn
			String keyColumnName = target.getTableName() + "_ID";
			MColumn key = target.getColumn(keyColumnName);
			if (key == null)
			{
				key = new MColumn(target);
				M_Element ele = M_Element.get(m_ctx, keyColumnName, target.get_Trx());
				if (ele == null)
				{
					ele = new M_Element(m_ctx, keyColumnName, target.getEntityType(), null);
					ele.save();
				}
				key.setAD_Element_ID(ele.getAD_Element_ID());
				key.setAD_Reference_ID(DisplayTypeConstants.ID);
				key.save();
			}
			addlColumns.add(keyColumnName);
			//	Addl References
			if (m_userDef)
			{
				String colName = "AD_Role_ID";
				addlColumns.add(colName);
				if (target.getColumn(colName) == null)
				{
					MColumn col = new MColumn(target);
					col.setColumnName(colName);
					col.setAD_Reference_ID (DisplayTypeConstants.TableDir);
					createColumn(col, target, false);
					col.setIsUpdateable(false);
					col.setIsMandatory(false);
				}
				colName = "AD_User_ID";
				addlColumns.add(colName);
				if (target.getColumn(colName) == null)
				{
					MColumn col = new MColumn(target);
					col.setColumnName(colName);
					col.setAD_Reference_ID (DisplayTypeConstants.TableDir);
					col.setIsUpdateable(false);
					col.setIsMandatory(false);
					createColumn(col, target, false);
				}
			}
			else	//	System
			{
				String colName = "IsSystemDefault";
				addlColumns.add(colName);
				if (target.getColumn(colName) == null)
				{
					MColumn col = new MColumn(target);
					col.setColumnName(colName);
					col.setAD_Reference_ID(DisplayTypeConstants.YesNo);
					col.setDefaultValue("N");
					col.setIsUpdateable(false);
					col.setIsMandatory(true);
					createColumn(col, target, false);
				}
			}
		}
		

		//	Delete
		for (MColumn tCol : tCols) {
			MColumn sCol = null;
			for (MColumn column : sCols) {
				if (tCol.getColumnName().equals(column.getColumnName()))
				{
					sCol = column;
					break;
				}
			}
			if (sCol == null)
			{
				if (!addlColumns.contains(tCol.getColumnName()))
				{
					if (!tCol.delete(true))
						throw new CompiereStateException("Cannot delete column " 
								+ tCol.getColumnName());
				}
			}
		}
		return tCols;
	}	//	syncMColumns
	
	/**
	 * 	Check Existence of Std columns and create them in AD and DB
	 *	@param ctx context
	 *	@param tableName table name
	 */
	static void checkStandardColumns(Ctx ctx, String tableName)
	{
		MTable table = MTable.get(ctx, tableName);
		checkStandardColumns(table, null);
	}
	
	/**
	 * 	Check Existence of Std columns and create them in AD and DB
	 *	@param table table
	 *	@param EntityType entity type if null defaults to table entity type
	 */
	public static void checkStandardColumns(MTable table, String EntityType)
	{
		if (table == null)
			return;
		if (Util.isEmpty(EntityType))
			EntityType = table.getEntityType();
		table.getColumns(true);		//	get new columns
		//	Key Column
		String colName = table.getTableName() + "_ID";
		if (table.getColumn(colName) == null)
		{
			MColumn col = new MColumn(table);
			col.setColumnName(colName);
			col.setAD_Reference_ID (DisplayTypeConstants.ID);
			col.setIsKey(true);
			col.setIsUpdateable(false);
			col.setIsMandatory(true);
			col.setEntityType(EntityType);
			createColumn(col, table, true);
		}
		colName = "AD_Client_ID";
		if (table.getColumn(colName) == null)
		{
			MColumn col = new MColumn(table);
			col.setColumnName(colName);
			col.setAD_Reference_ID (DisplayTypeConstants.TableDir);
			col.setIsUpdateable(false);
			col.setIsMandatory(true);
			col.setAD_Val_Rule_ID (116);	//	Client Login
			col.setDefaultValue("@#AD_Client_ID@");
			col.setConstraintType(X_AD_Column.CONSTRAINTTYPE_Restrict);
			col.setEntityType(EntityType);
			createColumn(col, table, true);
		}
		colName = "AD_Org_ID";
		if (table.getColumn(colName) == null)
		{
			MColumn col = new MColumn(table);
			col.setColumnName(colName);
			col.setAD_Reference_ID (DisplayTypeConstants.TableDir);
			col.setIsUpdateable(false);
			col.setIsMandatory(true);
			col.setAD_Val_Rule_ID (104);	//	Org Security
			col.setDefaultValue("@#AD_Org_ID@");
			col.setConstraintType(X_AD_Column.CONSTRAINTTYPE_Restrict);
			col.setEntityType(EntityType);
			createColumn(col, table, true);
		}
		colName = "Created";
		if (table.getColumn(colName) == null)
		{
			MColumn col = new MColumn(table);
			col.setColumnName(colName);
			col.setAD_Reference_ID (DisplayTypeConstants.DateTime);
			col.setIsUpdateable(false);
			col.setIsMandatory(true);
			col.setEntityType(EntityType);
			createColumn(col, table, true);
		}
		colName = "Updated";
		if (table.getColumn(colName) == null)
		{
			MColumn col = new MColumn(table);
			col.setColumnName(colName);
			col.setAD_Reference_ID (DisplayTypeConstants.DateTime);
			col.setIsUpdateable(false);
			col.setIsMandatory(true);
			col.setEntityType(EntityType);
			createColumn(col, table, true);
		}
		colName = "CreatedBy";
		if (table.getColumn(colName) == null)
		{
			MColumn col = new MColumn(table);
			col.setColumnName(colName);
			col.setAD_Reference_ID (DisplayTypeConstants.Table);
			col.setAD_Reference_Value_ID (110);
			col.setConstraintType(X_AD_Column.CONSTRAINTTYPE_DoNOTCreate);
			col.setIsUpdateable(false);
			col.setIsMandatory(true);
			col.setEntityType(EntityType);
			createColumn(col, table, true);
		}
		colName = "UpdatedBy";
		if (table.getColumn(colName) == null)
		{
			MColumn col = new MColumn(table);
			col.setColumnName(colName);
			col.setAD_Reference_ID (DisplayTypeConstants.Table);
			col.setAD_Reference_Value_ID (110);
			col.setConstraintType(X_AD_Column.CONSTRAINTTYPE_DoNOTCreate);
			col.setIsUpdateable(false);
			col.setIsMandatory(true);
			col.setEntityType(EntityType);
			createColumn(col, table, true);
		}
		colName = "IsActive";
		if (table.getColumn(colName) == null)
		{
			MColumn col = new MColumn(table);
			col.setColumnName(colName);
			col.setAD_Reference_ID (DisplayTypeConstants.YesNo);
			col.setDefaultValue("Y");
			col.setIsUpdateable(true);
			col.setIsMandatory(true);
			col.setEntityType(EntityType);
			createColumn(col, table, true);
		}
	}	//	checkStandardColumns
	
	/**
	 * 	Create Column in AD and DB
	 *	@param col column
	 *	@param table table
	 *	@param alsoInDB also in database
	 *	@return true if created
	 */
	private static boolean createColumn (MColumn col, MTable table, boolean alsoInDB)
	{
		//	Element
		M_Element element = M_Element.get (col.getCtx(), col.getColumnName(), col.get_Trx());
		if (element == null)
		{
			element = new M_Element (col.getCtx(), col.getColumnName(),
					col.getEntityType(), null);
			if (!element.save())
				return false;
			log.info("Created Element: " + element.getColumnName()); 
		}
		//	Column Sync
		col.setColumnName (element.getColumnName());
		col.setName (element.getName());
		col.setDescription (element.getDescription());
		col.setHelp (element.getHelp());
		col.setAD_Element_ID (element.getAD_Element_ID());
		//
		if (!col.save())
			return false;
		
		//	DB
		if (!alsoInDB)
			return true;
		//
		String sql = col.getSQLAdd(table);
		boolean success = DB.executeUpdateMultiple(table.get_Trx(), sql) >= 0;
		if (success)
			log.info("Created: " + table.getTableName() 
				+ "." + col.getColumnName());
		else
			log.warning("NOT Created: " + table.getTableName() 
				+ "." + col.getColumnName());
		return success;
	}	//	createColumn
	
	
	/**
	 * 	Test
	 *	@param args ignored
	 */
	public static void main(String[] args)
	{
		Compiere.startup(true);
		Ctx ctx = Env.getCtx();
		
		checkStandardColumns(ctx, "AD_Table");
		
		/**
		SubTableUtil gd = new SubTableUtil (ctx, "AD_Element", false);
		gd.generate();
		*/
	}	//	main

}	//	SubTableUtil
