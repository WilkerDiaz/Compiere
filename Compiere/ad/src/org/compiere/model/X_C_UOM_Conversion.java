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
/** Generated Model for C_UOM_Conversion
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_UOM_Conversion.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_UOM_Conversion extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_UOM_Conversion_ID id
    @param trx transaction
    */
    public X_C_UOM_Conversion (Ctx ctx, int C_UOM_Conversion_ID, Trx trx)
    {
        super (ctx, C_UOM_Conversion_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_UOM_Conversion_ID == 0)
        {
            setC_UOM_Conversion_ID (0);
            setC_UOM_ID (0);
            setC_UOM_To_ID (0);
            setDivideRate (Env.ZERO);
            setMultiplyRate (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_UOM_Conversion (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=175 */
    public static final int Table_ID=175;
    
    /** TableName=C_UOM_Conversion */
    public static final String Table_Name="C_UOM_Conversion";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set UOM Conversion.
    @param C_UOM_Conversion_ID Unit of Measure Conversion */
    public void setC_UOM_Conversion_ID (int C_UOM_Conversion_ID)
    {
        if (C_UOM_Conversion_ID < 1) throw new IllegalArgumentException ("C_UOM_Conversion_ID is mandatory.");
        set_ValueNoCheck ("C_UOM_Conversion_ID", Integer.valueOf(C_UOM_Conversion_ID));
        
    }
    
    /** Get UOM Conversion.
    @return Unit of Measure Conversion */
    public int getC_UOM_Conversion_ID() 
    {
        return get_ValueAsInt("C_UOM_Conversion_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_UOM_Conversion_ID()));
        
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
    
    /** Set UoM To.
    @param C_UOM_To_ID Target or destination Unit of Measure */
    public void setC_UOM_To_ID (int C_UOM_To_ID)
    {
        if (C_UOM_To_ID < 1) throw new IllegalArgumentException ("C_UOM_To_ID is mandatory.");
        set_Value ("C_UOM_To_ID", Integer.valueOf(C_UOM_To_ID));
        
    }
    
    /** Get UoM To.
    @return Target or destination Unit of Measure */
    public int getC_UOM_To_ID() 
    {
        return get_ValueAsInt("C_UOM_To_ID");
        
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
