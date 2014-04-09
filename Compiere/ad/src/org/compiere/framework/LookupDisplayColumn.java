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


/**
 *  Lookup Display Column Value Object
 *
 *  @author Jorg Janke
 *  @version $Id: LookupDisplayColumn.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class LookupDisplayColumn implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Lookup Column Value Object
	 * 	@param columnName column name
	 * 	@param isTranslated translated
	 * 	@param ad_Reference_ID display type
	 * 	@param ad_Reference_Value_ID table/list reference id
	 */
	public LookupDisplayColumn(String columnName, boolean isTranslated,
		int ad_Reference_ID, int ad_Reference_Value_ID)
	{
		ColumnName = columnName;
		IsTranslated = isTranslated;
		DisplayType = ad_Reference_ID;
		AD_Reference_ID = ad_Reference_Value_ID;
	}	//

	/** Column Name		*/
	public String 	ColumnName;
	/** Translated		*/
	public boolean 	IsTranslated;
	/** Display Type	*/
	public int 		DisplayType;
	/** Value Reference	*/
	public int 		AD_Reference_ID;

	/**
	 * 	String Representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("LookupDisplayColumn[");
		sb.append("ColumnName=").append(ColumnName);
		if (IsTranslated)
			sb.append(",IsTranslated");
		sb.append(",DisplayType=").append(DisplayType);
		if (AD_Reference_ID != 0)
			sb.append(",AD_Reference_ID=").append(AD_Reference_ID);
		sb.append("]");
		return sb.toString();
	}	//	toString

}	//	LookupDisplayColumn
