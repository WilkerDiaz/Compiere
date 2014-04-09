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
 *  @version    $Id: PlafRes_pt.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class PlafRes_pt extends ListResourceBundle
{
	/** The data    */
	static final Object[][] contents = new String[][]
	{
	{ "BackColType",            "Tipo da Cor de Fundo" },
	{ "BackColType_Flat",       "Plano" },
	{ "BackColType_Gradient",   "D\u00e9grad\u00e9" },
	{ "BackColType_Lines",      "Linhas" },
	{ "BackColType_Texture",    "Textura" },
	//
	{ "LookAndFeelEditor",      "Editor: Look & Feel" },
	{ "LookAndFeel",            "Look & Feel" },
	{ "Theme",                  "Tema" },
	{ "EditCompiereTheme",      "Editar Tema Compiere" },
	{ "SetDefault",             "Fundo Padr\u00e3o" },
	{ "SetDefaultColor",        "Cor de Fundo" },
	{ "ColorBlind",             "Defici\u00eancia de Cor" },
	{ "Example",                "Exemplo" },
	{ "Reset",                  "Reinicializar" },
	{ "OK",                     "OK" },
	{ "Cancel",                 "Cancelar" },
	//
	{ "CompiereThemeEditor",    "Compiere: Editor de Temas" },
	{ "MetalColors",            "Cores Metal" },
	{ "CompiereColors",         "Cores Compiere" },
	{ "CompiereFonts",          "Fontes Compiere" },
	{ "Primary1Info",           "Sombra, Separador" },
	{ "Primary1",               "Prim\u00e1rio 1" },
	{ "Primary2Info",           "Linha de Foco, Menu Selecionado" },
	{ "Primary2",               "Prim\u00e1rio 2" },
	{ "Primary3Info",           "Linha da Tabele Selecionada, Texto Selecionado, Fundo Dicas" },
	{ "Primary3",               "Prim\u00e1rio 3" },
	{ "Secondary1Info",         "Bordas de Linhas" },
	{ "Secondary1",             "Secund\u00e1rio 1" },
	{ "Secondary2Info",         "Tabs Inativas, Campos Pressed, Borda Inativa + Texto" },
	{ "Secondary2",             "Secund\u00e1rio 2" },
	{ "Secondary3Info",         "\u00c1rea de Fundo" },
	{ "Secondary3",             "Secund\u00e1rio 3" },
	//
	{ "ControlFontInfo",        "Controle de Fonte" },
	{ "ControlFont",            "R\u00f3tulo da Fonte" },
	{ "SystemFontInfo",         "Tool Tip, Tree nodes" },
	{ "SystemFont",             "System Font" },
	{ "UserFontInfo",           "Entrada de Dados pelo Usu\u00e1rio" },
	{ "UserFont",               "Fonte dos Campos" },
//	{ "SmallFontInfo",          "Relat\u00f3rios" },
	{ "SmallFont",              "Fonte pequena" },
	{ "WindowTitleFont",         "Fonte do T\u00edtulo" },
	{ "MenuFont",               "Fonte do Menu" },
	//
	{ "MandatoryInfo",          "Fundo de Campos Obrigat\u00f3rios" },
	{ "Mandatory",              "Obrigat\u00f3rio" },
	{ "ErrorInfo",              "Fundo de Campo de Erro" },
	{ "Error",                  "Erro" },
	{ "InfoInfo",               "Fundo de Campos Informativos" },
	{ "Info",                   "Info" },
	{ "WhiteInfo",              "Linhas" },
	{ "White",                  "Branco" },
	{ "BlackInfo",              "Linhas, Texto" },
	{ "Black",                  "Preto" },
	{ "InactiveInfo",           "Fundo de Campo Inativo" },
	{ "Inactive",               "Inativo" },
	{ "TextOKInfo",             "OK Fundo do Texto" },
	{ "TextOK",                 "Texto - OK" },
	{ "TextIssueInfo",          "Fundo do Texto de Erro" },
	{ "TextIssue",              "Texto - Erro" },
	//
	{ "FontChooser",            "Selecionar Fonte" },
	{ "Fonts",                  "Fontes" },
	{ "Plain",                  "Plano" },
	{ "Italic",                 "It\u00e1lico" },
	{ "Bold",                   "Negrito" },
	{ "BoldItalic",             "Negrito & It\u00e1lico" },
	{ "Name",                   "Nome" },
	{ "Size",                   "Tamanho" },
	{ "Style",                  "Estilo" },
	{ "TestString",             "Somente teste! The quick brown Fox is doing something. 12,3456.78 LetterLOne = l1 LetterOZero = O0" },
	{ "FontString",             "Fonte" },
	//
	{ "CompiereColorEditor",    "Compiere: Editor de Cores" },
	{ "CompiereType",           "Tipo de Cor" },
	{ "GradientUpperColor",     "Cor D\u00e9grad\u00e9 Superior" },
	{ "GradientLowerColor",     "Cor D\u00e9grad\u00e9 Inferior" },
	{ "GradientStart",          "In\u00edcio do D\u00e9grad\u00e9" },
	{ "GradientDistance",       "Dist\u00e2ncia do D\u00e9grad\u00e9" },
	{ "TextureURL",             "URL Textura" },
	{ "TextureAlpha",           "Alfa Textura" },
	{ "TextureTaintColor",      "Colora\u00e7\u00e3o da Textura" },
	{ "LineColor",              "Cor da Linha" },
	{ "LineBackColor",          "Cores de Fundo" },
	{ "LineWidth",              "Largura da Linha" },
	{ "LineDistance",           "Dist\u00e2ncia das linhas" },
	{ "FlatColor",              "Cores Planas" }
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
