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
/** Generated Model for AD_ViewComponent
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_ViewComponent.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_ViewComponent extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_ViewComponent_ID id
    @param trx transaction
    */
    public X_AD_ViewComponent (Ctx ctx, int AD_ViewComponent_ID, Trx trx)
    {
        super (ctx, AD_ViewComponent_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_ViewComponent_ID == 0)
        {
            setAD_ViewComponent_ID (0);
            setEntityType (null);	// U
            setFromClause (null);
            setName (null);
            setReferenced_Table_ID (0);
            setSeqNo (0);	// @SQL=SELECT COALESCE(MAX(SeqNo),0)+10 AS DefaultValue FROM AD_ViewComponent WHERE AD_Table_ID=@AD_Table_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_ViewComponent (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518313896789L;
    /** Last Updated Timestamp 2009-03-04 09:43:00.0 */
    public static final long updatedMS = 1236188580000L;
    /** AD_Table_ID=934 */
    public static final int Table_ID=934;
    
    /** TableName=AD_ViewComponent */
    public static final String Table_Name="AD_ViewComponent";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID <= 0) set_ValueNoCheck ("AD_Table_ID", null);
        else
        set_ValueNoCheck ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_Table_ID()));
        
    }
    
    /** Set View Component.
    @param AD_ViewComponent_ID Component (Select statement) of the view */
    public void setAD_ViewComponent_ID (int AD_ViewComponent_ID)
    {
        if (AD_ViewComponent_ID < 1) throw new IllegalArgumentException ("AD_ViewComponent_ID is mandatory.");
        set_ValueNoCheck ("AD_ViewComponent_ID", Integer.valueOf(AD_ViewComponent_ID));
        
    }
    
    /** Get View Component.
    @return Component (Select statement) of the view */
    public int getAD_ViewComponent_ID() 
    {
        return get_ValueAsInt("AD_ViewComponent_ID");
        
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
    
    /** Set SQL FROM.
    @param FromClause SQL FROM clause */
    public void setFromClause (String FromClause)
    {
        if (FromClause == null) throw new IllegalArgumentException ("FromClause is mandatory.");
        set_Value ("FromClause", FromClause);
        
    }
    
    /** Get SQL FROM.
    @return SQL FROM clause */
    public String getFromClause() 
    {
        return (String)get_Value("FromClause");
        
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
    
    /** Set Other SQL Clause.
    @param OtherClause Other SQL Clause */
    public void setOtherClause (String OtherClause)
    {
        set_Value ("OtherClause", OtherClause);
        
    }
    
    /** Get Other SQL Clause.
    @return Other SQL Clause */
    public String getOtherClause() 
    {
        return (String)get_Value("OtherClause");
        
    }
    
    /** Set Referenced Table.
    @param Referenced_Table_ID Referenced Table */
    public void setReferenced_Table_ID (int Referenced_Table_ID)
    {
        if (Referenced_Table_ID < 1) throw new IllegalArgumentException ("Referenced_Table_ID is mandatory.");
        set_Value ("Referenced_Table_ID", Integer.valueOf(Referenced_Table_ID));
        
    }
    
    /** Get Referenced Table.
    @return Referenced Table */
    public int getReferenced_Table_ID() 
    {
        return get_ValueAsInt("Referenced_Table_ID");
        
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
