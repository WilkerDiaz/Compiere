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
/** Generated Model for C_OrderLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_OrderLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_OrderLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_OrderLine_ID id
    @param trx transaction
    */
    public X_C_OrderLine (Ctx ctx, int C_OrderLine_ID, Trx trx)
    {
        super (ctx, C_OrderLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_OrderLine_ID == 0)
        {
            setC_Currency_ID (0);	// @C_Currency_ID@
            setC_OrderLine_ID (0);
            setC_Order_ID (0);
            setC_Tax_ID (0);
            setDateOrdered (new Timestamp(System.currentTimeMillis()));	// @SQL=SELECT DateOrdered AS DefaultValue FROM C_Order WHERE C_Order_ID=@C_Order_ID@
            setFreightAmt (Env.ZERO);
            setIsDescription (false);	// N
            setLine (0);	// @SQL=SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM C_OrderLine WHERE C_Order_ID=@C_Order_ID@
            setLineNetAmt (Env.ZERO);
            setM_Warehouse_ID (0);	// @M_Warehouse_ID@
            setPriceActual (Env.ZERO);
            setPriceEntered (Env.ZERO);
            setPriceLimit (Env.ZERO);
            setPriceList (Env.ZERO);
            setProcessed (false);	// N
            setQtyAllocated (Env.ZERO);	// 0
            setQtyDedicated (Env.ZERO);	// 0
            setQtyDelivered (Env.ZERO);
            setQtyEntered (Env.ZERO);	// 1
            setQtyInvoiced (Env.ZERO);
            setQtyLostSales (Env.ZERO);
            setQtyOrdered (Env.ZERO);	// 1
            setQtyReserved (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_OrderLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27533223707789L;
    /** Last Updated Timestamp 2009-08-24 00:19:51.0 */
    public static final long updatedMS = 1251098391000L;
    /** AD_Table_ID=260 */
    public static final int Table_ID=260;
    
    /** TableName=C_OrderLine */
    public static final String Table_Name="C_OrderLine";
    
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
        if (C_BPartner_ID <= 0) set_ValueNoCheck ("C_BPartner_ID", null);
        else
        set_ValueNoCheck ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
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
    
    /** Set Charge.
    @param C_Charge_ID Additional document charges */
    public void setC_Charge_ID (int C_Charge_ID)
    {
        if (C_Charge_ID <= 0) set_Value ("C_Charge_ID", null);
        else
        set_Value ("C_Charge_ID", Integer.valueOf(C_Charge_ID));
        
    }
    
    /** Get Charge.
    @return Additional document charges */
    public int getC_Charge_ID() 
    {
        return get_ValueAsInt("C_Charge_ID");
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID < 1) throw new IllegalArgumentException ("C_Currency_ID is mandatory.");
        set_ValueNoCheck ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set Order Line.
    @param C_OrderLine_ID Order Line */
    public void setC_OrderLine_ID (int C_OrderLine_ID)
    {
        if (C_OrderLine_ID < 1) throw new IllegalArgumentException ("C_OrderLine_ID is mandatory.");
        set_ValueNoCheck ("C_OrderLine_ID", Integer.valueOf(C_OrderLine_ID));
        
    }
    
    /** Get Order Line.
    @return Order Line */
    public int getC_OrderLine_ID() 
    {
        return get_ValueAsInt("C_OrderLine_ID");
        
    }
    
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID < 1) throw new IllegalArgumentException ("C_Order_ID is mandatory.");
        set_ValueNoCheck ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_Order_ID()));
        
    }
    
    /** Set Project Phase.
    @param C_ProjectPhase_ID Phase of a Project */
    public void setC_ProjectPhase_ID (int C_ProjectPhase_ID)
    {
        if (C_ProjectPhase_ID <= 0) set_ValueNoCheck ("C_ProjectPhase_ID", null);
        else
        set_ValueNoCheck ("C_ProjectPhase_ID", Integer.valueOf(C_ProjectPhase_ID));
        
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
        if (C_ProjectTask_ID <= 0) set_ValueNoCheck ("C_ProjectTask_ID", null);
        else
        set_ValueNoCheck ("C_ProjectTask_ID", Integer.valueOf(C_ProjectTask_ID));
        
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
    
    /** Set Tax.
    @param C_Tax_ID Tax identifier */
    public void setC_Tax_ID (int C_Tax_ID)
    {
        if (C_Tax_ID < 1) throw new IllegalArgumentException ("C_Tax_ID is mandatory.");
        set_Value ("C_Tax_ID", Integer.valueOf(C_Tax_ID));
        
    }
    
    /** Get Tax.
    @return Tax identifier */
    public int getC_Tax_ID() 
    {
        return get_ValueAsInt("C_Tax_ID");
        
    }
    
    /** Set UOM.
    @param C_UOM_ID Unit of Measure */
    public void setC_UOM_ID (int C_UOM_ID)
    {
        if (C_UOM_ID <= 0) set_ValueNoCheck ("C_UOM_ID", null);
        else
        set_ValueNoCheck ("C_UOM_ID", Integer.valueOf(C_UOM_ID));
        
    }
    
    /** Get UOM.
    @return Unit of Measure */
    public int getC_UOM_ID() 
    {
        return get_ValueAsInt("C_UOM_ID");
        
    }
    
    /** Set Date Delivered.
    @param DateDelivered Date when the product was delivered */
    public void setDateDelivered (Timestamp DateDelivered)
    {
        set_ValueNoCheck ("DateDelivered", DateDelivered);
        
    }
    
    /** Get Date Delivered.
    @return Date when the product was delivered */
    public Timestamp getDateDelivered() 
    {
        return (Timestamp)get_Value("DateDelivered");
        
    }
    
    /** Set Date Invoiced.
    @param DateInvoiced Date printed on Invoice */
    public void setDateInvoiced (Timestamp DateInvoiced)
    {
        set_ValueNoCheck ("DateInvoiced", DateInvoiced);
        
    }
    
    /** Get Date Invoiced.
    @return Date printed on Invoice */
    public Timestamp getDateInvoiced() 
    {
        return (Timestamp)get_Value("DateInvoiced");
        
    }
    
    /** Set Date Ordered.
    @param DateOrdered Date of Order */
    public void setDateOrdered (Timestamp DateOrdered)
    {
        if (DateOrdered == null) throw new IllegalArgumentException ("DateOrdered is mandatory.");
        set_Value ("DateOrdered", DateOrdered);
        
    }
    
    /** Get Date Ordered.
    @return Date of Order */
    public Timestamp getDateOrdered() 
    {
        return (Timestamp)get_Value("DateOrdered");
        
    }
    
    /** Set Date Promised.
    @param DatePromised Date Order was promised */
    public void setDatePromised (Timestamp DatePromised)
    {
        set_Value ("DatePromised", DatePromised);
        
    }
    
    /** Get Date Promised.
    @return Date Order was promised */
    public Timestamp getDatePromised() 
    {
        return (Timestamp)get_Value("DatePromised");
        
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
    
    /** Set Freight Amount.
    @param FreightAmt Freight Amount */
    public void setFreightAmt (java.math.BigDecimal FreightAmt)
    {
        if (FreightAmt == null) throw new IllegalArgumentException ("FreightAmt is mandatory.");
        set_Value ("FreightAmt", FreightAmt);
        
    }
    
    /** Get Freight Amount.
    @return Freight Amount */
    public java.math.BigDecimal getFreightAmt() 
    {
        return get_ValueAsBigDecimal("FreightAmt");
        
    }
    
    /** Set Description Only.
    @param IsDescription If true, the line is just description and no transaction */
    public void setIsDescription (boolean IsDescription)
    {
        set_Value ("IsDescription", Boolean.valueOf(IsDescription));
        
    }
    
    /** Get Description Only.
    @return If true, the line is just description and no transaction */
    public boolean isDescription() 
    {
        return get_ValueAsBoolean("IsDescription");
        
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
    
    /** Set Line Amount.
    @param LineNetAmt Line Extended Amount (Quantity * Actual Price) without Freight and Charges */
    public void setLineNetAmt (java.math.BigDecimal LineNetAmt)
    {
        if (LineNetAmt == null) throw new IllegalArgumentException ("LineNetAmt is mandatory.");
        set_ValueNoCheck ("LineNetAmt", LineNetAmt);
        
    }
    
    /** Get Line Amount.
    @return Line Extended Amount (Quantity * Actual Price) without Freight and Charges */
    public java.math.BigDecimal getLineNetAmt() 
    {
        return get_ValueAsBigDecimal("LineNetAmt");
        
    }
    
    /** Set Attribute Set Instance.
    @param M_AttributeSetInstance_ID Product Attribute Set Instance */
    public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
    {
        if (M_AttributeSetInstance_ID <= 0) set_Value ("M_AttributeSetInstance_ID", null);
        else
        set_Value ("M_AttributeSetInstance_ID", Integer.valueOf(M_AttributeSetInstance_ID));
        
    }
    
    /** Get Attribute Set Instance.
    @return Product Attribute Set Instance */
    public int getM_AttributeSetInstance_ID() 
    {
        return get_ValueAsInt("M_AttributeSetInstance_ID");
        
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
    
    /** Set Freight Carrier.
    @param M_Shipper_ID Method or manner of product delivery */
    public void setM_Shipper_ID (int M_Shipper_ID)
    {
        if (M_Shipper_ID <= 0) set_Value ("M_Shipper_ID", null);
        else
        set_Value ("M_Shipper_ID", Integer.valueOf(M_Shipper_ID));
        
    }
    
    /** Get Freight Carrier.
    @return Method or manner of product delivery */
    public int getM_Shipper_ID() 
    {
        return get_ValueAsInt("M_Shipper_ID");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID < 1) throw new IllegalArgumentException ("M_Warehouse_ID is mandatory.");
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Set Orig Shipment Line.
    @param Orig_InOutLine_ID Original shipment line of the RMA */
    public void setOrig_InOutLine_ID (int Orig_InOutLine_ID)
    {
        if (Orig_InOutLine_ID <= 0) set_Value ("Orig_InOutLine_ID", null);
        else
        set_Value ("Orig_InOutLine_ID", Integer.valueOf(Orig_InOutLine_ID));
        
    }
    
    /** Get Orig Shipment Line.
    @return Original shipment line of the RMA */
    public int getOrig_InOutLine_ID() 
    {
        return get_ValueAsInt("Orig_InOutLine_ID");
        
    }
    
    /** Set Orig Sales Order Line.
    @param Orig_OrderLine_ID Original Sales Order Line for Return Material Authorization */
    public void setOrig_OrderLine_ID (int Orig_OrderLine_ID)
    {
        if (Orig_OrderLine_ID <= 0) set_Value ("Orig_OrderLine_ID", null);
        else
        set_Value ("Orig_OrderLine_ID", Integer.valueOf(Orig_OrderLine_ID));
        
    }
    
    /** Get Orig Sales Order Line.
    @return Original Sales Order Line for Return Material Authorization */
    public int getOrig_OrderLine_ID() 
    {
        return get_ValueAsInt("Orig_OrderLine_ID");
        
    }
    
    /** Set Unit Price.
    @param PriceActual Actual Price */
    public void setPriceActual (java.math.BigDecimal PriceActual)
    {
        if (PriceActual == null) throw new IllegalArgumentException ("PriceActual is mandatory.");
        set_ValueNoCheck ("PriceActual", PriceActual);
        
    }
    
    /** Get Unit Price.
    @return Actual Price */
    public java.math.BigDecimal getPriceActual() 
    {
        return get_ValueAsBigDecimal("PriceActual");
        
    }
    
    /** Set Cost Price.
    @param PriceCost Indicates the Price per Unit of Measure including all indirect costs (Freight, etc.) */
    public void setPriceCost (java.math.BigDecimal PriceCost)
    {
        set_Value ("PriceCost", PriceCost);
        
    }
    
    /** Get Cost Price.
    @return Indicates the Price per Unit of Measure including all indirect costs (Freight, etc.) */
    public java.math.BigDecimal getPriceCost() 
    {
        return get_ValueAsBigDecimal("PriceCost");
        
    }
    
    /** Set Price.
    @param PriceEntered Price Entered - the price based on the selected/base UoM */
    public void setPriceEntered (java.math.BigDecimal PriceEntered)
    {
        if (PriceEntered == null) throw new IllegalArgumentException ("PriceEntered is mandatory.");
        set_Value ("PriceEntered", PriceEntered);
        
    }
    
    /** Get Price.
    @return Price Entered - the price based on the selected/base UoM */
    public java.math.BigDecimal getPriceEntered() 
    {
        return get_ValueAsBigDecimal("PriceEntered");
        
    }
    
    /** Set Limit Price.
    @param PriceLimit Lowest price for a product */
    public void setPriceLimit (java.math.BigDecimal PriceLimit)
    {
        if (PriceLimit == null) throw new IllegalArgumentException ("PriceLimit is mandatory.");
        set_Value ("PriceLimit", PriceLimit);
        
    }
    
    /** Get Limit Price.
    @return Lowest price for a product */
    public java.math.BigDecimal getPriceLimit() 
    {
        return get_ValueAsBigDecimal("PriceLimit");
        
    }
    
    /** Set List price.
    @param PriceList List price (Internally used vs. entered) */
    public void setPriceList (java.math.BigDecimal PriceList)
    {
        if (PriceList == null) throw new IllegalArgumentException ("PriceList is mandatory.");
        set_Value ("PriceList", PriceList);
        
    }
    
    /** Get List price.
    @return List price (Internally used vs. entered) */
    public java.math.BigDecimal getPriceList() 
    {
        return get_ValueAsBigDecimal("PriceList");
        
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
    
    /** Set Quantity Allocated.
    @param QtyAllocated Quantity that has been picked and is awaiting shipment */
    public void setQtyAllocated (java.math.BigDecimal QtyAllocated)
    {
        if (QtyAllocated == null) throw new IllegalArgumentException ("QtyAllocated is mandatory.");
        set_ValueNoCheck ("QtyAllocated", QtyAllocated);
        
    }
    
    /** Get Quantity Allocated.
    @return Quantity that has been picked and is awaiting shipment */
    public java.math.BigDecimal getQtyAllocated() 
    {
        return get_ValueAsBigDecimal("QtyAllocated");
        
    }
    
    /** Set Quantity Dedicated.
    @param QtyDedicated Quantity for which there is a pending Warehouse Task */
    public void setQtyDedicated (java.math.BigDecimal QtyDedicated)
    {
        if (QtyDedicated == null) throw new IllegalArgumentException ("QtyDedicated is mandatory.");
        set_ValueNoCheck ("QtyDedicated", QtyDedicated);
        
    }
    
    /** Get Quantity Dedicated.
    @return Quantity for which there is a pending Warehouse Task */
    public java.math.BigDecimal getQtyDedicated() 
    {
        return get_ValueAsBigDecimal("QtyDedicated");
        
    }
    
    /** Set Quantity Delivered.
    @param QtyDelivered Quantity Delivered */
    public void setQtyDelivered (java.math.BigDecimal QtyDelivered)
    {
        if (QtyDelivered == null) throw new IllegalArgumentException ("QtyDelivered is mandatory.");
        set_ValueNoCheck ("QtyDelivered", QtyDelivered);
        
    }
    
    /** Get Quantity Delivered.
    @return Quantity Delivered */
    public java.math.BigDecimal getQtyDelivered() 
    {
        return get_ValueAsBigDecimal("QtyDelivered");
        
    }
    
    /** Set Quantity.
    @param QtyEntered The Quantity Entered is based on the selected UoM */
    public void setQtyEntered (java.math.BigDecimal QtyEntered)
    {
        if (QtyEntered == null) throw new IllegalArgumentException ("QtyEntered is mandatory.");
        set_Value ("QtyEntered", QtyEntered);
        
    }
    
    /** Get Quantity.
    @return The Quantity Entered is based on the selected UoM */
    public java.math.BigDecimal getQtyEntered() 
    {
        return get_ValueAsBigDecimal("QtyEntered");
        
    }
    
    /** Set Quantity Invoiced.
    @param QtyInvoiced Invoiced Quantity */
    public void setQtyInvoiced (java.math.BigDecimal QtyInvoiced)
    {
        if (QtyInvoiced == null) throw new IllegalArgumentException ("QtyInvoiced is mandatory.");
        set_ValueNoCheck ("QtyInvoiced", QtyInvoiced);
        
    }
    
    /** Get Quantity Invoiced.
    @return Invoiced Quantity */
    public java.math.BigDecimal getQtyInvoiced() 
    {
        return get_ValueAsBigDecimal("QtyInvoiced");
        
    }
    
    /** Set Lost Sales Quantity.
    @param QtyLostSales Quantity of potential sales */
    public void setQtyLostSales (java.math.BigDecimal QtyLostSales)
    {
        if (QtyLostSales == null) throw new IllegalArgumentException ("QtyLostSales is mandatory.");
        set_Value ("QtyLostSales", QtyLostSales);
        
    }
    
    /** Get Lost Sales Quantity.
    @return Quantity of potential sales */
    public java.math.BigDecimal getQtyLostSales() 
    {
        return get_ValueAsBigDecimal("QtyLostSales");
        
    }
    
    /** Set Quantity Ordered.
    @param QtyOrdered Ordered Quantity */
    public void setQtyOrdered (java.math.BigDecimal QtyOrdered)
    {
        if (QtyOrdered == null) throw new IllegalArgumentException ("QtyOrdered is mandatory.");
        set_Value ("QtyOrdered", QtyOrdered);
        
    }
    
    /** Get Quantity Ordered.
    @return Ordered Quantity */
    public java.math.BigDecimal getQtyOrdered() 
    {
        return get_ValueAsBigDecimal("QtyOrdered");
        
    }
    
    /** Set Quantity Reserved.
    @param QtyReserved Quantity Reserved */
    public void setQtyReserved (java.math.BigDecimal QtyReserved)
    {
        if (QtyReserved == null) throw new IllegalArgumentException ("QtyReserved is mandatory.");
        set_ValueNoCheck ("QtyReserved", QtyReserved);
        
    }
    
    /** Get Quantity Reserved.
    @return Quantity Reserved */
    public java.math.BigDecimal getQtyReserved() 
    {
        return get_ValueAsBigDecimal("QtyReserved");
        
    }
    
    /** Set Quantity Returned.
    @param QtyReturned Quantity Returned */
    public void setQtyReturned (java.math.BigDecimal QtyReturned)
    {
        set_Value ("QtyReturned", QtyReturned);
        
    }
    
    /** Get Quantity Returned.
    @return Quantity Returned */
    public java.math.BigDecimal getQtyReturned() 
    {
        return get_ValueAsBigDecimal("QtyReturned");
        
    }
    
    /** Set Revenue Recognition Amt.
    @param RRAmt Revenue Recognition Amount */
    public void setRRAmt (java.math.BigDecimal RRAmt)
    {
        set_Value ("RRAmt", RRAmt);
        
    }
    
    /** Get Revenue Recognition Amt.
    @return Revenue Recognition Amount */
    public java.math.BigDecimal getRRAmt() 
    {
        return get_ValueAsBigDecimal("RRAmt");
        
    }
    
    /** Set Revenue Recognition Start.
    @param RRStartDate Revenue Recognition Start Date */
    public void setRRStartDate (Timestamp RRStartDate)
    {
        set_Value ("RRStartDate", RRStartDate);
        
    }
    
    /** Get Revenue Recognition Start.
    @return Revenue Recognition Start Date */
    public Timestamp getRRStartDate() 
    {
        return (Timestamp)get_Value("RRStartDate");
        
    }
    
    /** Set Referenced Order Line.
    @param Ref_OrderLine_ID Reference to corresponding Sales/Purchase Order */
    public void setRef_OrderLine_ID (int Ref_OrderLine_ID)
    {
        if (Ref_OrderLine_ID <= 0) set_Value ("Ref_OrderLine_ID", null);
        else
        set_Value ("Ref_OrderLine_ID", Integer.valueOf(Ref_OrderLine_ID));
        
    }
    
    /** Get Referenced Order Line.
    @return Reference to corresponding Sales/Purchase Order */
    public int getRef_OrderLine_ID() 
    {
        return get_ValueAsInt("Ref_OrderLine_ID");
        
    }
    
    /** Set Assigned Resource.
    @param S_ResourceAssignment_ID Assigned Resource */
    public void setS_ResourceAssignment_ID (int S_ResourceAssignment_ID)
    {
        if (S_ResourceAssignment_ID <= 0) set_Value ("S_ResourceAssignment_ID", null);
        else
        set_Value ("S_ResourceAssignment_ID", Integer.valueOf(S_ResourceAssignment_ID));
        
    }
    
    /** Get Assigned Resource.
    @return Assigned Resource */
    public int getS_ResourceAssignment_ID() 
    {
        return get_ValueAsInt("S_ResourceAssignment_ID");
        
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
