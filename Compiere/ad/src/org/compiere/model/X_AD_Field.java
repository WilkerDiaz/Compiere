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
/** Generated Model for AD_Field
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: X_AD_Field.java 9169 2010-08-05 09:24:48Z sdandapat $ */
public class X_AD_Field extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Field_ID id
    @param trx transaction
    */
    public X_AD_Field (Ctx ctx, int AD_Field_ID, Trx trx)
    {
        super (ctx, AD_Field_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Field_ID == 0)
        {
            setAD_Field_ID (0);
            setAD_Tab_ID (0);
            setEntityType (null);	// U
            setIsCentrallyMaintained (true);	// Y
            setIsDisplayed (true);	// Y
            setIsEncrypted (false);
            setIsFieldOnly (false);
            setIsHeading (false);
            setIsReadOnly (false);
            setIsSameLine (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Field (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27563067147789L;
    /** Last Updated Timestamp 2010-08-04 22:40:31.0 */
    public static final long updatedMS = 1280941831000L;
    /** AD_Table_ID=107 */
    public static final int Table_ID=107;
    
    /** TableName=AD_Field */
    public static final String Table_Name="AD_Field";
    
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
        if (AD_Column_ID <= 0) set_Value ("AD_Column_ID", null);
        else
        set_Value ("AD_Column_ID", Integer.valueOf(AD_Column_ID));
        
    }
    
    /** Get Column.
    @return Column in the table */
    public int getAD_Column_ID() 
    {
        return get_ValueAsInt("AD_Column_ID");
        
    }
    
    /** Set Field Group.
    @param AD_FieldGroup_ID Logical grouping of fields */
    public void setAD_FieldGroup_ID (int AD_FieldGroup_ID)
    {
        if (AD_FieldGroup_ID <= 0) set_Value ("AD_FieldGroup_ID", null);
        else
        set_Value ("AD_FieldGroup_ID", Integer.valueOf(AD_FieldGroup_ID));
        
    }
    
    /** Get Field Group.
    @return Logical grouping of fields */
    public int getAD_FieldGroup_ID() 
    {
        return get_ValueAsInt("AD_FieldGroup_ID");
        
    }
    
    /** Set Field.
    @param AD_Field_ID Field on a tab in a window */
    public void setAD_Field_ID (int AD_Field_ID)
    {
        if (AD_Field_ID < 1) throw new IllegalArgumentException ("AD_Field_ID is mandatory.");
        set_ValueNoCheck ("AD_Field_ID", Integer.valueOf(AD_Field_ID));
        
    }
    
    /** Get Field.
    @return Field on a tab in a window */
    public int getAD_Field_ID() 
    {
        return get_ValueAsInt("AD_Field_ID");
        
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
    
    /** Set Tab.
    @param AD_Tab_ID Tab within a Window */
    public void setAD_Tab_ID (int AD_Tab_ID)
    {
        if (AD_Tab_ID < 1) throw new IllegalArgumentException ("AD_Tab_ID is mandatory.");
        set_ValueNoCheck ("AD_Tab_ID", Integer.valueOf(AD_Tab_ID));
        
    }
    
    /** Get Tab.
    @return Tab within a Window */
    public int getAD_Tab_ID() 
    {
        return get_ValueAsInt("AD_Tab_ID");
        
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
    
    /** Set Centrally maintained.
    @param IsCentrallyMaintained Information maintained in System Element table */
    public void setIsCentrallyMaintained (boolean IsCentrallyMaintained)
    {
        set_Value ("IsCentrallyMaintained", Boolean.valueOf(IsCentrallyMaintained));
        
    }
    
    /** Get Centrally maintained.
    @return Information maintained in System Element table */
    public boolean isCentrallyMaintained() 
    {
        return get_ValueAsBoolean("IsCentrallyMaintained");
        
    }
    
    /** No = N */
    public static final String ISCOPY_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISCOPY_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Copy.
    @param IsCopy Copy contents of this field using the Copy Record function. */
    public void setIsCopy (String IsCopy)
    {
        if (!X_Ref__YesNo.isValid(IsCopy))
        throw new IllegalArgumentException ("IsCopy Invalid value - " + IsCopy + " - Reference_ID=319 - N - Y");
        set_Value ("IsCopy", IsCopy);
        
    }
    
    /** Get Copy.
    @return Copy contents of this field using the Copy Record function. */
    public String getIsCopy() 
    {
        return (String)get_Value("IsCopy");
        
    }
    
    /** Set Default Focus.
    @param IsDefaultFocus Field received the default focus */
    public void setIsDefaultFocus (boolean IsDefaultFocus)
    {
        set_Value ("IsDefaultFocus", Boolean.valueOf(IsDefaultFocus));
        
    }
    
    /** Get Default Focus.
    @return Field received the default focus */
    public boolean isDefaultFocus() 
    {
        return get_ValueAsBoolean("IsDefaultFocus");
        
    }
    
    /** Set Displayed.
    @param IsDisplayed Determines, if this field is displayed */
    public void setIsDisplayed (boolean IsDisplayed)
    {
        set_Value ("IsDisplayed", Boolean.valueOf(IsDisplayed));
        
    }
    
    /** Get Displayed.
    @return Determines, if this field is displayed */
    public boolean isDisplayed() 
    {
        return get_ValueAsBoolean("IsDisplayed");
        
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
    
    /** No = N */
    public static final String ISMANDATORYUI_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISMANDATORYUI_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Mandatory UI.
    @param IsMandatoryUI Data entry is required for data entry in the field */
    public void setIsMandatoryUI (String IsMandatoryUI)
    {
        if (!X_Ref__YesNo.isValid(IsMandatoryUI))
        throw new IllegalArgumentException ("IsMandatoryUI Invalid value - " + IsMandatoryUI + " - Reference_ID=319 - N - Y");
        set_Value ("IsMandatoryUI", IsMandatoryUI);
        
    }
    
    /** Get Mandatory UI.
    @return Data entry is required for data entry in the field */
    public String getIsMandatoryUI() 
    {
        return (String)get_Value("IsMandatoryUI");
        
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
    
    /** Set Multi-Row Sequence.
    @param MRSeqNo Method of ordering fields in Multi-Row (Grid) View;
     lowest number comes first */
    public void setMRSeqNo (int MRSeqNo)
    {
        set_Value ("MRSeqNo", Integer.valueOf(MRSeqNo));
        
    }
    
    /** Get Multi-Row Sequence.
    @return Method of ordering fields in Multi-Row (Grid) View;
     lowest number comes first */
    public int getMRSeqNo() 
    {
        return get_ValueAsInt("MRSeqNo");
        
    }
    
    /** Set Max Height.
    @param MaxHeight Maximum Height in 1/72 if an inch - 0 = no restriction */
    public void setMaxHeight (int MaxHeight)
    {
        set_Value ("MaxHeight", Integer.valueOf(MaxHeight));
        
    }
    
    /** Get Max Height.
    @return Maximum Height in 1/72 if an inch - 0 = no restriction */
    public int getMaxHeight() 
    {
        return get_ValueAsInt("MaxHeight");
        
    }
    
    /** Set Max Width.
    @param MaxWidth Maximum Width in 1/72 if an inch - 0 = no restriction */
    public void setMaxWidth (int MaxWidth)
    {
        set_Value ("MaxWidth", Integer.valueOf(MaxWidth));
        
    }
    
    /** Get Max Width.
    @return Maximum Width in 1/72 if an inch - 0 = no restriction */
    public int getMaxWidth() 
    {
        return get_ValueAsInt("MaxWidth");
        
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
    
    /** Obscure Digits but last 4 = 904 */
    public static final String OBSCURETYPE_ObscureDigitsButLast4 = X_Ref_AD_Field_ObscureType.OBSCURE_DIGITS_BUT_LAST4.getValue();
    /** Obscure Digits but first/last 4 = 944 */
    public static final String OBSCURETYPE_ObscureDigitsButFirstLast4 = X_Ref_AD_Field_ObscureType.OBSCURE_DIGITS_BUT_FIRST_LAST4.getValue();
    /** Obscure AlphaNumeric but last 4 = A04 */
    public static final String OBSCURETYPE_ObscureAlphaNumericButLast4 = X_Ref_AD_Field_ObscureType.OBSCURE_ALPHA_NUMERIC_BUT_LAST4.getValue();
    /** Obscure AlphaNumeric but first/last 4 = A44 */
    public static final String OBSCURETYPE_ObscureAlphaNumericButFirstLast4 = X_Ref_AD_Field_ObscureType.OBSCURE_ALPHA_NUMERIC_BUT_FIRST_LAST4.getValue();
    /** Set Obscure.
    @param ObscureType Type of obscuring the data (limiting the display) */
    public void setObscureType (String ObscureType)
    {
        if (!X_Ref_AD_Field_ObscureType.isValid(ObscureType))
        throw new IllegalArgumentException ("ObscureType Invalid value - " + ObscureType + " - Reference_ID=291 - 904 - 944 - A04 - A44");
        set_Value ("ObscureType", ObscureType);
        
    }
    
    /** Get Obscure.
    @return Type of obscuring the data (limiting the display) */
    public String getObscureType() 
    {
        return (String)get_Value("ObscureType");
        
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
    
    /** Set Record Sort No.
    @param SortNo Determines in what order the records are displayed */
    public void setSortNo (java.math.BigDecimal SortNo)
    {
        set_Value ("SortNo", SortNo);
        
    }
    
    /** Get Record Sort No.
    @return Determines in what order the records are displayed */
    public java.math.BigDecimal getSortNo() 
    {
        return get_ValueAsBigDecimal("SortNo");
        
    }
    
    
}
