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
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import org.compiere.apps.*;
import org.compiere.apps.form.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Point of Sales Main Window.
 *
 *  @author Jorg Janke
 *  @version $Id: PosPanel.java,v 1.2 2006/07/30 00:51:27 jjanke Exp $
 */
public class PosPanel extends CPanel implements FormPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Constructor - see init 
	 */
	public PosPanel()
	{
		super (new GridBagLayout());
		m_focusMgr = new PosKeyboardFocusManager();
		KeyboardFocusManager.setCurrentKeyboardFocusManager(m_focusMgr);
	}	//	PosPanel

	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**	Logger				*/
	private CLogger			log = CLogger.getCLogger(getClass());
	/** Context				*/
	private Ctx				m_ctx = Env.getCtx();
	/** Sales Rep 			*/
	private int				m_SalesRep_ID = 0;
	/** POS Model			*/
	protected MPOS			p_pos = null;
	/** Keyoard Focus Manager		*/
	private PosKeyboardFocusManager	m_focusMgr = null;

	/**	Status Bar					*/
	protected StatusBar			f_status = new StatusBar();
	/** Customer Panel				*/
	protected SubBPartner 		f_bpartner = null;
	/** Sales Rep Panel				*/
	protected SubSalesRep 		f_salesRep = null;
	/** Current Line				*/
	protected SubCurrentLine 	f_currentLine = null;
	/** Product	Selection			*/
	protected SubProduct 		f_product = null;
	/** All Lines					*/
	protected SubLines 			f_lines = null;
	/** Function Keys				*/
	protected SubFunctionKeys 	f_functionKeys = null;
	/** Checkout					*/
	protected SubCheckout 		f_checkout = null;
    /** Basic Keys                  */
    protected SubBasicKeys      f_basicKeys = null; 
    /** South Panel                  */
    protected CPanel            m_southPanel = null; 
    /** South Panel                  */
    protected HashSet<Frame>    m_visibleFrames = new HashSet<Frame>(); 
    
    private CPanel queryPanel;
	
	//	Today's (login) date		*/
	private Timestamp			m_today = new Timestamp(m_ctx.getContextAsTime("#Date"));
	
	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame parent frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		m_SalesRep_ID = m_ctx.getAD_User_ID();
		log.info("init - SalesRep_ID=" + m_SalesRep_ID);
		m_WindowNo = WindowNo;
		m_frame = frame;
        m_frame.setUndecorated(true);
        m_frame.setResizable(false);
        
        WindowListener wl = new WindowAdapter()
        {
            private JFrame frame = m_frame;
            @Override
			public void windowActivated(WindowEvent we)
            {
                frame.removeWindowListener(this);
                fullScreen(true);
                final WindowListener wl = this;
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        frame.addWindowListener(wl);
                    }
                });
            }

            @Override
			public void windowDeactivated(WindowEvent we)
            {
                frame.removeWindowListener(this);
                fullScreen(false);
                final WindowListener wl = this;
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        frame.addWindowListener(wl);
                    }
                });
            }
        };
        m_frame.addWindowListener(wl);
        m_frame.setSize(800, 600);
        
		//
		try
		{
			if (!dynInit())
			{
				dispose();
				frame.dispose();
				return;
			}
			frame.getContentPane().add(this, BorderLayout.CENTER);
			frame.getContentPane().add(f_status, BorderLayout.SOUTH);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "init", e);
		}
		log.config( "PosPanel.init - " + getPreferredSize());
		m_focusMgr.start();
	}	//	init

    /**
     * @param fullScreen
     */
    private void fullScreen(boolean fullScreen)
    {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        if (gd.isFullScreenSupported())
        {
            for (Frame frame : Frame.getFrames())
            {
                if (frame != m_frame)
                {
                    if (fullScreen && frame.isVisible())
                    {
                        m_visibleFrames.add(frame);
                        frame.setVisible(false);
                    }
                    else if (!fullScreen && m_visibleFrames.contains(frame))
                    {
                        m_visibleFrames.remove(frame);
                        frame.setVisible(true);
                    }
                }
                else
                    frame.setVisible(fullScreen);
            }

            try {
                gd.setFullScreenWindow(null);
                if (fullScreen)
                {
                    gd.setFullScreenWindow(m_frame);
                    if (gd.isDisplayChangeSupported())
                    {
                        for (DisplayMode dm : gd.getDisplayModes())
                        {
                            if (dm.getWidth() == 800)
                            {
                                gd.setDisplayMode(dm);
                            }
                        }
                    }
                    m_frame.invalidate();
                    m_frame.repaint();
                }
            }
            catch (Exception e)
            {
                gd.setFullScreenWindow(null);
            }
        }
    }

	/**
	 * 	Dispose - Free Resources
	 */
	public void dispose()
	{
		if (m_focusMgr != null)
			m_focusMgr.stop();
		m_focusMgr = null;
		//
		if (f_bpartner != null)
			f_bpartner.dispose();
		f_bpartner = null;
		if (f_salesRep != null)
			f_salesRep.dispose();
		f_salesRep = null;
		if (f_currentLine != null)
			f_currentLine.dispose();
		f_currentLine = null;
		if (f_product != null)
			f_product.dispose();
		f_product = null;
		if (f_lines != null)
			f_lines.dispose();
		f_lines = null;
		if (f_functionKeys != null)
			f_functionKeys.dispose();
		f_functionKeys = null;
		if (f_checkout != null)
			f_checkout.dispose();
		f_checkout = null;
		if (f_basicKeys != null)
			f_basicKeys.dispose();
		f_basicKeys = null;
		m_ctx = null;
	}	//	dispose

	
	/**************************************************************************
	 * 	Dynamic Init.
	 * 	PosPanel has a GridBagLayout.
	 * 	The Sub Panels return their position
	 */
	private boolean dynInit()
	{
		if (!setMPOS())
			return false;
		
		//	Create Sub Panels
        CPanel panel = new CPanel(new GridBagLayout());
		f_bpartner = new SubBPartner (this);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = .72;
        panel.add (f_bpartner, gbc);

		f_salesRep = new SubSalesRep (this);
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        gbc.weightx = .28;
        panel.add (f_salesRep, gbc);

		f_currentLine = new SubCurrentLine (this);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = .72;
        panel.add (f_currentLine, gbc);

		f_product = new SubProduct (this);
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        gbc.weightx = .28;
        panel.add (f_product, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = GridBagConstraints.RELATIVE;
        add(panel, gbc);

        m_southPanel = new CPanel(new GridBagLayout());
		f_lines = new SubLines (this);
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = .72;
        m_southPanel.add (f_lines, gbc);

		f_functionKeys = new SubFunctionKeys (this);
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        gbc.weightx = .28;
        m_southPanel.add (f_functionKeys, gbc);

		f_checkout = new SubCheckout (this);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = .72;
        m_southPanel.add (f_checkout, gbc);

		f_basicKeys = new SubBasicKeys (this);
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        gbc.weightx = .28;
        m_southPanel.add (f_basicKeys, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        add(m_southPanel, gbc);

		newOrder();
		return true;
	}	//	dynInit

	/**
	 * 	Set MPOS
	 *	@return true if found/set
	 */
	private boolean setMPOS()
	{
		MPOS[] poss = null;
		if (m_SalesRep_ID == 100)	//	superUser
			poss = getPOSs (0);
		else
			poss = getPOSs (m_SalesRep_ID);
		//
		if (poss.length == 0)
		{
			ADialog.error(m_WindowNo, m_frame, "NoPOSForUser");
			return false;
		}
		else if (poss.length == 1)
		{
			p_pos = poss[0];
			return true;
		}

		//	Select POS
		String msg = Msg.getMsg(m_ctx, "SelectPOS");
		String title = Env.getHeader(m_ctx, m_WindowNo);
		Object selection = JOptionPane.showInputDialog(m_frame, msg, title, 
			JOptionPane.QUESTION_MESSAGE, null, poss, poss[0]);
		if (selection != null)
		{
			p_pos = (MPOS)selection;;
			return true;
		}
		return false;
	}	//	setMPOS
	
	/**
	 * 	Get POSs for specific Sales Rep or all
	 *	@param SalesRep_ID
	 *	@return array of POS
	 */
	private MPOS[] getPOSs (int SalesRep_ID)
	{
		ArrayList<MPOS> list = new ArrayList<MPOS>();
		String sql = "SELECT * FROM C_POS WHERE SalesRep_ID=?";
		if (SalesRep_ID == 0)
			sql = "SELECT * FROM C_POS WHERE AD_Client_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			if (SalesRep_ID != 0)
				pstmt.setInt (1, m_SalesRep_ID);
			else
				pstmt.setInt (1, m_ctx.getAD_Client_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MPOS(m_ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		MPOS[] retValue = new MPOS[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getPOSs

	
	/**
	 * 	Set Visible
	 *	@param aFlag visible
	 */
	@Override
	public void setVisible (boolean aFlag)
	{
		super.setVisible (aFlag);
		f_product.f_name.requestFocus();
	}	//	setVisible

	
	/**
	 * 	Open Query Window
	 *	@param panel
	 */
	public void openQuery(CPanel panel)
	{
        if (queryPanel != null)
            closeQuery();
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.gridy = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        
        add (panel, gbc);
        queryPanel = panel;
        
        m_southPanel.setVisible(false);
	}	//	closeQuery

	/**
	 * 	Close Query Window
	 */
	public void closeQuery ()
	{
        if (queryPanel != null)
            remove(queryPanel);
        
        m_southPanel.setVisible(true);
	}	//	closeQuery

	/**************************************************************************
	 * 	Get Today's date
	 *	@return date
	 */
	public Timestamp getToday()
	{
		return m_today;
	}	//	getToday
	
	/**
	 * 	New Order
	 */
	public void newOrder()
	{
		log.info( "PosPabel.newOrder");
		f_bpartner.setC_BPartner_ID(0);
		f_currentLine.newLine();
		f_product.f_name.requestFocus();
	}	//	newOrder
	
}	//	PosPanel

