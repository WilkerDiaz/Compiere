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
/** Generated Model for E_XX_VCN_INVD53
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_E_XX_VCN_INVD53 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param E_XX_VCN_INVD53_ID id
    @param trx transaction
    */
    public X_E_XX_VCN_INVD53 (Ctx ctx, int E_XX_VCN_INVD53_ID, Trx trx)
    {
        super (ctx, E_XX_VCN_INVD53_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (E_XX_VCN_INVD53_ID == 0)
        {
            setE_XX_VCN_INVD53_ID (0);
            setName (null);
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_E_XX_VCN_INVD53 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27570855056789L;
    /** Last Updated Timestamp 2010-11-02 15:59:00.0 */
    public static final long updatedMS = 1288729740000L;
    /** AD_Table_ID=1000375 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("E_XX_VCN_INVD53");
        
    }
    ;
    
    /** TableName=E_XX_VCN_INVD53 */
    public static final String Table_Name="E_XX_VCN_INVD53";
    
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
    
    /** Set E_XX_VCN_INVD53_ID.
    @param E_XX_VCN_INVD53_ID E_XX_VCN_INVD53_ID */
    public void setE_XX_VCN_INVD53_ID (int E_XX_VCN_INVD53_ID)
    {
        if (E_XX_VCN_INVD53_ID < 1) throw new IllegalArgumentException ("E_XX_VCN_INVD53_ID is mandatory.");
        set_ValueNoCheck ("E_XX_VCN_INVD53_ID", Integer.valueOf(E_XX_VCN_INVD53_ID));
        
    }
    
    /** Get E_XX_VCN_INVD53_ID.
    @return E_XX_VCN_INVD53_ID */
    public int getE_XX_VCN_INVD53_ID() 
    {
        return get_ValueAsInt("E_XX_VCN_INVD53_ID");
        
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
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
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
    
    /** Set Quantity.
    @param Qty Quantity */
    public void setQty (int Qty)
    {
        set_Value ("Qty", Integer.valueOf(Qty));
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public int getQty() 
    {
        return get_ValueAsInt("Qty");
        
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
    
    /** Set Amount by notification.
    @param XX_Amount Monto del aviso de Credito */
    public void setXX_Amount (java.math.BigDecimal XX_Amount)
    {
        set_Value ("XX_Amount", XX_Amount);
        
    }
    
    /** Get Amount by notification.
    @return Monto del aviso de Credito */
    public java.math.BigDecimal getXX_Amount() 
    {
        return get_ValueAsBigDecimal("XX_Amount");
        
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
    
    /** Set Status.
    @param XX_Status Status */
    public void setXX_Status (int XX_Status)
    {
        set_Value ("XX_Status", Integer.valueOf(XX_Status));
        
    }
    
    /** Get Status.
    @return Status */
    public int getXX_Status() 
    {
        return get_ValueAsInt("XX_Status");
        
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
