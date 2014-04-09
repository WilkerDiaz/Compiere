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
/** Generated Model for XX_POTotalDistrib
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id$ */
public class X_XX_POTotalDistrib extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_POTotalDistrib_ID id
    @param trxName transaction
    */
    public X_XX_POTotalDistrib (Ctx ctx, int XX_POTotalDistrib_ID, Trx trxName)
    {
        super (ctx, XX_POTotalDistrib_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_POTotalDistrib_ID == 0)
        {
            setName (null);
            setValue (null);
            setXX_POTOTALDISTRIB_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_XX_POTotalDistrib (Ctx ctx, ResultSet rs, Trx trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27571440687789L;
    /** Last Updated Timestamp 2010-11-09 11:09:31.0 */
    public static final long updatedMS = 1289315371000L;
    /** AD_Table_ID=1000348 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_POTotalDistrib");
        
    }
    ;
    
    /** TableName=XX_POTotalDistrib */
    public static final String Table_Name="XX_POTotalDistrib";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"XX_POTotalDistrib");

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
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
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
    
    /** Set XX_POTOTALDISTRIB_ID.
    @param XX_POTOTALDISTRIB_ID XX_POTOTALDISTRIB_ID */
    public void setXX_POTOTALDISTRIB_ID (int XX_POTOTALDISTRIB_ID)
    {
        if (XX_POTOTALDISTRIB_ID < 1) throw new IllegalArgumentException ("XX_POTOTALDISTRIB_ID is mandatory.");
        set_ValueNoCheck ("XX_POTOTALDISTRIB_ID", Integer.valueOf(XX_POTOTALDISTRIB_ID));
        
    }
    
    /** Get XX_POTOTALDISTRIB_ID.
    @return XX_POTOTALDISTRIB_ID */
    public int getXX_POTOTALDISTRIB_ID() 
    {
        return get_ValueAsInt("XX_POTOTALDISTRIB_ID");
        
    }
    
    /** Set Total Amount Cost Centers.
    @param XX_TotalAmountCC Total Amount Cost Centers */
    public void setXX_TotalAmountCC (java.math.BigDecimal XX_TotalAmountCC)
    {
        set_Value ("XX_TotalAmountCC", XX_TotalAmountCC);
        
    }
    
    /** Get Total Amount Cost Centers.
    @return Total Amount Cost Centers */
    public java.math.BigDecimal getXX_TotalAmountCC() 
    {
        return get_ValueAsBigDecimal("XX_TotalAmountCC");
        
    }
    
    /** Set Total Cost Centers.
    @param XX_TotalCostCenters Total Cost Centers */
    public void setXX_TotalCostCenters (java.math.BigDecimal XX_TotalCostCenters)
    {
        set_Value ("XX_TotalCostCenters", XX_TotalCostCenters);
        
    }
    
    /** Get Total Cost Centers.
    @return Total Cost Centers */
    public java.math.BigDecimal getXX_TotalCostCenters() 
    {
        return get_ValueAsBigDecimal("XX_TotalCostCenters");
        
    }
    
    
}
