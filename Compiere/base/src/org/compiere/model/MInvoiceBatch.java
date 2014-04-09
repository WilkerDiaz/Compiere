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
 *	Invoice Batch Header Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MInvoiceBatch.java,v 1.2 2006/07/30 00:51:03 jjanke Exp $
 */
public class MInvoiceBatch extends X_C_InvoiceBatch
{
    /** Logger for class MInvoiceBatch */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MInvoiceBatch.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_InvoiceBatch_ID id
	 *	@param trx p_trx
	 */
	public MInvoiceBatch (Ctx ctx, int C_InvoiceBatch_ID, Trx trx)
	{
		super (ctx, C_InvoiceBatch_ID, trx);
		if (C_InvoiceBatch_ID == 0)
		{
		//	setDocumentNo (null);
		//	setC_Currency_ID (0);	// @$C_Currency_ID@
			setControlAmt (Env.ZERO);	// 0
			setDateDoc (new Timestamp(System.currentTimeMillis()));	// @#Date@
			setDocumentAmt (Env.ZERO);
			setIsSOTrx (false);	// N
			setProcessed (false);
		//	setSalesRep_ID (0);
		}
	}	//	MInvoiceBatch

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MInvoiceBatch (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MInvoiceBatch
	
	/**	The Lines						*/
	private MInvoiceBatchLine[]	m_lines	= null;

	
	/**
	 * 	Get Lines
	 *	@param reload reload data
	 *	@return array of lines
	 */
	public MInvoiceBatchLine[] getLines (boolean reload)
	{
		if (m_lines != null && !reload)
			return m_lines;
		String sql = "SELECT * FROM C_InvoiceBatchLine WHERE C_InvoiceBatch_ID=? ORDER BY Line";
		ArrayList<MInvoiceBatchLine> list = new ArrayList<MInvoiceBatchLine>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getC_InvoiceBatch_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MInvoiceBatchLine (getCtx(), rs, get_Trx()));
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//
		m_lines = new MInvoiceBatchLine[list.size ()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getLines

	
	/**
	 * 	Set Processed
	 *	@param processed processed
	 */
	@Override
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		String set = "SET Processed='"
			+ (processed ? "Y" : "N")
			+ "' WHERE C_InvoiceBatch_ID= ? ";
		int noLine = DB.executeUpdate(get_Trx(), "UPDATE C_InvoiceBatchLine " + set,getC_InvoiceBatch_ID());
		m_lines = null;
		log.fine(processed + " - Lines=" + noLine);
	}	//	setProcessed
	
}	//	MInvoiceBatch
