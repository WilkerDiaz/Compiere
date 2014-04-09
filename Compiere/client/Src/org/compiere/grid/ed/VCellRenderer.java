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
import java.text.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.table.*;

import org.compiere.apps.graph.*;
import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.util.*;

/**
 * Table Cell Renderer based on DisplayType
 * 
 * @author Jorg Janke
 * @version $Id: VCellRenderer.java,v 1.4 2006/07/30 00:51:27 jjanke Exp $
 */
public final class VCellRenderer extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for Grid
	 * 
	 * @param mField
	 *            field model
	 */
	public VCellRenderer(GridField mField) {
		this(mField.getDisplayType());
		m_columnName = mField.getColumnName();
		this.setName(m_columnName);
		m_lookup = mField.getLookup();
		m_password = mField.isEncrypted();
	} // VCellRenderer

	/**
	 * Constructor for MiniGrid
	 * 
	 * @param displayType
	 *            Display Type
	 */
	public VCellRenderer(int displayType) {
		super();
		m_displayType = displayType;
		// Number
		if (FieldType.isNumeric(m_displayType)) {
			m_numberFormat = DisplayType.getNumberFormat(m_displayType);
			setHorizontalAlignment(SwingConstants.RIGHT);
		}
		// Date
		else if (FieldType.isDate(m_displayType))
			m_dateFormat = DisplayType.getDateFormat(m_displayType);
		//
		else if (m_displayType == DisplayTypeConstants.YesNo) {
			m_check = new JCheckBox();
			m_check.setMargin(new Insets(0, 0, 0, 0));
			m_check.setHorizontalAlignment(SwingConstants.CENTER);
			m_check.setOpaque(true);
		}
	} // VCellRenderer

	private int m_displayType;
	private String m_columnName = null;
	private Lookup m_lookup = null;
	private boolean m_password = false;
	//
	private SimpleDateFormat m_dateFormat = null;
	private DecimalFormat m_numberFormat = null;
	private JCheckBox m_check = null;
	/** Logger */
	private static CLogger log = CLogger.getCLogger(VCellRenderer.class);

	/**
	 * Get TableCell RendererComponent.
	 * 
	 * @param table
	 *            table
	 * @param value
	 *            value
	 * @param isSelected
	 *            selected
	 * @param hasFocus
	 *            focus
	 * @param row
	 *            row
	 * @param col
	 *            col
	 * @return component
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int col) {
		// log.finest(m_columnName
		// + ": Value=" + (value == null ? "null" : value.toString())
		// + ", Row=" + row + ", Col=" + col);

		Component c = null;
		if (m_displayType == DisplayTypeConstants.YesNo)
			c = m_check;
		else { // returns JLabel
			c = super.getTableCellRendererComponent(table, value, isSelected,
					hasFocus, row, col);
			c.setFont(CompierePLAF.getFont_Field());
		}

		// Background & Foreground
		Color bg = CompierePLAF.getFieldBackground_Normal();
		Color fg = CompierePLAF.getTextColor_Normal();
		// Inactive Background
		boolean ro = !table.isCellEditable(row, col);
		if (ro) {
			bg = CompierePLAF.getFieldBackground_Inactive();
			if (isSelected && !hasFocus)
				bg = bg.darker();
		}

		// Foreground
		int cCode = 0;
		// Grid
		if (table instanceof org.compiere.grid.VTable)
			cCode = ((org.compiere.grid.VTable) table).getColorCode(row);
		// MiniGrid
		else if (table instanceof org.compiere.minigrid.MiniTable)
			cCode = ((org.compiere.minigrid.MiniTable) table).getColorCode(row);
		//
		if (cCode == 0)
			; // Black
		else if (cCode < 0)
			fg = CompierePLAF.getTextColor_Issue(); // Red
		else
			fg = CompierePLAF.getTextColor_OK(); // Blue

		// Highlighted row
		if (isSelected) {
			// Windows is white on blue
			bg = table.getSelectionBackground();
			fg = table.getSelectionForeground();
			if (hasFocus)
				bg = GraphUtil.brighter(bg, .9);
		}

		// Set Color
		c.setBackground(bg);
		c.setForeground(fg);
		//
		// log.fine( "r=" + row + " c=" + col, // + " - " +
		// c.getClass().getName(),
		// "sel=" + isSelected + ", focus=" + hasFocus + ", edit=" +
		// table.isCellEditable(row, col));
		// Log.trace(7, "BG=" + (bg.equals(Color.white) ? "white" :
		// bg.toString()), "FG=" + (fg.equals(Color.black) ? "black" :
		// fg.toString()));

		// Format it
		setValue(value);
		return c;
	} // getTableCellRendererComponent

	/**
	 * Format Display Value. Default is JLabel
	 * 
	 * @param value
	 *            (key)value
	 */
	@Override
	protected void setValue(Object value) {
		String retValue = null;
		try {
			// Checkbox
			if (m_displayType == DisplayTypeConstants.YesNo) {
				if (value instanceof Boolean)
					m_check.setSelected(((Boolean) value).booleanValue());
				else
					m_check.setSelected("Y".equals(value));
				return;
			} else if (value == null)
				;
			// Number
			else if (FieldType.isNumeric(m_displayType))
				retValue = m_numberFormat.format(value);
			// Date
			else if (FieldType.isDate(m_displayType))
				retValue = m_dateFormat.format(value);
			// Row ID
			else if (m_displayType == DisplayTypeConstants.RowID)
				retValue = "";
			// Lookup
			else if (m_lookup != null
					&& (FieldType.isLookup(m_displayType)
							|| m_displayType == DisplayTypeConstants.Location
							|| m_displayType == DisplayTypeConstants.Account
							|| m_displayType == DisplayTypeConstants.Locator || m_displayType == DisplayTypeConstants.PAttribute))
				retValue = m_lookup.getDisplay(value);
			// Button
			else if (m_displayType == DisplayTypeConstants.Button) {
				if ("Record_ID".equals(m_columnName))
					retValue = "#" + value + "#";
				else
					retValue = null;
			}
			// Password (fixed string)
			else if (m_password)
				retValue = "**********";
			// other (String ...)
			else {
				super.setValue(value);
				return;
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "(" + value + ") "
					+ value.getClass().getName(), e);
			retValue = value.toString();
		}
		super.setValue(retValue);
	} // setValue

	/**
	 * to String
	 * 
	 * @return String representation
	 */
	@Override
	public String toString() {
		return "VCellRenderer[" + m_columnName + ",DisplayType="
				+ m_displayType + " - " + m_lookup + "]";
	} // toString

	/**
	 * Dispose
	 */
	public void dispose() {

		m_lookup = null;
	} // dispose

} // VCellRenderer
