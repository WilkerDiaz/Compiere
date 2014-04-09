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
package org.compiere.apps;

import java.awt.*;

import javax.swing.*;

import org.compiere.grid.*;
import org.compiere.grid.ed.*;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *  Compiere Application Focus Traversal Policy
 *
 *  @author     Jorg Janke
 *  @version    $Id: AFocusTraversalPolicy.java,v 1.2 2006/07/30 00:51:27 jjanke Exp $
 */
public class AFocusTraversalPolicy extends LayoutFocusTraversalPolicy
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  Get singleton
	 *  @return AFocusTraversalPolicy
	 */
	public static AFocusTraversalPolicy get()
	{
		if (s_policy == null)
			s_policy = new AFocusTraversalPolicy();
		return s_policy;
	}   //  get

	/** Default Policy          */
	private static AFocusTraversalPolicy s_policy = new AFocusTraversalPolicy();

	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger(AFocusTraversalPolicy.class);
	
	private static final boolean DEBUG = false;
	
	/**************************************************************************
	 *  Constructor
	 */
	public AFocusTraversalPolicy ()
	{
		super();
	}   //  AFocusTraversalPolicy

	boolean m_default = false;
	
	/**
	 * 	Get Default Component
	 *	@param aContainer container
	 *	@return default or null
	 */
	@Override
	public Component getDefaultComponent (Container aContainer)
	{
		if (DEBUG)
			log.warning("Root: " + aContainer);
		m_default = true;
		Component c = super.getDefaultComponent (aContainer);
		if (DEBUG)
			log.warning("Default: " + c);
		m_default = false;
		return c;
	}	//	getDefaultComponent

	
	/**
	 * 	Get First Component
	 *	@param aContainer container
	 *	@return first component to focus
	 */
	@Override
	public Component getFirstComponent(Container aContainer)
	{
		Component c = null;
		if (aContainer instanceof VPanel)
		{
			VEditor editor = ((VPanel)aContainer).getDefaultFocus();
			if (editor != null && editor.isReadWrite())
				c = editor.getFocusableComponent();
			if (DEBUG)
				log.warning("Found=" + c);
			if (c != null)
				return c;
		}
	    return super.getFirstComponent(aContainer);
	}	//	getFirstComponent
	
	/**
	 * Determines whether the specified <code>Component</code>
	 * is an acceptable choice as the new focus owner.
	 * This method performs the following sequence of operations:
	 * <ol>
	 * <li>Checks whether <code>aComponent</code> is visible, displayable,
	 *     enabled, and focusable.  If any of these properties is
	 *     <code>false</code>, this method returns <code>false</code>.
	 * <li>If <code>aComponent</code> is an instance of <code>JTable</code>,
	 *     returns <code>true</code>.
	 * <li>If <code>aComponent</code> is an instance of <code>JComboBox</code>,
	 *     then returns the value of
	 *     <code>aComponent.getUI().isFocusTraversable(aComponent)</code>.
	 * <li>If <code>aComponent</code> is a <code>JComponent</code>
	 *     with a <code>JComponent.WHEN_FOCUSED</code>
	 *     <code>InputMap</code> that is neither <code>null</code>
	 *     nor empty, returns <code>true</code>.
	 * <li>Returns the value of
	 *     <code>DefaultFocusTraversalPolicy.accept(aComponent)</code>.
	 * </ol>
	 *
	 * @param aComponent the <code>Component</code> whose fitness
	 *                   as a focus owner is to be tested
	 * @see java.awt.Component#isVisible
	 * @see java.awt.Component#isDisplayable
	 * @see java.awt.Component#isEnabled
	 * @see java.awt.Component#isFocusable
	 * @see javax.swing.plaf.ComboBoxUI#isFocusTraversable
	 * @see javax.swing.JComponent#getInputMap()
	 * @see java.awt.DefaultFocusTraversalPolicy#accept
	 * @return <code>true</code> if <code>aComponent</code> is a valid choice
	 *         for a focus owner;
	 *         otherwise <code>false</code>
	 */
	@Override
	protected boolean accept(Component aComponent)
	{
        // ingore combo box editors and deal with their parent
        // NOTE: this is risky code, but given the way this is currently done,
        // this is the best we can do short of looping to the root component
        // every time
		if (!super.accept(aComponent))
			return false;
        
        if (aComponent.getParent() instanceof JComboBox)
            aComponent = aComponent.getParent();

		//  TabbedPane
		if (aComponent instanceof JTabbedPane)
			return false;
        
		//  R/O Editors
		if (aComponent instanceof CEditor)
		{
			CEditor ed = (CEditor)aComponent;
			if (!ed.isReadWrite())
				return false;
			if (m_default	//	get Default Focus 
				&& ("AD_Client_ID".equals(aComponent.getName()) || "AD_Org_ID".equals(aComponent.getName()) ))
				return false;
		}
        
		//  Toolbar Buttons
		if (aComponent.getParent() instanceof JToolBar)
			return false;

		return true;
	}   //  accept

}   //  AFocusTraversalPolicy
