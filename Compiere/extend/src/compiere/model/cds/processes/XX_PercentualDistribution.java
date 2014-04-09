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
import org.compiere.util.Msg;
import org.compiere.util.Trx;

import compiere.model.cds.MInOut;
import compiere.model.cds.MOrder;
import compiere.model.cds.MProduct;
import compiere.model.cds.X_Ref_XX_DistributionStatus;
import compiere.model.cds.X_XX_VMR_DistribProdPerStore;
import compiere.model.cds.X_XX_VMR_DistributionHeader;
import compiere.model.cds.X_XX_VMR_PO_DistribDetail;
import compiere.model.cds.X_XX_VMR_PO_ProductDistrib;
import compiere.model.cds.X_XX_VMR_StorePercentage;
import compiere.model.cds.X_XX_VMR_StoreQuantity;
import compiere.model.cds.X_XX_VMR_VendorProdRef;
import compiere.model.cds.distribution.XX_DistributionUtilities;
import compiere.model.cds.distribution.XX_StoreAmounts;

public class XX_PercentualDistribution {
	
	/** Indicates whether the distribution are allowed to delete old records*/
	private boolean allowed_to_process = false;
	
	/** A quick storage for the size curve */
	private Hashtable<Integer, Hashtable<Integer, Integer> > size_references
					= new Hashtable<Integer, Hashtable<Integer,Integer>>();
	
	int windowNo;
	static int autoAdjustment = 0;
	FormFrame frame; 
	X_XX_VMR_DistributionHeader header;
	
	/**
	 * Ask the user whether he wants to delete previous values. 
	 * This method should be used before any of the methods of this class
	 * in order to set the necessary flags 
	 * */
	private void safeDeleteRecords () {	
		if(autoAdjustment == 0){ //AGREGADO GHUCHET SOLO IF
			if (header.isXX_CalculatedPOSPercentages() || header.isXX_CalculatedPOSQuantities()) {
				allowed_to_process =
					ADialog.ask(windowNo, frame, "XX_SafeDeleteDistribution");			
			} else allowed_to_process = true;		
		}else allowed_to_process = true;
	}
	
	/**
	 * Deletes the previous percentages and save the new ones 
	 * @param header The Distribution Header to be distributed
	 * @param percentages Vector that holds the information of percentages. This vector has to be non empty
	 * @param codes Vector that holds the information of store codes
	 * */	
	private boolean deletePreviuosAndSaveNewPercentages(					
			Vector<? extends Number> percentages, 
			Vector<Integer> codes) {
			
		
		//Creates a transaction name for the operations
		Trx trans = Trx.get("XX_CHANGE_PO_STOREPERC");
		
		if (allowed_to_process) {			
				//Verify if there's something to delete and delete it
			if (header.isXX_CalculatedPOSPercentages() || header.isXX_CalculatedPOSQuantities()) {
				//SQL Strings			
				String sql_deletepercentages = "DELETE FROM XX_VMR_STOREPERCENTAGE WHERE " +
					"XX_VMR_DISTRIBUTIONHEADER_ID = " + header.get_ID();	
				String sql_delete_qtys = "DELETE FROM XX_VMR_STOREQUANTITY WHERE " +
					"XX_VMR_DISTRIBUTIONHEADER_ID = " + header.get_ID();
								
				try { 				
					//Executing the SQL QUERY	
					DB.executeUpdate(trans,sql_deletepercentages);
					DB.executeUpdate(trans,sql_delete_qtys);
				} catch (Exception e) {
					e.printStackTrace();
					ADialog.error(windowNo, frame, "XX_DatabaseAccessError");
					return false;
				}
			} 			
		} else return false;

			//Save the store percentages
		for (int iterator = 0; iterator < codes.size() ; iterator++) {	
			X_XX_VMR_StorePercentage store_percentage 
				= new X_XX_VMR_StorePercentage(Env.getCtx(), 0, trans);			
			store_percentage.setXX_VMR_DistributionHeader_ID(header.get_ID());
			store_percentage.setM_Warehouse_ID(codes.get(iterator));
			double percentage = percentages.get(iterator).doubleValue();			
			BigDecimal bd = new BigDecimal(Double.toString(percentage));
			bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);			
			store_percentage.setXX_WarehousePercentage(bd);
			if (percentage != 0.0) {
				store_percentage.save();					
			} 	
		}		
		X_XX_VMR_DistributionHeader head = 
			new X_XX_VMR_DistributionHeader(Env.getCtx(), header.get_ID(), trans);
		
		head.setXX_CalculatedPOSPercentages(true);
		head.setXX_CalculatedPOSQuantities(false);
		
		head.save();
		return trans.commit();
	}
	
	/**
	 * Deletes the previous qyts and save the new ones 
	 * @param header The Distribution Header to be distributed
	 * @param quantities Vector that holds the information of quantities. This vector has to be non empty
	 * @param codes Vector that holds the information of store codes
	 * */
	public boolean deletePreviuosAndSaveQtys(
		Vector<? extends Number> quantities, Vector<Integer> codes) {
				
			
			//Creates a transaction name for the operations
			Trx trans = Trx.get("XX_CHANGE_PO_STOREQTYS");				
			if (allowed_to_process) {
				
					//If there's something to delete delete it
				if (header.isXX_CalculatedPOSPercentages() || header.isXX_CalculatedPOSQuantities()) {					
					String sql_deletepercentages = "DELETE FROM XX_VMR_STOREPERCENTAGE WHERE " +
						"XX_VMR_DISTRIBUTIONHEADER_ID = " + header.get_ID();	
					String sql_delete_qtys = "DELETE FROM XX_VMR_STOREQUANTITY WHERE " +
						"XX_VMR_DISTRIBUTIONHEADER_ID = " + header.get_ID();
					
					PreparedStatement pstmt_deletepercentages = DB.prepareStatement(sql_deletepercentages, trans);
					PreparedStatement pstmt_deleteqtys = DB.prepareStatement(sql_delete_qtys, trans);				
					try { 				
						//Executing the SQL QUERY	
						DB.executeUpdate(trans,sql_deletepercentages);
						DB.executeUpdate(trans,sql_delete_qtys);
					} catch (Exception e) {
						e.printStackTrace();
						ADialog.error(windowNo, frame, "XX_DatabaseAccessError");
						return false;
					}
				} 			
			} else return false;
				
				//Saving the store qyts
			for (int iterator = 0; iterator < codes.size() ; iterator++) {	
				X_XX_VMR_StoreQuantity store_qty 
					= new X_XX_VMR_StoreQuantity(Env.getCtx(), 0, trans);
				
				store_qty.setXX_VMR_DistributionHeader_ID(header.get_ID());
				store_qty.setM_Warehouse_ID(codes.get(iterator));					
				Double quantity = quantities.get(iterator).doubleValue();
				if (!quantity.equals(Math.floor(quantity))) {
					ADialog.error(windowNo, frame, "XX_NotAnIntegralType");
					trans.rollback();					
					return false;
					}						
				store_qty.setXX_Quantity(new BigDecimal(quantity));
				if (quantity != 0.0) 
					store_qty.save();			
			}
			
			//Indicating in the header that the quantityes has been created
			X_XX_VMR_DistributionHeader head = 
				new X_XX_VMR_DistributionHeader(Env.getCtx(), header.get_ID(), trans);				
			head.setXX_CalculatedPOSQuantities(true);
			head.setXX_CalculatedPOSPercentages(false);
			head.save();
			
			//Commit the changes
			return trans.commit();		
		}
	
	/**
	 * Prepares the size curve hash table to accelerate the distribution process
	 * @param header The distribution header to be processed
	 * */
	private void prepareSizeCurve( ) {
		
		//Get all the values stored for the size curve
		String sql_sizecurve =
			" SELECT XX_VMR_VENDORPRODREF_ID, M_ATTRIBUTEVALUE_ID, XX_CURVEVALUE " +
			" FROM XX_VMR_SIZECURVE S JOIN XX_VMR_SIZECURVEDETAIL D " +
			" ON ( S.XX_VMR_SIZECURVE_ID = D.XX_VMR_SIZECURVE_ID) JOIN " +
			" XX_VMR_SIZECURVEREFERENCE R ON " +
			"(R.XX_VMR_SIZECURVE_ID = D.XX_VMR_SIZECURVE_ID)" +
			" WHERE S.XX_VMR_DISTRIBUTIONHEADER_ID = " + header.get_ID() +
			" AND XX_CURVEVALUE > -1 ";		
		//Calculated by references
		String sql_references = 
			" SELECT XX_VMR_VENDORPRODREF_ID, M_ATTRIBUTEVALUE_ID, XX_CURVEVALUE  " +
			" FROM XX_VMR_SIZECURVE S JOIN XX_VMR_SIZECURVEREFDETAIL D " +
			" ON (S.XX_VMR_SIZECURVE_ID = D.XX_VMR_SIZECURVE_ID) " +
			" WHERE S.XX_VMR_DISTRIBUTIONHEADER_ID = " + header.get_ID() +
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
	
	/*
	 * Jorge Pires - Busca si una PO tiene Recepcion
	 */
	private boolean verificaPOTieneRecepcion(Integer PO){
		boolean devuelve = false;
		String sql = "SELECT * " +
					 "FROM M_INOUT IO " +
					 "WHERE IO.C_ORDER_ID = "+PO+" " +
					 "AND DOCSTATUS = '"+MInOut.DOCACTION_Complete+"'";
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				devuelve = true;
			}else{
				devuelve = false;
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			//Log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			devuelve = false;
		}	
		return devuelve;
	}
	/*
	 * Fin Jorge Pires - Busca si una PO tiene Recepcion
	 */
	/*
	 * GHUCHET - Indica si una O/C tiene Recepción en el caso de Ajuste Automático
	 */
	private boolean verificaPOTieneRecepcionAuto(Integer PO){
		boolean devuelve = false;
		String sql = "SELECT * " +
					 "FROM M_INOUT " +
					 "WHERE C_ORDER_ID = "+PO;
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				devuelve = true;
			}else{
				devuelve = false;
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			//Log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			devuelve = false;
		}	
		return devuelve;
	}
	/*
	 * Fin GHUCHET - Indica si una O/C tiene Recepción en el caso de Ajuste Automático
	 */

	
	/**
	 * Apply the percentual distribution, save the product records  
	 * @param header The Distribution Header to be distributed
	 * @param percentages Vector that holds the information of percentages. This vector has to be non empty
	 * @param codes Vector that holds the information of store codes
	 * */	
	private boolean percentualDistribution(	Vector<? extends Number> percentages,
			Vector<Integer> warehouses) {
				
		boolean some_has_size_curve_and_multiple = false;
		int noProductos = 0;
	
			//If the user selected NO the process cannot be performed
		if (!allowed_to_process) return false;
		Trx trans = Trx.get("XX_CHANGE_PO_QUANTITIES");

		//Delete any previous distribution
		String SQL_t = " DELETE FROM XX_VMR_PO_DISTRIBDETAIL WHERE " +
						" XX_VMR_PO_PRODUCTDISTRIB_ID IN (SELECT XX_VMR_PO_PRODUCTDISTRIB_ID " +
						" FROM XX_VMR_PO_PRODUCTDISTRIB WHERE " +
						" XX_VMR_DISTRIBUTIONHEADER_ID =  " + header.get_ID() + " ) "; 		
	
		try { 
			//Executing the SQL QUERIES		
			DB.executeUpdate(trans,SQL_t);
		} catch (Exception e) {
			e.printStackTrace();
			ADialog.error(windowNo, frame, "XX_DatabaseAccessError");
			return false;
		}					
			//Prepares the size curve
		prepareSizeCurve();
		
			//Distribution variables - Knowing the largest store
		int total = 0, most = 0; 
		double most_value_percentage = 0;
		
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
		String sql_previous = " SELECT M_PRODUCT_ID, XX_VMR_PO_PRODUCTDISTRIB_ID " +
				" FROM XX_VMR_PO_PRODUCTDISTRIB WHERE XX_VMR_DISTRIBUTIONHEADER_ID = " 
				+ header.get_ID();		
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
				
		/*
		 * Modificado por Jorge Pires
		 * 
		 * Verifica si tiene chequeo para ver que cantidades toma
		 * */
		String SQL_a = "";
		boolean tieneChequeo = false;
		
			if(autoAdjustment == 0 )  //AGREGADO POR GHUCHET EL IF
				tieneChequeo = verificaPOTieneRecepcion(header.getC_Order_ID());
			 //AGREGADO POR GHUCHET
			else if(autoAdjustment == 1)
				tieneChequeo = verificaPOTieneRecepcionAuto(header.getC_Order_ID());
			//FIN AGREGADO POR GHUCHET
		if(tieneChequeo){
		   SQL_a = "SELECT PO_REF.XX_VMR_PO_LINEREFPROV_ID, PO_REF.XX_PACKAGEMULTIPLE, " +
				   "PO_REF.XX_VMR_VENDORPRODREF_ID, M_IOL.M_PRODUCT_ID M_PRODUCT, MOVEMENTQTY XX_QTY, " +
				   "PO_REF.XX_SALEPRICE, PO_REF.XX_TaxAmount, PO_REF.XX_SALEPRICEPLUSTAX, " +
				   "PO_REF.C_TaxCategory_ID, PO_REF.XX_Margin, PO_REF.PRICEACTUAL, " +
				   "PO_REF.XX_ISDEFINITIVE, PO_REF.XX_UnitPurchasePrice  " +
				   "FROM XX_VMR_PO_LINEREFPROV PO_REF, M_INOUTLINE M_IOL, C_ORDERLINE C_OL " +
				   "WHERE M_IOL.C_ORDERLINE_ID = C_OL.C_ORDERLINE_ID " +
				   "AND C_OL.XX_VMR_PO_LINEREFPROV_ID = PO_REF.XX_VMR_PO_LINEREFPROV_ID " +
				   "AND PO_REF.C_ORDER_ID = "+header.getC_Order_ID()+"";		   
		}else{
			//Distributes each product associated to the purchase order 
			SQL_a = "SELECT PO_REF.XX_VMR_PO_LINEREFPROV_ID, PO_REF.XX_PACKAGEMULTIPLE, " +
					" PO_REF.XX_VMR_VENDORPRODREF_ID, " +
					" M_PRODUCT, XX_VALUE1, XX_VALUE2, (XX_QUANTITYV + XX_QUANTITYO) XX_QTY, " +
					
					//AGREGADO POR JOSE LUIS
					" XX_SALEPRICE, XX_TaxAmount, XX_SALEPRICEPLUSTAX, C_TaxCategory_ID, XX_Margin, PRICEACTUAL, XX_ISDEFINITIVE, XX_UnitPurchasePrice " +				
					//FIN AGREGADO POR JOSE LUIS
					
					" FROM XX_VMR_PO_LINEREFPROV PO_REF LEFT JOIN XX_VMR_REFERENCEMATRIX REF_MATRIX " +
					" ON (PO_REF.XX_VMR_PO_LINEREFPROV_ID = REF_MATRIX.XX_VMR_PO_LINEREFPROV_ID) " +
					" WHERE PO_REF.C_ORDER_ID = " +  header.getC_Order_ID() + " AND (XX_QUANTITYV + XX_QUANTITYO) > 0 ";				
				//Create a transaction name			
		}
		/*
		 * Fin - Modificado por Jorge Pires
		 */
		
		//Revision 25/11/2010
		//Agregar que verifique e ignore los multiplos de empaque.
		boolean ignore_multiple = header.isXX_IgnorePackageMultiple();
		
		PreparedStatement pstmt_a = DB.prepareStatement(SQL_a, trans);
		boolean curve_compliance = true;
		boolean ok_to_distribute = true;		
		try { 
			ResultSet rs_a = pstmt_a.executeQuery();				
			int line = 0, reference = 0, product = 0, xx_value1 = 0, xx_value2 = 0, xx_package = 0,
			xx_quantity = 0;
							
			//FOR EACH PRODUCT IN THE PURCHASE ORDER
			while(rs_a.next()) {
				boolean both_size_curve_multiple = false;
				
				line = rs_a.getInt("XX_VMR_PO_LINEREFPROV_ID");
				reference = rs_a.getInt("XX_VMR_VENDORPRODREF_ID");
				product = rs_a.getInt("M_PRODUCT");									
				xx_quantity = rs_a.getInt("XX_QTY");				
				/*
				 * Modificado por Jorge Pires
				 * 
				 * Verifica si tiene chequeo para ver que cantidades toma
				 */
				
				if(tieneChequeo){
					String sql = "SELECT XX_VALUE1, XX_VALUE2 " +
								 "FROM XX_VMR_PO_LINEREFPROV PO_REF LEFT JOIN XX_VMR_REFERENCEMATRIX REF_MATRIX " +
								 "ON (PO_REF.XX_VMR_PO_LINEREFPROV_ID = REF_MATRIX.XX_VMR_PO_LINEREFPROV_ID) " +
								 "WHERE PO_REF.C_ORDER_ID = "+header.getC_Order_ID()+" " +
								 "AND M_PRODUCT = "+product+" ";
					PreparedStatement pstmt1 = null;
					ResultSet rs1 = null;
					try{
						pstmt1 = DB.prepareStatement(sql, null);
						rs1 = pstmt1.executeQuery();
						
						if(rs1.next()){
							xx_value1 = rs1.getInt("XX_VALUE1");
							xx_value2 = rs1.getInt("XX_VALUE2");
						}
					}
					catch (Exception e){
						e.printStackTrace();
						//Log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
					} finally {
						DB.closeStatement(pstmt1);
						DB.closeResultSet(rs1);
					}
				}else{
					xx_value1 = rs_a.getInt("XX_VALUE1");
					xx_value2 = rs_a.getInt("XX_VALUE2");
				}
				
				noProductos += xx_quantity;
				/*
				 * Fin - Modificado por Jorge Pires
				 * */
				
				if (ignore_multiple)				
					xx_package = 1;
				else 
					xx_package = rs_a.getInt("XX_PACKAGEMULTIPLE");
					
				
				//Verify that the product is assigned a reference
				if (product == 0 ) {
					X_XX_VMR_VendorProdRef vendor = new X_XX_VMR_VendorProdRef(Env.getCtx(),
							reference, trans); 							
					String mss = Msg.getMsg(Env.getCtx(), "XX_UnAssociatedProducts", 
							//reference
							//line
							new String[] { vendor.getValue()+ "-" + vendor.getName(),"" + line});
					ADialog.error(windowNo, frame, mss);						
					ok_to_distribute = false;
					break;
				}				
				
				/*There's nothing to distribute
				if (xx_quantity == 0) continue;*/
								
				int distributed_quantity = 0, curve_value = 0;				
				Hashtable<Integer, Integer> size_curve = size_references.get(reference);				
				if (size_curve != null) {					
					if (size_curve.containsKey(xx_value1)) {
						curve_value = size_curve.get(xx_value1);						
					} else if (size_curve.containsKey(xx_value2)) {
						curve_value = size_curve.get(xx_value2);					
					}
				}					
				//Check if there's an incompatibility in the curve
				if ((curve_value > 1) && (xx_package > 1 )) {
					some_has_size_curve_and_multiple = true;
					both_size_curve_multiple = true;
				}
				
				//Related to the previous distribution values				
				int po_detail_id = 0;
				boolean previous_distribution = false;
				if (old_product_distrib.containsKey(product)) {					
					po_detail_id = old_product_distrib.get(product);
					previous_distribution = true;
				} 
				
				//Creates a product distribution
				X_XX_VMR_PO_ProductDistrib distribution = 
					new X_XX_VMR_PO_ProductDistrib(header.getCtx(), po_detail_id, null);
				
				if (curve_value > 1) 
					distribution.setXX_UsedSizeCurve(true);
				else 
					distribution.setXX_UsedSizeCurve(false);
				
				if (!previous_distribution) {				
					distribution.setXX_VMR_DistributionHeader_ID(header.get_ID());
					distribution.setM_Product_ID(product);
					
					/*
					 * Jorge Pires
					 * */
					MProduct mProductAux = new MProduct(Env.getCtx(), product, null);
					distribution.setM_AttributeSet_ID(mProductAux.getM_AttributeSet_ID());
					distribution.setM_AttributeSetInstance_ID(mProductAux.getM_AttributeSetInstance_ID());
					/*
					 * Jorge Pires - Fin
					 * */
					
					//LO AGREGADO POR JOSE LUIS
					distribution.setXX_VMR_PO_LineRefProv_ID(line);
					distribution.setPriceActual(rs_a.getBigDecimal("PRICEACTUAL"));
					distribution.setXX_SalePrice(rs_a.getBigDecimal("XX_SALEPRICE"));
					distribution.setXX_TaxAmount(rs_a.getBigDecimal("XX_TaxAmount"));
					distribution.setXX_SalePricePlusTax(rs_a.getBigDecimal("XX_SALEPRICEPLUSTAX"));
					distribution.setC_TaxCategory_ID(rs_a.getInt("C_TaxCategory_ID"));				
					distribution.setXX_Margin(rs_a.getBigDecimal("XX_Margin"));
					distribution.setXX_UnitPurchasePrice(rs_a.getBigDecimal("XX_UnitPurchasePrice"));
					distribution.setXX_DistributedQTY(xx_quantity);
					distribution.setXX_CanSetDefinitive(true);
					if(rs_a.getString("XX_ISDEFINITIVE").equals("Y"))
						distribution.setXX_IsDefinitive(true);
					else
						distribution.setXX_IsDefinitive(false);			
					
				}
				//FIN CODIGO JOSE LUIS
				distribution.save(trans);
				
				MOrder order = new MOrder(Env.getCtx(),header.getC_Order_ID(), null);
				
				/*GHuchet*/
				XX_DistributionUtilities.aplicarPorcentajes(distribution.get_ID(), order, warehouses, percentages, xx_quantity, priorityToAdd,
							trans, Env.getCtx(), 100, xx_package, 1);
				/*Fin GHuchet*/
			}			
			
			
			//Si la distribucion estaba alterada por la distribucion, esto debio corregir la situacion
			if (header.getXX_DistributionStatus().equals(
					X_Ref_XX_DistributionStatus.PENDIENTE_POR_REDISTRIBUCION_Y_APROBACION.getValue())) {				
				header.setXX_DistributionStatus(X_Ref_XX_DistributionStatus.LISTA_PARA_APROBAR.getValue());
				header.save(trans);
			}

			pstmt_a.close();
			rs_a.close();
		} catch (SQLException e) {			
			e.printStackTrace();
			ADialog.error(windowNo, frame,  "XX_DatabaseAccessError");
			return false;
		}				
	
		if (ok_to_distribute) { /** It´s ok to distribute */
			
			if(autoAdjustment == 0){ //AGREGADO POR GHUCHET EL IF
				MOrder order = new MOrder(Env.getCtx(), header.getC_Order_ID(), null);			
				order.setXX_StoreDistribution("Y");			
				order.save();		
			}
			{
				String update_withchar = " UPDATE XX_VMR_PO_LINEREFPROV " +
						" SET XX_GENERATEMATRIX = 'N' , " +
						" XX_DELETEMATRIX = 'N' WHERE C_ORDER_ID = "
						+ header.getC_Order_ID() + " AND XX_WITHCHARACTERISTIC = 'Y'";				
				try {
					DB.executeUpdate(null,update_withchar);
				} catch (Exception e) {
					e.printStackTrace();
					ADialog.error(windowNo, frame, "XX_DatabaseAccessError");
					return false;
				}
			}
			if(autoAdjustment ==0){ 	//AGREGADO GHUCHET ESTE IF SOLAMENTE
				if (some_has_size_curve_and_multiple) {
					ADialog.error(windowNo, frame, "XX_BothSizeAndPackageMultiple");				
				} else if (!curve_compliance) { 
					ADialog.info(windowNo, frame, "XX_UnMatchedProductSizeCurve");
				} 
			}
			//AGREGADO GHUCHET
			X_XX_VMR_DistributionHeader head = 
				new X_XX_VMR_DistributionHeader(Env.getCtx(), header.get_ID(), trans);	
			int distType = Env.getCtx().getContextAsInt("#XX_L_DISTRIBUTIONTYPEPERCEN_ID");
			head.setXX_DistributionTypeApplied(distType);
			head.save(trans);
			//FIN GHUCHET
			return trans.commit();
		} else { 
			trans.rollback();
			return false;
		}
	}

	   
	/**
	 * Apply the percentual distribution, save the product records  
	 * @param header The Distribution Header to be distributed
	 * @param percentages Vector that holds the information of percentages. This vector has to be non empty
	 * @param codes Vector that holds the information of store codes
	 * */	
	public static boolean applyPOPercentualDistribution(X_XX_VMR_DistributionHeader header,
			Vector<? extends Number> percentages, Vector<Integer> warehouses,
			int windowNo, FormFrame frame, int isAutoAdjustment) {
			
		Cursor wait_cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
		Cursor default_cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR); 
		frame.setCursor(wait_cursor);
		autoAdjustment = isAutoAdjustment; //AGREGADO GHUCHET
		
		if (percentages.isEmpty()) {	
			frame.setCursor(default_cursor);
			if(autoAdjustment == 0){   	//AGREGADO GHUCHET EL IF
				ADialog.error(windowNo, frame, "XX_ThereAreNoPercentages");
			}
			return false;			
		}		
		//Checks if they are the same size
		if (warehouses.size() != percentages.size()) {
			frame.setCursor(default_cursor);
			if(autoAdjustment == 0){	//AGREGADO GHUCHET EL IF
				ADialog.error(windowNo, frame, "XX_DistinctSizes");
			}
			return false;
		}
		int zeros = 0;
		for (int i = 0; i < percentages.size(); i++) {
			if (percentages.get(i).doubleValue() == 0) 
				zeros++;
		}
		if (zeros == percentages.size()) {
			frame.setCursor(default_cursor);
			if(autoAdjustment == 0){  	//AGREGADO GHUCHET EL IF
				ADialog.error(windowNo, frame, "XX_ThereAreNoAmounts");
			}
			return false;			
		}

		
		XX_PercentualDistribution dist = new XX_PercentualDistribution();
		dist.header = header;
		dist.windowNo = windowNo;
		dist.frame = frame;
		dist.safeDeleteRecords();
		if (dist.deletePreviuosAndSaveNewPercentages(percentages, warehouses)){	
			if (dist.percentualDistribution(percentages, warehouses)) {
				if(autoAdjustment==0)
					frame.setCursor(default_cursor);
				return true;
			} else {
				if(autoAdjustment==0)
					frame.setCursor(default_cursor);
				return false;
			} 		
		} else {
			if(autoAdjustment==0)
				frame.setCursor(default_cursor);
			return false;
			}
	}
	
	/**
	 * Apply the quantities distribution, save the product records  
	 * @param header The Distribution Header to be distributed
	 * @param percentages Vector that holds the information of percentages. This vector has to be non empty
	 * @param codes Vector that holds the information of store codes
	 * */	
	public static boolean applyPOQuantityDistribution(
			X_XX_VMR_DistributionHeader header,
			Vector<? extends Number> quantities, 
			Vector<Integer> warehouses,
			int windowNo, FormFrame frame) {
		
		Cursor wait_cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
		Cursor default_cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR); 
		frame.setCursor(wait_cursor);
		
		//Checks if they are the same size
		if (warehouses.size() != quantities.size()) {
			frame.setCursor(default_cursor);
			ADialog.error(windowNo, frame, "XX_DistinctSizes");
			return false;
		}
		if (quantities.isEmpty()) {			
			frame.setCursor(default_cursor);
			ADialog.error(windowNo, frame, "XX_ThereAreNoAmounts");
			return false;
		}
		int zeros = 0;
		for (int i = 0; i < quantities.size(); i++) {
			if (quantities.get(i).doubleValue() == 0) 
				zeros++;
		}
		if (zeros == quantities.size()) {
			frame.setCursor(default_cursor);
			ADialog.error(windowNo, frame, "XX_ThereAreNoAmounts");
			return false;			
		}

		XX_PercentualDistribution dist = new XX_PercentualDistribution();
		
		dist.header = header;
		dist.windowNo = windowNo;
		dist.frame = frame;
		dist.safeDeleteRecords();
		if (dist.deletePreviuosAndSaveQtys(quantities, warehouses)) {
			long total_qty = 0; 
			Vector<Double> percentages = new Vector<Double>();
			for (int i = 0; i < quantities.size(); i++) {
				long element = quantities.elementAt(i).longValue();
				total_qty += element;			
			}		
			if (total_qty == 0) {			
				ADialog.error(windowNo, frame, "XX_ThereAreNoAmounts");
				return false;
			}		
			for (int i = 0; i < quantities.size(); i++) {
				Double element = quantities.elementAt(i).doubleValue();
				element = (element/total_qty)*100;
				percentages.add(element);
			}
			if (dist.percentualDistribution(percentages, warehouses)) {
				frame.setCursor(default_cursor);
				return true;
			} else {
				frame.setCursor(default_cursor);
				return false;
			}
		} else {
			frame.setCursor(default_cursor);
			return false;
		}		
	}
}
