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
/** Generated Model for C_Subscription_Delivery
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Subscription_Delivery.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Subscription_Delivery extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Subscription_Delivery_ID id
    @param trx transaction
    */
    public X_C_Subscription_Delivery (Ctx ctx, int C_Subscription_Delivery_ID, Trx trx)
    {
        super (ctx, C_Subscription_Delivery_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Subscription_Delivery_ID == 0)
        {
            setC_Subscription_Delivery_ID (0);
            setC_Subscription_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Subscription_Delivery (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=667 */
    public static final int Table_ID=667;
    
    /** TableName=C_Subscription_Delivery */
    public static final String Table_Name="C_Subscription_Delivery";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Subscription Delivery.
    @param C_Subscription_Delivery_ID Optional Delivery Record for a Subscription */
    public void setC_Subscription_Delivery_ID (int C_Subscription_Delivery_ID)
    {
        if (C_Subscription_Delivery_ID < 1) throw new IllegalArgumentException ("C_Subscription_Delivery_ID is mandatory.");
        set_ValueNoCheck ("C_Subscription_Delivery_ID", Integer.valueOf(C_Subscription_Delivery_ID));
        
    }
    
    /** Get Subscription Delivery.
    @return Optional Delivery Record for a Subscription */
    public int getC_Subscription_Delivery_ID() 
    {
        return get_ValueAsInt("C_Subscription_Delivery_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_Subscription_Delivery_ID()));
        
    }
    
    /** Set Subscription.
    @param C_Subscription_ID Subscription of a Business Partner of a Product to renew */
    public void setC_Subscription_ID (int C_Subscription_ID)
    {
        if (C_Subscription_ID < 1) throw new IllegalArgumentException ("C_Subscription_ID is mandatory.");
        set_ValueNoCheck ("C_Subscription_ID", Integer.valueOf(C_Subscription_ID));
        
    }
    
    /** Get Subscription.
    @return Subscription of a Business Partner of a Product to renew */
    public int getC_Subscription_ID() 
    {
        return get_ValueAsInt("C_Subscription_ID");
        
    }
    
    
}
