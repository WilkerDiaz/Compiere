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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.compiere.Compiere;
import org.compiere.common.CompiereStateException;
import org.compiere.framework.PO;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MCost;
import org.compiere.model.MPInstance;
import org.compiere.model.MProduct;
import org.compiere.model.MProductCosting;
import org.compiere.model.MProductPO;
import org.compiere.model.MProductPrice;
import org.compiere.model.MTaxCategory;
import org.compiere.model.X_I_Product;
import org.compiere.util.CLogMgt;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.Login;

/**
 *	Import Products from I_Product
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ImportProduct.java,v 1.3 2006/07/30 00:51:01 jjanke Exp $
 */
public class ImportProduct extends SvrProcess
{
	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;

	/** Effective						*/
	private Timestamp		m_DateValue = null;
	/** Pricelist to Update				*/
	private int 			p_M_PriceList_Version_ID = 0;

	private static final String STD_CLIENT_CHECK = " AND AD_Client_ID=? " ;	

	private static final boolean TESTMODE = false;
	/** Commit every 100 entities	*/
	private static final int	COMMITCOUNT = TESTMODE?100:Integer.parseInt(Ini.getProperty(Ini.P_IMPORT_BATCH_SIZE));

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) 
		{
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("AD_Client_ID"))
				m_AD_Client_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				m_deleteOldImported = "Y".equals(element.getParameter());
			else if (name.equals("M_PriceList_Version_ID"))
				p_M_PriceList_Version_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if (m_DateValue == null)
			m_DateValue = new Timestamp (System.currentTimeMillis());
	}	//	prepare


	/**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception
	 */
	@Override
	protected String doIt() throws java.lang.Exception
	{
		String sql = null;
		int no = 0;

		//	****	Prepare	****

		//	Delete Old Imported
		if (m_deleteOldImported)
		{
			sql = "DELETE FROM I_Product "
				+ "WHERE I_IsImported='Y'"
				+ STD_CLIENT_CHECK;
			no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
			log.info("Delete Old Impored =" + no);
		}

		//	Set Client, Org, IaActive, Created/Updated, 	ProductType
		sql = "UPDATE I_Product "
			+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ?),"
			+ " AD_Org_ID = COALESCE (AD_Org_ID, 0),"
			+ " IsActive = COALESCE (IsActive, 'Y'),"
			+ " Created = COALESCE (Created, SysDate),"
			+ " CreatedBy = COALESCE (CreatedBy, 0),"
			+ " Updated = COALESCE (Updated, SysDate),"
			+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
			+ " ProductType = COALESCE (ProductType, 'I'),"
			+ " I_ErrorMsg = NULL,"
			+ " I_IsImported = 'N' "
			+ "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL";
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		log.info("Reset=" + no);

		//	Set Optional BPartner
		sql = "UPDATE I_Product i "
			+ "SET C_BPartner_ID=(SELECT C_BPartner_ID FROM C_BPartner p"
			+ " WHERE i.BPartner_Value=p.Value AND i.AD_Client_ID=p.AD_Client_ID) "
			+ "WHERE C_BPartner_ID IS NULL"
			+ " AND I_IsImported<>'Y'"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		log.info("BPartner=" + no);
		//
		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  //java bug, it could not be used directly
		sql = "UPDATE I_Product i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid BPartner,' "
			+ "WHERE i.BPartner_Value is NOT NULL AND i.C_BPartner_ID IS NULL"
			+ " AND i.I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		if (no != 0)
			log.warning("Invalid BPartner=" + no);


		//	****	Find Product
		//	EAN/UPC
		sql = "UPDATE I_Product i "
			+ "SET M_Product_ID=(SELECT M_Product_ID FROM M_Product p"
			+ " WHERE i.UPC=p.UPC AND i.AD_Client_ID=p.AD_Client_ID) "
			+ "WHERE M_Product_ID IS NULL"
			+ " AND I_IsImported='N'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		log.info("Product Existing UPC=" + no);

		//	Value
		sql = "UPDATE I_Product i "
			+ "SET M_Product_ID=(SELECT M_Product_ID FROM M_Product p"
			+ " WHERE i.Value=p.Value AND i.AD_Client_ID=p.AD_Client_ID) "
			+ "WHERE M_Product_ID IS NULL"
			+ " AND I_IsImported='N'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		log.info("Product Existing Value=" + no);

		//	BP ProdNo
		sql = "UPDATE I_Product i "
			+ "SET M_Product_ID=(SELECT M_Product_ID FROM M_Product_po p"
			+ " WHERE i.C_BPartner_ID=p.C_BPartner_ID"
			+ " AND i.VendorProductNo=p.VendorProductNo AND i.AD_Client_ID=p.AD_Client_ID) "
			+ "WHERE M_Product_ID IS NULL"
			+ " AND I_IsImported='N'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		log.info("Product Existing Vendor ProductNo=" + no);

		//	Set Product Category
		sql = "UPDATE I_Product "
			+ "SET ProductCategory_Value=(SELECT MAX(Value) FROM M_Product_Category"
			+ " WHERE IsDefault='Y' AND AD_Client_ID=?) "
			+ "WHERE ProductCategory_Value IS NULL AND M_Product_Category_ID IS NULL"
			+ " AND M_Product_ID IS NULL"	//	set category only if product not found 
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID, m_AD_Client_ID});
		log.fine("Set Category Default Value=" + no);
		//
		sql = "UPDATE I_Product i "
			+ "SET M_Product_Category_ID=(SELECT M_Product_Category_ID FROM M_Product_Category c"
			+ " WHERE i.ProductCategory_Value=c.Value AND i.AD_Client_ID=c.AD_Client_ID) "
			+ "WHERE ProductCategory_Value IS NOT NULL AND M_Product_Category_ID IS NULL"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		log.info("Set Category=" + no);


		//	Copy From Product if Import does not have value
		String[] strFields = new String[] {"Value","Name","Description","DocumentNote","Help",
				"UPC","SKU","Classification","ProductType",
				"Discontinued","DiscontinuedBy","ImageURL","DescriptionURL"};
		for (String element : strFields) {
			sql = "UPDATE I_PRODUCT i "
				+ "SET "+ element + " = (SELECT "+ element + " FROM M_Product p"
				+ " WHERE i.M_Product_ID=p.M_Product_ID AND i.AD_Client_ID=p.AD_Client_ID) "
				+ "WHERE M_Product_ID IS NOT NULL"
				+ " AND "+ element + " IS NULL"
				+ " AND I_IsImported='N'" 
				+ STD_CLIENT_CHECK;
			no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
			if (no != 0)
				log.fine(element + " - default from existing Product=" + no);
		}
		String[] numFields = new String[] {"C_UOM_ID","M_Product_Category_ID",
				"Volume","Weight","ShelfWidth","ShelfHeight","ShelfDepth","UnitsPerPallet"};
		for (String element : numFields) {
			sql = "UPDATE I_PRODUCT i "
				+ "SET "+ element + " = (SELECT "+ element + " FROM M_Product p"
				+ " WHERE i.M_Product_ID=p.M_Product_ID AND i.AD_Client_ID=p.AD_Client_ID) "
				+ "WHERE M_Product_ID IS NOT NULL"
				+ " AND ("+ element + " IS NULL OR "+ element + " =0)"
				+ " AND I_IsImported='N'" 
				+ STD_CLIENT_CHECK;
			no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{ m_AD_Client_ID});
			if (no != 0)
				log.fine(element + " default from existing Product=" + no);
		}

		//	Copy From Product_PO if Import does not have value
		String[] strFieldsPO = new String[] {"UPC",
				"PriceEffective","VendorProductNo","VendorCategory","Manufacturer",
				"Discontinued","DiscontinuedBy"};
		for (String element : strFieldsPO) {
			sql = "UPDATE I_PRODUCT i "
				+ "SET "+ element + " = (SELECT "+ element + " FROM M_Product_PO p"
				+ " WHERE i.M_Product_ID=p.M_Product_ID AND i.C_BPartner_ID=p.C_BPartner_ID AND i.AD_Client_ID=p.AD_Client_ID) "
				+ "WHERE M_Product_ID IS NOT NULL AND C_BPartner_ID IS NOT NULL"
				+ " AND "+ element + " IS NULL"
				+ " AND I_IsImported='N'" 
				+ STD_CLIENT_CHECK;
			no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
			if (no != 0)
				log.fine(element + " default from existing Product PO=" + no);
		}
		String[] numFieldsPO = new String[] {"C_UOM_ID","C_Currency_ID",
				"PriceList","PricePO","RoyaltyAmt",
				"Order_Min","Order_Pack","CostPerOrder","DeliveryTime_Promised"};
		for (String element : numFieldsPO) {
			sql = "UPDATE I_PRODUCT i "
				+ "SET "+ element + " = (SELECT "+ element + " FROM M_Product_PO p"
				+ " WHERE i.M_Product_ID=p.M_Product_ID AND i.C_BPartner_ID=p.C_BPartner_ID AND i.AD_Client_ID=p.AD_Client_ID) "
				+ "WHERE M_Product_ID IS NOT NULL AND C_BPartner_ID IS NOT NULL"
				+ " AND ("+ element + " IS NULL OR "+ element + " =0)"
				+ " AND I_IsImported='N'" 
				+ STD_CLIENT_CHECK;
			no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
			if (no != 0)
				log.fine(element + " default from existing Product PO=" + no);
		}

		//	Invalid Category
		sql = "UPDATE I_Product "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid ProdCategorty,' "
			+ "WHERE M_Product_Category_ID IS NULL"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		if (no != 0)
			log.warning("Invalid Category=" + no);


		//	Set UOM (System/own)
		sql = "UPDATE I_Product i "
			+ "SET X12DE355 = "
			+ "(SELECT MAX(X12DE355) FROM C_UOM u WHERE u.IsDefault='Y' AND u.AD_Client_ID IN (0,i.AD_Client_ID)) "
			+ "WHERE X12DE355 IS NULL AND C_UOM_ID IS NULL"
			+ " AND I_IsImported<>'Y'" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		log.fine("Set UOM Default=" + no);
		//
		sql = "UPDATE I_Product i "
			+ "SET C_UOM_ID = (SELECT C_UOM_ID FROM C_UOM u WHERE u.X12DE355=i.X12DE355 AND u.AD_Client_ID IN (0,i.AD_Client_ID)) "
			+ "WHERE C_UOM_ID IS NULL"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		log.info("Set UOM=" + no);
		//
		sql = "UPDATE I_Product "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid UOM, ' "
			+ "WHERE C_UOM_ID IS NULL"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		if (no != 0)
			log.warning("Invalid UOM=" + no);


		//	Set Currency
		sql = "UPDATE I_Product i "
			+ "SET ISO_Code=(SELECT ISO_Code FROM C_Currency c"
			+ " INNER JOIN C_AcctSchema a ON (a.C_Currency_ID=c.C_Currency_ID)"
			+ " INNER JOIN AD_ClientInfo ci ON (a.C_AcctSchema_ID=ci.C_AcctSchema1_ID)"
			+ " WHERE ci.AD_Client_ID=i.AD_Client_ID) "
			+ "WHERE C_Currency_ID IS NULL AND ISO_Code IS NULL"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		log.fine("Set Currency Default=" + no);
		//
		sql = "UPDATE I_Product i "
			+ "SET C_Currency_ID=(SELECT C_Currency_ID FROM C_Currency c"
			+ " WHERE i.ISO_Code=c.ISO_Code AND c.AD_Client_ID IN (0,i.AD_Client_ID)) "
			+ "WHERE C_Currency_ID IS NULL"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		log.info("doIt- Set Currency=" + no);
		//
		sql = "UPDATE I_Product "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Currency,' "
			+ "WHERE C_Currency_ID IS NULL"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		if (no != 0)
			log.warning("Invalid Currency=" + no);

		//	Verify ProductType
		sql = "UPDATE I_Product "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid ProductType,' "
			+ "WHERE ProductType NOT IN ('I','S')"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		if (no != 0)
			log.warning("Invalid ProductType=" + no);

		//	Unique UPC/Value
		sql = "UPDATE I_Product i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Value not unique,' "
			+ "WHERE I_IsImported<>'Y'"
			+ " AND EXISTS (SELECT 1 FROM I_Product ii WHERE i.AD_Client_ID=ii.AD_Client_ID AND i.I_Product_ID<>ii.I_Product_ID AND i.Value=ii.Value) " + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		if (no != 0)
			log.warning("Not Unique Value=" + no);
		//
		sql = "UPDATE I_Product i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=UPC not unique,' "
			+ "WHERE I_IsImported<>'Y'"
			+ " AND EXISTS (SELECT 1 FROM I_Product ii WHERE ii.I_Product_ID<>i.I_Product_ID AND i.AD_Client_ID=ii.AD_Client_ID AND i.UPC=ii.UPC) " + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		if (no != 0)
			log.warning("Not Unique UPC=" + no);

		//	Mandatory Value
		sql = "UPDATE I_Product i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=No Mandatory Value,' "
			+ "WHERE Value IS NULL"
			+ " AND I_IsImported<>'Y'" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		if (no != 0)
			log.warning("No Mandatory Value=" + no);

		sql = "UPDATE I_Product "
			+ "SET VendorProductNo=Value "
			+ "WHERE C_BPartner_ID IS NOT NULL AND VendorProductNo IS NULL"
			+ " AND I_IsImported='N'" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		log.info("VendorProductNo Set to Value=" + no);
		//
		sql = "UPDATE I_Product i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=VendorProductNo not unique,' "
			+ "WHERE I_IsImported<>'Y'"
			+ " AND C_BPartner_ID IS NOT NULL"
			+ " AND EXISTS (SELECT 1 FROM I_Product ii WHERE i.AD_Client_ID=ii.AD_Client_ID AND i.I_Product_ID<>ii.I_Product_ID " 
			+ " AND i.C_BPartner_ID=ii.C_BPartner_ID AND i.VendorProductNo=ii.VendorProductNo) "
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		if (no != 0)
			log.warning("Not Unique VendorProductNo=" + no);

		//	Get Default Tax Category
		int C_TaxCategory_ID = 0;
		MTaxCategory tc = MTaxCategory.getDefault(getCtx());
		if (tc != null)
			C_TaxCategory_ID = tc.getC_TaxCategory_ID();
		log.fine("C_TaxCategory_ID=" + C_TaxCategory_ID);

		commit();

		//	-------------------------------------------------------------------
		int noInsert = 0;
		int noUpdate = 0;
		/**
		 * Changes for uptake of bulk sql update : PO.saveAll
		 * Go through all records
		 */
		log.fine("start separating ... ");
		sql = "SELECT * FROM I_Product WHERE I_IsImported='N' " + STD_CLIENT_CHECK;

		List<MProduct> productsToSave = new ArrayList<MProduct>();
		Map<Integer,X_I_Product> importProductMap = new HashMap<Integer,X_I_Product>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// Loop through records
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			pstmt.setInt(1, m_AD_Client_ID);
			rs = pstmt.executeQuery();
			while(rs.next()){
				X_I_Product imp = new X_I_Product(getCtx(), rs, get_Trx());
				importProductMap.put(imp.getI_Product_ID(), imp);
				int I_Product_ID = imp.getI_Product_ID();
				int M_Product_ID = imp.getM_Product_ID();
				int C_BPartner_ID = imp.getC_BPartner_ID();
				boolean newProduct = M_Product_ID == 0;
				log.fine("I_Product_ID=" + I_Product_ID + ", M_Product_ID=" + M_Product_ID 
						+ ", C_BPartner_ID=" + C_BPartner_ID);
				MProduct product = null;
				if(newProduct) {	// Insert new Product
					product = new MProduct(M_Product_ID, imp);
					product.setC_TaxCategory_ID(C_TaxCategory_ID);
					productsToSave.add(product);
					noInsert++;
				}
				else {
					product = new MProduct(M_Product_ID, imp);
					productsToSave.add(product);
					noUpdate++;
				}
				if(productsToSave.size() > COMMITCOUNT){
					saveProducts(productsToSave, importProductMap);
					productsToSave.clear();
					importProductMap.clear();
				}
			}
			saveProducts(productsToSave, importProductMap);
		}
		catch (Exception e)	{
			log.log(Level.SEVERE, "Locator - " + sql.toString(), e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		//	Set Error to indicator to not imported
		sql = "UPDATE I_Product "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{m_AD_Client_ID});
		addLog (0, null, new BigDecimal (no), "@Errors@");
		addLog (0, null, new BigDecimal (noInsert), "@M_Product_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (noUpdate), "@M_Product_ID@: @Updated@");
		return "";
	}	//	doIt


	private void saveProducts(List<MProduct> productsToSave,
			Map<Integer, X_I_Product> importProductMap) {

		if(productsToSave.isEmpty() || importProductMap.isEmpty())
			return;

		for(MProduct product : productsToSave){
			if(product.is_ValueChanged("M_Product_Category_ID"))
				product.setCategoryChanged(true);
		}

		if(!PO.saveAll(get_Trx(), productsToSave))
			throw new CompiereStateException("Could not save products");

		List<MProductCosting> productCostingToSave = new ArrayList<MProductCosting>();
		List<MCost> costsToSave = new ArrayList<MCost>();
		MAcctSchema[] mas = MAcctSchema.getClientAcctSchema(getCtx(), getAD_Client_ID(), get_Trx());
		List<MProduct> productsNew = new ArrayList<MProduct>();
		List<MProduct> productsChanged = new ArrayList<MProduct>();
		for(MProduct product : productsToSave){
			X_I_Product imp = importProductMap.get(product.getI_Product_ID());
			imp.setM_Product_ID(product.getM_Product_ID());

			if(product.isNew()){
				for(MAcctSchema element : mas){
					//	Old
					MProductCosting pcOld = new MProductCosting(product, element.getC_AcctSchema_ID());
					productCostingToSave.add(pcOld);
				}
				productsNew.add(product);
			}
			else 
				productsChanged.add(product);
				
			if(product.isNew() || product.isCategoryChanged()){
				List<MCost> costs = MCost.getAll(product);
				if(costs!=null && costs.size()>0)
					costsToSave.addAll(MCost.getAll(product));
			}
		}

		if(!PO.saveAll(get_Trx(), costsToSave))
			throw new CompiereStateException("Could not save cost records");

		if(!PO.saveAll(get_Trx(), productCostingToSave))
			throw new CompiereStateException("Could not save product costing");

		//	Bulk Insert Accounting records
		if(productsNew != null && productsNew.size()>0)
			if(!PO.insert_AccountingAll("M_Product_Acct", "M_Product_Category_Acct", "p.M_Product_Category_ID = a.M_Product_Category_ID", new ArrayList<PO>(productsNew)))
				throw new CompiereStateException("Could not insert product accouting");
				
		if(productsChanged != null && productsChanged.size()>0)
			if(!PO.update_AccountingAll("M_Product_Acct", "M_Product_Category_Acct", "p.M_Product_Category_ID = a.M_Product_Category_ID", new ArrayList<PO>(productsChanged)))
				throw new CompiereStateException("Could not update product accounting");

		int noInsertPO = 0;
		List<MProductPO> productPOsToSave = new ArrayList<MProductPO>();
		List<MProductPrice> priceListsToSave = new ArrayList<MProductPrice>();
		List<X_I_Product> importProductsToSave = new ArrayList<X_I_Product>(importProductMap.values());

		for(X_I_Product imp : importProductsToSave){
			int C_BPartner_ID = imp.getC_BPartner_ID();
			int M_Product_ID = imp.getM_Product_ID();
			//	PO Info
			if(C_BPartner_ID != 0){
				//	If Product existed, try to update first
				MProductPO productPO = MProductPO.getOfVendorProduct(getCtx(), C_BPartner_ID, M_Product_ID, get_Trx());
				if(productPO == null){
					productPO = new MProductPO(getCtx(), 0, get_Trx());
					noInsertPO++;
				}
				PO.copyValues(imp, productPO);
				productPO.setC_BPartner_ID(C_BPartner_ID);
				productPO.setM_Product_ID(M_Product_ID);
				productPOsToSave.add(productPO);
			}

			if(p_M_PriceList_Version_ID != 0){
				BigDecimal PriceList = imp.getPriceList();
				BigDecimal PriceStd = imp.getPriceStd();
				BigDecimal PriceLimit = imp.getPriceLimit();

				MProductPrice pp = MProductPrice.get(getCtx(), p_M_PriceList_Version_ID, M_Product_ID, get_Trx());
				if(pp == null)
					pp = new MProductPrice(getCtx(), p_M_PriceList_Version_ID, M_Product_ID, get_Trx());
				pp.setPrices(PriceList, PriceStd, PriceLimit);
				priceListsToSave.add(pp);
			}
			imp.setI_IsImported(X_I_Product.I_ISIMPORTED_Yes);
			imp.setProcessed(true);
		}
		if(!PO.saveAll(get_Trx(), productPOsToSave))
			throw new CompiereStateException("Could not save product purchasing");

		if(!PO.saveAll(get_Trx(), priceListsToSave))
			throw new CompiereStateException("Could not save price lists");

		if(!PO.saveAll(get_Trx(), importProductsToSave))
			throw new CompiereStateException("Could not save import product records");

		commit();
	}

	public static void main(String[] args)
	{
		System.setProperty ("PropertyFile", "/home/namitha/Useful/Compiere.properties");
		Compiere.startup(true);
		CLogMgt.setLoggerLevel(Level.INFO, null);
		CLogMgt.setLevel(Level.INFO);
		//	Same Login entries as entered
		Ini.setProperty(Ini.P_UID, "GardenAdmin");
		Ini.setProperty(Ini.P_PWD, "GardenAdmin");
		Ini.setProperty(Ini.P_ROLE, "GardenWorld Admin");
		Ini.setProperty(Ini.P_CLIENT, "GardenWorld");
		Ini.setProperty(Ini.P_ORG, "HQ");
		Ini.setProperty(Ini.P_WAREHOUSE, "HQ Warehouse");
		Ini.setProperty(Ini.P_LANGUAGE, "English");
		Ini.setProperty(Ini.P_IMPORT_BATCH_SIZE, "100");

		Ctx ctx = Env.getCtx();
		Login login = new Login(ctx);
		if (!login.batchLogin(null, null))
			System.exit(1);

		//	Reduce Log level for performance
		CLogMgt.setLoggerLevel(Level.WARNING, null);
		CLogMgt.setLevel(Level.WARNING);

		//	Data from Login Context
		int AD_Client_ID = ctx.getAD_Client_ID();
		int AD_User_ID = ctx.getAD_User_ID();
		//	Hardcoded
		int AD_Process_ID = 196;
		int AD_Table_ID = 0;
		int Record_ID = 0;

		//	Step 1: Setup Process
		MPInstance instance = new MPInstance(Env.getCtx(), AD_Process_ID, Record_ID);
		instance.save();

		ProcessInfo pi = new ProcessInfo("Import", AD_Process_ID, AD_Table_ID, Record_ID);
		pi.setAD_Client_ID(AD_Client_ID);
		pi.setAD_User_ID(AD_User_ID);
		pi.setIsBatch(false);  //  want to wait for result
		pi.setAD_PInstance_ID (instance.getAD_PInstance_ID());

		DB.startLoggingUpdates();

		// Step 3: Run the process directly
		ImportProduct test = new ImportProduct();
		test.m_AD_Client_ID = ctx.getAD_Client_ID();
		test.m_deleteOldImported = true;

		long start = System.currentTimeMillis();

		test.startProcess(ctx, pi, null);

		long end = System.currentTimeMillis();
		long durationMS = end - start;
		long duration = durationMS/1000;
		System.out.println("Total: " + duration + "s");

		// Step 4: get results
		if (pi.isError())
			System.err.println("Error: " + pi.getSummary());
		else
			System.out.println("OK: " + pi.getSummary());
		System.out.println(pi.getLogInfo());

		// stop logging database updates
		String logResult = DB.stopLoggingUpdates(0);
		System.out.println(logResult);

	}
}	//	ImportProduct
