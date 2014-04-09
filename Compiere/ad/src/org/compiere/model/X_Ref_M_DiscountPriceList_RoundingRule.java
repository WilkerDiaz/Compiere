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


/** Std_Rounding AD_Reference_ID=155 */
public enum X_Ref_M_DiscountPriceList_RoundingRule 
{
    /** Whole Number .00 = 0 */
    WHOLE_NUMBER00("0"),
    /** Nickel .05, .10, .15, ... = 5 */
    NICKEL051015("5"),
    /** Currency Precision = C */
    CURRENCY_PRECISION("C"),
    /** Dime .10, .20, .30... = D */
    DIME102030("D"),
    /** No Rounding = N */
    NO_ROUNDING("N"),
    /** Quarter .25 .50 .75 = Q */
    QUARTER255075("Q"),
    /** Ten 10.00, 20.00, .. = T */
    TEN10002000("T"),
    /** Hundred = h */
    HUNDRED("h"),
    /** Thousand = t */
    THOUSAND("t");
    
    public static final int AD_Reference_ID=155;
    private final String value;
    private X_Ref_M_DiscountPriceList_RoundingRule(String value)
    {
         this.value = value;
         
    }
    public String getValue() 
    {
         return this.value;
         
    }
    public static boolean isValid(String test) 
    {
         for( X_Ref_M_DiscountPriceList_RoundingRule v : X_Ref_M_DiscountPriceList_RoundingRule.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
