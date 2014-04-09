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
/** Generated Model for CM_Ad
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_CM_Ad.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_CM_Ad extends PO
{
    /** Standard Constructor
    @param ctx context
    @param CM_Ad_ID id
    @param trx transaction
    */
    public X_CM_Ad (Ctx ctx, int CM_Ad_ID, Trx trx)
    {
        super (ctx, CM_Ad_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (CM_Ad_ID == 0)
        {
            setActualClick (0);
            setActualImpression (0);
            setCM_Ad_Cat_ID (0);
            setCM_Ad_ID (0);
            setCM_Media_ID (0);
            setIsAdFlag (false);
            setIsLogged (false);
            setMaxClick (0);
            setMaxImpression (0);
            setName (null);
            setStartDate (new Timestamp(System.currentTimeMillis()));
            setStartImpression (0);
            setTarget_Frame (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_CM_Ad (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=858 */
    public static final int Table_ID=858;
    
    /** TableName=CM_Ad */
    public static final String Table_Name="CM_Ad";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Actual Click Count.
    @param ActualClick Indicates how many clicks have been counted */
    public void setActualClick (int ActualClick)
    {
        set_Value ("ActualClick", Integer.valueOf(ActualClick));
        
    }
    
    /** Get Actual Click Count.
    @return Indicates how many clicks have been counted */
    public int getActualClick() 
    {
        return get_ValueAsInt("ActualClick");
        
    }
    
    /** Set Actual Impression Count.
    @param ActualImpression Indicates how many impressions have been counted */
    public void setActualImpression (int ActualImpression)
    {
        set_Value ("ActualImpression", Integer.valueOf(ActualImpression));
        
    }
    
    /** Get Actual Impression Count.
    @return Indicates how many impressions have been counted */
    public int getActualImpression() 
    {
        return get_ValueAsInt("ActualImpression");
        
    }
    
    /** Set Advertisement Category.
    @param CM_Ad_Cat_ID Advertisement Category like Banner Homepage */
    public void setCM_Ad_Cat_ID (int CM_Ad_Cat_ID)
    {
        if (CM_Ad_Cat_ID < 1) throw new IllegalArgumentException ("CM_Ad_Cat_ID is mandatory.");
        set_ValueNoCheck ("CM_Ad_Cat_ID", Integer.valueOf(CM_Ad_Cat_ID));
        
    }
    
    /** Get Advertisement Category.
    @return Advertisement Category like Banner Homepage */
    public int getCM_Ad_Cat_ID() 
    {
        return get_ValueAsInt("CM_Ad_Cat_ID");
        
    }
    
    /** Set Advertisement.
    @param CM_Ad_ID An Advertisement is something like a banner */
    public void setCM_Ad_ID (int CM_Ad_ID)
    {
        if (CM_Ad_ID < 1) throw new IllegalArgumentException ("CM_Ad_ID is mandatory.");
        set_ValueNoCheck ("CM_Ad_ID", Integer.valueOf(CM_Ad_ID));
        
    }
    
    /** Get Advertisement.
    @return An Advertisement is something like a banner */
    public int getCM_Ad_ID() 
    {
        return get_ValueAsInt("CM_Ad_ID");
        
    }
    
    /** Set Media Item.
    @param CM_Media_ID Contains media content like images, flash movies etc. */
    public void setCM_Media_ID (int CM_Media_ID)
    {
        if (CM_Media_ID < 1) throw new IllegalArgumentException ("CM_Media_ID is mandatory.");
        set_Value ("CM_Media_ID", Integer.valueOf(CM_Media_ID));
        
    }
    
    /** Get Media Item.
    @return Contains media content like images, flash movies etc. */
    public int getCM_Media_ID() 
    {
        return get_ValueAsInt("CM_Media_ID");
        
    }
    
    /** Set Content HTML.
    @param ContentHTML Contains the content itself */
    public void setContentHTML (String ContentHTML)
    {
        set_Value ("ContentHTML", ContentHTML);
        
    }
    
    /** Get Content HTML.
    @return Contains the content itself */
    public String getContentHTML() 
    {
        return (String)get_Value("ContentHTML");
        
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
    
    /** Set End Date.
    @param EndDate Last effective date (inclusive) */
    public void setEndDate (Timestamp EndDate)
    {
        set_Value ("EndDate", EndDate);
        
    }
    
    /** Get End Date.
    @return Last effective date (inclusive) */
    public Timestamp getEndDate() 
    {
        return (Timestamp)get_Value("EndDate");
        
    }
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
    }
    
    /** Set Special AD Flag.
    @param IsAdFlag Do we need to specially mention this ad? */
    public void setIsAdFlag (boolean IsAdFlag)
    {
        set_Value ("IsAdFlag", Boolean.valueOf(IsAdFlag));
        
    }
    
    /** Get Special AD Flag.
    @return Do we need to specially mention this ad? */
    public boolean isAdFlag() 
    {
        return get_ValueAsBoolean("IsAdFlag");
        
    }
    
    /** Set Logging.
    @param IsLogged Do we need to log the banner impressions and clicks? (Requires a perform ant system) */
    public void setIsLogged (boolean IsLogged)
    {
        set_Value ("IsLogged", Boolean.valueOf(IsLogged));
        
    }
    
    /** Get Logging.
    @return Do we need to log the banner impressions and clicks? (Requires a perform ant system) */
    public boolean isLogged() 
    {
        return get_ValueAsBoolean("IsLogged");
        
    }
    
    /** Set Max Click Count.
    @param MaxClick Maximum Click Count until banner is deactivated */
    public void setMaxClick (int MaxClick)
    {
        set_Value ("MaxClick", Integer.valueOf(MaxClick));
        
    }
    
    /** Get Max Click Count.
    @return Maximum Click Count until banner is deactivated */
    public int getMaxClick() 
    {
        return get_ValueAsInt("MaxClick");
        
    }
    
    /** Set Max Impression Count.
    @param MaxImpression Maximum Impression Count until banner is deactivated */
    public void setMaxImpression (int MaxImpression)
    {
        set_Value ("MaxImpression", Integer.valueOf(MaxImpression));
        
    }
    
    /** Get Max Impression Count.
    @return Maximum Impression Count until banner is deactivated */
    public int getMaxImpression() 
    {
        return get_ValueAsInt("MaxImpression");
        
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
    
    /** Set Start Count Impression.
    @param StartImpression For rotation we need a start count */
    public void setStartImpression (int StartImpression)
    {
        set_Value ("StartImpression", Integer.valueOf(StartImpression));
        
    }
    
    /** Get Start Count Impression.
    @return For rotation we need a start count */
    public int getStartImpression() 
    {
        return get_ValueAsInt("StartImpression");
        
    }
    
    /** Set Target URL.
    @param TargetURL URL for the Target */
    public void setTargetURL (String TargetURL)
    {
        set_Value ("TargetURL", TargetURL);
        
    }
    
    /** Get Target URL.
    @return URL for the Target */
    public String getTargetURL() 
    {
        return (String)get_Value("TargetURL");
        
    }
    
    /** Set Target Frame.
    @param Target_Frame Which target should be used if user clicks? */
    public void setTarget_Frame (String Target_Frame)
    {
        if (Target_Frame == null) throw new IllegalArgumentException ("Target_Frame is mandatory.");
        set_Value ("Target_Frame", Target_Frame);
        
    }
    
    /** Get Target Frame.
    @return Which target should be used if user clicks? */
    public String getTarget_Frame() 
    {
        return (String)get_Value("Target_Frame");
        
    }
    
    
}
