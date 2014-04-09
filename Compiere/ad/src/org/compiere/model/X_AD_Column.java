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
/** Generated Model for AD_Column
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: X_AD_Column.java 9169 2010-08-05 09:24:48Z sdandapat $ */
public class X_AD_Column extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Column_ID id
    @param trx transaction
    */
    public X_AD_Column (Ctx ctx, int AD_Column_ID, Trx trx)
    {
        super (ctx, AD_Column_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Column_ID == 0)
        {
            setAD_Column_ID (0);
            setAD_Element_ID (0);
            setAD_Reference_ID (0);
            setAD_Table_ID (0);
            setColumnName (null);
            setEntityType (null);	// U
            setIsAlwaysUpdateable (false);	// N
            setIsCopy (true);	// Y
            setIsEncrypted (null);	// N
            setIsIdentifier (false);
            setIsKey (false);
            setIsMandatory (false);
            setIsMandatoryUI (false);
            setIsParent (false);
            setIsSelectionColumn (false);
            setIsTranslated (false);
            setIsUpdateable (true);	// Y
            setName (null);
            setVersion (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Column (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27563067037789L;
    /** Last Updated Timestamp 2010-08-04 22:38:41.0 */
    public static final long updatedMS = 1280941721000L;
    /** AD_Table_ID=101 */
    public static final int Table_ID=101;
    
    /** TableName=AD_Column */
    public static final String Table_Name="AD_Column";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Column.
    @param AD_Column_ID Column in the table */
    public void setAD_Column_ID (int AD_Column_ID)
    {
        if (AD_Column_ID < 1) throw new IllegalArgumentException ("AD_Column_ID is mandatory.");
        set_ValueNoCheck ("AD_Column_ID", Integer.valueOf(AD_Column_ID));
        
    }
    
    /** Get Column.
    @return Column in the table */
    public int getAD_Column_ID() 
    {
        return get_ValueAsInt("AD_Column_ID");
        
    }
    
    /** Set System Element.
    @param AD_Element_ID System Element enables the central maintenance of column description and help. */
    public void setAD_Element_ID (int AD_Element_ID)
    {
        if (AD_Element_ID < 1) throw new IllegalArgumentException ("AD_Element_ID is mandatory.");
        set_Value ("AD_Element_ID", Integer.valueOf(AD_Element_ID));
        
    }
    
    /** Get System Element.
    @return System Element enables the central maintenance of column description and help. */
    public int getAD_Element_ID() 
    {
        return get_ValueAsInt("AD_Element_ID");
        
    }
    
    /** Set Process.
    @param AD_Process_ID Process or Report */
    public void setAD_Process_ID (int AD_Process_ID)
    {
        if (AD_Process_ID <= 0) set_Value ("AD_Process_ID", null);
        else
        set_Value ("AD_Process_ID", Integer.valueOf(AD_Process_ID));
        
    }
    
    /** Get Process.
    @return Process or Report */
    public int getAD_Process_ID() 
    {
        return get_ValueAsInt("AD_Process_ID");
        
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
        set_ValueNoCheck ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
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
    
    /** Set Column SQL.
    @param ColumnSQL Virtual Column (r/o) */
    public void setColumnSQL (String ColumnSQL)
    {
        set_Value ("ColumnSQL", ColumnSQL);
        
    }
    
    /** Get Column SQL.
    @return Virtual Column (r/o) */
    public String getColumnSQL() 
    {
        return (String)get_Value("ColumnSQL");
        
    }
    
    /** Set Constraint Name.
    @param ConstraintName Constraint Name */
    public void setConstraintName (String ConstraintName)
    {
        throw new IllegalArgumentException ("ConstraintName is virtual column");
        
    }
    
    /** Get Constraint Name.
    @return Constraint Name */
    public String getConstraintName() 
    {
        return (String)get_Value("ConstraintName");
        
    }
    
    /** Cascade = C */
    public static final String CONSTRAINTTYPE_Cascade = X_Ref_AD_Column_ConstraintType.CASCADE.getValue();
    /** Null = N */
    public static final String CONSTRAINTTYPE_Null = X_Ref_AD_Column_ConstraintType.NULL.getValue();
    /** Restrict = R */
    public static final String CONSTRAINTTYPE_Restrict = X_Ref_AD_Column_ConstraintType.RESTRICT.getValue();
    /** Do NOT Create = X */
    public static final String CONSTRAINTTYPE_DoNOTCreate = X_Ref_AD_Column_ConstraintType.DO_NOT_CREATE.getValue();
    /** Cascade Trigger = c */
    public static final String CONSTRAINTTYPE_CascadeTrigger = X_Ref_AD_Column_ConstraintType.CASCADE_TRIGGER.getValue();
    /** Null Trigger = n */
    public static final String CONSTRAINTTYPE_NullTrigger = X_Ref_AD_Column_ConstraintType.NULL_TRIGGER.getValue();
    /** Restrict Trigger = r */
    public static final String CONSTRAINTTYPE_RestrictTrigger = X_Ref_AD_Column_ConstraintType.RESTRICT_TRIGGER.getValue();
    /** Set Constraint Type.
    @param ConstraintType Constraint Type */
    public void setConstraintType (String ConstraintType)
    {
        if (!X_Ref_AD_Column_ConstraintType.isValid(ConstraintType))
        throw new IllegalArgumentException ("ConstraintType Invalid value - " + ConstraintType + " - Reference_ID=411 - C - N - R - X - c - n - r");
        set_Value ("ConstraintType", ConstraintType);
        
    }
    
    /** Get Constraint Type.
    @return Constraint Type */
    public String getConstraintType() 
    {
        return (String)get_Value("ConstraintType");
        
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
    
    /** Set Always Updatable.
    @param IsAlwaysUpdateable The column is always updatable, even if the record is not active or processed */
    public void setIsAlwaysUpdateable (boolean IsAlwaysUpdateable)
    {
        set_Value ("IsAlwaysUpdateable", Boolean.valueOf(IsAlwaysUpdateable));
        
    }
    
    /** Get Always Updatable.
    @return The column is always updatable, even if the record is not active or processed */
    public boolean isAlwaysUpdateable() 
    {
        return get_ValueAsBoolean("IsAlwaysUpdateable");
        
    }
    
    /** Set Callout.
    @param IsCallout This column has implemented a Callout */
    public void setIsCallout (boolean IsCallout)
    {
        set_Value ("IsCallout", Boolean.valueOf(IsCallout));
        
    }
    
    /** Get Callout.
    @return This column has implemented a Callout */
    public boolean isCallout() 
    {
        return get_ValueAsBoolean("IsCallout");
        
    }
    
    /** Set Copy.
    @param IsCopy Copy contents of this field using the Copy Record function. */
    public void setIsCopy (boolean IsCopy)
    {
        set_Value ("IsCopy", Boolean.valueOf(IsCopy));
        
    }
    
    /** Get Copy.
    @return Copy contents of this field using the Copy Record function. */
    public boolean isCopy() 
    {
        return get_ValueAsBoolean("IsCopy");
        
    }
    
    /** Not Encrypted = N */
    public static final String ISENCRYPTED_NotEncrypted = X_Ref_AD_Column_Encrypted.NOT_ENCRYPTED.getValue();
    /** Encrypted = Y */
    public static final String ISENCRYPTED_Encrypted = X_Ref_AD_Column_Encrypted.ENCRYPTED.getValue();
    /** Set Encrypted.
    @param IsEncrypted Display or Storage is encrypted */
    public void setIsEncrypted (String IsEncrypted)
    {
        if (IsEncrypted == null) throw new IllegalArgumentException ("IsEncrypted is mandatory");
        if (!X_Ref_AD_Column_Encrypted.isValid(IsEncrypted))
        throw new IllegalArgumentException ("IsEncrypted Invalid value - " + IsEncrypted + " - Reference_ID=354 - N - Y");
        set_Value ("IsEncrypted", IsEncrypted);
        
    }
    
    /** Get Encrypted.
    @return Display or Storage is encrypted */
    public String getIsEncrypted() 
    {
        return (String)get_Value("IsEncrypted");
        
    }
    
    /** Set Identifier.
    @param IsIdentifier This column is part of the record identifier */
    public void setIsIdentifier (boolean IsIdentifier)
    {
        set_Value ("IsIdentifier", Boolean.valueOf(IsIdentifier));
        
    }
    
    /** Get Identifier.
    @return This column is part of the record identifier */
    public boolean isIdentifier() 
    {
        return get_ValueAsBoolean("IsIdentifier");
        
    }
    
    /** Set Key column.
    @param IsKey This column is the key in this table */
    public void setIsKey (boolean IsKey)
    {
        set_Value ("IsKey", Boolean.valueOf(IsKey));
        
    }
    
    /** Get Key column.
    @return This column is the key in this table */
    public boolean isKey() 
    {
        return get_ValueAsBoolean("IsKey");
        
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
    
    /** Set Mandatory UI.
    @param IsMandatoryUI Data entry is required for data entry in the field */
    public void setIsMandatoryUI (boolean IsMandatoryUI)
    {
        set_Value ("IsMandatoryUI", Boolean.valueOf(IsMandatoryUI));
        
    }
    
    /** Get Mandatory UI.
    @return Data entry is required for data entry in the field */
    public boolean isMandatoryUI() 
    {
        return get_ValueAsBoolean("IsMandatoryUI");
        
    }
    
    /** Set Parent link column.
    @param IsParent This column is a link to the parent table (e.g. header from lines) - incl. Association key columns */
    public void setIsParent (boolean IsParent)
    {
        set_Value ("IsParent", Boolean.valueOf(IsParent));
        
    }
    
    /** Get Parent link column.
    @return This column is a link to the parent table (e.g. header from lines) - incl. Association key columns */
    public boolean isParent() 
    {
        return get_ValueAsBoolean("IsParent");
        
    }
    
    /** Set Recursive FK.
    @param IsRecursiveFK Recursive Foreign key */
    public void setIsRecursiveFK (boolean IsRecursiveFK)
    {
        set_Value ("IsRecursiveFK", Boolean.valueOf(IsRecursiveFK));
        
    }
    
    /** Get Recursive FK.
    @return Recursive Foreign key */
    public boolean isRecursiveFK() 
    {
        return get_ValueAsBoolean("IsRecursiveFK");
        
    }
    
    /** Set Selection Column.
    @param IsSelectionColumn Is this column used for finding rows in windows? */
    public void setIsSelectionColumn (boolean IsSelectionColumn)
    {
        set_Value ("IsSelectionColumn", Boolean.valueOf(IsSelectionColumn));
        
    }
    
    /** Get Selection Column.
    @return Is this column used for finding rows in windows? */
    public boolean isSelectionColumn() 
    {
        return get_ValueAsBoolean("IsSelectionColumn");
        
    }
    
    /** Set Summary Column.
    @param IsSummaryColumn Summary Info Column */
    public void setIsSummaryColumn (boolean IsSummaryColumn)
    {
        set_Value ("IsSummaryColumn", Boolean.valueOf(IsSummaryColumn));
        
    }
    
    /** Get Summary Column.
    @return Summary Info Column */
    public boolean isSummaryColumn() 
    {
        return get_ValueAsBoolean("IsSummaryColumn");
        
    }
    
    /** Set Synchronize Database.
    @param IsSyncDatabase Change database table definition when changing dictionary definition */
    public void setIsSyncDatabase (String IsSyncDatabase)
    {
        set_Value ("IsSyncDatabase", IsSyncDatabase);
        
    }
    
    /** Get Synchronize Database.
    @return Change database table definition when changing dictionary definition */
    public String getIsSyncDatabase() 
    {
        return (String)get_Value("IsSyncDatabase");
        
    }
    
    /** Set Translated.
    @param IsTranslated This column is translated */
    public void setIsTranslated (boolean IsTranslated)
    {
        set_Value ("IsTranslated", Boolean.valueOf(IsTranslated));
        
    }
    
    /** Get Translated.
    @return This column is translated */
    public boolean isTranslated() 
    {
        return get_ValueAsBoolean("IsTranslated");
        
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
    
    /** Set Mandatory Logic.
    @param MandatoryLogic Logic determining when a field is required to be entered */
    public void setMandatoryLogic (String MandatoryLogic)
    {
        set_Value ("MandatoryLogic", MandatoryLogic);
        
    }
    
    /** Get Mandatory Logic.
    @return Logic determining when a field is required to be entered */
    public String getMandatoryLogic() 
    {
        return (String)get_Value("MandatoryLogic");
        
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
    
    /** Set Read Only Logic.
    @param ReadOnlyLogic Logic to determine if field is read only (applies only when field is read-write) */
    public void setReadOnlyLogic (String ReadOnlyLogic)
    {
        set_Value ("ReadOnlyLogic", ReadOnlyLogic);
        
    }
    
    /** Get Read Only Logic.
    @return Logic to determine if field is read only (applies only when field is read-write) */
    public String getReadOnlyLogic() 
    {
        return (String)get_Value("ReadOnlyLogic");
        
    }
    
    /** Set Selection Sequence.
    @param SelectionSeqNo Sequence in Selection */
    public void setSelectionSeqNo (int SelectionSeqNo)
    {
        set_Value ("SelectionSeqNo", Integer.valueOf(SelectionSeqNo));
        
    }
    
    /** Get Selection Sequence.
    @return Sequence in Selection */
    public int getSelectionSeqNo() 
    {
        return get_ValueAsInt("SelectionSeqNo");
        
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
    
    /** Set Summary Sequence.
    @param SummarySeqNo Sequence in Summary */
    public void setSummarySeqNo (int SummarySeqNo)
    {
        set_Value ("SummarySeqNo", Integer.valueOf(SummarySeqNo));
        
    }
    
    /** Get Summary Sequence.
    @return Sequence in Summary */
    public int getSummarySeqNo() 
    {
        return get_ValueAsInt("SummarySeqNo");
        
    }
    
    /** Set TableUID.
    @param TableUID Unique Table ID */
    public void setTableUID (int TableUID)
    {
        set_Value ("TableUID", Integer.valueOf(TableUID));
        
    }
    
    /** Get TableUID.
    @return Unique Table ID */
    public int getTableUID() 
    {
        return get_ValueAsInt("TableUID");
        
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
    
    /** Set Version.
    @param Version Version of the table definition */
    public void setVersion (java.math.BigDecimal Version)
    {
        if (Version == null) throw new IllegalArgumentException ("Version is mandatory.");
        set_Value ("Version", Version);
        
    }
    
    /** Get Version.
    @return Version of the table definition */
    public java.math.BigDecimal getVersion() 
    {
        return get_ValueAsBigDecimal("Version");
        
    }
    
    
}
