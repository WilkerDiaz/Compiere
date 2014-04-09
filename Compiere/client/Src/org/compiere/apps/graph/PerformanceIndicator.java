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
import java.text.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import org.compiere.common.constants.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 * 	Performance Indicator
 *	
 *  @author Jorg Janke
 *  @version $Id: PerformanceIndicator.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class PerformanceIndicator extends JComponent 
	implements MouseListener, ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Constructor
	 *	@param goal goal model
	 */
	public PerformanceIndicator(MGoal goal)
	{
		super();
		m_goal = goal;
		setName(m_goal.getName());
		getPreferredSize();		//	calculate size

        Border border = BorderFactory.createLineBorder(Color.GRAY); 
        Border emptyBorder = BorderFactory.createEmptyBorder(s_gap, s_gap, 0, s_gap); 
        border = BorderFactory.createCompoundBorder(border, emptyBorder); 
        setBorder(border);
        
		setOpaque(false);
        setFont(UIManager.getFont("Label.font").deriveFont(Font.BOLD));
        setFont(getFont().deriveFont(getFont().getSize() + 1));
		updateDisplay();

		//
		mRefresh.addActionListener(this);
		popupMenu.add(mRefresh);
		//
		addMouseListener(this);
	}	//	PerformanceIndicator
    
    /** Integer Number Format       */
    private static final int    s_gap = 3;
    
    /** Integer Number Format       */
    private static final DecimalFormat    s_format =
        DisplayType.getNumberFormat(DisplayTypeConstants.Integer);
    
    private Composite m_composite =
        AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
    
    /** The m_tempArea. */
    private Area m_tempArea = new Area();
    
    /** The rectangle for painting. */
    private Rectangle m_paintRect = new Rectangle();
    
    /** The rectangle for painting. */
    private Rectangle m_tempRect = new Rectangle();
    
    /** The rectangle for painting. */
    private Point m_bottomRight = new Point();
    
    /** The gradientPaint. */
    private GradientPaint m_gradientPaint;
    
    /** The gradientPaint. */
    private GradientPaint m_gradientPaintInner;
    
    /** The gradientPaint. */
    private GradientPaint m_gradientPaintInnerPercent;
    
    /** The gradientPaint. */
    private GradientPaint m_gradientPaintPercentInner;
    
    /** The gradientPaint. */
    private GradientPaint m_innerGradientPaintInnerDome;
    
    /** The gradientPaint. */
    private GradientPaint m_innerGradientDelineator;

    /** The goal. */
    private MGoal               m_goal = null;
    
    private TreeMap<Integer, Color>    m_colors = new TreeMap<Integer, Color>();

    /** The popupMenu. */
    private JPopupMenu          popupMenu = new JPopupMenu();
    
    /** The mRefresh. */
    private CMenuItem           mRefresh =
        new CMenuItem(Msg.getMsg(Env.getCtx(), "Refresh"), Env.getImageIcon("Refresh16.gif"));

	/**
	 * 	Action Listener.
	 * 	Update Display
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (e.getSource() == mRefresh)
		{
			m_goal.updateGoal(true);
			updateDisplay();
			//
			Container parent = getParent();
			if (parent != null)
				parent.invalidate();
			invalidate();
			if (parent != null)
				parent.repaint();
			else
				repaint();
		}
	}	//	actionPerformed

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
     * Calculate where the meter will be painted.
     */
    private void calcPaintRect()
    {
        Insets insets = getInsets();
        getBounds(m_paintRect);
        int origWidth = m_paintRect.width;
        int origHeight = m_paintRect.height;
        m_paintRect.setLocation(0, 0);
        m_paintRect.x += insets.left;
        m_paintRect.width -= insets.left + insets.right;
        if (m_paintRect.width > m_paintRect.height * 2)
        {
            m_paintRect.width = m_paintRect.height * 2;
        }
        m_paintRect.height = m_paintRect.width - insets.bottom;
        m_paintRect.x = origWidth / 2 - m_paintRect.width / 2;
        m_paintRect.y = origHeight - m_paintRect.height / 2 + insets.top;
    }
    
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
        			"pi", event.getWhen(), event.getModifiers());
        	element.actionPerformed(e);
        }
    }	//	fireActionPerformed
    
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
	 * 	Get Goal
	 *	@return goal
	 */
	public MGoal getGoal()
	{
		return m_goal;
	}	//	getGoal

    /**
     * @param rect
     * @return
     */
    private GradientPaint getGradientPaint(Rectangle rect)
    {
        m_bottomRight.setLocation(rect.x + rect.width,
                                  rect.y + rect.height);
        if (m_gradientPaint == null ||
            !m_gradientPaint.getPoint2().equals(m_bottomRight))
            m_gradientPaint =
                new GradientPaint(rect.x,
                                  rect.y,
                                  Color.LIGHT_GRAY,
                                  rect.x + rect.width,
                                  rect.y + rect.height,
                                  Color.DARK_GRAY);
        
        return m_gradientPaint;
    }

    /**
     * @param rect
     * @return
     */
    private GradientPaint getGradientPaintDelineator(Rectangle rect)
    {
        m_bottomRight.setLocation(rect.x + rect.width,
                                  rect.y + rect.height);
        if (m_innerGradientDelineator == null ||
            !m_innerGradientDelineator.getPoint2().equals(m_bottomRight))
            m_innerGradientDelineator =
                new GradientPaint(rect.x,
                                  rect.y,
                                  Color.DARK_GRAY,
                                  rect.x + rect.width,
                                  rect.y + rect.height,
                                  Color.GRAY);
        
        return m_innerGradientDelineator;
    }

    /**
     * @param rect
     * @return
     */
    private GradientPaint getGradientPaintInner(Rectangle rect)
    {
        m_bottomRight.setLocation(rect.x + rect.width,
                                  rect.y + rect.height);
        if (m_gradientPaintInner == null ||
            !m_gradientPaintInner.getPoint2().equals(m_bottomRight))
            m_gradientPaintInner =
                new GradientPaint(rect.x,
                                  rect.y,
                                  Color.DARK_GRAY,
                                  rect.x + rect.width,
                                  rect.y + rect.height,
                                  Color.WHITE);
        
        return m_gradientPaintInner;
    }

    /**
     * @param rect
     * @return
     */
    private GradientPaint getGradientPaintInnerDome(Rectangle rect)
    {
        m_bottomRight.setLocation(rect.x + rect.width,
                                  rect.y + rect.height);
        if (m_innerGradientPaintInnerDome == null ||
            !m_innerGradientPaintInnerDome.getPoint2().equals(m_bottomRight))
            m_innerGradientPaintInnerDome =
                new GradientPaint(rect.x,
                                  rect.y,
                                  Color.DARK_GRAY,
                                  rect.x + rect.width,
                                  rect.y + rect.height,
                                  Color.LIGHT_GRAY);
        
        return m_innerGradientPaintInnerDome;
    }

    /**
     * @param rect
     * @return
     */
    private GradientPaint getGradientPaintPercent(Rectangle rect)
    {
        m_bottomRight.setLocation(rect.x + rect.width,
                                  rect.y + rect.height);
        if (m_gradientPaintInnerPercent == null ||
            !m_gradientPaintInnerPercent.getPoint2().equals(m_bottomRight))
            m_gradientPaintInnerPercent =
                new GradientPaint(rect.x,
                                  rect.y,
                                  Color.DARK_GRAY,
                                  rect.x + rect.width,
                                  rect.y + rect.height,
                                  Color.LIGHT_GRAY);
        
        return m_gradientPaintInnerPercent;
    }

    /**
     * @param rect
     * @return
     */
    private GradientPaint getGradientPaintPercentInner(Rectangle rect)
    {
        m_bottomRight.setLocation(rect.x + rect.width,
                                  rect.y + rect.height);
        if (m_gradientPaintPercentInner == null ||
            !m_gradientPaintPercentInner.getPoint2().equals(m_bottomRight))
            m_gradientPaintPercentInner =
                new GradientPaint(rect.x,
                                  rect.y,
                                  Color.GRAY,
                                  rect.x + rect.width,
                                  rect.y + rect.height,
                                  Color.WHITE);
        
        return m_gradientPaintPercentInner;
    }
	
    
    /**************************************************************************
     * 	Mouse Clicked
     *	@param e mouse event
     */
	public void mouseClicked (MouseEvent e)
	{
		if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() > 1)
			fireActionPerformed(e);
		if (SwingUtilities.isRightMouseButton(e))
			popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
	}	//	mouseClicked

	public void mouseEntered (MouseEvent e)
	{
	}

	public void mouseExited (MouseEvent e)
	{
	}

	public void mousePressed (MouseEvent e)
	{
	}

	public void mouseReleased (MouseEvent e)
	{
	}
    
    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
	protected void paintComponent(Graphics g)
    {
        calcPaintRect();
        
        // collect the default Paint, Composite, clip, convert to Graphics2D, and
        // turn on antialiasing
        Graphics2D g2d = (Graphics2D)g;
        Paint paint = g2d.getPaint();
        Composite composite = g2d.getComposite();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                             RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                             RenderingHints.VALUE_STROKE_PURE);

        // paint the outside arc with a gradient
        g2d.setPaint(getGradientPaint(m_paintRect));
        g2d.fillArc(m_paintRect.x,
                    m_paintRect.y,
                    m_paintRect.width, m_paintRect.height + s_gap * 2,
                    0, 180);

        // paint the inner arc representing where the meter will reside
        m_paintRect.grow(-m_paintRect.width / 8 + s_gap,
                         -m_paintRect.height / 12 + s_gap);
        m_paintRect.height -= m_paintRect.height / 4;
        g2d.setPaint(getGradientPaintInner(m_paintRect));
        g2d.fillArc(m_paintRect.x,
                    m_paintRect.y,
                    m_paintRect.width,
                    m_paintRect.height,
                    0, 180);

        // accent by delineatation shelf of the inner arc
        m_tempRect.setBounds(m_paintRect.x,
                             m_paintRect.y + m_paintRect.height / 2, 
                             m_paintRect.width, 
                             2);
        g2d.setPaint(getGradientPaintDelineator(m_tempRect));
        g2d.fill(m_tempRect);

        // track the paint rect form here for creating shelf later
        m_tempRect.setBounds(m_paintRect);

        // paint the colors over an inner arc representing the state of the value of the meter
        m_paintRect.grow(-m_paintRect.height / 50, -m_paintRect.height / 50);
        int last = 0;
        for (Integer i : m_colors.keySet())
        {
            g2d.setColor(m_colors.get(i));
            g2d.fillArc(m_paintRect.x,
                        m_paintRect.y,
                        m_paintRect.width,
                        m_paintRect.height - s_gap,
                        180 * last/100, 180 - 180 * i/100);
            i = last;
        }

        // paint the inner arc for depth for the meter
        m_paintRect.grow(-m_paintRect.height / 25, -m_paintRect.height / 25);
        g2d.setPaint(getGradientPaintInnerDome(m_paintRect));
        g2d.fillArc(m_paintRect.x,
                    m_paintRect.y,
                    m_paintRect.width,
                    m_paintRect.height,
                    0, 180);

        // paint the analog meter if there is a percent
        int percent = (m_goal.getPercent() > 100) ? 100 : m_goal.getPercent();
        if (percent > 0)
        {
            g2d.setComposite(m_composite);
            g2d.setPaint(getGradientPaintPercentInner(m_tempRect));
            g2d.setColor(m_goal.getColor());
            g2d.fillArc(m_paintRect.x,
                        m_paintRect.y,
                        m_paintRect.width,
                        m_paintRect.height,
                        180, - 180 * percent / 100 );
        }
        
        // paint a rectangle at the bottom of inner arc to give further hint of depth
        m_tempRect.y  += m_tempRect.height / 2 - m_tempRect.height / 35;
        m_tempRect.height /= 35;
        g2d.setPaint(getGradientPaintInner(m_tempRect));
        Point p = m_tempRect.getLocation();
        m_tempRect.setLocation(0, 0);
        m_tempArea.reset();
        
        // calcs left side of sheared rectangle
        m_tempRect.width /= 2;
        AffineTransform at = AffineTransform.getShearInstance(-0.5, 0);
        Shape shape = at.createTransformedShape(m_tempRect);
        at.setToTranslation(p.x + m_tempRect.height / 2, p.y + 1);
        shape = at.createTransformedShape(shape);
        m_tempArea.add(new Area(shape));

        // calcs right side of sheared rectangle
        at.setToShear(0.5, 0);
        shape = at.createTransformedShape(m_tempRect);
        at.setToTranslation(p.x + m_tempRect.width - m_tempRect.height / 2, p.y + 1);
        shape = at.createTransformedShape(shape);
        m_tempArea.add(new Area(shape));
        g2d.fill(m_tempArea);
        
        // collect information about the font for the meter reading
        Font font = getFont().deriveFont((float)m_paintRect.height/4);
        font = font.deriveFont(Font.PLAIN);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics(font);
        String str = m_goal.getPercent() + "%";
        str = m_goal.getPercent() < 10 ? " " + str : str;
        int width = SwingUtilities.computeStringWidth(fm, str);

        // paint a lower beveled border around the meter indicator
        m_tempRect.setBounds(m_paintRect.x + m_paintRect.width / 2 - width / 2,
                             m_paintRect.y + m_paintRect.height / 4 - fm.getAscent() / 2,
                             width,
                             fm.getHeight());
        g2d.setPaint(getGradientPaintPercent(m_tempRect));
        g2d.fillRoundRect(m_tempRect.x - s_gap,
                          m_tempRect.y - s_gap, 
                          m_tempRect.width + s_gap * 2, 
                          m_tempRect.height + s_gap * 2,
                          10, 10);

        // fill meter indicator with color indicator, slightly transparent for
        // gradient effect
        g2d.setComposite(m_composite);
        g2d.setPaint(getGradientPaintPercentInner(m_tempRect));
        g2d.setColor(m_goal.getColor());
        g2d.fillRoundRect(m_tempRect.x,
                          m_tempRect.y, 
                          m_tempRect.width, 
                          m_tempRect.height,
                          10, 10);
        g2d.setPaint(paint);

        // paint the meter text
        g2d.setComposite(composite);
        g2d.setColor(Color.BLACK);
        g2d.drawString(str, m_tempRect.x, m_tempRect.y + m_tempRect.height - fm.getDescent());
        g2d.setFont(getFont());

        // calcs for the name plate
        font = getFont().deriveFont((float)m_paintRect.height / 10);
        g2d.setFont(font);
        fm = g2d.getFontMetrics(font);
        str = m_goal.getName();
        m_tempRect.setFrame(fm.getStringBounds(str, g2d));
        m_tempRect.grow(s_gap * 4, 1);
        m_tempRect.setLocation(getWidth() / 2 - m_tempRect.width / 2,
                               getHeight() - m_tempRect.height - m_tempRect.height / 6);
        
        // paint the name plate
        g2d.setPaint(paint);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawString(str, m_tempRect.x + s_gap * 4,
                       m_tempRect.y + m_tempRect.height - fm.getDescent());
        g2d.setColor(Color.BLACK);
        g2d.drawString(str, m_tempRect.x + s_gap * 4 + 1,
                       m_tempRect.y + m_tempRect.height - fm.getDescent()+ 2);
        g2d.setFont(getFont());
    }

	/**
     * Removes an <code>ActionListener</code> from the indicator.
     * @param l the listener to be removed
     */
    public void removeActionListener(ActionListener l) 
    {
		listenerList.remove(ActionListener.class, l);
    }	//	removeActionListener
    
    /**
	 * 	Update Display Data
	 */
	protected void updateDisplay()
	{        
        // collect the colors
        m_colors.clear();
        MColorSchema mcs = m_goal.getColorSchema();
        if (mcs != null)
        {
            Color lastColor = null;
            for (int i = 0; i < 100; i++)
            {
                Color color = mcs.getColor(i);
                if (!color.equals(lastColor))
                    m_colors.put(i, color);
                lastColor = color;
            }
        }
        
		//	ToolTip
        StringBuilder text = new StringBuilder();
		if (m_goal.getDescription() != null)
			text.append(m_goal.getDescription()).append(": ");
		text.append(s_format.format(m_goal.getMeasureActual()));
		if (m_goal.isTarget())
			text.append(" ").append(Msg.getMsg(Env.getCtx(), "of")).append(" ")
				.append(s_format.format(m_goal.getMeasureTarget()));
		setToolTipText(text.toString());
		//
		setBackground(m_goal.getColor());
		setForeground(GraphUtil.getForeground(getBackground()));
        
		invalidate();
	}	//	updateData
}	//	PerformanceIndicator
