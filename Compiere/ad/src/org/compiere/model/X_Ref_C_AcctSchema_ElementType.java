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


/** ElementType AD_Reference_ID=181 */
public enum X_Ref_C_AcctSchema_ElementType 
{
    /** Account = AC */
    ACCOUNT("AC"),
    /** Activity = AY */
    ACTIVITY("AY"),
    /** BPartner = BP */
    B_PARTNER("BP"),
    /** Location From = LF */
    LOCATION_FROM("LF"),
    /** Location To = LT */
    LOCATION_TO("LT"),
    /** Campaign = MC */
    CAMPAIGN("MC"),
    /** Organization = OO */
    ORGANIZATION("OO"),
    /** Org Trx = OT */
    ORG_TRX("OT"),
    /** Project = PJ */
    PROJECT("PJ"),
    /** Product = PR */
    PRODUCT("PR"),
    /** Sub Account = SA */
    SUB_ACCOUNT("SA"),
    /** Sales Region = SR */
    SALES_REGION("SR"),
    /** User List 1 = U1 */
    USER_LIST1("U1"),
    /** User List 2 = U2 */
    USER_LIST2("U2"),
    /** User Element 1 = X1 */
    USER_ELEMENT1("X1"),
    /** User Element 2 = X2 */
    USER_ELEMENT2("X2");
    
    public static final int AD_Reference_ID=181;
    private final String value;
    private X_Ref_C_AcctSchema_ElementType(String value)
    {
         this.value = value;
         
    }
    public String getValue() 
    {
         return this.value;
         
    }
    public static boolean isValid(String test) 
    {
         for( X_Ref_C_AcctSchema_ElementType v : X_Ref_C_AcctSchema_ElementType.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
