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
/** Generated Model for C_Bank
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Bank.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Bank extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Bank_ID id
    @param trx transaction
    */
    public X_C_Bank (Ctx ctx, int C_Bank_ID, Trx trx)
    {
        super (ctx, C_Bank_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Bank_ID == 0)
        {
            setC_Bank_ID (0);
            setIsOwnBank (true);	// Y
            setName (null);
            setRoutingNo (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Bank (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=296 */
    public static final int Table_ID=296;
    
    /** TableName=C_Bank */
    public static final String Table_Name="C_Bank";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Bank Verification Class.
    @param BankVerificationClass Bank Data Verification Class */
    public void setBankVerificationClass (String BankVerificationClass)
    {
        set_Value ("BankVerificationClass", BankVerificationClass);
        
    }
    
    /** Get Bank Verification Class.
    @return Bank Data Verification Class */
    public String getBankVerificationClass() 
    {
        return (String)get_Value("BankVerificationClass");
        
    }
    
    /** Set Bank.
    @param C_Bank_ID Bank */
    public void setC_Bank_ID (int C_Bank_ID)
    {
        if (C_Bank_ID < 1) throw new IllegalArgumentException ("C_Bank_ID is mandatory.");
        set_ValueNoCheck ("C_Bank_ID", Integer.valueOf(C_Bank_ID));
        
    }
    
    /** Get Bank.
    @return Bank */
    public int getC_Bank_ID() 
    {
        return get_ValueAsInt("C_Bank_ID");
        
    }
    
    /** Set Address.
    @param C_Location_ID Location or Address */
    public void setC_Location_ID (int C_Location_ID)
    {
        if (C_Location_ID <= 0) set_Value ("C_Location_ID", null);
        else
        set_Value ("C_Location_ID", Integer.valueOf(C_Location_ID));
        
    }
    
    /** Get Address.
    @return Location or Address */
    public int getC_Location_ID() 
    {
        return get_ValueAsInt("C_Location_ID");
        
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
    
    /** Set Own Bank.
    @param IsOwnBank Bank for this Organization */
    public void setIsOwnBank (boolean IsOwnBank)
    {
        set_Value ("IsOwnBank", Boolean.valueOf(IsOwnBank));
        
    }
    
    /** Get Own Bank.
    @return Bank for this Organization */
    public boolean isOwnBank() 
    {
        return get_ValueAsBoolean("IsOwnBank");
        
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
    
    /** Set Routing No.
    @param RoutingNo Bank Routing Number */
    public void setRoutingNo (String RoutingNo)
    {
        if (RoutingNo == null) throw new IllegalArgumentException ("RoutingNo is mandatory.");
        set_Value ("RoutingNo", RoutingNo);
        
    }
    
    /** Get Routing No.
    @return Bank Routing Number */
    public String getRoutingNo() 
    {
        return (String)get_Value("RoutingNo");
        
    }
    
    /** Set Swift code.
    @param SwiftCode Swift Code or BIC */
    public void setSwiftCode (String SwiftCode)
    {
        set_Value ("SwiftCode", SwiftCode);
        
    }
    
    /** Get Swift code.
    @return Swift Code or BIC */
    public String getSwiftCode() 
    {
        return (String)get_Value("SwiftCode");
        
    }
    
    
}
