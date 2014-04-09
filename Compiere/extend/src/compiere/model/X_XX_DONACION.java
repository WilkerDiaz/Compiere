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
/** Generated Model for XX_DONACION
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_DONACION extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_DONACION_ID id
    @param trx transaction
    */
    public X_XX_DONACION (Ctx ctx, int XX_DONACION_ID, Trx trx)
    {
        super (ctx, XX_DONACION_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_DONACION_ID == 0)
        {
            setXX_DONACION_ID (0);
            setXX_PROMOCIONES_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_DONACION (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27523842244789L;
    /** Last Updated Timestamp 2009-05-07 12:52:08.0 */
    public static final long updatedMS = 1241716928000L;
    /** AD_Table_ID=1000013 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_DONACION");
        
    }
    ;
    
    /** TableName=XX_DONACION */
    public static final String Table_Name="XX_DONACION";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Monto de la donacion.
    @param MontoDonacion Monto de la donacion */
    public void setMontoDonacion (java.math.BigDecimal MontoDonacion)
    {
        set_Value ("MontoDonacion", MontoDonacion);
        
    }
    
    /** Get Monto de la donacion.
    @return Monto de la donacion */
    public java.math.BigDecimal getMontoDonacion() 
    {
        return get_ValueAsBigDecimal("MontoDonacion");
        
    }
    
    /** Set Mostrar al Totalizar.
    @param MostrarAlTotalizar Mostrar al Totalizar */
    public void setMostrarAlTotalizar (boolean MostrarAlTotalizar)
    {
        set_Value ("MostrarAlTotalizar", Boolean.valueOf(MostrarAlTotalizar));
        
    }
    
    /** Get Mostrar al Totalizar.
    @return Mostrar al Totalizar */
    public boolean isMostrarAlTotalizar() 
    {
        return get_ValueAsBoolean("MostrarAlTotalizar");
        
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
    
    /** Set Recuerdo de la donación.
    @param RECUERDO_DONACION Recuerdo de la donación */
    public void setRECUERDO_DONACION (String RECUERDO_DONACION)
    {
        set_Value ("RECUERDO_DONACION", RECUERDO_DONACION);
        
    }
    
    /** Get Recuerdo de la donación.
    @return Recuerdo de la donación */
    public String getRECUERDO_DONACION() 
    {
        return (String)get_Value("RECUERDO_DONACION");
        
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
    
    /** Monto libre = 1000000 */
    public static final String TIPODONACION_MontoLibre = X_Ref_XX_Donacion_TipoDonacion.MONTO_LIBRE.getValue();
    /** Monto predefinido = 2000000 */
    public static final String TIPODONACION_MontoPredefinido = X_Ref_XX_Donacion_TipoDonacion.MONTO_PREDEFINIDO.getValue();
    /** Set Tipo de donación.
    @param TipoDonacion Tipo de donación */
    public void setTipoDonacion (String TipoDonacion)
    {
        if (!X_Ref_XX_Donacion_TipoDonacion.isValid(TipoDonacion))
        throw new IllegalArgumentException ("TipoDonacion Invalid value - " + TipoDonacion + " - Reference_ID=1000027 - 1000000 - 2000000");
        set_Value ("TipoDonacion", TipoDonacion);
        
    }
    
    /** Get Tipo de donación.
    @return Tipo de donación */
    public String getTipoDonacion() 
    {
        return (String)get_Value("TipoDonacion");
        
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
    
    /** Set XX_DONACION_ID.
    @param XX_DONACION_ID XX_DONACION_ID */
    public void setXX_DONACION_ID (int XX_DONACION_ID)
    {
        if (XX_DONACION_ID < 1) throw new IllegalArgumentException ("XX_DONACION_ID is mandatory.");
        set_ValueNoCheck ("XX_DONACION_ID", Integer.valueOf(XX_DONACION_ID));
        
    }
    
    /** Get XX_DONACION_ID.
    @return XX_DONACION_ID */
    public int getXX_DONACION_ID() 
    {
        return get_ValueAsInt("XX_DONACION_ID");
        
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
    
    
}
