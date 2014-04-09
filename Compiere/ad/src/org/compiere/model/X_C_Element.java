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
/** Generated Model for C_Element
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Element.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Element extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Element_ID id
    @param trx transaction
    */
    public X_C_Element (Ctx ctx, int C_Element_ID, Trx trx)
    {
        super (ctx, C_Element_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Element_ID == 0)
        {
            setAD_Tree_ID (0);
            setC_Element_ID (0);
            setElementType (null);	// A
            setIsBalancing (false);
            setIsNaturalAccount (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Element (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=142 */
    public static final int Table_ID=142;
    
    /** TableName=C_Element */
    public static final String Table_Name="C_Element";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Tree.
    @param AD_Tree_ID Identifies a Tree */
    public void setAD_Tree_ID (int AD_Tree_ID)
    {
        if (AD_Tree_ID < 1) throw new IllegalArgumentException ("AD_Tree_ID is mandatory.");
        set_ValueNoCheck ("AD_Tree_ID", Integer.valueOf(AD_Tree_ID));
        
    }
    
    /** Get Tree.
    @return Identifies a Tree */
    public int getAD_Tree_ID() 
    {
        return get_ValueAsInt("AD_Tree_ID");
        
    }
    
    /** Set Element.
    @param C_Element_ID Accounting Element */
    public void setC_Element_ID (int C_Element_ID)
    {
        if (C_Element_ID < 1) throw new IllegalArgumentException ("C_Element_ID is mandatory.");
        set_ValueNoCheck ("C_Element_ID", Integer.valueOf(C_Element_ID));
        
    }
    
    /** Get Element.
    @return Accounting Element */
    public int getC_Element_ID() 
    {
        return get_ValueAsInt("C_Element_ID");
        
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
    
    /** Account = A */
    public static final String ELEMENTTYPE_Account = X_Ref_C_Element_Type.ACCOUNT.getValue();
    /** User defined = U */
    public static final String ELEMENTTYPE_UserDefined = X_Ref_C_Element_Type.USER_DEFINED.getValue();
    /** Set Type.
    @param ElementType Element Type (account or user defined) */
    public void setElementType (String ElementType)
    {
        if (ElementType == null) throw new IllegalArgumentException ("ElementType is mandatory");
        if (!X_Ref_C_Element_Type.isValid(ElementType))
        throw new IllegalArgumentException ("ElementType Invalid value - " + ElementType + " - Reference_ID=116 - A - U");
        set_ValueNoCheck ("ElementType", ElementType);
        
    }
    
    /** Get Type.
    @return Element Type (account or user defined) */
    public String getElementType() 
    {
        return (String)get_Value("ElementType");
        
    }
    
    /** Set Balancing.
    @param IsBalancing All transactions within an element value must balance (e.g. legal entities) */
    public void setIsBalancing (boolean IsBalancing)
    {
        set_Value ("IsBalancing", Boolean.valueOf(IsBalancing));
        
    }
    
    /** Get Balancing.
    @return All transactions within an element value must balance (e.g. legal entities) */
    public boolean isBalancing() 
    {
        return get_ValueAsBoolean("IsBalancing");
        
    }
    
    /** Set Natural Account.
    @param IsNaturalAccount The primary natural account */
    public void setIsNaturalAccount (boolean IsNaturalAccount)
    {
        set_Value ("IsNaturalAccount", Boolean.valueOf(IsNaturalAccount));
        
    }
    
    /** Get Natural Account.
    @return The primary natural account */
    public boolean isNaturalAccount() 
    {
        return get_ValueAsBoolean("IsNaturalAccount");
        
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
    
    /** Set Value Format.
    @param VFormat Format of the value;
     Can contain fixed format elements, Variables: "_lLoOaAcCa09" */
    public void setVFormat (String VFormat)
    {
        set_Value ("VFormat", VFormat);
        
    }
    
    /** Get Value Format.
    @return Format of the value;
     Can contain fixed format elements, Variables: "_lLoOaAcCa09" */
    public String getVFormat() 
    {
        return (String)get_Value("VFormat");
        
    }
    
    
}
