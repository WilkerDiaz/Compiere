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
/** Generated Model for AD_InfoColumn
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_InfoColumn.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_InfoColumn extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_InfoColumn_ID id
    @param trx transaction
    */
    public X_AD_InfoColumn (Ctx ctx, int AD_InfoColumn_ID, Trx trx)
    {
        super (ctx, AD_InfoColumn_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_InfoColumn_ID == 0)
        {
            setAD_InfoColumn_ID (0);
            setAD_InfoWindow_ID (0);
            setAD_Reference_ID (0);
            setEntityType (null);	// U
            setIsDisplayed (true);	// Y
            setIsIdentifier (false);
            setIsKey (false);
            setIsQueryCriteria (false);
            setIsRange (false);
            setName (null);
            setSelectClause (null);
            setSeqNo (0);	// @SQL=SELECT COALESCE(MAX(SeqNo),0)+10 AS DefaultValue FROM AD_InfoColumn WHERE AD_InfoWindow_ID=@AD_InfoWindow_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_InfoColumn (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518313863789L;
    /** Last Updated Timestamp 2009-03-04 09:42:27.0 */
    public static final long updatedMS = 1236188547000L;
    /** AD_Table_ID=897 */
    public static final int Table_ID=897;
    
    /** TableName=AD_InfoColumn */
    public static final String Table_Name="AD_InfoColumn";
    
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
        if (AD_Element_ID <= 0) set_Value ("AD_Element_ID", null);
        else
        set_Value ("AD_Element_ID", Integer.valueOf(AD_Element_ID));
        
    }
    
    /** Get System Element.
    @return System Element enables the central maintenance of column description and help. */
    public int getAD_Element_ID() 
    {
        return get_ValueAsInt("AD_Element_ID");
        
    }
    
    /** Set Info Column.
    @param AD_InfoColumn_ID Info Window Column */
    public void setAD_InfoColumn_ID (int AD_InfoColumn_ID)
    {
        if (AD_InfoColumn_ID < 1) throw new IllegalArgumentException ("AD_InfoColumn_ID is mandatory.");
        set_ValueNoCheck ("AD_InfoColumn_ID", Integer.valueOf(AD_InfoColumn_ID));
        
    }
    
    /** Get Info Column.
    @return Info Window Column */
    public int getAD_InfoColumn_ID() 
    {
        return get_ValueAsInt("AD_InfoColumn_ID");
        
    }
    
    /** Set Info Window.
    @param AD_InfoWindow_ID Info and search/select Window */
    public void setAD_InfoWindow_ID (int AD_InfoWindow_ID)
    {
        if (AD_InfoWindow_ID < 1) throw new IllegalArgumentException ("AD_InfoWindow_ID is mandatory.");
        set_ValueNoCheck ("AD_InfoWindow_ID", Integer.valueOf(AD_InfoWindow_ID));
        
    }
    
    /** Get Info Window.
    @return Info and search/select Window */
    public int getAD_InfoWindow_ID() 
    {
        return get_ValueAsInt("AD_InfoWindow_ID");
        
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
    
    /** Set Query Criteria.
    @param IsQueryCriteria The column is also used as a query criterion */
    public void setIsQueryCriteria (boolean IsQueryCriteria)
    {
        set_Value ("IsQueryCriteria", Boolean.valueOf(IsQueryCriteria));
        
    }
    
    /** Get Query Criteria.
    @return The column is also used as a query criterion */
    public boolean isQueryCriteria() 
    {
        return get_ValueAsBoolean("IsQueryCriteria");
        
    }
    
    /** Set Range.
    @param IsRange The parameter is a range of values */
    public void setIsRange (boolean IsRange)
    {
        set_Value ("IsRange", Boolean.valueOf(IsRange));
        
    }
    
    /** Get Range.
    @return The parameter is a range of values */
    public boolean isRange() 
    {
        return get_ValueAsBoolean("IsRange");
        
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
    
    /** Set Sql SELECT.
    @param SelectClause SQL SELECT clause */
    public void setSelectClause (String SelectClause)
    {
        if (SelectClause == null) throw new IllegalArgumentException ("SelectClause is mandatory.");
        set_Value ("SelectClause", SelectClause);
        
    }
    
    /** Get Sql SELECT.
    @return SQL SELECT clause */
    public String getSelectClause() 
    {
        return (String)get_Value("SelectClause");
        
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
