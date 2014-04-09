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

import java.sql.Timestamp;

import org.compiere.api.*;
import org.compiere.model.*;

import com.compiere.client.*;

/**
 *  Login Message
 *
 *  @author     Ray Thng
 *  @version    $Id: LicenseMessage.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class LicenseMessage implements LoginMsgInterface
{
	private static boolean initialized = false;
	private static String noLicense = null;
	private static String expiringLicense = null;
	private static String expiredLicense = null;
	private static String expiredLicenseAdmin = null;
	private static String expiredLicenseUser = null;
	private static String expiredLicenseFinal = null;

	public LicenseMessage(){

		if (initialized) return;

		StringBuffer noLicenseSB = new StringBuffer();
		StringBuffer expiringLicenseSB = new StringBuffer();
		StringBuffer expiredLicenseAdminSB = new StringBuffer();
		StringBuffer expiredLicenseUserSB = new StringBuffer();
		StringBuffer expiredLicenseFinalSB = new StringBuffer();

		MEntityType[] installedModules = MEntityType.getEntityTypes(Env.getCtx(), true);
		
		for (MEntityType module : installedModules) {
			String entityName = module.getName();
			String entityTypeCode = module.getEntityType();
			SysEnv license = SysEnv.get(entityTypeCode);

			// No License
			if ((license==null)||
				!license.checkLicense()&&!license.isLicensed()&&!license.isAssetExpired()){
				noLicenseSB.append(entityName+Env.NL);
			}
			// Expiring/Expired License
			else {
				Timestamp expirationDate = license.getGuaranteeDate();
				if (expirationDate!= null){
					long expDate = expirationDate.getTime();
					long now = System.currentTimeMillis();
					long day = 24*60*60*1000;
					String msg = entityName+" ("+expirationDate.toString().substring(0, 10)+")"+Env.NL;					 
					if (now > expDate+31*day)
						expiredLicenseFinalSB.append(msg);
					else if (now > expDate+7*day){ // Users start to get expired warning only after 7 days
						expiredLicenseUserSB.append(msg);
						expiredLicenseAdminSB.append(msg);
					}
					else if (now > expDate) // Admins start to get expired warning right away
						expiredLicenseAdminSB.append(msg);
					else if (now > expDate-30*day)
						expiringLicenseSB.append(msg);
				}
			}
		}
		if (noLicenseSB.length()>0){
			noLicense = noLicenseSB.substring(0, noLicenseSB.length()-2);
		}
		if (expiringLicenseSB.length()>0){
			expiringLicense = expiringLicenseSB.substring(0, expiringLicenseSB.length()-2);
		}		
		if (expiredLicenseUserSB.length()>0){
			expiredLicenseUser = expiredLicenseUserSB.substring(0, expiredLicenseUserSB.length()-2);
		}		
		if (expiredLicenseAdminSB.length()>0){
			expiredLicenseAdmin = expiredLicenseAdminSB.substring(0, expiredLicenseAdminSB.length()-2);
		}
		if (expiredLicenseFinalSB.length()>0){
			expiredLicenseFinal = expiredLicenseFinalSB.substring(0, expiredLicenseFinalSB.length()-2);
		}
		
		initialized = true;
	}
	
	/**
	 * 	Send the login message to the user
	 *	@param int AD_LoginMsg_ID login message id
	 *	@param AD_User_ID user id
	 *  @param isAdministrator is the user logged in with an administrator privileges
	 *	@return should that message be sent to that user?
	 */
	public boolean isDisplayMsg(int AD_LoginMsg_ID, int AD_User_ID, boolean isAdministrator){		
		// No License
		if (AD_LoginMsg_ID == 100 && noLicense != null) 
			return true;
		
		// Expiring License
		if (AD_LoginMsg_ID == 101 && expiringLicense != null && isAdministrator){
			return true;
		}

		// Expired License Admins
		if (AD_LoginMsg_ID == 102 && expiredLicenseAdmin != null && isAdministrator){
			expiredLicense = expiredLicenseAdmin;
			return true;
		}

		// Expired License Users
		if (AD_LoginMsg_ID == 102 && expiredLicenseUser != null){
			expiredLicense = expiredLicenseUser;
			return true;
		}

		// Expired License Final
		if (AD_LoginMsg_ID == 103 && expiredLicenseFinal != null){
			return true;
		}

		return false;		
	}
	
	/**
	 * 	Get optional additional text added to the message
	 *	@param int AD_LoginMsg_ID login message id
	 *	@param AD_User_ID user id
	 *	@return null or message
	 */
	public String getAdditionalText(int AD_LoginMsg_ID, int AD_User_ID){		
		// No Valid License
		if (AD_LoginMsg_ID == 100 && noLicense != null) 
			return noLicense;
		
		// Expiring License
		if (AD_LoginMsg_ID == 101 && expiringLicense != null)
			return expiringLicense;

		// Expired License
		if (AD_LoginMsg_ID == 102 && expiredLicense != null)
			return expiredLicense;

		// Expired License Final
		if (AD_LoginMsg_ID == 103 && expiredLicenseFinal != null)
			return expiredLicenseFinal;

		return null;
	}
}	//	License Message
