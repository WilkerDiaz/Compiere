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

import org.compiere.framework.*;
import org.compiere.util.*;

/**
 * 	Container Stage Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MCStage.java,v 1.11 2006/09/23 10:44:05 comdivision Exp $
 */
public class MCStage extends X_CM_CStage
{
    /** Logger for class MCStage */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MCStage.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Stages
	 *	@param project project
	 *	@return stages 
	 */
	public static MCStage[] getStages (MWebProject project)
	{
		ArrayList<MCStage> list = new ArrayList<MCStage>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM CM_CStage WHERE CM_WebProject_ID=? ORDER BY CM_CStage_ID";
		try
		{
			pstmt = DB.prepareStatement (sql, project.get_Trx());
			pstmt.setInt (1, project.getCM_WebProject_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MCStage (project.getCtx(), rs, project.get_Trx()));
			}
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		MCStage[] retValue = new MCStage[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getStages

	/**
	 * 	Get Stage by Name
	 *	@param project project
	 *	@return stages 
	 */
	public static MCStage getByName (MWebProject project, String RelativeURL, int parent_ID)
	{
		MCStage retValue = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM CM_CStage WHERE CM_WebProject_ID=? AND RelativeURL LIKE ? " + //1,2
				"AND CM_CStage_ID IN (SELECT Node_ID FROM AD_TreeNodeCMS WHERE " +
					" AD_Tree_ID=? AND Parent_ID=?)" + // 3, 4
				"ORDER BY CM_CStage_ID";
		try
		{
			pstmt = DB.prepareStatement (sql, project.get_Trx());
			pstmt.setInt (1, project.getCM_WebProject_ID());
			pstmt.setString (2, RelativeURL);
			pstmt.setInt (3, project.getAD_TreeCMS_ID ());
			pstmt.setInt (4, parent_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MCStage (project.getCtx(), rs, project.get_Trx());
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return retValue;
	}	//	getStages

	/**
	 * 	Get Stages by Template
	 *	@param project project
	 *	@return stages 
	 */
	public static MCStage[] getStagesByTemplate (MWebProject project, int CM_Template_ID)
	{
		ArrayList<MCStage> list = new ArrayList<MCStage>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM CM_CStage WHERE CM_WebProject_ID=? AND CM_Template_ID=?";
		try
		{
			pstmt = DB.prepareStatement (sql, project.get_Trx());
			pstmt.setInt (1, project.getCM_WebProject_ID());
			pstmt.setInt (2, CM_Template_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MCStage (project.getCtx(), rs, project.get_Trx()));
			}
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MCStage[] retValue = new MCStage[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getStages

	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MCStage.class);
	
	/** Template */
	private MTemplate m_template = null;

	/**
	 * Get Template from Cache, or load it
	 * @return Template
	 */
	public MTemplate getTemplate() 
	{
		if (getCM_Template_ID()>0 && m_template==null)
			m_template = MTemplate.get(getCtx(), getCM_Template_ID(), null);
		return m_template;
	} // getTemplate
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param CM_CStage_ID id
	 *	@param trx tansaction
	 */
	public MCStage (Ctx ctx, int CM_CStage_ID, Trx trx)
	{
		super (ctx, CM_CStage_ID, trx);
		if (CM_CStage_ID == 0)
		{
			setIsValid(false);
			setIsModified(false);
			setIsSecure(false);
			setIsSummary(false);
			setIsIndexed(false);
		}
	}	//	MCStage

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MCStage (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MCStage
	
	/** Web Project			*/
	private MWebProject 	m_project = null;
	
	
	/**
	 * 	Set Relative URL
	 *	@param RelativeURL
	 */
	@Override
	public void setRelativeURL (String RelativeURL)
	{
		if (RelativeURL != null)
		{
			if (RelativeURL.endsWith("/"))
				RelativeURL = RelativeURL.substring(0, RelativeURL.length()-1);
			int index = RelativeURL.lastIndexOf("/");
			if (index != -1)
				RelativeURL = RelativeURL.substring(index+1);
		}
		super.setRelativeURL (RelativeURL);
	}	//	setRelativeURL
	
	/**
	 * 	Get Web Project
	 *	@return web project
	 */
	public MWebProject getWebProject()
	{
		if (m_project == null)
			m_project = MWebProject.get(getCtx(), getCM_WebProject_ID());
		return m_project;
	}	//	getWebProject
	
	/**
	 * 	Get AD_Tree_ID
	 *	@return tree
	 */
	public int getAD_Tree_ID()
	{
		return getWebProject().getAD_TreeCMS_ID();
	}	//	getAD_Tree_ID;
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MCStage[")
			.append (get_ID()).append ("-").append (getName()).append ("]");
		return sb.toString ();
	} 	//	toString
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		//	Length >0 if not (Binary, Image, Text Long)
		if ((!this.isSummary() || this.getContainerType().equals ("L")) && getCM_Template_ID()==0)
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "Template"));
			return false;
		}
		// On Modification set isModified
		if (is_Changed () && !is_ValueChanged("IsModified"))
			setIsModified(true);
		//	Validate
		setRelativeURL(getRelativeURL());
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save.
	 * 	Insert
	 * 	- create tree
	 *	@param newRecord insert
	 *	@param success save success
	 *	@return true if saved
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		// If Not Summary Node check whether all Elements and Templatetable Records exist.
		if (!isSummary()) { 
			checkElements();
			checkTemplateTable();
		}
		if (newRecord)
		{
			StringBuffer sb = new StringBuffer ("INSERT INTO AD_TreeNodeCMS "
				+ "(AD_Client_ID,AD_Org_ID, IsActive,Created,CreatedBy,Updated,UpdatedBy, "
				+ "AD_Tree_ID, Node_ID, Parent_ID, SeqNo) "
				+ "VALUES (?,0, 'Y', SysDate, 0, SysDate, 0,?,?, 0, 999)");
			Object [] params = new Object[]{getAD_Client_ID(),getAD_Tree_ID(),get_ID()};
			int no = DB.executeUpdate(get_Trx(), sb.toString(),params);
			if (no > 0)
				log.fine("#" + no + " - TreeType=CMS");
			else
				log.warning("#" + no + " - TreeType=CMS");
			return no > 0;
		}
		/*if (success) {
		}*/
		return success;
	}	//	afterSave
	
	/**
	 * 	After Delete
	 *	@param success
	 *	@return deleted
	 */
	@Override
	protected boolean afterDelete (boolean success)
	{
		if (!success)
			return success;
		//
		StringBuffer sb = new StringBuffer ("DELETE FROM AD_TreeNodeCMS ")
			.append(" WHERE Node_ID=?  AND AD_Tree_ID=? ");
		Object[] params = new Object[]{get_IDOld(),getAD_Tree_ID()};
		int no = DB.executeUpdate(get_Trx(), sb.toString(),params);
		if (no > 0)
			log.fine("#" + no + " - TreeType=CMS");
		else
			log.warning("#" + no + " - TreeType=CMS");
		return no > 0;
	}	//	afterDelete
	
	/**
	 * 	Validate
	 *	@return info
	 */
	public String validate()
	{
		return "";
	}	//	validate
	
	/**
	 * Check whether all Elements exist
	 * @return true if updated
	 */
	protected boolean checkElements () {
		MTemplate thisTemplate = new MTemplate(getCtx(), this.getCM_Template_ID(), get_Trx());
		StringBuffer thisElementList = new StringBuffer(thisTemplate.getElements());
		while (thisElementList.indexOf("\n")>=0) {
			String thisElement = thisElementList.substring(0,thisElementList.indexOf("\n"));
			thisElementList.delete(0,thisElementList.indexOf("\n")+1);
			if (thisElement!=null && !thisElement.equals(""))
				checkElement(thisElement);
		}
		String thisElement = thisElementList.toString();
		if (thisElement!=null && !thisElement.equals(""))
			checkElement(thisElement);
		return true;
	}
	
	protected MCStageElement getElementByName(String elementName) {
		return MCStageElement.getByName (getCtx(), get_ID(), elementName, get_Trx());
	}

	/**
	 * Check single Element, if not existing create it...
	 * @param elementName
	 */
	protected void checkElement(String elementName) {
		MCStageElement thisElement = getElementByName(elementName);
		if (thisElement==null) {
			thisElement = new MCStageElement(getCtx(), 0, get_Trx());
			thisElement.setAD_Client_ID(getAD_Client_ID());
			thisElement.setAD_Org_ID(getAD_Org_ID());
			thisElement.setCM_CStage_ID(this.get_ID());
			thisElement.setContentHTML(" ");
			thisElement.setName(elementName);
			thisElement.save(get_Trx());
		}
	}
	
	/**
	 * Check whether all Template Table records exits
	 * @return true if updated
	 */
	protected boolean checkTemplateTable () {
		int [] tableKeys = PO.getAllIDs("CM_TemplateTable", "CM_Template_ID=" + this.getCM_Template_ID(), get_Trx());
		if (tableKeys!=null) {
			for (int element : tableKeys) {
				X_CM_TemplateTable thisTemplateTable = new X_CM_TemplateTable(getCtx(), element, get_Trx());
				int [] existingKeys = PO.getAllIDs("CM_CStageTTable", "CM_TemplateTable_ID=" + thisTemplateTable.get_ID(), get_Trx());
				if (existingKeys==null || existingKeys.length==0) {
					X_CM_CStageTTable newCStageTTable = new X_CM_CStageTTable(getCtx(), 0, get_Trx());
					newCStageTTable.setAD_Client_ID(getAD_Client_ID());
					newCStageTTable.setAD_Org_ID(getAD_Org_ID());
					newCStageTTable.setCM_CStage_ID(get_ID());
					newCStageTTable.setCM_TemplateTable_ID(thisTemplateTable.get_ID());
					newCStageTTable.setDescription(thisTemplateTable.getDescription());
					newCStageTTable.setName(thisTemplateTable.getName());
					newCStageTTable.setOtherClause(thisTemplateTable.getOtherClause());
					newCStageTTable.setWhereClause(thisTemplateTable.getWhereClause());
					newCStageTTable.save();
				}
			}
		}
		return true;
	}
	
}	//	MCStage
