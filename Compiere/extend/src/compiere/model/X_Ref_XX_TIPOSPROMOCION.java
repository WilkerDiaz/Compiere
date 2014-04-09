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
package compiere.model;


/** TipoPromocion AD_Reference_ID=1000013 */
public enum X_Ref_XX_TIPOSPROMOCION 
{
    /** A1- Ahorro en compra = 1000100 */
    A1__AHORRO_EN_COMPRA("1000100"),
    /** A2- Producto gratis o en descuento = 1000200 */
    A2__PRODUCTO_GRATIS_O_EN_DESCUENTO("1000200"),
    /** A3- Descuento en productos publicados = 1000300 */
    A3__DESCUENTO_EN_PRODUCTOS_PUBLICADOS("1000300"),
    /** B1- Descuento en el producto X + 1 = 1000400 */
    B1__DESCUENTO_EN_EL_PRODUCTO_X_PLUS1("1000400"),
    /** C2 - Coorporativas = 1000500 */
    C2__COORPORATIVAS("1000500"),
    /** D1- Más se compra más se gana = 1000600 */
    D1__MÁS_SE_COMPRA_MÁS_SE_GANA("1000600"),
    /** D2- Transacción premiada = 1000700 */
    D2__TRANSACCIÓN_PREMIADA("1000700"),
    /** D3- Premio de Bono Regalo = 1000800 */
    D3__PREMIO_DE_BONO_REGALO("1000800"),
    /** D4- Cupón de descuento = 1000900 */
    D4__CUPÓN_DE_DESCUENTO("1000900"),
    /** E1- Regalo por compra = 1001000 */
    E1__REGALO_POR_COMPRA("1001000"),
    /** E3 - Promociones Clásicas = 1001100 */
    E3__PROMOCIONES_CLÁSICAS("1001100"),
    /** F1- Premio Ilusión = 1001200 */
    F1__PREMIO_ILUSIÓN("1001200");
    
    public static final int AD_Reference_ID=1000013;
    private final String value;
    private X_Ref_XX_TIPOSPROMOCION(String value)
    {
         this.value = value;
         
    }
    public String getValue() 
    {
         return this.value;
         
    }
    public static boolean isValid(String test) 
    {
         for( X_Ref_XX_TIPOSPROMOCION v : X_Ref_XX_TIPOSPROMOCION.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
