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
package org.compiere.db;

import org.compiere.startup.*;

/**
 *  General Database Constants and Utilities
 *
 *  @author     Jorg Janke
 *  @version    $Id: Database.java 8457 2010-02-17 23:15:36Z freyes $
 */
public class Database
{
	/** Supported Databases     */
	public static String[]      DB_NAMES = new String[] {
		Environment.DBTYPE_PG,
		Environment.DBTYPE_ORACLE,
		Environment.DBTYPE_ORACLEXE,
		Environment.DBTYPE_DB2,
		Environment.DBTYPE_MS
	};

	/** Database Classes        */
	protected static Class<?>[]    DB_CLASSES = new Class[] {
		DB_PostgreSQL.class,
		DB_Oracle.class,
		DB_Oracle.class,
		//DB_DB2.class,
		//DB_SQLServer.class
	};

	/** Connection Timeout in seconds   */
	public static final int           CONNECTION_TIMEOUT = 10;
	
}   //  Database
