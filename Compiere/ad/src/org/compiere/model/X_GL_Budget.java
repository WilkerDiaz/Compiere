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
/** Generated Model for GL_Budget
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_GL_Budget.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_GL_Budget extends PO
{
    /** Standard Constructor
    @param ctx context
    @param GL_Budget_ID id
    @param trx transaction
    */
    public X_GL_Budget (Ctx ctx, int GL_Budget_ID, Trx trx)
    {
        super (ctx, GL_Budget_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (GL_Budget_ID == 0)
        {
            setGL_Budget_ID (0);
            setIsPrimary (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_GL_Budget (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=271 */
    public static final int Table_ID=271;
    
    /** TableName=GL_Budget */
    public static final String Table_Name="GL_Budget";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Approved = A */
    public static final String BUDGETSTATUS_Approved = X_Ref_GL_Budget_Status.APPROVED.getValue();
    /** Draft = D */
    public static final String BUDGETSTATUS_Draft = X_Ref_GL_Budget_Status.DRAFT.getValue();
    /** Set Budget Status.
    @param BudgetStatus Indicates the current status of this budget */
    public void setBudgetStatus (String BudgetStatus)
    {
        if (!X_Ref_GL_Budget_Status.isValid(BudgetStatus))
        throw new IllegalArgumentException ("BudgetStatus Invalid value - " + BudgetStatus + " - Reference_ID=178 - A - D");
        set_Value ("BudgetStatus", BudgetStatus);
        
    }
    
    /** Get Budget Status.
    @return Indicates the current status of this budget */
    public String getBudgetStatus() 
    {
        return (String)get_Value("BudgetStatus");
        
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
    
    /** Set Budget.
    @param GL_Budget_ID General Ledger Budget */
    public void setGL_Budget_ID (int GL_Budget_ID)
    {
        if (GL_Budget_ID < 1) throw new IllegalArgumentException ("GL_Budget_ID is mandatory.");
        set_ValueNoCheck ("GL_Budget_ID", Integer.valueOf(GL_Budget_ID));
        
    }
    
    /** Get Budget.
    @return General Ledger Budget */
    public int getGL_Budget_ID() 
    {
        return get_ValueAsInt("GL_Budget_ID");
        
    }
    
    /** Set Primary.
    @param IsPrimary Indicates if this is the primary budget */
    public void setIsPrimary (boolean IsPrimary)
    {
        set_Value ("IsPrimary", Boolean.valueOf(IsPrimary));
        
    }
    
    /** Get Primary.
    @return Indicates if this is the primary budget */
    public boolean isPrimary() 
    {
        return get_ValueAsBoolean("IsPrimary");
        
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
