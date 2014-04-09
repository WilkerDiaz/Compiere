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
package org.compiere.process;

import java.lang.reflect.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Un-install component
 *	
 *  @author William Wong
 *  @version $Id$
 */
public class ComponentRemove extends SvrProcess
{
	/** Entity Type			*/
	protected String	p_entityType = null;
	/**	Delete record cascade 	*/
	protected Boolean	p_isCascade	= Boolean.FALSE;
	/**	Delete component war/ear	*/
	protected Boolean 	p_deleteFile	= Boolean.TRUE;
	
	/**
	 * 	Prepare
	 */
	@Override
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("EntityType"))
				p_entityType = (String)element.getParameter();
			else if (name.equals("DeleteRecord"))
				p_isCascade = "Y".equals(element.getParameter());
			else if (name.equals("DeleteDeploy"))
				p_deleteFile = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@return summary
	 *	@throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("EntityType=" + p_entityType);
		if (p_entityType == null || p_entityType.length() < 4)
			throw new CompiereUserException("You cannot remove component: " + p_entityType);
		
		MEntityType et = MEntityType.getEntityType(getCtx(), p_entityType);
		if (et == null)
			throw new CompiereUserException("Entity type not defined: " + p_entityType);
		
		//	Call
		Class<?>[] parameterTypes = new Class[]{
			String.class, Boolean.class, Boolean.class
		};
		Object[] args = new Object[]{
				p_entityType, p_isCascade, p_deleteFile
		};
		Object info = null;
		try
		{
			Class<?> clazz = Class.forName("com.compiere.client.StartComponent");
			Object instance = clazz.newInstance();
			Method method = clazz.getMethod ("removeComponent", parameterTypes);
			info = method.invoke (instance, args);
		}
		catch (Exception e)
		{
			throw new CompiereSystemException(e.toString());
		}
		
		if (info != null)
			return info.toString();
		throw new CompiereSystemException("Please check Log");
	}	//	doIt

}	//	ComponentRemove

