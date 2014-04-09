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
 * 	Process to delete/Truncate temporary data
 *	@author Jorg Janke
 */
public class TemporaryDelete extends SvrProcess
{
	/** Keep old data			*/
	private int		p_KeepTemporaryDays = 2;


	/**
	 * 	Prepare
	 *	@see org.compiere.process.SvrProcess#prepare()
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
			else if (name.equals("KeepTemporaryDays"))
				p_KeepTemporaryDays = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@see org.compiere.process.SvrProcess#doIt()
	 */
	@Override
	protected String doIt() throws Exception
	{
		int AD_Client_ID = getCtx().getAD_Client_ID();
		log.info("KeepTemporaryDays=" + p_KeepTemporaryDays
			+ ",AD_Client_ID=" + AD_Client_ID);

		int count = 0;

		String sql = "SELECT * FROM AD_Table t "
			+ "WHERE IsReportingTable='Y' AND IsView='N'"
			+ "ORDER BY 1";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
	        pstmt = DB.prepareStatement(sql, get_TrxName());
	        rs = pstmt.executeQuery();
	        while (rs.next())
	        {
	        	MTable table = new MTable(getCtx(), rs, null);
	        	if (table.isReportingTable())
	        		count += deleteData(AD_Client_ID, table);
	        }
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

		return "#" + count;
	}	//	doIt

	/**
	 * 	Delete Data
	 *	@param AD_Client_ID client
	 *	@param table the table
	 *	@return rows deleted
	 */
	private int deleteData (int AD_Client_ID, MTable table)
	{
		String sql = "DELETE FROM " + table.getTableName();		
		if (AD_Client_ID != 0){
			MColumn column = table.getColumn("AD_Client_ID");
			if (column != null)
				sql += " WHERE AD_Client_ID=" + AD_Client_ID;
			else {
				addLog(table.getTableName() + " #0, it may contain data from other tenants");
				return 0;
			}
		}
		if (p_KeepTemporaryDays > 0)
		{
			if (table.getColumn("Created") != null)
			{
				if (AD_Client_ID != 0)
					sql += " AND Created < SysDate-";
				else
					sql += " WHERE Created < SysDate-";
				sql += p_KeepTemporaryDays;
			}
		}


		//	Truncate
		if ((p_KeepTemporaryDays == 0) && (AD_Client_ID == 0))
			sql = "TRUNCATE TABLE " + table.getTableName();

		int count = DB.executeUpdate(get_TrxName(), sql);

		addLog(table.getTableName() + " #" + count);
		return count;
	}	//	deleteData


}	//	TemporaryDelete
