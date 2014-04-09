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


/** ObscureType AD_Reference_ID=291 */
public enum X_Ref_AD_Field_ObscureType 
{
    /** Obscure Digits but last 4 = 904 */
    OBSCURE_DIGITS_BUT_LAST4("904"),
    /** Obscure Digits but first/last 4 = 944 */
    OBSCURE_DIGITS_BUT_FIRST_LAST4("944"),
    /** Obscure AlphaNumeric but last 4 = A04 */
    OBSCURE_ALPHA_NUMERIC_BUT_LAST4("A04"),
    /** Obscure AlphaNumeric but first/last 4 = A44 */
    OBSCURE_ALPHA_NUMERIC_BUT_FIRST_LAST4("A44");
    
    public static final int AD_Reference_ID=291;
    private final String value;
    private X_Ref_AD_Field_ObscureType(String value)
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
         for( X_Ref_AD_Field_ObscureType v : X_Ref_AD_Field_ObscureType.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
