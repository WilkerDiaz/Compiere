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

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.*;
import java.net.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.Map.Entry;
import java.util.logging.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.compiere.*;
import org.compiere.api.*;
import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.util.Env.*;
import org.compiere.vos.*;
import org.compiere.webservice.WebServiceParameter;
import org.w3c.dom.*;

/**
 *  Persistent Object.
 *  Superclass for actual implementations
 *
 *  @author Jorg Janke
 *  @version $Id: PO.java 9223 2010-09-17 05:53:54Z ragrawal $
 */
public abstract class PO
implements Serializable, Cloneable, Comparator<PO>, Evaluatee
{

	/** */
	private static final long serialVersionUID = -3988161238689466044L;

	/**
	 * 	Get a copy/clone of PO and set ctx
	 *	@param ctx context
	 * @param po PO
	 * @param trx TODO
	 *	@return po or null
	 */
	static public PO copy (Ctx ctx, PO po, Trx trx)
	{
		if (ctx == null || po == null)
			return null;
		try
		{
			PO newPO = (PO)po.clone();
			newPO.m_ctx = ctx;
			newPO.m_trx = trx;
			return newPO;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, po.toString(), e);
		}
		return null;
	}	//	copy

	/**
	 * 	Set Document Value Workflow Manager
	 *	@param docWFMgr mgr
	 */
	public static void setDocWorkflowMgr (DocWorkflowMgr docWFMgr)
	{
		s_docWFMgr = docWFMgr;
		log.config (s_docWFMgr.toString());
	}	//	setDocWorkflowMgr

	/**
	 * 	Convert String to int.
	 * 	Null throws an error
	 *	@param stringValue value
	 *	@return int
	 */
	public static int convertToInt (String stringValue)
	{
		if (Util.isEmpty(stringValue))
			throw new IllegalArgumentException("Value is NULL");
		return Integer.parseInt(stringValue);
	}	//	convertToInt

	/**
	 * 	Convert String to ID.
	 *	@param stringValue value
	 *	@return int - null returns 0
	 */
	public static int convertToID (String stringValue)
	{
		if (Util.isEmpty(stringValue))
			return 0;
		return Integer.parseInt(stringValue);
	}	//	convertToID

	/**
	 * 	Convert String to Timestamp.
	 * 	Null is passed through
	 *	@param stringValue value
	 *	@return Timestamp
	 */
	public static Timestamp convertToTimestamp (String stringValue)
	{
		if (stringValue == null || stringValue.length() == 0)
			return null;
		//	long Time format
		try
		{
			long time = Long.parseLong(stringValue);
			return new Timestamp(time);
		}
		catch (Exception e)
		{
		}
		//	JDBC yyyy-mm-dd hh:mm:ss.fffffffff
		try
		{
			return Timestamp.valueOf(stringValue);
		}
		catch (Exception e)
		{
		}
		throw new IllegalArgumentException("Cannot convert to Timestamp: " + stringValue);
	}	//	convertToTimestamp

	/**
	 * 	Convert String to BigDecimal.
	 * 	Null is passed through
	 *	@param stringValue value
	 *	@return BigDecimal
	 */
	public static BigDecimal convertToBigDecimal (String stringValue)
	{
		if (Util.isEmpty(stringValue))
			return null;
		return new BigDecimal(stringValue);
	}	//	convertToBigDecimal

	/**
	 * 	Convert String to boolean.
	 * 	'Y','true' is true - rest (incl null) is false
	 *	@param stringValue value
	 *	@return boolean
	 */
	public static boolean convertToBoolean (String stringValue)
	{
		return stringValue != null
		&& ("Y".equals(stringValue)
				|| "true".equals(stringValue)		//	boolean
				|| "on".equals (stringValue));		//	web
	}	//	convertToBoolean

	/**
	 * 	Get ID of table
	 * 	@param tableName name
	 *	@return AD_Table_ID
	 */
	public static int get_Table_ID(String tableName)
	{
		String sql = "SELECT AD_Table_ID FROM AD_Table WHERE TableName=?";
		int AD_Table_ID = QueryUtil.getSQLValue(null, sql, tableName);
		return AD_Table_ID;
	}	//	get_Table_ID


	/** Document Value Workflow Manager		*/
	private static DocWorkflowMgr		s_docWFMgr = null;

	/** User Maintained Entity Type				*/
	static protected final String ENTITYTYPE_UserMaintained = "U";
	/** Dictionary Maintained Entity Type		*/
	static protected final String ENTITYTYPE_Dictionary = "D";

	/**************************************************************************
	 *  Create New Persisent Object
	 *  @param ctx context
	 */
	public PO (Ctx ctx)
	{
		init (ctx, 0, null, null);
	}   //  PO

	/**
	 *  Create & Load existing Persistent Object
	 *  @param ID  The unique ID of the object
	 *  @param ctx context
	 *  @param trx transaction name
	 */
	public PO (Ctx ctx, int ID, Trx trx)
	{
		init (ctx, ID, null, trx);
	}   //  PO

	/**
	 *  Create & Load existing Persistent Object.
	 *  @param ctx context
	 *  @param rs optional - load from current result set position (no navigation, not closed)
	 *  	if null, a new record is created.
	 *  @param trx transaction name
	 */
	public PO (Ctx ctx, ResultSet rs, Trx trx)
	{
		init (ctx, 0, rs, trx);
	}	//	PO

	/**
	 * 	Create New PO by Copying existing (key not copied).
	 * 	@param ctx context
	 * 	@param source source object
	 * 	@param AD_Client_ID client
	 * 	@param AD_Org_ID org
	 */
	public PO (Ctx ctx, PO source, int AD_Client_ID, int AD_Org_ID)
	{
		init (ctx, 0, null, null);	//	create new
		//
		if (source != null)
			copyValues (source, this);
		setAD_Client_ID(AD_Client_ID);
		setAD_Org_ID(AD_Org_ID);
	}	//	PO

	/**
	 * 	VO Constructor
	 *	@param ctx context
	 *	@param vo value object
	 */
	public PO (Ctx ctx, VO vo)
	{
		this (ctx, 0, null);
		load(vo);
	}	//	PO

	/**
	 * 	No Init Constructor.
	 * 	Must call init
	 */
	PO()
	{
	}	//	PO

	/**
	 *  Initialize & Load existing Persistent Object.
	 *  <pre>
	 *  You load
	 * 		- an existing single key record with 	new PO (ctx, Record_ID)
	 * 			or									new PO (ctx, Record_ID, trx)
	 * 			or									new PO (ctx, rs, get_TrxName())
	 * 		- a new single key record with			new PO (ctx, 0, trx)
	 * 		- an existing multi key record with		new PO (ctx, rs, get_TrxName())
	 * 		- a new multi key record with			new PO (ctx, null)
	 *  The ID for new single key records is created automatically,
	 *  you need to set the IDs for multi-key records explicitly.
	 *	</pre>
	 *  @param ctx context
	 *  @param ID the ID if 0, the record defaults are applied - ignored if re exists
	 *  @param trx transaction name
	 *  @param rs optional - load from current result set position (no navigation, not closed)
	 */
	void init (Ctx ctx, int ID, ResultSet rs, Trx trx)
	{
		if (ctx == null)
			throw new IllegalArgumentException ("No Context");
		if (ID == 0 && rs == null && ctx.getAD_Client_ID() < 0)
			throw new IllegalArgumentException ("R/O Context - no new instances allowed");
		m_ctx = ctx;
		p_info = initPO(ctx);
		if (p_info == null || p_info.getTableName() == null)
			throw new IllegalArgumentException ("Invalid PO Info - " + p_info);
		//
		int size = p_info.getColumnCount();
		m_oldValues = new Object[size];
		m_newValues = new Object[size];
		m_trx = trx;
		if (rs != null)
			load(rs);		//	will not have virtual columns
		else
			load(ID, trx);
	}   //  PO


	/** Static Logger					*/
	private static CLogger		log = CLogger.getCLogger (PO.class);

	/** Context                 */
	private Ctx					m_ctx;
	/** Model Info              */
	protected volatile POInfo	p_info = null;

	/** Original Values         */
	private Object[]    		m_oldValues = null;
	/** New Values              */
	private Object[]    		m_newValues = null;

	/** Record_IDs          		*/
	private Object[]       		m_IDs = new Object[] {I_ZERO};
	/** Create New for Multi Key 	*/
	private boolean				m_createNew = false;
	/**	Attachment with entries		*/
	private MAttachment			m_attachment = null;
	/**	Deleted ID					*/
	private int					m_idOld = 0;
	/** Custom Columns 				*/
	private HashMap<String,String>	m_custom = null;

	/** Zero Integer				*/
	protected static final Integer I_ZERO = Integer.valueOf(0);
	/** Accounting Columns			*/
	private ArrayList <String>	s_acctColumns = null;
	/** Change VO					*/
	protected ChangeVO			p_changeVO = null;

	/**
	 *  Initialize and return PO_Info
	 *  @param ctx context
	 *  @return POInfo
	 */
	protected POInfo initPO (Ctx ctx)
	{
		return POInfo.getPOInfo(ctx, get_Table_ID());
	}

	/**
	 * 	Get PO Info
	 *	@return info
	 */
	public POInfo get_Info()
	{
		return p_info;
	}	//	get_Info

	/**
	 *  String info
	 *  @return String info
	 */
	@Override
	public String toString()
	{
		String[] keyColumns = p_info.getKeyColumns();
		if (getClass().equals(PO.class))
			return "PO[" + get_WhereClause(true) + "]";
		else if (keyColumns != null && keyColumns.length > 1)
		{
			StringBuilder sb = new StringBuilder(getClass().getName()).append("[");
			for (int i = 0; i < keyColumns.length; i++)
			{
				String keyCol = keyColumns[i];
				if (i != 0)
					sb.append(",");
				sb.append(keyCol).append("=").append(get_Value(keyCol));
			}
			sb.append("]");
			return sb.toString();
		}
		else
			return getClass().getName() + "[" + get_ID() + "]";
	}	// toString

	/**
	 *  Extended String info
	 *  @return String info
	 */
	public String toStringX()
	{
		return toString();
	}	//  toString

	/**
	 * 	Equals based on ID
	 * 	@param cmp comparator
	 * 	@return true if ID the same
	 */
	@Override
	public boolean equals (Object cmp)
	{
		if (cmp == this)
			return true;
		if (cmp == null || (cmp.getClass() != this.getClass()))
			return false;

		if (m_IDs.length < 2)
			return ((PO) cmp).get_ID() == this.get_ID();

		// Multi-Key Compare
		String keyColumns[] = p_info.getKeyColumns();
		for (String keyColumn : keyColumns) {
			Object o1 = get_Value(keyColumn);
			Object o2 = ((PO) cmp).get_Value(keyColumn);
			if (!Util.isEqual(o1, o2))
				return false;
		}
		return true;
	}	// equals

	/**
	 * 	Used for HashTable
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + get_Table_ID();
		for (Object element : m_IDs) {
			int var_code = 0;
			if (element instanceof Number)
				var_code = ((Number) element).intValue();
			else if (element != null)
				var_code = element.hashCode();
			hash = 31 * hash + var_code;
		}
		return hash;
	} // hashCode


	/**
	 * Compare based on DocumentNo, Value, Name, Description
	 * 
	 * @param o1
	 *            Object 1
	 * @param o2
	 *            Object 2
	 * @return -1 if o1 < o2
	 */
	public int compare (PO o1, PO o2)
	{
		if (o1 == null)
			return -1;
		else if (o2 == null)
			return 1;
		//	same class
		if (o1.getClass().equals(o2.getClass()))
		{
			int index = get_ColumnIndex("DocumentNo");
			if (index == -1)
				index = get_ColumnIndex("Value");
			if (index == -1)
				index = get_ColumnIndex("Name");
			if (index == -1)
				index = get_ColumnIndex("Description");
			if (index != -1)
			{
				PO po1 = o1;
				Object comp1 = po1.get_Value(index);
				PO po2 = o2;
				Object comp2 = po2.get_Value(index);
				if (comp1 == null)
					return -1;
				else if (comp2 == null)
					return 1;
				return comp1.toString().compareTo(comp2.toString());
			}
		}
		return o1.toString().compareTo(o2.toString());
	}	//	compare

	/**
	 *  Get TableName.
	 *  @return table name
	 */
	public String get_TableName()
	{
		return p_info.getTableName();
	}   //  get_TableName

	/**
	 * 	Get Access Level.
	 * 	@see getTableTrxType()
	 * 	@return access level
	 */
	protected String get_AccessLevel()
	{
		return p_info.getAccessLevel();
	}	//	get_AccessLevel

	/**
	 * 	Get Table Transaction Type
	 *	@return p_trx type MTable.TABLETRXTYPE_
	 */
	protected String getTableTrxType()
	{
		return p_info.getTableTrxType();
	}	//	get_TableTrxType

	/**
	 *  Get Key Columns.
	 *  @return table name
	 */
	@Deprecated
	public String[] get_KeyColumns()
	{
		return p_info.getKeyColumns();
	}   //  get_KeyColumns

	/**
	 *  Is Column a Key Column?
	 *  @param columnName column name
	 *  @return true if key column
	 */
	@Deprecated
	public boolean is_KeyColumn(String columnName)
	{
		return p_info.isKeyColumn(columnName);
	}   //  ist_KeyColumn

	/**
	 *  Get AD Table ID.
	 *  @return AD_Table_ID
	 */
	public abstract int get_Table_ID();

	/**
	 *  Return Single Key Record ID
	 *  @return ID or 0
	 */
	public int get_ID()
	{
		Object oo = m_IDs[0];
		if (oo != null && oo instanceof Integer)
			return ((Integer)oo).intValue();
		return 0;
	}   //  getID

	/**
	 *  Return Deleted Single Key Record ID
	 *  @return ID or 0
	 */
	public int get_IDOld()
	{
		return m_idOld;
	}   //  getID

	/**
	 * 	Get Context
	 * 	@return context
	 */
	public Ctx getCtx()
	{
		return m_ctx;
	}	//	getCtx

	/**
	 * 	Set Context to Read Only
	 * 	DO NOT USE directly. This is used for cached entries.
	 */
	public void set_ROCtx()
	{
		log.fine("R/O Ctx - Old #" + m_ctx.size());
		m_ctx = Ctx.ROCTX;
	}	//	set_ROCtx

	/**
	 * 	Get Logger
	 *	@return logger
	 */
	public CLogger get_Logger()
	{
		return log;
	}	//	getLogger

	/**************************************************************************
	 *  Get Value
	 *  @param index index
	 *  @return value
	 */
	public final Object get_Value (int index)
	{
		if (index < 0 || index >= get_ColumnCount())
		{
			log.log(Level.WARNING, "Index invalid - " + index);
			return null;
		}
		if (m_newValues[index] != null)
		{
			if (m_newValues[index].equals(Null.NULL))
				return null;
			return m_newValues[index];
		}
		return m_oldValues[index];
	}   //  get_Value

	/**
	 *  Get Value as int
	 *  @param index index
	 *  @return int value or 0
	 */
	protected int get_ValueAsInt (int index)
	{
		Object value = get_Value(index);
		if (value == null)
			return 0;
		if (value instanceof Integer)
			return ((Integer)value).intValue();
		try
		{
			return Integer.parseInt(value.toString());
		}
		catch (NumberFormatException ex)
		{
			log.warning(p_info.getColumnName(index) + " - " + ex.getMessage());
			return 0;
		}
	}   //  get_ValueAsInt

	/**
	 *  Get Value
	 *  @param columnName column name
	 *  @return value or null
	 */
	public final Object get_Value (String columnName)
	{
		int index = get_ColumnIndex(columnName);
		if (index < 0)
		{
			log.log(Level.WARNING, "Column not found - "
					+ get_TableName() + "." + columnName);
			Trace.printStack();
			return null;
		}
		return get_Value (index);
	}   //  get_Value

	/**
	 *  Get Value as Integer
	 *  @param columnName column name
	 *  @return value or 0 if null
	 */
	public final int get_ValueAsInt (String columnName)
	{
		int index = get_ColumnIndex(columnName);
		if (index < 0)
		{
			log.log(Level.WARNING, "Column not found - "
					+ get_TableName() + "." + columnName);
			Trace.printStack();
			return 0;
		}
		return get_ValueAsInt(index);
	}   //  get_Value


	/**
	 *  Get Value as Boolean
	 *  @param index index
	 *  @return boolean value or false if null
	 */
	protected boolean get_ValueAsBoolean (int index)
	{
		Object oo = get_Value(index);
		if (oo instanceof Boolean)
			return ((Boolean) oo).booleanValue();
		else
			return "Y".equals(oo);
	}   //  get_ValueAsInt

	/**
	 *  Get Value as Boolean
	 *  @param columnName column name
	 *  @return value or false if null
	 */
	public final boolean get_ValueAsBoolean (String columnName)
	{
		int index = get_ColumnIndex(columnName);
		if (index < 0)
		{
			log.log(Level.WARNING, "Column not found - "
					+ get_TableName() + "." + columnName);
			Trace.printStack();
			return false;
		}
		return get_ValueAsBoolean(index);
	}   //  get_Value


	/**
	 *  Get Value as int
	 *  @param index index
	 *  @return int value or 0
	 */
	protected BigDecimal get_ValueAsBigDecimal (int index)
	{
		Object value = get_Value(index);
		if (value instanceof BigDecimal)
			return (BigDecimal) value;
		else if (value == null)
			return Env.ZERO;
		else if (value instanceof Integer)
			return new BigDecimal((Integer) value);
		else {
			try {
				return new BigDecimal(value.toString());
			} catch (NumberFormatException ex) {
				log.warning(p_info.getColumnName(index) + " - " + ex.getMessage());
				return Env.ZERO;
			}
		}
	}   //  get_ValueAsBigDecimal


	/**
	 *  Get Value as Boolean
	 *  @param columnName column name
	 *  @return value or false if null
	 */
	public final BigDecimal get_ValueAsBigDecimal (String columnName)
	{
		int index = get_ColumnIndex(columnName);
		if (index < 0)
		{
			log.log(Level.WARNING, "Column not found - "
					+ get_TableName() + "." + columnName);
			Trace.printStack();
			return Env.ZERO;
		}
		return get_ValueAsBigDecimal(index);
	}   //  get_Value

	/**
	 * 	Get Column Value as String
	 *	@param columnName name
	 *	@return value or ""
	 */
	public String get_ValueAsString (String columnName)
	{
		Object value = get_Value(columnName);
		if( value instanceof String ) {
			return (String) value;
		}
		else if (value == null) {
			return "";
		}
		else if (value instanceof Timestamp)
		{
			long time = ((Timestamp)value).getTime();
			return String.valueOf(time);
		}
		else if(value instanceof Boolean) {
			if(((Boolean)value).booleanValue())
				return "Y";
			else
				return "N";
		}
		else
			return value.toString();
	}	//	get_ValueAsString

	/**
	 * 	Get Column Values as String
	 *	@param columnNames column names
	 *	@return string array
	 */
	public String[] get_ValuesAsString (String[] columnNames)
	{
		String[] retValue = new String[columnNames.length];
		for (int i = 0; i < columnNames.length; i++)
			retValue[i] = get_ValueAsString(columnNames[i]);
		return retValue;
	}	//	get_ValuesAsString

	/**
	 *  Get Value of Column
	 *  @param AD_Column_ID column
	 *  @return value or null
	 */
	public final Object get_ValueOfColumn (int AD_Column_ID)
	{
		int index = p_info.getColumnIndex(AD_Column_ID);
		if (index < 0)
		{
			log.log(Level.WARNING, "Not found - AD_Column_ID=" + AD_Column_ID);
			return null;
		}
		return get_Value (index);
	}   //  get_ValueOfColumn

	/**
	 *  Get Old Value
	 *  @param index index
	 *  @return value
	 */
	public final Object get_ValueOld (int index)
	{
		if (index < 0 || index >= get_ColumnCount())
		{
			log.log(Level.WARNING, "Index invalid - " + index);
			return null;
		}
		return m_oldValues[index];
	}   //  get_ValueOld

	/**
	 *  Get Old Value
	 *  @param columnName column name
	 *  @return value or null
	 */
	public final Object get_ValueOld (String columnName)
	{
		int index = get_ColumnIndex(columnName);
		if (index < 0)
		{
			log.log(Level.WARNING, "Column not found - " + columnName);
			return null;
		}
		return get_ValueOld (index);
	}   //  get_ValueOld

	/**
	 *  Get Old Value as int
	 *  @param columnName column name
	 *  @return int value or 0
	 */
	protected int get_ValueOldAsInt (String columnName)
	{
		Object value = get_ValueOld(columnName);
		if (value == null)
			return 0;
		if (value instanceof Integer)
			return ((Integer)value).intValue();
		try
		{
			return Integer.parseInt(value.toString());
		}
		catch (NumberFormatException ex)
		{
			log.warning(columnName + " - " + ex.getMessage());
			return 0;
		}
	}   //  get_ValueOldAsInt

	/**
	 *  Is Value Changed
	 *  @param index index
	 *  @return true if changed
	 */
	public final boolean is_ValueChanged (int index)
	{
		if (index < 0 || index >= get_ColumnCount())
		{
			log.log(Level.WARNING, "Index invalid - " + index);
			return false;
		}
		if (m_newValues[index] == null)
			return false;
		return !m_newValues[index].equals(m_oldValues[index]);
	}   //  is_ValueChanged

	/**
	 *  Is Value Changed
	 *  @param columnName column name
	 *  @return true if changed
	 */
	public final boolean is_ValueChanged (String columnName)
	{
		int index = get_ColumnIndex(columnName);
		if (index < 0)
		{
			log.log(Level.WARNING, "Column not found - " + columnName);
			return false;
		}
		return is_ValueChanged (index);
	}   //  is_ValueChanged

	/**
	 *  Return new - old.
	 * 	- New Value if Old Valus is null
	 * 	- New Value - Old Value if Number
	 * 	- otherwise null
	 *  @param index index
	 *  @return new - old or null if not appropiate or not changed
	 */
	public final Object get_ValueDifference (int index)
	{
		if (index < 0 || index >= get_ColumnCount())
		{
			log.log(Level.WARNING, "Index invalid - " + index);
			return null;
		}
		Object nValue = m_newValues[index];
		//	No new Value or NULL
		if (nValue == null || nValue == Null.NULL)
			return null;
		//
		Object oValue = m_oldValues[index];
		if (oValue == null || oValue == Null.NULL)
			return nValue;
		if (nValue instanceof BigDecimal)
		{
			BigDecimal obd = (BigDecimal)oValue;
			return ((BigDecimal)nValue).subtract(obd);
		}
		else if (nValue instanceof Integer)
		{
			int result = ((Integer)nValue).intValue();
			result -= ((Integer)oValue).intValue();
			return Integer.valueOf(result);
		}
		//
		log.warning("Invalid type - New=" + nValue);
		return null;
	}   //  get_ValueDifference

	/**
	 *  Return new - old.
	 * 	- New Value if Old Valus is null
	 * 	- New Value - Old Value if Number
	 * 	- otherwise null
	 *  @param columnName column name
	 *  @return new - old or null if not appropiate or not changed
	 */
	public final Object get_ValueDifference (String columnName)
	{
		int index = get_ColumnIndex(columnName);
		if (index < 0)
		{
			log.log(Level.WARNING, "Column not found - " + columnName);
			return null;
		}
		return get_ValueDifference (index);
	}   //  get_ValueDifference


	/**************************************************************************
	 *  Set Value
	 *  @param ColumnName column name
	 *  @param value value
	 *  @return true if value set
	 */
	public final boolean set_Value (String ColumnName, Object value)
	{
		if (value instanceof String
				&& ColumnName.equals("WhereClause")
				&& value.toString().toUpperCase().indexOf("=NULL") != -1)
			log.warning("Invalid Null Value - " + ColumnName + "=" + value);

		int index = get_ColumnIndex(ColumnName);
		if (index < 0)
		{
			log.log(Level.SEVERE, "Column not found - " + ColumnName);
			return false;
		}
		if (ColumnName.toUpperCase().endsWith("_ID") && value instanceof String )
		{
			log.warning("String converted to Integer for " + ColumnName + "=" + value);
			value = Integer.parseInt((String)value);
		}

		return set_Value (index, value);
	}   //  setValue

	/**
	 *  Set Encrypted Value
	 *  @param ColumnName column name
	 *  @param value value
	 *  @return true if value set
	 */
	protected final boolean set_ValueE (String ColumnName, Object value)
	{
		return set_Value (ColumnName, value);
	}   //  setValueE

	/**
	 *  Set Value if updateable and correct class.
	 *  (and to NULL if not mandatory)
	 *  @param index index
	 *  @param value value
	 *  @return true if value set
	 */
	public final boolean set_Value (int index, Object value)
	{
		if (index < 0 || index >= get_ColumnCount())
		{
			log.log(Level.WARNING, "Index invalid - " + index);
			return false;
		}
		String ColumnName = p_info.getColumnName(index);
		String colInfo = " - " + ColumnName;
		//
		if (p_info.isVirtualColumn(index))
		{
			log.log(Level.WARNING, "Virtual Column" + colInfo);
			return false;
		}
		//
		if (!p_info.isColumnUpdateable(index))
		{
			colInfo += " - NewValue=" + value + " - OldValue=" + get_Value(index);
			log.log(Level.WARNING, "Column not updateable" + colInfo);
			return false;
		}
		//
		if (value == null)
		{
			if (p_info.isColumnMandatory(index))
			{
				log.log(Level.WARNING, "Cannot set mandatory column to null " + colInfo);
				//	Trace.printStack();
				return false;
			}
			m_newValues[index] = Null.NULL;          //  correct
			log.finer(ColumnName + " = null");
		}
		else
		{
			Class<?> clazz = p_info.getColumnClass(index);
			//  matching class or generic object
			if (value.getClass().equals(clazz)
					|| clazz == Object.class)
				m_newValues[index] = value;     //  correct
			//  Integer can be set as BigDecimal
			else if (value.getClass() == BigDecimal.class
					&& clazz == Integer.class)
				m_newValues[index] = Integer.valueOf (((BigDecimal)value).intValue());
			//	Set Boolean
			else if (clazz == Boolean.class
					&& ("Y".equals(value) || "N".equals(value)) )
				m_newValues[index] = Boolean.valueOf("Y".equals(value));
			//	Button
			else if (p_info.getColumnDisplayType(index) == DisplayTypeConstants.Button)
			{
				if (ColumnName.toUpperCase().endsWith("_ID"))
				{
					if (value instanceof Integer)
						m_newValues[index] = value;
					else
						m_newValues[index] = Integer.valueOf(value.toString());
				}
				else
					m_newValues[index] = value.toString();
			}
			else
			{
				log.log(Level.SEVERE, ColumnName
						+ " - Class invalid: " + value.getClass().toString()
						+ ", Should be " + clazz.toString() + ": " + value);
				return false;
			}
			//	Validate (Min/Max)
			String error = p_info.validate(index, value);
			if (error != null)
			{
				log.log(Level.WARNING, ColumnName + "=" + value + " - " + error);
				return false;
			}
			//	Length for String
			if (clazz == String.class)
			{
				String stringValue = value.toString();
				int length = p_info.getFieldLength(index);
				if (stringValue.length() > length && length > 0)
				{
					log.warning(ColumnName + " - Too long - truncated to " + length + " - " + stringValue);
					m_newValues[index] = stringValue.substring(0,length);
				}
			}
			log.finest(ColumnName + " = " + m_newValues[index]);
		}
		set_Keys (ColumnName, m_newValues[index]);
		if (p_changeVO != null)
			p_changeVO.addChangedValue(ColumnName, value);
		return true;
	}   //  setValue

	/**
	 *  Set Value w/o check (update, r/o, ..).
	 * 	Used when Column is R/O
	 *  Required for key and parent values
	 *  @param ColumnName column name
	 *  @param value value
	 *  @return true if value set
	 */
	public final boolean set_ValueNoCheck (String ColumnName, Object value)
	{
		boolean success = true;
		int index = get_ColumnIndex(ColumnName);
		if (index < 0)
		{
			log.log(Level.SEVERE, "Column not found - " + ColumnName);
			return false;
		}
		if (value == null)
			m_newValues[index] = Null.NULL;		//	write direct
		else
		{
			Class<?> clazz = p_info.getColumnClass(index);
			//  matching class or generic object
			if (value.getClass().equals(clazz)
					|| clazz == Object.class)
				m_newValues[index] = value;     //  correct
			//  Integer can be set as BigDecimal
			else if (value.getClass() == BigDecimal.class
					&& clazz == Integer.class)
				m_newValues[index] = Integer.valueOf (((BigDecimal)value).intValue());
			//	Set Boolean
			else if (clazz == Boolean.class
					&& ("Y".equals(value) || "N".equals(value)) )
				m_newValues[index] = Boolean.valueOf("Y".equals(value));
			else
			{
				log.warning (ColumnName
						+ " - Class invalid: " + value.getClass().toString()
						+ ", Should be " + clazz.toString() + ": " + value);
				m_newValues[index] = value;     //  correct
			}
			//	Validate (Min/Max)
			String error = p_info.validate(index, value);
			if (error != null)
			{
				success = false;
				log.warning(ColumnName + "=" + value + " - " + error);
				m_newValues[index] = m_oldValues[index];
			}
			//	length for String
			if (clazz == String.class)
			{
				String stringValue = value.toString();
				int length = p_info.getFieldLength(index);
				if (stringValue.length() > length && length > 0)
				{
					log.warning(ColumnName + " - truncated (" + length + "): " + stringValue);
					m_newValues[index] = stringValue.substring(0,length);
				}
			}
		}
		log.finest(ColumnName + " = " + m_newValues[index]
		                                            + " (" + (m_newValues[index]==null ? "-" : m_newValues[index].getClass().getName()) + ")");
		set_Keys (ColumnName, m_newValues[index]);
		if (p_changeVO != null)
			p_changeVO.addChangedValue(ColumnName, value);
		return success;
	}   //  set_ValueNoCheck

	/**
	 *  Set Encrypted Value w/o check (update, r/o, ..).
	 * 	Used when Column is R/O
	 *  Required for key and parent values
	 *  @param ColumnName column name
	 *  @param value value
	 *  @return true if value set
	 */
	protected final boolean set_ValueNoCheckE (String ColumnName, Object value)
	{
		return set_ValueNoCheck (ColumnName, value);
	}	//	set_ValueNoCheckE

	/**
	 * 	Set Value from String.
	 * 	No Check if column is updateable or mandatory
	 *	@param index index
	 *	@param stringValue value
	 *	@return error message or null
	 */
	protected String set_ValueString (int index, String stringValue)
	{
		try
		{
			String error = null;
			if (index < 0 || index >= get_ColumnCount())
				return "Index invalid - " + index;
			String ColumnName = p_info.getColumnName(index);
			String colInfo = " - " + ColumnName;
			//
			if (p_info.isVirtualColumn(index))
				return "Virtual Column" + colInfo;
			/** Mandatory	*/
			if (stringValue == null)
			{
				//	if (p_info.isColumnMandatory(index))
				//		return "Cannot set mandatory column to null " + colInfo;
				m_newValues[index] = Null.NULL;          //  correct
				log.finer(ColumnName + " = null");
			}
			else
			{
				Class<?> clazz = p_info.getColumnClass(index);
				//	Button
				if (p_info.getColumnDisplayType(index) == DisplayTypeConstants.Button)
				{
					if (ColumnName.toUpperCase().endsWith("_ID"))
						m_newValues[index] = Integer.valueOf(stringValue);
					else
						m_newValues[index] = stringValue;
				}
				else if (clazz == String.class)
				{
					m_newValues[index] = stringValue;
					int length = p_info.getFieldLength(index);
					if (stringValue.length() > length && length > 0)
					{
						log.warning(ColumnName + " - truncated (" + length + "): " + stringValue);
						m_newValues[index] = stringValue.substring(0,length);
					}
				}
				//  Integer
				else if (clazz == Integer.class)
					m_newValues[index] = Integer.valueOf(stringValue);
				//	BigDecimal
				else if (clazz == BigDecimal.class)
					m_newValues[index] = new BigDecimal(stringValue);
				//	Set Timestamp
				else if (clazz == Timestamp.class)
				{
					long time = Long.parseLong(stringValue);
					m_newValues[index] = new Timestamp(time);
				}
				//	Set Boolean
				else if (clazz == Boolean.class)
				{
					if (!("Y".equals(stringValue) || "N".equals(stringValue)))
						log.warning ("Boolean value = " + stringValue);
					m_newValues[index] = Boolean.valueOf("Y".equals(stringValue));
				}
				else
				{
					return ColumnName + ": Class unknown: " + clazz;
				}
				//	Validate (Min/Max)
				error = p_info.validate(index, stringValue);
				if (error != null)
					return ColumnName + ": " + error;
				log.finest(ColumnName + " = " + m_newValues[index]);
			}
			set_Keys (ColumnName, m_newValues[index]);
			return error;
		}
		catch(Exception e)
		{
			return e.getMessage();
		}
	}	//	set_ValueString

	/**
	 *  Set Value of Column
	 *  @param AD_Column_ID column
	 *  @param value value
	 */
	public final void set_ValueOfColumn (int AD_Column_ID, Object value)
	{
		int index = p_info.getColumnIndex(AD_Column_ID);
		if (index < 0)
			log.log(Level.SEVERE, "Not found - AD_Column_ID=" + AD_Column_ID);
		String ColumnName = p_info.getColumnName(index);
		if (ColumnName.equals("IsApproved"))
			set_ValueNoCheck(ColumnName, value);
		else
			set_Value (index, value);
	}   //  setValueOfColumn


	/**
	 * 	Set Custom Column
	 *	@param columnName column
	 *	@param value value
	 */
	public final void set_CustomColumn (String columnName, Object value)
	{
		if (m_custom == null)
			m_custom = new HashMap<String,String>();
		String valueString = "NULL";
		if (value == null)
			;
		else if (value instanceof Number)
			valueString = value.toString();
		else if (value instanceof Boolean)
			valueString = ((Boolean)value).booleanValue() ? "'Y'" : "'N'";
		else if (value instanceof Timestamp)
			valueString = DB.TO_DATE((Timestamp)value, false);
		else //	if (value instanceof String)
			valueString = DB.TO_STRING(value.toString());
		//	Save it
		log.log(Level.INFO, columnName + "=" + valueString);
		m_custom.put(columnName, valueString);
		if (p_changeVO != null)
			p_changeVO.addChangedValue(columnName, valueString);
	}	//	set_CustomColumn


	/**
	 *  Set (numeric) Key Value
	 *  @param ColumnName column name
	 *  @param value value
	 */
	private void set_Keys (String ColumnName, Object value)
	{
		String[] keyColumns = p_info.getKeyColumns();
		//	Update if KeyColumn
		for (int i = 0; i < m_IDs.length; i++)
		{
			if (ColumnName.equals (keyColumns[i]))
				m_IDs[i] = value;
		}	//	for all key columns
	}	//	setKeys


	/**
	 * 	set Context
	 */
	protected void setCtx(Ctx ctx)
	{
		m_ctx = ctx;
	}	//	setCtx


	/**************************************************************************
	 *  Get Column Count
	 *  @return column count
	 */
	public int get_ColumnCount()
	{
		return p_info.getColumnCount();
	}   //  getColumnCount

	/**
	 *  Get Column Name
	 *  @param index index
	 *  @return ColumnName
	 */
	public String get_ColumnName (int index)
	{
		return p_info.getColumnName (index);
	}   //  getColumnName

	/**
	 *  Get Column Label
	 *  @param index index
	 *  @return Column Label
	 */
	protected String get_ColumnLabel (int index)
	{
		return p_info.getColumnLabel (index);
	}   //  getColumnLabel

	/**
	 *  Get Column Description
	 *  @param index index
	 *  @return column description
	 */
	protected String get_ColumnDescription (int index)
	{
		return p_info.getColumnDescription (index);
	}   //  getColumnDescription

	/**
	 *  Is Column Mandatory
	 *  @param index index
	 *  @return true if column mandatory
	 */
	protected boolean isColumnMandatory (int index)
	{
		return p_info.isColumnMandatory(index);
	}   //  isColumnNandatory

	/**
	 *  Is Column Updateable
	 *  @param index index
	 *  @return true if column updateable
	 */
	protected boolean isColumnUpdateable (int index)
	{
		return p_info.isColumnUpdateable(index);
	}	//	isColumnUpdateable

	/**
	 *  Set Column Updateable
	 *  @param index index
	 *  @param updateable column updateable
	 */
	protected void set_ColumnUpdateable (int index, boolean updateable)
	{
		p_info.setColumnUpdateable(index, updateable);
	}	//	setColumnUpdateable

	/**
	 * 	Set all columns updateable
	 * 	@param updateable updateable
	 */
	protected void setUpdateable (boolean updateable)
	{
		p_info.setUpdateable (updateable);
	}	//	setUpdateable

	/**
	 *  Get Column DisplayType
	 *  @param index index
	 *  @return display type
	 */
	public int get_ColumnDisplayType (int index)
	{
		return p_info.getColumnDisplayType(index);
	}	//	getColumnDisplayType

	/**
	 *  Get Lookup
	 *  @param index index
	 *  @return Lookup or null
	 */
	protected Lookup get_ColumnLookup(int index)
	{
		return p_info.getColumnLookup(getCtx(), index);
	}   //  getColumnLookup

	/**
	 *  Get Column Index
	 *  @param columnName column name
	 *  @return index of column with ColumnName or -1 if not found
	 */
	public final int get_ColumnIndex (String columnName)
	{
		return p_info.getColumnIndex(columnName);
	}   //  getColumnIndex

	/**
	 * 	Get Display Value of value
	 *	@param columnName columnName
	 *	@param currentValue current value
	 *	@return String value with "./." as null
	 */
	protected String get_DisplayValue(String columnName, boolean currentValue)
	{
		Object value = currentValue ? get_Value(columnName) : get_ValueOld(columnName);
		if (value == null)
			return "./.";
		String retValue = value.toString();
		int index = get_ColumnIndex(columnName);
		if (index < 0)
			return retValue;
		int dt = get_ColumnDisplayType(index);
		if (FieldType.isText(dt) || DisplayTypeConstants.YesNo == dt)
			return retValue;
		//	Lookup
		Lookup lookup = get_ColumnLookup(index);
		if (lookup != null)
			return lookup.getDisplay(value);
		//	Other
		return retValue;
	}	//	get_DisplayValue


	/**
	 * 	Copy old values of From to new values of To.
	 *  Does not copy Keys
	 * 	@param from old, existing & unchanged PO
	 *  @param to new, not saved PO
	 * 	@param AD_Client_ID client
	 * 	@param AD_Org_ID org
	 */
	protected static void copyValues (PO from, PO to, int AD_Client_ID, int AD_Org_ID)
	{
		copyValues (from, to);
		to.setAD_Client_ID(AD_Client_ID);
		to.setAD_Org_ID(AD_Org_ID);
	}	//	copyValues

	/**
	 * 	Copy old values of From to new values of To.
	 *  Does not copy Keys and AD_Client_ID/AD_Org_ID
	 * 	@param from old, existing & unchanged PO
	 *  @param to new, not saved PO
	 */
	public static void copyValues (PO from, PO to)
	{
		log.fine("From ID=" + from.get_ID() + " - To ID=" + to.get_ID());
		//	Different Classes
		if (from.getClass() != to.getClass())
		{
			for (int i1 = 0; i1 < from.m_oldValues.length; i1++)
			{
				if (from.p_info.isVirtualColumn(i1)
						|| from.p_info.isKey(i1))		//	KeyColumn
					continue;
				String colName = from.p_info.getColumnName(i1);
				//  Ignore Standard Values
				if (colName.startsWith("Created")
						|| colName.startsWith("Updated")
						|| colName.equals("IsActive")
						|| colName.equals("AD_Client_ID")
						|| colName.equals("AD_Org_ID"))
					;	//	ignore
				else
				{
					for (int i2 = 0; i2 < to.m_oldValues.length; i2++)
					{
						if (to.p_info.getColumnName(i2).equals(colName))
						{
							to.m_newValues[i2] = from.m_oldValues[i1];
							break;
						}
					}
				}
			}	//	from loop
		}
		else	//	same class
		{
			for (int i = 0; i < from.m_oldValues.length; i++)
			{
				if (from.p_info.isVirtualColumn(i)
						|| from.p_info.isKey(i))		//	KeyColumn
					continue;
				String colName = from.p_info.getColumnName(i);
				//  Ignore Standard Values
				if (colName.startsWith("Created")
						|| colName.startsWith("Updated")
						|| colName.equals("IsActive")
						|| colName.equals("AD_Client_ID")
						|| colName.equals("AD_Org_ID"))
					;	//	ignore
				else
					to.m_newValues[i] = from.m_oldValues[i];
			}
		}	//	same class
	}	//	copy

	/**
	 * 	Get Change VO
	 *	@return change vo
	 */
	public ChangeVO get_ChangeVO()
	{
		return p_changeVO;
	}	//	get_ChangeVO

	/**
	 * 	Set Change VO
	 *	@param change vo
	 */
	public void set_ChangeVO(ChangeVO change)
	{
		p_changeVO = change;
	}	//	set_ChangeVO

	/**************************************************************************
	 *  Load record with ID
	 * 	@param ID ID
	 * 	@param trx transaction name
	 */
	public void load (int ID, Trx trx)
	{
		log.finest("ID=" + ID);
		if (ID > 0)
		{
			m_IDs = new Object[] {Integer.valueOf(ID)};
			load(trx);
		}
		else	//	new
		{

			loadDefaults();
			m_createNew = true;
			setKeyInfo();	//	sets m_IDs
			loadComplete(true);
		}
	}	//	load


	/**
	 *  (re)Load record with m_ID[*]
	 *  @param trx transaction
	 *  @return true if loaded
	 */
	public boolean load (Trx trx)
	{
		m_trx = trx;
		boolean success = true;

		String sql = p_info.getLoadSQL();

		int size = get_ColumnCount();

		//
		//	int index = -1;
		if (CLogMgt.isLevelFinest())
			log.finest(get_WhereClause(true));
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, m_trx);	//	local p_trx only
			for (int i = 0; i < m_IDs.length; i++)
			{
				Object oo = m_IDs[i];
				if (oo instanceof Integer)
					pstmt.setInt(i+1, ((Integer)m_IDs[i]).intValue());
				else
					pstmt.setString(i+1, m_IDs[i].toString());
			}
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				success = load(rs);
			}
			else
			{
				log.log(Level.WARNING, "NO Data found for " + get_WhereClause(true), new Exception());
				m_IDs = new Object[] {I_ZERO};
				success = false;
				//	throw new DBException("NO Data found for " + get_WhereClause(true));
			}
			m_createNew = false;
			//	reset new values
			m_newValues = new Object[size];
		}
		catch (Exception e)
		{
			String msg = "";
			if (m_trx != null)
				msg = "[" + m_trx + "] - ";
			msg += get_WhereClause(true)
			//	+ ", Index=" + index
			//	+ ", Column=" + get_ColumnName(index)
			//	+ ", " + p_info.toString(index)
			+ ", SQL=" + sql.toString();
			success = false;
			m_IDs = new Object[] {I_ZERO};
			log.log(Level.SEVERE, msg, e);
			//	throw new DBException(e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		loadComplete(success);
		return success;
	}   //  load


	private boolean m_hasVirtualColumns = false;

	private boolean hasVirtualColumns () {
		return m_hasVirtualColumns;
	}

	private void setHasVirtualColumns (boolean hasVirtualColumns) {
		m_hasVirtualColumns = hasVirtualColumns;
	}
	/**
	 * 	Load from the current position of a ResultSet
	 * 	@param rs result set
	 * 	@return true if loaded
	 */
	protected boolean load (ResultSet rs)
	{
		int size = get_ColumnCount();
		boolean success = true;
		int index = 0;
		log.finest("(rs)");
		//  load column values
		for (index = 0; index < size; index++)
		{
			String columnName = p_info.getColumnName(index);
			if (DB.isMSSQLServer() && columnName.equals("[LineNo]"))
				columnName = "LineNo";
			//	Make sure Virtual Column is part of Result Set
			if (p_info.isVirtualColumn(index))
			{
				try
				{
					int ii = rs.findColumn(columnName);
					if (ii < 0)
						continue;
				}
				catch (Exception e)
				{
					continue;
				}
				setHasVirtualColumns(true);
			}
			Class<?> clazz = p_info.getColumnClass(index);
			int dt = p_info.getColumnDisplayType(index);
			try
			{
				if (clazz == Integer.class)
					m_oldValues[index] = decrypt(index, Integer.valueOf(rs.getInt(columnName)));
				else if (clazz == BigDecimal.class)
					m_oldValues[index] = decrypt(index, rs.getBigDecimal(columnName));
				else if (clazz == Boolean.class)
					m_oldValues[index] = Boolean.valueOf ("Y".equals(decrypt(index, rs.getString(columnName))));
				else if (clazz == Timestamp.class)
					m_oldValues[index] = decrypt(index, rs.getTimestamp(columnName));
				else if (FieldType.isLOB(dt))
					m_oldValues[index] = get_LOB (rs.getObject(columnName));
				else if (clazz == String.class)
					m_oldValues[index] = decrypt(index, rs.getString(columnName));
				else
					m_oldValues[index] = loadSpecial(rs, index);
				//	NULL
				if (rs.wasNull() && m_oldValues[index] != null)
					m_oldValues[index] = null;
				//
				if (CLogMgt.isLevelAll())
					log.finest(String.valueOf(index) + ": " + p_info.getColumnName(index)
							+ "(" + clazz + ") = " + m_oldValues[index]);
			}
			catch (SQLException e)
			{
				if (p_info.isVirtualColumn(index))	//	if rs constructor used
					log.log(Level.FINER, "Virtual Column not loaded: " + columnName);
				else
				{
					log.log(Level.WARNING, "(rs) - " + String.valueOf(index)
							+ ": " + p_info.getTableName() + "." + p_info.getColumnName(index)
							+ " (" + clazz + ") - " + e);
					success = false;
				}
			}
		}
		m_createNew = false;
		setKeyInfo();
		loadComplete(success);
		return success;
	}	//	load

	/**
	 * 	Load from HashMap.
	 * 	Do not use unless you know what you are doing.
	 * 	@param hmIn hash map
	 * 	@return true if loaded
	 */
	public boolean load (Map<String,String> hmIn)
	{
		int size = get_ColumnCount();
		boolean success = true;
		//
		boolean keyHasNoValue = true;
		String[] keyColumns = p_info.getKeyColumns();
		if (keyColumns.length > 0)
		{
			for (String keyColumn : keyColumns) {
				String value = hmIn.get(keyColumn);
				if (value != null)
				{
					int index = p_info.getColumnIndex(keyColumn);
					m_oldValues[index] = Integer.valueOf(value);
				}
				keyHasNoValue = false;
			}
		}
		//
		log.finest("(hm)");
		int index = 0;
		//  load column values
		for (index = 0; index < size; index++)
		{
			String columnName = p_info.getColumnName(index);
			String value = hmIn.get(columnName);
			if (value == null || value.length() == 0)
				continue;
			Class<?> clazz = p_info.getColumnClass(index);
			int dt = p_info.getColumnDisplayType(index);
			try
			{
				if (clazz == Integer.class)
					m_newValues[index] = Integer.valueOf(value);
				else if (clazz == BigDecimal.class)
					m_newValues[index] = new BigDecimal(value);
				else if (clazz == Boolean.class)
					m_newValues[index] = Boolean.valueOf ("Y".equals(value)
							|| "on".equals (value) || "true".equals(value));
				else if (clazz == Timestamp.class)
					m_newValues[index] = convertToTimestamp(value);
				else if (FieldType.isLOB(dt))
					m_newValues[index] = null;	//	get_LOB (rs.getObject(columnName));
				else if (clazz == String.class)
				{
					m_newValues[index] = value;
					int length = p_info.getFieldLength (index);
					if (value.length() > length)
					{
						m_newValues[index] = value.substring (0, length);
						log.warning(columnName + ": truncated to length=" + length + " from=" + value);
					}
				}
				else
					m_newValues[index] = null;	// loadSpecial(rs, index);
				//
				if (CLogMgt.isLevelAll())
					log.finest(String.valueOf(index) + ": " + p_info.getColumnName(index)
							+ "(" + clazz + ") = " + m_oldValues[index]);
			}
			catch (Exception e)
			{
				if (p_info.isVirtualColumn(index))	//	if rs constructor used
					log.log(Level.FINER, "Virtual Column not loaded: " + columnName);
				else
				{
					log.log(Level.SEVERE, "(ht) - " + String.valueOf(index)
							+ ": " + p_info.getTableName() + "." + p_info.getColumnName(index)
							+ " (" + clazz + ") - " + e);
					success = false;
				}
			}
		}
		//	Overwrite
		//	setStandardDefaults();
		setKeyInfo();
		loadComplete(success);
		//
		m_createNew = keyHasNoValue;
		return success;
	}	//	load

	/**
	 *  Create HashMap with data as Strings
	 *  @return HashMap
	 */
	protected HashMap<String,String> get_HashMap()
	{
		HashMap<String,String> hmOut = new HashMap<String,String>();
		fillMap(hmOut);
		return hmOut;
	}	//	get_HashMap

	/**
	 * 	Get Object as VO
	 *	@return
	 */
	public VO getVO()
	{
		VO vo = new VO(get_TableName(), get_Table_ID(), get_ID());
		fillMap (vo);
		return vo;
	}	//	getVO

	/**
	 *  Fill map with data as Strings
	 *  @param hmOut map
	 */
	private void fillMap(Map<String,String> hmOut)
	{
		int size = get_ColumnCount();
		for (int i = 0; i < size; i++)
		{
			Object value = get_Value(i);
			//	Don't insert NULL values (allows Database defaults)
			if (value == null
					|| p_info.isVirtualColumn(i))
				continue;
			//	Display Type
			int dt = p_info.getColumnDisplayType(i);
			//  Based on class of definition, not class of value
			Class<?> c = p_info.getColumnClass(i);
			String stringValue = null;
			if (c == Object.class)
				;	//	saveNewSpecial (value, i));
			else if (value == null || value.equals (Null.NULL))
				;
			else if (value instanceof Integer || value instanceof BigDecimal)
				stringValue = value.toString();
			else if (c == Boolean.class)
			{
				boolean bValue = false;
				if (value instanceof Boolean)
					bValue = ((Boolean)value).booleanValue();
				else
					bValue = "Y".equals(value);
				stringValue = bValue ? "Y" : "N";
			}
			else if (value instanceof Timestamp)
				stringValue = value.toString();
			else if (c == String.class)
				stringValue = (String)value;
			else if (dt == DisplayTypeConstants.TextLong)
				stringValue = (String)value;
			else if (dt == DisplayTypeConstants.Binary)
				stringValue = Util.toHexString((byte[])value);
			else
				;	//	saveNewSpecial (value, i));
			//
			if (stringValue != null)
				hmOut.put(p_info.getColumnName(i), stringValue);
		}
		//	Custom Columns
		if (m_custom != null)
		{
			Iterator<String> it = m_custom.keySet().iterator();
			while (it.hasNext())
			{
				String column = it.next();
				//	int index = p_info.getColumnIndex(column);
				String value = m_custom.get(column);
				if (value != null)
					hmOut.put(column, value);
			}
			m_custom = null;
		}
	}   //  fillMap

	/**
	 *  Load Special data (images, ..).
	 *  To be extended by sub-classes
	 *  @param rs result set
	 *  @param index zero based index
	 *  @return value value
	 *  @throws SQLException
	 */
	protected Object loadSpecial (ResultSet rs, int index) throws SQLException
	{
		log.finest("(NOP) - " + p_info.getColumnName(index));
		return null;
	}   //  loadSpecial

	/**
	 *  Load is complete
	 * 	@param success success
	 *  To be extended by sub-classes
	 */
	protected void loadComplete (boolean success)
	{
	}   //  loadComplete


	/**
	 *	Load Defaults
	 */
	protected void loadDefaults()
	{
		setStandardDefaults();
		//
		/** @todo defaults from Field */
		//	MField.getDefault(p_info.getDefaultLogic(i));
	}	//	loadDefaults

	/**
	 *  Set Default values.
	 *  Client, Org, Created/Updated, *By, IsActive
	 */
	protected void setStandardDefaults()
	{
		int size = get_ColumnCount();
		for (int i = 0; i < size; i++)
		{
			if (p_info.isVirtualColumn(i))
				continue;
			String colName = p_info.getColumnName(i);
			//  Set Standard Values
			if (colName.endsWith("tedBy"))
				m_newValues[i] = Integer.valueOf (m_ctx.getAD_User_ID());
			else if (colName.equals("Created") || colName.equals("Updated"))
				m_newValues[i] = new Timestamp (System.currentTimeMillis());
			else if (colName.equalsIgnoreCase(p_info.getTableName() + "_ID"))    //  KeyColumn
				m_newValues[i] = I_ZERO;
			else if (colName.equals("IsActive"))
				m_newValues[i] = Boolean.valueOf(true);
			else if (colName.equals("AD_Client_ID"))
				m_newValues[i] = Integer.valueOf(m_ctx.getAD_Client_ID());
			else if (colName.equals("AD_Org_ID"))
				m_newValues[i] = Integer.valueOf(m_ctx.getAD_Org_ID());
			else if (colName.equals("Processed"))
				m_newValues[i] = Boolean.valueOf(false);
			else if (colName.equals("Processing"))
				m_newValues[i] = Boolean.valueOf(false);
			else if (colName.equals("Posted"))
				m_newValues[i] = Boolean.valueOf(false);
		}
	}   //  setDefaults

	/**
	 * 	Set Key Info (IDs and KeyColumns).
	 */
	private void setKeyInfo()
	{
		//	Search for Primary Key
		for (int i = 0; i < p_info.getColumnCount(); i++)
		{
			if (p_info.isKey(i))
			{
				String ColumnName = p_info.getColumnName(i);
				if (p_info.getColumnName(i).toUpperCase().endsWith("_ID"))
				{
					Integer ii = (Integer)get_Value(i);
					if (ii == null)
						m_IDs = new Object[] {I_ZERO};
					else
						m_IDs = new Object[] {ii};
					log.finest("(PK) " + ColumnName + "=" + ii);
				}
				else
				{
					Object oo = get_Value(i);
					if (oo == null)
						m_IDs = new Object[] {null};
					else
						m_IDs = new Object[] {oo};
					log.finest("(PK) " + ColumnName + "=" + oo);
				}
				return;
			}
		}	//	primary key search

		String[] keyColumns = p_info.getKeyColumns();
		//	Set FKs
		int size = keyColumns.length;
		if (size == 0)
			throw new CompiereStateException("No PK nor FK - " + p_info.getTableName());
		m_IDs = new Object[size];
		for (int i = 0; i < size; i++)
		{
			if (keyColumns[i].toUpperCase().endsWith("_ID"))
			{
				Integer ii = null;
				try
				{
					ii = (Integer)get_Value(keyColumns[i]);
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, "", e);
				}
				if (ii != null)
					m_IDs[i] = ii;
			}
			else
				m_IDs[i] = get_Value(keyColumns[i]);
			log.finest("(FK) " + keyColumns[i] + "=" + m_IDs[i]);
		}
	}	//	setKeyInfo


	/**************************************************************************
	 *  Are all mandatory Fields filled (i.e. can we save)?.
	 *  Stops at first null mandatory field
	 *  @return true if all mandatory fields are ok
	 */
	protected boolean isMandatoryOK()
	{
		int size = get_ColumnCount();
		for (int i = 0; i < size; i++)
		{
			if (p_info.isColumnMandatory(i))
			{
				if (p_info.isVirtualColumn(i))
					continue;
				if (get_Value(i) == null || get_Value(i).equals(Null.NULL))
				{
					log.info(p_info.getColumnName(i));
					return false;
				}
			}
		}
		return true;
	}   //  isMandatoryOK


	/**************************************************************************
	 * 	Set AD_Client
	 * 	@param AD_Client_ID client
	 */
	final public void setAD_Client_ID (int AD_Client_ID)
	{
		set_ValueNoCheck ("AD_Client_ID", Integer.valueOf(AD_Client_ID));
	}	//	setAD_Client_ID

	/**
	 * 	Get AD_Client
	 * 	@return AD_Client_ID
	 */
	public final int getAD_Client_ID()
	{
		Integer ii = (Integer)get_Value("AD_Client_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}	//	getAD_Client_ID

	/**
	 * 	Set AD_Org
	 * 	@param AD_Org_ID org
	 */
	final public void setAD_Org_ID (int AD_Org_ID)
	{
		set_ValueNoCheck ("AD_Org_ID", Integer.valueOf(AD_Org_ID));
	}	//	setAD_Org_ID

	/**
	 * 	Get AD_Org
	 * 	@return AD_Org_ID
	 */
	public int getAD_Org_ID()
	{
		Integer ii = (Integer)get_Value("AD_Org_ID");
		if (ii == null)
			return 0;
		return ii.intValue();
	}	//	getAD_Org_ID

	/**
	 * 	Overwrite Client Org if different
	 *	@param AD_Client_ID client
	 *	@param AD_Org_ID org
	 */
	public void setClientOrg (int AD_Client_ID, int AD_Org_ID)
	{
		if (AD_Client_ID != getAD_Client_ID())
			setAD_Client_ID(AD_Client_ID);
		if (AD_Org_ID != getAD_Org_ID())
			setAD_Org_ID(AD_Org_ID);
	}	//	setClientOrg

	/**
	 * 	Overwrite Client Org if different
	 *	@param po persistent object
	 */
	public void setClientOrg (PO po)
	{
		setClientOrg(po.getAD_Client_ID(), po.getAD_Org_ID());
	}	//	setClientOrg

	/**
	 * 	Set Active
	 * 	@param active active
	 */
	public final void setIsActive (boolean active)
	{
		set_Value("IsActive", Boolean.valueOf(active));
	}	//	setActive

	/**
	 *	Is Active
	 *  @return is active
	 */
	public final boolean isActive()
	{
		Boolean bb = (Boolean)get_Value("IsActive");
		if (bb != null)
			return bb.booleanValue();
		return false;
	}	//	isActive

	/**
	 * 	Get Created
	 * 	@return created
	 */
	final public Timestamp getCreated()
	{
		return (Timestamp)get_Value("Created");
	}	//	getCreated

	/**
	 * 	Get Updated
	 *	@return updated
	 */
	final public Timestamp getUpdated()
	{
		return (Timestamp)get_Value("Updated");
	}	//	getUpdated

	/**
	 * 	Get CreatedBy
	 * 	@return AD_User_ID
	 */
	final public int getCreatedBy()
	{
		Integer ii = (Integer)get_Value("CreatedBy");
		if (ii == null)
			return 0;
		return ii.intValue();
	}	//	getCreateddBy

	/**
	 * 	Get UpdatedBy
	 * 	@return AD_User_ID
	 */
	final public int getUpdatedBy()
	{
		Integer ii = (Integer)get_Value("UpdatedBy");
		if (ii == null)
			return 0;
		return ii.intValue();
	}	//	getUpdatedBy

	/**
	 * 	Set UpdatedBy
	 * 	@param AD_User_ID user
	 */
	final protected void setUpdatedBy (int AD_User_ID)
	{
		set_ValueNoCheck ("UpdatedBy", Integer.valueOf(AD_User_ID));
	}	//	setAD_User_ID

	/**
	 * 	Get Entity Type of record
	 *	@return entity type or null if none
	 */
	public String get_EntityType()
	{
		int index = get_ColumnIndex("EntityType");
		if (index != -1)
			return (String)get_Value(index);
		return null;
	}	//	getEntityType


	/**
	 * 	Get Translation of column
	 *	@param columnName
	 *	@param AD_Language
	 *	@return translation or null if not found
	 */
	protected String get_Translation (String columnName, String AD_Language)
	{
		if (columnName == null || AD_Language == null
				|| m_IDs.length > 1 || m_IDs[0].equals(I_ZERO)
				|| !(m_IDs[0] instanceof Integer))
		{
			log.severe ("Invalid Argument: ColumnName" + columnName
					+ ", AD_Language=" + AD_Language
					+ ", ID.length=" + m_IDs.length + ", ID=" + m_IDs[0]);
			return null;
		}
		int ID = ((Integer)m_IDs[0]).intValue();
		String retValue = null;
		StringBuilder sql = new StringBuilder ("SELECT ").append(columnName)
		.append(" FROM ").append(p_info.getTableName()).append("_Trl WHERE ")
		.append(p_info.getKeyColumns()[0]).append("=?")
		.append(" AND AD_Language=?");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), get_Trx());
			pstmt.setInt (1, ID);
			pstmt.setString (2, AD_Language);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = rs.getString(1);
		}
		catch (Exception e)	{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	get_Translation

	/**
	 * 	Is new record
	 *	@return true if new
	 */
	public boolean is_new()
	{
		if (m_createNew)
			return true;
		//
		for (Object element : m_IDs) {
			if (element.equals(I_ZERO))
				continue;
			return false;	//	one value is non-zero
		}
		return true;
	}	//	is_new

	/**
	 * 	Update PO based on string context
	 *	@param context row context
	 *	@return error message or null
	 */
	public String update (Map<String,String> context)
	{
		String error = null;
		Set<Map.Entry<String,String>> set = context.entrySet();
		Iterator<Map.Entry<String,String>> it = set.iterator();
		while (it.hasNext())
		{
			Map.Entry<String,String> entry = it.next();
			String columnName = entry.getKey();
			int index = get_ColumnIndex(columnName);
			if (index != -1)	//	ignore all other
			{
				//	Ignore Key Column
				if (p_info.isKeyColumn(columnName)&&!columnName.equalsIgnoreCase("EntityType"))
					continue;
				//	Virtual Column
				if (p_info.isVirtualColumn(index))
					continue;
				//	Don't allow to overwrite Std Column for users
				if (columnName.equals("Created") || columnName.equals("CreatedBy")
						|| columnName.equals("Updated") || columnName.equals("UpdatedBy"))
					continue;
				//	New
				String stringValue = entry.getValue();
				if (stringValue != null &&
						(stringValue.equals(Null.NULLString)
								|| stringValue.length () == 0))
					stringValue = null;
				//	Old
				String oldValue = get_ValueAsString(columnName);
				if (oldValue.length() == 0)
					oldValue = null;

				//	Same
				if (oldValue == null && stringValue == null
						|| stringValue != null && oldValue != null
						&& stringValue.equals(oldValue))
					continue;
				//
				error = set_ValueString (index, stringValue);
				if (error != null)
					return error;
			}
		}
		return error;
	}	//	update

	/**************************************************************************
	 *  Update Value or create new record.
	 * 	To reload call load() - not updated
	 *  @return true if saved
	 */
	public boolean save()
	{
		CLogger.resetLast();

		boolean newRecord = is_new();	//	save locally as load resets
		if (!newRecord && !is_Changed())
		{
			log.fine("Nothing changed - " + p_info.getTableName());
			return true;
		}

		boolean valid = validateBeforeSave(this);
		if(!valid)
			return false;

		//	Save
		if (newRecord)
			return saveNew();
		else
			return saveUpdate();
	}	//	save



	/**
	 * Perform validation before saving
	 * @param po
	 * @return
	 */
	private static boolean validateBeforeSave(PO po){
		if (po.getCtx().getAD_Client_ID() < 0)
			throw new IllegalArgumentException("R/O Context - no new instances allowed");

		boolean newRecord = po.is_new();	//	save locally as load resets

		//	Organization Check
		String TableTrxType = po.p_info.getTableTrxType();
		boolean orgRequired = X_AD_Table.TABLETRXTYPE_MandatoryOrganization.equals(TableTrxType);
		if (po.getAD_Org_ID() == 0)
		{
			if (!orgRequired)
			{
				Boolean shared = MClientShare.isShared(po.getAD_Client_ID(), po.get_Table_ID());
				orgRequired = shared != null && !shared.booleanValue();
			}
			if (orgRequired)
			{
				log.saveError("FillMandatory", Msg.getElement(po.getCtx(), "AD_Org_ID"));
				return false;
			}
		}
		else if (!orgRequired)	//	Org <> 0
		{
			boolean reset = X_AD_Table.TABLETRXTYPE_NoOrganization.equals(TableTrxType);
			if (!reset)
			{
				Boolean shared = MClientShare.isShared(po.getAD_Client_ID(), po.get_Table_ID());
				reset = shared != null && shared.booleanValue();
			}
			if (reset)
			{
				log.warning("Set Org to 0");
				po.setAD_Org_ID(0);
			}
		}


		//	Before Save
		MAssignSet.execute(po, newRecord);	//	Auto Value Assignment
		try
		{
			if (!po.beforeSave(newRecord))
			{
				log.warning("beforeSave failed - " + po.toString());
				// the subclasses of PO that return false in beforeDelete()
				// should have already called CLogger.saveError()
				if (!CLogger.hasError())
					log.saveError("Error", "BeforeSave failed", false);
				return false;
			}
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, "beforeSave - " + po.toString(), e);
			log.saveError("Error", e.toString(), false);
			//	throw new DBException(e);
			return false;
		}
		//	Model Validator
		String errorMsg = ModelValidationEngine.get().fireModelChange
		(po, newRecord ? ModelValidator.CHANGETYPE_NEW : ModelValidator.CHANGETYPE_CHANGE);
		if (errorMsg != null)
		{
			log.warning("Validation failed - " + errorMsg);
			log.saveError("Error", errorMsg);
			return false;
		}

		return true;
	}


	/**************************************************************************
	 *  Update Value or create new record.
	 * 	To reload call load() - not updated
	 *  @return true if saved
	 */
	public static boolean saveAll(Trx trx, List<? extends PO> p_objects)
	{
		CLogger.resetLast();

		// only process changed objects
		List<PO> objects = new ArrayList<PO>(p_objects.size());
		for (PO po : p_objects) {
			if (po.is_Changed())
				objects.add(po);
		}
		p_objects = null;


		for (PO po : objects) {
			if(!validateBeforeSave(po))
				return false;
		}


		/** map of SQL statements to sets of PO objects */
		HashMap<String, HashSet<PO>> sqls = new HashMap<String, HashSet<PO>>();
		/** map of PO objects to QueryParams */
		HashMap<PO, QueryParams> queries = new HashMap<PO, QueryParams>(objects.size());
		for (PO po : objects) {
			QueryParams param = po.is_new() ? po.getSaveNewQueryInfo() : po.getSaveUpdateQueryInfo();
			queries.put(po, param);
			HashSet<PO> pos = sqls.get(param.sql);
			if (pos == null) {
				pos = new HashSet<PO>();
				sqls.put(param.sql, pos);
			}
			pos.add(po);
		}

		for (Map.Entry<String, HashSet<PO>> sql : sqls.entrySet()) {
			log.fine("Bulk update " + sql.getValue().size() + " records for: " + sql.getKey());
			List<List<Object>> bulkParams = new ArrayList<List<Object>>();
			for (PO po : sql.getValue()) {
				bulkParams.add(queries.get(po).params);
			}
			int no = DB.executeBulkUpdate(trx, sql.getKey(), bulkParams);
			if( no != bulkParams.size())
				return false;
		}


		boolean allOK = true;
		for (PO po : objects) {
			boolean ok = true;

			boolean isNew = po.is_new();
			if (queries.get(po) != null) {
				ok &= po.lobSave();
				if(isNew)
					ok &= po.load(trx);
				ok &= po.saveFinish(isNew, ok);
			} else {
				// if new record, null params is failure;
				// if update, null params is success
				ok = po.saveFinish(isNew, !isNew);
			}
			allOK &= ok;
		}

		return allOK;
	}	//	save



	/**
	 * 	Finish Save Process
	 *	@param newRecord new
	 *	@param success success
	 *	@return true if saved
	 */
	private boolean saveFinish (boolean newRecord, boolean success)
	{
		//	Translations
		if (success)
		{
			if (newRecord)
			{
				insertTranslations();
				insertSubTable();
			}
			else
			{
				updateTranslations();
				updateSubTable();
			}
			updatePreferences();
		}
		//
		try
		{
			success = afterSave (newRecord, success);
			if (success && newRecord)
				insertTreeNode();
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, "afterSave", e);
			log.saveError("Error", e.toString(), false);
			success = false;
			//	throw new DBException(e);
		}
		//	OK
		if (success)
		{
			if (s_docWFMgr == null)
			{
				try
				{
					Class.forName("org.compiere.wf.DocWorkflowManager");
				}
				catch (Exception e)
				{
				}
			}
			if (s_docWFMgr != null)
				s_docWFMgr.process (this, p_info.getAD_Table_ID());

			//	Copy to Old values
			int size = p_info.getColumnCount();
			for (int i = 0; i < size; i++)
			{
				if (m_newValues[i] != null)
				{
					if (m_newValues[i] == Null.NULL)
						m_oldValues[i] = null;
					else
						m_oldValues[i] = m_newValues[i];
				}
			}
			m_newValues = new Object[size];
		}
		m_createNew = false;
		if (!newRecord) {
			CacheMgt.get().reset(p_info.getTableName());
		}
		return success;
	}	//	saveFinish

	/**
	 *  Update Value or create new record.
	 * 	To reload call load() - not updated
	 *	@param trx transaction
	 *  @return true if saved
	 */
	public boolean save (Trx trx)
	{
		set_Trx(trx);
		return save();
	}	//	save

	/**
	 * 	Is there a Change to be saved?
	 *	@return true if record changed
	 */
	public boolean is_Changed()
	{
		int size = get_ColumnCount();
		for (int i = 0; i < size; i++)
		{
			if (m_newValues[i] != null)
				return true;	//	something changed
		}
		return false;
	}	//	is_Change

	/**
	 * 	Called before Save for Pre-Save Operation
	 * 	@param newRecord new record
	 *	@return true if record can be saved
	 */
	protected boolean beforeSave(boolean newRecord)
	{
		/** Prevents saving
		log.saveError("Error", Msg.parseTranslation(getCtx(), "@C_Currency_ID@ = @C_Currency_ID@"));
		log.saveError("FillMandatory", Msg.getElement(getCtx(), "PriceEntered"));
		/** Issues message
		log.saveWarning(AD_Message, message);
		log.saveInfo (AD_Message, message);
		 **/
		return true;
	}	//	beforeSave

	/**
	 * 	Called after Save for Post-Save Operation
	 * 	@param newRecord new record
	 *	@param success true if save operation was success
	 *	@return if save was a success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		return success;
	}	//	afterSave

	/**
	 * 	Update Record directly
	 * 	@return true if updated
	 */
	protected QueryParams getSaveUpdateQueryInfo()
	{
		//
		boolean changes = false;
		StringBuilder sql = new StringBuilder ("UPDATE ");
		sql.append(p_info.getTableName()).append( " SET ");
		boolean updated = false;
		boolean updatedBy = false;
		lobReset();

		//	Change Log
		MSession session = null;
		if (!(this instanceof MSession))
			session = MSession.get (m_ctx);
		int AD_ChangeLog_ID = 0;
		boolean logThis = session != null;
		if (logThis)
			logThis = session.isLogged(p_info.getAD_Table_ID(),
					get_TableName(), X_AD_ChangeLog.CHANGELOGTYPE_Update);

		ArrayList<Object> params = new ArrayList<Object>();

		int size = get_ColumnCount();
		for (int i = 0; i < size; i++)
		{
			Object value = m_newValues[i];
			if (value == null
					|| p_info.isVirtualColumn(i))
				continue;
			//  we have a change
			Class<?> c = p_info.getColumnClass(i);
			int dt = p_info.getColumnDisplayType(i);
			String columnName = p_info.getColumnName(i);
			//
			//	updated/by
			if (columnName.equals("UpdatedBy"))
			{
				if (updatedBy)	//	explicit
					continue;
				updatedBy = true;
			}
			else if (columnName.equals("Updated"))
			{
				if (updated)
					continue;
				updated = true;
			}
			if (FieldType.isLOB(dt))
			{
				lobAdd (value, i, dt);
				//	If no changes set UpdatedBy explicitly to ensure commit of lob
				if (!changes & !updatedBy)
				{
					int AD_User_ID = m_ctx.getAD_User_ID();
					set_ValueNoCheck("UpdatedBy", Integer.valueOf(AD_User_ID));
					sql.append("UpdatedBy=?");
					params.add(AD_User_ID);
					changes = true;
					updatedBy = true;
				}
				continue;
			}
			//	Update Document No
			if (columnName.equals("DocumentNo") && !p_info.getTableName().startsWith("I_"))
			{
				String strValue = (String)value;
				if (strValue.startsWith("<") && strValue.endsWith(">"))
				{
					value = null;
					int AD_Client_ID = getAD_Client_ID();
					int index = p_info.getColumnIndex("C_DocTypeTarget_ID");
					if (index == -1)
						index = p_info.getColumnIndex("C_DocType_ID");
					if (index != -1)		//	get based on Doc Type (might return null)
						value = DB.getDocumentNo(get_ValueAsInt(index), m_trx);
					if (value == null)	//	not overwritten by DocType and not manually entered
						value = DB.getDocumentNo(AD_Client_ID, p_info.getTableName(), m_trx);
				}
				else if (!Util.isEqual(m_oldValues[i], value))
					log.info("DocumentNo updated: " + m_oldValues[i] + " -> " + value);
			}

			if (changes)
				sql.append(", ");
			changes = true;
			sql.append(columnName).append("=");

			//  values
			if (value == Null.NULL)
			{
				// sql.append("NULL");
				// sqlValues.append ("NULL");
				sql.append('?');

				int sqlType = Types.VARCHAR;
				if( Number.class.isAssignableFrom(c) )
					sqlType = Types.NUMERIC;
				else if( Date.class.isAssignableFrom(c) )
					sqlType = Types.TIMESTAMP;
				params.add(new NullParameter(sqlType));
			}
			else if (value instanceof Integer || value instanceof BigDecimal)
			{
				// for Quantity columns, use the incremental form of
				// UPDATE SET Qty=Qty+? to avoid lost quantity updates due to
				// concurrent access
				if (dt == DisplayTypeConstants.Quantity) {
					if( value instanceof BigDecimal ){
						BigDecimal oldValue = (BigDecimal) get_ValueOld(i);
						if( oldValue == null )
							oldValue = BigDecimal.ZERO;
						BigDecimal diff = ((BigDecimal) value).subtract(oldValue);
						sql.append(columnName).append("+?");
						params.add(diff);
					} else {
						Integer oldValue = (Integer) get_ValueOld(i);
						if( oldValue == null )
							oldValue = Integer.valueOf(0);
						Integer diff = ((Integer) value) - oldValue;
						sql.append(columnName).append("+?");
						params.add(diff);
					}
				}
				else {
					// sql.append(encrypt(i,value));
					sql.append('?');
					params.add(encrypt(i, value));
				}
			}
			else if (c == Boolean.class)
			{
				boolean bValue = false;
				if (value instanceof Boolean)
					bValue = ((Boolean)value).booleanValue();
				else
					bValue = "Y".equals(value);
				// sql.append(encrypt(i,bValue ? "'Y'" : "'N'"));
				sql.append('?');
				params.add(encrypt(i, bValue ? "Y" : "N"));
			}
			else if (value instanceof Timestamp)
			{
				// sql.append(DB.TO_DATE((Timestamp)encrypt(i,value),p_info.getColumnDisplayType(i) == DisplayTypeConstants.Date));
				if (p_info.getColumnDisplayType(i) == DisplayTypeConstants.Date) {
					//If condition added to store time component for the end date in C_Period table 
					if(!get_TableName().equals("C_Period"))
						value = truncDate((Timestamp) value);
				}
				sql.append('?');
				params.add(encrypt(i, value));
			}
			else
			{
				// sql.append(encrypt(i,DB.TO_STRING(value.toString())));
				sql.append('?');
				params.add(encrypt(i, value.toString()));
			}

			//	Change Log	- Only
			if (session != null && logThis
					&& !p_info.isEncrypted(i)		//	not encrypted
					&& !p_info.isVirtualColumn(i)	//	no virtual column
					&& !"Password".equals(columnName)
					&& !"CreditCardNumber".equals(columnName)
			)
			{
				Object oldV = m_oldValues[i];
				Object newV = value;
				if (oldV != null && oldV == Null.NULL)
					oldV = null;
				if (newV != null && newV == Null.NULL)
					newV = null;
				//
				Object keyInfo = get_ID();
				if (m_IDs.length != 1)
					keyInfo = get_WhereClause(true);
				MChangeLog cLog = session.changeLog (
						m_trx, AD_ChangeLog_ID,
						p_info.getAD_Table_ID(), p_info.getColumn(i).AD_Column_ID,
						keyInfo, getAD_Client_ID(), getAD_Org_ID(), oldV, newV,
						get_TableName(), X_AD_ChangeLog.CHANGELOGTYPE_Update);
				if (cLog != null)
					AD_ChangeLog_ID = cLog.getAD_ChangeLog_ID();
			}
		}	//   for all fields

		//	Custom Columns (cannot be logged as no column)
		if (m_custom != null)
		{
			Iterator<String> it = m_custom.keySet().iterator();
			while (it.hasNext())
			{
				if (changes)
					sql.append(", ");
				changes = true;
				//
				String columnName = it.next();
				int index = p_info.getColumnIndex(columnName);
				String value = m_custom.get(columnName);
				// sql.append(columnName).append("=").append(value);	//	no encryption
				sql.append('?');
				if (value != null && value.equals(Null.NULLString))
				{
					params.add(new NullParameter(Types.VARCHAR));
				}
				else
				{
					params.add(encrypt(index, value));
				}


				//	Change Log
				if (session != null
						&& !p_info.isEncrypted(index)		//	not encrypted
						&& !p_info.isVirtualColumn(index)	//	no virtual column
						&& !"Password".equals(columnName)
						&& !"CreditCardNumber".equals(columnName)
				)
				{
					Object oldV = m_oldValues[index];
					String newV = value;
					if (oldV != null && oldV == Null.NULL)
						oldV = null;
					//
					MChangeLog cLog = session.changeLog (
							m_trx, AD_ChangeLog_ID,
							p_info.getAD_Table_ID(), p_info.getColumn(index).AD_Column_ID,
							m_IDs, getAD_Client_ID(), getAD_Org_ID(), oldV, newV,
							get_TableName(), X_AD_ChangeLog.CHANGELOGTYPE_Update);
					if (cLog != null)
						AD_ChangeLog_ID = cLog.getAD_ChangeLog_ID();
				}
			}
			m_custom = null;
		}

		//	Something changed
		if (changes)
		{
			if (!updated)	//	Updated not explicitly set
			{
				Timestamp now = new Timestamp(System.currentTimeMillis());
				set_ValueNoCheck("Updated", now);
				sql.append(",Updated=?");
				params.add(now);
			}
			if (!updatedBy)	//	UpdatedBy not explicitly set
			{
				int AD_User_ID = m_ctx.getAD_User_ID();
				set_ValueNoCheck("UpdatedBy", Integer.valueOf(AD_User_ID));
				sql.append(",UpdatedBy=?");
				params.add(AD_User_ID);
			}
			sql.append(" WHERE ").append(get_WhereClause(false));
			params.addAll(get_WhereClauseParams());
			//
			log.finest(sql.toString());
			return new QueryParams(sql.toString(), params.toArray());
		}

		//  nothing changed, so OK
		return null;
	}   //  saveUpdate


	/**
	 * Update Record directly
	 * 
	 * @return true if updated
	 */
	protected boolean saveUpdate() {
		QueryParams params = getSaveUpdateQueryInfo();
		if (params != null) {
			int no = DB.executeUpdate(m_trx, params.sql, params.params);
			boolean ok = no == 1;
			if (ok) {
				ok = lobSave();
			} else {
				log.log(Level.WARNING, "#" + no + " - [" + m_trx + "] - " + p_info.getTableName() + "." + get_WhereClause(true));
			}
			return saveFinish(false, ok);
		} else {
			//  nothing changed, so OK
			return saveFinish(false, true);
		}
	}	

	/**
	 *  Create New Record
	 *  @return true if new record inserted
	 */
	private QueryParams getSaveNewQueryInfo()
	{
		String[] keyColumns = p_info.getKeyColumns();
		//  Set ID for single key - Multi-Key values need explicitly be set previously
		if (m_IDs.length == 1 && p_info.hasKeyColumn()
				&& keyColumns[0].toUpperCase().endsWith("_ID"))	//	AD_Language, EntityType
		{
			int no = saveNew_getID();
			if (no <= 0)
				no = DB.getNextID(getAD_Client_ID(), p_info.getTableName(), m_trx);
			if (no <= 0)
			{
				log.severe("No NextID (" + no + ")");
				return null;
			}
			m_IDs[0] = Integer.valueOf(no);
			set_ValueNoCheck(keyColumns[0], m_IDs[0]);
		}

		//	Set new DocumentNo for non-import tables
		String columnName = "DocumentNo";
		int index = p_info.getColumnIndex(columnName);
		if (index != -1 && !p_info.getTableName().startsWith("I_"))
		{
			String value = (String)get_Value(index);
			if (value != null && value.startsWith("<") && value.endsWith(">"))
				value = null;
			if (value == null || value.length() == 0)
			{
				int dtIndex = p_info.getColumnIndex("C_DocTypeTarget_ID");
				if (dtIndex == -1)
					dtIndex = p_info.getColumnIndex("C_DocType_ID");
				if (dtIndex != -1)		//	get based on Doc Type (might return null)
					value = DB.getDocumentNo(get_ValueAsInt(dtIndex), m_trx);
				if (value == null)	//	not overwritten by DocType and not manually entered
					value = DB.getDocumentNo(getAD_Client_ID(), p_info.getTableName(), m_trx);
				set_ValueNoCheck(columnName, value);
			}
		}
		//	Set empty Value
		columnName = "Value";
		index = p_info.getColumnIndex(columnName);
		if (index != -1)
		{
			String value = (String)get_Value(index);
			if (value == null || value.length() == 0)
			{
				// If import table, get the underlying data table to get the "Value"
				String tableName = p_info.getTableName(); 
				if(p_info.getTableName().startsWith("I_"))
					tableName = getTable();
				
				value = DB.getDocumentNo (getAD_Client_ID(), tableName, m_trx);
				set_ValueNoCheck(columnName, value);
			}
		}

		lobReset();
		//	Change Log
		MSession session = null;
		if (!(this instanceof MSession))	//	initial session
			session = MSession.get (m_ctx);
		int AD_ChangeLog_ID = 0;
		boolean logThis = session != null;
		if (logThis)
			logThis = session.isLogged(p_info.getAD_Table_ID(),
					get_TableName(), X_AD_ChangeLog.CHANGELOGTYPE_Insert);

		//	SQL
		StringBuilder sqlInsert = new StringBuilder("INSERT INTO ");
		sqlInsert.append(p_info.getTableName()).append(" (");
		StringBuilder sqlValues = new StringBuilder(") VALUES (");

		ArrayList<Object> params = new ArrayList<Object>();

		int size = get_ColumnCount();
		boolean doComma = false;
		for (int i = 0; i < size; i++)
		{
			String columnName1 = p_info.getColumnName(i);
			if (p_info.isVirtualColumn(i))
				continue;
			Object value = get_Value(i);
			//	Don't insert NULL values (allows Database defaults)
			if (value == null || value == Null.NULL)
				continue;

			//	Display Type
			int dt = p_info.getColumnDisplayType(i);
			if (FieldType.isLOB(dt))
			{
				lobAdd (value, i, dt);
				continue;
			}

			//	** add column **
			if (doComma)
			{
				sqlInsert.append(",");
				sqlValues.append(",");
			}
			else
				doComma = true;
			sqlInsert.append(columnName1);
			//
			//  Based on class of definition, not class of value
			Class<?> c = p_info.getColumnClass(i);
			try
			{
				if (c == Object.class) // may have need to deal with null
					// values differently
				{
					sqlValues.append(saveNewSpecial(value, i));
				}
				else if (value == null || value.equals (Null.NULL)) {
					// sqlValues.append ("NULL");
					sqlValues.append('?');

					int sqlType = Types.VARCHAR;
					if( Number.class.isAssignableFrom(c) )
						sqlType = Types.NUMERIC;
					else if( Date.class.isAssignableFrom(c) )
						sqlType = Types.TIMESTAMP;
					params.add(new NullParameter(sqlType));
				}
				else if (value instanceof Integer || value instanceof BigDecimal) {
					// sqlValues.append (encrypt(i,value));
					sqlValues.append('?');
					params.add(encrypt(i, value));
				}
				else if (c == Boolean.class)
				{
					boolean bValue = false;
					if (value instanceof Boolean)
						bValue = ((Boolean)value).booleanValue();
					else
						bValue = "Y".equals(value);
					// sqlValues.append (encrypt(i,bValue ? "'Y'" : "'N'"));
					sqlValues.append('?');
					params.add(encrypt(i, bValue ? "Y" : "N"));
				}
				else if (value instanceof Boolean) {
					// sqlValues.append (encrypt(i,(Boolean)value ? "'Y'" : "'N'"));
					sqlValues.append('?');
					params.add(encrypt(i, (Boolean) value ? "Y" : "N"));
				}
				else if (value instanceof Timestamp) {
					// sqlValues.append (DB.TO_DATE ((Timestamp)encrypt(i,value), p_info.getColumnDisplayType (i) == DisplayTypeConstants.Date));
					if (p_info.getColumnDisplayType(i) == DisplayTypeConstants.Date) {
						//If condition added to store time component for the end date in C_Period table 
						if(!get_TableName().equals("C_Period"))
							value = truncDate((Timestamp) value);
					}
					sqlValues.append('?');
					params.add(encrypt(i, value));
				}
				else if (c == String.class) {
					// sqlValues.append (encrypt(i,DB.TO_STRING ((String)value)));
					sqlValues.append('?');
					params.add(encrypt(i, value));
				}
				else if (FieldType.isLOB(dt)) {
					sqlValues.append("NULL");		//	no db dependent stuff here
				}
				else {
					sqlValues.append (saveNewSpecial (value, i));
				}
			}
			catch (Exception e)
			{
				String msg = "";
				if (m_trx != null)
					msg = "[" + m_trx + "] - ";
				msg += p_info.toString(i)
				+ " - Value=" + value
				+ "(" + (value==null ? "null" : value.getClass().getName()) + ")";
				log.log(Level.SEVERE, msg, e);
				throw new DBException(e);	//	fini
			}

			//	Change Log
			if (session != null && logThis
					&& !p_info.isEncrypted(i)		//	not encrypted
					&& !p_info.isVirtualColumn(i)	//	no virtual column
					&& !"Password".equals(columnName1)
					&& !"CreditCardNumber".equals(columnName1)
			)
			{
				Object keyInfo = get_ID();
				if (m_IDs.length != 1)
					keyInfo = get_WhereClause(true);
				MChangeLog cLog = session.changeLog (
						m_trx, AD_ChangeLog_ID,
						p_info.getAD_Table_ID(), p_info.getColumn(i).AD_Column_ID,
						keyInfo, getAD_Client_ID(), getAD_Org_ID(), null, value,
						get_TableName(), X_AD_ChangeLog.CHANGELOGTYPE_Insert);
				if (cLog != null)
					AD_ChangeLog_ID = cLog.getAD_ChangeLog_ID();
			}
		}
		//	Custom Columns
		int index1;
		if (m_custom != null)
		{
			Iterator<String> it = m_custom.keySet().iterator();
			while (it.hasNext())
			{
				String columnName2 = it.next();
				index1 = p_info.getColumnIndex(columnName2);
				String value = m_custom.get(columnName2);
				if (value == null || value.equals(Null.NULLString))
					continue;
				if (doComma)
				{
					sqlInsert.append(",");
					sqlValues.append(",");
				}
				else
					doComma = true;
				sqlInsert.append(columnName2);
				// String value2 = DB.TO_STRING(encrypt(index, value));
				// sqlValues.append(value2);
				sqlValues.append('?');
				params.add(encrypt(index1, value));

				//	Change Log
				if (session != null
						&& !p_info.isEncrypted(index1)		//	not encrypted
						&& !p_info.isVirtualColumn(index1)	//	no virtual column
						&& !"Password".equals(columnName2)
						&& !"CreditCardNumber".equals(columnName2)
				)
				{
					MChangeLog cLog = session.changeLog (
							m_trx, AD_ChangeLog_ID,
							p_info.getAD_Table_ID(), p_info.getColumn(index1).AD_Column_ID,
							m_IDs, getAD_Client_ID(), getAD_Org_ID(), null, value,
							get_TableName(), X_AD_ChangeLog.CHANGELOGTYPE_Insert);
					if (cLog != null)
						AD_ChangeLog_ID = cLog.getAD_ChangeLog_ID();
				}
			}
			m_custom = null;
		}
		sqlInsert.append(sqlValues).append(")");
		//
		return new QueryParams(sqlInsert.toString(), params.toArray() );
	}   //  saveNew


	/**
	 * Create New Record
	 * 
	 * @return true if new record inserted
	 */
	private boolean saveNew() {
		QueryParams params = getSaveNewQueryInfo();
		if (params != null) {
			int no = DB.executeUpdate(m_trx, params.sql, params.params);
			boolean ok = no == 1;
			if (ok) {
				ok = lobSave();
				if (!load(m_trx)) // re-read Info
				{
					log.log(Level.SEVERE, "[" + m_trx + "] - reloading");
					ok = false;
				}
			} else {
				log.log(Level.WARNING, "[" + m_trx + "]" + "Not inserted - " + params.sql);
			}

			if (ok)
				return saveFinish(true, ok);

			return ok;
		} else {
			// could not generate new unique ID, so failure
			return saveFinish(true, false);
		}
	}	

	private static Timestamp truncDate(Timestamp value) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(value);

		// due to difference in DST detection for dates prior to 1970 
		// between client/GWT and server/Java, 
		// prevent changing the date to 1 day earlier
		if (cal.get(Calendar.YEAR) <= 1970 &&
				cal.get(Calendar.HOUR_OF_DAY) >= 23) {
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
			
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Timestamp(cal.getTimeInMillis());
	}

	/**
	 * 	Get ID for new record during save.
	 * 	You can overwrite this to explicitly set the ID
	 *	@return ID to be used or 0 for default logic
	 */
	protected int saveNew_getID()
	{
		return 0;
	}	//	saveNew_getID


	/**
	 * 	Create Single/Multi Key Where Clause
	 * 	@param withValues if true uses actual values otherwise ?
	 * 	@return where clause
	 */
	public String get_WhereClause (boolean withValues) {
		if (!withValues) {
			return p_info.getWhereClause();
		} else {
			String[] keyColumns = p_info.getKeyColumns();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < m_IDs.length; i++)
			{
				if (i != 0)
					sb.append(" AND ");
				sb.append(keyColumns[i]).append("=");
				if (withValues)
				{
					if (keyColumns[i].toUpperCase().endsWith("_ID"))
						sb.append(m_IDs[i]);
					else
						sb.append("'").append(m_IDs[i]).append("'");
				}
				else
					sb.append("?");
			}
			return sb.toString();
		}
	}	//	getWhereClause

	public ArrayList<Object> get_WhereClauseParams() {
		ArrayList<Object> params = new ArrayList<Object>();
		for (Object mID : m_IDs) {
			if (mID instanceof Integer)
				params.add(mID);
			else
				params.add(mID.toString());
		}
		return params;
	}
	/**
	 *  Save Special Data.
	 *  To be extended by sub-classes
	 *  @param value value
	 *  @param index index
	 *  @return SQL code for INSERT VALUES clause
	 */
	protected String saveNewSpecial (Object value, int index)
	{
		String colName = p_info.getColumnName(index);
		String colClass = p_info.getColumnClass(index).toString();
		String colValue = value == null ? "null" : value.getClass().toString();
		//	int dt = p_info.getColumnDisplayType(index);

		log.log(Level.SEVERE, "Unknown class for column " + colName
				+ " (" + colClass + ") - Value=" + colValue);

		if (value == null)
			return "NULL";
		return value.toString();
	}   //  saveNewSpecial

	/**
	 * 	Encrypt data.
	 * 	Not: LOB, special values/Objects
	 *	@param index index
	 *	@param xx data
	 *	@return xx
	 */
	private Object encrypt (int index, Object xx)
	{
		if (xx == null)
			return null;
		if (index != -1 && p_info.isEncrypted(index))
			return SecureEngine.encrypt(xx);
		return xx;
	}	//	encrypt

	/**
	 * 	Encrypt data.
	 * 	Not: LOB, special values/Objects
	 *	@param index index
	 *	@param xx data
	 *	@return xx
	 */
	private String encrypt (int index, String xx)
	{
		if (xx == null)
			return null;
		if (index != -1 && p_info.isEncrypted(index))
			return SecureEngine.encrypt(xx);
		return xx;
	}	//	encrypt

	/**
	 * 	Decrypt data
	 *	@param index index
	 *	@param yy data
	 *	@return yy
	 */
	private Object decrypt (int index, Object yy)
	{
		if (yy == null)
			return null;
		if (index != -1 && p_info.isEncrypted(index))
			return SecureEngine.decrypt(yy);
		return yy;
	}	//	decrypt

	private Boolean CheckActiveWF(int AD_Table_ID, int Record_ID, Trx trx)
	{
		StringBuffer sql = new StringBuffer ("SELECT COUNT(*) FROM AD_WF_ACTIVITY " +
		"WHERE AD_Table_ID=? AND Record_ID=? AND PROCESSED='N'");		
		int no = QueryUtil.getSQLValue(trx, sql.toString(), AD_Table_ID, Record_ID);
		if (no > 0)
			return true;
		else
			return false;
	}

	/**************************************************************************
	 * 	Delete Current Record
	 * 	@param force delete also processed records
	 * 	@return true if deleted
	 */
	public boolean delete (boolean force)
	{
		CLogger.resetLast();
		if (is_new())
			return true;

		if (getCtx().getAD_Client_ID() < 0)
			throw new IllegalArgumentException ("R/O Context - no new instances allowed");

		int AD_Table_ID = p_info.getAD_Table_ID();
		int Record_ID = get_ID();

		if (!force)
		{
			int iProcessed = get_ColumnIndex("Processed");
			if  (iProcessed != -1)
			{
				Boolean processed = (Boolean)get_Value(iProcessed);
				if (processed != null && processed.booleanValue())
				{
					log.warning("Record processed");	//	CannotDeleteTrx
					log.saveError("Processed", "Processed", false);
					return false;
				}
			}	//	processed
		}	//	force

		if (CheckActiveWF(AD_Table_ID, Record_ID, m_trx))
		{
			log.warning("Active Workflow Exists");	//	CannotDeleteTrx
			log.saveError("ActiveWF", "ActiveWF Exists", false);
			return false;
		}


		try
		{
			if (!beforeDelete())
			{
				log.warning("beforeDelete failed");

				// the subclasses of PO that return false in beforeDelete()
				// should have already called CLogger.saveError()
				if( !CLogger.hasError() )
					log.saveError("Error", "beforeDelete failed", false);
				return false;
			}
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, "beforeDelete", e);
			log.saveError("Error", e.toString(), false);
			//	throw new DBException(e);
			return false;
		}
		//	Delete Restrict AD_Table_ID/Record_ID (Requests, ..)
		String errorMsg = PO_Record.exists(AD_Table_ID, Record_ID, m_trx);
		if (errorMsg != null)
		{
			log.saveError("CannotDelete", errorMsg);
			return false;
		}
		//
		errorMsg = ModelValidationEngine.get().fireModelChange
		(this, ModelValidator.CHANGETYPE_DELETE);
		if (errorMsg != null)
		{
			log.saveError("Error", errorMsg);
			return false;
		}

		Trx localTrx =  null;
		Trx localTrxName = m_trx;
		if (localTrxName == null)
		{
			localTrxName = Trx.get("POdel");
			localTrx = localTrxName;
		}
		//
		deleteTranslations(localTrxName);
		deleteSubTable(localTrxName);
		//	Delete Cascade AD_Table_ID/Record_ID (Attachments, ..)
		PO_Record.deleteCascade(AD_Table_ID, Record_ID, localTrxName);

		//	The Delete Statement
		StringBuilder sql = new StringBuilder ("DELETE FROM ")
		.append(p_info.getTableName())
		.append(" WHERE ")
		.append(get_WhereClause(false));
		int no = DB.executeUpdate(localTrxName, sql.toString(), get_WhereClauseParams());
		boolean success = no == 1;

		//	Save ID
		m_idOld = get_ID();
		//
		if (!success)
		{
			log.warning("Not deleted");
			if (localTrx != null)
				localTrx.rollback();
		}
		else
		{
			if (localTrx != null)
				localTrx.commit();
			//	Change Log
			MSession session = MSession.get (m_ctx);
			if (session == null)
				log.fine("No Session found");
			else if (session.isWebStoreSession()
					|| MChangeLog.isLogged(AD_Table_ID, X_AD_ChangeLog.CHANGELOGTYPE_Delete))
			{
				int AD_ChangeLog_ID = 0;
				int size = get_ColumnCount();
				for (int i = 0; i < size; i++)
				{
					Object value = m_oldValues[i];
					if (value != null
							&& !p_info.isEncrypted(i)		//	not encrypted
							&& !p_info.isVirtualColumn(i)	//	no virtual column
							&& !"Password".equals(p_info.getColumnName(i))
							&& !"CreditCardNumber".equals(p_info.getColumnName(i))
					)
					{
						Object keyInfo = get_ID();
						if (m_IDs.length != 1)
							keyInfo = get_WhereClause(true);
						MChangeLog cLog = session.changeLog (
								m_trx, AD_ChangeLog_ID,
								AD_Table_ID, p_info.getColumn(i).AD_Column_ID,
								keyInfo, getAD_Client_ID(), getAD_Org_ID(), value, null,
								get_TableName(), X_AD_ChangeLog.CHANGELOGTYPE_Delete);
						if (cLog != null)
							AD_ChangeLog_ID = cLog.getAD_ChangeLog_ID();
					}
				}	//   for all fields
			}

			//	Housekeeping
			m_IDs[0] = I_ZERO;
			if (m_trx == null)
				log.fine("complete");
			else
				log.fine("[" + m_trx + "] - complete");
			m_attachment = null;
		}
		if (localTrx != null)
			localTrx.close();
		localTrx = null;
		updatePreferences();

		try
		{
			success = afterDelete (success);
			if (success)
				deleteTreeNode();
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, "afterDelete", e);
			log.saveError("Error", e.toString(), false);
			success = false;
			//	throw new DBException(e);
		}

		//	Reset
		if (success)
		{
			m_idOld = 0;
			int size = p_info.getColumnCount();
			m_oldValues = new Object[size];
			m_newValues = new Object[size];
			CacheMgt.get().reset(p_info.getTableName());
		}
		//	log.info("" + success);
		return success;
	}	//	delete

	/**
	 * 	Delete Current Record
	 * 	@param force delete also processed records
	 *	@param trx transaction
	 *	@return true if deleted
	 */
	public boolean delete (boolean force, Trx trx)
	{
		set_Trx(trx);
		return delete (force);
	}	//	delete

	/**
	 * 	Executed before Delete operation.
	 *	@return true if record can be deleted
	 */
	protected boolean beforeDelete ()
	{
		//	log.saveError("Error", Msg.getMsg(getCtx(), "CannotDelete"));
		return true;
	} 	//	beforeDelete

	/**
	 * 	Executed after Delete operation.
	 * 	@param success true if record deleted
	 *	@return true if delete is a success
	 */
	protected boolean afterDelete (boolean success)
	{
		return success;
	} 	//	afterDelete


	/**
	 * 	Insert (missing) Translation Records
	 * 	@return false if error (true if no translation or success)
	 */
	private boolean insertTranslations()
	{
		//	Not a translation table
		if (m_IDs.length > 1
				|| m_IDs[0].equals(I_ZERO)
				|| !p_info.isTranslated()
				|| !(m_IDs[0] instanceof Integer))
			return true;
		//
		StringBuilder iColumns = new StringBuilder();
		StringBuilder sColumns = new StringBuilder();
		for (int i = 0; i < p_info.getColumnCount(); i++)
		{
			if (p_info.isColumnTranslated(i))
			{
				iColumns.append(p_info.getColumnName(i))
				.append(",");
				sColumns.append("t.")
				.append(p_info.getColumnName(i))
				.append(",");
			}
		}
		if (iColumns.length() == 0)
			return true;

		String tableName = p_info.getTableName();
		String keyColumn = p_info.getKeyColumns()[0];
		StringBuilder sql = new StringBuilder ("INSERT INTO ")
		.append(tableName).append("_Trl (AD_Language,")
		.append(keyColumn).append(", ")
		.append(iColumns)
		.append(" IsTranslated,AD_Client_ID,AD_Org_ID,Created,CreatedBy,Updated,UpdatedBy) ")
		.append("SELECT l.AD_Language,t.")
		.append(keyColumn).append(", ")
		.append(sColumns)
		.append(" 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.CreatedBy,t.Updated,t.UpdatedBy ")
		.append("FROM AD_Language l, ").append(tableName).append(" t ")
		.append("WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.")
		.append(keyColumn).append("=?")
		.append(" AND NOT EXISTS (SELECT * FROM ").append(tableName)
		.append("_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.")
		.append(keyColumn).append("=t.").append(keyColumn).append(")");
		/* 	Alternative *
			.append(" AND EXISTS (SELECT * FROM ").append(tableName)
			.append("_Trl tt WHERE tt.AD_Language!=l.AD_Language OR tt.")
			.append(keyColumn).append("!=t.").append(keyColumn).append(")");
			/** */
		int no = DB.executeUpdate(m_trx, sql.toString(),new Object[]{get_ID()});
		log.fine("#" + no);
		return no > 0;
	}	//	insertTranslations

	/**
	 * 	Update Translations.
	 * 	@return false if error (true if no translation or success)
	 */
	private boolean updateTranslations()
	{
		//	Not a translation table
		if (m_IDs.length > 1
				|| m_IDs[0].equals(I_ZERO)
				|| !p_info.isTranslated()
				|| !(m_IDs[0] instanceof Integer))
			return true;
		//
		boolean trlColumnChanged = false;
		for (int i = 0; i < p_info.getColumnCount(); i++)
		{
			if (p_info.isColumnTranslated(i)
					&& is_ValueChanged(p_info.getColumnName(i)))
			{
				trlColumnChanged = true;
				break;
			}
		}
		if (!trlColumnChanged)
			return true;
		//
		MClient client = MClient.get(getCtx());
		//
		String tableName = p_info.getTableName();
		String keyColumn = p_info.getKeyColumns()[0];
		StringBuilder sql = new StringBuilder ("UPDATE ")
		.append(tableName).append("_Trl SET ");
		//
		if (client.isAutoUpdateTrl(tableName))
		{
			for (int i = 0; i < p_info.getColumnCount(); i++)
			{
				if (p_info.isColumnTranslated(i))
				{
					String columnName = p_info.getColumnName(i);
					sql.append(columnName).append("=");
					Object value = get_Value(columnName);
					if (value == null)
						sql.append("NULL");
					else if (value instanceof String)
						sql.append(DB.TO_STRING((String)value));
					else if (value instanceof Boolean)
						sql.append(((Boolean)value).booleanValue() ? "'Y'" : "'N'");
					else if (value instanceof Timestamp)
						sql.append(DB.TO_DATE((Timestamp)value));
					else
						sql.append(value.toString());
					sql.append(",");
				}
			}
			sql.append("IsTranslated='Y'");
		}
		else
			sql.append("IsTranslated='N'");
		//
		sql.append(" WHERE ")
		.append(keyColumn).append("=").append(get_ID());
		int no = DB.executeUpdate(m_trx, sql.toString());
		log.fine("#" + no);
		return no >= 0;
	}	//	updateTranslations

	/**
	 * 	Delete Translation Records
	 * 	@param trx transaction
	 * 	@return false if error (true if no translation or success)
	 */
	private boolean deleteTranslations(Trx trx)
	{
		//	Not a translation table
		if (m_IDs.length > 1
				|| m_IDs[0].equals(I_ZERO)
				|| !p_info.isTranslated()
				|| !(m_IDs[0] instanceof Integer))
			return true;
		//
		String tableName = p_info.getTableName();
		String keyColumn = p_info.getKeyColumns()[0];
		StringBuilder sql = new StringBuilder ("DELETE FROM ")
		.append(tableName).append("_Trl WHERE ")
		.append(keyColumn).append("=?");
		int no = DB.executeUpdate(trx, sql.toString(), new Object[]{ get_ID()});
		log.fine("#" + no);
		return no >= 0;
	}	//	deleteTranslations


	/**************************************************************************
	 * 	Insert consequences for SubTable
	 *	@return false if error
	 */
	private boolean insertSubTable()
	{
		return true;
	}	//	insertSubTable

	/**
	 * 	Update consequences for SubTable
	 *	@return false if error
	 */
	private boolean updateSubTable()
	{
		return true;
	}	//	updateSubTable

	/**
	 * 	Delete consequences for SubTable
	 *	@param trx local transaction
	 *	@return false if error
	 */
	private boolean deleteSubTable(Trx trx)
	{
		return true;
	}	//	deleteSubTable

	/**************************************************************************
	 * 	Insert Accounting Records
	 *	@param acctTable accounting sub table
	 *	@param acctBaseTable acct table to get data from
	 *	@param whereClause optional where clause with alisa "p" for acctBaseTable
	 *	@return true if records inserted
	 */
	protected boolean insert_Accounting (String acctTable,
			String acctBaseTable, String whereClause)
	{
		if (s_acctColumns == null	//	cannot cache C_BP_*_Acct as there are 3
				|| acctTable.startsWith("C_BP_"))
		{
			s_acctColumns = new ArrayList<String>();
			String sql = "SELECT c.ColumnName "
				+ "FROM AD_Column c INNER JOIN AD_Table t ON (c.AD_Table_ID=t.AD_Table_ID) "
				+ "WHERE t.TableName=? AND c.IsActive='Y' AND c.AD_Reference_ID=25 ORDER BY 1";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, (Trx) null);
				pstmt.setString (1, acctTable);
				rs = pstmt.executeQuery ();
				while (rs.next ())
					s_acctColumns.add (rs.getString(1));
			}
			catch (Exception e)	{
				log.log(Level.SEVERE, acctTable, e);
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}

			if (s_acctColumns.size() == 0)
			{
				log.severe ("No Columns for " + acctTable);
				return false;
			}
		}

		//	Create SQL Statement - INSERT
		StringBuilder sb = new StringBuilder("INSERT INTO ")
		.append(acctTable)
		.append(" (").append(get_TableName())
		.append("_ID, C_AcctSchema_ID, AD_Client_ID,AD_Org_ID,IsActive, Created,CreatedBy,Updated,UpdatedBy ");
		for (int i = 0; i < s_acctColumns.size(); i++)
			sb.append(",").append(s_acctColumns.get(i));
		//	..	SELECT
		sb.append(") SELECT ").append(get_ID())
		.append(", p.C_AcctSchema_ID, p.AD_Client_ID,0,'Y', SysDate,")
		.append(getUpdatedBy()).append(",SysDate,").append(getUpdatedBy());
		for (int i = 0; i < s_acctColumns.size(); i++)
			sb.append(",p.").append(s_acctColumns.get(i));
		//	.. 	FROM
		sb.append(" FROM ").append(acctBaseTable)
		.append(" p WHERE p.AD_Client_ID=").append(getAD_Client_ID());
		if (whereClause != null && whereClause.length() > 0)
			sb.append (" AND ").append(whereClause);
		sb.append(" AND NOT EXISTS (SELECT * FROM ").append(acctTable)
		.append(" e WHERE e.C_AcctSchema_ID=p.C_AcctSchema_ID AND e.")
		.append(get_TableName()).append("_ID=").append(get_ID()).append(")");
		//
		int no = DB.executeUpdate(get_Trx(), sb.toString());
		if (no > 0)
			log.fine("#" + no);
		else
			log.warning("#" + no
					+ " - Table=" + acctTable + " from " + acctBaseTable);
		return no > 0;
	}	//	insert_Accounting

	/**************************************************************************
	 * 	Update Accounting Records
	 *	@param acctTable accounting sub table
	 *	@param acctBaseTable acct table to get data from
	 *	@param whereClause optional where clause with alisa "p" for acctBaseTable
	 *	@return true if records updated
	 */
	protected boolean update_Accounting (String acctTable,
			String acctBaseTable, String whereClause)
	{
		if (s_acctColumns == null	//	cannot cache C_BP_*_Acct as there are 3
				|| acctTable.startsWith("C_BP_"))
		{
			s_acctColumns = new ArrayList<String>();
			String sql = "SELECT c.ColumnName "
				+ "FROM AD_Column c INNER JOIN AD_Table t ON (c.AD_Table_ID=t.AD_Table_ID) "
				+ "WHERE t.TableName=? AND c.IsActive='Y' AND c.AD_Reference_ID=25 ORDER BY 1";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, (Trx) null);
				pstmt.setString (1, acctTable);
				rs = pstmt.executeQuery ();
				while (rs.next ())
					s_acctColumns.add (rs.getString(1));
			}
			catch (Exception e) {
				log.log(Level.SEVERE, acctTable, e);
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}

			if (s_acctColumns.size() == 0)
			{
				log.severe ("No Columns for " + acctTable);
				return false;
			}
		}

		//	Create SQL Statement - INSERT
		StringBuilder sb = new StringBuilder("UPDATE ")
		.append(acctTable).append(" acct ").append(" SET (")
		.append(" Updated,UpdatedBy ");
		for (int i = 0; i < s_acctColumns.size(); i++)
			sb.append(",").append(s_acctColumns.get(i));
		//	..	SELECT
		sb.append(") = (SELECT ")
		.append(" SysDate,").append(getUpdatedBy());
		for (int i = 0; i < s_acctColumns.size(); i++)
			sb.append(",p.").append(s_acctColumns.get(i));
		//	.. 	FROM
		sb.append(" FROM ").append(acctBaseTable)
		.append(" p WHERE p.AD_Client_ID= acct.AD_Client_ID AND p.C_AcctSchema_ID = acct.C_AcctSchema_ID");
		if (whereClause != null && whereClause.length() > 0)
			sb.append (" AND ").append(whereClause);
		sb.append(") WHERE acct.").append(get_TableName()).append("_ID = ").append(get_ID())
		.append(" AND EXISTS ( SELECT 1 FROM ").append(acctBaseTable)
		.append(" p WHERE p.C_AcctSchema_ID = acct.C_AcctSchema_ID");
		if (whereClause != null && whereClause.length() > 0)
			sb.append (" AND ").append(whereClause);		
		sb.append(")");
		//
		int no = DB.executeUpdate(get_Trx(), sb.toString());
		if (no >= 0)
			log.fine("#" + no);
		else
			log.warning("#" + no
					+ " - Table=" + acctTable + " from " + acctBaseTable);
		return no >= 0;
	}

	/**
	 * 	Delete Accounting records.
	 * 	NOP - done by database constraints
	 *	@param acctTable accounting sub table
	 *	@return true
	 */
	protected boolean delete_Accounting(String acctTable)
	{
		return true;
	}	//	delete_Accounting

	/**
	 * 	Update Preferences.
	 * 	If record is inactive or deleted
	 */
	private void updatePreferences()
	{
		if (!isActive())
		{
			String keyColumn = get_TableName() + "_ID";
			String keyValue = String.valueOf(get_ID());
			MPreference.delete(keyColumn, keyValue);
			getCtx().deletePreference(keyColumn, keyValue);
		}
	}	//	updatePreferences


	/**************************************************************************
	 * 	Insert id data into Tree
	 *	@return true if inserted
	 */
	private boolean insertTreeNode()
	{
		int AD_Table_ID = get_Table_ID();
		if (!MTree.hasTree(AD_Table_ID))
			return false;
		int id = get_ID();
		int AD_Client_ID = getAD_Client_ID();
		String treeTableName = MTree.getNodeTableName(AD_Table_ID);
		int C_Element_ID = 0;
		if (AD_Table_ID == X_C_ElementValue.Table_ID)
		{
			Integer ii = (Integer)get_Value("C_Element_ID");
			if (ii != null)
				C_Element_ID = ii.intValue();
		}
		//
		StringBuilder sb = new StringBuilder ("INSERT INTO ")
		.append(treeTableName)
		.append(" (AD_Client_ID,AD_Org_ID, IsActive,Created,CreatedBy,Updated,UpdatedBy, ")
		.append("AD_Tree_ID, Node_ID, Parent_ID, SeqNo) ")
		//
		.append("SELECT t.AD_Client_ID,0, 'Y', SysDate, 0, SysDate, 0,")
		.append("t.AD_Tree_ID, ").append(id).append(", 0, 999 ")
		.append("FROM AD_Tree t ")
		.append("WHERE t.AD_Client_ID=").append(AD_Client_ID).append(" AND t.IsActive='Y'");
		//	Account Element Value handling
		if (C_Element_ID != 0)
			sb.append(" AND EXISTS (SELECT * FROM C_Element ae WHERE ae.C_Element_ID=")
			.append(C_Element_ID).append(" AND t.AD_Tree_ID=ae.AD_Tree_ID)");
		else	//	std trees
			sb.append(" AND t.IsAllNodes='Y' AND t.AD_Table_ID=").append(AD_Table_ID);
		//	Duplicate Check
		sb.append(" AND NOT EXISTS (SELECT * FROM ").append (treeTableName).append (" e ")
		.append("WHERE e.AD_Tree_ID=t.AD_Tree_ID AND Node_ID=").append(id).append(")");
		//
		int no = DB.executeUpdate(get_Trx(), sb.toString());
		if (no > 0)
			log.fine("#" + no + " - AD_Table_ID=" + AD_Table_ID);
		else
			log.warning("#" + no + " - AD_Table_ID=" + AD_Table_ID);
		return no > 0;
	}	//	insert_Tree

	/**
	 * 	Delete ID Tree Nodes
	 *	@return true if actually deleted (could be non existing)
	 */
	private boolean deleteTreeNode()
	{
		int id = get_ID();
		if (id == 0)
			id = get_IDOld();
		int AD_Table_ID = get_Table_ID();
		if (!MTree.hasTree(AD_Table_ID))
			return false;
		String treeTableName = MTree.getNodeTableName(AD_Table_ID);
		if (treeTableName == null)
			return false;
		//
		StringBuilder sb = new StringBuilder ("DELETE FROM ")
		.append(treeTableName)
		.append(" n WHERE Node_ID=").append(id)
		.append(" AND EXISTS (SELECT * FROM AD_Tree t ")
		.append("WHERE t.AD_Tree_ID=n.AD_Tree_ID AND t.AD_Table_ID=")
		.append(AD_Table_ID).append(")");
		//
		int no = DB.executeUpdate(get_Trx(), sb.toString());
		if (no > 0)
			log.fine("#" + no + " - AD_Table_ID=" + AD_Table_ID);
		else
			log.warning("#" + no + " - AD_Table_ID=" + AD_Table_ID);
		return no > 0;
	}	//	delete_Tree


	/**************************************************************************
	 * 	Lock it.
	 * 	@return true if locked
	 */
	public boolean lock()
	{
		int index = get_ProcessingIndex();
		if (index != -1)
		{
			m_newValues[index] = Boolean.TRUE;		//	direct
			String sql = "UPDATE " + p_info.getTableName()
			+ " SET Processing='Y' WHERE (Processing='N' OR Processing IS NULL) AND "
			+ get_WhereClause(false);
			boolean success = DB.executeUpdate((Trx) null, sql, get_WhereClauseParams()) == 1;	//	outside p_trx
			if (success)
				log.fine("success");
			else
				log.log(Level.WARNING, "failed");
			return success;
		}
		return false;
	}	//	lock

	/**
	 * 	Get the Column Processing index
	 * 	@return index or -1
	 */
	private int get_ProcessingIndex()
	{
		return p_info.getColumnIndex("Processing");
	}	//	getProcessingIndex

	/**
	 * 	UnLock it
	 * 	@param trx transaction
	 * 	@return true if unlocked (false only if unlock fails)
	 */
	public boolean unlock (Trx trx)
	{
		int index = get_ProcessingIndex();
		if (index != -1)
		{
			m_newValues[index] = Boolean.FALSE;		//	direct
			String sql = "UPDATE " + p_info.getTableName()
			+ " SET Processing='N' WHERE " + get_WhereClause(false);
			boolean success = DB.executeUpdate(trx, sql, get_WhereClauseParams()) == 1;
			if (success)
				log.fine("success" + (trx == null ? "" : " [" + trx + "]"));
			else
				log.log(Level.WARNING, "failed" + (trx == null ? "" : " [" + trx + "]"));
			return success;
		}
		return true;
	}	//	unlock

	/**	Optional Transaction		*/
	private Trx			m_trx = null;

	/**
	 * 	Set Trx
	 *	@param trx transaction
	 */
	public void set_Trx (Trx trx)
	{
		m_trx = trx;
	}	//	setTrx

	/**
	 * 	Get Trx
	 *	@return transaction
	 */
	public Trx get_Trx()
	{
		return m_trx;
	}	//	getTrx


	/**************************************************************************
	 * 	Get Attachments.
	 * 	An attachment may have multiple entries
	 *	@return Attachment or null
	 */
	public MAttachment getAttachment ()
	{
		return getAttachment(false);
	}	//	getAttachment

	/**
	 * 	Get Attachments
	 * 	@param requery requery
	 *	@return Attachment or null
	 */
	public MAttachment getAttachment (boolean requery)
	{
		if (m_attachment == null || requery)
			m_attachment = MAttachment.get (getCtx(), p_info.getAD_Table_ID(), get_ID());
		return m_attachment;
	}	//	getAttachment

	/**
	 * 	Create/return Attachment for PO.
	 * 	If not exist, create new
	 *	@return attachment
	 */
	public MAttachment createAttachment()
	{
		getAttachment (false);
		if (m_attachment == null)
			m_attachment = new MAttachment (getCtx(), p_info.getAD_Table_ID(), get_ID(), null);
		return m_attachment;
	}	//	createAttachment


	/**
	 * 	Do we have a Attachment of type
	 * 	@param extension extension e.g. .pdf
	 * 	@return true if there is a attachment of type
	 */
	public boolean isAttachment (String extension)
	{
		getAttachment (false);
		if (m_attachment == null)
			return false;
		for (int i = 0; i < m_attachment.getEntryCount(); i++)
		{
			if (m_attachment.getEntryName(i).endsWith(extension))
			{
				log.fine("#" + i + ": " + m_attachment.getEntryName(i));
				return true;
			}
		}
		return false;
	}	//	isAttachment

	/**
	 * 	Get Attachment Data of type
	 * 	@param extension extension e.g. .pdf
	 *	@return data or null
	 */
	public byte[] getAttachmentData (String extension)
	{
		getAttachment(false);
		if (m_attachment == null)
			return null;
		for (int i = 0; i < m_attachment.getEntryCount(); i++)
		{
			if (m_attachment.getEntryName(i).endsWith(extension))
			{
				log.fine("#" + i + ": " + m_attachment.getEntryName(i));
				return m_attachment.getEntryData(i);
			}
		}
		return null;
	}	//	getAttachmentData

	/**
	 * 	Do we have a PDF Attachment
	 * 	@return true if there is a PDF attachment
	 */
	public boolean isPdfAttachment()
	{
		return isAttachment(".pdf");
	}	//	isPdfAttachment

	/**
	 * 	Get PDF Attachment Data
	 *	@return data or null
	 */
	public byte[] getPdfAttachment()
	{
		return getAttachmentData(".pdf");
	}	//	getPDFAttachment

	/**
	 * 	Add Attachment to Record
	 *	@param uri uri reference to be added
	 *	@return true if added
	 */
	public boolean addAttachment(URI uri)
	{
		if (uri == null)
			return false;
		if (get_ID() == 0)
		{
			log.warning("Save PO before adding attachment");
			return false;
		}
		return MAttachment.addEntry(this, uri);
	}	//	addAttachment

	/**
	 * 	Add Attachment to Record
	 *	@param file file to be loaded and added
	 *	@return true if added
	 */
	public boolean addAttachment(File file)
	{
		if (file == null)
			return false;
		if (get_ID() == 0)
		{
			log.warning("Save PO before adding attachment");
			return false;
		}
		return MAttachment.addEntry(this, file);
	}	//	addAttachment

	/**************************************************************************
	 *  Dump Record
	 */
	public void dump ()
	{
		if (CLogMgt.isLevelFinest())
		{
			log.finer(get_WhereClause (true));
			for (int i = 0; i < get_ColumnCount (); i++)
				dump (i);
		}
	}   //  dump

	/**
	 *  Dump column
	 *  @param index index
	 */
	public void dump (int index)
	{
		StringBuilder sb = new StringBuilder(" ").append(index);
		if (index < 0 || index >= get_ColumnCount())
		{
			log.finest(sb.append(": invalid").toString());
			return;
		}
		sb.append(": ").append(get_ColumnName(index))
		.append(" = ").append(m_oldValues[index])
		.append(" (").append(m_newValues[index]).append(")");
		log.finest(sb.toString());
	}   //  dump


	/*************************************************************************
	 * 	Get All IDs of Table.
	 * 	Used for listing all Entities
	 * 	<code>
	 	int[] IDs = PO.getAllIDs ("AD_PrintFont", null);
		for (int i = 0; i < IDs.length; i++)
		{
			pf = new MPrintFont(Env.getCtx(), IDs[i]);
			System.out.println(IDs[i] + " = " + pf.getFont());
		}
	 *	</code>
	 * 	@param TableName table name (key column with _ID)
	 * 	@param WhereClause optional where clause
	 * 	@return array of IDs or null
	 * 	@param trx transaction
	 */
	public static int[] getAllIDs (String TableName, String WhereClause, Trx trx)
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		StringBuilder sql = new StringBuilder("SELECT ");
		sql.append(TableName).append("_ID FROM ").append(TableName);
		if (WhereClause != null && WhereClause.length() > 0)
			sql.append(" WHERE ").append(WhereClause);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), trx);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(Integer.valueOf(rs.getInt(1)));
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sql.toString(), e);
			return null;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//	Convert to array
		int[] retValue = new int[list.size()];
		for (int i = 0; i < retValue.length; i++)
			retValue[i] = list.get(i).intValue();
		return retValue;
	}	//	getAllIDs


	/**
	 * 	Get Find parameter.
	 * 	Convert to upper case and add % at the end
	 *	@param query in string
	 *	@return out string
	 */
	protected static String getFindParameter (String query)
	{
		if (query == null)
			return null;
		if (query.length() == 0 || query.equals("%"))
			return null;
		if (!query.endsWith("%"))
			query += "%";
		return query.toUpperCase();
	}	//	getFindParameter



	/**************************************************************************
	 * 	Load LOB
	 * 	@param value LOB
	 * 	@return object
	 */
	private Object get_LOB (Object value)
	{
		if (value == null)
			return null;
		log.finest("Value=" + value);
		//
		Object retValue = null;
		long length = -99;
		try
		{
			if (value instanceof Clob)		//	returns String
			{
				Clob clob = (Clob)value;
				length = clob.length();
				retValue = clob.getSubString(1, (int)length);
			}
			else if (value instanceof Blob)	//	returns byte[]
			{
				Blob blob = (Blob)value;
				length = blob.length();
				int index = 1;	//	correct
				if (blob.getClass().getName().equals("oracle.jdbc.rowset.OracleSerialBlob"))
					index = 0;	//	Oracle Bug Invalid Arguments
				//	at oracle.jdbc.rowset.OracleSerialBlob.getBytes(OracleSerialBlob.java:130)
				retValue = blob.getBytes(index, (int)length);
			}
			else if (value instanceof String)
			{
				retValue = value;
			}
			else if (value instanceof byte[])	//EDB returns byte[] for blob
			{
				retValue = value;
			}
			else
				log.log(Level.SEVERE, "Unknown: " + value);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Length=" + length, e);
		}
		return retValue;
	}	//	getLOB

	/**	LOB Info				*/
	private ArrayList<PO_LOB>	m_lobInfo = null;

	/**
	 * 	Reset LOB info
	 */
	private void lobReset()
	{
		m_lobInfo = null;
	}	//	resetLOB

	/**
	 * 	Prepare LOB save
	 *	@param value value
	 *	@param index index
	 *	@param displayType display type
	 */
	private void lobAdd (Object value, int index, int displayType)
	{
		log.finest("Value=" + value);
		PO_LOB lob = new PO_LOB (p_info.getTableName(), get_ColumnName(index),
				get_WhereClause(true), displayType, value);
		if (m_lobInfo == null)
			m_lobInfo = new ArrayList<PO_LOB>();
			m_lobInfo.add(lob);
	}	//	lobAdd

	/**
	 * 	Save LOB
	 * 	@return true if saved or ok
	 */
	private boolean lobSave ()
	{
		if (m_lobInfo == null)
			return true;
		boolean retValue = true;
		for (int i = 0; i < m_lobInfo.size(); i++)
		{
			PO_LOB lob = m_lobInfo.get(i);
			if (!lob.save(get_Trx()))
			{
				retValue = false;
				break;
			}
		}	//	for all LOBs
		lobReset();
		return retValue;
	}	//	saveLOB

	/**
	 * 	Get Object xml (data only) representation as string
	 *	@param xml optional string buffer
	 *	@return updated/new string buffer header is only added once
	 */
	public StringBuffer get_xmlString (StringBuffer xml)
	{
		return get_xmlString(xml, true);
	}	//	get_xml_String

	/**
	 * 	Get Object xml representation as string
	 *	@param xml optional string buffer
	 * 	@param dataOnly if false, add value, label and info tags
	 *	@return updated/new string buffer header is only added once
	 */
	public StringBuffer get_xmlString (StringBuffer xml, boolean dataOnly)
	{
		if (xml == null)
			xml = new StringBuffer();
		else
			xml.append(Env.NL);
		//
		try
		{
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			DOMSource source = new DOMSource(get_xmlDocument(xml.length()!=0, dataOnly));
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			transformer.transform (source, result);
			StringBuffer newXML = writer.getBuffer();
			//
			if (xml.length() != 0)
			{	//	//	<?xml version="1.0" encoding="UTF-8"?>
				int tagIndex = newXML.indexOf("?>");
				if (tagIndex != -1)
					xml.append(newXML.substring(tagIndex+2));
				else
					xml.append(newXML);
			}
			else
				xml.append(newXML);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		return xml;
	}	//	get_xmlString

	/** Table ID Attribute		*/
	public final static String 	XML_ATTRIBUTE_AD_Table_ID = "AD_Table_ID";
	/** Record ID Attribute		*/
	public final static String 	XML_ATTRIBUTE_Record_ID = "Record_ID";

	/**
	 * 	Get XML Document representation
	 * 	@param noComment do not add comment
	 * 	@param dataOnly if false, add value, label and info tags
	 * 	@return XML document
	 */
	public Document get_xmlDocument(boolean noComment, boolean dataOnly)
	{
		Document document = null;
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();
			if (!noComment)
				document.appendChild(document.createComment(Compiere.getSummaryAscii()));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "doc", e);
		}

		xml_addPO (document, dataOnly);
		return document;
	}	//	get_xmlDocument

	/**
	 * 	Add PO to document
	 *	@param base xml node to add to
	 * 	@param dataOnly if false, add value, label and info tags
	 */
	public void xml_addPO(Node base, boolean dataOnly)
	{
		Document document = (Document)base;
		//	Root
		Element root = document.createElement(get_TableName());
		root.setAttribute(XML_ATTRIBUTE_AD_Table_ID, String.valueOf(get_Table_ID()));
		root.setAttribute(XML_ATTRIBUTE_Record_ID, String.valueOf(get_ID()));
		base.appendChild(root);

		//	Columns
		int size = get_ColumnCount();
		for (int i = 0; i < size; i++)
		{
			if (p_info.isVirtualColumn(i))
				continue;

			Object value = get_Value(i);
			int displayType = p_info.getColumnDisplayType(i);
			String columnName = p_info.getColumnName(i);

			Element colElement = document.createElement(columnName);
			Element valueElement = colElement;
			if (!dataOnly)
			{
				Element label = document.createElement("Label");
				label.appendChild(document.createCDATASection(p_info.getColumnLabel(i)));
				colElement.appendChild(label);
				valueElement = document.createElement("Value");
				colElement.appendChild(valueElement);
				//
				if (FieldType.isLookup(displayType) || FieldType.isLocation(displayType))		//	should be ID
				{
					Element infoElement = document.createElement("Info");
					colElement.appendChild(infoElement);
					Element uniqueIdElement = document.createElement("UniqueID");
					colElement.appendChild(uniqueIdElement);
					if (value != null)
					{
						String infoString = "Info for " + value.toString();
						String uniqueIdString = "ID for " + value.toString();
						String[] info = UniqueID.getInfo(columnName, displayType,
								p_info.getColumnAD_Reference_ID(i), value);
						if (info != null)
						{
							infoString = info[0];
							uniqueIdString = info[1];
						}
						infoElement.appendChild(document.createCDATASection(infoString));
						uniqueIdElement.appendChild(document.createCDATASection(uniqueIdString));
					}
				}
			}
			//
			//  Based on class of definition, not class of value
			Class<?> c = p_info.getColumnClass(i);
			if (value == null || value.equals (Null.NULL))
				;
			else if (c == Object.class)
				valueElement.appendChild(document.createCDATASection(value.toString()));
			else if (value instanceof Integer || value instanceof BigDecimal)
				valueElement.appendChild(document.createTextNode(value.toString()));
			else if (c == Boolean.class)
			{
				boolean bValue = false;
				if (value instanceof Boolean)
					bValue = ((Boolean)value).booleanValue();
				else
					bValue = "Y".equals(value);
				valueElement.appendChild(document.createTextNode(bValue ? "Y" : "N"));
			}
			else if (value instanceof Timestamp)
				valueElement.appendChild(document.createTextNode(value.toString()));
			else if (c == String.class)
				valueElement.appendChild(document.createCDATASection((String)value));
			else if (FieldType.isLOB(displayType))
				valueElement.appendChild(document.createCDATASection(value.toString()));
			else
				valueElement.appendChild(document.createCDATASection(value.toString()));
			//
			root.appendChild(colElement);
		}
		//	Custom Columns
		if (m_custom != null)
		{
			Iterator<String> it = m_custom.keySet().iterator();
			while (it.hasNext())
			{
				String columnName = it.next();
				//	int index = p_info.getColumnIndex(columnName);
				String value = m_custom.get(columnName);
				//
				Element col = document.createElement(columnName);
				if (value != null)
					col.appendChild(document.createTextNode(value));
				root.appendChild(col);
			}
			m_custom = null;
		}
	}	//	xml_addPO

	/**
	 * 	De-serialized object by setting the correct logger
	 * 	@param s ObjectInputStream
	 */
	private void readObject(ObjectInputStream s)
	throws IOException, ClassNotFoundException
	{
		s.defaultReadObject();
		log = CLogger.getCLogger (getClass());
	}  // readObject


	/**
	 *	Sets the context in p_changeVO, if p_changeVO is not null
	 *	@param windowNo window
	 *	@param columnName column
	 *	@param value value
	 */
	protected void setContext (int windowNo, String columnName, String value)
	{
		// Charges - Set Context
		if( p_changeVO != null )
			p_changeVO.setContext( getCtx(), windowNo, columnName, value );
	}	//	setContext

	/**
	 *	Added the message text to p_changeVO if p_changeVO is not null.
	 *	The message should have already been translated.
	 *	@param message error info
	 */
	protected void addError (String message)
	{
		if (p_changeVO != null)
			p_changeVO.addError(message);
	}	//	addError

	public static List<PO> findAll(Ctx ctx,MTable table,String className,Map<BigDecimal, WebServiceParameter> webParameters){
		List<PO> list = new ArrayList<PO>();

		Query query = new Query(table.getTableName());    
		Iterator<Entry<BigDecimal, WebServiceParameter>> iter = webParameters.entrySet().iterator();
		StringBuffer sort = new StringBuffer(" ORDER BY ");
		while (iter.hasNext()){    
			Entry<BigDecimal, WebServiceParameter> set = iter.next();            
			WebServiceParameter aParameter =(WebServiceParameter) set.getValue();             
			String columnName = aParameter.getParameterName();
			String value = aParameter.getParameterValue();
			String operator=aParameter.getOperator();
			String sortParameter = aParameter.getSortParameter();
			String sortOperator = aParameter.getSortOperator();

			if (sortParameter != null){				
				if (sort.length() > 10)
					sort.append(",");
				MColumn sortColumn = MColumn.getColumn(ctx, table.getAD_Table_ID(), sortParameter);
				if (sortColumn != null)
					sort.append(sortColumn.getColumnName());
				else
					sort.append(sortParameter);
				if (sortOperator != null && sortOperator.startsWith("D"))
					sort.append(" DESC ");				
				else
					sort.append(" ASC ");				
			}			

			if (columnName != null){
				if (operator == null)
					operator = "=";            

				MColumn column = MColumn.getColumn(ctx, table.getAD_Table_ID(),columnName);

				if (column != null){        

					int AD_Reference_ID=column.getAD_Reference_ID();
					if (FieldType.isLookup(AD_Reference_ID)){
						MLookupInfo lookupInfo=MLookupFactory.getLookupInfo(ctx,0,column.getAD_Reference_ID(), column.getAD_Column_ID(), Env.getLanguage(ctx), column.getColumnName(), column.getAD_Reference_Value_ID(), column.isParent(), null);

						Object id=null;
						String sql= lookupInfo.getQuery();
						// SELECT Key, Value, Name, IsActive FROM ...
						String name=MLookupFactory.getLookup_DisplayColumn (Env.getLanguage(ctx),lookupInfo.TableName).toString()+"=?";

						boolean isNumber = lookupInfo.KeyColumn.toUpperCase().endsWith("_ID");

						int posFrom = sql.lastIndexOf(" FROM ");
						boolean hasWhere = sql.indexOf(" WHERE ", posFrom) != -1;
						//
						int posOrder = sql.lastIndexOf(" ORDER BY ");								
						if (posOrder != -1)
							sql = sql.substring(0, posOrder)
							+ (hasWhere ? " AND " : " WHERE ") + name
							+ sql.substring(posOrder);
						else
							sql += (hasWhere ? " AND " : " WHERE ")

							+ name;

						boolean found=false;
						PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
						ResultSet rs = null;
						try {
							pstmt.setString(1, value);
							rs = pstmt.executeQuery();
							if (rs.next()){
								if (isNumber) {
									int key = rs.getInt(1);
									id=new BigDecimal(key);
								} else {						
									id=rs.getString(2);
								}
								found=true;
								query.addRestriction(new QueryRestriction(column.getColumnName(),operator,id,null,null));
							}
						} catch (SQLException e) {
							log.log(Level.SEVERE,e.getMessage());
						}   
						finally{
							DB.closeResultSet(rs);
							DB.closeStatement(pstmt);
						}

						if (!found)
							query.addRestriction(new QueryRestriction(column.getColumnName(),operator,value,null,null));

					}
					else if (FieldType.isText(AD_Reference_ID)){
						query.addRestriction(new QueryRestriction(column.getColumnName(),operator,value,null,null));
					}
					else if (FieldType.isDate(AD_Reference_ID)){
						Language lang = Env.getLanguage(ctx);

						SimpleDateFormat dateFormat = null;

						if (AD_Reference_ID == DisplayTypeConstants.DateTime || AD_Reference_ID == DisplayTypeConstants.Time)
							dateFormat=lang.getDateTimeFormat();
						else if (AD_Reference_ID == DisplayTypeConstants.Date)
							dateFormat=lang.getDateFormat();

						java.util.Date parsedDate;
						try {
							parsedDate = dateFormat.parse(value);
							java.sql.Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
							query.addRestriction(new QueryRestriction(column.getColumnName(),operator,timestamp,null,null));
						} catch (ParseException e) {
							Date now = new Date();
							String error="Error parsing Date. Pass Date in the following format: "+dateFormat.format(now)+":"+e.getMessage();
							log.saveError("Error",error);
							return null;
						}
					}
					else {
						query.addRestriction(new QueryRestriction(column.getColumnName(),operator,value,null,null));
					}
				} 
				else {
					String[] keyColumns = table.getKeyColumns();
					String keyColumn=null;
					if (keyColumns != null){
						keyColumn = keyColumns[0];
						query.addRestriction(new QueryRestriction(keyColumn,"=","-1",null,null));
						break;
					}
				}
			}		
		}

		StringBuffer sql = new StringBuffer("SELECT * FROM "+query.getTableName());

		if (query.getWhereClause() != null && query.getWhereClause().length() > 0)
			sql.append(" WHERE "+query.getWhereClause());

		if (sort.length() > 10)
			sql.append(sort);
		MRole role = MRole.get(ctx, ctx.getAD_Role_ID(), ctx.getAD_User_ID(), false);
		String sql1 = role.addAccessSQL(sql.toString(), "", true, false);    // ro
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql1,null);
			rs = pstmt.executeQuery();

			Constructor<?> intArgsConstructor;
			while (rs.next()){

				PO po=null;
				try {
					if (className != null)
					{
						Class<?> myClass = Class.forName(className);
						Class<?>[] intArgsClass = new Class[]{org.compiere.util.Ctx.class,java.sql.ResultSet.class,org.compiere.util.Trx.class};
						Object[] intArgs = new Object[] {ctx,rs,null};

						intArgsConstructor = myClass.getConstructor(intArgsClass);
						po = (PO)  intArgsConstructor.newInstance(intArgs);
					}
					else {
						po = new X(ctx, table, rs, null);
					}
				} catch (SecurityException e) {
					log.log(Level.SEVERE,e.getMessage());
				} catch (IllegalArgumentException e) {
					log.log(Level.SEVERE,e.getMessage());
				} catch (ClassNotFoundException e) {
					log.log(Level.SEVERE,e.getMessage());
				} catch (NoSuchMethodException e) {
					log.log(Level.SEVERE,e.getMessage());
				} catch (InstantiationException e) {
					log.log(Level.SEVERE,e.getMessage());
				} catch (IllegalAccessException e) {
					log.log(Level.SEVERE,e.getMessage());
				} catch (InvocationTargetException e) {
					log.log(Level.SEVERE,e.getMessage());
				} catch (Exception e){
					log.log(Level.SEVERE,e.getMessage());
				}

				list.add(po);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE,e.getMessage());            
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return list;
	}

	/**************************************************************************
	 * 	Create Accounting Records
	 *	@param acctTable accounting sub table
	 *	@param acctBaseTable acct table to get data from
	 *	@param whereClause optional where clause with alisa "p" for acctBaseTable
	 *  @param List of PO objects for which accounting records will be Created
	 *	@return true if records updated
	 */
	public static boolean insert_AccountingAll (String acctTable,
			String acctBaseTable, String whereClause, List<PO> po)
	{
		ArrayList <String>	s_acctColumns = new ArrayList<String>();
		String table_name = po.get(0).get_TableName();
		Object[] params = new Object[po.size()];

		int updatedBy = po.get(0).getUpdatedBy();
		int AD_Client_ID = po.get(0).getAD_Client_ID();

		String sql = "SELECT c.ColumnName "
			+ "FROM AD_Column c INNER JOIN AD_Table t ON (c.AD_Table_ID=t.AD_Table_ID) "
			+ "WHERE t.TableName=? AND c.IsActive='Y' AND c.AD_Reference_ID=25 ORDER BY 1";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString (1, acctTable);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				s_acctColumns.add (rs.getString(1));
		}
		catch (Exception e) {
			log.log(Level.SEVERE, acctTable, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		if (s_acctColumns.size() == 0)
		{
			log.severe ("No Columns for " + acctTable);
			return false;
		}

		//	Create SQL Statement - INSERT
		StringBuilder sb = new StringBuilder("INSERT INTO ")
		.append(acctTable)
		.append(" (").append(table_name)
		.append("_ID, C_AcctSchema_ID, AD_Client_ID,AD_Org_ID,IsActive, Created,CreatedBy,Updated,UpdatedBy ");
		for (int i = 0; i < s_acctColumns.size(); i++)
			sb.append(",").append(s_acctColumns.get(i));
		//	..	SELECT
		sb.append(") SELECT a.").append(table_name).append("_ID")
		.append(", p.C_AcctSchema_ID, p.AD_Client_ID,0,'Y', SysDate,")
		.append(updatedBy).append(",SysDate,").append(updatedBy);
		for (int i = 0; i < s_acctColumns.size(); i++)
			sb.append(",p.").append(s_acctColumns.get(i));
		//	.. 	FROM
		sb.append(" FROM ").append(table_name).append(" a, ").append(acctBaseTable)
		.append(" p WHERE p.AD_Client_ID=").append(AD_Client_ID);
		if (whereClause != null && whereClause.length() > 0)
			sb.append (" AND ").append(whereClause);
		sb.append(" AND a.").append(table_name).append("_ID IN (");
		int count =0;
		for(int i=0;i<po.size();i++)
		{
			if(i==0)
				sb.append("?");
			else
				sb.append(",? ");
			params[count++]=po.get(i).get_ID();
		}
		sb.append(" )");
		int no = DB.executeUpdate(po.get(0).get_Trx(), sb.toString(),params);
		if (no >= 0)
			log.fine("#" + no);
		else
			log.warning("#" + no
					+ " - Table=" + acctTable + " from " + acctBaseTable);
		return no >= 0;
	}

	/**************************************************************************
	 * 	Update Accounting Records
	 *	@param acctTable accounting sub table
	 *	@param acctBaseTable acct table to get data from
	 *	@param whereClause optional where clause with alisa "p" for acctBaseTable
	 *  @param List of PO objects for which accounting records will be udpated
	 *	@return true if records updated
	 */
	public static boolean update_AccountingAll (String acctTable,
			String acctBaseTable, String whereClause, List<PO> po)
	{
		ArrayList <String>	s_acctColumns = new ArrayList<String>();
		String table_name = po.get(0).get_TableName();
		int updatedBy = po.get(0).getUpdatedBy();
		Object[] params = new Object[po.size()];
		String sql = "SELECT c.ColumnName "
			+ "FROM AD_Column c INNER JOIN AD_Table t ON (c.AD_Table_ID=t.AD_Table_ID) "
			+ "WHERE t.TableName=? AND c.IsActive='Y' AND c.AD_Reference_ID=25 ORDER BY 1";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString (1, acctTable);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				s_acctColumns.add (rs.getString(1));
		}
		catch (Exception e) {
			log.log(Level.SEVERE, acctTable, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		if (s_acctColumns.size() == 0)
		{
			log.severe ("No Columns for " + acctTable);
			return false;
		}

		//	Create SQL Statement - INSERT
		StringBuilder sb = new StringBuilder("UPDATE ")
		.append(acctTable).append(" acct ").append(" SET (")
		.append(" Updated,UpdatedBy ");
		for (int i = 0; i < s_acctColumns.size(); i++)
			sb.append(",").append(s_acctColumns.get(i));
		//	..	SELECT
		sb.append(") = (SELECT ")
		.append(" SysDate,").append(updatedBy);
		for (int i = 0; i < s_acctColumns.size(); i++)
			sb.append(",p.").append(s_acctColumns.get(i));
		//	.. 	FROM
		sb.append(" FROM ").append(table_name).append(" a, ").append(acctBaseTable)
		.append(" p WHERE p.AD_Client_ID= acct.AD_Client_ID AND p.C_AcctSchema_ID = acct.C_AcctSchema_ID");
		if (whereClause != null && whereClause.length() > 0)
			sb.append (" AND ").append(whereClause);
		sb.append(" AND acct.").append(table_name).append("_ID = a.").append(table_name).append("_ID");
		sb.append(") WHERE acct.").append(table_name).append("_ID IN (");
		int count=0;
		for(int i=0;i<po.size();i++)
		{
			if(i==0)
				sb.append("?");
			else
				sb.append(",?");
			params[count++]=po.get(i).get_ID();
		}
		sb.append(" )");
		int no = DB.executeUpdate(po.get(0).get_Trx(), sb.toString(),params);
		if (no >= 0)
			log.fine("#" + no);
		else
			log.warning("#" + no
					+ " - Table=" + acctTable + " from " + acctBaseTable);
		return no >= 0;
	}
	
	private String getTable()
	{
		String tableName = p_info.getTableName();
		if(!tableName.startsWith("I_"))
			return p_info.getTableName();
		
		tableName = p_info.getTableName().replaceFirst("I_", "AD_");
		int tableID = MTable.get_Table_ID(tableName);
		if (tableID >0)
			return tableName;

		tableName = p_info.getTableName().replaceFirst("I_", "M_");
		tableID = MTable.get_Table_ID(tableName);
		if (tableID >0)
			return tableName;
		
		tableName = p_info.getTableName().replaceFirst("I_", "C_");
		tableID = MTable.get_Table_ID(tableName);
		if (tableID >0)
			return tableName;
		
		tableName = p_info.getTableName().replaceFirst("I_", "R_");
		tableID = MTable.get_Table_ID(tableName);
		if (tableID >0)
			return tableName;
		
		tableName = p_info.getTableName().replaceFirst("I_", "MRP_");
		tableID = MTable.get_Table_ID(tableName);
		if (tableID >0)
			return tableName;
		
		tableName = p_info.getTableName().replaceFirst("I_", "A_");
		tableID = MTable.get_Table_ID(tableName);
		if (tableID >0)
			return tableName;
	
		tableName = p_info.getTableName().replaceFirst("I_", "B_");
		tableID = MTable.get_Table_ID(tableName);
		if (tableID >0)
			return tableName;

		tableName = p_info.getTableName().replaceFirst("I_", "CM_");
		tableID = MTable.get_Table_ID(tableName);
		if (tableID >0)
			return tableName;
		
		tableName = p_info.getTableName().replaceFirst("I_", "GL_");
		tableID = MTable.get_Table_ID(tableName);
		if (tableID >0)
			return tableName;

		tableName = p_info.getTableName().replaceFirst("I_", "K_");
		tableID = MTable.get_Table_ID(tableName);
		if (tableID >0)
			return tableName;

		tableName = p_info.getTableName().replaceFirst("I_", "PA_");
		tableID = MTable.get_Table_ID(tableName);
		if (tableID >0)
			return tableName;

		tableName = p_info.getTableName().replaceFirst("I_", "S_");
		tableID = MTable.get_Table_ID(tableName);
		if (tableID >0)
			return tableName;

		tableName = p_info.getTableName().replaceFirst("I_", "T_");
		tableID = MTable.get_Table_ID(tableName);
		if (tableID >0)
			return tableName;

		tableName = p_info.getTableName().replaceFirst("I_", "W_");
		tableID = MTable.get_Table_ID(tableName);
		if (tableID >0)
			return tableName;
		
		return p_info.getTableName();
		
	}

}   //  PO
