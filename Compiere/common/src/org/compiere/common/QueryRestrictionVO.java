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


/**
 *	Query Restriction VO
 *	
 *  @author Jorg Janke
 *  @version $Id$
 */
public class QueryRestrictionVO implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Equal 			*/
	public static final String	EQUAL = "=";
	/** Not Equal		*/
	public static final String	NOT_EQUAL = "!=";
	/** Like			*/
	public static final String	LIKE = " LIKE ";
	/** Not Like		*/
	public static final String	NOT_LIKE = " NOT LIKE ";
	/** Greater			*/
	public static final String	GREATER = ">";
	/** Greater Equal	*/
	public static final String	GREATER_EQUAL = ">=";
	/** Less			*/
	public static final String	LESS = "<";
	/** Less Equal		*/
	public static final String	LESS_EQUAL = "<=";
	/** Between			*/
	public static final String	BETWEEN = " BETWEEN ";

	
	/**
	 * 	Serialization Constructor
	 */
	public QueryRestrictionVO() 
	{
	}	//	QueryRestrictionVO
	
	/**
	 * 	Query Restriction
	 * 	@param columnName ColumnName
	 * 	@param operator Operator, e.g. = != ..
	 * 	@param code Code, e.g 0, All%
	 *  @param infoName Display Name
	 * 	@param infoDisplay Display of Code (Lookup)
	 */
	public QueryRestrictionVO (String columnName, String operator,
		String code, String infoName, String infoDisplay, int displayType)
	{
		this.ColumnName = columnName.trim();
		if (infoName != null)
			InfoName = infoName;
		else
			InfoName = ColumnName;
		//
		this.Operator = operator;
		//	Boolean
	/*	if (code instanceof Boolean)
			Code = ((Boolean)code).booleanValue() ? "Y" : "N";
		else if (code instanceof KeyNamePair)
			Code = new Integer(((KeyNamePair)code).getKey());
		else if (code instanceof ValueNamePair)
			Code = ((ValueNamePair)code).getValue();
		else*/
		Code = code;
		//	clean code
		
		if(Code != null) {
			if (Code.startsWith("'"))
				Code = Code.substring(1);
			if (Code.endsWith("'"))
				Code = Code.substring(0, Code.length()-2);
		}
			
		if (infoDisplay != null)
			InfoDisplay = infoDisplay.trim();
		else if(Code != null)
			InfoDisplay = Code;
		
		DisplayType = displayType;
	}	//	Restriction

	/**
	 * 	Range Restriction (BETWEEN)
	 * 	@param columnName ColumnName
	 * 	@param code Code, e.g 0, All%
	 * 	@param code_to Code, e.g 0, All%
	 *  @param infoName Display Name
	 * 	@param infoDisplay Display of Code (Lookup)
	 * 	@param infoDisplay_to Display of Code (Lookup)
	 */
	public QueryRestrictionVO (String columnName,
			String code, String code_to,
		String infoName, String infoDisplay, String infoDisplay_to, int displayType)
	{
		this (columnName, BETWEEN, code, infoName, infoDisplay, displayType);

		//	Code_to
		Code_to = code_to;
		
		if (Code_to.startsWith("'"))
			Code_to = Code_to.substring(1);
		if (Code_to.endsWith("'"))
			Code_to = Code_to.substring(0, Code_to.length()-2);

		//	InfoDisplay_to
		if (infoDisplay_to != null)
			InfoDisplay_to = infoDisplay_to.trim();
		else if (Code_to != null)
			InfoDisplay_to = Code_to;
	}	//	Restriction

	/**
	 * 	Create Restriction with dircet WHERE clause
	 * 	@param whereClause SQL WHERE Clause
	 */
	public QueryRestrictionVO (String whereClause)
	{
		DirectWhereClause = whereClause;
	}	//	QueryRestrictionVO

	/**	Direct Where Clause	*/
	public String	DirectWhereClause = null;
	/**	Column Name			*/
	public String 	ColumnName;
	/** Name/Label			*/
	public String	InfoName;
	/** Operator			*/
	public String 	Operator;
	
	/** SQL Where Code		*/
	public String 	Code;
	/** Code Info			*/
	public String 	InfoDisplay;
	
	/** SQL Where Code To	*/
	public String 	Code_to;
	/** Code Info To		*/
	public String 	InfoDisplay_to;
	
	/** And/Or Condition	*/
	public boolean	AndCondition = true;

	/**	Helps identify the data type */
	public int DisplayType = 0;

	/**
	 * 	String Representation
	 */
	@Override
	public String toString() 
	{
		return DisplayType+InfoDisplay_to+Code_to+InfoDisplay+Code+Operator
			+InfoName+ColumnName+DirectWhereClause;
	}	//	toString
	
	/**
	 * 	Equals
	 * 	@param q 
	 *	@return true if string representation is equal
	 */
	public boolean equals(QueryRestrictionVO q) 
	{
		if(q == null)
			return false;
		return q.toString().equals(toString());
	}	//	equals
	
}	//	QueryRestrictionVO
