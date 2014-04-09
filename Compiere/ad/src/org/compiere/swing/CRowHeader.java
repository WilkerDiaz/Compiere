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
import java.beans.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.compiere.plaf.*;

/**
 * Row header for CTables in JScrollPanes. 
 * 
 */
public class CRowHeader extends JList
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a CRowHeader.
	 */
	public CRowHeader()
	{
		setModel(m_listModel);
		setUI(new CompiereRowHeaderUI());
		for (MouseMotionListener listener : getMouseMotionListeners()) 
			removeMouseMotionListener(listener);
		
		for (MouseListener listener : getMouseListeners()) 
			removeMouseListener(listener);
		m_button.setMargin(new Insets(0,0,0,0));
        
        ListCellRenderer renderer = new ListCellRenderer()
        {
            Dimension prefSize = new Dimension();
            public Component getListCellRendererComponent(JList list,
                                                          Object value,
                                                          int index,
                                                          boolean isSelected,
                                                          boolean cellHasFocus)
            {
                m_button.setText(value.toString());
                m_button.setFont(list.getFont());
                prefSize.width = computeWidth();
                prefSize.height = CRowHeader.this.m_table.getRowHeight(index);
                m_button.setPreferredSize(prefSize);
                return m_button;
            }
        };
        
        setCellRenderer(renderer);
	}	//	CRowHeader

	/**
	 *	Creates a CRowHeader.
	 *	@param table
	 */
	public CRowHeader(JTable table)
	{
		this();
		setTable(table);
	}	//	CRowHeader

	/** A listModel. */
	private FastListModel m_listModel = new FastListModel();

	/** A table. */
	protected JTable 		m_table;
	
	/** Width. */
	private int 			m_width;
	
	/** Row count. */
	private int 			m_rowCount = -1;

	//  for 3D effect in Windows
	CButton 				m_button = new CButton();
	
	/** A tableModelListener. */
	TableModelListener tableModelListener = new TableModelListener()
	{
		public void tableChanged(TableModelEvent tme)
		{
			updateRowModel();
		}
	};
	
	/** A modelChangeListener. */
	private PropertyChangeListener modelChangeListener = new PropertyChangeListener()
	{
		public void propertyChange(PropertyChangeEvent evt)
		{
			((TableModel)evt.getOldValue()).removeTableModelListener(tableModelListener);
			((TableModel)evt.getNewValue()).addTableModelListener(tableModelListener);
			updateRowModel();
		}
	};

	/**
	 * 	Compute Width of Header
	 */
	int computeWidth()
	{
		int tableCount = m_table.getRowCount();
		if (tableCount == m_rowCount)
			return m_width;
		
		m_rowCount = tableCount;
		FontMetrics fm = m_table.getFontMetrics(m_table.getFont());
		String rowCountStr = Integer.toString(m_rowCount * 10);
		m_width = SwingUtilities.computeStringWidth(fm, rowCountStr);
		
		return m_width;
	}	//	computeWidth
	
	/* (non-Javadoc)
	 * @see javax.swing.JList#getPreferredScrollableViewportSize()
	 */
	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		Dimension d = super.getPreferredScrollableViewportSize();
		d.width =
			(m_table != null && m_table.getRowCount() > 0) ? d.width : computeWidth();
		return d;
	}
	
	/**
	 * @return the table
	 */
	public JTable getTable()
	{
		return m_table;
	}
	
	/**
	 * @param table the table to set
	 */
	public void setTable(final JTable table)
	{
		if (m_table != null)
		{
			m_table.getModel().removeTableModelListener(tableModelListener);
			m_table.removePropertyChangeListener("model", modelChangeListener);
		}
		
		m_table = table;
		
		if (m_table != null)
		{
			m_table.getModel().addTableModelListener(tableModelListener);
			m_table.addPropertyChangeListener("model", modelChangeListener);

            setFont(table.getFont());
            setBackground(table.getBackground());
            setForeground(table.getForeground());

            updateRowModel();
            table.getModel().addTableModelListener(tableModelListener);
            table.addPropertyChangeListener("model", modelChangeListener);
		}
	}	//	setTable
	
	/**
	 * 	Sync List with Table
	 */
	void updateRowModel()
	{
		int tableRows = m_table.getRowCount();
		int listRows = m_listModel.getSize();
		if (tableRows == listRows)
			return;
		
		if (listRows > tableRows)
			m_listModel.clear();
		for (int i = m_listModel.getSize(); i < tableRows; i++)
		{
			m_listModel.add(Integer.toString(i + 1));
		}
		
		m_listModel.fireContentsChanged();
	}	//	updateRowModel
	
	class FastListModel extends ArrayList<String> implements ListModel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/** A m_listenerList. */
		private EventListenerList m_listenerList = new EventListenerList();
		
		/** A m_listDataEvent. */
		private ListDataEvent m_listDataEvent =
			new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize()); 
		
		/* (non-Javadoc)
		 * @see javax.swing.ListModel#addListDataListener(javax.swing.event.ListDataListener)
		 */
		public void addListDataListener(ListDataListener ldl)
		{
			m_listenerList.remove(ListDataListener.class, ldl);
			m_listenerList.add(ListDataListener.class, ldl);
		}
		
		/**
		 * 
		 */
		public void fireContentsChanged()
		{
			for (ListDataListener ldl : m_listenerList.getListeners(ListDataListener.class))
				ldl.contentsChanged(m_listDataEvent);
		}

		/* (non-Javadoc)
		 * @see javax.swing.ListModel#getElementAt(int)
		 */
		public Object getElementAt(int index)
		{
			return get(index);
		}

		/* (non-Javadoc)
		 * @see javax.swing.ListModel#getSize()
		 */
		public int getSize()
		{
			return size();
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.ListModel#removeListDataListener(javax.swing.event.ListDataListener)
		 */
		public void removeListDataListener(ListDataListener ldl)
		{
			m_listenerList.remove(ListDataListener.class, ldl);
		}
	}
	
}	//	CRowHeader
