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
/** Generated Model for XX_VLO_WarehouseAssociated
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_WarehouseAssociated extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_WarehouseAssociated_ID id
    @param trx transaction
    */
    public X_XX_VLO_WarehouseAssociated (Ctx ctx, int XX_VLO_WarehouseAssociated_ID, Trx trx)
    {
        super (ctx, XX_VLO_WarehouseAssociated_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_WarehouseAssociated_ID == 0)
        {
            setXX_VLO_WarehouseAssociated_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_WarehouseAssociated (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27553557568789L;
    /** Last Updated Timestamp 2010-04-16 11:07:32.0 */
    public static final long updatedMS = 1271432252000L;
    /** AD_Table_ID=1000283 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_WarehouseAssociated");
        
    }
    ;
    
    /** TableName=XX_VLO_WarehouseAssociated */
    public static final String Table_Name="XX_VLO_WarehouseAssociated";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Fleet.
    @param XX_VLO_Fleet_ID Fleet */
    public void setXX_VLO_Fleet_ID (int XX_VLO_Fleet_ID)
    {
        if (XX_VLO_Fleet_ID <= 0) set_Value ("XX_VLO_Fleet_ID", null);
        else
        set_Value ("XX_VLO_Fleet_ID", Integer.valueOf(XX_VLO_Fleet_ID));
        
    }
    
    /** Get Fleet.
    @return Fleet */
    public int getXX_VLO_Fleet_ID() 
    {
        return get_ValueAsInt("XX_VLO_Fleet_ID");
        
    }
    
    /** Set XX_VLO_WarehouseAssociated_ID.
    @param XX_VLO_WarehouseAssociated_ID XX_VLO_WarehouseAssociated_ID */
    public void setXX_VLO_WarehouseAssociated_ID (int XX_VLO_WarehouseAssociated_ID)
    {
        if (XX_VLO_WarehouseAssociated_ID < 1) throw new IllegalArgumentException ("XX_VLO_WarehouseAssociated_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_WarehouseAssociated_ID", Integer.valueOf(XX_VLO_WarehouseAssociated_ID));
        
    }
    
    /** Get XX_VLO_WarehouseAssociated_ID.
    @return XX_VLO_WarehouseAssociated_ID */
    public int getXX_VLO_WarehouseAssociated_ID() 
    {
        return get_ValueAsInt("XX_VLO_WarehouseAssociated_ID");
        
    }
    
    
}
