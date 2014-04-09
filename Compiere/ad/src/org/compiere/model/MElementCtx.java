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

import java.sql.*;

import org.compiere.util.*;

/**
 * 	Element Context Model
 *	@author Jorg Janke
 */
public class MElementCtx extends X_AD_ElementCtx
{
    /** Logger for class MElementCtx */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MElementCtx.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_ElementCtx_ID id
	 *	@param trx p_trx
	 */
	public MElementCtx(Ctx ctx, int AD_ElementCtx_ID, Trx trx)
	{
		super(ctx, AD_ElementCtx_ID, trx);
	}	//	MElementCtx

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MElementCtx(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MElementCtx

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Update Fields, Parameters, Print Info
		StringBuffer sql = null;
		int no = 0;
		//	Field
		sql = new StringBuffer("UPDATE AD_Field SET Name=")
			.append(DB.TO_STRING(getName()))
			.append(", Description=").append(DB.TO_STRING(getDescription()))
			.append(", Help=").append(DB.TO_STRING(getHelp()))
			.append(" WHERE AD_Column_ID IN (SELECT AD_Column_ID FROM AD_Column WHERE AD_Element_ID=")
			.append(getAD_Element_ID())
			.append(") AND IsCentrallyMaintained='Y'")
			.append(" AND AD_Tab_ID IN (SELECT AD_Tab_ID FROM AD_Tab t INNER JOIN AD_Window w ON (t.AD_Window_ID=w.AD_Window_ID)")
			.append(" WHERE ((t.AD_CtxArea_ID=").append(getAD_CtxArea_ID())
			.append(" AND w.AD_CtxArea_ID IS NULL) OR w.AD_CtxArea_ID=").append(getAD_CtxArea_ID())
			.append("))");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Fields updated #" + no);
			
		//	Parameter
		/**
		sql = new StringBuffer("UPDATE AD_Process_Para SET Name=")
			.append(DB.TO_STRING(getName()))
			.append(", Description=").append(DB.TO_STRING(getDescription()))
			.append(", Help=").append(DB.TO_STRING(getHelp()))
			.append(", AD_Element_ID=").append(getAD_Element_ID())
			.append(" WHERE UPPER(ColumnName)=")
			.append(DB.TO_STRING(getColumnName().toUpperCase()))
			.append(" AND IsCentrallyMaintained='Y' AND AD_Element_ID IS NULL");
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		**/
		sql = new StringBuffer("UPDATE AD_Process_Para SET Name=")
			.append(DB.TO_STRING(getName()))
			.append(", Description=").append(DB.TO_STRING(getDescription()))
			.append(", Help=").append(DB.TO_STRING(getHelp()))
			.append(" WHERE AD_Element_ID=").append(getAD_Element_ID())
			.append(" AND IsCentrallyMaintained='Y'")
			.append(" AND AD_Process_ID IN (SELECT AD_Process_ID FROM AD_Process")
			.append(" WHERE AD_CtxArea_ID=").append(getAD_CtxArea_ID()).append(")");
		no += DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Parameters updated #" + no);
			
		/**	Print Info
		sql = new StringBuffer("UPDATE AD_PrintFormatItem pi SET PrintName=")
			.append(DB.TO_STRING(getPrintName()))
			.append(", Name=").append(DB.TO_STRING(getName()))
			.append(" WHERE IsCentrallyMaintained='Y'")	
			.append(" AND EXISTS (SELECT * FROM AD_Column c ")
				.append("WHERE c.AD_Column_ID=pi.AD_Column_ID AND c.AD_Element_ID=")
				.append(get_ID()).append(")");
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("PrintFormatItem updated #" + no);

		// Info Column
		sql = new StringBuffer ("UPDATE AD_InfoColumn SET Name=")
			.append(DB.TO_STRING(getName()))
			.append(", Description=").append(DB.TO_STRING(getDescription()))
			.append(", Help=").append(DB.TO_STRING(getHelp()))
			.append(" WHERE AD_Element_ID=").append(get_ID())
			.append(" AND IsCentrallyMaintained='Y'");
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("InfoWindow updated #" + no);
		/** **/
		return success;
	}	//	afterSave
	
}	//	MElementCtx
