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
/** Generated Model for C_Job
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_Job.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_Job extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_Job_ID id
    @param trx transaction
    */
    public X_C_Job (Ctx ctx, int C_Job_ID, Trx trx)
    {
        super (ctx, C_Job_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_Job_ID == 0)
        {
            setC_JobCategory_ID (0);
            setC_Job_ID (0);
            setIsEmployee (true);	// Y
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_Job (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=789 */
    public static final int Table_ID=789;
    
    /** TableName=C_Job */
    public static final String Table_Name="C_Job";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Position Category.
    @param C_JobCategory_ID Job Position Category */
    public void setC_JobCategory_ID (int C_JobCategory_ID)
    {
        if (C_JobCategory_ID < 1) throw new IllegalArgumentException ("C_JobCategory_ID is mandatory.");
        set_Value ("C_JobCategory_ID", Integer.valueOf(C_JobCategory_ID));
        
    }
    
    /** Get Position Category.
    @return Job Position Category */
    public int getC_JobCategory_ID() 
    {
        return get_ValueAsInt("C_JobCategory_ID");
        
    }
    
    /** Set Position.
    @param C_Job_ID Job Position */
    public void setC_Job_ID (int C_Job_ID)
    {
        if (C_Job_ID < 1) throw new IllegalArgumentException ("C_Job_ID is mandatory.");
        set_ValueNoCheck ("C_Job_ID", Integer.valueOf(C_Job_ID));
        
    }
    
    /** Get Position.
    @return Job Position */
    public int getC_Job_ID() 
    {
        return get_ValueAsInt("C_Job_ID");
        
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
    
    /** Set Employee.
    @param IsEmployee Indicates if this Business Partner is an employee */
    public void setIsEmployee (boolean IsEmployee)
    {
        set_Value ("IsEmployee", Boolean.valueOf(IsEmployee));
        
    }
    
    /** Get Employee.
    @return Indicates if this Business Partner is an employee */
    public boolean isEmployee() 
    {
        return get_ValueAsBoolean("IsEmployee");
        
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
    
    
}
