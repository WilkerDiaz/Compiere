/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.translate;

import java.util.*;

/**
 *  Translation Texts for Look & Feel
 *
 *  @version    $Id: PlafRes_vi.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class PlafRes_vi extends ListResourceBundle
{
	/** The data    */
	static final Object[][] contents = new String[][]
	{
	{ "BackColType",            "Lo\u1EA1i m\u00e0u n\u1EC1n" },
	{ "BackColType_Flat",       "Ph\u1EB3ng" },
	{ "BackColType_Gradient",   "D\u1ED1c" },
	{ "BackColType_Lines",      "\u0110\u01B0\u1EDDng th\u1EB3ng" },
	{ "BackColType_Texture",    "\u0110an ch\u00e9o" },
	//
	{ "LookAndFeelEditor",      "C\u00f4ng c\u1EE5 thay \u0111\u1ED5i V\u1EBB ngo\u00e0i v\u00e0 C\u1EA3m nh\u1EADn" },
	{ "LookAndFeel",            "V\u1EBB ngo\u00e0i v\u00e0 C\u1EA3m nh\u1EADn" },
	{ "Theme",                  "Ch\u1EE7 \u0111\u1EC1" },
	{ "EditCompiereTheme",      "Hi\u1EC7u ch\u1EC9nh ch\u1EE7 \u0111\u1EC1 C\u0103mpia-\u01A1" },
	{ "SetDefault",             "N\u1EC1n m\u1EB7c nhi\u00ean" },
	{ "SetDefaultColor",        "M\u00e0u n\u1EC1n" },
	{ "ColorBlind",             "M\u00f9 m\u00e0u" },
	{ "Example",                "V\u00ed d\u1EE5" },
	{ "Reset",                  "C\u1EA5u h\u0301nh l\u1EA1i" },
	{ "OK",                     "\u0110\u1ED3ng \u01b0" },
	{ "Cancel",                 "H\u1EE7y" },
	//
	{ "CompiereThemeEditor",    "C\u00f4ng c\u1EE5 thay \u0111\u1ED5i ch\u1EE7 \u0111\u1EC1 C\u0103mpia-\u01A1" },
	{ "MetalColors",            "B\u1ED9 m\u00e0u \u00e1nh kim" },
	{ "CompiereColors",         "B\u1ED9 m\u00e0u C\u0103mpia-\u01A1" },
	{ "CompiereFonts",          "B\u1ED9 Font C\u0103mpia-\u01A1" },
	{ "Primary1Info",           "B\u00f3ng, T\u00e1ch bi\u1EC7t" },
	{ "Primary1",               "Ch\u1EE7 \u0111\u1EA1o 1" },
	{ "Primary2Info",           "\u0110\u01B0\u1EDDng ti\u00eau \u0111i\u1EC3m, Th\u1EF1c \u0111\u01A1n \u0111\u01B0\u1EE3c ch\u1ECDn" },
	{ "Primary2",               "Ch\u1EE7 \u0111\u1EA1o 2" },
	{ "Primary3Info",           "Table Selected Row, Selected Text, ToolTip Background" },
	{ "Primary3",               "Primary 3" },
	{ "Secondary1Info",         "\u0110\u01B0\u1EDDng bi\u00ean" },
	{ "Secondary1",             "Ph\u1EE5 1" },
	{ "Secondary2Info",         "Inactive Tabs, Pressed Fields, Inactive Border + Text" },
	{ "Secondary2",             "Ph\u1EE5 2" },
	{ "Secondary3Info",         "N\u1EC1n" },
	{ "Secondary3",             "Ph\u1EE5 3" },
	//
	{ "ControlFontInfo",        "Control Font" },
	{ "ControlFont",            "Label Font" },
	{ "SystemFontInfo",         "Tool Tip, Tree nodes" },
	{ "SystemFont",             "System Font" },
	{ "UserFontInfo",           "User Entered Data" },
	{ "UserFont",               "Field Font" },
//	{ "SmallFontInfo",          "Reports" },
	{ "SmallFont",              "Small Font" },
	{ "WindowTitleFont",         "Title Font" },
	{ "MenuFont",               "Menu Font" },
	//
	{ "MandatoryInfo",          "N\u1EC1n c\u1EE7a tr\u01B0\u1EDDng b\u1EAFt bu\u1ED9c" },
	{ "Mandatory",              "B\u1EAFt bu\u1ED9c" },
	{ "ErrorInfo",              "N\u1EC1n c\u1EE7a tr\u01B0\u1EDDng b\u1ECB l\u1ED7i" },
	{ "Error",                  "L\u1ED7i" },
	{ "InfoInfo",               "N\u1EC1n c\u1EE7a tr\u01B0\u1EDDng th\u00f4ng tin" },
	{ "Info",                   "Th\u00f4ng tin" },
	{ "WhiteInfo",              "\u0110\u01B0\u1EDDng th\u1EB3ng" },
	{ "White",                  "Tr\u1EAFng" },
	{ "BlackInfo",              "\u0110\u01B0\u1EDDng th\u1EB3ng, \u0110o\u1EA1n v\u0103n" },
	{ "Black",                  "Black" },
	{ "InactiveInfo",           "Inactive Field Background" },
	{ "Inactive",               "Inactive" },
	{ "TextOKInfo",             "OK Text Foreground" },
	{ "TextOK",                 "Text - OK" },
	{ "TextIssueInfo",          "Error Text Foreground" },
	{ "TextIssue",              "Text - Error" },
	//
	{ "FontChooser",            "Ch\u1ECDn Font" },
	{ "Fonts",                  "Fonts" },
	{ "Plain",                  "Plain" },
	{ "Italic",                 "Italic" },
	{ "Bold",                   "Bold" },
	{ "BoldItalic",             "Bold & Italic" },
	{ "Name",                   "T\u00ean" },
	{ "Size",                   "K\u00edch th\u01B0\u1EDBc" },
	{ "Style",                  "Ki\u1EC3u" },
	{ "TestString",             "\u0110\u00e2y ch\u1EC9 l\u00e0 ph\u1EA7n th\u1EED nghi\u1EC7m" },
	{ "FontString",             "Font" },
	//
	{ "CompiereColorEditor",    "C\u00f4ng c\u1EE5 thay \u0111\u1ED5i m\u00e0u C\u0103mpia-\u01A1" },
	{ "CompiereType",           "Lo\u1EA1i m\u00e0u" },
	{ "GradientUpperColor",     "Gradient Upper Color" },
	{ "GradientLowerColor",     "Gradient Lower Color" },
	{ "GradientStart",          "Gradient Start" },
	{ "GradientDistance",       "Gradient Distance" },
	{ "TextureURL",             "Texture URL" },
	{ "TextureAlpha",           "Texture Alpha" },
	{ "TextureTaintColor",      "Texture Taint Color" },
	{ "LineColor",              "Line Color" },
	{ "LineBackColor",          "Background Color" },
	{ "LineWidth",              "Chi\u1EC1u r\u1ED9ng \u0111\u01B0\u1EDDng" },
	{ "LineDistance",           "Chi\u1EC1u xa \u0111\u01B0\u1EDDng" },
	{ "FlatColor",              "M\u00e0u ph\u1EB3ng" }
	};

	/**
	 * Get Contents
	 * @return contents
	 */
	@Override
	public Object[][] getContents()
	{
		return contents;
	}
}   //  Res
