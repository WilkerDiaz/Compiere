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
import java.sql.*;
import java.util.logging.*;

import org.compiere.*;
import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Generate Value Objects for Tables
 *	
 *  @author Jorg Janke
 *  @version $Id: GenerateVO.java 8756 2010-05-12 21:21:27Z nnayak $
 */
public class GenerateVO
{
	/**
	 * 	Generate Value Object classes
	 *	@param tableName table
	 *	@param className class
	 *	@param directory directory
	 *	@param packageName package
	 */
	public GenerateVO(String tableName, String className,
		String directory, String packageName)
	{
		try
		{
			//	VO
			StringBuffer sb = createColumns (tableName);
			createVOHeader (tableName, className, packageName, sb);
			writeToFile (sb, directory + className + ".java");
			//	FT
			sb = createFT(tableName, className, packageName);
			writeToFile (sb, directory + className + "FT.java");
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}	//	GenerateVO
	
	/**	Logger	*/
    private static CLogger log = CLogger.getCLogger(GenerateVO.class);
    /** JDBC Load Method calls	*/
    private StringBuffer m_jdbcLoad = new StringBuffer();
    /** Created Timestamp in MS	*/
    private long m_createdMS = System.currentTimeMillis(); 

	
    /**************************************************************************
     * 	Create Columns
     *	@param tableName
     *	@return code
     */
    private StringBuffer createColumns(String tableName) throws Exception
    {
		String baseTable = tableName;
		if (tableName.endsWith("_v"))
			baseTable = baseTable.substring(0, baseTable.length()-2);

		Trx trx = Trx.get("getDatabaseMetaData");
		DatabaseMetaData md = trx.getConnection().getMetaData();
		String catalog = DB.getDatabase().getCatalog();
		String schema = DB.getDatabase().getSchema();
		if (md.storesUpperCaseIdentifiers())
			tableName = tableName.toUpperCase();
		if (md.storesLowerCaseIdentifiers())
			tableName = tableName.toLowerCase();
		//
		int noColumns = 0;
		StringBuffer sb = new StringBuffer();
		//
		ResultSet rs = md.getColumns(catalog, schema, tableName, null);
		while (rs.next())
		{
			noColumns++;
			String columnName = rs.getString ("COLUMN_NAME");
			int dataType = rs.getInt ("DATA_TYPE");
			createColumn (baseTable, columnName, dataType, sb);
		}
		rs.close();
		rs = null;
		trx.close();
		return sb;
    }	//	createColumns
    
    /**
     * 	Create Column info
     *	@param baseTable
     *	@param columnName
     *	@param dataType sql data type
     *	@param sb
     */
    private void createColumn (String baseTable, String columnName, int dataType,
    	StringBuffer sb)
    {
    	String name = columnName;
    	int AD_Reference_ID = 0;
    	int AD_Reference_Value_ID = 0;
    	
    	String sql = "SELECT e.ColumnName, e.Name, c.AD_Reference_ID, c.AD_Reference_Value_ID "
    		+ "FROM AD_Element e "
    		+ " INNER JOIN AD_Column c ON (e.AD_Element_ID=c.AD_Element_ID)"
    		+ "WHERE (UPPER(e.ColumnName)=?)";
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
        try
        {
	        pstmt = DB.prepareStatement(sql, (Trx) null);
	        pstmt.setString(1, columnName.toUpperCase());
	        rs = pstmt.executeQuery();
	        if (rs.next())
	        {
	        	columnName = rs.getString(1);
	        	name = rs.getString(2);
	        	AD_Reference_ID = rs.getInt(3);
	        	AD_Reference_Value_ID = rs.getInt(4);
	        }
        }
        catch (Exception e) {
	        log.log(Level.SEVERE, sql, e);
        }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
        
        //	Get from Element
        if (AD_Reference_ID == 0)
        {
        	sql = "SELECT ColumnName, Name, AD_Reference_ID, AD_Reference_Value_ID "
        		+ "FROM AD_Element "
        		+ "WHERE (UPPER(ColumnName)=?)";
            try
            {
    	        pstmt = DB.prepareStatement(sql, (Trx) null);
    	        pstmt.setString(1, columnName.toUpperCase());
    	        rs = pstmt.executeQuery();
    	        if (rs.next())
    	        {
    	        	columnName = rs.getString(1);
    	        	name = rs.getString(2);
    	        	AD_Reference_ID = rs.getInt(3);
    	        	AD_Reference_Value_ID = rs.getInt(4);
    	        }
    	        else
    	        	log.warning("Not Found: " + columnName);
            }
            catch (Exception e) {
    	        log.log(Level.SEVERE, sql, e);
            }
    		finally {
    			DB.closeResultSet(rs);
    			DB.closeStatement(pstmt);
    		}
        }
        
        if (columnName.equals(columnName.toUpperCase()))
        	log.warning("Did not Convert " + columnName);
    	
        //	Data Type
    	String dataTypeString = null;
    	String jdbcMethod = null;
    	String getMethod = "get";
    	String methodColumn = columnName;
    	String nullValue = "null";
    	String conversionGet = "";
    	String conversionSet = null;
    	
		if (AD_Reference_ID == DisplayTypeConstants.YesNo
			|| (AD_Reference_ID == DisplayTypeConstants.List 
				&& AD_Reference_Value_ID == X_Ref__YesNo.AD_Reference_ID))
		{
			dataTypeString = "boolean";
    		jdbcMethod = "\"Y\".equals(rs.getString(index))";
			getMethod = "is";
			if (columnName.startsWith("Is"))
				methodColumn = columnName.substring(2);
			nullValue = "false";
			conversionGet = "boolean retValue = \"true\".equals(value) || \"Y\".equals(value);";
			conversionSet = "PO.convertToBoolean";
		}
		else if (FieldType.isText(AD_Reference_ID) 
			|| AD_Reference_ID == DisplayTypeConstants.List
			|| (AD_Reference_ID == 0 
				&& (dataType == Types.CHAR || dataType == Types.CLOB 
					|| dataType == Types.VARCHAR || dataType == 1111)))
		{
			dataTypeString = "String";
			nullValue = "\"\"";
			conversionGet = "String retValue = value;if (value == null) retValue = \"\";";
		}
		else if (FieldType.isID(AD_Reference_ID) 
			|| AD_Reference_ID == DisplayTypeConstants.Integer
			|| dataType == Types.INTEGER 
			|| columnName.endsWith ("_ID"))
		{
			dataTypeString = "int";
    		jdbcMethod = "rs.getInt(index)";
    		nullValue = "0";
			conversionGet = "int retValue = 0;if (value != null) retValue = Integer.parseInt(value);";
			conversionSet = "PO.convertToInt";
		}
		else if (FieldType.isNumeric(AD_Reference_ID)
			|| dataType == Types.NUMERIC 
			|| dataType == Types.DECIMAL)
		{
			dataTypeString = "java.math.BigDecimal";
    		jdbcMethod = "rs.getBigDecimal(index)";
			conversionGet = "java.math.BigDecimal retValue = null;if (value == null) retValue = new java.math.BigDecimal(0);"
				+ "else\n\tretValue = new java.math.BigDecimal(value);";
			conversionSet = "PO.convertToBigDecimal";
		}
		else if (FieldType.isDate(AD_Reference_ID))
		{
			dataTypeString = "java.sql.Timestamp";
			jdbcMethod = "rs.getTimestamp(index)";
			conversionGet = "java.sql.Timestamp retValue = null; if (value != null) retValue = java.sql.Timestamp.valueOf(value);";
			conversionSet = "PO.convertToTimestamp";
		}
		else if (AD_Reference_ID == DisplayTypeConstants.Button)
		{
			log.fine("Ignored Button: " + baseTable + "." + columnName);
			return;
		}
		else if (FieldType.isLOB(AD_Reference_ID))	//	CLOB is String
		{
			log.fine("Ignored LOB: " + baseTable + "." + columnName);
			return;
		}
		else if (AD_Reference_ID == 0)
		{
			log.warning("Ignored (Type=" + dataType + "): " + baseTable + "." + columnName);
			return;
		}
		else
		{
			log.warning("Ignored (" + AD_Reference_ID + "): " + baseTable + "." + columnName 
					+ " - " + nullValue);
			return;
		}
    	if (jdbcMethod == null)
    		jdbcMethod = "rs.get" + dataTypeString + "(index)";
    	
    	/** Variable Declaration	*
    	sb.append("/** Variable: ").append(name).append(" *\/\n")
    		.append("public ").append(dataTypeString)
    			.append(" \t").append(columnName).append(" = ").append(nullValue)
    			.append(";");
    	/** Simple Method Declarations		*
    	sb.append("/** Get: ").append(name).append(" *\/\n")
			.append("public ").append(dataTypeString)
				.append(" ").append(getMethod).append(columnName).append("(){")
				.append("\treturn ").append(columnName)
			.append(";}");
    	sb.append("/** Set: ").append(name).append(" *\/\n")
    		.append("protected void set").append(columnName).append("(")
				.append(dataTypeString).append(" p_").append(columnName).append("){\t")
				.append(columnName).append(" = p_").append(columnName)
			.append(";}");

    	/** Conversion Method Declarations		*/
    	sb.append("/** Get: ").append(name).append(" */\n")
			.append("public ").append(dataTypeString)
				.append(" ").append(getMethod).append(methodColumn).append("(){")
			.append("String value = (String)get(\"").append(columnName).append("\");")
			.append(conversionGet)
			.append("\treturn retValue;}");
			
    	sb.append("/** Set: ").append(name).append(" */\n")
    		.append("protected void set").append(columnName).append("(")
				.append(dataTypeString).append(" p_").append(columnName).append("){\t")
				.append("put(\"").append(columnName).append("\", p_").append(columnName)
			.append(");}");
    	//	Conversion set
    	if (!dataTypeString.equals("String") && conversionSet != null)
    	{
        	sb.append("/** Set String: ").append(name).append(" */\n")
    		.append("protected void set").append(columnName)
				.append("(String p_").append(columnName).append("){\t");
			sb.append("set").append(columnName)
				.append("(").append(conversionSet).append("(p_").append(columnName)
				.append("));");
			sb.append("}");
    	}
    	
    	/** JDBC part			*/
    	if (m_jdbcLoad.length() == 0)
    		m_jdbcLoad.append("\n\tif (colName.equalsIgnoreCase(\"");
    	else
    		m_jdbcLoad.append("\telse if (colName.equalsIgnoreCase(\"");
		m_jdbcLoad.append(columnName).append("\"))\n\t\tvo.set")
			.append(columnName).append("(").append(jdbcMethod).append(");");

    }	//	createColumn
    
    
	/**
	 * 	Add VO Header info to buffer
	 * 	@param AD_Table_ID table
	 * 	@param sb buffer
	 * 	@param mandatory init call for mandatory columns
	 * 	@param packageName package name
	 * 	@return class name
	 */
	private String createVOHeader (String tableName, String className,
		String packageName, StringBuffer sb)
	{
		long serialVersionUID = 38026282125316789L + tableName.hashCode();
		
		StringBuffer start = new StringBuffer ()
			.append (GenerateModel.COPY)
			.append ("package " + packageName + ";\n"
				+ "/** Generated VO - DO NOT CHANGE */\n");
	//	start.append("import java.util.*;");
		start.append("import org.compiere.framework.*;\n"
			//	Class
			+ "/** Generated VO for ").append(tableName).append("\n"
			+ " *  @author Jorg Janke (generated) \n"
			+ " *  @version ").append(Compiere.MAIN_VERSION).append(" - $Id: GenerateVO.java 8756 2010-05-12 21:21:27Z nnayak $")
			//	.append(s_run)	//	Timestamp
				.append(" */\n"
			+ "public class ").append(className).append(" extends VO implements java.io.Serializable"
			+ "{"
			//	Standard Constructor
			+ "/** Standard Constructor\n*/\n"
			+ "public ").append(className).append("()"
			+ "{super();"
			+ "}"	//	Constructor End
			//	Clone Constructor
			+ "/** Clone Constructor\n*/\n"
			+ "public ").append(className).append("(VO vo)"
			+ "{super(vo);"
			+ "}"	//	Constructor End
			//
			+ "/** Serial Version No */\n"
			+ "static final long serialVersionUID = " + serialVersionUID + "L;"
			+ "/** Created Timestamp */\n"
			+ "public static final long createdMS = " + m_createdMS + "L;"
			
			//	toString
			+ "/** Info\n@return info\n*/\n"
			+ "public String toString()"
			+ "{"
			+ "StringBuffer sb = new StringBuffer (\"").append(className).append("[#\")"
			+ ".append(size())"
			+ ".append(\"]\");"
			+ "return sb.toString();"
			+ "}");

		StringBuffer end = new StringBuffer ("}");
		//
		sb.insert(0, start);
		sb.append(end);

		return className;
	}	//	createVOHeader

	
    /*************************************************************************
     * 	Create Factory Template class
     *	@param tableName
     *	@param className
     *	@param packageName
     *	@return
     */
    private StringBuffer createFT (String tableName, String className,
		String packageName)
    {
		StringBuffer sb = new StringBuffer ()
			.append (GenerateModel.COPY)
			.append ("package ").append(packageName).append(";\n")
			.append ("/** Generated Factory Template - DO NOT CHANGE */\n")
			.append ("import java.sql.*;")
			.append ("import java.util.logging.*;")
			.append ("import org.compiere.util.*;")
		//	Class
			.append ("/** Generated FT for ").append(tableName).append("\n"
				+ " *  @author Jorg Janke (generated) \n"
				+ " *  @version ").append(Compiere.MAIN_VERSION).append(" - $Id: GenerateVO.java 8756 2010-05-12 21:21:27Z nnayak $")
		//	.append(s_run)	//	Timestamp
			.append(" */\n")
			.append("public class ").append(className)
				.append("FT extends VOFactory<").append(className)
			.append(">{"
		//	Standard Constructor
		+ "/** Standard Constructor\n*/\n"
		+ "public ").append(className).append("FT ()"
		+ "{"
		+ "}"	//	Constructor End
		//
		
		+ "/** Info\n@return info\n*/\n"
		+ "public String toString()"
		+ "{"
		+ "StringBuffer sb = new StringBuffer (\"").append(className).append("FT[\")"
		+ ".append(\"]\");"
		+ "return sb.toString();"
		+ "}");

    	createFTLoad (className, sb);
    	sb.append("}");
    	return sb;
    }	//	createFT
    
    /**
     * 	Create Factory Template Load method
     *	@param sb buffer
     *	@param className class to load
     */
    private void createFTLoad (String className, StringBuffer sb)
    {
    	sb.append("/** Load from ResultSet **/\n")
    		.append("protected ").append(className).append(" load (ResultSet rs){")
    		.append(className).append(" vo = new ").append(className).append("();")
    	//
    		.append("String colName = null;")
    		.append("try{ResultSetMetaData rsmd = rs.getMetaData();")
    		.append("for (int index = 1; index <= rsmd.getColumnCount(); index++){")
    		.append("colName = rsmd.getColumnName(index);")
    		.append(m_jdbcLoad)
    		.append("}}catch (Exception e){log.log(Level.SEVERE, colName, e);}")
    		.append("return vo;}");
    }	//	createFTLoad
    
	/**************************************************************************
	 * 	Write to file
	 * 	@param sb string buffer
	 * 	@param fileName file name
	 */
	private void writeToFile (StringBuffer sb, String fileName)
	{
		try
		{
			File out = new File (fileName);
			FileWriter fw = new FileWriter (out);
			for (int i = 0; i < sb.length(); i++)
			{
				char c = sb.charAt(i);
				//	after
				if (c == ';' || c == '}')
				{
					fw.write (c);
					if (sb.substring(i+1).startsWith("//"))
						fw.write('\t');
					else
						fw.write(Env.NL);
				}
				//	before & after
				else if (c == '{')
				{
					fw.write(Env.NL);
					fw.write (c);
					fw.write(Env.NL);
				}
				else
					fw.write (c);
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

	
	/**************************************************************************
	 * 	Start
	 *	@param args
	 */
	public static void main(String[] args)
	{
		Compiere.startup(true, "GenerateVO");
		String directory = "C:\\Compiere\\core\\trunk\\ad\\src\\org\\compiere\\controller\\";
	//	String directory = "C:\\Compiere\\dev\\ad\\src\\org\\compiere\\controller\\";
		String packageName = "org.compiere.controller";
		new GenerateVO ("AD_Window_v", "UIWindowVO", directory, packageName);
		new GenerateVO ("AD_Tab_v", "UITabVO", directory, packageName);
		new GenerateVO ("AD_Field_v", "UIFieldVO", directory, packageName);
	}	//	main
	
}	//	GenerateVO
