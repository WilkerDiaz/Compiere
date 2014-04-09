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
/** Generated Model for M_PriceList
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_PriceList.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_PriceList extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_PriceList_ID id
    @param trx transaction
    */
    public X_M_PriceList (Ctx ctx, int M_PriceList_ID, Trx trx)
    {
        super (ctx, M_PriceList_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_PriceList_ID == 0)
        {
            setC_Currency_ID (0);	// @$C_Currency_ID@
            setEnforcePriceLimit (false);
            setIsDefault (false);
            setIsSOPriceList (false);
            setIsTaxIncluded (false);
            setM_PriceList_ID (0);
            setName (null);
            setPricePrecision (0);	// 2
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_PriceList (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27509346685789L;
    /** Last Updated Timestamp 2008-11-20 14:49:29.0 */
    public static final long updatedMS = 1227221369000L;
    /** AD_Table_ID=255 */
    public static final int Table_ID=255;
    
    /** TableName=M_PriceList */
    public static final String Table_Name="M_PriceList";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Base Pricelist.
    @param BasePriceList_ID Pricelist to be used, if product not found on this pricelist */
    public void setBasePriceList_ID (int BasePriceList_ID)
    {
        if (BasePriceList_ID <= 0) set_Value ("BasePriceList_ID", null);
        else
        set_Value ("BasePriceList_ID", Integer.valueOf(BasePriceList_ID));
        
    }
    
    /** Get Base Pricelist.
    @return Pricelist to be used, if product not found on this pricelist */
    public int getBasePriceList_ID() 
    {
        return get_ValueAsInt("BasePriceList_ID");
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID < 1) throw new IllegalArgumentException ("C_Currency_ID is mandatory.");
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
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
    
    /** Set Enforce price limit.
    @param EnforcePriceLimit Do not allow prices below the limit price */
    public void setEnforcePriceLimit (boolean EnforcePriceLimit)
    {
        set_Value ("EnforcePriceLimit", Boolean.valueOf(EnforcePriceLimit));
        
    }
    
    /** Get Enforce price limit.
    @return Do not allow prices below the limit price */
    public boolean isEnforcePriceLimit() 
    {
        return get_ValueAsBoolean("EnforcePriceLimit");
        
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
    
    /** Set Sales Price list.
    @param IsSOPriceList This is a Sales Price List */
    public void setIsSOPriceList (boolean IsSOPriceList)
    {
        set_Value ("IsSOPriceList", Boolean.valueOf(IsSOPriceList));
        
    }
    
    /** Get Sales Price list.
    @return This is a Sales Price List */
    public boolean isSOPriceList() 
    {
        return get_ValueAsBoolean("IsSOPriceList");
        
    }
    
    /** Set Price includes Tax.
    @param IsTaxIncluded Tax is included in the price */
    public void setIsTaxIncluded (boolean IsTaxIncluded)
    {
        set_Value ("IsTaxIncluded", Boolean.valueOf(IsTaxIncluded));
        
    }
    
    /** Get Price includes Tax.
    @return Tax is included in the price */
    public boolean isTaxIncluded() 
    {
        return get_ValueAsBoolean("IsTaxIncluded");
        
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
    
    /** Set Price Precision.
    @param PricePrecision Precision (number of decimals) for the Price */
    public void setPricePrecision (int PricePrecision)
    {
        set_Value ("PricePrecision", Integer.valueOf(PricePrecision));
        
    }
    
    /** Get Price Precision.
    @return Precision (number of decimals) for the Price */
    public int getPricePrecision() 
    {
        return get_ValueAsInt("PricePrecision");
        
    }
    
    
}
