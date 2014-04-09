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


/** Tienda AD_Reference_ID=1000026 */
public enum X_Ref_XX_Tienda 
{
    /** Todas las tiendas = 00 */
    TODAS_LAS_TIENDAS("00"),
    /** 2 - Puente Yanes = 02 */
    _2__PUENTE_YANES("02"),
    /** 3 - Chacaito = 03 */
    _3__CHACAITO("03"),
    /** 7 - Tamanaco = 07 */
    _7__TAMANACO("07"),
    /** 9 - La Granja = 09 */
    _9__LA_GRANJA("09"),
    /** 10 - Las Trinitarias = 10 */
    _10__LAS_TRINITARIAS("10"),
    /** 15 - La Trinidad = 15 */
    _15__LA_TRINIDAD("15"),
    /** 16 - Maracaibo = 16 */
    _16__MARACAIBO("16"),
    /** 17 - Millenium mall = 17 */
    _17__MILLENIUM_MALL("17");
    
    public static final int AD_Reference_ID=1000026;
    private final String value;
    private X_Ref_XX_Tienda(String value)
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
         for( X_Ref_XX_Tienda v : X_Ref_XX_Tienda.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
