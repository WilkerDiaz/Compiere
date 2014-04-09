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
/** Generated Model for W_Basket
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_W_Basket.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_W_Basket extends PO
{
    /** Standard Constructor
    @param ctx context
    @param W_Basket_ID id
    @param trx transaction
    */
    public X_W_Basket (Ctx ctx, int W_Basket_ID, Trx trx)
    {
        super (ctx, W_Basket_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (W_Basket_ID == 0)
        {
            setAD_User_ID (0);
            setSession_ID (null);
            setW_Basket_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_W_Basket (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=402 */
    public static final int Table_ID=402;
    
    /** TableName=W_Basket */
    public static final String Table_Name="W_Basket";
    
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
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
        else
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set EMail Address.
    @param EMail Electronic Mail Address */
    public void setEMail (String EMail)
    {
        set_Value ("EMail", EMail);
        
    }
    
    /** Get EMail Address.
    @return Electronic Mail Address */
    public String getEMail() 
    {
        return (String)get_Value("EMail");
        
    }
    
    /** Set Price List.
    @param M_PriceList_ID Unique identifier of a Price List */
    public void setM_PriceList_ID (int M_PriceList_ID)
    {
        if (M_PriceList_ID <= 0) set_Value ("M_PriceList_ID", null);
        else
        set_Value ("M_PriceList_ID", Integer.valueOf(M_PriceList_ID));
        
    }
    
    /** Get Price List.
    @return Unique identifier of a Price List */
    public int getM_PriceList_ID() 
    {
        return get_ValueAsInt("M_PriceList_ID");
        
    }
    
    /** Set Session ID.
    @param Session_ID Session ID */
    public void setSession_ID (String Session_ID)
    {
        if (Session_ID == null) throw new IllegalArgumentException ("Session_ID is mandatory.");
        set_Value ("Session_ID", Session_ID);
        
    }
    
    /** Get Session ID.
    @return Session ID */
    public String getSession_ID() 
    {
        return (String)get_Value("Session_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getSession_ID());
        
    }
    
    /** Set W_Basket_ID.
    @param W_Basket_ID Web Basket */
    public void setW_Basket_ID (int W_Basket_ID)
    {
        if (W_Basket_ID < 1) throw new IllegalArgumentException ("W_Basket_ID is mandatory.");
        set_ValueNoCheck ("W_Basket_ID", Integer.valueOf(W_Basket_ID));
        
    }
    
    /** Get W_Basket_ID.
    @return Web Basket */
    public int getW_Basket_ID() 
    {
        return get_ValueAsInt("W_Basket_ID");
        
    }
    
    
}
