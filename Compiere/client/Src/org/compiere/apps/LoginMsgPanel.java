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
import java.awt.event.*;

import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 * 	Login Message Panel.
 * 	Displays single message
 *	@author Jorg Janke
 */
public class LoginMsgPanel extends CPanel implements ActionListener
{
	/** */
    private static final long serialVersionUID = 3996216993220088510L;

	/** Veto Property		*/
	public static final String USER_ACCEPTED = "USER_OK";

	/**
	 * 	Login Message Panel
	 * @param msg
	 */
	public LoginMsgPanel(MLoginMsg msg)
	{
		super(new BorderLayout());
		setName(msg.getName());
		m_msg = msg;
		//
		CTextPane center = new CTextPane();
		center.setReadWrite (false);
	//	center.setPreferredSize(new Dimension (400,80));
		center.setText(msg.getTextMsg());
		add(center, BorderLayout.CENTER);
		//
		String type = msg.getLoginMsgType();
		ConfirmPanel confirm = null;
		if (X_AD_LoginMsg.LOGINMSGTYPE_License.equals(type))
		{
			confirm = new ConfirmPanel(true);
			CButton ok = confirm.getOKButton();
			ok.setText(Msg.getMsg(Env.getCtx(), "Yes_I_Understand"));
		}
		else if (X_AD_LoginMsg.LOGINMSGTYPE_Confirmation.equals(type))
			confirm = new ConfirmPanel(true);
		else
			confirm = new ConfirmPanel(false);
		confirm.addActionListener(this);
		add(confirm, BorderLayout.SOUTH);
		//
		String addlInfo = msg.getAdditionalText(Env.getCtx().getAD_User_ID());
		if (!Util.isEmpty(addlInfo))
		{
			CTextArea addl = new CTextArea(addlInfo);
			addl.setReadWrite(false);
			add(addl, BorderLayout.NORTH);
		}
		//
		setPreferredSize(new Dimension(400,400));
	}	//	LoginMsgPanel

	/** The Model			*/
	private MLoginMsg		m_msg = null;
	/** The Model			*/
	private MLoginMsgLog	m_msgLog = null;

	/**
	 * 	Action Listener
	 * 	@param e event
	 */
    public void actionPerformed(ActionEvent e)
    {
    	Boolean vetoValue = Boolean.TRUE;
    	if (m_msgLog == null)
    	{
    		m_msgLog = new MLoginMsgLog(m_msg);
    		MSession sess = MSession.get(Env.getCtx());
    		if (sess != null)
    			m_msgLog.setAD_Session_ID(sess.getAD_Session_ID());
    		m_msgLog.setAD_User_ID(Env.getCtx().getAD_User_ID());
    	}
    	if (e.getActionCommand().equals(ConfirmPanel.A_OK))
    	{
    		m_msgLog.setIsActive(true);
    		m_msgLog.setIsUserAccepted(X_AD_LoginMsgLog.ISUSERACCEPTED_Yes);
    	}
    	else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
    	{
    		m_msgLog.setIsActive(false);
    		m_msgLog.setIsUserAccepted(X_AD_LoginMsgLog.ISUSERACCEPTED_No);
    		vetoValue = Boolean.FALSE;
    	}
    	else
    		m_msgLog.setIsActive(true);
    	//	Switch Tab or finish
    	try
    	{
    		if (m_msgLog.save())
    			fireVetoableChange(USER_ACCEPTED, null, vetoValue);
    		else
    			m_msgLog = null;
    	}
    	catch (Exception e2)
    	{
		}
    }	//	actionPerformed

	/**
	 * 	Used Rejected.
	 * 	Note that the user may not have been asked
	 *	@return true if the user was asked and rejected
	 */
	public boolean isUserRejected()
	{
		if (m_msgLog != null)
			return m_msgLog.isUserRejected();
		return false;
	}	//	isUserRejected

	/**
	 * 	User Saw and clicked something
	 *	@return true if log exists
	 */
	public boolean isUserSawIt()
	{
		return m_msgLog != null;
	}	//	isUserSawIt

}	//	LoginMsgPanel
