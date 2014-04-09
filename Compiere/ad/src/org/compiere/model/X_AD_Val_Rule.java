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
/** Generated Model for AD_Val_Rule
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Val_Rule.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Val_Rule extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Val_Rule_ID id
    @param trx transaction
    */
    public X_AD_Val_Rule (Ctx ctx, int AD_Val_Rule_ID, Trx trx)
    {
        super (ctx, AD_Val_Rule_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Val_Rule_ID == 0)
        {
            setAD_Val_Rule_ID (0);
            setEntityType (null);	// U
            setName (null);
            setType (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Val_Rule (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518313760789L;
    /** Last Updated Timestamp 2009-03-04 09:40:44.0 */
    public static final long updatedMS = 1236188444000L;
    /** AD_Table_ID=108 */
    public static final int Table_ID=108;
    
    /** TableName=AD_Val_Rule */
    public static final String Table_Name="AD_Val_Rule";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Dynamic Validation.
    @param AD_Val_Rule_ID Dynamic Validation Rule */
    public void setAD_Val_Rule_ID (int AD_Val_Rule_ID)
    {
        if (AD_Val_Rule_ID < 1) throw new IllegalArgumentException ("AD_Val_Rule_ID is mandatory.");
        set_ValueNoCheck ("AD_Val_Rule_ID", Integer.valueOf(AD_Val_Rule_ID));
        
    }
    
    /** Get Dynamic Validation.
    @return Dynamic Validation Rule */
    public int getAD_Val_Rule_ID() 
    {
        return get_ValueAsInt("AD_Val_Rule_ID");
        
    }
    
    /** Set Code.
    @param Code Code to execute or to validate */
    public void setCode (String Code)
    {
        set_Value ("Code", Code);
        
    }
    
    /** Get Code.
    @return Code to execute or to validate */
    public String getCode() 
    {
        return (String)get_Value("Code");
        
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
    
    /** Set Entity Type.
    @param EntityType Dictionary Entity Type;
     Determines ownership and synchronization */
    public void setEntityType (String EntityType)
    {
        set_Value ("EntityType", EntityType);
        
    }
    
    /** Get Entity Type.
    @return Dictionary Entity Type;
     Determines ownership and synchronization */
    public String getEntityType() 
    {
        return (String)get_Value("EntityType");
        
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
    
    /** Java Script = E */
    public static final String TYPE_JavaScript = X_Ref_AD_Validation_Rule_Types.JAVA_SCRIPT.getValue();
    /** Java Language = J */
    public static final String TYPE_JavaLanguage = X_Ref_AD_Validation_Rule_Types.JAVA_LANGUAGE.getValue();
    /** SQL = S */
    public static final String TYPE_SQL = X_Ref_AD_Validation_Rule_Types.SQL.getValue();
    /** Set Code Type.
    @param Type Type of Code/Validation (SQL, Java Script, Java Language) */
    public void setType (String Type)
    {
        if (Type == null) throw new IllegalArgumentException ("Type is mandatory");
        if (!X_Ref_AD_Validation_Rule_Types.isValid(Type))
        throw new IllegalArgumentException ("Type Invalid value - " + Type + " - Reference_ID=101 - E - J - S");
        set_Value ("Type", Type);
        
    }
    
    /** Get Code Type.
    @return Type of Code/Validation (SQL, Java Script, Java Language) */
    public String getType() 
    {
        return (String)get_Value("Type");
        
    }
    
    
}
