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
/** Generated Model for AD_PrintFormatItem
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_PrintFormatItem.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_PrintFormatItem extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_PrintFormatItem_ID id
    @param trx transaction
    */
    public X_AD_PrintFormatItem (Ctx ctx, int AD_PrintFormatItem_ID, Trx trx)
    {
        super (ctx, AD_PrintFormatItem_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_PrintFormatItem_ID == 0)
        {
            setAD_PrintFormatItem_ID (0);
            setAD_PrintFormat_ID (0);
            setFieldAlignmentType (null);	// D
            setImageIsAttached (false);
            setIsAscending (true);	// Y
            setIsAveraged (false);
            setIsCentrallyMaintained (false);
            setIsCounted (false);
            setIsDeviationCalc (false);
            setIsFilledRectangle (false);	// N
            setIsFixedWidth (false);
            setIsGroupBy (false);
            setIsHeightOneLine (true);	// Y
            setIsImageField (false);
            setIsMaxCalc (false);
            setIsMinCalc (false);
            setIsNextLine (true);	// Y
            setIsNextPage (false);
            setIsOrderBy (false);
            setIsPageBreak (false);
            setIsPrinted (true);	// Y
            setIsRelativePosition (true);	// Y
            setIsRunningTotal (false);
            setIsSetNLPosition (false);
            setIsSummarized (false);
            setIsSuppressNull (false);
            setIsVarianceCalc (false);
            setLineAlignmentType (null);	// X
            setMaxHeight (0);
            setMaxWidth (0);
            setName (null);
            setPrintAreaType (null);	// C
            setPrintFormatType (null);	// F
            setSeqNo (0);	// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM AD_PrintFormatItem WHERE AD_PrintFormat_ID=@AD_PrintFormat_ID@
            setSortNo (0);
            setXPosition (0);
            setXSpace (0);
            setYPosition (0);
            setYSpace (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_PrintFormatItem (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27513434702789L;
    /** Last Updated Timestamp 2009-01-06 22:23:06.0 */
    public static final long updatedMS = 1231309386000L;
    /** AD_Table_ID=489 */
    public static final int Table_ID=489;
    
    /** TableName=AD_PrintFormatItem */
    public static final String Table_Name="AD_PrintFormatItem";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business View Field.
    @param AD_BView_Field_ID Identfies the fields included in this Business View */
    public void setAD_BView_Field_ID (int AD_BView_Field_ID)
    {
        if (AD_BView_Field_ID <= 0) set_Value ("AD_BView_Field_ID", null);
        else
        set_Value ("AD_BView_Field_ID", Integer.valueOf(AD_BView_Field_ID));
        
    }
    
    /** Get Business View Field.
    @return Identfies the fields included in this Business View */
    public int getAD_BView_Field_ID() 
    {
        return get_ValueAsInt("AD_BView_Field_ID");
        
    }
    
    /** Set Column.
    @param AD_Column_ID Column in the table */
    public void setAD_Column_ID (int AD_Column_ID)
    {
        if (AD_Column_ID <= 0) set_Value ("AD_Column_ID", null);
        else
        set_Value ("AD_Column_ID", Integer.valueOf(AD_Column_ID));
        
    }
    
    /** Get Column.
    @return Column in the table */
    public int getAD_Column_ID() 
    {
        return get_ValueAsInt("AD_Column_ID");
        
    }
    
    /** Set Field Group.
    @param AD_FieldGroup_ID Logical grouping of fields */
    public void setAD_FieldGroup_ID (int AD_FieldGroup_ID)
    {
        if (AD_FieldGroup_ID <= 0) set_Value ("AD_FieldGroup_ID", null);
        else
        set_Value ("AD_FieldGroup_ID", Integer.valueOf(AD_FieldGroup_ID));
        
    }
    
    /** Get Field Group.
    @return Logical grouping of fields */
    public int getAD_FieldGroup_ID() 
    {
        return get_ValueAsInt("AD_FieldGroup_ID");
        
    }
    
    /** Set Print Color.
    @param AD_PrintColor_ID Color used for printing and display */
    public void setAD_PrintColor_ID (int AD_PrintColor_ID)
    {
        if (AD_PrintColor_ID <= 0) set_Value ("AD_PrintColor_ID", null);
        else
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
        if (AD_PrintFont_ID <= 0) set_Value ("AD_PrintFont_ID", null);
        else
        set_Value ("AD_PrintFont_ID", Integer.valueOf(AD_PrintFont_ID));
        
    }
    
    /** Get Print Font.
    @return Maintain Print Font */
    public int getAD_PrintFont_ID() 
    {
        return get_ValueAsInt("AD_PrintFont_ID");
        
    }
    
    /** Set Included Print Format.
    @param AD_PrintFormatChild_ID Print format that is included here. */
    public void setAD_PrintFormatChild_ID (int AD_PrintFormatChild_ID)
    {
        if (AD_PrintFormatChild_ID <= 0) set_Value ("AD_PrintFormatChild_ID", null);
        else
        set_Value ("AD_PrintFormatChild_ID", Integer.valueOf(AD_PrintFormatChild_ID));
        
    }
    
    /** Get Included Print Format.
    @return Print format that is included here. */
    public int getAD_PrintFormatChild_ID() 
    {
        return get_ValueAsInt("AD_PrintFormatChild_ID");
        
    }
    
    /** Set Print Format Item.
    @param AD_PrintFormatItem_ID Item/Column in the Print format */
    public void setAD_PrintFormatItem_ID (int AD_PrintFormatItem_ID)
    {
        if (AD_PrintFormatItem_ID < 1) throw new IllegalArgumentException ("AD_PrintFormatItem_ID is mandatory.");
        set_ValueNoCheck ("AD_PrintFormatItem_ID", Integer.valueOf(AD_PrintFormatItem_ID));
        
    }
    
    /** Get Print Format Item.
    @return Item/Column in the Print format */
    public int getAD_PrintFormatItem_ID() 
    {
        return get_ValueAsInt("AD_PrintFormatItem_ID");
        
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
    
    /** Set Graph.
    @param AD_PrintGraph_ID Graph included in Reports */
    public void setAD_PrintGraph_ID (int AD_PrintGraph_ID)
    {
        if (AD_PrintGraph_ID <= 0) set_Value ("AD_PrintGraph_ID", null);
        else
        set_Value ("AD_PrintGraph_ID", Integer.valueOf(AD_PrintGraph_ID));
        
    }
    
    /** Get Graph.
    @return Graph included in Reports */
    public int getAD_PrintGraph_ID() 
    {
        return get_ValueAsInt("AD_PrintGraph_ID");
        
    }
    
    /** Set Arc Diameter.
    @param ArcDiameter Arc Diameter for rounded Rectangles */
    public void setArcDiameter (int ArcDiameter)
    {
        set_Value ("ArcDiameter", Integer.valueOf(ArcDiameter));
        
    }
    
    /** Get Arc Diameter.
    @return Arc Diameter for rounded Rectangles */
    public int getArcDiameter() 
    {
        return get_ValueAsInt("ArcDiameter");
        
    }
    
    /** Code 128 A character set = 28A */
    public static final String BARCODETYPE_Code128ACharacterSet = X_Ref_AD_PrintFormatItem_BarcodeType.CODE128_A_CHARACTER_SET.getValue();
    /** Code 128 B character set = 28B */
    public static final String BARCODETYPE_Code128BCharacterSet = X_Ref_AD_PrintFormatItem_BarcodeType.CODE128_B_CHARACTER_SET.getValue();
    /** Code 128 C character set = 28C */
    public static final String BARCODETYPE_Code128CCharacterSet = X_Ref_AD_PrintFormatItem_BarcodeType.CODE128_C_CHARACTER_SET.getValue();
    /** Codabar 2 of 7 linear = 2o9 */
    public static final String BARCODETYPE_Codabar2Of7Linear = X_Ref_AD_PrintFormatItem_BarcodeType.CODABAR2_OF7_LINEAR.getValue();
    /** Code 39 3 of 9 linear with Checksum = 3O9 */
    public static final String BARCODETYPE_Code393Of9LinearWithChecksum = X_Ref_AD_PrintFormatItem_BarcodeType.CODE393_OF9_LINEAR_WITH_CHECKSUM.getValue();
    /** Code 39 3 of 9 linear w/o Checksum = 3o9 */
    public static final String BARCODETYPE_Code393Of9LinearWOChecksum = X_Ref_AD_PrintFormatItem_BarcodeType.CODE393_OF9_LINEAR_WO_CHECKSUM.getValue();
    /** PDF417 two dimensional = 417 */
    public static final String BARCODETYPE_PDF417TwoDimensional = X_Ref_AD_PrintFormatItem_BarcodeType.PD_F417_TWO_DIMENSIONAL.getValue();
    /** SCC-14 shipping code UCC/EAN 128 = C14 */
    public static final String BARCODETYPE_SCC_14ShippingCodeUCCEAN128 = X_Ref_AD_PrintFormatItem_BarcodeType.SC_C_14_SHIPPING_CODE_UCCEA_N128.getValue();
    /** SSCC-18 number UCC/EAN 128 = C18 */
    public static final String BARCODETYPE_SSCC_18NumberUCCEAN128 = X_Ref_AD_PrintFormatItem_BarcodeType.SSC_C_18_NUMBER_UCCEA_N128.getValue();
    /** Code 128 dynamically switching = C28 */
    public static final String BARCODETYPE_Code128DynamicallySwitching = X_Ref_AD_PrintFormatItem_BarcodeType.CODE128_DYNAMICALLY_SWITCHING.getValue();
    /** Code 39 linear with Checksum = C39 */
    public static final String BARCODETYPE_Code39LinearWithChecksum = X_Ref_AD_PrintFormatItem_BarcodeType.CODE39_LINEAR_WITH_CHECKSUM.getValue();
    /** Codeabar linear = COD */
    public static final String BARCODETYPE_CodeabarLinear = X_Ref_AD_PrintFormatItem_BarcodeType.CODEABAR_LINEAR.getValue();
    /** EAN 128 = E28 */
    public static final String BARCODETYPE_EAN128 = X_Ref_AD_PrintFormatItem_BarcodeType.EA_N128.getValue();
    /** Global Trade Item No GTIN UCC/EAN 128 = GTN */
    public static final String BARCODETYPE_GlobalTradeItemNoGTINUCCEAN128 = X_Ref_AD_PrintFormatItem_BarcodeType.GLOBAL_TRADE_ITEM_NO_GTINUCCEA_N128.getValue();
    /** Codabar Monarch linear = MON */
    public static final String BARCODETYPE_CodabarMonarchLinear = X_Ref_AD_PrintFormatItem_BarcodeType.CODABAR_MONARCH_LINEAR.getValue();
    /** Codabar NW-7 linear = NW7 */
    public static final String BARCODETYPE_CodabarNW_7Linear = X_Ref_AD_PrintFormatItem_BarcodeType.CODABAR_N_W_7_LINEAR.getValue();
    /** Shipment ID number UCC/EAN 128 = SID */
    public static final String BARCODETYPE_ShipmentIDNumberUCCEAN128 = X_Ref_AD_PrintFormatItem_BarcodeType.SHIPMENT_ID_NUMBER_UCCEA_N128.getValue();
    /** UCC 128 = U28 */
    public static final String BARCODETYPE_UCC128 = X_Ref_AD_PrintFormatItem_BarcodeType.UC_C128.getValue();
    /** Code 39 USD3 with Checksum = US3 */
    public static final String BARCODETYPE_Code39USD3WithChecksum = X_Ref_AD_PrintFormatItem_BarcodeType.CODE39_US_D3_WITH_CHECKSUM.getValue();
    /** Codabar USD-4 linear = US4 */
    public static final String BARCODETYPE_CodabarUSD_4Linear = X_Ref_AD_PrintFormatItem_BarcodeType.CODABAR_US_D_4_LINEAR.getValue();
    /** US Postal Service UCC/EAN 128 = USP */
    public static final String BARCODETYPE_USPostalServiceUCCEAN128 = X_Ref_AD_PrintFormatItem_BarcodeType.US_POSTAL_SERVICE_UCCEA_N128.getValue();
    /** Code 39 linear w/o Checksum = c39 */
    public static final String BARCODETYPE_Code39LinearWOChecksum = X_Ref_AD_PrintFormatItem_BarcodeType.CODE39_LINEAR_WO_CHECKSUM.getValue();
    /** Code 39 USD3 w/o Checksum = us3 */
    public static final String BARCODETYPE_Code39USD3WOChecksum = X_Ref_AD_PrintFormatItem_BarcodeType.CODE39_US_D3_WO_CHECKSUM.getValue();
    /** Set Barcode Type.
    @param BarcodeType Type of barcode */
    public void setBarcodeType (String BarcodeType)
    {
        if (!X_Ref_AD_PrintFormatItem_BarcodeType.isValid(BarcodeType))
        throw new IllegalArgumentException ("BarcodeType Invalid value - " + BarcodeType + " - Reference_ID=377 - 28A - 28B - 28C - 2o9 - 3O9 - 3o9 - 417 - C14 - C18 - C28 - C39 - COD - E28 - GTN - MON - NW7 - SID - U28 - US3 - US4 - USP - c39 - us3");
        set_Value ("BarcodeType", BarcodeType);
        
    }
    
    /** Get Barcode Type.
    @return Type of barcode */
    public String getBarcodeType() 
    {
        return (String)get_Value("BarcodeType");
        
    }
    
    /** Set Below Column.
    @param BelowColumn Print this column below the column index entered */
    public void setBelowColumn (int BelowColumn)
    {
        set_Value ("BelowColumn", Integer.valueOf(BelowColumn));
        
    }
    
    /** Get Below Column.
    @return Print this column below the column index entered */
    public int getBelowColumn() 
    {
        return get_ValueAsInt("BelowColumn");
        
    }
    
    /** Block = B */
    public static final String FIELDALIGNMENTTYPE_Block = X_Ref_AD_Print_Field_Alignment.BLOCK.getValue();
    /** Center = C */
    public static final String FIELDALIGNMENTTYPE_Center = X_Ref_AD_Print_Field_Alignment.CENTER.getValue();
    /** Default = D */
    public static final String FIELDALIGNMENTTYPE_Default = X_Ref_AD_Print_Field_Alignment.DEFAULT.getValue();
    /** Leading (left) = L */
    public static final String FIELDALIGNMENTTYPE_LeadingLeft = X_Ref_AD_Print_Field_Alignment.LEADING_LEFT.getValue();
    /** Trailing (right) = T */
    public static final String FIELDALIGNMENTTYPE_TrailingRight = X_Ref_AD_Print_Field_Alignment.TRAILING_RIGHT.getValue();
    /** Set Field Alignment.
    @param FieldAlignmentType Field Text Alignment */
    public void setFieldAlignmentType (String FieldAlignmentType)
    {
        if (FieldAlignmentType == null) throw new IllegalArgumentException ("FieldAlignmentType is mandatory");
        if (!X_Ref_AD_Print_Field_Alignment.isValid(FieldAlignmentType))
        throw new IllegalArgumentException ("FieldAlignmentType Invalid value - " + FieldAlignmentType + " - Reference_ID=253 - B - C - D - L - T");
        set_Value ("FieldAlignmentType", FieldAlignmentType);
        
    }
    
    /** Get Field Alignment.
    @return Field Text Alignment */
    public String getFieldAlignmentType() 
    {
        return (String)get_Value("FieldAlignmentType");
        
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
    
    /** Set Ascending Sort Order.
    @param IsAscending Sort in Ascending Order */
    public void setIsAscending (boolean IsAscending)
    {
        set_Value ("IsAscending", Boolean.valueOf(IsAscending));
        
    }
    
    /** Get Ascending Sort Order.
    @return Sort in Ascending Order */
    public boolean isAscending() 
    {
        return get_ValueAsBoolean("IsAscending");
        
    }
    
    /** Set Calculate Mean (μ).
    @param IsAveraged Calculate Average of numeric content or length */
    public void setIsAveraged (boolean IsAveraged)
    {
        set_Value ("IsAveraged", Boolean.valueOf(IsAveraged));
        
    }
    
    /** Get Calculate Mean (μ).
    @return Calculate Average of numeric content or length */
    public boolean isAveraged() 
    {
        return get_ValueAsBoolean("IsAveraged");
        
    }
    
    /** Set Centrally maintained.
    @param IsCentrallyMaintained Information maintained in System Element table */
    public void setIsCentrallyMaintained (boolean IsCentrallyMaintained)
    {
        set_Value ("IsCentrallyMaintained", Boolean.valueOf(IsCentrallyMaintained));
        
    }
    
    /** Get Centrally maintained.
    @return Information maintained in System Element table */
    public boolean isCentrallyMaintained() 
    {
        return get_ValueAsBoolean("IsCentrallyMaintained");
        
    }
    
    /** Set Calculate Count (№).
    @param IsCounted Count number of not empty elements */
    public void setIsCounted (boolean IsCounted)
    {
        set_Value ("IsCounted", Boolean.valueOf(IsCounted));
        
    }
    
    /** Get Calculate Count (№).
    @return Count number of not empty elements */
    public boolean isCounted() 
    {
        return get_ValueAsBoolean("IsCounted");
        
    }
    
    /** Set Calculate Deviation (σ).
    @param IsDeviationCalc Calculate Standard Deviation */
    public void setIsDeviationCalc (boolean IsDeviationCalc)
    {
        set_Value ("IsDeviationCalc", Boolean.valueOf(IsDeviationCalc));
        
    }
    
    /** Get Calculate Deviation (σ).
    @return Calculate Standard Deviation */
    public boolean isDeviationCalc() 
    {
        return get_ValueAsBoolean("IsDeviationCalc");
        
    }
    
    /** Set Fill Shape.
    @param IsFilledRectangle Fill the shape with the color selected */
    public void setIsFilledRectangle (boolean IsFilledRectangle)
    {
        set_Value ("IsFilledRectangle", Boolean.valueOf(IsFilledRectangle));
        
    }
    
    /** Get Fill Shape.
    @return Fill the shape with the color selected */
    public boolean isFilledRectangle() 
    {
        return get_ValueAsBoolean("IsFilledRectangle");
        
    }
    
    /** Set Fixed Width.
    @param IsFixedWidth Column has a fixed width */
    public void setIsFixedWidth (boolean IsFixedWidth)
    {
        set_Value ("IsFixedWidth", Boolean.valueOf(IsFixedWidth));
        
    }
    
    /** Get Fixed Width.
    @return Column has a fixed width */
    public boolean isFixedWidth() 
    {
        return get_ValueAsBoolean("IsFixedWidth");
        
    }
    
    /** Set Group by.
    @param IsGroupBy After a group change, totals, etc. are printed */
    public void setIsGroupBy (boolean IsGroupBy)
    {
        set_Value ("IsGroupBy", Boolean.valueOf(IsGroupBy));
        
    }
    
    /** Get Group by.
    @return After a group change, totals, etc. are printed */
    public boolean isGroupBy() 
    {
        return get_ValueAsBoolean("IsGroupBy");
        
    }
    
    /** Set One Line Only.
    @param IsHeightOneLine If selected, only one line is printed */
    public void setIsHeightOneLine (boolean IsHeightOneLine)
    {
        set_Value ("IsHeightOneLine", Boolean.valueOf(IsHeightOneLine));
        
    }
    
    /** Get One Line Only.
    @return If selected, only one line is printed */
    public boolean isHeightOneLine() 
    {
        return get_ValueAsBoolean("IsHeightOneLine");
        
    }
    
    /** Set Image Field.
    @param IsImageField The image is retrieved from the data column */
    public void setIsImageField (boolean IsImageField)
    {
        set_Value ("IsImageField", Boolean.valueOf(IsImageField));
        
    }
    
    /** Get Image Field.
    @return The image is retrieved from the data column */
    public boolean isImageField() 
    {
        return get_ValueAsBoolean("IsImageField");
        
    }
    
    /** Set Calculate Maximum (↑).
    @param IsMaxCalc Calculate the maximum amount */
    public void setIsMaxCalc (boolean IsMaxCalc)
    {
        set_Value ("IsMaxCalc", Boolean.valueOf(IsMaxCalc));
        
    }
    
    /** Get Calculate Maximum (↑).
    @return Calculate the maximum amount */
    public boolean isMaxCalc() 
    {
        return get_ValueAsBoolean("IsMaxCalc");
        
    }
    
    /** Set Calculate Minimum (↓).
    @param IsMinCalc Calculate the minimum amount */
    public void setIsMinCalc (boolean IsMinCalc)
    {
        set_Value ("IsMinCalc", Boolean.valueOf(IsMinCalc));
        
    }
    
    /** Get Calculate Minimum (↓).
    @return Calculate the minimum amount */
    public boolean isMinCalc() 
    {
        return get_ValueAsBoolean("IsMinCalc");
        
    }
    
    /** Set Next Line.
    @param IsNextLine Print item on next line */
    public void setIsNextLine (boolean IsNextLine)
    {
        set_Value ("IsNextLine", Boolean.valueOf(IsNextLine));
        
    }
    
    /** Get Next Line.
    @return Print item on next line */
    public boolean isNextLine() 
    {
        return get_ValueAsBoolean("IsNextLine");
        
    }
    
    /** Set Next Page.
    @param IsNextPage The column is printed on the next page */
    public void setIsNextPage (boolean IsNextPage)
    {
        set_Value ("IsNextPage", Boolean.valueOf(IsNextPage));
        
    }
    
    /** Get Next Page.
    @return The column is printed on the next page */
    public boolean isNextPage() 
    {
        return get_ValueAsBoolean("IsNextPage");
        
    }
    
    /** Set Order by.
    @param IsOrderBy Include in sort order */
    public void setIsOrderBy (boolean IsOrderBy)
    {
        set_Value ("IsOrderBy", Boolean.valueOf(IsOrderBy));
        
    }
    
    /** Get Order by.
    @return Include in sort order */
    public boolean isOrderBy() 
    {
        return get_ValueAsBoolean("IsOrderBy");
        
    }
    
    /** Set Page break.
    @param IsPageBreak Start with new page */
    public void setIsPageBreak (boolean IsPageBreak)
    {
        set_Value ("IsPageBreak", Boolean.valueOf(IsPageBreak));
        
    }
    
    /** Get Page break.
    @return Start with new page */
    public boolean isPageBreak() 
    {
        return get_ValueAsBoolean("IsPageBreak");
        
    }
    
    /** Set Printed.
    @param IsPrinted Indicates if this document / line is printed */
    public void setIsPrinted (boolean IsPrinted)
    {
        set_Value ("IsPrinted", Boolean.valueOf(IsPrinted));
        
    }
    
    /** Get Printed.
    @return Indicates if this document / line is printed */
    public boolean isPrinted() 
    {
        return get_ValueAsBoolean("IsPrinted");
        
    }
    
    /** Set Relative Position.
    @param IsRelativePosition The item is relative positioned (not absolute) */
    public void setIsRelativePosition (boolean IsRelativePosition)
    {
        set_Value ("IsRelativePosition", Boolean.valueOf(IsRelativePosition));
        
    }
    
    /** Get Relative Position.
    @return The item is relative positioned (not absolute) */
    public boolean isRelativePosition() 
    {
        return get_ValueAsBoolean("IsRelativePosition");
        
    }
    
    /** Set Running Total.
    @param IsRunningTotal Create a running total (sum) */
    public void setIsRunningTotal (boolean IsRunningTotal)
    {
        set_Value ("IsRunningTotal", Boolean.valueOf(IsRunningTotal));
        
    }
    
    /** Get Running Total.
    @return Create a running total (sum) */
    public boolean isRunningTotal() 
    {
        return get_ValueAsBoolean("IsRunningTotal");
        
    }
    
    /** Set Set NL Position.
    @param IsSetNLPosition Set New Line Position */
    public void setIsSetNLPosition (boolean IsSetNLPosition)
    {
        set_Value ("IsSetNLPosition", Boolean.valueOf(IsSetNLPosition));
        
    }
    
    /** Get Set NL Position.
    @return Set New Line Position */
    public boolean isSetNLPosition() 
    {
        return get_ValueAsBoolean("IsSetNLPosition");
        
    }
    
    /** Set Calculate Sum (Σ).
    @param IsSummarized Calculate the Sum of numeric content or length */
    public void setIsSummarized (boolean IsSummarized)
    {
        set_Value ("IsSummarized", Boolean.valueOf(IsSummarized));
        
    }
    
    /** Get Calculate Sum (Σ).
    @return Calculate the Sum of numeric content or length */
    public boolean isSummarized() 
    {
        return get_ValueAsBoolean("IsSummarized");
        
    }
    
    /** Set Suppress Null.
    @param IsSuppressNull Suppress columns or elements with NULL value */
    public void setIsSuppressNull (boolean IsSuppressNull)
    {
        set_Value ("IsSuppressNull", Boolean.valueOf(IsSuppressNull));
        
    }
    
    /** Get Suppress Null.
    @return Suppress columns or elements with NULL value */
    public boolean isSuppressNull() 
    {
        return get_ValueAsBoolean("IsSuppressNull");
        
    }
    
    /** Set Calculate Variance (σ²).
    @param IsVarianceCalc Calculate Variance */
    public void setIsVarianceCalc (boolean IsVarianceCalc)
    {
        set_Value ("IsVarianceCalc", Boolean.valueOf(IsVarianceCalc));
        
    }
    
    /** Get Calculate Variance (σ²).
    @return Calculate Variance */
    public boolean isVarianceCalc() 
    {
        return get_ValueAsBoolean("IsVarianceCalc");
        
    }
    
    /** Center = C */
    public static final String LINEALIGNMENTTYPE_Center = X_Ref_AD_Print_Line_Alignment.CENTER.getValue();
    /** Leading (left) = L */
    public static final String LINEALIGNMENTTYPE_LeadingLeft = X_Ref_AD_Print_Line_Alignment.LEADING_LEFT.getValue();
    /** Trailing (right) = T */
    public static final String LINEALIGNMENTTYPE_TrailingRight = X_Ref_AD_Print_Line_Alignment.TRAILING_RIGHT.getValue();
    /** None = X */
    public static final String LINEALIGNMENTTYPE_None = X_Ref_AD_Print_Line_Alignment.NONE.getValue();
    /** Set Line Alignment.
    @param LineAlignmentType Line Alignment */
    public void setLineAlignmentType (String LineAlignmentType)
    {
        if (LineAlignmentType == null) throw new IllegalArgumentException ("LineAlignmentType is mandatory");
        if (!X_Ref_AD_Print_Line_Alignment.isValid(LineAlignmentType))
        throw new IllegalArgumentException ("LineAlignmentType Invalid value - " + LineAlignmentType + " - Reference_ID=254 - C - L - T - X");
        set_Value ("LineAlignmentType", LineAlignmentType);
        
    }
    
    /** Get Line Alignment.
    @return Line Alignment */
    public String getLineAlignmentType() 
    {
        return (String)get_Value("LineAlignmentType");
        
    }
    
    /** Set Line Width.
    @param LineWidth Width of the lines */
    public void setLineWidth (int LineWidth)
    {
        set_Value ("LineWidth", Integer.valueOf(LineWidth));
        
    }
    
    /** Get Line Width.
    @return Width of the lines */
    public int getLineWidth() 
    {
        return get_ValueAsInt("LineWidth");
        
    }
    
    /** Set Max Height.
    @param MaxHeight Maximum Height in 1/72 if an inch - 0 = no restriction */
    public void setMaxHeight (int MaxHeight)
    {
        set_Value ("MaxHeight", Integer.valueOf(MaxHeight));
        
    }
    
    /** Get Max Height.
    @return Maximum Height in 1/72 if an inch - 0 = no restriction */
    public int getMaxHeight() 
    {
        return get_ValueAsInt("MaxHeight");
        
    }
    
    /** Set Max Width.
    @param MaxWidth Maximum Width in 1/72 if an inch - 0 = no restriction */
    public void setMaxWidth (int MaxWidth)
    {
        set_Value ("MaxWidth", Integer.valueOf(MaxWidth));
        
    }
    
    /** Get Max Width.
    @return Maximum Width in 1/72 if an inch - 0 = no restriction */
    public int getMaxWidth() 
    {
        return get_ValueAsInt("MaxWidth");
        
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
    
    /** Content = C */
    public static final String PRINTAREATYPE_Content = X_Ref_AD_Print_Area.CONTENT.getValue();
    /** Footer = F */
    public static final String PRINTAREATYPE_Footer = X_Ref_AD_Print_Area.FOOTER.getValue();
    /** Header = H */
    public static final String PRINTAREATYPE_Header = X_Ref_AD_Print_Area.HEADER.getValue();
    /** Set Area.
    @param PrintAreaType Print Area */
    public void setPrintAreaType (String PrintAreaType)
    {
        if (PrintAreaType == null) throw new IllegalArgumentException ("PrintAreaType is mandatory");
        if (!X_Ref_AD_Print_Area.isValid(PrintAreaType))
        throw new IllegalArgumentException ("PrintAreaType Invalid value - " + PrintAreaType + " - Reference_ID=256 - C - F - H");
        set_Value ("PrintAreaType", PrintAreaType);
        
    }
    
    /** Get Area.
    @return Print Area */
    public String getPrintAreaType() 
    {
        return (String)get_Value("PrintAreaType");
        
    }
    
    /** Field = F */
    public static final String PRINTFORMATTYPE_Field = X_Ref_AD_Print_Format_Type.FIELD.getValue();
    /** Image = I */
    public static final String PRINTFORMATTYPE_Image = X_Ref_AD_Print_Format_Type.IMAGE.getValue();
    /** Line = L */
    public static final String PRINTFORMATTYPE_Line = X_Ref_AD_Print_Format_Type.LINE.getValue();
    /** Print Format = P */
    public static final String PRINTFORMATTYPE_PrintFormat = X_Ref_AD_Print_Format_Type.PRINT_FORMAT.getValue();
    /** Rectangle = R */
    public static final String PRINTFORMATTYPE_Rectangle = X_Ref_AD_Print_Format_Type.RECTANGLE.getValue();
    /** Text = T */
    public static final String PRINTFORMATTYPE_Text = X_Ref_AD_Print_Format_Type.TEXT.getValue();
    /** Set Format Type.
    @param PrintFormatType Print Format Type */
    public void setPrintFormatType (String PrintFormatType)
    {
        if (PrintFormatType == null) throw new IllegalArgumentException ("PrintFormatType is mandatory");
        if (!X_Ref_AD_Print_Format_Type.isValid(PrintFormatType))
        throw new IllegalArgumentException ("PrintFormatType Invalid value - " + PrintFormatType + " - Reference_ID=255 - F - I - L - P - R - T");
        set_Value ("PrintFormatType", PrintFormatType);
        
    }
    
    /** Get Format Type.
    @return Print Format Type */
    public String getPrintFormatType() 
    {
        return (String)get_Value("PrintFormatType");
        
    }
    
    /** Set Print Text.
    @param PrintName The label text to be printed on a document or correspondence. */
    public void setPrintName (String PrintName)
    {
        set_Value ("PrintName", PrintName);
        
    }
    
    /** Get Print Text.
    @return The label text to be printed on a document or correspondence. */
    public String getPrintName() 
    {
        return (String)get_Value("PrintName");
        
    }
    
    /** Set Print Label Suffix.
    @param PrintNameSuffix The label text to be printed on a document or correspondence after the field */
    public void setPrintNameSuffix (String PrintNameSuffix)
    {
        set_Value ("PrintNameSuffix", PrintNameSuffix);
        
    }
    
    /** Get Print Label Suffix.
    @return The label text to be printed on a document or correspondence after the field */
    public String getPrintNameSuffix() 
    {
        return (String)get_Value("PrintNameSuffix");
        
    }
    
    /** Set Running Total Lines.
    @param RunningTotalLines Create Running Total Lines (page break) every x lines */
    public void setRunningTotalLines (int RunningTotalLines)
    {
        set_Value ("RunningTotalLines", Integer.valueOf(RunningTotalLines));
        
    }
    
    /** Get Running Total Lines.
    @return Create Running Total Lines (page break) every x lines */
    public int getRunningTotalLines() 
    {
        return get_ValueAsInt("RunningTotalLines");
        
    }
    
    /** Set Sequence.
    @param SeqNo Method of ordering elements;
     lowest number comes first */
    public void setSeqNo (int SeqNo)
    {
        set_Value ("SeqNo", Integer.valueOf(SeqNo));
        
    }
    
    /** Get Sequence.
    @return Method of ordering elements;
     lowest number comes first */
    public int getSeqNo() 
    {
        return get_ValueAsInt("SeqNo");
        
    }
    
    /** 3D Rectangle = 3 */
    public static final String SHAPETYPE_3DRectangle = X_Ref_AD_PrintFormatItem_ShapeType._3_D_RECTANGLE.getValue();
    /** Normal Rectangle = N */
    public static final String SHAPETYPE_NormalRectangle = X_Ref_AD_PrintFormatItem_ShapeType.NORMAL_RECTANGLE.getValue();
    /** Oval = O */
    public static final String SHAPETYPE_Oval = X_Ref_AD_PrintFormatItem_ShapeType.OVAL.getValue();
    /** Round Rectangle = R */
    public static final String SHAPETYPE_RoundRectangle = X_Ref_AD_PrintFormatItem_ShapeType.ROUND_RECTANGLE.getValue();
    /** Set Shape Type.
    @param ShapeType Type of the shape to be painted */
    public void setShapeType (String ShapeType)
    {
        if (!X_Ref_AD_PrintFormatItem_ShapeType.isValid(ShapeType))
        throw new IllegalArgumentException ("ShapeType Invalid value - " + ShapeType + " - Reference_ID=333 - 3 - N - O - R");
        set_Value ("ShapeType", ShapeType);
        
    }
    
    /** Get Shape Type.
    @return Type of the shape to be painted */
    public String getShapeType() 
    {
        return (String)get_Value("ShapeType");
        
    }
    
    /** Set Record Sort No.
    @param SortNo Determines in what order the records are displayed */
    public void setSortNo (int SortNo)
    {
        set_Value ("SortNo", Integer.valueOf(SortNo));
        
    }
    
    /** Get Record Sort No.
    @return Determines in what order the records are displayed */
    public int getSortNo() 
    {
        return get_ValueAsInt("SortNo");
        
    }
    
    /** Set X Position.
    @param XPosition Absolute X (horizontal) position in 1/72 of an inch */
    public void setXPosition (int XPosition)
    {
        set_Value ("XPosition", Integer.valueOf(XPosition));
        
    }
    
    /** Get X Position.
    @return Absolute X (horizontal) position in 1/72 of an inch */
    public int getXPosition() 
    {
        return get_ValueAsInt("XPosition");
        
    }
    
    /** Set X Space.
    @param XSpace Relative X (horizontal) space in 1/72 of an inch */
    public void setXSpace (int XSpace)
    {
        set_Value ("XSpace", Integer.valueOf(XSpace));
        
    }
    
    /** Get X Space.
    @return Relative X (horizontal) space in 1/72 of an inch */
    public int getXSpace() 
    {
        return get_ValueAsInt("XSpace");
        
    }
    
    /** Set Y Position.
    @param YPosition Absolute Y (vertical) position in 1/72 of an inch */
    public void setYPosition (int YPosition)
    {
        set_Value ("YPosition", Integer.valueOf(YPosition));
        
    }
    
    /** Get Y Position.
    @return Absolute Y (vertical) position in 1/72 of an inch */
    public int getYPosition() 
    {
        return get_ValueAsInt("YPosition");
        
    }
    
    /** Set Y Space.
    @param YSpace Relative Y (vertical) space in 1/72 of an inch */
    public void setYSpace (int YSpace)
    {
        set_Value ("YSpace", Integer.valueOf(YSpace));
        
    }
    
    /** Get Y Space.
    @return Relative Y (vertical) space in 1/72 of an inch */
    public int getYSpace() 
    {
        return get_ValueAsInt("YSpace");
        
    }
    
    
}
