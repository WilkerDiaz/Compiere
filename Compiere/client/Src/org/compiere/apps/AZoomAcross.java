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

import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import org.compiere.framework.*;
import org.compiere.util.*;

/**
 *	Application Zoom Across Launcher.
 *  Called from APanel; Queries available Zoom Targets for Table.
 *
 *  @author Jorg Janke
 *  @version $Id: AZoomAcross.java,v 1.2 2006/07/30 00:51:27 jjanke Exp $
 */
public class AZoomAcross implements ActionListener
{
	/**
	 *	Constructor
	 *  @param invoker component to display popup (optional)
	 *  @param tableName table name
	 *  @param query query
	 */
	public AZoomAcross (JComponent invoker, String tableName, Query query, int curWindow_ID)
	{
		log.config("TableName=" + tableName + " - " + query);
		m_query = query;

		//	See What is there
		getZoomTargets (invoker, tableName, curWindow_ID);
		m_query.setRecordCount(1);	//	guess
	}	//	AReport

	/**	The Query						*/
	private final Query	 	m_query;
	/**	The Popup						*/
	private final JPopupMenu 	m_popup = new JPopupMenu("ZoomMenu");


	/**	The Option List					*/
	private final ArrayList<KeyNamePair>	m_list = new ArrayList<KeyNamePair>();
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(AZoomAcross.class);

	/**
	 * 	Get the Zomm Targets for the table.
	 *  Fill the list and the popup menu
	 *  @param invoker component to display popup (optional)
	 * 	@param tableName table
	 */
	private void getZoomTargets (JComponent invoker, String tableName, int curWindow_ID)
	{
		String sql = "SELECT DISTINCT t.AD_Table_ID, t.TableName "
			+ "FROM AD_Table t "
			+ "WHERE EXISTS (SELECT 1 FROM AD_Tab tt "
				+ "WHERE tt.AD_Table_ID = t.AD_Table_ID AND tt.SeqNo=10) "
			+ " AND t.AD_Table_ID IN "
				+ "(SELECT AD_Table_ID FROM AD_Column "
				+ "WHERE ColumnName=?) "
			+ "AND TableName NOT LIKE 'I%'"
			+ "AND TableName NOT LIKE ? "
			+ "ORDER BY 1";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			int index = 1;
			pstmt.setString (index++, tableName + "_ID");
			pstmt.setString (index++, tableName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				String targetTableName = rs.getString(2);
				ArrayList<KeyNamePair> zoomList = ZoomTarget.getZoomTargets(
					targetTableName, curWindow_ID, m_query.getWhereClause());

				if (zoomList != null)
				{
					for(int i = 0; i < zoomList.size(); i++)
					{
						KeyNamePair pp = zoomList.get(i);
						if(!m_list.contains(pp))
						{
							m_list.add(pp);
							String actionCommand = pp.toString();
							JMenuItem mi = m_popup.add(actionCommand);
							mi.addActionListener(this);
						}
					}
				}
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}

		//	No Zoom
		if (m_list.size() == 0)
		{
			ADialog.info(0, invoker, "NoZoomTarget");
		}
		else if (invoker.isShowing())
		{
			m_popup.show(invoker, 0, invoker.getHeight());	//	below button
		}
	}	//	getZoomTargets


	/**
	 * 	Action Listener
	 * 	@param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		m_popup.setVisible(false);
		String cmd = e.getActionCommand();
		for (int i = 0; i < m_list.size(); i++)
		{
			KeyNamePair pp = m_list.get(i);
			if (cmd.equals(pp.getName()))
			{
				launchZoom (pp);
				return;
			}
		}
	}	//	actionPerformed

	/**
	 * 	Launch Zoom
	 *	@param pp KeyPair
	 */
	private void launchZoom (KeyNamePair pp)
	{
		int AD_Window_ID = pp.getKey();
		log.info("AD_Window_ID=" + AD_Window_ID	+ " - " + m_query);
		AWindow frame = new AWindow();
		if (!frame.initWindow(AD_Window_ID, m_query))
			return;
		AEnv.showCenterScreen(frame);
		frame = null;
	}	//	launchZoom

}	//	AZoom
