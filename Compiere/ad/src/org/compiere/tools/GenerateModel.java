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

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.*;
import org.compiere.common.constants.*;
import org.compiere.util.*;

/**
 *  Generate Model Classes extending PO.
 *  Base class for CMP interface - will be extended to create byte code directly
 *
 *  @author Jorg Janke
 *  @version $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $
 */
public class GenerateModel
{
	/**
	 * 	Generate PO Class
	 * 	@param AD_Table_ID table id
	 * 	@param directory directory with \ or / at the end.
	 * 	@param packageName package name
	 * 	@param entity type sql
	 */
	public GenerateModel (int AD_Table_ID, String directory,
			String packageName, String entityTypeSQL)
	{
		m_directory = directory;
		m_packageName = packageName;
		//	create column access methods
		StringBuffer mandatory = new StringBuffer();
		StringBuffer sb = createColumns(AD_Table_ID, mandatory, entityTypeSQL);
		// add header stuff
		String tableName = createHeader(AD_Table_ID, sb, mandatory);
		//	Save it
		writeToFile (sb, directory + tableName + ".java");
	}	//	GenerateModel

	/** File Header			*/
	public static final String COPY =
		 "/******************************************************************************\r\n"
		+" * Product: Compiere ERP & CRM Smart Business Solution                        *\r\n"
		+" * Copyright (C) 1999-2008 Compiere, Inc. All Rights Reserved.                *\r\n"
		+" * This program is free software, you can redistribute it and/or modify it    *\r\n"
		+" * under the terms version 2 of the GNU General Public License as published   *\r\n"
		+" * by the Free Software Foundation. This program is distributed in the hope   *\r\n"
		+" * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *\r\n"
		+" * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *\r\n"
		+" * See the GNU General Public License for more details.                       *\r\n"
		+" * You should have received a copy of the GNU General Public License along    *\r\n"
		+" * with this program, if not, write to the Free Software Foundation, Inc.,    *\r\n"
		+" * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *\r\n"
		+" * For the text or an alternative of this public license, you may reach us at *\r\n"
		+" * Compiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *\r\n"
		+" * or via info@compiere.org or http://www.compiere.org/license.html           *\r\n"
		+" *****************************************************************************/\r\n";

	private String m_directory = null;
	private String m_packageName = null;

	/** Created Timestamp in MS	*/
    private long m_createdMS = System.currentTimeMillis();

	/**	Logger			*/
	private static CLogger	log	= CLogger.getCLogger (GenerateModel.class);

	/**
	 * 	Add Header info to buffer
	 * 	@param AD_Table_ID table
	 * 	@param sb buffer
	 * 	@param mandatory init call for mandatory columns
	 * 	@return class name
	 */
	private String createHeader (int AD_Table_ID, StringBuffer sb,
			StringBuffer mandatory)
	{
		String tableName = "";
		int accessLevel = 0;
		long updatedMS = m_createdMS;
		String entityType = "D";
		String sql = "SELECT TableName, AccessLevel, EntityType, "
			+ "(SELECT MAX(Updated) FROM AD_Column c WHERE t.AD_Table_ID=c.AD_Table_ID), Updated "
			+ "FROM AD_Table t WHERE AD_Table_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Table_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				tableName = rs.getString(1);
				accessLevel = rs.getInt(2);
				entityType = rs.getString(3);
				Timestamp columnTS = rs.getTimestamp(4);
				updatedMS = rs.getTimestamp(5).getTime();
				if (columnTS != null && columnTS.getTime() > updatedMS)
					updatedMS = columnTS.getTime();
			}
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql + " - " + AD_Table_ID, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		if (tableName == null)
			throw new RuntimeException ("TableName not found for ID=" + AD_Table_ID);
		//
		String accessLevelInfo = accessLevel + " ";
		if (accessLevel >= 4 )
			accessLevelInfo += "- System ";
		if (accessLevel == 2 || accessLevel == 3 || accessLevel == 6 || accessLevel == 7)
			accessLevelInfo += "- Client ";
		if (accessLevel == 1 || accessLevel == 3 || accessLevel == 5 || accessLevel == 7)
			accessLevelInfo += "- Org ";

		String keyColumn = tableName + "_ID";
		String className = "X_" + tableName;
		long serialVersionUID = 26282125316789L + updatedMS;
		//
		StringBuffer start = new StringBuffer ()
			.append (COPY)
			.append ("package " + m_packageName + ";\n"
				+ "/** Generated Model - DO NOT CHANGE */\n");
	//	if (!packageName.equals("org.compiere.model"))
	//		start.append("import org.compiere.model.*;");
		start.append("import java.sql.*;"
			+ "import org.compiere.framework.*;"
			+ "import org.compiere.util.*;"
			//	Class
			+ "/** Generated Model for ").append(tableName).append("\n"
			+ " *  @author Jorg Janke (generated) \n"
			+ " *  @version ").append(Compiere.MAIN_VERSION).append(" - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $")
			//	.append(s_run)	//	Timestamp
				.append(" */\n"
			+ "public class ").append(className).append(" extends PO"
			+ "{"
			//	Standard Constructor
			+ "/** Standard Constructor\n@param ctx context\n@param "
				+ keyColumn + " id\n@param trx transaction\n*/\n"
			+ "public ").append(className).append(" (Ctx ctx, int ").append(keyColumn)
				.append(", Trx trx)"
			+ "{"
			+ "super (ctx, ").append(keyColumn).append(", trx);"
			+ "\n/* The following are the mandatory fields for this object.\n\n"
			+ "if (").append(keyColumn).append(" == 0)"
			+ "{").append(mandatory).append("}*/\n"
			+ "}"	//	Constructor End

			//	Load Constructor
			+ "/** Load Constructor \n@param ctx context\n@param rs result set \n@param trx transaction\n*/\n"
			+ "public ").append(className).append(" (Ctx ctx, ResultSet rs, Trx trx)"
			+ "{"
			+ "super (ctx, rs, trx);"
			+ "}"	//	Load Constructor End
			//
			//	Utility Constructor
		//	+ "/** Utility Constructor - DO NOT USE */\n"
		//	+ "").append(className).append(" ()"
		//	+ "{"
		//	+ "super (Env.getCtx(), 0, null);"
		//	+ "}"	//	Utility Constructor End

			+ "/** Serial Version No */\n"
			+ "private static final long serialVersionUID = " + serialVersionUID + "L;"
			+ "/** Last Updated Timestamp " + new Timestamp(updatedMS) + " */\n"
			+ "public static final long updatedMS = " + updatedMS + "L;"
			+ "/** AD_Table_ID=").append(AD_Table_ID).append(" */\n");
		if ("D".equals(entityType) || "C".equals(entityType))
			start.append("public static final int Table_ID=").append(AD_Table_ID).append(";\n");
		else
			start.append("public static final int Table_ID;\n")
				.append("static{Table_ID = get_Table_ID(\"").append(tableName).append("\");};\n");
			//
		start.append("/** TableName=").append(tableName).append(" */\n"
			+ "public static final String Table_Name=\"").append(tableName).append("\";\n"
			// + "protected static KeyNamePair Model = new KeyNamePair(Table_ID,\"").append(tableName).append("\");\n"
			//
			+ "/**\n"
			+ " *  Get AD Table ID.\n"
			+ " *  @return AD_Table_ID\n"
			+ " */\n"
		//	+ "/** Load Meta Data\n@param ctx context\n@return PO Info\n*/\n"
			+ "@Override public int get_Table_ID()"
			+ "{"
			+ "return Table_ID;"
			+ "}"		//	initPO
			);

		StringBuffer end = new StringBuffer ("}");
		//
		sb.insert(0, start);
		sb.append(end);

		return className;
	}	//	createHeader


	/**
	 * 	Create Column access methods
	 * 	@param AD_Table_ID table
	 * 	@param mandatory init call for mandatory columns
	 * 	@param entityTypeSQL entity type sql
	 * 	@return set/get method
	 */
	private StringBuffer createColumns (int AD_Table_ID, StringBuffer mandatory,
			String entityTypeSQL)
	{
		StringBuffer sb = new StringBuffer();
		String sql = "SELECT c.ColumnName, c.IsUpdateable, c.IsMandatory,"		//	1..3
			+ " c.AD_Reference_ID, c.AD_Reference_Value_ID, DefaultValue, SeqNo, "	//	4..7
			+ " c.FieldLength, c.ValueMin, c.ValueMax, c.VFormat, c.Callout, "	//	8..12
			+ " c.Name, c.Description, c.ColumnSQL, c.IsEncrypted, c.IsKey "
			+ "FROM AD_Column c "
			+ "WHERE c.AD_Table_ID=?"
			+ " AND c.IsActive='Y'"
			+ " AND c.ColumnName <> 'AD_Client_ID'"
			+ " AND c.ColumnName <> 'AD_Org_ID'"
			+ " AND c.ColumnName <> 'IsActive'"
			+ " AND c.ColumnName NOT LIKE 'Created%'"
			+ " AND c.ColumnName NOT LIKE 'Updated%' "
			+ " AND c." + entityTypeSQL
			+ "ORDER BY c.ColumnName";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			boolean ppCreated = false;
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Table_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				String columnName = rs.getString(1);
				boolean isUpdateable = "Y".equals(rs.getString(2));
				boolean isMandatory = "Y".equals(rs.getString(3));
				int displayType = rs.getInt(4);
				int AD_Reference_Value_ID = rs.getInt(5);
				String defaultValue = rs.getString(6);
				int seqNo = rs.getInt(7);
			//	int fieldLength = rs.getInt(8);
			//	String ValueMin = rs.getString(9);
			//	String ValueMax = rs.getString(10);
			//	String VFormat = rs.getString(11);
				String Callout = rs.getString(12);
				String Name = rs.getString(13);
				String Description = rs.getString(14);
				String ColumnSQL = rs.getString(15);
				boolean virtualColumn = ColumnSQL != null && ColumnSQL.length() > 0;
				boolean IsEncrypted = "Y".equals(rs.getString(16));
			//	boolean IsKey = "Y".equals(rs.getString(17));
				//
				sb.append(createColumnMethods (mandatory,
					columnName, isUpdateable, isMandatory,
					displayType, AD_Reference_Value_ID,
					defaultValue,
					Callout, Name, Description, virtualColumn, IsEncrypted));
				//
				if (seqNo == 1 && !ppCreated)
				{
					sb.append(createKeyNamePair(columnName, displayType));
					ppCreated = true;
				}
			}
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return sb;
	}	//	createColumns

	/**
	 *	Create set/get methods for column
	 * 	@param mandatory init call for mandatory columns
	 * 	@param columnName column name
	 * 	@param isUpdateable updateable
	 * 	@param isMandatory mandatory
	 * 	@param displayType display type
	 * 	@param AD_Reference_ID validation reference
	 * 	@param fieldLength int
	 *	@param defaultValue default value
	 *	@param Callout String
	 *	@param Name String
	 *	@param Description String
	 * 	@param virtualColumn virtual column
	 * 	@param IsEncrypted stored encrypted
	@return set/get method
	 */
	private String createColumnMethods (StringBuffer mandatory,
		String columnName, boolean isUpdateable, boolean isMandatory,
		int displayType, int AD_Reference_ID,
		String defaultValue,
		String Callout, String Name, String Description,
		boolean virtualColumn, boolean IsEncrypted)
	{
		//	Clazz
		Class<?> clazz = DisplayType.getClass(displayType, true);
		if (defaultValue == null)
			defaultValue = "";

		//	Handle Posted
		if (columnName.equalsIgnoreCase("Posted")
			|| columnName.equalsIgnoreCase("Processed")
			|| columnName.equalsIgnoreCase("Processing"))
		{
			clazz = Boolean.class;
			AD_Reference_ID = 0;
		}
		//	Record_ID
		else if (columnName.equalsIgnoreCase("Record_ID") || columnName.equalsIgnoreCase("AD_OrgBP_ID"))
		{
			clazz = Integer.class;
			AD_Reference_ID = 0;
		}
		//	String Key
		else if (columnName.equalsIgnoreCase("AD_Language")
			|| columnName.equalsIgnoreCase("EntityType")
			|| columnName.equalsIgnoreCase("DocBaseType"))
		{
			clazz = String.class;
		}
		//	Data Type
		String dataType = clazz.getName();
		dataType = dataType.substring(dataType.lastIndexOf('.')+1);
		if (dataType.equals("Boolean"))
			dataType = "boolean";
		else if (dataType.equals("Integer"))
			dataType = "int";
		else if (displayType == DisplayTypeConstants.Binary)
			dataType = "byte[]";

		if( dataType.equals("BigDecimal") )
			dataType = "java.math.BigDecimal";

		StringBuffer sb = new StringBuffer();
		//	****** Set Comment ******
		sb.append("/** Set ").append(Name);
		sb.append(".\n@param ").append(columnName).append(" ");
		if (Description != null && Description.length() > 0)
			sb.append(Description);
		else
			sb.append(Name);
		sb.append(" */\n");

		//	Set	********
		String setValue = "set_Value";
		if (IsEncrypted)
			setValue = "set_ValueE";
		//	public void setColumn (xxx variable)
		sb.append("public ");
		if (!isUpdateable)
			setValue = "set_ValueNoCheck";
		sb.append("void set").append(columnName).append(" (").append(dataType).append(" ").append(columnName).append(")"
			+ "{");
		//	List Validation
		if (AD_Reference_ID != 0)
		{
			String staticVar = addListValidation (sb, AD_Reference_ID,
				columnName, !isMandatory);
			sb.insert(0, staticVar);	//	first check
		}
		//	setValue ("ColumnName", xx);
		if (virtualColumn)
		{
			sb.append ("throw new IllegalArgumentException (\"").append(columnName).append(" is virtual column\");");
		}
		//	Integer
		else if (clazz.equals(Integer.class))
		{
			if (columnName.endsWith("_ID"))
			{
				if (isMandatory)	//	check mandatory ID
				{
					int firstOK = 1;	//	Valid ID 0
					if (columnName.equals("AD_Client_ID") || columnName.equals("AD_Org_ID")
						|| columnName.equals("Record_ID") || columnName.equals("C_DocType_ID")
						|| columnName.equals("Node_ID") || columnName.equals("AD_Role_ID")
						|| columnName.equals("M_AttributeSet_ID") || columnName.equals("M_AttributeSetInstance_ID"))
						firstOK = 0;
					sb.append("if (").append (columnName)
						.append (" < ").append(firstOK).append(") throw new IllegalArgumentException (\"")
						.append(columnName).append(" is mandatory.\");");
				}
				else				//	set optional _ID to null if 0
				{
					sb.append("if (").append (columnName).append (" <= 0) ")
						.append(setValue).append(" (\"").append(columnName).append("\", null);")
						.append("else\n");
				}
			}
			//	set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
			sb.append(setValue).append(" (\"").append(columnName).append("\", Integer.valueOf(").append(columnName).append("));");
		}
		//	Boolean
		else if (clazz.equals(Boolean.class))
		{
			sb.append(setValue).append(" (\"").append(columnName)
				.append("\", Boolean.valueOf(").append(columnName).append("));");
		}
		else
		{
			if (isMandatory && AD_Reference_ID == 0)	//	does not apply to int/boolean
			{
				sb.append("if (")
					.append (columnName).append (" == null)"
						+ " throw new IllegalArgumentException (\"")
				  	.append(columnName).append(" is mandatory.\");");
			}

			//	set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
			sb.append(setValue).append(" (\"").append (columnName).append ("\", ")
				.append (columnName).append (");");
		}
		//	set Method close
		sb.append("}");
		sb.append(Env.NL);

		//	Mandatory call in constructor
		if (isMandatory)
		{
			mandatory.append("set").append(columnName).append(" (");
			if (clazz.equals(Integer.class))
				mandatory.append("0");
			else if (clazz.equals(Boolean.class))
			{
				if (defaultValue.indexOf('Y') != -1)
					mandatory.append(true);
				else
					mandatory.append("false");
			}
			else if (clazz.equals(BigDecimal.class))
				mandatory.append("Env.ZERO");
			else if (clazz.equals(Timestamp.class))
				mandatory.append("new Timestamp(System.currentTimeMillis())");
			else
				mandatory.append("null");
			mandatory.append(");");
			if (defaultValue.length() > 0)
				mandatory.append("// ").append(defaultValue).append(Env.NL);
		}

		//	****** Get Comment ******
		sb.append("/** Get ").append(Name);
		if (Description != null && Description.length() > 0)
			sb.append(".\n@return ").append(Description);
		else
			sb.append(".\n@return ").append(Name);
		sb.append(" */\n");

		//	Get	********
		String getValue = "get_Value";
		sb.append("public ").append(dataType);
		if (clazz.equals(Boolean.class))
		{
			sb.append(" is");
			if (columnName.toLowerCase().startsWith("is"))
				sb.append(columnName.substring(2));
			else
				sb.append(columnName);
		}
		else
			sb.append(" get").append(columnName);
		sb.append("() {");
		if (clazz.equals(Integer.class))
			sb.append("return get_ValueAsInt(\"").append(columnName).append("\");");
		else if (clazz.equals(BigDecimal.class))
			sb.append("return get_ValueAsBigDecimal(\"").append(columnName).append("\");");
		else if (clazz.equals(Boolean.class))
			sb.append("return get_ValueAsBoolean(\"").append(columnName).append("\");");
		else if (dataType.equals("Object"))
			sb.append("return ").append(getValue)
				.append("(\"").append(columnName).append("\");");
		else
			sb.append("return (").append(dataType).append(")").append(getValue)
				.append("(\"").append(columnName).append("\");");
		sb.append("}");
		sb.append(Env.NL);
		//
		return sb.toString();
	}	//	createColumnMethods


	/**
	 * 	Add List Validation
	 * 	@param sb buffer - example:
		if (NextAction.equals("N") || NextAction.equals("F"));
		else throw new IllegalArgumentException ("NextAction Invalid value - Reference_ID=219 - N - F");
	 * 	@param AD_Reference_ID reference
	 * 	@param columnName column
	 * 	@param nullable the validation must allow null values
	 * 	@return static parameter - Example:
		public static final int NEXTACTION_AD_Reference_ID=219;
		public static final String NEXTACTION_None = "N";
		public static final String NEXTACTION_FollowUp = "F";
	 */
	private String addListValidation (StringBuffer sb, int AD_Reference_ID,
		String columnName, boolean nullable)
	{
		String enumTypeName = "X_Ref_" + columnName;

		String enumNameSql = "SELECT Name FROM AD_Reference WHERE AD_Reference_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(enumNameSql, (Trx) null);
			pstmt.setInt(1, AD_Reference_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				enumTypeName = rs.getString(1);
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}


		enumTypeName = "X_Ref_" + convertToJavaIdentifier(enumTypeName);

		StringBuffer enumClass = new StringBuffer();
		enumClass.append( "enum " ).append(enumTypeName).append(" {");

		StringBuffer isValid = new StringBuffer(
				"/** Is test a valid value.\n@param test testvalue\n@return true if valid **/\n");
		isValid.append("public static boolean is").append(columnName).append("Valid(String test){ return ").append(
				enumTypeName).append(".isValid(test); }");

		StringBuffer retValue = new StringBuffer();

		// retValue.append("\n/** ").append(columnName).append(" AD_Reference_ID=").append(AD_Reference_ID) .append(" */\n")
		//	.append("public static final int ").append(columnName.toUpperCase())
		//	.append("_AD_Reference_ID=").append(AD_Reference_ID).append(";");
		//
		boolean found = false;
		StringBuffer values = new StringBuffer("Reference_ID=")
			.append(AD_Reference_ID);
		StringBuffer statement = new StringBuffer();
		if (nullable)
		{
			statement.append("if (").append(columnName).append(" == null");
		}
		//
		boolean isRealList = false;
		String sql = "SELECT Value, Name FROM AD_Ref_List WHERE AD_Reference_ID=?";
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Reference_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				isRealList = true;
				String value = rs.getString(1);
				values.append(" - ").append(value);

				//	Name (SmallTalkNotation)
				String name = rs.getString(2);
				char[] nameArray = name.toCharArray();
				StringBuffer nameClean = new StringBuffer();
				boolean initCap = true;
				for (char c : nameArray) {
					if (Character.isJavaIdentifierPart(c))
					{
						if (initCap)
							nameClean.append(Character.toUpperCase(c));
						else
							nameClean.append(c);
						initCap = false;
					}
					else
					{
						if (c == '+')
							nameClean.append("Plus");
						else if (c == '-')
							nameClean.append("_");
						else if (c == '>')
						{
							if (name.indexOf('<') == -1)	//	ignore <xx>
								nameClean.append("Gt");
						}
						else if (c == '<')
						{
							if (name.indexOf('>') == -1)	//	ignore <xx>
								nameClean.append("Le");
						}
						else if (c == '!')
							nameClean.append("Not");
						else if (c == '=')
							nameClean.append("Eq");
						else if (c == '~')
							nameClean.append("Like");
						initCap = true;
					}

				}

				String nameConstant = convertCamelCaseToConstant( nameClean );


				if (statement.length() == 0)
				{
					statement.append("if (");
				}
				else
				{
					statement.append(" || ");
				}

				statement.append(columnName).append(".equals(\"").append(value).append("\" ");

				//
				if (!found)
				{
					found = true;
					if (!nullable)
						sb.append("if (")
							.append (columnName).append (" == null)"
							+ " throw new IllegalArgumentException (\"")
							.append(columnName).append(" is mandatory\");");
				}
				else
				{
					enumClass.append(",\n");
				}

				enumClass.append("/** ").append(name).append(" = ").append(value).append(" */\n");
				enumClass.append( nameConstant ).append("(\"").append(value).append("\")");

				retValue.append("/** ").append(name).append(" = ").append(value).append(" */\n");
				retValue.append("public static final String ").append(columnName.toUpperCase())
					.append("_").append(nameClean)
					.append(" = ").append(enumTypeName).append(".").append(nameConstant).append(".getValue();");

			}
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
			found = false;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		statement = new StringBuffer();
		statement.append("if (!").append(enumTypeName).append(".isValid(").append(columnName).append(")");
		statement.append(")\nthrow new IllegalArgumentException (\"").append(columnName)
			.append(" Invalid value - \" + ").append(columnName)
			.append(" + \" - ").append(values).append("\");");
		//
		if (found && !columnName.equals("EntityType"))
			sb.append (statement);

		enumClass.append(";");
		enumClass.append("\n");
		enumClass.append("public static final int AD_Reference_ID=").append(AD_Reference_ID).append(";");
		enumClass.append("private final String value;");
		enumClass.append("private ").append(enumTypeName).append("(String value){ this.value = value; }");
		enumClass.append("public String getValue() { return this.value; }");
		enumClass.append("public static boolean isValid(String test) { ");
		if(nullable)
		{
			enumClass.append( "if( test == null ) return true; ");
		}
		enumClass.append( "for( ").append(enumTypeName).append(" v : " ).append( enumTypeName).append(".values())");
		enumClass.append( "{ if( v.getValue().equals(test)) return true; }");
		enumClass.append( "return false;");
		enumClass.append("}");
		enumClass.append("}");

		//
		if (isRealList)
		{
			// retValue.append(isValid);

			StringBuffer enumPublicClass = new StringBuffer(COPY).append("package ").append(m_packageName).append(";\n");
			enumPublicClass.append("\n/** ").append(columnName).append(" AD_Reference_ID=").append(AD_Reference_ID) .append(" */\n");
			enumPublicClass.append("public ").append(enumClass);

			writeToFile(enumPublicClass, m_directory + enumTypeName + ".java" );

			// return new StringBuffer("private static ").append(enumClass).append(retValue).toString();
		}
		return retValue.toString();
	}	//	addListValidation


	private String convertToJavaIdentifier(String enumTypeName) {
		StringBuilder nameConstant = new StringBuilder();
		char[] buf = enumTypeName.toCharArray();
		for (int i = 0; i < buf.length; ++i) {
			char c = buf[i];

			if (c == 0 && Character.isJavaIdentifierStart(c) || c > 0 && Character.isJavaIdentifierPart(c))
				nameConstant.append(c);
			else
				nameConstant.append("_");
		}
		return nameConstant.toString();
	}


	private String convertCamelCaseToConstant(StringBuffer nameClean) {
		StringBuilder nameConstant = new StringBuilder();
		char[] buf = nameClean.toString().toCharArray();
		for (int i = 0; i < buf.length; ++i) {
			char c = buf[i];
			if (Character.isUpperCase(c)
					&& nameConstant.length() > 0
					&& (i + 1 < buf.length && !Character.isUpperCase(buf[i + 1]) || i - 1 >= 0 && !Character
							.isUpperCase(buf[i - 1]))) {
				nameConstant.append("_");
			}

			if( nameConstant.length() == 0 && !Character.isJavaIdentifierStart(c) )
				nameConstant.append("_");

			if( Character.isJavaIdentifierPart(c) )
				nameConstant.append(Character.toUpperCase(c));
			else
				nameConstant.append("_");
		}
		return nameConstant.toString();
	}


	/**
	 * Create getKeyNamePair() method with first identifier
	 *
	 * @param columnName
	 *            name
	 * @param displayType
	 *            int
	 * @return method code
	 */
	private StringBuffer createKeyNamePair (String columnName, int displayType)
	{
		String method = "get" + columnName + "()";
		if (displayType != DisplayTypeConstants.String)
			method = "String.valueOf(" + method + ")";
		StringBuffer sb = new StringBuffer("/** Get Record ID/ColumnName\n@return ID/ColumnName pair */\n"
			+ "public KeyNamePair getKeyNamePair() "
			+ "{return new KeyNamePair(get_ID(), ").append(method).append(");}");
		sb.append(Env.NL);
		return sb;
	}	//	createKeyNamePair


	/**************************************************************************
	 * 	Write to file
	 * 	@param sb string buffer
	 * 	@param fileName file name
	 */
	private void writeToFile (StringBuffer sb, String fileName)
	{
		int indent = 0;
		try
		{
			File out = new File (fileName);
			FileWriter fw = new FileWriter (out);
			for (int i = 0; i < sb.length(); i++)
			{
				char c = sb.charAt(i);

				//	after
				if (c == ';')
				{
					fw.write (c);
					if (sb.substring(i+1).startsWith("//"))
						fw.write('\t');
					else
					{
						fw.write(Env.NL);
						writeIndentToFile(fw, indent);
					}
				}
				else if (c == '}')
				{
					if (sb.substring(i+1).startsWith("//"))
						fw.write('\t');
					else
					{
						--indent;
						fw.write(Env.NL);
						writeIndentToFile(fw, indent);
						fw.write (c);
						fw.write(Env.NL);
						writeIndentToFile(fw, indent);
					}
				}
				//	before & after
				else if (c == '{')
				{
					fw.write(Env.NL);
					writeIndentToFile(fw, indent);
					fw.write (c);
					fw.write(Env.NL);

					++indent;
					writeIndentToFile(fw, indent);
				}
				else
				{
					fw.write (c);
					if (Env.NL.equals(Character.toString(c)) || c == '\n')
						writeIndentToFile(fw, indent);
				}
			}
			fw.flush ();
			fw.close ();
			float size = out.length();
			size /= 1024;
			log.info(out.getAbsolutePath() + " - " + size + " kB");
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, fileName, ex);
		}
	}	//	writeToFile

	private void writeIndentToFile (FileWriter fw, int indent) throws IOException
	{
		for (int i = 0; i < 4 * indent; ++i)
			fw.write(' ');
	}

	/**
	 * 	String representation
	 * 	@return string representation
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("GenerateModel[")
			.append("]");
		return sb.toString();
	}	//	toString



	/**************************************************************************
	 * 	Generate PO Model Class.
	 * 	<pre>
	 * 	Example: java GenerateModel.class mydirectory myPackage 'U','A'
	 * 	would generate entity type User and Application classes into mydirectory.
	 * 	Without parameters, the default is used:
	 * 	C:\Compiere\compiere-all\extend\src\compiere\model\ compiere.model 'U','A'
	 * 	</pre>
	 * 	@param args directory package entityType
	 * 	- directory where to save the generated file
	 * 	- package of the classes to be generated
	 * 	- entityType to be generated
	 */
	public static void main (String[] args)
	{
		org.compiere.Compiere.startup(true, "GenerateModel");
		CLogMgt.setLevel(Level.FINE);
	//	CLogMgt.setLevel(Level.ALL);
		log.info("Generate Model   $Revision: 8952 $");
		log.info("----------------------------------");
		//	first parameter
		String directory = "C:\\Compiere\\dev\\extend\\src\\compiere\\model\\";
		if (args.length > 0)
			directory = args[0];
		if (directory == null || directory.length() == 0)
		{
			System.err.println("No Directory");
			System.exit(1);
		}
		if (!(directory.endsWith("/") || directory.endsWith("\\")))
			directory += File.separatorChar;
		log.info("Directory: " + directory);

		//	second parameter
		String packageName = "compiere.model";
		if (args.length > 1)
			packageName = args[1];
		if (packageName == null || packageName.length() == 0)
		{
			System.err.println("No package");
			System.exit(1);
		}
		log.info("Package:   " + packageName);

		//	third parameter
		String entityType = "'U','A'";	//	User, Application
		if (args.length > 2)
			entityType = args[2];
		if (entityType == null || entityType.length() == 0)
		{
			System.err.println("No EntityType");
			System.exit(1);
		}
		String entityTypeSQL = "EntityType IN (" + entityType + ")";
		log.info(entityTypeSQL);
		log.info("----------------------------------");

		//	complete sql
		StringBuffer sql = new StringBuffer("SELECT AD_Table_ID "
			+ "FROM AD_Table "
			+ "WHERE (TableName IN ('RV_WarehousePrice','RV_BPartner')"	//	special views
				+ " OR IsView='N')"
			+ " AND Referenced_Table_ID IS NULL"
		//	+ " AND TableName NOT LIKE '%_Trl'"
			+ " AND ");
		sql.append(entityTypeSQL)
			.append(" ORDER BY TableName");

		//
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				new GenerateModel(rs.getInt(1), directory, packageName, entityTypeSQL);
				count++;
			}
		}
		catch (Exception e) {
			log.severe(e.toString());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		log.info("Generated = " + count);

	}	//	main

}	//	GenerateModel
