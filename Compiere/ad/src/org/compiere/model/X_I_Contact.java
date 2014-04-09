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
/** Generated Model for I_Contact
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_I_Contact.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_I_Contact extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_Contact_ID id
    @param trx transaction
    */
    public X_I_Contact (Ctx ctx, int I_Contact_ID, Trx trx)
    {
        super (ctx, I_Contact_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_Contact_ID == 0)
        {
            setI_Contact_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_Contact (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=929 */
    public static final int Table_ID=929;
    
    /** TableName=I_Contact */
    public static final String Table_Name="I_Contact";
    
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
        if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
        else
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Bounced Info.
    @param BouncedInfo Information about the cause of bounce */
    public void setBouncedInfo (String BouncedInfo)
    {
        set_Value ("BouncedInfo", BouncedInfo);
        
    }
    
    /** Get Bounced Info.
    @return Information about the cause of bounce */
    public String getBouncedInfo() 
    {
        return (String)get_Value("BouncedInfo");
        
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
    
    /** Set Lead.
    @param C_Lead_ID Business Lead */
    public void setC_Lead_ID (int C_Lead_ID)
    {
        if (C_Lead_ID <= 0) set_Value ("C_Lead_ID", null);
        else
        set_Value ("C_Lead_ID", Integer.valueOf(C_Lead_ID));
        
    }
    
    /** Get Lead.
    @return Business Lead */
    public int getC_Lead_ID() 
    {
        return get_ValueAsInt("C_Lead_ID");
        
    }
    
    /** Set Contact Description.
    @param ContactDescription Description of Contact */
    public void setContactDescription (String ContactDescription)
    {
        set_Value ("ContactDescription", ContactDescription);
        
    }
    
    /** Get Contact Description.
    @return Description of Contact */
    public String getContactDescription() 
    {
        return (String)get_Value("ContactDescription");
        
    }
    
    /** Set Contact Name.
    @param ContactName Business Partner Contact Name */
    public void setContactName (String ContactName)
    {
        set_Value ("ContactName", ContactName);
        
    }
    
    /** Get Contact Name.
    @return Business Partner Contact Name */
    public String getContactName() 
    {
        return (String)get_Value("ContactName");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getEMail());
        
    }
    
    /** Set Import Contact.
    @param I_Contact_ID Import Contact */
    public void setI_Contact_ID (int I_Contact_ID)
    {
        if (I_Contact_ID < 1) throw new IllegalArgumentException ("I_Contact_ID is mandatory.");
        set_ValueNoCheck ("I_Contact_ID", Integer.valueOf(I_Contact_ID));
        
    }
    
    /** Get Import Contact.
    @return Import Contact */
    public int getI_Contact_ID() 
    {
        return get_ValueAsInt("I_Contact_ID");
        
    }
    
    /** Set Import Error Message.
    @param I_ErrorMsg Messages generated from import process */
    public void setI_ErrorMsg (String I_ErrorMsg)
    {
        set_Value ("I_ErrorMsg", I_ErrorMsg);
        
    }
    
    /** Get Import Error Message.
    @return Messages generated from import process */
    public String getI_ErrorMsg() 
    {
        return (String)get_Value("I_ErrorMsg");
        
    }
    
    /** Set Imported.
    @param I_IsImported Has this import been processed? */
    public void setI_IsImported (boolean I_IsImported)
    {
        set_Value ("I_IsImported", Boolean.valueOf(I_IsImported));
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public boolean isI_IsImported() 
    {
        return get_ValueAsBoolean("I_IsImported");
        
    }
    
    /** Set Interest Area.
    @param InterestAreaName Name of the Interest Area */
    public void setInterestAreaName (String InterestAreaName)
    {
        set_Value ("InterestAreaName", InterestAreaName);
        
    }
    
    /** Get Interest Area.
    @return Name of the Interest Area */
    public String getInterestAreaName() 
    {
        return (String)get_Value("InterestAreaName");
        
    }
    
    /** Set Create Business Partner.
    @param IsCreateBP If selected, also create Business Partner */
    public void setIsCreateBP (boolean IsCreateBP)
    {
        set_Value ("IsCreateBP", Boolean.valueOf(IsCreateBP));
        
    }
    
    /** Get Create Business Partner.
    @return If selected, also create Business Partner */
    public boolean isCreateBP() 
    {
        return get_ValueAsBoolean("IsCreateBP");
        
    }
    
    /** Set Create Lead.
    @param IsCreateLead If selected, also create Lead */
    public void setIsCreateLead (boolean IsCreateLead)
    {
        set_Value ("IsCreateLead", Boolean.valueOf(IsCreateLead));
        
    }
    
    /** Get Create Lead.
    @return If selected, also create Lead */
    public boolean isCreateLead() 
    {
        return get_ValueAsBoolean("IsCreateLead");
        
    }
    
    /** Set EMail Bounced.
    @param IsEMailBounced The email delivery bounced */
    public void setIsEMailBounced (boolean IsEMailBounced)
    {
        set_Value ("IsEMailBounced", Boolean.valueOf(IsEMailBounced));
        
    }
    
    /** Get EMail Bounced.
    @return The email delivery bounced */
    public boolean isEMailBounced() 
    {
        return get_ValueAsBoolean("IsEMailBounced");
        
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
    
    /** Set Interest Area.
    @param R_InterestArea_ID Interest Area or Topic */
    public void setR_InterestArea_ID (int R_InterestArea_ID)
    {
        if (R_InterestArea_ID <= 0) set_Value ("R_InterestArea_ID", null);
        else
        set_Value ("R_InterestArea_ID", Integer.valueOf(R_InterestArea_ID));
        
    }
    
    /** Get Interest Area.
    @return Interest Area or Topic */
    public int getR_InterestArea_ID() 
    {
        return get_ValueAsInt("R_InterestArea_ID");
        
    }
    
    
}
