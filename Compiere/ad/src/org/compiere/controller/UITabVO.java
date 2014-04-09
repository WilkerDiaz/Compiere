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

/** Generated VO - DO NOT CHANGE */
import org.compiere.framework.*;

/** Generated VO for AD_Tab_v
 *  @author Jorg Janke (generated) 
 *  @version Release 2.6.4 - $Id: UITabVO.java 8244 2009-12-04 23:25:29Z freyes $ */
public class UITabVO extends VO implements java.io.Serializable
{
/** Standard Constructor
*/
public UITabVO()
{
super();
}
/** Clone Constructor
*/
public UITabVO(VO vo)
{
super(vo);
}
/** Serial Version No */
static final long serialVersionUID = 38026282464911813L;
/** Created Timestamp */
public static final long createdMS = 1189540928251L;
/** Info
@return info
*/
@Override
public String toString()
{
StringBuffer sb = new StringBuffer ("UITabVO[#").append(size()).append("]");
return sb.toString();
}
/** Get: Tab */
public int getAD_Tab_ID()
{
String value = get("AD_Tab_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Tab */
protected void setAD_Tab_ID(int p_AD_Tab_ID)
{
	put("AD_Tab_ID", p_AD_Tab_ID);
}
/** Set String: Tab */
protected void setAD_Tab_ID(String p_AD_Tab_ID)
{
	setAD_Tab_ID(PO.convertToInt(p_AD_Tab_ID));
}
/** Get: Window */
public int getAD_Window_ID()
{
String value = get("AD_Window_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Window */
protected void setAD_Window_ID(int p_AD_Window_ID)
{
	put("AD_Window_ID", p_AD_Window_ID);
}
/** Set String: Window */
protected void setAD_Window_ID(String p_AD_Window_ID)
{
	setAD_Window_ID(PO.convertToInt(p_AD_Window_ID));
}
/** Get: Table */
public int getAD_Table_ID()
{
String value = get("AD_Table_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Table */
protected void setAD_Table_ID(int p_AD_Table_ID)
{
	put("AD_Table_ID", p_AD_Table_ID);
}
/** Set String: Table */
protected void setAD_Table_ID(String p_AD_Table_ID)
{
	setAD_Table_ID(PO.convertToInt(p_AD_Table_ID));
}
/** Get: Context Area */
public int getAD_CtxArea_ID()
{
String value = get("AD_CtxArea_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Context Area */
protected void setAD_CtxArea_ID(int p_AD_CtxArea_ID)
{
	put("AD_CtxArea_ID", p_AD_CtxArea_ID);
}
/** Set String: Context Area */
protected void setAD_CtxArea_ID(String p_AD_CtxArea_ID)
{
	setAD_CtxArea_ID(PO.convertToInt(p_AD_CtxArea_ID));
}
/** Get: User Def Role */
public int getUserDef_Role_ID()
{
String value = get("UserDef_Role_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: User Def Role */
protected void setUserDef_Role_ID(int p_UserDef_Role_ID)
{
	put("UserDef_Role_ID", p_UserDef_Role_ID);
}
/** Set String: User Def Role */
protected void setUserDef_Role_ID(String p_UserDef_Role_ID)
{
	setUserDef_Role_ID(PO.convertToInt(p_UserDef_Role_ID));
}
/** Get: User/Contact */
public int getAD_User_ID()
{
String value = get("AD_User_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: User/Contact */
protected void setAD_User_ID(int p_AD_User_ID)
{
	put("AD_User_ID", p_AD_User_ID);
}
/** Set String: User/Contact */
protected void setAD_User_ID(String p_AD_User_ID)
{
	setAD_User_ID(PO.convertToInt(p_AD_User_ID));
}
/** Get: User defined Window */
public int getAD_UserDef_Win_ID()
{
String value = get("AD_UserDef_Win_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: User defined Window */
protected void setAD_UserDef_Win_ID(int p_AD_UserDef_Win_ID)
{
	put("AD_UserDef_Win_ID", p_AD_UserDef_Win_ID);
}
/** Set String: User defined Window */
protected void setAD_UserDef_Win_ID(String p_AD_UserDef_Win_ID)
{
	setAD_UserDef_Win_ID(PO.convertToInt(p_AD_UserDef_Win_ID));
}
/** Get: Customization Name */
public String getCustomizationName()
{
String value = get("CustomizationName");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Customization Name */
protected void setCustomizationName(String p_CustomizationName)
{
	put("CustomizationName", p_CustomizationName);
}
/** Get: User defined Tab */
public int getAD_UserDef_Tab_ID()
{
String value = get("AD_UserDef_Tab_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: User defined Tab */
protected void setAD_UserDef_Tab_ID(int p_AD_UserDef_Tab_ID)
{
	put("AD_UserDef_Tab_ID", p_AD_UserDef_Tab_ID);
}
/** Set String: User defined Tab */
protected void setAD_UserDef_Tab_ID(String p_AD_UserDef_Tab_ID)
{
	setAD_UserDef_Tab_ID(PO.convertToInt(p_AD_UserDef_Tab_ID));
}
/** Get: Name */
public String getName()
{
String value = get("Name");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Name */
protected void setName(String p_Name)
{
	put("Name", p_Name);
}
/** Get: Description */
public String getDescription()
{
String value = get("Description");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Description */
protected void setDescription(String p_Description)
{
	put("Description", p_Description);
}
/** Get: Comment */
public String getHelp()
{
String value = get("Help");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Comment */
protected void setHelp(String p_Help)
{
	put("Help", p_Help);
}
/** Get: Sequence */
public int getSeqNo()
{
String value = get("SeqNo");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Sequence */
protected void setSeqNo(int p_SeqNo)
{
	put("SeqNo", p_SeqNo);
}
/** Set String: Sequence */
protected void setSeqNo(String p_SeqNo)
{
	setSeqNo(PO.convertToInt(p_SeqNo));
}
/** Get: Single Row Layout */
public boolean isSingleRow()
{
String value = get("IsSingleRow");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Single Row Layout */
protected void setIsSingleRow(boolean p_IsSingleRow)
{
	put("IsSingleRow", p_IsSingleRow);
}
/** Set String: Single Row Layout */
protected void setIsSingleRow(String p_IsSingleRow)
{
	setIsSingleRow(PO.convertToBoolean(p_IsSingleRow));
}
/** Get: Has Tree */
public boolean isHasTree()
{
String value = get("HasTree");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Has Tree */
protected void setHasTree(boolean p_HasTree)
{
	put("HasTree", p_HasTree);
}
/** Set String: Has Tree */
protected void setHasTree(String p_HasTree)
{
	setHasTree(PO.convertToBoolean(p_HasTree));
}
/** Get: Accounting Tab */
public boolean isInfoTab()
{
String value = get("IsInfoTab");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Accounting Tab */
protected void setIsInfoTab(boolean p_IsInfoTab)
{
	put("IsInfoTab", p_IsInfoTab);
}
/** Set String: Accounting Tab */
protected void setIsInfoTab(String p_IsInfoTab)
{
	setIsInfoTab(PO.convertToBoolean(p_IsInfoTab));
}
/** Get: Referenced Tab */
public int getReferenced_Tab_ID()
{
String value = get("Referenced_Tab_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Referenced Tab */
protected void setReferenced_Tab_ID(int p_Referenced_Tab_ID)
{
	put("Referenced_Tab_ID", p_Referenced_Tab_ID);
}
/** Set String: Referenced Tab */
protected void setReferenced_Tab_ID(String p_Referenced_Tab_ID)
{
	setReferenced_Tab_ID(PO.convertToInt(p_Referenced_Tab_ID));
}
/** Get: Replication Type */
public boolean isReplicationType()
{
String value = get("ReplicationType");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Replication Type */
protected void setReplicationType(boolean p_ReplicationType)
{
	put("ReplicationType", p_ReplicationType);
}
/** Set String: Replication Type */
protected void setReplicationType(String p_ReplicationType)
{
	setReplicationType(PO.convertToBoolean(p_ReplicationType));
}
/** Get: DB Table Name */
public String getTableName()
{
String value = get("TableName");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: DB Table Name */
protected void setTableName(String p_TableName)
{
	put("TableName", p_TableName);
}
/** Get: Data Access Level */
public String getAccessLevel()
{
String value = get("AccessLevel");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Data Access Level */
protected void setAccessLevel(String p_AccessLevel)
{
	put("AccessLevel", p_AccessLevel);
}
/** Get: Security enabled */
public boolean isSecurityEnabled()
{
String value = get("IsSecurityEnabled");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Security enabled */
protected void setIsSecurityEnabled(boolean p_IsSecurityEnabled)
{
	put("IsSecurityEnabled", p_IsSecurityEnabled);
}
/** Set String: Security enabled */
protected void setIsSecurityEnabled(String p_IsSecurityEnabled)
{
	setIsSecurityEnabled(PO.convertToBoolean(p_IsSecurityEnabled));
}
/** Get: Records deleteable */
public boolean isDeleteable()
{
String value = get("IsDeleteable");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Records deleteable */
protected void setIsDeleteable(boolean p_IsDeleteable)
{
	put("IsDeleteable", p_IsDeleteable);
}
/** Set String: Records deleteable */
protected void setIsDeleteable(String p_IsDeleteable)
{
	setIsDeleteable(PO.convertToBoolean(p_IsDeleteable));
}
/** Get: High Volume */
public boolean isHighVolume()
{
String value = get("IsHighVolume");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: High Volume */
protected void setIsHighVolume(boolean p_IsHighVolume)
{
	put("IsHighVolume", p_IsHighVolume);
}
/** Set String: High Volume */
protected void setIsHighVolume(String p_IsHighVolume)
{
	setIsHighVolume(PO.convertToBoolean(p_IsHighVolume));
}
/** Get: View */
public boolean isView()
{
String value = get("IsView");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: View */
protected void setIsView(boolean p_IsView)
{
	put("IsView", p_IsView);
}
/** Set String: View */
protected void setIsView(String p_IsView)
{
	setIsView(PO.convertToBoolean(p_IsView));
}
/** Get: TranslationTab */
public boolean isTranslationTab()
{
String value = get("IsTranslationTab");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: TranslationTab */
protected void setIsTranslationTab(boolean p_IsTranslationTab)
{
	put("IsTranslationTab", p_IsTranslationTab);
}
/** Set String: TranslationTab */
protected void setIsTranslationTab(String p_IsTranslationTab)
{
	setIsTranslationTab(PO.convertToBoolean(p_IsTranslationTab));
}
/** Get: Read Only */
public boolean isReadOnly()
{
String value = get("IsReadOnly");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Read Only */
protected void setIsReadOnly(boolean p_IsReadOnly)
{
	put("IsReadOnly", p_IsReadOnly);
}
/** Set String: Read Only */
protected void setIsReadOnly(String p_IsReadOnly)
{
	setIsReadOnly(PO.convertToBoolean(p_IsReadOnly));
}
/** Get: Image */
public int getAD_Image_ID()
{
String value = get("AD_Image_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Image */
protected void setAD_Image_ID(int p_AD_Image_ID)
{
	put("AD_Image_ID", p_AD_Image_ID);
}
/** Set String: Image */
protected void setAD_Image_ID(String p_AD_Image_ID)
{
	setAD_Image_ID(PO.convertToInt(p_AD_Image_ID));
}
/** Get: Tab Level */
public int getTabLevel()
{
String value = get("TabLevel");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Tab Level */
protected void setTabLevel(int p_TabLevel)
{
	put("TabLevel", p_TabLevel);
}
/** Set String: Tab Level */
protected void setTabLevel(String p_TabLevel)
{
	setTabLevel(PO.convertToInt(p_TabLevel));
}
/** Get: Sql WHERE */
public String getWhereClause()
{
String value = get("WhereClause");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Sql WHERE */
protected void setWhereClause(String p_WhereClause)
{
	put("WhereClause", p_WhereClause);
}
/** Get: Sql ORDER BY */
public String getOrderByClause()
{
String value = get("OrderByClause");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Sql ORDER BY */
protected void setOrderByClause(String p_OrderByClause)
{
	put("OrderByClause", p_OrderByClause);
}
/** Get: Commit Warning */
public String getCommitWarning()
{
String value = get("CommitWarning");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Commit Warning */
protected void setCommitWarning(String p_CommitWarning)
{
	put("CommitWarning", p_CommitWarning);
}
/** Get: Read Only Logic */
public String getReadOnlyLogic()
{
String value = get("ReadOnlyLogic");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Read Only Logic */
protected void setReadOnlyLogic(String p_ReadOnlyLogic)
{
	put("ReadOnlyLogic", p_ReadOnlyLogic);
}
/** Get: Displayed */
public boolean isDisplayed()
{
String value = get("IsDisplayed");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Displayed */
protected void setIsDisplayed(boolean p_IsDisplayed)
{
	put("IsDisplayed", p_IsDisplayed);
}
/** Set String: Displayed */
protected void setIsDisplayed(String p_IsDisplayed)
{
	setIsDisplayed(PO.convertToBoolean(p_IsDisplayed));
}
/** Get: Display Logic */
public String getDisplayLogic()
{
String value = get("DisplayLogic");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Display Logic */
protected void setDisplayLogic(String p_DisplayLogic)
{
	put("DisplayLogic", p_DisplayLogic);
}
/** Get: Column */
public int getAD_Column_ID()
{
String value = get("AD_Column_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Column */
protected void setAD_Column_ID(int p_AD_Column_ID)
{
	put("AD_Column_ID", p_AD_Column_ID);
}
/** Set String: Column */
protected void setAD_Column_ID(String p_AD_Column_ID)
{
	setAD_Column_ID(PO.convertToInt(p_AD_Column_ID));
}
/** Get: Link Column Name */
public String getLinkColumnName()
{
String value = get("LinkColumnName");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Link Column Name */
protected void setLinkColumnName(String p_LinkColumnName)
{
	put("LinkColumnName", p_LinkColumnName);
}
/** Get: Process */
public int getAD_Process_ID()
{
String value = get("AD_Process_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Process */
protected void setAD_Process_ID(int p_AD_Process_ID)
{
	put("AD_Process_ID", p_AD_Process_ID);
}
/** Set String: Process */
protected void setAD_Process_ID(String p_AD_Process_ID)
{
	setAD_Process_ID(PO.convertToInt(p_AD_Process_ID));
}
/** Get: Order Tab */
public boolean isSortTab()
{
String value = get("IsSortTab");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Order Tab */
protected void setIsSortTab(boolean p_IsSortTab)
{
	put("IsSortTab", p_IsSortTab);
}
/** Set String: Order Tab */
protected void setIsSortTab(String p_IsSortTab)
{
	setIsSortTab(PO.convertToBoolean(p_IsSortTab));
}
/** Get: Advanced Tab */
public boolean isAdvancedTab()
{
String value = get("IsAdvancedTab");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Advanced Tab */
protected void setIsAdvancedTab(boolean p_IsAdvancedTab)
{
	put("IsAdvancedTab", p_IsAdvancedTab);
}
/** Set String: Advanced Tab */
protected void setIsAdvancedTab(String p_IsAdvancedTab)
{
	setIsAdvancedTab(PO.convertToBoolean(p_IsAdvancedTab));
}
/** Get: Insert Record */
public boolean isInsertRecord()
{
String value = get("IsInsertRecord");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Insert Record */
protected void setIsInsertRecord(boolean p_IsInsertRecord)
{
	put("IsInsertRecord", p_IsInsertRecord);
}
/** Set String: Insert Record */
protected void setIsInsertRecord(String p_IsInsertRecord)
{
	setIsInsertRecord(PO.convertToBoolean(p_IsInsertRecord));
}
/** Get: Order Column */
public int getAD_ColumnSortOrder_ID()
{
String value = get("AD_ColumnSortOrder_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Order Column */
protected void setAD_ColumnSortOrder_ID(int p_AD_ColumnSortOrder_ID)
{
	put("AD_ColumnSortOrder_ID", p_AD_ColumnSortOrder_ID);
}
/** Set String: Order Column */
protected void setAD_ColumnSortOrder_ID(String p_AD_ColumnSortOrder_ID)
{
	setAD_ColumnSortOrder_ID(PO.convertToInt(p_AD_ColumnSortOrder_ID));
}
/** Get: Included Column */
public int getAD_ColumnSortYesNo_ID()
{
String value = get("AD_ColumnSortYesNo_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Included Column */
protected void setAD_ColumnSortYesNo_ID(int p_AD_ColumnSortYesNo_ID)
{
	put("AD_ColumnSortYesNo_ID", p_AD_ColumnSortYesNo_ID);
}
/** Set String: Included Column */
protected void setAD_ColumnSortYesNo_ID(String p_AD_ColumnSortYesNo_ID)
{
	setAD_ColumnSortYesNo_ID(PO.convertToInt(p_AD_ColumnSortYesNo_ID));
}
/** Get: Included Tab */
public int getIncluded_Tab_ID()
{
String value = get("Included_Tab_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Included Tab */
protected void setIncluded_Tab_ID(int p_Included_Tab_ID)
{
	put("Included_Tab_ID", p_Included_Tab_ID);
}
/** Set String: Included Tab */
protected void setIncluded_Tab_ID(String p_Included_Tab_ID)
{
	setIncluded_Tab_ID(PO.convertToInt(p_Included_Tab_ID));
}
}
