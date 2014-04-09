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
package org.compiere.print;

import java.math.*;
import java.lang.reflect.Constructor;
import java.util.logging.Level;

import org.compiere.process.*;

/**
 *	MPrintFormat Process.
 *  Performs Copy existing or Create from Table
 *  Called when pressing the Copy/Create button in Window Print Format
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MPrintFormatProcess.java,v 1.3 2006/07/30 00:53:02 jjanke Exp $
 */
public class MPrintFormatProcess extends SvrProcess
{
    /** Logger for class MPrintFormatProcess */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPrintFormatProcess.class);
	/** PrintFormat             */
	private BigDecimal	m_AD_PrintFormat_ID;
	/** Table	                */
	private BigDecimal	m_AD_Table_ID;
	/** Business View	                */
	private BigDecimal	m_AD_BView_ID;

	/**
	 *  Prepare - get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("AD_PrintFormat_ID"))
				m_AD_PrintFormat_ID = ((BigDecimal)element.getParameter());
			else if (name.equals("AD_Table_ID"))
				m_AD_Table_ID = ((BigDecimal)element.getParameter());
			else if (name.equals("AD_BView_ID"))
				m_AD_BView_ID = ((BigDecimal)element.getParameter());
			else
				log.equals("Unknown Parameter=" + element.getParameterName());
		}
	}   //  prepare

	/**
	 *  Perform process.
	 *  <pre>
	 *  If AD_Table_ID is not null, create from table,
	 *  otherwise copy from AD_PrintFormat_ID
	 *  </pre>
	 * @return Message
	 * @throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		if (m_AD_Table_ID != null && m_AD_Table_ID.intValue() > 0)
		{
			log.info("Create from AD_Table_ID=" + m_AD_Table_ID);
			MPrintFormat pf = MPrintFormat.createFromTable(getCtx(), m_AD_Table_ID.intValue(), getRecord_ID());
			if(pf != null)
			{
				addLog(m_AD_Table_ID.intValue(), null, new BigDecimal(pf.getItemCount()), pf.getName());
				return pf.getName() + " #" + pf.getItemCount();
			}
			else
				throw new Exception (MSG_PrintFormatNotCreated);
		}
		else if (m_AD_PrintFormat_ID != null && m_AD_PrintFormat_ID.intValue() > 0)
		{
			log.info("Copy from AD_PrintFormat_ID=" + m_AD_PrintFormat_ID);
			MPrintFormat pf = MPrintFormat.copy (getCtx(), m_AD_PrintFormat_ID.intValue(), getRecord_ID());
			if(pf != null)
			{
				addLog(m_AD_PrintFormat_ID.intValue(), null, new BigDecimal(pf.getItemCount()), pf.getName());
				return pf.getName() + " #" + pf.getItemCount();
			}
			else
				throw new Exception (MSG_PrintFormatNotCreated);
				
		}
		else if (m_AD_BView_ID != null && m_AD_BView_ID.intValue() > 0){
			MPrintFormat pf =null;			
			Constructor<?> intArgsConstructor;
				try
				{
					Class<?> myClass = Class.forName("org.compiere.eul.print.MBViewPrintFormat");
					Class<?>[] intArgsClass = new Class[] {org.compiere.util.Ctx.class, int.class, int.class };
					Object[] intArgs = new Object[] {getCtx(),m_AD_BView_ID.intValue(),getRecord_ID() };
						
					
				      intArgsConstructor = myClass.getConstructor(intArgsClass);
				      pf = (MPrintFormat)  intArgsConstructor.newInstance(intArgs);
				}
				catch (Exception e)
				{
					
					log.log(Level.SEVERE, "", e);
				}
			addLog(m_AD_BView_ID.intValue(), null, new BigDecimal(pf.getItemCount()), pf.getName());
			return pf.getName() + " #" + pf.getItemCount();
		}
		else
			throw new Exception (MSG_InvalidArguments);
	}	//	doIt

}	//	MPrintFormatProcess
