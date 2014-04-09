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
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;

/**
 *  Check Box UI
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereCheckBoxUI.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CompiereCheckBoxUI extends MetalCheckBoxUI
{
	/**
	 *  Create UI
	 *  @param b
	 *  @return ComponentUI
	 */
	public static ComponentUI createUI (JComponent b)
	{
		return s_checkBoxUI;
	}   //  createUI

	/** UI shared   */
	private static CompiereCheckBoxUI s_checkBoxUI = new CompiereCheckBoxUI();

	
	/**************************************************************************
	 *  Install Defaults
	 *  @param b
	 */
	@Override
	public void installDefaults (AbstractButton b)
	{
		super.installDefaults(b);
		b.setOpaque(false);
	}   //  installDefaults

	/**
	 * 	Create Button Listener
	 *	@param b button
	 *	@return listener
	 */
	@Override
	protected BasicButtonListener createButtonListener (AbstractButton b)
	{
		return new CompiereButtonListener(b);
	}	//	createButtonListener
	
	@Override
	protected void paintIcon(Graphics g, AbstractButton b, Rectangle iconRect)
	{
	    super.paintIcon(g, b, iconRect);
	}
	@Override
	protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect)
	{
	    super.paintIcon(g, c, iconRect);
	}
}   //  CompiereCheckBoxUI
