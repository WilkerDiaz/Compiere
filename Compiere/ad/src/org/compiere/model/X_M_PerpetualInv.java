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
/** Generated Model for M_PerpetualInv
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_PerpetualInv.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_PerpetualInv extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_PerpetualInv_ID id
    @param trx transaction
    */
    public X_M_PerpetualInv (Ctx ctx, int M_PerpetualInv_ID, Trx trx)
    {
        super (ctx, M_PerpetualInv_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_PerpetualInv_ID == 0)
        {
            setCountHighMovement (false);
            setDateNextRun (new Timestamp(System.currentTimeMillis()));
            setM_PerpetualInv_ID (0);
            setName (null);
            setNoInventoryCount (0);	// 1
            setNoProductCount (0);	// 1
            setNumberOfRuns (0);	// 1
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_PerpetualInv (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=342 */
    public static final int Table_ID=342;
    
    /** TableName=M_PerpetualInv */
    public static final String Table_Name="M_PerpetualInv";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Count high turnover items.
    @param CountHighMovement Count High Movement products */
    public void setCountHighMovement (boolean CountHighMovement)
    {
        set_Value ("CountHighMovement", Boolean.valueOf(CountHighMovement));
        
    }
    
    /** Get Count high turnover items.
    @return Count High Movement products */
    public boolean isCountHighMovement() 
    {
        return get_ValueAsBoolean("CountHighMovement");
        
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
    
    /** Set Date Next Run.
    @param DateNextRun Date the process will run next */
    public void setDateNextRun (Timestamp DateNextRun)
    {
        if (DateNextRun == null) throw new IllegalArgumentException ("DateNextRun is mandatory.");
        set_ValueNoCheck ("DateNextRun", DateNextRun);
        
    }
    
    /** Get Date Next Run.
    @return Date the process will run next */
    public Timestamp getDateNextRun() 
    {
        return (Timestamp)get_Value("DateNextRun");
        
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
    
    /** Set Perpetual Inventory.
    @param M_PerpetualInv_ID Rules for generating physical inventory */
    public void setM_PerpetualInv_ID (int M_PerpetualInv_ID)
    {
        if (M_PerpetualInv_ID < 1) throw new IllegalArgumentException ("M_PerpetualInv_ID is mandatory.");
        set_ValueNoCheck ("M_PerpetualInv_ID", Integer.valueOf(M_PerpetualInv_ID));
        
    }
    
    /** Get Perpetual Inventory.
    @return Rules for generating physical inventory */
    public int getM_PerpetualInv_ID() 
    {
        return get_ValueAsInt("M_PerpetualInv_ID");
        
    }
    
    /** Set Product Category.
    @param M_Product_Category_ID Category of a Product */
    public void setM_Product_Category_ID (int M_Product_Category_ID)
    {
        if (M_Product_Category_ID <= 0) set_Value ("M_Product_Category_ID", null);
        else
        set_Value ("M_Product_Category_ID", Integer.valueOf(M_Product_Category_ID));
        
    }
    
    /** Get Product Category.
    @return Category of a Product */
    public int getM_Product_Category_ID() 
    {
        return get_ValueAsInt("M_Product_Category_ID");
        
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
    
    /** Set Number of Inventory counts.
    @param NoInventoryCount Frequency of inventory counts per year */
    public void setNoInventoryCount (int NoInventoryCount)
    {
        set_Value ("NoInventoryCount", Integer.valueOf(NoInventoryCount));
        
    }
    
    /** Get Number of Inventory counts.
    @return Frequency of inventory counts per year */
    public int getNoInventoryCount() 
    {
        return get_ValueAsInt("NoInventoryCount");
        
    }
    
    /** Set Number of Product counts.
    @param NoProductCount Frequency of product counts per year */
    public void setNoProductCount (int NoProductCount)
    {
        set_Value ("NoProductCount", Integer.valueOf(NoProductCount));
        
    }
    
    /** Get Number of Product counts.
    @return Frequency of product counts per year */
    public int getNoProductCount() 
    {
        return get_ValueAsInt("NoProductCount");
        
    }
    
    /** Set Number of runs.
    @param NumberOfRuns Frequency of processing Perpetual Inventory */
    public void setNumberOfRuns (int NumberOfRuns)
    {
        set_Value ("NumberOfRuns", Integer.valueOf(NumberOfRuns));
        
    }
    
    /** Get Number of runs.
    @return Frequency of processing Perpetual Inventory */
    public int getNumberOfRuns() 
    {
        return get_ValueAsInt("NumberOfRuns");
        
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
