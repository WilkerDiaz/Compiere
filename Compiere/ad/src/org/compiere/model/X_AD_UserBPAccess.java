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
/** Generated Model for AD_UserBPAccess
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_UserBPAccess.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_UserBPAccess extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_UserBPAccess_ID id
    @param trx transaction
    */
    public X_AD_UserBPAccess (Ctx ctx, int AD_UserBPAccess_ID, Trx trx)
    {
        super (ctx, AD_UserBPAccess_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_UserBPAccess_ID == 0)
        {
            setAD_UserBPAccess_ID (0);
            setAD_User_ID (0);
            setBPAccessType (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_UserBPAccess (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=813 */
    public static final int Table_ID=813;
    
    /** TableName=AD_UserBPAccess */
    public static final String Table_Name="AD_UserBPAccess";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set User BP Access.
    @param AD_UserBPAccess_ID User/contact access to Business Partner information and resources */
    public void setAD_UserBPAccess_ID (int AD_UserBPAccess_ID)
    {
        if (AD_UserBPAccess_ID < 1) throw new IllegalArgumentException ("AD_UserBPAccess_ID is mandatory.");
        set_ValueNoCheck ("AD_UserBPAccess_ID", Integer.valueOf(AD_UserBPAccess_ID));
        
    }
    
    /** Get User BP Access.
    @return User/contact access to Business Partner information and resources */
    public int getAD_UserBPAccess_ID() 
    {
        return get_ValueAsInt("AD_UserBPAccess_ID");
        
    }
    
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID < 1) throw new IllegalArgumentException ("AD_User_ID is mandatory.");
        set_ValueNoCheck ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Assets, Download = A */
    public static final String BPACCESSTYPE_AssetsDownload = X_Ref_AD_User_BP_AccessType.ASSETS_DOWNLOAD.getValue();
    /** Business Documents = B */
    public static final String BPACCESSTYPE_BusinessDocuments = X_Ref_AD_User_BP_AccessType.BUSINESS_DOCUMENTS.getValue();
    /** Requests = R */
    public static final String BPACCESSTYPE_Requests = X_Ref_AD_User_BP_AccessType.REQUESTS.getValue();
    /** Set Access Type.
    @param BPAccessType Type of Access of the user/contact to Business Partner information and resources */
    public void setBPAccessType (String BPAccessType)
    {
        if (BPAccessType == null) throw new IllegalArgumentException ("BPAccessType is mandatory");
        if (!X_Ref_AD_User_BP_AccessType.isValid(BPAccessType))
        throw new IllegalArgumentException ("BPAccessType Invalid value - " + BPAccessType + " - Reference_ID=358 - A - B - R");
        set_Value ("BPAccessType", BPAccessType);
        
    }
    
    /** Get Access Type.
    @return Type of Access of the user/contact to Business Partner information and resources */
    public String getBPAccessType() 
    {
        return (String)get_Value("BPAccessType");
        
    }
    
    /** Set Document BaseType.
    @param DocBaseType Logical type of document */
    public void setDocBaseType (String DocBaseType)
    {
        set_Value ("DocBaseType", DocBaseType);
        
    }
    
    /** Get Document BaseType.
    @return Logical type of document */
    public String getDocBaseType() 
    {
        return (String)get_Value("DocBaseType");
        
    }
    
    /** Set Request Type.
    @param R_RequestType_ID Type of request (e.g. Inquiry, Complaint...) */
    public void setR_RequestType_ID (int R_RequestType_ID)
    {
        if (R_RequestType_ID <= 0) set_Value ("R_RequestType_ID", null);
        else
        set_Value ("R_RequestType_ID", Integer.valueOf(R_RequestType_ID));
        
    }
    
    /** Get Request Type.
    @return Type of request (e.g. Inquiry, Complaint...) */
    public int getR_RequestType_ID() 
    {
        return get_ValueAsInt("R_RequestType_ID");
        
    }
    
    
}
