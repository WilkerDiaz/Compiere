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

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.api.UICallout;
import org.compiere.framework.*;
import org.compiere.util.*;

/**
 * 	Product Model
 *
 *	@author Jorg Janke
 *	@version $Id: MProduct.java,v 1.5 2006/07/30 00:51:05 jjanke Exp $
 */
public class MProduct extends X_M_Product
{
    /** Logger for class MProduct */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MProduct.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get MProduct from Cache
	 *	@param ctx context
	 *	@param M_Product_ID id
	 *	@return MProduct
	 */
	public static MProduct get (Ctx ctx, int M_Product_ID)
	{
		Integer key = Integer.valueOf (M_Product_ID);
		MProduct retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MProduct (ctx, M_Product_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get MProduct from Cache
	 *	@param ctx context
	 *	@param whereClause sql where clause
	 *	@param trx p_trx
	 *	@return list of MProduct
	 */
	public static ArrayList<MProduct> findAll (Ctx ctx, String whereClause, Trx trx)
	{
		String sql = "SELECT * FROM M_Product WHERE AD_Client_ID=? ";
		if (whereClause != null && whereClause.length() > 0)
			sql += " AND " + whereClause;
		ArrayList<MProduct> list = new ArrayList<MProduct>();
		int AD_Client_ID = ctx.getAD_Client_ID();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt(1, AD_Client_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MProduct (ctx, rs, trx));
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
		return list;
	}	//	get
	
	/**
	 * 	Get Trial Products for Entity Type
	 *	@param ctx ctx
	 *	@param entityType entity type
	 *	@return trial product or null
	 */
	public static MProduct getTrial(Ctx ctx, String entityType)
	{
		if (Util.isEmpty(entityType))
		{
			s_log.warning("No Entity Type");
			return null;
		}
		MProduct retValue = null;
		String sql = "SELECT * FROM M_Product "
			+ "WHERE LicenseInfo LIKE ? AND TrialPhaseDays > 0 AND IsActive='Y' ORDER BY TrialPhaseDays DESC";
		String entityTypeLike = "%" + entityType + "%";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setString(1, entityTypeLike);
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = new MProduct(ctx, rs, null);
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
		if (retValue != null && retValue.getAD_Client_ID() != ctx.getAD_Client_ID())
			s_log.warning("ProductClient_ID=" + retValue.getAD_Client_ID() + " <> EnvClient_ID=" + ctx.getAD_Client_ID());
		if (retValue != null && retValue.getA_Asset_Group_ID() == 0)
		{
			s_log.warning("Product has no Asset Group - " + retValue);
			return null;
		}
		if (retValue == null)
			s_log.warning("No Product for EntityType - " + entityType);
		return retValue;
	}	//	getTrial
	
	
	/**
	 * 	Is Product Stocked
	 * 	@param ctx context
	 *	@param M_Product_ID id
	 *	@return true if found and stocked - false otherwise
	 */
	public static boolean isProductStocked (Ctx ctx, int M_Product_ID)
	{
		MProduct product = get (ctx, M_Product_ID);
		return product.isStocked();
	}	//	isProductStocked
	
	/**	Cache						*/
	private static final CCache<Integer,MProduct>	s_cache	= new CCache<Integer,MProduct>("M_Product", 40, 5);	//	5 minutes
	/**	Static Logger	*/
	private static final CLogger	s_log	= CLogger.getCLogger (MProduct.class);
	/** Link to import Product record	*/
	private transient int I_Product_ID = 0;
	/**	Indicates if product is created or existing for Import Product	*/
	private transient boolean isNew = true;
	/** Indicates if product category is changed */
	private transient boolean isCategoryChanged = false;

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_Product_ID id
	 *	@param trx transaction
	 */
	public MProduct (Ctx ctx, int M_Product_ID, Trx trx)
	{
		super (ctx, M_Product_ID, trx);
		if (M_Product_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
		//	setM_Product_Category_ID (0);
		//	setC_TaxCategory_ID (0);
		//	setC_UOM_ID (0);
		//
			setProductType (PRODUCTTYPE_Item);	// I
			setIsBOM (false);	// N
			setIsInvoicePrintDetails (false);
			setIsPickListPrintDetails (false);
			setIsPurchased (true);	// Y
			setIsSold (true);	// Y
			setIsStocked (true);	// Y
			setIsSummary (false);
			setIsVerified (false);	// N
			setIsWebStoreFeatured (false);
			setIsSelfService(true);
			setIsExcludeAutoDelivery(false);
			setProcessing (false);	// N
			setIsDropShip(false); // N
			setSupportUnits (1);
		}
	}	//	MProduct

	/**
	 * 	Load constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MProduct (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MProduct

	/**
	 * 	Parent Constructor
	 *	@param et parent
	 */
	public MProduct (MExpenseType et)
	{
		this (et.getCtx(), 0, et.get_Trx());
		setProductType(X_M_Product.PRODUCTTYPE_ExpenseType);
		setExpenseType(et);
	}	//	MProduct
	
	/**
	 * 	Parent Constructor
	 *	@param resource parent
	 *	@param resourceType resource type
	 */
	public MProduct (MResource resource, MResourceType resourceType)
	{
		this (resource.getCtx(), 0, resource.get_Trx());
		setProductType(X_M_Product.PRODUCTTYPE_Resource);
		setResourceGroup(X_M_Product.RESOURCEGROUP_Other);
		setResource(resource);
		setResource(resourceType);
	}	//	MProduct

	/**
	 * 	Import Constructor
	 *	@param impP import
	 */
	public MProduct (int M_Product_ID, X_I_Product impP)
	{
		this (impP.getCtx(), M_Product_ID, impP.get_Trx());
		PO.copyValues(impP, this, impP.getAD_Client_ID(), impP.getAD_Org_ID());
		I_Product_ID = impP.getI_Product_ID();
		setNew(M_Product_ID == 0);
	}	//	MProduct
	
	public int getI_Product_ID() {
		return I_Product_ID;
	}
	
	/** Additional Downloads				*/
	private MProductDownload[] m_downloads = null;
	
	/**
	 * 	Set Expense Type
	 *	@param parent expense type
	 *	@return true if changed
	 */
	public boolean setExpenseType (MExpenseType parent)
	{
		boolean changed = false;
		if (!PRODUCTTYPE_ExpenseType.equals(getProductType()))
		{
			setProductType(PRODUCTTYPE_ExpenseType);
			changed = true;
		}
		if (parent.getS_ExpenseType_ID() != getS_ExpenseType_ID())
		{
			setS_ExpenseType_ID(parent.getS_ExpenseType_ID());
			changed = true;
		}
		if (parent.isActive() != isActive())
		{
			setIsActive(parent.isActive());
			changed = true;
		}
		//
		if (!parent.getValue().equals(getValue()))
		{
			setValue(parent.getValue());
			changed = true;
		}
		if (!parent.getName().equals(getName()))
		{
			setName(parent.getName());
			changed = true;
		}
		if ((parent.getDescription() == null && getDescription() != null)
			|| (parent.getDescription() != null && !parent.getDescription().equals(getDescription())))
		{
			setDescription(parent.getDescription());
			changed = true;
		}
		if (parent.getC_UOM_ID() != getC_UOM_ID())
		{
			setC_UOM_ID(parent.getC_UOM_ID());
			changed = true;
		}
		if (parent.getM_Product_Category_ID() != getM_Product_Category_ID())
		{
			setM_Product_Category_ID(parent.getM_Product_Category_ID());
			changed = true;
		}
		if (parent.getC_TaxCategory_ID() != getC_TaxCategory_ID())
		{
			setC_TaxCategory_ID(parent.getC_TaxCategory_ID());
			changed = true;
		}
		//
		return changed;
	}	//	setExpenseType
	
	/**
	 * 	Set Resource
	 *	@param parent resource
	 *	@return true if changed
	 */
	public boolean setResource (MResource parent)
	{
		boolean changed = false;
		if (!PRODUCTTYPE_Resource.equals(getProductType()))
		{
			setProductType(PRODUCTTYPE_Resource);
			changed = true;
		}
		if (parent.getS_Resource_ID() != getS_Resource_ID())
		{
			setS_Resource_ID(parent.getS_Resource_ID());
			changed = true;
		}
		if (parent.isActive() != isActive())
		{
			setIsActive(parent.isActive());
			changed = true;
		}
		//
		if (!parent.getValue().equals(getValue()))
		{
			setValue(parent.getValue());
			changed = true;
		}
		if (!parent.getName().equals(getName()))
		{
			setName(parent.getName());
			changed = true;
		}
		if ((parent.getDescription() == null && getDescription() != null)
			|| (parent.getDescription() != null && !parent.getDescription().equals(getDescription())))
		{
			setDescription(parent.getDescription());
			changed = true;
		}
		//
		return changed;
	}	//	setResource

	/**
	 * 	Set Resource Type
	 *	@param parent resource type
	 *	@return true if changed
	 */
	public boolean setResource (MResourceType parent)
	{
		boolean changed = false;
		if (PRODUCTTYPE_Resource.equals(getProductType()))
		{
			setProductType(PRODUCTTYPE_Resource);
			changed = true;
		}
		//
		if (parent.getC_UOM_ID() != getC_UOM_ID())
		{
			setC_UOM_ID(parent.getC_UOM_ID());
			changed = true;
		}
		if (parent.getM_Product_Category_ID() != getM_Product_Category_ID())
		{
			setM_Product_Category_ID(parent.getM_Product_Category_ID());
			changed = true;
		}
		if (parent.getC_TaxCategory_ID() != getC_TaxCategory_ID())
		{
			setC_TaxCategory_ID(parent.getC_TaxCategory_ID());
			changed = true;
		}
		//
		return changed;
	}	//	setResource
	
	
	/**	UOM Precision			*/
	private Integer		m_precision = null;
	
	/**
	 * 	Get UOM Standard Precision
	 *	@return UOM Standard Precision
	 */
	public int getUOMPrecision()
	{
		if (m_precision == null)
		{
			int C_UOM_ID = getC_UOM_ID();
			if (C_UOM_ID == 0)
				return 0;	//	EA
			m_precision = Integer.valueOf (MUOM.getPrecision(getCtx(), C_UOM_ID));
		}
		return m_precision.intValue();
	}	//	getUOMPrecision
	
	
	/**
	 * 	Create Asset Group for this product
	 *	@return asset group id
	 */
	public int getA_Asset_Group_ID()
	{
		MProductCategory pc = MProductCategory.get(getCtx(), getM_Product_Category_ID());
		return pc.getA_Asset_Group_ID();
	}	//	getA_Asset_Group_ID

	/**
	 * 	Create Asset for this product
	 *	@return true if asset is created
	 */
	public boolean isCreateAsset()
	{
		MProductCategory pc = MProductCategory.get(getCtx(), getM_Product_Category_ID());
		return pc.getA_Asset_Group_ID() != 0;
	}	//	isCreated

	/**
	 * 	Get Attribute Set
	 *	@return set or null
	 */
	public MAttributeSet getAttributeSet()
	{
		if (getM_AttributeSet_ID() != 0)
			return MAttributeSet.get(getCtx(), getM_AttributeSet_ID());
		return null;
	}	//	getAttributeSet
	
	/**
	 * 	Has the Product Instance Attribute
	 *	@return true if instance attributes
	 */
	public boolean isInstanceAttribute()
	{
		if (getM_AttributeSet_ID() == 0)
			return false;
		MAttributeSet mas = MAttributeSet.get(getCtx(), getM_AttributeSet_ID());
		return mas.isInstanceAttribute();
	}	//	isInstanceAttribute
	
	/**
	 * 	Create One Asset Per UOM
	 *	@return individual asset
	 */
	public boolean isOneAssetPerUOM()
	{
		MProductCategory pc = MProductCategory.get(getCtx(), getM_Product_Category_ID());
		if (pc.getA_Asset_Group_ID() == 0)
			return false;
		MAssetGroup ag = MAssetGroup.get(getCtx(), pc.getA_Asset_Group_ID());
		return ag.isOneAssetPerUOM();
	}	//	isOneAssetPerUOM
	
	/**
	 * 	Product is Item
	 *	@return true if item
	 */
	public boolean isItem()
	{
		return PRODUCTTYPE_Item.equals(getProductType());
	}	//	isItem
		
	/**
	 * 	Product is an Item and Stocked
	 *	@return true if stocked and item
	 */
	@Override
	public boolean isStocked ()
	{
		return super.isStocked() && isItem();
	}	//	isStocked
	
	/**
	 * 	Is Service
	 *	@return true if service (resource, online)
	 */
	public boolean isService()
	{
		//	PRODUCTTYPE_Service, PRODUCTTYPE_Resource, PRODUCTTYPE_Online
		return !isItem();	//	
	}	//	isService
	
	/**
	 * 	Get UOM Symbol
	 *	@return UOM Symbol
	 */
	public String getUOMSymbol()
	{
		int C_UOM_ID = getC_UOM_ID();
		if (C_UOM_ID == 0)
			return "";
		return MUOM.get(getCtx(), C_UOM_ID).getUOMSymbol();
	}	//	getUOMSymbol
		
	/**
	 * 	Get Active(!) Product Downloads
	 * 	@param requery requery
	 *	@return array of downloads
	 */
	public MProductDownload[] getProductDownloads (boolean requery)
	{
		if (m_downloads != null && !requery)
			return m_downloads;
		//
		ArrayList<MProductDownload> list = new ArrayList<MProductDownload>();
		String sql = "SELECT * FROM M_ProductDownload "
			+ "WHERE M_Product_ID=? AND IsActive='Y' ORDER BY Name";
		//
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_Trx());
			pstmt.setInt (1, getM_Product_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MProductDownload (getCtx(), rs, get_Trx()));
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
		m_downloads = new MProductDownload[list.size ()];
		list.toArray (m_downloads);
		return m_downloads;
	}	//	getProductDownloads
	
	/**
	 * 	Does the product have downloads
	 *	@return true if downloads exists
	 */
	public boolean hasDownloads()
	{
		getProductDownloads(false);
		return m_downloads != null && m_downloads.length > 0;
	}	//	hasDownloads
	
	/**
	 * 	Get SupportUnits
	 *	@return units per UOM
	 */
	@Override
	public int getSupportUnits()
	{
		int ii = super.getSupportUnits ();
		if (ii < 1)
			ii = 1;
		return ii;
	}	//	getSupportUnits

	/**
	 * 	Set Partner Location - Callout
	 *	@param oldC_BPartner_Location_ID old value
	 *	@param newC_BPartner_Location_ID new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setM_Product_Category_ID (String oldM_Product_Category_ID, 
			String newM_Product_Category_ID, int windowNo) throws Exception
	{
		if (newM_Product_Category_ID == null || newM_Product_Category_ID.length() == 0)
			return;
		int M_Product_Category_ID = Integer.parseInt(newM_Product_Category_ID);
		if (M_Product_Category_ID == 0)
			return;
		//
		super.setM_Product_Category_ID(M_Product_Category_ID);
		MProductCategory pc = new MProductCategory (getCtx(), M_Product_Category_ID, get_Trx());
		setIsPurchasedToOrder(pc.isPurchasedToOrder());
	}	//	setC_BPartner_Location_ID

	
	/**
	 * 	Set Resource Group - Callout
	 * @param oldResourceGroup old value
	 *	@param newResourceGroup new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setResourceGroup (String oldResourceGroup, String newResourceGroup,
			int windowNo) throws Exception {	
		if (newResourceGroup == null || newResourceGroup.length() == 0)
		{
			return;
		}
		if (RESOURCEGROUP_Other.equals(newResourceGroup))
		{
			set_Value ("BasisType", null);
		}
		else
			setBasisType(BASISTYPE_PerItem);
	}	//	setResourceGroup

	
	/**
	 * 	Set Organization - Callout
	 * @param oldAD_Org_ID old value
	 *	@param newAD_Org_ID new value
	 *	@param windowNo window
	 *	@throws Exception
	 */
	@UICallout public void setAD_Org_ID (String oldAD_Org_ID, String newAD_Org_ID,
			int windowNo) throws Exception {	
		if (newAD_Org_ID == null || newAD_Org_ID.length() == 0)
		{
			return;
		}
		int AD_Org_ID = Integer.parseInt(newAD_Org_ID); 
		MLocator defaultLocator = MLocator.getDefaultLocatorOfOrg(getCtx(), AD_Org_ID);
		if(defaultLocator!=null)
			setM_Locator_ID(defaultLocator.get_ID());
	}	//	setAD_Org_ID

	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MProduct[");
		sb.append(get_ID()).append("-").append(getValue())
			.append(",C_UOM_ID=").append(getC_UOM_ID())
			.append("]");
		return sb.toString();
	}	//	toString

	

	
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		//	Check Storage
		if (!newRecord && 	//	
			((is_ValueChanged("IsActive") && !isActive())		//	now not active 
			|| (is_ValueChanged("IsStocked") && !isStocked())	//	now not stocked
			|| (is_ValueChanged("ProductType") 					//	from Item
				&& PRODUCTTYPE_Item.equals(get_ValueOld("ProductType")))))
		{
			BigDecimal OnHand = Storage.getProductQty(getCtx(), get_ID(), X_Ref_Quantity_Type.ON_HAND, get_Trx());
			BigDecimal Ordered = Storage.getProductQty(getCtx(), get_ID(), X_Ref_Quantity_Type.ORDERED, get_Trx());
			BigDecimal Reserved = Storage.getProductQty(getCtx(), get_ID(), X_Ref_Quantity_Type.RESERVED, get_Trx());
			
			String errMsg = "";
			if (OnHand.signum() != 0)
				errMsg = "@QtyOnHand@ = " + OnHand;
			if (Ordered.signum() != 0)
				errMsg += " - @QtyOrdered@ = " + Ordered;
			if (Reserved.signum() != 0)
				errMsg += " - @QtyReserved@" + Reserved;
			if (errMsg.length() > 0)
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), errMsg)); 
				return false;
			}
		}	//	storage
		
		//	Reset Stocked if not Item
		if (isStocked() && !PRODUCTTYPE_Item.equals(getProductType()))
			setIsStocked(false);
		
		//	UOM reset
		if (m_precision != null && is_ValueChanged("C_UOM_ID"))
			m_precision = null;
		
		//	Defaults
		if (getC_UOM_ID() == 0)
			setC_UOM_ID(MUOM.Each_ID);
		if (getM_Product_Category_ID() == 0)
		{
			MProductCategory pc = MProductCategory.getDefault(getCtx());
			setM_Product_Category_ID(pc.getM_Product_Category_ID());
		}
		if (getC_TaxCategory_ID() == 0)
		{
			MTaxCategory tc = MTaxCategory.getDefault(getCtx());
			setC_TaxCategory_ID(tc.getC_TaxCategory_ID());
		}
		
		/* Added the following for Manufacturing and MRP */
		// Cannot update resource group once it is set to Other		
		if (!newRecord 		
			&& is_ValueChanged("ResourceGroup")
			&& (RESOURCEGROUP_Other.equals(get_ValueOld("ResourceGroup"))
				|| RESOURCEGROUP_Other.equals(getResourceGroup())))
			{
				log.saveError("Error", Msg.getMsg(getCtx(), "ResourceGroupOther"));
				return false;
			}

		// Reset resource group to null if product type is no longer Resource 
		if (!newRecord 		
				&& is_ValueChanged("ProductType")
				&& PRODUCTTYPE_Resource.equals(get_ValueOld("ProductType")))
			{
				set_Value ("ResourceGroup", null);
			}

		//	Reset Manufactured if not Item or not Stocked
		if (!isStocked() || !PRODUCTTYPE_Item.equals(getProductType()))
			setIsManufactured(false);

		//	Reset Planned if not Item 
		if (!PRODUCTTYPE_Item.equals(getProductType()))
			setIsPlannedItem(false);
				
		// Reset Planned if neither Purchased or Manufactured
		if (!(isManufactured() || isPurchased()))
			setIsPlannedItem(false);
		
		// Reset Stocked for all Resources - Person, Equipment & Others
		if (PRODUCTTYPE_Resource.equals(getProductType()))
			setIsStocked(false);
		
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
		if (!success)
			return success;
		
		//	Value/Name change in Account
		if (!newRecord && (is_ValueChanged("Value") || is_ValueChanged("Name")))
			MAccount.updateValueDescription(getCtx(), "M_Product_ID= ? ",
					get_Trx(),new Object[]{getM_Product_ID()});
		
		//	Name/Description Change in Asset	MAsset.setValueNameDescription
		if (!newRecord && (is_ValueChanged("Name") || is_ValueChanged("Description")))
		{
			String sql = "UPDATE A_Asset a "
				+ "SET (Name, Description)="
					+ "(SELECT SUBSTR(bp.Name || ' - ' || p.Name,1,60), p.Description "
					+ "FROM M_Product p, C_BPartner bp "
					+ "WHERE p.M_Product_ID=a.M_Product_ID AND bp.C_BPartner_ID=a.C_BPartner_ID) "
				+ "WHERE IsActive='Y'"
				+ "  AND M_Product_ID=?";
			int no = DB.executeUpdate(get_Trx(), sql,getM_Product_ID());
			log.fine("Asset Description updated #" + no);
		}
		
		//	New - Acct, Tree, Old Costing
		if (!getCtx().isBatchMode() && newRecord)
		{
			success = insert_Accounting("M_Product_Acct", "M_Product_Category_Acct",
				"p.M_Product_Category_ID=" + getM_Product_Category_ID());
			//
			MAcctSchema[] mass = MAcctSchema.getClientAcctSchema(getCtx(), getAD_Client_ID(), get_Trx());
			for (MAcctSchema element : mass) {
				//	Old
				MProductCosting pcOld = new MProductCosting(this, element.getC_AcctSchema_ID());
				pcOld.save();
			}
		}
		
		//	New Costing
		if (!getCtx().isBatchMode() && (newRecord || is_ValueChanged("M_Product_Category_ID"))&&(!PRODUCTTYPE_Resource.equals(getProductType())))
			MCost.create(this,null);

		return success;
	}	//	afterSave

	/**
	 * 	Before Delete
	 *	@return true if it can be deleted
	 */
	@Override
	protected boolean beforeDelete ()
	{
		//	Check Storage
		if (isStocked() || PRODUCTTYPE_Item.equals(getProductType()))
		{
			BigDecimal OnHand = Storage.getProductQty(getCtx(), get_ID(), X_Ref_Quantity_Type.ON_HAND, get_Trx());
			BigDecimal Ordered = Storage.getProductQty(getCtx(), get_ID(), X_Ref_Quantity_Type.ORDERED, get_Trx());
			BigDecimal Reserved = Storage.getProductQty(getCtx(), get_ID(), X_Ref_Quantity_Type.RESERVED, get_Trx());
			
			String errMsg = "";
			if (OnHand.signum() != 0)
				errMsg = "@QtyOnHand@ = " + OnHand;
			if (Ordered.signum() != 0)
				errMsg += " - @QtyOrdered@ = " + Ordered;
			if (Reserved.signum() != 0)
				errMsg += " - @QtyReserved@" + Reserved;
			if (errMsg.length() > 0)
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), errMsg)); 
				return false;
			}
			
		}
		//	delete costing
		MProductCosting[] costings = MProductCosting.getOfProduct(getCtx(), get_ID(), get_Trx());
		for (MProductCosting element : costings)
			element.delete(true, get_Trx());

		// delete entries in M_Cost
		DB.executeUpdate(get_Trx(), "DELETE FROM M_Cost WHERE M_Product_ID = ?", get_ID());
		//
		return delete_Accounting("M_Product_Acct"); 
	}	//	beforeDelete

	/**
	 * @param isNew the isNew to set
	 */
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	/**
	 * @return the isNew
	 */
	public boolean isNew() {
		return isNew;
	}

	/**
	 * @param isCategoryChanged the isCategoryChanged to set
	 */
	public void setCategoryChanged(boolean isCategoryChanged) {
		this.isCategoryChanged = isCategoryChanged;
	}

	/**
	 * @return the isCategoryChanged
	 */
	public boolean isCategoryChanged() {
		return isCategoryChanged;
	}
	
}	//	MProduct

