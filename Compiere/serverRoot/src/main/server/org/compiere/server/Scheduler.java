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

import java.sql.*;
import java.util.logging.*;

import org.compiere.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	The Scheduler Process
 *
 *  @author Jorg Janke
 *  @version $Id: Scheduler.java 7514 2009-04-20 21:57:40Z freyes $
 */
public class Scheduler extends CompiereServer
{
	/**
	 * 	Scheduler
	 *	@param model model
	 */
	public Scheduler (MScheduler model)
	{
		super (model, 240);		//	nap
		m_model = model;
	//	m_client = MClient.get(model.getCtx(), model.getAD_Client_ID());
	}	//	Scheduler

	/**	The Concrete Model			*/
	private MScheduler			m_model = null;
	/**	Last Summary				*/
	private StringBuffer 		m_summary = new StringBuffer();
	/** Transaction					*/
	private Trx					m_trx = null;

	/**
	 * 	Work
	 */
	@Override
	protected void doWork ()
	{
		m_summary = new StringBuffer(m_model.toString())
			.append(" - ");
		MProcess process = m_model.getProcess();
		//
		try
		{
			//	Explicitly set Environment
			Ctx ctx = m_model.getCtx();
			ctx.setAD_Client_ID(m_model.getAD_Client_ID());
			ctx.setContext("AD_Client_ID", m_model.getAD_Client_ID());
			ctx.setAD_Org_ID(m_model.getAD_Org_ID());
			ctx.setContext("AD_Org_ID", m_model.getAD_Org_ID());
			ctx.setAD_User_ID(m_model.getUpdatedBy());
			ctx.setContext("AD_User_ID", m_model.getUpdatedBy());
			ctx.setContext("#SalesRep_ID", m_model.getUpdatedBy());
			// Agregar variables Globales de XX_VSI_KeyNameInfo a contexto de los
			// procesos del manejador de Tareas
			Login.cargaID(ctx);
			//
			m_trx = Trx.get("Scheduler");
			String result = m_model.execute(m_trx);
			m_summary.append(result);
			m_trx.commit();
		}
		catch (Exception e)
		{
			if (m_trx != null)
				m_trx.rollback();
			log.log(Level.WARNING, process.toString(), e);
			m_summary.append(e.toString());
		}
		if (m_trx != null)
			m_trx.close();
		//
		int no = m_model.deleteLog();
		m_summary.append("Logs deleted=").append(no);
		//
		MSchedulerLog pLog = new MSchedulerLog(m_model, m_summary.toString());
		pLog.setReference("#" + String.valueOf(p_runCount)
			+ " - " + TimeUtil.formatElapsed(new Timestamp(p_startWork)));
		pLog.save();
	}	//	doWork

	/**
	 * 	Get Server Info
	 *	@return info
	 */
	@Override
	public String getServerInfo()
	{
		return "#" + p_runCount + " - Last=" + m_summary.toString();
	}	//	getServerInfo

	/**
	 * 	Test
	 *	@param args ignored
	 */
	public static void main(String[] args)
    {
		Compiere.startup(true);
		CLogMgt.setLevel(Level.FINER);
		CompiereServer server = null;
		MScheduler[] schedulerModels = MScheduler.getActive(Env.getCtx());
		for (int i = 0; i < schedulerModels.length; i++)
		{
			MScheduler pModel = schedulerModels[i];
			server = CompiereServer.create(pModel);
			server.start();
		}
		//	Wait
		try
		{
			server.join();
		}
		catch (Exception e)
		{
		}
    }	//	main

}	//	Scheduler
