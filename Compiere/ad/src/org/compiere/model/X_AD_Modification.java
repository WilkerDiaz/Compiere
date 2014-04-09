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
/** Generated Model for AD_Modification
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Modification.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Modification extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Modification_ID id
    @param trx transaction
    */
    public X_AD_Modification (Ctx ctx, int AD_Modification_ID, Trx trx)
    {
        super (ctx, AD_Modification_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Modification_ID == 0)
        {
            setAD_Modification_ID (0);
            setAD_Version_ID (0);
            setModificationType (null);
            setName (null);
            setSeqNo (0);	// @SQL=SELECT COALESCE(MAX(SeqNo),0)+10 FROM AD_Modification WHERE AD_Version_ID='@AD_Version_ID@'
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Modification (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=883 */
    public static final int Table_ID=883;
    
    /** TableName=AD_Modification */
    public static final String Table_Name="AD_Modification";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Modification.
    @param AD_Modification_ID System Modification or Extension */
    public void setAD_Modification_ID (int AD_Modification_ID)
    {
        if (AD_Modification_ID < 1) throw new IllegalArgumentException ("AD_Modification_ID is mandatory.");
        set_ValueNoCheck ("AD_Modification_ID", Integer.valueOf(AD_Modification_ID));
        
    }
    
    /** Get Modification.
    @return System Modification or Extension */
    public int getAD_Modification_ID() 
    {
        return get_ValueAsInt("AD_Modification_ID");
        
    }
    
    /** Set Entity Version.
    @param AD_Version_ID Entity Version */
    public void setAD_Version_ID (int AD_Version_ID)
    {
        if (AD_Version_ID < 1) throw new IllegalArgumentException ("AD_Version_ID is mandatory.");
        set_ValueNoCheck ("AD_Version_ID", Integer.valueOf(AD_Version_ID));
        
    }
    
    /** Get Entity Version.
    @return Entity Version */
    public int getAD_Version_ID() 
    {
        return get_ValueAsInt("AD_Version_ID");
        
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
    
    /** Functionality Improvement = 1 */
    public static final String MODIFICATIONTYPE_FunctionalityImprovement = X_Ref_AD_Modification_Type.FUNCTIONALITY_IMPROVEMENT.getValue();
    /** Technology Improvement = 2 */
    public static final String MODIFICATIONTYPE_TechnologyImprovement = X_Ref_AD_Modification_Type.TECHNOLOGY_IMPROVEMENT.getValue();
    /** Business Process Improvement = 3 */
    public static final String MODIFICATIONTYPE_BusinessProcessImprovement = X_Ref_AD_Modification_Type.BUSINESS_PROCESS_IMPROVEMENT.getValue();
    /** Bug Fix = 9 */
    public static final String MODIFICATIONTYPE_BugFix = X_Ref_AD_Modification_Type.BUG_FIX.getValue();
    /** Set Modification Type.
    @param ModificationType Type of Modification */
    public void setModificationType (String ModificationType)
    {
        if (ModificationType == null) throw new IllegalArgumentException ("ModificationType is mandatory");
        if (!X_Ref_AD_Modification_Type.isValid(ModificationType))
        throw new IllegalArgumentException ("ModificationType Invalid value - " + ModificationType + " - Reference_ID=429 - 1 - 2 - 3 - 9");
        set_Value ("ModificationType", ModificationType);
        
    }
    
    /** Get Modification Type.
    @return Type of Modification */
    public String getModificationType() 
    {
        return (String)get_Value("ModificationType");
        
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
    
    /** Set Sequence.
    @param SeqNo Method of ordering elements;
     lowest number comes first */
    public void setSeqNo (int SeqNo)
    {
        set_Value ("SeqNo", Integer.valueOf(SeqNo));
        
    }
    
    /** Get Sequence.
    @return Method of ordering elements;
     lowest number comes first */
    public int getSeqNo() 
    {
        return get_ValueAsInt("SeqNo");
        
    }
    
    
}
