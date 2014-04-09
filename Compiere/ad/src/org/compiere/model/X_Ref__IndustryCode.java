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


/** Industry AD_Reference_ID=471 */
public enum X_Ref__IndustryCode 
{
    /** Arts, entertainment & recreation = 1000000 */
    ARTS_ENTERTAINMENT_RECREATION("1000000"),
    /** Construction = 1000001 */
    CONSTRUCTION("1000001"),
    /** Education = 1000002 */
    EDUCATION("1000002"),
    /** Finance & insurance = 1000003 */
    FINANCE_INSURANCE("1000003"),
    /** Health care & social assistance = 1000004 */
    HEALTH_CARE_SOCIAL_ASSISTANCE("1000004"),
    /** Manufacturing = 1000005 */
    MANUFACTURING("1000005"),
    /** Mining = 1000006 */
    MINING("1000006"),
    /** Public sector = 1000007 */
    PUBLIC_SECTOR("1000007"),
    /** Real estate, rental & leasing = 1000008 */
    REAL_ESTATE_RENTAL_LEASING("1000008"),
    /** Retail trade = 1000009 */
    RETAIL_TRADE("1000009"),
    /** Services = 1000010 */
    SERVICES("1000010"),
    /** Transportation & warehousing = 1000011 */
    TRANSPORTATION_WAREHOUSING("1000011"),
    /** Utilities = 1000012 */
    UTILITIES("1000012"),
    /** Wholesale trade = 1000013 */
    WHOLESALE_TRADE("1000013"),
    /** Other = 1000014 */
    OTHER("1000014");
    
    public static final int AD_Reference_ID=471;
    private final String value;
    private X_Ref__IndustryCode(String value)
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
         for( X_Ref__IndustryCode v : X_Ref__IndustryCode.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
