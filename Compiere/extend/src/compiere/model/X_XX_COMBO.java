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

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_COMBO
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_COMBO extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_COMBO_ID id
    @param trx transaction
    */
    public X_XX_COMBO (Ctx ctx, int XX_COMBO_ID, Trx trx)
    {
        super (ctx, XX_COMBO_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_COMBO_ID == 0)
        {
            setgrupo (0);	// 1
            setTipoPromocion (null);
            setXX_COMBO_ID (0);
            setXX_PROMOCIONES_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_COMBO (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27563249559789L;
    /** Last Updated Timestamp 2010-08-06 15:20:43.0 */
    public static final long updatedMS = 1281124243000L;
    /** AD_Table_ID=1000021 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_COMBO");
        
    }
    ;
    
    /** TableName=XX_COMBO */
    public static final String Table_Name="XX_COMBO";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Cantidad de productos.
    @param CantidadProducto Cantidad de productos */
    public void setCantidadProducto (int CantidadProducto)
    {
        set_Value ("CantidadProducto", Integer.valueOf(CantidadProducto));
        
    }
    
    /** Get Cantidad de productos.
    @return Cantidad de productos */
    public int getCantidadProducto() 
    {
        return get_ValueAsInt("CantidadProducto");
        
    }
    
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Grupo.
    @param grupo Grupo */
    public void setgrupo (int grupo)
    {
        set_Value ("grupo", Integer.valueOf(grupo));
        
    }
    
    /** Get Grupo.
    @return Grupo */
    public int getgrupo() 
    {
        return get_ValueAsInt("grupo");
        
    }
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
    }
    
    /** Set Hora de fin.
    @param HoraFin Hora de fin */
    public void setHoraFin (Timestamp HoraFin)
    {
        set_Value ("HoraFin", HoraFin);
        
    }
    
    /** Get Hora de fin.
    @return Hora de fin */
    public Timestamp getHoraFin() 
    {
        return (Timestamp)get_Value("HoraFin");
        
    }
    
    /** Set Hora de inicio.
    @param HoraInicio Hora de inicio */
    public void setHoraInicio (Timestamp HoraInicio)
    {
        set_Value ("HoraInicio", HoraInicio);
        
    }
    
    /** Get Hora de inicio.
    @return Hora de inicio */
    public Timestamp getHoraInicio() 
    {
        return (Timestamp)get_Value("HoraInicio");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
    }
    
    /** Set Nro de productos en descuento.
    @param NroPagar Nro de productos en descuento */
    public void setNroPagar (int NroPagar)
    {
        set_Value ("NroPagar", Integer.valueOf(NroPagar));
        
    }
    
    /** Get Nro de productos en descuento.
    @return Nro de productos en descuento */
    public int getNroPagar() 
    {
        return get_ValueAsInt("NroPagar");
        
    }
    
    /** Set Porcentaje de descuento.
    @param PorcDescuento Porcentaje de descuento */
    public void setPorcDescuento (java.math.BigDecimal PorcDescuento)
    {
        set_Value ("PorcDescuento", PorcDescuento);
        
    }
    
    /** Get Porcentaje de descuento.
    @return Porcentaje de descuento */
    public java.math.BigDecimal getPorcDescuento() 
    {
        return get_ValueAsBigDecimal("PorcDescuento");
        
    }
    
    /** Set Sincronizado.
    @param regactualizado Sincronizado */
    public void setregactualizado (boolean regactualizado)
    {
        set_Value ("regactualizado", Boolean.valueOf(regactualizado));
        
    }
    
    /** Get Sincronizado.
    @return Sincronizado */
    public boolean isregactualizado() 
    {
        return get_ValueAsBoolean("regactualizado");
        
    }
    
    /** Categoría = 1000000 */
    public static final String SELECCION_Categoría = X_Ref_XX_seleccion.CATEGORÍA.getValue();
    /** Departamento = 2000000 */
    public static final String SELECCION_Departamento = X_Ref_XX_seleccion.DEPARTAMENTO.getValue();
    /** Línea = 3000000 */
    public static final String SELECCION_Línea = X_Ref_XX_seleccion.LÍNEA.getValue();
    /** Sección = 4000000 */
    public static final String SELECCION_Sección = X_Ref_XX_seleccion.SECCIÓN.getValue();
    /** Marca = 5000000 */
    public static final String SELECCION_Marca = X_Ref_XX_seleccion.MARCA.getValue();
    /** Referencia del proveedor = 6000000 */
    public static final String SELECCION_ReferenciaDelProveedor = X_Ref_XX_seleccion.REFERENCIA_DEL_PROVEEDOR.getValue();
    /** Producto = 7000000 */
    public static final String SELECCION_Producto = X_Ref_XX_seleccion.PRODUCTO.getValue();
    /** Set Criterio de Selección.
    @param seleccion Criterio de Selección */
    public void setseleccion (String seleccion)
    {
        if (!X_Ref_XX_seleccion.isValid(seleccion))
        throw new IllegalArgumentException ("seleccion Invalid value - " + seleccion + " - Reference_ID=1000032 - 1000000 - 2000000 - 3000000 - 4000000 - 5000000 - 6000000 - 7000000");
        throw new IllegalArgumentException ("seleccion is virtual column");
        
    }
    
    /** Get Criterio de Selección.
    @return Criterio de Selección */
    public String getseleccion() 
    {
        return (String)get_Value("seleccion");
        
    }
    
    /** Todas las tiendas = 00 */
    public static final String TIENDA_TodasLasTiendas = X_Ref_XX_Tienda.TODAS_LAS_TIENDAS.getValue();
    /** 2 - Puente Yanes = 02 */
    public static final String TIENDA_2_PuenteYanes = X_Ref_XX_Tienda._2__PUENTE_YANES.getValue();
    /** 3 - Chacaito = 03 */
    public static final String TIENDA_3_Chacaito = X_Ref_XX_Tienda._3__CHACAITO.getValue();
    /** 7 - Tamanaco = 07 */
    public static final String TIENDA_7_Tamanaco = X_Ref_XX_Tienda._7__TAMANACO.getValue();
    /** 9 - La Granja = 09 */
    public static final String TIENDA_9_LaGranja = X_Ref_XX_Tienda._9__LA_GRANJA.getValue();
    /** 10 - Las Trinitarias = 10 */
    public static final String TIENDA_10_LasTrinitarias = X_Ref_XX_Tienda._10__LAS_TRINITARIAS.getValue();
    /** 15 - La Trinidad = 15 */
    public static final String TIENDA_15_LaTrinidad = X_Ref_XX_Tienda._15__LA_TRINIDAD.getValue();
    /** 16 - Maracaibo = 16 */
    public static final String TIENDA_16_Maracaibo = X_Ref_XX_Tienda._16__MARACAIBO.getValue();
    /** 17 - Millenium mall = 17 */
    public static final String TIENDA_17_MilleniumMall = X_Ref_XX_Tienda._17__MILLENIUM_MALL.getValue();
    /** Set Tienda.
    @param Tienda Wharehouse */
    public void setTienda (String Tienda)
    {
        if (!X_Ref_XX_Tienda.isValid(Tienda))
        throw new IllegalArgumentException ("Tienda Invalid value - " + Tienda + " - Reference_ID=1000026 - 00 - 02 - 03 - 07 - 09 - 10 - 15 - 16 - 17");
        set_Value ("Tienda", Tienda);
        
    }
    
    /** Get Tienda.
    @return Wharehouse */
    public String getTienda() 
    {
        return (String)get_Value("Tienda");
        
    }
    
    /** Linea = 1000000 */
    public static final String TIPODEDESCUENTO_Linea = X_Ref_XX_tipoDeDescuento.LINEA.getValue();
    /** Producto = 2000000 */
    public static final String TIPODEDESCUENTO_Producto = X_Ref_XX_tipoDeDescuento.PRODUCTO.getValue();
    /** Set Descuento en:.
    @param tipoDeDescuento Descuento en: */
    public void settipoDeDescuento (String tipoDeDescuento)
    {
        if (!X_Ref_XX_tipoDeDescuento.isValid(tipoDeDescuento))
        throw new IllegalArgumentException ("tipoDeDescuento Invalid value - " + tipoDeDescuento + " - Reference_ID=1000031 - 1000000 - 2000000");
        set_Value ("tipoDeDescuento", tipoDeDescuento);
        
    }
    
    /** Get Descuento en:.
    @return Descuento en: */
    public String gettipoDeDescuento() 
    {
        return (String)get_Value("tipoDeDescuento");
        
    }
    
    /** A1- Ahorro en compra = 1000100 */
    public static final String TIPOPROMOCION_A1_AhorroEnCompra = X_Ref_XX_TIPOSPROMOCION.A1__AHORRO_EN_COMPRA.getValue();
    /** A2- Producto gratis o en descuento = 1000200 */
    public static final String TIPOPROMOCION_A2_ProductoGratisOEnDescuento = X_Ref_XX_TIPOSPROMOCION.A2__PRODUCTO_GRATIS_O_EN_DESCUENTO.getValue();
    /** A3- Descuento en productos publicados = 1000300 */
    public static final String TIPOPROMOCION_A3_DescuentoEnProductosPublicados = X_Ref_XX_TIPOSPROMOCION.A3__DESCUENTO_EN_PRODUCTOS_PUBLICADOS.getValue();
    /** B1- Descuento en el producto X + 1 = 1000400 */
    public static final String TIPOPROMOCION_B1_DescuentoEnElProductoXPlus1 = X_Ref_XX_TIPOSPROMOCION.B1__DESCUENTO_EN_EL_PRODUCTO_X_PLUS1.getValue();
    /** C2 - Coorporativas = 1000500 */
    public static final String TIPOPROMOCION_C2_Coorporativas = X_Ref_XX_TIPOSPROMOCION.C2__COORPORATIVAS.getValue();
    /** D1- Más se compra más se gana = 1000600 */
    public static final String TIPOPROMOCION_D1_MásSeCompraMásSeGana = X_Ref_XX_TIPOSPROMOCION.D1__MÁS_SE_COMPRA_MÁS_SE_GANA.getValue();
    /** D2- Transacción premiada = 1000700 */
    public static final String TIPOPROMOCION_D2_TransacciónPremiada = X_Ref_XX_TIPOSPROMOCION.D2__TRANSACCIÓN_PREMIADA.getValue();
    /** D3- Premio de Bono Regalo = 1000800 */
    public static final String TIPOPROMOCION_D3_PremioDeBonoRegalo = X_Ref_XX_TIPOSPROMOCION.D3__PREMIO_DE_BONO_REGALO.getValue();
    /** D4- Cupón de descuento = 1000900 */
    public static final String TIPOPROMOCION_D4_CupónDeDescuento = X_Ref_XX_TIPOSPROMOCION.D4__CUPÓN_DE_DESCUENTO.getValue();
    /** E1- Regalo por compra = 1001000 */
    public static final String TIPOPROMOCION_E1_RegaloPorCompra = X_Ref_XX_TIPOSPROMOCION.E1__REGALO_POR_COMPRA.getValue();
    /** E3 - Promociones Clásicas = 1001100 */
    public static final String TIPOPROMOCION_E3_PromocionesClásicas = X_Ref_XX_TIPOSPROMOCION.E3__PROMOCIONES_CLÁSICAS.getValue();
    /** F1- Premio Ilusión = 1001200 */
    public static final String TIPOPROMOCION_F1_PremioIlusión = X_Ref_XX_TIPOSPROMOCION.F1__PREMIO_ILUSIÓN.getValue();
    /** Set Tipo de Promoción.
    @param TipoPromocion Tipo de Promoción */
    public void setTipoPromocion (String TipoPromocion)
    {
        if (TipoPromocion == null) throw new IllegalArgumentException ("TipoPromocion is mandatory");
        if (!X_Ref_XX_TIPOSPROMOCION.isValid(TipoPromocion))
        throw new IllegalArgumentException ("TipoPromocion Invalid value - " + TipoPromocion + " - Reference_ID=1000013 - 1000100 - 1000200 - 1000300 - 1000400 - 1000500 - 1000600 - 1000700 - 1000800 - 1000900 - 1001000 - 1001100 - 1001200");
        set_ValueNoCheck ("TipoPromocion", TipoPromocion);
        
    }
    
    /** Get Tipo de Promoción.
    @return Tipo de Promoción */
    public String getTipoPromocion() 
    {
        return (String)get_Value("TipoPromocion");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Categoría.
    @param XX_CATEGORIA_ID Categoría */
    public void setXX_CATEGORIA_ID (int XX_CATEGORIA_ID)
    {
        if (XX_CATEGORIA_ID <= 0) set_Value ("XX_CATEGORIA_ID", null);
        else
        set_Value ("XX_CATEGORIA_ID", Integer.valueOf(XX_CATEGORIA_ID));
        
    }
    
    /** Get Categoría.
    @return Categoría */
    public int getXX_CATEGORIA_ID() 
    {
        return get_ValueAsInt("XX_CATEGORIA_ID");
        
    }
    
    /** Set XX_COMBO_ID.
    @param XX_COMBO_ID XX_COMBO_ID */
    public void setXX_COMBO_ID (int XX_COMBO_ID)
    {
        if (XX_COMBO_ID < 1) throw new IllegalArgumentException ("XX_COMBO_ID is mandatory.");
        set_ValueNoCheck ("XX_COMBO_ID", Integer.valueOf(XX_COMBO_ID));
        
    }
    
    /** Get XX_COMBO_ID.
    @return XX_COMBO_ID */
    public int getXX_COMBO_ID() 
    {
        return get_ValueAsInt("XX_COMBO_ID");
        
    }
    
    /** Set Departamento.
    @param XX_DEPARTAMENTO_ID Departamento */
    public void setXX_DEPARTAMENTO_ID (int XX_DEPARTAMENTO_ID)
    {
        if (XX_DEPARTAMENTO_ID <= 0) set_Value ("XX_DEPARTAMENTO_ID", null);
        else
        set_Value ("XX_DEPARTAMENTO_ID", Integer.valueOf(XX_DEPARTAMENTO_ID));
        
    }
    
    /** Get Departamento.
    @return Departamento */
    public int getXX_DEPARTAMENTO_ID() 
    {
        return get_ValueAsInt("XX_DEPARTAMENTO_ID");
        
    }
    
    /** Set Línea.
    @param XX_LINEA_ID Línea */
    public void setXX_LINEA_ID (int XX_LINEA_ID)
    {
        if (XX_LINEA_ID <= 0) set_Value ("XX_LINEA_ID", null);
        else
        set_Value ("XX_LINEA_ID", Integer.valueOf(XX_LINEA_ID));
        
    }
    
    /** Get Línea.
    @return Línea */
    public int getXX_LINEA_ID() 
    {
        return get_ValueAsInt("XX_LINEA_ID");
        
    }
    
    /** Set Marca.
    @param XX_MARCA Marca */
    public void setXX_MARCA (String XX_MARCA)
    {
        set_Value ("XX_MARCA", XX_MARCA);
        
    }
    
    /** Get Marca.
    @return Marca */
    public String getXX_MARCA() 
    {
        return (String)get_Value("XX_MARCA");
        
    }
    
    /** Set Product.
    @param XX_Product_ID Product */
    public void setXX_Product_ID (int XX_Product_ID)
    {
        if (XX_Product_ID <= 0) set_Value ("XX_Product_ID", null);
        else
        set_Value ("XX_Product_ID", Integer.valueOf(XX_Product_ID));
        
    }
    
    /** Get Product.
    @return Product */
    public int getXX_Product_ID() 
    {
        return get_ValueAsInt("XX_Product_ID");
        
    }
    
    /** Set Promoción.
    @param XX_PROMOCIONES_ID Promoción */
    public void setXX_PROMOCIONES_ID (int XX_PROMOCIONES_ID)
    {
        if (XX_PROMOCIONES_ID < 1) throw new IllegalArgumentException ("XX_PROMOCIONES_ID is mandatory.");
        set_ValueNoCheck ("XX_PROMOCIONES_ID", Integer.valueOf(XX_PROMOCIONES_ID));
        
    }
    
    /** Get Promoción.
    @return Promoción */
    public int getXX_PROMOCIONES_ID() 
    {
        return get_ValueAsInt("XX_PROMOCIONES_ID");
        
    }
    
    /** Set Refencia de proveedor.
    @param XX_REFERENCIAPROVEEDOR_ID Refencia de proveedor */
    public void setXX_REFERENCIAPROVEEDOR_ID (int XX_REFERENCIAPROVEEDOR_ID)
    {
        if (XX_REFERENCIAPROVEEDOR_ID <= 0) set_Value ("XX_REFERENCIAPROVEEDOR_ID", null);
        else
        set_Value ("XX_REFERENCIAPROVEEDOR_ID", Integer.valueOf(XX_REFERENCIAPROVEEDOR_ID));
        
    }
    
    /** Get Refencia de proveedor.
    @return Refencia de proveedor */
    public int getXX_REFERENCIAPROVEEDOR_ID() 
    {
        return get_ValueAsInt("XX_REFERENCIAPROVEEDOR_ID");
        
    }
    
    /** Set Sección.
    @param XX_SECCION_ID Sección */
    public void setXX_SECCION_ID (int XX_SECCION_ID)
    {
        if (XX_SECCION_ID <= 0) set_Value ("XX_SECCION_ID", null);
        else
        set_Value ("XX_SECCION_ID", Integer.valueOf(XX_SECCION_ID));
        
    }
    
    /** Get Sección.
    @return Sección */
    public int getXX_SECCION_ID() 
    {
        return get_ValueAsInt("XX_SECCION_ID");
        
    }
    
    /** Set Transferencia.
    @param XX_Transferencia Transferencia */
    public void setXX_Transferencia (String XX_Transferencia)
    {
        set_Value ("XX_Transferencia", XX_Transferencia);
        
    }
    
    /** Get Transferencia.
    @return Transferencia */
    public String getXX_Transferencia() 
    {
        return (String)get_Value("XX_Transferencia");
        
    }
    
    
}
