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
/** Generated Model for AD_PInstance_Log
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_PInstance_Log.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_PInstance_Log extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_PInstance_Log_ID id
    @param trx transaction
    */
    public X_AD_PInstance_Log (Ctx ctx, int AD_PInstance_Log_ID, Trx trx)
    {
        super (ctx, AD_PInstance_Log_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_PInstance_Log_ID == 0)
        {
            setAD_PInstance_ID (0);
            setLog_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_PInstance_Log (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27511142558789L;
    /** Last Updated Timestamp 2008-12-11 09:40:42.0 */
    public static final long updatedMS = 1229017242000L;
    /** AD_Table_ID=578 */
    public static final int Table_ID=578;
    
    /** TableName=AD_PInstance_Log */
    public static final String Table_Name="AD_PInstance_Log";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Process Instance.
    @param AD_PInstance_ID Instance of the process */
    public void setAD_PInstance_ID (int AD_PInstance_ID)
    {
        if (AD_PInstance_ID < 1) throw new IllegalArgumentException ("AD_PInstance_ID is mandatory.");
        set_ValueNoCheck ("AD_PInstance_ID", Integer.valueOf(AD_PInstance_ID));
        
    }
    
    /** Get Process Instance.
    @return Instance of the process */
    public int getAD_PInstance_ID() 
    {
        return get_ValueAsInt("AD_PInstance_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_PInstance_ID()));
        
    }
    
    /** Set Log.
    @param Log_ID Log */
    public void setLog_ID (int Log_ID)
    {
        if (Log_ID < 1) throw new IllegalArgumentException ("Log_ID is mandatory.");
        set_ValueNoCheck ("Log_ID", Integer.valueOf(Log_ID));
        
    }
    
    /** Get Log.
    @return Log */
    public int getLog_ID() 
    {
        return get_ValueAsInt("Log_ID");
        
    }
    
    /** Set Process Date.
    @param P_Date Process Parameter */
    public void setP_Date (Timestamp P_Date)
    {
        set_ValueNoCheck ("P_Date", P_Date);
        
    }
    
    /** Get Process Date.
    @return Process Parameter */
    public Timestamp getP_Date() 
    {
        return (Timestamp)get_Value("P_Date");
        
    }
    
    /** Set Process ID.
    @param P_ID Process ID */
    public void setP_ID (int P_ID)
    {
        if (P_ID <= 0) set_ValueNoCheck ("P_ID", null);
        else
        set_ValueNoCheck ("P_ID", Integer.valueOf(P_ID));
        
    }
    
    /** Get Process ID.
    @return Process ID */
    public int getP_ID() 
    {
        return get_ValueAsInt("P_ID");
        
    }
    
    /** Set Process Message.
    @param P_Msg Process Message */
    public void setP_Msg (String P_Msg)
    {
        set_ValueNoCheck ("P_Msg", P_Msg);
        
    }
    
    /** Get Process Message.
    @return Process Message */
    public String getP_Msg() 
    {
        return (String)get_Value("P_Msg");
        
    }
    
    /** Set Process Number.
    @param P_Number Process Parameter */
    public void setP_Number (java.math.BigDecimal P_Number)
    {
        set_ValueNoCheck ("P_Number", P_Number);
        
    }
    
    /** Get Process Number.
    @return Process Parameter */
    public java.math.BigDecimal getP_Number() 
    {
        return get_ValueAsBigDecimal("P_Number");
        
    }
    
    
}
