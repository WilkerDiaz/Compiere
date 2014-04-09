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


/** BarcodeType AD_Reference_ID=377 */
public enum X_Ref_AD_PrintFormatItem_BarcodeType 
{
    /** Code 128 A character set = 28A */
    CODE128_A_CHARACTER_SET("28A"),
    /** Code 128 B character set = 28B */
    CODE128_B_CHARACTER_SET("28B"),
    /** Code 128 C character set = 28C */
    CODE128_C_CHARACTER_SET("28C"),
    /** Codabar 2 of 7 linear = 2o9 */
    CODABAR2_OF7_LINEAR("2o9"),
    /** Code 39 3 of 9 linear with Checksum = 3O9 */
    CODE393_OF9_LINEAR_WITH_CHECKSUM("3O9"),
    /** Code 39 3 of 9 linear w/o Checksum = 3o9 */
    CODE393_OF9_LINEAR_WO_CHECKSUM("3o9"),
    /** PDF417 two dimensional = 417 */
    PD_F417_TWO_DIMENSIONAL("417"),
    /** SCC-14 shipping code UCC/EAN 128 = C14 */
    SC_C_14_SHIPPING_CODE_UCCEA_N128("C14"),
    /** SSCC-18 number UCC/EAN 128 = C18 */
    SSC_C_18_NUMBER_UCCEA_N128("C18"),
    /** Code 128 dynamically switching = C28 */
    CODE128_DYNAMICALLY_SWITCHING("C28"),
    /** Code 39 linear with Checksum = C39 */
    CODE39_LINEAR_WITH_CHECKSUM("C39"),
    /** Codeabar linear = COD */
    CODEABAR_LINEAR("COD"),
    /** EAN 128 = E28 */
    EA_N128("E28"),
    /** Global Trade Item No GTIN UCC/EAN 128 = GTN */
    GLOBAL_TRADE_ITEM_NO_GTINUCCEA_N128("GTN"),
    /** Codabar Monarch linear = MON */
    CODABAR_MONARCH_LINEAR("MON"),
    /** Codabar NW-7 linear = NW7 */
    CODABAR_N_W_7_LINEAR("NW7"),
    /** Shipment ID number UCC/EAN 128 = SID */
    SHIPMENT_ID_NUMBER_UCCEA_N128("SID"),
    /** UCC 128 = U28 */
    UC_C128("U28"),
    /** Code 39 USD3 with Checksum = US3 */
    CODE39_US_D3_WITH_CHECKSUM("US3"),
    /** Codabar USD-4 linear = US4 */
    CODABAR_US_D_4_LINEAR("US4"),
    /** US Postal Service UCC/EAN 128 = USP */
    US_POSTAL_SERVICE_UCCEA_N128("USP"),
    /** Code 39 linear w/o Checksum = c39 */
    CODE39_LINEAR_WO_CHECKSUM("c39"),
    /** Code 39 USD3 w/o Checksum = us3 */
    CODE39_US_D3_WO_CHECKSUM("us3");
    
    public static final int AD_Reference_ID=377;
    private final String value;
    private X_Ref_AD_PrintFormatItem_BarcodeType(String value)
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
         for( X_Ref_AD_PrintFormatItem_BarcodeType v : X_Ref_AD_PrintFormatItem_BarcodeType.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
