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


/** MailMsgType AD_Reference_ID=342 */
public enum X_Ref_W_MailMsg_Type 
{
    /** Subscribe = LS */
    SUBSCRIBE("LS"),
    /** Unsubscribe = LU */
    UNSUBSCRIBE("LU"),
    /** Order Acknowledgement = OA */
    ORDER_ACKNOWLEDGEMENT("OA"),
    /** Payment Acknowledgement = PA */
    PAYMENT_ACKNOWLEDGEMENT("PA"),
    /** Payment Error = PE */
    PAYMENT_ERROR("PE"),
    /** User Account = UA */
    USER_ACCOUNT("UA"),
    /** User Password = UP */
    USER_PASSWORD("UP"),
    /** User Verification = UV */
    USER_VERIFICATION("UV"),
    /** Request = WR */
    REQUEST("WR");
    
    public static final int AD_Reference_ID=342;
    private final String value;
    private X_Ref_W_MailMsg_Type(String value)
    {
         this.value = value;
         
    }
    public String getValue() 
    {
         return this.value;
         
    }
    public static boolean isValid(String test) 
    {
         for( X_Ref_W_MailMsg_Type v : X_Ref_W_MailMsg_Type.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
