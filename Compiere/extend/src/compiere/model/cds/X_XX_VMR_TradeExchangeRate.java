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
/** Generated Model for XX_VMR_TradeExchangeRate
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_TradeExchangeRate extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_TradeExchangeRate_ID id
    @param trx transaction
    */
    public X_XX_VMR_TradeExchangeRate (Ctx ctx, int XX_VMR_TradeExchangeRate_ID, Trx trx)
    {
        super (ctx, XX_VMR_TradeExchangeRate_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_TradeExchangeRate_ID == 0)
        {
            setXX_Rate (Env.ZERO);
            setXX_VMR_TradeExchangeRate_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_TradeExchangeRate (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27654924837789L;
    /** Last Updated Timestamp 2013-07-02 16:42:01.0 */
    public static final long updatedMS = 1372799521000L;
    /** AD_Table_ID=1004053 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_TradeExchangeRate");
        
    }
    ;
    
    /** TableName=XX_VMR_TradeExchangeRate */
    public static final String Table_Name="XX_VMR_TradeExchangeRate";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set XX_EndDate.
    @param XX_EndDate XX_EndDate */
    public void setXX_EndDate (Timestamp XX_EndDate)
    {
        set_Value ("XX_EndDate", XX_EndDate);
        
    }
    
    /** Get XX_EndDate.
    @return XX_EndDate */
    public Timestamp getXX_EndDate() 
    {
        return (Timestamp)get_Value("XX_EndDate");
        
    }
    
    /** Set XX_InitDate.
    @param XX_InitDate XX_InitDate */
    public void setXX_InitDate (Timestamp XX_InitDate)
    {
        set_Value ("XX_InitDate", XX_InitDate);
        
    }
    
    /** Get XX_InitDate.
    @return XX_InitDate */
    public Timestamp getXX_InitDate() 
    {
        return (Timestamp)get_Value("XX_InitDate");
        
    }
    
    /** Set Rate.
    @param XX_Rate Terifa  */
    public void setXX_Rate (java.math.BigDecimal XX_Rate)
    {
        if (XX_Rate == null) throw new IllegalArgumentException ("XX_Rate is mandatory.");
        set_Value ("XX_Rate", XX_Rate);
        
    }
    
    /** Get Rate.
    @return Terifa  */
    public java.math.BigDecimal getXX_Rate() 
    {
        return get_ValueAsBigDecimal("XX_Rate");
        
    }
    
    /** Set XX_VMR_TradeExchangeRate_ID.
    @param XX_VMR_TradeExchangeRate_ID XX_VMR_TradeExchangeRate_ID */
    public void setXX_VMR_TradeExchangeRate_ID (int XX_VMR_TradeExchangeRate_ID)
    {
        if (XX_VMR_TradeExchangeRate_ID < 1) throw new IllegalArgumentException ("XX_VMR_TradeExchangeRate_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_TradeExchangeRate_ID", Integer.valueOf(XX_VMR_TradeExchangeRate_ID));
        
    }
    
    /** Get XX_VMR_TradeExchangeRate_ID.
    @return XX_VMR_TradeExchangeRate_ID */
    public int getXX_VMR_TradeExchangeRate_ID() 
    {
        return get_ValueAsInt("XX_VMR_TradeExchangeRate_ID");
        
    }
    
    
}
