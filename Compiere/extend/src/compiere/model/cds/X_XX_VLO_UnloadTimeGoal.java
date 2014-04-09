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
package compiere.model.cds;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VLO_UnloadTimeGoal
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_UnloadTimeGoal extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_UnloadTimeGoal_ID id
    @param trx transaction
    */
    public X_XX_VLO_UnloadTimeGoal (Ctx ctx, int XX_VLO_UnloadTimeGoal_ID, Trx trx)
    {
        super (ctx, XX_VLO_UnloadTimeGoal_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_UnloadTimeGoal_ID == 0)
        {
            setXX_VLO_UnloadTimeGoal_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_UnloadTimeGoal (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27579488717789L;
    /** Last Updated Timestamp 2011-02-10 14:13:21.0 */
    public static final long updatedMS = 1297363401000L;
    /** AD_Table_ID=1000385 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_UnloadTimeGoal");
        
    }
    ;
    
    /** TableName=XX_VLO_UnloadTimeGoal */
    public static final String Table_Name="XX_VLO_UnloadTimeGoal";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Imported Goal Hours.
    @param XX_ImportedGoalHours Imported Goal Hours */
    public void setXX_ImportedGoalHours (int XX_ImportedGoalHours)
    {
        set_Value ("XX_ImportedGoalHours", Integer.valueOf(XX_ImportedGoalHours));
        
    }
    
    /** Get Imported Goal Hours.
    @return Imported Goal Hours */
    public int getXX_ImportedGoalHours() 
    {
        return get_ValueAsInt("XX_ImportedGoalHours");
        
    }
    
    /** Set Imported Goal Mins.
    @param XX_ImportedGoalMins Imported Goal Mins */
    public void setXX_ImportedGoalMins (int XX_ImportedGoalMins)
    {
        set_Value ("XX_ImportedGoalMins", Integer.valueOf(XX_ImportedGoalMins));
        
    }
    
    /** Get Imported Goal Mins.
    @return Imported Goal Mins */
    public int getXX_ImportedGoalMins() 
    {
        return get_ValueAsInt("XX_ImportedGoalMins");
        
    }
    
    /** Set Imported Goal Seconds.
    @param XX_ImportedGoalSeconds Imported Goal Seconds */
    public void setXX_ImportedGoalSeconds (int XX_ImportedGoalSeconds)
    {
        set_Value ("XX_ImportedGoalSeconds", Integer.valueOf(XX_ImportedGoalSeconds));
        
    }
    
    /** Get Imported Goal Seconds.
    @return Imported Goal Seconds */
    public int getXX_ImportedGoalSeconds() 
    {
        return get_ValueAsInt("XX_ImportedGoalSeconds");
        
    }
    
    /** Set National Goal Hours.
    @param XX_NationalGoalHours National Goal Hours */
    public void setXX_NationalGoalHours (int XX_NationalGoalHours)
    {
        set_Value ("XX_NationalGoalHours", Integer.valueOf(XX_NationalGoalHours));
        
    }
    
    /** Get National Goal Hours.
    @return National Goal Hours */
    public int getXX_NationalGoalHours() 
    {
        return get_ValueAsInt("XX_NationalGoalHours");
        
    }
    
    /** Set National Goal Mins.
    @param XX_NationalGoalMins National Goal Mins */
    public void setXX_NationalGoalMins (int XX_NationalGoalMins)
    {
        set_Value ("XX_NationalGoalMins", Integer.valueOf(XX_NationalGoalMins));
        
    }
    
    /** Get National Goal Mins.
    @return National Goal Mins */
    public int getXX_NationalGoalMins() 
    {
        return get_ValueAsInt("XX_NationalGoalMins");
        
    }
    
    /** Set National Goal Seconds.
    @param XX_NationalGoalSeconds National Goal Seconds */
    public void setXX_NationalGoalSeconds (int XX_NationalGoalSeconds)
    {
        set_Value ("XX_NationalGoalSeconds", Integer.valueOf(XX_NationalGoalSeconds));
        
    }
    
    /** Get National Goal Seconds.
    @return National Goal Seconds */
    public int getXX_NationalGoalSeconds() 
    {
        return get_ValueAsInt("XX_NationalGoalSeconds");
        
    }
    
    /** Set XX_VLO_UnloadTimeGoal_ID.
    @param XX_VLO_UnloadTimeGoal_ID XX_VLO_UnloadTimeGoal_ID */
    public void setXX_VLO_UnloadTimeGoal_ID (int XX_VLO_UnloadTimeGoal_ID)
    {
        if (XX_VLO_UnloadTimeGoal_ID < 1) throw new IllegalArgumentException ("XX_VLO_UnloadTimeGoal_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_UnloadTimeGoal_ID", Integer.valueOf(XX_VLO_UnloadTimeGoal_ID));
        
    }
    
    /** Get XX_VLO_UnloadTimeGoal_ID.
    @return XX_VLO_UnloadTimeGoal_ID */
    public int getXX_VLO_UnloadTimeGoal_ID() 
    {
        return get_ValueAsInt("XX_VLO_UnloadTimeGoal_ID");
        
    }
    
    
}
