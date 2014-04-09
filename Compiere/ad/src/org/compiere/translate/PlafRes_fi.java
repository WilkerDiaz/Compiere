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
 *  Translation Texts for Look & Feel for Finnish Language
 *
 * 	@version 	$Id: PlafRes_fi.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class PlafRes_fi extends ListResourceBundle
{
	/** 
    * Data 
    */
	static final Object[][] contents = new String[][]
	{
	{ "BackColType",            "Taustan v\u00e4rityyppi" },
	{ "BackColType_Flat",       "Yksiv\u00e4ri" },
	{ "BackColType_Gradient",   "Gradientti" },
	{ "BackColType_Lines",      "Viiva" },
	{ "BackColType_Texture",    "Kuviointi" },
	//
	{ "LookAndFeelEditor",      "K\u00e4ytt\u00f6tuntuman muokkaus" },
	{ "LookAndFeel",            "K\u00e4ytt\u00f6tuntuma" },
	{ "Theme",                  "Teema" },
	{ "EditCompiereTheme",      "Muokkaa Compiere-teemaa" },
	{ "SetDefault",             "Oletustaustav\u00e4ri" },
	{ "SetDefaultColor",        "Taustav\u00e4ri" },
	{ "ColorBlind",             "V\u00e4risokeus" },
	{ "Example",                "Esimerkki" },
	{ "Reset",                  "Nollaa" },
	{ "OK",                     "Hyv\u00e4ksy" },
	{ "Cancel",                 "Peruuta" },
	//
	{ "CompiereThemeEditor",    "Compiere-teeman muokkaus" },
	{ "MetalColors",            "Metalliv\u00e4rit" },
	{ "CompiereColors",         "Compiere-v\u00e4rit" },
	{ "CompiereFonts",          "Compiere-kirjasimet" },
	{ "Primary1Info",           "Varjostin, Erotin" },
	{ "Primary1",               "Ensisijainen 1" },
	{ "Primary2Info",           "Focus-viiva, Valitty Valikko" },
	{ "Primary2",               "Ensisijainen 2" },
	{ "Primary3Info",           "Taulun Valittu Rivi, Valittu Teksti, ToolTip Tausta" },
	{ "Primary3",               "Ensisijainen 3" },
	{ "Secondary1Info",         "Reunaviivat" },
	{ "Secondary1",             "Toissijainen 1" },
	{ "Secondary2Info",         "Ei-aktiiviset Tabulaattorit, Painetut Kent\u00e4t, Ei-aktiivinen Reuna + Teksti" },
	{ "Secondary2",             "Toissijainen 2" },
	{ "Secondary3Info",         "Tausta" },
	{ "Secondary3",             "Toissijainen 3" },
	//
	{ "ControlFontInfo",        "Kontrollikirjasin" },
	{ "ControlFont",            "Nimikekirjasin" },
	{ "SystemFontInfo",         "Tool Tip, Puun Solmut" },
	{ "SystemFont",             "J\u00e4rjestelm\u00e4kirjasin" },
	{ "UserFontInfo",           "K\u00e4ytt\u00e4j\u00e4n Sy\u00f6tt\u00e4m\u00e4 Tieto" },
	{ "UserFont",               "Kentt\u00e4kirjasin" },
//	{ "SmallFontInfo",          "Raportit" },
	{ "SmallFont",              "Pieni Kirjasin" },
	{ "WindowTitleFont",         "Otsikkokirjasin" },
	{ "MenuFont",               "Valikkokirjasin" },
	//
	{ "MandatoryInfo",          "Pakollinen Kentt\u00e4tausta" },
	{ "Mandatory",              "Pakollinen" },
	{ "ErrorInfo",              "Virhekent\u00e4n Tausta" },
	{ "Error",                  "Virhe" },
	{ "InfoInfo",               "Tietokent\u00e4n Tausta" },
	{ "Info",                   "Tieto" },
	{ "WhiteInfo",              "Viivat" },
	{ "White",                  "Valkoinen" },
	{ "BlackInfo",              "Viivat, Teksti" },
	{ "Black",                  "Musta" },
	{ "InactiveInfo",           "Ei-aktiivinen Kentt\u00e4tausta" },
	{ "Inactive",               "Ei-aktiivinen" },
	{ "TextOKInfo",             "Hyv\u00e4ksy Teksti Edusta" },
	{ "TextOK",                 "Teksti - Hyv\u00e4ksy" },
	{ "TextIssueInfo",          "Virhetekstin Edusta" },
	{ "TextIssue",              "Teksti - Virhe" },
	//
	{ "FontChooser",            "Kirjasimen Valitsin" },
	{ "Fonts",                  "Kirjasimet" },
	{ "Plain",                  "Tavallinen" },
	{ "Italic",                 "Kursiivi" },
	{ "Bold",                   "Lihavoitu" },
	{ "BoldItalic",             "Lihavoitu ja Kursiivi" },
	{ "Name",                   "Nimi" },
	{ "Size",                   "Koko" },
	{ "Style",                  "Tyyli" },
	{ "TestString",             "T\u00e4m\u00e4 on vain Testi! Nopea ruskea Kettu suorittaa jotain. 12,3456.78 LetterLOne = l1 LetterOZero = O0" },
	{ "FontString",             "Kirjasin" },
	//
	{ "CompiereColorEditor",    "Compiere-v\u00e4rimuokkaus" },
	{ "CompiereType",           "V\u00e4rityyppi" },
	{ "GradientUpperColor",     "Gradientin Ylempi V\u00e4ri" },
	{ "GradientLowerColor",     "Gradientin Alempi V\u00e4ri" },
	{ "GradientStart",          "Gradientin Alku" },
	{ "GradientDistance",       "Gradientin Et\u00e4isyys" },
	{ "TextureURL",             "Kuvioinnin URL" },
	{ "TextureAlpha",           "Kuvioinnin Alpha" },
	{ "TextureTaintColor",      "Kuvioinnin Korvausv\u00e4ri" },
	{ "LineColor",              "Viivan V\u00e4ri" },
	{ "LineBackColor",          "Taustan V\u00e4ri" },
	{ "LineWidth",              "Viivan Paksuus" },
	{ "LineDistance",           "Viivan Et\u00e4isyys" },
	{ "FlatColor",              "Tavallinen V\u00e4ri" }
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

