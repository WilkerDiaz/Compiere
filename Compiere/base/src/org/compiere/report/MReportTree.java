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
package org.compiere.report;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Report Tree Model
 *
 *  @author Jorg Janke
 *  @version $Id: MReportTree.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MReportTree
{
    /** Logger for class MReportTree */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MReportTree.class);
	/**
	 * 	Get Report Tree (cached)
	 *	@param ctx context
	 *	@param PA_Hierarchy_ID optional hierarchy
	 *	@param ElementType Account Schema Element Type
	 *	@return tree
	 */
	public static MReportTree get (Ctx ctx, int PA_Hierarchy_ID, String ElementType)
	{
		return get(ctx, PA_Hierarchy_ID, ElementType, true);
	}	//	get
	
	/**
	 * 	Get Report Tree (cached)
	 *	@param ctx context
	 *	@param PA_Hierarchy_ID optional hierarchy
	 *	@param ElementType Account Schema Element Type
	 *	@return tree
	 */
	public static MReportTree get (Ctx ctx, int PA_Hierarchy_ID, String ElementType, boolean addAccessSQL)
	{
		String key = PA_Hierarchy_ID + ElementType;
		MReportTree tree = s_trees.get(ctx, key);
		if (tree == null)
		{
			tree = new MReportTree (ctx, PA_Hierarchy_ID, ElementType, addAccessSQL);
			s_trees.put(key, tree);
		}
		return tree;
	}	//	get
	
	/**
	 * 	Get Where Clause
	 *	@param ctx context
	 *	@param PA_Hierarchy_ID optional hierarchy
	 *	@param ElementType Account Schema Element Type
	 *	@param ID leaf element id
	 *	@return where clause
	 */
	public static String getWhereClause (Ctx ctx,
		int PA_Hierarchy_ID, String ElementType, int ID)
	{
		MReportTree tree = get (ctx, PA_Hierarchy_ID, ElementType);
		return tree.getWhereClause(ID);
	}	//	getWhereClause

	/**
	 * 	Get Child IDs
	 *	@param ctx context
	 *	@param PA_Hierarchy_ID optional hierarchie
	 *	@param ElementType Account Schema Element Type
	 *	@param ID id
	 *	@return array of IDs
	 */
	public static Integer[] getChildIDs (Ctx ctx,
		int PA_Hierarchy_ID, String ElementType, int ID)
	{
		return getChildIDs(ctx, PA_Hierarchy_ID, ElementType, ID, true);
	}	//	getChildIDs

	public static Integer[] getChildIDs (Ctx ctx,
			int PA_Hierarchy_ID, String ElementType, int ID, boolean addAccessSQL)
	{
			MReportTree tree = get (ctx, PA_Hierarchy_ID, ElementType, addAccessSQL);
			return tree.getChildIDs(ID);
	}	//	getChildIDs

	/**	Map with Tree				*/
	private static final CCache<String,MReportTree> s_trees
		= new CCache<String,MReportTree>("MReportTree", 20);


	/**************************************************************************
	 * 	Report Tree
	 *	@param ctx context
	 *	@param PA_Hierarchy_ID optional hierarchy
	 *	@param ElementType Account Schema Element Type
	 */
	public MReportTree (Ctx ctx, int PA_Hierarchy_ID, String ElementType)
	{
		this (ctx, PA_Hierarchy_ID, ElementType, true);
	}	//	MReportTree

	/**************************************************************************
	 * 	Report Tree
	 *	@param ctx context
	 *	@param PA_Hierarchy_ID optional hierarchy
	 *	@param ElementType Account Schema Element Type
	 */
	public MReportTree (Ctx ctx, int PA_Hierarchy_ID, String ElementType, boolean addAccessSQL)
	{
		m_ElementType = ElementType;
		m_TreeType = m_ElementType;
		if (X_C_AcctSchema_Element.ELEMENTTYPE_Account.equals(m_ElementType)
			|| X_C_AcctSchema_Element.ELEMENTTYPE_UserList1.equals(m_ElementType)
			|| X_C_AcctSchema_Element.ELEMENTTYPE_UserList2.equals(m_ElementType) )
			m_TreeType = X_AD_Tree.TREETYPE_ElementValue;
		m_PA_Hierarchy_ID = PA_Hierarchy_ID;
		m_ctx = ctx;
		//
		int AD_Tree_ID = getAD_Tree_ID();
		//	Not found
		if (AD_Tree_ID == 0)
			throw new IllegalArgumentException("No AD_Tree_ID for TreeType=" + m_TreeType
				+ ", PA_Hierarchy_ID=" + PA_Hierarchy_ID);
		//
		boolean clientTree = true;
		m_tree = new MTree (ctx, AD_Tree_ID, true, clientTree, null, addAccessSQL);
	}	//	MReportTree

	/** Optional Hierarchy		*/
	private int			m_PA_Hierarchy_ID = 0;
	/**	Element Type			*/
	private String		m_ElementType = null;
	/** Context					*/
	private Ctx			m_ctx = null;
	/** Tree Type				*/
	private String		m_TreeType = null;
	/**	The Tree				*/
	private MTree 		m_tree = null;


	/**
	 * 	Get AD_Tree_ID
	 *	@return tree
	 */
	protected int getAD_Tree_ID ()
	{
		if (m_PA_Hierarchy_ID == 0)
			return getDefaultAD_Tree_ID();

		MHierarchy hierarchy = MHierarchy.get(m_ctx, m_PA_Hierarchy_ID);
		int AD_Tree_ID = hierarchy.getAD_Tree_ID (m_TreeType);

		if (AD_Tree_ID == 0)
			return getDefaultAD_Tree_ID();

		return AD_Tree_ID;
	}	//	getAD_Tree_ID

	/**
	 * 	Get Default AD_Tree_ID
	 * 	see MTree.getDefaultAD_Tree_ID
	 *	@return tree
	 */
	protected int getDefaultAD_Tree_ID()
	{
		int AD_Tree_ID = 0;
		int AD_Client_ID = m_ctx.getAD_Client_ID();

		String sql = "SELECT AD_Tree_ID, Name FROM AD_Tree "
			+ "WHERE AD_Client_ID=? AND TreeType=? AND IsActive='Y' AND IsAllNodes='Y' "
			+ "ORDER BY ASCII(IsDefault) DESC, AD_Tree_ID";	//	assumes first is primary tree
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setString(2, m_TreeType);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				AD_Tree_ID = rs.getInt(1);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}

		return AD_Tree_ID;
	}	//	getDefaultAD_Tree_ID

	/**
	 * 	Get Account Schema Element Type
	 *	@return element Type
	 */
	public String getElementType()
	{
		return m_ElementType;
	}	//	getElementType

	/**
	 * 	Get Tree Type
	 *	@return tree type
	 */
	public String getTreeType()
	{
		return m_TreeType;
	}	//	getTreeType

	/**
	 * 	Get Tree
	 *	@return tree
	 */
	public MTree getTree()
	{
		return m_tree;
	}	//	getTreeType

	/**
	 * 	Get Where Clause
	 *	@param ID start node
	 *	@return ColumnName = 1 or ColumnName IN (1,2,3)
	 */
	public String getWhereClause (int ID)
	{
		log.fine("(" + m_ElementType + ") ID=" + ID);
		String ColumnName = MAcctSchemaElement.getColumnName(m_ElementType);
		if (ID == 0)	//	All
			return ColumnName + " IS NOT NULL";
		//
		CTreeNode node = m_tree.getRoot().findNode(ID);
		log.finest("Root=" + node);
		//
		StringBuffer result = null;
		if ((node != null) && node.isSummary())
		{
			StringBuffer sb = new StringBuffer();
			Enumeration<?> en = node.preorderEnumeration();
			while (en.hasMoreElements())
			{
				CTreeNode nn = (CTreeNode)en.nextElement();
				if (!nn.isSummary())
				{
					if (sb.length () > 0)
						sb.append (",");
					sb.append(nn.getNode_ID());
					log.finest("- " + nn);
				}
				else
					log.finest("- skipped parent (" + nn + ")");
			}
			if (sb.length () > 0)
			{
				result = new StringBuffer (ColumnName).append(" IN (").append(sb).append(")");
			}

		}
		else	//	not found or not summary
		{
			result = new StringBuffer (ColumnName).append("=").append(ID);
		}

	if(result == null)
	{
		return "";
	}
	else
		return (result.toString());
	//log.finest(result.toString());


}//	getWhereClause

	/**
	 * 	Get Child IDs
	 *	@param ID start node
	 *	@return array if IDs
	 */
	public Integer[] getChildIDs (int ID)
	{
		log.fine("(" + m_ElementType + ") ID=" + ID);
		ArrayList<Integer> list = new ArrayList<Integer>();
		//
		CTreeNode node = m_tree.getRoot().findNode(ID);
		log.finest("Root=" + node);
		//
		if ((node != null) && node.isSummary())
		{
			Enumeration<?> en = node.preorderEnumeration();
			while (en.hasMoreElements())
			{
				CTreeNode nn = (CTreeNode)en.nextElement();
				if (!nn.isSummary())
				{
					list.add(Integer.valueOf(nn.getNode_ID()));
					log.finest("- " + nn);
				}
				else
					log.finest("- skipped parent (" + nn + ")");
			}
		}
		else	//	not found or not summary
			list.add(Integer.valueOf(ID));
		//
		Integer[] retValue = new Integer[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getWhereClause


	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MReportTree[ElementType=");
		sb.append(m_ElementType).append(",TreeType=").append(m_TreeType)
			.append(",").append(m_tree)
			.append("]");
		return sb.toString();
	}	//	toString

}	//	MReportTree
