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
import java.util.logging.*;

import javax.swing.*;

import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *	Request Button Action.
 *	Popup Menu
 *
 *  @author Jorg Janke
 *  @version $Id: ARequest.java,v 1.2 2006/07/30 00:51:27 jjanke Exp $
 */
public class ARequest implements ActionListener
{
	/**
	 * 	Constructor
	 *	@param invoker invoker button
	 *	@param AD_Table_ID table
	 *	@param Record_ID record
	 *	@param C_BPartner_ID optional bp
	 *	@param origTab original tab
	 */
	public ARequest (JComponent invoker, int AD_Table_ID, int Record_ID,
		int C_BPartner_ID, GridTab origTab)
	{
		super ();
		log.config("AD_Table_ID=" + AD_Table_ID + ", Record_ID=" + Record_ID);
		m_AD_Table_ID = AD_Table_ID;
		m_Record_ID = Record_ID;
		m_C_BPartner_ID = C_BPartner_ID;
		m_origTab = origTab;
		getRequests(invoker);
	}	//	ARequest

	/**	The Table						*/
	private final int			m_AD_Table_ID;
	/** The Record						*/
	private final int			m_Record_ID;
	/** BPartner						*/
	private final int			m_C_BPartner_ID;
	/** Original Tab					*/
	private final GridTab		m_origTab;

	/**	The Popup						*/
	private final JPopupMenu 	m_popup = new JPopupMenu("RequestMenu");

	CMenuItem 	m_new = null;
	CMenuItem 	m_active = null;
	CMenuItem 	m_all = null;
	/** Where Clause					*/
	StringBuffer 		m_where = null;

	/**	Logger	*/
	private static CLogger	log	= CLogger.getCLogger (ARequest.class);

	/**
	 * 	Display Request Options - New/Existing.
	 * 	@param invoker button
	 */
	private void getRequests (JComponent invoker)
	{
		m_new = new CMenuItem(Msg.getMsg(Env.getCtx(), "RequestNew"));
		m_new.setIcon(Env.getImageIcon("New16.gif"));
		m_popup.add(m_new);
		m_new.addActionListener(this);
		//
		int activeCount = 0;
		int inactiveCount = 0;
		m_where = new StringBuffer();
		m_where.append("(AD_Table_ID=").append(m_AD_Table_ID)
			.append(" AND Record_ID=").append(m_Record_ID)
			.append(")");
		//
		if (m_AD_Table_ID == X_AD_User.Table_ID)
			m_where.append(" OR AD_User_ID=").append(m_Record_ID)
				.append(" OR SalesRep_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == X_C_BPartner.Table_ID)
			m_where.append(" OR C_BPartner_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == X_C_Order.Table_ID)
			m_where.append(" OR C_Order_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == X_C_Invoice.Table_ID)
			m_where.append(" OR C_Invoice_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == X_C_Payment.Table_ID)
			m_where.append(" OR C_Payment_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == X_M_Product.Table_ID)
			m_where.append(" OR M_Product_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == X_C_Project.Table_ID)
			m_where.append(" OR C_Project_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == X_C_Campaign.Table_ID)
			m_where.append(" OR C_Campaign_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == X_A_Asset.Table_ID)
			m_where.append(" OR A_Asset_ID=").append(m_Record_ID);
		//
		String sql = "SELECT Processed, COUNT(*) "
			+ "FROM R_Request WHERE " + m_where
			+ " GROUP BY Processed "
			+ "ORDER BY Processed DESC";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				if ("Y".equals(rs.getString(1)))
					inactiveCount = rs.getInt(2);
				else
					activeCount += rs.getInt(2);
			}
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

		//
		if (activeCount > 0)
		{
			m_active = new CMenuItem(Msg.getMsg(Env.getCtx(), "RequestActive")
				+ " (" + activeCount + ")");
			m_popup.add(m_active);
			m_active.addActionListener(this);
		}
		if (inactiveCount > 0)
		{
			m_all = new CMenuItem(Msg.getMsg(Env.getCtx(), "RequestAll")
				+ " (" + (activeCount + inactiveCount) + ")");
			m_popup.add(m_all);
			m_all.addActionListener(this);
		}
		//
		if (invoker.isShowing())
			m_popup.show(invoker, 0, invoker.getHeight());	//	below button
	}	//	getZoomTargets

	/**
	 * 	Listner
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		Query query = null;
		String actionCommand = e.getActionCommand();
		if (m_active != null && m_active.getText().equals(actionCommand))
		{
			query = new Query("");
			String where = "(" + m_where + ") AND Processed='N'";
			query.addRestriction(where);
		}
		else if (m_all != null && m_all.getText().equals(actionCommand))
		{
			query = new Query("");
			query.addRestriction(m_where.toString());
		}
		//
		int AD_Window_ID = 232;		//	232=all - 201=my
		AWindow reqFrame = new AWindow();
		if (!reqFrame.initWindow(AD_Window_ID, query))
			return;

		//	New - set Table/Record
		if (m_new.getText().equals(actionCommand))
		{
			GridTab reqTab = reqFrame.getAPanel().getCurrentTab();
			reqTab.dataNew (false);
			reqTab.setValue("AD_Table_ID", Integer.valueOf(m_AD_Table_ID));
			reqTab.setValue("Record_ID", Integer.valueOf(m_Record_ID));
			//
			if (m_C_BPartner_ID != 0)
				reqTab.setValue("C_BPartner_ID", Integer.valueOf(m_C_BPartner_ID));
			//
			if (m_AD_Table_ID == X_C_BPartner.Table_ID)
				reqTab.setValue("C_BPartner_ID", Integer.valueOf(m_Record_ID));
			else if (m_AD_Table_ID == X_AD_User.Table_ID)
				reqTab.setValue("AD_User_ID", Integer.valueOf(m_Record_ID));
			//
			else if (m_AD_Table_ID == X_C_Project.Table_ID)
				reqTab.setValue("C_Project_ID", Integer.valueOf(m_Record_ID));
			else if (m_AD_Table_ID == X_A_Asset.Table_ID)
				reqTab.setValue("A_Asset_ID", Integer.valueOf(m_Record_ID));
			//
			else if (m_AD_Table_ID == X_C_Order.Table_ID)
				reqTab.setValue("C_Order_ID", Integer.valueOf(m_Record_ID));
			else if (m_AD_Table_ID == X_C_Invoice.Table_ID)
				reqTab.setValue("C_Invoice_ID", Integer.valueOf(m_Record_ID));
			//
			else if (m_AD_Table_ID == X_M_Product.Table_ID)
				reqTab.setValue("M_Product_ID", Integer.valueOf(m_Record_ID));
			else if (m_AD_Table_ID == X_C_Payment.Table_ID)
				reqTab.setValue("C_Payment_ID", Integer.valueOf(m_Record_ID));
			//
			else if (m_AD_Table_ID == X_M_InOut.Table_ID)
				reqTab.setValue("M_InOut_ID", Integer.valueOf(m_Record_ID));
			//
			else if (m_AD_Table_ID == X_C_Campaign.Table_ID)
				reqTab.setValue("C_Campaign_ID", Integer.valueOf(m_Record_ID));
			//
			copyValue(reqTab, "SalesRep_ID");
			copyValue(reqTab, "C_Project_ID");
			copyValue(reqTab, "A_Asset_ID");
			copyValue(reqTab, "C_Order_ID");
			copyValue(reqTab, "C_Invoice_ID");
			copyValue(reqTab, "M_Product_ID");
			copyValue(reqTab, "C_Payment_ID");
			copyValue(reqTab, "M_InOut_ID");
			copyValue(reqTab, "C_Campaign_ID");
			copyValue(reqTab, "C_SalesRegion_ID");
		}
		AEnv.showCenterScreen(reqFrame);
		reqFrame = null;
	}	//	actionPerformed

	private void copyValue(GridTab reqTab, String columnName)
	{
		Object value = m_origTab.getValue(columnName);
		if (value != null)
			reqTab.setValue(columnName, value);
	}	//	copyValue

}	//	ARequest
