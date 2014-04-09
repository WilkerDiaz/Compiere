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
/** Generated Model for E_XX_VCN_INVM14
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_E_XX_VCN_INVM14 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param E_XX_VCN_INVM14_ID id
    @param trx transaction
    */
    public X_E_XX_VCN_INVM14 (Ctx ctx, int E_XX_VCN_INVM14_ID, Trx trx)
    {
        super (ctx, E_XX_VCN_INVM14_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (E_XX_VCN_INVM14_ID == 0)
        {
            setE_XX_VCN_INVM14_ID (0);
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_E_XX_VCN_INVM14 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27566690270789L;
    /** Last Updated Timestamp 2010-09-15 11:05:54.0 */
    public static final long updatedMS = 1284564954000L;
    /** AD_Table_ID=1000374 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("E_XX_VCN_INVM14");
        
    }
    ;
    
    /** TableName=E_XX_VCN_INVM14 */
    public static final String Table_Name="E_XX_VCN_INVM14";
    
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
    
    /** Set E_XX_VCN_INVM14_ID.
    @param E_XX_VCN_INVM14_ID E_XX_VCN_INVM14_ID */
    public void setE_XX_VCN_INVM14_ID (int E_XX_VCN_INVM14_ID)
    {
        if (E_XX_VCN_INVM14_ID < 1) throw new IllegalArgumentException ("E_XX_VCN_INVM14_ID is mandatory.");
        set_ValueNoCheck ("E_XX_VCN_INVM14_ID", Integer.valueOf(E_XX_VCN_INVM14_ID));
        
    }
    
    /** Get E_XX_VCN_INVM14_ID.
    @return E_XX_VCN_INVM14_ID */
    public int getE_XX_VCN_INVM14_ID() 
    {
        return get_ValueAsInt("E_XX_VCN_INVM14_ID");
        
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
    public void setXX_ADJUSTMENTSQUANTITY (int XX_ADJUSTMENTSQUANTITY)
    {
        set_Value ("XX_ADJUSTMENTSQUANTITY", Integer.valueOf(XX_ADJUSTMENTSQUANTITY));
        
    }
    
    /** Get Adjustments Quantity.
    @return Cantidad de Ajuste */
    public int getXX_ADJUSTMENTSQUANTITY() 
    {
        return get_ValueAsInt("XX_ADJUSTMENTSQUANTITY");
        
    }
    
    /** Set Consecutive Price.
    @param XX_ConsecutivePrice Consecutivo de Precio */
    public void setXX_ConsecutivePrice (int XX_ConsecutivePrice)
    {
        set_Value ("XX_ConsecutivePrice", Integer.valueOf(XX_ConsecutivePrice));
        
    }
    
    /** Get Consecutive Price.
    @return Consecutivo de Precio */
    public int getXX_ConsecutivePrice() 
    {
        return get_ValueAsInt("XX_ConsecutivePrice");
        
    }
    
    /** Set Initial Inventory Amount.
    @param XX_INITIALINVENTORYAMOUNT Monto de Inventario Inicial */
    public void setXX_INITIALINVENTORYAMOUNT (java.math.BigDecimal XX_INITIALINVENTORYAMOUNT)
    {
        set_Value ("XX_INITIALINVENTORYAMOUNT", XX_INITIALINVENTORYAMOUNT);
        
    }
    
    /** Get Initial Inventory Amount.
    @return Monto de Inventario Inicial */
    public java.math.BigDecimal getXX_INITIALINVENTORYAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_INITIALINVENTORYAMOUNT");
        
    }
    
    /** Set Initial Inventory Quantity.
    @param XX_INITIALINVENTORYQUANTITY Cantidad de Inventario Inicial */
    public void setXX_INITIALINVENTORYQUANTITY (int XX_INITIALINVENTORYQUANTITY)
    {
        set_Value ("XX_INITIALINVENTORYQUANTITY", Integer.valueOf(XX_INITIALINVENTORYQUANTITY));
        
    }
    
    /** Get Initial Inventory Quantity.
    @return Cantidad de Inventario Inicial */
    public int getXX_INITIALINVENTORYQUANTITY() 
    {
        return get_ValueAsInt("XX_INITIALINVENTORYQUANTITY");
        
    }
    
    /** Set Inventory Month.
    @param XX_INVENTORYMONTH Mes de Inventario */
    public void setXX_INVENTORYMONTH (int XX_INVENTORYMONTH)
    {
        set_Value ("XX_INVENTORYMONTH", Integer.valueOf(XX_INVENTORYMONTH));
        
    }
    
    /** Get Inventory Month.
    @return Mes de Inventario */
    public int getXX_INVENTORYMONTH() 
    {
        return get_ValueAsInt("XX_INVENTORYMONTH");
        
    }
    
    /** Set Inventory Year.
    @param XX_INVENTORYYEAR Ano de Inventario */
    public void setXX_INVENTORYYEAR (int XX_INVENTORYYEAR)
    {
        set_Value ("XX_INVENTORYYEAR", Integer.valueOf(XX_INVENTORYYEAR));
        
    }
    
    /** Get Inventory Year.
    @return Ano de Inventario */
    public int getXX_INVENTORYYEAR() 
    {
        return get_ValueAsInt("XX_INVENTORYYEAR");
        
    }
    
    /** Set Movement Amount.
    @param XX_MOVEMENTAMOUNT Monto de Movimiento */
    public void setXX_MOVEMENTAMOUNT (java.math.BigDecimal XX_MOVEMENTAMOUNT)
    {
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
    public void setXX_MOVEMENTQUANTITY (int XX_MOVEMENTQUANTITY)
    {
        set_Value ("XX_MOVEMENTQUANTITY", Integer.valueOf(XX_MOVEMENTQUANTITY));
        
    }
    
    /** Get Movement Quantity.
    @return Cantidad de Movimiento */
    public int getXX_MOVEMENTQUANTITY() 
    {
        return get_ValueAsInt("XX_MOVEMENTQUANTITY");
        
    }
    
    /** Set Previous Adjustments Amount.
    @param XX_PREVIOUSADJUSTMENTSAMOUNT Monto de Ajustes Anterior */
    public void setXX_PREVIOUSADJUSTMENTSAMOUNT (java.math.BigDecimal XX_PREVIOUSADJUSTMENTSAMOUNT)
    {
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
    public void setXX_PREVIOUSADJUSTMENTSQUANTITY (int XX_PREVIOUSADJUSTMENTSQUANTITY)
    {
        set_Value ("XX_PREVIOUSADJUSTMENTSQUANTITY", Integer.valueOf(XX_PREVIOUSADJUSTMENTSQUANTITY));
        
    }
    
    /** Get Previous Adjustments Quantity.
    @return Cantidad de Ajustes Anterior */
    public int getXX_PREVIOUSADJUSTMENTSQUANTITY() 
    {
        return get_ValueAsInt("XX_PREVIOUSADJUSTMENTSQUANTITY");
        
    }
    
    /** Set Sales Amount.
    @param XX_SALESAMOUNT Monto de Ventas */
    public void setXX_SALESAMOUNT (java.math.BigDecimal XX_SALESAMOUNT)
    {
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
    public void setXX_SALESQUANTITY (int XX_SALESQUANTITY)
    {
        set_Value ("XX_SALESQUANTITY", Integer.valueOf(XX_SALESQUANTITY));
        
    }
    
    /** Get Sales Quantity.
    @return Cantidad de Ventas */
    public int getXX_SALESQUANTITY() 
    {
        return get_ValueAsInt("XX_SALESQUANTITY");
        
    }
    
    /** Set Shopping Amount.
    @param XX_SHOPPINGAMOUNT Monto de Compra */
    public void setXX_SHOPPINGAMOUNT (java.math.BigDecimal XX_SHOPPINGAMOUNT)
    {
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
    public void setXX_SHOPPINGQUANTITY (int XX_SHOPPINGQUANTITY)
    {
        set_Value ("XX_SHOPPINGQUANTITY", Integer.valueOf(XX_SHOPPINGQUANTITY));
        
    }
    
    /** Get Shopping Quantity.
    @return Cantidad de Compra */
    public int getXX_SHOPPINGQUANTITY() 
    {
        return get_ValueAsInt("XX_SHOPPINGQUANTITY");
        
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
