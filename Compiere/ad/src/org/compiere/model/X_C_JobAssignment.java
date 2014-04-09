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
/** Generated Model for C_JobAssignment
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_JobAssignment.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_JobAssignment extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_JobAssignment_ID id
    @param trx transaction
    */
    public X_C_JobAssignment (Ctx ctx, int C_JobAssignment_ID, Trx trx)
    {
        super (ctx, C_JobAssignment_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_JobAssignment_ID == 0)
        {
            setAD_User_ID (0);
            setC_JobAssignment_ID (0);
            setC_Job_ID (0);
            setJobAssignmentType (null);	// P
            setValidFrom (new Timestamp(System.currentTimeMillis()));
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_JobAssignment (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=791 */
    public static final int Table_ID=791;
    
    /** TableName=C_JobAssignment */
    public static final String Table_Name="C_JobAssignment";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID < 1) throw new IllegalArgumentException ("AD_User_ID is mandatory.");
        set_ValueNoCheck ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Position Assignment.
    @param C_JobAssignment_ID Assignment of Employee (User) to Job Position */
    public void setC_JobAssignment_ID (int C_JobAssignment_ID)
    {
        if (C_JobAssignment_ID < 1) throw new IllegalArgumentException ("C_JobAssignment_ID is mandatory.");
        set_ValueNoCheck ("C_JobAssignment_ID", Integer.valueOf(C_JobAssignment_ID));
        
    }
    
    /** Get Position Assignment.
    @return Assignment of Employee (User) to Job Position */
    public int getC_JobAssignment_ID() 
    {
        return get_ValueAsInt("C_JobAssignment_ID");
        
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
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_Job_ID()));
        
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
    
    /** Other = O */
    public static final String JOBASSIGNMENTTYPE_Other = X_Ref_JobAssignmentType.OTHER.getValue();
    /** Primary = P */
    public static final String JOBASSIGNMENTTYPE_Primary = X_Ref_JobAssignmentType.PRIMARY.getValue();
    /** Secondary = S */
    public static final String JOBASSIGNMENTTYPE_Secondary = X_Ref_JobAssignmentType.SECONDARY.getValue();
    /** Temporary = T */
    public static final String JOBASSIGNMENTTYPE_Temporary = X_Ref_JobAssignmentType.TEMPORARY.getValue();
    /** Set Assignment Type.
    @param JobAssignmentType Job Assignment Type */
    public void setJobAssignmentType (String JobAssignmentType)
    {
        if (JobAssignmentType == null) throw new IllegalArgumentException ("JobAssignmentType is mandatory");
        if (!X_Ref_JobAssignmentType.isValid(JobAssignmentType))
        throw new IllegalArgumentException ("JobAssignmentType Invalid value - " + JobAssignmentType + " - Reference_ID=400 - O - P - S - T");
        set_Value ("JobAssignmentType", JobAssignmentType);
        
    }
    
    /** Get Assignment Type.
    @return Job Assignment Type */
    public String getJobAssignmentType() 
    {
        return (String)get_Value("JobAssignmentType");
        
    }
    
    /** Set Valid from.
    @param ValidFrom Valid from including this date (first day) */
    public void setValidFrom (Timestamp ValidFrom)
    {
        if (ValidFrom == null) throw new IllegalArgumentException ("ValidFrom is mandatory.");
        set_Value ("ValidFrom", ValidFrom);
        
    }
    
    /** Get Valid from.
    @return Valid from including this date (first day) */
    public Timestamp getValidFrom() 
    {
        return (Timestamp)get_Value("ValidFrom");
        
    }
    
    /** Set Valid to.
    @param ValidTo Valid to including this date (last day) */
    public void setValidTo (Timestamp ValidTo)
    {
        set_Value ("ValidTo", ValidTo);
        
    }
    
    /** Get Valid to.
    @return Valid to including this date (last day) */
    public Timestamp getValidTo() 
    {
        return (Timestamp)get_Value("ValidTo");
        
    }
    
    
}
