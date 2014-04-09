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
/** Generated Model for PA_SLA_Criteria
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_PA_SLA_Criteria.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_PA_SLA_Criteria extends PO
{
    /** Standard Constructor
    @param ctx context
    @param PA_SLA_Criteria_ID id
    @param trx transaction
    */
    public X_PA_SLA_Criteria (Ctx ctx, int PA_SLA_Criteria_ID, Trx trx)
    {
        super (ctx, PA_SLA_Criteria_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (PA_SLA_Criteria_ID == 0)
        {
            setIsManual (true);	// Y
            setName (null);
            setPA_SLA_Criteria_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_PA_SLA_Criteria (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=744 */
    public static final int Table_ID=744;
    
    /** TableName=PA_SLA_Criteria */
    public static final String Table_Name="PA_SLA_Criteria";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Classname.
    @param Classname Java Classname */
    public void setClassname (String Classname)
    {
        set_Value ("Classname", Classname);
        
    }
    
    /** Get Classname.
    @return Java Classname */
    public String getClassname() 
    {
        return (String)get_Value("Classname");
        
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
    
    /** Set Manual.
    @param IsManual This is a manual process or entry */
    public void setIsManual (boolean IsManual)
    {
        set_Value ("IsManual", Boolean.valueOf(IsManual));
        
    }
    
    /** Get Manual.
    @return This is a manual process or entry */
    public boolean isManual() 
    {
        return get_ValueAsBoolean("IsManual");
        
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
    
    /** Set SLA Criteria.
    @param PA_SLA_Criteria_ID Service Level Agreement Criteria */
    public void setPA_SLA_Criteria_ID (int PA_SLA_Criteria_ID)
    {
        if (PA_SLA_Criteria_ID < 1) throw new IllegalArgumentException ("PA_SLA_Criteria_ID is mandatory.");
        set_ValueNoCheck ("PA_SLA_Criteria_ID", Integer.valueOf(PA_SLA_Criteria_ID));
        
    }
    
    /** Get SLA Criteria.
    @return Service Level Agreement Criteria */
    public int getPA_SLA_Criteria_ID() 
    {
        return get_ValueAsInt("PA_SLA_Criteria_ID");
        
    }
    
    
}
