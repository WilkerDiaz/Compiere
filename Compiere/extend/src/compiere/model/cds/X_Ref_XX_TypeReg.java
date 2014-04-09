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


/** XX_TypeReg AD_Reference_ID=1000219 */
public enum X_Ref_XX_TypeReg 
{
    /** 01 - VENTAS AL CONTADO  = 01 */
    _01__VENTASALCONTADO("01"),
    /** 02 - MENOS DESCUENTOS SOBRE VENTAS = 02 */
    _02__MENOSDESCUENTOSSOBREVENTAS("02"),
    /** 03 - TOTAL VENTAS NETAS = 03 */
    _03__TOTALVENTASNETAS("03"),
    /** 04 = 04 */
    _04("04"),
    /** 05 = 05 */
    _05("05"),
    /** 06 = 06 */
    _06("06"),
    /** 07 - TOTAL VENTAS DEL MES = 07 */
    _07__TOTALVENTASDELMES("07"),
    /** 08 = 08 */
    _08("08"),
    /** 09 - INVENTARIO INICIAL A PRECIO DE VENTA = 09 */
    _09__INVENTARIOINICIALAPRECIODEVENTA("09"),
    /** 10 - COMPRAS AL PRECIO DE VENTA = 10 */
    _10__COMPRASALPRECIODEVENTA("10"),
    /** 11 - AUMENTOS EN ETIQUETAS = 11 */
    _11__AUMENTOSENETIQUETAS("11"),
    /** 12 = 12 */
    _12("12"),
    /** 13 = 13 */
    _13("13"),
    /** 14 - MERCANCIA DISPONIBLE = 14 */
    _14__MERCANCIADISPONIBLE("14"),
    /** 15 - MENOS REBAJAS EN ETIQUETAS  = 15 */
    _15__MENOSREBAJASENETIQUETAS("15"),
    /** 16 = 16 */
    _16("16"),
    /** 17 - MENOS VENTAS AL CONTADO = 17 */
    _17__MENOSVENTASALCONTADO("17"),
    /** 18 - MENOS RECTIFICACION DE MARGEN POR INVENTARIO FISICO = 18 */
    _18__MENOSRECTIFICACIONDEMARGENPORINVENTARIOFISICO("18"),
    /** 19 - INVENTARIO FINAL AL PRECIO DE VENTA = 19 */
    _19__INVENTARIOFINALALPRECIODEVENTA("19"),
    /** 20 = 20 */
    _20("20"),
    /** 21 - MARGEN POR GANAR INICIAL = 21 */
    _21__MARGENPORGANARINICIAL("21"),
    /** 22 - MARGEN SOBRE COMPRAS = 22 */
    _22__MARGENSOBRECOMPRAS("22"),
    /** 23 - MARGEN AUMENTADO EN ETIQUETAS = 23 */
    _23__MARGENAUMENTADOENETIQUETAS("23"),
    /** 24 = 24 */
    _24("24"),
    /** 25 - MARGEN POR GANAR SOBRE DISPONIBLE = 25 */
    _25__MARGENPORGANARSOBREDISPONIBLE("25"),
    /** 26 - MENOS MARGEN POR GANAR FINAL 19X25/14 = 26 */
    _26__MENOSMARGENPORGANARFINA_L19_X2514("26"),
    /** 27 - MENOS MARGEN REBAJADO = 27 */
    _27__MENOSMARGENREBAJADO("27"),
    /** 28 - MENOS RECTIFICACION DE MARGEN POR INVENTARIO FISICO = 28 */
    _28__MENOSRECTIFICACIONDEMARGENPORINVENTARIOFISICO("28"),
    /** 29 = 29 */
    _29("29"),
    /** 30 - MARGEN GANADO = 30 */
    _30__MARGENGANADO("30"),
    /** 31 - MENOS VENTAS AL CONTADO = 31 */
    _31__MENOSVENTASALCONTADO("31"),
    /** 32 = 32 */
    _32("32"),
    /** 33 - COSTO DE VENTAS = 33 */
    _33__COSTODEVENTAS("33"),
    /** 34 = 34 */
    _34("34"),
    /** 35 - COSTOS VARIOS Y AJUSTES DE COMPRAS = 35 */
    _35__COSTOSVARIOSYAJUSTESDECOMPRAS("35"),
    /** 36 - TOTAL COSTOS DE VENTAS = 36 */
    _36__TOTALCOSTOSDEVENTAS("36"),
    /** 37 = 37 */
    _37("37"),
    /** 38 - GANANCIA BRUTA SOBRE VENTAS (7-36) = 38 */
    _38__GANANCIABRUTASOBREVENTA_S7_36("38"),
    /** 39 - PORCENTAJE SOBRE VENTAS (38/7) = 39 */
    _39__PORCENTAJESOBREVENTA_S387("39"),
    /** 40 - PORCENTAJE MARGEN POR GANAR S/INVENTARIO FINAL (26/19) = 40 */
    _40__PORCENTAJEMARGENPORGANARSINVENTARIOFINA_L2619("40");
    
    public static final int AD_Reference_ID=1000219;
    private final String value;
    private X_Ref_XX_TypeReg(String value)
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
         for( X_Ref_XX_TypeReg v : X_Ref_XX_TypeReg.values())
        {
             if( v.getValue().equals(test)) return true;
             
        }
        return false;
        
    }
    
}
