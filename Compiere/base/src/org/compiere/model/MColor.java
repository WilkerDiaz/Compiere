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
import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.plaf.*;
import org.compiere.util.*;

/**
 *  Color Persistent Object Model
 *  (DisplayType=27)
 *
 *  @author Jorg Janke
 *  @version $Id: MColor.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MColor extends X_AD_Color
{
    /** Logger for class MColor */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MColor.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  Color Model
	 *  @param ctx context
	 *  @param AD_Color_ID color
	 *	@param trx transaction
	 */
	public MColor(Ctx ctx, int AD_Color_ID, Trx trx)
	{
		super (ctx, AD_Color_ID, trx);
		if (AD_Color_ID == 0)
			setName("-/-");
	}   //  MColor

	/**
	 *  String Representation
	 *  @return string
	 */
	@Override
	public String toString()
	{
		return "MColor[ID=" + get_ID() + " - " + getName() + "]";
	}   //  toString

	/**
	 *  Load Special data (images, ..).
	 *  To be extended by sub-classes
	 *  @param rs result set
	 *  @param index zero based index
	 *  @return value
	 *  @throws SQLException
	 */
	@Override
	protected Object loadSpecial (ResultSet rs, int index) throws SQLException
	{
		log.config(p_info.getColumnName(index));
		if (index == get_ColumnIndex("ColorType"))
			return rs.getString(index+1);
		return null;
	}   //  loadSpecial


	/**
	 *  Save Special Data.
	 *      AD_Image_ID (Background)
	 *  @param value value
	 *  @param index index
	 *  @return SQL code for INSERT VALUES clause
	 */
	@Override
	protected String saveNewSpecial (Object value, int index)
	{
		String colName = p_info.getColumnName(index);
		String colValue = value == null ? "null" : value.getClass().toString();
		log.fine(colName + "=" + colValue);
		if (value == null)
			return "NULL";
		return value.toString();
	}   //  saveNewSpecial

	
	/**************************************************************************
	 *  Get CompiereColor.
	 *  see org.compiere.grid.ed.VColor#getCompiereColor
	 *  @return CompiereColor
	 */
	public CompiereColor getCompiereColor()
	{
		if (get_ID() == 0)
			return null;

		//  Color Type
		String ColorType = getColorType();
		if (ColorType == null)
		{
			log.log(Level.SEVERE, "MColor.getCompiereColor - No ColorType");
			return null;
		}
		CompiereColor cc = null;
		//
		if (ColorType.equals(CompiereColor.TYPE_FLAT))
		{
			cc = new CompiereColor(getColor(true), true);
		}
		else if (ColorType.equals(CompiereColor.TYPE_GRADIENT))
		{
			int RepeatDistance = getRepeatDistance();
			String StartPoint = getStartPoint();
			int startPoint = StartPoint == null ? 0 : Integer.parseInt(StartPoint);
			cc = new CompiereColor(getColor(true), getColor(false), startPoint, RepeatDistance);
		}
		else if (ColorType.equals(CompiereColor.TYPE_LINES))
		{
			int LineWidth = getLineWidth();
			int LineDistance = getLineDistance();
			cc = new CompiereColor(getColor(false), getColor(true), LineWidth, LineDistance);
		}
		else if (ColorType.equals(CompiereColor.TYPE_TEXTURE))
		{
			int AD_Image_ID = getAD_Image_ID();
			String url = getURL(AD_Image_ID);
			if (url == null)
				return null;
			BigDecimal ImageAlpha = getImageAlpha();
			float compositeAlpha = ImageAlpha == null ? 0.7f : ImageAlpha.floatValue();
			cc = new CompiereColor(url, getColor(true), compositeAlpha);
		}
		return cc;
	}   //  getCompiereColor

	/**
	 *  Get Color
	 *  @param primary true if primary false if secondary
	 *  @return Color
	 */
	private Color getColor (boolean primary)
	{
		int red = primary ? getRed() : getRed_1();
		int green = primary ? getGreen() : getGreen_1();
		int blue = primary ? getBlue() : getBlue_1();
		//
		return new Color (red, green, blue);
	}   //  getColor

	/**
	 *  Get URL from Image
	 *  @param AD_Image_ID image
	 *  @return URL as String or null
	 */
	private String getURL (int AD_Image_ID)
	{
		if (AD_Image_ID == 0)
			return null;
		//
		String retValue = null;
		String sql = "SELECT ImageURL FROM AD_Image WHERE AD_Image_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt (1, AD_Image_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				retValue = rs.getString(1);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return retValue;
	}   //  getURL

}   //  MColor
