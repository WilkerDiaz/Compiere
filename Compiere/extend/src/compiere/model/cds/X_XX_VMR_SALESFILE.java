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
/** Generated Model for XX_VMR_SALESFILE
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_SALESFILE extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_SALESFILE_ID id
    @param trx transaction
    */
    public X_XX_VMR_SALESFILE (Ctx ctx, int XX_VMR_SALESFILE_ID, Trx trx)
    {
        super (ctx, XX_VMR_SALESFILE_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_SALESFILE_ID == 0)
        {
            setName (null);
            setValue (null);
            setXX_VMR_SALESFILE_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_SALESFILE (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27621736126789L;
    /** Last Updated Timestamp 2012-06-13 13:36:50.0 */
    public static final long updatedMS = 1339610810000L;
    /** AD_Table_ID=1002353 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_SALESFILE");
        
    }
    ;
    
    /** TableName=XX_VMR_SALESFILE */
    public static final String Table_Name="XX_VMR_SALESFILE";
    
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
    
    /** Set Vendor.
    @param XX_BPartnerName Vendor */
    public void setXX_BPartnerName (String XX_BPartnerName)
    {
        set_Value ("XX_BPartnerName", XX_BPartnerName);
        
    }
    
    /** Get Vendor.
    @return Vendor */
    public String getXX_BPartnerName() 
    {
        return (String)get_Value("XX_BPartnerName");
        
    }
    
    /** Set Brand.
    @param XX_Brand_Name Brand */
    public void setXX_Brand_Name (String XX_Brand_Name)
    {
        set_Value ("XX_Brand_Name", XX_Brand_Name);
        
    }
    
    /** Get Brand.
    @return Brand */
    public String getXX_Brand_Name() 
    {
        return (String)get_Value("XX_Brand_Name");
        
    }
    
    /** Set Category Code.
    @param XX_CATEGORYCODE Codigo de Categoria */
    public void setXX_CATEGORYCODE (String XX_CATEGORYCODE)
    {
        set_Value ("XX_CATEGORYCODE", XX_CATEGORYCODE);
        
    }
    
    /** Get Category Code.
    @return Codigo de Categoria */
    public String getXX_CATEGORYCODE() 
    {
        return (String)get_Value("XX_CATEGORYCODE");
        
    }
    
    /** Set Day of the week.
    @param XX_DayOfWeek Day of the week */
    public void setXX_DayOfWeek (String XX_DayOfWeek)
    {
        set_Value ("XX_DayOfWeek", XX_DayOfWeek);
        
    }
    
    /** Get Day of the week.
    @return Day of the week */
    public String getXX_DayOfWeek() 
    {
        return (String)get_Value("XX_DayOfWeek");
        
    }
    
    /** Set Department Code.
    @param XX_DepartmentCode Codigo de Departamento */
    public void setXX_DepartmentCode (String XX_DepartmentCode)
    {
        set_Value ("XX_DepartmentCode", XX_DepartmentCode);
        
    }
    
    /** Get Department Code.
    @return Codigo de Departamento */
    public String getXX_DepartmentCode() 
    {
        return (String)get_Value("XX_DepartmentCode");
        
    }
    
    /** Set Line Code.
    @param XX_LineCode Código de línea */
    public void setXX_LineCode (String XX_LineCode)
    {
        set_Value ("XX_LineCode", XX_LineCode);
        
    }
    
    /** Get Line Code.
    @return Código de línea */
    public String getXX_LineCode() 
    {
        return (String)get_Value("XX_LineCode");
        
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
    
    /** Set Product Code.
    @param XX_ProductCode Product Code */
    public void setXX_ProductCode (String XX_ProductCode)
    {
        set_Value ("XX_ProductCode", XX_ProductCode);
        
    }
    
    /** Get Product Code.
    @return Product Code */
    public String getXX_ProductCode() 
    {
        return (String)get_Value("XX_ProductCode");
        
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
    
    /** Set Promotion Quantity.
    @param XX_PromotionQty Promotion Quantity */
    public void setXX_PromotionQty (java.math.BigDecimal XX_PromotionQty)
    {
        set_Value ("XX_PromotionQty", XX_PromotionQty);
        
    }
    
    /** Get Promotion Quantity.
    @return Promotion Quantity */
    public java.math.BigDecimal getXX_PromotionQty() 
    {
        return get_ValueAsBigDecimal("XX_PromotionQty");
        
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
    
    /** Set Sales registry.
    @param XX_SalesReg Sales registry */
    public void setXX_SalesReg (java.math.BigDecimal XX_SalesReg)
    {
        set_Value ("XX_SalesReg", XX_SalesReg);
        
    }
    
    /** Get Sales registry.
    @return Sales registry */
    public java.math.BigDecimal getXX_SalesReg() 
    {
        return get_ValueAsBigDecimal("XX_SalesReg");
        
    }
    
    /** Set Section Code.
    @param XX_SectionCode Section Code */
    public void setXX_SectionCode (String XX_SectionCode)
    {
        set_Value ("XX_SectionCode", XX_SectionCode);
        
    }
    
    /** Get Section Code.
    @return Section Code */
    public String getXX_SectionCode() 
    {
        return (String)get_Value("XX_SectionCode");
        
    }
    
    /** Set Vendor Product Reference.
    @param XX_VendorProdRef Vendor Product Reference */
    public void setXX_VendorProdRef (String XX_VendorProdRef)
    {
        set_Value ("XX_VendorProdRef", XX_VendorProdRef);
        
    }
    
    /** Get Vendor Product Reference.
    @return Vendor Product Reference */
    public String getXX_VendorProdRef() 
    {
        return (String)get_Value("XX_VendorProdRef");
        
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
    
    /** Set XX_VMR_SALESFILE_ID.
    @param XX_VMR_SALESFILE_ID XX_VMR_SALESFILE_ID */
    public void setXX_VMR_SALESFILE_ID (int XX_VMR_SALESFILE_ID)
    {
        if (XX_VMR_SALESFILE_ID < 1) throw new IllegalArgumentException ("XX_VMR_SALESFILE_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_SALESFILE_ID", Integer.valueOf(XX_VMR_SALESFILE_ID));
        
    }
    
    /** Get XX_VMR_SALESFILE_ID.
    @return XX_VMR_SALESFILE_ID */
    public int getXX_VMR_SALESFILE_ID() 
    {
        return get_ValueAsInt("XX_VMR_SALESFILE_ID");
        
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
