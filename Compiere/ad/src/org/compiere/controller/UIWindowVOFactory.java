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
package org.compiere.controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;

import org.compiere.util.*;


/**
 * 	User Interface Window VO Factory
 *	
 *  @author Jorg Janke
 *  @version $Id: UIWindowVOFactory.java 8751 2010-05-12 16:49:31Z nnayak $
 */
public class UIWindowVOFactory extends UIWindowVOFT
{
	/**
	 *  Create Window Value Object for role
	 *  @param AD_Window_ID window id
	 *  @param AD_Menu_ID menu id
	 *  @return MWindowVO
	 */
	public UIWindowVO get (Ctx ctx, int AD_Window_ID, int AD_Menu_ID)
	{
		log.config("AD_Window_ID=" + AD_Window_ID + "; AD_Menu_ID=" + AD_Menu_ID);
		boolean IsReadOnly = false;

		//  Get Window_ID if required	- (used by HTML UI)
		if (AD_Window_ID == 0 && AD_Menu_ID != 0)
		{
			String sql = "SELECT AD_Window_ID, IsReadOnly FROM AD_Menu "
				+ "WHERE AD_Menu_ID=? AND Action='W'";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, (Trx) null);
				pstmt.setInt(1, AD_Menu_ID);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					AD_Window_ID = rs.getInt(1);
					IsReadOnly = "Y".equals(rs.getString(3));
				}
			}
			catch (SQLException e) {
				log.log(Level.SEVERE, sql, e);
				return null;
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}

			log.config("AD_Window_ID=" + AD_Window_ID);
		}

		//  --  Get Window
		UIWindowVO vo = get (ctx, AD_Window_ID);
		if (vo != null)
		{
			if (IsReadOnly)		//	Menu Overwrites
				vo.setIsReadWrite(false);
		}
		return vo;
	}   //  create

	/**
	 * 	Get Window VO for role
	 * 	@param ctx context
	 *	@param AD_Window_ID window
	 *	@return VO or null
	 */
	protected UIWindowVO get (Ctx ctx, int AD_Window_ID)
	{
		ArrayList<Object> params = new ArrayList<Object>();
		log.fine("AD_Window_ID=" + AD_Window_ID);
		
		int AD_Role_ID = ctx.getAD_Role_ID();

		params.add(AD_Window_ID);
		params.add(AD_Role_ID);

		String sql = "SELECT * FROM AD_Window_v WHERE AD_Window_ID=? AND AD_Role_ID=?";
		if (!Env.isBaseLanguage(ctx, "AD_Window")) {
			sql = "SELECT * FROM AD_Window_vt WHERE AD_Window_ID=? AND AD_Role_ID=? AND AD_Language=?";
			params.add(Env.getAD_Language(ctx));
		}

		UIWindowVO vo = get(sql, params);
		if (vo == null)
		{
			log.log(Level.SEVERE, "No Window - AD_Window_ID=" + AD_Window_ID
				+ ", AD_Role_ID=" + AD_Role_ID + " - " + sql);
			log.saveError("AccessTableNoView", "(Not found)");
			return null;
		}
		return vo;
	}	//	get
	
}	//	GridWindow_VOFactory
