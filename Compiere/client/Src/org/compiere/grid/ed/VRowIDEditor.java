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
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import org.compiere.util.*;

/**
 *  RowID Cell Editor providing Selection
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: VRowIDEditor.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class VRowIDEditor extends AbstractCellEditor implements TableCellEditor
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Constructor
	 */
	public VRowIDEditor(boolean select)
	{
		super();
		m_select = select;
		m_cb.setMargin(new Insets(0,0,0,0));
		m_cb.setHorizontalAlignment(SwingConstants.CENTER);
	}	//	VRowIDEditor

	private JCheckBox 	m_cb = new JCheckBox();
	private Object[] 	m_rid;
	private boolean		m_select;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VRowIDEditor.class);

	/**
	 *	Enable Selection to be displayed
	 */
	public void setEnableSelection(boolean showSelection)
	{
		m_select = showSelection;
	}	//	setEnableSelection

	/**
	 *	Ask the editor if it can start editing using anEvent.
	 *	This method is intended for the use of client to avoid the cost of
	 *	setting up and installing the editor component if editing is not possible.
	 *	If editing can be started this method returns true
	 */
	@Override
	public boolean isCellEditable(EventObject anEvent)
	{
		return m_select;
	}	//	isCellEditable

	/**
	 *	Sets an initial value for the editor. This will cause the editor to
	 *	stopEditing and lose any partially edited value if the editor is editing
	 *	when this method is called.
	 *	Returns the component that should be added to the client's Component hierarchy.
	 *	Once installed in the client's hierarchy this component
	 *	will then be able to draw and receive user input.
	 */
	public Component getTableCellEditorComponent(JTable table, 
		Object value, boolean isSelected, int row, int col)
	{
		log.fine("Value=" + value + ", row=" + row + ", col=" + col);
		m_rid = (Object[])value;
		if (m_rid == null || m_rid[1] == null)
			m_cb.setSelected(false);
		else
		{
			Boolean sel = (Boolean)m_rid[1];
			m_cb.setSelected(sel.booleanValue());
		}
		return m_cb;
	}	//	getTableCellEditorComponent

	/**
	 *	The editing cell should be selected or not
	 */
	@Override
	public boolean shouldSelectCell(EventObject anEvent)
	{
		return m_select;
	}	//	shouldSelectCell

	/**
	 *	Returns the value contained in the editor
	 */
	public Object getCellEditorValue()
	{
		log.fine("" + m_cb.isSelected());
		if (m_rid == null)
			return null;
		m_rid[1] = Boolean.valueOf (m_cb.isSelected());
		return m_rid;
	}	//	getCellEditorValue

}	//	VRowIDEditor
