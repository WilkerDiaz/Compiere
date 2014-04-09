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

import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Process Invoice Batch	
 *	
 *  @author Jorg Janke
 *  @version $Id: InvoiceBatchProcess.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class InvoiceBatchProcess extends SvrProcess
{
	/**	Batch to process		*/
	private int		p_C_InvoiceBatch_ID = 0;
	/** Action					*/
	private String	p_DocAction = null;
	
	/** Invoice					*/
	private MInvoice	m_invoice = null;
	/** Old DocumentNo			*/
	private String		m_oldDocumentNo = null;
	/** Old BPartner			*/
	private int			m_oldC_BPartner_ID = 0;
	/** Old BPartner Loc		*/
	private int			m_oldC_BPartner_Location_ID = 0;
	
	/** Counter					*/
	private int			m_count = 0;
	
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
			else if (name.equals("DocAction"))
				p_DocAction = (String)element.getParameter();
		}
		p_C_InvoiceBatch_ID = getRecord_ID();
	}   //  prepare

	/**
	 * 	Process Invoice Batch
	 *	@return message
	 *	@throws Exception
	 */
	@Override
	protected String doIt () throws Exception
	{
		log.info("C_InvoiceBatch_ID=" + p_C_InvoiceBatch_ID + ", DocAction=" + p_DocAction);
		if (p_C_InvoiceBatch_ID == 0)
			throw new CompiereUserException("C_InvoiceBatch_ID = 0");
		MInvoiceBatch batch = new MInvoiceBatch(getCtx(), p_C_InvoiceBatch_ID, get_TrxName());
		if (batch.get_ID() == 0)
			throw new CompiereUserException("@NotFound@: @C_InvoiceBatch_ID@ - " + p_C_InvoiceBatch_ID);
		if (batch.isProcessed())
			throw new CompiereUserException("@Processed@");
		//
		if (batch.getControlAmt().signum() != 0
			&& batch.getControlAmt().compareTo(batch.getDocumentAmt()) != 0)
			throw new CompiereUserException("@ControlAmt@ <> @DocumentAmt@");		
		//
		MInvoiceBatchLine[] lines = batch.getLines(false);
		for (MInvoiceBatchLine line : lines) {
			if (line.getC_Invoice_ID() != 0 || line.getC_InvoiceLine_ID() != 0)
				continue;
			
			if ((m_oldDocumentNo != null 
					&& !m_oldDocumentNo.equals(line.getDocumentNo()))
				|| m_oldC_BPartner_ID != line.getC_BPartner_ID()
				|| m_oldC_BPartner_Location_ID != line.getC_BPartner_Location_ID())
				completeInvoice();
			//	New Invoice
			if (m_invoice == null)
			{
				m_invoice = new MInvoice (batch, line);
				if (!m_invoice.save())
					throw new CompiereUserException("Cannot save Invoice");
				//
				m_oldDocumentNo = line.getDocumentNo();
				m_oldC_BPartner_ID = line.getC_BPartner_ID();
				m_oldC_BPartner_Location_ID = line.getC_BPartner_Location_ID();
			}
			
			if (line.isTaxIncluded() != m_invoice.isTaxIncluded())
			{
				//	rollback
				throw new CompiereUserException("Line " + line.getLine() + " TaxIncluded inconsistent");
			}
			
			//	Add Line
			MInvoiceLine invoiceLine = new MInvoiceLine (m_invoice);
			invoiceLine.setDescription(line.getDescription());
			invoiceLine.setC_Charge_ID(line.getC_Charge_ID());
			invoiceLine.setQty(line.getQtyEntered());	// Entered/Invoiced
			invoiceLine.setPrice(line.getPriceEntered());
			invoiceLine.setC_Tax_ID(line.getC_Tax_ID());
			invoiceLine.setTaxAmt(line.getTaxAmt());
			invoiceLine.setLineNetAmt(line.getLineNetAmt());
			invoiceLine.setLineTotalAmt(line.getLineTotalAmt());
			if (!invoiceLine.save())
			{
				//	rollback
				throw new CompiereUserException("Cannot save Invoice Line");
			}

			//	Update Batch Line
			line.setC_Invoice_ID(m_invoice.getC_Invoice_ID());
			line.setC_InvoiceLine_ID(invoiceLine.getC_InvoiceLine_ID());
			line.save();
			
		}	//	for all lines
		completeInvoice();
		//
		batch.setProcessed(true);
		batch.save();
		
		return "#" + m_count;
	}	//	doIt
	
	
	/**
	 * 	Complete Invoice
	 */
	private void completeInvoice()
	{
		if (m_invoice == null)
			return;
		
		m_invoice.setDocAction(p_DocAction);
		DocumentEngine.processIt(m_invoice, p_DocAction);
		m_invoice.save();
		
		addLog(0, m_invoice.getDateInvoiced(), m_invoice.getGrandTotal(), m_invoice.getDocumentNo());
		m_count++;
		
		m_invoice = null;
	}	//	completeInvoice
	
}	//	InvoiceBatchProcess
