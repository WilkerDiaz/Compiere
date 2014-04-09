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
/** Generated Model for R_Source
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_R_Source.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_R_Source extends PO
{
    /** Standard Constructor
    @param ctx context
    @param R_Source_ID id
    @param trx transaction
    */
    public X_R_Source (Ctx ctx, int R_Source_ID, Trx trx)
    {
        super (ctx, R_Source_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (R_Source_ID == 0)
        {
            setName (null);
            setR_Source_ID (0);
            setSourceCreateType (null);	// L
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_R_Source (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=914 */
    public static final int Table_ID=914;
    
    /** TableName=R_Source */
    public static final String Table_Name="R_Source";
    
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
    
    /** Set Source.
    @param R_Source_ID Source for the Lead or Request */
    public void setR_Source_ID (int R_Source_ID)
    {
        if (R_Source_ID < 1) throw new IllegalArgumentException ("R_Source_ID is mandatory.");
        set_ValueNoCheck ("R_Source_ID", Integer.valueOf(R_Source_ID));
        
    }
    
    /** Get Source.
    @return Source for the Lead or Request */
    public int getR_Source_ID() 
    {
        return get_ValueAsInt("R_Source_ID");
        
    }
    
    /** Both = B */
    public static final String SOURCECREATETYPE_Both = X_Ref_R_Source_CreateType.BOTH.getValue();
    /** Lead = L */
    public static final String SOURCECREATETYPE_Lead = X_Ref_R_Source_CreateType.LEAD.getValue();
    /** Request = R */
    public static final String SOURCECREATETYPE_Request = X_Ref_R_Source_CreateType.REQUEST.getValue();
    /** Set Create Type.
    @param SourceCreateType Automatically create Lead or Source */
    public void setSourceCreateType (String SourceCreateType)
    {
        if (SourceCreateType == null) throw new IllegalArgumentException ("SourceCreateType is mandatory");
        if (!X_Ref_R_Source_CreateType.isValid(SourceCreateType))
        throw new IllegalArgumentException ("SourceCreateType Invalid value - " + SourceCreateType + " - Reference_ID=423 - B - L - R");
        set_Value ("SourceCreateType", SourceCreateType);
        
    }
    
    /** Get Create Type.
    @return Automatically create Lead or Source */
    public String getSourceCreateType() 
    {
        return (String)get_Value("SourceCreateType");
        
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
    
    
}
