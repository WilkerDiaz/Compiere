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

import java.awt.event.*;
import java.beans.*;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

/**
 * 	Button Listener
 *	
 *  @author Jorg Janke
 *  @version $Id: CompiereButtonListener.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CompiereButtonListener extends BasicButtonListener
{
	/**
	 * 	Compiere Button Listener
	 *	@param b button
	 */
	public CompiereButtonListener (AbstractButton b)
	{
		super (b);
	}	//	CompiereButtonListener
	
	/**
	 * 	Install Keyboard Actions
	 *	@param c component
	 */
	@Override
	public void installKeyboardActions (JComponent c)
	{
		super.installKeyboardActions (c);
		updateMnemonicBindingX ((AbstractButton)c);
	}	//	installKeyboardActions

	/**
	 * 	Property Change
	 *	@param e event
	 */
	@Override
	public void propertyChange (PropertyChangeEvent e)
	{
		String prop = e.getPropertyName();
		if (prop == AbstractButton.MNEMONIC_CHANGED_PROPERTY)
			updateMnemonicBindingX ((AbstractButton)e.getSource());
		else
			super.propertyChange (e);
	}	//	propertyChange
	
	/**
	 * 	Update Mnemonic Binding
	 *	@param b button
	 */
    void updateMnemonicBindingX (AbstractButton b) 
    {
    	int m = b.getMnemonic();
    	if (m != 0) 
    	{
    	    InputMap map = SwingUtilities.getUIInputMap(b, JComponent.WHEN_IN_FOCUSED_WINDOW);

    	    if (map == null) 
    	    {
    	    	map = new ComponentInputMapUIResource(b);
    	    	SwingUtilities.replaceUIInputMap(b, JComponent.WHEN_IN_FOCUSED_WINDOW, map);
    	    }
    	    map.clear();
    	    String className = b.getClass().getName();
    	    int mask = InputEvent.ALT_MASK;		//	Default Buttons
    	    if (b instanceof JCheckBox 			//	In Tab
    	    	|| className.indexOf("VButton") != -1)
    	    	mask = InputEvent.SHIFT_MASK + InputEvent.CTRL_MASK;
    	    map.put(KeyStroke.getKeyStroke(m, mask, false), "pressed");
    	    map.put(KeyStroke.getKeyStroke(m, mask, true), "released");
    	    map.put(KeyStroke.getKeyStroke(m, 0, true), "released");
    	}
    	else 
    	{
    		InputMap map = SwingUtilities.getUIInputMap(b, JComponent.WHEN_IN_FOCUSED_WINDOW);
    		if (map != null)
    			map.clear();
    	}
    }	//	updateMnemonicBindingX
    
}	//	CompiereButtonListener
