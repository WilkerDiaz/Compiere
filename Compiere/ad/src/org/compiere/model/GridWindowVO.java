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

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.controller.*;
import org.compiere.util.*;

/**
 *  Model Window Value Object
 *
 *  @author Jorg Janke
 *  @version  $Id: GridWindowVO.java 8751 2010-05-12 16:49:31Z nnayak $
 */
public class GridWindowVO implements Serializable
{
	/**
	 *  Create Window Value Object
	 *  @param ctx context
	 *  @param WindowNo window no for ctx
	 *  @param AD_Window_ID window id
	 *  @return MWindowVO
	 */
	public static GridWindowVO create (Ctx ctx, int WindowNo, int AD_Window_ID)
	{
		return create (ctx, WindowNo, AD_Window_ID, 0);
	}   //  create

	/**
	 *  Create Window Value Object
	 *
	 *  @param ctx context
	 *  @param WindowNo window no for ctx
	 *  @param AD_Window_ID window id
	 *  @param AD_Menu_ID menu id
	 *  @return MWindowVO
	 */
	public static GridWindowVO create (Ctx ctx, int WindowNo, int AD_Window_ID, int AD_Menu_ID)
	{
		CLogger.get().config("#" + WindowNo
			+ " - AD_Window_ID=" + AD_Window_ID + "; AD_Menu_ID=" + AD_Menu_ID);
		GridWindowVO vo = new GridWindowVO (ctx, WindowNo);
		vo.AD_Window_ID = AD_Window_ID;

		//  Get Window_ID if required	- (used by HTML UI)
		if (vo.AD_Window_ID == 0 && AD_Menu_ID != 0)
		{
			String sql = "SELECT AD_Window_ID, IsSOTrx, IsReadOnly FROM AD_Menu "
				+ "WHERE AD_Menu_ID=? AND Action='W'";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, (Trx) null);
				pstmt.setInt(1, AD_Menu_ID);
				rs = pstmt.executeQuery();
				if (rs.next())
				{
					vo.AD_Window_ID = rs.getInt(1);
					String IsSOTrx = rs.getString(2);
					ctx.setContext(WindowNo, "IsSOTrx", (IsSOTrx != null && IsSOTrx.equals("Y")));
					//
					String IsReadOnly = rs.getString(3);
					if (IsReadOnly != null && IsReadOnly.equals("Y"))
						vo.IsReadWrite = "Y";
					else
						vo.IsReadWrite = "N";
				}
			}
			catch (SQLException e)
			{
				CLogger.get().log(Level.SEVERE, "Menu", e);
				return null;
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			CLogger.get().config("AD_Window_ID=" + vo.AD_Window_ID);
		}
  
		//  --  Get Window

		StringBuffer sql = new StringBuffer("SELECT Name,Description,Help,WindowType, "
			+ "AD_Color_ID,AD_Image_ID, IsReadWrite, WinHeight,WinWidth, "
			+ "IsSOTrx, AD_UserDef_Win_ID ");

		if (Env.isBaseLanguage(vo.ctx, "AD_Window"))
			sql.append("FROM AD_Window_v WHERE AD_Window_ID=? AND AD_Role_ID=?");
		else
			sql.append("FROM AD_Window_vt w WHERE AD_Window_ID=? AND AD_Role_ID=?")
				.append(" AND AD_Language='")
				.append(Env.getAD_Language(vo.ctx)).append("'");

		//int AD_Client_ID = vo.ctx.getAD_Client_ID();
		int AD_Role_ID = vo.ctx.getAD_Role_ID();
		int AD_UserDef_Win_ID = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			//	create statement
			pstmt = DB.prepareStatement(sql.toString(), (Trx) null);
			pstmt.setInt(1, vo.AD_Window_ID);
			pstmt.setInt(2, AD_Role_ID);
			//pstmt.setInt(3, AD_Client_ID);
			// 	get data
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				vo.Name = rs.getString(1);
				vo.Description = rs.getString(2);
				if (vo.Description == null)
					vo.Description = "";
				vo.Help = rs.getString(3);
				if (vo.Help == null)
					vo.Help = "";
				vo.WindowType = rs.getString(4);
				//
				vo.AD_Color_ID = rs.getInt(5);
				vo.AD_Image_ID = rs.getInt(6);
				vo.IsReadWrite = rs.getString(7);
				//
				vo.WinHeight = rs.getInt(8);
				vo.WinWidth = rs.getInt(9);
				//
				vo.IsSOTrx = "Y".equals(rs.getString(10));
				AD_UserDef_Win_ID = rs.getInt(11);
			}
			else
				vo = null;
		}
		catch (SQLException ex)
		{
			CLogger.get().log(Level.SEVERE, sql.toString(), ex);
			return null;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//	Not found
		if (vo == null)
		{
			CLogger.get().log(Level.WARNING, "No Window - AD_Window_ID=" + AD_Window_ID
				+ ", AD_Role_ID=" + AD_Role_ID + " - " + sql);
			CLogger.get().saveError("AccessTableNoView", "(Not found)");
			return null;
		}
		//	Read Write
		if (vo.IsReadWrite == null)
		{
			CLogger.get().saveError("AccessTableNoView", "(found)");
			return null;
		}

		//  Create Tabs
		createTabs (vo, AD_UserDef_Win_ID);
		if (vo.Tabs == null || vo.Tabs.size() == 0)
			return null;

		return vo;
	}   //  create

	/**
	 *  Create Window Tabs
	 *  @param mWindowVO Window Value Object
	 *  @return true if tabs were created
	 */
	private static boolean createTabs (GridWindowVO mWindowVO, int AD_UserDef_Win_ID)
	{
		mWindowVO.Tabs = new ArrayList<GridTabVO>();
		
		Trx p_trx = Trx.get("createTabs"); 

		String sql = GridTabVO.getSQL(mWindowVO.ctx, AD_UserDef_Win_ID);
		int TabNo = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			//	create statement
			pstmt = p_trx.getConnection().prepareStatement(sql);
			pstmt.setInt(1, mWindowVO.AD_Window_ID);
			rs = pstmt.executeQuery();
			boolean firstTab = true;
			while (rs.next())
			{
				if (mWindowVO.AD_Table_ID == 0)
					mWindowVO.AD_Table_ID = rs.getInt("AD_Table_ID");
				//  Create TabVO
				int onlyCurrentDays = 0;
				if (mWindowVO.WindowType.equals(WINDOWTYPE_TRX))
					onlyCurrentDays = 1;
				GridTabVO mTabVO = GridTabVO.create(mWindowVO, TabNo, rs,
					mWindowVO.WindowType.equals(WINDOWTYPE_QUERY),  //  isRO
					onlyCurrentDays, AD_UserDef_Win_ID, p_trx);
				if (mTabVO == null && firstTab)
					break;		//	don't continue if first tab is null
				if (mTabVO != null)
				{
					if (!mTabVO.IsReadOnly && "N".equals(mWindowVO.IsReadWrite))
						mTabVO.IsReadOnly = true;
					mWindowVO.Tabs.add(mTabVO);
					TabNo++;        //  must be same as mWindow.getTab(x)
					firstTab = false;
				}
			}
		}
		catch (SQLException e)
		{
			CLogger.get().log(Level.SEVERE, "createTabs", e);
			return false;
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			if( p_trx != null )
				p_trx.close();
		}

		//  No Tabs
		if (TabNo == 0 || mWindowVO.Tabs.size() == 0)
		{
			CLogger.get().log(Level.WARNING, "No Tabs - AD_Window_ID=" 
				+ mWindowVO.AD_Window_ID + " - " + sql);
			return false;
		}

		//	Put base table of window in ctx (for VDocAction)
		mWindowVO.ctx.setContext(mWindowVO.WindowNo, "BaseTable_ID", mWindowVO.AD_Table_ID);
		return true;
	}   //  createTabs

	
	/**************************************************************************
	 *  Private Constructor
	 *  @param Ctx context
	 *  @param windowNo window no
	 */
	private GridWindowVO (Ctx newCtx, int windowNo)
	{
		ctx = newCtx;
		WindowNo = windowNo;
	}   //  MWindowVO

	static final long serialVersionUID = 3802628212531678981L;

	/** Properties      */
	public Ctx			ctx;
	/** Window Number	*/
	public int 		    WindowNo;

	/** Window				*/
	public	int			AD_Window_ID = 0;
	/** Name				*/
	public	String		Name = "";
	/** Description			*/
	public	String		Description = "";
	/** Help				*/
	public	String		Help = "";
	/** Window Type			*/
	public	String		WindowType = "";
	/** Image				*/
	public int          AD_Image_ID = 0;
	/** Color				*/
	public int          AD_Color_ID = 0;
	/** Read Write			*/
	public String		IsReadWrite = null;
	/** Window Width		*/
	public int			WinWidth = 0;
	/** Window Height		*/
	public int			WinHeight = 0;
	/** Sales Order Trx		*/
	public boolean		IsSOTrx = false;

	/** Tabs contains MTabVO elements   */
	public ArrayList<GridTabVO>	Tabs = null;
	/** Base Table		*/
	public int 			AD_Table_ID = 0;

	/** Qyery				*/
	public static final String	WINDOWTYPE_QUERY = "Q";
	/** Transaction			*/
	public static final String	WINDOWTYPE_TRX = "T";
	/** Maintenance			*/
	public static final String	WINDOWTYPE_MMAINTAIN = "M";

	/**
	 *  Set Context including contained elements
	 *  @param newCtx context
	 */
	public void setCtx (Ctx newCtx)
	{
		ctx = newCtx;
		for (int i = 0; i < Tabs.size() ; i++)
		{
			GridTabVO tab = Tabs.get(i);
			tab.setCtx(newCtx);
		}
	}   //  setCtx

	/**
	 * 	Clone
	 * 	@param windowNo no
	 *	@return WindowVO
	 */
	public GridWindowVO clone (int windowNo)
	{
		GridWindowVO clone = null;
		try
		{
			clone = new GridWindowVO(ctx, windowNo);
			clone.AD_Window_ID = AD_Window_ID;
			clone.Name = Name;
			clone.Description = Description;
			clone.Help = Help;
			clone.WindowType = WindowType;
			clone.AD_Image_ID = AD_Image_ID;
			clone.AD_Color_ID = AD_Color_ID;
			clone.IsReadWrite = IsReadWrite;
			clone.WinWidth = WinWidth;
			clone.WinHeight = WinHeight;
			clone.IsSOTrx = IsSOTrx;
			ctx.setContext(windowNo, "IsSOTrx", clone.IsSOTrx);
			clone.AD_Table_ID = AD_Table_ID;
			ctx.setContext(windowNo, "BaseTable_ID", clone.AD_Table_ID);
			//
			clone.Tabs = new ArrayList<GridTabVO>();
			for (int i = 0; i < Tabs.size(); i++)
			{
				GridTabVO tab = Tabs.get(i);
				GridTabVO cloneTab = tab.clone(clone.ctx, windowNo);
				if (cloneTab == null)
					return null;
				clone.Tabs.add(cloneTab);
			}
		}
		catch (Exception e)
		{
			clone = null;
		}
		return clone;
	}	//	clone

}   //  MWindowVO

