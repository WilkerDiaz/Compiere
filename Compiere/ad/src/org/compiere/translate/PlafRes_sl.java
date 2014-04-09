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
 *  @version    $Id: PlafRes_sl.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class PlafRes_sl extends ListResourceBundle
{
	/** The data    */
	static final Object[][] contents = new String[][]
	{
	{ "BackColType",            "Tip ozadja" },
	{ "BackColType_Flat",       "Barva" },
	{ "BackColType_Gradient",   "Preliv" },
	{ "BackColType_Lines",      "\u010crte" },
	{ "BackColType_Texture",    "Tekstura" },
	//
	{ "LookAndFeelEditor",      "Urejevalnik izgleda" },
	{ "LookAndFeel",            "Izgled" },
	{ "Theme",                  "Tema" },
	{ "EditCompiereTheme",      "Urejanje Compiere teme" },
	{ "SetDefault",             "Privzeto ozadje" },
	{ "SetDefaultColor",        "Barva ozadja" },
	{ "ColorBlind",             "Color Deficiency" },
	{ "Example",                "Primer" },
	{ "Reset",                  "Ponastavi" },
	{ "OK",                     "V redu" },
	{ "Cancel",                 "Prekli\u010di" },
	//
	{ "CompiereThemeEditor",    "Urejanje Compiere teme" },
	{ "MetalColors",            "Barve Metal" },
	{ "CompiereColors",         "Barve Compiere" },
	{ "CompiereFonts",          "Compiere pisave" },
	{ "Primary1Info",           "Sen\u010denje, Lo\u010dilo" },
	{ "Primary1",               "Primarna 1" },
	{ "Primary2Info",           "\u010crta s fokusom, Izbran meni" },
	{ "Primary2",               "Primarna 2" },
	{ "Primary3Info",           "Izbrana vrstice tabele, Izbran tekst, Ozadje nasvetov" },
	{ "Primary3",               "Primarna 3" },
	{ "Secondary1Info",         "\u010crte okvirja" },
	{ "Secondary1",             "Sekundarno 1" },
	{ "Secondary2Info",         "Neaktivni zavihki, Izbrana (pritisnjena) polja, Neaktivne meje + tekst" },
	{ "Secondary2",             "Sekundarno 2" },
	{ "Secondary3Info",         "Ozadje" },
	{ "Secondary3",             "Sekundarno 3" },
	//
	{ "ControlFontInfo",        "Pisava za kontrole" },
	{ "ControlFont",            "Pisava za oznake" },
	{ "SystemFontInfo",         "Nasveti, listi drevesa" },
	{ "SystemFont",             "Sistemska pisava" },
	{ "UserFontInfo",           "Uporabni\u0161ko vne\u0161eni podatki" },
	{ "UserFont",               "Pisava za polje" },
//	{ "SmallFontInfo",          "Reporti" },
	{ "SmallFont",              "Majhna pisava" },
	{ "WindowTitleFont",        "Pisava naslovov" },
	{ "MenuFont",               "Pisava menija" },
	//
	{ "MandatoryInfo",          "Ozadje obveznega polja" },
	{ "Mandatory",              "Obvezno" },
	{ "ErrorInfo",              "Ozadje polja z napako" },
	{ "Error",                  "Napaka" },
	{ "InfoInfo",               "Ozadje informacijskih polj" },
	{ "Info",                   "Informacije" },
	{ "WhiteInfo",              "\u010crte" },
	{ "White",                  "Belo" },
	{ "BlackInfo",              "\u010crte, tekst" },
	{ "Black",                  "\u010crno" },
	{ "InactiveInfo",           "Ozadje neaktivnih polj" },
	{ "Inactive",               "Neaktivno" },
	{ "TextOKInfo",             "Barva pisave polja V redu" },
	{ "TextOK",                 "Tekst - V redu" },
	{ "TextIssueInfo",          "Barva teksta napake" },
	{ "TextIssue",              "Tekst - Napaka" },
	//
	{ "FontChooser",            "Font Chooser" },
	{ "Fonts",                  "Fonts" },
	{ "Plain",                  "Plain" },
	{ "Italic",                 "Italic" },
	{ "Bold",                   "Bold" },
	{ "BoldItalic",             "Bold & Italic" },
	{ "Name",                   "Ime" },
	{ "Size",                   "Velikost" },
	{ "Style",                  "Stil" },
	{ "TestString",             "To je samo test. The quick brown Fox is doing something. 12,3456.78 LetterLOne = l1 LetterOZero = O0" },
	{ "FontString",             "Font" },
	//
	{ "CompiereColorEditor",    "Compiere Color Editor" },
	{ "CompiereType",           "Color Type" },
	{ "GradientUpperColor",     "Gradient Upper Color" },
	{ "GradientLowerColor",     "Gradient Lower Color" },
	{ "GradientStart",          "Gradient Start" },
	{ "GradientDistance",       "Gradient Distance" },
	{ "TextureURL",             "Texture URL" },
	{ "TextureAlpha",           "Texture Alpha" },
	{ "TextureTaintColor",      "Texture Taint Color" },
	{ "LineColor",              "Line Color" },
	{ "LineBackColor",          "Background Color" },
	{ "LineWidth",              "Line Width" },
	{ "LineDistance",           "Line Distance" },
	{ "FlatColor",              "Flat Color" }
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

