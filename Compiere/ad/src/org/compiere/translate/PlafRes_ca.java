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
 *  @version    $Id: PlafRes_ca.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class PlafRes_ca extends ListResourceBundle
{
	/** The data    */
	static final Object[][] contents = new String[][]
	{
	{ "BackColType",            "Tipus Color Fons" },
	{ "BackColType_Flat",       "Pla" },
	{ "BackColType_Gradient",   "Gradient" },
	{ "BackColType_Lines",      "L\u00ednes" },
	{ "BackColType_Texture",    "Textura" },
	//
	{ "LookAndFeelEditor",      "Editor Aparen\u00e7a i Comportament" },
	{ "LookAndFeel",            "Aparen\u00e7a i Comportament" },
	{ "Theme",                  "Tema" },
	{ "EditCompiereTheme",      "Editar Tema Compiere" },
	{ "SetDefault",             "Fons Per Defecte" },
	{ "SetDefaultColor",        "Color Fons" },
	{ "ColorBlind",             "Defici\u00e8ncia Color" },
	{ "Example",                "Exemple" },
	{ "Reset",                  "Reiniciar" },
	{ "OK",                     "D'Acord" },
	{ "Cancel",                 "Cancel.lar" },
	//
	{ "CompiereThemeEditor",    "Editor Tema Compiere" },
	{ "MetalColors",            "Colors Metal" },
	{ "CompiereColors",         "Colors Compiere" },
	{ "CompiereFonts",          "Fonts Compiere" },
	{ "Primary1Info",           "Ombra, Separador" },
	{ "Primary1",               "Primari 1" },
	{ "Primary2Info",           "L\u00ednia Focus, Men\u00fa Seleccionat" },
	{ "Primary2",               "Primari 2" },
	{ "Primary3Info",           "Taula Fila Seleccionada, Texte Seleccionat, Indicador Fons" },
	{ "Primary3",               "Primari 3" },
	{ "Secondary1Info",         "L\u00ednies Marc" },
	{ "Secondary1",             "Secondari 1" },
	{ "Secondary2Info",         "Pestanyes Innactives, Camps Premuts, Texte + Marc Innactius" },
	{ "Secondary2",             "Secondari 2" },
	{ "Secondary3Info",         "Fons" },
	{ "Secondary3",             "Secondari 3" },
	//
	{ "ControlFontInfo",        "Font Control" },
	{ "ControlFont",            "Font Etiqueta" },
	{ "SystemFontInfo",         "Indicador, Nodes Arbre" },
	{ "SystemFont",             "Font Sistema" },
	{ "UserFontInfo",           "Dades Entrades Per l'Usuari" },
	{ "UserFont",               "Font Camp" },
//	{ "SmallFontInfo",          "Informes" },
	{ "SmallFont",              "Font Petita" },
	{ "WindowTitleFont",         "Font T\u00edtol" },
	{ "MenuFont",               "Font Men\u00fa" },
	//
	{ "MandatoryInfo",          "Camp de Fons Obligatori" },
	{ "Mandatory",              "Obligatori" },
	{ "ErrorInfo",              "Camp de Fons Error" },
	{ "Error",                  "Error" },
	{ "InfoInfo",               "Camp de Fons Informaci\u00f3" },
	{ "Info",                   "Informaci\u00f3" },
	{ "WhiteInfo",              "L\u00ednies" },
	{ "White",                  "Blanc" },
	{ "BlackInfo",              "L\u00ednies, Text" },
	{ "Black",                  "Negre" },
	{ "InactiveInfo",           "Camp de Fons Innactiu" },
	{ "Inactive",               "Innactiu" },
	{ "TextOKInfo",             "Texte Superior OK" },
	{ "TextOK",                 "Texte - OK" },
	{ "TextIssueInfo",          "Texte Superior Error" },
	{ "TextIssue",              "Texte - Error" },
	//
	{ "FontChooser",            "Escollidor Font" },
	{ "Fonts",                  "Fonts" },
	{ "Plain",                  "Plana" },
	{ "Italic",                 "It\u00e0lica" },
	{ "Bold",                   "Negreta" },
	{ "BoldItalic",             "Negreta & It\u00e0lica" },
	{ "Name",                   "Nom" },
	{ "Size",                   "Tamany" },
	{ "Style",                  "Estil" },
	{ "TestString",             "Aix\u00f2 \u00e9s nom\u00e9s una Prova! La Guineu marr\u00f3 r\u00e0pida \u00e9st\u00e0 fent quelcom. 12,3456.78 LetterLOne = l1 LetterOZero = O0" },
	{ "FontString",             "Font" },
	//
	{ "CompiereColorEditor",    "Editor Color Compiere" },
	{ "CompiereType",           "Tipus Color" },
	{ "GradientUpperColor",     "Color Dalt Degradat" },
	{ "GradientLowerColor",     "Color Baix Degradat" },
	{ "GradientStart",          "Inici Degradat" },
	{ "GradientDistance",       "Dist\u00e0ncia Degradat" },
	{ "TextureURL",             "Textura URL" },
	{ "TextureAlpha",           "Textura Alfa" },
	{ "TextureTaintColor",      "Textura Color Corrupci\u00f3" },
	{ "LineColor",              "Color L\u00ednia" },
	{ "LineBackColor",          "Color Fons" },
	{ "LineWidth",              "Ampla L\u00ednia" },
	{ "LineDistance",           "Dist\u00e0ncia L\u00ednia" },
	{ "FlatColor",              "Color Pla" }
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
