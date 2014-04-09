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
/** Generated Model for B_BuyerFunds
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_B_BuyerFunds.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_B_BuyerFunds extends PO
{
    /** Standard Constructor
    @param ctx context
    @param B_BuyerFunds_ID id
    @param trx transaction
    */
    public X_B_BuyerFunds (Ctx ctx, int B_BuyerFunds_ID, Trx trx)
    {
        super (ctx, B_BuyerFunds_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (B_BuyerFunds_ID == 0)
        {
            setAD_User_ID (0);
            setB_BuyerFunds_ID (0);
            setCommittedAmt (Env.ZERO);
            setNonCommittedAmt (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_B_BuyerFunds (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=683 */
    public static final int Table_ID=683;
    
    /** TableName=B_BuyerFunds */
    public static final String Table_Name="B_BuyerFunds";
    
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_User_ID()));
        
    }
    
    /** Set Buyer Funds.
    @param B_BuyerFunds_ID Buyer Funds for Bids on Topics */
    public void setB_BuyerFunds_ID (int B_BuyerFunds_ID)
    {
        if (B_BuyerFunds_ID < 1) throw new IllegalArgumentException ("B_BuyerFunds_ID is mandatory.");
        set_ValueNoCheck ("B_BuyerFunds_ID", Integer.valueOf(B_BuyerFunds_ID));
        
    }
    
    /** Get Buyer Funds.
    @return Buyer Funds for Bids on Topics */
    public int getB_BuyerFunds_ID() 
    {
        return get_ValueAsInt("B_BuyerFunds_ID");
        
    }
    
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID <= 0) set_ValueNoCheck ("C_Order_ID", null);
        else
        set_ValueNoCheck ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** Set Payment.
    @param C_Payment_ID Payment identifier */
    public void setC_Payment_ID (int C_Payment_ID)
    {
        if (C_Payment_ID <= 0) set_ValueNoCheck ("C_Payment_ID", null);
        else
        set_ValueNoCheck ("C_Payment_ID", Integer.valueOf(C_Payment_ID));
        
    }
    
    /** Get Payment.
    @return Payment identifier */
    public int getC_Payment_ID() 
    {
        return get_ValueAsInt("C_Payment_ID");
        
    }
    
    /** Set Committed Amount.
    @param CommittedAmt The (legal) commitment amount */
    public void setCommittedAmt (java.math.BigDecimal CommittedAmt)
    {
        if (CommittedAmt == null) throw new IllegalArgumentException ("CommittedAmt is mandatory.");
        set_Value ("CommittedAmt", CommittedAmt);
        
    }
    
    /** Get Committed Amount.
    @return The (legal) commitment amount */
    public java.math.BigDecimal getCommittedAmt() 
    {
        return get_ValueAsBigDecimal("CommittedAmt");
        
    }
    
    /** Set Not Committed Amount.
    @param NonCommittedAmt Amount not committed yet */
    public void setNonCommittedAmt (java.math.BigDecimal NonCommittedAmt)
    {
        if (NonCommittedAmt == null) throw new IllegalArgumentException ("NonCommittedAmt is mandatory.");
        set_Value ("NonCommittedAmt", NonCommittedAmt);
        
    }
    
    /** Get Not Committed Amount.
    @return Amount not committed yet */
    public java.math.BigDecimal getNonCommittedAmt() 
    {
        return get_ValueAsBigDecimal("NonCommittedAmt");
        
    }
    
    
}
