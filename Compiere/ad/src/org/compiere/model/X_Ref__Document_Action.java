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


/** DocAction AD_Reference_ID=135 */
public enum X_Ref__Document_Action 
{
    /** <None> = -- */
    NONE("--"),
    /** Approve = AP */
    APPROVE("AP"),
    /** Close = CL */
    CLOSE("CL"),
    /** Complete = CO */
    COMPLETE("CO"),
    /** Invalidate = IN */
    INVALIDATE("IN"),
    /** Post = PO */
    POST("PO"),
    /** Prepare = PR */
    PREPARE("PR"),
    /** Reverse - Accrual = RA */
    REVERSE__ACCRUAL("RA"),
    /** Reverse - Correct = RC */
    REVERSE__CORRECT("RC"),
    /** Re-activate = RE */
    RE__ACTIVATE("RE"),
    /** Reject = RJ */
    REJECT("RJ"),
    /** Void = VO */
    VOID("VO"),
    /** Wait Complete = WC */
    WAIT_COMPLETE("WC"),
    /** Unlock = XL */
    UNLOCK("XL");
    
    public static final int AD_Reference_ID=135;
    private final String value;
    private X_Ref__Document_Action(String value)
    {
         this.value = value;
         
    }
    public String getValue() 
    {
         return this.value;
         
    }
    public static boolean isValid(String test) 
    {
         for( X_Ref__Document_Action v : X_Ref__Document_Action.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
