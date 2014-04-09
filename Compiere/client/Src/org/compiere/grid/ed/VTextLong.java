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

import javax.swing.*;

import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Long Text (CBLOB) Editor 	
 *	
 *  @author Jorg Janke
 *  @version $Id: VTextLong.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class VTextLong extends CTextPane
	implements VEditor, KeyListener, ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Factory: Start Editor
	 *	@param jc container to get parent frame
	 *	@param header heading
	 *	@param text initial text
	 *	@param editable if false = r/o
	 *	@return edited string
	 */
	public static String startEditor(Container jc, String header, String text, boolean editable)
	{
		//	Find frame
		JFrame frame = Env.getFrame(jc);
		//	Start it
		HTMLEditor ed = new HTMLEditor (frame, header, text, editable);
		String s = ed.getHtmlText();
		ed = null;
		return s;
	}	//	startEditor

	/**
	 *	Standard Constructor
	 *  @param columnName column name
	 *  @param mandatory mandatory
	 *  @param isReadOnly read only
	 *  @param isUpdateable updateable
	 *  @param displayLength display length
	 *  @param fieldLength field length
	 */
	public VTextLong (String columnName, boolean mandatory, boolean isReadOnly, boolean isUpdateable,
		int displayLength, int fieldLength)
	{
		super ();
		super.setName(columnName);
		LookAndFeel.installBorder(this, "TextField.border");
		setPreferredSize(new Dimension (500,80));

		//  Create Editor
		setForeground(CompierePLAF.getTextColor_Normal());
		setBackground(CompierePLAF.getFieldBackground_Normal());

		setMandatory(mandatory);
		m_columnName = columnName;

		if (isReadOnly || !isUpdateable)
			setReadWrite(false);
		addKeyListener(this);

        //  Popup
        addMouseListener(new MouseAdapter()
        {
            @Override
			public void mouseClicked(MouseEvent e)
            {
                if (SwingUtilities.isRightMouseButton(e))
                    m_popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
            }
        });
        
        String actionKey = getClass().getName() + "_popop";
        InputMap iMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_MASK);
        iMap.put(ks, actionKey);
        getActionMap().put(actionKey, new AbstractAction()
        {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e)
            {
                Component comp = (Component)e.getSource();
                m_popupMenu.show(comp, 10, 10);
            }
        });
        
		menuEditor = new CMenuItem(Msg.getMsg(Env.getCtx(), "Editor"), Env.getImageIcon("Editor16.gif"));
		menuEditor.addActionListener(this);
		m_popupMenu.add(menuEditor);
	}	//	VText

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		m_field = null;
	}   //  dispose

	JPopupMenu          		m_popupMenu = new JPopupMenu();
	private CMenuItem 			menuEditor;

	private String				m_columnName;
	private String				m_oldText;
	private String				m_initialText;
	private volatile boolean	m_setting = false;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VTextLong.class);

	/**
	 *	Set Editor to value
	 *  @param value value
	 */
	@Override
	public void setValue(Object value)
	{
		if (value == null)
			m_oldText = "";
		else
			m_oldText = value.toString();
		if (m_setting)
			return;
		super.setValue(m_oldText);
		m_initialText = m_oldText;
		//	Always position Top 
		setCaretPosition(0);
	}	//	setValue

	/**
	 *  Property Change Listener
	 *  @param evt event
	 */
	public void propertyChange (PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals(org.compiere.model.GridField.PROPERTY))
			setValue(evt.getNewValue());
	}   //  propertyChange

	/**
	 *	ActionListener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		log.finest(e.getActionCommand());
		if (e.getSource() == menuEditor)
		{
			menuEditor.setEnabled(false);
			String s = VTextLong.startEditor (this, Msg.translate(Env.getCtx(), m_columnName), getText(), isEditable());
			menuEditor.setEnabled(true);
			//
			//	Data Binding
			try
			{
				fireVetoableChange(m_columnName, m_oldText, s);
			}
			catch (PropertyVetoException pve)	{}
		}
	}	//	actionPerformed

	/**
	 *  Action Listener Interface - NOP
	 *  @param listener listener
	 */
	public void addActionListener(ActionListener listener)
	{
	}   //  addActionListener

	/**
	 *  Action Listener Interface
	 *  @param listener
	 */
	public void removeActionListener(ActionListener listener)
	{
	}   //  removeActionListener

	/**************************************************************************
	 *	Key Listener Interface
	 *  @param e event
	 */
	public void keyTyped(KeyEvent e)	{}
	public void keyPressed(KeyEvent e)	{}

	/**
	 * 	Key Released
	 *	if Escape restore old Text.
	 *  @param e event
	 */
	public void keyReleased(KeyEvent e)
	{
		//  ESC
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			setText(m_initialText);
		m_setting = true;
		try
		{
			fireVetoableChange(m_columnName, m_oldText, getText());
		}
		catch (PropertyVetoException pve)	{}
		m_setting = false;
	}	//	keyReleased

	/**
	 *  Set Field/WindowNo 
	 *  @param mField field
	 */
	public void setField (GridField mField)
	{
		m_field = mField;
	}   //  setField

	/** Grid Field				*/
	private GridField 	m_field = null;
	
	/**
	 *  Get Field
	 *  @return gridField
	 */
	public GridField getField()
	{
		return m_field;
	}   //  getField

}	//	VTextLong
