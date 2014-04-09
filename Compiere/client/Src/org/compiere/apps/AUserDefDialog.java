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
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import org.compiere.grid.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 * 	User Defined Window Dialog
 *	
 *  @author Jorg Janke
 *  @version $Id$
 */
public class AUserDefDialog extends CDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	User Def Window Dialog
	 * 	@param owner frame
	 *	@param AD_Window_ID window
	 *	@param WindowNo no
	 */
	public AUserDefDialog(JFrame owner, int AD_Window_ID, int WindowNo)
	{
		super(owner, Msg.getMsg(Env.getCtx(), "UserDefDialog"), true);
		//
		m_AD_Window_ID = AD_Window_ID;
		if (!init(WindowNo))
			dispose();
		else
		{
			AEnv.positionCenterWindow(owner, this);
			setVisible(true);
		}
	}	//	AUserDefDialog
	
	/**	Logger	*/
    private static CLogger log = CLogger.getCLogger(AUserDefDialog.class);
	
	/**	Window				*/
	private int				m_AD_Window_ID;
	private int				m_AD_Client_ID;
	private MRole 			m_role;
	private Dimension 		m_size;
	
	/**	Existing			*/
	private MUserDefWin[]	m_userDefs = null;
	
	private CPanel mainPanel = new CPanel(new BorderLayout());
	private CPanel selectPanel = new CPanel(new GridBagLayout());
	private CCheckBox fUpdateExisting = new CCheckBox("Update Existing");
	private CComboBox fValues = null;
	private CLabel lPrefix = new CLabel(Msg.getMsg(Env.getCtx(), "Prefix"));
	private CTextField fPrefix = new CTextField(8);
	private CLabel lLevel = new CLabel(Msg.getMsg(Env.getCtx(), "Level"));
	private CComboBox fLevel = null;
	private CCheckBox fSaveSize = new CCheckBox("Save Window Size");
	private ConfirmPanel confirmPanel = new ConfirmPanel();

	private String[]		m_levels;
	private final static int	I_SYSTEM = 0;
	private final static int	I_CLIENT = 1;
	private final static int	I_ROLE = 2;
	private final static int	I_USER = 3;
	
	/**
	 * 	Init
	 *	@param WindowNo
	 *	@return true if ok
	 */
	private boolean init(int WindowNo)
	{
		Ctx ctx = Env.getCtx();
		m_role = MRole.getDefault();
		String custLevel = m_role.getWinUserDefLevel();
		
		if (custLevel.equals(X_AD_Role.WINUSERDEFLEVEL_None))
		{
			ADialog.error(WindowNo, this, "UserDefDialogNoAccess");
			return false;
		}

		//	Options
		m_AD_Client_ID = ctx.getAD_Client_ID(); 
		MUser user = MUser.get(ctx, ctx.getAD_User_ID());
		m_levels = new String[] {"System",
			Msg.getElement(ctx, "AD_Client_ID"),
			m_role.getName(), user.getName()};
		//
		int	AD_Role_ID = 0;
		int AD_User_ID = 0;
		Vector<String> levelItems = new Vector<String>();
		if (custLevel.equals(X_AD_Role.WINUSERDEFLEVEL_UserOnly))
		{
			AD_User_ID = ctx.getAD_User_ID();
			levelItems.add(m_levels[I_USER]);
		}
		else if (custLevel.equals(X_AD_Role.WINUSERDEFLEVEL_RoleOrUser))
		{
			AD_User_ID = ctx.getAD_User_ID();
			AD_Role_ID = m_role.getAD_Role_ID();
			levelItems.add(m_levels[I_ROLE]);
			levelItems.add(m_levels[I_USER]);
		}
		else if (custLevel.equals(X_AD_Role.WINUSERDEFLEVEL_TenantOrRoleOrUser))
		{
			if ("Y".equals(ctx.getContext( "#SysAdmin")))
				levelItems.add("System");
			levelItems.add(m_levels[I_CLIENT]);
			levelItems.add(m_levels[I_ROLE]);
			levelItems.add(m_levels[I_USER]);
		}
		m_userDefs = MUserDefWin.get(ctx, m_AD_Window_ID,
			m_AD_Client_ID, AD_Role_ID, AD_User_ID);
		fLevel = new CComboBox(levelItems);
		fValues = new CComboBox(m_userDefs); 
		if (m_userDefs.length == 0)
		{
			fUpdateExisting.setSelected(false);
			fUpdateExisting.setEnabled(false);
		}
		else
		{
			fUpdateExisting.setSelected(true);
			fUpdateExisting.addActionListener(this);
		}
		//	Initial
		boolean update = fUpdateExisting.isSelected();
		fValues.setReadWrite(update);
		fLevel.setReadWrite(!update);
		fPrefix.setReadWrite(!update);

		//	Layout
		Insets in = new Insets(2, 2, 2, 2);
		selectPanel.add(fUpdateExisting, new GridBagConstraints(0, 0,
			1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
			in, 0,0));
		selectPanel.add(fValues, new GridBagConstraints(1, 0,
			1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
			in, 0,0));
		selectPanel.add(lLevel, new GridBagConstraints(0, 1,
			1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
			in, 0,0));
		selectPanel.add(fLevel, new GridBagConstraints(1, 1,
			1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
			in, 0,0));
		selectPanel.add(lPrefix, new GridBagConstraints(0, 2,
			1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
			in, 0,0));
		selectPanel.add(fPrefix, new GridBagConstraints(1, 2,
			1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
			in, 0,0));
		selectPanel.add(fSaveSize, new GridBagConstraints(1, 3,
			1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
			in, 0,0));
		//
		mainPanel.add(selectPanel, BorderLayout.CENTER);
		mainPanel.add(confirmPanel, BorderLayout.SOUTH);
		getContentPane().add(mainPanel);
		//
		confirmPanel.addActionListener(this);
		return true;
	}	//	init
	
	
	/**
	 * 	Action Performed
	 *	@param e event
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		log.info(e.toString());
		if (e.getSource() == fUpdateExisting)
		{
			boolean update = fUpdateExisting.isSelected();
			fValues.setReadWrite(update);
			fLevel.setReadWrite(!update);
			fPrefix.setReadWrite(!update);
		}
		else if (e.getActionCommand().equals(ConfirmPanel.A_OK))
		{
			dispose();
		}
		else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
		{
			dispose();
			m_userDefs = null;
		}
	}	//	actionPerformed
	
	
	/**************************************************************************
	 * 	Save
	 *	@param AD_Tab_ID
	 *	@param vt Table
	 *	@param size window size
	 *	@return true if saved
	 */
	public boolean save(int AD_Tab_ID, VTable vt, Dimension size)
	{
		if (m_userDefs == null)		//	pressed cancel
			return false;
		//
		m_size = size;
		Ctx ctx = Env.getCtx();
		if (fUpdateExisting.isSelected())
		{
			MUserDefWin selected = (MUserDefWin)fValues.getSelectedItem();
			return update (selected, AD_Tab_ID, vt);
		}
		//	System
		Object level = fLevel.getSelectedItem();
		if (m_levels[I_SYSTEM].equals(level))
			return updateSystem (AD_Tab_ID, vt); 
		//	New
		MUserDefWin uDefWin = null;
		String prefix = fPrefix.getText();
		if (m_levels[I_CLIENT].equals(level))
			uDefWin = MUserDefWin.create(ctx, m_AD_Client_ID, m_AD_Window_ID, prefix, 0, 0); 
		else if (m_levels[I_ROLE].equals(level))
			uDefWin = MUserDefWin.create(ctx, m_AD_Client_ID, m_AD_Window_ID, 
				prefix, m_role.getAD_Role_ID(), 0); 
		else if (m_levels[I_USER].equals(level))
			uDefWin = MUserDefWin.create(ctx, m_AD_Client_ID, m_AD_Window_ID, 
				prefix, 0, ctx.getAD_User_ID()); 
		return update(uDefWin, AD_Tab_ID, vt);
	}	//	save

	/**
	 * 	Update existing record
	 *	@param uDefWin user defined
	 *	@param AD_Tab_ID tab to be updated
	 *	@param vt Table
	 *	@return true if saved
	 */
	private boolean update (MUserDefWin uDefWin, int AD_Tab_ID, VTable vt)
	{
		log.info("AD_Tab_ID=" + AD_Tab_ID);
		if (fSaveSize.isSelected())
			uDefWin.setWindowSize(m_size);
		else
			uDefWin.setWindowSize(null);
		uDefWin.save();
		
		MUserDefTab uDefTab = uDefWin.getTab(AD_Tab_ID);
		
		TableColumnModel tcm = vt.getColumnModel();
		int cs = tcm.getColumnCount();
		for (int ci = 0; ci < cs; ci++)
		{
			TableColumn tc = tcm.getColumn(ci);
			int mi = tc.getModelIndex();
			Object identifier = tc.getIdentifier();
			//
			MUserDefField uDefField = uDefTab.getField((String)identifier);
			if (uDefField == null)
			{
			//	log.warning("NotFound: " + ci +  " (" + mi + "): " + tc.getIdentifier() + " - " + tc.getHeaderValue());
				continue;
			}
			//	Multi-Row & Single-Row Sequence
			uDefField.setMRSeqNo((ci+1) * 10);
			uDefField.setSeqNo((mi+1) * 10);
			uDefField.save();
			log.finest(ci +  " (" + mi + "): " + tc.getIdentifier() + " - " + tc.getHeaderValue());
		}
		return true;
	}	//	update

	/**
	 * 	Update System record
	 *	@param AD_Tab_ID tab to be updated
	 *	@param vt Table
	 *	@return true if saved
	 */
	private boolean updateSystem (int AD_Tab_ID, VTable vt)
	{
		log.info("AD_Tab_ID=" + AD_Tab_ID);
		MWindow win = new MWindow(Env.getCtx(), m_AD_Window_ID, null);
		if (fSaveSize.isSelected())
			win.setWindowSize(m_size);
		else
			win.setWindowSize(new Dimension(0,0));
		win.save();
		
		MTab tab = win.getTab(AD_Tab_ID);
		
		TableColumnModel tcm = vt.getColumnModel();
		int cs = tcm.getColumnCount();
		for (int ci = 0; ci < cs; ci++)
		{
			TableColumn tc = tcm.getColumn(ci);
			int mi = tc.getModelIndex();
			Object identifier = tc.getIdentifier();
			//
			MField field = tab.getField((String)identifier);
			if (field == null)
			{
			//	log.warning("NotFound: " + ci +  " (" + mi + "): " + tc.getIdentifier() + " - " + tc.getHeaderValue());
				continue;
			}
			//	Multi-Row & Single-Row Sequence
			field.setMRSeqNo((ci+1) * 10);
			field.setSeqNo((mi+1) * 10);
			field.save();
			log.finest(ci +  " (" + mi + "): " + tc.getIdentifier() + " - " + tc.getHeaderValue());
		}
		return true;
	}	//	updateSystem

}	//	AUserDefDialog
