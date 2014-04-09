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
package compiere.model.payments;


/** XX_Month AD_Reference_ID=1001865 */
public enum X_Ref_XX_RefMonthDeclaration 
{
    /** Julio = 01 */
    JULIO("01"),
    /** Agosto = 02 */
    AGOSTO("02"),
    /** Septiembre = 03 */
    SEPTIEMBRE("03"),
    /** Octubre = 04 */
    OCTUBRE("04"),
    /** Noviembre = 05 */
    NOVIEMBRE("05"),
    /** Diciembre = 06 */
    DICIEMBRE("06"),
    /** Enero = 07 */
    ENERO("07"),
    /** Febrero = 08 */
    FEBRERO("08"),
    /** Marzo = 09 */
    MARZO("09"),
    /** Abril = 10 */
    ABRIL("10"),
    /** Mayo = 11 */
    MAYO("11"),
    /** Junio = 12 */
    JUNIO("12");
    
    public static final int AD_Reference_ID=1001865;
    private final String value;
    private X_Ref_XX_RefMonthDeclaration(String value)
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
         for( X_Ref_XX_RefMonthDeclaration v : X_Ref_XX_RefMonthDeclaration.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
