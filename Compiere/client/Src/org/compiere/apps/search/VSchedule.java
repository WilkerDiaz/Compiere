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
package org.compiere.apps.search;

import java.sql.*;
import java.util.*;

import javax.swing.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Visual and Control Part of Schedule.
 *  Contains Time and Schedule Panels
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: VSchedule.java,v 1.3 2006/07/30 00:51:27 jjanke Exp $
 */
public class VSchedule extends JScrollPane
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Constructor
	 *  @param is InfoSchedule for call back
	 *  @param type Type of schedule TYPE_...
	 */
	public VSchedule (InfoSchedule is, Period type)
	{
		m_numDays = type;
		m_model = new ScheduleUtil(Env.getCtx());
		schedulePanel.setInfoSchedule(is);	//	for callback
        getViewport().add(schedulePanel);
	}	//	VSchedule

	/**	Period of time the schedule represents. */
    public static enum Period
    {
        DAY(Calendar.DAY_OF_MONTH),
        WEEK(Calendar.WEEK_OF_YEAR),
        MONTH(Calendar.MONTH);
        
        /** The type. */
        private final int type;
        
        /**
         * Creates a Period.
         * @param days
         */
        private Period(int type)
        {
            this.type = type;
        }

        /**
         * @return
         */
        public int type()
        {
            return type;
        }
    };

    /** The s_calendar. */
    private static Calendar s_calendar =
        Calendar.getInstance(Language.getLoginLanguage().getLocale());
    
    /** Logger          */
    private static CLogger log = CLogger.getCLogger(VSchedule.class);

	/** Type					*/
	private Period	m_numDays = Period.DAY;
    
	/** Model					*/
	private ScheduleUtil	m_model = null;
    
	/**	 Start Date				*/
	private Timestamp		m_startDate;
    
	/**	End Date				*/
	private Timestamp		m_endDate;

	/** The schedulePanel. */
	private VSchedulePanel schedulePanel = new VSchedulePanel();

	/**
	 * 	Recreate View
	 * 	@param S_Resource_ID Resource
	 * 	@param date Date
	 */
	public void recreate (int S_Resource_ID, Timestamp date)
	{
		//	Calculate Start Day
        s_calendar.setTime(date);
        s_calendar.set(Calendar.HOUR, 0);
        s_calendar.set(Calendar.MINUTE, 0);
        s_calendar.set(Calendar.SECOND, 0);
        s_calendar.set(Calendar.MILLISECOND, 0);
		if (m_numDays == Period.WEEK)
            s_calendar.set(Calendar.DAY_OF_WEEK, s_calendar.getFirstDayOfWeek());
		else if (m_numDays == Period.MONTH)
            s_calendar.set(Calendar.DAY_OF_MONTH, 1);
		m_startDate = new Timestamp(s_calendar.getTimeInMillis());
		
		//	Calculate End Date
        s_calendar.add(m_numDays.type(), 1);
		m_endDate = new Timestamp (s_calendar.getTimeInMillis());
		
		log.config("(" + m_numDays + ") Resource_ID=" + S_Resource_ID + ": " + m_startDate + "->" + m_endDate);
        
		//	Create Slots
		MAssignmentSlot[] mas =
            m_model.getAssignmentSlots(S_Resource_ID, m_startDate, m_endDate, null, true, null);
		MAssignmentSlot[] mts = m_model.getDayTimeSlots();
        
		//	Set Panels
		schedulePanel.setAssignmentSlots(mas, mts, S_Resource_ID, m_startDate, m_numDays);
	}	//	recreate

	/**
	 * 	Enable/disable to Create New Assignments
	 * 	@param createNew if true, allows to create new Assignments
	 */
	public void setCreateNew (boolean createNew)
	{
		schedulePanel.setCreatesNewAssignments(createNew);
	}	//	setCreateNew

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		m_model = null;
		if (schedulePanel != null)
			schedulePanel.dispose();
		schedulePanel = null;
		this.removeAll();
	}	//	dispose

	/**
	 * 	Get Start Date
	 * 	@return start date
	 */
	public Timestamp getStartDate()
	{
		return m_startDate;
	}	//	getStartDate

}	//	VSchedule
