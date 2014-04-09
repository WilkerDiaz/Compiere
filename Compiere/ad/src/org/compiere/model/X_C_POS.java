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
/** Generated Model for C_POS
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_POS.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_POS extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_POS_ID id
    @param trx transaction
    */
    public X_C_POS (Ctx ctx, int C_POS_ID, Trx trx)
    {
        super (ctx, C_POS_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_POS_ID == 0)
        {
            setC_CashBook_ID (0);
            setC_POS_ID (0);
            setIsModifyPrice (false);	// N
            setM_PriceList_ID (0);
            setM_Warehouse_ID (0);
            setName (null);
            setSalesRep_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_POS (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=748 */
    public static final int Table_ID=748;
    
    /** TableName=C_POS */
    public static final String Table_Name="C_POS";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Template B.Partner.
    @param C_BPartnerCashTrx_ID Business Partner used for creating new Business Partners on the fly */
    public void setC_BPartnerCashTrx_ID (int C_BPartnerCashTrx_ID)
    {
        if (C_BPartnerCashTrx_ID <= 0) set_Value ("C_BPartnerCashTrx_ID", null);
        else
        set_Value ("C_BPartnerCashTrx_ID", Integer.valueOf(C_BPartnerCashTrx_ID));
        
    }
    
    /** Get Template B.Partner.
    @return Business Partner used for creating new Business Partners on the fly */
    public int getC_BPartnerCashTrx_ID() 
    {
        return get_ValueAsInt("C_BPartnerCashTrx_ID");
        
    }
    
    /** Set Cash Book.
    @param C_CashBook_ID Cash Book for recording petty cash transactions */
    public void setC_CashBook_ID (int C_CashBook_ID)
    {
        if (C_CashBook_ID < 1) throw new IllegalArgumentException ("C_CashBook_ID is mandatory.");
        set_Value ("C_CashBook_ID", Integer.valueOf(C_CashBook_ID));
        
    }
    
    /** Get Cash Book.
    @return Cash Book for recording petty cash transactions */
    public int getC_CashBook_ID() 
    {
        return get_ValueAsInt("C_CashBook_ID");
        
    }
    
    /** Set Document Type.
    @param C_DocType_ID Document type or rules */
    public void setC_DocType_ID (int C_DocType_ID)
    {
        if (C_DocType_ID <= 0) set_Value ("C_DocType_ID", null);
        else
        set_Value ("C_DocType_ID", Integer.valueOf(C_DocType_ID));
        
    }
    
    /** Get Document Type.
    @return Document type or rules */
    public int getC_DocType_ID() 
    {
        return get_ValueAsInt("C_DocType_ID");
        
    }
    
    /** Set POS Key Layout.
    @param C_POSKeyLayout_ID POS Function Key Layout */
    public void setC_POSKeyLayout_ID (int C_POSKeyLayout_ID)
    {
        if (C_POSKeyLayout_ID <= 0) set_Value ("C_POSKeyLayout_ID", null);
        else
        set_Value ("C_POSKeyLayout_ID", Integer.valueOf(C_POSKeyLayout_ID));
        
    }
    
    /** Get POS Key Layout.
    @return POS Function Key Layout */
    public int getC_POSKeyLayout_ID() 
    {
        return get_ValueAsInt("C_POSKeyLayout_ID");
        
    }
    
    /** Set POS Terminal.
    @param C_POS_ID Point of Sales Terminal */
    public void setC_POS_ID (int C_POS_ID)
    {
        if (C_POS_ID < 1) throw new IllegalArgumentException ("C_POS_ID is mandatory.");
        set_ValueNoCheck ("C_POS_ID", Integer.valueOf(C_POS_ID));
        
    }
    
    /** Get POS Terminal.
    @return Point of Sales Terminal */
    public int getC_POS_ID() 
    {
        return get_ValueAsInt("C_POS_ID");
        
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
    
    /** Set Modify Price.
    @param IsModifyPrice Allow modifying the price */
    public void setIsModifyPrice (boolean IsModifyPrice)
    {
        set_Value ("IsModifyPrice", Boolean.valueOf(IsModifyPrice));
        
    }
    
    /** Get Modify Price.
    @return Allow modifying the price */
    public boolean isModifyPrice() 
    {
        return get_ValueAsBoolean("IsModifyPrice");
        
    }
    
    /** Set Price List.
    @param M_PriceList_ID Unique identifier of a Price List */
    public void setM_PriceList_ID (int M_PriceList_ID)
    {
        if (M_PriceList_ID < 1) throw new IllegalArgumentException ("M_PriceList_ID is mandatory.");
        set_Value ("M_PriceList_ID", Integer.valueOf(M_PriceList_ID));
        
    }
    
    /** Get Price List.
    @return Unique identifier of a Price List */
    public int getM_PriceList_ID() 
    {
        return get_ValueAsInt("M_PriceList_ID");
        
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
    
    /** Set Printer Name.
    @param PrinterName Name of the Printer */
    public void setPrinterName (String PrinterName)
    {
        set_Value ("PrinterName", PrinterName);
        
    }
    
    /** Get Printer Name.
    @return Name of the Printer */
    public String getPrinterName() 
    {
        return (String)get_Value("PrinterName");
        
    }
    
    /** Set Representative.
    @param SalesRep_ID Company Agent like Sales Representative, Purchase Agent, and Customer Service Representative... */
    public void setSalesRep_ID (int SalesRep_ID)
    {
        if (SalesRep_ID < 1) throw new IllegalArgumentException ("SalesRep_ID is mandatory.");
        set_Value ("SalesRep_ID", Integer.valueOf(SalesRep_ID));
        
    }
    
    /** Get Representative.
    @return Company Agent like Sales Representative, Purchase Agent, and Customer Service Representative... */
    public int getSalesRep_ID() 
    {
        return get_ValueAsInt("SalesRep_ID");
        
    }
    
    
}
