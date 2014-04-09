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
/** Generated Model for AD_LdapProcessorLog
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_LdapProcessorLog.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_LdapProcessorLog extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_LdapProcessorLog_ID id
    @param trx transaction
    */
    public X_AD_LdapProcessorLog (Ctx ctx, int AD_LdapProcessorLog_ID, Trx trx)
    {
        super (ctx, AD_LdapProcessorLog_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_LdapProcessorLog_ID == 0)
        {
            setAD_LdapProcessorLog_ID (0);
            setAD_LdapProcessor_ID (0);
            setIsError (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_LdapProcessorLog (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27511157020789L;
    /** Last Updated Timestamp 2008-12-11 13:41:44.0 */
    public static final long updatedMS = 1229031704000L;
    /** AD_Table_ID=903 */
    public static final int Table_ID=903;
    
    /** TableName=AD_LdapProcessorLog */
    public static final String Table_Name="AD_LdapProcessorLog";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Ldap Processor Log.
    @param AD_LdapProcessorLog_ID LDAP Server Log */
    public void setAD_LdapProcessorLog_ID (int AD_LdapProcessorLog_ID)
    {
        if (AD_LdapProcessorLog_ID < 1) throw new IllegalArgumentException ("AD_LdapProcessorLog_ID is mandatory.");
        set_ValueNoCheck ("AD_LdapProcessorLog_ID", Integer.valueOf(AD_LdapProcessorLog_ID));
        
    }
    
    /** Get Ldap Processor Log.
    @return LDAP Server Log */
    public int getAD_LdapProcessorLog_ID() 
    {
        return get_ValueAsInt("AD_LdapProcessorLog_ID");
        
    }
    
    /** Set Ldap Processor.
    @param AD_LdapProcessor_ID LDAP Server to authenticate and authorize external systems based on Compiere */
    public void setAD_LdapProcessor_ID (int AD_LdapProcessor_ID)
    {
        if (AD_LdapProcessor_ID < 1) throw new IllegalArgumentException ("AD_LdapProcessor_ID is mandatory.");
        set_ValueNoCheck ("AD_LdapProcessor_ID", Integer.valueOf(AD_LdapProcessor_ID));
        
    }
    
    /** Get Ldap Processor.
    @return LDAP Server to authenticate and authorize external systems based on Compiere */
    public int getAD_LdapProcessor_ID() 
    {
        return get_ValueAsInt("AD_LdapProcessor_ID");
        
    }
    
    /** Set BinaryData.
    @param BinaryData Binary Data */
    public void setBinaryData (byte[] BinaryData)
    {
        set_Value ("BinaryData", BinaryData);
        
    }
    
    /** Get BinaryData.
    @return Binary Data */
    public byte[] getBinaryData() 
    {
        return (byte[])get_Value("BinaryData");
        
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
    
    /** Set Error.
    @param IsError An Error that has occurred in the execution */
    public void setIsError (boolean IsError)
    {
        set_Value ("IsError", Boolean.valueOf(IsError));
        
    }
    
    /** Get Error.
    @return An Error that has occurred in the execution */
    public boolean isError() 
    {
        return get_ValueAsBoolean("IsError");
        
    }
    
    /** Set Reference.
    @param Reference Reference for this record */
    public void setReference (String Reference)
    {
        set_Value ("Reference", Reference);
        
    }
    
    /** Get Reference.
    @return Reference for this record */
    public String getReference() 
    {
        return (String)get_Value("Reference");
        
    }
    
    /** Set Summary.
    @param Summary Textual summary of this request */
    public void setSummary (String Summary)
    {
        set_Value ("Summary", Summary);
        
    }
    
    /** Get Summary.
    @return Textual summary of this request */
    public String getSummary() 
    {
        return (String)get_Value("Summary");
        
    }
    
    /** Set Text Message.
    @param TextMsg Text Message */
    public void setTextMsg (String TextMsg)
    {
        set_Value ("TextMsg", TextMsg);
        
    }
    
    /** Get Text Message.
    @return Text Message */
    public String getTextMsg() 
    {
        return (String)get_Value("TextMsg");
        
    }
    
    
}
