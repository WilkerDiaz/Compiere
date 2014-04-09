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
/** Generated Model for AD_WorkbenchWindow
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_WorkbenchWindow.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_WorkbenchWindow extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_WorkbenchWindow_ID id
    @param trx transaction
    */
    public X_AD_WorkbenchWindow (Ctx ctx, int AD_WorkbenchWindow_ID, Trx trx)
    {
        super (ctx, AD_WorkbenchWindow_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_WorkbenchWindow_ID == 0)
        {
            setAD_WorkbenchWindow_ID (0);
            setAD_Workbench_ID (0);
            setEntityType (null);	// U
            setIsPrimary (false);
            setSeqNo (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_WorkbenchWindow (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=469 */
    public static final int Table_ID=469;
    
    /** TableName=AD_WorkbenchWindow */
    public static final String Table_Name="AD_WorkbenchWindow";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Special Form.
    @param AD_Form_ID Special Form */
    public void setAD_Form_ID (int AD_Form_ID)
    {
        if (AD_Form_ID <= 0) set_Value ("AD_Form_ID", null);
        else
        set_Value ("AD_Form_ID", Integer.valueOf(AD_Form_ID));
        
    }
    
    /** Get Special Form.
    @return Special Form */
    public int getAD_Form_ID() 
    {
        return get_ValueAsInt("AD_Form_ID");
        
    }
    
    /** Set Process.
    @param AD_Process_ID Process or Report */
    public void setAD_Process_ID (int AD_Process_ID)
    {
        if (AD_Process_ID <= 0) set_Value ("AD_Process_ID", null);
        else
        set_Value ("AD_Process_ID", Integer.valueOf(AD_Process_ID));
        
    }
    
    /** Get Process.
    @return Process or Report */
    public int getAD_Process_ID() 
    {
        return get_ValueAsInt("AD_Process_ID");
        
    }
    
    /** Set OS Task.
    @param AD_Task_ID Operation System Task */
    public void setAD_Task_ID (int AD_Task_ID)
    {
        if (AD_Task_ID <= 0) set_Value ("AD_Task_ID", null);
        else
        set_Value ("AD_Task_ID", Integer.valueOf(AD_Task_ID));
        
    }
    
    /** Get OS Task.
    @return Operation System Task */
    public int getAD_Task_ID() 
    {
        return get_ValueAsInt("AD_Task_ID");
        
    }
    
    /** Set Window.
    @param AD_Window_ID Data entry or display window */
    public void setAD_Window_ID (int AD_Window_ID)
    {
        if (AD_Window_ID <= 0) set_Value ("AD_Window_ID", null);
        else
        set_Value ("AD_Window_ID", Integer.valueOf(AD_Window_ID));
        
    }
    
    /** Get Window.
    @return Data entry or display window */
    public int getAD_Window_ID() 
    {
        return get_ValueAsInt("AD_Window_ID");
        
    }
    
    /** Set Workbench Window.
    @param AD_WorkbenchWindow_ID Workbench Window */
    public void setAD_WorkbenchWindow_ID (int AD_WorkbenchWindow_ID)
    {
        if (AD_WorkbenchWindow_ID < 1) throw new IllegalArgumentException ("AD_WorkbenchWindow_ID is mandatory.");
        set_ValueNoCheck ("AD_WorkbenchWindow_ID", Integer.valueOf(AD_WorkbenchWindow_ID));
        
    }
    
    /** Get Workbench Window.
    @return Workbench Window */
    public int getAD_WorkbenchWindow_ID() 
    {
        return get_ValueAsInt("AD_WorkbenchWindow_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_WorkbenchWindow_ID()));
        
    }
    
    /** Set Workbench.
    @param AD_Workbench_ID Collection of windows, reports */
    public void setAD_Workbench_ID (int AD_Workbench_ID)
    {
        if (AD_Workbench_ID < 1) throw new IllegalArgumentException ("AD_Workbench_ID is mandatory.");
        set_ValueNoCheck ("AD_Workbench_ID", Integer.valueOf(AD_Workbench_ID));
        
    }
    
    /** Get Workbench.
    @return Collection of windows, reports */
    public int getAD_Workbench_ID() 
    {
        return get_ValueAsInt("AD_Workbench_ID");
        
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
    
    /** Set Primary.
    @param IsPrimary Indicates if this is the primary budget */
    public void setIsPrimary (boolean IsPrimary)
    {
        set_Value ("IsPrimary", Boolean.valueOf(IsPrimary));
        
    }
    
    /** Get Primary.
    @return Indicates if this is the primary budget */
    public boolean isPrimary() 
    {
        return get_ValueAsBoolean("IsPrimary");
        
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
    
    
}
