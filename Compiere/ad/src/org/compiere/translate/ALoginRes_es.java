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
 *  Base Resource Bundle
 *
 * 	@version 	$Id: ALoginRes_es.java 8394 2010-02-04 22:55:56Z freyes $
 */
public final class ALoginRes_es extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection", "Conexi\u00F3n" },
	{ "Defaults", "Valores por Defecto" },
	{ "Login", "Login" },
	{ "File", "Archivo" },
	{ "Exit", "Salir" },
	{ "Help", "Ayuda" },
	{ "About", "Acerca de" },
	{ "Host", "Servidor" },
	{ "Database", "Base de datos" },
	{ "User", "ID de usuario" },
	{ "EnterUser", "ID de aplicacion de usuario" },
	{ "Password", "Contrase\u00F1a" },
	{ "EnterPassword", "Ingrese Contrase\u00F1a" },
	{ "Language", "Idioma" },
	{ "SelectLanguage", "Seleccione su idioma" },
	{ "Role", "Perfil" },
	{ "Client", "Titular" },
	{ "Organization", "Organizaci\u00F3n" },
	{ "Date", "Fecha" },
	{ "Warehouse", "Almac\u00E9n" },
	{ "Printer", "Impresora" },
	{ "Connected", "Conectado" },
	{ "NotConnected", "No Conectado" },
	{ "DatabaseNotFound", "Base de datos no encontrada" },
	{ "UserPwdError", "Usuario-Contrase\u00F1a no coincide" },
	{ "RoleNotFound", "Perfil no encontrado" },
	{ "Authorized", "Autorizado" },
	{ "Ok", "Aceptar" },
	{ "Cancel", "Cancelar" },
	{ "VersionConflict", "Conflicto de versi\u00F3n:" },
	{ "VersionInfo", "Servidor <> Cliente" },
    { "ChangeRole", "Cambie Perfil" },
	{ "PleaseUpgrade", "Por favor ejecutar programa de actualizaci\u00F3n" }
	};

	/**
	 *  Get Contents
	 *  @return context
	 */
	@Override
	public Object[][] getContents()
	{
		return contents;
	}   //  getContents
}	//	ALoginRes_es
