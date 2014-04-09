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
/** Generated Model for MRP_Product_Audit
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: X_MRP_Product_Audit.java 9171 2010-08-05 17:51:36Z kvora $ */
public class X_MRP_Product_Audit extends PO
{
    /** Standard Constructor
    @param ctx context
    @param MRP_Product_Audit_ID id
    @param trx transaction
    */
    public X_MRP_Product_Audit (Ctx ctx, int MRP_Product_Audit_ID, Trx trx)
    {
        super (ctx, MRP_Product_Audit_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (MRP_Product_Audit_ID == 0)
        {
            setMRP_PlanRun_ID (0);
            setMRP_Product_Audit_ID (0);
            setM_Product_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_MRP_Product_Audit (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27563153968789L;
    /** Last Updated Timestamp 2010-08-05 10:17:32.0 */
    public static final long updatedMS = 1281028652000L;
    /** AD_Table_ID=2105 */
    public static final int Table_ID=2105;
    
    /** TableName=MRP_Product_Audit */
    public static final String Table_Name="MRP_Product_Audit";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
        else
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getMRP_PlanRun_ID()));
        
    }
    
    /** Set Product Audit.
    @param MRP_Product_Audit_ID Products included in the plan run */
    public void setMRP_Product_Audit_ID (int MRP_Product_Audit_ID)
    {
        if (MRP_Product_Audit_ID < 1) throw new IllegalArgumentException ("MRP_Product_Audit_ID is mandatory.");
        set_ValueNoCheck ("MRP_Product_Audit_ID", Integer.valueOf(MRP_Product_Audit_ID));
        
    }
    
    /** Get Product Audit.
    @return Products included in the plan run */
    public int getMRP_Product_Audit_ID() 
    {
        return get_ValueAsInt("MRP_Product_Audit_ID");
        
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
    
    /** Set Minimum Order Qty.
    @param Order_Min Minimum order quantity in UOM */
    public void setOrder_Min (java.math.BigDecimal Order_Min)
    {
        set_Value ("Order_Min", Order_Min);
        
    }
    
    /** Get Minimum Order Qty.
    @return Minimum order quantity in UOM */
    public java.math.BigDecimal getOrder_Min() 
    {
        return get_ValueAsBigDecimal("Order_Min");
        
    }
    
    
}
