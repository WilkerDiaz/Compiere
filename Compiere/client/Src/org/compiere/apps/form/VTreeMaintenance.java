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
package org.compiere.apps.form;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.sql.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.event.*;

import org.compiere.grid.tree.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;
import org.compiere.vos.*;

/**
 *	Tree Maintenance
 *	
 *  @author Jorg Janke
 *  @version $Id: VTreeMaintenance.java,v 1.3 2006/07/30 00:51:28 jjanke Exp $
 */
public class VTreeMaintenance extends CPanel
	implements FormPanel, ActionListener, ListSelectionListener, PropertyChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**	Window No				*/
	private int         	m_WindowNo = 0;
	/**	FormFrame				*/
	private FormFrame 		m_frame;
	/**	Active Tree				*/
	private MTree		 	m_tree;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VTreeMaintenance.class);
	
	
	private BorderLayout	mainLayout	= new BorderLayout ();
	private CPanel 			northPanel	= new CPanel ();
	private FlowLayout		northLayout	= new FlowLayout ();
	private CLabel			treeLabel	= new CLabel ();
	private CComboBox		treeField;
	private CButton			bAddAll		= new CButton (Env.getImageIcon("FastBack24.gif"));
	private CButton			bAdd		= new CButton (Env.getImageIcon("StepBack24.gif"));
	private CButton			bDelete		= new CButton (Env.getImageIcon("StepForward24.gif"));
	private CButton			bDeleteAll	= new CButton (Env.getImageIcon("FastForward24.gif"));
	private CCheckBox		cbAllNodes	= new CCheckBox ();
	private CLabel			treeInfo	= new CLabel ();
	//
	private JSplitPane		splitPane	= new JSplitPane ();
	private VTreePanel		centerTree;
	private JList			centerList	= new JList ();

	
	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info( "VMerge.init - WinNo=" + m_WindowNo);
		try
		{
			preInit();
			jbInit ();
			frame.getContentPane().add(this, BorderLayout.CENTER);
		//	frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
			action_loadTree();
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "VTreeMaintenance.init", ex);
		}
	}	//	init
	
	/**
	 * 	Fill Tree Combo
	 */
	private void preInit()
	{
		KeyNamePair[] trees = DB.getKeyNamePairs(MRole.getDefault().addAccessSQL(
			"SELECT AD_Tree_ID, Name FROM AD_Tree WHERE TreeType NOT IN ('BB','PC') ORDER BY 2", 
			"AD_Tree", MRole.SQL_NOTQUALIFIED, MRole.SQL_RW), false);
		treeField = new CComboBox(trees);
		treeField.addActionListener(this);
		//
		centerTree = new VTreePanel (m_WindowNo, false, true);
		centerTree.addPropertyChangeListener(VTreePanel.NODE_SELECTION, this);
	}	//	preInit
	
	/**
	 * 	Static init
	 *	@throws Exception
	 */
	private void jbInit () throws Exception
	{
		this.setLayout (mainLayout);
		treeLabel.setText (Msg.translate(Env.getCtx(), "AD_Tree_ID"));
		cbAllNodes.setEnabled (false);
		cbAllNodes.setText (Msg.translate(Env.getCtx(), "IsAllNodes"));
		treeInfo.setText (" ");
		bAdd.setToolTipText("Add to Tree");
		bAddAll.setToolTipText("Add ALL to Tree");
		bDelete.setToolTipText("Delete from Tree");
		bDeleteAll.setToolTipText("Delete ALL from Tree");
		bAdd.addActionListener(this);
		bAddAll.addActionListener(this);
		bDelete.addActionListener(this);
		bDeleteAll.addActionListener(this);
		northPanel.setLayout (northLayout);
		northLayout.setAlignment (FlowLayout.LEFT);
		//
		this.add (northPanel, BorderLayout.NORTH);
		northPanel.add (treeLabel, null);
		northPanel.add (treeField, null);
		northPanel.add (cbAllNodes, null);
		northPanel.add (treeInfo, null);
		northPanel.add (bAddAll, null);
		northPanel.add (bAdd, null);
		northPanel.add (bDelete, null);
		northPanel.add (bDeleteAll, null);
		//
		this.add (splitPane, BorderLayout.CENTER);
		splitPane.add (centerTree, JSplitPane.LEFT);
		splitPane.add (new JScrollPane(centerList), JSplitPane.RIGHT);
		centerList.setSelectionMode (ListSelectionModel.SINGLE_SELECTION);
		centerList.addListSelectionListener(this);
	}	//	jbInit

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose

	/**
	 * 	Action Listener
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (e.getSource() == treeField)
			action_loadTree();
		else if (e.getSource() == bAddAll)
			action_treeAddAll();
		else if (e.getSource() == bAdd)
			action_treeAdd((TreeNodeVO)centerList.getSelectedValue());
		else if (e.getSource() == bDelete)
			action_treeDelete((TreeNodeVO)centerList.getSelectedValue());
		else if (e.getSource() == bDeleteAll)
			action_treeDeleteAll();
	}	//	actionPerformed

	
	/**
	 * 	Action: Fill Tree with all nodes
	 */
	private void action_loadTree()
	{
		KeyNamePair tree = (KeyNamePair)treeField.getSelectedItem();
		log.info("Tree=" + tree);
		if (tree.getKey() <= 0)
		{
			centerList.setModel(new DefaultListModel());
			return;
		}
		//	Tree
		m_tree = new MTree (Env.getCtx(), tree.getKey(), null);
		cbAllNodes.setSelected(m_tree.isAllNodes());
		bAddAll.setEnabled(!m_tree.isAllNodes());
		bAdd.setEnabled(!m_tree.isAllNodes());
		bDelete.setEnabled(!m_tree.isAllNodes());
		bDeleteAll.setEnabled(!m_tree.isAllNodes());
		//
		String fromClause = m_tree.getSourceTableName(false);	//	fully qualified
		String columnNameX = m_tree.getSourceTableName(true);
		String actionColor = m_tree.getActionColorName();
		//	List
		DefaultListModel model = new DefaultListModel();
		String sql = "SELECT t." + columnNameX 
			+ "_ID,t.Name,t.Description,t.IsSummary,"
			+ actionColor
			+ " FROM " + fromClause
		//	+ " WHERE t.IsActive='Y'"	//	R/O
			+ " ORDER BY 2";
		sql = MRole.getDefault().addAccessSQL(sql, 
			"t", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		log.config(sql);
		//	
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				TreeNodeVO item = new TreeNodeVO(rs.getInt(1), rs.getString(2),
					rs.getString(3), "Y".equals(rs.getString(4)), rs.getString(5));
				model.addElement(item);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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
		//	List
		log.config("#" + model.getSize());
		centerList.setModel(model);
		//	Tree
		centerTree.initTree(m_tree.getAD_Tree_ID());
	}	//	action_fillTree
	
	/**
	 * 	List Selection Listener
	 *	@param e event
	 */
	public void valueChanged (ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting())
			return;
		TreeNodeVO selected = null;
		try
		{	//	throws a ArrayIndexOutOfBoundsException if root is selected
			selected = (TreeNodeVO)centerList.getSelectedValue();
		}
		catch (Exception ex)
		{
		}
		log.info("Selected=" + selected);
		if (selected != null)	//	allow add if not in tree
			bAdd.setEnabled(!centerTree.setSelectedNode(selected.id));
	}	//	valueChanged
	
	/**
	 * 	VTreePanel Changed
	 *	@param e event
	 */
	public void propertyChange (PropertyChangeEvent e)
	{
		CTreeNode tn = (CTreeNode)e.getNewValue();
		log.info(tn.toString());
		if (tn == null)
			return;
		ListModel model = centerList.getModel();
		int size = model.getSize();
		int index = -1;
		for (index = 0; index < size; index++)
		{
			TreeNodeVO item = (TreeNodeVO)model.getElementAt(index);
			if (item.id == tn.getNode_ID())
				break;
		}
		centerList.setSelectedIndex(index);
	}	//	propertyChange

	private void action_treeAdd(TreeNodeVO item)
	{
		log.info("Item=" + item);
		if (item != null)
		{
			centerTree.nodeChanged(true, item.id, item.name, 
					item.description, item.isSummary, item.imageIndicator);
			action_treeAdd(m_tree, item.id);
		}
	}
	/**
	 * 	Action: Add Node to Tree
	 * 	@param item item
	 */
	public static boolean action_treeAdd(MTree tree, int Node_ID)
	{
		//	May cause Error if in tree
		if (tree.isProduct())
		{
			MTreeNodePR node = new MTreeNodePR (tree, Node_ID);
			return node.save();
		}
		else if (tree.isBPartner())
		{
			MTreeNodeBP node = new MTreeNodeBP (tree, Node_ID);
			return node.save();
		}
		else if (tree.isMenu())
		{
			MTreeNodeMM node = new MTreeNodeMM (tree, Node_ID);
			return node.save();
		}
		else
		{
			MTreeNode node = new MTreeNode (tree, Node_ID);
			return node.save();
		}
	}	//	action_treeAdd
	
	private void action_treeDelete(TreeNodeVO item)
	{
		log.info("Item=" + item);
		if (item != null)
		{
			centerTree.nodeChanged(false, item.id, item.name, 
				item.description, item.isSummary, item.imageIndicator);
			action_treeDelete(m_tree, item.id);
		}
	}

	/**
	 * 	Action: Delete Node from Tree
	 * 	@param item item
	 */
	public static boolean action_treeDelete(MTree tree, int Node_ID)
	{
		if (tree.isProduct())
		{
			MTreeNodePR node = MTreeNodePR.get (tree, Node_ID);
			if (node != null)
				return node.delete(true);
		}
		else if (tree.isBPartner())
		{
			MTreeNodeBP node = MTreeNodeBP.get (tree, Node_ID);
			if (node != null)
				return node.delete(true);
		}
		else if (tree.isMenu())
		{
			MTreeNodeMM node = MTreeNodeMM.get (tree, Node_ID);
			if (node != null)
				return node.delete(true);
		}
		else
		{
			MTreeNode node = MTreeNode.get (tree, Node_ID);
			if (node != null)
				return node.delete(true);
		}
		return false;
	}	//	action_treeDelete

	
	/**
	 * 	Action: Add All Nodes to Tree
	 */
	private void action_treeAddAll()
	{
		log.info("");
		ListModel model = centerList.getModel();
		int size = model.getSize();
		int index = -1;
		for (index = 0; index < size; index++)
		{
			TreeNodeVO item = (TreeNodeVO)model.getElementAt(index);
			action_treeAdd(item);
		}
	}	//	action_treeAddAll
	
	/**
	 * 	Action: Delete All Nodes from Tree
	 */
	private void action_treeDeleteAll()
	{
		log.info("");
		ListModel model = centerList.getModel();
		int size = model.getSize();
		int index = -1;
		for (index = 0; index < size; index++)
		{
			TreeNodeVO item = (TreeNodeVO)model.getElementAt(index);
			action_treeDelete(item);
		}
	}	//	action_treeDeleteAll
	
}	//	VTreeMaintenance
