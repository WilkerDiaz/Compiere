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
/** Generated Model for XX_VMR_StoreQuantityDistrib
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_StoreQuantityDistrib extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_StoreQuantityDistrib_ID id
    @param trx transaction
    */
    public X_XX_VMR_StoreQuantityDistrib (Ctx ctx, int XX_VMR_StoreQuantityDistrib_ID, Trx trx)
    {
        super (ctx, XX_VMR_StoreQuantityDistrib_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_StoreQuantityDistrib_ID == 0)
        {
            setM_Warehouse_ID (0);
            setXX_Quantity (0);
            setXX_VMR_DistributionDetail_ID (0);
            setXX_VMR_DistributionType_ID (0);
            setXX_VMR_StoreQuantityDistrib_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_StoreQuantityDistrib (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27578029709789L;
    /** Last Updated Timestamp 2011-01-24 16:56:33.0 */
    public static final long updatedMS = 1295904393000L;
    /** AD_Table_ID=1000210 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_StoreQuantityDistrib");
        
    }
    ;
    
    /** TableName=XX_VMR_StoreQuantityDistrib */
    public static final String Table_Name="XX_VMR_StoreQuantityDistrib";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID < 1) throw new IllegalArgumentException ("M_Warehouse_ID is mandatory.");
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
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
    
    /** Set Quantity.
    @param XX_Quantity Quantity */
    public void setXX_Quantity (int XX_Quantity)
    {
        set_Value ("XX_Quantity", Integer.valueOf(XX_Quantity));
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public int getXX_Quantity() 
    {
        return get_ValueAsInt("XX_Quantity");
        
    }
    
    /** Set XX_VMR_DistributionDetail_ID.
    @param XX_VMR_DistributionDetail_ID Distribution Detail */
    public void setXX_VMR_DistributionDetail_ID (int XX_VMR_DistributionDetail_ID)
    {
        if (XX_VMR_DistributionDetail_ID < 1) throw new IllegalArgumentException ("XX_VMR_DistributionDetail_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_DistributionDetail_ID", Integer.valueOf(XX_VMR_DistributionDetail_ID));
        
    }
    
    /** Get XX_VMR_DistributionDetail_ID.
    @return Distribution Detail */
    public int getXX_VMR_DistributionDetail_ID() 
    {
        return get_ValueAsInt("XX_VMR_DistributionDetail_ID");
        
    }
    
    /** Set Distribution Type.
    @param XX_VMR_DistributionType_ID Distribution Type */
    public void setXX_VMR_DistributionType_ID (int XX_VMR_DistributionType_ID)
    {
        if (XX_VMR_DistributionType_ID < 1) throw new IllegalArgumentException ("XX_VMR_DistributionType_ID is mandatory.");
        set_Value ("XX_VMR_DistributionType_ID", Integer.valueOf(XX_VMR_DistributionType_ID));
        
    }
    
    /** Get Distribution Type.
    @return Distribution Type */
    public int getXX_VMR_DistributionType_ID() 
    {
        return get_ValueAsInt("XX_VMR_DistributionType_ID");
        
    }
    
    /** Set XX_VMR_StoreQuantityDistrib_ID.
    @param XX_VMR_StoreQuantityDistrib_ID XX_VMR_StoreQuantityDistrib_ID */
    public void setXX_VMR_StoreQuantityDistrib_ID (int XX_VMR_StoreQuantityDistrib_ID)
    {
        if (XX_VMR_StoreQuantityDistrib_ID < 1) throw new IllegalArgumentException ("XX_VMR_StoreQuantityDistrib_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_StoreQuantityDistrib_ID", Integer.valueOf(XX_VMR_StoreQuantityDistrib_ID));
        
    }
    
    /** Get XX_VMR_StoreQuantityDistrib_ID.
    @return XX_VMR_StoreQuantityDistrib_ID */
    public int getXX_VMR_StoreQuantityDistrib_ID() 
    {
        return get_ValueAsInt("XX_VMR_StoreQuantityDistrib_ID");
        
    }
    
    
}
