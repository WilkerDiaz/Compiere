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


/** AmountType AD_Reference_ID=235 */
public enum X_Ref_PA_Report_AmountType 
{
    /** Period Balance = BP */
    PERIOD_BALANCE("BP"),
    /** Total Balance = BT */
    TOTAL_BALANCE("BT"),
    /** Year Balance = BY */
    YEAR_BALANCE("BY"),
    /** Period Credit Only = CP */
    PERIOD_CREDIT_ONLY("CP"),
    /** Total Credit Only = CT */
    TOTAL_CREDIT_ONLY("CT"),
    /** Year Credit Only = CY */
    YEAR_CREDIT_ONLY("CY"),
    /** Period Debit Only = DP */
    PERIOD_DEBIT_ONLY("DP"),
    /** Total Debit Only = DT */
    TOTAL_DEBIT_ONLY("DT"),
    /** Year Debit Only = DY */
    YEAR_DEBIT_ONLY("DY"),
    /** Period Quantity = QP */
    PERIOD_QUANTITY("QP"),
    /** Total Quantity = QT */
    TOTAL_QUANTITY("QT"),
    /** Year Quantity = QY */
    YEAR_QUANTITY("QY");
    
    public static final int AD_Reference_ID=235;
    private final String value;
    private X_Ref_PA_Report_AmountType(String value)
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
         for( X_Ref_PA_Report_AmountType v : X_Ref_PA_Report_AmountType.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
