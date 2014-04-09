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

import javax.swing.*;

import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	File/Path Selection
 *
 *  @version 	$Id: VFile.java 8523 2010-03-05 22:40:49Z freyes $
 */
public class VFile extends JComponent
	implements VEditor, ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Text Field - for file name      */
	private CTextField			m_text = new CTextField(VLookup.DISPLAY_LENGTH);
	/** Button - for launching file dialog */
	private CButton				m_button = new CButton();
	/** Column Name */
	private String				m_columnName;

	/** Title */
	private String				m_title;
	/** Selection Mode - Directory vs File */
	private int					m_selectionMode = JFileChooser.DIRECTORIES_ONLY;
	/** Save/Open - save dialog vs open dialog	*/
	private int			m_dialogType = JFileChooser.CUSTOM_DIALOG;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VFile.class);

	public enum DialogType {SAVE_DIALOG, OPEN_DIALOG, CUSTOM_DIALOG};
	/**
	 *	Constructor
	 *
	 * 	@param columnName column name
	 * 	@param mandatory mandatory
	 * 	@param isReadOnly read only
	 * 	@param isUpdateable updateable
	 * 	@param files Files only if false Directory only
	 */
	public VFile(String columnName, String title, boolean mandatory, 
		boolean isReadOnly, boolean isUpdateable, boolean files, int dialogType)
	{
		super();
		super.setName(columnName);
		setColumnName(columnName);
		m_title = title;
		m_dialogType = dialogType;
		
		if (files)	//	default Directories
			m_selectionMode = JFileChooser.FILES_ONLY;
		
		//
		LookAndFeel.installBorder(this, "TextField.border");
		this.setLayout(new BorderLayout());

		//  Size
		this.setPreferredSize(m_text.getPreferredSize());
		setBorder( null );

		setTextProperties();
		setButtonProperties();
		setReadWrite (!isReadOnly && isUpdateable);
		setMandatory (mandatory);
	}	//	VFile

	public void setTextProperties() {
		m_text.setFocusable(true);
		m_text.setFont(CompierePLAF.getFont_Field());
		m_text.setForeground(CompierePLAF.getTextColor_Normal());
		m_text.addMouseListener(new MouseAdapter()
        {
            @Override
			public void mouseClicked(MouseEvent e)
            {               
            }
        });;
		this.add(m_text, BorderLayout.CENTER);
	}
	
	public void setButtonProperties() {
		//  Button
		m_button.setFocusable(false);
		m_button.setIcon(Env.getImageIcon("Open16.gif"));
		int height = m_text.getPreferredSize().height;
		m_button.setPreferredSize(new Dimension(height, height));
		m_button.addActionListener(this);
		this.add(m_button, BorderLayout.EAST);
	}
	/**
	 *  Dispose
	 */
	public void dispose()
	{
		m_text = null;
		m_button = null;
		m_field = null;
	}   //  dispose


	/**
	 *	Enable/disable
	 *  @param value true if ReadWrite
	 */
	@Override
	public void setReadWrite (boolean value)
	{
		m_button.setReadWrite (value);
		m_text.setReadWrite(value);
		m_button.setFocusable(value);
		m_text.setFocusable(value);
		if (m_button.isVisible() != value)
			m_button.setVisible(value);
		setBackground(false);
	}	//	setReadWrite

	/**
	 *	IsReadWrite
	 *  @return value true if ReadWrite
	 */
	@Override
	public boolean isReadWrite()
	{
		return m_button.isReadWrite();
	}	//	isReadWrite

	/**
	 *	Set Mandatory (and back bolor)
	 *  @param mandatory true if mandatory
	 */
	@Override
	public void setMandatory (boolean mandatory)
	{
		m_button.setMandatory(mandatory);
		setBackground(false);
	}	//	setMandatory

	/**
	 *	Is it mandatory
	 *  @return true if mandatory
	 */
	@Override
	public boolean isMandatory()
	{
		return m_button.isMandatory();
	}	//	isMandatory

	/**
	 *	Set Background
	 *  @param color color
	 */
	@Override
	public void setBackground (Color color)
	{
		if (!color.equals(m_text.getBackground()))
			m_text.setBackground(color);
	}	//	setBackground

	/**
	 *  Set Background based on editable / mandatory / error
	 *  @param error if true, set background to error color, otherwise mandatory/editable
	 */
	public void setBackground (boolean error)
	{
		if (error)
			setBackground(CompierePLAF.getFieldBackground_Error());
		else if (!isReadWrite())
			setBackground(CompierePLAF.getFieldBackground_Inactive());
		else if (isMandatory())
			setBackground(CompierePLAF.getFieldBackground_Mandatory());
		else
			setBackground(CompierePLAF.getFieldBackground_Normal());
	}   //  setBackground

	/**
	 *  Set Foreground
	 *  @param fg color
	 */
	@Override
	public void setForeground(Color fg)
	{
		m_text.setForeground(fg);
	}   //  setForeground

	/**
	 *	Set Editor to value
	 *  @param value value
	 */
	@Override
	public void setValue(Object value)
	{
		if (value == null)
			m_text.setText(null);
		else
			m_text.setText(value.toString());
	}	//	setValue

	/**
	 *  Property Change Listener
	 *  @param evt PropertyChangeEvent
	 */
	@Override
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
		return m_text.getText();
	}	//	getValue

	/**
	 *  Return Display Value
	 *  @return display value
	 */
	@Override
	public String getDisplay()
	{
		return m_text.getText();
	}   //  getDisplay

	/**
	 *	ActionListener - Button - Start Dialog
	 *  @param e ActionEvent
	 */
	public void actionPerformed(ActionEvent e)
	{
		
		JFileChooser chooser = new JFileChooser(m_text.getText());
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileSelectionMode(m_selectionMode);
		chooser.setDialogTitle(m_title);
		chooser.setDialogType(m_dialogType);
		//	
		int returnVal = -1;
		if (m_dialogType == JFileChooser.SAVE_DIALOG)
			returnVal = chooser.showSaveDialog(this);
		else if (m_dialogType == JFileChooser.OPEN_DIALOG)
			returnVal = chooser.showOpenDialog(this);
		else //	if (m_dialogType == JFileChooser.CUSTOM_DIALOG)
			returnVal= chooser.showDialog(this, m_title);
		if (returnVal != JFileChooser.APPROVE_OPTION)
			return;
		
		File selectedFile = chooser.getSelectedFile();
		if(selectedFile == null || 
				(!selectedFile.exists() && m_dialogType != JFileChooser.SAVE_DIALOG))
			return;
		
		log.config(selectedFile.getAbsolutePath());
		m_text.setText(selectedFile.getAbsolutePath() );
		
	}	//	actionPerformed

	/**
	 *  Action Listener Interface
	 *  @param listener listener
	 */
	public void addActionListener(ActionListener listener)
	{
		m_text.addActionListener(listener);
	}   //  addActionListener

	/**
	 *  Action Listener Interface
	 *  @param listener
	 */
	public void removeActionListener(ActionListener listener)
	{
		m_text.removeActionListener(listener);
	}   //  removeActionListener

	/**
	 *  Set Field/WindowNo 
	 *  @param mField field
	 */
	public void setField (GridField mField)
	{
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
	 * 	Request Focus
	 */
	@Override
	public void requestFocus()
	{
		m_text.requestFocus();
	}	//	requestFocus

	/**
	 * 	Request Focus In Window
	 *	@return focus request
	 */
	@Override
	public boolean requestFocusInWindow()
	{
		return m_text.requestFocusInWindow();
	}	//	requestFocusInWindow
	
	/**
	 * 	Get Focus Component
	 *	@return component
	 */
	public Component getFocusableComponent()
	{
		return m_text;
	}	//	getFocusComponent

	public String getColumnName() {
		return m_columnName;
	}

	public void setColumnName(String mColumnName) {
		m_columnName = mColumnName;
	}

}	//	VFile

