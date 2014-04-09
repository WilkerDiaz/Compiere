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
/** Generated Model for C_BPartner_Location
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_BPartner_Location.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_BPartner_Location extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_BPartner_Location_ID id
    @param trx transaction
    */
    public X_C_BPartner_Location (Ctx ctx, int C_BPartner_Location_ID, Trx trx)
    {
        super (ctx, C_BPartner_Location_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_BPartner_Location_ID == 0)
        {
            setC_BPartner_ID (0);
            setC_BPartner_Location_ID (0);
            setC_Location_ID (0);
            setIsBillTo (true);	// Y
            setIsPayFrom (true);	// Y
            setIsRemitTo (true);	// Y
            setIsShipTo (true);	// Y
            setName (null);	// .
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_BPartner_Location (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=293 */
    public static final int Table_ID=293;
    
    /** TableName=C_BPartner_Location */
    public static final String Table_Name="C_BPartner_Location";
    
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
        set_ValueNoCheck ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
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
        set_ValueNoCheck ("C_BPartner_Location_ID", Integer.valueOf(C_BPartner_Location_ID));
        
    }
    
    /** Get Partner Location.
    @return Identifies the (ship to) address for this Business Partner */
    public int getC_BPartner_Location_ID() 
    {
        return get_ValueAsInt("C_BPartner_Location_ID");
        
    }
    
    /** Set Address.
    @param C_Location_ID Location or Address */
    public void setC_Location_ID (int C_Location_ID)
    {
        if (C_Location_ID < 1) throw new IllegalArgumentException ("C_Location_ID is mandatory.");
        set_Value ("C_Location_ID", Integer.valueOf(C_Location_ID));
        
    }
    
    /** Get Address.
    @return Location or Address */
    public int getC_Location_ID() 
    {
        return get_ValueAsInt("C_Location_ID");
        
    }
    
    /** Set Sales Region.
    @param C_SalesRegion_ID Sales coverage region */
    public void setC_SalesRegion_ID (int C_SalesRegion_ID)
    {
        if (C_SalesRegion_ID <= 0) set_Value ("C_SalesRegion_ID", null);
        else
        set_Value ("C_SalesRegion_ID", Integer.valueOf(C_SalesRegion_ID));
        
    }
    
    /** Get Sales Region.
    @return Sales coverage region */
    public int getC_SalesRegion_ID() 
    {
        return get_ValueAsInt("C_SalesRegion_ID");
        
    }
    
    /** Set Fax.
    @param Fax Facsimile number */
    public void setFax (String Fax)
    {
        set_Value ("Fax", Fax);
        
    }
    
    /** Get Fax.
    @return Facsimile number */
    public String getFax() 
    {
        return (String)get_Value("Fax");
        
    }
    
    /** Set ISDN.
    @param ISDN ISDN or modem line */
    public void setISDN (String ISDN)
    {
        set_Value ("ISDN", ISDN);
        
    }
    
    /** Get ISDN.
    @return ISDN or modem line */
    public String getISDN() 
    {
        return (String)get_Value("ISDN");
        
    }
    
    /** Set Invoice Address.
    @param IsBillTo Business Partner Invoice/Bill Address */
    public void setIsBillTo (boolean IsBillTo)
    {
        set_Value ("IsBillTo", Boolean.valueOf(IsBillTo));
        
    }
    
    /** Get Invoice Address.
    @return Business Partner Invoice/Bill Address */
    public boolean isBillTo() 
    {
        return get_ValueAsBoolean("IsBillTo");
        
    }
    
    /** Set Pay-From Address.
    @param IsPayFrom Business Partner pays from that address and we'll send dunning letters there */
    public void setIsPayFrom (boolean IsPayFrom)
    {
        set_Value ("IsPayFrom", Boolean.valueOf(IsPayFrom));
        
    }
    
    /** Get Pay-From Address.
    @return Business Partner pays from that address and we'll send dunning letters there */
    public boolean isPayFrom() 
    {
        return get_ValueAsBoolean("IsPayFrom");
        
    }
    
    /** Set Remit-To Address.
    @param IsRemitTo Business Partner payment address */
    public void setIsRemitTo (boolean IsRemitTo)
    {
        set_Value ("IsRemitTo", Boolean.valueOf(IsRemitTo));
        
    }
    
    /** Get Remit-To Address.
    @return Business Partner payment address */
    public boolean isRemitTo() 
    {
        return get_ValueAsBoolean("IsRemitTo");
        
    }
    
    /** Set Ship Address.
    @param IsShipTo Business Partner Shipment Address */
    public void setIsShipTo (boolean IsShipTo)
    {
        set_Value ("IsShipTo", Boolean.valueOf(IsShipTo));
        
    }
    
    /** Get Ship Address.
    @return Business Partner Shipment Address */
    public boolean isShipTo() 
    {
        return get_ValueAsBoolean("IsShipTo");
        
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
    
    /** Set Phone.
    @param Phone Identifies a telephone number */
    public void setPhone (String Phone)
    {
        set_Value ("Phone", Phone);
        
    }
    
    /** Get Phone.
    @return Identifies a telephone number */
    public String getPhone() 
    {
        return (String)get_Value("Phone");
        
    }
    
    /** Set 2nd Phone.
    @param Phone2 Identifies an alternate telephone number. */
    public void setPhone2 (String Phone2)
    {
        set_Value ("Phone2", Phone2);
        
    }
    
    /** Get 2nd Phone.
    @return Identifies an alternate telephone number. */
    public String getPhone2() 
    {
        return (String)get_Value("Phone2");
        
    }
    
    
}
