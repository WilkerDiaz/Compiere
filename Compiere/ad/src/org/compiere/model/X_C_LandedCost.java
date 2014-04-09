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
/** Generated Model for C_LandedCost
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_LandedCost.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_LandedCost extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_LandedCost_ID id
    @param trx transaction
    */
    public X_C_LandedCost (Ctx ctx, int C_LandedCost_ID, Trx trx)
    {
        super (ctx, C_LandedCost_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_LandedCost_ID == 0)
        {
            setC_InvoiceLine_ID (0);
            setC_LandedCost_ID (0);
            setLandedCostDistribution (null);	// Q
            setM_CostElement_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_LandedCost (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=759 */
    public static final int Table_ID=759;
    
    /** TableName=C_LandedCost */
    public static final String Table_Name="C_LandedCost";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Invoice Line.
    @param C_InvoiceLine_ID Invoice Detail Line */
    public void setC_InvoiceLine_ID (int C_InvoiceLine_ID)
    {
        if (C_InvoiceLine_ID < 1) throw new IllegalArgumentException ("C_InvoiceLine_ID is mandatory.");
        set_ValueNoCheck ("C_InvoiceLine_ID", Integer.valueOf(C_InvoiceLine_ID));
        
    }
    
    /** Get Invoice Line.
    @return Invoice Detail Line */
    public int getC_InvoiceLine_ID() 
    {
        return get_ValueAsInt("C_InvoiceLine_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_InvoiceLine_ID()));
        
    }
    
    /** Set Landed Cost.
    @param C_LandedCost_ID Landed cost to be allocated to material receipts */
    public void setC_LandedCost_ID (int C_LandedCost_ID)
    {
        if (C_LandedCost_ID < 1) throw new IllegalArgumentException ("C_LandedCost_ID is mandatory.");
        set_ValueNoCheck ("C_LandedCost_ID", Integer.valueOf(C_LandedCost_ID));
        
    }
    
    /** Get Landed Cost.
    @return Landed cost to be allocated to material receipts */
    public int getC_LandedCost_ID() 
    {
        return get_ValueAsInt("C_LandedCost_ID");
        
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
    
    /** Costs = C */
    public static final String LANDEDCOSTDISTRIBUTION_Costs = X_Ref_C_LandedCost_Distribution.COSTS.getValue();
    /** Line = L */
    public static final String LANDEDCOSTDISTRIBUTION_Line = X_Ref_C_LandedCost_Distribution.LINE.getValue();
    /** Quantity = Q */
    public static final String LANDEDCOSTDISTRIBUTION_Quantity = X_Ref_C_LandedCost_Distribution.QUANTITY.getValue();
    /** Volume = V */
    public static final String LANDEDCOSTDISTRIBUTION_Volume = X_Ref_C_LandedCost_Distribution.VOLUME.getValue();
    /** Weight = W */
    public static final String LANDEDCOSTDISTRIBUTION_Weight = X_Ref_C_LandedCost_Distribution.WEIGHT.getValue();
    /** Set Cost Distribution.
    @param LandedCostDistribution Landed Cost Distribution */
    public void setLandedCostDistribution (String LandedCostDistribution)
    {
        if (LandedCostDistribution == null) throw new IllegalArgumentException ("LandedCostDistribution is mandatory");
        if (!X_Ref_C_LandedCost_Distribution.isValid(LandedCostDistribution))
        throw new IllegalArgumentException ("LandedCostDistribution Invalid value - " + LandedCostDistribution + " - Reference_ID=339 - C - L - Q - V - W");
        set_Value ("LandedCostDistribution", LandedCostDistribution);
        
    }
    
    /** Get Cost Distribution.
    @return Landed Cost Distribution */
    public String getLandedCostDistribution() 
    {
        return (String)get_Value("LandedCostDistribution");
        
    }
    
    /** Set Cost Element.
    @param M_CostElement_ID Product Cost Element */
    public void setM_CostElement_ID (int M_CostElement_ID)
    {
        if (M_CostElement_ID < 1) throw new IllegalArgumentException ("M_CostElement_ID is mandatory.");
        set_Value ("M_CostElement_ID", Integer.valueOf(M_CostElement_ID));
        
    }
    
    /** Get Cost Element.
    @return Product Cost Element */
    public int getM_CostElement_ID() 
    {
        return get_ValueAsInt("M_CostElement_ID");
        
    }
    
    /** Set Shipment/Receipt Line.
    @param M_InOutLine_ID Line on Shipment or Receipt document */
    public void setM_InOutLine_ID (int M_InOutLine_ID)
    {
        if (M_InOutLine_ID <= 0) set_Value ("M_InOutLine_ID", null);
        else
        set_Value ("M_InOutLine_ID", Integer.valueOf(M_InOutLine_ID));
        
    }
    
    /** Get Shipment/Receipt Line.
    @return Line on Shipment or Receipt document */
    public int getM_InOutLine_ID() 
    {
        return get_ValueAsInt("M_InOutLine_ID");
        
    }
    
    /** Set Shipment/Receipt.
    @param M_InOut_ID Material Shipment Document */
    public void setM_InOut_ID (int M_InOut_ID)
    {
        if (M_InOut_ID <= 0) set_Value ("M_InOut_ID", null);
        else
        set_Value ("M_InOut_ID", Integer.valueOf(M_InOut_ID));
        
    }
    
    /** Get Shipment/Receipt.
    @return Material Shipment Document */
    public int getM_InOut_ID() 
    {
        return get_ValueAsInt("M_InOut_ID");
        
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
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    
}
