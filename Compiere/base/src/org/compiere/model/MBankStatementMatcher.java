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

import org.compiere.impexp.*;
import org.compiere.util.*;

/**
 *	Bank Statement Matcher Algorithm
 *	
 *  @author Jorg Janke
 *  @version $Id: MBankStatementMatcher.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class MBankStatementMatcher extends X_C_BankStatementMatcher
{
    /** Logger for class MBankStatementMatcher */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBankStatementMatcher.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Bank Statement Matcher Algorithms
	 * 	@param ctx context
	 *	@param trx transaction
	 *	@return matchers
	 */
	public static MBankStatementMatcher[] getMatchers (Ctx ctx, Trx trx)
	{
		ArrayList<MBankStatementMatcher> list = new ArrayList<MBankStatementMatcher>();
		String sql = MRole.getDefault(ctx, false).addAccessSQL(
			"SELECT * FROM C_BankStatementMatcher ORDER BY SeqNo", 
			"C_BankStatementMatcher", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add (new MBankStatementMatcher(ctx, rs, trx));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		//	Convert		
		MBankStatementMatcher[] retValue = new MBankStatementMatcher[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getMatchers

	/** Static Logger					*/
	private static CLogger 	s_log = CLogger.getCLogger(MBankStatementMatcher.class);

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_BankStatementMatcher_ID id
	 *	@param trx transaction
	 */
	public MBankStatementMatcher(Ctx ctx, int C_BankStatementMatcher_ID, Trx trx)
	{
		super(ctx, C_BankStatementMatcher_ID, trx);
	}	//	MBankStatementMatcher

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MBankStatementMatcher(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MBankStatementMatcher

	private BankStatementMatcherInterface	m_matcher = null;
	private Boolean							m_matcherValid = null;

	/**
	 * 	Is Matcher Valid
	 *	@return true if valid
	 */
	public boolean isMatcherValid()
	{
		if (m_matcherValid == null)
			getMatcher();
		return m_matcherValid.booleanValue();
	}	//	isMatcherValid

	/**
	 * 	Get Matcher 
	 *	@return Matcher Instance
	 */
	public BankStatementMatcherInterface getMatcher()
	{
		if (m_matcher != null 
			|| (m_matcherValid != null && m_matcherValid.booleanValue()))
			return m_matcher;
			
		String className = getClassname();
		if (className == null || className.length() == 0)
			return null;
		
		try
		{
			Class<?> matcherClass = Class.forName(className);
			m_matcher = (BankStatementMatcherInterface)matcherClass.newInstance();
			m_matcherValid = Boolean.TRUE;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, className, e);
			m_matcher = null;
			m_matcherValid = Boolean.FALSE;
		}
		return m_matcher;
	}	//	getMatcher


}	//	MBankStatementMatcher
