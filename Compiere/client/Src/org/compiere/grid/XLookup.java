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
package org.compiere.grid;

import java.util.*;

import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.util.*;

/**
 *  Manual Lookup (Model)- loaded by the put method
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: XLookup.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class XLookup extends Lookup
{
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 *	Manual Lookup
	 *	@param ctx context
	 *	@param WindowNo window
	 * 	@param keyColumn key Column
	 */
	public XLookup(Ctx ctx, int WindowNo, String keyColumn)
	{
		super (ctx, WindowNo, DisplayTypeConstants.TableDir);
		m_keyColumn = keyColumn;
	}	//	XLookup


	/** Key Column - as identifier      */
	private String		m_keyColumn;

	/**
	 *	Get Display String of key value
	 * 	@param key key
	 * 	@return display
	 */
	@Override
	public String getDisplay (Object key)
	{
		//  linear search in m_data
		for (int i = 0; i < p_data.size(); i++)
		{
			Object oo = p_data.get(i);
			if (oo != null && oo instanceof NamePair)
			{
				NamePair pp = (NamePair)oo;
				if (pp.getID().equals(key))
					return pp.getName();
			}
		}
		return "<" + key + ">";
	}	//	getDisplay

	/**
	 *  The Lookup contains the key
	 * 	@param key key
	 * 	@return true if contains key
	 */
	@Override
	public boolean containsKey (Object key)
	{
		//  linear seatch in p_data
		for (int i = 0; i < p_data.size(); i++)
		{
			Object oo = p_data.get(i);
			if (oo != null && oo instanceof NamePair)
			{
				NamePair pp = (NamePair)oo;
				if (pp.getID().equals(key))
					return true;
			}
		}
		return false;
	}   //  containsKey

	/**
	 *	Get Object of Key Value
	 *  @param key key
	 *  @return Object or null
	 */
	@Override
	public NamePair get (Object key)
	{
		//  linear seatch in m_data
		for (int i = 0; i < p_data.size(); i++)
		{
			Object oo = p_data.get(i);
			if (oo != null && oo instanceof NamePair)
			{
				NamePair pp = (NamePair)oo;
				if (pp.getID().equals(key))
					return pp;
			}
		}
		return null;
	}	//	get


	/**
	 *	Return data as sorted Array
	 * 	@param mandatory mandatory
	 * 	@param onlyValidated only validated
	 * 	@param onlyActive only active
	 * 	@param temporary force load for temporary display
	 * 	@return list of data
	 */
	@Override
    public ArrayList<NamePair> getData (boolean mandatory, 
		boolean onlyValidated, boolean onlyActive, boolean temporary)
	{
		ArrayList<NamePair> list = new ArrayList<NamePair>(p_data);
		
		//	Sort Data
		if (m_keyColumn.toUpperCase().endsWith("_ID"))
		{
			KeyNamePair p = new KeyNamePair (-1, "");
			if (!mandatory)
				list.add (p);
			Collections.sort (list, p);
		}
		else
		{
			ValueNamePair p = new ValueNamePair (null, "");
			if (!mandatory)
				list.add (p);
			Collections.sort (list, p);
		}
		return list;
	}	//	getArray

	/**
	 *	Refresh Values (nop)
	 * 	@return number of cache
	 */
	@Override
	public int refresh()
	{
		return p_data.size();
	}	//	refresh

	/**
	 *	Get underlying fully qualified Table.Column Name
	 * 	@return column name
	 */
	@Override
	public String getColumnName()
	{
		return m_keyColumn;
	}   //  getColumnName

}	//	XLookup
