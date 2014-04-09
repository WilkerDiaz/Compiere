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
package org.compiere.plaf;

import java.awt.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;

/**
 * 	Compiere L&F Borders
 *	
 *  @author Jorg Janke
 *  @version $Id: CompiereBorders.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CompiereBorders
{
	/** Focus Color				*/
	public static final Color fc = Color.red;

	/**
	 *	Compiere Margin Border
	 */
	public static class MarginBorder extends BasicBorders.MarginBorder
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 	Paint Border
		 *	@see javax.swing.border.AbstractBorder#paintBorder(java.awt.Component, java.awt.Graphics, int, int, int, int)
		 */
		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
		{
			if (c.hasFocus())
			{
				g.translate(x, y);
				g.setColor(fc);
				g.drawLine(0, 0, w, 0);
				g.drawLine(0, h, w, h);
				g.translate(-x, -y);
			}
		}	//	paintBorder
	}	//	MarginBorder
		
	
    /**
     * Returns a border instance for a JTextField
     */
    public static Border getTextFieldBorder()
	{
		return new BorderUIResource.CompoundBorderUIResource(
			    new MetalBorders.TextFieldBorder(),
			    new MarginBorder());
	}	//	getTextFieldBorder

    
    /**************************************************************************
     * 	ComboBox Editor Border
     * 	@see MetalComboBoxEditor
     */
    public static class ComboBoxBorder extends AbstractBorder
	{
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected static final Insets editorBorderInsets = new Insets(2, 2, 2, 0);
        private static final Insets SAFE_EDITOR_BORDER_INSETS = new Insets(2, 2, 2, 0);

        /**
         * 	Paint Border
         *	@see javax.swing.border.AbstractBorder#paintBorder(java.awt.Component, java.awt.Graphics, int, int, int, int)
         */
		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
		{
			g.translate(x, y);
			g.setColor(MetalLookAndFeel.getControlDarkShadow());
			g.drawLine(0, 0, w - 1, 0);
			g.drawLine(0, 0, 0, h - 2);
			g.drawLine(0, h - 2, w - 1, h - 2);
			g.setColor(MetalLookAndFeel.getControlHighlight());
			g.drawLine(1, 1, w - 1, 1);
			g.drawLine(1, 1, 1, h - 1);
			g.drawLine(1, h - 1, w - 1, h - 1);
			g.setColor(MetalLookAndFeel.getControl());
			g.drawLine(1, h - 2, 1, h - 2);
			if (c.hasFocus())
			{
				g.setColor(fc);
				g.drawLine(0, 0, w, 0);
				g.drawLine(0, h-2, w, h-2);
			}
			g.translate(-x, -y);
		}	//	paintBorder

		@Override
		public Insets getBorderInsets(Component c)
		{
			if (System.getSecurityManager() != null)
				return SAFE_EDITOR_BORDER_INSETS;
			else
				return editorBorderInsets;
		}	//	getBorderInsets
		
	}	//	EditorBorder
    
    /**
     * 	Get Button Border
     *	@return Button Border
     */
    public static Border getButtonBorder()
	{
		UIDefaults table = UIManager.getLookAndFeelDefaults();
		Border buttonBorder = new BorderUIResource.CompoundBorderUIResource(
			new BasicBorders.ButtonBorder(table.getColor("Button.shadow"),
		        table.getColor("Button.darkShadow"), 
		        table.getColor("Button.light"), 
		        table.getColor("Button.highlight")), 
			new MarginBorder());
		return buttonBorder;
	}	//	getButtonBorder

    
    /**************************************************************************
     * 	Check Box Icon
     */
    public static class CheckBoxIcon
	    implements Icon, UIResource, Serializable
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		protected int getControlSize()
		{
			return 13;
		}
		
		/**
		 * 	Paint Ocean Icon
		 */
		private void paintOceanIcon(Component c, Graphics g, int x, int y)
		{
			JCheckBox cb = (JCheckBox)c;
			ButtonModel model = cb.getModel();
			g.translate(x, y);
			int w = getIconWidth();
			int h = getIconHeight();
			if (model.isEnabled())
			{
				if (model.isPressed() && model.isArmed())
				{
					g.setColor(MetalLookAndFeel.getControlShadow());
					g.fillRect(0, 0, w, h);
					g.setColor(MetalLookAndFeel.getControlDarkShadow());
					g.fillRect(0, 0, w, 2);
					g.fillRect(0, 2, 2, h - 2);
					g.fillRect(w - 1, 1, 1, h - 1);
					g.fillRect(1, h - 1, w - 2, 1);
				}
				else if (model.isRollover())
				{
//					CompiereUtils.drawGradient(c, g, "CheckBox.gradient", 0, 0, w, h, true);
					g.setColor(MetalLookAndFeel.getControlDarkShadow());
					g.drawRect(0, 0, w - 1, h - 1);
					g.setColor(MetalLookAndFeel.getPrimaryControl());
					g.drawRect(1, 1, w - 3, h - 3);
					g.drawRect(2, 2, w - 5, h - 5);
				}
				else
				{
//					CompiereUtils.drawGradient(c, g, "CheckBox.gradient", 0, 0, w, h, true);
					g.setColor(MetalLookAndFeel.getControlDarkShadow());
					g.drawRect(0, 0, w - 1, h - 1);
				}
				if (cb.hasFocus())
				{
					g.setColor(fc);
					g.drawRect(0, 0, w, h);
				}
				g.setColor(MetalLookAndFeel.getControlInfo());
			}
			else	//	disabled
			{
				g.setColor(MetalLookAndFeel.getControlDarkShadow());
				g.drawRect(0, 0, w - 1, h - 1);
			}
			g.translate(-x, -y);
			if (model.isSelected())
				drawCheck(c, g, x, y);
		}	//	paintOceanIcon

		/**
		 * 	Paint Icon
		 *	@see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
		 */
		public void paintIcon(Component c, Graphics g, int x, int y)
		{
//			if (MetalLookAndFeel.usingOcean())
			if (false)
			{
				paintOceanIcon(c, g, x, y);
				return;
			}
			JCheckBox cb = (JCheckBox)c;
			ButtonModel model = cb.getModel();
			int controlSize = getControlSize();
			if (model.isEnabled())
			{
				if (model.isPressed() && model.isArmed())
				{
					g.setColor(MetalLookAndFeel.getControlShadow());
					g.fillRect(x, y, controlSize - 1, controlSize - 1);
					drawPressed3DBorder(g, x, y, controlSize, controlSize);
				}
				else
				{
					drawFlush3DBorder(g, x, y, controlSize, controlSize);
				}
				if (cb.hasFocus())
				{
					g.translate(x, y);
					g.setColor(fc);
					g.drawRect(0, 0, controlSize-1, controlSize-1);
					g.translate(-x, -y);
				}
				g.setColor(MetalLookAndFeel.getControlInfo());
			}
			else	//	Disabled
			{
				g.setColor(MetalLookAndFeel.getControlShadow());
				g.drawRect(x, y, controlSize - 2, controlSize - 2);
			}
			if (model.isSelected())
			{
				drawCheck(c, g, x, y);
			}
		}	//	paintIcon

		/**
		 * 	Draw Check
		 */
		protected void drawCheck(Component c, Graphics g, int x, int y)
		{
			int controlSize = getControlSize();
			g.fillRect(x + 3, y + 5, 2, controlSize - 8);
			g.drawLine(x + (controlSize - 4), y + 3, x + 5, y
			    + (controlSize - 6));
			g.drawLine(x + (controlSize - 4), y + 4, x + 5, y
			    + (controlSize - 5));
		}	//	drawCheck

		public int getIconWidth()
		{
			return getControlSize();
		}

		public int getIconHeight()
		{
			return getControlSize();
		}
		

		
	}	//	CheckBoxIcon
    
    /**
     * This draws a variant "Flush 3D Border"
     * It is used for things like pressed buttons.
     */
    static void drawPressed3DBorder(Graphics g, int x, int y, int w, int h)
	{
		g.translate(x, y);
		drawFlush3DBorder(g, 0, 0, w, h);
		g.setColor(MetalLookAndFeel.getControlShadow());
		g.drawLine(1, 1, 1, h - 2);
		g.drawLine(1, 1, w - 2, 1);
		g.translate(-x, -y);
	}	//	drawPressed3DBorder

	/**
	 * This draws the "Flush 3D Border" which is used throughout the Metal L&F
	 */
	static void drawFlush3DBorder(Graphics g, int x, int y, int w, int h)
	{
		g.translate(x, y);
		g.setColor(MetalLookAndFeel.getControlDarkShadow());
		g.drawRect(0, 0, w - 2, h - 2);
		g.setColor(MetalLookAndFeel.getControlHighlight());
		g.drawRect(1, 1, w - 2, h - 2);
		g.setColor(MetalLookAndFeel.getControl());
		g.drawLine(0, h - 1, 1, h - 2);
		g.drawLine(w - 1, 0, w - 2, 1);
		g.translate(-x, -y);
	}	//	drawPressed3DBorder

}	// CompiereBorders
