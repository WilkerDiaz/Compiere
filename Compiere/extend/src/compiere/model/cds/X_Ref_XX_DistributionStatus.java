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


/** XX_DistributionStatus AD_Reference_ID=1000148 */
public enum X_Ref_XX_DistributionStatus 
{
    /** Aprobada - Pendiente por chequeo de la O/C = AC */
    APROBADA__PENDIENTE_POR_CHEQUEO_DE_LA_OC("AC"),
    /** Anulada = AN */
    ANULADA("AN"),
    /** Aprobada = AP */
    APROBADA("AP"),
    /** Pendiente por fijar Precios Definitivos = FP */
    PENDIENTE_POR_FIJAR_PRECIOS_DEFINITIVOS("FP"),
    /** Pendiente = PE */
    PENDIENTE("PE"),
    /** Lista para Aprobar = QR */
    LISTA_PARA_APROBAR("QR"),
    /** Pendiente por Redistribucion y Aprobacion = QT */
    PENDIENTE_POR_REDISTRIBUCION_Y_APROBACION("QT");
    
    public static final int AD_Reference_ID=1000148;
    private final String value;
    private X_Ref_XX_DistributionStatus(String value)
    {
         this.value = value;
         
    }
    public String getValue() 
    {
         return this.value;
         
    }
    public static boolean isValid(String test) 
    {
         for( X_Ref_XX_DistributionStatus v : X_Ref_XX_DistributionStatus.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
