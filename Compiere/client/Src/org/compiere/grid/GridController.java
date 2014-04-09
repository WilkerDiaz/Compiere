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
package org.compiere.grid;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.compiere.apps.*;
import org.compiere.common.CompiereStateException;
import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.grid.ed.*;
import org.compiere.grid.tree.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  The Grid Controller is the panel for single and multi-row presentation
 *  and links to the Model Tab.
 *
 *  <pre>
 *  UI Structure:
 *  this    (BorderLayout)
 *      splitPane (JSplitPane)
 *          left
 *              graphicPanel
 *          right
 *              cardPanel   JPanel  (CardLayout)
 *                  srPane  JSplitPane
 * 						vPane	JScrollPane
 *              	        vPanel  VPanel (GridBagLayout)
 * 						vIncludedGC	GridController
 *                  mrPane  JScrollPane
 *                      vTable  VTable
 *
 *  <B>DataBinding:<B>
 *  - MultiRow - is automatic between VTable and MTable
 *  - SingleRow
 *		- from VEditors via fireVetoableChange(m_columnName, null, getText());
 *			(vetoableChange)
 *		- to VEditors via updateSingleRow -> Editor.setValue(object)
 *
 *  Event Chains
 *  -- Navigation --
 *  (VTable selection -> GridController.valueChanged)
 *  (APanel selection)
 *      + GridTab.navivate
 *          + GridTab.setCurrentRow
 *              + Update all GridFields
 *                  + GridField.setValue
 *                      + setContext
 *                      + fire PropertyChange "Value"
 *                          + VEditor.propertyChange
 *                              + VEditor.setValue
 *              + GridTab.fireProperyChange "CurrentRow"
 *                  + VTable.propertyChange (setRowSelectionInterval)
 *                      + GridController.valueChange
 *                          + GridController.dynamicDisplay(complete)
 *              + GridTab.fireDataStatusChanged
 *                  + APanel.statusChanged
 *
 *  -- ValueChanges --
 *  VEditor.fireVetoableChange
 *      + (VCellEditor.vetoableChange/getCellEditorValue)   -- multi-row source
 *      + (GridController.vetoableChange)                   -- single-row source
 *          + GridTable.setValueAt
 *              + GridField.setValue
 *                  + setContext
 *                  + fire PropertyChange "Value"
 *                      + VEditor.setValue
 *              + GridTable.fireDataStatusChanged
 *                  + GridTab.dataStatusChanged
 *                      + GridTab.fireDataStatusChanged
 *                          + APanel.statusChanged
 *                  + GridController.dataStatusChanged
 *                      + GridController.dynamicDisplay(selective)
 *  </pre>
 * @author  Jorg Janke
 * @version $Id$
 */
public class GridController extends CPanel
	implements DataStatusListener, ListSelectionListener, Evaluatee,
		VetoableChangeListener,	PropertyChangeListener, MouseListener
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  Constructor - you need to call initGrid for instanciation
	 *	@param withRowHeader with numbers in Front
	 */
	public GridController(boolean withRowHeader)
	{
		vTable = new VTable(withRowHeader, this);
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		setFocusable(false);
	}   //  GridController

	/**
	 *  toString
	 *  @return string representation
	 */
	@Override
	public String toString()
	{
		return "GridController for " + m_mTab;
	}   //  toString

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(GridController.class);
	
	/**
	 *  The Layout
	 */
	private final BorderLayout mainLayout = new BorderLayout();
	private JSplitPane splitPane = new JSplitPane();
	private final CPanel graphPanel = new CPanel();
	private final BorderLayout graphLayout = new BorderLayout();
	private final CPanel cardPanel = new CPanel();
	private final CardLayout cardLayout = new CardLayout();
	private JSplitPane srPane = new JSplitPane();
	private final JScrollPane vPane = new JScrollPane();
	private GridController vIncludedGC = null;
	private final CScrollPane mrPane = new CScrollPane();
	private final CPanel xPanel = new CPanel();
	private final BorderLayout xLayout = new BorderLayout();
	VTable vTable = null;
	private VPanel vPanel = new VPanel();

	/**
	 *  Static Layout init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		this.setLayout(mainLayout);
		this.add(splitPane, BorderLayout.CENTER);
		splitPane.setOpaque(false);
		graphPanel.setLayout(graphLayout);
		//
		splitPane.add(graphPanel, JSplitPane.LEFT);
		splitPane.add(cardPanel, JSplitPane.RIGHT);
		splitPane.setBorder(null);
		splitPane.setName("gc_splitPane");
		//
		cardPanel.setLayout(cardLayout);
		cardPanel.add(srPane, "srPane");	//	Sequence Important!
		cardPanel.add(mrPane, "mrPane");
		cardPanel.setBorder(null);
		cardPanel.setName("gc_cardPanel");
		//  single row (w/o xPane it would be centered)
		srPane.setBorder(null);
		srPane.setName("gc_srPane");
		srPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		srPane.add(vPane, JSplitPane.TOP);
		srPane.setTopComponent(vPane);
		srPane.setBottomComponent(null);	//	otherwise a button is created/displayed
		//
		vPane.getViewport().add(xPanel, null);
		vPane.setBorder(null);
		xPanel.setLayout(xLayout);
		xPanel.setName("gc_xPanel");
		xPanel.add(vPanel, BorderLayout.PAGE_START);
		//  multi-row
		mrPane.setBorder(null);
		mrPane.getViewport().add(vTable, null);

		//	Row header listener
		CRowHeader rh = vTable.getRowHeader();
		if (rh != null)
		{
			rh.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent me)
				{
					if ((vTable.getRowCount() > 0) && SwingUtilities.isLeftMouseButton(me))
					{
						if (me.getClickCount() < 2)
						{
							int row = vTable.rowAtPoint(me.getPoint());
							vTable.getSelectionModel().setSelectionInterval(row, row);
						}
						else
							switchRowPresentation();
					}
				}
			});
		}
		mrPane.setName("gc_mrPane");
		//
		graphPanel.setBorder(null);
		graphPanel.setName("gc_graphPanel");
		srPane.setDividerLocation(200);
	}   //  jbInit

	/**
	 *  Displose
	 */
	public void dispose()
	{
		log.config( "(" + m_mTab.toString() + ")");
		//  clear info
		stopEditor(false);
		if (m_mTab.needSave(true, false))
			m_mTab.dataIgnore();
		vIncludedGC = null;

		//  Listeners
		m_mTab.getTableModel().removeDataStatusListener(this);
		m_mTab.getTableModel().removeVetoableChangeListener(this);
		vTable.getSelectionModel().removeListSelectionListener(this);
		m_mTab.removePropertyChangeListener(vTable);

		//  editors
		Component[] comp = vPanel.getComponents();
		for (Component element : comp) {
			if (element instanceof VEditor)
			{
				VEditor vEditor = (VEditor)element;
				vEditor.removeVetoableChangeListener(this);
				String columnName = element.getName();
				GridField mField = m_mTab.getField(columnName);
				if (mField != null)
					mField.removePropertyChangeListener(vEditor);
				vEditor.dispose();
			}
		}
		/** @todo Remove APanel Button listeners */

		vTable.removeAll();
		vTable.setModel(new DefaultTableModel());   //  remove reference
		vTable = null;
		vPanel.removeAll();
		vPanel = null;
		srPane.removeAll();
		srPane = null;
		splitPane.removeAll();
		splitPane = null;
		m_mTab = null;
		m_tree = null;
		this.removeAll();
	}   //  dispose

	/** Model Tab                   */
	private GridTab		m_mTab = null;
	/** Window                      */
	private int         m_WindowNo;
	/** Only Multi-Row exist        */
	private boolean     m_onlyMultiRow = false;
	/** Single/Multi Row indicator  */
	private boolean     m_singleRow = true;
	/** Veto Active                 */
	private boolean     m_vetoActive = false;
	/** Tree Panel (optional)       */
	private VTreePanel  m_tree;


	/**************************************************************************
	 *  Init Grid.
	 *  <pre>
	 *  - Map table to model
	 *  - Update (multi-row) table info with renderers/editors
	 *  - build single-row panel
	 *  - initialize display
	 *  </pre>
	 *  @param mTab tab
	 *  @param onlyMultiRow only table
	 *  @param WindowNo window no
	 *  @param aPanel optional Application Panel for adding button listeners
	 * 	@param mWindow parent Window Model
	 *  @return true if initialized
	 */
	public boolean initGrid (GridTab mTab, boolean onlyMultiRow,
		int WindowNo, APanel aPanel, GridWindow mWindow)
	{
		log.config( "(" + mTab.toString() + ")");
		m_mTab = mTab;
		m_WindowNo = WindowNo;
		m_onlyMultiRow = onlyMultiRow;
		setName("GC-" + mTab);

		MRole role = MRole.getDefault();	//	Client
		if (!role.isDisplayClient())
			m_mTab.getField("AD_Client_ID").setDisplayed(false);
		if (!role.isDisplayOrg())
			m_mTab.getField("AD_Org_ID").setDisplayed(false);

		//  Set up Multi Row Table
		vTable.setModel(m_mTab.getTableModel());

		//  Update Table Info -------------------------------------------------
		int size = setupVTable (aPanel, m_mTab, vTable);

		//  Set Color on Tab Level
		//  this.setBackgroundColor (mTab.getColor());

		//  Single Row  -------------------------------------------------------
		if (!m_onlyMultiRow)
		{
			//	Set Softcoded Mnemonic &x
			for (int i = 0; i < size; i++)
			{
				GridField mField = m_mTab.getField(i);
				if (mField.isDisplayed())
					vPanel.setMnemonic(mField);
			}   //  for all fields

			//	Add Fields
			for (int i = 0; i < size; i++)
			{
				GridField mField = m_mTab.getField(i);
				if (mField.isDisplayed())
				{
					VEditor vEditor = VEditorFactory.getEditor(m_mTab, mField, false);
					if (vEditor == null && !mField.isHeading())
					{
						log.warning("Editor not created for " + mField.getColumnName());
						continue;
					}
					
					if (vEditor != null)
					{
						//  MField => VEditor - New Field value to be updated to editor
						mField.addPropertyChangeListener(vEditor);
						//  VEditor => this - New Editor value to be updated here (MTable)
						vEditor.addVetoableChangeListener(this);
					}
					//  Add to VPanel
					vPanel.addField(vEditor, mField);
					//  APanel Listen to buttons
					if ((mField.getDisplayType() == DisplayTypeConstants.Button) && (aPanel != null))
						((JButton)vEditor).addActionListener (aPanel);
				}
			}   //  for all fields

			//	No Included Grid Controller
			srPane.setResizeWeight(1);	//	top part gets all
			srPane.setDividerSize (0);
			srPane.setDividerLocation (9999);

			//  Use SR to size MR
			mrPane.setPreferredSize(vPanel.getPreferredSize());
		}   //  Single-Row

		//  Tree Graphics Layout
		int AD_Tree_ID = 0;
		if (m_mTab.isTreeTab())
			AD_Tree_ID = MTree.getDefaultAD_Tree_ID (
				Env.getCtx().getAD_Client_ID(), m_mTab.getAD_Table_ID());
		if (m_mTab.isTreeTab() && (AD_Tree_ID != 0))
		{
			m_tree = new VTreePanel(m_WindowNo, false, true);
			if (m_mTab.getTabNo() == 0)	//	initialize other tabs later
				m_tree.initTree(AD_Tree_ID);
			m_tree.addPropertyChangeListener(VTreePanel.NODE_SELECTION, this);
			graphPanel.add(m_tree, BorderLayout.CENTER);
			splitPane.setDividerLocation(250);
		//	splitPane.resetToPreferredSizes();
		}
		else    //  No Graphics - hide
		{
			graphPanel.setPreferredSize(new Dimension(0,0));
			splitPane.setDividerSize(0);
			splitPane.setDividerLocation(0);
		}

		//  Receive DataStatusChanged info from MTab
		m_mTab.addDataStatusListener(this);
		//  Receive vetoableChange info from MTable when saving
		m_mTab.getTableModel().addVetoableChangeListener(this);
		//	Selection Listener -> valueChanged
		vTable.getSelectionModel().addListSelectionListener(this);
		//  Navigation (RowChanged)
		m_mTab.addPropertyChangeListener(vTable);

		//  Update UI
		vTable.autoSize(true);

		setTabLevel(m_mTab.getTabLevel());

		//  Set initial presentation
		if (onlyMultiRow || !m_mTab.isSingleRow())
			switchMultiRow();
		else
			switchSingleRow();
		
		// delegates navigation key shortcuts handling to APanel
		InputMap vTableInputMap = null;
		ActionMap vTableActionMap = null;
		ActionMap aPanelActionMap = null;
		if (vTable != null) {
			vTableInputMap = vTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
			vTableActionMap = vTable.getActionMap();
		}
		if (aPanel != null)
			aPanelActionMap = aPanel.getActionMap();
		
		if (vTableInputMap != null && vTableActionMap != null && aPanelActionMap != null)
		{
			vTableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), APanel.ACTION_NAME_NEXT);
			vTableActionMap.put(APanel.ACTION_NAME_NEXT, aPanelActionMap.get(APanel.ACTION_NAME_NEXT));
			vTableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), APanel.ACTION_NAME_PREV);
			vTableActionMap.put(APanel.ACTION_NAME_PREV, aPanelActionMap.get(APanel.ACTION_NAME_PREV));
			vTableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, Event.CTRL_MASK), APanel.ACTION_NAME_FIRST);
			vTableActionMap.put(APanel.ACTION_NAME_FIRST, aPanelActionMap.get(APanel.ACTION_NAME_FIRST));
			vTableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_END, Event.CTRL_MASK), APanel.ACTION_NAME_LAST);
			vTableActionMap.put(APanel.ACTION_NAME_LAST, aPanelActionMap.get(APanel.ACTION_NAME_LAST));
			
			vTableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), APanel.ACTION_NAME_FIND);
			vTableActionMap.put(APanel.ACTION_NAME_FIND, aPanelActionMap.get(APanel.ACTION_NAME_FIND));
			vTableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0), APanel.ACTION_NAME_CHAT);
			vTableActionMap.put(APanel.ACTION_NAME_CHAT, aPanelActionMap.get(APanel.ACTION_NAME_CHAT));
			vTableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), APanel.ACTION_NAME_ARCHIVE);
			vTableActionMap.put(APanel.ACTION_NAME_ARCHIVE, aPanelActionMap.get(APanel.ACTION_NAME_ARCHIVE));
			
		}
		
		InputMap vPanelInputMap = null;
		ActionMap vPanelActionMap = null;
		if (vPanel != null) {
			vPanelInputMap = vPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
			vPanelActionMap = vPanel.getActionMap();
		}
		
		if (vPanelInputMap != null && vPanelActionMap != null && aPanelActionMap != null)
		{
			vPanelInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), APanel.ACTION_NAME_FIND);
			vPanelActionMap.put(APanel.ACTION_NAME_FIND, aPanelActionMap.get(APanel.ACTION_NAME_FIND));
			vPanelInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0), APanel.ACTION_NAME_CHAT);
			vPanelActionMap.put(APanel.ACTION_NAME_CHAT, aPanelActionMap.get(APanel.ACTION_NAME_CHAT));
			vPanelInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), APanel.ACTION_NAME_ARCHIVE);
			vPanelActionMap.put(APanel.ACTION_NAME_ARCHIVE, aPanelActionMap.get(APanel.ACTION_NAME_ARCHIVE));
		}

	//	log.config( "GridController.dynInit (" + mTab.toString() + ") - fini");
		return true;
	}   //  initGrid

	/**
	 * 	Include Tab
	 * 	@param gc grod controller to add
	 * 	@return true if included
	 */
	public boolean includeTab (GridController gc)
	{
		GridTab imcludedMTab = gc.getMTab();
		if (m_mTab.getIncluded_Tab_ID () != imcludedMTab.getAD_Tab_ID())
			return false;
		//
		vIncludedGC = gc;
		vIncludedGC.switchMultiRow();
		//
		Dimension size = getPreferredSize();
		srPane.setResizeWeight(.75);	//	top part gets 75%
		srPane.add(vIncludedGC, JSplitPane.BOTTOM);
		srPane.setBottomComponent(vIncludedGC);
		srPane.setDividerSize (5);
		//
		int height = 150;
		vIncludedGC.setPreferredSize(new Dimension(600, height));
		setPreferredSize(new Dimension(size.width, size.height+height));
		srPane.setDividerLocation (size.height);
		//
		imcludedMTab.setIncluded (true);
		imcludedMTab.query (0, 0, false);	//	updated
		//
		JRootPane rt = SwingUtilities.getRootPane(this);
		if (rt == null)
			System.out.println("Root pane null");
		else
		{
			System.out.println("Root=" + rt);
			rt.addMouseListener(vIncludedGC);
			Component gp = rt.getGlassPane();
			if (gp == null)
				System.out.println("No Glass Pane");
			else
			{
				System.out.println("Glass=" + gp);
				gp.addMouseListener(vIncludedGC);
			}

		}
		vIncludedGC.addMouseListener(vIncludedGC);
		vIncludedGC.enableEvents(AWTEvent.HIERARCHY_EVENT_MASK + AWTEvent.MOUSE_EVENT_MASK);
		/**
		vIncludedGC.splitPane.addMouseListener(vIncludedGC);
		vIncludedGC.cardPanel.addMouseListener(vIncludedGC);
		vIncludedGC.mrPane.addMouseListener(vIncludedGC);
		vIncludedGC.vTable.addMouseListener(vIncludedGC);
		**/
		return true;
	}	//	IncludeTab

	/**
	 * 	Get Title
	 *	@return title
	 */
	public String getTitle ()
	{
		return m_mTab.getName();
	}	//	getTitle

	/**
	 *	Setup Multi-Row Table (add fields)
	 * 	@param aPanel Panel
	 * 	@param mTab Model Tab
	 * 	@param table JTable
	 * 	@return size
	 */
	private int setupVTable (APanel aPanel, GridTab mTab, VTable table)
	{
		if (!mTab.isDisplayed(true))
			return 0;
		int size = mTab.getFieldCount ();
		TableColumnModel tcm = table.getColumnModel();
		if (size != tcm.getColumnCount())
			throw new CompiereStateException("TableColumn Size <> TableModel");

		for (int i = 0; i < size; i++)
		{
			GridField mField = mTab.getField (i);
			TableColumn tc = tcm.getColumn(i);
			tc.setMinWidth(30);
			//
			if (mField.getColumnName().equals(tc.getHeaderValue()))
			{
				tc.setIdentifier(mField.getColumnName());
				if (mField.getDisplayType () == DisplayTypeConstants.RowID)
				{
					tc.setCellRenderer (new VRowIDRenderer (false));
					tc.setCellEditor (new VRowIDEditor (false));
					tc.setHeaderValue ("");
					tc.setMaxWidth (2);
				}
				else
				{
					//  need to set CellEditor explicitly as default editor based on class causes problem (YesNo-> Boolean)
					if (mField.isDisplayed ())
					{
						tc.setCellRenderer (new VCellRenderer (mField));
						VCellEditor ce = new VCellEditor (mField);
						tc.setCellEditor (ce);
						//
						tc.setHeaderValue (mField.getHeader ());
						tc.setPreferredWidth (Math.max (mField.getDisplayLength (), 30));
						tc.setHeaderRenderer (new VHeaderRenderer (mField.getDisplayType ()));

						//  Enable Button actions in grid
						if (mField.getDisplayType () == DisplayTypeConstants.Button)
						{
							VEditor button = ce.getEditor ();
							if ((button != null) && (aPanel != null))
								((JButton)button).addActionListener (aPanel);
						}
					}
					else //  column not displayed
					{
						TableCellNone tcn = new TableCellNone(mField.getColumnName());
						tc.setCellRenderer (tcn);
						tc.setCellEditor (tcn);
						tc.setHeaderValue (null);
						tc.setMinWidth (0);
						tc.setMaxWidth (0);
						tc.setPreferredWidth (0);
					}
				}
			//	System.out.println ("TableColumnID " + tc.getIdentifier ()
			//	  + "  Renderer=" + tc.getCellRenderer ()
			//	  + mField.getHeader ());

			}	//	found field
			else
				log.log(Level.SEVERE, "TableColumn " + tc.getHeaderValue()
				  + " <> MField " + mField.getColumnName() + mField.getHeader());
		} 	//  for all fields

		/** Reorder
		for (int i = 0; i < size; i++)
			tcm.moveColumn(size, i);
		**/

		return size;
	}	//	setupVTable

	/**
	 * 	Activate Grid Controller.
	 * 	Called by APanel when GridController is displayed (foreground)
	 */
	public void activate ()
	{
		//	Tree to be initiated on second/.. tab
		if (m_mTab.isTreeTab() && (m_mTab.getTabNo() > 0))
		{
			int AD_Tree_ID = 0;
			if (m_mTab.getTabLevel() > 0)	//	check previous tab for AD_Tree_ID
			{
				String keyColumnName = m_mTab.getKeyColumnName();
				String treeName = "AD_Tree_ID";
				if (keyColumnName.startsWith("CM"))
				{
					if (keyColumnName.equals("CM_Container_ID"))
						treeName = "AD_TreeCMC_ID";
					else if (keyColumnName.equals("CM_CStage_ID"))
						treeName = "AD_TreeCMS_ID";
					else if (keyColumnName.equals("CM_Template_ID"))
						treeName = "AD_TreeCMT_ID";
					else if (keyColumnName.equals("CM_Media_ID"))
						treeName = "AD_TreeCMM_ID";
				}
				AD_Tree_ID = Env.getCtx().getContextAsInt( m_WindowNo, treeName);
				log.config(keyColumnName + " -> " + treeName + " = " + AD_Tree_ID);
			}
			if (AD_Tree_ID == 0)
				AD_Tree_ID = MTree.getDefaultAD_Tree_ID (
					Env.getCtx().getAD_Client_ID(), m_mTab.getAD_Table_ID());
			if (m_tree != null)
				m_tree.initTree (AD_Tree_ID);
		}
	}	//	activate


	/**
	 *  Register ESC Actions
	 *  - overwrite VTable's Keystroke assignment for ESC
	 *  @param aIgnore ignore
	 */
	public void registerESCAction (AppsAction aIgnore)
	{
		int c = JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
		vTable.getInputMap(c).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), aIgnore.getName());
		vTable.getActionMap().put(aIgnore.getName(), aIgnore);

	//	AEnv.printActionInputMap(vTable);
	}   //  registerESCAction

	/**
	 *  Query Tab and resize Table
	 *  (called from APanel)
	 *  @param onlyCurrentDays how many days back
	 *  @param maxRows maximum rows or 0 for all
	 *  @param created query based on created if true otherwise updated
	 */
	public boolean query (int onlyCurrentDays,
		int maxRows, boolean created)
	{
		boolean success = false;
		//  start loading while building screen
		success = m_mTab.query(onlyCurrentDays, maxRows, created);
		//  Update UI
		if (!isSingleRow())
			vTable.autoSize(true);
		dynamicDisplay(-1);
		return success;
	}   //  query


	/**************************************************************************
	 *  Switch from single to multi & vice versa
	 */
	public void switchRowPresentation()
	{
		stopEditor(true);
		if (m_singleRow)
			switchMultiRow();
		else
			switchSingleRow();
	}   //  switchRowPresentation

	/**
	 *  Switch to SingleRow Presentation
	 */
	public void switchSingleRow()
	{
		if (m_onlyMultiRow)
			return;
		cardLayout.first(cardPanel);
		m_singleRow = true;
		dynamicDisplay(-1);
		vPanel.requestFocus();
	}   //  switchSingleRow

	/**
	 *  Switch to MultiRow Presentation
	 */
	public void switchMultiRow()
	{
		cardLayout.last(cardPanel);
		m_singleRow = false;
		vTable.autoSize(true);	//	resizes
		vTable.requestFocus();
	}   //  switchSingleRow

	/**
	 *  Is Single Row presentation
	 *  @return true if Single Row is displayed
	 */
	public boolean isSingleRow()
	{
		return m_singleRow;
	}   //  isSingleRow

	/**
	 * 	Request Focus In Window
	 *	@return focus request
	 */
	@Override
	public boolean requestFocusInWindow()
	{
		if (m_singleRow && (vPanel != null))
			return vPanel.requestFocusInWindow();
	    return super.requestFocusInWindow();
	}	//	requestFocusInWindow


	/**
	 *	Request Focus - cancel editors
	 */
	@Override
	public void requestFocus()
	{
		if (vTable != null)
			vTable.editingCanceled(new ChangeEvent(this));
		if (m_singleRow && (vPanel != null))
			vPanel.requestFocus();
		else
			super.requestFocus();
	}	//	requestFocus

	/**************************************************************************
	 *  Remove Listener - pass on to MTab
	 *  @param l listener
	 */
	public synchronized void removeDataStatusListener(DataStatusListener l)
	{
		m_mTab.removeDataStatusListener(l);
	}   //  removeDataStatusListener

	/**
	 *  Add Data Status Listener - pass on to MTab
	 *  @param l listener
	 */
	public synchronized void addDataStatusListener(DataStatusListener l)
	{
		m_mTab.addDataStatusListener(l);
	}

	/**
	 *  Data Status Listener - for MTab events.
	 *  <p>
	 *  Callouts are processed here for GUI changes
	 *  - same as in MTab.setValue for batch changes
	 *  <p>
	 *  calls dynamicDisplay
	 *  @param e event
	 */
	public void dataStatusChanged(DataStatusEvent e)
	{
		int col = e.getChangedColumn();
		if (!e.isChanged() || col < 0)
			return;
		log.config("(" + m_mTab + ") Col=" + col + ": " + e.toString());

		//  Process Callout
		GridField mField = m_mTab.getField(col);
		if (mField != null)
		{
			mField.validateValue();
			if (mField.getCallout().length() > 0)
			{
				String msg = m_mTab.processFieldChange(mField);     //  Dependencies & Callout
				if (msg.length() > 0)
					ADialog.error(m_WindowNo, this, msg);
			}
			else	//	no callout to set dependent fields
			{
				String columnName = mField.getColumnName();
				ArrayList<GridField> dependants = m_mTab.getDependantFields(columnName);
				for (int i = 0; i < dependants.size(); i++)
                {
					GridField dep = dependants.get(i);
					if (dep == null)
						continue;
                	Lookup lookup = dep.getLookup();
	                if (lookup == null)
	                	continue;
	                //
                	String val = lookup.getValidation();
                	if (val.indexOf(columnName) != -1)	//	dep is dependent
                	{
                		Object oldValue = lookup.getSelectedItem();
                		boolean mandatory = dep.isMandatory(false);
            			lookup.fillComboBox (mandatory, true, true, false);
                		lookup.setSelectedItemAlways(oldValue);	//	set old value with new rules
                	}
                }	//	for all dependent fields
			}
		}
		dynamicDisplay(col);	//	 -1 = all
	}   //  dataStatusChanged


	/**************************************************************************
	 *  List Selection Listener (VTable) - row changed
	 *  @param e event
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		//  no rows
		if (m_mTab.getRowCount() == 0)
			return;

	//	vTable.stopEditor(graphPanel);
		int rowTable = vTable.getSelectedRow();
		int rowCurrent = m_mTab.getCurrentRow();
		log.config("(" + m_mTab.toString() + ") Row in Table=" + rowTable + ", in Model=" + rowCurrent);

		if (rowTable == -1)  //  nothing selected
		{
			if (rowCurrent >= 0)
			{
				vTable.setRowSelectionInterval(rowCurrent, rowCurrent); //  causes this method to be called again
				return;
			}
		}
		else
		{
			if (rowTable != rowCurrent)
				m_mTab.navigate(rowTable);
			dynamicDisplay(-1);
		}

		//	nothing or initiated by mouse (wait for "real" one)
		if (e.getValueIsAdjusting()){
			return;
		}

		//	TreeNavigation - Synchronize 	-- select node in tree
		if (m_tree != null)
			m_tree.setSelectedNode (m_mTab.getRecord_ID());	//	ignores new (-1)

	//	log.config( "GridController.valueChanged (" + m_mTab.toString() + ") - fini",
	//		"Row in Table=" + rowTable + ", in Model=" + rowCurrent);

		//	Query Included Tab
		if (vIncludedGC != null)
			vIncludedGC.getMTab().query(0, 0, false);
	}   //  valueChanged

	/**
	 *  PropertyChange Listener - Tree Panel - node selection
	 *  @param e event
	 */
	public void propertyChange(PropertyChangeEvent e)
	{
	//	System.out.println("propertyChange");
	//	System.out.println(e);
		if (e == null)
			return;
		Object value = e.getNewValue();
		if (value == null)
			return;
		log.config(e.getPropertyName() + "=" + value
			+ " - " + value.getClass().toString());
		if (!(value instanceof CTreeNode))
			return;

		//  We Have a TreeNode
		int nodeID = ((CTreeNode)value).getNode_ID();
		//  root of tree selected - ignore
		if (nodeID == 0)
			return;

		//  Search all rows for mode id
		int size = m_mTab.getRowCount();
		int row = -1;
		for (int i = 0; i < size; i++)
		{
			if (m_mTab.getKeyID(i) == nodeID)
			{
				row = i;
				break;
			}
		}
		if (row == -1)
		{
			log.log(Level.WARNING, "Tab does not have ID with Node_ID=" + nodeID);
			return;
		}

		//  Navigate to node row
		m_mTab.navigate(row);
	}   //  propertyChange

	/**
	 *  Dynamic Display.
	 *  - Single Row Screen layout and update of dynamic Lookups
	 *  <p>
	 *  Single Row layout:
	 *  the components's name is the ColumnName; if it matches, the
	 *  MField.isDisplayed(true) is used to determine if it is visible
	 *  if the component is a VEditor, setEnabled is set from the MField
	 *  <p>
	 *  Multi Row layout is not changed:
	 *  VCellRenderer calls JTable.isCellEditable -> checks MField.isEditable (Active, isDisplayed)
	 *  VCellEditor.isCellEditable calls MField.isEditable(true) <br>
	 *  If a column is not displayed, the width is set to 0 in dynInit
	 *  <p>
	 *  Dynamic update of data is handeled in VLookup.focusGained/Lost.
	 *  When focus is gained the model is temporarily updated with the
	 *  specific validated data, if lost, it is switched back to the
	 *  unvalidated data (i.e. everything). This allows that the display
	 *  methods have a lookup to display. <br>
	 *  Here: if the changed field has dependents and the dependent
	 *  is a Lookup and this lookup has a dynamic dependence of the changed field,
	 *  the value of that field is set to null (in MTab.processDependencies -
	 *  otherwise it would show an invalid value).
	 *  As Editors listen for value changed of their MField, the display is updated.
	 *  <p>
	 *  Called from GridController.valueChanged/dataStatusChanged, APane;.stateChanged/unlock/cmd_...
	 *  @param col selective column number or -1 if all
	 */
	public void dynamicDisplay (int col)
	{
	//	log.config( "GridController.dynamicDisplay (" + m_mTab.toString() + ") SingleRow=" + isSingleRow() + ", OnlyMultiRow=" + m_onlyMultiRow);
		//	Don't update if multi-row
		if (!isSingleRow() || m_onlyMultiRow)
			return;
		if (!m_mTab.isOpen())
			return;
		//  Selective
		if (col >= 0)
		{
			GridField changedField = m_mTab.getField(col);
			String columnName = changedField.getColumnName();
			ArrayList<GridField> dependants = m_mTab.getDependantFields(columnName);
			log.config(m_mTab.toString() + ": "
				+ columnName + " - Dependents=" + dependants.size());
			//	No Dependents and no Callout - Set just Background
			if ((dependants.size() == 0) && (changedField.getCallout().length() > 0))
			{
				Component[] comp = vPanel.getComponents();
				for (Component element : comp)
				{
					if (columnName.equals(element.getName ()) && (element instanceof VEditor))
					{
						VEditor ve = (VEditor)element;
						ve.setBackground( changedField.isError() );
						break;
					}
				}
				log.fine(m_mTab.toString() + " - fini - " + (col>=0 ? "seletive" : "complete"));
				return;
			}
		}   //  selective


		//  complete single row re-display
		boolean noData = m_mTab.getRowCount() == 0;
		log.config(m_mTab.toString() + " - Rows=" + m_mTab.getRowCount());
		//  All Components in vPanel (Single Row)
		Component[] comps = vPanel.getComponents();
		for (Component element : comps)
		{
			JComponent comp = (JComponent)element;
			String columnName = comp.getName();

			if (columnName != null)
			{
				GridField mField = m_mTab.getField(columnName);
				if (mField != null)
				{
					if (mField.isDisplayed(true))		//  check context
					{
						Boolean vis = (Boolean)comp.getClientProperty(VPanel.COMPONENT_VISIBLE);
						if (!comp.isVisible() && ((vis == null) || vis.booleanValue()))
							comp.setVisible(true);		//  visibility
						if (comp instanceof VEditor)
						{
							VEditor ve = (VEditor)comp;
							if (noData)
								ve.setReadWrite(false);
							else
							{
								boolean rw = mField.isEditable(true);	//  r/w - check Context
								ve.setReadWrite(rw);
							//	log.log(Level.FINEST, "RW=" + rw + " " + mField);
								ve.setMandatory( mField.isMandatory(true) );
								mField.validateValue();
								ve.setBackground( mField.isError() );
							}
						}
					}
					else if (comp.isVisible())
						comp.setVisible(false);

					// reset error status for nondisplayed fields if they are not mandatory
					if (!mField.isDisplayed(true) && !mField.isMandatory(true)){
						mField.setError(false);
					}
				}
			}
		}   //  all components
		log.fine(m_mTab.toString() + " - fini - " + (col>=0 ? "seletive" : "complete"));
	}   //  dynamicDisplay

	/**
	 *  Row Changed - synchronize with Tree
	 *
	 *  @param  save    true the row was saved (changed/added), false if the row was deleted
	 *  @param  keyID   the ID of the row changed
	 */
	public void rowChanged (boolean save, int keyID)
	{
		if ((m_tree == null) || (keyID <= 0))
			return;
		String name = (String)m_mTab.getValue("Name");
		String description = (String)m_mTab.getValue("Description");
		Boolean IsSummary = (Boolean)m_mTab.getValue("IsSummary");
		boolean summary = (IsSummary != null) && IsSummary.booleanValue();
		String imageIndicator = (String)m_mTab.getValue("Action");  //  Menu - Action
		//
		m_tree.nodeChanged(save, keyID, name, description,
			summary, imageIndicator);
	}   //  rowChanged



	/**************************************************************************
	 *  Vetoable Change Listener.
	 * 	Called from VEditor
	 *  <pre>
	 *  - for Save Confirmation dialog
	 *  - for Single Row from VEditor: Update MTable
	 *  </pre>
	 *  @param e event
	 *  @throws PropertyVetoException
	 */
	public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException
	{
		if (m_mTab.isProcessed())		//	only active records
		{
			Object source = e.getSource();
			if (source instanceof VEditor)
			{
				if (!((VEditor)source).isReadWrite())
				{
					log.config("(" + m_mTab.toString() + ") " + e.getPropertyName());
					return;
				}
			}
			else
			{
				log.config("(" + m_mTab.toString() + ") " + e.getPropertyName());
				return;
			}
		}	//	processed
		log.config("(" + m_mTab.toString() + ") "
			+ e.getPropertyName() + "=" + e.getNewValue() + " (" + e.getOldValue() + ") "
			+ (e.getOldValue() == null ? "" : e.getOldValue().getClass().getName()));


		//  Save Confirmation dialog    MTable-RowSave
		if (e.getPropertyName().equals(GridTable.PROPERTY))
		{
			//  throw new PropertyVetoException calls this method (??) again
			if (m_vetoActive)
			{
				m_vetoActive = false;
				return;
			}
			if (!Env.getCtx().isAutoCommit(m_WindowNo) || (m_mTab.getCommitWarning().length() > 0))
			{
				if (!ADialog.ask(m_WindowNo, this, "SaveChanges?", m_mTab.getCommitWarning()))
				{
					m_vetoActive = true;
					throw new PropertyVetoException ("UserDeniedSave", e);
				}
			}
			return;
		}   //  saveConfirmation


		//  Get Row/Col Info
		GridTable mTable = m_mTab.getTableModel();
		int row = m_mTab.getCurrentRow();
		int col = mTable.findColumn(e.getPropertyName());
		//
		// gwu: modified to enforce validation even when the new value is null
		mTable.setValueAt (e.getNewValue(), row, col);	//	-> dataStatusChanged -> dynamicDisplay
		//	Force Callout
		if (e.getPropertyName().equals("S_ResourceAssignment_ID"))
		{
			GridField mField = m_mTab.getField(col);
			if ((mField != null) && (mField.getCallout().length() > 0))
				m_mTab.processFieldChange(mField);     //  Dependencies & Callout
		}

	//	log.config( "GridController.vetoableChange (" + m_mTab.toString() + ") - fini", e.getPropertyName() + "=" + e.getNewValue());
	}   //  vetoableChange


	/**************************************************************************
	 *  Get Model Tab
	 *  @return Model Tab
	 */
	public GridTab getMTab()
	{
		return m_mTab;
	}   //  getMTab

	/**
	 * 	Is Tab displayed
	 *	@return true if displayed
	 */
	public boolean isDisplayed()
	{
		return m_mTab.isDisplayed(false);
	}	//	IsDisplayed

	/**
	 *  Get VTable
	 *  @return VTable
	 */
	public VTable getTable()
	{
		return vTable;
	}   //  getTable


	/**
	 * 	Set Window level Mnemonics
	 *	@param set true if set otherwise unregiser
	 */
	public void setMnemonics (boolean set)
	{
		if (vPanel != null)
			vPanel.setMnemonics(set);
	}	//	setMnemonics

	/**
	 *  Stop Table & SR Editors and move focus to graphPanel
	 *  @param saveValue save value
	 */
	public void stopEditor (boolean saveValue)
	{
		log.config("(" + m_mTab.toString() + ") TableEditing=" + vTable.isEditing());

		//  MultiRow - remove editors
		vTable.stopEditor(saveValue);

		//  SingleRow - stop editors by changing focus
		if (m_singleRow)
			vPanel.transferFocus();
		//	graphPanel.requestFocus();
		//
	//	log.config( "GridController.stopEditor (" + m_mTab.toString() + ") - fini",
	//		"Editing=" + vTable.isEditing());
	}   //  stopEditors

	/**
	 * 	Mouse Clicked
	 *	@param e event
	 */
	public void mouseClicked(MouseEvent e)
	{
		if (CLogMgt.isLevelFinest())
			log.finest("" + this + " - " + e);
	}
	/**
	 * 	Mouse Pressed
	 *	@param e event
	 */
	public void mousePressed(MouseEvent e)
	{
		if (CLogMgt.isLevelFinest())
			log.finest("" + this + " - " + e);
	}
	/**
	 * 	Mouse Released
	 *	@param e event
	 */
	public void mouseReleased(MouseEvent e)
	{
		if (CLogMgt.isLevelFinest())
			log.finest("" + this + " - " + e);
	}
	/**
	 * 	Mouse Entered
	 *	@param e event
	 */
	public void mouseEntered(MouseEvent e)
	{
		if (CLogMgt.isLevelFinest())
			log.finest("" + this + " - " + e);
	}
	/**
	 * 	Mouse Exited
	 *	@param e event
	 */
	public void mouseExited(MouseEvent e)
	{
		if (CLogMgt.isLevelFinest())
			log.finest("" + this + " - " + e);
	}

	/**
	 * 	Get Variable Value
	 *	@param variableName name
	 *	@return value
	 */
	public String get_ValueAsString (String variableName)
	{
		return Env.getCtx().getContext(m_WindowNo, variableName);
	}	//	get_ValueAsString


}   //  GridController
