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

import java.util.logging.*;

import org.compiere.*;
import org.compiere.apps.*;
import org.compiere.model.*;
import org.compiere.process.*;

/**
 * To uninstall component
 * @author wwong
 *
 */
public class UninstallComponent 
{
	private static void printUsage(String[] args)
	{
		System.out.println("Usage: "+ args[0] + " -E <Entity Type>");
		System.out.println("               -R <Delete Record>");
		System.out.println("               -F <Delete Deployment File>");
	}  // printUsage()
	
	public static void main(String[] args) 
	{
		Compiere.startup(true, false, "UninstallComponent");
		CLogMgt.setLevel(Level.FINE);
		
		if (args.length < 2 || args.length > 6)
		{
			System.out.println("Invalid arguments");
			printUsage(args);
			return;
		}
		String entityType = null;		// Entity type
		String deleteRecord = "N";		// Whether delete record from db, default = No
		String deleteFile = "Y";		// Whether delete deployment file, default = Yes
		
		for (int i = 0; i < args.length; i++)
		{
			if (args[i].compareTo("-E") == 0)
				entityType = args[++i];
			else if (args[i].compareTo("-R") == 0)
				deleteRecord = args[++i];
			else if (args[i].compareTo("-F") == 0)
				deleteFile = args[++i];
			else 
			{
				System.out.println("Invalid option: " + args[i]);
				printUsage(args);
				return;
			}
		}
		
		if (entityType == null)
		{
			System.out.println("No EntityType input");
			printUsage(args);
			return;
		}
		
		System.out.println("Command: UninstallComponent -E " + entityType + 
											" -R " + deleteRecord + 
											" -F " + deleteFile);
		
		int AD_Process_ID = 382;	//	Component Uninstall
		
		Ctx ctx = Env.getCtx();

		MSession session = MSession.get(ctx);
		if (session == null){
			//	Create Session
			MSession.get(Env.getCtx(), X_AD_Session.SESSIONTYPE_Other, true, null, null, "Uninstall");
		}

		//	Create Process Instance
		MPInstance instance = new MPInstance(ctx, AD_Process_ID, 0);
		instance.save();
		MPInstancePara para1 = new MPInstancePara(instance, 10);
		para1.setParameter("EntityType", entityType);
		para1.save();
		MPInstancePara para2 = new MPInstancePara(instance, 20);
		para2.setParameter("DeleteRecord", deleteRecord);
		para2.save();
		MPInstancePara para3 = new MPInstancePara(instance, 30);
		para3.setParameter("DeleteDeploy", deleteFile);
		para3.save();

		//	Process Info
		ProcessInfo pi = new ProcessInfo("Uninstall Component", AD_Process_ID);
		pi.setIsBatch(false);	//	want to get result
		pi.setAD_PInstance_ID (instance.getAD_PInstance_ID());
		
		ProcessCtl worker = new ProcessCtl(null, pi, null);
		worker.start();
		try
		{
			while (worker.isAlive())
			{
				Thread.sleep(500);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if (pi.isError())
			System.err.println("Error: " + pi.getSummary());
		else
			System.out.println("OK: " + pi.getSummary());
		System.out.println(pi.getLogInfo());
	}  // main()
}  // UninstallComponent
