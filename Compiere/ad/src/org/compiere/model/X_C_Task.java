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
/** Generated Model for C_Task
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Task.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Task extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Task_ID id
    @param trx transaction
    */
    public X_C_Task (Ctx ctx, int C_Task_ID, Trx trx)
    {
        super (ctx, C_Task_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Task_ID == 0)
        {
            setC_Phase_ID (0);
            setC_Task_ID (0);
            setName (null);
            setSeqNo (0);	// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM C_Task WHERE C_Phase_ID=@C_Phase_ID@
            setStandardQty (Env.ZERO);	// 1
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Task (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=583 */
    public static final int Table_ID=583;
    
    /** TableName=C_Task */
    public static final String Table_Name="C_Task";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Standard Phase.
    @param C_Phase_ID Standard Phase of the Project Type */
    public void setC_Phase_ID (int C_Phase_ID)
    {
        if (C_Phase_ID < 1) throw new IllegalArgumentException ("C_Phase_ID is mandatory.");
        set_ValueNoCheck ("C_Phase_ID", Integer.valueOf(C_Phase_ID));
        
    }
    
    /** Get Standard Phase.
    @return Standard Phase of the Project Type */
    public int getC_Phase_ID() 
    {
        return get_ValueAsInt("C_Phase_ID");
        
    }
    
    /** Set Standard Task.
    @param C_Task_ID Standard Project Type Task */
    public void setC_Task_ID (int C_Task_ID)
    {
        if (C_Task_ID < 1) throw new IllegalArgumentException ("C_Task_ID is mandatory.");
        set_ValueNoCheck ("C_Task_ID", Integer.valueOf(C_Task_ID));
        
    }
    
    /** Get Standard Task.
    @return Standard Project Type Task */
    public int getC_Task_ID() 
    {
        return get_ValueAsInt("C_Task_ID");
        
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
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
        else
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
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
    
    /** Set Standard Quantity.
    @param StandardQty Standard Quantity */
    public void setStandardQty (java.math.BigDecimal StandardQty)
    {
        if (StandardQty == null) throw new IllegalArgumentException ("StandardQty is mandatory.");
        set_Value ("StandardQty", StandardQty);
        
    }
    
    /** Get Standard Quantity.
    @return Standard Quantity */
    public java.math.BigDecimal getStandardQty() 
    {
        return get_ValueAsBigDecimal("StandardQty");
        
    }
    
    /** Personal Activity = A */
    public static final String TASKTYPE_PersonalActivity = X_Ref_C_Task_Type.PERSONAL_ACTIVITY.getValue();
    /** Delegation = D */
    public static final String TASKTYPE_Delegation = X_Ref_C_Task_Type.DELEGATION.getValue();
    /** Other = O */
    public static final String TASKTYPE_Other = X_Ref_C_Task_Type.OTHER.getValue();
    /** Research = R */
    public static final String TASKTYPE_Research = X_Ref_C_Task_Type.RESEARCH.getValue();
    /** Test/Verify = T */
    public static final String TASKTYPE_TestVerify = X_Ref_C_Task_Type.TEST_VERIFY.getValue();
    /** Set Task Type.
    @param TaskType Type of Project Task */
    public void setTaskType (String TaskType)
    {
        if (!X_Ref_C_Task_Type.isValid(TaskType))
        throw new IllegalArgumentException ("TaskType Invalid value - " + TaskType + " - Reference_ID=408 - A - D - O - R - T");
        set_Value ("TaskType", TaskType);
        
    }
    
    /** Get Task Type.
    @return Type of Project Task */
    public String getTaskType() 
    {
        return (String)get_Value("TaskType");
        
    }
    
    
}
