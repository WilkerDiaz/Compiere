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
/** Generated Model for C_Currency_Acct
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Currency_Acct.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Currency_Acct extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Currency_Acct_ID id
    @param trx transaction
    */
    public X_C_Currency_Acct (Ctx ctx, int C_Currency_Acct_ID, Trx trx)
    {
        super (ctx, C_Currency_Acct_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Currency_Acct_ID == 0)
        {
            setC_AcctSchema_ID (0);
            setC_Currency_ID (0);
            setRealizedGain_Acct (0);
            setRealizedLoss_Acct (0);
            setUnrealizedGain_Acct (0);
            setUnrealizedLoss_Acct (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Currency_Acct (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=638 */
    public static final int Table_ID=638;
    
    /** TableName=C_Currency_Acct */
    public static final String Table_Name="C_Currency_Acct";
    
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
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID < 1) throw new IllegalArgumentException ("C_Currency_ID is mandatory.");
        set_ValueNoCheck ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set Realized Gain Acct.
    @param RealizedGain_Acct Realized Gain Account */
    public void setRealizedGain_Acct (int RealizedGain_Acct)
    {
        set_Value ("RealizedGain_Acct", Integer.valueOf(RealizedGain_Acct));
        
    }
    
    /** Get Realized Gain Acct.
    @return Realized Gain Account */
    public int getRealizedGain_Acct() 
    {
        return get_ValueAsInt("RealizedGain_Acct");
        
    }
    
    /** Set Realized Loss Acct.
    @param RealizedLoss_Acct Realized Loss Account */
    public void setRealizedLoss_Acct (int RealizedLoss_Acct)
    {
        set_Value ("RealizedLoss_Acct", Integer.valueOf(RealizedLoss_Acct));
        
    }
    
    /** Get Realized Loss Acct.
    @return Realized Loss Account */
    public int getRealizedLoss_Acct() 
    {
        return get_ValueAsInt("RealizedLoss_Acct");
        
    }
    
    /** Set Unrealized Gain Acct.
    @param UnrealizedGain_Acct Unrealized Gain Account for currency revaluation */
    public void setUnrealizedGain_Acct (int UnrealizedGain_Acct)
    {
        set_Value ("UnrealizedGain_Acct", Integer.valueOf(UnrealizedGain_Acct));
        
    }
    
    /** Get Unrealized Gain Acct.
    @return Unrealized Gain Account for currency revaluation */
    public int getUnrealizedGain_Acct() 
    {
        return get_ValueAsInt("UnrealizedGain_Acct");
        
    }
    
    /** Set Unrealized Loss Acct.
    @param UnrealizedLoss_Acct Unrealized Loss Account for currency revaluation */
    public void setUnrealizedLoss_Acct (int UnrealizedLoss_Acct)
    {
        set_Value ("UnrealizedLoss_Acct", Integer.valueOf(UnrealizedLoss_Acct));
        
    }
    
    /** Get Unrealized Loss Acct.
    @return Unrealized Loss Account for currency revaluation */
    public int getUnrealizedLoss_Acct() 
    {
        return get_ValueAsInt("UnrealizedLoss_Acct");
        
    }
    
    
}
