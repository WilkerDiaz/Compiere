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
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;

import org.compiere.swing.*;
import org.compiere.util.*;
import org.compiere.wf.*;

/**
 *	Graphical Work Flow Node.
 *  Listen to PropertyChange for selection
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: WFNode.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class WFNode extends CPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Create WF Node
	 * 	@param node model
	 */
	public WFNode (MWFNode node)
	{
		super(new BorderLayout());
        add(m_label, BorderLayout.WEST);
        add(m_textArea);
        
		m_node = node;
        setOpaque(!isEditable());
        
        m_textArea.enableInputMethods(isEditable());
        m_textArea.setEditable(isEditable());
        m_textArea.setText(m_node.getName(true));
        Font font = getFont();
        font = isEditable() ? font.deriveFont(Font.ITALIC | Font.BOLD) : font;
        m_textArea.setFont(font);
        
        m_label.setOpaque(false);
        if (!isEditable())
            m_label.setVerticalAlignment(SwingConstants.TOP);
        m_label.setIcon(new WFIcon(node.getAction()));
        
		setBorder(s_border);
		
		//	Tool Tip
		String description = node.getDescription(true);
		if (description != null && description.length() > 0)
			setToolTipText(description);
		else
			setToolTipText(node.getName(true));
		
		//	Location
        Dimension size = getPreferredSize();
		setBounds(node.getXPosition(), node.getYPosition(), size.width, size.height);
		log.config(node.getAD_WF_Node_ID() 
			+ "," + node.getName() + " - " + getLocation());
		setSelected(false);
		setVisited(false);
	}	//	WFNode

	/**	Selected Property value			*/
	public static final String	PROPERTY_SELECTED = "selected";
	/**	Standard (raised) Border		*/
	private static final EtchedBorder 	s_border =
        (EtchedBorder)BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
    /** Selected (lowered) Border       */
    private static final Border     s_borderSelected =
        BorderFactory.createLoweredBevelBorder();
    /** Selected (lowered) Border       */
    private static final Border     s_borderEditableSelected =
        new EditableSelectedBorder();
    /** Selected (lowered) Border       */
    private static final Border s_borderEditable = new EditableBorder();
    
    /** Size of the Node                */
    private static final Dimension  s_size = new Dimension (120, 50);
    
    /** Size of the Node                */
    private static final Dimension  s_editableSize = new Dimension (130, 60);
    /** The s_ellipse. */
    private static final Ellipse2D.Float s_ellipse =
        new Ellipse2D.Float(-1, -1, s_editableSize.width + 2, s_editableSize.height + 2);
    
	/**	Logger			*/
	private static final CLogger log = CLogger.getCLogger(WFNode.class);
	
	/**	ID						*/
	private MWFNode 		m_node = null;
	/**	Selected Value			*/
	private boolean			m_selected = false;
	/**	Visited Value			*/
	private boolean			m_visited = false;
    
    /** The m_label. */
    private JLabel          m_label = new JLabel();
    
    /** The m_defaultColor. */
    private Color m_defaultColor = getBackground();
    
    /** The m_textArea. */
    private JTextArea       m_textArea = new NodeTextArea();
	
	/**************************************************************************
	 * 	Set Selected.
	 * 	Selected: blue foreground - lowered border
	 * 	UnSelected: black foreground - raised border
	 * 	@param selected selected
	 */
	public void setSelected (boolean selected)
	{
		firePropertyChange(PROPERTY_SELECTED, m_selected, selected);
		m_selected = selected;
		if (m_selected)
		{
            setBorder (isEditable() ? s_borderEditableSelected : s_borderSelected);
		}
        else
        {
            setBorder (isEditable() ? s_borderEditable : s_border);
        }
	}	//	setSelected
	
	/**
	 * 	Set Visited.
	 * 	Visited: green background
	 * 	NotVisited: 
	 *	@param visited visited
	 */
	public void setVisited (boolean visited)
	{
		m_visited = visited;
		if (m_visited)
		{
			setBackground(Color.GREEN);
		}
		else
		{
			setBackground(m_defaultColor);
		}
	}	//	setVisited

	/**
	 * 	Get Selected
	 * 	@return selected
	 */
	public boolean isSelected()
	{
		return m_selected;
	}	//	isSelected

	/**
	 * 	Get Client ID
	 * 	@return Client ID
	 */
	public int getAD_Client_ID()
	{
		return (m_node != null) ? m_node.getAD_Client_ID() : Integer.MIN_VALUE;
	}	//	getAD_Client_ID

	/**
	 * 	Is the node Editable
	 *	@return yes if the Client is the same
	 */
	public boolean isEditable()
	{
		return getAD_Client_ID() == Env.getCtx().getAD_Client_ID();
	}	//	isEditable
	
	/**
	 * 	Get Node ID
	 * 	@return Node ID
	 */
	public int getAD_WF_Node_ID()
	{
		return m_node.getAD_WF_Node_ID();
	}	//	getAD_WF_Node_ID

	/**
	 * 	Get Node Model
	 * 	@return Node Model
	 */
	public MWFNode getModel()
	{
		return m_node;
	}	//	getModel
    
    /**
     * @return
     */
    public RectangularShape getRectangularShape()
    {
        Rectangle rect = getBounds();
        if (isEditable())
            return new Ellipse2D.Double(rect.x, rect.y, rect.width, rect.height);
        else
            return rect;
    }

	/**
	 * 	Set Location - also for Node.
	 *	@param x x
	 *	@param y y
	 */
	@Override
	public void setLocation (int x, int y)
	{
		super.setLocation (x, y);
		m_node.setPosition(x, y);
	}	//	setLocation
	
	/**
	 * 	String Representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("WFNode[");
		sb.append(getAD_WF_Node_ID()).append("-").append(m_textArea.getText())
			.append(",").append(getBounds())
			.append("]");
		return sb.toString();
	}	//	toString
	
	/**************************************************************************
	 * 	Get Preferred Size
	 *	@return size
	 */
	@Override
	public Dimension getPreferredSize ()
	{
		return (Dimension)(isEditable() ? s_editableSize.clone() : s_size.clone());
	}	//	getPreferredSize
    
    /* (non-Javadoc)
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     */
    @Override
	public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                         RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                         RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        
        if (isEditable())
        {
            Color oldColor = g2d.getColor();
            g2d.setColor(getBackground());
            g2d.fill(s_ellipse);
            g2d.setColor(oldColor);
        }
                
        super.paint(g2d);
    }
    
    /**
     *
     */
    private static class EditableBorder extends EtchedBorder
    {
        
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/** The s_insets. */
        private static final Insets s_insets = new Insets(5, 10, 5, 10);

        /* (non-Javadoc)
         * @see javax.swing.border.Border#getBorderInsets(java.awt.Component)
         */
        @Override
		public Insets getBorderInsets(Component c)
        {
            return s_insets;
        }

        /* (non-Javadoc)
         * @see javax.swing.border.Border#isBorderOpaque()
         */
        @Override
		public boolean isBorderOpaque()
        {
            return true;
        }

        /* (non-Javadoc)
         * @see javax.swing.border.Border#paintBorder(java.awt.Component, java.awt.Graphics, int, int, int, int)
         */
        @Override
		public void paintBorder(Component comp, Graphics g, int x, int y, int width, int height)
        {
            Color oldColor = g.getColor();
            
            g.setColor(getShadowColor(comp));
            g.drawArc(2, 2, s_editableSize.width - 3, s_editableSize.height - 3, 45, 360);
            
            g.setColor(getHighlightColor(comp));
            g.drawArc(1, 1, s_editableSize.width - 3, s_editableSize.height - 3, 45, 360);
            
            g.setColor(oldColor);
        }
    }
    
    /**
     *
     */
    private static class EditableSelectedBorder extends BevelBorder
    {
        
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
         * Creates a WFNode.EditableSelectedBorder.
         */
        public EditableSelectedBorder()
        {
            super(BevelBorder.LOWERED);
        }
        
        /** The s_insets. */
        private static final Insets s_insets = new Insets(5, 10, 5, 10);

        /* (non-Javadoc)
         * @see javax.swing.border.Border#getBorderInsets(java.awt.Component)
         */
        @Override
		public Insets getBorderInsets(Component c)
        {
            return s_insets;
        }

        /* (non-Javadoc)
         * @see javax.swing.border.Border#isBorderOpaque()
         */
        @Override
		public boolean isBorderOpaque()
        {
            return true;
        }

        /* (non-Javadoc)
         * @see javax.swing.border.Border#paintBorder(java.awt.Component, java.awt.Graphics, int, int, int, int)
         */
        @Override
		public void paintBorder(Component comp, Graphics g, int x, int y, int width, int height)
        {
            Color oldColor = g.getColor();
            
            double calc = Math.atan2(s_editableSize.height, s_editableSize.width);
            int angle = (int)Math.round(Math.toDegrees(calc));
            g.setColor(getShadowInnerColor(comp));
            g.drawArc(0, 0, s_editableSize.width - 1, s_editableSize.height - 1, angle, 180);
            
            g.setColor(getShadowOuterColor(comp));
            g.drawArc(1, 1, s_editableSize.width - 2, s_editableSize.height - 2, angle, 180);
            
            int angleOpp = angle + 180;
            g.setColor(getHighlightOuterColor(comp));
            g.drawArc(0, 0, s_editableSize.width - 1, s_editableSize.height - 1, angleOpp, 180);
            
            g.setColor(getHighlightInnerColor(comp));
            g.drawArc(1, 1, s_editableSize.width - 2, s_editableSize.height - 2, angleOpp, 180);

            g.setColor(oldColor);
            
        }
    }
    
    /**
     *
     */
    private class NodeTextArea extends JTextArea
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
         * Creates a NodeTextArea.
         */
        public NodeTextArea()
        {
            enableEvents(AWTEvent.MOUSE_EVENT_MASK);
            setCursor(null);

            setOpaque(false);
            setLineWrap(true);
            setWrapStyleWord(true);
            setSelectionColor(Color.LIGHT_GRAY);
            
            AbstractDocument document = (AbstractDocument)getDocument();
            document.setDocumentFilter(new DocumentFilter()
            {
                @Override
				public void insertString(FilterBypass fb,
                                         int offset,
                                         String string,
                                         AttributeSet attr) throws BadLocationException
                {
                    if (string.indexOf('\r') < 0 && string.indexOf('\n') < 0)
                        super.insertString(fb, offset, string, attr);
                    else
                    {
                        m_node.setName(getText());
                        m_node.save();
                    }
                }

                @Override
				public void replace(FilterBypass fb,
                                    int offset,
                                    int length,
                                    String text,
                                    AttributeSet attrs) throws BadLocationException
                {
                    if (text.indexOf('\r') < 0 && text.indexOf('\n') < 0)
                        super.replace(fb, offset, length, text, attrs);
                    else
                    {
                        m_node.setName(getText());
                        m_node.save();
                    }
                }
            });
            
            addFocusListener(new FocusAdapter()
            {
                @Override
				public void focusLost(FocusEvent e)
                {
                    m_node.setName(getText());
                    m_node.save();
                }
            });
        }
        
        /* (non-Javadoc)
         * @see javax.swing.JComponent#processMouseEvent(java.awt.event.MouseEvent)
         */
        @Override
		protected void processMouseEvent(MouseEvent me)
        {
            MouseEvent newMe = SwingUtilities.convertMouseEvent(this, me, WFNode.this);
            WFNode.this.processMouseEvent(newMe);
            if (isEditable())
                super.processMouseEvent(me);
        }
        
        /* (non-Javadoc)
         * @see javax.swing.JComponent#processMouseMotionEvent(java.awt.event.MouseEvent)
         */
        @Override
		protected void processMouseMotionEvent(MouseEvent me)
        {
            me = SwingUtilities.convertMouseEvent(this, me, WFNode.this);
            WFNode.this.processMouseMotionEvent(me);
        }
    }
}	//	WFNode
