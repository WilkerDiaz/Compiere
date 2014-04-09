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
/** Generated Model for T_WorkOrderOperation
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_T_WorkOrderOperation.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_T_WorkOrderOperation extends PO
{
    /** Standard Constructor
    @param ctx context
    @param T_WorkOrderOperation_ID id
    @param trx transaction
    */
    public X_T_WorkOrderOperation (Ctx ctx, int T_WorkOrderOperation_ID, Trx trx)
    {
        super (ctx, T_WorkOrderOperation_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (T_WorkOrderOperation_ID == 0)
        {
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_T_WorkOrderOperation (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518323254789L;
    /** Last Updated Timestamp 2009-03-04 12:18:58.0 */
    public static final long updatedMS = 1236197938000L;
    /** AD_Table_ID=2101 */
    public static final int Table_ID=2101;
    
    /** TableName=T_WorkOrderOperation */
    public static final String Table_Name="T_WorkOrderOperation";
    
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
    public void setLine (int Line)
    {
        set_Value ("Line", Integer.valueOf(Line));
        
    }
    
    /** Get Line No.
    @return Unique line for this document */
    public int getLine() 
    {
        return get_ValueAsInt("Line");
        
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
    
    /** Set Work Order Operation.
    @param T_WorkOrderOperation_ID Work Order Operation */
    public void setT_WorkOrderOperation_ID (int T_WorkOrderOperation_ID)
    {
        if (T_WorkOrderOperation_ID <= 0) set_Value ("T_WorkOrderOperation_ID", null);
        else
        set_Value ("T_WorkOrderOperation_ID", Integer.valueOf(T_WorkOrderOperation_ID));
        
    }
    
    /** Get Work Order Operation.
    @return Work Order Operation */
    public int getT_WorkOrderOperation_ID() 
    {
        return get_ValueAsInt("T_WorkOrderOperation_ID");
        
    }
    
    
}
