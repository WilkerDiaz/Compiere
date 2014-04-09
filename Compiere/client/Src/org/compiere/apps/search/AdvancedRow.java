package org.compiere.apps.search;

import java.util.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Find Advanced Row
 *	
 *  @author Jorg Janke
 *  @version $Id$
 */
public class AdvancedRow extends Vector<ValueNamePair>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Load Query Lines
	 *	@param query
	 *	@return lines
	 */
	public static Vector<AdvancedRow> load (MUserQuery query)
	{
		Vector<AdvancedRow> retValue = new Vector<AdvancedRow>();
		MUserQueryLine[] lines = query.getLines(false);
		for (MUserQueryLine line : lines) {
			ValueNamePair column = new ValueNamePair(line.getKeyValue(), line.getKeyName());
			ValueNamePair operator = line.getOperatorPair();
			ValueNamePair value = new ValueNamePair(line.getValue1Value(), line.getValue1Name());
			ValueNamePair value2 = null;
			if (line.getValue2Value() != null)
				value2 = new ValueNamePair(line.getValue2Value(), line.getValue2Name());
			//
			AdvancedRow row = new AdvancedRow(column, operator, value, value2);
			retValue.add(row);
		}
		return retValue;
	}	//	load
	
	/**
	 * 	Store Query Lines
	 *	@param query query
	 *	@param rows rows to be saved
	 *	@return true if saved
	 */
	public static boolean store (MUserQuery query, Vector<AdvancedRow> rows)
	{
		query.deleteLines();
		if (rows == null)
			return true;
		for (int i = 0; i < rows.size(); i++)
		{
			AdvancedRow row = rows.get(i);
			MUserQueryLine line = new MUserQueryLine(query, (i+1)*10,
				row.getColumn(), row.getOperator().getValue(), 
				row.getValue(), row.getValue2());
			line.save();
		}
		return true;
	}	//	store
	
	
	/**************************************************************************
	 * 	Empty Advanced Row
	 */
	public AdvancedRow()
	{
		this (null, null, null, null);
	}	//	AdvancedRow
	
	/**
	 * 	Full Advanced Row Constructor
	 *	@param column
	 *	@param operator
	 *	@param value
	 *	@param value2
	 */
	public AdvancedRow(ValueNamePair column, ValueNamePair operator,
		ValueNamePair value, ValueNamePair value2)
	{
		super(4);
		setColumn(column);
		setOperator(operator);
		setValue(value);
		setValue2(value2);
	}	//	AdvancedRow

	
    public ValueNamePair getColumn()
    {
    	if (size() > 0)
    		return get(0);
    	return null;
    }
	
    public void setColumn(ValueNamePair column)
    {
    	if (size() < 1)
    		add(column);
    	else
    		set(0, column);
    }
	
    public ValueNamePair getOperator()
    {
    	if (size() > 1)
    		return get(1);
    	return null;
    }
	
    public void setOperator(ValueNamePair operator)
    {
    	if (size() < 2)
    		add(operator);
    	else
    		set(1, operator);
    }
	
    public ValueNamePair getValue()
    {
    	if (size() > 2)
    		return get(2);
    	return null;
    }
	
    public void setValue(ValueNamePair value)
    {
    	if (size() < 3)
    		add(value);
    	else
    		set(2, value);
    }
	
    public ValueNamePair getValue2()
    {
    	if (size() > 3)
    		return get(3);
    	return null;
    }
	
    public void setValue2(ValueNamePair value2)
    {
    	if (size() < 4)
    		add(value2);
    	else
    		set(3, value2);
    }

    /**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer();
	    ValueNamePair pp = getColumn();
	    if (pp != null)
	    	sb.append(pp.getName());
	    pp = getOperator();
	    if (pp != null)
	    	sb.append(pp.getName());
	    pp = getValue();
	    if (pp != null)
	    	sb.append(pp.getName());
	    pp = getValue2();
	    if (pp != null)
	    	sb.append(" - ").append(pp.getName());
	    return sb.toString();
    }	//	toString
    
    /**
     * 	String Representation
     *	@return info
     */
    public String toStringX()
    {
	    StringBuffer sb = new StringBuffer();
	    ValueNamePair pp = getColumn();
	    if (pp != null)
	    	sb.append(pp.toStringX());
	    pp = getOperator();
	    if (pp != null)
	    	sb.append(" - ").append(pp.toStringX());
	    pp = getValue();
	    if (pp != null)
	    	sb.append(" - ").append(pp.toStringX());
	    pp = getValue2();
	    if (pp != null)
	    	sb.append(" - ").append(pp.toStringX());
	    return sb.toString();
    }	//	toString
    
}	//	AdvancedRow
