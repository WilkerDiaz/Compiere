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
/** Generated Model for XX_VME_Elements
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VME_Elements extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VME_Elements_ID id
    @param trx transaction
    */
    public X_XX_VME_Elements (Ctx ctx, int XX_VME_Elements_ID, Trx trx)
    {
        super (ctx, XX_VME_Elements_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VME_Elements_ID == 0)
        {
            setName (null);
            setValue (null);
            setXX_VME_Elements_ID (0);
            setXX_VME_QTYPUBLISHED (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VME_Elements (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27629541469789L;
    /** Last Updated Timestamp 2012-09-11 21:45:53.0 */
    public static final long updatedMS = 1347416153000L;
    /** AD_Table_ID=1000953 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VME_Elements");
        
    }
    ;
    
    /** TableName=XX_VME_Elements */
    public static final String Table_Name="XX_VME_Elements";
    
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
    
    /** Set Consult PO.
    @param XX_ConsultPO Consult PO */
    public void setXX_ConsultPO (String XX_ConsultPO)
    {
        set_Value ("XX_ConsultPO", XX_ConsultPO);
        
    }
    
    /** Get Consult PO.
    @return Consult PO */
    public String getXX_ConsultPO() 
    {
        return (String)get_Value("XX_ConsultPO");
        
    }
    
    /** Set Samples Quantity.
    @param XX_SamplesQty Samples Quantity */
    public void setXX_SamplesQty (java.math.BigDecimal XX_SamplesQty)
    {
        set_Value ("XX_SamplesQty", XX_SamplesQty);
        
    }
    
    /** Get Samples Quantity.
    @return Samples Quantity */
    public java.math.BigDecimal getXX_SamplesQty() 
    {
        return get_ValueAsBigDecimal("XX_SamplesQty");
        
    }
    
    /** Set Brochure Page.
    @param XX_VMA_BrochurePage_ID Identifier of the Brochure Page. */
    public void setXX_VMA_BrochurePage_ID (int XX_VMA_BrochurePage_ID)
    {
        if (XX_VMA_BrochurePage_ID <= 0) set_Value ("XX_VMA_BrochurePage_ID", null);
        else
        set_Value ("XX_VMA_BrochurePage_ID", Integer.valueOf(XX_VMA_BrochurePage_ID));
        
    }
    
    /** Get Brochure Page.
    @return Identifier of the Brochure Page. */
    public int getXX_VMA_BrochurePage_ID() 
    {
        return get_ValueAsInt("XX_VMA_BrochurePage_ID");
        
    }
    
    /** Set Add Vendor Reference.
    @param XX_VME_AddVendorReference Add Vendor Reference */
    public void setXX_VME_AddVendorReference (String XX_VME_AddVendorReference)
    {
        set_Value ("XX_VME_AddVendorReference", XX_VME_AddVendorReference);
        
    }
    
    /** Get Add Vendor Reference.
    @return Add Vendor Reference */
    public String getXX_VME_AddVendorReference() 
    {
        return (String)get_Value("XX_VME_AddVendorReference");
        
    }
    
    /** Set Change Element to Page.
    @param XX_VME_ChangeElementPage Change the element to another page inside the brochure. */
    public void setXX_VME_ChangeElementPage (String XX_VME_ChangeElementPage)
    {
        set_Value ("XX_VME_ChangeElementPage", XX_VME_ChangeElementPage);
        
    }
    
    /** Get Change Element to Page.
    @return Change the element to another page inside the brochure. */
    public String getXX_VME_ChangeElementPage() 
    {
        return (String)get_Value("XX_VME_ChangeElementPage");
        
    }
    
    /** Set Product characteristics to publish.
    @param XX_VME_CharactPublished Product characteristics to publish */
    public void setXX_VME_CharactPublished (String XX_VME_CharactPublished)
    {
        set_Value ("XX_VME_CharactPublished", XX_VME_CharactPublished);
        
    }
    
    /** Get Product characteristics to publish.
    @return Product characteristics to publish */
    public String getXX_VME_CharactPublished() 
    {
        return (String)get_Value("XX_VME_CharactPublished");
        
    }
    
    /** Set Discount Percentage.
    @param XX_VME_DiscountPercentage Discount Percentage */
    public void setXX_VME_DiscountPercentage (java.math.BigDecimal XX_VME_DiscountPercentage)
    {
        set_Value ("XX_VME_DiscountPercentage", XX_VME_DiscountPercentage);
        
    }
    
    /** Get Discount Percentage.
    @return Discount Percentage */
    public java.math.BigDecimal getXX_VME_DiscountPercentage() 
    {
        return get_ValueAsBigDecimal("XX_VME_DiscountPercentage");
        
    }
    
    /** Set Dynamic Price.
    @param XX_VME_DynamicPrice Dynamic Price */
    public void setXX_VME_DynamicPrice (java.math.BigDecimal XX_VME_DynamicPrice)
    {
        set_Value ("XX_VME_DynamicPrice", XX_VME_DynamicPrice);
        
    }
    
    /** Get Dynamic Price.
    @return Dynamic Price */
    public java.math.BigDecimal getXX_VME_DynamicPrice() 
    {
        return get_ValueAsBigDecimal("XX_VME_DynamicPrice");
        
    }
    
    /** Set XX_VME_Elements_ID.
    @param XX_VME_Elements_ID XX_VME_Elements_ID */
    public void setXX_VME_Elements_ID (int XX_VME_Elements_ID)
    {
        if (XX_VME_Elements_ID < 1) throw new IllegalArgumentException ("XX_VME_Elements_ID is mandatory.");
        set_ValueNoCheck ("XX_VME_Elements_ID", Integer.valueOf(XX_VME_Elements_ID));
        
    }
    
    /** Get XX_VME_Elements_ID.
    @return XX_VME_Elements_ID */
    public int getXX_VME_Elements_ID() 
    {
        return get_ValueAsInt("XX_VME_Elements_ID");
        
    }
    
    /** Set Estimated Sale Quantity.
    @param XX_VME_EstimatedQty Estimated Sale Quantity */
    public void setXX_VME_EstimatedQty (java.math.BigDecimal XX_VME_EstimatedQty)
    {
        set_Value ("XX_VME_EstimatedQty", XX_VME_EstimatedQty);
        
    }
    
    /** Get Estimated Sale Quantity.
    @return Estimated Sale Quantity */
    public java.math.BigDecimal getXX_VME_EstimatedQty() 
    {
        return get_ValueAsBigDecimal("XX_VME_EstimatedQty");
        
    }
    
    /** Set Group Of Elements.
    @param XX_VME_GroupOfElements_ID Group Of Elements */
    public void setXX_VME_GroupOfElements_ID (int XX_VME_GroupOfElements_ID)
    {
        if (XX_VME_GroupOfElements_ID <= 0) set_Value ("XX_VME_GroupOfElements_ID", null);
        else
        set_Value ("XX_VME_GroupOfElements_ID", Integer.valueOf(XX_VME_GroupOfElements_ID));
        
    }
    
    /** Get Group Of Elements.
    @return Group Of Elements */
    public int getXX_VME_GroupOfElements_ID() 
    {
        return get_ValueAsInt("XX_VME_GroupOfElements_ID");
        
    }
    
    /** Set Import References.
    @param XX_VME_ImportRefExcel Import References */
    public void setXX_VME_ImportRefExcel (String XX_VME_ImportRefExcel)
    {
        set_Value ("XX_VME_ImportRefExcel", XX_VME_ImportRefExcel);
        
    }
    
    /** Get Import References.
    @return Import References */
    public String getXX_VME_ImportRefExcel() 
    {
        return (String)get_Value("XX_VME_ImportRefExcel");
        
    }
    
    /** Set Is Basic.
    @param XX_VME_IsBasic Establishes if the element of  a Marketing Activity is Basic or not. */
    public void setXX_VME_IsBasic (boolean XX_VME_IsBasic)
    {
        set_Value ("XX_VME_IsBasic", Boolean.valueOf(XX_VME_IsBasic));
        
    }
    
    /** Get Is Basic.
    @return Establishes if the element of  a Marketing Activity is Basic or not. */
    public boolean isXX_VME_IsBasic() 
    {
        return get_ValueAsBoolean("XX_VME_IsBasic");
        
    }
    
    /** Set Is Oportunity.
    @param XX_VME_IsOportunity Establishes if the element of  a Marketing Activity is of Oportunity or not. */
    public void setXX_VME_IsOportunity (boolean XX_VME_IsOportunity)
    {
        set_Value ("XX_VME_IsOportunity", Boolean.valueOf(XX_VME_IsOportunity));
        
    }
    
    /** Get Is Oportunity.
    @return Establishes if the element of  a Marketing Activity is of Oportunity or not. */
    public boolean isXX_VME_IsOportunity() 
    {
        return get_ValueAsBoolean("XX_VME_IsOportunity");
        
    }
    
    /** Set Is Star.
    @param XX_VME_IsStar Establishes if the element of  a Marketing Activity is Star or not. */
    public void setXX_VME_IsStar (boolean XX_VME_IsStar)
    {
        set_Value ("XX_VME_IsStar", Boolean.valueOf(XX_VME_IsStar));
        
    }
    
    /** Get Is Star.
    @return Establishes if the element of  a Marketing Activity is Star or not. */
    public boolean isXX_VME_IsStar() 
    {
        return get_ValueAsBoolean("XX_VME_IsStar");
        
    }
    
    /** Set Is Tendence.
    @param XX_VME_IsTendence Establishes if the element of  a Marketing Activity is of Tendence or not. */
    public void setXX_VME_IsTendence (boolean XX_VME_IsTendence)
    {
        set_Value ("XX_VME_IsTendence", Boolean.valueOf(XX_VME_IsTendence));
        
    }
    
    /** Get Is Tendence.
    @return Establishes if the element of  a Marketing Activity is of Tendence or not. */
    public boolean isXX_VME_IsTendence() 
    {
        return get_ValueAsBoolean("XX_VME_IsTendence");
        
    }
    
    /** Set Market Price.
    @param XX_VME_MarketPrice Market Price */
    public void setXX_VME_MarketPrice (java.math.BigDecimal XX_VME_MarketPrice)
    {
        set_Value ("XX_VME_MarketPrice", XX_VME_MarketPrice);
        
    }
    
    /** Get Market Price.
    @return Market Price */
    public java.math.BigDecimal getXX_VME_MarketPrice() 
    {
        return get_ValueAsBigDecimal("XX_VME_MarketPrice");
        
    }
    
    /** Set Modify Vendor Reference.
    @param XX_VME_ModifyVendorReference Modify Vendor Reference */
    public void setXX_VME_ModifyVendorReference (String XX_VME_ModifyVendorReference)
    {
        set_Value ("XX_VME_ModifyVendorReference", XX_VME_ModifyVendorReference);
        
    }
    
    /** Get Modify Vendor Reference.
    @return Modify Vendor Reference */
    public String getXX_VME_ModifyVendorReference() 
    {
        return (String)get_Value("XX_VME_ModifyVendorReference");
        
    }
    
    /** Set Outstanding product.
    @param XX_VME_Outstanding Outstanding product */
    public void setXX_VME_Outstanding (boolean XX_VME_Outstanding)
    {
        set_Value ("XX_VME_Outstanding", Boolean.valueOf(XX_VME_Outstanding));
        
    }
    
    /** Get Outstanding product.
    @return Outstanding product */
    public boolean isXX_VME_Outstanding() 
    {
        return get_ValueAsBoolean("XX_VME_Outstanding");
        
    }
    
    /** Set Dynamic Promotional Price.
    @param XX_VME_PromoDynPrice Dynamic Promotional Price */
    public void setXX_VME_PromoDynPrice (java.math.BigDecimal XX_VME_PromoDynPrice)
    {
        set_Value ("XX_VME_PromoDynPrice", XX_VME_PromoDynPrice);
        
    }
    
    /** Get Dynamic Promotional Price.
    @return Dynamic Promotional Price */
    public java.math.BigDecimal getXX_VME_PromoDynPrice() 
    {
        return get_ValueAsBigDecimal("XX_VME_PromoDynPrice");
        
    }
    
    /** Set Publicity Contribution.
    @param XX_VME_PublicityContribution Is the amount invested by the Product inside the publicitary investment. */
    public void setXX_VME_PublicityContribution (java.math.BigDecimal XX_VME_PublicityContribution)
    {
        set_Value ("XX_VME_PublicityContribution", XX_VME_PublicityContribution);
        
    }
    
    /** Get Publicity Contribution.
    @return Is the amount invested by the Product inside the publicitary investment. */
    public java.math.BigDecimal getXX_VME_PublicityContribution() 
    {
        return get_ValueAsBigDecimal("XX_VME_PublicityContribution");
        
    }
    
    /** Set Dynamic quantity to publish.
    @param XX_VME_QTYPUBLISHED Dynamic quantity to publish */
    public void setXX_VME_QTYPUBLISHED (java.math.BigDecimal XX_VME_QTYPUBLISHED)
    {
        if (XX_VME_QTYPUBLISHED == null) throw new IllegalArgumentException ("XX_VME_QTYPUBLISHED is mandatory.");
        set_Value ("XX_VME_QTYPUBLISHED", XX_VME_QTYPUBLISHED);
        
    }
    
    /** Get Dynamic quantity to publish.
    @return Dynamic quantity to publish */
    public java.math.BigDecimal getXX_VME_QTYPUBLISHED() 
    {
        return get_ValueAsBigDecimal("XX_VME_QTYPUBLISHED");
        
    }
    
    /** Set Quantity of references associated at the element.
    @param XX_VME_QtyRefAssociated Quantity of references associated at the element */
    public void setXX_VME_QtyRefAssociated (java.math.BigDecimal XX_VME_QtyRefAssociated)
    {
        set_Value ("XX_VME_QtyRefAssociated", XX_VME_QtyRefAssociated);
        
    }
    
    /** Get Quantity of references associated at the element.
    @return Quantity of references associated at the element */
    public java.math.BigDecimal getXX_VME_QtyRefAssociated() 
    {
        return get_ValueAsBigDecimal("XX_VME_QtyRefAssociated");
        
    }
    
    /** Set Relationship element - brands.
    @param XX_VME_RelBrand Relationship element - brands */
    public void setXX_VME_RelBrand (String XX_VME_RelBrand)
    {
        set_Value ("XX_VME_RelBrand", XX_VME_RelBrand);
        
    }
    
    /** Get Relationship element - brands.
    @return Relationship element - brands */
    public String getXX_VME_RelBrand() 
    {
        return (String)get_Value("XX_VME_RelBrand");
        
    }
    
    /** G - Group of products = G */
    public static final String XX_VME_TYPE_G_GroupOfProducts = X_Ref_XX_VME_Type.G__GROUP_OF_PRODUCTS.getValue();
    /** I - Image = I */
    public static final String XX_VME_TYPE_I_Image = X_Ref_XX_VME_Type.I__IMAGE.getValue();
    /** N - Product without code = N */
    public static final String XX_VME_TYPE_N_ProductWithoutCode = X_Ref_XX_VME_Type.N__PRODUCT_WITHOUT_CODE.getValue();
    /** P - Product = P */
    public static final String XX_VME_TYPE_P_Product = X_Ref_XX_VME_Type.P__PRODUCT.getValue();
    /** R - Vendor Reference = R */
    public static final String XX_VME_TYPE_R_VendorReference = X_Ref_XX_VME_Type.R__VENDOR_REFERENCE.getValue();
    /** Set Type.
    @param XX_VME_Type Is the type of the element contained in a Marketing Activity. */
    public void setXX_VME_Type (String XX_VME_Type)
    {
        if (!X_Ref_XX_VME_Type.isValid(XX_VME_Type))
        throw new IllegalArgumentException ("XX_VME_Type Invalid value - " + XX_VME_Type + " - Reference_ID=1000332 - G - I - N - P - R");
        set_Value ("XX_VME_Type", XX_VME_Type);
        
    }
    
    /** Get Type.
    @return Is the type of the element contained in a Marketing Activity. */
    public String getXX_VME_Type() 
    {
        return (String)get_Value("XX_VME_Type");
        
    }
    
    /** Set Element Validated.
    @param XX_VME_Validated Element Validated */
    public void setXX_VME_Validated (boolean XX_VME_Validated)
    {
        set_Value ("XX_VME_Validated", Boolean.valueOf(XX_VME_Validated));
        
    }
    
    /** Get Element Validated.
    @return Element Validated */
    public boolean isXX_VME_Validated() 
    {
        return get_ValueAsBoolean("XX_VME_Validated");
        
    }
    
    /** Set Validate Element.
    @param XX_VME_ValidateElement Validate Element */
    public void setXX_VME_ValidateElement (String XX_VME_ValidateElement)
    {
        set_Value ("XX_VME_ValidateElement", XX_VME_ValidateElement);
        
    }
    
    /** Get Validate Element.
    @return Validate Element */
    public String getXX_VME_ValidateElement() 
    {
        return (String)get_Value("XX_VME_ValidateElement");
        
    }
    
    
}
