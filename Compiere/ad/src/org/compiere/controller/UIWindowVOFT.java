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
/** Generated FT for AD_Window_v
 *  @author Jorg Janke (generated) 
 *  @version Release 2.6.4 - $Id: UIWindowVOFT.java 8244 2009-12-04 23:25:29Z freyes $ */
public class UIWindowVOFT extends VOFactory<UIWindowVO>
{
/** Standard Constructor
*/
public UIWindowVOFT ()
{
}
/** Info
@return info
*/
@Override
public String toString()
{
StringBuffer sb = new StringBuffer ("UIWindowVOFT[").append("]");
return sb.toString();
}
/** Load from ResultSet **/
@Override
protected UIWindowVO load (ResultSet rs)
{
UIWindowVO vo = new UIWindowVO();
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
	else if (colName.equalsIgnoreCase("AD_Client_ID"))
		vo.setAD_Client_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("UserDef_Role_ID"))
		vo.setUserDef_Role_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("AD_User_ID"))
		vo.setAD_User_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("AD_UserDef_Win_ID"))
		vo.setAD_UserDef_Win_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("CustomizationName"))
		vo.setCustomizationName(rs.getString(index));
	else if (colName.equalsIgnoreCase("Name"))
		vo.setName(rs.getString(index));
	else if (colName.equalsIgnoreCase("Description"))
		vo.setDescription(rs.getString(index));
	else if (colName.equalsIgnoreCase("Help"))
		vo.setHelp(rs.getString(index));
	else if (colName.equalsIgnoreCase("WindowType"))
		vo.setWindowType(rs.getString(index));
	else if (colName.equalsIgnoreCase("AD_Color_ID"))
		vo.setAD_Color_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("AD_Image_ID"))
		vo.setAD_Image_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("WinHeight"))
		vo.setWinHeight(rs.getInt(index));
	else if (colName.equalsIgnoreCase("WinWidth"))
		vo.setWinWidth(rs.getInt(index));
	else if (colName.equalsIgnoreCase("AD_CtxArea_ID"))
		vo.setAD_CtxArea_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("IsSOTrx"))
		vo.setIsSOTrx("Y".equals(rs.getString(index)));
	else if (colName.equalsIgnoreCase("AD_Role_ID"))
		vo.setAD_Role_ID(rs.getInt(index));
	else if (colName.equalsIgnoreCase("IsReadWrite"))
		vo.setIsReadWrite("Y".equals(rs.getString(index)));
}
}
catch (Exception e)
{
log.log(Level.SEVERE, colName, e);
}
return vo;
}
}
