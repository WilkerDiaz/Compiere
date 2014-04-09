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

/**
 *  Name Pair Interface
 *
 *  @author     Jorg Janke
 *  @version    $Id: NamePair.java,v 1.3 2006/07/30 00:52:23 jjanke Exp $
 */
public abstract class NamePair implements Comparator<NamePair>, Serializable, Comparable<NamePair>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* added so NamePair can be serialized by GWT */
	public NamePair(){}
	/**
	 *  Protected Constructor
	 *  @param   name    (Display) Name of the Pair
	 */
	protected NamePair (String name)
	{
		setName(name);
	}   //  NamePair

	/** The Name        		*/
	private String  m_name;
	/**	Sort by Name			*/
	private boolean	m_sortByName = true;

	/**
	 *  Returns display value
	 *  @return name
	 */
	public String getName()
	{
		return m_name;
	}   //  getName

	/**
	 * 	Set Name
	 *	@param name name
	 */
	public void setName(String name)
	{
		m_name = name;
		if (m_name == null)
			m_name = "";
	}	//	setName
	
	/**
	 * 	Get Sort By Name
	 *	@return true if by name
	 */
	public boolean isSortByName()
	{
		return m_sortByName;
	}	//	isSortByName
	
	/**
	 * 	Set Sort By Name
	 *	@param sortByName true if by name, false by ID
	 */
	public void setSortByName(boolean sortByName)
	{
		m_sortByName = sortByName;
	}	//	setSortByName
	
	/**
	 *  Returns Key or Value as String
	 *  @return String or null
	 */
	public abstract String getID();

	/**
	 *	Comparator Interface (based on Name/ID value)
	 *  @param p1 Value 1
	 *  @param p2 Value 2
	 *  @return compareTo value
	 */
	public int compare (NamePair p1, NamePair p2)
	{
		String s1 = p1 == null ? "" : (m_sortByName ? p1.getName() : p1.getID());
		String s2 = p2 == null ? "" : (m_sortByName ? p2.getName() : p2.getID());
		return s1.compareTo (s2);    //  sort order ??
	}	//	compare

		/**
	 * 	Comparable Interface (based on Name/ID value)
	 *  @param   o the Object to be compared.
	 *  @return  a negative integer, zero, or a positive integer as this object
	 *		is less than, equal to, or greater than the specified object.
	 */
	public int compareTo (NamePair o)
	{
		return compare (this, o);
	}	//	compareTo

	/**
	 *	To String - returns name
	 *  @return Name
	 */
	@Override
	public String toString()
	{
		return m_name;
	}	//	toString

	/**
	 *	To String - detail
	 *  @return String in format ID=Name
	 */
	public String toStringX()
	{
		StringBuffer sb = new StringBuffer (getID());
		sb.append("=").append(m_name);
		return sb.toString();
	}	//	toStringX
	
	/**
	 * Returns the index of the first option found for the key, or -1 if not found.
	 * @param key
	 * @return
	 */
	public static int indexOfKey(ArrayList<NamePair> options, String key )
	{
		if(options == null)
			return -1;
		if( key == null )
			key = "";
		
		int result = -1;
		for( int i = 0; i < options.size(); ++i )
		{
			//in KeyNamePair case, getID can be null
			if( key.equals( options.get(i).getID()) || ("".equals(key) && options.get(i).getID() == null))
			{
				result = i;
				break;
			}
		}
		return result;
	}

	/**
	 * Returns the index of the first option found for the key, or -1 if not found.
	 * @param value
	 * @return
	 */
	public static int indexOfValue(ArrayList<NamePair> options, String value )
	{
		if( value == null )
			value = "";
		
		int result = -1;
		for( int i = 0; i < options.size(); ++i )
		{
			if( value.equals( options.get(i).getName() ) )
			{
				result = i;
				break;
			}
		}
		return result;
	}
	


}	//	NamePair
