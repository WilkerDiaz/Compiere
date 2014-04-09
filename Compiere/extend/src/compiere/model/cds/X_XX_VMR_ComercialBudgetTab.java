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
/** Generated Model for XX_VMR_ComercialBudgetTab
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_ComercialBudgetTab extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_ComercialBudgetTab_ID id
    @param trx transaction
    */
    public X_XX_VMR_ComercialBudgetTab (Ctx ctx, int XX_VMR_ComercialBudgetTab_ID, Trx trx)
    {
        super (ctx, XX_VMR_ComercialBudgetTab_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_ComercialBudgetTab_ID == 0)
        {
            setXX_VMR_ComercialBudgetTab_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_ComercialBudgetTab (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27564375151789L;
    /** Last Updated Timestamp 2010-08-19 16:00:35.0 */
    public static final long updatedMS = 1282249835000L;
    /** AD_Table_ID=1000324 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_ComercialBudgetTab");
        
    }
    ;
    
    /** TableName=XX_VMR_ComercialBudgetTab */
    public static final String Table_Name="XX_VMR_ComercialBudgetTab";
    
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
    
    /** Set Cobert.
    @param XX_Cobert Cobert */
    public void setXX_Cobert (java.math.BigDecimal XX_Cobert)
    {
        set_Value ("XX_Cobert", XX_Cobert);
        
    }
    
    /** Get Cobert.
    @return Cobert */
    public java.math.BigDecimal getXX_Cobert() 
    {
        return get_ValueAsBigDecimal("XX_Cobert");
        
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
    
    /** Set Discount 1.
    @param XX_Discount1 Descuento número 1 */
    public void setXX_Discount1 (java.math.BigDecimal XX_Discount1)
    {
        set_Value ("XX_Discount1", XX_Discount1);
        
    }
    
    /** Get Discount 1.
    @return Descuento número 1 */
    public java.math.BigDecimal getXX_Discount1() 
    {
        return get_ValueAsBigDecimal("XX_Discount1");
        
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
    
    /** Set Discount 1.
    @param XX_Rebate1 Discount 1 */
    public void setXX_Rebate1 (java.math.BigDecimal XX_Rebate1)
    {
        set_Value ("XX_Rebate1", XX_Rebate1);
        
    }
    
    /** Get Discount 1.
    @return Discount 1 */
    public java.math.BigDecimal getXX_Rebate1() 
    {
        return get_ValueAsBigDecimal("XX_Rebate1");
        
    }
    
    /** Set Rotation.
    @param XX_Rotation Rotation */
    public void setXX_Rotation (java.math.BigDecimal XX_Rotation)
    {
        set_Value ("XX_Rotation", XX_Rotation);
        
    }
    
    /** Get Rotation.
    @return Rotation */
    public java.math.BigDecimal getXX_Rotation() 
    {
        return get_ValueAsBigDecimal("XX_Rotation");
        
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
    
    /** Set XX_VMR_BudgetSalesDepart_ID.
    @param XX_VMR_BudgetSalesDepart_ID XX_VMR_BudgetSalesDepart_ID */
    public void setXX_VMR_BudgetSalesDepart_ID (int XX_VMR_BudgetSalesDepart_ID)
    {
        if (XX_VMR_BudgetSalesDepart_ID <= 0) set_Value ("XX_VMR_BudgetSalesDepart_ID", null);
        else
        set_Value ("XX_VMR_BudgetSalesDepart_ID", Integer.valueOf(XX_VMR_BudgetSalesDepart_ID));
        
    }
    
    /** Get XX_VMR_BudgetSalesDepart_ID.
    @return XX_VMR_BudgetSalesDepart_ID */
    public int getXX_VMR_BudgetSalesDepart_ID() 
    {
        return get_ValueAsInt("XX_VMR_BudgetSalesDepart_ID");
        
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
    
    
}
