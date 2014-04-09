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
/** Generated Model for B_Topic
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_B_Topic.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_B_Topic extends PO
{
    /** Standard Constructor
    @param ctx context
    @param B_Topic_ID id
    @param trx transaction
    */
    public X_B_Topic (Ctx ctx, int B_Topic_ID, Trx trx)
    {
        super (ctx, B_Topic_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (B_Topic_ID == 0)
        {
            setB_TopicCategory_ID (0);
            setB_TopicType_ID (0);
            setB_Topic_ID (0);
            setDecisionDate (new Timestamp(System.currentTimeMillis()));
            setDocumentNo (null);
            setIsPublished (false);
            setName (null);
            setProcessed (false);	// N
            setTopicAction (null);
            setTopicStatus (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_B_Topic (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=679 */
    public static final int Table_ID=679;
    
    /** TableName=B_Topic */
    public static final String Table_Name="B_Topic";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Topic Category.
    @param B_TopicCategory_ID Auction Topic Category */
    public void setB_TopicCategory_ID (int B_TopicCategory_ID)
    {
        if (B_TopicCategory_ID < 1) throw new IllegalArgumentException ("B_TopicCategory_ID is mandatory.");
        set_ValueNoCheck ("B_TopicCategory_ID", Integer.valueOf(B_TopicCategory_ID));
        
    }
    
    /** Get Topic Category.
    @return Auction Topic Category */
    public int getB_TopicCategory_ID() 
    {
        return get_ValueAsInt("B_TopicCategory_ID");
        
    }
    
    /** Set Topic Type.
    @param B_TopicType_ID Auction Topic Type */
    public void setB_TopicType_ID (int B_TopicType_ID)
    {
        if (B_TopicType_ID < 1) throw new IllegalArgumentException ("B_TopicType_ID is mandatory.");
        set_ValueNoCheck ("B_TopicType_ID", Integer.valueOf(B_TopicType_ID));
        
    }
    
    /** Get Topic Type.
    @return Auction Topic Type */
    public int getB_TopicType_ID() 
    {
        return get_ValueAsInt("B_TopicType_ID");
        
    }
    
    /** Set Topic.
    @param B_Topic_ID Auction Topic */
    public void setB_Topic_ID (int B_Topic_ID)
    {
        if (B_Topic_ID < 1) throw new IllegalArgumentException ("B_Topic_ID is mandatory.");
        set_ValueNoCheck ("B_Topic_ID", Integer.valueOf(B_Topic_ID));
        
    }
    
    /** Get Topic.
    @return Auction Topic */
    public int getB_Topic_ID() 
    {
        return get_ValueAsInt("B_Topic_ID");
        
    }
    
    /** Set Decision date.
    @param DecisionDate Decision date */
    public void setDecisionDate (Timestamp DecisionDate)
    {
        if (DecisionDate == null) throw new IllegalArgumentException ("DecisionDate is mandatory.");
        set_Value ("DecisionDate", DecisionDate);
        
    }
    
    /** Get Decision date.
    @return Decision date */
    public Timestamp getDecisionDate() 
    {
        return (Timestamp)get_Value("DecisionDate");
        
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
    
    /** Set Published.
    @param IsPublished The entry is published and can be viewed */
    public void setIsPublished (boolean IsPublished)
    {
        set_Value ("IsPublished", Boolean.valueOf(IsPublished));
        
    }
    
    /** Get Published.
    @return The entry is published and can be viewed */
    public boolean isPublished() 
    {
        return get_ValueAsBoolean("IsPublished");
        
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
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    /** Set Details.
    @param TextDetails Details */
    public void setTextDetails (String TextDetails)
    {
        set_Value ("TextDetails", TextDetails);
        
    }
    
    /** Get Details.
    @return Details */
    public String getTextDetails() 
    {
        return (String)get_Value("TextDetails");
        
    }
    
    /** Set Text Message.
    @param TextMsg Text Message */
    public void setTextMsg (String TextMsg)
    {
        set_Value ("TextMsg", TextMsg);
        
    }
    
    /** Get Text Message.
    @return Text Message */
    public String getTextMsg() 
    {
        return (String)get_Value("TextMsg");
        
    }
    
    /** Set Topic Action.
    @param TopicAction Topic Action */
    public void setTopicAction (String TopicAction)
    {
        if (TopicAction == null) throw new IllegalArgumentException ("TopicAction is mandatory.");
        set_Value ("TopicAction", TopicAction);
        
    }
    
    /** Get Topic Action.
    @return Topic Action */
    public String getTopicAction() 
    {
        return (String)get_Value("TopicAction");
        
    }
    
    /** Set Topic Status.
    @param TopicStatus Topic Status */
    public void setTopicStatus (String TopicStatus)
    {
        if (TopicStatus == null) throw new IllegalArgumentException ("TopicStatus is mandatory.");
        set_Value ("TopicStatus", TopicStatus);
        
    }
    
    /** Get Topic Status.
    @return Topic Status */
    public String getTopicStatus() 
    {
        return (String)get_Value("TopicStatus");
        
    }
    
    
}
