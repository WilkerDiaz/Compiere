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
package org.compiere.common;

import java.io.*;
import java.util.*;

import org.compiere.common.constants.*;

/**
 * Query VO. If savedQueryName is passed the query is looked up If
 * savedQueryName & saveQuery & restrictions are passed, the query will be saved
 * for that tab
 * 
 * @author Jorg Janke
 * @version $Id$
 */
public class QueryVO implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Name of the saved query */
	public String savedQueryName = null;

	/** Code of the saved query */
	public boolean saveQuery = false;
	
	
	public boolean textSearch = false;
	
	public String textQuery = null;

	/**
	 * Query Restrictions
	 */
	public ArrayList<QueryRestrictionVO> restrictions = null;

	/** Records with Number of current days */
	public int onlyCurrentDays = 0;

	/** onlyCurrentDays using created or updated (default) */
	public boolean onlyCurrentCreated = false;

	public boolean equals( QueryVO v )
	{
		if( v == null )
			return false;
		// if address the same, then equal
		if( v == this )
			return true;
		if( onlyCurrentDays != v.onlyCurrentDays || onlyCurrentCreated != v.onlyCurrentCreated )
			return false;
		if( restrictions == null )
		{
			if( v.restrictions == null )
				return true;
			else
				return false;
		}

		if( v.restrictions == null )
			return false;

		if( restrictions.size() != v.restrictions.size() )
			return false;

		for( int i = 0; i < restrictions.size(); i++ )
		{
			if( !restrictions.get( i ).equals( v.restrictions.get( i ) ) )
				return false;
		}

		return true;
	}
	
	public void addRestrictions( QueryVO p_queryVO )
	{
		if( p_queryVO != null && p_queryVO.restrictions != null )
		{
			if( this.restrictions == null )
				this.restrictions = new ArrayList<QueryRestrictionVO>();
			this.restrictions.addAll( p_queryVO.restrictions );
		}
	}
	public void addRestriction(QueryRestrictionVO qVO) {
		if( this.restrictions == null )
			this.restrictions = new ArrayList<QueryRestrictionVO>();
		this.restrictions.add(qVO);
	}
	
	public StringBuffer getWhereClause() {
		StringBuffer whereClauseBuf = new StringBuffer();
		if(restrictions != null )
		{
			whereClauseBuf = new StringBuffer();
			boolean first = true;
			for(int i=0; i<restrictions.size(); i++) {
				QueryRestrictionVO r = restrictions.get(i);
				
				if( first )
					whereClauseBuf.append( " WHERE " );
				else
					whereClauseBuf.append( " AND " );
				first = false;

				StringBuffer columnName = new StringBuffer();
				if( !FieldType.isCaseSensitive( r.DisplayType ) )
					columnName.append( "UPPER( " );
				columnName.append( r.ColumnName );
				if( !FieldType.isCaseSensitive( r.DisplayType ) )
					columnName.append( " ) " );

				if( !r.Operator.equals( QueryRestrictionVO.BETWEEN ) )
				{
					whereClauseBuf.append( columnName );
					whereClauseBuf.append( r.Operator );
					whereClauseBuf.append( "? " );
				}
				else
				{
					if( r.Code != null && r.Code.length() > 0 )
					{
						whereClauseBuf.append( columnName );
						whereClauseBuf.append( QueryRestrictionVO.GREATER_EQUAL );
						whereClauseBuf.append( "? " );
					}
					if( r.Code != null && r.Code.length() > 0 && r.Code_to != null && r.Code_to.length() > 0 )
					{
						whereClauseBuf.append( "AND " );
					}
					if( r.Code_to != null && r.Code_to.length() > 0 )
					{
						whereClauseBuf.append( columnName );
						whereClauseBuf.append( QueryRestrictionVO.LESS_EQUAL );
						whereClauseBuf.append( "? " );
					}
				}
			}
		}
		return whereClauseBuf;
	}
	
	
	public Object[] getParams() {
		ArrayList<Object> params = new ArrayList<Object>();
		if( restrictions != null )
		{
			for(int i=0; i<restrictions.size(); i++) {
				QueryRestrictionVO r = restrictions.get(i);
				if(r.DisplayType == DisplayTypeConstants.Integer)
					params.add(new Integer(r.Code));
				else
					params.add(r.Code);
			}
		}
		Object[] ret = new Object[params.size()];
		for(int i=0; i<ret.length; i++)
			ret[i] = params.get(i);
		return ret;
	}
} // QueryVO

