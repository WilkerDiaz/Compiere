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
package org.compiere.install;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import org.compiere.apps.*;
import org.compiere.apps.SwingWorker;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *	Configuration Panel
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ConfigurationPanel.java 7904 2009-07-20 22:22:01Z freyes $
 */
public class ConfigurationPanel extends CPanel 
	implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Constructor
	 *  @param statusBar for info
	 *  @param properties configuration properties
	 */
	public ConfigurationPanel (JLabel statusBar, Properties properties)
	{
		m_statusBar = statusBar;
		m_data = new ConfigurationData(this, properties);
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}	//	ConfigurationPanel

	/**	Logger	*/
    private static CLogger log = CLogger.getCLogger(ConfigurationPanel.class);
    
	/** Error Info				*/
	String				m_errorString;
	/** Test Success			*/
	volatile boolean	m_success = false;
	/** Sync					*/
	volatile boolean	m_testing = false;

	/** Status Bar				*/
	JLabel 				m_statusBar;
	/**	Configuration Data		*/
	private ConfigurationData	m_data;

	private static ImageIcon iOpen = new ImageIcon(ConfigurationPanel.class.getResource("openFile.gif"));

	//	-------------	Static UI
	private GridBagLayout gridBagLayout = new GridBagLayout();
	private static final int	FIELDLENGTH = 15;
	//	Java
	private CLabel 		lJavaHome = new CLabel();
	CTextField 	fJavaHome = new CTextField(FIELDLENGTH);
	CCheckBox 	okJavaHome = new CCheckBox();
	private CButton 	bJavaHome = new CButton(iOpen);
	private CLabel 		lJavaType = new CLabel();
	CComboBox 	fJavaType = new CComboBox(ConfigurationData.JAVATYPE);
	//	Compiere - KeyStore
	private CLabel 		lCompiereHome = new CLabel();
	CTextField 	fCompiereHome = new CTextField(FIELDLENGTH);
	CCheckBox 	okCompiereHome = new CCheckBox();
	private CButton 	bCompiereHome = new CButton(iOpen);
	private CLabel 		lKeyStore = new CLabel();
	CPassword 	fKeyStore = new CPassword();
	CCheckBox 	okKeyStore = new CCheckBox();
	//	Apps Server  - Type 
	CLabel lAppsServer = new CLabel();
	CTextField fAppsServer = new CTextField(FIELDLENGTH);
	CCheckBox okAppsServer = new CCheckBox();
	private CLabel lAppsType = new CLabel();
	CComboBox 	fAppsType = new CComboBox(ConfigurationData.APPSTYPE);
	//	Deployment Directory - JNP
	private CLabel 		lDeployDir = new CLabel();
	CTextField 	fDeployDir = new CTextField(FIELDLENGTH);
	CCheckBox 	okDeployDir = new CCheckBox();
	CButton 	bDeployDir = new CButton(iOpen);
	CLabel lJNPPort = new CLabel();
	CTextField fJNPPort = new CTextField(FIELDLENGTH);
	CCheckBox okJNPPort = new CCheckBox();
	//	Web Ports
	private CLabel lWebPort = new CLabel();
	CTextField fWebPort = new CTextField(FIELDLENGTH);
	CCheckBox okWebPort = new CCheckBox();
	private CLabel lSSLPort = new CLabel();
	CTextField fSSLPort = new CTextField(FIELDLENGTH);
	CCheckBox okSSLPort = new CCheckBox();
	//	Database
	private CLabel lDatabaseType = new CLabel();
	CComboBox fDatabaseType = new CComboBox(ConfigurationData.DBTYPE);
	//
	CLabel lDatabaseServer = new CLabel();
	CTextField fDatabaseServer = new CTextField(FIELDLENGTH);
	private CLabel lDatabaseName = new CLabel();
	CTextField fDatabaseName = new CTextField(FIELDLENGTH);
	CLabel lDatabaseDiscovered = new CLabel();
	CComboBox fDatabaseDiscovered = new CComboBox();
	private CLabel lDatabasePort = new CLabel();
	CTextField fDatabasePort = new CTextField(FIELDLENGTH);
	private CLabel lSystemPassword = new CLabel();
	CPassword fSystemPassword = new CPassword();
	private CLabel lDatabaseUser = new CLabel();
	CTextField fDatabaseUser = new CTextField(FIELDLENGTH);
	private CLabel lDatabasePassword = new CLabel();
	CPassword fDatabasePassword = new CPassword();
	CCheckBox okDatabaseServer = new CCheckBox();
	CCheckBox okDatabaseUser = new CCheckBox();
	CCheckBox okDatabaseSystem = new CCheckBox();
	CCheckBox okDatabaseSQL = new CCheckBox();
	//
	CLabel lMailServer = new CLabel();
	CTextField fMailServer = new CTextField(FIELDLENGTH);
	private CLabel lAdminEMail = new CLabel();
	CTextField fAdminEMail = new CTextField(FIELDLENGTH);
	private CLabel lMailUser = new CLabel();
	CTextField fMailUser = new CTextField(FIELDLENGTH);
	private CLabel lMailPassword = new CLabel();
	CPassword fMailPassword = new CPassword();
	CCheckBox okMailServer = new CCheckBox();
	CCheckBox okMailUser = new CCheckBox();
	//
	private CButton bHelp = ConfirmPanel.createHelpButton("");
	CButton bTest = ConfirmPanel.createCustomizeButton("");
	private CButton bSave = ConfirmPanel.createSaveButton("");
	

	/**
	 * 	Static Layout Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		this.setLayout(gridBagLayout);
		Insets bInsets = new Insets(0, 5, 0, 5);

		// Row number of the panel
		int rowNum = 0;
		
		//	Java
		lJavaHome.setToolTipText(Setup.res.getString("JavaHomeInfo"));
		lJavaHome.setText(Setup.res.getString("JavaHome"));
		fJavaHome.setText(".");
		okJavaHome.setReadWrite(false);
		okJavaHome.setOpaque(true);
		bJavaHome.setMargin(bInsets);
		bJavaHome.setToolTipText(Setup.res.getString("JavaHomeInfo"));
		lJavaType.setToolTipText(Setup.res.getString("JavaTypeInfo"));
		lJavaType.setText(Setup.res.getString("JavaType"));
		fJavaType.setPreferredSize(fJavaHome.getPreferredSize());
		this.add(lJavaHome,    new GridBagConstraints(0, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(fJavaHome,    new GridBagConstraints(1, rowNum, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		this.add(okJavaHome,   new GridBagConstraints(2, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		this.add(bJavaHome,    new GridBagConstraints(3, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(lJavaType,    new GridBagConstraints(4, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(fJavaType,    new GridBagConstraints(5, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		//	CompiereHome - KeyStore
		lCompiereHome.setToolTipText(Setup.res.getString("CompiereHomeInfo"));
		lCompiereHome.setText(Setup.res.getString("CompiereHome"));
		fCompiereHome.setText(".");
		okCompiereHome.setReadWrite(false); 
		okCompiereHome.setOpaque(true);
		bCompiereHome.setMargin(bInsets);
		bCompiereHome.setToolTipText(Setup.res.getString("CompiereHomeInfo"));
		lKeyStore.setText(Setup.res.getString("KeyStorePassword"));
		lKeyStore.setToolTipText(Setup.res.getString("KeyStorePasswordInfo"));
		fKeyStore.setText("");
		okKeyStore.setReadWrite(false);
		okKeyStore.setOpaque(true);
		this.add(lCompiereHome,		new GridBagConstraints(0, ++rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(fCompiereHome,		new GridBagConstraints(1, rowNum, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		this.add(okCompiereHome,	new GridBagConstraints(2, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		this.add(bCompiereHome,     new GridBagConstraints(3, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(lKeyStore,  		new GridBagConstraints(4, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(fKeyStore,  		new GridBagConstraints(5, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		this.add(okKeyStore,  		new GridBagConstraints(6, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//	Apps Server - Type
		lAppsServer.setToolTipText(Setup.res.getString("AppsServerInfo"));
		lAppsServer.setText(Setup.res.getString("AppsServer"));
		lAppsServer.setFont(lAppsServer.getFont().deriveFont(Font.BOLD));
		fAppsServer.setText(".");
		okAppsServer.setReadWrite(false); 
		okAppsServer.setOpaque(true);
		lAppsType.setToolTipText(Setup.res.getString("AppsTypeInfo"));
		lAppsType.setText(Setup.res.getString("AppsType"));
		fAppsType.setPreferredSize(fAppsServer.getPreferredSize());
		this.add(lAppsServer,   new GridBagConstraints(0, ++rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 5, 5, 5), 0, 0));
		this.add(fAppsServer,   new GridBagConstraints(1, rowNum, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 0), 0, 0));
		this.add(okAppsServer,  new GridBagConstraints(2, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 5, 5), 0, 0));
		this.add(lAppsType,     new GridBagConstraints(4, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 5, 5, 5), 0, 0));
		this.add(fAppsType,     new GridBagConstraints(5, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 0), 0, 0));
		// 	Deployment - JNP
		lDeployDir.setToolTipText(Setup.res.getString("DeployDirInfo"));
		lDeployDir.setText(Setup.res.getString("DeployDir"));
		fDeployDir.setText(".");
		okDeployDir.setReadWrite(false); 
		okDeployDir.setOpaque(true);
		bDeployDir.setMargin(bInsets);
		bDeployDir.setToolTipText(Setup.res.getString("DeployDirInfo"));
		lJNPPort.setToolTipText(Setup.res.getString("JNPPortInfo"));
		lJNPPort.setText(Setup.res.getString("JNPPort"));
		fJNPPort.setText(".");
		okJNPPort.setReadWrite(false);
		okJNPPort.setOpaque(true);
		this.add(lDeployDir,	new GridBagConstraints(0, ++rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(fDeployDir,	new GridBagConstraints(1, rowNum, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		this.add(okDeployDir,	new GridBagConstraints(2, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		this.add(bDeployDir,    new GridBagConstraints(3, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(lJNPPort,      new GridBagConstraints(4, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(fJNPPort,      new GridBagConstraints(5, rowNum, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		this.add(okJNPPort,     new GridBagConstraints(6, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		//	Web Ports
		lWebPort.setToolTipText(Setup.res.getString("WebPortInfo"));
		lWebPort.setText(Setup.res.getString("WebPort"));
		fWebPort.setText(".");
		okWebPort.setReadWrite(false);
		okWebPort.setOpaque(true);
		lSSLPort.setText("SSL");
		fSSLPort.setText(".");
		okSSLPort.setReadWrite(false); 
		okSSLPort.setOpaque(true);
		this.add(lWebPort,   new GridBagConstraints(0, ++rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(fWebPort,   new GridBagConstraints(1, rowNum, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		this.add(okWebPort,  new GridBagConstraints(2, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		this.add(lSSLPort,   new GridBagConstraints(4, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(fSSLPort,   new GridBagConstraints(5, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		this.add(okSSLPort,  new GridBagConstraints(6, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		//	Database Server - Type
		lDatabaseServer.setToolTipText(Setup.res.getString("DatabaseServerInfo"));
		lDatabaseServer.setText(Setup.res.getString("DatabaseServer"));
		lDatabaseServer.setFont(lDatabaseServer.getFont().deriveFont(Font.BOLD));
		okDatabaseServer.setReadWrite(false); 
		okDatabaseServer.setOpaque(true);
		lDatabaseType.setToolTipText(Setup.res.getString("DatabaseTypeInfo"));
		lDatabaseType.setText(Setup.res.getString("DatabaseType"));
		fDatabaseType.setPreferredSize(fDatabaseServer.getPreferredSize());
		this.add(lDatabaseServer,	new GridBagConstraints(0, ++rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 5, 5, 5), 0, 0));
		this.add(fDatabaseServer,   new GridBagConstraints(1, rowNum, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 0), 0, 0));
		this.add(okDatabaseServer,  new GridBagConstraints(2, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 5, 5), 0, 0));
		this.add(lDatabaseType,		new GridBagConstraints(4, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 5, 5, 5), 0, 0));
		this.add(fDatabaseType,     new GridBagConstraints(5, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 0), 0, 0));
		//	DB Name - TNS
		lDatabaseName.setToolTipText(Setup.res.getString("DatabaseNameInfo"));
		lDatabaseName.setText(Setup.res.getString("DatabaseName"));
		fDatabaseName.setText(".");
		lDatabaseDiscovered.setToolTipText(Setup.res.getString("TNSNameInfo"));
		lDatabaseDiscovered.setText(Setup.res.getString("TNSName"));
		fDatabaseDiscovered.setEditable(true);
		fDatabaseDiscovered.setPreferredSize(fDatabaseName.getPreferredSize());
		okDatabaseSQL.setReadWrite(false);
		okDatabaseSQL.setOpaque(true);
		this.add(lDatabaseName,		new GridBagConstraints(0, ++rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(fDatabaseName,		new GridBagConstraints(1, rowNum, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
	//	this.add(okDatabaseSQL, 		new GridBagConstraints(2, rowNum, 1, 1, 0.0, 0.0
	//		,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		this.add(lDatabaseDiscovered,  	new GridBagConstraints(4, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		this.add(fDatabaseDiscovered,  	new GridBagConstraints(5, rowNum, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		//	Port - System
		lDatabasePort.setToolTipText(Setup.res.getString("DatabasePortInfo"));
		lDatabasePort.setText(Setup.res.getString("DatabasePort"));
		fDatabasePort.setText(".");
		lSystemPassword.setToolTipText(Setup.res.getString("SystemPasswordInfo"));
		lSystemPassword.setText(Setup.res.getString("SystemPassword"));
		fSystemPassword.setText(".");
		okDatabaseSystem.setReadWrite(false);
		okDatabaseSystem.setOpaque(true);
		this.add(lDatabasePort,		new GridBagConstraints(0, ++rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(fDatabasePort,     new GridBagConstraints(1, rowNum, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		this.add(lSystemPassword,   new GridBagConstraints(4, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(fSystemPassword,   new GridBagConstraints(5, rowNum, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		this.add(okDatabaseSystem,	new GridBagConstraints(6, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));

		//	User - Password
		lDatabaseUser.setToolTipText(Setup.res.getString("DatabaseUserInfo"));
		lDatabaseUser.setText(Setup.res.getString("DatabaseUser"));
		fDatabaseUser.setText(".");
		lDatabasePassword.setToolTipText(Setup.res.getString("DatabasePasswordInfo"));
		lDatabasePassword.setText(Setup.res.getString("DatabasePassword"));
		fDatabasePassword.setText(".");
		okDatabaseUser.setReadWrite(false);
		okDatabaseUser.setOpaque(true);
		this.add(lDatabaseUser,     new GridBagConstraints(0, ++rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(fDatabaseUser,		new GridBagConstraints(1, rowNum, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		this.add(lDatabasePassword, new GridBagConstraints(4, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(fDatabasePassword, new GridBagConstraints(5, rowNum, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		this.add(okDatabaseUser,	new GridBagConstraints(6, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));

		//	Mail Server - Email
		lMailServer.setToolTipText(Setup.res.getString("MailServerInfo"));
		lMailServer.setText(Setup.res.getString("MailServer"));
		lMailServer.setFont(lMailServer.getFont().deriveFont(Font.BOLD));
		fMailServer.setText(".");
		lAdminEMail.setToolTipText(Setup.res.getString("AdminEMailInfo"));
		lAdminEMail.setText(Setup.res.getString("AdminEMail"));
		fAdminEMail.setText(".");
		okMailServer.setReadWrite(false); 
		okMailServer.setOpaque(true);
		this.add(lMailServer,   new GridBagConstraints(0, ++rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 5, 5, 5), 0, 0));
		this.add(fMailServer,   new GridBagConstraints(1, rowNum, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 0), 0, 0));
		this.add(okMailServer,	new GridBagConstraints(2, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 5, 5), 0, 0));
		this.add(lAdminEMail,   new GridBagConstraints(4, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 5, 5, 5), 0, 0));
		this.add(fAdminEMail,   new GridBagConstraints(5, rowNum, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 0), 0, 0));
		
		//	Mail User = Password
		lMailUser.setToolTipText(Setup.res.getString("MailUserInfo"));
		lMailUser.setText(Setup.res.getString("MailUser"));
		fMailUser.setText(".");
		lMailPassword.setToolTipText(Setup.res.getString("MailPasswordInfo"));
		lMailPassword.setText(Setup.res.getString("MailPassword"));
		fMailPassword.setText(".");
		okMailUser.setReadWrite(false);
		okMailUser.setOpaque(true);
		this.add(lMailUser,		new GridBagConstraints(0, ++rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(fMailUser,     new GridBagConstraints(1, rowNum, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		this.add(lMailPassword, new GridBagConstraints(4, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(fMailPassword, new GridBagConstraints(5, rowNum, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		this.add(okMailUser,	new GridBagConstraints(6, rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		//	End
		bTest.setToolTipText(Setup.res.getString("TestInfo"));
		bTest.setText(Setup.res.getString("Test"));
		bSave.setToolTipText(Setup.res.getString("SaveInfo"));
		bSave.setText(Setup.res.getString("Save"));
		bHelp.setToolTipText(Setup.res.getString("HelpInfo"));
		this.add(bTest,    		new GridBagConstraints(0, ++rowNum, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 5, 10, 5), 0, 0));
		this.add(bHelp,         new GridBagConstraints(3, rowNum, 2, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 5, 10, 5), 0, 0));
		this.add(bSave,         new GridBagConstraints(5, rowNum, 2, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 5, 10, 5), 0, 0));
		//
		bCompiereHome.addActionListener(this);
		bJavaHome.addActionListener(this);
		bDeployDir.addActionListener(this);
		fJavaType.addActionListener(this);
		fAppsType.addActionListener(this);
		fDatabaseType.addActionListener(this);
		fDatabaseDiscovered.addActionListener(this);
		bHelp.addActionListener(this);
		bTest.addActionListener(this);
		bSave.addActionListener(this);
	}	//	jbInit

	/**
	 * 	Dynamic Initial.
	 * 	Called by Setup
	 *	@return true if success
	 */
	public boolean dynInit()
	{
		return m_data.load();
	}	//	dynInit

	/**
	 * 	Set Status Bar Text
	 *	@param text text
	 */
	protected void setStatusBar(String text)
	{
		m_statusBar.setText(text);
	}	//	setStatusBar
	
	
	/**************************************************************************
	 * 	ActionListener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (m_testing)
			return;
		//	TNS Name Changed
		if (e.getSource() == fDatabaseDiscovered)
		{
			String dbName = (String)fDatabaseDiscovered.getSelectedItem();
			if (dbName != null && dbName.length() > 0)
				fDatabaseName.setText(dbName);
		}
		//
		else if (e.getSource() == fJavaType)
			m_data.initJava();
		else if (e.getSource() == fAppsType)
			m_data.initAppsServer();
		else if (e.getSource() == fDatabaseType)
			m_data.initDatabase("");
		//
		else if (e.getSource() == bJavaHome)
			setPath (fJavaHome);
		else if (e.getSource() == bCompiereHome)
			setPath (fCompiereHome);
		else if (e.getSource() == bDeployDir)
			setPath (fDeployDir);
		//
		else if (e.getSource() == bHelp)
			new Setup_Help((Frame)SwingUtilities.getWindowAncestor(this));
		else if (e.getSource() == bTest)
			startTest(false);
		else if (e.getSource() == bSave)
			startTest(true);
		
	}	//	actionPerformed

	
	/**
	 * 	Set Path in Field
	 * 	@param field field to set Path
	 */
	private void setPath (CTextField field)
	{
		JFileChooser fc = new JFileChooser(field.getText());
		fc.setDialogType(JFileChooser.OPEN_DIALOG);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setDialogTitle(field.getToolTipText());
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			String dirName = fc.getSelectedFile().getAbsolutePath();
			field.setText(dirName);
		}
	}	//	setPath

	
	/**************************************************************************
	 * 	Start Test Async.
	 * 	@param saveIt save
	 *  @return SwingWorker
	 */
	private SwingWorker startTest(final boolean saveIt)
	{
		SwingWorker worker = new SwingWorker()
		{
			//	Start it
			@Override
			public Object construct()
			{
				m_testing = true;
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				bTest.setEnabled(false);
				m_success = false;
				m_errorString = null;
				try
				{
					test(saveIt);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
					m_errorString += "\n" + ex.toString();
				}
				//
				setCursor(Cursor.getDefaultCursor());
				if (m_errorString == null)
					m_success = true;
				bTest.setEnabled(true);
				m_testing = false;
				return new Boolean(m_success);
			}
			//	Finish it
			@Override
			public void finished()
			{
				if (m_errorString != null)
				{
					log.severe(m_errorString);
					JOptionPane.showConfirmDialog (m_statusBar.getParent(), 
						m_errorString, 
						Setup.res.getString("ServerError"),
						JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
				}
				else if (saveIt)
					save();
			}
		};
		worker.start();
		return worker;
	}	//	startIt

	/**
	 *	Test it
	 *	@param finalIteration final run
	 * 	@throws Exception
	 */
	void test(boolean finalIteration) throws Exception
	{
		bSave.setEnabled(false);
		if (!m_data.test(finalIteration))
			return;
		//
		m_statusBar.setText(Setup.res.getString("Ok"));
		bSave.setEnabled(true);
		m_errorString = null;
	}	//	test

	/**
	 * 	UI Signal OK
	 *	@param cb ckeck box
	 *	@param resString resource string key
	 *	@param pass true if test passed
	 *	@param critical true if critial
	 *	@param errorMsg error Message
	 */
	void signalOK (CCheckBox cb, String resString, 
		boolean pass, boolean critical, String errorMsg)
	{
		m_errorString = Setup.res.getString(resString);
		cb.setSelected(pass);
		if (pass)
			cb.setToolTipText(null);
		else
		{
			cb.setToolTipText(errorMsg);
			m_errorString += " \n(" + errorMsg + ")";
		}
		if (!pass && critical)
			cb.setBackground(Color.RED);
		else
			cb.setBackground(Color.GREEN);
	}	//	setOK


	/**************************************************************************
	 * 	Save Settings.
	 * 	Called from startTest.finished()
	 */
	void save()
	{
		if (!m_success)
			return;

		bSave.setEnabled(false);
		bTest.setEnabled(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		if (!m_data.save())
			return;
		
		firePropertyChange(Setup.SWITCHCARDS, Setup.CONFIGURATION, Setup.COMPONENTS);
	}	//	save

}	//	ConfigurationPanel
