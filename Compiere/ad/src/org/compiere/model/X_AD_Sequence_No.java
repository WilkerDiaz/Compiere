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
/** Generated Model for AD_Sequence_No
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Sequence_No.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Sequence_No extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Sequence_No_ID id
    @param trx transaction
    */
    public X_AD_Sequence_No (Ctx ctx, int AD_Sequence_No_ID, Trx trx)
    {
        super (ctx, AD_Sequence_No_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Sequence_No_ID == 0)
        {
            setAD_Sequence_ID (0);
            setCalendarYear (null);
            setCurrentNext (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Sequence_No (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=122 */
    public static final int Table_ID=122;
    
    /** TableName=AD_Sequence_No */
    public static final String Table_Name="AD_Sequence_No";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Sequence.
    @param AD_Sequence_ID Document Sequence */
    public void setAD_Sequence_ID (int AD_Sequence_ID)
    {
        if (AD_Sequence_ID < 1) throw new IllegalArgumentException ("AD_Sequence_ID is mandatory.");
        set_ValueNoCheck ("AD_Sequence_ID", Integer.valueOf(AD_Sequence_ID));
        
    }
    
    /** Get Sequence.
    @return Document Sequence */
    public int getAD_Sequence_ID() 
    {
        return get_ValueAsInt("AD_Sequence_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_Sequence_ID()));
        
    }
    
    /** Set Year.
    @param CalendarYear Calendar Year */
    public void setCalendarYear (String CalendarYear)
    {
        if (CalendarYear == null) throw new IllegalArgumentException ("CalendarYear is mandatory.");
        set_ValueNoCheck ("CalendarYear", CalendarYear);
        
    }
    
    /** Get Year.
    @return Calendar Year */
    public String getCalendarYear() 
    {
        return (String)get_Value("CalendarYear");
        
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
    
    
}
