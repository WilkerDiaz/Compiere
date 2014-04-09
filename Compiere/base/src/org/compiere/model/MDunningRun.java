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
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 *	Dunning Run Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MDunningRun.java,v 1.2 2006/07/30 00:51:03 jjanke Exp $
 */
public class MDunningRun extends X_C_DunningRun
{
    /** Logger for class MDunningRun */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MDunningRun.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_DunningRun_ID id
	 *	@param trx transaction
	 */
	public MDunningRun (Ctx ctx, int C_DunningRun_ID, Trx trx)
	{
		super (ctx, C_DunningRun_ID, trx);
		if (C_DunningRun_ID == 0)
		{
		//	setC_DunningLevel_ID (0);
			setDunningDate (new Timestamp(System.currentTimeMillis()));
			setProcessed (false);
		}	
	}	//	MDunningRun

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MDunningRun (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MDunningRun

	private MDunningLevel		m_level = null;
	private MDunningRunEntry[]	m_entries = null;
	
	/**
	 * 	Get Dunning Level
	 *	@return level
	 */
	public MDunningLevel getLevel()
	{
		if (m_level == null)
			m_level = new MDunningLevel (getCtx(), getC_DunningLevel_ID(), get_Trx());
		return m_level;
	}	//	getLevel
	
	/**
	 * 	Get Entries
	 * 	@param requery requery
	 *	@return entries
	 */
	public MDunningRunEntry[] getEntries (boolean requery) 
	{
		return getEntries(requery, false);
	}
	
	/**
	 * 	Get Entries
	 * 	@param requery requery requery
	 *  @param onlyInvoices only invoices
	 *	@return entries
	 */
	public MDunningRunEntry[] getEntries (boolean requery, boolean onlyInvoices)
	{
		if (m_entries != null && !requery)
			return m_entries;
		
		String sql = "SELECT * FROM C_DunningRunEntry WHERE C_DunningRun_ID=?";
		ArrayList<MDunningRunEntry> list = new ArrayList<MDunningRunEntry>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getC_DunningRun_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MDunningRunEntry thisEntry = new MDunningRunEntry(getCtx(), rs, get_Trx());
				if (!(onlyInvoices && thisEntry.hasInvoices()))
					list.add (new MDunningRunEntry(getCtx(), rs, get_Trx()));
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		m_entries = new MDunningRunEntry[list.size ()];
		list.toArray (m_entries);
		return m_entries;
	}	//	getEntries
	
	/**
	 * 	Delete all Entries
	 * 	@param force delete also processed records
	 *	@return true if deleted
	 */
	public boolean deleteEntries(boolean force)
	{
		getEntries(true);
		for (MDunningRunEntry entry : m_entries) {
			entry.delete(force);
		}
		boolean ok = getEntries(true).length == 0;
		if (ok)
			m_entries = null;
		return ok;
	}	//	deleteEntries
	
	/**
	 * 	Get/Create Entry for BPartner
	 *	@param C_BPartner_ID business partner
	 *	@param C_Currency_ID currency
	 *	@param SalesRep_ID sales rep
	 *	@return entry
	 */
	public MDunningRunEntry getEntry (int C_BPartner_ID, int C_Currency_ID, int SalesRep_ID)
	{
		// TODO: Related BP
		int C_BPartnerRelated_ID = C_BPartner_ID;
		//
		getEntries(false);
		for (MDunningRunEntry entry : m_entries) {
			if (entry.getC_BPartner_ID() == C_BPartnerRelated_ID)
				return entry;
		}
		//	New Entry
		MDunningRunEntry entry = new MDunningRunEntry (this);
		MBPartner bp = new MBPartner (getCtx(), C_BPartnerRelated_ID, get_Trx());
		entry.setBPartner(bp, true);	//	AR hardcoded
		//
		if (entry.getSalesRep_ID() == 0)
			entry.setSalesRep_ID (SalesRep_ID);
		entry.setC_Currency_ID (C_Currency_ID);
		//
		m_entries = null;
		return entry;
	}	//	getEntry
	
}	//	MDunningRun
