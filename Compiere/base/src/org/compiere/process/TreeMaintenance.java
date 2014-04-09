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

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.common.CompiereStateException;
import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Tree Maintenance	
 *	
 *  @author Jorg Janke
 *  @version $Id: TreeMaintenance.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class TreeMaintenance extends SvrProcess
{
	/**	Tree				*/
	private int		m_AD_Tree_ID;
	
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
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		m_AD_Tree_ID = getRecord_ID();		//	from Window
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("AD_Tree_ID=" + m_AD_Tree_ID);
		if (m_AD_Tree_ID == 0)
			throw new IllegalArgumentException("Tree_ID = 0");
		MTree tree = new MTree (getCtx(), m_AD_Tree_ID, get_TrxName());	
		if (tree == null || tree.getAD_Tree_ID() == 0)
			throw new IllegalArgumentException("No Tree -" + tree);
		//
		if (X_AD_Tree.TREETYPE_BoM.equals(tree.getTreeType()))
			return "BOM Trees not implemented";
		return verifyTree(tree);
	}	//	doIt

	/**
	 *  Verify Tree
	 * 	@param tree tree
	 */
	private String verifyTree (MTree tree)
	{
		if (tree.getAD_Table_ID(true) == 0)
			tree.updateTrees();
		String nodeTableName = tree.getNodeTableName();
		String sourceTableName = tree.getSourceTableName(true);
		String sourceTableKey = sourceTableName + "_ID";
		int AD_Client_ID = tree.getAD_Client_ID();
		int C_Element_ID = 0;
		if (X_AD_Tree.TREETYPE_ElementValue.equals(tree.getTreeType()))
		{
			String sql = "SELECT C_Element_ID FROM C_Element "
				+ "WHERE AD_Tree_ID= ? " ;
			C_Element_ID = QueryUtil.getSQLValue(null, sql,tree.getAD_Client_ID());
			if (C_Element_ID <= 0)
				throw new CompiereStateException("No Account Element found");
		}
		
		//	Delete unused
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM ").append(nodeTableName)
			.append(" WHERE AD_Tree_ID= ? ")
			.append(" AND Node_ID NOT IN (SELECT ").append(sourceTableKey)
			.append(" FROM ").append(sourceTableName)
			.append(" WHERE AD_Client_ID IN (0, ? )");
		if (C_Element_ID > 0)
			sql.append(" AND C_Element_ID= ? )");
		log.finer(sql.toString());
		//
		int deletes = 0;
		if(C_Element_ID  >0 )
			deletes = DB.executeUpdate(get_TrxName(), sql.toString(),
					tree.getAD_Client_ID(),AD_Client_ID,C_Element_ID);
		else
			deletes = DB.executeUpdate(get_TrxName(), sql.toString(),
					tree.getAD_Client_ID(),AD_Client_ID);
			
		addLog(0,null, new BigDecimal(deletes), tree.getName()+ " Deleted");
		if (!tree.isAllNodes())
			return tree.getName() + " OK";
		
		//	Insert new
		int inserts = 0;
		sql = new StringBuffer();
		sql.append("SELECT ").append(sourceTableKey)
			.append(" FROM ").append(sourceTableName)
			.append(" WHERE AD_Client_ID IN (0,?)");
		if (C_Element_ID > 0)
			sql.append(" AND C_Element_ID= ? ");
		sql.append(" AND ").append(sourceTableKey)
			.append("  NOT IN (SELECT Node_ID FROM ").append(nodeTableName)
			.append(" WHERE AD_Tree_ID= ? )");
		log.finer(sql.toString());
		//
		boolean ok = true;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int index=1;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			pstmt.setInt(index++, AD_Client_ID);
			if (C_Element_ID > 0)
				pstmt.setInt(index++, C_Element_ID);
			pstmt.setInt(index++, tree.getAD_Tree_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				int Node_ID = rs.getInt(1);
				PO node = null;
				if (nodeTableName.equals("AD_TreeNode"))
					node = new MTreeNode(tree, Node_ID);
				else if (nodeTableName.equals("AD_TreeNodeBP"))
					node = new MTreeNodeBP(tree, Node_ID);
				else if (nodeTableName.equals("AD_TreeNodePR"))
					node = new MTreeNodePR(tree, Node_ID);
				else if (nodeTableName.equals("AD_TreeNodeCMC"))
					node = new MTreeNodeCMC(tree, Node_ID);
				else if (nodeTableName.equals("AD_TreeNodeCMM"))
					node = new MTreeNodeCMM(tree, Node_ID);
				else if (nodeTableName.equals("AD_TreeNodeCMS"))
					node = new MTreeNodeCMS(tree, Node_ID);
				else if (nodeTableName.equals("AD_TreeNodeCMT"))
					node = new MTreeNodeCMT(tree, Node_ID);
				else if (nodeTableName.equals("AD_TreeNodeMM"))
					node = new MTreeNodeMM(tree, Node_ID);
				//				
				if (node == null)
					log.log(Level.SEVERE, "No Model for " + nodeTableName);
				else
				{
					if (node.save())
						inserts++;
					else
						log.log(Level.SEVERE, "Could not add to " + tree + " Node_ID=" + Node_ID);
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
			ok = false;
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		addLog(0,null, new BigDecimal(inserts), tree.getName()+ " Inserted");
		return tree.getName() + (ok ? " OK" : " Error");
	}	//	verifyTree

}	//	TreeMaintenence
