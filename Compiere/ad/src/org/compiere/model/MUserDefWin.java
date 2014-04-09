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
package org.compiere.model;

import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.common.CompiereStateException;
import org.compiere.util.*;


/**
 *	User Defined Window model
 *	
 *  @author Jorg Janke
 *  @version $Id: MUserDefWin.java 8756 2010-05-12 21:21:27Z nnayak $
 */
public class MUserDefWin extends X_AD_UserDef_Win
{
    /** Logger for class MUserDefWin */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MUserDefWin.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get existing User Defined Window for Role or User
	 *	@param ctx context
	 *	@param AD_Window_ID window
	 *	@param AD_Role_ID role
	 *	@param AD_User_ID user
	 *	@return array of Windows
	 */
	static public MUserDefWin[] get (Ctx ctx, int AD_Window_ID, 
		int AD_Client_ID, int AD_Role_ID, int AD_User_ID)
	{
		ArrayList<MUserDefWin> list = new ArrayList<MUserDefWin>();
		StringBuffer sql = new StringBuffer("SELECT * FROM AD_UserDef_Win "
			+ "WHERE AD_Client_ID=? AND AD_Window_ID=?");
		if (AD_Role_ID == 0 && AD_User_ID == 0)
			;
		else if (AD_Role_ID == 0 && AD_User_ID != 0)
			sql.append(" AND AD_User_ID=?");
		else if (AD_Role_ID != 0 && AD_User_ID != 0)
			sql.append(" AND (AD_Role_ID=? OR AD_User_ID=?)");
		else if (AD_Role_ID != 0 && AD_User_ID == 0)
			sql.append(" AND AD_Role_ID=?");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
			pstmt.setInt (1, AD_Client_ID);
			pstmt.setInt (2, AD_Window_ID);
			if (AD_Role_ID == 0 && AD_User_ID != 0)
				pstmt.setInt(3, AD_User_ID);
			else if (AD_Role_ID != 0 && AD_User_ID != 0)
			{
				pstmt.setInt(3, AD_Role_ID);
				pstmt.setInt(4, AD_User_ID);
			}
			else if (AD_Role_ID != 0 && AD_User_ID == 0)
				pstmt.setInt(3, AD_Role_ID);
			//
			rs = pstmt.executeQuery ();
			while (rs.next())
				list.add (new MUserDefWin (ctx, rs, null));
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, sql.toString(), e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		MUserDefWin[] retValue = new MUserDefWin[list.size()];
		list.toArray(retValue);
		s_log.fine("#" + retValue.length);
		return retValue;
	}	//	get

	
	/**
	 * 	Create new User Defined Window
	 *	@param ctx context
	 *	@param AD_Window_ID window
	 *	@param AD_Role_ID role
	 *	@param AD_User_ID user
	 *	@return User Defined Window
	 */
	static public MUserDefWin create (Ctx ctx, int AD_Client_ID, 
		int AD_Window_ID, String prefix,
		int AD_Role_ID, int AD_User_ID)
	{
		MWindow win = MWindow.get(ctx, AD_Window_ID);
		if (win.get_ID() != AD_Window_ID)
			return null;
		MUserDefWin retValue = new MUserDefWin(ctx, 0, null);
		retValue.setClientOrg(AD_Client_ID, 0);
		if (retValue.create (win, prefix, AD_Role_ID, AD_User_ID))
		{
			createSystemDefault(ctx, AD_Window_ID);
			return retValue;
		}
		return null;
	}	//	create
	
	/**
	 * 	Create System Default record
	 *	@param ctx context
	 *	@param AD_Window_ID window
	 *	@return true if created, false if existing or error
	 */
	static private boolean createSystemDefault (Ctx ctx, int AD_Window_ID)
	{
		if (getSystemDefault(ctx, AD_Window_ID) != null)
			return false;
		MUserDefWin retValue = new MUserDefWin (ctx, 0, null);
		retValue.setClientOrg(0, 0);
		retValue.setAD_Window_ID(AD_Window_ID);
		retValue.setCustomizationName(SYSTEMDEFAULT);
		retValue.setIsSystemDefault(true);
		return retValue.save();
	}	//	createSystemDefault
	
	/**
	 * 	Get System Default record
	 *	@param ctx context
	 *	@param AD_Window_ID window
	 *	@return default if exists
	 */
	static public MUserDefWin getSystemDefault (Ctx ctx, int AD_Window_ID)
	{
		MUserDefWin retValue = null;
		String sql = "SELECT * FROM AD_UserDef_Win WHERE AD_Window_ID=? AND IsSystemDefault='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
        try
        {
	        pstmt = DB.prepareStatement(sql, (Trx) null);
	        pstmt.setInt(1, AD_Window_ID);
	        rs = pstmt.executeQuery();
	        while (rs.next())
	        	retValue = new MUserDefWin(ctx, rs, null);
        }
        catch (Exception e) {
        	s_log.log(Level.SEVERE, sql, e);
        }
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

        return retValue;
	}	//	getSystemDefault
	
	/**	System Default Window			*/
	public final static String 	SYSTEMDEFAULT = "SystemDefault";
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MUserDefWin.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_UserDef_Win_ID id
	 *	@param trx p_trx
	 */
	public MUserDefWin(Ctx ctx, int AD_UserDef_Win_ID, Trx trx)
	{
		super (ctx, AD_UserDef_Win_ID, trx);
		if (AD_UserDef_Win_ID == 0)
		{
			setEntityType(ENTITYTYPE_UserMaintained);
			setIsSystemDefault(false);
		}
	}	//	MUserDefWin

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MUserDefWin(Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MUserDefWin

	/**	The Tabs						*/
	private MUserDefTab[] m_tabs = null;

	
	/**
	 * 	Get Lines
	 *	@param reload reload data
	 *	@return array of lines
	 */
	public MUserDefTab[] getTabs(boolean reload)
	{
		if (m_tabs != null && !reload)
			return m_tabs;
		String sql = "SELECT * FROM AD_UserDef_Tab "
			+ "WHERE AD_UserDef_Win_ID=?";
		ArrayList<MUserDefTab> list = new ArrayList<MUserDefTab>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx ());
			pstmt.setInt (1, getAD_UserDef_Win_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MUserDefTab(getCtx(), rs, get_Trx()));
		}
		catch (Exception e) {
			log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		m_tabs = new MUserDefTab[list.size()];
		list.toArray (m_tabs);
		return m_tabs;
	}	//	getTabs
	
	/**
	 * 	Get Tab
	 *	@param AD_Tab_ID tab
	 *	@return tab or null
	 */
	public MUserDefTab getTab (int AD_Tab_ID)
	{
		MUserDefTab[] tabs = getTabs(false);
		for (MUserDefTab element : tabs) {
	        if (element.getAD_Tab_ID() == AD_Tab_ID)
	        	return element; 
        }
		return null;
	}	//	getTab;
	
	/**
	 * 	Create from Window (incl. Tabs/Fields)
	 *	@param win window
	 *	@return true if created
	 */
	private boolean create (MWindow win, String prefix, int AD_Role_ID, int AD_User_ID)
	{
		if (getAD_UserDef_Win_ID() != 0)
			throw new CompiereStateException("Needs to be new");
		//	Win
		setAD_Window_ID(win.getAD_Window_ID());
		setAD_Org_ID(0);
		StringBuffer name = new StringBuffer();
		if (prefix != null && prefix.length() > 0)
			name.append(prefix).append(" ");
		if (AD_Role_ID != 0)
			name.append(MRole.get (getCtx(), AD_Role_ID).getName());
		if (AD_User_ID != 0)
			name.append(MUser.get (getCtx(), AD_User_ID).getName());
		if (name.length() > 0)
			name.append(": ");
		name.append(win.getName());
		setCustomizationName(name.toString());
		if (!save())
			return false;
		
		//	Tabs
		MTab[] tabs = win.getTabs (false, get_Trx());
		for (int i = 0; i < tabs.length; i++)
		{
			MUserDefTab tt = new MUserDefTab(this);
			if (!tt.create(tabs[i]))
				return false;
			m_tabs = null;
		}
		return true;
	}	//	create
	
	/**
	 * 	Set Window Size
	 *	@param size size
	 */
	public void setWindowSize (Dimension size)
	{
		if (size != null)
		{
			setWinWidth(size.width);
			setWinHeight(size.height);
		}
		else
		{
			set_Value("WinWidth", null);
			set_Value("WinHeight", null);
		}
	}	//	setWindowSize

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		if (!newRecord && isSystemDefault())
			return false;
	    return true;
	}	//	beforeSave
	
	/**
	 * 	String Representation
	 *	@return Name
	 */
	@Override
	public String toString()
	{
	    return getCustomizationName();
	}	//	toString
	
}	//	MUserDefWin
