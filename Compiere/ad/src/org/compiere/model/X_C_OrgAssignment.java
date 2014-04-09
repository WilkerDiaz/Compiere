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
/** Generated Model for C_OrgAssignment
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_OrgAssignment.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_OrgAssignment extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_OrgAssignment_ID id
    @param trx transaction
    */
    public X_C_OrgAssignment (Ctx ctx, int C_OrgAssignment_ID, Trx trx)
    {
        super (ctx, C_OrgAssignment_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_OrgAssignment_ID == 0)
        {
            setAD_User_ID (0);
            setC_OrgAssignment_ID (0);
            setOrgAssignmentType (null);	// P
            setValidFrom (new Timestamp(System.currentTimeMillis()));
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_OrgAssignment (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27513505512789L;
    /** Last Updated Timestamp 2009-01-07 18:03:16.0 */
    public static final long updatedMS = 1231380196000L;
    /** AD_Table_ID=585 */
    public static final int Table_ID=585;
    
    /** TableName=C_OrgAssignment */
    public static final String Table_Name="C_OrgAssignment";
    
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
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Org Assignment.
    @param C_OrgAssignment_ID Assignment to (transaction) Organization */
    public void setC_OrgAssignment_ID (int C_OrgAssignment_ID)
    {
        if (C_OrgAssignment_ID < 1) throw new IllegalArgumentException ("C_OrgAssignment_ID is mandatory.");
        set_ValueNoCheck ("C_OrgAssignment_ID", Integer.valueOf(C_OrgAssignment_ID));
        
    }
    
    /** Get Org Assignment.
    @return Assignment to (transaction) Organization */
    public int getC_OrgAssignment_ID() 
    {
        return get_ValueAsInt("C_OrgAssignment_ID");
        
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
    
    /** Matrix = M */
    public static final String ORGASSIGNMENTTYPE_Matrix = X_Ref_OrgAssignmentType.MATRIX.getValue();
    /** Other = O */
    public static final String ORGASSIGNMENTTYPE_Other = X_Ref_OrgAssignmentType.OTHER.getValue();
    /** Primary = P */
    public static final String ORGASSIGNMENTTYPE_Primary = X_Ref_OrgAssignmentType.PRIMARY.getValue();
    /** Secondary = S */
    public static final String ORGASSIGNMENTTYPE_Secondary = X_Ref_OrgAssignmentType.SECONDARY.getValue();
    /** Temporary = T */
    public static final String ORGASSIGNMENTTYPE_Temporary = X_Ref_OrgAssignmentType.TEMPORARY.getValue();
    /** Set Assignment Type.
    @param OrgAssignmentType Organization Assignment Type */
    public void setOrgAssignmentType (String OrgAssignmentType)
    {
        if (OrgAssignmentType == null) throw new IllegalArgumentException ("OrgAssignmentType is mandatory");
        if (!X_Ref_OrgAssignmentType.isValid(OrgAssignmentType))
        throw new IllegalArgumentException ("OrgAssignmentType Invalid value - " + OrgAssignmentType + " - Reference_ID=401 - M - O - P - S - T");
        set_Value ("OrgAssignmentType", OrgAssignmentType);
        
    }
    
    /** Get Assignment Type.
    @return Organization Assignment Type */
    public String getOrgAssignmentType() 
    {
        return (String)get_Value("OrgAssignmentType");
        
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
