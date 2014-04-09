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
/** Generated Model for AD_LoginMsgLog
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_LoginMsgLog.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_LoginMsgLog extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_LoginMsgLog_ID id
    @param trx transaction
    */
    public X_AD_LoginMsgLog (Ctx ctx, int AD_LoginMsgLog_ID, Trx trx)
    {
        super (ctx, AD_LoginMsgLog_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_LoginMsgLog_ID == 0)
        {
            setAD_LoginMsgLog_ID (0);
            setAD_LoginMsg_ID (0);
            setAD_User_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_LoginMsgLog (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27500341400789L;
    /** Last Updated Timestamp 2008-08-08 10:21:24.0 */
    public static final long updatedMS = 1218216084000L;
    /** AD_Table_ID=1066 */
    public static final int Table_ID=1066;
    
    /** TableName=AD_LoginMsgLog */
    public static final String Table_Name="AD_LoginMsgLog";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Login Msg Log.
    @param AD_LoginMsgLog_ID Login Message Log */
    public void setAD_LoginMsgLog_ID (int AD_LoginMsgLog_ID)
    {
        if (AD_LoginMsgLog_ID < 1) throw new IllegalArgumentException ("AD_LoginMsgLog_ID is mandatory.");
        set_ValueNoCheck ("AD_LoginMsgLog_ID", Integer.valueOf(AD_LoginMsgLog_ID));
        
    }
    
    /** Get Login Msg Log.
    @return Login Message Log */
    public int getAD_LoginMsgLog_ID() 
    {
        return get_ValueAsInt("AD_LoginMsgLog_ID");
        
    }
    
    /** Set Login Message.
    @param AD_LoginMsg_ID Login Message for System Users */
    public void setAD_LoginMsg_ID (int AD_LoginMsg_ID)
    {
        if (AD_LoginMsg_ID < 1) throw new IllegalArgumentException ("AD_LoginMsg_ID is mandatory.");
        set_ValueNoCheck ("AD_LoginMsg_ID", Integer.valueOf(AD_LoginMsg_ID));
        
    }
    
    /** Get Login Message.
    @return Login Message for System Users */
    public int getAD_LoginMsg_ID() 
    {
        return get_ValueAsInt("AD_LoginMsg_ID");
        
    }
    
    /** Set Session.
    @param AD_Session_ID User Session Online or Web */
    public void setAD_Session_ID (int AD_Session_ID)
    {
        if (AD_Session_ID <= 0) set_ValueNoCheck ("AD_Session_ID", null);
        else
        set_ValueNoCheck ("AD_Session_ID", Integer.valueOf(AD_Session_ID));
        
    }
    
    /** Get Session.
    @return User Session Online or Web */
    public int getAD_Session_ID() 
    {
        return get_ValueAsInt("AD_Session_ID");
        
    }
    
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID < 1) throw new IllegalArgumentException ("AD_User_ID is mandatory.");
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
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
    
    /** No = N */
    public static final String ISUSERACCEPTED_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISUSERACCEPTED_Yes = X_Ref__YesNo.YES.getValue();
    /** Set User Accepted.
    @param IsUserAccepted User Accepted */
    public void setIsUserAccepted (String IsUserAccepted)
    {
        if (!X_Ref__YesNo.isValid(IsUserAccepted))
        throw new IllegalArgumentException ("IsUserAccepted Invalid value - " + IsUserAccepted + " - Reference_ID=319 - N - Y");
        set_ValueNoCheck ("IsUserAccepted", IsUserAccepted);
        
    }
    
    /** Get User Accepted.
    @return User Accepted */
    public String getIsUserAccepted() 
    {
        return (String)get_Value("IsUserAccepted");
        
    }
    
    
}
