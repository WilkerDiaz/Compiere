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
/** Generated Model for AD_Element
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Element.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Element extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Element_ID id
    @param trx transaction
    */
    public X_AD_Element (Ctx ctx, int AD_Element_ID, Trx trx)
    {
        super (ctx, AD_Element_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Element_ID == 0)
        {
            setAD_Element_ID (0);
            setColumnName (null);
            setEntityType (null);	// U
            setName (null);
            setPrintName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Element (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518313499789L;
    /** Last Updated Timestamp 2009-03-04 09:36:23.0 */
    public static final long updatedMS = 1236188183000L;
    /** AD_Table_ID=276 */
    public static final int Table_ID=276;
    
    /** TableName=AD_Element */
    public static final String Table_Name="AD_Element";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set System Element.
    @param AD_Element_ID System Element enables the central maintenance of column description and help. */
    public void setAD_Element_ID (int AD_Element_ID)
    {
        if (AD_Element_ID < 1) throw new IllegalArgumentException ("AD_Element_ID is mandatory.");
        set_ValueNoCheck ("AD_Element_ID", Integer.valueOf(AD_Element_ID));
        
    }
    
    /** Get System Element.
    @return System Element enables the central maintenance of column description and help. */
    public int getAD_Element_ID() 
    {
        return get_ValueAsInt("AD_Element_ID");
        
    }
    
    /** Set Reference.
    @param AD_Reference_ID System Reference and Validation */
    public void setAD_Reference_ID (int AD_Reference_ID)
    {
        if (AD_Reference_ID <= 0) set_Value ("AD_Reference_ID", null);
        else
        set_Value ("AD_Reference_ID", Integer.valueOf(AD_Reference_ID));
        
    }
    
    /** Get Reference.
    @return System Reference and Validation */
    public int getAD_Reference_ID() 
    {
        return get_ValueAsInt("AD_Reference_ID");
        
    }
    
    /** Set Reference Key.
    @param AD_Reference_Value_ID Required to specify, if data type is Table or List */
    public void setAD_Reference_Value_ID (int AD_Reference_Value_ID)
    {
        if (AD_Reference_Value_ID <= 0) set_Value ("AD_Reference_Value_ID", null);
        else
        set_Value ("AD_Reference_Value_ID", Integer.valueOf(AD_Reference_Value_ID));
        
    }
    
    /** Get Reference Key.
    @return Required to specify, if data type is Table or List */
    public int getAD_Reference_Value_ID() 
    {
        return get_ValueAsInt("AD_Reference_Value_ID");
        
    }
    
    /** Set Dynamic Validation.
    @param AD_Val_Rule_ID Dynamic Validation Rule */
    public void setAD_Val_Rule_ID (int AD_Val_Rule_ID)
    {
        if (AD_Val_Rule_ID <= 0) set_Value ("AD_Val_Rule_ID", null);
        else
        set_Value ("AD_Val_Rule_ID", Integer.valueOf(AD_Val_Rule_ID));
        
    }
    
    /** Get Dynamic Validation.
    @return Dynamic Validation Rule */
    public int getAD_Val_Rule_ID() 
    {
        return get_ValueAsInt("AD_Val_Rule_ID");
        
    }
    
    /** Set DB Column Name.
    @param ColumnName Name of the column in the database */
    public void setColumnName (String ColumnName)
    {
        if (ColumnName == null) throw new IllegalArgumentException ("ColumnName is mandatory.");
        set_Value ("ColumnName", ColumnName);
        
    }
    
    /** Get DB Column Name.
    @return Name of the column in the database */
    public String getColumnName() 
    {
        return (String)get_Value("ColumnName");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getColumnName());
        
    }
    
    /** Binary LOB = B */
    public static final String DBDATATYPE_BinaryLOB = X_Ref_AD_Element_Data_Type.BINARY_LOB.getValue();
    /** Character Fixed = C */
    public static final String DBDATATYPE_CharacterFixed = X_Ref_AD_Element_Data_Type.CHARACTER_FIXED.getValue();
    /** Decimal = D */
    public static final String DBDATATYPE_Decimal = X_Ref_AD_Element_Data_Type.DECIMAL.getValue();
    /** Integer = I */
    public static final String DBDATATYPE_Integer = X_Ref_AD_Element_Data_Type.INTEGER.getValue();
    /** Character LOB = L */
    public static final String DBDATATYPE_CharacterLOB = X_Ref_AD_Element_Data_Type.CHARACTER_LOB.getValue();
    /** Number = N */
    public static final String DBDATATYPE_Number = X_Ref_AD_Element_Data_Type.NUMBER.getValue();
    /** Timestamp = T */
    public static final String DBDATATYPE_Timestamp = X_Ref_AD_Element_Data_Type.TIMESTAMP.getValue();
    /** Character Variable = V */
    public static final String DBDATATYPE_CharacterVariable = X_Ref_AD_Element_Data_Type.CHARACTER_VARIABLE.getValue();
    /** Set Data Type.
    @param DBDataType Database Data Type */
    public void setDBDataType (String DBDataType)
    {
        if (!X_Ref_AD_Element_Data_Type.isValid(DBDataType))
        throw new IllegalArgumentException ("DBDataType Invalid value - " + DBDataType + " - Reference_ID=422 - B - C - D - I - L - N - T - V");
        set_Value ("DBDataType", DBDataType);
        
    }
    
    /** Get Data Type.
    @return Database Data Type */
    public String getDBDataType() 
    {
        return (String)get_Value("DBDataType");
        
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
    
    /** Set Length.
    @param FieldLength Length of the column in the database */
    public void setFieldLength (int FieldLength)
    {
        set_Value ("FieldLength", Integer.valueOf(FieldLength));
        
    }
    
    /** Get Length.
    @return Length of the column in the database */
    public int getFieldLength() 
    {
        return get_ValueAsInt("FieldLength");
        
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
    
    /** Set PO Description.
    @param PO_Description Description in PO Screens */
    public void setPO_Description (String PO_Description)
    {
        set_Value ("PO_Description", PO_Description);
        
    }
    
    /** Get PO Description.
    @return Description in PO Screens */
    public String getPO_Description() 
    {
        return (String)get_Value("PO_Description");
        
    }
    
    /** Set PO Help.
    @param PO_Help Help for PO Screens */
    public void setPO_Help (String PO_Help)
    {
        set_Value ("PO_Help", PO_Help);
        
    }
    
    /** Get PO Help.
    @return Help for PO Screens */
    public String getPO_Help() 
    {
        return (String)get_Value("PO_Help");
        
    }
    
    /** Set PO Name.
    @param PO_Name Name on PO Screens */
    public void setPO_Name (String PO_Name)
    {
        set_Value ("PO_Name", PO_Name);
        
    }
    
    /** Get PO Name.
    @return Name on PO Screens */
    public String getPO_Name() 
    {
        return (String)get_Value("PO_Name");
        
    }
    
    /** Set PO Print name.
    @param PO_PrintName Print name on PO Screens/Reports */
    public void setPO_PrintName (String PO_PrintName)
    {
        set_Value ("PO_PrintName", PO_PrintName);
        
    }
    
    /** Get PO Print name.
    @return Print name on PO Screens/Reports */
    public String getPO_PrintName() 
    {
        return (String)get_Value("PO_PrintName");
        
    }
    
    /** Set Print Text.
    @param PrintName The label text to be printed on a document or correspondence. */
    public void setPrintName (String PrintName)
    {
        if (PrintName == null) throw new IllegalArgumentException ("PrintName is mandatory.");
        set_Value ("PrintName", PrintName);
        
    }
    
    /** Get Print Text.
    @return The label text to be printed on a document or correspondence. */
    public String getPrintName() 
    {
        return (String)get_Value("PrintName");
        
    }
    
    
}
