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
package org.compiere.apps.search;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.compiere.apps.*;
import org.compiere.framework.*;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Search Information and return selection - Base Class.
 *  <pre>
 *  Structure:
 *      parameterPanel  (JPanel) - for subclasses to add parameter fields
 *      scrollPame      (JScrollPane)
 *          m_table     (MiniTable)
 *      southPanel      (JPanel)
 *          confirmPanel
 *          statusPanel
 *  </pre>
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: Info.java,v 1.2 2006/07/30 00:51:27 jjanke Exp $
 */
public abstract class Info extends CDialog
	implements ListSelectionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 *  Factory Constructor
	 *  @param  frame   parent frame
	 *  @param  modal   new window is modal
	 *  @param  WindowNo	window no
	 *  @param  tableName   table name of the search
	 *  @param  keyColumn   key column of the search
	 *  @param	value		query value
	 *  @param  multiSelection  allow to select more than one row
	 *  @param  whereClause fully qualified where clause for the search
	 *  @return special or general Info Window
	 */
	public static Info create (Frame frame, boolean modal, int WindowNo,
		String tableName, String keyColumn, String value,
		boolean multiSelection, String whereClause)
	{
		Info info = null;
		Ctx ctx = Env.getCtx();
		if (tableName.equals("C_BPartner"))
			info = new InfoBPartner (frame, modal, WindowNo,  value, ctx.isSOTrx(),
					multiSelection, whereClause);
		else if (tableName.equals("M_Product"))
			info = new InfoProduct (frame, modal, WindowNo,  0,0, value,
					multiSelection, whereClause);
		else if (tableName.equals("C_Invoice"))
			info = new InfoInvoice (frame, modal, WindowNo, value,
					multiSelection, whereClause);
		else if (tableName.equals("A_Asset"))
			info = new InfoAsset (frame, modal, WindowNo, 0, value,
					multiSelection, whereClause);
		else if (tableName.equals("C_Order"))
			info = new InfoOrder (frame, modal, WindowNo, value,
					multiSelection, whereClause);
		else if (tableName.equals("M_InOut"))
			info = new InfoInOut (frame, modal, WindowNo, value,
					multiSelection, whereClause);
		else if (tableName.equals("C_Payment"))
			info = new InfoPayment (frame, modal, WindowNo, value,
					multiSelection, whereClause);
		else if (tableName.equals("C_CashLine"))
			info = new InfoCashLine (frame, modal, WindowNo, value,
					multiSelection, whereClause);
		else if (tableName.equals("S_ResourceAssigment"))
			info = new InfoAssignment (frame, modal, WindowNo, value,
					multiSelection, whereClause);
		else
			info = new InfoGeneral (frame, modal, WindowNo, value, 
				tableName, keyColumn, 
				multiSelection, whereClause);
		//
		AEnv.positionCenterWindow(frame, info);
		return info;
	}   //  create

	/**
	 * Show BPartner Info (non modal)
	 * @param frame Parent Frame
	 * @param WindowNo window no
	 */
	public static void showBPartner (Frame frame, int WindowNo)
	{
		Ctx ctx = Env.getCtx();
		Info info = new InfoBPartner (frame, false, WindowNo,  "",
			ctx.isSOTrx(), false, "");
		AEnv.showCenterWindow(frame, info);
	}   //  showBPartner

	/**
	 * Show Asset Info (non modal)
	 * @param frame Parent Frame
	 * @param WindowNo window no
	 */
	public static void showAsset (Frame frame, int WindowNo)
	{
		Info info = new InfoAsset (frame, false, WindowNo,  
			0, "", false, "");
		AEnv.showCenterWindow(frame, info);
	}   //  showBPartner

	/**
	 * Show Product Info (non modal)
	 * @param frame Parent Frame
	 * @param WindowNo window no
	 */
	public static void showProduct (Frame frame, int WindowNo)
	{
		Info info = new InfoProduct (frame, false, WindowNo,
			Env.getCtx().getContextAsInt( WindowNo, "M_Warehouse_ID"),
			Env.getCtx().getContextAsInt( WindowNo, "M_PriceList_ID"), 
			"",		//	value 
			false, "");
		AEnv.showCenterWindow(frame, info);
	}   //  showProduct

	/**
	 * Show Order Info (non modal)
	 * @param frame Parent Frame
	 * @param WindowNo window no
	 * @param value query value
	 */
	public static void showOrder (Frame frame, int WindowNo, String value)
	{
		Info info = new InfoOrder (frame, false, WindowNo, value,
			false, "");
		AEnv.showCenterWindow(frame, info);
	}   //  showOrder

	/**
	 * Show Invoice Info (non modal)
	 * @param frame Parent Frame
	 * @param WindowNo window no
	 * @param value query value
	 */
	public static void showInvoice (Frame frame, int WindowNo, String value)
	{
		Info info = new InfoInvoice (frame, false, WindowNo, value,
			false, "");
		AEnv.showCenterWindow(frame, info);
	}   //  showInvoice

	/**
	 * Show Shipment Info (non modal)
	 * @param frame Parent Frame
	 * @param WindowNo window no
	 * @param value query value
	 */
	public static void showInOut (Frame frame, int WindowNo, String value)
	{
		Info info = new InfoInOut (frame, false, WindowNo, value,
			false, "");
		AEnv.showCenterWindow(frame, info);
	}   //  showInOut

	/**
	 * Show Payment Info (non modal)
	 * @param frame Parent Frame
	 * @param WindowNo window no
	 * @param value query value
	 */
	public static void showPayment (Frame frame, int WindowNo, String value)
	{
		Info info = new InfoPayment (frame, false, WindowNo, value,
			false, "");
		AEnv.showCenterWindow(frame, info);
	}   //  showPayment

	/**
	 * Show Cash Line Info (non modal)
	 * @param frame Parent Frame
	 * @param WindowNo window no
	 * @param value query value
	 */
	public static void showCashLine (Frame frame, int WindowNo, String value)
	{
		Info info = new InfoCashLine (frame, false, WindowNo, value,
			false, "");
		AEnv.showCenterWindow(frame, info);
	}   //  showCashLine

	/**
	 * Show Assignment Info (non modal)
	 * @param frame Parent Frame
	 * @param WindowNo window no
	 * @param value query value
	 */
	public static void showAssignment (Frame frame, int WindowNo, String value)
	{
		Info info = new InfoAssignment (frame, false, WindowNo, value,
			false, "");
		AEnv.showCenterWindow(frame, info);
	}   //  showAssignment

	/** Window Width                */
	static final int        INFO_WIDTH = 800;

	
	/**************************************************************************
	 *	Detail Constructor
	 *  @param frame parent frame
	 *  @param modal modal
	 *  @param WindowNo window no
	 *  @param tableName table name
	 *  @param keyColumn key column name
	 *  @param multiSelection muiliple selection
	 *  @param whereClause where clause
	 */
	protected Info (Frame frame, boolean modal, int WindowNo,
		String tableName, String keyColumn,
		boolean multiSelection, String whereClause)
	{
		super (frame, modal);
		log.info("WinNo=" + p_WindowNo + " " + whereClause);
		p_WindowNo = WindowNo;
		p_tableName = tableName;
		p_keyColumn = keyColumn;
		p_multiSelection = multiSelection;
		if (whereClause == null || whereClause.indexOf('@') == -1)
			p_whereClause = whereClause;
		else
		{
			p_whereClause = Env.parseContext(Env.getCtx(), p_WindowNo, whereClause, false, false);
			if (p_whereClause.length() == 0)
				log.log(Level.SEVERE, "Cannot parse context= " + whereClause);
		}

		try
		{
			jbInit();
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "Info", ex);
		}
	}	//	Info


	/** Master (owning) Window  */
	protected int				p_WindowNo;
	/** Table Name              */
	protected String            p_tableName;
	/** Key Column Name         */
	protected String            p_keyColumn;
	/** Enable more than one selection  */
	protected boolean			p_multiSelection;
	/** Initial WHERE Clause    */
	protected String			p_whereClause = "";

	/** Table                   */
	protected MiniTable         p_table = new MiniTable(false);
	/** Model Index of Key Column   */
	private int                 m_keyColumnIndex = -1;
	/** OK pressed                  */
	private boolean			    m_ok = false;
	/** Cancel pressed - need to differentiate between OK - Cancel - Exit	*/
	private boolean			    m_cancel = false;
	/** Result IDs              */
	private ArrayList<Integer>	m_results = new ArrayList<Integer>(3);

	/** Layout of Grid          */
	protected Info_Column[]     p_layout;
	/** Main SQL Statement      */
	String						m_sqlMain;
	/** Count SQL Statement		*/
	private String              m_sqlCount;
	/** Order By Clause         */
	String  		            m_sqlOrder;

	/** Loading success indicator       */
	protected boolean	        p_loadedOK = false;
	/**	SO Zoom Window						*/
	private int					m_SO_Window_ID = -1;
	/**	PO Zoom Window						*/
	private int					m_PO_Window_ID = -1;

	/** Worker                  */
	private Worker              m_worker = null;
	
	/**	Logger			*/
	protected CLogger log = CLogger.getCLogger(getClass());

	/** Static Layout           */
	private CPanel southPanel = new CPanel();
	private BorderLayout southLayout = new BorderLayout();
	ConfirmPanel confirmPanel = new ConfirmPanel(true, true, true, true, true, true, true);
	protected StatusBar statusBar = new StatusBar();
	protected CPanel parameterPanel = new CPanel();
	private JScrollPane scrollPane = new JScrollPane();
	//
	private JPopupMenu popup = new JPopupMenu();
	private CMenuItem calcMenu = new CMenuItem();

	/**
	 *	Static Init
	 *  @throws Exception
	 */
	protected void jbInit() throws Exception
	{
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		southPanel.setLayout(southLayout);
		southPanel.add(confirmPanel, BorderLayout.CENTER);
		southPanel.add(statusBar, BorderLayout.SOUTH);
		getContentPane().add(southPanel, BorderLayout.SOUTH);
		this.getContentPane().add(parameterPanel, BorderLayout.NORTH);
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(p_table, null);
		//
		confirmPanel.addActionListener(this);
		confirmPanel.getResetButton().setVisible(hasReset());
		confirmPanel.getCustomizeButton().setVisible(hasCustomize());
		confirmPanel.getHistoryButton().setVisible(hasHistory());
		confirmPanel.getZoomButton().setVisible(hasZoom());
		//
		JButton print = ConfirmPanel.createPrintButton(true);
		print.addActionListener(this);
		confirmPanel.addButton(print);
		//
		popup.add(calcMenu);
		calcMenu.setText(Msg.getMsg(Env.getCtx(), "Calculator"));
		calcMenu.setIcon(new ImageIcon(org.compiere.Compiere.class.getResource("images/Calculator16.gif")));
		calcMenu.addActionListener(this);
		//
		p_table.getSelectionModel().addListSelectionListener(this);
		enableButtons();
	}	//	jbInit

	/**
	 *  Loaded correctly
	 *  @return true if loaded OK
	 */
	public boolean loadedOK()
	{
		return p_loadedOK;
	}   //  loadedOK

	/**
	 *	Set Status Line
	 *  @param text text
	 *  @param error error
	 */
	public void setStatusLine (String text, boolean error)
	{
		statusBar.setStatusLine(text, error);
		Thread.yield();
	}	//	setStatusLine

	/**
	 *	Set Status DB
	 *  @param text text
	 */
	public void setStatusDB (String text)
	{
		statusBar.setStatusDB(text);
	}	//	setStatusDB


	
	/**************************************************************************
	 *  Prepare Table, Construct SQL (m_m_sqlMain, m_sqlAdd)
	 *  and size Window
	 *  @param layout layout array
	 *  @param from from clause
	 *  @param staticWhere where clause
	 *  @param orderBy order by clause
	 */
	protected void prepareTable (String from, String staticWhere, String orderBy)
	{
		p_layout = getInfoColumns();
		ModelValidationEngine mve = ModelValidationEngine.get();
		int AD_Client_ID = Env.getCtx().getAD_Client_ID();
		ArrayList<Info_Column> columns = new ArrayList<Info_Column>();
		for (Info_Column element : p_layout)
			columns.add (element);
		StringBuffer sqlFrom = new StringBuffer(from);
		StringBuffer sqlOrder = new StringBuffer(orderBy);
		boolean change = mve.updateInfoColumns (AD_Client_ID, columns, sqlFrom, sqlOrder);
		if (change)
		{
			from = sqlFrom.toString();
			orderBy = sqlOrder.toString();
			p_layout = new Info_Column[columns.size()];
			columns.toArray(p_layout);
			Arrays.sort(p_layout);
		}
		
		//
		StringBuffer sql = new StringBuffer ("SELECT ");
		//  add columns & sql
		for (int i = 0; i < p_layout.length; i++)
		{
			if (i > 0)
				sql.append(", ");
			sql.append(p_layout[i].getColSQL());
			//  adding ID column
			if (p_layout[i].isIDcol())
				sql.append(",").append(p_layout[i].getIDcolSQL());
			//  add to model
			p_table.addColumn(p_layout[i].getColHeader());
			if (p_layout[i].isColorColumn())
				p_table.setColorColumn(i);
			if (p_layout[i].getColClass() == IDColumn.class)
				m_keyColumnIndex = i;
		}
		//  set editors (two steps)
		for (int i = 0; i < p_layout.length; i++)
			p_table.setColumnClass(i, p_layout[i].getColClass(), 
				p_layout[i].isReadOnly(), p_layout[i].getColHeader(), p_layout[i].getWidth());

		sql.append( " FROM ").append(from);
		//
		sql.append(" WHERE ").append(staticWhere);
		m_sqlMain = sql.toString();
		m_sqlCount = "SELECT COUNT(*) FROM " + from + " WHERE " + staticWhere;
		//
		m_sqlOrder = "";
		if (orderBy != null && orderBy.length() > 0)
			m_sqlOrder = " ORDER BY " + orderBy;

		if (m_keyColumnIndex == -1)
			log.log(Level.SEVERE, "No KeyColumn - " + sql);
		
		//  Table Selection
		p_table.setRowSelectionAllowed(true);
		p_table.addMouseListener(this);
		p_table.setMultiSelection(p_multiSelection);

		//  Window Sizing
		parameterPanel.setPreferredSize(new Dimension (INFO_WIDTH, parameterPanel.getPreferredSize().height));
		scrollPane.setPreferredSize(new Dimension(INFO_WIDTH, 400));
	}   //  prepareTable

	/**
	 * 	Get layout
	 *	@return array of info columns
	 */
	abstract Info_Column[] getInfoColumns();
	
	
	/**************************************************************************
	 *  Execute Query
	 */
	void executeQuery()
	{
		//  ignore when running
		if (m_worker != null && m_worker.isAlive())
			return;
		//
		if (!testCount())
			return;

		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setStatusLine(Msg.getMsg(Env.getCtx(), "StartSearch"), false);
		m_worker = new Worker();
		m_worker.start();
	}   //  executeQuery

	/**
	 * 	Test Row Count
	 *	@return true if display
	 */
	private boolean testCount()
	{
		long start = System.currentTimeMillis();
		String dynWhere = getSQLWhere();
		StringBuffer sql = new StringBuffer (m_sqlCount);
		if (dynWhere.length() > 0)
			sql.append(dynWhere);   //  includes first AND
		String countSql = Msg.parseTranslation(Env.getCtx(), sql.toString());	//	Variables
		countSql = MRole.getDefault().addAccessSQL(countSql, getTableName(), 
			MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO,true);
		log.finer(countSql);
		int no = -1;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(countSql, (Trx) null);
			setParameters (pstmt, true);
			rs = pstmt.executeQuery();
			if (rs.next())
				no = rs.getInt(1);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, countSql, e);
			no = -2;
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		log.fine("#" + no + " - " + (System.currentTimeMillis()-start) + "ms");
		if (no > 1000)
			return ADialog.ask(p_WindowNo, this, "InfoHighRecordCount", String.valueOf(no));
		return true;
	}	//	testCount
			

	/**
	 *	Save Selection	- Called by dispose
	 */
	protected void saveSelection ()
	{
		//	Already disposed
		if (p_table == null)
			return;

		log.config("OK=" + m_ok);
		if (!m_ok)      //  did not press OK
		{
			m_results.clear();
			p_table.removeAll();
			p_table = null;
			return;
		}

		//	Multi Selection
		if (p_multiSelection)
		{
		}
		else    //  singleSelection
		{
			Integer data = getSelectedRowKey();
			if (data != null)
				m_results.add(data);
		}
		log.config(getSelectedSQL());

		//	Save Settings of detail info screens
		saveSelectionDetail();
		//	Clean-up
		p_table.removeAll();
		p_table = null;
	}	//	saveSelection

	/**
	 *  Get the key of currently selected row
	 *  @return selected key
	 */
	protected Integer getSelectedRowKey()
	{
		int row = p_table.getSelectedRow();
		if (row != -1 && m_keyColumnIndex != -1)
		{
			Object data = p_table.getModel().getValueAt(row, m_keyColumnIndex);
			if (data instanceof IDColumn)
				data = ((IDColumn)data).getRecord_ID();
			if (data instanceof Integer)
				return (Integer)data;
		}
		return null;
	}   //  getSelectedRowKey

	/**
	 *	Get selected Keys
	 *  @return selected keys (Integers)
	 */
	public Object[] getSelectedKeys()
	{
		if (!m_ok || m_results.size() == 0)
			return null;
		return m_results.toArray();
	}	//	getSelectedKeys;

	/**
	 *	Get (first) selected Key
	 *  @return selected key
	 */
	public Object getSelectedKey()
	{
		if (!m_ok || m_results.size() == 0)
			return null;
		return m_results.get(0);
	}	//	getSelectedKey

	/**
	 *	Is cancelled?
	 *	- if pressed Cancel = true
	 *	- if pressed OK or window closed = false
	 *  @return true if cancelled
	 */
	public boolean isCancelled()
	{
		return m_cancel;
	}	//	isCancelled

	/**
	 *	Get where clause for (first) selected key
	 *  @return WHERE Clause
	 */
	public String getSelectedSQL()
	{
		//	No results
		Object[] keys = getSelectedKeys();
		if (keys == null || keys.length == 0)
		{
			log.config("No Results - OK=" 
				+ m_ok + ", Cancel=" + m_cancel);
			return "";
		}
		//
		StringBuffer sb = new StringBuffer(getKeyColumn());
		if (keys.length > 1)
			sb.append(" IN (");
		else
			sb.append("=");

		//	Add elements
		for (Object element : keys) {
			if (getKeyColumn().toUpperCase().endsWith("_ID"))
				sb.append(element.toString()).append(",");
			else
				sb.append("'").append(element.toString()).append("',");
		}

		sb.replace(sb.length()-1, sb.length(), "");
		if (keys.length > 1)
			sb.append(")");
		return sb.toString();
	}	//	getSelectedSQL;

	
	/**************************************************************************
	 *	(Button) Action Listener & Popup Menu
	 *  @param e event
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		//  Popup => Calculator
		if (e.getSource().equals(calcMenu))
		{
			BigDecimal number = null;
			Object data = p_table.getSelectedValue();
			try
			{
				if (data != null)
				{
					if (data instanceof BigDecimal)
						number = (BigDecimal)data;
					else
						number = new BigDecimal(data.toString());
				}
			}
			catch (Exception ex) {}
			Calculator c = new Calculator(null, number);
			c.setVisible(true);
			return;
		}   //  popup

		//  Confirm Panel
		String cmd = e.getActionCommand();
		if (cmd.equals(ConfirmPanel.A_OK))
		{
			dispose(true);
		}
		else if (cmd.equals(ConfirmPanel.A_CANCEL))
		{
			m_cancel = true;
			dispose(false);
		}
		//
		else if (cmd.equals(ConfirmPanel.A_HISTORY))
			showHistory();
		else if (cmd.equals(ConfirmPanel.A_CUSTOMIZE))
			customize();
		else if (cmd.equals(ConfirmPanel.A_ZOOM))
			zoom();
		else if (cmd.equals(ConfirmPanel.A_RESET))
			doReset();
		else if (cmd.equals(ConfirmPanel.A_PRINT))
			PrintScreenPainter.printScreen(this);
		//	Default
		else
			executeQuery();
	}	//	actionPerformed

	/**
	 *	Zoom to target
	 *  @param AD_Window_ID window id
	 *  @param zoomQuery zoom query
	 */
	void zoom (int AD_Window_ID, Query zoomQuery)
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		final AWindow frame = new AWindow();
		if (!frame.initWindow(AD_Window_ID, zoomQuery))
			return;
		//	Modal Window causes UI lock
		if (isModal())
		{
			setModal(false);	//	remove modal option has no effect
			dispose();			//	VLookup.actionButton - Result = null (not cancelled)
		}
		else
			setCursor(Cursor.getDefaultCursor());

		//	VLookup gets info after method finishes
		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					sleep(50);
				}
				catch (Exception e)
				{
				}
				AEnv.showCenterScreen(frame);
			}
		}.start();
	}	//	zoom

	/**
	 *	Dispose (not OK)
	 */
	@Override
	public void dispose()
	{
		dispose(false);
	}	//	dispose

	/**
	 *	Dispose and save Selection
	 *	@param ok OK pressed
	 */
	public void dispose(boolean ok)
	{
		log.config("OK=" + ok);
		m_ok = ok;

		//  End Worker
		if (m_worker != null)
		{
			//  worker continues, but it does not block UI
			if (m_worker.isAlive())
				m_worker.interrupt();
			log.config("Worker alive=" + m_worker.isAlive());
		}
		m_worker = null;
		//
		saveSelection();
		removeAll();
		super.dispose();
	}	//	dispose

	/**
	 *  Get Table name Synonym
	 *  @return table name
	 */
	String getTableName()
	{
		return p_tableName;
	}   //  getTableName

	/**
	 *  Get Key Column Name
	 *  @return column name
	 */
	String getKeyColumn()
	{
		return p_keyColumn;
	}   //  getKeyColumn

	
	/**************************************************************************
	 *  Table Selection Changed
	 *  @param e event
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting())
			return;
		enableButtons();
	}   //  calueChanged

	/**
	 *  Enable OK, History, Zoom if row selected
	 */
	void enableButtons ()
	{
		boolean enable = p_table.getSelectedRow() != -1;
		confirmPanel.getOKButton().setEnabled(enable);
		
		if (hasHistory())
			confirmPanel.getHistoryButton().setEnabled(enable);
		if (hasZoom())
			confirmPanel.getZoomButton().setEnabled(enable);
	}   //  enableButtons

	
	/**************************************************************************
	 *  Get dynamic WHERE part of SQL
	 *	To be overwritten by concrete classes
	 *  @return WHERE clause
	 */
	abstract String getSQLWhere();
	
	/**
	 *  Set Parameters for Query
	 *	To be overwritten by concrete classes
	 *  @param pstmt statement
	 *  @param forCount for counting records
	 *  @throws SQLException
	 */
	abstract void setParameters (PreparedStatement pstmt, boolean forCount) 
		throws SQLException;

	/**
	 *  Reset Parameters
	 *	To be overwritten by concrete classes
	 */
	void doReset()					{}
	/**
	 *  Has Reset (false)
	 *	To be overwritten by concrete classes
	 *  @return true if it has reset (default false)
	 */
	boolean hasReset()				{return false;}
	/**
	 *  History dialog
	 *	To be overwritten by concrete classes
	 */
	void showHistory()					{}
	/**
	 *  Has History (false)
	 *	To be overwritten by concrete classes
	 *  @return true if it has history (default false)
	 */
	boolean hasHistory()				{return false;}
	/**
	 *  Customize dialog
	 *	To be overwritten by concrete classes
	 */
	void customize()					{}
	/**
	 *  Has Customize (false)
	 *	To be overwritten by concrete classes
	 *  @return true if it has customize (default false)
	 */
	boolean hasCustomize()				{return false;}
	/**
	 *  Zoom action
	 *	To be overwritten by concrete classes
	 */
	void zoom()							{}
	/**
	 *  Has Zoom (false)
	 *	To be overwritten by concrete classes
	 *  @return true if it has zoom (default false)
	 */
	boolean hasZoom()					{return false;}
	/**
	 *  Save Selection Details
	 *	To be overwritten by concrete classes
	 */
	void saveSelectionDetail()          {}

	/**
	 * 	Get Zoom Window
	 *	@param tableName table name
	 *	@param isSOTrx sales p_trx
	 *	@return AD_Window_ID
	 */
	protected int getAD_Window_ID (String tableName, boolean isSOTrx)
	{
		if (!isSOTrx && m_PO_Window_ID > 0)
			return m_PO_Window_ID;
		if (m_SO_Window_ID > 0)
			return m_SO_Window_ID;
		//
		String sql = "SELECT AD_Window_ID, PO_Window_ID FROM AD_Table WHERE TableName=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, tableName);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				m_SO_Window_ID = rs.getInt(1);
				m_PO_Window_ID = rs.getInt(2);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//
		if (!isSOTrx && m_PO_Window_ID > 0)
			return m_PO_Window_ID;
		return m_SO_Window_ID;
	}	//	getAD_Window_ID


	/**************************************************************************
	 *  Mouse Clicked
	 *  @param e event
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		//  Double click with selected row => exit
		if (e.getClickCount() > 1 && p_table.getSelectedRow() != -1)
		{
			dispose(true);	        //  double_click same as OK
		}
		//  Right Click => start Calculator
		else if (SwingUtilities.isRightMouseButton(e))
		{
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}   //  mouseClicked


	/**
	 *  Worker
	 */
	class Worker extends Thread
	{
		/**
		 *  Do Work (load data)
		 */
		@Override
		public void run()
		{
			long start = System.currentTimeMillis();

			//  Clear Table
			p_table.setRowCount(0);
			//
			String dynWhere = getSQLWhere();
			StringBuffer sql = new StringBuffer (m_sqlMain);
			if (dynWhere.length() > 0)
				sql.append(dynWhere);   //  includes first AND
			sql.append(m_sqlOrder);
			String dataSqlStr = Msg.parseTranslation(Env.getCtx(), sql.toString());	//	Variables
			dataSqlStr = MRole.getDefault().addAccessSQL(dataSqlStr, getTableName(), 
				MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO,true);
			final String dataSql = dataSqlStr;
			log.finer(dataSql);
			try
			{
				PreparedStatement  pstmt = DB.prepareStatement(dataSql, (Trx) null);
				setParameters (pstmt, false);	//	no count
				log.finer("Start query - " + (System.currentTimeMillis()-start) + "ms");
				final ResultSet rs = pstmt.executeQuery();
				log.finer("End query - " + (System.currentTimeMillis()-start) + "ms");

				Runnable runnable = new Runnable() 
				{
					public void run()
					{
						TableModel model = p_table.getModel();
						try
						{
							while (!isInterrupted() & rs.next())
							{
								int row = p_table.getRowCount();
								p_table.setRowCount(row+1);
								int colOffset = 1;  //  columns start with 1
								for (int col = 0; col < p_layout.length; col++)
								{
									Object data = null;
									Class<?> c = p_layout[col].getColClass();
									int colIndex = col + colOffset;
									if (c == IDColumn.class)
										data = new IDColumn(rs.getInt(colIndex));
									else if (c == Boolean.class)
										data = Boolean.valueOf("Y".equals(rs.getString(colIndex)));
									else if (c == Timestamp.class)
										data = rs.getTimestamp(colIndex);
									else if (c == BigDecimal.class)
										data = rs.getBigDecimal(colIndex);
									else if (c == Double.class)
										data = new Double(rs.getDouble(colIndex));
									else if (c == Integer.class)
										data = Integer.valueOf(rs.getInt(colIndex));
									else if (c == KeyNamePair.class)
									{
										String display = rs.getString(colIndex);
										int key = rs.getInt(colIndex+1);
										data = new KeyNamePair(key, display);
										colOffset++;
									}
									else
										data = rs.getString(colIndex);
									//  store
									model.setValueAt(data, row, col);
								}	//	column
							}	//	next
						}
						catch (SQLException e)
						{
							log.log(Level.SEVERE, dataSql, e);
						}
						finally
						{
							DB.closeResultSet(rs);
						}
					}
				};
				
				try 
				{
					SwingUtilities.invokeAndWait(runnable);
				}
				catch (InterruptedException ie)
				{
					log.log(Level.SEVERE, ie.getLocalizedMessage(), ie);
				}
				catch (InvocationTargetException ite)
				{
					log.log(Level.SEVERE, ite.getLocalizedMessage(), ite);
				}
				
				if (isInterrupted())
					log.finer("Interrupted");
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, dataSql, e);
			}
			int no = p_table.getRowCount();
			log.fine("#" + no + " - " + (System.currentTimeMillis()-start) + "ms");
			p_table.autoSize();
			//
			setCursor(Cursor.getDefaultCursor());
			setStatusLine(Integer.toString(no) + " " + Msg.getMsg(Env.getCtx(), "SearchRows_EnterQuery"), false);
			setStatusDB(Integer.toString(no));
			if (no == 0)
				log.fine(dataSql);
			else
			{
				p_table.getSelectionModel().setSelectionInterval(0, 0);
				p_table.requestFocus();
			}
		}   //  run
	}   //  Worker

}	//	Info
