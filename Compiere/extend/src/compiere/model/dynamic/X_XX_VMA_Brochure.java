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
/** Generated Model for XX_VMA_Brochure
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMA_Brochure extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMA_Brochure_ID id
    @param trx transaction
    */
    public X_XX_VMA_Brochure (Ctx ctx, int XX_VMA_Brochure_ID, Trx trx)
    {
        super (ctx, XX_VMA_Brochure_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMA_Brochure_ID == 0)
        {
            setName (null);
            setXX_VMA_Assigned (false);	// N
            setXX_VMA_Brochure_ID (0);
            setXX_VMA_Expired (false);	// N
            setXX_VMA_Topic (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMA_Brochure (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27629541025789L;
    /** Last Updated Timestamp 2012-09-11 21:38:29.0 */
    public static final long updatedMS = 1347415709000L;
    /** AD_Table_ID=1000412 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMA_Brochure");
        
    }
    ;
    
    /** TableName=XX_VMA_Brochure */
    public static final String Table_Name="XX_VMA_Brochure";
    
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
    
    /** Set Registered.
    @param IsRegistered The application is registered. */
    public void setIsRegistered (boolean IsRegistered)
    {
        set_Value ("IsRegistered", Boolean.valueOf(IsRegistered));
        
    }
    
    /** Get Registered.
    @return The application is registered. */
    public boolean isRegistered() 
    {
        return get_ValueAsBoolean("IsRegistered");
        
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
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Brochure Report.
    @param XX_ReportBrochureBirt Brochure Report */
    public void setXX_ReportBrochureBirt (String XX_ReportBrochureBirt)
    {
        set_Value ("XX_ReportBrochureBirt", XX_ReportBrochureBirt);
        
    }
    
    /** Get Brochure Report.
    @return Brochure Report */
    public String getXX_ReportBrochureBirt() 
    {
        return (String)get_Value("XX_ReportBrochureBirt");
        
    }
    
    /** Set Assigned.
    @param XX_VMA_Assigned The element is assigned. */
    public void setXX_VMA_Assigned (boolean XX_VMA_Assigned)
    {
        set_Value ("XX_VMA_Assigned", Boolean.valueOf(XX_VMA_Assigned));
        
    }
    
    /** Get Assigned.
    @return The element is assigned. */
    public boolean isXX_VMA_Assigned() 
    {
        return get_ValueAsBoolean("XX_VMA_Assigned");
        
    }
    
    /** Set Brochure.
    @param XX_VMA_Brochure_ID Identifier of the Brochure. */
    public void setXX_VMA_Brochure_ID (int XX_VMA_Brochure_ID)
    {
        if (XX_VMA_Brochure_ID < 1) throw new IllegalArgumentException ("XX_VMA_Brochure_ID is mandatory.");
        set_ValueNoCheck ("XX_VMA_Brochure_ID", Integer.valueOf(XX_VMA_Brochure_ID));
        
    }
    
    /** Get Brochure.
    @return Identifier of the Brochure. */
    public int getXX_VMA_Brochure_ID() 
    {
        return get_ValueAsInt("XX_VMA_Brochure_ID");
        
    }
    
    /** Set Brochure Vendor Report.
    @param XX_VMA_BrochureVendorReport Brochure Vendor Report */
    public void setXX_VMA_BrochureVendorReport (String XX_VMA_BrochureVendorReport)
    {
        set_Value ("XX_VMA_BrochureVendorReport", XX_VMA_BrochureVendorReport);
        
    }
    
    /** Get Brochure Vendor Report.
    @return Brochure Vendor Report */
    public String getXX_VMA_BrochureVendorReport() 
    {
        return (String)get_Value("XX_VMA_BrochureVendorReport");
        
    }
    
    /** Set Expired.
    @param XX_VMA_Expired The Brochure has expired. */
    public void setXX_VMA_Expired (boolean XX_VMA_Expired)
    {
        set_Value ("XX_VMA_Expired", Boolean.valueOf(XX_VMA_Expired));
        
    }
    
    /** Get Expired.
    @return The Brochure has expired. */
    public boolean isXX_VMA_Expired() 
    {
        return get_ValueAsBoolean("XX_VMA_Expired");
        
    }
    
    /** Set Is Brochure Active.
    @param XX_VMA_IsBrochureActive The brochure is active or not */
    public void setXX_VMA_IsBrochureActive (boolean XX_VMA_IsBrochureActive)
    {
        set_Value ("XX_VMA_IsBrochureActive", Boolean.valueOf(XX_VMA_IsBrochureActive));
        
    }
    
    /** Get Is Brochure Active.
    @return The brochure is active or not */
    public boolean isXX_VMA_IsBrochureActive() 
    {
        return get_ValueAsBoolean("XX_VMA_IsBrochureActive");
        
    }
    
    /** Set Is MA Active.
    @param XX_VMA_IsMAActive The marketing activity asociated is active or not */
    public void setXX_VMA_IsMAActive (boolean XX_VMA_IsMAActive)
    {
        set_Value ("XX_VMA_IsMAActive", Boolean.valueOf(XX_VMA_IsMAActive));
        
    }
    
    /** Get Is MA Active.
    @return The marketing activity asociated is active or not */
    public boolean isXX_VMA_IsMAActive() 
    {
        return get_ValueAsBoolean("XX_VMA_IsMAActive");
        
    }
    
    /** Set Marketing Activity Approved.
    @param XX_VMA_MAApproved The marketing activity associated is already approved */
    public void setXX_VMA_MAApproved (boolean XX_VMA_MAApproved)
    {
        set_Value ("XX_VMA_MAApproved", Boolean.valueOf(XX_VMA_MAApproved));
        
    }
    
    /** Get Marketing Activity Approved.
    @return The marketing activity associated is already approved */
    public boolean isXX_VMA_MAApproved() 
    {
        return get_ValueAsBoolean("XX_VMA_MAApproved");
        
    }
    
    /** Set Generate Indepabis Report.
    @param XX_VMA_ReportIndepabis Generate Indepabis Report */
    public void setXX_VMA_ReportIndepabis (String XX_VMA_ReportIndepabis)
    {
        set_Value ("XX_VMA_ReportIndepabis", XX_VMA_ReportIndepabis);
        
    }
    
    /** Get Generate Indepabis Report.
    @return Generate Indepabis Report */
    public String getXX_VMA_ReportIndepabis() 
    {
        return (String)get_Value("XX_VMA_ReportIndepabis");
        
    }
    
    /** Set Topic.
    @param XX_VMA_Topic Is the theme that the company wants to promote. */
    public void setXX_VMA_Topic (String XX_VMA_Topic)
    {
        if (XX_VMA_Topic == null) throw new IllegalArgumentException ("XX_VMA_Topic is mandatory.");
        set_Value ("XX_VMA_Topic", XX_VMA_Topic);
        
    }
    
    /** Get Topic.
    @return Is the theme that the company wants to promote. */
    public String getXX_VMA_Topic() 
    {
        return (String)get_Value("XX_VMA_Topic");
        
    }
    
    
}
