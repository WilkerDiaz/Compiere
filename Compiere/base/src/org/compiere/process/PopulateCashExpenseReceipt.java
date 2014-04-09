/**
 * 
 */
package org.compiere.process;

import org.compiere.api.MigrationStepInterface;
import org.compiere.model.MCashExpenseReceiptType;
import org.compiere.model.MClient;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.QueryUtil;
import org.compiere.util.Trx;

/**
 * @author admin
 *
 */
public class PopulateCashExpenseReceipt implements MigrationStepInterface {

	/* (non-Javadoc)
	 * @see org.compiere.api.MigrationStepInterface#executeStep()
	 */
	@Override
	public String executeStep() {
		Ctx m_ctx = Env.getCtx();
		Trx m_trx = Trx.get("Cash");
		
		MClient[] clients = MClient.getAll(m_ctx);
		for(MClient client : clients)
		{
			// Don't do anything for System tenant
			if(client.getAD_Client_ID() == 0)
				continue;
			//Does records exist in the table. If Yes, then don't do anything. Else Create Default records
			String sql = "SELECT COUNT(1) FROM C_Cash_ExpenseReceiptType WHERE AD_Client_ID = ? ";
			int count = QueryUtil.getSQLValue(m_trx, sql,client.getAD_Client_ID());
			if(count>0)
				continue;
			
			MCashExpenseReceiptType expense = new MCashExpenseReceiptType(m_ctx,0,m_trx);
			expense.setAD_Client_ID(client.getAD_Client_ID());
			expense.setAD_Org_ID(0);
			expense.setName("General Expenses");
			expense.setDescription("General Expenses");
			expense.setIsActive(true);
			expense.setIsDefault(true);
			expense.setIsExpense(true);
			try
			{
				expense.save();
			}
			catch (Exception e)
			{
				m_trx.rollback();
				return "Default Expense/Receipt Type Not inserted " + e.toString();
			}
			
			MCashExpenseReceiptType receipt = new MCashExpenseReceiptType(m_ctx,0,m_trx);
			receipt.setAD_Client_ID(client.getAD_Client_ID());
			receipt.setAD_Org_ID(0);
			receipt.setName("General Receipts");
			receipt.setDescription("General Receipts");
			receipt.setIsActive(true);
			receipt.setIsDefault(true);
			receipt.setIsExpense(false);
			try
			{
				receipt.save();
			}
			catch (Exception e)
			{
				m_trx.rollback();
				return "Default Expense/Receipt Type Not inserted " + e.toString();
			}
			
			m_trx.commit();
			
			//update the existing cash journals
			String sql1 = " UPDATE C_CashLine line SET C_Cash_ExpenseReceiptType_ID = "
				       + " (SELECT MIN(a.C_Cash_ExpenseReceiptType_ID) "
				        + " FROM C_Cash_ExpenseReceiptType a "
					    + " WHERE a.IsDefault = 'Y' "
					    + " AND a.AD_Client_ID = line.AD_Client_ID "
					    + " AND IsExpense = CASE WHEN line.CashType = 'E' THEN 'Y' "
					                         + " WHEN line.CashType = 'R' THEN 'N' "
					                         + " ELSE 'Z' END) "
					   + " WHERE line.CashType IN ('E','R')" 
					   + " AND line.AD_Client_ID = ? "
					   + " AND EXISTS (SELECT 1 FROM C_Cash_ExpenseReceiptType WHERE AD_Client_ID = ? ) ";
			
			try
			{
				count = DB.executeUpdate(m_trx, sql1,client.getAD_Client_ID(),client.getAD_Client_ID());
			}
			catch (Exception e)
			{
				m_trx.rollback();
				return "Unable to update Cash Journal Lines " + e.toString();
			}
			
			if(count<0)
				m_trx.rollback();
			else
				m_trx.commit();
		}	
	return null;
	}
}