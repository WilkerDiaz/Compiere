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
import java.util.logging.*;

import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.vos.*;


/**
 *  Field Model Value Object
 *
 *  @author Jorg Janke
 *  @version  $Id: GridFieldVO.java 9169 2010-08-05 09:24:48Z sdandapat $
 */
public class GridFieldVO extends FieldVO implements Serializable
{
	/**	Callout			*/
	public String       Callout = "";



	/**
	 *  Return the SQL statement used for the MFieldVO.create
	 *  @param ctx context
	 *  @param AD_UserDef_Win_ID user id
	 *  @return SQL with or w/o translation and 1 parameter
	 */
	public static String getSQL (Ctx ctx, int AD_UserDef_Win_ID)
	{
		//	IsActive is part of View
		String sql = "SELECT * FROM AD_Field_v WHERE AD_Tab_ID=?";
		if (!Env.isBaseLanguage(ctx, "AD_Tab"))
			sql = "SELECT * FROM AD_Field_vt WHERE AD_Tab_ID=?"
				+ " AND AD_Language='" + Env.getAD_Language(ctx) + "'";
		if (AD_UserDef_Win_ID != 0)
			sql += " AND AD_UserDef_Win_ID=" + AD_UserDef_Win_ID;
		sql += " ORDER BY IsDisplayed DESC, SeqNo";
		return sql;
	}   //  getSQL

	/**
	 *  Create Field Value Object
	 *  @param ctx context
	 *  @param WindowNo window
	 *  @param TabNo tab
	 *  @param AD_Window_ID window
	 *  @param AD_Tab_ID tab
	 *  @param readOnly r/o
	 *  @param rs resultset AD_Field_v
	 *  @return MFieldVO
	 */
	public static GridFieldVO create (Ctx ctx, int WindowNo, int TabNo, 
		int AD_Window_ID, int AD_Tab_ID, boolean readOnly, ResultSet rs)
	{
		GridFieldVO vo = new GridFieldVO (ctx, WindowNo, TabNo, 
			AD_Window_ID, AD_Tab_ID, readOnly);
		String columnName = "ColumnName";
		try
		{
			vo.ColumnName = rs.getString("ColumnName");
			if (vo.ColumnName == null)
				return null;

			CLogger.get().fine(vo.ColumnName);

			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); i++)
			{
				columnName = rsmd.getColumnName (i);
				if (columnName.equalsIgnoreCase("Name"))
					vo.Header = rs.getString (i);
				else if (columnName.equalsIgnoreCase("AD_Reference_ID"))
					vo.displayType = rs.getInt (i);
				else if (columnName.equalsIgnoreCase("AD_Column_ID"))
					vo.AD_Column_ID = rs.getInt (i);
				else if (columnName.equalsIgnoreCase("AD_Table_ID"))
					vo.AD_Table_ID = rs.getInt (i);
				else if (columnName.equalsIgnoreCase("DisplayLength"))
					vo.DisplayLength = rs.getInt (i);
				else if (columnName.equalsIgnoreCase("IsSameLine"))
					vo.IsSameLine = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("IsDisplayed"))
					vo.IsDisplayed = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("DisplayLogic"))
					vo.DisplayLogic = rs.getString (i);
				else if (columnName.equalsIgnoreCase("DefaultValue"))
					vo.DefaultValue = rs.getString (i);
				else if (columnName.equalsIgnoreCase("IsMandatoryUI"))
					vo.IsMandatoryUI = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("IsReadOnly"))
					vo.IsReadOnly = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("IsUpdateable"))
					vo.IsUpdateable = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("IsAlwaysUpdateable"))
					vo.IsAlwaysUpdateable = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("IsHeading"))
					vo.IsHeading = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("IsFieldOnly"))
					vo.IsFieldOnly = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("IsEncryptedField"))
					vo.IsEncryptedField = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("IsEncryptedColumn"))
					vo.IsEncryptedColumn = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("IsSelectionColumn"))
					vo.IsSelectionColumn = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("SelectionSeqNo"))
					vo.SelectionSeqNo = rs.getInt(i);
				else if (columnName.equalsIgnoreCase("SortNo"))
					vo.SortNo = rs.getInt (i);
				else if (columnName.equalsIgnoreCase("FieldLength"))
					vo.FieldLength = rs.getInt (i);
				else if (columnName.equalsIgnoreCase("VFormat"))
					vo.VFormat = rs.getString (i);
				else if (columnName.equalsIgnoreCase("ValueMin"))
					vo.ValueMin = rs.getString (i);
				else if (columnName.equalsIgnoreCase("ValueMax"))
					vo.ValueMax = rs.getString (i);
				else if (columnName.equalsIgnoreCase("FieldGroup"))
					vo.FieldGroup = rs.getString (i);
				else if (columnName.equalsIgnoreCase("IsKey"))
					vo.IsKey = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("IsParent"))
					vo.IsParent = "Y".equals(rs.getString (i));
				else if (columnName.equalsIgnoreCase("Description"))
					vo.Description = rs.getString (i);
				else if (columnName.equalsIgnoreCase("Help"))
					vo.Help = rs.getString (i);
				else if (columnName.equalsIgnoreCase("Callout"))
					vo.Callout = rs.getString (i);
				else if (columnName.equalsIgnoreCase("AD_Process_ID"))
					vo.AD_Process_ID = rs.getInt (i);
				else if (columnName.equalsIgnoreCase("ReadOnlyLogic"))
					vo.ReadOnlyLogic = rs.getString (i);
				else if (columnName.equalsIgnoreCase("MandatoryLogic"))
					vo.mandatoryLogic = rs.getString (i);
				else if (columnName.equalsIgnoreCase("ObscureType"))
					vo.ObscureType = rs.getString (i);
				else if (columnName.equalsIgnoreCase("IsDefaultFocus"))
					vo.IsDefaultFocus = "Y".equals(rs.getString(i));
				//
				else if (columnName.equalsIgnoreCase("AD_Reference_Value_ID"))
					vo.AD_Reference_Value_ID = rs.getInt(i);
				else if (columnName.equalsIgnoreCase("ValidationCode"))
					vo.ValidationCode = rs.getString(i);
				else if (columnName.equalsIgnoreCase("ColumnSQL"))
					vo.ColumnSQL = rs.getString(i);
				else if (columnName.equalsIgnoreCase("IsCopy"))
					vo.IsCopy = "Y".equalsIgnoreCase(rs.getString(i));
			}
			if (vo.Header == null)
				vo.Header = vo.ColumnName;
			
			if (vo.AD_Process_ID != 0){
				vo.IsReadOnly = isReadonlyField(ctx, vo.AD_Process_ID);
			}
			
		}
		catch (SQLException e)
		{
			CLogger.get().log(Level.SEVERE, "ColumnName=" + columnName, e);
			return null;
		}
		vo.initFinish();
		return vo;
	}   //  create

	/**
	 *  Init Field for Process Parameter
	 *  @param ctx context
	 *  @param WindowNo window
	 *  @param rs result set AD_Process_Para
	 *  @return MFieldVO
	 */
	public static GridFieldVO createParameter (Ctx ctx, int WindowNo, ResultSet rs)
	{
		GridFieldVO vo = new GridFieldVO (ctx, WindowNo, 0, 0, 0, false);
		vo.isProcess = true;
		vo.IsDisplayed = true;
		vo.IsReadOnly = false;
		vo.IsUpdateable = true;

		try
		{
			vo.AD_Table_ID = 0;
			vo.AD_Column_ID = rs.getInt("AD_Process_Para_ID");	//	**
			vo.ColumnName = rs.getString("ColumnName");
			vo.Header = rs.getString("Name");
			vo.Description = rs.getString("Description");
			vo.Help = rs.getString("Help");
			vo.displayType = rs.getInt("AD_Reference_ID");
			vo.AD_Reference_ID = rs.getInt("AD_Reference_ID");
			vo.IsMandatoryUI = rs.getString("IsMandatoryUI").equals("Y");
			vo.FieldLength = rs.getInt("FieldLength");
			vo.DisplayLength = vo.FieldLength;
			vo.DefaultValue = rs.getString("DefaultValue");
			vo.DefaultValue2 = rs.getString("DefaultValue2");
			vo.VFormat = rs.getString("VFormat");
			vo.ValueMin = rs.getString("ValueMin");
			vo.ValueMax = rs.getString("ValueMax");
			vo.IsEncryptedField = rs.getString("IsEncrypted").equals("Y");
			vo.isRange = rs.getString("IsRange").equals("Y");
			//
			vo.AD_Reference_Value_ID = rs.getInt("AD_Reference_Value_ID");
			vo.ValidationCode = rs.getString("ValidationCode");
		}
		catch (SQLException e)
		{
			CLogger.get().log(Level.SEVERE, "createParameter", e);
		}
		//
		vo.initFinish();
		if (vo.DefaultValue2 == null)
			vo.DefaultValue2 = "";
		return vo;
	}   //  createParameter

	/**
	 *  Init Field for Dashboard Process Parameter
	 *  @param ctx context
	 *  @param WindowNo window
	 *  @param rs result set AD_Process_Para
	 *  @return MFieldVO
	 */
	public static GridFieldVO createDashboardParameter (Ctx ctx, int WindowNo, ResultSet rs)
	{
		GridFieldVO vo = new GridFieldVO (ctx, WindowNo, 0, 0, 0, false);
		vo.isProcess = true;
		vo.IsDisplayed = true;
		vo.IsReadOnly = false;
		vo.IsUpdateable = true;

		try
		{
			vo.AD_Table_ID = 0;
			vo.AD_Column_ID = rs.getInt("AD_Column_ID");
			vo.ColumnName = rs.getString("ColumnName");
			vo.Header = rs.getString("Name");
			vo.Description = rs.getString("Description");
			vo.Help = rs.getString("Help");
			vo.displayType = rs.getInt("AD_Reference_ID");
			vo.AD_Reference_ID = rs.getInt("AD_Reference_ID");
			vo.IsMandatoryUI = rs.getString("IsMandatoryUI").equals("Y");
			vo.FieldLength = rs.getInt("FieldLength");
			vo.DisplayLength = rs.getInt("DisplayLength");
			vo.AD_Reference_Value_ID = rs.getInt("AD_Reference_Value_ID");
			vo.ValidationCode = rs.getString("ValidationCode");
			vo.DisplayLogic = rs.getString("DisplayLogic");
		}
		catch (SQLException e)
		{
			CLogger.get().log(Level.SEVERE, "createParameter", e);
		}
		//
		vo.initFinish();
		if (vo.DefaultValue2 == null)
			vo.DefaultValue2 = "";
		return vo;
	}   //  createDashboardParameter
	
	/**
	 *  Create range "to" Parameter Field from "from" Parameter Field
	 *  @param voF field value object
	 *  @return to MFieldVO
	 */
	public static GridFieldVO createParameter (GridFieldVO voF)
	{
		GridFieldVO voT = new GridFieldVO (voF.ctx, voF.WindowNo, voF.TabNo, 
			voF.AD_Window_ID, voF.AD_Tab_ID, voF.tabReadOnly);
		voT.isProcess = true;
		voT.IsDisplayed = true;
		voT.IsReadOnly = false;
		voT.IsUpdateable = true;
		//
		voT.AD_Table_ID = voF.AD_Table_ID;
		voT.AD_Column_ID = voF.AD_Column_ID;    //  AD_Process_Para_ID
		voT.ColumnName = voF.ColumnName;
		voT.Header = voF.Header;
		voT.Description = voF.Description;
		voT.Help = voF.Help;
		voT.displayType = voF.displayType;
		voT.IsMandatoryUI = voF.IsMandatoryUI;
		voT.FieldLength = voF.FieldLength;
		voT.DisplayLength = voF.FieldLength;
		voT.DefaultValue = voF.DefaultValue2;
		voT.VFormat = voF.VFormat;
		voT.ValueMin = voF.ValueMin;
		voT.ValueMax = voF.ValueMax;
		voT.isRange = voF.isRange;
		//
		return voT;
	}   //  createParameter


	/**
	 *  Make a standard field (Created/Updated/By)
	 *  @param ctx context
	 *  @param WindowNo window
	 *  @param TabNo tab
	 *  @param AD_Window_ID window
	 *  @param AD_Tab_ID tab
	 *  @param tabReadOnly rab is r/o
	 *  @param isCreated is Created field
	 *  @param isTimestamp is the timestamp (not by)
	 *  @return MFieldVO
	 */
	public static GridFieldVO createStdField (Ctx ctx, int WindowNo, int TabNo, 
		int AD_Window_ID, int AD_Tab_ID, boolean tabReadOnly,
		boolean isCreated, boolean isTimestamp)
	{
		GridFieldVO vo = new GridFieldVO (ctx, WindowNo, TabNo, 
			AD_Window_ID, AD_Tab_ID, tabReadOnly);
		vo.ColumnName = isCreated ? "Created" : "Updated";
		if (!isTimestamp)
			vo.ColumnName += "By";
		vo.displayType = isTimestamp ? DisplayTypeConstants.DateTime : DisplayTypeConstants.Table;
		if (!isTimestamp)
			vo.AD_Reference_Value_ID = 110;		//	AD_User Table Reference
		vo.IsDisplayed = false;
		vo.IsMandatoryUI = false;
		vo.IsReadOnly = false;
		vo.IsUpdateable = true;
		vo.initFinish();
		return vo;
	}   //  initStdField

	
	/**************************************************************************
	 *  Private constructor.
	 *  @param Ctx context
	 *  @param windowNo window
	 *  @param tabNo tab
	 *  @param ad_Window_ID window
	 *  @param ad_Tab_ID tab
	 *  @param TabReadOnly tab read only
	 */
	private GridFieldVO (Ctx newCtx, int windowNo, int tabNo, 
		int ad_Window_ID, int ad_Tab_ID, boolean TabReadOnly)
	{
		ctx = newCtx;
		WindowNo = windowNo;
		TabNo = tabNo;
		AD_Window_ID = ad_Window_ID;
		AD_Tab_ID = ad_Tab_ID;
		tabReadOnly = TabReadOnly;
	}   //  MFieldVO

	
	

	/**
	 * Copy constructor from a FieldVO
	 * @param newCtx
	 * @param f
	 */
	public GridFieldVO( Ctx newCtx, FieldVO f )
	{
		GridFieldVO vo = this;
		this.ctx = newCtx;
		vo.isProcess = true;
		vo.IsDisplayed = true;
		vo.IsReadOnly = false;
		vo.IsUpdateable = true;

		vo.AD_Table_ID = 0;
		vo.AD_Column_ID = 0;
		vo.ColumnName = f.ColumnName;
		vo.Header = "";
		vo.Description = f.Description;
		vo.Help = f.Help;
		vo.displayType = f.displayType;
		vo.IsMandatoryUI = f.IsMandatoryUI;
		vo.FieldLength = f.FieldLength;
		vo.DisplayLength = f.DisplayLength;
		vo.DefaultValue = f.DefaultValue;
		vo.DefaultValue2 = f.DefaultValue2;
		vo.VFormat = f.VFormat;
		vo.ValueMin = f.ValueMin;
		vo.ValueMax = f.ValueMax;
		vo.isRange = f.isRange;
		//
		vo.AD_Reference_Value_ID = f.AD_Reference_Value_ID;
		vo.ValidationCode = f.ValidationCode;
		//
		vo.initFinish();
		if (vo.DefaultValue2 == null)
			vo.DefaultValue2 = "";		
	}
	
	
	
	static final long serialVersionUID = 4385061125114436797L;
	
	/** Context                     */
	public Ctx			ctx = null;
	/** Lookup Value Object     */
	public MLookupInfo  lookupInfo = null;

	
	/**
	 *  Set Context including contained elements
	 *  @param newCtx new context
	 */
	public void setCtx (Ctx newCtx)
	{
		ctx = newCtx;
	}   //  setCtx

	/**
	 *  Validate Fields and create LookupInfo if required
	 */
	protected void initFinish()
	{
		//  Not null fields
		if (DisplayLogic == null)
			DisplayLogic = "";
		if (DefaultValue == null)
			DefaultValue = "";
		if (FieldGroup == null)
			FieldGroup = "";
		if (Description == null)
			Description = "";
		if (Help == null)
			Help = "";
		if (Callout == null)
			Callout = "";
		if (ReadOnlyLogic == null)
			ReadOnlyLogic = "";
		if(mandatoryLogic == null)
			mandatoryLogic = "";


		//  Create Lookup, if not ID
		if (FieldType.isLookup(displayType))
		{
			try
			{
				lookupInfo = MLookupFactory.getLookupInfo (ctx, WindowNo, displayType, 
					AD_Column_ID, Env.getLanguage(ctx), ColumnName, AD_Reference_Value_ID,
					IsParent, ValidationCode);
			}
			catch (Exception e)     //  Cannot create Lookup
			{
				CLogger.get().log(Level.SEVERE, "No LookupInfo for " + ColumnName, e);
				displayType = DisplayTypeConstants.ID;
			}
		}
	}   //  initFinish

	/**
	 * 	Clone Field.
	 *	@param Ctx ctx
	 *	@param windowNo window no
	 *	@param tabNo tab no
	 *	@param ad_Window_ID window id
	 *	@param ad_Tab_ID tab id
	 *	@param TabReadOnly r/o
	 *	@return Field or null
	 */
	protected GridFieldVO clone(Ctx Ctx, int windowNo, int tabNo, 
		int ad_Window_ID, int ad_Tab_ID, 
		boolean TabReadOnly)
	{
		GridFieldVO clone = new GridFieldVO(Ctx, windowNo, tabNo, 
			ad_Window_ID, ad_Tab_ID, TabReadOnly);
		//
		clone.isProcess = false;
		//  Database Fields
		clone.ColumnName = ColumnName;
		clone.ColumnSQL = ColumnSQL;
		clone.Header = Header;
		clone.displayType = displayType;
		clone.AD_Table_ID = AD_Table_ID;
		clone.AD_Column_ID = AD_Column_ID;
		clone.DisplayLength = DisplayLength;
		clone.IsSameLine = IsSameLine;
		clone.IsDisplayed = IsDisplayed;
		clone.DisplayLogic = DisplayLogic;
		clone.DefaultValue = DefaultValue;
		clone.IsMandatoryUI = IsMandatoryUI;
		clone.IsReadOnly = IsReadOnly;
		clone.IsUpdateable = IsUpdateable;
		clone.IsAlwaysUpdateable = IsAlwaysUpdateable;
		clone.IsHeading = IsHeading;
		clone.IsFieldOnly = IsFieldOnly;
		clone.IsEncryptedField = IsEncryptedField;
		clone.IsEncryptedColumn = IsEncryptedColumn;
		clone.IsSelectionColumn = IsSelectionColumn;
		clone.SelectionSeqNo = SelectionSeqNo;
		clone.SortNo = SortNo;
		clone.FieldLength = FieldLength;
		clone.VFormat = VFormat;
		clone.ValueMin = ValueMin;
		clone.ValueMax = ValueMax;
		clone.FieldGroup = FieldGroup;
		clone.IsKey = IsKey;
		clone.IsParent = IsParent;
		clone.Callout = Callout;
		clone.AD_Process_ID = AD_Process_ID;
		clone.Description = Description;
		clone.Help = Help;
		clone.ReadOnlyLogic = ReadOnlyLogic;
		clone.mandatoryLogic = mandatoryLogic;
		clone.ObscureType = ObscureType;
		clone.IsDefaultFocus = IsDefaultFocus;
		//	Lookup
		clone.ValidationCode = ValidationCode;
		clone.AD_Reference_Value_ID = AD_Reference_Value_ID;
		clone.lookupInfo = lookupInfo;

		//  Process Parameter
		clone.isRange = isRange;
		clone.DefaultValue2 = DefaultValue2;

		return clone;
	}	//	clone
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MFieldVO[");
		sb.append(AD_Column_ID).append("-").append(ColumnName)
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * 	Is Read Only
	 *	@param ctx context
	 *	@param Process_ID
	 *	@return
	 */
	public static boolean isReadonlyField (Ctx ctx, int Process_ID)
	{
		boolean b = false;
		MRole role = MRole.get(ctx, ctx.getAD_Role_ID());
		Boolean access = false;
		if (Process_ID != 0){
			access = role.getProcessAccess(Process_ID);
			if (access == null || !access.booleanValue())
				b = true;
		}
		return b;
	}
	
}   //  MFieldVO
