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
/** Generated Model for M_WorkCenter_Acct
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_WorkCenter_Acct.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_WorkCenter_Acct extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_WorkCenter_Acct_ID id
    @param trx transaction
    */
    public X_M_WorkCenter_Acct (Ctx ctx, int M_WorkCenter_Acct_ID, Trx trx)
    {
        super (ctx, M_WorkCenter_Acct_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_WorkCenter_Acct_ID == 0)
        {
            setC_AcctSchema_ID (0);
            setM_WorkCenter_ID (0);
            setWC_Overhead_Acct (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_WorkCenter_Acct (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27519639898789L;
    /** Last Updated Timestamp 2009-03-19 18:03:02.0 */
    public static final long updatedMS = 1237514582000L;
    /** AD_Table_ID=2086 */
    public static final int Table_ID=2086;
    
    /** TableName=M_WorkCenter_Acct */
    public static final String Table_Name="M_WorkCenter_Acct";
    
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
    
    /** Set Work Center.
    @param M_WorkCenter_ID Identifies a production area within a warehouse consisting of people and equipment */
    public void setM_WorkCenter_ID (int M_WorkCenter_ID)
    {
        if (M_WorkCenter_ID < 1) throw new IllegalArgumentException ("M_WorkCenter_ID is mandatory.");
        set_ValueNoCheck ("M_WorkCenter_ID", Integer.valueOf(M_WorkCenter_ID));
        
    }
    
    /** Get Work Center.
    @return Identifies a production area within a warehouse consisting of people and equipment */
    public int getM_WorkCenter_ID() 
    {
        return get_ValueAsInt("M_WorkCenter_ID");
        
    }
    
    /** Set Work Center Overhead.
    @param WC_Overhead_Acct Work Center Overhead Account */
    public void setWC_Overhead_Acct (int WC_Overhead_Acct)
    {
        set_Value ("WC_Overhead_Acct", Integer.valueOf(WC_Overhead_Acct));
        
    }
    
    /** Get Work Center Overhead.
    @return Work Center Overhead Account */
    public int getWC_Overhead_Acct() 
    {
        return get_ValueAsInt("WC_Overhead_Acct");
        
    }
    
    
}
