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
/** Generated Model for AD_WF_ProcessData
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_WF_ProcessData.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_WF_ProcessData extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_WF_ProcessData_ID id
    @param trx transaction
    */
    public X_AD_WF_ProcessData (Ctx ctx, int AD_WF_ProcessData_ID, Trx trx)
    {
        super (ctx, AD_WF_ProcessData_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_WF_ProcessData_ID == 0)
        {
            setAD_WF_ProcessData_ID (0);
            setAD_WF_Process_ID (0);
            setAttributeName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_WF_ProcessData (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=648 */
    public static final int Table_ID=648;
    
    /** TableName=AD_WF_ProcessData */
    public static final String Table_Name="AD_WF_ProcessData";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Workflow Process Data.
    @param AD_WF_ProcessData_ID Workflow Process Context */
    public void setAD_WF_ProcessData_ID (int AD_WF_ProcessData_ID)
    {
        if (AD_WF_ProcessData_ID < 1) throw new IllegalArgumentException ("AD_WF_ProcessData_ID is mandatory.");
        set_ValueNoCheck ("AD_WF_ProcessData_ID", Integer.valueOf(AD_WF_ProcessData_ID));
        
    }
    
    /** Get Workflow Process Data.
    @return Workflow Process Context */
    public int getAD_WF_ProcessData_ID() 
    {
        return get_ValueAsInt("AD_WF_ProcessData_ID");
        
    }
    
    /** Set Workflow Process.
    @param AD_WF_Process_ID Actual Workflow Process Instance */
    public void setAD_WF_Process_ID (int AD_WF_Process_ID)
    {
        if (AD_WF_Process_ID < 1) throw new IllegalArgumentException ("AD_WF_Process_ID is mandatory.");
        set_ValueNoCheck ("AD_WF_Process_ID", Integer.valueOf(AD_WF_Process_ID));
        
    }
    
    /** Get Workflow Process.
    @return Actual Workflow Process Instance */
    public int getAD_WF_Process_ID() 
    {
        return get_ValueAsInt("AD_WF_Process_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_WF_Process_ID()));
        
    }
    
    /** Set Attribute Name.
    @param AttributeName Name of the Attribute */
    public void setAttributeName (String AttributeName)
    {
        if (AttributeName == null) throw new IllegalArgumentException ("AttributeName is mandatory.");
        set_Value ("AttributeName", AttributeName);
        
    }
    
    /** Get Attribute Name.
    @return Name of the Attribute */
    public String getAttributeName() 
    {
        return (String)get_Value("AttributeName");
        
    }
    
    /** Set Attribute Value.
    @param AttributeValue Value of the Attribute */
    public void setAttributeValue (String AttributeValue)
    {
        set_Value ("AttributeValue", AttributeValue);
        
    }
    
    /** Get Attribute Value.
    @return Value of the Attribute */
    public String getAttributeValue() 
    {
        return (String)get_Value("AttributeValue");
        
    }
    
    
}
