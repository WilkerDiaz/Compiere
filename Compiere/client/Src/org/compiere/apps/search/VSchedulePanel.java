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

import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.*;

import org.compiere.grid.ed.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Schedule Panel
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: VSchedulePanel.java,v 1.2 2006/07/30 00:51:27 jjanke Exp $
 */
public class VSchedulePanel extends CTable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Constructor
	 */
	public VSchedulePanel ()
	{
        setRowHeight(getRowHeight() * 2);            
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
        {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table,
                                                           Object value,
                                                           boolean isSelected,
                                                           boolean hasFocus,
                                                           int row,
                                                           int column)
            {
                return super.getTableCellRendererComponent(table, null, false, false, row, column);
            }
        });
        
        DefaultListCellRenderer renderer = new DefaultListCellRenderer()
        {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private Dimension size;
            private StringBuilder buffer = new StringBuilder();
            private Rectangle rect = new Rectangle();
            private boolean drawTop;
            @Override
			public Component getListCellRendererComponent(JList list,
                                                          Object value,
                                                          int index,
                                                          boolean isSelected,
                                                          boolean cellHasFocus)
            {
                buffer.setLength(0);
                value = m_timeFormat.format(m_timeSlots[index].getStartTime());
                buffer.append(value).append(" ");
                drawTop = index == 0;
                Component comp = super.getListCellRendererComponent(list,
                                                                    buffer,
                                                                    index,
                                                                    false,
                                                                    false);
                setBackground(list.getBackground());
                setHorizontalAlignment(SwingConstants.RIGHT);
                if (size == null)
                {
                    size = getPreferredSize();
                    size.width += size.width * 0.3;
                }
                size.height = getRowHeight(index);
                setPreferredSize(size);
                return comp;
            }

            @Override
			public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.setColor(getGridColor());
                getBounds(rect);
                if (drawTop)
                    g.drawLine(rect.x, 0, rect.width, 0);
                rect.x = -1;
                rect.y = -1;
                ((Graphics2D)g).draw(rect);
            }
        };
        getRowHeader().setCellRenderer(renderer);
        
        m_renderer.setOpaque(false);
        
        getTableHeader().setReorderingAllowed(false);
        
        setDefaultEditor(Object.class, new TimeEditor());

        setCellSelectionEnabled(false);
        setRowSelectionAllowed(false);
        setColumnSelectionAllowed(false);
	}	//	VSchedulePanel

    /** The s_calendar. */
    private static final Calendar s_calendar =
        Calendar.getInstance(Language.getLoginLanguage().getLocale());
    
    private static final Composite s_composite =
        AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f);

    /** The s_compareCalendar. */
    private static Calendar s_compareCalendar =
        Calendar.getInstance(Language.getLoginLanguage().getLocale());

    /** Assignment Slots        */
    private TreeMap<MAssignmentSlot, Rectangle> m_assignments =
        new TreeMap<MAssignmentSlot, Rectangle>();
    
    /** TimePanel for layout info   */
    private MAssignmentSlot[]   m_timeSlots = null;
    
    /** Start Date              */
    private Timestamp           m_startDate = null;
    
    /** If true creates new assignments     */
    private boolean             m_createNew = false;
    
    /** The period. */
    private VSchedule.Period    m_period;

    /** Resource ID             */
    private int                 m_S_Resource_ID = 0;
    
    /** An InfoSchedule. */
    private InfoSchedule        m_infoSchedule = null;
    
    /** An InfoSchedule. */
    private StringBuilder       m_tempBuilder = new StringBuilder();
    
    /** A rendererPane. */
    private CellRendererPane m_rendererPane = new CellRendererPane();
    
    /** A renderer. */
    private DefaultTableCellRenderer m_renderer = new DefaultTableCellRenderer();
    
    /** A format. */
    private DateFormat m_dateFormat =
        DateFormat.getDateInstance(DateFormat.FULL, Language.getLoginLanguage().getLocale());
    
    /** A format. */
    private DateFormat m_timeFormat =
        DateFormat.getTimeInstance(DateFormat.SHORT, Language.getLoginLanguage().getLocale());
    
    /**
     * Calculates the physical bounds of an assignment slot 
     */
    private Rectangle calcTimeSlotRect(MAssignmentSlot slot)
    {
        s_calendar.setTime(m_startDate);
        int dayToColAdjust = (m_period == VSchedule.Period.WEEK) ?
            1 : s_calendar.get(Calendar.DAY_OF_YEAR);
        
        s_calendar.setTime(slot.getStartTime());
        int col = 0;
        if (m_period == VSchedule.Period.WEEK)
            col = s_calendar.get(Calendar.DAY_OF_WEEK) - dayToColAdjust;
        else if (m_period == VSchedule.Period.MONTH)
            col = s_calendar.get(Calendar.DAY_OF_YEAR) - dayToColAdjust;
        int minStart = s_calendar.get(Calendar.MINUTE);
        s_calendar.set(Calendar.MINUTE, 0);
        
        int rowStart = 0;
        int compare = 0;
        do
        {
            if (rowStart  < getRowCount())
            {
                s_compareCalendar.setTime((Date)getValueAt(rowStart, col));
                compare = s_calendar.compareTo(s_compareCalendar);
                rowStart += (compare > 0) ? 1 : 0;
            }
        } while (compare > 0 && rowStart  < getRowCount());

        s_calendar.setTime(slot.getEndTime());
        int minEnd = s_calendar.get(Calendar.MINUTE);
        int rowEnd = rowStart;
        do
        {
            if (rowEnd + 1  < getRowCount())
            {
                s_compareCalendar.setTime((Date)getValueAt(rowEnd + 1, col));
                compare = s_calendar.compareTo(s_compareCalendar);
                rowEnd += (compare > 0) ? 1 : 0;
            }
        } while (compare > 0 && rowEnd + 1  < getRowCount());

        Rectangle rectStart = getCellRect(rowStart, col, false);
        rectStart.y += Math.round((float)minStart/60 * rectStart.height);
        
        Rectangle rectEnd = getCellRect(rowEnd, col, false);
        rectEnd.height -= Math.round((float)minEnd/60 * rectEnd.height);
        
        return rectStart.union(rectEnd);
    }

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
	}	//	dispose
    
    /**
     * @return the m_assignments
     */
    public MAssignmentSlot[] getAssignmentSlots()
    {
        return m_assignments.keySet().toArray(new MAssignmentSlot[m_assignments.size()]);
    }
    
    /**
     * @return true if new assignments can be created
     */
    public boolean isCreateNewsAssignments()
    {
        return m_createNew;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
	protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        for (MAssignmentSlot slot : m_assignments.keySet())
        {
            m_renderer.setForeground(slot.getColor(false));
            m_renderer.setVerticalAlignment(SwingConstants.TOP);

            m_tempBuilder.setLength(0);
            m_tempBuilder.append(slot.getName());
            String desc = slot.getDescription();
            if (desc != null && !"".equals(desc))
                m_tempBuilder.append(" (").append(desc).append(")");
            m_renderer.setText(m_tempBuilder.toString());
            
            Rectangle rect = m_assignments.get(slot);
            if (rect == null)
            {
                // lazily create the rect
                rect = calcTimeSlotRect(slot);
                m_assignments.put(slot, rect);
            }
            Graphics2D g2d = (Graphics2D)g;
            Composite composite = g2d.getComposite();
            g2d.setComposite(s_composite);
            Color color = g.getColor();
            g.setColor(slot.getColor(true));
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
            g2d.setComposite(composite);
            g.setColor(color);
            m_rendererPane.paintComponent(g, m_renderer, this, rect);
        }
    }
    
	/**
	 * @param assignments
	 * @param timeSlots
	 * @param S_Resource_ID
	 * @param startDate
	 * @param endDate
	 */
	public void setAssignmentSlots (MAssignmentSlot[] assignments, 
                                    MAssignmentSlot[] timeSlots, 
                                    int s_Resource_ID, 
                                    Timestamp startDate,
                                    VSchedule.Period period)
	{
        // start date
        m_startDate = startDate;
        m_S_Resource_ID = s_Resource_ID;
        m_period = period;
        
        // populate the assignments without location (lazy creation on painting)
        m_assignments.clear();
        for (MAssignmentSlot slot : assignments)
            m_assignments.put(slot, null);
        
        // collect and sort the time slots
        m_timeSlots = timeSlots.clone();
        Arrays.sort(m_timeSlots);

        // create the data model
        Vector<Vector<Date>> timeSlotList = new Vector<Vector<Date>>();
        for (int i = 0; i < m_timeSlots.length; ++i) {
            Vector<Date> timeslot = new Vector<Date>();
            timeSlotList.add(timeslot);
        }
        Vector<String> columnNames = new Vector<String>();
        
        // collect the data
        Calendar slotTime = Calendar.getInstance(Language.getLoginLanguage().getLocale());
        s_calendar.setTime(m_startDate);
        int days = 1;
        if (m_period == VSchedule.Period.WEEK)
            days = s_calendar.getActualMaximum(Calendar.DAY_OF_WEEK);
        else if (m_period == VSchedule.Period.MONTH)
            days = s_calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < days; i++)
        {
            columnNames.add(m_dateFormat.format(s_calendar.getTime()));
            
            for (int j = 0; j < timeSlotList.size(); j++)
            {
                Vector<Date> timeSlot = timeSlotList.get(j);
                slotTime.setTime(m_timeSlots[j].getStartTime());
                slotTime.set(s_calendar.get(Calendar.YEAR),
                             s_calendar.get(Calendar.MONTH),
                             s_calendar.get(Calendar.DAY_OF_MONTH));
                timeSlot.add(new Timestamp(slotTime.getTimeInMillis()));
            }
            s_calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        
        // set the model
        DefaultTableModel model = new DefaultTableModel(timeSlotList, columnNames);
        setModel(model);
        
        // set the column widths to something readable
        for (Enumeration<TableColumn> e = getColumnModel().getColumns(); e.hasMoreElements();)
        {
            TableColumn tc = e.nextElement();
            tc.setPreferredWidth(tc.getPreferredWidth() * 3);
        }
	}
    
	/**
	 * 	Enable/disable to Create New Assignments
     * 
	 * 	@param createNew if true, allows to create new Assignments
	 */
	public void setCreatesNewAssignments (boolean createNew)
	{
		m_createNew = createNew;
	}	//	setCreateNew
    
	/**
	 * 	Set InfoSchedule for callback
	 * 	@param is InfoSchedule
	 */
	public void setInfoSchedule (InfoSchedule is)
	{
		m_infoSchedule = is;
	}

	/**
     *  Set slots for a day in the schedule.
     *  
     *  @param timeSlots the slots for a day in the schedule
     */
    public void setTimeSlots (MAssignmentSlot[] timeSlots)
    {
        m_timeSlots = timeSlots.clone();
    }   //  setTimePanel
    
    /**
     * If you click on a table cell, can you create a new assignment or edit an
     * existing one?  If so, then the editor dialog will appear.
     *
     */
    private class TimeEditor extends AbstractCellEditor implements TableCellEditor
    {
        
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/** The editor button. */
        private JPanel m_editorComp = new JPanel();
        
        /** The last slot. */
        private MAssignmentSlot m_lastSlot;
        
        /**
         * Creates a TimeEditor.
         */
        public TimeEditor()
        {
            m_editorComp.setOpaque(false);
        }
        
        /* (non-Javadoc)
         * @see javax.swing.AbstractCellEditor#isCellEditable(java.util.EventObject)
         */
        @Override
		public boolean isCellEditable(EventObject eo)
        {
            boolean editable = m_createNew && super.isCellEditable(eo);
            m_lastSlot = null;
            if (eo instanceof MouseEvent)
            {
                MouseEvent me = (MouseEvent)eo;
                for (MAssignmentSlot slot : m_assignments.keySet())
                {
                    if (m_assignments.get(slot).contains(me.getX(), me.getY()))
                    {
                        m_lastSlot = slot;
                        editable = slot.isAssignment();
                        break;
                    }
                }
            }
            
            return editable;
        }
        
        /* (non-Javadoc)
         * @see javax.swing.CellEditor#getCellEditorValue()
         */
        public Object getCellEditorValue()
        {
            return null;
        }
        
        /* (non-Javadoc)
         * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
         */
        public Component getTableCellEditorComponent(JTable table,
                                                     Object value,
                                                     boolean isSelected,
                                                     int row, int column)
        {
            MResourceAssignment ma = null;
            if (m_lastSlot != null)
            {
                ma = m_lastSlot.getMAssignment();
            }
            else
            {
                ma = new MResourceAssignment(Env.getCtx(), 0, null);
                ma.setS_Resource_ID(m_S_Resource_ID);
                Timestamp ts = (Timestamp)getValueAt(row, column);
                ma.setAssignDateFrom(ts);
                ma.setQty(new BigDecimal(1));
            }
            JFrame frame = Env.getFrame(VSchedulePanel.this);
            VAssignmentDialog vad =
                new VAssignmentDialog (frame, ma, false, m_createNew);
            m_infoSchedule.mAssignmentCallback(vad.getMResourceAssignment());
            return m_editorComp;
        }
        
    }
}	//	VSchedulePanel
