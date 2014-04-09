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
package compiere.model.cds;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VMR_InventoryLevels
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_InventoryLevels extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_InventoryLevels_ID id
    @param trx transaction
    */
    public X_XX_VMR_InventoryLevels (Ctx ctx, int XX_VMR_InventoryLevels_ID, Trx trx)
    {
        super (ctx, XX_VMR_InventoryLevels_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_InventoryLevels_ID == 0)
        {
            setXX_VMR_INVENTORYLEVELS_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_InventoryLevels (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27619406307789L;
    /** Last Updated Timestamp 2012-05-17 14:26:31.0 */
    public static final long updatedMS = 1337280991000L;
    /** AD_Table_ID=1002053 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_InventoryLevels");
        
    }
    ;
    
    /** TableName=XX_VMR_InventoryLevels */
    public static final String Table_Name="XX_VMR_InventoryLevels";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set approvedOC.
    @param approvedOC approvedOC */
    public void setapprovedOC (int approvedOC)
    {
        set_Value ("approvedOC", Integer.valueOf(approvedOC));
        
    }
    
    /** Get approvedOC.
    @return approvedOC */
    public int getapprovedOC() 
    {
        return get_ValueAsInt("approvedOC");
        
    }
    
    /** Set InventoryCD.
    @param InventoryCD InventoryCD */
    public void setInventoryCD (int InventoryCD)
    {
        set_Value ("InventoryCD", Integer.valueOf(InventoryCD));
        
    }
    
    /** Get InventoryCD.
    @return InventoryCD */
    public int getInventoryCD() 
    {
        return get_ValueAsInt("InventoryCD");
        
    }
    
    /** Set inventoryWarehouse.
    @param inventoryWarehouse inventoryWarehouse */
    public void setinventoryWarehouse (int inventoryWarehouse)
    {
        set_Value ("inventoryWarehouse", Integer.valueOf(inventoryWarehouse));
        
    }
    
    /** Get inventoryWarehouse.
    @return inventoryWarehouse */
    public int getinventoryWarehouse() 
    {
        return get_ValueAsInt("inventoryWarehouse");
        
    }
    
    /** Set Sales.
    @param Sales Sales */
    public void setSales (int Sales)
    {
        set_Value ("Sales", Integer.valueOf(Sales));
        
    }
    
    /** Get Sales.
    @return Sales */
    public int getSales() 
    {
        return get_ValueAsInt("Sales");
        
    }
    
    /** Set securityInventory.
    @param securityInventory securityInventory */
    public void setsecurityInventory (int securityInventory)
    {
        set_Value ("securityInventory", Integer.valueOf(securityInventory));
        
    }
    
    /** Get securityInventory.
    @return securityInventory */
    public int getsecurityInventory() 
    {
        return get_ValueAsInt("securityInventory");
        
    }
    
    /** Set XX_VMR_INVENTORYLEVELS_ID.
    @param XX_VMR_INVENTORYLEVELS_ID XX_VMR_INVENTORYLEVELS_ID */
    public void setXX_VMR_INVENTORYLEVELS_ID (int XX_VMR_INVENTORYLEVELS_ID)
    {
        if (XX_VMR_INVENTORYLEVELS_ID < 1) throw new IllegalArgumentException ("XX_VMR_INVENTORYLEVELS_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_INVENTORYLEVELS_ID", Integer.valueOf(XX_VMR_INVENTORYLEVELS_ID));
        
    }
    
    /** Get XX_VMR_INVENTORYLEVELS_ID.
    @return XX_VMR_INVENTORYLEVELS_ID */
    public int getXX_VMR_INVENTORYLEVELS_ID() 
    {
        return get_ValueAsInt("XX_VMR_INVENTORYLEVELS_ID");
        
    }
    
    /** Set Vendor Product Reference.
    @param XX_VMR_VendorProdRef_ID Vendor Product Reference */
    public void setXX_VMR_VendorProdRef_ID (int XX_VMR_VendorProdRef_ID)
    {
        if (XX_VMR_VendorProdRef_ID <= 0) set_Value ("XX_VMR_VendorProdRef_ID", null);
        else
        set_Value ("XX_VMR_VendorProdRef_ID", Integer.valueOf(XX_VMR_VendorProdRef_ID));
        
    }
    
    /** Get Vendor Product Reference.
    @return Vendor Product Reference */
    public int getXX_VMR_VendorProdRef_ID() 
    {
        return get_ValueAsInt("XX_VMR_VendorProdRef_ID");
        
    }
    
    
}
