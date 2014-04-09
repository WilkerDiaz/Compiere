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

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.*;
import org.compiere.model.*;

/**
 * Utility function to create component CAR file
 * @author wwong
 *
 */
public class CreateComponent 
{
	private static void printUsage(String[] args)
	{
		System.out.println("Usage: CreateComponent -E <Entity Type>");
		System.out.println("                       -C <Jar Path>");
	}  // printUsage()
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// All compiere client has to do this
		Compiere.startup(true, false, "CreateComponent");
		CLogMgt.setLevel(Level.INFO);
		CLogErrorBuffer.get(false).setIssueError(false);
		
		if (args.length < 4)
		{
			System.out.println("Invalid arguments");
			printUsage(args);
			return;
		}
		
		String entityType = null;		// Entity type
		String jarDir = null;			// Jar directory
		String targetDir = null;		// Directory to create the CAR
		
		for (int i = 0; i < args.length; i++)
		{
			if (args[i].compareTo("-E") == 0)
				entityType = args[++i];
			else if (args[i].compareTo("-C") == 0)
				jarDir = args[++i];
			else if (args[i].compareTo("-D") == 0)
				targetDir = args[++i];
			else 
			{
				System.out.println("Invalid option: " + args[i]);
				printUsage(args);
				System.exit(-1);
			}
		}
		
		if (jarDir != null && !File.separator.equals("/"))
			while (jarDir.indexOf("/") > -1)
				jarDir = jarDir.replace("/", File.separator);			

		if (targetDir == null)
			targetDir = Ini.getCompiereHome() + File.separator + "data";
		else 
			while (targetDir.indexOf("/") > -1 && !File.separator.equals("/"))
				targetDir = targetDir.replace("/", File.separator);	
		
		MEntityType et = MEntityType.getEntityType(Env.getCtx(), entityType);
		if (et == null)
		{
			System.out.println("Entity Type not exists: " + entityType);
			System.exit(-1);
		}
		
		String EntityType = et.getEntityType();
		if (EntityType.equals("D") || EntityType.equals("C"))
		{
			System.out.println("You cannot create a Dictionary Component");
			System.exit(-1);
		}
		
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
			String fileName = jarDir + File.separator + jarName;
			File file = new File(fileName);
			if (file.exists() && file.isFile())
				;
			else
			{
				System.out.println("Cannot find: " + fileName);
				//System.exit(-1);
			}
			jars.add(fileName);
			if (!first)
				sb.append(",");
			else
				first = false;
			sb.append(fileName);
		}
		System.out.println("Deploy file: " + sb.toString());
		
		//	Call
		Class<?>[] parameterTypes = new Class[]{
			String.class, List.class, String.class
		};
		Object[] margs = new Object[]{
			EntityType, jars, targetDir
		};
		Object info = null;
		try
		{
			Class<?> clazz = Class.forName("com.compiere.client.StartComponent");
			Object instance = clazz.newInstance();
			Method method = clazz.getMethod ("createComponent", parameterTypes);
			info = method.invoke (instance, margs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if (info == null)
			return;
		
		String retVal = (String)info;
		System.out.println(retVal);
		if (retVal.startsWith("Error"))
			System.exit(-1);
		return;
	}  // main()
}  // CreateComponent
