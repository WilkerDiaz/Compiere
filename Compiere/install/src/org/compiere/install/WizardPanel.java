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

import javax.swing.*;

import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *	Random Test Panel
 *
 * 	@author 	Victor Lew
 * 	@version 	$Id: WizardPanel.java 7904 2009-07-20 22:22:01Z freyes $
 */
public class WizardPanel extends CPanel 
	implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String WELCOME_PAGE = "Welcome";
	private static final String INSTALLATION_DIRECTORY_PAGE = "Installation Directory";
	private static final String APPLICATION_SERVER_PAGE = "Application Server";
	private static final String DATABASE_PAGE = "Database";
	private static final String DATABASE_ADMINISTRATOR_PAGE = "Database Administrator";
	private static final String DATABASE_USER_PAGE = "Database User";
	private static final String SUBSCRIBER_INFO_PAGE = "Subscriber Information";
	private static final String SELECT_COMPONENTS_PAGE = "Select Components";
	private static final String PUBLIC_LICENSE_PAGE = "License Agreement";
	private static final String COMMERCIAL_LICENSE_PAGE = "License Agreement";
	private static final String REVIEW_PAGE = "Review";
	
	private int currentPage = 0;
	private WizardPage pages[] = new WizardPage[14];
	private int pageslength = 0;
	private CLabel titleLabel;
	private CButton buttonBack;
	private CButton buttonNext;
	private CardLayout cardLayout = new CardLayout();

	private static final int FIELDLENGTH = 15;
	
	//  Compiere Home
	private ImageIcon	iOpen = new ImageIcon(ConfigurationPanel.class.getResource("openFile.gif"));
	private CLabel 		lCompiereHome = new CLabel();
	private CTextField 	fCompiereHome = new CTextField(FIELDLENGTH);
	private CButton 	bCompiereHome = new CButton(iOpen);	

	Insets bInsets = new Insets(0, 5, 0, 5);
	//	Apps Server  - Type 
	CLabel lAppsServer = new CLabel();
	CTextField fAppsServer = new CTextField(FIELDLENGTH);
	private CLabel lAppsType = new CLabel();
	CComboBox 	fAppsType = new CComboBox(ConfigurationData.APPSTYPE);	
	//	Deployment Directory - JNP
	private CLabel 		lDeployDir = new CLabel();
	CTextField 	fDeployDir = new CTextField(FIELDLENGTH);
	CButton 	bDeployDir = new CButton(iOpen);
	CLabel lJNPPort = new CLabel();
	CTextField fJNPPort = new CTextField(FIELDLENGTH);
	//	Web Ports
	private CLabel lWebPort = new CLabel();
	CTextField fWebPort = new CTextField(FIELDLENGTH);
	private CLabel lSSLPort = new CLabel();
	CTextField fSSLPort = new CTextField(FIELDLENGTH);

	//  Database
	private CLabel lDatabaseType = new CLabel();
	CComboBox fDatabaseType = new CComboBox(ConfigurationData.DBTYPE);
	CLabel lDatabaseServer = new CLabel();
	CTextField fDatabaseServer = new CTextField(FIELDLENGTH);
	private CLabel lDatabaseName = new CLabel();
	CTextField fDatabaseName = new CTextField(FIELDLENGTH);	
	private CLabel lDatabasePort = new CLabel();
	CTextField fDatabasePort = new CTextField(FIELDLENGTH);
	
	//  Database Administrator
	private CLabel lSystemUser = new CLabel();
	CTextField fSystemUser = new CTextField(FIELDLENGTH);
	private CLabel lSystemPassword = new CLabel();
	CPassword fSystemPassword = new CPassword();
	
	//  Database User
	private CLabel lDatabaseUser = new CLabel();
	CTextField fDatabaseUser = new CTextField(FIELDLENGTH);
	private CLabel lDatabasePassword = new CLabel();
	CPassword fDatabasePassword = new CPassword();

	//  Subscriber Info
	private CLabel lSubscriberUser = new CLabel();
	CTextField fSubscriberUser = new CTextField(FIELDLENGTH);
	private CLabel lSubscriberPassword = new CLabel();
	CPassword fSubscriberPassword = new CPassword();
	
	
	private class WizardPage extends CPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private boolean visibilityNext;
		private boolean visibilityBack;
		private String label;
		
		public WizardPage (String label, boolean visibilityBack, boolean visibilityNext ) {
			this.setLabel(label);
			this.setVisibilityBack(visibilityBack);
			this.setVisibilityNext(visibilityNext);
		}

		public void setVisibilityNext(boolean visibilityNext) {
			this.visibilityNext = visibilityNext;
		}

		public boolean isVisibilityNext() {
			return visibilityNext;
		}

		public void setVisibilityBack(boolean visibilityBack) {
			this.visibilityBack = visibilityBack;
		}

		public boolean isVisibilityBack() {
			return visibilityBack;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}
	/**
	 * 	Constructor
	 *  @param statusBar for info
	 *  @param properties configuration properties
	 */
	public WizardPanel (CLabel title, CButton back, CButton next)
	{
		buttonBack = back;
		buttonNext = next;
		titleLabel = title;
		
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}	//	WizardPanel

	/**	Logger	*/
    private static CLogger log = CLogger.getCLogger(WizardPanel.class);
    
	/**
	 * 	Static Layout Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		this.setLayout(cardLayout);
		addWelcomePage();
		addInstallationDirectoryPage();
		addApplicationServerPage();
		addDatabasePage();
		addDatabaseAdministratorPage();
		addDatabaseUserPage();
		addSubscriberInfoPage();
		addSelectComponentsPage();
		addPublicLicensePage();
		addCommercialLicensePage();
		addReviewPage();
		
		setPage(0);
	}	//	jbInit

	private void setPage(int pageNum)
	{
		currentPage = pageNum;
		buttonBack.setEnabled(pages[pageNum].isVisibilityBack());
		buttonNext.setEnabled(pages[pageNum].isVisibilityNext());
		titleLabel.setText(pages[pageNum].getLabel());
		cardLayout.show(this, pages[pageNum].getLabel());
	}
	
	public void next() 
	{
		if ((currentPage + 1) < pageslength) {
			setPage(currentPage + 1);
		}
	}
	
	public void previous() 
	{
		if (currentPage > 0) {
			setPage(currentPage - 1);
		}		
	}

	private void addWelcomePage() 
	{
		WizardPage page = new WizardPage(WizardPanel.WELCOME_PAGE, false, true);
		page.setLayout(new GridBagLayout());
		page.add(new CLabel("Welcome to the Compiere Setup Wizard."), new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		page.add(new CLabel("Click \"Next\" to continue."), new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		add(page,page.getLabel());
		pages[pageslength] = page;
		pageslength++;
	}

	private void addInstallationDirectoryPage() 
	{
		WizardPage page = new WizardPage(WizardPanel.INSTALLATION_DIRECTORY_PAGE, true, true);
		page.setLayout(new GridBagLayout());
		page.add(new CLabel("Please specify the directory where Compiere will be installed:"), new GridBagConstraints(0, 0, 2, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		lCompiereHome.setToolTipText(Setup.res.getString("CompiereHomeInfo"));
		lCompiereHome.setText(Setup.res.getString("CompiereHome"));
		fCompiereHome.setText(".");
		bCompiereHome.setMargin(bInsets);
		bCompiereHome.setToolTipText(Setup.res.getString("CompiereHomeInfo"));
		bCompiereHome.addActionListener(this);
		
		page.add(lCompiereHome, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		page.add(fCompiereHome, new GridBagConstraints(1, 1, 1, 1, 0.5, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 0), 0, 0));
		page.add(bCompiereHome, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		add(page,page.getLabel());
		pages[pageslength] = page;
		pageslength++;
	}
	
	private void addApplicationServerPage() 
	{
		WizardPage page = new WizardPage(WizardPanel.APPLICATION_SERVER_PAGE, true, true);
		page.setLayout(new GridBagLayout());
		page.add(new CLabel("Please specify the application server that Compiere should use:"), new GridBagConstraints(0, 0, 2, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		lAppsServer.setToolTipText(Setup.res.getString("AppsServerInfo"));
		lAppsServer.setText(Setup.res.getString("AppsServer"));
		lAppsServer.setFont(lAppsServer.getFont().deriveFont(Font.BOLD));
		fAppsServer.setText(".");
		lAppsType.setToolTipText(Setup.res.getString("AppsTypeInfo"));
		lAppsType.setText(Setup.res.getString("AppsType"));
		fAppsType.setPreferredSize(fAppsServer.getPreferredSize());
		page.add(lAppsServer,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 5, 5, 5), 0, 0));
		page.add(fAppsServer,   new GridBagConstraints(1, 1, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 0), 0, 0));
		page.add(lAppsType,     new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 5, 5, 5), 0, 0));
		page.add(fAppsType,     new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 0), 0, 0));
		// 	Deployment - JNP
		lDeployDir.setToolTipText(Setup.res.getString("DeployDirInfo"));
		lDeployDir.setText(Setup.res.getString("DeployDir"));
		fDeployDir.setText(".");
		bDeployDir.setMargin(bInsets);
		bDeployDir.setToolTipText(Setup.res.getString("DeployDirInfo"));
		lJNPPort.setToolTipText(Setup.res.getString("JNPPortInfo"));
		lJNPPort.setText(Setup.res.getString("JNPPort"));
		fJNPPort.setText("1099");
		page.add(lDeployDir,	new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		page.add(fDeployDir,	new GridBagConstraints(1, 3, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		page.add(bDeployDir,    new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		page.add(lJNPPort,      new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		page.add(fJNPPort,      new GridBagConstraints(1, 4, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		//	Web Ports
		lWebPort.setToolTipText(Setup.res.getString("WebPortInfo"));
		lWebPort.setText(Setup.res.getString("WebPort"));
		fWebPort.setText("80");
		lSSLPort.setText("SSL");
		fSSLPort.setText("443");
		page.add(lWebPort,   new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		page.add(fWebPort,   new GridBagConstraints(1, 5, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		page.add(lSSLPort,   new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		page.add(fSSLPort,   new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		bDeployDir.addActionListener(this);
		add(page,page.getLabel());
		pages[pageslength] = page;
		pageslength++;
	}

	private void addDatabasePage() 
	{
		WizardPage page = new WizardPage(WizardPanel.DATABASE_PAGE, true, true);
		page.setLayout(new GridBagLayout());
		page.add(new CLabel("Please specify the database that Compiere should use:"), new GridBagConstraints(0, 0, 2, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		//	Database Server - Type
		lDatabaseServer.setToolTipText(Setup.res.getString("DatabaseServerInfo"));
		lDatabaseServer.setText(Setup.res.getString("DatabaseServer"));
		lDatabaseServer.setFont(lDatabaseServer.getFont().deriveFont(Font.BOLD));
		lDatabaseType.setToolTipText(Setup.res.getString("DatabaseTypeInfo"));
		lDatabaseType.setText(Setup.res.getString("DatabaseType"));
		fDatabaseType.setPreferredSize(fDatabaseServer.getPreferredSize());
		page.add(lDatabaseServer,	new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 5, 5, 5), 0, 0));
		page.add(fDatabaseServer,   new GridBagConstraints(1, 1, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 0), 0, 0));
		page.add(lDatabaseType,		new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 5, 5, 5), 0, 0));
		page.add(fDatabaseType,     new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 0), 0, 0));
		//	DB Name - TNS
		lDatabaseName.setToolTipText(Setup.res.getString("DatabaseNameInfo"));
		lDatabaseName.setText(Setup.res.getString("DatabaseName"));
		fDatabaseName.setText(".");
		page.add(lDatabaseName,		new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		page.add(fDatabaseName,		new GridBagConstraints(1, 3, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		//	Port - System
		lDatabasePort.setToolTipText(Setup.res.getString("DatabasePortInfo"));
		lDatabasePort.setText(Setup.res.getString("DatabasePort"));
		fDatabasePort.setText(".");
		page.add(lDatabasePort,		new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		page.add(fDatabasePort,     new GridBagConstraints(1, 4, 1, 1, 0.5, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
		
		
		add(page,page.getLabel());
		pages[pageslength] = page;
		pageslength++;		
	}
	
	private void addDatabaseAdministratorPage ()
	{
		int rowNum = 0;
		WizardPage page = new WizardPage(WizardPanel.DATABASE_ADMINISTRATOR_PAGE, true, true);
		page.setLayout(new GridBagLayout());
		page.add(new CLabel("Please specify the administrator account for the database:"), new GridBagConstraints(0, rowNum, 2, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		page.add(new CLabel("(This user must have the priviledge to create new users)"), new GridBagConstraints(0, ++rowNum, 2, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		lSystemPassword.setToolTipText(Setup.res.getString("SystemPasswordInfo"));
		lSystemPassword.setText(Setup.res.getString("SystemPassword"));
		fSystemPassword.setText(".");
		//lSystemUser.setToolTipText(Setup.res.getString("SystemUserInfo"));
		//lSystemUser.setText(Setup.res.getString("SystemUser"));
		lSystemUser.setText("System User");
		fSystemUser.setText(".");
		page.add(lSystemUser, new GridBagConstraints(0, ++rowNum, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		page.add(fSystemUser, new GridBagConstraints(1, rowNum, 1, 1, 0.5,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 0), 0, 0));		
		page.add(lSystemPassword, new GridBagConstraints(0, ++rowNum, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		page.add(fSystemPassword, new GridBagConstraints(1, rowNum, 1, 1, 0.5,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 0), 0, 0));
		add(page,page.getLabel());
		pages[pageslength] = page;
		pageslength++;				
	}
	
	private void addDatabaseUserPage()
	{
		int rowNum = 0;
		WizardPage page = new WizardPage(WizardPanel.DATABASE_USER_PAGE, true, true);
		page.setLayout(new GridBagLayout());
		page.add(new CLabel("Please specify the database account Compiere will use to create application data:"), new GridBagConstraints(0, rowNum, 2, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		lDatabasePassword.setToolTipText(Setup.res.getString("DatabasePasswordInfo"));
		lDatabasePassword.setText(Setup.res.getString("DatabasePassword"));
		fDatabasePassword.setText(".");
		lDatabaseUser.setToolTipText(Setup.res.getString("DatabaseUserInfo"));
		lDatabaseUser.setText(Setup.res.getString("DatabaseUser"));
		fDatabaseUser.setText(".");
		page.add(lDatabaseUser, new GridBagConstraints(0, ++rowNum, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		page.add(fDatabaseUser, new GridBagConstraints(1, rowNum, 1, 1, 0.5,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 0), 0, 0));		
		page.add(lDatabasePassword, new GridBagConstraints(0, ++rowNum, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		page.add(fDatabasePassword, new GridBagConstraints(1, rowNum, 1, 1, 0.5,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 0), 0, 0));
		
		add(page,page.getLabel());
		pages[pageslength] = page;
		pageslength++;
	}

	private void addSubscriberInfoPage()
	{
		int rowNum = 0;
		WizardPage page = new WizardPage(WizardPanel.SUBSCRIBER_INFO_PAGE, true, true);
		page.setLayout(new GridBagLayout());
		page.add(new CLabel("Please specify the subscriber information:"), new GridBagConstraints(0, rowNum, 2, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		//lSubscriberPassword.setToolTipText(Setup.res.getString("SubscriberPasswordInfo"));
		//lSubscriberPassword.setText(Setup.res.getString("SubscriberPassword"));
		lSubscriberPassword.setText("Password");
		fSubscriberPassword.setText(".");
		//lSubscriberUser.setToolTipText(Setup.res.getString("SubscriberUserInfo"));
		//lSubscriberUser.setText(Setup.res.getString("SubscriberUser"));
		lSubscriberUser.setText("User");
		fSubscriberUser.setText(".");
		page.add(lSubscriberUser, new GridBagConstraints(0, ++rowNum, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		page.add(fSubscriberUser, new GridBagConstraints(1, rowNum, 1, 1, 0.5,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 0), 0, 0));		
		page.add(lSubscriberPassword, new GridBagConstraints(0, ++rowNum, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		page.add(fSubscriberPassword, new GridBagConstraints(1, rowNum, 1, 1, 0.5,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 0), 0, 0));
		
		add(page,page.getLabel());
		pages[pageslength] = page;
		pageslength++;
	}
	
	private void addSelectComponentsPage()
	{
		WizardPage page = new WizardPage(WizardPanel.SELECT_COMPONENTS_PAGE, true, true);
		page.setLayout(new GridBagLayout());
		
		add(page,page.getLabel());
		pages[pageslength] = page;
		pageslength++;
	}
	
	private void addPublicLicensePage()
	{
		WizardPage page = new WizardPage(WizardPanel.PUBLIC_LICENSE_PAGE, true, true);
		page.setLayout(new GridBagLayout());
		
		add(page,page.getLabel());
		pages[pageslength] = page;
		pageslength++;
	}
	
	private void addCommercialLicensePage()
	{
		WizardPage page = new WizardPage(WizardPanel.COMMERCIAL_LICENSE_PAGE, true, true);
		page.setLayout(new GridBagLayout());
		
		add(page,page.getLabel());
		pages[pageslength] = page;
		pageslength++;
	}
	
	private void addReviewPage()	
	{
		WizardPage page = new WizardPage(WizardPanel.REVIEW_PAGE, true, true);
		page.setLayout(new GridBagLayout());
		
		add(page,page.getLabel());
		pages[pageslength] = page;
		pageslength++;
	}
	
	
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
	 * 	ActionListener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bCompiereHome)
			setPath (fCompiereHome); 
		else if (e.getSource() == bDeployDir)
				setPath (fDeployDir);
	}	//	actionPerformed

}	//	WizardPanel
