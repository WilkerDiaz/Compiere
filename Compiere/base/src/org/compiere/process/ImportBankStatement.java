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
package org.compiere.process;

import java.math.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.*;

import org.compiere.Compiere;
import org.compiere.common.CompiereSQLException;
import org.compiere.common.CompiereStateException;
import org.compiere.framework.PO;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Import Bank Statement from I_BankStatement
 *
 *	author Eldir Tomassen
 *	@version $Id: ImportBankStatement.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class ImportBankStatement extends SvrProcess
{
	/**	Client to be imported to		*/
	private int				p_AD_Client_ID = 0;
	/**	Organization to be imported to	*/
	private int				p_AD_Org_ID = 0;
	/** Default Bank Account			*/
	private int				p_C_BankAccount_ID = 0;
	/**	Delete old Imported				*/
	private boolean			p_deleteOldImported = false;
	
	private static final String STD_CLIENT_CHECK = " AND AD_Client_ID=?";
	
	private static final boolean TESTMODE = false;
	/** Commit every 100 entities	*/
	private static final int	COMMITCOUNT =TESTMODE?100:Integer.parseInt(Ini.getProperty(Ini.P_IMPORT_BATCH_SIZE));

	
	/** Properties						*/
	private Ctx				m_ctx;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) 
		{
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("AD_Client_ID"))
				p_AD_Client_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("AD_Org_ID"))
				p_AD_Org_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("C_BankAccount_ID"))
				p_C_BankAccount_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				p_deleteOldImported = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		m_ctx = Env.getCtx();
	}	//	prepare


	/**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception
	 */
	@Override
	protected String doIt() throws java.lang.Exception
	{
		log.info("AD_Org_ID=" + p_AD_Org_ID + ", C_BankAccount_ID" + p_C_BankAccount_ID);
		String sql = null;
		int no = 0;

		//	****	Prepare	****

		//	Delete Old Imported
		if (p_deleteOldImported)
		{
			sql = "DELETE FROM I_BankStatement "
				  + "WHERE I_IsImported='Y'" + STD_CLIENT_CHECK;
			no = DB.executeUpdate(get_Trx(), sql,p_AD_Client_ID);
			log.fine("Delete Old Impored =" + no);
		}

		//	Set Client, Org, IsActive, Created/Updated
		sql = "UPDATE I_BankStatement "
			  + "SET AD_Client_ID = COALESCE (AD_Client_ID, ?),"
			  + " AD_Org_ID = COALESCE (AD_Org_ID,?),"
			  +	" IsActive = COALESCE (IsActive, 'Y'),"
			  + " Created = COALESCE (Created, SysDate),"
			  + " CreatedBy = COALESCE (CreatedBy, 0),"
			  + " Updated = COALESCE (Updated, SysDate),"
			  + " UpdatedBy = COALESCE (UpdatedBy, 0),"
			  + " I_ErrorMsg = NULL,"
			  + " I_IsImported = 'N' "
			  + "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL OR AD_Client_ID IS NULL OR AD_Org_ID IS NULL OR AD_Client_ID=0 OR AD_Org_ID=0";
		//Object[] params = new Object[]{p_AD_Client_ID,p_AD_Org_ID};
		no = DB.executeUpdate(get_Trx(), sql,p_AD_Client_ID,p_AD_Org_ID);
		log.info ("Reset=" + no);

		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  //java bug, it could not be used directly
		sql = "UPDATE I_BankStatement o "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Org, '"
			+ "WHERE (AD_Org_ID IS NULL OR AD_Org_ID=0"
			+ " OR EXISTS (SELECT * FROM AD_Org oo WHERE o.AD_Org_ID=oo.AD_Org_ID AND (oo.IsSummary='Y' OR oo.IsActive='N')))"
			+ " AND I_IsImported<>'Y'" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql,p_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Org=" + no);
			
		//	Set Bank Account
		sql = "UPDATE I_BankStatement i "
			+ "SET C_BankAccount_ID="
			+ "( "
			+ " SELECT C_BankAccount_ID "
			+ " FROM C_BankAccount a, C_Bank b "
			+ " WHERE b.IsOwnBank='Y' "
			+ " AND a.AD_Client_ID=i.AD_Client_ID "
			+ " AND a.C_Bank_ID=b.C_Bank_ID "
			+ " AND a.AccountNo=i.BankAccountNo "
			+ " AND b.RoutingNo=i.RoutingNo "
			+ " OR b.SwiftCode=i.RoutingNo "
			+ ") "
			+ "WHERE i.C_BankAccount_ID IS NULL "
			+ "AND i.I_IsImported<>'Y' "
			+ "OR i.I_IsImported IS NULL" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql,p_AD_Client_ID);
		if (no != 0)
			log.info("Bank Account (With Routing No)=" + no);
		//
		sql = "UPDATE I_BankStatement i " 
		 	+ "SET C_BankAccount_ID="
			+ "( "
			+ " SELECT C_BankAccount_ID "
			+ " FROM C_BankAccount a, C_Bank b "
			+ " WHERE b.IsOwnBank='Y' "
			+ " AND a.C_Bank_ID=b.C_Bank_ID " 
			+ " AND a.AccountNo=i.BankAccountNo "
			+ " AND a.AD_Client_ID=i.AD_Client_ID "
			+ ") "
			+ "WHERE i.C_BankAccount_ID IS NULL "
			+ "AND i.I_isImported<>'Y' "
			+ "OR i.I_isImported IS NULL" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql,p_AD_Client_ID);
		if (no != 0)
			log.info("Bank Account (Without Routing No)=" + no);
		//
		sql = "UPDATE I_BankStatement i "
			+ "SET C_BankAccount_ID=(SELECT C_BankAccount_ID "
			                     + " FROM C_BankAccount a "
			                     + " WHERE a.C_BankAccount_ID= ? "
			                     + " AND a.AD_Client_ID=i.AD_Client_ID) "
			+ "WHERE i.C_BankAccount_ID IS NULL "
			+ "AND i.BankAccountNo IS NULL "
			+ "AND i.I_isImported<>'Y' "
			+ "OR i.I_isImported IS NULL" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql,p_C_BankAccount_ID,p_AD_Client_ID);
		if (no != 0)
			log.info("Bank Account=" + no);
		//	
		sql = "UPDATE I_BankStatement "
			+ "SET I_isImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Bank Account, ' "
			+ "WHERE C_BankAccount_ID IS NULL "
			+ "AND I_isImported<>'Y' "
			+ "OR I_isImported IS NULL" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql,p_AD_Client_ID);
		if (no != 0)
			log.warning("Invalid Bank Account=" + no);
		 
		//	Set Currency
		sql = "UPDATE I_BankStatement i "
			+ "SET C_Currency_ID=(SELECT C_Currency_ID FROM C_Currency c"
			+ " WHERE i.ISO_Code=c.ISO_Code AND c.AD_Client_ID IN (0,i.AD_Client_ID)) "
			+ "WHERE C_Currency_ID IS NULL"
			+ " AND I_IsImported<>'Y'" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql,p_AD_Client_ID);
		if (no != 0)
			log.info("Set Currency=" + no);
		//
		sql = "UPDATE I_BankStatement i "
			+ "SET C_Currency_ID=(SELECT C_Currency_ID FROM C_BankAccount WHERE C_BankAccount_ID=i.C_BankAccount_ID) "
			+ "WHERE i.C_Currency_ID IS NULL "
			+ "AND i.ISO_Code IS NULL" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql,p_AD_Client_ID);
		if (no != 0)
			log.info("Set Currency=" + no);
		//
		sql = "UPDATE I_BankStatement "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Currency,' "
			+ "WHERE C_Currency_ID IS NULL "
			+ "AND I_IsImported<>'E' "
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql,p_AD_Client_ID);
		if (no != 0)
			log.warning("Invalid Currency=" + no);
		
		 
		//	Set Amount
		 sql = "UPDATE I_BankStatement "
		 	+ "SET ChargeAmt=0 "
			+ "WHERE ChargeAmt IS NULL "
			+ "AND I_IsImported<>'Y'" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql,p_AD_Client_ID);
		if (no != 0)
			log.info("Charge Amount=" + no);
		//
		 sql = "UPDATE I_BankStatement "
		 	+ "SET InterestAmt=0 "
			+ "WHERE InterestAmt IS NULL "
			+ "AND I_IsImported<>'Y'" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql,p_AD_Client_ID);
		if (no != 0)
			log.info("Interest Amount=" + no);
		//
		 sql = "UPDATE I_BankStatement "
		 	+ "SET TrxAmt=StmtAmt - InterestAmt - ChargeAmt "
			+ "WHERE TrxAmt IS NULL "
			+ "AND I_IsImported<>'Y'" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql,p_AD_Client_ID);
		if (no != 0)
			log.info("Transaction Amount=" + no);
		//
		sql = "UPDATE I_BankStatement "
			+ "SET I_isImported='E', I_ErrorMsg="+ts +"||'Err=Invalid Amount, ' "
			+ "WHERE TrxAmt + ChargeAmt + InterestAmt <> StmtAmt "
			+ "AND I_isImported<>'Y'" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql,p_AD_Client_ID);
		if (no != 0)
			log.info("Invaid Amount=" + no);
		 
		 //	Set Valuta Date
		sql = "UPDATE I_BankStatement "
		 	+ "SET ValutaDate=StatementLineDate "
			+ "WHERE ValutaDate IS NULL "
			+ "AND I_isImported<>'Y'" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql,p_AD_Client_ID);
		if (no != 0)
			log.info("Valuta Date=" + no);
		
		//Set RateType
		
		sql = "UPDATE I_BankStatement i "
			  + "SET C_ConversionType_ID=(SELECT MAX(ctype.C_ConversionType_ID) FROM C_ConversionType ctype"
			  + " WHERE i.RateTypeName=ctype.Name AND (i.AD_Client_ID=ctype.AD_Client_ID OR ctype.AD_Client_ID =0) ) "
			  + " WHERE C_ConversionType_ID IS NULL AND RateTypeName IS NOT NULL"
			  + " AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,p_AD_Client_ID);
		if (no != 0)
			log.fine("Set C_ConversionType_ID from RateType Name=" + no);
		
		// Set Charge
		sql = "UPDATE I_BankStatement i "
			  + "SET C_Charge_ID=(SELECT MAX(C_Charge_ID) FROM C_Charge ch"
			  + " WHERE i.ChargeName=ch.Name AND i.AD_Client_ID=ch.AD_Client_ID) "
			  + " WHERE C_Charge_ID IS NULL AND ChargeName IS NOT NULL"
			  + " AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,p_AD_Client_ID);
		if (no != 0)
			log.fine("Set Charge from Name=" + no);
		
		// Check Charge if the charge amount is greater than 0
		sql = "UPDATE I_BankStatement "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=No Charge,' "
			+ "WHERE C_Charge_ID IS NULL "
			+ " AND ChargeAmt>0 "
			+ " AND I_IsImported<>'E' "
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,p_AD_Client_ID);
		if (no != 0)
			log.warning("No Charge=" + no);
		
			
		//	Check Payment<->Invoice combination
		sql = "UPDATE I_BankStatement "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'Err=Invalid Payment<->Invoice, ' "
			+ "WHERE I_BankStatement_ID IN "
				+ "(SELECT I_BankStatement_ID "
				+ "FROM I_BankStatement i"
				+ " INNER JOIN C_Payment p ON (i.C_Payment_ID=p.C_Payment_ID) "
				+ "WHERE i.C_Invoice_ID IS NOT NULL "
				+ " AND p.C_Invoice_ID IS NOT NULL "
				+ " AND p.C_Invoice_ID<>i.C_Invoice_ID) "
				+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql,p_AD_Client_ID);
		if (no != 0)
			log.info("Payment<->Invoice Mismatch=" + no);
			
		//	Check Payment<->BPartner combination
		sql = "UPDATE I_BankStatement "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'Err=Invalid Payment<->BPartner, ' "
			+ "WHERE I_BankStatement_ID IN "
				+ "(SELECT I_BankStatement_ID "
				+ "FROM I_BankStatement i"
				+ " INNER JOIN C_Payment p ON (i.C_Payment_ID=p.C_Payment_ID) "
				+ "WHERE i.C_BPartner_ID IS NOT NULL "
				+ " AND p.C_BPartner_ID IS NOT NULL "
				+ " AND p.C_BPartner_ID<>i.C_BPartner_ID) "
				+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql,p_AD_Client_ID);
		if (no != 0)
			log.info("Payment<->BPartner Mismatch=" + no);
			
		//	Check Invoice<->BPartner combination
		sql = "UPDATE I_BankStatement "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'Err=Invalid Invoice<->BPartner, ' "
			+ "WHERE I_BankStatement_ID IN "
				+ "(SELECT I_BankStatement_ID "
				+ "FROM I_BankStatement i"
				+ " INNER JOIN C_Invoice v ON (i.C_Invoice_ID=v.C_Invoice_ID) "
				+ "WHERE i.C_BPartner_ID IS NOT NULL "
				+ " AND v.C_BPartner_ID IS NOT NULL "
				+ " AND v.C_BPartner_ID<>i.C_BPartner_ID) "
				+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql,p_AD_Client_ID);
		if (no != 0)
			log.info("Invoice<->BPartner Mismatch=" + no);
			
		//	Check Invoice.BPartner<->Payment.BPartner combination
		sql = "UPDATE I_BankStatement "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'Err=Invalid Invoice.BPartner<->Payment.BPartner, ' "
			+ "WHERE I_BankStatement_ID IN "
				+ "(SELECT I_BankStatement_ID "
				+ "FROM I_BankStatement i"
				+ " INNER JOIN C_Invoice v ON (i.C_Invoice_ID=v.C_Invoice_ID)"
				+ " INNER JOIN C_Payment p ON (i.C_Payment_ID=p.C_Payment_ID) "
				+ "WHERE p.C_Invoice_ID<>v.C_Invoice_ID"
				+ " AND v.C_BPartner_ID<>p.C_BPartner_ID) "
				+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql,p_AD_Client_ID);
		if (no != 0)
			log.info("Invoice.BPartner<->Payment.BPartner Mismatch=" + no);
		
		
			
		//	Detect Duplicates
		 sql = "SELECT i.I_BankStatement_ID, l.C_BankStatementLine_ID, i.EftTrxID "
			+ "FROM I_BankStatement i, C_BankStatement s, C_BankStatementLine l "
			+ "WHERE i.I_isImported='N' "
			+ "AND s.C_BankStatement_ID=l.C_BankStatement_ID "
			+ "AND i.EftTrxID IS NOT NULL AND "
			//	Concatinate EFT Info
			+ "(l.EftTrxID||l.EftAmt||l.EftStatementLineDate||l.EftValutaDate||l.EftTrxType||l.EftCurrency||l.EftReference||s.EftStatementReference "
			+ "||l.EftCheckNo||l.EftMemo||l.EftPayee||l.EftPayeeAccount) "
			+ "= "
			+ "(i.EftTrxID||i.EftAmt||i.EftStatementLineDate||i.EftValutaDate||i.EftTrxType||i.EftCurrency||i.EftReference||i.EftStatementReference "
			+ "||i.EftCheckNo||i.EftMemo||i.EftPayee||i.EftPayeeAccount) ";
		
		String updateSql = "UPDATE I_Bankstatement "
				+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'Err=Duplicate['||?||']' "
				+ "WHERE I_BankStatement_ID=?"
				+ STD_CLIENT_CHECK;
		PreparedStatement pupdt = DB.prepareStatement(updateSql, get_Trx());
		
		PreparedStatement pstmtDuplicates = null;
		ResultSet rs = null;
		no = 0;
		try
		{
			pstmtDuplicates = DB.prepareStatement(sql, get_Trx());
			rs = pstmtDuplicates.executeQuery();
			while (rs.next())
			{
				String info = "Line_ID=" + rs.getInt(2)		//	l.C_BankStatementLine_ID
				 + ",EDTTrxID=" + rs.getString(3);			//	i.EftTrxID
				pupdt.setString(1, info);	
				pupdt.setInt(2, rs.getInt(1));	//	i.I_BankStatement_ID
				pupdt.setInt(3, p_AD_Client_ID);
				pupdt.executeUpdate();
				no++;
			}
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "DetectDuplicates " + e.getMessage());
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmtDuplicates);
			DB.closeStatement(pupdt);
		}
		if (no != 0)
			log.info("Duplicates=" + no);
		
		commit();
		
		//	Set Error to indicator to not imported
		String errorIndicatorSql = "UPDATE I_BankStatement "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;

		
		//Import Bank Statement
		sql = "SELECT * FROM I_BankStatement"
			+ " WHERE I_IsImported='N'"
			+ " ORDER BY C_BankAccount_ID, Name, EftStatementDate, EftStatementReference";
			
		MBankStatement statement = null;
		MBankAccount account = null;
		PreparedStatement pstmt = null;
		
		Map<Integer, X_I_BankStatement> importBankStatementMap = new HashMap<Integer, X_I_BankStatement>();
		Map <MBankStatement, List<MBankStatementLine>> bankStatementMap = new HashMap <MBankStatement, List<MBankStatementLine>> ();
		List<MBankStatementLine> bankStatementLines = null;
		
		int noInsert = 0;
		int noInsertLine = 0;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			rs = pstmt.executeQuery();
				
			while (rs.next())
			{ 
				X_I_BankStatement imp = new X_I_BankStatement(getCtx(), rs, get_Trx());
				
				//	Get the bank account for the first statement
				if (account == null)
				{
					account = MBankAccount.get (m_ctx, imp.getC_BankAccount_ID());
					statement = null;
					log.info("New Statement, Account=" + account.getAccountNo());
				}
				//	Create a new Bank Statement for every account
				else if (account.getC_BankAccount_ID() != imp.getC_BankAccount_ID())
				{
					account = MBankAccount.get (m_ctx, imp.getC_BankAccount_ID());
					statement = null;
					log.info("New Statement, Account=" + account.getAccountNo());
				}
				//	Create a new Bank Statement for every statement name
				else if ((statement.getName() != null) && (imp.getName() != null))
				{
					if (!statement.getName().equals(imp.getName()))
					{
						statement = null;
						log.info("New Statement, Statement Name=" + imp.getName());
					}
				}
				//	Create a new Bank Statement for every statement reference
				else if ((statement.getEftStatementReference() != null) && (imp.getEftStatementReference() != null))
				{
					if (!statement.getEftStatementReference().equals(imp.getEftStatementReference()))
					{
						statement = null;
						log.info("New Statement, Statement Reference=" + imp.getEftStatementReference());
					}
				}
				//	Create a new Bank Statement for every statement date
				else if ((statement.getStatementDate() != null) && (imp.getStatementDate() != null))
				{
					if (!statement.getStatementDate().equals(imp.getStatementDate()))
					{
						statement = null;
						log.info("New Statement, Statement Date=" + imp.getStatementDate());
					}
				}
				
				//	New Statement
				if (statement == null)
				{
					if(bankStatementMap.size() >= COMMITCOUNT) {
						saveBankStatement(importBankStatementMap,bankStatementMap);
						bankStatementMap.clear();
						importBankStatementMap.clear();
					}				

					
					statement = new MBankStatement(account, imp);	
					bankStatementLines=new ArrayList<MBankStatementLine>();					
					bankStatementMap.put(statement, bankStatementLines);
					noInsert++;

	
				}
				
				importBankStatementMap.put(imp.getI_BankStatement_ID(), imp);
				MBankStatementLine line = new MBankStatementLine(imp);				
				bankStatementLines.add(line);
				
				noInsertLine++;
				line = null;
				
			}
			saveBankStatement(importBankStatementMap, bankStatementMap);
			
			//	Close database connection
		}
		catch(Exception e)
		{
			noInsertLine=0;
			noInsert=0;			
			no = DB.executeUpdate(get_Trx(), errorIndicatorSql,p_AD_Client_ID);
			addLog (0, null, new BigDecimal (no), "@Errors@");
			addLog (0, null, new BigDecimal (noInsert), "@C_BankStatement_ID@: @Inserted@");
			addLog (0, null, new BigDecimal (noInsertLine), "@C_BankStatementLine_ID@: @Inserted@");
			log.log(Level.SEVERE, "Bank Statements - " + sql, e);
			throw new CompiereStateException(e.getMessage());

		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		no = DB.executeUpdate(get_Trx(), errorIndicatorSql,p_AD_Client_ID);
		addLog (0, null, new BigDecimal (no), "@Errors@");
		addLog (0, null, new BigDecimal (noInsert), "@C_BankStatement_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (noInsertLine), "@C_BankStatementLine_ID@: @Inserted@");
		return "";

	}	//	doIt
	
	private void saveBankStatement(Map<Integer, X_I_BankStatement> importBankStatementMap, Map <MBankStatement, List<MBankStatementLine>> bankStatementMap)
	{
		if(importBankStatementMap.isEmpty())
			return;
		
		//Save Bank Statements
		List<MBankStatement> bankStatementsToSave = new ArrayList<MBankStatement>(bankStatementMap.keySet());
		if(!PO.saveAll(get_Trx(), bankStatementsToSave)) 
			throw new CompiereStateException("Could not save Bank Statement");
		
		//Save Bank Statement Lines
		bankStatementsToSave.clear();
		BigDecimal stmtdiff=Env.ZERO;
		List<MBankStatementLine> bankStmtLinesToSave = new ArrayList<MBankStatementLine>();
		for(Map.Entry<MBankStatement, List<MBankStatementLine>> entry : bankStatementMap.entrySet()) {
			MBankStatement bankStmt = entry.getKey();
			MBankAccount ba=MBankAccount.get(getCtx(),bankStmt.getC_BankAccount_ID());
			for(MBankStatementLine stmtLine : entry.getValue()) {
				stmtLine.setC_BankStatement_ID(bankStmt.getC_BankStatement_ID());
				bankStmtLinesToSave.add(stmtLine);
				BigDecimal lineAmt = Env.ZERO;
				lineAmt=MConversionRate.convert (getCtx(),
						stmtLine.getStmtAmt(), stmtLine.getC_Currency_ID(), MBankAccount.get(getCtx(), bankStmt.getC_BankAccount_ID()).getC_Currency_ID(),
						stmtLine.getDateAcct(),stmtLine.getC_ConversionType_ID(), stmtLine.getAD_Client_ID(),stmtLine.getAD_Org_ID());
				if (lineAmt == null)
				{
					throw new CompiereStateException("Could not convert C_Currency_ID=" + stmtLine.getC_Currency_ID()
						+ " to base C_Currency_ID=" + MClient.get(Env.getCtx()).getC_Currency_ID());
					
				}
				
				stmtdiff = stmtdiff.add(lineAmt);

			}
			bankStmt.setStatementDifference(stmtdiff);
			bankStmt.setBeginningBalance(ba.getCurrentBalance());
			bankStmt.setEndingBalance(bankStmt.getBeginningBalance().add(stmtdiff));
			bankStatementsToSave.add(bankStmt);
			stmtdiff=Env.ZERO;
		}
		
		if(!PO.saveAll(get_Trx(), bankStmtLinesToSave)) 
			throw new CompiereStateException("Could not save Bank Statement Lines");
		
		if(!PO.saveAll(get_Trx(), bankStatementsToSave)) 
			throw new CompiereStateException("Could not update Statement Difference & Ending Balance");

		
	
			
		List<X_I_BankStatement> importBankStmtsToSave = new ArrayList<X_I_BankStatement>();
		
		for(MBankStatementLine line :bankStmtLinesToSave)
		{
			X_I_BankStatement imp = importBankStatementMap.get(line.getI_BankStatement_ID());
			if(imp != null) {
				imp.setC_BankStatementLine_ID(line.getC_BankStatementLine_ID());
				imp.setC_BankStatement_ID(line.getC_BankStatement_ID());
				imp.setI_IsImported(X_I_BankStatement.I_ISIMPORTED_Yes);
				imp.setProcessed(true);
				importBankStmtsToSave.add(imp);
			}
		}
		
		if(!PO.saveAll(get_Trx(), importBankStmtsToSave)) 
			throw new CompiereStateException("Could not save Bank Statement import");
		commit();

	
	}
	
	
	public static void main(String[] args)
	{
		System.setProperty ("PropertyFile", "C:/Compiere.properties");
		Compiere.startup(true);
		CLogMgt.setLoggerLevel(Level.INFO, null);
		CLogMgt.setLevel(Level.INFO);
		//	Same Login entries as entered
		Ini.setProperty(Ini.P_UID, "GardenAdmin");
		Ini.setProperty(Ini.P_PWD, "GardenAdmin");
		Ini.setProperty(Ini.P_ROLE, "GardenWorld Admin");
		Ini.setProperty(Ini.P_CLIENT, "GardenWorld");
		Ini.setProperty(Ini.P_ORG, "HQ");
		Ini.setProperty(Ini.P_WAREHOUSE, "HQ Warehouse");
		Ini.setProperty(Ini.P_LANGUAGE, "English");
		Ini.setProperty(Ini.P_IMPORT_BATCH_SIZE, "100");

		Ctx ctx = Env.getCtx();
		Login login = new Login(ctx);
		if (!login.batchLogin(null, null))
			System.exit(1);

		//	Reduce Log level for performance
		CLogMgt.setLoggerLevel(Level.WARNING, null);
		CLogMgt.setLevel(Level.WARNING);

		//	Data from Login Context
		int AD_Client_ID = ctx.getAD_Client_ID();
		int AD_User_ID = ctx.getAD_User_ID();
		//	Hardcoded
		int AD_Process_ID = 221;
		int AD_Table_ID = 0;
		int Record_ID = 0;

		//	Step 1: Setup Process
		MPInstance instance = new MPInstance(Env.getCtx(), AD_Process_ID, Record_ID);
		instance.save();

		ProcessInfo pi = new ProcessInfo("Import", AD_Process_ID, AD_Table_ID, Record_ID);
		pi.setAD_Client_ID(AD_Client_ID);
		pi.setAD_User_ID(AD_User_ID);
		pi.setIsBatch(true);  //  want to wait for result
		pi.setAD_PInstance_ID (instance.getAD_PInstance_ID());

		DB.startLoggingUpdates();

		// Step 3: Run the process directly
		ImportBankStatement test = new ImportBankStatement();
		test.p_AD_Client_ID = ctx.getAD_Client_ID();
		test.p_deleteOldImported = true;
		test.p_C_BankAccount_ID=100;
		test.p_AD_Org_ID=11;

		long start = System.currentTimeMillis();

		test.startProcess(ctx, pi, null);

		long end = System.currentTimeMillis();
		long durationMS = end - start;
		long duration = durationMS/1000;
		System.out.println("Total: " + duration + "s");

		// Step 4: get results
		if (pi.isError())
			System.err.println("Error: " + pi.getSummary());
		else
			System.out.println("OK: " + pi.getSummary());
		System.out.println(pi.getLogInfo());

		// stop logging database updates
		String logResult = DB.stopLoggingUpdates(0);
		System.out.println(logResult);

	}


}	//	ImportBankStatement
