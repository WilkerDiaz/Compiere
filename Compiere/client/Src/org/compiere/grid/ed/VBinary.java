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
import java.io.*;
import java.util.logging.*;

import javax.swing.*;

import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 * 	Binary Editor.
 * 	Shows length of data.
 *	
 *  @author Jorg Janke
 *  @version $Id: VBinary.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class VBinary extends JButton
	implements VEditor, ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  Binary Editor
	 *  @param columnName column name
	 *  @param WindowNo
	 */
	public VBinary (String columnName, int WindowNo)
	{
		super("");
		m_columnName = columnName; 
		super.addActionListener(this);
		m_popupMenu.add(m_save);
		m_popupMenu.add(m_open);
		m_save.addActionListener(this);
		m_open.addActionListener(this);
	}   //  VBinary

	private JPopupMenu		 	m_popupMenu = new JPopupMenu();
	private CMenuItem 			m_save = new CMenuItem("Save to local File");
	private CMenuItem 			m_open = new CMenuItem("Open/Load into Database");

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		m_data = null;
		m_field = null;
	}   //  dispose


	/** Column Name             */
	private String	m_columnName;
	/** Data					*/
	private Object	m_data = null;
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VBinary.class);

	/**
	 *  Set Value
	 *  @param value
	 */
	public void setValue(Object value)
	{
		log.config("=" + value);
		m_data = value;
		if (m_data == null)
			setText("-");
		else
		{
			String text = "?";
			if (m_data instanceof byte[])
			{
				byte[] bb = (byte[])m_data;
				text = "#" + bb.length;
			}
			else
			{
				text = m_data.getClass().getName();
				int index = text.lastIndexOf(".");
				if (index != -1)
					text = text.substring(index+1);
			}
			setText(text);
		}
	}   //  setValue

	/**
	 *  Get Value
	 *  @return value
	 */
	public Object getValue()
	{
		return m_data;
	}   //  getValue

	/**
	 *  Get Display Value
	 *  @return image name
	 */
	public String getDisplay()
	{
		return getText();
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
	 *  @param mandatory NOP
	 */
	public void setMandatory (boolean mandatory)
	{
	}   //  setMandatory

	/**
	 *  Get Mandatory
	 *  @return false
	 */
	public boolean isMandatory()
	{
		return false;
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
		log.info(evt.toString());
		if (evt.getPropertyName().equals(org.compiere.model.GridField.PROPERTY))
			setValue(evt.getNewValue());
	}   //  propertyChange

	/**
	 *  ActionListener - start dialog and set value
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (e.getSource() == this)
		{
			if (m_data == null)
				fileAction(false);
			else
				m_popupMenu.show(this, 10, 10);
		}
		else if (e.getSource() == m_open)
			fileAction(false);
		else if (e.getSource() == m_save)
			fileAction(true);
	}   //  actionPerformed

	/**
	 * 	File action
	 *	@param save true=save to file - false=load to db
	 */
	private void fileAction (boolean save)
	{
		JFileChooser fc = new JFileChooser("");
		fc.setMultiSelectionEnabled(false);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int option = 0;
		if (save && m_data != null)
			option = fc.showSaveDialog(this);
		else
			option = fc.showOpenDialog(this);
		if (option != JFileChooser.APPROVE_OPTION)
			return;
		File file = fc.getSelectedFile();
		if (file == null)
			return;
		//
		log.info(file.toString());
		try
		{
			if (save && m_data != null)
			{
				FileOutputStream os = new FileOutputStream(file);
				byte[] buffer = (byte[])m_data;
				os.write(buffer);
				os.flush();
				os.close();
				log.config("Save to " + file + " #" + buffer.length);
			}
			else	//	load
			{
				FileInputStream is = new FileInputStream(file);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024*8];   //  8kB
				int length = -1;
				while ((length = is.read(buffer)) != -1)
					os.write(buffer, 0, length);
				is.close();
				byte[] data = os.toByteArray();
				m_data = data;
				os.close();
				log.config("Load from " + file + " #" + data.length);
			}
		}
		catch (Exception ex)
		{
			log.log(Level.WARNING, "Save=" + save, ex);
		}
		
		try
		{
			fireVetoableChange(m_columnName, null, m_data);
		}
		catch (PropertyVetoException pve)	{}
	}	//	fileAction
	
	/**
	 *  Set Field/WindowNo 
	 *  @param mField field
	 */
	public void setField (GridField mField)
	{
		if (mField != null)
			mField.getWindowNo();
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
	}	//	getFocusComponent

}	//	VBinary
