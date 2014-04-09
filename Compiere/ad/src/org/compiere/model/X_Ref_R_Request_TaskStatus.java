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


/** TaskStatus AD_Reference_ID=366 */
public enum X_Ref_R_Request_TaskStatus 
{
    /** 0% Not Started = 0 */
    _0_NOT_STARTED("0"),
    /** 20% Started = 2 */
    _20_STARTED("2"),
    /** 40% Busy = 4 */
    _40_BUSY("4"),
    /** 60% Good Progress = 6 */
    _60_GOOD_PROGRESS("6"),
    /** 80% Nearly Done = 8 */
    _80_NEARLY_DONE("8"),
    /** 90% Finishing = 9 */
    _90_FINISHING("9"),
    /** 95% Almost Done = A */
    _95_ALMOST_DONE("A"),
    /** 99% Cleaning up = C */
    _99_CLEANING_UP("C"),
    /** 100% Complete = D */
    _100_COMPLETE("D");
    
    public static final int AD_Reference_ID=366;
    private final String value;
    private X_Ref_R_Request_TaskStatus(String value)
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
         for( X_Ref_R_Request_TaskStatus v : X_Ref_R_Request_TaskStatus.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
