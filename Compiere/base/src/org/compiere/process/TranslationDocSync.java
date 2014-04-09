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

import org.compiere.common.constants.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Document Translation Sync 
 *	
 *  @author Jorg Janke
 *  @version $Id: TranslationDocSync.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class TranslationDocSync extends SvrProcess
{
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		MClient client = MClient.get(getCtx());
		if (client.isMultiLingualDocument())
			throw new CompiereUserException("@AD_Client_ID@: @IsMultiLingualDocument@");
		//
		log.info("" + client);
		String sql = "SELECT * FROM AD_Table "
			+ "WHERE TableName LIKE '%_Trl' AND TableName NOT LIKE 'AD%' "
			+ "ORDER BY TableName";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				processTable (new MTable(getCtx(), rs, null), client.getAD_Client_ID());
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return "OK";
	}	//	doIt
	
	/**
	 * 	Process Translation Table
	 *	@param table table
	 */
	private void processTable (MTable table, int AD_Client_ID)
	{
		StringBuffer sql = new StringBuffer();
		MColumn[] columns = table.getColumns(false);
		for (MColumn column : columns) {
			if (column.getAD_Reference_ID() == DisplayTypeConstants.String
				|| column.getAD_Reference_ID() == DisplayTypeConstants.Text)
			{
				String columnName = column.getColumnName();
				if (sql.length() != 0)
					sql.append(",");
				sql.append(columnName);
			}
		}
		String baseTable = table.getTableName();
		baseTable = baseTable.substring(0, baseTable.length()-4);
		
		log.config(baseTable + ": " + sql);
		String columnNames = sql.toString();
		
		sql = new StringBuffer();
		sql.append("UPDATE ").append(table.getTableName()).append(" t SET (")
			.append(columnNames).append(") = (SELECT ").append(columnNames)
			.append(" FROM ").append(baseTable).append(" b WHERE t.")
			.append(baseTable).append("_ID=b.").append(baseTable).append("_ID) WHERE AD_Client_ID= ? ");
		int no = DB.executeUpdate(get_TrxName(), sql.toString(),AD_Client_ID);
		addLog(0, null, new BigDecimal(no), baseTable);
	}	//	processTable
	
}	//	TranslationDocSync
