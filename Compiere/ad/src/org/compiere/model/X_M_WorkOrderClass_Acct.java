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
/** Generated Model for M_WorkOrderClass_Acct
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_WorkOrderClass_Acct.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_WorkOrderClass_Acct extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_WorkOrderClass_Acct_ID id
    @param trx transaction
    */
    public X_M_WorkOrderClass_Acct (Ctx ctx, int M_WorkOrderClass_Acct_ID, Trx trx)
    {
        super (ctx, M_WorkOrderClass_Acct_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_WorkOrderClass_Acct_ID == 0)
        {
            setC_AcctSchema_ID (0);
            setM_WorkOrderClass_ID (0);
            setWO_MaterialOverhdVariance_Acct (0);
            setWO_MaterialOverhd_Acct (0);
            setWO_MaterialVariance_Acct (0);
            setWO_Material_Acct (0);
            setWO_OverhdVariance_Acct (0);
            setWO_ResourceVariance_Acct (0);
            setWO_Resource_Acct (0);
            setWO_Scrap_Acct (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_WorkOrderClass_Acct (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27519966119789L;
    /** Last Updated Timestamp 2009-03-23 12:40:03.0 */
    public static final long updatedMS = 1237840803000L;
    /** AD_Table_ID=1057 */
    public static final int Table_ID=1057;
    
    /** TableName=M_WorkOrderClass_Acct */
    public static final String Table_Name="M_WorkOrderClass_Acct";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Accounting Schema.
    @param C_AcctSchema_ID Rules for accounting */
    public void setC_AcctSchema_ID (int C_AcctSchema_ID)
    {
        if (C_AcctSchema_ID < 1) throw new IllegalArgumentException ("C_AcctSchema_ID is mandatory.");
        set_ValueNoCheck ("C_AcctSchema_ID", Integer.valueOf(C_AcctSchema_ID));
        
    }
    
    /** Get Accounting Schema.
    @return Rules for accounting */
    public int getC_AcctSchema_ID() 
    {
        return get_ValueAsInt("C_AcctSchema_ID");
        
    }
    
    /** Set Work Order Class.
    @param M_WorkOrderClass_ID Indicates the document types and accounts to be used for a work order */
    public void setM_WorkOrderClass_ID (int M_WorkOrderClass_ID)
    {
        if (M_WorkOrderClass_ID < 1) throw new IllegalArgumentException ("M_WorkOrderClass_ID is mandatory.");
        set_ValueNoCheck ("M_WorkOrderClass_ID", Integer.valueOf(M_WorkOrderClass_ID));
        
    }
    
    /** Get Work Order Class.
    @return Indicates the document types and accounts to be used for a work order */
    public int getM_WorkOrderClass_ID() 
    {
        return get_ValueAsInt("M_WorkOrderClass_ID");
        
    }
    
    /** Set Work Order Material Overhead Variance.
    @param WO_MaterialOverhdVariance_Acct Work Order Material Overhead Variance Account */
    public void setWO_MaterialOverhdVariance_Acct (int WO_MaterialOverhdVariance_Acct)
    {
        set_Value ("WO_MaterialOverhdVariance_Acct", Integer.valueOf(WO_MaterialOverhdVariance_Acct));
        
    }
    
    /** Get Work Order Material Overhead Variance.
    @return Work Order Material Overhead Variance Account */
    public int getWO_MaterialOverhdVariance_Acct() 
    {
        return get_ValueAsInt("WO_MaterialOverhdVariance_Acct");
        
    }
    
    /** Set Work Order Material Overhead.
    @param WO_MaterialOverhd_Acct Work Order Material Overhead Account */
    public void setWO_MaterialOverhd_Acct (int WO_MaterialOverhd_Acct)
    {
        set_Value ("WO_MaterialOverhd_Acct", Integer.valueOf(WO_MaterialOverhd_Acct));
        
    }
    
    /** Get Work Order Material Overhead.
    @return Work Order Material Overhead Account */
    public int getWO_MaterialOverhd_Acct() 
    {
        return get_ValueAsInt("WO_MaterialOverhd_Acct");
        
    }
    
    /** Set Work Order Material Variance.
    @param WO_MaterialVariance_Acct Work Order Material Variance Account */
    public void setWO_MaterialVariance_Acct (int WO_MaterialVariance_Acct)
    {
        set_Value ("WO_MaterialVariance_Acct", Integer.valueOf(WO_MaterialVariance_Acct));
        
    }
    
    /** Get Work Order Material Variance.
    @return Work Order Material Variance Account */
    public int getWO_MaterialVariance_Acct() 
    {
        return get_ValueAsInt("WO_MaterialVariance_Acct");
        
    }
    
    /** Set Work Order Material.
    @param WO_Material_Acct Work Order Material Account */
    public void setWO_Material_Acct (int WO_Material_Acct)
    {
        set_Value ("WO_Material_Acct", Integer.valueOf(WO_Material_Acct));
        
    }
    
    /** Get Work Order Material.
    @return Work Order Material Account */
    public int getWO_Material_Acct() 
    {
        return get_ValueAsInt("WO_Material_Acct");
        
    }
    
    /** Set Work Order Overhead Variance.
    @param WO_OverhdVariance_Acct Work Order Overhead Variance Account */
    public void setWO_OverhdVariance_Acct (int WO_OverhdVariance_Acct)
    {
        set_Value ("WO_OverhdVariance_Acct", Integer.valueOf(WO_OverhdVariance_Acct));
        
    }
    
    /** Get Work Order Overhead Variance.
    @return Work Order Overhead Variance Account */
    public int getWO_OverhdVariance_Acct() 
    {
        return get_ValueAsInt("WO_OverhdVariance_Acct");
        
    }
    
    /** Set Work Order Resource Variance.
    @param WO_ResourceVariance_Acct Work Order Resource Variance Account */
    public void setWO_ResourceVariance_Acct (int WO_ResourceVariance_Acct)
    {
        set_Value ("WO_ResourceVariance_Acct", Integer.valueOf(WO_ResourceVariance_Acct));
        
    }
    
    /** Get Work Order Resource Variance.
    @return Work Order Resource Variance Account */
    public int getWO_ResourceVariance_Acct() 
    {
        return get_ValueAsInt("WO_ResourceVariance_Acct");
        
    }
    
    /** Set Work Order Resource.
    @param WO_Resource_Acct Work Order Resource Account */
    public void setWO_Resource_Acct (int WO_Resource_Acct)
    {
        set_Value ("WO_Resource_Acct", Integer.valueOf(WO_Resource_Acct));
        
    }
    
    /** Get Work Order Resource.
    @return Work Order Resource Account */
    public int getWO_Resource_Acct() 
    {
        return get_ValueAsInt("WO_Resource_Acct");
        
    }
    
    /** Set Work Order Scrap.
    @param WO_Scrap_Acct Work Order Scrap Account */
    public void setWO_Scrap_Acct (int WO_Scrap_Acct)
    {
        set_Value ("WO_Scrap_Acct", Integer.valueOf(WO_Scrap_Acct));
        
    }
    
    /** Get Work Order Scrap.
    @return Work Order Scrap Account */
    public int getWO_Scrap_Acct() 
    {
        return get_ValueAsInt("WO_Scrap_Acct");
        
    }
    
    
}
