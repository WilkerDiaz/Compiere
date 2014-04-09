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

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.compiere.util.*;
import org.w3c.dom.*;

/**
 *	Window Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MWindow.java 8756 2010-05-12 21:21:27Z nnayak $
 */
public class MWindow extends X_AD_Window
{
    /** Logger for class MWindow */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MWindow.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get MWindow from Cache
	 *	@param ctx context
	 *	@param AD_Window_ID id
	 *	@return MWindow
	 */
	public static MWindow get(Ctx ctx, int AD_Window_ID)
	{
		Integer key = Integer.valueOf (AD_Window_ID);
		MWindow retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MWindow (ctx, AD_Window_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	
	/**
	 * 	Get workflow nodes with where clause.
	 * 	Is here as MWFNode is in base
	 *	@param ctx context
	 *	@param whereClause where clause w/o the actual WHERE
	 *	@return nodes
	 */
	public static X_AD_WF_Node[] getWFNodes (Ctx ctx, String whereClause)
	{
		String sql = "SELECT * FROM AD_WF_Node";
		if (whereClause != null && whereClause.length() > 0)
			sql += " WHERE " + whereClause;
		ArrayList<X_AD_WF_Node> list = new ArrayList<X_AD_WF_Node>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new X_AD_WF_Node (ctx, rs, null));
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		X_AD_WF_Node[] retValue = new X_AD_WF_Node[list.size()];
		list.toArray (retValue);
		return retValue;
	}	//	getWFNode
	
	
	/**	Cache						*/
	private static final CCache<Integer, MWindow> s_cache 
		= new CCache<Integer, MWindow> ("AD_Window_ID", 20);
	/**	Static Logger	*/
	private static final CLogger	s_log	= CLogger.getCLogger (MWindow.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Window_ID
	 *	@param trx transaction
	 */
	public MWindow (Ctx ctx, int AD_Window_ID, Trx trx)
	{
		super (ctx, AD_Window_ID, trx);
		if (AD_Window_ID == 0)
		{
			setWindowType (WINDOWTYPE_Maintain);	// M
			setEntityType (ENTITYTYPE_UserMaintained);	// U
			setIsBetaFunctionality (false);
			setIsDefault (false);
			setIsCustomDefault(false);
		}	}	//	M_Window

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MWindow (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	M_Window
	
	/**
	 * 	Set Window Size
	 *	@param size size
	 */
	public void setWindowSize (Dimension size)
	{
		if (size != null)
		{
			setWinWidth(size.width);
			setWinHeight(size.height);
		}
		else
		{
			setWinWidth(0);
			setWinHeight(0);
		}
	}	//	setWindowSize
	
	/**	The Lines						*/
	private MTab[]		m_tabs	= null;

	/**
	 * 	Get Tabs
	 *	@param reload reload data
	 *	@return array of lines
	 *	@param trx transaction
	 */
	public MTab[] getTabs (boolean reload, Trx trx)
	{
		if (m_tabs != null && !reload)
			return m_tabs;
		String sql = "SELECT * FROM AD_Tab tab WHERE AD_Window_ID=? " +
				"AND NOT EXISTS (SELECT 1 FROM AD_Table table1 WHERE table1.AD_Table_ID=tab.AD_Table_ID AND IsView='Y') " +
				"ORDER BY SeqNo";
		ArrayList<MTab> list = new ArrayList<MTab>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, getAD_Window_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MTab (getCtx(), rs, trx));
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		m_tabs = new MTab[list.size ()];
		list.toArray (m_tabs);
		return m_tabs;
	}	//	getFields

	/**
	 * 	Get Tab with ID
	 * 	@param AD_Tab_ID id
	 *	@return tab or null
	 */
	public MTab getTab(int AD_Tab_ID)
	{
		MTab[] tabs = getTabs(false, get_Trx());
		for (MTab element : tabs) {
			if (element.getAD_Tab_ID() == AD_Tab_ID)
				return element;
        }
		return null;
	}	//	getTab
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord)	//	Add to all automatic roles
		{
			MRole[] roles = MRole.getOf(getCtx(), "IsManual='N'");
			for (MRole element : roles) {
				MWindowAccess wa = new MWindowAccess(this, element.getAD_Role_ID());
				wa.save();
			}
		}
		//	Menu/Workflow
		else if (is_ValueChanged("IsActive") || is_ValueChanged("Name") 
			|| is_ValueChanged("Description") || is_ValueChanged("Help"))
		{
			MMenu[] menues = MMenu.get(getCtx(), "AD_Window_ID=" + getAD_Window_ID());
			for (MMenu element : menues) {
				element.setName(getName());
				element.setDescription(getDescription());
				element.setIsActive(isActive());
				element.save();
			}
			//
			X_AD_WF_Node[] nodes = getWFNodes(getCtx(), "AD_Window_ID=" + getAD_Window_ID());
			for (X_AD_WF_Node element : nodes) {
				boolean changed = false;
				if (element.isActive() != isActive())
				{
					element.setIsActive(isActive());
					changed = true;
				}
				if (element.isCentrallyMaintained())
				{
					element.setName(getName());
					element.setDescription(getDescription());
					element.setHelp(getHelp());
					changed = true;
				}
				if (changed)
					element.save();
			}
		}
		return success;
	}	//	afterSave
	
	/**
	 * 	String Representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MWindow[")
			.append(get_ID()).append("-").append(getName()).append("]");
		return sb.toString();
	}	//	toString
	
	private static String TAG_TABS = "AD_Tabs";
	private static String TAG_FIELDS = "AD_Fields";
	private static String TAG_FIELDGROUPS = "AD_FieldGroups";
	private static String TAG_DISPLAYTYPE = "DisplayType";
	private static String TAG_COL_MANDATORY = "IsColumnMandatory";
	private static String TAG_COL_MANDATORY_UI = "IsColumnMandatoryUI";
	private static String TAG_TABLES = "AD_Tables";
	private static String TAG_COLUMNS = "AD_Columns";

	public StringBuffer get_xmlAllTablesByWindow(StringBuffer xml, boolean dataOnly) {

		if(xml == null)
			xml = new StringBuffer();
		else
			xml.append(Env.NL);
		
		try {
			Document doc = get_xmlDocument(xml.length()!=0, dataOnly);
			NodeList nl = doc.getElementsByTagName("AD_Window");
			if(nl.getLength() < 1)
				return null;
			
			Element winEl = (Element)nl.item(0);
			Element tablesEl = doc.createElement(TAG_TABLES);
			winEl.appendChild(tablesEl);
			
			MTab[] tabs = getTabs(true, null);
			StringBuffer where = new StringBuffer(" AD_Table_ID IN (");
			
			for(int i=0; i<tabs.length; i++)
				where.append(tabs[i].getAD_Table_ID()+",");
			where.setCharAt(where.length()-1, ')');
			ArrayList<MTable> tables = MTable.getTables(getCtx(), where.toString(), null);
			for(MTable table : tables){
				if(!table.isActive())
					continue;
				
				Document tableDoc = table.get_xmlDocument(true, dataOnly);
				Element tableEl = tableDoc.getDocumentElement();
				doc.adoptNode(tableEl);
				tablesEl.appendChild(tableEl);
				
				Element columnsEl = doc.createElement(TAG_COLUMNS);
				tableEl.appendChild(columnsEl);
				
				MColumn[] columns = table.getColumns(true);
				for (MColumn column : columns) {
					if (!column.isActive())
						continue;
					Document columnDoc = column.get_xmlDocument(true, dataOnly);
					Element columnEl = columnDoc.getDocumentElement();
					doc.adoptNode(columnEl);
					columnsEl.appendChild(columnEl);
					
					int refId = column.getAD_Reference_ID();
					String fieldType = MReference.getFieldType(refId);
					Element disp = doc.createElement(TAG_DISPLAYTYPE);
					disp.appendChild(doc.createTextNode(fieldType));
					columnEl.appendChild(disp);
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
		} catch (Exception e){
			log.log(Level.SEVERE,"",e);
		}	// get_xmlAllTablesByWindow
		
		return xml;
	}

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
			NodeList nl = doc.getElementsByTagName("AD_Window");
			if (nl.getLength() < 1)
				return null;
			
			Element winEl = (Element) nl.item(0);
			Element tabsEl = doc.createElement(TAG_TABS);
			winEl.appendChild(tabsEl);
			
			Set<Integer> fieldGroupIDs = new HashSet<Integer>();
//			Set<Integer> columnIDs = new HashSet<Integer>();
//			Set<Integer> refIDs = new HashSet<Integer>();
			
			MTab[] tabs = getTabs(true, null);
			for (MTab tab : tabs) {
				if (!tab.isActive())
					continue;
				Document tabDoc = tab.get_xmlDocument(true, dataOnly);
				Element tabEl = tabDoc.getDocumentElement();
				doc.adoptNode(tabEl);
				tabsEl.appendChild(tabEl);

				Element fieldsEl = doc.createElement(TAG_FIELDS);
				tabEl.appendChild(fieldsEl);
				
				MField[] fields = tab.getFields(true, null);
				for (MField field : fields) {
					if (!field.isActive())
						continue;
					Document fieldDoc = field.get_xmlDocument(true, dataOnly);
					Element fieldEl = fieldDoc.getDocumentElement();
					doc.adoptNode(fieldEl);
					fieldsEl.appendChild(fieldEl);
					int refId = field.getAD_Reference_ID();
					MColumn col = new MColumn(getCtx(), field.getAD_Column_ID(), null);
					if (refId == 0) {
						refId = col.getAD_Reference_ID();
					}
					String	fieldType = MReference.getFieldType(refId);
					Element disp = doc.createElement(TAG_DISPLAYTYPE);
					disp.appendChild(doc.createTextNode(fieldType));
					fieldEl.appendChild(disp);
					
					Element mand = doc.createElement(TAG_COL_MANDATORY);
					mand.appendChild(doc.createTextNode(col.isMandatory()?"Y":"N"));
					fieldEl.appendChild(mand);
					
					Element mandUi = doc.createElement(TAG_COL_MANDATORY_UI);
					mandUi.appendChild(doc.createTextNode(col.isMandatoryUI()?"Y":"N"));
					fieldEl.appendChild(mandUi);

					// collect field group Ids	
					if (field.getAD_FieldGroup_ID() != 0)
						fieldGroupIDs.add(field.getAD_FieldGroup_ID());
//					columnIDs.add(field.getAD_Column_ID());
//					if (field.getAD_Reference_ID() != 0)
//						refIDs.add(field.getAD_Reference_ID());
				}
			}
			
			Element fieldGroupsEl = doc.createElement(TAG_FIELDGROUPS);
			winEl.appendChild(fieldGroupsEl);
			
			for (Integer fgId : fieldGroupIDs) {
				X_AD_FieldGroup fg = new X_AD_FieldGroup(getCtx(), fgId, null);
				Document fgDoc = fg.get_xmlDocument(true, dataOnly);
				Element fieldEl = fgDoc.getDocumentElement();
				doc.adoptNode(fieldEl);
				fieldGroupsEl.appendChild(fieldEl);
			}
			
//			for (Integer colId : columnIDs) {
//				MColumn col = new MColumn(getCtx(), colId, null);
//				refIDs.add(col.getAD_Reference_ID());
//				
//				Document colDoc = col.get_xmlDocument(true, dataOnly);
//				Element colEl = colDoc.getDocumentElement();
//				doc.adoptNode(colEl);
//				columnsEl.appendChild(colEl);
//			}

			
			
			for (Integer fgId : fieldGroupIDs) {
				X_AD_FieldGroup fg = new X_AD_FieldGroup(getCtx(), fgId, null);
				Document fgDoc = fg.get_xmlDocument(true, dataOnly);
				Element fieldEl = fgDoc.getDocumentElement();
				doc.adoptNode(fieldEl);
				fieldGroupsEl.appendChild(fieldEl);
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
	

//  TODO : delete/swap with xml doc version in case 
	
//	public StringBuffer get_xmlCompleteString (StringBuffer xml, boolean dataOnly) 
//	{
//		if (xml == null)
//			xml = new StringBuffer();
//		
//		get_xmlString(xml, dataOnly);
//		int idx = xml.lastIndexOf("</AD_Window>");
//		xml.delete(idx, xml.length());
//		xml.append("<AD_Tabs>");
//		
//		MTab[] tabs = getTabs(true, null);
//		for (MTab tab : tabs) {
//			xml = tab.get_xmlString(xml, dataOnly);
//			idx = xml.lastIndexOf("</AD_Tab>");
//			xml.delete(idx, xml.length());
//			
//			xml.append("<AD_Fields>");
//			MField[] fields = tab.getFields(true, null);
//			for (MField field : fields) {
//				xml = field.get_xmlString(xml, dataOnly);
//			}
//			xml.append("</AD_Fields>");
//			xml.append("</AD_Tab>");
//		}
//		xml.append("</AD_Tabs>");
//		xml.append("</AD_Window>");
//		
//		return xml;
//	}
	
}	//	MWindow
