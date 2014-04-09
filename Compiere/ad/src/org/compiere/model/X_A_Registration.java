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
/** Generated Model for A_Registration
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_A_Registration.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_A_Registration extends PO
{
    /** Standard Constructor
    @param ctx context
    @param A_Registration_ID id
    @param trx transaction
    */
    public X_A_Registration (Ctx ctx, int A_Registration_ID, Trx trx)
    {
        super (ctx, A_Registration_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (A_Registration_ID == 0)
        {
            setA_Registration_ID (0);
            setIsAllowPublish (false);
            setIsInProduction (false);
            setIsRegistered (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_A_Registration (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=651 */
    public static final int Table_ID=651;
    
    /** TableName=A_Registration */
    public static final String Table_Name="A_Registration";
    
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
    
    /** Set Asset.
    @param A_Asset_ID Asset used internally or by customers */
    public void setA_Asset_ID (int A_Asset_ID)
    {
        if (A_Asset_ID <= 0) set_Value ("A_Asset_ID", null);
        else
        set_Value ("A_Asset_ID", Integer.valueOf(A_Asset_ID));
        
    }
    
    /** Get Asset.
    @return Asset used internally or by customers */
    public int getA_Asset_ID() 
    {
        return get_ValueAsInt("A_Asset_ID");
        
    }
    
    /** Set Registration.
    @param A_Registration_ID User Asset Registration */
    public void setA_Registration_ID (int A_Registration_ID)
    {
        if (A_Registration_ID < 1) throw new IllegalArgumentException ("A_Registration_ID is mandatory.");
        set_ValueNoCheck ("A_Registration_ID", Integer.valueOf(A_Registration_ID));
        
    }
    
    /** Get Registration.
    @return User Asset Registration */
    public int getA_Registration_ID() 
    {
        return get_ValueAsInt("A_Registration_ID");
        
    }
    
    /** Set In Service Date.
    @param AssetServiceDate Date when Asset was put into service */
    public void setAssetServiceDate (Timestamp AssetServiceDate)
    {
        set_ValueNoCheck ("AssetServiceDate", AssetServiceDate);
        
    }
    
    /** Get In Service Date.
    @return Date when Asset was put into service */
    public Timestamp getAssetServiceDate() 
    {
        return (Timestamp)get_Value("AssetServiceDate");
        
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
    
    /** Set Allowed to be Published.
    @param IsAllowPublish Indicates that publishing on the information is allowed. Otherwise it is used just as statistical summary info */
    public void setIsAllowPublish (boolean IsAllowPublish)
    {
        set_Value ("IsAllowPublish", Boolean.valueOf(IsAllowPublish));
        
    }
    
    /** Get Allowed to be Published.
    @return Indicates that publishing on the information is allowed. Otherwise it is used just as statistical summary info */
    public boolean isAllowPublish() 
    {
        return get_ValueAsBoolean("IsAllowPublish");
        
    }
    
    /** Set In Production.
    @param IsInProduction The system is in production */
    public void setIsInProduction (boolean IsInProduction)
    {
        set_Value ("IsInProduction", Boolean.valueOf(IsInProduction));
        
    }
    
    /** Get In Production.
    @return The system is in production */
    public boolean isInProduction() 
    {
        return get_ValueAsBoolean("IsInProduction");
        
    }
    
    /** Set Registered.
    @param IsRegistered The application is registered. */
    public void setIsRegistered (boolean IsRegistered)
    {
        set_Value ("IsRegistered", Boolean.valueOf(IsRegistered));
        
    }
    
    /** Get Registered.
    @return The application is registered. */
    public boolean isRegistered() 
    {
        return get_ValueAsBoolean("IsRegistered");
        
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
    
    /** Set Note.
    @param Note Optional additional user defined information */
    public void setNote (String Note)
    {
        set_Value ("Note", Note);
        
    }
    
    /** Get Note.
    @return Optional additional user defined information */
    public String getNote() 
    {
        return (String)get_Value("Note");
        
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
    
    /** Set Remote Addr.
    @param Remote_Addr Remote Address */
    public void setRemote_Addr (String Remote_Addr)
    {
        set_Value ("Remote_Addr", Remote_Addr);
        
    }
    
    /** Get Remote Addr.
    @return Remote Address */
    public String getRemote_Addr() 
    {
        return (String)get_Value("Remote_Addr");
        
    }
    
    /** Set Remote Host.
    @param Remote_Host Remote host Info */
    public void setRemote_Host (String Remote_Host)
    {
        set_Value ("Remote_Host", Remote_Host);
        
    }
    
    /** Get Remote Host.
    @return Remote host Info */
    public String getRemote_Host() 
    {
        return (String)get_Value("Remote_Host");
        
    }
    
    
}
