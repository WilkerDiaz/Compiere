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

import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.util.*;

/**
 *	Persistent Column Model
 *
 *  @author Jorg Janke
 *  @version $Id: MColumn.java 9226 2010-09-21 18:33:47Z rthng $
 */
public class MColumn extends X_AD_Column
{
    /** Logger for class MColumn */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MColumn.class);

	/** */
    private static final long serialVersionUID = -8354584147105160689L;

	/**
	 * 	Get M_Column from Cache
	 *	@param ctx context
	 * 	@param AD_Column_ID id
	 *	@return M_Column
	 */
	public static MColumn get (Ctx ctx, int AD_Column_ID)
	{
		Integer key = Integer.valueOf (AD_Column_ID);
		MColumn retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MColumn (ctx, AD_Column_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get Column Name
	 *	@param ctx context
	 *	@param AD_Column_ID id
	 *	@return Column Name or null
	 */
	public static String getColumnName (Ctx ctx, int AD_Column_ID)
	{
		MColumn col = MColumn.get(ctx, AD_Column_ID);
		if (col.get_ID() == 0)
			return null;
		return col.getColumnName();
	}	//	getColumnName
	
	/**
	 * Get M Column from Column Name
	 * @param ctx - Context
	 * @param AD_Table_ID - Table Id
	 * @param columnName - Column Name
	 * @return M Column
	 */
	public static MColumn getColumn (Ctx ctx,int AD_Table_ID, String columnName){
		String sql="SELECT c.* FROM AD_Column c WHERE c.AD_Table_ID = ? AND "+DB.getSqlWhere("Upper(c.Name)", columnName.toUpperCase());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Table_ID);			
			rs = pstmt.executeQuery();
			MColumn column=null;
			if (rs.next())
				column= new MColumn(ctx, rs,null);
			return column;
			
		} 
		catch (SQLException e) {
			s_log.log(Level.SEVERE, sql, e);
		}
        finally {
        	DB.closeResultSet(rs);
        	DB.closeStatement(pstmt);
        }

		return null;		
	}

	/**
	 * 	Check if Callout exists
	 *	@param AD_Table_ID table
	 *	@param columnName column name
	 *	@param callout callout name
	 *	@return true if exists
	 */
	public static boolean checkCallout (int AD_Table_ID, String columnName, String callout)
	{
		MColumn column = null;
		String sql = "SELECT * FROM AD_Column WHERE AD_Table_ID=? AND ColumnName=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
	        pstmt = DB.prepareStatement(sql, (Trx) null);
	        pstmt.setInt(1, AD_Table_ID);
	        pstmt.setString(2, columnName);
	        rs = pstmt.executeQuery();
	        if (rs.next())
	        	column = new MColumn(Env.getCtx(), rs, null);
        }
        catch (Exception e) {
        	s_log.log(Level.SEVERE, sql, e);
        }
        finally {
        	DB.closeResultSet(rs);
        	DB.closeStatement(pstmt);
        }

        if (column == null)
        {
        	s_log.warning("Not found AD_Table_ID=" + AD_Table_ID + " Column=" + columnName);
        	return false;
        }
        String cc = column.getCallout();
        if (cc == null)
        	cc = "";
        //	Does not exists
        if (cc.indexOf(callout) == -1)
        {
        	if (callout.length() == 0)
        		cc = callout;
        	else
        		cc += ";" + callout;
        	column.setCallout(cc);
        	return column.save();
        }
        return true;
	}	//	checkCallout

	/**	Cache						*/
	private static final CCache<Integer,MColumn>	s_cache	= new CCache<Integer,MColumn>("AD_Column", 20);
	/**	Static Logger	*/
	private static final CLogger	s_log	= CLogger.getCLogger (MColumn.class);

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Column_ID
	 *	@param trx transaction
	 */
	public MColumn (Ctx ctx, int AD_Column_ID, Trx trx)
	{
		super (ctx, AD_Column_ID, trx);
		if (AD_Column_ID == 0)
		{
		//	setAD_Element_ID (0);
		//	setAD_Reference_ID (0);
		//	setColumnName (null);
		//	setName (null);
		//	setEntityType (null);	// U
			setIsAlwaysUpdateable (false);	// N
			setIsEncrypted (false);
			setIsIdentifier (false);
			setIsKey (false);
			setIsMandatory (false);
			setIsMandatoryUI (false);
			setIsParent (false);
			setIsSelectionColumn (false);
			setIsTranslated (false);
			setIsUpdateable (true);	// Y
			setVersion (Env.ZERO);
			setIsSelectionColumn (false);
			setIsSummaryColumn (false);
		}
	}	//	MColumn

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MColumn (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MColumn

	/**
	 * 	Parent Constructor
	 *	@param parent table
	 */
	public MColumn (MTable parent)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setAD_Table_ID (parent.getAD_Table_ID());
		setEntityType(parent.getEntityType());
	}	//	M_Column

	/**	Get Element			*/
	private M_Element	m_element = null;

	/**
	 * 	Is Standard Column
	 *	@return true for AD_Client_ID, etc.
	 */
	public boolean isStandardColumn()
	{
		String columnName = getColumnName();
		if (columnName.equals("AD_Client_ID")
			|| columnName.equals("AD_Org_ID")
			|| columnName.equals("IsActive")
			|| columnName.startsWith("Created")
			|| columnName.startsWith("Updated") )
			return true;

		return false;
	}	//	isStandardColumn

	/**
	 * 	Is Virtual Column
	 *	@return true if virtual column
	 */
	public boolean isVirtualColumn()
	{
		String s = getColumnSQL();
		return s != null && s.length() > 0;
	}	//	isVirtualColumn

	/**
	 * 	Is the Column Encrypted?
	 *	@return true if encrypted
	 */
	public boolean isEncrypted()
	{
		String s = getIsEncrypted();
		return "Y".equals(s);
	}	//	isEncrypted

	/**
	 * 	Set Encrypted
	 *	@param IsEncrypted encrypted
	 */
	public void setIsEncrypted (boolean IsEncrypted)
	{
		setIsEncrypted (IsEncrypted ? "Y" : "N");
	}	//	setIsEncrypted

	/**
	 * 	Get Element
	 *	@return element
	 */
	public M_Element getElement()
	{
		if (m_element == null || m_element.getAD_Element_ID() != getAD_Element_ID())
			m_element = new M_Element(getCtx(), getAD_Element_ID(), get_Trx());
		return m_element;
	}	//	getElement

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		int displayType = getAD_Reference_ID();
		//	Length
		if (FieldType.isLOB(displayType))	//	LOBs are 0
		{
			if (getFieldLength() != 0)
				setFieldLength(0);
		}
		else if (getFieldLength() == 0)
		{
			if (FieldType.isID(displayType))
				setFieldLength(10);
			else if (FieldType.isNumeric (displayType))
				setFieldLength(14);
			else if (FieldType.isDate (displayType))
				setFieldLength(7);
			else if (DisplayTypeConstants.YesNo == displayType)
				setFieldLength(1);
			else
			{
				log.saveError("FillMandatory", Msg.getElement(getCtx(), "FieldLength"));
				return false;
			}
		}

		/** Views are not updateable
		UPDATE AD_Column c
		SET IsUpdateable='N', IsAlwaysUpdateable='N'
		WHERE AD_Table_ID IN (SELECT AD_Table_ID FROM AD_Table WHERE IsView='Y')
		**/

		//	Virtual Column
		if (isVirtualColumn())
		{
			if (isMandatory())
				setIsMandatory(false);
			if (isMandatoryUI())
				setIsMandatoryUI(false);
			if (isUpdateable())
				setIsUpdateable(false);
		}
		//	Updateable/Mandatory
		if (isParent() || isKey())
		{
			setIsUpdateable(false);
			setIsMandatory(true);
		}
		if (isAlwaysUpdateable() && !isUpdateable())
			setIsAlwaysUpdateable(false);
		//	Encrypted
		if (isEncrypted())
		{
			int dt = getAD_Reference_ID();
			if (isKey() || isParent() || isStandardColumn()
				|| isVirtualColumn() || isIdentifier() || isTranslated()
				|| FieldType.isLookup(dt) || FieldType.isLOB(dt)
				|| "DocumentNo".equalsIgnoreCase(getColumnName())
				|| "Value".equalsIgnoreCase(getColumnName())
				|| "Name".equalsIgnoreCase(getColumnName()))
			{
				log.warning("Encryption not sensible - " + getColumnName());
				setIsEncrypted(false);
			}
		}

		//	Sync Terminology
		if ((newRecord || is_ValueChanged ("AD_Element_ID"))
			&& getAD_Element_ID() != 0)
		{
			m_element = new M_Element (getCtx(), getAD_Element_ID (), get_Trx());
			setColumnName (m_element.getColumnName());
			setName (m_element.getName());
			setDescription (m_element.getDescription());
			setHelp (m_element.getHelp());
		}

		if (isKey() && (newRecord || is_ValueChanged("IsKey")))
			setConstraintType(null);

		return true;
	}	//	beforeSave


	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Update Fields
		if (!newRecord)
		{
			StringBuffer sql = new StringBuffer("UPDATE AD_Field SET Name=")
				.append(DB.TO_STRING(getName()))
				.append(", Description=").append(DB.TO_STRING(getDescription()))
				.append(", Help=").append(DB.TO_STRING(getHelp()))
				.append(" WHERE AD_Column_ID=").append(get_ID())
				.append(" AND IsCentrallyMaintained='Y'");
			int no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("afterSave - Fields updated #" + no);
		}
		return success;
	}	//	afterSave

	/**
	 * 	Get SQL Add command
	 *	@param table table
	 *	@return sql
	 */
	public String getSQLAdd (MTable table)
	{
		if (isVirtualColumn())
			return null;
		StringBuffer sql = new StringBuffer ("ALTER TABLE ")
			.append(table.getTableName())
			.append(" ADD ").append(getSQLDDL());
		return sql.toString();
	}	//	getSQLAdd

	/**
	 * 	Get SQL DDL
	 *	@return columnName datataype .. or null if virtual column
	 */
	public String getSQLDDL()
	{
		if (isVirtualColumn())
			return null;
		//
		String columnName = getColumnName();
		StringBuffer sql = new StringBuffer (columnName)
			.append(" ").append(getSQLDataType());
		String defaultValue = getSQLDefaultValue();
		if (defaultValue != null && defaultValue.length() > 0)
			sql.append(" DEFAULT ").append(defaultValue);

		//	Inline Constraint only for oracle now
		if (DB.isOracle())
		{
			if (getAD_Reference_ID() == DisplayTypeConstants.YesNo)
				sql.append(" CHECK (").append(columnName).append(" IN ('Y','N'))");
		}

		//	Null
		if (isMandatory())
			sql.append(" NOT NULL");
		return sql.toString();
	}	//	getSQLDDL

	/**
	 * 	Get SQL Modify command
	 *	@param table table
     *  @param:setType generate column data type information
	 *	@param setNullOption generate null / not null statement
	 *	@return sql separated by ;
	 */
	public String getSQLModify (MTable table, boolean setType, boolean setNullOption)
	{
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlBase = new StringBuffer ("ALTER TABLE ")
			.append(table.getTableName())
			.append(" MODIFY ").append(getColumnName());

		//	Default
		StringBuffer sqlDefault = new StringBuffer(sqlBase);
		
		if (setType) sqlDefault.append(" ").append(getSQLDataType());

		sqlDefault.append(" DEFAULT ");
		String defaultValue = getSQLDefaultValue();
		if (defaultValue.length() > 0)
			sqlDefault.append(defaultValue);
		else
		{
			sqlDefault.append(" NULL ");
			defaultValue = null;
		}
		sql.append(sqlDefault);

		//	Inline Constraint only for oracle now
		if (DB.isOracle())
		{
			if (getAD_Reference_ID() == DisplayTypeConstants.YesNo){
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
					String search = "SELECT search_condition, constraint_name " +
							        "FROM user_constraints " +
							        "WHERE search_condition IS NOT NULL " +
							        "AND table_name LIKE ?";
					pstmt = DB.prepareStatement(search,get_Trx());
					String tableName = table.getTableName().toUpperCase();
					pstmt.setString(1, tableName);
					rs = pstmt.executeQuery();
					String constraint = getColumnName()+" IN ('Y','N')";
					Boolean exists = false;
					while (rs.next ()){
						String search_condition = rs.getNString(1);
						if (search_condition.equalsIgnoreCase(constraint)){
							exists = true;
							break;
						}
					}
					if (!exists)
						sql.append(" CHECK ("+constraint+")");
				}
				catch (Exception e){
					log.warning("Cannot add value constraint for column " + getColumnName());
				}
				finally {
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				}
			}
		}

		//	Constraint

		//	Null Values
		if (isMandatory() && defaultValue != null && defaultValue.length() > 0)
		{
			StringBuffer sqlSet = new StringBuffer("UPDATE ")
				.append(table.getTableName())
				.append(" SET ").append(getColumnName())
				.append("=").append(defaultValue)
				.append(" WHERE ").append(getColumnName()).append(" IS NULL");
			sql.append(DB.SQLSTATEMENT_SEPARATOR).append(sqlSet);
		}

		//	Null
		if (setNullOption)
		{
			StringBuffer sqlNull = new StringBuffer(sqlBase);
			if (isMandatory())
				sqlNull.append(" NOT NULL");
			else
				sqlNull.append(" NULL");
			sql.append(DB.SQLSTATEMENT_SEPARATOR).append(sqlNull);
		}

		//
		return sql.toString();
	}	//	getSQLModify

	/**
	 * 	Get SQL Default Value
	 *	@return default clause or ""
	 */
	public String getSQLDefaultValue()
	{
		String columnName = getColumnName();
		int dt = getAD_Reference_ID();

		//	See also GridField.getDefault
		String defaultValue = getDefaultValue();
		if (defaultValue == null)
			defaultValue = "";
		StringTokenizer st = new StringTokenizer(defaultValue, ",;", false);
		if (st.hasMoreTokens())		//	only first
			defaultValue = st.nextToken().trim();
		//
		String sql = "";
		if (columnName.equals("CreatedBy") || columnName.equals("UpdatedBy"))
			sql = "0";
		else if (columnName.equals("Created") || columnName.equals("Updated"))
		{
			if (DB.isOracle() || DB.isPostgreSQL())
				sql = "SYSDATE";
			else
				sql = "CURRENT_TIMESTAMP";

		}
		else if (defaultValue != null && defaultValue.length() > 0)
		{
			defaultValue = defaultValue.trim();
			if (defaultValue.startsWith("'") && defaultValue.endsWith("'"))
				defaultValue = defaultValue.substring(1, defaultValue.length()-1);
			if (defaultValue.equals("NULL"))
				sql = "NULL";
			else if (defaultValue.startsWith("@SQL="))
				s_log.finer(getColumnName() + " (ignored): " + defaultValue);	//	warning & ignore default
			else
			{
				int first = defaultValue.indexOf("@");
				if (first >= 0 && defaultValue.indexOf("@",first+1) >= 0)
					s_log.finer(getColumnName() + " (Unresolved Variable): " + defaultValue);
				else if (defaultValue.equals("-1"))
					s_log.finer(getColumnName() + " (invalid value): " + defaultValue);
				else
				{
					if (FieldType.isText(dt)
						|| DisplayTypeConstants.List == dt
						|| DisplayTypeConstants.YesNo == dt
						|| DisplayTypeConstants.Button == dt && !columnName.toUpperCase().endsWith("_ID")
						|| columnName.equals("EntityType")
						|| columnName.equals("AD_Language")
						|| columnName.equals("DocBaseType")
						)
						sql = DB.TO_STRING(defaultValue);
					else
						sql = defaultValue;
				}
			}
		}
		else if (columnName.equals("IsActive"))
			sql = "'Y'";
		//	NO default value - set Data Type
		else
		{
			if (dt == DisplayTypeConstants.YesNo)
				sql = "'N'";
			else if (FieldType.isNumeric (dt)
				&& (isMandatory() || isMandatoryUI()))
				sql = "0";
		}
		return sql;
	}	//	getSQLDefaultValue

	/**
	 * 	Get SQL Data Type
	 *	@return e.g. NVARCHAR2(60)
	 */
	public String getSQLDataType()
	{
		String columnName = getColumnName();
		int dt = getAD_Reference_ID();
		return DisplayType.getSQLDataType (dt, columnName, getFieldLength());
	}	//	getSQLDataType

	/**
	 * 	Get Label Name
	 *	@param AD_Language language
	 *	@return name
	 */
	public String getName (String AD_Language)
	{
		return get_Translation(getColumnName(), AD_Language);
	}	//	getName

	/**
	 * 	Column has FK (lists not included)
	 *	@return true if has FK
	 */
	public boolean isFK()
	{
		int dt = getAD_Reference_ID();
		if (FieldType.isID (dt)
			&& dt != DisplayTypeConstants.ID
			&& !isKey()
			&& !isVirtualColumn())
			return true;
		if (dt == DisplayTypeConstants.Button && getColumnName().toUpperCase().endsWith ("_ID"))
			return true;
		return false;
	}	//	isFK

	/**
	 * 	Column is a List
	 *	@return true if volumn is a list
	 */
	public boolean isList()
	{
		int dt = getAD_Reference_ID();
		return dt == DisplayTypeConstants.List;
	}	//	isList

	/**
	 * 	Get FK Name
	 *	@return foreign key column name
	 */
	public String getFKColumnName()
	{
		String keyColumnName = getColumnName();
		int displayType = getAD_Reference_ID();
		if (displayType == DisplayTypeConstants.List)
			return "Value";
		if (displayType == DisplayTypeConstants.Account)
			return "C_ValidCombination_ID";
		//
		int AD_Reference_ID = getAD_Reference_Value_ID();
		if (displayType == DisplayTypeConstants.Table
			|| AD_Reference_ID != 0 && displayType == DisplayTypeConstants.Search)
		{
			MRefTable rt = MRefTable.get (getCtx(), AD_Reference_ID);
			return rt.getKeyColumnName();
		}
		return keyColumnName;
	}	//	getFKColumnName

	/**
	 * 	Get FK Table - also for lists
	 *	@return FK Table
	 */
	public MTable getFKTable()
	{
		if (!isFK())
			return null;

		int displayType = getAD_Reference_ID();
		//	Lists
		if (displayType == DisplayTypeConstants.List)
			return MTable.get(getCtx(), X_AD_Ref_List.Table_ID);
		//	Account
		if (displayType == DisplayTypeConstants.Account)
			return MTable.get(getCtx(), X_C_ValidCombination.Table_ID);	//	C_ValidCombination

		//	Table, TableDir, ...
		String FKTableName = getFKColumnName();
		if (FKTableName.toUpperCase().endsWith("_ID"))
			FKTableName = FKTableName.substring(0, FKTableName.length()-3);

		return MTable.get (getCtx(), FKTableName);
	}	//	getFKTable

	/**
	 * 	Selection Column
	 *	@return true if Selection Column
	 */
	@Override
	public boolean isSelectionColumn()
	{
		if (super.isSelectionColumn())
			return true;
		String cn = getColumnName();
		return "Value".equals (cn)
			|| "Name".equals(cn)
			|| "Description".equals(cn)
			|| "DocumentNo".equals(cn);
	}	//	isSelectionCilumn


	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MColumn[");
		sb.append (get_ID()).append ("-").append (getColumnName()).append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Test
	 *	@param args ignored
	 *
	public static void main(String[] args)
    {
	    Compiere.startup(true);
	//	checkCallout(X_Test.Table_ID, "T_Integer", "compiere.model.CalloutUser.justAnExample");
	    s_log.info("" + checkCallout(X_Test.Table_ID, "T_Integer", "xx"));

    }	//	main
	/** */
}	//	MColumn
