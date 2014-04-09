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

//import org.compiere.process.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Online Payment Process
 *
 *  @author Jorg Janke
 *  @version $Id: PaymentOnline.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class PaymentOnline extends SvrProcess
{
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
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("Record_ID=" + getRecord_ID());
		//	get Payment
		MPayment pp = new MPayment (getCtx(), getRecord_ID(), get_TrxName());
		//	Validate Number
		String msg = MPaymentValidate.validateCreditCardNumber(pp.getCreditCardNumber(), pp.getCreditCardType());
		if (msg != null && msg.length() > 0)
			throw new IllegalArgumentException(Msg.getMsg(getCtx(), msg));
		msg = MPaymentValidate.validateCreditCardExp(pp.getCreditCardExpMM(), pp.getCreditCardExpYY());
		if (msg != null && msg.length() > 0)
			throw new IllegalArgumentException(Msg.getMsg(getCtx(), msg));
		if (pp.getCreditCardVV() != null && pp.getCreditCardVV().length() > 0)
		{
			msg = MPaymentValidate.validateCreditCardVV(pp.getCreditCardVV(), pp.getCreditCardType());
			if (msg != null && msg.length() > 0)
				throw new IllegalArgumentException(Msg.getMsg(getCtx(), msg));
		}
		
		//  Process it
		boolean ok = pp.processOnline();
		pp.save();
		if (!ok)
			throw new Exception(pp.getErrorMessage());
		return "OK";
	}	//	doIt

}	//	PaymentOnline
