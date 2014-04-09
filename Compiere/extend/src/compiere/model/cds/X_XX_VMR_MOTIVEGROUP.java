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
/** Generated Model for XX_VMR_MOTIVEGROUP
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_MOTIVEGROUP extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_MOTIVEGROUP_ID id
    @param trx transaction
    */
    public X_XX_VMR_MOTIVEGROUP (Ctx ctx, int XX_VMR_MOTIVEGROUP_ID, Trx trx)
    {
        super (ctx, XX_VMR_MOTIVEGROUP_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_MOTIVEGROUP_ID == 0)
        {
            setXX_Use (null);
            setXX_VMR_MOTIVEGROUP_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_MOTIVEGROUP (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27550803149789L;
    /** Last Updated Timestamp 2010-03-15 14:00:33.0 */
    public static final long updatedMS = 1268677833000L;
    /** AD_Table_ID=1000273 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_MOTIVEGROUP");
        
    }
    ;
    
    /** TableName=XX_VMR_MOTIVEGROUP */
    public static final String Table_Name="XX_VMR_MOTIVEGROUP";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Inactive Business Partner = CBPARTNER */
    public static final String XX_USE_InactiveBusinessPartner = X_Ref_XX_SourceCancellationMotive.INACTIVE_BUSINESS_PARTNER.getValue();
    /** Change Estimated Date Purchase Order = CORDCHANGE */
    public static final String XX_USE_ChangeEstimatedDatePurchaseOrder = X_Ref_XX_SourceCancellationMotive.CHANGE_ESTIMATED_DATE_PURCHASE_ORDER.getValue();
    /** Anull Purchase Order  = CORDER */
    public static final String XX_USE_AnullPurchaseOrder = X_Ref_XX_SourceCancellationMotive.ANULL_PURCHASE_ORDER.getValue();
    /** Entrance Fiscal Data Rejection Motives = ENTRANCE */
    public static final String XX_USE_EntranceFiscalDataRejectionMotives = X_Ref_XX_SourceCancellationMotive.ENTRANCE_FISCAL_DATA_REJECTION_MOTIVES.getValue();
    /** Placed Order Modification Motive = PORDER */
    public static final String XX_USE_PlacedOrderModificationMotive = X_Ref_XX_SourceCancellationMotive.PLACED_ORDER_MODIFICATION_MOTIVE.getValue();
    /** Motives for Return of Products to CD = RETURNCD */
    public static final String XX_USE_MotivesForReturnOfProductsToCD = X_Ref_XX_SourceCancellationMotive.MOTIVES_FOR_RETURN_OF_PRODUCTS_TO_CD.getValue();
    /** Motives for Product Returns  = RETURNM */
    public static final String XX_USE_MotivesForProductReturns = X_Ref_XX_SourceCancellationMotive.MOTIVES_FOR_PRODUCT_RETURNS.getValue();
    /** Motives for Transfer of Products between Stores = TRANSFERCD */
    public static final String XX_USE_MotivesForTransferOfProductsBetweenStores = X_Ref_XX_SourceCancellationMotive.MOTIVES_FOR_TRANSFER_OF_PRODUCTS_BETWEEN_STORES.getValue();
    /** Inactive Vendor Category = VENDORCAT */
    public static final String XX_USE_InactiveVendorCategory = X_Ref_XX_SourceCancellationMotive.INACTIVE_VENDOR_CATEGORY.getValue();
    /** Set Use.
    @param XX_Use Use */
    public void setXX_Use (String XX_Use)
    {
        if (XX_Use == null) throw new IllegalArgumentException ("XX_Use is mandatory");
        if (!X_Ref_XX_SourceCancellationMotive.isValid(XX_Use))
        throw new IllegalArgumentException ("XX_Use Invalid value - " + XX_Use + " - Reference_ID=1000174 - CBPARTNER - CORDCHANGE - CORDER - ENTRANCE - PORDER - RETURNCD - RETURNM - TRANSFERCD - VENDORCAT");
        set_Value ("XX_Use", XX_Use);
        
    }
    
    /** Get Use.
    @return Use */
    public String getXX_Use() 
    {
        return (String)get_Value("XX_Use");
        
    }
    
    /** Set XX_VMR_MOTIVEGROUP_ID.
    @param XX_VMR_MOTIVEGROUP_ID XX_VMR_MOTIVEGROUP_ID */
    public void setXX_VMR_MOTIVEGROUP_ID (int XX_VMR_MOTIVEGROUP_ID)
    {
        if (XX_VMR_MOTIVEGROUP_ID < 1) throw new IllegalArgumentException ("XX_VMR_MOTIVEGROUP_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_MOTIVEGROUP_ID", Integer.valueOf(XX_VMR_MOTIVEGROUP_ID));
        
    }
    
    /** Get XX_VMR_MOTIVEGROUP_ID.
    @return XX_VMR_MOTIVEGROUP_ID */
    public int getXX_VMR_MOTIVEGROUP_ID() 
    {
        return get_ValueAsInt("XX_VMR_MOTIVEGROUP_ID");
        
    }
    
    
}
