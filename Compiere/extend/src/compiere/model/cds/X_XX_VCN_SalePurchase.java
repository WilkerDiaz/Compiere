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

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VCN_SalePurchase
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_SalePurchase extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_SalePurchase_ID id
    @param trx transaction
    */
    public X_XX_VCN_SalePurchase (Ctx ctx, int XX_VCN_SalePurchase_ID, Trx trx)
    {
        super (ctx, XX_VCN_SalePurchase_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_SalePurchase_ID == 0)
        {
            setXX_VCN_SALEPURCHASE_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_SalePurchase (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27621050066789L;
    /** Last Updated Timestamp 2012-06-05 15:02:30.0 */
    public static final long updatedMS = 1338924750000L;
    /** AD_Table_ID=1000255 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_SalePurchase");
        
    }
    ;
    
    /** TableName=XX_VCN_SalePurchase */
    public static final String Table_Name="XX_VCN_SalePurchase";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
        else
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Set Accumulated Amount.
    @param XX_AmountAcu Accumulated Amount */
    public void setXX_AmountAcu (java.math.BigDecimal XX_AmountAcu)
    {
        set_Value ("XX_AmountAcu", XX_AmountAcu);
        
    }
    
    /** Get Accumulated Amount.
    @return Accumulated Amount */
    public java.math.BigDecimal getXX_AmountAcu() 
    {
        return get_ValueAsBigDecimal("XX_AmountAcu");
        
    }
    
    /** Set Month Amount.
    @param XX_AmountMonth Month Amount */
    public void setXX_AmountMonth (java.math.BigDecimal XX_AmountMonth)
    {
        set_Value ("XX_AmountMonth", XX_AmountMonth);
        
    }
    
    /** Get Month Amount.
    @return Month Amount */
    public java.math.BigDecimal getXX_AmountMonth() 
    {
        return get_ValueAsBigDecimal("XX_AmountMonth");
        
    }
    
    /** Set Month.
    @param XX_Month Month */
    public void setXX_Month (int XX_Month)
    {
        set_Value ("XX_Month", Integer.valueOf(XX_Month));
        
    }
    
    /** Get Month.
    @return Month */
    public int getXX_Month() 
    {
        return get_ValueAsInt("XX_Month");
        
    }
    
    /** 01 - VENTAS AL CONTADO  = 01 */
    public static final String XX_TYPEREG_01_VENTASALCONTADO = X_Ref_XX_TypeReg._01__VENTASALCONTADO.getValue();
    /** 02 - MENOS DESCUENTOS SOBRE VENTAS = 02 */
    public static final String XX_TYPEREG_02_MENOSDESCUENTOSSOBREVENTAS = X_Ref_XX_TypeReg._02__MENOSDESCUENTOSSOBREVENTAS.getValue();
    /** 03 - TOTAL VENTAS NETAS = 03 */
    public static final String XX_TYPEREG_03_TOTALVENTASNETAS = X_Ref_XX_TypeReg._03__TOTALVENTASNETAS.getValue();
    /** 04 = 04 */
    public static final String XX_TYPEREG_04 = X_Ref_XX_TypeReg._04.getValue();
    /** 05 = 05 */
    public static final String XX_TYPEREG_05 = X_Ref_XX_TypeReg._05.getValue();
    /** 06 = 06 */
    public static final String XX_TYPEREG_06 = X_Ref_XX_TypeReg._06.getValue();
    /** 07 - TOTAL VENTAS DEL MES = 07 */
    public static final String XX_TYPEREG_07_TOTALVENTASDELMES = X_Ref_XX_TypeReg._07__TOTALVENTASDELMES.getValue();
    /** 08 = 08 */
    public static final String XX_TYPEREG_08 = X_Ref_XX_TypeReg._08.getValue();
    /** 09 - INVENTARIO INICIAL A PRECIO DE VENTA = 09 */
    public static final String XX_TYPEREG_09_INVENTARIOINICIALAPRECIODEVENTA = X_Ref_XX_TypeReg._09__INVENTARIOINICIALAPRECIODEVENTA.getValue();
    /** 10 - COMPRAS AL PRECIO DE VENTA = 10 */
    public static final String XX_TYPEREG_10_COMPRASALPRECIODEVENTA = X_Ref_XX_TypeReg._10__COMPRASALPRECIODEVENTA.getValue();
    /** 11 - AUMENTOS EN ETIQUETAS = 11 */
    public static final String XX_TYPEREG_11_AUMENTOSENETIQUETAS = X_Ref_XX_TypeReg._11__AUMENTOSENETIQUETAS.getValue();
    /** 12 = 12 */
    public static final String XX_TYPEREG_12 = X_Ref_XX_TypeReg._12.getValue();
    /** 13 = 13 */
    public static final String XX_TYPEREG_13 = X_Ref_XX_TypeReg._13.getValue();
    /** 14 - MERCANCIA DISPONIBLE = 14 */
    public static final String XX_TYPEREG_14_MERCANCIADISPONIBLE = X_Ref_XX_TypeReg._14__MERCANCIADISPONIBLE.getValue();
    /** 15 - MENOS REBAJAS EN ETIQUETAS  = 15 */
    public static final String XX_TYPEREG_15_MENOSREBAJASENETIQUETAS = X_Ref_XX_TypeReg._15__MENOSREBAJASENETIQUETAS.getValue();
    /** 16 = 16 */
    public static final String XX_TYPEREG_16 = X_Ref_XX_TypeReg._16.getValue();
    /** 17 - MENOS VENTAS AL CONTADO = 17 */
    public static final String XX_TYPEREG_17_MENOSVENTASALCONTADO = X_Ref_XX_TypeReg._17__MENOSVENTASALCONTADO.getValue();
    /** 18 - MENOS RECTIFICACION DE MARGEN POR INVENTARIO FISICO = 18 */
    public static final String XX_TYPEREG_18_MENOSRECTIFICACIONDEMARGENPORINVENTARIOFISICO = X_Ref_XX_TypeReg._18__MENOSRECTIFICACIONDEMARGENPORINVENTARIOFISICO.getValue();
    /** 19 - INVENTARIO FINAL AL PRECIO DE VENTA = 19 */
    public static final String XX_TYPEREG_19_INVENTARIOFINALALPRECIODEVENTA = X_Ref_XX_TypeReg._19__INVENTARIOFINALALPRECIODEVENTA.getValue();
    /** 20 = 20 */
    public static final String XX_TYPEREG_20 = X_Ref_XX_TypeReg._20.getValue();
    /** 21 - MARGEN POR GANAR INICIAL = 21 */
    public static final String XX_TYPEREG_21_MARGENPORGANARINICIAL = X_Ref_XX_TypeReg._21__MARGENPORGANARINICIAL.getValue();
    /** 22 - MARGEN SOBRE COMPRAS = 22 */
    public static final String XX_TYPEREG_22_MARGENSOBRECOMPRAS = X_Ref_XX_TypeReg._22__MARGENSOBRECOMPRAS.getValue();
    /** 23 - MARGEN AUMENTADO EN ETIQUETAS = 23 */
    public static final String XX_TYPEREG_23_MARGENAUMENTADOENETIQUETAS = X_Ref_XX_TypeReg._23__MARGENAUMENTADOENETIQUETAS.getValue();
    /** 24 = 24 */
    public static final String XX_TYPEREG_24 = X_Ref_XX_TypeReg._24.getValue();
    /** 25 - MARGEN POR GANAR SOBRE DISPONIBLE = 25 */
    public static final String XX_TYPEREG_25_MARGENPORGANARSOBREDISPONIBLE = X_Ref_XX_TypeReg._25__MARGENPORGANARSOBREDISPONIBLE.getValue();
    /** 26 - MENOS MARGEN POR GANAR FINAL 19X25/14 = 26 */
    public static final String XX_TYPEREG_26_MENOSMARGENPORGANARFINAL19X2514 = X_Ref_XX_TypeReg._26__MENOSMARGENPORGANARFINA_L19_X2514.getValue();
    /** 27 - MENOS MARGEN REBAJADO = 27 */
    public static final String XX_TYPEREG_27_MENOSMARGENREBAJADO = X_Ref_XX_TypeReg._27__MENOSMARGENREBAJADO.getValue();
    /** 28 - MENOS RECTIFICACION DE MARGEN POR INVENTARIO FISICO = 28 */
    public static final String XX_TYPEREG_28_MENOSRECTIFICACIONDEMARGENPORINVENTARIOFISICO = X_Ref_XX_TypeReg._28__MENOSRECTIFICACIONDEMARGENPORINVENTARIOFISICO.getValue();
    /** 29 = 29 */
    public static final String XX_TYPEREG_29 = X_Ref_XX_TypeReg._29.getValue();
    /** 30 - MARGEN GANADO = 30 */
    public static final String XX_TYPEREG_30_MARGENGANADO = X_Ref_XX_TypeReg._30__MARGENGANADO.getValue();
    /** 31 - MENOS VENTAS AL CONTADO = 31 */
    public static final String XX_TYPEREG_31_MENOSVENTASALCONTADO = X_Ref_XX_TypeReg._31__MENOSVENTASALCONTADO.getValue();
    /** 32 = 32 */
    public static final String XX_TYPEREG_32 = X_Ref_XX_TypeReg._32.getValue();
    /** 33 - COSTO DE VENTAS = 33 */
    public static final String XX_TYPEREG_33_COSTODEVENTAS = X_Ref_XX_TypeReg._33__COSTODEVENTAS.getValue();
    /** 34 = 34 */
    public static final String XX_TYPEREG_34 = X_Ref_XX_TypeReg._34.getValue();
    /** 35 - COSTOS VARIOS Y AJUSTES DE COMPRAS = 35 */
    public static final String XX_TYPEREG_35_COSTOSVARIOSYAJUSTESDECOMPRAS = X_Ref_XX_TypeReg._35__COSTOSVARIOSYAJUSTESDECOMPRAS.getValue();
    /** 36 - TOTAL COSTOS DE VENTAS = 36 */
    public static final String XX_TYPEREG_36_TOTALCOSTOSDEVENTAS = X_Ref_XX_TypeReg._36__TOTALCOSTOSDEVENTAS.getValue();
    /** 37 = 37 */
    public static final String XX_TYPEREG_37 = X_Ref_XX_TypeReg._37.getValue();
    /** 38 - GANANCIA BRUTA SOBRE VENTAS (7-36) = 38 */
    public static final String XX_TYPEREG_38_GANANCIABRUTASOBREVENTAS7_36 = X_Ref_XX_TypeReg._38__GANANCIABRUTASOBREVENTA_S7_36.getValue();
    /** 39 - PORCENTAJE SOBRE VENTAS (38/7) = 39 */
    public static final String XX_TYPEREG_39_PORCENTAJESOBREVENTAS387 = X_Ref_XX_TypeReg._39__PORCENTAJESOBREVENTA_S387.getValue();
    /** 40 - PORCENTAJE MARGEN POR GANAR S/INVENTARIO FINAL (26/19) = 40 */
    public static final String XX_TYPEREG_40_PORCENTAJEMARGENPORGANARSINVENTARIOFINAL2619 = X_Ref_XX_TypeReg._40__PORCENTAJEMARGENPORGANARSINVENTARIOFINA_L2619.getValue();
    /** Set Registration Type.
    @param XX_TypeReg Registration Type */
    public void setXX_TypeReg (String XX_TypeReg)
    {
        if (!X_Ref_XX_TypeReg.isValid(XX_TypeReg))
        throw new IllegalArgumentException ("XX_TypeReg Invalid value - " + XX_TypeReg + " - Reference_ID=1000219 - 01 - 02 - 03 - 04 - 05 - 06 - 07 - 08 - 09 - 10 - 11 - 12 - 13 - 14 - 15 - 16 - 17 - 18 - 19 - 20 - 21 - 22 - 23 - 24 - 25 - 26 - 27 - 28 - 29 - 30 - 31 - 32 - 33 - 34 - 35 - 36 - 37 - 38 - 39 - 40");
        set_Value ("XX_TypeReg", XX_TypeReg);
        
    }
    
    /** Get Registration Type.
    @return Registration Type */
    public String getXX_TypeReg() 
    {
        return (String)get_Value("XX_TypeReg");
        
    }
    
    /** Set XX_VCN_SALEPURCHASE_ID.
    @param XX_VCN_SALEPURCHASE_ID XX_VCN_SALEPURCHASE_ID */
    public void setXX_VCN_SALEPURCHASE_ID (int XX_VCN_SALEPURCHASE_ID)
    {
        if (XX_VCN_SALEPURCHASE_ID < 1) throw new IllegalArgumentException ("XX_VCN_SALEPURCHASE_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_SALEPURCHASE_ID", Integer.valueOf(XX_VCN_SALEPURCHASE_ID));
        
    }
    
    /** Get XX_VCN_SALEPURCHASE_ID.
    @return XX_VCN_SALEPURCHASE_ID */
    public int getXX_VCN_SALEPURCHASE_ID() 
    {
        return get_ValueAsInt("XX_VCN_SALEPURCHASE_ID");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        if (XX_VMR_Department_ID <= 0) set_Value ("XX_VMR_Department_ID", null);
        else
        set_Value ("XX_VMR_Department_ID", Integer.valueOf(XX_VMR_Department_ID));
        
    }
    
    /** Get Department.
    @return Department */
    public int getXX_VMR_Department_ID() 
    {
        return get_ValueAsInt("XX_VMR_Department_ID");
        
    }
    
    /** Set Year.
    @param XX_Year Year */
    public void setXX_Year (int XX_Year)
    {
        set_Value ("XX_Year", Integer.valueOf(XX_Year));
        
    }
    
    /** Get Year.
    @return Year */
    public int getXX_Year() 
    {
        return get_ValueAsInt("XX_Year");
        
    }
    
    
}
