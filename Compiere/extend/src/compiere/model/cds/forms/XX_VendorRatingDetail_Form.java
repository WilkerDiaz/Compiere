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
package compiere.model.cds.forms;

import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.sql.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.event.*;

import org.compiere.apps.*;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.apps.form.VPayPrint;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.process.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  @author Jorge E. Pires G.
 *  @version $Id: VPaySelect.java,v 1.3 2006/07/30 00:51:28 jjanke Exp $
 */
public class XX_VendorRatingDetail_Form extends CPanel
	implements FormPanel, ActionListener, TableModelListener, ASyncProcess
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
		m_C_BPartner_ID = (Env.getCtx()).getContextAsInt("#vendorRating_ID");
		(Env.getCtx()).remove("#vendorRating_ID");
		
		log.info("");
		m_WindowNo = WindowNo;
		m_frame = frame;
		try
		{
			jbInit();
			dynInit();
			frame.getContentPane().add(commandPanel, BorderLayout.SOUTH);
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}	//	init

	
	private static Integer m_C_BPartner_ID = null;
	
	
	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;

	/** SQL for Query           */
	private static StringBuffer          m_sql;
	/**/
	private boolean         m_isLocked = false;
	/** Payment Selection		*/
	private MPaySelection	m_ps = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(XX_VendorRatingDetail_Form.class);

	
	//
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel parameterPanel = new CPanel();
	private GridBagLayout parameterLayout = new GridBagLayout();
	private CLabel labelBPartner = new CLabel();
	private CTextField fieldBPartner = new CTextField(); 
	private JScrollPane dataPane = new JScrollPane();
	private MiniTable miniTable = new MiniTable();
	private CPanel commandPanel = new CPanel();
	private JButton bCancel = ConfirmPanel.createCancelButton(true);
	private JButton bGenerate = ConfirmPanel.createRefreshButton(true);
	private FlowLayout commandLayout = new FlowLayout();


	private static Integer tableInit_option = null;

	private static CCheckBox checkDetalle = new CCheckBox();
	private CLabel labelCheckDetalle = new CLabel();
	
	private static String m_orderBy = "";

	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		CompiereColor.setBackground(this);
		//
		mainPanel.setLayout(mainLayout);
		parameterPanel.setLayout(parameterLayout);
		//
		labelBPartner.setText(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		labelCheckDetalle.setText(Msg.translate(Env.getCtx(), "Summary"));
		//fieldBPartner.addActionListener(this);
		fieldBPartner.setText(new MBPartner(Env.getCtx(), m_C_BPartner_ID, null).getName());
		fieldBPartner.setReadWrite(false);
		//
		bGenerate.addActionListener(this);
		bCancel.addActionListener(this);
		//
		mainPanel.add(parameterPanel, BorderLayout.NORTH);
		parameterPanel.add(labelBPartner,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldBPartner,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(labelCheckDetalle,   new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(checkDetalle,   new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(dataPane, BorderLayout.CENTER);
		
		dataPane.getViewport().add(miniTable, null);
		//
		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.RIGHT);
		commandLayout.setHgap(10);
		commandPanel.add(bCancel, null);
		commandPanel.add(bGenerate, null);
	}   //  jbInit

	/**
	 *  Dynamic Init.
	 *  - Load Bank Info
	 *  - Load BPartner
	 *  - Init Table
	 */
	private void dynInit()
	{
		ColumnInfo[] layout = new ColumnInfo[] {
				new ColumnInfo(Msg.translate(Env.getCtx(), "DocumentNo"),   ".", KeyNamePair.class),            //  1
				new ColumnInfo(Msg.translate(Env.getCtx(), "EvaluationCriteria"),   ".", KeyNamePair.class),            //  2
				new ColumnInfo(Msg.translate(Env.getCtx(), "Point"),   ".", BigDecimal.class)             //  3
			};
		
		miniTable.prepareTable(layout, "", "", false, "");
		miniTable.setAutoResizeMode(3);
		
		tableInit_option=0;
		tableInit();
		tableLoad (miniTable);
		
	}   //  dynInit

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose

	
	/**************************************************************************
	 *  ActionListener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{

		if (e.getSource() == bGenerate){
			actualizar();
		}
		else if (e.getSource() == bCancel)
			dispose();

	}   //  actionPerformed

	private void actualizar() {
		if (checkDetalle.isSelected()){
			//if(tableInit_option == 0){
				tableInit_option=1;
				tableInit();
				tableLoad (miniTable);				
			//}
		}else{
			//if(tableInit_option == 1){
				tableInit_option=0;
				tableInit();
				tableLoad (miniTable);				
			//}
		}
	}

	/**
	 *  Table Model Listener
	 *  @param e event
	 */
	public void tableChanged(TableModelEvent e)
	{
		if (e.getColumn() == 0){
			
		}
	}   //  valueChanged

	/**
	 *  Lock User Interface
	 *  Called from the Worker before processing
	 *  @param pi process info
	 */
	public void lockUI (ProcessInfo pi)
	{
		this.setEnabled(false);
		m_isLocked = true;
	}   //  lockUI

	/**
	 *  Unlock User Interface.
	 *  Called from the Worker when processing is done
	 *  @param pi process info
	 */
	public void unlockUI (ProcessInfo pi)
	{
	//	this.setEnabled(true);
	//	m_isLocked = false;
		//  Ask to Print it		//	Window is disposed
		if (!ADialog.ask(0, this, "VPaySelectPrint?", "(" + pi.getSummary() + ")"))
			return;

		//  Start PayPrint
		int AD_Form_ID = 106;	//	Payment Print/Export
		FormFrame ff = new FormFrame();
		ff.openForm (AD_Form_ID);
		//	Set Parameter
		if (m_ps != null)
		{
			VPayPrint pp = (VPayPrint)ff.getFormPanel();
			pp.setPaySelection(m_ps.getC_PaySelection_ID());
		}
		//
		ff.pack();
		this.setVisible(false);
		AEnv.showCenterScreen(ff);
		this.dispose();
	}   //  unlockUI

	/**
	 *  Is the UI locked (Internal method)
	 *  @return true, if UI is locked
	 */
	public boolean isUILocked()
	{
		return m_isLocked;
	}   //  isLoacked

	/**
	 *  Method to be executed async.
	 *  Called from the ASyncProcess worker
	 *  @param pi process info
	 */
	public void executeASync (ProcessInfo pi)
	{
		log.config("-");
	}   //  executeASync

	private static void tableLoad (MiniTable table)
	{
	//	log.finest(m_sql + " - " +  m_groupBy);
		String sql = MRole.getDefault().addAccessSQL(
			m_sql.toString(), "VR", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO) + m_orderBy;
		
		log.finest(sql);
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			table.loadTable(rs);
			stmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}   //  tableLoad
	
	private static void tableInit ()
	{
		m_sql = new StringBuffer ();

		if(tableInit_option==0)
		{
			m_sql.append("SELECT PO.DOCUMENTNO, VR.XX_C_Order_ID, EC.NAME, VR.XX_VCN_EVALUATIONCRITERIA_ID, VR.XX_Points " +
					"FROM XX_VCN_VendorRating VR, C_ORDER PO, XX_VCN_EVALUATIONCRITERIA EC " +
					"WHERE VR.C_BPartner_ID = " +
					m_C_BPartner_ID + " " +
					"AND PO.C_ORDER_ID = VR.XX_C_Order_ID " +
					"AND EC.XX_VCN_EVALUATIONCRITERIA_ID = VR.XX_VCN_EVALUATIONCRITERIA_ID ");				
				m_orderBy  = " order by VR.XX_VCN_VENDORRATING_ID";
			
		}else if(tableInit_option==1)
		{
			m_sql.append("SELECT PO.DOCUMENTNO, VR.XX_C_Order_ID, EC.NAME, VR.XX_VCN_EVALUATIONCRITERIA_ID, VR.XX_Points " +
					"FROM XX_VCN_VendorRating VR, C_ORDER PO, XX_VCN_EVALUATIONCRITERIA EC " +
					"WHERE VR.C_BPartner_ID = " +
					m_C_BPartner_ID + " " +
					"AND PO.C_ORDER_ID = VR.XX_C_Order_ID " +
					"AND EC.XX_VCN_EVALUATIONCRITERIA_ID = VR.XX_VCN_EVALUATIONCRITERIA_ID " +
					"AND (EC.XX_VCN_EVALUATIONCRITERIA_ID = "+Env.getCtx().getContext("#XX_L_EC_SCOREACCUMUPREVI_ID")+" " +
					"OR EC.XX_VCN_EVALUATIONCRITERIA_ID = "+Env.getCtx().getContext("#XX_L_EC_TOTALSCOREOC_ID")+") ");
				m_orderBy  = " order by VR.XX_VCN_VENDORRATING_ID";		
		}	
	}   //  tableInit

}   //  VPaySelect
