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
 * 	BOM Product/Component Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MBOMProduct.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class MBOMProduct extends X_M_BOMProduct
{
    /** Logger for class MBOMProduct */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBOMProduct.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Products of BOM
	 *	@param bom bom
	 *	@return array of BOM Products
	 */
	public static MBOMProduct[] getOfBOM (MBOM bom) 
	{
		ArrayList<MBOMProduct> list = new ArrayList<MBOMProduct>();
		String sql = "SELECT * FROM M_BOMProduct WHERE M_BOM_ID=? AND IsActive = 'Y' ORDER BY Line";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, bom.get_Trx());
			pstmt.setInt (1, bom.getM_BOM_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MBOMProduct (bom.getCtx(), rs, bom.get_Trx()));
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		MBOMProduct[] retValue = new MBOMProduct[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getOfProduct


	/**
	 * 	Get BOM Lines for Product. Default to Current Active, Master BOM
	 *	@param product product
	 *	@return array of BOMs
	 */
	public static MBOMProduct[] getBOMLines (MProduct product)
	{
		// return lines for Current Active, Master BOM
		return getBOMLines(product, X_M_BOM.BOMTYPE_CurrentActive, X_M_BOM.BOMUSE_Master);
	}	//	getBOMLines


	/**
	 * 	Get BOM Lines for Product. Default to Current Active, Master BOM
	 *	@param product product
	 *  @param bomType bomtype
	 *  @param bomUse bomuse
	 *	@return array of BOMs
	 */
	public static MBOMProduct[] getBOMLines (MProduct product, String bomType, String bomUse)
	{
		// return lines for Current Active, Master BOM
		String sql = "SELECT M_BOM_ID FROM M_BOM WHERE M_Product_ID=? " +
		"AND BOMType = ? AND BOMUse = ? AND IsActive = 'Y' ";
		Trx trx = product.get_Trx();
		int bomID = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt(1, product.getM_Product_ID());
			pstmt.setString(2, bomType);
			pstmt.setString(3, bomUse);
			rs = pstmt.executeQuery();
			if (rs.next())
				bomID = rs.getInt(1);
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
		return getBOMLines(MBOM.get(product.getCtx(), bomID));
	}	//	getBOMLines

	/**
	 * 	Get BOM Lines for Product. Default to Current Active, Master BOM
	 *  BOM Lines are Ordered By Ascending Order of Product Names.
	 *	@param product product
	 *  @param bomType bomtype
	 *  @param bomUse bomuse
	 *  @param isAscending true if ascending, false if descending
	 *	@return array of BOMs
	 */
	public static MBOMProduct[] getBOMLinesOrderByProductName (MProduct product, String bomType, String bomUse, boolean isAscending)
	{
		// return lines for Current Active, Master BOM
		String sql = "SELECT M_BOM_ID FROM M_BOM WHERE M_Product_ID=? " +
		"AND BOMType = ? AND BOMUse = ? AND IsActive = 'Y' ";
		Trx trx = product.get_Trx();
		int bomID = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt(1, product.getM_Product_ID());
			pstmt.setString(2, bomType);
			pstmt.setString(3, bomUse);
			rs = pstmt.executeQuery();
			if (rs.next())
				bomID = rs.getInt(1);
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

		return getBOMLinesOrderByProductName(MBOM.get(product.getCtx(), bomID), isAscending );
	}	//	getBOMLines

	/**
	 * 	Get BOM Lines for Product given a specific BOM
	 * 	@param bom BOM
	 *	@return array of BOMProducts.
	 */
	public static MBOMProduct[] getBOMLines (MBOM bom)
	{
		String sql = "SELECT * FROM M_BOMProduct WHERE M_BOM_ID=? AND IsActive='Y' ORDER BY Line";
		ArrayList<MBOMProduct> list = new ArrayList<MBOMProduct>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, bom.get_Trx());
			pstmt.setInt(1, bom.getM_BOM_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MBOMProduct (bom.getCtx(), rs, bom.get_Trx()));
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
		MBOMProduct[] retValue = new MBOMProduct[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getBOMLines

	/**
	 * 	Get BOM Lines for Product given a specific BOM
	 *  The result is Ordered By Product Name.
	 * 	@param bom Bom
	 *  @param isAscending true is ascending, false if descending
	 *	@return array of BOMProducts.
	 */
	public static MBOMProduct[] getBOMLinesOrderByProductName (MBOM bom, boolean isAscending)
	{
		String sql = "SELECT * FROM M_BOMProduct WHERE M_BOM_ID=? AND IsActive='Y'";
		if(isAscending)
			sql = sql.concat(" ORDER BY getProductName(M_ProductBOM_ID)" );
		else
			sql = sql.concat(" ORDER BY getProductName(M_ProductBOM_ID) DESC" );
		ArrayList<MBOMProduct> list = new ArrayList<MBOMProduct>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, bom.get_Trx());
			pstmt.setInt(1, bom.getM_BOM_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MBOMProduct (bom.getCtx(), rs, bom.get_Trx()));
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
		//
		MBOMProduct[] retValue = new MBOMProduct[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getBOMLines

	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MBOMProduct.class);

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_BOMProduct_ID id
	 *	@param trx p_trx
	 */
	public MBOMProduct (Ctx ctx, int M_BOMProduct_ID, Trx trx)
	{
		super (ctx, M_BOMProduct_ID, trx);
		if (M_BOMProduct_ID == 0)
		{
			//	setM_BOM_ID (0);
			setBOMProductType (BOMPRODUCTTYPE_StandardProduct);	// S
			setBOMQty (Env.ONE);
			setIsPhantom (false);
			//	setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM M_BOMProduct WHERE M_BOM_ID=@M_BOM_ID@
		}
	}	//	MBOMProduct

	/**
	 * 	Parent Constructor
	 *	@param bom product
	 */
	public MBOMProduct (MBOM bom)
	{
		this (bom.getCtx(), 0, bom.get_Trx());
		m_bom = bom;
		m_component = null;
		m_componentBOM = null;
	}	//	MBOMProduct


	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MBOMProduct (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MBOMProduct

	/**	BOM Parent				*/
	private MBOM		m_bom = null;

	/**
	 * 	Get Parent
	 *	@return parent
	 */
	public MBOM getBOM()
	{
		if (m_bom == null && getM_BOM_ID() != 0)
			m_bom = MBOM.get(getCtx(), getM_BOM_ID());
		return m_bom;
	}	//	getBOM


	/**	Included Component */
	private MProduct m_component = null;

	/**
	 * 	Get included component
	 *	@return product
	 */
	public MProduct getComponent()
	{
		if (m_component == null && getM_ProductBOM_ID() != 0)
			m_component = MProduct.get (getCtx(), getM_ProductBOM_ID());
		return m_component;
	}	//	getComponent

	/**
	 * 	Set component
	 *	@param M_ProductBOM_ID product ID
	 */
	@Override
	public void setM_ProductBOM_ID(int M_ProductBOM_ID)
	{
		super.setM_ProductBOM_ID (M_ProductBOM_ID);
		m_component = null;
	}	//	setM_ProductBOM_ID


	/**	Component BOM				*/
	private MBOM		m_componentBOM = null;

	/**
	 * 	Get Component BOM
	 *	@return MBOM
	 */
	public MBOM getComponentBOM()
	{
		if (m_componentBOM == null && getM_ProductBOMVersion_ID() != 0)
			m_componentBOM = MBOM.get(getCtx(), getM_ProductBOMVersion_ID());
		return m_componentBOM;
	}	//	getComponentBOM


	/**
	 * 	Set component BOM
	 *	@param M_ProductBOM_ID product ID
	 */
	@Override
	public void setM_ProductBOMVersion_ID(int M_ProductBOMVersion_ID)
	{
		super.setM_ProductBOMVersion_ID (M_ProductBOMVersion_ID);
		m_componentBOM = null;
	}	//	setM_ProductBOMVersion_ID

	/** Info
	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MBOMProduct[").append(get_ID()).append(",ComponentProduct=").
		append(getComponent().getName()).append("]");
		return sb.toString();
	}

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true/false
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		//	Product
		if (getBOMProductType().equals(BOMPRODUCTTYPE_OutsideProcessing))
		{
			if (getM_ProductBOM_ID() != 0)
				setM_ProductBOM_ID(0);
		}
		else if (getM_ProductBOM_ID() == 0)
		{
			log.saveError("Error", Msg.parseTranslation(getCtx(), "@NotFound@ @M_ProductBOM_ID@"));
			return false;
		}
		//	Product Attribute Instance
		if (getM_AttributeSetInstance_ID() != 0)
		{
			getBOM();
			if (m_bom != null 
					&& X_M_BOM.BOMTYPE_Make_To_Order.equals(m_bom.getBOMType()))
				;
			else
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), 
				"Reset @M_AttributeSetInstance_ID@: Not Make-to-Order"));
				setM_AttributeSetInstance_ID(0);
				return false;
			}
		}
		//	Alternate
		if ((getBOMProductType().equals(BOMPRODUCTTYPE_Alternative)
				|| getBOMProductType().equals(BOMPRODUCTTYPE_AlternativeDefault)))
		{
			if (getM_BOMAlternative_ID() == 0)
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), "@NotFound@ @M_BOMAlternative_ID@"));
				return false;
			}
		}
		else
		{
			setM_BOMAlternative_ID(0);
		}

		//	Set Line Number
		if (getLine() == 0)
		{
			String sql = "SELECT NVL(MAX(Line),0)+10 FROM M_BOMProduct WHERE M_BOM_ID=?";
			int ii = QueryUtil.getSQLValue (get_Trx(), sql, getM_BOM_ID());
			setLine (ii);
		}

		if ((getBOMQty().compareTo(Env.ZERO)) < 0){
			log.saveError("Error", Msg.parseTranslation(getCtx(), "@Qty@ < 0"));
			return false;
		}

		if(getOperationSeqNo() < 0){
			log.saveError("Error", Msg.parseTranslation(getCtx(), "@OperationSeqNo@ < 0"));
			return false;
		}

		if(getLine() < 0){
			log.saveError("Error", Msg.parseTranslation(getCtx(), "@Line@ < 0"));
			return false;
		}
		return true;
	}	//	beforeSave

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	BOM Component Line was changed
		if (newRecord || is_ValueChanged("M_ProductBOM_ID") || is_ValueChanged("M_ProductBOMVersion_ID") || is_ValueChanged("IsActive"))
		{
			MBOM mbom = new MBOM (getCtx(), getM_BOM_ID(), get_Trx());
			//	Invalidate BOM
			MProduct product = new MProduct (getCtx(), mbom.getM_Product_ID(), get_Trx());
			if (get_Trx() != null)
				product.load(get_Trx());
			if (product.isVerified())
			{
				product.setIsVerified(false);
				product.save(get_Trx());
			}
			//	Invalidate Products where BOM is used

		}
		return success;
	}	//	afterSave

	/** 
	 * After Delete
	 * @param success
	 * @return true
	 */
	@Override
	protected boolean afterDelete(boolean success) {		
		MBOMProduct[] lines = MBOMProduct.getBOMLines(getBOM());
		if(lines == null || 0 == lines.length || (1 == lines.length && lines[0].getM_BOMProduct_ID() == getM_BOMProduct_ID())){
			MProduct product = MProduct.get(getCtx(), m_bom.getM_Product_ID());
			product.setIsVerified(false);
			if(!product.save(get_Trx()))
				return false;
		}
		return true;
	}

}	//	MBOMProduct
