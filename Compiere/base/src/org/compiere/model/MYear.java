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
import java.text.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.process.*;
import org.compiere.util.*;


/**
 *	Year Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MYear.java,v 1.5 2006/10/11 04:12:39 jjanke Exp $
 */
public class MYear extends X_C_Year
{
    /** Logger for class MYear */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MYear.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Year
	 *	@param ctx context
	 *	@param C_Year_ID id
	 *	@return year or null
	 */
	public static MYear get (Ctx ctx, int C_Year_ID)
	{
		MYear year = s_years.get(ctx, C_Year_ID);
		if (year != null)
			return year;
		//
		String sql = "SELECT * FROM C_Year WHERE C_Year_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, C_Year_ID);
			rs = pstmt.executeQuery();
			if (rs.next())	//	first only
				year = new MYear(ctx, rs, null);
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return year;
	}	//	get
	
	/**	Cache							*/
	private static final CCache<Integer,MYear> s_years = new CCache<Integer,MYear>("C_Year", 10);
	
	/**	Logger							*/
	private static final CLogger		s_log = CLogger.getCLogger (MYear.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Year_ID id
	 *	@param trx transaction
	 */
	public MYear (Ctx ctx, int C_Year_ID, Trx trx)
	{
		super (ctx, C_Year_ID, trx);
		if (C_Year_ID == 0)
		{
		//	setC_Calendar_ID (0);
		//	setYear (null);
			setProcessing (false);	// N
		}		
	}	//	MYear

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MYear (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MYear
	
	/**
	 * 	Parent Constructor
	 *	@param calendar parent
	 */
	public MYear (MCalendar calendar)
	{
		this (calendar.getCtx(), 0, calendar.get_Trx());
		setClientOrg(calendar);
		setC_Calendar_ID(calendar.getC_Calendar_ID());
		setYear();
	}	//	MYear
	
	
	/**
	 * 	Set current Year
	 */
	private void setYear ()
	{
		GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
		String Year = String.valueOf(cal.get(Calendar.YEAR));
		super.setFiscalYear(Year);
	}	//	setYear
	
	/**
	 * 	Get Year As Int
	 *	@return year as int or 0
	 */
	public int getYearAsInt()
	{
		String year = getFiscalYear();
		try
		{
			return Integer.parseInt(year);
		}
		catch (Exception e)
		{
			StringTokenizer st = new StringTokenizer(year, "/-, \t\n\r\f");
			if (st.hasMoreTokens())
			{
				String year2 = st.nextToken();
				try
				{
					return Integer.parseInt(year2);
				}
				catch (Exception e2)
				{
					log.log(Level.WARNING, year + "->" + year2 + " - " + e2.toString());
				}
			}
			else
				log.log(Level.WARNING, year + " - " + e.toString());
		}
		return 0;
	}	//	getYearAsInt
	
	/**
	 * 	Get last two characters of year
	 *	@return 01
	 */
	public String getYY()
	{
		int yy = getYearAsInt();
		String year = String.valueOf(yy);
		if (year.length() == 4)
			return year.substring(2, 4);
		return getFiscalYear();
	}	//	getYY
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MYear[");
		sb.append(get_ID()).append("-")
			.append(getFiscalYear())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true if can be saved
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		int yy = getYearAsInt();
		if (yy == 0)
		{
			log.saveError("FillMandatory", Msg.getElement(getCtx(), "Year") 
				+ " -> " + yy + " (2006 - 2006/07 - 2006-07 - ...)");
			return false;
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	Create 12 Standard (Jan-Dec) Periods.
	 * 	Creates also Period Control from DocType.
	 * 	@see DocumentTypeVerify#createPeriodControls(Ctx, int, SvrProcess, String)
	 * 	@param locale locale 
	 *	@return true if created
	 */
	public boolean createStdPeriods(Locale locale)
	{
		if (locale == null)
		{
			MClient client = MClient.get(getCtx());
			locale = client.getLocale();
		}
		
		if (locale == null && Language.getLoginLanguage() != null)
			locale = Language.getLoginLanguage().getLocale();
		if (locale == null)
			locale = Env.getLanguage(getCtx()).getLocale();
		//
		String[] months = null;
		try
		{
			DateFormatSymbols symbols = new DateFormatSymbols(locale);
			months = symbols.getShortMonths();
		}
		catch (Exception e)
		{
			months = new String[]{"Jan", "Feb", "Mar",
				"Apr", "May", "Jun",
				"Jul", "Aug", "Sep",
				"Oct", "Nov", "Dec"};
		}
		//
		int year = getYearAsInt();
		GregorianCalendar cal = new GregorianCalendar(locale);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//
		for (int month = 0; month < 12; month++)
		{
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			Timestamp start = new Timestamp(cal.getTimeInMillis());
			String name = months[month] + "-" + getYY();
			//
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DAY_OF_YEAR, -1);
			Timestamp end = new Timestamp(cal.getTimeInMillis());
			//
			MPeriod period = new MPeriod (this, month+1, name, start, end);
			if (!period.save(get_Trx()))	//	Creates Period Control
				return false;
		}
		return true;
	}	//	createStdPeriods
	
}	//	MYear
