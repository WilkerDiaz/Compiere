/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2008 Compiere, Inc. All Rights Reserved.                *
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
 * Compiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.controller;

/** Generated Factory Template - DO NOT CHANGE */
import java.sql.*;
import java.util.logging.*;

import org.compiere.util.*;
/** Generated FT for AD_Field_v
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: UIFieldVOFT.java 9169 2010-08-05 09:24:48Z sdandapat $ */
public class UIFieldVOFT extends VOFactory<UIFieldVO>
{
/** Standard Constructor
*/
public UIFieldVOFT ()
{
}
/** Info
@return info
*/
@Override
public String toString()
{
StringBuffer sb = new StringBuffer ("UIFieldVOFT[").append("]");
return sb.toString();
}
/** Load from ResultSet **/
@Override
protected UIFieldVO load (ResultSet rs)
{
UIFieldVO vo = new UIFieldVO();
String colName = null;
try
{
ResultSetMetaData rsmd = rs.getMetaData();
for (int index = 1;
 index <= rsmd.getColumnCount();
 index++)
{
colName = rsmd.getColumnName(index);

	if (colName.equalsIgnoreCase("AD_Window_ID"))
		vo.setAD_Window_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("AD_Tab_ID"))
		vo.setAD_Tab_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("AD_Field_ID"))
		vo.setAD_Field_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("AD_Table_ID"))
		vo.setAD_Table_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("AD_Column_ID"))
		vo.setAD_Column_ID(rs.getInt(index));
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
	else if (colName.equalsIgnoreCase("AD_UserDef_Field_ID"))
		vo.setAD_UserDef_Field_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("Name"))
		vo.setName(rs.getString(index));
	else if (colName.equalsIgnoreCase("Description"))
		vo.setDescription(rs.getString(index));
	else if (colName.equalsIgnoreCase("Help"))
		vo.setHelp(rs.getString(index));
	else if (colName.equalsIgnoreCase("IsDisplayed"))
		vo.setIsDisplayed("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("DisplayLogic"))
		vo.setDisplayLogic(rs.getString(index));
	else if (colName.equalsIgnoreCase("DisplayLength"))
		vo.setDisplayLength(rs.getInt(index));
	else if (colName.equalsIgnoreCase("SeqNo"))
		vo.setSeqNo(rs.getInt(index));
	else if (colName.equalsIgnoreCase("SortNo"))
		vo.setSortNo(rs.getBigDecimal(index));
	else if (colName.equalsIgnoreCase("IsSameLine"))
		vo.setIsSameLine("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("IsHeading"))
		vo.setIsHeading("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("IsFieldOnly"))
		vo.setIsFieldOnly("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("IsReadOnly"))
		vo.setIsReadOnly("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("IsEncryptedField"))
		vo.setIsEncryptedField("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("ObscureType"))
		vo.setObscureType(rs.getString(index));
	else if (colName.equalsIgnoreCase("IsDefaultFocus"))
		vo.setIsDefaultFocus("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("MRSeqNo"))
		vo.setMRSeqNo(rs.getInt(index));
	else if (colName.equalsIgnoreCase("ColumnName"))
		vo.setColumnName(rs.getString(index));
	else if (colName.equalsIgnoreCase("ColumnSQL"))
		vo.setColumnSQL(rs.getString(index));
	else if (colName.equalsIgnoreCase("FieldLength"))
		vo.setFieldLength(rs.getInt(index));
	else if (colName.equalsIgnoreCase("VFormat"))
		vo.setVFormat(rs.getString(index));
	else if (colName.equalsIgnoreCase("DefaultValue"))
		vo.setDefaultValue(rs.getString(index));
	else if (colName.equalsIgnoreCase("IsKey"))
		vo.setIsKey("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("IsParent"))
		vo.setIsParent("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("IsMandatory"))
		vo.setIsMandatory("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("MandatoryLogic"))
		vo.setMandatoryLogic(rs.getString(index));
	else if (colName.equalsIgnoreCase("IsMandatoryUI"))
		vo.setIsMandatoryUI("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("IsIdentifier"))
		vo.setIsIdentifier("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("IsTranslated"))
		vo.setIsTranslated("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("AD_Reference_Value_ID"))
		vo.setAD_Reference_Value_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("Callout"))
		vo.setCallout(rs.getString(index));
	else if (colName.equalsIgnoreCase("IsCallout"))
		vo.setIsCallout("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("AD_Reference_ID"))
		vo.setAD_Reference_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("AD_Val_Rule_ID"))
		vo.setAD_Val_Rule_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("AD_Process_ID"))
		vo.setAD_Process_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("IsAlwaysUpdateable"))
		vo.setIsAlwaysUpdateable("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("ReadOnlyLogic"))
		vo.setReadOnlyLogic(rs.getString(index));
	else if (colName.equalsIgnoreCase("IsUpdateable"))
		vo.setIsUpdateable("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("IsEncryptedColumn"))
		vo.setIsEncryptedColumn("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("IsSelectionColumn"))
		vo.setIsSelectionColumn("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("SelectionSeqNo"))
		vo.setSelectionSeqNo(rs.getInt(index));
	else if (colName.equalsIgnoreCase("IsSummaryColumn"))
		vo.setIsSummaryColumn("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("SummarySeqNo"))
		vo.setSummarySeqNo(rs.getInt(index));
	else if (colName.equalsIgnoreCase("TableName"))
		vo.setTableName(rs.getString(index));
	else if (colName.equalsIgnoreCase("ValueMin"))
		vo.setValueMin(rs.getString(index));
	else if (colName.equalsIgnoreCase("ValueMax"))
		vo.setValueMax(rs.getString(index));
	else if (colName.equalsIgnoreCase("FieldGroup"))
		vo.setFieldGroup(rs.getString(index));
	else if (colName.equalsIgnoreCase("ValidationCode"))
		vo.setValidationCode(rs.getString(index));
	else if (colName.equalsIgnoreCase("MaxWidth"))
		vo.setMaxWidth(rs.getInt(index));
	else if (colName.equalsIgnoreCase("MaxHeight"))
		vo.setMaxHeight(rs.getInt(index));
	else if (colName.equalsIgnoreCase("IsCopy"))
		vo.setIsCopy("Y".equals(rs.getString(index)));
}
boolean check = false;
if (vo.getAD_Process_ID() != 0){
	
	Boolean b = UIFieldVOFactory.lUserRole.getProcessAccess(vo.getAD_Process_ID());
	if (b == null || !b.booleanValue()){
		check = true;
	}	
	vo.setIsReadOnly( check);
}


}
catch (Exception e)
{
log.log(Level.SEVERE, colName, e);
}
return vo;
}
}
