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
package org.compiere.pos;

import java.awt.*;
import java.awt.event.*;

import javax.swing.border.*;

import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *	POS Function Key Sub Panel
 *	
 *  @author Jorg Janke
 *  @version $Id: SubFunctionKeys.java,v 1.2 2006/07/30 00:51:27 jjanke Exp $
 */
public class SubFunctionKeys extends PosSubPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Constructor
	 *	@param posPanel POS Panel
	 */
	public SubFunctionKeys (PosPanel posPanel)
	{
		super (posPanel);
	}	//	PosSubFunctionKeys
	
	/**	Keys				*/
	private MPOSKey[] 	m_keys;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(SubFunctionKeys.class);
	
	/**
	 * 	Initialize
	 */
	@Override
	public void init()
	{
		//	Title
		TitledBorder border = new TitledBorder(Msg.translate(Env.getCtx(), "C_POSKeyLayout_ID"));
		setBorder(border);
		
		int C_POSKeyLayout_ID = p_pos.getC_POSKeyLayout_ID();
		if (C_POSKeyLayout_ID == 0)
			return;
		MPOSKeyLayout fKeys = MPOSKeyLayout.get(Env.getCtx(), C_POSKeyLayout_ID);
		if (fKeys.get_ID() == 0)
			return;
		
		int COLUMNS = 3;	//	Min Columns
		int ROWS = 3;		//	Min Rows
		m_keys = fKeys.getKeys(false);
		int noKeys = m_keys.length;
		int rows = Math.max (((noKeys-1) / COLUMNS) + 1, ROWS);
		int cols = ((noKeys-1) % COLUMNS) + 1;
		log.fine( "PosSubFunctionKeys.init - NoKeys=" + noKeys 
			+ " - Rows=" + rows + ", Cols=" + cols);
		//	Content
		CPanel content = new CPanel (new GridLayout(Math.max(rows, 3), Math.max(cols, 3)));
		for (int i = 0; i < m_keys.length; i++)
		{
			MPOSKey key = m_keys[i];
			StringBuffer buttonHTML = new StringBuffer("<html><p>");
			if (key.getAD_PrintColor_ID() != 0)
			{
				MPrintColor color = MPrintColor.get(Env.getCtx(), key.getAD_PrintColor_ID());
				buttonHTML
					.append("<font color=#")
					.append(color.getRRGGBB())
					.append(">")
					.append(key.getName())
					.append("</font>");
			}
			else
				buttonHTML.append(key.getName());
			buttonHTML.append("</p></html>");
			log.fine( "#" + i + " - " + buttonHTML); 
			CButton button = new CButton(buttonHTML.toString());
			button.setMargin(INSETS1);
			button.setFocusable(false);
			button.setActionCommand(String.valueOf(key.getC_POSKey_ID()));
			button.addActionListener(this);
			content.add (button);
		}
		for (int i = m_keys.length; i < rows*COLUMNS; i++)
		{
			CButton button = new CButton("");
			button.setFocusable(false);
			content.add (button);
		}
		content.setPreferredSize(new Dimension(cols*70, rows*50));
		add (content);
	}	//	init

	/**
	 * 	Action Listener
	 *	@param e event
	 */
	@Override
	public void actionPerformed (ActionEvent e)
	{
		String action = e.getActionCommand();
		if (action == null || action.length() == 0 || m_keys == null)
			return;
		log.info( "PosSubFunctionKeys - actionPerformed: " + action);
		try
		{
			int C_POSKey_ID = Integer.parseInt(action);
			for (MPOSKey key : m_keys) {
				if (key.getC_POSKey_ID() == C_POSKey_ID)
				{
					p_posPanel.f_product.setM_Product_ID(key.getM_Product_ID());
					p_posPanel.f_product.setPrice();
					p_posPanel.f_currentLine.setQty(key.getQty());
					p_posPanel.f_currentLine.saveLine();
					return;
				}
			}
		}
		catch (Exception ex)
		{
		}
	}	//	actinPerformed
	
}	//	PosSubFunctionKeys
