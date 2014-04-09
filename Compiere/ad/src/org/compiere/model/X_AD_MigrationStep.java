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
/** Generated Model for AD_MigrationStep
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_MigrationStep.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_MigrationStep extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_MigrationStep_ID id
    @param trx transaction
    */
    public X_AD_MigrationStep (Ctx ctx, int AD_MigrationStep_ID, Trx trx)
    {
        super (ctx, AD_MigrationStep_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_MigrationStep_ID == 0)
        {
            setAD_MigrationStep_ID (0);
            setAD_Version_ID (0);
            setIsOkToFail (true);	// Y
            setName (null);
            setSeqNo (0);	// @SQL=SELECT COALESCE(Max(SeqNo),0)+10 FROM AD_MigrationStep WHERE AD_Version_ID=@AD_Version_ID@
            setTimingType (null);	// B
            setType (null);	// S
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_MigrationStep (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27523777476789L;
    /** Last Updated Timestamp 2009-05-06 16:22:40.0 */
    public static final long updatedMS = 1241652160000L;
    /** AD_Table_ID=922 */
    public static final int Table_ID=922;
    
    /** TableName=AD_MigrationStep */
    public static final String Table_Name="AD_MigrationStep";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Migration Step.
    @param AD_MigrationStep_ID Migration Step */
    public void setAD_MigrationStep_ID (int AD_MigrationStep_ID)
    {
        if (AD_MigrationStep_ID < 1) throw new IllegalArgumentException ("AD_MigrationStep_ID is mandatory.");
        set_ValueNoCheck ("AD_MigrationStep_ID", Integer.valueOf(AD_MigrationStep_ID));
        
    }
    
    /** Get Migration Step.
    @return Migration Step */
    public int getAD_MigrationStep_ID() 
    {
        return get_ValueAsInt("AD_MigrationStep_ID");
        
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
    
    /** Set Classname.
    @param Classname Java Classname */
    public void setClassname (String Classname)
    {
        set_Value ("Classname", Classname);
        
    }
    
    /** Get Classname.
    @return Java Classname */
    public String getClassname() 
    {
        return (String)get_Value("Classname");
        
    }
    
    /** Set Code.
    @param Code Code to execute or to validate */
    public void setCode (String Code)
    {
        set_Value ("Code", Code);
        
    }
    
    /** Get Code.
    @return Code to execute or to validate */
    public String getCode() 
    {
        return (String)get_Value("Code");
        
    }
    
    /** IBM DB/2 = D */
    public static final String DBTYPE_IBMDB2 = X_Ref__Database_Type.IBMD_B2.getValue();
    /** Enterprise DB = E */
    public static final String DBTYPE_EnterpriseDB = X_Ref__Database_Type.ENTERPRISE_DB.getValue();
    /** Oracle = O */
    public static final String DBTYPE_Oracle = X_Ref__Database_Type.ORACLE.getValue();
    /** MS SQL Server = S */
    public static final String DBTYPE_MSSQLServer = X_Ref__Database_Type.MSSQL_SERVER.getValue();
    /** Set Database Type.
    @param DBType Database Type */
    public void setDBType (String DBType)
    {
        if (!X_Ref__Database_Type.isValid(DBType))
        throw new IllegalArgumentException ("DBType Invalid value - " + DBType + " - Reference_ID=426 - D - E - O - S");
        set_Value ("DBType", DBType);
        
    }
    
    /** Get Database Type.
    @return Database Type */
    public String getDBType() 
    {
        return (String)get_Value("DBType");
        
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
    
    /** Set OK to Fail.
    @param IsOkToFail It is OK for this step to fail */
    public void setIsOkToFail (boolean IsOkToFail)
    {
        set_Value ("IsOkToFail", Boolean.valueOf(IsOkToFail));
        
    }
    
    /** Get OK to Fail.
    @return It is OK for this step to fail */
    public boolean isOkToFail() 
    {
        return get_ValueAsBoolean("IsOkToFail");
        
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
    
    /** Before Structure = 1 */
    public static final String TIMINGTYPE_BeforeStructure = X_Ref_AD_MigrationStep_Timing_Type.BEFORE_STRUCTURE.getValue();
    /** Before Data = 3 */
    public static final String TIMINGTYPE_BeforeData = X_Ref_AD_MigrationStep_Timing_Type.BEFORE_DATA.getValue();
    /** After Data = 4 */
    public static final String TIMINGTYPE_AfterData = X_Ref_AD_MigrationStep_Timing_Type.AFTER_DATA.getValue();
    /** Set Timing Type.
    @param TimingType Migration Timing Type */
    public void setTimingType (String TimingType)
    {
        if (TimingType == null) throw new IllegalArgumentException ("TimingType is mandatory");
        if (!X_Ref_AD_MigrationStep_Timing_Type.isValid(TimingType))
        throw new IllegalArgumentException ("TimingType Invalid value - " + TimingType + " - Reference_ID=416 - 1 - 3 - 4");
        set_Value ("TimingType", TimingType);
        
    }
    
    /** Get Timing Type.
    @return Migration Timing Type */
    public String getTimingType() 
    {
        return (String)get_Value("TimingType");
        
    }
    
    /** Java Language = J */
    public static final String TYPE_JavaLanguage = X_Ref_AD_MigrationStep_Types.JAVA_LANGUAGE.getValue();
    /** SQL = S */
    public static final String TYPE_SQL = X_Ref_AD_MigrationStep_Types.SQL.getValue();
    /** Set Code Type.
    @param Type Type of Code/Validation (SQL, Java Script, Java Language) */
    public void setType (String Type)
    {
        if (Type == null) throw new IllegalArgumentException ("Type is mandatory");
        if (!X_Ref_AD_MigrationStep_Types.isValid(Type))
        throw new IllegalArgumentException ("Type Invalid value - " + Type + " - Reference_ID=514 - J - S");
        set_Value ("Type", Type);
        
    }
    
    /** Get Code Type.
    @return Type of Code/Validation (SQL, Java Script, Java Language) */
    public String getType() 
    {
        return (String)get_Value("Type");
        
    }
    
    
}
