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
/** Generated Model for AD_TableIndex
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_TableIndex.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_TableIndex extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_TableIndex_ID id
    @param trx transaction
    */
    public X_AD_TableIndex (Ctx ctx, int AD_TableIndex_ID, Trx trx)
    {
        super (ctx, AD_TableIndex_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_TableIndex_ID == 0)
        {
            setAD_TableIndex_ID (0);
            setAD_Table_ID (0);
            setEntityType (null);
            setIsCreateConstraint (false);	// N
            setIsUnique (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_TableIndex (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518313606789L;
    /** Last Updated Timestamp 2009-03-04 09:38:10.0 */
    public static final long updatedMS = 1236188290000L;
    /** AD_Table_ID=909 */
    public static final int Table_ID=909;
    
    /** TableName=AD_TableIndex */
    public static final String Table_Name="AD_TableIndex";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Table Index.
    @param AD_TableIndex_ID Table Index */
    public void setAD_TableIndex_ID (int AD_TableIndex_ID)
    {
        if (AD_TableIndex_ID < 1) throw new IllegalArgumentException ("AD_TableIndex_ID is mandatory.");
        set_ValueNoCheck ("AD_TableIndex_ID", Integer.valueOf(AD_TableIndex_ID));
        
    }
    
    /** Get Table Index.
    @return Table Index */
    public int getAD_TableIndex_ID() 
    {
        return get_ValueAsInt("AD_TableIndex_ID");
        
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
    
    /** Set Create Constraint.
    @param IsCreateConstraint Create Database Constraint */
    public void setIsCreateConstraint (boolean IsCreateConstraint)
    {
        set_Value ("IsCreateConstraint", Boolean.valueOf(IsCreateConstraint));
        
    }
    
    /** Get Create Constraint.
    @return Create Database Constraint */
    public boolean isCreateConstraint() 
    {
        return get_ValueAsBoolean("IsCreateConstraint");
        
    }
    
    /** Set Unique.
    @param IsUnique Unique */
    public void setIsUnique (boolean IsUnique)
    {
        set_Value ("IsUnique", Boolean.valueOf(IsUnique));
        
    }
    
    /** Get Unique.
    @return Unique */
    public boolean isUnique() 
    {
        return get_ValueAsBoolean("IsUnique");
        
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
    
    
}
