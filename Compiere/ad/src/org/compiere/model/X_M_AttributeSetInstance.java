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
/** Generated Model for M_AttributeSetInstance
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_AttributeSetInstance.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_AttributeSetInstance extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_AttributeSetInstance_ID id
    @param trx transaction
    */
    public X_M_AttributeSetInstance (Ctx ctx, int M_AttributeSetInstance_ID, Trx trx)
    {
        super (ctx, M_AttributeSetInstance_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_AttributeSetInstance_ID == 0)
        {
            setM_AttributeSetInstance_ID (0);
            setM_AttributeSet_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_AttributeSetInstance (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=559 */
    public static final int Table_ID=559;
    
    /** TableName=M_AttributeSetInstance */
    public static final String Table_Name="M_AttributeSetInstance";
    
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
    
    /** Set Expiration Date.
    @param GuaranteeDate Date when guarantee expires */
    public void setGuaranteeDate (Timestamp GuaranteeDate)
    {
        set_Value ("GuaranteeDate", GuaranteeDate);
        
    }
    
    /** Get Expiration Date.
    @return Date when guarantee expires */
    public Timestamp getGuaranteeDate() 
    {
        return (Timestamp)get_Value("GuaranteeDate");
        
    }
    
    /** Set Lot No.
    @param Lot Lot number (alphanumeric) */
    public void setLot (String Lot)
    {
        set_Value ("Lot", Lot);
        
    }
    
    /** Get Lot No.
    @return Lot number (alphanumeric) */
    public String getLot() 
    {
        return (String)get_Value("Lot");
        
    }
    
    /** Set Attribute Set Instance.
    @param M_AttributeSetInstance_ID Product Attribute Set Instance */
    public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
    {
        if (M_AttributeSetInstance_ID < 0) throw new IllegalArgumentException ("M_AttributeSetInstance_ID is mandatory.");
        set_ValueNoCheck ("M_AttributeSetInstance_ID", Integer.valueOf(M_AttributeSetInstance_ID));
        
    }
    
    /** Get Attribute Set Instance.
    @return Product Attribute Set Instance */
    public int getM_AttributeSetInstance_ID() 
    {
        return get_ValueAsInt("M_AttributeSetInstance_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_AttributeSetInstance_ID()));
        
    }
    
    /** Set Attribute Set.
    @param M_AttributeSet_ID Product Attribute Set */
    public void setM_AttributeSet_ID (int M_AttributeSet_ID)
    {
        if (M_AttributeSet_ID < 0) throw new IllegalArgumentException ("M_AttributeSet_ID is mandatory.");
        set_Value ("M_AttributeSet_ID", Integer.valueOf(M_AttributeSet_ID));
        
    }
    
    /** Get Attribute Set.
    @return Product Attribute Set */
    public int getM_AttributeSet_ID() 
    {
        return get_ValueAsInt("M_AttributeSet_ID");
        
    }
    
    /** Set Lot.
    @param M_Lot_ID Product Lot Definition */
    public void setM_Lot_ID (int M_Lot_ID)
    {
        if (M_Lot_ID <= 0) set_Value ("M_Lot_ID", null);
        else
        set_Value ("M_Lot_ID", Integer.valueOf(M_Lot_ID));
        
    }
    
    /** Get Lot.
    @return Product Lot Definition */
    public int getM_Lot_ID() 
    {
        return get_ValueAsInt("M_Lot_ID");
        
    }
    
    /** Set Serial No.
    @param SerNo Product Serial Number */
    public void setSerNo (String SerNo)
    {
        set_Value ("SerNo", SerNo);
        
    }
    
    /** Get Serial No.
    @return Product Serial Number */
    public String getSerNo() 
    {
        return (String)get_Value("SerNo");
        
    }
    
    
}
