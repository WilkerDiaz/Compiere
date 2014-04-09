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
/** Generated Model for AD_WF_NodeNext
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_WF_NodeNext.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_WF_NodeNext extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_WF_NodeNext_ID id
    @param trx transaction
    */
    public X_AD_WF_NodeNext (Ctx ctx, int AD_WF_NodeNext_ID, Trx trx)
    {
        super (ctx, AD_WF_NodeNext_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_WF_NodeNext_ID == 0)
        {
            setAD_WF_Next_ID (0);
            setAD_WF_NodeNext_ID (0);
            setAD_WF_Node_ID (0);
            setEntityType (null);	// U
            setIsStdUserWorkflow (false);
            setSeqNo (0);	// 10
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_WF_NodeNext (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=131 */
    public static final int Table_ID=131;
    
    /** TableName=AD_WF_NodeNext */
    public static final String Table_Name="AD_WF_NodeNext";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Next Node.
    @param AD_WF_Next_ID Next Node in workflow */
    public void setAD_WF_Next_ID (int AD_WF_Next_ID)
    {
        if (AD_WF_Next_ID < 1) throw new IllegalArgumentException ("AD_WF_Next_ID is mandatory.");
        set_Value ("AD_WF_Next_ID", Integer.valueOf(AD_WF_Next_ID));
        
    }
    
    /** Get Next Node.
    @return Next Node in workflow */
    public int getAD_WF_Next_ID() 
    {
        return get_ValueAsInt("AD_WF_Next_ID");
        
    }
    
    /** Set Node Transition.
    @param AD_WF_NodeNext_ID Workflow Node Transition */
    public void setAD_WF_NodeNext_ID (int AD_WF_NodeNext_ID)
    {
        if (AD_WF_NodeNext_ID < 1) throw new IllegalArgumentException ("AD_WF_NodeNext_ID is mandatory.");
        set_ValueNoCheck ("AD_WF_NodeNext_ID", Integer.valueOf(AD_WF_NodeNext_ID));
        
    }
    
    /** Get Node Transition.
    @return Workflow Node Transition */
    public int getAD_WF_NodeNext_ID() 
    {
        return get_ValueAsInt("AD_WF_NodeNext_ID");
        
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
    
    /** Set Std User Workflow.
    @param IsStdUserWorkflow Standard Manual User Approval Workflow */
    public void setIsStdUserWorkflow (boolean IsStdUserWorkflow)
    {
        set_Value ("IsStdUserWorkflow", Boolean.valueOf(IsStdUserWorkflow));
        
    }
    
    /** Get Std User Workflow.
    @return Standard Manual User Approval Workflow */
    public boolean isStdUserWorkflow() 
    {
        return get_ValueAsBoolean("IsStdUserWorkflow");
        
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
    
    /** Set Transition Code.
    @param TransitionCode Code resulting in TRUE of FALSE */
    public void setTransitionCode (String TransitionCode)
    {
        set_Value ("TransitionCode", TransitionCode);
        
    }
    
    /** Get Transition Code.
    @return Code resulting in TRUE of FALSE */
    public String getTransitionCode() 
    {
        return (String)get_Value("TransitionCode");
        
    }
    
    
}
