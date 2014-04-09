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
/** Generated Model for CM_Container_URL
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_CM_Container_URL.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_CM_Container_URL extends PO
{
    /** Standard Constructor
    @param ctx context
    @param CM_Container_URL_ID id
    @param trx transaction
    */
    public X_CM_Container_URL (Ctx ctx, int CM_Container_URL_ID, Trx trx)
    {
        super (ctx, CM_Container_URL_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (CM_Container_URL_ID == 0)
        {
            setCM_Container_ID (0);
            setCM_Container_URL_ID (0);
            setChecked (new Timestamp(System.currentTimeMillis()));
            setLast_Result (null);
            setStatus (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_CM_Container_URL (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=865 */
    public static final int Table_ID=865;
    
    /** TableName=CM_Container_URL */
    public static final String Table_Name="CM_Container_URL";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Web Container.
    @param CM_Container_ID Web Container contains content like images, text etc. */
    public void setCM_Container_ID (int CM_Container_ID)
    {
        if (CM_Container_ID < 1) throw new IllegalArgumentException ("CM_Container_ID is mandatory.");
        set_Value ("CM_Container_ID", Integer.valueOf(CM_Container_ID));
        
    }
    
    /** Get Web Container.
    @return Web Container contains content like images, text etc. */
    public int getCM_Container_ID() 
    {
        return get_ValueAsInt("CM_Container_ID");
        
    }
    
    /** Set Container URL.
    @param CM_Container_URL_ID Contains info on used URLs */
    public void setCM_Container_URL_ID (int CM_Container_URL_ID)
    {
        if (CM_Container_URL_ID < 1) throw new IllegalArgumentException ("CM_Container_URL_ID is mandatory.");
        set_ValueNoCheck ("CM_Container_URL_ID", Integer.valueOf(CM_Container_URL_ID));
        
    }
    
    /** Get Container URL.
    @return Contains info on used URLs */
    public int getCM_Container_URL_ID() 
    {
        return get_ValueAsInt("CM_Container_URL_ID");
        
    }
    
    /** Set Last Checked.
    @param Checked Info when we did the latest check */
    public void setChecked (Timestamp Checked)
    {
        if (Checked == null) throw new IllegalArgumentException ("Checked is mandatory.");
        set_Value ("Checked", Checked);
        
    }
    
    /** Get Last Checked.
    @return Info when we did the latest check */
    public Timestamp getChecked() 
    {
        return (Timestamp)get_Value("Checked");
        
    }
    
    /** Set Last Result.
    @param Last_Result Contains data on the last check result */
    public void setLast_Result (String Last_Result)
    {
        if (Last_Result == null) throw new IllegalArgumentException ("Last_Result is mandatory.");
        set_Value ("Last_Result", Last_Result);
        
    }
    
    /** Get Last Result.
    @return Contains data on the last check result */
    public String getLast_Result() 
    {
        return (String)get_Value("Last_Result");
        
    }
    
    /** Set Status.
    @param Status Status of the currently running check */
    public void setStatus (String Status)
    {
        if (Status == null) throw new IllegalArgumentException ("Status is mandatory.");
        set_Value ("Status", Status);
        
    }
    
    /** Get Status.
    @return Status of the currently running check */
    public String getStatus() 
    {
        return (String)get_Value("Status");
        
    }
    
    
}
