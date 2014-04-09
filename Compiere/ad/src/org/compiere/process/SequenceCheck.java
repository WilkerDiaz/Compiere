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
 *	System + Document Sequence Check
 *	
 *  @author Jorg Janke
 *  @version $Id: SequenceCheck.java 8780 2010-05-19 23:08:57Z nnayak $
 */
public class SequenceCheck extends SvrProcess
{
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (SequenceCheck.class);
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
	}	//	prepare
	
	/**
	 *  Perform process.
	 *  (see also MSequenve.validate)
	 *  @return Message to be translated
	 *  @throws Exception
	 */
	@Override
	protected String doIt() throws java.lang.Exception
	{
		log.info("");
		//
		checkTableSequences (Env.getCtx(), this);
		checkTableID (Env.getCtx(), this);
		checkClientSequences (Env.getCtx(), this);
		return "Sequence Check";
	}	//	doIt
	
	/**
	 *	Validate Sequences
	 *	@param ctx context
	 */
	public static void validate(Ctx ctx)
	{
		try
		{
			checkTableSequences (ctx, null);
			checkTableID (ctx, null);
			checkClientSequences (ctx, null);
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "validate", e);
		}
	}	//	validate
	
	
	
	/**************************************************************************
	 * 	Check existence of Table Sequences.
	 *	@param ctx context
	 *	@param sp server process or null
	 */
	private static void checkTableSequences (Ctx ctx, SvrProcess sp)
	{
		Trx trx = null;
		if (sp != null)
			trx = sp.get_TrxName();
		String sql = "SELECT TableName "
			+ "FROM AD_Table t "
			+ "WHERE IsActive='Y' AND IsView='N'"
			+ " AND NOT EXISTS (SELECT * FROM AD_Sequence s "
			+ "WHERE UPPER(s.Name)=UPPER(t.TableName) AND s.IsTableID='Y')";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				String tableName = rs.getString(1);
				if (MSequence.createTableSequence (ctx, tableName, trx))
				{
					if (sp != null)
						sp.addLog(0, null, null, tableName);
					else
						s_log.fine(tableName);
				}
				else
				{
					throw new Exception ("Error creating Table Sequence for " + tableName);
				}
			}
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		
		//	Sync Table Name case
		//jz replace s with AD_Sequence
		sql = "UPDATE AD_Sequence "
			+ "SET Name = (SELECT TableName FROM AD_Table t "
				+ "WHERE t.IsView='N' AND UPPER(AD_Sequence.Name)=UPPER(t.TableName)) "
			+ "WHERE AD_Sequence.IsTableID='Y'"
			+ " AND EXISTS (SELECT * FROM AD_Table t "
				+ "WHERE t.IsActive='Y' AND t.IsView='N'"
				+ " AND UPPER(AD_Sequence.Name)=UPPER(t.TableName) AND AD_Sequence.Name<>t.TableName)";
		int no = DB.executeUpdate(trx, sql);
		if (no > 0)
		{
			if (sp != null)
				sp.addLog(0, null, null, "SyncName #" + no);
			else
				s_log.fine("Sync #" + no);
		}
		if (no >= 0)
			return;
		
		/** Find Duplicates 		 */
		sql = "SELECT TableName, s.Name "
			+ "FROM AD_Table t, AD_Sequence s "
			+ "WHERE t.IsActive='Y' AND t.IsView='N'"
			+ " AND UPPER(s.Name)=UPPER(t.TableName) AND s.Name<>t.TableName";
		
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				String TableName = rs.getString(1);
				String SeqName = rs.getString(2);
				sp.addLog(0, null, null, "ERROR: TableName=" + TableName + " - Sequence=" + SeqName);
			}
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}	//	checkTableSequences
	

	/**
	 * 	Check Table Sequence ID values
	 *	@param ctx context
	 *	@param sp server process or null
	 */
	private static void checkTableID (Ctx ctx, SvrProcess sp)
	{
		int IDRangeEnd = QueryUtil.getSQLValue(null,
			"SELECT IDRangeEnd FROM AD_System");
		if (IDRangeEnd <= 0)
			IDRangeEnd = QueryUtil.getSQLValue(null,
				"SELECT MIN(IDRangeStart)-1 FROM AD_Replication");
		s_log.info("IDRangeEnd = " + IDRangeEnd);
		//
		String sql = "SELECT * FROM AD_Sequence "
			+ "WHERE IsTableID='Y' "
			+ "ORDER BY Name";
		int counter = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Trx trx = null;
		if (sp != null)
			trx = sp.get_TrxName();
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MSequence seq = new MSequence (ctx, rs, trx);
				int old = seq.getCurrentNext();
				int oldSys = seq.getCurrentNextSys();
				if (seq.validateTableIDValue())
				{
					if (seq.getCurrentNext() != old)
					{
						String msg = seq.getName() + " ID  " 
							+ old + " -> " + seq.getCurrentNext();
						if (sp != null)
							sp.addLog(0, null, null, msg);
						else
							s_log.fine(msg);
					}
					if (seq.getCurrentNextSys() != oldSys)
					{
						String msg = seq.getName() + " Sys " 
							+ oldSys + " -> " + seq.getCurrentNextSys();
						if (sp != null)
							sp.addLog(0, null, null, msg);
						else
							s_log.fine(msg);
					}
					if (seq.save())
						counter++;
					else
						s_log.severe("Not updated: " + seq);
				}
			//	else if (CLogMgt.isLevel(6)) 
			//		log.fine("OK - " + tableName);
			}
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		s_log.fine("#" + counter);
	}	//	checkTableID

	
	/**
	 *	Check/Initialize DocumentNo/Value Sequences for all Clients 	
	 *	@param ctx context
	 *	@param sp server process or null
	 */
	private static void checkClientSequences (Ctx ctx, SvrProcess sp)
	{
		Trx trx = null;
		if (sp != null)
			trx = sp.get_TrxName();
		//	Sequence for DocumentNo/Value
		MClient[] clients = MClient.getAll(ctx);
		for (MClient client : clients) {
			if (!client.isActive())
				continue;
			MSequence.checkClientSequences (ctx, client.getAD_Client_ID(), trx);
		}	//	for all clients
		
	}	//	checkClientSequences
	
}	//	SequenceCheck
