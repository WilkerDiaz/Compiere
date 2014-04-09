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
import java.rmi.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.api.*;
import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.db.*;
import org.compiere.framework.*;
import org.compiere.interfaces.*;
import org.compiere.process.*;
import org.compiere.util.*;
import org.compiere.util.Env.*;
import org.compiere.vos.*;

/**
 *  Payment Model.
 *  - retrieve and create payments for invoice
 *  <pre>
 *  Event chain
 *  - Payment inserted
 *      C_Payment_Trg fires
 *          update DocumentNo with payment summary
 *  - Payment posted (C_Payment_Post)
 *      create allocation line
 *          C_Allocation_Trg fires
 *              Update C_BPartner Open Item Amount
 *      update invoice (IsPaid)
 *      link invoice-payment if batch
 *
 *  Lifeline:
 *  -   Created by VPayment or directly
 *  -   When changed in VPayment
 *      - old payment is reversed
 *      - new payment created
 *
 *  When Payment is posed, the Allocation is made
 *  </pre>
 *  @author 	Jorg Janke
 *  @version 	$Id: MPayment.java,v 1.4 2006/10/02 05:18:39 jjanke Exp $
 */
public class MPayment extends X_C_Payment
	implements DocAction, ProcessCall
{
    /** Logger for class MPayment */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPayment.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private int I_Payment_ID=0;
	/**
	 * 	Get Payments Of BPartner
	 *	@param ctx context
	 *	@param C_BPartner_ID id
	 *	@param trx transaction
	 *	@return array
	 */
	public static MPayment[] getOfBPartner (Ctx ctx, int C_BPartner_ID, Trx trx)
	{
		ArrayList<MPayment> list = new ArrayList<MPayment>();
		String sql = "SELECT * FROM C_Payment WHERE C_BPartner_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt(1, C_BPartner_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MPayment(ctx,rs, trx));
		
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


		//
		MPayment[] retValue = new MPayment[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getOfBPartner


	/**************************************************************************
	 *  Default Constructor
	 *  @param ctx context
	 *  @param  C_Payment_ID    payment to load, (0 create new payment)
	 *  @param trx p_trx name
	 */
	public MPayment (Ctx ctx, int C_Payment_ID, Trx trx)
	{
		super (ctx, C_Payment_ID, trx);
		//  New
		if (C_Payment_ID == 0)
		{
			setDocAction(DOCACTION_Complete);
			setDocStatus(DOCSTATUS_Drafted);
			setTrxType(TRXTYPE_Sales);
			//
			setR_AvsAddr (R_AVSZIP_Unavailable);
			setR_AvsZip (R_AVSZIP_Unavailable);
			//
			setIsReceipt (true);
			setIsApproved (false);
			setIsReconciled (false);
			setIsAllocated(false);
			setIsOnline (false);
			setIsSelfService(false);
			setIsDelayedCapture (false);
			setIsPrepayment(false);
			setProcessed(false);
			setProcessing(false);
			setPosted (false);
			//
			setPayAmt(Env.ZERO);
			setDiscountAmt(Env.ZERO);
			setTaxAmt(Env.ZERO);
			setWriteOffAmt(Env.ZERO);
			setIsOverUnderPayment (false);
			setOverUnderAmt(Env.ZERO);
			//
			setDateTrx (new Timestamp(System.currentTimeMillis()));
			setDateAcct (getDateTrx());
			setTenderType(TENDERTYPE_Check);
		}
	}   //  MPayment

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *	@param trx transaction
	 */
	public MPayment (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MPayment

	/**
	 * 	Get Payment using document no
	 * 	@param ctx context
	 *	@param documentNo document number
	 *	@param trx transaction
	 *	@return payment
	 */
	public static MPayment getPayment(Ctx ctx, String documentNo, Trx trx)
	{
		MPayment payment = null;
		String sql = "SELECT * FROM C_Payment WHERE DocumentNo = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setString (1, documentNo);
			 rs = pstmt.executeQuery ();
			while (rs.next ())
				payment = new MPayment (ctx, rs, trx);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return payment;
	}	//	MPayment

	
	public MPayment (X_I_Payment imp)
	{
		this (imp.getCtx(), 0, imp.get_Trx());
	    PO.copyValues(imp, this, imp.getAD_Client_ID(), imp.getAD_Org_ID());
		I_Payment_ID = imp.getI_Payment_ID();
		
		if (imp.getTrxType()==null)
			setTrxType(TRXTYPE_Sales);
		if (imp.getDateTrx()==null)
			setDateTrx (new Timestamp(System.currentTimeMillis()));
		if (imp.getDateAcct()==null)
			setDateAcct (getDateTrx());
		if (imp.getTenderType()==null)
			setTenderType(TENDERTYPE_Check);
				
		setC_BankAccount_ID(imp.getC_BankAccount_ID());
		setCreditCardNumber(imp.getCreditCardNumber());
		setCreditCardExpMM (imp.getCreditCardExpMM());
		if (imp.getCreditCardExpYY()!=0)
			setCreditCardExpYY(imp.getCreditCardExpYY());
		setMicr(imp.getMicr());
		setRoutingNo(imp.getRoutingNo());
		setAccountNo(imp.getAccountNo());
		setCheckNo(imp.getCheckNo());
		setR_PnRef(imp.getR_PnRef());
		setPayAmt(imp.getPayAmt());
		setDiscountAmt(imp.getDiscountAmt());
		setWriteOffAmt(imp.getWriteOffAmt());
		setOverUnderAmt(imp.getOverUnderAmt());
		setTaxAmt(imp.getTaxAmt());


	}	//	MPayment


	public int getI_Payment_ID() {
		return I_Payment_ID;
	}

	/**	Temporary	Payment Processors		*/
	private MPaymentProcessor[]	m_mPaymentProcessors = null;
	/**	Temporary	Payment Processor		*/
	private MPaymentProcessor	m_mPaymentProcessor = null;
	/** VVC not stored						*/
	private String				m_CreditCardVV = null;
	/** Logger								*/
	private static CLogger		s_log = CLogger.getCLogger (MPayment.class);
	/** Error Message						*/
	private String				m_errorMessage = null;

	/** Reversal Indicator			*/
	public static final String	REVERSE_INDICATOR = "^";

	/**
	 *  Reset Payment to new status
	 */
	public void resetNew()
	{
		set_ValueNoCheck("C_Payment_ID",0);		//	forces new Record
		set_ValueNoCheck ("DocumentNo", null);
		setDocAction(DOCACTION_Prepare);
		setDocStatus(DOCSTATUS_Drafted);
		setProcessed(false);
		setPosted (false);
		setIsReconciled (false);
		setIsAllocated(false);
		setIsOnline(false);
		setIsDelayedCapture (false);
	//	setC_BPartner_ID(0);
		setC_Invoice_ID(0);
		setC_Order_ID(0);
		setC_Charge_ID(0);
		setC_Project_ID(0);
		setIsPrepayment(false);
	}	//	resetNew

	/**
	 * 	Is Cashbook Transfer Trx
	 *	@return true if Cash Trx
	 */
	public boolean isCashTrx()
	{
		return "X".equals(getTenderType());
	}	//	isCashTrx

	/**************************************************************************
	 *  Set Credit Card.
	 *  Need to set PatmentProcessor after Amount/Currency Set
	 *
	 *  @param TrxType Transaction Type see TRX_
	 *  @param creditCardType CC type
	 *  @param creditCardNumber CC number
	 *  @param creditCardVV CC verification
	 *  @param creditCardExpMM CC Exp MM
	 *  @param creditCardExpYY CC Exp YY
	 *  @return true if valid
	 */
	public boolean setCreditCard (String TrxType, String creditCardType, String creditCardNumber,
		String creditCardVV, int creditCardExpMM, int creditCardExpYY)
	{
		setTenderType(TENDERTYPE_CreditCard);
		setTrxType(TrxType);
		//
		setCreditCardType (creditCardType);
		setCreditCardNumber (creditCardNumber);
		setCreditCardVV (creditCardVV);
		setCreditCardExpMM (creditCardExpMM);
		setCreditCardExpYY (creditCardExpYY);
		//
		int check = MPaymentValidate.validateCreditCardNumber(creditCardNumber, creditCardType).length()
			+ MPaymentValidate.validateCreditCardExp(creditCardExpMM, creditCardExpYY).length();
		if (creditCardVV.length() > 0)
			check += MPaymentValidate.validateCreditCardVV(creditCardVV, creditCardType).length();
		return check == 0;
	}   //  setCreditCard

	/**
	 *  Set Credit Card - Exp.
	 *  Need to set PatmentProcessor after Amount/Currency Set
	 *
	 *  @param TrxType Transaction Type see TRX_
	 *  @param creditCardType CC type
	 *  @param creditCardNumber CC number
	 *  @param creditCardVV CC verification
	 *  @param creditCardExp CC Exp
	 *  @return true if valid
	 */
	public boolean setCreditCard (String TrxType, String creditCardType, String creditCardNumber,
		String creditCardVV, String creditCardExp)
	{
		return setCreditCard(TrxType, creditCardType, creditCardNumber,
			creditCardVV, MPaymentValidate.getCreditCardExpMM(creditCardExp),
			MPaymentValidate.getCreditCardExpYY(creditCardExp));
	}   //  setCreditCard

	/**
	 *  Set ACH BankAccount Info
	 *  @param preparedPayment pay selection check
	 *  @return true if valid
	 */
	public boolean setBankACH (MPaySelectionCheck preparedPayment)
	{
		//	Our Bank
		setC_BankAccount_ID(preparedPayment.getParent().getC_BankAccount_ID());
		//	Target Bank
		int C_BP_BankAccount_ID = preparedPayment.getC_BP_BankAccount_ID();
		MBPBankAccount ba = new MBPBankAccount (preparedPayment.getCtx(), C_BP_BankAccount_ID, null);
		setRoutingNo(ba.getRoutingNo());
		setAccountNo(ba.getAccountNo());
		setIsReceipt (X_C_Order.PAYMENTRULE_DirectDebit.equals	//	AR only
				(preparedPayment.getPaymentRule()));
		//
		int check = MPaymentValidate.validateRoutingNo(getRoutingNo()).length()
			+ MPaymentValidate.validateAccountNo(getAccountNo()).length();
		return check == 0;
	}	//	setBankACH

	/**
	 *  Set ACH BankAccount Info
	 *
	 *  @param C_BankAccount_ID bank account
	 *  @param isReceipt true if receipt
	 * 	@param tenderType - Direct Debit or Direct Deposit
	 *  @param routingNo routing
	 *  @param accountNo account
	 *  @return true if valid
	 */
	public boolean setBankACH (int C_BankAccount_ID, boolean isReceipt, String tenderType,
		String routingNo, String accountNo)
	{
		setTenderType (tenderType);
		setIsReceipt (isReceipt);
		//
		if ((C_BankAccount_ID > 0)
			&& ((routingNo == null) || (routingNo.length() == 0) || (accountNo == null) || (accountNo.length() == 0)))
			setBankAccountDetails(C_BankAccount_ID);
		else
		{
			setC_BankAccount_ID(C_BankAccount_ID);
			setRoutingNo (routingNo);
			setAccountNo (accountNo);
		}
		setCheckNo ("");
		//
		int check = MPaymentValidate.validateRoutingNo(routingNo).length()
			+ MPaymentValidate.validateAccountNo(accountNo).length();
		return check == 0;
	}   //  setBankACH

	/**
	 *  Set Check BankAccount Info
	 *
	 *  @param C_BankAccount_ID bank account
	 *  @param isReceipt true if receipt
	 *  @param checkNo chack no
	 *  @return true if valid
	 */
	public boolean setBankCheck (int C_BankAccount_ID, boolean isReceipt, String checkNo)
	{
		return setBankCheck (C_BankAccount_ID, isReceipt, null, null, checkNo);
	}	//	setBankCheck

	/**
	 *  Set Check BankAccount Info
	 *
	 *  @param C_BankAccount_ID bank account
	 *  @param isReceipt true if receipt
	 *  @param routingNo routing no
	 *  @param accountNo account no
	 *  @param checkNo chack no
	 *  @return true if valid
	 */
	public boolean setBankCheck (int C_BankAccount_ID, boolean isReceipt,
		String routingNo, String accountNo, String checkNo)
	{
		setTenderType (TENDERTYPE_Check);
		setIsReceipt (isReceipt);
		//
		if ((C_BankAccount_ID > 0)
			&& ((routingNo == null) || (routingNo.length() == 0)
				|| (accountNo == null) || (accountNo.length() == 0)))
			setBankAccountDetails(C_BankAccount_ID);
		else
		{
			setC_BankAccount_ID(C_BankAccount_ID);
			setRoutingNo (routingNo);
			setAccountNo (accountNo);
		}
		setCheckNo (checkNo);
		//
		int check = MPaymentValidate.validateRoutingNo(routingNo).length()
			+ MPaymentValidate.validateAccountNo(accountNo).length()
			+ MPaymentValidate.validateCheckNo(checkNo).length();
		return check == 0;       //  no error message
	}   //  setBankCheck

	/**
	 * 	Set Bank Account Details.
	 * 	Look up Routing No & Bank Acct No
	 * 	@param C_BankAccount_ID bank account
	 */
	public void setBankAccountDetails (int C_BankAccount_ID)
	{
		if (C_BankAccount_ID == 0)
			return;
		setC_BankAccount_ID(C_BankAccount_ID);
		//
		String sql = "SELECT b.RoutingNo, ba.AccountNo "
			+ "FROM C_BankAccount ba"
			+ " INNER JOIN C_Bank b ON (ba.C_Bank_ID=b.C_Bank_ID) "
			+ "WHERE C_BankAccount_ID=?";
		PreparedStatement pstmt=null;
		ResultSet rs =null;
		try
		{
			 pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, C_BankAccount_ID);
			 rs = pstmt.executeQuery();
			if (rs.next())
			{
				setRoutingNo (rs.getString(1));
				setAccountNo (rs.getString(2));
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
	}	//	setBankAccountDetails

	/**
	 *  Set Account Address
	 *
	 *  @param name name
	 *  @param street street
	 *  @param city city
	 *  @param state state
	 *  @param zip zip
	 * 	@param country country
	 */
	public void setAccountAddress (String name, String street,
		String city, String state, String zip, String country)
	{
		setA_Name (name);
		setA_Street (street);
		setA_City (city);
		setA_State (state);
		setA_Zip (zip);
		setA_Country(country);
	}   //  setAccountAddress


	/**************************************************************************
	 *  Process Payment
	 *  @return true if approved
	 */
	public boolean processOnline()
	{
		log.info ("Amt=" + getPayAmt());
		//
		setIsOnline(true);
		setErrorMessage(null);
		//	prevent charging twice
		if (isApproved())
		{
			log.info("Already processed - " + getR_Result() + " - " + getR_RespMsg());
			setErrorMessage("Payment already Processed");
			return true;
		}

		if (m_mPaymentProcessor == null)
			setPaymentProcessor();
		if (m_mPaymentProcessor == null)
		{
			log.log(Level.WARNING, "No Payment Processor Model");
			setErrorMessage("No Payment Processor Model");
			return false;
		}

		boolean approved = false;
		/**	Process Payment on Server	*/
		if (DB.isRemoteObjects())
		{
			Server server = CConnection.get().getServer();
			try
			{
				if (server != null)
				{	//	See ServerBean
					Trx trx = null;	//	unconditionally save
					save(trx);	//	server reads from disk
					approved = server.paymentOnline (getCtx(), getC_Payment_ID(),
						m_mPaymentProcessor.getC_PaymentProcessor_ID(), null);
					if (CLogMgt.isLevelFinest())
						s_log.fine("server => " + approved);
					load(trx);	//	server saves to disk
					setIsApproved(approved);
					return approved;
				}
				log.log(Level.WARNING, "AppsServer not found");
			}
			catch (RemoteException ex)
			{
				log.log(Level.SEVERE, "AppsServer error", ex);
			}
		}
		/** **/

		//	Try locally
		try
		{
			PaymentProcessor pp = PaymentProcessor.create(m_mPaymentProcessor, this);
			if (pp == null)
				setErrorMessage("No Payment Processor");
			else
			{
				approved = pp.processCC ();
				if (approved)
					setErrorMessage(null);
				else
					setErrorMessage("From " +  getCreditCardName() + ": " + getR_RespMsg());
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "processOnline", e);
			setErrorMessage("Payment Processor Error");
		}
		setIsApproved(approved);
		return approved;
	}   //  processOnline

	/**
	 *  Process Online Payment.
	 *  implements ProcessCall after standard constructor
	 *  Called when pressing the Process_Online button in C_Payment
	 *
	 *  @param ctx Context
	 *  @param pi Process Info
	 *  @param p_trx transaction
	 *  @return true if the next process should be performed
	 */
	public boolean startProcess (Ctx ctx, ProcessInfo pi, Trx p_trx)
	{
		log.info("startProcess - " + pi.getRecord_ID());
		boolean retValue = false;
		//
		if (pi.getRecord_ID() != get_ID())
		{
			log.log(Level.SEVERE, "startProcess - Not same Payment - " + pi.getRecord_ID());
			return false;
		}
		//  Process it
		retValue = processOnline();
		save();
		return retValue;    //  Payment processed
	}   //  startProcess


	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return save
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		
		//	We have a charge
		if (getC_Charge_ID() != 0)
		{
			if (newRecord || is_ValueChanged("C_Charge_ID"))
			{
				setC_Order_ID(0);
				setC_Invoice_ID(0);
				setWriteOffAmt(Env.ZERO);
			 	setIsOverUnderPayment(false);
				setOverUnderAmt(Env.ZERO);
				setIsPrepayment(false);
			}
		}
		//	We need a BPartner
		else if ((getC_BPartner_ID() == 0) && !isCashTrx())
		{
			if (getC_Invoice_ID() != 0)
				;
			else if (getC_Order_ID() != 0)
				;
			else
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), "@NotFound@: @C_BPartner_ID@"));
				return false;
			}
		}
		//	Prepayment: No charge and order or project (not as acct dimension)
		if (newRecord
			|| is_ValueChanged("C_Charge_ID") || is_ValueChanged("C_Invoice_ID")
			|| is_ValueChanged("C_Order_ID") || is_ValueChanged("C_Project_ID"))
			setIsPrepayment ((getC_Charge_ID() == 0)
				&& (getC_BPartner_ID() != 0)
				&& ((getC_Order_ID() != 0)
					|| ((getC_Project_ID() != 0) && (getC_Invoice_ID() == 0))));
		if (isPrepayment())
		{
			if (newRecord
				|| is_ValueChanged("C_Order_ID") || is_ValueChanged("C_Project_ID"))
			{
				setWriteOffAmt(Env.ZERO);
				setDiscountAmt(Env.ZERO);
				setIsOverUnderPayment(false);
				setOverUnderAmt(Env.ZERO);
			}
		}

		//	Document Type/Receipt
		if (getC_DocType_ID() == 0)
			setC_DocType_ID();
		else
		{
			MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
			setIsReceipt(dt.isSOTrx());
		}
		if(newRecord ||!(TENDERTYPE_Check.equals(getTenderType())
				&& !isReceipt()))
			setDocumentNo();
		//
		if (getDateAcct() == null)
			setDateAcct(getDateTrx());
		//
		if (!isOverUnderPayment())
			setOverUnderAmt(Env.ZERO);

		//	Organization
		if ((newRecord || is_ValueChanged("C_BankAccount_ID"))
			&& (getC_Charge_ID() == 0))	//	allow different org for charge
		{
			MBankAccount ba = MBankAccount.get(getCtx(), getC_BankAccount_ID());
			if (ba.getAD_Org_ID() != 0)
				setAD_Org_ID(ba.getAD_Org_ID());
		}

		setIsOverUnderPayment(getOverUnderAmt().compareTo(Env.ZERO) != 0);
		return true;
	}	//	beforeSave

	/**
	 * 	Get Allocated Amt in Payment Currency
	 *	@return amount or null
	 */
	public BigDecimal getAllocatedAmt ()
	{
		BigDecimal retValue = null;
		if (getC_Charge_ID() != 0)
			return getPayAmt(true);
		//
		String sql = "SELECT SUM(currencyConvert(al.Amount,"
				+ "ah.C_Currency_ID, p.C_Currency_ID,ah.DateTrx,p.C_ConversionType_ID, al.AD_Client_ID,al.AD_Org_ID)) "
			+ "FROM C_AllocationLine al"
			+ " INNER JOIN C_AllocationHdr ah ON (al.C_AllocationHdr_ID=ah.C_AllocationHdr_ID) "
			+ " INNER JOIN C_Payment p ON (al.C_Payment_ID=p.C_Payment_ID) "
			+ "WHERE al.C_Payment_ID=?"
			+ " AND ah.IsActive='Y' AND al.IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_Payment_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = rs.getBigDecimal(1);
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

	//	log.fine("getAllocatedAmt - " + retValue);
		//	? ROUND(NVL(v_AllocatedAmt,0), 2);
		return retValue;
	}	//	getAllocatedAmt

	/**
	 * 	Test Allocation (and set allocated flag)
	 *	@return true if updated
	 */
	public boolean testAllocation()
	{
		//	Cash Trx always allocated
		if (isCashTrx())
		{
			if (!isAllocated())
			{
				setIsAllocated(true);
				return true;
			}
			return false;
		}
		//
		BigDecimal alloc = getAllocatedAmt();
		if (alloc == null)
			alloc = Env.ZERO;
		BigDecimal total = getPayAmt();

		if (!isReceipt())
			total = total.negate();
		boolean test = total.compareTo(alloc) == 0;
		boolean change = test != isAllocated();
		if (change)
			setIsAllocated(test);
		log.fine("Allocated=" + test
			+ " (" + alloc + "=" + total + ")");
		return change;
	}	//	testAllocation

	/**
	 * 	Set Allocated Flag for payments
	 * 	@param ctx context
	 *	@param C_BPartner_ID if 0 all
	 *	@param trx p_trx
	 */
	public static void setIsAllocated (Ctx ctx, int C_BPartner_ID, Trx trx)
	{
		int counter = 0;
		String sql = "SELECT * FROM C_Payment "
			+ "WHERE IsAllocated='N' AND DocStatus IN ('CO','CL')";
		if (C_BPartner_ID > 1)
			sql += " AND C_BPartner_ID=?";
		else
			sql += " AND AD_Client_ID= ? " ;
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			if (C_BPartner_ID > 1)
				pstmt.setInt (1, C_BPartner_ID);
			else
				pstmt.setInt(1, ctx.getAD_Client_ID());
			 rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MPayment pay = new MPayment (ctx, rs, trx);
				if (pay.testAllocation())
					if (pay.save())
						counter++;
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

		s_log.config("#" + counter);
	}	//	setIsAllocated

	/**************************************************************************
	 * 	Set Error Message
	 *	@param errorMessage error message
	 */
	public void setErrorMessage(String errorMessage)
	{
		m_errorMessage = errorMessage;
	}	//	setErrorMessage

	/**
	 * 	Get Error Message
	 *	@return error message
	 */
	public String getErrorMessage()
	{
		return m_errorMessage;
	}	//	getErrorMessage


	/**
	 *  Set Bank Account for Payment.
	 *  @param C_BankAccount_ID C_BankAccount_ID
	 */
	@Override
	public void setC_BankAccount_ID (int C_BankAccount_ID)
	{
		if (C_BankAccount_ID == 0)
		{
			setPaymentProcessor();
			if (getC_BankAccount_ID() == 0)
				throw new IllegalArgumentException("Can't find Bank Account");
		}
		else
			super.setC_BankAccount_ID(C_BankAccount_ID);
	}	//	setC_BankAccount_ID

	/**
	 *  Set BankAccount and PaymentProcessor
	 *  @return true if found
	 */
	public boolean setPaymentProcessor ()
	{
		return setPaymentProcessor (getTenderType(), getCreditCardType());
	}	//	setPaymentProcessor

	/**
	 *  Set BankAccount and PaymentProcessor
	 *  @param tender TenderType see TENDER_
	 *  @param CCType CC Type see CC_
	 *  @return true if found
	 */
	public boolean setPaymentProcessor (String tender, String CCType)
	{
		m_mPaymentProcessor = null;
		//	Get Processor List
		if ((m_mPaymentProcessors == null) || (m_mPaymentProcessors.length == 0))
			m_mPaymentProcessors = MPaymentProcessor.find (getCtx(), tender, CCType, getAD_Client_ID(),
				getC_Currency_ID(), getPayAmt(), get_Trx());
		//	Relax Amount
		if ((m_mPaymentProcessors == null) || (m_mPaymentProcessors.length == 0))
			m_mPaymentProcessors = MPaymentProcessor.find (getCtx(), tender, CCType, getAD_Client_ID(),
				getC_Currency_ID(), Env.ZERO, get_Trx());
		if ((m_mPaymentProcessors == null) || (m_mPaymentProcessors.length == 0))
			return false;

		//	Find the first right one
		for (MPaymentProcessor pp : m_mPaymentProcessors)
		{
			if (pp.accepts (tender, CCType))
			{
				m_mPaymentProcessor = pp;
				break;
			}
		}
		if (m_mPaymentProcessor != null)
			setC_BankAccount_ID (m_mPaymentProcessor.getC_BankAccount_ID());
		//
		return m_mPaymentProcessor != null;
	}   //  setPaymentProcessor


	/**
	 * 	Get Accepted Credit Cards for PayAmt (default 0)
	 *	@return credit cards
	 */
	public ValueNamePair[] getCreditCards ()
	{
		return getCreditCards(getPayAmt());
	}	//	getCreditCards


	/**
	 * 	Get Accepted Credit Cards for amount
	 *	@param amt p_trx amount
	 *	@return credit cards
	 */
	public ValueNamePair[] getCreditCards (BigDecimal amt)
	{
		try
		{
			if ((m_mPaymentProcessors == null) || (m_mPaymentProcessors.length == 0))
				m_mPaymentProcessors = MPaymentProcessor.find (getCtx (), null, null,
					getAD_Client_ID (), getC_Currency_ID (), amt, get_Trx());
			//
			HashMap<String,ValueNamePair> map = new HashMap<String,ValueNamePair>(); //	to eliminate duplicates
			for (MPaymentProcessor pp : m_mPaymentProcessors)
			{
				if (pp.isAcceptAMEX ())
					map.put (CREDITCARDTYPE_Amex, getCreditCardPair (CREDITCARDTYPE_Amex));
				if (pp.isAcceptDiners ())
					map.put (CREDITCARDTYPE_Diners, getCreditCardPair (CREDITCARDTYPE_Diners));
				if (pp.isAcceptDiscover ())
					map.put (CREDITCARDTYPE_Discover, getCreditCardPair (CREDITCARDTYPE_Discover));
				if (pp.isAcceptMC ())
					map.put (CREDITCARDTYPE_MasterCard, getCreditCardPair (CREDITCARDTYPE_MasterCard));
				if (pp.isAcceptCorporate ())
					map.put (CREDITCARDTYPE_PurchaseCard, getCreditCardPair (CREDITCARDTYPE_PurchaseCard));
				if (pp.isAcceptVisa ())
					map.put (CREDITCARDTYPE_Visa, getCreditCardPair (CREDITCARDTYPE_Visa));
			} //	for all payment processors
			//
			ValueNamePair[] retValue = new ValueNamePair[map.size ()];
			map.values ().toArray (retValue);
			log.fine("#" + retValue.length + " - Processors=" + m_mPaymentProcessors.length);
			return retValue;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}	//	getCreditCards

	/**
	 * 	Get Type and name pair
	 *	@param CreditCardType credit card Type
	 *	@return pair
	 */
	private ValueNamePair getCreditCardPair (String CreditCardType)
	{
		return new ValueNamePair (CreditCardType, getCreditCardName(CreditCardType));
	}	//	getCreditCardPair


	/**************************************************************************
	 *  Credit Card Number
	 *  @param CreditCardNumber CreditCard Number
	 */
	@Override
	public void setCreditCardNumber (String CreditCardNumber)
	{
		super.setCreditCardNumber (MPaymentValidate.checkNumeric(CreditCardNumber));
	}	//	setCreditCardNumber

	/**
	 *  Verification Code
	 *  @param newCreditCardVV CC verification
	 */
	public void setCreditCardVV(String newCreditCardVV)
	{
		m_CreditCardVV = MPaymentValidate.checkNumeric(newCreditCardVV);
	}	//	setCreditCardVV

	/**
	 *  Verification Code
	 *  @return CC verification
	 */
	public String getCreditCardVV()
	{
		return m_CreditCardVV;
	}	//	getCreditCardVV

	/**
	 *  Two Digit CreditCard MM
	 *  @param CreditCardExpMM Exp month
	 */
	@Override
	public void setCreditCardExpMM (int CreditCardExpMM)
	{
		if ((CreditCardExpMM < 1) || (CreditCardExpMM > 12))
			;
		else
			super.setCreditCardExpMM (CreditCardExpMM);
	}	//	setCreditCardExpMM

	/**
	 *  Two digit CreditCard YY (til 2020)
	 *  @param newCreditCardExpYY 2 or 4 digit year
	 */
	@Override
	public void setCreditCardExpYY (int newCreditCardExpYY)
	{
		int CreditCardExpYY = newCreditCardExpYY;
		if (newCreditCardExpYY > 1999)
			CreditCardExpYY = newCreditCardExpYY-2000;
		super.setCreditCardExpYY(CreditCardExpYY);
	}	//	setCreditCardExpYY

	/**
	 *  CreditCard Exp  MMYY
	 *  @param mmyy Exp in form of mmyy
	 *  @return true if valid
	 */
	public boolean setCreditCardExp (String mmyy)
	{
		if (MPaymentValidate.validateCreditCardExp(mmyy).length() != 0)
			return false;
		//
		String exp = MPaymentValidate.checkNumeric(mmyy);
		String mmStr = exp.substring(0,2);
		String yyStr = exp.substring(2,4);
		setCreditCardExpMM (Integer.parseInt(mmStr));
		setCreditCardExpYY (Integer.parseInt(yyStr));
		return true;
	}   //  setCreditCardExp


	/**
	 *  CreditCard Exp  MMYY
	 *  @param delimiter / - or null
	 *  @return Exp
	 */
	public String getCreditCardExp(String delimiter)
	{
		String mm = String.valueOf(getCreditCardExpMM());
		String yy = String.valueOf(getCreditCardExpYY());

		StringBuffer retValue = new StringBuffer();
		if (mm.length() == 1)
			retValue.append("0");
		retValue.append(mm);
		//
		if (delimiter != null)
			retValue.append(delimiter);
		//
		if (yy.length() == 1)
			retValue.append("0");
		retValue.append(yy);
		//
		return (retValue.toString());
	}   //  getCreditCardExp

	/**
	 *  MICR
	 *  @param MICR MICR
	 */
	@Override
	public void setMicr (String MICR)
	{
		super.setMicr (MPaymentValidate.checkNumeric(MICR));
	}	//	setBankMICR

	/**
	 *  Routing No
	 *  @param RoutingNo Routing No
	 */
	@Override
	public void setRoutingNo(String RoutingNo)
	{
		super.setRoutingNo (MPaymentValidate.checkNumeric(RoutingNo));
	}	//	setBankRoutingNo


	/**
	 *  Bank Account No
	 *  @param AccountNo AccountNo
	 */
	@Override
	public void setAccountNo (String AccountNo)
	{
		super.setAccountNo (MPaymentValidate.checkNumeric(AccountNo));
	}	//	setBankAccountNo


	/**
	 *  Check No
	 *  @param CheckNo Check No
	 */
	@Override
	public void setCheckNo(String CheckNo)
	{
		super.setCheckNo(MPaymentValidate.checkNumeric(CheckNo));
	}	//	setBankCheckNo


	/**
	 *  Set DocumentNo to Payment info.
	 * 	If there is a R_PnRef that is set automatically
	 */
	private void setDocumentNo()
	{
		//	Cash Transfer
		if ("X".equals(getTenderType()))
			return;
		//	Current Document No
		String documentNo = getDocumentNo();
		//	Existing reversal
		if ((documentNo != null)
			&& (documentNo.indexOf(REVERSE_INDICATOR) >= 0))
			return;

		//	If external number exists - enforce it
		if (!Util.isEmpty(getR_PnRef()))
		{
			if (!getR_PnRef().equals(documentNo))
				setDocumentNo(getR_PnRef());
			return;
		}

		documentNo = "";
		//	Credit Card
		if (TENDERTYPE_CreditCard.equals(getTenderType())
			&& isReceipt())
		{
			documentNo = getCreditCardType()
				+ " " + Obscure.obscure(getCreditCardNumber())
				+ " " + getCreditCardExpMM()
				+ "/" + getCreditCardExpYY();
		}
		//	Own Check No
		else if (TENDERTYPE_Check.equals(getTenderType())
			&& !isReceipt()
			&& !Util.isEmpty(getCheckNo()))
		{
			documentNo = getCheckNo();
		}
		//	Customer Check: Routing: Account #Check
		else if (TENDERTYPE_Check.equals(getTenderType())
			&& isReceipt())
		{
			if (getRoutingNo() != null)
				documentNo = getRoutingNo() + ": ";
			if (getAccountNo() != null)
				documentNo += getAccountNo();
			if (getCheckNo() != null)
			{
				if (documentNo.length() > 0)
					documentNo += " ";
				documentNo += "#" + getCheckNo();
			}
		}
		//	TENDERTYPE_DirectDeposit

		//	Set Document No
		documentNo = documentNo.trim();
		if (documentNo.length() > 0)
			setDocumentNo(documentNo);
	}	//	setDocumentNo

	/**
	 * 	Set Reference No (and Document No)
	 *	@param R_PnRef reference
	 */
	@Override
	public void setR_PnRef (String R_PnRef)
	{
		super.setR_PnRef (R_PnRef);
		if (R_PnRef != null)
			setDocumentNo (R_PnRef);
	}	//	setR_PnRef

	//	---------------

	/**
	 *  Set Payment Amount
	 *  @param PayAmt Pay Amt
	 */
	@Override
	public void setPayAmt (BigDecimal PayAmt)
	{
		super.setPayAmt(PayAmt == null ? Env.ZERO : PayAmt);
	}	//	setPayAmt

	/**
	 *  Set Payment Amount
	 *
	 * @param C_Currency_ID currency
	 * @param payAmt amount
	 */
	public void setAmount (int C_Currency_ID, BigDecimal payAmt)
	{
		if (C_Currency_ID == 0)
			C_Currency_ID = MClient.get(getCtx()).getC_Currency_ID();
		setC_Currency_ID(C_Currency_ID);
		setPayAmt(payAmt);
	}   //  setAmount

	/**
	 *  Discount Amt
	 *  @param DiscountAmt Discount
	 */
	@Override
	public void setDiscountAmt (BigDecimal DiscountAmt)
	{
		super.setDiscountAmt (DiscountAmt == null ? Env.ZERO : DiscountAmt);
	}	//	setDiscountAmt

	/**
	 *  WriteOff Amt
	 *  @param WriteOffAmt WriteOff
	 */
	@Override
	public void setWriteOffAmt (BigDecimal WriteOffAmt)
	{
		super.setWriteOffAmt (WriteOffAmt == null ? Env.ZERO : WriteOffAmt);
	}	//	setWriteOffAmt

	/**
	 *  OverUnder Amt
	 *  @param OverUnderAmt OverUnder
	 */
	@Override
	public void setOverUnderAmt (BigDecimal OverUnderAmt)
	{
		super.setOverUnderAmt (OverUnderAmt == null ? Env.ZERO : OverUnderAmt);
		//setIsOverUnderPayment(getOverUnderAmt().compareTo(Env.ZERO) != 0);
	}	//	setOverUnderAmt

	/**
	 *  Tax Amt
	 *  @param TaxAmt Tax
	 */
	@Override
	public void setTaxAmt (BigDecimal TaxAmt)
	{
		super.setTaxAmt (TaxAmt == null ? Env.ZERO : TaxAmt);
	}	//	setTaxAmt

	/**
	 * 	Set Info from BP Bank Account
	 *	@param ba BP bank account
	 */
	public void setBP_BankAccount (MBPBankAccount ba)
	{
		log.fine("" + ba);
		if (ba == null)
			return;
		setC_BPartner_ID(ba.getC_BPartner_ID());
		setAccountAddress(ba.getA_Name(), ba.getA_Street(), ba.getA_City(),
			ba.getA_State(), ba.getA_Zip(), ba.getA_Country());
		setA_EMail(ba.getA_EMail());
		setA_Ident_DL(ba.getA_Ident_DL());
		setA_Ident_SSN(ba.getA_Ident_SSN());
		//	CC
		if (ba.getCreditCardType() != null)
			setCreditCardType(ba.getCreditCardType());
		if (ba.getCreditCardNumber() != null)
			setCreditCardNumber(ba.getCreditCardNumber());
		if (ba.getCreditCardExpMM() != 0)
			setCreditCardExpMM(ba.getCreditCardExpMM());
		if (ba.getCreditCardExpYY() != 0)
			setCreditCardExpYY(ba.getCreditCardExpYY());
		//	Bank
		if (ba.getAccountNo() != null)
			setAccountNo(ba.getAccountNo());
		if (ba.getRoutingNo() != null)
			setRoutingNo(ba.getRoutingNo());
	}	//	setBP_BankAccount

	/**
	 * 	Save Info from BP Bank Account
	 *	@param ba BP bank account
	 * 	@return true if saved
	 */
	public boolean saveToBP_BankAccount (MBPBankAccount ba)
	{
		if (ba == null)
			return false;
		ba.setA_Name(getA_Name());
		ba.setA_Street(getA_Street());
		ba.setA_City(getA_City());
		ba.setA_State(getA_State());
		ba.setA_Zip(getA_Zip());
		ba.setA_Country(getA_Country());
		ba.setA_EMail(getA_EMail());
		ba.setA_Ident_DL(getA_Ident_DL());
		ba.setA_Ident_SSN(getA_Ident_SSN());
		//	CC
		ba.setCreditCardType(getCreditCardType());
		ba.setCreditCardNumber(getCreditCardNumber());
		ba.setCreditCardExpMM(getCreditCardExpMM());
		ba.setCreditCardExpYY(getCreditCardExpYY());
		//	Bank
		if (getAccountNo() != null)
			ba.setAccountNo(getAccountNo());
		if (getRoutingNo() != null)
			ba.setRoutingNo(getRoutingNo());
		//	Trx
		ba.setR_AvsAddr(getR_AvsAddr());
		ba.setR_AvsZip(getR_AvsZip());
		//
		boolean ok = ba.save(get_Trx());
		log.fine(ba.toString());
		return ok;
	}	//	saveToBP_BankAccount

	/**
	 * 	Set Doc Type bases on IsReceipt
	 */
	private void setC_DocType_ID ()
	{
		setC_DocType_ID(isReceipt());
	}	//	setC_DocType_ID

	/**
	 * 	Set Doc Type
	 * 	@param isReceipt is receipt
	 */
	public void setC_DocType_ID (boolean isReceipt)
	{
		setIsReceipt(isReceipt);
		//Query Modified by shantha for Request No:10020652
		String sql = "SELECT C_DocType_ID FROM C_DocType WHERE AD_Client_ID=? AND DocBaseType=? AND IsActive='Y' ORDER BY ASCII(IsDefault) DESC";
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		try
		{
			 pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getAD_Client_ID());
			if (isReceipt)
				pstmt.setString(2, MDocBaseType.DOCBASETYPE_ARReceipt);
			else
				pstmt.setString(2, MDocBaseType.DOCBASETYPE_APPayment);
			 rs = pstmt.executeQuery();
			if (rs.next())
				setC_DocType_ID(rs.getInt(1));
			else
				log.warning ("NOT found - isReceipt=" + isReceipt);
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

	}	//	setC_DocType_ID

	/**
	 * 	Set Document Type
	 *	@param C_DocType_ID doc type
	 */
	@Override
	public void setC_DocType_ID (int C_DocType_ID)
	{
	//	if (getDocumentNo() != null && getC_DocType_ID() != C_DocType_ID)
	//		setDocumentNo(null);
		super.setC_DocType_ID(C_DocType_ID);
	}	//	setC_DocType_ID

	/**
	 * 	Verify Document Type with Invoice
	 *	@return true if ok
	 */
	private boolean verifyDocType()
	{
		if (getC_DocType_ID() == 0)
			return false;
		//
		Boolean invoiceSO = null;
		//	Check Invoice First
		if (getC_Invoice_ID() > 0)
		{
			String sql = "SELECT idt.IsSOTrx "
				+ "FROM C_Invoice i"
				+ " INNER JOIN C_DocType idt ON (i.C_DocType_ID=idt.C_DocType_ID) "
				+ "WHERE i.C_Invoice_ID=?";
			PreparedStatement pstmt = null;
			ResultSet rs=null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_Trx());
				pstmt.setInt(1, getC_Invoice_ID());
				 rs = pstmt.executeQuery();
				if (rs.next())
					invoiceSO = Boolean.valueOf ("Y".equals(rs.getString(1)));
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
		}	//	Invoice

		//	DocumentType
		Boolean paymentSO = null;
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		String sql = "SELECT IsSOTrx "
			+ "FROM C_DocType "
			+ "WHERE C_DocType_ID=?";
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_DocType_ID());
			 rs= pstmt.executeQuery();
			if (rs.next())
				paymentSO = Boolean.valueOf ("Y".equals(rs.getString(1)));
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

		//	No Payment info
		if (paymentSO == null)
			return false;
		setIsReceipt(paymentSO.booleanValue());

		//	We have an Invoice .. and it does not match
		if ((invoiceSO != null)
				&& (invoiceSO.booleanValue() != paymentSO.booleanValue()))
			return false;
		//	OK
		return true;
	}	//	verifyDocType


	/**
	 * 	Set Invoice - Callout
	 *	@param oldC_Invoice_ID old BP
	 *	@param newC_Invoice_ID new BP
	 *	@param windowNo window no
	 *	@throws Exception
	 */
	@UICallout public void setC_Invoice_ID (String oldC_Invoice_ID,
			String newC_Invoice_ID, int windowNo) throws Exception
	{
		if ((newC_Invoice_ID == null) || (newC_Invoice_ID.length() == 0))
			return;
		int C_Invoice_ID = Integer.parseInt(newC_Invoice_ID);
		//  reset as dependent fields get reset
		//do n't use p_changeVO.setContext, 'cuz this is not intended to pass to client. use getCtx.setContext instead
		//p_changeVO.setContext(getCtx(), windowNo, "C_Invoice_ID", C_Invoice_ID);
		getCtx().setContext(windowNo, "C_Invoice_ID", C_Invoice_ID);
		setC_Invoice_ID(C_Invoice_ID);
		if (C_Invoice_ID == 0)
			return;

		setC_Order_ID(0);
		setC_Charge_ID(0);
		setC_Project_ID(0);
		setIsPrepayment(false);
		//
		setDiscountAmt(Env.ZERO);
		setWriteOffAmt(Env.ZERO);
		setIsOverUnderPayment(false);
		setOverUnderAmt(Env.ZERO);

		int C_InvoicePaySchedule_ID = 0;
			if ((getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_Invoice_ID") == C_Invoice_ID)
			&& (getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_InvoicePaySchedule_ID") != 0))
			C_InvoicePaySchedule_ID = getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_InvoicePaySchedule_ID");

		//  Payment Date
		Timestamp ts = getDateTrx();
		if (ts == null)
			ts = new Timestamp(System.currentTimeMillis());
		//
		String sql = "SELECT C_BPartner_ID,C_Currency_ID, "		//	1..2
			+ " invoiceOpen(C_Invoice_ID, ?),"					//	3		#1
			+ " invoiceDiscount(C_Invoice_ID,?,?), IsSOTrx ,PaymentRule "	//	4..5	#2/3
			+ "FROM C_Invoice WHERE C_Invoice_ID=?";			//			#4
		
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_InvoicePaySchedule_ID);
			pstmt.setTimestamp(2, ts);
			pstmt.setInt(3, C_InvoicePaySchedule_ID);
			pstmt.setInt(4, C_Invoice_ID);
			 rs = pstmt.executeQuery();
			if (rs.next())
			{
				setC_BPartner_ID(rs.getInt(1));
				int C_Currency_ID = rs.getInt(2);					//	Set Invoice Currency
				setC_Currency_ID(C_Currency_ID);
				//
				BigDecimal InvoiceOpen = rs.getBigDecimal(3);		//	Set Invoice OPen Amount
				if (InvoiceOpen == null)
					InvoiceOpen = Env.ZERO;
				BigDecimal DiscountAmt = rs.getBigDecimal(4);		//	Set Discount Amt
				if (DiscountAmt == null)
					DiscountAmt = Env.ZERO;
				String strTenderType= rs.getString(6);
				
				/*if (strTenderType.equals(TENDERTYPE_Check) ||
						strTenderType.equals(TENDERTYPE_CreditCard) ||
						strTenderType.equals(TENDERTYPE_DirectDebit) ||
						strTenderType.equals(TENDERTYPE_DirectDeposit))
				{*/
				
				if (strTenderType.equals("D"))	//Direct Debit			
					setTenderType(TENDERTYPE_DirectDebit);
				else if (strTenderType.equals("K"))	//Credit Card
					setTenderType(TENDERTYPE_CreditCard);
				else if (strTenderType.equals("S"))	//Check
					setTenderType(TENDERTYPE_Check);
				else if (strTenderType.equals("T"))	//Direct Deposit
					setTenderType(TENDERTYPE_DirectDeposit);
				else
					setTenderType(TENDERTYPE_Check);
				
			/*	}*/
				
				MInvoice invoice = new MInvoice(getCtx(), C_Invoice_ID, null);
				MDocType docType = MDocType.get(getCtx(), invoice.getC_DocType_ID());
				if (docType.isReturnTrx())
				{
					// Adjust discount amount for credit memos. Invoice Open Amt is already adjusted.
					DiscountAmt = DiscountAmt.negate();
				}
				setPayAmt(InvoiceOpen.subtract(DiscountAmt));
				setDiscountAmt(DiscountAmt);
				//IsSOTrx, Project
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

		checkDocType(windowNo);
	}	//	setC_Invoice_ID

	/**
	 * 	Set Order - Callout
	 *	@param oldC_Order_ID old BP
	 *	@param newC_Order_ID new BP
	 *	@param windowNo window no
	 *	@throws Exception
	 */
	@UICallout public void setC_Order_ID (String oldC_Order_ID,
			String newC_Order_ID, int windowNo) throws Exception
	{
		if ((newC_Order_ID == null) || (newC_Order_ID.length() == 0))
			return;
		int C_Order_ID = Integer.parseInt(newC_Order_ID);
		setC_Order_ID(C_Order_ID);
		if (C_Order_ID == 0)
			return;
		//
		setC_Invoice_ID(0);
		setC_Charge_ID(0);
		setC_Project_ID(0);
		setIsPrepayment(true);
		//
		setDiscountAmt(Env.ZERO);
		setWriteOffAmt(Env.ZERO);
		setIsOverUnderPayment(false);
		setOverUnderAmt(Env.ZERO);
		//
		String sql = "SELECT C_BPartner_ID,C_Currency_ID, GrandTotal, C_Project_ID "
			+ "FROM C_Order WHERE C_Order_ID=?"; 	// #1
		
		PreparedStatement pstmt =null;
		ResultSet rs=null;
	

		try
		{
			 pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, C_Order_ID);
			 rs = pstmt.executeQuery();
			if (rs.next())
			{
				setC_BPartner_ID(rs.getInt(1));
				int C_Currency_ID = rs.getInt(2);					//	Set Order Currency
				setC_Currency_ID(C_Currency_ID);
				//
				BigDecimal GrandTotal = rs.getBigDecimal(3);		//	Set Pay Amount
				if (GrandTotal == null)
					GrandTotal = Env.ZERO;
				setPayAmt(GrandTotal);
				setC_Project_ID(rs.getInt(4));
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
		checkDocType(windowNo);
	}	//	setC_Order_ID

	/**
	 * 	Set Charge - Callout
	 *	@param oldC_Charge_ID old BP
	 *	@param newC_Charge_ID new BP
	 *	@param windowNo window no
	 *	@throws Exception
	 */
	@UICallout public void setC_Charge_ID (String oldC_Charge_ID,
			String newC_Charge_ID, int windowNo) throws Exception
	{
		if ((newC_Charge_ID == null) || (newC_Charge_ID.length() == 0))
			return;
		int C_Charge_ID = Integer.parseInt(newC_Charge_ID);
		setC_Charge_ID(C_Charge_ID);
		if (C_Charge_ID == 0)
			return;
		//
		MCharge charge = MCharge.get(getCtx(), C_Charge_ID);
		setPayAmt(charge.getChargeAmt());
		setC_Order_ID(0);
		setC_Invoice_ID(0);
		setC_Project_ID(0);
		setIsPrepayment(true);
		setIsReceipt(false);
		//
		setDiscountAmt(Env.ZERO);
		setWriteOffAmt(Env.ZERO);
		setIsOverUnderPayment(false);
		setOverUnderAmt(Env.ZERO);
	}	//	setC_Charge_ID

	/**
	 * 	Set Order - Callout
	 *	@param oldC_DocType_ID old BP
	 *	@param newC_DocType_ID new BP
	 *	@param windowNo window no
	 *	@throws Exception
	 */
	@UICallout public void setC_DocType_ID (String oldC_DocType_ID,
			String newC_DocType_ID, int windowNo) throws Exception
	{
		if ((newC_DocType_ID == null) || (newC_DocType_ID.length() == 0))
			return;
		int C_DocType_ID = Integer.parseInt(newC_DocType_ID);
		setC_DocType_ID(C_DocType_ID);
		checkDocType(windowNo);
	}	//	setC_DocType_ID

	/**
	 * 	Check Document Type (Callout)
	 *	@param windowNo windowNo no
	 */
	private void checkDocType(int windowNo)
	{
		if(CThreadUtil.isCalloutActive())
			return;
		int C_Invoice_ID = getC_Invoice_ID();
		int C_Order_ID = getC_Order_ID();
		int C_DocType_ID = getC_DocType_ID();
		log.fine("C_Invoice_ID=" + C_Invoice_ID + ", C_DocType_ID=" + C_DocType_ID);
		MDocType dt = null;
		if (C_DocType_ID != 0)
		{
			dt = MDocType.get(getCtx(), C_DocType_ID);
			setIsReceipt(dt.isSOTrx());
			p_changeVO.setContext(getCtx(), windowNo, "IsSOTrx", dt.isSOTrx());
		}
		//	Invoice
		if (C_Invoice_ID != 0)
		{
			MInvoice inv = new MInvoice (getCtx(), C_Invoice_ID, null);
			if (dt != null)
			{
				if (inv.isSOTrx() != dt.isSOTrx())
					p_changeVO.addError(Msg.getMsg(getCtx(), "PaymentDocTypeInvoiceInconsistent"));
			}
		}
		//	Order Waiting Payment (can only be SO)
		if ((C_Order_ID != 0) && !dt.isSOTrx())
			p_changeVO.addError(Msg.getMsg(getCtx(), "PaymentDocTypeInvoiceInconsistent"));
	}	//	checkDocType

	/**
	 * 	Set Rate - Callout.
	 *	@param oldC_ConversionType_ID old
	 *	@param newC_ConversionType_ID new
	 *	@param windowNo window no
	 *	@throws Exception
	 */
	@UICallout public void setC_ConversionType_ID (String oldC_ConversionType_ID,
			String newC_ConversionType_ID, int windowNo) throws Exception
	{
		if ((newC_ConversionType_ID == null) || (newC_ConversionType_ID.length() == 0))
			return;
		int C_ConversionType_ID = Integer.parseInt(newC_ConversionType_ID);
		setC_ConversionType_ID(C_ConversionType_ID);
		if (C_ConversionType_ID == 0)
			return;
		checkAmt(windowNo, "C_ConversionType_ID");
	}	//	setC_ConversionType_ID

	/**
	 * 	Set Currency - Callout.
	 *	@param oldC_Currency_ID old
	 *	@param newC_Currency_ID new
	 *	@param windowNo window no
	 *	@throws Exception
	 */
	@UICallout public void setC_Currency_ID (String oldC_Currency_ID,
			String newC_Currency_ID, int windowNo) throws Exception
	{
		if ((newC_Currency_ID == null) || (newC_Currency_ID.length() == 0))
			return;
		int C_Currency_ID = Integer.parseInt(newC_Currency_ID);
		if (C_Currency_ID == 0)
			return;
		setC_Currency_ID(C_Currency_ID);
		checkAmt(windowNo, "C_Currency_ID");
	}	//	setC_Currency_ID


	/**
	 * 	Set Discount - Callout
	 *	@param oldDiscountAmt old value
	 *	@param newDiscountAmt new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setDiscountAmt (String oldDiscountAmt,
			String newDiscountAmt, int windowNo) throws Exception
	{
		if ((newDiscountAmt == null) || (newDiscountAmt.length() == 0))
			return;
		BigDecimal DiscountAmt = PO.convertToBigDecimal(newDiscountAmt);
		setDiscountAmt(DiscountAmt);
		checkAmt(windowNo, "DiscountAmt");
	}	//	setDiscountAmt

	/**
	 * 	Set Is Over Under Payment - Callout
	 *	@param oldIsOverUnderPayment old value
	 *	@param newIsOverUnderPayment new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setIsOverUnderPayment (String oldIsOverUnderPayment,
			String newIsOverUnderPayment, int windowNo) throws Exception
	{
		if ((newIsOverUnderPayment == null) || (newIsOverUnderPayment.length() == 0))
			return;
		checkAmt(windowNo, "IsOverUnderPayment");
		setIsOverUnderPayment("Y".equals(newIsOverUnderPayment));
	}	//	setIsOverUnderPayment

	/**
	 * 	Set Over Under Amt - Callout
	 *	@param oldOverUnderAmt old value
	 *	@param newOverUnderAmt new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setOverUnderAmt (String oldOverUnderAmt,
			String newOverUnderAmt, int windowNo) throws Exception
	{
		if ((newOverUnderAmt == null) || (newOverUnderAmt.length() == 0))
			return;
		BigDecimal OverUnderAmt = PO.convertToBigDecimal(newOverUnderAmt);
		setOverUnderAmt(OverUnderAmt);
		checkAmt(windowNo, "OverUnderAmt");
	}	//	setOverUnderAmt

	/**
	 * 	Set Pay Amt - Callout
	 *	@param oldPayAmt old value
	 *	@param newPayAmt new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setPayAmt (String oldPayAmt,
			String newPayAmt, int windowNo) throws Exception
	{
		if ((newPayAmt == null) || (newPayAmt.length() == 0))
			return;
		BigDecimal PayAmt = PO.convertToBigDecimal(newPayAmt);
		setPayAmt(PayAmt);
		checkAmt(windowNo, "PayAmt");
	}	//	setPayAmt

	/**
	 * 	Set WriteOff Amt - Callout
	 *	@param oldWriteOffAmt old value
	 *	@param newWriteOffAmt new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setWriteOffAmt (String oldWriteOffAmt,
			String newWriteOffAmt, int windowNo) throws Exception
	{
		if ((newWriteOffAmt == null) || (newWriteOffAmt.length() == 0))
			return;
		BigDecimal WriteOffAmt = PO.convertToBigDecimal(newWriteOffAmt);
		setWriteOffAmt(WriteOffAmt);
		checkAmt(windowNo, "WriteOffAmt");
	}	//	setWriteOffAmt

	/**
	 * 	Set DateTrx - Callout
	 *	@param oldDateTrx old
	 *	@param newDateTrx new
	 *	@param windowNo window no
	 *	@throws Exception
	 */
	@UICallout public void setDateTrx (String oldDateTrx,
			String newDateTrx, int windowNo) throws Exception
	{
		if ((newDateTrx == null) || (newDateTrx.length() == 0))
			return;
		Timestamp dateTrx = PO.convertToTimestamp(newDateTrx);
		if (dateTrx == null)
			return;
		setDateTrx(dateTrx);
		setDateAcct(dateTrx);
		checkAmt(windowNo, "DateTrx");
	}	//	setDateTrx

	/**
	 * 	Check amount (Callout)
	 *	@param windowNo window
	 *	@param columnName columnName
	 */
	private void checkAmt (int windowNo, String columnName)
	{
		if(CThreadUtil.isCalloutActive())
			return;
		int C_Invoice_ID = getC_Invoice_ID();
		//	New Payment
		if ((getC_Payment_ID() == 0)
			&& (getC_BPartner_ID() == 0)
			&& (C_Invoice_ID == 0))
			return;
		int C_Currency_ID = getC_Currency_ID();
		if (C_Currency_ID == 0)
			return;

		//	Changed Column
		if (columnName.equals("IsOverUnderPayment")	//	Set Over/Under Amt to Zero
			|| !isOverUnderPayment())
			setOverUnderAmt(Env.ZERO);

		int C_InvoicePaySchedule_ID = 0;
		if ((getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_Invoice_ID") == C_Invoice_ID)
			&& (getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_InvoicePaySchedule_ID") != 0))
			C_InvoicePaySchedule_ID = getCtx().getContextAsInt(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "C_InvoicePaySchedule_ID");

		//	Get Open Amount & Invoice Currency
		BigDecimal InvoiceOpenAmt = Env.ZERO;
		BigDecimal DiscountAmt = Env.ZERO;
		int C_Currency_Invoice_ID = 0;
		if (C_Invoice_ID != 0)
		{
			Timestamp ts = getDateTrx();
			if (ts == null)
				ts = new Timestamp(System.currentTimeMillis());
			String sql = "SELECT C_BPartner_ID,C_Currency_ID,"		//	1..2
				+ " invoiceOpen(C_Invoice_ID, ?),"					//	3		#1
				+ " invoiceDiscount(C_Invoice_ID,?,?), IsSOTrx "	//	4..5	#2/3
				+ "FROM C_Invoice WHERE C_Invoice_ID=?";			//			#4
			
			PreparedStatement pstmt =null;
			ResultSet rs=null;
			
			try
			{
				 pstmt = DB.prepareStatement(sql, get_Trx());
				pstmt.setInt(1, C_InvoicePaySchedule_ID);
				pstmt.setTimestamp(2, ts);
				pstmt.setInt(3, C_InvoicePaySchedule_ID);
				pstmt.setInt(4, C_Invoice_ID);
				 rs = pstmt.executeQuery();
				if (rs.next())
				{
					C_Currency_Invoice_ID= rs.getInt(2);
					InvoiceOpenAmt = rs.getBigDecimal(3);		//	Set Invoice Open Amount
					if (InvoiceOpenAmt == null)
						InvoiceOpenAmt = Env.ZERO;
					DiscountAmt = rs.getBigDecimal(4);
					if (DiscountAmt == null)
						DiscountAmt = Env.ZERO;

					MInvoice invoice = new MInvoice(getCtx(), C_Invoice_ID, null);
					MDocType docType = MDocType.get(getCtx(), invoice.getC_DocType_ID());
					if (docType.isReturnTrx())
					{
						// Adjust discount amount for credit memos. Invoice Open Amt is already adjusted.
						DiscountAmt = DiscountAmt.negate();
					}
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
		}	//	get Invoice Info
		log.fine("Open=" + InvoiceOpenAmt + " Discount= "+DiscountAmt
			+ ", C_Invoice_ID=" + C_Invoice_ID
			+ ", C_Currency_ID=" + C_Currency_Invoice_ID);

		//	Get Info from Tab
		BigDecimal PayAmt = getPayAmt();
		BigDecimal WriteOffAmt = getWriteOffAmt();
		BigDecimal OverUnderAmt = getOverUnderAmt();
		BigDecimal EnteredDiscountAmt = getDiscountAmt();
		log.fine("Pay=" + PayAmt + ", Discount=" + EnteredDiscountAmt
			+ ", WriteOff=" + WriteOffAmt + ", OverUnderAmt=" + OverUnderAmt);
		//	Get Currency Info
		MCurrency currency = MCurrency.get(getCtx(), C_Currency_ID);
		Timestamp ConvDate = getDateAcct();
		int C_ConversionType_ID = getC_ConversionType_ID();
		int AD_Client_ID = getAD_Client_ID();
		int AD_Org_ID = getAD_Org_ID();
		//	Get Currency Rate
		BigDecimal CurrencyRate = Env.ONE;
		if (((C_Currency_ID > 0) && (C_Currency_Invoice_ID > 0) &&
			(C_Currency_ID != C_Currency_Invoice_ID))
			|| columnName.equals("C_Currency_ID") || columnName.equals("C_ConversionType_ID"))
		{
			log.fine("InvCurrency=" + C_Currency_Invoice_ID
				+ ", PayCurrency=" + C_Currency_ID
				+ ", Date=" + ConvDate + ", Type=" + C_ConversionType_ID);
			CurrencyRate = MConversionRate.getRate (C_Currency_Invoice_ID, C_Currency_ID,
				ConvDate, C_ConversionType_ID, AD_Client_ID, AD_Org_ID);
			if ((CurrencyRate == null) || (CurrencyRate.compareTo(Env.ZERO) == 0))
			{
			//	mTab.setValue("C_Currency_ID", Integer.valueOf(C_Currency_Invoice_ID));	//	does not work
				if (C_Currency_Invoice_ID != 0)
					p_changeVO.addError(Msg.getMsg(getCtx(), "NoCurrencyConversion"));
				return;
			}
			//
			InvoiceOpenAmt = InvoiceOpenAmt.multiply(CurrencyRate)
				.setScale(currency.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
			DiscountAmt = DiscountAmt.multiply(CurrencyRate)
				.setScale(currency.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
			log.fine("Rate=" + CurrencyRate + ", InvoiceOpenAmt=" + InvoiceOpenAmt + ", DiscountAmt=" + DiscountAmt);
		}

		//	Currency Changed - convert all
		if (columnName.equals("C_Currency_ID") || columnName.equals("C_ConversionType_ID"))
		{

			WriteOffAmt = WriteOffAmt.multiply(CurrencyRate)
				.setScale(currency.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
			setWriteOffAmt(WriteOffAmt);
			OverUnderAmt = OverUnderAmt.multiply(CurrencyRate)
				.setScale(currency.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
			setOverUnderAmt(OverUnderAmt);

			// Entered Discount amount should be converted to entered currency
			EnteredDiscountAmt = EnteredDiscountAmt.multiply(CurrencyRate)
				.setScale(currency.getStdPrecision(), BigDecimal.ROUND_HALF_UP);
			setDiscountAmt(EnteredDiscountAmt);

			PayAmt = InvoiceOpenAmt.subtract(DiscountAmt).subtract(WriteOffAmt).subtract(OverUnderAmt);
			setPayAmt(PayAmt);
		}

		//	No Invoice - Set Discount, Writeoff, Under/Over to 0
		else if (C_Invoice_ID == 0)
		{
			if (DiscountAmt.signum() != 0)
				setDiscountAmt(Env.ZERO);
			if (WriteOffAmt.signum() != 0)
				setWriteOffAmt(Env.ZERO);
			if (OverUnderAmt.signum() != 0)
				setOverUnderAmt(Env.ZERO);
		}
		//  PayAmt - calculate write off
		else if (columnName.equals("PayAmt"))
		{
			if (isOverUnderPayment())
			{
				OverUnderAmt = InvoiceOpenAmt.subtract(PayAmt).subtract(DiscountAmt).subtract(WriteOffAmt);
				if(Env.ZERO.compareTo(OverUnderAmt) > 0 )
				{
					if (OverUnderAmt.abs().compareTo(DiscountAmt) <=0)
						DiscountAmt = DiscountAmt.add(OverUnderAmt);
					else
						DiscountAmt = Env.ZERO;
					OverUnderAmt = InvoiceOpenAmt.subtract(PayAmt).subtract(DiscountAmt).subtract(WriteOffAmt);
				}
				setDiscountAmt(DiscountAmt);
				setOverUnderAmt(OverUnderAmt);
			}
			else
			{
				WriteOffAmt = InvoiceOpenAmt.subtract(PayAmt).subtract(DiscountAmt).subtract(OverUnderAmt);
				if(Env.ZERO.compareTo(WriteOffAmt) > 0 )
				{
					if (WriteOffAmt.abs().compareTo(DiscountAmt) <=0)
						DiscountAmt = DiscountAmt.add(WriteOffAmt);
					else
						DiscountAmt = Env.ZERO;
					WriteOffAmt = InvoiceOpenAmt.subtract(PayAmt).subtract(DiscountAmt).subtract(OverUnderAmt);
				}
				setDiscountAmt(DiscountAmt);
				setWriteOffAmt(WriteOffAmt);
			}
		}
		else    //  calculate PayAmt
		{
			/* Allow reduction in discount, but not an increase. To give a discount that is higher
			   than the calculated discount, users have to enter a write off */			
			/* DiscountAmt will be negative for Return transactions (AP/AR credit memo) */
			/* we need to prevent increase in discount only when user is changing the discountamt
			 * otherwise calculated discount should be retained when changing columns like
			 * Transaction date
			 */
			if(columnName.equals("DiscountAmt"))
			{
				if ( Env.ZERO.compareTo(DiscountAmt) > 0 )
				{
					if(EnteredDiscountAmt.compareTo(DiscountAmt)>0)
						DiscountAmt = EnteredDiscountAmt;
				}
				else
				{
					if(EnteredDiscountAmt.compareTo(DiscountAmt)<0)
						DiscountAmt = EnteredDiscountAmt;
				}
			}
			PayAmt = InvoiceOpenAmt.subtract(DiscountAmt).subtract(WriteOffAmt).subtract(OverUnderAmt);
			setPayAmt(PayAmt);
			setDiscountAmt(DiscountAmt);
		}
	}	//	checkAmt







	/**
	 *	Get ISO Code of Currency
	 *	@return Currency ISO
	 */
	public String getCurrencyISO()
	{
		return MCurrency.getISO_Code (getCtx(), getC_Currency_ID());
	}	//	getCurrencyISO

	/**
	 * 	Get Document Status
	 *	@return Document Status Clear Text
	 */
	public String getDocStatusName()
	{
		return MRefList.getListName(getCtx(), 131, getDocStatus());
	}	//	getDocStatusName

	/**
	 *	Get Name of Credit Card
	 *	@return Name
	 */
	public String getCreditCardName()
	{
		return getCreditCardName(getCreditCardType());
	}	//	getCreditCardName

	/**
	 *	Get Name of Credit Card
	 * 	@param CreditCardType credit card type
	 *	@return Name
	 */
	public String getCreditCardName(String CreditCardType)
	{
		if (CreditCardType == null)
			return "--";
		else if (CREDITCARDTYPE_MasterCard.equals(CreditCardType))
			return "MasterCard";
		else if (CREDITCARDTYPE_Visa.equals(CreditCardType))
			return "Visa";
		else if (CREDITCARDTYPE_Amex.equals(CreditCardType))
			return "Amex";
		else if (CREDITCARDTYPE_ATM.equals(CreditCardType))
			return "ATM";
		else if (CREDITCARDTYPE_Diners.equals(CreditCardType))
			return "Diners";
		else if (CREDITCARDTYPE_Discover.equals(CreditCardType))
			return "Discover";
		else if (CREDITCARDTYPE_PurchaseCard.equals(CreditCardType))
			return "PurchaseCard";
		return "?" + CreditCardType + "?";
	}	//	getCreditCardName

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
	 * 	Get Pay Amt
	 * 	@param absolute if true the absolute amount (i.e. negative if payment)
	 *	@return amount
	 */
	public BigDecimal getPayAmt (boolean absolute)
	{
		if (isReceipt())
			return super.getPayAmt();
		return super.getPayAmt().negate();
	}	//	getPayAmt

	/**
	 * 	Get Discount Amt
	 *	@return amount
	 */
	@Override
	public BigDecimal getDiscountAmt ()
	{
		if (isReceipt())
			return super.getDiscountAmt();
		return super.getDiscountAmt().negate();
	}	//	getDiscountAmt

	/**
	 * 	Get WriteOff Amt
	 *	@return amount
	 */
	@Override
	public BigDecimal getWriteOffAmt ()
	{
		if (isReceipt())
			return super.getWriteOffAmt();
		return super.getWriteOffAmt().negate();
	}	//	getWriteOffAmt

	/**
	 * 	Get Pay Amt in cents
	 *	@return amount in cents
	 */
	public int getPayAmtInCents ()
	{
		BigDecimal bd = super.getPayAmt().multiply(Env.ONEHUNDRED);
		return bd.intValue();
	}	//	getPayAmtInCents

	/**
	 * 	Get Reversal Payment using reversal document no in description
	 *	@return payment
	 */
	public MPayment getReversal()
	{
		String description = getDescription();
		if ((description == null) || (description.length() == 0))
			return null;
		String s = description;
		int pos1 = 0;
		pos1 = s.indexOf("<-)");
		if (pos1 == -1)
			return null;

		int pos2 = s.lastIndexOf("(", pos1);
		if (pos2 == -1)
			return null;
		String paymentDocNo = s.substring(pos2+1, pos1);

		MPayment reversal = getPayment(getCtx(), paymentDocNo, get_Trx());
		return reversal;
	}


	/**	Process Message 			*/
	private String		m_processMsg = null;

	/**
	 * 	Unlock Document.
	 * 	@return true if success
	 */
	public boolean unlockIt()
	{
		log.info(toString());
		setProcessing(false);
		return true;
	}	//	unlockIt

	/**
	 * 	Invalidate Document
	 * 	@return true if success
	 */
	public boolean invalidateIt()
	{
		log.info(toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt


	/**************************************************************************
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid)
	 */
	public String prepareIt()
	{
		//	Unsuccessful Online Payment
		if (isOnline() && !isApproved())
		{
			if (getR_Result() != null)
				m_processMsg = "@OnlinePaymentFailed@";
			else
				m_processMsg = "@PaymentNotProcessed@";
			return DocActionConstants.STATUS_Invalid;
		}
		
		if ((getC_Order_ID() == 0) && (getC_Charge_ID() == 0) && (getC_Invoice_ID() ==0))
		{
			BigDecimal payAllocated = Env.ZERO;
			MPaymentAllocate[] pAllocs = MPaymentAllocate.get(this);
			if (pAllocs.length != 0)
			{
				for (MPaymentAllocate pa : pAllocs)
				{
					payAllocated = payAllocated.add(pa.getAmount());
				}
				if ((getPayAmt(false).abs()).compareTo(payAllocated.abs()) == -1)
				{
					m_processMsg = "@PaymentAllocateMisMatch@";
					return DocActionConstants.STATUS_Invalid;
				}

			}
		}

		//	Waiting Payment - Need to create Invoice & Shipment
		if ((getC_Order_ID() != 0) && (getC_Invoice_ID() == 0))
		{	//	see WebOrder.process
			MOrder order = new MOrder (getCtx(), getC_Order_ID(), get_Trx());
			if (DOCSTATUS_WaitingPayment.equals(order.getDocStatus()))
			{
				order.setC_Payment_ID(getC_Payment_ID());
				order.setDocAction(X_C_Order.DOCACTION_WaitComplete);
				order.set_Trx(get_Trx());
			//	boolean ok =
				DocumentEngine.processIt(order, X_C_Order.DOCACTION_WaitComplete);
				m_processMsg = order.getProcessMsg();
				order.save(get_Trx());
				//	Set Invoice
				MInvoice[] invoices = order.getInvoices(true);
				int length = invoices.length;
				if (length > 0)		//	get last invoice
					setC_Invoice_ID (invoices[length-1].getC_Invoice_ID());
				//
				if (getC_Invoice_ID() == 0)
				{
					m_processMsg = "@NotFound@ @C_Invoice_ID@";
					return DocActionConstants.STATUS_Invalid;
				}
			}	//	WaitingPayment
		}

		//	Consistency of Invoice / Document Type and IsReceipt
		if (!verifyDocType())
		{
			m_processMsg = "@PaymentDocTypeInvoiceInconsistent@";
			return DocActionConstants.STATUS_Invalid;
		}

		//	Do not pay when Credit Stop/Hold
		if (!isReceipt())
		{
			MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_Trx());
			if (X_C_BPartner.SOCREDITSTATUS_CreditStop.equals(bp.getSOCreditStatus()))
			{
				m_processMsg = "@BPartnerCreditStop@ - @TotalOpenBalance@="
					+ bp.getTotalOpenBalance()
					+ ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
				return DocActionConstants.STATUS_Invalid;
			}
			if (X_C_BPartner.SOCREDITSTATUS_CreditHold.equals(bp.getSOCreditStatus()))
			{
				m_processMsg = "@BPartnerCreditHold@ - @TotalOpenBalance@="
					+ bp.getTotalOpenBalance()
					+ ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
				return DocActionConstants.STATUS_Invalid;
			}
		}

		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocActionConstants.STATUS_InProgress;
	}	//	prepareIt

	/**
	 * 	Approve Document
	 * 	@return true if success
	 */
	public boolean  approveIt()
	{
		log.info(toString());
		setIsApproved(true);
		return true;
	}	//	approveIt

	/**
	 * 	Reject Approval
	 * 	@return true if success
	 */
	public boolean rejectIt()
	{
		log.info(toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt


	/**************************************************************************
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		boolean hasPaymentAllocRecords = false; // payment was used to pay an invoice and allocation records were created
		//	Charge Handling
		if (getC_Charge_ID() != 0)
		{
			setIsAllocated(true);
		}
		else
		{
			hasPaymentAllocRecords = allocateIt();	//	Create Allocation Records
			testAllocation();
		}

		//	Project update
		if (getC_Project_ID() != 0)
		{
		//	MProject project = new MProject(getCtx(), getC_Project_ID());
		}

		//	Counter Doc
		MDocType docType = MDocType.get(getCtx(), getC_DocType_ID());
		if (docType.isCreateCounter()) {
			MPayment counter = createCounterDoc();
			if (counter != null)
				m_processMsg += " @CounterDoc@: @C_Payment_ID@="
						+ counter.getDocumentNo();
		}

		//	Calculate amt allocated in Allocate tab of Payment window
		boolean paymentRemaining = false;
		BigDecimal payAllocated = Env.ZERO;
		BigDecimal payRemaining = getPayAmt(false).add(getDiscountAmt().add(getWriteOffAmt()));
		MPaymentAllocate[] pAllocs = MPaymentAllocate.get(this);
		if (pAllocs.length != 0)
		{
			for (MPaymentAllocate pa : pAllocs)
			{
				payAllocated = payAllocated.add(pa.getAmount());
			}
			if (!isReceipt())
				payAllocated = payAllocated.negate();
			payRemaining = getPayAmt(false).subtract(payAllocated);	 // Overwrite with payment remaining			
			if (payRemaining.signum() != 0)
				paymentRemaining = true;
		}
				
		// Only payments not allocated to charges should affect the BP open balance
		if ((getC_BPartner_ID() != 0) && (getC_Charge_ID() == 0) && (!hasPaymentAllocRecords || paymentRemaining))
		{
			if(!updateBPOpenBalance(false, payRemaining)){
				return DocActionConstants.STATUS_Invalid;
			}
		}

		return DocActionConstants.STATUS_Completed;
	}	//	completeIt

	// update BP open balance and credit used
	private boolean updateBPOpenBalance(boolean reverse, BigDecimal unallocatedAmt)
	{
		MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_Trx());
		BigDecimal payAmt = MConversionRate.convertBase(getCtx(), getPayAmt(false).add(getDiscountAmt().add(getWriteOffAmt())),
			getC_Currency_ID(), getDateAcct(), getC_ConversionType_ID(), getAD_Client_ID(), getAD_Org_ID());

		if (payAmt == null)
		{
			m_processMsg = "Could not convert C_Currency_ID=" + getC_Currency_ID()
				+ " to base C_Currency_ID=" + MClient.get(Env.getCtx()).getC_Currency_ID();
			return false;
		}
		unallocatedAmt = MConversionRate.convertBase(getCtx(), unallocatedAmt,
				getC_Currency_ID(), getDateAcct(), getC_ConversionType_ID(), getAD_Client_ID(), getAD_Org_ID());

		
		//	Total Balance
		BigDecimal newBalance = bp.getTotalOpenBalance(false);
		if (newBalance == null)
			newBalance = Env.ZERO;

		if(reverse)
			newBalance = newBalance.add(unallocatedAmt);
		else
			newBalance = newBalance.subtract(unallocatedAmt);

		if (isReceipt())
		{
			BigDecimal newCreditAmt = bp.getSO_CreditUsed();

			if(reverse)
			{
				if (newCreditAmt == null)
					newCreditAmt = unallocatedAmt;
				else
					newCreditAmt = newCreditAmt.add(unallocatedAmt);
			}
			else
			{
				if (newCreditAmt == null)
					newCreditAmt = unallocatedAmt.negate();
				else
					newCreditAmt = newCreditAmt.subtract(unallocatedAmt);
			}

			log.fine("TotalOpenBalance=" + bp.getTotalOpenBalance(false) + "(" + unallocatedAmt
				+ ", Credit=" + bp.getSO_CreditUsed() + "->" + newCreditAmt
				+ ", Balance=" + bp.getTotalOpenBalance(false) + " -> " + newBalance);
			bp.setSO_CreditUsed(newCreditAmt);
		}	//	SO
		else
		{
			log.fine("Payment Amount =" + getPayAmt(false) + "(" + unallocatedAmt
				+ ") Balance=" + bp.getTotalOpenBalance(false) + " -> " + newBalance);
		}
		bp.setTotalOpenBalance(newBalance);
		bp.setSOCreditStatus();
		if (!bp.save(get_Trx()))
		{
			m_processMsg = "Could not update Business Partner";
			return false;
		}

		return true;
	}	// updateBPOpenBalance

	/**
	 * 	Create Counter Document
	 * 	@return payment
	 */
	private MPayment createCounterDoc()
	{
		//	Is this a counter doc ?
		if (getRef_Payment_ID() != 0)
			return null;

		//	Org Must be linked to BPartner
		MOrg org = MOrg.get(getCtx(), getAD_Org_ID());
		//jz int counterC_BPartner_ID = org.getLinkedC_BPartner_ID();
		int counterC_BPartner_ID = org.getLinkedC_BPartner_ID(get_Trx());
		if (counterC_BPartner_ID == 0)
			return null;
		//	Business Partner needs to be linked to Org
		//jz MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), null);
		MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_Trx());
		int counterAD_Org_ID = bp.getAD_OrgBP_ID_Int();
		if (counterAD_Org_ID == 0)
			return null;

		//jz MBPartner counterBP = new MBPartner (getCtx(), counterC_BPartner_ID, null);
		MBPartner counterBP = new MBPartner (getCtx(), counterC_BPartner_ID, get_Trx());
	//	MOrgInfo counterOrgInfo = MOrgInfo.get(getCtx(), counterAD_Org_ID);
		log.info("Counter BP=" + counterBP.getName());

		//	Document Type
		int C_DocTypeTarget_ID = 0;
		MDocTypeCounter counterDT = MDocTypeCounter.getCounterDocType(getCtx(), getC_DocType_ID());
		if (counterDT != null)
		{
			log.fine(counterDT.toString());
			if (!counterDT.isCreateCounter() || !counterDT.isValid() || !counterDT.isActive())
				return null;
			C_DocTypeTarget_ID = counterDT.getCounter_C_DocType_ID();
		}
		else	//	indirect
		{
			C_DocTypeTarget_ID = MDocTypeCounter.getCounterDocType_ID(getCtx(),
					getC_DocType_ID());
			if (C_DocTypeTarget_ID <= 0)
				return null;

			counterDT = MDocTypeCounter.getCounterDocType(getCtx(),
					C_DocTypeTarget_ID);
			if (counterDT != null) {
				log.fine("Indirect C_DocTypeTarget_ID=" + C_DocTypeTarget_ID);
				if (!counterDT.isCreateCounter() || !counterDT.isValid()
						|| !counterDT.isActive())
					return null;
			} else
				return null;
		}

		//	Deep Copy
		MPayment counter = new MPayment (getCtx(), 0, get_Trx());
		counter.setAD_Org_ID(counterAD_Org_ID);
		counter.setC_BPartner_ID(counterBP.getC_BPartner_ID());
		counter.setIsReceipt(!isReceipt());
		counter.setC_DocType_ID(C_DocTypeTarget_ID);
		counter.setTrxType(getTrxType());
		counter.setTenderType(getTenderType());
		//
		counter.setPayAmt(getPayAmt());
		counter.setDiscountAmt(getDiscountAmt());
		counter.setTaxAmt(getTaxAmt());
		counter.setWriteOffAmt(getWriteOffAmt());
		counter.setIsOverUnderPayment (isOverUnderPayment());
		counter.setOverUnderAmt(getOverUnderAmt());
		counter.setC_Currency_ID(getC_Currency_ID());
		counter.setC_ConversionType_ID(getC_ConversionType_ID());
		//
		counter.setDateTrx (getDateTrx());
		counter.setDateAcct (getDateAcct());
		counter.setRef_Payment_ID(getC_Payment_ID());
		//
		String sql = "SELECT C_BankAccount_ID FROM C_BankAccount "
			+ "WHERE C_Currency_ID=? AND AD_Org_ID IN (0,?) AND IsActive='Y' "
			+ "ORDER BY ASCII(IsDefault) DESC";
		int C_BankAccount_ID = QueryUtil.getSQLValue(get_Trx(), sql, getC_Currency_ID(), counterAD_Org_ID);
		counter.setC_BankAccount_ID(C_BankAccount_ID);

		//	Refernces
		counter.setC_Activity_ID(getC_Activity_ID());
		counter.setC_Campaign_ID(getC_Campaign_ID());
		counter.setC_Project_ID(getC_Project_ID());
		counter.setUser1_ID(getUser1_ID());
		counter.setUser2_ID(getUser2_ID());
		// if payment has an invoice, set its counter to invoice reference
		if(getC_Invoice_ID()!=0)
		{
			MInvoice invoice = new MInvoice(getCtx(), getC_Invoice_ID(), null);
			if(invoice.getRef_Invoice_ID()!= 0)
				counter.setC_Invoice_ID(invoice.getRef_Invoice_ID());			
		}
		counter.save(get_Trx());
		log.fine(counter.toString());
		setRef_Payment_ID(counter.getC_Payment_ID());

		//	Document Action
		if (counterDT != null)
		{
			if (counterDT.getDocAction() != null)
			{
				counter.setDocAction(counterDT.getDocAction());
				DocumentEngine.processIt(counter, counterDT.getDocAction());
				counter.setProcessing(false);
				counter.save(get_Trx());
			}
		}
		return counter;
	}	//	createCounterDoc

	/**
	 * 	Allocate It.
	 * 	Only call when there is NO allocation as it will create duplicates.
	 * 	If an invoice exists, it allocates that
	 * 	otherwise it allocates Payment Selection.
	 *	@return true if allocated
	 */
	public boolean allocateIt()
	{
		
		//	Create invoice Allocation -	See also MCash.completeIt
		if (getC_Invoice_ID() != 0)
			return allocateInvoice();
		//	Invoices of a AP Payment Selection
		if (allocatePaySelection())
			return true;

		if (getC_Order_ID() != 0)
			return false;

		//	Allocate to multiple Payments based on entry
		MPaymentAllocate[] pAllocs = MPaymentAllocate.get(this);
		if (pAllocs.length == 0)
			return false;

		MAllocationHdr alloc = new MAllocationHdr(getCtx(), false,
			getDateAcct(), getC_Currency_ID(),
				Msg.translate(getCtx(), "C_Payment_ID")	+ ": " + getDocumentNo(),
				get_Trx());
		alloc.setAD_Org_ID(getAD_Org_ID());
		if (!alloc.save())
		{
			log.severe("P.Allocations not created");
			return false;
		}
		//	Lines
		for (MPaymentAllocate pa : pAllocs)
		{
			MAllocationLine aLine = null;
			if (isReceipt())
				aLine = new MAllocationLine (alloc, pa.getAmount(),
					pa.getDiscountAmt(), pa.getWriteOffAmt(), pa.getOverUnderAmt());
			else
				aLine = new MAllocationLine (alloc, pa.getAmount().negate(),
					pa.getDiscountAmt().negate(), pa.getWriteOffAmt().negate(), pa.getOverUnderAmt().negate());
			aLine.setAD_Org_ID(pa.getAD_Org_ID());
			aLine.setDocInfo(pa.getC_BPartner_ID(), 0, pa.getC_Invoice_ID());
			aLine.setPaymentInfo(getC_Payment_ID(), 0);
			if (!aLine.save(get_Trx()))
				log.warning("P.Allocations - line not saved");
			else
			{
				pa.setC_AllocationLine_ID(aLine.getC_AllocationLine_ID());
				pa.save();
			}
		}
		//	Should start WF
		DocumentEngine.processIt(alloc, DocActionConstants.ACTION_Complete);
		m_processMsg = "@C_AllocationHdr_ID@: " + alloc.getDocumentNo();
		return alloc.save(get_Trx());
	}	//	allocateIt

	/**
	 * 	Allocate single AP/AR Invoice
	 * 	@return true if allocated
	 */
	private boolean allocateInvoice()
	{
		//	calculate actual allocation
		BigDecimal allocationAmt = getPayAmt();			//	underpayment
		MInvoice invoice = new MInvoice(getCtx(), getC_Invoice_ID(), null);
		MDocType docType = MDocType.get(getCtx(), invoice.getC_DocType_ID());
		if (!docType.isReturnTrx())
		{
			if ((getOverUnderAmt().signum() < 0) && (getPayAmt().signum() > 0))
				allocationAmt = allocationAmt.add(getOverUnderAmt());	//	overpayment (negative)
		}
		else
		{
			if ((getOverUnderAmt().signum() > 0) && (getPayAmt().signum() < 0))
				allocationAmt = allocationAmt.add(getOverUnderAmt());
		}


		MAllocationHdr alloc = new MAllocationHdr(getCtx(), false,
			getDateAcct(), getC_Currency_ID(),
			Msg.translate(getCtx(), "C_Payment_ID") + ": " + getDocumentNo() + " [1]", get_Trx());

		alloc.setAD_Org_ID(getAD_Org_ID());
		if (!alloc.save())
		{
			log.log(Level.SEVERE, "Could not create Allocation Hdr");
			return false;
		}
		MAllocationLine aLine = null;
		if (isReceipt())
			aLine = new MAllocationLine (alloc, allocationAmt,
				getDiscountAmt(), getWriteOffAmt(), getOverUnderAmt());
		else  // negate() not required for DiscountAmt and WriteOffAmt since the getters already handle this
			aLine = new MAllocationLine (alloc, allocationAmt.negate(),
				getDiscountAmt(), getWriteOffAmt(), getOverUnderAmt().negate());
		aLine.setAD_Org_ID(getAD_Org_ID());
		aLine.setDocInfo(getC_BPartner_ID(), 0, getC_Invoice_ID());
		aLine.setC_Payment_ID(getC_Payment_ID());
		if (!aLine.save(get_Trx()))
		{
			log.log(Level.SEVERE, "Could not create Allocation Line");
			return false;
		}
		//	Should start WF
		DocumentEngine.processIt(alloc, DocActionConstants.ACTION_Complete);
		alloc.save(get_Trx());
		m_processMsg = "@C_AllocationHdr_ID@: " + alloc.getDocumentNo();

		//	Get Project from Invoice
		int C_Project_ID = QueryUtil.getSQLValue(get_Trx(),
			"SELECT MAX(C_Project_ID) FROM C_Invoice WHERE C_Invoice_ID=?", getC_Invoice_ID());
		if ((C_Project_ID > 0) && (getC_Project_ID() == 0))
			setC_Project_ID(C_Project_ID);
		else if ((C_Project_ID > 0) && (getC_Project_ID() > 0) && (C_Project_ID != getC_Project_ID()))
			log.warning("Invoice C_Project_ID=" + C_Project_ID
				+ " <> Payment C_Project_ID=" + getC_Project_ID());
		return true;
	}	//	allocateInvoice

	/**
	 * 	Allocate Payment Selection
	 * 	@return true if allocated
	 */
	private boolean allocatePaySelection()
	{
		MAllocationHdr alloc = new MAllocationHdr(getCtx(), false,
			getDateAcct(), getC_Currency_ID(),
			Msg.translate(getCtx(), "C_Payment_ID")	+ ": " + getDocumentNo() + " [n]", get_Trx());
		alloc.setAD_Org_ID(getAD_Org_ID());

		String sql = "SELECT psc.C_BPartner_ID, psl.C_Invoice_ID, psl.IsSOTrx, "	//	1..3
			+ " psl.PayAmt, psl.DiscountAmt, psl.DifferenceAmt, psl.OpenAmt "
			+ "FROM C_PaySelectionLine psl"
			+ " INNER JOIN C_PaySelectionCheck psc ON (psl.C_PaySelectionCheck_ID=psc.C_PaySelectionCheck_ID) "
			+ "WHERE psc.C_Payment_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_Payment_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				int C_BPartner_ID = rs.getInt(1);
				int C_Invoice_ID = rs.getInt(2);
				if ((C_BPartner_ID == 0) && (C_Invoice_ID == 0))
					continue;
				boolean isSOTrx = "Y".equals(rs.getString(3));
				BigDecimal PayAmt = rs.getBigDecimal(4);
				BigDecimal DiscountAmt = rs.getBigDecimal(5);
				BigDecimal WriteOffAmt = rs.getBigDecimal(6);
				BigDecimal OpenAmt = rs.getBigDecimal(7);
				BigDecimal OverUnderAmt = OpenAmt.subtract(PayAmt)
					.subtract(DiscountAmt).subtract(WriteOffAmt);
				//
				if ((alloc.get_ID() == 0) && !alloc.save(get_Trx()))
				{
					log.log(Level.SEVERE, "Could not create Allocation Hdr");
					rs.close();
					pstmt.close();
					return false;
				}
				MAllocationLine aLine = null;
				if (isSOTrx)
					aLine = new MAllocationLine (alloc, PayAmt,
						DiscountAmt, WriteOffAmt, OverUnderAmt);
				else
					aLine = new MAllocationLine (alloc, PayAmt.negate(),
						DiscountAmt.negate(), WriteOffAmt.negate(), OverUnderAmt.negate());
				aLine.setAD_Org_ID(getAD_Org_ID());
				aLine.setDocInfo(C_BPartner_ID, 0, C_Invoice_ID);
				aLine.setC_Payment_ID(getC_Payment_ID());
				if (!aLine.save(get_Trx()))
					log.log(Level.SEVERE, "Could not create Allocation Line");
			}
			
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
		//	Should start WF
		boolean ok = true;
		if (alloc.get_ID() == 0)
		{
			log.fine("No Allocation created - C_Payment_ID="
				+ getC_Payment_ID());
			ok = false;
		}
		else
		{
			DocumentEngine.processIt(alloc, DocActionConstants.ACTION_Complete);
			ok = alloc.save(get_Trx());
			m_processMsg = "@C_AllocationHdr_ID@: " + alloc.getDocumentNo();
		}
		return ok;
	}	//	allocatePaySelection

	/**
	 * 	De-allocate Payment.
	 * 	Unkink Invoices and Orders and delete Allocations
	 */
	private void deAllocate()
	{
		if (getC_Order_ID() != 0)
			setC_Order_ID(0);
	//	if (getC_Invoice_ID() == 0)
	//		return;
		//	De-Allocate all
		MAllocationHdr[] allocations = MAllocationHdr.getOfPayment(getCtx(),
			getC_Payment_ID(), get_Trx());
		log.fine("#" + allocations.length);
		for (MAllocationHdr allocation : allocations)
		{
			if (DOCSTATUS_Reversed.equals(allocation.getDocStatus())
				|| DOCSTATUS_Voided.equals(allocation.getDocStatus()))
				continue;  // allocation is already voided/reversed
			allocation.set_Trx(get_Trx());
			allocation.setDocAction(DocActionConstants.ACTION_Reverse_Correct);
			DocumentEngine.processIt(allocation, DocActionConstants.ACTION_Reverse_Correct);
			allocation.save();
		}

		// 	Unlink (in case allocation did not get it)
		if (getC_Invoice_ID() != 0)
		{
			//	Invoice
			String sql = "UPDATE C_Invoice "
				+ "SET C_Payment_ID = NULL, IsPaid='N' "
				+ "WHERE C_Invoice_ID= ? "
				+ " AND C_Payment_ID= ? ";
			Object[] params = new Object[]{getC_Invoice_ID(),getC_Payment_ID()};
			int no = DB.executeUpdate(get_Trx(), sql,params);
			if (no != 0)
				log.fine("Unlink Invoice #" + no);
			//	Order
			sql = "UPDATE C_Order o "
				+ "SET C_Payment_ID = NULL "
				+ "WHERE EXISTS (SELECT * FROM C_Invoice i "
					+ "WHERE o.C_Order_ID=i.C_Order_ID AND i.C_Invoice_ID= ?)"
				+ " AND C_Payment_ID= ? ";
			params = new Object[]{getC_Invoice_ID(),getC_Payment_ID()};
			no = DB.executeUpdate(get_Trx(), sql,params);
			if (no != 0)
				log.fine("Unlink Order #" + no);
		}
		//
		setC_Invoice_ID(0);
	}	//	deallocate

	/**
	 * 	Void Document.
	 * 	@return true if success
	 */
	public boolean voidIt()
	{
		log.info(toString());
		if (DOCSTATUS_Closed.equals(getDocStatus())
			|| DOCSTATUS_Reversed.equals(getDocStatus())
			|| DOCSTATUS_Voided.equals(getDocStatus()))
		{
			m_processMsg = "Document Closed: " + getDocStatus();
			setDocAction(DOCACTION_None);
			return false;
		}
		//	If on Bank Statement, don't void it - reverse it
		if (getC_BankStatementLine_ID() > 0)
			return DocumentEngine.processIt(this, DocActionConstants.ACTION_Reverse_Correct);

		//	Not Processed
		if (DOCSTATUS_Drafted.equals(getDocStatus())
			|| DOCSTATUS_Invalid.equals(getDocStatus())
			|| DOCSTATUS_InProgress.equals(getDocStatus())
			|| DOCSTATUS_Approved.equals(getDocStatus())
			|| DOCSTATUS_NotApproved.equals(getDocStatus()) )
		{
			addDescription(Msg.getMsg(getCtx(), "Voided") + " (" + getPayAmt() + ")");
			setPayAmt(Env.ZERO);
			setDiscountAmt(Env.ZERO);
			setWriteOffAmt(Env.ZERO);
			setOverUnderAmt(Env.ZERO);
			setIsAllocated(false);
			//	Unlink & De-Allocate
			deAllocate();
		}
		else
			return DocumentEngine.processIt(this, DocActionConstants.ACTION_Reverse_Correct);

		//
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}	//	voidIt

	/**
	 * 	Close Document.
	 * 	@return true if success
	 */
	public boolean closeIt()
	{
		log.info(toString());
		setDocAction(DOCACTION_None);
		return true;
	}	//	closeIt

	/**
	 * 	Reverse Correction
	 * 	@return true if success
	 */
	public boolean reverseCorrectIt()
	{
		log.info(toString());

		//	Std Period open?
		Timestamp dateAcct = getDateAcct();
		m_processMsg = DocumentEngine.isPeriodOpen(this);
		if (m_processMsg != null) {
			dateAcct = new Timestamp(System.currentTimeMillis());
		}

		//	Auto Reconcile if not on Bank Statement
		boolean reconciled = false; //	getC_BankStatementLine_ID() == 0;

		//	Create Reversal
		MPayment reversal = new MPayment (getCtx(), 0, get_Trx());
		copyValues(this, reversal);
		boolean isPrepaymnt = reversal.isPrepayment();
		reversal.setClientOrg(this);
		reversal.setC_Order_ID(0);
		reversal.setC_Invoice_ID(0);
		reversal.setDateAcct(dateAcct);
		
		//
		reversal.setDocumentNo(getDocumentNo() + REVERSE_INDICATOR);	//	indicate reversals
		reversal.setDocStatus(DOCSTATUS_Drafted);
		reversal.setDocAction(DOCACTION_Complete);
		//
		reversal.setPayAmt(getPayAmt().negate());
		reversal.setOverUnderAmt(getOverUnderAmt().negate());
		
		

		if ( reversal.isReceipt() )
		{
			reversal.setDiscountAmt(getDiscountAmt().negate());
			reversal.setWriteOffAmt(getWriteOffAmt().negate());
		}
		else
		{
			// no need to run negate() again since getters already handle this
			reversal.setDiscountAmt(getDiscountAmt());
			reversal.setWriteOffAmt(getWriteOffAmt());
		}

		//
		reversal.setIsAllocated(true);
		reversal.setIsReconciled(reconciled);	//	to put on bank statement
		reversal.setIsOnline(false);
		reversal.setIsApproved(true);
		reversal.setR_PnRef(null);
		reversal.setR_Result(null);
		reversal.setR_RespMsg(null);
		reversal.setR_AuthCode(null);
		reversal.setR_Info(null);
		reversal.setProcessing(false);
		reversal.setOProcessing("N");
		reversal.setProcessed(false);
		reversal.setPosted(false);
		reversal.setDescription(getDescription());
		reversal.addDescription("{->" + getDocumentNo() + ")");
		reversal.save(get_Trx());
		//	Post Reversal
		if (!DocumentEngine.processIt(reversal, DocActionConstants.ACTION_Complete))
		{
			m_processMsg = "Reversal ERROR: " + reversal.getProcessMsg();
			return false;
		}
		reversal.setIsPrepayment(isPrepaymnt);
		DocumentEngine.processIt(reversal, DocActionConstants.ACTION_Close);
		reversal.setDocStatus(DOCSTATUS_Reversed);
		reversal.setDocAction(DOCACTION_None);
		reversal.save(get_Trx());

		// Save reverse status and reversal document no in description to database
		// so that allocation reversal can access these
		setDocStatus(DOCSTATUS_Reversed);
		addDescription("(" + reversal.getDocumentNo() + "<-)");
		save(get_Trx());

		//	Unlink & De-Allocate
		deAllocate();
		// Force reversal to be un-allocated to override status set by allocation reversal
		reversal.setIsAllocated(true);
		reversal.save(get_Trx());

		setIsReconciled (reconciled);
		setIsAllocated (true);
		//	Set Status
		setDocAction(DOCACTION_None);
		setProcessed(true);
		save(get_Trx());
	
		StringBuffer info = new StringBuffer (reversal.getDocumentNo());

		//	Update BPartner open balance if there is no charge
		/* if (getC_BPartner_ID() != 0 && getC_Charge_ID() == 0)
		{
			if(!updateBPOpenBalance(true))
				return false;
		} */

		m_processMsg = info.toString();
		return true;
	}	//	reverseCorrectionIt

	/**
	 * 	Get Bank Statement Line of payment or 0
	 *	@return id or 0
	 */
	private int getC_BankStatementLine_ID()
	{
		String sql = "SELECT C_BankStatementLine_ID FROM C_BankStatementLine WHERE C_Payment_ID=?";
		int id = QueryUtil.getSQLValue(get_Trx(), sql, getC_Payment_ID());
		if (id < 0)
			return 0;
		return id;
	}	//	getC_BankStatementLine_ID

	/**
	 * 	Reverse Accrual - none
	 * 	@return true if success
	 */
	public boolean reverseAccrualIt()
	{
		log.info(toString());
		return false;
	}	//	reverseAccrualIt

	/**
	 * 	Re-activate
	 * 	@return true if success
	 */
	public boolean reActivateIt()
	{
		log.info(toString());
		return DocumentEngine.processIt(this, DocActionConstants.ACTION_Reverse_Correct);
	}	//	reActivateIt

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MPayment[");
		sb.append(get_ID()).append("-").append(getDocumentNo())
			.append(",Receipt=").append(isReceipt())
			.append(",PayAmt=").append(getPayAmt())
			.append(",Discount=").append(getDiscountAmt())
			.append(",WriteOff=").append(getWriteOffAmt())
			.append(",OverUnder=").append(getOverUnderAmt());
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		return dt.getName() + " " + getDocumentNo();
	}	//	getDocumentInfo

	/**
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		try
		{
			File temp = File.createTempFile(get_TableName()+get_ID()+"_", ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}	//	getPDF

	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
	//	ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.PAYMENT, getC_Payment_ID());
	//	if (re == null)
			return null;
	//	return re.getPDF(file);
	}	//	createPDF


	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getDocumentNo());
		//	: Total Lines = 123.00 (#1)
		sb.append(": ")
			.append(Msg.translate(getCtx(),"PayAmt")).append("=").append(getPayAmt())
			.append(",").append(Msg.translate(getCtx(),"WriteOffAmt")).append("=").append(getWriteOffAmt());
		//	 - Description
		if ((getDescription() != null) && (getDescription().length() > 0))
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary

	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg

	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
		return getCreatedBy();
	}	//	getDoc_User_ID

	/**
	 * 	Get Document Approval Amount
	 *	@return amount payment(AP) or write-off(AR)
	 */
	public BigDecimal getApprovalAmt()
	{
		if (isReceipt())
			return getWriteOffAmt();
		return getPayAmt();
	}	//	getApprovalAmt
	
	@Override
	public void setProcessMsg(String processMsg) {
		m_processMsg = processMsg;
	}


	@Override
	public String getDocBaseType() {
		return isReceipt() ? MDocBaseType.DOCBASETYPE_ARReceipt : MDocBaseType.DOCBASETYPE_APPayment;
	}


	@Override
	public Timestamp getDocumentDate() {
		return getDateAcct();
	}


	@Override
	public QueryParams getLineOrgsQueryInfo() {
		return null;
	}

	
}   //  MPayment
