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
/** Generated Model for AD_PrintPaper
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_PrintPaper.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_PrintPaper extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_PrintPaper_ID id
    @param trx transaction
    */
    public X_AD_PrintPaper (Ctx ctx, int AD_PrintPaper_ID, Trx trx)
    {
        super (ctx, AD_PrintPaper_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_PrintPaper_ID == 0)
        {
            setAD_PrintPaper_ID (0);
            setCode (null);	// iso-a4
            setIsDefault (false);
            setIsLandscape (true);	// Y
            setMarginBottom (0);	// 36
            setMarginLeft (0);	// 36
            setMarginRight (0);	// 36
            setMarginTop (0);	// 36
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_PrintPaper (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=492 */
    public static final int Table_ID=492;
    
    /** TableName=AD_PrintPaper */
    public static final String Table_Name="AD_PrintPaper";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Print Paper.
    @param AD_PrintPaper_ID Printer paper definition */
    public void setAD_PrintPaper_ID (int AD_PrintPaper_ID)
    {
        if (AD_PrintPaper_ID < 1) throw new IllegalArgumentException ("AD_PrintPaper_ID is mandatory.");
        set_ValueNoCheck ("AD_PrintPaper_ID", Integer.valueOf(AD_PrintPaper_ID));
        
    }
    
    /** Get Print Paper.
    @return Printer paper definition */
    public int getAD_PrintPaper_ID() 
    {
        return get_ValueAsInt("AD_PrintPaper_ID");
        
    }
    
    /** Set Code.
    @param Code Code to execute or to validate */
    public void setCode (String Code)
    {
        if (Code == null) throw new IllegalArgumentException ("Code is mandatory.");
        set_Value ("Code", Code);
        
    }
    
    /** Get Code.
    @return Code to execute or to validate */
    public String getCode() 
    {
        return (String)get_Value("Code");
        
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
    
    /** Inch = I */
    public static final String DIMENSIONUNITS_Inch = X_Ref_AD_PrintPaper_Units.INCH.getValue();
    /** MM = M */
    public static final String DIMENSIONUNITS_MM = X_Ref_AD_PrintPaper_Units.MM.getValue();
    /** Set Dimension Units.
    @param DimensionUnits Units of Dimension */
    public void setDimensionUnits (String DimensionUnits)
    {
        if (!X_Ref_AD_PrintPaper_Units.isValid(DimensionUnits))
        throw new IllegalArgumentException ("DimensionUnits Invalid value - " + DimensionUnits + " - Reference_ID=375 - I - M");
        set_Value ("DimensionUnits", DimensionUnits);
        
    }
    
    /** Get Dimension Units.
    @return Units of Dimension */
    public String getDimensionUnits() 
    {
        return (String)get_Value("DimensionUnits");
        
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
    
    /** Set Bottom Margin.
    @param MarginBottom Bottom Space in 1/72 inch */
    public void setMarginBottom (int MarginBottom)
    {
        set_Value ("MarginBottom", Integer.valueOf(MarginBottom));
        
    }
    
    /** Get Bottom Margin.
    @return Bottom Space in 1/72 inch */
    public int getMarginBottom() 
    {
        return get_ValueAsInt("MarginBottom");
        
    }
    
    /** Set Left Margin.
    @param MarginLeft Left Space in 1/72 inch */
    public void setMarginLeft (int MarginLeft)
    {
        set_Value ("MarginLeft", Integer.valueOf(MarginLeft));
        
    }
    
    /** Get Left Margin.
    @return Left Space in 1/72 inch */
    public int getMarginLeft() 
    {
        return get_ValueAsInt("MarginLeft");
        
    }
    
    /** Set Right Margin.
    @param MarginRight Right Space in 1/72 inch */
    public void setMarginRight (int MarginRight)
    {
        set_Value ("MarginRight", Integer.valueOf(MarginRight));
        
    }
    
    /** Get Right Margin.
    @return Right Space in 1/72 inch */
    public int getMarginRight() 
    {
        return get_ValueAsInt("MarginRight");
        
    }
    
    /** Set Top Margin.
    @param MarginTop Top Space in 1/72 inch */
    public void setMarginTop (int MarginTop)
    {
        set_Value ("MarginTop", Integer.valueOf(MarginTop));
        
    }
    
    /** Get Top Margin.
    @return Top Space in 1/72 inch */
    public int getMarginTop() 
    {
        return get_ValueAsInt("MarginTop");
        
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
    
    /** Set Size X.
    @param SizeX X (horizontal) dimension size */
    public void setSizeX (java.math.BigDecimal SizeX)
    {
        set_Value ("SizeX", SizeX);
        
    }
    
    /** Get Size X.
    @return X (horizontal) dimension size */
    public java.math.BigDecimal getSizeX() 
    {
        return get_ValueAsBigDecimal("SizeX");
        
    }
    
    /** Set Size Y.
    @param SizeY Y (vertical) dimension size */
    public void setSizeY (java.math.BigDecimal SizeY)
    {
        set_Value ("SizeY", SizeY);
        
    }
    
    /** Get Size Y.
    @return Y (vertical) dimension size */
    public java.math.BigDecimal getSizeY() 
    {
        return get_ValueAsBigDecimal("SizeY");
        
    }
    
    
}
