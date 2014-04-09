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
/** Generated Model for M_PriceList_Version
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_PriceList_Version.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_PriceList_Version extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_PriceList_Version_ID id
    @param trx transaction
    */
    public X_M_PriceList_Version (Ctx ctx, int M_PriceList_Version_ID, Trx trx)
    {
        super (ctx, M_PriceList_Version_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_PriceList_Version_ID == 0)
        {
            setM_DiscountSchema_ID (0);
            setM_PriceList_ID (0);
            setM_PriceList_Version_ID (0);
            setName (null);	// @#Date@
            setValidFrom (new Timestamp(System.currentTimeMillis()));	// @#Date@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_PriceList_Version (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27509346704789L;
    /** Last Updated Timestamp 2008-11-20 14:49:48.0 */
    public static final long updatedMS = 1227221388000L;
    /** AD_Table_ID=295 */
    public static final int Table_ID=295;
    
    /** TableName=M_PriceList_Version */
    public static final String Table_Name="M_PriceList_Version";
    
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
    
    /** Set Discount Schema.
    @param M_DiscountSchema_ID Schema to calculate price lists or the trade discount percentage */
    public void setM_DiscountSchema_ID (int M_DiscountSchema_ID)
    {
        if (M_DiscountSchema_ID < 1) throw new IllegalArgumentException ("M_DiscountSchema_ID is mandatory.");
        set_Value ("M_DiscountSchema_ID", Integer.valueOf(M_DiscountSchema_ID));
        
    }
    
    /** Get Discount Schema.
    @return Schema to calculate price lists or the trade discount percentage */
    public int getM_DiscountSchema_ID() 
    {
        return get_ValueAsInt("M_DiscountSchema_ID");
        
    }
    
    /** Set Price List.
    @param M_PriceList_ID Unique identifier of a Price List */
    public void setM_PriceList_ID (int M_PriceList_ID)
    {
        if (M_PriceList_ID < 1) throw new IllegalArgumentException ("M_PriceList_ID is mandatory.");
        set_ValueNoCheck ("M_PriceList_ID", Integer.valueOf(M_PriceList_ID));
        
    }
    
    /** Get Price List.
    @return Unique identifier of a Price List */
    public int getM_PriceList_ID() 
    {
        return get_ValueAsInt("M_PriceList_ID");
        
    }
    
    /** Set Price List Version.
    @param M_PriceList_Version_ID Identifies a unique instance of a Price List */
    public void setM_PriceList_Version_ID (int M_PriceList_Version_ID)
    {
        if (M_PriceList_Version_ID < 1) throw new IllegalArgumentException ("M_PriceList_Version_ID is mandatory.");
        set_ValueNoCheck ("M_PriceList_Version_ID", Integer.valueOf(M_PriceList_Version_ID));
        
    }
    
    /** Get Price List Version.
    @return Identifies a unique instance of a Price List */
    public int getM_PriceList_Version_ID() 
    {
        return get_ValueAsInt("M_PriceList_Version_ID");
        
    }
    
    /** Set Base Price List.
    @param M_Pricelist_Version_Base_ID Source for Price list calculations */
    public void setM_Pricelist_Version_Base_ID (int M_Pricelist_Version_Base_ID)
    {
        if (M_Pricelist_Version_Base_ID <= 0) set_Value ("M_Pricelist_Version_Base_ID", null);
        else
        set_Value ("M_Pricelist_Version_Base_ID", Integer.valueOf(M_Pricelist_Version_Base_ID));
        
    }
    
    /** Get Base Price List.
    @return Source for Price list calculations */
    public int getM_Pricelist_Version_Base_ID() 
    {
        return get_ValueAsInt("M_Pricelist_Version_Base_ID");
        
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
    
    /** Set Create.
    @param ProcCreate Create */
    public void setProcCreate (String ProcCreate)
    {
        set_Value ("ProcCreate", ProcCreate);
        
    }
    
    /** Get Create.
    @return Create */
    public String getProcCreate() 
    {
        return (String)get_Value("ProcCreate");
        
    }
    
    /** Set Valid from.
    @param ValidFrom Valid from including this date (first day) */
    public void setValidFrom (Timestamp ValidFrom)
    {
        if (ValidFrom == null) throw new IllegalArgumentException ("ValidFrom is mandatory.");
        set_Value ("ValidFrom", ValidFrom);
        
    }
    
    /** Get Valid from.
    @return Valid from including this date (first day) */
    public Timestamp getValidFrom() 
    {
        return (Timestamp)get_Value("ValidFrom");
        
    }
    
    
}
