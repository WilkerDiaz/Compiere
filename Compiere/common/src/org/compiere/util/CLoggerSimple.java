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
 *	Compiere Simple Logger.
 *	Print to System.out/err	
 *	
 *  @author Jorg Janke
 *  @version $Id$
 */
public class CLoggerSimple
	implements CompiereLogger
{
	/**
	 * 	Simple Compiere Logger.
	 * 	No Prefix
	 */
	public CLoggerSimple()
	{
	}	//	CLoggerSimple
	
	/**
	 * 	Simple Compiere Logger
	 *	@param prefix prefix
	 */
	public CLoggerSimple(String prefix)
	{
		if (prefix != null && prefix.length() > 0)
			m_prefix = prefix;
	}	//	CLoggerSimple

	/**
	 * 	Simple Compiere Logger
	 *	@param clazz prefix
	 */
	public CLoggerSimple(Class<?> clazz)
	{
		String prefix = clazz.toString();
		int index = prefix.lastIndexOf('.');
		if (index != -1)
			prefix = prefix.substring(index);
		m_prefix = prefix;
	}	//	CLoggerSimple
	
	/**	Prefix				*/
	private String 		m_prefix = null;
	
	/**
	 * 	severe
	 *	@param msg
	 */
	public void severe(String msg)
	{
		print (msg, true);
	}

	/**
	 * 	warning
	 *	@param msg
	 */
	public void warning(String msg)
	{
		print (msg, false);
	}

	/**
	 * 	info
	 *	@param msg
	 */
	public void info(String msg)
	{
		print (msg, 1); 
	}

	/**
	 * 	config
	 *	@param msg
	 */
	public void config(String msg)
	{
		print (msg, 2); 
	}

	/**
	 * 	fine
	 *	@param msg
	 */
	public void fine(String msg)
	{
		print (msg, 3); 
	}

	/**
	 * 	finer
	 *	@param msg
	 */
	public void finer(String msg)
	{
		print (msg, 4); 
	}

	/**
	 * 	finest
	 *	@param msg
	 */
	public void finest(String msg)
	{
		print (msg, 5); 
	}

	/**
	 * 	Print Error
	 *	@param msg message
	 *	@param error true if error otherwise warning
	 */
	private void print (String msg, boolean error)
	{
		StringBuffer sb = new StringBuffer();
		if (m_prefix != null)
			sb.append (m_prefix).append (": ");
		sb.append(" ")
			.append (msg);
		System.err.println(sb.toString());
	}	//	print

	/**
	 * 	Print
	 *	@param msg
	 *	@param noSpaces
	 */
	private void print (String msg, int noSpaces)
	{
		StringBuffer sb = new StringBuffer();
		if (m_prefix != null)
			sb.append (m_prefix).append (": ");
		sb.append("                          ".subSequence (0, noSpaces))
			.append (msg);
		System.out.println(sb.toString());
	}	//	print
	
	/**
	 * 	isLevelFinest
	 *	@return
	 */
	public boolean isLevelFinest()
	{
		return false;
	}

}	//	CLoggerSimple
