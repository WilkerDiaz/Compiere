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
package org.compiere.apps.wf;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;

import org.compiere.wf.*;


/**
 *	Work Flow Line between Nodes.
 *	Coordinates based on WFContentPanel.
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: WFLine.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class WFLine extends Component
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Create Line
	 * 	@param next model
	 */
	public WFLine (MWFNodeNext next)
	{
		m_next = next;
	//	setOpaque(false);
		setFocusable(false);
		//
		m_description = next.getDescription();
		if (m_description != null && m_description.length() > 0)
			m_description = "{" + String.valueOf(next.getSeqNo()) 
				+ ": " + m_description + "}";
	}	//	WFLine
    
    /** The s_tempLine. */
    private Line2D.Double m_line = new Line2D.Double();
    
    /** The s_tempLine. */
    private static final Line2D.Double s_tempLine = new Line2D.Double();
    
    /** The s_polygon. */
    private static final Point2D.Double s_tempPoint = new Point2D.Double();
    
    /** The s_polygon. */
    private static final Polygon s_arrow = new Polygon();
    static 
    {
        s_arrow.addPoint(-8, -5);
        s_arrow.addPoint(0, 0);
        s_arrow.addPoint(-8, 5);
        s_arrow.addPoint(-8, -5);
    }

	/**	Model					*/
	private MWFNodeNext 	   m_next = null;
	/**	From Node				*/
	private RectangularShape   m_from = null;
	/**	To Node					*/
	private RectangularShape   m_to = null;
	/**	Descriprion				*/
	private String 			   m_description = null;
	/** Visited value			*/
	private boolean			   m_visited = false;
	
	/**
	 * 	Get From rectangle
	 * 	@return from node rectangle
	 */
	public Shape getFrom()
	{
		return m_from;
	}	//	getFrom

	/**
	 * 	Get To rectangle
	 * 	@return to node rectangle
	 */
	public Shape getTo()
	{
		return m_to;
	}	//	getTo

	/**
	 * 	Set From/To rectangle.
	 * 	Called from WFLayoutManager.layoutContainer
	 * 	@param from from node rectangle
	 * 	@param to to node rectangle
	 */
	public void setFromTo (RectangularShape from, RectangularShape to)
	{
		m_from = from;
		m_to = to;
	}	//	setFrom

	/**
	 * 	Get From Node ID
	 * 	@return from node id
	 */
	public int getAD_WF_Node_ID()
	{
		return m_next.getAD_WF_Node_ID();	//	Node ->
	}	//	getAD_WF_Node_ID

	/**
	 * 	Get To Node ID
	 * 	@return to node id
	 */
	public int getAD_WF_Next_ID()
	{
		return m_next.getAD_WF_Next_ID();	//	-> Next
	}	//	getAD_WF_Next_ID

	/**
	 * 	Set Visited.
	 *	@param visited visited
	 */
	public void setVisited (boolean visited)
	{
		m_visited = visited;
	}	//	setVisited
	
	
	/**************************************************************************
	 * 	Paint it.
	 *	Coordinates based on WFContentPanel.
	 * 	@param g Graph
	 */
	@Override
	public void paint (Graphics g)
	{
		if (m_from == null || m_to == null || m_from.intersects(m_to.getBounds2D()))
			return;
        Graphics2D g2d = (Graphics2D)g;

        if (!m_next.isUnconditional())
            g2d.setColor(Color.RED);  
        else if (m_visited)
            g2d.setColor(Color.GRAY);
        else
            g2d.setColor(Color.BLACK);
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        m_line = getLine();
        g2d.draw(getLine());

        AffineTransform at = g2d.getTransform();
        double theta = Math.atan2(m_line.y2 - m_line.y1, m_line.x2 - m_line.x1);
        g2d.translate(m_line.x2, m_line.y2);
        g2d.rotate(theta);

		/**
		 *	Paint Arrow:
		 * 	Unconditional: no fill - black text
		 *	Conditional: red fill - red text
		 * 	Visited: green line
		 *	NotVisited: black line
		 *	Split/Join: AND: Magenta Dot -- XOR: -
		 */
        g.setColor(getBackground());
        g.fillPolygon(s_arrow);
        
        g2d.setTransform(at);
		
		//	Paint Dot for AND From
		if (m_next.isFromSplitAnd())
		{
			g.setColor(Color.magenta);
			g.fillOval((int)m_line.x1 - 3, (int)m_line.y1 - 3, 6, 6);
		}
		//	Paint Dot for AND To
		if (m_next.isToJoinAnd())
		{
			g.setColor(Color.magenta);
            g.fillOval((int)m_line.x2 - 3, (int)m_line.y2 - 3, 6, 6);
		}
		
		//	Paint Description in red
		if (m_description != null)
		{
			Graphics2D g2D = (Graphics2D)g;
			Font font = new Font("Dialog", Font.PLAIN, 9);
			if (m_next.isUnconditional())
				g2D.setColor(Color.black);
			else
				g2D.setColor(Color.red);
			TextLayout layout = new TextLayout (m_description, font, g2D.getFontRenderContext());
			
			//	Mid Point
			double x = 0;
			if (m_line.x1 < m_line.x2)
				x = m_line.x1 + ((m_line.x2 - m_line.x1) / 2);
			else
				x = m_line.x2 + ((m_line.x1 - m_line.x2) / 2);
			double y = 0;
			if (m_line.y1 < m_line.y2)
				y = m_line.y1 + ((m_line.y2 - m_line.y1) / 2);
			else
				y = m_line.y2 + ((m_line.y1 - m_line.y2) / 2);

			//	Adjust |
			y -= (layout.getAscent() - 3);		//	above center
			
			//	Adjust -
			x -= (layout.getAdvance() / 2);		//	center
			if (x < 2)
				x = 2;
			
			layout.draw(g2D, (float)x, (float)y);
		}
		
	}	//	paintComponent
    
    private static void getEdge(RectangularShape rect, Line2D line, Line2D result)
    {
        result.setLine(rect.getX(), rect.getY(),
                       rect.getX() + rect.getWidth(), rect.getY());
        if (!result.intersectsLine(line))
        {
            result.setLine(rect.getX(), rect.getY() + rect.getHeight(),
                           rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());
            if (!result.intersectsLine(line))
            {
                result.setLine(rect.getX(), rect.getY(),
                               rect.getX(), rect.getY() + rect.getHeight());
                if (!result.intersectsLine(line))
                {
                    result.setLine(rect.getX() + rect.getWidth(), rect.getY(),
                                   rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());
                }
            }
        }
    }
    
    /**
     * 
     */
    private Line2D.Double getLine()
    {
        if (m_from instanceof Rectangle)
        {
            m_line.setLine(m_from.getCenterX(), m_from.getCenterY(),
                           m_to.getCenterX(), m_to.getCenterY());
    
            s_tempPoint.setLocation(m_line.x2, m_line.y2);
            getEdge(m_from, m_line, s_tempLine);
            if (getLineLineIntersection(m_line, s_tempLine, s_tempPoint))
            {
                m_line.x1 = s_tempPoint.x;
                m_line.y1 = s_tempPoint.y;
            }
        }
        else
        {
            double a = m_from.getWidth() / 2;
            double b = m_from.getHeight() / 2;
            
            double xx = m_to.getCenterX() - m_from.getCenterX();
            double yy = m_to.getCenterY() - m_from.getCenterY();
            m_line.x1 = (a*b*xx)/Math.sqrt(b*b*xx*xx + a*a*yy*yy) + m_from.getCenterX();
            m_line.y1 = (a*b*yy)/Math.sqrt(b*b*xx*xx + a*a*yy*yy) + m_from.getCenterY();
        }
        
        if (m_to instanceof Rectangle)
        {
            s_tempPoint.setLocation(m_line.x2, m_line.y2);
            getEdge(m_to, m_line, s_tempLine);
            if (getLineLineIntersection(m_line, s_tempLine, s_tempPoint))
            {
                m_line.x2 = s_tempPoint.x;
                m_line.y2 = s_tempPoint.y;
            }
        }
        else
        {
            double a = m_to.getWidth() / 2;
            double b = m_to.getHeight() / 2;
            
            double xx = m_from.getCenterX() - m_to.getCenterX();
            double yy = m_from.getCenterY() - m_to.getCenterY();
            m_line.x2 = (a*b*xx)/Math.sqrt(b*b*xx*xx + a*a*yy*yy) + m_to.getCenterX();
            m_line.y2 = (a*b*yy)/Math.sqrt(b*b*xx*xx + a*a*yy*yy) + m_to.getCenterY();
        }
        
        return m_line;
    }
    
    /**
     * @param l1
     * @param l2
     * @param intersection
     * @return
     */
    private static boolean getLineLineIntersection(Line2D.Double l1,
                                                   Line2D.Double l2,
                                                   Point2D.Double intersection)
    {        
        double A1 = l1.y2 - l1.y1;
        double B1 = l1.x1 - l1.x2;
        double C1 =  A1 * l1.x1 + B1 * l1.y1;
        
        double A2 = l2.y2 - l2.y1;
        double B2 = l2.x1 - l2.x2;
        double C2 =  A2 * l2.x1 + B2 * l2.y1;

        double det = A1 * B2 - A2 * B1;
        if(det != 0){
            intersection.x = (B2 * C1 - B1 * C2) / det;
            intersection.y = (A1 * C2 - A2 * C1) / det;
            return true;
        }

        return false;
    }

	/**
	 * 	String Representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("WFLine[");
		sb.append(getAD_WF_Node_ID()).append("->").append(getAD_WF_Next_ID());
		sb.append("]");
		return sb.toString();
	}	//	toString

}	//	WFLine
