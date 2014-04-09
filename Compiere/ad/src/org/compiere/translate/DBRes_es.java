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
 *  Connection Resource Strings
 *
 *  @version    $Id: DBRes_es.java 8394 2010-02-04 22:55:56Z freyes $
 */
public class DBRes_es extends ListResourceBundle
{
	/** Data        */
	static final Object[][] contents = new String[][]{
	{ "CConnectionDialog",  "Conexi\u00F3n Compiere" },
	{ "Name",               "Nombre" },
	{ "AppsHost",           "Servidor de Aplicaci\u00F3n" },
	{ "AppsPort",           "Puerto de Aplicaci\u00F3n" },
	{ "TestApps",           "Test de Aplicaci\u00F3n" },
	{ "DBHost",             "Host de Base de Datos" },
	{ "DBPort",             "Puerto de Base de Datos" },
	{ "DBName",             "Nombre de Base de datos" },
	{ "DBUidPwd",           "Usuario / Contrase\u00F1a" },
	{ "ViaFirewall",        "v\u00EDa Firewall" },
	{ "FWHost",             "Servidor de Firewall" },
	{ "FWPort",             "Puerto del Firewall" },
	{ "TestConnection",     "Test de Base de datos" },
	{ "Type",               "Tipo de Base de Datos" },
	{ "BequeathConnection", "Conexi\u00F3n" },
	{ "Overwrite",          "Sobreescribir" },
	{ "RMIoverHTTP", 		"Tunelizar Objetos v\u00EDa HTTP" },
	{ "ConnectionError",    "Error en conexi\u00F3n" },
	{ "ServerNotActive",    "Servidor inactivo" }};

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
