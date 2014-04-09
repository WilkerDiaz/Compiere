package org.compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.QueryUtil;
import org.compiere.util.Trx;

public class MCashExpenseReceiptType extends X_C_Cash_ExpenseReceiptType {

    /** Logger for class MCashExpenseType */
    private static final CLogger log = CLogger.getCLogger(MCashExpenseReceiptType.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Cash_ExpenseType_ID id
	 *	@param trx transaction
	 */
	public MCashExpenseReceiptType (Ctx ctx, int C_Cash_ExpenseReceiptType_ID, Trx trx)
	{
		super (ctx, C_Cash_ExpenseReceiptType_ID, trx);
	}

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MCashExpenseReceiptType (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}
	
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord & success)
			success = insert_Accounting("C_Cash_ExpenseReceipt_Acct", "C_AcctSchema_Default", null);

		return success;
	}

	public static MAccount getAccount(int C_Cash_ExpenseReceiptType_ID,String AcctType, MAcctSchema as)
	{
		if (C_Cash_ExpenseReceiptType_ID == 0 || as == null)
			return null;
		
		int acct_index = 0;
		if(AcctType.equals(MCashLine.CASHTYPE_Expense))
			acct_index = 1;
		else if (AcctType.equals(MCashLine.CASHTYPE_Receipt))
			acct_index = 2;
		
		if(acct_index==0)
			return null;
		
		String sql = "SELECT CB_Expense_Acct, CB_Receipt_Acct "
			      + " FROM C_Cash_ExpenseReceipt_Acct "
			      + " WHERE C_Cash_ExpenseReceiptType_ID =? AND C_AcctSchema_ID=? ";
		int Account_ID = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, as.get_Trx());
			pstmt.setInt (1, C_Cash_ExpenseReceiptType_ID);
			pstmt.setInt (2, as.getC_AcctSchema_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
				Account_ID = rs.getInt(acct_index);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return null;
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//	No account
		if (Account_ID == 0)
		{
			log.severe ("NO account for C_Cash_ExpenseReceiptType_ID=" + C_Cash_ExpenseReceiptType_ID);
			return null;
		}

		//	Return Account
		MAccount acct = MAccount.get (as.getCtx(), Account_ID);
		return acct;

	}
	
	private boolean isUsed()
	{
		String sql = " SELECT COUNT(1) " 
			       + " FROM C_CashLine cl "
			       + " WHERE cl.C_Cash_ExpenseReceiptType_ID = ? "
		           + " AND cl.AD_Client_ID = ? ";
		int count = QueryUtil.getSQLValue(get_Trx(), sql, getC_Cash_ExpenseReceiptType_ID(),getAD_Client_ID());
		if(count>0)
			return true;
		else
			return false;
	}
	
	private ArrayList<X_C_Cash_ExpenseReceipt_Acct> getAllAccounts()
	{
		ArrayList<X_C_Cash_ExpenseReceipt_Acct> retVal = new ArrayList<X_C_Cash_ExpenseReceipt_Acct>();
		String sql = "SELECT * FROM C_Cash_ExpenseReceipt_Acct WHERE C_Cash_ExpenseReceiptType_ID = ? ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try 
		{
			pstmt = DB.prepareStatement(sql,get_Trx());
			pstmt.setInt(1, getC_Cash_ExpenseReceiptType_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				X_C_Cash_ExpenseReceipt_Acct acct = new X_C_Cash_ExpenseReceipt_Acct(getCtx(),rs,get_Trx());
				retVal.add(acct);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return null;
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return retVal;

	}
	
	@Override
	protected boolean beforeDelete ()
	{
		if (isUsed())
		{
			log.saveError("This Expense/Receipt type is in use", "This Expense/Receipt type is in use");
			return false;
		}
		
		ArrayList<X_C_Cash_ExpenseReceipt_Acct> accounts = getAllAccounts();
		for(X_C_Cash_ExpenseReceipt_Acct account : accounts)
		{
			if(!account.delete(false,get_Trx()))
			{
				log.saveError("Unable to Delete the Record", "Unable to Delete the Record");
				return false;
			}
		}
		return true;
	} 	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true if can be saved
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if(newRecord)
			return true;
		
		if(is_ValueChanged("IsExpense") && isUsed())
		{
			log.saveError("Can't update Expense Type flag", "This Record is used in Cash Book");
			return false;
		}

		return true;
	}
}
