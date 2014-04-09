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
/** Generated Model for M_PackageLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_PackageLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_PackageLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_PackageLine_ID id
    @param trx transaction
    */
    public X_M_PackageLine (Ctx ctx, int M_PackageLine_ID, Trx trx)
    {
        super (ctx, M_PackageLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_PackageLine_ID == 0)
        {
            setM_InOutLine_ID (0);
            setM_PackageLine_ID (0);
            setM_Package_ID (0);
            setQty (Env.ZERO);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_PackageLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=663 */
    public static final int Table_ID=663;
    
    /** TableName=M_PackageLine */
    public static final String Table_Name="M_PackageLine";
    
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
    
    /** Set Shipment/Receipt Line.
    @param M_InOutLine_ID Line on Shipment or Receipt document */
    public void setM_InOutLine_ID (int M_InOutLine_ID)
    {
        if (M_InOutLine_ID < 1) throw new IllegalArgumentException ("M_InOutLine_ID is mandatory.");
        set_ValueNoCheck ("M_InOutLine_ID", Integer.valueOf(M_InOutLine_ID));
        
    }
    
    /** Get Shipment/Receipt Line.
    @return Line on Shipment or Receipt document */
    public int getM_InOutLine_ID() 
    {
        return get_ValueAsInt("M_InOutLine_ID");
        
    }
    
    /** Set Package Line.
    @param M_PackageLine_ID The detail content of the Package */
    public void setM_PackageLine_ID (int M_PackageLine_ID)
    {
        if (M_PackageLine_ID < 1) throw new IllegalArgumentException ("M_PackageLine_ID is mandatory.");
        set_ValueNoCheck ("M_PackageLine_ID", Integer.valueOf(M_PackageLine_ID));
        
    }
    
    /** Get Package Line.
    @return The detail content of the Package */
    public int getM_PackageLine_ID() 
    {
        return get_ValueAsInt("M_PackageLine_ID");
        
    }
    
    /** Set Package.
    @param M_Package_ID Shipment Package */
    public void setM_Package_ID (int M_Package_ID)
    {
        if (M_Package_ID < 1) throw new IllegalArgumentException ("M_Package_ID is mandatory.");
        set_ValueNoCheck ("M_Package_ID", Integer.valueOf(M_Package_ID));
        
    }
    
    /** Get Package.
    @return Shipment Package */
    public int getM_Package_ID() 
    {
        return get_ValueAsInt("M_Package_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_Package_ID()));
        
    }
    
    /** Set Quantity.
    @param Qty Quantity */
    public void setQty (java.math.BigDecimal Qty)
    {
        if (Qty == null) throw new IllegalArgumentException ("Qty is mandatory.");
        set_Value ("Qty", Qty);
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public java.math.BigDecimal getQty() 
    {
        return get_ValueAsBigDecimal("Qty");
        
    }
    
    
}
