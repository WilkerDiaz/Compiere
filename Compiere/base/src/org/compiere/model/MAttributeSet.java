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
 *  Product Attribute Set
 *
 *	@author Jorg Janke
 *	@version $Id: MAttributeSet.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MAttributeSet extends X_M_AttributeSet
{
    /** Logger for class MAttributeSet */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeSet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get MAttributeSet from Cache
	 *	@param ctx context
	 *	@param M_AttributeSet_ID id
	 *	@return MAttributeSet
	 */
	public static MAttributeSet get (Ctx ctx, int M_AttributeSet_ID)
	{
		Integer key = Integer.valueOf (M_AttributeSet_ID);
		MAttributeSet retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MAttributeSet (ctx, M_AttributeSet_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static final CCache<Integer,MAttributeSet> s_cache
		= new CCache<Integer,MAttributeSet> ("M_AttributeSet", 20);
	
	
	/**
	 * 	Standard constructor
	 *	@param ctx context
	 *	@param M_AttributeSet_ID id
	 *	@param trx transaction
	 */
	public MAttributeSet (Ctx ctx, int M_AttributeSet_ID, Trx trx)
	{
		super (ctx, M_AttributeSet_ID, trx);
		if (M_AttributeSet_ID == 0)
		{
		//	setName (null);
			setIsGuaranteeDate (false);
			setIsGuaranteeDateMandatory (false);
			setIsLot (false);
			setIsLotMandatory (false);
			setIsSerNo (false);
			setIsSerNoMandatory (false);
			setIsInstanceAttribute(false);
			setMandatoryType (MANDATORYTYPE_NotMandatory);
		}
	}	//	MAttributeSet

	/**
	 * 	Load constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MAttributeSet (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAttributeSet

	/**	Instance Attributes					*/
	private MAttribute[]		m_instanceAttributes = null;
	/**	Instance Attributes					*/
	private MAttribute[]		m_productAttributes = null;
	
	/** Entry Exclude						*/
	private X_M_AttributeSetExclude[] m_excludes = null;
	/** Lot create Exclude						*/
	private X_M_LotCtlExclude[] 	m_excludeLots = null;
	/** Serial No create Exclude				*/
	private X_M_SerNoCtlExclude[]	m_excludeSerNos = null;

	/**
	 * 	Get Attribute Array
	 * 	@param instanceAttributes true if for instance
	 *	@return instance or product attribute array
	 */
	public MAttribute[] getMAttributes (boolean instanceAttributes)
	{
		if ((m_instanceAttributes == null && instanceAttributes)
			|| m_productAttributes == null && !instanceAttributes)
		{
			String sql = "SELECT mau.M_Attribute_ID "
				+ "FROM M_AttributeUse mau"
				+ " INNER JOIN M_Attribute ma ON (mau.M_Attribute_ID=ma.M_Attribute_ID) "
				+ "WHERE mau.IsActive='Y' AND ma.IsActive='Y'"
				+ " AND mau.M_AttributeSet_ID=? AND ma.IsInstanceAttribute=? "	//	#1,2
				+ "ORDER BY mau.SeqNo";
			ArrayList<MAttribute> list = new ArrayList<MAttribute>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_Trx());
				pstmt.setInt(1, getM_AttributeSet_ID());
				pstmt.setString(2, instanceAttributes ? "Y" : "N");
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					MAttribute ma = new MAttribute (getCtx(), rs.getInt(1), get_Trx());
					list.add (ma);
				}
			}
			catch (SQLException ex)
			{
				log.log(Level.SEVERE, sql, ex);
			}
			finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			
			//	Differentiate attributes
			if (instanceAttributes)
			{
				m_instanceAttributes = new MAttribute[list.size()];
				list.toArray (m_instanceAttributes);
			}
			else
			{
				m_productAttributes = new MAttribute[list.size()];
				list.toArray (m_productAttributes);
			}
		}
		//
		if (instanceAttributes)
		{
			if (isInstanceAttribute() != m_instanceAttributes.length > 0)
				setIsInstanceAttribute(m_instanceAttributes.length > 0);
		}
		
		//	Return
		if (instanceAttributes)
			return m_instanceAttributes;
		return m_productAttributes;
	}	//	getMAttributes

	/**
	 * 	Something is Mandatory
	 *	@return true if something is mandatory
	 */
	public boolean isMandatory()
	{
		return !MANDATORYTYPE_NotMandatory.equals(getMandatoryType())
			|| isLotMandatory()
			|| isSerNoMandatory()
			|| isGuaranteeDateMandatory();
	}	//	isMandatory
	
	/**
	 * 	Is always mandatory
	 *	@return mandatory 
	 */
	public boolean isMandatoryAlways()
	{
		return MANDATORYTYPE_AlwaysMandatory.equals(getMandatoryType());
	}	//	isMandatoryAlways
	
	/**
	 * 	Is Mandatory when Shipping
	 *	@return true if required for shipping
	 */
	public boolean isMandatoryShipping()
	{
		return MANDATORYTYPE_WhenShipping.equals(getMandatoryType());
	}	//	isMandatoryShipping
	
	/**
	 * 	Exclude entry
	 *	@param AD_Column_ID column
	 *	@param isSOTrx sales order
	 *	@return true if excluded
	 */
	public boolean excludeEntry (int AD_Column_ID, boolean isSOTrx)
	{
		if (m_excludes == null)
		{
			ArrayList<X_M_AttributeSetExclude> list = new ArrayList<X_M_AttributeSetExclude>();
			String sql = "SELECT * FROM M_AttributeSetExclude WHERE IsActive='Y' AND M_AttributeSet_ID=?";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_Trx());
				pstmt.setInt (1, getM_AttributeSet_ID());
				rs = pstmt.executeQuery ();
				while (rs.next ())
					list.add (new X_M_AttributeSetExclude (getCtx(), rs, get_Trx()));
			}
			catch (Exception e)
			{
				log.log (Level.SEVERE, sql, e);
			}
			finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			m_excludes = new X_M_AttributeSetExclude[list.size ()];
			list.toArray (m_excludes);
		}
		//	Find it
		if (m_excludes != null && m_excludes.length > 0)
		{
			MColumn column = MColumn.get(getCtx(), AD_Column_ID);
			for (X_M_AttributeSetExclude element : m_excludes) {
				if (element.getAD_Table_ID() == column.getAD_Table_ID()
					&& element.isSOTrx() == isSOTrx)
					return true;
			}
		}
		return false;
	}	//	excludeEntry
	
	/**
	 * 	Exclude Lot creation
	 *	@param AD_Column_ID column
	 *	@param isSOTrx SO
	 *	@return true if excluded
	 */
	public boolean isExcludeLot (int AD_Column_ID, boolean isSOTrx)
	{
		if (getM_LotCtl_ID() == 0)
			return true;
		if (m_excludeLots == null)
		{
			ArrayList<X_M_LotCtlExclude> list = new ArrayList<X_M_LotCtlExclude>();
			String sql = "SELECT * FROM M_LotCtlExclude WHERE IsActive='Y' AND M_LotCtl_ID=?";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_Trx());
				pstmt.setInt (1, getM_LotCtl_ID());
				rs = pstmt.executeQuery ();
				while (rs.next ())
					list.add (new X_M_LotCtlExclude (getCtx(), rs, get_Trx()));
			}
			catch (Exception e)
			{
				log.log (Level.SEVERE, sql, e);
			}
			finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			m_excludeLots = new X_M_LotCtlExclude[list.size ()];
			list.toArray (m_excludeLots);
		}
		//	Find it
		if (m_excludeLots != null && m_excludeLots.length > 0)
		{
			MColumn column = MColumn.get(getCtx(), AD_Column_ID);
			for (X_M_LotCtlExclude element : m_excludeLots) {
				if (element.getAD_Table_ID() == column.getAD_Table_ID()
					&& element.isSOTrx() == isSOTrx)
					return true;
			}
		}
		return false;
	}	//	isExcludeLot
	
	/**
	 *	Exclude SerNo creation
	 *	@param AD_Column_ID column
	 *	@param isSOTrx SO
	 *	@return true if excluded
	 */
	public boolean isExcludeSerNo (int AD_Column_ID, boolean isSOTrx)
	{
		if (getM_SerNoCtl_ID() == 0)
			return true;
		if (m_excludeSerNos == null)
		{
			ArrayList<X_M_SerNoCtlExclude> list = new ArrayList<X_M_SerNoCtlExclude>();
			String sql = "SELECT * FROM M_SerNoCtlExclude WHERE IsActive='Y' AND M_SerNoCtl_ID=?";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_Trx());
				pstmt.setInt (1, getM_SerNoCtl_ID());
				rs = pstmt.executeQuery ();
				while (rs.next ())
					list.add (new X_M_SerNoCtlExclude (getCtx(), rs, get_Trx()));
			}
			catch (Exception e)
			{
				log.log (Level.SEVERE, sql, e);
			}
			finally
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			m_excludeSerNos = new X_M_SerNoCtlExclude[list.size ()];
			list.toArray (m_excludeSerNos);
		}
		//	Find it
		if (m_excludeSerNos != null && m_excludeSerNos.length > 0)
		{
			MColumn column = MColumn.get(getCtx(), AD_Column_ID);
			for (X_M_SerNoCtlExclude element : m_excludeSerNos) {
				if (element.getAD_Table_ID() == column.getAD_Table_ID()
					&& element.isSOTrx() == isSOTrx)
					return true;
			}
		}
		return false;
	}	//	isExcludeSerNo

	/**
	 * 	Get Lot Char Start
	 *	@return defined or \u00ab 
	 */
	public String getLotCharStart()
	{
		String s = super.getLotCharSOverwrite ();
		if (s != null && s.length() == 1 && !s.equals(" "))
			return s;
		return "\u00ab";
	}	//	getLotCharStart

	/**
	 * 	Get Lot Char End
	 *	@return defined or \u00bb 
	 */
	public String getLotCharEnd()
	{
		String s = super.getLotCharEOverwrite ();
		if (s != null && s.length() == 1 && !s.equals(" "))
			return s;
		return "\u00bb";
	}	//	getLotCharEnd
	
	/**
	 * 	Get SerNo Char Start
	 *	@return defined or #
	 */
	public String getSerNoCharStart()
	{
		String s = super.getSerNoCharSOverwrite ();
		if (s != null && s.length() == 1 && !s.equals(" "))
			return s;
		return "#";
	}	//	getSerNoCharStart

	/**
	 * 	Get SerNo Char End
	 *	@return defined or none
	 */
	public String getSerNoCharEnd()
	{
		String s = super.getSerNoCharEOverwrite ();
		if (s != null && s.length() == 1 && !s.equals(" "))
			return s;
		return "";
	}	//	getSerNoCharEnd
	
	
	/**
	 * 	Before Save.
	 * 	- set instance attribute flag
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (!isInstanceAttribute()
			&& (isSerNo() || isLot() || isGuaranteeDate()) )
			setIsInstanceAttribute(true);
		return true;
	}	//	beforeSave
	
	
	/**
	 * 	After Save.
	 * 	- Verify Instance Attribute
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Set Instance Attribute
		if (!isInstanceAttribute())
		{
			String sql = "UPDATE M_AttributeSet mas"
				+ " SET IsInstanceAttribute='Y' "
				+ "WHERE M_AttributeSet_ID= ? "
				+ " AND IsInstanceAttribute='N'"
				+ " AND (IsSerNo='Y' OR IsLot='Y' OR IsGuaranteeDate='Y'"
					+ " OR EXISTS (SELECT * FROM M_AttributeUse mau"
						+ " INNER JOIN M_Attribute ma ON (mau.M_Attribute_ID=ma.M_Attribute_ID) "
						+ "WHERE mau.M_AttributeSet_ID=mas.M_AttributeSet_ID"
						+ " AND mau.IsActive='Y' AND ma.IsActive='Y'"
						+ " AND ma.IsInstanceAttribute='Y')"
						+ ")";
			int no = DB.executeUpdate(get_Trx(), sql,getM_AttributeSet_ID());
			if (no != 0)
			{
				log.warning("Set Instance Attribute");
				setIsInstanceAttribute(true);
			}
		}
		//	Reset Instance Attribute
		if (isInstanceAttribute() && !isSerNo() && !isLot() && !isGuaranteeDate())
		{
			String sql = "UPDATE M_AttributeSet mas"
				+ " SET IsInstanceAttribute='N' "
				+ "WHERE M_AttributeSet_ID=? " 
				+ " AND IsInstanceAttribute='Y'"
				+ "	AND IsSerNo='N' AND IsLot='N' AND IsGuaranteeDate='N'"
				+ " AND NOT EXISTS (SELECT * FROM M_AttributeUse mau"
					+ " INNER JOIN M_Attribute ma ON (mau.M_Attribute_ID=ma.M_Attribute_ID) "
					+ "WHERE mau.M_AttributeSet_ID=mas.M_AttributeSet_ID"
					+ " AND mau.IsActive='Y' AND ma.IsActive='Y'"
					+ " AND ma.IsInstanceAttribute='Y')";
			int no = DB.executeUpdate(get_Trx(), sql,getM_AttributeSet_ID());
			if (no != 0)
			{
				log.warning("Reset Instance Attribute");
				setIsInstanceAttribute(false);
			}
		}
		return success;
	}	//	afterSave
	
}	//	MAttributeSet
