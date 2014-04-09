/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.
 * This program is free software; you can redistribute it and/or modify it
 * under the terms version 2 of the GNU General Public License as published
 * by the Free Software Foundation. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 * You may reach us at: ComPiere, Inc. - http://www.compiere.org/license.html
 * 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA or info@compiere.org 
 *****************************************************************************/
package org.compiere.install;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import org.compiere.apps.SwingWorker;
import org.compiere.startup.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Setup Progress Dialog
 *	
 *  @author Jorg Janke
 *  @version $Id: ProgressPanel.java 8456 2010-02-17 23:15:35Z freyes $
 */
public class ProgressPanel extends CPanel 
	implements ActionListener, PublishInterface
{
	/** */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Progress Panel
	 *	@param statusBar
	 */
	public ProgressPanel()
	{
		super(new BorderLayout());
	}	//	ProgressPanel

	/** Test - skip server install	- dev only */
	static final boolean 	SKIP_SERVERINSTALL = false;
	
	/**	Parent Window			*/
	private Setup		m_setup = null;
	/*	configuration properties	*/
	private Properties	m_properties = null;
	/** Can we migrate			*/
	private boolean		m_canMigrate = false;
	/** Dies a database exist	*/
	private boolean		m_dbExists = false;
	

	/** Process success			*/
	boolean 			m_success = false;
	
	private final static int		WHAT_SERVERINSTALL = 0;
	private final static int		WHAT_IMPORT = 1;
	private final static int		WHAT_MIGRATE = 2;
	private int			m_what = -1;
	
	private String m_serverInstallTitle;
	private String m_importTitle;
	private String m_migrateTitle;
	private String m_start;
	private String m_done;
	private String m_select;
	
	/**	Total number of steps		*/
	private int		m_totalSteps = 0;
	/**	Completed number of steps		*/
	private int		m_completedSteps = 0;
	
	/**	Logger	*/
	static CLogger log = CLogger.getCLogger(ProgressPanel.class);

	private CPanel		serverInstallPanel = new CPanel(new BorderLayout());
	private CTextArea	serverInstallText = new CTextArea();
	private CButton		serverInstallButton = new CButton();
	private JProgressBar serverInstallProgress = new JProgressBar(SwingConstants.HORIZONTAL);
	
	private CPanel		importPanel = new CPanel(new BorderLayout());
	private CTextArea	importText = new CTextArea();
	private CButton		importButton = new CButton();
	private JProgressBar importProgress = new JProgressBar(SwingConstants.HORIZONTAL);
	
	private CPanel		migratePanel = new CPanel(new BorderLayout());
	private CTextArea	migrateText = new CTextArea();
	private CButton		migrateButton = new CButton();
	private JProgressBar migrateProgress = new JProgressBar(SwingConstants.HORIZONTAL);


	/**
	 * 	Initialize
	 * 	@param setup parent
	 *  @param properties configuration properties
	 */
	protected void init(Setup setup, Properties properties)
	{
		m_setup = setup;
		m_properties = properties;
		m_dbExists = "Y".equals(m_properties
			.getProperty(Environment.COMPIERE_DB_USER_EXISTS, "N"));
		m_canMigrate = m_dbExists 
			&& !"0".equals(m_properties.getProperty("MIGRATIONSTEPS","x"));
		
		staticInit();
	}	//	init
	
	/**
	 * 	Layout
	 */
	private void staticInit()
	{
		Dimension textSize = new Dimension(300,200);
		initPublish();
		m_start = Setup.res.getString("Start");
		m_done = Setup.res.getString("Done");
		m_select = Setup.res.getString("SelectOption");
		m_setup.statusBar.setText(m_select);

		//
		m_serverInstallTitle = Setup.res.getString("ServerInstall");
		serverInstallPanel.setBorder(BorderFactory.createTitledBorder(m_serverInstallTitle));
		serverInstallButton.setText(m_start + " " + m_serverInstallTitle);
		serverInstallButton.addActionListener(this);
		serverInstallPanel.add(serverInstallButton, BorderLayout.NORTH);
		serverInstallText.setPreferredSize(textSize);
		serverInstallText.setReadWrite(false);
		serverInstallPanel.add(serverInstallText, BorderLayout.CENTER);
		serverInstallPanel.add(serverInstallProgress, BorderLayout.SOUTH);
		add(serverInstallPanel, BorderLayout.NORTH);

		m_importTitle = Setup.res.getString("CreateNewDatabase");
		if (m_dbExists)
			m_importTitle = Setup.res.getString("DropOldCreateNewDatabase");
		importPanel.setBorder(BorderFactory.createTitledBorder(m_importTitle));
		importButton.setText(m_start + " " + m_importTitle);
		importButton.addActionListener(this);
		importButton.setEnabled(false);
		importPanel.add(importButton, BorderLayout.NORTH);
		importText.setText(m_importTitle);
		importText.setPreferredSize(textSize);
		importText.setReadWrite(false);
		importPanel.add(importText, BorderLayout.CENTER);
		importPanel.add(importProgress, BorderLayout.SOUTH);
		
		CPanel center = new CPanel(new BorderLayout(0,0));
		if (m_canMigrate)
		{
			center.add(importPanel, BorderLayout.CENTER);
			add(center, BorderLayout.CENTER);
		}
		else
			add(importPanel, BorderLayout.CENTER);
		
		if (m_canMigrate)
		{
			m_migrateTitle = Setup.res.getString("MigrateExistingDatabase");
			migratePanel.setBorder(BorderFactory.createTitledBorder(m_migrateTitle));
			migrateButton.setText(m_start + " " + m_migrateTitle);
			migrateButton.addActionListener(this);
			migratePanel.add(migrateButton, BorderLayout.NORTH);
			migrateText.setText(m_migrateTitle);
			migrateText.setPreferredSize(textSize);
			migrateText.setReadWrite(false);
			migratePanel.add(migrateText, BorderLayout.CENTER);
			migratePanel.add(migrateProgress, BorderLayout.SOUTH);
			center.add(migratePanel, BorderLayout.EAST);
		}
		migrateButton.setEnabled(false);
		//
		serverInstallButton.setDefaultCapable(true);
		m_setup.getRootPane().setDefaultButton(serverInstallButton);
		serverInstallButton.requestFocus();
	}	//	staticInit
	
	/**
	 * 	Get Install Properties
	 *	@return properties
	 */
	public Properties getProperties()
	{
		return m_properties;
	}	//	getProperties
	
	/**
	 * 	Action Listener
	 *	@param e event
	 */
	public void actionPerformed(ActionEvent e)
    {
		if (!m_setup.menuFileExit.isEnabled())
			return;
		Object source = e.getSource();
		if (source == serverInstallButton)
			cmd(WHAT_SERVERINSTALL);
		else if (source == importButton)
			cmd(WHAT_IMPORT);
		else if (source == migrateButton)
			cmd(WHAT_MIGRATE);
    }	//	actionPerformed

	/**
	 * 	Execute Command
	 *	@param what
	 */
	private void cmd (int what)
	{
		//	Import Warning
		if (what == WHAT_IMPORT && m_dbExists 
			&& JOptionPane.showConfirmDialog(this, 
				Setup.res.getString("DropExistingDatabase"), 
				m_importTitle, 
				JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
			return;
		//
		enableUI(false);
		if (what == WHAT_SERVERINSTALL)
		{
			m_what = WHAT_SERVERINSTALL;
			log.info(m_serverInstallTitle);
			m_setup.statusBar.setText(m_serverInstallTitle);
			serverInstallProgress.setIndeterminate(true);
			startWork(WHAT_SERVERINSTALL);
		}
		else if (what == WHAT_IMPORT)
		{
			m_what = WHAT_IMPORT;
			log.info(m_importTitle);
			migratePanel.setVisible(false);
			this.invalidate();
			m_setup.statusBar.setText(m_importTitle);
			importProgress.setIndeterminate(true);
			startWork(WHAT_IMPORT);
		}
		else if (what == WHAT_MIGRATE && m_canMigrate)
		{
			m_what = WHAT_MIGRATE;
			log.info(m_migrateTitle);
			importPanel.setVisible(false);
			this.invalidate();
			m_setup.statusBar.setText(m_migrateTitle);
			migrateProgress.setIndeterminate(true);
			startWork(WHAT_MIGRATE);
		}
		else
			log.severe("Unknown What=" + what);
	}	//	cmd
	
	
	/**
	 * 	Enable Parent UI
	 *	@param enable
	 */
	void enableUI (boolean enable)
	{
		if (m_success)
			log.info("" + enable);
		else if (enable)
		{
			JOptionPane.showMessageDialog (this, 
				Setup.res.getString("ErrorProcess"), 
				Setup.res.getString("CompiereServerSetup"), JOptionPane.ERROR_MESSAGE);
		}
		m_setup.menuFileExit.setEnabled(enable);
		if (enable)
		{
			setCursor(Cursor.getDefaultCursor());
			if (m_what == WHAT_SERVERINSTALL && m_success)
			{
				importButton.setEnabled(true);
				if (m_canMigrate)
				{
					migrateButton.setEnabled(true);
					migrateButton.setDefaultCapable(true);
					m_setup.getRootPane().setDefaultButton(migrateButton);
					migrateButton.requestFocus();
				}
				else
				{
					importButton.setDefaultCapable(true);
					m_setup.getRootPane().setDefaultButton(importButton);
					importButton.requestFocus();
				}
			}
			serverInstallProgress.setIndeterminate(false);
			importProgress.setIndeterminate(false);
			migrateProgress.setIndeterminate(false);
		}
		else	//	disable
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			serverInstallButton.setEnabled(false);
			importButton.setEnabled(false);
			migrateButton.setEnabled(false);
		}
	}	//	enableUI
	
	final static String RETURN_CODE = "CompiereRetCode=";
	final static int RETURN_CODE_LENGTH = RETURN_CODE.length();
	
	/**
	 * 	Server Install.
	 * 	Run Ant Build Scripts
	 *	@return true if success
	 */
	boolean cmd_serverInstall()
	{
		final String compiereHome = m_properties.getProperty(ConfigurationData.COMPIERE_HOME);
		System.setProperty(ConfigurationData.COMPIERE_HOME, compiereHome);
		log.info(compiereHome);
		publish (Level.INFO, "** " + m_start, 0, 0);
		int retCode = -1;
		java.io.PrintStream stdOut = System.out;
		java.io.PrintStream stdErr = System.err;
		java.io.PipedInputStream pIn = null;
		java.io.PipedOutputStream pOut = null;
		final java.io.PrintStream ps;
		//
		m_totalSteps = 24;
		m_completedSteps = 0;
		try
		{			
			// Create stream pipe to communication with ant thread
			pIn = new java.io.PipedInputStream();
			pOut = new java.io.PipedOutputStream(pIn);
			ps = new java.io.PrintStream(pOut);
			
			// Start a thread to run the ant
			Thread antThread = new Thread () 
			{
				@Override
				public void run () 
			    {
					try 
				    {
			    		// Set the stdout/stderr to the piped stream
			    		System.setOut(ps);
			    		System.setErr(ps);
			    		
			    		// Setup properties for ant
			    		log.info("Starting Ant ... " + compiereHome);
			    		System.setProperty("ant.home", compiereHome);
			    		String[] args = new String[] {"-buildfile", 
			    								compiereHome + File.separator + "build.xml", 
			    								"setup"};
			    		
			    		// Start the ant and get the return code
			    		CompiereAntMain compiereMain = new CompiereAntMain();
			    		int rc = compiereMain.startAnt(args);
			    		
			    		// Ant done and notify the calling thread
			    		System.out.println(RETURN_CODE + rc);
			    		ps.close();
				    } 
					catch (Exception ex) 
					{
						ex.printStackTrace ();
					}
			    }  // run()
			};  // new Thread()
			antThread.start ();
		
			// Read from the piped stream
			int amount;
			byte[] buffer = new byte[10000];
			while (true)
			{
				try
				{
					amount = pIn.read(buffer);
					if (amount < 0)
						break;
					String msg = new String(buffer, 0, amount);
					// Check if ant is done
					if (msg.indexOf(RETURN_CODE) != -1)
					{ 
						// Capture the return code of ant
						int index = msg.indexOf(RETURN_CODE);
						retCode = Integer.parseInt(msg.substring(index+RETURN_CODE_LENGTH, index+RETURN_CODE_LENGTH+1));
						// Publish the last message
						msg = msg.substring(0, index);
						publishInstall(msg);
						break;
					}
					
					// Push the output of ant to swing console
					publishInstall(msg);
				}
				catch (java.io.IOException ex)
				{
					// The exception sometimes caused by ant's task thread which
					// closed stdout, so can't simply ignore it
					continue;
				}
			}  // while (true)
		}
		catch (Exception e)
		{
			publish(Level.SEVERE, e.toString(), m_totalSteps, m_totalSteps);
		}
		finally
		{
			// Reset the stdout & stderr
			System.setOut(stdOut);
			System.setErr(stdErr);
			try
			{
				if (pOut != null)
					pOut.close();
				if (pIn != null)
					pIn.close();
			}
			catch (Exception ex) {}
		}
		//
		publish (Level.INFO, "** " + m_done, m_totalSteps, m_totalSteps);
		m_setup.statusBar.setText(m_select);
		return retCode == 0;
	}	//	cmd_serverInstall
	
	/**
	 * 	Import - Fresh Install
	 *	@return true if success
	 */
	boolean cmd_import()
	{
		boolean success = false;
		m_totalSteps = 10;
		m_completedSteps = 0;
		//
		String dbType = m_properties.getProperty(Environment.COMPIERE_DB_TYPE);
		String systemPwd = m_properties.getProperty(Environment.COMPIERE_DB_SYSTEM);
		String dbName = m_properties.getProperty(Environment.COMPIERE_DB_NAME);
		String dbUid = m_properties.getProperty(Environment.COMPIERE_DB_USER);
		String dbPwd = m_properties.getProperty(Environment.COMPIERE_DB_PASSWORD);
		String dbHost = m_properties.getProperty(Environment.COMPIERE_DB_SERVER);
		String dbPort = m_properties.getProperty(Environment.COMPIERE_DB_PORT);
		publish (Level.INFO, "** " + m_start + "(" + dbHost + " " + dbUid + ")", 0, 0);
		
		/******	Drop/Create Database	******/
		Connection con = null;
		try
		{
			String url = null;
			if (Environment.DBTYPE_ORACLE.equals(dbType) 
				|| Environment.DBTYPE_ORACLEXE.equals(dbType))
			{
				oracle.jdbc.OracleDriver s_driver = new oracle.jdbc.OracleDriver();
				DriverManager.registerDriver (s_driver);
				url = "jdbc:oracle:thin:@//"+dbHost+":"+dbPort+"/"+dbName;
				con = DriverManager.getConnection (
					url, "System", systemPwd);
			}
			else if (Environment.DBTYPE_PG.equals(dbType))
			{ 
				com.edb.Driver s_driver = new com.edb.Driver();
				DriverManager.registerDriver (s_driver);
				url ="jdbc:edb://"+dbHost+":"+dbPort+"/mgmtsvr";
				con = DriverManager.getConnection (
					url, "enterprisedb", systemPwd);
			}
			else
				throw new IllegalArgumentException("Database not supported: " + dbType);
			//
			log.info(url);
			DatabaseCreate dbc = new DatabaseCreate(con, dbType, dbName,
				dbUid, dbPwd, this, m_dbExists);
			dbc.run();
			success = dbc.isSuccess();
			//
			con.close();
		}
		catch (Exception e)
		{
			publish (Level.SEVERE, e.toString(), m_totalSteps,m_totalSteps);
			success = false;
			if (con != null)
			{
				try
				{
					con.close();
				}
				catch (Exception ex)
				{
				}
			}
		}
		
		/******	Import Database	******/
		if (success)
		{
			log.info("");
			final String compiereHome = m_properties.getProperty(ConfigurationData.COMPIERE_HOME);
			String srcDir = compiereHome + File.separator + "data";
			
			//	Import
			Map<String,String> map = getInfo();
			Object info = null;
			try
			{
				Class<?> c = Class.forName("com.compiere.client.StartSupport");
				Object instance = c.newInstance();
				Method m = c.getMethod("startImport", new Class[] {String.class, 
						ProgressPanel.class, Object.class, Map.class});
				info = m.invoke(instance, new Object[] {srcDir, this, m_setup.getInfo(), map});
				String message = info.toString();
				if (message.startsWith("Error"))
				{
					log.severe(message);
					return false;
				}
			}
			catch (Exception e)
			{
				publish(Level.SEVERE, "** " + e.getMessage(), m_totalSteps, m_totalSteps);
				return false;
			}
		}
		
		//
		publish (Level.INFO, "** " + m_done, m_totalSteps, m_totalSteps);
		m_setup.statusBar.setText(m_done);
		return true;
	}	//	cmd_import

	/**
	 *	Migrate - existing stuff
	 *	@return true if success
	 */
	boolean cmd_migrate()
	{
		publish (Level.INFO, "** " + m_start + " " + m_migrateTitle + " **", 0, 0);
		//
		m_totalSteps = 4;
		m_completedSteps = 0;
		try
		{
			Class<?> c = Class.forName("com.compiere.client.StartSupport");
			Object instance = c.newInstance();
			Method m = c.getMethod("startMigrate", new Class[] {ProgressPanel.class, Object.class});
			m.invoke(instance, new Object[] {this, m_setup.getInfo()});
		}
		catch (Exception e)
		{
			publish(Level.SEVERE, "** " + e.getMessage(), m_totalSteps, m_totalSteps);
			log.log(Level.SEVERE, "migration exception. e.getMessage = "+e.getMessage(), e);
			return false;
		}
		//
		publish (Level.INFO, "** " + m_done, 0, 0);
		m_setup.statusBar.setText(m_done);
		return true;
	}	//	cmd_migrate

	/**
	 * 	Init Publish
	 */
	public void initPublish()
	{
		serverInstallText.initPublish();
		importText.initPublish();
		migrateText.initPublish();
	}	//	initPublish

	/**
	 * 	Publish Server Install Information to User.
	 * 	Derive Level & step from text 
	 *	@param info the clear text info
	 */
	private void publishInstall (String info)
	{
		if (info == null || info.trim().length() == 0)
			return;
		StringTokenizer st = new StringTokenizer(info, "\r\n", false);
		while (st.hasMoreTokens())
		{
			String s = st.nextToken();
			if (s == null || s.length() == 0 || s.trim().length() == 0)
				continue;
			Level level = Level.INFO;
			boolean mainOutput = !Character.isWhitespace(s.charAt(0));
			if (!mainOutput)
				level = Level.CONFIG;
			if (m_totalSteps != 0 && mainOutput)
				m_completedSteps++;
			publish (level, s, m_completedSteps, m_totalSteps);
		}
	}	//	publish

	/**
	 * 	Publish Information to User.
	 *	@param level type/importance 
	 *	- use INFO for main task 		- Create Database
	 *	-- CONFIG for areas				- (not used for this example) 
	 *	--- FINE for the actual work	- Drop User
	 *	---- FINER for trace 			- database feedback
	 *	* WARNING if we can continue
	 *	* SEVERE if we need to abort the process
	 *	@param info the clear text info
	 *	@param step the step you are on
	 *	@param totalSteps the total number of steps for this task
	 */
	public void publish (Level level, String info, int step, int totalSteps)
	{
		if (info == null || info.trim().length() == 0)
			return;
		//
		CTextArea text = null;
		JProgressBar progress;
		if (m_what == WHAT_SERVERINSTALL)
		{
			text = serverInstallText;
			progress = serverInstallProgress;
		}
		else if (m_what == WHAT_IMPORT)
		{
			text = importText;
			progress = importProgress;
		}
		else if (m_what == WHAT_MIGRATE)
		{
			text = migrateText;
			progress = migrateProgress;
		}
		else
		{
			log.warning("What=" + m_what + " - " + info);
			return;
		}

		log.log(level, info);
		if (!(Level.FINE.equals(level) || Level.FINER.equals(level) || Level.FINEST.equals(level)))
			text.publish(level, info, step, totalSteps);
		if (totalSteps == 0)
			progress.setIndeterminate(true);
		else
		{
			progress.setIndeterminate(false);
			if (progress.getMaximum() != totalSteps)
				progress.setMaximum(totalSteps);
			progress.setValue(step);
		}
	}	//	publish

	/**
	 * 	Get Info
	 *	@return Info
	 */
	private Map<String,String> getInfo()
	{
		Properties prop = getProperties();
		Map<String,String> map = new HashMap<String,String>();
		map.put(Environment.COMPIERE_DB_TYPE, prop.getProperty(Environment.COMPIERE_DB_TYPE));
		map.put("COMPIERE_MAIN_VERSION", prop.getProperty("COMPIERE_MAIN_VERSION"));
		map.put("COMPIERE_DATE_VERSION", prop.getProperty("COMPIERE_DATE_VERSION"));
		return map;
	}	//	getInfo
	
	/**************************************************************************
	 * 	Start Test Async.
	 * 	@param saveIt save
	 *  @return SwingWorker
	 */
	private SwingWorker startWork(final int what)
	{
		SwingWorker worker = new SwingWorker()
		{
			//	Start it
			@Override
			public Object construct()
			{
				m_success = false;
				try
				{
					if (what == WHAT_SERVERINSTALL)
					{
						if (SKIP_SERVERINSTALL)
							m_success = true;
						else
							m_success = cmd_serverInstall();
						//System.out.println("serverInstall m_success =" + m_success);
					}
					else if (what == WHAT_IMPORT)
					{
						m_success = cmd_import();
					}
					else if (what == WHAT_MIGRATE)
					{
						//System.out.println("before cmd_migrate =" + m_success);
						m_success = cmd_migrate();
						//System.out.println("after_migrate =" + m_success);
					}
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, "", e);
					m_success = false;
				}
				return new Boolean(m_success);
			}
			//	Finish it
			@Override
			public void finished()
			{
				log.info("Worker success=" + m_success);
				enableUI(true);
			}
		};
		worker.start();
		return worker;
	}	//	startWork

}	//	ProgressPanel
