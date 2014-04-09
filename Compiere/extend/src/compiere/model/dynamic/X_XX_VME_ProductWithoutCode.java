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
/** Generated Model for XX_VME_ProductWithoutCode
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VME_ProductWithoutCode extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VME_ProductWithoutCode_ID id
    @param trx transaction
    */
    public X_XX_VME_ProductWithoutCode (Ctx ctx, int XX_VME_ProductWithoutCode_ID, Trx trx)
    {
        super (ctx, XX_VME_ProductWithoutCode_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VME_ProductWithoutCode_ID == 0)
        {
            setC_BPartner_ID (0);
            setName (null);
            setPriceActual (Env.ZERO);
            setQtyAvailable (Env.ZERO);
            setValue (null);
            setXX_VME_ProductWithoutCode_ID (0);
            setXX_VMR_Department_ID (0);	// @#XX_VMR_Department_ID@
            setXX_VMR_Line_ID (0);	// @#XX_VMR_Line_ID@
            setXX_VMR_Section_ID (0);	// @#XX_VMR_Section_ID@
            setXX_VMR_VendorProdRef_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VME_ProductWithoutCode (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27604010074789L;
    /** Last Updated Timestamp 2011-11-21 09:42:38.0 */
    public static final long updatedMS = 1321884758000L;
    /** AD_Table_ID=1000438 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VME_ProductWithoutCode");
        
    }
    ;
    
    /** TableName=XX_VME_ProductWithoutCode */
    public static final String Table_Name="XX_VME_ProductWithoutCode";
    
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
        if (C_BPartner_ID < 1) throw new IllegalArgumentException ("C_BPartner_ID is mandatory.");
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
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
    
    /** Set Price.
    @param Price Price */
    public void setPrice (java.math.BigDecimal Price)
    {
        set_Value ("Price", Price);
        
    }
    
    /** Get Price.
    @return Price */
    public java.math.BigDecimal getPrice() 
    {
        return get_ValueAsBigDecimal("Price");
        
    }
    
    /** Set Unit Price.
    @param PriceActual Actual Price */
    public void setPriceActual (java.math.BigDecimal PriceActual)
    {
        if (PriceActual == null) throw new IllegalArgumentException ("PriceActual is mandatory.");
        set_Value ("PriceActual", PriceActual);
        
    }
    
    /** Get Unit Price.
    @return Actual Price */
    public java.math.BigDecimal getPriceActual() 
    {
        return get_ValueAsBigDecimal("PriceActual");
        
    }
    
    /** Set Price.
    @param PriceEntered Price Entered - the price based on the selected/base UoM */
    public void setPriceEntered (java.math.BigDecimal PriceEntered)
    {
        set_Value ("PriceEntered", PriceEntered);
        
    }
    
    /** Get Price.
    @return Price Entered - the price based on the selected/base UoM */
    public java.math.BigDecimal getPriceEntered() 
    {
        return get_ValueAsBigDecimal("PriceEntered");
        
    }
    
    /** Set List Price.
    @param PriceList List Price */
    public void setPriceList (java.math.BigDecimal PriceList)
    {
        set_Value ("PriceList", PriceList);
        
    }
    
    /** Get List Price.
    @return List Price */
    public java.math.BigDecimal getPriceList() 
    {
        return get_ValueAsBigDecimal("PriceList");
        
    }
    
    /** Set Available Quantity.
    @param QtyAvailable Available Quantity (On Hand - Reserved) */
    public void setQtyAvailable (java.math.BigDecimal QtyAvailable)
    {
        if (QtyAvailable == null) throw new IllegalArgumentException ("QtyAvailable is mandatory.");
        set_Value ("QtyAvailable", QtyAvailable);
        
    }
    
    /** Get Available Quantity.
    @return Available Quantity (On Hand - Reserved) */
    public java.math.BigDecimal getQtyAvailable() 
    {
        return get_ValueAsBigDecimal("QtyAvailable");
        
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
    
    /** Set Final Inventory.
    @param XX_VMA_FinalInventory Final Inventory */
    public void setXX_VMA_FinalInventory (int XX_VMA_FinalInventory)
    {
        set_Value ("XX_VMA_FinalInventory", Integer.valueOf(XX_VMA_FinalInventory));
        
    }
    
    /** Get Final Inventory.
    @return Final Inventory */
    public int getXX_VMA_FinalInventory() 
    {
        return get_ValueAsInt("XX_VMA_FinalInventory");
        
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
    
    /** Set Initial Inventory.
    @param XX_VMA_InitialInventory Initial Inventory */
    public void setXX_VMA_InitialInventory (int XX_VMA_InitialInventory)
    {
        set_Value ("XX_VMA_InitialInventory", Integer.valueOf(XX_VMA_InitialInventory));
        
    }
    
    /** Get Initial Inventory.
    @return Initial Inventory */
    public int getXX_VMA_InitialInventory() 
    {
        return get_ValueAsInt("XX_VMA_InitialInventory");
        
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
    
    /** Set Final Amount.
    @param XX_VME_MontFin Final amount of the inventory at the end of the marketing activity.  */
    public void setXX_VME_MontFin (java.math.BigDecimal XX_VME_MontFin)
    {
        set_Value ("XX_VME_MontFin", XX_VME_MontFin);
        
    }
    
    /** Get Final Amount.
    @return Final amount of the inventory at the end of the marketing activity.  */
    public java.math.BigDecimal getXX_VME_MontFin() 
    {
        return get_ValueAsBigDecimal("XX_VME_MontFin");
        
    }
    
    /** Set Initial Amount.
    @param XX_VME_MontIni Initial amount of the inventory at the beginning of the marketing activity. */
    public void setXX_VME_MontIni (java.math.BigDecimal XX_VME_MontIni)
    {
        set_Value ("XX_VME_MontIni", XX_VME_MontIni);
        
    }
    
    /** Get Initial Amount.
    @return Initial amount of the inventory at the beginning of the marketing activity. */
    public java.math.BigDecimal getXX_VME_MontIni() 
    {
        return get_ValueAsBigDecimal("XX_VME_MontIni");
        
    }
    
    /** Set Product without code.
    @param XX_VME_ProductWithoutCode_ID Identifier of the Product without code in Compiere. */
    public void setXX_VME_ProductWithoutCode_ID (int XX_VME_ProductWithoutCode_ID)
    {
        if (XX_VME_ProductWithoutCode_ID < 1) throw new IllegalArgumentException ("XX_VME_ProductWithoutCode_ID is mandatory.");
        set_ValueNoCheck ("XX_VME_ProductWithoutCode_ID", Integer.valueOf(XX_VME_ProductWithoutCode_ID));
        
    }
    
    /** Get Product without code.
    @return Identifier of the Product without code in Compiere. */
    public int getXX_VME_ProductWithoutCode_ID() 
    {
        return get_ValueAsInt("XX_VME_ProductWithoutCode_ID");
        
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
        if (XX_VMR_Department_ID < 1) throw new IllegalArgumentException ("XX_VMR_Department_ID is mandatory.");
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
        if (XX_VMR_Line_ID < 1) throw new IllegalArgumentException ("XX_VMR_Line_ID is mandatory.");
        set_Value ("XX_VMR_Line_ID", Integer.valueOf(XX_VMR_Line_ID));
        
    }
    
    /** Get Line.
    @return Line */
    public int getXX_VMR_Line_ID() 
    {
        return get_ValueAsInt("XX_VMR_Line_ID");
        
    }
    
    /** Set First group of product characteristics.
    @param XX_VMR_ProdWcCharact1 First group of product characteristics */
    public void setXX_VMR_ProdWcCharact1 (String XX_VMR_ProdWcCharact1)
    {
        set_Value ("XX_VMR_ProdWcCharact1", XX_VMR_ProdWcCharact1);
        
    }
    
    /** Get First group of product characteristics.
    @return First group of product characteristics */
    public String getXX_VMR_ProdWcCharact1() 
    {
        return (String)get_Value("XX_VMR_ProdWcCharact1");
        
    }
    
    /** Set Second group of product characteristics.
    @param XX_VMR_ProdWcCharact2 Second group of product characteristics */
    public void setXX_VMR_ProdWcCharact2 (String XX_VMR_ProdWcCharact2)
    {
        set_Value ("XX_VMR_ProdWcCharact2", XX_VMR_ProdWcCharact2);
        
    }
    
    /** Get Second group of product characteristics.
    @return Second group of product characteristics */
    public String getXX_VMR_ProdWcCharact2() 
    {
        return (String)get_Value("XX_VMR_ProdWcCharact2");
        
    }
    
    /** Set Third group of product characteristics.
    @param XX_VMR_ProdWcCharact3 Third group of product characteristics */
    public void setXX_VMR_ProdWcCharact3 (String XX_VMR_ProdWcCharact3)
    {
        set_Value ("XX_VMR_ProdWcCharact3", XX_VMR_ProdWcCharact3);
        
    }
    
    /** Get Third group of product characteristics.
    @return Third group of product characteristics */
    public String getXX_VMR_ProdWcCharact3() 
    {
        return (String)get_Value("XX_VMR_ProdWcCharact3");
        
    }
    
    /** Set Section.
    @param XX_VMR_Section_ID Section */
    public void setXX_VMR_Section_ID (int XX_VMR_Section_ID)
    {
        if (XX_VMR_Section_ID < 1) throw new IllegalArgumentException ("XX_VMR_Section_ID is mandatory.");
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
        if (XX_VMR_VendorProdRef_ID < 1) throw new IllegalArgumentException ("XX_VMR_VendorProdRef_ID is mandatory.");
        set_Value ("XX_VMR_VendorProdRef_ID", Integer.valueOf(XX_VMR_VendorProdRef_ID));
        
    }
    
    /** Get Vendor Product Reference.
    @return Vendor Product Reference */
    public int getXX_VMR_VendorProdRef_ID() 
    {
        return get_ValueAsInt("XX_VMR_VendorProdRef_ID");
        
    }
    
    
}
