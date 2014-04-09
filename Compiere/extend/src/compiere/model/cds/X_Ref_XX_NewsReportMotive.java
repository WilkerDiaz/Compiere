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


/** XX_NewsReportMotive AD_Reference_ID=1000349 */
public enum X_Ref_XX_NewsReportMotive 
{
    /** DAÑADO = 1 */
    DAÑADO("1"),
    /** NO APTO PARA LA VENTA = 2 */
    NOAPTOPARALAVENTA("2"),
    /** NO CORRESPONDE CON LO SOLICITADO = 3 */
    NOCORRESPONDECONLOSOLICITADO("3"),
    /** NO PEDIDO = 4 */
    NOPEDIDO("4"),
    /** EXCEDENTE = 5 */
    EXCEDENTE("5");
    
    public static final int AD_Reference_ID=1000349;
    private final String value;
    private X_Ref_XX_NewsReportMotive(String value)
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
         for( X_Ref_XX_NewsReportMotive v : X_Ref_XX_NewsReportMotive.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
