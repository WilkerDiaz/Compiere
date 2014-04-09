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
package org.compiere.util;



/**
 * 	Compiere System Error.
 * 	Error caused by invalid configurations, etc.
 * 	(No program error)
 *	
 *  @author Jorg Janke
 *  @version $Id: CompiereSystemException.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CompiereSystemException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Compiere System Error
	 *	@param message message
	 */
	public CompiereSystemException (String message)
	{
		super (message);
	}	//	CompiereSystemError

	/**
	 * 	Compiere System Error
	 *	@param message message
	 *	@param detail detail
	 */
	public CompiereSystemException (String message, Object detail)
	{
		super (message);
		setDetail (detail);
	}	//	CompiereSystemError

	/**
	 * 	Compiere System Error
	 *	@param message
	 *	@param cause
	 */
	public CompiereSystemException (String message, Throwable cause)
	{
		super (message, cause);
	}	//	CompiereSystemError

	/**	Details					*/
	private Object	m_detail = null;
	
	/**
	 * @return Returns the detail.
	 */
	public Object getDetail ()
	{
		return m_detail;
	}
	
	/**
	 * @param detail The detail to set.
	 */
	public void setDetail (Object detail)
	{
		m_detail = detail;
	}
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		super.toString();
		StringBuffer sb = new StringBuffer ("SystemError: ");
		sb.append(getLocalizedMessage());
		if (m_detail != null)
			sb.append(" (").append(m_detail).append (")");
		return sb.toString ();
	}	//	toString

}	//	CompiereSystemError
