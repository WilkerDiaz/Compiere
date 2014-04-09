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


/** TypeInventoryFault AD_Reference_ID=1001349 */
public enum X_Ref_TypeInventoryFault 
{
    /** % de falla por referencia básica por tienda = 1 */
    DE_FALLA_POR_REFERENCIA_BÁSICA_POR_TIENDA("1"),
    /** % de falla por referencia básica Consolidada compañía = 2 */
    DE_FALLA_POR_REFERENCIA_BÁSICA_CONSOLIDADA_COMPAÑÍA("2"),
    /** % de falla por tipo de básico Consolidado compañía = 3 */
    DE_FALLA_POR_TIPO_DE_BÁSICO_CONSOLIDADO_COMPAÑÍA("3"),
    /** % de falla por tipo de básico por tienda = 4 */
    DE_FALLA_POR_TIPO_DE_BÁSICO_POR_TIENDA("4"),
    /** % de falla por producto  por tienda = 5 */
    DE_FALLA_POR_PRODUCTO_POR_TIENDA("5"),
    /** % de falla por producto por básico compañía = 6 */
    DE_FALLA_POR_PRODUCTO_POR_BÁSICO_COMPAÑÍA("6");
    
    public static final int AD_Reference_ID=1001349;
    private final String value;
    private X_Ref_TypeInventoryFault(String value)
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
         for( X_Ref_TypeInventoryFault v : X_Ref_TypeInventoryFault.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
