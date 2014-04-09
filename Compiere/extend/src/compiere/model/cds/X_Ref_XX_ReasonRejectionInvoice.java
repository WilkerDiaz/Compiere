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
package compiere.model.cds;


/** XX_ReasonRejectionInvoice AD_Reference_ID=1000315 */
public enum X_Ref_XX_ReasonRejectionInvoice 
{
    /** Total error in the amount of the invoice  = EAI */
    TOTAL_ERROR_IN_THE_AMOUNT_OF_THE_INVOICE("EAI"),
    /** Error in the amount of tax on the tax base = EAT */
    ERROR_IN_THE_AMOUNT_OF_TAX_ON_THE_TAX_BASE("EAT"),
    /** Error in the fiscal data of the acquiring company = EDC */
    ERROR_IN_THE_FISCAL_DATA_OF_THE_ACQUIRING_COMPANY("EDC"),
    /** Failure Preprinted formal duties = FPF */
    FAILURE_PREPRINTED_FORMAL_DUTIES("FPF");
    
    public static final int AD_Reference_ID=1000315;
    private final String value;
    private X_Ref_XX_ReasonRejectionInvoice(String value)
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
         for( X_Ref_XX_ReasonRejectionInvoice v : X_Ref_XX_ReasonRejectionInvoice.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
