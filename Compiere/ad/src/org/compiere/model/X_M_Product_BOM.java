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
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for M_Product_BOM
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_Product_BOM.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_Product_BOM extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_Product_BOM_ID id
    @param trx transaction
    */
    public X_M_Product_BOM (Ctx ctx, int M_Product_BOM_ID, Trx trx)
    {
        super (ctx, M_Product_BOM_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_Product_BOM_ID == 0)
        {
            setBOMQty (Env.ZERO);	// 1
            setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM M_Product_BOM WHERE M_Product_ID=@M_Product_ID@
            setM_ProductBOM_ID (0);
            setM_Product_BOM_ID (0);
            setM_Product_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_Product_BOM (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=383 */
    public static final int Table_ID=383;
    
    /** TableName=M_Product_BOM */
    public static final String Table_Name="M_Product_BOM";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Quantity.
    @param BOMQty Bill of Materials Quantity */
    public void setBOMQty (java.math.BigDecimal BOMQty)
    {
        if (BOMQty == null) throw new IllegalArgumentException ("BOMQty is mandatory.");
        set_Value ("BOMQty", BOMQty);
        
    }
    
    /** Get Quantity.
    @return Bill of Materials Quantity */
    public java.math.BigDecimal getBOMQty() 
    {
        return get_ValueAsBigDecimal("BOMQty");
        
    }
    
    /** In alternative Group 1 = 1 */
    public static final String BOMTYPE_InAlternativeGroup1 = X_Ref_M_Product_BOM_Product_TypeX.IN_ALTERNATIVE_GROUP1.getValue();
    /** In alternative Group 2 = 2 */
    public static final String BOMTYPE_InAlternativeGroup2 = X_Ref_M_Product_BOM_Product_TypeX.IN_ALTERNATIVE_GROUP2.getValue();
    /** In alternative Group 3 = 3 */
    public static final String BOMTYPE_InAlternativeGroup3 = X_Ref_M_Product_BOM_Product_TypeX.IN_ALTERNATIVE_GROUP3.getValue();
    /** In alternative Group 4 = 4 */
    public static final String BOMTYPE_InAlternativeGroup4 = X_Ref_M_Product_BOM_Product_TypeX.IN_ALTERNATIVE_GROUP4.getValue();
    /** In alternative Group 5 = 5 */
    public static final String BOMTYPE_InAlternativeGroup5 = X_Ref_M_Product_BOM_Product_TypeX.IN_ALTERNATIVE_GROUP5.getValue();
    /** In alternative Group 6 = 6 */
    public static final String BOMTYPE_InAlternativeGroup6 = X_Ref_M_Product_BOM_Product_TypeX.IN_ALTERNATIVE_GROUP6.getValue();
    /** In alternative Group 7 = 7 */
    public static final String BOMTYPE_InAlternativeGroup7 = X_Ref_M_Product_BOM_Product_TypeX.IN_ALTERNATIVE_GROUP7.getValue();
    /** In alternative Group 8 = 8 */
    public static final String BOMTYPE_InAlternativeGroup8 = X_Ref_M_Product_BOM_Product_TypeX.IN_ALTERNATIVE_GROUP8.getValue();
    /** In alternative Group 9 = 9 */
    public static final String BOMTYPE_InAlternativeGroup9 = X_Ref_M_Product_BOM_Product_TypeX.IN_ALTERNATIVE_GROUP9.getValue();
    /** Optional Part = O */
    public static final String BOMTYPE_OptionalPart = X_Ref_M_Product_BOM_Product_TypeX.OPTIONAL_PART.getValue();
    /** Standard Part = P */
    public static final String BOMTYPE_StandardPart = X_Ref_M_Product_BOM_Product_TypeX.STANDARD_PART.getValue();
    /** Set BOM Type.
    @param BOMType Type of BOM */
    public void setBOMType (String BOMType)
    {
        if (!X_Ref_M_Product_BOM_Product_TypeX.isValid(BOMType))
        throw new IllegalArgumentException ("BOMType Invalid value - " + BOMType + " - Reference_ID=279 - 1 - 2 - 3 - 4 - 5 - 6 - 7 - 8 - 9 - O - P");
        set_Value ("BOMType", BOMType);
        
    }
    
    /** Get BOM Type.
    @return Type of BOM */
    public String getBOMType() 
    {
        return (String)get_Value("BOMType");
        
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
    
    /** Set Line No.
    @param Line Unique line for this document */
    public void setLine (int Line)
    {
        set_Value ("Line", Integer.valueOf(Line));
        
    }
    
    /** Get Line No.
    @return Unique line for this document */
    public int getLine() 
    {
        return get_ValueAsInt("Line");
        
    }
    
    /** Set Component.
    @param M_ProductBOM_ID Bill of Materials Component (Product) */
    public void setM_ProductBOM_ID (int M_ProductBOM_ID)
    {
        if (M_ProductBOM_ID < 1) throw new IllegalArgumentException ("M_ProductBOM_ID is mandatory.");
        set_Value ("M_ProductBOM_ID", Integer.valueOf(M_ProductBOM_ID));
        
    }
    
    /** Get Component.
    @return Bill of Materials Component (Product) */
    public int getM_ProductBOM_ID() 
    {
        return get_ValueAsInt("M_ProductBOM_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_ProductBOM_ID()));
        
    }
    
    /** Set BOM Line.
    @param M_Product_BOM_ID (Old Product BOM Line) */
    public void setM_Product_BOM_ID (int M_Product_BOM_ID)
    {
        if (M_Product_BOM_ID < 1) throw new IllegalArgumentException ("M_Product_BOM_ID is mandatory.");
        set_ValueNoCheck ("M_Product_BOM_ID", Integer.valueOf(M_Product_BOM_ID));
        
    }
    
    /** Get BOM Line.
    @return (Old Product BOM Line) */
    public int getM_Product_BOM_ID() 
    {
        return get_ValueAsInt("M_Product_BOM_ID");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
        set_ValueNoCheck ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    
}
