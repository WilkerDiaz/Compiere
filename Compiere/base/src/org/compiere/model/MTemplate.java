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

import org.compiere.util.*;

/**
 * Web Template Model
 * 
 * @author Yves Sandfort
 * @version $Id: MTemplate.java,v 1.12 2006/08/08 18:56:05 comdivision Exp $
 */
public class MTemplate extends X_CM_Template
{
    /** Logger for class MTemplate */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MTemplate.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Get MTemplate from Cache
     * 
     *	@param ctx context
     *	@param CM_Template_ID id
     *	@param trx Transaction
     *	@return MWebProject
     */
	public static MTemplate get (Ctx ctx, int CM_Template_ID, Trx trx)
	{
		MTemplate retValue = new MTemplate (ctx, CM_Template_ID, trx);
		if (retValue != null)
			return retValue;
		retValue = new MTemplate (ctx, CM_Template_ID, null);
		return retValue;
	}	// get

	/**
     * Standard Constructor
     * 
     *	@param ctx context
     *	@param CM_Template_ID id
     *	@param trx transaction
     */
	public MTemplate (Ctx ctx, int CM_Template_ID, Trx trx)
	{
		super (ctx, CM_Template_ID, trx);
	} // MTemplate

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
	public MTemplate (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	} // MTemplate

	/** Web Project */
	private MWebProject m_project = null;
	/** 
     * preBuildTemplate contains a preset Version including needed Subtemplates
     */
	private StringBuffer m_preBuildTemplate;
	
	/** Logger								*/
	private static CLogger		s_log = CLogger.getCLogger (MTemplate.class);

	/**
	 * 	get Template by Name
	 *	@param ctx
	 *	@param name
	 *	@param projectID
	 *	@param trx
	 *	@return Template
	 */
	public static MTemplate getByName (Ctx ctx, String name, int projectID, Trx trx)
	{
		String sql = "SELECT * FROM CM_Template WHERE Value LIKE ? AND CM_WebProject_ID=?";
		MTemplate thisElement = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setString (1, name);
			pstmt.setInt (2, projectID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				thisElement = new MTemplate(ctx, rs, trx);
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "getByName", e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return thisElement;
	}	//	getEntries

	
	/**
     * Get Web Project
     * @return web project
     */
	public MWebProject getWebProject ()
	{
		if (m_project == null)
			m_project = MWebProject.get (getCtx (), getCM_WebProject_ID ());
		return m_project;
	}	// getWebProject

	/**
     *	Get AD_Tree_ID
     *	@return tree
     */
	public int getAD_Tree_ID ()
	{
		return getWebProject ().getAD_TreeCMT_ID ();
	}	// getAD_Tree_ID;
	
	/**	m_isUseAd Global Use Ad **/
	private boolean m_isUseAd = false;
	
	/** m_adTemplates StringBuffer with all IDs of Templates **/
	private StringBuffer m_adTemplates = new StringBuffer();
	
	/**
	 * 	Check for isUseAd if needed including subtemplates
	 *	@param includeSub
	 *	@return m_isUseAd
	 */
	public boolean isUseAd(boolean includeSub) 
	{
		if (!includeSub)
		{
			return isUseAd();
		} else {
			if (m_preBuildTemplate==null) 
				rebuildTemplate();
			if (!m_isUseAd && isUseAd()) 
				m_isUseAd = isUseAd();
			if (isUseAd())
				m_adTemplates.append (get_ID() + ",");
			return m_isUseAd;
		}
	}
	
	/**	m_isNews Global Use News **/
	private boolean m_isNews = false;
	
	/**
	 * 	Check for isNews if needed including subtemplates
	 *	@param includeSub
	 *	@return m_isNews
	 */
	public boolean isNews(boolean includeSub) 
	{
		if (!includeSub)
		{
			return isNews ();
		} else {
			if (m_preBuildTemplate==null) 
				rebuildTemplate();
			if (m_isNews!=isNews()) 
				m_isNews = isNews();
			return m_isNews;
		}
	}
	
	/** m_isRequest Global Request News **/
	private boolean m_isRequest = false;
	
	/**
	 * 	Check whether we need to include Request data
	 *	@param includeSub
	 *	@return m_isRequest
	 */
	public boolean isRequest(boolean includeSub) 
	{
		if (m_preBuildTemplate==null)
			rebuildTemplate();
		return m_isRequest;
	}

	/**
     * Get the Template we prebuild (this means with added subtemplates)
     * @return StringBuffer with complete XSL Template
     */
	public StringBuffer getPreBuildTemplate ()
	{
		if (m_preBuildTemplate == null)
			rebuildTemplate();
		return m_preBuildTemplate;
	}

	/**
     * Prebuild Template, this also set's parameters of subtemplates 
     * on the main template
     */
	public void rebuildTemplate ()
	{
		// We will build the prebuild code, so we check which subs are
		// needed and build it depending on them
		m_preBuildTemplate = new StringBuffer (getTemplateXST ());
		// Let's see whether the template calls Subtemplates...
		if (m_preBuildTemplate.indexOf ("<xsl:call-template") >= 0)
		{
			StringBuffer subTemplates = new StringBuffer ();
			int pos = 0;
			ArrayList<String> subTemplateNames = new ArrayList<String> ();
			while (m_preBuildTemplate.indexOf ("<xsl:call-template", pos) >= 0)
			{
				String thisName = null;
				int beginPos = m_preBuildTemplate.indexOf (
					"<xsl:call-template", pos);
				int endPos = m_preBuildTemplate.indexOf ("/>", beginPos);
				if (m_preBuildTemplate.indexOf (">", beginPos) < endPos)
				{
					endPos = m_preBuildTemplate.indexOf (">", beginPos) + 1;
				}
				String tempTemplate = m_preBuildTemplate.substring (beginPos,
					endPos);
				pos = m_preBuildTemplate.indexOf ("<xsl:call-template", pos)
					+ tempTemplate.length ();
				if (tempTemplate.indexOf ("name=") >= 0)
				{
					thisName = tempTemplate.substring (tempTemplate
						.indexOf ("name=\"") + 6, tempTemplate.indexOf (
						"\"", tempTemplate.indexOf ("name=\"") + 7));
					if (!subTemplateNames.contains (thisName))
						subTemplateNames.add (thisName);
				}
			}
			// Build all the subtemplates and add them to the main template
			for (int i=0;i<subTemplateNames.size ();i++) 
			{
				MTemplate subTemplate = getByName(getCtx(), subTemplateNames.get (i), getCM_WebProject_ID(), get_Trx());
				if (subTemplate != null)
				{
					if (subTemplate.containsSubtemplates (true, subTemplateNames)) 
					{
						subTemplateNames = subTemplate.getSubTemplateList();
					}
					subTemplates.append (subTemplate.getTemplateXST ());
					if (subTemplate.isUseAd ())
					{
						m_isUseAd = true;
						m_adTemplates.append(subTemplate.get_ID () + ",");
					}
					if (subTemplate.isNews ())
						m_isNews = true;
				}
			}
			m_preBuildTemplate.append (subTemplates);
			m_preBuildTemplate = new StringBuffer (m_preBuildTemplate
				.substring (0, m_preBuildTemplate
					.indexOf ("</xsl:stylesheet>"))
				+ subTemplates.toString () + "\n</xsl:stylesheet>");
			// Check whether we need Request functionality for handling
			if (m_preBuildTemplate.indexOf ("/webCM/requestTables/")>=0) 
				m_isRequest = true;
		}
	}	//	getPreBuildTemplate
	
	private boolean m_hasSubtemplates = true;
	private ArrayList<String> m_subTemplates = null;
	
	private ArrayList<String> getSubTemplateList()
	{
		return m_subTemplates;
	}
	
	private boolean containsSubtemplates(boolean refresh, ArrayList<String> existingSubTemplates) 
	{
		if (refresh)
			m_subTemplates = null;
		if (m_subTemplates!=null)
			return m_hasSubtemplates;
		m_subTemplates = new ArrayList<String> ();
		// Procedure to get the Subtemplates as an ArrayList
		if (existingSubTemplates!=null) 
		{
			for (int i=0;i<existingSubTemplates.size ();i++)
			{
				String thisTemplate = existingSubTemplates.get (i);
				m_subTemplates.add (thisTemplate);
			}
			//	m_subTemplates.add(existingSubTemplates.get (i));
		}
		if (getTemplateXST().indexOf ("<xsl:call-template") >= 0)
		{
			int pos = 0;
			while (getTemplateXST().indexOf ("<xsl:call-template", pos) >= 0)
			{
				String thisName = null;
				int beginPos = getTemplateXST().indexOf (
					"<xsl:call-template", pos);
				int endPos = getTemplateXST().indexOf ("/>", beginPos);
				if (getTemplateXST().indexOf (">", beginPos) < endPos)
				{
					endPos = getTemplateXST().indexOf (">", beginPos) + 1;
				}
				String tempTemplate = getTemplateXST().substring (beginPos,
					endPos);
				pos = getTemplateXST().indexOf ("<xsl:call-template", pos)
					+ tempTemplate.length ();
				if (tempTemplate.indexOf ("name=") >= 0)
				{
					thisName = tempTemplate.substring (tempTemplate
						.indexOf ("name=\"") + 6, tempTemplate.indexOf (
						"\"", tempTemplate.indexOf ("name=\"") + 7));
					if (!m_subTemplates.contains (thisName))
						m_subTemplates.add (thisName);
				}
			}
			m_hasSubtemplates = true;
		} else {
			m_hasSubtemplates = false;
		}
		return m_hasSubtemplates;
	}
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		// TODO: We should implement the validation, until then we enforce it
		if (!isValid()) {
			setIsValid(true);
		}
		return true;
	}	//	beforeSave

	/**
     * After Save. Insert - create tree
     * @param newRecord insert
     * @param success save success
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
				"INSERT INTO AD_TreeNodeCMT "
					+ "(AD_Client_ID,AD_Org_ID, IsActive,Created,CreatedBy,Updated,UpdatedBy, "
					+ "AD_Tree_ID, Node_ID, Parent_ID, SeqNo) " 
					+ " VALUES (?,0,'Y',SysDate,0,SysDate,0,?,?,0,999)");
			Object[] params = new Object[]{getAD_Client_ID(),getAD_Tree_ID(),get_ID()};
			int no = DB.executeUpdate (get_Trx (), sb.toString (),params);
			if (no > 0)
				log.fine ("#" + no + " - TreeType=CMT");
			else
				log.warning ("#" + no + " - TreeType=CMT");
			return no > 0;
		}
		if (!newRecord)
		{
			org.compiere.cm.CacheHandler thisHandler = new org.compiere.cm.CacheHandler (
				org.compiere.cm.CacheHandler.convertJNPURLToCacheURL (getCtx ()
					.getContext("java.naming.provider.url")), log, getCtx (),
				get_Trx ());
			if (!isInclude ())
			{
				// Clean Main Templates on a single level.
				thisHandler.cleanTemplate (this.get_ID ());
				// Check the elements in the Stage Area
				MCStage[] theseStages = MCStage.getStagesByTemplate (getWebProject(), get_ID());
				for (MCStage element : theseStages)
					element.checkElements ();
			}
			else
			{
				// Since we not know which main templates we will clean up all!
				thisHandler.emptyTemplate ();
			}
		}
		return success;
	}	// afterSave

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
		StringBuffer sb = new StringBuffer ("DELETE FROM AD_TreeNodeCMT ")
			.append (" WHERE Node_ID= ?")
			.append (" AND AD_Tree_ID=?");
		Object[] params = new Object[]{get_IDOld(),getAD_Tree_ID()};
		int no = DB.executeUpdate (get_Trx (), sb.toString (),params);
		if (no > 0)
			log.fine ("#" + no + " - TreeType=CMT");
		else
			log.warning ("#" + no + " - TreeType=CMT");
		return no > 0;
	}	// afterDelete

	/**
     * Get's all the Ads from Template AD Cat (including all subtemplates)
     * @return Array of MAds
     */
	public MAd[] getAds ()
	{
		int[] AdCats = null;
		String sql = "SELECT count(*) FROM CM_Template_AD_Cat WHERE CM_Template_ID IN (" + m_adTemplates.toString ().substring (0,m_adTemplates.length ()-1) + ")";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			int numberAdCats = 0;
			pstmt = DB.prepareStatement (sql, get_Trx ());
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				numberAdCats = rs.getInt (1);
			}
			rs.close ();
			AdCats = new int[numberAdCats];
			int i = 0;
			sql = "SELECT CM_Ad_Cat_ID FROM CM_Template_AD_Cat WHERE CM_Template_ID IN (" + m_adTemplates.toString ().substring (0,m_adTemplates.length ()-1) + ")";
			pstmt = DB.prepareStatement (sql, get_Trx ());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				AdCats[i] = rs.getInt (1);
				i++;
			}
		}
		catch (SQLException ex)
		{
			log.log (Level.SEVERE, sql, ex);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		if (AdCats != null && AdCats.length > 0)
		{
			MAd[] returnAds = new MAd[AdCats.length];
			for (int i = 0; i < AdCats.length; i++)
			{
				MAd thisAd = MAd.getNext (getCtx (), AdCats[i],
					get_Trx ());
				if (thisAd!=null) 
					returnAds[i] = thisAd;
			}
			return returnAds;
		}
		else
		{
			return null;
		}
	}	//	getAds
	
} // MTemplate
