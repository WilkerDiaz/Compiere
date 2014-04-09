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
 *	RfQ Response Line Model	
 *	
 *  @author Jorg Janke
 *  @version $Id: MRfQResponseLine.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MRfQResponseLine extends X_C_RfQResponseLine
{
    /** Logger for class MRfQResponseLine */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MRfQResponseLine.class);
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
	public MRfQResponseLine (Ctx ctx, int ignored, Trx trx)
	{
		super(ctx, 0, trx);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MRfQResponseLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MRfQResponseLine (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MRfQResponseLine
	
	/**
	 * 	Parent Constructor.
	 * 	Also creates qtys if RfQ Qty
	 * 	Is saved if there are qtys(!)
	 *	@param response response
	 *	@param line line
	 */
	public MRfQResponseLine (MRfQResponse response, MRfQLine line)
	{
		super (response.getCtx(), 0, response.get_Trx());
		setClientOrg(response);
		setC_RfQResponse_ID (response.getC_RfQResponse_ID());
		//
		setC_RfQLine_ID (line.getC_RfQLine_ID());
		//
		setIsSelectedWinner (false);
		setIsSelfService (false);
		//
		MRfQLineQty[] qtys = line.getQtys();
		for (MRfQLineQty element : qtys) {
			if (element.isActive() && element.isRfQQty())
			{
				if (get_ID() == 0)	//	save this line
					save();
				MRfQResponseLineQty qty = new MRfQResponseLineQty (this, element);
				qty.save();
			}
		}
	}	//	MRfQResponseLine
	
	/**	RfQ Line				*/
	private MRfQLine				m_rfqLine = null;
	/**	Quantities				*/
	private MRfQResponseLineQty[] 	m_qtys = null;
	
	/**
	 * 	Get Quantities
	 *	@return array of quantities
	 */
	public MRfQResponseLineQty[] getQtys ()
	{
		return getQtys (false);
	}	//	getQtys

	/**
	 * 	Get Quantities
	 * 	@param requery requery
	 *	@return array of quantities
	 */
	public MRfQResponseLineQty[] getQtys (boolean requery)
	{
		if (m_qtys != null && !requery)
			return m_qtys;
		
		ArrayList<MRfQResponseLineQty> list = new ArrayList<MRfQResponseLineQty>();
		String sql = "SELECT * FROM C_RfQResponseLineQty "
			+ "WHERE C_RfQResponseLine_ID=? AND IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getC_RfQResponseLine_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MRfQResponseLineQty(getCtx(), rs, get_Trx()));
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
		m_qtys = new MRfQResponseLineQty[list.size ()];
		list.toArray (m_qtys);
		return m_qtys;
	}	//	getQtys
	
	/**
	 * 	Get RfQ Line
	 *	@return rfq line
	 */
	public MRfQLine getRfQLine()
	{
		if (m_rfqLine == null)
			m_rfqLine = new MRfQLine(getCtx(), getC_RfQLine_ID(), get_Trx());
		return m_rfqLine;
	}	//	getRfQLine
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MRfQResponseLine[");
		sb.append(get_ID()).append(",Winner=").append(isSelectedWinner())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		//	Calculate Complete Date (also used to verify)
		if (getDateWorkStart() != null && getDeliveryDays() != 0)
			setDateWorkComplete (TimeUtil.addDays(getDateWorkStart(), getDeliveryDays()));
		//	Calculate Delivery Days
		else if (getDateWorkStart() != null && getDeliveryDays() == 0 && getDateWorkComplete() != null)
			setDeliveryDays (TimeUtil.getDaysBetween(getDateWorkStart(), getDateWorkComplete()));
		//	Calculate Start Date
		else if (getDateWorkStart() == null && getDeliveryDays() != 0 && getDateWorkComplete() != null)
			setDateWorkStart (TimeUtil.addDays(getDateWorkComplete(), getDeliveryDays() * -1));

		if (!isActive())
			setIsSelectedWinner(false);
		return true;
	}	//	beforeSave
	

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!isActive())
		{
			getQtys (false);
			for (MRfQResponseLineQty qty : m_qtys) {
				if (qty.isActive())
				{
					qty.setIsActive(false);
					qty.save();
				}
			}
		}
		return success;
	}	//	success
	
}	//	MRfQResponseLine
