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
/** Generated Model for AD_PrintFormat
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_PrintFormat.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_PrintFormat extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_PrintFormat_ID id
    @param trx transaction
    */
    public X_AD_PrintFormat (Ctx ctx, int AD_PrintFormat_ID, Trx trx)
    {
        super (ctx, AD_PrintFormat_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_PrintFormat_ID == 0)
        {
            setAD_PrintColor_ID (0);
            setAD_PrintFont_ID (0);
            setAD_PrintFormat_ID (0);
            setAD_PrintPaper_ID (0);
            setAD_Table_ID (0);
            setFooterMargin (0);
            setHeaderMargin (0);
            setIsDefault (false);
            setIsForm (false);
            setIsStandardHeaderFooter (true);	// Y
            setIsSuppressDupGroupBy (false);	// N
            setIsTableBased (true);	// Y
            setIsTotalsOnly (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_PrintFormat (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27516344539789L;
    /** Last Updated Timestamp 2009-02-09 14:40:23.0 */
    public static final long updatedMS = 1234219223000L;
    /** AD_Table_ID=493 */
    public static final int Table_ID=493;
    
    /** TableName=AD_PrintFormat */
    public static final String Table_Name="AD_PrintFormat";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business View.
    @param AD_BView_ID The logical subset of related data for the purposes of reporting */
    public void setAD_BView_ID (int AD_BView_ID)
    {
        if (AD_BView_ID <= 0) set_Value ("AD_BView_ID", null);
        else
        set_Value ("AD_BView_ID", Integer.valueOf(AD_BView_ID));
        
    }
    
    /** Get Business View.
    @return The logical subset of related data for the purposes of reporting */
    public int getAD_BView_ID() 
    {
        return get_ValueAsInt("AD_BView_ID");
        
    }
    
    /** Set Print Color.
    @param AD_PrintColor_ID Color used for printing and display */
    public void setAD_PrintColor_ID (int AD_PrintColor_ID)
    {
        if (AD_PrintColor_ID < 1) throw new IllegalArgumentException ("AD_PrintColor_ID is mandatory.");
        set_Value ("AD_PrintColor_ID", Integer.valueOf(AD_PrintColor_ID));
        
    }
    
    /** Get Print Color.
    @return Color used for printing and display */
    public int getAD_PrintColor_ID() 
    {
        return get_ValueAsInt("AD_PrintColor_ID");
        
    }
    
    /** Set Print Font.
    @param AD_PrintFont_ID Maintain Print Font */
    public void setAD_PrintFont_ID (int AD_PrintFont_ID)
    {
        if (AD_PrintFont_ID < 1) throw new IllegalArgumentException ("AD_PrintFont_ID is mandatory.");
        set_Value ("AD_PrintFont_ID", Integer.valueOf(AD_PrintFont_ID));
        
    }
    
    /** Get Print Font.
    @return Maintain Print Font */
    public int getAD_PrintFont_ID() 
    {
        return get_ValueAsInt("AD_PrintFont_ID");
        
    }
    
    /** Set Print Format.
    @param AD_PrintFormat_ID Data Print Format */
    public void setAD_PrintFormat_ID (int AD_PrintFormat_ID)
    {
        if (AD_PrintFormat_ID < 1) throw new IllegalArgumentException ("AD_PrintFormat_ID is mandatory.");
        set_ValueNoCheck ("AD_PrintFormat_ID", Integer.valueOf(AD_PrintFormat_ID));
        
    }
    
    /** Get Print Format.
    @return Data Print Format */
    public int getAD_PrintFormat_ID() 
    {
        return get_ValueAsInt("AD_PrintFormat_ID");
        
    }
    
    /** Set Print Paper.
    @param AD_PrintPaper_ID Printer paper definition */
    public void setAD_PrintPaper_ID (int AD_PrintPaper_ID)
    {
        if (AD_PrintPaper_ID < 1) throw new IllegalArgumentException ("AD_PrintPaper_ID is mandatory.");
        set_Value ("AD_PrintPaper_ID", Integer.valueOf(AD_PrintPaper_ID));
        
    }
    
    /** Get Print Paper.
    @return Printer paper definition */
    public int getAD_PrintPaper_ID() 
    {
        return get_ValueAsInt("AD_PrintPaper_ID");
        
    }
    
    /** Set Print Table Format.
    @param AD_PrintTableFormat_ID Table Format in Reports */
    public void setAD_PrintTableFormat_ID (int AD_PrintTableFormat_ID)
    {
        if (AD_PrintTableFormat_ID <= 0) set_Value ("AD_PrintTableFormat_ID", null);
        else
        set_Value ("AD_PrintTableFormat_ID", Integer.valueOf(AD_PrintTableFormat_ID));
        
    }
    
    /** Get Print Table Format.
    @return Table Format in Reports */
    public int getAD_PrintTableFormat_ID() 
    {
        return get_ValueAsInt("AD_PrintTableFormat_ID");
        
    }
    
    /** Set Report View.
    @param AD_ReportView_ID View used to generate this report */
    public void setAD_ReportView_ID (int AD_ReportView_ID)
    {
        if (AD_ReportView_ID <= 0) set_ValueNoCheck ("AD_ReportView_ID", null);
        else
        set_ValueNoCheck ("AD_ReportView_ID", Integer.valueOf(AD_ReportView_ID));
        
    }
    
    /** Get Report View.
    @return View used to generate this report */
    public int getAD_ReportView_ID() 
    {
        return get_ValueAsInt("AD_ReportView_ID");
        
    }
    
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID < 1) throw new IllegalArgumentException ("AD_Table_ID is mandatory.");
        set_ValueNoCheck ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
    }
    
    /** Set Create Copy.
    @param CreateCopy Create Copy */
    public void setCreateCopy (String CreateCopy)
    {
        set_Value ("CreateCopy", CreateCopy);
        
    }
    
    /** Get Create Copy.
    @return Create Copy */
    public String getCreateCopy() 
    {
        return (String)get_Value("CreateCopy");
        
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
    
    /** Set Footer Margin.
    @param FooterMargin Margin of the Footer in 1/72 of an inch */
    public void setFooterMargin (int FooterMargin)
    {
        set_Value ("FooterMargin", Integer.valueOf(FooterMargin));
        
    }
    
    /** Get Footer Margin.
    @return Margin of the Footer in 1/72 of an inch */
    public int getFooterMargin() 
    {
        return get_ValueAsInt("FooterMargin");
        
    }
    
    /** Set Header Margin.
    @param HeaderMargin Margin of the Header in 1/72 of an inch */
    public void setHeaderMargin (int HeaderMargin)
    {
        set_Value ("HeaderMargin", Integer.valueOf(HeaderMargin));
        
    }
    
    /** Get Header Margin.
    @return Margin of the Header in 1/72 of an inch */
    public int getHeaderMargin() 
    {
        return get_ValueAsInt("HeaderMargin");
        
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
    
    /** Set Form.
    @param IsForm If selected, a Form is printed, if not selected a columnar List report */
    public void setIsForm (boolean IsForm)
    {
        set_Value ("IsForm", Boolean.valueOf(IsForm));
        
    }
    
    /** Get Form.
    @return If selected, a Form is printed, if not selected a columnar List report */
    public boolean isForm() 
    {
        return get_ValueAsBoolean("IsForm");
        
    }
    
    /** Set Standard Header/Footer.
    @param IsStandardHeaderFooter The standard Header and Footer is used */
    public void setIsStandardHeaderFooter (boolean IsStandardHeaderFooter)
    {
        set_Value ("IsStandardHeaderFooter", Boolean.valueOf(IsStandardHeaderFooter));
        
    }
    
    /** Get Standard Header/Footer.
    @return The standard Header and Footer is used */
    public boolean isStandardHeaderFooter() 
    {
        return get_ValueAsBoolean("IsStandardHeaderFooter");
        
    }
    
    /** Set Suppress Duplicate Group By.
    @param IsSuppressDupGroupBy Show Group By columns only in the first record for each unique combination */
    public void setIsSuppressDupGroupBy (boolean IsSuppressDupGroupBy)
    {
        set_Value ("IsSuppressDupGroupBy", Boolean.valueOf(IsSuppressDupGroupBy));
        
    }
    
    /** Get Suppress Duplicate Group By.
    @return Show Group By columns only in the first record for each unique combination */
    public boolean isSuppressDupGroupBy() 
    {
        return get_ValueAsBoolean("IsSuppressDupGroupBy");
        
    }
    
    /** Set Table Based.
    @param IsTableBased Table based List Reporting */
    public void setIsTableBased (boolean IsTableBased)
    {
        set_ValueNoCheck ("IsTableBased", Boolean.valueOf(IsTableBased));
        
    }
    
    /** Get Table Based.
    @return Table based List Reporting */
    public boolean isTableBased() 
    {
        return get_ValueAsBoolean("IsTableBased");
        
    }
    
    /** Set Totals Only.
    @param IsTotalsOnly Include only columns that represent totals in the print format */
    public void setIsTotalsOnly (boolean IsTotalsOnly)
    {
        set_Value ("IsTotalsOnly", Boolean.valueOf(IsTotalsOnly));
        
    }
    
    /** Get Totals Only.
    @return Include only columns that represent totals in the print format */
    public boolean isTotalsOnly() 
    {
        return get_ValueAsBoolean("IsTotalsOnly");
        
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
