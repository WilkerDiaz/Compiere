package org.compiere.test;

import java.util.logging.*;

import org.compiere.*;
import org.compiere.apps.*;
import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.*;

/**
 *	Example Command Line Start 	
 * 	@author Jorg Janke
 */
public class CmdLineImport 
{

	/**
	 * 	Example Implementation how to start a process from command line
	 *	@param args ignored
	 */
	public static void main(String[] args) 
	{
		Compiere.startup(true);
		CLogMgt.setLevel(Level.FINE);

		int AD_Client_ID = 11;		//	GardenWorld
		int AD_Org_ID = 11;			//	HQ
		int AD_User_ID = 101;		//	GardenAdmin
		int AD_Process_ID = 207;	//	Import Invoice
		int AD_Table_ID = 0;		//	Used for Button
		int Record_ID = 0;			//	Used for Button

		//	Set Environment
		Ctx ctx = Env.getCtx();
		ctx.setAD_Client_ID(AD_Client_ID);
		ctx.setAD_Org_ID(AD_Org_ID);
		ctx.setAD_User_ID(AD_User_ID);

		//	Create Process Instance
		MPInstance instance = new MPInstance(ctx, AD_Process_ID, Record_ID);
		instance.save();
		MPInstancePara para1 = new MPInstancePara(instance, 10);
		para1.setParameter("AD_Client_ID", AD_Client_ID);
		para1.save();
		MPInstancePara para2 = new MPInstancePara(instance, 20);
		para2.setParameter("AD_Org_ID", AD_Org_ID);
		para2.save();
		MPInstancePara para3 = new MPInstancePara(instance, 30);
		para3.setParameter("DocAction", X_C_Invoice.DOCACTION_Complete);
		para3.save();
		MPInstancePara para4 = new MPInstancePara(instance, 40);
		para4.setParameter("DeleteOldImported", "N");
		para4.save();
		
		//	Process Info
		ProcessInfo pi = new ProcessInfo("Title", AD_Process_ID, AD_Table_ID, Record_ID);
		pi.setAD_Client_ID(AD_Client_ID);
		pi.setAD_User_ID(AD_User_ID);
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
	}	//	main


}	//	CmdLineImport
