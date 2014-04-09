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
/** Generated Model for XX_VCN_InventoryMovDetail
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_InventoryMovDetail extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_InventoryMovDetail_ID id
    @param trx transaction
    */
    public X_XX_VCN_InventoryMovDetail (Ctx ctx, int XX_VCN_InventoryMovDetail_ID, Trx trx)
    {
        super (ctx, XX_VCN_InventoryMovDetail_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_InventoryMovDetail_ID == 0)
        {
            setXX_ConsecutivePrice (Env.ZERO);
            setXX_INVENTORYMONTH (Env.ZERO);
            setXX_INVENTORYYEAR (Env.ZERO);
            setXX_MOVEMENTAMOUNT (Env.ZERO);
            setXX_MOVEMENTQUANTITY (Env.ZERO);
            setXX_MovementType (null);
            setXX_VCN_InventoryMovDetail_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_InventoryMovDetail (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27604722448789L;
    /** Last Updated Timestamp 2011-11-29 15:35:32.0 */
    public static final long updatedMS = 1322597132000L;
    /** AD_Table_ID=1000753 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_InventoryMovDetail");
        
    }
    ;
    
    /** TableName=XX_VCN_InventoryMovDetail */
    public static final String Table_Name="XX_VCN_InventoryMovDetail";
    
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
    
    /** Set Lot No.
    @param Lot Lot number (alphanumeric) */
    public void setLot (String Lot)
    {
        set_Value ("Lot", Lot);
        
    }
    
    /** Get Lot No.
    @return Lot number (alphanumeric) */
    public String getLot() 
    {
        return (String)get_Value("Lot");
        
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
    
    /** Devolución = 1 */
    public static final String XX_MOVEMENTTYPE_Devolución = X_Ref_XX_MovementType.DEVOLUCIÓN.getValue();
    /** Redistribuciones (Pedidos) = 10 */
    public static final String XX_MOVEMENTTYPE_RedistribucionesPedidos = X_Ref_XX_MovementType.REDISTRIBUCIONES_PEDIDOS.getValue();
    /** Traspasos entre Tiendas = 2 */
    public static final String XX_MOVEMENTTYPE_TraspasosEntreTiendas = X_Ref_XX_MovementType.TRASPASOS_ENTRE_TIENDAS.getValue();
    /** Rebajas Definitivas = 4 */
    public static final String XX_MOVEMENTTYPE_RebajasDefinitivas = X_Ref_XX_MovementType.REBAJAS_DEFINITIVAS.getValue();
    /** Rebajas Promocionales = 7 */
    public static final String XX_MOVEMENTTYPE_RebajasPromocionales = X_Ref_XX_MovementType.REBAJAS_PROMOCIONALES.getValue();
    /** Set  Movement Type.
    @param XX_MovementType  Movement Type */
    public void setXX_MovementType (String XX_MovementType)
    {
        if (XX_MovementType == null) throw new IllegalArgumentException ("XX_MovementType is mandatory");
        if (!X_Ref_XX_MovementType.isValid(XX_MovementType))
        throw new IllegalArgumentException ("XX_MovementType Invalid value - " + XX_MovementType + " - Reference_ID=1000749 - 1 - 10 - 2 - 4 - 7");
        set_Value ("XX_MovementType", XX_MovementType);
        
    }
    
    /** Get  Movement Type.
    @return  Movement Type */
    public String getXX_MovementType() 
    {
        return (String)get_Value("XX_MovementType");
        
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
    
    /** Set XX_VCN_InventoryMovDetail_ID.
    @param XX_VCN_InventoryMovDetail_ID XX_VCN_InventoryMovDetail_ID */
    public void setXX_VCN_InventoryMovDetail_ID (int XX_VCN_InventoryMovDetail_ID)
    {
        if (XX_VCN_InventoryMovDetail_ID < 1) throw new IllegalArgumentException ("XX_VCN_InventoryMovDetail_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_InventoryMovDetail_ID", Integer.valueOf(XX_VCN_InventoryMovDetail_ID));
        
    }
    
    /** Get XX_VCN_InventoryMovDetail_ID.
    @return XX_VCN_InventoryMovDetail_ID */
    public int getXX_VCN_InventoryMovDetail_ID() 
    {
        return get_ValueAsInt("XX_VCN_InventoryMovDetail_ID");
        
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
