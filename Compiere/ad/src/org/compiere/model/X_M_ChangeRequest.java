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
/** Generated Model for M_ChangeRequest
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_ChangeRequest.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_ChangeRequest extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_ChangeRequest_ID id
    @param trx transaction
    */
    public X_M_ChangeRequest (Ctx ctx, int M_ChangeRequest_ID, Trx trx)
    {
        super (ctx, M_ChangeRequest_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_ChangeRequest_ID == 0)
        {
            setDocumentNo (null);
            setIsApproved (false);	// N
            setM_BOM_ID (0);
            setM_ChangeRequest_ID (0);
            setName (null);
            setProcessed (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_ChangeRequest (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=800 */
    public static final int Table_ID=800;
    
    /** TableName=M_ChangeRequest */
    public static final String Table_Name="M_ChangeRequest";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Detail Information.
    @param DetailInfo Additional Detail Information */
    public void setDetailInfo (String DetailInfo)
    {
        set_Value ("DetailInfo", DetailInfo);
        
    }
    
    /** Get Detail Information.
    @return Additional Detail Information */
    public String getDetailInfo() 
    {
        return (String)get_Value("DetailInfo");
        
    }
    
    /** Set Document No.
    @param DocumentNo Document sequence number of the document */
    public void setDocumentNo (String DocumentNo)
    {
        if (DocumentNo == null) throw new IllegalArgumentException ("DocumentNo is mandatory.");
        set_Value ("DocumentNo", DocumentNo);
        
    }
    
    /** Get Document No.
    @return Document sequence number of the document */
    public String getDocumentNo() 
    {
        return (String)get_Value("DocumentNo");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getDocumentNo());
        
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
    
    /** Set Approved.
    @param IsApproved Indicates if this document requires approval */
    public void setIsApproved (boolean IsApproved)
    {
        set_Value ("IsApproved", Boolean.valueOf(IsApproved));
        
    }
    
    /** Get Approved.
    @return Indicates if this document requires approval */
    public boolean isApproved() 
    {
        return get_ValueAsBoolean("IsApproved");
        
    }
    
    /** Set BOM.
    @param M_BOM_ID Bill of Materials */
    public void setM_BOM_ID (int M_BOM_ID)
    {
        if (M_BOM_ID < 1) throw new IllegalArgumentException ("M_BOM_ID is mandatory.");
        set_ValueNoCheck ("M_BOM_ID", Integer.valueOf(M_BOM_ID));
        
    }
    
    /** Get BOM.
    @return Bill of Materials */
    public int getM_BOM_ID() 
    {
        return get_ValueAsInt("M_BOM_ID");
        
    }
    
    /** Set Change Notice.
    @param M_ChangeNotice_ID Bill of Materials (Engineering) Change Notice (Version) */
    public void setM_ChangeNotice_ID (int M_ChangeNotice_ID)
    {
        if (M_ChangeNotice_ID <= 0) set_ValueNoCheck ("M_ChangeNotice_ID", null);
        else
        set_ValueNoCheck ("M_ChangeNotice_ID", Integer.valueOf(M_ChangeNotice_ID));
        
    }
    
    /** Get Change Notice.
    @return Bill of Materials (Engineering) Change Notice (Version) */
    public int getM_ChangeNotice_ID() 
    {
        return get_ValueAsInt("M_ChangeNotice_ID");
        
    }
    
    /** Set Change Request.
    @param M_ChangeRequest_ID BOM (Engineering) Change Request */
    public void setM_ChangeRequest_ID (int M_ChangeRequest_ID)
    {
        if (M_ChangeRequest_ID < 1) throw new IllegalArgumentException ("M_ChangeRequest_ID is mandatory.");
        set_ValueNoCheck ("M_ChangeRequest_ID", Integer.valueOf(M_ChangeRequest_ID));
        
    }
    
    /** Get Change Request.
    @return BOM (Engineering) Change Request */
    public int getM_ChangeRequest_ID() 
    {
        return get_ValueAsInt("M_ChangeRequest_ID");
        
    }
    
    /** Set Fixed in.
    @param M_FixChangeNotice_ID Fixed in Change Notice */
    public void setM_FixChangeNotice_ID (int M_FixChangeNotice_ID)
    {
        if (M_FixChangeNotice_ID <= 0) set_ValueNoCheck ("M_FixChangeNotice_ID", null);
        else
        set_ValueNoCheck ("M_FixChangeNotice_ID", Integer.valueOf(M_FixChangeNotice_ID));
        
    }
    
    /** Get Fixed in.
    @return Fixed in Change Notice */
    public int getM_FixChangeNotice_ID() 
    {
        return get_ValueAsInt("M_FixChangeNotice_ID");
        
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
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    
}
