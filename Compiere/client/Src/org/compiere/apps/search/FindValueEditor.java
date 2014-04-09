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
package org.compiere.apps.search;

import java.awt.*;

import javax.swing.*;

import org.compiere.common.constants.*;
import org.compiere.grid.ed.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Cell editor for Find Value field.
 *  Editor depends on Column setting
 *	Has to save entries how they are used in the query, i.e. '' for strings
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: FindValueEditor.java,v 1.2 2006/07/30 00:51:27 jjanke Exp $
 */
public class FindValueEditor extends JComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Constructor
	 *  @param find find
	 *  @param valueTo true if it is the "to" value column
	 */
	public FindValueEditor ()
	{
		super();
		setLayout(new BorderLayout());
		setEditor(null);
	}	//	FindValueEditor

	/**	Editor					*/
	private VEditor			m_editor = null;
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(FindValueEditor.class);
	
	/**
	 * 	Set Editor
	 *	@param field field
	 */
	public void setEditor (GridField field)
	{
		if (m_editor != null)
			remove((Component)m_editor);

		String columnName = "";
		if (field != null)
		{
			columnName = field.getColumnName();
			if (field.isKey())
				m_editor = new VNumber(columnName, false, false, true, DisplayTypeConstants.Integer, columnName);
			else
				m_editor = VEditorFactory.getEditor(null, field, true, true);
		}
		//
		if (m_editor == null)
			m_editor = new VString();
		//
		m_editor.setReadWrite(true);
		m_editor.setMandatory(false);
		m_editor.setValue(null);
		Dimension size = ((Component)m_editor).getPreferredSize();
		size.width = 120;
		setPreferredSize(size);
		add((Component)m_editor, BorderLayout.CENTER);
	}	//	setEditor

	/**
	 * 	Set Enabled
	 *	@param enabled enabled
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		if (m_editor != null)
			m_editor.setReadWrite(enabled);
	    super.setEnabled(enabled);
	}	//	setEnabled

	/**
	 * 	Focusable
	 *	@return true if enabled
	 */
	@Override
	public boolean isFocusable()
	{
	    return isEnabled();
	}	// isFocusable
	
	/**
	 * 	Set Value
	 *	@param value value
	 */
	public void setValue(Object value)
	{
		if (m_editor == null)
			return;
		m_editor.setValue(value);
	}	//	setValue
	
	/**
	 *	Get Value
	 *	Need to convert to String
	 *  @return current value or null
	 */
	public Object getValue()
	{
		if (m_editor == null)
			return null;
		Object obj = m_editor.getValue();		//	returns Integer, BidDecimal, String
		log.fine(m_editor.getName() + "=" + obj);
		return obj;
	}	//	getValue
	
	/**
	 * 	Get Display Value
	 *	@return value or null
	 */
	public String getDisplay()
	{
		if (m_editor == null)
			return null;
		return m_editor.getDisplay();
	}	//	getDisplay

	/**
	 * 	Get Values
	 *	@return values or null
	 */
	public String[] getValues()
	{
		if (!(m_editor instanceof VLookup))
			return null;

		return null;
	}	//	getValues
	
	/**
	 * 	Get Display Values
	 *	@return values or null
	 */
	public String[] getDisplays()
	{
		if (!(m_editor instanceof VLookup))
			return null;

		return null;
	}	//	getDisplays
	
}	//	FindValueEditor
