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
 * Container Model
 * 
 * @author Yves Sandfort
 * @version $Id: MContainer.java,v 1.20 2006/09/05 23:22:53 comdivision Exp $
 */
public class MContainer extends X_CM_Container
{
    /** Logger for class MContainer */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MContainer.class);
	/**	serialVersionUID	*/
	private static final long serialVersionUID = 395679572291279730L;
	
	/**
	 * 	get Container by Relative URL
	 *	@param ctx
	 *	@param relURL
	 *	@param CM_WebProject_Id
	 *	@param trx
	 *	@return Container or null if not found
	 */
	public static MContainer get(Ctx ctx, String relURL, int CM_WebProject_Id, Trx trx) {
		MContainer thisContainer = null;
		String sql = "SELECT * FROM CM_Container WHERE (RelativeURL LIKE ? OR RelativeURL LIKE ?) AND CM_WebProject_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setString (1,relURL);
			pstmt.setString (2,relURL+"/");
			pstmt.setInt(3, CM_WebProject_Id);
			rs = pstmt.executeQuery();
			if (rs.next())
				thisContainer = (new MContainer(ctx, rs, trx));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return thisContainer;
	}
	
	/**
	 * 	get Container by Name
	 *	@param ctx
	 *  @param Name 
	 *	@param CM_WebProject_Id
	 *	@param trx
	 *	@return Container or null if not found
	 */
	public static MContainer getByName(Ctx ctx, String Name, int CM_WebProject_Id, Trx trx) {
		MContainer thisContainer = null;
		String sql = "SELECT * FROM CM_Container WHERE (Name LIKE ?) AND CM_WebProject_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setString (1,Name);
			pstmt.setInt(2, CM_WebProject_Id);
			rs = pstmt.executeQuery();
			if (rs.next())
				thisContainer = (new MContainer(ctx, rs, trx));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return thisContainer;
	}
	
	
	/**
	 * 	get Container by Title
	 *	@param ctx
	 *  @param Title 
	 *	@param CM_WebProject_Id
	 *	@param trx
	 *	@return Container or null if not found
	 */
	public static MContainer getByTitle(Ctx ctx, String Title, int CM_WebProject_Id, Trx trx) {
		MContainer thisContainer = null;
		String sql = "SELECT * FROM CM_Container WHERE (Title LIKE ?) AND CM_WebProject_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setString (1,Title);
			pstmt.setInt(2, CM_WebProject_Id);
			rs = pstmt.executeQuery();
			if (rs.next())
				thisContainer = (new MContainer(ctx, rs, trx));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return thisContainer;
	}
	
	/**
	 * 	get Container
	 *	@param ctx
	 *	@param CM_Container_ID
	 *	@param CM_WebProject_Id
	 *	@param trx
	 *	@return Container or null if not found
	 */
	public static MContainer get(Ctx ctx, int CM_Container_ID, int CM_WebProject_Id, Trx trx) {
		MContainer thisContainer = null;
		String sql = "SELECT * FROM CM_Container WHERE CM_Container_ID=? AND CM_WebProject_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt(1, CM_Container_ID);
			pstmt.setInt(2, CM_WebProject_Id);
			rs = pstmt.executeQuery();
			if (rs.next())
				thisContainer = (new MContainer(ctx, rs, trx));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return thisContainer;
	}
	
	/**
     * Deploy Stage into Container
     * 
     * @param project WebProject
     * @param stage Stage to deploy from
     * @param path Relative URL to it
     * @return Container
     */
	public static MContainer deploy (MWebProject project, MCStage stage,
		String path)
	{
		MContainer cc = getDirect (stage.getCtx(), stage.getCM_CStage_ID (),
			stage.get_Trx ());
		if (cc == null) // new
			cc = new MContainer (stage.getCtx (), 0, stage.get_Trx ());
		cc.setStage (project, stage, path);
		cc.save ();
		if (!stage.isSummary ())
		{
			cc.updateElements (project, stage, stage.get_Trx ());
			cc.updateTTables (project, stage, stage.get_Trx ());
		}
		return cc;
	}	// copy

	/**
     * Get Container directly from DB (not cached)
     * 
     * @param ctx context
     * @param CM_Container_ID Container ID
     * @param trx transaction
     * @return Container or null
     */
	public static MContainer getDirect (Ctx ctx, int CM_Container_ID,
		Trx trx)
	{
		MContainer cc = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM CM_Container WHERE CM_Container_ID=?";
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt (1, CM_Container_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				cc = new MContainer (ctx, rs, trx);
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
		return cc;
	} // getDirect

	/**
     * Get Containers
     * 
     * @param project
     *            Project to use
     * @return stages
     */
	public static MContainer[] getContainers (MWebProject project)
	{
		ArrayList<MContainer> list = new ArrayList<MContainer> ();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM CM_Container WHERE CM_WebProject_ID=? ORDER BY CM_Container_ID";
		try
		{
			pstmt = DB.prepareStatement (sql, project.get_Trx ());
			pstmt.setInt (1, project.getCM_WebProject_ID ());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MContainer (project.getCtx (), rs, project
					.get_Trx ()));
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
		MContainer[] retValue = new MContainer[list.size ()];
		list.toArray (retValue);
		return retValue;
	} // getContainers

	/** Logger */
	private static CLogger s_log = CLogger.getCLogger (MContainer.class);

	
	/***************************************************************************
     * Standard Constructor
     * 
     * @param ctx context
     * @param CM_Container_ID id
     * @param trx transaction
     */
	public MContainer (Ctx ctx, int CM_Container_ID, Trx trx)
	{
		super (ctx, CM_Container_ID, trx);
		if (CM_Container_ID == 0)
		{
			setIsValid(false);
			setIsIndexed(false);
			setIsSecure(false);
			setIsSummary(false);
		}
	} // MContainer

	/**
     * Load Constructor
     * 
     * @param ctx
     *            context
     * @param rs
     *            result set
     * @param trx
     *            transaction
     */
	public MContainer (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	} // MContainer

	/** Web Project */
	private MWebProject m_project = null;

	/** Stage Source */
	private MCStage	 m_stage   = null;
	
	/** Template */
	private MTemplate m_template = null;

	/**
     * Get Web Project
     * 
     * @return web project
     */
	public MWebProject getWebProject ()
	{
		if (m_project == null)
			m_project = MWebProject.get (getCtx (), getCM_WebProject_ID ());
		return m_project;
	} // getWebProject
	
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

	/**
     * Get AD_Tree_ID
     * 
     * @return tree
     */
	public int getAD_Tree_ID ()
	{
		return getWebProject ().getAD_TreeCMC_ID ();
	} // getAD_Tree_ID;

	/**
     * Set/Copy Stage
     * 
     * @param project
     *            parent
     * @param stage
     *            stage
     * @param path
     *            path
     */
	protected void setStage (MWebProject project, MCStage stage, String path)
	{
		m_stage = stage;
		PO.copyValues (stage, this);
		setAD_Client_ID (project.getAD_Client_ID ());
		setAD_Org_ID (project.getAD_Org_ID ());
		setIsActive (stage.isActive ());
		setCM_ContainerLink_ID (stage.getCM_CStageLink_ID ());
		//
		setRelativeURL (path + stage.getRelativeURL ());
		//
		if (getMeta_Author () == null || getMeta_Author ().length () == 0)
			setMeta_Author (project.getMeta_Author ());
		if (getMeta_Content () == null || getMeta_Content ().length () == 0)
			setMeta_Content (project.getMeta_Content ());
		if (getMeta_Copyright () == null || getMeta_Copyright ().length () == 0)
			setMeta_Copyright (project.getMeta_Copyright ());
		if (getMeta_Publisher () == null || getMeta_Publisher ().length () == 0)
			setMeta_Publisher (project.getMeta_Publisher ());
		if (getMeta_RobotsTag () == null || getMeta_RobotsTag ().length () == 0)
			setMeta_RobotsTag (project.getMeta_RobotsTag ());
	} // setStage

	/**
     * Update Elements in Container from Stage
     * 
     * @param project
     *            project
     * @param stage
     *            stage
     * @param trx
     *            Transaction
     */
	protected void updateElements (MWebProject project, MCStage stage,
		Trx trx)
	{
		org.compiere.cm.CacheHandler thisHandler = new org.compiere.cm.CacheHandler (
			org.compiere.cm.CacheHandler.convertJNPURLToCacheURL (getCtx()
				.getContext("java.naming.provider.url")), log, getCtx (),
			get_Trx ());
		// First update the new ones...
		int[] tableKeys = PO.getAllIDs ("CM_CStage_Element",
			"CM_CStage_ID=" + stage.get_ID (), trx);
		if (tableKeys != null && tableKeys.length > 0)
		{
			for (int element : tableKeys) {
				X_CM_CStage_Element thisStageElement = new X_CM_CStage_Element (
					project.getCtx (), element, trx);
				int[] thisContainerElementKeys = PO
					.getAllIDs ("CM_Container_Element", "CM_Container_ID="
						+ stage.get_ID () + " AND Name LIKE '"
						+ thisStageElement.getName () + "'", trx);
				X_CM_Container_Element thisContainerElement;
				if (thisContainerElementKeys != null
					&& thisContainerElementKeys.length > 0)
				{
					thisContainerElement = new X_CM_Container_Element (project
						.getCtx (), thisContainerElementKeys[0], trx);
				}
				else
				{
					thisContainerElement = new X_CM_Container_Element (project
						.getCtx (), 0, trx);
				}
				thisContainerElement.setCM_Container_ID (stage.get_ID ());
				X_CM_CStage_Element stageElement = new X_CM_CStage_Element (
					project.getCtx (), element, trx);
				thisContainerElement.setName (stageElement.getName ());
				thisContainerElement.setDescription (stageElement.getDescription());
				thisContainerElement.setHelp (stageElement.getHelp ());
				thisContainerElement.setIsActive (stageElement.isActive ());
				thisContainerElement.setIsValid (stageElement.isValid ());
				String contentHTML = thisStageElement.getContentHTML ();
				thisContainerElement.setContentHTML (contentHTML);
				// PO.copyValues(new
				// X_CM_CStage_Element(project.getCtx(),tableKeys[i],trx),
				// thisContainerElement);
				thisContainerElement.save (trx);
				// Remove Container from cache
				thisHandler.cleanContainerElement (thisContainerElement
					.get_ID ());
			}
		}
		// Now we are checking the existing ones to delete the unneeded ones...
		tableKeys = PO.getAllIDs ("CM_Container_Element",
			"CM_Container_ID=" + stage.get_ID (), trx);
		if (tableKeys != null && tableKeys.length > 0)
		{
			for (int element : tableKeys) {
				X_CM_Container_Element thisContainerElement = new X_CM_Container_Element (
					project.getCtx (), element, trx);
				int[] thisCStageElementKeys = PO
					.getAllIDs ("CM_CStage_Element", "CM_CStage_ID="
						+ stage.get_ID () + " AND Name LIKE '"
						+ thisContainerElement.getName () + "'", trx);
				// If we cannot find a representative in the Stage we will delete from production
				if (thisCStageElementKeys == null
					|| thisCStageElementKeys.length < 1)
				{
					// First delete it from cache, then delete the record itself
					thisHandler.cleanContainerElement (thisContainerElement
						.get_ID ());
					thisContainerElement.delete (true);
				}
			}
		}
	}

	/**
     * Update Elements in Container from Stage
     * 
     * @param project
     *            project
     * @param stage
     *            stage
     * @param trx
     *            Transaction
     */
	protected void updateTTables (MWebProject project, MCStage stage,
		Trx trx)
	{
		int[] tableKeys = PO.getAllIDs ("CM_CStageTTable",
			"CM_CStage_ID=" + stage.get_ID (), trx);
		if (tableKeys != null && tableKeys.length > 0)
		{
			for (int element : tableKeys) {
				X_CM_CStageTTable thisStageTTable = new X_CM_CStageTTable (
					project.getCtx (), element, trx);
				int[] thisContainerTTableKeys = PO.getAllIDs (
					"CM_ContainerTTable", "CM_Container_ID=" + stage.get_ID ()
						+ " AND CM_TemplateTable_ID="
						+ thisStageTTable.getCM_TemplateTable_ID (), trx);
				X_CM_ContainerTTable thisContainerTTable;
				if (thisContainerTTableKeys != null
					&& thisContainerTTableKeys.length > 0)
				{
					thisContainerTTable = new X_CM_ContainerTTable (project
						.getCtx (), thisContainerTTableKeys[0], trx);
				}
				else
				{
					thisContainerTTable = new X_CM_ContainerTTable (project
						.getCtx (), 0, trx);
				}
				thisContainerTTable.setCM_Container_ID (stage.get_ID ());
				PO.copyValues (new X_CM_CStageTTable (project.getCtx (),
					element, trx), thisContainerTTable);
				thisContainerTTable.save (trx);
			}
		}
	}

	/**
     * SaveNew getID
     * 
     * @return ID
     */
	@Override
	protected int saveNew_getID ()
	{
		if (m_stage != null)
			return m_stage.getCM_CStage_ID ();
		return 0;
	} // saveNew_getID

	/**
     * String Representation
     * 
     * @return info
     */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MContainer[").append (get_ID ())
			.append ("-").append (getName ()).append ("]");
		return sb.toString ();
	} // toString

	/**
     * After Save. Insert - create tree
     * 
     * @param newRecord
     *            insert
     * @param success
     *            save success
     * @return true if saved
     */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		if (newRecord)
		{
			StringBuffer sb = new StringBuffer (
				"INSERT INTO AD_TreeNodeCMC "
					+ "(AD_Client_ID,AD_Org_ID, IsActive,Created,CreatedBy,Updated,UpdatedBy, "
					+ "AD_Tree_ID, Node_ID, Parent_ID, SeqNo) " 
					+ " VALUES (?,0, 'Y', SysDate, 0, SysDate, 0,?,?, 0, 999)");
			Object[] params = new Object[]{getAD_Client_ID (),getAD_Tree_ID (),get_ID ()};
			int no = DB.executeUpdate (get_Trx (), sb.toString (),params);
			if (no > 0)
				log.fine ("#" + no + " - TreeType=CMC");
			else
				log.warning ("#" + no + " - TreeType=CMC");
			return no > 0;
		}
		reIndex(newRecord);
		return success;
	} // afterSave
	
	protected MContainerElement[] getAllElements()
	{
		int elements[] = PO.getAllIDs("CM_Container_Element", "CM_Container_ID=" + get_ID(), get_Trx());
		if (elements.length>0)
		{
			MContainerElement[] containerElements = new MContainerElement[elements.length];
			for (int i=0;i<elements.length;i++)
			{
				containerElements[i] = new MContainerElement(getCtx(), elements[i], get_Trx());
			}
			return containerElements;
		} else {
			return null;
		}
	}
	
	@Override
	protected boolean beforeDelete()
	{
		// Clean own index
		MIndex.cleanUp(get_Trx(), getAD_Client_ID(), get_Table_ID(), get_ID());
		// Clean ElementIndex
		MContainerElement[] theseElements = getAllElements();
		if (theseElements!=null)
		{
			for (MContainerElement element : theseElements) {
				element.delete(false);
			}
		}
		//
		StringBuffer sb = new StringBuffer ("DELETE FROM AD_TreeNodeCMC ")
			.append (" WHERE Node_ID= ? AND AD_Tree_ID= ? ");
		Object[] params = new Object[] {get_ID(),getAD_Tree_ID()};
		int no = DB.executeUpdate (get_Trx (), sb.toString (),params);
		if (no > 0)
			log.fine ("#" + no + " - TreeType=CMC");
		else
			log.warning ("#" + no + " - TreeType=CMC");
		return no > 0;
	}

	/**
     * After Delete
     * 
     * @param success
     * @return deleted
     */
	@Override
	protected boolean afterDelete (boolean success)
	{
		if (!success)
			return success;
		//
		StringBuffer sb = new StringBuffer ("DELETE FROM AD_TreeNodeCMC ")
			.append (" WHERE Node_ID= ?  AND AD_Tree_ID=? ");
		Object[] params = new Object[] {get_IDOld(),getAD_Tree_ID()};
		int no = DB.executeUpdate (get_Trx (), sb.toString (),params);
		// If 0 than there is nothing to delete which is okay.
		if (no > 0)
			log.fine ("#" + no + " - TreeType=CMC");
		else
			log.warning ("#" + no + " - TreeType=CMC");
		return true;
	} // afterDelete
	
	/**
	 * 	reIndex
	 *	@param newRecord
	 */
	public void reIndex(boolean newRecord)
	{
		if (isIndexed()) {
			String [] toBeIndexed = new String[8];
			toBeIndexed[0] = this.getName();
			toBeIndexed[1] = this.getDescription();
			toBeIndexed[2] = this.getRelativeURL();
			toBeIndexed[3] = this.getMeta_Author();
			toBeIndexed[4] = this.getMeta_Copyright();
			toBeIndexed[5] = this.getMeta_Description();
			toBeIndexed[6] = this.getMeta_Keywords();
			toBeIndexed[7] = this.getMeta_Publisher();
			MIndex.reIndex (newRecord, toBeIndexed, getCtx(), getAD_Client_ID(), get_Table_ID(), get_ID(), getCM_WebProject_ID(), this.getUpdated());
			MContainerElement[] theseElements = getAllElements();
			if (theseElements!=null)
				for (MContainerElement element : theseElements)
					element.reIndex (false);
		}
		if (!isIndexed() && !newRecord)
			MIndex.cleanUp (get_Trx(), getAD_Client_ID(), get_Table_ID(), get_ID());
	} // reIndex
} // MContainer
