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
/** Generated Model for AD_AlertProcessorLog
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_AlertProcessorLog.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_AlertProcessorLog extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_AlertProcessorLog_ID id
    @param trx transaction
    */
    public X_AD_AlertProcessorLog (Ctx ctx, int AD_AlertProcessorLog_ID, Trx trx)
    {
        super (ctx, AD_AlertProcessorLog_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_AlertProcessorLog_ID == 0)
        {
            setAD_AlertProcessorLog_ID (0);
            setAD_AlertProcessor_ID (0);
            setIsError (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_AlertProcessorLog (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=699 */
    public static final int Table_ID=699;
    
    /** TableName=AD_AlertProcessorLog */
    public static final String Table_Name="AD_AlertProcessorLog";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Alert Processor Log.
    @param AD_AlertProcessorLog_ID Result of the execution of the Alert Processor */
    public void setAD_AlertProcessorLog_ID (int AD_AlertProcessorLog_ID)
    {
        if (AD_AlertProcessorLog_ID < 1) throw new IllegalArgumentException ("AD_AlertProcessorLog_ID is mandatory.");
        set_ValueNoCheck ("AD_AlertProcessorLog_ID", Integer.valueOf(AD_AlertProcessorLog_ID));
        
    }
    
    /** Get Alert Processor Log.
    @return Result of the execution of the Alert Processor */
    public int getAD_AlertProcessorLog_ID() 
    {
        return get_ValueAsInt("AD_AlertProcessorLog_ID");
        
    }
    
    /** Set Alert Processor.
    @param AD_AlertProcessor_ID Alert Processor/Server Parameter */
    public void setAD_AlertProcessor_ID (int AD_AlertProcessor_ID)
    {
        if (AD_AlertProcessor_ID < 1) throw new IllegalArgumentException ("AD_AlertProcessor_ID is mandatory.");
        set_ValueNoCheck ("AD_AlertProcessor_ID", Integer.valueOf(AD_AlertProcessor_ID));
        
    }
    
    /** Get Alert Processor.
    @return Alert Processor/Server Parameter */
    public int getAD_AlertProcessor_ID() 
    {
        return get_ValueAsInt("AD_AlertProcessor_ID");
        
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
