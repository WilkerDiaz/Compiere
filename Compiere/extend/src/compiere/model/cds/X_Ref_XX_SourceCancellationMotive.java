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


/** XX_CancellationCode AD_Reference_ID=1000174 */
public enum X_Ref_XX_SourceCancellationMotive 
{
    /** Inactive Business Partner = CBPARTNER */
    INACTIVE_BUSINESS_PARTNER("CBPARTNER"),
    /** Change Estimated Date Purchase Order = CORDCHANGE */
    CHANGE_ESTIMATED_DATE_PURCHASE_ORDER("CORDCHANGE"),
    /** Anull Purchase Order  = CORDER */
    ANULL_PURCHASE_ORDER("CORDER"),
    /** Entrance Fiscal Data Rejection Motives = ENTRANCE */
    ENTRANCE_FISCAL_DATA_REJECTION_MOTIVES("ENTRANCE"),
    /** Placed Order Modification Motive = PORDER */
    PLACED_ORDER_MODIFICATION_MOTIVE("PORDER"),
    /** Motives for Return of Products to CD = RETURNCD */
    MOTIVES_FOR_RETURN_OF_PRODUCTS_TO_CD("RETURNCD"),
    /** Motives for Product Returns  = RETURNM */
    MOTIVES_FOR_PRODUCT_RETURNS("RETURNM"),
    /** Motives for Transfer of Products between Stores = TRANSFERCD */
    MOTIVES_FOR_TRANSFER_OF_PRODUCTS_BETWEEN_STORES("TRANSFERCD"),
    /** Inactive Vendor Category = VENDORCAT */
    INACTIVE_VENDOR_CATEGORY("VENDORCAT");
    
    public static final int AD_Reference_ID=1000174;
    private final String value;
    private X_Ref_XX_SourceCancellationMotive(String value)
    {
         this.value = value;
         
    }
    public String getValue() 
    {
         return this.value;
         
    }
    public static boolean isValid(String test) 
    {
         for( X_Ref_XX_SourceCancellationMotive v : X_Ref_XX_SourceCancellationMotive.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
