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
/** Generated Model for I_Inventory
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: X_I_Inventory.java 9131 2010-07-20 06:51:58Z ssharma $ */
public class X_I_Inventory extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_Inventory_ID id
    @param trx transaction
    */
    public X_I_Inventory (Ctx ctx, int I_Inventory_ID, Trx trx)
    {
        super (ctx, I_Inventory_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_Inventory_ID == 0)
        {
            setI_Inventory_ID (0);
            setI_IsImported (null);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_Inventory (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27561686414789L;
    /** Last Updated Timestamp 2010-07-19 23:08:18.0 */
    public static final long updatedMS = 1279561098000L;
    /** AD_Table_ID=572 */
    public static final int Table_ID=572;
    
    /** TableName=I_Inventory */
    public static final String Table_Name="I_Inventory";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Bin.
    @param Bin Locator Bin segment */
    public void setBin (String Bin)
    {
        set_Value ("Bin", Bin);
        
    }
    
    /** Get Bin.
    @return Locator Bin segment */
    public String getBin() 
    {
        return (String)get_Value("Bin");
        
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
    
    /** Set Import Error Message.
    @param I_ErrorMsg Messages generated from import process */
    public void setI_ErrorMsg (String I_ErrorMsg)
    {
        set_Value ("I_ErrorMsg", I_ErrorMsg);
        
    }
    
    /** Get Import Error Message.
    @return Messages generated from import process */
    public String getI_ErrorMsg() 
    {
        return (String)get_Value("I_ErrorMsg");
        
    }
    
    /** Set Import Inventory.
    @param I_Inventory_ID Import Inventory Transactions */
    public void setI_Inventory_ID (int I_Inventory_ID)
    {
        if (I_Inventory_ID < 1) throw new IllegalArgumentException ("I_Inventory_ID is mandatory.");
        set_ValueNoCheck ("I_Inventory_ID", Integer.valueOf(I_Inventory_ID));
        
    }
    
    /** Get Import Inventory.
    @return Import Inventory Transactions */
    public int getI_Inventory_ID() 
    {
        return get_ValueAsInt("I_Inventory_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getI_Inventory_ID()));
        
    }
    
    /** Error = E */
    public static final String I_ISIMPORTED_Error = X_Ref__IsImported.ERROR.getValue();
    /** No = N */
    public static final String I_ISIMPORTED_No = X_Ref__IsImported.NO.getValue();
    /** Yes = Y */
    public static final String I_ISIMPORTED_Yes = X_Ref__IsImported.YES.getValue();
    /** Set Imported.
    @param I_IsImported Has this import been processed? */
    public void setI_IsImported (String I_IsImported)
    {
        if (I_IsImported == null) throw new IllegalArgumentException ("I_IsImported is mandatory");
        if (!X_Ref__IsImported.isValid(I_IsImported))
        throw new IllegalArgumentException ("I_IsImported Invalid value - " + I_IsImported + " - Reference_ID=420 - E - N - Y");
        set_Value ("I_IsImported", I_IsImported);
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public String getI_IsImported() 
    {
        return (String)get_Value("I_IsImported");
        
    }
    
    /** Set Locator Key.
    @param LocatorValue Key of the Warehouse Locator */
    public void setLocatorValue (String LocatorValue)
    {
        set_Value ("LocatorValue", LocatorValue);
        
    }
    
    /** Get Locator Key.
    @return Key of the Warehouse Locator */
    public String getLocatorValue() 
    {
        return (String)get_Value("LocatorValue");
        
    }
    
    /** Set Lot No.
    @param Lot Lot number (alphanumeric) */
    public void setLot (String Lot)
    {
        set_Value ("Lot", Lot);
        
    }
    
    /** Get Lot No.
    @return Lot number (alphanumeric) */
    public String getLot() 
    {
        return (String)get_Value("Lot");
        
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
    
    /** Set Phys.Inventory.
    @param M_Inventory_ID Parameters for a Physical Inventory */
    public void setM_Inventory_ID (int M_Inventory_ID)
    {
        if (M_Inventory_ID <= 0) set_Value ("M_Inventory_ID", null);
        else
        set_Value ("M_Inventory_ID", Integer.valueOf(M_Inventory_ID));
        
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
        if (M_Locator_ID <= 0) set_Value ("M_Locator_ID", null);
        else
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
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
        else
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Set Movement Date.
    @param MovementDate Date a product was moved in or out of inventory */
    public void setMovementDate (Timestamp MovementDate)
    {
        set_Value ("MovementDate", MovementDate);
        
    }
    
    /** Get Movement Date.
    @return Date a product was moved in or out of inventory */
    public Timestamp getMovementDate() 
    {
        return (Timestamp)get_Value("MovementDate");
        
    }
    
    /** Set Position.
    @param Position Locator Position segment */
    public void setPosition (String Position)
    {
        set_Value ("Position", Position);
        
    }
    
    /** Get Position.
    @return Locator Position segment */
    public String getPosition() 
    {
        return (String)get_Value("Position");
        
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
    
    /** Set Product Key.
    @param ProductValue Key of the Product */
    public void setProductValue (String ProductValue)
    {
        set_Value ("ProductValue", ProductValue);
        
    }
    
    /** Get Product Key.
    @return Key of the Product */
    public String getProductValue() 
    {
        return (String)get_Value("ProductValue");
        
    }
    
    /** Set Quantity book.
    @param QtyBook Book Quantity */
    public void setQtyBook (java.math.BigDecimal QtyBook)
    {
        set_Value ("QtyBook", QtyBook);
        
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
        set_Value ("QtyCount", QtyCount);
        
    }
    
    /** Get Quantity count.
    @return Counted Quantity */
    public java.math.BigDecimal getQtyCount() 
    {
        return get_ValueAsBigDecimal("QtyCount");
        
    }
    
    /** Set Serial No.
    @param SerNo Product Serial Number */
    public void setSerNo (String SerNo)
    {
        set_Value ("SerNo", SerNo);
        
    }
    
    /** Get Serial No.
    @return Product Serial Number */
    public String getSerNo() 
    {
        return (String)get_Value("SerNo");
        
    }
    
    /** Set UPC/EAN.
    @param UPC Bar Code (Universal Product Code or its superset European Article Number) */
    public void setUPC (String UPC)
    {
        set_Value ("UPC", UPC);
        
    }
    
    /** Get UPC/EAN.
    @return Bar Code (Universal Product Code or its superset European Article Number) */
    public String getUPC() 
    {
        return (String)get_Value("UPC");
        
    }
    
    /** Set Warehouse Key.
    @param WarehouseValue Key of the Warehouse */
    public void setWarehouseValue (String WarehouseValue)
    {
        set_Value ("WarehouseValue", WarehouseValue);
        
    }
    
    /** Get Warehouse Key.
    @return Key of the Warehouse */
    public String getWarehouseValue() 
    {
        return (String)get_Value("WarehouseValue");
        
    }
    
    /** Set Aisle.
    @param X Locator Aisle segment */
    public void setX (String X)
    {
        set_Value ("X", X);
        
    }
    
    /** Get Aisle.
    @return Locator Aisle segment */
    public String getX() 
    {
        return (String)get_Value("X");
        
    }
    
    /** Set Bay.
    @param Y Locator Bay segment */
    public void setY (String Y)
    {
        set_Value ("Y", Y);
        
    }
    
    /** Get Bay.
    @return Locator Bay segment */
    public String getY() 
    {
        return (String)get_Value("Y");
        
    }
    
    /** Set Row.
    @param Z Row segment of locator */
    public void setZ (String Z)
    {
        set_Value ("Z", Z);
        
    }
    
    /** Get Row.
    @return Row segment of locator */
    public String getZ() 
    {
        return (String)get_Value("Z");
        
    }
    
    
}
