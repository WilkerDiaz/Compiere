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

import org.compiere.*;
import org.compiere.plaf.*;
import org.compiere.util.*;

/**
 *  Compiere Button supporting colored Background
 *
 *  @author     Jorg Janke
 *  @version    $Id: CButton.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CButton extends JButton implements CEditor
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Small
	 *	@param action sction
	 *	@return button with small icon
	 */
	public static CButton getSmall (String action)
	{
		String[] sizes = new String[] {"16.png", "16.gif", "22.png", "24.gif"};
		Icon icon = null;
		for (String element : sizes) {
			String iconFile = "images/" + action + element;
			icon = new ImageIcon(Compiere.class.getResource(iconFile));
	        if (icon != null && icon.getIconHeight() > 0)
	        	break;
        }
		CButton button = new CButton(icon);
		button.setMargin(new Insets(1, 1, 1, 1));
		button.setToolTipText(Msg.getMsg(Env.getCtx(), action));
		return button;
	}	//	getSmall
	
	
	/**************************************************************************
	 * Creates a button with no set text or icon.
	 */
	public CButton()
	{
		this (null, null);
	}	//	CButton

	/**
	 * Creates a button with an icon.
	 *
	 * @param icon  the Icon image to display on the button
	 */
	public CButton(Icon icon)
	{
		this (null, icon);
	}	//	CButton

	/**
	 * Creates a button with text.
	 *
	 * @param text  the text of the button
	 */
	public CButton(String text)
	{
		this (text, null);
	}	//	CButton

	/**
	 * Creates a button where properties are taken from the
	 * <code>Action</code> supplied.
	 *
	 * @param a the <code>Action</code> used to specify the new button
	 *
	 * @since 1.3
	 */
	public CButton (Action a)
	{
		super (a);
		setContentAreaFilled(false);
		setOpaque(false);
	}	//	CButton

	/**
	 * Creates a button with initial text and an icon.
	 *
	 * @param text  the text of the button
	 * @param icon  the Icon image to display on the button
	 */
	public CButton(String text, Icon icon)
	{
		super (text, icon);
		setContentAreaFilled(false);
		setOpaque(false);
		//
		setFont(CompierePLAF.getFont_Label());
		setForeground(CompierePLAF.getTextColor_Label());
	}   //  CButton

	
	/**************************************************************************
	 *  Set Background - Differentiates between system & user call.
	 *  If User Call, sets Opaque & ContextAreaFilled to true
	 *  @param bg background color
	 */
	@Override
	public void setBackground (Color bg)
	{
		if (bg.equals(getBackground()))
			return;
		super.setBackground (bg);
		//  ignore calls from javax.swing.LookAndFeel.installColors(LookAndFeel.java:61)
		if (!Trace.getCallerClass(1).startsWith("javax"))
		{
			setOpaque(true);
			setContentAreaFilled(true);
		}
		this.repaint();
	}   //  setBackground

	/**
	 *  Set Background - NOP
	 *  @param error error
	 */
	public void setBackground (boolean error)
	{
	}   //  setBackground

	/**
	 *  Set Standard Background
	 */
	public void setBackgroundColor ()
	{
		setBackgroundColor (null);
	}   //  setBackground

	/**
	 *  Set Background
	 *  @param bg CompiereColor for Background, if null set standard background
	 */
	public void setBackgroundColor (CompiereColor bg)
	{
		if (bg == null)
			bg = CompiereColor.getDefaultBackground();
		setOpaque(true);
		putClientProperty(CompierePLAF.BACKGROUND, bg);
		super.setBackground (bg.getFlatColor());
		this.repaint();
	}   //  setBackground

	/**
	 *  Get Background
	 *  @return Color for Background
	 */
	public CompiereColor getBackgroundColor ()
	{
		try
		{
			return (CompiereColor)getClientProperty(CompierePLAF.BACKGROUND);
		}
		catch (Exception e)
		{
			System.err.println("CButton - ClientProperty: " + e.getMessage());
		}
		return null;
	}   //  getBackgroundColor

	/** Mandatory (default false)   */
	private boolean m_mandatory = false;
	/** Read-Write                  */
	private boolean m_readWrite = true;

	/**
	 *	Set Editor Mandatory
	 *  @param mandatory true, if you have to enter data
	 */
	public void setMandatory (boolean mandatory)
	{
		m_mandatory = mandatory;
		setBackground(false);
	}   //  setMandatory

	/**
	 *	Is Field mandatory
	 *  @return true, if mandatory
	 */
	public boolean isMandatory()
	{
		return m_mandatory;
	}   //  isMandatory

	/**
	 *	Enable Editor
	 *  @param rw true, if you can enter/select data
	 */
	public void setReadWrite (boolean rw)
	{
		if (isEnabled() != rw)
			setEnabled(rw);
		m_readWrite = rw;
	}   //  setReadWrite

	/**
	 *	Is it possible to edit
	 *  @return true, if editable
	 */
	public boolean isReadWrite()
	{
		return m_readWrite;
	}   //  isReadWrite

	/**
	 *	Set Editor to value
	 *  @param value value of the editor
	 */
	public void setValue (Object value)
	{
		if (value == null)
			setText("");
		else
			setText(value.toString());
	}   //  setValue

	/**
	 *	Return Editor value
	 *  @return current value
	 */
	public Object getValue()
	{
		return getText();
	}   //  getValue

	/**
	 *  Return Display Value
	 *  @return displayed String value
	 */
	public String getDisplay()
	{
		return getText();
	}   //  getDisplay

	/**
	 *	Set Text & Mnemonic
	 *	@param text text
	 */
	@Override
	public void setText (String text)
	{
		if (text == null)
		{
			super.setText(text);
			return;
		}
		int pos = text.indexOf('&');
		if (pos != -1)					//	We have a nemonic - creates ALT-_
		{
			int mnemonic = text.toUpperCase().charAt(pos+1);
			if (mnemonic != ' ')
			{
				setMnemonic(mnemonic);
				text = text.substring(0, pos) + text.substring(pos+1);
			}
		}
		super.setText (text);
		if (getName() == null)
			setName (text);
	}	//	setText
	
	/**
	 * 	Set Tool Tip Text & Mnemonic
	 *	@param text text
	 */
	@Override
	public void setToolTipText (String text)
	{
		if (text == null)
		{
			super.setText(text);
			return;
		}
		int pos = text.indexOf('&');
		if (pos != -1)					//	We have a nemonic - creates ALT-_
		{
			int mnemonic = text.toUpperCase().charAt(pos+1);
			if (mnemonic != ' ')
			{
				setMnemonic(mnemonic);
				text = text.substring(0, pos) + text.substring(pos+1);
			}
		}
		super.setToolTipText (text);
		if (getName() == null)
			setName (text);
	}	//	setToolTipText
	
	/**
	 * 	Set Action Command
	 *	@param actionCommand command 
	 */
	@Override
	public void setActionCommand (String actionCommand)
	{
		super.setActionCommand (actionCommand);
		if (getName() == null && actionCommand != null && actionCommand.length() > 0)
			setName(actionCommand);
	}	//	setActionCommand
	
}   //  CButton
