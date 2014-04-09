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


/** MovementType AD_Reference_ID=189 */
public enum X_Ref_M_Transaction_Movement_Type 
{
    /** Customer Returns = C+ */
    CUSTOMER_RETURNS("C+"),
    /** Customer Shipment = C- */
    CUSTOMER_SHIPMENT("C-"),
    /** Inventory In = I+ */
    INVENTORY_IN("I+"),
    /** Inventory Out = I- */
    INVENTORY_OUT("I-"),
    /** Movement To = M+ */
    MOVEMENT_TO("M+"),
    /** Movement From = M- */
    MOVEMENT_FROM("M-"),
    /** Production + = P+ */
    PRODUCTION_PLUS("P+"),
    /** Production - = P- */
    PRODUCTION_("P-"),
    /** Vendor Receipts = V+ */
    VENDOR_RECEIPTS("V+"),
    /** Vendor Returns = V- */
    VENDOR_RETURNS("V-"),
    /** Work Order + = W+ */
    WORK_ORDER_PLUS("W+"),
    /** Work Order - = W- */
    WORK_ORDER_("W-");
    
    public static final int AD_Reference_ID=189;
    private final String value;
    private X_Ref_M_Transaction_Movement_Type(String value)
    {
         this.value = value;
         
    }
    public String getValue() 
    {
         return this.value;
         
    }
    public static boolean isValid(String test) 
    {
         for( X_Ref_M_Transaction_Movement_Type v : X_Ref_M_Transaction_Movement_Type.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
