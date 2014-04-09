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

import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Order Batch Processing
 *	
 *  @author Jorg Janke
 *  @version $Id: OrderBatchProcess.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class OrderBatchProcess extends SvrProcess
{
	private int			p_C_DocTypeTarget_ID = 0;
	private String 		p_DocStatus = null;
	private int			p_C_BPartner_ID = 0;
	private String 		p_IsSelfService = null;
	private Timestamp	p_DateOrdered_From = null;
	private Timestamp	p_DateOrdered_To = null;
	private String 		p_DocAction = null;

	/**
	 * 	Prepare
	 */
	@Override
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("C_DocTypeTarget_ID"))
				p_C_DocTypeTarget_ID = element.getParameterAsInt();
			else if (name.equals("DocStatus"))
				p_DocStatus = (String)element.getParameter();
			else if (name.equals("IsSelfService"))
				p_IsSelfService = (String)element.getParameter();
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = element.getParameterAsInt();
			else if (name.equals("DateOrdered"))
			{
				p_DateOrdered_From = (Timestamp)element.getParameter();
				p_DateOrdered_To = (Timestamp)element.getParameter_To();
			}
			else if (name.equals("DocAction"))
				p_DocAction = (String)element.getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@return msg
	 *	@throws Exception
	 */
	@Override
	protected String doIt () throws Exception
	{
		log.info("C_DocTypeTarget_ID=" + p_C_DocTypeTarget_ID + ", DocStatus=" + p_DocStatus
			+ ", IsSelfService=" + p_IsSelfService + ", C_BPartner_ID=" + p_C_BPartner_ID
			+ ", DateOrdered=" + p_DateOrdered_From + "->" + p_DateOrdered_To
			+ ", DocAction=" + p_DocAction);
		
		if (p_C_DocTypeTarget_ID == 0)
			throw new CompiereUserException("@NotFound@: @C_DocTypeTarget_ID@");
		if (p_DocStatus == null || p_DocStatus.length() != 2)
			throw new CompiereUserException("@NotFound@: @DocStatus@");
		if (p_DocAction == null || p_DocAction.length() != 2)
			throw new CompiereUserException("@NotFound@: @DocAction@");
		
		//
		StringBuffer sql = new StringBuffer("SELECT * FROM C_Order "
			+ "WHERE C_DocTypeTarget_ID=? AND DocStatus=?");
		if (p_IsSelfService != null && p_IsSelfService.length() == 1)
			sql.append(" AND IsSelfService= ? ");
		if (p_C_BPartner_ID != 0)
			sql.append(" AND C_BPartner_ID= ? ");
		if (p_DateOrdered_From != null)
			sql.append(" AND TRUNC(DateOrdered,'DD') >= TRUNC(?,'DD') ");
		if (p_DateOrdered_To != null)
			sql.append(" AND TRUNC(DateOrdered,'DD') <= TRUNC(?,'DD') ");
		
		int counter = 0;
		int errCounter = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int index = 1;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			pstmt.setInt(index++, p_C_DocTypeTarget_ID);
			pstmt.setString(index++, p_DocStatus);
			if (p_IsSelfService != null && p_IsSelfService.length() == 1)
				pstmt.setString(index++, p_IsSelfService);
			if (p_C_BPartner_ID != 0)
				pstmt.setInt(index++, p_C_BPartner_ID);
			if (p_DateOrdered_From != null)
				pstmt.setTimestamp(index++, p_DateOrdered_From);
			if (p_DateOrdered_To != null)
				pstmt.setTimestamp(index++, p_DateOrdered_To);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				if (process(new MOrder(getCtx(),rs, get_TrxName())))
					counter++;
				else
					errCounter++;
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return "@Updated@=" + counter + ", @Errors@=" + errCounter;
	}	//	doIt
	
	/**
	 * 	Process Order
	 *	@param order order
	 *	@return true if ok
	 */
	private boolean process (MOrder order)
	{
		log.info(order.toString());
		//
		order.setDocAction(p_DocAction);
		
		if (DocumentEngine.processIt(order, p_DocAction))
		{
			order.save();
			commit();
			addLog(0, null, null, order.getDocumentNo() + ": OK");
			return true;
		}
		else
		{
			String status = order.getDocStatus();
			rollback();
			order.load(get_Trx());
			order.setDocStatus(status);
			order.save();
			commit();
		}
		addLog (0, null, null, order.getDocumentNo() + ": Error " + order.getProcessMsg());
		return false;
	}	//	process
	
}	//	OrderBatchProcess
