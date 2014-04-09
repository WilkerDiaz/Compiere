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
package org.compiere.swing;

import java.awt.*;

import javax.swing.*;

import org.compiere.plaf.*;

/**
 *	Compiere Srcoll Pane.
 *	
 *  @author Jorg Janke
 *  @version $Id: CScrollPane.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CScrollPane extends JScrollPane
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Compiere ScollPane
	 */
	public CScrollPane ()
	{
		this (null, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}	//	CScollPane

	/**
	 * 	Compiere ScollPane
	 *	@param vsbPolicy vertical policy
	 *	@param hsbPolicy horizontal policy
	 */
	public CScrollPane (int vsbPolicy, int hsbPolicy)
	{
		this (null, vsbPolicy, hsbPolicy);
	}	//	CScollPane

	/**
	 * 	Compiere ScollPane
	 *	@param view view
	 */
	public CScrollPane (Component view)
	{
		this (view, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}	//	CScollPane

	/**
	 * 	Compiere ScollPane
	 *	@param view view
	 *	@param vsbPolicy vertical policy
	 *	@param hsbPolicy horizontal policy
	 */
	public CScrollPane (Component view, int vsbPolicy, int hsbPolicy)
	{
		super (view, vsbPolicy, hsbPolicy);
		setBackgroundColor(null);
		setOpaque(false);
		getViewport().setOpaque(false);
	}	//	CScollPane
	
	
	/**
	 *  Set Background
	 *  @param bg CompiereColor for Background, if null set standard background
	 */
	public void setBackgroundColor (CompiereColor bg)
	{
		if (bg == null)
			bg = CompierePanelUI.getDefaultBackground();
		putClientProperty(CompierePLAF.BACKGROUND, bg);
	//	super.setBackground(bg.getFlatColor());
	//	getViewport().putClientProperty(CompierePLAF.BACKGROUND, bg);
	//	getViewport().setBackground(bg.getFlatColor());
	//	getViewport().setOpaque(true);
	}   //  setBackground
	
}	//	CScollPane
