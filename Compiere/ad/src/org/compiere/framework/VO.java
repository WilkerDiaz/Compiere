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
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.compiere.*;
import org.compiere.util.*;
import org.w3c.dom.*;

/**
 * 	Value Object
 *	
 *  @author Jorg Janke
 *  @version $Id: VO.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class VO
	implements Map<String,String>, Serializable
{
	/**
	 * 	ArrayList Constructor
	 *	@param keys keys
	 *	@param values values
	 */
	public VO (ArrayList<String> keys, ArrayList<String> values)
	{
		set (keys, values);
	}	//	VO

	/**
	 * 	Array Constructor
	 *	@param keys keys
	 *	@param values values
	 */
	public VO (Object[] keys, Object[] values)
	{
		set (keys, values);
	}	//	VO

	/**
	 * 	Empty Constructor
	 */
	public VO ()
	{
		this (new String[]{}, new String[]{});
	}	//	VO
	
	/**
	 * 	Empty Constructor
	 */
	public VO (String tableName, int table_ID, int record_ID)
	{
		this (new String[]{}, new String[]{});
		setHdrInfo(tableName, table_ID, record_ID);
	}	//	VO

	/**
	 * 	Copy Constructor
	 */
	public VO (VO vo)
	{
		this (vo.m_keys, vo.m_values);
	}	//	VO
	
	/**
	 * 	Map Constructor
	 *	@param map map
	 */
	public VO (Map<String,String> map)
	{
		this();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext())
		{
			Object key = it.next().toString();
			Object value = map.get(key);
			put (key, value);
		}
	}	//	VO

	/** Base SerVersion	*/
	private static final long serialVersionUID = 8683452581122892189L;

	/**	Keys			*/
	private ArrayList<String>	m_keys;
	/**	Values			*/
	private ArrayList<String>	m_values;
	/**	Embedded VOs	*/
	protected ArrayList<VO>		p_vos = null;

	private String				m_TableName;
	private int					m_Table_ID;
	private int					m_Record_ID;
	
	/**
	 * 	Set optional Header Info
	 *	@param tableName table name
	 *	@param table_ID table id
	 *	@param record_ID record_id
	 */
	public void setHdrInfo(String tableName, int table_ID, int record_ID)
	{
		m_TableName = tableName;
		m_Table_ID = table_ID;
		m_Record_ID = record_ID;
	}	//	setHdrInfo
	
	/**
	 * 	Set Basics
	 *	@param keys
	 *	@param values
	 */
	private synchronized void set (ArrayList<String> keys, ArrayList<String> values)
	{
		if (keys == null)
			throw new IllegalArgumentException("No Keys");
		if (values == null)
			throw new IllegalArgumentException("No Values");
		int size = keys.size();
		if (size != values.size())
			throw new IllegalArgumentException("Inconsistency: #Keys=" 
				+ keys.size() + "<>#Values=" + values.size());
		int capacity = Math.max (size, 10);		//	default
		m_keys = new ArrayList<String>(capacity);
		m_values = new ArrayList<String>(capacity);
		for (int i = 0; i < size; i++)
			put (keys.get(i), values.get(i));
	}	//	set
	

	/**
	 * 	Set Basics
	 *	@param keys
	 *	@param values
	 */
	private synchronized void set (Object[] keys, Object[] values)
	{
		if (keys == null)
			throw new IllegalArgumentException("No Keys");
		if (values == null)
			throw new IllegalArgumentException("No Values");
		int size = keys.length;
		if (size != values.length)
			throw new IllegalArgumentException("Inconsistency: #Keys=" 
				+ keys.length + "<>#Values=" + values.length);
		int capacity = Math.max (size, 10);		//	default
		m_keys = new ArrayList<String>(capacity);
		m_values = new ArrayList<String>(capacity);
		for (int i = 0; i < size; i++)
			put (keys[i], values[i]);
	}	//	set

	/**
	 * 	Get Size
	 *	@return size
	 */
	public int size()
	{
		if (m_keys == null)
			return 0;
		int noK = m_keys.size();
		int noV = m_values.size();
		if (noK != noV)
			System.err.println(getClass() + " Inconsistency: #Keys=" 
				+ noK + "<>#Values=" + noV);
		return noK;
	}	//	size

	/**
	 * 	Is Empty
	 *	@return true if empty
	 */
	public boolean isEmpty ()
	{
		return m_keys.isEmpty();
	}	//	isEmpty

	/**
	 * 	Contains Key
	 *	@param key key
	 *	@return true if contains
	 */
	public boolean containsKey (Object key)
	{
		if (key == null)
			return false;
		return m_keys.contains(key);
	}	//	containsKey

	/**
	 * 	Contains Value
	 *	@param value value
	 *	@return true if contains value
	 */
	public boolean containsValue (Object value)
	{
		if (value == null)
			return false;
		return m_values.contains(value);
	}	//	containsValue

	/**
	 * 	Get Value as String of Key
	 *	@param key key
	 *	@return value or null
	 */
	public synchronized String get (Object key)
	{
		if (key == null)
			return null;
		int index = m_keys.indexOf(key);
		if (index != -1)
			return m_values.get(index);
		return null;
	}	//	get
	
	/**
	 * 	Put key/value
	 *	@param key key
	 *	@param value value
	 *	@return previous value or null
	 */
	public synchronized String put (Object key, Object value)
	{
		if (key == null)
			return null;
		if (value == null)
			return remove(key);
		//
		String stringKey = key.toString();
		String stringValue = value.toString();
		if (value instanceof Boolean)
			stringValue = ((Boolean)value) ? "Y" : "N";
		return put (stringKey, stringValue);
	}	//	put

	/**
	 * 	Put key/value
	 *	@param key key
	 *	@param value value
	 *	@return previous value or null
	 */
	public synchronized String put (String key, String value)
	{
		if (key == null)
			return null;
		if (value == null)
			return remove(key);
		//
		int index = m_keys.indexOf(key);
		if (index != -1)
			return m_values.set(index, value);
		m_keys.add(key);
		m_values.add(value);
		return null;
	}	//	put

	/**
	 * 	Remove
	 *	@param key key
	 *	@return previous value or null
	 */
	public synchronized String remove (Object key)
	{
		if (key == null)
			return null;
		int index = m_keys.indexOf(key);
		String old = null;
		if (index != -1)
		{
			old = m_values.get(index);
			m_keys.remove(index);
			m_values.remove(index);
		}
		return old;
	}	//	remove

	/**
	 * 	Put All
	 *	@param t map
	 */
	public void putAll (Map<? extends String,? extends String> t)
	{
		Iterator<? extends String> it = t.keySet().iterator();
		while (it.hasNext())
		{
			Object key = it.next();
			Object value = t.get(key);
			put(key.toString(), value.toString());
		}
	}	//	putAll

	/**
	 * 	Clear keys/values
	 */
	public void clear()
	{
		m_keys.clear();
		m_values.clear();
	}	//	clear

	/**
	 * 	Get Key Set
	 *	@return key set
	 */
	public Set<String> keySet()
	{
		HashSet<String> set = new HashSet<String>(m_keys);
		return set;
	}	//	keySet

	/**
	 * 	Get Values
	 *	@return values as collection
	 */
	public Collection<String> values()
	{
		return m_values;
	}	//	values

	/**
	 * 	Get Values Set
	 *	@return values set
	 */
	public Set<Map.Entry<String,String>> entrySet()
	{
		return getHashMap().entrySet();
	}	//	entrySet
	
	/**
	 * 	Get VO as HashMap
	 *	@return hash map
	 */
	public HashMap<String,String> getHashMap()
	{
		HashMap<String,String> map = new HashMap<String,String>(m_keys.size());
		for (int i = 0; i < m_keys.size(); i++)
		{
			String key = m_keys.get(i); 
			String value = get(key);
			map.put(key, value);
		}
		return map;
	}	//	getHashMap
	
	/**
	 * 	Clone object
	 *	@return clone
	 */
	@Override
	public VO clone()
	{
		return new VO(this);
	}	//	clone
	
	/**
	 * 	Get Data as Array positioned by column
	 *	@param columns array of Columns
	 *	@return array of data
	 */
	public String[] getData (String[] columns)
	{
		String[] retValue = new String[columns.length];
		for (int i = 0; i < columns.length; i++)
		{
			String key = columns[i]; 
			String value = get(key);
			retValue[i] = value;
		}
		return retValue;
	}	//	getData
	
	/**
	 * 	Set Embedded
	 *	@param vos array list of VOs
	 */
	public void setEmbedded(ArrayList<VO> vos)
	{
		p_vos = vos;
	}	//	setEmbedded
	
	/**
	 * 	Get Embedded VOs
	 *	@return array list of VOs or null
	 */
	public ArrayList<VO> getEmbedded()
	{
		return p_vos;
	}	//	getEmbedded

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("VO[");
		if (m_keys != null)
		{
			sb.append ("#").append (m_keys.size());
			if (m_keys.size() != m_values.size())
				sb.append("<>values#").append(m_values.size());
		}
		if (p_vos != null)
			sb.append (";Embedded=#").append(p_vos.size());
		sb.append ("]");
		return sb.toString();
	}	//	toString
	
	/**
	 * 	Extended Info
	 */
	public String toStringX()
	{
		String className = getClass().toString();
		int index = className.lastIndexOf(".");
		if (index != -1)
			className = className.substring(index+1);
		//
		StringBuffer sb = new StringBuffer(className).append("[");
		for (int i = 0; i < m_keys.size(); i++)
		{
			if (i != 0)
				sb.append(";");
			String key = m_keys.get(i); 
			sb.append(key)
				.append("=")
				.append(get(key));
		}
		if (p_vos != null)
		{
			for (int i = 0; i < p_vos.size(); i++)
			{
				VO vo = p_vos.get(i);
				sb.append("\n ").append(vo.toStringX());
			}
		}
		sb.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Dump Attributes to out
	 */
	public void dump()
	{
		System.out.println(toStringX());
	}	//	dump
	
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
		}
		//	Root
		if (Util.isEmpty(m_TableName))
			m_TableName="VO";
		Element root = document.createElement(m_TableName);
		root.setAttribute(XML_ATTRIBUTE_AD_Table_ID, String.valueOf(m_Table_ID));
		root.setAttribute(XML_ATTRIBUTE_Record_ID, String.valueOf(m_Record_ID));
		document.appendChild(root);
		if (m_keys == null)
			return document;
		
		//	Columns
		int size = m_keys.size();
		for (int i = 0; i < size; i++)
		{
			String value = m_values.get(i);
			Element colElement = document.createElement(m_keys.get(i));
			if (Util.is7Bit(value))
				colElement.appendChild(document.createTextNode(value));
			else
				colElement.appendChild(document.createCDATASection(value));
			//
			root.appendChild(colElement);
		}
		return document;
	}	//	getDocument
	
}	//	VO
