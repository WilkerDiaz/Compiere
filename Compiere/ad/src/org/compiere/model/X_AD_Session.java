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
/** Generated Model for AD_Session
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Session.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Session extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Session_ID id
    @param trx transaction
    */
    public X_AD_Session (Ctx ctx, int AD_Session_ID, Trx trx)
    {
        super (ctx, AD_Session_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Session_ID == 0)
        {
            setAD_Session_ID (0);
            setProcessed (false);	// N
            setSessionType (null);	// O
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Session (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27511059984789L;
    /** Last Updated Timestamp 2008-12-10 10:44:28.0 */
    public static final long updatedMS = 1228934668000L;
    /** AD_Table_ID=566 */
    public static final int Table_ID=566;
    
    /** TableName=AD_Session */
    public static final String Table_Name="AD_Session";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Role.
    @param AD_Role_ID Responsibility Role */
    public void setAD_Role_ID (int AD_Role_ID)
    {
        if (AD_Role_ID <= 0) set_Value ("AD_Role_ID", null);
        else
        set_Value ("AD_Role_ID", Integer.valueOf(AD_Role_ID));
        
    }
    
    /** Get Role.
    @return Responsibility Role */
    public int getAD_Role_ID() 
    {
        return get_ValueAsInt("AD_Role_ID");
        
    }
    
    /** Set Session.
    @param AD_Session_ID User Session Online or Web */
    public void setAD_Session_ID (int AD_Session_ID)
    {
        if (AD_Session_ID < 1) throw new IllegalArgumentException ("AD_Session_ID is mandatory.");
        set_ValueNoCheck ("AD_Session_ID", Integer.valueOf(AD_Session_ID));
        
    }
    
    /** Get Session.
    @return User Session Online or Web */
    public int getAD_Session_ID() 
    {
        return get_ValueAsInt("AD_Session_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_Session_ID()));
        
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
    
    /** Set Elapsed Last Acitivity.
    @param ElapsedLastActivityM Elapsed Time in minutes since last activity */
    public void setElapsedLastActivityM (java.math.BigDecimal ElapsedLastActivityM)
    {
        throw new IllegalArgumentException ("ElapsedLastActivityM is virtual column");
        
    }
    
    /** Get Elapsed Last Acitivity.
    @return Elapsed Time in minutes since last activity */
    public java.math.BigDecimal getElapsedLastActivityM() 
    {
        return get_ValueAsBigDecimal("ElapsedLastActivityM");
        
    }
    
    /** Set Elapsed Hours.
    @param ElapsedTimeH Elapsed Time in hours */
    public void setElapsedTimeH (java.math.BigDecimal ElapsedTimeH)
    {
        throw new IllegalArgumentException ("ElapsedTimeH is virtual column");
        
    }
    
    /** Get Elapsed Hours.
    @return Elapsed Time in hours */
    public java.math.BigDecimal getElapsedTimeH() 
    {
        return get_ValueAsBigDecimal("ElapsedTimeH");
        
    }
    
    /** Set Last Activity.
    @param LastActivityTime Last recorded Activity Time of User/Session */
    public void setLastActivityTime (Timestamp LastActivityTime)
    {
        set_ValueNoCheck ("LastActivityTime", LastActivityTime);
        
    }
    
    /** Get Last Activity.
    @return Last recorded Activity Time of User/Session */
    public Timestamp getLastActivityTime() 
    {
        return (Timestamp)get_Value("LastActivityTime");
        
    }
    
    /** Set Last Type.
    @param LastActivityType Last recorded Activity Type of User/Session */
    public void setLastActivityType (String LastActivityType)
    {
        set_ValueNoCheck ("LastActivityType", LastActivityType);
        
    }
    
    /** Get Last Type.
    @return Last recorded Activity Type of User/Session */
    public String getLastActivityType() 
    {
        return (String)get_Value("LastActivityType");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    /** Set Remote Addr.
    @param Remote_Addr Remote Address */
    public void setRemote_Addr (String Remote_Addr)
    {
        set_ValueNoCheck ("Remote_Addr", Remote_Addr);
        
    }
    
    /** Get Remote Addr.
    @return Remote Address */
    public String getRemote_Addr() 
    {
        return (String)get_Value("Remote_Addr");
        
    }
    
    /** Set Remote Host.
    @param Remote_Host Remote host Info */
    public void setRemote_Host (String Remote_Host)
    {
        set_ValueNoCheck ("Remote_Host", Remote_Host);
        
    }
    
    /** Get Remote Host.
    @return Remote host Info */
    public String getRemote_Host() 
    {
        return (String)get_Value("Remote_Host");
        
    }
    
    /** API = A */
    public static final String SESSIONTYPE_API = X_Ref_AD_Session_Type.API.getValue();
    /** GWT Web UI = G */
    public static final String SESSIONTYPE_GWTWebUI = X_Ref_AD_Session_Type.GWT_WEB_UI.getValue();
    /** Other = O */
    public static final String SESSIONTYPE_Other = X_Ref_AD_Session_Type.OTHER.getValue();
    /** Swing UI = S */
    public static final String SESSIONTYPE_SwingUI = X_Ref_AD_Session_Type.SWING_UI.getValue();
    /** Web Store = W */
    public static final String SESSIONTYPE_WebStore = X_Ref_AD_Session_Type.WEB_STORE.getValue();
    /** Web Services = X */
    public static final String SESSIONTYPE_WebServices = X_Ref_AD_Session_Type.WEB_SERVICES.getValue();
    /** Application Server = Z */
    public static final String SESSIONTYPE_ApplicationServer = X_Ref_AD_Session_Type.APPLICATION_SERVER.getValue();
    /** Set Session Type.
    @param SessionType Type of Session */
    public void setSessionType (String SessionType)
    {
        if (SessionType == null) throw new IllegalArgumentException ("SessionType is mandatory");
        if (!X_Ref_AD_Session_Type.isValid(SessionType))
        throw new IllegalArgumentException ("SessionType Invalid value - " + SessionType + " - Reference_ID=492 - A - G - O - S - W - X - Z");
        set_ValueNoCheck ("SessionType", SessionType);
        
    }
    
    /** Get Session Type.
    @return Type of Session */
    public String getSessionType() 
    {
        return (String)get_Value("SessionType");
        
    }
    
    /** Set Web Session.
    @param WebSession Web Session ID */
    public void setWebSession (String WebSession)
    {
        set_ValueNoCheck ("WebSession", WebSession);
        
    }
    
    /** Get Web Session.
    @return Web Session ID */
    public String getWebSession() 
    {
        return (String)get_Value("WebSession");
        
    }
    
    
}
