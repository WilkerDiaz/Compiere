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
import java.math.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

import javax.net.ssl.*;

import org.compiere.util.*;

/**
 *  Payment Processor Abstract Class
 *
 *  @author Jorg Janke
 *  @version $Id: PaymentProcessor.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public abstract class PaymentProcessor
{
	/**
	 *  Public Constructor
	 */
	public PaymentProcessor()
	{
	}   //  PaymentProcessor

	/**	Logger							*/
	protected CLogger			log = CLogger.getCLogger (getClass());
	/** Payment Processor Logger		*/
	static private CLogger		s_log = CLogger.getCLogger (PaymentProcessor.class);
	/** Encoding (ISO-8859-1 - UTF-8) 		*/
	public static final String	ENCODING = "UTF-8";
	/** Encode Parameters		*/
	private boolean 			m_encoded = false;
	/** Ampersand				*/
	public static final char	AMP = '&'; 
	/** Equals					*/
	public static final char	EQ = '='; 

	/**
	 *  Factory
	 * 	@param mpp payment processor model
	 * 	@param mp payment model
	 *  @return initialized PaymentProcessor or null
	 */
	public static PaymentProcessor create (MPaymentProcessor mpp, MPayment mp)
	{
		s_log.info("create for " + mpp);
		String className = mpp.getPayProcessorClass();
		if (className == null || className.length() == 0)
		{
			s_log.log(Level.SEVERE, "No PaymentProcessor class name in " + mpp);
			return null;
		}
		//
		PaymentProcessor myProcessor = null;
		try
		{
			Class<?> ppClass = Class.forName(className);
			if (ppClass != null)
				myProcessor = (PaymentProcessor)ppClass.newInstance();
		}
		catch (Error e1)    //  NoClassDefFound
		{
			s_log.log(Level.SEVERE, className + " - Error=" + e1.getMessage());
			return null;
		}
		catch (Exception e2)
		{
			s_log.log(Level.SEVERE, className, e2);
			return null;
		}
		if (myProcessor == null)
		{
			s_log.log(Level.SEVERE, "no class");
			return null;
		}

		//  Initialize
		myProcessor.p_mpp = mpp;
		myProcessor.p_mp = mp;
		//
		return myProcessor;
	}   //  create

	/*************************************************************************/

	/** Bank Account Processor	*/
	protected MPaymentProcessor p_mpp = null;
	/** The Payment				*/
	protected MPayment			p_mp = null;
	//
	private int     m_timeout = 30;

	/*************************************************************************/

	/**
	 *  Process CreditCard (no date check)
	 *  @return true if processed successfully
	 *  @throws IllegalArgumentException
	 */
	public abstract boolean processCC () throws IllegalArgumentException;

	/**
	 *  Payment is processed successfully
	 *  @return true if OK
	 */
	public abstract boolean isProcessedOK();

	
	/**************************************************************************
	 * 	Set Timeout
	 * 	@param newTimeout timeout
	 */
	public void setTimeout(int newTimeout)
	{
		m_timeout = newTimeout;
	}
	/**
	 * 	Get Timeout
	 *	@return timeout
	 */
	public int getTimeout()
	{
		return m_timeout;
	}

	
	/**************************************************************************
	 *  Check for delimiter fields &= and add length of not encoded
	 *  @param name name
	 *  @param value value
	 *  @param maxLength maximum length
	 *  @return name[5]=value or name=value
	 */
	protected String createPair(String name, BigDecimal value, int maxLength)
	{
		if (value == null)
			return createPair (name, "0", maxLength);
		else
		{
			if (value.scale() < 2)
				value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
			return createPair (name, String.valueOf(value), maxLength);
		}
	}	//	createPair

	/**
	 *  Check for delimiter fields &= and add length of not encoded
	 *  @param name name
	 *  @param value value
	 *  @param maxLength maximum length
	 *  @return name[5]=value or name=value
	 */
	protected String createPair(String name, int value, int maxLength)
	{
		if (value == 0)
			return "";
		else
			return createPair (name, String.valueOf(value), maxLength);
	}	//	createPair

	/**
	 *  Check for delimiter fields &= and add length of not encoded
	 *  @param name name
	 *  @param value value
	 *  @param maxLength maximum length
	 *  @return name[5]=value or name=value 
	 */
	protected String createPair(String name, String value, int maxLength)
	{
		//  Nothing to say
		if (name == null || name.length() == 0
			|| value == null || value.length() == 0)
			return "";
		
		if (value.length() > maxLength)
			value = value.substring(0, maxLength);
		
		StringBuffer retValue = new StringBuffer(name);
		if (m_encoded)
			try
			{
				value = URLEncoder.encode(value, ENCODING);
			}
			catch (UnsupportedEncodingException e)
			{
				log.log(Level.SEVERE, value + " - " + e.toString());
			}
		else if (value.indexOf(AMP) != -1 || value.indexOf(EQ) != -1)
			retValue.append("[").append(value.length()).append("]");
		//
		retValue.append(EQ);
		retValue.append(value);
		return retValue.toString();
	}   // createPair
	
	/**
	 * 	Set Encoded
	 *	@param doEncode true if encode
	 */
	public void setEncoded (boolean doEncode)
	{
		m_encoded = doEncode;
	}	//	setEncode
	/**
	 * 	Is Encoded
	 *	@return true if encoded
	 */
	public boolean isEncoded()
	{
		return m_encoded;
	}	//	setEncode
	
	/**
	 * 	Get Connect Post Properties
	 *	@param urlString POST url string
	 *	@param parameter parameter
	 *	@return result as properties
	 */
	protected Properties getConnectPostProperties (String urlString, String parameter)
	{
		long start = System.currentTimeMillis();
		String result = connectPost(urlString, parameter);
		if (result == null)
			return null;
		Properties prop = new Properties();
		try
		{
			String info = URLDecoder.decode(result, ENCODING);
			StringTokenizer st = new StringTokenizer(info, "&");	//	AMP
			while (st.hasMoreTokens())
			{
				String token = st.nextToken();
				int index = token.indexOf('=');
				if (index == -1)
					prop.put(token, "");
				else
				{
					String key = token.substring(0, index);
					String value = token.substring(index+1);
					prop.put(key, value);
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, result, e);
		}
		long ms = System.currentTimeMillis() - start;
		log.fine(ms + "ms - " + prop.toString());
		return prop;
	}	//	connectPost
	
	/**
	 * 	Connect via Post
	 *	@param urlString url destination (assuming https)
	 *	@param parameter parameter
	 *	@return response or null if failure
	 */
	protected String connectPost (String urlString, String parameter)
	{
		String response = null;
		try
		{
			// open secure connection
			URL url = new URL(urlString);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		//	URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			log.fine(connection.getURL().toString());

			// POST the parameter
			DataOutputStream out = new DataOutputStream (connection.getOutputStream());
			out.write(parameter.getBytes());
			out.flush();
			out.close();

			// process and read the gateway response
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			response = in.readLine();
			in.close();	                     // no more data
			log.finest(response);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, urlString, e);
		}
		//
	    return response;
	}	//	connectPost
		
}   //  PaymentProcessor
