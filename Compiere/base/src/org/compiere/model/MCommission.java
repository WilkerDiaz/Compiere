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

import org.compiere.framework.*;
import org.compiere.util.*;

/**
 *	Model for Commission.
 *	(has Lines)
 *	
 *  @author Jorg Janke
 *  @version $Id: MCommission.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class MCommission extends X_C_Commission
{
    /** Logger for class MCommission */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MCommission.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Commission_ID id
	 *	@param trx transaction
	 */
	public MCommission(Ctx ctx, int C_Commission_ID, Trx trx)
	{
		super(ctx, C_Commission_ID, trx);
		if (C_Commission_ID == 0)
		{
		//	setName (null);
		//	setC_BPartner_ID (0);
		//	setC_Charge_ID (0);
		//	setC_Commission_ID (0);
		//	setC_Currency_ID (0);
			//
			setDocBasisType (DOCBASISTYPE_Invoice);	// I
			setFrequencyType (FREQUENCYTYPE_Monthly);	// M
			setListDetails (false);
		}
	}	//	MCommission

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MCommission(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MCommission

	/**
	 * 	Get Lines
	 *	@return array of lines
	 */
	public MCommissionLine[] getLines()
	{
		String sql = "SELECT * FROM C_CommissionLine WHERE C_Commission_ID=? AND isActive = 'Y' ORDER BY Line";
		ArrayList<MCommissionLine> list = new ArrayList<MCommissionLine>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, getC_Commission_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MCommissionLine(getCtx(), rs, get_Trx()));

		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e); 
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		//	Convert
		MCommissionLine[] retValue = new MCommissionLine[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getLines

	/**
	 * 	Set Date Last Run
	 *	@param DateLastRun date
	 */
	@Override
	public void setDateLastRun (Timestamp DateLastRun)
	{
		if (DateLastRun != null)
			super.setDateLastRun(DateLastRun);
	}	//	setDateLastRun

	/**
	 * 	Copy Lines From other Commission
	 *	@param otherCom commission
	 *	@return number of lines copied
	 */
	public int copyLinesFrom (MCommission otherCom)
	{
		if (otherCom == null)
			return 0;
		MCommissionLine[] fromLines = otherCom.getLines ();
		int count = 0;
		for (MCommissionLine element : fromLines) {
			MCommissionLine line = new MCommissionLine (getCtx(), 0, get_Trx());
			PO.copyValues(element, line, getAD_Client_ID(), getAD_Org_ID());
			line.set_ValueNoCheck ("C_CommissionLine_ID", null);	//	new
			line.setC_Commission_ID (getC_Commission_ID());
			if (line.save())
				count++;
		}
		if (fromLines.length != count)
			log.log(Level.SEVERE, "Line difference - From=" + fromLines.length + " <> Saved=" + count);
		return count;
	}	//	copyLinesFrom

}	//	MCommission
