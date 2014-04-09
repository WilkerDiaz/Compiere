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
/** Generated Model for AD_PrintLabel
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_PrintLabel.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_PrintLabel extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_PrintLabel_ID id
    @param trx transaction
    */
    public X_AD_PrintLabel (Ctx ctx, int AD_PrintLabel_ID, Trx trx)
    {
        super (ctx, AD_PrintLabel_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_PrintLabel_ID == 0)
        {
            setAD_LabelPrinter_ID (0);
            setAD_PrintLabel_ID (0);
            setAD_Table_ID (0);
            setIsLandscape (false);
            setLabelHeight (0);
            setLabelWidth (0);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_PrintLabel (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=570 */
    public static final int Table_ID=570;
    
    /** TableName=AD_PrintLabel */
    public static final String Table_Name="AD_PrintLabel";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Label printer.
    @param AD_LabelPrinter_ID Label Printer Definition */
    public void setAD_LabelPrinter_ID (int AD_LabelPrinter_ID)
    {
        if (AD_LabelPrinter_ID < 1) throw new IllegalArgumentException ("AD_LabelPrinter_ID is mandatory.");
        set_Value ("AD_LabelPrinter_ID", Integer.valueOf(AD_LabelPrinter_ID));
        
    }
    
    /** Get Label printer.
    @return Label Printer Definition */
    public int getAD_LabelPrinter_ID() 
    {
        return get_ValueAsInt("AD_LabelPrinter_ID");
        
    }
    
    /** Set Print Label.
    @param AD_PrintLabel_ID Label Format to print */
    public void setAD_PrintLabel_ID (int AD_PrintLabel_ID)
    {
        if (AD_PrintLabel_ID < 1) throw new IllegalArgumentException ("AD_PrintLabel_ID is mandatory.");
        set_ValueNoCheck ("AD_PrintLabel_ID", Integer.valueOf(AD_PrintLabel_ID));
        
    }
    
    /** Get Print Label.
    @return Label Format to print */
    public int getAD_PrintLabel_ID() 
    {
        return get_ValueAsInt("AD_PrintLabel_ID");
        
    }
    
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID < 1) throw new IllegalArgumentException ("AD_Table_ID is mandatory.");
        set_Value ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
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
    
    /** Set Landscape.
    @param IsLandscape Landscape orientation */
    public void setIsLandscape (boolean IsLandscape)
    {
        set_Value ("IsLandscape", Boolean.valueOf(IsLandscape));
        
    }
    
    /** Get Landscape.
    @return Landscape orientation */
    public boolean isLandscape() 
    {
        return get_ValueAsBoolean("IsLandscape");
        
    }
    
    /** Set Label Height.
    @param LabelHeight Height of the label */
    public void setLabelHeight (int LabelHeight)
    {
        set_Value ("LabelHeight", Integer.valueOf(LabelHeight));
        
    }
    
    /** Get Label Height.
    @return Height of the label */
    public int getLabelHeight() 
    {
        return get_ValueAsInt("LabelHeight");
        
    }
    
    /** Set Label Width.
    @param LabelWidth Width of the Label */
    public void setLabelWidth (int LabelWidth)
    {
        set_Value ("LabelWidth", Integer.valueOf(LabelWidth));
        
    }
    
    /** Get Label Width.
    @return Width of the Label */
    public int getLabelWidth() 
    {
        return get_ValueAsInt("LabelWidth");
        
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
    
    
}
