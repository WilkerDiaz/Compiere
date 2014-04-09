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
/** Generated Model for M_BOM
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_BOM.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_BOM extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_BOM_ID id
    @param trx transaction
    */
    public X_M_BOM (Ctx ctx, int M_BOM_ID, Trx trx)
    {
        super (ctx, M_BOM_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_BOM_ID == 0)
        {
            setBOMType (null);	// A
            setBOMUse (null);	// A
            setM_BOM_ID (0);
            setM_Product_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_BOM (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27496802968789L;
    /** Last Updated Timestamp 2008-06-28 11:27:32.0 */
    public static final long updatedMS = 1214677652000L;
    /** AD_Table_ID=798 */
    public static final int Table_ID=798;
    
    /** TableName=M_BOM */
    public static final String Table_Name="M_BOM";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Current Active = A */
    public static final String BOMTYPE_CurrentActive = X_Ref_M_BOM_Type.CURRENT_ACTIVE.getValue();
    /** Future = F */
    public static final String BOMTYPE_Future = X_Ref_M_BOM_Type.FUTURE.getValue();
    /** Maintenance = M */
    public static final String BOMTYPE_Maintenance = X_Ref_M_BOM_Type.MAINTENANCE.getValue();
    /** Make-To-Order = O */
    public static final String BOMTYPE_Make_To_Order = X_Ref_M_BOM_Type.MAKE__TO__ORDER.getValue();
    /** Previous = P */
    public static final String BOMTYPE_Previous = X_Ref_M_BOM_Type.PREVIOUS.getValue();
    /** Repair = R */
    public static final String BOMTYPE_Repair = X_Ref_M_BOM_Type.REPAIR.getValue();
    /** Spare = S */
    public static final String BOMTYPE_Spare = X_Ref_M_BOM_Type.SPARE.getValue();
    /** Set BOM Type.
    @param BOMType Type of BOM */
    public void setBOMType (String BOMType)
    {
        if (BOMType == null) throw new IllegalArgumentException ("BOMType is mandatory");
        if (!X_Ref_M_BOM_Type.isValid(BOMType))
        throw new IllegalArgumentException ("BOMType Invalid value - " + BOMType + " - Reference_ID=347 - A - F - M - O - P - R - S");
        set_Value ("BOMType", BOMType);
        
    }
    
    /** Get BOM Type.
    @return Type of BOM */
    public String getBOMType() 
    {
        return (String)get_Value("BOMType");
        
    }
    
    /** Master = A */
    public static final String BOMUSE_Master = X_Ref_M_BOM_Use.MASTER.getValue();
    /** Engineering = E */
    public static final String BOMUSE_Engineering = X_Ref_M_BOM_Use.ENGINEERING.getValue();
    /** Manufacturing = M */
    public static final String BOMUSE_Manufacturing = X_Ref_M_BOM_Use.MANUFACTURING.getValue();
    /** Maintenance = N */
    public static final String BOMUSE_Maintenance = X_Ref_M_BOM_Use.MAINTENANCE.getValue();
    /** Planning = P */
    public static final String BOMUSE_Planning = X_Ref_M_BOM_Use.PLANNING.getValue();
    /** Repair = R */
    public static final String BOMUSE_Repair = X_Ref_M_BOM_Use.REPAIR.getValue();
    /** Set BOM Use.
    @param BOMUse The use of the Bill of Material */
    public void setBOMUse (String BOMUse)
    {
        if (BOMUse == null) throw new IllegalArgumentException ("BOMUse is mandatory");
        if (!X_Ref_M_BOM_Use.isValid(BOMUse))
        throw new IllegalArgumentException ("BOMUse Invalid value - " + BOMUse + " - Reference_ID=348 - A - E - M - N - P - R");
        set_Value ("BOMUse", BOMUse);
        
    }
    
    /** Get BOM Use.
    @return The use of the Bill of Material */
    public String getBOMUse() 
    {
        return (String)get_Value("BOMUse");
        
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
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
    }
    
    /** Set BOM.
    @param M_BOM_ID Bill of Materials */
    public void setM_BOM_ID (int M_BOM_ID)
    {
        if (M_BOM_ID < 1) throw new IllegalArgumentException ("M_BOM_ID is mandatory.");
        set_ValueNoCheck ("M_BOM_ID", Integer.valueOf(M_BOM_ID));
        
    }
    
    /** Get BOM.
    @return Bill of Materials */
    public int getM_BOM_ID() 
    {
        return get_ValueAsInt("M_BOM_ID");
        
    }
    
    /** Set Change Notice.
    @param M_ChangeNotice_ID Bill of Materials (Engineering) Change Notice (Version) */
    public void setM_ChangeNotice_ID (int M_ChangeNotice_ID)
    {
        if (M_ChangeNotice_ID <= 0) set_Value ("M_ChangeNotice_ID", null);
        else
        set_Value ("M_ChangeNotice_ID", Integer.valueOf(M_ChangeNotice_ID));
        
    }
    
    /** Get Change Notice.
    @return Bill of Materials (Engineering) Change Notice (Version) */
    public int getM_ChangeNotice_ID() 
    {
        return get_ValueAsInt("M_ChangeNotice_ID");
        
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
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    
}
