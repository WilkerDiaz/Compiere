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


/** ObjComercial AD_Reference_ID=1000024 */
public enum X_Ref_ObjComerciales 
{
    /** Promociones de Temporadas Comerciales = 1000000 */
    PROMOCIONES_DE_TEMPORADAS_COMERCIALES("1000000"),
    /** Promociones Creadoras de Valor = 2000000 */
    PROMOCIONES_CREADORAS_DE_VALOR("2000000"),
    /** Promociones Orientadas a Impulsar Surtido = 3000000 */
    PROMOCIONES_ORIENTADAS_A_IMPULSAR_SURTIDO("3000000");
    
    public static final int AD_Reference_ID=1000024;
    private final String value;
    private X_Ref_ObjComerciales(String value)
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
         for( X_Ref_ObjComerciales v : X_Ref_ObjComerciales.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
