
package compiere.model.cds.processes;

import java.awt.Cursor;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import org.compiere.apps.ADialog;
import org.compiere.apps.form.FormFrame;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

import compiere.model.cds.MOrder;
import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRVendorProdRef;
import compiere.model.cds.MVMR_BudgetSalesDepart;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VMR_DistribProdPerStore;
import compiere.model.cds.X_XX_VMR_DistribProductDetail;
import compiere.model.cds.X_XX_VMR_DistributionDetail;
import compiere.model.cds.X_XX_VMR_PO_DistribDetail;
import compiere.model.cds.X_XX_VMR_StorePercentDistrib;
import compiere.model.cds.X_XX_VMR_StoreQuantityDistrib;
import compiere.model.cds.distribution.XX_DistributionUtilities;

public class XX_PercentualDistributionRedistribution {
	
	
	private boolean allowed_to_process = false;
	private Hashtable<Integer, Hashtable<Integer, Integer> > size_references
					= new Hashtable<Integer, Hashtable<Integer,Integer>>();	
	private int windowNo = 0;
	private FormFrame frame = null;
	private X_XX_VMR_DistributionDetail detail;
	final static int size_attribute = Env.getCtx().getContextAsInt("#XX_L_M_ATTRIBUTESIZE_ID");
	private Trx trans ;

	private void safeDeleteRecords () {				
		if (detail.isXX_DistributionApplied()) {
			allowed_to_process =
				ADialog.ask(windowNo, frame, "XX_SafeDeleteDistribution");			
		} else allowed_to_process = true;		
	}
		
	private boolean deletePreviuosAndSaveNewPerc (			
				Vector<? extends Number> percentages, 
				Vector<Integer> codes) {
			
		if (!allowed_to_process) return false;			
		
		String sql_deleteperc = "DELETE FROM XX_VMR_STOREPERCENTDISTRIB" +
			" WHERE XX_VMR_DISTRIBUTIONDETAIL_ID = " + detail.get_ID();		
		String sql_deleteqtys = "DELETE FROM XX_VMR_STOREQUANTITYDISTRIB" +
		" WHERE XX_VMR_DISTRIBUTIONDETAIL_ID = " + detail.getXX_VMR_DistributionDetail_ID();
		
		try { 				
			//Executing the SQL QUERY	
			DB.executeUpdate(trans,sql_deleteperc);
			DB.executeUpdate(trans,sql_deleteqtys);			
		} catch (Exception e) {
			e.printStackTrace();
			ADialog.error(windowNo, frame, "XX_DatabaseAccessError");
			return false;
		}
		
		for (int iterator = 0; iterator < codes.size() ; iterator++) {	
			X_XX_VMR_StorePercentDistrib store_percentage 
				= new X_XX_VMR_StorePercentDistrib (Env.getCtx(), 0, trans);
			
			store_percentage.setXX_VMR_DistributionDetail_ID(detail.get_ID());
			store_percentage.setM_Warehouse_ID(codes.get(iterator));
			double percentage = percentages.get(iterator).doubleValue();			
			
			if (percentage != 0.0) {
				BigDecimal bd = new BigDecimal(percentage);
			    bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);		
				store_percentage.setXX_Percentage(bd);				
				store_percentage.setXX_VMR_DistributionType_ID(detail.getXX_VMR_DistributionType_ID());
				store_percentage.save();
			}			
		}
		detail.setXX_CalculatedPER(true);
		detail.setXX_CalculatedQTY(false);
		detail.setXX_DistributionApplied(true);
		detail.save(trans);
		return true;
	}
	
	
	private boolean deletePreviuosAndSaveNewQtys (			
			Vector<Integer> qtys, Vector<Integer> codes) {
		
	if (!allowed_to_process) return false;			
	
	String sql_deleteperc = "DELETE FROM XX_VMR_STOREPERCENTDISTRIB" +
		" WHERE XX_VMR_DISTRIBUTIONDETAIL_ID = " + detail.get_ID();		
	String sql_deleteqtys = "DELETE FROM XX_VMR_STOREQUANTITYDISTRIB" +
	" WHERE XX_VMR_DISTRIBUTIONDETAIL_ID = " + detail.get_ID();
	
	PreparedStatement pstmt_deleteperc =
		DB.prepareStatement(sql_deleteperc, trans);
	PreparedStatement pstmt_deleteqtys = 
		DB.prepareStatement(sql_deleteqtys, trans);
	try { 				
		//Executing the SQL QUERY	
		DB.executeUpdate(trans,sql_deleteperc);
		DB.executeUpdate(trans,sql_deleteqtys);
		
	} catch (Exception e) {
		e.printStackTrace();
		ADialog.error(windowNo, frame, "XX_DatabaseAccessError");
		return false;
	}
	
	for (int iterator = 0; iterator < codes.size() ; iterator++) {			
		X_XX_VMR_StoreQuantityDistrib store_qty = 
			new X_XX_VMR_StoreQuantityDistrib(Env.getCtx(), 0, trans);
				
		store_qty.setXX_VMR_DistributionDetail_ID(detail.get_ID());
		store_qty.setM_Warehouse_ID(codes.get(iterator));		
		int percentage = qtys.get(iterator);			
		
		if (percentage != 0) {		
			store_qty.setXX_Quantity(percentage);				
			store_qty.setXX_VMR_DistributionType_ID(detail.getXX_VMR_DistributionType_ID());
			store_qty.save(trans);
		}		
	}
	X_XX_VMR_DistributionDetail det = 
		new X_XX_VMR_DistributionDetail(Env.getCtx(), detail.get_ID(), null);
	det.setXX_CalculatedPER(false);
	det.setXX_CalculatedQTY(true);
	det.setXX_DistributionApplied(true);
	det.save(trans);
	return true; 
	 	
}
		
	/**
	 * Apply the percentual distribution, save the product records  
	 * @param header The Distribution Header to be distributed
	 * @param percentages Vector that holds the information of percentages. This vector has to be non empty
	 * @param warehouses Vector that holds the information of store codes
	 * */	
	private boolean percentualDistribution( 
			Vector<? extends Number> percentages, Vector<Integer> warehouses) {
									
		if (!allowed_to_process) return false;
		
		//Delete any previous distribution
		String SQL_t = 
		" DELETE FROM XX_VMR_DISTRIBPRODPERSTORE WHERE XX_VMR_DISTRIBPRODUCTDETAIL_ID " +
			" IN ( SELECT XX_VMR_DISTRIBPRODUCTDETAIL_ID FROM XX_VMR_DISTRIBPRODUCTDETAIL  " +
			" WHERE XX_VMR_DISTRIBUTIONDETAIL_ID = " + detail.getXX_VMR_DistributionDetail_ID() + ") ";					
		try { 
			//Executing the SQL QUERIES		
			DB.executeUpdate(trans, SQL_t);
		} catch (Exception e) {
			e.printStackTrace();
			ADialog.error(windowNo, frame, "XX_DatabaseAccessError");
			return false;
		}					
			//Prepares the size curve
		prepareSizeCurve();
		
			//Distribution variables
		int total = 0, most = 0; double most_value_percentage = 0;

		//AGREGADO GHUCHET
		/*Vector que contiene los indices que definen la prioridad de las tiendas para agregarle piezas, de mayor a menor*/
		Vector<Integer> priorityToAdd= new Vector<Integer>();
		for (int j = 0 ; j < percentages.size() ; j++) {
			for (int i = 0 ; i < percentages.size() ; i++) {
				if (percentages.elementAt(i).doubleValue() > most_value_percentage && !priorityToAdd.contains(i)) {
					most = i;
					most_value_percentage = percentages.elementAt(i).doubleValue();
				}
			}
			most_value_percentage = 0;
			priorityToAdd.add(most);

		}
		/*Vector que contiene los indices que definen la prioridad de las tiendas para removerle piezas, de mayor a menor*/
		Vector<Integer> priorityToCutDown = new Vector<Integer>(); int less = 0; double less_value_percentage = 100;
		for (int j = 0 ; j < percentages.size() ; j++) {
			for (int i = 0 ; i < percentages.size() ; i++) {
				if (percentages.elementAt(i).doubleValue() < less_value_percentage && !priorityToCutDown.contains(i)) {
					less = i;
					less_value_percentage = percentages.elementAt(i).doubleValue();
				}
			}
			less_value_percentage = 100;
			priorityToCutDown.add(less);
		}
		//FIN GHUCHET
		
		
		//Finding the records of a previous distribution in order to use them
		Hashtable< Integer, Integer> old_product_distrib = new Hashtable<Integer, Integer>();
		
		String sql_previous = " SELECT M_PRODUCT_ID, XX_VMR_DISTRIBPRODUCTDETAIL_ID " +
				" FROM XX_VMR_DISTRIBPRODUCTDETAIL WHERE XX_VMR_DISTRIBUTIONDETAIL_ID = " 
				+ detail.getXX_VMR_DistributionDetail_ID() ;		
		PreparedStatement pstmt_previous = DB.prepareStatement(sql_previous, null);
		try {			
			ResultSet rs_previous = pstmt_previous.executeQuery();
			while (rs_previous.next()) {
				old_product_distrib.put(rs_previous.getInt(1), rs_previous.getInt(2));
			}
			rs_previous.close();
			pstmt_previous.close();
		} catch (SQLException e1) {			
			e1.printStackTrace();
			ADialog.error(windowNo, frame, "XX_DatabaseAccessError");
		}
		
		
			//Distributes each product associated to the purchase order
		String SQL_a = "SELECT M_PRODUCT_ID, XX_DESIREDQUANTITY XX_QTY FROM XX_VMR_DISTRIBDETAILTEMP " +
			" WHERE XX_VMR_DISTRIBUTIONDETAIL_ID = " + detail.get_ID()
			+ " AND M_PRODUCT_ID IS NOT NULL ";
			//Create a transaction name		
		PreparedStatement pstmt_a = DB.prepareStatement(SQL_a, trans);
		ResultSet rs_a = null;
		boolean curve_compliance = true;
		try { 
			rs_a = pstmt_a.executeQuery();				
			int product = 0, xx_quantity = 0, reference = 0;			
							
			//FOR EACH PRODUCT IN THE TEMPORARY TABLE
			while(rs_a.next()) {
				
				product = rs_a.getInt("M_PRODUCT_ID");									
				xx_quantity = rs_a.getInt("XX_QTY");
				
				//Related to the previous distribution values				
				int distrib_prod = 0;
				boolean previous_distribution = false;
				if (old_product_distrib.containsKey(product)) {					
					distrib_prod = old_product_distrib.get(product);
					previous_distribution = true;
				}
				MProduct product_info = new MProduct(Env.getCtx(), product, trans);				
				X_XX_VMR_DistribProductDetail productDet = 
					new X_XX_VMR_DistribProductDetail(Env.getCtx(), distrib_prod ,trans);
				
				//There's something to distribute				
				reference = product_info.getXX_VMR_VendorProdRef_ID();
				if (!previous_distribution) {

					productDet.setXX_VMR_DistributionDetail_ID(detail.get_ID());
					productDet.setM_Product_ID(product);
					productDet.setXX_Quantity(xx_quantity);
					
					XX_BatchNumberInfo.ProductCostOrigin(product, xx_quantity, productDet);
					productDet.setM_AttributeSet_ID(product_info.getM_AttributeSet_ID());
					productDet.setM_AttributeSetInstance_ID(product_info.getM_AttributeSetInstance_ID());
					productDet.setXX_CanSetDefinitive(true);					
				}

				int distributed_quantity = 0, curve_value = 0 ;
				//Recalcular la curva
									
				String SQL_b = " SELECT I.M_ATTRIBUTEVALUE_ID " +
				" FROM M_ATTRIBUTEINSTANCE I WHERE M_ATTRIBUTESETINSTANCE_ID = "
				+ product_info.getM_AttributeSetInstance_ID() 
				+ " AND I.M_ATTRIBUTEVALUE_ID IS NOT NULL " +
				" ORDER BY I.M_ATTRIBUTEVALUE_ID";
				PreparedStatement pstmt_att = DB.prepareStatement(SQL_b, trans);
				ResultSet rs_att = pstmt_att.executeQuery();				
				//Verify the size curve				
				Hashtable<Integer, Integer> size_curve = 
					size_references.get(reference);		
				if (size_curve != null) {
					//GHUCHET ¿POR QUE REPITE ESTO 3 VECES?
					if (rs_att.next()) { 
						if (size_curve.containsKey(rs_att.getInt(1))) 
							curve_value = size_curve.get(rs_att.getInt(1));												
					}
					if (rs_att.next()) { 
						if (size_curve.containsKey(rs_att.getInt(1))) 
							curve_value = size_curve.get(rs_att.getInt(1));												
					}
					if (rs_att.next()) { 
						if (size_curve.containsKey(rs_att.getInt(1))) 
							curve_value = size_curve.get(rs_att.getInt(1));											
					}					
				}
				
				pstmt_att.close();
				rs_att.close();	
								
				//Check if theres a curve
				if (curve_value > 1) 				
					productDet.setXX_UseSizeCurve(true);
				else 
					productDet.setXX_UseSizeCurve(false);
				productDet.save();
				
				/*GHuchet*/		
				MVMRVendorProdRef ref_info = new MVMRVendorProdRef(Env.getCtx(), reference, trans);
				int xx_package = ref_info.getXX_PackageMultiple();
				
				XX_DistributionUtilities.aplicarPorcentajes(productDet.get_ID(), null, warehouses, percentages, 
						xx_quantity, priorityToAdd, trans, Env.getCtx(), 100, xx_package, 1);
				/*Fin GHuchet*/
			}	
		

		} catch (SQLException e) {			
			e.printStackTrace();
			ADialog.error(windowNo, frame,  "XX_DatabaseAccessError");
			return false;
		}finally {
			try {
				pstmt_a.close();
				rs_a.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		if (!curve_compliance) {
			ADialog.info(windowNo, frame, "XX_UnMatchedProductSizeCurve");						 			
		}
		return trans.commit();
	}
	
	
	
	
	/**
	 * Apply the percentual distribution, save the product records  
	 * @param detail The detail to be distributed
	 * @param percentages Vector that holds the information of percentages. This vector has to be non empty
	 * @param codes Vector that holds the information of store codes
	 *
	 */	
	public static boolean applyDistribution(
			X_XX_VMR_DistributionDetail detail,
			Vector<? extends Number> percentages,
			Vector<Integer> warehouses, 			
			int windowNo, FormFrame frame) {
						
		Cursor wait_cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
		Cursor default_cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR); 
		frame.setCursor(wait_cursor);
		
		if (percentages.isEmpty()) {			
			frame.setCursor(default_cursor);
			ADialog.error(windowNo, frame, "XX_ThereAreNoPercentages");
			return false;			
		}
		int zeros = 0;
		for (int i = 0; i < percentages.size(); i++) {
			if (percentages.get(i).doubleValue() == 0) 
				zeros++;
		}
		if (zeros == percentages.size()) {
			frame.setCursor(default_cursor);
			ADialog.error(windowNo, frame, "XX_ThereAreNoPercentages");
			return false;			
		}
		
		if (warehouses.size() != percentages.size()) {
			frame.setCursor(default_cursor);
			ADialog.error(windowNo, frame, "XX_DistinctSizes");
			return false;
		}
		XX_PercentualDistributionRedistribution dist = 
					new XX_PercentualDistributionRedistribution();
		
		dist.frame = frame;
		dist.windowNo = windowNo;
		dist.detail = detail;
		dist.trans = Trx.get("XX_CHANGE_DELETQTYS");
		dist.safeDeleteRecords();	
		
		if (dist.deletePreviuosAndSaveNewPerc( percentages, warehouses)){
			if ( dist.percentualDistribution(percentages, warehouses) ) {
				frame.setCursor(default_cursor);
				return true;				
			} else {
				frame.setCursor(default_cursor);
				return false;
			}
		}			
		else {
			frame.setCursor(default_cursor);
			return false;		
		}
	}
	
	/**
	 * Apply the percentual distribution, save the product records  
	 * @param detail The detail to be distributed
	 * @param qtys Vector that holds the information of percentages. This vector has to be non empty
	 * @param codes Vector that holds the information of store codes
	 *
	 */	
	public static boolean applyDistributionQty(
			X_XX_VMR_DistributionDetail detail,
			Vector<Integer> qtys,
			Vector<Integer> warehouses, 			
			int windowNo, FormFrame frame) {
		
		Cursor wait_cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
		Cursor default_cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR); 
		frame.setCursor(wait_cursor);
						
		if (qtys.isEmpty()) {			
			ADialog.error(windowNo, frame, "XX_ThereAreNoQtys");
			return false;			
		}
		int zeros = 0;
		for (int i = 0; i < qtys.size(); i++) {
			if (qtys.get(i) == 0) 
				zeros++;
		}
		if (zeros == qtys.size()) {
			ADialog.error(windowNo, frame, "XX_ThereAreNoQtys");
			return false;			
		}
		
		if (warehouses.size() != qtys.size()) {
			ADialog.error(windowNo, frame, "XX_DistinctSizes");
			return false;
		}
		XX_PercentualDistributionRedistribution dist = 
					new XX_PercentualDistributionRedistribution();
		
		dist.frame = frame;
		dist.windowNo = windowNo;
		dist.detail = detail;	
		dist.trans = Trx.get("XX_CHANGE_DELETPERC");
		
		dist.safeDeleteRecords();
		long total_qty = 0;
		
		if (dist.deletePreviuosAndSaveNewQtys( qtys, warehouses)){
			for (int i = 0; i < qtys.size(); i++) {
				long element =qtys.elementAt(i).longValue();
				total_qty += element;			
			}		
			if (total_qty == 0) {			
				ADialog.error(windowNo, frame, "XX_ThereAreNoAmounts");
				return false;
			}			
			Vector<Double> percentages = new Vector<Double>();
			for (int i = 0; i < qtys.size(); i++) {
				Double element = qtys.elementAt(i).doubleValue();
				element = (element/total_qty)*100;
				percentages.add(element);
			}
			if (dist.percentualDistribution(percentages, warehouses)) {
				frame.setCursor(default_cursor);
				return true;
			}
			else {
				frame.setCursor(default_cursor);
				return false;
			}
		}			
		else {
			frame.setCursor(default_cursor);
			return false;		
		}
	}
	
	
	/*
	 * } else {
			Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			m_frame.setCursor(normalCursor);
		}
	 * **/
	/**
	 * Prepares the size curve hash table to accelerate the distribution process
	 * @param header The distribution header to be processed
	 * */
	private void prepareSizeCurve() {
		
		//Get all the values stored for the size curve
		String sql_sizecurve =
			" SELECT XX_VMR_VENDORPRODREF_ID, M_ATTRIBUTEVALUE_ID, XX_CURVEVALUE " +
			" FROM XX_VMR_SIZECURVE S JOIN XX_VMR_SIZECURVEDETAIL D " +
			" ON ( S.XX_VMR_SIZECURVE_ID = D.XX_VMR_SIZECURVE_ID) JOIN " +
			" XX_VMR_SIZECURVEREFERENCE R ON " +
			"(R.XX_VMR_SIZECURVE_ID = D.XX_VMR_SIZECURVE_ID)" +
			" WHERE S.XX_VMR_DISTRIBUTIONHEADER_ID = " + detail.getXX_VMR_DistributionHeader_ID() +
			" AND XX_CURVEVALUE > -1 ";		
		//Calculated by references
		String sql_references = 
			" SELECT XX_VMR_VENDORPRODREF_ID, M_ATTRIBUTEVALUE_ID, XX_CURVEVALUE  " +
			" FROM XX_VMR_SIZECURVE S JOIN XX_VMR_SIZECURVEREFDETAIL D " +
			" ON (S.XX_VMR_SIZECURVE_ID = D.XX_VMR_SIZECURVE_ID) " +
			" WHERE S.XX_VMR_DISTRIBUTIONHEADER_ID = " + detail.getXX_VMR_DistributionHeader_ID() +
			" AND XX_CURVEVALUE > -1 ";
		String sql_union = sql_sizecurve +  " UNION " + sql_references;
		try { 								
			PreparedStatement pstmt_sizecurve = DB.prepareStatement(sql_union, null);
			ResultSet rs_sizecurve = pstmt_sizecurve.executeQuery();
			while (rs_sizecurve.next()) {
				Hashtable<Integer, Integer> curve =
					size_references.get(rs_sizecurve.getInt(1));
				if (curve == null) {
					curve = new Hashtable<Integer, Integer>();
					size_references.put(rs_sizecurve.getInt(1), curve );
				}
				curve.put(rs_sizecurve.getInt(2), rs_sizecurve.getInt(3));
			}
			pstmt_sizecurve.close();
			rs_sizecurve.close();
		} catch (SQLException e) {
			ADialog.error(windowNo, frame, "XX_DatabaseAccessError");
			e.printStackTrace();
		}
	}
	
}

