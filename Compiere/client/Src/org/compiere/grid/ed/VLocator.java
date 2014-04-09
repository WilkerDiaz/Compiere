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

import org.compiere.apps.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Warehouse Locator Control
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: VLocator.java,v 1.5 2006/07/30 00:51:27 jjanke Exp $
 */
public class VLocator extends JComponent
	implements VEditor, ActionListener, FocusListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  IDE Constructor
	 */
	public VLocator ()
	{
		this("M_Locator_ID", false, false, true, null, 0);
	}   //  VLocator

	/**
	 *	Constructor
	 *
	 * 	@param columnName ColumnName
	 *	@param mandatory mandatory
	 *	@param isReadOnly read only
	 *	@param isUpdateable updateable
	 *	@param mLocator locator (lookup) model
	 * 	@param WindowNo window no
	 */
	public VLocator(String columnName, boolean mandatory, boolean isReadOnly, boolean isUpdateable,
		MLocatorLookup mLocator, int WindowNo)
	{
		super();
		super.setName(columnName);
		m_columnName = columnName;
		m_mLocator = mLocator;
		m_WindowNo = WindowNo;
		//
		LookAndFeel.installBorder(this, "TextField.border");
		this.setLayout(new BorderLayout());
		//  Size
		this.setPreferredSize(m_text.getPreferredSize());		//	causes r/o to be the same length
		int height = m_text.getPreferredSize().height;

		//	***	Button & Text	***
		m_text.setEditable(true);
		m_text.setFocusable(true);


        //  Popup
        m_text.addMouseListener(new MouseAdapter()
        {
            @Override
			public void mouseClicked(MouseEvent e)
            {
                if (SwingUtilities.isRightMouseButton(e))
                    m_popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
            }
        });
        
        String actionKey = m_text.getClass().getName() + "_popop";
        InputMap iMap = m_text.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_MASK);
        iMap.put(ks, actionKey);
        m_text.getActionMap().put(actionKey, new AbstractAction()
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
        
        m_text.addFocusListener( this );
        
		m_text.setFont(CompierePLAF.getFont_Field());
		m_text.setForeground(CompierePLAF.getTextColor_Normal());
		m_text.addActionListener(this);
		this.add(m_text, BorderLayout.CENTER);

		m_button.setIcon(new ImageIcon(org.compiere.Compiere.class.getResource("images/Locator10.gif")));
		m_button.setPreferredSize(new Dimension(height, height));
		m_button.addActionListener(this);
		this.add(m_button, BorderLayout.EAST);
		m_button.setFocusable(false);

		setBorder( null );

		//	Prefereed Size
		this.setPreferredSize(this.getPreferredSize());		//	causes r/o to be the same length

		//	ReadWrite
		if (isReadOnly || !isUpdateable)
			setReadWrite (false);
		else
			setReadWrite (true);
		setMandatory (mandatory);
		//
		mZoom = new CMenuItem(Msg.getMsg(Env.getCtx(), "Zoom"), Env.getImageIcon("Zoom16.gif"));
		mZoom.addActionListener(this);
		m_popupMenu.add(mZoom);
		mRefresh = new CMenuItem(Msg.getMsg(Env.getCtx(), "Refresh"), Env.getImageIcon("Refresh16.gif"));
		mRefresh.addActionListener(this);
		m_popupMenu.add(mRefresh);
	}	//	VLocator

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		m_text = null;
		m_button = null;
		m_mLocator = null;
		m_field = null;
	}   //  dispose

	private JTextField			m_text = new JTextField (VLookup.DISPLAY_LENGTH);
	private CButton				m_button = new CButton();
	private MLocatorLookup		m_mLocator;
	private Object				m_value;
	//
	private String				m_columnName;
	private int					m_WindowNo;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VLocator.class);
	//	Popup
	protected JPopupMenu 		m_popupMenu = new JPopupMenu();
	private CMenuItem 			mZoom;
	private CMenuItem 			mRefresh;

	/**
	 *	Enable/disable
	 *  @param value r/w
	 */
	public void setReadWrite (boolean value)
	{
		m_button.setReadWrite(value);
		if (m_button.isVisible() != value)
			m_button.setVisible(value);
		m_text.setEditable(value);
		m_text.setFocusable(value);
		setBackground(false);
	}	//	setReadWrite

	/**
	 *	IsReadWrite
	 *  @return true if ReadWrite
	 */
	public boolean isReadWrite()
	{
		return m_button.isReadWrite();
	}	//	isReadWrite

	/**
	 *	Set Mandatory (and back bolor)
	 *  @param mandatory true if mandatory
	 */
	public void setMandatory (boolean mandatory)
	{
		m_button.setMandatory(mandatory);
		setBackground(false);
	}	//	setMandatory

	/**
	 *	Is it mandatory
	 *  @return true if mandatory
	 */
	public boolean isMandatory()
	{
		return m_button.isMandatory();
	}	//	isMandatory

	/**
	 *	Set Background
	 *  @param color color
	 */
	@Override
	public void setBackground (Color color)
	{
		if (!color.equals(m_text.getBackground()))
			m_text.setBackground(color);
	}	//	setBackground

	/**
	 *  Set Background based on editable / mandatory / error
	 *  @param error if true, set background to error color, otherwise mandatory/editable
	 */
	public void setBackground (boolean error)
	{
		if (error)
			setBackground(CompierePLAF.getFieldBackground_Error());
		else if (!isReadWrite())
			setBackground(CompierePLAF.getFieldBackground_Inactive());
		else if (isMandatory())
			setBackground(CompierePLAF.getFieldBackground_Mandatory());
		else
			setBackground(CompierePLAF.getFieldBackground_Normal());
	}   //  setBackground

	/**
	 *  Set Foreground
	 *  @param fg color
	 */
	@Override
	public void setForeground(Color fg)
	{
		m_text.setForeground(fg);
	}   //  setForeground

	/**
	 * 	Request Focus
	 */
	@Override
	public void requestFocus()
	{
		m_text.requestFocus();
	}	//	requestFocus

	/**
	 * 	Request Focus In Window
	 *	@return focus request
	 */
	@Override
	public boolean requestFocusInWindow()
	{
		return m_text.requestFocusInWindow();
	}	//	requestFocusInWindow
	
	/**
	 * 	Get Focus Component
	 *	@return component
	 */
	public Component getFocusableComponent()
	{
		return m_text;
	}	//	getFocusComponent

	/**
	 *	Set Editor to value
	 *  @param value Integer
	 */
	public void setValue(Object value)
	{
		setValue (value, false);
	}	//	setValue

	/**
	 * 	Set Value
	 *	@param value value
	 *	@param fire data binding
	 */
	private void setValue (Object value, boolean fire)
	{
		if (value != null)
		{
			m_mLocator.setOnly_Warehouse_ID (getOnly_Warehouse_ID ());
			m_mLocator.setOnly_Product_ID(getOnly_Product_ID());
			if (!m_mLocator.isValid(value))
				value = null;
		}
		//
		m_value = value;
		m_text.setText(m_mLocator.getDisplay(value));	//	loads value

		//	Data Binding
		try
		{
			fireVetoableChange(m_columnName, null, value);
		}
		catch (PropertyVetoException pve)
		{
		}
	}	//	setValue


	/**
	 *  Property Change Listener
	 *  @param evt PropertyChangeEvent
	 */
	public void propertyChange (PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals(org.compiere.model.GridField.PROPERTY))
			setValue(evt.getNewValue());
	}   //  propertyChange

	/**
	 *	Return Editor value
	 *  @return value
	 */
	public Object getValue()
	{
		if (getM_Locator_ID() == 0)
			return null;
		return m_value;
	}	//	getValue
	
	/**
	 * 	Get M_Locator_ID
	 *	@return id
	 */
	public int getM_Locator_ID()
	{
		if (m_value != null 
			&& m_value instanceof Integer)
			return ((Integer)m_value).intValue();
		return 0;
	}	//	getM_Locator_ID

	/**
	 *  Return Display Value
	 *  @return display value
	 */
	public String getDisplay()
	{
		return m_text.getText();
	}   //  getDisplay

	
	
	
	/**
	 * Shows the VLocatorDialog dialog box
	 * @param only_Warehouse_ID
	 * @param only_Product_ID
	 */
	public void showDialog( int only_Warehouse_ID, int only_Product_ID )
	{
		//	 Button - Start Dialog
		int M_Locator_ID = 0;
		if (m_value instanceof Integer)
			M_Locator_ID = ((Integer)m_value).intValue();
		//
		m_mLocator.setOnly_Warehouse_ID(only_Warehouse_ID);		
		m_mLocator.setOnly_Product_ID(only_Product_ID);

		boolean IsReturnTrx = "Y".equals(Env.getCtx().getContext(m_WindowNo, "IsReturnTrx"));
		boolean IsSOTrx = Env.getCtx().isSOTrx(m_WindowNo);
		boolean isOnlyOutgoing = ((IsSOTrx && !IsReturnTrx) ||
								  (!IsSOTrx && IsReturnTrx))
								  && m_columnName.equals("M_Locator_ID");		
		m_mLocator.setOnly_Outgoing(isOnlyOutgoing);
		m_mLocator.refresh();
		VLocatorDialog ld = new VLocatorDialog(Env.getFrame(this),
			Msg.translate(Env.getCtx(), m_columnName),
			m_mLocator, M_Locator_ID, isMandatory(), only_Warehouse_ID);
		//	display
		ld.setVisible(true);
		m_mLocator.setOnly_Warehouse_ID(0);

		setValue (ld.getValue(), true);
		
	}
	
	
	
	/**
	 *	ActionListener
	 *  @param e ActionEvent
	 */
	public void actionPerformed(ActionEvent e)
	{
		//	Refresh
		if (e.getSource() == mRefresh)
		{
			m_mLocator.refresh();
			return;
		}

		//	Zoom to M_Warehouse
		if (e.getSource() == mZoom)
		{
			actionZoom();
			return;
		}

		//	Warehouse/Product
		int only_Warehouse_ID = getOnly_Warehouse_ID();
		int only_Product_ID = getOnly_Product_ID();
		log.config("Only Warehouse_ID=" + only_Warehouse_ID
			+ ", Product_ID=" + only_Product_ID);

		//	Text Entry ok
		if (e.getSource() == m_text )
		{
			validateText();
			return;
		}
		if( e.getSource() == m_button )
		{
			showDialog( only_Warehouse_ID, only_Product_ID );
			return;
		}
	}	//	actionPerformed

	/**
	 * 	Hit Enter in Text Field
	 * 	@param only_Warehouse_ID if not 0 restrict warehouse
	 * 	@param only_Product_ID of not 0 restricted product
	 * 	@return true if found
	 */
	private boolean actionText (int only_Warehouse_ID, int only_Product_ID)
	{
		
		String text = m_text.getText();
		log.fine(text);
		//	Null
		if (text == null || text.length() == 0)
		{
			if (isMandatory())
			{
				return false;
			}
			else
			{
				setValue (null, true);
				return true;
			}
		}
		
		m_mLocator.setOnly_Warehouse_ID(only_Warehouse_ID);
		m_mLocator.setOnly_Product_ID(only_Product_ID);
		
		boolean IsReturnTrx = "Y".equals(Env.getCtx().getContext(m_WindowNo, "IsReturnTrx"));
		boolean IsSOTrx = Env.getCtx().isSOTrx(m_WindowNo);
		boolean isOnlyOutgoing = ((IsSOTrx && !IsReturnTrx) ||
								  (!IsSOTrx && IsReturnTrx))
								  && m_columnName.equals("M_Locator_ID");		
		m_mLocator.setOnly_Outgoing(isOnlyOutgoing);

		m_mLocator.refresh();

		// Perform autocompletion.
		// If multiple hits are found, we'll use the original value in the field
		// if the display text has an exact match (not just the starting charaters)
		
		int matches = 0;
		int matched_M_Locator_ID = 0;
		int orig_m_value = 0;
		if( m_value instanceof Integer )
			orig_m_value = ((Integer) m_value).intValue();
		boolean matched_orig_m_value = false;
		
		for( KeyNamePair pp : m_mLocator.getData() )
		{
			if( text != null && 
				pp.getName().toUpperCase().startsWith( text.toUpperCase() ) )
			{
				matched_M_Locator_ID = pp.getKey();
				
				// this checks for an exact match with the original value
				if (pp.getName().equals( text ) && 
					orig_m_value == matched_M_Locator_ID )
					matched_orig_m_value = true;
				
				++matches;
			}
		}
		
		if( matches == 1 ) // unique match found
		{
			setValue( Integer.valueOf( matched_M_Locator_ID ), true );
			return true;
		}
		else if( matches > 1 && matched_orig_m_value ) // duplicate matches found
		{
			setValue( Integer.valueOf( orig_m_value ), true );
			return true;
		}
		return false;
	}	//	actionText

	/**
	 *  Action Listener Interface
	 *  @param listener listener
	 */
	public void addActionListener(ActionListener listener)
	{
		m_text.addActionListener(listener);
	}   //  addActionListener

	/**
	 *  Action Listener Interface
	 *  @param listener
	 */
	public void removeActionListener(ActionListener listener)
	{
		m_text.removeActionListener(listener);
	}   //  removeActionListener

	/**
	 *	Action - Zoom
	 */
	private void actionZoom()
	{
		int AD_Window_ID = 139;				//	hardcoded
		log.info("");
		//
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		AWindow frame = new AWindow();
		if (!frame.initWindow(AD_Window_ID, null))
			return;
		AEnv.showCenterScreen(frame);
		frame = null;
		setCursor(Cursor.getDefaultCursor());
	}	//	actionZoom

	/**
	 *  Set Field/WindowNo 
	 *  @param mField field
	 */
	public void setField (GridField mField)
	{
		if (mField != null)
			m_WindowNo = mField.getWindowNo();
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

	/**
	 * 	Get Warehouse restriction if any.
	 *	@return	M_Warehouse_ID or 0
	 */
	private int getOnly_Warehouse_ID()
	{
		Ctx ctx = Env.getCtx ();
		// gwu: do not restrict locators by warehouse when in Import Inventory Transactions window 
		String AD_Table_ID = ctx.getContext( m_WindowNo, "0|AD_Table_ID", true);
		if( "572".equals(AD_Table_ID ) ) // Import Inventory Transactions
		{
			return 0;
		}

		String only_Warehouse = ctx.getContext( m_WindowNo, "M_Warehouse_ID", true);
		int only_Warehouse_ID = 0;
		try
		{
			if (only_Warehouse != null && only_Warehouse.length () > 0)
				only_Warehouse_ID = Integer.parseInt (only_Warehouse);
		}
		catch (Exception ex)
		{
		}
		return only_Warehouse_ID;
	}	//	getOnly_Warehouse_ID

	/**
	 * 	Get Product restriction if any.
	 *	@return	M_Product_ID or 0
	 */
	private int getOnly_Product_ID()
	{
	//	if (!Env.isSOTrx(Env.getCtx(), m_WindowNo))
	//		return 0;	//	No product restrictions for PO
		//
		Ctx ctx = Env.getCtx ();
		// gwu: do not restrict locators by product when in Import Inventory Transactions window 
		String AD_Table_ID = ctx.getContext( m_WindowNo, "0|AD_Table_ID", true);
		if( "572".equals(AD_Table_ID ) ) // Import Inventory Transactions
		{
			return 0;
		}
		
		String only_Product = ctx.getContext( m_WindowNo, "M_Product_ID", true);
		int only_Product_ID = 0;
		try
		{
			if (only_Product != null && only_Product.length () > 0)
				only_Product_ID = Integer.parseInt (only_Product);
		}
		catch (Exception ex)
		{
		}
		return only_Product_ID;
	}	//	getOnly_Product_ID

	
	/**
	 * 
	 * @param e
	 */
	public void focusGained( FocusEvent e )
	{
		
	}	
	
	
	/**
	 * 
	 * @param e
	 */
	public void focusLost( FocusEvent e )
	{
		if( e.isTemporary() )
			return;
		
		//	Text Entry ok
		if (e.getSource() == m_text )
		{
			validateText();
		}
	}	

	/**
	 * Validates the text field to ensure that it contains a valid locator for the context.
	 * If not valid, the locator dialog is opened.
	 * Note that if the field contains a valid value, the dialog will not open even if
	 * there are duplicate duplicate locators with the same name.
	 */
	public void validateText()
	{
		//	Warehouse/Product
		int only_Warehouse_ID = getOnly_Warehouse_ID();
		int only_Product_ID = getOnly_Product_ID();
		log.config("Only Warehouse_ID=" + only_Warehouse_ID
			+ ", Product_ID=" + only_Product_ID);

		if( m_text.isEditable() &&
				!actionText(only_Warehouse_ID, only_Product_ID) )
		{
			showDialog( only_Warehouse_ID, only_Product_ID );
		}
	}
	
}	//	VLocator
