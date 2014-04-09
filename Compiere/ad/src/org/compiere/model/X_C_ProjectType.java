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
/** Generated Model for C_ProjectType
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_ProjectType.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_ProjectType extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_ProjectType_ID id
    @param trx transaction
    */
    public X_C_ProjectType (Ctx ctx, int C_ProjectType_ID, Trx trx)
    {
        super (ctx, C_ProjectType_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_ProjectType_ID == 0)
        {
            setC_ProjectType_ID (0);
            setName (null);
            setProjectCategory (null);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_ProjectType (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=575 */
    public static final int Table_ID=575;
    
    /** TableName=C_ProjectType */
    public static final String Table_Name="C_ProjectType";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Project Type.
    @param C_ProjectType_ID Type of the project */
    public void setC_ProjectType_ID (int C_ProjectType_ID)
    {
        if (C_ProjectType_ID < 1) throw new IllegalArgumentException ("C_ProjectType_ID is mandatory.");
        set_ValueNoCheck ("C_ProjectType_ID", Integer.valueOf(C_ProjectType_ID));
        
    }
    
    /** Get Project Type.
    @return Type of the project */
    public int getC_ProjectType_ID() 
    {
        return get_ValueAsInt("C_ProjectType_ID");
        
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
    
    /** Asset Project = A */
    public static final String PROJECTCATEGORY_AssetProject = X_Ref_C_ProjectType_Category.ASSET_PROJECT.getValue();
    /** General = N */
    public static final String PROJECTCATEGORY_General = X_Ref_C_ProjectType_Category.GENERAL.getValue();
    /** Service (Charge) Project = S */
    public static final String PROJECTCATEGORY_ServiceChargeProject = X_Ref_C_ProjectType_Category.SERVICE_CHARGE_PROJECT.getValue();
    /** Work Order (Job) = W */
    public static final String PROJECTCATEGORY_WorkOrderJob = X_Ref_C_ProjectType_Category.WORK_ORDER_JOB.getValue();
    /** Set Project Category.
    @param ProjectCategory Project Category */
    public void setProjectCategory (String ProjectCategory)
    {
        if (ProjectCategory == null) throw new IllegalArgumentException ("ProjectCategory is mandatory");
        if (!X_Ref_C_ProjectType_Category.isValid(ProjectCategory))
        throw new IllegalArgumentException ("ProjectCategory Invalid value - " + ProjectCategory + " - Reference_ID=288 - A - N - S - W");
        set_ValueNoCheck ("ProjectCategory", ProjectCategory);
        
    }
    
    /** Get Project Category.
    @return Project Category */
    public String getProjectCategory() 
    {
        return (String)get_Value("ProjectCategory");
        
    }
    
    
}
