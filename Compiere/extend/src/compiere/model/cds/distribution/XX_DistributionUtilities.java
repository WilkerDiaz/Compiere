package compiere.model.cds.distribution;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import org.compiere.apps.ADialog;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

import compiere.model.cds.MInOut;
import compiere.model.cds.MOrder;
import compiere.model.cds.MProduct;
import compiere.model.cds.X_XX_VMR_DistribProdPerStore;
import compiere.model.cds.X_XX_VMR_DistribProductDetail;
import compiere.model.cds.X_XX_VMR_PO_DistribDetail;
import compiere.model.cds.X_XX_VMR_PO_ProductDistrib;
import compiere.model.cds.processes.XX_BatchNumberInfo;

	public class XX_DistributionUtilities { 
		
		//Almacena para facil acceso el atributo tall
		final static int ATRIBUTO_TALLA = Env.getCtx().getContextAsInt("#XX_L_M_ATTRIBUTESIZE_ID");
		
		/**
		 * Prepara la curva de tallas de una cabecera de distribucion
		 * */
		public static void prepararCurvaDeTallas( int XX_VMR_DistributionHeader_ID,
				Hashtable<Integer, Hashtable<Integer, Integer> > size_references) {
			
			//Get all the values stored for the size curve
			String sql_sizecurve =
				" SELECT XX_VMR_VENDORPRODREF_ID, M_ATTRIBUTEVALUE_ID, XX_CURVEVALUE " +
				" FROM XX_VMR_SIZECURVE S JOIN XX_VMR_SIZECURVEDETAIL D " +
				" ON ( S.XX_VMR_SIZECURVE_ID = D.XX_VMR_SIZECURVE_ID) JOIN " +
				" XX_VMR_SIZECURVEREFERENCE R ON " +
				"(R.XX_VMR_SIZECURVE_ID = D.XX_VMR_SIZECURVE_ID)" +
				" WHERE S.XX_VMR_DISTRIBUTIONHEADER_ID = " + XX_VMR_DistributionHeader_ID +
				" AND XX_CURVEVALUE > -1 ";	
			
			//Calculated by references
			String sql_references = 
				" SELECT XX_VMR_VENDORPRODREF_ID, M_ATTRIBUTEVALUE_ID, XX_CURVEVALUE  " +
				" FROM XX_VMR_SIZECURVE S JOIN XX_VMR_SIZECURVEREFDETAIL D " +
				" ON (S.XX_VMR_SIZECURVE_ID = D.XX_VMR_SIZECURVE_ID) " +
				" WHERE S.XX_VMR_DISTRIBUTIONHEADER_ID = " + XX_VMR_DistributionHeader_ID +
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
				ADialog.error(1, new Container(), "XX_DatabaseAccessError");
				e.printStackTrace();
			}
		}
		
		
		/** Llena los campos de una Distribucion de OC */ 
		public static void configurarDistribucionOC (
				int cabeceraID, ResultSet rs_a, X_XX_VMR_PO_ProductDistrib distribution) 
				throws SQLException {
			
			distribution.setXX_VMR_DistributionHeader_ID(cabeceraID);	
			distribution.setM_Product_ID(rs_a.getInt("M_PRODUCT"));			
			MProduct mProductAux = new MProduct(Env.getCtx(), rs_a.getInt("M_PRODUCT"), null);
			distribution.setM_AttributeSet_ID(mProductAux.getM_AttributeSet_ID());
			distribution.setM_AttributeSetInstance_ID(mProductAux.getM_AttributeSetInstance_ID());
			distribution.setXX_VMR_PO_LineRefProv_ID(rs_a.getInt("XX_VMR_PO_LINEREFPROV_ID"));
			distribution.setPriceActual(rs_a.getBigDecimal("PriceActual"));
			distribution.setXX_SalePrice(rs_a.getBigDecimal("XX_SALEPRICE"));
			distribution.setXX_TaxAmount(rs_a.getBigDecimal("XX_TaxAmount"));
			distribution.setXX_SalePricePlusTax(rs_a.getBigDecimal("XX_SalePricePlusTax"));
			distribution.setC_TaxCategory_ID(rs_a.getInt("C_TaxCategory_ID"));				
			distribution.setXX_Margin(rs_a.getBigDecimal("XX_Margin"));
			distribution.setXX_UnitPurchasePrice(rs_a.getBigDecimal("XX_UnitPurchasePrice"));
			distribution.setXX_DistributedQTY(rs_a.getInt("XX_QTY"));
			distribution.setXX_CanSetDefinitive(true);
			if(rs_a.getString("XX_ISDEFINITIVE").equals("Y"))
				distribution.setXX_IsDefinitive(true);
			else
				distribution.setXX_IsDefinitive(false);		
		}
		
		/** Llena los campos de un detalle de distribucion de Inventario*/
		public static boolean configurarDistribucionInv (
				ResultSet rs_a, X_XX_VMR_DistribProductDetail productoDet, MProduct producto )
				throws SQLException {
			
			
			productoDet.setXX_VMR_DistributionDetail_ID(rs_a.getInt("XX_VMR_DISTRIBUTIONDETAIL_ID"));
			productoDet.setM_Product_ID(rs_a.getInt("M_PRODUCT"));
			productoDet.setXX_Quantity(rs_a.getInt("XX_QTY"));			
			boolean esCorrecto = XX_BatchNumberInfo.ProductCostOrigin(rs_a.getInt("M_PRODUCT"), rs_a.getInt("XX_QTY"), productoDet);
			productoDet.setM_AttributeSet_ID(producto.getM_AttributeSet_ID());
			productoDet.setM_AttributeSetInstance_ID(producto.getM_AttributeSetInstance_ID());
			productoDet.setXX_CanSetDefinitive(true);
			return esCorrecto;
		}
		
		/** Indica si la fecha es correcta dependiendo si es mayor que la fecha actual */
		public static boolean fechaMayorActual (int mes, int año) {
			
			Calendar cal = Calendar.getInstance();
			int añoActual = cal.get(Calendar.YEAR);
			int mesActual = cal.get(Calendar.MONTH) + 1;
			if (año == añoActual) {
				if (mes >= mesActual) {
					return true;				
				}				
			} else if  (año > añoActual) {		
				return true;					
			}
			return false;	
		}
		
		/** Indica si la fecha es correcta dependiendo si es mayor que la fecha actual */
		public static boolean fechaMenorActual (int mes, int año) {
			
			Calendar cal = Calendar.getInstance();
			int añoActual = cal.get(Calendar.YEAR);
			int mesActual = cal.get(Calendar.MONTH) + 1;
			if (año == añoActual) {
				if (mes <= mesActual) {
					return true;				
				}				
			} else if  (año < añoActual) {		
				return true;					
			}
			return false;	
		}
		
		
		/** Verifica si una orden de compra ha sido chequeada */
		public static boolean verificaPOTieneRecepcion(Integer PO){
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
				devuelve = false;
			}	
			return devuelve;
		}
		
		/** Verifica si una orden de compra ha sido chequeada en caso de Ajuste Automático */
		public static boolean verificaPOTieneRecepcionAuto(Integer PO){
			boolean devuelve = false;
			String sql = "SELECT * " +
						 "FROM M_INOUT IO " +
						 "WHERE IO.C_ORDER_ID = "+PO;
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
				devuelve = false;
			}	
			return devuelve;
		}
		
		/** Elimina los detalles de distribucion por tienda de un producto de una dISTRIBUCION */
		public static void eliminarDetallesDistribucion (int distribucionID, int ordenId, Trx nombreTrx) {
			
			String SQL_t = "";
			if (ordenId > 0) {
				SQL_t = " DELETE FROM XX_VMR_PO_DISTRIBDETAIL WHERE " +
					" XX_VMR_PO_PRODUCTDISTRIB_ID IN (SELECT XX_VMR_PO_PRODUCTDISTRIB_ID " +
					" FROM XX_VMR_PO_PRODUCTDISTRIB WHERE " +
					" XX_VMR_DISTRIBUTIONHEADER_ID =  " + distribucionID + " ) "; 
			} else {
				SQL_t = " DELETE FROM XX_VMR_DISTRIBPRODPERSTORE WHERE XX_VMR_DISTRIBPRODUCTDETAIL_ID " +
					" IN ( SELECT PR.XX_VMR_DISTRIBPRODUCTDETAIL_ID " +
					" FROM XX_VMR_DISTRIBPRODUCTDETAIL PR JOIN XX_VMR_DISTRIBUTIONDETAIL DET ON  " +
					" PR.XX_VMR_DISTRIBUTIONDETAIL_ID = DET.XX_VMR_DISTRIBUTIONDETAIL_ID " +
					" WHERE DET.XX_VMR_DISTRIBUTIONHEADER_ID = " + distribucionID + ") ";				
			}	
			
		    try {
		    	DB.executeUpdate(nombreTrx, SQL_t);
		    } catch(Exception e)
		    {
				e.printStackTrace();
				ADialog.error(0, new Container(), "XX_DatabaseAccessError");
		    }
					
		}
		
		/** Elimina las distribuciones de productos por tienda de un detalle de distribucion específico */
		public static void eliminarDetalle(int detalle_ID, Trx nombreTrx) {
			String SQL_t = "";
			
			SQL_t = " DELETE FROM XX_VMR_DISTRIBPRODPERSTORE WHERE XX_VMR_DISTRIBPRODUCTDETAIL_ID " +
					" IN ( SELECT PR.XX_VMR_DISTRIBPRODUCTDETAIL_ID " +
					" FROM XX_VMR_DISTRIBPRODUCTDETAIL PR JOIN XX_VMR_DISTRIBUTIONDETAIL DET ON  " +
					" PR.XX_VMR_DISTRIBUTIONDETAIL_ID = DET.XX_VMR_DISTRIBUTIONDETAIL_ID " +
					" WHERE DET.XX_VMR_DISTRIBUTIONDETAIL_ID = " + detalle_ID + ") ";	
			
		    try {
		    	DB.executeUpdate(nombreTrx, SQL_t);
		    } catch(Exception e)
		    {
				e.printStackTrace();
				ADialog.error(0, new Container(), "XX_DatabaseAccessError");
		    }				
			
		}
		
		/** Elimina las distribuciones de productos por tienda de un detalle de predistribuida específico */
		public static void eliminarDetalleOC(int detalle_ID, Trx nombreTrx) {
			String SQL_t = "";
			
			SQL_t = " DELETE FROM XX_VMR_PO_DISTRIBDETAIL WHERE " +
			" XX_VMR_PO_PRODUCTDISTRIB_ID  = " +detalle_ID + " "; 
			
			PreparedStatement pstmt_t = DB.prepareStatement(SQL_t, nombreTrx);			
			try { 
				//Executing the SQL QUERIES							
				pstmt_t.executeUpdate();
				pstmt_t.close();
			} catch (SQLException e) {
				e.printStackTrace();
				ADialog.error(0, new Container(), "XX_DatabaseAccessError");
			}					
			
		}
		
		/** Prepara una tabla de hash con los productos previamente distribuidos, de esta manera pudiendolos reutilizar */
		public static void buscarDetallesDeProductos (int cabeceraID, MOrder orden, Hashtable< Integer, Integer> viejosProductos) {
			
			String sqlViejos = "";
			if (orden != null) {
				sqlViejos = " SELECT M_PRODUCT_ID, XX_VMR_PO_PRODUCTDISTRIB_ID FROM XX_VMR_PO_PRODUCTDISTRIB " +
						" WHERE XX_VMR_DISTRIBUTIONHEADER_ID = " + cabeceraID ;
			} else {
				sqlViejos = " SELECT M_PRODUCT_ID, XX_VMR_DISTRIBPRODUCTDETAIL_ID  " +
					" FROM XX_VMR_DISTRIBPRODUCTDETAIL WHERE XX_VMR_DISTRIBUTIONDETAIL_ID IN " +
					"( SELECT XX_VMR_DISTRIBUTIONDETAIL_ID FROM XX_VMR_DISTRIBUTIONDETAIL " +
					" WHERE XX_VMR_DISTRIBUTIONHEADER_ID = " + cabeceraID + " )" ;
			}
			PreparedStatement pstmt_previous = DB.prepareStatement(sqlViejos, null);
			try {			
				ResultSet rs_previous = pstmt_previous.executeQuery();
				while (rs_previous.next()) {
					viejosProductos.put(rs_previous.getInt(1), rs_previous.getInt(2));
				}
				rs_previous.close();
				pstmt_previous.close();
			} catch (SQLException e1) {			
				e1.printStackTrace();
				ADialog.error(0, new Container(), "XX_DatabaseAccessError");
			}
		}
		
		/** Aplica los porcentajes a la distribucion 
		 * @author ghuchet */
		public static void aplicarPorcentajes (int distribucionProductoID, MOrder orden, Vector<Integer> warehouses, 
				Vector<? extends Number> percentages, int xx_quantity, Vector<Integer> priorityToAdd, Trx nombreTrx, 
				Ctx ctx, int divisor, int xx_package, int distPrev) {

			//No hay piezas, continuar
			if (xx_quantity == 0) return;
			
			int distributed_quantity = 0, total = 0;				
			Vector<Integer> qtys = new Vector<Integer>();		
			//Se obtiene un vector con los indice de las tiendas en 
			//orden de mayor a menor prioridad para agregar en caso de ser necesario
			Vector<Integer> priorityToAddDecimal = 
					XX_StoreAmounts.obtPrioridadesDecimalAgregar(percentages, xx_quantity, divisor, xx_package,  distPrev);
			float percentage = 0;
			
			//Inicializar vector de cantidades para cada tienda
			for (int i = 0; i < warehouses.size(); i++) {	
				 qtys.add(i,0);
			}
			
			
			//Se distribuye un paquete a cada tienda dando prioridad a las tiendas de mayor porcentaje 
			//(en el caso de ajuste automático el campo distPrev es 0)
			for (int i = 0; i < warehouses.size(); i++) {	
				if(total + distPrev*xx_package <= xx_quantity){
					qtys.set(priorityToAdd.get(i), distPrev*xx_package);
					total = total + distPrev*xx_package;
				}else break;
			}
			
			//Se distribuyen las piezas faltantes a cada tienda según su porcentaje asociado.
			if(total < xx_quantity){
				for (int i = 0; i < warehouses.size(); i++) {												
					//Calcula la cantidad de piezas que le corresponde a la tienda segun el porcentaje asociado										
					percentage = percentages.elementAt(i).floatValue()/divisor;
					distributed_quantity = ((int)(xx_quantity*percentage/xx_package)) - distPrev;
					distributed_quantity = distributed_quantity*xx_package ;
					if(distributed_quantity>0){
						qtys.set(i,qtys.elementAt(i) + distributed_quantity);
						total = total + distributed_quantity;	
					}
					
				}
				//Se distribuye el sobrante usando el vector que tiene el orden 
				//de las tiendas según la prioridad para agregarle más piezas
				if (total < xx_quantity){ 	
					int to_add =  xx_quantity - total ;
					int add = xx_package;
					
					//Se agrega un paquete a cada tienda en el orden de prioridad hasta que se acaben las piezas.
					while(to_add>0){
						for (int i = 0 ; i < qtys.size()  ; i++) { 	
							if ( to_add < add ) {
								add = to_add;
							} 
							qtys.set(priorityToAddDecimal.get(i), qtys.elementAt(priorityToAddDecimal.get(i)) + add);
							to_add = to_add - add;
							if (to_add == 0) break;
						}
					}
				}	
			}
			/*Se agregan la cantidad de piezas correspondientes a cada almacén a la distribución*/
			//Predistribuida
			if (orden != null) {
				//Es una distribucion de orden de compra
				for (int i = 0; i < warehouses.size(); i++) {
					X_XX_VMR_PO_DistribDetail detail = new X_XX_VMR_PO_DistribDetail(ctx, 0, nombreTrx);								
					
					detail.setXX_VMR_PO_ProductDistrib_ID(distribucionProductoID);												

					if (qtys.elementAt(i) != 0) {
						detail.setM_Warehouse_ID(warehouses.get(i));
						detail.setXX_ProductQuantity(qtys.elementAt(i));						
						detail.save(nombreTrx);
					}						
					
				}		
			}else {			
				//Redistribucion		
				for (int i = 0; i < warehouses.size(); i++) {
					
					if (qtys.elementAt(i) == 0) continue; 
					
					X_XX_VMR_DistribProdPerStore prodPerStore = new X_XX_VMR_DistribProdPerStore(Env.getCtx(),0,null);
					prodPerStore.setXX_VMR_DistribProductDetail_ID(distribucionProductoID);											
					prodPerStore.setM_Warehouse_ID(warehouses.get(i));
					prodPerStore.setXX_Quantity(qtys.elementAt(i));	
					prodPerStore.save(nombreTrx);
					}	
			}	
		} 
		
		/** Aplica los porcentajes a la distribucion cuando esta posee curva de tallas o el múltiplo de empaque es mayor a 1 
		 * (No se esta usando actualmente)
		 * @author ghuchet */
		public static void aplicarPorcentajesCurva (int distribucionProductoID, MOrder orden, Vector<Integer> warehouses, 
				Vector<? extends Number> percentages, int xx_quantity, int curve_value, int xx_package, 
				Hashtable<Integer, Integer> size_curve, Vector<Integer> priorityToAdd, Vector<Integer> priorityToCutDown,
				Trx nombreTrx, Ctx ctx, int divisor, int  Warehouse_CD) {

			//No hay piezas, continuar
			if (xx_quantity == 0) return;
			
			boolean curve_compliance = true;
			int distributed_quantity = 0, total = 0;
			
			Vector<Integer> qtys = new Vector<Integer>();
			
			//For each warehouse
			for (int i = 0; i < warehouses.size(); i++) {	
				 qtys.add(i,0);
			}
			
			total = 0;
			float percentage = 0;
			int to_cd = 0;
			
			/* Si tiene curva de tallas*/
			if (size_curve != null) {
				if(curve_value ==0){
					to_cd = xx_quantity;
					total = xx_quantity;
				}
				else{
				/* Asegura que se distribuya al menos una curva de talla a cada tienda (siempre que hayan piezas suficientes)*/
				for (int i = 0; i < priorityToAdd.size(); i++) {	
					distributed_quantity =0;
					if (curve_value > 0) {		
						distributed_quantity = curve_value;		
					}
					qtys.set(priorityToAdd.get(i),distributed_quantity);
					total = total + distributed_quantity;	
				}			
				/*Si el total distribuido a las tiendas es menor al la cantidad a distribuir*/
				if(total<xx_quantity){
					for (int i = 0; i < warehouses.size() && total<xx_quantity; i++) {												
						
						//Calcula la cantidad de piezas que le corresponde a la tienda segun el porcentaje asociado										
						percentage = percentages.elementAt(priorityToAdd.get(i)).floatValue()/divisor;
						distributed_quantity = Math.round(xx_quantity*percentage);	
						Integer qtyAdded = qtys.get(priorityToAdd.get(i));
						/*Si la cantidad asignada previamente a la tienda es menor al porcentaje asociado, se puede agregar piezas*/
						if(qtyAdded<distributed_quantity && curve_value<=distributed_quantity){
							distributed_quantity =  distributed_quantity - qtyAdded;
							//Set the distribution according to the algorithm described in the function
							if (curve_value > 0) {		
								int remainder = distributed_quantity%curve_value;	
								if (remainder > 0) {
									int aux = curve_value - remainder;
									if (aux <= 1) distributed_quantity =  distributed_quantity + aux;
									else distributed_quantity =  distributed_quantity - remainder;														
								} 
							}
							qtys.set(priorityToAdd.get(i),distributed_quantity+qtyAdded);
							total =total + distributed_quantity;	
						}
					}
				}
				}
				/*Si no tiene curva de talla*/
			}else { 
				for (int i = 0; i < warehouses.size(); i++) {												
					
					//Calcula la cantidad de piezas que le corresponde a la tienda segun el porcentaje asociado										
					percentage = percentages.elementAt(i).floatValue()/divisor;
					//Si tiene multiplo de empaque y no se ignora 
					distributed_quantity = Math.round(xx_quantity*percentage);
					if (xx_package > 1) {
						distributed_quantity = Math.round(distributed_quantity/xx_package);
						distributed_quantity = distributed_quantity*xx_package;				
					}
					qtys.set(i,distributed_quantity);
					total = total + distributed_quantity;	
				}
			}
			
			float wSize = warehouses.size();
			if (total > xx_quantity ) {  			
				int to_remove = total - xx_quantity;
				float temp;
				int remove = 1;
				if (xx_package > 1){
					remove = xx_package; 
					to_remove = (int) Math.ceil(to_remove/xx_package);
					to_remove = to_remove*xx_package;
					to_cd = to_remove - (total - xx_quantity);
				}else {
					temp = to_remove/wSize;
					remove =(int) Math.ceil(temp);
				}
				
				boolean seguir = true;
				if (size_curve != null && curve_value>1) {	
					while(seguir){
						seguir = false;
						for (int i = 0 ; i < qtys.size()  ; i++) { 	
							if ( to_remove < remove ) {
								remove = to_remove;
							} 
							if ((qtys.elementAt(priorityToCutDown.get(i)) - remove)%curve_value == 0 
									&& (qtys.elementAt(priorityToCutDown.get(i)) - remove) >= curve_value) {
								qtys.set(priorityToCutDown.get(i), qtys.elementAt(priorityToCutDown.get(i)) - remove);
								to_remove = to_remove - remove;
								seguir = true;
							}
							if (to_remove < 1) {
								seguir = false;
								break;
							}
						}
					}
					seguir = true;
					while(to_remove>0 && seguir){
						seguir = false;
						for (int i = 0 ; i < qtys.size()  ; i++) { 	
							if ( to_remove < remove ) {
								remove = to_remove;
							} 
							if ((qtys.elementAt(priorityToCutDown.get(i)) - remove) >= curve_value) {
								qtys.set(priorityToCutDown.get(i), qtys.elementAt(priorityToCutDown.get(i)) - remove);
								to_remove = to_remove - remove;
								seguir = true;
							}
							if (to_remove < 1) {
								seguir = false;
								break;
							}
						}
					}
				}
				seguir = true;
				while(to_remove>0 && seguir){
					seguir = false;
					for (int i = 0 ; i < qtys.size()  ; i++) { 			
						if ( to_remove < remove && xx_package == 1) {
							remove = to_remove;
						} 
						//Tratando de dejar al menos un multiplo de empaque a cada tienda
						if ((qtys.elementAt(priorityToCutDown.get(i)) - remove) >= xx_package) {
							qtys.set(priorityToCutDown.get(i), qtys.elementAt(priorityToCutDown.get(i)) - remove);
							to_remove = to_remove - remove;
							seguir = true;
						}
						if (to_remove < 1) {
							seguir = false;
							break;
						}
					}
				}
				if(to_remove>0){
					for (int i = 0 ; i < qtys.size()  ; i++) { 			
						if ( to_remove < remove && xx_package == 1) {
							remove = to_remove;
						} 
						//Si aun falta por remover, se remueven los restantes sin importar si queda algo en la tienda
						if ( qtys.elementAt(priorityToCutDown.get(i)) <  remove ) {
							to_remove = to_remove - qtys.elementAt(priorityToCutDown.get(i));
							qtys.set(priorityToCutDown.get(i), 0 );
						} else {								
							qtys.set(priorityToCutDown.get(i), qtys.elementAt(priorityToCutDown.get(i)) - remove);
							to_remove = to_remove - remove;
						}
						if (to_remove  < 1) break;
					}
				}
				curve_compliance = false;
			} else if (total < xx_quantity ){ 	
				int to_add =  xx_quantity - total ;
				float temp;
				int add = 1;
				
				if (xx_package > 1){
					add = xx_package; 
					to_add = (int) (to_add/xx_package);
					to_add = to_add*xx_package;
					to_cd = (xx_quantity - total) - to_add;
				}else {
					temp = to_add/wSize;
					add = (int) Math.ceil(temp);
				}
				//Start add from stores 
				while(to_add>0){
					for (int i = 0 ; i < qtys.size()  ; i++) { 	
						if ( to_add < add ) {
							add = to_add;
						} 
						qtys.set(priorityToAdd.get(i), qtys.elementAt(priorityToAdd.get(i)) + add);
						to_add = to_add - add;
						if (to_add == 0) break;
					}
				}
				curve_compliance = false;
			}	
			
			/*Se agregan a las piezas correspondientes a cada almacen */
			if (orden != null) {
				//Es una distribucion de orden de compra
				for (int i = 0; i < warehouses.size(); i++) {
					X_XX_VMR_PO_DistribDetail detail = new X_XX_VMR_PO_DistribDetail(ctx, 0, nombreTrx);								
					
					detail.setXX_VMR_PO_ProductDistrib_ID(distribucionProductoID);												
								
					if (curve_value == 0) {
						detail.setXX_SizeCurveCompliance(false);						
					} else {
						//If the qty was cut down						
						if (!curve_compliance && qtys.elementAt(i)%curve_value !=0) {
							detail.setXX_SizeCurveCompliance(false);
						} else detail.setXX_SizeCurveCompliance(true);						
					}
					
					//CD 	
					if(warehouses.get(i) == Warehouse_CD){
						detail.setM_Warehouse_ID(Warehouse_CD);
						detail.setXX_ProductQuantity(to_cd+qtys.elementAt(i));						
						detail.save(nombreTrx);
						to_cd = 0;
					}
					//No associated quantity to distribute
					else if (qtys.elementAt(i) != 0) {
						detail.setM_Warehouse_ID(warehouses.get(i));
						detail.setXX_ProductQuantity(qtys.elementAt(i));						
						detail.save(nombreTrx);
					}						
					
				}		
				if(to_cd>0) {
					X_XX_VMR_PO_DistribDetail detail = new X_XX_VMR_PO_DistribDetail(ctx, 0, nombreTrx);								
									
					detail.setXX_VMR_PO_ProductDistrib_ID(distribucionProductoID);											
					
					if (curve_value == 0) {
						detail.setXX_SizeCurveCompliance(false);						
					} else {
						detail.setXX_SizeCurveCompliance(true);			
					}			 				
					detail.setM_Warehouse_ID(Warehouse_CD);
					detail.setXX_ProductQuantity(to_cd);						
					detail.save(nombreTrx);
				}
			} else {			
				//Es una redistribucion		
				for (int i = 0; i < warehouses.size(); i++) {
					
					if (qtys.elementAt(i) == 0) continue; 
					
					X_XX_VMR_DistribProdPerStore prodPerStore = new X_XX_VMR_DistribProdPerStore(Env.getCtx(),0,null);
					prodPerStore.setXX_VMR_DistribProductDetail_ID(distribucionProductoID);											

					
					if (curve_value == 0) 
						prodPerStore.setXX_SizeCurveCompliance(false);
					else {
						//If the qty was cut down						
						if (!curve_compliance && qtys.elementAt(i)%curve_value !=0) {
							prodPerStore.setXX_SizeCurveCompliance(false);
						} else prodPerStore.setXX_SizeCurveCompliance(true);
					}
					//CD 	
					if(warehouses.get(i) == Warehouse_CD){
						prodPerStore.setM_Warehouse_ID(warehouses.get(i));
						prodPerStore.setXX_Quantity(to_cd+qtys.elementAt(i));	
						prodPerStore.save(nombreTrx);
						to_cd = 0;
					}
					//No associated quantity to distribute
					else if (qtys.elementAt(i) != 0) { 
						prodPerStore.setM_Warehouse_ID(warehouses.get(i));
						prodPerStore.setXX_Quantity(qtys.elementAt(i));	
						prodPerStore.save(nombreTrx);
					}	
				}	
				
				/*Se agregan a CD las piezas sobrantes de productos 
				 * que no cumplen con el multiplo de empaque, o si la curva de tallas es 0*/
				if(to_cd>0) {
					
					X_XX_VMR_DistribProdPerStore prodPerStore =
						new X_XX_VMR_DistribProdPerStore(Env.getCtx(),0,null);
					
					prodPerStore.setXX_VMR_DistribProductDetail_ID(distribucionProductoID);		
					
					if (curve_value == 0) 
						prodPerStore.setXX_SizeCurveCompliance(false);
					else {
						prodPerStore.setXX_SizeCurveCompliance(true);
					}
					//CD 	
						prodPerStore.setM_Warehouse_ID(Warehouse_CD);
						prodPerStore.setXX_Quantity(to_cd);	
						prodPerStore.save(nombreTrx);
					
				}	
			}
		} 

	}

