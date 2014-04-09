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
package org.compiere.util;


/**
 *	Compiere Data Value Exception
 *	
 *  @author Jorg Janke
 *  @version $Id: CompiereValueException.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CompiereValueException extends IllegalArgumentException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Create Message
	 *	@param ColumnName column
	 *	@param Value new value
	 *	@param message error message
	 *	@return message
	 */
	private static String message (String ColumnName, Object Value, String message)
	{
		StringBuffer sb = new StringBuffer(ColumnName);
		if (Value == null)
			sb.append("=NULL");
		else if (Value instanceof String)
			sb.append("='").append(Value).append("'");
		else
			sb.append("=").append(Value);
		sb.append(": ").append(message);
		return sb.toString();
	}	//	message
	
	/**
	 * 	Compiere Value Exception
	 *	@param ColumnName column
	 *	@param Value new value
	 *	@param message error message
	 */
	public CompiereValueException (String ColumnName, Object Value, String message)
	{
		super(message(ColumnName, Value, message));
	}	//	CompiereValueException
	
}	//	CompiereValueException
