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
 *  Compiere Menu Bar UI.
 *  Main Menu background
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereMenuBarUI.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CompiereMenuBarUI extends MetalMenuBarUI
{
	/**
	 *  Create own instance
	 *  @param x
	 *  @return CompiereMenuBarUI
	 */
	public static ComponentUI createUI (JComponent x)
	{
		return new CompiereMenuBarUI();
	}   // createUI

	/**
	 *  Install UI
	 *  @param c
	 */
	@Override
	public void installUI (JComponent c)
	{
		super.installUI(c);
		c.setOpaque(true);
	}   //  installUI

	/**
	 * 	Update UI
	 *	@param g graphics
	 *	@param c component
	 */
	@Override
	public void update (Graphics g, JComponent c)
	{
		if (c.isOpaque())
		{
			//  Get CompiereColor
			CompiereColor bg = CompiereColor.getDefaultBackground();
			bg.paint (g, c);
		}
		else
		{
			g.setColor(c.getBackground());
			g.fillRect(0,0, c.getWidth(), c.getHeight());
		}
		paint(g,c);
	}	//	update
	
}   //  CompiereMenuBarUI
