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
 *	RfQ Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRfQ.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MRfQ extends X_C_RfQ
{
    /** Logger for class MRfQ */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MRfQ.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get MRfQ from Cache
	 *	@param ctx context
	 *	@param C_RfQ_ID id
	 *	@return MRfQ
	 */
	public static MRfQ get (Ctx ctx, int C_RfQ_ID)
	{
		Integer key = Integer.valueOf (C_RfQ_ID);
		MRfQ retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MRfQ (ctx, C_RfQ_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static final CCache<Integer,MRfQ>	s_cache	= new CCache<Integer,MRfQ>("C_RfQ", 10);
	
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_RfQ_ID id
	 *	@param trx transaction
	 */
	public MRfQ (Ctx ctx, int C_RfQ_ID, Trx trx)
	{
		super (ctx, C_RfQ_ID, trx);
		if (C_RfQ_ID == 0)
		{
		//	setC_RfQ_Topic_ID (0);
		//	setName (null);
		//	setC_Currency_ID (0);	// @$C_Currency_ID @
		//	setSalesRep_ID (0);
			//
			setDateResponse (new Timestamp(System.currentTimeMillis()));
			setDateWorkStart (new Timestamp(System.currentTimeMillis()));
			setIsInvitedVendorsOnly (false);
			setQuoteType (QUOTETYPE_QuoteSelectedLines);
			setIsQuoteAllQty (false);
			setIsQuoteTotalAmt (false);
			setIsRfQResponseAccepted (true);
			setIsSelfService (true);
			setProcessed (false);
		}
	}	//	MRfQ

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MRfQ (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MRfQ
	
	/**
	 * 	Get active Lines
	 *	@return array of lines
	 */
	public MRfQLine[] getLines()
	{
		ArrayList<MRfQLine> list = new ArrayList<MRfQLine>();
		String sql = "SELECT * FROM C_RfQLine "
			+ "WHERE C_RfQ_ID=? AND IsActive='Y' "
			+ "ORDER BY Line";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getC_RfQ_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MRfQLine (getCtx(), rs, get_Trx()));
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
		MRfQLine[] retValue = new MRfQLine[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getLines

	/**
	 * 	Get RfQ Responses
	 * 	@param activeOnly active responses only
	 * 	@param completedOnly complete responses only
	 *	@return array of lines
	 */
	public MRfQResponse[] getResponses (boolean activeOnly, boolean completedOnly)
	{
		ArrayList<MRfQResponse> list = new ArrayList<MRfQResponse>();
		String sql = "SELECT * FROM C_RfQResponse "
			+ "WHERE C_RfQ_ID=?";
		if (activeOnly)
			sql += " AND IsActive='Y'";
		if (completedOnly)
			sql += " AND IsComplete='Y'";
		sql += " ORDER BY Price";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getC_RfQ_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MRfQResponse (getCtx(), rs, get_Trx()));
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
		MRfQResponse[] retValue = new MRfQResponse[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getResponses
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MRfQ[");
		sb.append(get_ID()).append(",Name=").append(getName())
			.append(",QuoteType=").append(getQuoteType())
			.append("]");
		return sb.toString ();
	}	//	toString
	
	
	/**************************************************************************
	 * 	Is Quote Total Amt Only
	 *	@return true if total amout only
	 */
	public boolean isQuoteTotalAmtOnly()
	{
		return QUOTETYPE_QuoteTotalOnly.equals(getQuoteType());
	}	//	isQuoteTotalAmtOnly
	
	/**
	 * 	Is Quote Selected Lines
	 *	@return true if quote selected lines
	 */
	public boolean isQuoteSelectedLines()
	{
		return QUOTETYPE_QuoteSelectedLines.equals(getQuoteType());
	}	//	isQuoteSelectedLines

	/**
	 * 	Is Quote All Lines
	 *	@return true if quote selected lines
	 */
	public boolean isQuoteAllLines()
	{
		return QUOTETYPE_QuoteAllLines.equals(getQuoteType());
	}	//	isQuoteAllLines

	/**
	 * 	Is "Quote Total Amt Only" Valid
	 *	@return null or error message
	 */
	public String checkQuoteTotalAmtOnly()
	{
		if (!isQuoteTotalAmtOnly())
			return null;
		//	Need to check Line Qty
		MRfQLine[] lines = getLines();
		for (MRfQLine line : lines) {
			MRfQLineQty[] qtys = line.getQtys();
			if (qtys.length > 1)
			{
				log.warning("isQuoteTotalAmtOnlyValid - #" + qtys.length + " - " + line);
				String msg = "@Line@ " + line.getLine() 
					+ ": #@C_RfQLineQty@=" + qtys.length + " - @IsQuoteTotalAmt@";
				return msg;
			}
		}
		return null;
	}	//	checkQuoteTotalAmtOnly
		
	
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
		
		return true;
	}	//	beforeSave
	
}	//	MRfQ
