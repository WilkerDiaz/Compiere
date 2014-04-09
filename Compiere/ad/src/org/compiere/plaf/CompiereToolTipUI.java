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

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;

/**
 * 	Tool Tip
 *	
 *  @author Jorg Janke
 *  @version $Id: CompiereToolTipUI.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CompiereToolTipUI extends MetalToolTipUI
{
	
    public static ComponentUI createUI(JComponent c) 
    {
        return sharedInstance;
    }

    static CompiereToolTipUI sharedInstance = new CompiereToolTipUI();

    private JToolTip tip;
	
    @Override
	public Dimension getPreferredSize(JComponent c) 
    {
    	tip = (JToolTip)c;
    	return super.getPreferredSize(c);
    }
    @Override
	public void paint(Graphics g, JComponent c) 
    {
    	tip = (JToolTip)c;
    	super.paint(g, c);
    }

    /**
     * 	Get Accelerator String
     *	@return string
     */
    @Override
	public String getAcceleratorString ()
	{
    	String str = super.getAcceleratorString();
    	
		if (tip == null || isAcceleratorHidden ())
			return str;
		JComponent comp = tip.getComponent ();
		if (comp == null || comp instanceof JTabbedPane || comp instanceof JMenuItem)
			return str;
		
		KeyStroke[] keys = comp.getRegisteredKeyStrokes ();
		StringBuffer controlKeyStr = new StringBuffer();
		for (KeyStroke element : keys) {
			int mod = element.getModifiers ();
			int condition = comp.getConditionForKeyStroke (element);
			if (condition == JComponent.WHEN_IN_FOCUSED_WINDOW)
			{
				String prefix = KeyEvent.getKeyModifiersText (mod);
				if (prefix.length() > 1)
				{
					if (controlKeyStr.length() > 0)
						controlKeyStr.append("  ");
					controlKeyStr.append(prefix).append("-")
						.append(KeyEvent.getKeyText(element.getKeyCode()));
					break;		//	just first
				}
			}
		}
		return controlKeyStr.toString();
	}	//	getAcceleratorString
	
}	//	CompierelToolTipUI
