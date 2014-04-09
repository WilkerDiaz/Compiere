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
package org.compiere.print;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import org.compiere.apps.*;
import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Application Report Launcher.
 *  Called from APanel; Queries available Reports for Table.
 *  If no report available, a new one is created and launched.
 *  If there is one report, it is launched.
 *  If there are more, the available reports are displayed and the selected is launched.
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: AReport.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class AReport implements ActionListener
{
	/**
	 *	Constructor
	 *
	 *  @param AD_Table_ID table
	 *  @param invoker component to display popup (optional)
	 *  @param query query
	 *  @param tab AD_Tab_ID
	 */
	public AReport (int AD_Table_ID, JComponent invoker, Query	query, int AD_Tab_ID)
	{
		log.config("AD_Table_ID=" + AD_Table_ID + " " + query);
		if (!MRole.getDefault().isCanReport(AD_Table_ID))
		{
			ADialog.error(0, invoker, "AccessCannotReport", query.getTableName());
			return;
		}

		m_query = query;

		//	See What is there
		getPrintFormats (AD_Table_ID, invoker, AD_Tab_ID);
	}	//	AReport
	
	public AReport (int AD_Table_ID, JComponent invoker, Query	query){
		this(AD_Table_ID,invoker,query,-1);
	}

	/**	The Query						*/
	private Query		 		m_query;
	/**	The Popup						*/
	private final JPopupMenu 	m_popup = new JPopupMenu("ReportMenu");

	/**	The Option List					*/
	private final ArrayList<KeyNamePair>	m_list = new ArrayList<KeyNamePair>();
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(AReport.class);
	/** Parent Tab */
	private int 	m_AD_Tab_ID=-1;

	/**
	 * 	Get the Print Formats for the table.
	 *  Fill the list and the popup menu
	 * 	@param AD_Table_ID table
	 *  @param invoker component to display popup (optional)
	 */
	private void getPrintFormats (int AD_Table_ID, JComponent invoker, int AD_Tab_ID)
	{
		int AD_Client_ID = Env.getCtx().getAD_Client_ID();
		//
		String sql = MRole.getDefault().addAccessSQL (
			"SELECT AD_PrintFormat_ID, Name, AD_Client_ID "
				+ "FROM AD_PrintFormat "
				+ "WHERE AD_Table_ID=? AND IsTableBased='Y' AND IsActive='Y'  "
				+ "ORDER BY AD_Client_ID DESC, IsDefault DESC, Name",		//	Own First
			"AD_PrintFormat", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		KeyNamePair pp = null;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, AD_Table_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				pp = new KeyNamePair (rs.getInt(1), rs.getString(2));
				if (rs.getInt(3) == AD_Client_ID)
				{
					m_list.add(pp);
					final String actionCommand = pp.toString();
					final JMenuItem mi = m_popup.add(actionCommand);
					mi.addActionListener(this);
				}
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}

		//	No Format exists - create it
		if (m_list.size() == 0)
		{
			if (pp == null)
				createNewFormat (AD_Table_ID);		//	calls launch
			else
				copyFormat(pp.getKey(), AD_Client_ID);
		}
		//	One Format exists or no invoker - show it
		else if (m_list.size() == 1 || invoker == null)
			launchReport (m_list.get(0),AD_Tab_ID);
		//	Multiple Formats exist - show selection
		else if (invoker.isShowing()){
			m_popup.show(invoker, 0, invoker.getHeight());	//	below button
			setAD_Tab_ID(AD_Tab_ID);
		}
	}	//	getPrintFormats	

	/**
	 * 	Create and Launch new Format for table
	 * 	@param AD_Table_ID table
	 */
	private void createNewFormat (int AD_Table_ID)
	{
		MPrintFormat pf = MPrintFormat.createFromTable(Env.getCtx(), AD_Table_ID);
		launchReport (pf);
	}	//	createNewFormat

	/**
	 * 	Copy existing Format
	 * 	@param AD_PrintFormat_ID print format
	 * 	@param To_Client_ID to client
	 */
	private void copyFormat (int AD_PrintFormat_ID, int To_Client_ID)
	{
		MPrintFormat pf = MPrintFormat.copyToClient(Env.getCtx(), AD_PrintFormat_ID, To_Client_ID);
		launchReport (pf);
	}	//	copyFormatFromClient

	/**
	 * 	Launch Report
	 * 	@param pp Key=AD_PrintFormat_ID
	 *  @param tab AD_Tab_ID
	 */
	private void launchReport (KeyNamePair pp, int AD_Tab_ID)
	{
		MPrintFormat pf = MPrintFormat.get(Env.getCtx(), pp.getKey(), false);
		launchReport (pf, AD_Tab_ID);
	}	//	launchReport
	
	/**
	 * 	Launch Report
	 * 	@param pf print format
	 *  @param tab AD_Tab_ID
	 */
	private void launchReport (MPrintFormat pf, int AD_Tab_ID)
	{
		int Record_ID = 0;
		if (m_query.getRestrictionCount()==1 && m_query.getCode(0) instanceof Integer)
			Record_ID = ((Integer)m_query.getCode(0)).intValue();
		PrintInfo info = new PrintInfo(
			pf.getName(),
			pf.getAD_Table_ID(),
			Record_ID);
		info.setDescription(m_query.getInfo());
		ReportEngine re = new ReportEngine (Env.getCtx(), pf, m_query, info);
		Viewer viewer = new Viewer(re);
		viewer.setAD_Tab_ID(AD_Tab_ID);
	//	if (m_popup.isVisible())
	//		m_popup.setVisible(false);
	}	//	launchReport
	
	private void launchReport (MPrintFormat pf){
		launchReport(pf,-1);
	}

	/**
	 * 	Action Listener
	 * 	@param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		m_popup.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		String cmd = e.getActionCommand();
		for (int i = 0; i < m_list.size(); i++)
		{
			KeyNamePair pp = m_list.get(i);
			if (cmd.equals(pp.getName()))
			{
				launchReport (pp,getAD_Tab_ID());
				return;
			}
		}
	}	//	actionPerformed


	/**************************************************************************
	 * 	Get AD_Table_ID for Table Name
	 * 	@param TableName table name
	 * 	@return AD_Table_ID or 0
	 */
	static public int getAD_Table_ID (String TableName)
	{
		int AD_Table_ID = 0;
		String sql = "SELECT AD_Table_ID FROM AD_Table WHERE TableName=?";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, TableName);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				AD_Table_ID = rs.getInt(1);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		return AD_Table_ID;
	}	//	getAD_Table_ID

	public int getAD_Tab_ID() {
		return m_AD_Tab_ID;
	}

	public void setAD_Tab_ID(int tab_ID) {
		m_AD_Tab_ID = tab_ID;
	}

}	//	AReport

