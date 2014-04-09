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

import org.compiere.apps.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 * 	URL Editor
 *	
 *  @author Jorg Janke
 *  @version $Id: VURL.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class VURL extends JComponent
	implements VEditor, ActionListener, KeyListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	IDE Constructor
	 */
	public VURL ()
	{
		this ("URL", false, false, true, 20, 60);
	}	//	VURL
	
	/**
	 *	Detail Constructor
	 *  @param columnName column name
	 *  @param mandatory mandatory
	 *  @param isReadOnly read only
	 *  @param isUpdateable updateable
	 *  @param displayLength display length
	 *  @param fieldLength field length
	 */
	public VURL (String columnName, boolean mandatory, boolean isReadOnly, boolean isUpdateable,
		int displayLength, int fieldLength)
	{
		super.setName(columnName);
		m_columnName = columnName;
		m_fieldLength = fieldLength;
		m_mandatory = mandatory;
		LookAndFeel.installBorder(this, "TextField.border");
		this.setLayout(new BorderLayout());
		//  Size
		this.setPreferredSize(m_text.getPreferredSize());
		int height = m_text.getPreferredSize().height;

		//	***	Text	***
		m_text = new CTextField(displayLength>VString.MAXDISPLAY_LENGTH ? VString.MAXDISPLAY_LENGTH : displayLength);
		m_text.setEditable(isReadOnly);
		m_text.setFocusable(true);
		m_text.setHorizontalAlignment(SwingConstants.LEADING);

		//	Background
		setMandatory(mandatory);
		this.add(m_text, BorderLayout.CENTER);

		//	***	Button	***
		m_button.setIcon(Env.getImageIcon("Online10.gif"));	//	should be 10
		m_button.setPreferredSize(new Dimension(height, height));
		m_button.addActionListener(this);
		m_button.setFocusable(false);
		this.add(m_button, BorderLayout.EAST);
		
		setBorder( null );

		//	Prefereed Size
		this.setPreferredSize(this.getPreferredSize());		//	causes r/o to be the same length
		//	ReadWrite
		if (isReadOnly || !isUpdateable)
			setReadWrite(false);
		else
			setReadWrite(true);

		m_text.addKeyListener(this);
		m_text.addActionListener(this);
		//	Popup for Editor
		if (fieldLength > displayLength)
		{

            //  Popup
            addMouseListener(new MouseAdapter()
            {
                @Override
				public void mouseClicked(MouseEvent e)
                {
                    if (SwingUtilities.isRightMouseButton(e))
                        m_popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
                }
            });
            
            String actionKey = getClass().getName() + "_popop";
            InputMap iMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
            KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_MASK);
            iMap.put(ks, actionKey);
            getActionMap().put(actionKey, new AbstractAction()
            {
                /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e)
                {
                    Component comp = (Component)e.getSource();
                    m_popupMenu.show(comp, 10, 10);
                }
            });
            
			mEditor = new CMenuItem (Msg.getMsg(Env.getCtx(), "Editor"), Env.getImageIcon("Editor16.gif"));
			mEditor.addActionListener(this);
			m_popupMenu.add(mEditor);
		}
		setForeground(CompierePLAF.getTextColor_Normal());
		setBackground(CompierePLAF.getFieldBackground_Normal());
	}	//	VURL

	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger (VURL.class);
	/** Column Name				*/
	private String				m_columnName;
	/** The Text				*/
	private CTextField			m_text = new CTextField();
	private boolean				m_readWrite;
	private boolean				m_mandatory;
	/** The Button              */
	private CButton				m_button = new CButton();
	/**	Popup Menu				*/
	JPopupMenu 					m_popupMenu = new JPopupMenu();
	/** Editor Menu Item		*/
	private CMenuItem 			mEditor;
	/** Grid Field				*/
	private GridField      		m_mField = null;

	private String				m_oldText;
	private String				m_initialText;
	/**	Setting new value			*/
	private volatile boolean	m_setting = false;
	/** Field Length				*/
	private int					m_fieldLength;

	/**
	 * 	Dispose resources
	 */
	public void dispose()
	{
		m_text = null;
		m_button = null;
		m_mField = null;
	}	//	dispose

	/**
	 * 	Set Mandatory
	 * 	@param mandatory mandatory
	 */
	public void setMandatory (boolean mandatory)
	{
		m_mandatory = mandatory;
		m_text.setMandatory(mandatory);
		setBackground (false);
	}	//	setMandatory

	/**
	 * 	Get Mandatory
	 *  @return mandatory
	 */
	public boolean isMandatory()
	{
		return m_mandatory;
	}	//	isMandatory

	/**
	 * 	Set ReadWrite
	 * 	@param rw read rwite
	 */
	public void setReadWrite (boolean rw)
	{
		m_readWrite = rw;
		m_text.setReadWrite(rw);
		setBackground (false);
	}	//	setReadWrite

	/**
	 * 	Is Read Write
	 * 	@return read write
	 */
	public boolean isReadWrite()
	{
		return m_readWrite;
	}	//	isReadWrite

	/**
	 * 	Set Foreground
	 * 	@param color color
	 */
	@Override
	public void setForeground (Color color)
	{
		m_text.setForeground(color);
	}	//	SetForeground

	/**
	 * 	Set Background
	 * 	@param error Error
	 */
	public void setBackground (boolean error)
	{
		if (error)
			setBackground(CompierePLAF.getFieldBackground_Error());
		else if (!m_readWrite)
			setBackground(CompierePLAF.getFieldBackground_Inactive());
		else if (m_mandatory)
			setBackground(CompierePLAF.getFieldBackground_Mandatory());
		else
			setBackground(CompierePLAF.getFieldBackground_Normal());
	}	//	setBackground

	/**
	 * 	Set Background
	 * 	@param color Color
	 */
	@Override
	public void setBackground (Color color)
	{
		m_text.setBackground(color);
	}	//	setBackground

	/**
	 *	Set Editor to value
	 *  @param value value
	 */
	public void setValue(Object value)
	{
	//	log.config( "VString.setValue", value);
		if (value == null)
			m_oldText = "";
		else
			m_oldText = value.toString();
		//	only set when not updated here
		if (m_setting)
			return;
		setText (m_oldText);
		m_initialText = m_oldText;
		//	If R/O left justify 
		if (!m_text.isEditable() || !isEnabled())
			m_text.setCaretPosition(0);
	}	//	setValue

	/**
	 *  Property Change Listener
	 *  @param evt event
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
	public Object getValue()
	{
		return getText();
	}	//	getValue

	/**
	 *  Return Display Value
	 *  @return value
	 */
	public String getDisplay()
	{
		return m_text.getText();
	}   //  getDisplay


	/**
	 *	Key Released.
	 *	if Escape Restore old Text
	 *  @param e event
	 */
	public void keyReleased(KeyEvent e)
	{
		log.finest("Key=" + e.getKeyCode() + " - " + e.getKeyChar()
			+ " -> " + getText());
		//  ESC
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			setText(m_initialText);
		m_setting = true;
		try
		{
			String clear = getText();
			if (clear.length() > m_fieldLength)
				clear = clear.substring(0, m_fieldLength);
			fireVetoableChange (m_columnName, m_oldText, clear);
		}
		catch (PropertyVetoException pve)	
		{
		}
		m_setting = false;
	}	//	keyReleased

	/**
	 * 	Key Pressed
	 *	@param e ignored
	 */
	public void keyPressed (KeyEvent e)
	{
	}	//	keyPressed
	/**
	 * 	Key Typed
	 *	@param e ignored
	 */
	public void keyTyped (KeyEvent e)
	{
	}	//	keyTyped
	
	/**
	 * 	Add Action Listener
	 *	@param listener listener
	 */
	public void addActionListener (ActionListener listener)
	{
		m_text.addActionListener(listener);
	}	//	addActionListener
	
	/**
	 *  Action Listener Interface
	 *  @param listener
	 */
	public void removeActionListener(ActionListener listener)
	{
		m_text.removeActionListener(listener);
	}   //  removeActionListener

	/**
	 *	Data Binding to MTable (via GridController)	-	Enter pressed
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals(ValuePreference.NAME))
		{
			if (MRole.getDefault().isShowPreference())
				ValuePreference.start (m_mField, getValue());
			return;
		}

		//  Invoke Editor
		else if (e.getSource() == mEditor)
		{
			String s = Editor.startEditor(this, Msg.translate(Env.getCtx(), m_columnName), 
				getText(), m_text.isEditable(), m_fieldLength, null);
			setText(s);
		}
		//	
		else if (e.getSource() == m_button)
		{
			action_button();
			return;
		}
		//  Data Binding
		try
		{
			fireVetoableChange(m_columnName, m_oldText, getText());
		}
		catch (PropertyVetoException pve)	
		{
		}
	}	//	actionPerformed

	/**
	 * 	Action button pressed - show URL
	 */
	private void action_button()
	{
		String urlString = m_text.getText();
		String message = null;
		if (urlString != null && urlString.length() > 0)
		{
			try
			{
				Env.startBrowser(urlString);
				return;
			}
			catch (Exception e)
			{
				message = e.getMessage();
			}
		}
		ADialog.warn(0, this, "URLnotValid", message);
	}	//	action button
	
	/**
	 *  Set Field/WindowNo
	 *  @param mField field
	 */
	public void setField (GridField mField)
	{
		m_mField = mField;
		if (m_mField != null
			&& MRole.getDefault().isShowPreference())
			ValuePreference.addMenu (this, m_popupMenu);
	}   //  setField

	/**
	 *  Get Field
	 *  @return gridField
	 */
	public GridField getField()
	{
		return m_mField;
	}   //  getField

	/**
	 * 	Set Text
	 *	@param text text
	 */
	public void setText (String text)
	{
		m_text.setText (text);
	}	//	setText

	
	/**
	 * 	Get Text (clear)
	 *	@return text
	 */
	public String getText ()
	{
		String text = m_text.getText();
		return text;
	}	//	getText

	/**
	 * 	Focus Gained.
	 * 	Enabled with Obscure
	 *	@param e event
	 */
	public void focusGained (FocusEvent e)
	{
		setText(getText());		//	clear
	}	//	focusGained

	/**
	 * 	Focus Lost
	 * 	Enabled with Obscure
	 *	@param e event
	 */
	public void focusLost (FocusEvent e)
	{
		setText(getText());		//	obscure
	}	//	focus Lost
	
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

}	//	VURL
