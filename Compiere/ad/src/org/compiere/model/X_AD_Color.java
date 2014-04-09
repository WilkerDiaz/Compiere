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
/** Generated Model for AD_Color
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Color.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Color extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Color_ID id
    @param trx transaction
    */
    public X_AD_Color (Ctx ctx, int AD_Color_ID, Trx trx)
    {
        super (ctx, AD_Color_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Color_ID == 0)
        {
            setAD_Color_ID (0);
            setAlpha (0);
            setBlue (0);
            setColorType (null);
            setGreen (0);
            setImageAlpha (Env.ZERO);
            setIsDefault (false);
            setName (null);
            setRed (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Color (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=457 */
    public static final int Table_ID=457;
    
    /** TableName=AD_Color */
    public static final String Table_Name="AD_Color";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set System Color.
    @param AD_Color_ID Color for backgrounds or indicators */
    public void setAD_Color_ID (int AD_Color_ID)
    {
        if (AD_Color_ID < 1) throw new IllegalArgumentException ("AD_Color_ID is mandatory.");
        set_ValueNoCheck ("AD_Color_ID", Integer.valueOf(AD_Color_ID));
        
    }
    
    /** Get System Color.
    @return Color for backgrounds or indicators */
    public int getAD_Color_ID() 
    {
        return get_ValueAsInt("AD_Color_ID");
        
    }
    
    /** Set Image.
    @param AD_Image_ID Image or Icon */
    public void setAD_Image_ID (int AD_Image_ID)
    {
        if (AD_Image_ID <= 0) set_Value ("AD_Image_ID", null);
        else
        set_Value ("AD_Image_ID", Integer.valueOf(AD_Image_ID));
        
    }
    
    /** Get Image.
    @return Image or Icon */
    public int getAD_Image_ID() 
    {
        return get_ValueAsInt("AD_Image_ID");
        
    }
    
    /** Set Alpha.
    @param Alpha Color Alpha value 0-255 */
    public void setAlpha (int Alpha)
    {
        set_Value ("Alpha", Integer.valueOf(Alpha));
        
    }
    
    /** Get Alpha.
    @return Color Alpha value 0-255 */
    public int getAlpha() 
    {
        return get_ValueAsInt("Alpha");
        
    }
    
    /** Set 2nd Alpha.
    @param Alpha_1 Alpha value for second color */
    public void setAlpha_1 (int Alpha_1)
    {
        set_Value ("Alpha_1", Integer.valueOf(Alpha_1));
        
    }
    
    /** Get 2nd Alpha.
    @return Alpha value for second color */
    public int getAlpha_1() 
    {
        return get_ValueAsInt("Alpha_1");
        
    }
    
    /** Set Blue.
    @param Blue Color RGB blue value */
    public void setBlue (int Blue)
    {
        set_Value ("Blue", Integer.valueOf(Blue));
        
    }
    
    /** Get Blue.
    @return Color RGB blue value */
    public int getBlue() 
    {
        return get_ValueAsInt("Blue");
        
    }
    
    /** Set 2nd Blue.
    @param Blue_1 RGB value for second color */
    public void setBlue_1 (int Blue_1)
    {
        set_Value ("Blue_1", Integer.valueOf(Blue_1));
        
    }
    
    /** Get 2nd Blue.
    @return RGB value for second color */
    public int getBlue_1() 
    {
        return get_ValueAsInt("Blue_1");
        
    }
    
    /** Normal (Flat) = F */
    public static final String COLORTYPE_NormalFlat = X_Ref_AD_Color_Type.NORMAL_FLAT.getValue();
    /** Gradient = G */
    public static final String COLORTYPE_Gradient = X_Ref_AD_Color_Type.GRADIENT.getValue();
    /** Line = L */
    public static final String COLORTYPE_Line = X_Ref_AD_Color_Type.LINE.getValue();
    /** Texture (Picture) = T */
    public static final String COLORTYPE_TexturePicture = X_Ref_AD_Color_Type.TEXTURE_PICTURE.getValue();
    /** Set Color Type.
    @param ColorType Color presentation for this color */
    public void setColorType (String ColorType)
    {
        if (ColorType == null) throw new IllegalArgumentException ("ColorType is mandatory");
        if (!X_Ref_AD_Color_Type.isValid(ColorType))
        throw new IllegalArgumentException ("ColorType Invalid value - " + ColorType + " - Reference_ID=243 - F - G - L - T");
        set_Value ("ColorType", ColorType);
        
    }
    
    /** Get Color Type.
    @return Color presentation for this color */
    public String getColorType() 
    {
        return (String)get_Value("ColorType");
        
    }
    
    /** Set Green.
    @param Green RGB value */
    public void setGreen (int Green)
    {
        set_Value ("Green", Integer.valueOf(Green));
        
    }
    
    /** Get Green.
    @return RGB value */
    public int getGreen() 
    {
        return get_ValueAsInt("Green");
        
    }
    
    /** Set 2nd Green.
    @param Green_1 RGB value for second color */
    public void setGreen_1 (int Green_1)
    {
        set_Value ("Green_1", Integer.valueOf(Green_1));
        
    }
    
    /** Get 2nd Green.
    @return RGB value for second color */
    public int getGreen_1() 
    {
        return get_ValueAsInt("Green_1");
        
    }
    
    /** Set Image Alpha.
    @param ImageAlpha Image Texture Composite Alpha */
    public void setImageAlpha (java.math.BigDecimal ImageAlpha)
    {
        if (ImageAlpha == null) throw new IllegalArgumentException ("ImageAlpha is mandatory.");
        set_Value ("ImageAlpha", ImageAlpha);
        
    }
    
    /** Get Image Alpha.
    @return Image Texture Composite Alpha */
    public java.math.BigDecimal getImageAlpha() 
    {
        return get_ValueAsBigDecimal("ImageAlpha");
        
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
    
    /** Set Line Distance.
    @param LineDistance Distance between lines */
    public void setLineDistance (int LineDistance)
    {
        set_Value ("LineDistance", Integer.valueOf(LineDistance));
        
    }
    
    /** Get Line Distance.
    @return Distance between lines */
    public int getLineDistance() 
    {
        return get_ValueAsInt("LineDistance");
        
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
    
    /** Set Red.
    @param Red RGB value */
    public void setRed (int Red)
    {
        set_Value ("Red", Integer.valueOf(Red));
        
    }
    
    /** Get Red.
    @return RGB value */
    public int getRed() 
    {
        return get_ValueAsInt("Red");
        
    }
    
    /** Set 2nd Red.
    @param Red_1 RGB value for second color */
    public void setRed_1 (int Red_1)
    {
        set_Value ("Red_1", Integer.valueOf(Red_1));
        
    }
    
    /** Get 2nd Red.
    @return RGB value for second color */
    public int getRed_1() 
    {
        return get_ValueAsInt("Red_1");
        
    }
    
    /** Set Repeat Distance.
    @param RepeatDistance Distance in points to repeat gradient color - or zero */
    public void setRepeatDistance (int RepeatDistance)
    {
        set_Value ("RepeatDistance", Integer.valueOf(RepeatDistance));
        
    }
    
    /** Get Repeat Distance.
    @return Distance in points to repeat gradient color - or zero */
    public int getRepeatDistance() 
    {
        return get_ValueAsInt("RepeatDistance");
        
    }
    
    /** North = 1 */
    public static final String STARTPOINT_North = X_Ref_AD_Color_StartPoint.NORTH.getValue();
    /** North East = 2 */
    public static final String STARTPOINT_NorthEast = X_Ref_AD_Color_StartPoint.NORTH_EAST.getValue();
    /** East = 3 */
    public static final String STARTPOINT_East = X_Ref_AD_Color_StartPoint.EAST.getValue();
    /** South East = 4 */
    public static final String STARTPOINT_SouthEast = X_Ref_AD_Color_StartPoint.SOUTH_EAST.getValue();
    /** South = 5 */
    public static final String STARTPOINT_South = X_Ref_AD_Color_StartPoint.SOUTH.getValue();
    /** South West = 6 */
    public static final String STARTPOINT_SouthWest = X_Ref_AD_Color_StartPoint.SOUTH_WEST.getValue();
    /** West = 7 */
    public static final String STARTPOINT_West = X_Ref_AD_Color_StartPoint.WEST.getValue();
    /** North West = 8 */
    public static final String STARTPOINT_NorthWest = X_Ref_AD_Color_StartPoint.NORTH_WEST.getValue();
    /** Set Start Point.
    @param StartPoint Start point of the gradient colors */
    public void setStartPoint (String StartPoint)
    {
        if (!X_Ref_AD_Color_StartPoint.isValid(StartPoint))
        throw new IllegalArgumentException ("StartPoint Invalid value - " + StartPoint + " - Reference_ID=248 - 1 - 2 - 3 - 4 - 5 - 6 - 7 - 8");
        set_Value ("StartPoint", StartPoint);
        
    }
    
    /** Get Start Point.
    @return Start point of the gradient colors */
    public String getStartPoint() 
    {
        return (String)get_Value("StartPoint");
        
    }
    
    
}
