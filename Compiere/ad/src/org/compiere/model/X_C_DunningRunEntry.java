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
/** Generated Model for C_DunningRunEntry
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_DunningRunEntry.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_DunningRunEntry extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_DunningRunEntry_ID id
    @param trx transaction
    */
    public X_C_DunningRunEntry (Ctx ctx, int C_DunningRunEntry_ID, Trx trx)
    {
        super (ctx, C_DunningRunEntry_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_DunningRunEntry_ID == 0)
        {
            setAmt (Env.ZERO);
            setC_BPartner_ID (0);
            setC_BPartner_Location_ID (0);
            setC_Currency_ID (0);
            setC_DunningRunEntry_ID (0);
            setC_DunningRun_ID (0);
            setProcessed (false);	// N
            setQty (Env.ZERO);
            setSalesRep_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_DunningRunEntry (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=527 */
    public static final int Table_ID=527;
    
    /** TableName=C_DunningRunEntry */
    public static final String Table_Name="C_DunningRunEntry";
    
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
    
    /** Set Amount.
    @param Amt Amount */
    public void setAmt (java.math.BigDecimal Amt)
    {
        if (Amt == null) throw new IllegalArgumentException ("Amt is mandatory.");
        set_Value ("Amt", Amt);
        
    }
    
    /** Get Amount.
    @return Amount */
    public java.math.BigDecimal getAmt() 
    {
        return get_ValueAsBigDecimal("Amt");
        
    }
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID < 1) throw new IllegalArgumentException ("C_BPartner_ID is mandatory.");
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Partner Location.
    @param C_BPartner_Location_ID Identifies the (ship to) address for this Business Partner */
    public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
    {
        if (C_BPartner_Location_ID < 1) throw new IllegalArgumentException ("C_BPartner_Location_ID is mandatory.");
        set_Value ("C_BPartner_Location_ID", Integer.valueOf(C_BPartner_Location_ID));
        
    }
    
    /** Get Partner Location.
    @return Identifies the (ship to) address for this Business Partner */
    public int getC_BPartner_Location_ID() 
    {
        return get_ValueAsInt("C_BPartner_Location_ID");
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID < 1) throw new IllegalArgumentException ("C_Currency_ID is mandatory.");
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set Dunning Run Entry.
    @param C_DunningRunEntry_ID Dunning Run Entry */
    public void setC_DunningRunEntry_ID (int C_DunningRunEntry_ID)
    {
        if (C_DunningRunEntry_ID < 1) throw new IllegalArgumentException ("C_DunningRunEntry_ID is mandatory.");
        set_ValueNoCheck ("C_DunningRunEntry_ID", Integer.valueOf(C_DunningRunEntry_ID));
        
    }
    
    /** Get Dunning Run Entry.
    @return Dunning Run Entry */
    public int getC_DunningRunEntry_ID() 
    {
        return get_ValueAsInt("C_DunningRunEntry_ID");
        
    }
    
    /** Set Dunning Run.
    @param C_DunningRun_ID Dunning Run */
    public void setC_DunningRun_ID (int C_DunningRun_ID)
    {
        if (C_DunningRun_ID < 1) throw new IllegalArgumentException ("C_DunningRun_ID is mandatory.");
        set_ValueNoCheck ("C_DunningRun_ID", Integer.valueOf(C_DunningRun_ID));
        
    }
    
    /** Get Dunning Run.
    @return Dunning Run */
    public int getC_DunningRun_ID() 
    {
        return get_ValueAsInt("C_DunningRun_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_DunningRun_ID()));
        
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
    
    /** Set Quantity.
    @param Qty Quantity */
    public void setQty (java.math.BigDecimal Qty)
    {
        if (Qty == null) throw new IllegalArgumentException ("Qty is mandatory.");
        set_Value ("Qty", Qty);
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public java.math.BigDecimal getQty() 
    {
        return get_ValueAsBigDecimal("Qty");
        
    }
    
    /** Set Representative.
    @param SalesRep_ID Company Agent like Sales Representative, Purchase Agent, and Customer Service Representative... */
    public void setSalesRep_ID (int SalesRep_ID)
    {
        if (SalesRep_ID < 1) throw new IllegalArgumentException ("SalesRep_ID is mandatory.");
        set_Value ("SalesRep_ID", Integer.valueOf(SalesRep_ID));
        
    }
    
    /** Get Representative.
    @return Company Agent like Sales Representative, Purchase Agent, and Customer Service Representative... */
    public int getSalesRep_ID() 
    {
        return get_ValueAsInt("SalesRep_ID");
        
    }
    
    
}
