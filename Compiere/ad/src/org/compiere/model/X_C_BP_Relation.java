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
/** Generated Model for C_BP_Relation
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_BP_Relation.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_BP_Relation extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_BP_Relation_ID id
    @param trx transaction
    */
    public X_C_BP_Relation (Ctx ctx, int C_BP_Relation_ID, Trx trx)
    {
        super (ctx, C_BP_Relation_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_BP_Relation_ID == 0)
        {
            setC_BP_Relation_ID (0);
            setC_BPartnerRelation_ID (0);
            setC_BPartnerRelation_Location_ID (0);
            setC_BPartner_ID (0);
            setIsBillTo (false);
            setIsPayFrom (false);
            setIsRemitTo (false);
            setIsShipTo (false);	// N
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_BP_Relation (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=678 */
    public static final int Table_ID=678;
    
    /** TableName=C_BP_Relation */
    public static final String Table_Name="C_BP_Relation";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Partner Relation.
    @param C_BP_Relation_ID Business Partner Relation */
    public void setC_BP_Relation_ID (int C_BP_Relation_ID)
    {
        if (C_BP_Relation_ID < 1) throw new IllegalArgumentException ("C_BP_Relation_ID is mandatory.");
        set_ValueNoCheck ("C_BP_Relation_ID", Integer.valueOf(C_BP_Relation_ID));
        
    }
    
    /** Get Partner Relation.
    @return Business Partner Relation */
    public int getC_BP_Relation_ID() 
    {
        return get_ValueAsInt("C_BP_Relation_ID");
        
    }
    
    /** Set Related Partner.
    @param C_BPartnerRelation_ID Related Business Partner */
    public void setC_BPartnerRelation_ID (int C_BPartnerRelation_ID)
    {
        if (C_BPartnerRelation_ID < 1) throw new IllegalArgumentException ("C_BPartnerRelation_ID is mandatory.");
        set_Value ("C_BPartnerRelation_ID", Integer.valueOf(C_BPartnerRelation_ID));
        
    }
    
    /** Get Related Partner.
    @return Related Business Partner */
    public int getC_BPartnerRelation_ID() 
    {
        return get_ValueAsInt("C_BPartnerRelation_ID");
        
    }
    
    /** Set Related Partner Location.
    @param C_BPartnerRelation_Location_ID Location of the related Business Partner */
    public void setC_BPartnerRelation_Location_ID (int C_BPartnerRelation_Location_ID)
    {
        if (C_BPartnerRelation_Location_ID < 1) throw new IllegalArgumentException ("C_BPartnerRelation_Location_ID is mandatory.");
        set_Value ("C_BPartnerRelation_Location_ID", Integer.valueOf(C_BPartnerRelation_Location_ID));
        
    }
    
    /** Get Related Partner Location.
    @return Location of the related Business Partner */
    public int getC_BPartnerRelation_Location_ID() 
    {
        return get_ValueAsInt("C_BPartnerRelation_Location_ID");
        
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
        set_ValueNoCheck ("IsShipTo", Boolean.valueOf(IsShipTo));
        
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
    
    
}
