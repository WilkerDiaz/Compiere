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

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;

/**
 *  Compiere Scroll Pane UI
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereScrollPaneUI.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CompiereScrollPaneUI extends MetalScrollPaneUI
{
	/**
	 * Create UI (not shared)
	 * @param x
	 * @return UI
	 */
	public static ComponentUI createUI(JComponent x)
	{
		return new CompiereScrollPaneUI();
	}   //  createUI

	/**
	 *  Install UI
	 *  @param c
	 */
	@Override
	public void installUI (JComponent c)
	{
		super.installUI(c);
		c.setOpaque(false);
		//  was ignored as set in constructor after updateUI - Sun bug: 138828
		((JScrollPane)c).getViewport().setOpaque(false);
	}   //  installUI

}   //  CompiereScrollPaneUI
