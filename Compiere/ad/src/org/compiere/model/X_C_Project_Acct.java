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
/** Generated Model for C_Project_Acct
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Project_Acct.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Project_Acct extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Project_Acct_ID id
    @param trx transaction
    */
    public X_C_Project_Acct (Ctx ctx, int C_Project_Acct_ID, Trx trx)
    {
        super (ctx, C_Project_Acct_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Project_Acct_ID == 0)
        {
            setC_AcctSchema_ID (0);
            setC_Project_ID (0);
            setPJ_Asset_Acct (0);
            setPJ_WIP_Acct (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Project_Acct (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=204 */
    public static final int Table_ID=204;
    
    /** TableName=C_Project_Acct */
    public static final String Table_Name="C_Project_Acct";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Accounting Schema.
    @param C_AcctSchema_ID Rules for accounting */
    public void setC_AcctSchema_ID (int C_AcctSchema_ID)
    {
        if (C_AcctSchema_ID < 1) throw new IllegalArgumentException ("C_AcctSchema_ID is mandatory.");
        set_ValueNoCheck ("C_AcctSchema_ID", Integer.valueOf(C_AcctSchema_ID));
        
    }
    
    /** Get Accounting Schema.
    @return Rules for accounting */
    public int getC_AcctSchema_ID() 
    {
        return get_ValueAsInt("C_AcctSchema_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_AcctSchema_ID()));
        
    }
    
    /** Set Project.
    @param C_Project_ID Financial Project */
    public void setC_Project_ID (int C_Project_ID)
    {
        if (C_Project_ID < 1) throw new IllegalArgumentException ("C_Project_ID is mandatory.");
        set_ValueNoCheck ("C_Project_ID", Integer.valueOf(C_Project_ID));
        
    }
    
    /** Get Project.
    @return Financial Project */
    public int getC_Project_ID() 
    {
        return get_ValueAsInt("C_Project_ID");
        
    }
    
    /** Set Project Asset.
    @param PJ_Asset_Acct Project Asset Account */
    public void setPJ_Asset_Acct (int PJ_Asset_Acct)
    {
        set_Value ("PJ_Asset_Acct", Integer.valueOf(PJ_Asset_Acct));
        
    }
    
    /** Get Project Asset.
    @return Project Asset Account */
    public int getPJ_Asset_Acct() 
    {
        return get_ValueAsInt("PJ_Asset_Acct");
        
    }
    
    /** Set Work In Progress.
    @param PJ_WIP_Acct Account for Work in Progress */
    public void setPJ_WIP_Acct (int PJ_WIP_Acct)
    {
        set_Value ("PJ_WIP_Acct", Integer.valueOf(PJ_WIP_Acct));
        
    }
    
    /** Get Work In Progress.
    @return Account for Work in Progress */
    public int getPJ_WIP_Acct() 
    {
        return get_ValueAsInt("PJ_WIP_Acct");
        
    }
    
    
}
