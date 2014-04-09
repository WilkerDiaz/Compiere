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
 *  Compiere Tool Bar
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereToolBarUI.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CompiereToolBarUI extends MetalToolBarUI
{
	/**
	 *  Create UI (own instance)
	 *  @param c
	 *  @return CompiereToolBarUI
	 */
	public static ComponentUI createUI (JComponent c)
	{
		return new CompiereToolBarUI();
	}   //  createUI


	/**
	 *  Install UI - not Opaque
	 *  @param c
	 */
	@Override
	public void installUI( JComponent c )
	{
		super.installUI (c);
		c.setOpaque(false);
	}   //  installUI

	/**
	 * 	JToolbar error :  I just instanciate and display a toolbar
	 *	in the AWT dispatcher thread. When the toolbar is repaint
	 *	with a SwingUtilities.updateTreeView, buttons are larger
	 *	than usual. Note that I have several toolbars
	 *	== paint is done by Metal, so error is there ==
	 */
	
}   //  CompiereToolBarUI
