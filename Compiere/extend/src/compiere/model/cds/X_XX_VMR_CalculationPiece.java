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
/** Generated Model for XX_VMR_CalculationPiece
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_CalculationPiece extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_CalculationPiece_ID id
    @param trx transaction
    */
    public X_XX_VMR_CalculationPiece (Ctx ctx, int XX_VMR_CalculationPiece_ID, Trx trx)
    {
        super (ctx, XX_VMR_CalculationPiece_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_CalculationPiece_ID == 0)
        {
            setXX_VMR_CALCULATIONPIECE_ID (0);
            setXX_VMR_PURCHASEPLAN_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_CalculationPiece (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27561272559789L;
    /** Last Updated Timestamp 2010-07-14 18:10:43.0 */
    public static final long updatedMS = 1279147243000L;
    /** AD_Table_ID=1000334 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_CalculationPiece");
        
    }
    ;
    
    /** TableName=XX_VMR_CalculationPiece */
    public static final String Table_Name="XX_VMR_CalculationPiece";
    
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
    
    /** Set XX_VMR_CALCULATIONPIECE_ID.
    @param XX_VMR_CALCULATIONPIECE_ID XX_VMR_CALCULATIONPIECE_ID */
    public void setXX_VMR_CALCULATIONPIECE_ID (int XX_VMR_CALCULATIONPIECE_ID)
    {
        if (XX_VMR_CALCULATIONPIECE_ID < 1) throw new IllegalArgumentException ("XX_VMR_CALCULATIONPIECE_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_CALCULATIONPIECE_ID", Integer.valueOf(XX_VMR_CALCULATIONPIECE_ID));
        
    }
    
    /** Get XX_VMR_CALCULATIONPIECE_ID.
    @return XX_VMR_CALCULATIONPIECE_ID */
    public int getXX_VMR_CALCULATIONPIECE_ID() 
    {
        return get_ValueAsInt("XX_VMR_CALCULATIONPIECE_ID");
        
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
    
    /** Set XX_VMR_PURCHASEPLAN_ID.
    @param XX_VMR_PURCHASEPLAN_ID XX_VMR_PURCHASEPLAN_ID */
    public void setXX_VMR_PURCHASEPLAN_ID (int XX_VMR_PURCHASEPLAN_ID)
    {
        if (XX_VMR_PURCHASEPLAN_ID < 1) throw new IllegalArgumentException ("XX_VMR_PURCHASEPLAN_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_PURCHASEPLAN_ID", Integer.valueOf(XX_VMR_PURCHASEPLAN_ID));
        
    }
    
    /** Get XX_VMR_PURCHASEPLAN_ID.
    @return XX_VMR_PURCHASEPLAN_ID */
    public int getXX_VMR_PURCHASEPLAN_ID() 
    {
        return get_ValueAsInt("XX_VMR_PURCHASEPLAN_ID");
        
    }
    
    /** Set Section.
    @param XX_VMR_Section_ID Section */
    public void setXX_VMR_Section_ID (int XX_VMR_Section_ID)
    {
        if (XX_VMR_Section_ID <= 0) set_Value ("XX_VMR_Section_ID", null);
        else
        set_Value ("XX_VMR_Section_ID", Integer.valueOf(XX_VMR_Section_ID));
        
    }
    
    /** Get Section.
    @return Section */
    public int getXX_VMR_Section_ID() 
    {
        return get_ValueAsInt("XX_VMR_Section_ID");
        
    }
    
    
}
