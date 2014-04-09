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
package org.compiere.db;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.compiere.plaf.*;
import org.compiere.swing.*;

/**
 *  Connection Editor.
 *
 *  @author     Jorg Janke
 *  @version    $Id: CConnectionEditor.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CConnectionEditor extends JComponent
	implements CEditor
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  Connection Editor creating new Connection
	 */
	public CConnectionEditor()
	{
		super();
		setName("ConnectionEditor");
		CConnectionEditor_MouseListener ml = new CConnectionEditor_MouseListener();
		//  Layout
		m_text.setEditable(false);
		m_text.setBorder(null);
		m_text.addMouseListener(ml);
		m_server.setIcon(new ImageIcon(getClass().getResource("Server16.gif")));
		m_server.setFocusable(false);
		m_server.setBorder(null);
		m_server.setOpaque(true);
		m_server.addMouseListener(ml);
		m_db.setIcon(new ImageIcon(getClass().getResource("Database16.gif")));
		m_db.setFocusable(false);
		m_db.setBorder(null);
		m_db.setOpaque(true);
		m_db.addMouseListener(ml);
		LookAndFeel.installBorder(this, "TextField.border");
		//
		setLayout(new BorderLayout(0,0));
		add(m_server, BorderLayout.WEST);
		add(m_text, BorderLayout.CENTER);
		add(m_db, BorderLayout.EAST);
	}   //  CConnectionEditor

	/** Text Element        */
	private JTextField  m_text = new JTextField(10);
	/** DB Button Element   */
	private JLabel      m_db = new JLabel ();
	/** Host Button Element */
	private JLabel      m_server = new JLabel();
	/** The Value           */
	CConnection 		m_value = null;
	/** ReadWrite           */
	boolean				m_rw = true;
	/** Mandatory           */
	private boolean     m_mandatory = false;

	/**
	 *	Enable Editor
	 *  @param rw true, if you can enter/select data
	 */
	public void setReadWrite (boolean rw)
	{
		m_rw = rw;
		setBackground(false);
	}   //  setReadWrite

	/**
	 *	Is it possible to edit
	 *  @return true, if editable
	 */
	public boolean isReadWrite()
	{
		return m_rw;
	}   //  isReadWrite

	/**
	 *	Set Editor Mandatory
	 *  @param mandatory true, if you have to enter data
	 */
	public void setMandatory (boolean mandatory)
	{
		m_mandatory = mandatory;
	}   //  setMandatory

	/**
	 *	Is Field mandatory
	 *  @return true, if mandatory
	 */
	public boolean isMandatory()
	{
		return m_mandatory;
	}   //  isMandatory

	/**
	 *  Set Background based on editable / mandatory / error
	 *  @param error if true, set background to error color, otherwise mandatory/editable
	 */
	public void setBackground (boolean error)
	{
		Color c = null;
		if (error)
			c = CompierePLAF.getFieldBackground_Error();
		else if (!m_rw)
			c = CompierePLAF.getFieldBackground_Inactive();
		else if (m_mandatory)
			c = CompierePLAF.getFieldBackground_Mandatory();
		else
			c = CompierePLAF.getFieldBackground_Normal();
		setBackground(c);
	}   //  setBackground

	/**
	 *  Set Background color
	 *  @param color
	 */
	@Override
	public void setBackground (Color color)
	{
		m_server.setBackground(color);
		m_text.setBackground(color);
		m_db.setBackground(color);
	}   //  setBackground

	/**
	 *	Set Editor to value
	 *  @param value value of the editor
	 */
	public void setValue (Object value)
	{
		if (value != null && value instanceof CConnection)
			m_value = (CConnection)value;
		setDisplay();
	}   //  setValue

	/**
	 *	Return Editor value
	 *  @return current value
	 */
	public Object getValue()
	{
		return m_value;
	}   //  getValue

	/**
	 *  Return Display Value
	 *  @return displayed String value
	 */
	public String getDisplay()
	{
		if (m_value == null)
			return "";
		return m_value.getName();
	}   //  getDisplay

	/**
	 *  Update Display with Connection info
	 */
	public void setDisplay()
	{
		m_text.setText(getDisplay());
		m_text.setCaretPosition(0);
		if (m_value == null)
			return;
		//  Text
		if (m_value.isAppsServerOK(false) || m_value.isDatabaseOK())
		{
			m_text.setForeground(CompierePLAF.getTextColor_OK());
			setBackground(false);
			if (!m_value.isAppsServerOK(false))
				m_server.setBackground(CompierePLAF.getFieldBackground_Error());
			if (!m_value.isDatabaseOK())
				m_db.setBackground(CompierePLAF.getFieldBackground_Error());
		}
		else
		{
			m_text.setForeground(CompierePLAF.getTextColor_Issue());
			setBackground(true);
		}
	}   //  setDisplay


	/**************************************************************************
	 *  Remove Action Listener
	 *  @param l
	 */
	public synchronized void removeActionListener(ActionListener l)
	{
		listenerList.remove(ActionListener.class, l);
	}   //  removeActionListener

	/**
	 *  Add Action Listener
	 *  @param l
	 */
	public synchronized void addActionListener(ActionListener l)
	{
	    listenerList.add(ActionListener.class, l);
	}   //  addActionListener

	/**
	 *  Fire Action Performed
	 */
	void fireActionPerformed()
	{
		ActionEvent e = null;
		ActionListener[] listeners = listenerList.getListeners(ActionListener.class);
        for (ActionListener element : listeners) {
       		if (e == null) 
       			e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "actionPerformed");
       		element.actionPerformed(e);
        }
	}   //  fireActionPerformed

	
	/**************************************************************************
	 *  Test Method
	 *  @param args
	 */
	public static void main(String[] args)
	{
	//	System.out.println("CConnectionEditor");
		JFrame frame = new JFrame("CConnectionEditor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getRootPane().getContentPane().add(new CConnectionEditor());
		CompierePLAF.showCenterScreen(frame);
	}   //  main


	/**
	 *  MouseListener
	 */
	public class CConnectionEditor_MouseListener extends MouseAdapter
	{
		/**
		 *  Mouse Clicked - Open Dialog
		 *  @param e
		 */
		@Override
		public void mouseClicked(MouseEvent e)
		{
			if (!isEnabled() || !m_rw || m_active)
				return;
			m_active = true;
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			//
			CConnectionDialog cd = new CConnectionDialog(m_value);
			setValue(cd.getConnection());
			fireActionPerformed();
			//
			setCursor(Cursor.getDefaultCursor());
			m_active = false;
		}   //  mouseClicked

		private boolean     m_active = false;
	}   //  CConnectionExitor_MouseListener

}   //  CConnectionEditor
