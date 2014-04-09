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
 * Product Attribute Set Instance Editor
 * 
 * @author Jorg Janke
 * @version $Id: VPAttribute.java,v 1.2 2006/07/30 00:51:27 jjanke Exp $
 */
public class VPAttribute extends JComponent implements VEditor, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * IDE Constructor
	 */
	public VPAttribute() {
		this(false, false, true, 0, null);
	} // VAssigment

	/**
	 * Create Product Attribute Set Instance Editor.
	 * 
	 * @param mandatory
	 *            mandatory
	 * @param isReadOnly
	 *            read only
	 * @param isUpdateable
	 *            updateable
	 * @param WindowNo
	 *            WindowNo
	 * @param lookup
	 *            Model Product Attribute
	 */
	public VPAttribute(boolean mandatory, boolean isReadOnly,
			boolean isUpdateable, int WindowNo, MPAttributeLookup lookup) {
		super.setName("M_AttributeSetInstance_ID");
		m_WindowNo = WindowNo;
		m_mPAttribute = lookup;
		m_C_BPartner_ID = Env.getCtx().getContextAsInt(WindowNo,
				"C_BPartner_ID");
		LookAndFeel.installBorder(this, "TextField.border");
		this.setLayout(new BorderLayout());
		// Size
		this.setPreferredSize(m_text.getPreferredSize());
		int height = m_text.getPreferredSize().height;

		// *** Text ***
		m_text.setEditable(false);
		m_text.setFocusable(false);
		m_text.setHorizontalAlignment(SwingConstants.LEADING);
		// Background
		setMandatory(mandatory);
		this.add(m_text, BorderLayout.CENTER);

		// *** Button ***
		m_button.setIcon(Env.getImageIcon("PAttribute10.gif"));
		m_button.setPreferredSize(new Dimension(height, height));
		m_button.addActionListener(this);
		m_button.setFocusable(true);
		this.add(m_button, BorderLayout.EAST);

		setBorder(null);

		// Prefereed Size
		this.setPreferredSize(this.getPreferredSize()); // causes r/o to be the
														// same length
		// ReadWrite
		if (isReadOnly || !isUpdateable)
			setReadWrite(false);
		else
			setReadWrite(true);

		// Popup
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e))
					m_popupMenu.show((Component) e.getSource(), e.getX(), e
							.getY());
			}
		});

		String actionKey = getClass().getName() + "_popop";
		InputMap iMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,
				InputEvent.CTRL_MASK);
		iMap.put(ks, actionKey);
		getActionMap().put(actionKey, new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				Component comp = (Component) e.getSource();
				m_popupMenu.show(comp, 10, 10);
			}
		});

		menuEditor = new CMenuItem(Msg.getMsg(Env.getCtx(), "PAttribute"), Env
				.getImageIcon("Zoom16.gif"));
		menuEditor.addActionListener(this);
		m_popupMenu.add(menuEditor);
	} // VPAttribute

	/** Data Value */
	private Object m_value = new Object();
	/** The Attribute Instance */
	private MPAttributeLookup m_mPAttribute;

	/** The Text Field */
	private JTextField m_text = new JTextField(VLookup.DISPLAY_LENGTH);
	/** The Button */
	private CButton m_button = new CButton();

	JPopupMenu m_popupMenu = new JPopupMenu();
	private CMenuItem menuEditor;

	private boolean m_readWrite;
	private boolean m_mandatory;
	private int m_WindowNo;
	private int m_C_BPartner_ID;

	/** Calling Window Info */
	private int m_AD_Column_ID = 0;
	/** No Instance Key */
	private static Integer NO_INSTANCE = Integer.valueOf(0);
	/** Logger */
	private static CLogger log = CLogger.getCLogger(VPAttribute.class);

	/**
	 * Dispose resources
	 */
	public void dispose() {
		m_text = null;
		m_button = null;
		m_mPAttribute = null;
		m_field = null;
	} // dispose

	/**
	 * Set Mandatory
	 * 
	 * @param mandatory
	 *            mandatory
	 */
	public void setMandatory(boolean mandatory) {
		m_mandatory = mandatory;
		m_button.setMandatory(mandatory);
		setBackground(false);
	} // setMandatory

	/**
	 * Get Mandatory
	 * 
	 * @return mandatory
	 */
	public boolean isMandatory() {
		return m_mandatory;
	} // isMandatory

	/**
	 * Set ReadWrite
	 * 
	 * @param rw
	 *            read rwite
	 */
	public void setReadWrite(boolean rw) {
		m_readWrite = rw;
		m_button.setReadWrite(rw);
		setBackground(false);
	} // setReadWrite

	/**
	 * Is Read Write
	 * 
	 * @return read write
	 */
	public boolean isReadWrite() {
		return m_readWrite;
	} // isReadWrite

	/**
	 * Set Foreground
	 * 
	 * @param color
	 *            color
	 */
	@Override
	public void setForeground(Color color) {
		m_text.setForeground(color);
	} // SetForeground

	/**
	 * Set Background
	 * 
	 * @param error
	 *            Error
	 */
	public void setBackground(boolean error) {
		if (error)
			setBackground(CompierePLAF.getFieldBackground_Error());
		else if (!m_readWrite)
			setBackground(CompierePLAF.getFieldBackground_Inactive());
		else if (m_mandatory)
			setBackground(CompierePLAF.getFieldBackground_Mandatory());
		else
			setBackground(CompierePLAF.getInfoBackground());
	} // setBackground

	/**
	 * Set Background
	 * 
	 * @param color
	 *            Color
	 */
	@Override
	public void setBackground(Color color) {
		m_text.setBackground(color);
	} // setBackground

	/**************************************************************************
	 * Set/lookup Value
	 * 
	 * @param value
	 *            value
	 */
	public void setValue(Object value) {
		if (value == null || NO_INSTANCE.equals(value)) {
			m_text.setText("");
			m_value = value;
			return;
		}

		// The same
		if (value.equals(m_value))
			return;
		// new value
		log.fine("Value=" + value);
		m_value = value;
		m_text.setText(m_mPAttribute.getDisplay(value)); // loads value
	} // setValue

	/**
	 * Get Value
	 * 
	 * @return value
	 */
	public Object getValue() {
		return m_value;
	} // getValue

	/**
	 * Get Display Value
	 * 
	 * @return info
	 */
	public String getDisplay() {
		return m_text.getText();
	} // getDisplay

	/**
	 * Set Field/WindowNo
	 * 
	 * @param mField
	 *            field
	 */
	public void setField(GridField mField) {
		if (mField != null) {
			m_WindowNo = mField.getWindowNo();
			m_AD_Column_ID = mField.getAD_Column_ID();
		}
		m_field = mField;
	} // setField

	/** Grid Field */
	private GridField m_field = null;

	/**
	 * Get Field
	 * 
	 * @return gridField
	 */
	public GridField getField() {
		return m_field;
	} // getField

	/**
	 * Action Listener Interface
	 * 
	 * @param listener
	 *            listener
	 */
	public void addActionListener(ActionListener listener) {
		m_text.addActionListener(listener);
	} // addActionListener

	/**
	 * Action Listener Interface
	 * 
	 * @param listener
	 */
	public void removeActionListener(ActionListener listener) {
		m_text.removeActionListener(listener);
	} // removeActionListener

	/**
	 * Action Listener - start dialog
	 * 
	 * @param e
	 *            Event
	 */
	public void actionPerformed(ActionEvent e) {
		if (!m_button.isEnabled())
			return;
		m_button.setEnabled(false);
		//
		Integer oldValue = (Integer) getValue();
		int M_AttributeSetInstance_ID = oldValue == null ? 0 : oldValue
				.intValue();
		int M_Product_ID = Env.getCtx().getContextAsInt(m_WindowNo,
				"M_Product_ID");
		int M_ProductBOM_ID = Env.getCtx().getContextAsInt(m_WindowNo,
				"M_ProductBOM_ID");

		log.config("M_Product_ID=" + M_Product_ID + "/" + M_ProductBOM_ID
				+ ",M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID
				+ ", AD_Column_ID=" + m_AD_Column_ID);

		// M_Product.M_AttributeSetInstance_ID = 8418
		boolean productWindow = m_AD_Column_ID == 8418; // HARDCODED

		// Exclude ability to enter ASI
		boolean exclude = true;
		if (M_Product_ID != 0) {
			MProduct product = MProduct.get(Env.getCtx(), M_Product_ID);
			int M_AttributeSet_ID = product.getM_AttributeSet_ID();
			if (M_AttributeSet_ID != 0) {
				MAttributeSet mas = MAttributeSet.get(Env.getCtx(),
						M_AttributeSet_ID);
				exclude = mas.excludeEntry(m_AD_Column_ID, Env.getCtx()
						.isSOTrx(m_WindowNo));
			}
		}

		boolean changed = false;
		if (M_ProductBOM_ID != 0) // Use BOM Component
			M_Product_ID = M_ProductBOM_ID;
		//	
		if (!productWindow && (M_Product_ID == 0 || exclude)) {
			changed = true;
			m_text.setText(null);
			M_AttributeSetInstance_ID = 0;
		} else {
			VPAttributeDialog vad = new VPAttributeDialog(Env.getFrame(this),
					M_AttributeSetInstance_ID, M_Product_ID, m_C_BPartner_ID,
					productWindow, m_AD_Column_ID, m_WindowNo);
			if (vad.isChanged()) {
				m_text.setText(vad.getM_AttributeSetInstanceName());
				M_AttributeSetInstance_ID = vad.getM_AttributeSetInstance_ID();
				changed = true;
			}
		}
		/**
		 * Selection { // Get Model MAttributeSetInstance masi =
		 * MAttributeSetInstance.get(Env.getCtx(), M_AttributeSetInstance_ID,
		 * M_Product_ID); if (masi == null) { log.log(Level.SEVERE,
		 * "No Model for M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID
		 * + ", M_Product_ID=" + M_Product_ID); } else {
		 * ctx.setContext(m_WindowNo, "M_AttributeSet_ID",
		 * masi.getM_AttributeSet_ID()); // Get Attribute Set MAttributeSet as =
		 * masi.getMAttributeSet(); // Product has no Attribute Set if (as ==
		 * null) ADialog.error(m_WindowNo, this, "PAttributeNoAttributeSet"); //
		 * Product has no Instance Attributes else if
		 * (!as.isInstanceAttribute()) ADialog.error(m_WindowNo, this,
		 * "PAttributeNoInstanceAttribute"); else { int M_Warehouse_ID =
		 * Env.getCtx().getContextAsInt( m_WindowNo, "M_Warehouse_ID"); int
		 * M_Locator_ID = Env.getCtx().getContextAsInt( m_WindowNo,
		 * "M_Locator_ID"); String title = ""; PAttributeInstance pai = new
		 * PAttributeInstance ( Env.getFrame(this), title, M_Warehouse_ID,
		 * M_Locator_ID, M_Product_ID, m_C_BPartner_ID); if
		 * (pai.getM_AttributeSetInstance_ID() != -1) {
		 * m_text.setText(pai.getM_AttributeSetInstanceName());
		 * M_AttributeSetInstance_ID = pai.getM_AttributeSetInstance_ID();
		 * changed = true; } } } }
		 **/

		// Set Value
		if (changed) {
			log.finest("Changed M_AttributeSetInstance_ID="
					+ M_AttributeSetInstance_ID);
			m_value = new Object(); // force re-query display
			if (M_AttributeSetInstance_ID == 0)
				setValue(null);
			else
				setValue(Integer.valueOf(M_AttributeSetInstance_ID));
			try {
				fireVetoableChange("M_AttributeSetInstance_ID", new Object(),
						getValue());
			} catch (PropertyVetoException pve) {
				log.log(Level.SEVERE, "", pve);
			}
		} // change
		m_button.setEnabled(true);
		requestFocus();
	} // actionPerformed

	/**
	 * Property Change Listener
	 * 
	 * @param evt
	 *            event
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(org.compiere.model.GridField.PROPERTY))
			setValue(evt.getNewValue());
	} // propertyChange

	/**
	 * Request Focus
	 */
	@Override
	public void requestFocus() {
		m_text.requestFocus();
	} // requestFocus

	/**
	 * Request Focus In Window
	 * 
	 * @return focus request
	 */
	@Override
	public boolean requestFocusInWindow() {
		return m_text.requestFocusInWindow();
	} // requestFocusInWindow

	/**
	 * Get Focus Component
	 * 
	 * @return component
	 */
	public Component getFocusableComponent() {
		return m_text;
	} // getFocusComponent

} // VPAttribute

