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
package org.compiere.impexp;

import java.math.*;
import java.util.logging.*;

import org.compiere.process.*;


/**
 *	Copy Import Format (lines)
 *	
 *  @author Jorg Janke
 *  @version $Id: CopyImportFormat.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class CopyImportFormat extends SvrProcess
{
	private int from_AD_ImpFormat_ID = 0;
	private int to_AD_ImpFormat_ID = 0;
	
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
			else if (name.equals("AD_ImpFormat_ID"))
				from_AD_ImpFormat_ID = ((BigDecimal)element.getParameter()).intValue();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		to_AD_ImpFormat_ID = getRecord_ID();
	}	//	prepare

	
	/**
	 * 	Process Copy
	 *	@return info
	 *	@throws Exception
	 */
	@Override
	protected String doIt () throws Exception
	{
		log.info("doIt = From=" + from_AD_ImpFormat_ID + " To=" + to_AD_ImpFormat_ID);
		MImpFormat from = new MImpFormat (getCtx(), from_AD_ImpFormat_ID, get_TrxName());
		if (from.getAD_ImpFormat_ID() != from_AD_ImpFormat_ID)
			throw new Exception ("From Format not found - " + from_AD_ImpFormat_ID);
		//
		MImpFormat to = new MImpFormat (getCtx(), to_AD_ImpFormat_ID, get_TrxName());
		if (to.getAD_ImpFormat_ID() != to_AD_ImpFormat_ID)
			throw new Exception ("To Format not found - " + from_AD_ImpFormat_ID);
		//
		if (from.getAD_Table_ID() != to.getAD_Table_ID())
			throw new Exception ("From-To do Not have same Format Table");
		//
		MImpFormatRow[] rows = from.getRows();	//	incl. inactive
		for (MImpFormatRow row : rows) {
			MImpFormatRow copy = new MImpFormatRow (to, row);
			if (!copy.save())
				throw new Exception ("Copy error");
		}
		
		String msg = "#" + rows.length;
		if (!from.getFormatType().equals(to.getFormatType()))
			return msg + " - Note: Format Type different!";
		return msg;
	}	//	doIt
	
}	//	CopyImportFormat
