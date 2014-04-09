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
/** Generated Model for XX_VME_CRDetallePromocion
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VME_CRDetallePromocion extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VME_CRDetallePromocion_ID id
    @param trx transaction
    */
    public X_XX_VME_CRDetallePromocion (Ctx ctx, int XX_VME_CRDetallePromocion_ID, Trx trx)
    {
        super (ctx, XX_VME_CRDetallePromocion_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VME_CRDetallePromocion_ID == 0)
        {
            setValue (null);
            setXX_VME_CRDetallePromocion_ID (0);
            setXX_VME_CRPromocion_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VME_CRDetallePromocion (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27538364640789L;
    /** Last Updated Timestamp 2009-10-22 14:52:04.0 */
    public static final long updatedMS = 1256239324000L;
    /** AD_Table_ID=1000428 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VME_CRDetallePromocion");
        
    }
    ;
    
    /** TableName=XX_VME_CRDetallePromocion */
    public static final String Table_Name="XX_VME_CRDetallePromocion";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
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
    
    /** Set Store code.
    @param XX_VME_CodTienda Number of the store */
    public void setXX_VME_CodTienda (java.math.BigDecimal XX_VME_CodTienda)
    {
        set_Value ("XX_VME_CodTienda", XX_VME_CodTienda);
        
    }
    
    /** Get Store code.
    @return Number of the store */
    public java.math.BigDecimal getXX_VME_CodTienda() 
    {
        return get_ValueAsBigDecimal("XX_VME_CodTienda");
        
    }
    
    /** Set Consecutive.
    @param XX_VME_Consecutive_ID Is a Product  that belongs to a diferent batch of the same product. */
    public void setXX_VME_Consecutive_ID (int XX_VME_Consecutive_ID)
    {
        if (XX_VME_Consecutive_ID <= 0) set_Value ("XX_VME_Consecutive_ID", null);
        else
        set_Value ("XX_VME_Consecutive_ID", Integer.valueOf(XX_VME_Consecutive_ID));
        
    }
    
    /** Get Consecutive.
    @return Is a Product  that belongs to a diferent batch of the same product. */
    public int getXX_VME_Consecutive_ID() 
    {
        return get_ValueAsInt("XX_VME_Consecutive_ID");
        
    }
    
    /** Set Promotion Detail ID.
    @param XX_VME_CRDetallePromocion_ID Identifier of the promotion detail */
    public void setXX_VME_CRDetallePromocion_ID (int XX_VME_CRDetallePromocion_ID)
    {
        if (XX_VME_CRDetallePromocion_ID < 1) throw new IllegalArgumentException ("XX_VME_CRDetallePromocion_ID is mandatory.");
        set_ValueNoCheck ("XX_VME_CRDetallePromocion_ID", Integer.valueOf(XX_VME_CRDetallePromocion_ID));
        
    }
    
    /** Get Promotion Detail ID.
    @return Identifier of the promotion detail */
    public int getXX_VME_CRDetallePromocion_ID() 
    {
        return get_ValueAsInt("XX_VME_CRDetallePromocion_ID");
        
    }
    
    /** Set Promotion.
    @param XX_VME_CRPromocion_ID Promotion identifier. */
    public void setXX_VME_CRPromocion_ID (int XX_VME_CRPromocion_ID)
    {
        if (XX_VME_CRPromocion_ID < 1) throw new IllegalArgumentException ("XX_VME_CRPromocion_ID is mandatory.");
        set_ValueNoCheck ("XX_VME_CRPromocion_ID", Integer.valueOf(XX_VME_CRPromocion_ID));
        
    }
    
    /** Get Promotion.
    @return Promotion identifier. */
    public int getXX_VME_CRPromocion_ID() 
    {
        return get_ValueAsInt("XX_VME_CRPromocion_ID");
        
    }
    
    /** Set Cupon Number.
    @param XX_VME_NumCupon Number of the promotional cupon */
    public void setXX_VME_NumCupon (java.math.BigDecimal XX_VME_NumCupon)
    {
        set_Value ("XX_VME_NumCupon", XX_VME_NumCupon);
        
    }
    
    /** Get Cupon Number.
    @return Number of the promotional cupon */
    public java.math.BigDecimal getXX_VME_NumCupon() 
    {
        return get_ValueAsBigDecimal("XX_VME_NumCupon");
        
    }
    
    /** Set Detail number.
    @param XX_VME_NumDetalle Number of the detail inside the promotion */
    public void setXX_VME_NumDetalle (java.math.BigDecimal XX_VME_NumDetalle)
    {
        set_Value ("XX_VME_NumDetalle", XX_VME_NumDetalle);
        
    }
    
    /** Get Detail number.
    @return Number of the detail inside the promotion */
    public java.math.BigDecimal getXX_VME_NumDetalle() 
    {
        return get_ValueAsBigDecimal("XX_VME_NumDetalle");
        
    }
    
    /** Set Discount percent.
    @param XX_VME_PorcentajeDescuento Discount percent of a detail inside the promotion */
    public void setXX_VME_PorcentajeDescuento (java.math.BigDecimal XX_VME_PorcentajeDescuento)
    {
        set_Value ("XX_VME_PorcentajeDescuento", XX_VME_PorcentajeDescuento);
        
    }
    
    /** Get Discount percent.
    @return Discount percent of a detail inside the promotion */
    public java.math.BigDecimal getXX_VME_PorcentajeDescuento() 
    {
        return get_ValueAsBigDecimal("XX_VME_PorcentajeDescuento");
        
    }
    
    /** Set Final Price.
    @param XX_VME_PrecioFinal Final price for a detail inside a promotion */
    public void setXX_VME_PrecioFinal (java.math.BigDecimal XX_VME_PrecioFinal)
    {
        set_Value ("XX_VME_PrecioFinal", XX_VME_PrecioFinal);
        
    }
    
    /** Get Final Price.
    @return Final price for a detail inside a promotion */
    public java.math.BigDecimal getXX_VME_PrecioFinal() 
    {
        return get_ValueAsBigDecimal("XX_VME_PrecioFinal");
        
    }
    
    
}
