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
/** Generated Model for GL_BudgetControl
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_GL_BudgetControl.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_GL_BudgetControl extends PO
{
    /** Standard Constructor
    @param ctx context
    @param GL_BudgetControl_ID id
    @param trx transaction
    */
    public X_GL_BudgetControl (Ctx ctx, int GL_BudgetControl_ID, Trx trx)
    {
        super (ctx, GL_BudgetControl_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (GL_BudgetControl_ID == 0)
        {
            setBudgetControlScope (null);
            setC_AcctSchema_ID (0);
            setCommitmentType (null);	// C
            setGL_BudgetControl_ID (0);
            setGL_Budget_ID (0);
            setIsBeforeApproval (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_GL_BudgetControl (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=822 */
    public static final int Table_ID=822;
    
    /** TableName=GL_BudgetControl */
    public static final String Table_Name="GL_BudgetControl";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Period only = P */
    public static final String BUDGETCONTROLSCOPE_PeriodOnly = X_Ref_GL_BudgetControl_Scope.PERIOD_ONLY.getValue();
    /** Total = T */
    public static final String BUDGETCONTROLSCOPE_Total = X_Ref_GL_BudgetControl_Scope.TOTAL.getValue();
    /** Year to Date = Y */
    public static final String BUDGETCONTROLSCOPE_YearToDate = X_Ref_GL_BudgetControl_Scope.YEAR_TO_DATE.getValue();
    /** Set Control Scope.
    @param BudgetControlScope Scope of the Budget Control */
    public void setBudgetControlScope (String BudgetControlScope)
    {
        if (BudgetControlScope == null) throw new IllegalArgumentException ("BudgetControlScope is mandatory");
        if (!X_Ref_GL_BudgetControl_Scope.isValid(BudgetControlScope))
        throw new IllegalArgumentException ("BudgetControlScope Invalid value - " + BudgetControlScope + " - Reference_ID=361 - P - T - Y");
        set_Value ("BudgetControlScope", BudgetControlScope);
        
    }
    
    /** Get Control Scope.
    @return Scope of the Budget Control */
    public String getBudgetControlScope() 
    {
        return (String)get_Value("BudgetControlScope");
        
    }
    
    /** Set Accounting Schema.
    @param C_AcctSchema_ID Rules for accounting */
    public void setC_AcctSchema_ID (int C_AcctSchema_ID)
    {
        if (C_AcctSchema_ID < 1) throw new IllegalArgumentException ("C_AcctSchema_ID is mandatory.");
        set_Value ("C_AcctSchema_ID", Integer.valueOf(C_AcctSchema_ID));
        
    }
    
    /** Get Accounting Schema.
    @return Rules for accounting */
    public int getC_AcctSchema_ID() 
    {
        return get_ValueAsInt("C_AcctSchema_ID");
        
    }
    
    /** Commitment & Reservation = B */
    public static final String COMMITMENTTYPE_CommitmentReservation = X_Ref_C_AcctSchema_CommitmentType.COMMITMENT_RESERVATION.getValue();
    /** Commitment only = C */
    public static final String COMMITMENTTYPE_CommitmentOnly = X_Ref_C_AcctSchema_CommitmentType.COMMITMENT_ONLY.getValue();
    /** None = N */
    public static final String COMMITMENTTYPE_None = X_Ref_C_AcctSchema_CommitmentType.NONE.getValue();
    /** Set Commitment Type.
    @param CommitmentType Create Commitment and/or Reservations for Budget Control */
    public void setCommitmentType (String CommitmentType)
    {
        if (CommitmentType == null) throw new IllegalArgumentException ("CommitmentType is mandatory");
        if (!X_Ref_C_AcctSchema_CommitmentType.isValid(CommitmentType))
        throw new IllegalArgumentException ("CommitmentType Invalid value - " + CommitmentType + " - Reference_ID=359 - B - C - N");
        set_Value ("CommitmentType", CommitmentType);
        
    }
    
    /** Get Commitment Type.
    @return Create Commitment and/or Reservations for Budget Control */
    public String getCommitmentType() 
    {
        return (String)get_Value("CommitmentType");
        
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
    
    /** Set Budget Control.
    @param GL_BudgetControl_ID Budget Control */
    public void setGL_BudgetControl_ID (int GL_BudgetControl_ID)
    {
        if (GL_BudgetControl_ID < 1) throw new IllegalArgumentException ("GL_BudgetControl_ID is mandatory.");
        set_ValueNoCheck ("GL_BudgetControl_ID", Integer.valueOf(GL_BudgetControl_ID));
        
    }
    
    /** Get Budget Control.
    @return Budget Control */
    public int getGL_BudgetControl_ID() 
    {
        return get_ValueAsInt("GL_BudgetControl_ID");
        
    }
    
    /** Set Budget.
    @param GL_Budget_ID General Ledger Budget */
    public void setGL_Budget_ID (int GL_Budget_ID)
    {
        if (GL_Budget_ID < 1) throw new IllegalArgumentException ("GL_Budget_ID is mandatory.");
        set_Value ("GL_Budget_ID", Integer.valueOf(GL_Budget_ID));
        
    }
    
    /** Get Budget.
    @return General Ledger Budget */
    public int getGL_Budget_ID() 
    {
        return get_ValueAsInt("GL_Budget_ID");
        
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
    
    /** Set Before Approval.
    @param IsBeforeApproval The Check is before the (manual) approval */
    public void setIsBeforeApproval (boolean IsBeforeApproval)
    {
        set_Value ("IsBeforeApproval", Boolean.valueOf(IsBeforeApproval));
        
    }
    
    /** Get Before Approval.
    @return The Check is before the (manual) approval */
    public boolean isBeforeApproval() 
    {
        return get_ValueAsBoolean("IsBeforeApproval");
        
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
