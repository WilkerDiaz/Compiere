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
/** Generated Model for A_Asset_Delivery
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_A_Asset_Delivery.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_A_Asset_Delivery extends PO
{
    /** Standard Constructor
    @param ctx context
    @param A_Asset_Delivery_ID id
    @param trx transaction
    */
    public X_A_Asset_Delivery (Ctx ctx, int A_Asset_Delivery_ID, Trx trx)
    {
        super (ctx, A_Asset_Delivery_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (A_Asset_Delivery_ID == 0)
        {
            setA_Asset_Delivery_ID (0);
            setA_Asset_ID (0);
            setMovementDate (new Timestamp(System.currentTimeMillis()));
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_A_Asset_Delivery (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=541 */
    public static final int Table_ID=541;
    
    /** TableName=A_Asset_Delivery */
    public static final String Table_Name="A_Asset_Delivery";
    
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
    
    /** Set Asset Delivery.
    @param A_Asset_Delivery_ID Delivery of Asset */
    public void setA_Asset_Delivery_ID (int A_Asset_Delivery_ID)
    {
        if (A_Asset_Delivery_ID < 1) throw new IllegalArgumentException ("A_Asset_Delivery_ID is mandatory.");
        set_ValueNoCheck ("A_Asset_Delivery_ID", Integer.valueOf(A_Asset_Delivery_ID));
        
    }
    
    /** Get Asset Delivery.
    @return Delivery of Asset */
    public int getA_Asset_Delivery_ID() 
    {
        return get_ValueAsInt("A_Asset_Delivery_ID");
        
    }
    
    /** Set Asset.
    @param A_Asset_ID Asset used internally or by customers */
    public void setA_Asset_ID (int A_Asset_ID)
    {
        if (A_Asset_ID < 1) throw new IllegalArgumentException ("A_Asset_ID is mandatory.");
        set_ValueNoCheck ("A_Asset_ID", Integer.valueOf(A_Asset_ID));
        
    }
    
    /** Get Asset.
    @return Asset used internally or by customers */
    public int getA_Asset_ID() 
    {
        return get_ValueAsInt("A_Asset_ID");
        
    }
    
    /** Set Delivery Confirmation.
    @param DeliveryConfirmation EMail Delivery confirmation */
    public void setDeliveryConfirmation (String DeliveryConfirmation)
    {
        set_Value ("DeliveryConfirmation", DeliveryConfirmation);
        
    }
    
    /** Get Delivery Confirmation.
    @return EMail Delivery confirmation */
    public String getDeliveryConfirmation() 
    {
        return (String)get_Value("DeliveryConfirmation");
        
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
    
    /** Set EMail Address.
    @param EMail Electronic Mail Address */
    public void setEMail (String EMail)
    {
        set_ValueNoCheck ("EMail", EMail);
        
    }
    
    /** Get EMail Address.
    @return Electronic Mail Address */
    public String getEMail() 
    {
        return (String)get_Value("EMail");
        
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
    
    /** Set Lot No.
    @param Lot Lot number (alphanumeric) */
    public void setLot (String Lot)
    {
        set_ValueNoCheck ("Lot", Lot);
        
    }
    
    /** Get Lot No.
    @return Lot number (alphanumeric) */
    public String getLot() 
    {
        return (String)get_Value("Lot");
        
    }
    
    /** Set Shipment/Receipt Line.
    @param M_InOutLine_ID Line on Shipment or Receipt document */
    public void setM_InOutLine_ID (int M_InOutLine_ID)
    {
        if (M_InOutLine_ID <= 0) set_ValueNoCheck ("M_InOutLine_ID", null);
        else
        set_ValueNoCheck ("M_InOutLine_ID", Integer.valueOf(M_InOutLine_ID));
        
    }
    
    /** Get Shipment/Receipt Line.
    @return Line on Shipment or Receipt document */
    public int getM_InOutLine_ID() 
    {
        return get_ValueAsInt("M_InOutLine_ID");
        
    }
    
    /** Set Product Download.
    @param M_ProductDownload_ID Product downloads */
    public void setM_ProductDownload_ID (int M_ProductDownload_ID)
    {
        if (M_ProductDownload_ID <= 0) set_Value ("M_ProductDownload_ID", null);
        else
        set_Value ("M_ProductDownload_ID", Integer.valueOf(M_ProductDownload_ID));
        
    }
    
    /** Get Product Download.
    @return Product downloads */
    public int getM_ProductDownload_ID() 
    {
        return get_ValueAsInt("M_ProductDownload_ID");
        
    }
    
    /** Set Message ID.
    @param MessageID EMail Message ID */
    public void setMessageID (String MessageID)
    {
        set_ValueNoCheck ("MessageID", MessageID);
        
    }
    
    /** Get Message ID.
    @return EMail Message ID */
    public String getMessageID() 
    {
        return (String)get_Value("MessageID");
        
    }
    
    /** Set Movement Date.
    @param MovementDate Date a product was moved in or out of inventory */
    public void setMovementDate (Timestamp MovementDate)
    {
        if (MovementDate == null) throw new IllegalArgumentException ("MovementDate is mandatory.");
        set_ValueNoCheck ("MovementDate", MovementDate);
        
    }
    
    /** Get Movement Date.
    @return Date a product was moved in or out of inventory */
    public Timestamp getMovementDate() 
    {
        return (Timestamp)get_Value("MovementDate");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getMovementDate()));
        
    }
    
    /** Set Referrer.
    @param Referrer Referring web address */
    public void setReferrer (String Referrer)
    {
        set_ValueNoCheck ("Referrer", Referrer);
        
    }
    
    /** Get Referrer.
    @return Referring web address */
    public String getReferrer() 
    {
        return (String)get_Value("Referrer");
        
    }
    
    /** Set Remote Addr.
    @param Remote_Addr Remote Address */
    public void setRemote_Addr (String Remote_Addr)
    {
        set_ValueNoCheck ("Remote_Addr", Remote_Addr);
        
    }
    
    /** Get Remote Addr.
    @return Remote Address */
    public String getRemote_Addr() 
    {
        return (String)get_Value("Remote_Addr");
        
    }
    
    /** Set Remote Host.
    @param Remote_Host Remote host Info */
    public void setRemote_Host (String Remote_Host)
    {
        set_ValueNoCheck ("Remote_Host", Remote_Host);
        
    }
    
    /** Get Remote Host.
    @return Remote host Info */
    public String getRemote_Host() 
    {
        return (String)get_Value("Remote_Host");
        
    }
    
    /** Set Serial No.
    @param SerNo Product Serial Number */
    public void setSerNo (String SerNo)
    {
        set_ValueNoCheck ("SerNo", SerNo);
        
    }
    
    /** Get Serial No.
    @return Product Serial Number */
    public String getSerNo() 
    {
        return (String)get_Value("SerNo");
        
    }
    
    /** Set URL.
    @param URL Full URL address - e.g. http://www.compiere.org */
    public void setURL (String URL)
    {
        set_ValueNoCheck ("URL", URL);
        
    }
    
    /** Get URL.
    @return Full URL address - e.g. http://www.compiere.org */
    public String getURL() 
    {
        return (String)get_Value("URL");
        
    }
    
    /** Set Version No.
    @param VersionNo Version Number */
    public void setVersionNo (String VersionNo)
    {
        set_ValueNoCheck ("VersionNo", VersionNo);
        
    }
    
    /** Get Version No.
    @return Version Number */
    public String getVersionNo() 
    {
        return (String)get_Value("VersionNo");
        
    }
    
    
}
