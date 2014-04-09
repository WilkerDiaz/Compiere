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
package compiere.model.dynamic;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VME_Product
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VME_Product extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VME_Product_ID id
    @param trx transaction
    */
    public X_XX_VME_Product (Ctx ctx, int XX_VME_Product_ID, Trx trx)
    {
        super (ctx, XX_VME_Product_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VME_Product_ID == 0)
        {
            setName (null);
            setValue (null);
            setXX_VME_Product_ID (0);
            setXX_VME_Reference_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VME_Product (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27618110976789L;
    /** Last Updated Timestamp 2012-05-02 14:37:40.0 */
    public static final long updatedMS = 1335985660000L;
    /** AD_Table_ID=1000956 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VME_Product");
        
    }
    ;
    
    /** TableName=XX_VME_Product */
    public static final String Table_Name="XX_VME_Product";
    
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
    
    /** Set Country.
    @param C_Country_ID Country */
    public void setC_Country_ID (int C_Country_ID)
    {
        if (C_Country_ID <= 0) set_Value ("C_Country_ID", null);
        else
        set_Value ("C_Country_ID", Integer.valueOf(C_Country_ID));
        
    }
    
    /** Get Country.
    @return Country */
    public int getC_Country_ID() 
    {
        return get_ValueAsInt("C_Country_ID");
        
    }
    
    /** Set Descripción.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Descripción.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
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
    
    /** Set Nombre.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
        set_Value ("Name", Name);
        
    }
    
    /** Get Nombre.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
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
    
    /** Set Initial Amount.
    @param XX_VMA_InitialAmount Initial Amount */
    public void setXX_VMA_InitialAmount (java.math.BigDecimal XX_VMA_InitialAmount)
    {
        set_Value ("XX_VMA_InitialAmount", XX_VMA_InitialAmount);
        
    }
    
    /** Get Initial Amount.
    @return Initial Amount */
    public java.math.BigDecimal getXX_VMA_InitialAmount() 
    {
        return get_ValueAsBigDecimal("XX_VMA_InitialAmount");
        
    }
    
    /** Set Actual Price (+IVA).
    @param XX_VME_ActualPrice Actual Price (+IVA) */
    public void setXX_VME_ActualPrice (java.math.BigDecimal XX_VME_ActualPrice)
    {
        set_Value ("XX_VME_ActualPrice", XX_VME_ActualPrice);
        
    }
    
    /** Get Actual Price (+IVA).
    @return Actual Price (+IVA) */
    public java.math.BigDecimal getXX_VME_ActualPrice() 
    {
        return get_ValueAsBigDecimal("XX_VME_ActualPrice");
        
    }
    
    /** Set Indepabis Quantity.
    @param XX_VME_IndepabisQty Indepabis Quantity */
    public void setXX_VME_IndepabisQty (java.math.BigDecimal XX_VME_IndepabisQty)
    {
        set_Value ("XX_VME_IndepabisQty", XX_VME_IndepabisQty);
        
    }
    
    /** Get Indepabis Quantity.
    @return Indepabis Quantity */
    public java.math.BigDecimal getXX_VME_IndepabisQty() 
    {
        return get_ValueAsBigDecimal("XX_VME_IndepabisQty");
        
    }
    
    /** Set Inventory Quantity.
    @param XX_VME_InventoryQty Inventory Quantity */
    public void setXX_VME_InventoryQty (java.math.BigDecimal XX_VME_InventoryQty)
    {
        set_Value ("XX_VME_InventoryQty", XX_VME_InventoryQty);
        
    }
    
    /** Get Inventory Quantity.
    @return Inventory Quantity */
    public java.math.BigDecimal getXX_VME_InventoryQty() 
    {
        return get_ValueAsBigDecimal("XX_VME_InventoryQty");
        
    }
    
    /** Set Final Inventory.
    @param XX_VME_InvFin Final inventory  at the end of the marketing activity. */
    public void setXX_VME_InvFin (java.math.BigDecimal XX_VME_InvFin)
    {
        set_Value ("XX_VME_InvFin", XX_VME_InvFin);
        
    }
    
    /** Get Final Inventory.
    @return Final inventory  at the end of the marketing activity. */
    public java.math.BigDecimal getXX_VME_InvFin() 
    {
        return get_ValueAsBigDecimal("XX_VME_InvFin");
        
    }
    
    /** Set Initial Inventory.
    @param XX_VME_InvIni Initial inventory at the beginning of the marketing activity */
    public void setXX_VME_InvIni (java.math.BigDecimal XX_VME_InvIni)
    {
        set_Value ("XX_VME_InvIni", XX_VME_InvIni);
        
    }
    
    /** Get Initial Inventory.
    @return Initial inventory at the beginning of the marketing activity */
    public java.math.BigDecimal getXX_VME_InvIni() 
    {
        return get_ValueAsBigDecimal("XX_VME_InvIni");
        
    }
    
    /** Set Inventory update based on Pedidos.
    @param XX_VME_InvUpdate Inventory update based on Pedidos */
    public void setXX_VME_InvUpdate (java.math.BigDecimal XX_VME_InvUpdate)
    {
        set_Value ("XX_VME_InvUpdate", XX_VME_InvUpdate);
        
    }
    
    /** Get Inventory update based on Pedidos.
    @return Inventory update based on Pedidos */
    public java.math.BigDecimal getXX_VME_InvUpdate() 
    {
        return get_ValueAsBigDecimal("XX_VME_InvUpdate");
        
    }
    
    /** Set Defines if the indepabis quantity was manually modified.
    @param XX_VME_Manual Defines if the indepabis quantity was manually modified */
    public void setXX_VME_Manual (boolean XX_VME_Manual)
    {
        set_Value ("XX_VME_Manual", Boolean.valueOf(XX_VME_Manual));
        
    }
    
    /** Get Defines if the indepabis quantity was manually modified.
    @return Defines if the indepabis quantity was manually modified */
    public boolean isXX_VME_Manual() 
    {
        return get_ValueAsBoolean("XX_VME_Manual");
        
    }
    
    /** Set XX_VME_Product_ID.
    @param XX_VME_Product_ID XX_VME_Product_ID */
    public void setXX_VME_Product_ID (int XX_VME_Product_ID)
    {
        if (XX_VME_Product_ID < 1) throw new IllegalArgumentException ("XX_VME_Product_ID is mandatory.");
        set_ValueNoCheck ("XX_VME_Product_ID", Integer.valueOf(XX_VME_Product_ID));
        
    }
    
    /** Get XX_VME_Product_ID.
    @return XX_VME_Product_ID */
    public int getXX_VME_Product_ID() 
    {
        return get_ValueAsInt("XX_VME_Product_ID");
        
    }
    
    /** Set Promotional Price.
    @param XX_VME_PromotionalPrice Promotional Price */
    public void setXX_VME_PromotionalPrice (java.math.BigDecimal XX_VME_PromotionalPrice)
    {
        set_Value ("XX_VME_PromotionalPrice", XX_VME_PromotionalPrice);
        
    }
    
    /** Get Promotional Price.
    @return Promotional Price */
    public java.math.BigDecimal getXX_VME_PromotionalPrice() 
    {
        return get_ValueAsBigDecimal("XX_VME_PromotionalPrice");
        
    }
    
    /** Set Cantidad en CD.
    @param XX_VME_QtyCD Cantidad en CD */
    public void setXX_VME_QtyCD (java.math.BigDecimal XX_VME_QtyCD)
    {
        set_Value ("XX_VME_QtyCD", XX_VME_QtyCD);
        
    }
    
    /** Get Cantidad en CD.
    @return Cantidad en CD */
    public java.math.BigDecimal getXX_VME_QtyCD() 
    {
        return get_ValueAsBigDecimal("XX_VME_QtyCD");
        
    }
    
    /** Set XX_VME_Reference_ID.
    @param XX_VME_Reference_ID XX_VME_Reference_ID */
    public void setXX_VME_Reference_ID (int XX_VME_Reference_ID)
    {
        if (XX_VME_Reference_ID < 1) throw new IllegalArgumentException ("XX_VME_Reference_ID is mandatory.");
        set_ValueNoCheck ("XX_VME_Reference_ID", Integer.valueOf(XX_VME_Reference_ID));
        
    }
    
    /** Get XX_VME_Reference_ID.
    @return XX_VME_Reference_ID */
    public int getXX_VME_Reference_ID() 
    {
        return get_ValueAsInt("XX_VME_Reference_ID");
        
    }
    
    /** Set Brand.
    @param XX_VMR_Brand_ID Id de la Tabla XX_VMR_BRAND(Marca) */
    public void setXX_VMR_Brand_ID (int XX_VMR_Brand_ID)
    {
        if (XX_VMR_Brand_ID <= 0) set_Value ("XX_VMR_Brand_ID", null);
        else
        set_Value ("XX_VMR_Brand_ID", Integer.valueOf(XX_VMR_Brand_ID));
        
    }
    
    /** Get Brand.
    @return Id de la Tabla XX_VMR_BRAND(Marca) */
    public int getXX_VMR_Brand_ID() 
    {
        return get_ValueAsInt("XX_VMR_Brand_ID");
        
    }
    
    /** Set Category.
    @param XX_VMR_Category_ID Category */
    public void setXX_VMR_Category_ID (int XX_VMR_Category_ID)
    {
        if (XX_VMR_Category_ID <= 0) set_Value ("XX_VMR_Category_ID", null);
        else
        set_Value ("XX_VMR_Category_ID", Integer.valueOf(XX_VMR_Category_ID));
        
    }
    
    /** Get Category.
    @return Category */
    public int getXX_VMR_Category_ID() 
    {
        return get_ValueAsInt("XX_VMR_Category_ID");
        
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
    
    /** Set Vendor Product Reference.
    @param XX_VMR_VendorProdRef_ID Vendor Product Reference */
    public void setXX_VMR_VendorProdRef_ID (int XX_VMR_VendorProdRef_ID)
    {
        if (XX_VMR_VendorProdRef_ID <= 0) set_Value ("XX_VMR_VendorProdRef_ID", null);
        else
        set_Value ("XX_VMR_VendorProdRef_ID", Integer.valueOf(XX_VMR_VendorProdRef_ID));
        
    }
    
    /** Get Vendor Product Reference.
    @return Vendor Product Reference */
    public int getXX_VMR_VendorProdRef_ID() 
    {
        return get_ValueAsInt("XX_VMR_VendorProdRef_ID");
        
    }
    
    
}
