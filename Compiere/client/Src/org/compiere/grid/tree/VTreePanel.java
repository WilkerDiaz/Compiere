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
package org.compiere.grid.tree;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.compiere.apps.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  Tree Panel displays trees.
 *  <br>
 *	When a node is selected, a propertyChange (NODE_SELECTION) event is fired
 *  <pre>
 *		PropertyChangeListener -
 *			treePanel.addPropertyChangeListener(VTreePanel.NODE_SELECTION, this);
 *			calls: public void propertyChange(PropertyChangeEvent e)
 *  </pre>
 *  To select a specific node call
 *      setSelectedNode(NodeID);
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: VTreePanel.java,v 1.3 2006/07/30 00:51:28 jjanke Exp $
 */
public final class VTreePanel extends CPanel
	implements ActionListener, DragGestureListener, DragSourceListener, DropTargetListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  Tree Panel for browsing and editing of a tree.
	 *  Need to call initTree
	 *  @param  WindowNo	WindowNo
	 *  @param  editable    if true you can edit it
	 *  @param  hasBar      has OutlookBar
	 */
	public VTreePanel(int WindowNo, boolean hasBar, boolean editable)
	{
		super();
		log.config("Bar=" + hasBar + ", Editable=" + editable);
		m_WindowNo = WindowNo;
		m_hasBar = hasBar;
		m_editable = editable;

		//	static init
		jbInit();
		if (!hasBar)
		{
			bar.setPreferredSize(new Dimension(0,0));
			centerSplitPane.setDividerLocation(0);
			centerSplitPane.setDividerSize(0);
			popMenuTree.remove(mBarAdd);
		}
		else
			centerSplitPane.setDividerLocation(80);
		//  base settings
		if (editable)
			tree.setDropTarget(dropTarget);
		else
		{
			popMenuTree.remove(mFrom);
			popMenuTree.remove(mTo);
		}
	}   //  VTreePanel

	/**
	 *  Tree initialization.
	 * 	May be called several times
	 *	@param	AD_Tree_ID	tree to load
	 *  @return true if loaded ok
	 */
	public boolean initTree (int AD_Tree_ID)
	{
		log.config("AD_Tree_ID=" + AD_Tree_ID);
		//
		if(AD_Tree_ID == m_AD_Tree_ID)
			return false;
			
		m_AD_Tree_ID = AD_Tree_ID;

		//  Get Tree
		MTree vTree = new MTree (Env.getCtx(), AD_Tree_ID, m_editable, true, null);
		m_root = vTree.getRoot();
		log.config("root=" + m_root);
		m_nodeTableName = vTree.getNodeTableName();
		treeModel = new DefaultTreeModel(m_root, true);
		tree.setModel(treeModel);

		//  Shortcut Bar
		if (m_hasBar)
		{
			bar.removeAll();	//	remove all existing buttons
			Enumeration<?> en = m_root.preorderEnumeration();
			while (en.hasMoreElements())
			{
				CTreeNode nd = (CTreeNode)en.nextElement();
				if (nd.isOnBar())
					addToBar(nd);
			}
		}
        
        tree.addTreeWillExpandListener(new TreeWillExpandListener()
        {
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException
            {
                if (event.getPath().getPathCount() == 1)
                    throw new ExpandVetoException(event);
            }

            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException
            {
            }
        });

		return true;
	}   //  initTree

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VTreePanel.class);

	private BorderLayout mainLayout = new BorderLayout();
	private JTree tree = new JTree();
	private DefaultTreeModel treeModel;
	private DefaultTreeSelectionModel treeSelect = new DefaultTreeSelectionModel();
	private CTextField treeSearch = new CTextField(10);
	private CLabel treeSearchLabel = new CLabel();
	private JPopupMenu popMenuTree = new JPopupMenu();
	private JPopupMenu popMenuBar = new JPopupMenu();
	private CMenuItem mFrom = new CMenuItem();
	private CMenuItem mTo = new CMenuItem();
	private CPanel bar = new CPanel();
	private CMenuItem mBarAdd = new CMenuItem();
	private CMenuItem mBarRemove = new CMenuItem();
	private JSplitPane centerSplitPane = new JSplitPane();
	private JScrollPane treePane = new JScrollPane();
	private MouseListener mouseListener = new VTreePanel_mouseAdapter(this);
	private KeyListener keyListener = new VTreePanel_keyAdapter(this);

	//
	private int			m_WindowNo;
	/** Tree ID                     */
	private int			m_AD_Tree_ID = 0;
	/** Table Name for TreeNode     */
	private String      m_nodeTableName = null;
	/** Tree is editable (can move nodes) - also not active shown   */
	private boolean     m_editable;
	/** Tree has a shortcut Bar     */
	private boolean     m_hasBar;
	/** The root node               */
	private CTreeNode  	m_root = null;


	private CTreeNode   m_moveNode;    	//	the node to move
	private String      m_search = "";
	private Enumeration<?> m_nodeEn;
	private CTreeNode   m_selectedNode;	//	the selected model node
	private CButton     m_buttonSelected;

	/**	Property Listener NodeSelected			*/
	public static final String NODE_SELECTION = "NodeSelected";

	/**
	 *  Static Component initialization.
	 *  <pre>
	 *  - centerSplitPane
	 *      - treePane
	 *          - tree
	 *      - bar
	 *  - southPanel
	 *  </pre>
	 */
	private void jbInit()
	{
		this.setLayout(mainLayout);
		mainLayout.setVgap(5);
		//
		//  only one node to be selected
		treeSelect.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setSelectionModel(treeSelect);
		//
		tree.setEditable(false);		            //	allows to change the text
		tree.addMouseListener(mouseListener);
		tree.addKeyListener(keyListener);
		tree.setCellRenderer(new VTreeCellRenderer());
		treePane.getViewport().add(tree, null);

        URL url = VTreePanel.class.getResource("/org/compiere/images/ExpandAll16.gif");
        CButton expandButton = new CButton(new ImageIcon(url));
        Dimension d = expandButton.getPreferredSize();
        d.width = d.height;
        expandButton.setPreferredSize(d);
        expandButton.setToolTipText(Msg.getMsg(Env.getCtx(), "ExpandTree"));
        final HashSet<TreePath> paths = new HashSet<TreePath>();
        expandButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                for (int i = 1; i < tree.getRowCount(); i++)
                {
                    if (tree.isCollapsed(i))
                        paths.add(tree.getPathForRow(i));
                }
                
                for (TreePath path : paths)
                {
                    tree.expandPath(path);
                }
            }
        });
        
        url = VTreePanel.class.getResource("/org/compiere/images/CollapseAll16.gif");
        CButton collapseButton = new CButton(new ImageIcon(url));
        collapseButton.setPreferredSize(d);
        collapseButton.setToolTipText(Msg.getMsg(Env.getCtx(), "CollapseTree"));
        collapseButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                paths.clear();
                for (int i = 1; i < tree.getRowCount(); i++)
                {
                    if (!tree.isExpanded(i))
                        paths.add(tree.getPathForRow(i).getParentPath());
                }
                
                for (TreePath path : paths)
                {
                    tree.collapsePath(path);
                }
            }
        });
        
		//
		treeSearchLabel.setText(Msg.getMsg(Env.getCtx(), "TreeSearch") + " ");
		treeSearchLabel.setLabelFor(treeSearch);
		treeSearchLabel.setToolTipText(Msg.getMsg(Env.getCtx(), "TreeSearchText"));

		treeSearch.setBackground(CompierePLAF.getInfoBackground());
		treeSearch.addKeyListener(keyListener);

        CPanel northPanel = new CPanel(new BorderLayout());
        northPanel.add(treeSearchLabel, BorderLayout.WEST);
        treeSearchLabel.setVerticalAlignment(SwingConstants.CENTER);
        CPanel panel = new CPanel(new GridBagLayout());
        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.anchor = GridBagConstraints.LINE_START;
        gbConstraints.insets = new Insets(2, 0, 0, 0);
        gbConstraints.weightx = 1;
        panel.add(treeSearch, gbConstraints);
        panel.addComponentListener(new ComponentAdapter()
        {
            private Dimension prefSize = treeSearch.getPreferredSize();
            private Dimension newSize = new Dimension(prefSize);
            @Override
			public void componentResized(ComponentEvent e)
            {
                if (treeSearch.isShowing())
                {
                    newSize.width = (prefSize.width > e.getComponent().getWidth()) ?
                        e.getComponent().getWidth() : prefSize.width;
                    treeSearch.setSize(newSize);
                    e.getComponent().invalidate();
                    e.getComponent().repaint();
                }
            }
        });
        northPanel.add(panel);
        panel = new CPanel(new GridBagLayout());
        gbConstraints.anchor = GridBagConstraints.CENTER;
        gbConstraints.weightx = 0;
        gbConstraints.gridx = 0;
        gbConstraints.insets = new Insets(2, 2, 0, 1);
        panel.add(expandButton, gbConstraints);
        gbConstraints.insets = new Insets(2, 1, 0, 0);
        gbConstraints.gridx = 1;
        panel.add(collapseButton, gbConstraints);
        northPanel.add(panel, BorderLayout.EAST);

        CPanel menuPanel = new CPanel(new BorderLayout());
        menuPanel.add(northPanel, BorderLayout.SOUTH);
        menuPanel.add(treePane);
        
		centerSplitPane.add(menuPanel, JSplitPane.RIGHT);
		centerSplitPane.add(bar, JSplitPane.LEFT);
		this.add(centerSplitPane, BorderLayout.CENTER);
		//
		mFrom.setText(Msg.getMsg(Env.getCtx(), "ItemMove"));
		mFrom.setActionCommand("From");
		mFrom.addActionListener(this);
		mTo.setEnabled(false);
		mTo.setText(Msg.getMsg(Env.getCtx(), "ItemInsert"));
		mTo.setActionCommand("To");
		mTo.addActionListener(this);
		//
		bar.setLayout(new BoxLayout(bar, BoxLayout.Y_AXIS));
		bar.setMinimumSize(new Dimension (50,50));

		mBarAdd.setText(Msg.getMsg(Env.getCtx(), "BarAdd"));
		mBarAdd.setActionCommand("BarAdd");
		mBarAdd.addActionListener(this);
		mBarRemove.setText(Msg.getMsg(Env.getCtx(), "BarRemove"));
		mBarRemove.setActionCommand("BarRemove");
		mBarRemove.addActionListener(this);
		//
		popMenuTree.setLightWeightPopupEnabled(false);
		popMenuTree.add(mBarAdd);
		popMenuTree.addSeparator();
		popMenuTree.add(mFrom);
		popMenuTree.add(mTo);
		popMenuBar.setLightWeightPopupEnabled(false);
		popMenuBar.add(mBarRemove);
	}   //  jbInit


	/**
	 * 	Set Divider Location
	 *	@param location location (80 default)
	 */
	public void setDividerLocation(int location)
	{
		centerSplitPane.setDividerLocation(location);
	}	//	setDividerLocation
	
	/**
	 * 	Get Divider Location
	 *	@return divider location
	 */
	public int getDividerLocation()
	{
		return centerSplitPane.getDividerLocation();
	}	//	getDividerLocation
	
	
	/*************************************************************************
	 *	Drag & Drop
	 */
	protected DragSource dragSource
		= DragSource.getDefaultDragSource();
	protected DropTarget dropTarget
		= new DropTarget(tree, DnDConstants.ACTION_MOVE, this, true, null);
	protected DragGestureRecognizer recognizer
		= dragSource.createDefaultDragGestureRecognizer(tree, DnDConstants.ACTION_MOVE, this);


	/**
	 *	Drag Gesture Interface	** Start **
	 *  @param e event
	 */
	public void dragGestureRecognized(DragGestureEvent e)
	{
		if (!m_editable)
			return;
		//
		try
		{
			m_moveNode = (CTreeNode)tree.getSelectionPath().getLastPathComponent();
		}
		catch (Exception ex)	//	nothing selected
		{
			return;
		}
		//	start moving
		StringSelection content = new StringSelection(m_moveNode.toString());
		e.startDrag(DragSource.DefaultMoveDrop,		//	cursor
					content,						//	Transferable
					this);
		log.fine( "Drag: " + m_moveNode.toString());
	}	//	dragGestureRecognized


	/**
	 *	DragSourceListener interface - noop
	 *  @param e event
	 */
	public void dragDropEnd(DragSourceDropEvent e)	{}
	/**
	 *	DragSourceListener interface - noop
	 *  @param e event
	 */
	public void dragEnter(DragSourceDragEvent e)	{}
	/**
	 *	DragSourceListener interface - noop
	 *  @param e event
	 */
	public void dragExit(DragSourceEvent e)	{}
	/**
	 *	DragSourceListener interface - noop
	 *  @param e event
	 */
	public void dragOver(DragSourceDragEvent e)	{}
	/**
	 *	DragSourceListener interface - noop
	 *  @param e event
	 */
	public void dropActionChanged(DragSourceDragEvent e)	{}

	/**
	 *	DropTargetListener interface
	 *  @param e event
	 */
	public void dragEnter(DropTargetDragEvent e)
	{
		e.acceptDrag(DnDConstants.ACTION_MOVE);
	}
	/**
	 *	DragSourceListener interface - noop
	 *  @param e event
	 */
	public void dropActionChanged(DropTargetDragEvent e)	{}
	/**
	 *	DragSourceListener interface - noop
	 *  @param e event
	 */
	public void dragExit(DropTargetEvent e)	{}


	/**
	 *	Drag over 				** Between **
	 *  @param e event
	 */
	public void dragOver(DropTargetDragEvent e)
	{
		Point mouseLoc = e.getLocation(); 	//	where are we?
		TreePath path = tree.getClosestPathForLocation(mouseLoc.x, mouseLoc.y);
		tree.setSelectionPath(path);		//	show it by selecting
		CTreeNode toNode = (CTreeNode)path.getLastPathComponent();
		//
	//	log.fine( "Move: " + toNode);
		if (m_moveNode == null				//	nothing to move
			||	toNode == null)				//	nothing to drop on
			e.rejectDrag();
		else
			e.acceptDrag(DnDConstants.ACTION_MOVE);
		
		Rectangle bounds = tree.getBounds();
		if(mouseLoc.y < -bounds.y + 20){
			Rectangle rec = new Rectangle(0, -bounds.y - 5, 1, 1);
			tree.scrollRectToVisible(rec);
		} else if(mouseLoc.y > -bounds.y + tree.getVisibleRect().height - 20){
			Rectangle rec = new Rectangle(0, -bounds.y + tree.getVisibleRect().height + 5, 1, 1);
			tree.scrollRectToVisible(rec);
		} 
	}	//	dragOver


	/**
	 *	Drop					** End **
	 *  @param e event
	 */
	public void drop(DropTargetDropEvent e)
	{
		Point mouseLoc = e.getLocation(); 	//	where are we?
		TreePath path = tree.getClosestPathForLocation(mouseLoc.x, mouseLoc.y);
		tree.setSelectionPath(path);		//	show it by selecting
		CTreeNode toNode = (CTreeNode)path.getLastPathComponent();
		//
		log.fine( "Drop: " + toNode);
		if (m_moveNode == null				//	nothing to move
			||	toNode == null)				//	nothing to drop on
		{
			e.rejectDrop();
			return;
		}
		//
		e.acceptDrop(DnDConstants.ACTION_MOVE);
		moveNode(m_moveNode, toNode);

		e.dropComplete(true);
		m_moveNode = null;
	}	//	drop


	/**
	 *	Move TreeNode
	 *	@param	movingNode	The node to be moved
	 *	@param	toNode		The target node
	 */
	private void moveNode(CTreeNode movingNode, CTreeNode toNode)
	{
		log.info(movingNode.toString() + " to " + toNode.toString());

		if( toNode.isNodeAncestor(movingNode) )
			return;

		//  remove
		CTreeNode oldParent = (CTreeNode)movingNode.getParent();
		int childIndex = oldParent.getIndex(movingNode);
		oldParent.remove(movingNode);
		treeModel.nodesWereRemoved(oldParent, new int[] { childIndex }, new CTreeNode[] { movingNode });
		// treeModel.nodeStructureChanged(oldParent);

		//  insert
		CTreeNode newParent;
		int index;
		if (!toNode.isSummary())	//	drop on a child node
		{
			newParent = (CTreeNode)toNode.getParent();
			index = newParent.getIndex(toNode) + 1;	//	the next node
		}
		else									//	drop on a summary node
		{
			newParent = toNode;
			index = 0;                   			//	the first node
		}
		newParent.insert(movingNode, index);
		treeModel.nodesWereInserted(newParent, new int[] { index });
		// treeModel.nodeStructureChanged(newParent);

		//	***	Save changes to disk
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		Trx p_trx = Trx.get ("VTreePanel");
		try
		{
			Statement stmt = p_trx.getConnection().createStatement();
			//	START TRANSACTION   **************
			for (int i = 0; i < oldParent.getChildCount(); i++)
			{
				CTreeNode nd = (CTreeNode)oldParent.getChildAt(i);
				StringBuffer sql = new StringBuffer("UPDATE ");
				sql.append(m_nodeTableName)
					.append(" SET Parent_ID=").append(oldParent.getNode_ID())
					.append(", SeqNo=").append(i)
					.append(", Updated=SysDate")
					.append(" WHERE AD_Tree_ID=").append(m_AD_Tree_ID)
					.append(" AND Node_ID=").append(nd.getNode_ID());
				log.fine(sql.toString());
				stmt.executeUpdate(sql.toString());
			}
			if (oldParent != newParent)
				for (int i = 0; i < newParent.getChildCount(); i++)
				{
					CTreeNode nd = (CTreeNode)newParent.getChildAt(i);
					StringBuffer sql = new StringBuffer("UPDATE ");
					sql.append(m_nodeTableName)
						.append(" SET Parent_ID=").append(newParent.getNode_ID())
						.append(", SeqNo=").append(i)
						.append(", Updated=SysDate")
						.append(" WHERE AD_Tree_ID=").append(m_AD_Tree_ID)
						.append(" AND Node_ID=").append(nd.getNode_ID());
					log.fine(sql.toString());
					stmt.executeUpdate(sql.toString());
				}
			//	COMMIT          *********************
			p_trx.commit();
			stmt.close();
		}
		catch (SQLException e)
		{
			p_trx.rollback();
			log.log(Level.SEVERE, "move", e);
			ADialog.error(m_WindowNo, this, "TreeUpdateError", e.getLocalizedMessage());
		}
		p_trx.close();
		p_trx = null;
		setCursor(Cursor.getDefaultCursor());
		log.config("complete");
	}	//	moveNode


	/*************************************************************************/

	/**
	 *  Enter Key
	 *  @param e event
	 */
	protected void keyPressed(KeyEvent e)
	{
		//  *** Tree ***
		if (e.getSource() instanceof JTree
			|| (e.getSource() == treeSearch && e.getModifiers() != 0))	//	InputEvent.CTRL_MASK
		{
			TreePath tp = tree.getSelectionPath();
			if (tp == null)
				ADialog.beep();
			else
			{
				CTreeNode tn = (CTreeNode)tp.getLastPathComponent();
				setSelectedNode(tn);
			}
		}

		//  *** treeSearch ***
		else if (e.getSource() == treeSearch)
		{
			String search = treeSearch.getText();
			boolean found = false;

			//  at the end - try from top
			if (m_nodeEn != null && !m_nodeEn.hasMoreElements())
				m_search = "";

			//  this is the first time
			if (!search.equals(m_search))
			{
				//  get enumeration of all nodes
				m_nodeEn = m_root.preorderEnumeration();
				m_search = search;
			}

			//  search the nodes
			while(!found && m_nodeEn != null && m_nodeEn.hasMoreElements())
			{
				CTreeNode nd = (CTreeNode)m_nodeEn.nextElement();
				//	compare in upper case
				if (nd.toString().toUpperCase().indexOf(search.toUpperCase()) != -1)
				{
					found = true;
					TreePath treePath = new TreePath(nd.getPath());
					tree.setSelectionPath(treePath);
					tree.makeVisible(treePath);			//	expand it
					tree.scrollPathToVisible(treePath);
				}
			}
			if (!found)
				ADialog.beep();
		}   //  treeSearch

	}   //  keyPressed


	/*************************************************************************/

	/**
	 *  Mouse clicked
	 *  @param e event
	 */
	protected void mouseClicked(MouseEvent e)
	{
		//  *** JTree ***
		if (e.getSource() instanceof JTree)
		{
			//  Left Double Click
			if (SwingUtilities.isLeftMouseButton(e)
				&& e.getClickCount() > 0)
			{
				int selRow = tree.getRowForLocation(e.getX(), e.getY());
				if(selRow != -1)
				{
					CTreeNode tn = (CTreeNode)tree.getPathForLocation
						(e.getX(), e.getY()).getLastPathComponent();
					setSelectedNode(tn);
				}
			}

			//  Right Click for PopUp
			else if ((m_editable || m_hasBar)
				&& SwingUtilities.isRightMouseButton(e)
				&& tree.getSelectionPath() != null)         //  need select first
			{
				Rectangle r = tree.getPathBounds(tree.getSelectionPath());
				popMenuTree.show(tree, (int)r.getMaxX(), (int)r.getY());
			}
		}   //  JTree

		//  *** JButton ***
		else if (e.getSource() instanceof JButton)
		{
			if (SwingUtilities.isRightMouseButton(e))
			{
				m_buttonSelected = (CButton)e.getSource();
				popMenuBar.show(m_buttonSelected, e.getX(), e.getY());
			}
		}   //  JButton

	}   //  mouseClicked


	/**
	 *  Get currently selected node
	 *  @return CTreeNode
	 */
	public CTreeNode getSelectedNode()
	{
		return m_selectedNode;
	}   //  getSelectedNode

	/**
	 *  Search Field
	 *  @return Search Field
	 */
	public JComponent getSearchField()
	{
		return treeSearch;
	}   //  getSearchField

	/**
	 *  Set Selection to Node in Event
	 *  @param nodeID Node ID
	 * 	@return true if selected
	 */
	public boolean setSelectedNode (int nodeID)
	{
		log.config("ID=" + nodeID);
		if (nodeID != -1)				//	new is -1
			return selectID(nodeID, true);     //  show selection
		return false;
	}   //  setSelectedNode

	/**
	 *  Select ID in Tree
	 *  @param nodeID	Node ID
	 *  @param show	scroll to node
	 * 	@return true if selected
	 */
	private boolean selectID (int nodeID, boolean show)
	{
		if (m_root == null)
			return false;
		log.config("NodeID=" + nodeID 
			+ ", Show=" + show + ", root=" + m_root);
		//  try to find the node
		CTreeNode node = m_root.findNode (nodeID);
		if (node != null)
		{
			TreePath treePath = new TreePath(node.getPath());
			log.config("Node=" + node 
				+ ", Path=" + treePath.toString());
			tree.setSelectionPath(treePath);
			if (show)
			{
				tree.makeVisible(treePath);       	//	expand it
				tree.scrollPathToVisible(treePath);
			}
			return true;
		}
		log.info("Node not found; ID=" + nodeID);
		return false;
	}   //  selectID


	/**
	 *  Set the selected node & initiate all listeners
	 *  @param nd node
	 */
	private void setSelectedNode (CTreeNode nd)
	{
		log.config("Node = " + nd);
		m_selectedNode = nd;
		//
		firePropertyChange(NODE_SELECTION, null, nd);
	}   //  setSelectedNode

	
	/**************************************************************************
	 *  Node Changed - synchromize Node
	 *
	 *  @param  save    true the node was saved (changed/added), false if the row was deleted
	 *  @param  keyID   the ID of the row changed
	 *  @param  name	name
	 *  @param  description	description
	 *  @param  isSummary	summary node
	 *  @param  imageIndicator image indicator
	 */
	public void nodeChanged (boolean save, int keyID,
		String name, String description, boolean isSummary, String imageIndicator)
	{
		log.config("Save=" + save + ", KeyID=" + keyID
			+ ", Name=" + name + ", Description=" + description 
			+ ", IsSummary=" + isSummary + ", ImageInd=" + imageIndicator
			+ ", root=" + m_root);
		//	if ID==0=root - don't update it
		if (keyID == 0)
			return;	
			
		//  try to find the node
		CTreeNode node = m_root.findNode(keyID);

		//  Node not found and saved -> new
		if (node == null && save)
		{
			node = new CTreeNode (keyID, 0, name, description,
				m_root.getNode_ID(), isSummary, imageIndicator, false, null);
			m_root.add (node);
		}

		//  Node found and saved -> change
		else if (node != null && save)
		{
			node.setName (name);
			node.setAllowsChildren(isSummary);
		}

		//  Node found and not saved -> delete
		else if (node != null && !save)
		{
			CTreeNode parent = (CTreeNode)node.getParent();
			node.removeFromParent();
			node = parent;  //  select Parent
		}

		//  Error
		else
		{
			log.log(Level.SEVERE, "Save=" + save + ", KeyID=" + keyID + ", Node=" + node);
			node = null;
		}

		//  Nothing to display
		if (node == null)
			return;

		//  (Re) Display Node
		tree.updateUI();
		TreePath treePath = new TreePath(node.getPath());
		tree.setSelectionPath(treePath);
		tree.makeVisible(treePath);       	//	expand it
		tree.scrollPathToVisible(treePath);
	}   //  nodeChanged


	/**************************************************************************
	 *  ActionListener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		//  bar button pressed
		if (e.getSource() instanceof JButton)
		{
			//  Find Node - don't show
			selectID(Integer.parseInt(e.getActionCommand()), false);
			//  Select it
			CTreeNode tn = (CTreeNode)tree.getSelectionPath().getLastPathComponent();
			setSelectedNode(tn);
		}

		//  popup menu commands
		else if (e.getSource() instanceof JMenuItem)
		{
			if (e.getActionCommand().equals("From"))
				moveFrom();
			else if (e.getActionCommand().equals("To"))
				moveTo();
			else if (e.getActionCommand().equals("BarAdd"))
				barAdd();
			else if (e.getActionCommand().equals("BarRemove"))
				barRemove();
		}
	}   //  actionPerformed


	/*************************************************************************/

	/**
	 *  Copy Node into buffer
	 */
	private void moveFrom()
	{
		m_moveNode = (CTreeNode)tree.getSelectionPath().getLastPathComponent();
		if (m_moveNode != null)
			mTo.setEnabled(true);		//	enable menu
	}   //  mFrom_actionPerformed

	/**
	 *  Move Node
	 */
	private void moveTo()
	{
		mFrom.setEnabled(true);
		mTo.setEnabled(false);
		if (m_moveNode == null)
			return;

		CTreeNode toNode = (CTreeNode)tree.getSelectionPath().getLastPathComponent();
		moveNode(m_moveNode, toNode);
		//	cleanup
		m_moveNode = null;
	}   //  mTo_actionPerformed

	/**
	 *  Add selected TreeNode to Bar
	 */
	private void barAdd()
	{
		CTreeNode nd = (CTreeNode)tree.getSelectionPath().getLastPathComponent();
		if (barDBupdate(true, nd.getNode_ID()))
			addToBar(nd);
	}   //  barAdd

	/**
	 *  Add TreeNode to Bar
	 *  @param nd node
	 */
	private void addToBar(CTreeNode nd)
	{
		//	Only first word of Label
		String label = nd.toString().trim();

		CButton button = new CButton(label);		//	Create the button
		button.setToolTipText(nd.getDescription());
		button.setActionCommand(String.valueOf(nd.getNode_ID()));
		//
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setIcon(nd.getIcon());
		button.setBorderPainted(false);

		button.setRequestFocusEnabled(false);
		//
		button.addActionListener(this);
		button.addMouseListener(mouseListener);
		//
		bar.add(button);
		bar.validate();
		if (centerSplitPane.getDividerLocation() == -1)
			centerSplitPane.setDividerLocation(button.getPreferredSize().width);
		bar.repaint();
	}   //  addToBar

	/**
	 *  Remove from Bar
	 */
	private void barRemove()
	{
		bar.remove(m_buttonSelected);
		bar.validate();
		bar.repaint();
		barDBupdate(false, Integer.parseInt(m_buttonSelected.getActionCommand()));
	}   //  barRemove

	/**
	 *	Make Bar add/remove persistent
	 *  @param add true if add - otherwise remove
	 *  @param Node_ID Node ID
	 *  @return true if updated
	 */
	private boolean barDBupdate (boolean add, int Node_ID)
	{
		int AD_Client_ID = Env.getCtx().getAD_Client_ID();
		int AD_Org_ID = Env.getCtx().getAD_Org_ID();
		int AD_User_ID = Env.getCtx().getAD_User_ID();
		StringBuffer sql = new StringBuffer();
		if (add)
			sql.append("INSERT INTO AD_TreeBar "
				+ "(AD_Tree_ID,AD_User_ID,Node_ID, "
				+ "AD_Client_ID,AD_Org_ID, "
				+ "IsActive,Created,CreatedBy,Updated,UpdatedBy)VALUES (")
				.append(m_AD_Tree_ID).append(",").append(AD_User_ID).append(",").append(Node_ID).append(",")
				.append(AD_Client_ID).append(",").append(AD_Org_ID).append(",")
				.append("'Y',SysDate,").append(AD_User_ID).append(",SysDate,").append(AD_User_ID).append(")");
			//	if already exist, will result in ORA-00001: unique constraint (COMPIERE.AD_TREEBAR_KEY)
		else
			sql.append("DELETE FROM AD_TreeBar WHERE AD_Tree_ID=").append(m_AD_Tree_ID)
				.append(" AND AD_User_ID=").append(AD_User_ID)
				.append(" AND Node_ID=").append(Node_ID);
		int no = DB.executeUpdateIgnoreError(null, sql.toString());
		return no == 1;
	}	//	barDBupdate

}   //  VTreePanel


/******************************************************************************
 *  Mouse Clicked
 */
class VTreePanel_mouseAdapter extends java.awt.event.MouseAdapter
{
	VTreePanel m_adaptee;

	/**
	 * 	VTreePanel_mouseAdapter
	 *	@param adaptee
	 */
	VTreePanel_mouseAdapter(VTreePanel adaptee)
	{
		m_adaptee = adaptee;
	}

	/**
	 *	Mouse Clicked
	 *	@param e
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		m_adaptee.mouseClicked(e);
	}
}   //  VTreePanel_mouseAdapter

/**
 *  Key Pressed
 */
class VTreePanel_keyAdapter extends java.awt.event.KeyAdapter
{
	VTreePanel m_adaptee;

	/**
	 * 	VTreePanel_keyAdapter
	 *	@param adaptee
	 */
	VTreePanel_keyAdapter(VTreePanel adaptee)
	{
		m_adaptee = adaptee;
	}

	/**
	 * 	Key Pressed
	 *	@param e
	 */
	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			m_adaptee.keyPressed(e);
	}
}   //  VTreePanel_keyAdapter
