/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.api;


/**
 * 	Login Message Interface.
 * 	Use this class to overwrite default behavior
 *	@author Jorg Janke
 */
public interface LoginMsgInterface
{
	/**
	 * 	Send the login message to the user
	 *	@param int AD_LoginMsg_ID login message id
	 *	@param AD_User_ID user id
	 *  @param isAdministrator is the user logged in with an administrator privileges
	 *	@return should that message be sent to that user?
	 */
	boolean isDisplayMsg(int AD_LoginMsg_ID, int AD_User_ID, boolean isAdministrator);
	
	/**
	 * 	Get optional additional text added to the message
	 *	@param int AD_LoginMsg_ID login message id
	 *	@param AD_User_ID user id
	 *	@return null or message
	 */
	String getAdditionalText(int AD_LoginMsg_ID, int AD_User_ID);
	
	
}	//	LoginMsgInterface
