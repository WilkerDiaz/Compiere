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
/** Generated Model for CM_CStage_Element
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_CM_CStage_Element.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_CM_CStage_Element extends PO
{
    /** Standard Constructor
    @param ctx context
    @param CM_CStage_Element_ID id
    @param trx transaction
    */
    public X_CM_CStage_Element (Ctx ctx, int CM_CStage_Element_ID, Trx trx)
    {
        super (ctx, CM_CStage_Element_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (CM_CStage_Element_ID == 0)
        {
            setCM_CStage_Element_ID (0);
            setCM_CStage_ID (0);
            setIsValid (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_CM_CStage_Element (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=867 */
    public static final int Table_ID=867;
    
    /** TableName=CM_CStage_Element */
    public static final String Table_Name="CM_CStage_Element";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Container Stage Element.
    @param CM_CStage_Element_ID Container element i.e. Headline, Content, Footer etc. */
    public void setCM_CStage_Element_ID (int CM_CStage_Element_ID)
    {
        if (CM_CStage_Element_ID < 1) throw new IllegalArgumentException ("CM_CStage_Element_ID is mandatory.");
        set_ValueNoCheck ("CM_CStage_Element_ID", Integer.valueOf(CM_CStage_Element_ID));
        
    }
    
    /** Get Container Stage Element.
    @return Container element i.e. Headline, Content, Footer etc. */
    public int getCM_CStage_Element_ID() 
    {
        return get_ValueAsInt("CM_CStage_Element_ID");
        
    }
    
    /** Set Web Container Stage.
    @param CM_CStage_ID Web Container Stage contains the staging content like images, text etc. */
    public void setCM_CStage_ID (int CM_CStage_ID)
    {
        if (CM_CStage_ID < 1) throw new IllegalArgumentException ("CM_CStage_ID is mandatory.");
        set_ValueNoCheck ("CM_CStage_ID", Integer.valueOf(CM_CStage_ID));
        
    }
    
    /** Get Web Container Stage.
    @return Web Container Stage contains the staging content like images, text etc. */
    public int getCM_CStage_ID() 
    {
        return get_ValueAsInt("CM_CStage_ID");
        
    }
    
    /** Set Content HTML.
    @param ContentHTML Contains the content itself */
    public void setContentHTML (String ContentHTML)
    {
        set_Value ("ContentHTML", ContentHTML);
        
    }
    
    /** Get Content HTML.
    @return Contains the content itself */
    public String getContentHTML() 
    {
        return (String)get_Value("ContentHTML");
        
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
    
    /** Set Valid.
    @param IsValid Element is valid */
    public void setIsValid (boolean IsValid)
    {
        set_Value ("IsValid", Boolean.valueOf(IsValid));
        
    }
    
    /** Get Valid.
    @return Element is valid */
    public boolean isValid() 
    {
        return get_ValueAsBoolean("IsValid");
        
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
