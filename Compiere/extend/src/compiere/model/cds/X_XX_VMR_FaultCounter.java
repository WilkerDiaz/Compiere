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
/** Generated Model for XX_VMR_FaultCounter
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_FaultCounter extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_FaultCounter_ID id
    @param trx transaction
    */
    public X_XX_VMR_FaultCounter (Ctx ctx, int XX_VMR_FaultCounter_ID, Trx trx)
    {
        super (ctx, XX_VMR_FaultCounter_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_FaultCounter_ID == 0)
        {
            setXX_VMR_FAULTCOUNTER_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_FaultCounter (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27610257259789L;
    /** Last Updated Timestamp 2012-02-01 17:02:23.0 */
    public static final long updatedMS = 1328131943000L;
    /** AD_Table_ID=1001253 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_FaultCounter");
        
    }
    ;
    
    /** TableName=XX_VMR_FaultCounter */
    public static final String Table_Name="XX_VMR_FaultCounter";
    
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
    
    /** Set XX_Counter.
    @param XX_Counter XX_Counter */
    public void setXX_Counter (int XX_Counter)
    {
        set_Value ("XX_Counter", Integer.valueOf(XX_Counter));
        
    }
    
    /** Get XX_Counter.
    @return XX_Counter */
    public int getXX_Counter() 
    {
        return get_ValueAsInt("XX_Counter");
        
    }
    
    /** Set XX_VMR_FAULTCOUNTER_ID.
    @param XX_VMR_FAULTCOUNTER_ID XX_VMR_FAULTCOUNTER_ID */
    public void setXX_VMR_FAULTCOUNTER_ID (int XX_VMR_FAULTCOUNTER_ID)
    {
        if (XX_VMR_FAULTCOUNTER_ID < 1) throw new IllegalArgumentException ("XX_VMR_FAULTCOUNTER_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_FAULTCOUNTER_ID", Integer.valueOf(XX_VMR_FAULTCOUNTER_ID));
        
    }
    
    /** Get XX_VMR_FAULTCOUNTER_ID.
    @return XX_VMR_FAULTCOUNTER_ID */
    public int getXX_VMR_FAULTCOUNTER_ID() 
    {
        return get_ValueAsInt("XX_VMR_FAULTCOUNTER_ID");
        
    }
    
    
}
