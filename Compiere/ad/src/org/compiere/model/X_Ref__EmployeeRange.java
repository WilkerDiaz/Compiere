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


/** EmployeeRange AD_Reference_ID=470 */
public enum X_Ref__EmployeeRange 
{
    /** Up to 10 employees = 1000000 */
    UP_TO10_EMPLOYEES("1000000"),
    /** 11-20 employees = 1000001 */
    _11_20_EMPLOYEES("1000001"),
    /** 21-100 employees = 1000002 */
    _21_100_EMPLOYEES("1000002"),
    /** 101-200 employees = 1000003 */
    _101_200_EMPLOYEES("1000003"),
    /** 201-500 employees = 1000004 */
    _201_500_EMPLOYEES("1000004"),
    /** 501-1000 employees = 1000005 */
    _501_1000_EMPLOYEES("1000005"),
    /** 1000-2000 employees = 1000006 */
    _1000_2000_EMPLOYEES("1000006"),
    /** Over 2000 employees = 1000007 */
    OVER2000_EMPLOYEES("1000007");
    
    public static final int AD_Reference_ID=470;
    private final String value;
    private X_Ref__EmployeeRange(String value)
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
         for( X_Ref__EmployeeRange v : X_Ref__EmployeeRange.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
