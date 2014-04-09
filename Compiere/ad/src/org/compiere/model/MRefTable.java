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
package org.compiere.model;

import java.sql.*;
import java.util.*;

import org.compiere.util.*;

/**
 * 	Table Reference Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRefTable.java 8288 2009-12-17 23:58:10Z gwu $
 */
public class MRefTable extends X_AD_Ref_Table
{
    /** Logger for class MRefTable */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MRefTable.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get MRefTable from Cache
	 *	@param ctx context
	 *	@param AD_Reference_ID id
	 *	@return MRefTable
	 */
	public static MRefTable get(Ctx ctx, int AD_Reference_ID)
	{
		Integer key = Integer.valueOf (AD_Reference_ID);
		MRefTable retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		if (s_cache.size() == 0 && s_notFound.size() > 0)	//	cache reset
			s_notFound.clear();
		if (s_notFound.contains(AD_Reference_ID))
			return new MRefTable (ctx, 0, null);
		retValue = new MRefTable (ctx, AD_Reference_ID, null);
		if (retValue.get_ID () == 0)
			s_notFound.add(AD_Reference_ID);
		else
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static final CCache<Integer, MRefTable> s_cache = new CCache<Integer, MRefTable> 
		("AD_Ref_Table", 20);
	/** Not found List				*/
	private static final ArrayList<Integer>		s_notFound = new ArrayList<Integer>(); 
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Reference_ID id
	 *	@param trx p_trx
	 */
	public MRefTable (Ctx ctx, int AD_Reference_ID, Trx trx)
	{
		super (ctx, AD_Reference_ID, trx);
		if (AD_Reference_ID == 0)
		{
		//	setAD_Table_ID (0);
			setEntityType (ENTITYTYPE_UserMaintained);	// U
			setIsValueDisplayed (false);
		}
	}	//	MRefTable

	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MRefTable (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MRefTable

	/**
	 * 	Get Table Name
	 *	@return Table Name
	 */
	public String getTableName()
	{
		int AD_Table_ID = getAD_Table_ID();
		return MTable.getTableName (getCtx(), AD_Table_ID);
	}	//	getTableName
	
	/**
	 * 	Get Key ColumnName
	 *	@return Key Column Name
	 */
	public String getKeyColumnName()
	{
		int AD_Column_ID = getColumn_Key_ID();
		return MColumn.getColumnName (getCtx(), AD_Column_ID);
	}	//	getKeyColumnName

	/**
	 * 	Get Display ColumnName
	 *	@return Display Column Name
	 */
	public String getDisplayColumnName()
	{
		int AD_Column_ID = getColumn_Display_ID();
		return MColumn.getColumnName (getCtx(), AD_Column_ID);
	}	//	getDisplayColumnName

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MRefTable[");
		sb.append (getAD_Reference_ID()).append ("-")
			.append (getAD_Table_ID ()).append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MRefTable
