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

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.process.*;
import org.compiere.util.*;
import org.compiere.vos.*;

/**
 *  Payment Print/Export model.
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MPaySelectionCheck.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class MPaySelectionCheck extends X_C_PaySelectionCheck
{
    /** Logger for class MPaySelectionCheck */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPaySelectionCheck.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Check for Payment
	 *	@param ctx context
	 *	@param C_Payment_ID id
	 *	@param trx transaction
	 *	@return pay selection check for payment or null
	 */
	public static MPaySelectionCheck getOfPayment (Ctx ctx, int C_Payment_ID, Trx trx)
	{
		MPaySelectionCheck retValue = null;
		String sql = "SELECT * FROM C_PaySelectionCheck WHERE C_Payment_ID=?";
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, C_Payment_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MPaySelectionCheck psc = new MPaySelectionCheck (ctx, rs, trx);
				if (retValue == null)
					retValue = psc;
				else if (!retValue.isProcessed() && psc.isProcessed())
					retValue = psc;
				count++;
			}
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e); 
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		if (count > 1)
			s_log.warning ("More then one for C_Payment_ID=" + C_Payment_ID);
		return retValue;
	}	//	getOfPayment

	/**
	 * 	Create Check for Payment
	 *	@param ctx context
	 *	@param C_Payment_ID id
	 *	@param trx transaction
	 *	@return pay selection check for payment or null
	 */
	public static MPaySelectionCheck createForPayment (Ctx ctx, int C_Payment_ID, Trx trx)
	{
		if (C_Payment_ID == 0)
			return null;
		MPayment payment = new MPayment (ctx, C_Payment_ID, null);
		//	Map Payment Rule <- Tender Type
		String PaymentRule = PAYMENTRULE_Check;
		if (payment.getTenderType().equals(X_C_Payment.TENDERTYPE_CreditCard))
			PaymentRule = PAYMENTRULE_CreditCard;
		else if (payment.getTenderType().equals(X_C_Payment.TENDERTYPE_DirectDebit))
			PaymentRule = PAYMENTRULE_DirectDebit;
		else if (payment.getTenderType().equals(X_C_Payment.TENDERTYPE_DirectDeposit))
			PaymentRule = PAYMENTRULE_DirectDeposit;
	//	else if (payment.getTenderType().equals(MPayment.TENDERTYPE_Check))
	//		PaymentRule = MPaySelectionCheck.PAYMENTRULE_Check;
		
		//	Create new PaySelection
		MPaySelection ps = new MPaySelection(ctx, 0, trx);
		ps.setC_BankAccount_ID (payment.getC_BankAccount_ID());
		ps.setName (Msg.translate(ctx, "C_Payment_ID") + ": " + payment.getDocumentNo());
		ps.setDescription(payment.getDescription());
		ps.setPayDate (payment.getDateTrx());
		ps.setTotalAmt (payment.getPayAmt());
		ps.setIsApproved (true);
		ps.save();
		
		//	Create new PaySelection Line
		MPaySelectionLine psl = null;
		if (payment.getC_Invoice_ID() != 0)
		{
			psl = new MPaySelectionLine (ps, 10, PaymentRule);
			psl.setC_Invoice_ID(payment.getC_Invoice_ID());
			psl.setIsSOTrx (payment.isReceipt());
			psl.setOpenAmt(payment.getPayAmt().add(payment.getDiscountAmt()));
			psl.setPayAmt (payment.getPayAmt());
			psl.setDiscountAmt(payment.getDiscountAmt());
			psl.setDifferenceAmt (Env.ZERO);
			psl.save();
		}
		
		//	Create new PaySelection Check
		MPaySelectionCheck psc = new MPaySelectionCheck(ps, PaymentRule);
		psc.setC_BPartner_ID (payment.getC_BPartner_ID());
		psc.setC_Payment_ID(payment.getC_Payment_ID());
		psc.setIsReceipt(payment.isReceipt());
		psc.setPayAmt (payment.getPayAmt());
		psc.setDiscountAmt(payment.getDiscountAmt());
		psc.setQty (1);
		psc.setCheckNo(payment.getDocumentNo());
		psc.setProcessed(true);
		psc.save();
		
		//	Update optional Line
		if (psl != null)
		{
			psl.setC_PaySelectionCheck_ID(psc.getC_PaySelectionCheck_ID());
			psl.setProcessed(true);
			psl.save();
		}
		
		//	Indicate Done
		ps.setProcessed(true);
		ps.save();
		return psc;
	}	//	createForPayment

	
	/**************************************************************************
	 *  Get Checks of Payment Selection
	 *
	 *  @param C_PaySelection_ID Payment Selection
	 *  @param PaymentRule Payment Rule
	 *  @param startDocumentNo start document no
	 *	@param trx transaction
	 *  @return array of checks
	 */
	static public MPaySelectionCheck[] get (int C_PaySelection_ID,
		String PaymentRule, int startDocumentNo, Trx trx)
	{
		s_log.fine("C_PaySelection_ID=" + C_PaySelection_ID
			+ ", PaymentRule=" +  PaymentRule + ", startDocumentNo=" + startDocumentNo);
		ArrayList<MPaySelectionCheck> list = new ArrayList<MPaySelectionCheck>();

		int docNo = startDocumentNo;
		String sql = "SELECT * FROM C_PaySelectionCheck "
			+ "WHERE C_PaySelection_ID=? AND PaymentRule=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt(1, C_PaySelection_ID);
			pstmt.setString(2, PaymentRule);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MPaySelectionCheck check = new MPaySelectionCheck (Env.getCtx(), rs, trx);
				//	Set new Check Document No - saved in confirmPrint
				check.setCheckNo(String.valueOf(docNo++));
				list.add(check);
			}
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//  convert to Array
		MPaySelectionCheck[] retValue = new MPaySelectionCheck[list.size()];
		list.toArray(retValue);
		return retValue;
	}   //  get

	
	/**************************************************************************
	 *  Export to File
	 *  @param checks array of checks
	 *  @param file file to export checks
	 *  @return number of lines
	 */
	public static int exportToFile (MPaySelectionCheck[] checks, File file)
	{
		if (checks == null || checks.length == 0)
			return 0;
		//  Must be a file
		if (file.isDirectory())
		{
			s_log.log(Level.WARNING, "File is directory - " + file.getAbsolutePath());
			return 0;
		}
		//  delete if exists
		try
		{
			if (file.exists())
				file.delete();
		}
		catch (Exception e)
		{
			s_log.log(Level.WARNING, "Could not delete - " + file.getAbsolutePath(), e);
		}

		char x = '"';      //  ease
		int noLines = 0;
		StringBuffer line = null;
		try
		{
			FileWriter fw = new FileWriter(file);

			//  write header
			line = new StringBuffer();
			line.append(x).append("Value").append(x).append(",")
				.append(x).append("Name").append(x).append(",")
				.append(x).append("Contact").append(x).append(",")
				.append(x).append("Addr1").append(x).append(",")
				.append(x).append("Addr2").append(x).append(",")
				.append(x).append("City").append(x).append(",")
				.append(x).append("State").append(x).append(",")
				.append(x).append("ZIP").append(x).append(",")
				.append(x).append("Country").append(x).append(",")
				.append(x).append("ReferenceNo").append(x).append(",")
			    .append(x).append("BPRoutingNo").append(x).append(",")
				.append(x).append("BPAccountNo").append(x).append(",")
				.append(x).append("BPAName").append(x).append(",")
				.append(x).append("BPACity").append(x).append(",")
				.append(x).append("BPBBAN").append(x).append(",")
				.append(x).append("BPIBAN").append(x).append(",")
				.append(x).append("BAName").append(x).append(",")
				.append(x).append("BARoutingNo").append(x).append(",")
				.append(x).append("BASwiftCode").append(x).append(",")
				.append(x).append("DocumentNo").append(x).append(",")
				.append(x).append("PayDate").append(x).append(",")
				.append(x).append("Currency").append(x).append(",")
				.append(x).append("PayAmount").append(x).append(",")
				.append(x).append("Comment").append(x)
				.append(Env.NL);
			fw.write(line.toString());
			noLines++;

			//  write lines
			for (MPaySelectionCheck mpp : checks) {
				if (mpp == null)
					continue;
				//  BPartner Info
				String bp[] = getBPartnerInfo(mpp.getC_BPartner_ID());
				//  Target BankAccount Info
				String bpba[] = getBPBankAccountInfo(mpp.getC_BP_BankAccount_ID ());

				//  Comment - list of invoice document no
				StringBuffer comment = new StringBuffer();
				MPaySelectionLine[] psls = mpp.getPaySelectionLines(false);
				for (int l = 0; l < psls.length; l++)
				{
					if (l > 0)
						comment.append(", ");
					comment.append(psls[l].getInvoice().getDocumentNo());
				}
				line = new StringBuffer();
				line.append(x).append(bp[BP_VALUE]).append(x).append(",")   // Value
					.append(x).append(bp[BP_NAME]).append(x).append(",")    // Name
					.append(x).append(bp[BP_CONTACT]).append(x).append(",") // Contact
					.append(x).append(bp[BP_ADDR1]).append(x).append(",")   // Addr1
					.append(x).append(bp[BP_ADDR2]).append(x).append(",")   // Addr2
					.append(x).append(bp[BP_CITY]).append(x).append(",")    // City
					.append(x).append(bp[BP_REGION]).append(x).append(",")  // State
					.append(x).append(bp[BP_POSTAL]).append(x).append(",")  // ZIP
					.append(x).append(bp[BP_COUNTRY]).append(x).append(",") // Country
					.append(x).append(bp[BP_REFNO]).append(x).append(",")   // ReferenceNo
				    .append(x).append(bpba[BPBA_RoutingNo]).append(x).append(",")   // Routing No (as of BPBankAccount
					.append(x).append(bpba[BPBA_AccountNo]).append(x).append(",")   // AccountNo
					.append(x).append(bpba[BPBA_AName]).append(x).append(",")       // Account Name
					.append(x).append(bpba[BPBA_ACity]).append(x).append(",")       // Account City
					.append(x).append(bpba[BPBA_BBAN]).append(x).append(",")        // BBAN
					.append(x).append(bpba[BPBA_IBAN]).append(x).append(",")        // IBAN
					.append(x).append(bpba[BA_Name]).append(x).append(",")          // Bank Name
					.append(x).append(bpba[BA_RoutingNo]).append(x).append(",")     // Bank RoutingNo
					.append(x).append(bpba[BA_SwitftCode]).append(x).append(",")    // SwiftCode
					//  Payment Info
					.append(x).append(mpp.getCheckNo()).append(x).append(",")    // DocumentNo
					.append(mpp.getParent().getPayDate()).append(",")               // PayDate
					.append(x).append(MCurrency.getISO_Code(Env.getCtx(), mpp.getParent().getC_Currency_ID())).append(x).append(",")    // Currency
					.append(mpp.getPayAmt()).append(",")                // PayAmount
					.append(x).append(comment.toString()).append(x)     // Comment
					.append(Env.NL);
				fw.write(line.toString());
				noLines++;
			}   //  write line

			fw.flush();
			fw.close();
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "", e);
		}

		return noLines;
	}   //  exportToFile
	
	
	/**
	 *  Get Customer/Vendor Info.
	 *  Based on BP_ static variables
	 *  @param C_BPartner_ID BPartner
	 *  @return info array
	 */
	private static String[] getBPartnerInfo (int C_BPartner_ID)
	{
		String[] bp = new String[10];

		String sql = "SELECT bp.Value, bp.Name, c.Name AS Contact, "
			+ "a.Address1, a.Address2, a.City, r.Name AS Region, a.Postal, "
			+ "cc.Name AS Country, bp.ReferenceNo "
			+ "FROM C_BPartner bp "
			+ "LEFT OUTER JOIN AD_User c ON (bp.C_BPartner_ID=c.C_BPartner_ID) "
			+ "INNER JOIN C_BPartner_Location l ON (bp.C_BPartner_ID=l.C_BPartner_ID) "
			+ "INNER JOIN C_Location a ON (l.C_Location_ID=a.C_Location_ID) "
			+ "LEFT OUTER JOIN C_Region r ON (a.C_Region_ID=r.C_Region_ID) "
			+ "INNER JOIN C_Country cc ON (a.C_Country_ID=cc.C_Country_ID) "
			+ "WHERE bp.C_BPartner_ID=?"        // #1
			+ "ORDER BY l.IsBillTo DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_BPartner_ID);
			rs = pstmt.executeQuery();
			//
			if (rs.next())
			{
				bp[BP_VALUE] = rs.getString(1);
				if (bp[BP_VALUE] == null)
					bp[BP_VALUE] = "";
				bp[BP_NAME] = rs.getString(2);
				if (bp[BP_NAME] == null)
					bp[BP_NAME] = "";
				bp[BP_CONTACT] = rs.getString(3);
				if (bp[BP_CONTACT] == null)
					bp[BP_CONTACT] = "";
				bp[BP_ADDR1] = rs.getString(4);
				if (bp[BP_ADDR1] == null)
					bp[BP_ADDR1] = "";
				bp[BP_ADDR2] = rs.getString(5);
				if (bp[BP_ADDR2] == null)
					bp[BP_ADDR2] = "";
				bp[BP_CITY] = rs.getString(6);
				if (bp[BP_CITY] == null)
					bp[BP_CITY] = "";
				bp[BP_REGION] = rs.getString(7);
				if (bp[BP_REGION] == null)
					bp[BP_REGION] = "";
				bp[BP_POSTAL] = rs.getString(8);
				if (bp[BP_POSTAL] == null)
					bp[BP_POSTAL] = "";
				bp[BP_COUNTRY] = rs.getString(9);
				if (bp[BP_COUNTRY] == null)
					bp[BP_COUNTRY] = "";
				bp[BP_REFNO] = rs.getString(10);
				if (bp[BP_REFNO] == null)
					bp[BP_REFNO] = "";
			}
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return bp;
	}   //  getBPartnerInfo


	/** BankAccount Info Index for RoutingNo       */
	private static final int     BPBA_RoutingNo = 0;
	/** BankAccount Info Index for AccountNo       */
	private static final int     BPBA_AccountNo = 1;
	/** BankAccount Info Index for AccountName     */
	private static final int     BPBA_AName = 2;
	/** BankAccount Info Index for AccountCity     */
	private static final int     BPBA_ACity = 3;
	/** BankAccount Info Index for BBAN            */
	private static final int     BPBA_BBAN = 4;
	/** BankAccount Info Index for IBAN            */
	private static final int     BPBA_IBAN = 5;
	/** BankAccount Info Index for Bank Name       */
	private static final int     BA_Name = 6;
	/** BankAccount Info Index for Bank RoutingNo  */
	private static final int     BA_RoutingNo = 7;
	/** BankAccount Info Index for Bank SwiftCode  */
	private static final int     BA_SwitftCode = 8;

	/**
	 *  Get Bank Account Info for target Accpimt.
	 *  Based on BP_ static variables
	 *  @param C_BPartner_ID BPartner
	 *  @return info array
	 */
	private static String[] getBPBankAccountInfo (int C_BP_BankAccount_ID)
	{
		String[] bp = new String[10];

		String sql = "SELECT bpba.RoutingNo, bpba.AccountNo, bpba.A_Name, bpba.A_City, bpba.BBAN, "
			+ "bpba.IBAN, ba.Name, ba.RoutingNo, ba.SwiftCode "
			+ "FROM C_BP_BankAccount bpba "
			+ "LEFT OUTER JOIN C_Bank ba ON (bpba.C_Bank_ID = ba.C_Bank_ID) "
			+ "WHERE bpba.C_BP_BankAccount_ID=?";        // #1
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_BP_BankAccount_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				bp[BPBA_RoutingNo] = rs.getString(1);
				if (bp[BPBA_RoutingNo] == null)
					bp[BPBA_RoutingNo] = "";
				bp[BPBA_AccountNo] = rs.getString(2);
				if (bp[BPBA_AccountNo] == null)
					bp[BPBA_AccountNo] = "";
				bp[BPBA_AName] = rs.getString(3);
				if (bp[BPBA_AName] == null)
					bp[BPBA_AName] = "";
				bp[BPBA_ACity] = rs.getString(4);
				if (bp[BPBA_ACity] == null)
					bp[BPBA_ACity] = "";
				bp[BPBA_BBAN] = rs.getString(5);
				if (bp[BPBA_BBAN] == null)
					bp[BPBA_BBAN] = "";
				bp[BPBA_IBAN] = rs.getString(6);
				if (bp[BPBA_IBAN] == null)
					bp[BPBA_IBAN] = "";
				bp[BA_Name] = rs.getString(7);
				if (bp[BA_Name] == null)
					bp[BA_Name] = "";
				bp[BA_RoutingNo] = rs.getString(8);
				if (bp[BA_RoutingNo] == null)
					bp[BA_RoutingNo] = "";
				bp[BA_SwitftCode] = rs.getString(9);
				if (bp[BA_SwitftCode] == null)
					bp[BA_SwitftCode] = "";
			}
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return bp;
	}   //  getBPartnerInfo
	
	/**************************************************************************
	 * 	Confirm Print.
	 * 	Create Payments the first time 
	 * 	@param checks checks
	 * 	@param batch batch
	 * 	@return last Document number or 0 if nothing printed
	 */
	public static int confirmPrint (MPaySelectionCheck[] checks, MPaymentBatch batch)
	{
		int lastDocumentNo = 0;
		for (MPaySelectionCheck check : checks) {
			MPayment payment;
			//	Existing Payment
			if (check.getC_Payment_ID() != 0)
			{
				payment = new MPayment(check.getCtx(), check.getC_Payment_ID(), null);
				//	Update check number
				if (check.getPaymentRule().equals(PAYMENTRULE_Check))
				{
					payment.setCheckNo(check.getCheckNo());
					if (!payment.save())
						s_log.log(Level.SEVERE, "Payment not saved: " + payment);
				}
			}
			else	//	New Payment
			{
				payment = new MPayment(check.getCtx(), 0, null);
				payment.setAD_Org_ID(check.getAD_Org_ID());
				payment.setAD_Client_ID(check.getAD_Client_ID());
				//
				if (check.getPaymentRule().equals(PAYMENTRULE_Check)){
					payment.setBankCheck (check.getParent().getC_BankAccount_ID(), false, check.getCheckNo());
					payment.setTenderType(X_C_Payment.TENDERTYPE_Check);
				}
				else if (check.getPaymentRule().equals(PAYMENTRULE_CreditCard)){
					payment.setTenderType(X_C_Payment.TENDERTYPE_CreditCard);
				}
				else if (check.getPaymentRule().equals(PAYMENTRULE_DirectDeposit)){
					payment.setBankACH(check);
					payment.setTenderType(X_C_Payment.TENDERTYPE_DirectDeposit);
				}
				else if (check.getPaymentRule().equals(PAYMENTRULE_DirectDebit)){
					payment.setBankACH(check);
					payment.setTenderType(X_C_Payment.TENDERTYPE_DirectDebit);
				}
				else
				{
					s_log.log(Level.SEVERE, "Unsupported Payment Rule=" + check.getPaymentRule());
					continue;
				}
				payment.setTrxType(X_C_Payment.TRXTYPE_CreditPayment);
				payment.setAmount(check.getParent().getC_Currency_ID(), check.getPayAmt());
				payment.setDiscountAmt(check.getDiscountAmt());
				payment.setDateTrx(check.getParent().getPayDate());
				payment.setC_BPartner_ID(check.getC_BPartner_ID());
				//	Link to Batch
				if (batch != null)
				{
					if (batch.getC_PaymentBatch_ID() == 0)
						batch.save();	//	new
					payment.setC_PaymentBatch_ID(batch.getC_PaymentBatch_ID());
				}
				//	Link to Invoice
				MPaySelectionLine[] psls = check.getPaySelectionLines(false);
				s_log.fine("confirmPrint - " + check + " (#SelectionLines=" + psls.length + ")");
				if (check.getQty() == 1 && psls != null && psls.length == 1)
				{
					MPaySelectionLine psl = psls[0];
					s_log.fine("Map to Invoice " + psl);
					//
					payment.setC_Invoice_ID (psl.getC_Invoice_ID());
					payment.setDiscountAmt (psl.getDiscountAmt());
					payment.setWriteOffAmt(psl.getDifferenceAmt());
					BigDecimal overUnder = psl.getOpenAmt().subtract(psl.getPayAmt())
						.subtract(psl.getDiscountAmt()).subtract(psl.getDifferenceAmt());
					payment.setOverUnderAmt(overUnder);
				}
				else
					payment.setDiscountAmt(Env.ZERO);
				payment.setWriteOffAmt(Env.ZERO);
				if (!payment.save()){
					s_log.log(Level.SEVERE, "Payment not saved: " + payment);
					return lastDocumentNo;
				}
				else {
					int C_Payment_ID = payment.get_ID();
					if (C_Payment_ID < 1){
						s_log.log(Level.SEVERE, "Payment not created=" + check);
						return lastDocumentNo;
					}
					else
					{
						check.setC_Payment_ID (C_Payment_ID);
						if (!check.save ()){
							s_log.log(Level.SEVERE, "Check not saved: " + check);
							return lastDocumentNo;
						}

						//	Should start WF
						DocumentEngine.processIt(payment, DocActionConstants.ACTION_Complete);
						if (!payment.save()){
							s_log.log(Level.SEVERE, "Payment not saved after complete: " + payment);
							return lastDocumentNo;
						}
					}
				}
			}	//	new Payment

			//	Get Check Document No
			try
			{
				int no = Integer.parseInt(check.getCheckNo());
				if (lastDocumentNo < no)
					lastDocumentNo = no;
			}
			catch (NumberFormatException ex)
			{
				s_log.log(Level.SEVERE, "DocumentNo=" + check.getCheckNo(), ex);
			}
			check.setIsPrinted(true);
			check.setProcessed(true);
			if (!check.save ())
				s_log.log(Level.SEVERE, "Check not saved after documentNo update: " + check);
		}	//	all checks

		s_log.fine("Last Document No = " + lastDocumentNo);
		return lastDocumentNo;
	}	//	confirmPrint

	/** Logger								*/
	static private CLogger	s_log = CLogger.getCLogger (MPaySelectionCheck.class);

	/** BPartner Info Index for Value       */
	private static final int     BP_VALUE = 0;
	/** BPartner Info Index for Name        */
	private static final int     BP_NAME = 1;
	/** BPartner Info Index for Contact Name    */
	private static final int     BP_CONTACT = 2;
	/** BPartner Info Index for Address 1   */
	private static final int     BP_ADDR1 = 3;
	/** BPartner Info Index for Address 2   */
	private static final int     BP_ADDR2 = 4;
	/** BPartner Info Index for City        */
	private static final int     BP_CITY = 5;
	/** BPartner Info Index for Region      */
	private static final int     BP_REGION = 6;
	/** BPartner Info Index for Postal Code */
	private static final int     BP_POSTAL = 7;
	/** BPartner Info Index for Country     */
	private static final int     BP_COUNTRY = 8;
	/** BPartner Info Index for Reference No    */
	private static final int     BP_REFNO = 9;

	
	/**************************************************************************
	 *	Constructor
	 *  @param ctx context
	 *  @param C_PaySelectionCheck_ID C_PaySelectionCheck_ID
	 *	@param trx transaction
	 */
	public MPaySelectionCheck (Ctx ctx, int C_PaySelectionCheck_ID, Trx trx)
	{
		super(ctx, C_PaySelectionCheck_ID, trx);
		if (C_PaySelectionCheck_ID == 0)
		{
		//	setC_PaySelection_ID (0);
		//	setC_BPartner_ID (0);
		//	setPaymentRule (null);
			setPayAmt (Env.ZERO);
			setDiscountAmt(Env.ZERO);
			setIsPrinted (false);
			setIsReceipt (false);
			setQty (0);
		}
	}   //  MPaySelectionCheck

	/**
	 *	Load Constructor
	 *  @param ctx context
	 *  @param rs result set
	 *	@param trx transaction
	 */
	public MPaySelectionCheck(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}   //  MPaySelectionCheck

	/**
	 * 	Create from Line
	 *	@param line payment selection
	 *	@param PaymentRule payment rule
	 */
	public MPaySelectionCheck (MPaySelectionLine line, String PaymentRule)
	{
		this (line.getCtx(), 0, line.get_Trx());
		setClientOrg(line);
		setC_PaySelection_ID (line.getC_PaySelection_ID());
		int C_BPartner_ID = line.getInvoice().getC_BPartner_ID();
		setC_BPartner_ID (C_BPartner_ID);
		//
		if (X_C_Order.PAYMENTRULE_DirectDebit.equals(PaymentRule))
		{
			MBPBankAccount[] bas = MBPBankAccount.getOfBPartner (line.getCtx(), C_BPartner_ID); 
			for (MBPBankAccount account : bas) {
				if (account.isDirectDebit())
				{
					setC_BP_BankAccount_ID(account.getC_BP_BankAccount_ID());
					break;
				}
			}
		}
		else if (X_C_Order.PAYMENTRULE_DirectDeposit.equals(PaymentRule))
		{
			MBPBankAccount[] bas = MBPBankAccount.getOfBPartner (line.getCtx(), C_BPartner_ID); 
			for (MBPBankAccount account : bas) {
				if (account.isDirectDeposit())
				{
					setC_BP_BankAccount_ID(account.getC_BP_BankAccount_ID());
					break;
				}
			}
		}
		setPaymentRule (PaymentRule);
		//
		setIsReceipt(line.isSOTrx());
		setPayAmt (line.getPayAmt());
		setDiscountAmt(line.getDiscountAmt());
		setQty (1);
	}	//	MPaySelectionCheck

	/**
	 * 	Create from Pay Selection
	 *	@param ps payment selection
	 *	@param PaymentRule payment rule
	 */
	public MPaySelectionCheck (MPaySelection ps, String PaymentRule)
	{
		this (ps.getCtx(), 0, ps.get_Trx());
		setClientOrg(ps);
		setC_PaySelection_ID (ps.getC_PaySelection_ID());
		setPaymentRule (PaymentRule);
	}	//	MPaySelectionCheck
	
	
	/**	Parent					*/
	private MPaySelection			m_parent = null;
	/**	Payment Selection lines of this check	*/
	private MPaySelectionLine[]		m_lines = null;

	
	/**
	 * 	Add Payment Selection Line
	 *	@param line line
	 */
	public void addLine (MPaySelectionLine line)
	{
		if (getC_BPartner_ID() != line.getInvoice().getC_BPartner_ID())
			throw new IllegalArgumentException("Line for fifferent BPartner");
		//
		if (isReceipt() == line.isSOTrx())
		{
			setPayAmt (getPayAmt().add(line.getPayAmt()));
			setDiscountAmt(getDiscountAmt().add(line.getDiscountAmt()));
		}
		else
		{
			setPayAmt (getPayAmt().subtract(line.getPayAmt()));
			setDiscountAmt(getDiscountAmt().subtract(line.getDiscountAmt()));
		}
		setQty (getQty()+1);
	}	//	addLine
	
	/**
	 * 	Get Parent
	 *	@return parent
	 */
	public MPaySelection getParent()
	{
		if (m_parent == null)
			m_parent = new MPaySelection (getCtx(), getC_PaySelection_ID(), get_Trx());
		return m_parent;
	}	//	getParent

	/**
	 * 	Is this a valid Prepared Payment
	 *	@return true if valid
	 */
	public boolean isValid()
	{
		if (getC_BP_BankAccount_ID() != 0)
			return true;
		return !isDirect();
	}	//	isValid
	
	/**
	 * 	Is this a direct Debit or Deposit
	 *	@return true if direct
	 */
	public boolean isDirect()
	{
		return (X_C_Order.PAYMENTRULE_DirectDeposit.equals(getPaymentRule())
			|| X_C_Order.PAYMENTRULE_DirectDebit.equals(getPaymentRule()));
	}	//	isDirect
	
	/**
	 * 	String Representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MPaymentCheck[");
		sb.append(get_ID()).append("-").append(getCheckNo())
			.append("-").append(getPayAmt())
			.append(",PaymetRule=").append(getPaymentRule())
			.append(",Qty=").append(getQty())
			.append("]");
		return sb.toString();
	}	//	toString
	
	/**
	 * 	Get Payment Selection Lines of this check
	 *	@param requery requery
	 * 	@return array of peyment selection lines
	 */
	public MPaySelectionLine[] getPaySelectionLines (boolean requery)
	{
		if (m_lines != null && !requery)
			return m_lines;
		ArrayList<MPaySelectionLine> list = new ArrayList<MPaySelectionLine>();
		String sql = "SELECT * FROM C_PaySelectionLine WHERE C_PaySelectionCheck_ID=? ORDER BY Line";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getC_PaySelectionCheck_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MPaySelectionLine(getCtx(), rs, get_Trx()));
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
		m_lines = new MPaySelectionLine[list.size ()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getPaySelectionLines

	/**
	 * 	update allocations for the payment
	 *	@param C_Payment_ID
	 * 	@return boolean returns true if payselection lines were created for an allocation
	 */
	public boolean updateForPayment (int C_Payment_ID)
	{
	
		if (C_Payment_ID == 0)
			return false;
		MPayment payment = new MPayment (Env.getCtx(), C_Payment_ID, null);
		//PaySelection
		MPaySelection ps = new MPaySelection(Env.getCtx(), getC_PaySelection_ID(), get_Trx());
		//get the lines.
		MPaySelectionLine[] lines=ps.getLines(false);
		//if lines already there, do nothing.  Allocation is done only once.
		if(lines.length!=0)
			return false;

		//	Create new PaySelection Line
		MAllocationHdr[] hdrs=MAllocationHdr.getOfPayment(Env.getCtx(), C_Payment_ID, null);
		int counter=0;
		for (MAllocationHdr hdr : hdrs) {
			MAllocationLine[] alls=hdr.getLines(false);
			for (MAllocationLine all : alls) {
				if(all.getC_Invoice_ID()!=0)
				{
					MPaySelectionLine psl = new MPaySelectionLine (ps, ++counter, getPaymentRule());
					BigDecimal one = new BigDecimal(1);
					if(!payment.isReceipt())
						one=one.negate();
					BigDecimal openAmt=all.getAmount().add(all.getDiscountAmt()).add(all.getWriteOffAmt()).add(all.getOverUnderAmt());
					psl.setInvoice(all.getC_Invoice_ID(), isReceipt(), openAmt.multiply(one), 
							all.getAmount().multiply(one), all.getDiscountAmt().multiply(one));
					psl.setC_PaySelectionCheck_ID(getC_PaySelectionCheck_ID());
					psl.setProcessed(true);
					psl.save();
				}
			}
		}
		return true;
	}	//	updateForPayment
	
}   //  MPaySelectionCheck
