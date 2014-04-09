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
package compiere.model.suppliesservices;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_ProductPercentDistrib
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_ProductPercentDistrib extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_ProductPercentDistrib_ID id
    @param trx transaction
    */
    public X_XX_ProductPercentDistrib (Ctx ctx, int XX_ProductPercentDistrib_ID, Trx trx)
    {
        super (ctx, XX_ProductPercentDistrib_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_ProductPercentDistrib_ID == 0)
        {
            setC_OrderLine_ID (0);
            setXX_ProductPercentDistrib_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_ProductPercentDistrib (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27621125111789L;
    /** Last Updated Timestamp 2012-06-06 11:53:15.0 */
    public static final long updatedMS = 1338999795000L;
    /** AD_Table_ID=1002459 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_ProductPercentDistrib");
        
    }
    ;
    
    /** TableName=XX_ProductPercentDistrib */
    public static final String Table_Name="XX_ProductPercentDistrib";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID <= 0) set_Value ("C_Order_ID", null);
        else
        set_Value ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** Set Order Line.
    @param C_OrderLine_ID Order Line */
    public void setC_OrderLine_ID (int C_OrderLine_ID)
    {
        if (C_OrderLine_ID < 1) throw new IllegalArgumentException ("C_OrderLine_ID is mandatory.");
        set_ValueNoCheck ("C_OrderLine_ID", Integer.valueOf(C_OrderLine_ID));
        
    }
    
    /** Get Order Line.
    @return Order Line */
    public int getC_OrderLine_ID() 
    {
        return get_ValueAsInt("C_OrderLine_ID");
        
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
    
    /** Set Amount Per Cost Center.
    @param XX_AmountPerCC Amount Per Cost Center */
    public void setXX_AmountPerCC (java.math.BigDecimal XX_AmountPerCC)
    {
        set_Value ("XX_AmountPerCC", XX_AmountPerCC);
        
    }
    
    /** Get Amount Per Cost Center.
    @return Amount Per Cost Center */
    public java.math.BigDecimal getXX_AmountPerCC() 
    {
        return get_ValueAsBigDecimal("XX_AmountPerCC");
        
    }
    
    /** Set Cost Center.
    @param XX_Org_ID Cost Center */
    public void setXX_Org_ID (int XX_Org_ID)
    {
        if (XX_Org_ID <= 0) set_Value ("XX_Org_ID", null);
        else
        set_Value ("XX_Org_ID", Integer.valueOf(XX_Org_ID));
        
    }
    
    /** Get Cost Center.
    @return Cost Center */
    public int getXX_Org_ID() 
    {
        return get_ValueAsInt("XX_Org_ID");
        
    }
    
    /** Set Percentage Per Cost Center.
    @param XX_PercentagePerCC Percentage Per Cost Center */
    public void setXX_PercentagePerCC (java.math.BigDecimal XX_PercentagePerCC)
    {
        set_Value ("XX_PercentagePerCC", XX_PercentagePerCC);
        
    }
    
    /** Get Percentage Per Cost Center.
    @return Percentage Per Cost Center */
    public java.math.BigDecimal getXX_PercentagePerCC() 
    {
        return get_ValueAsBigDecimal("XX_PercentagePerCC");
        
    }
    
    /** Set XX_ProductPercentDistrib_ID.
    @param XX_ProductPercentDistrib_ID XX_ProductPercentDistrib_ID */
    public void setXX_ProductPercentDistrib_ID (int XX_ProductPercentDistrib_ID)
    {
        if (XX_ProductPercentDistrib_ID < 1) throw new IllegalArgumentException ("XX_ProductPercentDistrib_ID is mandatory.");
        set_ValueNoCheck ("XX_ProductPercentDistrib_ID", Integer.valueOf(XX_ProductPercentDistrib_ID));
        
    }
    
    /** Get XX_ProductPercentDistrib_ID.
    @return XX_ProductPercentDistrib_ID */
    public int getXX_ProductPercentDistrib_ID() 
    {
        return get_ValueAsInt("XX_ProductPercentDistrib_ID");
        
    }
    
    /** Set Quantity Per Cost Center.
    @param XX_QuantityPerCC Quantity Per Cost Center */
    public void setXX_QuantityPerCC (java.math.BigDecimal XX_QuantityPerCC)
    {
        set_Value ("XX_QuantityPerCC", XX_QuantityPerCC);
        
    }
    
    /** Get Quantity Per Cost Center.
    @return Quantity Per Cost Center */
    public java.math.BigDecimal getXX_QuantityPerCC() 
    {
        return get_ValueAsBigDecimal("XX_QuantityPerCC");
        
    }
    
    
}
