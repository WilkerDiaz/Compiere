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

import org.compiere.util.*;

/**
 *	Invoice Schedule Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MInvoiceSchedule.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MInvoiceSchedule extends X_C_InvoiceSchedule
{
    /** Logger for class MInvoiceSchedule */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MInvoiceSchedule.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get MInvoiceSchedule from Cache
	 *	@param ctx context
	 *	@param C_InvoiceSchedule_ID id
	 *	@return MInvoiceSchedule
	 */
	public static MInvoiceSchedule get (Ctx ctx, int C_InvoiceSchedule_ID)
	{
		Integer key = Integer.valueOf (C_InvoiceSchedule_ID);
		MInvoiceSchedule retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MInvoiceSchedule (ctx, C_InvoiceSchedule_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static final CCache<Integer,MInvoiceSchedule>	s_cache	= new CCache<Integer,MInvoiceSchedule>("C_InvoiceSchedule", 5);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_InvoiceSchedule_ID id
	 *	@param trx transaction
	 */
	public MInvoiceSchedule (Ctx ctx, int C_InvoiceSchedule_ID, Trx trx)
	{
		super (ctx, C_InvoiceSchedule_ID, trx);
	}	//	MInvoiceSchedule

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MInvoiceSchedule (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MInvoiceSchedule
	
	/**
	 * 	Can I send Invoice
	 * 	@param xDate date
	 * 	@param orderAmt order amount
	 *	@return true if I can send Invoice
	 */
	public boolean canInvoice (Timestamp xDate, BigDecimal orderAmt)
	{
		//	Amount
		if (isAmount() && getAmt() != null && orderAmt != null 
			&& orderAmt.compareTo(getAmt()) >= 0)
			return true;
		
		//	Daily
		if (INVOICEFREQUENCY_Daily.equals(getInvoiceFrequency()))
			return true;

		//	Remove time
		xDate = TimeUtil.getDay(xDate);
		Calendar today = TimeUtil.getToday();
		
		//	Weekly
		if (INVOICEFREQUENCY_Weekly.equals(getInvoiceFrequency()))
		{
			Calendar cutoff = TimeUtil.getToday();
			cutoff.set(Calendar.DAY_OF_WEEK, getCalendarDay(getInvoiceWeekDayCutoff()));
			if (cutoff.after(today))
				cutoff.add(Calendar.DAY_OF_YEAR, -7);
			Timestamp cutoffDate = new Timestamp (cutoff.getTimeInMillis());
			log.fine("canInvoice - Date=" + xDate + " > Cutoff=" + cutoffDate 
				+ " - " + xDate.after(cutoffDate));
			if (xDate.after(cutoffDate))
				return false;
			//
			Calendar invoice = TimeUtil.getToday();
			invoice.set(Calendar.DAY_OF_WEEK, getCalendarDay(getInvoiceWeekDay()));
			if (invoice.after(today))
				invoice.add(Calendar.DAY_OF_YEAR, -7);
			Timestamp invoiceDate = new Timestamp (invoice.getTimeInMillis());
			log.fine("canInvoice - Date=" + xDate + " > Invoice=" + invoiceDate 
				+ " - " + xDate.after(invoiceDate));
			if (xDate.after(invoiceDate))
				return false;
			return true;
		}
		
		//	Monthly
		if (INVOICEFREQUENCY_Monthly.equals(getInvoiceFrequency())
			|| INVOICEFREQUENCY_TwiceMonthly.equals(getInvoiceFrequency()))
		{
			if (getInvoiceDayCutoff() > 0)
			{
				Calendar cutoff = TimeUtil.getToday();
				//get the cut off Invoice day or the last day of the month whichever is lower	
				int max = cutoff.getActualMaximum(Calendar.DAY_OF_MONTH);				
				int invoiceDay = getInvoiceDayCutoff();
				int correctInvoiceDay = 0;
				if(invoiceDay < max )
					correctInvoiceDay = invoiceDay;
				else
					correctInvoiceDay = max;
				cutoff.set(Calendar.DAY_OF_MONTH, correctInvoiceDay );
				if (cutoff.after(today))
					cutoff.add(Calendar.MONTH, -1);
				Timestamp cutoffDate = new Timestamp (cutoff.getTimeInMillis());
				log.fine("canInvoice - Date=" + xDate + " > Cutoff=" + cutoffDate 
					+ " - " + xDate.after(cutoffDate));
				if (xDate.after(cutoffDate))
					return false;
			}
			Calendar invoice = TimeUtil.getToday(); 
			//get the Invoice day or the last day of the month whichever is lower		
			int max = invoice.getActualMaximum(Calendar.DAY_OF_MONTH);
			int invoiceDay = getInvoiceDay();
			int correctInvoiceDay = 0;
			if(invoiceDay < max )
				correctInvoiceDay = invoiceDay;
			else
				correctInvoiceDay = max;
			invoice.set(Calendar.DAY_OF_MONTH, correctInvoiceDay);
			if (invoice.after(today))
				invoice.add(Calendar.MONTH, -1);
			Timestamp invoiceDate = new Timestamp (invoice.getTimeInMillis());
			log.fine("canInvoice - Date=" + xDate + " > Invoice=" + invoiceDate 
				+ " - " + xDate.after(invoiceDate));
			if (xDate.after(invoiceDate))
				return false;
			return true;
		}

		//	Bi-Monthly (+15)
		if (INVOICEFREQUENCY_TwiceMonthly.equals(getInvoiceFrequency()))
		{
			if (getInvoiceDayCutoff() > 0)
			{
				Calendar cutoff = TimeUtil.getToday();
				cutoff.set(Calendar.DAY_OF_MONTH, getInvoiceDayCutoff() +15);
				if (cutoff.after(today))
					cutoff.add(Calendar.MONTH, -1);
				Timestamp cutoffDate = new Timestamp (cutoff.getTimeInMillis());
				if (xDate.after(cutoffDate))
					return false;
			}
			Calendar invoice = TimeUtil.getToday();
			invoice.set(Calendar.DAY_OF_MONTH, getInvoiceDay() +15);
			if (invoice.after(today))
				invoice.add(Calendar.MONTH, -1);
			Timestamp invoiceDate = new Timestamp (invoice.getTimeInMillis());
			if (xDate.after(invoiceDate))
				return false;
			return true;
		}
		return false;
	}	//	canInvoice
	
	/**
	 * 	Convert to Calendar day
	 *	@param day Invoice Week Day
	 *	@return day
	 */
	private int getCalendarDay (String day)
	{
		if (INVOICEWEEKDAY_Friday.equals(day))
			return Calendar.FRIDAY;
		if (INVOICEWEEKDAY_Saturday.equals(day))
			return Calendar.SATURDAY;
		if (INVOICEWEEKDAY_Sunday.equals(day))
			return Calendar.SUNDAY;
		if (INVOICEWEEKDAY_Monday.equals(day))
			return Calendar.MONDAY;
		if (INVOICEWEEKDAY_Tuesday.equals(day))
			return Calendar.TUESDAY;
		if (INVOICEWEEKDAY_Wednesday.equals(day))
			return Calendar.WEDNESDAY;
	//	if (INVOICEWEEKDAY_Thursday.equals(day))
		return Calendar.THURSDAY;
	}	//	getCalendarDay
	
}	//	MInvoiceSchedule
