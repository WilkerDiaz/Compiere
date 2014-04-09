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
 *	Dunning Run Entry Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MDunningRunEntry.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class MDunningRunEntry extends X_C_DunningRunEntry
{
    /** Logger for class MDunningRunEntry */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MDunningRunEntry.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Logger								*/
	private static CLogger		s_log = CLogger.getCLogger (MPayment.class);

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_DunningRunEntry_ID id
	 *	@param trx transaction
	 */
	public MDunningRunEntry (Ctx ctx, int C_DunningRunEntry_ID, Trx trx)
	{
		super (ctx, C_DunningRunEntry_ID, trx);
		if (C_DunningRunEntry_ID == 0)
		{
		//	setC_BPartner_ID (0);
		//	setC_BPartner_Location_ID (0);
		//	setAD_User_ID (0);
			
		//	setSalesRep_ID (0);
		//	setC_Currency_ID (0);
			setAmt (Env.ZERO);
			setQty (Env.ZERO);
			setProcessed (false);
		}
	}	//	MDunningRunEntry

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MDunningRunEntry (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MDunningRunEntry
	
	/**
	 * 	Parent Constructor
	 *	@param parent parent 
	 */
	public MDunningRunEntry (MDunningRun parent)
	{
		this (parent.getCtx(), 0, parent.get_Trx());
		setClientOrg(parent);
		setC_DunningRun_ID(parent.getC_DunningRun_ID());
		m_parent = parent;
	}	//	MDunningRunEntry

	/** Parent				*/
	private MDunningRun		m_parent = null;
	
	public boolean equals(Object o)
	{
		if(this==o)
			return true;
		if(o==null || getClass() != o.getClass())
			return false;
		MDunningRunEntry that = (MDunningRunEntry)o;
		if(this.getC_DunningRunEntry_ID() == 0 || that.getC_DunningRunEntry_ID() ==0)
			return false;
		super.equals(o);
		return true;
	}
	
	/**
	 * 	Set BPartner
	 *	@param bp partner
	 *	@param isSOTrx SO
	 */
	public void setBPartner (MBPartner bp, boolean isSOTrx)
	{
		setC_BPartner_ID(bp.getC_BPartner_ID());
		MBPartnerLocation[] locations = bp.getLocations(false);
		//	Location
		if (locations.length == 1)
			setC_BPartner_Location_ID (locations[0].getC_BPartner_Location_ID());
		else
		{
			for (MBPartnerLocation location : locations) {
				if (!location.isActive())
					continue;
				if ((location.isPayFrom() && isSOTrx)
					|| (location.isRemitTo() && !isSOTrx))
				{
					setC_BPartner_Location_ID (location.getC_BPartner_Location_ID());
					break;
				}
			}
		}
		if (getC_BPartner_Location_ID() == 0)
		{
			String msg= bp.getName()+" : "+"@BPartnerwithNoPayFrom@";
			throw new IllegalArgumentException (msg);
		}
		//	User with location
		MUser[] users = MUser.getOfBPartner(getCtx(), bp.getC_BPartner_ID());
		if (users.length == 1)
			setAD_User_ID (users[0].getAD_User_ID());
		else
		{
			for (MUser user : users) {
				if (user.getC_BPartner_Location_ID() == getC_BPartner_Location_ID())
				{
					setAD_User_ID (user.getAD_User_ID());
					break;
				}
			}
		}
		//
		int SalesRep_ID = bp.getSalesRep_ID();
		if (SalesRep_ID != 0)
			setSalesRep_ID (SalesRep_ID);
	}	//	setBPartner
	
	/**
	 * 	Get Lines
	 *	@return Array of all lines for this Run
	 */
	public MDunningRunLine[] getLines() 
	{
		return getLines(false); 
	}	//	getLines

	/**
	 * 	Get Lines
	 *  @param onlyInvoices only with invoices 
	 *	@return Array of all lines for this Run
	 */
	public MDunningRunLine[] getLines (boolean onlyInvoices) 
	{
		ArrayList<MDunningRunLine> list = new ArrayList<MDunningRunLine>();
		String sql = "SELECT * FROM C_DunningRunLine WHERE C_DunningRunEntry_ID=?";
		if (onlyInvoices)
			sql += " AND C_Invoice_ID IS NOT NULL";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, get_ID ());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MDunningRunLine(getCtx(), rs, get_Trx()));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//
		MDunningRunLine[] retValue = new MDunningRunLine[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getLines

	/**
	 * 	Check whether has Invoices
	 *	@return true if it has Invoices
	 */
	public boolean hasInvoices() 
	{
		boolean retValue = false;
		String sql = "SELECT COUNT(1) FROM C_DunningRunLine WHERE C_DunningRunEntry_ID=? AND C_Invoice_ID IS NOT NULL";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, get_ID ());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				if (rs.getInt(1) > 0) 
					retValue = true;
			}
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return retValue;
	}	//	hasInvoices
	
	/**
	 * 	Get Parent
	 *	@return Dunning Run
	 */
	MDunningRun getParent() 
	{
		if (m_parent == null) 
			m_parent = new MDunningRun(getCtx(), getC_DunningRun_ID (), get_Trx());
		return m_parent;
	}	//	getParent
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		//	Set Processed
		if (isProcessed() && is_ValueChanged("Processed"))
		{
			MDunningRunLine[] theseLines = getLines();
			for (MDunningRunLine element : theseLines) {
				element.setProcessed (true);
				element.save(get_Trx());
			}
		}
		return true;
	}	//	beforeSave

}	//	MDunningRunEntry
