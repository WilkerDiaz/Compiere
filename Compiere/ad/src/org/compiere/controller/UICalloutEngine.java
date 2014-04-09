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
package org.compiere.controller;

import java.lang.reflect.*;
import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.api.*;
import org.compiere.framework.*;
import org.compiere.util.*;
import org.compiere.vos.*;

/**
 *  Callout Engine
 *
 *  @author     Jorg Janke
 *  @version    $Id: UICalloutEngine.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class UICalloutEngine implements CalloutInterface
{
	//	TODO: ImpFormatRow
	/**
	 *	Constructor
	 */
	public UICalloutEngine()
	{
		super();
		m_name = getClass().getName();
		int index = m_name.lastIndexOf(".");
		if (index > 0)
			m_name = m_name.substring(index+1);
		m_name += ": ";
	}	//	UICalloutEngine

	/** Logger					*/
	protected CLogger		log = CLogger.getCLogger(getClass());
	/**	Class Name				*/
	private String			m_name = "";

	/**
	 * 	Callout
	 *	@param ctx context
	 * 	@param windowNo window no
	 * 	@param po business object
	 *	@param field field
	 *	@param oldValue old value
	 *	@param newValue new value
	 *	@param methodName method name
	 *	@return Change Info or null
	 */
	public ChangeVO start(Ctx ctx, int windowNo, PO po,
			UIField field, String oldValue, String newValue, String methodName)
	{
		ChangeVO retValue = new ChangeVO();
		if (methodName == null || methodName.length() == 0)
		{
			retValue.addError(m_name + "No Method Name");
			return retValue;
		}
		//	Find Method
		Method method = getMethod(methodName);
		if (method == null)
		{
			retValue.addError(m_name + "Method not found: " + methodName);
			return retValue;
		}
		int argLength = method.getParameterTypes().length;
		if (argLength != 5)
		{
			retValue.addError(m_name + "Method " + methodName
				+ " has invalid no of arguments: " + argLength);
			return retValue;
		}

		//	Call Method
		StringBuffer msg = new StringBuffer(methodName).append(" - ")
			.append(field.getColumnName())
			.append("=").append(newValue)
			.append(" (old=").append(oldValue)
			.append(")");
		log.info (msg.toString());
		try
		{
			Object[] args = new Object[] {ctx, Integer.valueOf(windowNo), field, oldValue, newValue};
			retValue = (ChangeVO)method.invoke(this, args);
		}
		catch (Exception e)
		{
			Throwable ex = e.getCause();	//	InvocationTargetException
			if (ex == null)
				ex = e;
			log.log(Level.WARNING, methodName, ex);
		//	ex.printStackTrace(System.err);
			retValue.addError(ex.getLocalizedMessage());
		}
		return retValue;
	}	//	start

	/**
	 * 	Get Method
	 *	@param methodName method name
	 *	@return method or null
	 */
	private Method getMethod (String methodName)
	{
		Method[] allMethods = getClass().getMethods();
		for (Method element : allMethods)
		{
			if (methodName.equals(element.getName()))
				return element;
		}
		return null;
	}	//	getMethod


	/**************************************************************************
	 * 	Calculate Cross rate for MultiplyRate and DivideRate
	 * 	org.compiere.controller.UICalloutEngine.rate
	 *	@param ctx context
	 *	@param windowNo window no
	 *	@param field field
	 *	@param oldValue old
	 *	@param newValue new
	 *	@return changes or null
	 */
	public ChangeVO rate (Ctx ctx, int windowNo, UIField field, String oldValue, String newValue)
	{
		ChangeVO retValue = new ChangeVO();
		BigDecimal rate1 = field.convertToBigDecimal(newValue);
		if (rate1 == null)
			rate1 = Env.ZERO;
		BigDecimal rate2 = Env.ZERO;
		BigDecimal one = new BigDecimal(1.0);

		if (rate1.doubleValue() != 0.0)	//	no divide by zero
			rate2 = one.divide(rate1, 12, BigDecimal.ROUND_HALF_UP);
		//
		if (field.getColumnName().equals("MultiplyRate"))
			retValue.addChangedValue("DivideRate", field.convertToString(rate2));
		else
			retValue.addChangedValue("MultiplyRate", field.convertToString(rate2));
		log.info(field.getColumnName() + "=" + rate1 + " => " + rate2);
		return retValue;
	}	//	rate

	/**
	 *  Set Account Date to the date of the calling column.
	 * 	org.compiere.controller.UICalloutEngine.dateAcct
	 *	@param ctx context
	 *	@param windowNo window no
	 *	@param field field
	 *	@param oldValue old
	 *	@param newValue new
	 *	@return changes or null
	 */
	public ChangeVO dateAcct (Ctx ctx, int windowNo, UIField field, String oldValue, String newValue)
	{
		if (newValue == null || newValue.length() == 0)
			return null;
		Timestamp value = field.convertToTimestamp(newValue);
		if (value == null)	//	cannot convert
			return null;

		ChangeVO retValue = new ChangeVO();
		retValue.addChangedValue("DateAcct", newValue);
		log.info("DateAcct=" + value);
		return retValue;
	}	//	dateAcct


}	//	UICalloutEngine
