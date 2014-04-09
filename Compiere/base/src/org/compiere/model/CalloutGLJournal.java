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
import java.util.logging.*;

import org.compiere.util.*;

/**
 *	GL Journal Callout
 *
 *  @author Jorg Janke
 *  @version $Id: CalloutGLJournal.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class CalloutGLJournal extends CalloutEngine
{
	/** Logger					*/
	private CLogger		log = CLogger.getCLogger(getClass());

	/**
	 *  Journal - Period.
	 *  Check that selected period is in DateAcct Range or Adjusting Period
	 *  Called when C_Period_ID or DateAcct, DateDoc changed
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String period (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		String colName = mField.getColumnName();
		if (value == null || isCalloutActive())
			return "";
		setCalloutActive(true);

		int AD_Client_ID = ctx.getContextAsInt( WindowNo, "AD_Client_ID");
		Timestamp DateAcct = null;
		if (colName.equals("DateAcct"))
			DateAcct = (Timestamp)value;
		else
			DateAcct = (Timestamp)mTab.getValue("DateAcct");
		int C_Period_ID = 0;
		if (colName.equals("C_Period_ID"))
			C_Period_ID = ((Integer)value).intValue();

		//  When DateDoc is changed, update DateAcct
		if (colName.equals("DateDoc"))
		{
			mTab.setValue("DateAcct", value);
		}

		//  When DateAcct is changed, set C_Period_ID
		else if (colName.equals("DateAcct"))
		{
			String sql = "SELECT C_Period_ID "
				+ "FROM C_Period "
				+ "WHERE C_Year_ID IN "
				+ "	(SELECT C_Year_ID FROM C_Year WHERE C_Calendar_ID ="
				+ "  (SELECT C_Calendar_ID FROM AD_ClientInfo WHERE AD_Client_ID=?))"
				+ " AND ? BETWEEN StartDate AND EndDate"
				+ " AND PeriodType='S'";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, (Trx) null);
				pstmt.setInt(1, AD_Client_ID);
				pstmt.setTimestamp(2, DateAcct);
				rs = pstmt.executeQuery();
				if (rs.next())
					C_Period_ID = rs.getInt(1);
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql, e);
				setCalloutActive(false);
				return e.getLocalizedMessage();
			}
			finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			if (C_Period_ID != 0)
				mTab.setValue("C_Period_ID", Integer.valueOf(C_Period_ID));
		}

		//  When C_Period_ID is changed, check if in DateAcct range and set to end date if not
		else
		{
			String sql = "SELECT PeriodType, StartDate, EndDate "
				+ "FROM C_Period WHERE C_Period_ID=?";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, (Trx) null);
				pstmt.setInt(1, C_Period_ID);
				rs = pstmt.executeQuery();
				if (rs.next())
				{
					String PeriodType = rs.getString(1);
					Timestamp StartDate = rs.getTimestamp(2);
					Timestamp EndDate = rs.getTimestamp(3);
					if (PeriodType.equals("S")) //  Standard Periods
					{
						//  out of range - set to last day
						if (DateAcct == null
							|| DateAcct.before(StartDate) || DateAcct.after(EndDate))
							mTab.setValue("DateAcct", EndDate);
					}
				}
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql, e);
				setCalloutActive(false);
				return e.getLocalizedMessage();
			}
			finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		}
		setCalloutActive(false);
		return "";
	}   //  	Journal_Period

	/**
	 * 	Journal/Line - rate.
	 * 	Set CurrencyRate from DateAcct, C_ConversionType_ID, C_Currency_ID
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	@Override
	public String rate (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (value == null)
			return "";

		//  Source info
		Integer Currency_ID = (Integer)mTab.getValue("C_Currency_ID");
		int C_Currency_ID = Currency_ID.intValue();
		Integer ConversionType_ID = (Integer)mTab.getValue("C_ConversionType_ID");
		int C_ConversionType_ID = ConversionType_ID.intValue();
		Timestamp DateAcct = (Timestamp)mTab.getValue("DateAcct");
		if (DateAcct == null)
			DateAcct = new Timestamp(System.currentTimeMillis());
		//
		int C_AcctSchema_ID = ctx.getContextAsInt( WindowNo, "C_AcctSchema_ID");
		MAcctSchema as = MAcctSchema.get (ctx, C_AcctSchema_ID);
		int AD_Client_ID = ctx.getContextAsInt( WindowNo, "AD_Client_ID");
		int AD_Org_ID = ctx.getContextAsInt( WindowNo, "AD_Org_ID");

		BigDecimal CurrencyRate = MConversionRate.getRate(C_Currency_ID, as.getC_Currency_ID(),
			DateAcct, C_ConversionType_ID, AD_Client_ID, AD_Org_ID);
		log.fine("rate = " + CurrencyRate);
		if (CurrencyRate == null)
			CurrencyRate = Env.ZERO;
		mTab.setValue("CurrencyRate", CurrencyRate);

		return "";
	}	//	rate

	/**
	 *  JournalLine - Amt.
	 *  Convert the source amount to accounted amount (AmtAcctDr/Cr)
	 *  Called when source amount (AmtSourceCr/Dr) or rate changes
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String amt (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
	//	String colName = mField.getColumnName();
		if (value == null || isCalloutActive())
			return "";

		setCalloutActive(true);

		//  Get Target Currency & Precision from C_AcctSchema.C_Currency_ID
		int C_AcctSchema_ID = ctx.getContextAsInt( WindowNo, "C_AcctSchema_ID");
		MAcctSchema as = MAcctSchema.get(ctx, C_AcctSchema_ID);
		int Precision = as.getStdPrecision();

		BigDecimal CurrencyRate = (BigDecimal)mTab.getValue("CurrencyRate");
		if (CurrencyRate == null)
		{
			CurrencyRate = Env.ONE;
			mTab.setValue("CurrencyRate", CurrencyRate);
		}

		//  AmtAcct = AmtSource * CurrencyRate  ==> Precision
		BigDecimal AmtSourceDr = (BigDecimal)mTab.getValue("AmtSourceDr");
		if (AmtSourceDr == null)
			AmtSourceDr = Env.ZERO;
		BigDecimal AmtSourceCr = (BigDecimal)mTab.getValue("AmtSourceCr");
		if (AmtSourceCr == null)
			AmtSourceCr = Env.ZERO;

		BigDecimal AmtAcctDr = AmtSourceDr.multiply(CurrencyRate);
		AmtAcctDr = AmtAcctDr.setScale(Precision, BigDecimal.ROUND_HALF_UP);
		mTab.setValue("AmtAcctDr", AmtAcctDr);
		BigDecimal AmtAcctCr = AmtSourceCr.multiply(CurrencyRate);
		AmtAcctCr = AmtAcctCr.setScale(Precision, BigDecimal.ROUND_HALF_UP);
		mTab.setValue("AmtAcctCr", AmtAcctCr);

		setCalloutActive(false);
		return "";
	}   //  amt

	/**
	 *  JournalLine - Alias.
	 *  Populate Combination if alias is provided and vice versa
	 *	@param ctx context
	 *	@param WindowNo window no
	 *	@param mTab tab
	 *	@param mField field
	 *	@param value value
	 *	@return null or error message
	 */
	public String alias (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		String colName = mField.getColumnName();
		if (value == null || isCalloutActive())
			return "";

		setCalloutActive(true);

		int C_Combination_ID = (Integer)value;


		if (colName.equals("C_AccountAlias_ID"))
			mTab.setValue("C_ValidCombination_ID", C_Combination_ID);
		else if (colName.equals("C_ValidCombination_ID"))
			mTab.setValue("C_AccountAlias_ID", C_Combination_ID);

		setCalloutActive(false);

		return "";
	}


}	//	CalloutGLJournal
