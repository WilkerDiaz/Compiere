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
package org.compiere.model;

import java.math.*;
import java.sql.*;
import java.util.*;

import org.compiere.framework.*;
import org.compiere.util.*;

/**
 *	Asssign Criteria Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MAssignCriteria.java 8780 2010-05-19 23:08:57Z nnayak $
 */
public class MAssignCriteria extends X_AD_AssignCriteria
{
    /** Logger for class MAssignCriteria */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAssignCriteria.class);
	/** */
    private static final long serialVersionUID = 2551495916742254959L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_AssignCriteria_ID id
	 *	@param trx p_trx
	 */
	public MAssignCriteria(Ctx ctx, int AD_AssignCriteria_ID,
	    Trx trx)
	{
		super(ctx, AD_AssignCriteria_ID, trx);
	}	//	MAssignCriteria

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MAssignCriteria(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAssignCriteria
	
    /** The Source Column					*/
    private MColumn				m_column = null;

    /**
     * 	Get Source Column
     *	@return source column
     */
    public MColumn getSourceColumn()
    {
    	if (m_column == null || m_column.getAD_Column_ID() != getAD_SourceColumn_ID())
    		m_column = MColumn.get(getCtx(), getAD_SourceColumn_ID());
    	return m_column;
    }	//	getSourceColumn

	/**
	 * 	Is Criteria Met
	 *	@param po po
	 *	@return true if criteria is met
	 */
	public boolean isMet (PO po)
	{
		MColumn column = getSourceColumn();
		String columnName = column.getColumnName();
		int index = po.get_ColumnIndex(columnName);
		if (index == -1)
        	throw new IllegalArgumentException(toString() + ": AD_Column_ID not found");
		//	Get Value
		Object value = po.get_Value(index);
		String op = getOperation();
		//	Compare Value
		String compareString = getValueString();
		if (op.equals(OPERATION_Sql))
		{
			compareString = MAssignTarget.getSQLValue(po, getValueString(), log);
			op = OPERATION_Eq;
		}
		//	NULL handling
		if (value == null)
		{
			if (compareString == null 
				|| compareString.length() == 0
				|| compareString.equalsIgnoreCase("NULL"))
			{
				if (op.equals(OPERATION_Eq))
					return true;
			}
			else
			{
				if (!op.equals(OPERATION_Eq))
					return true;
			}
			return false;
		}
		if (getRecord_ID() == 0		//	no value to compare to
			&& (compareString == null || compareString.length() == 0))
		{
			return false;
		}
		
		//	Like - String
		if (op.equals(OPERATION_Like))
		{
			String s = value.toString();
			String cmp = compareString;
			if (cmp.indexOf('%') != -1)		//	SQL Like
				log.warning (toString() + ": SQL LIKE not fully supported yet");
			return s.toUpperCase()
				.indexOf(cmp.toUpperCase()) != 0;	//	substring
		}
		
		try
		{
			if (value instanceof Integer)
			{
				Integer ii = (Integer)value;
				Integer cmp = null;
				if (getRecord_ID() > 0)
					cmp = getRecord_ID();
				else
					cmp = Integer.valueOf(compareString);
				//	Tree Handling
				Boolean treeOp = treeOperation(columnName, cmp, op, ii, po.getAD_Client_ID());
				if (treeOp != null)
					return treeOp.booleanValue();
				//
				if (op.equals(OPERATION_Eq))
					return ii.equals(cmp);
				else if (op.equals(OPERATION_NotEq))
					return !ii.equals(cmp);
				else if (op.equals(OPERATION_Gt))
					return ii.compareTo(cmp) > 0;
				else if (op.equals(OPERATION_GtEq))
					return ii.compareTo(cmp) >= 0;
				else if (op.equals(OPERATION_Le))
					return ii.compareTo(cmp) < 0;
				else if (op.equals(OPERATION_LeEq))
					return ii.compareTo(cmp) <= 0;
			}
			else if (value instanceof BigDecimal)
			{
				BigDecimal bd = (BigDecimal)value;
				BigDecimal cmp = new BigDecimal(compareString);
				if (op.equals(OPERATION_Eq))
					return bd.equals(cmp);
				else if (op.equals(OPERATION_NotEq))
					return !bd.equals(cmp);
				else if (op.equals(OPERATION_Gt))
					return bd.compareTo(cmp) > 0;
				else if (op.equals(OPERATION_GtEq))
					return bd.compareTo(cmp) >= 0;
				else if (op.equals(OPERATION_Le))
					return bd.compareTo(cmp) < 0;
				else if (op.equals(OPERATION_LeEq))
					return bd.compareTo(cmp) <= 0;
			}
			else if (value instanceof Timestamp)
			{
				Timestamp ts = (Timestamp)value;
				Timestamp cmp = Timestamp.valueOf(compareString);
				if (op.equals(OPERATION_Eq))
					return ts.equals(cmp);
				else if (op.equals(OPERATION_NotEq))
					return !ts.equals(cmp);
				else if (op.equals(OPERATION_Gt))
					return ts.compareTo(cmp) > 0;
				else if (op.equals(OPERATION_GtEq))
					return ts.compareTo(cmp) >= 0;
				else if (op.equals(OPERATION_Le))
					return ts.compareTo(cmp) < 0;
				else if (op.equals(OPERATION_LeEq))
					return ts.compareTo(cmp) <= 0;
			}
			else
			// String
			{
				String s = value.toString();
				String cmp = compareString;
				if (op.equals(OPERATION_Eq))
					return s.equals(cmp);
				else if (op.equals(OPERATION_NotEq))
					return !s.equals(cmp);
				else if (op.equals(OPERATION_Gt))
					return s.compareTo(cmp) > 0;
				else if (op.equals(OPERATION_GtEq))
					return s.compareTo(cmp) >= 0;
				else if (op.equals(OPERATION_Le))
					return s.compareTo(cmp) < 0;
				else if (op.equals(OPERATION_LeEq))
					return s.compareTo(cmp) <= 0;
			}
		}
		catch (Exception e)
		{
			log.warning(toString() + ": " + e);
		}
		return false;
	}	//	isMet
	
	
	/**
	 * 	Tree Operation
	 *	@param columnName columnName
	 *	@param cmp compare value
	 *	@param op operation (only == or !=)
	 *	@param value user value
	 *	@return null if n/a otherwise evaluation
	 */
	private Boolean treeOperation(String columnName, int cmp, 
		String op, Integer value, int AD_Client_ID)
	{
		String tableName = null;
		//	Is this a Tree capable column
		if (columnName.toUpperCase().endsWith ("_ID")
			&& (op.equals(OPERATION_Eq) || op.equals(OPERATION_NotEq)))
		{
			String temp = columnName;
			if (temp.toUpperCase().endsWith ("_ID"))
				temp = columnName.substring (0, columnName.length()-3);
			if (MTree.hasTree(temp))
				tableName = temp; 
		}
		if (tableName == null)
			return null;
		
		//	Is the value a Summary node
		StringBuffer sql = new StringBuffer("SELECT ").append (columnName)
			.append(" FROM ").append(tableName)
			.append(" WHERE ").append(columnName).append("=? AND IsSummary='Y'");
		int id = QueryUtil.getSQLValue (null, sql.toString(), cmp);
		if (id <= 0)
			return null;
		
		//	Get Tree
		int AD_Tree_ID = MTree.getDefaultAD_Tree_ID (AD_Client_ID, tableName);
		if (AD_Tree_ID <= 0)
			return null;
		
		MTree tree = new MTree (getCtx(), AD_Tree_ID, false, true, null);
		CTreeNode node = tree.getRoot().findNode(id);
		log.finest("Root=" + node);
		//
		if (node != null && node.isSummary())
		{
			Enumeration<?> en = node.preorderEnumeration();
			while (en.hasMoreElements())
			{
				CTreeNode nn = (CTreeNode)en.nextElement();
				if (!nn.isSummary())
				{
					int cmp1 = nn.getNode_ID();
					if (op.equals(OPERATION_Eq))
					{
						if (value.equals(cmp1))
							return Boolean.TRUE;
					}
					else if (op.equals(OPERATION_NotEq))
					{
						if (!value.equals(cmp1))
							return Boolean.TRUE;
					}
				}
			}
		}	//	tree elements
		return Boolean.FALSE;
	}	//	treeOperation
	
	/**
	 * 	Is the Criteria Met nased on AND/OR
	 *	@param oldCriteriaMet old value
	 *	@param meetThis current value
	 *	@return new value
	 */
	public Boolean isCriteriaMet (Boolean oldCriteriaMet, boolean meetThis)
	{
		if (oldCriteriaMet == null)
			return new Boolean(meetThis);
		
		if (ANDOR_And.equals(getAndOr()))	//	AND
			return oldCriteriaMet && meetThis;
		else		//	OR
			return oldCriteriaMet || meetThis;
	}	//	isCriteriaMet
	
	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MAssignCriteria[")
	    	.append(get_ID())
	    	.append("-").append(getSeqNo())
	    	.append(",AD_SourceColumn_ID=").append(getAD_SourceColumn_ID())
	    	.append(",Operation=").append(getOperation());
	    if (getRecord_ID() != 0)
	    	sb.append(",Record_ID=").append(getRecord_ID());
	    if (getValueString() != null)
	    	sb.append(",ValueString=").append(getValueString());
	    sb.append("]");
	    return sb.toString();
    }	//	toString
    
}	//	MAssignCriteria
