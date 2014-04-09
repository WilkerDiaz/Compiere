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
/** Generated Model for XX_VLO_DetailDispatchGuide
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_DetailDispatchGuide extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_DetailDispatchGuide_ID id
    @param trx transaction
    */
    public X_XX_VLO_DetailDispatchGuide (Ctx ctx, int XX_VLO_DetailDispatchGuide_ID, Trx trx)
    {
        super (ctx, XX_VLO_DetailDispatchGuide_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_DetailDispatchGuide_ID == 0)
        {
            setXX_VLO_DetailDispatchGuide_ID (0);
            setXX_VLO_DispatchGuide_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_DetailDispatchGuide (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27631600510789L;
    /** Last Updated Timestamp 2012-10-05 17:43:14.0 */
    public static final long updatedMS = 1349475194000L;
    /** AD_Table_ID=1000291 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_DetailDispatchGuide");
        
    }
    ;
    
    /** TableName=XX_VLO_DetailDispatchGuide */
    public static final String Table_Name="XX_VLO_DetailDispatchGuide";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Qty Fully Allocated.
    @param FullyAllocated Total quantity allocated to a pick task or a shipment */
    public void setFullyAllocated (boolean FullyAllocated)
    {
        set_Value ("FullyAllocated", Boolean.valueOf(FullyAllocated));
        
    }
    
    /** Get Qty Fully Allocated.
    @return Total quantity allocated to a pick task or a shipment */
    public boolean isFullyAllocated() 
    {
        return get_ValueAsBoolean("FullyAllocated");
        
    }
    
    /** Set Movement.
    @param M_MovementM_ID Movement */
    public void setM_MovementM_ID (int M_MovementM_ID)
    {
        if (M_MovementM_ID <= 0) set_Value ("M_MovementM_ID", null);
        else
        set_Value ("M_MovementM_ID", Integer.valueOf(M_MovementM_ID));
        
    }
    
    /** Get Movement.
    @return Movement */
    public int getM_MovementM_ID() 
    {
        return get_ValueAsInt("M_MovementM_ID");
        
    }
    
    /** Set Inventory Move.
    @param M_MovementR_ID Movement of Inventory */
    public void setM_MovementR_ID (int M_MovementR_ID)
    {
        if (M_MovementR_ID <= 0) set_Value ("M_MovementR_ID", null);
        else
        set_Value ("M_MovementR_ID", Integer.valueOf(M_MovementR_ID));
        
    }
    
    /** Get Inventory Move.
    @return Movement of Inventory */
    public int getM_MovementR_ID() 
    {
        return get_ValueAsInt("M_MovementR_ID");
        
    }
    
    /** Set Inventory Move.
    @param M_MovementT_ID Movement of Inventory */
    public void setM_MovementT_ID (int M_MovementT_ID)
    {
        if (M_MovementT_ID <= 0) set_Value ("M_MovementT_ID", null);
        else
        set_Value ("M_MovementT_ID", Integer.valueOf(M_MovementT_ID));
        
    }
    
    /** Get Inventory Move.
    @return Movement of Inventory */
    public int getM_MovementT_ID() 
    {
        return get_ValueAsInt("M_MovementT_ID");
        
    }
    
    /** Set Packages Received.
    @param XX_PackagesReceived Packages Received */
    public void setXX_PackagesReceived (int XX_PackagesReceived)
    {
        set_Value ("XX_PackagesReceived", Integer.valueOf(XX_PackagesReceived));
        
    }
    
    /** Get Packages Received.
    @return Packages Received */
    public int getXX_PackagesReceived() 
    {
        return get_ValueAsInt("XX_PackagesReceived");
        
    }
    
    /** Set Packages Sent.
    @param XX_PackagesSent Packages Sent */
    public void setXX_PackagesSent (int XX_PackagesSent)
    {
        set_ValueNoCheck ("XX_PackagesSent", Integer.valueOf(XX_PackagesSent));
        
    }
    
    /** Get Packages Sent.
    @return Packages Sent */
    public int getXX_PackagesSent() 
    {
        return get_ValueAsInt("XX_PackagesSent");
        
    }
    
    /** Set Placed Order Packages.
    @param XX_PlacedOrderPackages Placed Order Packages */
    public void setXX_PlacedOrderPackages (int XX_PlacedOrderPackages)
    {
        set_Value ("XX_PlacedOrderPackages", Integer.valueOf(XX_PlacedOrderPackages));
        
    }
    
    /** Get Placed Order Packages.
    @return Placed Order Packages */
    public int getXX_PlacedOrderPackages() 
    {
        return get_ValueAsInt("XX_PlacedOrderPackages");
        
    }
    
    /** Set Quantity Received.
    @param XX_QuantityReceived Quantity Received */
    public void setXX_QuantityReceived (int XX_QuantityReceived)
    {
        set_Value ("XX_QuantityReceived", Integer.valueOf(XX_QuantityReceived));
        
    }
    
    /** Get Quantity Received.
    @return Quantity Received */
    public int getXX_QuantityReceived() 
    {
        return get_ValueAsInt("XX_QuantityReceived");
        
    }
    
    /** Set Quantity Sent.
    @param XX_QuantitySent Quantity Sent */
    public void setXX_QuantitySent (int XX_QuantitySent)
    {
        set_Value ("XX_QuantitySent", Integer.valueOf(XX_QuantitySent));
        
    }
    
    /** Get Quantity Sent.
    @return Quantity Sent */
    public int getXX_QuantitySent() 
    {
        return get_ValueAsInt("XX_QuantitySent");
        
    }
    
    /** Set Remaining Packages.
    @param XX_RemainingPackages Remaining Packages */
    public void setXX_RemainingPackages (int XX_RemainingPackages)
    {
        set_Value ("XX_RemainingPackages", Integer.valueOf(XX_RemainingPackages));
        
    }
    
    /** Get Remaining Packages.
    @return Remaining Packages */
    public int getXX_RemainingPackages() 
    {
        return get_ValueAsInt("XX_RemainingPackages");
        
    }
    
    /** Store Returns = DEV */
    public static final String XX_TYPEDETAILDISPATCHGUIDE_StoreReturns = X_Ref_XX_Ref_TypeDispatchGuide.STORE_RETURNS.getValue();
    /** Inventory Movement = IMV */
    public static final String XX_TYPEDETAILDISPATCHGUIDE_InventoryMovement = X_Ref_XX_Ref_TypeDispatchGuide.INVENTORY_MOVEMENT.getValue();
    /** Merchandise Distribution Center = MCD */
    public static final String XX_TYPEDETAILDISPATCHGUIDE_MerchandiseDistributionCenter = X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue();
    /** Other Goods = OTR */
    public static final String XX_TYPEDETAILDISPATCHGUIDE_OtherGoods = X_Ref_XX_Ref_TypeDispatchGuide.OTHER_GOODS.getValue();
    /** Transfers between stores = TRA */
    public static final String XX_TYPEDETAILDISPATCHGUIDE_TransfersBetweenStores = X_Ref_XX_Ref_TypeDispatchGuide.TRANSFERS_BETWEEN_STORES.getValue();
    /** Set Type of Detail on Dispatch Guide.
    @param XX_TypeDetailDispatchGuide Type of Detail on Dispatch Guide */
    public void setXX_TypeDetailDispatchGuide (String XX_TypeDetailDispatchGuide)
    {
        if (!X_Ref_XX_Ref_TypeDispatchGuide.isValid(XX_TypeDetailDispatchGuide))
        throw new IllegalArgumentException ("XX_TypeDetailDispatchGuide Invalid value - " + XX_TypeDetailDispatchGuide + " - Reference_ID=1000240 - DEV - IMV - MCD - OTR - TRA");
        set_Value ("XX_TypeDetailDispatchGuide", XX_TypeDetailDispatchGuide);
        
    }
    
    /** Get Type of Detail on Dispatch Guide.
    @return Type of Detail on Dispatch Guide */
    public String getXX_TypeDetailDispatchGuide() 
    {
        return (String)get_Value("XX_TypeDetailDispatchGuide");
        
    }
    
    /** Set XX_VLO_DetailDispatchGuide_ID.
    @param XX_VLO_DetailDispatchGuide_ID XX_VLO_DetailDispatchGuide_ID */
    public void setXX_VLO_DetailDispatchGuide_ID (int XX_VLO_DetailDispatchGuide_ID)
    {
        if (XX_VLO_DetailDispatchGuide_ID < 1) throw new IllegalArgumentException ("XX_VLO_DetailDispatchGuide_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_DetailDispatchGuide_ID", Integer.valueOf(XX_VLO_DetailDispatchGuide_ID));
        
    }
    
    /** Get XX_VLO_DetailDispatchGuide_ID.
    @return XX_VLO_DetailDispatchGuide_ID */
    public int getXX_VLO_DetailDispatchGuide_ID() 
    {
        return get_ValueAsInt("XX_VLO_DetailDispatchGuide_ID");
        
    }
    
    /** Set Dispatch Guide.
    @param XX_VLO_DispatchGuide_ID Dispatch Guide */
    public void setXX_VLO_DispatchGuide_ID (int XX_VLO_DispatchGuide_ID)
    {
        if (XX_VLO_DispatchGuide_ID < 1) throw new IllegalArgumentException ("XX_VLO_DispatchGuide_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_DispatchGuide_ID", Integer.valueOf(XX_VLO_DispatchGuide_ID));
        
    }
    
    /** Get Dispatch Guide.
    @return Dispatch Guide */
    public int getXX_VLO_DispatchGuide_ID() 
    {
        return get_ValueAsInt("XX_VLO_DispatchGuide_ID");
        
    }
    
    /** Set Placed Order.
    @param XX_VMR_Order_ID Placed Order */
    public void setXX_VMR_Order_ID (int XX_VMR_Order_ID)
    {
        if (XX_VMR_Order_ID <= 0) set_ValueNoCheck ("XX_VMR_Order_ID", null);
        else
        set_ValueNoCheck ("XX_VMR_Order_ID", Integer.valueOf(XX_VMR_Order_ID));
        
    }
    
    /** Get Placed Order.
    @return Placed Order */
    public int getXX_VMR_Order_ID() 
    {
        return get_ValueAsInt("XX_VMR_Order_ID");
        
    }
    
    
}
