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
/** Generated Model for R_RequestUpdate
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_R_RequestUpdate.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_R_RequestUpdate extends PO
{
    /** Standard Constructor
    @param ctx context
    @param R_RequestUpdate_ID id
    @param trx transaction
    */
    public X_R_RequestUpdate (Ctx ctx, int R_RequestUpdate_ID, Trx trx)
    {
        super (ctx, R_RequestUpdate_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (R_RequestUpdate_ID == 0)
        {
            setConfidentialTypeEntry (null);
            setR_RequestUpdate_ID (0);
            setR_Request_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_R_RequestUpdate (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=802 */
    public static final int Table_ID=802;
    
    /** TableName=R_RequestUpdate */
    public static final String Table_Name="R_RequestUpdate";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Public Information = A */
    public static final String CONFIDENTIALTYPEENTRY_PublicInformation = X_Ref_R_Request_Confidential.PUBLIC_INFORMATION.getValue();
    /** Partner Confidential = C */
    public static final String CONFIDENTIALTYPEENTRY_PartnerConfidential = X_Ref_R_Request_Confidential.PARTNER_CONFIDENTIAL.getValue();
    /** Internal = I */
    public static final String CONFIDENTIALTYPEENTRY_Internal = X_Ref_R_Request_Confidential.INTERNAL.getValue();
    /** Private Information = P */
    public static final String CONFIDENTIALTYPEENTRY_PrivateInformation = X_Ref_R_Request_Confidential.PRIVATE_INFORMATION.getValue();
    /** Set Entry Access Level.
    @param ConfidentialTypeEntry Confidentiality of the individual entry */
    public void setConfidentialTypeEntry (String ConfidentialTypeEntry)
    {
        if (ConfidentialTypeEntry == null) throw new IllegalArgumentException ("ConfidentialTypeEntry is mandatory");
        if (!X_Ref_R_Request_Confidential.isValid(ConfidentialTypeEntry))
        throw new IllegalArgumentException ("ConfidentialTypeEntry Invalid value - " + ConfidentialTypeEntry + " - Reference_ID=340 - A - C - I - P");
        set_Value ("ConfidentialTypeEntry", ConfidentialTypeEntry);
        
    }
    
    /** Get Entry Access Level.
    @return Confidentiality of the individual entry */
    public String getConfidentialTypeEntry() 
    {
        return (String)get_Value("ConfidentialTypeEntry");
        
    }
    
    /** Set End Time.
    @param EndTime End of the time span */
    public void setEndTime (Timestamp EndTime)
    {
        set_Value ("EndTime", EndTime);
        
    }
    
    /** Get End Time.
    @return End of the time span */
    public Timestamp getEndTime() 
    {
        return (Timestamp)get_Value("EndTime");
        
    }
    
    /** Set Product Used.
    @param M_ProductSpent_ID Product/Resource/Service used in Request */
    public void setM_ProductSpent_ID (int M_ProductSpent_ID)
    {
        if (M_ProductSpent_ID <= 0) set_Value ("M_ProductSpent_ID", null);
        else
        set_Value ("M_ProductSpent_ID", Integer.valueOf(M_ProductSpent_ID));
        
    }
    
    /** Get Product Used.
    @return Product/Resource/Service used in Request */
    public int getM_ProductSpent_ID() 
    {
        return get_ValueAsInt("M_ProductSpent_ID");
        
    }
    
    /** Set Quantity Invoiced.
    @param QtyInvoiced Invoiced Quantity */
    public void setQtyInvoiced (java.math.BigDecimal QtyInvoiced)
    {
        set_Value ("QtyInvoiced", QtyInvoiced);
        
    }
    
    /** Get Quantity Invoiced.
    @return Invoiced Quantity */
    public java.math.BigDecimal getQtyInvoiced() 
    {
        return get_ValueAsBigDecimal("QtyInvoiced");
        
    }
    
    /** Set Quantity Used.
    @param QtySpent Quantity used for this event */
    public void setQtySpent (java.math.BigDecimal QtySpent)
    {
        set_Value ("QtySpent", QtySpent);
        
    }
    
    /** Get Quantity Used.
    @return Quantity used for this event */
    public java.math.BigDecimal getQtySpent() 
    {
        return get_ValueAsBigDecimal("QtySpent");
        
    }
    
    /** Set Request Update.
    @param R_RequestUpdate_ID Request Updates */
    public void setR_RequestUpdate_ID (int R_RequestUpdate_ID)
    {
        if (R_RequestUpdate_ID < 1) throw new IllegalArgumentException ("R_RequestUpdate_ID is mandatory.");
        set_ValueNoCheck ("R_RequestUpdate_ID", Integer.valueOf(R_RequestUpdate_ID));
        
    }
    
    /** Get Request Update.
    @return Request Updates */
    public int getR_RequestUpdate_ID() 
    {
        return get_ValueAsInt("R_RequestUpdate_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getR_RequestUpdate_ID()));
        
    }
    
    /** Set Request.
    @param R_Request_ID Request from a Business Partner or Prospect */
    public void setR_Request_ID (int R_Request_ID)
    {
        if (R_Request_ID < 1) throw new IllegalArgumentException ("R_Request_ID is mandatory.");
        set_ValueNoCheck ("R_Request_ID", Integer.valueOf(R_Request_ID));
        
    }
    
    /** Get Request.
    @return Request from a Business Partner or Prospect */
    public int getR_Request_ID() 
    {
        return get_ValueAsInt("R_Request_ID");
        
    }
    
    /** Set Result.
    @param Result Result of the action taken */
    public void setResult (String Result)
    {
        set_ValueNoCheck ("Result", Result);
        
    }
    
    /** Get Result.
    @return Result of the action taken */
    public String getResult() 
    {
        return (String)get_Value("Result");
        
    }
    
    /** Set Start Time.
    @param StartTime Time started */
    public void setStartTime (Timestamp StartTime)
    {
        set_Value ("StartTime", StartTime);
        
    }
    
    /** Get Start Time.
    @return Time started */
    public Timestamp getStartTime() 
    {
        return (Timestamp)get_Value("StartTime");
        
    }
    
    
}
