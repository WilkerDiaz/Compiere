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
package compiere.model.promociones;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VME_DonationStore
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VME_DonationStore extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VME_DonationStore_ID id
    @param trx transaction
    */
    public X_XX_VME_DonationStore (Ctx ctx, int XX_VME_DonationStore_ID, Trx trx)
    {
        super (ctx, XX_VME_DonationStore_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VME_DonationStore_ID == 0)
        {
            setXX_VME_DonationStore_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VME_DonationStore (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27612590786789L;
    /** Last Updated Timestamp 2012-02-28 17:14:30.0 */
    public static final long updatedMS = 1330465470000L;
    /** AD_Table_ID=1000404 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VME_DonationStore");
        
    }
    ;
    
    /** TableName=XX_VME_DonationStore */
    public static final String Table_Name="XX_VME_DonationStore";
    
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
    
    /** Set XX_VME_Donations_ID.
    @param XX_VME_Donations_ID XX_VME_Donations_ID */
    public void setXX_VME_Donations_ID (int XX_VME_Donations_ID)
    {
        if (XX_VME_Donations_ID <= 0) set_Value ("XX_VME_Donations_ID", null);
        else
        set_Value ("XX_VME_Donations_ID", Integer.valueOf(XX_VME_Donations_ID));
        
    }
    
    /** Get XX_VME_Donations_ID.
    @return XX_VME_Donations_ID */
    public int getXX_VME_Donations_ID() 
    {
        return get_ValueAsInt("XX_VME_Donations_ID");
        
    }
    
    /** Set XX_VME_DonationStore_ID.
    @param XX_VME_DonationStore_ID XX_VME_DonationStore_ID */
    public void setXX_VME_DonationStore_ID (int XX_VME_DonationStore_ID)
    {
        if (XX_VME_DonationStore_ID < 1) throw new IllegalArgumentException ("XX_VME_DonationStore_ID is mandatory.");
        set_ValueNoCheck ("XX_VME_DonationStore_ID", Integer.valueOf(XX_VME_DonationStore_ID));
        
    }
    
    /** Get XX_VME_DonationStore_ID.
    @return XX_VME_DonationStore_ID */
    public int getXX_VME_DonationStore_ID() 
    {
        return get_ValueAsInt("XX_VME_DonationStore_ID");
        
    }
    
    
}
