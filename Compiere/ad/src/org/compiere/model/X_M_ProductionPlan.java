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
/** Generated Model for M_ProductionPlan
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_ProductionPlan.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_ProductionPlan extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_ProductionPlan_ID id
    @param trx transaction
    */
    public X_M_ProductionPlan (Ctx ctx, int M_ProductionPlan_ID, Trx trx)
    {
        super (ctx, M_ProductionPlan_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_ProductionPlan_ID == 0)
        {
            setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM M_ProductionPlan WHERE M_Production_ID=@M_Production_ID@
            setM_Locator_ID (0);	// @M_Locator_ID@
            setM_Product_ID (0);
            setM_ProductionPlan_ID (0);
            setM_Production_ID (0);
            setProcessed (false);	// N
            setProductionQty (Env.ZERO);	// 1
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_ProductionPlan (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=385 */
    public static final int Table_ID=385;
    
    /** TableName=M_ProductionPlan */
    public static final String Table_Name="M_ProductionPlan";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getLine()));
        
    }
    
    /** Set Locator.
    @param M_Locator_ID Warehouse Locator */
    public void setM_Locator_ID (int M_Locator_ID)
    {
        if (M_Locator_ID < 1) throw new IllegalArgumentException ("M_Locator_ID is mandatory.");
        set_Value ("M_Locator_ID", Integer.valueOf(M_Locator_ID));
        
    }
    
    /** Get Locator.
    @return Warehouse Locator */
    public int getM_Locator_ID() 
    {
        return get_ValueAsInt("M_Locator_ID");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Production Plan.
    @param M_ProductionPlan_ID Plan for how a product is produced */
    public void setM_ProductionPlan_ID (int M_ProductionPlan_ID)
    {
        if (M_ProductionPlan_ID < 1) throw new IllegalArgumentException ("M_ProductionPlan_ID is mandatory.");
        set_ValueNoCheck ("M_ProductionPlan_ID", Integer.valueOf(M_ProductionPlan_ID));
        
    }
    
    /** Get Production Plan.
    @return Plan for how a product is produced */
    public int getM_ProductionPlan_ID() 
    {
        return get_ValueAsInt("M_ProductionPlan_ID");
        
    }
    
    /** Set Production.
    @param M_Production_ID Plan for producing a product */
    public void setM_Production_ID (int M_Production_ID)
    {
        if (M_Production_ID < 1) throw new IllegalArgumentException ("M_Production_ID is mandatory.");
        set_ValueNoCheck ("M_Production_ID", Integer.valueOf(M_Production_ID));
        
    }
    
    /** Get Production.
    @return Plan for producing a product */
    public int getM_Production_ID() 
    {
        return get_ValueAsInt("M_Production_ID");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Production Quantity.
    @param ProductionQty Quantity of products to produce */
    public void setProductionQty (java.math.BigDecimal ProductionQty)
    {
        if (ProductionQty == null) throw new IllegalArgumentException ("ProductionQty is mandatory.");
        set_Value ("ProductionQty", ProductionQty);
        
    }
    
    /** Get Production Quantity.
    @return Quantity of products to produce */
    public java.math.BigDecimal getProductionQty() 
    {
        return get_ValueAsBigDecimal("ProductionQty");
        
    }
    
    
}
