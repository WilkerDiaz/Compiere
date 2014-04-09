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
package org.compiere.cm;

import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.*;

/**
 * 	Deploy Web Project
 *	
 *  @author Jorg Janke
 *  @version $Id: WebProjectDeploy.java,v 1.10 2006/09/04 21:21:31 comdivision Exp $
 */
public class WebProjectDeploy extends SvrProcess
{
	/**	WebProject					*/
	private int		p_CM_WebProject_ID = 0;
	/** Full Redeploy				*/
	private boolean p_isRedeploy = false;
	
	/** Project						*/
	private MWebProject 				m_project = null;
	/**	Stage Hash Map				*/
	private HashMap<Integer, MCStage> 	m_map = new HashMap<Integer, MCStage>();
	/** List of IDs					*/
	private ArrayList <Integer>			m_idList = new ArrayList<Integer>();
	
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("CM_WebProject_ID"))
				p_CM_WebProject_ID = element.getParameterAsInt();
			else if (name.equals("ReDeploy"))
				if (element.getParameter().toString ().equals ("Y"))
					p_isRedeploy = true;
				else 
					p_isRedeploy = false;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	@Override
	protected String doIt ()
		throws Exception
	{
		org.compiere.cm.CacheHandler thisHandler = new org.compiere.cm.CacheHandler
		(org.compiere.cm.CacheHandler.convertJNPURLToCacheURL
			(getCtx().getContext("java.naming.provider.url")), log, getCtx(), get_TrxName());
		
		log.info("CM_WebProject_ID=" + p_CM_WebProject_ID);
		m_project = new MWebProject(getCtx(), p_CM_WebProject_ID, get_TrxName());
		if (m_project.get_ID() != p_CM_WebProject_ID)
			throw new CompiereUserException("@NotFound@ @CM_WebProject_ID@ " + p_CM_WebProject_ID);
		
		log.log (Level.INFO, "Starting media deployment");
		//	Deploy Media
		MMediaServer[] mserver = MMediaServer.getMediaServer(m_project);
			
		for (MMediaServer element : mserver) {
			log.log (Level.INFO, "Media Server deployment started on: " + mserver.toString ());
			if (p_isRedeploy)
				element.reDeployAll ();
			element.deploy();
			log.log (Level.INFO, "Media Server deployment finished on: " + mserver.toString ());
		}
		
		//	Stage
		MCStage[] stages = MCStage.getStages(m_project);
		for (MCStage element : stages)
			m_map.put(Integer.valueOf(element.getCM_CStage_ID()), element);
		
		//	Copy Stage Tree
		MTree treeS = new MTree (getCtx(), m_project.getAD_TreeCMS_ID(), false, false, get_TrxName());
		CTreeNode root = treeS.getRoot();
		copyStage(root, "/", p_isRedeploy);
		
		//	Delete Inactive Containers
		MContainer[] containers = MContainer.getContainers(m_project);
		for (MContainer container : containers) {
			if (!m_idList.contains(Integer.valueOf(container.getCM_Container_ID())))
			{
				String name = container.getName();
				if (container.delete(true))
					log.fine("Deleted: " + name);
				else	//	e.g. was referenced
				{
					log.warning("Failed Delete: " + name);
					addLog(0,null,null, "@Error@ @Delete@: " + name);
				}
			}
			// Remove Container from cache
			thisHandler.cleanContainer(container.get_ID());
		}	//	Delete Inactive

		//	Sync Stage & Container Tree
		MTreeNodeCMS nodesCMS[] = MTreeNodeCMS.getTree(getCtx(), m_project.getAD_TreeCMS_ID(), get_TrxName());
		MTreeNodeCMC nodesCMC[] = MTreeNodeCMC.getTree(getCtx(), m_project.getAD_TreeCMC_ID(), get_TrxName());
		for (MTreeNodeCMS nodeCMS : nodesCMS) {
			int Node_ID = nodeCMS.getNode_ID();
			for (MTreeNodeCMC nodeCMC : nodesCMC) {
				if (nodeCMC.getNode_ID() == Node_ID)
				{
					//if (nodeCMS.getParent_ID()!=0) 
						nodeCMC.setParent_ID(nodeCMS.getParent_ID());
					nodeCMC.setSeqNo(nodeCMS.getSeqNo());
					nodeCMC.save();
					break;
				}
			}
		}	//	for all stage nodes
		// Clean ContainerTree Cache
		thisHandler.cleanContainerTree (p_CM_WebProject_ID);
	
		return "@Copied@ @CM_Container_ID@ #" + m_idList.size();
	}	//	doIt
	
	
	/**
	 * 	Copy Stage
	 *	@param node node 
	 *	@param path path
	 */
	private void copyStage (CTreeNode node, String path, boolean isRedeploy)
	{
		org.compiere.cm.CacheHandler thisHandler = new org.compiere.cm.CacheHandler
		(org.compiere.cm.CacheHandler.convertJNPURLToCacheURL
			(getCtx().getContext("java.naming.provider.url")), log, getCtx(), get_TrxName());
		Integer ID = Integer.valueOf(node.getNode_ID());
		MCStage stage = m_map.get(ID);
		//	
		int size = node.getChildCount();
		for (int i = 0; i < size; i++)
		{
			CTreeNode child = (CTreeNode)node.getChildAt(i);
			ID = Integer.valueOf(child.getNode_ID());
			stage = m_map.get(ID);
			if (stage == null)
			{
				log.warning("Not Found ID=" + ID);
				continue;
			}
			if (!stage.isActive())
				continue;
			// If we have a stage and it is modified we will update!
			if (stage != null)
			{
				if (isRedeploy || stage.isModified () || stage.isSummary ())
				{
					log.log (Level.INFO, "Deploying container: " + path + stage.toString ());
					MContainer cc = MContainer.deploy (m_project, stage, path);
					if (cc != null)
					{
						addLog (0, null, null, "@Updated@: " + cc.getName());
						m_idList.add(ID);
					}
					// Remove Container from cache
					thisHandler.cleanContainer(cc.get_ID());
					// Reset Modified flag...
					stage.setIsModified(false);
					stage.save(stage.get_Trx());
				} else {
					// If not modified we should check update status...
					// But even if updtodate we need to add it to the list, because otherwise it will get deleted!
					m_idList.add (ID);
				}
			}
			if (child.isSummary())
				copyStage (child, path + stage.getRelativeURL() + "/", isRedeploy);
		}
	}	//	copyStage
	
}	//	WebProjectDeploy
