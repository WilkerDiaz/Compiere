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
/** Generated Model for XX_VMR_INVENTORYFILE
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_INVENTORYFILE extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_INVENTORYFILE_ID id
    @param trx transaction
    */
    public X_XX_VMR_INVENTORYFILE (Ctx ctx, int XX_VMR_INVENTORYFILE_ID, Trx trx)
    {
        super (ctx, XX_VMR_INVENTORYFILE_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_INVENTORYFILE_ID == 0)
        {
            setValue (null);
            setXX_VMR_INVENTORYFILE_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_INVENTORYFILE (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27631576335789L;
    /** Last Updated Timestamp 2012-10-05 11:00:19.0 */
    public static final long updatedMS = 1349451019000L;
    /** AD_Table_ID=1002254 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_INVENTORYFILE");
        
    }
    ;
    
    /** TableName=XX_VMR_INVENTORYFILE */
    public static final String Table_Name="XX_VMR_INVENTORYFILE";
    
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
    public void setXX_ADJUSTMENTSQUANTITY (java.math.BigDecimal XX_ADJUSTMENTSQUANTITY)
    {
        set_Value ("XX_ADJUSTMENTSQUANTITY", XX_ADJUSTMENTSQUANTITY);
        
    }
    
    /** Get Adjustments Quantity.
    @return Cantidad de Ajuste */
    public java.math.BigDecimal getXX_ADJUSTMENTSQUANTITY() 
    {
        return get_ValueAsBigDecimal("XX_ADJUSTMENTSQUANTITY");
        
    }
    
    /** Set Attribute Set Instance Description.
    @param XX_AttrSetInstanceDesc Attribute Set Instance Description */
    public void setXX_AttrSetInstanceDesc (String XX_AttrSetInstanceDesc)
    {
        set_Value ("XX_AttrSetInstanceDesc", XX_AttrSetInstanceDesc);
        
    }
    
    /** Get Attribute Set Instance Description.
    @return Attribute Set Instance Description */
    public String getXX_AttrSetInstanceDesc() 
    {
        return (String)get_Value("XX_AttrSetInstanceDesc");
        
    }
    
    /** Set XX_CHARACTERISTIC1.
    @param XX_CHARACTERISTIC1 XX_CHARACTERISTIC1 */
    public void setXX_CHARACTERISTIC1 (String XX_CHARACTERISTIC1)
    {
        set_Value ("XX_CHARACTERISTIC1", XX_CHARACTERISTIC1);
        
    }
    
    /** Get XX_CHARACTERISTIC1.
    @return XX_CHARACTERISTIC1 */
    public String getXX_CHARACTERISTIC1() 
    {
        return (String)get_Value("XX_CHARACTERISTIC1");
        
    }
    
    /** Set XX_CHARACTERISTIC2.
    @param XX_CHARACTERISTIC2 XX_CHARACTERISTIC2 */
    public void setXX_CHARACTERISTIC2 (String XX_CHARACTERISTIC2)
    {
        set_Value ("XX_CHARACTERISTIC2", XX_CHARACTERISTIC2);
        
    }
    
    /** Get XX_CHARACTERISTIC2.
    @return XX_CHARACTERISTIC2 */
    public String getXX_CHARACTERISTIC2() 
    {
        return (String)get_Value("XX_CHARACTERISTIC2");
        
    }
    
    /** Set XX_CHARACTERISTIC3.
    @param XX_CHARACTERISTIC3 XX_CHARACTERISTIC3 */
    public void setXX_CHARACTERISTIC3 (String XX_CHARACTERISTIC3)
    {
        set_Value ("XX_CHARACTERISTIC3", XX_CHARACTERISTIC3);
        
    }
    
    /** Get XX_CHARACTERISTIC3.
    @return XX_CHARACTERISTIC3 */
    public String getXX_CHARACTERISTIC3() 
    {
        return (String)get_Value("XX_CHARACTERISTIC3");
        
    }
    
    /** Pedido = P */
    public static final String XX_CONSECUTIVEORIGIN_Pedido = X_Ref_XX_ConsecutiveOrigin.PEDIDO.getValue();
    /** Rebajas = R */
    public static final String XX_CONSECUTIVEORIGIN_Rebajas = X_Ref_XX_ConsecutiveOrigin.REBAJAS.getValue();
    /** Set Consecutive Origin.
    @param XX_ConsecutiveOrigin Consecutive Origin */
    public void setXX_ConsecutiveOrigin (String XX_ConsecutiveOrigin)
    {
        if (!X_Ref_XX_ConsecutiveOrigin.isValid(XX_ConsecutiveOrigin))
        throw new IllegalArgumentException ("XX_ConsecutiveOrigin Invalid value - " + XX_ConsecutiveOrigin + " - Reference_ID=1000250 - P - R");
        set_Value ("XX_ConsecutiveOrigin", XX_ConsecutiveOrigin);
        
    }
    
    /** Get Consecutive Origin.
    @return Consecutive Origin */
    public String getXX_ConsecutiveOrigin() 
    {
        return (String)get_Value("XX_ConsecutiveOrigin");
        
    }
    
    /** Set Entrance Month.
    @param XX_EntranceMonth Entrance Month */
    public void setXX_EntranceMonth (java.math.BigDecimal XX_EntranceMonth)
    {
        set_Value ("XX_EntranceMonth", XX_EntranceMonth);
        
    }
    
    /** Get Entrance Month.
    @return Entrance Month */
    public java.math.BigDecimal getXX_EntranceMonth() 
    {
        return get_ValueAsBigDecimal("XX_EntranceMonth");
        
    }
    
    /** Set Entrance Year.
    @param XX_EntranceYear Entrance Year */
    public void setXX_EntranceYear (java.math.BigDecimal XX_EntranceYear)
    {
        set_Value ("XX_EntranceYear", XX_EntranceYear);
        
    }
    
    /** Get Entrance Year.
    @return Entrance Year */
    public java.math.BigDecimal getXX_EntranceYear() 
    {
        return get_ValueAsBigDecimal("XX_EntranceYear");
        
    }
    
    /** Set Total Bs..
    @param XX_FinalInventoryAmount Total Bs. */
    public void setXX_FinalInventoryAmount (java.math.BigDecimal XX_FinalInventoryAmount)
    {
        set_Value ("XX_FinalInventoryAmount", XX_FinalInventoryAmount);
        
    }
    
    /** Get Total Bs..
    @return Total Bs. */
    public java.math.BigDecimal getXX_FinalInventoryAmount() 
    {
        return get_ValueAsBigDecimal("XX_FinalInventoryAmount");
        
    }
    
    /** Set Pieces.
    @param XX_FinalInventoryQuantity Pieces */
    public void setXX_FinalInventoryQuantity (int XX_FinalInventoryQuantity)
    {
        set_Value ("XX_FinalInventoryQuantity", Integer.valueOf(XX_FinalInventoryQuantity));
        
    }
    
    /** Get Pieces.
    @return Pieces */
    public int getXX_FinalInventoryQuantity() 
    {
        return get_ValueAsInt("XX_FinalInventoryQuantity");
        
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
    public void setXX_INITIALINVENTORYQUANTITY (java.math.BigDecimal XX_INITIALINVENTORYQUANTITY)
    {
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
        set_Value ("XX_INVENTORYYEAR", XX_INVENTORYYEAR);
        
    }
    
    /** Get Inventory Year.
    @return Ano de Inventario */
    public java.math.BigDecimal getXX_INVENTORYYEAR() 
    {
        return get_ValueAsBigDecimal("XX_INVENTORYYEAR");
        
    }
    
    /** Set Long Characteristic Name.
    @param XX_LongCharName Long Characteristic Name */
    public void setXX_LongCharName (String XX_LongCharName)
    {
        set_Value ("XX_LongCharName", XX_LongCharName);
        
    }
    
    /** Get Long Characteristic Name.
    @return Long Characteristic Name */
    public String getXX_LongCharName() 
    {
        return (String)get_Value("XX_LongCharName");
        
    }
    
    /** Set Margin.
    @param XX_Margin Margin */
    public void setXX_Margin (java.math.BigDecimal XX_Margin)
    {
        set_Value ("XX_Margin", XX_Margin);
        
    }
    
    /** Get Margin.
    @return Margin */
    public java.math.BigDecimal getXX_Margin() 
    {
        return get_ValueAsBigDecimal("XX_Margin");
        
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
    public void setXX_MOVEMENTQUANTITY (java.math.BigDecimal XX_MOVEMENTQUANTITY)
    {
        set_Value ("XX_MOVEMENTQUANTITY", XX_MOVEMENTQUANTITY);
        
    }
    
    /** Get Movement Quantity.
    @return Cantidad de Movimiento */
    public java.math.BigDecimal getXX_MOVEMENTQUANTITY() 
    {
        return get_ValueAsBigDecimal("XX_MOVEMENTQUANTITY");
        
    }
    
    /** Set PriceConsecutive.
    @param XX_PriceConsecutive PriceConsecutive */
    public void setXX_PriceConsecutive (int XX_PriceConsecutive)
    {
        set_Value ("XX_PriceConsecutive", Integer.valueOf(XX_PriceConsecutive));
        
    }
    
    /** Get PriceConsecutive.
    @return PriceConsecutive */
    public int getXX_PriceConsecutive() 
    {
        return get_ValueAsInt("XX_PriceConsecutive");
        
    }
    
    /** Set Product Name.
    @param XX_Product_Name Product Name */
    public void setXX_Product_Name (String XX_Product_Name)
    {
        set_Value ("XX_Product_Name", XX_Product_Name);
        
    }
    
    /** Get Product Name.
    @return Product Name */
    public String getXX_Product_Name() 
    {
        return (String)get_Value("XX_Product_Name");
        
    }
    
    /** Set Promotion Expenses.
    @param XX_PromotionExpenses Promotion Expenses */
    public void setXX_PromotionExpenses (java.math.BigDecimal XX_PromotionExpenses)
    {
        set_Value ("XX_PromotionExpenses", XX_PromotionExpenses);
        
    }
    
    /** Get Promotion Expenses.
    @return Promotion Expenses */
    public java.math.BigDecimal getXX_PromotionExpenses() 
    {
        return get_ValueAsBigDecimal("XX_PromotionExpenses");
        
    }
    
    /** Set Reserved Quantity.
    @param XX_ReservedQty Reserved Quantity */
    public void setXX_ReservedQty (java.math.BigDecimal XX_ReservedQty)
    {
        set_Value ("XX_ReservedQty", XX_ReservedQty);
        
    }
    
    /** Get Reserved Quantity.
    @return Reserved Quantity */
    public java.math.BigDecimal getXX_ReservedQty() 
    {
        return get_ValueAsBigDecimal("XX_ReservedQty");
        
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
    public void setXX_SALESQUANTITY (java.math.BigDecimal XX_SALESQUANTITY)
    {
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
        set_Value ("XX_SHOPPINGQUANTITY", XX_SHOPPINGQUANTITY);
        
    }
    
    /** Get Shopping Quantity.
    @return Cantidad de Compra */
    public java.math.BigDecimal getXX_SHOPPINGQUANTITY() 
    {
        return get_ValueAsBigDecimal("XX_SHOPPINGQUANTITY");
        
    }
    
    /** Set Type Inventory.
    @param XX_TypeInventory Type Inventory */
    public void setXX_TypeInventory (String XX_TypeInventory)
    {
        set_Value ("XX_TypeInventory", XX_TypeInventory);
        
    }
    
    /** Get Type Inventory.
    @return Type Inventory */
    public String getXX_TypeInventory() 
    {
        return (String)get_Value("XX_TypeInventory");
        
    }
    
    /** Set Concept Value.
    @param XX_VME_ConceptValue_ID Concept Value */
    public void setXX_VME_ConceptValue_ID (int XX_VME_ConceptValue_ID)
    {
        if (XX_VME_ConceptValue_ID <= 0) set_Value ("XX_VME_ConceptValue_ID", null);
        else
        set_Value ("XX_VME_ConceptValue_ID", Integer.valueOf(XX_VME_ConceptValue_ID));
        
    }
    
    /** Get Concept Value.
    @return Concept Value */
    public int getXX_VME_ConceptValue_ID() 
    {
        return get_ValueAsInt("XX_VME_ConceptValue_ID");
        
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
    
    /** Set XX_VMR_INVENTORYFILE_ID.
    @param XX_VMR_INVENTORYFILE_ID XX_VMR_INVENTORYFILE_ID */
    public void setXX_VMR_INVENTORYFILE_ID (int XX_VMR_INVENTORYFILE_ID)
    {
        if (XX_VMR_INVENTORYFILE_ID < 1) throw new IllegalArgumentException ("XX_VMR_INVENTORYFILE_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_INVENTORYFILE_ID", Integer.valueOf(XX_VMR_INVENTORYFILE_ID));
        
    }
    
    /** Get XX_VMR_INVENTORYFILE_ID.
    @return XX_VMR_INVENTORYFILE_ID */
    public int getXX_VMR_INVENTORYFILE_ID() 
    {
        return get_ValueAsInt("XX_VMR_INVENTORYFILE_ID");
        
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
    
    /** Set Store Number.
    @param XX_WarehouseBecoNumber Store Number */
    public void setXX_WarehouseBecoNumber (int XX_WarehouseBecoNumber)
    {
        set_Value ("XX_WarehouseBecoNumber", Integer.valueOf(XX_WarehouseBecoNumber));
        
    }
    
    /** Get Store Number.
    @return Store Number */
    public int getXX_WarehouseBecoNumber() 
    {
        return get_ValueAsInt("XX_WarehouseBecoNumber");
        
    }
    
    
}
