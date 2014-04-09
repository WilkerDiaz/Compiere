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
/** Generated Model for AD_PrintTableFormat
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_PrintTableFormat.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_PrintTableFormat extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_PrintTableFormat_ID id
    @param trx transaction
    */
    public X_AD_PrintTableFormat (Ctx ctx, int AD_PrintTableFormat_ID, Trx trx)
    {
        super (ctx, AD_PrintTableFormat_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_PrintTableFormat_ID == 0)
        {
            setAD_PrintTableFormat_ID (0);
            setIsDefault (false);
            setIsPaintBoundaryLines (false);
            setIsPaintHLines (false);
            setIsPaintHeaderLines (true);	// Y
            setIsPaintVLines (false);
            setIsPrintFunctionSymbols (false);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_PrintTableFormat (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=523 */
    public static final int Table_ID=523;
    
    /** TableName=AD_PrintTableFormat */
    public static final String Table_Name="AD_PrintTableFormat";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Print Table Format.
    @param AD_PrintTableFormat_ID Table Format in Reports */
    public void setAD_PrintTableFormat_ID (int AD_PrintTableFormat_ID)
    {
        if (AD_PrintTableFormat_ID < 1) throw new IllegalArgumentException ("AD_PrintTableFormat_ID is mandatory.");
        set_ValueNoCheck ("AD_PrintTableFormat_ID", Integer.valueOf(AD_PrintTableFormat_ID));
        
    }
    
    /** Get Print Table Format.
    @return Table Format in Reports */
    public int getAD_PrintTableFormat_ID() 
    {
        return get_ValueAsInt("AD_PrintTableFormat_ID");
        
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
    
    /** Set Footer Center.
    @param FooterCenter Content of the center portion of the footer. */
    public void setFooterCenter (String FooterCenter)
    {
        set_Value ("FooterCenter", FooterCenter);
        
    }
    
    /** Get Footer Center.
    @return Content of the center portion of the footer. */
    public String getFooterCenter() 
    {
        return (String)get_Value("FooterCenter");
        
    }
    
    /** Set Footer Left.
    @param FooterLeft Content of the left portion of the footer. */
    public void setFooterLeft (String FooterLeft)
    {
        set_Value ("FooterLeft", FooterLeft);
        
    }
    
    /** Get Footer Left.
    @return Content of the left portion of the footer. */
    public String getFooterLeft() 
    {
        return (String)get_Value("FooterLeft");
        
    }
    
    /** Set Footer Right.
    @param FooterRight Content of the right portion of the footer. */
    public void setFooterRight (String FooterRight)
    {
        set_Value ("FooterRight", FooterRight);
        
    }
    
    /** Get Footer Right.
    @return Content of the right portion of the footer. */
    public String getFooterRight() 
    {
        return (String)get_Value("FooterRight");
        
    }
    
    /** Set Function BG Color.
    @param FunctBG_PrintColor_ID Function Background Color */
    public void setFunctBG_PrintColor_ID (int FunctBG_PrintColor_ID)
    {
        if (FunctBG_PrintColor_ID <= 0) set_Value ("FunctBG_PrintColor_ID", null);
        else
        set_Value ("FunctBG_PrintColor_ID", Integer.valueOf(FunctBG_PrintColor_ID));
        
    }
    
    /** Get Function BG Color.
    @return Function Background Color */
    public int getFunctBG_PrintColor_ID() 
    {
        return get_ValueAsInt("FunctBG_PrintColor_ID");
        
    }
    
    /** Set Function Color.
    @param FunctFG_PrintColor_ID Function Foreground Color */
    public void setFunctFG_PrintColor_ID (int FunctFG_PrintColor_ID)
    {
        if (FunctFG_PrintColor_ID <= 0) set_Value ("FunctFG_PrintColor_ID", null);
        else
        set_Value ("FunctFG_PrintColor_ID", Integer.valueOf(FunctFG_PrintColor_ID));
        
    }
    
    /** Get Function Color.
    @return Function Foreground Color */
    public int getFunctFG_PrintColor_ID() 
    {
        return get_ValueAsInt("FunctFG_PrintColor_ID");
        
    }
    
    /** Set Function Font.
    @param Funct_PrintFont_ID Function row Font */
    public void setFunct_PrintFont_ID (int Funct_PrintFont_ID)
    {
        if (Funct_PrintFont_ID <= 0) set_Value ("Funct_PrintFont_ID", null);
        else
        set_Value ("Funct_PrintFont_ID", Integer.valueOf(Funct_PrintFont_ID));
        
    }
    
    /** Get Function Font.
    @return Function row Font */
    public int getFunct_PrintFont_ID() 
    {
        return get_ValueAsInt("Funct_PrintFont_ID");
        
    }
    
    /** Set Header Line Color.
    @param HdrLine_PrintColor_ID Table header row line color */
    public void setHdrLine_PrintColor_ID (int HdrLine_PrintColor_ID)
    {
        if (HdrLine_PrintColor_ID <= 0) set_Value ("HdrLine_PrintColor_ID", null);
        else
        set_Value ("HdrLine_PrintColor_ID", Integer.valueOf(HdrLine_PrintColor_ID));
        
    }
    
    /** Get Header Line Color.
    @return Table header row line color */
    public int getHdrLine_PrintColor_ID() 
    {
        return get_ValueAsInt("HdrLine_PrintColor_ID");
        
    }
    
    /** Set Header Stroke.
    @param HdrStroke Width of the Header Line Stroke */
    public void setHdrStroke (java.math.BigDecimal HdrStroke)
    {
        set_Value ("HdrStroke", HdrStroke);
        
    }
    
    /** Get Header Stroke.
    @return Width of the Header Line Stroke */
    public java.math.BigDecimal getHdrStroke() 
    {
        return get_ValueAsBigDecimal("HdrStroke");
        
    }
    
    /** Dash-Dotted Line = 2 */
    public static final String HDRSTROKETYPE_Dash_DottedLine = X_Ref_AD_PrintTableFormat_Stroke.DASH__DOTTED_LINE.getValue();
    /** Dashed Line = D */
    public static final String HDRSTROKETYPE_DashedLine = X_Ref_AD_PrintTableFormat_Stroke.DASHED_LINE.getValue();
    /** Solid Line = S */
    public static final String HDRSTROKETYPE_SolidLine = X_Ref_AD_PrintTableFormat_Stroke.SOLID_LINE.getValue();
    /** Dotted Line = d */
    public static final String HDRSTROKETYPE_DottedLine = X_Ref_AD_PrintTableFormat_Stroke.DOTTED_LINE.getValue();
    /** Set Header Stroke Type.
    @param HdrStrokeType Type of the Header Line Stroke */
    public void setHdrStrokeType (String HdrStrokeType)
    {
        if (!X_Ref_AD_PrintTableFormat_Stroke.isValid(HdrStrokeType))
        throw new IllegalArgumentException ("HdrStrokeType Invalid value - " + HdrStrokeType + " - Reference_ID=312 - 2 - D - S - d");
        set_Value ("HdrStrokeType", HdrStrokeType);
        
    }
    
    /** Get Header Stroke Type.
    @return Type of the Header Line Stroke */
    public String getHdrStrokeType() 
    {
        return (String)get_Value("HdrStrokeType");
        
    }
    
    /** Set Header Row BG Color.
    @param HdrTextBG_PrintColor_ID Background color of header row */
    public void setHdrTextBG_PrintColor_ID (int HdrTextBG_PrintColor_ID)
    {
        if (HdrTextBG_PrintColor_ID <= 0) set_Value ("HdrTextBG_PrintColor_ID", null);
        else
        set_Value ("HdrTextBG_PrintColor_ID", Integer.valueOf(HdrTextBG_PrintColor_ID));
        
    }
    
    /** Get Header Row BG Color.
    @return Background color of header row */
    public int getHdrTextBG_PrintColor_ID() 
    {
        return get_ValueAsInt("HdrTextBG_PrintColor_ID");
        
    }
    
    /** Set Header Row Color.
    @param HdrTextFG_PrintColor_ID Foreground color of the table header row */
    public void setHdrTextFG_PrintColor_ID (int HdrTextFG_PrintColor_ID)
    {
        if (HdrTextFG_PrintColor_ID <= 0) set_Value ("HdrTextFG_PrintColor_ID", null);
        else
        set_Value ("HdrTextFG_PrintColor_ID", Integer.valueOf(HdrTextFG_PrintColor_ID));
        
    }
    
    /** Get Header Row Color.
    @return Foreground color of the table header row */
    public int getHdrTextFG_PrintColor_ID() 
    {
        return get_ValueAsInt("HdrTextFG_PrintColor_ID");
        
    }
    
    /** Set Header Row Font.
    @param Hdr_PrintFont_ID Header row Font */
    public void setHdr_PrintFont_ID (int Hdr_PrintFont_ID)
    {
        if (Hdr_PrintFont_ID <= 0) set_Value ("Hdr_PrintFont_ID", null);
        else
        set_Value ("Hdr_PrintFont_ID", Integer.valueOf(Hdr_PrintFont_ID));
        
    }
    
    /** Get Header Row Font.
    @return Header row Font */
    public int getHdr_PrintFont_ID() 
    {
        return get_ValueAsInt("Hdr_PrintFont_ID");
        
    }
    
    /** Set Header Center.
    @param HeaderCenter Content of the center portion of the header. */
    public void setHeaderCenter (String HeaderCenter)
    {
        set_Value ("HeaderCenter", HeaderCenter);
        
    }
    
    /** Get Header Center.
    @return Content of the center portion of the header. */
    public String getHeaderCenter() 
    {
        return (String)get_Value("HeaderCenter");
        
    }
    
    /** Set Header Left.
    @param HeaderLeft Content of the left portion of the header. */
    public void setHeaderLeft (String HeaderLeft)
    {
        set_Value ("HeaderLeft", HeaderLeft);
        
    }
    
    /** Get Header Left.
    @return Content of the left portion of the header. */
    public String getHeaderLeft() 
    {
        return (String)get_Value("HeaderLeft");
        
    }
    
    /** Set Header Right.
    @param HeaderRight Content of the right portion of the header. */
    public void setHeaderRight (String HeaderRight)
    {
        set_Value ("HeaderRight", HeaderRight);
        
    }
    
    /** Get Header Right.
    @return Content of the right portion of the header. */
    public String getHeaderRight() 
    {
        return (String)get_Value("HeaderRight");
        
    }
    
    /** Set Image attached.
    @param ImageIsAttached The image to be printed is attached to the record */
    public void setImageIsAttached (boolean ImageIsAttached)
    {
        set_Value ("ImageIsAttached", Boolean.valueOf(ImageIsAttached));
        
    }
    
    /** Get Image attached.
    @return The image to be printed is attached to the record */
    public boolean isImageIsAttached() 
    {
        return get_ValueAsBoolean("ImageIsAttached");
        
    }
    
    /** Set Image URL.
    @param ImageURL URL of image */
    public void setImageURL (String ImageURL)
    {
        set_Value ("ImageURL", ImageURL);
        
    }
    
    /** Get Image URL.
    @return URL of image */
    public String getImageURL() 
    {
        return (String)get_Value("ImageURL");
        
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
    
    /** Set Paint Boundary Lines.
    @param IsPaintBoundaryLines Paint table boundary lines */
    public void setIsPaintBoundaryLines (boolean IsPaintBoundaryLines)
    {
        set_Value ("IsPaintBoundaryLines", Boolean.valueOf(IsPaintBoundaryLines));
        
    }
    
    /** Get Paint Boundary Lines.
    @return Paint table boundary lines */
    public boolean isPaintBoundaryLines() 
    {
        return get_ValueAsBoolean("IsPaintBoundaryLines");
        
    }
    
    /** Set Paint Horizontal Lines.
    @param IsPaintHLines Paint horizontal lines */
    public void setIsPaintHLines (boolean IsPaintHLines)
    {
        set_Value ("IsPaintHLines", Boolean.valueOf(IsPaintHLines));
        
    }
    
    /** Get Paint Horizontal Lines.
    @return Paint horizontal lines */
    public boolean isPaintHLines() 
    {
        return get_ValueAsBoolean("IsPaintHLines");
        
    }
    
    /** Set Paint Header Lines.
    @param IsPaintHeaderLines Paint Lines over/under the Header Line */
    public void setIsPaintHeaderLines (boolean IsPaintHeaderLines)
    {
        set_Value ("IsPaintHeaderLines", Boolean.valueOf(IsPaintHeaderLines));
        
    }
    
    /** Get Paint Header Lines.
    @return Paint Lines over/under the Header Line */
    public boolean isPaintHeaderLines() 
    {
        return get_ValueAsBoolean("IsPaintHeaderLines");
        
    }
    
    /** Set Paint Vertical Lines.
    @param IsPaintVLines Paint vertical lines */
    public void setIsPaintVLines (boolean IsPaintVLines)
    {
        set_Value ("IsPaintVLines", Boolean.valueOf(IsPaintVLines));
        
    }
    
    /** Get Paint Vertical Lines.
    @return Paint vertical lines */
    public boolean isPaintVLines() 
    {
        return get_ValueAsBoolean("IsPaintVLines");
        
    }
    
    /** Set Print Function Symbols.
    @param IsPrintFunctionSymbols Print Symbols for Functions (Sum, Average, Count) */
    public void setIsPrintFunctionSymbols (boolean IsPrintFunctionSymbols)
    {
        set_Value ("IsPrintFunctionSymbols", Boolean.valueOf(IsPrintFunctionSymbols));
        
    }
    
    /** Get Print Function Symbols.
    @return Print Symbols for Functions (Sum, Average, Count) */
    public boolean isPrintFunctionSymbols() 
    {
        return get_ValueAsBoolean("IsPrintFunctionSymbols");
        
    }
    
    /** Set Line Stroke.
    @param LineStroke Width of the Line Stroke */
    public void setLineStroke (java.math.BigDecimal LineStroke)
    {
        set_Value ("LineStroke", LineStroke);
        
    }
    
    /** Get Line Stroke.
    @return Width of the Line Stroke */
    public java.math.BigDecimal getLineStroke() 
    {
        return get_ValueAsBigDecimal("LineStroke");
        
    }
    
    /** Dash-Dotted Line = 2 */
    public static final String LINESTROKETYPE_Dash_DottedLine = X_Ref_AD_PrintTableFormat_Stroke.DASH__DOTTED_LINE.getValue();
    /** Dashed Line = D */
    public static final String LINESTROKETYPE_DashedLine = X_Ref_AD_PrintTableFormat_Stroke.DASHED_LINE.getValue();
    /** Solid Line = S */
    public static final String LINESTROKETYPE_SolidLine = X_Ref_AD_PrintTableFormat_Stroke.SOLID_LINE.getValue();
    /** Dotted Line = d */
    public static final String LINESTROKETYPE_DottedLine = X_Ref_AD_PrintTableFormat_Stroke.DOTTED_LINE.getValue();
    /** Set Line Stroke Type.
    @param LineStrokeType Type of the Line Stroke */
    public void setLineStrokeType (String LineStrokeType)
    {
        if (!X_Ref_AD_PrintTableFormat_Stroke.isValid(LineStrokeType))
        throw new IllegalArgumentException ("LineStrokeType Invalid value - " + LineStrokeType + " - Reference_ID=312 - 2 - D - S - d");
        set_Value ("LineStrokeType", LineStrokeType);
        
    }
    
    /** Get Line Stroke Type.
    @return Type of the Line Stroke */
    public String getLineStrokeType() 
    {
        return (String)get_Value("LineStrokeType");
        
    }
    
    /** Set Line Color.
    @param Line_PrintColor_ID Table line color */
    public void setLine_PrintColor_ID (int Line_PrintColor_ID)
    {
        if (Line_PrintColor_ID <= 0) set_Value ("Line_PrintColor_ID", null);
        else
        set_Value ("Line_PrintColor_ID", Integer.valueOf(Line_PrintColor_ID));
        
    }
    
    /** Get Line Color.
    @return Table line color */
    public int getLine_PrintColor_ID() 
    {
        return get_ValueAsInt("Line_PrintColor_ID");
        
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
    
    
}
