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
/** Generated Model for XX_PromProductoDetalleCaja
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_PromProductoDetalleCaja extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_PromProductoDetalleCaja_ID id
    @param trx transaction
    */
    public X_XX_PromProductoDetalleCaja (Ctx ctx, int XX_PromProductoDetalleCaja_ID, Trx trx)
    {
        super (ctx, XX_PromProductoDetalleCaja_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_PromProductoDetalleCaja_ID == 0)
        {
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_PromProductoDetalleCaja (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27565674235789L;
    /** Last Updated Timestamp 2010-09-03 16:51:59.0 */
    public static final long updatedMS = 1283548919000L;
    /** AD_Table_ID=1000025 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_PromProductoDetalleCaja");
        
    }
    ;
    
    /** TableName=XX_PromProductoDetalleCaja */
    public static final String Table_Name="XX_PromProductoDetalleCaja";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Cant. de Bs. en Bono Regalo.
    @param BsBonoRegalo Cant. de Bs. en Bono Regalo */
    public void setBsBonoRegalo (java.math.BigDecimal BsBonoRegalo)
    {
        set_Value ("BsBonoRegalo", BsBonoRegalo);
        
    }
    
    /** Get Cant. de Bs. en Bono Regalo.
    @return Cant. de Bs. en Bono Regalo */
    public java.math.BigDecimal getBsBonoRegalo() 
    {
        return get_ValueAsBigDecimal("BsBonoRegalo");
        
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
    
    /** Set Categoría.
    @param Categoria Categoria a promocionar */
    public void setCategoria (String Categoria)
    {
        set_Value ("Categoria", Categoria);
        
    }
    
    /** Get Categoría.
    @return Categoria a promocionar */
    public String getCategoria() 
    {
        return (String)get_Value("Categoria");
        
    }
    
    /** Set Departamento.
    @param Departamento Departamento */
    public void setDepartamento (String Departamento)
    {
        set_Value ("Departamento", Departamento);
        
    }
    
    /** Get Departamento.
    @return Departamento */
    public String getDepartamento() 
    {
        return (String)get_Value("Departamento");
        
    }
    
    /** Set Detalle Promocion.
    @param DetallePromocion Detalle promocion en caja */
    public void setDetallePromocion (int DetallePromocion)
    {
        set_Value ("DetallePromocion", Integer.valueOf(DetallePromocion));
        
    }
    
    /** Get Detalle Promocion.
    @return Detalle promocion en caja */
    public int getDetallePromocion() 
    {
        return get_ValueAsInt("DetallePromocion");
        
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
    
    /** Set Monto de compra.
    @param MontoPremiado Monto de compra */
    public void setMontoPremiado (java.math.BigDecimal MontoPremiado)
    {
        set_Value ("MontoPremiado", MontoPremiado);
        
    }
    
    /** Get Monto de compra.
    @return Monto de compra */
    public java.math.BigDecimal getMontoPremiado() 
    {
        return get_ValueAsBigDecimal("MontoPremiado");
        
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
    
    /** Set Regalo.
    @param Regalo Regalo */
    public void setRegalo (String Regalo)
    {
        set_Value ("Regalo", Regalo);
        
    }
    
    /** Get Regalo.
    @return Regalo */
    public String getRegalo() 
    {
        return (String)get_Value("Regalo");
        
    }
    
    /** Set Sección.
    @param Seccion Sección */
    public void setSeccion (String Seccion)
    {
        set_Value ("Seccion", Seccion);
        
    }
    
    /** Get Sección.
    @return Sección */
    public String getSeccion() 
    {
        return (String)get_Value("Seccion");
        
    }
    
    /** Set Tienda.
    @param Tienda Wharehouse */
    public void setTienda (String Tienda)
    {
        set_Value ("Tienda", Tienda);
        
    }
    
    /** Get Tienda.
    @return Wharehouse */
    public String getTienda() 
    {
        return (String)get_Value("Tienda");
        
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
        if (!X_Ref_XX_TIPOSPROMOCION.isValid(TipoPromocion))
        throw new IllegalArgumentException ("TipoPromocion Invalid value - " + TipoPromocion + " - Reference_ID=1000013 - 1000100 - 1000200 - 1000300 - 1000400 - 1000500 - 1000600 - 1000700 - 1000800 - 1000900 - 1001000 - 1001100 - 1001200");
        set_Value ("TipoPromocion", TipoPromocion);
        
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
    
    /** Set XX_COMBO_ID.
    @param XX_COMBO_ID XX_COMBO_ID */
    public void setXX_COMBO_ID (int XX_COMBO_ID)
    {
        if (XX_COMBO_ID <= 0) set_Value ("XX_COMBO_ID", null);
        else
        set_Value ("XX_COMBO_ID", Integer.valueOf(XX_COMBO_ID));
        
    }
    
    /** Get XX_COMBO_ID.
    @return XX_COMBO_ID */
    public int getXX_COMBO_ID() 
    {
        return get_ValueAsInt("XX_COMBO_ID");
        
    }
    
    /** Set Línea.
    @param XX_LINEA_ID Línea */
    public void setXX_LINEA_ID (String XX_LINEA_ID)
    {
        set_Value ("XX_LINEA_ID", XX_LINEA_ID);
        
    }
    
    /** Get Línea.
    @return Línea */
    public String getXX_LINEA_ID() 
    {
        return (String)get_Value("XX_LINEA_ID");
        
    }
    
    /** Set Marca.
    @param XX_MARCA Marca */
    public void setXX_MARCA (int XX_MARCA)
    {
        set_Value ("XX_MARCA", Integer.valueOf(XX_MARCA));
        
    }
    
    /** Get Marca.
    @return Marca */
    public int getXX_MARCA() 
    {
        return get_ValueAsInt("XX_MARCA");
        
    }
    
    /** Set Promoción.
    @param XX_PROMOCIONES_ID Promoción */
    public void setXX_PROMOCIONES_ID (int XX_PROMOCIONES_ID)
    {
        if (XX_PROMOCIONES_ID <= 0) set_Value ("XX_PROMOCIONES_ID", null);
        else
        set_Value ("XX_PROMOCIONES_ID", Integer.valueOf(XX_PROMOCIONES_ID));
        
    }
    
    /** Get Promoción.
    @return Promoción */
    public int getXX_PROMOCIONES_ID() 
    {
        return get_ValueAsInt("XX_PROMOCIONES_ID");
        
    }
    
    /** Set XX_PromProductoDetalleCaja_ID.
    @param XX_PromProductoDetalleCaja_ID XX_PromProductoDetalleCaja_ID */
    public void setXX_PromProductoDetalleCaja_ID (int XX_PromProductoDetalleCaja_ID)
    {
        if (XX_PromProductoDetalleCaja_ID <= 0) set_Value ("XX_PromProductoDetalleCaja_ID", null);
        else
        set_Value ("XX_PromProductoDetalleCaja_ID", Integer.valueOf(XX_PromProductoDetalleCaja_ID));
        
    }
    
    /** Get XX_PromProductoDetalleCaja_ID.
    @return XX_PromProductoDetalleCaja_ID */
    public int getXX_PromProductoDetalleCaja_ID() 
    {
        return get_ValueAsInt("XX_PromProductoDetalleCaja_ID");
        
    }
    
    /** Set Referencia de proveedor.
    @param XX_RefProv Referencia de proveedor */
    public void setXX_RefProv (String XX_RefProv)
    {
        set_Value ("XX_RefProv", XX_RefProv);
        
    }
    
    /** Get Referencia de proveedor.
    @return Referencia de proveedor */
    public String getXX_RefProv() 
    {
        return (String)get_Value("XX_RefProv");
        
    }
    
    /** Set XX_REGALOPPORCOMPRA_ID.
    @param XX_REGALOPPORCOMPRA_ID XX_REGALOPPORCOMPRA_ID */
    public void setXX_REGALOPPORCOMPRA_ID (int XX_REGALOPPORCOMPRA_ID)
    {
        if (XX_REGALOPPORCOMPRA_ID <= 0) set_Value ("XX_REGALOPPORCOMPRA_ID", null);
        else
        set_Value ("XX_REGALOPPORCOMPRA_ID", Integer.valueOf(XX_REGALOPPORCOMPRA_ID));
        
    }
    
    /** Get XX_REGALOPPORCOMPRA_ID.
    @return XX_REGALOPPORCOMPRA_ID */
    public int getXX_REGALOPPORCOMPRA_ID() 
    {
        return get_ValueAsInt("XX_REGALOPPORCOMPRA_ID");
        
    }
    
    
}
