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
/** Generated Model for C_DocBaseType
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_DocBaseType.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_DocBaseType extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_DocBaseType_ID id
    @param trx transaction
    */
    public X_C_DocBaseType (Ctx ctx, int C_DocBaseType_ID, Trx trx)
    {
        super (ctx, C_DocBaseType_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_DocBaseType_ID == 0)
        {
            setC_DocBaseType_ID (0);
            setDocBaseType (null);	// XXX
            setEntityType (null);	// U
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_DocBaseType (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518313956789L;
    /** Last Updated Timestamp 2009-03-04 09:44:00.0 */
    public static final long updatedMS = 1236188640000L;
    /** AD_Table_ID=988 */
    public static final int Table_ID=988;
    
    /** TableName=C_DocBaseType */
    public static final String Table_Name="C_DocBaseType";
    
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
        if (AD_Table_ID <= 0) set_Value ("AD_Table_ID", null);
        else
        set_Value ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
    }
    
    /** Set Accounting Classname.
    @param AccountingClassname Classname for Accounting class */
    public void setAccountingClassname (String AccountingClassname)
    {
        set_Value ("AccountingClassname", AccountingClassname);
        
    }
    
    /** Get Accounting Classname.
    @return Classname for Accounting class */
    public String getAccountingClassname() 
    {
        return (String)get_Value("AccountingClassname");
        
    }
    
    /** Set Document Base Type.
    @param C_DocBaseType_ID Accounting Document base type */
    public void setC_DocBaseType_ID (int C_DocBaseType_ID)
    {
        if (C_DocBaseType_ID < 1) throw new IllegalArgumentException ("C_DocBaseType_ID is mandatory.");
        set_ValueNoCheck ("C_DocBaseType_ID", Integer.valueOf(C_DocBaseType_ID));
        
    }
    
    /** Get Document Base Type.
    @return Accounting Document base type */
    public int getC_DocBaseType_ID() 
    {
        return get_ValueAsInt("C_DocBaseType_ID");
        
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
    
    /** Set Document BaseType.
    @param DocBaseType Logical type of document */
    public void setDocBaseType (String DocBaseType)
    {
        if (DocBaseType == null) throw new IllegalArgumentException ("DocBaseType is mandatory.");
        set_Value ("DocBaseType", DocBaseType);
        
    }
    
    /** Get Document BaseType.
    @return Logical type of document */
    public String getDocBaseType() 
    {
        return (String)get_Value("DocBaseType");
        
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
    
    
}
