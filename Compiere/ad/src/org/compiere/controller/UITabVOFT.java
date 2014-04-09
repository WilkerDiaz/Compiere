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

/** Generated Factory Template - DO NOT CHANGE */
import java.sql.*;
import java.util.logging.*;

import org.compiere.util.*;
/** Generated FT for AD_Tab_v
 *  @author Jorg Janke (generated) 
 *  @version Release 2.6.4 - $Id: UITabVOFT.java 8244 2009-12-04 23:25:29Z freyes $ */
public class UITabVOFT extends VOFactory<UITabVO>
{
/** Standard Constructor
*/
public UITabVOFT ()
{
}
/** Info
@return info
*/
@Override
public String toString()
{
StringBuffer sb = new StringBuffer ("UITabVOFT[").append("]");
return sb.toString();
}
/** Load from ResultSet **/
@Override
protected UITabVO load (ResultSet rs)
{
UITabVO vo = new UITabVO();
String colName = null;
try
{
ResultSetMetaData rsmd = rs.getMetaData();
for (int index = 1;
 index <= rsmd.getColumnCount();
 index++)
{
colName = rsmd.getColumnName(index);

	if (colName.equalsIgnoreCase("AD_Tab_ID"))
		vo.setAD_Tab_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("AD_Window_ID"))
		vo.setAD_Window_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("AD_Table_ID"))
		vo.setAD_Table_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("AD_CtxArea_ID"))
		vo.setAD_CtxArea_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("UserDef_Role_ID"))
		vo.setUserDef_Role_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("AD_User_ID"))
		vo.setAD_User_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("AD_UserDef_Win_ID"))
		vo.setAD_UserDef_Win_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("CustomizationName"))
		vo.setCustomizationName(rs.getString(index));
	else if (colName.equalsIgnoreCase("AD_UserDef_Tab_ID"))
		vo.setAD_UserDef_Tab_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("Name"))
		vo.setName(rs.getString(index));
	else if (colName.equalsIgnoreCase("Description"))
		vo.setDescription(rs.getString(index));
	else if (colName.equalsIgnoreCase("Help"))
		vo.setHelp(rs.getString(index));
	else if (colName.equalsIgnoreCase("SeqNo"))
		vo.setSeqNo(rs.getInt(index));
	else if (colName.equalsIgnoreCase("IsSingleRow"))
		vo.setIsSingleRow("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("HasTree"))
		vo.setHasTree("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("IsInfoTab"))
		vo.setIsInfoTab("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("Referenced_Tab_ID"))
		vo.setReferenced_Tab_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("ReplicationType"))
		vo.setReplicationType("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("TableName"))
		vo.setTableName(rs.getString(index));
	else if (colName.equalsIgnoreCase("AccessLevel"))
		vo.setAccessLevel(rs.getString(index));
	else if (colName.equalsIgnoreCase("IsSecurityEnabled"))
		vo.setIsSecurityEnabled("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("IsDeleteable"))
		vo.setIsDeleteable("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("IsHighVolume"))
		vo.setIsHighVolume("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("IsView"))
		vo.setIsView("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("IsTranslationTab"))
		vo.setIsTranslationTab("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("IsReadOnly"))
		vo.setIsReadOnly("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("AD_Image_ID"))
		vo.setAD_Image_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("TabLevel"))
		vo.setTabLevel(rs.getInt(index));
	else if (colName.equalsIgnoreCase("WhereClause"))
		vo.setWhereClause(rs.getString(index));
	else if (colName.equalsIgnoreCase("OrderByClause"))
		vo.setOrderByClause(rs.getString(index));
	else if (colName.equalsIgnoreCase("CommitWarning"))
		vo.setCommitWarning(rs.getString(index));
	else if (colName.equalsIgnoreCase("ReadOnlyLogic"))
		vo.setReadOnlyLogic(rs.getString(index));
	else if (colName.equalsIgnoreCase("IsDisplayed"))
		vo.setIsDisplayed("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("DisplayLogic"))
		vo.setDisplayLogic(rs.getString(index));
	else if (colName.equalsIgnoreCase("AD_Column_ID"))
		vo.setAD_Column_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("LinkColumnName"))
		vo.setLinkColumnName(rs.getString(index));
	else if (colName.equalsIgnoreCase("AD_Process_ID"))
		vo.setAD_Process_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("IsSortTab"))
		vo.setIsSortTab("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("IsAdvancedTab"))
		vo.setIsAdvancedTab("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("IsInsertRecord"))
		vo.setIsInsertRecord("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("AD_ColumnSortOrder_ID"))
		vo.setAD_ColumnSortOrder_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("AD_ColumnSortYesNo_ID"))
		vo.setAD_ColumnSortYesNo_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("Included_Tab_ID"))
		vo.setIncluded_Tab_ID(rs.getInt(index));
}
}
catch (Exception e)
{
log.log(Level.SEVERE, colName, e);
}
return vo;
}
}
