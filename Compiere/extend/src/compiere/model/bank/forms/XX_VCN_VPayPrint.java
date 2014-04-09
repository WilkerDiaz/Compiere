//IVAN VALDES MIGLIORE
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
package compiere.model.bank.forms;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.*;

import javax.swing.*;

import org.compiere.apps.*;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.apps.form.VPayPrint;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.process.DocumentEngine;
import org.compiere.swing.*;
import org.compiere.util.*;

import compiere.model.bank.MVCNCheck;
import compiere.model.bank.XX_VCN_DetalleContable;
import compiere.model.bank.XX_VCN_PrintList;
import compiere.model.birt.BIRTReport;
import compiere.model.cds.MPayment;
import compiere.model.payments.X_XX_VCN_AccoutingEntry;

public class XX_VCN_VPayPrint extends CPanel implements FormPanel, ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//VARIABLES GLOBALES
	private String bankAccount_ID="";
	private java.math.BigDecimal montoTotal;

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
			frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
			frame.getContentPane().add(southPanel, BorderLayout.SOUTH);
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
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VPayPrint.class);
	
	//  Static Variables
	private CPanel centerPanel = new CPanel();
	private CPanel southPanel = new CPanel();
	private FlowLayout southLayout = new FlowLayout();
	private GridBagLayout centerLayout = new GridBagLayout();
	private JButton bPrint = ConfirmPanel.createPrintButton(true);
	private JButton bCancel = ConfirmPanel.createCancelButton(true);
	private JCheckBox fSelections = new JCheckBox();
	private JCheckBox fAnticipo = new JCheckBox();
	private JCheckBox fManualCheck = new JCheckBox();
	private CLabel lPaySelect = new CLabel();
	private CComboBox fPaySelect = new CComboBox();
	private CLabel lBank = new CLabel();
	private CComboBox fBank = new CComboBox();
	private CLabel lPaymentRule = new CLabel();
	private CComboBox fPaymentRule = new CComboBox();
	private CLabel lDocumentNo = new CLabel();
	private CComboBox fCheque = new CComboBox();
	
	//VARIABLES MIAS
	static ArrayList<XX_VCN_PrintList> listprint = new ArrayList<XX_VCN_PrintList>();
	public static int zz=1;
	

	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		CompiereColor.setBackground(this);
		//
		southPanel.setLayout(southLayout);
		southLayout.setAlignment(FlowLayout.RIGHT);
		centerPanel.setLayout(centerLayout);
		//
		bPrint.addActionListener(this);
		bCancel.addActionListener(this);
		//
		fManualCheck.setText("Es cheque Manual");
		fManualCheck.setSelected(false);
		fManualCheck.addActionListener(this);
		//
		fSelections.setText("Es selección de Pago");
		fSelections.setSelected(true);
		fSelections.addActionListener(this);
		//
		fAnticipo.setText("Es anticipo de Pago");
		fAnticipo.addActionListener(this);
		fAnticipo.addActionListener(this);
		//
		lPaySelect.setText("Pago");
		fPaySelect.addActionListener(this);
		//
		lBank.setText("Cuenta bancaria");
		fBank.setPreferredSize(new Dimension(400, 22));
		fBank.addActionListener(this);
		//
		lPaymentRule.setText(Msg.translate(Env.getCtx(), "PaymentRule"));
		fPaymentRule.addActionListener(this);
		//
		lDocumentNo.setText("Cheque");
		//
		southPanel.add(bCancel, null);
		southPanel.add(bPrint, null);
		//
		centerPanel.add(fSelections,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1, 0, 5, 5), 0, 0));
		centerPanel.add(fAnticipo,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1, 150, 5, 5), 0, 0));
		centerPanel.add(fManualCheck,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1, 290, 5, 5), 0, 0));
		centerPanel.add(lPaySelect,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		centerPanel.add(fPaySelect,    new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 12), 0, 0));
		centerPanel.add(lBank,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		centerPanel.add(fBank,    new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 12), 0, 0));
		centerPanel.add(lPaymentRule,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		centerPanel.add(fPaymentRule,    new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 12), 0, 0));
		centerPanel.add(lDocumentNo,   new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 5, 5), 0, 0));
		centerPanel.add(fCheque,    new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 12), 0, 0));
	}   //  VPayPrint

	/**
	 *  Dynamic Init
	 */
	private void dynInit()
	{
		
		log.config("");
		loadPaySelections();
	}   //  dynInit

	/**
	 * 	Dispose METODO QUE CIERRA LA VENTANA
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose

	/**
	 *  Load Pay Selections METODO QUE CARGA LAS SELECCIONES DE PAGO
	 */
	private void loadPaySelections()
	{		
		fPaySelect.removeAllItems();
		int AD_Client_ID = Env.getCtx().getAD_Client_ID();
		//  Load PaySelect
		StringBuffer sql = new StringBuffer("SELECT C_PaySelection_ID, Name || ' - ' || TotalAmt"
											+" FROM C_PaySelection "
											+" WHERE AD_Client_ID=? AND Processed='Y' AND IsActive='Y' AND PROCESSING = 'N'"
											+" ORDER BY PayDate DESC, C_PaySelection_ID DESC");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
			pstmt.setInt(1, AD_Client_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				ValueNamePair pp = new ValueNamePair(Integer.toString(rs.getInt(1)), rs.getString(2));
				fPaySelect.addItem(pp);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		if (fPaySelect.getItemCount() == 0)
			ADialog.info(m_WindowNo, this, "VPayPrintNoRecords");
	}
	
	/**
	 *  Load Payment METODO QUE CARGA LOS ANTICIPOS DE PAGO 
	 */
	private void loadPayment()
	{		
		fPaySelect.removeAllItems();
		int AD_Client_ID = Env.getCtx().getAD_Client_ID();
		//  Load PaySelect
		StringBuffer sql = new StringBuffer("SELECT P.C_PAYMENT_ID, B.NAME || ' - ' || P.PayAmt || ' BSf'"
											+" FROM C_PAYMENT P, C_BPARTNER B"
											+" WHERE P.AD_Client_ID=? AND P.Processed='Y' AND P.IsActive='Y' AND P.PROCESSING = 'N'"
											+" AND P.IsApproved = 'Y' AND P.XX_ISADVANCE = 'Y'"
											+" AND P.C_BPartner_ID = B.C_BPartner_ID"
											+" ORDER BY P.CREATED DESC, P.C_PAYMENT_ID DESC");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
			pstmt.setInt(1, AD_Client_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				ValueNamePair pp = new ValueNamePair(Integer.toString(rs.getInt(1)), rs.getString(2));
				fPaySelect.addItem(pp);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		if (fPaySelect.getItemCount() == 0)
			ADialog.info(m_WindowNo, this, "PaymentNoRecords");
	}
	
	/**
	 *  Load Payment METODO QUE CARGA LOS CHEQUES MANUALES
	 */
	private void loadManualCheck()
	{		
		fPaySelect.removeAllItems();
		//  Load PaySelect
		StringBuffer sql = new StringBuffer("SELECT P.C_PAYMENT_ID, B.NAME || ' - ' || P.PayAmt || ' BSf'"
											+" FROM C_PAYMENT P, C_BPARTNER B"
											+" WHERE P.DOCSTATUS='CO' AND  P.XX_VCN_MANUALCHECK= 'Y' AND P.PROCESSING='N'"
											+" AND P.C_BPartner_ID = B.C_BPartner_ID"
											+" ORDER BY P.CREATED DESC, P.C_PAYMENT_ID DESC");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				ValueNamePair pp = new ValueNamePair(Integer.toString(rs.getInt(1)), rs.getString(2));
				fPaySelect.addItem(pp);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		if (fPaySelect.getItemCount() == 0)
			ADialog.info(m_WindowNo, this, "PaymentNoRecords");
	}
	
	/**
	 * 	Set Payment Selection
	 *	@param C_PaySelection_ID id
	 */
	public void setPaySelection (int C_PaySelection_ID)
	{
		if (C_PaySelection_ID == 0)
			return;
		//
		for (int i = 0; i < fPaySelect.getItemCount(); i++)
		{
			KeyNamePair pp = (KeyNamePair)fPaySelect.getItemAt(i);
			if (pp.getKey() == C_PaySelection_ID)
			{
				fPaySelect.setSelectedIndex(i);
				return;
			}
		}
	}	//	setsetPaySelection

	
	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(fSelections)){
			//CARGAR SELECCIONES DE PAGO
			loadPaySelections();
			if (fAnticipo.isSelected())
				fAnticipo.setSelected(false);
			if (fManualCheck.isSelected())
				fManualCheck.setSelected(false);
		}
		else if (e.getSource() == fPaySelect){
			loadPaySelectInfo();
		}
		else if (e.getSource() == fAnticipo){
			//CARGAR ANTICIPOS DE PAGO
			loadPayment();
			if (fSelections.isSelected())
				fSelections.setSelected(false);
			if (fManualCheck.isSelected())
				fManualCheck.setSelected(false);
		}
		else if (e.getSource() == fManualCheck){
			//CARGAR Cheques Manuales
			loadManualCheck();
			if (fSelections.isSelected())
				fSelections.setSelected(false);
			if (fAnticipo.isSelected())
				fAnticipo.setSelected(false);
		}
		else if (e.getSource() == fPaymentRule){
			setCheckNro();}
		else if (e.getSource() == fBank){
			setCheckNro();
		}
		//
		else if (e.getSource() == bCancel)
			dispose();
		else if (e.getSource() == bPrint)
			cmd_print();
	}   //  actionPerformed

	/**
	 *  PaySelect changed - load Bank METODO QUE SE ENCARGA DE CARGAR LOS BANCOS
	 */
	private void loadPaySelectInfo()
	{
		
		//  LOAD BANCOS Y SUS CUENTAS
		//BANCOS-> BANCO VENEZUELA, VENEZOLANO DE CREDITO, MERCANTIL, PROVINCIAL, EXTERIOR, OCCIDENTAL DE DESCUENTO, CORP BANCA, BANESCO Y DEL TESORO
		String sql = 
				"SELECT  ba.C_BankAccount_ID, b.Name || ' - ' || ba.AccountNo, c.ISO_Code, CurrentBalance"
				+" FROM C_BankAccount ba, C_Bank b, C_Currency c "
				+" WHERE ba.C_Bank_ID=b.C_Bank_ID AND ba.C_Currency_ID=c.C_Currency_ID AND"
				+" b.NAME='Banco de Venezuela'"
				+" UNION"
				+" SELECT  ba.C_BankAccount_ID, b.Name || ' ' || ba.AccountNo, c.ISO_Code, CurrentBalance"
				+" FROM C_BankAccount ba, C_Bank b, C_Currency c"
				+" WHERE ba.C_Bank_ID=b.C_Bank_ID AND ba.C_Currency_ID=c.C_Currency_ID AND" 
				+" b.NAME='Venezolano de  Crédito'"
				+" UNION"
				+" SELECT  ba.C_BankAccount_ID, b.Name || ' ' || ba.AccountNo, c.ISO_Code, CurrentBalance"
				+" FROM C_BankAccount ba, C_Bank b, C_Currency c"
				+" WHERE ba.C_Bank_ID=b.C_Bank_ID AND ba.C_Currency_ID=c.C_Currency_ID AND" 
				+" b.NAME='Banco Mercantil'"
				+" UNION"
				+" SELECT  ba.C_BankAccount_ID, b.Name || ' ' || ba.AccountNo, c.ISO_Code, CurrentBalance"
				+" FROM C_BankAccount ba, C_Bank b, C_Currency c"
				+" WHERE ba.C_Bank_ID=b.C_Bank_ID AND ba.C_Currency_ID=c.C_Currency_ID AND" 
				+" b.NAME='BBVA Banco Provincial'"
				+" UNION"
				+" SELECT  ba.C_BankAccount_ID, b.Name || ' ' || ba.AccountNo, c.ISO_Code, CurrentBalance"
				+" FROM C_BankAccount ba, C_Bank b, C_Currency c"
				+" WHERE ba.C_Bank_ID=b.C_Bank_ID AND ba.C_Currency_ID=c.C_Currency_ID AND" 
				+" b.NAME='Banco Exterior'"
				+" UNION"
				+" SELECT  ba.C_BankAccount_ID, b.Name || ' ' || ba.AccountNo, c.ISO_Code, CurrentBalance"
				+" FROM C_BankAccount ba, C_Bank b, C_Currency c"
				+" WHERE ba.C_Bank_ID=b.C_Bank_ID AND ba.C_Currency_ID=c.C_Currency_ID AND" 
				+" b.NAME='Banco Occidental de Descuento '"
				+" UNION"
				+" SELECT  ba.C_BankAccount_ID, b.Name || ' ' || ba.AccountNo, c.ISO_Code, CurrentBalance"
				+" FROM C_BankAccount ba, C_Bank b, C_Currency c"
				+" WHERE ba.C_Bank_ID=b.C_Bank_ID AND ba.C_Currency_ID=c.C_Currency_ID AND" 
				+" b.NAME='Corp Banca'"
				+" UNION"
				+" SELECT  ba.C_BankAccount_ID, b.Name || ' ' || ba.AccountNo, c.ISO_Code, CurrentBalance"
				+" FROM C_BankAccount ba, C_Bank b, C_Currency c"
				+" WHERE ba.C_Bank_ID=b.C_Bank_ID AND ba.C_Currency_ID=c.C_Currency_ID AND" 
				+" b.NAME='Banesco Banco Universal'"
				+" UNION"
				+" SELECT  ba.C_BankAccount_ID, b.Name || ' ' || ba.AccountNo, c.ISO_Code, CurrentBalance"
				+" FROM C_BankAccount ba, C_Bank b, C_Currency c"
				+" WHERE ba.C_Bank_ID=b.C_Bank_ID AND ba.C_Currency_ID=c.C_Currency_ID AND" 
				+" b.NAME='BANCO DEL TESORO'";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery();
			
				fBank.removeAllItems();
				while (rs.next())
				{
					ValueNamePair pp = new ValueNamePair(Integer.toString(rs.getInt(1)), rs.getString(2));
					fBank.addItem(pp);
				}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		loadPaymentRule();
	}   //  loadPaySelectInfo

	/**
	 *  Bank changed - load PaymentRule METODO QUE SE ENCARGA DE CARGAR LA FORMA DE PAGO
	 */
	private void loadPaymentRule()
	{
		
		log.info("");
		// load PaymentRule for Bank
				fPaymentRule.removeAllItems();
				
				ValueNamePair pp = new ValueNamePair("S", "CHEQUE");
				fPaymentRule.addItem(pp);
				setCheckNro();
	}   //  loadPaymentRule
	

	/**
	 *METODO QUE SE ENCARGARA DE BUSCAR EL CHEQUE DE ESA CUENTA BANCARIA  
	 */
	public void setCheckNro(){
		
		if( fBank.getSelectedItem() != null)
		{
			fCheque.removeAllItems();
			int cuenta = Integer.parseInt(((ValueNamePair)fBank.getSelectedItem()).getValue());

					
			String sql=
					"SELECT C.XX_VCN_CHECK_ID, C.XX_VCN_CHECKNUMBER"
					+" FROM C_BANKACCOUNT BA, C_BANKACCOUNTDOC BAD, XX_VCN_CHECK C"
					+" WHERE BA.C_BANKACCOUNT_ID = "+ cuenta +"AND BA.C_BANKACCOUNT_ID = BAD.C_BANKACCOUNT_ID AND BAD.XX_VCN_STATEOFCHECKSBOOK = 1 AND"
					+" C.C_BANKACCOUNTDOC_ID = BAD.C_BANKACCOUNTDOC_ID"
					+" AND C.XX_VCN_LOCATIONOFCHECKS= 1 AND C.ISACTIVE='Y'"
					+" ORDER BY C.XX_VCN_CHECKNUMBER ASC";

					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try
					{
						pstmt = DB.prepareStatement(sql, (Trx) null);
						rs = pstmt.executeQuery();
						
							if (rs.next())
							{
								ValueNamePair pp = new ValueNamePair(Integer.toString(rs.getInt(1)), rs.getString(2));
								fCheque.addItem(pp);
							}
					}
					catch (SQLException e)
					{
								log.log(Level.SEVERE, sql, e);
					}
					finally
					{
								DB.closeResultSet(rs);
								DB.closeStatement(pstmt);
					}
		}
	}//setCheckNro
	/**
	 *  METODO QUE SE ENCARGA DE REALIZAR EL PAGO DE UN ANTICIPO O SELECION DE PAGO Y LUEGO IMPRIMIR EL CHEQUE 
	 */
	private void cmd_print()
	{
		if (fPaySelect.getItemCount() == 0 || fPaymentRule.getItemCount() == 0 || fCheque.getItemCount() == 0|| fBank.getItemCount() == 0){
			ADialog.info(m_WindowNo, this, "Existe un campo vacío");
		}
		else{
			//OBTENGO EL ID DEL ANTICIPO O LA SELECCION DE PAGO
			String Pay = ((ValueNamePair)fPaySelect.getSelectedItem()).getValue();
			//OBTENGO EL ID DEL CHEQUE
			String checkId = ((ValueNamePair)fCheque.getSelectedItem()).getValue();
			//OBTENGO EL NUMERO DEL CHEQUE
			String checkNro = ((ValueNamePair)fCheque.getSelectedItem()).getName();
			Trx trans = Trx.get("XX_VCN_PRINTCHECK");
			//SETEO LA VARIABLE QUE VA A CONTENER LA CUENTA BANCARIA
			bankAccount_ID= ((ValueNamePair)fBank.getSelectedItem()).getValue();
	
			//VERIFICO QUE VOY A CANCELAR
			if(fAnticipo.isSelected()){
				payAnticipo(Integer.parseInt(Pay), trans, Integer.parseInt(checkId), Integer.parseInt(checkNro));
				//ADialog.info(1,  new Container(), "El Anticipo de pago ha sido cancelado");
				trans.commit();
				trans.close();
				loadPayment();
				//EJECUTO EL SQL QUE CONTIENE LOS PARAMETROS PARA EL REPORTE
				String sql=parametrosAnticipo(Integer.parseInt(Pay));
							PreparedStatement prst = DB.prepareStatement(sql,null);
						   	ResultSet rs = null;
						   	//EJECUTO LA SENTENCIA SQL
							   	try {
							   		rs = prst.executeQuery();
							   		if (rs.next()){
							   			BigDecimal total= rs.getBigDecimal("TOTAL");
							   			String beneficiario = rs.getString("BENEFICIARIO");
							   			//BUSCA LA RELACION
							   			String relacion=buscarRelacion(beneficiario);
							   			if (relacion != null)
							   				beneficiario=relacion;
							   			String formato = rs.getString("FORMATO");
							   			reporte(total,beneficiario,formato);
							   			
							   			//LLAMO AL REPORTE DEL VOUCHER
								   			//LOAD C_PAYMENT
							   				MPayment pay = new MPayment (Env.getCtx(), Integer.parseInt(Pay), null);
							   				int org_ID= pay.getAD_Client_ID();
							   				String empresa= empresaEmisora(org_ID);
							   				
							   			reporte1(Integer.parseInt(Pay),empresa);
							   			
							   		}// Fin if
							   	} 
							   	catch (Exception e){
									System.out.println(e);
								}
							   	finally {
							   	//CERRAR CONEXION
									DB.closeResultSet(rs);
									DB.closeStatement(prst);
								}
			}
			else if(fSelections.isSelected()){
				paySeleccion(Integer.parseInt(Pay), trans, Integer.parseInt(checkId), Integer.parseInt(checkNro));
				//ADialog.info(1,  new Container(), "Selección de Pago ha sido cancelada");
				trans.commit();
				trans.close();
				loadPaySelections();
				//EJECUTO EL SQL QUE CONTIENE LOS PARAMETROS PARA EL REPORTE
				String sql=parametrosSeleccion(Integer.parseInt(Pay));
							PreparedStatement prst = DB.prepareStatement(sql,null);
						   	ResultSet rs = null;
						   	//EJECUTO LA SENTENCIA SQL
							   	try {
							   		rs = prst.executeQuery();
							   		if (rs.next()){
							   			BigDecimal total= rs.getBigDecimal("TOTAL");
							   			String beneficiario = rs.getString("BENEFICIARIO");
							   			//BUSCA LA RELACION DEL SOCIO DE NEGOCIO
							   			String relacion=buscarRelacion(beneficiario);
							   			if (relacion != null)
							   				beneficiario=relacion;
							   			String formato = rs.getString("FORMATO");
							   			reporte(total,beneficiario,formato);
							   		}// Fin if
							   	} 
							   	catch (Exception e){
									System.out.println(e);
								}
							   	finally {
							   	//CERRAR CONEXION
									DB.closeResultSet(rs);
									DB.closeStatement(prst);
								}
						
			}
			else if(fManualCheck.isSelected()){
				payManualCheck(Integer.parseInt(Pay), trans, Integer.parseInt(checkId), Integer.parseInt(checkNro));
				//ADialog.info(1,  new Container(), "El Pago manual ha sido cancelado");
				trans.commit();
				trans.close();
				loadManualCheck();
				//EJECUTO EL SQL QUE CONTIENE LOS PARAMETROS PARA EL REPORTE
				String sql=parametrosAnticipo(Integer.parseInt(Pay));
							PreparedStatement prst = DB.prepareStatement(sql,null);
						   	ResultSet rs = null;
						   	//EJECUTO LA SENTENCIA SQL
							   	try {
							   		rs = prst.executeQuery();
							   		if (rs.next()){
							   			BigDecimal total= rs.getBigDecimal("TOTAL");
							   			String beneficiario = rs.getString("BENEFICIARIO");
							   			//BUSCA LA RELACION
							   			String relacion=buscarRelacion(beneficiario);
							   			if (relacion != null)
							   				beneficiario=relacion;
							   			String formato = rs.getString("FORMATO");
							   			reporte(total,beneficiario,formato);
							   			
							   			//LLAMO AL REPORTE DEL VOUCHER
							   			//LOAD C_PAYMENT
						   				MPayment pay = new MPayment (Env.getCtx(), Integer.parseInt(Pay), null);
						   				int org_ID= pay.getAD_Client_ID();
						   				String empresa= empresaEmisora(org_ID);
						   				
						   			reporte1(Integer.parseInt(Pay),empresa);

							   		
							   		}// Fin if
							   	} 
							   	catch (Exception e){
									System.out.println(e);
								}
							   	finally {
							   	//CERRAR CONEXION
									DB.closeResultSet(rs);
									DB.closeStatement(prst);
								}

			}
	        
		}//FIN DEL ELSE SI NO HAY CAMPO VACIO
	}   //  cmd_print
	
/*
* METODO QUE PAGA LOS ANTICIPOS
*/
	private void payAnticipo(int Id, Trx trans, int checkId, int CheckNo) {
		// TODO Auto-generated method stub
		
		java.util.Date utilDate = new java.util.Date(); //FECHA ACTUAL
		long lnMilisegundos = utilDate.getTime();
		java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);

		//CREO EL PAGO C_PAYMENT
		MPayment pay = new MPayment (Env.getCtx(), Id, trans);

		//ASIGNO EL TIPO DE DOCUMENTO
		pay.setC_DocType_ID(buscarTipoDocumento());
		
		//ASIGNO LA CUENTA BANCARIA DE LA CUAL HICE EL PAGO
		pay.setC_BankAccount_ID(Integer.parseInt(bankAccount_ID));
		
		//ASIGNO LA FECHA DE LA TRANSACCION
		pay.setDateTrx(sqlTimestamp);
		
		//ASIGNO LA FECHA CONTABLE
		pay.setDateAcct(sqlTimestamp);
		
		//ASIGNO PROCESSED
		pay.setProcessed(true);
		
		//ASIGNO EL NOMBRE DEL SOCIO DE NEGOCIO
			//OBTENGO EL NOMBRE DEL SOCIO DE NEGOCIO
			String nombresocio=buscarSocioDeNegocios(pay.getC_BPartner_ID());
			//OBTENGO LA RELACION
			String relacion= buscarRelacion(nombresocio);
			
			if(relacion!= null) nombresocio=relacion;
			pay.setA_Name(nombresocio);
		
		//ASIGNO LA FORMA EN QUE LA CANCELE
		pay.setTenderType("K");
		
		//ASIGNO LA FECHA DE PAGO
		pay.setXX_DateFinalPay(sqlTimestamp);
		
		//ASIGNO EL NUMERO DEL CHEQUE
		pay.setCheckNo(Integer.toString(CheckNo));
		
		//ASIGNO LA CUENTA BANCARIA A LA CUAL HICE LA TRANSFERENCIA
		pay.setAccountNo(cuentaSocioDeNegocios(pay.getC_BPartner_ID()));
		
		//ASIGNO EL POSTED
		pay.setPosted(true);
		
		//CUANDO EL PROCESSING SEA TRUE EL NO VUELVE A BUSCAR ESTE ANTICIPO
		pay.setProcessing(true);
		
		//GUARDO LOS CAMBIOS
		pay.save();

		//APLICO LA CONTABILIDAD
			//CREO LA CABECERA
			X_XX_VCN_AccoutingEntry cabecera = new X_XX_VCN_AccoutingEntry(Env.getCtx(), 0, trans);
			cabecera.setDateFrom(sqlTimestamp);
			cabecera.setDateTo(sqlTimestamp);
			cabecera.setDateTrx(sqlTimestamp);
			cabecera.setDescription("COMPROBANTE DIARIO");
			cabecera.setXX_TotalHave(pay.getPayAmt());
			cabecera.setXX_TotalShould(pay.getPayAmt());
			cabecera.setXX_ListCX017("BA");
//			cabecera.setXX_ProcessType("");
//			cabecera.setXX_TypeTransferSP("");
			cabecera.setIsTransferred(false);
			String numberControl = String.valueOf(5) + "0" + 0 + String.valueOf(numberControl());
			cabecera.setXX_ControlNumber(numberControl);

			cabecera.save();

				//CREO EL DETALLE DE LA CUENTA CONTABLE *DEBE*
				buscarCuentaAnticipo(pay,cabecera,trans);
				// CREO EL HABER
				MFactAcct haber = new MFactAcct(Env.getCtx(), 0, trans);
				haber.setAD_Table_ID(318);
				haber.setAccount_ID(buscarElementoContableBanco(pay.getC_BankAccount_ID()));
				haber.setAmtAcctCr(pay.getPayAmt());
				java.math.BigDecimal cero = new java.math.BigDecimal("0");
				haber.setAmtAcctDr(cero);
				haber.setAmtSourceCr(pay.getPayAmt());
				haber.setAmtSourceDr(cero);
				haber.setC_AcctSchema_ID(1000009);
				haber.setC_Currency_ID(205);
				haber.setC_BPartner_ID(pay.getC_BPartner_ID());
				haber.set_ValueNoCheck("XX_VCN_Line", buscarCuantasLineas(pay.getC_Payment_ID(), cabecera) + 1);
				haber.set_ValueNoCheck("XX_VCN_AccoutingEntry_ID", cabecera.getXX_VCN_AccoutingEntry_ID());
				haber.setC_Period_ID(buscarPeriodoContable());
				haber.setDateAcct(sqlTimestamp);
				haber.setDateTrx(sqlTimestamp);
				haber.setPostingType("A");
				haber.setRecord_ID(pay.getC_Payment_ID());
				haber.setDescription(pay.getA_Name() + " " + buscarNombreElementoContableBanco(pay.getC_BankAccount_ID()));
				
				// CREO EL DETALLE DEL HABER
				XX_VCN_DetalleContable contable = new XX_VCN_DetalleContable();
				ArrayList<String> detalle = new ArrayList<String>();
				detalle = contable.DetalleContabilidad(pay,
						buscarElementoContableBanco(pay.getC_BankAccount_ID()));

				if(detalle.get(0) != null)
					haber.set_Value("XX_Aux", detalle.get(0));
				if(detalle.get(1) != null)
					haber.set_Value("XX_Departament", detalle.get(1));
				if(detalle.get(2) != null)
					haber.set_Value("XX_Division", detalle.get(2));
				haber.set_Value("XX_DocumentDate", pay.getXX_DateFinalPay());
				if(detalle.get(4) != null)
					haber.set_Value("XX_DocumentType", detalle.get(4));
				haber.set_Value("XX_DueDate", pay.getXX_DateFinalPay());
				haber.set_Value("DocumentNo", Integer.toString(haber.getFact_Acct_ID()));
				if(detalle.get(7) != null)
				haber.set_Value("XX_Office", detalle.get(7));
				if(detalle.get(8) != null)
					haber.set_Value("XX_SectionCode", detalle.get(8));

				haber.save();
				//Actualizo el Document No en el debe
				haber.set_Value("DocumentNo", Integer.toString(haber.getFact_Acct_ID()));
				haber.save();

		//PONER EL CHEQUE ES STADO EN USO
				//CREO EL OBJETO
				MVCNCheck aux = new MVCNCheck(Env.getCtx(),checkId,trans);
				//ASIGNO EL PAGO AL CHEQUE
				aux.setC_Payment_ID(pay.getC_Payment_ID());
				//ASIGNO ES STATUS ELABORADO
				aux.setXX_VCN_LocationofChecks("5");
				
				aux.save();

	}


/*
*METODO QUE PAGA LAS SELECCIONES DE PAGO 
*/
	private void paySeleccion(int Id, Trx trans, int checkId, int CheckNo) {
		
				//CREO EL OBJETO C_PAYSELECTION
				MPaySelection psel = new MPaySelection (Env.getCtx(), Id, trans);
				
				//GUARDO EL VALOR DEL MONTO DE LA SELECCION DE PAGO
				montoTotal= psel.getTotalAmt();
				
				//LLAMO AL METODO QUE SE VA A ENCARGAR DE BUSCAR EL PAGO QUE NO HA SIDO PROCESADO DE LAS SELECCIONES DE PAGO
				//BUSCO SI TIENE PAGOS RELACIONADOS 
						int pagoAsociado=0;
				
						String sql= 
						"SELECT PSC.C_PAYSELECTIONCHECK_ID, PS.C_PAYSELECTION_ID"
						+" FROM C_PAYSELECTIONLINE PSL, C_PAYSELECTION PS, C_PAYSELECTIONCHECK PSC"
						+" WHERE PS.C_PAYSELECTION_ID = "+Id
						+" AND PS.C_PAYSELECTION_ID = PSL.C_PAYSELECTION_ID"
						+" AND PSL.C_PAYSELECTIONCHECK_ID = PSC.C_PAYSELECTIONCHECK_ID" 
						+" GROUP BY PSC.C_PAYSELECTIONCHECK_ID, PS.C_PAYSELECTION_ID";
												
										PreparedStatement prst = DB.prepareStatement(sql,null);
									   	ResultSet rs = null;
									   	//EJECUTO LA SENTENCIA SQL
										   	try {
										   		rs = prst.executeQuery();
										   		if(rs.next()){
										   			//OBTENGO EL ID DEL PAGO QUE ES EL DE LA TABLA-> C_PAYSELECTIONCHECK
										   			pagoAsociado= rs.getInt("C_PAYSELECTIONCHECK_ID");
										   			//METODO QUE CREO EL PAGO DE ESA SELECCION DE PAGO
										   			PagarLineasCheque(pagoAsociado,checkId,CheckNo, trans,Id);
										   		}// Fin if
										   	} 
										   	catch (Exception e){
												System.out.println(e);
											}
										   	finally {
										   	//CERRAR CONEXION
												DB.closeResultSet(rs);
												DB.closeStatement(prst);
											}
				
				//BUSCO Y CANCELO LAS LINEAS DE PAGO DE ESA SELECCION DE PAGO
				String sql1= "SELECT C_PAYSELECTIONLINE_ID"
				+" FROM C_PAYSELECTIONLINE"
				+" WHERE ISACTIVE = 'Y' AND C_PAYSELECTION_ID= "+ psel.getC_PaySelection_ID();
						
						PreparedStatement prst1 = DB.prepareStatement(sql1,null);
					   	ResultSet rs1 = null;
					   	//EJECUTO LA SENTENCIA SQL
						   	try {
						   		rs1 = prst1.executeQuery();
						   		while (rs1.next()){

						   			//OBTENGO EL ID DE LAS SELECCIONES DE PAGO
						   			int seleccion= rs1.getInt("C_PAYSELECTIONLINE_ID");
						   			//PAGO LAS LINEAS DE PAGO DE ESA SELECCION
						   			PagarLineasSeleccion(seleccion,trans);
						   		}// Fin WHILE
						   	} 
						   	catch (Exception e){
								System.out.println(e);
							}
						   	finally {
						   	//CERRAR CONEXION
								DB.closeResultSet(rs1);
								DB.closeStatement(prst1);
							}
				
						   	//ASIGNO LA CUENTA BANCARIA DE LA CUAL ESTOY PAGANDO
						   	psel.setC_BankAccount_ID(Integer.parseInt(bankAccount_ID));
						   	//LA COLOCO COMO YA PROCESADA, CUANDO ESTE VALOR SEA TRUE EL NO LA VUELVE A BUSCAR
						   	psel.setProcessing(true);

						   	psel.save();
	}
		

/*
* METODO QUE SE ENCARGA DE REALIZAR EL PAGO DE LA SELECCION DE PAGO	
*/
public void PagarLineasCheque(int Id, int checkId, int checkNo, Trx trans, int idSeleccion) {
		
		//CREO EL OBJETO QUE HACE REFERENCIA AL PAGO DE LA SELECCION DE PAGO
		MPaySelectionCheck cheque = new MPaySelectionCheck (Env.getCtx(), Id, trans);
		//SETEO SUS VALORES
			cheque.setPaymentRule("S");
			cheque.setCheckNo(Integer.toString(checkNo));
			cheque.setProcessed(true);
			
			
			java.util.Date utilDate = new java.util.Date(); //fecha actual
			long lnMilisegundos = utilDate.getTime();
			java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);

		//CREO EL PAGO PARA ASIGNARSELO AL PAYSELECTIONCHECK
				MPayment pay = new MPayment (Env.getCtx(), 0, trans);
				
				//ASINGO LA ORGANIZACION
				pay.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
				
				//ASIGNO EL TIPO DE DOCUMENTO
				pay.setC_DocType_ID(buscarTipoDocumento());
				
				//ASIGNO LA CUENTA BANCARIA DE LA CUAL HICE EL PAGO
				pay.setC_BankAccount_ID(Integer.parseInt(bankAccount_ID));
				
				//ASIGNO LA MONEDA
				pay.setC_Currency_ID(buscarTipoMoneda());
				
				//ASIGNO LA FECHA DE LA TRANSACCION
				pay.setDateTrx(sqlTimestamp);
				
				//ASIGNO LA FECHA CONTABLE
				pay.setDateAcct(sqlTimestamp);
				
				//ASIGNO PROCESSED
				pay.setProcessed(true);
				
				//DESTILDO LOS CAMPOS DE ANTICIPO Y COBRO
				pay.setXX_IsAdvance(false);
				pay.setIsReceipt(false);
				
				//ASIGNO EL SOCIO DE NEGOCIO
				pay.setC_BPartner_ID(cheque.getC_BPartner_ID());
				
				//ASIGNO EL NOMBRE DEL SOCIO DE NEGOCIO
					//OBTENGO EL NOMBRE DEL SOCIO DE NEGOCIO
					String nombresocio=buscarSocioDeNegocios(cheque.getC_BPartner_ID());
					//OBTENGO LA RELACION
					String relacion= buscarRelacion(nombresocio);
					
					if(relacion!= null) nombresocio=relacion;
					pay.setA_Name(nombresocio);
				
				//ASIGNO EL TOTAL DEL PAGO
				pay.setPayAmt(montoTotal);

				//ASIGNO LA FORMA EN QUE LA CANCELE (TRANSFERENCIA)
				pay.setTenderType("K");
				
				//TOTAL DE DESCUENTO
				java.math.BigDecimal cero = new java.math.BigDecimal("0.00");
				pay.setDiscountAmt(cero);

				//ASIGNO LA FECHA DE PAGO
				pay.setXX_DateFinalPay(sqlTimestamp);
				
				//ASIGNO EL NUMERO DEL CHEQUE
				pay.setCheckNo(Integer.toString(checkNo));
				
				//ASIGNO LA CUENTA BANCARIA A LA CUAL HICE LA TRANSFERENCIA
				pay.setAccountNo(cuentaSocioDeNegocios(cheque.getC_BPartner_ID()));
				
				//ASIGNO EL PROCESSING
				pay.setProcessing(true);
				
				//GUARDO LOS CAMBIOS
				pay.save();
				
				//APLICO LA CONTABILIDAD
					//CREO LA CABECERA
					X_XX_VCN_AccoutingEntry cabecera = new X_XX_VCN_AccoutingEntry(Env.getCtx(), 0, trans);
					cabecera.setDateFrom(sqlTimestamp);
					cabecera.setDateTo(sqlTimestamp);
					cabecera.setDateTrx(sqlTimestamp);
					cabecera.setDescription("COMPROBANTE DIARIO");
					cabecera.setXX_TotalHave(pay.getPayAmt());
					cabecera.setXX_TotalShould(pay.getPayAmt());
					cabecera.setXX_ListCX017("BA");
	//				cabecera.setXX_ProcessType("");
	//				cabecera.setXX_TypeTransferSP("");
					cabecera.setIsTransferred(false);
					String numberControl = String.valueOf(5) + "0" + 0
							+ String.valueOf(numberControl());
					cabecera.setXX_ControlNumber(numberControl);
					cabecera.save();
					
					//CREO EL DETALLE DE LA CUENTA CONTABLE *DEBE*
					buscarCuentaSeleccion(pay,cabecera,trans, idSeleccion);
					// CREO EL HABER
					MFactAcct haber = new MFactAcct(Env.getCtx(), 0, trans);
					haber.setAD_Table_ID(318);
					haber.setAccount_ID(buscarElementoContableBanco(pay.getC_BankAccount_ID()));
					haber.setAmtAcctCr(pay.getPayAmt());
					java.math.BigDecimal cero1 = new java.math.BigDecimal("0");
					haber.setAmtAcctDr(cero1);
					haber.setAmtSourceCr(pay.getPayAmt());
					haber.setAmtSourceDr(cero1);
					haber.setC_AcctSchema_ID(1000009);
					haber.setC_Currency_ID(205);
					haber.setC_BPartner_ID(pay.getC_BPartner_ID());
					haber.set_ValueNoCheck("XX_VCN_Line", buscarCuantasLineas(pay.getC_Payment_ID(), cabecera) + 1);
					haber.set_ValueNoCheck("XX_VCN_AccoutingEntry_ID", cabecera.getXX_VCN_AccoutingEntry_ID());
					haber.setC_Period_ID(buscarPeriodoContable());
					haber.setDateAcct(sqlTimestamp);
					haber.setDateTrx(sqlTimestamp);
					haber.setPostingType("A");
					haber.setRecord_ID(pay.getC_Payment_ID());
					haber.setDescription(pay.getA_Name() + " " + buscarNombreElementoContableBanco(pay.getC_BankAccount_ID()));
					
					// CREO EL DETALLE DEL HABER
					XX_VCN_DetalleContable contable = new XX_VCN_DetalleContable();
					ArrayList<String> detalle = new ArrayList<String>();
					detalle = contable.DetalleContabilidad(pay,
							buscarElementoContableBanco(pay.getC_BankAccount_ID()));

					if(detalle.get(0) != null)
						haber.set_Value("XX_Aux", detalle.get(0));
					if(detalle.get(1) != null)
						haber.set_Value("XX_Departament", detalle.get(1));
					if(detalle.get(2) != null)
						haber.set_Value("XX_Division", detalle.get(2));
					haber.set_Value("XX_DocumentDate", pay.getXX_DateFinalPay());
					if(detalle.get(4) != null)
						haber.set_Value("XX_DocumentType", detalle.get(4));
					haber.set_Value("XX_DueDate", pay.getXX_DateFinalPay());
					haber.set_Value("DocumentNo", Integer.toString(haber.getFact_Acct_ID()));
					if(detalle.get(7) != null)
					haber.set_Value("XX_Office", detalle.get(7));
					if(detalle.get(8) != null)
						haber.set_Value("XX_SectionCode", detalle.get(8));

					haber.save();
					//Actualizo el Document No en el debe
					haber.set_Value("DocumentNo", Integer.toString(haber.getFact_Acct_ID()));
					haber.save();
			
		//ASIGNO EL ID DEL PAGO QUE ACABO DE CREAR AL C_PAYSELECTIONCHECK
		cheque.setC_Payment_ID(pay.getC_Payment_ID());
		//GUARDO EL CHEQUE
		cheque.save();
		
		//COMPLETO EL PAGO QUE ACABO DE CREAR
		pay.setDocAction(X_C_Payment.DOCACTION_Complete);
		DocumentEngine.processIt(pay, X_C_Payment.DOCACTION_Complete);
		pay.save();
		
		//PONER EL CHEQUE ES STADO EN USO
		//CREO EL OBJETO
		MVCNCheck aux = new MVCNCheck(Env.getCtx(),checkId,trans);
		//ASIGNO EL PAGO AL CHEQUE
		aux.setC_Payment_ID(pay.getC_Payment_ID());
		//ASIGNO ES STATUS ELABORADO
		aux.setXX_VCN_LocationofChecks("5");
		
		aux.save();
		
			//LLAMO AL REPORTE DEL VOUCHER
			//LOAD C_PAYMENT
			int org_ID= pay.getAD_Client_ID();
			String empresa= empresaEmisora(org_ID);
			reporte1(pay.getC_Payment_ID(),empresa);

		
	}


/*
 * METODO QUE SE ENCARGA DE HACER LOS DEPOSITOS A LAS SELECCION DE PAGO	
 */
	public void PagarLineasSeleccion(int Id,Trx trans){

		//CREO UN OBJETO C_PAYSELECTIONLINE Y ASIGNO QUE SU FORMA DE PAGO ES TRANSFERENCIA
		MPaySelectionLine line = new MPaySelectionLine (Env.getCtx(), Id, trans);
		line.setPaymentRule("S");
		line.setProcessed(true);
		PagarFactura(line.getC_Invoice_ID(),trans);
		line.save();
	}

	/*
	 * METODO QUE SE ENCARGA DE PAGAR LAS FACTURAS	
	 */
		public void PagarFactura(int Id,Trx trans){

			//CREO UN OBJETO C_PAYSELECTIONLINE Y ASIGNO QUE SU FORMA DE PAGO ES TRANSFERENCIA
			MInvoice factura = new MInvoice (Env.getCtx(), Id, trans);
			factura.set_Value ("XX_AccountPayableStatus", "P");
			factura.save();
		}

	/*
	 * METODO QUE EL ID DEL TIPO DE DOCUMENTO APP PAYMENT	
	 */
		private Integer buscarTipoDocumento() {

			// TODO Auto-generated method stub
			int documento=0;
			
			String sql=
			"SELECT C_DOCTYPE_ID"
			+" FROM C_DOCTYPE"
			+" WHERE NAME='AP Payment'";

			PreparedStatement prst = DB.prepareStatement(sql,null);
		   	ResultSet rs = null;
		   	//EJECUTO LA SENTENCIA SQL
			   	try {
			   		rs = prst.executeQuery();
			   		if (rs.next()){
			   			documento = rs.getInt("C_DOCTYPE_ID");
			   		}// Fin if
			   	} 
			   	catch (Exception e){
					System.out.println(e);
				}
			   	finally {
			   	//CERRAR CONEXION
					DB.closeResultSet(rs);
					DB.closeStatement(prst);
				}
			
			return documento;
		}

		/*
		 * METODO QUE BUSCA EL ID DE LA MONEDA "BOLIVAR FUERTE"	
		 */
			private Integer buscarTipoMoneda() {

				// TODO Auto-generated method stub
				int moneda=0;
				
				String sql=
				"SELECT C_CURRENCY_ID"
				+" FROM C_CURRENCY"
				+" WHERE ISO_CODE='VEB'";

				PreparedStatement prst = DB.prepareStatement(sql,null);
			   	ResultSet rs = null;
			   	//EJECUTO LA SENTENCIA SQL
				   	try {
				   		rs = prst.executeQuery();
				   		if (rs.next()){
				   			moneda = rs.getInt("C_CURRENCY_ID");
				   		}// Fin if
				   	} 
				   	catch (Exception e){
						System.out.println(e);
					}
				   	finally {
				   	//CERRAR CONEXION
						DB.closeResultSet(rs);
						DB.closeStatement(prst);
					}
				
				return moneda;
			}

			/*
			 * METODO QUE BUSCA EL NOMBRE DE UN SOCIO DE NEGOCIO
			 */
				private String buscarSocioDeNegocios(int id) {

					// TODO Auto-generated method stub
					String Socio="";
					
					String sql=
					"select BP.NAME AS SOCIO"
					+" from C_BPartner BP"
					+" where BP.C_BPartner_ID = " +id;

					PreparedStatement prst = DB.prepareStatement(sql,null);
				   	ResultSet rs = null;
				   	//EJECUTO LA SENTENCIA SQL
					   	try {
					   		rs = prst.executeQuery();
					   		if (rs.next()){
					   			Socio = rs.getString("SOCIO");
					   		}// Fin if
					   	} 
					   	catch (Exception e){
							System.out.println(e);
						}
					   	finally {
					   	//CERRAR CONEXION
							DB.closeResultSet(rs);
							DB.closeStatement(prst);
						}
					
					return Socio;
				}
				
				/*
				 * METODO QUE BUSCA LA CUENTA A DEPOSITAR EL CHEQUE
				 */
					private String cuentaSocioDeNegocios(int id) {

						// TODO Auto-generated method stub
						String cuenta="";
						
						String sql=
								"SELECT CBP.NAME BENEFICIARIO, CBPBA.ACCOUNTNO CUENTA"
								+" FROM C_BPARTNER CBP, C_BP_BANKACCOUNT CBPBA"
								+" WHERE"
								+" CBP.C_BPARTNER_ID=CBPBA.C_BPARTNER_ID"
								+" AND CBPBA.XX_ISPRIMARY='Y'"
								+" AND CBPBA.XX_ISPRIMARY='Y'"
								+" AND CBP.C_BPartner_ID = "+id;

						PreparedStatement prst = DB.prepareStatement(sql,null);
					   	ResultSet rs = null;
					   	//EJECUTO LA SENTENCIA SQL
						   	try {
						   		rs = prst.executeQuery();
						   		if (rs.next()){
						   			cuenta = rs.getString("CUENTA");
						   		}// Fin if
						   	} 
						   	catch (Exception e){
								System.out.println(e);
							}
						   	finally {
						   	//CERRAR CONEXION
								DB.closeResultSet(rs);
								DB.closeStatement(prst);
							}
						
						return cuenta;
					}
					/*
					 * METODO QUE SE ENCARGA DE CREAR EL SQL CON LOS VALORES QUE NECESITA EL PARAMETRO
					 * SELECCION DE PAGO
					 */
					private String parametrosSeleccion(int Id){
						String sql=
								" SELECT PS.C_PAYSELECTION_ID ID, PS.TOTALAMT TOTAL,  CBP.NAME BENEFICIARIO, TO_CHAR(PS.TOTALAMT, '999G999G999G999D99') FORMATO"
								+" FROM C_PAYSELECTION PS, C_PAYSELECTIONLINE PSL, C_PAYSELECTIONCHECK PSC, C_BPARTNER CBP"
								+" WHERE PS.ISACTIVE='Y' AND PS.C_PAYSELECTION_ID = "+Id
								+" AND PS.C_PAYSELECTION_ID=PSL.C_PAYSELECTION_ID AND PSL.C_PAYSELECTIONCHECK_ID = PSC.C_PAYSELECTIONCHECK_ID"
								+" AND CBP.C_BPARTNER_ID = PSC.C_BPARTNER_ID";
						
					return sql;
					}
					/*
					 * METODO QUE SE ENCARGA DE CREAR EL SQL CON LOS VALORES QUE NECESITA EL PARAMETRO
					 * ANTICIPO DE PAGO
					 */
					private String parametrosAnticipo(int Id){
						String sql=
								" SELECT CP.C_PAYMENT_ID ID, CP.PAYAMT TOTAL, CBP.NAME BENEFICIARIO, TO_CHAR(CP.PAYAMT, '999G999G999G999D99') FORMATO"
								+" FROM C_PAYMENT CP, C_BPARTNER CBP"
								+" WHERE CP.ISACTIVE='Y' AND CBP.C_BPARTNER_ID=CP.C_BPARTNER_ID"
								+" AND CP.C_PAYMENT_ID = "+Id;						
					return sql;
					}
					/*
					 * METODO ENCARGADO DE BUSCAR LA RELACION DE UN SOCIO DE NEGOCIO
					 */
					private String buscarRelacion(String name) {

						// TODO Auto-generated method stub
						String Socio=null;
						
						String sql=
						"select R.NAME AS RELATION, BP.NAME AS SOCIO"
						+" from C_BPartner BP, C_BP_RELATION R"
						+" where BP.NAME = '" +name+ "'"
						+" and R.C_BPartner_ID = BP.C_BPartner_ID"
						+" and R.ISACTIVE = 'Y'";

						PreparedStatement prst = DB.prepareStatement(sql,null);
					   	ResultSet rs = null;
					   	//EJECUTO LA SENTENCIA SQL
						   	try {
						   		rs = prst.executeQuery();
						   		if (rs.next()){
						   			Socio = rs.getString("RELATION");
						   		}// Fin if
						   	} 
						   	catch (Exception e){
								System.out.println(e);
							}
						   	finally {
						   	//CERRAR CONEXION
								DB.closeResultSet(rs);
								DB.closeStatement(prst);
							}
						
						return Socio;
					}
					
/*
* METODO QUE LLAMA AL REPORTE
*/
					private void reporte(BigDecimal total, String beneficiario, String formato) {
						// TODO Auto-generated method stub
						
						String designName = "PrintCheck";

						//INSTANCIAR REPORTE
						BIRTReport myReport = new BIRTReport();
						
						//AGREGAR PARAMETRO
						myReport.parameterName.add("total");
						myReport.parameterValue.add(total.toString());
												
						myReport.parameterName.add("beneficiario");
						myReport.parameterValue.add(quitarcaracteresespeciales(beneficiario));
						
						myReport.parameterName.add("formato");
						myReport.parameterValue.add(formatoEspacioReporte(formato));
						
						//CORRER REPORTE
						myReport.runReport(designName,"pdf");
						
					}
					
/*
* METODO QUE SE ENCARGA DE QUITAR CARACTERES ESPECIALES
*/
					private String quitarcaracteresespeciales(String beneficiario) {
						// TODO Auto-generated method stub
						 beneficiario= beneficiario.replace("ñ", "n");
						 beneficiario= beneficiario.replace("Ñ", "N");
						 beneficiario= beneficiario.replace("á", "a");
						 beneficiario= beneficiario.replace("Á", "A");
						 beneficiario= beneficiario.replace("é", "e");
						 beneficiario= beneficiario.replace("É", "E");
						 beneficiario= beneficiario.replace("í", "i");
						 beneficiario= beneficiario.replace("Í", "I");
						 beneficiario= beneficiario.replace("ó", "o");
						 beneficiario= beneficiario.replace("Ó", "O");
						 beneficiario= beneficiario.replace("ú", "u");
						 beneficiario= beneficiario.replace("Ú", "U");
						 beneficiario= beneficiario.replace("-", "%20");
						 beneficiario= beneficiario.replace(".", "%20");
						 beneficiario= beneficiario.replace(",", "%20");
						 beneficiario= beneficiario.replace(";", "%20");
						 beneficiario= beneficiario.replace(" ", "%20");
						 
						return beneficiario;
					}

					/*
					 * METODO QUE SE ENCARGA DE QUITAR CARACTERES ESPECIALES
					 */
					private String formatoEspacioReporte(String beneficiario) {
						// TODO Auto-generated method stub
 
						 beneficiario= beneficiario.replace(" ", "%20");
						 
						return beneficiario;
					}
			
/*
 * METODO QUE SE ENCARGA DE BUSCAR LA CUENTA CONTABLE DE UN ANTICIPO					
 */
private void buscarCuentaAnticipo(MPayment pay, X_XX_VCN_AccoutingEntry cabecera, Trx trans) {
	// TODO Auto-generated method stub
	String sql =
	"select bp.c_bpartner_id as socio, bp.value VALUE, ord.XX_POType, ord.XX_PurchaseType, " +
	"ord.DocumentNo NRODOC, ord.Created FECDOC, " +
	"to_char(sysdate,'dd.mm.yyyy') FECVEN, pay.PayAmt MONASI,to_char(pay.PayAmt, '999G999G999G999D99') FORMATO, to_char('ANT') TIPDOC " +
	"from C_Payment pay " +
	"inner join C_Order ord on (pay.C_Order_ID = ord.C_Order_ID) " +
	"inner join C_bpartner bp on (ord.c_bpartner_id = bp.c_bpartner_id) "+
	"where pay.C_Payment_ID = " + pay.getC_Payment_ID();
	int tipoCategoria = 0;
	int line=1;
	StringBuffer m_where = new StringBuffer();
	PreparedStatement pstmt = null; 
	ResultSet rs = null; 
	try{
		pstmt = DB.prepareStatement(sql, null); 
		rs = pstmt.executeQuery(); 
		while(rs.next()){
			
			String tipo= rs.getString("TIPDOC");
			String nroDoc= rs.getString("NRODOC");
			Timestamp fecha= rs.getTimestamp("FECDOC");
							
			if (rs.getString("XX_POType") == null)
				if ((rs.getString("XX_LEASE")==null) || rs.getString("XX_LEASE").equals("N"))
					//Para Servicios que no sean Arrendadmientos se pasa la categoria de producto Standard (CCAPOTE)
					tipoCategoria = Env.getCtx().getContextAsInt("#M_Product_Category_ID");
				else {
					//Para Servicios de tipo Arrendadmientos se pasa la categoria de producto Servicio con la marca de Arrendamiento (CCAPOTE)
					tipoCategoria = Env.getCtx().getContextAsInt("#XX_L_CATEPRODUCTSERVICES_ID");
					m_where.append(" and XX_Lease = 'Y' ");
				}
			else{
				// Para Mercancia para la Venta
				if (rs.getString("XX_PurchaseType") == null){
					tipoCategoria = Env.getCtx().getContextAsInt("#XX_L_CATEGORYPRODUCTITEM_ID");
					// Para ubicar la cuenta que le corresponde a Mercancia (CCAPOTE)  
					m_where.append(" and XX_ElementType = 'Nacional' " +
								   "and XX_Transitional <> 'Y' ");
				}
				else if ((rs.getString("XX_PurchaseType").equals("SU")) || (rs.getString("XX_PurchaseType").equals("SE")))
					//Se pasa servicios porque es la misma cuenta tanto la servicios como para sumistros y materiales
					tipoCategoria = Env.getCtx().getContextAsInt("#M_Product_Category_ID"); 					
				else if (rs.getString("XX_PurchaseType").equals("FA")){
					//Para Activo Fijo se le pasa categoría del producto “Servicio” y marcada como “Cuentas por Pagar” 
					tipoCategoria = Env.getCtx().getContextAsInt("#XX_L_CATEPRODUCTSERVICES_ID");
					m_where.append(" and XX_AccountPayable = 'Y' ");
				}
			}
			accounts(tipoCategoria, m_where, pay, cabecera,trans, rs.getBigDecimal("MONASI"), rs.getInt("socio"),line, nroDoc, tipo, fecha);
			line=line+1;
		}
		
	}catch(Exception e){
		log.log(Level.SEVERE, sql);	
	}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
	}

}

/**
 * Se encarga de buscar la cuenta contable, a través de los parámetros de entrada
 * @param cabecera 
 * @param pay 
 * @param trans 
 * @param monto 
 * @param socio 
 * @param tipoOC Tipo de la Orden de Compra
 * @param tipoBien Tipo de Bien
 * @return La cuenta contable
 */
public void accounts(int tipoCategoria, StringBuffer m_where, MPayment pay,
		X_XX_VCN_AccoutingEntry cabecera, Trx trans, BigDecimal monto,
		int socio, int line, String nroDoc, String tipo, Timestamp fecha) {
	String sqlAccount = "select cev.c_elementvalue_id "
			+ "from C_ElementValue cev "
			+ "inner join M_Product_Category mpc on (cev.M_Product_Category_ID = mpc.M_Product_Category_ID) "
			+ "where cev.AccountType = 'L' "
			+ "and mpc.M_Product_Category_ID = " + tipoCategoria + m_where;

	int account = 0;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	try {
		pstmt = DB.prepareStatement(sqlAccount, null);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			account = rs.getInt(1);
			MFactAcct debe = new MFactAcct(Env.getCtx(), 0, trans);
			debe.setAD_Table_ID(318);
			debe.setAccount_ID(account);
			java.math.BigDecimal cero = new java.math.BigDecimal("0");

			// VERIFICA QUE EN EL DEBE NO EXISTAN MONTOS NEGATIVOS EN EL
			// DEBE
			if (monto.compareTo(BigDecimal.ZERO) < 0) {
				debe.setAmtAcctCr(monto.abs());
				debe.setAmtAcctDr(cero);
				debe.setAmtSourceCr(monto.abs());
				debe.setAmtSourceDr(cero);
			} else {
				debe.setAmtAcctCr(cero);
				debe.setAmtAcctDr(monto);
				debe.setAmtSourceCr(cero);
				debe.setAmtSourceDr(monto);
			}
			debe.setC_AcctSchema_ID(1000009);
			debe.setC_BPartner_ID(socio);
			debe.setC_Currency_ID(205);
			debe.set_ValueNoCheck("XX_VCN_Line", line);
			debe.set_ValueNoCheck("XX_VCN_AccoutingEntry_ID", cabecera.getXX_VCN_AccoutingEntry_ID());
			debe.setC_Period_ID(buscarPeriodoContable());
			java.util.Date utilDate = new java.util.Date(); // FECHA ACTUAL
			long lnMilisegundos = utilDate.getTime();
			java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);
			debe.setDateAcct(sqlTimestamp);
			debe.setDateTrx(sqlTimestamp);
			debe.setPostingType("A");
			debe.setRecord_ID(pay.getC_Payment_ID());
			debe.setDescription(pay.getA_Name());
			// CREO EL DETALLE DEL DEBE
			XX_VCN_DetalleContable contable = new XX_VCN_DetalleContable();
			ArrayList<String> detalle = new ArrayList<String>();
			detalle = contable.DetalleContabilidad(pay, account);

			if(detalle.get(0) != null)
				debe.set_Value("XX_Aux", detalle.get(0));
			if(detalle.get(1) != null)
				debe.set_Value("XX_Departament", detalle.get(1));
			if(detalle.get(2) != null)
				debe.set_Value("XX_Division", detalle.get(2));
			debe.set_Value("XX_DocumentDate", fecha);
			if(detalle.get(4) != null)
				debe.set_Value("XX_DocumentType", tipo);
			
				debe.set_Value("XX_DueDate", fecha);
				debe.set_Value("DocumentNo", nroDoc);
				
			if(detalle.get(7) != null)
				debe.set_Value("XX_Office", detalle.get(7));
			if(detalle.get(8) != null)
			debe.set_Value("XX_SectionCode", detalle.get(8));

			debe.save();
			//Actualizo el Nro de documento
			debe.set_Value("DocumentNo", nroDoc);
			debe.save();
			trans.commit();

		}
	} catch (Exception e) {
		log.log(Level.SEVERE, sqlAccount);
	} finally {
		DB.closeResultSet(rs);
		DB.closeStatement(pstmt);
	}

}
/*
 * METODO QUE BUSCA CUANTAS LINEAS CONTABLES TIENE UN PAGO 
 */
private Integer buscarCuantasLineas(int c_Payment_ID, X_XX_VCN_AccoutingEntry cabecera) {
// TODO Auto-generated method stub
		int cuenta=0;
		
		String sql=
		"select COUNT(*) as cuenta"
		+" from Fact_acct"
		+" where Record_id = " +c_Payment_ID + " and XX_VCN_ACCOUTINGENTRY_ID = "+cabecera.getXX_VCN_AccoutingEntry_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
	   	ResultSet rs = null;
	   	//EJECUTO LA SENTENCIA SQL
		   	try {
		   		rs = prst.executeQuery();
		   		if (rs.next()){
		   			cuenta = rs.getInt("cuenta");
		   		}// Fin if
		   	} 
		   	catch (Exception e){
				System.out.println(e);
			}
		   	finally {
		   	//CERRAR CONEXION
				DB.closeResultSet(rs);
				DB.closeStatement(prst);
			}
		
		return cuenta;
	}
/*
 * METODO QUE SE ENCARGA DE BUSCAR EL ELEMENTO CONTABLE DE UN BANCO
 */
private int buscarElementoContableBanco(int c_BankAccount_ID) {
	// TODO Auto-generated method stub
	int cuenta=0;
	String sql=
	"select c_elementValue.c_elementValue_id as cuenta,c_elementValue.name"
	+" from C_BankAccount_Acct, c_validCombination, c_elementvalue"
	+" where C_BankAccount_Acct.C_BankAccount_ID="+c_BankAccount_ID
	+" and C_BankAccount_Acct.C_AcctSchema_ID=1000009"
	+" and C_BankAccount_Acct.B_INTRANSIT_ACCT= c_validCombination.c_validCombination_id"
	+" and c_validCombination.ACCOUNT_ID= c_elementvalue.c_elementvalue_id";
	PreparedStatement prst = DB.prepareStatement(sql,null);
   	ResultSet rs = null;
   	//EJECUTO LA SENTENCIA SQL
	   	try {
	   		rs = prst.executeQuery();
	   		if (rs.next()){
	   			cuenta = rs.getInt("cuenta");
	   		}// Fin if
	   	} 
	   	catch (Exception e){
			System.out.println(e);
		}
	   	finally {
	   	//CERRAR CONEXION
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}
	
	return cuenta;
}
/*
 * METODO QUE SE ENCARGA DE BUSCAR NOMBRE DE UN EL ELEMENTO CONTABLE DE UN BANCO
 */
private String buscarNombreElementoContableBanco(int c_BankAccount_ID) {
	// TODO Auto-generated method stub
	String nombre="";
	String sql=
	"select c_elementValue.c_elementValue_id as cuenta,c_elementValue.name as name"
	+" from C_BankAccount_Acct, c_validCombination, c_elementvalue"
	+" where C_BankAccount_Acct.C_BankAccount_ID="+c_BankAccount_ID
	+" and C_BankAccount_Acct.C_AcctSchema_ID=1000009"
	+" and C_BankAccount_Acct.B_INTRANSIT_ACCT= c_validCombination.c_validCombination_id"
	+" and c_validCombination.ACCOUNT_ID= c_elementvalue.c_elementvalue_id";
	PreparedStatement prst = DB.prepareStatement(sql,null);
   	ResultSet rs = null;
   	//EJECUTO LA SENTENCIA SQL
	   	try {
	   		rs = prst.executeQuery();
	   		if (rs.next()){
	   			nombre = rs.getString("name");
	   		}// Fin if
	   	} 
	   	catch (Exception e){
			System.out.println(e);
		}
	   	finally {
	   	//CERRAR CONEXION
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}
	
	return nombre;
}


/*
 * METODO QUE SE ENCARGA DE BUSCAR EL PERIODO ACTUAL
 */
private int buscarPeriodoContable() {
	// TODO Auto-generated method stub
	int id=0;
	Calendar c1 = Calendar.getInstance();
	int mes = c1.get(Calendar.MONTH);
	mes=mes+1;
	String mesletra="";
	int year = c1.get(Calendar.YEAR);
	
	if (mes == 1) mesletra="Enero"; else if (mes == 2) mesletra="Febrero"; else if (mes == 3) mesletra="Marzo";
	else if (mes == 4) mesletra="Abril";else if (mes == 5) mesletra="Mayo";else if (mes == 6) mesletra="Junio";
	else if (mes == 7) mesletra="Julio";else if (mes == 8) mesletra="Agosto";else if (mes == 9) mesletra="Septiembre";
	else if (mes == 10) mesletra="Octubre";else if (mes == 11) mesletra="Noviembre";else if (mes == 12) mesletra="Diciembre";
	
	mesletra=mesletra+" "+year;
	String mesletraMayuscula= mesletra.toUpperCase();
	
	String sql=
	"select c_period_id"
	+" from c_period"
	+" where name = '"+mesletra+"' OR name= '"+mesletraMayuscula+"'";
	PreparedStatement prst = DB.prepareStatement(sql,null);
   	ResultSet rs = null;
   	//EJECUTO LA SENTENCIA SQL
	   	try {
	   		rs = prst.executeQuery();
	   		if (rs.next()){
	   			id = rs.getInt(1);
	   		}// Fin if
	   	} 
	   	catch (Exception e){
			System.out.println(e);
		}
	   	finally {
	   	//CERRAR CONEXION
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}
	
	return id;
}
/*
 * METODO QUE SE ENCARGA DE BUSCAR LA CUENTA CONTABLE DE UNA SELECCION					
 */
private void buscarCuentaSeleccion(MPayment pay,
		X_XX_VCN_AccoutingEntry cabecera, Trx trans, int idSeleccion) {
	// TODO Auto-generated method stub
	// TODO Auto-generated method stub
	String sql =
			"select ord.XX_POType, ord.XX_PurchaseType, con.XX_Contract_ID, doc.C_DocType_ID TIPDOC, " +
					"inv.DocumentNo NRODOC, inv.DateInvoiced FECDOC, " +
					"(case when inv.XX_DueDate > inv.DateInvoiced then " +
					"to_char(inv.XX_DueDate,'dd.mm.yyyy') else to_char(inv.DateInvoiced,'dd.mm.yyyy') " +
					"end) FECVEN,  "+
					"ord.DocumentNo, psl.PayAmt MONASI, to_char(psl.PayAmt, '999G999G999G999D99') FORMATO, con.XX_Lease, par.value, par.C_BPartner_ID as socio  " +
					"from C_PaySelection pay  " +
					"inner join C_PaySelectionLine psl on (pay.C_PaySelection_ID = psl.C_PaySelection_ID ) " +
					"inner join C_Invoice inv on (psl.C_Invoice_ID = inv.C_Invoice_ID) " +
					"inner join C_BPartner par on (inv.C_BPartner_ID = par.C_BPartner_ID) " +
					"inner join C_DocType doc on (inv.C_DocTypeTarget_ID = doc.C_DocType_ID) " +
					"left outer join C_Order ord on (inv.C_Order_ID = ord.C_Order_ID) " +
					"left outer join XX_Contract con on (inv.XX_Contract_ID = con.XX_Contract_ID) " +
					"where pay.C_PaySelection_ID = " + idSeleccion;
	int tipoCategoria = 0;
	int line=1;
	StringBuffer m_where = new StringBuffer();
	PreparedStatement pstmt = null; 
	ResultSet rs = null; 
	try{
		pstmt = DB.prepareStatement(sql, null); 
		rs = pstmt.executeQuery(); 
		while(rs.next()){
			String nroDoc=rs.getString("NRODOC");
			Timestamp fecha= rs.getTimestamp("FECDOC");
			String tipo=buscarTipo(rs.getString("TIPDOC"));
							
			if (rs.getString("XX_POType") == null)
				if ((rs.getString("XX_LEASE")==null) || rs.getString("XX_LEASE").equals("N"))
					//Para Servicios que no sean Arrendadmientos se pasa la categoria de producto Standard (CCAPOTE)
					tipoCategoria = Env.getCtx().getContextAsInt("#M_Product_Category_ID");
				else {
					//Para Servicios de tipo Arrendadmientos se pasa la categoria de producto Servicio con la marca de Arrendamiento (CCAPOTE)
					tipoCategoria = Env.getCtx().getContextAsInt("#XX_L_CATEPRODUCTSERVICES_ID");
					m_where.append(" and XX_Lease = 'Y' ");
				}
			else{
				// Para Mercancia para la Venta
				if (rs.getString("XX_PurchaseType") == null){
					tipoCategoria = Env.getCtx().getContextAsInt("#XX_L_CATEGORYPRODUCTITEM_ID");
					// Para ubicar la cuenta que le corresponde a Mercancia (CCAPOTE)  
					m_where.append(" and XX_ElementType = 'Nacional' " +
								   "and XX_Transitional <> 'Y' ");
				}
				else if ((rs.getString("XX_PurchaseType").equals("SU")) || (rs.getString("XX_PurchaseType").equals("SE")))
					//Se pasa servicios porque es la misma cuenta tanto la servicios como para sumistros y materiales
					tipoCategoria = Env.getCtx().getContextAsInt("#M_Product_Category_ID"); 					
				else if (rs.getString("XX_PurchaseType").equals("FA")){
					//Para Activo Fijo se le pasa categoría del producto “Servicio” y marcada como “Cuentas por Pagar” 
					tipoCategoria = Env.getCtx().getContextAsInt("#XX_L_CATEPRODUCTSERVICES_ID");
					m_where.append(" and XX_AccountPayable = 'Y' ");
				}
			}
			accounts(tipoCategoria, m_where, pay, cabecera,trans, rs.getBigDecimal("MONASI"), rs.getInt("socio"),line, nroDoc, tipo, fecha);
			line=line+1;
		}
		
	}catch(Exception e){
		log.log(Level.SEVERE, sql);	
	}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
	}
	
}

/*
 * METODO QUE PAGA LOS CHEQUES MANUALES
 */
	private void payManualCheck(int Id, Trx trans, int checkId, int CheckNo) {
		// TODO Auto-generated method stub
		
		java.util.Date utilDate = new java.util.Date(); //FECHA ACTUAL
		long lnMilisegundos = utilDate.getTime();
		java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);

		//CREO EL PAGO C_PAYMENT
		MPayment pay = new MPayment (Env.getCtx(), Id, trans);

		//ASIGNO EL TIPO DE DOCUMENTO
		pay.setC_DocType_ID(buscarTipoDocumento());
		
		//ASIGNO LA CUENTA BANCARIA DE LA CUAL HICE EL PAGO
		pay.setC_BankAccount_ID(Integer.parseInt(bankAccount_ID));
		
		//ASIGNO LA FECHA DE LA TRANSACCION
		pay.setDateTrx(sqlTimestamp);
		
		//ASIGNO LA FECHA CONTABLE
		pay.setDateAcct(sqlTimestamp);
		
		//ASIGNO PROCESSED
		pay.setProcessed(true);
		
		//ASIGNO EL NOMBRE DEL SOCIO DE NEGOCIO
			//OBTENGO EL NOMBRE DEL SOCIO DE NEGOCIO
			String nombresocio=buscarSocioDeNegocios(pay.getC_BPartner_ID());
			//OBTENGO LA RELACION
			String relacion= buscarRelacion(nombresocio);
			
			if(relacion!= null) nombresocio=relacion;
			pay.setA_Name(nombresocio);
		
		//ASIGNO LA FORMA EN QUE LA CANCELE
		pay.setTenderType("K");
		
		//ASIGNO LA FECHA DE PAGO
		pay.setXX_DateFinalPay(sqlTimestamp);
		
		//ASIGNO EL NUMERO DEL CHEQUE
		pay.setCheckNo(Integer.toString(CheckNo));
		
		//ASIGNO LA CUENTA BANCARIA A LA CUAL HICE LA TRANSFERENCIA
		pay.setAccountNo(cuentaSocioDeNegocios(pay.getC_BPartner_ID()));
		
		//ASIGNO EL POSTED
		pay.setPosted(true);
		
		//CUANDO EL PROCESSING SEA TRUE EL NO VUELVE A BUSCAR ESTE ANTICIPO
		pay.setProcessing(true);
		
		//GUARDO LOS CAMBIOS
		pay.save();

		//APLICO LA CONTABILIDAD
			//CREO LA CABECERA
			X_XX_VCN_AccoutingEntry cabecera = new X_XX_VCN_AccoutingEntry(Env.getCtx(), 0, trans);
			cabecera.setDateFrom(sqlTimestamp);
			cabecera.setDateTo(sqlTimestamp);
			cabecera.setDateTrx(sqlTimestamp);
			cabecera.setDescription("COMPROBANTE DIARIO");
			cabecera.setXX_TotalHave(pay.getPayAmt());
			cabecera.setXX_TotalShould(pay.getPayAmt());
			cabecera.setXX_ListCX017("BA");
//			cabecera.setXX_ProcessType("");
//			cabecera.setXX_TypeTransferSP("");
			cabecera.setIsTransferred(false);
			String numberControl = String.valueOf(5) + "0" + 0
					+ String.valueOf(numberControl());
			cabecera.setXX_ControlNumber(numberControl);
			cabecera.save();
				//CREO EL DETALLE DE LA CUENTA CONTABLE *DEBE*
				MFactAcct debe = new MFactAcct(Env.getCtx(), 0, trans);
				debe.setAD_Table_ID(318);
				debe.setAccount_ID(pay.getXX_VCN_ElementValue());
				java.math.BigDecimal cero = new java.math.BigDecimal("0");
				debe.setAmtAcctCr(cero);
				debe.setAmtAcctDr(pay.getPayAmt());
				debe.setAmtSourceCr(cero);
				debe.setAmtSourceDr(pay.getPayAmt());
				debe.setC_AcctSchema_ID(1000009);
				debe.setC_BPartner_ID(pay.getC_BPartner_ID());
				debe.setC_Currency_ID(205);
				debe.set_ValueNoCheck ("XX_VCN_Line", 1);
				debe.set_ValueNoCheck ("XX_VCN_AccoutingEntry_ID", cabecera.getXX_VCN_AccoutingEntry_ID());
				debe.setC_Period_ID(buscarPeriodoContable());
				debe.setDateAcct(sqlTimestamp);
				debe.setDateTrx(sqlTimestamp);
				debe.setPostingType("A");
				debe.setRecord_ID(pay.getC_Payment_ID());
				debe.setDescription(pay.getA_Name());
				
				//CREO EL DETALLE DEL DEBE
				XX_VCN_DetalleContable contable = new XX_VCN_DetalleContable();
				ArrayList<String> detalle = new ArrayList<String>();
				detalle= contable.DetalleContabilidad(pay,pay.getXX_VCN_ElementValue());
			
				if(detalle.get(0) != null)
					debe.set_Value("XX_Aux", detalle.get(0));
				if(detalle.get(1) != null)
					debe.set_Value("XX_Departament", detalle.get(1));
				if(detalle.get(2) != null)
					debe.set_Value("XX_Division", detalle.get(2));
				debe.set_Value("XX_DocumentDate", pay.getXX_DateFinalPay());
				if(detalle.get(4) != null)
					debe.set_Value("XX_DocumentType", detalle.get(4));
				
					debe.set_Value("XX_DueDate", pay.getXX_DateFinalPay());
					debe.set_Value("DocumentNo", Integer.toString(debe.getFact_Acct_ID()));
					
				if(detalle.get(7) != null)
					debe.set_Value("XX_Office", detalle.get(7));
				if(detalle.get(8) != null)
				debe.set_Value("XX_SectionCode", detalle.get(8));

				debe.save();
				//Actualizo el Nro de documento
				debe.set_Value("DocumentNo", Integer.toString(debe.getFact_Acct_ID()));
				debe.save();
				trans.commit();

				//CREO EL HABER
				MFactAcct haber = new MFactAcct(Env.getCtx(), 0, trans);
				haber.setAD_Table_ID(318);
				haber.setAccount_ID(buscarElementoContableBanco(pay.getC_BankAccount_ID()));
				haber.setAmtAcctCr(pay.getPayAmt());
				haber.setAmtAcctDr(cero);
				haber.setAmtSourceCr(pay.getPayAmt());
				haber.setAmtSourceDr(cero);
				haber.setC_AcctSchema_ID(1000009);
				haber.setC_Currency_ID(205);
				haber.setC_BPartner_ID(pay.getC_BPartner_ID());
				haber.set_ValueNoCheck ("XX_VCN_Line", 2);
				haber.set_ValueNoCheck ("XX_VCN_AccoutingEntry_ID", cabecera.getXX_VCN_AccoutingEntry_ID());
				haber.setC_Period_ID(buscarPeriodoContable());
				haber.setDateAcct(sqlTimestamp);
				haber.setDateTrx(sqlTimestamp);
				haber.setPostingType("A");
				haber.setRecord_ID(pay.getC_Payment_ID());
				haber.setDescription(pay.getA_Name()+" "+buscarNombreElementoContableBanco(pay.getC_BankAccount_ID()));
				
				// CREO EL DETALLE DEL HABER
				detalle= detalle = contable.DetalleContabilidad(pay,buscarElementoContableBanco(pay.getC_BankAccount_ID()));
				if(detalle.get(0) != null)
					haber.set_Value("XX_Aux", detalle.get(0));
				if(detalle.get(1) != null)
				haber.set_Value("XX_Departament", detalle.get(1));
				if(detalle.get(2) != null)
					haber.set_Value("XX_Division", detalle.get(2));
				haber.set_Value("XX_DocumentDate", pay.getXX_DateFinalPay());
				if(detalle.get(4) != null)
					haber.set_Value("XX_DocumentType", detalle.get(4));
				haber.set_Value("XX_DueDate", pay.getXX_DateFinalPay());
				haber.set_Value("DocumentNo", Integer.toString(haber.getFact_Acct_ID()));
				if(detalle.get(7) != null)
				haber.set_Value("XX_Office", detalle.get(7));
				if(detalle.get(8) != null)
				haber.set_Value("XX_SectionCode", detalle.get(8));

				haber.save();
				//Actualizo el Nro de documento
				haber.set_Value("DocumentNo", Integer.toString(haber.getFact_Acct_ID()));
				haber.save();

		//PONER EL CHEQUE ES STADO EN USO
				//CREO EL OBJETO
				MVCNCheck aux = new MVCNCheck(Env.getCtx(),checkId,trans);
				//ASIGNO EL PAGO AL CHEQUE
				aux.setC_Payment_ID(pay.getC_Payment_ID());
				//ASIGNO ES STATUS ELABORADO
				aux.setXX_VCN_LocationofChecks("5");
				aux.save();

	}

	/*
	 * METODO QUE LLAMA AL REPORTE DEL VOUCHER
	 */
	private void reporte1(int pago, String empresa) {
		// TODO Auto-generated method stub
		
		String designName = "PrintVoucher";

		//INSTANCIAR REPORTE
		BIRTReport myReport = new BIRTReport();
		
		//AGREGAR PARAMETRO
		myReport.parameterName.add("pago");
		myReport.parameterValue.add(pago);
								
		myReport.parameterName.add("empresa");
		myReport.parameterValue.add(formatoEspacioReporte(empresa));
		
		//CORRER REPORTE
		myReport.runReport(designName,"pdf");
		
	}
	
	/*
	 * METODO ENCARGADO DE BUSCAR SI EL PAGO PERTENECE A UNA SELECION DE PAGO
	 */
	private String empresaEmisora(Integer Id) {

		// TODO Auto-generated method stub
		String empresa=null;
		
		String sql=
		"select NAME"
		+" from AD_CLIENT"
		+" where AD_CLIENT_ID = "+Id;


		PreparedStatement prst = DB.prepareStatement(sql,null);
	   	ResultSet rs = null;
	   	//EJECUTO LA SENTENCIA SQL
		   	try {
		   		rs = prst.executeQuery();
		   		if (rs.next()){
		   			empresa = rs.getString(1);
		   		}// Fin if
		   	} 
		   	catch (Exception e){
				System.out.println(e);
			}
		   	finally {
		   	//CERRAR CONEXION
				DB.closeResultSet(rs);
				DB.closeStatement(prst);
			}
		
		return empresa;
	}

	/*
	 * GENERA EL NUMERO DE CONTROL DE LA CABECERA
	 */
	public int numberControl() {
		int number = 0;
		String sql_comprobante = "select XX_NUMCOMPROBANTECX017.NEXTVAL from dual";
		PreparedStatement ps_comprobante = null;
		ResultSet rs_comprobante = null;
		try {
			ps_comprobante = DB.prepareStatement(sql_comprobante, null);
			rs_comprobante = ps_comprobante.executeQuery();
			rs_comprobante.next();
			number = rs_comprobante.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs_comprobante.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				ps_comprobante.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return number;
	}
	
	/*
	 * METODO QUE BUSCA EN EL C_DOCTYPE EL TIPO DE DOCUMENTO
	 */
	private String buscarTipo(String id) {
		// TODO Auto-generated method stub
		String tipo="";
		String sql = "select XX_CODDOC from C_DOCTYPE where C_DOCTYPE_ID = "+id;
		PreparedStatement prst = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		// EJECUTO LA SENTENCIA SQL
		try {
			rs = prst.executeQuery();
			if (rs.next()) {
				tipo = rs.getString("XX_CODDOC");
			}// Fin if
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			// CERRAR CONEXION
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}

		return tipo;
	}	
	
}   //  PayPrint