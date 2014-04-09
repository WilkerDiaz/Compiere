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


/** XX_Status AD_Reference_ID=1000214 */
public enum X_Ref_XX_StatusReturn 
{
    /** Abandonada = ABA */
    ABANDONADA("ABA"),
    /** Pendiente Por Retirar = DPR */
    PENDIENTE_POR_RETIRAR("DPR"),
    /** Retirada por el Proveedor = DRE */
    RETIRADA_POR_EL_PROVEEDOR("DRE"),
    /** Devolucion Pendiente por Completar Chequeo = PRE */
    DEVOLUCION_PENDIENTE_POR_COMPLETAR_CHEQUEO("PRE");
    
    public static final int AD_Reference_ID=1000214;
    private final String value;
    private X_Ref_XX_StatusReturn(String value)
    {
         this.value = value;
         
    }
    public String getValue() 
    {
         return this.value;
         
    }
    public static boolean isValid(String test) 
    {
         for( X_Ref_XX_StatusReturn v : X_Ref_XX_StatusReturn.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
