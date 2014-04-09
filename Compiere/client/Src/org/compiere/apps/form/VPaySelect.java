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
import java.math.*;
import java.sql.*;
import java.text.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.event.*;

import org.compiere.apps.*;
import org.compiere.common.constants.*;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.process.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  Create Manual Payments From (AP) Invoices or (AR) Credit Memos.
 *  Allows user to select Invoices for payment.
 *  When Processed, PaySelection is created
 *  and optionally posted/generated and printed
 *
 *  @author Jorg Janke
 *  @version $Id: VPaySelect.java,v 1.3 2006/07/30 00:51:28 jjanke Exp $
 */
public class VPaySelect extends CPanel
	implements FormPanel, ActionListener, TableModelListener, ASyncProcess
{
	/** @todo withholding */
	
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

	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;

	/** Format                  */
	private DecimalFormat   m_format = DisplayType.getNumberFormat(DisplayTypeConstants.Amount);
	/** Bank Balance            */
	private BigDecimal      m_bankBalance = new BigDecimal(0.0);
	/** SQL for Query           */
	private String          m_sql;
	/** Number of selected rows */
	private int             m_noSelected = 0;
	/** Client ID               */
	private int             m_AD_Client_ID = 0;
	/**/
	private boolean         m_isLocked = false;
	/** Payment Selection		*/
	private MPaySelection	m_ps = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VPaySelect.class);

	//
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel parameterPanel = new CPanel();
	private CLabel labelBankAccount = new CLabel();
	private VComboBox fieldBankAccount = new VComboBox();
	private GridBagLayout parameterLayout = new GridBagLayout();
	private CLabel labelBankBalance = new CLabel();
	private CLabel labelCurrency = new CLabel();
	private CLabel labelBalance = new CLabel();
	private VCheckBox onlyDue = new VCheckBox();
	private CLabel labelBPartner = new CLabel();
	private VComboBox fieldBPartner = new VComboBox();
	private JLabel dataStatus = new JLabel();
	private JScrollPane dataPane = new JScrollPane();
	private MiniTable miniTable = new MiniTable();
	private CPanel commandPanel = new CPanel();
	private JButton bCancel = ConfirmPanel.createCancelButton(true);
	private JButton bGenerate = ConfirmPanel.createProcessButton(true);
	private FlowLayout commandLayout = new FlowLayout();
	private JButton bRefresh = ConfirmPanel.createRefreshButton(true);
	private CLabel labelPayDate = new CLabel();
	private VDate fieldPayDate = new VDate();
	private CLabel labelPaymentRule = new CLabel();
	private VComboBox fieldPaymentRule = new VComboBox();

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
		labelBankAccount.setText(Msg.translate(Env.getCtx(), "C_BankAccount_ID"));
		fieldBankAccount.addActionListener(this);
		labelBPartner.setText(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		fieldBPartner.addActionListener(this);
		bRefresh.addActionListener(this);
		labelPayDate.setText(Msg.translate(Env.getCtx(), "PayDate"));
		labelPaymentRule.setText(Msg.translate(Env.getCtx(), "PaymentRule"));
		fieldPaymentRule.addActionListener(this);
		//
		labelBankBalance.setText(Msg.translate(Env.getCtx(), "CurrentBalance"));
		labelBalance.setText("0");
		onlyDue.setText(Msg.getMsg(Env.getCtx(), "OnlyDue"));
		dataStatus.setText(" ");
		//
		bGenerate.addActionListener(this);
		bCancel.addActionListener(this);
		//
		mainPanel.add(parameterPanel, BorderLayout.NORTH);
		parameterPanel.add(labelBankAccount,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldBankAccount,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelCurrency,  new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		parameterPanel.add(labelBankBalance,  new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelBalance,   new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelBPartner,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldBPartner,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(onlyDue,  new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelPayDate,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldPayDate,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelPaymentRule,  new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
						,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldPaymentRule,  new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(bRefresh,    new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
				
		mainPanel.add(dataStatus, BorderLayout.SOUTH);
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
		Ctx ctx = Env.getCtx();

		//  Bank Account Info
		String sql = MRole.getDefault().addAccessSQL(
			"SELECT ba.C_BankAccount_ID,"                       //  1
			+ "b.Name || ' ' || ba.AccountNo AS Name,"          //  2
			+ "ba.C_Currency_ID, c.ISO_Code,"                   //  3..4
			+ "ba.CurrentBalance "                              //  5
			+ "FROM C_Bank b " 
			+ "INNER JOIN C_BankAccount ba ON (b.C_Bank_ID=ba.C_Bank_ID) "
			+ "INNER JOIN C_Currency c ON (ba.C_Currency_ID=c.C_Currency_ID) "
			+ " AND ba.IsActive = 'Y'"
			+ " AND EXISTS (SELECT * FROM C_BankAccountDoc d WHERE d.C_BankAccount_ID=ba.C_BankAccount_ID) "
			+ " AND (ba.AD_Org_ID IN (SELECT ro.AD_Org_ID FROM AD_Role_OrgAccess ro" 
			+ " WHERE ro.AD_Role_ID = ? AND ro.IsActive = 'Y')"
			+ " OR (ba.AD_Org_ID = 0 AND EXISTS (SELECT ro.AD_Org_ID FROM AD_Role_OrgAccess ro" 
			+ " WHERE ro.AD_Role_ID = ? AND ro.IsActive = 'Y')) "
			+ " OR EXISTS (SELECT NULL FROM AD_Role WHERE AD_Role_ID=? AND IsAccessAllOrgs='Y'))"
			+ " ORDER BY 2",
			"b", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, MRole.getDefault().getAD_Role_ID());
			pstmt.setInt(2, MRole.getDefault().getAD_Role_ID());
			pstmt.setInt(3, MRole.getDefault().getAD_Role_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				boolean transfers = false;
				BankInfo bi = new BankInfo (rs.getInt(1), rs.getInt(3),
					rs.getString(2), rs.getString(4),
					rs.getBigDecimal(5), transfers);
				fieldBankAccount.addItem(bi);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		if (fieldBankAccount.getItemCount() == 0)
			ADialog.error(m_WindowNo, this, "VPaySelectNoBank");
		else
			fieldBankAccount.setSelectedIndex(0);

		//  Optional BusinessPartner with unpaid AP Invoices
		KeyNamePair pp = new KeyNamePair(0, "");
		fieldBPartner.addItem(pp);
		sql = MRole.getDefault().addAccessSQL(
			"SELECT bp.C_BPartner_ID, bp.Name FROM C_BPartner bp", "bp", 
			MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO)
			+ " AND EXISTS (SELECT * FROM C_Invoice i WHERE bp.C_BPartner_ID=i.C_BPartner_ID"
			//	X_C_Order.PAYMENTRULE_DirectDebit
			  + " AND (i.IsSOTrx='N' OR (i.IsSOTrx='Y' AND i.PaymentRule='D'))"
			  + " AND i.IsPaid<>'Y') "
			+ "ORDER BY 2";

		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
				fieldBPartner.addItem(pp);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		fieldBPartner.setSelectedIndex(0);

		/**  prepare MiniTable
		 *
		SELECT i.C_Invoice_ID, i.DateInvoiced+p.NetDays AS DateDue,
		bp.Name, i.DocumentNo, c.ISO_Code, i.GrandTotal,
		paymentTermDiscount(i.GrandTotal, i.C_PaymentTerm_ID, i.DateInvoiced, SysDate) AS Discount,
		SysDate-paymentTermDueDays(i.C_PaymentTerm_ID,i.DateInvoiced) AS DiscountDate,
		i.GrandTotal-paymentTermDiscount(i.GrandTotal,i.C_PaymentTerm_ID,i.DateInvoiced,SysDate) AS DueAmount,
		currencyConvert(i.GrandTotal-paymentTermDiscount(i.GrandTotal,i.C_PaymentTerm_ID,i.DateInvoiced,SysDate,null),
			i.C_Currency_ID,xx100,SysDate) AS PayAmt
		FROM C_Invoice i, C_BPartner bp, C_Currency c, C_PaymentTerm p
		WHERE i.IsSOTrx='N'
		AND i.C_BPartner_ID=bp.C_BPartner_ID
		AND i.C_Currency_ID=c.C_Currency_ID
		AND i.C_PaymentTerm_ID=p.C_PaymentTerm_ID
		AND i.DocStatus IN ('CO','CL')
		ORDER BY 2,3
		 */

		m_sql = miniTable.prepareTable(new ColumnInfo[] {
			//  0..4
			new ColumnInfo(" ", "i.C_Invoice_ID", IDColumn.class, false, false, null),
			new ColumnInfo(Msg.translate(ctx, "DueDate"), "i.DateInvoiced+p.NetDays AS DateDue", Timestamp.class, true, true, null),
			new ColumnInfo(Msg.translate(ctx, "C_BPartner_ID"), "bp.Name", KeyNamePair.class, true, false, "i.C_BPartner_ID"),
			new ColumnInfo(Msg.translate(ctx, "DocumentNo"), "i.DocumentNo", String.class),
			new ColumnInfo(Msg.translate(ctx, "C_Currency_ID"), "c.ISO_Code", KeyNamePair.class, true, false, "i.C_Currency_ID"),
			// 5..9
			new ColumnInfo(Msg.translate(ctx, "GrandTotal"), "i.GrandTotal", BigDecimal.class),
			new ColumnInfo(Msg.translate(ctx, "DiscountAmt"), "paymentTermDiscount(i.GrandTotal,i.C_Currency_ID,i.C_PaymentTerm_ID,i.DateInvoiced, ?)", BigDecimal.class),
			new ColumnInfo(Msg.getMsg(ctx, "DiscountDate"), "SysDate-paymentTermDueDays(i.C_PaymentTerm_ID,i.DateInvoiced,SysDate)", Timestamp.class),
			new ColumnInfo(Msg.getMsg(ctx, "AmountDue"), "currencyConvert(invoiceOpen(i.C_Invoice_ID,i.C_InvoicePaySchedule_ID),i.C_Currency_ID, ?,?,i.C_ConversionType_ID, i.AD_Client_ID,i.AD_Org_ID)", BigDecimal.class),
			new ColumnInfo(Msg.getMsg(ctx, "AmountPay"), "currencyConvert(invoiceOpen(i.C_Invoice_ID,i.C_InvoicePaySchedule_ID)-paymentTermDiscount(i.GrandTotal,i.C_Currency_ID,i.C_PaymentTerm_ID,i.DateInvoiced, ?),i.C_Currency_ID, ?,?,i.C_ConversionType_ID, i.AD_Client_ID,i.AD_Org_ID)", BigDecimal.class)
			},
			//	FROM
			"C_Invoice_v i"
			+ " INNER JOIN C_BPartner bp ON (i.C_BPartner_ID=bp.C_BPartner_ID)"
			+ " INNER JOIN C_Currency c ON (i.C_Currency_ID=c.C_Currency_ID)"
			+ " INNER JOIN C_PaymentTerm p ON (i.C_PaymentTerm_ID=p.C_PaymentTerm_ID)",
			//	WHERE
			"i.IsSOTrx=? AND IsPaid='N'"
			//	Different Payment Selection 
			+ " AND NOT EXISTS (SELECT * FROM C_PaySelectionLine psl"
				+ " WHERE i.C_Invoice_ID=psl.C_Invoice_ID AND psl.C_PaySelectionCheck_ID IS NOT NULL AND COALESCE(psl.IsCancelled,'N') = 'N')"
			+ " AND i.DocStatus IN ('CO','CL')"
			+ " AND i.AD_Client_ID=?",	//	additional where & order in loadTableInfo() 
			true, "i");
		//
		miniTable.getModel().addTableModelListener(this);
		//
		fieldPayDate.setMandatory(true);
		fieldPayDate.setValue(new Timestamp(System.currentTimeMillis()));
		//
		m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	}   //  dynInit

	/**
	 *  Load Bank Info - Load Info from Bank Account and valid Documents (PaymentRule)
	 */
	private void loadBankInfo()
	{
		BankInfo bi = (BankInfo)fieldBankAccount.getSelectedItem();
		if (bi == null)
			return;
		labelCurrency.setText(bi.Currency);
		labelBalance.setText(m_format.format(bi.Balance));
		m_bankBalance = bi.Balance;

		//  PaymentRule
		fieldPaymentRule.removeAllItems();
		int AD_Reference_ID = 195;  //  MLookupInfo.getAD_Reference_ID("All_Payment Rule");
		Language language = Env.getLanguage(Env.getCtx());
		MLookupInfo info = MLookupFactory.getLookup_List(language, AD_Reference_ID);
		String infoQuery = info.getQuery();
		String sql = infoQuery.substring(0, infoQuery.indexOf(" ORDER BY"))
			+ " AND " + info.KeyColumn
			+ " IN (SELECT PaymentRule FROM C_BankAccountDoc WHERE C_BankAccount_ID=?) "
			+ infoQuery.substring(infoQuery.indexOf(" ORDER BY"));
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, bi.C_BankAccount_ID);
			ResultSet rs = pstmt.executeQuery();
			ValueNamePair vp = null;
			while (rs.next())
			{
				vp = new ValueNamePair(rs.getString(2), rs.getString(3));   //  returns also not active
				fieldPaymentRule.addItem(vp);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		fieldPaymentRule.setSelectedIndex(0);

	}   //  loadBankInfo

	/**
	 *  Query and create TableInfo
	 */
	private void loadTableInfo()
	{
		log.config("");
		//  not yet initialized
		if (m_sql == null)
			return;

		String sql = m_sql;
		//  Parameters
		Timestamp payDate = (Timestamp)fieldPayDate.getValue();
		miniTable.setColorCompare(payDate);
		log.config("PayDate=" + payDate);
		BankInfo bi = (BankInfo)fieldBankAccount.getSelectedItem();
		//
		String isSOTrx = "N";
		ValueNamePair vp = (ValueNamePair)fieldPaymentRule.getSelectedItem();
		if (vp != null && X_C_Order.PAYMENTRULE_DirectDebit.equals(vp.getValue()))
		{
			isSOTrx = "Y";
			sql += " AND i.PaymentRule='" + X_C_Order.PAYMENTRULE_DirectDebit + "'";
		}
		//
		if (onlyDue.isSelected())
			sql += " AND i.DateInvoiced+p.NetDays <= ?";
		//
		KeyNamePair pp = (KeyNamePair)fieldBPartner.getSelectedItem();
		int C_BPartner_ID = 0;
		if(pp != null)
			C_BPartner_ID = pp.getKey();
		if (C_BPartner_ID != 0)
			sql += " AND i.C_BPartner_ID=?";
		sql += " ORDER BY 2,3";
		//
		log.finest(sql + " - C_Currecny_ID=" + bi.C_Currency_ID + ", C_BPartner_ID=" + C_BPartner_ID);
		
		//  Get Open Invoices
		try
		{
			int index = 1;
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setTimestamp(index++, payDate);		//	DiscountAmt
			pstmt.setInt(index++, bi.C_Currency_ID);	//	DueAmt
			pstmt.setTimestamp(index++, payDate);		
			pstmt.setTimestamp(index++, payDate);		//	PayAmt
			pstmt.setInt(index++, bi.C_Currency_ID);
			pstmt.setTimestamp(index++, payDate);
			pstmt.setString(index++, isSOTrx);			//	IsSOTrx	
			pstmt.setInt(index++, m_AD_Client_ID);		//	Client	
			if (onlyDue.isSelected())
				pstmt.setTimestamp(index++, payDate);
			if (C_BPartner_ID != 0)
				pstmt.setInt(index++, C_BPartner_ID);
			//
			ResultSet rs = pstmt.executeQuery();
			miniTable.loadTable(rs);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		calculateSelection();
	}   //  loadTableInfo

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
		//  Update Bank Info
		if (e.getSource() == fieldBankAccount)
			loadBankInfo();

		//  Generate PaySelection
		else if (e.getSource() == bGenerate)
		{
			generatePaySelect();
			dispose();
		}

		else if (e.getSource() == bCancel)
			dispose();

		//  Update Open Invoices
		else if (e.getSource() == fieldBPartner || e.getSource() == bRefresh)
			loadTableInfo();

	}   //  actionPerformed

	/**
	 *  Table Model Listener
	 *  @param e event
	 */
	public void tableChanged(TableModelEvent e)
	{
		if (e.getColumn() == 0)
			calculateSelection();
	}   //  valueChanged

	/**
	 *  Calculate selected rows.
	 *  - add up selected rows
	 */
	public void calculateSelection()
	{
		m_noSelected = 0;
		BigDecimal invoiceAmt = new BigDecimal(0.0);

		int rows = miniTable.getRowCount();
		for (int i = 0; i < rows; i++)
		{
			IDColumn id = (IDColumn)miniTable.getModel().getValueAt(i, 0);
			if (id.isSelected())
			{
				BigDecimal amt = (BigDecimal)miniTable.getModel().getValueAt(i, 9);
				invoiceAmt = invoiceAmt.add(amt);
				m_noSelected++;
			}
		}

		//  Information
		BigDecimal remaining = m_bankBalance.subtract(invoiceAmt);
		StringBuffer info = new StringBuffer();
		info.append(m_noSelected).append(" ").append(Msg.getMsg(Env.getCtx(), "Selected")).append(" - ");
		info.append(m_format.format(invoiceAmt)).append(", ");
		info.append(Msg.getMsg(Env.getCtx(), "Remaining")).append(" ").append(m_format.format(remaining));
		dataStatus.setText(info.toString());
		//
		bGenerate.setEnabled(m_noSelected != 0);
	}   //  calculateSelection

	/**
	 *  Generate PaySelection
	 */
	private void generatePaySelect()
	{
		log.info("");
	//	Trx trx Trx.createTrxName("PaySelect");
	//	Trx p_trx = Trx.get(trx, true);	p_trx needs to be committed too
		Trx trx = null;
		Trx p_trx = null;
		//
		miniTable.stopEditor(true);
		if (miniTable.getRowCount() == 0)
			return;
		miniTable.setRowSelectionInterval(0,0);
		calculateSelection();
		if (m_noSelected == 0)
			return;

		String paymentRule = ((ValueNamePair)fieldPaymentRule.getSelectedItem()).getValue();

		//  Create Header
		String dateString = DisplayType.getDateFormat(DisplayTypeConstants.Date).format(fieldPayDate.getTimestamp());
		m_ps = new MPaySelection(Env.getCtx(), 0, trx);
		m_ps.setName (Msg.getMsg(Env.getCtx(), "VPaySelect")
				+ " - " + ((ValueNamePair)fieldPaymentRule.getSelectedItem()).getName()
				+ " - " + dateString);
		m_ps.setPayDate (fieldPayDate.getTimestamp());
		BankInfo bi = (BankInfo)fieldBankAccount.getSelectedItem();
		m_ps.setC_BankAccount_ID(bi.C_BankAccount_ID);
		m_ps.setIsApproved(true);
		if (!m_ps.save())
		{
			ADialog.error(m_WindowNo, this, "SaveError", Msg.translate(Env.getCtx(), "C_PaySelection_ID"));
			m_ps = null;
			return;
		}
		log.config(m_ps.toString());

		boolean isSOTrx = false;
		if (X_C_Order.PAYMENTRULE_DirectDebit.equals(paymentRule))
		{
			isSOTrx = true;
		}
		
		//  Create Lines
		int rows = miniTable.getRowCount();
		int line = 0;
		for (int i = 0; i < rows; i++)
		{
			IDColumn id = (IDColumn)miniTable.getModel().getValueAt(i, 0);
			if (id.isSelected())
			{
				line += 10;
				MPaySelectionLine psl = new MPaySelectionLine (m_ps, line, paymentRule);
				int C_Invoice_ID = id.getRecord_ID().intValue();
				BigDecimal OpenAmt = (BigDecimal)miniTable.getModel().getValueAt(i, 8);
				BigDecimal PayAmt = (BigDecimal)miniTable.getModel().getValueAt(i, 9);
				//
				psl.setInvoice(C_Invoice_ID, isSOTrx, 
					OpenAmt, PayAmt, OpenAmt.subtract(PayAmt));
				if (!psl.save(trx))
				{
					ADialog.error(m_WindowNo, this, "SaveError", Msg.translate(Env.getCtx(), "C_PaySelectionLine_ID"));
					return;
				}
				log.fine("C_Invoice_ID=" + C_Invoice_ID + ", PayAmt=" + PayAmt);
			}
		}   //  for all rows in table


		//  Ask to Post it
		if (!ADialog.ask(m_WindowNo, this, "VPaySelectGenerate?", "(" + m_ps.getName() + ")"))
			return;

		//  Prepare Process 
		int AD_Proces_ID = 155;	//	C_PaySelection_CreatePayment
		ProcessInfo pi = new ProcessInfo (m_frame.getTitle(), AD_Proces_ID,
			X_C_PaySelection.Table_ID, m_ps.getC_PaySelection_ID());
		pi.setAD_User_ID (Env.getCtx().getAD_User_ID());
		pi.setAD_Client_ID(Env.getCtx().getAD_Client_ID());

		//	Execute Process
		ProcessCtl.process(this, m_WindowNo, pi, p_trx); 
	//	ProcessCtl worker = new ProcessCtl(this, pi, p_trx);
	//	worker.start();     //  complete tasks in unlockUI
	}   //  generatePaySelect

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

	
	/**************************************************************************
	 *  Bank Account Info
	 */
	public static class BankInfo
	{
		/**
		 * 	BankInfo
		 *	@param newC_BankAccount_ID
		 *	@param newC_Currency_ID
		 *	@param newName
		 *	@param newCurrency
		 *	@param newBalance
		 *	@param newTransfers
		 */
		public BankInfo (int newC_BankAccount_ID, int newC_Currency_ID,
			String newName, String newCurrency, BigDecimal newBalance, boolean newTransfers)
		{
			C_BankAccount_ID = newC_BankAccount_ID;
			C_Currency_ID = newC_Currency_ID;
			Name = newName;
			Currency = newCurrency;
			Balance = newBalance;
		}
		int C_BankAccount_ID;
		int C_Currency_ID;
		String Name;
		String Currency;
		BigDecimal Balance;
		boolean Transfers;

		/**
		 * 	to String
		 *	@return info
		 */
		@Override
		public String toString()
		{
			return Name;
		}
	}   //  BankInfo

}   //  VPaySelect
