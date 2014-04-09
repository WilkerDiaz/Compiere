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
/** Generated Model for B_Offer
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_B_Offer.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_B_Offer extends PO
{
    /** Standard Constructor
    @param ctx context
    @param B_Offer_ID id
    @param trx transaction
    */
    public X_B_Offer (Ctx ctx, int B_Offer_ID, Trx trx)
    {
        super (ctx, B_Offer_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (B_Offer_ID == 0)
        {
            setAD_User_ID (0);
            setB_Offer_ID (0);
            setB_SellerFunds_ID (0);
            setB_Topic_ID (0);
            setIsWillingToCommit (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_B_Offer (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=682 */
    public static final int Table_ID=682;
    
    /** TableName=B_Offer */
    public static final String Table_Name="B_Offer";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID < 1) throw new IllegalArgumentException ("AD_User_ID is mandatory.");
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Offer.
    @param B_Offer_ID Offer for a Topic */
    public void setB_Offer_ID (int B_Offer_ID)
    {
        if (B_Offer_ID < 1) throw new IllegalArgumentException ("B_Offer_ID is mandatory.");
        set_ValueNoCheck ("B_Offer_ID", Integer.valueOf(B_Offer_ID));
        
    }
    
    /** Get Offer.
    @return Offer for a Topic */
    public int getB_Offer_ID() 
    {
        return get_ValueAsInt("B_Offer_ID");
        
    }
    
    /** Set Seller Funds.
    @param B_SellerFunds_ID Seller Funds from Offers on Topics */
    public void setB_SellerFunds_ID (int B_SellerFunds_ID)
    {
        if (B_SellerFunds_ID < 1) throw new IllegalArgumentException ("B_SellerFunds_ID is mandatory.");
        set_Value ("B_SellerFunds_ID", Integer.valueOf(B_SellerFunds_ID));
        
    }
    
    /** Get Seller Funds.
    @return Seller Funds from Offers on Topics */
    public int getB_SellerFunds_ID() 
    {
        return get_ValueAsInt("B_SellerFunds_ID");
        
    }
    
    /** Set Topic.
    @param B_Topic_ID Auction Topic */
    public void setB_Topic_ID (int B_Topic_ID)
    {
        if (B_Topic_ID < 1) throw new IllegalArgumentException ("B_Topic_ID is mandatory.");
        set_Value ("B_Topic_ID", Integer.valueOf(B_Topic_ID));
        
    }
    
    /** Get Topic.
    @return Auction Topic */
    public int getB_Topic_ID() 
    {
        return get_ValueAsInt("B_Topic_ID");
        
    }
    
    /** Set Willing to commit.
    @param IsWillingToCommit Willing to commit */
    public void setIsWillingToCommit (boolean IsWillingToCommit)
    {
        set_Value ("IsWillingToCommit", Boolean.valueOf(IsWillingToCommit));
        
    }
    
    /** Get Willing to commit.
    @return Willing to commit */
    public boolean isWillingToCommit() 
    {
        return get_ValueAsBoolean("IsWillingToCommit");
        
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
    
    /** Set Private Note.
    @param PrivateNote Private Note - not visible to the other parties */
    public void setPrivateNote (String PrivateNote)
    {
        set_Value ("PrivateNote", PrivateNote);
        
    }
    
    /** Get Private Note.
    @return Private Note - not visible to the other parties */
    public String getPrivateNote() 
    {
        return (String)get_Value("PrivateNote");
        
    }
    
    /** Set Text Message.
    @param TextMsg Text Message */
    public void setTextMsg (String TextMsg)
    {
        set_Value ("TextMsg", TextMsg);
        
    }
    
    /** Get Text Message.
    @return Text Message */
    public String getTextMsg() 
    {
        return (String)get_Value("TextMsg");
        
    }
    
    
}
