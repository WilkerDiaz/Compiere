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
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.compiere.apps.*;
import org.compiere.apps.form.*;
import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.grid.ed.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;
import org.compiere.wf.*;

/**
 *	WorkFlow Activities Panel.
 *	Stand alone or in Menu
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: WFActivity.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class WFActivity extends CPanel 
implements FormPanel, ActionListener, ListSelectionListener
{
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * 	WF Activity
	 */
	public WFActivity()
	{
		super ();
		//	needs to call init
	}	//	WFActivity

	/**
	 * 	WF Activity
	 * @param menu AMenu
	 */
	public WFActivity (AMenu menu)
	{
		super ();
		log.config("");
		try 
		{
			dynInit(0);
			jbInit();
		}
		catch(Exception e) 
		{
			log.log(Level.SEVERE, "", e);
		}
		m_menu = menu;
	}	//	WFActivity


	/**	Window No					*/
	protected int         		m_WindowNo = 0;
	/**	FormFrame					*/
	private FormFrame 			m_frame = null;
	/**	Menu						*/
	protected AMenu 				m_menu = null;
	/**	Open Activities				*/
	protected ArrayList<MWFActivity> 		m_activities = null;
	/**	Current Activity			*/
	protected MWFActivity 		m_activity = null;
	/**	Set Column					*/
	protected	MColumn 			m_column = null; 
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(WFActivity.class);

	protected DefaultTableModel 	selTableModel = new DefaultTableModel(
			new String[]{Msg.translate(Env.getCtx(), "Priority"),
					Msg.translate(Env.getCtx(), "AD_WF_Node_ID"),
					Msg.translate(Env.getCtx(), "Summary"), ""}, 0); 
	protected CTable 		selTable = new CTable(false, selTableModel);
	protected CScrollPane selPane = new CScrollPane(selTable);
	//
	protected CPanel centerPanel = new CPanel();
	protected GridBagLayout centerLayout = new GridBagLayout();
	private CLabel lNode = new CLabel(Msg.translate(Env.getCtx(), "AD_WF_Node_ID"));
	private CTextField fNode = new CTextField();
	private CLabel lDesctiption = new CLabel(Msg.translate(Env.getCtx(), "Description"));
	private CTextArea fDescription = new CTextArea();
	private CLabel lHelp = new CLabel(Msg.translate(Env.getCtx(), "Help"));
	private CTextArea fHelp = new CTextArea();
	private CLabel lHistory = new CLabel(Msg.translate(Env.getCtx(), "History"));
	private CTextPane fHistory = new CTextPane();
	protected CLabel lAnswer = new CLabel(Msg.getMsg(Env.getCtx(), "Answer"));
	private CPanel answers = new CPanel(new FlowLayout(FlowLayout.LEADING));
	protected CTextField fAnswerText = new CTextField();
	protected CComboBox fAnswerList = new CComboBox();
	private CButton fAnswerButton = new CButton();
	protected CButton bZoom = AEnv.getButton("Zoom");
	private CLabel lTextMsg = new CLabel(Msg.getMsg(Env.getCtx(), "Messages"));
	protected CTextArea fTextMsg = new CTextArea();
	protected CButton bOK = ConfirmPanel.createOKButton(true);
	protected VLookup fForward = null;	//	dynInit
	private CLabel lForward = new CLabel(Msg.getMsg(Env.getCtx(), "Forward"));
	private CLabel lOptional = new CLabel("(" + Msg.translate(Env.getCtx(), "Optional") + ")");
	private StatusBar statusBar = new StatusBar(); 

	/**
	 * 	Dynamic Init.
	 * 	Called before Static Init
	 * 	@param WindowNo window
	 */
	protected void dynInit(int WindowNo)
	{
		loadActivities();
		//	Forward
		fForward = VLookup.createUser(WindowNo);
	}	//	dynInit

	/**
	 * 	Static Init.
	 * 	Called after Dynamic Init
	 * 	@throws Exception
	 */
	protected void jbInit () throws Exception
	{
		int width = 150;
		centerPanel.setLayout (centerLayout);
		fNode.setReadWrite (false);
		fDescription.setReadWrite (false);
		fDescription.setPreferredSize(new Dimension (width,40));
		fHelp.setReadWrite (false);
		fHelp.setPreferredSize(new Dimension (width,40));
		fHistory.setReadWrite (false);
		fHistory.setPreferredSize(new Dimension (width,80));
		fTextMsg.setPreferredSize(new Dimension (width,40));
		//
		selTable.getSelectionModel().addListSelectionListener(this);
		// hide id column
		TableColumn col = selTable.getColumnModel().getColumn(3);
		col.setWidth(0); col.setMinWidth(0); col.setMaxWidth(0);

		bZoom.addActionListener(this);
		bOK.addActionListener(this);
		//
		this.setLayout(new BorderLayout());
		this.add (centerPanel, BorderLayout.CENTER);
		this.add (statusBar, BorderLayout.SOUTH);
		//
		//	answers.setOpaque(false);
		answers.add(fAnswerText);
		answers.add(fAnswerList);
		answers.add(fAnswerButton);
		fAnswerButton.setIcon(Env.getImageIcon("mWindow.gif"));
		fAnswerButton.addActionListener(this);
		//
		int row = 0;
		selPane.setPreferredSize(new Dimension(width, 60));
		selPane.setMinimumSize(new Dimension(100, 60));
		centerPanel.add (selPane, new GridBagConstraints (0, row, 4, 1, 0.3, 0.3, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, 
				new Insets (5, 10, 5, 10), 0, 0));

		centerPanel.add (lNode, new GridBagConstraints (0, ++row, 1, 1, 0.0, 0.0, 
				GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, 
				new Insets (5, 10, 5, 5), 0, 0));
		centerPanel.add (fNode, new GridBagConstraints (1, row, 3, 2, 0.5, 0.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, 
				new Insets (5,	0, 5, 10), 0, 0));

		centerPanel.add (lDesctiption, new GridBagConstraints (0, ++row, 1, 1, 0.0, 0.0, 
				GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
				new Insets (5, 10, 5, 5), 0, 0));
		centerPanel.add (fDescription, new GridBagConstraints (1, row, 3, 1, 0.0, 0.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, 
				new Insets (5, 0, 5, 10), 0, 0));

		centerPanel.add (lHelp, new GridBagConstraints (0, ++row, 1, 1, 0.0, 0.0, 
				GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, 
				new Insets (2, 10, 5, 5), 0, 0));
		centerPanel.add (fHelp, new GridBagConstraints (1, row, 3, 1, 0.0, 0.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, 
				new Insets (2, 0, 5, 10), 0, 0));

		centerPanel.add (lHistory, new GridBagConstraints (0, ++row, 1, 1, 0.0, 0.0, 
				GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, 
				new Insets (5, 10, 5, 5), 0, 0));
		centerPanel.add (fHistory, new GridBagConstraints (1, row, 3, 1, 0.5, 0.5, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, 
				new Insets (5,	0, 5, 10), 0, 0));

		centerPanel.add (lAnswer, new GridBagConstraints (0, ++row, 1, 1, 0.0, 0.0, 
				GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets (10, 10, 5, 5), 0, 0));
		centerPanel.add (answers, new GridBagConstraints (1, row, 2, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
				new Insets (10,	0, 5, 5), 0, 0));
		centerPanel.add (bZoom, new GridBagConstraints (3, row, 1, 1, 0.0, 0.0, 
				GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets (10,	0, 10, 10), 0, 0));

		centerPanel.add (lTextMsg, new GridBagConstraints (0, ++row, 1, 1, 0.0, 0.0, 
				GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, 
				new Insets (5, 10, 5, 5), 0, 0));
		centerPanel.add (fTextMsg, new GridBagConstraints (1, row, 3, 1, 0.5, 0.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, 
				new Insets (5, 0, 5, 10), 0, 0));

		centerPanel.add (lForward, new GridBagConstraints (0, ++row, 1, 1, 0.0, 0.0, 
				GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets (10, 10,	5, 5), 0, 0));
		centerPanel.add (fForward, new GridBagConstraints (1, row, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets (10, 0, 5, 0), 0, 0));
		centerPanel.add (lOptional, new GridBagConstraints (2, row, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets (10,	5, 5, 5), 0, 0));
		centerPanel.add (bOK, new GridBagConstraints (3, row, 1, 1, 0.0, 0.0, 
				GridBagConstraints.EAST, GridBagConstraints.NONE, 
				new Insets (10,	5, 5, 10), 0, 0));
	}	//	jbInit

	/**
	 *	Initialize Panel for FormPanel
	 *  @param WindowNo window
	 *  @param frame frame
	 *	@see org.compiere.apps.form.FormPanel#init(int, FormFrame)
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		m_WindowNo = WindowNo;
		m_frame = frame;
		//
		log.info("");
		try
		{
			dynInit(WindowNo);
			jbInit();
			//
			//	this.setPreferredSize(new Dimension (400,400));
			frame.getContentPane().add(this, BorderLayout.CENTER);
			display(-1);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}	//	init

	/**
	 * 	Dispose
	 * 	@see org.compiere.apps.form.FormPanel#dispose()
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose


	/**
	 * 	(Re) Load Activities
	 * 	@return int
	 */
	public int loadActivities()
	{
		while (selTableModel.getRowCount() > 0)
			selTableModel.removeRow(0);	
		long start = System.currentTimeMillis();
		m_activities = new ArrayList<MWFActivity>();
		String sql = "SELECT * FROM AD_WF_Activity a "
			+ "WHERE a.Processed='N' AND a.WFState='OS' AND a.AD_Client_ID=? AND (" //#1
			//	Owner of Activity
			+ " a.AD_User_ID=?"	//	#2
			//	Invoker (if no invoker = all)
			+ " OR EXISTS (SELECT * FROM AD_WF_Responsible r WHERE a.AD_WF_Responsible_ID=r.AD_WF_Responsible_ID"
			+ " AND COALESCE(r.AD_User_ID,0)=0 AND (a.AD_User_ID=? OR a.AD_User_ID IS NULL))"	//	#3
			// Responsible User
			+ " OR EXISTS (SELECT * FROM AD_WF_Responsible r WHERE a.AD_WF_Responsible_ID=r.AD_WF_Responsible_ID"
			+ " AND r.AD_User_ID=? AND r.responsibletype = 'H')"		//	#4
			//	Responsible Role
			+ " OR EXISTS (SELECT * FROM AD_WF_Responsible r INNER JOIN AD_User_Roles ur ON (r.AD_Role_ID=ur.AD_Role_ID)"
			+ " WHERE a.AD_WF_Responsible_ID=r.AD_WF_Responsible_ID AND ur.AD_User_ID=? AND r.responsibletype = 'R')"	//	#5
			//
			+ ") ORDER BY a.Priority DESC, Created";
		int AD_User_ID = Env.getCtx().getAD_User_ID();
		int AD_Client_ID = Env.getCtx().getAD_Client_ID();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_Client_ID);
			pstmt.setInt (2, AD_User_ID);
			pstmt.setInt (3, AD_User_ID);
			pstmt.setInt (4, AD_User_ID);
			pstmt.setInt (5, AD_User_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MWFActivity activity = new MWFActivity(Env.getCtx(), rs, null);
				m_activities.add (activity);
				Object[] rowData = new Object[4];
				rowData[0] = activity.getPriority();
				rowData[1] = activity.getNodeName();
				rowData[2] = activity.getSummary();
				rowData[3] = activity.get_ID();

				selTableModel.addRow(rowData);
				if (m_activities.size() > 200)		//	HARDCODED
				{
					log.warning("More then 200 Activities - ignored");
					break;
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		selTable.autoSize(false);
		log.config("#" + m_activities.size() 
				+ "(" + (System.currentTimeMillis()-start) + "ms)");
		return m_activities.size();
	}	//	loadActivities

	/**
	 * 	Display.
	 * 	@param index index of table
	 * 	Fill Editors
	 */
	public void display(int index)
	{
		log.fine("Index=" + index);
		m_activity = resetDisplay(index);
		if (m_activity == null)
			return;

		//	Display Activity
		fNode.setText (m_activity.getNodeName());
		fDescription.setText (m_activity.getNodeDescription());
		fHelp.setText (m_activity.getNodeHelp());
		//
		fHistory.setText (m_activity.getHistoryHTML());

		//	User Actions
		MWFNode node = m_activity.getNode();
		if (X_AD_WF_Node.ACTION_UserChoice.equals(node.getAction()))
		{
			if (m_column == null)
				m_column = node.getColumn();
			if (m_column != null && m_column.get_ID() != 0)
			{
				int dt = m_column.getAD_Reference_ID();
				if (dt == DisplayTypeConstants.YesNo)
				{
					ValueNamePair[] values = MRefList.getList(Env.getCtx(), 319, false);		//	_YesNo
					fAnswerList.setModel(new DefaultComboBoxModel(values));
					fAnswerList.setVisible(true);
				}
				else if (dt == DisplayTypeConstants.List)
				{
					ValueNamePair[] values = MRefList.getList(Env.getCtx(), m_column.getAD_Reference_Value_ID(), false);
					fAnswerList.setModel(new DefaultComboBoxModel(values));
					fAnswerList.setVisible(true);
				}
				else	//	other display types come here
				{
					fAnswerText.setText ("");
					fAnswerText.setVisible(true);
				}
			}
		}
		//	--
		else if (X_AD_WF_Node.ACTION_UserWindow.equals(node.getAction())
				|| X_AD_WF_Node.ACTION_UserForm.equals(node.getAction()))
		{
			fAnswerButton.setText(node.getName());
			fAnswerButton.setToolTipText(node.getDescription());
			fAnswerButton.setVisible(true);
		}
		else if (X_AD_WF_Node.ACTION_UserWorkbench.equals(node.getAction()))
			log.log(Level.SEVERE, "Workflow Action not implemented yet");
		else
			log.log(Level.SEVERE, "Unknown Node Action: " + node.getAction());
		//
		statusBar.setStatusDB((index+1) + "/" + m_activities.size());
		statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "WFActivities"));
	}	//	display

	/**
	 * 	Reset Display
	 *	@param selIndex select index
	 *	@return selected activity
	 */
	private MWFActivity resetDisplay(int selIndex)
	{
		fAnswerText.setVisible(false);
		fAnswerList.setVisible(false);
		fAnswerButton.setVisible(false);
		fTextMsg.setReadWrite(selIndex >= 0);
		fTextMsg.setText("");
		bZoom.setEnabled(selIndex >= 0);
		bOK.setEnabled(selIndex >= 0);
		fForward.setValue(null);
		fForward.setEnabled(selIndex >= 0);
		//
		statusBar.setStatusDB(String.valueOf(selIndex+1) + "/" + m_activities.size());
		m_activity = null;
		m_column = null;
		if (m_activities.size() > 0)
		{
			if (selIndex >= 0 && selIndex < m_activities.size())
				m_activity = m_activities.get(selIndex);
		}
		//	Nothing to show
		if (m_activity == null)
		{
			fNode.setText ("");
			fDescription.setText ("");
			fHelp.setText ("");
			fHistory.setText ("");
			statusBar.setStatusDB("0/0");
			statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "WFNoActivities"));
		}
		return m_activity;
	}	//	resetDisplay


	/**
	 * 	Selection Listener
	 * 	@param e event
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		int m_index = -1;
		//find index based on activity id; 
		// note: m_activities should have been a hashmap for performance
		int row = selTable.getSelectedRow();
		if (row>=0){
			int id = ((Integer) selTableModel.getValueAt(row, 3)).intValue();
			for (int i = 0; i < m_activities.size(); i++) {
				MWFActivity act = m_activities.get(i);
				if (id ==  act.get_ID()) {
					m_index = i;
					break;
				}
			}
		}
		if (m_index >= 0)
			display(m_index);
	}	//	valueChanged


	/**
	 * 	Action Listener
	 *	@param e event
	 * 	@see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed (ActionEvent e)
	{
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		//
		if (e.getSource() == bZoom)
			cmd_zoom();
		else if (e.getSource() == bOK)
			cmd_OK();
		else if (e.getSource() == fAnswerButton)
			cmd_button();
		//
		this.setCursor(Cursor.getDefaultCursor());
	}	//	actionPerformed


	/**
	 * 	Zoom
	 */
	private void cmd_zoom()
	{
		log.config("Activity=" + m_activity);
		if (m_activity == null)
			return;
		AEnv.zoom(m_activity.getAD_Table_ID(), m_activity.getRecord_ID());
	}	//	cmd_zoom

	/**
	 * 	Answer Button
	 */
	private void cmd_button()
	{
		log.config("Activity=" + m_activity);
		if (m_activity == null)
			return;
		//
		MWFNode node = m_activity.getNode();
		if (X_AD_WF_Node.ACTION_UserWindow.equals(node.getAction()))
		{
			int AD_Window_ID = node.getAD_Window_ID();		// Explicit Window
			String ColumnName = m_activity.getPO().get_TableName() + "_ID";
			int Record_ID = m_activity.getRecord_ID();
			Query query = Query.getEqualQuery(ColumnName, Record_ID);
			boolean IsSOTrx = m_activity.isSOTrx();
			//
			log.info("Zoom to AD_Window_ID=" + AD_Window_ID 
					+ " - " + query + " (IsSOTrx=" + IsSOTrx + ")");
			AWindow frame = new AWindow();
			if (!frame.initWindow(AD_Window_ID, query))
				return;
			AEnv.showCenterScreen(frame);
			frame = null;
		}
		else if (X_AD_WF_Node.ACTION_UserForm.equals(node.getAction()))
		{
			int AD_Form_ID = node.getAD_Form_ID();
			FormFrame ff = new FormFrame();
			ff.openForm(AD_Form_ID);
			ff.pack();
			AEnv.showCenterScreen(ff);
		}
		else if (X_AD_WF_Node.ACTION_UserWorkbench.equals(node.getAction()))
		{

		}
		else
			log.log(Level.SEVERE, "No User Action:" + node.getAction());
	}	//	cmd_button


	/**
	 * 	Save
	 */
	private void cmd_OK()
	{
		log.config("Activity=" + m_activity);
		if (m_activity == null)
			return;
		int AD_User_ID = Env.getCtx().getAD_User_ID();
		String textMsg = fTextMsg.getText();
		//
		MWFNode node = m_activity.getNode();

		Object forward = fForward.getValue();
		if (forward != null)
		{
			log.config("Forward to " + forward);
			int fw = ((Integer)forward).intValue();
			if (fw == AD_User_ID || fw == 0)
			{
				log.log(Level.SEVERE, "Forward User=" + fw);
				return;
			}
			if (!m_activity.forwardTo(fw, textMsg))
			{
				ADialog.error(m_WindowNo, this, "CannotForward");
				return;
			}
		}
		//	User Choice - Answer
		else if (X_AD_WF_Node.ACTION_UserChoice.equals(node.getAction()))
		{
			if (m_column == null)
				m_column = node.getColumn();
			//	Do we have an answer?
			int dt = m_column.getAD_Reference_ID();
			String value = fAnswerText.getText();
			if (dt == DisplayTypeConstants.YesNo || dt == DisplayTypeConstants.List)
			{
				ValueNamePair pp = (ValueNamePair)fAnswerList.getSelectedItem();
				value = pp.getValue();
			}
			if (value == null || value.length() == 0)
			{
				ADialog.error(m_WindowNo, this, "FillMandatory", Msg.getMsg(Env.getCtx(), "Answer"));
				return;
			}
			//
			log.config("Answer=" + value + " - " + textMsg);
			try
			{
				m_activity.setUserChoice(AD_User_ID, value, dt, textMsg);
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, node.getName(), e);
				ADialog.error(m_WindowNo, this, "Error", e.toString());
				return;
			}
		}
		//	User Action
		else
		{
			log.config("Action=" + node.getAction() + " - " + textMsg);
			try
			{
				m_activity.setUserConfirmation(AD_User_ID, textMsg);
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, node.getName(), e);
				ADialog.error(m_WindowNo, this, "Error", e.toString());
				return;
			}

		}
		//	Next
		if (m_menu != null)
			m_menu.updateInfo();
		else
			loadActivities();
		display(-1);
	}	//	cmd_OK


}	//	WFActivity
