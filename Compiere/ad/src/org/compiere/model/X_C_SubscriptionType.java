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
/** Generated Model for C_SubscriptionType
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_SubscriptionType.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_SubscriptionType extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_SubscriptionType_ID id
    @param trx transaction
    */
    public X_C_SubscriptionType (Ctx ctx, int C_SubscriptionType_ID, Trx trx)
    {
        super (ctx, C_SubscriptionType_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_SubscriptionType_ID == 0)
        {
            setC_SubscriptionType_ID (0);
            setFrequency (0);
            setFrequencyType (null);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_SubscriptionType (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=668 */
    public static final int Table_ID=668;
    
    /** TableName=C_SubscriptionType */
    public static final String Table_Name="C_SubscriptionType";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Subscription Type.
    @param C_SubscriptionType_ID Type of subscription */
    public void setC_SubscriptionType_ID (int C_SubscriptionType_ID)
    {
        if (C_SubscriptionType_ID < 1) throw new IllegalArgumentException ("C_SubscriptionType_ID is mandatory.");
        set_ValueNoCheck ("C_SubscriptionType_ID", Integer.valueOf(C_SubscriptionType_ID));
        
    }
    
    /** Get Subscription Type.
    @return Type of subscription */
    public int getC_SubscriptionType_ID() 
    {
        return get_ValueAsInt("C_SubscriptionType_ID");
        
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
    
    /** Set Frequency.
    @param Frequency Frequency of events */
    public void setFrequency (int Frequency)
    {
        set_Value ("Frequency", Integer.valueOf(Frequency));
        
    }
    
    /** Get Frequency.
    @return Frequency of events */
    public int getFrequency() 
    {
        return get_ValueAsInt("Frequency");
        
    }
    
    /** Day = D */
    public static final String FREQUENCYTYPE_Day = X_Ref__Frequency_Type.DAY.getValue();
    /** Hour = H */
    public static final String FREQUENCYTYPE_Hour = X_Ref__Frequency_Type.HOUR.getValue();
    /** Minute = M */
    public static final String FREQUENCYTYPE_Minute = X_Ref__Frequency_Type.MINUTE.getValue();
    /** Set Frequency Type.
    @param FrequencyType Frequency of event */
    public void setFrequencyType (String FrequencyType)
    {
        if (FrequencyType == null) throw new IllegalArgumentException ("FrequencyType is mandatory");
        if (!X_Ref__Frequency_Type.isValid(FrequencyType))
        throw new IllegalArgumentException ("FrequencyType Invalid value - " + FrequencyType + " - Reference_ID=221 - D - H - M");
        set_Value ("FrequencyType", FrequencyType);
        
    }
    
    /** Get Frequency Type.
    @return Frequency of event */
    public String getFrequencyType() 
    {
        return (String)get_Value("FrequencyType");
        
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
