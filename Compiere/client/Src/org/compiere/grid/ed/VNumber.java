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
import java.math.*;
import java.text.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.text.*;

import org.compiere.apps.*;
import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Number Control
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: VNumber.java,v 1.2 2006/07/30 00:51:27 jjanke Exp $
 */
public final class VNumber extends JComponent
implements VEditor, ActionListener, KeyListener, FocusListener
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**	Number of Columns (12)			*/
	public final static int 		SIZE = 12;
	/** Automatically pop up calculator	*/
	public final static boolean		AUTO_POPUP = true;

	/**
	 *  IDE Bean Constructor
	 */
	public VNumber()
	{
		this("Number", false, false, true, DisplayTypeConstants.Number, "Number");
	}   //  VNumber

	/**
	 *	Create right aligned Number field.
	 *	no popup, if WindowNo == 0 (for IDs)
	 *  @param columnName column name
	 *  @param mandatory mandatory
	 *  @param isReadOnly read only
	 *  @param isUpdateable updateable
	 *  @param displayType display type
	 *  @param title title
	 */
	public VNumber(String columnName, boolean mandatory, boolean isReadOnly, boolean isUpdateable,
			int displayType, String title)
	{
		super();
		super.setName(columnName);
		m_columnName = columnName;
		m_title = title;
		setDisplayType(displayType);
		//
		LookAndFeel.installBorder(this, "TextField.border");
		this.setLayout(new BorderLayout());
		Dimension size = m_text.getPreferredSize();
		this.setPreferredSize(size);		//	causes r/o to be the same length
		int height = size.height;
		setMinimumSize(new Dimension(20,height));

		//	***	Text	***
		m_text.setHorizontalAlignment(SwingConstants.TRAILING);
		m_text.addKeyListener(this);
		m_text.addFocusListener(this);
		//	Background
		setMandatory(mandatory);
		this.add(m_text, BorderLayout.CENTER);

		//	***	Button	***
		m_button.setIcon(Env.getImageIcon("Calculator10.gif"));
		m_button.setFocusable(false);
		m_button.addActionListener(this);
		this.add (m_button, BorderLayout.EAST);

		setBorder( null );

		//	Preferred Size
		//	this.setPreferredSize(this.getPreferredSize());		//	causes r/o to be the same length

		//  Size
		setColumns(SIZE, VLookup.FIELD_HIGHT);
		//	ReadWrite
		if (isReadOnly || !isUpdateable)
			setReadWrite(false);
		else
			setReadWrite(true);
	}	//	VNumber

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		m_text = null;
		m_button = null;
		m_mField = null;
	}   //  dispose

	/**
	 *	Set Document
	 *  @param doc document
	 */
	protected void setDocument(Document doc)
	{
		m_text.setDocument(doc);
	}	//	getDocument

	private final String	m_columnName;
	protected int			m_displayType;	//  Currency / UoM via Context
	private DecimalFormat	m_format;
	private final String	m_title;
	private boolean			m_setting;
	private String			m_oldText;
	private String			m_initialText;

	private boolean			m_rangeSet = false;
	private Double			m_minValue = null;
	private Double			m_maxValue = null;
	private boolean			m_modified = false;

	/**  The Field                  */
	private CTextField		m_text = new CTextField(SIZE);	//	Standard
	/** The Button                  */
	private CButton		    m_button = new CButton();

	private GridField		m_mField = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VNumber.class);

	/**
	 * Select all the number text.
	 */
	public void selectAll()
	{
		m_text.selectAll();
	}

	/**
	 * 	Set no of Columns
	 *	@param columns columns
	 */
	public void setColumns (int columns, int height)
	{
		m_text.setPreferredSize(null);
		m_text.setColumns(columns);
		Dimension size = m_text.getPreferredSize();
		if (height > size.height)			//	default 16
			size.height = height;
		if (CComboBox.FIELD_HIGHT > size.height)
			size.height = VLookup.FIELD_HIGHT;
		this.setPreferredSize(size);		//	causes r/o to be the same length
		this.setMinimumSize(new Dimension (columns*10, size.height));
		m_button.setPreferredSize(new Dimension(size.height, size.height));
	}	//	setColumns

	/**
	 *	Set Range with min & max
	 *  @param minValue min value
	 *  @param maxValue max value
	 *	@return true, if accepted
	 */
	public boolean setRange(Double minValue, Double maxValue)
	{
		m_rangeSet = true;
		m_minValue = minValue;
		m_maxValue = maxValue;
		return m_rangeSet;
	}	//	setRange

	/**
	 *	Set Range with min & max = parse US style number w/o Grouping
	 *  @param minValue min value
	 *  @param maxValue max value
	 *  @return true if accepted
	 */
	public boolean setRange(String minValue, String maxValue)
	{
		if ((minValue == null) && (maxValue == null))
			return false;
		try
		{
			if (minValue != null)
				m_minValue = Double.valueOf(minValue);
			if (maxValue != null)
				m_maxValue = Double.valueOf(maxValue);
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}
		m_rangeSet = true;
		return m_rangeSet;
	}	//	setRange

	/**
	 *  Set and check DisplayType
	 *  @param displayType display type
	 */
	public void setDisplayType (int displayType)
	{
		m_displayType = displayType;
		if (!FieldType.isNumeric(displayType))
			m_displayType = DisplayTypeConstants.Number;
		m_format = DisplayType.getNumberFormat(displayType);
		m_text.setDocument (new MDocNumber(displayType, m_format, m_text, m_title));
	}   //  setDisplayType

	/**
	 *  Set and check DisplayType
	 *  @param displayType display type
	 *  @param precision overwrite maximum digits
	 */
	public void setDisplayType (int displayType, int precision)
	{
		m_displayType = displayType;
		if (!FieldType.isNumeric(displayType))
			m_displayType = DisplayTypeConstants.Number;
		m_format = DisplayType.getNumberFormat(displayType);
		m_format.setMaximumFractionDigits(precision);
		m_text.setDocument (new MDocNumber(displayType, m_format, m_text, m_title));
	}   //  setDisplayType
	/**
	 *	Set ReadWrite
	 *  @param value value
	 */
	public void setReadWrite (boolean value)
	{
		if (m_text.isReadWrite() != value)
			m_text.setReadWrite(value);
		if (m_button.isReadWrite() != value)
			m_button.setReadWrite(value);
		//	Don't show button if not ReadWrite
		if (m_button.isVisible() != value)
			m_button.setVisible(value);
	}	//	setReadWrite

	/**
	 *	IsReadWrite
	 *  @return true if rw
	 */
	public boolean isReadWrite()
	{
		return m_text.isReadWrite();
	}	//	isReadWrite

	/**
	 *	Set Mandatory (and back bolor)
	 *  @param mandatory mandatory
	 */
	public void setMandatory (boolean mandatory)
	{
		m_text.setMandatory(mandatory);
	}	//	setMandatory

	/**
	 *	Is it mandatory
	 *  @return true if mandatory
	 */
	public boolean isMandatory()
	{
		return m_text.isMandatory();
	}	//	isMandatory

	/**
	 *	Set Background
	 *  @param color color
	 */
	@Override
	public void setBackground(Color color)
	{
		m_text.setBackground(color);
	}	//	setBackground

	/**
	 *	Set Background
	 *  @param error error
	 */
	public void setBackground (boolean error)
	{
		m_text.setBackground(error);
	}	//	setBackground

	/**
	 *  Set Foreground
	 *  @param fg foreground
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
	public void setValue(Object value)
	{
		log.finest("Value=" + value);
		if (value == null)
			m_oldText = "";
		else
			m_oldText = m_format.format(value);
		//	only set when not updated here
		if (m_setting)
			return;
		m_text.setText (m_oldText);
		m_initialText = m_oldText;
		m_modified = false;
	}	//	setValue

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
	 *  @return value value (big decimal or integer)
	 */
	public Object getValue()
	{
		if ((m_text == null) || (m_text.getText() == null) || (m_text.getText().length() == 0))
			return null;
		String value = m_text.getText();
		//	return 0 if text deleted
		if ((value == null) || (value.length() == 0))
		{
			if (!m_modified)
				return null;
			if (m_displayType == DisplayTypeConstants.Integer)
				return Integer.valueOf(0);
			return Env.ZERO;
		}
		if (value.equals(".") || value.equals(",") || value.equals("-"))
			value = "0";
		try
		{
			Number number = m_format.parse(value);
			value = number.toString();      //	converts it to US w/o thousands
			BigDecimal bd = new BigDecimal(value);
			if (m_displayType == DisplayTypeConstants.Integer)
				if(bd.compareTo(new BigDecimal(Integer.MIN_VALUE)) < 0){
					//m_text.setText(Integer.toString(Integer.MIN_VALUE));
					//set the field blank
					m_text.setText("");
					log.warning("Integer value too small");
					return Env.ZERO;
				}
				else if(bd.compareTo(new BigDecimal(Integer.MAX_VALUE)) > 0){
					//m_text.setText(Integer.toString(Integer.MAX_VALUE));
					//set the field blank
					m_text.setText("");
					log.warning("Integer value too large");
					return Env.ZERO;
				}
				else
					return Integer.valueOf(bd.intValue());
			if (bd.signum() == 0)
				return bd;
			return bd.setScale(m_format.getMaximumFractionDigits(), BigDecimal.ROUND_HALF_UP);
		}
		catch (Exception e)
		{
			//revert to prev value if entered string isn't numeric
			try {
				m_text.setText(m_oldText);
				Number number = m_format.parse(m_oldText);
				value = number.toString();      //	converts it to US w/o thousands
				BigDecimal bd = new BigDecimal(value);
				if (m_displayType == DisplayTypeConstants.Integer)
					return Integer.valueOf(bd.intValue());
				if (bd.signum() == 0)
					return bd;
				return bd.setScale(m_format.getMaximumFractionDigits(), BigDecimal.ROUND_HALF_UP);
			} catch (ParseException e1){
				//just log; will return zero later
				log.log(Level.SEVERE, "Value=" + value, e);
			}
		}
		if (m_displayType == DisplayTypeConstants.Integer)
			return Integer.valueOf(0);
		return Env.ZERO;
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
	 * 	Get Title
	 *	@return title
	 */
	public String getTitle()
	{
		return m_title;
	}	//	getTitle

	/**
	 * 	Plus - add one.
	 * 	Also sets Value
	 *	@return new value
	 */
	public Object plus()
	{
		Object value = getValue();
		if (value == null)
		{
			if (m_displayType == DisplayTypeConstants.Integer)
				value = Integer.valueOf(0);
			else
				value = Env.ZERO;
		}
		//	Add
		if (value instanceof BigDecimal)
			value = ((BigDecimal)value).add(Env.ONE);
		else
			value = Integer.valueOf(((Integer)value).intValue() + 1);
		//
		setValue(value);
		return value;
	}	//	plus

	/**
	 * 	Minus - subtract one, but not below minimum.
	 * 	Also sets Value
	 *	@param minimum minimum
	 *	@return new value
	 */
	public Object minus (int minimum)
	{
		Object value = getValue();
		if (value == null)
		{
			if (m_displayType == DisplayTypeConstants.Integer)
				value = Integer.valueOf(minimum);
			else
				value = new BigDecimal(minimum);
			setValue(value);
			return value;
		}

		//	Subtract
		if (value instanceof BigDecimal)
		{
			BigDecimal bd = ((BigDecimal)value).subtract(Env.ONE);
			BigDecimal min = new BigDecimal(minimum);
			if (bd.compareTo(min) < 0)
				value = min;
			else
				value = bd;
		}
		else
		{
			int i = ((Integer)value).intValue();
			i--;
			if (i < minimum)
				i = minimum;
			value = Integer.valueOf(i);
		}
		//
		setValue(value);
		return value;
	}	//	minus

	/**************************************************************************
	 *	Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		log.config(e.getActionCommand());
		if (ValuePreference.NAME.equals(e.getActionCommand()))
		{
			if (MRole.getDefault().isShowPreference())
				ValuePreference.start (m_mField, getValue());
			return;
		}

		if (e.getSource() == m_button)
		{
			m_button.setEnabled(false);
			String str = startCalculator(this, m_text.getText(), m_format, m_displayType, m_title);
			m_text.setText(str);
			m_button.setEnabled(true);
			try
			{
				fireVetoableChange (m_columnName, m_oldText, getValue());
			}
			catch (PropertyVetoException pve)	{}
			m_text.requestFocus();
		}
	}	//	actionPerformed

	/**************************************************************************
	 *	Key Listener Interface
	 *  @param e event
	 */
	public void keyTyped(KeyEvent e)    {}
	public void keyPressed(KeyEvent e)  {}

	/**
	 *	Key Listener.
	 *		- Escape 		- Restore old Text
	 *		- firstChange	- signal change
	 *  @param e event
	 */
	public void keyReleased(KeyEvent e)
	{
		log.finest("Key=" + e.getKeyCode() + " - " + e.getKeyChar()
				+ " -> " + m_text.getText());

		//  ESC
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			m_text.setText(m_initialText);
		m_modified = true;
		m_setting = true;
		try
		{
			if (e.getKeyCode() == KeyEvent.VK_ENTER)	//	10
			{
				fireVetoableChange (m_columnName, m_oldText, getValue());
				fireActionPerformed();
			}
		}
		catch (PropertyVetoException pve)	{}
		m_setting = false;
	}	//	keyReleased

	/**
	 *	Focus Gained
	 *  @param e event
	 */
	public void focusGained (FocusEvent e)
	{
		if (m_text != null)
			m_text.selectAll();
	}	//	focusGained

	/**
	 *	Data Binding to MTable (via GridController.vetoableChange).
	 *  @param e event
	 */
	public void focusLost (FocusEvent e)
	{
		//	log.finest(e.toString());
		//	APanel - Escape
		if (e.getOppositeComponent() instanceof AGlassPane)
		{
			m_text.setText(m_initialText);
			return;
		}
		Object oo = getValue();
		if (m_rangeSet)
		{
			String error = null;
			if (oo instanceof Integer)
			{
				Integer ii = (Integer)oo;
				if ((m_minValue != null) && (ii  < m_minValue))
				{
					error = oo + " < " + m_minValue;
					oo = Integer.valueOf(m_minValue.intValue());
				}
				else if ((m_maxValue != null) && (ii > m_maxValue))
				{
					error = oo + " > " + m_maxValue;
					oo = Integer.valueOf(m_maxValue.intValue());
				}
			}
			else if (oo instanceof BigDecimal)
			{
				BigDecimal bd = (BigDecimal)oo;
				if ((m_minValue != null) && (bd.doubleValue()  < m_minValue))
				{
					error = oo + " < " + m_minValue;
					oo = new BigDecimal(m_minValue);
				}
				else if ((m_maxValue != null) && (bd.doubleValue() > m_maxValue))
				{
					error = oo + " > " + m_maxValue;
					oo = new BigDecimal(m_maxValue);
				}
			}
			if ((error != null) && (m_text != null))
			{
				log.warning(error);
				setValue(oo);
				Toolkit.getDefaultToolkit().beep();
				setBackground(true);
			}
		}
		try
		{
			fireVetoableChange (m_columnName, m_initialText, oo);
			fireActionPerformed();
		}
		catch (PropertyVetoException pve)
		{}
	}   //  focusLost

	/**
	 *	Invalid Entry - Start Calculator
	 *  @param jc parent
	 *  @param value value
	 *  @param format format
	 *  @param displayType display type
	 *  @param title title
	 *  @return value
	 */
	public static String startCalculator(Container jc, String value,
			DecimalFormat format, int displayType, String title)
	{
		log.config("Value=" + value);
		BigDecimal startValue = new BigDecimal(0.0);
		try
		{
			if ((value != null) && (value.length() > 0))
			{
				Number number = format.parse(value);
				startValue = new BigDecimal (number.toString());
			}
		}
		catch (ParseException pe)
		{
			log.info("InvalidEntry - " + pe.getMessage());
		}

		//	Find frame
		Frame frame = Env.getFrame(jc);
		//	Actual Call
		Calculator calc = new Calculator(frame, title,
				displayType, format, startValue);
		AEnv.showCenterWindow(frame, calc);
		BigDecimal result = calc.getNumber();
		log.config( "Result=" + result);
		//
		calc = null;
		if (result != null)
			return format.format(result);
		else
			return value;		//	original value
	}	//	startCalculator

	/**
	 *  Set Field/WindowNo for ValuePreference
	 *  @param mField field
	 */
	public void setField (GridField mField)
	{
		m_mField = mField;
		/**
		if (m_mField != null
			&& MRole.getDefault().isShowPreference())
			ValuePreference.addMenu (this, popupMenu);
		 **/
	}   //  setField

	/**
	 *  Get Field
	 *  @return gridField
	 */
	public GridField getField()
	{
		return m_mField;
	}   //  getField


	/**************************************************************************
	 * 	Remove Action Listner
	 * 	@param l Action Listener
	 */
	public void removeActionListener(ActionListener l)
	{
		listenerList.remove(ActionListener.class, l);
	}	//	removeActionListener

	/**
	 * 	Add Action Listner
	 * 	@param l Action Listener
	 */
	public void addActionListener(ActionListener l)
	{
		listenerList.add(ActionListener.class, l);
	}	//	addActionListener

	/**
	 * 	Fire Action Event to listeners
	 */
	protected void fireActionPerformed()
	{
		int modifiers = 0;
		AWTEvent currentEvent = EventQueue.getCurrentEvent();
		if (currentEvent instanceof InputEvent)
			modifiers = ((InputEvent)currentEvent).getModifiers();
		else if (currentEvent instanceof ActionEvent)
			modifiers = ((ActionEvent)currentEvent).getModifiers();
		ActionEvent ae = new ActionEvent (this, ActionEvent.ACTION_PERFORMED,
				"VNumber", EventQueue.getMostRecentEventTime(), modifiers);

		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i]==ActionListener.class)
			{
				((ActionListener)listeners[i+1]).actionPerformed(ae);
			}
		}
	}	//	fireActionPerformed
	/**/
}	//	VNumber
