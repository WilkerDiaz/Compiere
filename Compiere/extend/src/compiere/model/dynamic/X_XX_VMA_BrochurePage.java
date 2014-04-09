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
/** Generated Model for XX_VMA_BrochurePage
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMA_BrochurePage extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMA_BrochurePage_ID id
    @param trx transaction
    */
    public X_XX_VMA_BrochurePage (Ctx ctx, int XX_VMA_BrochurePage_ID, Trx trx)
    {
        super (ctx, XX_VMA_BrochurePage_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMA_BrochurePage_ID == 0)
        {
            setName (null);
            setXX_VMA_Brochure_ID (0);
            setXX_VMA_BrochurePage_ID (0);
            setXX_VMA_PageNumber (0);
            setXX_VMA_PageType (null);
            setXX_VMA_Topic (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMA_BrochurePage (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27629541123789L;
    /** Last Updated Timestamp 2012-09-11 21:40:07.0 */
    public static final long updatedMS = 1347415807000L;
    /** AD_Table_ID=1000413 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMA_BrochurePage");
        
    }
    ;
    
    /** TableName=XX_VMA_BrochurePage */
    public static final String Table_Name="XX_VMA_BrochurePage";
    
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
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Consult PO.
    @param XX_ConsultPOForm Consult PO */
    public void setXX_ConsultPOForm (String XX_ConsultPOForm)
    {
        set_Value ("XX_ConsultPOForm", XX_ConsultPOForm);
        
    }
    
    /** Get Consult PO.
    @return Consult PO */
    public String getXX_ConsultPOForm() 
    {
        return (String)get_Value("XX_ConsultPOForm");
        
    }
    
    /** Set Add department.
    @param XX_VMA_AddDepartment Asociated a department with the page */
    public void setXX_VMA_AddDepartment (String XX_VMA_AddDepartment)
    {
        set_Value ("XX_VMA_AddDepartment", XX_VMA_AddDepartment);
        
    }
    
    /** Get Add department.
    @return Asociated a department with the page */
    public String getXX_VMA_AddDepartment() 
    {
        return (String)get_Value("XX_VMA_AddDepartment");
        
    }
    
    /** Set Brochure.
    @param XX_VMA_Brochure_ID Identifier of the Brochure. */
    public void setXX_VMA_Brochure_ID (int XX_VMA_Brochure_ID)
    {
        if (XX_VMA_Brochure_ID < 1) throw new IllegalArgumentException ("XX_VMA_Brochure_ID is mandatory.");
        set_ValueNoCheck ("XX_VMA_Brochure_ID", Integer.valueOf(XX_VMA_Brochure_ID));
        
    }
    
    /** Get Brochure.
    @return Identifier of the Brochure. */
    public int getXX_VMA_Brochure_ID() 
    {
        return get_ValueAsInt("XX_VMA_Brochure_ID");
        
    }
    
    /** Set Brochure Page.
    @param XX_VMA_BrochurePage_ID Identifier of the Brochure Page. */
    public void setXX_VMA_BrochurePage_ID (int XX_VMA_BrochurePage_ID)
    {
        if (XX_VMA_BrochurePage_ID < 1) throw new IllegalArgumentException ("XX_VMA_BrochurePage_ID is mandatory.");
        set_ValueNoCheck ("XX_VMA_BrochurePage_ID", Integer.valueOf(XX_VMA_BrochurePage_ID));
        
    }
    
    /** Get Brochure Page.
    @return Identifier of the Brochure Page. */
    public int getXX_VMA_BrochurePage_ID() 
    {
        return get_ValueAsInt("XX_VMA_BrochurePage_ID");
        
    }
    
    /** Set Category editable.
    @param XX_VMA_CategoryEditable Category editable */
    public void setXX_VMA_CategoryEditable (java.math.BigDecimal XX_VMA_CategoryEditable)
    {
        throw new IllegalArgumentException ("XX_VMA_CategoryEditable is virtual column");
        
    }
    
    /** Get Category editable.
    @return Category editable */
    public java.math.BigDecimal getXX_VMA_CategoryEditable() 
    {
        return get_ValueAsBigDecimal("XX_VMA_CategoryEditable");
        
    }
    
    /** Set Drop Department.
    @param XX_VMA_DropDepartment Unlink a department from a page */
    public void setXX_VMA_DropDepartment (String XX_VMA_DropDepartment)
    {
        set_Value ("XX_VMA_DropDepartment", XX_VMA_DropDepartment);
        
    }
    
    /** Get Drop Department.
    @return Unlink a department from a page */
    public String getXX_VMA_DropDepartment() 
    {
        return (String)get_Value("XX_VMA_DropDepartment");
        
    }
    
    /** Set Import Products.
    @param XX_VMA_ImportProducts Import Products */
    public void setXX_VMA_ImportProducts (String XX_VMA_ImportProducts)
    {
        set_Value ("XX_VMA_ImportProducts", XX_VMA_ImportProducts);
        
    }
    
    /** Get Import Products.
    @return Import Products */
    public String getXX_VMA_ImportProducts() 
    {
        return (String)get_Value("XX_VMA_ImportProducts");
        
    }
    
    /** Set Is Brochure Page Active.
    @param XX_VMA_IsBrochurePActive The page is active or not */
    public void setXX_VMA_IsBrochurePActive (boolean XX_VMA_IsBrochurePActive)
    {
        set_Value ("XX_VMA_IsBrochurePActive", Boolean.valueOf(XX_VMA_IsBrochurePActive));
        
    }
    
    /** Get Is Brochure Page Active.
    @return The page is active or not */
    public boolean isXX_VMA_IsBrochurePActive() 
    {
        return get_ValueAsBoolean("XX_VMA_IsBrochurePActive");
        
    }
    
    /** Set Page Dept relation.
    @param XX_VMA_PageDept_V_ID relation between a page and his departments */
    public void setXX_VMA_PageDept_V_ID (int XX_VMA_PageDept_V_ID)
    {
        if (XX_VMA_PageDept_V_ID <= 0) set_Value ("XX_VMA_PageDept_V_ID", null);
        else
        set_Value ("XX_VMA_PageDept_V_ID", Integer.valueOf(XX_VMA_PageDept_V_ID));
        
    }
    
    /** Get Page Dept relation.
    @return relation between a page and his departments */
    public int getXX_VMA_PageDept_V_ID() 
    {
        return get_ValueAsInt("XX_VMA_PageDept_V_ID");
        
    }
    
    /** Set Page Number.
    @param XX_VMA_PageNumber Number of the Page. */
    public void setXX_VMA_PageNumber (int XX_VMA_PageNumber)
    {
        set_Value ("XX_VMA_PageNumber", Integer.valueOf(XX_VMA_PageNumber));
        
    }
    
    /** Get Page Number.
    @return Number of the Page. */
    public int getXX_VMA_PageNumber() 
    {
        return get_ValueAsInt("XX_VMA_PageNumber");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getXX_VMA_PageNumber()));
        
    }
    
    /** I - Image = I */
    public static final String XX_VMA_PAGETYPE_I_Image = X_Ref_XX_VMA_PageType.I__IMAGE.getValue();
    /** M - Mixed = M */
    public static final String XX_VMA_PAGETYPE_M_Mixed = X_Ref_XX_VMA_PageType.M__MIXED.getValue();
    /** P - Product = P */
    public static final String XX_VMA_PAGETYPE_P_Product = X_Ref_XX_VMA_PageType.P__PRODUCT.getValue();
    /** Set Page Type.
    @param XX_VMA_PageType Type of the Page. */
    public void setXX_VMA_PageType (String XX_VMA_PageType)
    {
        if (XX_VMA_PageType == null) throw new IllegalArgumentException ("XX_VMA_PageType is mandatory");
        if (!X_Ref_XX_VMA_PageType.isValid(XX_VMA_PageType))
        throw new IllegalArgumentException ("XX_VMA_PageType Invalid value - " + XX_VMA_PageType + " - Reference_ID=1000324 - I - M - P");
        set_Value ("XX_VMA_PageType", XX_VMA_PageType);
        
    }
    
    /** Get Page Type.
    @return Type of the Page. */
    public String getXX_VMA_PageType() 
    {
        return (String)get_Value("XX_VMA_PageType");
        
    }
    
    /** Set Topic.
    @param XX_VMA_Topic Is the theme that the company wants to promote. */
    public void setXX_VMA_Topic (String XX_VMA_Topic)
    {
        if (XX_VMA_Topic == null) throw new IllegalArgumentException ("XX_VMA_Topic is mandatory.");
        set_Value ("XX_VMA_Topic", XX_VMA_Topic);
        
    }
    
    /** Get Topic.
    @return Is the theme that the company wants to promote. */
    public String getXX_VMA_Topic() 
    {
        return (String)get_Value("XX_VMA_Topic");
        
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
    
    
}
