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
package org.compiere.controller;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Model Tab Value Object
 *
 *  @author Jorg Janke
 *  @version  $Id: GridTabVO.java 8751 2010-05-12 16:49:31Z nnayak $
 */
public class GridTabVO implements Evaluatee, Serializable
{
	/**************************************************************************
	 *	Create MTab VO
	 *
	 *  @param wVO value object
	 * @param TabNo tab no
	 * @param rs ResultSet from AD_Tab_v
	 * @param isRO true if window is r/o
	 * @param p_trx TODO
	 * @param onlyCurrentRows if true query is limited to not processed records
	 *  @return TabVO
	 */
	public static GridTabVO create (GridWindowVO wVO, int TabNo, ResultSet rs, 
		boolean isRO, int onlyCurrentDays, int AD_UserDef_Win_ID, Trx p_trx)
	{
		CLogger.get().config("#" + TabNo);

		GridTabVO vo = new GridTabVO (wVO.ctx, wVO.WindowNo);
		vo.AD_Window_ID = wVO.AD_Window_ID;
		vo.TabNo = TabNo;
		//
		if (!loadTabDetails(vo, rs))
			return null;

		if (isRO)
		{
			CLogger.get().fine("Tab is ReadOnly");
			vo.IsReadOnly = true;
		}
		vo.onlyCurrentDays = onlyCurrentDays;

		//  Create Fields
		if (vo.IsSortTab)
		{
			vo.Fields = new ArrayList<GridFieldVO>();	//	dummy
		}
		else
		{
			createFields (vo, AD_UserDef_Win_ID, p_trx);
			if (vo.Fields == null || vo.Fields.size() == 0)
			{
				CLogger.get().log(Level.SEVERE, vo.Name + ": No Fields");
				return null;
			}
		}
		return vo;
	}	//	create

	/**
	 * 	Load Tab Details from rs into vo
	 * 	@param vo Tab value object
	 *	@param rs ResultSet from AD_Tab_v/t
	 * 	@return true if read ok
	 */
	private static boolean loadTabDetails (GridTabVO vo, ResultSet rs)
	{
		MRole role = MRole.getDefault(vo.ctx, false);
		boolean showTrl = "Y".equals(vo.ctx.getContext("#ShowTrl"));
		boolean showAcct = "Y".equals(vo.ctx.getContext("#ShowAcct"));
		boolean showAdvanced = "Y".equals(vo.ctx.getContext("#ShowAdvanced"));
	//	CLogger.get().warning("ShowTrl=" + showTrl + ", showAcct=" + showAcct);
		try
		{
			vo.AD_Tab_ID = rs.getInt("AD_Tab_ID");
			vo.Referenced_Tab_ID = rs.getInt ("Referenced_Tab_ID");
			vo.ctx.setContext (vo.WindowNo, vo.TabNo, "AD_Tab_ID", String.valueOf(vo.AD_Tab_ID));
			vo.Name = rs.getString("Name");
			vo.ctx.setContext(vo.WindowNo, vo.TabNo, "Name", vo.Name);

			//	Translation Tab	**
			if (rs.getString("IsTranslationTab").equals("Y"))
			{
				//	Document Translation
				vo.TableName = rs.getString("TableName");
				if (!Env.isBaseTranslation(vo.TableName)	//	C_UOM, ...
					&& !Env.isMultiLingualDocument(vo.ctx))
					showTrl = false;
				if (!showTrl)
				{
					CLogger.get().config("TrlTab Not displayed - AD_Tab_ID=" 
						+ vo.AD_Tab_ID + "=" + vo.Name + ", Table=" + vo.TableName
						+ ", BaseTrl=" + Env.isBaseTranslation(vo.TableName)
						+ ", MultiLingual=" + Env.isMultiLingualDocument(vo.ctx));
					return false;
				}
			}
			//	Advanced Tab	**
			if (!showAdvanced && rs.getString("IsAdvancedTab").equals("Y"))
			{
				CLogger.get().config("AdvancedTab Not displayed - AD_Tab_ID=" 
					+ vo.AD_Tab_ID + " " + vo.Name);
				return false;
			}
			//	Accounting Info Tab	**
			if (!showAcct && rs.getString("IsInfoTab").equals("Y"))
			{
				CLogger.get().fine("AcctTab Not displayed - AD_Tab_ID=" 
					+ vo.AD_Tab_ID + " " + vo.Name);
				return false;
			}
			
			//	DisplayLogic
			vo.DisplayLogic = rs.getString("DisplayLogic");
			
			//	Access Level
			vo.AccessLevel = rs.getString("AccessLevel");
			if (!role.canView (vo.ctx, vo.AccessLevel))	//	No Access
			{
				CLogger.get().fine("No Role Access - AD_Tab_ID=" + vo.AD_Tab_ID + " " + vo. Name);
				return false;
			}	//	Used by MField.getDefault
			vo.ctx.setContext(vo.WindowNo, vo.TabNo, "AccessLevel", vo.AccessLevel);

			//	Table Access
			vo.AD_Table_ID = rs.getInt("AD_Table_ID");
			vo.ctx.setContext(vo.WindowNo, vo.TabNo, "AD_Table_ID", String.valueOf(vo.AD_Table_ID));
			if (!role.isTableAccess(vo.AD_Table_ID, true))
			{
				CLogger.get().config("No Table Access - AD_Tab_ID=" 
					+ vo.AD_Tab_ID + " " + vo. Name);
				return false;
			}
			if (rs.getString("IsReadOnly").equals("Y"))
				vo.IsReadOnly = true;
			vo.ReadOnlyLogic = rs.getString("ReadOnlyLogic");
			//newly added code by Nadeem
			if(vo.ReadOnlyLogic!=null && !vo.ReadOnlyLogic.equals(""))
			{
				if(Evaluator.evaluateLogic(vo,vo.ReadOnlyLogic))
				{
					vo.IsReadOnly=true;
				}
			}
			// end of new Code
			
			if (rs.getString("IsInsertRecord").equals("N"))
				vo.IsInsertRecord = false;
			
			//
			vo.Description = rs.getString("Description");
			if (vo.Description == null)
				vo.Description = "";
			vo.Help = rs.getString("Help");
			if (vo.Help == null)
				vo.Help = "";

			if (rs.getString("IsSingleRow").equals("Y"))
				vo.IsSingleRow = true;
			if (rs.getString("HasTree").equals("Y"))
				vo.HasTree = true;

			vo.AD_Table_ID = rs.getInt("AD_Table_ID");
			vo.TableName = rs.getString("TableName");
			if (rs.getString("IsView").equals("Y"))
				vo.IsView = true;
			vo.AD_Column_ID = rs.getInt("AD_Column_ID");   //  Primary Parent Column

			if (rs.getString("IsSecurityEnabled").equals("Y"))
				vo.IsSecurityEnabled = true;
			if (rs.getString("IsDeleteable").equals("Y"))
				vo.IsDeleteable = true;
			if (rs.getString("IsHighVolume").equals("Y"))
				vo.IsHighVolume = true;

			vo.CommitWarning = rs.getString("CommitWarning");
			if (vo.CommitWarning == null)
				vo.CommitWarning = "";
			vo.WhereClause = rs.getString("WhereClause");
			if (vo.WhereClause == null)
				vo.WhereClause = "";

			vo.OrderByClause = rs.getString("OrderByClause");
			if (vo.OrderByClause == null)
				vo.OrderByClause = "";

			vo.AD_Process_ID = rs.getInt("AD_Process_ID");
			if (rs.wasNull())
				vo.AD_Process_ID = 0;
			vo.AD_Image_ID = rs.getInt("AD_Image_ID");
			if (rs.wasNull())
				vo.AD_Image_ID = 0;
			vo.Included_Tab_ID = rs.getInt("Included_Tab_ID");
			if (rs.wasNull())
				vo.Included_Tab_ID = 0;
			//
			vo.TabLevel = rs.getInt("TabLevel");
			if (rs.wasNull())
				vo.TabLevel = 0;
			//
			vo.IsSortTab = rs.getString("IsSortTab").equals("Y");
			if (vo.IsSortTab)
			{
				vo.AD_ColumnSortOrder_ID = rs.getInt("AD_ColumnSortOrder_ID");
				vo.AD_ColumnSortYesNo_ID = rs.getInt("AD_ColumnSortYesNo_ID");
			}
			//
			//	Replication Type - set R/O if Reference
			try
			{
				int index = rs.findColumn ("ReplicationType");
				vo.ReplicationType = rs.getString (index);
				if ("R".equals(vo.ReplicationType))
					vo.IsReadOnly = true;
			}
			catch (Exception e)
			{
			}
		}
		catch (SQLException ex)
		{
			CLogger.get().log(Level.SEVERE, "", ex);
			return false;
		}
		
		return true;
	}	//	loadTabDetails


	/**************************************************************************
	 *  Create Tab Fields
	 *  @param mTabVO tab value object
	 * @param p_trx TODO
	 *  @return true if fields were created
	 */
	private static boolean createFields (GridTabVO mTabVO, int AD_UserDef_Win_ID, Trx p_trx)
	{
		mTabVO.Fields = new ArrayList<GridFieldVO>();

		String sql = GridFieldVO.getSQL(mTabVO.ctx, AD_UserDef_Win_ID);
		int AD_Tab_ID = mTabVO.Referenced_Tab_ID;
		if (AD_Tab_ID == 0)
			AD_Tab_ID = mTabVO.AD_Tab_ID;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = p_trx.getConnection().prepareStatement(sql);
			pstmt.setInt(1, AD_Tab_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				GridFieldVO voF = GridFieldVO.create (mTabVO.ctx, 
					mTabVO.WindowNo, mTabVO.TabNo, 
					mTabVO.AD_Window_ID, AD_Tab_ID, 
					mTabVO.IsReadOnly, rs);
				if (voF != null)
					mTabVO.Fields.add(voF);
			}
		}
		catch (Exception e)	{
			CLogger.get().log(Level.SEVERE, sql, e);
			return false;
		} 
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return mTabVO.Fields.size() != 0;
	}   //  createFields

	/**
	 *  Return the SQL statement used for the MTabVO.create
	 *  @param ctx context
	 *  @return SQL SELECT String
	 */
	public static String getSQL (Ctx ctx, int AD_UserDef_Win_ID)
	{
		//  View only returns IsActive='Y'
		String sql = "SELECT * FROM AD_Tab_v WHERE AD_Window_ID=?";
		if (!Env.isBaseLanguage(ctx, "AD_Window"))
			sql = "SELECT * FROM AD_Tab_vt WHERE AD_Window_ID=?"
				+ " AND AD_Language='" + Env.getAD_Language(ctx) + "'";
		if (AD_UserDef_Win_ID != 0)
			sql += " AND AD_UserDef_Win_ID=" + AD_UserDef_Win_ID;
		sql += " ORDER BY SeqNo";
		return sql;
	}   //  getSQL
	
	
	/**************************************************************************
	 *  Private constructor - must use Factory
	 *  @param Ctx context
	 *  @param windowNo window
	 */
	private GridTabVO (Ctx newCtx, int windowNo)
	{
		ctx = newCtx;
		WindowNo = windowNo;
	}   //  MTabVO

	static final long serialVersionUID = 9160212869277319305L;
	
	/** Context - replicated    */
	public  Ctx		      ctx;
	/** Window No - replicated  */
	public  int				WindowNo;
	/** AD Window - replicated  */
	public  int             AD_Window_ID;

	/** Tab No (not AD_Tab_ID) 0.. */
	public  int				TabNo;

	/**	Tab	ID			*/
	public	int		    AD_Tab_ID;
	/**	Tab	ID			*/
	public	int		    Referenced_Tab_ID;
	/** Name			*/
	public	String	    Name = "";
	/** Description		*/
	public	String	    Description = "";
	/** Help			*/
	public	String	    Help = "";
	/** Single Row		*/
	public	boolean	    IsSingleRow = false;
	/** Read Only		*/
	public  boolean     IsReadOnly = false;
	/** Insert Record	*/
	public 	boolean		IsInsertRecord = true;
	/** Tree			*/
	public  boolean	    HasTree = false;
	/** Table			*/
	public  int		    AD_Table_ID;
	/** Primary Parent Column   */
	public  int		    AD_Column_ID = 0;
	/** Table Name		*/
	public  String	    TableName;
	/** Table is View	*/
	public  boolean     IsView = false;
	/** Table Access Level	*/
	public  String	    AccessLevel;
	/** Security		*/
	public  boolean	    IsSecurityEnabled = false;
	/** Table Deleteable	*/
	public  boolean	    IsDeleteable = false;
	/** Table High Volume	*/
	public  boolean     IsHighVolume = false;
	/** Process			*/
	public	int		    AD_Process_ID = 0;
	/** Commit Warning	*/
	public  String	    CommitWarning;
	/** Where			*/
	public  String	    WhereClause;
	/** Order by		*/
	public  String      OrderByClause;
	/** Tab Read Only	*/
	public  String      ReadOnlyLogic;
	/** Tab Display		*/
	public  String      DisplayLogic;
	/** Level			*/
	public  int         TabLevel = 0;
	/** Image			*/
	public int          AD_Image_ID = 0;
	/** Included Tab	*/
	public int          Included_Tab_ID = 0;
	/** Replication Type	*/
	public String		ReplicationType = "L";

	/** Sort Tab			*/
	public boolean		IsSortTab = false;
	/** Column Sort			*/
	public int			AD_ColumnSortOrder_ID = 0;
	/** Column Displayed	*/
	public int			AD_ColumnSortYesNo_ID = 0;

	/**	Only Current Days - derived	*/
	public int			onlyCurrentDays = 0;

	/** Fields contain MFieldVO entities    */
	public ArrayList<GridFieldVO>	Fields = null;

	
	/**
	 *  Set Context including contained elements
	 *  @param newCtx new context
	 */
	public void setCtx (Ctx newCtx)
	{
		ctx = newCtx;
		for (int i = 0; i < Fields.size() ; i++)
		{
			GridFieldVO field = Fields.get(i);
			field.setCtx(newCtx);
		}
	}   //  setCtx
	
	/**
	 * 	Get Variable Value (Evaluatee)
	 *	@param variableName name
	 *	@return value
	 */
	public String get_ValueAsString (String variableName)
	{
		return ctx.getContext(WindowNo, variableName, false);	// not just window
	}	//	get_ValueAsString

	/**
	 * 	Clone
	 * 	@param Ctx context
	 * 	@param windowNo no
	 *	@return MTabVO or null
	 */
	public GridTabVO clone(Ctx myCtx, int windowNo)
	{
		GridTabVO clone = new GridTabVO(myCtx, windowNo);
		clone.AD_Window_ID = AD_Window_ID;
		clone.TabNo = TabNo;
		myCtx.setContext(windowNo, clone.TabNo, "AD_Tab_ID", String.valueOf(clone.AD_Tab_ID));
		//
		clone.AD_Tab_ID = AD_Tab_ID;
		clone.Referenced_Tab_ID = Referenced_Tab_ID;
		clone.Name = Name;
		myCtx.setContext(windowNo, clone.TabNo, "Name", clone.Name);
		clone.Description = Description;
		clone.Help = Help;
		clone.IsSingleRow = IsSingleRow;
		clone.IsReadOnly = IsReadOnly;
		clone.IsInsertRecord = IsInsertRecord;
		clone.HasTree = HasTree;
		clone.AD_Table_ID = AD_Table_ID;
		clone.AD_Column_ID = AD_Column_ID;
		clone.TableName = TableName;
		clone.IsView = IsView;
		clone.AccessLevel = AccessLevel;
		clone.IsSecurityEnabled = IsSecurityEnabled;
		clone.IsDeleteable = IsDeleteable;
		clone.IsHighVolume = IsHighVolume;
		clone.AD_Process_ID = AD_Process_ID;
		clone.CommitWarning = CommitWarning;
		clone.WhereClause = WhereClause;
		clone.OrderByClause = OrderByClause;
		clone.ReadOnlyLogic = ReadOnlyLogic;
		clone.DisplayLogic = DisplayLogic;
		clone.TabLevel = TabLevel;
		clone.AD_Image_ID = AD_Image_ID;
		clone.Included_Tab_ID = Included_Tab_ID;
		clone.ReplicationType = ReplicationType;
		myCtx.setContext(windowNo, clone.TabNo, "AccessLevel", clone.AccessLevel);
		myCtx.setContext(windowNo, clone.TabNo, "AD_Table_ID", String.valueOf(clone.AD_Table_ID));

		//
		clone.IsSortTab = IsSortTab;
		clone.AD_ColumnSortOrder_ID = AD_ColumnSortOrder_ID;
		clone.AD_ColumnSortYesNo_ID = AD_ColumnSortYesNo_ID;
		//  Derived
		clone.onlyCurrentDays = 0;

		clone.Fields = new ArrayList<GridFieldVO>();
		for (int i = 0; i < Fields.size(); i++)
		{
			GridFieldVO field = Fields.get(i);
			GridFieldVO cloneField = field.clone(myCtx, windowNo, TabNo, 
				AD_Window_ID, AD_Tab_ID, IsReadOnly);
			if (cloneField == null)
				return null;
			clone.Fields.add(cloneField);
		}
		
		return clone;
	}	//	clone

}   //  MTabVO

