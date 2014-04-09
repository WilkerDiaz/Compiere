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
package org.compiere.framework;

import java.sql.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Generic PO Class
 *	@author Jorg Janke
 */
public class X extends PO
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private X(MTable table) {
		super();
		Table_ID = table.getAD_Table_ID();
		Table_Name = table.getTableName();
	}	
	
	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param table table to load
	 *	@param id id of table
	 *	@param trx transaction
	 */
	public X (Ctx ctx, MTable table, int id, Trx trx)
	{
		this(table);
		init (ctx, id, null, trx);
	}	//	X

	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param table table to load
	 *	@param rs result set of table
	 *	@param trx transaction
	 */
	public X (Ctx ctx, MTable table, ResultSet rs, Trx trx)
	{
		this(table);
		init (ctx, 0, rs, trx);
	}	//	X

	/**	AD_Table_ID				*/
	private final int 		Table_ID;
	/** Table Name				*/
	private final String	Table_Name;

	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("X[").append(get_ID())
	    	.append("-")
	        .append(Table_Name);
	    sb.append("]");
	    return sb.toString();
    }	//	toString

	@Override
	public String get_TableName() {
		return Table_Name;
	}

	@Override
	public int get_Table_ID() {
		return Table_ID;
	}

}	//	X
