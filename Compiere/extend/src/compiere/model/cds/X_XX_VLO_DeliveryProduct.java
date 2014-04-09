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
/** Generated Model for XX_VLO_DeliveryProduct
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_DeliveryProduct extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_DeliveryProduct_ID id
    @param trx transaction
    */
    public X_XX_VLO_DeliveryProduct (Ctx ctx, int XX_VLO_DeliveryProduct_ID, Trx trx)
    {
        super (ctx, XX_VLO_DeliveryProduct_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_DeliveryProduct_ID == 0)
        {
            setM_Product_ID (0);
            setXX_VLO_ClientDelivery_ID (0);
            setXX_VLO_DeliveryProduct_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_DeliveryProduct (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27599560882789L;
    /** Last Updated Timestamp 2011-09-30 21:49:26.0 */
    public static final long updatedMS = 1317435566000L;
    /** AD_Table_ID=1000396 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_DeliveryProduct");
        
    }
    ;
    
    /** TableName=XX_VLO_DeliveryProduct */
    public static final String Table_Name="XX_VLO_DeliveryProduct";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Quantity.
    @param Quantity Quantity */
    public void setQuantity (int Quantity)
    {
        set_Value ("Quantity", Integer.valueOf(Quantity));
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public int getQuantity() 
    {
        return get_ValueAsInt("Quantity");
        
    }
    
    /** Set XX_VLO_ClientDelivery_ID.
    @param XX_VLO_ClientDelivery_ID XX_VLO_ClientDelivery_ID */
    public void setXX_VLO_ClientDelivery_ID (int XX_VLO_ClientDelivery_ID)
    {
        if (XX_VLO_ClientDelivery_ID < 1) throw new IllegalArgumentException ("XX_VLO_ClientDelivery_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_ClientDelivery_ID", Integer.valueOf(XX_VLO_ClientDelivery_ID));
        
    }
    
    /** Get XX_VLO_ClientDelivery_ID.
    @return XX_VLO_ClientDelivery_ID */
    public int getXX_VLO_ClientDelivery_ID() 
    {
        return get_ValueAsInt("XX_VLO_ClientDelivery_ID");
        
    }
    
    /** Set XX_VLO_DeliveryProduct_ID.
    @param XX_VLO_DeliveryProduct_ID XX_VLO_DeliveryProduct_ID */
    public void setXX_VLO_DeliveryProduct_ID (int XX_VLO_DeliveryProduct_ID)
    {
        if (XX_VLO_DeliveryProduct_ID < 1) throw new IllegalArgumentException ("XX_VLO_DeliveryProduct_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_DeliveryProduct_ID", Integer.valueOf(XX_VLO_DeliveryProduct_ID));
        
    }
    
    /** Get XX_VLO_DeliveryProduct_ID.
    @return XX_VLO_DeliveryProduct_ID */
    public int getXX_VLO_DeliveryProduct_ID() 
    {
        return get_ValueAsInt("XX_VLO_DeliveryProduct_ID");
        
    }
    
    /** Set Brand.
    @param XX_VMR_Brand_ID Id de la Tabla XX_VMR_BRAND(Marca) */
    public void setXX_VMR_Brand_ID (int XX_VMR_Brand_ID)
    {
        throw new IllegalArgumentException ("XX_VMR_Brand_ID is virtual column");
        
    }
    
    /** Get Brand.
    @return Id de la Tabla XX_VMR_BRAND(Marca) */
    public int getXX_VMR_Brand_ID() 
    {
        return get_ValueAsInt("XX_VMR_Brand_ID");
        
    }
    
    /** Set Line.
    @param XX_VMR_Line_ID Line */
    public void setXX_VMR_Line_ID (int XX_VMR_Line_ID)
    {
        throw new IllegalArgumentException ("XX_VMR_Line_ID is virtual column");
        
    }
    
    /** Get Line.
    @return Line */
    public int getXX_VMR_Line_ID() 
    {
        return get_ValueAsInt("XX_VMR_Line_ID");
        
    }
    
    /** Set Section.
    @param XX_VMR_Section_ID Section */
    public void setXX_VMR_Section_ID (int XX_VMR_Section_ID)
    {
        throw new IllegalArgumentException ("XX_VMR_Section_ID is virtual column");
        
    }
    
    /** Get Section.
    @return Section */
    public int getXX_VMR_Section_ID() 
    {
        return get_ValueAsInt("XX_VMR_Section_ID");
        
    }
    
    
}
