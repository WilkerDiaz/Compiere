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
/** Generated Model for M_ABCRankSort
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.2 Dev - $Id: GenerateModel.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_ABCRankSort extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_ABCRankSort_ID id
    @param trx transaction
    */
    public X_M_ABCRankSort (Ctx ctx, int M_ABCRankSort_ID, Trx trx)
    {
        super (ctx, M_ABCRankSort_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_ABCRankSort_ID == 0)
        {
            setIsBasedOnPriceList (false);
            setM_ABCRankSort_ID (0);
            setName (null);
            setSortOption (false);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_ABCRankSort (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27547788897789L;
    /** Last Updated Timestamp 2010-02-09 02:43:01.0 */
    public static final long updatedMS = 1265663581000L;
    /** AD_Table_ID=2149 */
    public static final int Table_ID=2149;
    
    /** TableName=M_ABCRankSort */
    public static final String Table_Name="M_ABCRankSort";
    
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
    
    /** Set Based on Price List.
    @param IsBasedOnPriceList Is the criteria based on price list */
    public void setIsBasedOnPriceList (boolean IsBasedOnPriceList)
    {
        set_Value ("IsBasedOnPriceList", Boolean.valueOf(IsBasedOnPriceList));
        
    }
    
    /** Get Based on Price List.
    @return Is the criteria based on price list */
    public boolean isBasedOnPriceList() 
    {
        return get_ValueAsBoolean("IsBasedOnPriceList");
        
    }
    
    /** Set Rank Sort Criteria.
    @param M_ABCRankSort_ID Rank Sort Criteria */
    public void setM_ABCRankSort_ID (int M_ABCRankSort_ID)
    {
        if (M_ABCRankSort_ID < 1) throw new IllegalArgumentException ("M_ABCRankSort_ID is mandatory.");
        set_ValueNoCheck ("M_ABCRankSort_ID", Integer.valueOf(M_ABCRankSort_ID));
        
    }
    
    /** Get Rank Sort Criteria.
    @return Rank Sort Criteria */
    public int getM_ABCRankSort_ID() 
    {
        return get_ValueAsInt("M_ABCRankSort_ID");
        
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
    
    /** Set Sort Class Name.
    @param SortClassName Sort Class Name */
    public void setSortClassName (String SortClassName)
    {
        set_Value ("SortClassName", SortClassName);
        
    }
    
    /** Get Sort Class Name.
    @return Sort Class Name */
    public String getSortClassName() 
    {
        return (String)get_Value("SortClassName");
        
    }
    
    /** Set Asc.
    @param SortOption Asc */
    public void setSortOption (boolean SortOption)
    {
        set_Value ("SortOption", Boolean.valueOf(SortOption));
        
    }
    
    /** Get Asc.
    @return Asc */
    public boolean isSortOption() 
    {
        return get_ValueAsBoolean("SortOption");
        
    }
    
    
}
