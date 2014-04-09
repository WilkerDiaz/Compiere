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
/** Generated Model for M_InventoryLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.1 - $Id: X_M_InventoryLine.java 8640 2010-04-19 09:04:27Z ragrawal $ */
public class X_M_InventoryLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_InventoryLine_ID id
    @param trx transaction
    */
    public X_M_InventoryLine (Ctx ctx, int M_InventoryLine_ID, Trx trx)
    {
        super (ctx, M_InventoryLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_InventoryLine_ID == 0)
        {
            setInventoryType (null);	// D
            setIsInternalUse (false);
            setM_InventoryLine_ID (0);
            setM_Inventory_ID (0);
            setM_Locator_ID (0);	// @M_Locator_ID@
            setM_Product_ID (0);
            setProcessed (false);	// N
            setQtyBook (Env.ZERO);
            setQtyCount (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_InventoryLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27553748014789L;
    /** Last Updated Timestamp 2010-04-19 02:01:38.0 */
    public static final long updatedMS = 1271622698000L;
    /** AD_Table_ID=322 */
    public static final int Table_ID=322;
    
    /** TableName=M_InventoryLine */
    public static final String Table_Name="M_InventoryLine";
    
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
    
    /** Charge Account = C */
    public static final String INVENTORYTYPE_ChargeAccount = X_Ref_M_Inventory_Type.CHARGE_ACCOUNT.getValue();
    /** Inventory Difference = D */
    public static final String INVENTORYTYPE_InventoryDifference = X_Ref_M_Inventory_Type.INVENTORY_DIFFERENCE.getValue();
    /** Set Inventory Type.
    @param InventoryType Type of inventory difference */
    public void setInventoryType (String InventoryType)
    {
        if (InventoryType == null) throw new IllegalArgumentException ("InventoryType is mandatory");
        if (!X_Ref_M_Inventory_Type.isValid(InventoryType))
        throw new IllegalArgumentException ("InventoryType Invalid value - " + InventoryType + " - Reference_ID=292 - C - D");
        set_Value ("InventoryType", InventoryType);
        
    }
    
    /** Get Inventory Type.
    @return Type of inventory difference */
    public String getInventoryType() 
    {
        return (String)get_Value("InventoryType");
        
    }
    
    /** Set Internal Use.
    @param IsInternalUse The Record is internal use */
    public void setIsInternalUse (boolean IsInternalUse)
    {
        set_Value ("IsInternalUse", Boolean.valueOf(IsInternalUse));
        
    }
    
    /** Get Internal Use.
    @return The Record is internal use */
    public boolean isInternalUse() 
    {
        return get_ValueAsBoolean("IsInternalUse");
        
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
    
    /** Set Analysis Group.
    @param M_ABCAnalysisGroup_ID Analysis Group */
    public void setM_ABCAnalysisGroup_ID (int M_ABCAnalysisGroup_ID)
    {
        if (M_ABCAnalysisGroup_ID <= 0) set_Value ("M_ABCAnalysisGroup_ID", null);
        else
        set_Value ("M_ABCAnalysisGroup_ID", Integer.valueOf(M_ABCAnalysisGroup_ID));
        
    }
    
    /** Get Analysis Group.
    @return Analysis Group */
    public int getM_ABCAnalysisGroup_ID() 
    {
        return get_ValueAsInt("M_ABCAnalysisGroup_ID");
        
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
    
    /** Set Phys Inventory Line.
    @param M_InventoryLine_ID Unique line in an Inventory document */
    public void setM_InventoryLine_ID (int M_InventoryLine_ID)
    {
        if (M_InventoryLine_ID < 1) throw new IllegalArgumentException ("M_InventoryLine_ID is mandatory.");
        set_ValueNoCheck ("M_InventoryLine_ID", Integer.valueOf(M_InventoryLine_ID));
        
    }
    
    /** Get Phys Inventory Line.
    @return Unique line in an Inventory document */
    public int getM_InventoryLine_ID() 
    {
        return get_ValueAsInt("M_InventoryLine_ID");
        
    }
    
    /** Set Phys.Inventory.
    @param M_Inventory_ID Parameters for a Physical Inventory */
    public void setM_Inventory_ID (int M_Inventory_ID)
    {
        if (M_Inventory_ID < 1) throw new IllegalArgumentException ("M_Inventory_ID is mandatory.");
        set_ValueNoCheck ("M_Inventory_ID", Integer.valueOf(M_Inventory_ID));
        
    }
    
    /** Get Phys.Inventory.
    @return Parameters for a Physical Inventory */
    public int getM_Inventory_ID() 
    {
        return get_ValueAsInt("M_Inventory_ID");
        
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
    
    /** Set Quantity book.
    @param QtyBook Book Quantity */
    public void setQtyBook (java.math.BigDecimal QtyBook)
    {
        if (QtyBook == null) throw new IllegalArgumentException ("QtyBook is mandatory.");
        set_ValueNoCheck ("QtyBook", QtyBook);
        
    }
    
    /** Get Quantity book.
    @return Book Quantity */
    public java.math.BigDecimal getQtyBook() 
    {
        return get_ValueAsBigDecimal("QtyBook");
        
    }
    
    /** Set Quantity count.
    @param QtyCount Counted Quantity */
    public void setQtyCount (java.math.BigDecimal QtyCount)
    {
        if (QtyCount == null) throw new IllegalArgumentException ("QtyCount is mandatory.");
        set_Value ("QtyCount", QtyCount);
        
    }
    
    /** Get Quantity count.
    @return Counted Quantity */
    public java.math.BigDecimal getQtyCount() 
    {
        return get_ValueAsBigDecimal("QtyCount");
        
    }
    
    /** Set Internal Use Qty.
    @param QtyInternalUse Internal Use Quantity removed from Inventory */
    public void setQtyInternalUse (java.math.BigDecimal QtyInternalUse)
    {
        set_Value ("QtyInternalUse", QtyInternalUse);
        
    }
    
    /** Get Internal Use Qty.
    @return Internal Use Quantity removed from Inventory */
    public java.math.BigDecimal getQtyInternalUse() 
    {
        return get_ValueAsBigDecimal("QtyInternalUse");
        
    }
    
    /** Set UPC/EAN.
    @param UPC Bar Code (Universal Product Code or its superset European Article Number) */
    public void setUPC (String UPC)
    {
        throw new IllegalArgumentException ("UPC is virtual column");
        
    }
    
    /** Get UPC/EAN.
    @return Bar Code (Universal Product Code or its superset European Article Number) */
    public String getUPC() 
    {
        return (String)get_Value("UPC");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        throw new IllegalArgumentException ("Value is virtual column");
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    
}
