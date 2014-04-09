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
 *  @version    $Id: PlafRes_da.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class PlafRes_da extends ListResourceBundle
{
	/** The data    */
	static final Object[][] contents = new String[][]
	{
	{ "BackColType",            "Baggrund: Farvetype" },
	{ "BackColType_Flat",       "Fast" },
	{ "BackColType_Gradient",   "Farveforl\u00c3\u00b8b" },
	{ "BackColType_Lines",      "Linjer" },
	{ "BackColType_Texture",    "Struktur" },
	//
	{ "LookAndFeelEditor",      "Redig\u00c3\u00a9r & udseende" },
	{ "LookAndFeel",            "Udseende" },
	{ "Theme",                  "Tema" },
	{ "EditCompiereTheme",      "Redig\u00c3\u00a9r Compiere tema" },
	{ "SetDefault",             "Baggrund: Standard" },
	{ "SetDefaultColor",        "Baggrundsfarve" },
	{ "ColorBlind",             "Farvereduktion" },
	{ "Example",                "Eksempel" },
	{ "Reset",                  "Gendan" },
	{ "OK",                     "OK" },
	{ "Cancel",                 "Annull\u00c3\u00a9r" },
	//
	{ "CompiereThemeEditor",    "Compiere-tema: Redig\u00c3\u00a9r" },
	{ "MetalColors",            "Metal-farver" },
	{ "CompiereColors",         "Compiere-farver" },
	{ "CompiereFonts",          "Compiere-skrifttyper" },
	{ "Primary1Info",           "Skygge, Separator" },
	{ "Primary1",               "Prim\u00c3\u00a6r 1" },
	{ "Primary2Info",           "Markeret element, Markeret menu" },
	{ "Primary2",               "Prim\u00c3\u00a6r 2" },
	{ "Primary3Info",           "Markeret r\u00c3\u00a6kke i tabel, Markeret tekst, V\u00c3\u00a6rkt\u00c3\u00b8jstip - baggr." },
	{ "Primary3",               "Prim\u00ef\u00bf\u00bdr 3" },
	{ "Secondary1Info",         "Rammelinjer" },
	{ "Secondary1",             "Sekund\u00c3\u00a6r 1" },
	{ "Secondary2Info",         "Ikke-aktive faner, Markerede felter, Ikke-aktiv ramme + tekst" },
	{ "Secondary2",             "Sekund\u00c3\u00a6r 2" },
	{ "Secondary3Info",         "Baggrund" },
	{ "Secondary3",             "Sekund\u00c3\u00a6r 3" },
	//
	{ "ControlFontInfo",        "Skrifttype: Knapper" },
	{ "ControlFont",            "Skrifttype: Etiket" },
	{ "SystemFontInfo",         "V\u00c3\u00a6rkt\u00c3\u00b8jstip, Strukturknuder" },
	{ "SystemFont",             "Skrifttype: System" },
	{ "UserFontInfo",           "Anvend" },
	{ "UserFont",               "Skrifttype: Felt" },
//	{ "SmallFontInfo",          "Rapporter" },
	{ "SmallFont",              "Lille" },
	{ "WindowTitleFont",         "Skrifttype: Titellinje" },
	{ "MenuFont",               "Skrifttype: Menu" },
	//
	{ "MandatoryInfo",          "Tvungen feltbaggrund" },
	{ "Mandatory",              "Tvungen" },
	{ "ErrorInfo",              "Fejl: Feltbaggrund" },
	{ "Error",                  "Fejl" },
	{ "InfoInfo",               "Info: Feltbaggrund" },
	{ "Info",                   "Info" },
	{ "WhiteInfo",              "Linjer" },
	{ "White",                  "Hvid" },
	{ "BlackInfo",              "Linjer, Tekst" },
	{ "Black",                  "Sort" },
	{ "InactiveInfo",           "Inaktiv feltbaggrund" },
	{ "Inactive",               "Inaktiv" },
	{ "TextOKInfo",             "OK: Tekstforgrund" },
	{ "TextOK",                 "Tekst: OK" },
	{ "TextIssueInfo",          "Fejl: Tekstforgrund" },
	{ "TextIssue",              "Tekst: Fejl" },
	//
	{ "FontChooser",            "Skriftype" },
	{ "Fonts",                  "Skrifttyper" },
	{ "Plain",                  "Normal" },
	{ "Italic",                 "Kursiv" },
	{ "Bold",                   "Fed" },
	{ "BoldItalic",             "Fed & kursiv" },
	{ "Name",                   "Navn" },
	{ "Size",                   "St\u00c3\u00b8rrelse" },
	{ "Style",                  "Type" },
	{ "TestString",             "Dette er en pr\u00c3\u00b8ve! 12.3456,78 BogstavLEn = l1 BogstavONul = O0" },
	{ "FontString",             "Skrifttype" },
	//
	{ "CompiereColorEditor",    "Compiere-farveeditor" },
	{ "CompiereType",           "Farvetype" },
	{ "GradientUpperColor",     "Farveforl\u00c3\u00b8b: Farve 1" },
	{ "GradientLowerColor",     "Farveforl\u00c3\u00b8b: Farve 2" },
	{ "GradientStart",          "Farveforl\u00c3\u00b8b: Start" },
	{ "GradientDistance",       "Farveforl\u00c3\u00b8b: Afstand" },
	{ "TextureURL",             "Struktur: URL" },
	{ "TextureAlpha",           "Struktur: Alpha" },
	{ "TextureTaintColor",      "Struktur: Pletvis" },
	{ "LineColor",              "Linjefarve" },
	{ "LineBackColor",          "Baggrundsfarve" },
	{ "LineWidth",              "Linjebredde" },
	{ "LineDistance",           "Linjeafstand" },
	{ "FlatColor",              "Fast farve" }
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
