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
package org.compiere.model;
 
import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.api.*;
import org.compiere.framework.*;
import org.compiere.util.*;
 
/**
 *	Bank Statement Line Model
 *
 *	@author Eldir Tomassen/Jorg Janke
 *	@version $Id: MBankStatementLine.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
 public class MBankStatementLine extends X_C_BankStatementLine
 {
    /** Logger for class MBankStatementLine */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBankStatementLine.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int I_BankStatement_ID=0;
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_BankStatementLine_ID id
	 *	@param trx transaction
	 */
	public MBankStatementLine (Ctx ctx, int C_BankStatementLine_ID, Trx trx)
	{
		super (ctx, C_BankStatementLine_ID, trx);
		if (C_BankStatementLine_ID == 0)
		{
		//	setC_BankStatement_ID (0);		//	Parent
		//	setC_Charge_ID (0);
		//	setC_Currency_ID (0);	//	Bank Acct Currency
		//	setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM C_BankStatementLine WHERE C_BankStatement_ID=@C_BankStatement_ID@
			setStmtAmt(Env.ZERO);
			setTrxAmt(Env.ZERO);
			setInterestAmt(Env.ZERO);
			setChargeAmt(Env.ZERO);
			setIsReversal (false);
		//	setValutaDate (new Timestamp(System.currentTimeMillis()));	// @StatementDate@
		//	setDateAcct (new Timestamp(System.currentTimeMillis()));	// @StatementDate@
		}
	}	//	MBankStatementLine
	
	/**
	 *	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MBankStatementLine (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MBankStatementLine
	
	/**
	 * 	Parent Constructor
	 * 	@param statement Bank Statement that the line is part of
	 */
	public MBankStatementLine(MBankStatement statement)
	{
		this (statement.getCtx(), 0, statement.get_Trx());
		setClientOrg(statement);
		setC_BankStatement_ID(statement.getC_BankStatement_ID());
		setStatementLineDate(statement.getStatementDate());
	}	//	MBankStatementLine

	/**
	 * 	Parent Constructor
	 * 	@param statement Bank Statement that the line is part of
	 * 	@param lineNo position of the line within the statement
	 */
	public MBankStatementLine(MBankStatement statement, int lineNo)
	{
		this (statement);
		setLine(lineNo);
	}	//	MBankStatementLine
	
	public MBankStatementLine (X_I_BankStatement imp)
	{
		this(imp.getCtx(), 0, imp.get_Trx());	
		PO.copyValues(imp, this, imp.getAD_Client_ID(), imp.getAD_Org_ID());
		I_BankStatement_ID=imp.getI_BankStatement_ID();
		
		if (imp.getStatementLineDate() !=null)
			setStatementLineDate(imp.getStatementLineDate());
		else
			setStatementLineDate(imp.getStatementDate());

		
	}

	public int getI_BankStatement_ID()
	{
		return I_BankStatement_ID;
	}

	
	/**
	 * 	Set Payment
	 *	@param payment payment
	 */
	public void setPayment (MPayment payment)
	{
		setC_Payment_ID (payment.getC_Payment_ID());
		setC_Currency_ID (payment.getC_Currency_ID());
		setC_ConversionType_ID(payment.getC_ConversionType_ID());
		setC_BPartner_ID(payment.getC_BPartner_ID());
		if (payment.getC_Invoice_ID() != 0)
			setC_Invoice_ID(payment.getC_Invoice_ID());

		//
		BigDecimal amt = payment.getPayAmt(true); 
		setTrxAmt(amt);
		setStmtAmt(amt);
		//
		setDescription(payment.getDescription());
	}	//	setPayment

	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription

	
	/**
	 * 	Set Statement Line Date and all other dates (Valuta, Acct)
	 *	@param StatementLineDate date
	 */
	@Override
	public void setStatementLineDate(Timestamp StatementLineDate)
	{
		super.setStatementLineDate(StatementLineDate);
		setValutaDate (StatementLineDate);
		setDateAcct (StatementLineDate);
	}	//	setStatementLineDate

	/**
	 * 	Set StatementLineDate - Callout
	 *	@param oldStatementLineDate old
	 *	@param newStatementLineDate new
	 *	@param windowNo window no
	 */
	@UICallout public void setStatementLineDate (String oldStatementLineDate, 
			String newStatementLineDate, int windowNo) throws Exception
	{
		if (newStatementLineDate == null || newStatementLineDate.length() == 0)
			return;
		Timestamp statementLineDate = PO.convertToTimestamp(newStatementLineDate);
		if (statementLineDate == null)
			return;
		setStatementLineDate(statementLineDate);
	}	//	setDateOrdered
	
	/**
	 * 	Set ChargeAmt - Callout
	 *	@param oldChangeAmt old value
	 *	@param newChangeAmt new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setChargeAmt (String oldChargeAmt, 
			String newChargeAmt, int windowNo) throws Exception
	{
		if (newChargeAmt == null || newChargeAmt.length() == 0)
			return;
		BigDecimal ChargeAmt = new BigDecimal(newChargeAmt);
		super.setChargeAmt(ChargeAmt);
		setAmt("ChargeAmt");
	}	//	setChargeAmt
	
	/**
	 * 	Set InterestAmt - Callout
	 *	@param oldInterestAmt old value
	 *	@param newInterestAmt new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setInterestAmt (String oldInterestAmt, 
			String newInterestAmt, int windowNo) throws Exception
	{
		if (newInterestAmt == null || newInterestAmt.length() == 0)
			return;
		BigDecimal InterestAmt = new BigDecimal(newInterestAmt);
		super.setInterestAmt(InterestAmt);
		setAmt("InterestAmt");
	}	//	setInterestAmt
	
	/**
	 * 	Set StmtAmt - Callout
	 *	@param oldStmtAmt old value
	 *	@param newStmtAmt new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setStmtAmt (String oldStmtAmt, 
			String newStmtAmt, int windowNo) throws Exception
	{
		if (newStmtAmt == null || newStmtAmt.length() == 0)
			return;
		BigDecimal StmtAmt = new BigDecimal(newStmtAmt);
		super.setStmtAmt(StmtAmt);
		setAmt("StmtAmt");
	}	//	setStmtAmt

	/**
	 * 	Set Charge/Interest Amount
	 *	@param windowNo window
	 * 	@param columnName callout source
	 */
	private void setAmt (String columnName)
	{
		BigDecimal stmt = getStmtAmt();
		if (stmt == null)
			stmt = Env.ZERO;
		BigDecimal p_trx = getTrxAmt();
		if (p_trx == null)
			p_trx = Env.ZERO;
		BigDecimal bd = stmt.subtract(p_trx);

		//  Charge - calculate Interest
		if (columnName.equals("ChargeAmt"))
		{
			BigDecimal charge = getChargeAmt();
			if (charge == null)
				charge = Env.ZERO;
			bd = bd.subtract(charge);
		//	log.trace(log.l5_DData, "Interest (" + bd + ") = Stmt(" + stmt + ") - Trx(" + p_trx + ") - Charge(" + charge + ")");
			setInterestAmt(bd);
		}
		//  Calculate Charge
		else
		{
			BigDecimal interest = getInterestAmt();
			if (interest == null)
				interest = Env.ZERO;
			bd = bd.subtract(interest);
		//	log.trace(log.l5_DData, "Charge (" + bd + ") = Stmt(" + stmt + ") - Trx(" + p_trx + ") - Interest(" + interest + ")");
			setChargeAmt(bd);
		}
	}	//	setAmt
	
	
	/**
	 * 	Set Payment - Callout.
	 * 	@param oldC_Payment_ID old ID
	 * 	@param newC_Payment_ID new ID
	 * 	@param windowNo window
	 */
	@UICallout public void setC_Payment_ID (String oldC_Payment_ID, 
			String newC_Payment_ID, int windowNo) throws Exception
	{
		if (newC_Payment_ID == null || newC_Payment_ID.length() == 0)
			return;
		int C_Payment_ID = Integer.parseInt(newC_Payment_ID);
		if (C_Payment_ID == 0)
			return;
		setC_Payment_ID(C_Payment_ID);
		
		BigDecimal stmt = getStmtAmt();
		if (stmt == null)
			stmt = Env.ZERO;

		String sql = "SELECT PayAmt,C_Currency_ID,C_ConversionType_ID FROM C_Payment_v WHERE C_Payment_ID=?";		//	1
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, C_Payment_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				BigDecimal bd = rs.getBigDecimal(1);
				setTrxAmt(bd);
				if (stmt.signum() == 0)
					setStmtAmt(bd);
				setC_Currency_ID(rs.getInt("C_Currency_ID"));
				setC_ConversionType_ID(rs.getInt("C_ConversionType_ID"));
				
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
		//  Recalculate Amounts
		setAmt ("C_Payment_ID");
	}	//	setC_Payment_ID
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (getChargeAmt().signum() != 0 && getC_Charge_ID() == 0)
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "C_Charge_ID"));
			return false;
		}
		//	Set Line No
		if (getLine() == 0)
		{
			String sql = "SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM C_BankStatementLine WHERE C_BankStatement_ID=?";
			int ii = QueryUtil.getSQLValue (get_Trx(), sql, getC_BankStatement_ID());
			setLine (ii);
		}
		
		//	Set References
		if (getC_Payment_ID() != 0 && getC_BPartner_ID() == 0)
		{
			MPayment payment = new MPayment (getCtx(), getC_Payment_ID(), get_Trx());
			setC_BPartner_ID(payment.getC_BPartner_ID());
			setC_Currency_ID(payment.getC_Currency_ID());
			setC_ConversionType_ID(payment.getC_ConversionType_ID());
			if (payment.getC_Invoice_ID() != 0)
				setC_Invoice_ID(payment.getC_Invoice_ID());
		}
		if (getC_Invoice_ID() != 0 && getC_BPartner_ID() == 0)
		{
			MInvoice invoice = new MInvoice (getCtx(), getC_Invoice_ID(), get_Trx());
			setC_BPartner_ID(invoice.getC_BPartner_ID());
			setC_Currency_ID(invoice.getC_Currency_ID());
			setC_ConversionType_ID(invoice.getC_ConversionType_ID());

		}
		
		//	Calculate Charge = Statement - p_trx - Interest  
		BigDecimal amt = getStmtAmt();
		amt = amt.subtract(getTrxAmt());
		amt = amt.subtract(getInterestAmt());
		if (amt.compareTo(getChargeAmt()) != 0)
			setChargeAmt (amt);
		//
		
		return true;
	}	//	beforeSave
	
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!getCtx().isBatchMode())
			return updateHeader();
		return success;
	}	//	afterSave
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterDelete (boolean success)
	{
		
		return updateHeader();
	}	//	afterSave

	/**
	 * 	Update Header
	 */
	private boolean updateHeader()
	{
		MBankStatement bs = new MBankStatement(getCtx(), getC_BankStatement_ID(), get_Trx());
		MBankStatementLine[] lines = bs.getLines(true);
		BigDecimal stmtdiff= Env.ZERO;
		
		for (MBankStatementLine line : lines) {
			BigDecimal lineAmt = Env.ZERO;
			lineAmt=MConversionRate.convert (getCtx(),
					line.getStmtAmt(), line.getC_Currency_ID(), MBankAccount.get(getCtx(), bs.getC_BankAccount_ID()).getC_Currency_ID(),
					line.getDateAcct(),line.getC_ConversionType_ID(), line.getAD_Client_ID(),line.getAD_Org_ID());
			
			if (lineAmt == null)
			{
				log.saveError("Error","Could not convert C_Currency_ID=" + getC_Currency_ID()
					+ " to base C_Currency_ID=" + MClient.get(Env.getCtx()).getC_Currency_ID());
				return false;
			}
			
			stmtdiff = stmtdiff.add(lineAmt);

		}
		
		
		String sql = "UPDATE C_BankStatement bs"
			+ " SET StatementDifference= ? "
			+ "WHERE C_BankStatement_ID= ? ";
		DB.executeUpdate(get_Trx(), sql,stmtdiff,getC_BankStatement_ID());
		
		sql = "UPDATE C_BankStatement bs"
			+ " SET EndingBalance=BeginningBalance+StatementDifference "
			+ "WHERE C_BankStatement_ID= ? ";
		DB.executeUpdate(get_Trx(), sql,getC_BankStatement_ID());
		
		return true;
	}	//	updateHeader
	
 }	//	MBankStatementLine
