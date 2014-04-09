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

import java.sql.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Update Role Access
 *	
 *  @author Jorg Janke
 *  @version $Id: RoleAccessUpdate.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class RoleAccessUpdate extends SvrProcess
{
	/**	Update Role				*/
	private int	p_AD_Role_ID = 0;
	/**	Update Roles of Client	*/
	private int	p_AD_Client_ID = 0;
	
	
	/**
	 * 	Prepare
	 */
	@Override
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) 
		{
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("AD_Role_ID"))
				p_AD_Role_ID = element.getParameterAsInt();
			else if (name.equals("AD_Client_ID"))
				p_AD_Client_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	@Override
	protected String doIt () throws Exception
	{
		log.info("AD_Client_ID=" + p_AD_Client_ID + ", AD_Role_ID=" + p_AD_Role_ID);
		//
		if (p_AD_Role_ID != 0)
			updateRole (new MRole (getCtx(), p_AD_Role_ID, get_TrxName()));
		else
		{
			String sql = "SELECT * FROM AD_Role ";
			if (p_AD_Client_ID != 0)
				sql += "WHERE AD_Client_ID=? ";
			sql += "ORDER BY AD_Client_ID, Name";

			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement (sql, get_TrxName());
				if (p_AD_Client_ID != 0)
					pstmt.setInt (1, p_AD_Client_ID);
				rs = pstmt.executeQuery ();
				while (rs.next ())
					updateRole (new MRole (getCtx(), rs, get_TrxName()));
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, sql, e);
			}
			finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		}
		
		return "";
	}	//	doIt

	/**
	 * 	Update Role
	 *	@param role role
	 */
	private void updateRole (MRole role)
	{
		addLog(0, null, null, role.getName() + ": " 
			+ role.updateAccessRecords());
	}	//	updateRole
	
}	//	RoleAccessUpdate
