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
/** Generated Model for AD_Tab
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Tab.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Tab extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Tab_ID id
    @param trx transaction
    */
    public X_AD_Tab (Ctx ctx, int AD_Tab_ID, Trx trx)
    {
        super (ctx, AD_Tab_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Tab_ID == 0)
        {
            setAD_Tab_ID (0);
            setAD_Table_ID (0);
            setAD_Window_ID (0);
            setEntityType (null);	// U
            setHasTree (false);
            setIsAdvancedTab (false);	// N
            setIsDisplayed (true);	// Y
            setIsInsertRecord (true);	// Y
            setIsReadOnly (false);
            setIsSingleRow (false);
            setIsSortTab (false);	// N
            setIsTranslationTab (false);
            setName (null);
            setSeqNo (0);	// @SQL=SELECT COALESCE(MAX(SeqNo),0)+10 AS DefaultValue FROM AD_Tab WHERE AD_Window_ID=@AD_Window_ID@
            setTabLevel (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Tab (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518313712789L;
    /** Last Updated Timestamp 2009-03-04 09:39:56.0 */
    public static final long updatedMS = 1236188396000L;
    /** AD_Table_ID=106 */
    public static final int Table_ID=106;
    
    /** TableName=AD_Tab */
    public static final String Table_Name="AD_Tab";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Order Column.
    @param AD_ColumnSortOrder_ID Column determining the order */
    public void setAD_ColumnSortOrder_ID (int AD_ColumnSortOrder_ID)
    {
        if (AD_ColumnSortOrder_ID <= 0) set_Value ("AD_ColumnSortOrder_ID", null);
        else
        set_Value ("AD_ColumnSortOrder_ID", Integer.valueOf(AD_ColumnSortOrder_ID));
        
    }
    
    /** Get Order Column.
    @return Column determining the order */
    public int getAD_ColumnSortOrder_ID() 
    {
        return get_ValueAsInt("AD_ColumnSortOrder_ID");
        
    }
    
    /** Set Included Column.
    @param AD_ColumnSortYesNo_ID Column determining if a Table Column is included in Ordering */
    public void setAD_ColumnSortYesNo_ID (int AD_ColumnSortYesNo_ID)
    {
        if (AD_ColumnSortYesNo_ID <= 0) set_Value ("AD_ColumnSortYesNo_ID", null);
        else
        set_Value ("AD_ColumnSortYesNo_ID", Integer.valueOf(AD_ColumnSortYesNo_ID));
        
    }
    
    /** Get Included Column.
    @return Column determining if a Table Column is included in Ordering */
    public int getAD_ColumnSortYesNo_ID() 
    {
        return get_ValueAsInt("AD_ColumnSortYesNo_ID");
        
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
    
    /** Set Context Area.
    @param AD_CtxArea_ID Business Domain Area Terminology */
    public void setAD_CtxArea_ID (int AD_CtxArea_ID)
    {
        if (AD_CtxArea_ID <= 0) set_Value ("AD_CtxArea_ID", null);
        else
        set_Value ("AD_CtxArea_ID", Integer.valueOf(AD_CtxArea_ID));
        
    }
    
    /** Get Context Area.
    @return Business Domain Area Terminology */
    public int getAD_CtxArea_ID() 
    {
        return get_ValueAsInt("AD_CtxArea_ID");
        
    }
    
    /** Set Image.
    @param AD_Image_ID Image or Icon */
    public void setAD_Image_ID (int AD_Image_ID)
    {
        if (AD_Image_ID <= 0) set_Value ("AD_Image_ID", null);
        else
        set_Value ("AD_Image_ID", Integer.valueOf(AD_Image_ID));
        
    }
    
    /** Get Image.
    @return Image or Icon */
    public int getAD_Image_ID() 
    {
        return get_ValueAsInt("AD_Image_ID");
        
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
    
    /** Set Window.
    @param AD_Window_ID Data entry or display window */
    public void setAD_Window_ID (int AD_Window_ID)
    {
        if (AD_Window_ID < 1) throw new IllegalArgumentException ("AD_Window_ID is mandatory.");
        set_ValueNoCheck ("AD_Window_ID", Integer.valueOf(AD_Window_ID));
        
    }
    
    /** Get Window.
    @return Data entry or display window */
    public int getAD_Window_ID() 
    {
        return get_ValueAsInt("AD_Window_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_Window_ID()));
        
    }
    
    /** Set Commit Warning.
    @param CommitWarning Warning displayed when saving */
    public void setCommitWarning (String CommitWarning)
    {
        set_Value ("CommitWarning", CommitWarning);
        
    }
    
    /** Get Commit Warning.
    @return Warning displayed when saving */
    public String getCommitWarning() 
    {
        return (String)get_Value("CommitWarning");
        
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
    
    /** Set Has Tree.
    @param HasTree Window has Tree Graph */
    public void setHasTree (boolean HasTree)
    {
        set_Value ("HasTree", Boolean.valueOf(HasTree));
        
    }
    
    /** Get Has Tree.
    @return Window has Tree Graph */
    public boolean isHasTree() 
    {
        return get_ValueAsBoolean("HasTree");
        
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
    
    /** Set Import Fields.
    @param ImportFields Create Fields from Table Columns */
    public void setImportFields (String ImportFields)
    {
        set_Value ("ImportFields", ImportFields);
        
    }
    
    /** Get Import Fields.
    @return Create Fields from Table Columns */
    public String getImportFields() 
    {
        return (String)get_Value("ImportFields");
        
    }
    
    /** Set Included Tab.
    @param Included_Tab_ID Included Tab in this Tab (Master Detail) */
    public void setIncluded_Tab_ID (int Included_Tab_ID)
    {
        if (Included_Tab_ID <= 0) set_Value ("Included_Tab_ID", null);
        else
        set_Value ("Included_Tab_ID", Integer.valueOf(Included_Tab_ID));
        
    }
    
    /** Get Included Tab.
    @return Included Tab in this Tab (Master Detail) */
    public int getIncluded_Tab_ID() 
    {
        return get_ValueAsInt("Included_Tab_ID");
        
    }
    
    /** Set Advanced Tab.
    @param IsAdvancedTab This Tab contains advanced Functionality */
    public void setIsAdvancedTab (boolean IsAdvancedTab)
    {
        set_Value ("IsAdvancedTab", Boolean.valueOf(IsAdvancedTab));
        
    }
    
    /** Get Advanced Tab.
    @return This Tab contains advanced Functionality */
    public boolean isAdvancedTab() 
    {
        return get_ValueAsBoolean("IsAdvancedTab");
        
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
    
    /** Set Accounting Tab.
    @param IsInfoTab This Tab contains accounting information */
    public void setIsInfoTab (boolean IsInfoTab)
    {
        set_Value ("IsInfoTab", Boolean.valueOf(IsInfoTab));
        
    }
    
    /** Get Accounting Tab.
    @return This Tab contains accounting information */
    public boolean isInfoTab() 
    {
        return get_ValueAsBoolean("IsInfoTab");
        
    }
    
    /** Set Insert Record.
    @param IsInsertRecord The user can insert a new Record */
    public void setIsInsertRecord (boolean IsInsertRecord)
    {
        set_Value ("IsInsertRecord", Boolean.valueOf(IsInsertRecord));
        
    }
    
    /** Get Insert Record.
    @return The user can insert a new Record */
    public boolean isInsertRecord() 
    {
        return get_ValueAsBoolean("IsInsertRecord");
        
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
    
    /** Set Single Row Layout.
    @param IsSingleRow Default for toggle between Single- and Multi-Row (Grid) Layouts */
    public void setIsSingleRow (boolean IsSingleRow)
    {
        set_Value ("IsSingleRow", Boolean.valueOf(IsSingleRow));
        
    }
    
    /** Get Single Row Layout.
    @return Default for toggle between Single- and Multi-Row (Grid) Layouts */
    public boolean isSingleRow() 
    {
        return get_ValueAsBoolean("IsSingleRow");
        
    }
    
    /** Set Order Tab.
    @param IsSortTab The Tab determines the Order */
    public void setIsSortTab (boolean IsSortTab)
    {
        set_Value ("IsSortTab", Boolean.valueOf(IsSortTab));
        
    }
    
    /** Get Order Tab.
    @return The Tab determines the Order */
    public boolean isSortTab() 
    {
        return get_ValueAsBoolean("IsSortTab");
        
    }
    
    /** Set TranslationTab.
    @param IsTranslationTab This Tab contains translation information */
    public void setIsTranslationTab (boolean IsTranslationTab)
    {
        set_Value ("IsTranslationTab", Boolean.valueOf(IsTranslationTab));
        
    }
    
    /** Get TranslationTab.
    @return This Tab contains translation information */
    public boolean isTranslationTab() 
    {
        return get_ValueAsBoolean("IsTranslationTab");
        
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
    
    /** Set Sql ORDER BY.
    @param OrderByClause Fully qualified ORDER BY clause */
    public void setOrderByClause (String OrderByClause)
    {
        set_Value ("OrderByClause", OrderByClause);
        
    }
    
    /** Get Sql ORDER BY.
    @return Fully qualified ORDER BY clause */
    public String getOrderByClause() 
    {
        return (String)get_Value("OrderByClause");
        
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
    
    /** Set Referenced Tab.
    @param Referenced_Tab_ID Referenced Tab */
    public void setReferenced_Tab_ID (int Referenced_Tab_ID)
    {
        if (Referenced_Tab_ID <= 0) set_Value ("Referenced_Tab_ID", null);
        else
        set_Value ("Referenced_Tab_ID", Integer.valueOf(Referenced_Tab_ID));
        
    }
    
    /** Get Referenced Tab.
    @return Referenced Tab */
    public int getReferenced_Tab_ID() 
    {
        return get_ValueAsInt("Referenced_Tab_ID");
        
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
    
    /** Set Tab Level.
    @param TabLevel Hierarchical Tab Level (0 = top) */
    public void setTabLevel (int TabLevel)
    {
        set_Value ("TabLevel", Integer.valueOf(TabLevel));
        
    }
    
    /** Get Tab Level.
    @return Hierarchical Tab Level (0 = top) */
    public int getTabLevel() 
    {
        return get_ValueAsInt("TabLevel");
        
    }
    
    /** Set Sql WHERE.
    @param WhereClause Fully qualified SQL WHERE clause */
    public void setWhereClause (String WhereClause)
    {
        set_Value ("WhereClause", WhereClause);
        
    }
    
    /** Get Sql WHERE.
    @return Fully qualified SQL WHERE clause */
    public String getWhereClause() 
    {
        return (String)get_Value("WhereClause");
        
    }
    
    
}
