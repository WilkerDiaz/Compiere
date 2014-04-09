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
package org.compiere.util;

import com.google.common.base.ReferenceType;

/**
 *  Compiere Permanent Cache.  The items held by this cache will not automatically be reclaimed by the garbage collector.
 *	@param <K> Key 
 *	@param <V> Value
 *
 *  @author Di Zhao
 *  
 */
public class CCachePerm<K, V> extends CCache<K, V>
{

	public CCachePerm(String tableName, int initialCapacity, int expireMinutes,
			boolean resetAll, String tableName2) {
		super(tableName, initialCapacity, expireMinutes, resetAll, ReferenceType.STRONG, tableName2);
	}
	public CCachePerm (String tableName, int initialCapacity) {
		this (tableName, initialCapacity, 120, false, null);
	}
	public CCachePerm (String tableName, int initialCapacity, int expireMinutes) {
		this (tableName, initialCapacity, expireMinutes, false, null);
	}

	public CCachePerm (String tableName, int initialCapacity, String alternativeName) {
		this (tableName, initialCapacity, 120, false, alternativeName);
	}
	public CCachePerm (String tableName, int initialCapacity, int expireMinutes, String alternativeName) {
		this (tableName, initialCapacity, expireMinutes, false, alternativeName);
	}
}
