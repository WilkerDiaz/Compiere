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
import java.sql.*;
import java.util.logging.*;

import javax.swing.*;

import org.compiere.apps.*;
import org.compiere.apps.search.*;
import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  Lookup Visual Field.
 *  <p>
 *	    When r/o - display a Label
 *		When STABLE - display a ComboBox
 *		Otherwise show Selection Dialog
 *  <p>
 *  Special handling of BPartner and Product
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: VLookup.java,v 1.5 2006/10/06 00:42:38 jjanke Exp $
 */
public class VLookup extends JComponent
	implements VEditor, ActionListener, FocusListener
{
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 *  Create Optional BPartner Search Lookup
	 *  @param WindowNo window
	 *  @return VLookup
	 */
	public static VLookup createBPartner (int WindowNo)
	{
		int AD_Column_ID = 3499;    //  C_Invoice.C_BPartner_ID
		try
		{
			Lookup lookup = MLookupFactory.get (Env.getCtx(), WindowNo,
				AD_Column_ID, DisplayTypeConstants.Search);
			return new VLookup ("C_BPartner_ID", false, false, true, lookup);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		return null;
	}   //  createBPartner

	/**
	 *  Create Optional Product Search Lookup
	 *  @param WindowNo window
	 *  @return VLookup
	 */
	public static VLookup createProduct (int WindowNo)
	{
		int AD_Column_ID = 3840;    //  C_InvoiceLine.M_Product_ID
		try
		{
			Lookup lookup = MLookupFactory.get (Env.getCtx(), WindowNo,
				AD_Column_ID, DisplayTypeConstants.Search);
			return new VLookup ("M_Product_ID", false, false, true, lookup);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		return null;
	}   //  createProduct

	/**
	 *  Create Optional User Search Lookup
	 *  @param WindowNo window
	 *  @return VLookup
	 */
	public static VLookup createUser (int WindowNo)
	{
		int AD_Column_ID = 10443;    //  AD_WF_Activity.AD_User_UD
		try
		{
			Lookup lookup = MLookupFactory.get (Env.getCtx(), WindowNo,
				AD_Column_ID, DisplayTypeConstants.Search);
			return new VLookup ("AD_User_ID", false, false, true, lookup);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		return null;
	}   //  createProduct


	/*************************************************************************
	 *	Detail Constructor
	 *
	 *  @param columnName column
	 *  @param mandatory mandatory
	 *  @param isReadOnly read only
	 *  @param isUpdateable updateable
	 *  @param lookup lookup
	 */
	public VLookup (String columnName, boolean mandatory, boolean isReadOnly, boolean isUpdateable,
		Lookup lookup)
	{
		super();
		super.setName(columnName);
		m_combo.setName(columnName);
		m_columnName = columnName;
		setMandatory(mandatory);
		m_lookup = lookup;
		//
		setLayout(new BorderLayout());

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
                final Component comp = (Component)e.getSource();
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        m_popupMenu.show(comp, 10, 10);
                    }
                });
            }
        });

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
		//	***	Text & Button	***
		m_text.addActionListener(this);
		m_text.addFocusListener(this);
		m_text.addMouseListener(mouseListener);
		//  Button
		m_button.addActionListener(this);
		m_button.addMouseListener(mouseListener);
		m_button.setFocusable(false);   //  don't focus when tabbing
		m_button.setMargin(new Insets(0, 0, 0, 0));
		if (columnName.equals("C_BPartner_ID")
				|| (columnName.equals("C_BPartner_To_ID") && lookup.getColumnName().equals("C_BPartner.C_BPartner_ID")))
			m_button.setIcon(Env.getImageIcon("BPartner10.gif"));
		else if (columnName.equals("M_Product_ID")
				|| (columnName.equals("M_Product_To_ID") && lookup.getColumnName().equals("M_Product.M_Product_ID")))
			m_button.setIcon(Env.getImageIcon("Product10.gif"));
		else
			m_button.setIcon(Env.getImageIcon("PickOpen10.gif"));

		//	*** VComboBox	***
		if ((m_lookup != null) && (m_lookup.getDisplayType() != DisplayTypeConstants.Search))	//	No Search
		{
			//  Memory Leak after executing the next two lines ??
			m_lookup.fillComboBox (isMandatory(), false, false, false);
			m_combo.setModel(m_lookup);
			//
			m_combo.addActionListener(this);				//	Selection
			m_combo.addMouseListener(mouseListener);		//	popup
			m_combo.getEditor().getEditorComponent().addMouseListener(mouseListener);
			//	FocusListener to refresh selection before opening
			m_combo.addFocusListener(this);
			m_combo.getEditor().getEditorComponent().addFocusListener(this);
		}

		setUI (true);
		//	ReadWrite	-	decides what components to show
		if (isReadOnly || !isUpdateable || (m_lookup == null))
			setReadWrite(false);
		else
			setReadWrite(true);

		//	Popup
		if (m_lookup != null)
		{
			if (((m_lookup.getDisplayType() == DisplayTypeConstants.List) && (Env.getCtx().getAD_Role_ID() == 0))
				|| (m_lookup.getDisplayType() != DisplayTypeConstants.List))     //  only system admins can change lists, so no need to zoom for others
			{
				m_zoom = new CMenuItem(Msg.getMsg(Env.getCtx(), "Zoom"), Env.getImageIcon("Zoom16.gif"));
				m_zoom.addActionListener(this);
				m_popupMenu.add(m_zoom);
			}
			m_refresh = new CMenuItem(Msg.getMsg(Env.getCtx(), "Refresh"), Env.getImageIcon("Refresh16.gif"));
			m_refresh.addActionListener(this);
			m_popupMenu.add(m_refresh);
		}
		
		
		//	VBPartner quick entry link
		if (columnName.equals("C_BPartner_ID") && Env.getCtx().getContext("#EnableCreateUpdateBP").equals("Y"))
		{
			m_bpartnerNew = new CMenuItem (Msg.getMsg(Env.getCtx(), "New"), Env.getImageIcon("InfoBPartner16.gif"));
			m_bpartnerNew.addActionListener(this);
			m_popupMenu.add(m_bpartnerNew);
			m_bpartnerUpdate = new CMenuItem (Msg.getMsg(Env.getCtx(), "Update"), Env.getImageIcon("InfoBPartner16.gif"));
			m_bpartnerUpdate.addActionListener(this);
			m_popupMenu.add(m_bpartnerUpdate);
		}
		//
		if ((m_lookup != null) && (m_lookup.getZoomWindow() == 0))
			m_zoom.setEnabled(false);
	}	//	VLookup

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		m_text = null;
		m_button = null;
		m_lookup = null;
		m_mField = null;
		//
		m_combo.getEditor().getEditorComponent().removeFocusListener(this);
		m_combo.removeFocusListener(this);
		m_combo.removeActionListener(this);
		m_combo.setModel(new DefaultComboBoxModel());    //  remove reference
	//	m_combo.removeAllItems();
		m_combo = null;
	}   //  dispose

	/** Display Length for Lookups (15)         */
	public final static int     DISPLAY_LENGTH = 15;
	/** Field Height (20)				 */
	public static int     		FIELD_HIGHT = 20;

	/** Search: The Editable Text Field         */
	private CTextField 			m_text = new CTextField (DISPLAY_LENGTH);
	/** Search: The Button to open Editor   */
	private CButton				m_button = new CButton();
	/** The Combo Box if not a Search Lookup    */
	private VComboBox			m_combo = new VComboBox();
	/** Indicator that value is being set       */
	private volatile boolean 	m_settingValue = false;
	/** Indicator that docus is being set       */
	private volatile boolean 	m_settingFocus = false;
	/** Indicator - inserting new value			*/
	private volatile boolean	m_inserting = false;
	/** Last Display							*/
	private String				m_lastDisplay = "";
	/** Column Name								*/
	private final String				m_columnName;
	/** Lookup									*/
	private Lookup				m_lookup;
	/** Conbo Box Active						*/
	private boolean				m_comboActive = true;
	/** The Value								*/
	private Object				m_value;
	/** Zoom started - requery values			*/
	private boolean				m_zoomStarted = false;


	//	Popup
	JPopupMenu		 			m_popupMenu = new JPopupMenu();
	private CMenuItem 			m_zoom;
	private CMenuItem 			m_refresh;
	private CMenuItem			m_bpartnerNew;
	private CMenuItem			m_bpartnerUpdate;
	//	Field for Value Preference
	private GridField              m_mField = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VLookup.class);

	/**
	 *  Set Content and Size of Components
	 *  @param initial if true, size and margins will be set
	 */
	private void setUI (boolean initial)
	{
		if (initial)
		{
			Dimension size = m_text.getPreferredSize();
			setPreferredSize(new Dimension(size));  //	causes r/o to be the same length
			m_combo.setPreferredSize(new Dimension(size));
			setMinimumSize(new Dimension (30, size.height));
			FIELD_HIGHT = size.height;
			//
			Dimension bSize = new Dimension(size.height, size.height);
			m_button.setPreferredSize (bSize);
		}

		//	What to show
		this.remove(m_combo);
		this.remove(m_button);
		this.remove(m_text);
		//
		if (!isReadWrite())			//	r/o - show text only
		{
			LookAndFeel.installBorder(this, "TextField.border");
			this.add(m_text, BorderLayout.CENTER);
			m_text.setReadWrite(false);
			m_combo.setReadWrite(false);
			m_comboActive = false;
		}
	    //	show combo if not Search
		else if ((m_lookup != null) && (m_lookup.getDisplayType() != DisplayTypeConstants.Search))
		{
			this.add(m_combo, BorderLayout.CENTER);
			m_comboActive = true;
		}
		else 						//	Search or unstable - show text & button
		{
			LookAndFeel.installBorder(this, "TextField.border");
			this.add(m_text, BorderLayout.CENTER);
			this.add(m_button, BorderLayout.EAST);
			m_text.setReadWrite (true);
			m_comboActive = false;
		}

		this.setBorder(null);
	}   //  setUI

	/**
	 *	Set ReadWrite
	 *  @param value ReadWrite
	 */
	public void setReadWrite (boolean value)
	{
		boolean rw = value;
		if (m_lookup == null)
			rw = false;
		if (m_combo.isReadWrite() != value)
		{
			m_combo.setReadWrite(rw);
			setUI (false);
			if (m_comboActive)
				setValue (m_value);
		}
	}	//	setReadWrite

	/**
	 * 	Set enabled
	 * 	@param enabled enabled
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		setReadWrite(enabled);
	    super.setEnabled(enabled);
	}	//	setEnabled

	/**
	 *	IsEditable
	 *  @return is lookup ReadWrite
	 */
	public boolean isReadWrite()
	{
		if (m_combo != null)
			return m_combo.isReadWrite();
		return true;
	}	//	isReadWrite

	/**
	 *	Set Mandatory (and back color)
	 *  @param mandatory mandatory
	 */
	public void setMandatory (boolean mandatory)
	{
		m_combo.setMandatory(mandatory);
		m_text.setMandatory(mandatory);
	}	//	setMandatory

	/**
	 *	Is it mandatory
	 *  @return true if mandatory
	 */
	public boolean isMandatory()
	{
		return m_combo.isMandatory();
	}	//	isMandatory

	/**
	 *	Set Background
	 *  @param color color
	 */
	@Override
	public void setBackground(Color color)
	{
		m_text.setBackground(color);
		m_combo.setBackground(color);
	}	//	setBackground

	/**
	 *	Set Background
	 *  @param error error
	 */
	public void setBackground (boolean error)
	{
		m_text.setBackground(error);
		m_combo.setBackground(error);
	}	//	setBackground

	/**
	 *  Set Foreground
	 *  @param fg Foreground color
	 */
	@Override
	public void setForeground(Color fg)
	{
		m_text.setForeground(fg);
		m_combo.setForeground(fg);
	}   //  setForeground

	/**
	 * 	Request Focus
	 */
	@Override
	public void requestFocus()
	{
		if ((m_lookup != null) && (m_lookup.getDisplayType() != DisplayTypeConstants.Search))
			m_combo.requestFocus();
		else
			m_text.requestFocus();
	}	//	requestFocus

	/**
	 * 	Request Focus In Window
	 *	@return focus request
	 */
	@Override
	public boolean requestFocusInWindow()
	{
		if ((m_lookup != null) && (m_lookup.getDisplayType() != DisplayTypeConstants.Search))
			return m_combo.requestFocusInWindow();
		return m_text.requestFocusInWindow();
	}	//	requestFocusInWindow

	/**
	 * 	Get Focus Component
	 *	@return component
	 */
	public Component getFocusableComponent()
	{
		if ((m_lookup != null) && (m_lookup.getDisplayType() != DisplayTypeConstants.Search))
			return m_combo;
		return m_text;
	}	//	getFocusComponent

	/**
	 *  Set Editor to value
	 *  @param value new Value
	 */
	public void setValue (Object value)
	{
		log.fine(m_columnName + "=" + value);
		m_settingValue = true;		//	disable actions
		m_value = value;

		//	Set both for switching
		m_combo.setValue (value);
		if (value == null)
		{
			m_text.setText (null);
			m_lastDisplay = "";
			m_settingValue = false;
			return;
		}
		if (m_lookup == null)
		{
			m_text.setText (value.toString());
			m_lastDisplay = value.toString();
			m_settingValue = false;
			return;
		}

		//	Set Display
		if (m_zoomStarted)	//	assume new value
			m_lookup.removeAllElements();
		m_zoomStarted = false;
		m_lastDisplay = m_lookup.getDisplay(value);
		if (m_lastDisplay.equals("<-1>"))
		{
			m_lastDisplay = "";
			m_value = null;
		}
		boolean notFound = m_lastDisplay.startsWith("<") && m_lastDisplay.endsWith(">");
		m_text.setText (m_lastDisplay);
		m_text.setCaretPosition (0); //	show beginning

		//	Nothing showing in Combo and should be showing
		if ((m_combo.getSelectedItem() == null)
			&& (m_comboActive || (m_inserting && (m_lookup.getDisplayType() != DisplayTypeConstants.Search))))
		{
			//  lookup found nothing too
			if (notFound)
			{
				log.finest(m_columnName + "=" + value + ": Not found - " + m_lastDisplay);
				//  we may have a new value
				m_lookup.refresh();
				m_combo.setValue (value);
				m_lastDisplay = m_lookup.getDisplay(value);
				m_text.setText (m_lastDisplay);
				m_text.setCaretPosition (0);	//	show beginning
				notFound = m_lastDisplay.startsWith("<") && m_lastDisplay.endsWith(">");
			}
			if (notFound)	//	<key>
			{
				m_value = null;
				log.fine(m_columnName + "=" + value + ": Not found - set to null");
				actionCombo (null);             //  data binding
			}
			//  we have lookup
			else if (m_combo.getSelectedItem() == null)
			{
				NamePair pp = m_lookup.get(value);
				if (pp != null)
				{
					log.fine(m_columnName + ": added to combo = " + pp.toStringX());
					//  Add to Combo
					m_combo.addItem (pp);
					m_combo.setValue (value);
				}
			}
			//  Not in Lookup - set to Null
			if ((m_combo.getSelectedItem() == null) && (m_value != null))
			{
				log.info(m_columnName + "=" + value + ": not in Lookup - set to NULL");
				actionCombo (null);             //  data binding (calls setValue again)
				m_value = null;
			}
		}
		m_settingValue = false;
	}	//	setValue

	/**
	 *  Property Change Listener
	 *  @param evt PropertyChangeEvent
	 */
	public void propertyChange (PropertyChangeEvent evt)
	{
	//	log.fine( "VLookup.propertyChange", evt);
		if (evt.getPropertyName().equals(GridField.PROPERTY))
		{
			m_inserting = GridField.INSERTING.equals(evt.getOldValue());	//	MField.setValue
			setValue(evt.getNewValue());
			m_inserting = false;
		}
	}   //  propertyChange

	/**
	 *	Return Editor value (Integer)
	 *  @return value
	 */
	public Object getValue()
	{
		if (m_comboActive)
			return m_combo.getValue ();
		return m_value;
	}	//	getValue

	/**
	 *  Return editor display
	 *  @return display value
	 */
	public String getDisplay()
	{
		String retValue = null;
		if (m_comboActive)
			retValue = m_combo.getDisplay();
		//  check lookup
		else if (m_lookup == null)
			retValue = m_value == null ? null : m_value.toString();
		else
			retValue = m_lookup.getDisplay(m_value);
	//	log.fine( "VLookup.getDisplay - " + retValue, "ComboActive=" + m_comboActive);
		return retValue;
	}   //  getDisplay

	/**
	 *  Set Field/WindowNo for ValuePreference
	 *  @param mField Model Field for Lookup
	 */
	public void setField (GridField mField)
	{
		m_mField = mField;
		if ((m_mField != null)
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


	/**************************************************************************
	 *	Action Listener	- data binding
	 *  @param e ActionEvent
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (m_settingValue || m_settingFocus)
			return;
		log.config(m_columnName + " - " + e.getActionCommand() + ", ComboValue=" + m_combo.getSelectedItem());
	//	log.fine("Hash=" + this.hashCode());

		//  Preference
		if (e.getActionCommand().equals(ValuePreference.NAME))
		{
			if (MRole.getDefault().isShowPreference())
				ValuePreference.start (m_mField, getValue(), getDisplay());
			return;
		}

		//  Combo Selection
		else if (e.getSource() == m_combo)
		{
			Object value = getValue();
			Object o = m_combo.getSelectedItem();
			if (o != null)
			{
				String s = o.toString();
				//  don't allow selection of inactive
				if (s.startsWith(MLookup.INACTIVE_S) && s.endsWith(MLookup.INACTIVE_E))
				{
					log.info(m_columnName + " - selection inactive set to NULL");
					value = null;
				}
			}
			actionCombo (value);                //  data binding
		}
		//  Button pressed
		else if (e.getSource() == m_button)
			actionButton ("");
		//  Text entered
		else if (e.getSource() == m_text)
			actionText();

		//  Popup Menu
		else if (e.getSource() == m_zoom)
			actionZoom(m_combo.getSelectedItem());
		else if (e.getSource() == m_refresh)
			actionRefresh();
		else if (e.getSource() == m_bpartnerNew)
			actionBPartner(true);
		else if (e.getSource() == m_bpartnerUpdate)
			actionBPartner(false);
	}	//	actionPerformed

	/**
	 *  Action Listener Interface
	 *  @param listener listener
	 */
	public void addActionListener(ActionListener listener)
	{
		m_combo.addActionListener(listener);
		m_text.addActionListener(listener);
	}   //  addActionListener

	/**
	 *  Action Listener Interface
	 *  @param listener
	 */
	public void removeActionListener(ActionListener listener)
	{
		m_combo.removeActionListener (listener);
		m_text.removeActionListener(listener);
	}   //  removeActionListener

	/**
	 *	Action - Combo.
	 *  <br>
	 *	== dataBinding == inform of new value
	 *  <pre>
	 *  VLookup.actionCombo
	 *      GridController.vetoableChange
	 *          GridTable.setValueAt
	 *              GridField.setValue
	 *                  VLookup.setValue
	 *          GridTab.dataStatusChanged
	 *  </pre>
	 *  @param value new value
	 */
	private void actionCombo (Object value)
	{
		log.fine("Value=" + value);
		try
		{
			// -> GridController.vetoableChange
			fireVetoableChange (m_columnName, null, value);
		}
		catch (PropertyVetoException pve)
		{
			log.log(Level.SEVERE, m_columnName, pve);
		}
		//  is the value updated ?
		boolean updated = false;
		if ((value == null) && (m_value == null))
			updated = true;
		else if ((value != null) && value.equals(m_value))
			updated = true;
		if (!updated)
		{
			//  happens if VLookup is used outside of APanel/GridController (no property listener)
			log.fine(m_columnName + " - Value explicitly set - new=" + value + ", old=" + m_value);
			setValue(value);
		}
	}	//	actionCombo


	/**
	 *	Action - Button.
	 *	- Call Info
	 *	@param queryValue initial query value
	 */
	private void actionButton (String queryValue)
	{
		m_button.setEnabled(false);                 //  disable double click
		if (m_lookup == null)
			return;		//	leave button disabled
		m_text.requestFocus();						//  closes other editors
		Frame frame = Env.getFrame(this);

		/**
		 *  Three return options:
		 *  - Value Selected & OK pressed   => store result => result has value
		 *  - Cancel pressed                => store null   => result == null && cancelled
		 *  - Window closed                 -> ignore       => result == null && !cancalled
		 */
		Object result = null;
		boolean cancelled = false;
		//
		String col = m_lookup.getColumnName();		//	fully qualified name
		if (col.indexOf(".") != -1)
			col = col.substring(col.indexOf(".")+1);
		//  Zoom / Validation
		String whereClause = getWhereClause( Env.getCtx(), m_columnName, m_lookup );
		//
		log.fine(col
			+ ", Zoom=" + m_lookup.getZoomWindow()
			+ " (" + whereClause + ")");
		//
		Ctx ctx = Env.getCtx ();
		boolean resetValue = false;	//	reset value so that is always treated as new entry
		if (col.equals("M_Product_ID"))
		{
			//	Reset
			ctx.setContext(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_Product_ID", "0");
			ctx.setContext(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_AttributeSetInstance_ID", "0");
			ctx.setContext(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_Lookup_ID", "0");
			//  Replace Value with name if no value exists
			if ((queryValue.length() == 0) && (m_text.getText().length() > 0))
				queryValue = "@" + m_text.getText() + "@";   //  Name indicator - otherwise Value
			int M_Warehouse_ID = Env.getCtx().getContextAsInt( m_lookup.getWindowNo(), "M_Warehouse_ID");
			int M_PriceList_ID = Env.getCtx().getContextAsInt( m_lookup.getWindowNo(), "M_PriceList_ID");
			//	Show Info
			InfoProduct ip = new InfoProduct (frame, true, m_lookup.getWindowNo(),
				M_Warehouse_ID, M_PriceList_ID, queryValue, false, whereClause);
			ip.setVisible(true);
			cancelled = ip.isCancelled();
			result = ip.getSelectedKey();
			resetValue = true;
		}
		else if (col.equals("C_BPartner_ID"))
		{
			//  Replace Value with name if no value exists
			if ((queryValue.length() == 0) && (m_text.getText().length() > 0))
				queryValue = m_text.getText();
			boolean isSOTrx = ctx.isSOTrx(m_lookup.getWindowNo());
			InfoBPartner ip = new InfoBPartner (frame, true, m_lookup.getWindowNo(),
				queryValue, isSOTrx, false, whereClause);
			ip.setVisible(true);
			cancelled = ip.isCancelled();
			result = ip.getSelectedKey();
		}
		else	//	General Info
		{
			if (m_tableName == null)	//	sets table name & key column
				getDirectAccessSQL("*");
			Info ig = Info.create (frame, true, m_lookup.getWindowNo(),
				m_tableName, m_keyColumnName, queryValue, false, whereClause);
			ig.setVisible(true);
			cancelled = ig.isCancelled();
			result = ig.getSelectedKey();
		}

		//  Result
		if (result != null)
		{
			log.config(m_columnName + " - Result = " + result.toString() + " (" + result.getClass().getName() + ")");
			//  make sure that value is in cache
			m_lookup.getDirect(result, false, true);
			if (resetValue)
				actionCombo (null);
			actionCombo (result);	//	data binding
		}
		else if (cancelled)
		{
			log.config(m_columnName + " - Result = null (cancelled)");
			actionCombo(null);
		}
		else
		{
			log.config(m_columnName + " - Result = null (not cancelled)");
			setValue(m_value);      //  to re-display value
		}
		//
		m_button.setEnabled(true);
		m_text.requestFocus();
	}	//	actionButton

	/**
	 * 	Get Where Clause
	 * 	@param ctx context
	 * 	@param columnName column name
	 * 	@param lookup lookup
	 *	@return where clause or ""
	 */
	public static String getWhereClause(Ctx ctx, String columnName, Lookup lookup)
	{
		String whereClause = "";
		if (lookup == null)
			return "";
		if (lookup.getZoomQuery() != null)
			whereClause = lookup.getZoomQuery().getWhereClause();
		String validation = lookup.getValidation();
		if (validation == null)
			validation = "";
		if (whereClause.length() == 0)
			whereClause = validation;
		else if (validation.length() > 0)
			whereClause += " AND " + validation;
	//	log.finest("ZoomQuery=" + (m_lookup.getZoomQuery()==null ? "" : m_lookup.getZoomQuery().getWhereClause())
	//		+ ", Validation=" + m_lookup.getValidation());
		if (whereClause.indexOf('@') != -1)
		{
			String validated = Env.parseContext( ctx, lookup.getWindowNo(), whereClause, false);
			if (validated.length() == 0)
				log.severe(columnName + " - Cannot Parse=" + whereClause);
			else
			{
				log.fine(columnName + " - Parsed: " + validated);
				return validated;
			}
		}
		return whereClause;
	}	//	getWhereClause

	/**
	 *	Check, if data returns unique entry, otherwise involve Info via Button
	 */
	private void actionText()
	{
		String text = m_text.getText();
		//	Nothing entered
		if ((text == null) || (text.length() == 0) || text.equals("%"))
		{
			actionButton(text);
			return;
		}
		text = text.toUpperCase();
		log.config(m_columnName + " - " + text);

		//	Exact first
		PreparedStatement pstmt = null;
		String finalSQL = Msg.parseTranslation(Env.getCtx(), getDirectAccessSQL(text));
		int id = -3;
		/*
		 try
		{
			pstmt = DB.prepareStatement(finalSQL, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				id = rs.getInt(1);		//	first
				if (rs.next())
					id = -1;			//	only if unique
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, finalSQL, e);
			id = -2;
		}
		*/
		//	Try like
		if ((id == -3) && !text.endsWith("%"))
		if (!text.endsWith("%"))
		{
			text += "%";
			finalSQL = Msg.parseTranslation(Env.getCtx(), getDirectAccessSQL(text));
			try
			{
				pstmt = DB.prepareStatement(finalSQL, (Trx) null);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
				{
					id = rs.getInt(1);		//	first
					if (rs.next())
						id = -1;			//	only if unique
				}
				rs.close();
				pstmt.close();
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, finalSQL, e);
				id = -2;
			}
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
		}
		catch (Exception e)
		{
		}

		//	No (unique) result
		if (id <= 0)
		{
			if (id == -3)
				log.fine(m_columnName + " - Not Found - " + finalSQL);
			else
				log.fine(m_columnName + " - Not Unique - " + finalSQL);
			m_value = null;	// force re-display
			actionButton(m_text.getText());
			return;
		}
		log.fine(m_columnName + " - Unique ID=" + id);
		m_value = null;     //  forces re-display if value is unchanged but text updated and still unique
		actionCombo (Integer.valueOf(id));          //  data binding
		m_text.requestFocus();
	}	//	actionText


	private String		m_tableName = null;
	private String		m_keyColumnName = null;

	/**
	 * 	Generate Access SQL for Search.
	 * 	The SQL returns the ID of the value entered
	 * 	Also sets m_tableName and m_keyColumnName
	 *	@param text uppercase text for LIKE comparison
	 *	@return sql or ""
	 *  Example
	 *	SELECT C_Payment_ID FROM C_Payment WHERE UPPER(DocumentNo) LIKE x OR ...
	 */
	private String getDirectAccessSQL (String text)
	{
		String[] result = getDirectAccessSQL(Env.getCtx(), m_columnName, m_lookup, text);
		m_tableName = result[1];
		m_keyColumnName = result[2];
		return result[0];
	}


	/**
	 *
	 * @param ctx
	 * @param m_columnName
	 * @param m_lookup
	 * @param text
	 * @return An array of 3 Strings; 0=SQL, 1=m_tableName, 2=m_keyColumnName
	 */
	public static String[] getDirectAccessSQL( Ctx ctx, String m_columnName, Lookup m_lookup, String text )
	{
		StringBuffer sql = new StringBuffer();
		String m_tableName = m_columnName.substring(0, m_columnName.length()-3);
		String m_keyColumnName = m_columnName;
		//
		if (m_columnName.equals("M_Product_ID"))
		{
			//	Reset
			ctx.setContext(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_Product_ID", "0");
			ctx.setContext(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_AttributeSetInstance_ID", "0");
			ctx.setContext(EnvConstants.WINDOW_INFO, EnvConstants.TAB_INFO, "M_Locator_ID", "0");
			//
			sql.append("SELECT M_Product_ID FROM M_Product WHERE (UPPER(Value) LIKE ")
				.append(DB.TO_STRING(text))
				.append(" OR UPPER(Name) LIKE ").append(DB.TO_STRING(text))
				.append(" OR UPC LIKE ").append(DB.TO_STRING(text))
				.append(" OR UPPER(M_Product.SKU) LIKE ").append(DB.TO_STRING(text))
				.append(" OR EXISTS (SELECT 1 FROM M_Product_PO po WHERE ")
							.append(" M_Product.M_Product_ID=po.M_Product_ID AND" )
							.append(" (UPPER(po.UPC) LIKE ").append(DB.TO_STRING(text))
							.append(" OR UPPER(po.VendorProductNo) LIKE ")
							.append(DB.TO_STRING(text)).append("))")
				.append( " OR EXISTS (SELECT 1 FROM C_BPartner_Product bp WHERE ")
							.append(" M_Product.M_Product_ID=bp.M_Product_ID AND")
							.append(" UPPER(bp.VendorProductNo) LIKE ")
							.append(DB.TO_STRING(text)).append(")").append(")");
		}
		else if (m_columnName.equals("C_BPartner_ID"))
		{
			sql.append("SELECT C_BPartner_ID FROM C_BPartner WHERE (UPPER(Value) LIKE ")
				.append(DB.TO_STRING(text))
				.append(" OR UPPER(Name) LIKE ").append(DB.TO_STRING(text)).append(")");
		}
		else if (m_columnName.equals("C_Order_ID"))
		{
			sql.append("SELECT C_Order_ID FROM C_Order WHERE UPPER(DocumentNo) LIKE ")
				.append(DB.TO_STRING(text));
		}
		else if (m_columnName.equals("C_Invoice_ID"))
		{
			sql.append("SELECT C_Invoice_ID FROM C_Invoice WHERE UPPER(DocumentNo) LIKE ")
				.append(DB.TO_STRING(text));
		}
		else if (m_columnName.equals("M_InOut_ID"))
		{
			sql.append("SELECT M_InOut_ID FROM M_InOut WHERE UPPER(DocumentNo) LIKE ")
				.append(DB.TO_STRING(text));
		}
		else if (m_columnName.equals("C_Payment_ID"))
		{
			sql.append("SELECT C_Payment_ID FROM C_Payment WHERE UPPER(DocumentNo) LIKE ")
				.append(DB.TO_STRING(text));
		}
		else if (m_columnName.equals("GL_JournalBatch_ID"))
		{
			sql.append("SELECT GL_JournalBatch_ID FROM GL_JournalBatch WHERE UPPER(DocumentNo) LIKE ")
				.append(DB.TO_STRING(text));
		}
		else if (m_columnName.equals("SalesRep_ID"))
		{
			sql.append("SELECT AD_User_ID FROM AD_User WHERE UPPER(Name) LIKE ")
				.append(DB.TO_STRING(text));
			m_tableName = "AD_User";
			m_keyColumnName = "AD_User_ID";
		}
		//	Predefined
		if (sql.length() > 0)
		{
			String wc = getWhereClause( ctx, m_columnName, m_lookup );
			if ((wc != null) && (wc.length() > 0))
				sql.append(" AND ").append(wc);
			sql.append(" AND IsActive='Y'");
			//	***
			log.finest(m_columnName + " (predefined) " + sql.toString());
			return new String[] {
					MRole.getDefault().addAccessSQL( sql.toString(), m_tableName, MRole.SQL_NOTQUALIFIED, MRole.SQL_RO ),
					m_tableName, m_keyColumnName };
		}

		//	Check if it is a Table Reference
		if ((m_lookup != null) && (m_lookup instanceof MLookup))
		{
			int AD_Reference_ID = ((MLookup)m_lookup).getAD_Reference_Value_ID();
			if (AD_Reference_ID != 0)
			{
				String query = "SELECT kc.ColumnName, dc.ColumnName, t.TableName "
					+ "FROM AD_Ref_Table rt"
					+ " INNER JOIN AD_Column kc ON (rt.Column_Key_ID=kc.AD_Column_ID)"
					+ " INNER JOIN AD_Column dc ON (rt.Column_Display_ID=dc.AD_Column_ID)"
					+ " INNER JOIN AD_Table t ON (rt.AD_Table_ID=t.AD_Table_ID) "
					+ "WHERE rt.AD_Reference_ID=?";
				String displayColumnName = null;
				PreparedStatement pstmt = null;
				try
				{
					pstmt = DB.prepareStatement(query, (Trx) null);
					pstmt.setInt(1, AD_Reference_ID);
					ResultSet rs = pstmt.executeQuery();
					if (rs.next())
					{
						m_keyColumnName = rs.getString(1);
						displayColumnName = rs.getString(2);
						m_tableName = rs.getString(3);
					}
					rs.close();
					pstmt.close();
					pstmt = null;
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, query, e);
				}
				try
				{
					if (pstmt != null)
						pstmt.close();
					pstmt = null;
				}
				catch (Exception e)
				{
					pstmt = null;
				}
				if (displayColumnName != null)
				{
					sql = new StringBuffer();
					sql.append("SELECT ").append(m_keyColumnName)
						.append(" FROM ").append(m_tableName)
						.append(" WHERE UPPER(").append(displayColumnName)
						.append(") LIKE ").append(DB.TO_STRING(text))
						.append(" AND IsActive='Y'");
					String wc = getWhereClause( ctx, m_columnName, m_lookup );
					if ((wc != null) && (wc.length() > 0))
						sql.append(" AND ").append(wc);
					//	***
					log.finest(m_columnName + " (Table) " + sql.toString());
					return new String[] {
							MRole.getDefault(ctx, false).addAccessSQL( sql.toString(), m_tableName, MRole.SQL_NOTQUALIFIED, MRole.SQL_RO ),
							m_tableName, m_keyColumnName };
				}
			}	//	Table Reference
		}	//	MLookup

		/** Check Well Known Columns of Table - assumes TableDir	**/
		String query = "SELECT t.TableName, c.ColumnName "
			+ "FROM AD_Column c "
			+ " INNER JOIN AD_Table t ON (c.AD_Table_ID=t.AD_Table_ID AND t.IsView='N') "
			+ "WHERE (c.ColumnName IN ('DocumentNo', 'Value', 'Name') OR c.IsIdentifier='Y')"
			+ " AND c.AD_Reference_ID IN (10,14)"
			+ " AND EXISTS (SELECT * FROM AD_Column cc WHERE cc.AD_Table_ID=t.AD_Table_ID"
				+ " AND cc.IsKey='Y' AND cc.ColumnName=?)";
		m_keyColumnName = m_columnName;
		sql = new StringBuffer();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(query, (Trx) null);
			pstmt.setString(1, m_keyColumnName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				if (sql.length() != 0)
					sql.append(" OR ");
				m_tableName = rs.getString(1);
				sql.append("UPPER(").append(rs.getString(2)).append(") LIKE ").append(DB.TO_STRING(text));
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, query, ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		//
		if (sql.length() == 0)
		{
			log.log(Level.SEVERE, m_columnName + " (TableDir) - no standard/identifier columns");
			return new String[] { "", m_tableName, m_keyColumnName };
		}
		//
		StringBuffer retValue = new StringBuffer ("SELECT ")
			.append(m_columnName).append(" FROM ").append(m_tableName)
			.append(" WHERE ").append(sql)
			.append(" AND IsActive='Y'");
		String wc = getWhereClause( ctx, m_columnName, m_lookup );
		if ((wc != null) && (wc.length() > 0))
			retValue.append(" AND ").append(wc);
		//	***
		log.finest(m_columnName + " (TableDir) " + sql.toString());
		return new String[] {
				MRole.getDefault(ctx, false).addAccessSQL( retValue.toString(), m_tableName, MRole.SQL_NOTQUALIFIED, MRole.SQL_RO ),
				m_tableName, m_keyColumnName };
	}	//	getDirectAccessSQL


	/**
	 *	Action - Special BPartner Screen
	 *  @param newRecord true if new record should be created
	 */
	private void actionBPartner (boolean newRecord)
	{
		VBPartner vbp = new VBPartner (Env.getFrame(this), m_lookup.getWindowNo());
		int BPartner_ID = 0;
		//  if update, get current value
		if (!newRecord)
		{
			if (m_value instanceof Integer)
				BPartner_ID = ((Integer)m_value).intValue();
			else if (m_value != null)
				BPartner_ID = Integer.parseInt(m_value.toString());
		}

		vbp.loadBPartner (BPartner_ID);
		vbp.setVisible(true);
		//  get result
		int result = vbp.getC_BPartner_ID();
		
		// clear the lookup to force re-query after update
		m_lookup.clear();
		if ((result == 0					//	0 = not saved
)
			&& (result == BPartner_ID))	//	the same
			return;
		//  Maybe new BPartner - put in cache
		m_value = null;     //  forces re-display if value is unchanged but text updated and still unique
		m_lookup.getDirect(Integer.valueOf(result), false, true);

		actionCombo (Integer.valueOf(result));      //  data binding
	}	//	actionBPartner

	/**
	 *	Action - Zoom
	 *	@param selectedItem item
	 */
	private void actionZoom (Object selectedItem)
	{
		if (m_lookup == null)
			return;
		//
		Query zoomQuery = m_lookup.getZoomQuery();
		Object value = getValue();
		if (value == null)
			value = selectedItem;
		//	If not already exist or exact value
		if ((zoomQuery == null) || (value != null))
		{
			zoomQuery = new Query();	//	ColumnName might be changed in MTab.validateQuery

			String keyColumnName = null;
			//	Check if it is a Table Reference
			if ((m_lookup != null) && (m_lookup instanceof MLookup))
			{
				int AD_Reference_ID = ((MLookup)m_lookup).getAD_Reference_Value_ID();
				if (AD_Reference_ID != 0)
				{
					String query = "SELECT kc.ColumnName"
						+ " FROM AD_Ref_Table rt"
						+ " INNER JOIN AD_Column kc ON (rt.Column_Key_ID=kc.AD_Column_ID)"
						+ "WHERE rt.AD_Reference_ID=?";

					PreparedStatement pstmt = null;
					try
					{
						pstmt = DB.prepareStatement(query, (Trx) null);
						pstmt.setInt(1, AD_Reference_ID);
						ResultSet rs = pstmt.executeQuery();
						if (rs.next())
						{
							keyColumnName = rs.getString(1);
						}
						rs.close();
						pstmt.close();
						pstmt = null;
					}
					catch (Exception e)
					{
						log.log(Level.SEVERE, query, e);
					}
					try
					{
						if (pstmt != null)
							pstmt.close();
						pstmt = null;
					}
					catch (Exception e)
					{
						pstmt = null;
					}
				}	//	Table Reference
			}	//	MLookup

			if((keyColumnName != null) && (keyColumnName.length() !=0))
				zoomQuery.addRestriction(keyColumnName, Query.EQUAL, value);
			else
				zoomQuery.addRestriction(m_columnName, Query.EQUAL, value);
			zoomQuery.setRecordCount(1);	//	guess
		}

		int AD_Window_ID = m_lookup.getZoomWindow(zoomQuery);
		//
		log.info(m_columnName + " - AD_Window_ID=" + AD_Window_ID
			+ " - Query=" + zoomQuery + " - Value=" + value);
		//
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		//
		AWindow frame = new AWindow();
		if (!frame.initWindow(AD_Window_ID, zoomQuery))
		{
			/*sraval: sourceforge bug 1763808: commented section below to eliminate duplicate error messages in case user role does now have access to zoom to destination window
			setCursor(Cursor.getDefaultCursor());
			ValueNamePair pp = CLogger.retrieveError();
			String msg = pp==null ? "AccessTableNoView" : pp.getValue();
			ADialog.error(m_lookup.getWindowNo(), this, msg, pp==null ? "" : pp.getName());
			*/
		}
		else
			AEnv.showCenterScreen(frame);
			//  async window - not able to get feedback
		frame = null;
		//
		m_zoomStarted = true;
		setCursor(Cursor.getDefaultCursor());
	}	//	actionZoom

	/**
	 *	Action - Refresh
	 */
	private void actionRefresh()
	{
		if (m_lookup == null)
			return;
		//
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		//
		Object obj = m_combo.getSelectedItem();
		log.info(m_columnName + " #" + m_lookup.getSize() + ", Selected=" + obj);
		m_lookup.refresh();
		if (m_lookup.isValidated())
			m_lookup.fillComboBox(isMandatory(), false, false, false);
		else
			m_lookup.fillComboBox(isMandatory(), true, false, false);
		m_combo.setSelectedItem(obj);
	//	m_combo.revalidate();
		//
		setCursor(Cursor.getDefaultCursor());
		log.info(m_columnName + " #" + m_lookup.getSize() + ", Selected=" + m_combo.getSelectedItem());
	}	//	actionRefresh



	/**************************************************************************
	 *	Focus Listener for ComboBoxes with missing Validation or invalid entries
	 *	- Requery listener for updated list
	 *  @param e FocusEvent
	 */
	public void focusGained (FocusEvent e)
	{
		if (e.isTemporary() || (m_lookup == null))
			return;
		if ((e.getComponent() == m_combo) || !SwingUtilities.isDescendingFrom(e.getComponent(), m_combo))
			return;
		if (m_lookup.isValidated() && !m_lookup.hasInactive())
			return;
		if ((m_combo != null) && !m_combo.isReadWrite())
			return;

		//  prevents actionPerformed
		m_settingFocus = true;

		Object obj = m_lookup.getSelectedItem();
		log.config(m_columnName
			+ " - Start    Count=" + m_combo.getItemCount() + ", Selected=" + obj);

		m_lookup.fillComboBox(isMandatory(), true, true, true);     //  only validated & active & temporary
		log.fine(m_columnName
			+ " - Update   Count=" + m_combo.getItemCount() + ", Selected=" + m_lookup.getSelectedItem());
		m_lookup.setSelectedItem(obj);
		obj = m_lookup.getSelectedItem();
		log.fine(m_columnName
			+ " - Selected Count=" + m_combo.getItemCount() + ", Selected=" + obj);
		m_settingFocus = false;
	}	//	focusGained

	/**
	 *	Reset Selection List
	 *  @param e FocusEvent
	 */
	public void focusLost(FocusEvent e)
	{
	//	log.config(m_columnName + " " + m_columnName + " = " + m_value + " - " + e);
		if (e.isTemporary()
			|| (m_lookup == null)
			|| !m_button.isEnabled() )	//	set by actionButton
			return;
		//	Text Lost focus
		if (e.getSource() == m_text)
		{
			String text = m_text.getText();
			log.config(m_columnName + " (Text) " + m_columnName + " = " + m_value + " - " + text);
			//	Skip if empty
			if (((m_value == null)
				&& (m_text.getText().length() == 0)))
				return;
			if (m_lastDisplay.equals(text))
				return;
			//
			actionText();	//	re-display
			return;
		}
		//	Combo lost focus
		Object src = e.getSource();
		if (!((src == m_combo) || (src == m_combo.getEditor().getEditorComponent())))
			return;
		if (m_lookup.isValidated() && !m_lookup.hasInactive())
			return;
		//
		m_settingFocus = true;  //  prevents actionPerformed
		//
		log.config(m_columnName + " = " + m_combo.getSelectedItem());
		Object obj = m_combo.getSelectedItem();
		//	set original model
		if (!m_lookup.isValidated())
			m_lookup.fillComboBox(true);    //  previous selection
		//	Set value
		if (obj != null)
		{
			m_combo.setSelectedItem(obj);
			//	original model may not have item
			if (!m_combo.getSelectedItem().equals(obj))
			{
				log.fine(m_columnName + " - added to combo - " + obj);
				m_combo.addItem(obj);
				m_combo.setSelectedItem(obj);
			}
		}
		actionCombo(getValue());	// should not be required if movements do not fire value
		m_settingFocus = false;
	}	//	focusLost

	/**
	 *  Set ToolTip
	 *  @param text tool tip text
	 */
	@Override
	public void setToolTipText(String text)
	{
		super.setToolTipText(text);
		m_button.setToolTipText(text);
		m_text.setToolTipText(text);
		m_combo.setToolTipText(text);
	}   //  setToolTipText

	/**
	 * 	Refresh Query
	 *	@return count
	 */
	public int refresh()
	{
		if (m_lookup == null)
			return -1;
		return m_lookup.refresh();
	}	//	refresh

	/**
	 * 	Disable Validation
	 */
	public void disableValisation()
	{
		if (m_lookup != null)
			m_lookup.disableValidation();
	}	//	disableValidation
}	//	VLookup
