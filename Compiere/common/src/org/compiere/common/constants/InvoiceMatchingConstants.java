package org.compiere.common.constants;

public class InvoiceMatchingConstants
{

	public static final String MATCH_FROM = "MatchFrom";
	public static final String MATCH_TO = "MatchTo";
	public static final String MATCH_MODE = "MatchMode";
	public static final String BPARTNER = "C_BPartner_ID";
	public static final String PRODUCT = "M_Product_ID";
	public static final String DATE_FROM = "DateFrom";
	public static final String DATE_TO = "DateTo";
	public static final String SAME_BPARTNER = "SameBPartner";
	public static final String SAME_PRODUCT = "SameProduct";
	public static final String SAME_QUANTITY = "SameQuantity";
	public static final String TOBEMATCHED = "ToBeMatched";
	public static final String MATCHING = "Matching";
	public static final String DIFFERENCE = "Difference";
	public static final String ISRETURNTRX = "IsReturnTrx";

	public static final String[] MATCH_OPTIONS = { "Invoice", "Order", "Receipt" };

	public static final String MATCH_UNMATCHED = "NotMatched";
	public static final String MATCH_MATCHED = "Matched";

	public static final int MODE_INVOICE_TO_RECEIPT = 0;
	public static final int MODE_RECEIPT_TO_INVOICE = 1;
	public static final int MODE_RECEIPT_TO_ORDER = 2;
	public static final int MODE_ORDER_TO_RECEIPT = 3;

	public static final int I_INVOICE = 0;
	public static final int I_ORDER = 1;
	public static final int I_RECEIPT = 2;

	public static final String[] QTY_COLUMN = { "QtyInvoiced", // I_INVOICE
			"QtyOrdered", // I_ORDER
			"MovementQty", // I_RECEIPT
			"QtyMatched", // I_INVOICE matched qty
			"QtyMatched", // I_ORDER matched qty
			"QtyMatched" // I_RECEIPT matched qty
	};

	public static final int[][] MODE_INDEX = { { I_INVOICE, I_RECEIPT }, // MODE_INVOICE_TO_RECEIPT
			{ I_RECEIPT, I_INVOICE }, // MODE_RECEIPT_TO_INVOICE
			{ I_RECEIPT, I_ORDER }, // MODE_RECEIPT_TO_ORDER
			{ I_ORDER, I_RECEIPT } // MODE_ORDER_TO_RECEIPT
	};

	public static int TABNO_FROM = 1;
	public static int TABNO_TO = 3;

}
