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


/** PaymentRule AD_Reference_ID=195 */
public enum X_Ref__Payment_Rule 
{
	/** Transfer = A */
    TRANSFER("A"),
    /** Cash = B */
    CASH("B"),
    /** Direct Debit = D */
    DIRECT_DEBIT("D"),
    /** Draft = G */
    DRAFT("G"),
    /** Credit Card = K */
    CREDIT_CARD("K"),
    /** On Credit = P */
    ON_CREDIT("P"),
    /** Check = S */
    CHECK("S"),
    /** Direct Deposit = T */
    DIRECT_DEPOSIT("T");
    
    public static final int AD_Reference_ID=195;
    private final String value;
    private X_Ref__Payment_Rule(String value)
    {
         this.value = value;
         
    }
    public String getValue() 
    {
         return this.value;
         
    }
    public static boolean isValid(String test) 
    {
         for( X_Ref__Payment_Rule v : X_Ref__Payment_Rule.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
