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
/** Generated Model for XX_VMA_Season
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMA_Season extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMA_Season_ID id
    @param trx transaction
    */
    public X_XX_VMA_Season (Ctx ctx, int XX_VMA_Season_ID, Trx trx)
    {
        super (ctx, XX_VMA_Season_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMA_Season_ID == 0)
        {
            setDocStatus (null);	// ??
            setEndDate (new Timestamp(System.currentTimeMillis()));
            setName (null);
            setStartDate (new Timestamp(System.currentTimeMillis()));
            setValue (null);
            setXX_VMA_Season_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMA_Season (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27629540960789L;
    /** Last Updated Timestamp 2012-09-11 21:37:24.0 */
    public static final long updatedMS = 1347415644000L;
    /** AD_Table_ID=1000422 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMA_Season");
        
    }
    ;
    
    /** TableName=XX_VMA_Season */
    public static final String Table_Name="XX_VMA_Season";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
        if (DocStatus == null) throw new IllegalArgumentException ("DocStatus is mandatory");
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
    
    /** Set Is Season Active.
    @param XX_VMA_IsSeasonActive Determines if the season is or isn't active */
    public void setXX_VMA_IsSeasonActive (boolean XX_VMA_IsSeasonActive)
    {
        set_Value ("XX_VMA_IsSeasonActive", Boolean.valueOf(XX_VMA_IsSeasonActive));
        
    }
    
    /** Get Is Season Active.
    @return Determines if the season is or isn't active */
    public boolean isXX_VMA_IsSeasonActive() 
    {
        return get_ValueAsBoolean("XX_VMA_IsSeasonActive");
        
    }
    
    /** Set Is Season Approved.
    @param XX_VMA_IsSeasonApproved Determines if the season have been approved. */
    public void setXX_VMA_IsSeasonApproved (boolean XX_VMA_IsSeasonApproved)
    {
        set_Value ("XX_VMA_IsSeasonApproved", Boolean.valueOf(XX_VMA_IsSeasonApproved));
        
    }
    
    /** Get Is Season Approved.
    @return Determines if the season have been approved. */
    public boolean isXX_VMA_IsSeasonApproved() 
    {
        return get_ValueAsBoolean("XX_VMA_IsSeasonApproved");
        
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
    
    /** Set Season.
    @param XX_VMA_Season_ID Identifier used for a Season. */
    public void setXX_VMA_Season_ID (int XX_VMA_Season_ID)
    {
        if (XX_VMA_Season_ID < 1) throw new IllegalArgumentException ("XX_VMA_Season_ID is mandatory.");
        set_ValueNoCheck ("XX_VMA_Season_ID", Integer.valueOf(XX_VMA_Season_ID));
        
    }
    
    /** Get Season.
    @return Identifier used for a Season. */
    public int getXX_VMA_Season_ID() 
    {
        return get_ValueAsInt("XX_VMA_Season_ID");
        
    }
    
    
}
