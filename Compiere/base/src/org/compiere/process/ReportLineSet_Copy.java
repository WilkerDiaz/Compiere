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

import org.compiere.report.*;

/**
 *	Copy Line Set at the end of the Line Set
 *
 *  @author Jorg Janke
 *  @version $Id: ReportLineSet_Copy.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class ReportLineSet_Copy extends SvrProcess
{
	/**
	 * 	Constructor
	 */
	public ReportLineSet_Copy()
	{
		super();
	}	//	ReportLineSet_Copy

	/**	Source Line Set					*/
	private int		m_PA_ReportLineSet_ID = 0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("PA_ReportLineSet_ID"))
				m_PA_ReportLineSet_ID = ((BigDecimal)element.getParameter()).intValue();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		int to_ID = super.getRecord_ID();
		log.info("From PA_ReportLineSet_ID=" + m_PA_ReportLineSet_ID + ", To=" + to_ID);
		if (to_ID < 1)
			throw new Exception(MSG_SaveErrorRowNotFound);
		//
		MReportLineSet to = new MReportLineSet(getCtx(), to_ID, get_TrxName());
		MReportLineSet rlSet = new MReportLineSet(getCtx(), m_PA_ReportLineSet_ID, get_TrxName());
		MReportLine[] rls = rlSet.getLiness();
		for (MReportLine element : rls) {
			MReportLine rl = MReportLine.copy (getCtx(), to.getAD_Client_ID(), to.getAD_Org_ID(), to_ID, element, get_TrxName());
			rl.save();
			MReportSource[] rss = element.getSources();
			if (rss != null)
			{
				for (MReportSource element2 : rss) {
					MReportSource rs = MReportSource.copy (getCtx(), to.getAD_Client_ID(), to.getAD_Org_ID(), rl.get_ID(), element2, get_TrxName());
					rs.save();
				}
			}
			//	Oper 1/2 were set to Null ! 
		}
		return "@Copied@=" + rls.length;
	}	//	doIt

}	//	ReportLineSet_Copy
