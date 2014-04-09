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
/** Generated Model for XX_VMR_DiscountRequest
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_DiscountRequest extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_DiscountRequest_ID id
    @param trx transaction
    */
    public X_XX_VMR_DiscountRequest (Ctx ctx, int XX_VMR_DiscountRequest_ID, Trx trx)
    {
        super (ctx, XX_VMR_DiscountRequest_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_DiscountRequest_ID == 0)
        {
            setValue (null);
            setXX_VMR_DiscountRequest_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_DiscountRequest (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27592631674789L;
    /** Last Updated Timestamp 2011-07-12 17:02:38.0 */
    public static final long updatedMS = 1310506358000L;
    /** AD_Table_ID=1000329 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_DiscountRequest");
        
    }
    ;
    
    /** TableName=XX_VMR_DiscountRequest */
    public static final String Table_Name="XX_VMR_DiscountRequest";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
        else
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
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
    
    /** Set Total Quantity.
    @param TotalQty Total Quantity */
    public void setTotalQty (int TotalQty)
    {
        set_Value ("TotalQty", Integer.valueOf(TotalQty));
        
    }
    
    /** Get Total Quantity.
    @return Total Quantity */
    public int getTotalQty() 
    {
        return get_ValueAsInt("TotalQty");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Date Request.
    @param XX_DateRequest Date Request */
    public void setXX_DateRequest (Timestamp XX_DateRequest)
    {
        set_Value ("XX_DateRequest", XX_DateRequest);
        
    }
    
    /** Get Date Request.
    @return Date Request */
    public Timestamp getXX_DateRequest() 
    {
        return (Timestamp)get_Value("XX_DateRequest");
        
    }
    
    /** Set DayUpdate.
    @param XX_DayUpdate DayUpdate */
    public void setXX_DayUpdate (String XX_DayUpdate)
    {
        set_Value ("XX_DayUpdate", XX_DayUpdate);
        
    }
    
    /** Get DayUpdate.
    @return DayUpdate */
    public String getXX_DayUpdate() 
    {
        return (String)get_Value("XX_DayUpdate");
        
    }
    
    /** Set MonthUpdate.
    @param XX_MonthUpdate MonthUpdate */
    public void setXX_MonthUpdate (String XX_MonthUpdate)
    {
        set_Value ("XX_MonthUpdate", XX_MonthUpdate);
        
    }
    
    /** Get MonthUpdate.
    @return MonthUpdate */
    public String getXX_MonthUpdate() 
    {
        return (String)get_Value("XX_MonthUpdate");
        
    }
    
    /** Set Print Label.
    @param XX_PrintLabel Imprime las Etiquetas Beco */
    public void setXX_PrintLabel (String XX_PrintLabel)
    {
        set_Value ("XX_PrintLabel", XX_PrintLabel);
        
    }
    
    /** Get Print Label.
    @return Imprime las Etiquetas Beco */
    public String getXX_PrintLabel() 
    {
        return (String)get_Value("XX_PrintLabel");
        
    }
    
    /** Anulada = AN */
    public static final String XX_STATUS_Anulada = X_Ref_XX_StatusDiscountApplication.ANULADA.getValue();
    /** Aprobada = AP */
    public static final String XX_STATUS_Aprobada = X_Ref_XX_StatusDiscountApplication.APROBADA.getValue();
    /** Pendiente = PE */
    public static final String XX_STATUS_Pendiente = X_Ref_XX_StatusDiscountApplication.PENDIENTE.getValue();
    /** Reversada = RV */
    public static final String XX_STATUS_Reversada = X_Ref_XX_StatusDiscountApplication.REVERSADA.getValue();
    /** Set Status.
    @param XX_Status Status */
    public void setXX_Status (String XX_Status)
    {
        if (!X_Ref_XX_StatusDiscountApplication.isValid(XX_Status))
        throw new IllegalArgumentException ("XX_Status Invalid value - " + XX_Status + " - Reference_ID=1000274 - AN - AP - PE - RV");
        set_Value ("XX_Status", XX_Status);
        
    }
    
    /** Get Status.
    @return Status */
    public String getXX_Status() 
    {
        return (String)get_Value("XX_Status");
        
    }
    
    /** Set Synchronized.
    @param XX_Synchronized Indica si el registro ya fue exportado */
    public void setXX_Synchronized (boolean XX_Synchronized)
    {
        set_Value ("XX_Synchronized", Boolean.valueOf(XX_Synchronized));
        
    }
    
    /** Get Synchronized.
    @return Indica si el registro ya fue exportado */
    public boolean isXX_Synchronized() 
    {
        return get_ValueAsBoolean("XX_Synchronized");
        
    }
    
    /** Set Total Price.
    @param XX_TotalPrice Total Price */
    public void setXX_TotalPrice (java.math.BigDecimal XX_TotalPrice)
    {
        throw new IllegalArgumentException ("XX_TotalPrice is virtual column");
        
    }
    
    /** Get Total Price.
    @return Total Price */
    public java.math.BigDecimal getXX_TotalPrice() 
    {
        return get_ValueAsBigDecimal("XX_TotalPrice");
        
    }
    
    /** Set Total PVP Plus Tax.
    @param XX_TotalPVPPlusTax Total PVP Plus Tax */
    public void setXX_TotalPVPPlusTax (java.math.BigDecimal XX_TotalPVPPlusTax)
    {
        set_Value ("XX_TotalPVPPlusTax", XX_TotalPVPPlusTax);
        
    }
    
    /** Get Total PVP Plus Tax.
    @return Total PVP Plus Tax */
    public java.math.BigDecimal getXX_TotalPVPPlusTax() 
    {
        return get_ValueAsBigDecimal("XX_TotalPVPPlusTax");
        
    }
    
    /** Set Total Real.
    @param XX_TotalReal Total Real */
    public void setXX_TotalReal (java.math.BigDecimal XX_TotalReal)
    {
        throw new IllegalArgumentException ("XX_TotalReal is virtual column");
        
    }
    
    /** Get Total Real.
    @return Total Real */
    public java.math.BigDecimal getXX_TotalReal() 
    {
        return get_ValueAsBigDecimal("XX_TotalReal");
        
    }
    
    /** Set Total Spending Of Discount.
    @param XX_TotalSpendingOfDiscount Total Spending Of Discount */
    public void setXX_TotalSpendingOfDiscount (java.math.BigDecimal XX_TotalSpendingOfDiscount)
    {
        set_Value ("XX_TotalSpendingOfDiscount", XX_TotalSpendingOfDiscount);
        
    }
    
    /** Get Total Spending Of Discount.
    @return Total Spending Of Discount */
    public java.math.BigDecimal getXX_TotalSpendingOfDiscount() 
    {
        return get_ValueAsBigDecimal("XX_TotalSpendingOfDiscount");
        
    }
    
    /** Set XX_UserCreation.
    @param XX_UserCreation XX_UserCreation */
    public void setXX_UserCreation (String XX_UserCreation)
    {
        throw new IllegalArgumentException ("XX_UserCreation is virtual column");
        
    }
    
    /** Get XX_UserCreation.
    @return XX_UserCreation */
    public String getXX_UserCreation() 
    {
        return (String)get_Value("XX_UserCreation");
        
    }
    
    /** Set XX_UserUpdate.
    @param XX_UserUpdate XX_UserUpdate */
    public void setXX_UserUpdate (String XX_UserUpdate)
    {
        throw new IllegalArgumentException ("XX_UserUpdate is virtual column");
        
    }
    
    /** Get XX_UserUpdate.
    @return XX_UserUpdate */
    public String getXX_UserUpdate() 
    {
        return (String)get_Value("XX_UserUpdate");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        if (XX_VMR_Department_ID <= 0) set_ValueNoCheck ("XX_VMR_Department_ID", null);
        else
        set_ValueNoCheck ("XX_VMR_Department_ID", Integer.valueOf(XX_VMR_Department_ID));
        
    }
    
    /** Get Department.
    @return Department */
    public int getXX_VMR_Department_ID() 
    {
        return get_ValueAsInt("XX_VMR_Department_ID");
        
    }
    
    /** Set XX_VMR_DiscountRequest_ID.
    @param XX_VMR_DiscountRequest_ID XX_VMR_DiscountRequest_ID */
    public void setXX_VMR_DiscountRequest_ID (int XX_VMR_DiscountRequest_ID)
    {
        if (XX_VMR_DiscountRequest_ID < 1) throw new IllegalArgumentException ("XX_VMR_DiscountRequest_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_DiscountRequest_ID", Integer.valueOf(XX_VMR_DiscountRequest_ID));
        
    }
    
    /** Get XX_VMR_DiscountRequest_ID.
    @return XX_VMR_DiscountRequest_ID */
    public int getXX_VMR_DiscountRequest_ID() 
    {
        return get_ValueAsInt("XX_VMR_DiscountRequest_ID");
        
    }
    
    /** Set Discount Type.
    @param XX_VMR_DiscountType_ID Discount Type */
    public void setXX_VMR_DiscountType_ID (int XX_VMR_DiscountType_ID)
    {
        if (XX_VMR_DiscountType_ID <= 0) set_Value ("XX_VMR_DiscountType_ID", null);
        else
        set_Value ("XX_VMR_DiscountType_ID", Integer.valueOf(XX_VMR_DiscountType_ID));
        
    }
    
    /** Get Discount Type.
    @return Discount Type */
    public int getXX_VMR_DiscountType_ID() 
    {
        return get_ValueAsInt("XX_VMR_DiscountType_ID");
        
    }
    
    /** Set XX_VMR_ReverseDiscount.
    @param XX_VMR_ReverseDiscount XX_VMR_ReverseDiscount */
    public void setXX_VMR_ReverseDiscount (String XX_VMR_ReverseDiscount)
    {
        set_Value ("XX_VMR_ReverseDiscount", XX_VMR_ReverseDiscount);
        
    }
    
    /** Get XX_VMR_ReverseDiscount.
    @return XX_VMR_ReverseDiscount */
    public String getXX_VMR_ReverseDiscount() 
    {
        return (String)get_Value("XX_VMR_ReverseDiscount");
        
    }
    
    /** Set YearUpdate.
    @param XX_YearUpdate YearUpdate */
    public void setXX_YearUpdate (String XX_YearUpdate)
    {
        set_Value ("XX_YearUpdate", XX_YearUpdate);
        
    }
    
    /** Get YearUpdate.
    @return YearUpdate */
    public String getXX_YearUpdate() 
    {
        return (String)get_Value("XX_YearUpdate");
        
    }
    
    
}
