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
/** Generated Model for C_RfQ
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_RfQ.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_RfQ extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_RfQ_ID id
    @param trx transaction
    */
    public X_C_RfQ (Ctx ctx, int C_RfQ_ID, Trx trx)
    {
        super (ctx, C_RfQ_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_RfQ_ID == 0)
        {
            setC_Currency_ID (0);	// @$C_Currency_ID @
            setC_RfQ_ID (0);
            setC_RfQ_Topic_ID (0);
            setDateResponse (new Timestamp(System.currentTimeMillis()));
            setDocumentNo (null);
            setIsInvitedVendorsOnly (false);
            setIsQuoteAllQty (false);
            setIsQuoteTotalAmt (false);
            setIsRfQResponseAccepted (true);	// Y
            setIsSelfService (true);	// Y
            setName (null);
            setProcessed (false);	// N
            setQuoteType (null);	// S
            setSalesRep_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_RfQ (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=677 */
    public static final int Table_ID=677;
    
    /** TableName=C_RfQ */
    public static final String Table_Name="C_RfQ";
    
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
    
    /** Set Partner Location.
    @param C_BPartner_Location_ID Identifies the (ship to) address for this Business Partner */
    public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
    {
        if (C_BPartner_Location_ID <= 0) set_Value ("C_BPartner_Location_ID", null);
        else
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
    
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID <= 0) set_Value ("C_Order_ID", null);
        else
        set_Value ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** Set RfQ.
    @param C_RfQ_ID Request for Quotation */
    public void setC_RfQ_ID (int C_RfQ_ID)
    {
        if (C_RfQ_ID < 1) throw new IllegalArgumentException ("C_RfQ_ID is mandatory.");
        set_ValueNoCheck ("C_RfQ_ID", Integer.valueOf(C_RfQ_ID));
        
    }
    
    /** Get RfQ.
    @return Request for Quotation */
    public int getC_RfQ_ID() 
    {
        return get_ValueAsInt("C_RfQ_ID");
        
    }
    
    /** Set RfQ Topic.
    @param C_RfQ_Topic_ID Topic for Request for Quotations */
    public void setC_RfQ_Topic_ID (int C_RfQ_Topic_ID)
    {
        if (C_RfQ_Topic_ID < 1) throw new IllegalArgumentException ("C_RfQ_Topic_ID is mandatory.");
        set_Value ("C_RfQ_Topic_ID", Integer.valueOf(C_RfQ_Topic_ID));
        
    }
    
    /** Get RfQ Topic.
    @return Topic for Request for Quotations */
    public int getC_RfQ_Topic_ID() 
    {
        return get_ValueAsInt("C_RfQ_Topic_ID");
        
    }
    
    /** Set Copy Lines.
    @param CopyLines Copy Lines */
    public void setCopyLines (String CopyLines)
    {
        set_Value ("CopyLines", CopyLines);
        
    }
    
    /** Get Copy Lines.
    @return Copy Lines */
    public String getCopyLines() 
    {
        return (String)get_Value("CopyLines");
        
    }
    
    /** Set Create PO.
    @param CreatePO Create Purchase Order */
    public void setCreatePO (String CreatePO)
    {
        set_Value ("CreatePO", CreatePO);
        
    }
    
    /** Get Create PO.
    @return Create Purchase Order */
    public String getCreatePO() 
    {
        return (String)get_Value("CreatePO");
        
    }
    
    /** Set Create SO.
    @param CreateSO Create SO */
    public void setCreateSO (String CreateSO)
    {
        set_Value ("CreateSO", CreateSO);
        
    }
    
    /** Get Create SO.
    @return Create SO */
    public String getCreateSO() 
    {
        return (String)get_Value("CreateSO");
        
    }
    
    /** Set Response Date.
    @param DateResponse Date of the Response */
    public void setDateResponse (Timestamp DateResponse)
    {
        if (DateResponse == null) throw new IllegalArgumentException ("DateResponse is mandatory.");
        set_Value ("DateResponse", DateResponse);
        
    }
    
    /** Get Response Date.
    @return Date of the Response */
    public Timestamp getDateResponse() 
    {
        return (Timestamp)get_Value("DateResponse");
        
    }
    
    /** Set Work Complete.
    @param DateWorkComplete Date when work is (planned to be) complete */
    public void setDateWorkComplete (Timestamp DateWorkComplete)
    {
        set_Value ("DateWorkComplete", DateWorkComplete);
        
    }
    
    /** Get Work Complete.
    @return Date when work is (planned to be) complete */
    public Timestamp getDateWorkComplete() 
    {
        return (Timestamp)get_Value("DateWorkComplete");
        
    }
    
    /** Set Work Start.
    @param DateWorkStart Date when work is (planned to be) started */
    public void setDateWorkStart (Timestamp DateWorkStart)
    {
        set_Value ("DateWorkStart", DateWorkStart);
        
    }
    
    /** Get Work Start.
    @return Date when work is (planned to be) started */
    public Timestamp getDateWorkStart() 
    {
        return (Timestamp)get_Value("DateWorkStart");
        
    }
    
    /** Set Delivery Days.
    @param DeliveryDays Number of Days (planned) until Delivery */
    public void setDeliveryDays (int DeliveryDays)
    {
        set_Value ("DeliveryDays", Integer.valueOf(DeliveryDays));
        
    }
    
    /** Get Delivery Days.
    @return Number of Days (planned) until Delivery */
    public int getDeliveryDays() 
    {
        return get_ValueAsInt("DeliveryDays");
        
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
    
    /** Set Document No.
    @param DocumentNo Document sequence number of the document */
    public void setDocumentNo (String DocumentNo)
    {
        if (DocumentNo == null) throw new IllegalArgumentException ("DocumentNo is mandatory.");
        set_Value ("DocumentNo", DocumentNo);
        
    }
    
    /** Get Document No.
    @return Document sequence number of the document */
    public String getDocumentNo() 
    {
        return (String)get_Value("DocumentNo");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getDocumentNo());
        
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
    
    /** Set Invited Vendors Only.
    @param IsInvitedVendorsOnly Only invited vendors can respond to an RfQ */
    public void setIsInvitedVendorsOnly (boolean IsInvitedVendorsOnly)
    {
        set_Value ("IsInvitedVendorsOnly", Boolean.valueOf(IsInvitedVendorsOnly));
        
    }
    
    /** Get Invited Vendors Only.
    @return Only invited vendors can respond to an RfQ */
    public boolean isInvitedVendorsOnly() 
    {
        return get_ValueAsBoolean("IsInvitedVendorsOnly");
        
    }
    
    /** Set Quote All Quantities.
    @param IsQuoteAllQty Suppliers are requested to provide responses for all quantities */
    public void setIsQuoteAllQty (boolean IsQuoteAllQty)
    {
        set_Value ("IsQuoteAllQty", Boolean.valueOf(IsQuoteAllQty));
        
    }
    
    /** Get Quote All Quantities.
    @return Suppliers are requested to provide responses for all quantities */
    public boolean isQuoteAllQty() 
    {
        return get_ValueAsBoolean("IsQuoteAllQty");
        
    }
    
    /** Set Quote Total Amt.
    @param IsQuoteTotalAmt The response can have just the total amount for the RfQ */
    public void setIsQuoteTotalAmt (boolean IsQuoteTotalAmt)
    {
        set_Value ("IsQuoteTotalAmt", Boolean.valueOf(IsQuoteTotalAmt));
        
    }
    
    /** Get Quote Total Amt.
    @return The response can have just the total amount for the RfQ */
    public boolean isQuoteTotalAmt() 
    {
        return get_ValueAsBoolean("IsQuoteTotalAmt");
        
    }
    
    /** Set Responses Accepted.
    @param IsRfQResponseAccepted Are Responses to the Request for Quotation accepted */
    public void setIsRfQResponseAccepted (boolean IsRfQResponseAccepted)
    {
        set_Value ("IsRfQResponseAccepted", Boolean.valueOf(IsRfQResponseAccepted));
        
    }
    
    /** Get Responses Accepted.
    @return Are Responses to the Request for Quotation accepted */
    public boolean isRfQResponseAccepted() 
    {
        return get_ValueAsBoolean("IsRfQResponseAccepted");
        
    }
    
    /** Set Self-Service.
    @param IsSelfService This is a Self-Service entry or this entry can be changed via Self-Service */
    public void setIsSelfService (boolean IsSelfService)
    {
        set_Value ("IsSelfService", Boolean.valueOf(IsSelfService));
        
    }
    
    /** Get Self-Service.
    @return This is a Self-Service entry or this entry can be changed via Self-Service */
    public boolean isSelfService() 
    {
        return get_ValueAsBoolean("IsSelfService");
        
    }
    
    /** Set Margin %.
    @param Margin Margin for a product as a percentage */
    public void setMargin (java.math.BigDecimal Margin)
    {
        set_Value ("Margin", Margin);
        
    }
    
    /** Get Margin %.
    @return Margin for a product as a percentage */
    public java.math.BigDecimal getMargin() 
    {
        return get_ValueAsBigDecimal("Margin");
        
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
    
    /** Set Publish RfQ.
    @param PublishRfQ Publish RfQ */
    public void setPublishRfQ (String PublishRfQ)
    {
        set_Value ("PublishRfQ", PublishRfQ);
        
    }
    
    /** Get Publish RfQ.
    @return Publish RfQ */
    public String getPublishRfQ() 
    {
        return (String)get_Value("PublishRfQ");
        
    }
    
    /** Quote All Lines = A */
    public static final String QUOTETYPE_QuoteAllLines = X_Ref_C_RfQ_QuoteType.QUOTE_ALL_LINES.getValue();
    /** Quote Selected Lines = S */
    public static final String QUOTETYPE_QuoteSelectedLines = X_Ref_C_RfQ_QuoteType.QUOTE_SELECTED_LINES.getValue();
    /** Quote Total only = T */
    public static final String QUOTETYPE_QuoteTotalOnly = X_Ref_C_RfQ_QuoteType.QUOTE_TOTAL_ONLY.getValue();
    /** Set RfQ Type.
    @param QuoteType Request for Quotation Type */
    public void setQuoteType (String QuoteType)
    {
        if (QuoteType == null) throw new IllegalArgumentException ("QuoteType is mandatory");
        if (!X_Ref_C_RfQ_QuoteType.isValid(QuoteType))
        throw new IllegalArgumentException ("QuoteType Invalid value - " + QuoteType + " - Reference_ID=314 - A - S - T");
        set_Value ("QuoteType", QuoteType);
        
    }
    
    /** Get RfQ Type.
    @return Request for Quotation Type */
    public String getQuoteType() 
    {
        return (String)get_Value("QuoteType");
        
    }
    
    /** Set Rank RfQ.
    @param RankRfQ Rank RfQ */
    public void setRankRfQ (String RankRfQ)
    {
        set_Value ("RankRfQ", RankRfQ);
        
    }
    
    /** Get Rank RfQ.
    @return Rank RfQ */
    public String getRankRfQ() 
    {
        return (String)get_Value("RankRfQ");
        
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
