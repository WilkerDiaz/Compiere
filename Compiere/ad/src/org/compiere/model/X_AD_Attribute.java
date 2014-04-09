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
/** Generated Model for AD_Attribute
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Attribute.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Attribute extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Attribute_ID id
    @param trx transaction
    */
    public X_AD_Attribute (Ctx ctx, int AD_Attribute_ID, Trx trx)
    {
        super (ctx, AD_Attribute_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Attribute_ID == 0)
        {
            setAD_Attribute_ID (0);
            setAD_Reference_ID (0);
            setAD_Table_ID (0);
            setIsEncrypted (false);
            setIsFieldOnly (false);
            setIsHeading (false);
            setIsMandatory (false);
            setIsReadOnly (false);
            setIsSameLine (false);
            setIsUpdateable (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Attribute (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=405 */
    public static final int Table_ID=405;
    
    /** TableName=AD_Attribute */
    public static final String Table_Name="AD_Attribute";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set System Attribute.
    @param AD_Attribute_ID Identifying attribute of a record */
    public void setAD_Attribute_ID (int AD_Attribute_ID)
    {
        if (AD_Attribute_ID < 1) throw new IllegalArgumentException ("AD_Attribute_ID is mandatory.");
        set_ValueNoCheck ("AD_Attribute_ID", Integer.valueOf(AD_Attribute_ID));
        
    }
    
    /** Get System Attribute.
    @return Identifying attribute of a record */
    public int getAD_Attribute_ID() 
    {
        return get_ValueAsInt("AD_Attribute_ID");
        
    }
    
    /** Set Reference.
    @param AD_Reference_ID System Reference and Validation */
    public void setAD_Reference_ID (int AD_Reference_ID)
    {
        if (AD_Reference_ID < 1) throw new IllegalArgumentException ("AD_Reference_ID is mandatory.");
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
    
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID < 1) throw new IllegalArgumentException ("AD_Table_ID is mandatory.");
        set_Value ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
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
    
    /** Set Callout Code.
    @param Callout External Callout Code - Fully qualified class names and method - separated by semicolons */
    public void setCallout (String Callout)
    {
        set_Value ("Callout", Callout);
        
    }
    
    /** Get Callout Code.
    @return External Callout Code - Fully qualified class names and method - separated by semicolons */
    public String getCallout() 
    {
        return (String)get_Value("Callout");
        
    }
    
    /** Set Default Logic.
    @param DefaultValue Default value hierarchy, separated by;
     */
    public void setDefaultValue (String DefaultValue)
    {
        set_Value ("DefaultValue", DefaultValue);
        
    }
    
    /** Get Default Logic.
    @return Default value hierarchy, separated by;
     */
    public String getDefaultValue() 
    {
        return (String)get_Value("DefaultValue");
        
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
    
    /** Set Display Length.
    @param DisplayLength Length of the display in characters */
    public void setDisplayLength (int DisplayLength)
    {
        set_Value ("DisplayLength", Integer.valueOf(DisplayLength));
        
    }
    
    /** Get Display Length.
    @return Length of the display in characters */
    public int getDisplayLength() 
    {
        return get_ValueAsInt("DisplayLength");
        
    }
    
    /** Set Display Logic.
    @param DisplayLogic If the Field is displayed, the result determines if the field is actually displayed */
    public void setDisplayLogic (String DisplayLogic)
    {
        set_Value ("DisplayLogic", DisplayLogic);
        
    }
    
    /** Get Display Logic.
    @return If the Field is displayed, the result determines if the field is actually displayed */
    public String getDisplayLogic() 
    {
        return (String)get_Value("DisplayLogic");
        
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
    
    /** Set Encrypted.
    @param IsEncrypted Display or Storage is encrypted */
    public void setIsEncrypted (boolean IsEncrypted)
    {
        set_Value ("IsEncrypted", Boolean.valueOf(IsEncrypted));
        
    }
    
    /** Get Encrypted.
    @return Display or Storage is encrypted */
    public boolean isEncrypted() 
    {
        return get_ValueAsBoolean("IsEncrypted");
        
    }
    
    /** Set Field Only.
    @param IsFieldOnly Label is not displayed */
    public void setIsFieldOnly (boolean IsFieldOnly)
    {
        set_Value ("IsFieldOnly", Boolean.valueOf(IsFieldOnly));
        
    }
    
    /** Get Field Only.
    @return Label is not displayed */
    public boolean isFieldOnly() 
    {
        return get_ValueAsBoolean("IsFieldOnly");
        
    }
    
    /** Set Heading only.
    @param IsHeading Field without Column - Only label is displayed */
    public void setIsHeading (boolean IsHeading)
    {
        set_Value ("IsHeading", Boolean.valueOf(IsHeading));
        
    }
    
    /** Get Heading only.
    @return Field without Column - Only label is displayed */
    public boolean isHeading() 
    {
        return get_ValueAsBoolean("IsHeading");
        
    }
    
    /** Set Mandatory.
    @param IsMandatory Data is required in this column */
    public void setIsMandatory (boolean IsMandatory)
    {
        set_Value ("IsMandatory", Boolean.valueOf(IsMandatory));
        
    }
    
    /** Get Mandatory.
    @return Data is required in this column */
    public boolean isMandatory() 
    {
        return get_ValueAsBoolean("IsMandatory");
        
    }
    
    /** Set Read Only.
    @param IsReadOnly Field is read only */
    public void setIsReadOnly (boolean IsReadOnly)
    {
        set_Value ("IsReadOnly", Boolean.valueOf(IsReadOnly));
        
    }
    
    /** Get Read Only.
    @return Field is read only */
    public boolean isReadOnly() 
    {
        return get_ValueAsBoolean("IsReadOnly");
        
    }
    
    /** Set Same Line.
    @param IsSameLine Displayed on same line as previous field */
    public void setIsSameLine (boolean IsSameLine)
    {
        set_Value ("IsSameLine", Boolean.valueOf(IsSameLine));
        
    }
    
    /** Get Same Line.
    @return Displayed on same line as previous field */
    public boolean isSameLine() 
    {
        return get_ValueAsBoolean("IsSameLine");
        
    }
    
    /** Set Updateable.
    @param IsUpdateable Determines, if the field can be updated */
    public void setIsUpdateable (boolean IsUpdateable)
    {
        set_Value ("IsUpdateable", Boolean.valueOf(IsUpdateable));
        
    }
    
    /** Get Updateable.
    @return Determines, if the field can be updated */
    public boolean isUpdateable() 
    {
        return get_ValueAsBoolean("IsUpdateable");
        
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
    
    /** Set Sequence.
    @param SeqNo Method of ordering elements;
     lowest number comes first */
    public void setSeqNo (int SeqNo)
    {
        set_Value ("SeqNo", Integer.valueOf(SeqNo));
        
    }
    
    /** Get Sequence.
    @return Method of ordering elements;
     lowest number comes first */
    public int getSeqNo() 
    {
        return get_ValueAsInt("SeqNo");
        
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
    
    /** Set Max. Value.
    @param ValueMax Maximum Value for a field */
    public void setValueMax (String ValueMax)
    {
        set_Value ("ValueMax", ValueMax);
        
    }
    
    /** Get Max. Value.
    @return Maximum Value for a field */
    public String getValueMax() 
    {
        return (String)get_Value("ValueMax");
        
    }
    
    /** Set Min. Value.
    @param ValueMin Minimum Value for a field */
    public void setValueMin (String ValueMin)
    {
        set_Value ("ValueMin", ValueMin);
        
    }
    
    /** Get Min. Value.
    @return Minimum Value for a field */
    public String getValueMin() 
    {
        return (String)get_Value("ValueMin");
        
    }
    
    
}
