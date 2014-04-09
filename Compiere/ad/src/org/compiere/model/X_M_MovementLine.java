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
/** Generated Model for M_MovementLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_MovementLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_MovementLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_MovementLine_ID id
    @param trx transaction
    */
    public X_M_MovementLine (Ctx ctx, int M_MovementLine_ID, Trx trx)
    {
        super (ctx, M_MovementLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_MovementLine_ID == 0)
        {
            setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM M_MovementLine WHERE M_Movement_ID=@M_Movement_ID@
            setM_LocatorTo_ID (0);	// -1
            setM_Locator_ID (0);	// @M_Locator_ID@
            setM_MovementLine_ID (0);
            setM_Movement_ID (0);
            setM_Product_ID (0);
            setMovementQty (Env.ZERO);	// 1
            setProcessed (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_MovementLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27506304630789L;
    /** Last Updated Timestamp 2008-10-16 10:48:34.0 */
    public static final long updatedMS = 1224179314000L;
    /** AD_Table_ID=324 */
    public static final int Table_ID=324;
    
    /** TableName=M_MovementLine */
    public static final String Table_Name="M_MovementLine";
    
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
    
    /** Set Confirmed Quantity.
    @param ConfirmedQty Confirmation of a received quantity */
    public void setConfirmedQty (java.math.BigDecimal ConfirmedQty)
    {
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
    
    /** Set Attribute Set Instance To.
    @param M_AttributeSetInstanceTo_ID Target Product Attribute Set Instance */
    public void setM_AttributeSetInstanceTo_ID (int M_AttributeSetInstanceTo_ID)
    {
        if (M_AttributeSetInstanceTo_ID <= 0) set_ValueNoCheck ("M_AttributeSetInstanceTo_ID", null);
        else
        set_ValueNoCheck ("M_AttributeSetInstanceTo_ID", Integer.valueOf(M_AttributeSetInstanceTo_ID));
        
    }
    
    /** Get Attribute Set Instance To.
    @return Target Product Attribute Set Instance */
    public int getM_AttributeSetInstanceTo_ID() 
    {
        return get_ValueAsInt("M_AttributeSetInstanceTo_ID");
        
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
    
    /** Set Locator To.
    @param M_LocatorTo_ID Location inventory is moved to */
    public void setM_LocatorTo_ID (int M_LocatorTo_ID)
    {
        if (M_LocatorTo_ID < 1) throw new IllegalArgumentException ("M_LocatorTo_ID is mandatory.");
        set_Value ("M_LocatorTo_ID", Integer.valueOf(M_LocatorTo_ID));
        
    }
    
    /** Get Locator To.
    @return Location inventory is moved to */
    public int getM_LocatorTo_ID() 
    {
        return get_ValueAsInt("M_LocatorTo_ID");
        
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
    
    /** Set Move Line.
    @param M_MovementLine_ID Inventory Move document Line */
    public void setM_MovementLine_ID (int M_MovementLine_ID)
    {
        if (M_MovementLine_ID < 1) throw new IllegalArgumentException ("M_MovementLine_ID is mandatory.");
        set_ValueNoCheck ("M_MovementLine_ID", Integer.valueOf(M_MovementLine_ID));
        
    }
    
    /** Get Move Line.
    @return Inventory Move document Line */
    public int getM_MovementLine_ID() 
    {
        return get_ValueAsInt("M_MovementLine_ID");
        
    }
    
    /** Set Inventory Move.
    @param M_Movement_ID Movement of Inventory */
    public void setM_Movement_ID (int M_Movement_ID)
    {
        if (M_Movement_ID < 1) throw new IllegalArgumentException ("M_Movement_ID is mandatory.");
        set_ValueNoCheck ("M_Movement_ID", Integer.valueOf(M_Movement_ID));
        
    }
    
    /** Get Inventory Move.
    @return Movement of Inventory */
    public int getM_Movement_ID() 
    {
        return get_ValueAsInt("M_Movement_ID");
        
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
    
    /** Set Movement Quantity.
    @param MovementQty Quantity of a product moved. */
    public void setMovementQty (java.math.BigDecimal MovementQty)
    {
        if (MovementQty == null) throw new IllegalArgumentException ("MovementQty is mandatory.");
        set_Value ("MovementQty", MovementQty);
        
    }
    
    /** Get Movement Quantity.
    @return Quantity of a product moved. */
    public java.math.BigDecimal getMovementQty() 
    {
        return get_ValueAsBigDecimal("MovementQty");
        
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
        set_Value ("TargetQty", TargetQty);
        
    }
    
    /** Get Target Quantity.
    @return Target Movement Quantity */
    public java.math.BigDecimal getTargetQty() 
    {
        return get_ValueAsBigDecimal("TargetQty");
        
    }
    
    
}
