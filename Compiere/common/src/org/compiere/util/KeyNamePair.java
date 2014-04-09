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


/**
 *	(Numeric) Key Name Pair
 *
 *  @author     Jorg Janke
 *  @version    $Id: KeyNamePair.java,v 1.2 2006/07/30 00:52:23 jjanke Exp $
 */
public final class KeyNamePair extends NamePair
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Use for GWT serialization
	 */
	public KeyNamePair()
	{
	}	//	KeyNamePair

	/**
	 *	Constructor KeyValue Pair -
	 *  @param key Key (-1 is considered as null)
	 *  @param name string representation
	 */
	public KeyNamePair(int key, String name)
	{
		super(name);
		m_key = key;
	}   //  KeyNamePair

	/** The Key         */
	private int 	m_key = 0;

	/**
	 *	Get Key
	 *  @return key
	 */
	public int getKey()
	{
		return m_key;
	}	//	getKey

	/**
	 *	Get ID (key as String)
	 *  @return String value of key or null if -1
	 */
	@Override
	public String getID()
	{
		if (m_key == -1)
			return null;
		return String.valueOf(m_key);
	}	//	getID


	/**
	 *	Equals
	 *  @param obj object
	 *  @return true if equal
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof KeyNamePair)
		{
			KeyNamePair pp = (KeyNamePair)obj;
			if (pp.getKey() == m_key
				&& pp.getName() != null
				&& pp.getName().equals(getName()))
				return true;
			return false;
		}
		return false;
	}	//	equals

	/**
	 *	Comparator Interface (based on Name/ID value)
	 *  @param p1 Value 1
	 *  @param p2 Value 2
	 *  @return compareTo value
	 */
	public int compare (KeyNamePair p1, KeyNamePair p2)
	{
		if (isSortByName())
			return super.compare (p1, p2);
		Integer i1 = p1.getKey();
		Integer i2 = p2.getKey();
		return i1.compareTo (i2);
	}	//	compare

	/**
	 * 	Comparable Interface (based on Name/ID value)
	 *  @param   o the Object to be compared.
	 *  @return  a negative integer, zero, or a positive integer as this object
	 *		is less than, equal to, or greater than the specified object.
	 */
	public int compareTo (KeyNamePair o)
	{
		if (isSortByName())
			return super.compare (this, o);
		if (o == null)
			return -1;
		Integer i1 = m_key;
		Integer i2 = o.getKey();
		return i1.compareTo (i2);
	}	//	compareTo

	/**
	 *  Return Hashcode of key
	 *  @return hascode
	 */
	@Override
	public int hashCode()
	{
		return m_key;
	}   //  hashCode

}	//	KeyNamePair
