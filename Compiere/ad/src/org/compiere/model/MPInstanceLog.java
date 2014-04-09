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

import java.math.*;
import java.sql.*;

import org.compiere.util.*;

/**
 *  Process Instance Log Model.
 * 	(not standard table)
 *
 *  @author Jorg Janke
 *  @version $Id: MPInstanceLog.java 8731 2010-05-06 19:58:12Z rthng $
 */
public class MPInstanceLog extends X_AD_PInstance_Log
{
    /** Logger for class MPInstanceLog */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPInstanceLog.class);
	/**
	 * 	Full Constructor
	 *	@param AD_PInstance_ID instance
	 *	@param Log_ID log sequence
	 *	@param P_Date date
	 *	@param P_ID id
	 *	@param P_Number number
	 *	@param P_Msg msg
	 */
	public MPInstanceLog (Ctx ctx, int AD_PInstance_ID, int Log_ID, Timestamp P_Date,
	  int P_ID, BigDecimal P_Number, String P_Msg, Trx trx)
	{
		super(ctx, 0, trx);
		setAD_PInstance_ID(AD_PInstance_ID);
		setLog_ID(Log_ID);
		setP_Date(P_Date);
		setP_ID(P_ID);
		setP_Number(P_Number);
		setP_Msg(P_Msg);
	}	//	MPInstance_Log

	/**
	 * 	Load Constructor
	 * 	@param rs Result Set
	 * 	@throws SQLException
	 */
	public MPInstanceLog (Ctx ctx, ResultSet rs, Trx trx) throws SQLException
	{
		super(ctx, rs, trx);
		setAD_PInstance_ID(rs.getInt("AD_PInstance_ID"));
		setLog_ID(rs.getInt("Log_ID"));
		setP_Date(rs.getTimestamp("P_Date"));
		setP_ID(rs.getInt("P_ID"));
		setP_Number(rs.getBigDecimal("P_Number"));
		setP_Msg(rs.getString("P_Msg"));
	}	//	MPInstance_Log

	/**
	 * 	String Representation
	 * 	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer("PPInstance_Log[");
		sb.append(getLog_ID());
		if (getP_Date() != null)
			sb.append(",Date=").append(getP_Date());
		if (getP_ID() != 0)
			sb.append(",ID=").append(getP_ID());
		if (getP_Number() != null)
			sb.append(",Number=").append(getP_Number());
		if (getP_Msg() != null)
			sb.append(",").append(getP_Msg());
		sb.append("]");
		return sb.toString();
	}	//	toString

} //	MPInstance_Log
