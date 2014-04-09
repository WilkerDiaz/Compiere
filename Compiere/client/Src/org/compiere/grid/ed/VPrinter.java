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
import java.util.*;

import javax.swing.*;

import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.print.*;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *	Printer Editor
 *	
 *  @author Jorg Janke
 *  @version $Id$
 */
public class VPrinter extends CComboBox
	implements VEditor
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Detail Constructor
	 *  @param columnName column name
	 *  @param mandatory mandatory
	 *  @param isReadOnly read only
	 *  @param isUpdateable updateable
	 *  @param fieldLength field length
	 */
	public VPrinter (String columnName, boolean mandatory, boolean isReadOnly, boolean isUpdateable,
		int fieldLength)
	{
		super();
		setName(columnName);
		m_columnName = columnName;
		//
		setMandatory(mandatory);
		if (mandatory)
			setModel(new DefaultComboBoxModel(CPrinter.getPrinterNames()));
		else
		{
			String[] printer = CPrinter.getPrinterNames();
			Vector<String> v = new Vector<String>();
			v.add("");
			for (String element : printer)
				v.add (element);
			setModel(new DefaultComboBoxModel(v));
		}

		//	Editable
		if (isReadOnly || !isUpdateable)
		{
			setEditable(false);
			setBackground(CompierePLAF.getFieldBackground_Inactive());
		}
		this.addActionListener(this);
		setForeground(CompierePLAF.getTextColor_Normal());
		setBackground(CompierePLAF.getFieldBackground_Normal());
	}	//	VString

	/** Grid Field				*/
	private GridField      		m_mField = null;
	/** Column Name				*/
	private String				m_columnName;
	/**	Setting Value			*/
	private volatile boolean	m_setting = false;
	
	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger (VPrinter.class);

	/**
	 *	Set Editor to value
	 *  @param value value
	 */
	@Override
	public void setValue(Object value)
	{
		log.config("" + value);
		m_setting = true;
		super.setValue(value);
		m_setting = false;
	}	//	setValue

	/**
	 * 	Action Performed
	 *	@param e event
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (m_setting)
			return;
		super.actionPerformed (e);
		Object value = getValue();
		log.config("" + value);
		try
		{
			fireVetoableChange (m_columnName, "_", value);
		}
		catch (PropertyVetoException e1)
		{
		}
	}	//	actionPerformed

	
	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		m_mField = null;
	}	//	dispose

	/**
	 *  Set Field/WindowNo 
	 *  @param mField field
	 */
	public void setField (GridField mField)
	{
		m_mField = mField;
	}   //  setField

	/**
	 *  Get Field
	 *  @return gridField
	 */
	public GridField getField()
	{
		return m_mField;
	}   //  getField

	/**
	 * 	Property Change
	 *	@param evt event
	 */
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals(GridField.PROPERTY))
			setValue(evt.getNewValue());
	}	//	propertyChange
	
	/**
	 * 	Get Focus Component
	 *	@return component
	 */
	public Component getFocusableComponent()
	{
		return this;
	}	//	getFocusableComponent

}	//	VPrinter
