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
package org.compiere.grid;

import java.beans.*;
import java.math.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.table.*;

import org.compiere.apps.*;
import org.compiere.common.constants.*;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.vos.*;

/**
 *  Create Transactions for Bank Statements
 *
 *  @author Jorg Janke
 *  @version  $Id: VCreateFromStatement.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class VCreateFromStatement extends VCreateFrom implements VetoableChangeListener
{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static CLogger 	s_log = CLogger.getCLogger (VCreateFromStatement.class);
	
	/**
	 *  Protected Constructor
	 *  @param mTab MTab
	 */
	VCreateFromStatement(GridTab mTab)
	{
		super (mTab);
		log.info("");
	}   //  VCreateFromStatement

	/**
	 *  Dynamic Init
	 *  @throws Exception if Lookups cannot be initialized
	 *  @return true if initialized
	 */
	@Override
	protected boolean dynInit() throws Exception
	{
		if (p_mTab.getValue("C_BankStatement_ID") == null)
		{
			ADialog.error(0, this, "SaveErrorRowNotFound");
			return false;
		}

		setTitle(Msg.translate(Env.getCtx(), "C_BankStatement_ID") + " .. " + Msg.translate(Env.getCtx(), "CreateFrom"));
		parameterStdPanel.setVisible(false);

		int AD_Column_ID = 4917;        //  C_BankStatement.C_BankAccount_ID
		MLookup lookup = MLookupFactory.get (Env.getCtx(), p_WindowNo, AD_Column_ID, DisplayTypeConstants.TableDir);
		bankAccountField = new VLookup ("C_BankAccount_ID", true, true, true, lookup);
		bankAccountField.addVetoableChangeListener(this);
		//  Set Default
		int C_BankAccount_ID = Env.getCtx().getContextAsInt( p_WindowNo, "C_BankAccount_ID");
		bankAccountField.setValue(Integer.valueOf(C_BankAccount_ID));
		//  initial Loading
		loadBankAccount(C_BankAccount_ID);

		return true;
	}   //  dynInit

	/**
	 *  Init Details (never called)
	 *  @param C_BPartner_ID BPartner
	 */
	@Override
	protected void initBPDetails(int C_BPartner_ID)
	{
	}   //  initDetails

	/**
	 *  Change Listener
	 *  @param e event
	 */
	public void vetoableChange (PropertyChangeEvent e)
	{
		log.config(e.getPropertyName() + "=" + e.getNewValue());

		//  BankAccount
		if (e.getPropertyName() == "C_BankAccount_ID")
		{
			int C_BankAccount_ID = ((Integer)e.getNewValue()).intValue();
			loadBankAccount(C_BankAccount_ID);
		}
		tableChanged(null);
	}   //  vetoableChange

	

	public static ArrayList< FieldVO > getTableFieldVOs( Ctx ctx )
	{
		ArrayList< FieldVO > fieldVOs = new ArrayList< FieldVO >();
		fieldVOs.add( new FieldVO( "Date", Msg.translate( ctx, "Date" ), DisplayTypeConstants.Date ) );
		fieldVOs.add( new FieldVO( "C_Payment_ID", Msg.getElement( ctx, "C_Payment_ID" ), DisplayTypeConstants.ID ) );
		fieldVOs.add( new FieldVO( "DocumentNo", Msg.getElement( ctx, "C_Payment_ID" ), DisplayTypeConstants.String ) );
		fieldVOs.add( new FieldVO( "C_Currency_ID", Msg.translate( ctx, "C_Currency_ID" ), DisplayTypeConstants.ID ) );
		fieldVOs.add( new FieldVO( "Amount", Msg.translate( ctx, "Amount" ), DisplayTypeConstants.Amount ) );
		fieldVOs.add( new FieldVO( "ConvertedAmount", Msg.translate( ctx, "ConvertedAmount" ), DisplayTypeConstants.Amount ) );
		fieldVOs.add( new FieldVO( "C_BPartner_ID", Msg.translate( ctx, "C_BPartner_ID" ), DisplayTypeConstants.ID ) );
		
		for( FieldVO f : fieldVOs )
			f.IsReadOnly = true;
		
		fieldVOs.get( 1 ).IsKey = true;
		fieldVOs.get( 1 ).IsDisplayed = false;
		
		return fieldVOs;
	}

	
	
	public static Vector<Vector< Object >> getBankAccountData( Ctx ctx, int C_BankAccount_ID, Timestamp ts )
	{
		Vector<Vector< Object >> data = new Vector< Vector< Object > >();		

		
		String sql = "SELECT p.DateTrx,p.C_Payment_ID,p.DocumentNo, p.C_Currency_ID,c.ISO_Code, p.PayAmt,"
			+ "currencyConvert(p.PayAmt,p.C_Currency_ID,ba.C_Currency_ID,?,p.C_ConversionType_ID,p.AD_Client_ID,p.AD_Org_ID),"   //  #1
			+ " bp.Name "
			+ "FROM C_BankAccount ba"
			+ " INNER JOIN C_Payment_v p ON (p.C_BankAccount_ID=ba.C_BankAccount_ID)"
			+ " INNER JOIN C_Currency c ON (p.C_Currency_ID=c.C_Currency_ID)"
			+ " LEFT OUTER JOIN C_BPartner bp ON (p.C_BPartner_ID=bp.C_BPartner_ID) "
			+ "WHERE p.Processed='Y' AND p.IsReconciled='N'"
			+ " AND p.DocStatus IN ('CO','CL','RE','VO') AND p.PayAmt<>0"
			+ " AND p.C_BankAccount_ID=?"                              	//  #2
			+ " AND NOT EXISTS (SELECT * FROM C_BankStatementLine l " 
			//	Voided Bank Statements have 0 StmtAmt
				+ "WHERE p.C_Payment_ID=l.C_Payment_ID AND l.StmtAmt <> 0)";
		
		if (ts == null)
			ts = new Timestamp(System.currentTimeMillis());

		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setTimestamp(1, ts);
			pstmt.setInt(2, C_BankAccount_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>(6);
				line.add(Boolean.valueOf(false));   //  0-Selection
				line.add(rs.getTimestamp(1));       //  1-DateTrx
				line.add(rs.getInt(2));				//  2-C_Payment_ID
				line.add(rs.getString(3));          //  3-Document No
				KeyNamePair pp = new KeyNamePair(rs.getInt(4), rs.getString(5));
				line.add(pp);                       //  4-Currency
				line.add(rs.getBigDecimal(6));      //  5-PayAmt
				line.add(rs.getBigDecimal(7));      //  6-Conv Amt
				line.add(rs.getString(8));      	//  7-BParner
				data.add(line);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		return data;
		
	}
	
	
	/**
	 *  Load Data - Bank Account
	 *  @param C_BankAccount_ID Bank Account
	 */
	private void loadBankAccount (int C_BankAccount_ID)
	{
		log.config ("C_BankAccount_ID=" + C_BankAccount_ID);
		/**
		 *  Selected        - 0
		 *  Date            - 1
		 *  C_Payment_ID    - 2
		 *  C_Currenncy     - 3
		 *  Amt             - 4
		 */

		//  Get StatementDate
		Timestamp ts = (Timestamp)p_mTab.getValue("StatementDate");
		Vector<Vector< Object >> data = getBankAccountData( Env.getCtx(), C_BankAccount_ID, ts );		
		
		
		
		//  Header Info
		Vector<String> columnNames = new Vector<String>(6);
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		for( FieldVO vo : getTableFieldVOs( Env.getCtx() ) )
		{
			columnNames.add( vo.name );
		}

		//  Remove previous listeners
		dataTable.getModel().removeTableModelListener(this);
		//  Set Model
		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		model.addTableModelListener(this);		
		dataTable.setModel(model);
		//
		dataTable.setColumnClass(0, Boolean.class, false);      	//  0-Selection
		dataTable.setColumnClass(1, Timestamp.class, true);     	//  1-TrxDate
		dataTable.setColumnClass(2, IDColumn.class, false);  		//  2-Payment
		dataTable.setColumnClass(3, String.class, true);        	//  3-Document No
		dataTable.setColumnClass(4, String.class, true);        	//  4-Currency
		dataTable.setColumnClass(5, BigDecimal.class, true);    	//  5-Amount
		dataTable.setColumnClass(6, BigDecimal.class, true);    	//  6-ConvAmount
		dataTable.setColumnClass(7, String.class, true);    		//  7-BPartner
		//  Table UI
		dataTable.getColumnModel().getColumn(2).setResizable(false); // For Payment Field.
		dataTable.autoSize();
		// ensure select all button is only available when there is data
		int rows = dataTable.getRowCount();
		selectAllAction.setEnabled(rows > 0);
	}   //  loadBankAccount

	/**
	 *  List total amount
	 */
	@Override
	protected void info()
	{
		DecimalFormat format = DisplayType.getNumberFormat(DisplayTypeConstants.Amount);

		TableModel model = dataTable.getModel();
		BigDecimal total = new BigDecimal(0.0);
		int rows = model.getRowCount();
		int count = 0;
		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)model.getValueAt(i, 0)).booleanValue())
			{
				total = total.add((BigDecimal)model.getValueAt(i, 6));
				count++;
			}
		}
		statusBar.setStatusLine(String.valueOf(count) + " - " + Msg.getMsg(Env.getCtx(), "Sum") + "  " + format.format(total));
	}   //  infoStatement

	
	
	
	/**
	 *  Save Statement - Insert Data
	 *  @return true if saved
	 */
	@Override
	protected boolean save()
	{
		log.config("");
		TableModel model = dataTable.getModel();

		//  fixed values
		int C_BankStatement_ID = ((Integer)p_mTab.getValue("C_BankStatement_ID")).intValue();

		return saveData( Env.getCtx(), model, C_BankStatement_ID );
	}


	public static boolean saveData( Ctx ctx, TableModel model, int C_BankStatement_ID )
	{
		int rows = model.getRowCount();
		if (rows == 0)
			return false;

		MBankStatement bs = new MBankStatement (ctx, C_BankStatement_ID, null);
		s_log.config(bs.toString());
		
		//  Lines
		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)model.getValueAt(i, 0)).booleanValue())
			{
				Timestamp trxDate = (Timestamp)model.getValueAt(i, 1);  //  1-DateTrx
				int C_Payment_ID = (Integer)model.getValueAt(i, 2); 	//	2-C_Payment_ID
				KeyNamePair pp = (KeyNamePair)model.getValueAt(i, 4);   //  3-Currency
				int C_Currency_ID = pp.getKey();
				BigDecimal TrxAmt = (BigDecimal)model.getValueAt(i, 5); //  4-PayAmt
			//	BigDecimal StmtAmt = (BigDecimal)model.getValueAt(i, 6);//  5-Conv Amt
				//
				s_log.fine("Line Date=" + trxDate
					+ ", Payment=" + C_Payment_ID + ", Currency=" + C_Currency_ID + ", Amt=" + TrxAmt);
				//	
				MPayment payment = new MPayment(ctx, C_Payment_ID, null);
				MBankStatementLine bsl = new MBankStatementLine (bs);
				bsl.setStatementLineDate(trxDate);
				bsl.setDateAcct(payment.getDateAcct());
				bsl.setPayment(new MPayment(ctx, C_Payment_ID, null));
				if (!bsl.save())
					s_log.log(Level.SEVERE, "Line not created #" + i);
			}   //   if selected
		}   //  for all rows
		return true;
	}   //  save

}   //  VCreateFromStatemen