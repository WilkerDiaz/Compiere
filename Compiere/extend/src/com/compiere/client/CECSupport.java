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
package com.compiere.client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.sql.*;
import java.util.List;
import java.util.logging.*;

import javax.swing.*;

import org.compiere.*;
import org.compiere.common.constants.*;
import org.compiere.db.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 * 	Support Dialog Frame
 *
 *  @author Jorg Janke
 *  @version $Id: Support.java 3553 2008-10-17 01:11:57Z rthng $
 */
public final class CECSupport
{
	/**	Title					*/
	public final static String TITLE = "Compiere Support 3.2.0.2";
	/** Copyright				*/
	public static final String COPYRIGHT = "Copyright (c) ComPiere, Inc. 1999-2008";

	/**	Debug Info 						*/
	/** How to load the migrate package: false - from URL Class loader; true - class path */
	static final boolean		INTERNALTEST = false;	// TODO reset it to false before release
	/** Enable Distribution				*/
	static final boolean		INTERNALDISTRIBUTION = true;	// TODO reset it to false before distribution

	/** Prefix http://				*/
	static final String				SERVER_PREFIX = "http://";
	/** License Server Name can be overwritten via -DCServerName=xxx	*/
	//@ TODO EPA LIC: validar la licencia en servidor local
	//static final String				SERVER_NAME = "www.compiereeeee.com";
	static final String				SERVER_NAME = "e00jcomp1";
	/** License Server URI			*/
	//@ TODO EPA LIC: para validar la licencia y generar el summary
	static final String				SERVER_URI = "/migrateApps/Migrate3";
	
	/** File to download	3a for 3.0 SP1	*/
	static private final String		JAR_URI = "/migrate/MigrateClient32.jar?320";
	/** */
    private static final long serialVersionUID = -875163484858750714L;
	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger (CECSupport.class);

	
	/**
	 * 	Get the Jar URL
	 * 	@return http://www.compiere.com//migrate/MigrateClient32.jar?320
	 */
	static String getJarURL()
	{
		/*
		String serverName = System.getProperty("CServerName", CECSupport.SERVER_NAME);
		return SERVER_PREFIX + serverName + JAR_URI;
		*/
		return "http://www.epaenlinea.com/compiere/MigrateClient32.zip";
		
	}	//	getJarURL



	/** List to Migrate			*/
	private List<String>	m_carList = null;
	/** Properties				*/
	private final Ctx			ctx = Env.getCtx();
	/** System Record			*/
	private MSystem		m_system = null;
	/** Internal users			*/
	private static int	s_users = -1;
	/** Licensed Flag			*/
	private boolean		m_licensed = false;

	private String 		m_targetURL = "";
	private String 		m_targetUID = "";
	private String 		m_targetPWD = "";
	private String		m_sourceURL = "";
	private String		m_sourceUID = "";
	private String		m_sourcePWD = "";

	/** Confirm Panel			*/
	
	/** Main Panel				*/
	
	private final CTextField	nameField = new CTextField(10);
	private final	CLabel		nameLabel = new CLabel(Msg.getMsg(ctx, "SystemName", true), nameField);
	private CComboBox	statusField = null;
	private	CLabel		statusLabel = null;
	private final CTextField	emailField = new CTextField(10);
	private final	CLabel		emailLabel = new CLabel("Registered EMail", emailField);
	private final CPassword	passwordField = new CPassword(10);
	private final	CLabel		passwordLabel = new CLabel(Msg.getElement(ctx, "Password"), passwordField);
	private final CTextField	supportField = new CTextField(10);
	private final	CLabel		supportLabel = new CLabel(Msg.translate(ctx, "SupportEMail"), emailField);
	//
	private final CTextField	targetURLField = new CTextField(15);
	private final	CLabel		targetURLLabel = new CLabel(Msg.translate(ctx, "TargetURL"), targetURLField);
	private final CTextField	unitsSysField = new CTextField(10);
	private final	CLabel		unitsSysLabel = new CLabel(Msg.translate(ctx, "SupportUnits"), unitsSysField);
	private final CTextField	dateExpField = new CTextField(10);
	private final	CLabel		dateExpLabel = new CLabel(Msg.translate(ctx, "SupportExpDate"), dateExpField);
	private final CTextField	unitsContractField = new CTextField(10);
	private final	CLabel		unitsContractLabel = new CLabel(Msg.getMsg(ctx, "SupportContractUnits", true), unitsContractField);
	private final CCheckBox	fieldRegistered = new CCheckBox(Msg.getMsg(ctx, "SupportContract", true));
	private final CTextField	supportLevelField = new CTextField(10);
	private final	CLabel		supportLevelLabel = new CLabel(Msg.translate(ctx, "SupportLevel"), supportLevelField);
	//
	private final CTextField	sourceURLField = new CTextField(15);
	private final CLabel		sourceURLLabel = new CLabel(Msg.translate(ctx, "SourceURL"), sourceURLField);
	private final CTextField	sourceUIDField = new CTextField(10);
	private final CLabel		sourceUIDLabel = new CLabel(Msg.translate(ctx, "SourceUID"), sourceUIDField);
	private final CPassword	sourcePWDField = new CPassword(10);
	private final CLabel		sourcePWDLabel = new CLabel(Msg.translate (ctx, "SourcePWD"), sourcePWDField);
	//
	private final CTextArea	licenseField = new CTextArea(3,30);
	private final CTextArea	remarksField = new CTextArea(3,30);
	private CButton		startButton = null;

	

	/**
	 * 	Get DB Address.
	 *	See Paper.getDBAddress()
	 *	@return url # uid
	 */
	private String getDBAddress()
	{
		CConnection cc = CConnection.get();
		m_targetURL = cc.getConnectionURL();
		m_targetUID = cc.getDbUid();
		m_targetPWD = cc.getDbPwd();

		if (m_carList == null)
			// Start the Maintenance mode
			m_sourceURL = m_targetURL;
		else
		{
			// Start the migrate mode
			String carFileString = System.getenv(Ini.COMPIERE_HOME);
			carFileString += File.separator + "data" + File.separator + "compiere.car";
			File carFile = new File(carFileString);
			try
			{
				m_sourceURL = carFile.toURI().toURL().toString();
			}
			catch (Exception ex)
			{
				m_sourceURL = "";
			}
		}
		return getThisDBAddress();
	}	//	getDBAddress

	/**
	 * 	Get DB Address of this instance
	 *	@return dbAddress
	 */
	static String getThisDBAddress()
	{
		CConnection cc = CConnection.get();
		String targetURL = cc.getConnectionURL();
		String targetUID = cc.getDbUid();
		String retValue = targetURL + "#" + targetUID;
		return retValue.toLowerCase();
	}	//	getThisDBAddress

	/**
	 * 	Set Internal User Count
	 * 	@return count
	 */
	static int getInternalUsers()
	{
		if (s_users != -1)
			return s_users;
		s_users = 9999;
		String sql = "SELECT COUNT(DISTINCT (u.AD_User_ID)) AS iu "
			+ "FROM AD_User u"
			+ " INNER JOIN AD_User_Roles ur ON (u.AD_User_ID=ur.AD_User_ID) "
			+ "WHERE u.AD_Client_ID<>11"			//	no Demo
			+ " AND u.AD_User_ID NOT IN (0,100)";	//	no System/SuperUser
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				s_users = rs.getInt (1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_users = -2;
			if (log != null)
				log.log(Level.SEVERE, "iu", e);
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
		return s_users;
	}	//	getInternalUsers

	/**
	 * 	Get Statistics Info
	 *	@return statistics
	 */
	static String getStatisticsInfo()
	{
		String s = null;
		String count = DB.TO_CHAR("COUNT(*)", DisplayTypeConstants.Number, Env.getAD_Language(Env.getCtx()));
		String sql = "SELECT 'C'||(SELECT " + count + " FROM AD_Client)"
			+ " ||'U'||(SELECT " + count + " FROM AD_User)"
			+ " ||'B'||(SELECT " + count + " FROM C_BPartner)"
			+ " ||'P'||(SELECT " + count + " FROM M_Product)"
			+ " ||'I'||(SELECT " + count + " FROM C_Invoice)"
			+ " ||'L'||(SELECT " + count + " FROM C_InvoiceLine)"
			+ " ||'M'||(SELECT " + count + " FROM M_Transaction)"
			+ " ||'c'||(SELECT " + count + " FROM AD_Column WHERE EntityType NOT IN ('C','D'))"
			+ " ||'t'||(SELECT " + count + " FROM AD_Table WHERE EntityType NOT IN ('C','D'))"
			+ " ||'f'||(SELECT " + count + " FROM AD_Field WHERE EntityType NOT IN ('C','D'))"
			+ " FROM AD_System";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				s = rs.getString(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			if (log != null)
				log.log(Level.SEVERE, "si", e);
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
		return s;
	}	//	getStatisticsInfo

	/**
	 * 	Get Profile Info
	 *	@return profile
	 */
	static String getProfileInfo()
	{
		String sql = "SELECT Value FROM AD_Client "
			+ "WHERE IsActive='Y' ORDER BY AD_Client_ID DESC";
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				sb.append(rs.getString(1)).append('|');
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			if (log != null)
				log.log(Level.SEVERE, "pi", e);
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
		return sb.toString();
	}	//	getProfileInfo

	
	

	/**
	 * 	Publish Info
	 *	@param info info
	 */
	private void publishInfo (String info)
	{
		String text = remarksField.getText();
		if ((text == null) || (text.length() == 0))
			text = info;
		else
			text += "\n" + info;
		remarksField.setText(text);
		remarksField.setCaretPosition(text.length());
	}	//	publishInfo


}
