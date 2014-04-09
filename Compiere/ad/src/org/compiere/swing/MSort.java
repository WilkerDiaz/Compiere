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
package org.compiere.swing;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;

import org.compiere.util.*;

/**
 *	Class to Sort Data
 *
 *  @author Jorg Janke
 *  @version  $Id: MSort.java 8244 2009-12-04 23:25:29Z freyes $
 */
public final class MSort implements Comparator<Object>, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Constructor - Sort Entity
	 *  @param new_index index
	 *  @param new_data  data
	 */
	public MSort (int new_index, Object new_data)
	{
		index = new_index;
		data = new_data;
	}	//	MSort

	/** Direct access index */
	public int 		index;
	/** The data            */
	public Object 	data;

	/** Multiplier          */
	private int		m_multiplier = 1;		//	Asc by default

	/**
	 *	Sort Ascending
	 *  @param ascending if true sort ascending
	 */
	public void setSortAsc (boolean ascending)
	{
		if (ascending)
			m_multiplier = 1;
		else
			m_multiplier = -1;
	}	//	setSortAsc

	
	/**************************************************************************
	 *	Compare Data of two entities
	 *  @param o1 object
	 *  @param o2 object
	 *  @return comparator
	 */
	public int compare (Object o1, Object o2)
	{
		//	Get Objects to compare
		Object cmp1 = null;
		if (o1 instanceof MSort)
			cmp1 = ((MSort)o1).data;
		if (cmp1 instanceof NamePair)
			cmp1 = ((NamePair)cmp1).getName();

		Object cmp2 = o2;
		if (o2 instanceof MSort)
			cmp2 = ((MSort)o2).data;
		if (cmp2 instanceof NamePair)
			cmp2 = ((NamePair)cmp2).getName();

		//	Comparing Null values
		if (cmp1 == null)
		{
			if (cmp2 == null)
				return 0;
			return -1 * m_multiplier;
		}
		if (cmp2 == null)
			return 1 * m_multiplier;

		/**
		 *	compare different data types
		 */

		//	String
		if (cmp1 instanceof String && cmp2 instanceof String)
		{
			String s = (String)cmp1;
			return s.compareToIgnoreCase((String)cmp2) * m_multiplier;
		}
		//	Date
		else if (cmp1 instanceof Timestamp && cmp2 instanceof Timestamp)
		{
			Timestamp t = (Timestamp)cmp1;
			return t.compareTo((Timestamp)cmp2) * m_multiplier;
		}
		//	BigDecimal
		else if (cmp1 instanceof BigDecimal && cmp2 instanceof BigDecimal)
		{
			BigDecimal d = (BigDecimal)cmp1;
			return d.compareTo((BigDecimal)cmp2) * m_multiplier;
		}
		//	Integer
		else if (cmp1 instanceof Integer && cmp2 instanceof Integer)
		{
			Integer d = (Integer)cmp1;
			return d.compareTo((Integer)cmp2) * m_multiplier;
		}

		//  Convert to string value
		String s = cmp1.toString();
		return s.compareToIgnoreCase(cmp2.toString()) * m_multiplier;
	}	//	compare

	/**
	 *	Equal (based on data, ignores index)
	 *  @param obj object
	 *  @return true if equal
	 */
	@Override
	public boolean equals (Object obj)
	{
		if (obj instanceof MSort)
		{
			MSort ms = (MSort)obj;
			if (data == ms.data)
				return true;
		}
		return false;
	}	//	equals

	/**
	 * 	String Representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MSort[");
		sb.append("Index=").append(index).append(",Data=").append(data);
		sb.append("]");
		return sb.toString();
	}	//	toString

}	//	MSort
