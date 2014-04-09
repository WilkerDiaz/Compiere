package compiere.model.cds.processes;

import java.awt.Container;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import org.compiere.apps.ADialog;
import org.compiere.apps.form.FormFrame;
import org.compiere.model.MRole;
import org.compiere.model.X_Ref_Quantity_Type;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import compiere.model.cds.MOrder;
import compiere.model.cds.MProduct;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_VMR_OrderStatus;
import compiere.model.cds.X_XX_VMR_DistribProductDetail;
import compiere.model.cds.X_XX_VMR_DistributionDetail;

public class XX_BatchNumberInfo {
	
	public Hashtable<Integer, helperClass> data = new Hashtable<Integer, helperClass>();

	
	public static boolean ProductCostOrigin (int mproduct_id, int pieces, X_XX_VMR_DistribProductDetail detail) {
		
		X_XX_VMR_DistributionDetail distDetail = new X_XX_VMR_DistributionDetail(Env.getCtx(), detail.getXX_VMR_DistributionDetail_ID(), null);
		int m_warehouse_id = Utilities.obtenerDistribucionCD(distDetail.getXX_VMR_DistributionHeader_ID());
		
		String sql = 
			 " SELECT STO.QTY QTY, LOT.XX_SALEPRICE SALE, LOT.M_ATTRIBUTESETINSTANCE_ID LOTE , LOT.PRICEACTUAL PRICE, PRO.C_TAXCATEGORY_ID CAT, " +
			 " (SELECT rate/100 FROM C_Tax WHERE ValidFrom= (SELECT MAX(ValidFrom) FROM C_Tax " + 
			 "    WHERE C_TaxCategory_ID= PRO.C_TaxCategory_ID) and ROWNUM <= 1) TAXAMOUNT " +
			 " FROM M_STORAGEDETAIL STO INNER JOIN M_ATTRIBUTESETINSTANCE LOT " + 
			 " ON ( STO.M_ATTRIBUTESETINSTANCE_ID = LOT.M_ATTRIBUTESETINSTANCE_ID ) " +
			 " JOIN M_PRODUCT PRO ON (STO.M_PRODUCT_ID = PRO.M_PRODUCT_ID ) " +
			 " WHERE STO.M_PRODUCT_ID = " + mproduct_id + 
			 " AND STO.M_AttributeSetInstance_ID>=0" +
			 " AND STO.M_lOCATOR_ID >= 0" +
			 " AND STO.M_LOCATOR_ID = " + Utilities.obtenerLocatorChequeado(m_warehouse_id).get_ID()+  
//			 " AND STO.M_LOCATOR_ID = " + Env.getCtx().getContextAsInt("#XX_L_LOCATORCDCHEQUEADO_ID") +  
			 " AND STO.QTY > 0 " + 
			 " AND STO.QTYTYPE = '"+X_Ref_Quantity_Type.ON_HAND.getValue()+"' " + 
			 " ORDER BY STO.M_ATTRIBUTESETINSTANCE_ID";  
	System.out.println(sql);
		PreparedStatement ps = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, null);
		double total = 0;
		try {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) 
				total += rs.getDouble(1); 			
			if (total < pieces ) {				
				rs.close();
				ps.close();
				MProduct product = new MProduct(Env.getCtx(), mproduct_id, null);
				String mss = Msg.getMsg(Env.getCtx(), "XX_InventoryInconsistency", 
						new String[] {product.get_ID()+ "-"+ product.getName()});
				ADialog.error(1, new Container(), mss);
				return false;
			}
			Vector<Integer> tax_cat = new Vector<Integer>();
			Vector<Double> cost = new Vector<Double>();	
			Vector <Double> sale_amount = new Vector<Double>();			
			Vector<Double> price_actual = new Vector<Double>();
			Vector<Double> tax_amount = new Vector<Double>();
			//System.out.println(sql);
			
			//Se recorre nuevamente el resultset
			rs.beforeFirst();
			while (rs.next()) {	
				sale_amount.add(rs.getDouble("SALE"));
				price_actual.add(rs.getDouble("PRICE"));				
				tax_cat.add(rs.getInt("CAT"));												
				tax_amount.add( rs.getDouble("TAXAMOUNT") * rs.getDouble("SALE") );
			
				if ( pieces - rs.getInt("QTY") > 0 ) {										
					cost.add(rs.getDouble("PRICE"));					
					pieces -= rs.getInt(2) ;
				} else {					
					cost.add(rs.getDouble("PRICE"));					
					pieces = 0 ;
					break;
				}				
			}			
			rs.close();
			ps.close();
			
			//Verify wich one is the largest cost			
			double product_cost = 0.01;	
			int max_index = -1;
			for (int j = 0; j < cost.size() ; j++) {
				if ( product_cost < cost.elementAt(j) ) {
					product_cost = cost.elementAt(j);
					max_index = j;					
				} 				
			}
			if (max_index != -1) {											
				detail.setXX_UnitPurchasePrice(new BigDecimal(price_actual.get(max_index))) ;
				detail.setC_TaxCategory_ID(tax_cat.get(max_index));								
				if (sale_amount.get(max_index) != 0.0) {
					
					//Solo en este caso podemos colocar margen
					detail.setXX_Margin( new BigDecimal(
								(100*(sale_amount.get(max_index) - price_actual.get(max_index)) / sale_amount.get(max_index))
								).setScale(2, RoundingMode.HALF_EVEN));	
				}
				detail.setXX_SalePrice(new BigDecimal(sale_amount.get(max_index)).setScale(2, RoundingMode.HALF_EVEN));
				detail.setXX_TaxAmount(new BigDecimal(tax_amount.get(max_index)).setScale(2, RoundingMode.HALF_EVEN));
				detail.setPriceActual(new BigDecimal(product_cost).setScale(2, RoundingMode.HALF_EVEN));
				detail.setXX_SalePricePlusTax(
						new BigDecimal(sale_amount.get(max_index) + tax_amount.get(max_index)).setScale(2, RoundingMode.HALF_EVEN)
				);
			} else {
				detail.setC_TaxCategory_ID(tax_cat.get(0));	
				detail.setXX_UnitPurchasePrice(new BigDecimal(product_cost).setScale(2, RoundingMode.HALF_EVEN)); 
				detail.setXX_SalePrice(new BigDecimal(product_cost).setScale(2, RoundingMode.HALF_EVEN));
				detail.setXX_TaxAmount(new BigDecimal(product_cost).setScale(2, RoundingMode.HALF_EVEN));
				detail.setPriceActual(new BigDecimal(product_cost).setScale(2, RoundingMode.HALF_EVEN));
				detail.setXX_SalePricePlusTax(new BigDecimal(product_cost).setScale(2, RoundingMode.HALF_EVEN));
							
			}			
		} catch (SQLException e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
	/** Private class that holds the available quantity for a locator*/
	public class helperClass {
		
		Vector<Integer> batchnumbers = new Vector<Integer>();		
		Vector<Integer> remaining_quantity = new Vector<Integer>();
		Vector<BigDecimal> original_cost = new  Vector<BigDecimal>(); 
		
		@Override
		public String toString() {

			return batchnumbers + "\n" + remaining_quantity;
		}
	}
	
	/**
	 * Holds the availability on a locator an indicates where a product
	 * was supposed to be purchase given given the fifo approach
	 * */
	public class helperClass2 {		
		Vector<Integer> c_order = new Vector<Integer>();
		Vector<Integer> is_associated = new Vector<Integer>();
		Vector<Integer> batchno = new Vector<Integer>();
		Vector<Integer> qtyonhands = new Vector<Integer>();
	}
		
	/** */
	
	
	public void get_put (int m_product_id, int m_detail_id, Trx getTrx, int m_warehouse_id) {
		
		if (data.containsKey(m_product_id)) {			
			return;
		}			
		//Check the locator info for each product 
		
		String withSQL = "WITH OrderRequest AS " +
			"(select det.M_Product_ID, det.XX_ProductBatch_ID, sum(det.XX_ProductQuantity) Qty " +
			"FROM XX_VMR_Order ord " +
			"join XX_VMR_ORDERREQUESTDETAIL det on (ord.XX_VMR_Order_ID = det.XX_VMR_Order_ID) " +
			"join XX_VMR_DISTRIBUTIONHEADER dis on (dis.XX_VMR_DISTRIBUTIONHEADER_ID = ord.XX_VMR_DISTRIBUTIONHEADER_ID) " +
			"WHERE " +
			"det.M_PRODUCT_ID = "+ m_product_id +" "+
			"AND ord.XX_OrderRequestStatus IN ('"+ X_Ref_XX_VMR_OrderStatus.POR_ETIQUETAR.getValue() +"') " + 
			"and dis.M_WAREHOUSE_ID = " + m_warehouse_id +" "+
			"group by det.M_Product_ID, det.XX_ProductBatch_ID) ";
		
		String sql = withSQL +
			" SELECT STO.M_ATTRIBUTESETINSTANCE_ID, (STO.QTY - NVL(ord.QTY, 0)) AS QTYONHAND " +				
			" FROM M_STORAGEDETAIL STO" +
			" LEFT JOIN OrderRequest ord ON (ord.M_Product_ID = sto.M_Product_ID AND ord.XX_ProductBatch_ID = sto.M_ATTRIBUTESETINSTANCE_ID)" +
			" WHERE STO.M_PRODUCT_ID = " + m_product_id + 
			" AND STO.M_LOCATOR_ID = " + Utilities.obtenerLocatorChequeado(m_warehouse_id).get_ID() +  //CAMBIO PROYECTO CD VALENCIA - GHUCHET
			" AND STO.QTYTYPE = '"+X_Ref_Quantity_Type.ON_HAND.getValue()+"' " +
			" AND STO.QTY > 0 " +
			" AND STO.M_AttributeSetInstance_ID>=0" +
			" AND STO.M_lOCATOR_ID >= 0" +
			" AND (STO.QTY - NVL(ord.QTY, 0)) > 0" +
			" ORDER BY STO.M_ATTRIBUTESETINSTANCE_ID ";
		
		PreparedStatement ps = DB.prepareStatement(sql, getTrx);	
		
		try {

			helperClass help = new helperClass();				
			
			ResultSet rs = ps.executeQuery();	
			while (rs.next())  {
				help.batchnumbers.add(rs.getInt(1));
				help.remaining_quantity.add(rs.getInt(2));
			}
			//Add the result to the hash table			
			data.put(m_product_id, help);	
			
			rs.close();
			ps.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}
	/**
	 * Indica las ordenes de compra asociadas a una o/c dada
	 * 
	 * */
	public static Vector<MOrder> associatedPurchaseOrders (int c_order_id, int window, FormFrame frame ) {
		
		Vector<MOrder> vector_resultado = new Vector<MOrder>();
		
		MOrder order = new MOrder(Env.getCtx(), c_order_id, null);
		if (order.getAssociation_ID() == 0)
			return vector_resultado;
		
		String sql = " SELECT C.C_ORDER_ID FROM C_ORDER  C " +
					 " WHERE C.XX_ISASSOCIATED= 'Y' AND " +
					 " C.ASSOCIATION_ID = " + order.getAssociation_ID() + 
					 " AND C.C_ORDER_ID <> " + c_order_id; 		
		sql =  MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		PreparedStatement ps = DB.prepareStatement(sql, null);
		try {
			ResultSet rs = ps.executeQuery();			
			while (rs.next()) {
				vector_resultado.add(
						new MOrder(Env.getCtx(), rs.getInt(1), null));				
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {			
			e.printStackTrace();
			ADialog.error(window, frame, "XX_DatabaseAccessError");			
			return new Vector<MOrder>() ;
		}		
		return vector_resultado;		
	}
	
	public Vector<Integer> hasAssociatedPOProduct (int mproduct_id, int qty, int window, FormFrame frame, int m_warehouse_id) {
		
		Vector<Integer> c_order = new Vector<Integer>();
		Vector<Integer> is_associated = new Vector<Integer>();		
		Vector<Integer> qtyonhands = new Vector<Integer>();
		
		Vector<Integer> result = new Vector<Integer>();
		
		String sql = 
			" SELECT ORD.C_ORDER_ID, ORD.ASSOCIATION_ID, STO.QTY as QTYONHAND " +			
			" FROM M_STORAGEDETAIL STO INNER JOIN M_INOUTLINE REC  " +
			" ON ( STO.M_ATTRIBUTESETINSTANCE_ID = REC.M_ATTRIBUTESETINSTANCE_ID " +
			" AND STO.M_PRODUCT_ID = REC.M_PRODUCT_ID ) " +
			" INNER JOIN C_ORDERLINE LIN ON (REC.C_ORDERLINE_ID = LIN.C_ORDERLINE_ID) " +
			" INNER JOIN XX_VMR_PO_LINEREFPROV REF " +
			" ON (LIN.XX_VMR_PO_LINEREFPROV_ID = REF.XX_VMR_PO_LINEREFPROV_ID) " +
			" INNER JOIN C_ORDER ORD ON (ORD.C_ORDER_ID = REF.C_ORDER_ID )" + 
			" WHERE STO.M_PRODUCT_ID = " + mproduct_id + 
			" AND STO.M_AttributeSetInstance_ID>=0" +
			" AND STO.M_lOCATOR_ID >= 0" +
			" AND STO.M_LOCATOR_ID = " + Utilities.obtenerLocatorChequeado(m_warehouse_id).get_ID()+  
//			" AND STO.M_LOCATOR_ID = " + Env.getCtx().getContextAsInt("#XX_L_LOCATORCDCHEQUEADO_ID") +  			
			" AND STO.M_ATTRIBUTESETINSTANCE_ID > 0 " +
			" AND STO.QTYTYPE = '"+X_Ref_Quantity_Type.ON_HAND.getValue()+"' " +
			" AND STO.QTY > 0 " +
			" ORDER BY STO.M_ATTRIBUTESETINSTANCE_ID ";	

		//OPTIMIZACION Eliminado
		
		/*Lo que actualmente esta reservado
		String sql_reservado = " SELECT COALESCE(SUM(XX_DESIREDQUANTITY), 0) FROM XX_VMR_DISTRIBDETAILTEMP " +
				" WHERE M_PRODUCT_ID = " + mproduct_id;
		System.out.println(sql_reservado);*/
		PreparedStatement ps = DB.prepareStatement(sql, null);
		//PreparedStatement ps_reservado = DB.prepareStatement(sql_reservado, null);		 
		try {
			
			Integer reserved_pieces = 0;
			/*ResultSet rs_reservado = ps_reservado.executeQuery();
			if (rs_reservado.next()) {
				reserved_pieces = rs_reservado.getInt(1);
			}
			rs_reservado.close();*/
			//ps_reservado.close();
						
			ResultSet rs = ps.executeQuery();
			Integer real_pieces = 0;
			while (rs.next()) {				
				real_pieces = rs.getInt(3);
				if (reserved_pieces > 0) {
					if (reserved_pieces <= real_pieces) {
						reserved_pieces -= real_pieces;
						continue;						
					} else {
						real_pieces -= reserved_pieces;	
						reserved_pieces = 0;
					}					
				}				
				c_order.add(rs.getInt(1));
				is_associated.add(rs.getInt(2));				
				qtyonhands.add(real_pieces);		
			}			
			rs.close();
			ps.close();
			int reserved = 0;			
		    for (int i = 0; i < c_order.size() ; i++) {		    	
		    	if (qty == 0)
		    		break;
		    	reserved = qtyonhands.get(i);			    	
		    	if (is_associated.get(i) > 0 ) { //Is associated
		    		result.add(c_order.get(i));
		    	}
		    	if (qty >= reserved) {
		    		qty -= reserved;		    	
		    	} else {
		    		qty = 0;
		    	}		    	
		    }
			return result;
			
		} catch (SQLException e) {
			ADialog.error(window, frame, "XX_DatabaseAccessError");
			e.printStackTrace();			
		} 
		return null;
	}
	
	public Informacion crearInfo() {
		return this.new Informacion();
	}
	
	public class Informacion {
		
		int producto = 0;		
		int consecutivo = 0;
		int almacen = 0;
		int locator = 0;
		int lote = 0;
	
		BigDecimal cantidadDisponible = null;
		BigDecimal cantidadInventario = null;
		BigDecimal cantidadStorage = null;		
		boolean correcto = false;
		
				
		/** Retorna la cantidad maxima a traspasar de un producto para un consecutivo*/
		public String cantidadProductoConsecutivo() {
			
			correcto = false;
			if ((producto == 0)  || 
					(almacen == 0) || 
					    (locator == 0)) {				
				//Alguno de los campos necesarios no está presente				
				return Msg.translate(Env.getCtx(), "XX_ProductPConsecNotFound");			
			}			
			
			//Hallar con el lote la cantidad disponible en el xx_vcn_inventory del mes -año
			//Calendar actualDate = Calendar.getInstance();
			//int year = actualDate.get(Calendar.YEAR);
			//int month = actualDate.get(Calendar.MONTH) + 1;
			
			/*Buscar en el inventario de tienda la cantidad maxima
			String sql_inventory = 
				"SELECT SUM(IV.XX_INITIALINVENTORYQUANTITY + IV.XX_SHOPPINGQUANTITY " +
						" - IV.XX_SALESQUANTITY + IV.XX_MOVEMENTQUANTITY + IV.XX_ADJUSTMENTSQUANTITY) QTY" +
						" FROM XX_VCN_INVENTORY IV " +
						" WHERE M_WAREHOUSE_ID = " + almacen +
						" AND M_PRODUCT_ID = " + producto +
						" AND ISACTIVE = 'Y' " +
						" AND XX_INVENTORYMONTH = " + month +
						" AND XX_INVENTORYYEAR = " + year; 
			if (consecutivo > 0)
				sql_inventory += " AND XX_CONSECUTIVEPRICE = " + consecutivo;			
			try {
				PreparedStatement ps = DB.prepareStatement(sql_inventory, null);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					cantidadInventario = rs.getBigDecimal(1);										
				}
				rs.close();
				ps.close();
			} catch (SQLException e) {				
				return Msg.translate(Env.getCtx(), "XX_DatabaseAccessError");
			}	
			
			if (cantidadInventario == null || cantidadInventario.compareTo(Env.ZERO) == -1) {
				cantidadInventario = Env.ZERO;				
			}*/
							
			//Buscar la cantidad real en el mstorage
			String sql_storage = "SELECT SUM (QTY)  FROM M_STORAGEDETAIL STO JOIN XX_VMR_PRICECONSECUTIVE PR " +
					" ON (PR.M_ATTRIBUTESETINSTANCE_ID = STO.M_ATTRIBUTESETINSTANCE_ID)" +
					" AND STO.M_LOCATOR_ID =  " + locator +
		  			" AND STO.M_PRODUCT_ID =  " + producto +
		  			" AND STO.M_AttributeSetInstance_ID>=0" +
					" AND STO.M_lOCATOR_ID >= 0" +
					" AND STO.QTYTYPE = '"+X_Ref_Quantity_Type.ON_HAND.getValue()+"'";
			if (consecutivo > 0)
				sql_storage += " AND PR.XX_PRICECONSECUTIVE = " + consecutivo;
			try {
				PreparedStatement ps = DB.prepareStatement(sql_storage, null);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					cantidadStorage = rs.getBigDecimal(1);										
				}
				rs.close();
				ps.close();
			} catch (SQLException e) {				
				return Msg.translate(Env.getCtx(), "XX_DatabaseAccessError");
			}
			if (cantidadStorage == null || cantidadStorage.compareTo(Env.ZERO) == -1) {
				cantidadStorage = Env.ZERO;
			}
				
			//TODO VOLVER A COLOCAR Y BORRAR
			cantidadDisponible = cantidadStorage;
			
			/*Verificar que la cantidad disponible sea la menor entre ambas cantidades
			if (cantidadInventario.compareTo(cantidadStorage) == -1) {
				cantidadDisponible = cantidadInventario;				
			} else {
				cantidadDisponible = cantidadStorage;
			}*/
			
			cantidadDisponible = cantidadDisponible.setScale(0);
			//Indicar que todo esta correcto
			correcto = true;	
			return "";
			
		}

		public BigDecimal getCantidadDisponible() {
			return cantidadDisponible;
		}

		public boolean isCorrecto() {
			return correcto;
		}


		public void setProducto(int producto) {
			this.producto = producto;
		}


		public void setConsecutivo(int consecutivo) {
			this.consecutivo = consecutivo;
		}


		public void setAlmacen(int almacen) {
			this.almacen = almacen;
		}


		public void setLocator(int locator) {
			this.locator = locator;
		}


		public void setLote(int lote) {
			this.lote = lote;
		}

		@Override
		public String toString() {
			return "Informacion [cantidadDisponible=" + cantidadDisponible
					+ ", cantidadInventario=" + cantidadInventario
					+ ", cantidadStorage=" + cantidadStorage + ", consecutivo="
					+ consecutivo + ", correcto=" + correcto + ", locator="
					+ locator + ", lote=" + lote + ",producto=" + producto + ", almacen="
					+ almacen + "]";
		}
	}
	
}

	