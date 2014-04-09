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

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.compiere.*;
import org.compiere.model.*;
import org.compiere.util.*;
import org.w3c.dom.*;


/**
 *	Translation Table Import + Export
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: Translation.java,v 1.3 2006/07/30 00:51:28 jjanke Exp $
 */
public class TranslationMgr
{
	/**
	 * 	Translation
	 *	@param ctx context
	 */
	public TranslationMgr (Ctx ctx)
	{
		m_ctx = ctx;
	}	//	Translation

	/**	DTD						*/
	public static final String DTD = "<!DOCTYPE compiereTrl PUBLIC \"-//ComPiere, Inc.//DTD Compiere Translation 1.0//EN\" \"http://www.compiere.org/dtd/compiereTrl.dtd\">";
	/**	XML Element Tag			*/
	public static final String	XML_TAG = "compiereTrl";
	/**	XML Attribute Table			*/
	public static final String	XML_ATTRIBUTE_TABLE = "table";
	/** XML Attribute Language		*/
	public static final String	XML_ATTRIBUTE_LANGUAGE = "language";

	/**	XML Row Tag					*/
	public static final String	XML_ROW_TAG = "row";
	/** XML Row Attribute ID		*/
	public static final String	XML_ROW_ATTRIBUTE_ID = "id";
	/** XML Row Attribute Translated	*/
	public static final String	XML_ROW_ATTRIBUTE_TRANSLATED = "trl";

	/**	XML Value Tag				*/
	public static final String	XML_VALUE_TAG = "value";
	/** XML Value Column			*/
	public static final String	XML_VALUE_ATTRIBUTE_COLUMN = "column";
	/** XML Value Original			*/
	public static final String	XML_VALUE_ATTRIBUTE_ORIGINAL = "original";

	/**	Table is centrally maintained	*/
	private boolean			m_IsCentrallyMaintained = false;
	/**	Logger						*/
	private final CLogger			log = CLogger.getCLogger(getClass());
	/** Context						*/
	private Ctx				m_ctx = null;
	/** Number of Words				*/
	private int				m_wordCount = 0;

	private String			m_ExportScope = null;
	private int				m_AD_Client_ID = 0;
	private String 			m_AD_Language = null;


	/**
	 *	Set Export Scope
	 *	@param exportScope
	 * 	@param AD_Client_ID only certain client if id >= 0
	 */
    public void setExportScope(String exportScope, int AD_Client_ID)
    {
    	m_ExportScope = exportScope;
    	m_AD_Client_ID = AD_Client_ID;
    }	//	setExportScope

	/**
	 * 	Get Tenant
	 *	@return tenant
	 */
    public int getAD_Client_ID()
    {
    	return m_AD_Client_ID;
    }	//	getAD_Client_ID

    /**
     * 	You should use method validateLanguage()
     *	@param language language
     */
    public void setAD_Language(String language)
    {
    	m_AD_Language = language;
    }	//	setLanguage

    /**
     * 	Get Language
     *	@return language
     */
    public String getAD_Language()
    {
    	return m_AD_Language;
    }	//	getAD_Language

	/**
	 * 	Import Translation.
	 * 	Uses TranslationHandler to update translation
	 *	@param directory file directory
	 * 	@param Trl_Table table
	 * 	@return status message
	 */
	public String importTrl (String directory, String Trl_Table)
	{
		String fileName = directory + File.separator + Trl_Table + "_" + m_AD_Language + ".xml";
		log.config(fileName);
		File in = new File (fileName);
		if (!in.exists())
		{
			String msg = "File does not exist: " + fileName;
			log.log(Level.WARNING, msg);
			return msg;
		}

		int words = m_wordCount;
		try
		{
			TranslationHandler handler = new TranslationHandler(m_AD_Client_ID);
			SAXParserFactory factory = SAXParserFactory.newInstance();
		//	factory.setValidating(true);
			SAXParser parser = factory.newSAXParser();
			parser.parse(in, handler);
			//
			m_wordCount += handler.getWordCount();
			log.config("Updated=" + handler.getUpdateCount() + ", Words=" + (m_wordCount-words));
			return Msg.getMsg(m_ctx, "Updated") + "=" + handler.getUpdateCount();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "importTrl", e);
			return e.toString();
		}
	}	//	importTrl


	/**************************************************************************
	 * 	Import Translation
	 *	@param directory file directory
	 * 	@param trlTableName translation table _Trl
	 * 	@param translationLevel optional translation level TranslationImportExport.TranslationLevel_*
	 * 	@return status message
	 */
	public String exportTrl (String directory, String trlTableName,
		String translationLevel)
	{
		if (translationLevel == null)
			translationLevel = TranslationImportExport.TranslationLevel_All;

		String fileName = directory + File.separator + trlTableName + "_" + m_AD_Language + ".xml";
		log.config(fileName);
		File out = new File(fileName);

		String info = "-";
		int words = m_wordCount;
		boolean isBaseLanguage = Language.isBaseLanguage(m_AD_Language);
		String tableName = trlTableName;
		int pos = tableName.indexOf("_Trl");
		String baseTableName = trlTableName.substring(0, pos);
		if (isBaseLanguage)
			tableName =  baseTableName;
		String keyColumn = baseTableName + "_ID";
		String[] trlColumns = getTrlColumns (baseTableName);
		//
		StringBuffer sql = null;
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			//	System.out.println(factory.getClass().getName());
			DocumentBuilder builder = factory.newDocumentBuilder();
			//	<!DOCTYPE compiereTrl SYSTEM "http://www.compiere.org/dtd/compiereTrl.dtd">
			//	<!DOCTYPE compiereTrl PUBLIC "-//ComPiere, Inc.//DTD Compiere Translation 1.0//EN" "http://www.compiere.org/dtd/compiereTrl.dtd">
			Document document = builder.newDocument();
			document.appendChild(document.createComment(Compiere.getSummaryAscii()));
			document.appendChild(document.createComment(DTD));

			//	Root
			Element root = document.createElement(XML_TAG);
			root.setAttribute(XML_ATTRIBUTE_LANGUAGE, m_AD_Language);
			root.setAttribute(XML_ATTRIBUTE_TABLE, baseTableName);
			document.appendChild(root);
			//
			sql = new StringBuffer ("SELECT ");
			if (isBaseLanguage)
				sql.append("'Y',");							//	1
			else
				sql.append("t.IsTranslated,");
			sql.append("t.").append(keyColumn);				//	2
			//
			for (String element : trlColumns)
				sql.append(", t.").append(element)
					.append(",o.").append(element).append(" AS ").append(element).append("O");
			//
			sql.append(" FROM ").append(tableName).append(" t")
				.append(" INNER JOIN ").append(baseTableName)
				.append(" o ON (t.").append(keyColumn).append("=o.").append(keyColumn).append(")");
			boolean haveWhere = false;
			if (!isBaseLanguage)
			{
				sql.append(" WHERE t.AD_Language=?");
				haveWhere = true;
			}
			if (m_IsCentrallyMaintained)
			{
				sql.append (haveWhere ? " AND " : " WHERE ")
					.append ("o.IsCentrallyMaintained='N'");
				haveWhere = true;
			}
			if (m_AD_Client_ID >= 0)
			{
				sql.append(haveWhere ? " AND " : " WHERE ")
					.append("o.AD_Client_ID=").append(m_AD_Client_ID);
				haveWhere = true;
			}
			String scopeSQL = getScopeSQL(baseTableName);
			if (!Util.isEmpty(scopeSQL))
				sql.append(haveWhere ? " AND " : " WHERE ").append(scopeSQL);
			sql.append(" ORDER BY t.").append(keyColumn);
			//
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
			if (!isBaseLanguage)
				pstmt.setString(1, m_AD_Language);
			ResultSet rs = pstmt.executeQuery();
			int rows = 0;
			while (rs.next())
			{
				Element row = document.createElement (XML_ROW_TAG);
				int rowWordCount = 0;
				row.setAttribute(XML_ROW_ATTRIBUTE_ID, String.valueOf(rs.getInt(2)));	//	KeyColumn
				row.setAttribute(XML_ROW_ATTRIBUTE_TRANSLATED, rs.getString(1));		//	IsTranslated
				for (String element : trlColumns)
				{
					if (!baseTableName.equals("AD_PrintFormatItem"))
					{
						if (translationLevel.equals(TranslationImportExport.TranslationLevel_LabelOnly)
							&& !((element.indexOf("Name") != -1)
								|| element.equals("MsgText")))
							continue;
						if (translationLevel.equals(TranslationImportExport.TranslationLevel_LabelDescriptionOnly)
							&& !((element.indexOf("Name") != -1)
								|| (element.indexOf("Description") != -1)
								|| element.equals("MsgText")
								|| element.equals("MsgTip")
								|| element.equals("PrintName")
								|| element.equals("RegionName") ))
						continue;
					}
					Element value = document.createElement (XML_VALUE_TAG);
					value.setAttribute(XML_VALUE_ATTRIBUTE_COLUMN, element);
					String origString = rs.getString(element + "O");			//	Original Value
					if (origString == null)
						origString = "";
					String valueString = rs.getString(element);				//	Value
					if (valueString == null)
						valueString = "";
					value.setAttribute(XML_VALUE_ATTRIBUTE_ORIGINAL, origString);
					value.appendChild(document.createTextNode(valueString));
					row.appendChild(value);
					rowWordCount += Util.countWords(origString);
				}
				//	Add if there is something to translate
				if (rowWordCount != 0)
				{
					m_wordCount += rowWordCount;
					root.appendChild(row);
					rows++;
				}
			}
			rs.close();
			pstmt.close();
			info = trlTableName + ": Records=" + rows + ", Words=" + (m_wordCount-words);
			if (document.getDoctype() != null)
				info += ", DTD=" + document.getDoctype();
			log.config(info);
			if (rows == 0)
				return info + " (ignored)";
			//
			DOMSource source = new DOMSource(document);
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			//	Output
			out.createNewFile();
			StreamResult result = new StreamResult(out);
			//	Transform
			transformer.transform (source, result);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
			info = e.getLocalizedMessage();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			info = e.getLocalizedMessage();
		}
		return info;
	}	//	exportTrl


	/**
	 * 	Get Columns for Table
	 * 	@param Base_Table table
	 * 	@return array of translated columns
	 */
	private String[] getTrlColumns (String Base_Table)
	{
		m_IsCentrallyMaintained = false;
		String sql = "SELECT TableName FROM AD_Table t"
			+ " INNER JOIN AD_Column c ON (c.AD_Table_ID=t.AD_Table_ID AND c.ColumnName='IsCentrallyMaintained') "
			+ "WHERE t.TableName=? AND c.IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, Base_Table);
			rs = pstmt.executeQuery();
			if (rs.next())
				m_IsCentrallyMaintained = true;
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		sql = "SELECT ColumnName "
			+ "FROM AD_Column c"
			+ " INNER JOIN AD_Table t ON (c.AD_Table_ID=t.AD_Table_ID) "
			+ "WHERE t.TableName=?"
			+ " AND c.AD_Reference_ID IN (10,14) "
			+ "ORDER BY IsMandatory DESC, ColumnName";
		ArrayList<String> list = new ArrayList<String>();
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, Base_Table + "_Trl");
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				String s = rs.getString(1);
			//	System.out.println(s);
				list.add(s);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//	Convert to Array
		String[] retValue = new String[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getTrlColumns

	/**
	 * 	Reduce Scope to tenant users
	 *	@param baseTableName table name
	 *	@return
	 */
	private String getScopeSQL(String baseTableName)
	{
		if (Util.isEmpty(baseTableName)
			|| (m_ExportScope == null)
			|| !m_ExportScope.equals(TranslationImportExport.ExportScope_SystemUser))
			return null;
		//	Not translated
		if (baseTableName.equals("AD_Table"))
			return "1=2";
		//	AccessLevel 4=System only
		if (baseTableName.equals("AD_Window"))
			return "o.AD_Window_ID IN (SELECT t.AD_Window_ID FROM AD_Tab tab"
				+ " INNER JOIN AD_Table tt ON (tab.AD_Table_ID=tt.AD_Table_ID) "
				+ "WHERE tt.AccessLevel <> '4')";
		if (baseTableName.equals("AD_Tab"))
			return "EXISTS (SELECT * FROM AD_Table tt "
				+ "WHERE o.AD_Table_ID=tt.AD_Table_ID AND tt.AccessLevel <> '4')";
		if (baseTableName.equals("AD_Field"))
			return "o.AD_Tab_ID IN (SELECT AD_Tab_ID FROM AD_Tab tab"
				+ " INNER JOIN AD_Table tt ON (tab.AD_Table_ID=tt.AD_Table_ID) "
				+ "WHERE tt.AccessLevel <> '4')";
		if (baseTableName.equals("AD_Element"))
			return "o.AD_Element_ID IN (SELECT AD_Element_ID FROM AD_Column c"
				+ " INNER JOIN AD_Table tt ON (c.AD_Table_ID=tt.AD_Table_ID) "
				+ "WHERE tt.AccessLevel <> '4')";
		if (baseTableName.equals("AD_Process"))
			return "o.AccessLevel <> '4'";
		if (baseTableName.equals("AD_Process_Para"))
			return "o.AD_Process_ID IN (SELECT AD_Process_ID FROM AD_Process WHERE AccessLevel<>'4')";

		return null;
	}	//	getScopeSQL

	/**************************************************************************
	 * 	Validate Language.
	 *  - Check if AD_Language record exists
	 *  - Check Trl table records
	 * 	@param m_AD_Language language
	 * 	@return "" if validated - or error message
	 */
	public String validateLanguage (String AD_Language)
	{
		String sql = "SELECT * "
			+ "FROM AD_Language "
			+ "WHERE AD_Language=?";
		MLanguage language = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, AD_Language);
			rs = pstmt.executeQuery();
			if (rs.next())
				language = new MLanguage (m_ctx, rs, null);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return e.toString();
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//	No AD_Language Record
		if (language == null)
		{
			log.log(Level.WARNING, "Language does not exist: " + AD_Language);
			return "Language does not exist: " + AD_Language;
		}
		//	Language exists
		setAD_Language(AD_Language);
		if (language.isActive())
		{
			if (language.isBaseLanguage())
				return "";
		}
		else
		{
			log.log(Level.WARNING, "Language not active or not system language: " + AD_Language);
			return "Language not active or not system language: " + AD_Language;
		}

		//	Validate Translation
		log.info("Start Validating ... " + language);
		language.maintain(true);
		return "";
	}	//	validateLanguage

	/**
	 * 	Get Number of Words
	 *	@return number of words exported
	 */
	public int getWordCount()
	{
		return m_wordCount;
	}	//	getWordCount

	/**
	 * 	Process
	 * 	@param directory directory
	 * 	@param mode mode
	 */
	private void process (String directory, String mode)
	{
		File dir = new File(directory);
		if (!dir.exists())
			dir.mkdir();
		dir = new File(directory);
		if (!dir.exists())
		{
			System.out.println("Cannot create directory " + directory);
			System.exit(1);
		}

		String 	sql = "SELECT Name, TableName "
			+ "FROM AD_Table "
			+ "WHERE TableName LIKE '%_Trl' "
			+ "ORDER BY 1";
		ArrayList<String> trlTables = new ArrayList<String>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery();
			while (rs.next())
				trlTables.add(rs.getString(2));
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		for (int i = 0; i < trlTables.size(); i++)
		{
			String table = trlTables.get(i);
			if (mode.startsWith("i"))
				importTrl(directory, table);
			else
				exportTrl(directory, table, null);
		}
	}	//	process


	/**************************************************************************
	 * 	Batch Interface
	 * 	@param args directory AD_Language import/export
	 */
	public static void main (String[] args)
	{
		if (args.length != 3)
		{
			System.out.println("format : java Translation directory AD_Language import|export");
			System.out.println("example: java Translation /Compiere2/data/de_DE de_DE import");
			System.out.println("example: java Translation /Compiere2/data/fr_FR fr_FR export");
			System.exit(1);
		}
		//
		Login.initTest (false);
		String directory = args[0];
		String AD_Language = args[1];
		String mode = args[2];

		TranslationMgr trl = new TranslationMgr(Env.getCtx());
		String msg = trl.validateLanguage (AD_Language);
		if (msg.length() > 0)
			System.err.println(msg);
		else
			trl.process (directory, mode);

		System.exit(0);
	}	//	main

}	//	Translation
