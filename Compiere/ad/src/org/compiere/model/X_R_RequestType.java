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
/** Generated Model for R_RequestType
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_R_RequestType.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_R_RequestType extends PO
{
    /** Standard Constructor
    @param ctx context
    @param R_RequestType_ID id
    @param trx transaction
    */
    public X_R_RequestType (Ctx ctx, int R_RequestType_ID, Trx trx)
    {
        super (ctx, R_RequestType_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (R_RequestType_ID == 0)
        {
            setConfidentialType (null);	// C
            setDueDateTolerance (0);	// 7
            setIsAutoChangeRequest (false);
            setIsConfidentialInfo (false);	// N
            setIsDefault (false);	// N
            setIsEMailWhenDue (false);
            setIsEMailWhenOverdue (false);
            setIsIndexed (false);
            setIsSelfService (true);	// Y
            setName (null);
            setR_RequestType_ID (0);
            setR_StatusCategory_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_R_RequestType (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=529 */
    public static final int Table_ID=529;
    
    /** TableName=R_RequestType */
    public static final String Table_Name="R_RequestType";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Auto Due Date Days.
    @param AutoDueDateDays Automatic Due Date Days */
    public void setAutoDueDateDays (int AutoDueDateDays)
    {
        set_Value ("AutoDueDateDays", Integer.valueOf(AutoDueDateDays));
        
    }
    
    /** Get Auto Due Date Days.
    @return Automatic Due Date Days */
    public int getAutoDueDateDays() 
    {
        return get_ValueAsInt("AutoDueDateDays");
        
    }
    
    /** Public Information = A */
    public static final String CONFIDENTIALTYPE_PublicInformation = X_Ref_R_Request_Confidential.PUBLIC_INFORMATION.getValue();
    /** Partner Confidential = C */
    public static final String CONFIDENTIALTYPE_PartnerConfidential = X_Ref_R_Request_Confidential.PARTNER_CONFIDENTIAL.getValue();
    /** Internal = I */
    public static final String CONFIDENTIALTYPE_Internal = X_Ref_R_Request_Confidential.INTERNAL.getValue();
    /** Private Information = P */
    public static final String CONFIDENTIALTYPE_PrivateInformation = X_Ref_R_Request_Confidential.PRIVATE_INFORMATION.getValue();
    /** Set Confidentiality.
    @param ConfidentialType Type of Confidentiality */
    public void setConfidentialType (String ConfidentialType)
    {
        if (ConfidentialType == null) throw new IllegalArgumentException ("ConfidentialType is mandatory");
        if (!X_Ref_R_Request_Confidential.isValid(ConfidentialType))
        throw new IllegalArgumentException ("ConfidentialType Invalid value - " + ConfidentialType + " - Reference_ID=340 - A - C - I - P");
        set_Value ("ConfidentialType", ConfidentialType);
        
    }
    
    /** Get Confidentiality.
    @return Type of Confidentiality */
    public String getConfidentialType() 
    {
        return (String)get_Value("ConfidentialType");
        
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
    
    /** Set Due Date Tolerance.
    @param DueDateTolerance Tolerance in days between the Date Next Action and the date the request is regarded as overdue */
    public void setDueDateTolerance (int DueDateTolerance)
    {
        set_Value ("DueDateTolerance", Integer.valueOf(DueDateTolerance));
        
    }
    
    /** Get Due Date Tolerance.
    @return Tolerance in days between the Date Next Action and the date the request is regarded as overdue */
    public int getDueDateTolerance() 
    {
        return get_ValueAsInt("DueDateTolerance");
        
    }
    
    /** Set Create Change Request.
    @param IsAutoChangeRequest Automatically create BOM (Engineering) Change Request */
    public void setIsAutoChangeRequest (boolean IsAutoChangeRequest)
    {
        set_Value ("IsAutoChangeRequest", Boolean.valueOf(IsAutoChangeRequest));
        
    }
    
    /** Get Create Change Request.
    @return Automatically create BOM (Engineering) Change Request */
    public boolean isAutoChangeRequest() 
    {
        return get_ValueAsBoolean("IsAutoChangeRequest");
        
    }
    
    /** Set Confidential Info.
    @param IsConfidentialInfo Can enter confidential information */
    public void setIsConfidentialInfo (boolean IsConfidentialInfo)
    {
        set_Value ("IsConfidentialInfo", Boolean.valueOf(IsConfidentialInfo));
        
    }
    
    /** Get Confidential Info.
    @return Can enter confidential information */
    public boolean isConfidentialInfo() 
    {
        return get_ValueAsBoolean("IsConfidentialInfo");
        
    }
    
    /** Set Default.
    @param IsDefault Default value */
    public void setIsDefault (boolean IsDefault)
    {
        set_Value ("IsDefault", Boolean.valueOf(IsDefault));
        
    }
    
    /** Get Default.
    @return Default value */
    public boolean isDefault() 
    {
        return get_ValueAsBoolean("IsDefault");
        
    }
    
    /** Set EMail when Due.
    @param IsEMailWhenDue Send EMail when Request becomes due */
    public void setIsEMailWhenDue (boolean IsEMailWhenDue)
    {
        set_Value ("IsEMailWhenDue", Boolean.valueOf(IsEMailWhenDue));
        
    }
    
    /** Get EMail when Due.
    @return Send EMail when Request becomes due */
    public boolean isEMailWhenDue() 
    {
        return get_ValueAsBoolean("IsEMailWhenDue");
        
    }
    
    /** Set EMail when Overdue.
    @param IsEMailWhenOverdue Send EMail when Request becomes overdue */
    public void setIsEMailWhenOverdue (boolean IsEMailWhenOverdue)
    {
        set_Value ("IsEMailWhenOverdue", Boolean.valueOf(IsEMailWhenOverdue));
        
    }
    
    /** Get EMail when Overdue.
    @return Send EMail when Request becomes overdue */
    public boolean isEMailWhenOverdue() 
    {
        return get_ValueAsBoolean("IsEMailWhenOverdue");
        
    }
    
    /** Set Indexed.
    @param IsIndexed Index the document for the internal search engine */
    public void setIsIndexed (boolean IsIndexed)
    {
        set_Value ("IsIndexed", Boolean.valueOf(IsIndexed));
        
    }
    
    /** Get Indexed.
    @return Index the document for the internal search engine */
    public boolean isIndexed() 
    {
        return get_ValueAsBoolean("IsIndexed");
        
    }
    
    /** Set Invoiced.
    @param IsInvoiced Is this invoiced? */
    public void setIsInvoiced (boolean IsInvoiced)
    {
        set_Value ("IsInvoiced", Boolean.valueOf(IsInvoiced));
        
    }
    
    /** Get Invoiced.
    @return Is this invoiced? */
    public boolean isInvoiced() 
    {
        return get_ValueAsBoolean("IsInvoiced");
        
    }
    
    /** Set Self-Service.
    @param IsSelfService This is a Self-Service entry or this entry can be changed via Self-Service */
    public void setIsSelfService (boolean IsSelfService)
    {
        set_Value ("IsSelfService", Boolean.valueOf(IsSelfService));
        
    }
    
    /** Get Self-Service.
    @return This is a Self-Service entry or this entry can be changed via Self-Service */
    public boolean isSelfService() 
    {
        return get_ValueAsBoolean("IsSelfService");
        
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
    
    /** Set Request Type.
    @param R_RequestType_ID Type of request (e.g. Inquiry, Complaint...) */
    public void setR_RequestType_ID (int R_RequestType_ID)
    {
        if (R_RequestType_ID < 1) throw new IllegalArgumentException ("R_RequestType_ID is mandatory.");
        set_ValueNoCheck ("R_RequestType_ID", Integer.valueOf(R_RequestType_ID));
        
    }
    
    /** Get Request Type.
    @return Type of request (e.g. Inquiry, Complaint...) */
    public int getR_RequestType_ID() 
    {
        return get_ValueAsInt("R_RequestType_ID");
        
    }
    
    /** Set Status Category.
    @param R_StatusCategory_ID Request Status Category */
    public void setR_StatusCategory_ID (int R_StatusCategory_ID)
    {
        if (R_StatusCategory_ID < 1) throw new IllegalArgumentException ("R_StatusCategory_ID is mandatory.");
        set_Value ("R_StatusCategory_ID", Integer.valueOf(R_StatusCategory_ID));
        
    }
    
    /** Get Status Category.
    @return Request Status Category */
    public int getR_StatusCategory_ID() 
    {
        return get_ValueAsInt("R_StatusCategory_ID");
        
    }
    
    
}
