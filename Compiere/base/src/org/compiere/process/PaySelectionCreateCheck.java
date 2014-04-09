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

import org.compiere.common.CompiereStateException;
import org.compiere.model.*;
import org.compiere.util.*;
 

/**
 *	Create Checks from Payment Selection Line
 *	
 *  @author Jorg Janke
 *  @version $Id: PaySelectionCreateCheck.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class PaySelectionCreateCheck extends SvrProcess
{
	/**	Target Payment Rule			*/
	private String		p_PaymentRule = null;
	/**	Payment Selection			*/
	private int			p_C_PaySelection_ID = 0;
	/** The checks					*/
	private ArrayList<MPaySelectionCheck>	m_list = new ArrayList<MPaySelectionCheck>();
	
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
			else if (name.equals("PaymentRule"))
				p_PaymentRule = (String)element.getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_C_PaySelection_ID = getRecord_ID();
		if (p_PaymentRule != null && p_PaymentRule.equals(X_C_Order.PAYMENTRULE_DirectDebit))
			p_PaymentRule = null;
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	@Override
	protected String doIt () throws Exception
	{
		log.info ("C_PaySelection_ID=" + p_C_PaySelection_ID
			+ ", PaymentRule=" + p_PaymentRule);
		
		MPaySelection psel = new MPaySelection (getCtx(), p_C_PaySelection_ID, get_TrxName());
		if (psel.get_ID() == 0)
			throw new IllegalArgumentException("Not found C_PaySelection_ID=" + p_C_PaySelection_ID);
		if (psel.isProcessed())
			throw new IllegalArgumentException("@Processed@");
		//
		MPaySelectionLine[] lines = psel.getLines(false);
		if (lines != null && lines.length > 0 )
		{

			for (MPaySelectionLine line : lines) {
				if (!line.isActive() || line.isProcessed())
					continue;
				if(p_PaymentRule != null)
					line.setPaymentRule(p_PaymentRule);
				createCheck (line);
			}
			//
			psel.setProcessed(true);
			psel.save();

			return "@C_PaySelectionCheck_ID@ - #" + m_list.size();
		}
		
		return "@NoPaymentLines@";
	}	//	doIt

	/**
	 * 	Create Check from line
	 *	@param line
	 *	@throws Exception for invalid bank accounts
	 */
	private void createCheck (MPaySelectionLine line) throws Exception
	{
		//	Try to find one
		for (int i = 0; i < m_list.size(); i++)
		{
			MPaySelectionCheck check = m_list.get(i);
			//	Add to existing
			if (check.getC_BPartner_ID() == line.getInvoice().getC_BPartner_ID() &&
				check.getPaymentRule().equals(line.getPaymentRule()))
			{
				check.addLine(line);
				if (!check.save())
					throw new CompiereStateException("Cannot save MPaySelectionCheck");
				line.setC_PaySelectionCheck_ID(check.getC_PaySelectionCheck_ID());
				line.setProcessed(true);
				if (!line.save())
					throw new CompiereStateException("Cannot save MPaySelectionLine");
				return;
			}
		}
		//	Create new
		String PaymentRule = line.getPaymentRule();
		if (p_PaymentRule != null)
		{
			if (!X_C_Order.PAYMENTRULE_DirectDebit.equals(PaymentRule))
				PaymentRule = p_PaymentRule;
		}
		MPaySelectionCheck check = new MPaySelectionCheck(line, PaymentRule);
		if (!check.isValid())
		{
			int C_BPartner_ID = check.getC_BPartner_ID();
			MBPartner bp = MBPartner.get(getCtx(), C_BPartner_ID);
			String msg = "@NotFound@ @C_BP_BankAccount@: " + bp.getName();
			throw new CompiereUserException(msg);
		}
		if (!check.save())
			throw new CompiereStateException("Cannot save MPaySelectionCheck");
		line.setC_PaySelectionCheck_ID(check.getC_PaySelectionCheck_ID());
		line.setProcessed(true);
		if (!line.save())
			throw new CompiereStateException("Cannot save MPaySelectionLine");
		m_list.add(check);
	}	//	createCheck
	
}	//	PaySelectionCreateCheck
