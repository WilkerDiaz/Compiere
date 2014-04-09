/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2008 ComPiere, Inc. All Rights Reserved.                *
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

/** Generated VO for AD_Field_v
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: UIFieldVO.java 9169 2010-08-05 09:24:48Z sdandapat $ */
public class UIFieldVO extends VO implements java.io.Serializable
{
/** Standard Constructor
*/
public UIFieldVO()
{
super();
}
/** Clone Constructor
*/
public UIFieldVO(VO vo)
{
super(vo);
}
/** Serial Version No */
static final long serialVersionUID = 38026282750647530L;
/** Created Timestamp */
public static final long createdMS = 1189540928798L;
/** Info
@return info
*/
@Override
public String toString()
{
StringBuffer sb = new StringBuffer ("UIFieldVO[#").append(size()).append("]");
return sb.toString();
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
/** Get: Field */
public int getAD_Field_ID()
{
String value = get("AD_Field_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Field */
protected void setAD_Field_ID(int p_AD_Field_ID)
{
	put("AD_Field_ID", p_AD_Field_ID);
}
/** Set String: Field */
protected void setAD_Field_ID(String p_AD_Field_ID)
{
	setAD_Field_ID(PO.convertToInt(p_AD_Field_ID));
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
/** Get: User defined Field */
public int getAD_UserDef_Field_ID()
{
String value = get("AD_UserDef_Field_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: User defined Field */
protected void setAD_UserDef_Field_ID(int p_AD_UserDef_Field_ID)
{
	put("AD_UserDef_Field_ID", p_AD_UserDef_Field_ID);
}
/** Set String: User defined Field */
protected void setAD_UserDef_Field_ID(String p_AD_UserDef_Field_ID)
{
	setAD_UserDef_Field_ID(PO.convertToInt(p_AD_UserDef_Field_ID));
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
/** Get: Display Length */
public int getDisplayLength()
{
String value = get("DisplayLength");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Display Length */
protected void setDisplayLength(int p_DisplayLength)
{
	put("DisplayLength", p_DisplayLength);
}
/** Set String: Display Length */
protected void setDisplayLength(String p_DisplayLength)
{
	setDisplayLength(PO.convertToInt(p_DisplayLength));
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
/** Get: Record Sort No */
public java.math.BigDecimal getSortNo()
{
String value = get("SortNo");
java.math.BigDecimal retValue = null;
if (value == null) retValue = new java.math.BigDecimal(0);
else
	retValue = new java.math.BigDecimal(value);
	return retValue;
}
/** Set: Record Sort No */
protected void setSortNo(java.math.BigDecimal p_SortNo)
{
	put("SortNo", p_SortNo);
}
/** Set String: Record Sort No */
protected void setSortNo(String p_SortNo)
{
	setSortNo(PO.convertToBigDecimal(p_SortNo));
}
/** Get: Same Line */
public boolean isSameLine()
{
String value = get("IsSameLine");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Same Line */
protected void setIsSameLine(boolean p_IsSameLine)
{
	put("IsSameLine", p_IsSameLine);
}
/** Set String: Same Line */
protected void setIsSameLine(String p_IsSameLine)
{
	setIsSameLine(PO.convertToBoolean(p_IsSameLine));
}
/** Get: Heading only */
public boolean isHeading()
{
String value = get("IsHeading");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Heading only */
protected void setIsHeading(boolean p_IsHeading)
{
	put("IsHeading", p_IsHeading);
}
/** Set String: Heading only */
protected void setIsHeading(String p_IsHeading)
{
	setIsHeading(PO.convertToBoolean(p_IsHeading));
}
/** Get: Field Only */
public boolean isFieldOnly()
{
String value = get("IsFieldOnly");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Field Only */
protected void setIsFieldOnly(boolean p_IsFieldOnly)
{
	put("IsFieldOnly", p_IsFieldOnly);
}
/** Set String: Field Only */
protected void setIsFieldOnly(String p_IsFieldOnly)
{
	setIsFieldOnly(PO.convertToBoolean(p_IsFieldOnly));
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
/** Get: Encrypted Field */
public boolean isEncryptedField()
{
String value = get("IsEncryptedField");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Encrypted Field */
protected void setIsEncryptedField(boolean p_IsEncryptedField)
{
	put("IsEncryptedField", p_IsEncryptedField);
}
/** Set String: Encrypted Field */
protected void setIsEncryptedField(String p_IsEncryptedField)
{
	setIsEncryptedField(PO.convertToBoolean(p_IsEncryptedField));
}
/** Get: Obscure */
public String getObscureType()
{
String value = get("ObscureType");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Obscure */
protected void setObscureType(String p_ObscureType)
{
	put("ObscureType", p_ObscureType);
}
/** Get: Default Focus */
public boolean isDefaultFocus()
{
String value = get("IsDefaultFocus");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Default Focus */
protected void setIsDefaultFocus(boolean p_IsDefaultFocus)
{
	put("IsDefaultFocus", p_IsDefaultFocus);
}
/** Set String: Default Focus */
protected void setIsDefaultFocus(String p_IsDefaultFocus)
{
	setIsDefaultFocus(PO.convertToBoolean(p_IsDefaultFocus));
}
/** Get: Multi-Row Sequence */
public int getMRSeqNo()
{
String value = get("MRSeqNo");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Multi-Row Sequence */
protected void setMRSeqNo(int p_MRSeqNo)
{
	put("MRSeqNo", p_MRSeqNo);
}
/** Set String: Multi-Row Sequence */
protected void setMRSeqNo(String p_MRSeqNo)
{
	setMRSeqNo(PO.convertToInt(p_MRSeqNo));
}
/** Get: DB Column Name */
public String getColumnName()
{
String value = get("ColumnName");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: DB Column Name */
protected void setColumnName(String p_ColumnName)
{
	put("ColumnName", p_ColumnName);
}
/** Get: Column SQL */
public String getColumnSQL()
{
String value = get("ColumnSQL");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Column SQL */
protected void setColumnSQL(String p_ColumnSQL)
{
	put("ColumnSQL", p_ColumnSQL);
}
/** Get: Length */
public int getFieldLength()
{
String value = get("FieldLength");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Length */
protected void setFieldLength(int p_FieldLength)
{
	put("FieldLength", p_FieldLength);
}
/** Set String: Length */
protected void setFieldLength(String p_FieldLength)
{
	setFieldLength(PO.convertToInt(p_FieldLength));
}
/** Get: Value Format */
public String getVFormat()
{
String value = get("VFormat");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Value Format */
protected void setVFormat(String p_VFormat)
{
	put("VFormat", p_VFormat);
}
/** Get: Default Logic */
public String getDefaultValue()
{
String value = get("DefaultValue");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Default Logic */
protected void setDefaultValue(String p_DefaultValue)
{
	put("DefaultValue", p_DefaultValue);
}
/** Get: Key column */
public boolean isKey()
{
String value = get("IsKey");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Key column */
protected void setIsKey(boolean p_IsKey)
{
	put("IsKey", p_IsKey);
}
/** Set String: Key column */
protected void setIsKey(String p_IsKey)
{
	setIsKey(PO.convertToBoolean(p_IsKey));
}
/** Get: Parent link column */
public boolean isParent()
{
String value = get("IsParent");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Parent link column */
protected void setIsParent(boolean p_IsParent)
{
	put("IsParent", p_IsParent);
}
/** Set String: Parent link column */
protected void setIsParent(String p_IsParent)
{
	setIsParent(PO.convertToBoolean(p_IsParent));
}
/** Get: Mandatory */
public boolean isMandatory()
{
String value = get("IsMandatory");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Mandatory */
protected void setIsMandatory(boolean p_IsMandatory)
{
	put("IsMandatory", p_IsMandatory);
}
/** Set String: Mandatory */
protected void setIsMandatory(String p_IsMandatory)
{
	setIsMandatory(PO.convertToBoolean(p_IsMandatory));
}
/** Get: Mandatory Logic */
public String getMandatoryLogic()
{
String value = get("MandatoryLogic");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Mandatory Logic */
protected void setMandatoryLogic(String p_MandatoryLogic)
{
	put("MandatoryLogic", p_MandatoryLogic);
}
/** Get: Mandatory UI */
public boolean isMandatoryUI()
{
String value = get("IsMandatoryUI");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Mandatory UI */
protected void setIsMandatoryUI(boolean p_IsMandatoryUI)
{
	put("IsMandatoryUI", p_IsMandatoryUI);
}
/** Set String: Mandatory UI */
protected void setIsMandatoryUI(String p_IsMandatoryUI)
{
	setIsMandatoryUI(PO.convertToBoolean(p_IsMandatoryUI));
}
/** Get: Identifier */
public boolean isIdentifier()
{
String value = get("IsIdentifier");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Identifier */
protected void setIsIdentifier(boolean p_IsIdentifier)
{
	put("IsIdentifier", p_IsIdentifier);
}
/** Set String: Identifier */
protected void setIsIdentifier(String p_IsIdentifier)
{
	setIsIdentifier(PO.convertToBoolean(p_IsIdentifier));
}
/** Get: Translated */
public boolean isTranslated()
{
String value = get("IsTranslated");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Translated */
protected void setIsTranslated(boolean p_IsTranslated)
{
	put("IsTranslated", p_IsTranslated);
}
/** Set String: Translated */
protected void setIsTranslated(String p_IsTranslated)
{
	setIsTranslated(PO.convertToBoolean(p_IsTranslated));
}
/** Get: Reference Key */
public int getAD_Reference_Value_ID()
{
String value = get("AD_Reference_Value_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Reference Key */
protected void setAD_Reference_Value_ID(int p_AD_Reference_Value_ID)
{
	put("AD_Reference_Value_ID", p_AD_Reference_Value_ID);
}
/** Set String: Reference Key */
protected void setAD_Reference_Value_ID(String p_AD_Reference_Value_ID)
{
	setAD_Reference_Value_ID(PO.convertToInt(p_AD_Reference_Value_ID));
}
/** Get: Callout Code */
public String getCallout()
{
String value = get("Callout");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Callout Code */
protected void setCallout(String p_Callout)
{
	put("Callout", p_Callout);
}
/** Get: Callout */
public boolean isCallout()
{
String value = get("IsCallout");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Callout */
protected void setIsCallout(boolean p_IsCallout)
{
	put("IsCallout", p_IsCallout);
}
/** Set String: Callout */
protected void setIsCallout(String p_IsCallout)
{
	setIsCallout(PO.convertToBoolean(p_IsCallout));
}
/** Get: Reference */
public int getAD_Reference_ID()
{
String value = get("AD_Reference_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Reference */
protected void setAD_Reference_ID(int p_AD_Reference_ID)
{
	put("AD_Reference_ID", p_AD_Reference_ID);
}
/** Set String: Reference */
protected void setAD_Reference_ID(String p_AD_Reference_ID)
{
	setAD_Reference_ID(PO.convertToInt(p_AD_Reference_ID));
}
/** Get: Dynamic Validation */
public int getAD_Val_Rule_ID()
{
String value = get("AD_Val_Rule_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Dynamic Validation */
protected void setAD_Val_Rule_ID(int p_AD_Val_Rule_ID)
{
	put("AD_Val_Rule_ID", p_AD_Val_Rule_ID);
}
/** Set String: Dynamic Validation */
protected void setAD_Val_Rule_ID(String p_AD_Val_Rule_ID)
{
	setAD_Val_Rule_ID(PO.convertToInt(p_AD_Val_Rule_ID));
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
/** Get: Always Updatable */
public boolean isAlwaysUpdateable()
{
String value = get("IsAlwaysUpdateable");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Always Updatable */
protected void setIsAlwaysUpdateable(boolean p_IsAlwaysUpdateable)
{
	put("IsAlwaysUpdateable", p_IsAlwaysUpdateable);
}
/** Set String: Always Updatable */
protected void setIsAlwaysUpdateable(String p_IsAlwaysUpdateable)
{
	setIsAlwaysUpdateable(PO.convertToBoolean(p_IsAlwaysUpdateable));
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
/** Get: Updateable */
public boolean isUpdateable()
{
String value = get("IsUpdateable");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Updateable */
protected void setIsUpdateable(boolean p_IsUpdateable)
{
	put("IsUpdateable", p_IsUpdateable);
}
/** Set String: Updateable */
protected void setIsUpdateable(String p_IsUpdateable)
{
	setIsUpdateable(PO.convertToBoolean(p_IsUpdateable));
}
/** Get: Encrypted Column */
public boolean isEncryptedColumn()
{
String value = get("IsEncryptedColumn");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Encrypted Column */
protected void setIsEncryptedColumn(boolean p_IsEncryptedColumn)
{
	put("IsEncryptedColumn", p_IsEncryptedColumn);
}
/** Set String: Encrypted Column */
protected void setIsEncryptedColumn(String p_IsEncryptedColumn)
{
	setIsEncryptedColumn(PO.convertToBoolean(p_IsEncryptedColumn));
}
/** Get: Selection Column */
public boolean isSelectionColumn()
{
String value = get("IsSelectionColumn");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Selection Column */
protected void setIsSelectionColumn(boolean p_IsSelectionColumn)
{
	put("IsSelectionColumn", p_IsSelectionColumn);
}
/** Set String: Selection Column */
protected void setIsSelectionColumn(String p_IsSelectionColumn)
{
	setIsSelectionColumn(PO.convertToBoolean(p_IsSelectionColumn));
}
/** Get: Selection Sequence */
public int getSelectionSeqNo()
{
String value = get("SelectionSeqNo");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Selection Sequence */
protected void setSelectionSeqNo(int p_SelectionSeqNo)
{
	put("SelectionSeqNo", p_SelectionSeqNo);
}
/** Set String: Selection Sequence */
protected void setSelectionSeqNo(String p_SelectionSeqNo)
{
	setSelectionSeqNo(PO.convertToInt(p_SelectionSeqNo));
}
/** Get: Summary Column */
public boolean isSummaryColumn()
{
String value = get("IsSummaryColumn");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Summary Column */
protected void setIsSummaryColumn(boolean p_IsSummaryColumn)
{
	put("IsSummaryColumn", p_IsSummaryColumn);
}
/** Set String: Summary Column */
protected void setIsSummaryColumn(String p_IsSummaryColumn)
{
	setIsSummaryColumn(PO.convertToBoolean(p_IsSummaryColumn));
}
/** Get: Summary Sequence */
public int getSummarySeqNo()
{
String value = get("SummarySeqNo");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Summary Sequence */
protected void setSummarySeqNo(int p_SummarySeqNo)
{
	put("SummarySeqNo", p_SummarySeqNo);
}
/** Set String: Summary Sequence */
protected void setSummarySeqNo(String p_SummarySeqNo)
{
	setSummarySeqNo(PO.convertToInt(p_SummarySeqNo));
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
/** Get: Min. Value */
public String getValueMin()
{
String value = get("ValueMin");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Min. Value */
protected void setValueMin(String p_ValueMin)
{
	put("ValueMin", p_ValueMin);
}
/** Get: Max. Value */
public String getValueMax()
{
String value = get("ValueMax");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Max. Value */
protected void setValueMax(String p_ValueMax)
{
	put("ValueMax", p_ValueMax);
}
/** Get: Field Group */
public String getFieldGroup()
{
String value = get("FieldGroup");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Field Group */
protected void setFieldGroup(String p_FieldGroup)
{
	put("FieldGroup", p_FieldGroup);
}
/** Get: Validation Code */
public String getValidationCode()
{
String value = get("ValidationCode");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: Validation Code */
protected void setValidationCode(String p_ValidationCode)
{
	put("ValidationCode", p_ValidationCode);
}
/** Get: Max Width */
public int getMaxWidth()
{
String value = get("MaxWidth");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Max Width */
protected void setMaxWidth(int p_MaxWidth)
{
	put("MaxWidth", p_MaxWidth);
}
/** Set String: Max Width */
protected void setMaxWidth(String p_MaxWidth)
{
	setMaxWidth(PO.convertToInt(p_MaxWidth));
}
/** Get: Max Height */
public int getMaxHeight()
{
String value = get("MaxHeight");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Max Height */
protected void setMaxHeight(int p_MaxHeight)
{
	put("MaxHeight", p_MaxHeight);
}
/** Set String: Max Height */
protected void setMaxHeight(String p_MaxHeight)
{
	setMaxHeight(PO.convertToInt(p_MaxHeight));
}
/** Get: Copy */
public boolean isCopy()
{
String value = get("IsCopy");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Copy */
protected void setIsCopy(boolean p_IsCopy)
{
	put("IsCopy", p_IsCopy);
}
/** Set String: Copy */
protected void setIsCopy(String p_IsCopy)
{
	setIsCopy(PO.convertToBoolean(p_IsCopy));
}
}
