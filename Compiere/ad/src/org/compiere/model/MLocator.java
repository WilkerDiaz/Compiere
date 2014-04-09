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

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.framework.PO;
import org.compiere.util.*;


/**
 *	Warehouse Locator Object
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: MLocator.java 8780 2010-05-19 23:08:57Z nnayak $
 */
public class MLocator extends X_M_Locator
{
    /** Logger for class MLocator */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MLocator.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get oldest Default Locator of warehouse with locator
	 *	@param ctx context
	 *	@param M_Locator_ID locator
	 *	@return locator or null
	 */
	public static MLocator getDefault (Ctx ctx, int M_Locator_ID)
	{
		
		Trx trx = null;
		MLocator retValue = null;
		String sql = "SELECT * FROM M_Locator l "
			+ "WHERE IsDefault='Y'"
			+ " AND EXISTS (SELECT * FROM M_Locator lx "
				+ "WHERE l.M_Warehouse_ID=lx.M_Warehouse_ID AND lx.M_Locator_ID=?) "
			+ "ORDER BY Created";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, M_Locator_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				retValue = new MLocator (ctx, rs, trx);
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return retValue;
	}	//	getDefault
	
	
	/**
	 * 	Get Default Locator in organization
	 *	@param ctx context
	 *	@param AD_Org_ID org
	 *	@return locator or null
	 */
	public static MLocator getDefaultLocatorOfOrg (Ctx ctx, int AD_Org_ID)
	{	
		MLocator retValue = null;
		ArrayList<Integer> defaultlocators = new ArrayList<Integer>();
		ArrayList<Integer> locators = new ArrayList<Integer>();
		String sql = "SELECT M_Locator_ID, IsDefault FROM M_Locator WHERE (AD_Org_ID=? OR 0=?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_Org_ID);
			pstmt.setInt (2, AD_Org_ID);
			rs = pstmt.executeQuery ();
			while(rs!=null && rs.next())
			{
				if(rs.getString(2)=="Y" ){
					defaultlocators.add(rs.getInt(1));
					break;
				}

				else
					locators.add(rs.getInt(1));
			}

			if (defaultlocators.size() > 0)
			{				
				retValue = MLocator.get(ctx, Integer.valueOf(defaultlocators.get(0)));
				return  retValue;						
			}
			if (locators.size()>0)
			{
				retValue = MLocator.get(ctx, Integer.valueOf(locators.get(0)));
				return  retValue;
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
	}	//	getDefaultLocatorofOrg

	/**
	 * 	Get the Locator with the combination or create new one
	 *	@param ctx Context
	 *	@param M_Warehouse_ID warehouse
	 *	@param Value value
	 *	@param X x
	 *	@param Y y
	 *	@param Z z
	 * 	@return locator
	 */
	 public static MLocator get (Ctx ctx, int M_Warehouse_ID, String Value,
		 String Aisle, String Bay, String Row, String Position, String Bin)
	 {
		MLocator retValue = null;
		String sql = "SELECT * FROM M_Locator WHERE M_Warehouse_ID=? AND X=? AND Y=? AND Z=? AND Position=? AND Bin=? ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, M_Warehouse_ID);
			pstmt.setString(2, Aisle);
			pstmt.setString(3, Bay);
			pstmt.setString(4, Row);
			pstmt.setString(5, Position);
			pstmt.setString(6, Bin);
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = new MLocator (ctx, rs, null);
		}
		catch (SQLException ex) {
			s_log.log(Level.SEVERE, "get", ex);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		if (retValue == null)
		{
			MWarehouse wh = MWarehouse.get (ctx, M_Warehouse_ID);
			retValue = new MLocator (wh, Value);
			retValue.setXYZ(Aisle, Bay, Row, Position, Bin);
			if (!retValue.save())
				retValue = null;
		}
		return retValue;
	 }	//	get

	/**
	 * 	Get Locator from Cache
	 *	@param ctx context
	 *	@param M_Locator_ID id
	 *	@return MLocator
	 */
	public static MLocator get (Ctx ctx, int M_Locator_ID)
	{
		Integer key = Integer.valueOf (M_Locator_ID);
		MLocator retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MLocator (ctx, M_Locator_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static final CCache<Integer,MLocator> s_cache	= new CCache<Integer,MLocator>("M_Locator", 20);
 
	 
	/**	Logger						*/
	private static final CLogger		s_log = CLogger.getCLogger (MLocator.class);
	
	
	/**************************************************************************
	 * 	Standard Locator Constructor
	 *	@param ctx Context
	 *	@param M_Locator_ID id
	 *	@param trx transaction
	 */
	public MLocator (Ctx ctx, int M_Locator_ID, Trx trx)
	{
		super (ctx, M_Locator_ID, trx);
		if (M_Locator_ID == 0)
		{
		//	setM_Locator_ID (0);		//	PK
		//	setM_Warehouse_ID (0);		//	Parent
			setIsDefault (false);
			setPriorityNo (50);
		//	setValue (null);
		//	setX (null);
		//	setY (null);
		//	setZ (null);
		}
	}	//	MLocator

	/**
	 * 	New Locator Constructor with XYZ=000
	 *	@param warehouse parent
	 *	@param Value value
	 */
	public MLocator (MWarehouse warehouse, String Value)
	{
		this (warehouse.getCtx(), 0, warehouse.get_Trx());
		setClientOrg(warehouse);
		setM_Warehouse_ID (warehouse.getM_Warehouse_ID());		//	Parent
		setValue (Value);
		setXYZ("0","0","0","0","0");
	}	//	MLocator

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MLocator (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MLocator

	/**
	 * 	Import Constructor
	 *	@param imp import
	 */
	public MLocator (X_I_Locator loc)
	{
		this (loc.getCtx(), 0, loc.get_Trx());
		PO.copyValues(loc, this, loc.getAD_Client_ID(), loc.getAD_Org_ID());
		I_Locator_ID = loc.getI_Locator_ID();
	}	//	MLocator

	private int I_Locator_ID = 0;
	
	public int getI_Locator_ID() {
		return I_Locator_ID;
	}
	
	/**
	 * 	Import Constructor
	 *	@param imp import
	 */
	public MLocator (int M_Locator_ID, X_I_Locator loc)
	{
		this (loc.getCtx(), M_Locator_ID, loc.get_Trx());
		PO.copyValues(loc, this, loc.getAD_Client_ID(), loc.getAD_Org_ID());
		I_Locator_ID = loc.getI_Locator_ID();
	}	//	MLocator

	/**
	 *	Get String Representation
	 * 	@return Value
	 */
	@Override
	public String toString()
	{
		return getValue();
	}	//	getValue

	/**
	 * 	Set Location
	 *	@param X x
	 *	@param Y y
	 *	@param Z z
	 */
	public void setXYZ (String Aisle, String Bay, String Row, String Position, String Bin)
	{
		setX (Aisle);
		setY (Bay);
		setZ (Row);
		setPosition (Position);
		setBin (Bin);
	}	//	setXYZ
	
	/**
	 * Is this locator fixed 
	 * A locator is considered fixed if there is any product assigned to it.
	 * @return true if locator is fixed
	 */
	public boolean isFixed()
	{
		String sql = "SELECT count(*) FROM M_ProductLocator pl " +
						"WHERE pl.M_Locator_ID = ? ";
		
		int ii = QueryUtil.getSQLValue (get_Trx(), sql, getM_Locator_ID());
		if(ii != 0)
			return true;
		return false;
	}
	
	
	/**
	 * 	Get Warehouse Name
	 * 	@return name
	 */
	public String getWarehouseName()
	{
		MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
		if (wh.get_ID() == 0)
			return "<" + getM_Warehouse_ID() + ">";
		return wh.getName();
	}	//	getWarehouseName
	
	
	/**
	 * 	Get Storage Info for a Locator 
	 *	@param ctx context
	 *	@param M_Locator_ID Locator
	 *	@param trx transaction
	 *	@return existing or null
	 */
	public static boolean checkStock (Ctx ctx, int M_Locator_ID, Trx trx)
	{
		//ArrayList<MStorage> list = new ArrayList<MStorage>();
		String sql = "SELECT COALESCE(SUM(CASE WHEN QtyType LIKE 'H' THEN Qty ELSE 0 END), 0) QtyOnHand, " +
							"COALESCE(SUM(CASE WHEN QtyType LIKE 'O' THEN Qty ELSE 0 END), 0) QtyOrdered, " +
							"COALESCE(SUM(CASE WHEN QtyType LIKE 'R' THEN Qty ELSE 0 END), 0) QtyReserved " +
				"FROM M_StorageDetail WHERE M_Locator_ID=?";
		BigDecimal OnHand = Env.ZERO;
		BigDecimal Ordered = Env.ZERO;
		BigDecimal Reserved = Env.ZERO;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, M_Locator_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ()){
				OnHand = rs.getBigDecimal("QtyOnHand");			
				Ordered = rs.getBigDecimal("QtyOrdered");
				Reserved = rs.getBigDecimal("QtyReserved");
			}
		}
		catch (SQLException ex) {
			s_log.log(Level.SEVERE, sql, ex);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		if ((OnHand.signum() != 0) ||(Ordered.signum() != 0) || (Reserved.signum() != 0))
				return true;
		else
			return false;
	}	//	getOfLocator
	
	
	/**
	 * Before Save
	 * @param newRecord new
	 * @return save
	 */
	@Override
	protected boolean beforeSave(boolean newRecord) {
	
//		Check Storage
		if (is_ValueChanged("IsActive") && !isActive())		//	now not active 
			
		{
			if (checkStock(getCtx(),get_ID(),get_Trx()))
			{
				log.saveError("Error", Msg.getMsg(getCtx(), "LocatorHasStock"));
				return false;
			}
			
		}	//	storage
		
		if(newRecord 
				|| is_ValueChanged("X")
				|| is_ValueChanged("Y")
				|| is_ValueChanged("Z")
				|| is_ValueChanged("Position") 
				|| is_ValueChanged("Bin"))
		{
			MWarehouse wh = new MWarehouse(getCtx(), getM_Warehouse_ID(), get_Trx()); 
			
			if(getBin()!= null && getBin().length()!=0 && 
					(getPosition()== null || getPosition().length()==0))
			{
				log.saveError("Error", Msg.getMsg(getCtx(), "PositionMandatorySegment"));
				return false;				
			}
	
			
			if (getX() == null || getX().length()==0 || !Util.isAlphaNumeric(getX()) ||
				getY() == null || getY().length()==0 || !Util.isAlphaNumeric(getY()) ||
				getZ() == null || getZ().length()==0 || !Util.isAlphaNumeric(getZ()) ||
				(getBin() != null && getBin().length()!=0 && !Util.isAlphaNumeric(getBin())) ||
				(getPosition() != null && getPosition().length()!=0 && !Util.isAlphaNumeric(getPosition())))
			{
				log.saveError("Error", Msg.getMsg(getCtx(), "SegmentNotAlphaNumeric"));
				return false;				
			}

			String combination =  getX().concat(wh.getSeparator());
			combination = combination.concat(getY()).concat(wh.getSeparator());
			combination = combination.concat(getZ());
			
			if(getPosition()!= null && getPosition().length() !=0)
				combination = combination.concat(wh.getSeparator()).concat(getPosition());
			
			if(getBin()!= null && getBin().length() !=0)
				combination = combination.concat(wh.getSeparator()).concat(getBin());
			
			log.fine("Set Locator Combination :" + combination);
			
			String sql = "SELECT count(*) FROM M_Locator WHERE M_Locator_ID<>?"+
							" AND M_Warehouse_ID = ?" +
							" AND UPPER(LocatorCombination) = UPPER(?)";
							
			int ii = QueryUtil.getSQLValue (get_Trx(), sql, getM_Locator_ID(), getM_Warehouse_ID(), combination);
			if(ii != 0)
			{
				log.saveError("Error", Msg.getMsg(getCtx(), "CombinationNotUnique"));
				return false;
			}

			setLocatorCombination(combination);
		}
		
		if(newRecord 
				|| is_ValueChanged("IsAvailableToPromise")
				|| is_ValueChanged("IsAvailableForAllocation"))
		{
			if (isAvailableForAllocation() && !isAvailableToPromise())
			{
				log.saveError("Error", Msg.getMsg(getCtx(), "InvalidCombination"));
				return false;				
			}
		}
		return true;
	}

	
}	//	MLocator
