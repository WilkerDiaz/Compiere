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
/** Generated Model for AD_UserDef_Field
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_UserDef_Field.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_UserDef_Field extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_UserDef_Field_ID id
    @param trx transaction
    */
    public X_AD_UserDef_Field (Ctx ctx, int AD_UserDef_Field_ID, Trx trx)
    {
        super (ctx, AD_UserDef_Field_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_UserDef_Field_ID == 0)
        {
            setAD_Field_ID (0);
            setAD_UserDef_Field_ID (0);
            setAD_UserDef_Tab_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_UserDef_Field (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=464 */
    public static final int Table_ID=464;
    
    /** TableName=AD_UserDef_Field */
    public static final String Table_Name="AD_UserDef_Field";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Field.
    @param AD_Field_ID Field on a tab in a window */
    public void setAD_Field_ID (int AD_Field_ID)
    {
        if (AD_Field_ID < 1) throw new IllegalArgumentException ("AD_Field_ID is mandatory.");
        set_Value ("AD_Field_ID", Integer.valueOf(AD_Field_ID));
        
    }
    
    /** Get Field.
    @return Field on a tab in a window */
    public int getAD_Field_ID() 
    {
        return get_ValueAsInt("AD_Field_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_Field_ID()));
        
    }
    
    /** Set User defined Field.
    @param AD_UserDef_Field_ID User defined Field */
    public void setAD_UserDef_Field_ID (int AD_UserDef_Field_ID)
    {
        if (AD_UserDef_Field_ID < 1) throw new IllegalArgumentException ("AD_UserDef_Field_ID is mandatory.");
        set_ValueNoCheck ("AD_UserDef_Field_ID", Integer.valueOf(AD_UserDef_Field_ID));
        
    }
    
    /** Get User defined Field.
    @return User defined Field */
    public int getAD_UserDef_Field_ID() 
    {
        return get_ValueAsInt("AD_UserDef_Field_ID");
        
    }
    
    /** Set User defined Tab.
    @param AD_UserDef_Tab_ID User defined Tab */
    public void setAD_UserDef_Tab_ID (int AD_UserDef_Tab_ID)
    {
        if (AD_UserDef_Tab_ID < 1) throw new IllegalArgumentException ("AD_UserDef_Tab_ID is mandatory.");
        set_ValueNoCheck ("AD_UserDef_Tab_ID", Integer.valueOf(AD_UserDef_Tab_ID));
        
    }
    
    /** Get User defined Tab.
    @return User defined Tab */
    public int getAD_UserDef_Tab_ID() 
    {
        return get_ValueAsInt("AD_UserDef_Tab_ID");
        
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
    
    /** No = N */
    public static final String ISDISPLAYED_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISDISPLAYED_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Displayed.
    @param IsDisplayed Determines, if this field is displayed */
    public void setIsDisplayed (String IsDisplayed)
    {
        if (!X_Ref__YesNo.isValid(IsDisplayed))
        throw new IllegalArgumentException ("IsDisplayed Invalid value - " + IsDisplayed + " - Reference_ID=319 - N - Y");
        set_Value ("IsDisplayed", IsDisplayed);
        
    }
    
    /** Get Displayed.
    @return Determines, if this field is displayed */
    public String getIsDisplayed() 
    {
        return (String)get_Value("IsDisplayed");
        
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
    
    /** No = N */
    public static final String ISREADONLY_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISREADONLY_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Read Only.
    @param IsReadOnly Field is read only */
    public void setIsReadOnly (String IsReadOnly)
    {
        if (!X_Ref__YesNo.isValid(IsReadOnly))
        throw new IllegalArgumentException ("IsReadOnly Invalid value - " + IsReadOnly + " - Reference_ID=319 - N - Y");
        set_Value ("IsReadOnly", IsReadOnly);
        
    }
    
    /** Get Read Only.
    @return Field is read only */
    public String getIsReadOnly() 
    {
        return (String)get_Value("IsReadOnly");
        
    }
    
    /** No = N */
    public static final String ISSAMELINE_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISSAMELINE_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Same Line.
    @param IsSameLine Displayed on same line as previous field */
    public void setIsSameLine (String IsSameLine)
    {
        if (!X_Ref__YesNo.isValid(IsSameLine))
        throw new IllegalArgumentException ("IsSameLine Invalid value - " + IsSameLine + " - Reference_ID=319 - N - Y");
        set_Value ("IsSameLine", IsSameLine);
        
    }
    
    /** Get Same Line.
    @return Displayed on same line as previous field */
    public String getIsSameLine() 
    {
        return (String)get_Value("IsSameLine");
        
    }
    
    /** No = N */
    public static final String ISSELECTIONCOLUMN_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISSELECTIONCOLUMN_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Selection Column.
    @param IsSelectionColumn Is this column used for finding rows in windows? */
    public void setIsSelectionColumn (String IsSelectionColumn)
    {
        if (!X_Ref__YesNo.isValid(IsSelectionColumn))
        throw new IllegalArgumentException ("IsSelectionColumn Invalid value - " + IsSelectionColumn + " - Reference_ID=319 - N - Y");
        set_Value ("IsSelectionColumn", IsSelectionColumn);
        
    }
    
    /** Get Selection Column.
    @return Is this column used for finding rows in windows? */
    public String getIsSelectionColumn() 
    {
        return (String)get_Value("IsSelectionColumn");
        
    }
    
    /** No = N */
    public static final String ISSUMMARYCOLUMN_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISSUMMARYCOLUMN_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Summary Column.
    @param IsSummaryColumn Summary Info Column */
    public void setIsSummaryColumn (String IsSummaryColumn)
    {
        if (!X_Ref__YesNo.isValid(IsSummaryColumn))
        throw new IllegalArgumentException ("IsSummaryColumn Invalid value - " + IsSummaryColumn + " - Reference_ID=319 - N - Y");
        set_Value ("IsSummaryColumn", IsSummaryColumn);
        
    }
    
    /** Get Summary Column.
    @return Summary Info Column */
    public String getIsSummaryColumn() 
    {
        return (String)get_Value("IsSummaryColumn");
        
    }
    
    /** No = N */
    public static final String ISUPDATEABLE_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISUPDATEABLE_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Updateable.
    @param IsUpdateable Determines, if the field can be updated */
    public void setIsUpdateable (String IsUpdateable)
    {
        if (!X_Ref__YesNo.isValid(IsUpdateable))
        throw new IllegalArgumentException ("IsUpdateable Invalid value - " + IsUpdateable + " - Reference_ID=319 - N - Y");
        set_Value ("IsUpdateable", IsUpdateable);
        
    }
    
    /** Get Updateable.
    @return Determines, if the field can be updated */
    public String getIsUpdateable() 
    {
        return (String)get_Value("IsUpdateable");
        
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
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
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
    
    /** Set Record Sort No.
    @param SortNo Determines in what order the records are displayed */
    public void setSortNo (int SortNo)
    {
        set_Value ("SortNo", Integer.valueOf(SortNo));
        
    }
    
    /** Get Record Sort No.
    @return Determines in what order the records are displayed */
    public int getSortNo() 
    {
        return get_ValueAsInt("SortNo");
        
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
