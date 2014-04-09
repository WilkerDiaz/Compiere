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
/** Generated Model for C_Commission
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Commission.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Commission extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Commission_ID id
    @param trx transaction
    */
    public X_C_Commission (Ctx ctx, int C_Commission_ID, Trx trx)
    {
        super (ctx, C_Commission_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Commission_ID == 0)
        {
            setC_BPartner_ID (0);
            setC_Charge_ID (0);
            setC_Commission_ID (0);
            setC_Currency_ID (0);
            setDocBasisType (null);	// I
            setFrequencyType (null);	// M
            setListDetails (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Commission (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=429 */
    public static final int Table_ID=429;
    
    /** TableName=C_Commission */
    public static final String Table_Name="C_Commission";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Charge.
    @param C_Charge_ID Additional document charges */
    public void setC_Charge_ID (int C_Charge_ID)
    {
        if (C_Charge_ID < 1) throw new IllegalArgumentException ("C_Charge_ID is mandatory.");
        set_Value ("C_Charge_ID", Integer.valueOf(C_Charge_ID));
        
    }
    
    /** Get Charge.
    @return Additional document charges */
    public int getC_Charge_ID() 
    {
        return get_ValueAsInt("C_Charge_ID");
        
    }
    
    /** Set Commission.
    @param C_Commission_ID Commission */
    public void setC_Commission_ID (int C_Commission_ID)
    {
        if (C_Commission_ID < 1) throw new IllegalArgumentException ("C_Commission_ID is mandatory.");
        set_ValueNoCheck ("C_Commission_ID", Integer.valueOf(C_Commission_ID));
        
    }
    
    /** Get Commission.
    @return Commission */
    public int getC_Commission_ID() 
    {
        return get_ValueAsInt("C_Commission_ID");
        
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
    
    /** Set Create lines from.
    @param CreateFrom Process which will generate a new document lines based on an existing document */
    public void setCreateFrom (String CreateFrom)
    {
        set_Value ("CreateFrom", CreateFrom);
        
    }
    
    /** Get Create lines from.
    @return Process which will generate a new document lines based on an existing document */
    public String getCreateFrom() 
    {
        return (String)get_Value("CreateFrom");
        
    }
    
    /** Set Date Last Run.
    @param DateLastRun Date the process was last run. */
    public void setDateLastRun (Timestamp DateLastRun)
    {
        set_ValueNoCheck ("DateLastRun", DateLastRun);
        
    }
    
    /** Get Date Last Run.
    @return Date the process was last run. */
    public Timestamp getDateLastRun() 
    {
        return (Timestamp)get_Value("DateLastRun");
        
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
    
    /** Invoice = I */
    public static final String DOCBASISTYPE_Invoice = X_Ref_C_Commission_DocBasis.INVOICE.getValue();
    /** Order = O */
    public static final String DOCBASISTYPE_Order = X_Ref_C_Commission_DocBasis.ORDER.getValue();
    /** Receipt = R */
    public static final String DOCBASISTYPE_Receipt = X_Ref_C_Commission_DocBasis.RECEIPT.getValue();
    /** Set Calculation Basis.
    @param DocBasisType Basis for the calculation the commission */
    public void setDocBasisType (String DocBasisType)
    {
        if (DocBasisType == null) throw new IllegalArgumentException ("DocBasisType is mandatory");
        if (!X_Ref_C_Commission_DocBasis.isValid(DocBasisType))
        throw new IllegalArgumentException ("DocBasisType Invalid value - " + DocBasisType + " - Reference_ID=224 - I - O - R");
        set_Value ("DocBasisType", DocBasisType);
        
    }
    
    /** Get Calculation Basis.
    @return Basis for the calculation the commission */
    public String getDocBasisType() 
    {
        return (String)get_Value("DocBasisType");
        
    }
    
    /** Monthly = M */
    public static final String FREQUENCYTYPE_Monthly = X_Ref_C_Commission_Frequency.MONTHLY.getValue();
    /** Quarterly = Q */
    public static final String FREQUENCYTYPE_Quarterly = X_Ref_C_Commission_Frequency.QUARTERLY.getValue();
    /** Weekly = W */
    public static final String FREQUENCYTYPE_Weekly = X_Ref_C_Commission_Frequency.WEEKLY.getValue();
    /** Yearly = Y */
    public static final String FREQUENCYTYPE_Yearly = X_Ref_C_Commission_Frequency.YEARLY.getValue();
    /** Set Frequency Type.
    @param FrequencyType Frequency of event */
    public void setFrequencyType (String FrequencyType)
    {
        if (FrequencyType == null) throw new IllegalArgumentException ("FrequencyType is mandatory");
        if (!X_Ref_C_Commission_Frequency.isValid(FrequencyType))
        throw new IllegalArgumentException ("FrequencyType Invalid value - " + FrequencyType + " - Reference_ID=225 - M - Q - W - Y");
        set_Value ("FrequencyType", FrequencyType);
        
    }
    
    /** Get Frequency Type.
    @return Frequency of event */
    public String getFrequencyType() 
    {
        return (String)get_Value("FrequencyType");
        
    }
    
    /** Set List Details.
    @param ListDetails List document details */
    public void setListDetails (boolean ListDetails)
    {
        set_Value ("ListDetails", Boolean.valueOf(ListDetails));
        
    }
    
    /** Get List Details.
    @return List document details */
    public boolean isListDetails() 
    {
        return get_ValueAsBoolean("ListDetails");
        
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
    
    
}
