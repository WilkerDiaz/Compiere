package org.compiere.common.constants;

/*
 * Constants used in Payment Selection form and Payment Print form
 */
public class PaymentPrintConstants {

	// AD_Message
	public static final String EFT = "EFT";
	public static final String EXPORT = "Export";
	public static final String NUMPAYMENTS = "NoOfPayments";
	public static final String PARTNER = "BPartner";
	public static final String PRINT = "Print";
	public static final String REMAINING = "Remaining";
	public static final String SELECTED = "Selected";
	
	// AD_Element
	public static final String BANKACCOUNT = "BankAccountNo";
	public static final String BANKACCOUNTID = "C_BankAccount_ID";
	public static final String CURRENCY = "C_Currency_ID";
	public static final String CURRENTBALANCE = "CurrentBalance";
	public static final String DOCUMENTNUM = "DocumentNo";
	public static final String ONLYDUE = "OnlyDue";
	public static final String PAYDATE = "PayDate";
	public static final String PAYMENTMETHOD = "PaymentRule";
	public static final String PAYMENTRULE = "PaymentRule";
	public static final String PAYMENTSELECTION = "C_PaySelection_ID";
	public static final String PAYSELECTIONLINE = "C_PaySelectionLine_ID";
	public static final String SHOWPRINTEDPAY = "ShowPrintedPayments";
	
	// DocumentNo and OnlyDue are in both element and message
	
	public static final int EXPORTMODE = 1;
	public static final int CONFIRMMODE = 2;
	public static final int PRINTMODE = 3;
	public static final int REMITMODE = 4;
	public static final int CANCELMODE = 5;
	
}
