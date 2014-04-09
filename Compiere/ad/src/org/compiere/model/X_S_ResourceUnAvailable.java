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
/** Generated Model for S_ResourceUnAvailable
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_S_ResourceUnAvailable.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_S_ResourceUnAvailable extends PO
{
    /** Standard Constructor
    @param ctx context
    @param S_ResourceUnAvailable_ID id
    @param trx transaction
    */
    public X_S_ResourceUnAvailable (Ctx ctx, int S_ResourceUnAvailable_ID, Trx trx)
    {
        super (ctx, S_ResourceUnAvailable_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (S_ResourceUnAvailable_ID == 0)
        {
            setDateFrom (new Timestamp(System.currentTimeMillis()));
            setS_ResourceUnAvailable_ID (0);
            setS_Resource_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_S_ResourceUnAvailable (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=482 */
    public static final int Table_ID=482;
    
    /** TableName=S_ResourceUnAvailable */
    public static final String Table_Name="S_ResourceUnAvailable";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Date From.
    @param DateFrom Starting date for a range */
    public void setDateFrom (Timestamp DateFrom)
    {
        if (DateFrom == null) throw new IllegalArgumentException ("DateFrom is mandatory.");
        set_Value ("DateFrom", DateFrom);
        
    }
    
    /** Get Date From.
    @return Starting date for a range */
    public Timestamp getDateFrom() 
    {
        return (Timestamp)get_Value("DateFrom");
        
    }
    
    /** Set Date To.
    @param DateTo End date of a date range */
    public void setDateTo (Timestamp DateTo)
    {
        set_Value ("DateTo", DateTo);
        
    }
    
    /** Get Date To.
    @return End date of a date range */
    public Timestamp getDateTo() 
    {
        return (Timestamp)get_Value("DateTo");
        
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
    
    /** Set Resource Unavailability.
    @param S_ResourceUnAvailable_ID Resource Unavailability */
    public void setS_ResourceUnAvailable_ID (int S_ResourceUnAvailable_ID)
    {
        if (S_ResourceUnAvailable_ID < 1) throw new IllegalArgumentException ("S_ResourceUnAvailable_ID is mandatory.");
        set_ValueNoCheck ("S_ResourceUnAvailable_ID", Integer.valueOf(S_ResourceUnAvailable_ID));
        
    }
    
    /** Get Resource Unavailability.
    @return Resource Unavailability */
    public int getS_ResourceUnAvailable_ID() 
    {
        return get_ValueAsInt("S_ResourceUnAvailable_ID");
        
    }
    
    /** Set Resource.
    @param S_Resource_ID Resource */
    public void setS_Resource_ID (int S_Resource_ID)
    {
        if (S_Resource_ID < 1) throw new IllegalArgumentException ("S_Resource_ID is mandatory.");
        set_ValueNoCheck ("S_Resource_ID", Integer.valueOf(S_Resource_ID));
        
    }
    
    /** Get Resource.
    @return Resource */
    public int getS_Resource_ID() 
    {
        return get_ValueAsInt("S_Resource_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getS_Resource_ID()));
        
    }
    
    
}
