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
import java.beans.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.event.*;

import org.compiere.apps.*;
import org.compiere.common.constants.*;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.print.*;
import org.compiere.process.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Manual Shipment Selection
 *
 *  @author Jorg Janke
 *  @version $Id: VInOutGen.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class VInOutGen extends CPanel
	implements FormPanel, ActionListener, VetoableChangeListener, 
		ChangeListener, TableModelListener, ASyncProcess
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		log.info("");
		m_WindowNo = WindowNo;
		m_frame = frame;
		Env.getCtx().setIsSOTrx(m_WindowNo, true);
		try
		{
			fillPicks();
			jbInit();
			dynInit();
			frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
			executeQuery();
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "init", ex);
		}
	}	//	init

	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;

	private boolean			m_selectionActive = true;
	private String          m_whereClause;
	private Object 			m_M_Warehouse_ID = null;
	private Object 			m_C_BPartner_ID = null;
	private Integer			m_ShipmentGroup_ID = 0;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VInOutGen.class);
	//
	private CTabbedPane tabbedPane = new CTabbedPane();
	private CPanel selPanel = new CPanel();
	private CPanel selNorthPanel = new CPanel();
	private BorderLayout selPanelLayout = new BorderLayout();
	private CLabel lWarehouse = new CLabel();
	private VLookup fWarehouse;
	private CLabel lBPartner = new CLabel();
	private VLookup fBPartner;
	private FlowLayout northPanelLayout = new FlowLayout();
	private ConfirmPanel confirmPanelSel = new ConfirmPanel(true);
	private ConfirmPanel confirmPanelGen = new ConfirmPanel(false, true, false, false, false, false, true);
	private StatusBar statusBar = new StatusBar();
	private CPanel genPanel = new CPanel();
	private BorderLayout genLayout = new BorderLayout();
	private CTextPane info = new CTextPane();
	private JScrollPane scrollPane = new JScrollPane();
	private MiniTable miniTable = new MiniTable();

	/**
	 *	Static Init.
	 *  <pre>
	 *  selPanel (tabbed)
	 *      fOrg, fBPartner
	 *      scrollPane & miniTable
	 *  genPanel
	 *      info
	 *  </pre>
	 *  @throws Exception
	 */
	void jbInit() throws Exception
	{
		CompiereColor.setBackground(this);
		//
		selPanel.setLayout(selPanelLayout);
		lWarehouse.setLabelFor(fWarehouse);
		lBPartner.setLabelFor(fBPartner);
		lBPartner.setText("BPartner");
		selNorthPanel.setLayout(northPanelLayout);
		northPanelLayout.setAlignment(FlowLayout.LEFT);
		tabbedPane.add(selPanel, Msg.getMsg(Env.getCtx(), "Select"));
		selPanel.add(selNorthPanel, BorderLayout.NORTH);
		selNorthPanel.add(lWarehouse, null);
		selNorthPanel.add(fWarehouse, null);
		selNorthPanel.add(lBPartner, null);
		selNorthPanel.add(fBPartner, null);
		selPanel.setName("selPanel");
		selPanel.add(confirmPanelSel, BorderLayout.SOUTH);
		selPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(miniTable, null);
		confirmPanelSel.addActionListener(this);
		//
		tabbedPane.add(genPanel, Msg.getMsg(Env.getCtx(), "Generate"));
		genPanel.setLayout(genLayout);
		genPanel.add(info, BorderLayout.CENTER);
		genPanel.setEnabled(false);
		info.setBackground(CompierePLAF.getFieldBackground_Inactive());
		info.setEditable(false);
		genPanel.add(confirmPanelGen, BorderLayout.SOUTH);
		confirmPanelGen.addActionListener(this);
	}	//	jbInit

	/**
	 *	Fill Picks.
	 *		Column_ID from C_Order
	 *  @throws Exception if Lookups cannot be initialized
	 */
	private void fillPicks() throws Exception
	{
		//	C_OrderLine.M_Warehouse_ID
		MLookup orgL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 2223, DisplayTypeConstants.TableDir);
		fWarehouse = new VLookup ("M_Warehouse_ID", true, false, true, orgL);
		lWarehouse.setText(Msg.translate(Env.getCtx(), "M_Warehouse_ID"));
		fWarehouse.addVetoableChangeListener(this);
		m_M_Warehouse_ID = fWarehouse.getValue();
		//	C_Order.C_BPartner_ID
		MLookup bpL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 2762, DisplayTypeConstants.Search);
		fBPartner = new VLookup ("C_BPartner_ID", false, false, true, bpL);
		lBPartner.setText(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		fBPartner.addVetoableChangeListener(this);
	}	//	fillPicks

	/**
	 *	Dynamic Init.
	 *	- Create GridController & Panel
	 *	- AD_Column_ID from C_Order
	 */
	private void dynInit()
	{
		//  create Columns
		miniTable.addColumn("C_Order_ID");
		miniTable.addColumn("AD_Org_ID");
		miniTable.addColumn("C_DocType_ID");
		miniTable.addColumn("DocumentNo");
		miniTable.addColumn("C_BPartner_ID");
		miniTable.addColumn("DateOrdered");
		miniTable.addColumn("TotalLines");
		miniTable.addColumn("DatePromised");
		//
		miniTable.setMultiSelection(true);
		miniTable.setRowSelectionAllowed(true);
		//  set details
		miniTable.setColumnClass(0, IDColumn.class, false, " ");
		miniTable.setColumnClass(1, String.class, true, Msg.translate(Env.getCtx(), "AD_Org_ID"));
		miniTable.setColumnClass(2, String.class, true, Msg.translate(Env.getCtx(), "C_DocType_ID"));
		miniTable.setColumnClass(3, String.class, true, Msg.translate(Env.getCtx(), "DocumentNo"));
		miniTable.setColumnClass(4, String.class, true, Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		miniTable.setColumnClass(5, Timestamp.class, true, Msg.translate(Env.getCtx(), "DateOrdered"));
		miniTable.setColumnClass(6, BigDecimal.class, true, Msg.translate(Env.getCtx(), "TotalLines"));
		miniTable.setColumnClass(7, Timestamp.class, true, Msg.translate(Env.getCtx(), "DatePromised"));
		//
		miniTable.autoSize();
		miniTable.getModel().addTableModelListener(this);
		//	Info
		statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "InOutGenerateSel"));//@@
		statusBar.setStatusDB(" ");
		//	Tabbed Pane Listener
		tabbedPane.addChangeListener(this);
	}	//	dynInit

	/**
	 *  Query Info
	 */
	private void executeQuery()
	{
		log.info("");
		int AD_Client_ID = Env.getCtx().getAD_Client_ID();
		//  Create SQL
		StringBuffer sql = new StringBuffer(
			"SELECT ic.C_Order_ID, o.Name, dt.Name, ic.DocumentNo, bp.Name, ic.DateOrdered, ic.TotalLines, so.DatePromised  "
			+ "FROM M_InOut_Candidate_v ic, AD_Org o, C_BPartner bp, C_DocType dt, C_Order so "
 			+ "WHERE ic.AD_Org_ID=o.AD_Org_ID"
 			+ " AND ic.C_BPartner_ID=bp.C_BPartner_ID"
 			+ " AND ic.C_DocType_ID=dt.C_DocType_ID"
			+ " AND so.C_Order_ID=ic.C_Order_ID"
 			+ " AND ic.AD_Client_ID=?");
 

		if (m_M_Warehouse_ID != null)
			sql.append(" AND ic.M_Warehouse_ID=").append(m_M_Warehouse_ID);
		if (m_C_BPartner_ID != null)
			sql.append(" AND ic.C_BPartner_ID=").append(m_C_BPartner_ID);
		//
		sql.append(" ORDER BY o.Name,bp.Name,DateOrdered");
		log.fine(sql.toString());

		//  reset table
		int row = 0;
		miniTable.setRowCount(row);
		//  Execute
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
			pstmt.setInt(1, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();
			//
			while (rs.next())
			{
				//  extend table
				miniTable.setRowCount(row+1);
				//  set values
				miniTable.setValueAt(new IDColumn(rs.getInt(1)), row, 0);   //  C_Order_ID
				miniTable.setValueAt(rs.getString(2), row, 1);              //  Org
				miniTable.setValueAt(rs.getString(3), row, 2);              //  DocType
				miniTable.setValueAt(rs.getString(4), row, 3);              //  Doc No
				miniTable.setValueAt(rs.getString(5), row, 4);              //  BPartner
				miniTable.setValueAt(rs.getTimestamp(6), row, 5);           //  DateOrdered
				miniTable.setValueAt(rs.getBigDecimal(7), row, 6);          //  TotalLines
				miniTable.setValueAt(rs.getTimestamp(8), row, 7);			// DatePromised
				//  prepare next
				row++;
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		//
		miniTable.autoSize();
	//	statusBar.setStatusDB(String.valueOf(miniTable.getRowCount()));
	}   //  executeQuery

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose

	/**
	 *	Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		log.info("Cmd=" + e.getActionCommand());
		//
		if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
		{
			dispose();
			return;
		}
		//
		m_whereClause = saveSelection();
		if (m_whereClause.length() > 0 
			&& m_selectionActive	//	on selection tab
			&& m_M_Warehouse_ID != null)
			generateShipments ();
		else
			dispose();
	}	//	actionPerformed

	/**
	 *	Vetoable Change Listener - requery
	 *  @param e event
	 */
	public void vetoableChange(PropertyChangeEvent e)
	{
		log.info(e.getPropertyName() + "=" + e.getNewValue());
		if (e.getPropertyName().equals("M_Warehouse_ID"))
			m_M_Warehouse_ID = e.getNewValue();
		if (e.getPropertyName().equals("C_BPartner_ID"))
		{
			m_C_BPartner_ID = e.getNewValue();
			fBPartner.setValue(m_C_BPartner_ID);	//	display value
		}
		executeQuery();
	}	//	vetoableChange

	/**
	 *	Change Listener (Tab changed)
	 *  @param e event
	 */
	public void stateChanged (ChangeEvent e)
	{
		int index = tabbedPane.getSelectedIndex();
		m_selectionActive = (index == 0);
	}	//	stateChanged

	/**
	 *  Table Model Listener
	 *  @param e event
	 */
	public void tableChanged(TableModelEvent e)
	{
		int rowsSelected = 0;
		int rows = miniTable.getRowCount();
		for (int i = 0; i < rows; i++)
		{
			IDColumn id = (IDColumn)miniTable.getValueAt(i, 0);     //  ID in column 0
			if (id != null && id.isSelected())
				rowsSelected++;
		}
		statusBar.setStatusDB(" " + rowsSelected + " ");
	}   //  tableChanged

	/**
	 *	Save Selection & return selecion Query or ""
	 *  @return where clause like C_Order_ID IN (...)
	 */
	private String saveSelection()
	{
		log.info("");
		//  ID selection may be pending
		miniTable.editingStopped(new ChangeEvent(this));
		//  Array of Integers
		ArrayList<Integer> results = new ArrayList<Integer>();

		//	Get selected entries
		int rows = miniTable.getRowCount();
		for (int i = 0; i < rows; i++)
		{
			IDColumn id = (IDColumn)miniTable.getValueAt(i, 0);     //  ID in column 0
		//	log.fine( "Row=" + i + " - " + id);
			if (id != null && id.isSelected())
				results.add(id.getRecord_ID());
		}

		if (results.size() == 0)
			return "";
		log.config("Selected #" + results.size());

		//	Query String
		String keyColumn = "C_Order_ID";
		StringBuffer sb = new StringBuffer(keyColumn);
		if (results.size() > 1)
			sb.append(" IN (");
		else
			sb.append("=");
		//	Add elements
		for (int i = 0; i < results.size(); i++)
		{
			if (i > 0)
				sb.append(",");
			if (keyColumn.toUpperCase().endsWith("_ID"))
				sb.append(results.get(i).toString());
			else
				sb.append("'").append(results.get(i).toString());
		}

		if (results.size() > 1)
			sb.append(")");
		//
		log.config(sb.toString());
		return sb.toString();
	}	//	saveSelection

	
	/**************************************************************************
	 *	Generate Shipments
	 */
	private void generateShipments ()
	{
		log.info("M_Warehouse_ID=" + m_M_Warehouse_ID);
	//	Trx trx = Trx.createTrxName("IOG");	
	//	Trx p_trx = Trx.get(trx, true);	p_trx needs to be committed too
		Trx trx = null;
		Trx p_trx = null;
		
		//	Reset Selection
		/* String sql = "UPDATE C_Order SET IsSelected = 'N' "
			+ "WHERE IsSelected='Y'"
			+ " AND AD_Client_ID=" + Env.getCtx().getAD_Client_ID();
		int no = DB.executeUpdate(sql, trx);
		log.config("Reset=" + no); */

		//	Set Selection
		m_ShipmentGroup_ID = QueryUtil.getSQLValue(trx, "SELECT C_Order_ShipmentGroup_Seq.NEXTVAL FROM DUAL");
		String sql = "UPDATE C_Order SET Shipment_Group_ID = ? "
						+ "WHERE Shipment_Group_ID IS NULL AND " 
						+ m_whereClause;
		int no = DB.executeUpdate(trx, sql, m_ShipmentGroup_ID );
		
		log.fine(sql);
		if (no == 0)
		{
			String msg = "No Shipments";     //  not translated!
			log.config(msg);
			info.setText(msg);
			return;
		}
		log.info("Set Selection #" + no);

		m_selectionActive = false;  //  prevents from being called twice
		statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "InOutGenerateGen"));
		statusBar.setStatusDB(String.valueOf(no));

		//	Prepare Process
		int AD_Process_ID = 199;	  // M_InOutCreate - org.compiere.process.InOutGenerate
		MPInstance instance = new MPInstance(Env.getCtx(), AD_Process_ID, 0);
		if (!instance.save())
		{
			info.setText(Msg.getMsg(Env.getCtx(), "ProcessNoInstance"));
			return;
		}
		ProcessInfo pi = new ProcessInfo ("VInOutGen", AD_Process_ID);
		pi.setAD_PInstance_ID (instance.getAD_PInstance_ID());

		//	Add Parameter - Selection=Y
		MPInstancePara ip = new MPInstancePara(instance, 10);
		ip.setParameter("Selection","N");
		if (!ip.save())
		{
			String msg = "No Parameter added";  //  not translated
			info.setText(msg);
			log.log(Level.SEVERE, msg);
			return;
		}
		//	Add Parameter - M_Warehouse_ID=x
		ip = new MPInstancePara(instance, 20);
		ip.setParameter("M_Warehouse_ID", Integer.parseInt(m_M_Warehouse_ID.toString()));
		if (!ip.save())
		{
			String msg = "No Parameter added";  //  not translated
			info.setText(msg);
			log.log(Level.SEVERE, msg);
			return;
		}

		ip = new MPInstancePara(instance, 30);
		ip.setParameter("Shipment_Group_ID", Integer.parseInt(m_ShipmentGroup_ID.toString()));
		if (!ip.save())
		{
			String msg = "No Parameter added";  //  not translated
			info.setText(msg);
			log.log(Level.SEVERE, msg);
			return;
		}

		//	Execute Process
		ProcessCtl worker = new ProcessCtl(this, pi, p_trx);
		worker.start();     //  complete tasks in unlockUI / generateShipments_complete
		//
	}	//	generateShipments

	/**
	 *  Complete generating shipments.
	 *  Called from Unlock UI
	 *  @param pi process info
	 */
	private void generateShipments_complete (ProcessInfo pi)
	{
		//  Switch Tabs
		tabbedPane.setSelectedIndex(1);
		//
		ProcessInfoUtil.setLogFromDB(pi);
		StringBuffer iText = new StringBuffer();
		iText.append("<b>").append(pi.getSummary())
			.append("</b><br>(")
			.append(Msg.getMsg(Env.getCtx(), "InOutGenerateInfo"))
			//  Shipments are generated depending on the Delivery Rule selection in the Order
			.append(")<br>")
			.append(pi.getLogInfo(true));
		info.setText(iText.toString());

		//	Reset Selection
		String sql = "UPDATE C_Order SET Shipment_Group_ID = NULL WHERE Shipment_Group_ID = ? AND " + m_whereClause;
		int no = DB.executeUpdate((Trx) null, sql, m_ShipmentGroup_ID);
		log.config("Reset=" + no);

		//	Get results
		int[] ids = pi.getIDs();
		if (ids == null || ids.length == 0)
			return;
		log.config("PrintItems=" + ids.length);

		confirmPanelGen.getOKButton().setEnabled(false);
		//	OK to print shipments
		if (ADialog.ask(m_WindowNo, this, "PrintShipments"))
		{
		//	info.append("\n\n" + Msg.getMsg(Env.getCtx(), "PrintShipments"));
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			int retValue = ADialogDialog.A_CANCEL;	//	see also ProcessDialog.printShipments/Invoices
			do
			{
				//	Loop through all items
				for (int M_InOut_ID : ids) {
					ReportEngine re = ReportCtl.startDocumentPrint( Env.getCtx(), ReportEngine.SHIPMENT, M_InOut_ID, true );
					if (re == null) {
						ADialog.error(0, null, CLogger.retrieveError().getName() );
						break;
					}
				}
				ADialogDialog d = new ADialogDialog (m_frame,
					Env.getHeader(Env.getCtx(), m_WindowNo),
					Msg.getMsg(Env.getCtx(), "PrintoutOK?"),
					JOptionPane.QUESTION_MESSAGE);
				retValue = d.getReturnCode();
			}
			while (retValue == ADialogDialog.A_CANCEL);
			setCursor(Cursor.getDefaultCursor());
		}	//	OK to print shipments

		//
		confirmPanelGen.getOKButton().setEnabled(true);
	}   //  generateShipments_complete

	
	/**************************************************************************
	 *  Lock User Interface.
	 *  Called from the Worker before processing
	 *  @param pi process info
	 */
	public void lockUI (ProcessInfo pi)
	{
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		this.setEnabled(false);
	}   //  lockUI

	/**
	 *  Unlock User Interface.
	 *  Called from the Worker when processing is done
	 *  @param pi result of execute ASync call
	 */
	public void unlockUI (ProcessInfo pi)
	{
		this.setEnabled(true);
		this.setCursor(Cursor.getDefaultCursor());
		//
		generateShipments_complete(pi);
	}   //  unlockUI

	/**
	 *  Is the UI locked (Internal method)
	 *  @return true, if UI is locked
	 */
	public boolean isUILocked()
	{
		return this.isEnabled();
	}   //  isUILocked

	/**
	 *  Method to be executed async.
	 *  Called from the Worker
	 *  @param pi ProcessInfo
	 */
	public void executeASync (ProcessInfo pi)
	{
	}   //  executeASync

}	//	VInOutGen
