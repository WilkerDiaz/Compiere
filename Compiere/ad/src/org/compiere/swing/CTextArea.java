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
import java.awt.event.*;
import java.awt.im.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.text.*;

import org.compiere.plaf.*;

/**
 *  Compiere TextArea - A ScrollPane with a JTextArea.
 *  Manages visibility, opaque and color consistently
 *
 *  @author     Jorg Janke
 *  @version    $Id: CTextArea.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CTextArea extends JScrollPane
	implements CEditor, FocusListener, PublishInterface
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new TextArea.  A default model is set, the initial string
	 * is null, and rows/columns are set to 0.
	 */
	public CTextArea()
	{
		this (new JTextArea());
	}	//	CText

	/**
	 * Constructs a new TextArea with the specified text displayed.
	 * A default model is created and rows/columns are set to 0.
	 *
	 * @param text the text to be displayed, or null
	 */
	public CTextArea (String text)
	{
		this (new JTextArea (text));
	}	//	CText

	/**
	 * Constructs a new empty TextArea with the specified number of
	 * rows and columns.  A default model is created, and the initial
	 * string is null.
	 *
	 * @param rows the number of rows >= 0
	 * @param columns the number of columns >= 0
	 * @exception IllegalArgumentException if the rows or columns
	 *  arguments are negative.
	 */
	public CTextArea (int rows, int columns)
	{
		this (new JTextArea (rows, columns));
	}	//	CText

	/**
	 * Constructs a new TextArea with the specified text and number
	 * of rows and columns.  A default model is created.
	 *
	 * @param text the text to be displayed, or null
	 * @param rows the number of rows >= 0
	 * @param columns the number of columns >= 0
	 * @exception IllegalArgumentException if the rows or columns
	 *  arguments are negative.
	 */
	public CTextArea (String text, int rows, int columns)
	{
		this (new JTextArea(text, rows, columns));
	}	//	CText

	/**
	 * Constructs a new JTextArea with the given document model, and defaults
	 * for all of the other arguments (null, 0, 0).
	 *
	 * @param doc  the model to use
	 */
	public CTextArea (Document doc)
	{
		this (new JTextArea (doc));
	}	//	CText

	/**
	 * Constructs a new JTextArea with the specified number of rows
	 * and columns, and the given model.  All of the constructors
	 * feed through this constructor.
	 *
	 * @param doc the model to use, or create a default one if null
	 * @param text the text to be displayed, null if none
	 * @param rows the number of rows >= 0
	 * @param columns the number of columns >= 0
	 * @exception IllegalArgumentException if the rows or columns
	 *  arguments are negative.
	 */
	public CTextArea (Document doc, String text, int rows, int columns)
	{
		this (new JTextArea (doc, text, rows, columns));
	}	//	CTextArea


	/**
	 *  Create a JScrollArea with a JTextArea.
	 *  (use Compiere Colors, Line wrap)
	 *  @param textArea
	 */
	public CTextArea (JTextArea textArea)
	{
		super (textArea);
		m_textArea = textArea;
		super.setOpaque(false);
		super.getViewport().setOpaque(false);
		m_textArea.setFont(CompierePLAF.getFont_Field());
		m_textArea.setForeground(CompierePLAF.getTextColor_Normal());
		m_textArea.setLineWrap(true);
		m_textArea.setWrapStyleWord(true);
		//	Overwrite default Tab
		m_textArea.firePropertyChange("editable", !isEditable(), isEditable());

		setBackground(false);
		m_textArea.addFocusListener(this);
	}   //  CTextArea

	/**	Text Area					*/
	private JTextArea m_textArea = null;
	/** New Line			*/
	private static final String NL = System.getProperty("line.separator");

	/*************************************************************************/

	/** Mandatory (default false)   */
	private boolean m_mandatory = false;

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
		if (m_textArea.isEditable() != rw)
			m_textArea.setEditable (rw);
		setBackground(false);
	}   //  setReadWrite

	/**
	 *	Is it possible to edit
	 *  @return true, if editable
	 */
	public boolean isReadWrite()
	{
		return m_textArea.isEditable();
	}   //  isReadWrite

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
		else if (m_mandatory)
			setBackground(CompierePLAF.getFieldBackground_Mandatory());
		else
			setBackground(CompierePLAF.getFieldBackground_Normal());
	}   //  setBackground

	/**
	 * 	Set Background color
	 *	@param color color
	 */
	@Override
	public void setBackground (Color color)
	{
		if (color.equals(getBackground()))
			return;
		if (m_textArea == null)     //  during init
			super.setBackground(color);
		else
			m_textArea.setBackground(color);
	}
	/**
	 * 	Get Background color
	 *	@return background
	 */
	@Override
	public Color getBackground ()
	{
		if (m_textArea == null)     //  during init
			return super.getBackground();
		else
			return m_textArea.getBackground();
	}
	/**
	 * 	Set Foreground color
	 *	@param color
	 */
	@Override
	public void setForeground (Color color)
	{
		if (m_textArea == null)     //  during init
			super.setForeground(color);
		else
			m_textArea.setForeground(color);
	}
	/**
	 * 	Get Foreground color
	 *	@return color
	 */
	@Override
	public Color getForeground ()
	{
		if (m_textArea == null)     //  during init
			return super.getForeground();
		else
			return m_textArea.getForeground();
	}

	/**
	 *	Set Editor to value
	 *  @param value value of the editor
	 */
	public void setValue (Object value)
	{
		if (value == null)
			m_textArea.setText("");
		else
			m_textArea.setText(value.toString());
	}   //  setValue

	/**
	 *	Return Editor value
	 *  @return current value
	 */
	public Object getValue()
	{
		return m_textArea.getText();
	}   //  getValue

	/**
	 *  Return Display Value
	 *  @return displayed String value
	 */
	public String getDisplay()
	{
		return m_textArea.getText();
	}   //  getDisplay

	/*************************************************************************
	 *  Set Text and position top
	 *  @param text
	 */
	public void	setText (String text)
	{
		m_textArea.setText(text);
		m_textArea.setCaretPosition(0);
	}	//	setText
	
	/**
	 * 	Get Text
	 *	@return text
	 */
	public String getText()
	{
		return m_textArea.getText();
	}	//	getText
	
	/**
	 * 	Append text
	 *	@param text
	 */
	public void append (String text)
	{
		m_textArea.append (text);
	}	// append

	/**
	 * 	Add Text (in New Line)
	 *	@param text text to be added
	 */
	public void addText(String text)
	{
		m_textArea.append(NL);
		m_textArea.append(text);
		String newText = m_textArea.getText();
		m_textArea.setCaretPosition(newText.length());
	}	// addText

	/**
	 * 	Set Columns
	 *	@param cols
	 */
	public void setColumns (int cols)
	{
		m_textArea.setColumns (cols);
	}
	/**
	 * 	Get Columns
	 *	@return columns
	 */
	public int getColumns()
	{
		return m_textArea.getColumns();
	}

	/**
	 * 	Set Rows
	 *	@param rows
	 */
	public void setRows (int rows)
	{
		m_textArea.setRows(rows);
	}
	/**
	 * 	Get Rows
	 *	@return rows
	 */
	public int getRows()
	{
		return m_textArea.getRows();
	}

	/**
	 * 	Set Text Caret Position
	 *	@param pos
	 */
	public void setCaretPosition (int pos)
	{
		m_textArea.setCaretPosition (pos);
	}
	/**
	 * 	Get  Text Caret Position
	 *	@return position
	 */
	public int getCaretPosition()
	{
		return m_textArea.getCaretPosition();
	}

	/**
	 * 	Set Text Editable
	 *	@param edit
	 */
	public void setEditable (boolean edit)
	{
		m_textArea.setEditable(edit);
	}
	/**
	 * 	Is Text Editable
	 *	@return true if editable
	 */
	public boolean isEditable()
	{
		return m_textArea.isEditable();
	}

	/**
	 * 	Set Text Line Wrap
	 *	@param wrap
	 */
	public void setLineWrap (boolean wrap)
	{
		m_textArea.setLineWrap (wrap);
	}
	/**
	 * 	Set Text Wrap Style Word
	 *	@param word
	 */
	public void setWrapStyleWord (boolean word)
	{
		m_textArea.setWrapStyleWord (word);
	}

	/**
	 * 	Set Opaque
	 *	@param isOpaque
	 */
	@Override
	public void setOpaque (boolean isOpaque)
	{
		//  JScrollPane & Viewport is always not Opaque
		if (m_textArea == null)     //  during init of JScrollPane
			super.setOpaque(isOpaque);
		else
			m_textArea.setOpaque(isOpaque);
	}   //  setOpaque

	/**
	 * 	Set Text Margin
	 *	@param m insets
	 */
	public void setMargin (Insets m)
	{
		if (m_textArea != null)
			m_textArea.setMargin(m);
	}	//	setMargin
	
	
	/**
	 * 	AddFocusListener
	 *	@param l
	 */
	@Override
	public void addFocusListener (FocusListener l)
	{
		if (m_textArea == null) //  during init
			super.addFocusListener(l);
		else
			m_textArea.addFocusListener(l);
	}
	/**
	 * 	Add Text Mouse Listener
	 *	@param l
	 */
	@Override
	public void addMouseListener (MouseListener l)
	{
		m_textArea.addMouseListener(l);
	}
	/**
	 * 	Add Text Key Listener
	 *	@param l
	 */
	@Override
	public void addKeyListener (KeyListener l)
	{
		m_textArea.addKeyListener(l);
	}
	/**
	 * 	Add Text Input Method Listener
	 *	@param l
	 */
	@Override
	public void addInputMethodListener (InputMethodListener l)
	{
		m_textArea.addInputMethodListener(l);
	}
	/**
	 * 	Get text Input Method Requests
	 *	@return requests
	 */
	@Override
	public InputMethodRequests getInputMethodRequests()
	{
		return m_textArea.getInputMethodRequests();
	}
	/**
	 * 	Set Text Input Verifier
	 *	@param l
	 */
	@Override
	public void setInputVerifier (InputVerifier l)
	{
		m_textArea.setInputVerifier(l);
	}

	/**
	 * 	Request Focus
	 */
	@Override
	public void requestFocus()
	{
		m_textArea.requestFocus();
	}	//	requestFocus

	/**
	 * 	Request Focus In Window
	 *	@return focus request
	 */
	@Override
	public boolean requestFocusInWindow()
	{
		return m_textArea.requestFocusInWindow();
	}	//	requestFocusInWindow
	
	/**
	 * 	Get Focus Component
	 *	@return component
	 */
	public Component getFocusableComponent()
	{
		return m_textArea;
	}	//	getFocusComponent


	/**
	 * Initialize Publishing
	 */
	public void initPublish()
	{
		m_textArea.setEditable(false);
	}	// initPublish

	/**
	 * Publish Information to User.
	 * @param level type/importance - use INFO for main task - Create Database --
	 *        CONFIG for areas - (not used for this example) --- FINE for the
	 *        actual work - Drop User ---- FINER for trace - database feedback *
	 *        WARNING if we can continue * SEVERE if we need to abort the
	 *        process
	 * @param info the clear text info
	 * @param step the step you are on
	 * @param totalSteps the total number of steps for this task
	 */
	public void publish(Level level, String info, int step, int totalSteps)
	{
		addText(step + ": " + info);
	}	// publish

	/**
	 * 	Focus Listener - Gained
	 *	@param fe event
	 */
	public void focusGained(FocusEvent fe)
	{
		if (isEditable())
		{
			m_textArea.setCaretPosition(0);
			m_textArea.repaint();
		}
	}	//	focusGained
	
	/**
	 * 	Focus Listener - Lost
	 *	@param fe event
	 */
	public void focusLost(FocusEvent fe)
	{
		if (isEditable())
		{
			m_textArea.setCaretPosition(0);
			m_textArea.repaint();
		}
	}	//	focusLost
	
	public JTextArea getTextArea()
	{
		return m_textArea;
	}

}   //  CTextArea
