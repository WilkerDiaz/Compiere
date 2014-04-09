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
package org.compiere.xuom;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for C_UOMGroupConversion
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.2_Dev - $Id$ */
public class X_C_UOMGroupConversion extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_UOMGroupConversion_ID id
    @param trx transaction
    */
    public X_C_UOMGroupConversion (Ctx ctx, int C_UOMGroupConversion_ID, Trx trx)
    {
        super (ctx, C_UOMGroupConversion_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_UOMGroupConversion_ID == 0)
        {
            setC_UOMGroupConversion_ID (0);
            setC_UOMGroup_ID (0);
            setC_UOM_ID (0);
            setDivideRate (Env.ZERO);
            setIsDefaultReport (false);
            setIsPurchased (false);
            setIsSold (false);
            setMultiplyRate (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_UOMGroupConversion (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=1004 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("C_UOMGroupConversion");
        
    }
    ;
    
    /** TableName=C_UOMGroupConversion */
    public static final String Table_Name="C_UOMGroupConversion";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"C_UOMGroupConversion");
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set UOM Group Conversion.
    @param C_UOMGroupConversion_ID Conversion rate from base UOM for Group */
    public void setC_UOMGroupConversion_ID (int C_UOMGroupConversion_ID)
    {
        if (C_UOMGroupConversion_ID < 1) throw new IllegalArgumentException ("C_UOMGroupConversion_ID is mandatory.");
        set_ValueNoCheck ("C_UOMGroupConversion_ID", Integer.valueOf(C_UOMGroupConversion_ID));
        
    }
    
    /** Get UOM Group Conversion.
    @return Conversion rate from base UOM for Group */
    public int getC_UOMGroupConversion_ID() 
    {
        return get_ValueAsInt("C_UOMGroupConversion_ID");
        
    }
    
    /** Set UOM Group.
    @param C_UOMGroup_ID Group for managing sets of Unit of Measure */
    public void setC_UOMGroup_ID (int C_UOMGroup_ID)
    {
        if (C_UOMGroup_ID < 1) throw new IllegalArgumentException ("C_UOMGroup_ID is mandatory.");
        set_ValueNoCheck ("C_UOMGroup_ID", Integer.valueOf(C_UOMGroup_ID));
        
    }
    
    /** Get UOM Group.
    @return Group for managing sets of Unit of Measure */
    public int getC_UOMGroup_ID() 
    {
        return get_ValueAsInt("C_UOMGroup_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_UOMGroup_ID()));
        
    }
    
    /** Set UOM.
    @param C_UOM_ID Unit of Measure */
    public void setC_UOM_ID (int C_UOM_ID)
    {
        if (C_UOM_ID < 1) throw new IllegalArgumentException ("C_UOM_ID is mandatory.");
        set_Value ("C_UOM_ID", Integer.valueOf(C_UOM_ID));
        
    }
    
    /** Get UOM.
    @return Unit of Measure */
    public int getC_UOM_ID() 
    {
        return get_ValueAsInt("C_UOM_ID");
        
    }
    
    /** Set Divide Rate.
    @param DivideRate To convert Source number to Target number, the Source is divided */
    public void setDivideRate (java.math.BigDecimal DivideRate)
    {
        if (DivideRate == null) throw new IllegalArgumentException ("DivideRate is mandatory.");
        set_Value ("DivideRate", DivideRate);
        
    }
    
    /** Get Divide Rate.
    @return To convert Source number to Target number, the Source is divided */
    public java.math.BigDecimal getDivideRate() 
    {
        return get_ValueAsBigDecimal("DivideRate");
        
    }
    
    /** Set Default Report.
    @param IsDefaultReport Default Reporting Unit of Measure */
    public void setIsDefaultReport (boolean IsDefaultReport)
    {
        set_Value ("IsDefaultReport", Boolean.valueOf(IsDefaultReport));
        
    }
    
    /** Get Default Report.
    @return Default Reporting Unit of Measure */
    public boolean isDefaultReport() 
    {
        return get_ValueAsBoolean("IsDefaultReport");
        
    }
    
    /** Set Purchased.
    @param IsPurchased Organization purchases this product */
    public void setIsPurchased (boolean IsPurchased)
    {
        set_Value ("IsPurchased", Boolean.valueOf(IsPurchased));
        
    }
    
    /** Get Purchased.
    @return Organization purchases this product */
    public boolean isPurchased() 
    {
        return get_ValueAsBoolean("IsPurchased");
        
    }
    
    /** Set Sold.
    @param IsSold Organization sells this product */
    public void setIsSold (boolean IsSold)
    {
        set_Value ("IsSold", Boolean.valueOf(IsSold));
        
    }
    
    /** Get Sold.
    @return Organization sells this product */
    public boolean isSold() 
    {
        return get_ValueAsBoolean("IsSold");
        
    }
    
    /** Set Multiply Rate.
    @param MultiplyRate Multiply Rate */
    public void setMultiplyRate (java.math.BigDecimal MultiplyRate)
    {
        if (MultiplyRate == null) throw new IllegalArgumentException ("MultiplyRate is mandatory.");
        set_Value ("MultiplyRate", MultiplyRate);
        
    }
    
    /** Get Multiply Rate.
    @return Multiply Rate */
    public java.math.BigDecimal getMultiplyRate() 
    {
        return get_ValueAsBigDecimal("MultiplyRate");
        
    }
    
    
}
