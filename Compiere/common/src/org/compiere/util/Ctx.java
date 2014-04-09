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

import java.io.*;
import java.util.*;

import org.compiere.common.constants.*;

/**
 *	Context
 *
 *  @author Jorg Janke
 *  @version $Id$
 */
public class Ctx implements Map<String, String>, Evaluatee, Serializable
{
	private static final long serialVersionUID = -411257863402987484L;
	public final static String TABLE_SELECT_ALL = "#TableSelectAll";

	/** R/O Context with no Context for cached entries		*/
	public static Ctx 	ROCTX = new Ctx("{AD_Org_ID=-1}");
	/**	Logger		*/
	public static CompiereLogger	log = new CLoggerSimple(Ctx.class);


	/**
	 * 	Context
	 */
	public Ctx()
	{
	}	//	Ctx

	/**
	 * 	Context
	 *	@param map context (clone)
	 */
	public Ctx (Map<String,String> map)
	{
		this();
		load (map);
	}	//	Ctx

	/**
	 * 	Context
	 *	@param stringRepresentation example {AD_Org_ID=11}
	 */
	public Ctx(String stringRepresentation)
	{
		this();
		load (stringRepresentation);
	}	//	Ctx


	/**
	 * 	Context
	 *	@param set set of Map.Entry
	 */
	public Ctx (Set<Map.Entry<String, String>> set)
	{
		this();
		load (set);
	}	//	Ctx


	/**	The Map */
	protected HashMap<String,String> m_map = new HashMap<String, String>();

	/**
	 * 	Load
	 *	@param stringRepresentation example
	 *	{AD_Org_ID=11, IsDefault=N, IsActive=Y, Greeting=Mr, Name=Mr, C_Greeting_ID=100, AD_Client_ID=11, IsFirstNameOnly=N}
	 */
	protected void load (String stringRepresentation)
	{
		if ((stringRepresentation == null)
				|| !stringRepresentation.startsWith ("{")
				|| !stringRepresentation.endsWith ("}"))
			throw new IllegalArgumentException("Cannot load: " + stringRepresentation);
		String string = stringRepresentation.substring (1, stringRepresentation.length()-1);
		StringTokenizer st = new StringTokenizer(string, ",");
		while (st.hasMoreTokens())
		{
			String pair = st.nextToken().trim();
			if (pair.length() == 0)
				continue;
			int index = pair.indexOf("=");
			if (index == -1)
			{
				log.warning("Load invalid format: " + pair);
				continue;
			}
			String key = pair.substring (0,index);
			String value = pair.substring (index+1);
			m_map.put(key,value);
		}
		if (log != null)
			log.info(toString());
	}	//	load

	/**
	 * 	Load
	 *	@param set set of Map.Entry
	 */
	private void load (Set<Map.Entry<String,String>> set)
	{
		Iterator<Entry<String, String>> it = set.iterator();
		while (it.hasNext())
		{
			Map.Entry<String,String> entry = it.next();
			String key = entry.getKey();
			setContext (key, entry.getValue());
		}
	}	//	load

	/**
	 * 	Load
	 *	@param map map
	 */
	private void load (Map< ? extends String, ? extends String> map)
	{
		Iterator<? extends Map.Entry<? extends String, ? extends String>> it = map.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry<? extends String, ? extends String> entry = it.next();
			String key = entry.getKey();
			setContext (key, entry.getValue());
		}
	}	//	load

	/**
	 *	Set Global Context to Value
	 *  @param context context key
	 *  @param value context value
	 *  @return old value
	 */
	public String setContext (String context, String value)
	{
		if (context == null)
			return null;
		log.finer(context + "=" + value);
		//
		if ((value == null) || value.equals(""))
			return m_map.put(context, null);
		//	return m_map.remove(context);
		else
			return m_map.put(context, value);
	}	//	setContext

	/**
	 *	Set Global Context to Value
	 *  @param ctx context
	 *  @param context context key
	 *  @param value context value
	 */
	public Object setContext (String context, Date value)
	{
		String stringValue = null;
		if (value != null)
		{
			long time = value.getTime();
			stringValue = String.valueOf(time);
		}
		return setContext(context, stringValue);
	}	//	setContext

	/**
	 *	Set Global Context to (int) Value
	 *  @param ctx context
	 *  @param context context key
	 *  @param value context value
	 */
	public Object setContext (String context, int value)
	{
		return setContext(context, String.valueOf(value));
	}	//	setContext

	/**
	 *	Set Global Context to Y/N Value
	 *  @param ctx context
	 *  @param context context key
	 *  @param value context value
	 */
	public Object setContext (String context, boolean value)
	{
		return setContext(context, value ? "Y" : "N");
	}	//	setContext

	/**
	 *	Set Context for Window to Value
	 *  @param windowNo window no
	 *  @param context context key
	 *  @param value context value
	 */
	public void setContext (int windowNo, String context, String value)
	{
		if (context == null)
			return;
		if ((windowNo != EnvConstants.WINDOW_FIND)
			&& (windowNo != EnvConstants.WINDOW_MLOOKUP)
			&& (windowNo < EnvConstants.WINDOW_TEMP))
			log.finer("("+windowNo+"): " + context + "=" + value);
		//
		if ((value == null) || value.equals(""))
		//	m_map.remove(WindowNo+"|"+context);
			m_map.put(windowNo+"|"+context, null);
		else
			m_map.put(windowNo+"|"+context, value);
	}	//	setContext

	/**
	 * 	Add Map elements to context
	 *	@param WindowNo window
	 * 	@param addContext new context
	 */
	public void setContext (int WindowNo, Map<String,String> addContext)
	{
		if (addContext == null)
			return;
		for( Map.Entry<String, String> entry : addContext.entrySet() )
		{
			setContext(WindowNo, entry.getKey(), entry.getValue());
		}
	}	//	setContext

	/**
	 *	Set Context for Window to Value
	 *  @param WindowNo window no
	 *  @param context context key
	 *  @param value context value
	 */
	public void setContext (int WindowNo, String context, Date value)
	{
		String stringValue = null;
		if (value != null)
		{
			long time = value.getTime();
			stringValue = String.valueOf(time);
		}
		setContext(WindowNo, context, stringValue);
	}	//	setContext

	/**
	 *	Set Context for Window to int Value
	 *  @param WindowNo window no
	 *  @param context context key
	 *  @param value context value
	 */
	public void setContext (int WindowNo, String context, int value)
	{
		setContext(WindowNo, context, String.valueOf(value));
	}	//	setContext

	/**
	 *	Set Context for Window to Y/N Value
	 *  @param WindowNo window no
	 *  @param context context key
	 *  @param value context value
	 */
	public void setContext (int WindowNo, String context, boolean value)
	{
		setContext (WindowNo, context, value ? "Y" : "N");
	}	//	setContext

	/**
	 *	Set Context for Window & Tab to Value
	 *  @param WindowNo window no
	 *  @param TabNo tab no
	 *  @param context context key
	 *  @param value context value
	 */
	public void setContext (int WindowNo, int TabNo, String context, String value)
	{
		if (context == null)
			return;
//		if (WindowNo != WINDOW_FIND && WindowNo != WINDOW_MLOOKUP)
		log.finest("C("+WindowNo+","+TabNo+"): " + context + "=" + value);
		//
		if ((value == null) || value.equals(""))
			m_map.put(WindowNo+"|"+TabNo+"|"+context, null);
		//	m_map.remove(WindowNo+"|"+TabNo+"|"+context);
		else
			m_map.put(WindowNo+"|"+TabNo+"|"+context, value);
	}	//	setContext

	/**
	 *	Set Auto Commit
	 *  @param autoCommit auto commit (save)
	 */
	public void setAutoCommit (boolean autoCommit)
	{
		m_map.put("AutoCommit", autoCommit ? "Y" : "N");
	}	//	setAutoCommit

	/**
	 *	Set Auto Commit for Window
	 *  @param WindowNo window no
	 *  @param autoCommit auto commit (save)
	 */
	public void setAutoCommit (int WindowNo, boolean autoCommit)
	{
		setContext(WindowNo, "AutoCommit", autoCommit ? "Y" : "N");
	}	//	setAutoCommit

	/**
	 *	Set Auto New Record
	 *  @param autoNew auto new record
	 */
	public void setAutoNew (boolean autoNew)
	{
		m_map.put("AutoNew", autoNew ? "Y" : "N");
	}	//	setAutoNew

	/**
	 *	Set Auto New Record for Window
	 *  @param ctx context
	 *  @param WindowNo window no
	 *  @param autoNew auto new record
	 */
	public void setAutoNew (int WindowNo, boolean autoNew)
	{
		setContext(WindowNo, "AutoNew", autoNew ? "Y" : "N");
	}	//	setAutoNew


	/**
	 *	Set SO Trx
	 *  @param isSOTrx SO Context
	 */
	public void setIsSOTrx (boolean isSOTrx)
	{
		m_map.put("IsSOTrx", isSOTrx ? "Y" : "N");
	}	//	setSOTrx

	/**
	 *	Set SO Trx
	 *  @param WindowNo window no
	 *  @param isSOTrx SO Context
	 */
	public void setIsSOTrx (int WindowNo, boolean isSOTrx)
	{
		setContext(WindowNo, "IsSOTrx", isSOTrx ? "Y" : "N");
	}	//	setSOTrx

	/**
	 * 	Get Primary Accounting Schema Currency Precision
	 * 	@return std precision
	 */
	public int getStdPrecision()
	{
		return getContextAsInt("#StdPrecision");
	}	//	getStdPrecision

	/**
	 * 	Set Primary Accounting Schema Currency Precision
	 * 	@param precision std precision
	 */
	public void setStdPrecision(int precision)
	{
		setContext("#StdPrecision", precision);
	}	//	setStdPrecision

	/**
	 * 	Set Printer Name
	 * 	@param printerName printer
	 */
	public void setPrinterName (String printerName)
	{
		setContext("#Printer", printerName);
	}	//	setPrinterName

	/**
	 * 	Get Printer Name
	 * 	@return printer
	 */
	public String getPrinterName()
	{
		return getContext("#Printer");
	}	//	getPrinterName

	/**
	 *	Get global Value of Context
	 *  @param ctx context
	 *  @param context context key
	 *  @return value or ""
	 */
	public String getContext (String context)
	{
		if (context == null)
			throw new IllegalArgumentException ("Require Context");
		Object value = m_map.get(context);
		if (value == null)
		{
			if (context.equals("#AD_User_ID"))
				return getContext("#" + context);
			return "";
		}
		String retValue = value.toString();
		return retValue;
	}	//	getContext

	/**
	 *	Get Value of Context for Window.
	 *	if not found global context if available and enabled
	 *  @param WindowNo window
	 *  @param context context key
	 *  @param  onlyWindow  if true, no defaults are used unless explicitly asked for
	 *  @return value or ""
	 */
	public String getContext (int WindowNo, String context, boolean onlyWindow)
	{
		if (context == null)
			throw new IllegalArgumentException ("Require Context");
		String key = WindowNo + "|" + context;
		if (WindowNo == 0)
			key = context;
		Object value = m_map.get(key);
		//don't use value == null 'cuz there may be a null value for this key
		//if (value == null)
		if(!m_map.containsKey(key))
		{
			//	Explicit Base Values
			if (context.startsWith("#") || context.startsWith("$"))
				return getContext(context);
			if (onlyWindow)			//	no Default values
				return "";
			return getContext("#" + context);
		}
		if(value == null)
			return "";
		else
		return value.toString();
	}	//	getContext

	/**
	 *	Get Value of Context for Window.
	 *	if not found global context if available
	 *  @param WindowNo window
	 *  @param context context key
	 *  @return value or ""
	 */
	public String getContext (int WindowNo, String context)
	{
		return getContext(WindowNo, context, false);
	}	//	getContext

	/**
	 *	Get Value of Context for Window & Tab,
	 *	if not found global context if available
	 *  @param WindowNo window no
	 *  @param TabNo tab no
	 *  @param context context key
	 *  @return value or ""
	 */
	public String getContext (int WindowNo, int TabNo, String context)
	{
		if (context == null)
			throw new IllegalArgumentException ("Require Context");
		Object value = m_map.get(WindowNo+"|"+TabNo+"|"+context);
		if (value == null)
			return getContext(WindowNo, context, false);
		return value.toString();
	}	//	getContext

	/**
	 *	Get Context and convert it to an integer (0 if error)
	 *  @param context context key
	 *  @return value
	 */
	public int getContextAsInt(String context)
	{
		if (context == null)
			throw new IllegalArgumentException ("Require Context");
		String value = getContext(context);
		if (value.length() == 0)
			value = getContext(0, context, false);		//	search 0 and defaults
		if (value.length() == 0)
			return 0;
		//
		try
		{
			return Integer.parseInt(value);
		}
		catch (NumberFormatException e)
		{
			log.warning("(" + context + ") = " + value + " - " + e.getMessage());
		}
		return 0;
	}	//	getContextAsInt

	public int getContextAsInt(int WindowNo, String context)
	{
		return getContextAsInt(WindowNo, context, false);
	}
	
	/**
	 *	Get Context and convert it to an integer (0 if error)
	 *  @param ctx context
	 *  @param WindowNo window no
	 *  @param context context key
	 *  @return value or 0
	 */
	public int getContextAsInt(int WindowNo, String context, boolean onlyWindow)
	{
		String s = getContext(WindowNo, context, onlyWindow);
		if (s.length() == 0)
			return 0;
		//
		try
		{
			return Integer.parseInt(s);
		}
		catch (NumberFormatException e)
		{
			log.warning("(" + context + ") = " + s + " - " + e.getMessage());
		}
		return 0;
	}	//	getContextAsInt

	/**
	 *	Get Context and convert it to an integer (0 if error)
	 *  @param WindowNo window no
	 *  @param TabNo tab no
	 * 	@param context context key
	 *  @return value or 0
	 */
	public int getContextAsInt (int WindowNo, int TabNo, String context)
	{
		String s = getContext(WindowNo, TabNo, context);
		if (s.length() == 0)
			return 0;
		//
		try
		{
			return Integer.parseInt(s);
		}
		catch (NumberFormatException e)
		{
			log.warning("(" + context + ") = " + s + " - " + e.getMessage());
		}
		return 0;
	}	//	getContextAsInt

	/**
	 *	Is AutoCommit
	 *  @return true if auto commit
	 */
	public boolean isAutoCommit()
	{
		String s = getContext("AutoCommit");
		if ((s != null) && s.equals("Y"))
			return true;
		return false;
	}	//	isAutoCommit

	/**
	 *	Is Window AutoCommit (if not set use default)
	 *  @param WindowNo window no
	 *  @return true if auto commit
	 */
	public boolean isAutoCommit (int WindowNo)
	{
		String s = getContext(WindowNo, "AutoCommit", false);
		if (s != null)
		{
			if (s.equals("Y"))
				return true;
			else
				return false;
		}
		return isAutoCommit();
	}	//	isAutoCommit


	/**
	 *	Is Auto New Record
	 *  @return true if auto new
	 */
	public boolean isAutoNew()
	{
		String s = getContext("AutoNew");
		if ((s != null) && s.equals("Y"))
			return true;
		return false;
	}	//	isAutoNew

	/**
	 *	Is Window Auto New Record (if not set use default)
	 *  @param WindowNo window no
	 *  @return true if auto new record
	 */
	public boolean isAutoNew (int WindowNo)
	{
		String s = getContext(WindowNo, "AutoNew", false);
		if (s != null)
		{
			if (s.equals("Y"))
				return true;
			else
				return false;
		}
		return isAutoNew();
	}	//	isAutoNew


	/**
	 *	Is Sales Order Trx
	 *  @return true if SO (default)
	 */
	public boolean isSOTrx ()
	{
		String s = getContext("IsSOTrx");
		if ((s != null) && s.equals("N"))
			return false;
		return true;
	}	//	isSOTrx

	/**
	 *	Is Sales Order Trx
	 *  @param WindowNo window no
	 *  @return true if SO (default)
	 */
	public boolean isSOTrx (int WindowNo)
	{
		String s = getContext(WindowNo, "IsSOTrx", true);
		if ((s != null) && s.equals("N"))
			return false;
		return true;
	}	//	isSOTrx

	/**
	 *	Get Context and convert it to a Timestamp
	 *	if error return today's date
	 *  @param context context key
	 *  @return Time
	 */
	public long getContextAsTime(String context)
	{
		return getContextAsTime(0, context);
	}	//	getContextAsDate

	/**
	 *	Get Context and convert it to a Timestamp
	 *	if error return today's date
	 *  @param WindowNo window no
	 *  @param context context key
	 *  @return Time
	 */
	public long getContextAsTime(int WindowNo, String context)
	{
		if (context == null)
			throw new IllegalArgumentException ("Require Context");
		String s = getContext(WindowNo, context, false);
		if ((s == null) || s.equals(""))
		{
			log.warning("No value for: " + context);
			return System.currentTimeMillis();
		}
		try
		{
			return Long.parseLong(s);
		}
		catch (NumberFormatException e)
		{
			log.warning("(" + context + ") = " + s + " - " + e.getMessage());
		}
		return System.currentTimeMillis();
	}	//	getContextAsTime

	/**
	 * 	Get Login AD_Client_ID
	 *	@return login AD_Client_ID
	 */
	public int getAD_Client_ID()
	{
		return getContextAsInt("#AD_Client_ID");
	}	//	getAD_Client_ID

	/**
	 * 	Set Login AD_Client_ID
	 *	@param AD_Client_ID client
	 */
	public void setAD_Client_ID(int AD_Client_ID)
	{
		setContext("#AD_Client_ID", AD_Client_ID);
	}	//	setAD_Client_ID

	/**
	 * 	Get AD_Client_ID
	 * 	@param WindowNo Window
	 *	@return login AD_Client_ID
	 */
	public int getAD_Client_ID (int WindowNo)
	{
		return getContextAsInt(WindowNo, "AD_Client_ID");
	}	//	getAD_Client_ID

	/**
	 * 	Get Login AD_Org_ID
	 *	@return login AD_Org_ID
	 */
	public int getAD_Org_ID()
	{
		return getContextAsInt("#AD_Org_ID");
	}	//	getAD_Org_ID

	/**
	 * 	Set Login AD_Org_ID
	 *	@param AD_Org_ID org
	 */
	public void setAD_Org_ID(int AD_Org_ID)
	{
		setContext("#AD_Org_ID", AD_Org_ID);
	}	//	setAD_Org_ID

	/**
	 * 	Get Window AD_Org_ID
	 * 	@param WindowNo window
	 *	@return login AD_Org_ID
	 */
	public int getAD_Org_ID (int WindowNo)
	{
		return getContextAsInt(WindowNo, "AD_Org_ID");
	}	//	getAD_Org_ID

	/**
	 * 	Get Tab AD_Org_ID
	 * 	@param WindowNo window
	 * 	@param TabNo tab
	 *	@return login AD_Org_ID
	 */
	public int getAD_Org_ID (int WindowNo, int TabNo)
	{
		return getContextAsInt(WindowNo, TabNo, "AD_Org_ID");
	}	//	getAD_Org_ID

	/**
	 * 	Get Login AD_User_ID
	 *	@return login AD_User_ID
	 */
	public int getAD_User_ID()
	{
		return getContextAsInt("##AD_User_ID");
	}	//	getAD_User_ID

	/**
	 * 	Set Login AD_User_ID
	 *	@param AD_User_ID user
	 */
	public void setAD_User_ID (int AD_User_ID)
	{
		setContext("##AD_User_ID", AD_User_ID);
	}	//	setAD_User_ID

	/**
	 * 	Get Login AD_Role_ID
	 *	@return login AD_Role_ID
	 */
	public int getAD_Role_ID()
	{
		return getContextAsInt("#AD_Role_ID");
	}	//	getAD_Role_ID

	/**
	 * 	Set Login AD_Role_ID
	 *	@param AD_Role_ID role
	 */
	public void setAD_Role_ID (int AD_Role_ID)
	{
		setContext("#AD_Role_ID", AD_Role_ID);
	}	//	setAD_Role_ID

	/**
	 * 	Is Processed
	 *	@return true if processed
	 */
	public boolean isProcessed (int WindowNo)
	{
		return "Y".equals (getContext(WindowNo, "Processed"));
	}	//	isProcessed

	/**
	 * 	Is Processing
	 *	@return true if processing
	 */
	public boolean isProcessing (int WindowNo)
	{
		return "Y".equals (getContext(WindowNo, "Processing"));
	}	//	isProcessing

	/**
	 * 	Is Active
	 *	@return true if active
	 */
	public boolean isActive (int WindowNo)
	{
		return "Y".equals (getContext(WindowNo, "IsActive"));
	}	//	isActive

	/**
	 * 	Size
	 *	@return size of context
	 */
	public int size()
	{
		return m_map.size();
	}	//	size

	/**
	 * 	String Info
	 *	@return info
	 */
	public String toStringShort()
	{
		StringBuffer sb = new StringBuffer ("Ctx[#")
		.append (m_map.size())
		.append ("]");
		return sb.toString ();
	}	//	toStringShort

	/**
	 * 	Extended String Info
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("Ctx[#")
		.append(m_map.size());
		sb.append(m_map.toString());
		sb.append("]");
		return sb.toString ();
	}	//	toString

	/**************************************************************************
	 *	Get sorted Context as String array with format: key == value
	 *  @param ctx context
	 *  @return context string
	 */
	public String[] getEntireContext()
	{
		Iterator<String> keyIterator = keySet().iterator();
		ArrayList<String> sList = new ArrayList<String>(size());
		while (keyIterator.hasNext())
		{
			Object key = keyIterator.next();
			String s = key + " == " + get(key);
			sList.add(s);
		}

		String[] retValue = new String[sList.size()];
		sList.toArray(retValue);
		Arrays.sort(retValue);
		return retValue;
	}	//	getEntireContext

	/**
	 * 	Evaluatee
	 *	@param variableName key
	 *	@return value
	 */
	public String get_ValueAsString (String variableName)
	{
		return getContext (variableName);
	}	//	get_ValueAsString


	/**************************************************************************
	 * 	Get Ctx with just Window data and basic context
	 *	@param windowNo
	 *	@return new context
	 */
	public Ctx getCtx (int windowNo)
	{
		Ctx newCtx = new Ctx(getMap(windowNo, false));
		return newCtx;
	}	//	getCtx

	/**
	 * 	Get Map with spaces as null
	 *	@param windowNo window no
	 *	@return map
	 */
	public Map<String,String> getMap (int windowNo)
	{
		return getMap (windowNo, false);
	}	//	getMap

	/**
	 * 	Get Map with just Window data and basic context
	 *	@param windowNo window
	 *	@return map
	 */
	public Map<String,String> getMap (int windowNo, boolean convertNullToEmptyString)
	{
		HashMap<String, String> newMap = new HashMap<String, String>();
		Set<Entry<String,String>> set = m_map.entrySet();
		/**	Copy Window only 	**/
		String keyPart = windowNo + "|";
		String infoPart = EnvConstants.WINDOW_INFO+"|";
		int keyPartIndex = keyPart.length();

		Iterator<Entry<String, String>> it = set.iterator();
		while (it.hasNext())
		{
			Map.Entry<String,String> entry = it.next();
			String key = entry.getKey();
			int index = key.indexOf(keyPart);
			if (index == 0)		//	start of Key
			{
				String newKey = key.substring (keyPartIndex);
				String oo = entry.getValue();
				if ((oo == null) && convertNullToEmptyString)
					newMap.put(newKey, "");
				else
					newMap.put(newKey, oo);
			}
			else
			{
				index = key.indexOf(infoPart);
				if (index == 0)		//	start of Key
				{
					//String newKey = key.substring (infoPartIndex);
					//only overwrite newMap when the key is not there.
					//for example, user may have changed 1|C_BPartner_ID on base window to 118
					//and 1113|1113|C_BPartner_ID in info window ctx stays as 117. We
					// don't want 117 to overwrite 118
					//if(!newMap.containsKey(newKey))
					newMap.put(key, entry.getValue());
				}
			}
		}
		return newMap;
	}	//	getMap

	/**
	 *	Remove context for Window (i.e. delete it)
	 *  @param windowNo window
	 */
	public void removeWindow (int windowNo)
	{
		int startSize = keySet().size();
		Object[] keys = keySet().toArray();
		for (Object element : keys)
		{
			String key = element.toString();
			if (key.startsWith(windowNo + "|"))
				remove(key);
		}
		int endSize = keySet().size();
		log.fine("#" + windowNo + ": " + startSize + " -> " + endSize);
	}	//	removeWindow

	/**
	 * 	Remove Preferences
	 *	@param attribute attribute
	 *	@param attributeValue value
	 */
	public void deletePreference(String attribute, String attributeValue)
	{
		Iterator<String> it = m_map.keySet().iterator();
		while (it.hasNext())
		{
			String key = it.next();
			if ((key.startsWith("P") || key.startsWith("#")) //	Preferences or Defaults
					&& (key.indexOf(attribute) != -1))
			{
				String value = getContext(key);
				if (value.equals(attributeValue))
					//m_map.remove(key);
					it.remove();
			}
		}
	}	//	deletePreference


	/**************************************************************************
	 * 	clear
	 *	@see java.util.Map#clear()
	 */
	public void clear()
	{
		m_map.clear();
	}	//	clear

	/**
	 * 	Contains Key
	 *	@param key key
	 *	@return true if contains key
	 */
	public boolean containsKey(Object key)
	{
		return m_map.containsKey (key);
	}	//	containsKey

	/**
	 * 	Contains Value
	 *	@param value
	 *	@return
	 */
	public boolean containsValue(Object value)
	{
		return m_map.containsValue (value);
	}

	/**
	 * 	entrySet
	 *	@return
	 */
	public Set<Map.Entry<String, String>> entrySet()
	{
		return m_map.entrySet();
	}

	/**
	 * 	get
	 *	@param key
	 *	@return
	 */
	public String get(Object key)
	{
		return m_map.get (key);
	}

	/**
	 * 	isEmpty
	 *	@return
	 */
	public boolean isEmpty()
	{
		return m_map.isEmpty();
	}

	/**
	 * 	keySet
	 *	@return
	 */
	public Set<String> keySet()
	{
		return m_map.keySet();
	}

	/**
	 * 	Set Context key / value
	 *	@param key
	 *	@param value
	 *	@return old value
	 */
	public String put(String key, String value)
	{
		if (key == null)
			return null;
		if (value == null)
			return setContext (key, (String)null);
		String stringValue = null;
		if (value != null)
			stringValue = value;
		return setContext (key, stringValue);
	}	//	put

	/**
	 * 	Put All
	 *	@param arg0
	 */
	public void putAll(Map<? extends String, ? extends String> map)
	{
		load (map);
	}	//	putAll

	/**
	 * 	remove
	 *	@param key
	 *	@return
	 */
	public String remove(Object key)
	{
		return m_map.remove(key);
	}

	/**
	 * 	values
	 *	@return
	 */
	public Collection<String> values()
	{
		return m_map.values();
	}	//	values

	/**
	 * 	Get Map
	 *	@return map
	 */
	protected Map<String, String> getMap()
	{
		return m_map;
	}	//	getMap

	public void copyContext( int from_windowNO, int to_windowNO, String context, boolean onlyWindow )
	{
		if( to_windowNO != from_windowNO )
			setContext( to_windowNO, context, getContext( from_windowNO, context, onlyWindow ) );
	}

	/**
	 * Copy the designated context variable from one windowNO to another
	 * @param windowNo
	 * @param window_info
	 * @param string
	 */
	public void copyContext( int from_windowNO, int to_windowNO, String context )
	{
		if( to_windowNO != from_windowNO )
			setContext( to_windowNO, context, getContext( from_windowNO, context ) );
	}

	public String getUITheme() {
		return getContext("#UITheme");
	}

	public void setUITheme(String value){
		setContext("#UITheme", value);
	}

	public boolean isShowAdvanced() {
		return getContext("#ShowAdvanced").equals("Y");
	}

	public void setShowAdvanced(boolean showAdvanced){
		m_map.put("#ShowAdvanced", showAdvanced ? "Y" : "N");
	}

	public boolean isPrintPreview() {
		return getContext("#PrintPreview").equals("Y");
	}

	public void setPrintPreview(boolean printPreview){
		m_map.put("#PrintPreview", printPreview ? "Y" : "N");
	}
	public boolean isTableSelectAll(int windowNO, int tabNO) {
		return getContext(windowNO, tabNO, TABLE_SELECT_ALL).equals("Y");
	}

	public void setTableSelectAll(int windowNO, int tabNO, boolean tableSelectAll){
		setContext(windowNO, tabNO, TABLE_SELECT_ALL, tableSelectAll ? "Y" : "N");
	}

	public void setDefaultDate (String date)
	{
		setContext("#Date", date);
	}	//	setDefaultDate

	public String getDefaultDate()
	{
		return getContext("#Date");
	}	//	getDefaultDate
	
	public void setBatchMode(boolean isBatch) {
		setContext("##IsBatchMode", isBatch ? "Y" : "N");
	}
	public boolean isBatchMode() {
		return "Y".equals(getContext("##IsBatchMode"));
	}
}	//	Ctx
