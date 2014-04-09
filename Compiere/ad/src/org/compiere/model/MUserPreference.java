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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Trx;

/**
 * 	User Preference Model
 *	@author Jorg Janke
 */
public class MUserPreference extends X_AD_UserPreference
{
    /** Logger for class MUserPreference */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MUserPreference.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get User Preference
	 *	@param user user
	 *	@param createNew create new if not found
	 *	@return user preference
	 */
	public static MUserPreference getOfUser (MUser user, boolean createNew)
	{
		MUserPreference retValue = null;
		String sql = "SELECT * FROM AD_UserPreference WHERE AD_User_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, user.getAD_User_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = new MUserPreference(user.getCtx(), rs, null);
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		if (retValue == null && createNew)
		{
			retValue = new MUserPreference(user.getCtx(), 0, null);
			retValue.setClientOrg(user);
			retValue.setAD_User_ID(user.getAD_User_ID());
			retValue.save();
		}
		return retValue;
	}	//	getOfUser

	/**	Logger						*/
	protected static final CLogger s_log = CLogger.getCLogger(MUserPreference.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_UserPreference_ID id
	 *	@param trx p_trx
	 */
	public MUserPreference(Ctx ctx, int AD_UserPreference_ID, Trx trx)
	{
		super(ctx, AD_UserPreference_ID, trx);
		if (AD_UserPreference_ID == 0)
		{
			setIsAutoCommit(true);
			setIsShowAcct(false);
			setIsShowAdvanced(false);
			setIsShowTrl(false);
		}
	}	//	MUserPreference

	/**
	 * @param ctx
	 * @param rs
	 * @param trx
	 */
	public MUserPreference(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MUserPreference


	/**
	 * 	Set User
	 * 	@param AD_User_ID user
	 */
	@Override
	public void setAD_User_ID(int AD_User_ID)
	{
		if (AD_User_ID == 0)
			set_ValueNoCheck("AD_User_ID", Integer.valueOf (0));
		else
			super.setAD_User_ID(AD_User_ID);
	}	//	setAD_User_ID
	
	
	/**
	 *	String representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MUserPreference[")
			.append(get_ID())
			.append(",AD_User_ID=").append(getAD_User_ID())
			.append(",AutoCommit=").append(isAutoCommit())
			.append(",ShowAcct=").append(isShowAcct())
			.append(",ShowAdv=").append(isShowAdvanced())
			.append(",ShowTrl=").append(isShowTrl());
		if (getPrinterName() != null)
			sb.append(",PrinterName=").append(getPrinterName());
		if (getUITheme() != null)
			sb.append(",UITheme=").append(getUITheme());
		sb.append("]");
		return sb.toString();
	}	//	toString
	
}	//	MUserPreference
