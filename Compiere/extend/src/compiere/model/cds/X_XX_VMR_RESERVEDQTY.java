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
/** Generated Model for XX_VMR_RESERVEDQTY
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_RESERVEDQTY extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_RESERVEDQTY_ID id
    @param trx transaction
    */
    public X_XX_VMR_RESERVEDQTY (Ctx ctx, int XX_VMR_RESERVEDQTY_ID, Trx trx)
    {
        super (ctx, XX_VMR_RESERVEDQTY_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_RESERVEDQTY_ID == 0)
        {
            setName (null);
            setValue (null);
            setXX_VMR_RESERVEDQTY_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_RESERVEDQTY (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27620965376789L;
    /** Last Updated Timestamp 2012-06-04 15:31:00.0 */
    public static final long updatedMS = 1338840060000L;
    /** AD_Table_ID=1002253 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_RESERVEDQTY");
        
    }
    ;
    
    /** TableName=XX_VMR_RESERVEDQTY */
    public static final String Table_Name="XX_VMR_RESERVEDQTY";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Descripción.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Descripción.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Nombre.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
        set_Value ("Name", Name);
        
    }
    
    /** Get Nombre.
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
    
    /** Set PriceConsecutive.
    @param XX_PriceConsecutive PriceConsecutive */
    public void setXX_PriceConsecutive (int XX_PriceConsecutive)
    {
        set_Value ("XX_PriceConsecutive", Integer.valueOf(XX_PriceConsecutive));
        
    }
    
    /** Get PriceConsecutive.
    @return PriceConsecutive */
    public int getXX_PriceConsecutive() 
    {
        return get_ValueAsInt("XX_PriceConsecutive");
        
    }
    
    /** Set Product Code.
    @param XX_ProductCode Product Code */
    public void setXX_ProductCode (String XX_ProductCode)
    {
        set_Value ("XX_ProductCode", XX_ProductCode);
        
    }
    
    /** Get Product Code.
    @return Product Code */
    public String getXX_ProductCode() 
    {
        return (String)get_Value("XX_ProductCode");
        
    }
    
    /** Set Reserved Quantity.
    @param XX_ReservedQty Reserved Quantity */
    public void setXX_ReservedQty (java.math.BigDecimal XX_ReservedQty)
    {
        set_Value ("XX_ReservedQty", XX_ReservedQty);
        
    }
    
    /** Get Reserved Quantity.
    @return Reserved Quantity */
    public java.math.BigDecimal getXX_ReservedQty() 
    {
        return get_ValueAsBigDecimal("XX_ReservedQty");
        
    }
    
    /** Set XX_VMR_RESERVEDQTY_ID.
    @param XX_VMR_RESERVEDQTY_ID XX_VMR_RESERVEDQTY_ID */
    public void setXX_VMR_RESERVEDQTY_ID (int XX_VMR_RESERVEDQTY_ID)
    {
        if (XX_VMR_RESERVEDQTY_ID < 1) throw new IllegalArgumentException ("XX_VMR_RESERVEDQTY_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_RESERVEDQTY_ID", Integer.valueOf(XX_VMR_RESERVEDQTY_ID));
        
    }
    
    /** Get XX_VMR_RESERVEDQTY_ID.
    @return XX_VMR_RESERVEDQTY_ID */
    public int getXX_VMR_RESERVEDQTY_ID() 
    {
        return get_ValueAsInt("XX_VMR_RESERVEDQTY_ID");
        
    }
    
    /** Set Store Number.
    @param XX_WarehouseBecoNumber Store Number */
    public void setXX_WarehouseBecoNumber (int XX_WarehouseBecoNumber)
    {
        set_Value ("XX_WarehouseBecoNumber", Integer.valueOf(XX_WarehouseBecoNumber));
        
    }
    
    /** Get Store Number.
    @return Store Number */
    public int getXX_WarehouseBecoNumber() 
    {
        return get_ValueAsInt("XX_WarehouseBecoNumber");
        
    }
    
    
}
