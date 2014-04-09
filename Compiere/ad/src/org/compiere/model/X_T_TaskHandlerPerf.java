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
/** Generated Model for T_TaskHandlerPerf
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_T_TaskHandlerPerf.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_T_TaskHandlerPerf extends PO
{
    /** Standard Constructor
    @param ctx context
    @param T_TaskHandlerPerf_ID id
    @param trx transaction
    */
    public X_T_TaskHandlerPerf (Ctx ctx, int T_TaskHandlerPerf_ID, Trx trx)
    {
        super (ctx, T_TaskHandlerPerf_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (T_TaskHandlerPerf_ID == 0)
        {
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_T_TaskHandlerPerf (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27511671714789L;
    /** Last Updated Timestamp 2008-12-17 12:39:58.0 */
    public static final long updatedMS = 1229546398000L;
    /** AD_Table_ID=1075 */
    public static final int Table_ID=1075;
    
    /** TableName=T_TaskHandlerPerf */
    public static final String Table_Name="T_TaskHandlerPerf";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Process Instance.
    @param AD_PInstance_ID Instance of the process */
    public void setAD_PInstance_ID (int AD_PInstance_ID)
    {
        if (AD_PInstance_ID <= 0) set_Value ("AD_PInstance_ID", null);
        else
        set_Value ("AD_PInstance_ID", Integer.valueOf(AD_PInstance_ID));
        
    }
    
    /** Get Process Instance.
    @return Instance of the process */
    public int getAD_PInstance_ID() 
    {
        return get_ValueAsInt("AD_PInstance_ID");
        
    }
    
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
        else
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Lines Putaway.
    @param LinesPutaway Lines Putaway */
    public void setLinesPutaway (java.math.BigDecimal LinesPutaway)
    {
        set_Value ("LinesPutaway", LinesPutaway);
        
    }
    
    /** Get Lines Putaway.
    @return Lines Putaway */
    public java.math.BigDecimal getLinesPutaway() 
    {
        return get_ValueAsBigDecimal("LinesPutaway");
        
    }
    
    /** Set Lines Replenished.
    @param LinesReplenished Lines Replenished */
    public void setLinesReplenished (java.math.BigDecimal LinesReplenished)
    {
        set_Value ("LinesReplenished", LinesReplenished);
        
    }
    
    /** Get Lines Replenished.
    @return Lines Replenished */
    public java.math.BigDecimal getLinesReplenished() 
    {
        return get_ValueAsBigDecimal("LinesReplenished");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
        else
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Set Movement Date.
    @param MovementDate Date a product was moved in or out of inventory */
    public void setMovementDate (Timestamp MovementDate)
    {
        set_Value ("MovementDate", MovementDate);
        
    }
    
    /** Get Movement Date.
    @return Date a product was moved in or out of inventory */
    public Timestamp getMovementDate() 
    {
        return (Timestamp)get_Value("MovementDate");
        
    }
    
    /** Set Order Lines Picked.
    @param OrderLinesPicked Order Lines Picked */
    public void setOrderLinesPicked (java.math.BigDecimal OrderLinesPicked)
    {
        set_Value ("OrderLinesPicked", OrderLinesPicked);
        
    }
    
    /** Get Order Lines Picked.
    @return Order Lines Picked */
    public java.math.BigDecimal getOrderLinesPicked() 
    {
        return get_ValueAsBigDecimal("OrderLinesPicked");
        
    }
    
    /** Set Orders Picked.
    @param OrdersPicked Orders Picked */
    public void setOrdersPicked (java.math.BigDecimal OrdersPicked)
    {
        set_Value ("OrdersPicked", OrdersPicked);
        
    }
    
    /** Get Orders Picked.
    @return Orders Picked */
    public java.math.BigDecimal getOrdersPicked() 
    {
        return get_ValueAsBigDecimal("OrdersPicked");
        
    }
    
    /** Set Trolleys Picked.
    @param TrolleysPicked Trolleys Picked */
    public void setTrolleysPicked (java.math.BigDecimal TrolleysPicked)
    {
        set_Value ("TrolleysPicked", TrolleysPicked);
        
    }
    
    /** Get Trolleys Picked.
    @return Trolleys Picked */
    public java.math.BigDecimal getTrolleysPicked() 
    {
        return get_ValueAsBigDecimal("TrolleysPicked");
        
    }
    
    
}
