/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.util;

/**
 *  Compiere Cache Interface
 *
 *  @author Jorg Janke
 *  @version $Id: CacheInterface.java 8244 2009-12-04 23:25:29Z freyes $
 */
public interface CacheInterface
{
	/**
	 * 	Get Table Name of cached entity
	 *	@return table name
	 */
	public String getTableName();
	
	/**
	 *	Reset Cache
	 *	@return number of items reset
	 */
	public int reset();

	/**
	 * 	Get Size of Cache
	 *	@return number of items
	 */
	public int size();

	/**
	 *	Reset Cache if applies
	 *	@param tableName table name
	 *	@param record_ID record id or 0 for all 
	 * 	@return number of items cleared or -1 if n/a
	 *	@see org.compiere.util.CacheInterface#reset()
	 */
	public int reset(String tableName, int record_ID);

}	//	CacheInterface
