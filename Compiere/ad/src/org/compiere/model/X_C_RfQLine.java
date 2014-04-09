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
/** Generated Model for C_RfQLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_RfQLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_RfQLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_RfQLine_ID id
    @param trx transaction
    */
    public X_C_RfQLine (Ctx ctx, int C_RfQLine_ID, Trx trx)
    {
        super (ctx, C_RfQLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_RfQLine_ID == 0)
        {
            setC_RfQLine_ID (0);
            setC_RfQ_ID (0);
            setLine (0);	// @SQL=SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM C_RfQLine WHERE C_RfQ_ID=@C_RfQ_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_RfQLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=676 */
    public static final int Table_ID=676;
    
    /** TableName=C_RfQLine */
    public static final String Table_Name="C_RfQLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set RfQ Line.
    @param C_RfQLine_ID Request for Quotation Line */
    public void setC_RfQLine_ID (int C_RfQLine_ID)
    {
        if (C_RfQLine_ID < 1) throw new IllegalArgumentException ("C_RfQLine_ID is mandatory.");
        set_ValueNoCheck ("C_RfQLine_ID", Integer.valueOf(C_RfQLine_ID));
        
    }
    
    /** Get RfQ Line.
    @return Request for Quotation Line */
    public int getC_RfQLine_ID() 
    {
        return get_ValueAsInt("C_RfQLine_ID");
        
    }
    
    /** Set RfQ.
    @param C_RfQ_ID Request for Quotation */
    public void setC_RfQ_ID (int C_RfQ_ID)
    {
        if (C_RfQ_ID < 1) throw new IllegalArgumentException ("C_RfQ_ID is mandatory.");
        set_ValueNoCheck ("C_RfQ_ID", Integer.valueOf(C_RfQ_ID));
        
    }
    
    /** Get RfQ.
    @return Request for Quotation */
    public int getC_RfQ_ID() 
    {
        return get_ValueAsInt("C_RfQ_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_RfQ_ID()));
        
    }
    
    /** Set Work Complete.
    @param DateWorkComplete Date when work is (planned to be) complete */
    public void setDateWorkComplete (Timestamp DateWorkComplete)
    {
        set_Value ("DateWorkComplete", DateWorkComplete);
        
    }
    
    /** Get Work Complete.
    @return Date when work is (planned to be) complete */
    public Timestamp getDateWorkComplete() 
    {
        return (Timestamp)get_Value("DateWorkComplete");
        
    }
    
    /** Set Work Start.
    @param DateWorkStart Date when work is (planned to be) started */
    public void setDateWorkStart (Timestamp DateWorkStart)
    {
        set_Value ("DateWorkStart", DateWorkStart);
        
    }
    
    /** Get Work Start.
    @return Date when work is (planned to be) started */
    public Timestamp getDateWorkStart() 
    {
        return (Timestamp)get_Value("DateWorkStart");
        
    }
    
    /** Set Delivery Days.
    @param DeliveryDays Number of Days (planned) until Delivery */
    public void setDeliveryDays (int DeliveryDays)
    {
        set_Value ("DeliveryDays", Integer.valueOf(DeliveryDays));
        
    }
    
    /** Get Delivery Days.
    @return Number of Days (planned) until Delivery */
    public int getDeliveryDays() 
    {
        return get_ValueAsInt("DeliveryDays");
        
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
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
    }
    
    /** Set Line No.
    @param Line Unique line for this document */
    public void setLine (int Line)
    {
        set_Value ("Line", Integer.valueOf(Line));
        
    }
    
    /** Get Line No.
    @return Unique line for this document */
    public int getLine() 
    {
        return get_ValueAsInt("Line");
        
    }
    
    /** Set Attribute Set Instance.
    @param M_AttributeSetInstance_ID Product Attribute Set Instance */
    public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
    {
        if (M_AttributeSetInstance_ID <= 0) set_Value ("M_AttributeSetInstance_ID", null);
        else
        set_Value ("M_AttributeSetInstance_ID", Integer.valueOf(M_AttributeSetInstance_ID));
        
    }
    
    /** Get Attribute Set Instance.
    @return Product Attribute Set Instance */
    public int getM_AttributeSetInstance_ID() 
    {
        return get_ValueAsInt("M_AttributeSetInstance_ID");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
        else
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    
}
