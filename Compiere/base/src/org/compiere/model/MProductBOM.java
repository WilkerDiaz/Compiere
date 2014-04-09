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
 *	Product BOM Model (old).
 *	M_Product_ID = the parent 
 *	M_Product_BOM_ID = the BOM line
 *	M_ProductBOM_ID = the BOM line product
 *	
 *  @author Jorg Janke
 *  @version $Id: MProductBOM.java,v 1.5 2006/07/30 00:51:02 jjanke Exp $
 */
public class MProductBOM extends X_M_Product_BOM
{
    /** Logger for class MProductBOM */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MProductBOM.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get BOM Lines for Product
	 *	@param product product
	 *	@return array of BOMs
	 */
	public static MProductBOM[] getBOMLines (MProduct product)
	{
		return getBOMLines(product.getCtx(), product.getM_Product_ID(), product.get_Trx());
	}	//	getBOMLines
	
	/**
	 * 	Get BOM Lines for Product
	 * 	@param ctx context
	 *	@param M_Product_ID product
	 *	@param trx transaction
	 *	@return array of BOMs
	 */
	public static MProductBOM[] getBOMLines (Ctx ctx, int M_Product_ID, Trx trx)
	{
		String sql = "SELECT * FROM M_Product_BOM WHERE M_Product_ID=? ORDER BY Line";
		ArrayList<MProductBOM> list = new ArrayList<MProductBOM>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt(1, M_Product_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MProductBOM (ctx, rs, trx));
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
		MProductBOM[] retValue = new MProductBOM[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getBOMLines

	/** Static Logger					*/
	private static CLogger s_log = CLogger.getCLogger(MProductBOM.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_Product_BOM_ID id
	 *	@param trx transaction
	 */
	public MProductBOM (Ctx ctx, int M_Product_BOM_ID, Trx trx)
	{
		super (ctx, M_Product_BOM_ID, trx);
		if (M_Product_BOM_ID == 0)
		{
		//	setM_Product_ID (0);	//	parent
		//	setLine (0);	// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM M_Product_BOM WHERE M_Product_ID=@M_Product_ID@
		//	setM_ProductBOM_ID(0);
			setBOMQty (Env.ZERO);	// 1
		}
	}	//	MProductBOM

	/**
	 * 	Load Construvtor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MProductBOM (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MProductBOM

	/**	Included Product		*/
	private MProduct m_product = null;


	/**
	 * 	Get BOM Product
	 *	@return product
	 */
	public MProduct getProduct()
	{
		if (m_product == null && getM_ProductBOM_ID() != 0)
			m_product = MProduct.get (getCtx(), getM_ProductBOM_ID());
		return m_product;
	}	//	getProduct

	/**
	 * 	Set included Product
	 *	@param M_ProductBOM_ID product ID
	 */
	@Override
	public void setM_ProductBOM_ID(int M_ProductBOM_ID)
	{
		super.setM_ProductBOM_ID (M_ProductBOM_ID);
		m_product = null;
	}	//	setM_ProductBOM_ID

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MProductBOM[");
		sb.append(get_ID()).append(",Line=").append(getLine())
			.append(",Type=").append(getBOMType()).append(",Qty=").append(getBOMQty());
		if (m_product == null)
			sb.append(",M_Product_ID=").append(getM_ProductBOM_ID());
		else
			sb.append(",").append(m_product);
		sb.append("]");
		return sb.toString();
	}	//	toString

	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Product Line was changed
		if (newRecord || is_ValueChanged("M_ProductBOM_ID"))
		{
			//	Invalidate BOM
			MProduct product = new MProduct (getCtx(), getM_Product_ID(), get_Trx());
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
	
}	//	MProductBOM
