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

import java.sql.*;
import java.util.logging.*;

import org.compiere.common.CompiereStateException;
import org.compiere.model.*;

/**
 *	Copy Order and optionally close
 *	
 *  @author Jorg Janke
 *  @version $Id: CopyOrder.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class CopyOrder extends SvrProcess
{
	/** Order to Copy				*/
	private int 		p_C_Order_ID = 0;
	/** Document Type of new Order	*/
	private int 		p_C_DocType_ID = 0;
	/** New Doc Date				*/
	private Timestamp	p_DateDoc = null;
	/** Close/Process Old Order		*/
	private boolean 	p_IsCloseDocument = false;
	
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
			else if (name.equals("C_Order_ID"))
				p_C_Order_ID = element.getParameterAsInt();
			else if (name.equals("C_DocType_ID"))
				p_C_DocType_ID = element.getParameterAsInt();
			else if (name.equals("DateDoc"))
				p_DateDoc = (Timestamp)element.getParameter();
			else if (name.equals("IsCloseDocument"))
				p_IsCloseDocument = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("C_Order_ID=" + p_C_Order_ID 
			+ ", C_DocType_ID=" + p_C_DocType_ID 
			+ ", CloseDocument=" + p_IsCloseDocument);
		if (p_C_Order_ID == 0)
			throw new IllegalArgumentException("No Order");
		MDocType dt = MDocType.get(getCtx(), p_C_DocType_ID);
		if (dt.get_ID() == 0)
			throw new IllegalArgumentException("No DocType");
		if (p_DateDoc == null)
			p_DateDoc = new Timestamp (System.currentTimeMillis());
		//
		MOrder from = new MOrder (getCtx(), p_C_Order_ID, get_TrxName());
		MOrder newOrder = MOrder.copyFrom (from, p_DateDoc, 
			dt.getC_DocType_ID(), false, true, null);		//	copy ASI
		newOrder.setC_DocTypeTarget_ID(p_C_DocType_ID);
		boolean OK = newOrder.save();
		if (!OK)
			throw new CompiereStateException("Could not create new Order");
		//
		if (p_IsCloseDocument)
		{
			MOrder original = new MOrder (getCtx(), p_C_Order_ID, get_TrxName());
			original.setDocAction(X_C_Order.DOCACTION_Complete);
			DocumentEngine.processIt(original, X_C_Order.DOCACTION_Complete);
			original.save();
			original.setDocAction(X_C_Order.DOCACTION_Close);
			DocumentEngine.processIt(original, X_C_Order.DOCACTION_Close);
			original.save();
		}
		//
	//	Env.setSOTrx(getCtx(), newOrder.isSOTrx());
	//	return "@C_Order_ID@ " + newOrder.getDocumentNo();
		return dt.getName() + ": " + newOrder.getDocumentNo();
	}	//	doIt

}	//	CopyOrder
