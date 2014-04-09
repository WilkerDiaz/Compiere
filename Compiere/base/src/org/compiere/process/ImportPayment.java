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
 * 	Import Payments
 *	
 *  @author Jorg Janke
 *  @version $Id: ImportPayment.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class ImportPayment extends SvrProcess
{
	/**	Organization to be imported to	*/
	private int				m_AD_Org_ID = 0;
	/** Default Bank Account			*/
	private int				m_C_BankAccount_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_DeleteOldImported = false;
	
	private int				m_AD_Client_ID = 0;

	/** Properties						*/
	private Ctx 			m_ctx;
	
	private static final String STD_CLIENT_CHECK = " AND AD_Client_ID=? " ;	

	
	private static final boolean TESTMODE = true;
	/** Commit every 100 entities	*/
	private static final int	COMMITCOUNT =TESTMODE?100:Integer.parseInt(Ini.getProperty(Ini.P_IMPORT_BATCH_SIZE));

	
	
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
		int AD_Process_ID = 343;
		int AD_Table_ID = 0;
		int Record_ID = 0;

		//	Step 1: Setup Process
		MPInstance instance = new MPInstance(Env.getCtx(), AD_Process_ID, Record_ID);
		instance.save();

		ProcessInfo pi = new ProcessInfo("Import", AD_Process_ID, AD_Table_ID, Record_ID);
		pi.setAD_Client_ID(AD_Client_ID);
		pi.setAD_User_ID(AD_User_ID);
		pi.setIsBatch(false);  //  want to wait for result
		pi.setAD_PInstance_ID (instance.getAD_PInstance_ID());

		DB.startLoggingUpdates();

		// Step 3: Run the process directly
		ImportPayment test = new ImportPayment();
		test.m_AD_Client_ID = ctx.getAD_Client_ID();
		test.m_DeleteOldImported = true;
		test.m_C_BankAccount_ID=100;
		test.m_AD_Org_ID=11;

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
			else if (name.equals("AD_Org_ID"))
				m_AD_Org_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("C_BankAccount_ID"))
				m_C_BankAccount_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				m_DeleteOldImported = "Y".equals(element.getParameter());
		//	else if (name.equals("DocAction"))
		//		m_docAction = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		m_ctx = Env.getCtx();
	}	//	prepare

	/**
	 * 	Proccess
	 *	@return info
	 *	@throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("C_BankAccount_ID" + m_C_BankAccount_ID);
		MBankAccount ba = MBankAccount.get(getCtx(), m_C_BankAccount_ID);
		if (m_C_BankAccount_ID == 0 || ba.get_ID() != m_C_BankAccount_ID)
			throw new CompiereUserException("@NotFound@ @C_BankAccount_ID@ - " + m_C_BankAccount_ID);
		if (m_AD_Org_ID != ba.getAD_Org_ID() && ba.getAD_Org_ID() != 0)
			m_AD_Org_ID = ba.getAD_Org_ID();
		log.info("AD_Org_ID=" + m_AD_Org_ID);
		
		String sql = null;
		int no = 0;
		m_AD_Client_ID= ba.getAD_Client_ID();

		//	****	Prepare	****

		//	Delete Old Imported
		if (m_DeleteOldImported)
		{
			sql = "DELETE FROM I_Payment "
				  + "WHERE I_IsImported='Y'"
				  + STD_CLIENT_CHECK;
			no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
			log.fine("Delete Old Impored =" + no);
		}

		//	Set Client, Org, IsActive, Created/Updated
		sql = "UPDATE I_Payment "
			  + "SET AD_Client_ID = COALESCE (AD_Client_ID,?),"
			  + " AD_Org_ID = COALESCE (AD_Org_ID,?),"
			  + " IsActive = COALESCE (IsActive, 'Y'),"
			  + " Created = COALESCE (Created, SysDate),"
			  + " CreatedBy = COALESCE (CreatedBy, 0),"
			  + " Updated = COALESCE (Updated, SysDate),"
			  + " UpdatedBy = COALESCE (UpdatedBy, 0),"
			  + " I_ErrorMsg = NULL,"
			  + " I_IsImported = 'N' "
			  + "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL " 
			  +	"OR AD_Client_ID IS NULL OR AD_Org_ID IS NULL OR AD_Client_ID=0 OR AD_Org_ID=0";
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID,m_AD_Org_ID);
		log.info ("Reset=" + no);

		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  //java bug, it could not be used directly
		sql = "UPDATE I_Payment o "
			+ "SET I_IsImported='E', I_ErrorMsg="+ ts +"||'ERR=Invalid Org, '"
			+ "WHERE (AD_Org_ID IS NULL OR AD_Org_ID=0"
			+ " OR EXISTS (SELECT * FROM AD_Org oo WHERE o.AD_Org_ID=oo.AD_Org_ID AND (oo.IsSummary='Y' OR oo.IsActive='N')))"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid Org=" + no);
			
		//	Set Bank Account
		sql = "UPDATE I_Payment i "
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
			+ "OR i.I_IsImported IS NULL"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.info("Bank Account (With Routing No)=" + no);
		//
		sql = "UPDATE I_Payment i " 
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
			+ "OR i.I_isImported IS NULL"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.info("Bank Account (Without Routing No)=" + no);
		//
		sql = "UPDATE I_Payment i "
			+ "SET C_BankAccount_ID=(SELECT C_BankAccount_ID FROM C_BankAccount a " 
			+ " WHERE a.C_BankAccount_ID= ?"			   
			+ " and a.AD_Client_ID=i.AD_Client_ID) "
			+ "WHERE i.C_BankAccount_ID IS NULL "
			+ "AND i.BankAccountNo IS NULL "
			+ "AND i.I_isImported<>'Y' "
			+ "OR i.I_isImported IS NULL"
			+  STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_C_BankAccount_ID,m_AD_Client_ID);
		if (no != 0)
			log.info("Bank Account=" + no);
		//	
		sql = "UPDATE I_Payment "
			+ "SET I_isImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Bank Account, ' "
			+ "WHERE C_BankAccount_ID IS NULL "
			+ "AND I_isImported<>'Y' "
			+ "OR I_isImported IS NULL"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning("Invalid Bank Account=" + no);
		 
		//	Set Currency
		sql = "UPDATE I_Payment i "
			+ "SET C_Currency_ID=(SELECT C_Currency_ID FROM C_Currency c"
			+ " WHERE i.ISO_Code=c.ISO_Code AND c.AD_Client_ID IN (0,i.AD_Client_ID)) "
			+ "WHERE C_Currency_ID IS NULL"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.info("Set Currency=" + no);
		//
		sql = "UPDATE I_Payment i "
				+ "SET C_Currency_ID=(SELECT C_Currency_ID FROM C_BankAccount WHERE C_BankAccount_ID=i.C_BankAccount_ID) "
			+ "WHERE i.C_Currency_ID IS NULL "
			+ "AND i.ISO_Code IS NULL"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.info("Set Currency=" + no);
		//
		sql = "UPDATE I_Payment "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=No Currency,' "
			+ "WHERE C_Currency_ID IS NULL "
			+ "AND I_IsImported<>'E' "
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning("No Currency=" + no);
		 
		//	Set Amount
		sql = "UPDATE I_Payment "
		 	+ "SET ChargeAmt=0 "
			+ "WHERE ChargeAmt IS NULL "
			+ "AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.info("Charge Amount=" + no);
		//
		sql = "UPDATE I_Payment "
		 	+ "SET TaxAmt=0 "
			+ "WHERE TaxAmt IS NULL "
			+ "AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.info("Tax Amount=" + no);
		//
		sql = "UPDATE I_Payment "
			+ "SET WriteOffAmt=0 "
			+ "WHERE WriteOffAmt IS NULL "
			+ "AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.info("WriteOff Amount=" + no);
		//
		sql = "UPDATE I_Payment "
			+ "SET DiscountAmt=0 "
			+ "WHERE DiscountAmt IS NULL "
			+ "AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.info("Discount Amount=" + no);
		//
				//	Set Date
		sql = "UPDATE I_Payment "
		 	+ "SET DateTrx=Created "
			+ "WHERE DateTrx IS NULL "
			+ "AND I_isImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.info("Trx Date=" + no);
		
		//	Invoice
		sql = "UPDATE I_Payment i "
			  + "SET C_Invoice_ID=(SELECT MAX(C_Invoice_ID) FROM C_Invoice ii"
			  + " WHERE i.InvoiceDocumentNo=ii.DocumentNo AND i.AD_Client_ID=ii.AD_Client_ID) "
			  + "WHERE C_Invoice_ID IS NULL AND InvoiceDocumentNo IS NOT NULL"
			  + " AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.fine("Set Invoice from DocumentNo=" + no);
		
		//	BPartner
		sql = "UPDATE I_Payment i "
			  + "SET C_BPartner_ID=(SELECT MAX(C_BPartner_ID) FROM C_BPartner bp"
			  + " WHERE i.BPartnerValue=bp.Value AND i.AD_Client_ID=bp.AD_Client_ID) "
			  + "WHERE C_BPartner_ID IS NULL AND BPartnerValue IS NOT NULL"
			  + " AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.fine("Set BP from Value=" + no);
		
		sql = "UPDATE I_Payment i "
			  + "SET C_BPartner_ID=(SELECT MAX(C_BPartner_ID) FROM C_Invoice ii"
			  + " WHERE i.C_Invoice_ID=ii.C_Invoice_ID AND i.AD_Client_ID=ii.AD_Client_ID) "
			  + "WHERE C_BPartner_ID IS NULL AND C_Invoice_ID IS NOT NULL"
			  + " AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.fine("Set BP from Invoice=" + no);
		
		sql = "UPDATE I_Payment "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=No BPartner,' "
			+ "WHERE C_BPartner_ID IS NULL "
			+ "AND I_IsImported<>'E' "
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning("No BPartner=" + no);
		
		
		//	Check Payment<->Invoice combination
		sql = "UPDATE I_Payment "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'Err=Invalid Payment<->Invoice, ' "
			+ "WHERE I_Payment_ID IN "
				+ "(SELECT I_Payment_ID "
				+ "FROM I_Payment i"
				+ " INNER JOIN C_Payment p ON (i.C_Payment_ID=p.C_Payment_ID) "
				+ "WHERE i.C_Invoice_ID IS NOT NULL "
				+ " AND p.C_Invoice_ID IS NOT NULL "
				+ " AND p.C_Invoice_ID<>i.C_Invoice_ID) "
				+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.info("Payment<->Invoice Mismatch=" + no);
			
		//	Check Payment<->BPartner combination
		sql = "UPDATE I_Payment "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'Err=Invalid Payment<->BPartner, ' "
			+ "WHERE I_Payment_ID IN "
				+ "(SELECT I_Payment_ID "
				+ "FROM I_Payment i"
				+ " INNER JOIN C_Payment p ON (i.C_Payment_ID=p.C_Payment_ID) "
				+ "WHERE i.C_BPartner_ID IS NOT NULL "
				+ " AND p.C_BPartner_ID IS NOT NULL "
				+ " AND p.C_BPartner_ID<>i.C_BPartner_ID) "
				+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.info("Payment<->BPartner Mismatch=" + no);
			
		//	Check Invoice<->BPartner combination
		sql = "UPDATE I_Payment "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'Err=Invalid Invoice<->BPartner, ' "
			+ "WHERE I_Payment_ID IN "
				+ "(SELECT I_Payment_ID "
				+ "FROM I_Payment i"
				+ " INNER JOIN C_Invoice v ON (i.C_Invoice_ID=v.C_Invoice_ID) "
				+ "WHERE i.C_BPartner_ID IS NOT NULL "
				+ " AND v.C_BPartner_ID IS NOT NULL "
				+ " AND v.C_BPartner_ID<>i.C_BPartner_ID) "
				+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.info("Invoice<->BPartner Mismatch=" + no);
			
		//	Check Invoice.BPartner<->Payment.BPartner combination
		sql = "UPDATE I_Payment "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'Err=Invalid Invoice.BPartner<->Payment.BPartner, ' "
			+ "WHERE I_Payment_ID IN "
				+ "(SELECT I_Payment_ID "
				+ "FROM I_Payment i"
				+ " INNER JOIN C_Invoice v ON (i.C_Invoice_ID=v.C_Invoice_ID)"
				+ " INNER JOIN C_Payment p ON (i.C_Payment_ID=p.C_Payment_ID) "
				+ "WHERE p.C_Invoice_ID<>v.C_Invoice_ID"
				+ " AND v.C_BPartner_ID<>p.C_BPartner_ID) "
				+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.info("Invoice.BPartner<->Payment.BPartner Mismatch=" + no);
			

		if (no != 0)
			log.info("TenderType Default=" + no);

		//	Document Type
		sql = "UPDATE I_Payment i "
			  + "SET C_DocType_ID=(SELECT C_DocType_ID FROM C_DocType d WHERE d.Name=i.DocTypeName"
			  + " AND d.DocBaseType IN ('ARR','APP') AND i.AD_Client_ID=d.AD_Client_ID) "
			  + "WHERE C_DocType_ID IS NULL AND DocTypeName IS NOT NULL AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.fine("Set DocType=" + no);
		sql = "UPDATE I_Payment "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid DocTypeName, ' "
			  + "WHERE C_DocType_ID IS NULL AND DocTypeName IS NOT NULL"
			  + " AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("Invalid DocTypeName=" + no);
		sql = "UPDATE I_Payment "
			  + "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=No DocType, ' "
			  + "WHERE C_DocType_ID IS NULL"
			  + " AND I_IsImported<>'Y'"
			  + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(),sql,m_AD_Client_ID);
		if (no != 0)
			log.warning ("No DocType=" + no);

		commit();
		
		//	Set Error to indicator to not imported
		String errorIndicatorSql = "UPDATE I_Payment "
									+ "SET I_IsImported='N', Updated=SysDate "
									+ "WHERE I_IsImported<>'Y'"
									+ STD_CLIENT_CHECK;
		
		//Import Bank Statement
		sql = "SELECT * FROM I_Payment"
			+ " WHERE I_IsImported='N'"
			+ STD_CLIENT_CHECK
			+ " ORDER BY C_BankAccount_ID, CheckNo, DateTrx, R_AuthCode";
			
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		int noInsert = 0;
		List<MPayment> paymentsToSave=new ArrayList<MPayment>();
		Map<Integer, X_I_Payment> importPaymentMap = new HashMap<Integer, X_I_Payment>();
		
		
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			pstmt.setInt(1, m_AD_Client_ID);
			 rs = pstmt.executeQuery();
				
			while (rs.next())
			{ 
				
				if(paymentsToSave.size() >= COMMITCOUNT) {
					savePayments(paymentsToSave,importPaymentMap);
					paymentsToSave.clear();
					importPaymentMap.clear();
				}
				
				X_I_Payment imp = new X_I_Payment(m_ctx, rs, get_Trx());
				importPaymentMap.put(imp.getI_Payment_ID(), imp);
				MPayment payment = new MPayment (imp);
				paymentsToSave.add(payment);
				noInsert++;
			}
			savePayments(paymentsToSave,importPaymentMap);

		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "Payments - " + sql, e);
			noInsert=0;
			no = DB.executeUpdate(get_Trx(),errorIndicatorSql,m_AD_Client_ID);
			addLog (0, null, new BigDecimal (no), "@Errors@");
			addLog (0, null, new BigDecimal (noInsert), "@C_Payment_ID@: @Inserted@");
			throw new CompiereSQLException();

		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		no = DB.executeUpdate(get_Trx(),errorIndicatorSql,m_AD_Client_ID);
		addLog (0, null, new BigDecimal (no), "@Errors@");
		addLog (0, null, new BigDecimal (noInsert), "@C_Payment_ID@: @Inserted@");

		return "";
	}	//	doIt
	
	
	public void savePayments(List<MPayment> paymentsToSave,Map<Integer, X_I_Payment> importPaymentMap)
	{
		List<X_I_Payment> importPaymentsToSave = new ArrayList<X_I_Payment>();
		
		if(!PO.saveAll(get_Trx(), paymentsToSave)) 
			throw new CompiereStateException("Could not save Payments");
		
	
		
		for(MPayment payment :paymentsToSave)
		{
			X_I_Payment imp = importPaymentMap.get(payment.getI_Payment_ID());
			if(imp != null) {				
				imp.setC_Payment_ID(payment.getC_Payment_ID());
				imp.setI_IsImported(X_I_Payment.I_ISIMPORTED_Yes);
				imp.setProcessed(true);
				importPaymentsToSave.add(imp);
			}
		}
		
		if(!PO.saveAll (get_Trx(), importPaymentsToSave))
			throw new CompiereStateException("Could not save Import payment records");

		commit();
	}
	
	
}	//	ImportPayment
