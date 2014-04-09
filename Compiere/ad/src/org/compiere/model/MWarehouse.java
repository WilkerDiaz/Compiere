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
 *	Warehouse Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MWarehouse.java 8780 2010-05-19 23:08:57Z nnayak $
 */
public class MWarehouse extends X_M_Warehouse
{
    /** Logger for class MWarehouse */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MWarehouse.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get from Cache
	 *	@param ctx context
	 *	@param M_Warehouse_ID id
	 *	@return warehouse
	 */
	public static MWarehouse get (Ctx ctx, int M_Warehouse_ID)
	{
		Integer key = Integer.valueOf(M_Warehouse_ID);
		MWarehouse retValue = s_cache.get(ctx, key);
		if (retValue != null)
			return retValue;
		//
		retValue = new MWarehouse (ctx, M_Warehouse_ID, null);
		s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get Warehouses for Org
	 *	@param ctx context
	 *	@param AD_Org_ID id
	 *	@return warehouse
	 */
	public static MWarehouse[] getForOrg (Ctx ctx, int AD_Org_ID)
	{
		ArrayList<MWarehouse> list = new ArrayList<MWarehouse>();
		String sql = "SELECT * FROM M_Warehouse WHERE AD_Org_ID=? ORDER BY Created";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_Org_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MWarehouse (ctx, rs, null));
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		MWarehouse[] retValue = new MWarehouse[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	get
	
	
	/**	Cache					*/
	private static final CCache<Integer,MWarehouse> s_cache = new CCache<Integer,MWarehouse>("M_Warehouse", 5);
	/**	Static Logger	*/
	private static final CLogger	s_log	= CLogger.getCLogger (MWarehouse.class);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_Warehouse_ID id
	 *	@param trx transaction
	 */
	public MWarehouse (Ctx ctx, int M_Warehouse_ID, Trx trx)
	{
		super(ctx, M_Warehouse_ID, trx);
		if (M_Warehouse_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
		//	setC_Location_ID (0);
			setSeparator ("*");	// *
		}
	}	//	MWarehouse
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MWarehouse (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MWarehouse

	/**
	 * 	Organization Constructor
	 *	@param org parent
	 */
	public MWarehouse (MOrg org)
	{
		this (org.getCtx(), 0, org.get_Trx());
		setClientOrg(org);
		setValue (org.getValue());
		setName (org.getName());
		if (org.getInfo() != null)
			setC_Location_ID (org.getInfo().getC_Location_ID());
	}	//	MWarehouse

	/**	Warehouse Locators				*/
	private MLocator[]	m_locators = null;
	/**	Default Locator					*/
	private int			m_M_Locator_ID = -1;
	
	/**
	 * 	Get Locators
	 *	@param reload if true reload
	 *	@return array of locators
	 */
	public MLocator[] getLocators(boolean reload)
	{
		if (!reload && m_locators != null)
			return m_locators;
		//
		String sql = "SELECT * FROM M_Locator WHERE M_Warehouse_ID=? ORDER BY X,Y,Z";
		ArrayList<MLocator> list = new ArrayList<MLocator>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, getM_Warehouse_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MLocator (getCtx(), rs, null));
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		m_locators = new MLocator[list.size()];
		list.toArray (m_locators);
		return m_locators;
	}	//	getLocators
	
	/**
	 * 	Get Default Locator
	 *	@return (first) default locator
	 */
	public MLocator getDefaultLocator()
	{
		MLocator defaultLoc = null;
		// Try to find default locator directly
		String sql = "SELECT * FROM M_Locator WHERE M_Warehouse_ID=? AND IsDefault='Y' AND IsActive='Y' ORDER BY X,Y,Z";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, getM_Warehouse_ID());
			rs = pstmt.executeQuery ();
			if (rs.next ())
				defaultLoc = new MLocator (getCtx(), rs, null);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}


		if (defaultLoc != null)
			return defaultLoc;

		log.info("No default Locator for " + getName());

		String sql2 = "SELECT * FROM M_Locator WHERE M_Warehouse_ID=? AND IsActive='Y' ORDER BY PriorityNo, X, Y, Z";
		try
		{
			pstmt = DB.prepareStatement(sql2, (Trx) null);
			pstmt.setInt (1, getM_Warehouse_ID());
			rs = pstmt.executeQuery ();
			if (rs.next ())
				defaultLoc = new MLocator (getCtx(), rs, null);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}


		if (defaultLoc != null)
			return defaultLoc;


		//	No Locator - create one
		MLocator loc = new MLocator (this, "Standard");
		loc.setIsDefault(true);
		loc.save();
		log.info("Created default locator for " + getName());
		return loc;
	}	//	getDefaultLocator
	
	/**
	 *  Check if locator is in warehouse 
	 *  return true if locator is in the warehouse
	 */
	public static boolean IsLocatorInWarehouse(int p_M_Warehouse_ID, int p_M_Locator_ID)
	{
		int M_Warehouse_ID = QueryUtil.getSQLValue(null, 
				"SELECT M_Warehouse_ID FROM M_Locator WHERE M_Locator_ID=?", p_M_Locator_ID);
		if(p_M_Warehouse_ID == M_Warehouse_ID)
			return true;
		
		return false;
	}
	
	/**
	 * 	Get Default M_Locator_ID
	 *	@return id
	 */
	public int getDefaultM_Locator_ID()
	{
		if (m_M_Locator_ID <= 0)
			m_M_Locator_ID = getDefaultLocator().getM_Locator_ID();
		return m_M_Locator_ID;
	}	//	getDefaultM_Locator_ID
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MWarehouse[");
		sb.append (get_ID ()).append ("-").append (getName ()).append ("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * Before Save
	 * @param newRecord new
	 * @param success success
	 * @return success
	 */
	@Override
	protected boolean beforeSave(boolean newRecord) 
	{
		/* Disallow Negative Inventory cannot be checked if there are storage records 
		with negative onhand. */
		if (is_ValueChanged("IsDisallowNegativeInv") && isDisallowNegativeInv())
		{
			String sql = "SELECT M_Product_ID FROM M_StorageDetail s "+
						 "WHERE s.QtyType = 'H' AND s.M_Locator_ID IN (SELECT M_Locator_ID FROM M_Locator l " +
						 				"WHERE M_Warehouse_ID=? )" +
						 " GROUP BY M_Product_ID, M_Locator_ID " +
						 " HAVING SUM(s.Qty) < 0 ";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Boolean negInvExists = false;
			try
			{
				pstmt = DB.prepareStatement(sql, get_Trx());
				pstmt.setInt(1, getM_Warehouse_ID());
				rs = pstmt.executeQuery();
				if (rs.next())
					negInvExists = true;
			}
			catch (Exception e) {
				log.log(Level.SEVERE, sql , e);
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}

			if (negInvExists)
			{
				log.saveError("Error", Msg.translate(getCtx(), "NegativeOnhandExists"));
				return false;
			}		
		}
		
		if (getAD_Org_ID() == 0)
		{
			int context_AD_Org_ID = getCtx().getAD_Org_ID();
			if (context_AD_Org_ID != 0)
			{
				setAD_Org_ID(context_AD_Org_ID);
				log.warning("Changed Org to Context=" + context_AD_Org_ID);
			}
			else
			{
				log.saveError("Error", Msg.translate(getCtx(), "Org0NotAllowed"));
				return false;
			}
		}
		
		if((newRecord || is_ValueChanged("IsWMSEnabled") || is_ValueChanged("IsDisallowNegativeInv")) 
				&& isWMSEnabled() && !isDisallowNegativeInv())
		{
			log.saveError("Error", Msg.translate(getCtx(), "NegativeInventoryDisallowedWMS"));
			return false;
		}
		
		if(newRecord || is_ValueChanged("Separator")) {
			if(getSeparator() == null || getSeparator().isEmpty())
				setSeparator("*");
		}
		
		return true;
	}
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord & success)
			success = insert_Accounting("M_Warehouse_Acct", "C_AcctSchema_Default", null);
		
		return success;
	}	//	afterSave

	/**
	 * 	Before Delete
	 *	@return true
	 */
	@Override
	protected boolean beforeDelete ()
	{
		return delete_Accounting("M_Warehouse_Acct"); 
	}	//	beforeDelete

}	//	MWarehouse
