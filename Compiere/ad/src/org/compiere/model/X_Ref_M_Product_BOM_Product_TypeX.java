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


/** BOMType AD_Reference_ID=279 */
public enum X_Ref_M_Product_BOM_Product_TypeX 
{
    /** In alternative Group 1 = 1 */
    IN_ALTERNATIVE_GROUP1("1"),
    /** In alternative Group 2 = 2 */
    IN_ALTERNATIVE_GROUP2("2"),
    /** In alternative Group 3 = 3 */
    IN_ALTERNATIVE_GROUP3("3"),
    /** In alternative Group 4 = 4 */
    IN_ALTERNATIVE_GROUP4("4"),
    /** In alternative Group 5 = 5 */
    IN_ALTERNATIVE_GROUP5("5"),
    /** In alternative Group 6 = 6 */
    IN_ALTERNATIVE_GROUP6("6"),
    /** In alternative Group 7 = 7 */
    IN_ALTERNATIVE_GROUP7("7"),
    /** In alternative Group 8 = 8 */
    IN_ALTERNATIVE_GROUP8("8"),
    /** In alternative Group 9 = 9 */
    IN_ALTERNATIVE_GROUP9("9"),
    /** Optional Part = O */
    OPTIONAL_PART("O"),
    /** Standard Part = P */
    STANDARD_PART("P");
    
    public static final int AD_Reference_ID=279;
    private final String value;
    private X_Ref_M_Product_BOM_Product_TypeX(String value)
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
         for( X_Ref_M_Product_BOM_Product_TypeX v : X_Ref_M_Product_BOM_Product_TypeX.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
