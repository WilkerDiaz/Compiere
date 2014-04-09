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
import java.beans.*;
import java.util.*;

import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 * 	Login Message Dialog
 *	@author Jorg Janke
 */
public class LoginMsgDialog extends CDialog implements VetoableChangeListener
{
	/** */
    private static final long serialVersionUID = -8716224744383021489L;

	/**
	 * 	Login Message Dialog.
	 * 	The method init() loads the data and sets the layout
	 * 	@param parent parent
	 */
	public LoginMsgDialog(Frame parent)
	{
		super(parent, Msg.getMsg(Env.getCtx(), "News"), true);
		
	}	//	LoginMsgDialog
	
	private CPanel mainPanel = new CPanel(new BorderLayout());
	private CTabbedPane tabPane = new CTabbedPane();
	//
	private ArrayList<LoginMsgPanel> m_tabs = new ArrayList<LoginMsgPanel>();

	/**
	 * 	Initialize Messages
	 *	@return true if messages to display
	 */
	public boolean init()
	{
		Ctx ctx = Env.getCtx();
		ArrayList<MLoginMsg> msgs = MLoginMsg.getForUser(ctx, ctx.getAD_User_ID());
		if (msgs == null || msgs.size() == 0)
			return false;
		//
		for (int i = 0; i < msgs.size(); i++)
        {
			MLoginMsg msg = msgs.get(i);
			LoginMsgPanel panel = new LoginMsgPanel(msg);
			tabPane.insertTab(msg.getName(), null, panel, msg.getDescription(), i);
			m_tabs.add(panel);
			panel.addVetoableChangeListener(this);
        }
		this.getContentPane().add(mainPanel);
		mainPanel.add(tabPane, BorderLayout.CENTER);
		mainPanel.setName("loginMsgMainPanel");
	//	mainPanel.add(southPanel, BorderLayout.SOUTH);
		return true;
	}	//	init

	/**
	 * 	User Rejected
	 *	@return true if user rejected
	 */
	public boolean isUserRejected()
	{
		boolean userRejected = false;
		for (LoginMsgPanel tab : m_tabs)
        {
			if (tab.isUserRejected())
				userRejected = true;
        }
		return userRejected;
	}	//	isUserRejected

	/**
	 * 	User OK'ed
	 *	@return true if user did not reject and saw all
	 */
	public boolean isUserOK()
	{
		boolean userOKed = true;
		boolean userRejected = false;
		for (LoginMsgPanel tab : m_tabs)
        {
			if (!tab.isUserSawIt())
				userOKed = false;
			if (tab.isUserRejected())
				userRejected = true;
        }
		return userOKed && !userRejected;
	}	//	isUserOKed

	/**
	 * 	Veto Listener
	 */
    public void vetoableChange(PropertyChangeEvent evt)
        throws PropertyVetoException
    {
    	if (LoginMsgPanel.USER_ACCEPTED.equals(evt.getPropertyName()))
   		{
    		Boolean result = (Boolean)evt.getNewValue();
    		if (result.booleanValue())
    		{
    			int selIndex = tabPane.getSelectedIndex();
    			int maxIndex = tabPane.getTabCount() - 1;
    			if (selIndex < maxIndex)
    			{
    				tabPane.setSelectedIndex(selIndex + 1);
    				return;
    			}
    		}
   			dispose();
   		}
    }	//	vetoableChange

}	//	ALoginMsgDialog
