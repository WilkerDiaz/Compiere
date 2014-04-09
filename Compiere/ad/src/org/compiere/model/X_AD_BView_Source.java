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
/** Generated Model for AD_BView_Source
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_BView_Source.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_BView_Source extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_BView_Source_ID id
    @param trx transaction
    */
    public X_AD_BView_Source (Ctx ctx, int AD_BView_Source_ID, Trx trx)
    {
        super (ctx, AD_BView_Source_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_BView_Source_ID == 0)
        {
            setAD_BView_ID (0);
            setAD_BView_Source_ID (0);
            setIsDefault (true);	// Y
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_BView_Source (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27499073345789L;
    /** Last Updated Timestamp 2008-07-24 18:07:09.0 */
    public static final long updatedMS = 1216948029000L;
    /** AD_Table_ID=1049 */
    public static final int Table_ID=1049;
    
    /** TableName=AD_BView_Source */
    public static final String Table_Name="AD_BView_Source";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business View.
    @param AD_BView_ID The logical subset of related data for the purposes of reporting */
    public void setAD_BView_ID (int AD_BView_ID)
    {
        if (AD_BView_ID < 1) throw new IllegalArgumentException ("AD_BView_ID is mandatory.");
        set_ValueNoCheck ("AD_BView_ID", Integer.valueOf(AD_BView_ID));
        
    }
    
    /** Get Business View.
    @return The logical subset of related data for the purposes of reporting */
    public int getAD_BView_ID() 
    {
        return get_ValueAsInt("AD_BView_ID");
        
    }
    
    /** Set Business View Source.
    @param AD_BView_Source_ID Identifies the Source for this Business View */
    public void setAD_BView_Source_ID (int AD_BView_Source_ID)
    {
        if (AD_BView_Source_ID < 1) throw new IllegalArgumentException ("AD_BView_Source_ID is mandatory.");
        set_ValueNoCheck ("AD_BView_Source_ID", Integer.valueOf(AD_BView_Source_ID));
        
    }
    
    /** Get Business View Source.
    @return Identifies the Source for this Business View */
    public int getAD_BView_Source_ID() 
    {
        return get_ValueAsInt("AD_BView_Source_ID");
        
    }
    
    /** Set Process.
    @param AD_Process_ID Process or Report */
    public void setAD_Process_ID (int AD_Process_ID)
    {
        if (AD_Process_ID <= 0) set_Value ("AD_Process_ID", null);
        else
        set_Value ("AD_Process_ID", Integer.valueOf(AD_Process_ID));
        
    }
    
    /** Get Process.
    @return Process or Report */
    public int getAD_Process_ID() 
    {
        return get_ValueAsInt("AD_Process_ID");
        
    }
    
    /** Set Report View.
    @param AD_ReportView_ID View used to generate this report */
    public void setAD_ReportView_ID (int AD_ReportView_ID)
    {
        if (AD_ReportView_ID <= 0) set_Value ("AD_ReportView_ID", null);
        else
        set_Value ("AD_ReportView_ID", Integer.valueOf(AD_ReportView_ID));
        
    }
    
    /** Get Report View.
    @return View used to generate this report */
    public int getAD_ReportView_ID() 
    {
        return get_ValueAsInt("AD_ReportView_ID");
        
    }
    
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID <= 0) set_Value ("AD_Table_ID", null);
        else
        set_Value ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
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
    
    /** Set Entity Type.
    @param EntityType Dictionary Entity Type;
     Determines ownership and synchronization */
    public void setEntityType (String EntityType)
    {
        set_Value ("EntityType", EntityType);
        
    }
    
    /** Get Entity Type.
    @return Dictionary Entity Type;
     Determines ownership and synchronization */
    public String getEntityType() 
    {
        return (String)get_Value("EntityType");
        
    }
    
    /** Set Import Fields.
    @param ImportFields Create Fields from Table Columns */
    public void setImportFields (String ImportFields)
    {
        set_Value ("ImportFields", ImportFields);
        
    }
    
    /** Get Import Fields.
    @return Create Fields from Table Columns */
    public String getImportFields() 
    {
        return (String)get_Value("ImportFields");
        
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
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
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
    
    /** Table or View = 1 */
    public static final String SOURCETYPE_TableOrView = X_Ref_AD_BView_SourceType.TABLE_OR_VIEW.getValue();
    /** Report View = 2 */
    public static final String SOURCETYPE_ReportView = X_Ref_AD_BView_SourceType.REPORT_VIEW.getValue();
    /** Report & Process = 3 */
    public static final String SOURCETYPE_ReportProcess = X_Ref_AD_BView_SourceType.REPORT_PROCESS.getValue();
    /** Set SourceType.
    @param SourceType SourceType */
    public void setSourceType (String SourceType)
    {
        if (!X_Ref_AD_BView_SourceType.isValid(SourceType))
        throw new IllegalArgumentException ("SourceType Invalid value - " + SourceType + " - Reference_ID=457 - 1 - 2 - 3");
        set_Value ("SourceType", SourceType);
        
    }
    
    /** Get SourceType.
    @return SourceType */
    public String getSourceType() 
    {
        return (String)get_Value("SourceType");
        
    }
    
    /** Set DB Table Name.
    @param TableName Name of the table in the database */
    public void setTableName (String TableName)
    {
        set_Value ("TableName", TableName);
        
    }
    
    /** Get DB Table Name.
    @return Name of the table in the database */
    public String getTableName() 
    {
        return (String)get_Value("TableName");
        
    }
    
    
}
