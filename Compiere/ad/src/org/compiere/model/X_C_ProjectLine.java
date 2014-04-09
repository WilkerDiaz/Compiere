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
/** Generated Model for C_ProjectLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_ProjectLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_ProjectLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_ProjectLine_ID id
    @param trx transaction
    */
    public X_C_ProjectLine (Ctx ctx, int C_ProjectLine_ID, Trx trx)
    {
        super (ctx, C_ProjectLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_ProjectLine_ID == 0)
        {
            setC_ProjectLine_ID (0);
            setC_Project_ID (0);
            setInvoicedAmt (Env.ZERO);
            setInvoicedQty (Env.ZERO);	// 0
            setIsPrinted (true);	// Y
            setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM C_ProjectLine WHERE C_Project_ID=@C_Project_ID@
            setPlannedAmt (Env.ZERO);
            setPlannedPrice (Env.ZERO);
            setPlannedQty (Env.ZERO);	// 1
            setProcessed (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_ProjectLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27529719185789L;
    /** Last Updated Timestamp 2009-07-14 10:51:09.0 */
    public static final long updatedMS = 1247593869000L;
    /** AD_Table_ID=434 */
    public static final int Table_ID=434;
    
    /** TableName=C_ProjectLine */
    public static final String Table_Name="C_ProjectLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Purchase Order.
    @param C_OrderPO_ID Purchase Order */
    public void setC_OrderPO_ID (int C_OrderPO_ID)
    {
        if (C_OrderPO_ID <= 0) set_ValueNoCheck ("C_OrderPO_ID", null);
        else
        set_ValueNoCheck ("C_OrderPO_ID", Integer.valueOf(C_OrderPO_ID));
        
    }
    
    /** Get Purchase Order.
    @return Purchase Order */
    public int getC_OrderPO_ID() 
    {
        return get_ValueAsInt("C_OrderPO_ID");
        
    }
    
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID <= 0) set_ValueNoCheck ("C_Order_ID", null);
        else
        set_ValueNoCheck ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** Set Project Issue.
    @param C_ProjectIssue_ID Project Issues (Material, Labor) */
    public void setC_ProjectIssue_ID (int C_ProjectIssue_ID)
    {
        if (C_ProjectIssue_ID <= 0) set_ValueNoCheck ("C_ProjectIssue_ID", null);
        else
        set_ValueNoCheck ("C_ProjectIssue_ID", Integer.valueOf(C_ProjectIssue_ID));
        
    }
    
    /** Get Project Issue.
    @return Project Issues (Material, Labor) */
    public int getC_ProjectIssue_ID() 
    {
        return get_ValueAsInt("C_ProjectIssue_ID");
        
    }
    
    /** Set Project Line.
    @param C_ProjectLine_ID Task or step in a project */
    public void setC_ProjectLine_ID (int C_ProjectLine_ID)
    {
        if (C_ProjectLine_ID < 1) throw new IllegalArgumentException ("C_ProjectLine_ID is mandatory.");
        set_ValueNoCheck ("C_ProjectLine_ID", Integer.valueOf(C_ProjectLine_ID));
        
    }
    
    /** Get Project Line.
    @return Task or step in a project */
    public int getC_ProjectLine_ID() 
    {
        return get_ValueAsInt("C_ProjectLine_ID");
        
    }
    
    /** Set Project Phase.
    @param C_ProjectPhase_ID Phase of a Project */
    public void setC_ProjectPhase_ID (int C_ProjectPhase_ID)
    {
        if (C_ProjectPhase_ID <= 0) set_Value ("C_ProjectPhase_ID", null);
        else
        set_Value ("C_ProjectPhase_ID", Integer.valueOf(C_ProjectPhase_ID));
        
    }
    
    /** Get Project Phase.
    @return Phase of a Project */
    public int getC_ProjectPhase_ID() 
    {
        return get_ValueAsInt("C_ProjectPhase_ID");
        
    }
    
    /** Set Project Task.
    @param C_ProjectTask_ID Actual Project Task in a Phase */
    public void setC_ProjectTask_ID (int C_ProjectTask_ID)
    {
        if (C_ProjectTask_ID <= 0) set_Value ("C_ProjectTask_ID", null);
        else
        set_Value ("C_ProjectTask_ID", Integer.valueOf(C_ProjectTask_ID));
        
    }
    
    /** Get Project Task.
    @return Actual Project Task in a Phase */
    public int getC_ProjectTask_ID() 
    {
        return get_ValueAsInt("C_ProjectTask_ID");
        
    }
    
    /** Set Project.
    @param C_Project_ID Financial Project */
    public void setC_Project_ID (int C_Project_ID)
    {
        if (C_Project_ID < 1) throw new IllegalArgumentException ("C_Project_ID is mandatory.");
        set_ValueNoCheck ("C_Project_ID", Integer.valueOf(C_Project_ID));
        
    }
    
    /** Get Project.
    @return Financial Project */
    public int getC_Project_ID() 
    {
        return get_ValueAsInt("C_Project_ID");
        
    }
    
    /** Set Committed Amount.
    @param CommittedAmt The (legal) commitment amount */
    public void setCommittedAmt (java.math.BigDecimal CommittedAmt)
    {
        set_Value ("CommittedAmt", CommittedAmt);
        
    }
    
    /** Get Committed Amount.
    @return The (legal) commitment amount */
    public java.math.BigDecimal getCommittedAmt() 
    {
        return get_ValueAsBigDecimal("CommittedAmt");
        
    }
    
    /** Set Committed Quantity.
    @param CommittedQty The (legal) commitment Quantity */
    public void setCommittedQty (java.math.BigDecimal CommittedQty)
    {
        set_Value ("CommittedQty", CommittedQty);
        
    }
    
    /** Get Committed Quantity.
    @return The (legal) commitment Quantity */
    public java.math.BigDecimal getCommittedQty() 
    {
        return get_ValueAsBigDecimal("CommittedQty");
        
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
    
    /** Set Discount %.
    @param Discount Discount in percent */
    public void setDiscount (java.math.BigDecimal Discount)
    {
        set_Value ("Discount", Discount);
        
    }
    
    /** Get Discount %.
    @return Discount in percent */
    public java.math.BigDecimal getDiscount() 
    {
        return get_ValueAsBigDecimal("Discount");
        
    }
    
    /** Set Pricing.
    @param DoPricing Pricing */
    public void setDoPricing (String DoPricing)
    {
        set_Value ("DoPricing", DoPricing);
        
    }
    
    /** Get Pricing.
    @return Pricing */
    public String getDoPricing() 
    {
        return (String)get_Value("DoPricing");
        
    }
    
    /** Set Invoiced Amount.
    @param InvoicedAmt The amount invoiced */
    public void setInvoicedAmt (java.math.BigDecimal InvoicedAmt)
    {
        if (InvoicedAmt == null) throw new IllegalArgumentException ("InvoicedAmt is mandatory.");
        set_Value ("InvoicedAmt", InvoicedAmt);
        
    }
    
    /** Get Invoiced Amount.
    @return The amount invoiced */
    public java.math.BigDecimal getInvoicedAmt() 
    {
        return get_ValueAsBigDecimal("InvoicedAmt");
        
    }
    
    /** Set Quantity Invoiced.
    @param InvoicedQty The quantity invoiced */
    public void setInvoicedQty (java.math.BigDecimal InvoicedQty)
    {
        if (InvoicedQty == null) throw new IllegalArgumentException ("InvoicedQty is mandatory.");
        set_Value ("InvoicedQty", InvoicedQty);
        
    }
    
    /** Get Quantity Invoiced.
    @return The quantity invoiced */
    public java.math.BigDecimal getInvoicedQty() 
    {
        return get_ValueAsBigDecimal("InvoicedQty");
        
    }
    
    /** Set Printed.
    @param IsPrinted Indicates if this document / line is printed */
    public void setIsPrinted (boolean IsPrinted)
    {
        set_Value ("IsPrinted", Boolean.valueOf(IsPrinted));
        
    }
    
    /** Get Printed.
    @return Indicates if this document / line is printed */
    public boolean isPrinted() 
    {
        return get_ValueAsBoolean("IsPrinted");
        
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
    
    /** Set Product Category.
    @param M_Product_Category_ID Category of a Product */
    public void setM_Product_Category_ID (int M_Product_Category_ID)
    {
        if (M_Product_Category_ID <= 0) set_Value ("M_Product_Category_ID", null);
        else
        set_Value ("M_Product_Category_ID", Integer.valueOf(M_Product_Category_ID));
        
    }
    
    /** Get Product Category.
    @return Category of a Product */
    public int getM_Product_Category_ID() 
    {
        return get_ValueAsInt("M_Product_Category_ID");
        
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
    
    /** Set Planned Amount.
    @param PlannedAmt Planned amount for this project */
    public void setPlannedAmt (java.math.BigDecimal PlannedAmt)
    {
        if (PlannedAmt == null) throw new IllegalArgumentException ("PlannedAmt is mandatory.");
        set_Value ("PlannedAmt", PlannedAmt);
        
    }
    
    /** Get Planned Amount.
    @return Planned amount for this project */
    public java.math.BigDecimal getPlannedAmt() 
    {
        return get_ValueAsBigDecimal("PlannedAmt");
        
    }
    
    /** Set Planned Date.
    @param PlannedDate Date projected */
    public void setPlannedDate (Timestamp PlannedDate)
    {
        set_Value ("PlannedDate", PlannedDate);
        
    }
    
    /** Get Planned Date.
    @return Date projected */
    public Timestamp getPlannedDate() 
    {
        return (Timestamp)get_Value("PlannedDate");
        
    }
    
    /** Set Planned Margin.
    @param PlannedMarginAmt Project's planned margin amount */
    public void setPlannedMarginAmt (java.math.BigDecimal PlannedMarginAmt)
    {
        set_Value ("PlannedMarginAmt", PlannedMarginAmt);
        
    }
    
    /** Get Planned Margin.
    @return Project's planned margin amount */
    public java.math.BigDecimal getPlannedMarginAmt() 
    {
        return get_ValueAsBigDecimal("PlannedMarginAmt");
        
    }
    
    /** Set Planned Price.
    @param PlannedPrice Planned price for this project line */
    public void setPlannedPrice (java.math.BigDecimal PlannedPrice)
    {
        if (PlannedPrice == null) throw new IllegalArgumentException ("PlannedPrice is mandatory.");
        set_Value ("PlannedPrice", PlannedPrice);
        
    }
    
    /** Get Planned Price.
    @return Planned price for this project line */
    public java.math.BigDecimal getPlannedPrice() 
    {
        return get_ValueAsBigDecimal("PlannedPrice");
        
    }
    
    /** Set Planned Quantity.
    @param PlannedQty Planned quantity for this project */
    public void setPlannedQty (java.math.BigDecimal PlannedQty)
    {
        if (PlannedQty == null) throw new IllegalArgumentException ("PlannedQty is mandatory.");
        set_Value ("PlannedQty", PlannedQty);
        
    }
    
    /** Get Planned Quantity.
    @return Planned quantity for this project */
    public java.math.BigDecimal getPlannedQty() 
    {
        return get_ValueAsBigDecimal("PlannedQty");
        
    }
    
    /** Set List price.
    @param PriceList List price (Internally used vs. entered) */
    public void setPriceList (java.math.BigDecimal PriceList)
    {
        set_Value ("PriceList", PriceList);
        
    }
    
    /** Get List price.
    @return List price (Internally used vs. entered) */
    public java.math.BigDecimal getPriceList() 
    {
        return get_ValueAsBigDecimal("PriceList");
        
    }
    
    /** Set Probability.
    @param Probability Probability in Percent */
    public void setProbability (int Probability)
    {
        set_Value ("Probability", Integer.valueOf(Probability));
        
    }
    
    /** Get Probability.
    @return Probability in Percent */
    public int getProbability() 
    {
        return get_ValueAsInt("Probability");
        
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
    
    
}
