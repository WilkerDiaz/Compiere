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
 *	Reset (delete) Allocations	
 *	
 *  @author Jorg Janke
 *  @version $Id: AllocationReset.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class AllocationReset extends SvrProcess
{
	/** BP Group				*/
	private int			p_C_BP_Group_ID = 0;
	/** BPartner				*/
	private int			p_C_BPartner_ID = 0;
	/** Date Acct From			*/
	private Timestamp	p_DateAcct_From = null;
	/** Date Acct To			*/
	private Timestamp	p_DateAcct_To = null;
	/** Allocation directly		*/
	private int			p_C_AllocationHdr_ID = 0;
	/** Transaction				*/
	private Trx			m_trx = null;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			log.fine("prepare - " + element);
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("C_BP_Group_ID"))
				p_C_BP_Group_ID = element.getParameterAsInt();
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = element.getParameterAsInt();
			else if (name.equals("C_AllocationHdr_ID"))
				p_C_AllocationHdr_ID = element.getParameterAsInt();
			else if (name.equals("DateAcct"))
			{
				p_DateAcct_From = (Timestamp)element.getParameter();
				p_DateAcct_To = (Timestamp)element.getParameter_To();
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare
	
	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info ("C_BP_Group_ID=" + p_C_BP_Group_ID + ", C_BPartner_ID=" + p_C_BPartner_ID
			+ ", DateAcct= " + p_DateAcct_From + " - " + p_DateAcct_To
			+ ", C_AllocationHdr_ID=" + p_C_AllocationHdr_ID);

		m_trx = Trx.get("AllocReset");
		int count = 0;

		if (p_C_AllocationHdr_ID != 0)
		{
			MAllocationHdr hdr = new MAllocationHdr(getCtx(), p_C_AllocationHdr_ID, m_trx);
			if (delete(hdr))
				count++;
			m_trx.close();
			return "@Deleted@ #" + count;
		}
		
		//	Selection
		StringBuffer sql = new StringBuffer("SELECT * FROM C_AllocationHdr ah "
			+ "WHERE EXISTS (SELECT * FROM C_AllocationLine al "
				+ "WHERE ah.C_AllocationHdr_ID=al.C_AllocationHdr_ID");
		if (p_C_BPartner_ID != 0)
			sql.append(" AND al.C_BPartner_ID=?");
		else if (p_C_BP_Group_ID != 0)
			sql.append(" AND EXISTS (SELECT * FROM C_BPartner bp "
					+ "WHERE bp.C_BPartner_ID=al.C_BPartner_ID AND bp.C_BP_Group_ID=?)");
		else
			sql.append(" AND AD_Client_ID=?");
		if (p_DateAcct_From != null)
			sql.append(" AND TRIM(ah.DateAcct) >= ?");
		if (p_DateAcct_To != null)
			sql.append(" AND TRIM(ah.DateAcct) <= ?");
		//	Do not delete Cash Trx
		sql.append(" AND al.C_CashLine_ID IS NULL)");
		//	Open Period
		sql.append(" AND EXISTS (SELECT * FROM C_Period p"
			+ " INNER JOIN C_PeriodControl pc ON (p.C_Period_ID=pc.C_Period_ID AND pc.DocBaseType='CMA') "
			+ "WHERE ah.DateAcct BETWEEN p.StartDate AND p.EndDate)");
		//
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), m_trx);
			int index = 1;
			if (p_C_BPartner_ID != 0)
				pstmt.setInt(index++, p_C_BPartner_ID);
			else if (p_C_BP_Group_ID != 0)
				pstmt.setInt(index++, p_C_BP_Group_ID);
			else
				pstmt.setInt(index++, getCtx().getAD_Client_ID());
			if (p_DateAcct_From != null)
				pstmt.setTimestamp(index++, p_DateAcct_From);
			if (p_DateAcct_To != null)
				pstmt.setTimestamp(index++, p_DateAcct_To);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MAllocationHdr hdr = new MAllocationHdr(getCtx(), rs, m_trx);
				if (delete(hdr))
					count++;
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
			m_trx.rollback();
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		m_trx.close();
		return "@Deleted@ #" + count;
	}	//	doIt

	
	private boolean delete(MAllocationHdr hdr)
	{
	//	m_trx.start();
		boolean success = false;
		String msg = null;

		//	Std Period open?
		msg = DocumentEngine.isPeriodOpen(hdr);
		if (msg != null)
		{
			msg = "@DeleteError@" + hdr.getDocumentNo() + ": " + msg;
			log.warning (msg);
			return false;
		}
		
		if (hdr.delete(true, m_trx))
		{
			log.fine(hdr.toString());
			success = true;
		}
		if (success)
			m_trx.commit();
		else
			m_trx.rollback();
		return success;
	}	//	delete

}	//	AllocationReset
