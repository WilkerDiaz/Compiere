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
/** Generated Model for XX_VME_PriceRule
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VME_PriceRule extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VME_PriceRule_ID id
    @param trx transaction
    */
    public X_XX_VME_PriceRule (Ctx ctx, int XX_VME_PriceRule_ID, Trx trx)
    {
        super (ctx, XX_VME_PriceRule_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VME_PriceRule_ID == 0)
        {
            setValue (null);
            setXX_INCREASE (0);	// 1
            setXX_TERMINATION (Env.ZERO);
            setXX_VME_PRICERULE_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VME_PriceRule (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27536638508789L;
    /** Last Updated Timestamp 2009-10-02 15:23:12.0 */
    public static final long updatedMS = 1254513192000L;
    /** AD_Table_ID=1000073 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VME_PriceRule");
        
    }
    ;
    
    /** TableName=XX_VME_PriceRule */
    public static final String Table_Name="XX_VME_PriceRule";
    
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
    
    /** Set High Rank.
    @param XX_HIGHRANK Rango de Precio Superior */
    public void setXX_HIGHRANK (int XX_HIGHRANK)
    {
        set_Value ("XX_HIGHRANK", Integer.valueOf(XX_HIGHRANK));
        
    }
    
    /** Get High Rank.
    @return Rango de Precio Superior */
    public int getXX_HIGHRANK() 
    {
        return get_ValueAsInt("XX_HIGHRANK");
        
    }
    
    /** Set Increase.
    @param XX_INCREASE Incremento */
    public void setXX_INCREASE (int XX_INCREASE)
    {
        set_Value ("XX_INCREASE", Integer.valueOf(XX_INCREASE));
        
    }
    
    /** Get Increase.
    @return Incremento */
    public int getXX_INCREASE() 
    {
        return get_ValueAsInt("XX_INCREASE");
        
    }
    
    /** Set Infinite.
    @param XX_InfiniteValue Precio Muy Alto */
    public void setXX_InfiniteValue (boolean XX_InfiniteValue)
    {
        set_Value ("XX_InfiniteValue", Boolean.valueOf(XX_InfiniteValue));
        
    }
    
    /** Get Infinite.
    @return Precio Muy Alto */
    public boolean isXX_InfiniteValue() 
    {
        return get_ValueAsBoolean("XX_InfiniteValue");
        
    }
    
    /** Set Low Rank.
    @param XX_LOWRANK Rango de Precio Inferior */
    public void setXX_LOWRANK (int XX_LOWRANK)
    {
        set_ValueNoCheck ("XX_LOWRANK", Integer.valueOf(XX_LOWRANK));
        
    }
    
    /** Get Low Rank.
    @return Rango de Precio Inferior */
    public int getXX_LOWRANK() 
    {
        return get_ValueAsInt("XX_LOWRANK");
        
    }
    
    /** Set Termination.
    @param XX_TERMINATION Terminacion */
    public void setXX_TERMINATION (java.math.BigDecimal XX_TERMINATION)
    {
        if (XX_TERMINATION == null) throw new IllegalArgumentException ("XX_TERMINATION is mandatory.");
        set_Value ("XX_TERMINATION", XX_TERMINATION);
        
    }
    
    /** Get Termination.
    @return Terminacion */
    public java.math.BigDecimal getXX_TERMINATION() 
    {
        return get_ValueAsBigDecimal("XX_TERMINATION");
        
    }
    
    /** Set XX_VME_PRICERULE_ID.
    @param XX_VME_PRICERULE_ID XX_VME_PRICERULE_ID */
    public void setXX_VME_PRICERULE_ID (int XX_VME_PRICERULE_ID)
    {
        if (XX_VME_PRICERULE_ID < 1) throw new IllegalArgumentException ("XX_VME_PRICERULE_ID is mandatory.");
        set_ValueNoCheck ("XX_VME_PRICERULE_ID", Integer.valueOf(XX_VME_PRICERULE_ID));
        
    }
    
    /** Get XX_VME_PRICERULE_ID.
    @return XX_VME_PRICERULE_ID */
    public int getXX_VME_PRICERULE_ID() 
    {
        return get_ValueAsInt("XX_VME_PRICERULE_ID");
        
    }
    
    
}
