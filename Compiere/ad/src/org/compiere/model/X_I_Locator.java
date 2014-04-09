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
/** Generated Model for I_Locator
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_I_Locator.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_I_Locator extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_Locator_ID id
    @param trx transaction
    */
    public X_I_Locator (Ctx ctx, int I_Locator_ID, Trx trx)
    {
        super (ctx, I_Locator_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_Locator_ID == 0)
        {
            setI_IsImported (null);	// N
            setI_Locator_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_Locator (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27501564842789L;
    /** Last Updated Timestamp 2008-08-22 14:12:06.0 */
    public static final long updatedMS = 1219439526000L;
    /** AD_Table_ID=1014 */
    public static final int Table_ID=1014;
    
    /** TableName=I_Locator */
    public static final String Table_Name="I_Locator";
    
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
    
    /** Set Tenant Name.
    @param ClientName Name of the Tenant */
    public void setClientName (String ClientName)
    {
        set_Value ("ClientName", ClientName);
        
    }
    
    /** Get Tenant Name.
    @return Name of the Tenant */
    public String getClientName() 
    {
        return (String)get_Value("ClientName");
        
    }
    
    /** Set Tenant Key.
    @param ClientValue Key of the Tenant */
    public void setClientValue (String ClientValue)
    {
        set_Value ("ClientValue", ClientValue);
        
    }
    
    /** Get Tenant Key.
    @return Key of the Tenant */
    public String getClientValue() 
    {
        return (String)get_Value("ClientValue");
        
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
    
    /** Set Import Locator.
    @param I_Locator_ID Import Warehouse Locators */
    public void setI_Locator_ID (int I_Locator_ID)
    {
        if (I_Locator_ID < 1) throw new IllegalArgumentException ("I_Locator_ID is mandatory.");
        set_ValueNoCheck ("I_Locator_ID", Integer.valueOf(I_Locator_ID));
        
    }
    
    /** Get Import Locator.
    @return Import Warehouse Locators */
    public int getI_Locator_ID() 
    {
        return get_ValueAsInt("I_Locator_ID");
        
    }
    
    /** Set Available For Allocation.
    @param IsAvailableForAllocation Stock in this locator is available for allocation */
    public void setIsAvailableForAllocation (boolean IsAvailableForAllocation)
    {
        set_Value ("IsAvailableForAllocation", Boolean.valueOf(IsAvailableForAllocation));
        
    }
    
    /** Get Available For Allocation.
    @return Stock in this locator is available for allocation */
    public boolean isAvailableForAllocation() 
    {
        return get_ValueAsBoolean("IsAvailableForAllocation");
        
    }
    
    /** Set Available To Promise.
    @param IsAvailableToPromise Stock in this locator is available to promise */
    public void setIsAvailableToPromise (boolean IsAvailableToPromise)
    {
        set_Value ("IsAvailableToPromise", Boolean.valueOf(IsAvailableToPromise));
        
    }
    
    /** Get Available To Promise.
    @return Stock in this locator is available to promise */
    public boolean isAvailableToPromise() 
    {
        return get_ValueAsBoolean("IsAvailableToPromise");
        
    }
    
    /** Set Default.
    @param IsDefault Default value */
    public void setIsDefault (boolean IsDefault)
    {
        set_Value ("IsDefault", Boolean.valueOf(IsDefault));
        
    }
    
    /** Get Default.
    @return Default value */
    public boolean isDefault() 
    {
        return get_ValueAsBoolean("IsDefault");
        
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
    
    /** Set Max Stocking Quantity.
    @param MaxQuantity Maximum stocking capacity of the locator in units */
    public void setMaxQuantity (java.math.BigDecimal MaxQuantity)
    {
        set_Value ("MaxQuantity", MaxQuantity);
        
    }
    
    /** Get Max Stocking Quantity.
    @return Maximum stocking capacity of the locator in units */
    public java.math.BigDecimal getMaxQuantity() 
    {
        return get_ValueAsBigDecimal("MaxQuantity");
        
    }
    
    /** Set Min Stocking Quantity.
    @param MinQuantity Minimum stocking quantity of locator in units */
    public void setMinQuantity (java.math.BigDecimal MinQuantity)
    {
        set_Value ("MinQuantity", MinQuantity);
        
    }
    
    /** Get Min Stocking Quantity.
    @return Minimum stocking quantity of locator in units */
    public java.math.BigDecimal getMinQuantity() 
    {
        return get_ValueAsBigDecimal("MinQuantity");
        
    }
    
    /** Set Organization Name.
    @param OrgName Name of the Organization */
    public void setOrgName (String OrgName)
    {
        set_Value ("OrgName", OrgName);
        
    }
    
    /** Get Organization Name.
    @return Name of the Organization */
    public String getOrgName() 
    {
        return (String)get_Value("OrgName");
        
    }
    
    /** Set Organization Key.
    @param OrgValue Key of the Organization */
    public void setOrgValue (String OrgValue)
    {
        set_Value ("OrgValue", OrgValue);
        
    }
    
    /** Get Organization Key.
    @return Key of the Organization */
    public String getOrgValue() 
    {
        return (String)get_Value("OrgValue");
        
    }
    
    /** Set Picking Sequence No.
    @param PickingSeqNo Picking Sequence Number of locator/zone */
    public void setPickingSeqNo (int PickingSeqNo)
    {
        set_Value ("PickingSeqNo", Integer.valueOf(PickingSeqNo));
        
    }
    
    /** Get Picking Sequence No.
    @return Picking Sequence Number of locator/zone */
    public int getPickingSeqNo() 
    {
        return get_ValueAsInt("PickingSeqNo");
        
    }
    
    /** Set Picking UOM Name.
    @param PickingUOMName UOM Name of Picking UOM */
    public void setPickingUOMName (String PickingUOMName)
    {
        set_Value ("PickingUOMName", PickingUOMName);
        
    }
    
    /** Get Picking UOM Name.
    @return UOM Name of Picking UOM */
    public String getPickingUOMName() 
    {
        return (String)get_Value("PickingUOMName");
        
    }
    
    /** Set Picking UOM Symbol.
    @param PickingUOMSymbol UOM Symbol of Picking UOM */
    public void setPickingUOMSymbol (String PickingUOMSymbol)
    {
        set_Value ("PickingUOMSymbol", PickingUOMSymbol);
        
    }
    
    /** Get Picking UOM Symbol.
    @return UOM Symbol of Picking UOM */
    public String getPickingUOMSymbol() 
    {
        return (String)get_Value("PickingUOMSymbol");
        
    }
    
    /** Set Picking UOM.
    @param Picking_UOM_ID Picking UOM of locator */
    public void setPicking_UOM_ID (int Picking_UOM_ID)
    {
        if (Picking_UOM_ID <= 0) set_Value ("Picking_UOM_ID", null);
        else
        set_Value ("Picking_UOM_ID", Integer.valueOf(Picking_UOM_ID));
        
    }
    
    /** Get Picking UOM.
    @return Picking UOM of locator */
    public int getPicking_UOM_ID() 
    {
        return get_ValueAsInt("Picking_UOM_ID");
        
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
    
    /** Set Relative Priority.
    @param PriorityNo Where inventory should be picked from first */
    public void setPriorityNo (int PriorityNo)
    {
        set_Value ("PriorityNo", Integer.valueOf(PriorityNo));
        
    }
    
    /** Get Relative Priority.
    @return Where inventory should be picked from first */
    public int getPriorityNo() 
    {
        return get_ValueAsInt("PriorityNo");
        
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
    
    /** Set Max Stocking Quantity.
    @param ProductMaxQuantity Maximum stocking capacity of the locator for this product in units */
    public void setProductMaxQuantity (java.math.BigDecimal ProductMaxQuantity)
    {
        set_Value ("ProductMaxQuantity", ProductMaxQuantity);
        
    }
    
    /** Get Max Stocking Quantity.
    @return Maximum stocking capacity of the locator for this product in units */
    public java.math.BigDecimal getProductMaxQuantity() 
    {
        return get_ValueAsBigDecimal("ProductMaxQuantity");
        
    }
    
    /** Set Min Stocking Quantity.
    @param ProductMinQuantity Minimum stocking quantity of locator for the product in units */
    public void setProductMinQuantity (java.math.BigDecimal ProductMinQuantity)
    {
        set_Value ("ProductMinQuantity", ProductMinQuantity);
        
    }
    
    /** Get Min Stocking Quantity.
    @return Minimum stocking quantity of locator for the product in units */
    public java.math.BigDecimal getProductMinQuantity() 
    {
        return get_ValueAsBigDecimal("ProductMinQuantity");
        
    }
    
    /** Set Product Name.
    @param ProductName Name of the Product */
    public void setProductName (String ProductName)
    {
        set_Value ("ProductName", ProductName);
        
    }
    
    /** Get Product Name.
    @return Name of the Product */
    public String getProductName() 
    {
        return (String)get_Value("ProductName");
        
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
    
    /** Set Putaway Sequence No.
    @param PutawaySeqNo Putaway Sequence Number of locator/zone */
    public void setPutawaySeqNo (int PutawaySeqNo)
    {
        set_Value ("PutawaySeqNo", Integer.valueOf(PutawaySeqNo));
        
    }
    
    /** Get Putaway Sequence No.
    @return Putaway Sequence Number of locator/zone */
    public int getPutawaySeqNo() 
    {
        return get_ValueAsInt("PutawaySeqNo");
        
    }
    
    /** Set Stocking UOM Name.
    @param StockingUOMName UOM Name of Stocking UOM */
    public void setStockingUOMName (String StockingUOMName)
    {
        set_Value ("StockingUOMName", StockingUOMName);
        
    }
    
    /** Get Stocking UOM Name.
    @return UOM Name of Stocking UOM */
    public String getStockingUOMName() 
    {
        return (String)get_Value("StockingUOMName");
        
    }
    
    /** Set Stocking UOM Symbol.
    @param StockingUOMSymbol Stocking UOM Symbol */
    public void setStockingUOMSymbol (String StockingUOMSymbol)
    {
        set_Value ("StockingUOMSymbol", StockingUOMSymbol);
        
    }
    
    /** Get Stocking UOM Symbol.
    @return Stocking UOM Symbol */
    public String getStockingUOMSymbol() 
    {
        return (String)get_Value("StockingUOMSymbol");
        
    }
    
    /** Set Stocking UOM.
    @param Stocking_UOM_ID Stocking UOM of locator */
    public void setStocking_UOM_ID (int Stocking_UOM_ID)
    {
        if (Stocking_UOM_ID <= 0) set_Value ("Stocking_UOM_ID", null);
        else
        set_Value ("Stocking_UOM_ID", Integer.valueOf(Stocking_UOM_ID));
        
    }
    
    /** Get Stocking UOM.
    @return Stocking UOM of locator */
    public int getStocking_UOM_ID() 
    {
        return get_ValueAsInt("Stocking_UOM_ID");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Warehouse Name.
    @param WarehouseName Warehouse Name */
    public void setWarehouseName (String WarehouseName)
    {
        set_Value ("WarehouseName", WarehouseName);
        
    }
    
    /** Get Warehouse Name.
    @return Warehouse Name */
    public String getWarehouseName() 
    {
        return (String)get_Value("WarehouseName");
        
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
