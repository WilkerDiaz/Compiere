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
/** Generated Model for AD_BView_ReportLayer
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_BView_ReportLayer.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_BView_ReportLayer extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_BView_ReportLayer_ID id
    @param trx transaction
    */
    public X_AD_BView_ReportLayer (Ctx ctx, int AD_BView_ReportLayer_ID, Trx trx)
    {
        super (ctx, AD_BView_ReportLayer_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_BView_ReportLayer_ID == 0)
        {
            setAD_BView_ID (0);
            setAD_BView_ReportLayer_ID (0);
            setDB_View_Name (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_BView_ReportLayer (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27503803143789L;
    /** Last Updated Timestamp 2008-09-17 11:57:07.0 */
    public static final long updatedMS = 1221677827000L;
    /** AD_Table_ID=1051 */
    public static final int Table_ID=1051;
    
    /** TableName=AD_BView_ReportLayer */
    public static final String Table_Name="AD_BView_ReportLayer";
    
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
    
    /** Set Export.
    @param AD_BView_ReportLayer_ID Export */
    public void setAD_BView_ReportLayer_ID (int AD_BView_ReportLayer_ID)
    {
        if (AD_BView_ReportLayer_ID < 1) throw new IllegalArgumentException ("AD_BView_ReportLayer_ID is mandatory.");
        set_ValueNoCheck ("AD_BView_ReportLayer_ID", Integer.valueOf(AD_BView_ReportLayer_ID));
        
    }
    
    /** Get Export.
    @return Export */
    public int getAD_BView_ReportLayer_ID() 
    {
        return get_ValueAsInt("AD_BView_ReportLayer_ID");
        
    }
    
    /** Set CreateProcess.
    @param CreateProcess CreateProcess */
    public void setCreateProcess (boolean CreateProcess)
    {
        set_Value ("CreateProcess", Boolean.valueOf(CreateProcess));
        
    }
    
    /** Get CreateProcess.
    @return CreateProcess */
    public boolean isCreateProcess() 
    {
        return get_ValueAsBoolean("CreateProcess");
        
    }
    
    /** Set DB View Name.
    @param DB_View_Name DB View Name */
    public void setDB_View_Name (String DB_View_Name)
    {
        if (DB_View_Name == null) throw new IllegalArgumentException ("DB_View_Name is mandatory.");
        set_Value ("DB_View_Name", DB_View_Name);
        
    }
    
    /** Get DB View Name.
    @return DB View Name */
    public String getDB_View_Name() 
    {
        return (String)get_Value("DB_View_Name");
        
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
    
    /** Set Publish.
    @param Publish Publish */
    public void setPublish (String Publish)
    {
        set_Value ("Publish", Publish);
        
    }
    
    /** Get Publish.
    @return Publish */
    public String getPublish() 
    {
        return (String)get_Value("Publish");
        
    }
    
    
}
