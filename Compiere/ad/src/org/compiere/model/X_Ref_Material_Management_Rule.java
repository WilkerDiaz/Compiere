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


/** Rule AD_Reference_ID=456 */
public enum X_Ref_Material_Management_Rule 
{
    /** Find a fixed locator = MMA */
    FIND_A_FIXED_LOCATOR("MMA"),
    /** Find a floating locator with the same product = MMB */
    FIND_A_FLOATING_LOCATOR_WITH_THE_SAME_PRODUCT("MMB"),
    /** Find any locator with available capacity = MMC */
    FIND_ANY_LOCATOR_WITH_AVAILABLE_CAPACITY("MMC"),
    /** Find item in the warehouse = MMD */
    FIND_ITEM_IN_THE_WAREHOUSE("MMD"),
    /** Find item in the warehouse (Pick to clean) = MME */
    FIND_ITEM_IN_THE_WAREHOUSE_PICK_TO_CLEAN("MME"),
    /** Find any empty floating locator = MMF */
    FIND_ANY_EMPTY_FLOATING_LOCATOR("MMF"),
    /** Hard allocate purchased to order products = MMH */
    HARD_ALLOCATE_PURCHASED_TO_ORDER_PRODUCTS("MMH"),
    /** Use custom class = MMZ */
    USE_CUSTOM_CLASS("MMZ");
    
    public static final int AD_Reference_ID=456;
    private final String value;
    private X_Ref_Material_Management_Rule(String value)
    {
         this.value = value;
         
    }
    public String getValue() 
    {
         return this.value;
         
    }
    public static boolean isValid(String test) 
    {
         for( X_Ref_Material_Management_Rule v : X_Ref_Material_Management_Rule.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
