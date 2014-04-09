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
/** Generated Model for AD_UserDef_Tab
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_UserDef_Tab.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_UserDef_Tab extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_UserDef_Tab_ID id
    @param trx transaction
    */
    public X_AD_UserDef_Tab (Ctx ctx, int AD_UserDef_Tab_ID, Trx trx)
    {
        super (ctx, AD_UserDef_Tab_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_UserDef_Tab_ID == 0)
        {
            setAD_Tab_ID (0);
            setAD_UserDef_Tab_ID (0);
            setAD_UserDef_Win_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_UserDef_Tab (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=466 */
    public static final int Table_ID=466;
    
    /** TableName=AD_UserDef_Tab */
    public static final String Table_Name="AD_UserDef_Tab";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Tab.
    @param AD_Tab_ID Tab within a Window */
    public void setAD_Tab_ID (int AD_Tab_ID)
    {
        if (AD_Tab_ID < 1) throw new IllegalArgumentException ("AD_Tab_ID is mandatory.");
        set_Value ("AD_Tab_ID", Integer.valueOf(AD_Tab_ID));
        
    }
    
    /** Get Tab.
    @return Tab within a Window */
    public int getAD_Tab_ID() 
    {
        return get_ValueAsInt("AD_Tab_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_Tab_ID()));
        
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
    
    /** Set User defined Window.
    @param AD_UserDef_Win_ID User defined Window */
    public void setAD_UserDef_Win_ID (int AD_UserDef_Win_ID)
    {
        if (AD_UserDef_Win_ID < 1) throw new IllegalArgumentException ("AD_UserDef_Win_ID is mandatory.");
        set_ValueNoCheck ("AD_UserDef_Win_ID", Integer.valueOf(AD_UserDef_Win_ID));
        
    }
    
    /** Get User defined Window.
    @return User defined Window */
    public int getAD_UserDef_Win_ID() 
    {
        return get_ValueAsInt("AD_UserDef_Win_ID");
        
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
    public static final String ISINSERTRECORD_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISINSERTRECORD_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Insert Record.
    @param IsInsertRecord The user can insert a new Record */
    public void setIsInsertRecord (String IsInsertRecord)
    {
        if (!X_Ref__YesNo.isValid(IsInsertRecord))
        throw new IllegalArgumentException ("IsInsertRecord Invalid value - " + IsInsertRecord + " - Reference_ID=319 - N - Y");
        set_Value ("IsInsertRecord", IsInsertRecord);
        
    }
    
    /** Get Insert Record.
    @return The user can insert a new Record */
    public String getIsInsertRecord() 
    {
        return (String)get_Value("IsInsertRecord");
        
    }
    
    /** No = N */
    public static final String ISMULTIROWONLY_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISMULTIROWONLY_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Multi Row Only.
    @param IsMultiRowOnly This applies to Multi-Row view only */
    public void setIsMultiRowOnly (String IsMultiRowOnly)
    {
        if (!X_Ref__YesNo.isValid(IsMultiRowOnly))
        throw new IllegalArgumentException ("IsMultiRowOnly Invalid value - " + IsMultiRowOnly + " - Reference_ID=319 - N - Y");
        set_Value ("IsMultiRowOnly", IsMultiRowOnly);
        
    }
    
    /** Get Multi Row Only.
    @return This applies to Multi-Row view only */
    public String getIsMultiRowOnly() 
    {
        return (String)get_Value("IsMultiRowOnly");
        
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
    public static final String ISSINGLEROW_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISSINGLEROW_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Single Row Layout.
    @param IsSingleRow Default for toggle between Single- and Multi-Row (Grid) Layouts */
    public void setIsSingleRow (String IsSingleRow)
    {
        if (!X_Ref__YesNo.isValid(IsSingleRow))
        throw new IllegalArgumentException ("IsSingleRow Invalid value - " + IsSingleRow + " - Reference_ID=319 - N - Y");
        set_Value ("IsSingleRow", IsSingleRow);
        
    }
    
    /** Get Single Row Layout.
    @return Default for toggle between Single- and Multi-Row (Grid) Layouts */
    public String getIsSingleRow() 
    {
        return (String)get_Value("IsSingleRow");
        
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
    
    
}
