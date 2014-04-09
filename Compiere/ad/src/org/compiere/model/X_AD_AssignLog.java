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
/** Generated Model for AD_AssignLog
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_AssignLog.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_AssignLog extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_AssignLog_ID id
    @param trx transaction
    */
    public X_AD_AssignLog (Ctx ctx, int AD_AssignLog_ID, Trx trx)
    {
        super (ctx, AD_AssignLog_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_AssignLog_ID == 0)
        {
            setAD_AssignLog_ID (0);
            setAD_AssignTarget_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_AssignLog (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=933 */
    public static final int Table_ID=933;
    
    /** TableName=AD_AssignLog */
    public static final String Table_Name="AD_AssignLog";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Assign Log.
    @param AD_AssignLog_ID Assignment Log */
    public void setAD_AssignLog_ID (int AD_AssignLog_ID)
    {
        if (AD_AssignLog_ID < 1) throw new IllegalArgumentException ("AD_AssignLog_ID is mandatory.");
        set_ValueNoCheck ("AD_AssignLog_ID", Integer.valueOf(AD_AssignLog_ID));
        
    }
    
    /** Get Assign Log.
    @return Assignment Log */
    public int getAD_AssignLog_ID() 
    {
        return get_ValueAsInt("AD_AssignLog_ID");
        
    }
    
    /** Set Assign Target.
    @param AD_AssignTarget_ID Automatic Assignment Target Column */
    public void setAD_AssignTarget_ID (int AD_AssignTarget_ID)
    {
        if (AD_AssignTarget_ID < 1) throw new IllegalArgumentException ("AD_AssignTarget_ID is mandatory.");
        set_ValueNoCheck ("AD_AssignTarget_ID", Integer.valueOf(AD_AssignTarget_ID));
        
    }
    
    /** Get Assign Target.
    @return Automatic Assignment Target Column */
    public int getAD_AssignTarget_ID() 
    {
        return get_ValueAsInt("AD_AssignTarget_ID");
        
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
    
    /** Set Record ID.
    @param Record_ID Direct internal record ID */
    public void setRecord_ID (int Record_ID)
    {
        if (Record_ID <= 0) set_Value ("Record_ID", null);
        else
        set_Value ("Record_ID", Integer.valueOf(Record_ID));
        
    }
    
    /** Get Record ID.
    @return Direct internal record ID */
    public int getRecord_ID() 
    {
        return get_ValueAsInt("Record_ID");
        
    }
    
    
}
