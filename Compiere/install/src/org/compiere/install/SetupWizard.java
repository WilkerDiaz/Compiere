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
import java.beans.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import org.compiere.*;
import org.compiere.apps.*;
import org.compiere.images.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Compiere Setup Wizard Frame.
 *
 * 	@author 	Victor Lew
 * 	@version 	$Id: SetupWizard.java 7904 2009-07-20 22:22:01Z freyes $
 */
public class SetupWizard extends CFrame 
	implements ActionListener, PropertyChangeListener
{
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Constructor
	 */
	public SetupWizard()
	{
		log.info(Compiere.getSummaryAscii());
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				AEnv.showCenterScreen(quitDialog);
			}
		});
		setResizable(false);
		//
		CLogErrorBuffer eb = CLogErrorBuffer.get(true);
		if (eb != null && eb.isIssueError())
			eb.setIssueError(false);
		//
	//	addWindowListener(this);
		try
		{
			res.getString("CompiereServerSetup");
		}
		catch(Exception e)
		{
			System.err.print("Resource not found - use different language - " + e.toString());
			System.exit(1);
		}
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}

		/** Init Panel			**/
		AEnv.showCenterScreen(this);
		try
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			AEnv.positionCenterScreen(this);
			setCursor(Cursor.getDefaultCursor());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}	//	SetupWizard

	/**	Logger	*/
    private static CLogger log = CLogger.getCLogger(Setup.class);
	/**	Sync 				*/
	private Object m_info = new Object();
	

	//	Static UI
	static public ResourceBundle res = ResourceBundle.getBundle("org.compiere.translate.SetupRes");
	//  Menu
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menuFile = new JMenu();
	CMenuItem menuFileExit = new CMenuItem();
	private JMenu menuHelp = new JMenu();
	private CMenuItem menuHelpInfo = new CMenuItem();
	//  Title Bar
	private CLabel titleLabel = new CLabel();
	private CLabel titleIcon = new CLabel();
	//  Buttons
	private CButton buttonBack = new CButton("< Back");
	private CButton buttonNext = new CButton("Next >");
	private CButton buttonCancel = new CButton("Cancel");
	private CButton buttonHelp = new CButton("Help");
	//  Card Layout
	private WizardPanel wizardPanel = new WizardPanel(titleLabel, buttonBack, buttonNext);

	//  Quit Dialog
	private CDialog quitDialog = new CDialog(this, "Quit Compiere Setup?", true);
	
	/** Environment Properties	*/
	protected Properties		p_properties = new Properties();

	/**
	 * 	Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		this.setIconImage(Compiere.getImage16());
		this.setTitle(res.getString("CompiereServerSetup") + " - " + Compiere.MAIN_VERSION);
		//
		JPanel contentPane = (JPanel) this.getContentPane();
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setHgap(5);
		borderLayout.setVgap(5);
		contentPane.setLayout(borderLayout);

		//  Quit Dialog
		final JOptionPane optionPane = new JOptionPane(
                "Do you want to quit Compiere Setup?\n",
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION);
		
		quitDialog.setContentPane(optionPane);
		quitDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		quitDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				if (we.getSource() instanceof CDialog) {
					CDialog source = (CDialog) we.getSource();
					source.setVisible(false);
				}
			}
		});
		optionPane.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				String prop = e.getPropertyName();

				if (quitDialog.isVisible() && (e.getSource() == optionPane)
						&& (prop.equals(JOptionPane.VALUE_PROPERTY))) {
					int value = ((Integer) optionPane.getValue()).intValue();
					if (value == JOptionPane.YES_OPTION) {
						System.exit(0);
					} else if (value == JOptionPane.NO_OPTION) {
						quitDialog.setVisible(false);
						optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
					}
				}
			}
		});
		
		//	Menu
		menuFile.setText(res.getString("File"));
		menuFileExit.setText(res.getString("Exit"));
		menuFileExit.addActionListener(this);
		menuHelp.setText(res.getString("Help"));
		menuHelpInfo.setText(res.getString("Help"));
		menuHelpInfo.addActionListener(this);
		menuFile.add(menuFileExit);
		menuHelp.add(menuHelpInfo);
		menuBar.add(menuFile);
		menuBar.add(menuHelp);

		this.setJMenuBar(menuBar);

		//  Title bar
		titleLabel.setFont(new Font("Default", Font.BOLD, 30));
		titleIcon.setIcon(ImageFactory.getImageIcon("Compiere150x32.png"));
		CPanel titlePane = new CPanel();
		titlePane.setLayout(new GridBagLayout());
		titlePane.add(titleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						5, 5, 5), 0, 0));
		titlePane.add(titleIcon, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,
						5, 5, 5), 0, 0));
		this.add(titlePane,BorderLayout.NORTH);

		//  Buttons
		buttonHelp.addActionListener(this);
		buttonCancel.addActionListener(this);
		buttonNext.addActionListener(this);
		buttonBack.addActionListener(this);
		
		JPanel buttonPane = new CPanel();
		BoxLayout buttonLayout = new BoxLayout(buttonPane, BoxLayout.LINE_AXIS);
		buttonPane.setLayout(buttonLayout);
		Dimension buttonPadd = new Dimension(15,40);
		buttonPane.add(Box.createRigidArea(buttonPadd));
		buttonPane.add(buttonBack);
		buttonPane.add(Box.createRigidArea(new Dimension(2,40)));
		buttonPane.add(buttonNext);
		buttonPane.add(Box.createRigidArea(buttonPadd));
		buttonPane.add(buttonCancel);
		buttonPane.add(Box.createRigidArea(buttonPadd));		
		buttonPane.add(buttonHelp);
		buttonPane.add(Box.createRigidArea(buttonPadd));
		contentPane.add(buttonPane, BorderLayout.SOUTH);

		//  Add WizardPanel
		contentPane.add(wizardPanel, BorderLayout.CENTER);
		
	}	//	jbInit

	/**
	 * 	Dispose
	 */
	@Override
	public void dispose()
	{
		super.dispose();
		log.info("");
	}	//	dispose
	
	/**
	 * 	Get Sync Info
	 *	@return installInfo
	 */
	public Object getInfo()
	{
		return m_info;
	}	//	getInfo
	
	/**
	 * 	Set Sync info
	 *	@param info info
	 */
	public void setInfo(Object info)
	{
		m_info = info;
	}	//	setInfo

	/**
	 * 	Action Listener.
	 * 	Exit or Help
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if ((e.getSource() == menuFileExit) || (e.getSource() == buttonCancel))
			AEnv.showCenterScreen(quitDialog);
		else if ((e.getSource() == menuHelpInfo) || (e.getSource() == buttonHelp))
			new Setup_Help(this);
		else if (e.getSource() == buttonNext)
			wizardPanel.next();
		else if (e.getSource() == buttonBack)
			wizardPanel.previous();
	}	//	actionPerformed
	
	/**
	 * 	Property Change Listener.
	 * 	Change to Progress Card
	 *	@param evt event
	 */
	public void propertyChange(PropertyChangeEvent evt)
    {
		log.config(evt.getOldValue() + "->" + evt.getNewValue());
		//
/*		String newCard = (String)evt.getNewValue();
		
		//	Switch to Progress
		if (newCard.equals(PROGRESS))
		{
			progressPanel.init(this, p_properties);
		}
		else if (newCard.equals(COMPONENTS))
		{
			componentPanel.init(this, p_properties);
		}
		else
			return;
		
		cardLayout.show(cards, newCard);*/
    }	//	evt
	

	/**************************************************************************
	 * 	Start
	 * 	@param args Log Level e.g. ALL, FINE
	 */
	public static void main(String[] args)
	{
		CLogMgt.initialize(true);
		Handler fileHandler = new CLogFile(System.getProperty("user.dir"), false, false);
		CLogMgt.addHandler(fileHandler);
		//	Log Level
		if (args.length > 0)
			CLogMgt.setLevel(args[0]);
		else
			CLogMgt.setLevel(Level.INFO);
		//	File Loger at least FINE
		if (fileHandler.getLevel().intValue() > Level.FINE.intValue())
			fileHandler.setLevel(Level.FINE);
		//	PLAF
		CompierePLAF.setPLAF (null);
		
		new SetupWizard();
	}	//	main


}	//	Setup
