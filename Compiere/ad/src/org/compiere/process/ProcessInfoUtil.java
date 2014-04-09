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
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 * 	Process Info with Utilities
 *
 *  @author Jorg Janke
 *  @version $Id: ProcessInfoUtil.java 8776 2010-05-19 17:50:56Z nnayak $
 */
public class ProcessInfoUtil
{
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (ProcessInfoUtil.class);

	
	/**************************************************************************
	 *	Query PInstance for result.
	 *  Fill Summary and success in ProcessInfo
	 * 	@param pi process info
	 */
	public static void setSummaryFromDB (ProcessInfo pi)
	{
	//	s_log.fine("setSummaryFromDB - AD_PInstance_ID=" + pi.getAD_PInstance_ID());
		//
		int sleepTime = 2000;	//	2 secomds
		int noRetry = 5;        //  10 seconds total
		//
		String sql = "SELECT Result, ErrorMsg FROM AD_PInstance "
			+ "WHERE AD_PInstance_ID=?"
			+ " AND Result IS NOT NULL";

		for (int noTry = 0; noTry < noRetry; noTry++)
		{
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				pstmt = DB.prepareStatement (sql, 
						ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, null);
				pstmt.setInt(1, pi.getAD_PInstance_ID());
				rs = pstmt.executeQuery();
				if (rs.next())
				{
					//	we have a result
					int i = rs.getInt(1);
					if (i == 1)
						pi.setSummary(Msg.getMsg(Env.getCtx(), "Success"));
					else
						pi.setSummary(Msg.getMsg(Env.getCtx(), "Failure"), true);

					String Message = rs.getString(2);
					if (Message != null)
						pi.addSummary ("  (" +  Msg.parseTranslation(Env.getCtx(), Message)  + ")");
					return;
				}
			}
			catch (SQLException e)
			{
				s_log.log(Level.SEVERE, sql, e);
				pi.setSummary (e.getLocalizedMessage(), true);
				return;
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}

			//	sleep
			try {
				s_log.fine("sleeping");
				Thread.sleep(sleepTime);
			}
			catch (InterruptedException ie)	{
				s_log.log(Level.SEVERE, "Sleep Thread", ie);
			}
		}

		pi.setSummary (Msg.getMsg(Env.getCtx(), "Timeout"), true);
	}	

	/**
	 *	Set Log of Process.
	 * 	@param pi process info
	 */
	public static void setLogFromDB (ProcessInfo pi)
	{
	//	s_log.fine("setLogFromDB - AD_PInstance_ID=" + pi.getAD_PInstance_ID());
		String sql = "SELECT Log_ID, P_ID, P_Date, P_Number, P_Msg "
			+ "FROM AD_PInstance_Log "
			+ "WHERE AD_PInstance_ID=? "
			+ "ORDER BY Log_ID";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, pi.getAD_PInstance_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			//	int Log_ID, int P_ID, Timestamp P_Date, BigDecimal P_Number, String P_Msg
				pi.addLog (rs.getInt(1), rs.getInt(2), rs.getTimestamp(3), rs.getBigDecimal(4), rs.getString(5));
		}
		catch (SQLException e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}	//	getLogFromDB

	/**
	 *  Create Process Log
	 * 	@param pi process info
	 */
	public static void saveLogToDB (Ctx ctx, ProcessInfo pi)
	{
		ProcessInfoLog[] logs = pi.getLogs();
		if (logs == null || logs.length == 0)
		{
			s_log.fine("No Log");
			return;
		}
		if (pi.getAD_PInstance_ID() == 0)
		{
			s_log.log(Level.WARNING, "AD_PInstance_ID==0");
			return;
		}
		
		String sql = "INSERT INTO AD_PInstance_Log "
				+ "(AD_Client_ID, AD_Org_ID, AD_PInstance_ID, Log_ID, P_Date, P_ID, P_Number, P_Msg)"
				+ " VALUES (?,?,?,?, ?, ?, ?, ?) ";
		List<Object[]> bulkParams = new ArrayList<Object[]>();
		
				
		for (ProcessInfoLog element : logs) 
		{
			ArrayList<Object> params = new ArrayList<Object>();
			params.add(ctx.getAD_Client_ID());
			params.add(ctx.getAD_Org_ID());
			params.add(pi.getAD_PInstance_ID());
			params.add(element.getLog_ID());
			
			if(element.getP_Date() != null)
				params.add(element.getP_Date());
			else
				params.add(new NullParameter(Types.TIMESTAMP));
				
			if(element.getP_ID() == 0)
				params.add(new NullParameter(Types.INTEGER));
			else
				params.add(element.getP_ID());
			
			if(element.getP_Number() == null)
				params.add(new NullParameter(Types.DECIMAL));
			else
				params.add(element.getP_Number());
			
			if(element.getP_Msg() == null)
				params.add(new NullParameter(Types.NVARCHAR));
			else
				params.add(element.getP_Msg());
			bulkParams.add(params.toArray());
		}
		
		DB.executeBulkUpdate(null, sql, bulkParams, true, true);
		
	}   //  saveLogToDB

	/**
	 *  Set Parameter of Process (and Client/User)
	 * 	@param pi Process Info
	 */
	public static void setParameterFromDB (ProcessInfo pi)
	{
		ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
		String sql = "SELECT p.ParameterName,"         			    	//  1
			+ " p.P_String,p.P_String_To, p.P_Number,p.P_Number_To,"    //  2/3 4/5
			+ " p.P_Date,p.P_Date_To, p.Info,p.Info_To, "               //  6/7 8/9
			+ " i.AD_Client_ID, i.AD_Org_ID, i.AD_User_ID "				//	10..12
			+ "FROM AD_PInstance_Para p"
			+ " INNER JOIN AD_PInstance i ON (p.AD_PInstance_ID=i.AD_PInstance_ID) "
			+ "WHERE p.AD_PInstance_ID=? "
			+ "ORDER BY p.SeqNo";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, pi.getAD_PInstance_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				String ParameterName = rs.getString(1);
				//	String
				Object Parameter = rs.getString(2);
				Object Parameter_To = rs.getString(3);
				//	Big Decimal
				if (Parameter == null && Parameter_To == null)
				{
					Parameter = rs.getBigDecimal(4);
					Parameter_To = rs.getBigDecimal(5);
				}
				//	Timestamp
				if (Parameter == null && Parameter_To == null)
				{
					Parameter = rs.getTimestamp(6);
					Parameter_To = rs.getTimestamp(7);
				}
				//	Info
				String Info = rs.getString(8);
				String Info_To = rs.getString(9);
				//
				list.add (new ProcessInfoParameter(ParameterName, Parameter, Parameter_To, Info, Info_To));
				//
				if (pi.getAD_Client_ID() == null)
					pi.setAD_Client_ID (rs.getInt(10));
				if (pi.getAD_User_ID() == null)
					pi.setAD_User_ID(rs.getInt(12));
			}
		}
		catch (SQLException e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
		list.toArray(pars);
		pi.setParameter(pars);
	}   //  setParameterFromDB


}	//	ProcessInfoUtil
