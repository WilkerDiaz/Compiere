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
 *	Compiere User Error.
 *	Cuased by (lack of) user input/selection.
 * 	(No program error)
 *	
 *  @author Jorg Janke
 *  @version $Id: CompiereUserException.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CompiereUserException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Compiere User Error
	 *	@param message message
	 */
	public CompiereUserException (String message)
	{
		super (message);
	}	//	CompiereUserError

	/**
	 * 	Compiere User Error
	 *	@param message message
	 *	@param fixHint fix hint
	 */
	public CompiereUserException (String message, String fixHint)
	{
		super (message);
		setFixHint(fixHint);
	}	//	CompiereUserError

	/**
	 * 	CompiereUserError
	 *	@param message
	 *	@param cause
	 */
	public CompiereUserException (String message, Throwable cause)
	{
		super (message, cause);
	}	//	CompiereUserError

	/**	Additional Info how to fix	**/
	private String	m_fixHint = null;
	
	/**
	 * @return Returns the fixHint.
	 */
	public String getFixHint ()
	{
		return m_fixHint;
	}	//	getFixHint
	
	/**
	 * 	Set Fix Hint
	 *	@param fixHint fix hint
	 */
	public void setFixHint (String fixHint)
	{
		m_fixHint = fixHint;
	}	//	setFixHint
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		super.toString();
		StringBuffer sb = new StringBuffer ("UserError: ");
		sb.append(getLocalizedMessage());
		if (m_fixHint != null && m_fixHint.length() > 0)
			sb.append(" (").append(m_fixHint).append (")");
		return sb.toString ();
	}	//	toString
	
}	//	CompiereUserError
