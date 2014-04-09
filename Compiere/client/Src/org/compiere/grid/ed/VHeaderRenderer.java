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

import javax.swing.*;
import javax.swing.table.*;

import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.swing.*;

/**
 *  Table Header Renderer based on Display Type for aligmnet
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: VHeaderRenderer.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public final class VHeaderRenderer implements TableCellRenderer
{
	/**
	 *	Constructor
	 *  @param displayType
	 */
	public VHeaderRenderer(int displayType)
	{
		super();
		//	Alignment
		if (FieldType.isNumeric(displayType))
			m_button.setHorizontalAlignment(SwingConstants.RIGHT);
		else if (displayType == DisplayTypeConstants.YesNo)
			m_button.setHorizontalAlignment(SwingConstants.CENTER);
		else
			m_button.setHorizontalAlignment(SwingConstants.LEFT);
		m_button.setMargin(new Insets(0,0,0,0));
	}	//	VHeaderRenderer

	//  for 3D effect in Windows
	private CButton m_button = new CButton();

	/**
	 *	Get TableCell RendererComponent
	 *  @param table
	 *  @param value
	 *  @param isSelected
	 *  @param hasFocus
	 *  @param row
	 *  @param column
	 *  @return Button
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int column)
	{
	//	Log.trace(this,10, "VHeaderRenderer.getTableCellRendererComponent", value==null ? "null" : value.toString());
		//  indicator for invisible column
		m_button.setFont(table.getFont());
		if (value == null)
		{
			m_button.setPreferredSize(new Dimension(0,0));
			return m_button;
		}
		m_button.setText(value.toString());
		return m_button;
	}	//	getTableCellRendererComponent

}	//	VHeaderRenderer
