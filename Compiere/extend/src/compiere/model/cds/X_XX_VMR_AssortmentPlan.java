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
package compiere.model.cds;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VMR_AssortmentPlan
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_AssortmentPlan extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_AssortmentPlan_ID id
    @param trx transaction
    */
    public X_XX_VMR_AssortmentPlan (Ctx ctx, int XX_VMR_AssortmentPlan_ID, Trx trx)
    {
        super (ctx, XX_VMR_AssortmentPlan_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_AssortmentPlan_ID == 0)
        {
            setXX_VMR_ASSORTMENTPLAN_ID (0);
            setXX_VMR_ComercialBudgetTab_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_AssortmentPlan (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27564361255789L;
    /** Last Updated Timestamp 2010-08-19 12:08:59.0 */
    public static final long updatedMS = 1282235939000L;
    /** AD_Table_ID=1000323 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_AssortmentPlan");
        
    }
    ;
    
    /** TableName=XX_VMR_AssortmentPlan */
    public static final String Table_Name="XX_VMR_AssortmentPlan";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Period.
    @param C_Period_ID Period of the Calendar */
    public void setC_Period_ID (int C_Period_ID)
    {
        if (C_Period_ID <= 0) set_Value ("C_Period_ID", null);
        else
        set_Value ("C_Period_ID", Integer.valueOf(C_Period_ID));
        
    }
    
    /** Get Period.
    @return Period of the Calendar */
    public int getC_Period_ID() 
    {
        return get_ValueAsInt("C_Period_ID");
        
    }
    
    /** Set Budgeted Final Inventory.
    @param XX_BudFinalInventoryPiece Budgeted Final Inventory */
    public void setXX_BudFinalInventoryPiece (int XX_BudFinalInventoryPiece)
    {
        set_Value ("XX_BudFinalInventoryPiece", Integer.valueOf(XX_BudFinalInventoryPiece));
        
    }
    
    /** Get Budgeted Final Inventory.
    @return Budgeted Final Inventory */
    public int getXX_BudFinalInventoryPiece() 
    {
        return get_ValueAsInt("XX_BudFinalInventoryPiece");
        
    }
    
    /** Set Budgeted Final Inventory.
    @param XX_BudgetedFinalInventory Budgeted Final Inventory */
    public void setXX_BudgetedFinalInventory (java.math.BigDecimal XX_BudgetedFinalInventory)
    {
        set_Value ("XX_BudgetedFinalInventory", XX_BudgetedFinalInventory);
        
    }
    
    /** Get Budgeted Final Inventory.
    @return Budgeted Final Inventory */
    public java.math.BigDecimal getXX_BudgetedFinalInventory() 
    {
        return get_ValueAsBigDecimal("XX_BudgetedFinalInventory");
        
    }
    
    /** Set Budgetted Final Inventory.
    @param XX_BUDGETEDFINALINVENTORYBEST Budgetted Final Inventory */
    public void setXX_BUDGETEDFINALINVENTORYBEST (java.math.BigDecimal XX_BUDGETEDFINALINVENTORYBEST)
    {
        set_Value ("XX_BUDGETEDFINALINVENTORYBEST", XX_BUDGETEDFINALINVENTORYBEST);
        
    }
    
    /** Get Budgetted Final Inventory.
    @return Budgetted Final Inventory */
    public java.math.BigDecimal getXX_BUDGETEDFINALINVENTORYBEST() 
    {
        return get_ValueAsBigDecimal("XX_BUDGETEDFINALINVENTORYBEST");
        
    }
    
    /** Set Budgetted Final Inventory.
    @param XX_BUDGETEDFINALINVENTORYOK Budgetted Final Inventory */
    public void setXX_BUDGETEDFINALINVENTORYOK (java.math.BigDecimal XX_BUDGETEDFINALINVENTORYOK)
    {
        set_Value ("XX_BUDGETEDFINALINVENTORYOK", XX_BUDGETEDFINALINVENTORYOK);
        
    }
    
    /** Get Budgetted Final Inventory.
    @return Budgetted Final Inventory */
    public java.math.BigDecimal getXX_BUDGETEDFINALINVENTORYOK() 
    {
        return get_ValueAsBigDecimal("XX_BUDGETEDFINALINVENTORYOK");
        
    }
    
    /** Set Budgetted Final Inventory.
    @param XX_BUDGETEDFINALINVENTORYOPT Budgetted Final Inventory */
    public void setXX_BUDGETEDFINALINVENTORYOPT (java.math.BigDecimal XX_BUDGETEDFINALINVENTORYOPT)
    {
        set_Value ("XX_BUDGETEDFINALINVENTORYOPT", XX_BUDGETEDFINALINVENTORYOPT);
        
    }
    
    /** Get Budgetted Final Inventory.
    @return Budgetted Final Inventory */
    public java.math.BigDecimal getXX_BUDGETEDFINALINVENTORYOPT() 
    {
        return get_ValueAsBigDecimal("XX_BUDGETEDFINALINVENTORYOPT");
        
    }
    
    /** Set Decrease.
    @param XX_Decrease Decrease */
    public void setXX_Decrease (java.math.BigDecimal XX_Decrease)
    {
        set_Value ("XX_Decrease", XX_Decrease);
        
    }
    
    /** Get Decrease.
    @return Decrease */
    public java.math.BigDecimal getXX_Decrease() 
    {
        return get_ValueAsBigDecimal("XX_Decrease");
        
    }
    
    /** Set Decrease.
    @param XX_DECREASEBEST Decrease */
    public void setXX_DECREASEBEST (java.math.BigDecimal XX_DECREASEBEST)
    {
        set_Value ("XX_DECREASEBEST", XX_DECREASEBEST);
        
    }
    
    /** Get Decrease.
    @return Decrease */
    public java.math.BigDecimal getXX_DECREASEBEST() 
    {
        return get_ValueAsBigDecimal("XX_DECREASEBEST");
        
    }
    
    /** Set Decrease.
    @param XX_DECREASEOK Decrease */
    public void setXX_DECREASEOK (java.math.BigDecimal XX_DECREASEOK)
    {
        set_Value ("XX_DECREASEOK", XX_DECREASEOK);
        
    }
    
    /** Get Decrease.
    @return Decrease */
    public java.math.BigDecimal getXX_DECREASEOK() 
    {
        return get_ValueAsBigDecimal("XX_DECREASEOK");
        
    }
    
    /** Set Decrease.
    @param XX_DECREASEOPT Decrease */
    public void setXX_DECREASEOPT (java.math.BigDecimal XX_DECREASEOPT)
    {
        set_Value ("XX_DECREASEOPT", XX_DECREASEOPT);
        
    }
    
    /** Get Decrease.
    @return Decrease */
    public java.math.BigDecimal getXX_DECREASEOPT() 
    {
        return get_ValueAsBigDecimal("XX_DECREASEOPT");
        
    }
    
    /** Set Decrease.
    @param XX_DecreasePieces Decrease */
    public void setXX_DecreasePieces (int XX_DecreasePieces)
    {
        set_Value ("XX_DecreasePieces", Integer.valueOf(XX_DecreasePieces));
        
    }
    
    /** Get Decrease.
    @return Decrease */
    public int getXX_DecreasePieces() 
    {
        return get_ValueAsInt("XX_DecreasePieces");
        
    }
    
    /** Set Discount ?.
    @param XX_Discount Discount Application */
    public void setXX_Discount (java.math.BigDecimal XX_Discount)
    {
        set_Value ("XX_Discount", XX_Discount);
        
    }
    
    /** Get Discount ?.
    @return Discount Application */
    public java.math.BigDecimal getXX_Discount() 
    {
        return get_ValueAsBigDecimal("XX_Discount");
        
    }
    
    /** Set Rebates.
    @param XX_DISCOUNTBEST Rebates */
    public void setXX_DISCOUNTBEST (java.math.BigDecimal XX_DISCOUNTBEST)
    {
        set_Value ("XX_DISCOUNTBEST", XX_DISCOUNTBEST);
        
    }
    
    /** Get Rebates.
    @return Rebates */
    public java.math.BigDecimal getXX_DISCOUNTBEST() 
    {
        return get_ValueAsBigDecimal("XX_DISCOUNTBEST");
        
    }
    
    /** Set Rebates.
    @param XX_DISCOUNTOK Rebates */
    public void setXX_DISCOUNTOK (java.math.BigDecimal XX_DISCOUNTOK)
    {
        set_Value ("XX_DISCOUNTOK", XX_DISCOUNTOK);
        
    }
    
    /** Get Rebates.
    @return Rebates */
    public java.math.BigDecimal getXX_DISCOUNTOK() 
    {
        return get_ValueAsBigDecimal("XX_DISCOUNTOK");
        
    }
    
    /** Set Rebates.
    @param XX_DISCOUNTOPT Rebates */
    public void setXX_DISCOUNTOPT (java.math.BigDecimal XX_DISCOUNTOPT)
    {
        set_Value ("XX_DISCOUNTOPT", XX_DISCOUNTOPT);
        
    }
    
    /** Get Rebates.
    @return Rebates */
    public java.math.BigDecimal getXX_DISCOUNTOPT() 
    {
        return get_ValueAsBigDecimal("XX_DISCOUNTOPT");
        
    }
    
    /** Set Employee Discount.
    @param XX_EMPLOYEDISCOUNTBEST Employee Discount */
    public void setXX_EMPLOYEDISCOUNTBEST (java.math.BigDecimal XX_EMPLOYEDISCOUNTBEST)
    {
        set_Value ("XX_EMPLOYEDISCOUNTBEST", XX_EMPLOYEDISCOUNTBEST);
        
    }
    
    /** Get Employee Discount.
    @return Employee Discount */
    public java.math.BigDecimal getXX_EMPLOYEDISCOUNTBEST() 
    {
        return get_ValueAsBigDecimal("XX_EMPLOYEDISCOUNTBEST");
        
    }
    
    /** Set Employee Discount.
    @param XX_EMPLOYEDISCOUNTOK Employee Discount */
    public void setXX_EMPLOYEDISCOUNTOK (java.math.BigDecimal XX_EMPLOYEDISCOUNTOK)
    {
        set_Value ("XX_EMPLOYEDISCOUNTOK", XX_EMPLOYEDISCOUNTOK);
        
    }
    
    /** Get Employee Discount.
    @return Employee Discount */
    public java.math.BigDecimal getXX_EMPLOYEDISCOUNTOK() 
    {
        return get_ValueAsBigDecimal("XX_EMPLOYEDISCOUNTOK");
        
    }
    
    /** Set Employee Discount.
    @param XX_EMPLOYEDISCOUNTOPT Employee Discount */
    public void setXX_EMPLOYEDISCOUNTOPT (java.math.BigDecimal XX_EMPLOYEDISCOUNTOPT)
    {
        set_Value ("XX_EMPLOYEDISCOUNTOPT", XX_EMPLOYEDISCOUNTOPT);
        
    }
    
    /** Get Employee Discount.
    @return Employee Discount */
    public java.math.BigDecimal getXX_EMPLOYEDISCOUNTOPT() 
    {
        return get_ValueAsBigDecimal("XX_EMPLOYEDISCOUNTOPT");
        
    }
    
    /** Set Employee Discount.
    @param XX_EmployeeDiscount Employee Discount */
    public void setXX_EmployeeDiscount (java.math.BigDecimal XX_EmployeeDiscount)
    {
        set_Value ("XX_EmployeeDiscount", XX_EmployeeDiscount);
        
    }
    
    /** Get Employee Discount.
    @return Employee Discount */
    public java.math.BigDecimal getXX_EmployeeDiscount() 
    {
        return get_ValueAsBigDecimal("XX_EmployeeDiscount");
        
    }
    
    /** Set Initial Inventory.
    @param XX_InitialInventory Initial Inventory */
    public void setXX_InitialInventory (java.math.BigDecimal XX_InitialInventory)
    {
        set_Value ("XX_InitialInventory", XX_InitialInventory);
        
    }
    
    /** Get Initial Inventory.
    @return Initial Inventory */
    public java.math.BigDecimal getXX_InitialInventory() 
    {
        return get_ValueAsBigDecimal("XX_InitialInventory");
        
    }
    
    /** Set Initial Inventory.
    @param XX_INITIALINVENTORYBEST Initial Inventory */
    public void setXX_INITIALINVENTORYBEST (java.math.BigDecimal XX_INITIALINVENTORYBEST)
    {
        set_Value ("XX_INITIALINVENTORYBEST", XX_INITIALINVENTORYBEST);
        
    }
    
    /** Get Initial Inventory.
    @return Initial Inventory */
    public java.math.BigDecimal getXX_INITIALINVENTORYBEST() 
    {
        return get_ValueAsBigDecimal("XX_INITIALINVENTORYBEST");
        
    }
    
    /** Set Initial Inventory.
    @param XX_INITIALINVENTORYOK Initial Inventory */
    public void setXX_INITIALINVENTORYOK (java.math.BigDecimal XX_INITIALINVENTORYOK)
    {
        set_Value ("XX_INITIALINVENTORYOK", XX_INITIALINVENTORYOK);
        
    }
    
    /** Get Initial Inventory.
    @return Initial Inventory */
    public java.math.BigDecimal getXX_INITIALINVENTORYOK() 
    {
        return get_ValueAsBigDecimal("XX_INITIALINVENTORYOK");
        
    }
    
    /** Set Initial Inventory.
    @param XX_INITIALINVENTORYOPT Initial Inventory */
    public void setXX_INITIALINVENTORYOPT (java.math.BigDecimal XX_INITIALINVENTORYOPT)
    {
        set_Value ("XX_INITIALINVENTORYOPT", XX_INITIALINVENTORYOPT);
        
    }
    
    /** Get Initial Inventory.
    @return Initial Inventory */
    public java.math.BigDecimal getXX_INITIALINVENTORYOPT() 
    {
        return get_ValueAsBigDecimal("XX_INITIALINVENTORYOPT");
        
    }
    
    /** Set Initial Inventory.
    @param XX_InitialInventoryPieces Initial Inventory */
    public void setXX_InitialInventoryPieces (int XX_InitialInventoryPieces)
    {
        set_Value ("XX_InitialInventoryPieces", Integer.valueOf(XX_InitialInventoryPieces));
        
    }
    
    /** Get Initial Inventory.
    @return Initial Inventory */
    public int getXX_InitialInventoryPieces() 
    {
        return get_ValueAsInt("XX_InitialInventoryPieces");
        
    }
    
    /** Set Purchases.
    @param XX_Purchases Purchases */
    public void setXX_Purchases (java.math.BigDecimal XX_Purchases)
    {
        set_Value ("XX_Purchases", XX_Purchases);
        
    }
    
    /** Get Purchases.
    @return Purchases */
    public java.math.BigDecimal getXX_Purchases() 
    {
        return get_ValueAsBigDecimal("XX_Purchases");
        
    }
    
    /** Set Purchases.
    @param XX_PURCHASESBEST Purchases */
    public void setXX_PURCHASESBEST (java.math.BigDecimal XX_PURCHASESBEST)
    {
        set_Value ("XX_PURCHASESBEST", XX_PURCHASESBEST);
        
    }
    
    /** Get Purchases.
    @return Purchases */
    public java.math.BigDecimal getXX_PURCHASESBEST() 
    {
        return get_ValueAsBigDecimal("XX_PURCHASESBEST");
        
    }
    
    /** Set Purchases.
    @param XX_PURCHASESOK Purchases */
    public void setXX_PURCHASESOK (java.math.BigDecimal XX_PURCHASESOK)
    {
        set_Value ("XX_PURCHASESOK", XX_PURCHASESOK);
        
    }
    
    /** Get Purchases.
    @return Purchases */
    public java.math.BigDecimal getXX_PURCHASESOK() 
    {
        return get_ValueAsBigDecimal("XX_PURCHASESOK");
        
    }
    
    /** Set Purchases.
    @param XX_PURCHASESOPT Purchases */
    public void setXX_PURCHASESOPT (java.math.BigDecimal XX_PURCHASESOPT)
    {
        set_Value ("XX_PURCHASESOPT", XX_PURCHASESOPT);
        
    }
    
    /** Get Purchases.
    @return Purchases */
    public java.math.BigDecimal getXX_PURCHASESOPT() 
    {
        return get_ValueAsBigDecimal("XX_PURCHASESOPT");
        
    }
    
    /** Set Purchases.
    @param XX_PurchasesPieces Purchases */
    public void setXX_PurchasesPieces (int XX_PurchasesPieces)
    {
        set_Value ("XX_PurchasesPieces", Integer.valueOf(XX_PurchasesPieces));
        
    }
    
    /** Get Purchases.
    @return Purchases */
    public int getXX_PurchasesPieces() 
    {
        return get_ValueAsInt("XX_PurchasesPieces");
        
    }
    
    /** Set Sales.
    @param XX_SalesDistribution Sales */
    public void setXX_SalesDistribution (java.math.BigDecimal XX_SalesDistribution)
    {
        set_Value ("XX_SalesDistribution", XX_SalesDistribution);
        
    }
    
    /** Get Sales.
    @return Sales */
    public java.math.BigDecimal getXX_SalesDistribution() 
    {
        return get_ValueAsBigDecimal("XX_SalesDistribution");
        
    }
    
    /** Set Sales.
    @param XX_SalesDistributionBest Sales */
    public void setXX_SalesDistributionBest (java.math.BigDecimal XX_SalesDistributionBest)
    {
        set_Value ("XX_SalesDistributionBest", XX_SalesDistributionBest);
        
    }
    
    /** Get Sales.
    @return Sales */
    public java.math.BigDecimal getXX_SalesDistributionBest() 
    {
        return get_ValueAsBigDecimal("XX_SalesDistributionBest");
        
    }
    
    /** Set Sales.
    @param XX_SalesDistributionOk Sales */
    public void setXX_SalesDistributionOk (java.math.BigDecimal XX_SalesDistributionOk)
    {
        set_Value ("XX_SalesDistributionOk", XX_SalesDistributionOk);
        
    }
    
    /** Get Sales.
    @return Sales */
    public java.math.BigDecimal getXX_SalesDistributionOk() 
    {
        return get_ValueAsBigDecimal("XX_SalesDistributionOk");
        
    }
    
    /** Set Sales.
    @param XX_SalesDistributionOpt Sales */
    public void setXX_SalesDistributionOpt (java.math.BigDecimal XX_SalesDistributionOpt)
    {
        set_Value ("XX_SalesDistributionOpt", XX_SalesDistributionOpt);
        
    }
    
    /** Get Sales.
    @return Sales */
    public java.math.BigDecimal getXX_SalesDistributionOpt() 
    {
        return get_ValueAsBigDecimal("XX_SalesDistributionOpt");
        
    }
    
    /** Set Sales.
    @param XX_SalesDistributionPiece Sales */
    public void setXX_SalesDistributionPiece (int XX_SalesDistributionPiece)
    {
        set_Value ("XX_SalesDistributionPiece", Integer.valueOf(XX_SalesDistributionPiece));
        
    }
    
    /** Get Sales.
    @return Sales */
    public int getXX_SalesDistributionPiece() 
    {
        return get_ValueAsInt("XX_SalesDistributionPiece");
        
    }
    
    /** Set XX_VMR_ASSORTMENTPLAN_ID.
    @param XX_VMR_ASSORTMENTPLAN_ID XX_VMR_ASSORTMENTPLAN_ID */
    public void setXX_VMR_ASSORTMENTPLAN_ID (int XX_VMR_ASSORTMENTPLAN_ID)
    {
        if (XX_VMR_ASSORTMENTPLAN_ID < 1) throw new IllegalArgumentException ("XX_VMR_ASSORTMENTPLAN_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_ASSORTMENTPLAN_ID", Integer.valueOf(XX_VMR_ASSORTMENTPLAN_ID));
        
    }
    
    /** Get XX_VMR_ASSORTMENTPLAN_ID.
    @return XX_VMR_ASSORTMENTPLAN_ID */
    public int getXX_VMR_ASSORTMENTPLAN_ID() 
    {
        return get_ValueAsInt("XX_VMR_ASSORTMENTPLAN_ID");
        
    }
    
    /** Set Comercial Budget Tab.
    @param XX_VMR_ComercialBudgetTab_ID Comercial Budget Tab */
    public void setXX_VMR_ComercialBudgetTab_ID (int XX_VMR_ComercialBudgetTab_ID)
    {
        if (XX_VMR_ComercialBudgetTab_ID < 1) throw new IllegalArgumentException ("XX_VMR_ComercialBudgetTab_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_ComercialBudgetTab_ID", Integer.valueOf(XX_VMR_ComercialBudgetTab_ID));
        
    }
    
    /** Get Comercial Budget Tab.
    @return Comercial Budget Tab */
    public int getXX_VMR_ComercialBudgetTab_ID() 
    {
        return get_ValueAsInt("XX_VMR_ComercialBudgetTab_ID");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        if (XX_VMR_Department_ID <= 0) set_Value ("XX_VMR_Department_ID", null);
        else
        set_Value ("XX_VMR_Department_ID", Integer.valueOf(XX_VMR_Department_ID));
        
    }
    
    /** Get Department.
    @return Department */
    public int getXX_VMR_Department_ID() 
    {
        return get_ValueAsInt("XX_VMR_Department_ID");
        
    }
    
    /** Set Line.
    @param XX_VMR_Line_ID Line */
    public void setXX_VMR_Line_ID (int XX_VMR_Line_ID)
    {
        if (XX_VMR_Line_ID <= 0) set_Value ("XX_VMR_Line_ID", null);
        else
        set_Value ("XX_VMR_Line_ID", Integer.valueOf(XX_VMR_Line_ID));
        
    }
    
    /** Get Line.
    @return Line */
    public int getXX_VMR_Line_ID() 
    {
        return get_ValueAsInt("XX_VMR_Line_ID");
        
    }
    
    
}
