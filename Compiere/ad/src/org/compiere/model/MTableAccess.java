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
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Jorg Janke
 *  @version $Id: MTableAccess.java 8755 2010-05-12 18:30:14Z nnayak $
 */
public class MTableAccess extends X_AD_Table_Access
{
    /** Logger for class MTableAccess */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MTableAccess.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 *	@param trx transaction
	 */
	public MTableAccess (Ctx ctx, int ignored, Trx trx)
	{
		super(ctx, 0, trx);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MTableAccess

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MTableAccess(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MTableAccess

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MTableAccess[");
		sb.append("AD_Role_ID=").append(getAD_Role_ID())
			.append(",AD_Table_ID=").append(getAD_Table_ID())
			.append(",Exclude=").append(isExclude())
			.append(",Type=").append(getAccessTypeRule());
		if (ACCESSTYPERULE_Accessing.equals(getAccessTypeRule()))
			sb.append("-ReadOnly=").append(isReadOnly());
		else if (ACCESSTYPERULE_Exporting.equals(getAccessTypeRule()))
			sb.append("-CanExport=").append(isCanExport());
		else if (ACCESSTYPERULE_Reporting.equals(getAccessTypeRule()))
			sb.append("-CanReport=").append(isCanReport());
		sb.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Extended String Representation
	 * 	@param ctx context
	 *	@return extended info
	 */
	public String toStringX (Ctx ctx)
	{
		String in = Msg.getMsg(ctx, "Include");
		String ex = Msg.getMsg(ctx, "Exclude");
		StringBuffer sb = new StringBuffer();
		sb.append(Msg.translate(ctx, "AD_Table_ID"))
			.append("=").append(getTableName(ctx));
		if (ACCESSTYPERULE_Accessing.equals(getAccessTypeRule()))
			sb.append(" - ").append(Msg.translate(ctx, "IsReadOnly")).append("=").append(isReadOnly());
		else if (ACCESSTYPERULE_Exporting.equals(getAccessTypeRule()))
			sb.append(" - ").append(Msg.translate(ctx, "IsCanExport")).append("=").append(isCanExport());
		else if (ACCESSTYPERULE_Reporting.equals(getAccessTypeRule()))
			sb.append(" - ").append(Msg.translate(ctx, "IsCanReport")).append("=").append(isCanReport());
		sb.append(" - ").append(isExclude() ?  ex : in);
		return sb.toString();
	}	//	toStringX

	/**	TableName			*/
	private String		m_tableName;

	/**
	 * 	Get Table Name
	 *	@param ctx context
	 *	@return table name
	 */
	public String getTableName (Ctx ctx)
	{
		if (m_tableName == null)
		{
			String sql = "SELECT TableName FROM AD_Table WHERE AD_Table_ID=?";
			PreparedStatement pstmt = null;
			ResultSet rs =  null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_Trx());
				pstmt.setInt(1, getAD_Table_ID());
				rs = pstmt.executeQuery();
				if (rs.next())
					m_tableName = rs.getString(1);
			}
			catch (Exception e) {
				log.log(Level.SEVERE, "getTableName", e);
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}

			//	Get Clear Text
			String realName = Msg.translate(ctx, m_tableName + "_ID");
			if (!realName.equals(m_tableName + "_ID"))
				m_tableName = realName;
		}		
		return m_tableName;
	}	//	getTableName

}	//	MTableAccess
