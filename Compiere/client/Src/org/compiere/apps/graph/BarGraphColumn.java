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
package org.compiere.apps.graph;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.math.*;
import java.sql.*;
import java.text.*;

import javax.swing.*;
import javax.swing.event.*;

import org.compiere.common.constants.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Bar Graph Column
 *	
 *  @author Jorg Janke
 *  @version $Id: BarGraphColumn.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class BarGraphColumn extends JComponent implements MouseListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Base Constructor
	 *	@param label label
	 *	@param value value
	 */
	public BarGraphColumn (String label, double value)
	{
		m_label = label;
		setValue(value);
		addMouseListener(this);
	}	//	BarGraphColumn
	
	/**
	 * 	Single Achievement Constructor
	 *	@param achievement achievement
	 */
	public BarGraphColumn (MAchievement achievement)
	{
		this (achievement.getName(), achievement.getManualActual().doubleValue());
		m_achievement = achievement;
	}	//	BarGraphColumn

	/**
	 * 	Achievement Goal Constructor
	 *	@param goal goal
	 *	@param data count
	 */
	public BarGraphColumn (MGoal goal, BigDecimal data)
	{
		this ("", data == null ? 0 : data.doubleValue());
		m_goal = goal;
	}	//	BarGraphColumn

	/**
	 * 	Measure Calc Constructor
	 *	@param mc MeasureCalc
	 */
	public BarGraphColumn (MMeasureCalc mc, BigDecimal data)
	{
		this ("", data == null ? 0 : data.doubleValue());
		m_mc = mc;
	}	//	BarGraphColumn

	/**
	 * 	Request Type Constructor
	 *	@param rt Request Type
	 */
	public BarGraphColumn (MRequestType rt, BigDecimal data, int id)
	{
		this ("", data == null ? 0 : data.doubleValue());
		m_rt = rt;
		m_id = id;
	}	//	BarGraphColumn

	/**
	 * 	Project Type Constructor
	 *	@param pt Procet Type
	 */
	public BarGraphColumn (MProjectType pt, BigDecimal data, int id)
	{
		this ("", data == null ? 0 : data.doubleValue());
		m_pt = pt;
		m_id = id;
	}	//	BarGraphColumn

	/** Optional Achievement		*/
	private MAchievement	m_achievement = null;
	/** Measure Calc				*/
	private MMeasureCalc	m_mc = null;
	/** Goal				*/
	private MGoal			m_goal = null;
	
	private MRequestType	m_rt = null;
	private MProjectType	m_pt = null;
	private int				m_id = 0;
	
	/** Display						*/
	private String			m_measureDisplay = null;
	private Timestamp		m_date = null;
	
	/** Column Label				*/
	private String	m_label = null;
	/** Column Data Value			*/
	private double	m_value = 0;
	/** Column Label Value			*/
	private String	m_labelValue = "";
	/** Column Data Target Value	*/
	private double	m_targetValue = 0;
	/** Column Width in pixles		*/
	private double	m_width = 0;
	/** Column Height in pixles		*/
	private double	m_height = 0;
	
	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger (BarGraphColumn.class);
	/** Integer Number Format		*/
	private static DecimalFormat	s_format = DisplayType.getNumberFormat(DisplayTypeConstants.Integer);
    
    private static final AffineTransform s_affineTransform = new AffineTransform(); 
	
	/**
	 * 	Get Achievement Goal
	 *	@return achievement or null
	 */
	public MGoal getGoal()
	{
		return m_goal;
	}	//	getGoal

	
	/**
	 * 	Get Single Achievement
	 *	@return achievement or null
	 */
	public MAchievement getAchievement()
	{
		return m_achievement;
	}	//	getAchievement
	
	/**
	 * 	Get MeasureCalc
	 *	@return measure
	 */
	public MMeasureCalc getMeasureCalc()
	{
		return m_mc;
	}	//	getMeasureCalc
	
	public MRequestType getRequestType()
	{
		return m_rt;
	}
	
	public MProjectType getProjectType()
	{
		return m_pt;
	}
	
	public String getMeasureDisplay()
	{
		return m_measureDisplay;
	}	//	getMeasureDisplay
	
	public Timestamp getDate()
	{
		return m_date;
	}	//	getDate
	
	public int getID()
	{
		return m_id;
	}
	
	/**
	 * 	Set Background and matching Foreground
	 *	@param bg background
	 */
	@Override
	public void setBackground (Color bg)
	{
		super.setBackground (bg);
		setForeground(GraphUtil.getForeground(bg));
	}	//	setBackground
	
	/**
	 * @return Returns the label.
	 */
	public String getLabel ()
	{
		return m_label;
	}	//	getLabel
	
	/**
	 * @param label The label to set.
	 */
	public void setLabel (String label)
	{
		m_label = label;
		if (m_label != null)
			m_labelValue = s_format.format(m_value) + " - " + m_label;
		else
			m_labelValue = s_format.format(m_value);
		setToolTipText(m_labelValue);
		setName(m_labelValue);
	}	//	setLabel

	/**
	 *	@param date for label.
	 * 	@param MeasureDisplay measure display
	 */
	public void setLabel (Timestamp date, String MeasureDisplay)
	{
		if (date == null)
			return;
		m_date = date;
		m_measureDisplay = MeasureDisplay;
		//
		SimpleDateFormat format = DisplayType.getDateFormat(DisplayTypeConstants.Date);
		String text = format.format(date);
		//	Month only
		if (X_PA_Goal.MEASUREDISPLAY_Month.equals(MeasureDisplay)
			|| X_PA_Goal.MEASUREDISPLAY_Quarter.equals(MeasureDisplay))
		{
			String pattern = format.toPattern();
			String mmText = text;
			int index = pattern.indexOf("dd");
			if (index == 0)			//	dd.MM.yyyy
				mmText = text.substring(3);
			else if (index > 0)		//	MM/dd/yyyy
			{
				mmText = text.substring(0, index-1);
				if (text.length() > index+2)
					mmText += text.substring(index+2);
			}
			setLabel(mmText);
		}
		else	//	Day
			setLabel(text);
	}	//	setLabel

	/**
	 * @return Returns the targetValue.
	 */
	public double getTargetValue ()
	{
		return m_targetValue;
	}	//	getTargetValue
	
	/**
	 * @param targetValue The targetValue to set.
	 */
	public void setTargetValue (double targetValue)
	{
		m_targetValue = targetValue;
	}	//	setTargetValue
	
	/**
	 * @return Returns the data value.
	 */
	public double getValue ()
	{
		return m_value;
	}	//	getValue
	
	/**
	 * @param value The data value to set.
	 */
	public void setValue (double value)
	{
		m_value = value;
		if (m_label != null)
			m_labelValue = s_format.format(m_value) + " - " + m_label;
		else
			m_labelValue = s_format.format(m_value);
		setToolTipText(m_labelValue);
		setName(m_labelValue);
	}	//	setValue
	
	/**
	 * @return Returns the column width in pixles.
	 */
	public double getColWidth ()
	{
		return m_width;
	}	//	getColWidth
	
	/**
	 * @param width The column width in pixles.
	 */
	public void setColWidth (double width)
	{
		m_width = width;
		if (isPreferredSizeSet())
			setPreferredSize(null);
	}	//	getColWidth

	/**
	 * @return Returns the height in pixles.
	 */
	public double getColHeight()
	{
		return m_height;
	}	//	getHeight
	
	/**
	 * @param height The hight in pixles.
	 */
	public void setColHeight (double height)
	{
		m_height = height;
		if (isPreferredSizeSet())
			setPreferredSize(null);
	}	//	setHeight
	
	/**
	 * 	Get Maximum Size
	 *	@return size
	 */
	@Override
	public Dimension getMaximumSize ()
	{
		return getPreferredSize();
	}	//	getMaximumSize

	/**
	 * 	Get Minimum Size
	 *	@return size
	 */
	@Override
	public Dimension getMinimumSize ()
	{
		return getPreferredSize();
	}	//	getMinimumSize

	/**
	 * 	Get Preferred Size
	 *	@return size
	 */
	@Override
	public Dimension getPreferredSize ()
	{
		if (!isPreferredSizeSet()) 
		{
			Dimension size = new Dimension((int)m_width, (int)m_height);
			setPreferredSize(size);
		}
		return super.getPreferredSize ();
	}	//	getPreferredSize
	
	
	/**
	 * 	Paint Component
	 *	@param g graphics
	 */
	@Override
	protected void paintComponent (Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
		Rectangle bounds = getBounds();
        bounds.setLocation(0, 0);
        bounds.width /= 4;
        
        // draw boxes for 3d effect
        g2d.setColor(getBackground());
        g2d.fill3DRect(bounds.x, bounds.y, bounds.width * 3, bounds.height, false);

        g2d.setColor(getBackground().darker());
        s_affineTransform.setToShear(0, 0.9);
        Shape shape = s_affineTransform.createTransformedShape(bounds);
        s_affineTransform.setToTranslation(bounds.width * 3, 0);
        shape = s_affineTransform.createTransformedShape(shape);
        g2d.fill(shape);
		
		//	Paint Label & Value
		Color color = getForeground();
		g2d.setPaint(color);
		//
		Font font = getFont();
		FontMetrics fm = g2d.getFontMetrics(font);
		int fontHeight = fm.getHeight();
		s_affineTransform.setToRotation(Math.PI*3/2);
		font = font.deriveFont(s_affineTransform);
		g2d.setFont(font);
		//
		int x = bounds.width + (fontHeight - 2)/2;
		if (x < fontHeight)
			x = fontHeight - 2;
		int y = bounds.height -3 ;
		g2d.drawString(m_labelValue, x, y);
		log.finest("x=" + x + ",fontHeight=" + fontHeight +  ", y=" + y + " - " + m_labelValue);
	}	//	paintComponent
	
	
    /**************************************************************************
     * Adds an <code>ActionListener</code> to the indicator.
     * @param l the <code>ActionListener</code> to be added
     */
    public void addActionListener(ActionListener l) 
    {
    	if (l != null)
    		listenerList.add(ActionListener.class, l);
    }	//	addActionListener
    
    /**
     * Removes an <code>ActionListener</code> from the indicator.
     * @param l the listener to be removed
     */
    public void removeActionListener(ActionListener l) 
    {
    	if (l != null)
    		listenerList.remove(ActionListener.class, l);
    }	//	removeActionListener
    
    /**
     * Returns an array of all the <code>ActionListener</code>s added
     * to this indicator with addActionListener().
     *
     * @return all of the <code>ActionListener</code>s added or an empty
     *         array if no listeners have been added
     */
    public ActionListener[] getActionListeners() 
    {
        return (listenerList.getListeners(ActionListener.class));
    }	//	getActionListeners

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the <code>event</code> 
     * parameter.
     *
     * @param event  the <code>ActionEvent</code> object
     * @see EventListenerList
     */
    protected void fireActionPerformed(MouseEvent event) 
    {
        // Guaranteed to return a non-null array
    	ActionListener[] listeners = getActionListeners();
        ActionEvent e = null;
        // Process the listeners first to last
        for (ActionListener element : listeners) {
        	//	Lazily create the event:
        	if (e == null) 
        		e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
        			"column"+m_label, event.getWhen(), event.getModifiers());
        	element.actionPerformed(e);
        }
    }	//	fireActionPerformed
	
    
    /**
     * 	Mouse Clicked
     *	@param e mouse event
     */
	public void mouseClicked (MouseEvent e)
	{
		if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() > 1)
			fireActionPerformed(e);
	}	//	mouseClicked

	public void mousePressed (MouseEvent e)
	{
	}

	public void mouseReleased (MouseEvent e)
	{
	}

	public void mouseEntered (MouseEvent e)
	{
	}

	public void mouseExited (MouseEvent e)
	{
	}

}	//	BarGraphColumn
