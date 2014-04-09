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
/** Generated Model for C_RfQResponse
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_RfQResponse.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_RfQResponse extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_RfQResponse_ID id
    @param trx transaction
    */
    public X_C_RfQResponse (Ctx ctx, int C_RfQResponse_ID, Trx trx)
    {
        super (ctx, C_RfQResponse_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_RfQResponse_ID == 0)
        {
            setC_BPartner_ID (0);
            setC_BPartner_Location_ID (0);
            setC_Currency_ID (0);	// @C_Currency_ID@
            setC_RfQResponse_ID (0);
            setC_RfQ_ID (0);
            setIsComplete (false);
            setIsSelectedWinner (false);
            setIsSelfService (false);
            setName (null);
            setPrice (Env.ZERO);
            setProcessed (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_RfQResponse (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=674 */
    public static final int Table_ID=674;
    
    /** TableName=C_RfQResponse */
    public static final String Table_Name="C_RfQResponse";
    
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
        if (AD_User_ID <= 0) set_ValueNoCheck ("AD_User_ID", null);
        else
        set_ValueNoCheck ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
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
    
    /** Set RfQ Response.
    @param C_RfQResponse_ID Request for Quotation Response from a potential Vendor */
    public void setC_RfQResponse_ID (int C_RfQResponse_ID)
    {
        if (C_RfQResponse_ID < 1) throw new IllegalArgumentException ("C_RfQResponse_ID is mandatory.");
        set_ValueNoCheck ("C_RfQResponse_ID", Integer.valueOf(C_RfQResponse_ID));
        
    }
    
    /** Get RfQ Response.
    @return Request for Quotation Response from a potential Vendor */
    public int getC_RfQResponse_ID() 
    {
        return get_ValueAsInt("C_RfQResponse_ID");
        
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
    
    /** Set Check Complete.
    @param CheckComplete Check Complete */
    public void setCheckComplete (String CheckComplete)
    {
        set_Value ("CheckComplete", CheckComplete);
        
    }
    
    /** Get Check Complete.
    @return Check Complete */
    public String getCheckComplete() 
    {
        return (String)get_Value("CheckComplete");
        
    }
    
    /** Set Invited.
    @param DateInvited Date when (last) invitation was sent */
    public void setDateInvited (Timestamp DateInvited)
    {
        set_Value ("DateInvited", DateInvited);
        
    }
    
    /** Get Invited.
    @return Date when (last) invitation was sent */
    public Timestamp getDateInvited() 
    {
        return (Timestamp)get_Value("DateInvited");
        
    }
    
    /** Set Response Date.
    @param DateResponse Date of the Response */
    public void setDateResponse (Timestamp DateResponse)
    {
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
    
    /** Set Complete.
    @param IsComplete It is complete */
    public void setIsComplete (boolean IsComplete)
    {
        set_Value ("IsComplete", Boolean.valueOf(IsComplete));
        
    }
    
    /** Get Complete.
    @return It is complete */
    public boolean isComplete() 
    {
        return get_ValueAsBoolean("IsComplete");
        
    }
    
    /** Set Selected Winner.
    @param IsSelectedWinner The response is the selected winner */
    public void setIsSelectedWinner (boolean IsSelectedWinner)
    {
        set_Value ("IsSelectedWinner", Boolean.valueOf(IsSelectedWinner));
        
    }
    
    /** Get Selected Winner.
    @return The response is the selected winner */
    public boolean isSelectedWinner() 
    {
        return get_ValueAsBoolean("IsSelectedWinner");
        
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
    
    /** Set Price.
    @param Price Price */
    public void setPrice (java.math.BigDecimal Price)
    {
        if (Price == null) throw new IllegalArgumentException ("Price is mandatory.");
        set_Value ("Price", Price);
        
    }
    
    /** Get Price.
    @return Price */
    public java.math.BigDecimal getPrice() 
    {
        return get_ValueAsBigDecimal("Price");
        
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
    
    /** Set Ranking.
    @param Ranking Relative Rank Number */
    public void setRanking (int Ranking)
    {
        set_Value ("Ranking", Integer.valueOf(Ranking));
        
    }
    
    /** Get Ranking.
    @return Relative Rank Number */
    public int getRanking() 
    {
        return get_ValueAsInt("Ranking");
        
    }
    
    
}
