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
/** Generated Model for M_ABCProductAssignment
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.2 Dev - $Id: GenerateModel.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_ABCProductAssignment extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_ABCProductAssignment_ID id
    @param trx transaction
    */
    public X_M_ABCProductAssignment (Ctx ctx, int M_ABCProductAssignment_ID, Trx trx)
    {
        super (ctx, M_ABCProductAssignment_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_ABCProductAssignment_ID == 0)
        {
            setIsCounted (false);
            setM_ABCAnalysisGroup_ID (0);
            setM_ABCProductAssignment_ID (0);
            setM_ABCRank_ID (0);
            setM_Product_ID (0);
            setM_Warehouse_ID (0);	// @M_Warehouse_ID@
            setProcessing (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_ABCProductAssignment (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27548385677789L;
    /** Last Updated Timestamp 2010-02-16 00:29:21.0 */
    public static final long updatedMS = 1266260361000L;
    /** AD_Table_ID=2152 */
    public static final int Table_ID=2152;
    
    /** TableName=M_ABCProductAssignment */
    public static final String Table_Name="M_ABCProductAssignment";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Date Last Counted.
    @param DateLastCounted Date Last Counted */
    public void setDateLastCounted (Timestamp DateLastCounted)
    {
        set_ValueNoCheck ("DateLastCounted", DateLastCounted);
        
    }
    
    /** Get Date Last Counted.
    @return Date Last Counted */
    public Timestamp getDateLastCounted() 
    {
        return (Timestamp)get_Value("DateLastCounted");
        
    }
    
    /** Set Date Next Count.
    @param DateNextCount Date on which next counting should be done */
    public void setDateNextCount (Timestamp DateNextCount)
    {
        set_ValueNoCheck ("DateNextCount", DateNextCount);
        
    }
    
    /** Get Date Next Count.
    @return Date on which next counting should be done */
    public Timestamp getDateNextCount() 
    {
        return (Timestamp)get_Value("DateNextCount");
        
    }
    
    /** Set Calculate Count (?).
    @param IsCounted Count number of not empty elements */
    public void setIsCounted (boolean IsCounted)
    {
        set_Value ("IsCounted", Boolean.valueOf(IsCounted));
        
    }
    
    /** Get Calculate Count (?).
    @return Count number of not empty elements */
    public boolean isCounted() 
    {
        return get_ValueAsBoolean("IsCounted");
        
    }
    
    /** Set M_ABCAnalysisGroup_ID.
    @param M_ABCAnalysisGroup_ID M_ABCAnalysisGroup_ID */
    public void setM_ABCAnalysisGroup_ID (int M_ABCAnalysisGroup_ID)
    {
        if (M_ABCAnalysisGroup_ID < 1) throw new IllegalArgumentException ("M_ABCAnalysisGroup_ID is mandatory.");
        set_Value ("M_ABCAnalysisGroup_ID", Integer.valueOf(M_ABCAnalysisGroup_ID));
        
    }
    
    /** Get M_ABCAnalysisGroup_ID.
    @return M_ABCAnalysisGroup_ID */
    public int getM_ABCAnalysisGroup_ID() 
    {
        return get_ValueAsInt("M_ABCAnalysisGroup_ID");
        
    }
    
    /** Set ABC Product Assignment.
    @param M_ABCProductAssignment_ID ABC Product Assignment */
    public void setM_ABCProductAssignment_ID (int M_ABCProductAssignment_ID)
    {
        if (M_ABCProductAssignment_ID < 1) throw new IllegalArgumentException ("M_ABCProductAssignment_ID is mandatory.");
        set_ValueNoCheck ("M_ABCProductAssignment_ID", Integer.valueOf(M_ABCProductAssignment_ID));
        
    }
    
    /** Get ABC Product Assignment.
    @return ABC Product Assignment */
    public int getM_ABCProductAssignment_ID() 
    {
        return get_ValueAsInt("M_ABCProductAssignment_ID");
        
    }
    
    /** Set Rank.
    @param M_ABCRank_ID Rank */
    public void setM_ABCRank_ID (int M_ABCRank_ID)
    {
        if (M_ABCRank_ID < 1) throw new IllegalArgumentException ("M_ABCRank_ID is mandatory.");
        set_Value ("M_ABCRank_ID", Integer.valueOf(M_ABCRank_ID));
        
    }
    
    /** Get Rank.
    @return Rank */
    public int getM_ABCRank_ID() 
    {
        return get_ValueAsInt("M_ABCRank_ID");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
        set_ValueNoCheck ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID < 1) throw new IllegalArgumentException ("M_Warehouse_ID is mandatory.");
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
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
