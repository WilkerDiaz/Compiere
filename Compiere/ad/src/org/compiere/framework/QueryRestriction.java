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
import java.sql.*;

import org.compiere.util.*;


/**
 *	Query Restriction
 *	
 *  @author Jorg Janke
 *  @version $Id: QueryRestriction.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class QueryRestriction implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Restriction
	 * 	@param columnName ColumnName
	 * 	@param operator Operator, e.g. = != ..
	 * 	@param code Code, e.g 0, All%
	 *  @param infoName Display Name
	 * 	@param infoDisplay Display of Code (Lookup)
	 */
	public QueryRestriction (String columnName, String operator,
		Object code, String infoName, String infoDisplay)
	{
		this.ColumnName = columnName.trim();
		if (infoName != null)
			InfoName = infoName;
		else
			InfoName = ColumnName;
		//
		this.Operator = operator;
		//	Boolean
		if (code instanceof Boolean)
			Code = ((Boolean)code).booleanValue() ? "Y" : "N";
		else if (code instanceof KeyNamePair)
			Code = Integer.valueOf(((KeyNamePair)code).getKey());
		else if (code instanceof ValueNamePair)
			Code = ((ValueNamePair)code).getValue();
		else
			Code = code;
		//	clean code
		if (Code instanceof String)
		{
			if (Code.toString().startsWith("'"))
				Code = Code.toString().substring(1);
			if (Code.toString().endsWith("'"))
				Code = Code.toString().substring(0, Code.toString().length()-2);
		}
		if (infoDisplay != null)
			InfoDisplay = infoDisplay.trim();
		else if (code != null)
			InfoDisplay = code.toString();
	}	//	QueryRestriction

	/**
	 * 	Range Restriction (BETWEEN)
	 * 	@param columnName ColumnName
	 * 	@param code Code, e.g 0, All%
	 * 	@param code_to Code, e.g 0, All%
	 *  @param infoName Display Name
	 * 	@param infoDisplay Display of Code (Lookup)
	 * 	@param infoDisplay_to Display of Code (Lookup)
	 */
	public QueryRestriction (String columnName,
		Object code, Object code_to,
		String infoName, String infoDisplay, String infoDisplay_to)
	{
		this (columnName, Query.BETWEEN, code, infoName, infoDisplay);

		//	Code_to
		Code_to = code_to;
		if (Code_to instanceof String)
		{
			if (Code_to.toString().startsWith("'"))
				Code_to = Code_to.toString().substring(1);
			if (Code_to.toString().endsWith("'"))
				Code_to = Code_to.toString().substring(0, Code_to.toString().length()-2);
		}
		//	InfoDisplay_to
		if (infoDisplay_to != null)
			InfoDisplay_to = infoDisplay_to.trim();
		else if (Code_to != null)
			InfoDisplay_to = Code_to.toString();
	}	//	QueryRestriction

	/**
	 * 	Create Restriction with direct WHERE clause
	 * 	@param whereClause SQL WHERE Clause
	 */
	public QueryRestriction (String whereClause)
	{
		DirectWhereClause = whereClause;
	}	//	QueryRestriction

	/**
	 * 	Copy Constructor - Internal Use
	 *	@param columnName
	 *	@param code
	 *	@param code_to
	 *	@param infoName
	 *	@param infoDisplay
	 *	@param infoDisplay_to
	 *	@param operator
	 *	@param directWhereClause
	 *	@param andCondition
	 */
	public QueryRestriction (String columnName, Object code, Object code_to,
		String infoName, String infoDisplay, String infoDisplay_to,
		String operator, String directWhereClause, boolean andCondition)
	{
		ColumnName = columnName;
		InfoName = infoName;
		Code = code;
		Code_to = code_to;
		InfoName = infoName;
		InfoDisplay = infoDisplay;
		InfoDisplay_to = infoDisplay_to;
		Operator = operator;
		DirectWhereClause = directWhereClause;
		AndCondition = andCondition;
	}	//	QueryRestriction
	
	
	/**	Direct Where Clause	*/
	protected String	DirectWhereClause = null;
	/**	Column Name			*/
	protected String 	ColumnName;
	/** Name				*/
	protected String	InfoName;
	/** Operator			*/
	protected String 	Operator;
	/** SQL Where Code		*/
	protected Object 	Code;
	/** Info				*/
	protected String 	InfoDisplay;
	/** SQL Where Code To	*/
	protected Object 	Code_to;
	/** Info To				*/
	protected String 	InfoDisplay_to;
	/** And/Or Condition	*/
	protected boolean	AndCondition = true;

	/**
	 * 	Return SQL construct for this restriction
	 *  @param tableName optional table name
	 * 	@return SQL WHERE construct
	 */
	public String getSQL (String tableName)
	{
		if (DirectWhereClause != null)
			return DirectWhereClause;
		//
		StringBuffer sb = new StringBuffer();

		// opening parenthesis for case insensitive search
		if (Code instanceof String)
			sb.append( " UPPER( " );
		
		if (tableName != null && tableName.length() > 0)
		{
			//	Assumes - REPLACE(INITCAP(variable),'s','X') or UPPER(variable)
			int pos = ColumnName.lastIndexOf('(')+1;	//	including (
			int end = ColumnName.indexOf(')');
			//	We have a Function in the ColumnName
			if (pos != 0 && end != -1)
				sb.append(ColumnName.substring(0, pos))
					.append(tableName).append(".").append(ColumnName.substring(pos, end))
					.append(ColumnName.substring(end));
			else
				sb.append(tableName).append(".").append(ColumnName);
		}
		else
			sb.append(ColumnName);

		// closing parenthesis for case insensitive search
		if (Code instanceof String)
			sb.append( " ) " );
		
		
		//	NULL Operator
		if (Code == null
			|| "NULL".equals (Code.toString().toUpperCase())
			|| Null.NULLString.equals(Code.toString()))
		{
			if (Operator.equals(Query.EQUAL))
				sb.append(" IS NULL ");
			else
				sb.append(" IS NOT NULL ");
		}				
		else
		{
			sb.append(Operator);
			if (Query.IN.equals(Operator) || Query.NOT_IN.equals(Operator))
				sb.append("(");
			
			if (Code instanceof String)
			{
				//BECO JTrias
				if(Code.toString().contains("addDays("))
					sb.append(Code.toString());
				else{
					sb.append( " UPPER( " );
					sb.append(DB.TO_STRING(Code.toString()));
					sb.append( " ) " );
				}
				//FIN BECO
			}
			else if (Code instanceof Timestamp)
				sb.append(DB.TO_DATE((Timestamp)Code, false));
			else
				sb.append(Code);
	
			//	Between
			if (Query.BETWEEN.equals(Operator))
			{
				//	if (Code_to != null && InfoDisplay_to != null)
				sb.append(" AND ");
				if (Code_to instanceof String)
					sb.append(DB.TO_STRING(Code_to.toString()));
				else if (Code_to instanceof Timestamp)
					sb.append(DB.TO_DATE((Timestamp)Code_to));
				else
					sb.append(Code_to);
			}
			else if (Query.IN.equals(Operator) || Query.NOT_IN.equals(Operator))
				sb.append(")");
		}
		return sb.toString();
	}	//	getSQL

	/**
	 * 	Get String Representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		return getSQL(null);   //jz will it be used to generate update set clause???
	}	//	toString

	/**
	 * 	Get Info Name
	 * 	@return Info Name
	 */
	public String getInfoName()
	{
		return InfoName;
	}	//	getInfoName

	/**
	 * 	Get Info Operator
	 * 	@return info Operator
	 */
	public String getInfoOperator()
	{
		for (ValueNamePair element : Query.OPERATORS) {
			if (element.getValue().equals(Operator))
				return element.getName();
		}
		return Operator;
	}	//	getInfoOperator

	/**
	 * 	Get Display with optional To
	 * 	@return info display
	 */
	public String getInfoDisplayAll()
	{
		if (InfoDisplay_to == null)
			return InfoDisplay;
		StringBuffer sb = new StringBuffer(InfoDisplay);
		sb.append(" - ").append(InfoDisplay_to);
		return sb.toString();
	}	//	getInfoDisplay

	public void setAndCondition(boolean and){
		AndCondition = and;
	}
	
}	//	QueryRestriction
