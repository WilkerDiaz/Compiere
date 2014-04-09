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
/** Generated Model for M_MovementLineConfirm
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_MovementLineConfirm.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_MovementLineConfirm extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_MovementLineConfirm_ID id
    @param trx transaction
    */
    public X_M_MovementLineConfirm (Ctx ctx, int M_MovementLineConfirm_ID, Trx trx)
    {
        super (ctx, M_MovementLineConfirm_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_MovementLineConfirm_ID == 0)
        {
            setConfirmedQty (Env.ZERO);
            setDifferenceQty (Env.ZERO);
            setM_MovementConfirm_ID (0);
            setM_MovementLineConfirm_ID (0);
            setM_MovementLine_ID (0);
            setProcessed (false);	// N
            setScrappedQty (Env.ZERO);
            setTargetQty (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_MovementLineConfirm (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518840073789L;
    /** Last Updated Timestamp 2009-03-10 11:52:37.0 */
    public static final long updatedMS = 1236714757000L;
    /** AD_Table_ID=737 */
    public static final int Table_ID=737;
    
    /** TableName=M_MovementLineConfirm */
    public static final String Table_Name="M_MovementLineConfirm";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
        if (DifferenceQty == null) throw new IllegalArgumentException ("DifferenceQty is mandatory.");
        set_Value ("DifferenceQty", DifferenceQty);
        
    }
    
    /** Get Difference.
    @return Difference Quantity */
    public java.math.BigDecimal getDifferenceQty() 
    {
        return get_ValueAsBigDecimal("DifferenceQty");
        
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
    
    /** Set Move Confirm.
    @param M_MovementConfirm_ID Inventory Move Confirmation */
    public void setM_MovementConfirm_ID (int M_MovementConfirm_ID)
    {
        if (M_MovementConfirm_ID < 1) throw new IllegalArgumentException ("M_MovementConfirm_ID is mandatory.");
        set_ValueNoCheck ("M_MovementConfirm_ID", Integer.valueOf(M_MovementConfirm_ID));
        
    }
    
    /** Get Move Confirm.
    @return Inventory Move Confirmation */
    public int getM_MovementConfirm_ID() 
    {
        return get_ValueAsInt("M_MovementConfirm_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_MovementConfirm_ID()));
        
    }
    
    /** Set Move Line Confirm.
    @param M_MovementLineConfirm_ID Inventory Move Line Confirmation */
    public void setM_MovementLineConfirm_ID (int M_MovementLineConfirm_ID)
    {
        if (M_MovementLineConfirm_ID < 1) throw new IllegalArgumentException ("M_MovementLineConfirm_ID is mandatory.");
        set_ValueNoCheck ("M_MovementLineConfirm_ID", Integer.valueOf(M_MovementLineConfirm_ID));
        
    }
    
    /** Get Move Line Confirm.
    @return Inventory Move Line Confirmation */
    public int getM_MovementLineConfirm_ID() 
    {
        return get_ValueAsInt("M_MovementLineConfirm_ID");
        
    }
    
    /** Set Move Line.
    @param M_MovementLine_ID Inventory Move document Line */
    public void setM_MovementLine_ID (int M_MovementLine_ID)
    {
        if (M_MovementLine_ID < 1) throw new IllegalArgumentException ("M_MovementLine_ID is mandatory.");
        set_Value ("M_MovementLine_ID", Integer.valueOf(M_MovementLine_ID));
        
    }
    
    /** Get Move Line.
    @return Inventory Move document Line */
    public int getM_MovementLine_ID() 
    {
        return get_ValueAsInt("M_MovementLine_ID");
        
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
        if (ScrappedQty == null) throw new IllegalArgumentException ("ScrappedQty is mandatory.");
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
        set_Value ("TargetQty", TargetQty);
        
    }
    
    /** Get Target Quantity.
    @return Target Movement Quantity */
    public java.math.BigDecimal getTargetQty() 
    {
        return get_ValueAsBigDecimal("TargetQty");
        
    }
    
    
}
