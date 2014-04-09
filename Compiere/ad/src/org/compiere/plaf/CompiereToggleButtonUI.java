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

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;

/**
 *  Compiere Toggle Button UI
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereToggleButtonUI.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CompiereToggleButtonUI extends MetalToggleButtonUI
{
	/**
	 *  Static Create UI
	 *  @param c Component
	 *  @return Compiere ToggleButton UI
	 */
	public static ComponentUI createUI (JComponent c)
	{
		return s_toggleButtonUI;
	}   //  createUI

	/** UI shared   */
	private static CompiereToggleButtonUI s_toggleButtonUI = new CompiereToggleButtonUI();

	
	/**************************************************************************
	 *  Install Defaults
	 *  @param b
	 */
	@Override
	public void installDefaults(AbstractButton b)
	{
		super.installDefaults(b);
		b.setOpaque(false);
	}   //  installDefaults

	/**
	 *  Update -
	 *  This method is invoked by <code>JComponent</code> when the specified
	 *  component is being painted.
	 *
	 *  By default this method will fill the specified component with
	 *  its background color (if its <code>opaque</code> property is
	 *  <code>true</code>) and then immediately call <code>paint</code>.
	 *
	 *  @param g the <code>Graphics</code> context in which to paint
	 *  @param c the component being painted
	 *
	 *  @see #paint
	 *  @see javax.swing.JComponent#paintComponent
	 */
	@Override
	public void update(Graphics g, JComponent c)
	{
		if (c.isOpaque())   //  flat background
	//		CompiereColor.getDefaultBackground().paint(g, c);
			CompiereUtils.fillRectange((Graphics2D)g, c, CompiereLookAndFeel.ROUND);
		paint (g, c);
	}   //  update

	/**
	 *  Paint 3D Box
	 *  @param g Graphics
	 *  @param c Component
	 */
	@Override
	public void paint(Graphics g, JComponent c)
	{
		AbstractButton b = (AbstractButton) c;
		ButtonModel model = b.getModel();
		boolean in = model.isPressed() || model.isSelected();
		//
		if( in )
			b.setBorderPainted(false);
		else
			b.setBorderPainted(true);
		CompiereUtils.paint3Deffect((Graphics2D)g, c, CompiereLookAndFeel.ROUND, !in, true);
		super.paint(g, c);
	}   //  paint

	/**
	 *  Don't get selected Color - use default (otherwise the pressed button is gray)
	 *  @param g
	 *  @param b
	 */
	@Override
	protected void paintButtonPressed(Graphics g, AbstractButton b)
	{
	//	if (b.isContentAreaFilled())
	//	{
	//		Dimension size = b.getSize();
	//		g.setColor(getSelectColor());
	//		g.fillRect(0, 0, size.width, size.height);
	//	}
	}   //  paintButtonPressed

}   //  CompiereToggleButton
