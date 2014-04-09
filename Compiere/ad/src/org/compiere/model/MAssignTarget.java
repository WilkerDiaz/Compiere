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

import java.lang.reflect.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.util.*;

/**
 *	Assign Target Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MAssignTarget.java 8753 2010-05-12 17:34:49Z nnayak $
 */
public class MAssignTarget extends X_AD_AssignTarget
{
    /** Logger for class MAssignTarget */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAssignTarget.class);
	/** */
    private static final long serialVersionUID = 6056518075742542627L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_AssignTarget_ID id
	 *	@param trx p_trx
	 */
	public MAssignTarget(Ctx ctx, int AD_AssignTarget_ID, Trx trx)
	{
		super(ctx, AD_AssignTarget_ID, trx);
	}	//	MAssignTarget

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MAssignTarget(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAssignTarget
	
	/**	The Criteria Lines					*/
    private MAssignCriteria[] m_criteria = null;
    /** The Target Column					*/
    private MColumn				m_column = null;
    
    /**
     * 	Get Criteria Lines
     *	@param reload reload data
     *	@return array of lines
     */
    public MAssignCriteria[] getCriteria(boolean reload)
    {
	    if (m_criteria != null && !reload)
		    return m_criteria;
	    String sql = "SELECT * FROM AD_AssignCriteria "
	    	+ "WHERE AD_AssignTarget_ID=? ORDER BY SeqNo";
	    ArrayList<MAssignCriteria> list = new ArrayList<MAssignCriteria>();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
        try
        {
	        pstmt = DB.prepareStatement(sql, get_Trx());
	        pstmt.setInt(1, getAD_AssignTarget_ID());
	        rs = pstmt.executeQuery();
	        while (rs.next())
		        list.add(new MAssignCriteria(getCtx(), rs, get_Trx()));
        }
        catch (Exception e) {
	        log.log(Level.SEVERE, sql, e);
        }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		m_criteria = new MAssignCriteria[list.size()];
	    list.toArray(m_criteria);
	    return m_criteria;
    }	//	getCriteria

    /**
     * 	Get Target Column
     *	@return target column
     */
    public MColumn getTargetColumn()
    {
    	if (m_column == null || m_column.getAD_Column_ID() != getAD_TargetColumn_ID())
    		m_column = MColumn.get(getCtx(), getAD_TargetColumn_ID());
    	return m_column;
    }	//	getTargetColumn
	
    /**
     * 	Execute Auto Assignment
     *	@param po PO to be modified
     *	@return true if modified
     */
	public boolean executeIt (PO po)
	{
		//	Check Column
		MColumn column = getTargetColumn();
		String columnName = column.getColumnName();
		int index = po.get_ColumnIndex(columnName);
		if (index == -1)
        	throw new IllegalArgumentException(toString() + ": AD_Column_ID not found");
		//	Check Value
		Object currentValue = po.get_Value(index);
		String assignRule = getAssignRule();
		if (currentValue == null && assignRule.equals(ASSIGNRULE_OnlyIfNOTNULL))
			return false;
		else if (currentValue != null && assignRule.equals(ASSIGNRULE_OnlyIfNULL))
			return false;
		
		//	Check Criteria
		if (m_criteria == null)
			getCriteria(false);
		Boolean criteriaMet = null;		//	no criteria = assign
		for (MAssignCriteria criteria : m_criteria) 
		{
			boolean meetThis = criteria.isMet(po);
        	criteriaMet = criteria.isCriteriaMet (criteriaMet, meetThis);
        }
		if (criteriaMet != null && !criteriaMet)
			return false;
		
		MAssignLog aLog = new MAssignLog(po, this);
		//	Assignment
		String methodName = "set" + columnName;
		Class<?> parameterType = null;
		Object parameter = null;
		int displayType = column.getAD_Reference_ID();
		//	Target Value
		String valueString = getValue(po);
		int Record_ID = getRecord_ID();
		
		//
		if (FieldType.isText(displayType) || displayType == DisplayTypeConstants.List)
		{
			parameterType = String.class;
			parameter = valueString;
		}
		else if (FieldType.isID(displayType) || displayType == DisplayTypeConstants.Integer)
		{
			parameterType = int.class;
			if (Record_ID != 0)
				parameter = Record_ID;
			else if (valueString != null && valueString.length() > 0)
			{
				try
				{
					parameter = Integer.parseInt(valueString);
				}
				catch (Exception e)
				{
					log.warning(toString() + " " + e);
					return false;
				}
			}
		}
		else if (FieldType.isNumeric(displayType))
		{
			parameterType = BigDecimal.class;
			if (valueString != null && valueString.length() > 0)
			{
				try
				{
					parameter = new BigDecimal(valueString);
				}
				catch (Exception e)
				{
					log.warning(toString() + " " + e);
					return false;
				}
			}
		}
		else if (FieldType.isDate(displayType))
		{
			parameterType = java.sql.Timestamp.class;
			if (valueString != null && valueString.length() > 0)
			{
				try
				{
					parameter = Timestamp.valueOf(valueString);
				}
				catch (Exception e)
				{
					log.warning(toString() + " " + e);
					return false;
				}
			}
		}
		else if (displayType == DisplayTypeConstants.YesNo)
		{
			parameterType = boolean.class;
			parameter = "Y".equals(valueString);
		}
		else if (displayType == DisplayTypeConstants.Button)
		{
			parameterType = String.class;
			parameter = getValueString();
		}
		else if (FieldType.isLOB(displayType))	//	CLOB is String
		{
			parameterType = byte[].class;
		//	parameter = getValueString();
		}
		
		//	Assignment
		aLog.addHelp(currentValue + "=>" + parameter + "<=");
		try
		{
			Class<? extends PO> clazz = po.getClass();
			Method method = clazz.getMethod(methodName, new Class[] {parameterType});
			method.invoke(po, new Object[] {parameter});
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, toString(), e);
			//	fallback
			if (parameter instanceof Boolean)
				po.set_Value(index, valueString);
			else
				po.set_Value(index, parameter);
		//	modified = false;
		}
		aLog.save();
		return true;
	}	//	executeIt
	
	/**
	 * 	Get new Value - String or sql
	 * 	@param po the PO
	 *	@return value
	 */
	private String getValue(PO po)
	{
		String value = getSQLValue(po, getValueSQL(), log);
		if (Util.isEmpty(value))
			value = getValueString();
		return value;
	}	//	getValue
	
    /**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MAssignTarget[")
	    	.append(get_ID())
	    	.append(",AD_TargetColumn_ID=").append(getAD_TargetColumn_ID());
	    if (getRecord_ID() != 0)
	    	sb.append(",Record_ID=").append(getRecord_ID());
	    if (getValueString() != null)
	    	sb.append(",ValueString=").append(getValueString());
	    sb.append("]");
	    return sb.toString();
    }	//	toString
    
    
    /**************************************************************************
	 * 	Get SQL Value
	 * 	@param po the PO
	 * 	@param sql sql statement
	 * 	@param log logger
	 *	@return sql value or null
	 */
	static String getSQLValue(PO po, String sql, CLogger log)
	{
		if (Util.isEmpty(sql))
			return null;
		//	should add security and maybe parse
		if (!sql.toUpperCase().startsWith("SELECT "))
		{
			log.warning("SQL Statement must be a SELECT statement: " + sql);
			return null;
		}
		//	Replace Variables comes here
		
		
		String retValue = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
        try
        {
	        pstmt = DB.prepareStatement(sql, (Trx) null);
	        rs = pstmt.executeQuery();
	        if (rs.next())
	        {
	        	retValue = rs.getString(1);
	        	if (rs.next())
	        		log.warning(sql + ": More than one sql value");
	        }
        }
        catch (Exception e) {
        	log.log(Level.WARNING, sql, e);
        }
        finally {
        	DB.closeResultSet(rs);
        	DB.closeStatement(pstmt);
        }

        return retValue;
	}	//	getSQLValue

    
}	//	MAssignTarget
