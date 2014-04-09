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
package org.compiere.print;

import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.util.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *	AD_PrintFormat - Print Format Model.
 *	(Add missing Items with PrintFormatUtil)
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MPrintFormat.java,v 1.3 2006/07/30 00:53:02 jjanke Exp $
 */
public class MPrintFormat extends X_AD_PrintFormat
{
    /** Logger for class MPrintFormat */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPrintFormat.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Public Constructor.
	 * 	Use static get methods
	 *  @param ctx context
	 *  @param AD_PrintFormat_ID AD_PrintFormat_ID
	 *	@param trx transaction
	 */
	public MPrintFormat (Ctx ctx, int AD_PrintFormat_ID, Trx trx)
	{
		super (ctx, AD_PrintFormat_ID, trx);
		//	Language=[Deutsch,Locale=de_DE,AD_Language=en_US,DatePattern=DD.MM.YYYY,DecimalPoint=false]
		m_language = Env.getLanguage( ctx );
		if (AD_PrintFormat_ID == 0)
		{
			setStandardHeaderFooter(true);
			setIsTableBased(true);
			setIsForm(false);
			setIsDefault(false);
		}
		m_items = getItems();
	}	//	MPrintFormat

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MPrintFormat (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
		m_language = Env.getLanguage( ctx );
		m_items = getItems();
	}	//	MPrintFormat

	/** Items							*/
	private MPrintFormatItem[]		m_items = null;
	/** Translation View Language		*/
	private String					m_translationViewLanguage = null;
	/**	Language of Report				*/
	private Language 				m_language;
	/** Table Format					*/
	private MPrintTableFormat 		m_tFormat;

	private static CLogger			s_log = CLogger.getCLogger (MPrintFormat.class);

	/**
	 * 	Get Language
	 *  @return language
	 */
	public Language getLanguage()
	{
		return m_language;
	}	//	getLanguage

	/**
	 * 	Set Language
	 *  @param language language
	 */
	public void setLanguage(Language language)
	{
		if (language != null)
		{
			m_language = language;
		//	log.fine("setLanguage - " + language);
		}
		m_translationViewLanguage = null;
	}	//	getLanguage

	/**
	 * 	Get AD_Column_ID of Order Columns
	 * 	@return Array of AD_Column_IDs in Sort Order
	 */
	public int[] getOrderAD_Column_IDs()
	{
		//	SortNo - AD_Column_ID
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		for (MPrintFormatItem element : m_items) {
			//	Sort Order and Column must be > 0
			if ((element.getSortNo() != 0) && (element.getAD_Column_ID() != 0))
				map.put(new Integer(element.getSortNo()), new Integer(element.getAD_Column_ID()));
		}
		//	Get SortNo and Sort them
		Integer[] keys = new Integer[map.keySet().size()];
		map.keySet().toArray(keys);
		Arrays.sort(keys);

		//	Create AD_Column_ID array
		int[] retValue = new int[keys.length];
		for (int i = 0; i < keys.length; i++)
		{
			Integer value = map.get(keys[i]);
			retValue[i] = value.intValue();
		}
		return retValue;
	}	//	getOrderAD_Column_IDs

	/**
	 * 	Get AD_Column_ID of Order Columns
	 * 	@return Array of AD_Column_IDs in Sort Order
	 */
	public ArrayList<NamePair> getOrderByColumns()
	{
		//	SortNo - AD_Column_ID
		HashMap<Integer,MPrintFormatItem> map = new HashMap<Integer,MPrintFormatItem>();
		for (MPrintFormatItem element : m_items) {
			//	Sort Order and Column must be > 0
			if ((element.getSortNo() != 0) && (element.getAD_Column_ID() != 0))
				map.put(new Integer(element.getSortNo()), element);
		}
		//	Get SortNo and Sort them
		Integer[] keys = new Integer[map.keySet().size()];
		map.keySet().toArray(keys);
		Arrays.sort(keys);

		//	Create AD_Column_ID array
		ArrayList< NamePair > list = new ArrayList< NamePair >();
		for (int i = 0; i < keys.length; i++)
		{
			MPrintFormatItem pfi = map.get(keys[i]);
			list.add( new ValueNamePair( String.valueOf(pfi.getAD_Column_ID()), pfi.isAscending()?"Y":"N" ));
		}
		
		return list;
	}	//	getOrderAD_Column_IDs

	/**
	 * 	Get AD_Column_IDs of columns in Report
	 * 	@return Array of AD_Column_ID
	 */
	public int[] getAD_Column_IDs()
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (MPrintFormatItem element : m_items) {
			if ((element.getAD_Column_ID() != 0) && element.isPrinted())
				list.add(new Integer(element.getAD_Column_ID()));
		}
		//	Convert
		int[] retValue = new int[list.size()];
		for (int i = 0; i < list.size(); i++)
			retValue[i] = list.get(i).intValue();
		return retValue;
	}	//	getAD_Column_IDs

	/**
	 * 	Set Items
	 * 	@param items items
	 */
	protected void setItems (MPrintFormatItem[] items)
	{
		if (items != null)
			m_items = items;
	}	//	setItems

	/**
	 * 	Get active Items
	 * 	@return items
	 */
	private MPrintFormatItem[] getItems()
	{
		ArrayList<MPrintFormatItem> list = new ArrayList<MPrintFormatItem>();
		String sql = "SELECT * FROM AD_PrintFormatItem pfi "
			+ "WHERE pfi.AD_PrintFormat_ID=? AND pfi.IsActive='Y'"
			//	Display restrictions - Passwords, etc.
			+ " AND NOT EXISTS (SELECT * FROM AD_Field f "
				+ "WHERE pfi.AD_Column_ID=f.AD_Column_ID"
				+ " AND (f.IsEncrypted='Y' OR f.ObscureType IS NOT NULL))"
			+ "ORDER BY SeqNo";
		MRole role = MRole.getDefault(getCtx(), false);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, get_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MPrintFormatItem pfi = new MPrintFormatItem(getCtx(), rs, get_Trx());
				if (role.isColumnAccess(getAD_Table_ID(), pfi.getAD_Column_ID(), true))
					list.add (pfi);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		//
		MPrintFormatItem[] retValue = new MPrintFormatItem[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getItems

	/**
	 * 	Get Item Count
	 * 	@return number of items or -1 if items not defined
	 */
	public int getItemCount()
	{
		if (m_items == null)
			return -1;
		return m_items.length;
	}	//	getItemCount

	/**
	 * 	Get Print Format Item
	 * 	@param index index
	 * 	@return Print Format Item
	 */
	public MPrintFormatItem getItem (int index)
	{
		if ((index < 0) || (index >= m_items.length))
			throw new ArrayIndexOutOfBoundsException("Index=" + index + " - Length=" + m_items.length);
		return m_items[index];
	}	//	getItem

	/**
	 * 	Get Print Format Item based on ID
	 * 	@param AD_PrintFormatItem_ID
	 * 	@return Print Format Item
	 */
	public MPrintFormatItem getPrintFormatItem (int AD_PrintFormatItem_ID)
	{
		MPrintFormatItem pfi = null;
		for(MPrintFormatItem item : m_items)
		{
			if (item.getAD_PrintFormatItem_ID() == AD_PrintFormatItem_ID)
				pfi = item; 
		}
		return pfi;
	}	//	getItem
	
	/**
	 * 	Set the translation of the Format Items to the original
	 */
	public void setTranslation()
	{
		StringBuffer sb = new StringBuffer ("UPDATE AD_PrintFormatItem_Trl t"
			+ " SET (PrintName, PrintNameSuffix)="
			+ " (SELECT PrintName, PrintNameSuffix FROM AD_PrintFormatItem i WHERE i.AD_PrintFormatItem_ID=t.AD_PrintFormatItem_ID) "
			+ "WHERE AD_PrintFormatItem_ID IN"
			+ " (SELECT AD_PrintFormatItem_ID FROM AD_PrintFormatItem WHERE AD_PrintFormat_ID=?)");
		int no = DB.executeUpdate(get_Trx(), sb.toString(), new Object[] {get_ID()});
		log.fine("setTranslation #" + no);
	}	//	setTranslation


	/**************************************************************************
	 * 	Set Standard Header
	 *	@param standardHeaderFooter true if std header
	 */
	public void setStandardHeaderFooter (boolean standardHeaderFooter)
	{
		super.setIsStandardHeaderFooter(standardHeaderFooter);
		if (standardHeaderFooter)
		{
			setFooterMargin(0);
			setHeaderMargin(0);
		}
	}	//	setSatndardHeaderFooter

	/**
	 * 	Set Table based.
	 * 	Reset Form
	 * 	@param tableBased true if table based
	 */
	@Override
	public void setIsTableBased (boolean tableBased)
	{
		super.setIsTableBased (tableBased);
		if (tableBased)
			super.setIsForm(false);
	}	//	setIsTableBased


	/**************************************************************************
	 * 	Set Translation View Language.
	 * 	@param language language (checked for base language)
	 */
	public void setTranslationLanguage (Language language)
	{
		if ((language == null) || language.isBaseLanguage())
		{
			log.info("Ignored - " + language);
			m_translationViewLanguage = null;
		}
		else
		{
			log.info("Language=" + language.getAD_Language());
			m_translationViewLanguage = language.getAD_Language();
			m_language = language;
		}
	}	//	setTranslationLanguage

	/**
	 *  Get Translation View use
	 *	@return true if a translation view is used
	 */
	public boolean isTranslationView()
	{
		return m_translationViewLanguage != null;
	}	//	isTranslationView

	/**
	 *	Update the Query to access the Translation View.
	 *  Can be called multiple times, adds only if not set already
	 *  @param query query to be updated
	 */
	public void setTranslationViewQuery (Query query)
	{
		//	Set Table Name and add add restriction, if a view and language set
		if ((m_translationViewLanguage != null) && (query != null) && query.getTableName().toUpperCase().endsWith("_V"))
		{
			query.setTableName(query.getTableName() + "t");
			query.addRestriction("AD_Language", Query.EQUAL, m_translationViewLanguage);
		}
	}	//	setTranslationViewQuery


	/**************************************************************************
	 * 	Get Optional TableFormat
	 * 	@param AD_PrintTableFormat_ID table format
	 */
	@Override
	public void setAD_PrintTableFormat_ID (int AD_PrintTableFormat_ID)
	{
		super.setAD_PrintTableFormat_ID(AD_PrintTableFormat_ID);
		m_tFormat = MPrintTableFormat.get (getCtx(), AD_PrintTableFormat_ID, getAD_PrintFont_ID());
	}	//	getAD_PrintTableFormat_ID

	/**
	 * 	Get Table Format
	 * 	@return Table Format
	 */
	public MPrintTableFormat getTableFormat()
	{
		if (m_tFormat == null)
			m_tFormat = MPrintTableFormat.get(getCtx(), getAD_PrintTableFormat_ID(), getAD_PrintFont_ID());
		return m_tFormat;
	}	//	getTableFormat

	/**
	 * 	Sting Representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MPrintFormat[ID=").append(get_ID())
			.append(",Name=").append(getName())
			.append(",Language=").append(getLanguage())
			.append(",Items=").append(getItemCount())
			.append("]");
		return sb.toString();
	}	//	toString


	/**************************************************************************
	 *  Load Special data (images, ..).
	 *  To be extended by sub-classes
	 *  @param rs result set
	 *  @param index zero based index
	 *  @return value value
	 *  @throws SQLException
	 */
	@Override
	protected Object loadSpecial (ResultSet rs, int index) throws SQLException
	{
		//	CreateCopy
	//	log.config(p_info.getColumnName(index));
		return null;
	}   //  loadSpecial

	/**
	 *  Save Special Data.
	 *  To be extended by sub-classes
	 *  @param value value
	 *  @param index index
	 *  @return SQL code for INSERT VALUES clause
	 */
	@Override
	protected String saveNewSpecial (Object value, int index)
	{
		//	CreateCopy
	//	String colName = p_info.getColumnName(index);
	//	String colClass = p_info.getColumnClass(index).toString();
	//	String colValue = value == null ? "null" : value.getClass().toString();
	//	log.log(Level.SEVERE, "Unknown class for column " + colName + " (" + colClass + ") - Value=" + colValue);
		if (value == null)
			return "NULL";
		return value.toString();
	}   //  saveNewSpecial

	
	
	/**************************************************************************
	 * 	Create MPrintFormat for Table
	 *  @param ctx context
	 * 	@param AD_Table_ID table
	 * 	@return print format
	 */
	static public MPrintFormat createFromTable (Ctx ctx, int AD_Table_ID)
	{
		return createFromTable(ctx, AD_Table_ID, 0);
	}	//	createFromTable

	/**************************************************************************
	 * 	Create MPrintFormat for Table
	 *  @param ctx context
	 * 	@param AD_Table_ID table
	 * 	@return print format
	 */
	static public MPrintFormat createFromTable (Ctx ctx, int AD_Table_ID, String printFormatName)
	{
		return createFromTable(ctx, AD_Table_ID, 0, printFormatName);
	}	//	createFromTable

	static public MPrintFormat createFromTable (Ctx ctx,
			int AD_Table_ID, int AD_PrintFormat_ID)
	{
		return createFromTable ( ctx, AD_Table_ID, AD_PrintFormat_ID, null);
	}
	
	/**
	 * 	Create MPrintFormat for Table
	 *  @param ctx context
	 * 	@param AD_Table_ID table
	 *  @param AD_PrintFormat_ID 0 or existing PrintFormat
	 * 	@return print format
	 */
	static public MPrintFormat createFromTable (Ctx ctx,
		int AD_Table_ID, int AD_PrintFormat_ID, String printFormatName)
	{
		int AD_Client_ID = ctx.getAD_Client_ID();
		s_log.info ("AD_Table_ID=" + AD_Table_ID + " - AD_Client_ID=" + AD_Client_ID);

		MPrintFormat pf = new MPrintFormat(ctx, AD_PrintFormat_ID, null);
		pf.setAD_Table_ID (AD_Table_ID);

		//	Get Info
		String sql = "SELECT TableName,"		//	1
			+ " (SELECT COUNT(*) FROM AD_PrintFormat x WHERE x.AD_Table_ID=t.AD_Table_ID AND x.AD_Client_ID=c.AD_Client_ID) AS Count,"
			+ " COALESCE (cpc.AD_PrintColor_ID, pc.AD_PrintColor_ID) AS AD_PrintColor_ID,"	//	3
			+ " COALESCE (cpf.AD_PrintFont_ID, pf.AD_PrintFont_ID) AS AD_PrintFont_ID,"
			+ " COALESCE (cpp.AD_PrintPaper_ID, pp.AD_PrintPaper_ID) AS AD_PrintPaper_ID "
			+ "FROM AD_Table t, AD_Client c"
			+ " LEFT OUTER JOIN AD_PrintColor cpc ON (cpc.AD_Client_ID=c.AD_Client_ID AND cpc.IsDefault='Y')"
			+ " LEFT OUTER JOIN AD_PrintFont cpf ON (cpf.AD_Client_ID=c.AD_Client_ID AND cpf.IsDefault='Y')"
			+ " LEFT OUTER JOIN AD_PrintPaper cpp ON (cpp.AD_Client_ID=c.AD_Client_ID AND cpp.IsDefault='Y'),"
			+ " AD_PrintColor pc, AD_PrintFont pf, AD_PrintPaper pp "
			+ "WHERE t.AD_Table_ID=? AND c.AD_Client_ID=?"		//	#1/2
			+ " AND pc.IsDefault='Y' AND pf.IsDefault='Y' AND pp.IsDefault='Y'";
		boolean error = true;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Table_ID);
			pstmt.setInt(2, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	Name
				String TableName = rs.getString(1);
				String ColumnName = TableName + "_ID";
				String s = ColumnName;
				if (!ColumnName.equals("T_Report_ID"))
				{
					s = Msg.translate (ctx, ColumnName);
					if (ColumnName.equals (s)) //	not found
						s = Msg.translate (ctx, TableName);
				}
				int count = rs.getInt(2);
				if (count > 0)
					s += "_" + (count+1);
				if(printFormatName == null || printFormatName.length() == 0)
					pf.setName(s);
				else
					pf.setName(printFormatName);
				//
				pf.setAD_PrintColor_ID(rs.getInt(3));
				pf.setAD_PrintFont_ID(rs.getInt(4));
				pf.setAD_PrintPaper_ID(rs.getInt(5));
				//
				error = false;
			}
			else
				s_log.log(Level.SEVERE, "No info found " + AD_Table_ID);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		if (error)
			return null;

		//	Save & complete
		if (!pf.save())
			return null;
	//	pf.dump();
		pf.setItems (createItems(ctx, pf));
		//
		return pf;
	}	//	createFromTable

	/**
	 * 	Create MPrintFormat for ReportView
	 *  @param ctx context
	 * 	@param AD_ReportView_ID ReportView
	 *  @param ReportName - optional Report Name
	 * 	@return print format
	 */
	static public MPrintFormat createFromReportView (Ctx ctx, int AD_ReportView_ID, String ReportName)
	{
		return createFromReportView(ctx, AD_ReportView_ID, ReportName, false);
	}
	
	/**
	 * 	Create MPrintFormat for ReportView
	 *  @param ctx context
	 * 	@param AD_ReportView_ID ReportView
	 *  @param ReportName - optional Report Name
	 * 	@return print format
	 */
	static public MPrintFormat createFromReportView (Ctx ctx, int AD_ReportView_ID, String ReportName, boolean appendCount)
	{
		int AD_Client_ID = ctx.getAD_Client_ID();
		s_log.info ("AD_ReportView_ID=" + AD_ReportView_ID + " - AD_Client_ID=" + AD_Client_ID + " - " + ReportName);

		MPrintFormat pf = new MPrintFormat(ctx, 0, null);
		pf.setAD_ReportView_ID (AD_ReportView_ID);

		//	Get Info
		String sql = "SELECT t.TableName,"
			+ " (SELECT COUNT(*) FROM AD_PrintFormat x WHERE x.AD_ReportView_ID=rv.AD_ReportView_ID AND x.AD_Client_ID=c.AD_Client_ID) AS Count,"
			+ " COALESCE (cpc.AD_PrintColor_ID, pc.AD_PrintColor_ID) AS AD_PrintColor_ID,"
			+ " COALESCE (cpf.AD_PrintFont_ID, pf.AD_PrintFont_ID) AS AD_PrintFont_ID,"
			+ " COALESCE (cpp.AD_PrintPaper_ID, pp.AD_PrintPaper_ID) AS AD_PrintPaper_ID,"
			+ " t.AD_Table_ID "
			+ "FROM AD_ReportView rv"
			+ " INNER JOIN AD_Table t ON (rv.AD_Table_ID=t.AD_Table_ID),"
			+ " AD_Client c"
			+ " LEFT OUTER JOIN AD_PrintColor cpc ON (cpc.AD_Client_ID=c.AD_Client_ID AND cpc.IsDefault='Y')"
			+ " LEFT OUTER JOIN AD_PrintFont cpf ON (cpf.AD_Client_ID=c.AD_Client_ID AND cpf.IsDefault='Y')"
			+ " LEFT OUTER JOIN AD_PrintPaper cpp ON (cpp.AD_Client_ID=c.AD_Client_ID AND cpp.IsDefault='Y'),"
			+ " AD_PrintColor pc, AD_PrintFont pf, AD_PrintPaper pp "
			+ "WHERE rv.AD_ReportView_ID=? AND c.AD_Client_ID=?"
			+ " AND pc.IsDefault='Y' AND pf.IsDefault='Y' AND pp.IsDefault='Y'";
		boolean error = true;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_ReportView_ID);
			pstmt.setInt(2, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	Name
				String name = ReportName;
				if ((name == null) || (name.length() == 0))
					name = rs.getString(1);		//	TableName
				
				if(appendCount)
				{
					int count = rs.getInt(2);
					if (count > 0)
						name += "_" + count;
				}
				
				pf.setName(name);
				//
				pf.setAD_PrintColor_ID(rs.getInt(3));
				pf.setAD_PrintFont_ID(rs.getInt(4));
				pf.setAD_PrintPaper_ID(rs.getInt(5));
				//
				pf.setAD_Table_ID (rs.getInt(6));
				error = false;
			}
			else
				s_log.log(Level.SEVERE, "Not found: AD_ReportView_ID=" + AD_ReportView_ID);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		if (error)
			return null;

		//	Save & complete
		if (!pf.save())
			return null;
	//	pf.dump();
		pf.setItems (createItems(ctx, pf));
		//
		return pf;
	}	//	createFromReportView


	/**
	 * 	Create Items.
	 *  Using the display order of Fields in some Tab
	 *  @param ctx context
	 *  @param format print format
	 * 	@return items
	 */
	static private MPrintFormatItem[] createItems (Ctx ctx, MPrintFormat format)
	{
		s_log.fine ("From window Tab ...");
		ArrayList<MPrintFormatItem> list = new ArrayList<MPrintFormatItem>();
		//	Get Column List from Tab
		String sql = "SELECT AD_Column_ID " //, Name, IsDisplayed, SeqNo
			+ "FROM AD_Field "
			+ "WHERE AD_Tab_ID=(SELECT MIN(AD_Tab_ID) FROM AD_Tab WHERE AD_Table_ID=?)"
			+ " AND IsEncrypted='N' AND ObscureType IS NULL "
			+ "ORDER BY COALESCE(IsDisplayed,'N') DESC, SortNo, COALESCE(MRSeqNo, SeqNo), Name";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, format.get_Trx());
			pstmt.setInt(1, format.getAD_Table_ID());
			ResultSet rs = pstmt.executeQuery();
			int seqNo = 1;
			while (rs.next())
			{
				MPrintFormatItem pfi = MPrintFormatItem.createFromColumn (format, rs.getInt(1), seqNo++);
				if (pfi != null)
				{
					list.add (pfi);
					s_log.finest("Tab: " + pfi);
				}
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, "(tab) - " + sql, e);
		}
		//	No Tab found for Table
		if (list.size() == 0)
		{
			s_log.fine("From Table ...");
			sql = "SELECT AD_Column_ID "
				+ "FROM AD_Column "
				+ "WHERE AD_Table_ID=? "
				+ "ORDER BY IsIdentifier DESC, SeqNo, Name";
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql, format.get_Trx());
				pstmt.setInt(1, format.getAD_Table_ID());
				ResultSet rs = pstmt.executeQuery();
				int seqNo = 1;
				while (rs.next())
				{
					MPrintFormatItem pfi = MPrintFormatItem.createFromColumn (format, rs.getInt(1), seqNo++);
					if (pfi != null)
					{
						list.add (pfi);
						s_log.finest("Table: " + pfi);
					}
				}
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				s_log.log(Level.SEVERE, "(table) - " + sql, e);
			}
		}

		//
		MPrintFormatItem[] retValue = new MPrintFormatItem[list.size()];
		list.toArray(retValue);
		s_log.info(format + " - #" + retValue.length);
		return retValue;
	}	//	createItems

	/**
	 * 	Copy Items
	 *  @param fromFormat from print format
	 *  @param toFormat to print format (client, id)
	 * 	@return items
	 */
	static private MPrintFormatItem[] copyItems (MPrintFormat fromFormat, MPrintFormat toFormat)
	{
		s_log.info("From=" + fromFormat);
		ArrayList<MPrintFormatItem> list = new ArrayList<MPrintFormatItem>();

		MPrintFormatItem[] items = fromFormat.getItems();
		for (MPrintFormatItem element : items) {
			MPrintFormatItem pfi = element.copyToClient (toFormat.getAD_Client_ID(), toFormat.get_ID());
			if (pfi != null)
				list.add (pfi);
		}
		//
		MPrintFormatItem[] retValue = new MPrintFormatItem[list.size()];
		list.toArray(retValue);
		copyTranslationItems (items, retValue);	//	JTP fix
		return retValue;
	}	//	copyItems

	/**
     *	Copy translation records (from - to)
     *	@param fromItems from items
     *	@param toItems to items
     */
    static private void copyTranslationItems (MPrintFormatItem[] fromItems,
    	MPrintFormatItem[] toItems)
    {
    	if ((fromItems == null) || (toItems == null))
            return;		//	should not happen

    	int counter = 0;
        for (int i = 0; i < fromItems.length; i++)
        {
            int fromID = fromItems[i].getAD_PrintFormatItem_ID();
            int toID = toItems[i].getAD_PrintFormatItem_ID();

            StringBuffer sql = new StringBuffer("UPDATE AD_PrintFormatItem_Trl newFormat ")
            	//	SET
            	.append("SET PrintName = (")
            	.append("SELECT PrintName ")
            	.append("FROM AD_PrintFormatItem_Trl oldFormat ")
            	.append("WHERE oldFormat.AD_Language=newFormat.AD_Language")
            	.append(" AND AD_PrintFormatItem_ID =?) ")
            	.append(", PrintNameSuffix = (")
            	.append("SELECT PrintNameSuffix ")
            	.append("FROM AD_PrintFormatItem_Trl oldFormat ")
            	.append("WHERE oldFormat.AD_Language=newFormat.AD_Language")
            	.append(" AND AD_PrintFormatItem_ID =?) ")
            	.append(", IsTranslated = (")
            	.append("SELECT IsTranslated ")
            	.append("FROM AD_PrintFormatItem_Trl oldFormat ")
            	.append("WHERE oldFormat.AD_Language=newFormat.AD_Language")
            	.append(" AND AD_PrintFormatItem_ID =?) ")
            	//	WHERE
            	.append("WHERE  AD_PrintFormatItem_ID=?")
            	.append(" AND EXISTS (SELECT AD_PrintFormatItem_ID ")
            		.append(" FROM AD_PrintFormatItem_trl oldFormat")
            		.append(" WHERE oldFormat.AD_Language=newFormat.AD_Language")
            		.append(" AND AD_PrintFormatItem_ID =?) ");
            int no = DB.executeUpdate((Trx) null, sql.toString(), new Object[]{fromID,fromID,fromID,toID,fromID});
            if (no == 0)	//	if first has no translation, the rest does neither
            	break;
            counter += no;
        }	//	for
        s_log.finest("#" + counter);
    }	//	copyTranslationItems


	/**************************************************************************
	 * 	Copy existing Definition To Client
	 * 	@param ctx context
	 * 	@param from_AD_PrintFormat_ID format
	 * 	@param to_AD_PrintFormat_ID format
	 * 	@return print format
	 */
	public static MPrintFormat copy (Ctx ctx,
		int from_AD_PrintFormat_ID, int to_AD_PrintFormat_ID)
	{
		return copy (ctx, from_AD_PrintFormat_ID, to_AD_PrintFormat_ID, -1, null);
	}	//	copy

	public static MPrintFormat copy (Ctx ctx,
			int from_AD_PrintFormat_ID, int to_AD_PrintFormat_ID, String printFormatName)
	{
		return copy (ctx, from_AD_PrintFormat_ID, to_AD_PrintFormat_ID, -1, printFormatName);
	}	//	copy

	/**
	 * 	Copy existing Definition To Client
	 * 	@param ctx context
	 * 	@param AD_PrintFormat_ID format
	 * 	@param to_Client_ID to client
	 * 	@return print format
	 */
	public static MPrintFormat copyToClient (Ctx ctx,
		int AD_PrintFormat_ID, int to_Client_ID)
	{
		return copy (ctx, AD_PrintFormat_ID, 0, to_Client_ID, null);
	}	//	copy

	/**
	 * 	Copy existing Definition To Client
	 * 	@param ctx context
	 * 	@param from_AD_PrintFormat_ID format
	 *  @param to_AD_PrintFormat_ID to format or 0 for new
	 * 	@param to_Client_ID to client (ignored, if to_AD_PrintFormat_ID <> 0)
	 * 	@return print format
	 */
	private static MPrintFormat copy (Ctx ctx, int from_AD_PrintFormat_ID,
		int to_AD_PrintFormat_ID, int to_Client_ID, String printFormatName)
	{
		s_log.info ("From AD_PrintFormat_ID=" + from_AD_PrintFormat_ID
			+ ", To AD_PrintFormat_ID=" + to_AD_PrintFormat_ID
			+ ", To Client_ID=" + to_Client_ID);
		if (from_AD_PrintFormat_ID == 0)
			throw new IllegalArgumentException ("From_AD_PrintFormat_ID is 0");
		//
		MPrintFormat from = new MPrintFormat(ctx, from_AD_PrintFormat_ID, null);
		MPrintFormat to = new MPrintFormat (ctx, to_AD_PrintFormat_ID, null);		//	could be 0
		PO.copyValues (from, to);
		//	New
		if (to_AD_PrintFormat_ID == 0)
		{
			if (to_Client_ID < 0)
				to_Client_ID = ctx.getAD_Client_ID();
			to.setClientOrg (to_Client_ID, 0);
			
			// SR 10023140: While copying system printformats, use organization from context
			if (from.getAD_Org_ID() == 0 && from.getAD_Org_ID() != ctx.getAD_Org_ID()){
				to.setAD_Org_ID(ctx.getAD_Org_ID());
			}
		}
		
		if(printFormatName ==null || printFormatName.length()== 0)
		{
			String pfName = from.getName();
			String version ="_" + Msg.getMsg(ctx, "Version");
			if(pfName.contains(version))
			{
				int index = pfName.lastIndexOf(version);
				pfName = pfName.substring(0,index);
			}
				
			pfName += version ;

			int count=0;
			
			String sql = "SELECT COUNT(*), MAX(pfTo.Name) " +
							"FROM AD_PrintFormat pfFrom "+
							"INNER JOIN AD_PrintFormat pfTo ON (pfFrom.AD_Table_ID=pfTo.AD_Table_ID) "+
							"WHERE pfFrom.AD_PrintFormat_ID=? " +
							"AND pfTo.Name like ? " +
							"AND pfTo.AD_Client_ID = ?";
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
				//
				pstmt.setInt(1, from_AD_PrintFormat_ID);
				pstmt.setString(2, pfName+ "%");
				pstmt.setInt(3, ctx.getAD_Client_ID());
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
					count = rs.getInt(1);
				rs.close();
				pstmt.close();

			}
			catch (SQLException e)
			{
				s_log.log(Level.SEVERE, "(table) - " + sql, e);
			}

			//	Set Name - Remove TEMPLATE - add copy
			pfName = Util.replace(pfName, "TEMPLATE", String.valueOf(to_Client_ID));
			count++;
			to.setName(pfName + count);		

		}
		else
			to.setName(printFormatName);
		if(!to.save())
			return null;

		//	Copy Items
		to.setItems(copyItems(from,to));
		return to;
	}	//	copyToClient

	/*************************************************************************/

	/**
	 * 	Get Format
	 * 	@param ctx context
	 * 	@param AD_PrintFormat_ID id
	 *  @param reload refresh from disk
	 * 	@return Format
	 */
	static public MPrintFormat get (Ctx ctx, int AD_PrintFormat_ID, boolean reload)
	{
		MPrintFormat pf = null;
		//if (!reload)
			//pf = (MPrintFormat)getCache(MPrintFormat.class, key, ctx);
		//disable cache for now until we found a way to systematically cascading the parent cache
		if (pf == null)
		{
			pf = new MPrintFormat (ctx, AD_PrintFormat_ID, null);
		}
		return pf;
	}	//	get

	/**
	 * 	Get (default) Printformat for Report View or Table
	 *	@param ctx context
	 *	@param AD_ReportView_ID id or 0
	 *	@param AD_Table_ID id or 0
	 *	@return first print format found or null
	 */
	static public MPrintFormat get (Ctx ctx, int AD_ReportView_ID, int AD_Table_ID)
	{
		MPrintFormat retValue = null;
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM AD_PrintFormat WHERE ";
		if (AD_ReportView_ID > 0)
			sql += "AD_ReportView_ID=?";
		else
			sql += "AD_Table_ID=?";
		sql += " AND AD_Client_ID IN (0,?)"
			+ " ORDER BY ASCII(IsDefault) DESC";
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_ReportView_ID > 0 ? AD_ReportView_ID : AD_Table_ID);
			pstmt.setInt(2, ctx.getAD_Client_ID());
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MPrintFormat (ctx, rs, null);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		return retValue;
	}	//	get

	
	public static ArrayList<NamePair> getPrintFormatsForProcess(Ctx ctx,int AD_Process_ID, boolean rw){
		
		ArrayList<NamePair> list = new ArrayList< NamePair >();
		
		if(AD_Process_ID == 0)
			return list;
		
		MRole role = MRole.get(ctx, ctx.getAD_Role_ID(), ctx.getAD_User_ID(), false);
		int AD_PrintFormat_ID = 0;
		
		String sql = role.addAccessSQL(
						"SELECT p.AD_ReportView_ID," 
						+" p.AD_PrintFormat_ID,p.AD_BView_ID"
						+" FROM AD_Process p "
						+" WHERE p.AD_Process_ID=?",
						"AD_Process", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO );

		String column=null;
		int value = 0;
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx)null);
			pstmt.setInt(1, AD_Process_ID);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()){
				AD_PrintFormat_ID = rs.getInt(2);
				
				if (rs.getInt(1)  > 0){
					column = "AD_ReportView_ID";
					value=rs.getInt(1);
				}
				else if (rs.getInt(2)>0){
					column="AD_PrintFormat_ID";
					value=rs.getInt(2);
				}
				else if(rs.getInt(3)>0) 
				{
					column="AD_BView_ID";
					value = rs.getInt(3);
				}
			}
			rs.close();
			pstmt.close();			
		}		
		catch (SQLException e) {
		log.log(Level.SEVERE, null, e);
		}

		if(column == null || column.length()==0)
			return list;
		
		if(column.equals("AD_ReportView_ID"))
			list = getPrintFormatsByReportView(ctx, value, rw);
		else if(column.equals("AD_BView_ID"))
			list = getPrintFormatsByBusinessView(ctx, value, rw);
		else if(column.equals("AD_PrintFormat_ID"))
		{
			int AD_Table_ID = QueryUtil.getSQLValue(null, "SELECT AD_Table_ID FROM AD_PrintFormat "
													+ "WHERE AD_PrintFormat_ID=?", value);
			if(AD_Table_ID > 0)
				list = getPrintFormatsByTable(ctx, AD_Table_ID, rw);
		}
		

		// if there is a print format on process, move it to the top of the list
		if(AD_PrintFormat_ID != 0)
		{
			for(NamePair pf: list)
			{
				if(Integer.parseInt(pf.getID()) == AD_PrintFormat_ID)
				{
					list.remove(pf);
					list.add(0,pf);
					break;
				}
			}
		}

		return list;
	}

	/**
	 * @param AD_ReportView_ID
	 * @return
	 */
	public static ArrayList< NamePair > getPrintFormatsByReportView( Ctx ctx, int AD_ReportView_ID, boolean rw )
	{
		MRole role = MRole.get(ctx, ctx.getAD_Role_ID(), ctx.getAD_User_ID(), false);
		
		ArrayList< NamePair > list = new ArrayList< NamePair >();
		String pf_sql = role.addAccessSQL("SELECT PF.AD_PrintFormat_ID, PF.Name FROM AD_PrintFormat PF " +
						"WHERE PF.AD_ReportView_ID = ? AND IsActive='Y' " +
						"ORDER BY AD_Client_ID DESC, IsDefault DESC, Name",
						"AD_PrintFormat", MRole.SQL_NOTQUALIFIED, rw);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(pf_sql, (Trx) null);
			pstmt.setInt( 1, AD_ReportView_ID );
			ResultSet rs = pstmt.executeQuery();
			while( rs.next() )
			{
				list.add( new KeyNamePair( rs.getInt( 1 ), rs.getString( 2 ) ) );
			}
			rs.close();
			pstmt.close();
		}
		catch(SQLException e)
		{
			log.log(Level.SEVERE, pf_sql, e);
		}
		return list;
	}

	/**
	 * @param AD_ReportView_ID
	 * @return
	 */
	public static ArrayList< NamePair > getPrintFormatsByTable( Ctx ctx, int AD_Table_ID, boolean rw )
	{
		MRole role = MRole.get(ctx, ctx.getAD_Role_ID(), ctx.getAD_User_ID(), false);
		
		ArrayList< NamePair > list = new ArrayList< NamePair >();
		String pf_sql = role.addAccessSQL("SELECT PF.AD_PrintFormat_ID, PF.Name " +
											"FROM AD_PrintFormat PF " +
											"WHERE PF.AD_Table_ID = ?  AND IsActive='Y'  " +
											"ORDER BY AD_Client_ID DESC, IsDefault DESC, Name",
											"AD_PrintFormat", MRole.SQL_NOTQUALIFIED, rw);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(pf_sql, (Trx) null);
			pstmt.setInt( 1, AD_Table_ID );
			ResultSet rs = pstmt.executeQuery();
			while( rs.next() )
			{
				list.add( new KeyNamePair( rs.getInt( 1 ), rs.getString( 2 ) ) );
			}
			rs.close();
			pstmt.close();
		}
		catch(SQLException e)
		{
			log.log(Level.SEVERE, pf_sql, e);
		}
		return list;
	}


	public static ArrayList< NamePair > getPrintFormatsByBusinessView( Ctx ctx, int AD_BView_ID, boolean rw )
	{
		ArrayList< NamePair > list = new ArrayList< NamePair >();
		MRole role = MRole.get(ctx, ctx.getAD_Role_ID(), ctx.getAD_User_ID(), false);
		
		String pf_sql = role.addAccessSQL("SELECT PF.AD_PrintFormat_ID, PF.Name FROM AD_PrintFormat PF " +
											"WHERE PF.AD_BView_ID = ? AND IsActive='Y' " +
											"ORDER BY AD_Client_ID DESC, IsDefault DESC, Name",
											"AD_PrintFormat", MRole.SQL_NOTQUALIFIED, rw );
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(pf_sql, (Trx) null);
			pstmt.setInt( 1, AD_BView_ID );
			ResultSet rs = pstmt.executeQuery();
			while( rs.next() )
			{
				list.add( new KeyNamePair( rs.getInt( 1 ), rs.getString( 2 ) ) );
			}
			rs.close();
			pstmt.close();
		}
		catch(SQLException e)
		{
			log.log(Level.SEVERE, pf_sql, e);
		}
		return list;
	}
	
	public static MPrintFormat createForProcess (Ctx ctx, int AD_Process_ID, String reportName)
	{
		String column=null;
		int value = 0;
		int AD_Table_ID=-1;
		
		String sql = "SELECT p.AD_ReportView_ID, " +
						"p.AD_PrintFormat_ID,p.AD_BView_ID " +
						"FROM AD_Process p WHERE p.AD_Process_ID=?";
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx)null);
			pstmt.setInt(1, AD_Process_ID);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()){
				
				if (rs.getInt(1)  > 0){
					column = "AD_ReportView_ID";
					value=rs.getInt(1);
					
				}
				else if (rs.getInt(2)>0){
					column="AD_PrintFormat_ID";
					value=rs.getInt(2);
				}
				else if(rs.getInt(3)>0) 
				{
					column="AD_BView_ID";
					value = rs.getInt(3);
				}
				
				if(value > 0 && !column.equals("AD_BView_ID"))
				{
					String table_sql = "SELECT AD_Table_ID " +
										" FROM " + column.substring(0,column.length()-3) +  
										" WHERE "+ column + " = ?";
					PreparedStatement table_pstmt = DB.prepareStatement(table_sql, (Trx)null);
					table_pstmt.setInt(1, value);
					
					ResultSet table_rs = table_pstmt.executeQuery();
					
					if (table_rs.next())
						AD_Table_ID=table_rs.getInt(1);
					
					table_rs.close();
					table_pstmt.close();
				}
			}
			rs.close();
			pstmt.close();			
		}
		
		catch (SQLException e) {
			log.log(Level.SEVERE, null, e);
		}
		
		if(column.equals("AD_ReportView_ID"))
			return createFromReportView(ctx, value, reportName);
		else if (column.equals("AD_BView_ID")){
			MPrintFormat pf =null;			
			Constructor<?> intArgsConstructor;
				try
				{
					Class<?> myClass = Class.forName("org.compiere.eul.print.MBViewPrintFormat");
					Class<?>[] intArgsClass = new Class[] {org.compiere.util.Ctx.class, int.class, int.class,String.class,org.compiere.util.Trx.class };
					Object[] intArgs = new Object[] {ctx,0,value,reportName,null };						
					
				    intArgsConstructor = myClass.getConstructor(intArgsClass);
				    pf = (MPrintFormat)  intArgsConstructor.newInstance(intArgs);
				}
				catch (Exception e)
				{
					
					log.log(Level.SEVERE, "", e);
				}
			return pf;
		}
		else 
			return createFromTable(ctx, AD_Table_ID, 0, reportName);
	}
	
	/**
	 * 	Get all Form Based Print Formats
	 * 	@param ctx context
	 * 	@param rw read write
	 * 	@return print formats
	 */
	public static StringBuffer getPrintFormatListXML( Ctx ctx, boolean rw )
	{
		StringBuffer pf_xml = new StringBuffer();
		MRole role = MRole.get(ctx, ctx.getAD_Role_ID(), ctx.getAD_User_ID(), false);
		
		String pf_sql = role.addAccessSQL("SELECT pf.AD_PrintFormat_ID, pf.Name, pf.IsForm, pf.AD_Client_ID FROM AD_PrintFormat pf " +
						"WHERE IsActive='Y' " +
						"ORDER BY Name ASC",
						"AD_PrintFormat", MRole.SQL_NOTQUALIFIED, rw);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(pf_sql, (Trx) null);
			ResultSet rs = pstmt.executeQuery();
			while( rs.next() )
			{
				pf_xml.append("<AD_PrintFormat>");

				pf_xml.append("<AD_PrintFormat_ID>");
				pf_xml.append(rs.getInt(1));
				pf_xml.append("</AD_PrintFormat_ID>");

				pf_xml.append("<Name>");
				pf_xml.append(rs.getString(2));
				pf_xml.append("</Name>");	

				boolean isForm = "Y".equals(rs.getString(3));
								
				int AD_Client_ID = rs.getInt(4);
				
				if (ctx.getAD_Client_ID() != AD_Client_ID)
					isForm = false;
				
				pf_xml.append("<IsForm>");
				pf_xml.append(isForm ? "Y":"N");
				pf_xml.append("</IsForm>");	

				pf_xml.append("</AD_PrintFormat>");
			}
			rs.close();
			pstmt.close();
		}
		catch(SQLException e)
		{
			log.log(Level.SEVERE, pf_sql, e);
		}
		return pf_xml;
	}

	private static String TAG_LANDSCAPE = "IsLandscape";
	private static String TAG_PRINTFORMATITEMS = "AD_PrintFormatItems";
	private static String TAG_TOPMARGIN = "PaperTopMargin";
	private static String TAG_LEFTMARGIN = "PaperLeftMargin";
	private static String TAG_RIGHTMARGIN = "PaperRightMargin";
	private static String TAG_BOTTOMMARGIN = "PaperBottomMargin";
	private static String TAG_PAPERWIDTH = "PaperWidth";
	private static String TAG_PAPERHEIGHT = "PaperHeight";
	private static String TAG_REFNAME = "RefName";
	private static String TAG_REFID = "AD_Reference_ID";
	private static String TAG_FIELDLENGTH = "FieldLength";
	private static String TAG_COLUMNNAME = "ColumnName";
	
	public StringBuffer get_xmlComplete (StringBuffer xml, boolean dataOnly)
	{
		if (xml == null)
			xml = new StringBuffer();
		else
			xml.append(Env.NL);
		//
		try
		{
			Document doc = get_xmlDocument(xml.length()!=0, dataOnly);
			NodeList nl = doc.getElementsByTagName("AD_PrintFormat");
			if (nl.getLength() < 1)
				return null;
			
			Element pfEl = (Element) nl.item(0);
			MPrintPaper printPaper = MPrintPaper.get(getCtx(), getAD_PrintPaper_ID());
			
			Element landscape = doc.createElement(TAG_LANDSCAPE);
			landscape.appendChild(doc.createTextNode(printPaper.isLandscape()?"Y":"N"));
			pfEl.appendChild(landscape);
			Element topMargin = doc.createElement(TAG_TOPMARGIN);
			topMargin.appendChild(doc.createTextNode(Integer.toString(printPaper.getMarginTop())));
			pfEl.appendChild(topMargin);
			Element leftMargin = doc.createElement(TAG_LEFTMARGIN);
			leftMargin.appendChild(doc.createTextNode(Integer.toString(printPaper.getMarginLeft())));
			pfEl.appendChild(leftMargin);
			Element rightMargin = doc.createElement(TAG_RIGHTMARGIN);
			rightMargin.appendChild(doc.createTextNode(Integer.toString(printPaper.getMarginRight())));
			pfEl.appendChild(rightMargin);
			Element bottomMargin = doc.createElement(TAG_BOTTOMMARGIN);
			bottomMargin.appendChild(doc.createTextNode(Integer.toString(printPaper.getMarginBottom())));
			pfEl.appendChild(bottomMargin);
			Element paperWidth = doc.createElement(TAG_PAPERWIDTH);
			paperWidth.appendChild(doc.createTextNode(Double.toString(printPaper.getCPaper().getWidth())));
			pfEl.appendChild(paperWidth);
			Element paperHeight = doc.createElement(TAG_PAPERHEIGHT);
			paperHeight.appendChild(doc.createTextNode(Double.toString(printPaper.getCPaper().getHeight())));
			pfEl.appendChild(paperHeight);

			Element pfisEl = doc.createElement(TAG_PRINTFORMATITEMS);
			pfEl.appendChild(pfisEl);
			
			for (MPrintFormatItem pfi : m_items) {
				if (!pfi.isActive())
					continue;
				Document pfiDoc = pfi.get_xmlDocument(true, dataOnly);
				Element pfiEl = pfiDoc.getDocumentElement();
				doc.adoptNode(pfiEl);
				pfisEl.appendChild(pfiEl);
				
				if (pfi.getAD_Column_ID() != 0)
				{
					MColumn col = new MColumn(getCtx(), pfi.getAD_Column_ID(), null);
					int refID = col.getAD_Reference_ID();
					Element refId = doc.createElement(TAG_REFID);
					refId.appendChild(doc.createTextNode(Integer.toString(refID)));
					pfiEl.appendChild(refId);
					Element refName = doc.createElement(TAG_REFNAME);
					refName.appendChild(doc.createTextNode(MReference.getFieldType(refID)));
					pfiEl.appendChild(refName);
					Element fieldLength = doc.createElement(TAG_FIELDLENGTH);
					fieldLength.appendChild(doc.createTextNode(Integer.toString(col.getFieldLength())));
					pfiEl.appendChild(fieldLength);
					Element columnName = doc.createElement(TAG_COLUMNNAME);
					columnName.appendChild(doc.createTextNode(col.getColumnName() + "_" + col.getName()));
					pfiEl.appendChild(columnName); 
				}
			}
			
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			DOMSource source = new DOMSource(doc);
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			transformer.transform (source, result);
			StringBuffer newXML = writer.getBuffer();
			//
			if (xml.length() != 0)
			{	//	//	<?xml version="1.0" encoding="UTF-8"?>
				int tagIndex = newXML.indexOf("?>");
				if (tagIndex != -1)
					xml.append(newXML.substring(tagIndex+2));
				else
					xml.append(newXML);
			}
			else
				xml.append(newXML);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		return xml;
	}	//	get_xmlComplete

	
	/**************************************************************************
	 * 	Test
	 * 	@param args arga
	 */
	static public void main (String[] args)
	{
		org.compiere.Compiere.startup(true);
		/**
		MPrintFormat.createFromTable(Env.getCtx(), 496);	//	Order
		MPrintFormat.createFromTable(Env.getCtx(), 497);
		MPrintFormat.createFromTable(Env.getCtx(), 516);	//	Invoice
		MPrintFormat.createFromTable(Env.getCtx(), 495);
		MPrintFormat.createFromTable(Env.getCtx(), 500);	//	Shipment
		MPrintFormat.createFromTable(Env.getCtx(), 501);

		MPrintFormat.createFromTable(Env.getCtx(), 498);	//	Check
		MPrintFormat.createFromTable(Env.getCtx(), 499);
		MPrintFormat.createFromTable(Env.getCtx(), 498);	//	Remittance
		**/
	}	//	main

}	//	MPrintFormat
