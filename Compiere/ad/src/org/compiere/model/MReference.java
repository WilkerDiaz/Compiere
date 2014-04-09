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
import org.compiere.common.constants.*;
import org.compiere.util.*;

/**
 * @author Jorg Janke
 *
 */
public class MReference extends X_AD_Reference
{
    /** Logger for class MReference */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MReference.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Reference from Cache
	 *	@param ctx context
	 *	@param AD_Reference_ID id
	 *	@return MReference
	 */
	public static MReference get (Ctx ctx, int AD_Reference_ID)
	{
		Integer key = Integer.valueOf(AD_Reference_ID);
		MReference retValue = s_cache.get(ctx, key);
		if (retValue == null)
			return new MReference (ctx, AD_Reference_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get
	
	/**	Cache						*/
	private static final CCache<Integer,MReference> s_cache = new CCache<Integer,MReference>("AD_Reference", 20);

	
	/**
	 * @param ctx
	 * @param AD_Reference_ID
	 * @param trx
	 */
	public MReference(Ctx ctx, int AD_Reference_ID, Trx trx)
	{
		super(ctx, AD_Reference_ID, trx);
	}

	/**
	 * @param ctx
	 * @param rs
	 * @param trx
	 */
	public MReference(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}

	/**
	 * 	String Representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MReference[")
			.append(get_ID()).append("-").append(getName()).append("]");
		return sb.toString();
	}	//	toString
	
	private static Map<Integer, String> refToField;
	
	// static initializer
	static {
		refToField = new HashMap<Integer, String>();
		refToField.put(0, ""); //Default
		refToField.put(DisplayTypeConstants.String, DisplayTypeConstants.FIELD_TEXT); // 10
		refToField.put(DisplayTypeConstants.Binary, DisplayTypeConstants.FIELD_TEXT); // 23
		refToField.put(DisplayTypeConstants.FilePath, DisplayTypeConstants.FIELD_TEXT); // 38
		refToField.put(DisplayTypeConstants.FileName, DisplayTypeConstants.FIELD_TEXT); // 39
		refToField.put(DisplayTypeConstants.URL , DisplayTypeConstants.FIELD_SEARCH); // 40
		refToField.put(DisplayTypeConstants.Text, DisplayTypeConstants.FIELD_TEXTAREA); // 14
		refToField.put(DisplayTypeConstants.Memo, DisplayTypeConstants.FIELD_TEXTAREA); // 34
		refToField.put(DisplayTypeConstants.TextLong, DisplayTypeConstants.FIELD_TEXTAREA); // 36
		refToField.put(DisplayTypeConstants.Integer, DisplayTypeConstants.FIELD_NUMERIC); // 11
		refToField.put(DisplayTypeConstants.Amount, DisplayTypeConstants.FIELD_NUMERIC); // 12
		refToField.put(DisplayTypeConstants.Number, DisplayTypeConstants.FIELD_NUMERIC); // 22
		refToField.put(DisplayTypeConstants.Date, DisplayTypeConstants.FIELD_DATETIME); // 15
		refToField.put(DisplayTypeConstants.Time, DisplayTypeConstants.FIELD_DATETIME); // 24
		refToField.put(DisplayTypeConstants.DateTime, DisplayTypeConstants.FIELD_DATETIME); // 16
		refToField.put(DisplayTypeConstants.ID, DisplayTypeConstants.FIELD_LIST); // 13
		refToField.put(DisplayTypeConstants.List, DisplayTypeConstants.FIELD_LIST); // 17
		refToField.put(DisplayTypeConstants.Table, DisplayTypeConstants.FIELD_LIST); // 18
		refToField.put(DisplayTypeConstants.TableDir, DisplayTypeConstants.FIELD_LIST); // 19
		refToField.put(DisplayTypeConstants.Locator, DisplayTypeConstants.FIELD_SEARCH); // 31
		refToField.put(DisplayTypeConstants.Image, DisplayTypeConstants.FIELD_LIST); // 32
		refToField.put(DisplayTypeConstants.Assignment, DisplayTypeConstants.FIELD_LIST); // 33
		refToField.put(DisplayTypeConstants.PrinterName, DisplayTypeConstants.FIELD_LIST); // 42
		refToField.put(DisplayTypeConstants.Color, DisplayTypeConstants.FIELD_LIST); // 27
		refToField.put(DisplayTypeConstants.Location, DisplayTypeConstants.FIELD_SEARCH); // 21
		refToField.put(DisplayTypeConstants.Account, DisplayTypeConstants.FIELD_SEARCH); // 25
		refToField.put(DisplayTypeConstants.Search, DisplayTypeConstants.FIELD_SEARCH); // 30
		refToField.put(DisplayTypeConstants.PAttribute, DisplayTypeConstants.FIELD_SEARCH); // 35
		refToField.put(DisplayTypeConstants.Button, DisplayTypeConstants.FIELD_BUTTON); // 28
		refToField.put(DisplayTypeConstants.YesNo, DisplayTypeConstants.FIELD_CHECKBOX); // 20
		refToField.put(DisplayTypeConstants.CostPrice, DisplayTypeConstants.FIELD_NUMERIC); // 37
		refToField.put(DisplayTypeConstants.Quantity, DisplayTypeConstants.FIELD_NUMERIC); // 29
	}
	
	public static String getFieldType(int refId)
	{
		String ft = refToField.get(refId); 
		return ( ft== null? "": ft);
	}

}
