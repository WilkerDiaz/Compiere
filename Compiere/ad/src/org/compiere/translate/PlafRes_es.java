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
 *  Translation Texts for Look & Feel (Spanish)
 *
 *  @version    $Id: PlafRes_es.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class PlafRes_es extends ListResourceBundle
{
	/** The data    */
	static final Object[][] contents = new String[][]
	{
	{ "BackColType",            "Tipo de Color de fondo" },
	{ "BackColType_Flat",       "Liso" },
	{ "BackColType_Gradient",   "Gradiente" },
	{ "BackColType_Lines",      "L\u00EDneas" },
	{ "BackColType_Texture",    "Textura" },
	//
	{ "LookAndFeelEditor",      "Editor de ambientes" },
	{ "LookAndFeel",            "Ambiente" },
	{ "Theme",                  "Tema" },
	{ "EditCompiereTheme",      "Editar Tema Compiere" },
	{ "SetDefault",             "Fondo por defecto" },
	{ "SetDefaultColor",        "Color de Fondo" },
	{ "ColorBlind",             "Deficiencia de Color" },
	{ "Example",                "Ejemplo" },
	{ "Reset",                  "Restaurar" },
	{ "OK",                     "OK" },
	{ "Cancel",                 "Cancelar" },
	//
	{ "CompiereThemeEditor",    "Editor Temas Compiere" },
	{ "MetalColors",            "Colores Met\u00E1l." },
	{ "CompiereColors",         "Colores Compiere" },
	{ "CompiereFonts",          "Fuentes Compiere" },
	{ "Primary1Info",           "Sombra , Separador" },
	{ "Primary1",               "Primario 1" },
	{ "Primary2Info",           "Foco de L\u00EDnea, Seleccionado en Men\u00FA" },
	{ "Primary2",               "Primario 2" },
	{ "Primary3Info",           "Fila Seleccionada en Tabla, Texto Seleccionado, Fondos de Herramientas" },
	{ "Primary3",               "Primario 3" },
	{ "Secondary1Info",         "L\u00EDneas de borde" },
	{ "Secondary1",             "Secondario 1" },
	{ "Secondary2Info",         "Pesta\u00F1as inactivas, Campos Presionados,  Borde Inactivo + Texto" },
	{ "Secondary2",             "Secondario 2" },
	{ "Secondary3Info",         "Fondo" },
	{ "Secondary3",             "Secundario 3" },
	//
	{ "ControlFontInfo",        "Control de fuentes" },
	{ "ControlFont",            "Fuente de Etiqueta" },
	{ "SystemFontInfo",         "Informaci\u00F3n de Herramienta , Arbol de nodos" },
	{ "SystemFont",             "Fuentes del sistema" },
	{ "UserFontInfo",           "Usuario haya introducido los datos" },
	{ "UserFont",               "Fuente de Campo" },
//	{ "SmallFontInfo",          "Informes" },
	{ "SmallFont",              "Fuentes Peque\u00F1as" },
	{ "WindowTitleFont",        "Fuente de T\u00EDtulo" },
	{ "MenuFont",               "Fuente Men\u00FA" },
	//
	{ "MandatoryInfo",          "Fondo de campo obligatorio" },
	{ "Mandatory",              "Mandatorio" },
	{ "ErrorInfo",              "Fondo de campo de error" },
	{ "Error",                  "Error" },
	{ "InfoInfo",               "Fondo del campo de informaci\u00F3n" },
	{ "Info",                   "Info" },
	{ "WhiteInfo",              "L\u00EDneas" },
	{ "White",                  "Escribe" },
	{ "BlackInfo",              "L\u00EDneas, Texto" },
	{ "Black",                  "Negro" },
	{ "InactiveInfo",           "Fondo de Campo inactivo" },
	{ "Inactive",               "Inactivo" },
	{ "TextOKInfo",             "Primer Plano de Texto OK" },
	{ "TextOK",                 "Texto - OK" },
	{ "TextIssueInfo",          "Primer plano de Texto de error" },
	{ "TextIssue",              "Texto - Error" },
	//
	{ "FontChooser",            "Selector de Fuente" },
	{ "Fonts",                  "Fuentes" },
	{ "Plain",                  "Liso" },
	{ "Italic",                 "Cursiva" },
	{ "Bold",                   "Negrita" },
	{ "BoldItalic",             "Negrita y Cursiva" },
	{ "Name",                   "Nombre" },
	{ "Size",                   "Tama\u00F1o" },
	{ "Style",                  "Estilo" },
	{ "TestString",             "Esto es s\u00F3lo una prueba! El r\u00E1pido zorro marr\u00F3n est\u00E1 haciendo algo. 12,3456.78 LetterLOne = l1 LetterOZero = O0" },
	{ "FontString",             "Fuente" },
	//
	{ "CompiereColorEditor",    "Editor de Color Compiere" },
	{ "CompiereType",           "Tipo de Color" },
	{ "GradientUpperColor",     "Gradiente de color superior" },
	{ "GradientLowerColor",     "Gradiente de color inferior" },
	{ "GradientStart",          "Inicio de Gradiente" },
	{ "GradientDistance",       "Distancia de Gradiente" },
	{ "TextureURL",             "Textura en URL" },
	{ "TextureAlpha",           "Texture Alfa" },
	{ "TextureTaintColor",      "Textura mancha de color" },
	{ "LineColor",              "Color de L\u00EDnea" },
	{ "LineBackColor",          "Color de Fondo" },
	{ "LineWidth",              "Ancho de la l\u00EDnea" },
	{ "LineDistance",           "Distancia de l\u00EDnea" },
	{ "FlatColor",              "Color Liso" }
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
