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
/** Generated Model for XX_VCN_Inventory
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_Inventory extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_Inventory_ID id
    @param trx transaction
    */
    public X_XX_VCN_Inventory (Ctx ctx, int XX_VCN_Inventory_ID, Trx trx)
    {
        super (ctx, XX_VCN_Inventory_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_Inventory_ID == 0)
        {
            setValue (null);
            setXX_AdjustmentsAmount (Env.ZERO);
            setXX_ADJUSTMENTSQUANTITY (Env.ZERO);
            setXX_ConsecutivePrice (Env.ZERO);
            setXX_INITIALINVENTORYAMOUNT (Env.ZERO);
            setXX_INITIALINVENTORYQUANTITY (Env.ZERO);
            setXX_INVENTORYMONTH (Env.ZERO);
            setXX_INVENTORYYEAR (Env.ZERO);
            setXX_MOVEMENTAMOUNT (Env.ZERO);
            setXX_MOVEMENTQUANTITY (Env.ZERO);
            setXX_PREVIOUSADJUSTMENTSAMOUNT (Env.ZERO);
            setXX_PREVIOUSADJUSTMENTSQUANTITY (Env.ZERO);
            setXX_SALESAMOUNT (Env.ZERO);
            setXX_SALESQUANTITY (Env.ZERO);
            setXX_SHOPPINGAMOUNT (Env.ZERO);
            setXX_SHOPPINGQUANTITY (Env.ZERO);
            setXX_VCN_INVENTORY_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_Inventory (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27572122947789L;
    /** Last Updated Timestamp 2010-11-17 08:10:31.0 */
    public static final long updatedMS = 1289997631000L;
    /** AD_Table_ID=1000068 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_Inventory");
        
    }
    ;
    
    /** TableName=XX_VCN_Inventory */
    public static final String Table_Name="XX_VCN_Inventory";
    
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
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
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
    
    /** Set Adjustments Amount.
    @param XX_AdjustmentsAmount Monto de Ajuste */
    public void setXX_AdjustmentsAmount (java.math.BigDecimal XX_AdjustmentsAmount)
    {
        if (XX_AdjustmentsAmount == null) throw new IllegalArgumentException ("XX_AdjustmentsAmount is mandatory.");
        set_Value ("XX_AdjustmentsAmount", XX_AdjustmentsAmount);
        
    }
    
    /** Get Adjustments Amount.
    @return Monto de Ajuste */
    public java.math.BigDecimal getXX_AdjustmentsAmount() 
    {
        return get_ValueAsBigDecimal("XX_AdjustmentsAmount");
        
    }
    
    /** Set Adjustments Quantity.
    @param XX_ADJUSTMENTSQUANTITY Cantidad de Ajuste */
    public void setXX_ADJUSTMENTSQUANTITY (java.math.BigDecimal XX_ADJUSTMENTSQUANTITY)
    {
        if (XX_ADJUSTMENTSQUANTITY == null) throw new IllegalArgumentException ("XX_ADJUSTMENTSQUANTITY is mandatory.");
        set_Value ("XX_ADJUSTMENTSQUANTITY", XX_ADJUSTMENTSQUANTITY);
        
    }
    
    /** Get Adjustments Quantity.
    @return Cantidad de Ajuste */
    public java.math.BigDecimal getXX_ADJUSTMENTSQUANTITY() 
    {
        return get_ValueAsBigDecimal("XX_ADJUSTMENTSQUANTITY");
        
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
    
    /** Set Initial Inventory Amount.
    @param XX_INITIALINVENTORYAMOUNT Monto de Inventario Inicial */
    public void setXX_INITIALINVENTORYAMOUNT (java.math.BigDecimal XX_INITIALINVENTORYAMOUNT)
    {
        if (XX_INITIALINVENTORYAMOUNT == null) throw new IllegalArgumentException ("XX_INITIALINVENTORYAMOUNT is mandatory.");
        set_Value ("XX_INITIALINVENTORYAMOUNT", XX_INITIALINVENTORYAMOUNT);
        
    }
    
    /** Get Initial Inventory Amount.
    @return Monto de Inventario Inicial */
    public java.math.BigDecimal getXX_INITIALINVENTORYAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_INITIALINVENTORYAMOUNT");
        
    }
    
    /** Set InitiaI Inventory Cost Price.
    @param XX_InitialInventoryCostPrice InitiaI Inventory Cost Price */
    public void setXX_InitialInventoryCostPrice (java.math.BigDecimal XX_InitialInventoryCostPrice)
    {
        set_Value ("XX_InitialInventoryCostPrice", XX_InitialInventoryCostPrice);
        
    }
    
    /** Get InitiaI Inventory Cost Price.
    @return InitiaI Inventory Cost Price */
    public java.math.BigDecimal getXX_InitialInventoryCostPrice() 
    {
        return get_ValueAsBigDecimal("XX_InitialInventoryCostPrice");
        
    }
    
    /** Set Initial Inventory Quantity.
    @param XX_INITIALINVENTORYQUANTITY Cantidad de Inventario Inicial */
    public void setXX_INITIALINVENTORYQUANTITY (java.math.BigDecimal XX_INITIALINVENTORYQUANTITY)
    {
        if (XX_INITIALINVENTORYQUANTITY == null) throw new IllegalArgumentException ("XX_INITIALINVENTORYQUANTITY is mandatory.");
        set_Value ("XX_INITIALINVENTORYQUANTITY", XX_INITIALINVENTORYQUANTITY);
        
    }
    
    /** Get Initial Inventory Quantity.
    @return Cantidad de Inventario Inicial */
    public java.math.BigDecimal getXX_INITIALINVENTORYQUANTITY() 
    {
        return get_ValueAsBigDecimal("XX_INITIALINVENTORYQUANTITY");
        
    }
    
    /** Set Inventory Month.
    @param XX_INVENTORYMONTH Mes de Inventario */
    public void setXX_INVENTORYMONTH (java.math.BigDecimal XX_INVENTORYMONTH)
    {
        if (XX_INVENTORYMONTH == null) throw new IllegalArgumentException ("XX_INVENTORYMONTH is mandatory.");
        set_Value ("XX_INVENTORYMONTH", XX_INVENTORYMONTH);
        
    }
    
    /** Get Inventory Month.
    @return Mes de Inventario */
    public java.math.BigDecimal getXX_INVENTORYMONTH() 
    {
        return get_ValueAsBigDecimal("XX_INVENTORYMONTH");
        
    }
    
    /** Set Inventory Year.
    @param XX_INVENTORYYEAR Ano de Inventario */
    public void setXX_INVENTORYYEAR (java.math.BigDecimal XX_INVENTORYYEAR)
    {
        if (XX_INVENTORYYEAR == null) throw new IllegalArgumentException ("XX_INVENTORYYEAR is mandatory.");
        set_Value ("XX_INVENTORYYEAR", XX_INVENTORYYEAR);
        
    }
    
    /** Get Inventory Year.
    @return Ano de Inventario */
    public java.math.BigDecimal getXX_INVENTORYYEAR() 
    {
        return get_ValueAsBigDecimal("XX_INVENTORYYEAR");
        
    }
    
    /** Set IsAdjustment.
    @param XX_IsAdjustment IsAdjustment */
    public void setXX_IsAdjustment (boolean XX_IsAdjustment)
    {
        set_Value ("XX_IsAdjustment", Boolean.valueOf(XX_IsAdjustment));
        
    }
    
    /** Get IsAdjustment.
    @return IsAdjustment */
    public boolean isXX_IsAdjustment() 
    {
        return get_ValueAsBoolean("XX_IsAdjustment");
        
    }
    
    /** Set Movement Amount.
    @param XX_MOVEMENTAMOUNT Monto de Movimiento */
    public void setXX_MOVEMENTAMOUNT (java.math.BigDecimal XX_MOVEMENTAMOUNT)
    {
        if (XX_MOVEMENTAMOUNT == null) throw new IllegalArgumentException ("XX_MOVEMENTAMOUNT is mandatory.");
        set_Value ("XX_MOVEMENTAMOUNT", XX_MOVEMENTAMOUNT);
        
    }
    
    /** Get Movement Amount.
    @return Monto de Movimiento */
    public java.math.BigDecimal getXX_MOVEMENTAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_MOVEMENTAMOUNT");
        
    }
    
    /** Set Movement Quantity.
    @param XX_MOVEMENTQUANTITY Cantidad de Movimiento */
    public void setXX_MOVEMENTQUANTITY (java.math.BigDecimal XX_MOVEMENTQUANTITY)
    {
        if (XX_MOVEMENTQUANTITY == null) throw new IllegalArgumentException ("XX_MOVEMENTQUANTITY is mandatory.");
        set_Value ("XX_MOVEMENTQUANTITY", XX_MOVEMENTQUANTITY);
        
    }
    
    /** Get Movement Quantity.
    @return Cantidad de Movimiento */
    public java.math.BigDecimal getXX_MOVEMENTQUANTITY() 
    {
        return get_ValueAsBigDecimal("XX_MOVEMENTQUANTITY");
        
    }
    
    /** Set Previous Adjustments Amount.
    @param XX_PREVIOUSADJUSTMENTSAMOUNT Monto de Ajustes Anterior */
    public void setXX_PREVIOUSADJUSTMENTSAMOUNT (java.math.BigDecimal XX_PREVIOUSADJUSTMENTSAMOUNT)
    {
        if (XX_PREVIOUSADJUSTMENTSAMOUNT == null) throw new IllegalArgumentException ("XX_PREVIOUSADJUSTMENTSAMOUNT is mandatory.");
        set_Value ("XX_PREVIOUSADJUSTMENTSAMOUNT", XX_PREVIOUSADJUSTMENTSAMOUNT);
        
    }
    
    /** Get Previous Adjustments Amount.
    @return Monto de Ajustes Anterior */
    public java.math.BigDecimal getXX_PREVIOUSADJUSTMENTSAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_PREVIOUSADJUSTMENTSAMOUNT");
        
    }
    
    /** Set Previous Adjustments Quantity.
    @param XX_PREVIOUSADJUSTMENTSQUANTITY Cantidad de Ajustes Anterior */
    public void setXX_PREVIOUSADJUSTMENTSQUANTITY (java.math.BigDecimal XX_PREVIOUSADJUSTMENTSQUANTITY)
    {
        if (XX_PREVIOUSADJUSTMENTSQUANTITY == null) throw new IllegalArgumentException ("XX_PREVIOUSADJUSTMENTSQUANTITY is mandatory.");
        set_Value ("XX_PREVIOUSADJUSTMENTSQUANTITY", XX_PREVIOUSADJUSTMENTSQUANTITY);
        
    }
    
    /** Get Previous Adjustments Quantity.
    @return Cantidad de Ajustes Anterior */
    public java.math.BigDecimal getXX_PREVIOUSADJUSTMENTSQUANTITY() 
    {
        return get_ValueAsBigDecimal("XX_PREVIOUSADJUSTMENTSQUANTITY");
        
    }
    
    /** Set Sales Amount.
    @param XX_SALESAMOUNT Monto de Ventas */
    public void setXX_SALESAMOUNT (java.math.BigDecimal XX_SALESAMOUNT)
    {
        if (XX_SALESAMOUNT == null) throw new IllegalArgumentException ("XX_SALESAMOUNT is mandatory.");
        set_Value ("XX_SALESAMOUNT", XX_SALESAMOUNT);
        
    }
    
    /** Get Sales Amount.
    @return Monto de Ventas */
    public java.math.BigDecimal getXX_SALESAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_SALESAMOUNT");
        
    }
    
    /** Set Sales Quantity.
    @param XX_SALESQUANTITY Cantidad de Ventas */
    public void setXX_SALESQUANTITY (java.math.BigDecimal XX_SALESQUANTITY)
    {
        if (XX_SALESQUANTITY == null) throw new IllegalArgumentException ("XX_SALESQUANTITY is mandatory.");
        set_Value ("XX_SALESQUANTITY", XX_SALESQUANTITY);
        
    }
    
    /** Get Sales Quantity.
    @return Cantidad de Ventas */
    public java.math.BigDecimal getXX_SALESQUANTITY() 
    {
        return get_ValueAsBigDecimal("XX_SALESQUANTITY");
        
    }
    
    /** Set Shopping Amount.
    @param XX_SHOPPINGAMOUNT Monto de Compra */
    public void setXX_SHOPPINGAMOUNT (java.math.BigDecimal XX_SHOPPINGAMOUNT)
    {
        if (XX_SHOPPINGAMOUNT == null) throw new IllegalArgumentException ("XX_SHOPPINGAMOUNT is mandatory.");
        set_Value ("XX_SHOPPINGAMOUNT", XX_SHOPPINGAMOUNT);
        
    }
    
    /** Get Shopping Amount.
    @return Monto de Compra */
    public java.math.BigDecimal getXX_SHOPPINGAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_SHOPPINGAMOUNT");
        
    }
    
    /** Set Shopping Quantity.
    @param XX_SHOPPINGQUANTITY Cantidad de Compra */
    public void setXX_SHOPPINGQUANTITY (java.math.BigDecimal XX_SHOPPINGQUANTITY)
    {
        if (XX_SHOPPINGQUANTITY == null) throw new IllegalArgumentException ("XX_SHOPPINGQUANTITY is mandatory.");
        set_Value ("XX_SHOPPINGQUANTITY", XX_SHOPPINGQUANTITY);
        
    }
    
    /** Get Shopping Quantity.
    @return Cantidad de Compra */
    public java.math.BigDecimal getXX_SHOPPINGQUANTITY() 
    {
        return get_ValueAsBigDecimal("XX_SHOPPINGQUANTITY");
        
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
    
    /** Set Synchronized.
    @param XX_SyncronizedToINVD53 Synchronized */
    public void setXX_SyncronizedToINVD53 (boolean XX_SyncronizedToINVD53)
    {
        set_Value ("XX_SyncronizedToINVD53", Boolean.valueOf(XX_SyncronizedToINVD53));
        
    }
    
    /** Get Synchronized.
    @return Synchronized */
    public boolean isXX_SyncronizedToINVD53() 
    {
        return get_ValueAsBoolean("XX_SyncronizedToINVD53");
        
    }
    
    /** Set XX_VCN_INVENTORY_ID.
    @param XX_VCN_INVENTORY_ID Id de la Tabla de Inventario */
    public void setXX_VCN_INVENTORY_ID (int XX_VCN_INVENTORY_ID)
    {
        if (XX_VCN_INVENTORY_ID < 1) throw new IllegalArgumentException ("XX_VCN_INVENTORY_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_INVENTORY_ID", Integer.valueOf(XX_VCN_INVENTORY_ID));
        
    }
    
    /** Get XX_VCN_INVENTORY_ID.
    @return Id de la Tabla de Inventario */
    public int getXX_VCN_INVENTORY_ID() 
    {
        return get_ValueAsInt("XX_VCN_INVENTORY_ID");
        
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
    
    
}
