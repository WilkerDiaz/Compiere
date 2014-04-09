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
/** Generated Model for XX_PROMOCIONES
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_PROMOCIONES extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_PROMOCIONES_ID id
    @param trx transaction
    */
    public X_XX_PROMOCIONES (Ctx ctx, int XX_PROMOCIONES_ID, Trx trx)
    {
        super (ctx, XX_PROMOCIONES_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_PROMOCIONES_ID == 0)
        {
            setXX_PROMOCIONES_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_PROMOCIONES (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27529208275789L;
    /** Last Updated Timestamp 2009-07-08 15:25:59.0 */
    public static final long updatedMS = 1247082959000L;
    /** AD_Table_ID=1000009 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_PROMOCIONES");
        
    }
    ;
    
    /** TableName=XX_PROMOCIONES */
    public static final String Table_Name="XX_PROMOCIONES";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Aprobado.
    @param AprobadoGG Aprobado por la Gerencia General */
    public void setAprobadoGG (boolean AprobadoGG)
    {
        set_Value ("AprobadoGG", Boolean.valueOf(AprobadoGG));
        
    }
    
    /** Get Aprobado.
    @return Aprobado por la Gerencia General */
    public boolean isAprobadoGG() 
    {
        return get_ValueAsBoolean("AprobadoGG");
        
    }
    
    /** Set Aprobado.
    @param AprobadoMar Aprobado por Mercadeo */
    public void setAprobadoMar (boolean AprobadoMar)
    {
        set_Value ("AprobadoMar", Boolean.valueOf(AprobadoMar));
        
    }
    
    /** Get Aprobado.
    @return Aprobado por Mercadeo */
    public boolean isAprobadoMar() 
    {
        return get_ValueAsBoolean("AprobadoMar");
        
    }
    
    /** Set Aprobado.
    @param AprobadoMer Aprobado por Merchandising */
    public void setAprobadoMer (boolean AprobadoMer)
    {
        set_Value ("AprobadoMer", Boolean.valueOf(AprobadoMer));
        
    }
    
    /** Get Aprobado.
    @return Aprobado por Merchandising */
    public boolean isAprobadoMer() 
    {
        return get_ValueAsBoolean("AprobadoMer");
        
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
    
    /** Set Enviar para aprobación.
    @param enviarParaAprobar Enviar para aprobación */
    public void setenviarParaAprobar (String enviarParaAprobar)
    {
        set_Value ("enviarParaAprobar", enviarParaAprobar);
        
    }
    
    /** Get Enviar para aprobación.
    @return Enviar para aprobación */
    public String getenviarParaAprobar() 
    {
        return (String)get_Value("enviarParaAprobar");
        
    }
    
    /** Set Fecha de fin.
    @param FechaFin Fecha de fin */
    public void setFechaFin (Timestamp FechaFin)
    {
        set_Value ("FechaFin", FechaFin);
        
    }
    
    /** Get Fecha de fin.
    @return Fecha de fin */
    public Timestamp getFechaFin() 
    {
        return (Timestamp)get_Value("FechaFin");
        
    }
    
    /** Set Fecha de inicio.
    @param FechaInicio Fecha de inicio */
    public void setFechaInicio (Timestamp FechaInicio)
    {
        set_Value ("FechaInicio", FechaInicio);
        
    }
    
    /** Get Fecha de inicio.
    @return Fecha de inicio */
    public Timestamp getFechaInicio() 
    {
        return (Timestamp)get_Value("FechaInicio");
        
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
    
    /** Promociones de Temporadas Comerciales = 1000000 */
    public static final String OBJCOMERCIAL_PromocionesDeTemporadasComerciales = X_Ref_ObjComerciales.PROMOCIONES_DE_TEMPORADAS_COMERCIALES.getValue();
    /** Promociones Creadoras de Valor = 2000000 */
    public static final String OBJCOMERCIAL_PromocionesCreadorasDeValor = X_Ref_ObjComerciales.PROMOCIONES_CREADORAS_DE_VALOR.getValue();
    /** Promociones Orientadas a Impulsar Surtido = 3000000 */
    public static final String OBJCOMERCIAL_PromocionesOrientadasAImpulsarSurtido = X_Ref_ObjComerciales.PROMOCIONES_ORIENTADAS_A_IMPULSAR_SURTIDO.getValue();
    /** Set Objetivo Comercial.
    @param ObjComercial Refieren la finalidad o intención comercial que sirve de base a una acción promocional de mercadeo.  */
    public void setObjComercial (String ObjComercial)
    {
        if (!X_Ref_ObjComerciales.isValid(ObjComercial))
        throw new IllegalArgumentException ("ObjComercial Invalid value - " + ObjComercial + " - Reference_ID=1000024 - 1000000 - 2000000 - 3000000");
        set_Value ("ObjComercial", ObjComercial);
        
    }
    
    /** Get Objetivo Comercial.
    @return Refieren la finalidad o intención comercial que sirve de base a una acción promocional de mercadeo.  */
    public String getObjComercial() 
    {
        return (String)get_Value("ObjComercial");
        
    }
    
    /** Aumento del Ticket Promedio = 1000000 */
    public static final String OBJTACTICO_AumentoDelTicketPromedio = X_Ref_ObjTactico.AUMENTO_DEL_TICKET_PROMEDIO.getValue();
    /** Aumento del número de Transacciones = 2000000 */
    public static final String OBJTACTICO_AumentoDelNúmeroDeTransacciones = X_Ref_ObjTactico.AUMENTO_DEL_NÚMERO_DE_TRANSACCIONES.getValue();
    /** Aumento del Tráfico = 3000000 */
    public static final String OBJTACTICO_AumentoDelTráfico = X_Ref_ObjTactico.AUMENTO_DEL_TRÁFICO.getValue();
    /** Set Objetivo Táctico.
    @param ObjTactico Dependiendo del objetivo táctico que se persiga, las acciones promocionales de mercadeo pueden ser diseñadas para modificar alguno de los indicadores a través de los cuales se mide la dinámica de la gestión de la tienda */
    public void setObjTactico (String ObjTactico)
    {
        if (!X_Ref_ObjTactico.isValid(ObjTactico))
        throw new IllegalArgumentException ("ObjTactico Invalid value - " + ObjTactico + " - Reference_ID=1000025 - 1000000 - 2000000 - 3000000");
        set_Value ("ObjTactico", ObjTactico);
        
    }
    
    /** Get Objetivo Táctico.
    @return Dependiendo del objetivo táctico que se persiga, las acciones promocionales de mercadeo pueden ser diseñadas para modificar alguno de los indicadores a través de los cuales se mide la dinámica de la gestión de la tienda */
    public String getObjTactico() 
    {
        return (String)get_Value("ObjTactico");
        
    }
    
    /** Set Prioridad.
    @param prioridad Prioridad */
    public void setprioridad (int prioridad)
    {
        set_Value ("prioridad", Integer.valueOf(prioridad));
        
    }
    
    /** Get Prioridad.
    @return Prioridad */
    public int getprioridad() 
    {
        return get_ValueAsInt("prioridad");
        
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
        set_Value ("seleccion", seleccion);
        
    }
    
    /** Get Criterio de Selección.
    @return Criterio de Selección */
    public String getseleccion() 
    {
        return (String)get_Value("seleccion");
        
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
    
    /** Set Aviso.
    @param xx_aviso Aviso */
    public void setxx_aviso (boolean xx_aviso)
    {
        set_Value ("xx_aviso", Boolean.valueOf(xx_aviso));
        
    }
    
    /** Get Aviso.
    @return Aviso */
    public boolean isxx_aviso() 
    {
        return get_ValueAsBoolean("xx_aviso");
        
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
