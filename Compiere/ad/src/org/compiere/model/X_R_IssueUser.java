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
/** Generated Model for R_IssueUser
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_R_IssueUser.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_R_IssueUser extends PO
{
    /** Standard Constructor
    @param ctx context
    @param R_IssueUser_ID id
    @param trx transaction
    */
    public X_R_IssueUser (Ctx ctx, int R_IssueUser_ID, Trx trx)
    {
        super (ctx, R_IssueUser_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (R_IssueUser_ID == 0)
        {
            setR_IssueUser_ID (0);
            setUserName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_R_IssueUser (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=841 */
    public static final int Table_ID=841;
    
    /** TableName=R_IssueUser */
    public static final String Table_Name="R_IssueUser";
    
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
    
    /** Set Issue User.
    @param R_IssueUser_ID User who reported issues */
    public void setR_IssueUser_ID (int R_IssueUser_ID)
    {
        if (R_IssueUser_ID < 1) throw new IllegalArgumentException ("R_IssueUser_ID is mandatory.");
        set_ValueNoCheck ("R_IssueUser_ID", Integer.valueOf(R_IssueUser_ID));
        
    }
    
    /** Get Issue User.
    @return User who reported issues */
    public int getR_IssueUser_ID() 
    {
        return get_ValueAsInt("R_IssueUser_ID");
        
    }
    
    /** Set Registered EMail.
    @param UserName Email of the responsible for the System */
    public void setUserName (String UserName)
    {
        if (UserName == null) throw new IllegalArgumentException ("UserName is mandatory.");
        set_ValueNoCheck ("UserName", UserName);
        
    }
    
    /** Get Registered EMail.
    @return Email of the responsible for the System */
    public String getUserName() 
    {
        return (String)get_Value("UserName");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getUserName());
        
    }
    
    
}
