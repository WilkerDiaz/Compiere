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


/** XX_TypePersonISLR AD_Reference_ID=1000429 */
public enum X_Ref_XX_Ref_TypePersonISLR 
{
    /** PJD - PERSONA JURIDICA = PJD */
    PJ_D__PERSONAJURIDICA("PJD"),
    /** PJND - COMPAÑIAS EXTRANJERAS = PJND */
    PJN_D__COMPAÑIASEXTRANJERAS("PJND"),
    /** PNNR - PASAPORTE CON VISA DE NEGOCIOS = PNNR */
    PNN_R__PASAPORTECONVISADENEGOCIOS("PNNR"),
    /** PNR - PERSONA NATURAL VENEZOLANO = PNR */
    PN_R__PERSONANATURALVENEZOLANO("PNR");
    
    public static final int AD_Reference_ID=1000429;
    private final String value;
    private X_Ref_XX_Ref_TypePersonISLR(String value)
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
         for( X_Ref_XX_Ref_TypePersonISLR v : X_Ref_XX_Ref_TypePersonISLR.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
