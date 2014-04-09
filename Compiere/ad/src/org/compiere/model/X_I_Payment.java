/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2008 Compiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us at *
 * Compiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for I_Payment
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_I_Payment.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_I_Payment extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_Payment_ID id
    @param trx transaction
    */
    public X_I_Payment (Ctx ctx, int I_Payment_ID, Trx trx)
    {
        super (ctx, I_Payment_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_Payment_ID == 0)
        {
            setI_IsImported (null);	// N
            setI_Payment_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_Payment (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27511144612789L;
    /** Last Updated Timestamp 2008-12-11 10:14:56.0 */
    public static final long updatedMS = 1229019296000L;
    /** AD_Table_ID=597 */
    public static final int Table_ID=597;
    
    /** TableName=I_Payment */
    public static final String Table_Name="I_Payment";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Account City.
    @param A_City City or the Credit Card or Account Holder */
    public void setA_City (String A_City)
    {
        set_Value ("A_City", A_City);
        
    }
    
    /** Get Account City.
    @return City or the Credit Card or Account Holder */
    public String getA_City() 
    {
        return (String)get_Value("A_City");
        
    }
    
    /** Set Account Country.
    @param A_Country Country */
    public void setA_Country (String A_Country)
    {
        set_Value ("A_Country", A_Country);
        
    }
    
    /** Get Account Country.
    @return Country */
    public String getA_Country() 
    {
        return (String)get_Value("A_Country");
        
    }
    
    /** Set Account EMail.
    @param A_EMail Email Address */
    public void setA_EMail (String A_EMail)
    {
        set_Value ("A_EMail", A_EMail);
        
    }
    
    /** Get Account EMail.
    @return Email Address */
    public String getA_EMail() 
    {
        return (String)get_Value("A_EMail");
        
    }
    
    /** Set Driver License.
    @param A_Ident_DL Payment Identification - Driver License */
    public void setA_Ident_DL (String A_Ident_DL)
    {
        set_Value ("A_Ident_DL", A_Ident_DL);
        
    }
    
    /** Get Driver License.
    @return Payment Identification - Driver License */
    public String getA_Ident_DL() 
    {
        return (String)get_Value("A_Ident_DL");
        
    }
    
    /** Set Social Security No.
    @param A_Ident_SSN Payment Identification - Social Security No */
    public void setA_Ident_SSN (String A_Ident_SSN)
    {
        set_Value ("A_Ident_SSN", A_Ident_SSN);
        
    }
    
    /** Get Social Security No.
    @return Payment Identification - Social Security No */
    public String getA_Ident_SSN() 
    {
        return (String)get_Value("A_Ident_SSN");
        
    }
    
    /** Set Account Name.
    @param A_Name Name on Credit Card or Account holder */
    public void setA_Name (String A_Name)
    {
        set_Value ("A_Name", A_Name);
        
    }
    
    /** Get Account Name.
    @return Name on Credit Card or Account holder */
    public String getA_Name() 
    {
        return (String)get_Value("A_Name");
        
    }
    
    /** Set Account State.
    @param A_State State of the Credit Card or Account holder */
    public void setA_State (String A_State)
    {
        set_Value ("A_State", A_State);
        
    }
    
    /** Get Account State.
    @return State of the Credit Card or Account holder */
    public String getA_State() 
    {
        return (String)get_Value("A_State");
        
    }
    
    /** Set Account Street.
    @param A_Street Street address of the Credit Card or Account holder */
    public void setA_Street (String A_Street)
    {
        set_Value ("A_Street", A_Street);
        
    }
    
    /** Get Account Street.
    @return Street address of the Credit Card or Account holder */
    public String getA_Street() 
    {
        return (String)get_Value("A_Street");
        
    }
    
    /** Set Account Zip/Postal.
    @param A_Zip Zip Code of the Credit Card or Account Holder */
    public void setA_Zip (String A_Zip)
    {
        set_Value ("A_Zip", A_Zip);
        
    }
    
    /** Get Account Zip/Postal.
    @return Zip Code of the Credit Card or Account Holder */
    public String getA_Zip() 
    {
        return (String)get_Value("A_Zip");
        
    }
    
    /** Set Account No.
    @param AccountNo Account Number */
    public void setAccountNo (String AccountNo)
    {
        set_Value ("AccountNo", AccountNo);
        
    }
    
    /** Get Account No.
    @return Account Number */
    public String getAccountNo() 
    {
        return (String)get_Value("AccountNo");
        
    }
    
    /** Set Business Partner Key.
    @param BPartnerValue Key of the Business Partner */
    public void setBPartnerValue (String BPartnerValue)
    {
        set_Value ("BPartnerValue", BPartnerValue);
        
    }
    
    /** Get Business Partner Key.
    @return Key of the Business Partner */
    public String getBPartnerValue() 
    {
        return (String)get_Value("BPartnerValue");
        
    }
    
    /** Set Bank Account No.
    @param BankAccountNo Bank Account Number */
    public void setBankAccountNo (String BankAccountNo)
    {
        set_Value ("BankAccountNo", BankAccountNo);
        
    }
    
    /** Get Bank Account No.
    @return Bank Account Number */
    public String getBankAccountNo() 
    {
        return (String)get_Value("BankAccountNo");
        
    }
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
        else
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Bank Account.
    @param C_BankAccount_ID Account at the Bank */
    public void setC_BankAccount_ID (int C_BankAccount_ID)
    {
        if (C_BankAccount_ID <= 0) set_Value ("C_BankAccount_ID", null);
        else
        set_Value ("C_BankAccount_ID", Integer.valueOf(C_BankAccount_ID));
        
    }
    
    /** Get Bank Account.
    @return Account at the Bank */
    public int getC_BankAccount_ID() 
    {
        return get_ValueAsInt("C_BankAccount_ID");
        
    }
    
    /** Set Charge.
    @param C_Charge_ID Additional document charges */
    public void setC_Charge_ID (int C_Charge_ID)
    {
        if (C_Charge_ID <= 0) set_Value ("C_Charge_ID", null);
        else
        set_Value ("C_Charge_ID", Integer.valueOf(C_Charge_ID));
        
    }
    
    /** Get Charge.
    @return Additional document charges */
    public int getC_Charge_ID() 
    {
        return get_ValueAsInt("C_Charge_ID");
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID <= 0) set_Value ("C_Currency_ID", null);
        else
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set Document Type.
    @param C_DocType_ID Document type or rules */
    public void setC_DocType_ID (int C_DocType_ID)
    {
        if (C_DocType_ID <= 0) set_Value ("C_DocType_ID", null);
        else
        set_Value ("C_DocType_ID", Integer.valueOf(C_DocType_ID));
        
    }
    
    /** Get Document Type.
    @return Document type or rules */
    public int getC_DocType_ID() 
    {
        return get_ValueAsInt("C_DocType_ID");
        
    }
    
    /** Set Invoice.
    @param C_Invoice_ID Invoice Identifier */
    public void setC_Invoice_ID (int C_Invoice_ID)
    {
        if (C_Invoice_ID <= 0) set_Value ("C_Invoice_ID", null);
        else
        set_Value ("C_Invoice_ID", Integer.valueOf(C_Invoice_ID));
        
    }
    
    /** Get Invoice.
    @return Invoice Identifier */
    public int getC_Invoice_ID() 
    {
        return get_ValueAsInt("C_Invoice_ID");
        
    }
    
    /** Set Payment.
    @param C_Payment_ID Payment identifier */
    public void setC_Payment_ID (int C_Payment_ID)
    {
        if (C_Payment_ID <= 0) set_Value ("C_Payment_ID", null);
        else
        set_Value ("C_Payment_ID", Integer.valueOf(C_Payment_ID));
        
    }
    
    /** Get Payment.
    @return Payment identifier */
    public int getC_Payment_ID() 
    {
        return get_ValueAsInt("C_Payment_ID");
        
    }
    
    /** Set Charge amount.
    @param ChargeAmt Charge Amount */
    public void setChargeAmt (java.math.BigDecimal ChargeAmt)
    {
        set_Value ("ChargeAmt", ChargeAmt);
        
    }
    
    /** Get Charge amount.
    @return Charge Amount */
    public java.math.BigDecimal getChargeAmt() 
    {
        return get_ValueAsBigDecimal("ChargeAmt");
        
    }
    
    /** Set Charge Name.
    @param ChargeName Name of the Charge */
    public void setChargeName (String ChargeName)
    {
        set_Value ("ChargeName", ChargeName);
        
    }
    
    /** Get Charge Name.
    @return Name of the Charge */
    public String getChargeName() 
    {
        return (String)get_Value("ChargeName");
        
    }
    
    /** Set Check No.
    @param CheckNo Check Number */
    public void setCheckNo (String CheckNo)
    {
        set_Value ("CheckNo", CheckNo);
        
    }
    
    /** Get Check No.
    @return Check Number */
    public String getCheckNo() 
    {
        return (String)get_Value("CheckNo");
        
    }
    
    /** Set Exp. Month.
    @param CreditCardExpMM Expiry Month */
    public void setCreditCardExpMM (int CreditCardExpMM)
    {
        set_Value ("CreditCardExpMM", Integer.valueOf(CreditCardExpMM));
        
    }
    
    /** Get Exp. Month.
    @return Expiry Month */
    public int getCreditCardExpMM() 
    {
        return get_ValueAsInt("CreditCardExpMM");
        
    }
    
    /** Set Exp. Year.
    @param CreditCardExpYY Expiry Year */
    public void setCreditCardExpYY (int CreditCardExpYY)
    {
        set_Value ("CreditCardExpYY", Integer.valueOf(CreditCardExpYY));
        
    }
    
    /** Get Exp. Year.
    @return Expiry Year */
    public int getCreditCardExpYY() 
    {
        return get_ValueAsInt("CreditCardExpYY");
        
    }
    
    /** Set Number.
    @param CreditCardNumber Credit Card Number */
    public void setCreditCardNumber (String CreditCardNumber)
    {
        set_Value ("CreditCardNumber", CreditCardNumber);
        
    }
    
    /** Get Number.
    @return Credit Card Number */
    public String getCreditCardNumber() 
    {
        return (String)get_Value("CreditCardNumber");
        
    }
    
    /** Amex = A */
    public static final String CREDITCARDTYPE_Amex = X_Ref_C_Payment_CreditCard_Type.AMEX.getValue();
    /** ATM = C */
    public static final String CREDITCARDTYPE_ATM = X_Ref_C_Payment_CreditCard_Type.ATM.getValue();
    /** Diners = D */
    public static final String CREDITCARDTYPE_Diners = X_Ref_C_Payment_CreditCard_Type.DINERS.getValue();
    /** MasterCard = M */
    public static final String CREDITCARDTYPE_MasterCard = X_Ref_C_Payment_CreditCard_Type.MASTER_CARD.getValue();
    /** Discover = N */
    public static final String CREDITCARDTYPE_Discover = X_Ref_C_Payment_CreditCard_Type.DISCOVER.getValue();
    /** Purchase Card = P */
    public static final String CREDITCARDTYPE_PurchaseCard = X_Ref_C_Payment_CreditCard_Type.PURCHASE_CARD.getValue();
    /** Visa = V */
    public static final String CREDITCARDTYPE_Visa = X_Ref_C_Payment_CreditCard_Type.VISA.getValue();
    /** Set Credit Card.
    @param CreditCardType Credit Card (Visa, MC, Amex) */
    public void setCreditCardType (String CreditCardType)
    {
        if (!X_Ref_C_Payment_CreditCard_Type.isValid(CreditCardType))
        throw new IllegalArgumentException ("CreditCardType Invalid value - " + CreditCardType + " - Reference_ID=149 - A - C - D - M - N - P - V");
        set_Value ("CreditCardType", CreditCardType);
        
    }
    
    /** Get Credit Card.
    @return Credit Card (Visa, MC, Amex) */
    public String getCreditCardType() 
    {
        return (String)get_Value("CreditCardType");
        
    }
    
    /** Set Verification Code.
    @param CreditCardVV Credit Card Verification code on credit card */
    public void setCreditCardVV (String CreditCardVV)
    {
        set_Value ("CreditCardVV", CreditCardVV);
        
    }
    
    /** Get Verification Code.
    @return Credit Card Verification code on credit card */
    public String getCreditCardVV() 
    {
        return (String)get_Value("CreditCardVV");
        
    }
    
    /** Set Account Date.
    @param DateAcct General Ledger Date */
    public void setDateAcct (Timestamp DateAcct)
    {
        set_Value ("DateAcct", DateAcct);
        
    }
    
    /** Get Account Date.
    @return General Ledger Date */
    public Timestamp getDateAcct() 
    {
        return (Timestamp)get_Value("DateAcct");
        
    }
    
    /** Set Transaction Date.
    @param DateTrx Transaction Date */
    public void setDateTrx (Timestamp DateTrx)
    {
        set_Value ("DateTrx", DateTrx);
        
    }
    
    /** Get Transaction Date.
    @return Transaction Date */
    public Timestamp getDateTrx() 
    {
        return (Timestamp)get_Value("DateTrx");
        
    }
    
    /** Set Discount Amount.
    @param DiscountAmt Calculated amount of discount */
    public void setDiscountAmt (java.math.BigDecimal DiscountAmt)
    {
        set_Value ("DiscountAmt", DiscountAmt);
        
    }
    
    /** Get Discount Amount.
    @return Calculated amount of discount */
    public java.math.BigDecimal getDiscountAmt() 
    {
        return get_ValueAsBigDecimal("DiscountAmt");
        
    }
    
    /** Set Document Type Name.
    @param DocTypeName Name of the Document Type */
    public void setDocTypeName (String DocTypeName)
    {
        set_Value ("DocTypeName", DocTypeName);
        
    }
    
    /** Get Document Type Name.
    @return Name of the Document Type */
    public String getDocTypeName() 
    {
        return (String)get_Value("DocTypeName");
        
    }
    
    /** Set Document No.
    @param DocumentNo Document sequence number of the document */
    public void setDocumentNo (String DocumentNo)
    {
        set_Value ("DocumentNo", DocumentNo);
        
    }
    
    /** Get Document No.
    @return Document sequence number of the document */
    public String getDocumentNo() 
    {
        return (String)get_Value("DocumentNo");
        
    }
    
    /** Set ISO Currency Code.
    @param ISO_Code Three letter ISO 4217 Code of the Currency */
    public void setISO_Code (String ISO_Code)
    {
        set_Value ("ISO_Code", ISO_Code);
        
    }
    
    /** Get ISO Currency Code.
    @return Three letter ISO 4217 Code of the Currency */
    public String getISO_Code() 
    {
        return (String)get_Value("ISO_Code");
        
    }
    
    /** Set Import Error Message.
    @param I_ErrorMsg Messages generated from import process */
    public void setI_ErrorMsg (String I_ErrorMsg)
    {
        set_Value ("I_ErrorMsg", I_ErrorMsg);
        
    }
    
    /** Get Import Error Message.
    @return Messages generated from import process */
    public String getI_ErrorMsg() 
    {
        return (String)get_Value("I_ErrorMsg");
        
    }
    
    /** Error = E */
    public static final String I_ISIMPORTED_Error = X_Ref__IsImported.ERROR.getValue();
    /** No = N */
    public static final String I_ISIMPORTED_No = X_Ref__IsImported.NO.getValue();
    /** Yes = Y */
    public static final String I_ISIMPORTED_Yes = X_Ref__IsImported.YES.getValue();
    /** Set Imported.
    @param I_IsImported Has this import been processed? */
    public void setI_IsImported (String I_IsImported)
    {
        if (I_IsImported == null) throw new IllegalArgumentException ("I_IsImported is mandatory");
        if (!X_Ref__IsImported.isValid(I_IsImported))
        throw new IllegalArgumentException ("I_IsImported Invalid value - " + I_IsImported + " - Reference_ID=420 - E - N - Y");
        set_Value ("I_IsImported", I_IsImported);
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public String getI_IsImported() 
    {
        return (String)get_Value("I_IsImported");
        
    }
    
    /** Set Import Payment.
    @param I_Payment_ID Import Payment */
    public void setI_Payment_ID (int I_Payment_ID)
    {
        if (I_Payment_ID < 1) throw new IllegalArgumentException ("I_Payment_ID is mandatory.");
        set_ValueNoCheck ("I_Payment_ID", Integer.valueOf(I_Payment_ID));
        
    }
    
    /** Get Import Payment.
    @return Import Payment */
    public int getI_Payment_ID() 
    {
        return get_ValueAsInt("I_Payment_ID");
        
    }
    
    /** Set Invoice Document No.
    @param InvoiceDocumentNo Document Number of the Invoice */
    public void setInvoiceDocumentNo (String InvoiceDocumentNo)
    {
        set_Value ("InvoiceDocumentNo", InvoiceDocumentNo);
        
    }
    
    /** Get Invoice Document No.
    @return Document Number of the Invoice */
    public String getInvoiceDocumentNo() 
    {
        return (String)get_Value("InvoiceDocumentNo");
        
    }
    
    /** Set Approved.
    @param IsApproved Indicates if this document requires approval */
    public void setIsApproved (boolean IsApproved)
    {
        set_Value ("IsApproved", Boolean.valueOf(IsApproved));
        
    }
    
    /** Get Approved.
    @return Indicates if this document requires approval */
    public boolean isApproved() 
    {
        return get_ValueAsBoolean("IsApproved");
        
    }
    
    /** Set Delayed Capture.
    @param IsDelayedCapture Charge after Shipment */
    public void setIsDelayedCapture (boolean IsDelayedCapture)
    {
        set_Value ("IsDelayedCapture", Boolean.valueOf(IsDelayedCapture));
        
    }
    
    /** Get Delayed Capture.
    @return Charge after Shipment */
    public boolean isDelayedCapture() 
    {
        return get_ValueAsBoolean("IsDelayedCapture");
        
    }
    
    /** Set Over/Under Payment.
    @param IsOverUnderPayment Over-Payment (unallocated) or Under-Payment (partial payment) */
    public void setIsOverUnderPayment (boolean IsOverUnderPayment)
    {
        set_Value ("IsOverUnderPayment", Boolean.valueOf(IsOverUnderPayment));
        
    }
    
    /** Get Over/Under Payment.
    @return Over-Payment (unallocated) or Under-Payment (partial payment) */
    public boolean isOverUnderPayment() 
    {
        return get_ValueAsBoolean("IsOverUnderPayment");
        
    }
    
    /** Set Receipt.
    @param IsReceipt This is a sales transaction (receipt) */
    public void setIsReceipt (boolean IsReceipt)
    {
        set_Value ("IsReceipt", Boolean.valueOf(IsReceipt));
        
    }
    
    /** Get Receipt.
    @return This is a sales transaction (receipt) */
    public boolean isReceipt() 
    {
        return get_ValueAsBoolean("IsReceipt");
        
    }
    
    /** Set Self-Service.
    @param IsSelfService This is a Self-Service entry or this entry can be changed via Self-Service */
    public void setIsSelfService (boolean IsSelfService)
    {
        set_Value ("IsSelfService", Boolean.valueOf(IsSelfService));
        
    }
    
    /** Get Self-Service.
    @return This is a Self-Service entry or this entry can be changed via Self-Service */
    public boolean isSelfService() 
    {
        return get_ValueAsBoolean("IsSelfService");
        
    }
    
    /** Set Micr.
    @param Micr Combination of routing no, account and check no */
    public void setMicr (String Micr)
    {
        set_Value ("Micr", Micr);
        
    }
    
    /** Get Micr.
    @return Combination of routing no, account and check no */
    public String getMicr() 
    {
        return (String)get_Value("Micr");
        
    }
    
    /** Set Original Transaction ID.
    @param Orig_TrxID Original Transaction ID */
    public void setOrig_TrxID (String Orig_TrxID)
    {
        set_Value ("Orig_TrxID", Orig_TrxID);
        
    }
    
    /** Get Original Transaction ID.
    @return Original Transaction ID */
    public String getOrig_TrxID() 
    {
        return (String)get_Value("Orig_TrxID");
        
    }
    
    /** Set Over/Under Payment Amount.
    @param OverUnderAmt Over-Payment (unallocated) or Under-Payment (partial payment) Amount */
    public void setOverUnderAmt (java.math.BigDecimal OverUnderAmt)
    {
        set_Value ("OverUnderAmt", OverUnderAmt);
        
    }
    
    /** Get Over/Under Payment Amount.
    @return Over-Payment (unallocated) or Under-Payment (partial payment) Amount */
    public java.math.BigDecimal getOverUnderAmt() 
    {
        return get_ValueAsBigDecimal("OverUnderAmt");
        
    }
    
    /** Set PO Number.
    @param PONum Purchase Order Number */
    public void setPONum (String PONum)
    {
        set_Value ("PONum", PONum);
        
    }
    
    /** Get PO Number.
    @return Purchase Order Number */
    public String getPONum() 
    {
        return (String)get_Value("PONum");
        
    }
    
    /** Set Payment amount.
    @param PayAmt Amount being paid */
    public void setPayAmt (java.math.BigDecimal PayAmt)
    {
        set_Value ("PayAmt", PayAmt);
        
    }
    
    /** Get Payment amount.
    @return Amount being paid */
    public java.math.BigDecimal getPayAmt() 
    {
        return get_ValueAsBigDecimal("PayAmt");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    /** Set Authorization Code.
    @param R_AuthCode Authorization Code returned */
    public void setR_AuthCode (String R_AuthCode)
    {
        set_Value ("R_AuthCode", R_AuthCode);
        
    }
    
    /** Get Authorization Code.
    @return Authorization Code returned */
    public String getR_AuthCode() 
    {
        return (String)get_Value("R_AuthCode");
        
    }
    
    /** Set Info.
    @param R_Info Response info */
    public void setR_Info (String R_Info)
    {
        set_Value ("R_Info", R_Info);
        
    }
    
    /** Get Info.
    @return Response info */
    public String getR_Info() 
    {
        return (String)get_Value("R_Info");
        
    }
    
    /** Set Reference.
    @param R_PnRef Payment reference */
    public void setR_PnRef (String R_PnRef)
    {
        set_Value ("R_PnRef", R_PnRef);
        
    }
    
    /** Get Reference.
    @return Payment reference */
    public String getR_PnRef() 
    {
        return (String)get_Value("R_PnRef");
        
    }
    
    /** Set Response Message.
    @param R_RespMsg Response message */
    public void setR_RespMsg (String R_RespMsg)
    {
        set_Value ("R_RespMsg", R_RespMsg);
        
    }
    
    /** Get Response Message.
    @return Response message */
    public String getR_RespMsg() 
    {
        return (String)get_Value("R_RespMsg");
        
    }
    
    /** Set Result.
    @param R_Result Result of transmission */
    public void setR_Result (String R_Result)
    {
        set_Value ("R_Result", R_Result);
        
    }
    
    /** Get Result.
    @return Result of transmission */
    public String getR_Result() 
    {
        return (String)get_Value("R_Result");
        
    }
    
    /** Set Routing No.
    @param RoutingNo Bank Routing Number */
    public void setRoutingNo (String RoutingNo)
    {
        set_Value ("RoutingNo", RoutingNo);
        
    }
    
    /** Get Routing No.
    @return Bank Routing Number */
    public String getRoutingNo() 
    {
        return (String)get_Value("RoutingNo");
        
    }
    
    /** Set Swipe.
    @param Swipe Track 1 and 2 of the Credit Card */
    public void setSwipe (String Swipe)
    {
        set_Value ("Swipe", Swipe);
        
    }
    
    /** Get Swipe.
    @return Track 1 and 2 of the Credit Card */
    public String getSwipe() 
    {
        return (String)get_Value("Swipe");
        
    }
    
    /** Set Tax Amount.
    @param TaxAmt Tax Amount for a document */
    public void setTaxAmt (java.math.BigDecimal TaxAmt)
    {
        set_Value ("TaxAmt", TaxAmt);
        
    }
    
    /** Get Tax Amount.
    @return Tax Amount for a document */
    public java.math.BigDecimal getTaxAmt() 
    {
        return get_ValueAsBigDecimal("TaxAmt");
        
    }
    
    /** Direct Deposit = A */
    public static final String TENDERTYPE_DirectDeposit = X_Ref_C_Payment_Tender_Type.DIRECT_DEPOSIT.getValue();
    /** Credit Card = C */
    public static final String TENDERTYPE_CreditCard = X_Ref_C_Payment_Tender_Type.CREDIT_CARD.getValue();
    /** Direct Debit = D */
    public static final String TENDERTYPE_DirectDebit = X_Ref_C_Payment_Tender_Type.DIRECT_DEBIT.getValue();
    /** Check = K */
    public static final String TENDERTYPE_Check = X_Ref_C_Payment_Tender_Type.CHECK.getValue();
    /** Set Tender type.
    @param TenderType Method of Payment */
    public void setTenderType (String TenderType)
    {
        if (!X_Ref_C_Payment_Tender_Type.isValid(TenderType))
        throw new IllegalArgumentException ("TenderType Invalid value - " + TenderType + " - Reference_ID=214 - A - C - D - K");
        set_Value ("TenderType", TenderType);
        
    }
    
    /** Get Tender type.
    @return Method of Payment */
    public String getTenderType() 
    {
        return (String)get_Value("TenderType");
        
    }
    
    /** Authorization = A */
    public static final String TRXTYPE_Authorization = X_Ref_C_Payment_Trx_Type.AUTHORIZATION.getValue();
    /** Credit (Payment) = C */
    public static final String TRXTYPE_CreditPayment = X_Ref_C_Payment_Trx_Type.CREDIT_PAYMENT.getValue();
    /** Delayed Capture = D */
    public static final String TRXTYPE_DelayedCapture = X_Ref_C_Payment_Trx_Type.DELAYED_CAPTURE.getValue();
    /** Voice Authorization = F */
    public static final String TRXTYPE_VoiceAuthorization = X_Ref_C_Payment_Trx_Type.VOICE_AUTHORIZATION.getValue();
    /** Sales = S */
    public static final String TRXTYPE_Sales = X_Ref_C_Payment_Trx_Type.SALES.getValue();
    /** Void = V */
    public static final String TRXTYPE_Void = X_Ref_C_Payment_Trx_Type.VOID.getValue();
    /** Set Transaction Type.
    @param TrxType Type of credit card transaction */
    public void setTrxType (String TrxType)
    {
        if (!X_Ref_C_Payment_Trx_Type.isValid(TrxType))
        throw new IllegalArgumentException ("TrxType Invalid value - " + TrxType + " - Reference_ID=215 - A - C - D - F - S - V");
        set_Value ("TrxType", TrxType);
        
    }
    
    /** Get Transaction Type.
    @return Type of credit card transaction */
    public String getTrxType() 
    {
        return (String)get_Value("TrxType");
        
    }
    
    /** Set Voice authorization code.
    @param VoiceAuthCode Voice Authorization Code from credit card company */
    public void setVoiceAuthCode (String VoiceAuthCode)
    {
        set_Value ("VoiceAuthCode", VoiceAuthCode);
        
    }
    
    /** Get Voice authorization code.
    @return Voice Authorization Code from credit card company */
    public String getVoiceAuthCode() 
    {
        return (String)get_Value("VoiceAuthCode");
        
    }
    
    /** Set Write-off Amount.
    @param WriteOffAmt Amount to write-off */
    public void setWriteOffAmt (java.math.BigDecimal WriteOffAmt)
    {
        set_Value ("WriteOffAmt", WriteOffAmt);
        
    }
    
    /** Get Write-off Amount.
    @return Amount to write-off */
    public java.math.BigDecimal getWriteOffAmt() 
    {
        return get_ValueAsBigDecimal("WriteOffAmt");
        
    }
    
    
}
