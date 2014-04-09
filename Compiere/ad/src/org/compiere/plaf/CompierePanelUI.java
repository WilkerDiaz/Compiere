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

/**
 *  Panel UI.
 *  The default properties can be set via
 *  <pre>
 *  CompierePanelUI.setDefaultBackground (new CompiereColor());
 *  </pre>
 *  The individual Panel can set the background type by setting the
 *  parameter via
 *  <pre>
 *  putClientProperty(CompierePanelUI.BACKGROUND, new CompiereColor());
 *  </pre>
 *  @see org.compiere.swing.CPanel
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompierePanelUI.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CompierePanelUI extends BasicPanelUI
{
	/**
	 *  Static Create UI
	 *  @param c Vomponent
	 *  @return Compiere Panel UI
	 */
	public static ComponentUI createUI (JComponent c)
	{
	//	return new CompierePanelUI();
		return s_panelUI;
	}   //  createUI

	/** UI                  */
	private static CompierePanelUI  s_panelUI = new CompierePanelUI();

	
	/**************************************************************************
	 * Install Defaults
	 * @param p Panel
	 */
	@Override
	protected void installDefaults (JPanel p)
	{
		super.installDefaults(p);
		/** If enabled, all windows are with Compiere Background,
		 * 	but Sun dialogs (print ..) are "patchy" as they are opaque		**
	//	System.out.println ("BG=" + p.getClientProperty(CompierePLAF.BACKGROUND));
		if (s_setDefault || p.getClientProperty(CompierePLAF.BACKGROUND) == null)
			p.putClientProperty (CompierePLAF.BACKGROUND, s_default);
		/** **/
	}   //  installDefaults

	
	/**************************************************************************
	 *  Update.
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
	 *  @see javax.swing.JComponent#paintComponent
	 */
	@Override
	public void update (Graphics g, JComponent c)
	{
	//	CompiereUtils.printParents (c);
		if (c.isOpaque())
			updateIt (g, c);
		paint (g, c);   //  does nothing
	}   //  update

	/**
	 *  Print background based on CompiereColor or flat background if not found
	 *  @param g
	 *  @param c
	 */
	static void updateIt (Graphics g, JComponent c)
	{
	//	System.out.print("Panel " + c.getName());
	//	System.out.print(" Bounds=" + c.getBounds().toString());
	//	System.out.print(" - Background: ");

		//  Get CompiereColor
		CompiereColor bg = null;
		try
		{
			bg = (CompiereColor)c.getClientProperty(CompierePLAF.BACKGROUND);
		}
		catch (Exception e)
		{
			System.err.println("CompierePanelUI - ClientProperty: " + e.getMessage());
		}
		//  paint compiere background
		if (bg != null)
		{
	//		System.out.print(bg);
			bg.paint (g, c);
		}
		else
		{
	//		System.out.print(c.getBackground());
			g.setColor(c.getBackground());
			g.fillRect(0,0, c.getWidth(), c.getHeight());
		}
	//	System.out.println();
	}   //  updateIt

	/*************************************************************************/

	/** Default Background      			*/
	private static CompiereColor    s_default = new CompiereColor();
	/** Set Background to default setting	*/
	private static boolean          s_setDefault = false;


	/**
	 *  Set Default Background
	 *  @param bg Background Color
	 */
	public static void setDefaultBackground (CompiereColor bg)
	{
		if (bg == null)
			return;
		s_default.setColor(bg);
	}   //  setBackground

	/**
	 *  Get Default Background
	 *  @return Background
	 */
	public static CompiereColor getDefaultBackground()
	{
		return s_default;
	}   //  getBackground

	/**
	 *  Set Default Background
	 *  @param setDefault if true, the background will be set to the default color
	 */
	public static void setSetDefault (boolean setDefault)
	{
		s_setDefault = setDefault;
	}   //  setSetDefault

	/**
	 *  Is the Default Background set by default
	 *  @return true if default background is set
	 */
	public static boolean isSetDefault()
	{
		return s_setDefault;
	}   //  isSetDefault

}   //  CompierePanel
