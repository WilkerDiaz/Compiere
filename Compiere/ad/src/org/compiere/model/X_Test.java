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
/** Generated Model for Test
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_Test.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_Test extends PO
{
    /** Standard Constructor
    @param ctx context
    @param Test_ID id
    @param trx transaction
    */
    public X_Test (Ctx ctx, int Test_ID, Trx trx)
    {
        super (ctx, Test_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (Test_ID == 0)
        {
            setName (null);
            setTest_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_Test (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=135 */
    public static final int Table_ID=135;
    
    /** TableName=Test */
    public static final String Table_Name="Test";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Account_Acct.
    @param Account_Acct Account_Acct */
    public void setAccount_Acct (int Account_Acct)
    {
        set_Value ("Account_Acct", Integer.valueOf(Account_Acct));
        
    }
    
    /** Get Account_Acct.
    @return Account_Acct */
    public int getAccount_Acct() 
    {
        return get_ValueAsInt("Account_Acct");
        
    }
    
    /** Set BinaryData.
    @param BinaryData Binary Data */
    public void setBinaryData (byte[] BinaryData)
    {
        set_Value ("BinaryData", BinaryData);
        
    }
    
    /** Get BinaryData.
    @return Binary Data */
    public byte[] getBinaryData() 
    {
        return (byte[])get_Value("BinaryData");
        
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
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID <= 0) set_Value ("C_Currency_ID", null);
        else
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
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
    
    /** Set Payment.
    @param C_Payment_ID Payment identifier */
    public void setC_Payment_ID (int C_Payment_ID)
    {
        if (C_Payment_ID <= 0) set_Value ("C_Payment_ID", null);
        else
        set_Value ("C_Payment_ID", Integer.valueOf(C_Payment_ID));
        
    }
    
    /** Get Payment.
    @return Payment identifier */
    public int getC_Payment_ID() 
    {
        return get_ValueAsInt("C_Payment_ID");
        
    }
    
    /** Set UOM.
    @param C_UOM_ID Unit of Measure */
    public void setC_UOM_ID (int C_UOM_ID)
    {
        if (C_UOM_ID <= 0) set_Value ("C_UOM_ID", null);
        else
        set_Value ("C_UOM_ID", Integer.valueOf(C_UOM_ID));
        
    }
    
    /** Get UOM.
    @return Unit of Measure */
    public int getC_UOM_ID() 
    {
        return get_ValueAsInt("C_UOM_ID");
        
    }
    
    /** Set Character Data.
    @param CharacterData Long Character Field */
    public void setCharacterData (String CharacterData)
    {
        set_Value ("CharacterData", CharacterData);
        
    }
    
    /** Get Character Data.
    @return Long Character Field */
    public String getCharacterData() 
    {
        return (String)get_Value("CharacterData");
        
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
    
    /** Set Locator.
    @param M_Locator_ID Warehouse Locator */
    public void setM_Locator_ID (int M_Locator_ID)
    {
        if (M_Locator_ID <= 0) set_Value ("M_Locator_ID", null);
        else
        set_Value ("M_Locator_ID", Integer.valueOf(M_Locator_ID));
        
    }
    
    /** Get Locator.
    @return Warehouse Locator */
    public int getM_Locator_ID() 
    {
        return get_ValueAsInt("M_Locator_ID");
        
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
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    /** Set Amount.
    @param T_Amount Amount */
    public void setT_Amount (java.math.BigDecimal T_Amount)
    {
        set_Value ("T_Amount", T_Amount);
        
    }
    
    /** Get Amount.
    @return Amount */
    public java.math.BigDecimal getT_Amount() 
    {
        return get_ValueAsBigDecimal("T_Amount");
        
    }
    
    /** Set Date.
    @param T_Date Date */
    public void setT_Date (Timestamp T_Date)
    {
        set_Value ("T_Date", T_Date);
        
    }
    
    /** Get Date.
    @return Date */
    public Timestamp getT_Date() 
    {
        return (Timestamp)get_Value("T_Date");
        
    }
    
    /** Set Date Time.
    @param T_DateTime Date and Time */
    public void setT_DateTime (Timestamp T_DateTime)
    {
        set_Value ("T_DateTime", T_DateTime);
        
    }
    
    /** Get Date Time.
    @return Date and Time */
    public Timestamp getT_DateTime() 
    {
        return (Timestamp)get_Value("T_DateTime");
        
    }
    
    /** Set Integer.
    @param T_Integer Integer */
    public void setT_Integer (int T_Integer)
    {
        set_Value ("T_Integer", Integer.valueOf(T_Integer));
        
    }
    
    /** Get Integer.
    @return Integer */
    public int getT_Integer() 
    {
        return get_ValueAsInt("T_Integer");
        
    }
    
    /** Set Number.
    @param T_Number Number */
    public void setT_Number (java.math.BigDecimal T_Number)
    {
        set_Value ("T_Number", T_Number);
        
    }
    
    /** Get Number.
    @return Number */
    public java.math.BigDecimal getT_Number() 
    {
        return get_ValueAsBigDecimal("T_Number");
        
    }
    
    /** Set Qty.
    @param T_Qty Qty */
    public void setT_Qty (java.math.BigDecimal T_Qty)
    {
        set_Value ("T_Qty", T_Qty);
        
    }
    
    /** Get Qty.
    @return Qty */
    public java.math.BigDecimal getT_Qty() 
    {
        return get_ValueAsBigDecimal("T_Qty");
        
    }
    
    /** Set Test ID.
    @param Test_ID Test ID */
    public void setTest_ID (int Test_ID)
    {
        if (Test_ID < 1) throw new IllegalArgumentException ("Test_ID is mandatory.");
        set_ValueNoCheck ("Test_ID", Integer.valueOf(Test_ID));
        
    }
    
    /** Get Test ID.
    @return Test ID */
    public int getTest_ID() 
    {
        return get_ValueAsInt("Test_ID");
        
    }
    
    
}
