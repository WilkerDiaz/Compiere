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
import java.sql.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.vos.*;

/**
 * 	Create Invoices for Requests
 *	
 *	
 *  @author Jorg Janke
 *  @version $Id: RequestInvoice.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class RequestInvoice extends SvrProcess
{
	/** Request Type				*/
	private int		p_R_RequestType_ID = 0;
	/**	Request Group (opt)			*/
	private int		p_R_Group_ID = 0;
	/** Request Categpry (opt)		*/
	private int		p_R_Category_ID = 0;
	/** Business Partner (opt)		*/
	private int		p_C_BPartner_ID = 0;
	/** Default product				*/
	private int		p_M_Product_ID = 0;

	/** The invoice					*/
	private MInvoice m_invoice = null;
	/**	Line Count					*/
	private int		m_linecount = 0;
	
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
			else if (name.equals("R_RequestType_ID"))
				p_R_RequestType_ID = element.getParameterAsInt();
			else if (name.equals("R_Group_ID"))
				p_R_Group_ID = element.getParameterAsInt();
			else if (name.equals("R_Category_ID"))
				p_R_Category_ID = element.getParameterAsInt();
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = element.getParameterAsInt();
			else if (name.equals("M_Product_ID"))
				p_M_Product_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare
	
	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	@Override
	protected String doIt () throws Exception
	{
		log.info("R_RequestType_ID=" + p_R_RequestType_ID + ", R_Group_ID=" + p_R_Group_ID
			+ ", R_Category_ID=" + p_R_Category_ID + ", C_BPartner_ID=" + p_C_BPartner_ID
			+ ", p_M_Product_ID=" + p_M_Product_ID);
		
		MRequestType type = MRequestType.get (getCtx(), p_R_RequestType_ID);
		if (type.get_ID() == 0)
			throw new CompiereSystemException("@R_RequestType_ID@ @NotFound@ " + p_R_RequestType_ID);
		if (!type.isInvoiced())
			throw new CompiereSystemException("@R_RequestType_ID@ <> @IsInvoiced@");
		
		String sql = "SELECT * FROM R_Request r"
			+ " INNER JOIN R_Status s ON (r.R_Status_ID=s.R_Status_ID) "
			+ "WHERE s.IsClosed='Y'"
			+ " AND r.C_Invoice_ID IS NULL "
			+ " AND r.R_RequestType_ID=?";
		if (p_R_Group_ID != 0)
			sql += " AND r.R_Group_ID=?";
		if (p_R_Category_ID != 0)
			sql += " AND r.R_Category_ID=?";
		if (p_C_BPartner_ID != 0)
			sql += " AND r.C_BPartner_ID=?";
		sql += " AND r.IsInvoiced='Y' "
			+ "ORDER BY C_BPartner_ID";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			int index = 1;
			pstmt.setInt (index++, p_R_RequestType_ID);
			if (p_R_Group_ID != 0)
				pstmt.setInt (index++, p_R_Group_ID);
			if (p_R_Category_ID != 0)
				pstmt.setInt (index++, p_R_Category_ID);
			if (p_C_BPartner_ID != 0)
				pstmt.setInt (index++, p_C_BPartner_ID);
			rs = pstmt.executeQuery ();
			int oldC_BPartner_ID = 0;
			while (rs.next ())
			{
				MRequest request = new MRequest (getCtx(), rs, get_TrxName());
				if (!request.isInvoiced())
					continue;
				if (oldC_BPartner_ID != request.getC_BPartner_ID())
					invoiceDone();
				if (m_invoice == null)
				{
					invoiceNew(request);
					oldC_BPartner_ID = request.getC_BPartner_ID();
				}
				if(invoiceLine(request)>0)
				{
					request.setC_Invoice_ID(m_invoice.getC_Invoice_ID());
					request.save();
				}

			}
			invoiceDone();
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//	R_Category_ID
		return null;
	}	//	doIt
	
	/**
	 * 	Done with Invoice
	 */
	private void invoiceDone()
	{
		//	Close Old
		if (m_invoice != null)
		{
			if (m_linecount == 0)
				m_invoice.delete(false);
			else
			{
				DocumentEngine.processIt(m_invoice, DocActionConstants.ACTION_Prepare);
				m_invoice.save();
				addLog(0, null, m_invoice.getGrandTotal(), m_invoice.getDocumentNo());
			}
		}
		m_invoice = null;
	}	//	invoiceDone
	
	/**
	 * 	New Invoice
	 *	@param request request
	 */
	private void invoiceNew (MRequest request)
	{
		m_invoice = new MInvoice (getCtx(), 0, get_TrxName());
		m_invoice.setC_DocTypeTarget_ID(MDocBaseType.DOCBASETYPE_ARInvoice);
		
		MBPartner partner = new MBPartner (getCtx(), request.getC_BPartner_ID(), null);
		m_invoice.setBPartner(partner);
		
		m_invoice.save();
		m_linecount = 0;
	}	//	invoiceNew
	
	/**
	 * 	Invoice Line
	 *	@param request request
	 */
	private int invoiceLine (MRequest request)
	{
		int requestLine = 0;
		MRequestUpdate[] updates = request.getUpdates(null);
		for (MRequestUpdate element : updates) {
			BigDecimal qty = element.getQtyInvoiced();
			if (qty == null || qty.signum() == 0)
				continue;
			
			MInvoiceLine il = new MInvoiceLine(m_invoice);
			m_linecount++;
			il.setLine(m_linecount*10);
			//
			il.setQty(qty);
			//	Product
			int M_Product_ID = element.getM_ProductSpent_ID();
			if (M_Product_ID == 0)
				M_Product_ID = p_M_Product_ID;
			il.setM_Product_ID(M_Product_ID);
			//
			il.setPrice();
			il.save();
			requestLine++;
		}
		return requestLine;
	}	//	invoiceLine
	
}	//	RequestInvoice
