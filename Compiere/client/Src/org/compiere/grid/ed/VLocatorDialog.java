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
package org.compiere.grid.ed;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.logging.*;

import javax.swing.*;

import org.compiere.apps.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Dialog to enter Warehouse Locator Info
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: VLocatorDialog.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class VLocatorDialog extends CDialog
	implements ActionListener, KeyListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Constructor
	 *  @param frame frame
	 *  @param title title
	 *  @param mLocator locator
	 *  @param M_Locator_ID locator id
	 * 	@param mandatory mandatory
	 * 	@param only_Warehouse_ID of not 0 restrict warehouse
	 */
	public VLocatorDialog (Frame frame, String title, MLocatorLookup mLocator,
		int M_Locator_ID, boolean mandatory, int only_Warehouse_ID)
	{
		super (frame, title, true);
		m_WindowNo = Env.getWindowNo(frame);
		try
		{
			jbInit();
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "VLocatorDialog", ex);
		}
		//
		m_mLocator = mLocator;
		m_M_Locator_ID = M_Locator_ID;
		m_mandatory = mandatory;
		m_only_Warehouse_ID = only_Warehouse_ID;
		//
		initLocator();
		AEnv.positionCenterWindow(frame, this);
	}	//	VLocatorDialog

	private int				m_WindowNo;
	private boolean 		m_change = false;
	private MLocatorLookup	m_mLocator;
	private int				m_M_Locator_ID;
	private boolean			m_mandatory = false;
	private int				m_only_Warehouse_ID = 0;
	//
	private int				m_M_Warehouse_ID;
	private String 			m_M_WarehouseValue;
	private String 			m_Separator;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VLocatorDialog.class);
	//
	private CPanel panel = new CPanel();
	private CPanel mainPanel = new CPanel();
	private CPanel southPanel = new CPanel();
	private BorderLayout panelLayout = new BorderLayout();
	private GridBagLayout gridBagLayout = new GridBagLayout();
	private ConfirmPanel confirmPanel = new ConfirmPanel(true);
	private BorderLayout southLayout = new BorderLayout();
	//
	private VComboBox fLocator = new VComboBox();
	private CComboBox fWarehouse = new CComboBox();
	private JCheckBox fCreateNew = new JCheckBox();
	private CTextField fAisle = new CTextField();
	private CTextField fBay = new CTextField();
	private CTextField fRow = new CTextField();
	private CTextField fPosition = new CTextField();
	private CTextField fBin = new CTextField();
	private JLabel lLocator = new JLabel();
	private CTextField fWarehouseInfo = new CTextField();
	private CTextField fValue = new CTextField();
	private JLabel lWarehouseInfo = new JLabel();
	private JLabel lWarehouse = new JLabel();
	private JLabel lAisle = new JLabel();
	private JLabel lBay = new JLabel();
	private JLabel lRow = new JLabel();
	private JLabel lPosition = new JLabel();
	private JLabel lBin = new JLabel();
	private JLabel lValue = new JLabel();

	/**
	 *	Static component init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		panel.setLayout(panelLayout);
		southPanel.setLayout(southLayout);
		mainPanel.setLayout(gridBagLayout);
		panelLayout.setHgap(5);
		panelLayout.setVgap(10);
		fCreateNew.setText(Msg.getMsg(Env.getCtx(), "CreateNew"));
		fAisle.setColumns(15);
		fBay.setColumns(15);
		fRow.setColumns(15);
		fPosition.setColumns(15);
		fBin.setColumns(15);
		lLocator.setLabelFor(fLocator);
		lLocator.setText(Msg.translate(Env.getCtx(), "M_Locator_ID"));
		fWarehouseInfo.setBackground(CompierePLAF.getFieldBackground_Inactive());
		fWarehouseInfo.setReadWrite(false);
		fWarehouseInfo.setColumns(15);
		fValue.setColumns(15);
		lWarehouseInfo.setLabelFor(fWarehouseInfo);
		lWarehouseInfo.setText(Msg.translate(Env.getCtx(), "M_Warehouse_ID"));
		lWarehouse.setLabelFor(fWarehouse);
		lWarehouse.setText(Msg.translate(Env.getCtx(), "M_Warehouse_ID"));
		lAisle.setLabelFor(fAisle);
		lAisle.setText(Msg.getElement(Env.getCtx(), "X"));
		lBay.setLabelFor(fBay);
		lBay.setText(Msg.getElement(Env.getCtx(), "Y"));
		lRow.setLabelFor(fRow);
		lRow.setText(Msg.getElement(Env.getCtx(), "Z"));
		lPosition.setLabelFor(fPosition);
		lPosition.setText(Msg.getElement(Env.getCtx(), "Position"));
		lBin.setLabelFor(fBin);
		lBin.setText(Msg.getElement(Env.getCtx(), "Bin"));
		lValue.setLabelFor(fValue);
		String label = Msg.translate(Env.getCtx(), "Value");
		lValue.setText(Util.cleanMnemonic(label));
		getContentPane().add(panel);
		panel.add(mainPanel, BorderLayout.CENTER);
		//
		mainPanel.add(lLocator, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(fLocator, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
		mainPanel.add(fCreateNew, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 0, 0));
		mainPanel.add(lWarehouseInfo, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		mainPanel.add(fWarehouseInfo, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
		mainPanel.add(lWarehouse, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		mainPanel.add(fWarehouse, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
		mainPanel.add(lAisle, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		mainPanel.add(fAisle, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
		mainPanel.add(lBay, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		mainPanel.add(fBay, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
		mainPanel.add(lRow, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		mainPanel.add(fRow, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
		mainPanel.add(lPosition, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		mainPanel.add(fPosition, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
		mainPanel.add(lBin, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		mainPanel.add(fBin, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
		mainPanel.add(lValue, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		mainPanel.add(fValue, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
		//
		panel.add(southPanel, BorderLayout.SOUTH);
		southPanel.add(confirmPanel, BorderLayout.NORTH);
		confirmPanel.addActionListener(this);
	}	//	jbInit

	/**
	 *	Dynanmic Init & fill fields
	 */
	private void initLocator()
	{
		log.fine("");

		//	Load Warehouse
		String sql = "SELECT M_Warehouse_ID, Name FROM M_Warehouse";
		if (m_only_Warehouse_ID != 0)
			sql += " WHERE M_Warehouse_ID=" + m_only_Warehouse_ID;
		String SQL = MRole.getDefault().addAccessSQL(
			sql, "M_Warehouse", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO)
			+ " ORDER BY 2";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, (Trx) null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				fWarehouse.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, SQL, e);
		}
		log.fine("Warehouses=" + fWarehouse.getItemCount());

		//	Load valid Locators
		m_mLocator.fillComboBox(m_mandatory, true, true, false);
		log.fine(m_mLocator.toString());
		fLocator.setModel(m_mLocator);
		fLocator.setValue(m_M_Locator_ID);
		fLocator.addActionListener(this);
		displayLocator();
		//
		fCreateNew.setSelected(false);
		if (m_mLocator.isOnly_Outgoing())
			fCreateNew.setVisible(false);
		else
			fCreateNew.addActionListener(this);
		enableNew();
		//
		fWarehouse.addActionListener(this);
		fAisle.addKeyListener(this);
		fBay.addKeyListener(this);
		fRow.addKeyListener(this);
		fPosition.addKeyListener(this);
		fBin.addKeyListener(this);

		//	Update UI
		pack();
	}	//	initLocator


	/**************************************************************************
	 *	ActionListener
	 *  @param e event
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		//
		if (e.getActionCommand().equals(ConfirmPanel.A_OK))
		{
			actionOK();
			m_change = true;
			dispose();
		}
		else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
		{
			m_change = false;
			dispose();
		}
		//	Locator Change
		else if (e.getSource() == fLocator)
			displayLocator();

		//	New Value Change
		else if (source == fCreateNew)
			enableNew();

		//	Entered/Changed data for Value
		else if (fCreateNew.isSelected() && source == fWarehouse)
			createValue();

	}	//	actionPerformed

	/**
	 *	KeyListener - nop
	 *  @param e event
	 */
	public void keyPressed(KeyEvent e)
	{}
	/**
	 *	KeyListener
	 *  @param e event
	 */
	public void keyReleased(KeyEvent e)
	{
		if (fCreateNew.isSelected())
			createValue();
	}
	/**
	 *	KeyListener - nop
	 *  @param e event
	 */
	public void keyTyped(KeyEvent e)
	{}

	/**
	 *	Display value of current locator
	 */
	private void displayLocator()
	{
		KeyNamePair pp = (KeyNamePair)fLocator.getSelectedItem();
		if (pp == null || pp.getKey() == 0)
			return;
		//
		m_M_Locator_ID = pp.getKey();
		MLocator loc = MLocator.get(Env.getCtx(), m_M_Locator_ID);
		fWarehouseInfo.setText(loc.getWarehouseName());
		fAisle.setText(loc.getX());
		fBay.setText(loc.getY());
		fRow.setText(loc.getZ());
		fPosition.setText(loc.getPosition());
		fBin.setText(loc.getBin());
		fValue.setText(loc.getValue());
		getWarehouseInfo(loc.getM_Warehouse_ID());
		//	Set Warehouse
		int size = fWarehouse.getItemCount();
		for (int i = 0; i < size; i++)
		{
			KeyNamePair ppWH = (KeyNamePair)fWarehouse.getItemAt(i);
			if (ppWH.getKey() == loc.getM_Warehouse_ID())
			{
				fWarehouse.setSelectedIndex(i);
				continue;
			}
		}
	}	//	displayLocator

	/**
	 *	Enable/disable New data entry
	 */
	private void enableNew()
	{
		boolean sel = fCreateNew.isSelected();
		lWarehouse.setVisible(sel);
		fWarehouse.setVisible(sel);
		lWarehouseInfo.setVisible(!sel);
		fWarehouseInfo.setVisible(!sel);
		fAisle.setReadWrite(sel);
		fBay.setReadWrite(sel);
		fRow.setReadWrite(sel);
		fPosition.setReadWrite(sel);
		fBin.setReadWrite(sel);
		fValue.setReadWrite(sel);
		pack();
	}	//	enableNew

	/**
	 *	Get Warehouse Info
	 *  @param M_Warehouse_ID warehouse
	 */
	private void getWarehouseInfo (int M_Warehouse_ID)
	{
		if (M_Warehouse_ID == m_M_Warehouse_ID)
			return;
		MWarehouse wh = MWarehouse.get(Env.getCtx(), M_Warehouse_ID);
		m_M_Warehouse_ID = wh.getM_Warehouse_ID();
		m_M_WarehouseValue = wh.getValue();
		m_Separator = wh.getSeparator();
	}	//	getWarehouseInfo

	/**
	 *	Create Locator-Value
	 */
	private void createValue()
	{
		//	Get Warehouse Info
		KeyNamePair pp = (KeyNamePair)fWarehouse.getSelectedItem();
		if (pp == null)
			return;
		getWarehouseInfo(pp.getKey());
		//
		StringBuffer buf = new StringBuffer(m_M_WarehouseValue);
		buf.append(m_Separator).append(fAisle.getText());
		buf.append(m_Separator).append(fBay.getText());
		buf.append(m_Separator).append(fRow.getText());
		buf.append(m_Separator).append(fPosition.getText());
		buf.append(m_Separator).append(fBin.getText());
		fValue.setText(buf.toString());
	}	//	createValue

	/**
	 * 	OK - check for changes (save them) & Exit
	 */
	private void actionOK()
	{
		if (fCreateNew.isSelected())
		{
			//	Get Warehouse Info
			KeyNamePair pp = (KeyNamePair)fWarehouse.getSelectedItem();
			if (pp != null)
				getWarehouseInfo(pp.getKey());

			//	Check mandatory values
			String mandatoryFields = "";
			if (m_M_Warehouse_ID == 0)
				mandatoryFields += lWarehouse.getText() + " - ";
			if (fValue.getText().length()==0)
				mandatoryFields += lValue.getText() + " - ";
			if (fAisle.getText().length()==0)
				mandatoryFields += lAisle.getText() + " - ";
			if (fBay.getText().length()==0)
				mandatoryFields += lBay.getText() + " - ";
			if (fRow.getText().length()==0)
				mandatoryFields += lRow.getText() + " - ";
			if (mandatoryFields.length() != 0)
			{
				ADialog.error(m_WindowNo, this, "FillMandatory", mandatoryFields.substring(0, mandatoryFields.length()-3));
				return;
			}

			MLocator loc = MLocator.get(Env.getCtx(), m_M_Warehouse_ID, fValue.getText(),
				fAisle.getText(), fBay.getText(), fRow.getText(),fPosition.getText(),fBin.getText());
			
			if(loc == null)
			{
				String errorMsg;
				ValueNamePair ep = CLogger.retrieveError();
				if (ep != null)
					errorMsg = ep.getName();
				else
					errorMsg = "Cannot create locator";
				ADialog.error(m_WindowNo, this, errorMsg);
				return;
			}

			m_M_Locator_ID = loc.getM_Locator_ID();
			m_mLocator.refresh();
			fLocator.addItem(loc.getKeyNamePair());
			fLocator.setSelectedItem(loc.getKeyNamePair());
		}	//	createNew
		//
		log.config("M_Locator_ID=" + m_M_Locator_ID);
	}	//	actionOK

	/**
	 *	Get Selected value
	 *  @return value as Integer
	 */
	public Integer getValue()
	{
		KeyNamePair l = (KeyNamePair) fLocator.getSelectedItem();
		if (l != null && l.getKey() != 0)
			return Integer.valueOf (l.getKey());
		return null;
	}	//	getValue

	/**
	 *	Get result
	 *  @return true if changed
	 */
	public boolean isChanged()
	{
		if (m_change)
		{
			KeyNamePair l = (KeyNamePair) fLocator.getSelectedItem();
			if (l != null)
				return l.getKey() == m_M_Locator_ID;
		}
		return m_change;
	}	//	getChange

}	//	VLocatorDialog
