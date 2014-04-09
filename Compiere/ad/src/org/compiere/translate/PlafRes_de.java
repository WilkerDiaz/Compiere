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
 *  Translation Texts for Look & Feel (German)
 *
 *  @version    $Id: PlafRes_de.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class PlafRes_de extends ListResourceBundle
{
	/** The data    */
	static final Object[][] contents = new String[][]
	{
	{ "BackColType",            "Hintergrund Farbtyp" },
	{ "BackColType_Flat",       "Flach" },
	{ "BackColType_Gradient",   "Gradient" },
	{ "BackColType_Lines",      "Linie" },
	{ "BackColType_Texture",    "Textur" },
	//
	{ "LookAndFeelEditor",      "Benutzeroberfl\u00e4che Editor" },
	{ "LookAndFeel",            "Oberfl\u00e4che" },
	{ "Theme",                  "Thema" },
	{ "EditCompiereTheme",      "Compiere Thema bearbeiten" },
	{ "SetDefault",             "Standard Hintergrund" },
	{ "SetDefaultColor",        "Hintergrund Farbe" },
	{ "ColorBlind",             "Farbblindheit" },
	{ "Example",                "Beispiel" },
	{ "Reset",                  "Zur\u00fccksetzen" },
	{ "OK",                     "Ja" },
	{ "Cancel",                 "Abbruch" },
	//
	{ "CompiereThemeEditor",    "Compiere Thema Editor" },
	{ "MetalColors",            "Metal Farben" },
	{ "CompiereColors",         "Compiere Farben" },
	{ "CompiereFonts",          "Compiere Schriften" },
	{ "Primary1Info",           "Schatten, Separator" },
	{ "Primary1",               "Prim\u00e4r 1" },
	{ "Primary2Info",           "Fokuslinien, Aktives Men\u00fc" },
	{ "Primary2",               "Prim\u00e4r 2" },
	{ "Primary3Info",           "Tabelle Selected Row, Selected Text, Tool Tip Hintergrund" },
	{ "Primary3",               "Prim\u00e4r 3" },
	{ "Secondary1Info",         "Rahmen Linien" },
	{ "Secondary1",             "Sekund\u00e4r 1" },
	{ "Secondary2Info",         "Inaktive Tabs, Pressed Felder, Inaktive Rahmen + Text" },
	{ "Secondary2",             "Sekund\u00e4r 2" },
	{ "Secondary3Info",         "Hintergrund" },
	{ "Secundary3",             "Sekund\u00e4r 3" },
	//
	{ "ControlFontInfo",        "Labels" },
	{ "ControlFont",            "Standard Schrift" },
	{ "SystemFontInfo",         "Tool Tip" },
	{ "SystemFont",             "System Schrift" },
	{ "UserFontInfo",           "Entered Data" },
	{ "UserFont",               "Nutzer Schrift" },
	{ "SmallFont",              "Kleine Schrift" },
	{ "WindowTitleFont",        "Titel Schrift" },
	{ "MenuFont",               "Men\u00fc Schrift" },
	//
	{ "MandatoryInfo",          "Erforderliches Feld Hintergrund" },
	{ "Mandatory",              "Erforderlich" },
	{ "ErrorInfo",              "Fehler Feld Hintergrund" },
	{ "Error",                  "Fehler" },
	{ "InfoInfo",               "Info Feld Hintergrund" },
	{ "Info",                   "Info" },
	{ "WhiteInfo",              "Linien" },
	{ "White",                  "Wei\u00df" },
	{ "BlackInfo",              "Linien, Text" },
	{ "Black",                  "Schwarz" },
	{ "InactiveInfo",           "Inaktiv Feld Hintergrund" },
	{ "Inactive",               "Inaktiv" },
	{ "TextOKInfo",             "OK Text Fordergrund" },
	{ "TextOK",                 "Text - OK" },
	{ "TextIssueInfo",          "Fehler Text Fordergrund" },
	{ "TextIssue",              "Text - Fehler" },
	//
	{ "FontChooser",            "Schrift Auswahl" },
	{ "Fonts",                  "Schriften" },
	{ "Plain",                  "Normal" },
	{ "Italic",                 "Italic" },
	{ "Bold",                   "Fett" },
	{ "BoldItalic",             "Fett & Italic" },
	{ "Name",                   "Name" },
	{ "Size",                   "Gr\u00f6\u00dfe" },
	{ "Style",                  "Stil" },
	{ "TestString",             "Dies ist nur ein Test! The quick brown Fox is doing something. 12,3456.78 BuchstabeLEins = l1 BuchstabeONull = O0" },
	{ "FontString",             "Schrift" },
	//
	{ "CompiereColorEditor",    "Compiere Farben Auswahl" },
	{ "CompiereType",           "Farbtyp" },
	{ "GradientUpperColor",     "Gradient obere Farbe" },
	{ "GradientLowerColor",     "Gradient untere Farbe" },
	{ "GradientStart",          "Gradient Start" },
	{ "GradientDistance",       "Gradient Distanz" },
	{ "TextureURL",             "Textur URL" },
	{ "TextureAlpha",           "Textur Alpha" },
	{ "TextureTaintColor",      "Textur T\u00f6nung Farbe" },
	{ "LineColor",              "Linie Farbe" },
	{ "LineBackColor",          "Hintergrund Farbe" },
	{ "LineWidth",              "Linie Breite" },
	{ "LineDistance",           "Linie Distanz" },
	{ "FlatColor",              "Flache Farbe" }
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
}   //  Res_de
