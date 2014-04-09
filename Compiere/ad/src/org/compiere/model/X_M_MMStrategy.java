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
/** Generated Model for M_MMStrategy
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_MMStrategy.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_MMStrategy extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_MMStrategy_ID id
    @param trx transaction
    */
    public X_M_MMStrategy (Ctx ctx, int M_MMStrategy_ID, Trx trx)
    {
        super (ctx, M_MMStrategy_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_MMStrategy_ID == 0)
        {
            setIsAllowSplit (false);	// N
            setMMType (null);
            setM_MMStrategy_ID (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_MMStrategy (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27503840363789L;
    /** Last Updated Timestamp 2008-09-17 22:17:27.0 */
    public static final long updatedMS = 1221715047000L;
    /** AD_Table_ID=1038 */
    public static final int Table_ID=1038;
    
    /** TableName=M_MMStrategy */
    public static final String Table_Name="M_MMStrategy";
    
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
    
    /** Set Allow Splitting.
    @param IsAllowSplit Indicates if multiple pick or putaway tasks can be created for a single order line */
    public void setIsAllowSplit (boolean IsAllowSplit)
    {
        set_Value ("IsAllowSplit", Boolean.valueOf(IsAllowSplit));
        
    }
    
    /** Get Allow Splitting.
    @return Indicates if multiple pick or putaway tasks can be created for a single order line */
    public boolean isAllowSplit() 
    {
        return get_ValueAsBoolean("IsAllowSplit");
        
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
    
    /** Set Warehouse Management Strategy.
    @param M_MMStrategy_ID Sequential group of rules used for picking or putaway */
    public void setM_MMStrategy_ID (int M_MMStrategy_ID)
    {
        if (M_MMStrategy_ID < 1) throw new IllegalArgumentException ("M_MMStrategy_ID is mandatory.");
        set_ValueNoCheck ("M_MMStrategy_ID", Integer.valueOf(M_MMStrategy_ID));
        
    }
    
    /** Get Warehouse Management Strategy.
    @return Sequential group of rules used for picking or putaway */
    public int getM_MMStrategy_ID() 
    {
        return get_ValueAsInt("M_MMStrategy_ID");
        
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
