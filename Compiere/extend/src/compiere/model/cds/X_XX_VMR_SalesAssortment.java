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
/** Generated Model for XX_VMR_SalesAssortment
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_SalesAssortment extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_SalesAssortment_ID id
    @param trx transaction
    */
    public X_XX_VMR_SalesAssortment (Ctx ctx, int XX_VMR_SalesAssortment_ID, Trx trx)
    {
        super (ctx, XX_VMR_SalesAssortment_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_SalesAssortment_ID == 0)
        {
            setName (null);
            setValue (null);
            setXX_VMR_SALESASSORTMENT_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_SalesAssortment (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27620535720789L;
    /** Last Updated Timestamp 2012-05-30 16:10:04.0 */
    public static final long updatedMS = 1338410404000L;
    /** AD_Table_ID=1002153 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_SalesAssortment");
        
    }
    ;
    
    /** TableName=XX_VMR_SalesAssortment */
    public static final String Table_Name="XX_VMR_SalesAssortment";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Attribute Set Instance.
    @param M_AttributeSetInstance_ID Product Attribute Set Instance */
    public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
    {
        if (M_AttributeSetInstance_ID <= 0) set_Value ("M_AttributeSetInstance_ID", null);
        else
        set_Value ("M_AttributeSetInstance_ID", Integer.valueOf(M_AttributeSetInstance_ID));
        
    }
    
    /** Get Attribute Set Instance.
    @return Product Attribute Set Instance */
    public int getM_AttributeSetInstance_ID() 
    {
        return get_ValueAsInt("M_AttributeSetInstance_ID");
        
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
    
    /** Set Season.
    @param XX_VMA_Season_ID Identifier used for a Season. */
    public void setXX_VMA_Season_ID (int XX_VMA_Season_ID)
    {
        if (XX_VMA_Season_ID <= 0) set_Value ("XX_VMA_Season_ID", null);
        else
        set_Value ("XX_VMA_Season_ID", Integer.valueOf(XX_VMA_Season_ID));
        
    }
    
    /** Get Season.
    @return Identifier used for a Season. */
    public int getXX_VMA_Season_ID() 
    {
        return get_ValueAsInt("XX_VMA_Season_ID");
        
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
    
    /** Set Collection.
    @param XX_VMR_Collection_ID ID de Colección */
    public void setXX_VMR_Collection_ID (int XX_VMR_Collection_ID)
    {
        if (XX_VMR_Collection_ID <= 0) set_Value ("XX_VMR_Collection_ID", null);
        else
        set_Value ("XX_VMR_Collection_ID", Integer.valueOf(XX_VMR_Collection_ID));
        
    }
    
    /** Get Collection.
    @return ID de Colección */
    public int getXX_VMR_Collection_ID() 
    {
        return get_ValueAsInt("XX_VMR_Collection_ID");
        
    }
    
    /** Set Day Sales (Amount).
    @param XX_VMR_DaySalesAmount Day Sales (Amount) */
    public void setXX_VMR_DaySalesAmount (java.math.BigDecimal XX_VMR_DaySalesAmount)
    {
        set_Value ("XX_VMR_DaySalesAmount", XX_VMR_DaySalesAmount);
        
    }
    
    /** Get Day Sales (Amount).
    @return Day Sales (Amount) */
    public java.math.BigDecimal getXX_VMR_DaySalesAmount() 
    {
        return get_ValueAsBigDecimal("XX_VMR_DaySalesAmount");
        
    }
    
    /** Set Day Sales (Pieces).
    @param XX_VMR_DaySalesPieces Day Sales (Pieces) */
    public void setXX_VMR_DaySalesPieces (java.math.BigDecimal XX_VMR_DaySalesPieces)
    {
        set_Value ("XX_VMR_DaySalesPieces", XX_VMR_DaySalesPieces);
        
    }
    
    /** Get Day Sales (Pieces).
    @return Day Sales (Pieces) */
    public java.math.BigDecimal getXX_VMR_DaySalesPieces() 
    {
        return get_ValueAsBigDecimal("XX_VMR_DaySalesPieces");
        
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
    
    /** Set Month Sales (Amount).
    @param XX_VMR_MonthSalesAmount Month Sales (Amount) */
    public void setXX_VMR_MonthSalesAmount (java.math.BigDecimal XX_VMR_MonthSalesAmount)
    {
        set_Value ("XX_VMR_MonthSalesAmount", XX_VMR_MonthSalesAmount);
        
    }
    
    /** Get Month Sales (Amount).
    @return Month Sales (Amount) */
    public java.math.BigDecimal getXX_VMR_MonthSalesAmount() 
    {
        return get_ValueAsBigDecimal("XX_VMR_MonthSalesAmount");
        
    }
    
    /** Set Month Sales (Pieces).
    @param XX_VMR_MonthSalesPieces Month Sales (Pieces) */
    public void setXX_VMR_MonthSalesPieces (java.math.BigDecimal XX_VMR_MonthSalesPieces)
    {
        set_Value ("XX_VMR_MonthSalesPieces", XX_VMR_MonthSalesPieces);
        
    }
    
    /** Get Month Sales (Pieces).
    @return Month Sales (Pieces) */
    public java.math.BigDecimal getXX_VMR_MonthSalesPieces() 
    {
        return get_ValueAsBigDecimal("XX_VMR_MonthSalesPieces");
        
    }
    
    /** Set Package.
    @param XX_VMR_Package_ID Package */
    public void setXX_VMR_Package_ID (int XX_VMR_Package_ID)
    {
        if (XX_VMR_Package_ID <= 0) set_Value ("XX_VMR_Package_ID", null);
        else
        set_Value ("XX_VMR_Package_ID", Integer.valueOf(XX_VMR_Package_ID));
        
    }
    
    /** Get Package.
    @return Package */
    public int getXX_VMR_Package_ID() 
    {
        return get_ValueAsInt("XX_VMR_Package_ID");
        
    }
    
    /** Set XX_VMR_SALESASSORTMENT_ID.
    @param XX_VMR_SALESASSORTMENT_ID Id de la Tabla */
    public void setXX_VMR_SALESASSORTMENT_ID (int XX_VMR_SALESASSORTMENT_ID)
    {
        if (XX_VMR_SALESASSORTMENT_ID < 1) throw new IllegalArgumentException ("XX_VMR_SALESASSORTMENT_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_SALESASSORTMENT_ID", Integer.valueOf(XX_VMR_SALESASSORTMENT_ID));
        
    }
    
    /** Get XX_VMR_SALESASSORTMENT_ID.
    @return Id de la Tabla */
    public int getXX_VMR_SALESASSORTMENT_ID() 
    {
        return get_ValueAsInt("XX_VMR_SALESASSORTMENT_ID");
        
    }
    
    /** Set Sales Date.
    @param XX_VMR_SalesDate Sales Date */
    public void setXX_VMR_SalesDate (Timestamp XX_VMR_SalesDate)
    {
        set_Value ("XX_VMR_SalesDate", XX_VMR_SalesDate);
        
    }
    
    /** Get Sales Date.
    @return Sales Date */
    public Timestamp getXX_VMR_SalesDate() 
    {
        return (Timestamp)get_Value("XX_VMR_SalesDate");
        
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
    
    /** Set Type Inventory.
    @param XX_VMR_TypeInventory_ID Tipo de Inventario */
    public void setXX_VMR_TypeInventory_ID (int XX_VMR_TypeInventory_ID)
    {
        if (XX_VMR_TypeInventory_ID <= 0) set_Value ("XX_VMR_TypeInventory_ID", null);
        else
        set_Value ("XX_VMR_TypeInventory_ID", Integer.valueOf(XX_VMR_TypeInventory_ID));
        
    }
    
    /** Get Type Inventory.
    @return Tipo de Inventario */
    public int getXX_VMR_TypeInventory_ID() 
    {
        return get_ValueAsInt("XX_VMR_TypeInventory_ID");
        
    }
    
    
}
