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
package org.compiere.process;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Validate BOM
 *  This process is called from the Verify BOMs process included in the Main Menu 
 *  and by clicking the Verify BOM button in the Product window. 
 *	- Checks that a bom is not recursively included e.g. BOMProduct1 includes BOMProduct2 
 *	  which in turn includes BOMProduct1
 *  - Checks that a bom has at least one BOM component specified
 *  - If called from the Verify BOM button on the Product window, verifies that any included 
 *    BOM products have verified BOMs
 *  @author Jorg Janke
 *  @version $Id: BOMValidate.java,v 1.3 2006/07/30 00:51:01 jjanke Exp $
 */
public class BOMValidate extends SvrProcess
{
	/**	The Product			*/
	private int		p_M_Product_ID = 0;
	/** Product Category	*/
	private int		p_M_Product_Category_ID = 0;
	/** Re-Validate			*/
	private boolean	p_IsReValidate = false;
	/** Where is the BOMValidate process called from
	 *  true  = Called from menu option Verify BOMs 
	 *  false = Called by clicking the Verify button in the Product window */
	private boolean m_CalledFromMenu = true;

	/**	Product				*/
	private MProduct			m_product = null;
	/**	List of Products	*/
	private ArrayList<MProduct>	m_products = null;

	/** Parent BOM existence..  */
	private boolean parentPLVexist = false;
	/** Check Price List     */
	private boolean	p_IsCheckPriceList = false;	
	/** List of PriceListVersions where the BOMProduct is defined */
	private ArrayList<Integer> parentPLV = null;

	/**
	 * 	Prepare
	 */
	@Override
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("M_Product_Category_ID"))
				p_M_Product_Category_ID = element.getParameterAsInt();
			else if (name.equals("IsReValidate"))
				p_IsReValidate = "Y".equals(element.getParameter());
			else if (name.equals("IsCheckPriceList"))
				p_IsCheckPriceList = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_M_Product_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return Info
	 *	@throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		if (p_M_Product_ID != 0)
		{
			m_CalledFromMenu = false;
			log.info("M_Product_ID=" + p_M_Product_ID);
			return validateProduct(new MProduct(getCtx(), p_M_Product_ID, get_Trx()));
		}
		log.info("M_Product_Category_ID=" + p_M_Product_Category_ID
				+ ", IsReValidate=" + p_IsReValidate + ", IsCheckPriceList=" + p_IsCheckPriceList );
		//
		int counter = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM M_Product "
			+ "WHERE IsBOM='Y' AND ";
		if (p_M_Product_Category_ID == 0)
			sql += "AD_Client_ID=? ";
		else
			sql += "M_Product_Category_ID=? ";
		if (!p_IsReValidate)
			sql += "AND IsVerified<>'Y' ";
		sql += "ORDER BY Name";
		int AD_Client_ID = getCtx().getAD_Client_ID();
		try
		{
			pstmt = DB.prepareStatement(sql,  get_Trx());
			if (p_M_Product_Category_ID == 0)
				pstmt.setInt (1, AD_Client_ID);
			else
				pstmt.setInt(1, p_M_Product_Category_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				String info = validateProduct(new MProduct(getCtx(), rs, get_Trx()));
				addLog(0, null, null, info);
				counter++;
			}
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
		return "#" + counter;
	}	//	doIt



	/**
	 * 	Validate Product
	 *	@param product product
	 *	@return Info
	 */
	private String validateProduct (MProduct product)
	{
		m_product = product;

		if (!m_product.isBOM())
		{				
			m_product.setIsVerified(false);
			m_product.save();
			return m_product.getName() + " @NotValid@";
		}

		/** Price list versions where the BOMproduct is included*/ 
		parentPLV = new ArrayList<Integer>();
		parentPLVexist = false;
		if(p_IsCheckPriceList)
		{       
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = "SELECT M_Pricelist_Version_ID FROM M_ProductPrice WHERE M_Product_ID=? AND IsActive = 'Y'";
			try
			{
				pstmt = DB.prepareStatement(sql,get_Trx());
				pstmt.setInt(1, m_product.get_ID());
				rs = pstmt.executeQuery ();
				while (rs.next ())
					parentPLV.add(Integer.valueOf(rs.getInt(1)));

				if (parentPLV.size() > 0)
					parentPLVexist = true;
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
		}/** End of finding PLV's for the BOMProduct */


		MBOM[] boms = MBOM.getOfProduct(getCtx(), m_product.getM_Product_ID(), get_Trx(), " IsActive = 'Y' ");
		if (boms.length == 0)
		{
			log.warning (m_product.getName() + ": "  + "Does not have any active BOMs.");
			m_product.setIsVerified(false);
			m_product.save();
			return m_product.getName() + " @NotValid@";
		}

		for (int i = 0; i < boms.length; i++)
		{
			if (!validateBOM(boms[i]))
			{
				m_product.setIsVerified(false);
				m_product.save();
				return m_product.getName() + " " + boms[i].getName() + " @NotValid@";
			}
		}

		//	OK
		m_product.setIsVerified(true);
		m_product.save();
		return m_product.getName() + " @IsValid@";
	}	//	validateProduct

	/**
	 * 	Validate BOM
	 *	@param bom bom
	 *	@return true if valid
	 */
	private boolean validateBOM (MBOM bom)
	{
		MBOMProduct[] BOMproducts = MBOMProduct.getOfBOM(bom);
		if (BOMproducts.length == 0)
		{
			log.warning (m_product.getName() + ": "  + "Does not have any active BOM components for one of its BOMs.");
			return false;
		}
		boolean retvalue = true;
		for (MBOMProduct BOMproduct : BOMproducts) {
			
			//If the BOM item has IsPlannedItem enabled then each of its components should have either 
			//IsManufactured or IsPurchased enabled if it is of ProductType Item 
			if(m_product.isPlannedItem())
			{
				MProduct componentProduct = new MProduct(getCtx(), BOMproduct.getM_ProductBOM_ID(), null);	
				if(componentProduct.getProductType().equals(X_M_Product.PRODUCTTYPE_Item))								
					if(!(componentProduct.isManufactured() || (componentProduct.isPurchased())))
					{
						log.warning (m_product.getName() + ": " + componentProduct.getName() + 
						" is not manufactured or purchased but is included in a planned item.");
						return false;
					}				 
			}// End of verification for a PlannedItem
			m_products = new ArrayList<MProduct>();
			m_products.add(m_product);
			MProduct pp = new MProduct(getCtx(), BOMproduct.getM_ProductBOM_ID(), get_Trx());
			if (pp.isBOM())
				retvalue = validateProduct(pp, BOMproduct.getComponentBOM(), bom.getBOMType(), bom.getBOMUse());
			if(parentPLVexist)
				retvalue &= checkPLV(pp);
			if (!(retvalue))
				return false;
		}
		if (!(retvalue))
			return false;
		return true;
	}	//	validateBOM

	/**
	 * 	Validate Product
	 *	@param product product
	 *  @param componentBOM bom
	 *	@param BOMType type
	 *	@param BOMUse use
	 *	@return true if valid
	 */
	private boolean validateProduct (MProduct product, MBOM componentBOM, String BOMType, String BOMUse)
	{
		
		if (!product.isBOM())
			return true;

		if (m_products.contains(product))
		{
			log.warning (m_product.getName() + ": " + product.getName() + " is recursively included.");
			return false;
		}

		/** If BOMValidate is being run for a single product, set validation to false
		 *  if any of the included BOMS are not verified. We cannot do this for the
		 *  Verify BOMs process because we cannot control the order in which the BOMs
		 *  will be processed.
		 */
		if (!m_CalledFromMenu && !product.isVerified())
		{
			log.warning (m_product.getName() + ": " + product.getName() + " does not have a valid BOM. Try verifying its BOM first.");
			return false;
		}

		m_products.add(product);
		log.fine(product.getName());

		MBOM bom = null;
		// The included component is a BOM component but no Component BOM was 
		// specified in the BOM line. In this case only validate that BOM of 
		// the included component having the same BOM Type and Use as the parent product.
		if (componentBOM == null)
		{
			String restriction = "BOMType='" + BOMType + "' AND BOMUse='" + BOMUse + "'" + " AND IsActive = 'Y' ";
			MBOM[] boms = MBOM.getOfProduct(getCtx(), product.getM_Product_ID(), get_Trx(),
					restriction);
			if (boms.length != 1)
			{
				log.warning("Component (M_Product_ID) " + product.getM_Product_ID() + ", " + 
						restriction + " - Length=" + boms.length);
				return false;
			}
			bom = boms[0];
		}
		// A Component BOM was specified for the included component. 
		// Hence only validate that component BOM.
		else
		{
			bom = componentBOM;
		}
		MBOMProduct[] BOMproducts = MBOMProduct.getOfBOM(bom);
		if (BOMproducts.length == 0)
		{
			log.warning (m_product.getName() + ": " + product.getName() + " does not have any active BOM components for one of its BOMs.");
			return false;
		}
		boolean retvalue = true;
		for (MBOMProduct BOMproduct : BOMproducts) {
			MProduct pp = new MProduct(getCtx(), BOMproduct.getM_ProductBOM_ID(), get_Trx());
			//If the BOM item has IsPlannedItem enabled then each of its components should have either 
			//IsManufactured or IsPurchased enabled if it is of ProductType Item 
			if(product.isPlannedItem())
			{
				MProduct componentProduct = new MProduct(getCtx(), BOMproduct.getM_ProductBOM_ID(), null);	
				if(componentProduct.getProductType().equals(X_M_Product.PRODUCTTYPE_Item))								
					if(!(componentProduct.isManufactured() || (componentProduct.isPurchased())))
					{
						log.warning (product.getName() + "(BOM Product) : " + componentProduct.getName() + 
						" is not manufactured or purchased but is included in a planned item.");
						return false;
					}				 
			}// End of verification for a PlannedItem
			if (pp.isBOM())
				retvalue = validateProduct(pp, BOMproduct.getComponentBOM(), bom.getBOMType(), bom.getBOMUse());
			if(parentPLVexist)
				retvalue &= checkPLV(pp);
			if (!(retvalue))
				return false;
		}
		if (!(retvalue))
			return false;

		return true;			
	}	//	validateProduct

	/**When the process is executed for a BOM product,
	 * it checks whether the BOM components are included in the price list versions where the BOMProduct is included. 
	 */
	private boolean checkPLV(MProduct prod)
	{
		ArrayList<Integer> childPLV = new ArrayList<Integer>();
		PreparedStatement pstmt = null;
		String sql = "SELECT M_Pricelist_Version_ID FROM M_ProductPrice WHERE M_Product_ID=? AND IsActive = 'Y'";
		ResultSet rs = null;
		boolean retVal = true;
		try
		{
			pstmt = DB.prepareStatement(sql, get_Trx());
			pstmt.setInt(1, prod.get_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				childPLV.add(Integer.valueOf(rs.getInt(1)));

			if (childPLV.size() == 0)
			{
				log.warning (prod.getName() + ": "  + "Is not included in any Price List");
				retVal = false;	
			}				
			if (!( (childPLV.size() >= parentPLV.size()) && (childPLV.containsAll(parentPLV)) ))
			{
				log.warning (prod.getName() + ": "  + "Is not included in all the parent BOM product Price List Versions");
				retVal = false;	
			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return retVal;
	}
	/**  End of verification of Price List Versions	 */


}	//	BOMValidate