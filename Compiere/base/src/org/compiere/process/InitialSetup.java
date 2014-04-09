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

import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.print.*;
import org.compiere.util.*;


/**
 * 	Initial Tenant Setup
 * 	@author Jorg Janke
 */
public class InitialSetup extends SvrProcess
{
	private String		p_ClientName = null;
	private String		p_OrgName = null;
	private int			p_C_Currency_ID = 0;
	private int			p_C_Country_ID = 0;
	private int			p_C_Region_ID = 0;
	private String		p_City = null;
	private boolean		p_IsBPartnerAcctElement = true;
	private boolean		p_IsProductAcctElement = true;
	private boolean		p_IsProjectAcctElement = false;
	private boolean		p_IsCampaignAcctElement = false;
	private boolean		p_IsSalesRegionAcctElement = false;
	private String		p_AccountsFileName = null;

	/**
	 * 	Get Parameters
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para)
		{
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("ClientName"))
				p_ClientName = (String)element.getParameter();
			else if (name.equals("OrgName"))
				p_OrgName = (String)element.getParameter();
			else if (name.equals("C_Currency_ID"))
				p_C_Currency_ID = element.getParameterAsInt();
			else if (name.equals("C_Country_ID"))
				p_C_Country_ID = element.getParameterAsInt();
			else if (name.equals("C_Region_ID"))
				p_C_Region_ID = element.getParameterAsInt();
			else if (name.equals("City"))
				p_City = (String)element.getParameter();

			else if (name.equals("IsBPartnerAcctElement"))
				p_IsBPartnerAcctElement = "Y".equals(element.getParameter());
			else if (name.equals("IsProductAcctElement"))
				p_IsProductAcctElement = "Y".equals(element.getParameter());
			else if (name.equals("IsProjectAcctElement"))
				p_IsProjectAcctElement = "Y".equals(element.getParameter());
			else if (name.equals("IsCampaignAcctElement"))
				p_IsCampaignAcctElement = "Y".equals(element.getParameter());
			else if (name.equals("IsSalesRegionAcctElement"))
				p_IsSalesRegionAcctElement = "Y".equals(element.getParameter());

			else if (name.equals("AccountsFileName"))
				p_AccountsFileName = (String)element.getParameter();

			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Perform Process
	 */
	@Override
	protected String doIt() throws Exception
	{
		if (!Util.isEmpty(p_AccountsFileName)
			&& !p_AccountsFileName.toLowerCase().endsWith(".csv"))
			throw new CompiereUserException("Require .csv file: " + p_AccountsFileName);
		//
		MSetup ms = new MSetup();
		//  Step 1 - Client
		boolean ok = ms.createClient(p_ClientName, p_OrgName, null, null);
		String info = ms.getInfo();
		StringTokenizer st = new StringTokenizer(info, "\n");
		while (st.hasMoreTokens())
			addLog(st.nextToken());
		if (!ok)
			throw new CompiereSystemException("@AccountSetupError@ - " + info);

		//	Step 2 - Generate Accounting
		ok = ms.createAccounting(p_C_Country_ID, p_C_Currency_ID,
			p_IsProductAcctElement, p_IsBPartnerAcctElement, p_IsProjectAcctElement,
			p_IsCampaignAcctElement, p_IsSalesRegionAcctElement,
			p_AccountsFileName);
		String s = ms.getInfo();
		st = new StringTokenizer(s, "\n");
		while (st.hasMoreTokens())
			addLog(st.nextToken());
		info += s;
		if (!ok)
			throw new CompiereSystemException("@AccountSetupError@ - " + s);

		//  Step 3 - Generate Entities
		ok = ms.createEntities(p_City, p_C_Region_ID);
		s = ms.getInfo();
		st = new StringTokenizer(s, "\n");
		while (st.hasMoreTokens())
			addLog(st.nextToken());
		info += s;
		if (!ok)
			throw new CompiereSystemException("@Error@ - " + s);

		//	Step 4 - Create Print Documents
		PrintUtil.setupPrintForm(ms.getAD_Client_ID());

		//	Reset
		MSetup.resetServer();

		return "@AD_User_ID@ : " + ms.getUserName() + "/" + ms.getUserName();
	}	//	doIt

}	//	InitialSetup
