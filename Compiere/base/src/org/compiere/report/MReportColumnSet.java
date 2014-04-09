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
package org.compiere.report;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;


/**
 *  Report Column Set Model
 *
 *  @author Jorg Janke
 *  @version $Id: MReportColumnSet.java,v 1.3 2006/08/03 22:16:52 jjanke Exp $
 */
public class MReportColumnSet extends X_PA_ReportColumnSet
{
    /** Logger for class MReportColumnSet */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MReportColumnSet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Constructor
	 * 	@param ctx context
	 * 	@param PA_ReportColumnSet_ID id
	 * 	@param trx transaction
	 */
	public MReportColumnSet (Ctx ctx, int PA_ReportColumnSet_ID, Trx trx)
	{
		super (ctx, PA_ReportColumnSet_ID, trx);
		if (PA_ReportColumnSet_ID == 0)
		{
		}
		else
			loadColumns();
	}	//	MReportColumnSet

	/** Contained Columns		*/
	private MReportColumn[]	m_columns = null;

	/**
	 *	Load contained columns
	 */
	private void loadColumns()
	{
		ArrayList<MReportColumn> list = new ArrayList<MReportColumn>();
		String sql = "SELECT * FROM PA_ReportColumn WHERE PA_ReportColumnSet_ID=? AND IsActive='Y' ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getPA_ReportColumnSet_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MReportColumn (getCtx(), rs, null));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}
		//
		m_columns = new MReportColumn[list.size()];
		list.toArray(m_columns);
		log.finest("ID=" + getPA_ReportColumnSet_ID() 
			+ " - Size=" + list.size());
	}	//	loadColumns

	/**
	 * 	Get Columns
	 *	@return columns
	 */
	public MReportColumn[] getColumns()
	{
		return m_columns;
	}	//	getColumns

	/**
	 * 	List Info
	 */
	public void list()
	{
		System.out.println(toString());
		if (m_columns == null)
			return;
		for (MReportColumn element : m_columns)
			System.out.println("- " + element.toString());
	}	//	list

	/*************************************************************************/

	/**
	 * 	String Representation
	 * 	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MReportColumnSet[")
			.append(get_ID()).append(" - ").append(getName())
			.append ("]");
		return sb.toString ();
	}

}	//	MReportColumnSet
