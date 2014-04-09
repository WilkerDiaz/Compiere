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
/** Generated Model for C_Subscription
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Subscription.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Subscription extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Subscription_ID id
    @param trx transaction
    */
    public X_C_Subscription (Ctx ctx, int C_Subscription_ID, Trx trx)
    {
        super (ctx, C_Subscription_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Subscription_ID == 0)
        {
            setC_BPartner_ID (0);
            setC_SubscriptionType_ID (0);
            setC_Subscription_ID (0);
            setIsDue (false);
            setM_Product_ID (0);
            setName (null);
            setPaidUntilDate (new Timestamp(System.currentTimeMillis()));
            setRenewalDate (new Timestamp(System.currentTimeMillis()));
            setStartDate (new Timestamp(System.currentTimeMillis()));
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Subscription (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=669 */
    public static final int Table_ID=669;
    
    /** TableName=C_Subscription */
    public static final String Table_Name="C_Subscription";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID < 1) throw new IllegalArgumentException ("C_BPartner_ID is mandatory.");
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Subscription Type.
    @param C_SubscriptionType_ID Type of subscription */
    public void setC_SubscriptionType_ID (int C_SubscriptionType_ID)
    {
        if (C_SubscriptionType_ID < 1) throw new IllegalArgumentException ("C_SubscriptionType_ID is mandatory.");
        set_Value ("C_SubscriptionType_ID", Integer.valueOf(C_SubscriptionType_ID));
        
    }
    
    /** Get Subscription Type.
    @return Type of subscription */
    public int getC_SubscriptionType_ID() 
    {
        return get_ValueAsInt("C_SubscriptionType_ID");
        
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
    
    /** Set Due.
    @param IsDue Subscription Renewal is Due */
    public void setIsDue (boolean IsDue)
    {
        set_Value ("IsDue", Boolean.valueOf(IsDue));
        
    }
    
    /** Get Due.
    @return Subscription Renewal is Due */
    public boolean isDue() 
    {
        return get_ValueAsBoolean("IsDue");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
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
    
    /** Set Paid Until.
    @param PaidUntilDate Subscription is paid/valid until this date */
    public void setPaidUntilDate (Timestamp PaidUntilDate)
    {
        if (PaidUntilDate == null) throw new IllegalArgumentException ("PaidUntilDate is mandatory.");
        set_Value ("PaidUntilDate", PaidUntilDate);
        
    }
    
    /** Get Paid Until.
    @return Subscription is paid/valid until this date */
    public Timestamp getPaidUntilDate() 
    {
        return (Timestamp)get_Value("PaidUntilDate");
        
    }
    
    /** Set Renewal Date.
    @param RenewalDate Renewal Date */
    public void setRenewalDate (Timestamp RenewalDate)
    {
        if (RenewalDate == null) throw new IllegalArgumentException ("RenewalDate is mandatory.");
        set_Value ("RenewalDate", RenewalDate);
        
    }
    
    /** Get Renewal Date.
    @return Renewal Date */
    public Timestamp getRenewalDate() 
    {
        return (Timestamp)get_Value("RenewalDate");
        
    }
    
    /** Set Start Date.
    @param StartDate First effective day (inclusive) */
    public void setStartDate (Timestamp StartDate)
    {
        if (StartDate == null) throw new IllegalArgumentException ("StartDate is mandatory.");
        set_Value ("StartDate", StartDate);
        
    }
    
    /** Get Start Date.
    @return First effective day (inclusive) */
    public Timestamp getStartDate() 
    {
        return (Timestamp)get_Value("StartDate");
        
    }
    
    
}
