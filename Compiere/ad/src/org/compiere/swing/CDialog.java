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

import javax.swing.*;

import org.compiere.plaf.*;
import org.compiere.util.*;

/**
 *	Conveniance Dialog Class.
 *	Compiere Background + Dispose on Close  
 *  Implementing empty Action and Mouse Listener
 *	
 *  @author Jorg Janke
 *  @version $Id: CDialog.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CDialog extends JDialog 
	implements ActionListener, MouseListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	CDialog
	 *	@throws HeadlessException
	 */
	public CDialog() throws HeadlessException 
	{
		this((Frame)null, false);
	}
	/**
	 * 	CDialog
	 *	@param owner
	 *	@throws HeadlessException
	 */
	public CDialog(Frame owner) throws HeadlessException 
	{
		this (owner, false);
	}
	/**
	 * 	CDialog
	 *	@param owner
	 *	@param modal
	 *	@throws HeadlessException
	 */
	public CDialog(Frame owner, boolean modal) throws HeadlessException 
	{
		this (owner, null, modal);
	}
	/**
	 * 	CDialog
	 *	@param owner
	 *	@param title
	 *	@throws HeadlessException
	 */
	public CDialog(Frame owner, String title) throws HeadlessException 
	{
		this (owner, title, false);     
	}
	/**
	 * 	CDialog
	 *	@param owner
	 *	@param title
	 *	@param modal
	 *	@throws HeadlessException
	 */
	public CDialog(Frame owner, String title, boolean modal) throws HeadlessException 
	{
		super(owner, title, modal);
	}
	/**
	 * 	CDialog
	 *	@param owner
	 *	@param title
	 *	@param modal
	 *	@param gc
	 */
	public CDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) 
	{
		super(owner, title, modal, gc);
	}
	/**
	 * 	CDialog
	 *	@param owner
	 *	@throws HeadlessException
	 */
	public CDialog(Dialog owner) throws HeadlessException 
	{
		this (owner, false);
	}
	/**
	 * 	CDialog
	 *	@param owner
	 *	@param modal
	 *	@throws HeadlessException
	 */
	public CDialog(Dialog owner, boolean modal) throws HeadlessException 
	{
		this(owner, null, modal);
	}
	/**
	 * 	CDialog
	 *	@param owner
	 *	@param title
	 *	@throws HeadlessException
	 */
	public CDialog(Dialog owner, String title) throws HeadlessException 
	{
		this(owner, title, false);     
	}
	/**
	 * 	CDialog
	 *	@param owner
	 *	@param title
	 *	@param modal
	 *	@throws HeadlessException
	 */
	public CDialog(Dialog owner, String title, boolean modal) throws HeadlessException 
	{
		super(owner, title, modal);
	}
	/**
	 * 	CDialog
	 *	@param owner
	 *	@param title
	 *	@param modal
	 *	@param gc
	 *	@throws HeadlessException
	 */
	public CDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) throws HeadlessException 
    {
		super(owner, title, modal, gc);
	}

	/**
	 * 	Initialize.
	 * 	Install ALT-Pause
	 */
	@Override
	protected void dialogInit()
	{
		super.dialogInit();
		CompiereColor.setBackground(this);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(getTitle());	//	remove Mn
		//
		Container c = getContentPane();
		if (c instanceof JPanel)
		{
			JPanel panel = (JPanel)c;
			panel.getActionMap().put(ACTION_DISPOSE, s_dialogAction);
			panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(s_disposeKeyStroke, ACTION_DISPOSE);
		}
	}	//	init

	
	/**************************************************************************
	 *	@see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 *	@param e
	 */
	public void actionPerformed(ActionEvent e)
	{
	}

	/**
	 *	@see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 *	@param e
	 */
	public void mouseClicked(MouseEvent e)
	{
	}

	/**
	 *	@see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 *	@param e
	 */
	public void mouseEntered(MouseEvent e)
	{
	}

	/**
	 *	@see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 *	@param e
	 */
	public void mouseExited(MouseEvent e)
	{
	}

	/**
	 *	@see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 *	@param e
	 */
	public void mousePressed(MouseEvent e)
	{
	}

	/**
	 *	@see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 *	@param e
	 */
	public void mouseReleased(MouseEvent e)
	{
	}

	/**
	 * 	Set Title
	 *	@param title title
	 */
	@Override
	public void setTitle(String title)
	{
		if (title != null)
			title = Util.cleanMnemonic(title);
		super.setTitle(title);
	}	//	setTitle

	/** Dispose Action Name				*/
	protected static final String			ACTION_DISPOSE = "CDialogDispose";
	/**	Action							*/
	protected static final DialogAction	s_dialogAction = new DialogAction(ACTION_DISPOSE);
	/** ALT-EXCAPE						*/
	protected static final KeyStroke		s_disposeKeyStroke = 
		KeyStroke.getKeyStroke(KeyEvent.VK_PAUSE, InputEvent.ALT_MASK);
	
	/**
	 * 	Compiere Dialog Action
	 *	
	 *  @author Jorg Janke
	 *  @version $Id: CDialog.java 8244 2009-12-04 23:25:29Z freyes $
	 */
	static class DialogAction extends AbstractAction
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		DialogAction (String actionName)
		{
			super(actionName);
			putValue(Action.ACTION_COMMAND_KEY, actionName);
		}	//	DialogAction
		
		/**
		 * 	Action Listener
		 *	@param e event
		 */
		public void actionPerformed (ActionEvent e)
		{
			if (ACTION_DISPOSE.equals(e.getActionCommand()))
			{
				Object source = e.getSource();
				while (source != null)
				{
					if (source instanceof Window)
					{
						((Window)source).dispose();
						return;
					}
					if (source instanceof Container)
						source = ((Container)source).getParent();
					else
						source = null;
				}
			}
			else
				System.out.println("Action: " + e);
		}	//	actionPerformed
	}	//	DialogAction
	
}	//	CDialog
