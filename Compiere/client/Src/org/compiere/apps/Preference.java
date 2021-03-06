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
package org.compiere.apps;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.compiere.db.*;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.print.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Customize settings like L&F, AutoCommit, etc. & Diagnostics
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: Preference.java,v 1.2 2006/07/30 00:51:27 jjanke Exp $
 */
public final class Preference extends CDialog
	implements ActionListener, ListSelectionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Standard Constructor
	 *  @param frame frame
	 *  @param WindowNo window
	 */
	public Preference(Frame frame, int WindowNo)
	{
		super(frame, Msg.getMsg(Env.getCtx(), "Preference"), true);
		log.config("Preference");
		try
		{
			jbInit();
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, ex.getMessage());
		}
		load();
		//
		StringBuffer sta = new StringBuffer("#");
		sta.append(Env.getCtx().size()).append(" - ")
			.append(Msg.translate(Env.getCtx(), "AD_Window_ID"))
			.append("=").append(WindowNo);
		statusBar.setStatusLine(sta.toString());
		statusBar.setStatusDB("");
		AEnv.positionCenterWindow(frame, this);
	}	//	Preference

	/**	Logger				*/
	private static CLogger log = CLogger.getCLogger(Preference.class);
	/** User Preference		*/
	private MUserPreference	m_preference = null;
	
	private CPanel panel = new CPanel();
	private BorderLayout panelLayout = new BorderLayout();
	private CTabbedPane tabPane = new CTabbedPane();
	private CPanel customizePane = new CPanel();
	private CPanel contextPane = new CPanel();
	private GridBagLayout customizeLayout = new GridBagLayout();
	private CCheckBox autoCommit = new CCheckBox();
	private CCheckBox autoNew = new CCheckBox();
	private CCheckBox printPreview = new CCheckBox();
	private CPanel southPanel = new CPanel();
	private BorderLayout southLayout = new BorderLayout();
	private BorderLayout icontextLayout = new BorderLayout();
	private JList infoList = new JList();
	private JScrollPane contextListScrollPane = new JScrollPane(infoList);
	private CPanel contextSouthPanel = new CPanel();
	private CTextArea contextHeader = new CTextArea(4,15);
	private CTextArea contextDetail = new CTextArea(4,35);
	private CTextArea infoArea = new CTextArea(5, 30);
	private BorderLayout contextSouthLayout = new BorderLayout();
	private StatusBar statusBar = new StatusBar();
	private ConfirmPanel confirm = new ConfirmPanel(true);
	private CComboBox traceLevel = new CComboBox(CLogMgt.LEVELS);
	private CLabel traceLabel = new CLabel();
	private CCheckBox traceFile = new CCheckBox();
	private CCheckBox autoLogin = new CCheckBox();
	private CCheckBox compiereSys = new CCheckBox();
	private CCheckBox storePassword = new CCheckBox();
	private CCheckBox showTrl = new CCheckBox();
	private CCheckBox showAcct = new CCheckBox();
	private CCheckBox showAdvanced = new CCheckBox();
	private CCheckBox cacheWindow = new CCheckBox();
	private CButton uiTheme = new CButton();
	private CLabel lPrinter = new CLabel();
	private CPrinter fPrinter = new CPrinter();
	private CLabel lDate = new CLabel();
	private VDate fDate = new VDate();
	private CComboBox connectionProfile = new CComboBox(CConnection.CONNECTIONProfiles);
	private CLabel connectionProfileLabel = new CLabel();
	private CPanel errorPane = new CPanel();
	private BorderLayout errorLayout = new BorderLayout();
	private JScrollPane errorScrollPane = new JScrollPane();
	private MiniTable errorTable = new MiniTable();
	private CPanel errorPanel = new CPanel(new FlowLayout(FlowLayout.TRAILING));
	private CToggleButton bErrorsOnly = new CToggleButton(Msg.getMsg(Env.getCtx(), "ErrorsOnly"));
	private CButton bErrorReset = new CButton(Msg.getMsg(Env.getCtx(), "Reset"));
	private CButton bErrorEMail = new CButton(Msg.getMsg(Env.getCtx(), "SendEMail"));
	private CButton bErrorSave = new CButton(Msg.getMsg(Env.getCtx(), "SaveFile"));
	private CButton bRoleInfo = new CButton(Msg.translate(Env.getCtx(), "AD_Role_ID"));

	/**
	 *	Static Init.
	 *  <pre>
	 *  - panel
	 *      - tabPane
	 *          - customizePane
	 *              - infoArea
	 *              - fields ...
	 *          - contextPane
	 *              - contextList
	 *              - contextSouthPanel
	 *                  - contextHeader
	 *                  - contextDetail
	 * 			- errorPane
	 * 				- errorScollPane
	 * 					- errorTable
	 *      - southPanel
	 *  </pre>
	 *  @throws Exception
	 */
	void jbInit() throws Exception
	{
		traceLabel.setRequestFocusEnabled(false);
		traceLabel.setText(Msg.getMsg(Env.getCtx(), "TraceLevel", true));
		traceLabel.setToolTipText(Msg.getMsg(Env.getCtx(), "TraceLevel", false));
		traceFile.setText(Msg.getMsg(Env.getCtx(), "TraceFile", true));
		traceFile.setToolTipText(Msg.getMsg(Env.getCtx(), "TraceFile", false));

		uiTheme.setText(Msg.getMsg(Env.getCtx(), "UITheme", true));
		uiTheme.setToolTipText(Msg.getMsg(Env.getCtx(), "UITheme", false));
		autoCommit.setText(Msg.getMsg(Env.getCtx(), "AutoCommit", true));
		autoCommit.setToolTipText(Msg.getMsg(Env.getCtx(), "AutoCommit", false));
		autoNew.setText(Msg.getMsg(Env.getCtx(), "AutoNew", true));
		autoNew.setToolTipText(Msg.getMsg(Env.getCtx(), "AutoNew", false));
		compiereSys.setText(Msg.getMsg(Env.getCtx(), "CompiereSys", true));
		compiereSys.setToolTipText(Msg.getMsg(Env.getCtx(), "CompiereSys", false));
		printPreview.setText(Msg.getMsg(Env.getCtx(), "AlwaysPrintPreview", true));
		printPreview.setToolTipText(Msg.getMsg(Env.getCtx(), "AlwaysPrintPreview", false));
		autoLogin.setText(Msg.getMsg(Env.getCtx(), "AutoLogin", true));
		autoLogin.setToolTipText(Msg.getMsg(Env.getCtx(), "AutoLogin", false));
		storePassword.setText(Msg.getMsg(Env.getCtx(), "StorePassword", true));
		storePassword.setToolTipText(Msg.getMsg(Env.getCtx(), "StorePassword", false));
		showTrl.setText(Msg.getMsg(Env.getCtx(), "ShowTrlTab", true));
		showTrl.setToolTipText(Msg.getMsg(Env.getCtx(), "ShowTrlTab", false));
		showAcct.setText(Msg.getMsg(Env.getCtx(), "ShowAcctTab", true));
		showAcct.setToolTipText(Msg.getMsg(Env.getCtx(), "ShowAcctTab", false));
		showAdvanced.setText(Msg.getMsg(Env.getCtx(), "ShowAdvancedTab", true));
		showAdvanced.setToolTipText(Msg.getMsg(Env.getCtx(), "ShowAdvancedTab", false));
		connectionProfileLabel.setText(Msg.getElement(Env.getCtx(), "ConnectionProfile"));
		cacheWindow.setText(Msg.getMsg(Env.getCtx(), "CacheWindow", true));
		cacheWindow.setToolTipText(Msg.getMsg(Env.getCtx(), "CacheWindow", false));
		lPrinter.setText(Msg.getMsg(Env.getCtx(), "Printer"));
		lDate.setText(Msg.getMsg(Env.getCtx(), "Date"));
		infoArea.setReadWrite(false);
		getContentPane().add(panel);
		panel.setLayout(panelLayout);
		panel.add(tabPane, BorderLayout.CENTER);
		//	Customize
//		tabPane.add(customizePane,  Msg.getMsg(Env.getCtx(), "Preference"));
		tabPane.add(customizePane,  Msg.getMsg(Env.getCtx(), "Preference"));
		customizePane.setLayout(customizeLayout);
		customizePane.add(infoArea,          new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		customizePane.add(uiTheme,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		customizePane.add(bRoleInfo,    new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

		customizePane.add(autoCommit,     new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		customizePane.add(compiereSys,     new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0			
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		customizePane.add(autoLogin,      new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		customizePane.add(storePassword,     new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		customizePane.add(showAcct,     new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		customizePane.add(showTrl,   new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		customizePane.add(showAdvanced,	new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		customizePane.add(autoNew,     new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0			
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		customizePane.add(connectionProfileLabel,    new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		customizePane.add(connectionProfile,    new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		customizePane.add(cacheWindow,     new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0			
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

		customizePane.add(traceLabel,    new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
		customizePane.add(traceLevel,       new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		customizePane.add(traceFile,       new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		customizePane.add(lPrinter,     new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		customizePane.add(fPrinter,         new GridBagConstraints(1, 8, 2, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		customizePane.add(lDate,       new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		customizePane.add(fDate,        new GridBagConstraints(1, 9, 2, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		customizePane.add(printPreview,    new GridBagConstraints(2, 9, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//	Info
//		tabPane.add(contextPane,  Msg.getMsg(Env.getCtx(), "Context"));
		tabPane.add(contextPane,  Msg.getMsg(Env.getCtx(), "Context"));
		contextPane.setLayout(icontextLayout);
		contextPane.add(contextListScrollPane, BorderLayout.CENTER);
		contextListScrollPane.setPreferredSize(new Dimension(200, 300));
		infoList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		infoList.setBackground(CompierePLAF.getFieldBackground_Inactive());
		infoList.addListSelectionListener(this);
		infoList.setFixedCellWidth(30);
		contextPane.add(contextSouthPanel, BorderLayout.SOUTH);
		contextSouthPanel.setLayout(contextSouthLayout);
		contextSouthPanel.add(contextHeader, BorderLayout.WEST);
		contextHeader.setBackground(SystemColor.info);
		contextHeader.setReadWrite(false);
		contextHeader.setLineWrap(true);
		contextHeader.setWrapStyleWord(true);
		contextHeader.setBorder(BorderFactory.createLoweredBevelBorder());
		contextSouthPanel.add(contextDetail, BorderLayout.CENTER);
		contextDetail.setBackground(SystemColor.info);
		contextDetail.setReadWrite(false);
		contextDetail.setLineWrap(true);
		contextDetail.setWrapStyleWord(true);
		contextDetail.setBorder(BorderFactory.createLoweredBevelBorder());
		//	Error Pane
		errorPane.setLayout(errorLayout);
//		tabPane.add(errorPane,  Msg.getMsg(Env.getCtx(), "Errors"));
		tabPane.add(errorPane,  "Errors");
		errorPane.add(errorScrollPane, BorderLayout.CENTER);
		errorScrollPane.getViewport().add(errorTable, null);
		//
		errorPanel.add(bErrorsOnly);
		errorPanel.add(bErrorReset);
		errorPanel.add(bErrorEMail);
		errorPanel.add(bErrorSave);
		errorPane.add(errorPanel, BorderLayout.SOUTH);
		//	South
		panel.add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(southLayout);
		southPanel.add(statusBar, BorderLayout.SOUTH);
		southPanel.add(confirm, BorderLayout.CENTER);
		//
		bRoleInfo.addActionListener(this);
		confirm.addActionListener(this);
	}	//	jbInit


	/**
	 *	List Selection Listener - show info in header/detail fields
	 *  @param e evant
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting())
			return;

		String value = (String)infoList.getSelectedValue();
		if (value == null)
			return;
		int pos = value.indexOf("==");
		if (pos == -1)
		{
			contextHeader.setText("");
			contextDetail.setText(value);
		}
		else
		{
			contextHeader.setText(value.substring(0, pos).replace('|','\n'));
			contextDetail.setText(value.substring(pos+3));
		}
	}	//	valueChanged


	/**
	 *	ActionListener
	 *  @param e event
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		//	UI Change
		if (e.getSource() == uiTheme)
		{
			new CompierePLAFEditor(this, false);
		}
		else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
			dispose();
		else if (e.getActionCommand().equals(ConfirmPanel.A_OK))
			cmd_save();
		//
		else if (e.getSource() == bErrorsOnly)
			cmd_displayErrors();
		else if (e.getSource() == bErrorReset)
			cmd_errorReset();
		else if (e.getSource() == bErrorEMail)
			cmd_errorEMail();
		else if (e.getSource() == bErrorSave)
			cmd_errorSave();
		//
		else if (e.getSource() == bRoleInfo)
			ADialog.info(0, this, "RoleInfo", MRole.getDefault().toStringX(Env.getCtx()));
	}	//	actionPerformed


	/**
	 *	Load Settings - and Context
	 */
	private void load()
	{
		MUser user = MUser.get(Env.getCtx());
		m_preference = user.getPreference();
		log.config(user.toString());
		//
		infoArea.setText(CLogMgt.getInfo(Env.getCtx(), null).toString());
		infoArea.setCaretPosition(0);

		//	--	Load Settings	--
		//	UI
		uiTheme.addActionListener(this);
		//	AutoCommit
		autoCommit.setSelected(Env.getCtx().isAutoCommit());
		autoNew.setSelected(Env.getCtx().isAutoNew());
		//	CompiereSys
		compiereSys.setSelected(Ini.isPropertyBool(Ini.P_COMPIERESYS));
		if (Env.getCtx().getAD_Client_ID() > 20)
		{
			compiereSys.setSelected(false);
			compiereSys.setEnabled(false);
		}
		//	AutoLogin
		autoLogin.setSelected(Ini.isPropertyBool(Ini.P_A_LOGIN));
		//	Save Password
		storePassword.setSelected(Ini.isPropertyBool(Ini.P_STORE_PWD));
		//	Show Acct Tab
		if (MRole.getDefault().isShowAcct())
			showAcct.setSelected(m_preference.isShowAcct());
		else
		{
			showAcct.setSelected(false);
			showAcct.setReadWrite(false);
		}
		//	Show Trl/Advanced Tab
		showTrl.setSelected(m_preference.isShowTrl());
		showAdvanced.setSelected(m_preference.isShowAdvanced());
		
		//  Connection Profile
		String cp = user.getConnectionProfile();
		if (cp == null)
			cp = MRole.getDefault().getConnectionProfile();
		if (cp != null)
		{
			CConnection.get().setConnectionProfile(cp);
			connectionProfile.setReadWrite(false);
		}
		connectionProfile.setSelectedItem(CConnection.get().getConnectionProfilePair());
		cacheWindow.setSelected(Ini.isCacheWindow());
		
		//  Print Preview
		printPreview.setSelected(Ini.isPropertyBool(Ini.P_PRINTPREVIEW));

		//	TraceLevel
		traceLevel.setSelectedItem(CLogMgt.getLevel());
		traceFile.setSelected(Ini.isPropertyBool(Ini.P_TRACEFILE));
		//  Printer
		fPrinter.setValue(Env.getCtx().getPrinterName());
		//  Date
		fDate.setValue(new Timestamp(Env.getCtx().getContextAsTime("#Date")));

		//	--	Load and sort Context	--
		String[] context = Env.getCtx().getEntireContext();
		infoList.setListData(context);

		//	Load Errors
	//	CLogMgt mgt = new CLogMgt();		//	creates test trace
		bErrorsOnly.setSelected(true);
		errorTable.setCellSelectionEnabled(true);
		cmd_displayErrors();
	//	for (int i = 2; i < 6; i++)
	//		errorTable.setColumnReadOnly(i, false);
		//
		bErrorsOnly.addActionListener(this);
		bErrorReset.addActionListener(this);
		bErrorSave.addActionListener(this);
		bErrorEMail.addActionListener(this);
	}	//	load

	/**
	 *	Save Settings
	 */
	private void cmd_save()
	{
		log.config("");
		//  UI
		//	AutoCommit
		Ctx ctx = Env.getCtx();
		m_preference.setIsAutoCommit(autoCommit.isSelected());
		ctx.setAutoCommit(autoCommit.isSelected());
		Ini.setProperty(Ini.P_A_NEW, (autoNew.isSelected()));
		ctx.setAutoNew(autoNew.isSelected());
		//	CompiereSys
		Ini.setProperty(Ini.P_COMPIERESYS, compiereSys.isSelected());
		//	AutoLogin
		Ini.setProperty(Ini.P_A_LOGIN, (autoLogin.isSelected()));
		//	Save Password
		Ini.setProperty(Ini.P_STORE_PWD, (storePassword.isSelected()));
		//	Show Acct Tab
		m_preference.setIsShowAcct(showAcct.isSelected());
		ctx.setContext("#ShowAcct", (showAcct.isSelected()));
		//	Show Trl Tab
		m_preference.setIsShowTrl(showTrl.isSelected());
		ctx.setContext("#ShowTrl", (showTrl.isSelected()));
		//	Show Advanced Tab
		m_preference.setIsShowAdvanced(showAdvanced.isSelected());
		ctx.setContext("#ShowAdvanced", (showAdvanced.isSelected()));
		
		//  ConnectionProfile
		ValueNamePair ppNew = (ValueNamePair)connectionProfile.getSelectedItem();
		String cpNew = ppNew.getValue();
		String cpOld = CConnection.get().getConnectionProfile(); 
		CConnection.get().setConnectionProfile(cpNew);
		if (!cpNew.equals(cpOld)
			&& (cpNew.equals(CConnection.PROFILE_WAN) || cpOld.equals(CConnection.PROFILE_WAN))) 
			ADialog.info(0, this, "ConnectionProfileChange");
		Ini.setProperty(Ini.P_CACHE_WINDOW, cacheWindow.isSelected());
		
		//  Print Preview
		Ini.setProperty(Ini.P_PRINTPREVIEW, (printPreview.isSelected()));
		//	TraceLevel/File
		Level level = (Level)traceLevel.getSelectedItem();
		CLogMgt.setLevel(level);
		Ini.setProperty(Ini.P_TRACELEVEL, level.getName());
		Ini.setProperty(Ini.P_TRACEFILE, traceFile.isSelected());
		//  Printer
		String printer = (String)fPrinter.getSelectedItem();
		ctx.setPrinterName(printer);
		m_preference.setPrinterName(printer);
		//  Date (remove seconds)
		java.sql.Timestamp ts = (java.sql.Timestamp)fDate.getValue();
		if (ts != null)
			ctx.setContext("#Date", ts);

		Ini.saveProperties(Ini.isClient());
		m_preference.save();
		dispose();
	}	//	cmd_save
	
	/**
	 * 	(Re)Display Errors
	 */
	private void cmd_displayErrors()
	{
		Vector<Vector<Object>> data = CLogErrorBuffer.get(true).getLogData(bErrorsOnly.isSelected());
		Vector<String> columnNames = CLogErrorBuffer.get(true).getColumnNames(Env.getCtx());
		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		errorTable.setModel(model);
		//
		if (bErrorsOnly.isSelected())
			tabPane.setTitleAt(2, Msg.getMsg(Env.getCtx(), "Errors") + " (" + data.size() + ")");
		else
			tabPane.setTitleAt(2, Msg.getMsg(Env.getCtx(), "TraceInfo") + " (" + data.size() + ")");
		errorTable.autoSize();
	}	//	cmd_errorsOnly
	
	/**
	 * 	Reset Errors
	 */
	private void cmd_errorReset()
	{
		CLogErrorBuffer.get(true).resetBuffer(bErrorsOnly.isSelected());
		cmd_displayErrors();
	}	//	cmd_errorReset

	/**
	 * 	EMail Errors
	 */
	private void cmd_errorEMail()
	{
		new EMailDialog(this, 
						"EMail Trace", 
						MUser.get(Env.getCtx()), 
						"",			//	to 
						"Compiere Trace Info", 
						CLogErrorBuffer.get(true).getErrorInfo(Env.getCtx(), bErrorsOnly.isSelected()), 
						null);
		
	}	//	cmd_errorEMail
	
	/**
	 * 	Save Error to File
	 */
	private void cmd_errorSave()
	{
	    JFileChooser chooser = new JFileChooser();
	    chooser.setDialogType(JFileChooser.SAVE_DIALOG);
	    chooser.setDialogTitle("Compiere Trace File");
	    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    chooser.setSelectedFile(new File ("traceInfo.log"));
	    int returnVal = chooser.showSaveDialog(this);
	    if(returnVal != JFileChooser.APPROVE_OPTION)
	    	return;
	    try
	    {
	    	File file = chooser.getSelectedFile();
	    	FileWriter writer = new FileWriter(file);
	    	writer.write(CLogErrorBuffer.get(true).getErrorInfo(Env.getCtx(), bErrorsOnly.isSelected()));
	    	writer.flush();
	    	writer.close();
	    }
	    catch (Exception e)
	    {
	    	log.log(Level.SEVERE, "", e);
	    }
	}	//	cmd_errorSave
		
}	//	Preference
