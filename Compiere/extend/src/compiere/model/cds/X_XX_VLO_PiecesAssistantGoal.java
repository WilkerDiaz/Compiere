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
/** Generated Model for XX_VLO_PiecesAssistantGoal
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_PiecesAssistantGoal extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_PiecesAssistantGoal_ID id
    @param trx transaction
    */
    public X_XX_VLO_PiecesAssistantGoal (Ctx ctx, int XX_VLO_PiecesAssistantGoal_ID, Trx trx)
    {
        super (ctx, XX_VLO_PiecesAssistantGoal_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_PiecesAssistantGoal_ID == 0)
        {
            setXX_VLO_PiecesAssistantGoal_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_PiecesAssistantGoal (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27579487629789L;
    /** Last Updated Timestamp 2011-02-10 13:55:13.0 */
    public static final long updatedMS = 1297362313000L;
    /** AD_Table_ID=1000384 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_PiecesAssistantGoal");
        
    }
    ;
    
    /** TableName=XX_VLO_PiecesAssistantGoal */
    public static final String Table_Name="XX_VLO_PiecesAssistantGoal";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Checkup Assistant.
    @param XX_CheckAssistant_ID Checkup Assistant */
    public void setXX_CheckAssistant_ID (int XX_CheckAssistant_ID)
    {
        if (XX_CheckAssistant_ID <= 0) set_Value ("XX_CheckAssistant_ID", null);
        else
        set_Value ("XX_CheckAssistant_ID", Integer.valueOf(XX_CheckAssistant_ID));
        
    }
    
    /** Get Checkup Assistant.
    @return Checkup Assistant */
    public int getXX_CheckAssistant_ID() 
    {
        return get_ValueAsInt("XX_CheckAssistant_ID");
        
    }
    
    /** Set Month.
    @param XX_Month Month */
    public void setXX_Month (int XX_Month)
    {
        set_Value ("XX_Month", Integer.valueOf(XX_Month));
        
    }
    
    /** Get Month.
    @return Month */
    public int getXX_Month() 
    {
        return get_ValueAsInt("XX_Month");
        
    }
    
    /** Set Pieces Goal.
    @param XX_PiecesGoal Pieces Goal */
    public void setXX_PiecesGoal (int XX_PiecesGoal)
    {
        set_Value ("XX_PiecesGoal", Integer.valueOf(XX_PiecesGoal));
        
    }
    
    /** Get Pieces Goal.
    @return Pieces Goal */
    public int getXX_PiecesGoal() 
    {
        return get_ValueAsInt("XX_PiecesGoal");
        
    }
    
    /** Set XX_VLO_PiecesAssistantGoal_ID.
    @param XX_VLO_PiecesAssistantGoal_ID XX_VLO_PiecesAssistantGoal_ID */
    public void setXX_VLO_PiecesAssistantGoal_ID (int XX_VLO_PiecesAssistantGoal_ID)
    {
        if (XX_VLO_PiecesAssistantGoal_ID < 1) throw new IllegalArgumentException ("XX_VLO_PiecesAssistantGoal_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_PiecesAssistantGoal_ID", Integer.valueOf(XX_VLO_PiecesAssistantGoal_ID));
        
    }
    
    /** Get XX_VLO_PiecesAssistantGoal_ID.
    @return XX_VLO_PiecesAssistantGoal_ID */
    public int getXX_VLO_PiecesAssistantGoal_ID() 
    {
        return get_ValueAsInt("XX_VLO_PiecesAssistantGoal_ID");
        
    }
    
    /** Set Year.
    @param XX_Year Year */
    public void setXX_Year (int XX_Year)
    {
        set_Value ("XX_Year", Integer.valueOf(XX_Year));
        
    }
    
    /** Get Year.
    @return Year */
    public int getXX_Year() 
    {
        return get_ValueAsInt("XX_Year");
        
    }
    
    
}
