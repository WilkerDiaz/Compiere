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
/** Generated Model for M_ABCAnalysisGroup
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: GenerateModel.java 8757 2010-05-12 21:32:32Z nnayak $ */
public class X_M_ABCAnalysisGroup extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_ABCAnalysisGroup_ID id
    @param trx transaction
    */
    public X_M_ABCAnalysisGroup (Ctx ctx, int M_ABCAnalysisGroup_ID, Trx trx)
    {
        super (ctx, M_ABCAnalysisGroup_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_ABCAnalysisGroup_ID == 0)
        {
            setC_DocType_ID (0);
            setDaysForAnalysis (0);
            setIsValid (false);
            setM_ABCAnalysisGroup_ID (0);
            setM_Warehouse_ID (0);
            setName (null);
            setProcessing (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_ABCAnalysisGroup (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27555181995789L;
    /** Last Updated Timestamp 2010-05-05 16:21:19.0 */
    public static final long updatedMS = 1273056679000L;
    /** AD_Table_ID=2156 */
    public static final int Table_ID=2156;
    
    /** TableName=M_ABCAnalysisGroup */
    public static final String Table_Name="M_ABCAnalysisGroup";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Document Type.
    @param C_DocType_ID Document type or rules */
    public void setC_DocType_ID (int C_DocType_ID)
    {
        if (C_DocType_ID < 0) throw new IllegalArgumentException ("C_DocType_ID is mandatory.");
        set_Value ("C_DocType_ID", Integer.valueOf(C_DocType_ID));
        
    }
    
    /** Get Document Type.
    @return Document type or rules */
    public int getC_DocType_ID() 
    {
        return get_ValueAsInt("C_DocType_ID");
        
    }
    
    /** Set Date Last Run.
    @param DateLastRun Date the process was last run. */
    public void setDateLastRun (Timestamp DateLastRun)
    {
        set_ValueNoCheck ("DateLastRun", DateLastRun);
        
    }
    
    /** Get Date Last Run.
    @return Date the process was last run. */
    public Timestamp getDateLastRun() 
    {
        return (Timestamp)get_Value("DateLastRun");
        
    }
    
    /** Set Days For Analysis.
    @param DaysForAnalysis Number of Days which will be considered for carrying out initial Analysis  */
    public void setDaysForAnalysis (int DaysForAnalysis)
    {
        set_Value ("DaysForAnalysis", Integer.valueOf(DaysForAnalysis));
        
    }
    
    /** Get Days For Analysis.
    @return Number of Days which will be considered for carrying out initial Analysis  */
    public int getDaysForAnalysis() 
    {
        return get_ValueAsInt("DaysForAnalysis");
        
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
    
    /** Set Analysis Group.
    @param M_ABCAnalysisGroup_ID Analysis Group */
    public void setM_ABCAnalysisGroup_ID (int M_ABCAnalysisGroup_ID)
    {
        if (M_ABCAnalysisGroup_ID < 1) throw new IllegalArgumentException ("M_ABCAnalysisGroup_ID is mandatory.");
        set_ValueNoCheck ("M_ABCAnalysisGroup_ID", Integer.valueOf(M_ABCAnalysisGroup_ID));
        
    }
    
    /** Get Analysis Group.
    @return Analysis Group */
    public int getM_ABCAnalysisGroup_ID() 
    {
        return get_ValueAsInt("M_ABCAnalysisGroup_ID");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID < 1) throw new IllegalArgumentException ("M_Warehouse_ID is mandatory.");
        set_ValueNoCheck ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
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
    
    
}
