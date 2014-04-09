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

import java.util.*;

import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	UI Field Factory
 *	
 *  @author Jorg Janke
 *  @version $Id: UIFieldVOFactory.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class UIFieldVOFactory extends UIFieldVOFT
{
	
	
	
	public static MRole lUserRole = null; 
	
	/**
	 * 	Get Tab VOs for window
	 * 	@param ctx context (to get Language)
	 *	@param AD_Window_ID window
	 *	@param AD_UserDef_Win_ID optional customization
	 *	@return Tab VOs
	 */
	public ArrayList<UIFieldVO> getAll (Ctx ctx, int AD_Window_ID, int AD_UserDef_Win_ID)
	{
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(AD_Window_ID);
		log.info("AD_Window_ID=" + AD_Window_ID);
		//	Load Fields
		StringBuffer sql = new StringBuffer("SELECT * FROM AD_Field_v WHERE AD_Window_ID=?");
		if (!Env.isBaseLanguage(ctx, "AD_Field")) {
			sql = new StringBuffer("SELECT * FROM AD_Field_vt WHERE AD_Window_ID=? AND AD_Language=?");
			params.add(Env.getAD_Language(ctx));
		}
		if (AD_UserDef_Win_ID != 0) {
			sql.append(" AND AD_UserDef_Win_ID=?");
			params.add(AD_UserDef_Win_ID);
		}
		sql.append(" ORDER BY SeqNo");
		
		lUserRole = MRole.get(ctx, ctx.getAD_Role_ID());
		
		ArrayList<UIFieldVO> retValue = getAll(sql.toString(), params);
		return retValue;
	}	//	getALll

	/**
	 * 	Get Tab VOs for referenced tabs in window
	 * 	@param ctx context (to get Language)
	 *	@param AD_Tab_ID referenced tab
	 *	@param AD_UserDef_Win_ID optional customization
	 *	@return Tab VOs
	 */
	public ArrayList<UIFieldVO> getReferenced (Ctx ctx, int AD_Tab_ID, int AD_UserDef_Win_ID)
	{
		//	Load Fields
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(AD_Tab_ID);
		StringBuffer sql = new StringBuffer("SELECT * FROM AD_Field_v WHERE AD_Tab_ID=?");
		if (!Env.isBaseLanguage(ctx, "AD_Field")) {
			sql = new StringBuffer("SELECT * FROM AD_Field_vt WHERE AD_Tab_ID=? AND AD_Language=?");
			params.add(Env.getAD_Language(ctx));
		}
		if (AD_UserDef_Win_ID != 0) {
			sql.append(" AND AD_UserDef_Win_ID=?");
			params.add(AD_UserDef_Win_ID);
		}
		sql.append(" ORDER BY SeqNo");
		ArrayList<UIFieldVO> retValue = getAll(sql.toString(), params);
		return retValue;
	}	//	getReferenced
	
	/**
	 * 	Get Field
	 * 	@param ctx context (to get Language)
	 *	@param AD_Field_ID field
	 *	@return field or null
	 */
	public UIFieldVO get (Ctx ctx, int AD_Field_ID)
	{
		log.info("AD_Field_ID=" + AD_Field_ID);
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(AD_Field_ID);
		StringBuffer sql = new StringBuffer("SELECT * FROM AD_Field_v WHERE AD_Field_ID=?");
		if (!Env.isBaseLanguage(ctx, "AD_Field")) {
			sql = new StringBuffer("SELECT * FROM AD_Field_vt WHERE AD_Field_ID=? AND AD_Language=?");
			params.add(Env.getAD_Language(ctx));
		}
	//	sql.append(" AND AD_UserDef_Win_ID IS NULL");
		return get(sql.toString(), params);
	}	//	get

}	//	UIFieldVOFactory
