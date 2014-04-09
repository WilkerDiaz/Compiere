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
/** Generated Model for XX_VMR_Sold01
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_Sold01 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_Sold01_ID id
    @param trx transaction
    */
    public X_XX_VMR_Sold01 (Ctx ctx, int XX_VMR_Sold01_ID, Trx trx)
    {
        super (ctx, XX_VMR_Sold01_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_Sold01_ID == 0)
        {
            setXX_AlternateCodePEREMP (Env.ZERO);
            setXX_AmountCost (Env.ZERO);
            setXX_AmountOfSale (Env.ZERO);
            setXX_AnnulmentProductCode (null);
            setXX_ApplicationNumber (Env.ZERO);
            setXX_CodeMovementRequest (null);
            setXX_ConsecutivePrice (Env.ZERO);
            setXX_ConsecutivePrices (Env.ZERO);
            setXX_DiscountPromotion (Env.ZERO);
            setXX_MonthStatusMovement (Env.ZERO);
            setXX_MovementDayStatus (Env.ZERO);
            setXX_MovementRequestStatus (null);
            setXX_MovementTypeApplication (null);
            setXX_PricePromotion (Env.ZERO);
            setXX_ProductCode (Env.ZERO);
            setXX_QuantityPhysicalStore (Env.ZERO);
            setXX_QuantityProductBuyer (Env.ZERO);
            setXX_QuantityProductStorage (Env.ZERO);
            setXX_QuantityProductStore (Env.ZERO);
            setXX_VMR_Sold01_ID (0);
            setXX_YearMovementStatus (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_Sold01 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27566095876789L;
    /** Last Updated Timestamp 2010-09-08 13:59:20.0 */
    public static final long updatedMS = 1283970560000L;
    /** AD_Table_ID=1000030 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_Sold01");
        
    }
    ;
    
    /** TableName=XX_VMR_Sold01 */
    public static final String Table_Name="XX_VMR_Sold01";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
        else
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
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
    
    /** Set XX_AlternateCodePEREMP.
    @param XX_AlternateCodePEREMP XX_AlternateCodePEREMP */
    public void setXX_AlternateCodePEREMP (java.math.BigDecimal XX_AlternateCodePEREMP)
    {
        if (XX_AlternateCodePEREMP == null) throw new IllegalArgumentException ("XX_AlternateCodePEREMP is mandatory.");
        set_Value ("XX_AlternateCodePEREMP", XX_AlternateCodePEREMP);
        
    }
    
    /** Get XX_AlternateCodePEREMP.
    @return XX_AlternateCodePEREMP */
    public java.math.BigDecimal getXX_AlternateCodePEREMP() 
    {
        return get_ValueAsBigDecimal("XX_AlternateCodePEREMP");
        
    }
    
    /** Set XX_AmountCost.
    @param XX_AmountCost XX_AmountCost */
    public void setXX_AmountCost (java.math.BigDecimal XX_AmountCost)
    {
        if (XX_AmountCost == null) throw new IllegalArgumentException ("XX_AmountCost is mandatory.");
        set_Value ("XX_AmountCost", XX_AmountCost);
        
    }
    
    /** Get XX_AmountCost.
    @return XX_AmountCost */
    public java.math.BigDecimal getXX_AmountCost() 
    {
        return get_ValueAsBigDecimal("XX_AmountCost");
        
    }
    
    /** Set XX_AmountOfSale.
    @param XX_AmountOfSale XX_AmountOfSale */
    public void setXX_AmountOfSale (java.math.BigDecimal XX_AmountOfSale)
    {
        if (XX_AmountOfSale == null) throw new IllegalArgumentException ("XX_AmountOfSale is mandatory.");
        set_Value ("XX_AmountOfSale", XX_AmountOfSale);
        
    }
    
    /** Get XX_AmountOfSale.
    @return XX_AmountOfSale */
    public java.math.BigDecimal getXX_AmountOfSale() 
    {
        return get_ValueAsBigDecimal("XX_AmountOfSale");
        
    }
    
    /** Set XX_AnnulmentProductCode.
    @param XX_AnnulmentProductCode XX_AnnulmentProductCode */
    public void setXX_AnnulmentProductCode (String XX_AnnulmentProductCode)
    {
        if (XX_AnnulmentProductCode == null) throw new IllegalArgumentException ("XX_AnnulmentProductCode is mandatory.");
        set_Value ("XX_AnnulmentProductCode", XX_AnnulmentProductCode);
        
    }
    
    /** Get XX_AnnulmentProductCode.
    @return XX_AnnulmentProductCode */
    public String getXX_AnnulmentProductCode() 
    {
        return (String)get_Value("XX_AnnulmentProductCode");
        
    }
    
    /** Set Application Number.
    @param XX_ApplicationNumber Application Number */
    public void setXX_ApplicationNumber (java.math.BigDecimal XX_ApplicationNumber)
    {
        if (XX_ApplicationNumber == null) throw new IllegalArgumentException ("XX_ApplicationNumber is mandatory.");
        set_Value ("XX_ApplicationNumber", XX_ApplicationNumber);
        
    }
    
    /** Get Application Number.
    @return Application Number */
    public java.math.BigDecimal getXX_ApplicationNumber() 
    {
        return get_ValueAsBigDecimal("XX_ApplicationNumber");
        
    }
    
    /** Set XX_CodeMovementRequest.
    @param XX_CodeMovementRequest XX_CodeMovementRequest */
    public void setXX_CodeMovementRequest (String XX_CodeMovementRequest)
    {
        if (XX_CodeMovementRequest == null) throw new IllegalArgumentException ("XX_CodeMovementRequest is mandatory.");
        set_Value ("XX_CodeMovementRequest", XX_CodeMovementRequest);
        
    }
    
    /** Get XX_CodeMovementRequest.
    @return XX_CodeMovementRequest */
    public String getXX_CodeMovementRequest() 
    {
        return (String)get_Value("XX_CodeMovementRequest");
        
    }
    
    /** Set Consecutive Price.
    @param XX_ConsecutivePrice Consecutivo de Precio */
    public void setXX_ConsecutivePrice (java.math.BigDecimal XX_ConsecutivePrice)
    {
        if (XX_ConsecutivePrice == null) throw new IllegalArgumentException ("XX_ConsecutivePrice is mandatory.");
        set_Value ("XX_ConsecutivePrice", XX_ConsecutivePrice);
        
    }
    
    /** Get Consecutive Price.
    @return Consecutivo de Precio */
    public java.math.BigDecimal getXX_ConsecutivePrice() 
    {
        return get_ValueAsBigDecimal("XX_ConsecutivePrice");
        
    }
    
    /** Set XX_ConsecutivePrices.
    @param XX_ConsecutivePrices XX_ConsecutivePrices */
    public void setXX_ConsecutivePrices (java.math.BigDecimal XX_ConsecutivePrices)
    {
        if (XX_ConsecutivePrices == null) throw new IllegalArgumentException ("XX_ConsecutivePrices is mandatory.");
        set_Value ("XX_ConsecutivePrices", XX_ConsecutivePrices);
        
    }
    
    /** Get XX_ConsecutivePrices.
    @return XX_ConsecutivePrices */
    public java.math.BigDecimal getXX_ConsecutivePrices() 
    {
        return get_ValueAsBigDecimal("XX_ConsecutivePrices");
        
    }
    
    /** Set XX_DiscountPromotion.
    @param XX_DiscountPromotion XX_DiscountPromotion */
    public void setXX_DiscountPromotion (java.math.BigDecimal XX_DiscountPromotion)
    {
        if (XX_DiscountPromotion == null) throw new IllegalArgumentException ("XX_DiscountPromotion is mandatory.");
        set_Value ("XX_DiscountPromotion", XX_DiscountPromotion);
        
    }
    
    /** Get XX_DiscountPromotion.
    @return XX_DiscountPromotion */
    public java.math.BigDecimal getXX_DiscountPromotion() 
    {
        return get_ValueAsBigDecimal("XX_DiscountPromotion");
        
    }
    
    /** Set XX_MonthStatusMovement.
    @param XX_MonthStatusMovement XX_MonthStatusMovement */
    public void setXX_MonthStatusMovement (java.math.BigDecimal XX_MonthStatusMovement)
    {
        if (XX_MonthStatusMovement == null) throw new IllegalArgumentException ("XX_MonthStatusMovement is mandatory.");
        set_Value ("XX_MonthStatusMovement", XX_MonthStatusMovement);
        
    }
    
    /** Get XX_MonthStatusMovement.
    @return XX_MonthStatusMovement */
    public java.math.BigDecimal getXX_MonthStatusMovement() 
    {
        return get_ValueAsBigDecimal("XX_MonthStatusMovement");
        
    }
    
    /** Set XX_MovementDayStatus.
    @param XX_MovementDayStatus XX_MovementDayStatus */
    public void setXX_MovementDayStatus (java.math.BigDecimal XX_MovementDayStatus)
    {
        if (XX_MovementDayStatus == null) throw new IllegalArgumentException ("XX_MovementDayStatus is mandatory.");
        set_Value ("XX_MovementDayStatus", XX_MovementDayStatus);
        
    }
    
    /** Get XX_MovementDayStatus.
    @return XX_MovementDayStatus */
    public java.math.BigDecimal getXX_MovementDayStatus() 
    {
        return get_ValueAsBigDecimal("XX_MovementDayStatus");
        
    }
    
    /** Set XX_MovementRequestStatus.
    @param XX_MovementRequestStatus XX_MovementRequestStatus */
    public void setXX_MovementRequestStatus (String XX_MovementRequestStatus)
    {
        if (XX_MovementRequestStatus == null) throw new IllegalArgumentException ("XX_MovementRequestStatus is mandatory.");
        set_Value ("XX_MovementRequestStatus", XX_MovementRequestStatus);
        
    }
    
    /** Get XX_MovementRequestStatus.
    @return XX_MovementRequestStatus */
    public String getXX_MovementRequestStatus() 
    {
        return (String)get_Value("XX_MovementRequestStatus");
        
    }
    
    /** Set XX_MovementTypeApplication.
    @param XX_MovementTypeApplication XX_MovementTypeApplication */
    public void setXX_MovementTypeApplication (String XX_MovementTypeApplication)
    {
        if (XX_MovementTypeApplication == null) throw new IllegalArgumentException ("XX_MovementTypeApplication is mandatory.");
        set_Value ("XX_MovementTypeApplication", XX_MovementTypeApplication);
        
    }
    
    /** Get XX_MovementTypeApplication.
    @return XX_MovementTypeApplication */
    public String getXX_MovementTypeApplication() 
    {
        return (String)get_Value("XX_MovementTypeApplication");
        
    }
    
    /** Set XX_PricePromotion.
    @param XX_PricePromotion XX_PricePromotion */
    public void setXX_PricePromotion (java.math.BigDecimal XX_PricePromotion)
    {
        if (XX_PricePromotion == null) throw new IllegalArgumentException ("XX_PricePromotion is mandatory.");
        set_Value ("XX_PricePromotion", XX_PricePromotion);
        
    }
    
    /** Get XX_PricePromotion.
    @return XX_PricePromotion */
    public java.math.BigDecimal getXX_PricePromotion() 
    {
        return get_ValueAsBigDecimal("XX_PricePromotion");
        
    }
    
    /** Set Product Code.
    @param XX_ProductCode Product Code */
    public void setXX_ProductCode (java.math.BigDecimal XX_ProductCode)
    {
        if (XX_ProductCode == null) throw new IllegalArgumentException ("XX_ProductCode is mandatory.");
        set_Value ("XX_ProductCode", XX_ProductCode);
        
    }
    
    /** Get Product Code.
    @return Product Code */
    public java.math.BigDecimal getXX_ProductCode() 
    {
        return get_ValueAsBigDecimal("XX_ProductCode");
        
    }
    
    /** Set XX_QuantityPhysicalStore.
    @param XX_QuantityPhysicalStore XX_QuantityPhysicalStore */
    public void setXX_QuantityPhysicalStore (java.math.BigDecimal XX_QuantityPhysicalStore)
    {
        if (XX_QuantityPhysicalStore == null) throw new IllegalArgumentException ("XX_QuantityPhysicalStore is mandatory.");
        set_Value ("XX_QuantityPhysicalStore", XX_QuantityPhysicalStore);
        
    }
    
    /** Get XX_QuantityPhysicalStore.
    @return XX_QuantityPhysicalStore */
    public java.math.BigDecimal getXX_QuantityPhysicalStore() 
    {
        return get_ValueAsBigDecimal("XX_QuantityPhysicalStore");
        
    }
    
    /** Set XX_QuantityProductBuyer.
    @param XX_QuantityProductBuyer XX_QuantityProductBuyer */
    public void setXX_QuantityProductBuyer (java.math.BigDecimal XX_QuantityProductBuyer)
    {
        if (XX_QuantityProductBuyer == null) throw new IllegalArgumentException ("XX_QuantityProductBuyer is mandatory.");
        set_Value ("XX_QuantityProductBuyer", XX_QuantityProductBuyer);
        
    }
    
    /** Get XX_QuantityProductBuyer.
    @return XX_QuantityProductBuyer */
    public java.math.BigDecimal getXX_QuantityProductBuyer() 
    {
        return get_ValueAsBigDecimal("XX_QuantityProductBuyer");
        
    }
    
    /** Set XX_QuantityProductStorage.
    @param XX_QuantityProductStorage XX_QuantityProductStorage */
    public void setXX_QuantityProductStorage (java.math.BigDecimal XX_QuantityProductStorage)
    {
        if (XX_QuantityProductStorage == null) throw new IllegalArgumentException ("XX_QuantityProductStorage is mandatory.");
        set_Value ("XX_QuantityProductStorage", XX_QuantityProductStorage);
        
    }
    
    /** Get XX_QuantityProductStorage.
    @return XX_QuantityProductStorage */
    public java.math.BigDecimal getXX_QuantityProductStorage() 
    {
        return get_ValueAsBigDecimal("XX_QuantityProductStorage");
        
    }
    
    /** Set XX_QuantityProductStore.
    @param XX_QuantityProductStore XX_QuantityProductStore */
    public void setXX_QuantityProductStore (java.math.BigDecimal XX_QuantityProductStore)
    {
        if (XX_QuantityProductStore == null) throw new IllegalArgumentException ("XX_QuantityProductStore is mandatory.");
        set_Value ("XX_QuantityProductStore", XX_QuantityProductStore);
        
    }
    
    /** Get XX_QuantityProductStore.
    @return XX_QuantityProductStore */
    public java.math.BigDecimal getXX_QuantityProductStore() 
    {
        return get_ValueAsBigDecimal("XX_QuantityProductStore");
        
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
    
    /** Set XX_VMR_SOLD01_ID.
    @param XX_VMR_Sold01_ID XX_VMR_SOLD01_ID */
    public void setXX_VMR_Sold01_ID (int XX_VMR_Sold01_ID)
    {
        if (XX_VMR_Sold01_ID < 1) throw new IllegalArgumentException ("XX_VMR_Sold01_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_Sold01_ID", Integer.valueOf(XX_VMR_Sold01_ID));
        
    }
    
    /** Get XX_VMR_SOLD01_ID.
    @return XX_VMR_SOLD01_ID */
    public int getXX_VMR_Sold01_ID() 
    {
        return get_ValueAsInt("XX_VMR_Sold01_ID");
        
    }
    
    /** Set XX_YearMovementStatus.
    @param XX_YearMovementStatus XX_YearMovementStatus */
    public void setXX_YearMovementStatus (java.math.BigDecimal XX_YearMovementStatus)
    {
        if (XX_YearMovementStatus == null) throw new IllegalArgumentException ("XX_YearMovementStatus is mandatory.");
        set_Value ("XX_YearMovementStatus", XX_YearMovementStatus);
        
    }
    
    /** Get XX_YearMovementStatus.
    @return XX_YearMovementStatus */
    public java.math.BigDecimal getXX_YearMovementStatus() 
    {
        return get_ValueAsBigDecimal("XX_YearMovementStatus");
        
    }
    
    
}
