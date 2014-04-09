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
/** Generated Model for TIRE_Storage
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_TIRE_Storage.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_TIRE_Storage extends PO
{
    /** Standard Constructor
    @param ctx context
    @param TIRE_Storage_ID id
    @param trx transaction
    */
    public X_TIRE_Storage (Ctx ctx, int TIRE_Storage_ID, Trx trx)
    {
        super (ctx, TIRE_Storage_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (TIRE_Storage_ID == 0)
        {
            setDateReceived (new Timestamp(System.currentTimeMillis()));	// @#Date@
            setIsReturned (false);
            setIsStored (false);
            setName (null);
            setTIRE_Storage_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_TIRE_Storage (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=384 */
    public static final int Table_ID=384;
    
    /** TableName=TIRE_Storage */
    public static final String Table_Name="TIRE_Storage";
    
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
    
    /** Set Date received.
    @param DateReceived Date a product was received */
    public void setDateReceived (Timestamp DateReceived)
    {
        if (DateReceived == null) throw new IllegalArgumentException ("DateReceived is mandatory.");
        set_Value ("DateReceived", DateReceived);
        
    }
    
    /** Get Date received.
    @return Date a product was received */
    public Timestamp getDateReceived() 
    {
        return (Timestamp)get_Value("DateReceived");
        
    }
    
    /** Set Date returned.
    @param DateReturned Date a product was returned */
    public void setDateReturned (Timestamp DateReturned)
    {
        set_Value ("DateReturned", DateReturned);
        
    }
    
    /** Get Date returned.
    @return Date a product was returned */
    public Timestamp getDateReturned() 
    {
        return (Timestamp)get_Value("DateReturned");
        
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
    
    /** Set Returned.
    @param IsReturned Returned */
    public void setIsReturned (boolean IsReturned)
    {
        set_Value ("IsReturned", Boolean.valueOf(IsReturned));
        
    }
    
    /** Get Returned.
    @return Returned */
    public boolean isReturned() 
    {
        return get_ValueAsBoolean("IsReturned");
        
    }
    
    /** Set Moved to storage.
    @param IsStored Moved to storage */
    public void setIsStored (boolean IsStored)
    {
        set_Value ("IsStored", Boolean.valueOf(IsStored));
        
    }
    
    /** Get Moved to storage.
    @return Moved to storage */
    public boolean isStored() 
    {
        return get_ValueAsBoolean("IsStored");
        
    }
    
    /** Set Locator.
    @param M_Locator_ID Warehouse Locator */
    public void setM_Locator_ID (int M_Locator_ID)
    {
        if (M_Locator_ID <= 0) set_Value ("M_Locator_ID", null);
        else
        set_Value ("M_Locator_ID", Integer.valueOf(M_Locator_ID));
        
    }
    
    /** Get Locator.
    @return Warehouse Locator */
    public int getM_Locator_ID() 
    {
        return get_ValueAsInt("M_Locator_ID");
        
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
    
    /** Set Registration.
    @param Registration Vehicle registration */
    public void setRegistration (String Registration)
    {
        set_Value ("Registration", Registration);
        
    }
    
    /** Get Registration.
    @return Vehicle registration */
    public String getRegistration() 
    {
        return (String)get_Value("Registration");
        
    }
    
    /** Set Remark.
    @param Remark Remark */
    public void setRemark (String Remark)
    {
        set_Value ("Remark", Remark);
        
    }
    
    /** Get Remark.
    @return Remark */
    public String getRemark() 
    {
        return (String)get_Value("Remark");
        
    }
    
    /** Set Rim.
    @param Rim Stored rim */
    public void setRim (String Rim)
    {
        set_Value ("Rim", Rim);
        
    }
    
    /** Get Rim.
    @return Stored rim */
    public String getRim() 
    {
        return (String)get_Value("Rim");
        
    }
    
    /** Set Rim Back.
    @param Rim_B Rim Back */
    public void setRim_B (String Rim_B)
    {
        set_Value ("Rim_B", Rim_B);
        
    }
    
    /** Get Rim Back.
    @return Rim Back */
    public String getRim_B() 
    {
        return (String)get_Value("Rim_B");
        
    }
    
    /** Set Tire Storage.
    @param TIRE_Storage_ID Tire Storage */
    public void setTIRE_Storage_ID (int TIRE_Storage_ID)
    {
        if (TIRE_Storage_ID < 1) throw new IllegalArgumentException ("TIRE_Storage_ID is mandatory.");
        set_ValueNoCheck ("TIRE_Storage_ID", Integer.valueOf(TIRE_Storage_ID));
        
    }
    
    /** Get Tire Storage.
    @return Tire Storage */
    public int getTIRE_Storage_ID() 
    {
        return get_ValueAsInt("TIRE_Storage_ID");
        
    }
    
    /** Set Tire Quality.
    @param TireQuality Tire Quality */
    public void setTireQuality (String TireQuality)
    {
        set_Value ("TireQuality", TireQuality);
        
    }
    
    /** Get Tire Quality.
    @return Tire Quality */
    public String getTireQuality() 
    {
        return (String)get_Value("TireQuality");
        
    }
    
    /** Set Tire Quality Back.
    @param TireQuality_B Tire Quality Back */
    public void setTireQuality_B (String TireQuality_B)
    {
        set_Value ("TireQuality_B", TireQuality_B);
        
    }
    
    /** Get Tire Quality Back.
    @return Tire Quality Back */
    public String getTireQuality_B() 
    {
        return (String)get_Value("TireQuality_B");
        
    }
    
    /** Set Tire size (L/R).
    @param TireSize Tire size (L/R) */
    public void setTireSize (String TireSize)
    {
        set_Value ("TireSize", TireSize);
        
    }
    
    /** Get Tire size (L/R).
    @return Tire size (L/R) */
    public String getTireSize() 
    {
        return (String)get_Value("TireSize");
        
    }
    
    /** Set Tire size back.
    @param TireSize_B Tire size back */
    public void setTireSize_B (String TireSize_B)
    {
        set_Value ("TireSize_B", TireSize_B);
        
    }
    
    /** Get Tire size back.
    @return Tire size back */
    public String getTireSize_B() 
    {
        return (String)get_Value("TireSize_B");
        
    }
    
    /** Set Tire type.
    @param TireType Tire type */
    public void setTireType (String TireType)
    {
        set_Value ("TireType", TireType);
        
    }
    
    /** Get Tire type.
    @return Tire type */
    public String getTireType() 
    {
        return (String)get_Value("TireType");
        
    }
    
    /** Set Tire type back.
    @param TireType_B Tire type back */
    public void setTireType_B (String TireType_B)
    {
        set_Value ("TireType_B", TireType_B);
        
    }
    
    /** Get Tire type back.
    @return Tire type back */
    public String getTireType_B() 
    {
        return (String)get_Value("TireType_B");
        
    }
    
    /** Set Vehicle.
    @param Vehicle Vehicle */
    public void setVehicle (String Vehicle)
    {
        set_Value ("Vehicle", Vehicle);
        
    }
    
    /** Get Vehicle.
    @return Vehicle */
    public String getVehicle() 
    {
        return (String)get_Value("Vehicle");
        
    }
    
    
}
