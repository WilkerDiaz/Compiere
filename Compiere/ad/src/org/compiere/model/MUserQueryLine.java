/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.
 * This program is free software; you can redistribute it and/or modify it
 * under the terms version 2 of the GNU General Public License as published
 * by the Free Software Foundation. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 * You may reach us at: ComPiere, Inc. - http://www.compiere.org/license.html
 * 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA or info@compiere.org 
 *****************************************************************************/
package org.compiere.model;

import java.sql.*;

import org.compiere.framework.*;
import org.compiere.util.*;


/**
 *	User Query Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MUserQueryLine.java 8780 2010-05-19 23:08:57Z nnayak $
 */
public class MUserQueryLine extends X_AD_UserQueryLine
{
    /** Logger for class MUserQueryLine */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MUserQueryLine.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_UserQueryLine_ID id
	 *	@param trx p_trx
	 */
	public MUserQueryLine(Ctx ctx, int AD_UserQueryLine_ID, Trx trx)
	{
		super(ctx, AD_UserQueryLine_ID, trx);
		if (AD_UserQueryLine_ID == 0)
		{
			setIsAnd (true);	// Y
		}
	}	//	MUserQueryLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MUserQueryLine(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);	
	}	//	MUserQueryLine
	
	/**
	 * 	Full Constructor
	 *	@param parent user query
	 *	@param SeqNo sequence
	 *	@param column column
	 *	@param operator query operator
	 *	@param value value
	 *	@param value2 optional to value
	 */
	public MUserQueryLine(MUserQuery parent, int SeqNo, ValueNamePair column, 
		String operator, ValueNamePair value, ValueNamePair value2)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setAD_UserQuery_ID(parent.getAD_UserQuery_ID());
		setSeqNo(SeqNo);
		//
		setKeyValue(column.getValue());
		setKeyName(column.getName());
		setOperator(operator);
		//
		setValue1Value(value.getValue());
		setValue1Name(value.getName());
		if (value2 != null)
		{
			setValue2Value(value2.getValue());
			setValue2Name(value2.getName());
		}
	}	//	MUserQueryLine

	/**
	 * 	Get Operator Name
	 *	@return name
	 */
	public ValueNamePair getOperatorPair()
	{
		String op = getOperator();
		for (ValueNamePair pp : Query.OPERATORS) {
			if (pp.getValue().equals(op))
				return pp;
		}
		log.warning("Did not find operator: " + op);
		return new ValueNamePair(op,op);
	}	//	getOperatorName
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		if (getSeqNo() == 0)
		{
			int no = QueryUtil.getSQLValue (null, 
				"SELECT COALESCE(MAX(SeqNo),0)+10 FROM AD_UserQueryLine WHERE AD_UserQuery_ID=?",
				getAD_UserQuery_ID());
			setSeqNo(no);
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MUserQueryLine[");
		sb.append(get_ID()).append("-").append(getSeqNo())
			.append("]");
		return sb.toString();
	}	//	toString
	
}	//	MUserQueryLine
