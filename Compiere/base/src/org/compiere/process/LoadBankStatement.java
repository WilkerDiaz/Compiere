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

import java.math.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Process for loading Bank Statements into I_BankStatement
 *
 *	@author Maarten Klinker, Eldir Tomassen
 *	@version $Id: LoadBankStatement.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class LoadBankStatement extends SvrProcess
{
	public LoadBankStatement()
	{
		super();
		log.info("LoadBankStatement");
	}	//	LoadBankStatement

	/**	Client to be imported to			*/
	private int				m_AD_Client_ID = 0;

	/** Organization to be imported to			*/
	private int				m_AD_Org_ID = 0;
	
	/** Ban Statement Loader				*/
	private int m_C_BankStmtLoader_ID = 0;

	/** File to be imported					*/
	private String fileName = "";
	
	/** Current context					*/
	private Ctx				m_ctx;
	
	/** Current context					*/
	private MBankStatementLoader m_controller = null;
	

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		log.info("");
		m_ctx = Env.getCtx();
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (name.equals("C_BankStatementLoader_ID"))
				m_C_BankStmtLoader_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("FileName"))
				fileName = (String)element.getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		m_AD_Client_ID = m_ctx.getAD_Client_ID();
		log.info("AD_Client_ID=" + m_AD_Client_ID);
		m_AD_Org_ID = m_ctx.getAD_Org_ID();
		log.info("AD_Org_ID=" + m_AD_Org_ID);
		log.info("C_BankStatementLoader_ID=" + m_C_BankStmtLoader_ID);
	}	//	prepare


	/**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception
	 */
	@Override
	protected String doIt() throws java.lang.Exception
	{
		log.info("LoadBankStatement.doIt");
		String message = "@Error@";
		
		m_controller = new MBankStatementLoader(m_ctx, m_C_BankStmtLoader_ID, fileName, get_TrxName());
		log.info(m_controller.toString());
		
		if (m_controller == null || m_controller.get_ID() == 0)
			log.log(Level.SEVERE, "Invalid Loader");

		// Start loading bank statement lines
		else if (!m_controller.loadLines())
			log.log(Level.SEVERE, m_controller.getErrorMessage() + " - " + m_controller.getErrorDescription());
		
		else
		{
			log.info("Imported=" + m_controller.getLoadCount());
			addLog (0, null, new BigDecimal (m_controller.getLoadCount()), "@Loaded@");
			message = "@OK@";
		}

		return message;
	}	//	doIt

}	//	LoadBankStatement
