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
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for A_Asset_Retirement
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_A_Asset_Retirement.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_A_Asset_Retirement extends PO
{
    /** Standard Constructor
    @param ctx context
    @param A_Asset_Retirement_ID id
    @param trx transaction
    */
    public X_A_Asset_Retirement (Ctx ctx, int A_Asset_Retirement_ID, Trx trx)
    {
        super (ctx, A_Asset_Retirement_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (A_Asset_Retirement_ID == 0)
        {
            setA_Asset_ID (0);
            setA_Asset_Retirement_ID (0);
            setAssetMarketValueAmt (Env.ZERO);
            setAssetValueAmt (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_A_Asset_Retirement (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=540 */
    public static final int Table_ID=540;
    
    /** TableName=A_Asset_Retirement */
    public static final String Table_Name="A_Asset_Retirement";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Asset.
    @param A_Asset_ID Asset used internally or by customers */
    public void setA_Asset_ID (int A_Asset_ID)
    {
        if (A_Asset_ID < 1) throw new IllegalArgumentException ("A_Asset_ID is mandatory.");
        set_ValueNoCheck ("A_Asset_ID", Integer.valueOf(A_Asset_ID));
        
    }
    
    /** Get Asset.
    @return Asset used internally or by customers */
    public int getA_Asset_ID() 
    {
        return get_ValueAsInt("A_Asset_ID");
        
    }
    
    /** Set Asset Retirement.
    @param A_Asset_Retirement_ID Internally used asset is not longer used. */
    public void setA_Asset_Retirement_ID (int A_Asset_Retirement_ID)
    {
        if (A_Asset_Retirement_ID < 1) throw new IllegalArgumentException ("A_Asset_Retirement_ID is mandatory.");
        set_ValueNoCheck ("A_Asset_Retirement_ID", Integer.valueOf(A_Asset_Retirement_ID));
        
    }
    
    /** Get Asset Retirement.
    @return Internally used asset is not longer used. */
    public int getA_Asset_Retirement_ID() 
    {
        return get_ValueAsInt("A_Asset_Retirement_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getA_Asset_Retirement_ID()));
        
    }
    
    /** Set Market value Amount.
    @param AssetMarketValueAmt Market value of the asset */
    public void setAssetMarketValueAmt (java.math.BigDecimal AssetMarketValueAmt)
    {
        if (AssetMarketValueAmt == null) throw new IllegalArgumentException ("AssetMarketValueAmt is mandatory.");
        set_Value ("AssetMarketValueAmt", AssetMarketValueAmt);
        
    }
    
    /** Get Market value Amount.
    @return Market value of the asset */
    public java.math.BigDecimal getAssetMarketValueAmt() 
    {
        return get_ValueAsBigDecimal("AssetMarketValueAmt");
        
    }
    
    /** Set Asset value.
    @param AssetValueAmt Book Value of the asset */
    public void setAssetValueAmt (java.math.BigDecimal AssetValueAmt)
    {
        if (AssetValueAmt == null) throw new IllegalArgumentException ("AssetValueAmt is mandatory.");
        set_Value ("AssetValueAmt", AssetValueAmt);
        
    }
    
    /** Get Asset value.
    @return Book Value of the asset */
    public java.math.BigDecimal getAssetValueAmt() 
    {
        return get_ValueAsBigDecimal("AssetValueAmt");
        
    }
    
    /** Set Invoice Line.
    @param C_InvoiceLine_ID Invoice Detail Line */
    public void setC_InvoiceLine_ID (int C_InvoiceLine_ID)
    {
        if (C_InvoiceLine_ID <= 0) set_Value ("C_InvoiceLine_ID", null);
        else
        set_Value ("C_InvoiceLine_ID", Integer.valueOf(C_InvoiceLine_ID));
        
    }
    
    /** Get Invoice Line.
    @return Invoice Detail Line */
    public int getC_InvoiceLine_ID() 
    {
        return get_ValueAsInt("C_InvoiceLine_ID");
        
    }
    
    
}
