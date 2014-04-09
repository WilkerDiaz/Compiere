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
/** Generated Model for M_MMStrategySet
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_MMStrategySet.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_MMStrategySet extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_MMStrategySet_ID id
    @param trx transaction
    */
    public X_M_MMStrategySet (Ctx ctx, int M_MMStrategySet_ID, Trx trx)
    {
        super (ctx, M_MMStrategySet_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_MMStrategySet_ID == 0)
        {
            setIsEvaluateAllStrategies (false);
            setMMType (null);
            setM_MMStrategySet_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_MMStrategySet (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27503840390789L;
    /** Last Updated Timestamp 2008-09-17 22:17:54.0 */
    public static final long updatedMS = 1221715074000L;
    /** AD_Table_ID=1042 */
    public static final int Table_ID=1042;
    
    /** TableName=M_MMStrategySet */
    public static final String Table_Name="M_MMStrategySet";
    
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
    
    /** Set Evaluate All Strategies.
    @param IsEvaluateAllStrategies Evaluate all strategies until locators are found */
    public void setIsEvaluateAllStrategies (boolean IsEvaluateAllStrategies)
    {
        set_Value ("IsEvaluateAllStrategies", Boolean.valueOf(IsEvaluateAllStrategies));
        
    }
    
    /** Get Evaluate All Strategies.
    @return Evaluate all strategies until locators are found */
    public boolean isEvaluateAllStrategies() 
    {
        return get_ValueAsBoolean("IsEvaluateAllStrategies");
        
    }
    
    /** Material Picking = PCK */
    public static final String MMTYPE_MaterialPicking = X_Ref_TaskType.MATERIAL_PICKING.getValue();
    /** Material Putaway = PUT */
    public static final String MMTYPE_MaterialPutaway = X_Ref_TaskType.MATERIAL_PUTAWAY.getValue();
    /** Set Type.
    @param MMType Warehouse Management Rule Type */
    public void setMMType (String MMType)
    {
        if (MMType == null) throw new IllegalArgumentException ("MMType is mandatory");
        if (!X_Ref_TaskType.isValid(MMType))
        throw new IllegalArgumentException ("MMType Invalid value - " + MMType + " - Reference_ID=455 - PCK - PUT");
        set_Value ("MMType", MMType);
        
    }
    
    /** Get Type.
    @return Warehouse Management Rule Type */
    public String getMMType() 
    {
        return (String)get_Value("MMType");
        
    }
    
    /** Set Locator.
    @param M_Locator_ID Warehouse Locator */
    public void setM_Locator_ID (int M_Locator_ID)
    {
        if (M_Locator_ID <= 0) set_Value ("M_Locator_ID", null);
        else
        set_Value ("M_Locator_ID", Integer.valueOf(M_Locator_ID));
        
    }
    
    /** Get Locator.
    @return Warehouse Locator */
    public int getM_Locator_ID() 
    {
        return get_ValueAsInt("M_Locator_ID");
        
    }
    
    /** Set Material Management Strategy Set.
    @param M_MMStrategySet_ID Group of selection criteria to be used in the putaway and picking processes */
    public void setM_MMStrategySet_ID (int M_MMStrategySet_ID)
    {
        if (M_MMStrategySet_ID < 1) throw new IllegalArgumentException ("M_MMStrategySet_ID is mandatory.");
        set_ValueNoCheck ("M_MMStrategySet_ID", Integer.valueOf(M_MMStrategySet_ID));
        
    }
    
    /** Get Material Management Strategy Set.
    @return Group of selection criteria to be used in the putaway and picking processes */
    public int getM_MMStrategySet_ID() 
    {
        return get_ValueAsInt("M_MMStrategySet_ID");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
        else
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
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
    
    
}
