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
package org.compiere.util;

import java.io.*;
import java.util.logging.*;

import javax.mail.*;

/**
 *  Email User Authentication
 *
 *  @author Jorg Janke
 *  @version $Id: EMailAuthenticator.java 9194 2010-08-19 17:50:12Z rthng $
 */
public class EMailAuthenticator extends Authenticator implements Serializable
{
	/** */
    private static final long serialVersionUID = -1453447115062744524L;

	/**
	 * 	Constructor
	 * 	@param username user name
	 * 	@param password user password
	 */
	public EMailAuthenticator (String username, String password)
	{
		m_pass = new PasswordAuthentication (username, password);
		if (username == null || username.length() == 0)
		{
			log.log(Level.SEVERE, "Username is NULL");
			Thread.dumpStack();
		}
		if (password == null || password.length() == 0)
		{
			log.log(Level.SEVERE, "Password is NULL");
			Thread.dumpStack();
		}
	}	//	EMailAuthenticator

	/**	Password		*/
	transient private PasswordAuthentication 	m_pass = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(EMailAuthenticator.class);

	/**
	 *	Get PasswordAuthentication
	 * 	@return Password Authentication
	 */
	@Override
	protected PasswordAuthentication getPasswordAuthentication()
	{
		return m_pass;
	}	//	getPasswordAuthentication

	/**
	 * 	Get String representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		if (m_pass == null)
			return "EMailAuthenticator[]";
		if (CLogMgt.isLevelFinest())
			return "EMailAuthenticator["
				+ m_pass.getUserName() + "/" + m_pass.getPassword() + "]";
		return "EMailAuthenticator["
			+ m_pass.getUserName() + "/************]";
	}	//	toString

}	//	EMailAuthenticator
