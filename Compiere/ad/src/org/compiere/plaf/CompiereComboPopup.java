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
package org.compiere.plaf;

import javax.swing.*;
import javax.swing.plaf.basic.*;

import org.compiere.swing.*;

/**
 *  Compiere Combo Popup - allows to prevent the display of the popup
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereComboPopup.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CompiereComboPopup extends BasicComboPopup
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  Constructor
	 *  @param combo
	 */
	public CompiereComboPopup(JComboBox combo)
	{
		super(combo);
	}   //  CompiereComboPopup
	
	@Override
	protected void configureList()
	{
		super.configureList();
	//	list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}
	/**
	 * 	Get Height
	 */
	@Override
	protected int getPopupHeightForRowCount(int maxRowCount) 
	{
		// ensure the combo box sized for the amount of data to be displayed
		int rows = comboBox.getItemCount() < comboBox.getMaximumRowCount() ?
			comboBox.getItemCount() : comboBox.getMaximumRowCount();
		return super.getPopupHeightForRowCount(1) * rows;
	}

	/**
	 *  Conditionally show the Popup.
	 *  If the combo is a CComboBox/CField, the return value of the
	 *  method displayPopup determines if the popup is actually displayed
	 *  @see CComboBox#displayPopup()
	 *  @see CField#displayPopup()
	 */
	@Override
	public void show()
	{
		//  Check ComboBox if popup should be displayed
		if (comboBox instanceof CComboBox && !((CComboBox)comboBox).displayPopup())
			return;
		//  Check Field if popup should be displayed
		if (comboBox instanceof CField && !((CField)comboBox).displayPopup())
			return;
		super.show();
	}   //  show


	/**
	 *  Inform CComboBox and CField that Popup was hidden
	 *  @see CComboBox.hidingPopup
	 *  @see CField.hidingPopup
	 *
	public void hide()
	{
		super.hide();
		//  Inform ComboBox that popup was hidden
		if (comboBox instanceof CComboBox)
			(CComboBox)comboBox).hidingPopup();
		else if (comboBox instanceof CComboBox)
			(CComboBox)comboBox).hidingPopup();
	}   //  hide
	/**/
}   //  CompiereComboPopup
