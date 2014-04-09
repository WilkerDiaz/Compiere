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
/** Generated Model for XX_VMR_RangeDaysPromote
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_RangeDaysPromote extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_RangeDaysPromote_ID id
    @param trx transaction
    */
    public X_XX_VMR_RangeDaysPromote (Ctx ctx, int XX_VMR_RangeDaysPromote_ID, Trx trx)
    {
        super (ctx, XX_VMR_RangeDaysPromote_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_RangeDaysPromote_ID == 0)
        {
            setXX_VMR_RangeDaysPromote_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_RangeDaysPromote (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27562993475789L;
    /** Last Updated Timestamp 2010-08-03 16:12:39.0 */
    public static final long updatedMS = 1280868159000L;
    /** AD_Table_ID=1000342 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_RangeDaysPromote");
        
    }
    ;
    
    /** TableName=XX_VMR_RangeDaysPromote */
    public static final String Table_Name="XX_VMR_RangeDaysPromote";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Range 1.
    @param XX_Range1 Initial Days */
    public void setXX_Range1 (int XX_Range1)
    {
        set_Value ("XX_Range1", Integer.valueOf(XX_Range1));
        
    }
    
    /** Get Range 1.
    @return Initial Days */
    public int getXX_Range1() 
    {
        return get_ValueAsInt("XX_Range1");
        
    }
    
    /** Set Range 2.
    @param XX_Range2 Intermediate Days */
    public void setXX_Range2 (int XX_Range2)
    {
        set_Value ("XX_Range2", Integer.valueOf(XX_Range2));
        
    }
    
    /** Get Range 2.
    @return Intermediate Days */
    public int getXX_Range2() 
    {
        return get_ValueAsInt("XX_Range2");
        
    }
    
    /** Set Range 3.
    @param XX_Range3 Final Days */
    public void setXX_Range3 (int XX_Range3)
    {
        set_Value ("XX_Range3", Integer.valueOf(XX_Range3));
        
    }
    
    /** Get Range 3.
    @return Final Days */
    public int getXX_Range3() 
    {
        return get_ValueAsInt("XX_Range3");
        
    }
    
    /** Set Category.
    @param XX_VMR_Category_ID Category */
    public void setXX_VMR_Category_ID (int XX_VMR_Category_ID)
    {
        if (XX_VMR_Category_ID <= 0) set_Value ("XX_VMR_Category_ID", null);
        else
        set_Value ("XX_VMR_Category_ID", Integer.valueOf(XX_VMR_Category_ID));
        
    }
    
    /** Get Category.
    @return Category */
    public int getXX_VMR_Category_ID() 
    {
        return get_ValueAsInt("XX_VMR_Category_ID");
        
    }
    
    /** Set XX_VMR_RangeDaysPromote_ID.
    @param XX_VMR_RangeDaysPromote_ID XX_VMR_RangeDaysPromote_ID */
    public void setXX_VMR_RangeDaysPromote_ID (int XX_VMR_RangeDaysPromote_ID)
    {
        if (XX_VMR_RangeDaysPromote_ID < 1) throw new IllegalArgumentException ("XX_VMR_RangeDaysPromote_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_RangeDaysPromote_ID", Integer.valueOf(XX_VMR_RangeDaysPromote_ID));
        
    }
    
    /** Get XX_VMR_RangeDaysPromote_ID.
    @return XX_VMR_RangeDaysPromote_ID */
    public int getXX_VMR_RangeDaysPromote_ID() 
    {
        return get_ValueAsInt("XX_VMR_RangeDaysPromote_ID");
        
    }
    
    /** Set Type Inventory.
    @param XX_VMR_TypeInventory_ID Tipo de Inventario */
    public void setXX_VMR_TypeInventory_ID (int XX_VMR_TypeInventory_ID)
    {
        if (XX_VMR_TypeInventory_ID <= 0) set_Value ("XX_VMR_TypeInventory_ID", null);
        else
        set_Value ("XX_VMR_TypeInventory_ID", Integer.valueOf(XX_VMR_TypeInventory_ID));
        
    }
    
    /** Get Type Inventory.
    @return Tipo de Inventario */
    public int getXX_VMR_TypeInventory_ID() 
    {
        return get_ValueAsInt("XX_VMR_TypeInventory_ID");
        
    }
    
    
}
