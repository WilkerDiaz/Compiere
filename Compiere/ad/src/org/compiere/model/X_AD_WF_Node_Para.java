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
/** Generated Model for AD_WF_Node_Para
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_WF_Node_Para.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_WF_Node_Para extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_WF_Node_Para_ID id
    @param trx transaction
    */
    public X_AD_WF_Node_Para (Ctx ctx, int AD_WF_Node_Para_ID, Trx trx)
    {
        super (ctx, AD_WF_Node_Para_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_WF_Node_Para_ID == 0)
        {
            setAD_WF_Node_ID (0);
            setAD_WF_Node_Para_ID (0);
            setEntityType (null);	// U
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_WF_Node_Para (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=643 */
    public static final int Table_ID=643;
    
    /** TableName=AD_WF_Node_Para */
    public static final String Table_Name="AD_WF_Node_Para";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Process Parameter.
    @param AD_Process_Para_ID Process Parameter */
    public void setAD_Process_Para_ID (int AD_Process_Para_ID)
    {
        if (AD_Process_Para_ID <= 0) set_Value ("AD_Process_Para_ID", null);
        else
        set_Value ("AD_Process_Para_ID", Integer.valueOf(AD_Process_Para_ID));
        
    }
    
    /** Get Process Parameter.
    @return Process Parameter */
    public int getAD_Process_Para_ID() 
    {
        return get_ValueAsInt("AD_Process_Para_ID");
        
    }
    
    /** Set Node.
    @param AD_WF_Node_ID Workflow Node (activity), step or process */
    public void setAD_WF_Node_ID (int AD_WF_Node_ID)
    {
        if (AD_WF_Node_ID < 1) throw new IllegalArgumentException ("AD_WF_Node_ID is mandatory.");
        set_ValueNoCheck ("AD_WF_Node_ID", Integer.valueOf(AD_WF_Node_ID));
        
    }
    
    /** Get Node.
    @return Workflow Node (activity), step or process */
    public int getAD_WF_Node_ID() 
    {
        return get_ValueAsInt("AD_WF_Node_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_WF_Node_ID()));
        
    }
    
    /** Set Workflow Node Parameter.
    @param AD_WF_Node_Para_ID Workflow Node Execution Parameter */
    public void setAD_WF_Node_Para_ID (int AD_WF_Node_Para_ID)
    {
        if (AD_WF_Node_Para_ID < 1) throw new IllegalArgumentException ("AD_WF_Node_Para_ID is mandatory.");
        set_ValueNoCheck ("AD_WF_Node_Para_ID", Integer.valueOf(AD_WF_Node_Para_ID));
        
    }
    
    /** Get Workflow Node Parameter.
    @return Workflow Node Execution Parameter */
    public int getAD_WF_Node_Para_ID() 
    {
        return get_ValueAsInt("AD_WF_Node_Para_ID");
        
    }
    
    /** Set Attribute Name.
    @param AttributeName Name of the Attribute */
    public void setAttributeName (String AttributeName)
    {
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
    
    /** Set Entity Type.
    @param EntityType Dictionary Entity Type;
     Determines ownership and synchronization */
    public void setEntityType (String EntityType)
    {
        set_Value ("EntityType", EntityType);
        
    }
    
    /** Get Entity Type.
    @return Dictionary Entity Type;
     Determines ownership and synchronization */
    public String getEntityType() 
    {
        return (String)get_Value("EntityType");
        
    }
    
    
}
