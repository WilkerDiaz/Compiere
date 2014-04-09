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
package org.compiere.apps.wf;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;

import org.compiere.swing.*;
import org.compiere.util.*;
import org.compiere.wf.*;

/**
 *	Workflow Content Panel.
 *	
 *	
 *  @author Jorg Janke
 *  @version $Id: WFContentPanel.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class WFContentPanel extends CPanel 	
	implements MouseListener, MouseMotionListener, ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	WFContentPanel
	 */
	public WFContentPanel (WFPanel parent)
	{
		super (new WFLayoutManager());
		m_parent = parent;

		m_NewPopupMenu.add(m_NewMenuNode);
		m_NewMenuNode.addActionListener(this);
	}	//	WFContentPanel
	
	/** Parent Panel	*/
	WFPanel 					m_parent = null;
	/**	Logger			*/
	static final CLogger		log = CLogger.getCLogger(WFContentPanel.class);
	/** Node List		*/
	private ArrayList<WFNode>	m_nodes = new ArrayList<WFNode>();
	/** Line List		*/
	private ArrayList<WFLine>	m_lines = new ArrayList<WFLine>();
	/**	Last Pressed Point	*/
	private Point		m_draggedStart = null;
	/** Last Dragged Node	*/
	private WFNode		m_draggedNode = null;
	/**	Dragged				*/
	private boolean		m_dragged = false;
	
	/**	ReadWrite			*/
	private boolean		m_readWrite = false;
	/** The Workflow		*/
	MWorkflow			m_wf = null;
	
	private JPopupMenu 			m_NewPopupMenu = new JPopupMenu();
	private CMenuItem 			m_NewMenuNode = new CMenuItem(Msg.getMsg(Env.getCtx(), "CreateNewNode"));

	private JPopupMenu 			m_LinePopupMenu = null;

	/**
	 * 	Set Read/Write
	 *	@param readWrite read/write
	 */
	public void setReadWrite (boolean readWrite)
	{
		m_readWrite = readWrite;
		if (m_readWrite)
			addMouseListener(this);
		else
			removeMouseListener(this);
	}	//	setReadWrite

	/**
	 * 	Set Workflow
	 *	@param wf workflow
	 */
	public void setWorkflow (MWorkflow wf)
	{
		m_wf = wf;
	}	//	setWorkflow
	
	
	/**
	 * 	Remove All and their listeners
	 */
	@Override
	public void removeAll ()
	{
		m_nodes.clear();
		m_lines.clear();
		Component[] components = getComponents();
		for (Component component : components) {
			component.removeMouseListener(this);
			component.removeMouseMotionListener(this);
		}
		super.removeAll ();
	}	//	removeAll
	
	
	/**
	 * 	Add Component and add Mouse Listener
	 *	@param comp component
	 *	@param rw read/write
	 *	@return component
	 */
	public Component add (Component comp, boolean rw)
	{
		if (comp instanceof WFLine)
		{
			m_lines.add((WFLine)comp);
			return comp;
		}
        else if (comp instanceof WFNode)
		{
			m_nodes.add((WFNode)comp);
			comp.addMouseListener(this);
			if (m_readWrite && rw)	//	can be moved
				comp.addMouseMotionListener(this);
		}
        
		return super.add (comp);
	}	//	add

	/**
	 * 	Create Lines.
	 * 	Called by WF Layout Manager
	 */
	protected void createLines()
	{
		log.fine("Lines #" + m_lines.size());
		for (int i = 0; i < m_lines.size(); i++)
		{
			WFLine line = m_lines.get(i);
			RectangularShape from = findBounds (line.getAD_WF_Node_ID());
            RectangularShape to = findBounds (line.getAD_WF_Next_ID());
			line.setFromTo(from, to);
		}	//	for all lines
	}
	
	/**
	 * 	Get Bounds of WF Node Icon
	 * 	@param AD_WF_Node_ID node id
	 * 	@return bounds of node with ID or null
	 */
	private RectangularShape findBounds (int AD_WF_Node_ID)
	{
		for (int i = 0; i < m_nodes.size(); i++)
		{
			WFNode node = m_nodes.get(i);
			if (node.getAD_WF_Node_ID() == AD_WF_Node_ID)
				return node.getRectangularShape();
		}
		return null;
	}	//	findBounds

	
	/**
	 * 	Get Component At point
	 *	@param p point
	 *	@return Node (ignore lines)
	 */
	@Override
	public Component getComponentAt (Point p)
	{
		return getComponentAt (p.x, p.y);
	}	//	getComponentAt
	
	/**
	 * 	Get Node at x/y
	 *	@param x x
	 *	@param y y
	 *	@return Node (ignore lines)
	 */
	@Override
	public Component getComponentAt (int x, int y)
	{
		Component comp = super.getComponentAt (x, y);
		if (comp instanceof WFNode)
			return comp;
		for (int i = 0; i < m_nodes.size(); i++)
		{
			WFNode node = m_nodes.get(i);
			int xx = x - node.getX();
			int yy = y - node.getY();
			if (node.contains(xx, yy))
				return node;
		}
		return comp;
	}	//	getComponentAt
	
	
	/**************************************************************************
	 * 	Mouse Clicked.
	 * 	Pressed - Released - Clicked.
	 *	@param e event
	 */
	public void mouseClicked (MouseEvent e)
	{
		if (m_readWrite && SwingUtilities.isRightMouseButton(e))
		{
			int AD_Client_ID = Env.getCtx().getAD_Client_ID();
			if (e.getSource() == this && m_wf != null)
			{
				m_NewPopupMenu.show(this, e.getX(), e.getY());
			}
			else if (e.getSource() instanceof WFNode)
			{
				MWFNode node = ((WFNode)e.getSource()).getModel();
				m_LinePopupMenu = new JPopupMenu(node.getName());
				if (node.getAD_Client_ID() == AD_Client_ID)
				{
					String title = Msg.getMsg(Env.getCtx(), "DeleteNode") + 
						": " + node.getName();
					addMenuItem(m_LinePopupMenu, title, node, -1);
					m_LinePopupMenu.addSeparator();
				}
				MWFNode[] nodes = m_wf.getNodes(true, AD_Client_ID);
				MWFNodeNext[] lines = node.getTransitions(AD_Client_ID);
				//	Add New Line
				for (MWFNode nn : nodes) {
					if (nn.getAD_WF_Node_ID() == node.getAD_WF_Node_ID())
						continue;	//	same
					boolean found = false;
					if (m_parent.nodesConnected(node.getAD_WF_Node_ID(), nn.getAD_WF_Node_ID()))
						continue;
					for (MWFNodeNext line : lines) {
						if (nn.getAD_WF_Node_ID() == line.getAD_WF_Next_ID())
						{
							found = true;
							break;
						}
					}
					if (!found &&
						!workflowContainsLoop(m_wf, node.getAD_WF_Node_ID(), nn.getAD_WF_Node_ID()) )
					{
						String title = Msg.getMsg(Env.getCtx(), "AddLine") 
							+ ": " + node.getName() + " -> " + nn.getName();
						addMenuItem(m_LinePopupMenu, title, node, nn.getAD_WF_Node_ID());
					}
				}
				m_LinePopupMenu.addSeparator();
				//	Delete Lines
				for (MWFNodeNext line : lines) {
					if (line.getAD_Client_ID() != AD_Client_ID)
						continue;
					MWFNode next = MWFNode.get(Env.getCtx(), line.getAD_WF_Next_ID());
					String title = Msg.getMsg(Env.getCtx(), "DeleteLine")
						+ ": " + node.getName() + " -> " + next.getName();
					addMenuItem(m_LinePopupMenu, title, line);
					System.out.println(" from : " + line.getAD_WF_Node_ID() + " to : " + line.getAD_WF_Next_ID());
				}
				m_LinePopupMenu.show(this, e.getX(), e.getY());
			}
		}
		
		//	Selection
		else if (e.getSource() instanceof WFNode)
		{
			WFNode selected = (WFNode)e.getSource();
			log.fine(selected.toString());
			for (int i = 0; i < m_nodes.size(); i++)
			{
				WFNode node = m_nodes.get(i);
				if (selected.getAD_WF_Node_ID() == node.getAD_WF_Node_ID())
					node.setSelected(true);
				else
					node.setSelected(false);
			}
		}
		m_dragged = false;
	}	//	mouseClicked
	

	/**
	 * 	Mouse Entered
	 *	@param e event
	 */
	public void mouseEntered (MouseEvent e)
	{
	}	//	mouseEntered

	/**
	 * 	Mouse Exited
	 *	@param e event
	 */
	public void mouseExited (MouseEvent e)
	{
	}	//	mouseExited

	/**
	 * 	Mouse Pressed.
	 * 	Initial drag
	 *	@param e event
	 */
	public void mousePressed (MouseEvent e)
	{
		if (e.getSource() instanceof WFNode)
		{
			WFNode node = (WFNode)e.getSource();
			if (node.isEditable())
			{
				m_draggedNode = node;
				m_draggedStart = SwingUtilities.convertPoint(m_draggedNode, e.getX(), e.getY(), this);
			}
			else
			{
				m_dragged = false;
				m_draggedNode = null;
				m_draggedStart = null;
			}
		}
	}	//	mousePressed

	
	/**********************************
	 * 	Mouse Dragged
	 *	@param e event
	 */
	public void mouseDragged (MouseEvent e)
	{
		//	Nothing selected
		if (m_draggedNode == null || e.getSource() != m_draggedNode)
		{	
			if (e.getSource() instanceof WFNode)
			{
				WFNode node = (WFNode)e.getSource();
				if (node.isEditable())
					m_draggedNode = node;
				m_draggedStart = null;
			}
		}
		//	Move Node
		if (m_draggedNode != null)
		{
			m_dragged = true;
			if (m_draggedStart == null)
				m_draggedStart = SwingUtilities.convertPoint(m_draggedNode, e.getX(), e.getY(), this);
			//	If not converted to coordinate system of parent, it gets jumpy
			Point mousePosition = SwingUtilities.convertPoint(m_draggedNode, e.getX(), e.getY(), this);
			int xDelta = mousePosition.x - m_draggedStart.x;
			int yDelta = mousePosition.y - m_draggedStart.y;
			Point newLocation = m_draggedNode.getLocation();
			newLocation.x += xDelta;
			if (newLocation.x < 0)
				newLocation.x = 0;
			newLocation.y += yDelta;
			if (newLocation.y < 0)
				newLocation.y = 0;
			m_draggedNode.setLocation(newLocation.x, newLocation.y);
		//	log.fine("mouseDragged - " + m_draggedNode + " - " + e);
		//	log.fine("mouseDragged - Delta=" + xDelta + "/" + yDelta); 
			m_draggedStart = mousePosition;
			invalidate();
			validate();
			repaint();
		}
	}	//	mouseDragged

	/**
	 * 	Mouse Released.
	 * 	Finals dragging
	 *	@param e event
	 */
	public void mouseReleased (MouseEvent e)
	{
	//	log.fine("Node=" + m_draggedNode);
		m_dragged = false;
		if (m_draggedNode != null)
			m_draggedNode.getModel().save();	//	save position
		m_draggedNode = null;
		m_draggedStart = null;
		repaint();
	}	//	mouseReleased

	/**
	 * 	Mouse Moved
	 *	@param e event
	 */
	public void mouseMoved (MouseEvent e)
	{
	}	//	mouseMoved

    /**************************************************************************
     *  Paint Children first, and then paint Lines directly since they're not
     *  components.
     *  
     *  @param g graphics
     */
    @Override
	protected void paintChildren(Graphics g)
    {
        super.paintChildren(g);
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                         RenderingHints.VALUE_ANTIALIAS_ON);
        //  Paint Lines
        for (int i = 0; i < m_lines.size(); i++)
        {
            WFLine line = m_lines.get(i);
            line.paint(g);
        }
        
        //  Paint Position = right next to the box
        if (m_dragged && m_draggedNode != null)
        {
            Point loc = m_draggedNode.getLocation();
            String text = "(" + loc.x + "," + loc.y +")"; 
            Graphics2D g2D = (Graphics2D)g;
            Font font = new Font("Dialog", Font.PLAIN, 10);
            g2D.setColor(Color.magenta);
            TextLayout layout = new TextLayout (text, font, g2D.getFontRenderContext());
            loc.x += m_draggedNode.getWidth();
            loc.y += layout.getAscent();
            layout.draw(g2D, loc.x, loc.y);
        }
    }
	
	/**
	 * 	Add Menu Item to - add new line to node
	 *	@param menu base menu
	 *	@param title title
	 */
	private void addMenuItem (JPopupMenu menu, String title, MWFNode node, int AD_WF_NodeTo_ID)
	{
		WFPopupItem item = new WFPopupItem (title, node, AD_WF_NodeTo_ID);
		menu.add(item);
		item.addActionListener(this);
	}	//	addMenuItem
	
	/**
	 * 	Add Menu Item to - delete line
	 *	@param menu base menu
	 *	@param title title
	 */
	private void addMenuItem (JPopupMenu menu, String title, MWFNodeNext line)
	{
		WFPopupItem item = new WFPopupItem (title, line);
		menu.add(item);
		item.addActionListener(this);
	}	//	addMenuItem

	/**
	 * 	Action Listener
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
	//	log.info(e.toString());
		
		//	Add new Node
		if (e.getSource()== m_NewMenuNode)
		{
			log.info("Create New Node");
			String nameLabel = Util.cleanMnemonic(Msg.getMsg(Env.getCtx(), "Name"));
			String name = JOptionPane.showInputDialog(this,
				nameLabel,									//	message
				Msg.getMsg(Env.getCtx(), "CreateNewNode"),	//	title
				JOptionPane.QUESTION_MESSAGE);
			if (name != null && name.length() > 0)
			{
				int AD_Client_ID = Env.getCtx().getAD_Client_ID();
				MWFNode node = new MWFNode(m_wf, name, name);
				node.setClientOrg(AD_Client_ID, 0);
				node.save();
				m_parent.load(m_wf.getAD_Workflow_ID(), true);
			}
		}
		//	Add/Delete Line
		else if (e.getSource() instanceof WFPopupItem)
		{
			WFPopupItem item = (WFPopupItem)e.getSource();
			item.execute();
		}
		
	}	//	actionPerformed
	
	
	/**************************************************************************
	 * 	WF Content Panel Popup Item
	 *  @author Jorg Janke
	 *  @version $Id: WFContentPanel.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
	 */
	class WFPopupItem extends JMenuItem
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 	Add Line Item
		 *	@param title title
		 *	@param node node
		 *	@param AD_WF_NodeTo_ID line to
		 */
		public WFPopupItem (String title, MWFNode node, int AD_WF_NodeTo_ID)
		{
			super (title);
			m_node = node;
			m_AD_WF_NodeTo_ID = AD_WF_NodeTo_ID;
		}	//	WFPopupItem
		
		/**
		 * 	Delete Line Item
		 *	@param title title
		 *	@param line line to be deleted
		 */
		public WFPopupItem (String title, MWFNodeNext line)
		{
			super (title);
			m_line = line;
		}	//	WFPopupItem

		/** The Node			*/
		private MWFNode		m_node;
		/** The Line			*/
		private MWFNodeNext m_line;
		/** The Next Node ID	*/
		private int			m_AD_WF_NodeTo_ID;
		
		/**
		 * 	Execute 
		 */
		public void execute()
		{
			//	Add Line
			if (m_node != null && m_AD_WF_NodeTo_ID > 0)
			{
				int AD_Client_ID = Env.getCtx().getAD_Client_ID();
				MWFNodeNext newLine = new MWFNodeNext(m_node, m_AD_WF_NodeTo_ID);
				newLine.setClientOrg(AD_Client_ID, 0);
				newLine.save();
				log.info("Add Line to " + m_node + " -> " + newLine);
				m_parent.load(m_wf.getAD_Workflow_ID(), true);
			}
			//	Delete Node
			else if (m_node != null && m_AD_WF_NodeTo_ID == -1)
			{
				log.info("Delete Node: " + m_node);
				m_node.delete(false);
				m_parent.load(m_wf.getAD_Workflow_ID(), true);
			}
			//	Delete Line
			else if (m_line != null)
			{
				log.info("Delete Line: " + m_line);
				m_line.delete(false);
				m_parent.load(m_wf.getAD_Workflow_ID(), true);
			}
			else
				log.severe("No Action??");
		}	//	execute
		
	}	//	WFPopupItem
	
	
	private static boolean workflowContainsLoop(MWorkflow wf, int fromId, int toId) {
		
		Set<MWFNode> rootNodes = wf.getRootNodes(Env.getCtx().getAD_Client_ID());
		for (MWFNode node : rootNodes) {
			if (checkLoop(wf, node, fromId, toId, new HashSet<Integer>()))
				return true;
		}
		return false;
		
	}
	
	/**
	 * Depth first search traversal of workflow graph to detect loop
	 *   before a transition/line is created
	 * @param wf  workflow to be searched
	 * @param startNode  starting node to search
	 * @param fromId     transition source node to be added
	 * @param toId       transition destination node to be added
	 * @param visitedNodeIds   nodes traversed 
	 * @return  if the workflow contains loop
	 */
	private static boolean checkLoop(MWorkflow wf, MWFNode startNode, 
			int fromId, int toId, Set<Integer> visitedNodeIds) {
		
		Integer nodeId = Integer.valueOf(startNode.getAD_WF_Node_ID());
		boolean loop = visitedNodeIds.contains(nodeId);
		if (!loop) {
			visitedNodeIds.add(nodeId);
			if (fromId == nodeId.intValue()) {
				MWFNode next = MWFNode.get(Env.getCtx(), toId);
				if (checkLoop(wf, next, fromId, toId, visitedNodeIds))
					return true;
			}
			MWFNodeNext[] lines = startNode.getTransitions(Env.getCtx().getAD_Client_ID());
			for (MWFNodeNext line :  lines) {
				MWFNode next = MWFNode.get(Env.getCtx(), line.getAD_WF_Next_ID());
				if(checkLoop(wf, next, fromId, toId, visitedNodeIds))
					return true;
			}
		}
		visitedNodeIds.remove(nodeId);
		return loop;
	}

}	//	WFContentPanel
