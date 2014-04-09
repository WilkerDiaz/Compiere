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
package org.compiere.util;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import org.compiere.plaf.*;
import org.compiere.swing.*;

/**
 *  Init Dialog
 *
 *  @author     Jorg Janke
 *  @version    $Id: IniDialog.java 8244 2009-12-04 23:25:29Z freyes $
 */
public final class IniDialog extends JDialog implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  Display License and exit if rejected
	 *  @return true if accepted - otherwise will Stop System!
	 */
	public static final boolean accept()
	{
		IniDialog id = new IniDialog(null, null);
		if (id.isAccepted())
		{
			log.info("License Accepted");
			return true;
		}
		System.exit(10);
		return false;       //  never executed.
	}   //  accept

	/**
	 * 	Accept License
	 *	@param license license
	 *	@return true if accepted
	 */
	public static final boolean accept(String heading, String license)
	{
		IniDialog id = new IniDialog(heading, license);
		if (id.isAccepted())
		{
			log.info("License Accepted");
			return true;
		}
		return false;
	}   //  accept

	
	/**************************************************************************
	 *  Constructor
	 *  @param heading optional heading
	 *  @param license optional license
	 */
	private IniDialog(String heading, String license)
	{
		super();
		try
		{
			if (heading == null)
				heading = "Compiere";
			jbInit(heading);
			if (license != null)
			{
				licensePane.setText(license);
			}
			else
			{
				String where = "org/compiere/license.html";
				URL url = null;
				ClassLoader cl = getClass().getClassLoader();
				if (cl != null)	//	Bootstrap
					url = cl.getResource(where);
				if (url == null)
				{
					log.warning("No license in resource ");
					url = new URL("http://www.compiere.org/license.htm");
				}
				if (url == null)
				{
					cmd_reject();
					dispose();
					return;
				}
				else
					licensePane.setPage(url);
			}
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "init", ex);
			cmd_reject();
			dispose();
			return;
		}
		CompierePLAF.showCenterScreen(this);
	}   //  IniDialog

	/** Translation     	*/
	static ResourceBundle   s_res = ResourceBundle.getBundle("org.compiere.translate.IniRes");
	/** License Accepted	*/
	private boolean         m_accept = false;
	/**	Logger				*/
	private static Logger	log	= Logger.getLogger (IniDialog.class.getName());
	

	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel southPanel = new CPanel();
	private JButton bReject = CompierePLAF.getCancelButton();
	private JButton bAccept = CompierePLAF.getOKButton();
	private FlowLayout southLayout = new FlowLayout();
	private JLabel southLabel = new JLabel();
	private CTextPane licensePane = new CTextPane();

	/**
	 *  Static Layout
	 *  @throws Exception
	 */
	private void jbInit(String heading) throws Exception
	{
		setTitle(heading + " - " + s_res.getString("Compiere_License"));
		southLabel.setText(s_res.getString("Do_you_accept"));
		bReject.setText(s_res.getString("No"));
		bAccept.setText(s_res.getString("Yes_I_Understand"));
		//
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setModal(true);
		//
		mainPanel.setLayout(mainLayout);
		bReject.setForeground(Color.red);
		bReject.addActionListener(this);
		bAccept.addActionListener(this);
		southPanel.setLayout(southLayout);
		southLayout.setAlignment(FlowLayout.RIGHT);
		licensePane.setEditable(false);
		licensePane.setContentType("text/html");
		licensePane.setPreferredSize(new Dimension(700, 400));
		southPanel.add(southLabel, null);
		getContentPane().add(mainPanel);
		mainPanel.add(licensePane, BorderLayout.CENTER);
		mainPanel.add(southPanel,  BorderLayout.SOUTH);
		southPanel.add(bReject, null);
		southPanel.add(bAccept, null);
	}   //  jbInit

	/**
	 * ActionListener
	 * @param e event
	 */
	public final void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bAccept)
			m_accept = true;
		dispose();
	}   //  actionPerformed

	/**
	 *  Dispose
	 */
	@Override
	public final void dispose()
	{
		super.dispose();
		if (!m_accept)
			cmd_reject();
	}   //  dispose

	/**
	 *  Is Accepted
	 *  @return true if accepted
	 */
	public final boolean isAccepted()
	{
		return m_accept;
	}   //  isAccepted

	/**
	 *  Reject License
	 */
	public final void cmd_reject()
	{
		String info = "License rejected or expired";
		try
		{
			info = s_res.getString("License_rejected");
		}
		catch (Exception e)
		{
		}
		log.severe(info);
		m_accept = false;
	}   //  cmd_reject

}   //  IniDialog 
