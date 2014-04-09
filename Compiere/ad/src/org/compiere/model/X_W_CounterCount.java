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
/** Generated Model for W_CounterCount
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_W_CounterCount.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_W_CounterCount extends PO
{
    /** Standard Constructor
    @param ctx context
    @param W_CounterCount_ID id
    @param trx transaction
    */
    public X_W_CounterCount (Ctx ctx, int W_CounterCount_ID, Trx trx)
    {
        super (ctx, W_CounterCount_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (W_CounterCount_ID == 0)
        {
            setName (null);
            setPageURL (null);
            setW_CounterCount_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_W_CounterCount (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=552 */
    public static final int Table_ID=552;
    
    /** TableName=W_CounterCount */
    public static final String Table_Name="W_CounterCount";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
        else
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Counter.
    @param Counter Count Value */
    public void setCounter (int Counter)
    {
        throw new IllegalArgumentException ("Counter is virtual column");
        
    }
    
    /** Get Counter.
    @return Count Value */
    public int getCounter() 
    {
        return get_ValueAsInt("Counter");
        
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
    
    /** Set Page URL.
    @param PageURL Page URL */
    public void setPageURL (String PageURL)
    {
        if (PageURL == null) throw new IllegalArgumentException ("PageURL is mandatory.");
        set_Value ("PageURL", PageURL);
        
    }
    
    /** Get Page URL.
    @return Page URL */
    public String getPageURL() 
    {
        return (String)get_Value("PageURL");
        
    }
    
    /** Set Counter Count.
    @param W_CounterCount_ID Web Counter Count Management */
    public void setW_CounterCount_ID (int W_CounterCount_ID)
    {
        if (W_CounterCount_ID < 1) throw new IllegalArgumentException ("W_CounterCount_ID is mandatory.");
        set_ValueNoCheck ("W_CounterCount_ID", Integer.valueOf(W_CounterCount_ID));
        
    }
    
    /** Get Counter Count.
    @return Web Counter Count Management */
    public int getW_CounterCount_ID() 
    {
        return get_ValueAsInt("W_CounterCount_ID");
        
    }
    
    
}
