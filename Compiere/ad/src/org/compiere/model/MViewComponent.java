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
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;


/**
 *	Database View Component Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MViewComponent.java 8756 2010-05-12 21:21:27Z nnayak $
 */
public class MViewComponent extends X_AD_ViewComponent
{
    /** Logger for class MViewComponent */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MViewComponent.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_View_ID view
	 *	@param trx p_trx
	 */
	public MViewComponent(Ctx ctx, int AD_ViewComponent_ID,
		Trx trx)
	{
		super (ctx, AD_ViewComponent_ID, trx);
	}	//	MViewComponent

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MViewComponent(Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MViewComponent
	
	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 */
	public MViewComponent(MTable parent)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg (parent);
		setAD_Table_ID (parent.getAD_Table_ID());
	}	//	MViewComponent

	/** The Columns				*/
	private MViewColumn[]		m_columns = null;
	
	/**
	 * 	Get Columns
	 *	@param reload reload data
	 *	@return array of Columns
	 */
	public MViewColumn[] getColumns(boolean reload)
	{
		if (m_columns != null && !reload)
			return m_columns;
		String sql = "SELECT * FROM AD_ViewColumn WHERE AD_ViewComponent_ID=? ORDER BY AD_ViewColumn_ID";
		ArrayList<MViewColumn> list = new ArrayList<MViewColumn>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx ());
			pstmt.setInt (1, getAD_ViewComponent_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MViewColumn (getCtx(), rs, get_Trx ()));
			}
		}
		catch (Exception e) {
			log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		m_columns = new MViewColumn[list.size()];
		list.toArray (m_columns);
		return m_columns;
	} //	getColumns

	/**
	 * 	Get SQL Select
	 * 	@param requery refresh columns
	 * 	@param vCols model MViewColumn array
	 *	@return select statement
	 */
	public String getSelect(boolean requery, MViewColumn[] vCols)
	{
		getColumns(requery);
		if (m_columns == null || m_columns.length == 0)
			return null;
		
		if (vCols == null)
			vCols = m_columns;
		
		StringBuffer sb = new StringBuffer("SELECT ");
		//

		for (int i = 0; i < vCols.length; i++)
		//	for (int i = 0; i < m_columns.length; i++)
		{
			String colName = vCols[i].getColumnName();
			MViewColumn vc = null;
			for (MViewColumn element : m_columns) {
				if (element.getColumnName().equals(colName))
				{
					vc = element;
					break;
				}
			}
			if (i>0)
				sb.append(", ");
			String colSQL = vc.getColumnSQL();
			//String colName = vc.getColumnName();
			
			if (colSQL == null || colSQL.toUpperCase().equals("NULL"))
			{
				String dt = vc.getDBDataType();
				if (dt!=null)
				{
					if (dt.equals(X_AD_ViewColumn.DBDATATYPE_CharacterFixed) || 
							dt.equals(X_AD_ViewColumn.DBDATATYPE_CharacterVariable))
							colSQL = "NULLIF('a','a')";
					else
					if (dt.equals(X_AD_ViewColumn.DBDATATYPE_Decimal) || 
							dt.equals(X_AD_ViewColumn.DBDATATYPE_Integer) ||
							dt.equals(X_AD_ViewColumn.DBDATATYPE_Number))
							colSQL = "NULLIF(1,1)";
					else
					if (dt.equals(X_AD_ViewColumn.DBDATATYPE_Timestamp))
						colSQL = "NULL";
				}
				else
					colSQL = "NULL";
			}
			
			sb.append(colSQL);
			if (!colName.equals("*"))
				sb.append(" AS ").append(colName);
		}
		
		sb.append(" ").append(getFromClause());
		String t = getWhereClause();
		if (t!=null && t.length()>0)
			sb.append(" ").append(t);
		t = getOtherClause();
		if (t!=null && t.length()>0)
			sb.append(" ").append(t);
		
		return sb.toString();
	}	//	getViewCreate
	
	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MViewComponent[")
	    	.append(get_ID())
	        .append("-").append(getName());
	    sb.append("]");
	    return sb.toString();
    }	//	toString
	
}	//	MViewComponent
