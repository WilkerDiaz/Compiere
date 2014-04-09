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
/** Generated Model for T_Wave
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_T_Wave.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_T_Wave extends PO
{
    /** Standard Constructor
    @param ctx context
    @param T_Wave_ID id
    @param trx transaction
    */
    public X_T_Wave (Ctx ctx, int T_Wave_ID, Trx trx)
    {
        super (ctx, T_Wave_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (T_Wave_ID == 0)
        {
            setSeqNo (0);
            setT_Wave_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_T_Wave (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27541768501789L;
    /** Last Updated Timestamp 2009-11-30 20:53:05.0 */
    public static final long updatedMS = 1259643185000L;
    /** AD_Table_ID=1067 */
    public static final int Table_ID=1067;
    
    /** TableName=T_Wave */
    public static final String Table_Name="T_Wave";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Order Line.
    @param C_OrderLine_ID Order Line */
    public void setC_OrderLine_ID (int C_OrderLine_ID)
    {
        if (C_OrderLine_ID <= 0) set_Value ("C_OrderLine_ID", null);
        else
        set_Value ("C_OrderLine_ID", Integer.valueOf(C_OrderLine_ID));
        
    }
    
    /** Get Order Line.
    @return Order Line */
    public int getC_OrderLine_ID() 
    {
        return get_ValueAsInt("C_OrderLine_ID");
        
    }
    
    /** Set Wave Line.
    @param C_WaveLine_ID Selected order lines for which there is sufficient onhand quantity in the warehouse */
    public void setC_WaveLine_ID (int C_WaveLine_ID)
    {
        if (C_WaveLine_ID <= 0) set_Value ("C_WaveLine_ID", null);
        else
        set_Value ("C_WaveLine_ID", Integer.valueOf(C_WaveLine_ID));
        
    }
    
    /** Get Wave Line.
    @return Selected order lines for which there is sufficient onhand quantity in the warehouse */
    public int getC_WaveLine_ID() 
    {
        return get_ValueAsInt("C_WaveLine_ID");
        
    }
    
    /** Set Work Order Component.
    @param M_WorkOrderComponent_ID Work Order Component */
    public void setM_WorkOrderComponent_ID (int M_WorkOrderComponent_ID)
    {
        if (M_WorkOrderComponent_ID <= 0) set_Value ("M_WorkOrderComponent_ID", null);
        else
        set_Value ("M_WorkOrderComponent_ID", Integer.valueOf(M_WorkOrderComponent_ID));
        
    }
    
    /** Get Work Order Component.
    @return Work Order Component */
    public int getM_WorkOrderComponent_ID() 
    {
        return get_ValueAsInt("M_WorkOrderComponent_ID");
        
    }
    
    /** Set Operation.
    @param M_WorkOrderOperation_ID Production routing operation on a work order */
    public void setM_WorkOrderOperation_ID (int M_WorkOrderOperation_ID)
    {
        if (M_WorkOrderOperation_ID <= 0) set_Value ("M_WorkOrderOperation_ID", null);
        else
        set_Value ("M_WorkOrderOperation_ID", Integer.valueOf(M_WorkOrderOperation_ID));
        
    }
    
    /** Get Operation.
    @return Production routing operation on a work order */
    public int getM_WorkOrderOperation_ID() 
    {
        return get_ValueAsInt("M_WorkOrderOperation_ID");
        
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
    
    /** Set Wave.
    @param T_Wave_ID Wave */
    public void setT_Wave_ID (int T_Wave_ID)
    {
        if (T_Wave_ID < 1) throw new IllegalArgumentException ("T_Wave_ID is mandatory.");
        set_ValueNoCheck ("T_Wave_ID", Integer.valueOf(T_Wave_ID));
        
    }
    
    /** Get Wave.
    @return Wave */
    public int getT_Wave_ID() 
    {
        return get_ValueAsInt("T_Wave_ID");
        
    }
    
    
}
