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

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Entity Type Package Component
 *	
 *  @author Jorg Janke
 *  @version $Id$
 */
public class EntityTypeComponent extends SvrProcess
{
	/** Entity Type			*/
	protected int		p_AD_EntityType_ID = 0;
	/** Directory			*/
	protected String 	p_directory = null;
	
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
			else if (name.equals("JarDirectory"))
				p_directory = (String)element.getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_AD_EntityType_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return summary
	 *	@throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("AD_EntityType_ID=" + p_AD_EntityType_ID 
			+ ", JarDirectory=" + p_directory);
		MEntityType et = MEntityType.getEntityType(getCtx(), p_AD_EntityType_ID);
		String EntityType = et.getEntityType();
		if (EntityType.equals("D"))
			throw new CompiereUserException("You cannot create a Dictionary Component");
		
		//	Directory
		if (p_directory == null || p_directory.length() == 0)
			p_directory = "";	//	current
		else if (!p_directory.endsWith("/") && !p_directory.endsWith("\\"))
				p_directory += File.separator;
		
		//	Check Jar Files
		boolean forWindows = Env.isWindows();
		String classPath = et.getClasspath(forWindows);
		StringTokenizer st = new StringTokenizer(classPath, ",;: ", false);
		List<String> jars = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		while (st.hasMoreTokens())
		{
			String jarName = st.nextToken();
			if (jarName.length() == 0)
				continue;
			String fileName = p_directory + jarName;
			File file = new File(fileName);
			if (file.exists() && file.isFile())
				;
			else
				throw new CompiereUserException("Cannot find: " + fileName);
			jars.add(fileName);
			if (!first)
				sb.append(",");
			else
				first = false;
			sb.append(fileName);
		}
		log.info("Deploy file: " + sb.toString());
		
		//	Call
		Class<?>[] parameterTypes = new Class[]{
			String.class, List.class, String.class
		};
		String targetDir = Ini.getCompiereHome() + File.separator + "data";
		Object[] args = new Object[]{EntityType, jars, targetDir};
		Object info = null;
		try
		{
			Class<?> clazz = Class.forName("com.compiere.client.StartComponent");
			Object instance = clazz.newInstance();
			Method method = clazz.getMethod ("createComponent", parameterTypes);
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

}	//	EntityTypeComponent
