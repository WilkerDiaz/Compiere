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
/** Generated Model for R_Status
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_R_Status.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_R_Status extends PO
{
    /** Standard Constructor
    @param ctx context
    @param R_Status_ID id
    @param trx transaction
    */
    public X_R_Status (Ctx ctx, int R_Status_ID, Trx trx)
    {
        super (ctx, R_Status_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (R_Status_ID == 0)
        {
            setIsClosed (false);	// N
            setIsDefault (false);
            setIsFinalClose (false);	// N
            setIsOpen (false);
            setIsWebCanUpdate (false);
            setName (null);
            setR_StatusCategory_ID (0);
            setR_Status_ID (0);
            setSeqNo (0);	// @SQL=SELECT COALESCE(MAX(SeqNo),0)+10 AS DefaultValue FROM R_Status WHERE R_StatusCategory_ID=@R_StatusCategory_ID@
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_R_Status (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27509347000789L;
    /** Last Updated Timestamp 2008-11-20 14:54:44.0 */
    public static final long updatedMS = 1227221684000L;
    /** AD_Table_ID=776 */
    public static final int Table_ID=776;
    
    /** TableName=R_Status */
    public static final String Table_Name="R_Status";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Closed Status.
    @param IsClosed The status is closed */
    public void setIsClosed (boolean IsClosed)
    {
        set_Value ("IsClosed", Boolean.valueOf(IsClosed));
        
    }
    
    /** Get Closed Status.
    @return The status is closed */
    public boolean isClosed() 
    {
        return get_ValueAsBoolean("IsClosed");
        
    }
    
    /** Set Default.
    @param IsDefault Default value */
    public void setIsDefault (boolean IsDefault)
    {
        set_Value ("IsDefault", Boolean.valueOf(IsDefault));
        
    }
    
    /** Get Default.
    @return Default value */
    public boolean isDefault() 
    {
        return get_ValueAsBoolean("IsDefault");
        
    }
    
    /** Set Final Close.
    @param IsFinalClose Entries with Final Close cannot be re-opened */
    public void setIsFinalClose (boolean IsFinalClose)
    {
        set_Value ("IsFinalClose", Boolean.valueOf(IsFinalClose));
        
    }
    
    /** Get Final Close.
    @return Entries with Final Close cannot be re-opened */
    public boolean isFinalClose() 
    {
        return get_ValueAsBoolean("IsFinalClose");
        
    }
    
    /** Set Open Status.
    @param IsOpen The status is closed */
    public void setIsOpen (boolean IsOpen)
    {
        set_Value ("IsOpen", Boolean.valueOf(IsOpen));
        
    }
    
    /** Get Open Status.
    @return The status is closed */
    public boolean isOpen() 
    {
        return get_ValueAsBoolean("IsOpen");
        
    }
    
    /** Set Web Can Update.
    @param IsWebCanUpdate Entry can be updated from the Web */
    public void setIsWebCanUpdate (boolean IsWebCanUpdate)
    {
        set_Value ("IsWebCanUpdate", Boolean.valueOf(IsWebCanUpdate));
        
    }
    
    /** Get Web Can Update.
    @return Entry can be updated from the Web */
    public boolean isWebCanUpdate() 
    {
        return get_ValueAsBoolean("IsWebCanUpdate");
        
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
    
    /** Set Next Status.
    @param Next_Status_ID Move to next status automatically after timeout */
    public void setNext_Status_ID (int Next_Status_ID)
    {
        if (Next_Status_ID <= 0) set_Value ("Next_Status_ID", null);
        else
        set_Value ("Next_Status_ID", Integer.valueOf(Next_Status_ID));
        
    }
    
    /** Get Next Status.
    @return Move to next status automatically after timeout */
    public int getNext_Status_ID() 
    {
        return get_ValueAsInt("Next_Status_ID");
        
    }
    
    /** Set Status Category.
    @param R_StatusCategory_ID Request Status Category */
    public void setR_StatusCategory_ID (int R_StatusCategory_ID)
    {
        if (R_StatusCategory_ID < 1) throw new IllegalArgumentException ("R_StatusCategory_ID is mandatory.");
        set_ValueNoCheck ("R_StatusCategory_ID", Integer.valueOf(R_StatusCategory_ID));
        
    }
    
    /** Get Status Category.
    @return Request Status Category */
    public int getR_StatusCategory_ID() 
    {
        return get_ValueAsInt("R_StatusCategory_ID");
        
    }
    
    /** Set Status.
    @param R_Status_ID Request Status */
    public void setR_Status_ID (int R_Status_ID)
    {
        if (R_Status_ID < 1) throw new IllegalArgumentException ("R_Status_ID is mandatory.");
        set_ValueNoCheck ("R_Status_ID", Integer.valueOf(R_Status_ID));
        
    }
    
    /** Get Status.
    @return Request Status */
    public int getR_Status_ID() 
    {
        return get_ValueAsInt("R_Status_ID");
        
    }
    
    /** Set Sequence.
    @param SeqNo Method of ordering elements;
     lowest number comes first */
    public void setSeqNo (int SeqNo)
    {
        set_Value ("SeqNo", Integer.valueOf(SeqNo));
        
    }
    
    /** Get Sequence.
    @return Method of ordering elements;
     lowest number comes first */
    public int getSeqNo() 
    {
        return get_ValueAsInt("SeqNo");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getSeqNo()));
        
    }
    
    /** Set Timeout in Days.
    @param TimeoutDays Timeout in Days to change Status automatically */
    public void setTimeoutDays (int TimeoutDays)
    {
        set_Value ("TimeoutDays", Integer.valueOf(TimeoutDays));
        
    }
    
    /** Get Timeout in Days.
    @return Timeout in Days to change Status automatically */
    public int getTimeoutDays() 
    {
        return get_ValueAsInt("TimeoutDays");
        
    }
    
    /** Set Update Status.
    @param Update_Status_ID Automatically change the status after entry from web */
    public void setUpdate_Status_ID (int Update_Status_ID)
    {
        if (Update_Status_ID <= 0) set_Value ("Update_Status_ID", null);
        else
        set_Value ("Update_Status_ID", Integer.valueOf(Update_Status_ID));
        
    }
    
    /** Get Update Status.
    @return Automatically change the status after entry from web */
    public int getUpdate_Status_ID() 
    {
        return get_ValueAsInt("Update_Status_ID");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    
}
