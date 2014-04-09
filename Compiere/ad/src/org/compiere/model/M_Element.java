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

import java.sql.*;
import java.util.logging.*;

import org.compiere.util.*;


/**
 *	System Element Model
 *	
 *  @author Jorg Janke
 *  @version $Id: M_Element.java 8776 2010-05-19 17:50:56Z nnayak $
 */
public class M_Element extends X_AD_Element
{
    /** Logger for class M_Element */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(M_Element.class);
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get case sensitive Column Name
	 *	@param columnName case insensitive column name
	 *	@return case sensitive column name
	 */
	public static String getColumnName (String columnName)
	{
		if (columnName == null || columnName.length() == 0)
			return columnName;
		String retValue = columnName;
		String sql = "SELECT ColumnName FROM AD_Element WHERE UPPER(ColumnName)=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString (1, columnName.toUpperCase());
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retValue = rs.getString(1);
				if (rs.next())
					s_log.warning("Not unique: " + columnName 
						+ " -> " + retValue + " - " + rs.getString(1));
			}
			else
				s_log.warning("No found: " + columnName);
		}
		catch (Exception e)	{
			s_log.log (Level.SEVERE, columnName, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	getColumnName

	/**
	 * 	Get Element
	 * 	@param ctx context
	 *	@param columnName case insensitive column name
	 *	@param trx optional Transaction
	 *	@return system element or null
	 */
	public static M_Element get (Ctx ctx, String columnName, Trx trx)
	{
		if (Util.isEmpty(columnName))
			return null;
		M_Element retValue = null;
		String sql = "SELECT * FROM AD_Element WHERE UPPER(ColumnName)=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setString (1, columnName.toUpperCase());
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retValue = new M_Element (ctx, rs, null);
				if (rs.next())
					s_log.warning("Not unique: " + columnName 
						+ " -> " + retValue + " - " + rs.getString("ColumnName"));
			}
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	get

	/**
	 * 	Get Element
	 * 	@param ctx context
	 *	@param AD_Column_ID id
	 *	@return system element or null
	 */
	public static M_Element getOfColumn (Ctx ctx, int AD_Column_ID)
	{
		if (AD_Column_ID ==0)
			return null;
		M_Element retValue = null;
		String sql = "SELECT * FROM AD_Element e "
			+ "WHERE EXISTS (SELECT * FROM AD_Column c "
				+ "WHERE c.AD_Element_ID=e.AD_Element_ID AND c.AD_Column_ID=?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_Column_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new M_Element (ctx, rs, null);
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	get

	/**
	 * 	Get Element
	 * 	@param ctx context
	 *	@param AD_Column_ID id
	 *	@return system element or null
	 */
	public static M_Element getOfColumnName (Ctx ctx, String columnName)
	{
		if (columnName==null || columnName.length() == 0)
			return null;
		M_Element retValue = null;
		String sql = "SELECT * FROM AD_Element e "
			+ "WHERE UPPER(e.columnName) = UPPER(?) ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, columnName);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new M_Element (ctx, rs, null);
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return retValue;
	}	//	get

	/**	Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (M_Element.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Element_ID element
	 *	@param trx transaction
	 */
	public M_Element (Ctx ctx, int AD_Element_ID, Trx trx)
	{
		super (ctx, AD_Element_ID, trx);
		if (AD_Element_ID == 0)
		{
		//	setColumnName (null);
		//	setEntityType (null);	// U
		//	setName (null);
		//	setPrintName (null);
		}	
	}	//	M_Element

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public M_Element (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	M_Element

	/**
	 * 	Minimum Constructor
	 *	@param ctx context
	 *	@param columnName column
	 *	@param EntityType entity type
	 *	@param trx p_trx
	 */
	public M_Element (Ctx ctx, String columnName, String EntityType,
		Trx trx)
	{
		super(ctx, 0, trx);
		setColumnName (columnName);
		setName (columnName);
		setPrintName (columnName);
		//
		setEntityType (EntityType);	// U
	}	//	M_Element

	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Update Columns, Fields, Parameters, Print Info
		if (!newRecord)
		{
			//	Column
			StringBuffer sql = new StringBuffer("UPDATE AD_Column SET ColumnName=")
				.append(DB.TO_STRING(getColumnName()))
				.append(", Name=").append(DB.TO_STRING(getName()))
				.append(", Description=").append(DB.TO_STRING(getDescription()))
				.append(", Help=").append(DB.TO_STRING(getHelp()))
				.append(" WHERE AD_Element_ID=").append(get_ID());
			int no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Columns updated #" + no);
			
			//	Field
			sql = new StringBuffer("UPDATE AD_Field f SET Name=")
				.append(DB.TO_STRING(getName()))
				.append(", Description=").append(DB.TO_STRING(getDescription()))
				.append(", Help=").append(DB.TO_STRING(getHelp()))
				.append(" WHERE AD_Column_ID IN (SELECT AD_Column_ID FROM AD_Column WHERE AD_Element_ID=")
				.append(get_ID())
				.append(") AND IsCentrallyMaintained='Y'")
				.append(" AND NOT EXISTS ( SELECT * FROM AD_Tab t, AD_Window w, AD_ElementCTX ec")
				.append(" WHERE t.AD_Tab_ID=f.AD_Tab_ID AND w.AD_Window_ID=t.AD_Window_ID")
				.append(" AND ec.AD_Element_ID=").append(get_ID())
		        .append(" AND ec.AD_CTXArea_ID=COALESCE(t.AD_CTXArea_ID, w.AD_CTXArea_ID))");
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Fields updated #" + no);
			
			//	Parameter 
			sql = new StringBuffer("UPDATE AD_Process_Para SET ColumnName=")
				.append(DB.TO_STRING(getColumnName()))
				.append(", Name=").append(DB.TO_STRING(getName()))
				.append(", Description=").append(DB.TO_STRING(getDescription()))
				.append(", Help=").append(DB.TO_STRING(getHelp()))
				.append(", AD_Element_ID=").append(get_ID())
				.append(" WHERE UPPER(ColumnName)=")
				.append(DB.TO_STRING(getColumnName().toUpperCase()))
				.append(" AND IsCentrallyMaintained='Y' AND AD_Element_ID IS NULL");
			no = DB.executeUpdate(get_Trx(), sql.toString());
			sql = new StringBuffer("UPDATE AD_Process_Para pp SET ColumnName=")
				.append(DB.TO_STRING(getColumnName()))
				.append(", Name=").append(DB.TO_STRING(getName()))
				.append(", Description=").append(DB.TO_STRING(getDescription()))
				.append(", Help=").append(DB.TO_STRING(getHelp()))
				.append(" WHERE AD_Element_ID=").append(get_ID())
				.append(" AND IsCentrallyMaintained='Y'")
				.append(" AND NOT EXISTS (SELECT * FROM AD_Process p, AD_ElementCTX ec")				
				.append(" WHERE p.AD_Process_ID=pp.AD_Process_ID")
				.append(" AND ec.AD_Element_ID=").append(get_ID())
				.append(" AND ec.AD_CtxArea_ID=p.AD_CtxArea_ID)");
			no += DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Parameters updated #" + no);
			
			//	Print Info
			sql = new StringBuffer("UPDATE AD_PrintFormatItem pi SET PrintName=")
				.append(DB.TO_STRING(getPrintName()))
				.append(", Name=").append(DB.TO_STRING(getName()))
				.append(" WHERE IsCentrallyMaintained='Y'")	
				.append(" AND EXISTS (SELECT * FROM AD_Column c ")
					.append("WHERE c.AD_Column_ID=pi.AD_Column_ID AND c.AD_Element_ID=")
					.append(get_ID()).append(")");
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("PrintFormatItem updated #" + no);
			
			// Info Column
			sql = new StringBuffer ("UPDATE AD_InfoColumn SET Name=")
				.append(DB.TO_STRING(getName()))
				.append(", Description=").append(DB.TO_STRING(getDescription()))
				.append(", Help=").append(DB.TO_STRING(getHelp()))
				.append(" WHERE AD_Element_ID=").append(get_ID())
				.append(" AND IsCentrallyMaintained='Y'");
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("InfoWindow updated #" + no);
		}
		return success;
	}	//	afterSave
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("M_Element[");
		sb.append (get_ID()).append ("-").append (getColumnName()).append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	M_Element
