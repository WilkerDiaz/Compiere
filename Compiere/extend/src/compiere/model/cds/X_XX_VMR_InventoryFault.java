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
/** Generated Model for XX_VMR_InventoryFault
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_InventoryFault extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_InventoryFault_ID id
    @param trx transaction
    */
    public X_XX_VMR_InventoryFault (Ctx ctx, int XX_VMR_InventoryFault_ID, Trx trx)
    {
        super (ctx, XX_VMR_InventoryFault_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_InventoryFault_ID == 0)
        {
            setXX_VMR_INVENTORYFAULT_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_InventoryFault (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27612764144789L;
    /** Last Updated Timestamp 2012-03-01 17:23:48.0 */
    public static final long updatedMS = 1330638828000L;
    /** AD_Table_ID=1001653 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_InventoryFault");
        
    }
    ;
    
    /** TableName=XX_VMR_InventoryFault */
    public static final String Table_Name="XX_VMR_InventoryFault";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set ClosedMonth.
    @param ClosedMonth ClosedMonth */
    public void setClosedMonth (int ClosedMonth)
    {
        set_Value ("ClosedMonth", Integer.valueOf(ClosedMonth));
        
    }
    
    /** Get ClosedMonth.
    @return ClosedMonth */
    public int getClosedMonth() 
    {
        return get_ValueAsInt("ClosedMonth");
        
    }
    
    /** Set FaultPercentage.
    @param FaultPercentage FaultPercentage */
    public void setFaultPercentage (java.math.BigDecimal FaultPercentage)
    {
        set_Value ("FaultPercentage", FaultPercentage);
        
    }
    
    /** Get FaultPercentage.
    @return FaultPercentage */
    public java.math.BigDecimal getFaultPercentage() 
    {
        return get_ValueAsBigDecimal("FaultPercentage");
        
    }
    
    /** Set InventoryYear.
    @param InventoryYear InventoryYear */
    public void setInventoryYear (int InventoryYear)
    {
        set_Value ("InventoryYear", Integer.valueOf(InventoryYear));
        
    }
    
    /** Get InventoryYear.
    @return InventoryYear */
    public int getInventoryYear() 
    {
        return get_ValueAsInt("InventoryYear");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
        else
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
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
    
    /** % de falla por referencia básica por tienda = 1 */
    public static final String TYPEINVENTORYFAULT_DeFallaPorReferenciaBásicaPorTienda = X_Ref_TypeInventoryFault.DE_FALLA_POR_REFERENCIA_BÁSICA_POR_TIENDA.getValue();
    /** % de falla por referencia básica Consolidada compañía = 2 */
    public static final String TYPEINVENTORYFAULT_DeFallaPorReferenciaBásicaConsolidadaCompañía = X_Ref_TypeInventoryFault.DE_FALLA_POR_REFERENCIA_BÁSICA_CONSOLIDADA_COMPAÑÍA.getValue();
    /** % de falla por tipo de básico Consolidado compañía = 3 */
    public static final String TYPEINVENTORYFAULT_DeFallaPorTipoDeBásicoConsolidadoCompañía = X_Ref_TypeInventoryFault.DE_FALLA_POR_TIPO_DE_BÁSICO_CONSOLIDADO_COMPAÑÍA.getValue();
    /** % de falla por tipo de básico por tienda = 4 */
    public static final String TYPEINVENTORYFAULT_DeFallaPorTipoDeBásicoPorTienda = X_Ref_TypeInventoryFault.DE_FALLA_POR_TIPO_DE_BÁSICO_POR_TIENDA.getValue();
    /** % de falla por producto  por tienda = 5 */
    public static final String TYPEINVENTORYFAULT_DeFallaPorProductoPorTienda = X_Ref_TypeInventoryFault.DE_FALLA_POR_PRODUCTO_POR_TIENDA.getValue();
    /** % de falla por producto por básico compañía = 6 */
    public static final String TYPEINVENTORYFAULT_DeFallaPorProductoPorBásicoCompañía = X_Ref_TypeInventoryFault.DE_FALLA_POR_PRODUCTO_POR_BÁSICO_COMPAÑÍA.getValue();
    /** Set TypeInventoryFault.
    @param TypeInventoryFault TypeInventoryFault */
    public void setTypeInventoryFault (String TypeInventoryFault)
    {
        if (!X_Ref_TypeInventoryFault.isValid(TypeInventoryFault))
        throw new IllegalArgumentException ("TypeInventoryFault Invalid value - " + TypeInventoryFault + " - Reference_ID=1001349 - 1 - 2 - 3 - 4 - 5 - 6");
        set_Value ("TypeInventoryFault", TypeInventoryFault);
        
    }
    
    /** Get TypeInventoryFault.
    @return TypeInventoryFault */
    public String getTypeInventoryFault() 
    {
        return (String)get_Value("TypeInventoryFault");
        
    }
    
    /** Set XX_VMR_INVENTORYFAULT_ID.
    @param XX_VMR_INVENTORYFAULT_ID XX_VMR_INVENTORYFAULT_ID */
    public void setXX_VMR_INVENTORYFAULT_ID (int XX_VMR_INVENTORYFAULT_ID)
    {
        if (XX_VMR_INVENTORYFAULT_ID < 1) throw new IllegalArgumentException ("XX_VMR_INVENTORYFAULT_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_INVENTORYFAULT_ID", Integer.valueOf(XX_VMR_INVENTORYFAULT_ID));
        
    }
    
    /** Get XX_VMR_INVENTORYFAULT_ID.
    @return XX_VMR_INVENTORYFAULT_ID */
    public int getXX_VMR_INVENTORYFAULT_ID() 
    {
        return get_ValueAsInt("XX_VMR_INVENTORYFAULT_ID");
        
    }
    
    /** Set XX_VMR_TypeBasic_ID.
    @param XX_VMR_TypeBasic_ID XX_VMR_TypeBasic_ID */
    public void setXX_VMR_TypeBasic_ID (int XX_VMR_TypeBasic_ID)
    {
        if (XX_VMR_TypeBasic_ID <= 0) set_Value ("XX_VMR_TypeBasic_ID", null);
        else
        set_Value ("XX_VMR_TypeBasic_ID", Integer.valueOf(XX_VMR_TypeBasic_ID));
        
    }
    
    /** Get XX_VMR_TypeBasic_ID.
    @return XX_VMR_TypeBasic_ID */
    public int getXX_VMR_TypeBasic_ID() 
    {
        return get_ValueAsInt("XX_VMR_TypeBasic_ID");
        
    }
    
    /** Set Vendor Product Reference.
    @param XX_VMR_VendorProdRef_ID Vendor Product Reference */
    public void setXX_VMR_VendorProdRef_ID (int XX_VMR_VendorProdRef_ID)
    {
        if (XX_VMR_VendorProdRef_ID <= 0) set_Value ("XX_VMR_VendorProdRef_ID", null);
        else
        set_Value ("XX_VMR_VendorProdRef_ID", Integer.valueOf(XX_VMR_VendorProdRef_ID));
        
    }
    
    /** Get Vendor Product Reference.
    @return Vendor Product Reference */
    public int getXX_VMR_VendorProdRef_ID() 
    {
        return get_ValueAsInt("XX_VMR_VendorProdRef_ID");
        
    }
    
    
}
