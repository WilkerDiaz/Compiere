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
import java.util.logging.*;

import javax.swing.*;

import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Location Control (Address)
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: VLocation.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class VLocation extends JComponent
	implements VEditor, ActionListener
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Constructor
	 *
	 * 	@param columnName column name
	 * 	@param mandatory mandatory
	 * 	@param isReadOnly read only
	 * 	@param isUpdateable updateable
	 * 	@param mLocation location model
	 */
	public VLocation(String columnName, boolean mandatory, boolean isReadOnly, boolean isUpdateable,
		MLocationLookup mLocation)
	{
		super();
		super.setName(columnName);
		m_columnName = columnName;
		m_mLocation = mLocation;
		//
		LookAndFeel.installBorder(this, "TextField.border");
		this.setLayout(new BorderLayout());

		//  Size
		Dimension size = m_text.getPreferredSize();
		this.setPreferredSize(size);		//	causes r/o to be the same length

		// normailize for buttons
		size.width = size.height;

		//  Edit Button
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
		this.add(buttonPanel, BorderLayout.EAST);

		//  to Internet Edit Button
		m_toMapButton.setIcon(Env.getImageIcon("ToLink16.gif"));
		m_toMapButton.setMargin(new Insets(0,0,0,0));
		m_toMapButton.setPreferredSize(size);
		m_toMapButton.addActionListener(this);
		buttonPanel.add(m_toMapButton);

		m_editButton.setIcon(Env.getImageIcon("Location10.gif"));
		m_editButton.setPreferredSize(size);
		m_editButton.addActionListener(this);
		buttonPanel.add(m_editButton);

		//	***	Button & Text	***
		m_text.setEditable(false);
		m_text.setFocusable(false);
		m_text.setFont(CompierePLAF.getFont_Field());
		m_text.setForeground(CompierePLAF.getTextColor_Normal());

		setBorder( null );

        //  Popup
        m_text.addMouseListener(new MouseAdapter()
        {
            @Override
			public void mouseClicked(MouseEvent e)
            {
                if (SwingUtilities.isRightMouseButton(e))
                    m_popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
            }
        });

        String actionKey = m_text.getClass().getName() + "_popop";
        InputMap iMap = m_text.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_MASK);
        iMap.put(ks, actionKey);
        m_text.getActionMap().put(actionKey, new AbstractAction()
        {
			private static final long serialVersionUID = 1L;
			//
			public void actionPerformed(ActionEvent e)
            {
                Component comp = (Component)e.getSource();
                m_popupMenu.show(comp, 10, 10);
            }
        });

		this.add(m_text, BorderLayout.CENTER);

		//	Editable
		if (isReadOnly || !isUpdateable)
			setReadWrite (false);
		else
			setReadWrite (true);
		setMandatory (mandatory);
		//
		mDelete = new CMenuItem(Msg.getMsg(Env.getCtx(), "Delete"), Env.getImageIcon ("Delete16.gif"));
		mDelete.addActionListener(this);
		m_popupMenu.add(mDelete);

	}	//	VLocation

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		m_text = null;
		m_editButton = null;
		m_mLocation = null;
		m_field = null;
	}   //  dispose

	/** The Text Field                  */
	private JTextField			m_text = new JTextField(VLookup.DISPLAY_LENGTH);

	/** The Edit location Button                      */
	private CButton				m_editButton = new CButton();

	/** The "to map" Button                      */
	private final CButton				m_toMapButton = new CButton();

	private MLocationLookup		m_mLocation;
	private MLocation			m_value;

	private final String				m_columnName;

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VLocation.class);

	//	Popup
	JPopupMenu 					m_popupMenu = new JPopupMenu();
	private final CMenuItem 			mDelete;

	/**
	 *	Enable/disable
	 *  @param value true if ReadWrite
	 */
	public void setReadWrite (boolean value)
	{
		m_editButton.setReadWrite (value);
		if (m_editButton.isVisible() != value)
			m_editButton.setVisible (value);
		setBackground(false);
	}	//	setReadWrite

	/**
	 *	IsReadWrite
	 *  @return value true if ReadWrite
	 */
	public boolean isReadWrite()
	{
		return m_editButton.isReadWrite();
	}	//	isReadWrite

	/**
	 *	Set Mandatory (and back bolor)
	 *  @param mandatory true if mandatory
	 */
	public void setMandatory (boolean mandatory)
	{
		m_editButton.setMandatory(mandatory);
		setBackground(false);
	}	//	setMandatory

	/**
	 *	Is it mandatory
	 *  @return true if mandatory
	 */
	public boolean isMandatory()
	{
		return m_editButton.isMandatory();
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
	public void setValue(Object value)
	{
		if (value == null)
		{
			m_value = null;
			m_text.setText(null);
		}
		else
		{
			m_value = m_mLocation.getLocation(value, null);
			if (m_value == null)
				m_text.setText("<" + value + ">");
			else
				m_text.setText(m_value.toString());
		}

		m_toMapButton.setEnabled(value != null);
		m_text.setToolTipText(m_text.getText());
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
	 *  @param evt PropertyChangeEvent
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
		if (m_value == null)
			return null;
		return Integer.valueOf(m_value.getC_Location_ID());
	}	//	getValue

	/**
	 *	Return Editor value
	 *  @return value
	 */
	public int getC_Location_ID()
	{
		if (m_value == null)
			return 0;
		return m_value.getC_Location_ID();
	}	//	getC_Location_ID

	/**
	 *  Return Display Value
	 *  @return display value
	 */
	public String getDisplay()
	{
		return m_text.getText();
	}   //  getDisplay

	/**
	 *	ActionListener - Button - Start Dialog
	 *  @param ae ActionEvent
	 */
	public void actionPerformed(ActionEvent ae)
	{
		Object source = ae.getSource();

		if (source == mDelete)
			m_value = null;        //  create new

		if (source == m_toMapButton)
		{
			Env.startBrowser(Env.GOOGLE_MAPS_URL_PREFIX + m_value);
		}
		else
		{
			log.config("" + m_value);
			VLocationDialog ld = new VLocationDialog(Env.getFrame(this),
				Msg.getMsg(Env.getCtx(), "Location"), m_value);
			ld.setVisible(true);
			m_value = ld.getValue();

			if ((source != mDelete) && !ld.isChanged())
				return;

			//	Data Binding
			try
			{
				int C_Location_ID = 0;
				if (m_value != null)
					C_Location_ID = m_value.getC_Location_ID();
				Integer ii = Integer.valueOf(C_Location_ID);
				//  force Change - user does not realize that embedded object is already saved.
				fireVetoableChange(m_columnName, null, null);   //  resets m_mLocation
				if (C_Location_ID != 0)
					fireVetoableChange(m_columnName, null, ii);
				setValue(ii);
			}
			catch (PropertyVetoException pve)
			{
				log.log(Level.SEVERE, "", pve);
			}
		}
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

}	//	VLocation
