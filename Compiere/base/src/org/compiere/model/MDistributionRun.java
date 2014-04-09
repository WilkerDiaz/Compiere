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
 *	Distribution Run Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MDistributionRun.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MDistributionRun extends X_M_DistributionRun
{
    /** Logger for class MDistributionRun */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MDistributionRun.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_DistributionRun_ID id
	 *	@param trx transaction
	 */
	public MDistributionRun (Ctx ctx, int M_DistributionRun_ID, Trx trx)
	{
		super (ctx, M_DistributionRun_ID, trx);
	}	//	MDistributionRun

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MDistributionRun (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MDistributionRun
	
	/**	 Cached Lines					*/
	private MDistributionRunLine[] 	m_lines = null;
	
	/**
	 * 	Get active, non zero lines
	 *	@param reload true if reload
	 *	@return lines
	 */
	public MDistributionRunLine[] getLines (boolean reload)
	{
		if (!reload && m_lines != null)
			return m_lines;
		//
		String sql = "SELECT * FROM M_DistributionRunLine "
			+ "WHERE M_DistributionRun_ID=? AND IsActive='Y' AND TotalQty IS NOT NULL AND TotalQty<> 0 ORDER BY Line";
		ArrayList<MDistributionRunLine> list = new ArrayList<MDistributionRunLine>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getM_DistributionRun_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MDistributionRunLine(getCtx(), rs, get_Trx()));
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
		m_lines = new MDistributionRunLine[list.size()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getLines
	
}	//	MDistributionRun
