	/**
 * 
 */
package compiere.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MProduct;
import org.compiere.model.MProductPrice;
import org.compiere.model.MTaxCategory;
import org.compiere.model.X_I_Product;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

/**
 * @author aavila
 *
 */
public class ImportProduct extends SvrProcess{
	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;

	/** Effective						*/
	private Timestamp		m_DateValue = null;
	/** Pricelist to Update				*/
	private int 			p_M_PriceList_Version_ID = 0;
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
	}
	@Override
	protected String doIt() throws Exception {
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + m_AD_Client_ID;

		//	****	Prepare	****

		//	Delete Old Imported
		if (m_deleteOldImported)
		{
			sql = new StringBuffer ("DELETE FROM I_Product "
				+ "WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString() );
			log.info("Delete Old Impored =" + no);
		}

		//	Set Client, Org, IaActive, Created/Updated, 	ProductType
		sql = new StringBuffer ("UPDATE I_Product "
			+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(m_AD_Client_ID).append("),"
			+ " AD_Org_ID = COALESCE (AD_Org_ID, 0),"
			+ " IsActive = COALESCE (IsActive, 'Y'),"
			+ " Created = COALESCE (Created, SysDate),"
			+ " CreatedBy = COALESCE (CreatedBy, 0),"
			+ " Updated = COALESCE (Updated, SysDate),"
			+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
			+ " ProductType = COALESCE (ProductType, 'I'),"
			+ " I_ErrorMsg = NULL,"
			+ " I_IsImported = 'N' "
			+ "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		log.info("Reset=" + no);

		//	Set Optional BPartner
		sql = new StringBuffer ("UPDATE I_Product i "
			+ "SET C_BPartner_ID=(SELECT C_BPartner_ID FROM C_BPartner p"
			+ " WHERE i.BPartner_Value=p.Value AND i.AD_Client_ID=p.AD_Client_ID) "
			+ "WHERE C_BPartner_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		log.info("BPartner=" + no);
		//
		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  //java bug, it could not be used directly
		sql = new StringBuffer ("UPDATE I_Product i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid BPartner,' "
			+ "WHERE i.BPartner_Value is NOT NULL AND i.C_BPartner_ID IS NULL"
			+ " AND i.I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		if (no != 0)
			log.warning("Invalid BPartner=" + no);

		//	****	Find Product????????????????????????
		//	EAN/UPC
		sql = new StringBuffer ("UPDATE I_Product i "
			+ "SET M_Product_ID=(SELECT M_Product_ID FROM M_Product p"
			+ " WHERE i.UPC=p.UPC AND i.AD_Client_ID=p.AD_Client_ID) "
			+ "WHERE M_Product_ID IS NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(),sql.toString());
		log.info("Product Existing UPC=" + no);

		//	Value
		sql = new StringBuffer ("UPDATE I_Product i "
			+ "SET M_Product_ID=(SELECT M_Product_ID FROM M_Product p"
			+ " WHERE i.Value=p.Value AND i.AD_Client_ID=p.AD_Client_ID) "
			+ "WHERE M_Product_ID IS NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		log.info("Product Existing Value=" + no);

		//	BP ProdNo
		sql = new StringBuffer ("UPDATE I_Product i "
			+ "SET M_Product_ID=(SELECT M_Product_ID FROM M_Product_po p"
			+ " WHERE i.C_BPartner_ID=p.C_BPartner_ID"
			+ " AND i.VendorProductNo=p.VendorProductNo AND i.AD_Client_ID=p.AD_Client_ID) "
			+ "WHERE M_Product_ID IS NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		log.info("Product Existing Vendor ProductNo=" + no);

		//	Set Product Category
		sql = new StringBuffer ("UPDATE I_Product "
			+ "SET ProductCategory_Value=(SELECT MAX(Value) FROM M_Product_Category"
			+ " WHERE IsDefault='Y' AND AD_Client_ID=").append(m_AD_Client_ID).append(") "
			+ "WHERE ProductCategory_Value IS NULL AND M_Product_Category_ID IS NULL"
			+ " AND M_Product_ID IS NULL"	//	set category only if product not found 
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		log.fine("Set Category Default Value=" + no);
		//
		sql = new StringBuffer ("UPDATE I_Product i "
			+ "SET M_Product_Category_ID=(SELECT M_Product_Category_ID FROM M_Product_Category c"
			+ " WHERE i.ProductCategory_Value=c.Value AND i.AD_Client_ID=c.AD_Client_ID) "
			+ "WHERE ProductCategory_Value IS NOT NULL AND M_Product_Category_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		log.info("Set Category=" + no);
		/**
		 * Promociones
		 * @autor aavila
		 * inserta los M_Product_ID de categoria, departamento, linea, seccion y productos.
		 * */
		
		//Categoria
		sql = new StringBuffer ("UPDATE I_Product i "
				+ "SET XX_Categoria_id=(SELECT M_Product_id FROM M_Product c"
				+ " WHERE i.XX_Categoria_value=c.Value AND XX_TipoProducto=1000000) "
				+ "WHERE XX_Categoria_value IS NOT NULL AND M_Product_ID IS NULL"
				+ " AND I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString());
			log.info("Set Category=" + no);
		//Departemento
		sql = new StringBuffer ("UPDATE I_Product i "
				+ "SET XX_Departamento_id=(SELECT M_Product_id FROM M_Product c"
				+ " WHERE i.XX_Departamento_value=c.Value AND XX_TipoProducto=2000000) "
				+ "WHERE XX_Departamento_value IS NOT NULL AND M_Product_ID IS NULL"
				+ " AND I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString());
			log.info("Set Category=" + no);
		//Linea
		sql = new StringBuffer ("UPDATE I_Product i "
				+ "SET XX_Linea_id=(SELECT M_Product_id FROM M_Product c"
				+ " WHERE i.XX_Linea_value=c.Value AND XX_TipoProducto=3000000) "
				+ "WHERE XX_Linea_value IS NOT NULL AND M_Product_ID IS NULL"
				+ " AND I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString());
			log.info("Set Category=" + no);
		//Seccion
		sql = new StringBuffer ("UPDATE I_Product i "
				+ "SET XX_Seccion_id=(SELECT M_Product_id FROM M_Product c"
				+ " WHERE i.XX_Seccion_value=c.Value AND XX_TipoProducto=4000000) "
				+ "WHERE XX_Seccion_value IS NOT NULL AND M_Product_ID IS NULL"
				+ " AND I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString());
			log.info("Set Category=" + no);
		//Producto
		sql = new StringBuffer ("UPDATE I_Product i "
				+ "SET XX_Producto_id=(SELECT M_Product_id FROM M_Product c"
				+ " WHERE i.XX_Producto_value=c.Value AND XX_TipoProducto=5000000) "
				+ "WHERE XX_Producto_value IS NOT NULL AND M_Product_ID IS NULL"
				+ " AND I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString());
			log.info("Set Category=" + no);
		//Error de referencia
		sql = new StringBuffer ("UPDATE I_Product "
				+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=No se encontro referencia,' "
				+ "WHERE ((xx_categoria_id IS NULL AND XX_TipoProducto<>1000000) or" +
						"(xx_departamento_id IS NULL AND (XX_TipoProducto<>1000000 OR XX_TipoProducto<>2000000)) OR " +
						"(xx_linea_id IS NULL AND (XX_TipoProducto<>1000000 OR XX_TipoProducto<>2000000 OR " +
						"XX_TipoProducto<>3000000)) OR" +
						"(xx_seccion_id IS NULL AND (XX_TipoProducto<>1000000 OR XX_TipoProducto<>2000000 OR " +
						"XX_TipoProducto<>3000000 OR XX_TipoProducto<>4000000)) OR" +
						"(xx_producto_id IS NULL AND (XX_TipoProducto<>1000000 OR XX_TipoProducto<>2000000 OR " +
						"XX_TipoProducto<>3000000 OR XX_TipoProducto<>4000000 OR XX_TipoProducto=5000000))) " +
						"AND I_IsImported<>'Y' AND issummary='N'").append(clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString());
			if (no != 0)
				log.warning("Invalid referencia=" + no);

		
		//	Copy From Product if Import does not have value
			
		String[] strFields = new String[] {"Value","Name","Description","DocumentNote","Help",
			"UPC","SKU","Classification","ProductType",
			"Discontinued","DiscontinuedBy","ImageURL","DescriptionURL"};
		for (String element : strFields) {
			sql = new StringBuffer ("UPDATE I_PRODUCT i "
				+ "SET ").append(element).append(" = (SELECT ").append(element).append(" FROM M_Product p"
				+ " WHERE i.M_Product_ID=p.M_Product_ID AND i.AD_Client_ID=p.AD_Client_ID) "
				+ "WHERE M_Product_ID IS NOT NULL"
				+ " AND ").append(element).append(" IS NULL"
				+ " AND I_IsImported='N'").append(clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString());
			if (no != 0)
				log.fine(element + " - default from existing Product=" + no);
		}
		String[] numFields = new String[] {"C_UOM_ID","M_Product_Category_ID",
			"Volume","Weight","ShelfWidth","ShelfHeight","ShelfDepth","UnitsPerPallet"};
		for (String element : numFields) {
			sql = new StringBuffer ("UPDATE I_PRODUCT i "
				+ "SET ").append(element).append(" = (SELECT ").append(element).append(" FROM M_Product p"
				+ " WHERE i.M_Product_ID=p.M_Product_ID AND i.AD_Client_ID=p.AD_Client_ID) "
				+ "WHERE M_Product_ID IS NOT NULL"
				+ " AND (").append(element).append(" IS NULL OR ").append(element).append("=0)"
				+ " AND I_IsImported='N'").append(clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString());
			if (no != 0)
				log.fine(element + " default from existing Product=" + no);
		}
		
		//	Copy From Product_PO if Import does not have value
		
		String[] strFieldsPO = new String[] {"UPC",
			"PriceEffective","VendorProductNo","VendorCategory","Manufacturer",
			"Discontinued","DiscontinuedBy"};
		for (String element : strFieldsPO) {
			sql = new StringBuffer ("UPDATE I_PRODUCT i "
				+ "SET ").append(element).append(" = (SELECT ").append(element)
				.append(" FROM M_Product_PO p"
				+ " WHERE i.M_Product_ID=p.M_Product_ID AND i.C_BPartner_ID=p.C_BPartner_ID AND i.AD_Client_ID=p.AD_Client_ID) "
				+ "WHERE M_Product_ID IS NOT NULL AND C_BPartner_ID IS NOT NULL"
				+ " AND ").append(element).append(" IS NULL"
				+ " AND I_IsImported='N'").append(clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString());
			if (no != 0)
				log.fine(element + " default from existing Product PO=" + no);
		}
		String[] numFieldsPO = new String[] {"C_UOM_ID","C_Currency_ID",
			"PriceList","PricePO","RoyaltyAmt",
			"Order_Min","Order_Pack","CostPerOrder","DeliveryTime_Promised"};
		for (String element : numFieldsPO) {
			sql = new StringBuffer ("UPDATE I_PRODUCT i "
				+ "SET ").append(element).append(" = (SELECT ").append(element)
				.append(" FROM M_Product_PO p"
				+ " WHERE i.M_Product_ID=p.M_Product_ID AND i.C_BPartner_ID=p.C_BPartner_ID AND i.AD_Client_ID=p.AD_Client_ID) "
				+ "WHERE M_Product_ID IS NOT NULL AND C_BPartner_ID IS NOT NULL"
				+ " AND (").append(element).append(" IS NULL OR ").append(element).append("=0)"
				+ " AND I_IsImported='N'").append(clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString());
			if (no != 0)
				log.fine(element + " default from existing Product PO=" + no);
		}
		

		//	Invalid Category
		sql = new StringBuffer ("UPDATE I_Product "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid ProdCategorty,' "
			+ "WHERE M_Product_Category_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		if (no != 0)
			log.warning("Invalid Category=" + no);
		
		//	Set UOM (System/own)
		sql = new StringBuffer ("UPDATE I_Product i "
			+ "SET X12DE355 = "
			+ "(SELECT MAX(X12DE355) FROM C_UOM u WHERE u.IsDefault='Y' AND u.AD_Client_ID IN (0,i.AD_Client_ID)) "
			+ "WHERE X12DE355 IS NULL AND C_UOM_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		log.fine("Set UOM Default=" + no);
		//
		sql = new StringBuffer ("UPDATE I_Product i "
			+ "SET C_UOM_ID = (SELECT C_UOM_ID FROM C_UOM u WHERE u.X12DE355=i.X12DE355 AND u.AD_Client_ID IN (0,i.AD_Client_ID)) "
			+ "WHERE C_UOM_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		log.info("Set UOM=" + no);
		//
		sql = new StringBuffer ("UPDATE I_Product "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid UOM, ' "
			+ "WHERE C_UOM_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		if (no != 0)
			log.warning("Invalid UOM=" + no);


		//	Set Currency
		sql = new StringBuffer ("UPDATE I_Product i "
			+ "SET ISO_Code=(SELECT ISO_Code FROM C_Currency c"
			+ " INNER JOIN C_AcctSchema a ON (a.C_Currency_ID=c.C_Currency_ID)"
			+ " INNER JOIN AD_ClientInfo ci ON (a.C_AcctSchema_ID=ci.C_AcctSchema1_ID)"
			+ " WHERE ci.AD_Client_ID=i.AD_Client_ID) "
			+ "WHERE C_Currency_ID IS NULL AND ISO_Code IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		log.fine("Set Currency Default=" + no);
		//
		sql = new StringBuffer ("UPDATE I_Product i "
			+ "SET C_Currency_ID=(SELECT C_Currency_ID FROM C_Currency c"
			+ " WHERE i.ISO_Code=c.ISO_Code AND c.AD_Client_ID IN (0,i.AD_Client_ID)) "
			+ "WHERE C_Currency_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		log.info("doIt- Set Currency=" + no);
		//
		sql = new StringBuffer ("UPDATE I_Product "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Currency,' "
			+ "WHERE C_Currency_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		if (no != 0)
			log.warning("Invalid Currency=" + no);

		//	Verify ProductType
		sql = new StringBuffer ("UPDATE I_Product "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid ProductType,' "
			+ "WHERE ProductType NOT IN ('I','S')"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		if (no != 0)
			log.warning("Invalid ProductType=" + no);

		//	Unique UPC/Value
		/*sql = new StringBuffer ("UPDATE I_Product i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Value not unique,' "
			+ "WHERE I_IsImported<>'Y'"
			+ " AND Value IN (SELECT Value FROM I_Product ii WHERE i.AD_Client_ID=ii.AD_Client_ID GROUP BY Value HAVING COUNT(*) > 1)").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning("Not Unique Value=" + no);
		//
		sql = new StringBuffer ("UPDATE I_Product i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=UPC not unique,' "
			+ "WHERE I_IsImported<>'Y'"
			+ " AND UPC IN (SELECT UPC FROM I_Product ii WHERE i.AD_Client_ID=ii.AD_Client_ID GROUP BY UPC HAVING COUNT(*) > 1)").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning("Not Unique UPC=" + no);
		*/
		//	Mandatory Value
		sql = new StringBuffer ("UPDATE I_Product i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=No Mandatory Value,' "
			+ "WHERE Value IS NULL"
			+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		if (no != 0)
			log.warning("No Mandatory Value=" + no);
		
		//	Vendor Product No
		sql = new StringBuffer ("UPDATE I_Product "
			+ "SET VendorProductNo=Value "
			+ "WHERE C_BPartner_ID IS NOT NULL AND VendorProductNo IS NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		log.info("VendorProductNo Set to Value=" + no);
		//
		/*sql = new StringBuffer ("UPDATE I_Product i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=VendorProductNo not unique,' "
			+ "WHERE I_IsImported<>'Y'"
			+ " AND C_BPartner_ID IS NOT NULL"
			+ " AND (C_BPartner_ID, VendorProductNo) IN "
			+ " (SELECT C_BPartner_ID, VendorProductNo FROM I_Product ii WHERE i.AD_Client_ID=ii.AD_Client_ID GROUP BY C_BPartner_ID, VendorProductNo HAVING COUNT(*) > 1)")
			.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning("Not Unique VendorProductNo=" + no);
*/
		//	Get Default Tax Category
		int C_TaxCategory_ID = 0;
		MTaxCategory tc = MTaxCategory.getDefault(getCtx());
		if (tc != null)
			C_TaxCategory_ID = tc.getC_TaxCategory_ID();
		else
		{
			try
			{
				PreparedStatement pstmt = DB.prepareStatement
					("SELECT C_TaxCategory_ID FROM C_TaxCategory WHERE IsDefault='Y'" + clientCheck, get_TrxName());
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
					C_TaxCategory_ID = rs.getInt(1);
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{	
				throw new Exception ("TaxCategory", e);
			}
		}
		log.fine("C_TaxCategory_ID=" + C_TaxCategory_ID);

		commit();
		
		//	-------------------------------------------------------------------
		int noInsert = 0;
		int noUpdate = 0;
		int noInsertPO = 0;
		int noUpdatePO = 0;
		
		//	Go through Records
		log.fine("start inserting/updating ...");
		sql = new StringBuffer ("SELECT * FROM I_Product WHERE I_IsImported='N'")
			.append(clientCheck);
		try
		{
			PreparedStatement pstmt_insertProductPO = DB.prepareStatement
				("INSERT INTO M_Product_PO (M_Product_ID,C_BPartner_ID, "
				+ "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,"
				+ "IsCurrentVendor,C_UOM_ID,C_Currency_ID,UPC,"
				+ "PriceList,PricePO,RoyaltyAmt,PriceEffective,"
				+ "VendorProductNo,VendorCategory,Manufacturer,"
				+ "Discontinued,DiscontinuedBy,Order_Min,Order_Pack,"
				+ "CostPerOrder,DeliveryTime_Promised) "
				+ "SELECT ?,?, "
				+ "AD_Client_ID,AD_Org_ID,'Y',SysDate,CreatedBy,SysDate,UpdatedBy,"
				+ "'Y',C_UOM_ID,C_Currency_ID,UPC,"
				+ "PriceList,PricePO,RoyaltyAmt,PriceEffective,"
				+ "VendorProductNo,VendorCategory,Manufacturer,"
				+ "Discontinued,DiscontinuedBy,Order_Min,Order_Pack,"
				+ "CostPerOrder,DeliveryTime_Promised "
				+ "FROM I_Product "
				+ "WHERE I_Product_ID=?", get_TrxName());

			//	Set Imported = Y
			PreparedStatement pstmt_setImported = DB.prepareStatement
				("UPDATE I_Product SET I_IsImported='Y', M_Product_ID=?, "
				+ "Updated=SysDate, Processed='Y' WHERE I_Product_ID=?", get_TrxName());

			//
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				X_I_Product imp = new X_I_Product(getCtx(), rs, get_TrxName());
				int I_Product_ID = imp.getI_Product_ID();
				int M_Product_ID = imp.getM_Product_ID();
				int C_BPartner_ID = imp.getC_BPartner_ID();
				boolean newProduct = M_Product_ID == 0;
				log.fine("I_Product_ID=" + I_Product_ID + ", M_Product_ID=" + M_Product_ID 
					+ ", C_BPartner_ID=" + C_BPartner_ID);

				//	Product
				if (newProduct)			//	Insert new Product
				{
					MProduct product = new MProduct(getCtx(),M_Product_ID,get_Trx());
					product.setC_TaxCategory_ID(C_TaxCategory_ID);
					if (product.save())
					{
						M_Product_ID = product.getM_Product_ID();
						log.finer("Insert Product");
						noInsert++;
					}
					else
					{
						Exception ex = CLogger.retrieveException();
						String exMsg = ex == null ? "" : ex.toString();
						StringBuffer sql0 = new StringBuffer ("UPDATE I_Product i "
							+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||").append(DB.TO_STRING("Insert Product failed - " + exMsg ))
							.append("WHERE I_Product_ID=").append(I_Product_ID);
						DB.executeUpdate(get_TrxName(), sql0.toString());
						continue;
					}
				}
				else					//	Update Product
				{
					String sqlt = "UPDATE M_PRODUCT "
						+ "SET (Value,Name,Description,DocumentNote,Help,"
						+ "UPC,SKU,C_UOM_ID,M_Product_Category_ID,Classification,ProductType,"
						+ "Volume,Weight,ShelfWidth,ShelfHeight,ShelfDepth,UnitsPerPallet,"
						+ "Discontinued,DiscontinuedBy,Updated,UpdatedBy)= "
						+ "(SELECT Value,Name,Description,DocumentNote,Help,"
						+ "UPC,SKU,C_UOM_ID,M_Product_Category_ID,Classification,ProductType,"
						+ "Volume,Weight,ShelfWidth,ShelfHeight,ShelfDepth,UnitsPerPallet,"
						+ "Discontinued,DiscontinuedBy,SysDate,UpdatedBy"
						+ " FROM I_Product WHERE I_Product_ID="+I_Product_ID+") "
						+ "WHERE M_Product_ID="+M_Product_ID;
					PreparedStatement pstmt_updateProduct = DB.prepareStatement
						(sqlt, get_TrxName());

					//jz pstmt_updateProduct.setInt(1, I_Product_ID);
					//   pstmt_updateProduct.setInt(2, M_Product_ID);
					try
					{
						no = pstmt_updateProduct.executeUpdate();
						log.finer("Update Product = " + no);
						noUpdate++;
					}
					catch (SQLException ex)
					{
						log.warning("Update Product - " + ex.toString());
						StringBuffer sql0 = new StringBuffer ("UPDATE I_Product i "
							+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||").append(DB.TO_STRING("Update Product: " + ex.toString()))
							.append("WHERE I_Product_ID=").append(I_Product_ID);
						DB.executeUpdate(get_TrxName(), sql0.toString());
						continue;
					}
					pstmt_updateProduct.close();
				}

				//	Do we have PO Info
				if (C_BPartner_ID != 0)
				{
					no = 0;
					//	If Product existed, Try to Update first
					if (!newProduct)
					{
						String sqlt = "UPDATE M_Product_PO "
							+ "SET (IsCurrentVendor,C_UOM_ID,C_Currency_ID,UPC,"
							+ "PriceList,PricePO,RoyaltyAmt,PriceEffective,"
							+ "VendorProductNo,VendorCategory,Manufacturer,"
							+ "Discontinued,DiscontinuedBy,Order_Min,Order_Pack,"
							+ "CostPerOrder,DeliveryTime_Promised,Updated,UpdatedBy)= "
							+ "(SELECT CAST('Y' AS CHAR), C_UOM_ID, C_Currency_ID, UPC, "    //jz fix EDB unknown datatype error
							+ "PriceList, PricePO, RoyaltyAmt, PriceEffective, "
							+ "VendorProductNo, VendorCategory, Manufacturer, "
							+ "Discontinued, DiscontinuedBy, Order_Min, Order_Pack, "
							+ "CostPerOrder, DeliveryTime_Promised, SysDate, UpdatedBy"
							+ " FROM I_Product"
							+ " WHERE I_Product_ID="+I_Product_ID+") "
							+ "WHERE M_Product_ID="+M_Product_ID+" AND C_BPartner_ID="+C_BPartner_ID;
						PreparedStatement pstmt_updateProductPO = DB.prepareStatement
							(sqlt, get_TrxName());
						try
						{
							no = pstmt_updateProductPO.executeUpdate();
							log.finer("Update Product_PO = " + no);
							noUpdatePO++;
						}
						catch (SQLException ex)
						{
							log.warning("Update Product_PO - " + ex.toString());
							noUpdate--;
							rollback();
							StringBuffer sql0 = new StringBuffer ("UPDATE I_Product i "
								+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||").append(DB.TO_STRING("Update Product_PO: " + ex.toString()))
								.append("WHERE I_Product_ID=").append(I_Product_ID);
							DB.executeUpdate(get_TrxName(), sql0.toString());
							continue;
						}
						pstmt_updateProductPO.close();
					}
					if (no == 0)		//	Insert PO
					{
						pstmt_insertProductPO.setInt(1, M_Product_ID);
						pstmt_insertProductPO.setInt(2, C_BPartner_ID);
						pstmt_insertProductPO.setInt(3, I_Product_ID);
						try
						{
							no = pstmt_insertProductPO.executeUpdate();
							log.finer("Insert Product_PO = " + no);
							noInsertPO++;
						}
						catch (SQLException ex)
						{
							log.warning("Insert Product_PO - " + ex.toString());
							noInsert--;			//	assume that product also did not exist
							rollback();
							StringBuffer sql0 = new StringBuffer ("UPDATE I_Product i "
								+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||").append(DB.TO_STRING("Insert Product_PO: " + ex.toString()))
								.append("WHERE I_Product_ID=").append(I_Product_ID);
							DB.executeUpdate(get_TrxName(), sql0.toString());
							continue;
						}
					}
				}	//	C_BPartner_ID != 0

				//	Price List
				if (p_M_PriceList_Version_ID != 0)
				{
					BigDecimal PriceList = imp.getPriceList();
					BigDecimal PriceStd = imp.getPriceStd();
					BigDecimal PriceLimit = imp.getPriceLimit();
					if (PriceStd.signum() != 0 && PriceLimit.signum() != 0 && PriceList.signum() != 0)
					{
						MProductPrice pp = MProductPrice.get(getCtx(), 
							p_M_PriceList_Version_ID, M_Product_ID, get_TrxName());
						if (pp == null)
							pp = new MProductPrice (getCtx(), 
								p_M_PriceList_Version_ID, M_Product_ID, get_TrxName());
						pp.setPrices(PriceList, PriceStd, PriceLimit);
						pp.save();
					}
				}
				
				//	Update I_Product
				pstmt_setImported.setInt(1, M_Product_ID);
				pstmt_setImported.setInt(2, I_Product_ID);
				no = pstmt_setImported.executeUpdate();
				//
				commit();
			}	//	for all I_Product
			rs.close();
			pstmt.close();

			pstmt_insertProductPO.close();
			pstmt_setImported.close();
		}
		catch (SQLException e)
		{
		}
		//	Set Error to indicator to not imported
		sql = new StringBuffer ("UPDATE I_Product "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		addLog (0, null, new BigDecimal (no), "@Errors@");
		addLog (0, null, new BigDecimal (noInsert), "@M_Product_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (noUpdate), "@M_Product_ID@: @Updated@");
		addLog (0, null, new BigDecimal (noInsertPO), "@M_Product_ID@ @Purchase@: @Inserted@");
		addLog (0, null, new BigDecimal (noUpdatePO), "@M_Product_ID@ @Purchase@: @Updated@");
		return "";
	}	//	doIt
}
