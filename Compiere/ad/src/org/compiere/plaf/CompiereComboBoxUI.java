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
import java.awt.event.*;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;


/**
 *  Compiere ComboBox UI.
 *  The ComboBox is opaque - with opaque arrow button and textfield background
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereComboBoxUI.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CompiereComboBoxUI extends MetalComboBoxUI
{
	/**
	 *  Create UI
	 *  @param c
	 *  @return new instance of CompiereComboBoxUI
	 */
	public static ComponentUI createUI(JComponent c)
	{
		return new CompiereComboBoxUI();
	}   //  CreateUI

	/**************************************************************************
	 *  Install UI - Set ComboBox opaque.
	 *  Bug in Metal: arrowButton gets Mouse Events, so add the JComboBox
	 *  MouseListeners to the arrowButton
	 *  @see org.compiere.swing.CComboBox#addMouseListener(MouseListener)
	 *  @param c component
	 */
	@Override
	public void installUI (JComponent c)
	{
		MouseListener[] ml = c.getMouseListeners();
		super.installUI(c);
		c.setOpaque(false);
		//
		for (MouseListener element : ml) {
		//	System.out.println("adding " + c.getClass().getName());
			arrowButton.addMouseListener(element);
		}
	}   //  installUI
	
	/**
	 * 	ConboBox Editor
	 */
    @Override
	protected ComboBoxEditor createEditor() 
    {
        return new CompiereComboBoxEditor.UIResource();
    }	//	createEditor

	/**
	 *  Create opaque button
	 *  @return opaque button
	 */
	@Override
	protected JButton createArrowButton()
	{
		JButton button = super.createArrowButton();
		button.setContentAreaFilled(false);
		button.setOpaque(false);
		return button;
	}   //  createArrowButton

	/**
	 * 	Get Arrow Button
	 *	@return button
	 */
	public JButton getArrowButton()
	{
		return arrowButton;
	}	//	getArrowButton

	/**
	 *  Set Icon  of arrow button
	 *  @param defaultIcon
	 */
	public void setIcon(Icon defaultIcon)
	{
		((MetalComboBoxButton)arrowButton).setComboIcon(defaultIcon);
	}   //  setIcon


	/**************************************************************************
	 *  Create Popup
	 *  @return CompiereComboPopup
	 */
	@Override
	protected ComboPopup createPopup()
	{
		CompiereComboPopup newPopup = new CompiereComboPopup( comboBox );
		newPopup.getAccessibleContext().setAccessibleParent(comboBox);
		return newPopup;
	}   //  createPopup

	/**
	 * @see javax.swing.plaf.basic.BasicComboBoxUI#getPreferredSize(javax.swing.JComponent)
	 */
	@Override
	public Dimension getPreferredSize(JComponent c)
	{
		Dimension size = super.getPreferredSize(c);
		
		if (c instanceof JComboBox)
		{
			JComboBox cBox = (JComboBox)c;
			JComponent edComp = (JComponent)cBox.getEditor().getEditorComponent();
			Dimension edSize = edComp.getPreferredSize();
			Insets insets = edComp.getInsets();
			size.height = edSize.height - insets.bottom - insets.top;
		}
		return size;
	}	//	getPreferredSize

}   //  CompiereComboBoxUI
