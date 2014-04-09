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
import org.compiere.util.*;

/**
 *  Image Display of AD_Iamge_ID
 *
 *  @author  Jorg Janke
 *  @version $Id: VImage.java,v 1.6 2006/07/30 00:51:28 jjanke Exp $
 */
public class VImage extends JButton
	implements VEditor, ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  Image Editor
	 *  @param columnName column name
	 *  @param WindowNo window no
	 */
	public VImage (String columnName, int WindowNo)
	{
		super("-");
		m_columnName = columnName;
		m_WindowNo = WindowNo;
		super.addActionListener(this);
	}   //  VImage

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		m_mImage = null;
		m_field = null;
	}   //  dispose

	/** WindowNo                */
	private int     m_WindowNo;
	/** The Image Model         */
	private MImage  m_mImage = null;
	/** Mandatory flag          */
	private boolean m_mandatory = false;
	/** Column Name             */
	private String	m_columnName = "AD_Image_ID";
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VImage.class);

	/**
	 *  Set Value
	 *  @param value
	 */
	public void setValue(Object value)
	{
		log.fine("=" + value);
		int newValue = 0;
		if (value instanceof Integer)
			newValue = ((Integer)value).intValue();
		if (newValue == 0)
		{
			m_mImage = null;
			super.setText("-");
			super.setIcon(null);
			super.setToolTipText(null);
			return;
		}
		//  Get/Create Image
		if (m_mImage == null || newValue != m_mImage.get_ID())
			m_mImage = MImage.get (Env.getCtx(), newValue);
		//
		log.fine(m_mImage.toString());
		super.setText(null);
		super.setIcon(m_mImage.getIcon());
		super.setToolTipText(m_mImage.getName());
		invalidate();
	}   //  setValue

	/**
	 *  Get Value
	 *  @return value
	 */
	public Object getValue()
	{
		if (m_mImage == null || m_mImage.get_ID() == 0)
			return null;
		return Integer.valueOf(m_mImage.get_ID());
	}   //  getValue

	/**
	 *  Get Display Value
	 *  @return image name
	 */
	public String getDisplay()
	{
		return m_mImage.getName();
	}   //  getDisplay

	/**
	 *  Set ReadWrite
	 *  @param rw
	 */
	public void setReadWrite (boolean rw)
	{
		if (isEnabled() != rw)
			setEnabled (rw);
	}   //  setReadWrite

	/**
	 *  Get ReadWrite
	 *  @return true if rw
	 */
	public boolean isReadWrite()
	{
		return super.isEnabled();
	}   //  getReadWrite

	/**
	 *  Set Mandatory
	 *  @param mandatory
	 */
	public void setMandatory (boolean mandatory)
	{
		m_mandatory = mandatory;
	}   //  setMandatory

	/**
	 *  Get Mandatory
	 *  @return true if mandatory
	 */
	public boolean isMandatory()
	{
		return m_mandatory;
	}   //  isMandatory

	/**
	 *  Set Background - nop
	 *  @param color
	 */
	@Override
	public void setBackground(Color color)
	{
	}   //  setBackground

	/**
	 *  Set Background - nop
	 */
	public void setBackground()
	{
	}   //  setBackground

	/**
	 *  Set Background - nop
	 *  @param error
	 */
	public void setBackground(boolean error)
	{
	}   //  setBackground

	/**
	 *  Property Change
	 *  @param evt
	 */
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals(org.compiere.model.GridField.PROPERTY))
			setValue(evt.getNewValue());
	}   //  propertyChange

	/**
	 *  ActionListener - start dialog and set value
	 *  @param e
	 */
	public void actionPerformed (ActionEvent e)
	{
		VImageDialog vid = new VImageDialog(Env.getWindow(m_WindowNo), m_mImage);
		vid.setVisible(true);
		int AD_Image_ID = vid.getAD_Image_ID();
		Integer newValue = null;
		if (AD_Image_ID != 0)
			newValue = Integer.valueOf (AD_Image_ID);
		//
		m_mImage = null;	//	force reload
		setValue(newValue);	//	set explicitly
		//
		try
		{
			fireVetoableChange(m_columnName, null, newValue);
		}
		catch (PropertyVetoException pve)	{}
	}   //  actionPerformed

	/**
	 *  Set Field/WindowNo 
	 *  @param mField field
	 */
	public void setField (GridField mField)
	{
		if (mField != null)
			m_WindowNo = mField.getWindowNo();
		m_field = mField;
	}   //  setField

	/** Grid Field				*/
	private GridField 	m_field = null;
	
	/**
	 *  Get Field
	 *  @return gridField
	 */
	public GridField getField()
	{
		return m_field;
	}   //  getField

	/**
	 * 	Get Focus Component
	 *	@return component
	 */
	public Component getFocusableComponent()
	{
		return this;
	}	//	getFocusableComponent

}   //  VImage
