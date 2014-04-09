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
package compiere.model.dynamic;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VMA_MarketingActivity
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMA_MarketingActivity extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMA_MarketingActivity_ID id
    @param trx transaction
    */
    public X_XX_VMA_MarketingActivity (Ctx ctx, int XX_VMA_MarketingActivity_ID, Trx trx)
    {
        super (ctx, XX_VMA_MarketingActivity_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMA_MarketingActivity_ID == 0)
        {
            setC_Campaign_ID (0);
            setCosts (Env.ZERO);
            setEndDate (new Timestamp(System.currentTimeMillis()));
            setIsApproved (false);	// N
            setName (null);
            setStartDate (new Timestamp(System.currentTimeMillis()));
            setValue (null);
            setXX_VMA_ActivityType (null);
            setXX_VMA_MarketingActivity_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMA_MarketingActivity (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27629958040789L;
    /** Last Updated Timestamp 2012-09-16 17:28:44.0 */
    public static final long updatedMS = 1347832724000L;
    /** AD_Table_ID=1000415 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMA_MarketingActivity");
        
    }
    ;
    
    /** TableName=XX_VMA_MarketingActivity */
    public static final String Table_Name="XX_VMA_MarketingActivity";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Campaign.
    @param C_Campaign_ID Marketing Campaign */
    public void setC_Campaign_ID (int C_Campaign_ID)
    {
        if (C_Campaign_ID < 1) throw new IllegalArgumentException ("C_Campaign_ID is mandatory.");
        set_ValueNoCheck ("C_Campaign_ID", Integer.valueOf(C_Campaign_ID));
        
    }
    
    /** Get Campaign.
    @return Marketing Campaign */
    public int getC_Campaign_ID() 
    {
        return get_ValueAsInt("C_Campaign_ID");
        
    }
    
    /** Set Costs.
    @param Costs Costs in accounting currency */
    public void setCosts (java.math.BigDecimal Costs)
    {
        if (Costs == null) throw new IllegalArgumentException ("Costs is mandatory.");
        set_Value ("Costs", Costs);
        
    }
    
    /** Get Costs.
    @return Costs in accounting currency */
    public java.math.BigDecimal getCosts() 
    {
        return get_ValueAsBigDecimal("Costs");
        
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
    
    /** Unknown = ?? */
    public static final String DOCSTATUS_Unknown = X_Ref__Document_Status.UNKNOWN.getValue();
    /** Approved = AP */
    public static final String DOCSTATUS_Approved = X_Ref__Document_Status.APPROVED.getValue();
    /** Closed = CL */
    public static final String DOCSTATUS_Closed = X_Ref__Document_Status.CLOSED.getValue();
    /** Completed = CO */
    public static final String DOCSTATUS_Completed = X_Ref__Document_Status.COMPLETED.getValue();
    /** Drafted = DR */
    public static final String DOCSTATUS_Drafted = X_Ref__Document_Status.DRAFTED.getValue();
    /** Invalid = IN */
    public static final String DOCSTATUS_Invalid = X_Ref__Document_Status.INVALID.getValue();
    /** In Progress = IP */
    public static final String DOCSTATUS_InProgress = X_Ref__Document_Status.IN_PROGRESS.getValue();
    /** Not Approved = NA */
    public static final String DOCSTATUS_NotApproved = X_Ref__Document_Status.NOT_APPROVED.getValue();
    /** Reversed = RE */
    public static final String DOCSTATUS_Reversed = X_Ref__Document_Status.REVERSED.getValue();
    /** Voided = VO */
    public static final String DOCSTATUS_Voided = X_Ref__Document_Status.VOIDED.getValue();
    /** Waiting Confirmation = WC */
    public static final String DOCSTATUS_WaitingConfirmation = X_Ref__Document_Status.WAITING_CONFIRMATION.getValue();
    /** Waiting Payment = WP */
    public static final String DOCSTATUS_WaitingPayment = X_Ref__Document_Status.WAITING_PAYMENT.getValue();
    /** Set Document Status.
    @param DocStatus The current status of the document */
    public void setDocStatus (String DocStatus)
    {
        if (!X_Ref__Document_Status.isValid(DocStatus))
        throw new IllegalArgumentException ("DocStatus Invalid value - " + DocStatus + " - Reference_ID=131 - ?? - AP - CL - CO - DR - IN - IP - NA - RE - VO - WC - WP");
        set_Value ("DocStatus", DocStatus);
        
    }
    
    /** Get Document Status.
    @return The current status of the document */
    public String getDocStatus() 
    {
        return (String)get_Value("DocStatus");
        
    }
    
    /** Set End Date.
    @param EndDate Last effective date (inclusive) */
    public void setEndDate (Timestamp EndDate)
    {
        if (EndDate == null) throw new IllegalArgumentException ("EndDate is mandatory.");
        set_Value ("EndDate", EndDate);
        
    }
    
    /** Get End Date.
    @return Last effective date (inclusive) */
    public Timestamp getEndDate() 
    {
        return (Timestamp)get_Value("EndDate");
        
    }
    
    /** Set Approved.
    @param IsApproved Indicates if this document requires approval */
    public void setIsApproved (boolean IsApproved)
    {
        set_Value ("IsApproved", Boolean.valueOf(IsApproved));
        
    }
    
    /** Get Approved.
    @return Indicates if this document requires approval */
    public boolean isApproved() 
    {
        return get_ValueAsBoolean("IsApproved");
        
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
    
    /** Set Start Date.
    @param StartDate First effective day (inclusive) */
    public void setStartDate (Timestamp StartDate)
    {
        if (StartDate == null) throw new IllegalArgumentException ("StartDate is mandatory.");
        set_Value ("StartDate", StartDate);
        
    }
    
    /** Get Start Date.
    @return First effective day (inclusive) */
    public Timestamp getStartDate() 
    {
        return (Timestamp)get_Value("StartDate");
        
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
    
    /** A - Advertising = A */
    public static final String XX_VMA_ACTIVITYTYPE_A_Advertising = X_Ref_XX_VMA_ActivityTypeR.A__ADVERTISING.getValue();
    /** B - Brochure = B */
    public static final String XX_VMA_ACTIVITYTYPE_B_Brochure = X_Ref_XX_VMA_ActivityTypeR.B__BROCHURE.getValue();
    /** O - Other = O */
    public static final String XX_VMA_ACTIVITYTYPE_O_Other = X_Ref_XX_VMA_ActivityTypeR.O__OTHER.getValue();
    /** Set Activity Type.
    @param XX_VMA_ActivityType Type of the marketing activity. */
    public void setXX_VMA_ActivityType (String XX_VMA_ActivityType)
    {
        if (XX_VMA_ActivityType == null) throw new IllegalArgumentException ("XX_VMA_ActivityType is mandatory");
        if (!X_Ref_XX_VMA_ActivityTypeR.isValid(XX_VMA_ActivityType))
        throw new IllegalArgumentException ("XX_VMA_ActivityType Invalid value - " + XX_VMA_ActivityType + " - Reference_ID=1000318 - A - B - O");
        set_Value ("XX_VMA_ActivityType", XX_VMA_ActivityType);
        
    }
    
    /** Get Activity Type.
    @return Type of the marketing activity. */
    public String getXX_VMA_ActivityType() 
    {
        return (String)get_Value("XX_VMA_ActivityType");
        
    }
    
    /** Set XX_VMA_AddMedia.
    @param XX_VMA_AddMedia add publication media */
    public void setXX_VMA_AddMedia (String XX_VMA_AddMedia)
    {
        set_Value ("XX_VMA_AddMedia", XX_VMA_AddMedia);
        
    }
    
    /** Get XX_VMA_AddMedia.
    @return add publication media */
    public String getXX_VMA_AddMedia() 
    {
        return (String)get_Value("XX_VMA_AddMedia");
        
    }
    
    /** Set Final Approvement.
    @param XX_VMA_AprobFin Final Approvement of a Marketing Activity has been sent. */
    public void setXX_VMA_AprobFin (boolean XX_VMA_AprobFin)
    {
        set_Value ("XX_VMA_AprobFin", Boolean.valueOf(XX_VMA_AprobFin));
        
    }
    
    /** Get Final Approvement.
    @return Final Approvement of a Marketing Activity has been sent. */
    public boolean isXX_VMA_AprobFin() 
    {
        return get_ValueAsBoolean("XX_VMA_AprobFin");
        
    }
    
    /** Set Initial Approvement.
    @param XX_VMA_AprobIni The initial approvement of a Marketing Acitivity has been sent  */
    public void setXX_VMA_AprobIni (boolean XX_VMA_AprobIni)
    {
        set_Value ("XX_VMA_AprobIni", Boolean.valueOf(XX_VMA_AprobIni));
        
    }
    
    /** Get Initial Approvement.
    @return The initial approvement of a Marketing Acitivity has been sent  */
    public boolean isXX_VMA_AprobIni() 
    {
        return get_ValueAsBoolean("XX_VMA_AprobIni");
        
    }
    
    /** Set Brochure.
    @param XX_VMA_Brochure_ID Identifier of the Brochure. */
    public void setXX_VMA_Brochure_ID (int XX_VMA_Brochure_ID)
    {
        if (XX_VMA_Brochure_ID <= 0) set_Value ("XX_VMA_Brochure_ID", null);
        else
        set_Value ("XX_VMA_Brochure_ID", Integer.valueOf(XX_VMA_Brochure_ID));
        
    }
    
    /** Get Brochure.
    @return Identifier of the Brochure. */
    public int getXX_VMA_Brochure_ID() 
    {
        return get_ValueAsInt("XX_VMA_Brochure_ID");
        
    }
    
    /** Set XX_VMA_DropMedia.
    @param XX_VMA_DropMedia Drop a media asociated with a marketing activity */
    public void setXX_VMA_DropMedia (String XX_VMA_DropMedia)
    {
        set_Value ("XX_VMA_DropMedia", XX_VMA_DropMedia);
        
    }
    
    /** Get XX_VMA_DropMedia.
    @return Drop a media asociated with a marketing activity */
    public String getXX_VMA_DropMedia() 
    {
        return (String)get_Value("XX_VMA_DropMedia");
        
    }
    
    /** Set Is Activity Active.
    @param XX_VMA_IsActivityActive The activity is active or not. */
    public void setXX_VMA_IsActivityActive (boolean XX_VMA_IsActivityActive)
    {
        set_Value ("XX_VMA_IsActivityActive", Boolean.valueOf(XX_VMA_IsActivityActive));
        
    }
    
    /** Get Is Activity Active.
    @return The activity is active or not. */
    public boolean isXX_VMA_IsActivityActive() 
    {
        return get_ValueAsBoolean("XX_VMA_IsActivityActive");
        
    }
    
    /** Set Is Activity Approved.
    @param XX_VMA_IsActivityApproved The activity is finally approved */
    public void setXX_VMA_IsActivityApproved (boolean XX_VMA_IsActivityApproved)
    {
        set_Value ("XX_VMA_IsActivityApproved", Boolean.valueOf(XX_VMA_IsActivityApproved));
        
    }
    
    /** Get Is Activity Approved.
    @return The activity is finally approved */
    public boolean isXX_VMA_IsActivityApproved() 
    {
        return get_ValueAsBoolean("XX_VMA_IsActivityApproved");
        
    }
    
    /** Set Marketing Activity.
    @param XX_VMA_MarketingActivity_ID Marketing Activity implemented in the Company by Marketing Management. */
    public void setXX_VMA_MarketingActivity_ID (int XX_VMA_MarketingActivity_ID)
    {
        if (XX_VMA_MarketingActivity_ID < 1) throw new IllegalArgumentException ("XX_VMA_MarketingActivity_ID is mandatory.");
        set_ValueNoCheck ("XX_VMA_MarketingActivity_ID", Integer.valueOf(XX_VMA_MarketingActivity_ID));
        
    }
    
    /** Get Marketing Activity.
    @return Marketing Activity implemented in the Company by Marketing Management. */
    public int getXX_VMA_MarketingActivity_ID() 
    {
        return get_ValueAsInt("XX_VMA_MarketingActivity_ID");
        
    }
    
    /** Set Media Type.
    @param XX_VMA_MediaType_ID Identifier of a Media type  */
    public void setXX_VMA_MediaType_ID (int XX_VMA_MediaType_ID)
    {
        if (XX_VMA_MediaType_ID <= 0) set_Value ("XX_VMA_MediaType_ID", null);
        else
        set_Value ("XX_VMA_MediaType_ID", Integer.valueOf(XX_VMA_MediaType_ID));
        
    }
    
    /** Get Media Type.
    @return Identifier of a Media type  */
    public int getXX_VMA_MediaType_ID() 
    {
        return get_ValueAsInt("XX_VMA_MediaType_ID");
        
    }
    
    /** Set Not Approved.
    @param XX_VMA_NotApproved Disapproved the marketing activity */
    public void setXX_VMA_NotApproved (boolean XX_VMA_NotApproved)
    {
        set_Value ("XX_VMA_NotApproved", Boolean.valueOf(XX_VMA_NotApproved));
        
    }
    
    /** Get Not Approved.
    @return Disapproved the marketing activity */
    public boolean isXX_VMA_NotApproved() 
    {
        return get_ValueAsBoolean("XX_VMA_NotApproved");
        
    }
    
    /** Set Final Approvement User.
    @param XX_VMA_UserAprobFin User who did the initial approvement of the Marketing Activity  */
    public void setXX_VMA_UserAprobFin (int XX_VMA_UserAprobFin)
    {
        set_Value ("XX_VMA_UserAprobFin", Integer.valueOf(XX_VMA_UserAprobFin));
        
    }
    
    /** Get Final Approvement User.
    @return User who did the initial approvement of the Marketing Activity  */
    public int getXX_VMA_UserAprobFin() 
    {
        return get_ValueAsInt("XX_VMA_UserAprobFin");
        
    }
    
    /** Set Initial Approvement User.
    @param XX_VMA_UserAprobIni User who did the initial approvement of the marketing activity */
    public void setXX_VMA_UserAprobIni (int XX_VMA_UserAprobIni)
    {
        set_Value ("XX_VMA_UserAprobIni", Integer.valueOf(XX_VMA_UserAprobIni));
        
    }
    
    /** Get Initial Approvement User.
    @return User who did the initial approvement of the marketing activity */
    public int getXX_VMA_UserAprobIni() 
    {
        return get_ValueAsInt("XX_VMA_UserAprobIni");
        
    }
    
    
}
