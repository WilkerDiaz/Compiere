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
/** Generated Model for I_XX_VMR_Promociones
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.1 - $Id: GenerateModel.java 8757 2010-05-12 21:32:32Z nnayak $ */
public class X_I_XX_VMR_Promociones extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_VMR_Promociones_ID id
    @param trx transaction
    */
    public X_I_XX_VMR_Promociones (Ctx ctx, int I_XX_VMR_Promociones_ID, Trx trx)
    {
        super (ctx, I_XX_VMR_Promociones_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_VMR_Promociones_ID == 0)
        {
            setI_XX_VMR_PROMOCIONES_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_VMR_Promociones (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27533255824789L;
    /** Last Updated Timestamp 2009-08-24 11:45:08.0 */
    public static final long updatedMS = 1251130508000L;
    /** AD_Table_ID=1000030 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_VMR_Promociones");
        
    }
    ;
    
    /** TableName=I_XX_VMR_Promociones */
    public static final String Table_Name="I_XX_VMR_Promociones";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Articulos existentes.
    @param ArtExistentes Articulos existentes */
    public void setArtExistentes (int ArtExistentes)
    {
        set_Value ("ArtExistentes", Integer.valueOf(ArtExistentes));
        
    }
    
    /** Get Articulos existentes.
    @return Articulos existentes */
    public int getArtExistentes() 
    {
        return get_ValueAsInt("ArtExistentes");
        
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
    
    /** Set Cantidad de productos a comprar.
    @param CantProdComprar Cantidad de productos a comprar */
    public void setCantProdComprar (int CantProdComprar)
    {
        set_Value ("CantProdComprar", Integer.valueOf(CantProdComprar));
        
    }
    
    /** Get Cantidad de productos a comprar.
    @return Cantidad de productos a comprar */
    public int getCantProdComprar() 
    {
        return get_ValueAsInt("CantProdComprar");
        
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
        set_ValueNoCheck ("HoraFin", HoraFin);
        
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
        set_ValueNoCheck ("HoraInicio", HoraInicio);
        
    }
    
    /** Get Hora de inicio.
    @return Hora de inicio */
    public Timestamp getHoraInicio() 
    {
        return (Timestamp)get_Value("HoraInicio");
        
    }
    
    /** Set Import Error Message.
    @param I_ErrorMsg Messages generated from import process */
    public void setI_ErrorMsg (String I_ErrorMsg)
    {
        set_Value ("I_ErrorMsg", I_ErrorMsg);
        
    }
    
    /** Get Import Error Message.
    @return Messages generated from import process */
    public String getI_ErrorMsg() 
    {
        return (String)get_Value("I_ErrorMsg");
        
    }
    
    /** Set Imported.
    @param I_IsImported Has this import been processed? */
    public void setI_IsImported (boolean I_IsImported)
    {
        set_Value ("I_IsImported", Boolean.valueOf(I_IsImported));
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public boolean isI_IsImported() 
    {
        return get_ValueAsBoolean("I_IsImported");
        
    }
    
    /** Set I_XX_VMR_PROMOCIONES_ID.
    @param I_XX_VMR_PROMOCIONES_ID I_XX_VMR_PROMOCIONES_ID */
    public void setI_XX_VMR_PROMOCIONES_ID (int I_XX_VMR_PROMOCIONES_ID)
    {
        if (I_XX_VMR_PROMOCIONES_ID < 1) throw new IllegalArgumentException ("I_XX_VMR_PROMOCIONES_ID is mandatory.");
        set_ValueNoCheck ("I_XX_VMR_PROMOCIONES_ID", Integer.valueOf(I_XX_VMR_PROMOCIONES_ID));
        
    }
    
    /** Get I_XX_VMR_PROMOCIONES_ID.
    @return I_XX_VMR_PROMOCIONES_ID */
    public int getI_XX_VMR_PROMOCIONES_ID() 
    {
        return get_ValueAsInt("I_XX_VMR_PROMOCIONES_ID");
        
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
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
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
    @param tienda Tienda */
    public void settienda (String tienda)
    {
        if (!X_Ref_XX_Tienda.isValid(tienda))
        throw new IllegalArgumentException ("tienda Invalid value - " + tienda + " - Reference_ID=1000026 - 00 - 02 - 03 - 07 - 09 - 10 - 15 - 16 - 17");
        set_Value ("tienda", tienda);
        
    }
    
    /** Get Tienda.
    @return Tienda */
    public String gettienda() 
    {
        return (String)get_Value("tienda");
        
    }
    
    /** Set Descuento en:.
    @param tipoDeDescuento Descuento en: */
    public void settipoDeDescuento (String tipoDeDescuento)
    {
        set_Value ("tipoDeDescuento", tipoDeDescuento);
        
    }
    
    /** Get Descuento en:.
    @return Descuento en: */
    public String gettipoDeDescuento() 
    {
        return (String)get_Value("tipoDeDescuento");
        
    }
    
    /** Set Tipo de Promoción.
    @param TipoPromocion Tipo de Promoción */
    public void setTipoPromocion (String TipoPromocion)
    {
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
    
    /** Set XX_CATEGORIA_VALUE.
    @param XX_CATEGORIA_VALUE XX_CATEGORIA_VALUE */
    public void setXX_CATEGORIA_VALUE (String XX_CATEGORIA_VALUE)
    {
        set_Value ("XX_CATEGORIA_VALUE", XX_CATEGORIA_VALUE);
        
    }
    
    /** Get XX_CATEGORIA_VALUE.
    @return XX_CATEGORIA_VALUE */
    public String getXX_CATEGORIA_VALUE() 
    {
        return (String)get_Value("XX_CATEGORIA_VALUE");
        
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
    
    /** Set XX_DEPARTAMENTO_VALUE.
    @param XX_DEPARTAMENTO_VALUE XX_DEPARTAMENTO_VALUE */
    public void setXX_DEPARTAMENTO_VALUE (String XX_DEPARTAMENTO_VALUE)
    {
        set_Value ("XX_DEPARTAMENTO_VALUE", XX_DEPARTAMENTO_VALUE);
        
    }
    
    /** Get XX_DEPARTAMENTO_VALUE.
    @return XX_DEPARTAMENTO_VALUE */
    public String getXX_DEPARTAMENTO_VALUE() 
    {
        return (String)get_Value("XX_DEPARTAMENTO_VALUE");
        
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
    
    /** Set XX_LINEA_VALUE.
    @param XX_LINEA_VALUE XX_LINEA_VALUE */
    public void setXX_LINEA_VALUE (String XX_LINEA_VALUE)
    {
        set_Value ("XX_LINEA_VALUE", XX_LINEA_VALUE);
        
    }
    
    /** Get XX_LINEA_VALUE.
    @return XX_LINEA_VALUE */
    public String getXX_LINEA_VALUE() 
    {
        return (String)get_Value("XX_LINEA_VALUE");
        
    }
    
    /** Set Monto mínimo.
    @param XX_MontoMinimo Monto mínimo */
    public void setXX_MontoMinimo (java.math.BigDecimal XX_MontoMinimo)
    {
        set_Value ("XX_MontoMinimo", XX_MontoMinimo);
        
    }
    
    /** Get Monto mínimo.
    @return Monto mínimo */
    public java.math.BigDecimal getXX_MontoMinimo() 
    {
        return get_ValueAsBigDecimal("XX_MontoMinimo");
        
    }
    
    /** Set Producto.
    @param XX_PRODUCTO_ID Producto */
    public void setXX_PRODUCTO_ID (int XX_PRODUCTO_ID)
    {
        if (XX_PRODUCTO_ID <= 0) set_Value ("XX_PRODUCTO_ID", null);
        else
        set_Value ("XX_PRODUCTO_ID", Integer.valueOf(XX_PRODUCTO_ID));
        
    }
    
    /** Get Producto.
    @return Producto */
    public int getXX_PRODUCTO_ID() 
    {
        return get_ValueAsInt("XX_PRODUCTO_ID");
        
    }
    
    /** Set XX_PRODUCTO_VALUE.
    @param XX_PRODUCTO_VALUE XX_PRODUCTO_VALUE */
    public void setXX_PRODUCTO_VALUE (String XX_PRODUCTO_VALUE)
    {
        set_Value ("XX_PRODUCTO_VALUE", XX_PRODUCTO_VALUE);
        
    }
    
    /** Get XX_PRODUCTO_VALUE.
    @return XX_PRODUCTO_VALUE */
    public String getXX_PRODUCTO_VALUE() 
    {
        return (String)get_Value("XX_PRODUCTO_VALUE");
        
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
    
    /** Set XX_REGALO.
    @param XX_REGALO XX_REGALO */
    public void setXX_REGALO (String XX_REGALO)
    {
        set_Value ("XX_REGALO", XX_REGALO);
        
    }
    
    /** Get XX_REGALO.
    @return XX_REGALO */
    public String getXX_REGALO() 
    {
        return (String)get_Value("XX_REGALO");
        
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
    
    /** Set XX_SECCION_VALUE.
    @param XX_SECCION_VALUE XX_SECCION_VALUE */
    public void setXX_SECCION_VALUE (String XX_SECCION_VALUE)
    {
        set_Value ("XX_SECCION_VALUE", XX_SECCION_VALUE);
        
    }
    
    /** Get XX_SECCION_VALUE.
    @return XX_SECCION_VALUE */
    public String getXX_SECCION_VALUE() 
    {
        return (String)get_Value("XX_SECCION_VALUE");
        
    }
	
}
