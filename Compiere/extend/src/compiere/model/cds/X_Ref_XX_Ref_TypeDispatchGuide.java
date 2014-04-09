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


/** XX_TypeDetailDispatchGuide AD_Reference_ID=1000240 */
public enum X_Ref_XX_Ref_TypeDispatchGuide 
{
    /** Store Returns = DEV */
    STORE_RETURNS("DEV"),
    /** Inventory Movement = IMV */
    INVENTORY_MOVEMENT("IMV"),
    /** Merchandise Distribution Center = MCD */
    MERCHANDISE_DISTRIBUTION_CENTER("MCD"),
    /** Other Goods = OTR */
    OTHER_GOODS("OTR"),
    /** Transfers between stores = TRA */
    TRANSFERS_BETWEEN_STORES("TRA");
    
    public static final int AD_Reference_ID=1000240;
    private final String value;
    private X_Ref_XX_Ref_TypeDispatchGuide(String value)
    {
         this.value = value;
         
    }
    public String getValue() 
    {
         return this.value;
         
    }
    public static boolean isValid(String test) 
    {
         if( test == null ) return true;
         for( X_Ref_XX_Ref_TypeDispatchGuide v : X_Ref_XX_Ref_TypeDispatchGuide.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
