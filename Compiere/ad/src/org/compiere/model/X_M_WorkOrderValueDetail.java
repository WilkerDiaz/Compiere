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
/** Generated Model for M_WorkOrderValueDetail
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_WorkOrderValueDetail.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_WorkOrderValueDetail extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_WorkOrderValueDetail_ID id
    @param trx transaction
    */
    public X_M_WorkOrderValueDetail (Ctx ctx, int M_WorkOrderValueDetail_ID, Trx trx)
    {
        super (ctx, M_WorkOrderValueDetail_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_WorkOrderValueDetail_ID == 0)
        {
            setC_AcctSchema_ID (0);
            setC_Period_ID (0);
            setC_UOM_ID (0);
            setM_CostElement_ID (0);
            setM_Product_ID (0);
            setM_WorkOrderValueDetail_ID (0);
            setM_WorkOrderValue_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_WorkOrderValueDetail (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27523117691789L;
    /** Last Updated Timestamp 2009-04-29 01:06:15.0 */
    public static final long updatedMS = 1240992375000L;
    /** AD_Table_ID=2117 */
    public static final int Table_ID=2117;
    
    /** TableName=M_WorkOrderValueDetail */
    public static final String Table_Name="M_WorkOrderValueDetail";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Trx Organization.
    @param AD_OrgTrx_ID Performing or initiating organization */
    public void setAD_OrgTrx_ID (int AD_OrgTrx_ID)
    {
        if (AD_OrgTrx_ID <= 0) set_Value ("AD_OrgTrx_ID", null);
        else
        set_Value ("AD_OrgTrx_ID", Integer.valueOf(AD_OrgTrx_ID));
        
    }
    
    /** Get Trx Organization.
    @return Performing or initiating organization */
    public int getAD_OrgTrx_ID() 
    {
        return get_ValueAsInt("AD_OrgTrx_ID");
        
    }
    
    /** Set Accounting Schema.
    @param C_AcctSchema_ID Rules for accounting */
    public void setC_AcctSchema_ID (int C_AcctSchema_ID)
    {
        if (C_AcctSchema_ID < 1) throw new IllegalArgumentException ("C_AcctSchema_ID is mandatory.");
        set_Value ("C_AcctSchema_ID", Integer.valueOf(C_AcctSchema_ID));
        
    }
    
    /** Get Accounting Schema.
    @return Rules for accounting */
    public int getC_AcctSchema_ID() 
    {
        return get_ValueAsInt("C_AcctSchema_ID");
        
    }
    
    /** Set Period.
    @param C_Period_ID Period of the Calendar */
    public void setC_Period_ID (int C_Period_ID)
    {
        if (C_Period_ID < 1) throw new IllegalArgumentException ("C_Period_ID is mandatory.");
        set_Value ("C_Period_ID", Integer.valueOf(C_Period_ID));
        
    }
    
    /** Get Period.
    @return Period of the Calendar */
    public int getC_Period_ID() 
    {
        return get_ValueAsInt("C_Period_ID");
        
    }
    
    /** Set UOM.
    @param C_UOM_ID Unit of Measure */
    public void setC_UOM_ID (int C_UOM_ID)
    {
        if (C_UOM_ID < 1) throw new IllegalArgumentException ("C_UOM_ID is mandatory.");
        set_Value ("C_UOM_ID", Integer.valueOf(C_UOM_ID));
        
    }
    
    /** Get UOM.
    @return Unit of Measure */
    public int getC_UOM_ID() 
    {
        return get_ValueAsInt("C_UOM_ID");
        
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
    
    /** Set Work Order Transaction.
    @param M_WorkOrderTransaction_ID Work Order Transaction */
    public void setM_WorkOrderTransaction_ID (int M_WorkOrderTransaction_ID)
    {
        if (M_WorkOrderTransaction_ID <= 0) set_Value ("M_WorkOrderTransaction_ID", null);
        else
        set_Value ("M_WorkOrderTransaction_ID", Integer.valueOf(M_WorkOrderTransaction_ID));
        
    }
    
    /** Get Work Order Transaction.
    @return Work Order Transaction */
    public int getM_WorkOrderTransaction_ID() 
    {
        return get_ValueAsInt("M_WorkOrderTransaction_ID");
        
    }
    
    /** Set M_WorkOrderValueDetail_ID.
    @param M_WorkOrderValueDetail_ID Work Order Value Detail Identifier */
    public void setM_WorkOrderValueDetail_ID (int M_WorkOrderValueDetail_ID)
    {
        if (M_WorkOrderValueDetail_ID < 1) throw new IllegalArgumentException ("M_WorkOrderValueDetail_ID is mandatory.");
        set_ValueNoCheck ("M_WorkOrderValueDetail_ID", Integer.valueOf(M_WorkOrderValueDetail_ID));
        
    }
    
    /** Get M_WorkOrderValueDetail_ID.
    @return Work Order Value Detail Identifier */
    public int getM_WorkOrderValueDetail_ID() 
    {
        return get_ValueAsInt("M_WorkOrderValueDetail_ID");
        
    }
    
    /** Set Work Order Value Identifier.
    @param M_WorkOrderValue_ID Work Order Value Identifier */
    public void setM_WorkOrderValue_ID (int M_WorkOrderValue_ID)
    {
        if (M_WorkOrderValue_ID < 1) throw new IllegalArgumentException ("M_WorkOrderValue_ID is mandatory.");
        set_Value ("M_WorkOrderValue_ID", Integer.valueOf(M_WorkOrderValue_ID));
        
    }
    
    /** Get Work Order Value Identifier.
    @return Work Order Value Identifier */
    public int getM_WorkOrderValue_ID() 
    {
        return get_ValueAsInt("M_WorkOrderValue_ID");
        
    }
    
    /** Set Material In.
    @param MaterialIn Value of material coming into the work order */
    public void setMaterialIn (java.math.BigDecimal MaterialIn)
    {
        set_Value ("MaterialIn", MaterialIn);
        
    }
    
    /** Get Material In.
    @return Value of material coming into the work order */
    public java.math.BigDecimal getMaterialIn() 
    {
        return get_ValueAsBigDecimal("MaterialIn");
        
    }
    
    /** Set Material Out.
    @param MaterialOut Value of material going out of the work order */
    public void setMaterialOut (java.math.BigDecimal MaterialOut)
    {
        set_Value ("MaterialOut", MaterialOut);
        
    }
    
    /** Get Material Out.
    @return Value of material going out of the work order */
    public java.math.BigDecimal getMaterialOut() 
    {
        return get_ValueAsBigDecimal("MaterialOut");
        
    }
    
    /** Set Material Overhead In.
    @param MaterialOverhdIn Value of material overhead coming into the work order */
    public void setMaterialOverhdIn (java.math.BigDecimal MaterialOverhdIn)
    {
        set_Value ("MaterialOverhdIn", MaterialOverhdIn);
        
    }
    
    /** Get Material Overhead In.
    @return Value of material overhead coming into the work order */
    public java.math.BigDecimal getMaterialOverhdIn() 
    {
        return get_ValueAsBigDecimal("MaterialOverhdIn");
        
    }
    
    /** Set Material Overhead Out.
    @param MaterialOverhdOut Value of material overhead going out of the work order */
    public void setMaterialOverhdOut (java.math.BigDecimal MaterialOverhdOut)
    {
        set_Value ("MaterialOverhdOut", MaterialOverhdOut);
        
    }
    
    /** Get Material Overhead Out.
    @return Value of material overhead going out of the work order */
    public java.math.BigDecimal getMaterialOverhdOut() 
    {
        return get_ValueAsBigDecimal("MaterialOverhdOut");
        
    }
    
    /** Set Material Overhead Variance.
    @param MaterialOverhdVariance Variance in Material Overhead for the work order */
    public void setMaterialOverhdVariance (java.math.BigDecimal MaterialOverhdVariance)
    {
        set_Value ("MaterialOverhdVariance", MaterialOverhdVariance);
        
    }
    
    /** Get Material Overhead Variance.
    @return Variance in Material Overhead for the work order */
    public java.math.BigDecimal getMaterialOverhdVariance() 
    {
        return get_ValueAsBigDecimal("MaterialOverhdVariance");
        
    }
    
    /** Set Material Variance.
    @param MaterialVariance Variance of material in the work order */
    public void setMaterialVariance (java.math.BigDecimal MaterialVariance)
    {
        set_Value ("MaterialVariance", MaterialVariance);
        
    }
    
    /** Get Material Variance.
    @return Variance of material in the work order */
    public java.math.BigDecimal getMaterialVariance() 
    {
        return get_ValueAsBigDecimal("MaterialVariance");
        
    }
    
    /** Set Overhead In.
    @param OverhdIn Value of Overhead coming into the work order */
    public void setOverhdIn (java.math.BigDecimal OverhdIn)
    {
        set_Value ("OverhdIn", OverhdIn);
        
    }
    
    /** Get Overhead In.
    @return Value of Overhead coming into the work order */
    public java.math.BigDecimal getOverhdIn() 
    {
        return get_ValueAsBigDecimal("OverhdIn");
        
    }
    
    /** Set Overhead Out.
    @param OverhdOut Value of Overhead going out of the work order */
    public void setOverhdOut (java.math.BigDecimal OverhdOut)
    {
        set_Value ("OverhdOut", OverhdOut);
        
    }
    
    /** Get Overhead Out.
    @return Value of Overhead going out of the work order */
    public java.math.BigDecimal getOverhdOut() 
    {
        return get_ValueAsBigDecimal("OverhdOut");
        
    }
    
    /** Set Overhead Variance.
    @param OverhdVariance Overhead Variance in the work order */
    public void setOverhdVariance (java.math.BigDecimal OverhdVariance)
    {
        set_Value ("OverhdVariance", OverhdVariance);
        
    }
    
    /** Get Overhead Variance.
    @return Overhead Variance in the work order */
    public java.math.BigDecimal getOverhdVariance() 
    {
        return get_ValueAsBigDecimal("OverhdVariance");
        
    }
    
    /** Set Resource In.
    @param ResourceIn Value of resource coming into the work order */
    public void setResourceIn (java.math.BigDecimal ResourceIn)
    {
        set_Value ("ResourceIn", ResourceIn);
        
    }
    
    /** Get Resource In.
    @return Value of resource coming into the work order */
    public java.math.BigDecimal getResourceIn() 
    {
        return get_ValueAsBigDecimal("ResourceIn");
        
    }
    
    /** Set Resource Out.
    @param ResourceOut Value of resource going out of the work order */
    public void setResourceOut (java.math.BigDecimal ResourceOut)
    {
        set_Value ("ResourceOut", ResourceOut);
        
    }
    
    /** Get Resource Out.
    @return Value of resource going out of the work order */
    public java.math.BigDecimal getResourceOut() 
    {
        return get_ValueAsBigDecimal("ResourceOut");
        
    }
    
    /** Set Resource Variance.
    @param ResourceVariance Variance in the resource value in work order */
    public void setResourceVariance (java.math.BigDecimal ResourceVariance)
    {
        set_Value ("ResourceVariance", ResourceVariance);
        
    }
    
    /** Get Resource Variance.
    @return Variance in the resource value in work order */
    public java.math.BigDecimal getResourceVariance() 
    {
        return get_ValueAsBigDecimal("ResourceVariance");
        
    }
    
    /** Set Scrap Value.
    @param ScrapValue Scrap value in the work order */
    public void setScrapValue (java.math.BigDecimal ScrapValue)
    {
        set_Value ("ScrapValue", ScrapValue);
        
    }
    
    /** Get Scrap Value.
    @return Scrap value in the work order */
    public java.math.BigDecimal getScrapValue() 
    {
        return get_ValueAsBigDecimal("ScrapValue");
        
    }
    
    /** Set User List 1.
    @param User1_ID User defined list element #1 */
    public void setUser1_ID (int User1_ID)
    {
        if (User1_ID <= 0) set_Value ("User1_ID", null);
        else
        set_Value ("User1_ID", Integer.valueOf(User1_ID));
        
    }
    
    /** Get User List 1.
    @return User defined list element #1 */
    public int getUser1_ID() 
    {
        return get_ValueAsInt("User1_ID");
        
    }
    
    /** Set User List 2.
    @param User2_ID User defined list element #2 */
    public void setUser2_ID (int User2_ID)
    {
        if (User2_ID <= 0) set_Value ("User2_ID", null);
        else
        set_Value ("User2_ID", Integer.valueOf(User2_ID));
        
    }
    
    /** Get User List 2.
    @return User defined list element #2 */
    public int getUser2_ID() 
    {
        return get_ValueAsInt("User2_ID");
        
    }
    
    
}
