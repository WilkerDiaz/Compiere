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

import javax.swing.plaf.*;

/**
 * 	Compiere default Blue Metal Color Theme
 *	
 *  @author Jorg Janke, Adam Michau
 *  @version $Id: CompiereThemeBlueMetal.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CompiereThemeBlueMetal extends CompiereTheme
{
	/**
	 * 	Compiere default Theme Blue Metal
	 */
	public CompiereThemeBlueMetal()
	{
		setDefault();
		s_theme = this;
		s_name = NAME;
	}	//	CompiereThemeBlueMetal
	
	/**	Name			*/
	public static final String	NAME = "Compiere Theme";
	
	/**
	 *  Set Defaults
	 */
	@Override
	protected void setDefault()
	{
		/** Blue 102, 102, 153      */
		primary1 =      new ColorUIResource(102, 102, 153);
		/** Blue 153, 153, 204      */
		primary2 =      new ColorUIResource(153, 153, 204);
		/** Blue 204, 204, 255      */
		primary3 =      new ColorUIResource(204, 204, 255);
		/** Gray 102, 102, 102      */
		secondary1 =    new ColorUIResource(102, 102, 102);
		/** Gray 153, 153, 153      */
		secondary2 =    new ColorUIResource(153, 153, 153);
		/** BlueGray 214, 224, 234 - background */
		secondary3 =    new ColorUIResource(205, 215, 231);

		/** Black                   */
		black =         new ColorUIResource(Color.black);
		/** White                   */
		white =         new ColorUIResource(Color.white);

		/** Background for mandatory fields */
		mandatory =     new ColorUIResource(255, 255, 204); //  yellow-isch
		/** Background for fields in error  */
		error =         new ColorUIResource(255, 204, 204); //  red-isch
		/** Background for inactive fields  */
		inactive =      new ColorUIResource(200, 200, 200);	//	light gray
		/** Background for info fields      */
		info =          new ColorUIResource(253, 237, 207);	//	light yellow

		/** Foreground Text OK      */
		txt_ok =        new ColorUIResource(51, 51, 102);   //  dark blue
		/** Foreground Text Error   */
		txt_error =     new ColorUIResource(204, 0, 0);     //  dark red

		/** Control font            */
		controlFont = null;
		_getControlTextFont();
		/** System font             */
		systemFont = null;
		_getSystemTextFont();
		/** User font               */
		userFont = null;
		_getUserTextFont();
		/** Small font              */
		smallFont = null;
		_getSubTextFont();
		/** Window Title font       */
		windowFont = null;
		_getWindowTitleFont();
		/** Menu font               */
		menuFont = null;
		_getMenuTextFont();
	}   //  setDefault
	
}	//	CompiereThemeBlueMetal
