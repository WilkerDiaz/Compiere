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
import java.sql.*;
import java.util.logging.*;

import javax.swing.*;

import org.compiere.apps.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Form Framework
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: FormFrame.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class FormFrame extends CFrame
	implements ActionListener
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Create Form.
	 *  Need to call openForm
	 */
	public FormFrame ()
	{
		super();
		addWindowListener(new java.awt.event.WindowAdapter()
		{
			@Override
			public void windowOpened(java.awt.event.WindowEvent evt)
			{
				formWindowOpened(evt);
			}
		});

		m_WindowNo = Env.createWindowNo (this);
	    setGlassPane(m_glassPane);
		try
		{
			jbInit();
			createMenu();
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}	//	FormFrame

	/**	WindowNo					*/
	private int			m_WindowNo;
	/** The GlassPane           	*/
	private AGlassPane  m_glassPane = new AGlassPane();
	/**	Description					*/
	private String		m_Description = null;
	/**	Help						*/
	private String		m_Help = null;
	/**	Menu Bar					*/
	private JMenuBar 	menuBar = new JMenuBar();
	/**	The Panel to be displayed	*/
	private FormPanel 	m_panel = null;
	/** Maximize Window				*/
	public boolean 		m_maximize = false;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(FormFrame.class);

	/**
	 * 	Static Init
	 * 	@throws Exception
	 */
	private void jbInit() throws Exception
	{
		this.setIconImage(org.compiere.Compiere.getImage16());
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setJMenuBar(menuBar);
	}	//	jbInit

	/**
	 *  Create Menu
	 */
	private void createMenu()
	{
		//      File
		JMenu mFile = AEnv.getMenu("File");
		menuBar.add(mFile);
		AEnv.addMenuItem("PrintScreen", null, KeyStroke.getKeyStroke(KeyEvent.VK_PRINTSCREEN, 0), mFile, this);
		AEnv.addMenuItem("ScreenShot", null, KeyStroke.getKeyStroke(KeyEvent.VK_PRINTSCREEN, Event.SHIFT_MASK), mFile, this);
		AEnv.addMenuItem("Report", null, KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.ALT_MASK), mFile, this);
		mFile.addSeparator();
		AEnv.addMenuItem("End", null, KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.ALT_MASK), mFile, this);
		AEnv.addMenuItem("Exit", null, KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.SHIFT_MASK+Event.ALT_MASK), mFile, this);

		//      View
		JMenu mView = AEnv.getMenu("View");
		menuBar.add(mView);
		AEnv.addMenuItem("InfoProduct", null, KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.CTRL_MASK), mView, this);
		AEnv.addMenuItem("InfoBPartner", null, KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.SHIFT_MASK+Event.CTRL_MASK), mView, this);
		AEnv.addMenuItem("InfoAccount", null, KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.ALT_MASK+Event.CTRL_MASK), mView, this);
		mView.addSeparator();
		AEnv.addMenuItem("InfoOrder", "Info", null, mView, this);
		AEnv.addMenuItem("InfoInvoice", "Info", null, mView, this);
		AEnv.addMenuItem("InfoInOut", "Info", null, mView, this);
		AEnv.addMenuItem("InfoPayment", "Info", null, mView, this);
		AEnv.addMenuItem("InfoSchedule", "Info", null, mView, this);

		//      Tools
		JMenu mTools = AEnv.getMenu("Tools");
		menuBar.add(mTools);
		AEnv.addMenuItem("Calculator", null, null, mTools, this);
		AEnv.addMenuItem("Calendar", null, null, mTools, this);
		AEnv.addMenuItem("Editor", null, null, mTools, this);
		AEnv.addMenuItem("Script", null, null, mTools, this);
		if (MRole.getDefault().isShowPreference())
		{
			mTools.addSeparator();
			AEnv.addMenuItem("Preference", null, null, mTools, this);
		}

		//      Help
		JMenu mHelp = AEnv.getMenu("Help");
		menuBar.add(mHelp);
		AEnv.addMenuItem("Help", "Help", KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0),	mHelp, this);
		AEnv.addMenuItem("Online", null, null, mHelp, this);
		AEnv.addMenuItem("EMailSupport", null, null, mHelp, this);
		AEnv.addMenuItem("About", null, null, mHelp, this);
	}   //  createMenu

	/**
	 *  Dispose
	 */
	@Override
	public void dispose()
	{
		log.config("");
		//	recursive calls
		if (m_panel != null
			&& Trace.isCalledFrom("JFrame"))	//	[x] close window pressed
			m_panel.dispose();
		m_panel = null;
		Env.clearWinContext(m_WindowNo);
		super.dispose();
	}	//  dispose

	/**
	 * 	Open Form
	 * 	@param AD_Form_ID form
	 *  @return true if form opened
	 */
	public final boolean openForm (int AD_Form_ID)
	{
		Ctx ctx = Env.getCtx();
		//
		String name = null;
		String className = null;
		String sql = "SELECT Name, Description, Classname, Help FROM AD_Form WHERE AD_Form_ID=?";  //jz ClassName, AD?
		boolean trl = !Env.isBaseLanguage(ctx, "AD_Form");
		if (trl)
			sql = "SELECT t.Name, t.Description, f.Classname, t.Help " //jz
				+ "FROM AD_Form f INNER JOIN AD_Form_Trl t"
				+ " ON (f.AD_Form_ID=t.AD_Form_ID AND AD_Language=?)"
				+ "WHERE f.AD_Form_ID=?";

		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			if (trl)
			{
				pstmt.setString(1, Env.getAD_Language(ctx));
				pstmt.setInt(2, AD_Form_ID);
			}
			else
				pstmt.setInt(1, AD_Form_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				name = rs.getString(1);
				m_Description = rs.getString(2);
				className = rs.getString(3);
				m_Help = rs.getString(4);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		if (className == null)
			return false;
		//
		boolean success = openForm(AD_Form_ID, className, name);
		//	Log
		MSession session = MSession.get(ctx);
		if (session != null)
			session.windowLog(ctx.getAD_Client_ID(), ctx.getAD_Org_ID(),
				0, AD_Form_ID);
		else
			return false;
		return success;
	}	//	openForm

	/**
	 * 	Open Form (directly)
	 *	@param AD_Form_ID Form
	 *	@param className class name
	 *	@param name title
	 *	@return true if started
	 */
	public boolean openForm (int AD_Form_ID, String className, String name)
	{
		log.info("AD_Form_ID=" + AD_Form_ID + ", Class=" + className);
		Ctx ctx = Env.getCtx();
		ctx.setContext( m_WindowNo, "WindowName", name);
		setTitle(Env.getHeader(ctx, m_WindowNo));

		try
		{
			//	Create instance w/o parameters
			m_panel = (FormPanel)Class.forName(className).newInstance();
		}
		catch (Throwable e)
		{
			log.log(Level.WARNING, "Class=" + className + ", AD_Form_ID=" + AD_Form_ID, e);
			return false;
		}
		//
		m_panel.init(m_WindowNo, this);
		return true;
	}	//	openForm

	/**
	 * 	Get Form Panel
	 *	@return form panel
	 */
	public FormPanel getFormPanel()
	{
		return m_panel;
	}	//	getFormPanel

	/**
	 * 	Action Listener
	 * 	@param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		String cmd = e.getActionCommand();
		if (cmd.equals("End"))
			dispose();
		else if (cmd.equals("Help"))
			actionHelp();
		else if (!AEnv.actionPerformed(cmd, m_WindowNo, this))
			log.log(Level.SEVERE, "Not handeled=" + cmd);
	}   //  actionPerformed

	/**
	 *	Show Help
	 */
	private void actionHelp()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<h2>").append(m_Description).append("</h2><p>")
			.append(m_Help);
		Help hlp = new Help (Env.getFrame(this), this.getTitle(), sb.toString());
		hlp.setVisible(true);
	}	//	actionHelp


	/*************************************************************************
	 *  Set Window Busy
	 *  @param busy busy
	 */
	public void setBusy (boolean busy)
	{
		if (busy == m_glassPane.isVisible())
			return;
		log.info("Busy=" + busy);
		if (busy)
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		else
			setCursor(Cursor.getDefaultCursor());
		m_glassPane.setMessage(null);
		m_glassPane.setVisible(busy);
		m_glassPane.requestFocus();
	}   //  setBusy

	/**
	 *  Set Busy Message
	 *  @param AD_Message message
	 */
	public void setBusyMessage (String AD_Message)
	{
		m_glassPane.setMessage(AD_Message);
	}   //  setBusyMessage

	/**
	 *  Set and start Busy Counter
	 *  @param time in seconds
	 */
	public void setBusyTimer (int time)
	{
		m_glassPane.setBusyTimer (time);
	}   //  setBusyTimer


	/**
	 * 	Set Maximize Window
	 *	@param max maximize
	 */
	public void setMaximize (boolean max)
	{
		m_maximize = max;
	}	//	setMaximize


	/**
	 * 	Form Window Opened.
	 * 	Maximize window if required
	 *	@param evt event
	 */
	void formWindowOpened(java.awt.event.WindowEvent evt)
	{
		if (m_maximize == true)
		{
			super.setVisible(true);
			super.setExtendedState(Frame.MAXIMIZED_BOTH);
		}
   }	//	formWindowOpened

	/**
	 * 	Start Batch
	 *	@param process
	 *	@return running thread
	 */
	public Thread startBatch (final Runnable process)
	{
		Thread worker = new Thread()
		{
			@Override
			public void run()
			{
				setBusy(true);
				process.run();
				setBusy(false);
			}
		};
		worker.start();
		return worker;
	}	//	startBatch

}	//	FormFrame
