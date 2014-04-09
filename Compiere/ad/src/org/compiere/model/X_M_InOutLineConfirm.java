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
/** Generated Model for M_InOutLineConfirm
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_InOutLineConfirm.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_InOutLineConfirm extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_InOutLineConfirm_ID id
    @param trx transaction
    */
    public X_M_InOutLineConfirm (Ctx ctx, int M_InOutLineConfirm_ID, Trx trx)
    {
        super (ctx, M_InOutLineConfirm_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_InOutLineConfirm_ID == 0)
        {
            setConfirmedQty (Env.ZERO);
            setM_InOutConfirm_ID (0);
            setM_InOutLineConfirm_ID (0);
            setM_InOutLine_ID (0);
            setProcessed (false);	// N
            setTargetQty (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_InOutLineConfirm (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518840090789L;
    /** Last Updated Timestamp 2009-03-10 11:52:54.0 */
    public static final long updatedMS = 1236714774000L;
    /** AD_Table_ID=728 */
    public static final int Table_ID=728;
    
    /** TableName=M_InOutLineConfirm */
    public static final String Table_Name="M_InOutLineConfirm";
    
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
        if (C_InvoiceLine_ID <= 0) set_Value ("C_InvoiceLine_ID", null);
        else
        set_Value ("C_InvoiceLine_ID", Integer.valueOf(C_InvoiceLine_ID));
        
    }
    
    /** Get Invoice Line.
    @return Invoice Detail Line */
    public int getC_InvoiceLine_ID() 
    {
        return get_ValueAsInt("C_InvoiceLine_ID");
        
    }
    
    /** Set Confirmation No.
    @param ConfirmationNo Confirmation Number */
    public void setConfirmationNo (String ConfirmationNo)
    {
        set_Value ("ConfirmationNo", ConfirmationNo);
        
    }
    
    /** Get Confirmation No.
    @return Confirmation Number */
    public String getConfirmationNo() 
    {
        return (String)get_Value("ConfirmationNo");
        
    }
    
    /** Set Confirmed Quantity.
    @param ConfirmedQty Confirmation of a received quantity */
    public void setConfirmedQty (java.math.BigDecimal ConfirmedQty)
    {
        if (ConfirmedQty == null) throw new IllegalArgumentException ("ConfirmedQty is mandatory.");
        set_Value ("ConfirmedQty", ConfirmedQty);
        
    }
    
    /** Get Confirmed Quantity.
    @return Confirmation of a received quantity */
    public java.math.BigDecimal getConfirmedQty() 
    {
        return get_ValueAsBigDecimal("ConfirmedQty");
        
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
    
    /** Set Difference.
    @param DifferenceQty Difference Quantity */
    public void setDifferenceQty (java.math.BigDecimal DifferenceQty)
    {
        set_Value ("DifferenceQty", DifferenceQty);
        
    }
    
    /** Get Difference.
    @return Difference Quantity */
    public java.math.BigDecimal getDifferenceQty() 
    {
        return get_ValueAsBigDecimal("DifferenceQty");
        
    }
    
    /** Set Ship/Receipt Confirmation.
    @param M_InOutConfirm_ID Material Shipment or Receipt Confirmation */
    public void setM_InOutConfirm_ID (int M_InOutConfirm_ID)
    {
        if (M_InOutConfirm_ID < 1) throw new IllegalArgumentException ("M_InOutConfirm_ID is mandatory.");
        set_ValueNoCheck ("M_InOutConfirm_ID", Integer.valueOf(M_InOutConfirm_ID));
        
    }
    
    /** Get Ship/Receipt Confirmation.
    @return Material Shipment or Receipt Confirmation */
    public int getM_InOutConfirm_ID() 
    {
        return get_ValueAsInt("M_InOutConfirm_ID");
        
    }
    
    /** Set Ship/Receipt Confirmation Line.
    @param M_InOutLineConfirm_ID Material Shipment or Receipt Confirmation Line */
    public void setM_InOutLineConfirm_ID (int M_InOutLineConfirm_ID)
    {
        if (M_InOutLineConfirm_ID < 1) throw new IllegalArgumentException ("M_InOutLineConfirm_ID is mandatory.");
        set_ValueNoCheck ("M_InOutLineConfirm_ID", Integer.valueOf(M_InOutLineConfirm_ID));
        
    }
    
    /** Get Ship/Receipt Confirmation Line.
    @return Material Shipment or Receipt Confirmation Line */
    public int getM_InOutLineConfirm_ID() 
    {
        return get_ValueAsInt("M_InOutLineConfirm_ID");
        
    }
    
    /** Set Shipment/Receipt Line.
    @param M_InOutLine_ID Line on Shipment or Receipt document */
    public void setM_InOutLine_ID (int M_InOutLine_ID)
    {
        if (M_InOutLine_ID < 1) throw new IllegalArgumentException ("M_InOutLine_ID is mandatory.");
        set_ValueNoCheck ("M_InOutLine_ID", Integer.valueOf(M_InOutLine_ID));
        
    }
    
    /** Get Shipment/Receipt Line.
    @return Line on Shipment or Receipt document */
    public int getM_InOutLine_ID() 
    {
        return get_ValueAsInt("M_InOutLine_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_InOutLine_ID()));
        
    }
    
    /** Set Phys Inventory Line.
    @param M_InventoryLine_ID Unique line in an Inventory document */
    public void setM_InventoryLine_ID (int M_InventoryLine_ID)
    {
        if (M_InventoryLine_ID <= 0) set_Value ("M_InventoryLine_ID", null);
        else
        set_Value ("M_InventoryLine_ID", Integer.valueOf(M_InventoryLine_ID));
        
    }
    
    /** Get Phys Inventory Line.
    @return Unique line in an Inventory document */
    public int getM_InventoryLine_ID() 
    {
        return get_ValueAsInt("M_InventoryLine_ID");
        
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
    
    /** Set Scrapped Quantity.
    @param ScrappedQty The Quantity scrapped due to QA issues */
    public void setScrappedQty (java.math.BigDecimal ScrappedQty)
    {
        set_Value ("ScrappedQty", ScrappedQty);
        
    }
    
    /** Get Scrapped Quantity.
    @return The Quantity scrapped due to QA issues */
    public java.math.BigDecimal getScrappedQty() 
    {
        return get_ValueAsBigDecimal("ScrappedQty");
        
    }
    
    /** Set Target Quantity.
    @param TargetQty Target Movement Quantity */
    public void setTargetQty (java.math.BigDecimal TargetQty)
    {
        if (TargetQty == null) throw new IllegalArgumentException ("TargetQty is mandatory.");
        set_ValueNoCheck ("TargetQty", TargetQty);
        
    }
    
    /** Get Target Quantity.
    @return Target Movement Quantity */
    public java.math.BigDecimal getTargetQty() 
    {
        return get_ValueAsBigDecimal("TargetQty");
        
    }
    
    
}
