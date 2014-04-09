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
/** Generated Model for M_SerNoCtl
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_SerNoCtl.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_SerNoCtl extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_SerNoCtl_ID id
    @param trx transaction
    */
    public X_M_SerNoCtl (Ctx ctx, int M_SerNoCtl_ID, Trx trx)
    {
        super (ctx, M_SerNoCtl_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_SerNoCtl_ID == 0)
        {
            setCurrentNext (0);	// 100
            setIncrementNo (0);	// 1
            setM_SerNoCtl_ID (0);
            setName (null);
            setStartNo (0);	// 100
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_SerNoCtl (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=555 */
    public static final int Table_ID=555;
    
    /** TableName=M_SerNoCtl */
    public static final String Table_Name="M_SerNoCtl";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Current Next.
    @param CurrentNext The next number to be used */
    public void setCurrentNext (int CurrentNext)
    {
        set_Value ("CurrentNext", Integer.valueOf(CurrentNext));
        
    }
    
    /** Get Current Next.
    @return The next number to be used */
    public int getCurrentNext() 
    {
        return get_ValueAsInt("CurrentNext");
        
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
    
    /** Set Increment.
    @param IncrementNo The number to increment the last document number by */
    public void setIncrementNo (int IncrementNo)
    {
        set_Value ("IncrementNo", Integer.valueOf(IncrementNo));
        
    }
    
    /** Get Increment.
    @return The number to increment the last document number by */
    public int getIncrementNo() 
    {
        return get_ValueAsInt("IncrementNo");
        
    }
    
    /** Set Serial No Control.
    @param M_SerNoCtl_ID Product Serial Number Control */
    public void setM_SerNoCtl_ID (int M_SerNoCtl_ID)
    {
        if (M_SerNoCtl_ID < 1) throw new IllegalArgumentException ("M_SerNoCtl_ID is mandatory.");
        set_ValueNoCheck ("M_SerNoCtl_ID", Integer.valueOf(M_SerNoCtl_ID));
        
    }
    
    /** Get Serial No Control.
    @return Product Serial Number Control */
    public int getM_SerNoCtl_ID() 
    {
        return get_ValueAsInt("M_SerNoCtl_ID");
        
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
    
    /** Set Prefix.
    @param Prefix Prefix before the sequence number */
    public void setPrefix (String Prefix)
    {
        set_Value ("Prefix", Prefix);
        
    }
    
    /** Get Prefix.
    @return Prefix before the sequence number */
    public String getPrefix() 
    {
        return (String)get_Value("Prefix");
        
    }
    
    /** Set Start No.
    @param StartNo Starting number/position */
    public void setStartNo (int StartNo)
    {
        set_Value ("StartNo", Integer.valueOf(StartNo));
        
    }
    
    /** Get Start No.
    @return Starting number/position */
    public int getStartNo() 
    {
        return get_ValueAsInt("StartNo");
        
    }
    
    /** Set Suffix.
    @param Suffix Suffix after the number */
    public void setSuffix (String Suffix)
    {
        set_Value ("Suffix", Suffix);
        
    }
    
    /** Get Suffix.
    @return Suffix after the number */
    public String getSuffix() 
    {
        return (String)get_Value("Suffix");
        
    }
    
    
}
