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

import org.compiere.api.UICallout;
import org.compiere.util.*;

/**
 *	AP Payment Selection
 *	
 *  @author Jorg Janke
 *  @version $Id: MPaySelection.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MPaySelection extends X_C_PaySelection
{
    /** Logger for class MPaySelection */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MPaySelection.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param C_PaySelection_ID id
	 *	@param trx transaction
	 */
	public MPaySelection (Ctx ctx, int C_PaySelection_ID, Trx trx)
	{
		super(ctx, C_PaySelection_ID, trx);
		if (C_PaySelection_ID == 0)
		{
		//	setC_BankAccount_ID (0);
		//	setName (null);	// @#Date@
		//	setPayDate (new Timestamp(System.currentTimeMillis()));	// @#Date@
			setTotalAmt (Env.ZERO);
			setIsApproved (false);
			setProcessed (false);
			setProcessing (false);
		}
	}	//	MPaySelection

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MPaySelection(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MPaySelection

	/**	Lines						*/
	private MPaySelectionLine[]	m_lines = null;
	/**	Currency of Bank Account	*/
	private int					m_C_Currency_ID = 0;
	
	/**
	 * 	Get Lines
	 *	@param requery requery
	 *	@return lines
	 */
	public MPaySelectionLine[] getLines(boolean requery)
	{
		if (m_lines != null && !requery)
			return m_lines;
		ArrayList<MPaySelectionLine> list = new ArrayList<MPaySelectionLine>();
		String sql = "SELECT * FROM C_PaySelectionLine WHERE C_PaySelection_ID=? ORDER BY Line";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getC_PaySelection_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MPaySelectionLine(getCtx(), rs, get_Trx()));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getLines", e); 
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//
		m_lines = new MPaySelectionLine[list.size ()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getLines
	
	/**
	 * 	Get Currency of Bank Account
	 *	@return C_Currency_ID
	 */
	@Override
	public int getC_Currency_ID()
	{
		if (m_C_Currency_ID == 0)
		{
			String sql = "SELECT C_Currency_ID FROM C_BankAccount " 
				+ "WHERE C_BankAccount_ID=?";
			m_C_Currency_ID = QueryUtil.getSQLValue(get_Trx(), sql, getC_BankAccount_ID());
		}
		return m_C_Currency_ID;
	}	//	getC_Currency_ID
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MPaySelection[");
		sb.append(get_ID()).append(",").append(getName())
			.append("]");
		return sb.toString();
	}	//	toString
	
	/**
	 * Set C_Currency_ID based on C_BankAccount_ID - Callout
	 * @param oldC_BankAccount_ID old account
	 * @param newC_BankAccount_ID new account
	 * @param windowNo
	 * @throws Exception
	 */
	@UICallout public void setC_BankAccount_ID (String oldC_BankAccount_ID, 
			String newC_BankAccount_ID, int windowNo) throws Exception
			{
		// If Bank Account is NULL, reset Currency
		if (newC_BankAccount_ID == null || newC_BankAccount_ID.length() == 0)
		{
			set_ValueNoCheck("C_Currency_ID", null);
			return;
		}
		int C_BankAccount_ID = Integer.parseInt(newC_BankAccount_ID);
		if (C_BankAccount_ID == 0)
		{
			set_ValueNoCheck("C_Currency_ID", null);
			return;
		}
		// Set Currency From Bank Account
		MBankAccount bankAccount = new MBankAccount(Env.getCtx(), C_BankAccount_ID, (Trx)null);
		setC_Currency_ID(bankAccount.getC_Currency_ID());

			}	//	setC_BankAccount_ID


	
	
}	//	MPaySelection
