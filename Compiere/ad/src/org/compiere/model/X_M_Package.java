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
/** Generated Model for M_Package
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_Package.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_Package extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_Package_ID id
    @param trx transaction
    */
    public X_M_Package (Ctx ctx, int M_Package_ID, Trx trx)
    {
        super (ctx, M_Package_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_Package_ID == 0)
        {
            setDocumentNo (null);
            setM_InOut_ID (0);
            setM_Package_ID (0);
            setM_Shipper_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_Package (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=664 */
    public static final int Table_ID=664;
    
    /** TableName=M_Package */
    public static final String Table_Name="M_Package";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Date received.
    @param DateReceived Date a product was received */
    public void setDateReceived (Timestamp DateReceived)
    {
        set_Value ("DateReceived", DateReceived);
        
    }
    
    /** Get Date received.
    @return Date a product was received */
    public Timestamp getDateReceived() 
    {
        return (Timestamp)get_Value("DateReceived");
        
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
        set_ValueNoCheck ("DocumentNo", DocumentNo);
        
    }
    
    /** Get Document No.
    @return Document sequence number of the document */
    public String getDocumentNo() 
    {
        return (String)get_Value("DocumentNo");
        
    }
    
    /** Set Shipment/Receipt.
    @param M_InOut_ID Material Shipment Document */
    public void setM_InOut_ID (int M_InOut_ID)
    {
        if (M_InOut_ID < 1) throw new IllegalArgumentException ("M_InOut_ID is mandatory.");
        set_ValueNoCheck ("M_InOut_ID", Integer.valueOf(M_InOut_ID));
        
    }
    
    /** Get Shipment/Receipt.
    @return Material Shipment Document */
    public int getM_InOut_ID() 
    {
        return get_ValueAsInt("M_InOut_ID");
        
    }
    
    /** Set Package.
    @param M_Package_ID Shipment Package */
    public void setM_Package_ID (int M_Package_ID)
    {
        if (M_Package_ID < 1) throw new IllegalArgumentException ("M_Package_ID is mandatory.");
        set_ValueNoCheck ("M_Package_ID", Integer.valueOf(M_Package_ID));
        
    }
    
    /** Get Package.
    @return Shipment Package */
    public int getM_Package_ID() 
    {
        return get_ValueAsInt("M_Package_ID");
        
    }
    
    /** Set Freight Carrier.
    @param M_Shipper_ID Method or manner of product delivery */
    public void setM_Shipper_ID (int M_Shipper_ID)
    {
        if (M_Shipper_ID < 1) throw new IllegalArgumentException ("M_Shipper_ID is mandatory.");
        set_Value ("M_Shipper_ID", Integer.valueOf(M_Shipper_ID));
        
    }
    
    /** Get Freight Carrier.
    @return Method or manner of product delivery */
    public int getM_Shipper_ID() 
    {
        return get_ValueAsInt("M_Shipper_ID");
        
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
    
    /** Set Info Received.
    @param ReceivedInfo Information of the receipt of the package (acknowledgement) */
    public void setReceivedInfo (String ReceivedInfo)
    {
        set_Value ("ReceivedInfo", ReceivedInfo);
        
    }
    
    /** Get Info Received.
    @return Information of the receipt of the package (acknowledgement) */
    public String getReceivedInfo() 
    {
        return (String)get_Value("ReceivedInfo");
        
    }
    
    /** Set Ship Date.
    @param ShipDate Shipment Date/Time */
    public void setShipDate (Timestamp ShipDate)
    {
        set_Value ("ShipDate", ShipDate);
        
    }
    
    /** Get Ship Date.
    @return Shipment Date/Time */
    public Timestamp getShipDate() 
    {
        return (Timestamp)get_Value("ShipDate");
        
    }
    
    /** Set Tracking Info.
    @param TrackingInfo Tracking Info */
    public void setTrackingInfo (String TrackingInfo)
    {
        set_Value ("TrackingInfo", TrackingInfo);
        
    }
    
    /** Get Tracking Info.
    @return Tracking Info */
    public String getTrackingInfo() 
    {
        return (String)get_Value("TrackingInfo");
        
    }
    
    
}
