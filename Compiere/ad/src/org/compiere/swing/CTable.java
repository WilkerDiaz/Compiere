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
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/**
 *	Model Independent enhanced JTable.
 *  Provides sizing and sorting
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: CTable.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CTable extends JTable
{
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 *	Default Constructor.
	 */
	public CTable()
	{
		this(true, new DefaultTableModel());
	}	//	CTable
	
	/**
	 *	Creates a CTable.
	 *	@param withRowHeader with numbers in Front
	 */
	public CTable(boolean withRowHeader)
	{
		this(withRowHeader, new DefaultTableModel());
	}	//	CTable
	
	/**
	 *	Creates a CTable.
	 *	@param withRowHeader with numbers in Front
	 *	@param model table model
	 */
	public CTable(boolean withRowHeader, TableModel model)
	{
		super(model);
		if (withRowHeader)
			m_rowHeader = new CRowHeader(this);
		//
		setColumnSelectionAllowed(false);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTableHeader().addMouseListener(new CTableMouseListener());
		setSurrendersFocusOnKeystroke(true);
	}	//	CTable
	
	/** Sort Enabled			*/
	private boolean				m_sortEnabled = true;
	/** Last model index sorted */
	protected int         		p_lastSortIndex = -1;
	/** Sort direction          */
	protected boolean     		p_asc = true;

	/** Sizing: making sure it fits in a column	*/
	private final int 			SLACK = 15;
	/** Sizing: max size in pt					*/
	private final int 			MAXSIZE = 250;
	/** Model Index of Key Column   */
	protected int              	p_keyColumnIndex = -1;
	
	/** A rowHeader. */
	private CRowHeader 			m_rowHeader = null;
	
	// temp sort comparators
	MSort m_sortTemp1 = new MSort(0, null);
	MSort m_sortTemp2 = new MSort(1, null);
	
	/**	Logger			*/
	private static Logger log = Logger.getLogger(CTable.class.getName());
	
	/**
	 * @see javax.swing.JTable#configureEnclosingScrollPane()
	 */
	@Override
	protected void configureEnclosingScrollPane()
	{
		super.configureEnclosingScrollPane();
		if (m_rowHeader == null)
			return;
        Container parent = getParent();
    	if (parent instanceof JViewport) 
    	{
    		parent = parent.getParent();
    		if (parent instanceof JScrollPane)
    		{
    			((JScrollPane)parent).setRowHeaderView(m_rowHeader);
        		m_rowHeader.setTable(this);
    		}
    	}
	}	//	configureEnclosingScrollPane()
	
	/**
	 * @return the rowHeader
	 */
	public CRowHeader getRowHeader()
	{
		return m_rowHeader;
	}	//	getRowHeader
	
	/**
	 * @param rowHeader the rowHeader to set
	 */
	public void setRowHeader(CRowHeader rowHeader)
	{
		if (m_rowHeader != null && m_rowHeader.getParent() != null)
		{
			m_rowHeader.getParent().remove(this.m_rowHeader);
		}
		m_rowHeader = rowHeader;
		
		if (rowHeader != null)
		{
			rowHeader.setTable(this);
			configureEnclosingScrollPane();
		}
	}	//	setRowHeader
	
	/**
	 * 	Set Model index of Key Column.
	 *  Used for identifying previous selected row after fort complete to set as selected row.
	 *  If not set, column 0 is used.
	 * 	@param keyColumnIndex model index
	 */
	public void setKeyColumnIndex (int keyColumnIndex)
	{
		p_keyColumnIndex = keyColumnIndex;
	}	//	setKeyColumnIndex

	/**
	 * 	Get Model index of Key Column
	 *  @return model index
	 */
	public int getKeyColumnIndex()
	{
		return p_keyColumnIndex;
	}	//	getKeyColumnIndex

	/**
	 * 	Get Current Row Key Column Value
	 *  @return value or null
	 */
	public Object getSelectedKeyColumnValue()
	{
		int row = getSelectedRow();
		if (row != -1 && p_keyColumnIndex != -1)
			return getModel().getValueAt(row, p_keyColumnIndex);
		return null;
	}	//	getKeyColumnValue

	/**
	 *  Get Selected Value or null
	 *  @return value
	 */
	public Object getSelectedValue()
	{
		int row = getSelectedRow();
		int col = getSelectedColumn();
		if (row == -1 || col == -1)
			return null;
		return getValueAt(row, col);
	}   //  getSelectedValue

	/**
	 *  Stop Table Editors and remove focus
	 *  @param saveValue save value
	 */
	public void stopEditor (boolean saveValue)
	{
		//  MultiRow - remove editors
		ChangeEvent ce = new ChangeEvent(this);
		if (saveValue)
			editingStopped(ce);
		else
			editingCanceled(ce);
		//
		if (getInputContext() != null)
			getInputContext().endComposition();
		//  change focus to next
		transferFocus();
	}   //  stopEditor

	
	/**************************************************************************
	 *	Size Columns.
	 *	@param useColumnIdentifier if false uses plain content -
	 *  otherwise uses Column Identifier to indicate displayed columns
	 */
	public void autoSize (boolean useColumnIdentifier)
	{
		TableModel model = this.getModel();
		int size = model.getColumnCount();
		//	for all columns
		for (int c = 0; c < size; c++)
		{
			TableColumn column = getColumnModel().getColumn(c);
			//	Not displayed columns
			if (useColumnIdentifier
				&& (column.getIdentifier() == null
					|| column.getMaxWidth() == 0
					|| column.getIdentifier().toString().length() == 0))
				continue;

			int width = 0;
			//	Header
			TableCellRenderer renderer = column.getHeaderRenderer();
			if (renderer == null)
				renderer = new DefaultTableCellRenderer();
			Component comp = null;
			if (renderer != null)
				comp = renderer.getTableCellRendererComponent
					(this, column.getHeaderValue(), false, false, 0, 0);
			//
			if (comp != null)
			{
				width = comp.getPreferredSize().width;
				width = Math.max(width, comp.getWidth());

				//	Cells
			//	int col = column.getModelIndex();
				int maxRow = Math.min(15, getRowCount());
				TableCellRenderer cRenderer = null;
				Component cComp = null;
				try
				{
					for (int row = 0; row < maxRow; row++)
					{
						cRenderer = getCellRenderer(row, c);
						cComp = cRenderer.getTableCellRendererComponent
							(this, getValueAt(row, c), false, false, row, c);
						int rowWidth = cComp.getPreferredSize().width;
						width = Math.max(width, rowWidth);
					}
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, column.getIdentifier().toString(), e);
				}
				//	Width not greater than 250
				width = Math.min(MAXSIZE, width + SLACK);
			}
			//
			column.setPreferredWidth(width);
		}	//	for all columns
	}	//	autoSize

	/**
	 * 	Set Sorting Enablement
	 *	@param enable true if enabled
	 */
	public void setSortEnabled(boolean enable)
	{
		m_sortEnabled = enable;
	}	//	setSortEnabled
	
	/**
	 * 	Is Sorting Enabled
	 *	@return enabled
	 */
	public boolean isSortEnabled()
	{
		return m_sortEnabled;
	}	//	isSortEnabled
	
	/**
	 *  Sort Table
	 *  @param modelColumnIndex model column sort index
	 */
	protected void sort (final int modelColumnIndex)
	{
		if (!m_sortEnabled)
			return;
		int rows = getRowCount();
		if (rows == 0)
			return;
		//  other column
		if (modelColumnIndex != p_lastSortIndex)
			p_asc = true;
		else
			p_asc = !p_asc;
		p_lastSortIndex = modelColumnIndex;
		//
		log.config("#" + modelColumnIndex + " - rows=" + rows + ", asc=" + p_asc);

		//  Selection
		Object selected = null;
		int selRow = getSelectedRow();
		int selCol = p_keyColumnIndex == -1 ? 0 : p_keyColumnIndex;	//	used to identify current row
		if (getSelectedRow() >= 0)
			selected = getValueAt(selRow, selCol);

		//  Prepare sorting
		DefaultTableModel model = (DefaultTableModel)getModel();

		
		//	sort list it
		Vector<?> modelData = model.getDataVector();
		Collections.sort(modelData, new Comparator<Object>()
		{
			{
				m_sortTemp1.setSortAsc(p_asc);
			}
			public int compare(Object o1, Object o2)
			{
				Vector<?> s1 = (Vector<?>) o1;
				Vector<?> s2 = (Vector<?>) o2;
				m_sortTemp1.data = s1.get(modelColumnIndex);
				m_sortTemp2.data = s2.get(modelColumnIndex);
				return m_sortTemp1.compare(m_sortTemp1, m_sortTemp2);
			}
		});
		model.fireTableDataChanged();
		//  we are done

		//  selection
		clearSelection();
		if (selected != null)
		{
			for (int r = 0; r < rows; r++)
			{
				if (selected.equals(getValueAt(r, selCol)))
				{
					setRowSelectionInterval(r,r);
					break;
				}
			}
		}   //  selected != null
	}   //  sort

	/**
	 *  String Representation
	 *  @return info
	 */
	@Override
	public String toString()
	{
		return new StringBuffer("CTable[").append(getModel()).append("]").toString();
	}   //  toString

	
	/**************************************************************************
	 *  MouseListener
	 */
	class CTableMouseListener extends MouseAdapter
	{
		/**
		 *  Constructor
		 */
		public CTableMouseListener()
		{
			super();
		}   //  CTableMouseListener

		/**
		 *  Mouse clicked
		 *  @param e event
		 */
		@Override
		public void mouseClicked (MouseEvent e)
		{
			int vc = getColumnModel().getColumnIndexAtX(e.getX());
		//	log.info( "Sort " + vc + "=" + getColumnModel().getColumn(vc).getHeaderValue());
			int mc = convertColumnIndexToModel(vc);
			sort(mc);
		}
	}	//  CTableMouseListener

}	//	CTable
