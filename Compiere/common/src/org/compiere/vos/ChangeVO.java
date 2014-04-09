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
package org.compiere.vos;

import java.util.*;

import org.compiere.common.*;
import org.compiere.util.*;


/**
 *	Consequences of 
 *	- Field Value Changes
 *	- New Row
 *	- Save (Update/Insert) Row
 *	
 *  @author Jorg Janke
 *  @version $Id$
 */
public class ChangeVO extends ResponseVO
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Change VO
	 */
	public ChangeVO()
	{
	}	//	ChangeVO
	
	/**
	 * 	Change VO Info Message
	 *	@param error error flag
	 *	@param message info message
	 */
	public ChangeVO (String message)
	{
		this (false, message);
	}	//	ChangeVO
	
	/**
	 * 	Change VO Error/Info Message
	 *	@param error error flag
	 *	@param message Error/Info message
	 */
	public ChangeVO (boolean error, String message)
	{
		if(error)
			addError(message);
		else
			addSuccess(message);
	}	//	ChangeVO
	
	
	/** New Confirmed Value	for Field Changes			*/
	public String 		newConfirmedFieldValue = null;
	
	/** Changed or New Values
	 * 	Map< ColumnName,ColumnValue >						
	 */
	public HashMap<String, String> 			changedFields = null;
	
	/** Changed Drop Down Lists		
	 * 	Map< FieldName,ArrayList<NamePair> >				
	 * */
	public HashMap<String, ArrayList<NamePair>>			changedDropDowns = null;

	/** Changed or New Window Context
	 * 	Map<ColumnName,ColumnValue>						
	 */
	public HashMap<String, String> 			changedContext = null;

	/** Components that are changed
	 * 	now only support display/non-display flag						
	 */	
	public HashMap<String,String> 			changedComponents = null;
	/** Row Data										*/
	public String[]		rowData = null;


	/**
	 *  The map of component IDs to QueryVOs.  If a QueryVO is present for a component, 
	 *  a query will be run against that component with the provided QueryVO.
	 */
	public HashMap<Integer,QueryVO>              queryComponents = null;
	
	/**
	 * If this is set, the row represented by the current WindowCtx object will be updated.   
	 */
	public boolean updateCurrentRow = false;


	/**
	 * Set this to true to request the client UI to re-render the entire window.
	 */
	public boolean updateWindowVO = false;
	/**
	 * Populate this to completely replace the WindowVO of the currently open window
	 */
	public WindowVO     newWindowVO = null;

	/**
	 * 
	 */
	public String trxInfo = null;

	
	/**
	 * 	Add Changed Component
	 *	@param ColumnName column
	 *	@param value new value
	 *	@param previous value or null
	 */
	public String addChangedComponent (String tableName, String value)
	{
		if (tableName != null)
		{
			if (changedComponents == null)
				changedComponents = new HashMap<String, String>();
			String s = changedComponents.put(tableName, value);
			return s;
		}
		return null;
	}	//	addChangedComponent

	/**
	 * 	Add Changed Value
	 *	@param columnName column
	 *	@param value new value
	 *	@param previous value or null
	 */
	public String addChangedValue (String columnName, Object value)
	{
		if (columnName != null)
		{
			if (changedFields == null)
				changedFields = new HashMap<String, String>();
			
			if (value == null || value instanceof String)
				return changedFields.put(columnName, (String)value);
			String stringValue = value.toString();
			if (value instanceof Date)
			{
				long longValue = ((Date)value).getTime();
				stringValue = String.valueOf(longValue);
			}
			else if (value instanceof Boolean)
				stringValue = ((Boolean)value).booleanValue() ? "Y" : "N";
			return changedFields.put(columnName, stringValue);
		}
		return null;
	}	//	addChangedValue

	/**
	 * 	Add Changed Value
	 *	@param columnName column
	 *	@param value new value
	 *	@param previous value or null
	 */
	public String addChangedValue (String columnName, boolean value)
	{
		return addChangedValue(columnName, value ? "Y" : "N");
	}	//	addChangedValue

	/**
	 * 	Add Changed Value
	 *	@param columnName column
	 *	@param value new value
	 *	@param previous value or null
	 */
	public String addChangedValue (String columnName, int value)
	{
		String stringValue = String.valueOf(value);
		if (value < 0)
			stringValue = null;
		return addChangedValue(columnName, stringValue);
	}	//	addChangedValue

	/**
	 * 	Add Changed Value
	 *	@param columnName column
	 *	@param value new value
	 *	@param previous value or null
	 */
	public String addChangedValue (String columnName, byte[] value)
	{
		return null;
	}	//	addChangedValue

	/**
	 * 	Add Changed Drop Down Values
	 * 	@param columnName column
	 * 	@param data ArrayList<NamePair> data
	 *	@param previous value or null
	 */
	public ArrayList<NamePair> addChangedDropDown (String columnName, ArrayList<NamePair> data)
	{
		if (columnName != null)
		{
			if (changedDropDowns == null)
				changedDropDowns = new HashMap<String, ArrayList<NamePair>>();
			return changedDropDowns.put(columnName, data);
		}
		return null;
	}	//	addChangedDropDown

	public String setChangedContext(String colName, String val) {
		if (changedContext == null)
			changedContext = new HashMap<String, String>();
		String s = changedContext.put(colName, val);
		return s;
	}
	/**
	 * 	Set Context - add to changed Context
	 * 	@param ctx	transitory context
	 * 	@param windowNo window no
	 *	@param columnName column
	 *	@param value new value
	 *	@param previous value or null
	 */
	public String setContext (Ctx ctx, int windowNo, String columnName, String value)
	{
		if (columnName != null)
		{
			ctx.setContext(windowNo, columnName, value);
			//
			return setChangedContext(columnName, value);
		}
		return null;
	}	//	addChangedContext

	/**
	 * 	Set Context - add to changed Context
	 * 	@param ctx	transitory context
	 * 	@param windowNo window no
	 *	@param columnName column
	 *	@param value new value
	 *	@param previous value or null
	 */
	public String setContext (Ctx ctx, int windowNo, String columnName, int value)
	{
		return setContext(ctx, windowNo, columnName, String.valueOf(value));
	}	//	addChangedContext

	/**
	 * 	Set Context - add to changed Context
	 * 	@param ctx	transitory context
	 * 	@param windowNo window no
	 *	@param columnName column
	 *	@param value new value
	 *	@param previous value or null
	 */
	public String setContext (Ctx ctx, int windowNo, String columnName, boolean value)
	{
		return setContext(ctx, windowNo, columnName, value ? "Y" : "N");
	}	//	addChangedContext

	/**
	 * 	Set Context - add to changed Context
	 * 	@param ctx	transitory context
	 * 	@param windowNo window no
	 *	@param columnName column
	 *	@param value new value
	 *	@param previous value or null
	 */
	public String setContext (Ctx ctx, int windowNo, String columnName, Number value)
	{
		String stringValue = null;
		if (value != null)
			stringValue = value.toString();
		return setContext(ctx, windowNo, columnName, stringValue);
	}	//	setContext
	
	/**
	 * 	Set Context - merge changedContext to Ctx
	 * 	@param ctx	transitory context
	 * 	@param windowNo window no
	 *	@param previous value or null
	 */
	public void addContextAll (Ctx ctx, int windowNo)
	{
		if (changedContext != null) 
		{
			Set<String> keys = changedContext.keySet();
			Iterator<String> it = keys.iterator();
			while(it.hasNext()) 
			{
				String colName = it.next();
				Object value = changedContext.get(colName);
				String stringValue = null;
				if (value != null)
					stringValue = value.toString();
				ctx.setContext(windowNo, colName, stringValue);
			}
		}
			
	}	//	addContextAll

	public void mergeTo(HashMap<String, String> to) {
		if(changedContext != null)
			to.putAll(changedContext);
		if(changedFields != null)
			to.putAll(changedFields);
		//to.put(key, value)
	}

	/**
	 * 	Add all values for change vo
	 * 	@param add change VO to be added
	 */
	public void addAll(ChangeVO add)
	{
		if (add == null)
			return;
		if (add.changedDropDowns != null)
		{
			if (changedDropDowns == null)
				changedDropDowns = add.changedDropDowns;
			else
				changedDropDowns.putAll(add.changedDropDowns);
		}
		if (add.changedFields != null)
		{
			if (changedFields == null)
				changedFields = add.changedFields;
			else
				changedFields.putAll(add.changedFields);
		}
		if (add.changedContext != null)
		{
			if (changedContext == null)
				changedContext = add.changedContext;
			else
				changedContext.putAll(add.changedContext);
		}
		if (add.params != null) {
			if (params == null)
				params = add.params;
			else
				params.putAll(add.params);
		}
			
		//	Messages & Flags
		messages.addAll(add.messages);
		if (add.hasError)
			hasError = true;
		if (add.hasWarning)
			hasWarning = true;
	}	//	addAll
	
	
	/**
	 * 	Cleanup Values
	 *	@return this
	 */
	public ChangeVO cleanup()
	{
		if (changedFields != null && changedFields.isEmpty())
			changedFields = null;
		if (changedDropDowns != null && changedDropDowns.isEmpty())
			changedDropDowns = null;
		return this;
	}	// cleanup
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		return "changedFields: "+changedFields+" changedDropDowns:"+this.changedDropDowns+" changedContext:"+this.changedContext;
	}	//	toString
	
	public static String CURRENT_ROW_UPDATEABLE_STRING = "COMPIERE_IS_CURRENT_ROW_UPDATEABLE";

}	//	ChangeVO
