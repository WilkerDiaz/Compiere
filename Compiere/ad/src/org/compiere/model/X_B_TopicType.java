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
/** Generated Model for B_TopicType
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_B_TopicType.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_B_TopicType extends PO
{
    /** Standard Constructor
    @param ctx context
    @param B_TopicType_ID id
    @param trx transaction
    */
    public X_B_TopicType (Ctx ctx, int B_TopicType_ID, Trx trx)
    {
        super (ctx, B_TopicType_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (B_TopicType_ID == 0)
        {
            setAuctionType (null);
            setB_TopicType_ID (0);
            setM_PriceList_ID (0);
            setM_ProductMember_ID (0);
            setM_Product_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_B_TopicType (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=690 */
    public static final int Table_ID=690;
    
    /** TableName=B_TopicType */
    public static final String Table_Name="B_TopicType";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Auction Type.
    @param AuctionType Auction Type */
    public void setAuctionType (String AuctionType)
    {
        if (AuctionType == null) throw new IllegalArgumentException ("AuctionType is mandatory.");
        set_Value ("AuctionType", AuctionType);
        
    }
    
    /** Get Auction Type.
    @return Auction Type */
    public String getAuctionType() 
    {
        return (String)get_Value("AuctionType");
        
    }
    
    /** Set Topic Type.
    @param B_TopicType_ID Auction Topic Type */
    public void setB_TopicType_ID (int B_TopicType_ID)
    {
        if (B_TopicType_ID < 1) throw new IllegalArgumentException ("B_TopicType_ID is mandatory.");
        set_ValueNoCheck ("B_TopicType_ID", Integer.valueOf(B_TopicType_ID));
        
    }
    
    /** Get Topic Type.
    @return Auction Topic Type */
    public int getB_TopicType_ID() 
    {
        return get_ValueAsInt("B_TopicType_ID");
        
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
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
    }
    
    /** Set Price List.
    @param M_PriceList_ID Unique identifier of a Price List */
    public void setM_PriceList_ID (int M_PriceList_ID)
    {
        if (M_PriceList_ID < 1) throw new IllegalArgumentException ("M_PriceList_ID is mandatory.");
        set_Value ("M_PriceList_ID", Integer.valueOf(M_PriceList_ID));
        
    }
    
    /** Get Price List.
    @return Unique identifier of a Price List */
    public int getM_PriceList_ID() 
    {
        return get_ValueAsInt("M_PriceList_ID");
        
    }
    
    /** Set Membership.
    @param M_ProductMember_ID Product used to determine the price of the membership for the topic type */
    public void setM_ProductMember_ID (int M_ProductMember_ID)
    {
        if (M_ProductMember_ID < 1) throw new IllegalArgumentException ("M_ProductMember_ID is mandatory.");
        set_Value ("M_ProductMember_ID", Integer.valueOf(M_ProductMember_ID));
        
    }
    
    /** Get Membership.
    @return Product used to determine the price of the membership for the topic type */
    public int getM_ProductMember_ID() 
    {
        return get_ValueAsInt("M_ProductMember_ID");
        
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
    
    
}
