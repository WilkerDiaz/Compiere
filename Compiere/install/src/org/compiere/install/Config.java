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
package org.compiere.install;

import java.sql.*;

import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *	Configuration Setup and Test
 *	
 *  @author Jorg Janke
 *  @version $Id: Config.java 7904 2009-07-20 22:22:01Z freyes $
 */
public abstract class Config
{
	/**
	 * 	Configuration
	 * 	@param data configuration
	 */
	public Config (ConfigurationData data)
	{
		super ();
		p_data = data;
	}	//	Config

	/**	Configuration Data			*/
	protected ConfigurationData 	p_data = null;
	/**	Logger	*/
	protected CLogger	log	= CLogger.getCLogger (getClass());
	
	
	/**
	 * 	Initialize
	 */
	abstract void init();
	
	/**
	 * 	Test
	 *	@return error message or null of OK
	 */
	abstract String test();

	/**
	 * 	Discover Databases.
	 * 	To be overwritten by database configs
	 *	@param selected selected database
	 *	@return array of databases
	 */
	public String[] discoverDatabases(String selected)
	{
		return new String[]{};
	}	//	discoverDatabases
	
	/**
	 * 	Get Panel
	 *	@return panel
	 */
	protected ConfigurationPanel getPanel()
	{
		return p_data.p_panel;
	}	//	getPanel
	
	/**
	 * 	Set Configuration Property
	 *	@param key key
	 *	@param value value
	 */
	protected void setProperty(String key, String value)
	{
		p_data.p_properties.setProperty(key, value);
	}	//	setProperty

	/**
	 * 	Get Configuration Property
	 *	@param key key
	 *	@return value or ""
	 */
	protected String getProperty(String key)
	{
		return p_data.p_properties.getProperty(key, "");
	}	//	getProperty
	
	/**
	 * 	UI Signal OK
	 *	@param cb check box
	 *	@param resString resource string key
	 *	@param pass true if test passed
	 *	@param critical true if critical
	 *	@param errorMsg error Message
	 */
	void signalOK (CCheckBox cb, String resString, 
		boolean pass, boolean critical, String errorMsg)
	{
		p_data.p_panel.signalOK(cb, resString, pass, critical, errorMsg);
	}	//	signalOK

	/**
	 * 	Get Web Store Context Names separated by ,
	 *	@param con connection
	 *	@return String of Web Store Names - e.g. /wstore
	 */
	protected String getWebStores(Connection con)
	{
		String sql = "SELECT WebContext FROM W_Store WHERE IsActive='Y'";
		Statement stmt = null;
		StringBuffer result = new StringBuffer();
		try
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next ())
			{
				if (result.length() > 0)
					result.append(",");
				result.append(rs.getString(1));
			}
			rs.close ();
			stmt.close ();
			stmt = null;
		}
		catch (Exception e)
		{
			log.warning(e.toString());
		}
		try
		{
			if (stmt != null)
				stmt.close ();
			stmt = null;
		}
		catch (Exception e)
		{
			stmt = null;
		}
		return result.toString();
	}	//	getWebStores	

}	//	Config