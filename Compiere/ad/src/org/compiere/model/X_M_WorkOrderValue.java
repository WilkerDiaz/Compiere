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
/** Generated Model for M_WorkOrderValue
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_WorkOrderValue.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_WorkOrderValue extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_WorkOrderValue_ID id
    @param trx transaction
    */
    public X_M_WorkOrderValue (Ctx ctx, int M_WorkOrderValue_ID, Trx trx)
    {
        super (ctx, M_WorkOrderValue_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_WorkOrderValue_ID == 0)
        {
            setC_AcctSchema_ID (0);
            setC_Currency_ID (0);
            setM_Product_ID (0);
            setM_WorkOrderValue_ID (0);
            setM_WorkOrder_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_WorkOrderValue (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27523117632789L;
    /** Last Updated Timestamp 2009-04-29 01:05:16.0 */
    public static final long updatedMS = 1240992316000L;
    /** AD_Table_ID=2116 */
    public static final int Table_ID=2116;
    
    /** TableName=M_WorkOrderValue */
    public static final String Table_Name="M_WorkOrderValue";
    
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
    
    /** Set Activity.
    @param C_Activity_ID Business Activity */
    public void setC_Activity_ID (int C_Activity_ID)
    {
        if (C_Activity_ID <= 0) set_Value ("C_Activity_ID", null);
        else
        set_Value ("C_Activity_ID", Integer.valueOf(C_Activity_ID));
        
    }
    
    /** Get Activity.
    @return Business Activity */
    public int getC_Activity_ID() 
    {
        return get_ValueAsInt("C_Activity_ID");
        
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
    
    /** Set Partner Location.
    @param C_BPartner_Location_ID Identifies the (ship to) address for this Business Partner */
    public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
    {
        if (C_BPartner_Location_ID <= 0) set_Value ("C_BPartner_Location_ID", null);
        else
        set_Value ("C_BPartner_Location_ID", Integer.valueOf(C_BPartner_Location_ID));
        
    }
    
    /** Get Partner Location.
    @return Identifies the (ship to) address for this Business Partner */
    public int getC_BPartner_Location_ID() 
    {
        return get_ValueAsInt("C_BPartner_Location_ID");
        
    }
    
    /** Set Campaign.
    @param C_Campaign_ID Marketing Campaign */
    public void setC_Campaign_ID (int C_Campaign_ID)
    {
        if (C_Campaign_ID <= 0) set_Value ("C_Campaign_ID", null);
        else
        set_Value ("C_Campaign_ID", Integer.valueOf(C_Campaign_ID));
        
    }
    
    /** Get Campaign.
    @return Marketing Campaign */
    public int getC_Campaign_ID() 
    {
        return get_ValueAsInt("C_Campaign_ID");
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID < 1) throw new IllegalArgumentException ("C_Currency_ID is mandatory.");
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set Project.
    @param C_Project_ID Financial Project */
    public void setC_Project_ID (int C_Project_ID)
    {
        if (C_Project_ID <= 0) set_Value ("C_Project_ID", null);
        else
        set_Value ("C_Project_ID", Integer.valueOf(C_Project_ID));
        
    }
    
    /** Get Project.
    @return Financial Project */
    public int getC_Project_ID() 
    {
        return get_ValueAsInt("C_Project_ID");
        
    }
    
    /** Set BOM.
    @param M_BOM_ID Bill of Materials */
    public void setM_BOM_ID (int M_BOM_ID)
    {
        if (M_BOM_ID <= 0) set_Value ("M_BOM_ID", null);
        else
        set_Value ("M_BOM_ID", Integer.valueOf(M_BOM_ID));
        
    }
    
    /** Get BOM.
    @return Bill of Materials */
    public int getM_BOM_ID() 
    {
        return get_ValueAsInt("M_BOM_ID");
        
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
    
    /** Set Routing.
    @param M_Routing_ID Routing for an assembly */
    public void setM_Routing_ID (int M_Routing_ID)
    {
        if (M_Routing_ID <= 0) set_Value ("M_Routing_ID", null);
        else
        set_Value ("M_Routing_ID", Integer.valueOf(M_Routing_ID));
        
    }
    
    /** Get Routing.
    @return Routing for an assembly */
    public int getM_Routing_ID() 
    {
        return get_ValueAsInt("M_Routing_ID");
        
    }
    
    /** Set Work Order Value Identifier.
    @param M_WorkOrderValue_ID Work Order Value Identifier */
    public void setM_WorkOrderValue_ID (int M_WorkOrderValue_ID)
    {
        if (M_WorkOrderValue_ID < 1) throw new IllegalArgumentException ("M_WorkOrderValue_ID is mandatory.");
        set_ValueNoCheck ("M_WorkOrderValue_ID", Integer.valueOf(M_WorkOrderValue_ID));
        
    }
    
    /** Get Work Order Value Identifier.
    @return Work Order Value Identifier */
    public int getM_WorkOrderValue_ID() 
    {
        return get_ValueAsInt("M_WorkOrderValue_ID");
        
    }
    
    /** Set Work Order.
    @param M_WorkOrder_ID Work Order */
    public void setM_WorkOrder_ID (int M_WorkOrder_ID)
    {
        if (M_WorkOrder_ID < 1) throw new IllegalArgumentException ("M_WorkOrder_ID is mandatory.");
        set_Value ("M_WorkOrder_ID", Integer.valueOf(M_WorkOrder_ID));
        
    }
    
    /** Get Work Order.
    @return Work Order */
    public int getM_WorkOrder_ID() 
    {
        return get_ValueAsInt("M_WorkOrder_ID");
        
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
    
    
}
