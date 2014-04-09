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
/** Generated Model for CM_WebAccessLog
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_CM_WebAccessLog.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_CM_WebAccessLog extends PO
{
    /** Standard Constructor
    @param ctx context
    @param CM_WebAccessLog_ID id
    @param trx transaction
    */
    public X_CM_WebAccessLog (Ctx ctx, int CM_WebAccessLog_ID, Trx trx)
    {
        super (ctx, CM_WebAccessLog_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (CM_WebAccessLog_ID == 0)
        {
            setCM_WebAccessLog_ID (0);
            setIP_Address (null);
            setLogType (null);
            setProtocol (null);
            setRequestType (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_CM_WebAccessLog (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=894 */
    public static final int Table_ID=894;
    
    /** TableName=CM_WebAccessLog */
    public static final String Table_Name="CM_WebAccessLog";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
        else
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Accept Language.
    @param AcceptLanguage Language accepted based on browser information */
    public void setAcceptLanguage (String AcceptLanguage)
    {
        set_Value ("AcceptLanguage", AcceptLanguage);
        
    }
    
    /** Get Accept Language.
    @return Language accepted based on browser information */
    public String getAcceptLanguage() 
    {
        return (String)get_Value("AcceptLanguage");
        
    }
    
    /** Set Broadcast Server.
    @param CM_BroadcastServer_ID Web Broadcast Server */
    public void setCM_BroadcastServer_ID (int CM_BroadcastServer_ID)
    {
        if (CM_BroadcastServer_ID <= 0) set_Value ("CM_BroadcastServer_ID", null);
        else
        set_Value ("CM_BroadcastServer_ID", Integer.valueOf(CM_BroadcastServer_ID));
        
    }
    
    /** Get Broadcast Server.
    @return Web Broadcast Server */
    public int getCM_BroadcastServer_ID() 
    {
        return get_ValueAsInt("CM_BroadcastServer_ID");
        
    }
    
    /** Set Media Item.
    @param CM_Media_ID Contains media content like images, flash movies etc. */
    public void setCM_Media_ID (int CM_Media_ID)
    {
        if (CM_Media_ID <= 0) set_Value ("CM_Media_ID", null);
        else
        set_Value ("CM_Media_ID", Integer.valueOf(CM_Media_ID));
        
    }
    
    /** Get Media Item.
    @return Contains media content like images, flash movies etc. */
    public int getCM_Media_ID() 
    {
        return get_ValueAsInt("CM_Media_ID");
        
    }
    
    /** Set Web Access Log.
    @param CM_WebAccessLog_ID Web Access Log Information */
    public void setCM_WebAccessLog_ID (int CM_WebAccessLog_ID)
    {
        if (CM_WebAccessLog_ID < 1) throw new IllegalArgumentException ("CM_WebAccessLog_ID is mandatory.");
        set_ValueNoCheck ("CM_WebAccessLog_ID", Integer.valueOf(CM_WebAccessLog_ID));
        
    }
    
    /** Get Web Access Log.
    @return Web Access Log Information */
    public int getCM_WebAccessLog_ID() 
    {
        return get_ValueAsInt("CM_WebAccessLog_ID");
        
    }
    
    /** Set Web Project.
    @param CM_WebProject_ID A web project is the main data container for Containers, URLs, Ads, and Media etc. */
    public void setCM_WebProject_ID (int CM_WebProject_ID)
    {
        if (CM_WebProject_ID <= 0) set_Value ("CM_WebProject_ID", null);
        else
        set_Value ("CM_WebProject_ID", Integer.valueOf(CM_WebProject_ID));
        
    }
    
    /** Get Web Project.
    @return A web project is the main data container for Containers, URLs, Ads, and Media etc. */
    public int getCM_WebProject_ID() 
    {
        return get_ValueAsInt("CM_WebProject_ID");
        
    }
    
    /** Set File Size.
    @param FileSize Size of the File in bytes */
    public void setFileSize (java.math.BigDecimal FileSize)
    {
        set_Value ("FileSize", FileSize);
        
    }
    
    /** Get File Size.
    @return Size of the File in bytes */
    public java.math.BigDecimal getFileSize() 
    {
        return get_ValueAsBigDecimal("FileSize");
        
    }
    
    /** Set Hyphen.
    @param Hyphen Hyphen */
    public void setHyphen (String Hyphen)
    {
        set_Value ("Hyphen", Hyphen);
        
    }
    
    /** Get Hyphen.
    @return Hyphen */
    public String getHyphen() 
    {
        return (String)get_Value("Hyphen");
        
    }
    
    /** Set IP Address.
    @param IP_Address Defines the IP address to transfer data to */
    public void setIP_Address (String IP_Address)
    {
        if (IP_Address == null) throw new IllegalArgumentException ("IP_Address is mandatory.");
        set_Value ("IP_Address", IP_Address);
        
    }
    
    /** Get IP Address.
    @return Defines the IP address to transfer data to */
    public String getIP_Address() 
    {
        return (String)get_Value("IP_Address");
        
    }
    
    /** Ad display = A */
    public static final String LOGTYPE_AdDisplay = X_Ref_CM_WebAccessLog_Type.AD_DISPLAY.getValue();
    /** Redirect = R */
    public static final String LOGTYPE_Redirect = X_Ref_CM_WebAccessLog_Type.REDIRECT.getValue();
    /** Web Access = W */
    public static final String LOGTYPE_WebAccess = X_Ref_CM_WebAccessLog_Type.WEB_ACCESS.getValue();
    /** Set Log Type.
    @param LogType Web Log Type */
    public void setLogType (String LogType)
    {
        if (LogType == null) throw new IllegalArgumentException ("LogType is mandatory");
        if (!X_Ref_CM_WebAccessLog_Type.isValid(LogType))
        throw new IllegalArgumentException ("LogType Invalid value - " + LogType + " - Reference_ID=390 - A - R - W");
        set_Value ("LogType", LogType);
        
    }
    
    /** Get Log Type.
    @return Web Log Type */
    public String getLogType() 
    {
        return (String)get_Value("LogType");
        
    }
    
    /** Set Page URL.
    @param PageURL Page URL */
    public void setPageURL (String PageURL)
    {
        set_Value ("PageURL", PageURL);
        
    }
    
    /** Get Page URL.
    @return Page URL */
    public String getPageURL() 
    {
        return (String)get_Value("PageURL");
        
    }
    
    /** Set Protocol.
    @param Protocol Protocol */
    public void setProtocol (String Protocol)
    {
        if (Protocol == null) throw new IllegalArgumentException ("Protocol is mandatory.");
        set_Value ("Protocol", Protocol);
        
    }
    
    /** Get Protocol.
    @return Protocol */
    public String getProtocol() 
    {
        return (String)get_Value("Protocol");
        
    }
    
    /** Set Referrer.
    @param Referrer Referring web address */
    public void setReferrer (String Referrer)
    {
        set_Value ("Referrer", Referrer);
        
    }
    
    /** Get Referrer.
    @return Referring web address */
    public String getReferrer() 
    {
        return (String)get_Value("Referrer");
        
    }
    
    /** Set Remote Addr.
    @param Remote_Addr Remote Address */
    public void setRemote_Addr (String Remote_Addr)
    {
        set_Value ("Remote_Addr", Remote_Addr);
        
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
        set_Value ("Remote_Host", Remote_Host);
        
    }
    
    /** Get Remote Host.
    @return Remote host Info */
    public String getRemote_Host() 
    {
        return (String)get_Value("Remote_Host");
        
    }
    
    /** Set Request Type.
    @param RequestType Request Type */
    public void setRequestType (String RequestType)
    {
        if (RequestType == null) throw new IllegalArgumentException ("RequestType is mandatory.");
        set_Value ("RequestType", RequestType);
        
    }
    
    /** Get Request Type.
    @return Request Type */
    public String getRequestType() 
    {
        return (String)get_Value("RequestType");
        
    }
    
    /** Set Status Code.
    @param StatusCode Status Code */
    public void setStatusCode (int StatusCode)
    {
        set_Value ("StatusCode", Integer.valueOf(StatusCode));
        
    }
    
    /** Get Status Code.
    @return Status Code */
    public int getStatusCode() 
    {
        return get_ValueAsInt("StatusCode");
        
    }
    
    /** Set User Agent.
    @param UserAgent Browser Used */
    public void setUserAgent (String UserAgent)
    {
        set_Value ("UserAgent", UserAgent);
        
    }
    
    /** Get User Agent.
    @return Browser Used */
    public String getUserAgent() 
    {
        return (String)get_Value("UserAgent");
        
    }
    
    /** Set Web Session.
    @param WebSession Web Session ID */
    public void setWebSession (String WebSession)
    {
        set_Value ("WebSession", WebSession);
        
    }
    
    /** Get Web Session.
    @return Web Session ID */
    public String getWebSession() 
    {
        return (String)get_Value("WebSession");
        
    }
    
    
}
