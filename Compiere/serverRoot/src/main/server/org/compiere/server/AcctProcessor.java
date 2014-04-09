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
package org.compiere.server;

import java.sql.*;
import java.util.logging.*;

import org.compiere.acct.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Accounting Processor
 *	
 *  @author Jorg Janke
 *  @version $Id: AcctProcessor.java 8969 2010-06-18 08:09:13Z ragrawal $
 */
public class AcctProcessor extends CompiereServer
{
	/**
	 * 	Accounting Processor
	 *	@param model model 
	 */
	public AcctProcessor (MAcctProcessor model)
	{
		super (model, 30);	//	30 seconds delay
		m_model = model;
		m_client = MClient.get(model.getCtx(), model.getAD_Client_ID());
	}	//	AcctProcessor

	/**	The Concrete Model			*/
	private MAcctProcessor		m_model = null;
	/**	Last Summary				*/
	private StringBuffer 		m_summary = new StringBuffer();
	/** Client info					*/
	private MClient 			m_client = null;
	/**	Accounting Schemata			*/
	private MAcctSchema[] 		m_ass = null;
	/** Document Base Types			*/
	private MDocBaseType[]		m_docBaseTypes = null;
	
	/**
	 * 	Work
	 */
	@Override
	protected void doWork ()
	{
		m_summary = new StringBuffer();
		//	Get Schemata
		if (m_model.getC_AcctSchema_ID() == 0)
			m_ass = MAcctSchema.getClientAcctSchema(getCtx(), m_model.getAD_Client_ID());
		else	//	only specific accounting schema
			m_ass = new MAcctSchema[] {new MAcctSchema (getCtx(), m_model.getC_AcctSchema_ID(), null)};
		//
		postSession();
		MCost.create(m_client);
		//
		int no = m_model.deleteLog();
		m_summary.append("Logs deleted=").append(no);
		//
		MAcctProcessorLog pLog = new MAcctProcessorLog(m_model, m_summary.toString());
		pLog.setReference("#" + String.valueOf(p_runCount) 
			+ " - " + TimeUtil.formatElapsed(new Timestamp(p_startWork)));
		pLog.save();
	}	//	doWork

	/**
	 * 	Post Session
	 */
	private void postSession()
	{
		if (m_docBaseTypes == null)
			m_docBaseTypes = MDocBaseType.getAll(m_model.getCtx());
		
		for (int i = 0; i < m_docBaseTypes.length; i++)
		{
			MDocBaseType dbt = m_docBaseTypes[i];
			int AD_Table_ID = dbt.getAD_Table_ID();
			String TableName = dbt.getTableName();
			
			// Skip posting for warehouse tasks
			if (AD_Table_ID == 1018)
				continue;
			
			if (AD_Table_ID == 0 || TableName == null)
			{
				log.info("Skipped - Invalid: " + dbt);
				continue;
			}
			//	Post only special documents
			if (m_model.getAD_Table_ID() != 0 
				&& m_model.getAD_Table_ID() != AD_Table_ID)
				continue;
			//  SELECT * FROM table
			StringBuffer sql = new StringBuffer ("SELECT * FROM ").append(TableName)
				.append(" WHERE AD_Client_ID=?")	//	Include Document Errors
				.append(" AND Processed='Y' AND Posted IN ('N','e') AND IsActive='Y'")
				.append(" ORDER BY Created asc , ")
				.append(TableName).append("_ID asc");
			//
			int count = 0;
			int countError = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
				pstmt.setInt(1, m_model.getAD_Client_ID());
				rs = pstmt.executeQuery();
				while (!isInterrupted() && rs.next())
				{
					count++;
					boolean ok = true;
					Trx trx = Trx.get("Post " + TableName);
					try
					{
						Doc doc = Doc.get (m_ass, AD_Table_ID, rs, trx);
						if (doc == null)
						{
							log.severe(getName() + ": No Doc for " + TableName);
							ok = false;
						}
						else
						{
							String error = doc.post(false, false);   //  post no force/repost
							ok = error == null;
						}
					}
					catch (Exception e)
					{
						log.log(Level.SEVERE, getName() + ": " + TableName, e);
						ok = false;
					}
					finally
					{
						if( trx != null )
							trx.close();
					}
					if (!ok)
						countError++;
				}
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, sql.toString(), e);
			}
			finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			//
			if (count > 0)
			{
				m_summary.append(TableName).append("=").append(count);
				if (countError > 0)
					m_summary.append("(Errors=").append(countError).append(")");
				m_summary.append(" - ");
				log.fine(getName() + ": " + m_summary.toString());
			}
			else
				log.fine(getName() + ": " + TableName + " - no work");
		}
	}	//	postSession
	
	/**
	 * 	Get Server Info
	 *	@return info
	 */
	@Override
	public String getServerInfo()
	{
		return "#" + p_runCount + " - Last=" + m_summary.toString();
	}	//	getServerInfo

}	//	AcctProcessor
