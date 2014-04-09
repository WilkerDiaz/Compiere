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
/** Generated Model for T_MRPPlanImplement
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_T_MRPPlanImplement.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_T_MRPPlanImplement extends PO
{
    /** Standard Constructor
    @param ctx context
    @param T_MRPPlanImplement_ID id
    @param trx transaction
    */
    public X_T_MRPPlanImplement (Ctx ctx, int T_MRPPlanImplement_ID, Trx trx)
    {
        super (ctx, T_MRPPlanImplement_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (T_MRPPlanImplement_ID == 0)
        {
            setT_MRPPlanImplement_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_T_MRPPlanImplement (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27520725477789L;
    /** Last Updated Timestamp 2009-04-01 07:36:01.0 */
    public static final long updatedMS = 1238600161000L;
    /** AD_Table_ID=2121 */
    public static final int Table_ID=2121;
    
    /** TableName=T_MRPPlanImplement */
    public static final String Table_Name="T_MRPPlanImplement";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Line No.
    @param Line Unique line for this document */
    public void setLine (java.math.BigDecimal Line)
    {
        set_Value ("Line", Line);
        
    }
    
    /** Get Line No.
    @return Unique line for this document */
    public java.math.BigDecimal getLine() 
    {
        return get_ValueAsBigDecimal("Line");
        
    }
    
    /** Set Planned Order.
    @param MRP_PlannedOrder_ID Recommended orders calculated by the plan engine */
    public void setMRP_PlannedOrder_ID (int MRP_PlannedOrder_ID)
    {
        if (MRP_PlannedOrder_ID <= 0) set_Value ("MRP_PlannedOrder_ID", null);
        else
        set_Value ("MRP_PlannedOrder_ID", Integer.valueOf(MRP_PlannedOrder_ID));
        
    }
    
    /** Get Planned Order.
    @return Recommended orders calculated by the plan engine */
    public int getMRP_PlannedOrder_ID() 
    {
        return get_ValueAsInt("MRP_PlannedOrder_ID");
        
    }
    
    /** Set T_MRPPlanImplement_ID.
    @param T_MRPPlanImplement_ID T_MRPPlanImplement_ID */
    public void setT_MRPPlanImplement_ID (int T_MRPPlanImplement_ID)
    {
        if (T_MRPPlanImplement_ID < 1) throw new IllegalArgumentException ("T_MRPPlanImplement_ID is mandatory.");
        set_ValueNoCheck ("T_MRPPlanImplement_ID", Integer.valueOf(T_MRPPlanImplement_ID));
        
    }
    
    /** Get T_MRPPlanImplement_ID.
    @return T_MRPPlanImplement_ID */
    public int getT_MRPPlanImplement_ID() 
    {
        return get_ValueAsInt("T_MRPPlanImplement_ID");
        
    }
    
    
}
