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
/** Generated Model for MRP_Inventory_Audit
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_MRP_Inventory_Audit.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_MRP_Inventory_Audit extends PO
{
    /** Standard Constructor
    @param ctx context
    @param MRP_Inventory_Audit_ID id
    @param trx transaction
    */
    public X_MRP_Inventory_Audit (Ctx ctx, int MRP_Inventory_Audit_ID, Trx trx)
    {
        super (ctx, MRP_Inventory_Audit_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (MRP_Inventory_Audit_ID == 0)
        {
            setMRP_PlanRun_ID (0);
            setM_Product_ID (0);
            setQtyOnHand (Env.ZERO);	// 0
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_MRP_Inventory_Audit (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27523134653789L;
    /** Last Updated Timestamp 2009-04-29 05:48:57.0 */
    public static final long updatedMS = 1241009337000L;
    /** AD_Table_ID=2104 */
    public static final int Table_ID=2104;
    
    /** TableName=MRP_Inventory_Audit */
    public static final String Table_Name="MRP_Inventory_Audit";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Plan Run.
    @param MRP_PlanRun_ID An execution of the plan */
    public void setMRP_PlanRun_ID (int MRP_PlanRun_ID)
    {
        if (MRP_PlanRun_ID < 1) throw new IllegalArgumentException ("MRP_PlanRun_ID is mandatory.");
        set_ValueNoCheck ("MRP_PlanRun_ID", Integer.valueOf(MRP_PlanRun_ID));
        
    }
    
    /** Get Plan Run.
    @return An execution of the plan */
    public int getMRP_PlanRun_ID() 
    {
        return get_ValueAsInt("MRP_PlanRun_ID");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
        set_ValueNoCheck ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_Product_ID()));
        
    }
    
    /** Set On Hand Quantity.
    @param QtyOnHand On Hand Quantity */
    public void setQtyOnHand (java.math.BigDecimal QtyOnHand)
    {
        if (QtyOnHand == null) throw new IllegalArgumentException ("QtyOnHand is mandatory.");
        set_ValueNoCheck ("QtyOnHand", QtyOnHand);
        
    }
    
    /** Get On Hand Quantity.
    @return On Hand Quantity */
    public java.math.BigDecimal getQtyOnHand() 
    {
        return get_ValueAsBigDecimal("QtyOnHand");
        
    }
    
    
}
