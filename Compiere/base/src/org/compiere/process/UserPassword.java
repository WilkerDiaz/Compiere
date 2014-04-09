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

import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Reset Password
 *	
 *  @author Jorg Janke
 *  @version $Id: UserPassword.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class UserPassword extends SvrProcess
{
	private int			p_AD_User_ID = -1;
	private String 		p_OldPassword = null;
	private String 		p_CurrentPassword = null;
	private String 		p_NewPassword = null;
	private String		p_NewEMail = null;
	private String		p_NewEMailUser = null;
	private String		p_NewEMailUserPW = null;

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
			else if (name.equals("AD_User_ID"))
				p_AD_User_ID = element.getParameterAsInt();
			else if (name.equals("OldPassword"))
				p_OldPassword = (String)element.getParameter();
			else if (name.equals("CurrentPassword"))
				p_CurrentPassword = (String)element.getParameter();
			else if (name.equals("NewPassword"))
				p_NewPassword = (String)element.getParameter();
			else if (name.equals("NewEMail"))
				p_NewEMail = (String)element.getParameter();
			else if (name.equals("NewEMailUser"))
				p_NewEMailUser = (String)element.getParameter();
			else if (name.equals("NewEMailUserPW"))
				p_NewEMailUserPW = (String)element.getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info ("AD_User_ID=" + p_AD_User_ID + " from " + getAD_User_ID());
		
		// If User is not selected, use current user
		if (p_AD_User_ID == -1)
			p_AD_User_ID = getAD_User_ID();

		MUser user = MUser.get(getCtx(), p_AD_User_ID);
		MUser operator = MUser.get(getCtx(), getAD_User_ID());
		log.fine("User=" + user + ", Operator=" + operator);

		if (!operator.isAdministrator() && p_AD_User_ID != getAD_User_ID() && user.hasRole())
			throw new IllegalArgumentException("@UserCannotUpdate@");
		
		// SuperUser and System passwords can only be updated by themselves
		if (user.isSystemAdministrator() && p_AD_User_ID != getAD_User_ID())
			throw new IllegalArgumentException("@UserCannotUpdate@");
		
		// Current Password - Admin's current password
		// Old Password - User's old password
		if (Util.isEmpty(p_CurrentPassword))
		{
			if (Util.isEmpty(p_OldPassword))
				throw new IllegalArgumentException("@OldPasswordMandatory@");
			else if (!p_OldPassword.equals(user.getPassword()))
				throw new IllegalArgumentException("@OldPasswordNoMatch@");
		}
		else if (!operator.isAdministrator() && p_AD_User_ID != getAD_User_ID() && user.hasRole())
			throw new IllegalArgumentException("@UserCannotUpdate@");
		else if (!p_CurrentPassword.equals(operator.getPassword()))
			throw new IllegalArgumentException("@AdminPasswordNoMatch@");
		
		String sql = "UPDATE AD_User SET Updated=SysDate, UpdatedBy=" + getAD_User_ID();
		if (!Util.isEmpty(p_NewPassword))
		{
			MColumn column = MColumn.get(getCtx(), 417);
			if (column.isEncrypted() )
				p_NewPassword = SecureEngine.encrypt(p_NewPassword);				
			sql += ", Password=" + DB.TO_STRING(p_NewPassword);
		}
		if (!Util.isEmpty(p_NewEMail))
			sql += ", Email=" + DB.TO_STRING(p_NewEMail);
		if (!Util.isEmpty(p_NewEMailUser))
			sql += ", EmailUser=" + DB.TO_STRING(p_NewEMailUser);
		if (!Util.isEmpty(p_NewEMailUserPW))
			sql += ", EmailUserPW=" + DB.TO_STRING(p_NewEMailUserPW);
		sql += " WHERE AD_User_ID="+ p_AD_User_ID;
		if (DB.executeUpdate(get_TrxName(), sql) == 1)
		{
			user.setPassword( p_NewPassword );
			return "OK";
		}
		else 
			return "@Error@";
	}	//	doIt

}	//	UserPassword

