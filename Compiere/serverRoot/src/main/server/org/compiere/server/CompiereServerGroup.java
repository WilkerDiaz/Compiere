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
package org.compiere.server;

import org.compiere.util.*;

/**
 *	Compiere Server Group
 *	
 *  @author Jorg Janke
 *  @version $Id: CompiereServerGroup.java 7514 2009-04-20 21:57:40Z freyes $
 */
public class CompiereServerGroup extends ThreadGroup
{
	/**
	 * 	Get Compiere Server Group
	 *	@return Server Group
	 */
	public static CompiereServerGroup get()
	{
		if (s_group == null || s_group.isDestroyed())
			s_group = new CompiereServerGroup(); 
		return s_group;
	}	//	get
	
	/** Group */
	private static CompiereServerGroup	s_group	= null;
	
	/**
	 * 	CompiereServerGroup
	 */
	private CompiereServerGroup ()
	{
		super ("CompiereServers");
		setDaemon(true);
		setMaxPriority(Thread.MAX_PRIORITY);
		log.info(getName() + " - Parent=" + getParent());
	}	//	CompiereServerGroup

	/**	Logger			*/
	protected CLogger	log = CLogger.getCLogger(getClass());
	
	/**
	 * 	Uncaught Exception
	 *	@param t thread
	 *	@param e exception
	 */
	@Override
	public void uncaughtException (Thread t, Throwable e)
	{
		log.info ("uncaughtException = " + e.toString());
		super.uncaughtException (t, e);
	}	//	uncaughtException
	
	/**
	 * 	String Representation
	 *	@return name
	 */
	@Override
	public String toString ()
	{
		return getName();
	}	//	toString

	/**
	 * 	Dump Info
	 */
	public void dump ()
	{
		log.fine(getName() + (isDestroyed() ? " (destroyed)" : ""));
		log.fine("- Parent=" + getParent());
		Thread[] list = new Thread[activeCount()];
		log.fine("- Count=" + enumerate(list, true));
		for (int i = 0; i < list.length; i++)
			log.fine("-- " + list[i]);
	}	//	dump

}	//	CompiereServerGroup
