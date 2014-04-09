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

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 *	Distribution Run Detail
 *	
 *  @author Jorg Janke
 *  @version $Id: MDistributionRunDetail.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class MDistributionRunDetail extends X_T_DistributionRunDetail
{
    /** Logger for class MDistributionRunDetail */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MDistributionRunDetail.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Distribution Dun details
	 *	@param ctx context
	 *	@param M_DistributionRun_ID id
	 *	@param orderBP if true ordered by Business Partner otherwise Run Line
	 *	@param trx transaction
	 *	@return array of details
	 */
	static public MDistributionRunDetail[] get (Ctx ctx, int M_DistributionRun_ID, 
		boolean orderBP, Trx trx)
	{
		ArrayList<MDistributionRunDetail> list = new ArrayList<MDistributionRunDetail>();
		String sql = "SELECT * FROM T_DistributionRunDetail WHERE M_DistributionRun_ID=? ";
		if (orderBP)
			sql += "ORDER BY C_BPartner_ID, C_BPartner_Location_ID";
		else
			sql += "ORDER BY M_DistributionRunLine_ID";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, M_DistributionRun_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MDistributionRunDetail(ctx, rs, trx));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MDistributionRunDetail[] retValue = new MDistributionRunDetail[list.size()];
		list.toArray (retValue);
		return retValue;
	}	//	get
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MDistributionRunDetail.class);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param T_DistributionRunDetail_ID id
	 *	@param trx p_trx
	 */
	public MDistributionRunDetail (Ctx ctx, int T_DistributionRunDetail_ID, Trx trx)
	{
		super (ctx, T_DistributionRunDetail_ID, trx);
	}	//	MDistributionRunDetail
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MDistributionRunDetail (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	DistributionRunDetail
	
	/**	Precision		*/
	private int	m_precision = 0;
	
	/**
	 * 	Round MinQty & Qty
	 *	@param precision precision (saved)
	 */
	public void round (int precision)
	{
		boolean dirty = false;
		m_precision = precision; 
		BigDecimal min = getMinQty();
		if (min.scale() > m_precision)
		{
			setMinQty(min.setScale(m_precision, BigDecimal.ROUND_HALF_UP));
			dirty = true;
		}
		BigDecimal qty = getQty();
		if (qty.scale() > m_precision)
		{
			setQty(qty.setScale(m_precision, BigDecimal.ROUND_HALF_UP));
			dirty = true;
		}
		if (dirty)
			save();
	}	//	round
	
	/**
	 * 	We can adjust Allocation Qty
	 *	@return true if qty > min
	 */
	public boolean isCanAdjust()
	{
		return (getQty().compareTo(getMinQty()) > 0);
	}	//	isCanAdjust

	/**
	 * 	Get Actual Allocation Qty
	 *	@return the greater of the min/qty
	 */
	public BigDecimal getActualAllocation()
	{
		if (getQty().compareTo(getMinQty()) > 0)
			return getQty();
		else
			return getMinQty();
	}	//	getActualAllocation

	/**
	 * 	Adjust the Quantity maintaining UOM precision
	 * 	@param difference difference
	 * 	@return remaining difference (because under Min or rounding)
	 */
	public BigDecimal adjustQty (BigDecimal difference)
	{
		BigDecimal diff = difference.setScale(m_precision, BigDecimal.ROUND_HALF_UP);
		BigDecimal qty = getQty();
		BigDecimal max = getMinQty().subtract(qty);
		BigDecimal remaining = Env.ZERO;
		if (max.compareTo(diff) > 0)	//	diff+max are negative
		{
			remaining = diff.subtract(max);
			setQty(qty.add(max));
		}
		else
			setQty(qty.add(diff));
		log.fine("Qty=" + qty + ", Min=" + getMinQty() 
			+ ", Max=" + max + ", Diff=" + diff + ", newQty=" + getQty() 
			+ ", Remaining=" + remaining);
		return remaining;
	}	//	adjustQty
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MDistributionRunDetail[")
			.append (get_ID ())
			.append (";M_DistributionListLine_ID=").append (getM_DistributionListLine_ID())
			.append(";Qty=").append(getQty())
			.append(";Ratio=").append(getRatio())
			.append(";MinQty=").append(getMinQty())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	DistributionRunDetail
