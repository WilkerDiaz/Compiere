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
/** Generated Model for S_ResourceType
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_S_ResourceType.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_S_ResourceType extends PO
{
    /** Standard Constructor
    @param ctx context
    @param S_ResourceType_ID id
    @param trx transaction
    */
    public X_S_ResourceType (Ctx ctx, int S_ResourceType_ID, Trx trx)
    {
        super (ctx, S_ResourceType_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (S_ResourceType_ID == 0)
        {
            setAllowUoMFractions (false);	// N
            setC_TaxCategory_ID (0);
            setC_UOM_ID (0);
            setIsDateSlot (false);
            setIsSingleAssignment (false);
            setIsTimeSlot (false);
            setM_Product_Category_ID (0);
            setName (null);
            setOnFriday (true);	// Y
            setOnMonday (true);	// Y
            setOnSaturday (false);
            setOnSunday (false);
            setOnThursday (true);	// Y
            setOnTuesday (true);	// Y
            setOnWednesday (true);	// Y
            setS_ResourceType_ID (0);
            setValue (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_S_ResourceType (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27518321896789L;
    /** Last Updated Timestamp 2009-03-04 11:56:20.0 */
    public static final long updatedMS = 1236196580000L;
    /** AD_Table_ID=480 */
    public static final int Table_ID=480;
    
    /** TableName=S_ResourceType */
    public static final String Table_Name="S_ResourceType";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Allow UoM Fractions.
    @param AllowUoMFractions Allow Unit of Measure Fractions */
    public void setAllowUoMFractions (boolean AllowUoMFractions)
    {
        set_Value ("AllowUoMFractions", Boolean.valueOf(AllowUoMFractions));
        
    }
    
    /** Get Allow UoM Fractions.
    @return Allow Unit of Measure Fractions */
    public boolean isAllowUoMFractions() 
    {
        return get_ValueAsBoolean("AllowUoMFractions");
        
    }
    
    /** Set Tax Category.
    @param C_TaxCategory_ID Tax Category */
    public void setC_TaxCategory_ID (int C_TaxCategory_ID)
    {
        if (C_TaxCategory_ID < 1) throw new IllegalArgumentException ("C_TaxCategory_ID is mandatory.");
        set_Value ("C_TaxCategory_ID", Integer.valueOf(C_TaxCategory_ID));
        
    }
    
    /** Get Tax Category.
    @return Tax Category */
    public int getC_TaxCategory_ID() 
    {
        return get_ValueAsInt("C_TaxCategory_ID");
        
    }
    
    /** Set UOM.
    @param C_UOM_ID Unit of Measure */
    public void setC_UOM_ID (int C_UOM_ID)
    {
        if (C_UOM_ID < 1) throw new IllegalArgumentException ("C_UOM_ID is mandatory.");
        set_Value ("C_UOM_ID", Integer.valueOf(C_UOM_ID));
        
    }
    
    /** Get UOM.
    @return Unit of Measure */
    public int getC_UOM_ID() 
    {
        return get_ValueAsInt("C_UOM_ID");
        
    }
    
    /** Set Chargeable Quantity.
    @param ChargeableQty Chargeable Quantity */
    public void setChargeableQty (int ChargeableQty)
    {
        set_Value ("ChargeableQty", Integer.valueOf(ChargeableQty));
        
    }
    
    /** Get Chargeable Quantity.
    @return Chargeable Quantity */
    public int getChargeableQty() 
    {
        return get_ValueAsInt("ChargeableQty");
        
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
    
    /** Set Day Slot.
    @param IsDateSlot Resource has day slot availability */
    public void setIsDateSlot (boolean IsDateSlot)
    {
        set_Value ("IsDateSlot", Boolean.valueOf(IsDateSlot));
        
    }
    
    /** Get Day Slot.
    @return Resource has day slot availability */
    public boolean isDateSlot() 
    {
        return get_ValueAsBoolean("IsDateSlot");
        
    }
    
    /** Set Single Assignment only.
    @param IsSingleAssignment Only one assignment at a time (no double-booking or overlapping) */
    public void setIsSingleAssignment (boolean IsSingleAssignment)
    {
        set_Value ("IsSingleAssignment", Boolean.valueOf(IsSingleAssignment));
        
    }
    
    /** Get Single Assignment only.
    @return Only one assignment at a time (no double-booking or overlapping) */
    public boolean isSingleAssignment() 
    {
        return get_ValueAsBoolean("IsSingleAssignment");
        
    }
    
    /** Set Time Slot.
    @param IsTimeSlot Resource has time slot availability */
    public void setIsTimeSlot (boolean IsTimeSlot)
    {
        set_Value ("IsTimeSlot", Boolean.valueOf(IsTimeSlot));
        
    }
    
    /** Get Time Slot.
    @return Resource has time slot availability */
    public boolean isTimeSlot() 
    {
        return get_ValueAsBoolean("IsTimeSlot");
        
    }
    
    /** Set Product Category.
    @param M_Product_Category_ID Category of a Product */
    public void setM_Product_Category_ID (int M_Product_Category_ID)
    {
        if (M_Product_Category_ID < 1) throw new IllegalArgumentException ("M_Product_Category_ID is mandatory.");
        set_Value ("M_Product_Category_ID", Integer.valueOf(M_Product_Category_ID));
        
    }
    
    /** Get Product Category.
    @return Category of a Product */
    public int getM_Product_Category_ID() 
    {
        return get_ValueAsInt("M_Product_Category_ID");
        
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
    
    /** Set Friday.
    @param OnFriday Available on Fridays */
    public void setOnFriday (boolean OnFriday)
    {
        set_Value ("OnFriday", Boolean.valueOf(OnFriday));
        
    }
    
    /** Get Friday.
    @return Available on Fridays */
    public boolean isOnFriday() 
    {
        return get_ValueAsBoolean("OnFriday");
        
    }
    
    /** Set Monday.
    @param OnMonday Available on Mondays */
    public void setOnMonday (boolean OnMonday)
    {
        set_Value ("OnMonday", Boolean.valueOf(OnMonday));
        
    }
    
    /** Get Monday.
    @return Available on Mondays */
    public boolean isOnMonday() 
    {
        return get_ValueAsBoolean("OnMonday");
        
    }
    
    /** Set Saturday.
    @param OnSaturday Available on Saturday */
    public void setOnSaturday (boolean OnSaturday)
    {
        set_Value ("OnSaturday", Boolean.valueOf(OnSaturday));
        
    }
    
    /** Get Saturday.
    @return Available on Saturday */
    public boolean isOnSaturday() 
    {
        return get_ValueAsBoolean("OnSaturday");
        
    }
    
    /** Set Sunday.
    @param OnSunday Available on Sundays */
    public void setOnSunday (boolean OnSunday)
    {
        set_Value ("OnSunday", Boolean.valueOf(OnSunday));
        
    }
    
    /** Get Sunday.
    @return Available on Sundays */
    public boolean isOnSunday() 
    {
        return get_ValueAsBoolean("OnSunday");
        
    }
    
    /** Set Thursday.
    @param OnThursday Available on Thursdays */
    public void setOnThursday (boolean OnThursday)
    {
        set_Value ("OnThursday", Boolean.valueOf(OnThursday));
        
    }
    
    /** Get Thursday.
    @return Available on Thursdays */
    public boolean isOnThursday() 
    {
        return get_ValueAsBoolean("OnThursday");
        
    }
    
    /** Set Tuesday.
    @param OnTuesday Available on Tuesdays */
    public void setOnTuesday (boolean OnTuesday)
    {
        set_Value ("OnTuesday", Boolean.valueOf(OnTuesday));
        
    }
    
    /** Get Tuesday.
    @return Available on Tuesdays */
    public boolean isOnTuesday() 
    {
        return get_ValueAsBoolean("OnTuesday");
        
    }
    
    /** Set Wednesday.
    @param OnWednesday Available on Wednesdays */
    public void setOnWednesday (boolean OnWednesday)
    {
        set_Value ("OnWednesday", Boolean.valueOf(OnWednesday));
        
    }
    
    /** Get Wednesday.
    @return Available on Wednesdays */
    public boolean isOnWednesday() 
    {
        return get_ValueAsBoolean("OnWednesday");
        
    }
    
    /** Equipment = E */
    public static final String RESOURCEGROUP_Equipment = X_Ref_M_Product_Resource_Group.EQUIPMENT.getValue();
    /** Other = O */
    public static final String RESOURCEGROUP_Other = X_Ref_M_Product_Resource_Group.OTHER.getValue();
    /** Person = P */
    public static final String RESOURCEGROUP_Person = X_Ref_M_Product_Resource_Group.PERSON.getValue();
    /** Set Resource Group.
    @param ResourceGroup First level grouping of resources into Person, Equipment or Other. */
    public void setResourceGroup (String ResourceGroup)
    {
        if (!X_Ref_M_Product_Resource_Group.isValid(ResourceGroup))
        throw new IllegalArgumentException ("ResourceGroup Invalid value - " + ResourceGroup + " - Reference_ID=498 - E - O - P");
        throw new IllegalArgumentException ("ResourceGroup is virtual column");
        
    }
    
    /** Get Resource Group.
    @return First level grouping of resources into Person, Equipment or Other. */
    public String getResourceGroup() 
    {
        return (String)get_Value("ResourceGroup");
        
    }
    
    /** Set Resource Type.
    @param S_ResourceType_ID Resource Type */
    public void setS_ResourceType_ID (int S_ResourceType_ID)
    {
        if (S_ResourceType_ID < 1) throw new IllegalArgumentException ("S_ResourceType_ID is mandatory.");
        set_ValueNoCheck ("S_ResourceType_ID", Integer.valueOf(S_ResourceType_ID));
        
    }
    
    /** Get Resource Type.
    @return Resource Type */
    public int getS_ResourceType_ID() 
    {
        return get_ValueAsInt("S_ResourceType_ID");
        
    }
    
    /** Set Slot End.
    @param TimeSlotEnd Time when timeslot ends */
    public void setTimeSlotEnd (Timestamp TimeSlotEnd)
    {
        set_Value ("TimeSlotEnd", TimeSlotEnd);
        
    }
    
    /** Get Slot End.
    @return Time when timeslot ends */
    public Timestamp getTimeSlotEnd() 
    {
        return (Timestamp)get_Value("TimeSlotEnd");
        
    }
    
    /** Set Slot Start.
    @param TimeSlotStart Time when timeslot starts */
    public void setTimeSlotStart (Timestamp TimeSlotStart)
    {
        set_Value ("TimeSlotStart", TimeSlotStart);
        
    }
    
    /** Get Slot Start.
    @return Time when timeslot starts */
    public Timestamp getTimeSlotStart() 
    {
        return (Timestamp)get_Value("TimeSlotStart");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    
}
