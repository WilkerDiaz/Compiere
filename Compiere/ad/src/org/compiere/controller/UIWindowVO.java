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

/** Generated VO for AD_Window_v
 *  @author Jorg Janke (generated) 
 *  @version Release 2.6.4 - $Id: UIWindowVO.java 8244 2009-12-04 23:25:29Z freyes $ */
public class UIWindowVO extends VO implements java.io.Serializable
{
/** Standard Constructor
*/
public UIWindowVO()
{
super();
}
/** Clone Constructor
*/
public UIWindowVO(VO vo)
{
super(vo);
}
/** Serial Version No */
static final long serialVersionUID = 38026284144329560L;
/** Created Timestamp */
public static final long createdMS = 1189540927439L;
/** Info
@return info
*/
@Override
public String toString()
{
StringBuffer sb = new StringBuffer ("UIWindowVO[#").append(size()).append("]");
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
/** Get: Tenant */
public int getAD_Client_ID()
{
String value = get("AD_Client_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Tenant */
protected void setAD_Client_ID(int p_AD_Client_ID)
{
	put("AD_Client_ID", p_AD_Client_ID);
}
/** Set String: Tenant */
protected void setAD_Client_ID(String p_AD_Client_ID)
{
	setAD_Client_ID(PO.convertToInt(p_AD_Client_ID));
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
/** Get: WindowType */
public String getWindowType()
{
String value = get("WindowType");
String retValue = value;
if (value == null) retValue = "";
	return retValue;
}
/** Set: WindowType */
protected void setWindowType(String p_WindowType)
{
	put("WindowType", p_WindowType);
}
/** Get: System Color */
public int getAD_Color_ID()
{
String value = get("AD_Color_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: System Color */
protected void setAD_Color_ID(int p_AD_Color_ID)
{
	put("AD_Color_ID", p_AD_Color_ID);
}
/** Set String: System Color */
protected void setAD_Color_ID(String p_AD_Color_ID)
{
	setAD_Color_ID(PO.convertToInt(p_AD_Color_ID));
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
/** Get: Window Height */
public int getWinHeight()
{
String value = get("WinHeight");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Window Height */
protected void setWinHeight(int p_WinHeight)
{
	put("WinHeight", p_WinHeight);
}
/** Set String: Window Height */
protected void setWinHeight(String p_WinHeight)
{
	setWinHeight(PO.convertToInt(p_WinHeight));
}
/** Get: Window Width */
public int getWinWidth()
{
String value = get("WinWidth");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Window Width */
protected void setWinWidth(int p_WinWidth)
{
	put("WinWidth", p_WinWidth);
}
/** Set String: Window Width */
protected void setWinWidth(String p_WinWidth)
{
	setWinWidth(PO.convertToInt(p_WinWidth));
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
/** Get: Sales Transaction */
public boolean isSOTrx()
{
String value = get("IsSOTrx");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Sales Transaction */
protected void setIsSOTrx(boolean p_IsSOTrx)
{
	put("IsSOTrx", p_IsSOTrx);
}
/** Set String: Sales Transaction */
protected void setIsSOTrx(String p_IsSOTrx)
{
	setIsSOTrx(PO.convertToBoolean(p_IsSOTrx));
}
/** Get: Role */
public int getAD_Role_ID()
{
String value = get("AD_Role_ID");
int retValue = 0;
if (value != null) retValue = Integer.parseInt(value);
	return retValue;
}
/** Set: Role */
protected void setAD_Role_ID(int p_AD_Role_ID)
{
	put("AD_Role_ID", p_AD_Role_ID);
}
/** Set String: Role */
protected void setAD_Role_ID(String p_AD_Role_ID)
{
	setAD_Role_ID(PO.convertToInt(p_AD_Role_ID));
}
/** Get: Read Write */
public boolean isReadWrite()
{
String value = get("IsReadWrite");
boolean retValue = "true".equals(value) || "Y".equals(value);
	return retValue;
}
/** Set: Read Write */
protected void setIsReadWrite(boolean p_IsReadWrite)
{
	put("IsReadWrite", p_IsReadWrite);
}
/** Set String: Read Write */
protected void setIsReadWrite(String p_IsReadWrite)
{
	setIsReadWrite(PO.convertToBoolean(p_IsReadWrite));
}
}
