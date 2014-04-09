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
package compiere.model.promociones;


/** XX_WarehouseBecoNumber AD_Reference_ID=1000310 */
public enum X_Ref_XX_WarehousePromotion 
{
    /** All stores = 000 */
    ALL_STORES("000"),
    /** 01- Laboratorio = 001 */
    _01__LABORATORIO("001"),
    /** 02 - Puente Yanes = 002 */
    _02__PUENTE_YANES("002"),
    /** 03 - Chacaito = 003 */
    _03__CHACAITO("003"),
    /** 07 - Tamanaco = 007 */
    _07__TAMANACO("007"),
    /** 09 - La Granja = 009 */
    _09__LA_GRANJA("009"),
    /** 10 - Las Trinitarias = 010 */
    _10__LAS_TRINITARIAS("010"),
    /** 15 - La Trinidad = 015 */
    _15__LA_TRINIDAD("015"),
    /** 16 - Maracaibo = 016 */
    _16__MARACAIBO("016"),
    /** 17 - Millennium = 017 */
    _17__MILLENNIUM("017");
    
    public static final int AD_Reference_ID=1000310;
    private final String value;
    private X_Ref_XX_WarehousePromotion(String value)
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
         for( X_Ref_XX_WarehousePromotion v : X_Ref_XX_WarehousePromotion.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
