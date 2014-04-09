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
/** Generated Model for C_NonBusinessDay
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_NonBusinessDay.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_NonBusinessDay extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_NonBusinessDay_ID id
    @param trx transaction
    */
    public X_C_NonBusinessDay (Ctx ctx, int C_NonBusinessDay_ID, Trx trx)
    {
        super (ctx, C_NonBusinessDay_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_NonBusinessDay_ID == 0)
        {
            setC_Calendar_ID (0);
            setC_NonBusinessDay_ID (0);
            setDate1 (new Timestamp(System.currentTimeMillis()));
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_NonBusinessDay (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=163 */
    public static final int Table_ID=163;
    
    /** TableName=C_NonBusinessDay */
    public static final String Table_Name="C_NonBusinessDay";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Calendar.
    @param C_Calendar_ID Accounting Calendar Name */
    public void setC_Calendar_ID (int C_Calendar_ID)
    {
        if (C_Calendar_ID < 1) throw new IllegalArgumentException ("C_Calendar_ID is mandatory.");
        set_ValueNoCheck ("C_Calendar_ID", Integer.valueOf(C_Calendar_ID));
        
    }
    
    /** Get Calendar.
    @return Accounting Calendar Name */
    public int getC_Calendar_ID() 
    {
        return get_ValueAsInt("C_Calendar_ID");
        
    }
    
    /** Set Non Business Day.
    @param C_NonBusinessDay_ID Day on which business is not transacted */
    public void setC_NonBusinessDay_ID (int C_NonBusinessDay_ID)
    {
        if (C_NonBusinessDay_ID < 1) throw new IllegalArgumentException ("C_NonBusinessDay_ID is mandatory.");
        set_ValueNoCheck ("C_NonBusinessDay_ID", Integer.valueOf(C_NonBusinessDay_ID));
        
    }
    
    /** Get Non Business Day.
    @return Day on which business is not transacted */
    public int getC_NonBusinessDay_ID() 
    {
        return get_ValueAsInt("C_NonBusinessDay_ID");
        
    }
    
    /** Set Date.
    @param Date1 Date when business is not conducted */
    public void setDate1 (Timestamp Date1)
    {
        if (Date1 == null) throw new IllegalArgumentException ("Date1 is mandatory.");
        set_Value ("Date1", Date1);
        
    }
    
    /** Get Date.
    @return Date when business is not conducted */
    public Timestamp getDate1() 
    {
        return (Timestamp)get_Value("Date1");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
    }
    
    
}
