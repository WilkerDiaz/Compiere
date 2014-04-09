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
package compiere.model.dynamic;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VME_BrochureInvStatus
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id: GenerateModel.java 8757 2010-05-12 21:32:32Z nnayak $ */
public class X_XX_VME_BrochureInvStatus extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VME_BrochureInvStatus_ID id
    @param trx transaction
    */
    public X_XX_VME_BrochureInvStatus (Ctx ctx, int XX_VME_BrochureInvStatus_ID, Trx trx)
    {
        super (ctx, XX_VME_BrochureInvStatus_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VME_BrochureInvStatus_ID == 0)
        {
            setValue (null);
            setXX_PRODUCTO_ID (0);
            setXX_VMA_Brochure_ID (0);
            setXX_VME_BROCHUREINVSTATUS_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VME_BrochureInvStatus (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27562291867789L;
    /** Last Updated Timestamp 2010-07-26 13:19:11.0 */
    public static final long updatedMS = 1280166551000L;
    /** AD_Table_ID=1000064 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VME_BrochureInvStatus");
        
    }
    ;
    
    /** TableName=XX_VME_BrochureInvStatus */
    public static final String Table_Name="XX_VME_BrochureInvStatus";
    
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
    
    /** Set Producto.
    @param XX_PRODUCTO_ID Producto */
    public void setXX_PRODUCTO_ID (int XX_PRODUCTO_ID)
    {
        if (XX_PRODUCTO_ID < 1) throw new IllegalArgumentException ("XX_PRODUCTO_ID is mandatory.");
        set_Value ("XX_PRODUCTO_ID", Integer.valueOf(XX_PRODUCTO_ID));
        
    }
    
    /** Get Producto.
    @return Producto */
    public int getXX_PRODUCTO_ID() 
    {
        return get_ValueAsInt("XX_PRODUCTO_ID");
        
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
    
    /** Set Brochure.
    @param XX_VMA_Brochure_ID Identifier of the Brochure. */
    public void setXX_VMA_Brochure_ID (int XX_VMA_Brochure_ID)
    {
        if (XX_VMA_Brochure_ID < 1) throw new IllegalArgumentException ("XX_VMA_Brochure_ID is mandatory.");
        set_Value ("XX_VMA_Brochure_ID", Integer.valueOf(XX_VMA_Brochure_ID));
        
    }
    
    /** Get Brochure.
    @return Identifier of the Brochure. */
    public int getXX_VMA_Brochure_ID() 
    {
        return get_ValueAsInt("XX_VMA_Brochure_ID");
        
    }
    
    /** Set XX_VME_AnoInv.
    @param XX_VME_AnoInv XX_VME_AnoInv */
    public void setXX_VME_AnoInv (java.math.BigDecimal XX_VME_AnoInv)
    {
        set_Value ("XX_VME_AnoInv", XX_VME_AnoInv);
        
    }
    
    /** Get XX_VME_AnoInv.
    @return XX_VME_AnoInv */
    public java.math.BigDecimal getXX_VME_AnoInv() 
    {
        return get_ValueAsBigDecimal("XX_VME_AnoInv");
        
    }
    
    /** Set XX_VME_BROCHUREINVSTATUS_ID.
    @param XX_VME_BROCHUREINVSTATUS_ID XX_VME_BROCHUREINVSTATUS_ID */
    public void setXX_VME_BROCHUREINVSTATUS_ID (int XX_VME_BROCHUREINVSTATUS_ID)
    {
        if (XX_VME_BROCHUREINVSTATUS_ID < 1) throw new IllegalArgumentException ("XX_VME_BROCHUREINVSTATUS_ID is mandatory.");
        set_ValueNoCheck ("XX_VME_BROCHUREINVSTATUS_ID", Integer.valueOf(XX_VME_BROCHUREINVSTATUS_ID));
        
    }
    
    /** Get XX_VME_BROCHUREINVSTATUS_ID.
    @return XX_VME_BROCHUREINVSTATUS_ID */
    public int getXX_VME_BROCHUREINVSTATUS_ID() 
    {
        return get_ValueAsInt("XX_VME_BROCHUREINVSTATUS_ID");
        
    }
    
    /** Set XX_VME_CantAjuAnt.
    @param XX_VME_CantAjuAnt XX_VME_CantAjuAnt */
    public void setXX_VME_CantAjuAnt (java.math.BigDecimal XX_VME_CantAjuAnt)
    {
        set_Value ("XX_VME_CantAjuAnt", XX_VME_CantAjuAnt);
        
    }
    
    /** Get XX_VME_CantAjuAnt.
    @return XX_VME_CantAjuAnt */
    public java.math.BigDecimal getXX_VME_CantAjuAnt() 
    {
        return get_ValueAsBigDecimal("XX_VME_CantAjuAnt");
        
    }
    
    /** Set XX_VME_CantAjuste.
    @param XX_VME_CantAjuste XX_VME_CantAjuste */
    public void setXX_VME_CantAjuste (java.math.BigDecimal XX_VME_CantAjuste)
    {
        set_Value ("XX_VME_CantAjuste", XX_VME_CantAjuste);
        
    }
    
    /** Get XX_VME_CantAjuste.
    @return XX_VME_CantAjuste */
    public java.math.BigDecimal getXX_VME_CantAjuste() 
    {
        return get_ValueAsBigDecimal("XX_VME_CantAjuste");
        
    }
    
    /** Set XX_VME_CantCompra.
    @param XX_VME_CantCompra XX_VME_CantCompra */
    public void setXX_VME_CantCompra (java.math.BigDecimal XX_VME_CantCompra)
    {
        set_Value ("XX_VME_CantCompra", XX_VME_CantCompra);
        
    }
    
    /** Get XX_VME_CantCompra.
    @return XX_VME_CantCompra */
    public java.math.BigDecimal getXX_VME_CantCompra() 
    {
        return get_ValueAsBigDecimal("XX_VME_CantCompra");
        
    }
    
    /** Set XX_VME_CantInvAct.
    @param XX_VME_CantInvAct XX_VME_CantInvAct */
    public void setXX_VME_CantInvAct (java.math.BigDecimal XX_VME_CantInvAct)
    {
        set_Value ("XX_VME_CantInvAct", XX_VME_CantInvAct);
        
    }
    
    /** Get XX_VME_CantInvAct.
    @return XX_VME_CantInvAct */
    public java.math.BigDecimal getXX_VME_CantInvAct() 
    {
        return get_ValueAsBigDecimal("XX_VME_CantInvAct");
        
    }
    
    /** Set XX_VME_CantInvIni.
    @param XX_VME_CantInvIni XX_VME_CantInvIni */
    public void setXX_VME_CantInvIni (java.math.BigDecimal XX_VME_CantInvIni)
    {
        set_Value ("XX_VME_CantInvIni", XX_VME_CantInvIni);
        
    }
    
    /** Get XX_VME_CantInvIni.
    @return XX_VME_CantInvIni */
    public java.math.BigDecimal getXX_VME_CantInvIni() 
    {
        return get_ValueAsBigDecimal("XX_VME_CantInvIni");
        
    }
    
    /** Set XX_VME_CantMovimi.
    @param XX_VME_CantMovimi XX_VME_CantMovimi */
    public void setXX_VME_CantMovimi (java.math.BigDecimal XX_VME_CantMovimi)
    {
        set_Value ("XX_VME_CantMovimi", XX_VME_CantMovimi);
        
    }
    
    /** Get XX_VME_CantMovimi.
    @return XX_VME_CantMovimi */
    public java.math.BigDecimal getXX_VME_CantMovimi() 
    {
        return get_ValueAsBigDecimal("XX_VME_CantMovimi");
        
    }
    
    /** Set XX_VME_CantVentas.
    @param XX_VME_CantVentas XX_VME_CantVentas */
    public void setXX_VME_CantVentas (java.math.BigDecimal XX_VME_CantVentas)
    {
        set_Value ("XX_VME_CantVentas", XX_VME_CantVentas);
        
    }
    
    /** Get XX_VME_CantVentas.
    @return XX_VME_CantVentas */
    public java.math.BigDecimal getXX_VME_CantVentas() 
    {
        return get_ValueAsBigDecimal("XX_VME_CantVentas");
        
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
    
    /** Set Date when the inventory status was created.
    @param XX_VME_DateStatusInv Date when the inventory status was created */
    public void setXX_VME_DateStatusInv (Timestamp XX_VME_DateStatusInv)
    {
        set_Value ("XX_VME_DateStatusInv", XX_VME_DateStatusInv);
        
    }
    
    /** Get Date when the inventory status was created.
    @return Date when the inventory status was created */
    public Timestamp getXX_VME_DateStatusInv() 
    {
        return (Timestamp)get_Value("XX_VME_DateStatusInv");
        
    }
    
    /** Set XX_VME_MesInv.
    @param XX_VME_MesInv XX_VME_MesInv */
    public void setXX_VME_MesInv (java.math.BigDecimal XX_VME_MesInv)
    {
        set_Value ("XX_VME_MesInv", XX_VME_MesInv);
        
    }
    
    /** Get XX_VME_MesInv.
    @return XX_VME_MesInv */
    public java.math.BigDecimal getXX_VME_MesInv() 
    {
        return get_ValueAsBigDecimal("XX_VME_MesInv");
        
    }
    
    /** Set XX_VME_MontAjuAnt.
    @param XX_VME_MontAjuAnt XX_VME_MontAjuAnt */
    public void setXX_VME_MontAjuAnt (java.math.BigDecimal XX_VME_MontAjuAnt)
    {
        set_Value ("XX_VME_MontAjuAnt", XX_VME_MontAjuAnt);
        
    }
    
    /** Get XX_VME_MontAjuAnt.
    @return XX_VME_MontAjuAnt */
    public java.math.BigDecimal getXX_VME_MontAjuAnt() 
    {
        return get_ValueAsBigDecimal("XX_VME_MontAjuAnt");
        
    }
    
    /** Set XX_VME_MontAjuste.
    @param XX_VME_MontAjuste XX_VME_MontAjuste */
    public void setXX_VME_MontAjuste (java.math.BigDecimal XX_VME_MontAjuste)
    {
        set_Value ("XX_VME_MontAjuste", XX_VME_MontAjuste);
        
    }
    
    /** Get XX_VME_MontAjuste.
    @return XX_VME_MontAjuste */
    public java.math.BigDecimal getXX_VME_MontAjuste() 
    {
        return get_ValueAsBigDecimal("XX_VME_MontAjuste");
        
    }
    
    /** Set XX_VME_MontCompra.
    @param XX_VME_MontCompra XX_VME_MontCompra */
    public void setXX_VME_MontCompra (java.math.BigDecimal XX_VME_MontCompra)
    {
        set_Value ("XX_VME_MontCompra", XX_VME_MontCompra);
        
    }
    
    /** Get XX_VME_MontCompra.
    @return XX_VME_MontCompra */
    public java.math.BigDecimal getXX_VME_MontCompra() 
    {
        return get_ValueAsBigDecimal("XX_VME_MontCompra");
        
    }
    
    /** Set XX_VME_MontInvIni.
    @param XX_VME_MontInvIni XX_VME_MontInvIni */
    public void setXX_VME_MontInvIni (java.math.BigDecimal XX_VME_MontInvIni)
    {
        set_Value ("XX_VME_MontInvIni", XX_VME_MontInvIni);
        
    }
    
    /** Get XX_VME_MontInvIni.
    @return XX_VME_MontInvIni */
    public java.math.BigDecimal getXX_VME_MontInvIni() 
    {
        return get_ValueAsBigDecimal("XX_VME_MontInvIni");
        
    }
    
    /** Set XX_VME_MontMovimi.
    @param XX_VME_MontMovimi XX_VME_MontMovimi */
    public void setXX_VME_MontMovimi (java.math.BigDecimal XX_VME_MontMovimi)
    {
        set_Value ("XX_VME_MontMovimi", XX_VME_MontMovimi);
        
    }
    
    /** Get XX_VME_MontMovimi.
    @return XX_VME_MontMovimi */
    public java.math.BigDecimal getXX_VME_MontMovimi() 
    {
        return get_ValueAsBigDecimal("XX_VME_MontMovimi");
        
    }
    
    /** Set XX_VME_MontVentas.
    @param XX_VME_MontVentas XX_VME_MontVentas */
    public void setXX_VME_MontVentas (java.math.BigDecimal XX_VME_MontVentas)
    {
        set_Value ("XX_VME_MontVentas", XX_VME_MontVentas);
        
    }
    
    /** Get XX_VME_MontVentas.
    @return XX_VME_MontVentas */
    public java.math.BigDecimal getXX_VME_MontVentas() 
    {
        return get_ValueAsBigDecimal("XX_VME_MontVentas");
        
    }
    
    /** Set XX_VME_Tienda.
    @param XX_VME_Tienda XX_VME_Tienda */
    public void setXX_VME_Tienda (java.math.BigDecimal XX_VME_Tienda)
    {
        set_Value ("XX_VME_Tienda", XX_VME_Tienda);
        
    }
    
    /** Get XX_VME_Tienda.
    @return XX_VME_Tienda */
    public java.math.BigDecimal getXX_VME_Tienda() 
    {
        return get_ValueAsBigDecimal("XX_VME_Tienda");
        
    }
	
    
    
}
