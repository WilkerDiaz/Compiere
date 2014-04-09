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
/** Generated Model for M_ReturnPolicyLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_ReturnPolicyLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_ReturnPolicyLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_ReturnPolicyLine_ID id
    @param trx transaction
    */
    public X_M_ReturnPolicyLine (Ctx ctx, int M_ReturnPolicyLine_ID, Trx trx)
    {
        super (ctx, M_ReturnPolicyLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_ReturnPolicyLine_ID == 0)
        {
            setM_ReturnPolicyLine_ID (0);
            setM_ReturnPolicy_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_ReturnPolicyLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=986 */
    public static final int Table_ID=986;
    
    /** TableName=M_ReturnPolicyLine */
    public static final String Table_Name="M_ReturnPolicyLine";
    
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
    
    /** Set Product Category.
    @param M_Product_Category_ID Category of a Product */
    public void setM_Product_Category_ID (int M_Product_Category_ID)
    {
        if (M_Product_Category_ID <= 0) set_Value ("M_Product_Category_ID", null);
        else
        set_Value ("M_Product_Category_ID", Integer.valueOf(M_Product_Category_ID));
        
    }
    
    /** Get Product Category.
    @return Category of a Product */
    public int getM_Product_Category_ID() 
    {
        return get_ValueAsInt("M_Product_Category_ID");
        
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
    
    /** Set Return Policy Line.
    @param M_ReturnPolicyLine_ID Exceptions to the return policy timeframe for specific products or product categories can be specified at the line level. */
    public void setM_ReturnPolicyLine_ID (int M_ReturnPolicyLine_ID)
    {
        if (M_ReturnPolicyLine_ID < 1) throw new IllegalArgumentException ("M_ReturnPolicyLine_ID is mandatory.");
        set_ValueNoCheck ("M_ReturnPolicyLine_ID", Integer.valueOf(M_ReturnPolicyLine_ID));
        
    }
    
    /** Get Return Policy Line.
    @return Exceptions to the return policy timeframe for specific products or product categories can be specified at the line level. */
    public int getM_ReturnPolicyLine_ID() 
    {
        return get_ValueAsInt("M_ReturnPolicyLine_ID");
        
    }
    
    /** Set Return Policy.
    @param M_ReturnPolicy_ID The Return Policy dictates the timeframe within which goods can be returned. */
    public void setM_ReturnPolicy_ID (int M_ReturnPolicy_ID)
    {
        if (M_ReturnPolicy_ID < 1) throw new IllegalArgumentException ("M_ReturnPolicy_ID is mandatory.");
        set_ValueNoCheck ("M_ReturnPolicy_ID", Integer.valueOf(M_ReturnPolicy_ID));
        
    }
    
    /** Get Return Policy.
    @return The Return Policy dictates the timeframe within which goods can be returned. */
    public int getM_ReturnPolicy_ID() 
    {
        return get_ValueAsInt("M_ReturnPolicy_ID");
        
    }
    
    /** Set TimeFrame (in days).
    @param TimeFrame The timeframe dictates the number of days after shipment that the goods can be returned. */
    public void setTimeFrame (int TimeFrame)
    {
        set_Value ("TimeFrame", Integer.valueOf(TimeFrame));
        
    }
    
    /** Get TimeFrame (in days).
    @return The timeframe dictates the number of days after shipment that the goods can be returned. */
    public int getTimeFrame() 
    {
        return get_ValueAsInt("TimeFrame");
        
    }
    
    
}
