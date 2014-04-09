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

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.common.constants.*;
import org.compiere.db.*;
import org.compiere.model.*;
import org.compiere.tools.*;
import org.compiere.util.*;


/**
 *	Create Columns of Table or View
 *
 *  @author Jorg Janke
 *  @version $Id: TableCreateColumns.java,v 1.3 2006/07/30 00:51:01 jjanke Exp $
 */
public class TableCreateColumns extends SvrProcess
{
	/** Entity Type			*/
	private String	p_EntityType = "C";	//	ENTITYTYPE_Customization
	/** Table				*/
	private int		p_AD_Table_ID = 0;
	/** CheckAllDBTables	*/
	private boolean	p_AllTables = false;

	/** Column Count	*/
	private int 	m_count = 0;


	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("EntityType"))
				p_EntityType = (String)element.getParameter();
			else if (name.equals("AllTables"))
				p_AllTables = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_AD_Table_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	@Override
	protected String doIt () throws Exception
	{
		if (p_AD_Table_ID == 0)
			throw new CompiereSystemException("@NotFound@ @AD_Table_ID@ " + p_AD_Table_ID);
		log.info("EntityType=" + p_EntityType
			+ ", AllTables=" + p_AllTables
			+ ", AD_Table_ID=" + p_AD_Table_ID);
		//
		CompiereDatabase db = DB.getDatabase();
		Trx trx = Trx.get("getDatabaseMetaData");
		DatabaseMetaData md = trx.getConnection().getMetaData();
		String catalog = db.getCatalog();
		String schema = db.getSchema();

		if (p_AllTables)
			addTable (md, catalog, schema);
		else
		{
			MTable table = new MTable (getCtx(), p_AD_Table_ID, get_TrxName());
			if ((table == null) || (table.get_ID() == 0))
				throw new CompiereSystemException("@NotFound@ @AD_Table_ID@ " + p_AD_Table_ID);
			log.info(table.getTableName() + ", EntityType=" + p_EntityType);
			String tableName = table.getTableName();
			if (md.storesUpperCaseIdentifiers())
				tableName = tableName.toUpperCase();
			if (md.storesLowerCaseIdentifiers())
				tableName = tableName.toLowerCase();
			ResultSet rs = md.getColumns(catalog, schema, tableName, null);
			addTableColumn(getCtx(), rs, table, p_EntityType);
			SubTableUtil.checkStandardColumns(table, p_EntityType);
		}

		trx.close();
		
		return "#" + m_count;
	}	//	doIt

	/**
	 * 	Add Table
	 *	@param md meta data
	 *	@param catalog catalog
	 *	@param schema schema
	 *	@throws Exception
	 */
	private void addTable (DatabaseMetaData md, String catalog, String schema) throws Exception
	{
		ResultSet rs = md.getTables(catalog, schema, null, null);
		while (rs.next())
		{
			String tableName = rs.getString("TABLE_NAME");
			String tableType = rs.getString("TABLE_TYPE");

			//	Try to find
			MTable table = MTable.get(getCtx(), tableName);
			//	Create new ?
			if (table == null)
			{
				String tn = tableName.toUpperCase();
				if (tn.startsWith("T_SELECTION")	//	temp table
					|| tn.endsWith("_VT")			//	print trl views
					|| tn.endsWith("_V")			//	views
					|| tn.endsWith("_V1")			//	views
					|| tn.startsWith("A_A")			//	asset tables not yet
					|| tn.startsWith("A_D")			//	asset tables not yet
					|| (tn.indexOf("$") != -1		//	oracle system tables
)
					|| (tn.indexOf("EXPLAIN") != -1	//	explain plan
)
					)
				{
					log.fine("Ignored: " + tableName + " - " + tableType);
					continue;
				}
				//
				log.info(tableName + " - " + tableType);

				//	Create New
				table = new MTable(getCtx(), 0, get_TrxName());
				table.setEntityType (p_EntityType);
				table.setName (tableName);
				table.setTableName (tableName);
				table.setIsView("VIEW".equals(tableType));
				if (!table.save())
					continue;
			}
			//	Check Columns
			if (md.storesUpperCaseIdentifiers())
				tableName = tableName.toUpperCase();
			if (md.storesLowerCaseIdentifiers())
				tableName = tableName.toLowerCase();
			ResultSet rsC = md.getColumns(catalog, schema, tableName, null);
			addTableColumn(getCtx(), rsC, table, p_EntityType);
			SubTableUtil.checkStandardColumns(table, p_EntityType);
		}
	}	//	addTable


	/**
	 * 	Add Table Column
     *  @param ctx Context
	 *	@param rs result set with meta data
	 *	@param table table
     *  @param entityType entity type of the columns
	 *	@throws Exception
	 */
	protected ArrayList<String> addTableColumn (Ctx ctx, ResultSet rs, MTable table, String entityType) throws Exception
	{
		ArrayList<String> colName = new ArrayList<String>();
		String tableName = table.getTableName ();
		if (DB.isOracle ())
			tableName = tableName.toUpperCase ();
		//
		while (rs.next ())
		{
			String tn = rs.getString ("TABLE_NAME");
			if (!tableName.equalsIgnoreCase (tn))
				continue;
			String columnName = rs.getString ("COLUMN_NAME");
			colName.add(columnName);
			MColumn column = table.getColumn (columnName);
			if (column != null)
				continue;
			int dataType = rs.getInt ("DATA_TYPE");
			String typeName = rs.getString ("TYPE_NAME");
			String nullable = rs.getString ("IS_NULLABLE");
			int size = rs.getInt ("COLUMN_SIZE");
			int digits = rs.getInt ("DECIMAL_DIGITS");
			//
			log.config (columnName + " - DataType=" + dataType + " " + typeName
				+ ", Nullable=" + nullable + ", Size=" + size + ", Digits="
				+ digits);
			//
			column = new MColumn (table);
			column.setEntityType (entityType);
			//	Element
			M_Element element = M_Element.get (ctx, columnName, get_TrxName());
			if (element == null)
			{
				element = new M_Element (ctx, columnName, entityType,
					get_TrxName ());
				element.save ();
			}
			//	Column Sync
			column.setColumnName (element.getColumnName ());
			column.setName (element.getName ());
			column.setDescription (element.getDescription ());
			column.setHelp (element.getHelp ());
			column.setAD_Element_ID (element.getAD_Element_ID ());
			//	Other
			column.setIsMandatory ("NO".equals (nullable));
			column.setIsMandatoryUI (column.isMandatory());

			// Key
			if (columnName.equalsIgnoreCase (tableName + "_ID"))
			{
				column.setIsKey (true);
				column.setAD_Reference_ID (DisplayTypeConstants.ID);
				column.setIsUpdateable(false);
			}
			// Account
			else if ((columnName.toUpperCase ().indexOf ("ACCT") != -1)
				&& (size == 10))
				column.setAD_Reference_ID (DisplayTypeConstants.Account);
			// Location
			else if (columnName.equalsIgnoreCase ("C_Location_ID"))
				column.setAD_Reference_ID (DisplayTypeConstants.Location);
			// Product Attribute
			else if (columnName.equalsIgnoreCase ("M_AttributeSetInstance_ID"))
				column.setAD_Reference_ID (DisplayTypeConstants.PAttribute);
			// SalesRep_ID (=User)
			else if (columnName.equalsIgnoreCase ("SalesRep_ID"))
			{
				column.setAD_Reference_ID (DisplayTypeConstants.Table);
				column.setAD_Reference_Value_ID (190);
			}
			// ID
			else if (columnName.toUpperCase().endsWith ("_ID"))
				column.setAD_Reference_ID (DisplayTypeConstants.TableDir);
			// Date
			else if ((dataType == Types.DATE) || (dataType == Types.TIME)
				|| (dataType == Types.TIMESTAMP)
				// || columnName.toUpperCase().indexOf("DATE") != -1
				|| columnName.equalsIgnoreCase ("Created")
				|| columnName.equalsIgnoreCase ("Updated"))
			{
				column.setAD_Reference_ID (DisplayTypeConstants.DateTime);
				column.setIsUpdateable(false);
			}
			// CreatedBy/UpdatedBy (=User)
			else if (columnName.equalsIgnoreCase ("CreatedBy")
				|| columnName.equalsIgnoreCase ("UpdatedBy"))
			{
				column.setAD_Reference_ID (DisplayTypeConstants.Table);
				column.setAD_Reference_Value_ID (110);
				column.setConstraintType(X_AD_Column.CONSTRAINTTYPE_DoNOTCreate);
				column.setIsUpdateable(false);
			}
			//	Entity Type
			else if (columnName.equalsIgnoreCase ("EntityType"))
			{
				column.setAD_Reference_ID (DisplayTypeConstants.Table);
				column.setAD_Reference_Value_ID (389);
				column.setDefaultValue ("U");
				column.setConstraintType (X_AD_Column.CONSTRAINTTYPE_Restrict);
				column.setReadOnlyLogic ("@EntityType@=D");
			}
			// CLOB
			else if (dataType == Types.CLOB)
				column.setAD_Reference_ID (DisplayTypeConstants.TextLong);
			// BLOB
			else if (dataType == Types.BLOB)
				column.setAD_Reference_ID (DisplayTypeConstants.Binary);
			// Amount
			else if (columnName.toUpperCase ().indexOf ("AMT") != -1)
				column.setAD_Reference_ID (DisplayTypeConstants.Amount);
			// Qty
			else if (columnName.toUpperCase ().indexOf ("QTY") != -1)
				column.setAD_Reference_ID (DisplayTypeConstants.Quantity);
			// Boolean
			else if ((size == 1)
				&& (columnName.toUpperCase ().startsWith ("IS") || (dataType == Types.CHAR)))
				column.setAD_Reference_ID (DisplayTypeConstants.YesNo);
			// List
			else if ((size < 4) && (dataType == Types.CHAR))
				column.setAD_Reference_ID (DisplayTypeConstants.List);
			// Name, DocumentNo
			else if (columnName.equalsIgnoreCase ("Name")
				|| columnName.equalsIgnoreCase("DocumentNo"))
			{
				column.setAD_Reference_ID (DisplayTypeConstants.String);
				column.setIsIdentifier (true);
				column.setSeqNo (1);
			}
			// String, Text
			else if ((dataType == Types.CHAR) || (dataType == Types.VARCHAR)
				|| typeName.startsWith ("NVAR")
				|| typeName.startsWith ("NCHAR"))
			{
				if (typeName.startsWith("N"))	//	MultiByte
					size /= 2;
				if (size > 255)
					column.setAD_Reference_ID (DisplayTypeConstants.Text);
				else
					column.setAD_Reference_ID (DisplayTypeConstants.String);
			}
			// Number
			else if ((dataType == Types.INTEGER) || (dataType == Types.SMALLINT)
				|| (dataType == Types.DECIMAL) || (dataType == Types.NUMERIC))
			{
				if (size == 10)
					column.setAD_Reference_ID (DisplayTypeConstants.Integer);
				else
					column.setAD_Reference_ID (DisplayTypeConstants.Number);
			}
			//	??
			else
				column.setAD_Reference_ID (DisplayTypeConstants.String);

			//	General Defaults
			if (columnName.toUpperCase().endsWith("_ID"))
				column.setConstraintType (X_AD_Column.CONSTRAINTTYPE_Restrict);
			if (columnName.equalsIgnoreCase("AD_Client_ID"))
			{
				column.setAD_Val_Rule_ID (116);	//	Client Login
				column.setDefaultValue("@#AD_Client_ID@");
				column.setIsUpdateable(false);
			}
			else if (columnName.equalsIgnoreCase("AD_Org_ID"))
			{
				column.setAD_Val_Rule_ID (104);	//	Org Security
				column.setDefaultValue("@#AD_Org_ID@");
				column.setIsUpdateable(false);
			}
			else if (columnName.equalsIgnoreCase("Processed"))
			{
				column.setAD_Reference_ID(DisplayTypeConstants.YesNo);
				column.setDefaultValue("N");
				column.setIsUpdateable(false);
			}
			else if (columnName.equalsIgnoreCase("Posted"))
			{
				column.setAD_Reference_ID(DisplayTypeConstants.Button);
				column.setAD_Reference_Value_ID(234);	//	_PostedStatus
				column.setDefaultValue("N");
				column.setIsUpdateable(false);
			}

			//	General
			column.setFieldLength (size);
			if (column.isUpdateable() && table.isView())
				column.setIsUpdateable(false);

			//	Done
			if (column.save ())
			{
				addLog (0, null, null, table.getTableName() + "." + column.getColumnName());
				m_count++;
			}
		}	//	while columns
		return colName;
	}	//	addTableColumn

}	//	TableCreateColumns
