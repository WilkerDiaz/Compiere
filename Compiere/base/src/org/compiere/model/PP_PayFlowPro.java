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
package org.compiere.model;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import paypal.payflow.*;

/**
 *  Payment Processor for VeriSign PayFlow Pro.
 * 	Needs Certification File (get from VeriSign)
 *
 *  @author  Jorg Janke
 *  @version $Id: PP_PayFlowPro.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public final class PP_PayFlowPro extends PaymentProcessor
	implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  PayFowPro Constructor
	 */
	public PP_PayFlowPro()
	{
		super();
		m_pp = new PayflowAPI();
	}   //  PP_PayFowPro

	//	Payment System			*/
	private PayflowAPI  m_pp = null;
	private boolean		m_ok = false;

	protected final static String	RESULT_OK = "0";
	protected final static String	RESULT_DECLINED = "12";
	protected final static String	RESULT_INVALID_NO = "23";
	protected final static String	RESULT_INVALID_EXP = "24";
	protected final static String	RESULT_INSUFFICIENT_FUNDS = "50";
	protected final static String	RESULT_TIMEOUT_PROCESSOR = "104";
	protected final static String	RESULT_TIMEOUT_HOST = "109";
	
	static String hostAddress;
	static int hostPort;
	static String parmList;
	static int timeout;
	static String proxyAddress;
	static int proxyPort;
	static String proxyLogon;
	static String proxyPassword;

	/**
	 *  Get Version
	 *  @return version
	 */
	public String getVersion()
	{
		return "PayFlowPro " + m_pp.getVersion();
	}   //  getVersion

	/**
	 *  Process CreditCard (no date check)
	 *  @return true if processed successfully
	 *  @throws IllegalArgumentException
	 */
	@Override
	public boolean processCC () throws IllegalArgumentException
	{
		//
		StringBuffer param = new StringBuffer();
		//  Transaction Type
		if (p_mp.getTrxType().equals(X_C_Payment.TRXTYPE_Sales))
			param.append("TRXTYPE=").append(p_mp.getTrxType());
		else
			throw new IllegalArgumentException("PP_PayFlowPro TrxType not supported - " + p_mp.getTrxType());

		//  Mandatory Fields
		param.append("&TENDER=C")										//	CreditCard
			.append("&ACCT=").append(MPaymentValidate.checkNumeric(p_mp.getCreditCardNumber()));	//	CreditCard No
		param.append("&EXPDATE=");										//	ExpNo
		String month = String.valueOf(p_mp.getCreditCardExpMM());
		if (month.length() == 1)
			param.append("0");
		param.append(month);
		int expYY = p_mp.getCreditCardExpYY();
		if (expYY > 2000)
			expYY -= 2000;
		String year = String.valueOf(expYY);
		if (year.length() == 1)
			param.append("0");
		param.append(year);
		param.append("&AMT=").append(p_mp.getPayAmt());					//	Amount

		//  Optional Control Fields		- AuthCode & Orig ID
		param.append(createPair("&AUTHCODE", p_mp.getVoiceAuthCode(), 6));
		param.append(createPair("&ORIGID", p_mp.getOrig_TrxID(), 12));	//	PNREF - returned
		//	CVV
		param.append(createPair("&CVV2", p_mp.getCreditCardVV(), 4));
	//	param.append(createPair("&SWIPE", p_mp.getXXX(), 80));			//	Track 1+2

		//	Address
		param.append(createPair("&NAME", p_mp.getA_Name(), 30));
		param.append(createPair("&STREET", p_mp.getA_Street(), 30));	//	Street
		param.append(createPair("&ZIP", p_mp.getA_Zip(), 9));			//	Zip 5-9
		//	CITY 20, STATE 2,
		param.append(createPair("&EMAIL", p_mp.getA_EMail(), 64));		//	EMail

		//	Amex Fields
		//	DESC, SHIPTOZIP, TAXAMT
	//	param.append(createPair("&DESC", p_mp.getXXX(), 23));			//	Description
		param.append(createPair("&SHIPTOZIP", p_mp.getA_Zip(), 6));		//	Zip 6
		param.append(createPair("&TAXAMT", p_mp.getTaxAmt(), 10));		//	Tax

		//	Invoice No
		param.append(createPair("&INVNUM", p_mp.getC_Invoice_ID(), 9));

		//	COMMENT1/2
		param.append(createPair("&COMMENT1", p_mp.getC_Payment_ID(), 128));		//	Comment
		param.append(createPair("&COMMENT2", p_mp.getC_BPartner_ID(), 128)); 	//	Comment2

		return process(param.toString());
	}   //  processCC

	/**
	 *  Process Transaction
	 *  @param parameter Command String
	 *  @return true if processed successfully
	 */
	public boolean process (String parameter)
	{
		long start = System.currentTimeMillis();
		StringBuffer param = new StringBuffer(parameter);
		//  Usr/Pwd
		param
			.append("&PARTNER=").append(p_mpp.getPartnerID())
			.append("&VENDOR=").append(p_mpp.getVendorID())
			.append("&USER=").append(p_mpp.getUserID())
			.append("&PWD=").append(p_mpp.getPassword());
		log.fine("-> " + param.toString());

		hostAddress = p_mpp.getHostAddress();
		hostPort = p_mpp.getHostPort();
		timeout =  getTimeout();
		proxyAddress = p_mpp.getProxyAddress();
		proxyPort = p_mpp.getProxyPort();
		proxyLogon =  p_mpp.getProxyLogon();
		proxyPassword = p_mpp.getProxyPassword();
		log.fine(hostAddress + " " + hostPort + "; Timeout=" + timeout
				+ "; Proxy=" + proxyAddress + " " + proxyPort + " " + proxyLogon + " " + proxyPassword);
		m_pp = new PayflowAPI(hostAddress, hostPort, timeout, proxyAddress, proxyPort, proxyLogon, proxyPassword);		

		String requestID = PayflowUtility.getRequestId();
		String response = m_pp.submitTransaction(param.toString(), requestID);

		long ms = System.currentTimeMillis() - start;
		log.fine("<- " + ms + "ms - " + "request id - " + requestID + " - " + response);
		p_mp.setR_Result("");
		p_mp.setR_Info(response);		//	complete info

		//  RESULT=1&PNREF=PN0001480030&RESPMSG=Invalid User Authentication
		//  RESULT=0&PNREF=P60501480167&RESPMSG=Approved&AUTHCODE=010101&AVSADDR=X&AVSZIP=X
		//	RESULT=-31&RESPMSG=The certificate chain did not validate, no local certificate found, javax.net.ssl.SSLException: Cert Path = C:\Compiere2\lib, Working Directory = C:\Compiere\compiere-all2\client\temp
		StringTokenizer st = new StringTokenizer(response, "&", false);
		while (st.hasMoreTokens())
		{
			String token = st.nextToken();
			int pos = token.indexOf("=");
			String name = token.substring(0, pos);
			String value = token.substring(pos+1);
			//
			if (name.equals("RESULT"))
			{
				p_mp.setR_Result (value);
				m_ok = RESULT_OK.equals(value);
			}
			else if (name.equals("PNREF"))
				p_mp.setR_PnRef(value);
			else if (name.equals("RESPMSG"))
				p_mp.setR_RespMsg(value);
			else if (name.equals("AUTHCODE"))
				p_mp.setR_AuthCode(value);
			else if (name.equals("AVSADDR"))
				p_mp.setR_AvsAddr(value);
			else if (name.equals("AVSZIP"))
				p_mp.setR_AvsZip(value);
			else if (name.equals("IAVS"))		//	N=YSA, Y=International
				;
			else if (name.equals("CVV2MATCH"))	//	Y/N X=not supported
				;
			else
				log.log(Level.SEVERE, "Response unknown = " + token);
		}
		//  Probelms with rc (e.g. 0 with Result=24)
		return m_ok;
	}   //  process

	/**
	 *  Payment is procesed successfully
	 *  @return true if OK
	 */
	@Override
	public boolean isProcessedOK()
	{
		return m_ok;
	}   //  isProcessedOK

}   //  PP_PayFowPro
