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
package org.compiere.ldap;

import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

import com.sun.jndi.ldap.*;

/**
 * 	Ldap Wire Response
 *
 *  @author Jorg Janke
 *  @version $Id: LdapResult.java 7514 2009-04-20 21:57:40Z freyes $
 */
public class LdapResult
{
	/** LdapMesssage */
	private LdapMessage	m_ldapMsg = null;
	/** Encoder							*/
	private BerEncoder 	m_encoder = null;
	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger (LdapResult.class);
	/** Error number */
	private int 		m_errNo = LDAP_SUCCESS;
	/** Error String */
	private String 		m_errStr = "";
	/** LdapUser */
	private MLdapUser 	m_ldapUser = null;
	/** disconnect to client */
	private boolean 	m_disconnect = false;

	public LdapResult ()
	{
	}	//	LdapResult

	/*
	 * Reset the attributes
	 */
	public void reset(LdapMessage LdapMsg, MLdapUser LdapUser)
	{
		this.m_ldapMsg = LdapMsg;
		m_encoder = new BerEncoder();
		m_errNo = LDAP_SUCCESS;
		m_errStr = "";
		this.m_ldapUser = LdapUser;
	}  // reset()

	/**
	 * 	Get the response according to the request message
	 * 	@param model model
	 * 	@param remoteHost remote host name
	 * 	@param remoteAddr remote host ip address
	 *	@return response
	 */
	public byte[] getResult(MLdapProcessor model, String remoteHost, String remoteAddr)
	{
		if (m_errNo != LDAP_SUCCESS)
		{
			generateResult("",
				(m_ldapMsg.getOperation() == LdapMessage.BIND_REQUEST ?
					LdapMessage.BIND_RESPONSE : LdapMessage.SEARCH_RES_RESULT),
				m_errNo,
				ldapErrorMessage[m_errNo] + ": " + m_errStr);
			m_encoder.getTrimmedBuf();
		}

		try
		{
			String usrId = m_ldapMsg.getUserId();
			String o = m_ldapMsg.getOrg();
			String ou = m_ldapMsg.getOrgUnit();
			int msgId = m_ldapMsg.getMsgId();

			// Adding the Application 1 Sequence
			if (m_ldapMsg.getOperation() == LdapMessage.BIND_REQUEST)
			{
				String pwd = m_ldapMsg.getUserPasswd();
				if (pwd == null || pwd.length() <= 0)
				{
					// 1st anonymous bind
					generateResult(m_ldapMsg.getDN(), LdapMessage.BIND_RESPONSE,
							LDAP_SUCCESS, null);
					log.config("#" + msgId + ": Success on anonymous bind");
					return m_encoder.getTrimmedBuf();
				}

				// Authenticate with Compiere data
				if (m_ldapUser.getUserId() == null)
				{  // Try to authenticate on the 1st bind, must be java client
					m_ldapUser.reset();
					model.authenticate(m_ldapUser, usrId, o, ou, remoteHost, remoteAddr);
					if (m_ldapUser.getErrorMsg() != null)
					{   // Failed to authenticated with compiere
						m_errNo = LDAP_NO_SUCH_OBJECT;
						generateResult(m_ldapMsg.getBaseObj(), LdapMessage.SEARCH_RES_RESULT,
								LDAP_NO_SUCH_OBJECT,
								ldapErrorMessage[LDAP_NO_SUCH_OBJECT] + m_ldapUser.getErrorMsg());
						log.config("#" + msgId + ": Failed with bind");
						return m_encoder.getTrimmedBuf();
					}
				}

				// Check to see if the input passwd is match to the one
				// in compiere database
				if (m_ldapUser.getUserId() != null
					&& m_ldapUser.getPassword() != null	
					&& usrId.compareTo(m_ldapUser.getUserId()) == 0
					&& !SecureEngine.isEncrypted(pwd)
					&& (pwd.compareTo(m_ldapUser.getPassword()) == 0
						|| pwd.compareTo(SecureEngine.decrypt(m_ldapUser.getPassword())) == 0))
				{	// Successfully authenticated
					generateResult("", LdapMessage.BIND_RESPONSE,
							LDAP_SUCCESS, null);
					// Close the connection to client since most of the client
					// application might cache the connection but we can't afford
					// to have too many such client connection
					m_disconnect = true;
					log.config("#" + msgId + ": Success authenticate with password");
				}
				else
				{	// Unsuccessfully authenticated
					m_errNo = LDAP_INAPPROPRIATE_AUTHENTICATION;
					generateResult("", LdapMessage.BIND_RESPONSE,
							LDAP_INAPPROPRIATE_AUTHENTICATION,
							ldapErrorMessage[LDAP_INAPPROPRIATE_AUTHENTICATION]);
					log.info("#" + msgId + ": Failed : " + ldapErrorMessage[LDAP_INAPPROPRIATE_AUTHENTICATION]);
				}
			}
			else if (m_ldapMsg.getOperation() == LdapMessage.SEARCH_REQUEST)
			{
				// Authenticate with compiere database
				m_ldapUser.reset();
				model.authenticate(m_ldapUser, usrId, o, ou, remoteHost, remoteAddr);
				if (m_ldapUser.getErrorMsg() != null)
				{
					m_errNo = LDAP_NO_SUCH_OBJECT;
					generateResult(m_ldapMsg.getBaseObj(), LdapMessage.SEARCH_RES_RESULT,
							LDAP_NO_SUCH_OBJECT,
							ldapErrorMessage[LDAP_NO_SUCH_OBJECT] + m_ldapUser.getErrorMsg());
					log.info("#" + msgId + ": Failed with SEARCH_REQUEST");
					return m_encoder.getTrimmedBuf();
				}

			    m_encoder.beginSeq(48);  // Hard coded here for Envelope header
			    m_encoder.encodeInt(msgId);
				m_encoder.beginSeq(LdapMessage.SEARCH_REP_ENTRY);  // Application 4
				m_encoder.encodeString("cn="+m_ldapMsg.getUserId(), true);   // this should be object name
				// not going to put in any attributes for this
				m_encoder.beginSeq(48);
				m_encoder.endSeq();
				m_encoder.endSeq();
				m_encoder.endSeq();

				// SearchResultDone Application 5 for bind
				// Result 0 = success
				// No error message
				generateResult(m_ldapMsg.getBaseObj(), LdapMessage.SEARCH_RES_RESULT,
						LDAP_SUCCESS, null);
				log.config("#" + msgId + ": Success with SEARCH_REQUEST");
			}

			return m_encoder.getTrimmedBuf();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);

			// Get the response operation
			int responseOp = LdapMessage.BIND_RESPONSE;
			if (m_ldapMsg.getOperation() == LdapMessage.SEARCH_REQUEST)
				responseOp = LdapMessage.SEARCH_RES_RESULT;

			// Send the response to the client and disconnect
			m_errNo = LDAP_OTHER;
			generateResult(m_ldapMsg.getBaseObj(), responseOp,
					LDAP_OTHER,
					ldapErrorMessage[LDAP_OTHER] + e.getMessage());
			m_disconnect = true;
		}

		return m_encoder.getTrimmedBuf();
	}	//	getResult

	/**
	 * 	Generate LDAPResult
	 *  @param dn Distinguished Name
	 *  @param resultProtocol Result protocol/operation code
	 *  @param resultCode Result code
	 *  @param errMsg Error Message
	 *	@return reponse
	 */
	private void generateResult(String dn, int resultProtocol,
		int resultCode, String errMsg)
	{
		try
		{
		    m_encoder.beginSeq(48);  // Hard coded here for Envelope header
		    m_encoder.encodeInt(m_ldapMsg.getMsgId());
		    m_encoder.beginSeq(resultProtocol);
		    m_encoder.encodeInt(resultCode, 10);   // Enumeration - 10
	        // Adding LDAPDN
	        m_encoder.encodeString(dn, true);
	        // Adding error message
	        m_encoder.encodeString(errMsg == null?"":errMsg, true);
	        m_encoder.endSeq();
	        m_encoder.endSeq();
	        log.fine("#" + m_ldapMsg.getMsgId() + ": "
	        	+ "dn=" + dn + ", op=" + resultProtocol
	        	+ ", result=" + resultCode + ", errMsg=" + errMsg);
		}
	    catch (Exception ex)
	    {
			log.log(Level.SEVERE, "", ex);
	    }
	}  // generateResult

	/*
	 * Should it be close the connection with client
	 */
	public boolean getDone()
	{
		if (m_errNo != LDAP_SUCCESS)
			return true;
		return m_disconnect;
	}  // getDone()

	/**
	 * Set the error No
	 * @param errNo Error Number
	 */
	public void setErrorNo(int errNo)
	{
		this.m_errNo = errNo;
	}  // setErrorNo()

	/**
	 * Get the error No
	 * @return errNo Error Number
	 */
	public int getErrorNo()
	{
		return m_errNo;
	}  // getErrorNo()

	/**
	 * Set the error String
	 * @param errStr Error String
	 */
	public void setErrorString(String errStr)
	{
		m_errStr = errStr;
	}  // setErrorStr()

    static final int LDAP_SUCCESS = 0;
    static final int LDAP_OPERATIONS_ERROR = 1;
    static final int LDAP_PROTOCOL_ERROR = 2;
    static final int LDAP_TIME_LIMIT_EXCEEDED = 3;
    static final int LDAP_SIZE_LIMIT_EXCEEDED = 4;
    static final int LDAP_COMPARE_FALSE = 5;
    static final int LDAP_COMPARE_TRUE = 6;
    static final int LDAP_AUTH_METHOD_NOT_SUPPORTED = 7;
    static final int LDAP_STRONG_AUTH_REQUIRED = 8;
    static final int LDAP_PARTIAL_RESULTS = 9;
    static final int LDAP_REFERRAL = 10;
    static final int LDAP_ADMIN_LIMIT_EXCEEDED = 11;
    static final int LDAP_UNAVAILABLE_CRITICAL_EXTENSION = 12;
    static final int LDAP_CONFIDENTIALITY_REQUIRED = 13;
    static final int LDAP_SASL_BIND_IN_PROGRESS = 14;
    static final int LDAP_NO_SUCH_ATTRIBUTE = 16;
    static final int LDAP_UNDEFINED_ATTRIBUTE_TYPE = 17;
    static final int LDAP_INAPPROPRIATE_MATCHING = 18;
    static final int LDAP_CONSTRAINT_VIOLATION = 19;
    static final int LDAP_ATTRIBUTE_OR_VALUE_EXISTS = 20;
    static final int LDAP_INVALID_ATTRIBUTE_SYNTAX = 21;
    static final int LDAP_NO_SUCH_OBJECT = 32;
    static final int LDAP_ALIAS_PROBLEM = 33;
    static final int LDAP_INVALID_DN_SYNTAX = 34;
    static final int LDAP_IS_LEAF = 35;
    static final int LDAP_ALIAS_DEREFERENCING_PROBLEM = 36;
    static final int LDAP_INAPPROPRIATE_AUTHENTICATION = 48;
    static final int LDAP_INVALID_CREDENTIALS = 49;
    static final int LDAP_INSUFFICIENT_ACCESS_RIGHTS = 50;
    static final int LDAP_BUSY = 51;
    static final int LDAP_UNAVAILABLE = 52;
    static final int LDAP_UNWILLING_TO_PERFORM = 53;
    static final int LDAP_LOOP_DETECT = 54;
    static final int LDAP_NAMING_VIOLATION = 64;
    static final int LDAP_OBJECT_CLASS_VIOLATION = 65;
    static final int LDAP_NOT_ALLOWED_ON_NON_LEAF = 66;
    static final int LDAP_NOT_ALLOWED_ON_RDN = 67;
    static final int LDAP_ENTRY_ALREADY_EXISTS = 68;
    static final int LDAP_OBJECT_CLASS_MODS_PROHIBITED = 69;
    static final int LDAP_AFFECTS_MULTIPLE_DSAS = 71;
    static final int LDAP_OTHER = 80;
    static final String ldapErrorMessage[] = {
        "Success", "Operations Error", "Protocol Error", "Timelimit Exceeded",
        "Sizelimit Exceeded", "Compare False", "Compare True",
        "Authentication Method Not Supported", "Strong Authentication Required", null,
        "Referral", "Administrative Limit Exceeded", "Unavailable Critical Extension",
        "Confidentiality Required", "SASL Bind In Progress", null, "No Such Attribute",
        "Undefined Attribute Type", "Inappropriate Matching", "Constraint Violation",
        "Attribute Or Value Exists", "Invalid Attribute Syntax", null, null, null,
        null, null, null, null, null,null, null, "No Such Object", "Alias Problem",
        "Invalid DN Syntax", null, "Alias Dereferencing Problem", null, null, null,
        null, null, null, null, null, null, null, null, "Inappropriate Authentication",
        "Invalid Credentials", "Insufficient Access Rights", "Busy", "Unavailable",
        "Unwilling To Perform", "Loop Detect", null, null, null, null, null,
        null, null, null, null, "Naming Violation", "Object Class Violation",
        "Not Allowed On Non-leaf", "Not Allowed On RDN", "Entry Already Exists",
        "Object Class Modifications Prohibited", null, "Affects Multiple DSAs", null,
        null, null, null, null, null, null, null,"Other", null, null, null, null,
        null, null, null, null, null,null
    };
}	//	LdapResult
