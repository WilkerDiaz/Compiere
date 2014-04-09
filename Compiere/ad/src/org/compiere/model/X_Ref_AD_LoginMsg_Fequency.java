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


/** LoginMsgFrequency AD_Reference_ID=476 */
public enum X_Ref_AD_LoginMsg_Fequency 
{
    /** Daily = D */
    DAILY("D"),
    /** Once per Month = M */
    ONCE_PER_MONTH("M"),
    /** Once = O */
    ONCE("O"),
    /** Once per Week = W */
    ONCE_PER_WEEK("W");
    
    public static final int AD_Reference_ID=476;
    private final String value;
    private X_Ref_AD_LoginMsg_Fequency(String value)
    {
         this.value = value;
         
    }
    public String getValue() 
    {
         return this.value;
         
    }
    public static boolean isValid(String test) 
    {
         for( X_Ref_AD_LoginMsg_Fequency v : X_Ref_AD_LoginMsg_Fequency.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}