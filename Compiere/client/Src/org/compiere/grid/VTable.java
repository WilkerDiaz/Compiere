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
package org.compiere.grid;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;

import javax.swing.table.*;

import org.compiere.grid.ed.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  Table Grid based on CTable.
 * 	Used in GridController
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: VTable.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public final class VTable extends CTable 
	implements PropertyChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final GridController m_controller;

	/**
	 *	Default Constructor
	 *	@param withRowHeader with numbers in Front
	 * @param controller TODO
	 */
	public VTable(boolean withRowHeader, GridController controller)
	{
		super(withRowHeader);
		setAutoscrolls(true);
		
		addMouseListener(new MouseAdapter()
		{
			// ensures that right clicks start select, start editing, and, if the
			// right mouse button is pressed, show the popup menu
			@Override
			public void mouseClicked(MouseEvent e) {
				Point p = e.getPoint();
				boolean canEdit = !isEditing() || getCellEditor().stopCellEditing();
		
				if (canEdit && editCellAt(rowAtPoint(p), columnAtPoint(p)))
				{
					getEditorComponent().dispatchEvent(e);
				}
			}
		});
		
		m_controller = controller;
	}	//	VTable

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VTable.class);
	
	/**
	 *  Property Change Listener for CurrentRow.
	 *  - Selects the current row if not already selected
	 *  - Required when navigating via Buttons
	 *  @param evt event
	 */
	public void propertyChange(PropertyChangeEvent evt)
	{
	//	log.config(evt);
		if (evt.getPropertyName().equals(GridTab.PROPERTY))
		{
			int row = ((Integer)evt.getNewValue()).intValue();
			int selRow = getSelectedRow();
			if (row == selRow)
				return;
			log.config(GridTab.PROPERTY + "=" + row + " from " + selRow);
			setRowSelectionInterval(row,row);
		    Rectangle cellRect = getCellRect(row, 0, false);
		    if (cellRect != null)
		    	scrollRectToVisible(cellRect);
			log.config(GridTab.PROPERTY + "=" + row + " from " + selRow);
		}
	}   //  propertyChange

	/**
	 *	Get ColorCode for Row.
	 *  <pre>
	 *	If numerical value in compare column is
	 *		negative = -1,
	 *      positive = 1,
	 *      otherwise = 0
	 *  </pre>
	 *  @param row row
	 *  @return color code
	 */
	public int getColorCode (int row)
	{
		return ((GridTable)getModel()).getColorCode(row);
	}   //  getColorCode

	/**
	 *  Sort Table
	 *  @param modelColumnIndex model column sort index
	 */
	@Override
	protected void sort (int modelColumnIndex)
	{
		int rows = getRowCount();
		if (rows == 0)
			return;
		//
		TableModel model = getModel();
		if (!(model instanceof GridTable))
		{
			super.sort(modelColumnIndex);
			return;
		}

		//  other sort column
		if (modelColumnIndex != p_lastSortIndex)
			p_asc = true;
		else
			p_asc = !p_asc;

		p_lastSortIndex = modelColumnIndex;
		//
		log.config("#" + modelColumnIndex
			+ " - rows=" + rows + ", asc=" + p_asc);

		((GridTable)model).sort(modelColumnIndex, p_asc);
		//  table model fires "Sorted" DataStatus event which causes MTab to position to row 0
	}   //  sort

	/**
	 *  Transfer focus explicitly to editor due to editors with multiple components
	 *
	 *  @param row row
	 *  @param column column
	 *  @param e event
	 *  @return true if cell is editing
	 */
	@Override
	public boolean editCellAt (int row, int column, java.util.EventObject e)
	{
		if(m_controller != null && m_controller.getMTab() != null )
			m_controller.getMTab().navigate(row);
		
		if (!super.editCellAt(row, column, e))
			return false;
	//	log.fine( "VTable.editCellAt", "r=" + row + ", c=" + column);

		Object ed = getCellEditor();
		if (ed instanceof VEditor)
			((Component)ed).requestFocus();
		else if (ed instanceof VCellEditor)
		{
			ed = ((VCellEditor)ed).getEditor();
			((Component)ed).requestFocus();
			if (ed instanceof VString)
			{
				((VString)ed).selectAll();
			}
			else if (ed instanceof VNumber)
			{
				((VNumber)ed).selectAll();
			}
		}
		return true;
	}   //  editCellAt

	/**
	 *  toString
	 *  @return String representation
	 */
	@Override
	public String toString()
	{
		return new StringBuffer("VTable[")
			.append(getModel()).append("]").toString();
	}   //  toString
	
}	//	VTable
