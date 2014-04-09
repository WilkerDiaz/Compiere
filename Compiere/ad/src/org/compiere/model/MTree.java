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
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.sql.*;
import org.compiere.util.*;

/**
 *  Builds Tree.
 *  Creates tree structure - maintained in VTreePanel
 *
 *  @author     Jorg Janke
 *  @version    $Id: MTree.java 8776 2010-05-19 17:50:56Z nnayak $
 */
public class MTree extends X_AD_Tree
{
    /** Logger for class MTree */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MTree.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**************************************************************************
	 *  Get default (oldest) complete AD_Tree_ID for KeyColumn.
	 *  Called from GridController
	 *  @param AD_Client_ID client
	 *  @param AD_Table_ID table
	 *  @return AD_Tree_ID or 0
	 */
	public static int getDefaultAD_Tree_ID (int AD_Client_ID, int AD_Table_ID)
	{
		s_log.finer("AD_Table_ID=" + AD_Table_ID);
		if (AD_Table_ID == 0)
			return 0;
		int AD_Tree_ID = 0;
		String sql = "SELECT AD_Tree_ID, Name FROM AD_Tree "
			+ "WHERE AD_Client_ID IN(?, 0) AND AD_Table_ID=? AND IsActive='Y' AND IsAllNodes='Y' "
			+ "ORDER BY AD_Client_ID DESC, IsDefault DESC, AD_Tree_ID";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setInt(2, AD_Table_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				AD_Tree_ID = rs.getInt(1);
		}
		catch (SQLException e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return AD_Tree_ID;
	}   //  getDefaultAD_Tree_ID

	/**************************************************************************
	 *  Get default (oldest) complete AD_Tree_ID for KeyColumn.
	 *  Called from GridController
	 *  @param AD_Client_ID client
	 *  @param AD_Table_ID table
	 *  @return AD_Tree_ID or 0
	 */
	public static int getDefaultAD_Tree_ID (int AD_Client_ID, String tableName)
	{
		s_log.finer("TableName=" + tableName);
		if (tableName == null)
			return 0;
		int AD_Tree_ID = 0;
		String sql = "SELECT tr.AD_Tree_ID, tr.Name "
			+ "FROM AD_Tree tr INNER JOIN AD_Table tb ON (tr.AD_Table_ID=tb.AD_Table_ID) "
			+ "WHERE tr.AD_Client_ID=? AND tb.TableName=? AND tr.IsActive='Y' AND tr.IsAllNodes='Y' "
			+ "ORDER BY tr.IsDefault DESC, tr.AD_Tree_ID";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Client_ID);
			pstmt.setString(2, tableName);
			rs = pstmt.executeQuery();
			if (rs.next())
				AD_Tree_ID = rs.getInt(1);
		}
		catch (SQLException e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return AD_Tree_ID;
	}   //  getDefaultAD_Tree_ID

	/**
	 * 	Get Node TableName
	 *	@param treeType tree type
	 *	@return node table name, e.g. AD_TreeNode
	 */
	static String getNodeTableName (String treeType)
	{
		String	nodeTableName = "AD_TreeNode";
		if (TREETYPE_Menu.equals(treeType))
			nodeTableName += "MM";
		else if  (TREETYPE_BPartner.equals(treeType))
			nodeTableName += "BP";
		else if  (TREETYPE_Product.equals(treeType))
			nodeTableName += "PR";
		//
		else if  (TREETYPE_CMContainer.equals(treeType))
			nodeTableName += "CMC";
		else if  (TREETYPE_CMContainerStage.equals(treeType))
			nodeTableName += "CMS";
		else if  (TREETYPE_CMMedia.equals(treeType))
			nodeTableName += "CMM";
		else if  (TREETYPE_CMTemplate.equals(treeType))
			nodeTableName += "CMT";
		//
		else if  (TREETYPE_User1.equals(treeType))
			nodeTableName += "U1";
		else if  (TREETYPE_User2.equals(treeType))
			nodeTableName += "U2";
		else if  (TREETYPE_User3.equals(treeType))
			nodeTableName += "U3";
		else if  (TREETYPE_User4.equals(treeType))
			nodeTableName += "U4";
		return nodeTableName;
	}	//	getNodeTableName

	/**
	 * 	Get Node TableName
	 *	@param AD_Table_ID table
	 *	@return node table name, e.g. AD_TreeNode
	 */
	static public String getNodeTableName (int AD_Table_ID)
	{
		String	nodeTableName = "AD_TreeNode";
		if (X_AD_Menu.Table_ID == AD_Table_ID)
			nodeTableName += "MM";
		else if  (X_C_BPartner.Table_ID == AD_Table_ID)
			nodeTableName += "BP";
		else if  (X_M_Product.Table_ID == AD_Table_ID)
			nodeTableName += "PR";
		//
		else if  (X_CM_Container.Table_ID == AD_Table_ID)
			nodeTableName += "CMC";
		else if  (X_CM_CStage.Table_ID == AD_Table_ID)
			nodeTableName += "CMS";
		else if  (X_CM_Media.Table_ID == AD_Table_ID)
			nodeTableName += "CMM";
		else if  (X_CM_Template.Table_ID == AD_Table_ID)
			nodeTableName += "CMT";
		//
		else
		{
			if (s_TableIDs == null)
				fillUserTables(null);
			Integer ii = Integer.valueOf(AD_Table_ID);
			if (s_TableIDs.contains(ii))
			{
				if  (s_TableIDs_U1.contains(ii))
					nodeTableName += "U1";
				else if (s_TableIDs_U2.contains(ii))
					nodeTableName += "U2";
				else if (s_TableIDs_U3.contains(ii))
					nodeTableName += "U3";
				else if (s_TableIDs_U4.contains(ii))
					nodeTableName += "U4";
			}
			else	//	no tree
				return null;
		}
		return nodeTableName;
	}	//	getNodeTableName

	/**
	 * 	Table has Tree
	 *	@param AD_Table_ID table
	 *	@return true if table has tree
	 */
	static public boolean hasTree (int AD_Table_ID)
	{
		if (s_TableIDs == null)
			fillUserTables(null);
		Integer ii = Integer.valueOf(AD_Table_ID);
		return s_TableIDs.contains(ii);
	}	//	hasTree
	
	/**
	 * 	Table has Tree
	 *	@param tableName table
	 *	@return true if table has tree
	 */
	static public boolean hasTree (String tableName)
	{
		if (s_TableNames == null)
			fillUserTables(null);
		return s_TableNames.contains(tableName);
	}	//	hasTree

	/**
	 * 	Fill User Tables
	 * 	@param trx transaction
	 */
	static synchronized void fillUserTables (Trx trx)
	{
		s_TableNames = new ArrayList<String>();
		s_TableIDs = new ArrayList<Integer>();
		s_TableIDs_U1 = new ArrayList<Integer>();
		s_TableIDs_U2 = new ArrayList<Integer>();
		s_TableIDs_U3 = new ArrayList<Integer>();
		s_TableIDs_U4 = new ArrayList<Integer>();
		//
		boolean error = false;
		//
		String sql = "SELECT DISTINCT TreeType, AD_Table_ID FROM AD_Tree";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				String TreeType = rs.getString(1);
				int AD_Table_ID = rs.getInt(2);
				if (AD_Table_ID == 0)
					continue;
				Integer ii = Integer.valueOf(AD_Table_ID);
				s_TableIDs.add(ii);		//	all
				if (TreeType.equals ("U1"))
					s_TableIDs_U1.add(ii);
				else if (TreeType.equals ("U2"))
					s_TableIDs_U2.add(ii);
				else if (TreeType.equals ("U3"))
					s_TableIDs_U3.add(ii);
				else if (TreeType.equals ("U4"))
					s_TableIDs_U4.add(ii);
			}
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, sql, e);
			error = true;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//	Not updated
		if (!error && s_TableIDs.size() < 3)
		{
			MTree xx = get (Env.getCtx(), 10, trx);
			xx.updateTrees();
			fillUserTables(null);
		}
	}	//	fillUserTables

	/**************************************************************************
	 * 	Get MTree_Base from Cache
	 *	@param ctx context
	 *	@param AD_Tree_ID id
	 *	@param trx transaction
	 *	@return MTree_Base
	 */
	public static MTree get (Ctx ctx, int AD_Tree_ID, Trx trx)
	{
		Integer key = Integer.valueOf (AD_Tree_ID);
		MTree retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MTree (ctx, AD_Tree_ID, trx);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static final CCache<Integer,MTree> s_cache = new CCache<Integer,MTree>("AD_Tree", 10);
	
	/** All Table Names with tree		*/
	private static ArrayList<String> s_TableNames = null;
	/** All Table IDs with tree			*/
	private static ArrayList<Integer> s_TableIDs = null;
	/** U1 Table IDs					*/
	private static ArrayList<Integer> s_TableIDs_U1 = null;
	/** U2 Table IDs					*/
	private static ArrayList<Integer> s_TableIDs_U2 = null;
	/** U3 Table IDs					*/
	private static ArrayList<Integer> s_TableIDs_U3 = null;
	/** U4 Table IDs					*/
	private static ArrayList<Integer> s_TableIDs_U4 = null;

	/**	Logger			*/
	private static CLogger s_log = CLogger.getCLogger(MTree.class);
	
	/**************************************************************************
	 *  Default Constructor.
	 * 	Need to call loadNodes explicitly
	 * 	@param ctx context for security
	 *  @param AD_Tree_ID   The tree to build
	 *  @param trx transaction
	 */
	public MTree (Ctx ctx, int AD_Tree_ID, Trx trx)
	{
		super (ctx, AD_Tree_ID, trx);
		if (AD_Tree_ID == 0)
		{
		//	setName (null);
		//	setTreeType (null);
			setIsAllNodes (true);	//	complete tree
			setIsDefault(false);
		}
	}   //  MTree

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MTree (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MTree_Base

	/**
	 * 	Parent Constructor
	 *	@param client client
	 *	@param name name
	 *	@param treeType
	 */
	public MTree (MClient client, String name, String treeType)
	{
		this (client.getCtx(), 0, client.get_Trx());
		setClientOrg (client);
		setName (name);
		setTreeType (treeType);
		setAD_Table_ID();
	}	//	MTree_Base


	/**
	 * 	Full Constructor
	 *	@param ctx context
	 *	@param Name name
	 *	@param TreeType tree type
	 *	@param trx transaction
	 */
	public MTree (Ctx ctx, String Name, String TreeType,  
		Trx trx)
	{
		this (ctx, 0, trx);
		setName (Name);
		setTreeType (TreeType);
		setAD_Table_ID();
		setIsAllNodes (true);	//	complete tree
		setIsDefault(false);
	}	//	MTree_Base

	/**
	 *  Construct & Load Tree
	 *  @param AD_Tree_ID   The tree to build
	 *  @param editable     True, if tree can be modified
	 *  - includes inactive and empty summary nodes
	 * 	@param ctx context for security
	 *	@param clientTree the tree is displayed on the java client (not on web)
	 *  @param trx transaction
	 */
	public MTree (Ctx ctx, int AD_Tree_ID, 
		boolean editable, boolean clientTree, 
		boolean webUI, Trx trx)
	{
		this (ctx, AD_Tree_ID, editable, clientTree, webUI, trx, true);
	}   //  MTree

	/**
	 *  Construct & Load Tree
	 *  @param AD_Tree_ID   The tree to build
	 *  @param editable     True, if tree can be modified
	 *  - includes inactive and empty summary nodes
	 * 	@param ctx context for security
	 *	@param clientTree the tree is displayed on the java client (not on web)
	 *  @param trx transaction
	 */
	public MTree (Ctx ctx, int AD_Tree_ID, 
		boolean editable, boolean clientTree, 
		boolean webUI, Trx trx, boolean addAccessSQL)
	{
		this (ctx, AD_Tree_ID, trx);
		m_editable = editable;
		int AD_User_ID = ctx.getAD_User_ID();
		m_clientTree = clientTree;
		m_webUI = webUI;
		log.info("AD_Tree_ID=" + AD_Tree_ID
			+ ", AD_User_ID=" + AD_User_ID 
			+ ", Editable=" + editable
			+ ", OnClient=" + clientTree);
		//
		loadNodes(AD_User_ID, addAccessSQL);
	}   //  MTree

	public MTree (Ctx ctx, int AD_Tree_ID, 
			boolean editable, boolean clientTree, 
			Trx trx)
	{
		this (ctx, AD_Tree_ID, editable, clientTree, false, trx);
	}
	
	public MTree (Ctx ctx, int AD_Tree_ID, 
			boolean editable, boolean clientTree, 
			Trx trx, boolean addAccessSQL)
	{
		this (ctx, AD_Tree_ID, editable, clientTree, false, trx, addAccessSQL);
	}
	
	/** Is Tree editable    	*/
	private boolean     		m_editable = false;
	/** Root Node                   */
	private CTreeNode           m_root = null;
	/** Buffer while loading tree   */
	private ArrayList<CTreeNode> m_buffer = new ArrayList<CTreeNode>();
	/** Prepared Statement for Node Details */
	private RowSet			   	m_nodeRowSet;
	/** The tree is displayed on the Java Client (i.e. not web)	*/
	private boolean				m_clientTree = true;
	/** The tree is displayed on GWT UI	*/
	private boolean				m_webUI = false;


	/*************************************************************************
	 *  Load Nodes and Bar
	 * 	@param AD_User_ID user for tree bar
	 */
	private void loadNodes (int AD_User_ID)
	{
		loadNodes (AD_User_ID, true);
	}   //  loadNodes

	/*************************************************************************
	 *  Load Nodes and Bar
	 * 	@param AD_User_ID user for tree bar
	 */
	private void loadNodes (int AD_User_ID, boolean addAccessSQL)
	{
		//  SQL for TreeNodes
		StringBuffer sql = new StringBuffer("SELECT "
			+ "tn.Node_ID,tn.Parent_ID,tn.SeqNo,tb.IsActive "
			+ "FROM ").append(getNodeTableName()).append(" tn"
			+ " LEFT OUTER JOIN AD_TreeBar tb ON (tn.AD_Tree_ID=tb.AD_Tree_ID"
			+ " AND tn.Node_ID=tb.Node_ID AND tb.AD_User_ID=?) "	//	#1
			+ "WHERE tn.AD_Tree_ID=?");								//	#2
		if (!m_editable)
			sql.append(" AND tn.IsActive='Y'");
		sql.append(" ORDER BY COALESCE(tn.Parent_ID, -1), tn.SeqNo");
		log.finest(sql.toString());

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//  The Node Loop
		try
		{
			// load Node details - addToTree -> getNodeDetail
			getNodeDetails(addAccessSQL); 
			//
			pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			pstmt.setInt(1, AD_User_ID);
			pstmt.setInt(2, getAD_Tree_ID());
			//	Get Tree & Bar
			rs = pstmt.executeQuery();
			m_root = new CTreeNode (0, 0, getName(), getDescription(), 0, true, null, false, null);
			while (rs.next())
			{
				int node_ID = rs.getInt(1);
				int parent_ID = rs.getInt(2);
				int seqNo = rs.getInt(3);
				boolean onBar = (rs.getString(4) != null);
				//
				if (node_ID == 0 && parent_ID == 0)
					;
				else
					addToTree (node_ID, parent_ID, seqNo, onBar);	//	calls getNodeDetail
			}
			m_nodeRowSet.close();
			m_nodeRowSet = null;
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
			m_nodeRowSet = null;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
			
		//  Done with loading - add remainder from buffer
		if (m_buffer.size() != 0)
		{
			log.finest("clearing buffer - Adding to: " + m_root);
			for (int i = 0; i < m_buffer.size(); i++)
			{
				CTreeNode node = m_buffer.get(i);
				CTreeNode parent = m_root.findNode(node.getParent_ID());
				if (parent != null && parent.getAllowsChildren())
				{
					parent.add(node);
					checkBuffer(node);
					m_buffer.remove(node);
					i = -1;		//	start again with i=0
				}
			}
		}

		//	Nodes w/o parent
		if (m_buffer.size() != 0)
		{
			log.warning ("Nodes w/o parent - adding to root - " + m_buffer);
			for (int i = 0; i < m_buffer.size(); i++)
			{
				CTreeNode node = m_buffer.get(i);
				m_root.add(node);
				checkBuffer(node);
				m_buffer.remove(node);
				i = -1;
			}
			if (m_buffer.size() != 0)
				log.warning ("Still nodes in Buffer - " + m_buffer);
		}	//	nodes w/o parents

		//  clean up
		if (!m_editable && m_root.getChildCount() > 0)
			trimTree();
//		diagPrintTree();
		if (CLogMgt.isLevelFinest() || m_root.getChildCount() == 0)
			log.fine("ChildCount=" + m_root.getChildCount());
	}   //  loadNodes

	/**
	 *  Add Node to Tree.
	 *  If not found add to buffer
	 *  @param node_ID Node_ID
	 *  @param parent_ID Parent_ID
	 *  @param seqNo SeqNo
	 *  @param onBar on bar
	 */
	private void addToTree (int node_ID, int parent_ID, int seqNo, boolean onBar)
	{
		//  Create new Node
		CTreeNode child = getNodeDetail (node_ID, parent_ID, seqNo, onBar);
		if (child == null)
			return;

		//  Add to Tree
		CTreeNode parent = null;
		if (m_root != null)
			parent = m_root.findNode (parent_ID);
		//  Parent found
		if (parent != null && parent.getAllowsChildren())
		{
			parent.add(child);
			//  see if we can add nodes from buffer
			if (m_buffer.size() > 0)
				checkBuffer(child);
		}
		else
			m_buffer.add(child);
	}   //  addToTree

	/**
	 *  Check the buffer for nodes which have newNode as Parents
	 *  @param newNode new node
	 */
	private void checkBuffer (CTreeNode newNode)
	{
		//	Ability to add nodes
		if (!newNode.isSummary() || !newNode.getAllowsChildren())
			return;
		//
		for (int i = 0; i < m_buffer.size(); i++)
		{
			CTreeNode node = m_buffer.get(i);
			if (node.getParent_ID() == newNode.getNode_ID())
			{
				try
				{
					newNode.add(node);
				}
				catch (Exception e)
				{
					log.severe("Adding " + node.getName() 
						+ " to " + newNode.getName() + ": " + e.getMessage());
				}
				m_buffer.remove(i);
				i--;
			}
		}
	}   //  checkBuffer

	
	
	/**************************************************************************
	 *  Get Node Detail.
	 * 	Loads data into RowSet m_nodeRowSet
	 *  Columns:
	 * 	- ID
	 *  - Name
	 *  - Description
	 *  - IsSummary
	 *  - ImageIndicator
	 * 	- additional for Menu
	 *  Parameter:
	 *  - Node_ID
	 *  The SQL contains security/access control
	 */
	private void getNodeDetails (boolean addAccessSQL)
	{
		//  SQL for Node Info
		StringBuffer sqlNode = new StringBuffer();
		String sourceTable = "t";
		String fromClause = getSourceTableName(false);	//	fully qualified
		String columnNameX = getSourceTableName(true);
		String color = getActionColorName();
		if (getTreeType().equals(TREETYPE_Menu))
		{
			boolean base = Env.isBaseLanguage(getCtx(), "AD_Menu");
			sourceTable = "m";
			if (base)
				sqlNode.append("SELECT m.AD_Menu_ID, m.Name,m.Description,m.IsSummary,m.Action, "
					+ "m.AD_Window_ID, m.AD_Process_ID, m.AD_Form_ID, m.AD_Workflow_ID, m.AD_Task_ID, m.AD_Workbench_ID "
					+ "FROM AD_Menu m");
			else
				sqlNode.append("SELECT m.AD_Menu_ID,  t.Name,t.Description,m.IsSummary,m.Action, "
					+ "m.AD_Window_ID, m.AD_Process_ID, m.AD_Form_ID, m.AD_Workflow_ID, m.AD_Task_ID, m.AD_Workbench_ID "
					+ "FROM AD_Menu m, AD_Menu_Trl t");
			if (!base)
				sqlNode.append(" WHERE m.AD_Menu_ID=t.AD_Menu_ID AND t.AD_Language='")
					.append(Env.getAD_Language(getCtx())).append("'");
			if (!m_editable)
			{
				boolean hasWhere = sqlNode.indexOf(" WHERE ") != -1;
				sqlNode.append(hasWhere ? " AND " : " WHERE ").append("m.IsActive='Y' ");
			}
			//	Do not show Beta
			if (!MClient.get(getCtx()).isUseBetaFunctions())
			{
				boolean hasWhere = sqlNode.indexOf(" WHERE ") != -1;
				sqlNode.append(hasWhere ? " AND " : " WHERE ");
				sqlNode.append("(m.AD_Window_ID IS NULL OR EXISTS (SELECT * FROM AD_Window w WHERE m.AD_Window_ID=w.AD_Window_ID AND w.IsBetaFunctionality='N'))")
					.append(" AND (m.AD_Process_ID IS NULL OR EXISTS (SELECT * FROM AD_Process p WHERE m.AD_Process_ID=p.AD_Process_ID AND p.IsBetaFunctionality='N'))")
					.append(" AND (m.AD_Form_ID IS NULL OR EXISTS (SELECT * FROM AD_Form f WHERE m.AD_Form_ID=f.AD_Form_ID AND f.IsBetaFunctionality='N'))");
			}
			
			if(!m_webUI)
				sqlNode.append(" AND (m.AD_Process_ID IS NULL OR EXISTS (SELECT * FROM AD_Process p WHERE m.AD_Process_ID=p.AD_Process_ID AND p.IsExternal='N' AND p.IsDashboard='N'))");

			//	In R/O Menu - Show only defined Forms
			if (!m_editable)
			{
				boolean hasWhere = sqlNode.indexOf(" WHERE ") != -1;
				sqlNode.append(hasWhere ? " AND " : " WHERE ");
				sqlNode.append("(m.AD_Form_ID IS NULL OR EXISTS (SELECT * FROM AD_Form f WHERE m.AD_Form_ID=f.AD_Form_ID AND ");
				if (m_clientTree && !m_webUI)
					sqlNode.append("f.Classname");
				else if (m_clientTree && m_webUI)
					sqlNode.append("f.WebClassname");
				else
					sqlNode.append("f.JSPURL");
				sqlNode.append(" IS NOT NULL))");
			}
		}
		else
		{
			if (columnNameX == null)
				throw new IllegalArgumentException("Unknown TreeType=" + getTreeType());
			sqlNode.append("SELECT t.").append(columnNameX)
				.append("_ID,t.Name,t.Description,t.IsSummary,").append(color)
				.append(" FROM ").append(fromClause);
			if (!m_editable)
				sqlNode.append(" WHERE t.IsActive='Y'");
		}
		
		String sql = sqlNode.toString();
		if(addAccessSQL)
			sql = MRole.getDefault(getCtx(), false).addAccessSQL(sql, sourceTable,
				MRole.SQL_FULLYQUALIFIED, m_editable);
		log.fine(sql);
		m_nodeRowSet = DB.getRowSet (sql, true);
	}   //  getNodeDetails

	/**
	 *  Get Menu Node Details.
	 *  As SQL contains security access, not all nodes will be found
	 *  @param  node_ID     Key of the record
	 *  @param  parent_ID   Parent ID of the record
	 *  @param  seqNo       Sort index
	 *  @param  onBar       Node also on Shortcut bar
	 *  @return Node
	 */
	private CTreeNode getNodeDetail (int node_ID, int parent_ID, int seqNo, boolean onBar)
	{
		int AD_Window_ID = 0;
        int AD_Process_ID = 0;
        int AD_Form_ID = 0;
        int AD_Workflow_ID = 0;
        int AD_Task_ID = 0;
        int AD_Workbench_ID = 0;
		
		CTreeNode retValue = null;
		try
		{
			m_nodeRowSet.beforeFirst();
			while (m_nodeRowSet.next())
			{
				int node = m_nodeRowSet.getInt(1);				
				if (node_ID != node)	//	search for correct one
					continue;
				//	ID,Name,Description,IsSummary,Action/Color
				int index = 2;				
				String name = m_nodeRowSet.getString(index++); 
				String description = m_nodeRowSet.getString(index++);
				boolean isSummary = "Y".equals(m_nodeRowSet.getString(index++));
				String actionColor = m_nodeRowSet.getString(index++);
				//	Menu only
				if (getTreeType().equals(TREETYPE_Menu) && !isSummary)
				{
					AD_Window_ID = m_nodeRowSet.getInt(index++);
					AD_Process_ID = m_nodeRowSet.getInt(index++);
					AD_Form_ID = m_nodeRowSet.getInt(index++);
					AD_Workflow_ID = m_nodeRowSet.getInt(index++);
					AD_Task_ID = m_nodeRowSet.getInt(index++);
					AD_Workbench_ID = m_nodeRowSet.getInt(index++);
					//
					MRole role = MRole.getDefault(getCtx(), false);
					Boolean access = null;
					if (X_AD_Menu.ACTION_Window.equals(actionColor))
						access = role.getWindowAccess(AD_Window_ID);
					else if (X_AD_Menu.ACTION_Process.equals(actionColor) 
						|| X_AD_Menu.ACTION_Report.equals(actionColor))
						access = role.getProcessAccess(AD_Process_ID);
					else if (X_AD_Menu.ACTION_Form.equals(actionColor))
						access = role.getFormAccess(AD_Form_ID);
					else if (X_AD_Menu.ACTION_WorkFlow.equals(actionColor))
						access = role.getWorkflowAccess(AD_Workflow_ID);
					else if (X_AD_Menu.ACTION_Task.equals(actionColor))
						access = role.getTaskAccess(AD_Task_ID);
				//	else if (X_AD_Menu.ACTION_Workbench.equals(action))
				//		access = role.getWorkbenchAccess(AD_Window_ID);
				//	log.fine("getNodeDetail - " + name + " - " + actionColor + " - " + access);
					//
					if (access != null		//	rw or ro for Role 
						|| m_editable)		//	Menu Window can see all
					{
						retValue = new CTreeNode (node_ID, seqNo,
							name, description, parent_ID, isSummary,
							actionColor, onBar, null);	//	menu has no color
					}
				}
				else	//	always add
				{
					Color color = null;	//	action
					if (actionColor != null && !getTreeType().equals(TREETYPE_Menu))
					{
						MPrintColor printColor = MPrintColor.get(getCtx(), actionColor);
						if (printColor != null)
							color = printColor.getColor();
					}
					//
					retValue = new CTreeNode (node_ID, seqNo,
						name, description, parent_ID, isSummary,
						null, onBar, color);			//	no action
				}
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "", e);
		}
        if (retValue != null) 
        {
            // set CTreeNode ID's
            retValue.setAD_Window_ID(AD_Window_ID);
            retValue.setAD_Process_ID(AD_Process_ID);
            retValue.setAD_Form_ID(AD_Form_ID);
            retValue.setAD_Workflow_ID(AD_Workflow_ID);
            retValue.setAD_Task_ID(AD_Task_ID);
            retValue.setAD_Workbench_ID(AD_Workbench_ID);
        }
        
		return retValue;
	}   //  getNodeDetails

	
	/**************************************************************************
	 *  Trim tree of empty summary nodes
	 */
	public void trimTree()
	{
		boolean needsTrim = m_root != null;
		while (needsTrim)
		{
			needsTrim = false;
			Enumeration<?> en = m_root.preorderEnumeration();
			while (m_root.getChildCount() > 0 && en.hasMoreElements())
			{
				CTreeNode nd = (CTreeNode)en.nextElement();
				if (nd.isSummary() && nd.getChildCount() == 0)
				{
					nd.removeFromParent();
					needsTrim = true;
				}
			}
		}
	}   //  trimTree

	/**
	 *  Diagnostics: Print tree
	 */
	void dumpTree()
	{
		Enumeration<?> en = m_root.preorderEnumeration();
		int count = 0;
		while (en.hasMoreElements())
		{
			StringBuffer sb = new StringBuffer();
			CTreeNode nd = (CTreeNode)en.nextElement();
			for (int i = 0; i < nd.getLevel(); i++)
				sb.append(" ");
			sb.append("ID=").append(nd.getNode_ID())
				.append(", SeqNo=").append(nd.getSeqNo())
				.append(" ").append(nd.getName());
			System.out.println(sb.toString());
			count++;
		}
		System.out.println("Count=" + count);
	}   //  diagPrintTree

	/**
	 *  Get Root node
	 *  @return root
	 */
	public CTreeNode getRoot()
	{
		return m_root;
	}   //  getRoot

	/**
	 * 	Is Menu Tree
	 *	@return true if menu
	 */
	public boolean isMenu()
	{
		return TREETYPE_Menu.equals(getTreeType());
	}	//	isMenu

	/**
	 * 	Is Product Tree
	 *	@return true if product
	 */
	public boolean isProduct()
	{
		return TREETYPE_Product.equals(getTreeType());
	}	//	isProduct
	
	/**
	 * 	Is Business Partner Tree
	 *	@return true if partner
	 */
	public boolean isBPartner()
	{
		return TREETYPE_BPartner.equals(getTreeType());
	}	//	isBPartner
	
	/**
	 *	Get Node TableName
	 *	@return node table name, e.g. AD_TreeNode
	 */
	public String getNodeTableName()
	{
		return getNodeTableName(getTreeType());
	}	//	getNodeTableName
	
	/**
	 * 	Get Source TableName (i.e. where to get the name and color)
	 * 	@param tableNameOnly if false return From clause (alias = t)
	 *	@return source table name, e.g. AD_Org or null
	 */
	public String getSourceTableName (boolean tableNameOnly)
	{
		int AD_Table_ID = getAD_Table_ID();
		String tableName = MTable.getTableName (getCtx(), AD_Table_ID);
		//
		if (tableNameOnly)
			return tableName;
		if ("M_Product".equals(tableName))
			return "M_Product t INNER JOIN M_Product_Category x ON (t.M_Product_Category_ID=x.M_Product_Category_ID)";
		if ("C_BPartner".equals(tableName))
			return "C_BPartner t INNER JOIN C_BP_Group x ON (t.C_BP_Group_ID=x.C_BP_Group_ID)";
		if ("AD_Org".equals(tableName))
			return "AD_Org t INNER JOIN AD_OrgInfo i ON (t.AD_Org_ID=i.AD_Org_ID) "
				+ "LEFT OUTER JOIN AD_OrgType x ON (i.AD_OrgType_ID=x.AD_OrgType_ID)";
		if ("C_Campaign".equals(tableName))
			return "C_Campaign t LEFT OUTER JOIN C_Channel x ON (t.C_Channel_ID=x.C_Channel_ID)";
		if (tableName != null)
			tableName += " t";
		return tableName;
	}	//	getSourceTableName

	
	/**
	 * 	Get fully qualified Name of Action/Color Column
	 *	@return NULL or Action or Color
	 */
	public String getActionColorName()
	{
		int AD_Table_ID = getAD_Table_ID();
		String tableName = MTable.getTableName (getCtx(), AD_Table_ID);
		//
		if ("AD_Menu".equals(tableName))
			return "t.Action";
		if ("M_Product".equals(tableName) || "C_BPartner".equals(tableName) 
			|| "AD_Org".equals(tableName) || "C_Campaign".equals(tableName))
			return "x.AD_PrintColor_ID";
		return "NULL";
	}	//	getSourceTableName

	/**
	 * 	Get AD_Table_ID
	 *	@return table
	 */
	@Override
	public int getAD_Table_ID()
	{
		int AD_Table_ID = super.getAD_Table_ID();
		if (AD_Table_ID == 0)
			AD_Table_ID = setAD_Table_ID();
		return AD_Table_ID;
	}	//	getAD_Table_ID

	/**
	 * 	Get AD_Table_ID
	 * 	@param base base info
	 *	@return table
	 */
	public int getAD_Table_ID(boolean base)
	{
		if (base)
			return super.getAD_Table_ID();
		return getAD_Table_ID();
	}	//	getAD_Table_ID

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (!isActive() || !isAllNodes())
			if (isDefault())
				setIsDefault(false);
		//	Table
		if (getAD_Table_ID(true) == 0)
		{
			if (newRecord)
				setAD_Table_ID();
			else
				updateTrees();
			//
			if (getAD_Table_ID(true) == 0)
			{
				log.warning ("No Table for " + toString());
				return false;
			}
		}
		return validate();
	}	//	beforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		String treeType = getTreeType();
		/**
		if (newRecord)	//	Base Node
		{
			if (TREETYPE_BPartner.equals(treeType))
			{
				MTreeNodeBP ndBP = new MTreeNodeBP(this, 0);
				ndBP.save();
			}
			else if (TREETYPE_Menu.equals(treeType))
			{
				MTreeNodeMM ndMM = new MTreeNodeMM(this, 0);
				ndMM.save();
			}
			else if (TREETYPE_Product.equals(treeType))
			{
				MTreeNodePR ndPR = new MTreeNodePR(this, 0);
				ndPR.save();
			}
			else
			{
				MTreeNode nd = new MTreeNode(this, 0);
				nd.save();
			}
		}
		**/
		if (treeType.startsWith("U"))
			fillUserTables(get_Trx());
		//
		return success;
	}	//	afterSave
	
	/**************************************************************************
	 * 	Set AD_Table_ID from TreeType
	 *	@return AD_Table_ID
	 */
	private int setAD_Table_ID()
	{
		int AD_Table_ID = 0;
		String type = getTreeType();
		if (type == null
			|| type.startsWith ("U")	//	User
			|| type.equals (TREETYPE_Other))
			return 0;
		for (int i = 0; i < TREETYPES.length; i++)
		{
			if (type.equals (TREETYPES[i]))
			{
				AD_Table_ID = TABLEIDS[i];
				break;
			}
		}
		if (AD_Table_ID != 0)
			setAD_Table_ID (AD_Table_ID);
		if (AD_Table_ID == 0)
			log.warning ("Did not find Table for TreeType=" + type);
		return AD_Table_ID;
	}	//	setAD_Table_ID
	
	/**
	 * 	Validate TreeType and AD_Table_ID
	 *	@return true if Tree Type compatible with AD_Table_ID
	 */
	private boolean validate()
	{
		String type = getTreeType();
		if (type != null
				&& (type.startsWith ("U") || type.equals (TREETYPE_Other)))
			return true;
		//
		int AD_Table_ID = getAD_Table_ID(true);
		for (int i = 0; i < TREETYPES.length; i++)
		{
			if (type == null)
			{
				if (AD_Table_ID == TABLEIDS[i])
				{
					setTreeType (TREETYPES[i]);
					return true;
				}
			}
			else if (AD_Table_ID == TABLEIDS[i])
			{
				if (type.equals(TREETYPES[i]))
					return true;
				else
				{
					setTreeType (TREETYPES[i]);
					return true;
				}
			}
			else if (AD_Table_ID == 0 && type.equals(TREETYPES[i]))
			{
				setAD_Table_ID(TABLEIDS[i]);
				return true;
			}
		}
		//	None found
		if (type == null)
		{
			setTreeType (TREETYPE_Other);
			return true;
		}
		log.warning ("TreeType=" + type + " <> AD_Table_ID=" + AD_Table_ID);
		setTreeType (TREETYPE_Other);
		return false;
	}	//	validate
	
	/** Tree Type Array		*/
	private static final String[]	TREETYPES = new String[] {
		TREETYPE_Activity,
		TREETYPE_BoM,
		TREETYPE_BPartner,
		TREETYPE_CMContainer,
		TREETYPE_CMMedia,
		TREETYPE_CMContainerStage,
		TREETYPE_CMTemplate,
		TREETYPE_ElementValue,
		TREETYPE_Campaign,
		TREETYPE_Menu,
		TREETYPE_Organization,
		TREETYPE_ProductCategory,
		TREETYPE_Project,
		TREETYPE_Product,
		TREETYPE_SalesRegion,
		TREETYPE_User1,
		TREETYPE_User2,
		TREETYPE_User3,
		TREETYPE_User4,
		TREETYPE_Other
	};
	/** Table ID Array				*/
	private static final int[]		TABLEIDS = new int[] {
		X_C_Activity.Table_ID,
		X_M_BOM.Table_ID,
		X_C_BPartner.Table_ID,
		X_CM_Container.Table_ID,
		X_CM_Media.Table_ID,
		X_CM_CStage.Table_ID,
		X_CM_Template.Table_ID,
		X_C_ElementValue.Table_ID,
		X_C_Campaign.Table_ID,
		X_AD_Menu.Table_ID,
		X_AD_Org.Table_ID,
		X_M_Product_Category.Table_ID,
		X_C_Project.Table_ID,
		X_M_Product.Table_ID,
		X_C_SalesRegion.Table_ID,
		0,0,0,0,0
	};
	
	/**
	 * 	Update all Trees with Table_ID
	 */
	public void updateTrees()
	{
		setAD_Table_ID();
		for (int i = 0; i < TREETYPES.length; i++)
		{
			if (!updateTrees (TREETYPES[i], TABLEIDS[i]))
				break;
		}
	}	//	updateTrees

	/**
	 * 	Update Trees
	 *	@param treeType tree type
	 *	@param AD_Table_ID table
	 *	@return true if no error
	 */
	private boolean updateTrees(String treeType, int AD_Table_ID)
	{
		if (AD_Table_ID == 0)
			return true;
		StringBuffer sb = new StringBuffer("UPDATE AD_Tree SET AD_Table_ID=")
			.append (AD_Table_ID)
			.append (" WHERE TreeType='").append (treeType).append ("' AND AD_Table_ID IS NULL");
		int no = DB.executeUpdate(get_Trx(), sb.toString());
		log.fine (treeType + " #" + no);
		return no >= 0;
	}	//	updateTrees


	/**
	 *  String representation
	 *  @return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MTree[");
		sb.append (get_ID ()).append ("-")
			.append(getName())
			.append(",Type=").append(getTreeType())
			.append(",AD_Table_ID=").append(getAD_Table_ID(true))
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}   //  MTree
