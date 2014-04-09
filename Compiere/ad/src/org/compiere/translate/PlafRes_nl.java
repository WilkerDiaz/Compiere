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
 *  @version    $Id: PlafRes_nl.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class PlafRes_nl extends ListResourceBundle
{
	/** The data    */
	static final Object[][] contents = new String[][]
	{
	{ "BackColType",            "Achtergrond Kleur Type" },
	{ "BackColType_Flat",       "Egaal" },
	{ "BackColType_Gradient",   "Verloop" },
	{ "BackColType_Lines",      "Lijnen" },
	{ "BackColType_Texture",    "Reli�f" },
	//
	{ "LookAndFeelEditor",      "Look & Feel Editor" },
	{ "LookAndFeel",            "Look & Feel" },
	{ "Theme",                  "Thema" },
	{ "EditCompiereTheme",      "Compiere Theme Bewerken" },
	{ "SetDefault",             "Standaard Achtergrond" },
	{ "SetDefaultColor",        "Achtergrond Kleur" },
	{ "ColorBlind",             "Kleur Verloop" },
	{ "Example",                "Voorbeeld" },
	{ "Reset",                  "Ongedaan maken" },
	{ "OK",                     "OK" },
	{ "Cancel",                 "Annuleren" },
	//
	{ "CompiereThemeEditor",    "Compiere Thema Editor" },
	{ "MetalColors",            "Metaal Kleuren" },
	{ "CompiereColors",         "Compiere Kleuren" },
	{ "CompiereFonts",          "Compiere Lettertypen" },
	{ "Primary1Info",           "Shaduw, Schijdingsteken" },
	{ "Primary1",               "Primair 1" },
	{ "Primary2Info",           "Schijdingslijn, Geselecteerd Menu" },
	{ "Primary2",               "Primair 2" },
	{ "Primary3Info",           "Tabel Geselecteerde Rij, Geselecteerde Tekst, ToolTip Achtergrond" },
	{ "Primary3",               "Primair 3" },
	{ "Secondary1Info",         "Begrenzing Lijnen" },
	{ "Secondary1",             "Secundair 2" },
	{ "Secondary2Info",         "Inactieve Tabs, Geselecteerde Velden, Inactieve Begrenzing + Tekst" },
	{ "Secondary2",             "Secundait 2" },
	{ "Secondary3Info",         "Achtergrond" },
	{ "Secondary3",             "Secondair 3" },
	//
	{ "ControlFontInfo",        "Beheer Lettertype" },
	{ "ControlFont",            "Label Lettertype" },
	{ "SystemFontInfo",         "Tool Tip, Boom Iconen" },
	{ "SystemFont",             "Systeem Letterype" },
	{ "UserFontInfo",           "Door Gebruiker ingevoerde gegevens" },
	{ "UserFont",               "Veld Lettertype" },
//	{ "SmallFontInfo",          "Reports" },
	{ "SmallFont",              "Klein Lettertype" },
	{ "WindowTitleFont",         "Titel Lettertype" },
	{ "MenuFont",               "Menu Lettertype" },
	//
	{ "MandatoryInfo",          "Verplicht Veld Achtergrond" },
	{ "Mandatory",              "Verplicht" },
	{ "ErrorInfo",              "Foutief Veld Achtergrond" },
	{ "Error",                  "Foutief" },
	{ "InfoInfo",               "Informatie Veld Achtergrond" },
	{ "Info",                   "Informatie" },
	{ "WhiteInfo",              "Lijnen" },
	{ "White",                  "Wit" },
	{ "BlackInfo",              "Lijnen, Tekst" },
	{ "Black",                  "Zwart" },
	{ "InactiveInfo",           "Inactief Veld Achtergrond" },
	{ "Inactive",               "Inactief" },
	{ "TextOKInfo",             "OK Tekst Voorgrond" },
	{ "TextOK",                 "Tekst - OK" },
	{ "TextIssueInfo",          "Foutief Tekst Voorgrond" },
	{ "TextIssue",              "Tekst - Foutief" },
	//
	{ "FontChooser",            "Lettertype Selecteren" },
	{ "Fonts",                  "Lettertypen" },
	{ "Plain",                  "Normaal" },
	{ "Italic",                 "Schuin" },
	{ "Bold",                   "Vet" },
	{ "BoldItalic",             "Vet & Schuin" },
	{ "Name",                   "Naam" },
	{ "Size",                   "Formaat" },
	{ "Style",                  "Stijl" },
	{ "TestString",             "Dit is een test! De thema brwoser is bezig. 12,3456.78 LetterLOne = l1 LetterOZero = O0" },
	{ "FontString",             "Lettertype" },
	//
	{ "CompiereColorEditor",    "Compiere Kleur Editor" },
	{ "CompiereType",           "Kleur Type" },
	{ "GradientUpperColor",     "Verloop Bovenste Kleur" },
	{ "GradientLowerColor",     "Verloop Onderste Kleur" },
	{ "GradientStart",          "Verloop Start" },
	{ "GradientDistance",       "Verloop Afstand" },
	{ "TextureURL",             "Textuur URL" },
	{ "TextureAlpha",           "Textuur Alpha" },
	{ "TextureTaintColor",      "Textuur Taint Kleur" },
	{ "LineColor",              "Lijn Kleur" },
	{ "LineBackColor",          "Achtergrond Kleur" },
	{ "LineWidth",              "Lijn Breedte" },
	{ "LineDistance",           "Lijn Reikwijdte" },
	{ "FlatColor",              "Egale Kleur" }
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

