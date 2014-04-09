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

import org.compiere.model.*;
import org.compiere.util.*;


/**
 *  Report Model
 *
 *  @author Jorg Janke
 *  @version $Id: MReport.java,v 1.3 2006/08/03 22:16:52 jjanke Exp $
 */
public class MReport extends X_PA_Report
{
    /** Logger for class MReport */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MReport.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Constructor
	 * 	@param ctx context
	 * 	@param PA_Report_ID id
	 * 	@param trx transaction
	 */
	public MReport (Ctx ctx, int PA_Report_ID, Trx trx)
	{
		super (ctx, PA_Report_ID, trx);
		if (PA_Report_ID == 0)
		{
		//	setName (null);
		//	setPA_ReportLineSet_ID (0);
		//	setPA_ReportColumnSet_ID (0);
			setListSources(false);
			setListTrx(false);
		}
		else
		{
			m_columnSet = new MReportColumnSet (ctx, getPA_ReportColumnSet_ID(), trx);
			m_lineSet = new MReportLineSet (ctx, getPA_ReportLineSet_ID(), trx);
		}
	}	//	MReport

	/** 
	 * 	Load Constructor 
	 * 	@param ctx context
	 * 	@param rs result set
	 * 	@param trx transaction
	 */
	public MReport (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
		m_columnSet = new MReportColumnSet (ctx, getPA_ReportColumnSet_ID(), trx);
		m_lineSet = new MReportLineSet (ctx, getPA_ReportLineSet_ID(), trx);
	}	//	MReport

	private MReportColumnSet	m_columnSet = null;
	private MReportLineSet		m_lineSet = null;

	/**
	 * 	List Info
	 */
	public void list()
	{
		System.out.println(toString());
		if (m_columnSet != null)
			m_columnSet.list();
		System.out.println();
		if (m_lineSet != null)
			m_lineSet.list();
	}	//	dump

	/**
	 * 	Get Where Clause for Report
	 * 	@return Where Clause for Report
	 */
	public String getWhereClause()
	{
		//	AD_Client indirectly via AcctSchema
		StringBuffer sb = new StringBuffer();
		//	Mandatory 	AcctSchema
		sb.append("C_AcctSchema_ID=").append(getC_AcctSchema_ID());
		//
		return sb.toString();
	}	//	getWhereClause

	/*************************************************************************/

	/**
	 * 	String Representation
	 * 	@return Info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MReport[")
			.append(get_ID()).append(" - ").append(getName());
		if (getDescription() != null)
			sb.append("(").append(getDescription()).append(")");
		sb.append(" - C_AcctSchema_ID=").append(getC_AcctSchema_ID())
			.append(", C_Calendar_ID=").append(getC_Calendar_ID());
		sb.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get Column Set
	 *	@return Column Set
	 */
	public MReportColumnSet	getColumnSet()
	{
		return m_columnSet;
	}

	/**
	 * 	Get Line Set
	 *	@return Line Set
	 */
	public MReportLineSet getLineSet()
	{
		return m_lineSet;
	}

}	//	MReport
