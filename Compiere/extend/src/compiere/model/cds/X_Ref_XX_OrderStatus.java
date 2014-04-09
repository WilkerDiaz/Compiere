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


/** XX_OrderStatus AD_Reference_ID=1000103 */
public enum X_Ref_XX_OrderStatus 
{
    /** ANULADA = AN */
    ANULADA("AN"),
    /** APROBADA = AP */
    APROBADA("AP"),
    /** CHEQUEADA = CH */
    CHEQUEADA("CH"),
    /** EN ADUANA = EA */
    ENADUANA("EA"),
    /** ENTREGADA AL AGENTE DE CARGA = EAC */
    ENTREGADAALAGENTEDECARGA("EAC"),
    /** EN PRODUCCIÓN = EP */
    ENPRODUCCIÓN("EP"),
    /** EN PROCESO DE NACIONALIZACIÓN = EPN */
    ENPROCESODENACIONALIZACIÓN("EPN"),
    /** EN TRÁNSITO INTERNACIONAL = ETI */
    ENTRÁNSITOINTERNACIONAL("ETI"),
    /** EN TRÁNSITO NACIONAL = ETN */
    ENTRÁNSITONACIONAL("ETN"),
    /** LLEGADA A CD = LCD */
    LLEGADAACD("LCD"),
    /** LLEGADA A VENEZUELA = LVE */
    LLEGADAAVENEZUELA("LVE"),
    /** PENDIENTE = PEN */
    PENDIENTE("PEN"),
    /** PROFORMA = PRO */
    PROFORMA("PRO"),
    /** RECIBIDA = RE */
    RECIBIDA("RE"),
    /** SITME = SIT */
    SITME("SIT");
    
    public static final int AD_Reference_ID=1000103;
    private final String value;
    private X_Ref_XX_OrderStatus(String value)
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
         for( X_Ref_XX_OrderStatus v : X_Ref_XX_OrderStatus.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
