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
package org.compiere.wstore;

import java.io.*;
import java.math.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.*;
import org.compiere.vos.*;

/**
 *	Web Store Payment Entry & Confirmation
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: PaymentServlet.java,v 1.2 2006/07/30 00:53:21 jjanke Exp $
 */
public class PaymentServlet  extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**	Logging						*/
	private static CLogger			log = CLogger.getCLogger(PaymentServlet.class);

	public static final String		ATTR_PAYMENT = "payment";

	/**
	 * 	Initialize global variables
	 *  @param config servlet configuration
	 *  @throws ServletException
	 */
	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		if (!WebEnv.initWeb(config))
			throw new ServletException("PaymentServlet.init");
	}	//	init

	/**
	 * Get Servlet information
	 * @return Info
	 */
	@Override
	public String getServletInfo()
	{
		return "Compiere Payment Servlet";
	}	//	getServletInfo

	/**
	 * Clean up resources
	 */
	@Override
	public void destroy()
	{
		log.info("");
	}   //  destroy

	
	/**************************************************************************
	 *  Process the initial HTTP Get request.
	 *  Reads the Parameter Amt and optional C_Invoice_ID
	 *
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.info("Get from " + request.getRemoteHost() + " - " + request.getRemoteAddr());
		Ctx ctx = JSPEnv.getCtx(request);
		HttpSession session = request.getSession(true);
		session.removeAttribute(WebSessionCtx.HDR_MESSAGE);
	//	WEnv.dump(session);
	//	WEnv.dump(request);

		//	Non existing user or Existing Web Payment
		WebUser wu = (WebUser)session.getAttribute(WebUser.NAME);
		MPayment p = (MPayment)session.getAttribute (ATTR_PAYMENT);
		if (wu == null)
		{
			log.info ("No User");
			String url = "/index.jsp";
			log.info ("Forward to " + url);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (url);
			dispatcher.forward (request, response);
		}
		
		String confirmed = WebUtil.getParameter (request, "orderConfirm");
		boolean orderPayment = (confirmed != null) && ("y".equalsIgnoreCase(confirmed));
		
		//	Remove any open Order
		session.removeAttribute(WebOrder.NAME);
		//	Payment Amount
		String amtParam = WebUtil.getParameter (request, "Amt");
		if (amtParam == null || amtParam.length() == 0)
		{
			log.info ("No Payment Amount (" + amtParam + ")");
			doPost (request, response);
			return;
		}
		char[] chars = amtParam.toCharArray();
		StringBuffer sb = new StringBuffer();
		boolean decimal = false;
		for (int i = chars.length-1; i >=0; i--)
		{
			char c = chars[i];
			if (c == ',' || c == '.')
			{
				if (!decimal)
				{
					sb.insert (0, '.');
					decimal = true;
				}
			}
			else if (Character.isDigit(c))
				sb.insert(0,c);
		}
		BigDecimal amt = null;
		try
		{
				if (sb.length() > 0)
				{
					amt = new BigDecimal (sb.toString ());
					amt = amt.abs (); //	make it positive
				}
		}
		catch (Exception ex)
		{
			log.warning("Parsing Amount=" + amtParam + " (" + sb + ") - " + ex.toString());
		}
		//	Need to be positive amount
		if (amt == null || amt.compareTo(Env.ZERO) < 0)
		{
			log.info("No valid Payment Amount (" + amtParam + ") - " + amt);
			doPost (request, response);
			return;
		}

		String invoiceParam = WebUtil.getParameter (request, "C_Invoice_ID");
		int C_Invoice_ID = 0;
		try
		{
			if (invoiceParam != null)
				C_Invoice_ID = Integer.parseInt (invoiceParam);
		}
		catch (NumberFormatException ex)
		{
			log.warning("Parsing C_Invoice_ID=" + invoiceParam + " - " + ex.toString());
		}
		log.info("Amt=" + amt + ", C_Invoice_ID=" + C_Invoice_ID);

		//	Create New Payment for Amt & optional Invoice
		//	see OrderServlet.createPayment
		p = new MPayment(ctx, 0, null);
	//	p.setAD_Org_ID(..);
		p.setIsSelfService(true);
		p.setAmount(0, amt);			//	for CC selection ges default from Acct Currency
		p.setIsOnline(true);

		//	Sales CC Trx
		p.setC_DocType_ID(true);
		p.setTrxType(X_C_Payment.TRXTYPE_Sales);
		p.setTenderType(X_C_Payment.TENDERTYPE_CreditCard);
		//	Payment Info
		p.setC_Invoice_ID(C_Invoice_ID);
		//	BP Info
		p.setBP_BankAccount(wu.getBankAccount());
		//
	//	p.save();
		session.setAttribute (ATTR_PAYMENT, p);

		String url = "/paymentInfo.jsp";
		if (orderPayment)
			url = "/orderConfirm.jsp";
		
		log.info ("Forward to " + url);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (url);
		dispatcher.forward (request, response);
	}   //  doGet

	/**
	 *  Process the HTTP Post request.
	 * 	The actual payment processing
	 *
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.info("Post from " + request.getRemoteHost() + " - " + request.getRemoteAddr());
		Ctx ctx = JSPEnv.getCtx(request);
		HttpSession session = request.getSession(true);
	//	WEnv.dump(session);
	//	WEnv.dump(request);

		//	Web User/Payment
		WebUser wu = (WebUser)session.getAttribute(WebUser.NAME);
		MPayment p = (MPayment)session.getAttribute (ATTR_PAYMENT);
		WebOrder wo = (WebOrder)session.getAttribute (WebOrder.NAME);

		String confirmed = WebUtil.getParameter (request, "orderConfirm");
		boolean orderPayment = (confirmed != null) && ("y".equalsIgnoreCase(confirmed));

		String url = null;
		if (wu == null || p == null) {
			url = "/index.jsp";
			session.setAttribute(CheckOutServlet.ATTR_CHECKOUT, "");
		}
		else if (processPayment(request, ctx, p, wu, wo)) {
			if (orderPayment)
				url = "/orderCompleted.jsp";
			else 
				url = "/confirm.jsp";
			session.setAttribute(CheckOutServlet.ATTR_CHECKOUT, "");
		}
		else {
			if (orderPayment)
				url = "/orderConfirm.jsp";
			else 
				url = "/paymentInfo.jsp";
		}
		
//System.out.println("ORder no : " + wo.getC_Order_ID() + " " + wo.getOrder().get_ID()
//		+ " " + wo.getDocumentNo() + " " + wo.getOrder().getDocumentNo());		

		log.info ("Forward to " + url);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher (url);
		dispatcher.forward (request, response);
	}   //  doPost


	/**************************************************************************
	 * 	Process Payment.
	 * 	@param request request
	 * 	@param ctx context
	 * 	@param payment payment
	 * 	@param wu web user
	 * 	@param wo web order (optional)
	 *	@return true if processed
	 */
	private boolean processPayment(HttpServletRequest request, Ctx ctx, 
		MPayment payment, WebUser wu, WebOrder wo)
	{
		boolean ok = processParameter(request, ctx, payment, wu);
		if (ok)
		{
			//	if negative amount - make it positive
			if (payment.getPayAmt().compareTo(Env.ZERO) < 0)
				payment.setPayAmt(payment.getPayAmt().abs());
			
			//
			ok = payment.processOnline();
				
			payment.save();		//	save Info
			if (ok)
			{
				//	Process Web Order and Set Invoice ID
				if (wo != null)
				{
					if (!wo.isCompleted())
						wo.process(payment);
					if (!wo.isCompleted())
						log.warning("Order not processed " + wo);
				}
				else
					log.warning("No Order");
				//	
				DocumentEngine.processIt(payment, DocActionConstants.ACTION_Complete);
				payment.save();
				sendThanksEMail (request, ctx, payment, wu, wo);
			}
			else
			{
				log.fine(payment.getErrorMessage());
				String errMsg = payment.getErrorMessage();
				payment.save ();
				payment.setErrorMessage(errMsg);
				request.getSession().setAttribute(WebSessionCtx.HDR_MESSAGE, errMsg);
				//
				sendDeclineEMail(request, payment, wu, wo);
			}
		}
		return ok;
	}	//	processPayment

	/**
	 * 	Process Parameter and check them
	 * 	@param request request
	 * 	@param ctx context
	 * 	@param p payment
	 * 	@param wu web user
	 *	@return true if processed
	 */
	private boolean processParameter (HttpServletRequest request, Ctx ctx, MPayment p, WebUser wu)
	{
		StringBuffer sb = new StringBuffer();
		p.setTenderType(X_C_Payment.TENDERTYPE_CreditCard);
		p.setTrxType(X_C_Payment.TRXTYPE_Sales);
		p.setA_EMail(wu.getEmail());
		//	CC & Number
		String ccType = WebUtil.getParameter (request, "CreditCard");
		p.setCreditCardType(ccType);
		String ccNumber = WebUtil.getParameter (request, "CreditCardNumber");
		p.setCreditCardNumber (ccNumber);
		String AD_Message = MPaymentValidate.validateCreditCardNumber(ccNumber, ccType);
		if (AD_Message.length() > 0)
			sb.append(Msg.getMsg(ctx, AD_Message)).append(" - ");

		//	Optional Verification Code
		String ccVV = WebUtil.getParameter (request, "CreditCardVV");
		p.setCreditCardVV(ccVV);
		if (ccVV != null && ccVV.length() > 0)
		{
			AD_Message = MPaymentValidate.validateCreditCardVV (ccVV, ccType);
			if (AD_Message.length () > 0)
				sb.append (Msg.getMsg (ctx, AD_Message)).append (" - ");
		}
		//	Exp
		int mm = WebUtil.getParameterAsInt(request, "CreditCardExpMM");
		p.setCreditCardExpMM (mm);
		int yy = WebUtil.getParameterAsInt(request, "CreditCardExpYY");
		p.setCreditCardExpYY (yy);
		AD_Message = MPaymentValidate.validateCreditCardExp(mm, yy);
		if (AD_Message.length() > 0)
			sb.append(Msg.getMsg(ctx, AD_Message)).append(" - ");

		//	Account Info
		String aName = WebUtil.getParameter (request, "A_Name");
		if (aName == null || aName.length() == 0)
			sb.append("Name - ");
		else
			p.setA_Name(aName);
		String aStreet = WebUtil.getParameter (request, "A_Street");
		p.setA_Street(aStreet);
		String aCity = WebUtil.getParameter (request, "A_City");
		if (aCity == null || aCity.length() == 0)
			sb.append("City - ");
		else
			p.setA_City(aCity);
		String aState = WebUtil.getParameter (request, "A_State");
		p.setA_State(aState);
		String aZip = WebUtil.getParameter (request, "A_Zip");
		if (aZip == null || aZip.length() == 0)
			sb.append("Zip - ");
		else
			p.setA_Zip(aZip);
		String aCountry = WebUtil.getParameter (request, "A_Country");
		p.setA_Country(aCountry);

		//	Error Message
		boolean ok = sb.length() == 0;
		p.setErrorMessage(sb.toString());		//	always set

		//	Save BP Bank Account
		if (ok)
		{
			String SP = "SavePayment";
			String SavePayment = WebUtil.getParameter (request, SP);
			if (SP.equals(SavePayment))
				p.saveToBP_BankAccount(wu.getBankAccount());
		}
		//
		return ok;
	}	//	processParameter


	/**
	 * 	Send Payment EMail.
	 * 	@param request request
	 * 	@param p payment
	 * 	@param wu web user
	 * 	@param wo optional web order
	 */
	private void sendThanksEMail (HttpServletRequest request, Ctx ctx,
		MPayment p, WebUser wu, WebOrder wo)
	{
		StringBuffer message = new StringBuffer()
			.append(p.getPayAmt())
			.append(" (").append(Msg.getElement(ctx, "R_PnRef"))
			.append("=").append(p.getR_PnRef()).append(") ");
		if (wo != null)
			message.append("\n").append(Msg.getElement(ctx, "C_Order_ID"))
				.append(": ").append(wo.getDocumentNo());
		
		JSPEnv.sendEMail(request, wu, X_W_MailMsg.MAILMSGTYPE_PaymentAcknowledgement,
			new Object[]{
				p.getDocumentNo() + " (" + p.getPayAmt() + ")",
				wu.getName(),
				message.toString()});
		//	SalesRep EMail
		if (wo != null && wo.getSalesRep_ID() != 0)
		{
			MClient client = MClient.get(ctx);
			client.sendEMail(wo.getSalesRep_ID(), 
				"(CC) Payment: " + p.getDocumentNo() + " (" + p.getPayAmt() + ")", 
				"Order: " + wo.getDocumentNo() 
				+ "\nUser: " + wu.getName() + " - " + wu.getEmail(),
				null);
		}
	}	//	sendEMail

	/**
	 * 	Send Payment EMail.
	 * 	@param request request
	 * 	@param p payment
	 * 	@param wu web user
	 */
	private void sendDeclineEMail (HttpServletRequest request, 
		MPayment p, WebUser wu, WebOrder wo)
	{
		StringBuffer message = new StringBuffer(p.getErrorMessage())
			.append(" - ").append(p.getCurrencyISO()).append(" ").append(p.getPayAmt())
			.append(" (Reference=").append(p.getR_PnRef()).append(") ");
			if (wo != null)
				message.append("\nfor Order: ").append(wo.getDocumentNo());
		
		JSPEnv.sendEMail(request, wu, X_W_MailMsg.MAILMSGTYPE_PaymentError, 
			new Object[]{
				p.getDocumentNo() + " (" + p.getCurrencyISO() + " " + p.getPayAmt() + ")",
				wu.getName(),
				message.toString()});
	}	//	sendDeclineEMail

}	//	PaymentServlet
