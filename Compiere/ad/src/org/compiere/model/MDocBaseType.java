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

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.api.*;
import org.compiere.util.*;

/**
 * 	Document Base Type Model
 *	@author Jorg Janke
 */
public class MDocBaseType extends X_C_DocBaseType
{
    /** Logger for class MDocBaseType */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MDocBaseType.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get all base types for all tenants
	 *	@param ctx context
	 *	@return array of base doc types
	 */
	public static MDocBaseType[] getAll(Ctx ctx)
	{
		if (s_docBaseTypes != null)
			return s_docBaseTypes;
		
		ArrayList<MDocBaseType> list = new ArrayList<MDocBaseType>();
		String sql = "SELECT * FROM C_DocBaseType WHERE IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MDocBaseType(ctx, rs, null));
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		s_docBaseTypes = new MDocBaseType[list.size()];
		list.toArray(s_docBaseTypes);
		return s_docBaseTypes;
	}	//	getAll

	/**
	 * 	Get Base Type for Table
	 *	@param ctx context
	 *	@param AD_Table_ID table
	 *  @param C_DocBaseType_ID Base Document type
	 *	@return base type or null
	 */
	public static MDocBaseType getForTable(Ctx ctx, int AD_Table_ID,int C_DocBaseType_ID)
	{
		if (s_docBaseTypes == null)
			getAll(ctx);
		for (MDocBaseType dbt : s_docBaseTypes)
		{
			if (dbt.getAD_Table_ID() == AD_Table_ID)
			{
				if(C_DocBaseType_ID == 0)
					return dbt;
				else if(dbt.getC_DocBaseType_ID() == C_DocBaseType_ID)
					return dbt;
			}
		}
		return null;
	}	//	getForTable

	/**	Logger						*/
	private static CLogger s_log = CLogger.getCLogger(MDocBaseType.class);
	/** Document Base Types			*/
	private static MDocBaseType[]		s_docBaseTypes = null;

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_DocBaseType_ID id
	 *	@param trx p_trx
	 */
	public MDocBaseType(Ctx ctx, int C_DocBaseType_ID, Trx trx)
	{
		super(ctx, C_DocBaseType_ID, trx);
	}	//	MDocBaseType

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MDocBaseType(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MDocBaseType

	/** AP Credit Memo = APC */
	public static final String DOCBASETYPE_APCreditMemo = "APC";
	/** AP Invoice = API */
	public static final String DOCBASETYPE_APInvoice = "API";
	/** AP Payment = APP */
	public static final String DOCBASETYPE_APPayment = "APP";
	/** AR Credit Memo = ARC */
	public static final String DOCBASETYPE_ARCreditMemo = "ARC";
	/** AR Pro Forma Invoice = ARF */
	public static final String DOCBASETYPE_ARProFormaInvoice = "ARF";
	/** AR Invoice = ARI */
	public static final String DOCBASETYPE_ARInvoice = "ARI";
	/** AR Receipt = ARR */
	public static final String DOCBASETYPE_ARReceipt = "ARR";
	/** Payment Allocation = CMA */
	public static final String DOCBASETYPE_PaymentAllocation = "CMA";
	/** Bank Statement = CMB */
	public static final String DOCBASETYPE_BankStatement = "CMB";
	/** Cash Journal = CMC */
	public static final String DOCBASETYPE_CashJournal = "CMC";
	/** GL Document = GLD */
	public static final String DOCBASETYPE_GLDocument = "GLD";
	/** GL Journal = GLJ */
	public static final String DOCBASETYPE_GLJournal = "GLJ";
	/** Material Physical Inventory = MMI */
	public static final String DOCBASETYPE_MaterialPhysicalInventory = "MMI";
	/** Material Movement = MMM */
	public static final String DOCBASETYPE_MaterialMovement = "MMM";
	/** Material Production = MMP */
	public static final String DOCBASETYPE_MaterialProduction = "MMP";
	/** Material Receipt = MMR */
	public static final String DOCBASETYPE_MaterialReceipt = "MMR";
	/** Material Delivery = MMS */
	public static final String DOCBASETYPE_MaterialDelivery = "MMS";
	/** Material Movement = PUT */
	public static final String DOCBASETYPE_MaterialPutaway = "PUT";
	/** Material Movement = PCK */
	public static final String DOCBASETYPE_MaterialPick = "PCK";
	/** Material Movement = RPL */
	public static final String DOCBASETYPE_MaterialReplenishment = "RPL";
	/** Match Invoice = MXI */
	public static final String DOCBASETYPE_MatchInvoice = "MXI";
	/** Match PO = MXP */
	public static final String DOCBASETYPE_MatchPO = "MXP";
	/** Project Issue = PJI */
	public static final String DOCBASETYPE_ProjectIssue = "PJI";
	/** Purchase Order = POO */
	public static final String DOCBASETYPE_PurchaseOrder = "POO";
	/** Purchase Requisition = POR */
	public static final String DOCBASETYPE_PurchaseRequisition = "POR";
	/** Sales Order = SOO */
	public static final String DOCBASETYPE_SalesOrder = "SOO";
	/** Work Order = WOO */
	public static final String DOCBASETYPE_WorkOrder = "WOO";
	/** Work Order Transaction = WOT */
	public static final String DOCBASETYPE_WorkOrderTransaction = "WOT";
	/** Standard Cost Update = SCU **/
	public static final String DOCBASETYPE_StandardCostUpdate = "SCU";


	/**
	 * 	Set Document Base Type
	 * 	@param DocBaseType type
	 */
	@Override
	public void setDocBaseType(String DocBaseType)
	{
		if (DocBaseType != null)
			super.setDocBaseType(DocBaseType.toUpperCase());
	}	//	setDocBaseType

	/**
	 * 	Check Document Base Type
	 *	@return true if ok
	 */
	public boolean checkDocBaseType()
	{
		String s = getDocBaseType();
		if ((s == null) || (s.length() != 3))
			return false;
		if (!s.equals(s.toUpperCase()))
			setDocBaseType(s.toUpperCase());
		return true;
	}	//	checkDocBaseType


	/**
	 * 	Get Table ID.
	 * 	@return table ID
	 */
	@Override
	public int getAD_Table_ID()
	{
	//	Multiple mappings of table->type in FactAcctReset
		int AD_Table_ID = super.getAD_Table_ID();
		if (AD_Table_ID > 0)
			return AD_Table_ID;
		//
		String dbt = getDocBaseType();
		if (dbt.equals(DOCBASETYPE_SalesOrder) || dbt.equals(DOCBASETYPE_PurchaseOrder))
			return X_C_Order.Table_ID;
		if (dbt.equals(DOCBASETYPE_MaterialReceipt) || dbt.equals(DOCBASETYPE_MaterialDelivery))
			return X_M_InOut.Table_ID;
		if (dbt.equals(DOCBASETYPE_APInvoice) || dbt.equals(DOCBASETYPE_APCreditMemo)
				|| dbt.equals(DOCBASETYPE_ARInvoice) || dbt.equals(DOCBASETYPE_ARCreditMemo)
				|| dbt.equals(DOCBASETYPE_ARProFormaInvoice))
			return X_C_Invoice.Table_ID;
		if (dbt.equals(DOCBASETYPE_APPayment) || dbt.equals(DOCBASETYPE_ARReceipt))
			return X_C_Payment.Table_ID;
		if (dbt.equals(DOCBASETYPE_PaymentAllocation))
			return X_C_AllocationHdr.Table_ID;
		if (dbt.equals(DOCBASETYPE_CashJournal))
			return X_C_Cash.Table_ID;
		if (dbt.equals(DOCBASETYPE_BankStatement))
			return X_C_BankStatement.Table_ID;
		if (dbt.equals(DOCBASETYPE_MaterialPhysicalInventory))
			return X_M_Inventory.Table_ID;
		if (dbt.equals(DOCBASETYPE_MaterialMovement))
			return X_M_Movement.Table_ID;
		if (dbt.equals(DOCBASETYPE_MaterialPick) || dbt.equals(DOCBASETYPE_MaterialPutaway)
				||dbt.equals(DOCBASETYPE_MaterialReplenishment))
			return X_M_WarehouseTask.Table_ID;
		if (dbt.equals(DOCBASETYPE_MaterialProduction))
			return X_M_Production.Table_ID;
		if (dbt.equals(DOCBASETYPE_GLJournal))
			return X_GL_Journal.Table_ID;
		if (dbt.equals(DOCBASETYPE_GLDocument))
			return 0;
		if (dbt.equals(DOCBASETYPE_MatchInvoice))
			return X_M_MatchInv.Table_ID;
		if (dbt.equals(DOCBASETYPE_MatchPO))
			return X_M_MatchPO.Table_ID;
		if (dbt.equals(DOCBASETYPE_ProjectIssue))
			return X_C_ProjectIssue.Table_ID;
		if (dbt.equals(DOCBASETYPE_PurchaseRequisition))
			return X_M_Requisition.Table_ID;
		if (dbt.equals(DOCBASETYPE_WorkOrder))
			return X_M_WorkOrder.Table_ID;
		if (dbt.equals(DOCBASETYPE_WorkOrderTransaction))
			return X_M_WorkOrderTransaction.Table_ID;
		if (dbt.equals(DOCBASETYPE_StandardCostUpdate))
			return X_M_CostUpdate.Table_ID;

		//	Error
		log.warning("No AD_Table_ID for " + getName() + " (DocBaseType=" + dbt + ")");
		return 0;
	}	//	getAD_Table_ID

	/**
	 * 	Get Table Name
	 *	@return table name
	 */
	public String getTableName()
	{
		int AD_Table_ID = getAD_Table_ID();
		if (AD_Table_ID == 0)
			return null;
		//
		if (AD_Table_ID == X_C_Order.Table_ID)
			return X_C_Order.Table_Name;
		if (AD_Table_ID == X_M_InOut.Table_ID)
			return X_M_InOut.Table_Name;
		if (AD_Table_ID == X_C_Invoice.Table_ID)
			return X_C_Invoice.Table_Name;
		if (AD_Table_ID == X_C_Payment.Table_ID)
			return X_C_Payment.Table_Name;
		if (AD_Table_ID == X_C_AllocationHdr.Table_ID)
			return X_C_AllocationHdr.Table_Name;
		if (AD_Table_ID == X_C_Cash.Table_ID)
			return X_C_Cash.Table_Name;
		if (AD_Table_ID == X_C_BankStatement.Table_ID)
			return X_C_BankStatement.Table_Name;
		if (AD_Table_ID == X_M_Inventory.Table_ID)
			return X_M_Inventory.Table_Name;
		if (AD_Table_ID == X_M_Movement.Table_ID)
			return X_M_Movement.Table_Name;
		if (AD_Table_ID == X_M_WarehouseTask.Table_ID)
			return X_M_WarehouseTask.Table_Name;
		if (AD_Table_ID == X_GL_Journal.Table_ID)
			return X_GL_Journal.Table_Name;
		if (AD_Table_ID == X_M_MatchInv.Table_ID)
			return X_M_MatchInv.Table_Name;
		if (AD_Table_ID == X_M_MatchPO.Table_ID)
			return X_M_MatchPO.Table_Name;
		if (AD_Table_ID == X_C_ProjectIssue.Table_ID)
			return X_C_ProjectIssue.Table_Name;
		if (AD_Table_ID == X_M_Requisition.Table_ID)
			return X_M_Requisition.Table_Name;
		if (AD_Table_ID == X_M_WorkOrder.Table_ID)
			return X_M_WorkOrder.Table_Name;
		if (AD_Table_ID == X_M_WorkOrderTransaction.Table_ID)
			return X_M_WorkOrderTransaction.Table_Name;
		if (AD_Table_ID == X_M_CostUpdate.Table_ID )
			return X_M_CostUpdate.Table_Name;
		//
		return MTable.getTableName(getCtx(), AD_Table_ID);
	}	//	getTableName

	/**
	 * 	Get Class Name
	 * 	@return class name
	 */
	@Override
	public String getAccountingClassname()
	{
		String className = super.getAccountingClassname();
		if ((className != null) && (className.length() > 0))
			return className;
		//
		String dbt = getDocBaseType();
		if (dbt.equals(DOCBASETYPE_SalesOrder) || dbt.equals(DOCBASETYPE_PurchaseOrder))
			return "org.compiere.acct.Doc_Order";
		if (dbt.equals(DOCBASETYPE_MaterialReceipt) || dbt.equals(DOCBASETYPE_MaterialDelivery))
			return "org.compiere.acct.Doc_InOut";
		if (dbt.equals(DOCBASETYPE_APInvoice) || dbt.equals(DOCBASETYPE_APCreditMemo)
			|| dbt.equals(DOCBASETYPE_ARInvoice) || dbt.equals(DOCBASETYPE_ARCreditMemo)
			|| dbt.equals(DOCBASETYPE_ARProFormaInvoice))
			return "org.compiere.acct.Doc_Invoice";
		if (dbt.equals(DOCBASETYPE_APPayment) || dbt.equals(DOCBASETYPE_ARReceipt))
			return "org.compiere.acct.Doc_Payment";
		if (dbt.equals(DOCBASETYPE_PaymentAllocation))
			return "org.compiere.acct.Doc_Allocation";
		if (dbt.equals(DOCBASETYPE_CashJournal))
			return "org.compiere.acct.Doc_Cash";
		if (dbt.equals(DOCBASETYPE_BankStatement))
			return "org.compiere.acct.Doc_Bank";
		if (dbt.equals(DOCBASETYPE_MaterialPhysicalInventory))
			return "org.compiere.acct.Doc_Inventory";
		if (dbt.equals(DOCBASETYPE_MaterialMovement))
			return "org.compiere.acct.Doc_Movement";
		if (dbt.equals(DOCBASETYPE_MaterialPick) || dbt.equals(DOCBASETYPE_MaterialPutaway)
				||dbt.equals(DOCBASETYPE_MaterialReplenishment))
			return "org.compiere.cwms.acct.Doc_WarehouseTask";
		if (dbt.equals(DOCBASETYPE_MaterialProduction))
			return "org.compiere.acct.Doc_Production";
		if (dbt.equals(DOCBASETYPE_GLJournal))
			return "org.compiere.acct.Doc_GLJournal";
		if (dbt.equals(DOCBASETYPE_GLDocument))
			return null;
		if (dbt.equals(DOCBASETYPE_MatchInvoice))
			return "org.compiere.acct.Doc_MatchInv";
		if (dbt.equals(DOCBASETYPE_MatchPO))
			return "org.compiere.acct.Doc_MatchPO";
		if (dbt.equals(DOCBASETYPE_ProjectIssue))
			return "org.compiere.acct.Doc_ProjectIssue";
		if (dbt.equals(DOCBASETYPE_PurchaseRequisition))
			return "org.compiere.acct.Doc_Requisition";
		if (dbt.equals(DOCBASETYPE_WorkOrder))
			return "org.compiere.cmfg.acct.Doc_WorkOrder";
		if (dbt.equals(DOCBASETYPE_WorkOrderTransaction))
			return "org.compiere.cmfg.acct.Doc_WorkOrderTransaction";
		if (dbt.equals(DOCBASETYPE_StandardCostUpdate))
			return "org.compiere.acct.Doc_CostUpdate";
		//	Error
		return null;
	}	//	getAccountingClassname

	/**
	 * 	Get Class
	 *	@return class or null
	 */
	protected Class<?> getAccountingClass()
	{
		String className = getAccountingClassname();
		if ((className == null) || (className.length() == 0))
		{
			log.warning("No ClassName defined");
			return null;
		}
		try
		{
			Class<?> clazz = Class.forName(className);
			return clazz;
		}
		catch (Exception e)
		{
			log.warning("Error creating class for " + getName() + ": - " + e.toString());
		}
		return null;
	}	//	getAccountingClass

	/**
	 * 	Get Accounting Class
	 *	@param ass accounting schema array
	 *	@param rs result set
	 *	@param trx p_trx
	 *	@return instance or null
	 */
	public AccountingInterface getAccountingInstance(MAcctSchema[] ass, ResultSet rs, Trx trx)
	{
		Class<?> clazz = getAccountingClass();
		if (clazz == null)
			return null;

		try
		{
			Constructor<?> constr = clazz.getConstructor(MAcctSchema[].class, ResultSet.class, Trx.class);
			AccountingInterface retValue = (AccountingInterface)constr.newInstance(ass, rs, trx);
			return retValue;
		}
		catch (Exception e)
		{
			log.warning("Error instantiating " + getName() + ": - " + e.toString());
		}
		return null;
	}	//	getAccountingInstance


	/**
	 *	Before Save
	 *	@param newRecord new
	 *	@return true if can be saved
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		if (getAD_Table_ID() == 0)
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "AD_Table_ID"));
			return false;
		}
		String s = getAccountingClassname();
		if ((s == null) || (s.length() == 0))
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "AccountingClassname"));
			return false;
		}
		if (!checkDocBaseType())
			return false;
		return true;
	}	//	beforeSave

	/**
	 * 	String Representation
	 * 	@param info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MDocBaseType[")
			.append(getName())
			.append(",AD_Table_ID=").append(getAD_Table_ID())
			.append(", Class=").append(getAccountingClassname())
			.append("]");
		return sb.toString();
	}	//	toString

}	//	MDocBaseType
