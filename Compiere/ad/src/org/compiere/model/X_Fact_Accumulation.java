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
/** Generated Model for Fact_Accumulation
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_Fact_Accumulation.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_Fact_Accumulation extends PO
{
    /** Standard Constructor
    @param ctx context
    @param Fact_Accumulation_ID id
    @param trx transaction
    */
    public X_Fact_Accumulation (Ctx ctx, int Fact_Accumulation_ID, Trx trx)
    {
        super (ctx, Fact_Accumulation_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (Fact_Accumulation_ID == 0)
        {
            setBalanceAccumulation (null);	// M
            setC_AcctSchema_ID (0);
            setFact_Accumulation_ID (0);
            setIsActivity (true);	// Y
            setIsBudget (true);	// Y
            setIsBusinessPartner (true);	// Y
            setIsCampaign (true);	// Y
            setIsDefault (false);	// N
            setIsLocationFrom (true);	// Y
            setIsLocationTo (true);	// Y
            setIsProduct (true);	// Y
            setIsProject (true);	// Y
            setIsSalesRegion (true);	// Y
            setIsUserElement1 (true);	// Y
            setIsUserElement2 (true);	// Y
            setIsUserList1 (true);	// Y
            setIsUserList2 (true);	// Y
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_Fact_Accumulation (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27536887460789L;
    /** Last Updated Timestamp 2009-10-05 10:02:24.0 */
    public static final long updatedMS = 1254762144000L;
    /** AD_Table_ID=1068 */
    public static final int Table_ID=1068;
    
    /** TableName=Fact_Accumulation */
    public static final String Table_Name="Fact_Accumulation";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Daily = D */
    public static final String BALANCEACCUMULATION_Daily = X_Ref_Fact_Accumulation_Type.DAILY.getValue();
    /** Calendar Month = M */
    public static final String BALANCEACCUMULATION_CalendarMonth = X_Ref_Fact_Accumulation_Type.CALENDAR_MONTH.getValue();
    /** Period of a Compiere Calendar = P */
    public static final String BALANCEACCUMULATION_PeriodOfACompiereCalendar = X_Ref_Fact_Accumulation_Type.PERIOD_OF_A_COMPIERE_CALENDAR.getValue();
    /** Calendar Week = W */
    public static final String BALANCEACCUMULATION_CalendarWeek = X_Ref_Fact_Accumulation_Type.CALENDAR_WEEK.getValue();
    /** Set Balance Accumulation.
    @param BalanceAccumulation Balance Accumulation Type */
    public void setBalanceAccumulation (String BalanceAccumulation)
    {
        if (BalanceAccumulation == null) throw new IllegalArgumentException ("BalanceAccumulation is mandatory");
        if (!X_Ref_Fact_Accumulation_Type.isValid(BalanceAccumulation))
        throw new IllegalArgumentException ("BalanceAccumulation Invalid value - " + BalanceAccumulation + " - Reference_ID=481 - D - M - P - W");
        set_Value ("BalanceAccumulation", BalanceAccumulation);
        
    }
    
    /** Get Balance Accumulation.
    @return Balance Accumulation Type */
    public String getBalanceAccumulation() 
    {
        return (String)get_Value("BalanceAccumulation");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getBalanceAccumulation()));
        
    }
    
    /** Set Accounting Schema.
    @param C_AcctSchema_ID Rules for accounting */
    public void setC_AcctSchema_ID (int C_AcctSchema_ID)
    {
        if (C_AcctSchema_ID < 1) throw new IllegalArgumentException ("C_AcctSchema_ID is mandatory.");
        set_Value ("C_AcctSchema_ID", Integer.valueOf(C_AcctSchema_ID));
        
    }
    
    /** Get Accounting Schema.
    @return Rules for accounting */
    public int getC_AcctSchema_ID() 
    {
        return get_ValueAsInt("C_AcctSchema_ID");
        
    }
    
    /** Set Calendar.
    @param C_Calendar_ID Accounting Calendar Name */
    public void setC_Calendar_ID (int C_Calendar_ID)
    {
        if (C_Calendar_ID <= 0) set_Value ("C_Calendar_ID", null);
        else
        set_Value ("C_Calendar_ID", Integer.valueOf(C_Calendar_ID));
        
    }
    
    /** Get Calendar.
    @return Accounting Calendar Name */
    public int getC_Calendar_ID() 
    {
        return get_ValueAsInt("C_Calendar_ID");
        
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
    
    /** Set Balance Aggregation.
    @param Fact_Accumulation_ID Balance Aggregation */
    public void setFact_Accumulation_ID (int Fact_Accumulation_ID)
    {
        if (Fact_Accumulation_ID < 1) throw new IllegalArgumentException ("Fact_Accumulation_ID is mandatory.");
        set_ValueNoCheck ("Fact_Accumulation_ID", Integer.valueOf(Fact_Accumulation_ID));
        
    }
    
    /** Get Balance Aggregation.
    @return Balance Aggregation */
    public int getFact_Accumulation_ID() 
    {
        return get_ValueAsInt("Fact_Accumulation_ID");
        
    }
    
    /** Set Activity.
    @param IsActivity Activity */
    public void setIsActivity (boolean IsActivity)
    {
        set_Value ("IsActivity", Boolean.valueOf(IsActivity));
        
    }
    
    /** Get Activity.
    @return Activity */
    public boolean isActivity() 
    {
        return get_ValueAsBoolean("IsActivity");
        
    }
    
    /** Set Budget.
    @param IsBudget Budget */
    public void setIsBudget (boolean IsBudget)
    {
        set_Value ("IsBudget", Boolean.valueOf(IsBudget));
        
    }
    
    /** Get Budget.
    @return Budget */
    public boolean isBudget() 
    {
        return get_ValueAsBoolean("IsBudget");
        
    }
    
    /** Set Business Partner.
    @param IsBusinessPartner Business Partner */
    public void setIsBusinessPartner (boolean IsBusinessPartner)
    {
        set_Value ("IsBusinessPartner", Boolean.valueOf(IsBusinessPartner));
        
    }
    
    /** Get Business Partner.
    @return Business Partner */
    public boolean isBusinessPartner() 
    {
        return get_ValueAsBoolean("IsBusinessPartner");
        
    }
    
    /** Set Campaign.
    @param IsCampaign Campaign */
    public void setIsCampaign (boolean IsCampaign)
    {
        set_Value ("IsCampaign", Boolean.valueOf(IsCampaign));
        
    }
    
    /** Get Campaign.
    @return Campaign */
    public boolean isCampaign() 
    {
        return get_ValueAsBoolean("IsCampaign");
        
    }
    
    /** Set Default.
    @param IsDefault Default value */
    public void setIsDefault (boolean IsDefault)
    {
        set_Value ("IsDefault", Boolean.valueOf(IsDefault));
        
    }
    
    /** Get Default.
    @return Default value */
    public boolean isDefault() 
    {
        return get_ValueAsBoolean("IsDefault");
        
    }
    
    /** Set Location From.
    @param IsLocationFrom Location From */
    public void setIsLocationFrom (boolean IsLocationFrom)
    {
        set_Value ("IsLocationFrom", Boolean.valueOf(IsLocationFrom));
        
    }
    
    /** Get Location From.
    @return Location From */
    public boolean isLocationFrom() 
    {
        return get_ValueAsBoolean("IsLocationFrom");
        
    }
    
    /** Set Location To.
    @param IsLocationTo Location To */
    public void setIsLocationTo (boolean IsLocationTo)
    {
        set_Value ("IsLocationTo", Boolean.valueOf(IsLocationTo));
        
    }
    
    /** Get Location To.
    @return Location To */
    public boolean isLocationTo() 
    {
        return get_ValueAsBoolean("IsLocationTo");
        
    }
    
    /** Set Product.
    @param IsProduct Product */
    public void setIsProduct (boolean IsProduct)
    {
        set_Value ("IsProduct", Boolean.valueOf(IsProduct));
        
    }
    
    /** Get Product.
    @return Product */
    public boolean isProduct() 
    {
        return get_ValueAsBoolean("IsProduct");
        
    }
    
    /** Set Project.
    @param IsProject Project */
    public void setIsProject (boolean IsProject)
    {
        set_Value ("IsProject", Boolean.valueOf(IsProject));
        
    }
    
    /** Get Project.
    @return Project */
    public boolean isProject() 
    {
        return get_ValueAsBoolean("IsProject");
        
    }
    
    /** Set Sales Region.
    @param IsSalesRegion Sales Region */
    public void setIsSalesRegion (boolean IsSalesRegion)
    {
        set_Value ("IsSalesRegion", Boolean.valueOf(IsSalesRegion));
        
    }
    
    /** Get Sales Region.
    @return Sales Region */
    public boolean isSalesRegion() 
    {
        return get_ValueAsBoolean("IsSalesRegion");
        
    }
    
    /** Set User Element 1.
    @param IsUserElement1 User Element 1 */
    public void setIsUserElement1 (boolean IsUserElement1)
    {
        set_Value ("IsUserElement1", Boolean.valueOf(IsUserElement1));
        
    }
    
    /** Get User Element 1.
    @return User Element 1 */
    public boolean isUserElement1() 
    {
        return get_ValueAsBoolean("IsUserElement1");
        
    }
    
    /** Set User Element 2.
    @param IsUserElement2 User Element 2 */
    public void setIsUserElement2 (boolean IsUserElement2)
    {
        set_Value ("IsUserElement2", Boolean.valueOf(IsUserElement2));
        
    }
    
    /** Get User Element 2.
    @return User Element 2 */
    public boolean isUserElement2() 
    {
        return get_ValueAsBoolean("IsUserElement2");
        
    }
    
    /** Set User List 1.
    @param IsUserList1 User List 1 */
    public void setIsUserList1 (boolean IsUserList1)
    {
        set_Value ("IsUserList1", Boolean.valueOf(IsUserList1));
        
    }
    
    /** Get User List 1.
    @return User List 1 */
    public boolean isUserList1() 
    {
        return get_ValueAsBoolean("IsUserList1");
        
    }
    
    /** Set User List 2.
    @param IsUserList2 User List 2 */
    public void setIsUserList2 (boolean IsUserList2)
    {
        set_Value ("IsUserList2", Boolean.valueOf(IsUserList2));
        
    }
    
    /** Get User List 2.
    @return User List 2 */
    public boolean isUserList2() 
    {
        return get_ValueAsBoolean("IsUserList2");
        
    }
    
    
}
