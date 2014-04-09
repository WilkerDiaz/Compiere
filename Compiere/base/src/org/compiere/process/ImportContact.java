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

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Import Contacts
 *	
 *  @author Jorg Janke
 *  @version $Id$
 */
public class ImportContact extends SvrProcess
{
	/**	Client to be imported to		*/
	private int				p_AD_Client_ID = 0;
	/**	Delete old Imported				*/
	private boolean			p_deleteOldImported = false;

	/**
	 * 	Prepare
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) 
		{
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("AD_Client_ID"))
				p_AD_Client_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				p_deleteOldImported = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@return Summary
	 *	@throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID= ? ";

		//	****	Prepare	****

		//	Delete Old Imported
		if (p_deleteOldImported)
		{
			sql = new StringBuffer ("DELETE FROM I_Contact "
				+ "WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString(),p_AD_Client_ID);
			log.fine("Delete Old Impored =" + no);
		}

		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE I_Contact "
			+ "SET AD_Client_ID = COALESCE (AD_Client_ID,?), "
			+ " AD_Org_ID = COALESCE (AD_Org_ID, 0),"
			+ " IsActive = COALESCE (IsActive, 'Y'),"
			+ " Created = COALESCE (Created, SysDate),"
			+ " CreatedBy = COALESCE (CreatedBy, 0),"
			+ " Updated = COALESCE (Updated, SysDate),"
			+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
			+ " I_ErrorMsg = NULL,"
			+ " I_IsImported = 'N' "
			+ "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(get_TrxName(), sql.toString(),p_AD_Client_ID);
		log.fine("Reset=" + no);

		//	Interest Area
		sql = new StringBuffer ("UPDATE I_Contact i " 
			+ "SET R_InterestArea_ID=(SELECT R_InterestArea_ID FROM R_InterestArea ia "
				+ "WHERE i.InterestAreaName=ia.Name AND ia.AD_Client_ID=i.AD_Client_ID) "
			+ "WHERE R_InterestArea_ID IS NULL AND InterestAreaName IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString(),p_AD_Client_ID);
		log.fine("Set Interest Area=" + no);
		
		
		int noProcessed = 0;
		String sql0 = "SELECT * FROM I_Contact "
			+ "WHERE I_IsImported<>'Y' AND AD_Client_ID=? ORDER BY I_Contact_ID";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql0, get_TrxName());
			pstmt.setInt (1, p_AD_Client_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				if (process (new X_I_Contact (getCtx(), rs, get_TrxName())))
					noProcessed++;
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql0, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return "@Processed@ #" + noProcessed;
	}	//	doIt

	/**
	 * 	Process Import
	 *	@param imp import
	 *	@return true if processed
	 */
	private boolean process (X_I_Contact imp)
	{
		if (imp.getEMail() == null || imp.getEMail().length() == 0)
			return processFail (imp, "No EMail");
		
		MUser user = MUser.get (getCtx(), imp.getEMail(), get_TrxName());
		//	New User
		if (user == null || user.getAD_User_ID() == 0)
		{
			if (imp.isEMailBounced())
				return processFail(imp, "No User found with email - cannot set Bounced flag");
			if (imp.getContactName() == null || imp.getContactName().length() == 0)
				return processFail(imp, "No Name for User/Contact");
			
			user = new MUser (getCtx(), 0, get_TrxName());
			user.setName (imp.getContactName());
			user.setDescription (imp.getContactDescription());
			user.setEMail (imp.getEMail());
		}
		//	Existing User
		else
		{
			if (imp.isEMailBounced())
			{
				user.setIsEMailBounced (true);
				user.setBouncedInfo (imp.getBouncedInfo());
			}
		}
		if (!user.save())
			return processFail(imp, "Cannot save User");
		
		//	Create BP
		if (imp.isCreateBP())
		{
			if (user.getC_BPartner_ID() == 0)
			{
				MBPartner bp = new MBPartner(getCtx(), 0, get_TrxName());
				bp.setName (user.getName());
				if (!bp.save())
					return processFail(imp, "Cannot create BPartner");
				else
				{
					user.setC_BPartner_ID (bp.getC_BPartner_ID());
					if (!user.save())
						return processFail(imp, "Cannot update User");
				}
			}
			imp.setC_BPartner_ID (user.getC_BPartner_ID());
		}
		
		//	Create Lead
		if (imp.isCreateLead())
		{
			MLead lead = new MLead(getCtx(), 0, get_TrxName());
			lead.setName (imp.getContactName());
			lead.setDescription (imp.getContactDescription());
			lead.setAD_User_ID(user.getAD_User_ID ());
			lead.setC_BPartner_ID (user.getC_BPartner_ID());
			lead.save();
			imp.setC_Lead_ID (lead.getC_Lead_ID());
		}
		
		//	Interest Area
		if (imp.getR_InterestArea_ID() != 0 && user != null)
		{
			MContactInterest ci = MContactInterest.get(getCtx(), 
				imp.getR_InterestArea_ID(), user.getAD_User_ID(), 
				true, get_TrxName());
			ci.save();		//	don't subscribe or re-activate
		}
		
		imp.setAD_User_ID (user.getAD_User_ID());
		imp.setI_IsImported (true);
		imp.setI_ErrorMsg (null);
		imp.save();
		return true;
	}	//	process
	
	/**
	 * 	Process Fail
	 *	@param imp import
	 *	@param errorMsg error message
	 *	@return false
	 */
	private boolean processFail (X_I_Contact imp, String errorMsg)
	{
		imp.setI_IsImported(false);
		imp.setI_ErrorMsg(errorMsg);
		imp.save();
		
		return false;
	}	//	processFail
	
}	//	ImportContact
