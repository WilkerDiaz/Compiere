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
/** Generated Model for C_RfQ_TopicSubscriberOnly
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_RfQ_TopicSubscriberOnly.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_RfQ_TopicSubscriberOnly extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_RfQ_TopicSubscriberOnly_ID id
    @param trx transaction
    */
    public X_C_RfQ_TopicSubscriberOnly (Ctx ctx, int C_RfQ_TopicSubscriberOnly_ID, Trx trx)
    {
        super (ctx, C_RfQ_TopicSubscriberOnly_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_RfQ_TopicSubscriberOnly_ID == 0)
        {
            setC_RfQ_TopicSubscriberOnly_ID (0);
            setC_RfQ_TopicSubscriber_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_RfQ_TopicSubscriberOnly (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=747 */
    public static final int Table_ID=747;
    
    /** TableName=C_RfQ_TopicSubscriberOnly */
    public static final String Table_Name="C_RfQ_TopicSubscriberOnly";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set RfQ Topic Subscriber Restriction.
    @param C_RfQ_TopicSubscriberOnly_ID Include Subscriber only for certain products or product categories */
    public void setC_RfQ_TopicSubscriberOnly_ID (int C_RfQ_TopicSubscriberOnly_ID)
    {
        if (C_RfQ_TopicSubscriberOnly_ID < 1) throw new IllegalArgumentException ("C_RfQ_TopicSubscriberOnly_ID is mandatory.");
        set_ValueNoCheck ("C_RfQ_TopicSubscriberOnly_ID", Integer.valueOf(C_RfQ_TopicSubscriberOnly_ID));
        
    }
    
    /** Get RfQ Topic Subscriber Restriction.
    @return Include Subscriber only for certain products or product categories */
    public int getC_RfQ_TopicSubscriberOnly_ID() 
    {
        return get_ValueAsInt("C_RfQ_TopicSubscriberOnly_ID");
        
    }
    
    /** Set RfQ Subscriber.
    @param C_RfQ_TopicSubscriber_ID Request for Quotation Topic Subscriber */
    public void setC_RfQ_TopicSubscriber_ID (int C_RfQ_TopicSubscriber_ID)
    {
        if (C_RfQ_TopicSubscriber_ID < 1) throw new IllegalArgumentException ("C_RfQ_TopicSubscriber_ID is mandatory.");
        set_ValueNoCheck ("C_RfQ_TopicSubscriber_ID", Integer.valueOf(C_RfQ_TopicSubscriber_ID));
        
    }
    
    /** Get RfQ Subscriber.
    @return Request for Quotation Topic Subscriber */
    public int getC_RfQ_TopicSubscriber_ID() 
    {
        return get_ValueAsInt("C_RfQ_TopicSubscriber_ID");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_Product_Category_ID()));
        
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
    
    
}
