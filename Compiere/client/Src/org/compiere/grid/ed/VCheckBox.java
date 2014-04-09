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
package org.compiere.grid.ed;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;

import javax.swing.*;

import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  Checkbox Control
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: VCheckBox.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class VCheckBox extends CCheckBox
	implements VEditor, ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Default Constructor
	 */
	public VCheckBox()
	{
		this("", false, false, true, "", null, false);
	}	//	VCheckBox

	/**
	 *	Standard Constructor
	 *  @param columnName
	 *  @param mandatory
	 *  @param isReadOnly
	 *  @param isUpdateable
	 *  @param title
	 *  @param description
	 *  @param tableEditor
	 */
	public VCheckBox(String columnName, boolean mandatory, boolean isReadOnly, boolean isUpdateable,
		String title, String description, boolean tableEditor)
	{
		super();
		super.setName(columnName);
		m_columnName = columnName;
		setMandatory(mandatory);
		//
		if (isReadOnly || !isUpdateable)
			setEditable(false);
		else
			setEditable(true);

		//  Normal
		if (!tableEditor)
		{
			setText(title);
			if (description != null && description.length() > 0)
				setToolTipText(description);
		}
		else
		{
			setHorizontalAlignment(SwingConstants.CENTER);
		}
		//
		this.addActionListener(this);
	}	//	VCheckBox

	/** Mnemonic saved			*/
	private char		m_savedMnemonic = 0;
	/** Grid Field				*/
	private GridField 	m_field = null;	
	/** Popup					*/
	JPopupMenu		 	m_popupMenu = new JPopupMenu();

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		m_field = null;
	}   //  dispose

	private String		m_columnName;

	/**
	 *	Set Editable
	 *  @param value
	 */
	public void setEditable (boolean value)
	{
		super.setReadWrite(value);
	}	//	setEditable

	/**
	 *	IsEditable
	 *  @return true if editable
	 */
	public boolean isEditable()
	{
		return super.isReadWrite();
	}	//	isEditable

	/**
	 *	Set Editor to value
	 *  @param value
	 */
	@Override
	public void setValue (Object value)
	{
		boolean sel = false;
		if (value != null)
		{
			if (value instanceof Boolean)
				sel = ((Boolean)value).booleanValue();
			else
				sel = "Y".equals(value);
		}
		setSelected(sel);
	}	//	setValue

	/**
	 *  Property Change Listener
	 *  @param evt
	 */
	public void propertyChange (PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals(org.compiere.model.GridField.PROPERTY))
			setValue(evt.getNewValue());
	}   //  propertyChange

	/**
	 *	Return Editor value
	 *  @return value
	 */
	@Override
	public Object getValue()
	{
		return Boolean.valueOf (isSelected());
	}	//	getValue

	/**
	 *  Return Display Value
	 *  @return value
	 */
	@Override
	public String getDisplay()
	{
		String value = isSelected() ? "Y" : "N";
		return Msg.getMsg(Env.getCtx(), value);
	}   //  getDisplay

	/**
	 *	Set Background (nop)
	 */
	public void setBackground()
	{
	}	//	setBackground

	/**
	 *	Action Listener	- data binding
	 *  @param e
	 */
	public void actionPerformed(ActionEvent e)
	{
		//  Preference
		if (e.getActionCommand().equals(ValuePreference.NAME))
		{
			if (MRole.getDefault().isShowPreference())
			{
				ValuePreference.start (m_field, getValue(), getDisplay());
			}
			return;
		}

	//	ADebug.info("VCheckBox.actionPerformed");
		try
		{
			fireVetoableChange(m_columnName, null, getValue());
		}
		catch (PropertyVetoException pve)
		{
		}
	}	//	actionPerformed

	/**
	 *  Set Field/WindowNo 
	 *  @param mField field
	 */
	public void setField (GridField mField)
	{
		m_field = mField;
		if (m_field != null 
			&& !m_field.getColumnName().equals("IsActive")
			&& MRole.getDefault().isShowPreference())
		{
			ValuePreference.addMenu (this, m_popupMenu);
			//	Popup
	        MouseListener mouseListener = new MouseAdapter()
	        {
	            @Override
				public void mouseClicked(MouseEvent e)
	            {
	                if (SwingUtilities.isRightMouseButton(e))
	                    m_popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
	            }
	        };    //  popup
	        this.addMouseListener(mouseListener);
		}
	}   //  setField
	
	/**
	 *  Get Field
	 *  @return gridField
	 */
	public GridField getField()
	{
		return m_field;
	}   //  getField

	/**
	 * @return Returns the savedMnemonic.
	 */
	public char getSavedMnemonic ()
	{
		return m_savedMnemonic;
	}	//	getSavedMnemonic
	
	/**
	 * @param savedMnemonic The savedMnemonic to set.
	 */
	public void setSavedMnemonic (char savedMnemonic)
	{
		m_savedMnemonic = savedMnemonic;
	}	//	getSavedMnemonic

	/**
	 * 	Get Focus Component
	 *	@return component
	 */
	public Component getFocusableComponent()
	{
		return this;
	}	//	getFocusableComponent

}	//	VCheckBox
