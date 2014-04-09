package compiere.model.cds.processes;
  
import java.awt.Container;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.apps.ADialog;
import org.compiere.apps.ProcessCtl;
import org.compiere.common.constants.EnvConstants;
import org.compiere.model.MPInstance;
import org.compiere.model.X_M_Warehouse;
import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import bsh.util.Util;


import compiere.model.cds.MOrder;
import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRCriticalTaskForClose;
import compiere.model.cds.MVMRDistribProductDetail;
import compiere.model.cds.MVMRDistributionHeader;
import compiere.model.cds.MVMROrder;
import compiere.model.cds.MVMRPOProductDistrib;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_DistributionStatus;
import compiere.model.cds.X_Ref_XX_OrderRequestType;
import compiere.model.cds.X_Ref_XX_VMR_OrderStatus;
import compiere.model.cds.X_XX_VLO_PlacedOrderAs;
import compiere.model.cds.X_XX_VMR_DistribDetailTemp;
import compiere.model.cds.X_XX_VMR_HeaderAssociation;
import compiere.model.cds.X_XX_VMR_OrderRequestDetail;
import compiere.model.cds.X_XX_VMR_StoredProduct;


public class XX_ApproveDistribution extends SvrProcess{

	@Override
	protected String doIt() throws Exception {
	
		MVMRDistributionHeader distribucion_aprobada = 
			new MVMRDistributionHeader(getCtx(),getRecord_ID(),get_TrxName());
		return approve_distribution(distribucion_aprobada);
	}
	
	private boolean checkSalePrice(MVMRDistributionHeader distribucion_aprobada){
		
		String sqlPrice  = "";
		
		int id_orden = distribucion_aprobada.getC_Order_ID();
		
		if(id_orden == 0){
			
			sqlPrice = "select * from XX_VMR_DistribProductDetail " +
					   "where XX_SALEPRICEPLUSTAX < 1 and " +
					   "XX_VMR_DISTRIBUTIONDETAIL_ID IN " +
					   "(SELECT XX_VMR_DISTRIBUTIONDETAIL_ID " +
					   "FROM XX_VMR_DISTRIBUTIONDETAIL " +
					   "WHERE XX_VMR_DISTRIBUTIONHEADER_ID = " + distribucion_aprobada.get_ID() +")";
		}
		else
		{
			sqlPrice = "select * from XX_VMR_PO_ProductDistrib " +
					  "where XX_SALEPRICEPLUSTAX < 1 and " +
					  "XX_VMR_DISTRIBUTIONHEADER_ID = " + distribucion_aprobada.get_ID();
			
		}
		
		PreparedStatement prst_def = DB.prepareStatement(sqlPrice, null);
		boolean priceError = false;
		
		try {
			ResultSet rs_def = prst_def.executeQuery();
			if (rs_def.next() ) {
				priceError = true;
			}
			rs_def.close();
			prst_def.close();
		} 
		catch (SQLException e){
			e.printStackTrace();
			ADialog.error(1, new Container(), "XX_DatabaseAccessError");
		}
		
		return priceError;
	}
		
	public String approve_distribution (MVMRDistributionHeader distribucion_aprobada) {
		
		if(distribucion_aprobada.getXX_DistributionStatus().equals(X_Ref_XX_DistributionStatus.PENDIENTE_POR_FIJAR_PRECIOS_DEFINITIVOS))
			if(checkSalePrice(distribucion_aprobada)){
				ADialog.error(1, new Container(), "Precio de venta menor a 1 en alguno de los productos.");
				return "Precio de venta menor a 1 en alguno de los productos.";
			}
		
		int id_orden = distribucion_aprobada.getC_Order_ID();

		if (distribucion_aprobada.getXX_DistributionStatus().equals(X_Ref_XX_DistributionStatus.APROBADA.getValue())) {
			//Evita que se generen pedidos dos veces si ya está aprobada.
			return "";
		} else if (distribucion_aprobada.getXX_DistributionStatus().equals(X_Ref_XX_DistributionStatus.APROBADA__PENDIENTE_POR_CHEQUEO_DE_LA_OC.getValue())) {
			//Evita que se generen pedidos dos veces si ya está aprobada esperando por chequeo
			return "";
		} else if (Utilities.hasAssociatedPlacedOrders(distribucion_aprobada.get_ID())) {
			return "";
		}
		
		// Agregado por GMARQUES
		// Fecha del servidor
		Date serverDate = currentServerDate();
		
		//Si la distribucion viene de una orden de compra
		if(id_orden != 0) {
						
			if (!distribucion_aprobada.getXX_DistributionStatus().equals(X_Ref_XX_DistributionStatus.LISTA_PARA_APROBAR.getValue())) {
				
				//Si la cabecera se ha distribuido			
				if (!distribucion_aprobada.isXX_CalculatedPOSPercentages() && !distribucion_aprobada.isXX_CalculatedPOSQuantities()) {
					ADialog.error(1, new Container(), "XX_NoPercentages");
					return "";
				} 
				//Si la cabecera tiene sus precios definitivos			
				String sql_def = "SELECT SUM( " +
					" CASE P.XX_IsDefinitive" +
					" WHEN 'Y' THEN 1 " +
					" ELSE 0 " +
					" END) APP," +
					"SUM( " + 
					" CASE P.XX_IsDefinitiveIndividual " +
					"WHEN 'Y' THEN 1" +
					"ELSE 0"  +
					"END) APP2," +
					" COUNT(*) TUP FROM XX_VMR_PO_PRODUCTDISTRIB P WHERE P.XX_VMR_DistributionHeader_ID = "
					+ distribucion_aprobada.getXX_VMR_DistributionHeader_ID();
				PreparedStatement prst_def = DB.prepareStatement(sql_def, null);
				boolean definitive = false;
				try {
					ResultSet rs_def = prst_def.executeQuery();
					if (rs_def.next() ) {
						if (rs_def.getInt(1) + rs_def.getInt(2) == rs_def.getInt(3)) 
							definitive = true;
						else 
							definitive = false;
					}
					rs_def.close();
					prst_def.close();
				} catch (SQLException e){
					e.printStackTrace();
					ADialog.error(1, new Container(), "XX_DatabaseAccessError");
				}
				
				//Si la cabecera no tiene precios definitivos deben ser fijados
				if (!definitive) {		
					setLastSalePriceOC();	//AGREGADO GHUCHET
					setMinCompetitionPriceOC(); //AGREGADO GHUCHET
					distribucion_aprobada.setXX_DistributionStatus(X_Ref_XX_DistributionStatus.PENDIENTE_POR_FIJAR_PRECIOS_DEFINITIVOS.getValue());
					distribucion_aprobada.save();
					String mss = Msg.getMsg(Env.getCtx(), "XX_PendingPrizesDistribution", 
							new String[] {""+distribucion_aprobada.getXX_VMR_DistributionHeader_ID()});
					ADialog.info(1, new Container(), mss);
					
					//Realizado por Rosmaira Arvelo
					//Generacion de Alerta de Tarea Critica para Asignar precio de venta
					if((distribucion_aprobada.getC_Order_ID()!=0)&&(distribucion_aprobada.getXX_DistributionStatus().equals(X_Ref_XX_DistributionStatus.PENDIENTE_POR_FIJAR_PRECIOS_DEFINITIVOS.getValue())) && (distribucion_aprobada.isXX_Alert() == false))
					{
						try
						{
							Env.getCtx().setContext("#XX_TypeAlertAP","AP");
							Env.getCtx().setContext("#XX_POProdDistriCT",distribucion_aprobada.get_ID());
								
							Utilities.generatedAlert(distribucion_aprobada.get_ID());
						}
						catch(Exception e)
						{
							log.log(Level.SEVERE,e.getMessage());
						}
						
					}//fin RArvelo
					
					return "";					
				}
					
				//Verificar que la orden de compra ha sido chequeda
				MOrder orden = new MOrder(getCtx(), id_orden, null);  
				
				//Verificar si existen productos en la tabla de distrib product detail	
				//Se agregó que el almacen CD del temporal debe ser el mismo almacen CD de la distribucion - PROYECTO CD VALENCIA
				int producto_inters = 0;
				String sql = " SELECT T.M_PRODUCT_ID FROM XX_VMR_DISTRIBDETAILTEMP T " + 
					" JOIN XX_VMR_PO_PRODUCTDISTRIB D ON (D.M_PRODUCT_ID = T.M_PRODUCT_ID) " +
					" WHERE D.XX_VMR_DISTRIBUTIONHEADER_ID = " + distribucion_aprobada.getXX_VMR_DistributionHeader_ID()+
					" AND T.M_WAREHOUSE_ID = " +distribucion_aprobada.getM_Warehouse_ID(); 
					
				PreparedStatement ps_a = DB.prepareStatement(sql, null);
				try {
					ResultSet rs_a = ps_a.executeQuery();
					if (rs_a.next()) {
						producto_inters = rs_a.getInt(1);
					}
					rs_a.close();
					ps_a.close();									
				} catch (SQLException e) {
					ADialog.error(1, new Container(), "XX_DatabaseAccessError");
					e.printStackTrace();
				}
				//GHUCHET se comento esta parte del codigo para que permita aprobar predistribuidas 
				//aunque exista una redistribución pendiente.
//				if (producto_inters > 0) {
//					MProduct producto = new MProduct(Env.getCtx(), producto_inters, get_TrxName());
//					String mss = Msg.getMsg(Env.getCtx(), "XX_NotApprovedDistributedProduct", 
//							new String[] {
//						"" + producto.getValue(), producto.getName(), orden.getDocumentNo()});									
//					ADialog.error(1, new Container(), mss );	
//					return "";
//				}
				//FIN GHUCHET comentado
					
				if ( !orden.getXX_OrderStatus().equals("CH") ) {
					distribucion_aprobada.setXX_DistributionStatus(X_Ref_XX_DistributionStatus.APROBADA__PENDIENTE_POR_CHEQUEO_DE_LA_OC.getValue());
					distribucion_aprobada.save();
					String mss = Msg.getMsg(Env.getCtx(), "XX_AprovedDistribution", 
							new String[] {""+distribucion_aprobada.getXX_VMR_DistributionHeader_ID()});					
					ADialog.info(1, new Container(), mss);
					
					//Realizado por Rosmaira Arvelo
					MVMRCriticalTaskForClose task = new MVMRCriticalTaskForClose(
							Env.getCtx(),Utilities.getCriticalTaskForClose(distribucion_aprobada.get_ID()), get_TrxName());
						
					//Llama al proceso cerrar alerta de Asignación de precios de venta
					if(((distribucion_aprobada.getXX_DistributionStatus().equals("AC"))||
							(distribucion_aprobada.getXX_DistributionStatus().equals("AP")))
							&&(task.get_ID()!=0)&&(task.isActive()==true)&&((task.getXX_TypeTask().equals("AP"))||(task.getXX_TypeTask().equals("AP1"))))	{						
						try {
							Utilities.closeAlert(task.get_ID());
						}  catch(Exception e)	{
							log.log(Level.SEVERE,e.getMessage());
						}
					}//fin RArvelo
					
					return "";	
				}
			} else { 
				
				//Verificar que la orden de compra ha sido chequeda
				MOrder orden = new MOrder(getCtx(), id_orden, null);  
				if ( orden.getXX_OrderStatus().equals("CH") ) {				
					//Esta chequeada por lo tanto es posible crear los pedidos
					generatePlacedOrdersFromCheckedPurchaseOrder (distribucion_aprobada); 
				}
			}

		} else { 
			
			//Si es cabecera de distribucion
			XX_BatchNumberInfo info = new XX_BatchNumberInfo();
			Calendar calendar = new GregorianCalendar();
			//calendar.setTime(new Date()); // COMENTADO POR GMARQUES Fecha del servidor
			calendar.setTime(serverDate); // AGREGADO POR GMARQUES Fecha del servidor
			boolean worked = true;
			try {
				String sql_applied = 
					" SELECT SUM(  CASE XX_DISTRIBUTIONAPPLIED " +
					" WHEN 'Y' THEN 1"  +
					" ELSE 0 " +
					" END) APP, COUNT(*) TUP" +
					" FROM XX_VMR_DISTRIBUTIONDETAIL " +
					" WHERE XX_VMR_DISTRIBUTIONHEADER_ID = " + distribucion_aprobada.get_ID();
											
				PreparedStatement prst_applied = DB.prepareStatement(sql_applied, null);
				ResultSet rs_applied = prst_applied.executeQuery();				
				boolean distribuidos_todos = false;
				if (rs_applied.next()){
					if ( rs_applied.getInt(1) == rs_applied.getInt(2) ) {
						distribuidos_todos = true;						
					} else { 
						distribuidos_todos=false;
					}
				}
				rs_applied.close();
				prst_applied.close();
				if (!distribuidos_todos) {
					ADialog.error(1, new Container(), "XX_UndistributedDetails");
					return "";
				} else {
					boolean definitive = false;
					String sql_definitive = 
						" SELECT SUM(  CASE S.XX_IsDefinitive " +
						" WHEN 'Y' THEN 1"  +
						" ELSE 0 " +
						" END) APP, " +
						"SUM( " + 
						" CASE S.XX_IsDefinitiveIndividual " +
						"WHEN 'Y' THEN 1" +
						"ELSE 0"  +
						"END) APP2" +
						", COUNT(*) TUP" +
						" FROM XX_VMR_DISTRIBUTIONDETAIL D JOIN XX_VMR_DISTRIBPRODUCTDETAIL S ON " +
						" (D.XX_VMR_DISTRIBUTIONDETAIL_ID = S.XX_VMR_DISTRIBUTIONDETAIL_ID )" + 
						" WHERE D.XX_VMR_DISTRIBUTIONHEADER_ID = " + distribucion_aprobada.get_ID();					
					try {
						PreparedStatement pr_definitive = 	DB.prepareStatement(sql_definitive, null);
						ResultSet rs_definitive = pr_definitive.executeQuery();
						if (rs_definitive.next() ) {
							if (rs_definitive.getInt(1) + rs_definitive.getInt(2) == rs_definitive.getInt(3)) 
								definitive = true;
							else 
								definitive = false;
						}
						rs_definitive.close();
						pr_definitive.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					if (!definitive) {
						//Valida si las cantidades distribuidas a las tiendas 
						//son consistentes con la cantidad a distribuir del producto 	
						if(!isInconsistent(distribucion_aprobada)){  //AGREGADO GHUCHET
							setLastSalePrice();	//AGREGADO GHUCHET
							setMinCompetitionPrice(); //AGREGADO GHUCHET
							distribucion_aprobada.setXX_DistributionStatus(X_Ref_XX_DistributionStatus.PENDIENTE_POR_FIJAR_PRECIOS_DEFINITIVOS.getValue());
							distribucion_aprobada.save();					
							ADialog.info(1, new Container(), "The Distribution Header " + distribucion_aprobada.getXX_VMR_DistributionHeader_ID()
									+ " is now Pending Definitive Prices");	
													
							//Realizado por Rosmaira Arvelo
							//Generacion de Alerta de Tarea Critica para Asignar precio de venta
							if((distribucion_aprobada.getC_Order_ID()==0)&&(distribucion_aprobada.getXX_DistributionStatus().equals("FP")) && (distribucion_aprobada.isXX_Alert2() == false))
							{
								try
								{
									Env.getCtx().setContext("#XX_TypeAlertAP1","AP1");
									Env.getCtx().setContext("#XX_DistProdDetCT",distribucion_aprobada.get_ID());
									
									Utilities.generatedAlert(distribucion_aprobada.get_ID());
								}
								catch(Exception e)
								{
									log.log(Level.SEVERE,e.getMessage());
								}
								
							}//fin RArvelo
						}else {
							//Mensaje de Incosistencia de Datos en Inventario
							ADialog.error(1, new Container(), "XX_InventoryInconsistency");
						}
						return "";					
					}	
					
					HashMap<Integer, String> warehouses = new HashMap<Integer, String>();
					String sql_warehouses = "SELECT M_WAREHOUSE_ID, VALUE FROM M_WAREHOUSE";
					PreparedStatement ps_warehouses = DB.prepareStatement(sql_warehouses, get_TrxName());
					ResultSet rs_warehouses = ps_warehouses.executeQuery();
					while (rs_warehouses.next()) {
						warehouses.put(rs_warehouses.getInt(1), rs_warehouses.getString(2));						
					}					
					rs_warehouses.close();
					ps_warehouses.close();
					
					Vector<Integer> numeros_tiendas = new Vector<Integer>();				
					Vector<Integer> numero_pedido = new Vector<Integer>();
					
					//The distribution 
					String sql_detalle = " SELECT XX_VMR_DistributionDetail_ID FROM XX_VMR_DistributionDetail" +
							" WHERE XX_VMR_DistributionHeader_ID = " + distribucion_aprobada.getXX_VMR_DistributionHeader_ID();

					//Se agregó que el almacen CD del temporal debe ser el mismo almacen CD de la distribucion - PROYECTO CD VALENCIA
					String sql_producto = " SELECT P.XX_VMR_DistributionDetail_ID, S.XX_VMR_DISTRIBPRODUCTDETAIL_ID, S.XX_VMR_DISTRIBPRODPERSTORE_ID, " +
					  " S.M_WAREHOUSE_ID, S.XX_QUANTITY, P.XX_SalePrice, P.XX_TaxAmount, P.XX_SalePricePlusTax, Pr.C_TaxCategory_ID, P.XX_UnitPurchasePrice, " +
					  " P.PriceActual, PR.M_PRODUCT_ID, PR.XX_VMR_Category_ID, PR.XX_VMR_Department_ID, PR.XX_VMR_Line_ID, PR.XX_VMR_Section_ID," +
					  " TM.XX_VMR_DISTRIBDETAILTEMP_ID " +
					  " FROM XX_VMR_DISTRIBPRODUCTDETAIL P JOIN XX_VMR_DISTRIBDETAILTEMP TM ON " +
					  " (TM.XX_VMR_DISTRIBUTIONDETAIL_ID = P.XX_VMR_DISTRIBUTIONDETAIL_ID AND P.M_PRODUCT_ID = TM.M_PRODUCT_ID) " +
					  " JOIN XX_VMR_DISTRIBPRODPERSTORE S " +
					  " ON (P.XX_VMR_DISTRIBPRODUCTDETAIL_ID= S.XX_VMR_DISTRIBPRODUCTDETAIL_ID) " +
					  " JOIN M_PRODUCT PR ON (P.M_PRODUCT_ID = PR.M_PRODUCT_ID) " +
					  " WHERE TM.M_WAREHOUSE_ID = "+distribucion_aprobada.getM_Warehouse_ID()+" AND  P.XX_VMR_DistributionDetail_ID = ?"; 
					
					//Se prepara el statement de eliminacion						
					PreparedStatement prst_producto = DB.prepareStatement(sql_producto, null);
					PreparedStatement prstdetalle = DB.prepareStatement(sql_detalle, get_TrxName());					
					ResultSet rsdetalle = prstdetalle.executeQuery();
					
					
					
					while(rsdetalle.next()){
						
						prst_producto.setInt(1, rsdetalle.getInt("XX_VMR_DistributionDetail_ID"));						
						ResultSet rs_producto = prst_producto.executeQuery();
						int tienda_id = 0;
						while(rs_producto.next()) {
							info.get_put( rs_producto.getInt("M_PRODUCT_ID"),
									rs_producto.getInt("XX_VMR_DistributionDetail_ID"), get_Trx(), distribucion_aprobada.getM_Warehouse_ID());							
							XX_BatchNumberInfo.helperClass whereTO = 								
								info.data.get(
										rs_producto.getInt("M_PRODUCT_ID")
								);							
							
							tienda_id = rs_producto.getInt("M_WAREHOUSE_ID");	
							
							//Si la tienda es alguno de los CD - PROYECTO CD VALENCIA
							if (Utilities.esCD(tienda_id)) { 
								//Estos productos no deben generar pedido
								
								//Para cada detalle de distribucion temporal, disminuir la cantidad o borrarlo si no hay cantidad
								X_XX_VMR_DistribDetailTemp detail_temp = 
									new X_XX_VMR_DistribDetailTemp(Env.getCtx(), rs_producto.getInt("XX_VMR_DISTRIBDETAILTEMP_ID"), get_TrxName());
								BigDecimal temp = detail_temp.getXX_DesiredQuantity();
								temp = temp.subtract(rs_producto.getBigDecimal("XX_QUANTITY"));								
								if (temp.compareTo(Env.ZERO) > 0) {								
									detail_temp.setXX_DesiredQuantity(temp);
									detail_temp.save();
								} else {			
									String sql_delete = "DELETE FROM XX_VMR_DistribDetailTemp WHERE XX_VMR_DistribDetailTemp_id = " + detail_temp.get_ID();
									DB.executeUpdate(get_TrxName(),sql_delete);
								}								
								continue;
							}
							boolean insertar_tienda = true;
							int useful = 0, distributed = rs_producto.getInt("XX_QUANTITY");
							for (int i = 0; i < whereTO.remaining_quantity.size(); i++) {
								int index = -1;
								for(int  j = 0; j <  numeros_tiendas.size();j++){
									if(numeros_tiendas.elementAt(j).equals(tienda_id)){
										insertar_tienda = false;
										index = j;
										break;
									}
								}									
								useful = whereTO.remaining_quantity.elementAt(i);		
								if (useful == 0) continue;
								if (distributed == 0) break;																

								X_XX_VMR_OrderRequestDetail detalle_pedido = new X_XX_VMR_OrderRequestDetail(getCtx(), 0 , get_TrxName());
								detalle_pedido.setM_Product_ID( rs_producto.getInt("M_PRODUCT_ID") );
								
								if(insertar_tienda){
									MVMROrder pedido = new MVMROrder(getCtx(),0, get_TrxName());															
									if(distribucion_aprobada.isXX_IsAutomaticRedistribution()){
										pedido.setXX_OrderRequestType("A");
									}else{
										pedido.setXX_OrderRequestType("R");
									}
									if(distribucion_aprobada.isXX_IsAutomaticRedistribution()){
										pedido.setXX_OrderBecoCorrelative("A"+ warehouses.get(tienda_id)+""+distribucion_aprobada.getXX_VMR_DistributionHeader_ID());
									}else{
										pedido.setXX_OrderBecoCorrelative("R"+ warehouses.get(tienda_id)+""+distribucion_aprobada.getXX_VMR_DistributionHeader_ID());
									}
									pedido.setXX_VMR_DistributionHeader_ID(distribucion_aprobada.getXX_VMR_DistributionHeader_ID());
									pedido.setXX_OrderRequestStatus(X_Ref_XX_VMR_OrderStatus.POR_ETIQUETAR.getValue());
									pedido.setXX_OrderRequestType(X_Ref_XX_OrderRequestType.RE__DISTRIBUCIÓN.getValue());									
									pedido.setM_Warehouse_ID(tienda_id);										
									//pedido.setXX_WeekCreated(calendar.get(Calendar.WEEK_OF_YEAR)); //COMENTADO GHUCHET
									pedido.setXX_WeekCreated(Utilities.getWeekOfYear(calendar)); //AGREGADO GHUCHET
									// agregado por vlomonaco
									// actualiza la fecha del estado cuando se pone en pendiente
									//Calendar cal = Calendar.getInstance();				// Comentado por GMARQUES
								    //Timestamp t = new Timestamp(cal.getTime().getTime()); // Comentado por GMARQUES
									//pedido.setXX_DateStatusPending(t); 					// Comentado por GMARQUES
									Timestamp t = new Timestamp(serverDate.getTime());		// Agregado por GMARQUES
									pedido.setXX_DateStatusPending(t); 						// Agregado por GMARQUES
									// fin vlomonaco
									pedido.save();	

									numeros_tiendas.add(tienda_id);							
									numero_pedido.add(pedido.get_ID());
									detalle_pedido.setXX_VMR_Order_ID( pedido.get_ID());	
									
								} else {
									detalle_pedido.setXX_VMR_Order_ID(numero_pedido.get(index));										
								} 	
								
								detalle_pedido.setXX_VMR_Category_ID(rs_producto.getInt("XX_VMR_Category_ID"));
								detalle_pedido.setXX_VMR_Department_ID(rs_producto.getInt("XX_VMR_Department_ID"));														
								detalle_pedido.setXX_VMR_Line_ID(rs_producto.getInt("XX_VMR_Line_ID"));
								detalle_pedido.setXX_VMR_Section_ID(rs_producto.getInt("XX_VMR_Section_ID"));
								
								detalle_pedido.setXX_SalePrice(rs_producto.getBigDecimal("XX_SalePrice").setScale(2, RoundingMode.HALF_EVEN));
								detalle_pedido.setXX_TaxAmount(rs_producto.getBigDecimal("XX_TaxAmount").setScale(2, RoundingMode.HALF_EVEN));
								detalle_pedido.setXX_SalePricePlusTax(rs_producto.getBigDecimal("XX_SalePricePlusTax").setScale(2, RoundingMode.HALF_EVEN));
								detalle_pedido.setC_TaxCategory_ID(rs_producto.getInt("C_TaxCategory_ID"));
								detalle_pedido.setXX_UnitPurchasePrice(rs_producto.getBigDecimal("XX_UnitPurchasePrice").setScale(2, RoundingMode.HALF_EVEN));
								detalle_pedido.setPriceActual(rs_producto.getBigDecimal("PriceActual").setScale(2, RoundingMode.HALF_EVEN));	

								if (useful >= distributed ) {								
									detalle_pedido.setXX_ProductQuantity( distributed ); 
									detalle_pedido.setQtyReserved(distributed); //Se almacena dos veces para tener la cantidad original
									detalle_pedido.setXX_ProductBatch_ID(whereTO.batchnumbers.get(i));
									whereTO.remaining_quantity.set(i, useful - distributed); 																
									detalle_pedido.save();									
									distributed = 0;
									break;								
								} else {
									detalle_pedido.setXX_ProductQuantity( useful );
									detalle_pedido.setQtyReserved(useful);//Se almacena dos veces para tener la cantidad original
									detalle_pedido.setXX_ProductBatch_ID(whereTO.batchnumbers.get(i));									
									detalle_pedido.save();
									whereTO.remaining_quantity.set(i, 0);
									distributed -= useful;
								}									
							}
							if (distributed > 0) {									
								worked = false;
								break;
							}
						}
						rs_producto.close();
					}
					rsdetalle.close();
					prstdetalle.close();
					prst_producto.close();
					
/**					if (!worked) {
						//Jorge Pires - Comente Mensaje de Incosistencia de Datos en Inventario
						ADialog.error(1, new Container(), "XX_InventoryInconsistency");
						return "Rollback " + get_Trx().rollback();						
					}					
*/
					/*Hallar las colecciones de los productos de los distintos pedidos
					String sql_colecciones = " SELECT CO.NAME " +
							" FROM XX_VMR_PACKAGE PA JOIN C_Campaign CO " +
							" ON (PA.C_Campaign_ID = CO.C_Campaign_ID) " +
							" WHERE PA.XX_VMR_PACKAGE_ID IN ( " +
								" SELECT A.XX_VMR_PACKAGE_ID FROM XX_VMR_ORDERREQUESTDETAIL D " +
								" JOIN XX_VMR_ORDER O ON D.XX_VMR_ORDER_ID = O.XX_VMR_ORDER_ID " +
								" JOIN M_ATTRIBUTESETINSTANCE A ON D.XX_ProductBatch_ID = A.M_ATTRIBUTESETINSTANCE_ID " +
								" WHERE O.XX_VMR_DISTRIBUTIONHEADER_ID = ? ) " +
							"ORDER BY XX_PERIOD";
					PreparedStatement ps_colecciones = DB.prepareStatement(sql_colecciones, get_TrxName());
					ps_colecciones.setInt(1, distribucion_aprobada.get_ID());
					ResultSet rs_colecciones = ps_colecciones.executeQuery();
					String colecciones = new String();
					while (rs_colecciones.next()) {
						if (colecciones.isEmpty())
							colecciones += rs_colecciones.getString(1);
						else colecciones += " / " + rs_colecciones.getString(1);
					}
					rs_colecciones.close();
					ps_colecciones.close();
					if (colecciones.length() > 100)
						colecciones = colecciones.substring(0, 100);
					//Asignar estos a los distintos pedidos
					MVMROrder cabecera ;
					Iterator<Integer> pedidos = numero_pedido.iterator();
					while (pedidos.hasNext()) {
						cabecera = new MVMROrder(getCtx(), pedidos.next(), get_TrxName());
						cabecera.setXX_CollectionName(colecciones);
						cabecera.save();
					}*/
					//FIN - Guardar las colecciones
					
					//Verificar si la distribucion tiene distribuciones hermanas para crear las asociaciones de pedidos
					String sql_asociadas = 
						" SELECT XX_VMR_HEADERASSOCIATION_ID, XX_ASSOCIATIONNUMBER FROM XX_VMR_HEADERASSOCIATION " +
						" WHERE XX_VMR_DISTRIBUTIONHEADER_ID = " + distribucion_aprobada.get_ID();  
					int grupo_no = 0;					
					try {
						PreparedStatement ps = DB.prepareStatement(sql_asociadas,get_TrxName());
						ResultSet rs = ps.executeQuery();
						X_XX_VMR_HeaderAssociation header_asoc = null;
						if (rs.next() ) { 
							
							//Indicar que esta distribucion fue correctamente aprobada
							header_asoc  = new X_XX_VMR_HeaderAssociation(getCtx(), rs.getInt(1), get_TrxName());
							grupo_no = rs.getInt(2);
							header_asoc.setIsApproved(true);
							header_asoc.save();	
						}
						rs.close();
						ps.close();
					} catch (SQLException e){
						e.printStackTrace();
						ADialog.error(1, new Container(), "XX_DatabaseAccessError");				
					}
					
					//Si la cabecera está asociada a alguna otra distribucion
					if (grupo_no > 0) {
						
						//Agregar todos los pedidos a mi grupo de asociacion
						for (int i = 0 ; i < numero_pedido.size() ; i++) {
							X_XX_VLO_PlacedOrderAs asociacion = 
								new X_XX_VLO_PlacedOrderAs(Env.getCtx(), 0 , get_TrxName());
							asociacion.setXX_VMR_Order_ID(numero_pedido.get(i));
							asociacion.setXX_AssociationNumber(grupo_no);
							asociacion.save();							
						}
						
						//Advertir al usuario que debe distribuir el resto
						String sql_asociadasaprobadas = 
							" SELECT XX_VMR_DISTRIBUTIONHEADER_ID, ISAPPROVED FROM " +
							" XX_VMR_HEADERASSOCIATION WHERE XX_ASSOCIATIONNUMBER = " + grupo_no +
							" AND XX_VMR_DISTRIBUTIONHEADER_ID <> " + distribucion_aprobada.get_ID();
						
						PreparedStatement ps = DB.prepareStatement(sql_asociadasaprobadas, null);
						ResultSet rs = ps.executeQuery();
						Vector<String> distribuciones_faltantes = new Vector<String>();  					
						while (rs.next()) {
							if ( rs.getString(2) == null || !rs.getString(2).equals("Y")) {
								distribuciones_faltantes.add("" + rs.getInt(1));
							} 							
						}					
						rs.close();
						ps.close();						
						if (distribuciones_faltantes.size() > 0) {
							String mss = Msg.getMsg(Env.getCtx(), "XX_AssociatedHeaders", 
									distribuciones_faltantes.toArray());					
							ADialog.info(1, new Container(), mss);
						}
					}					
					distribucion_aprobada.setXX_DistributionStatus(
							X_Ref_XX_DistributionStatus.APROBADA.getValue());
					distribucion_aprobada.save();
					commit();
					
					// GMARQUES Colocar la fecha del servidor
					updateDates(numero_pedido);
					
					//Realizado por Rosmaira Arvelo
					MVMRCriticalTaskForClose task = new MVMRCriticalTaskForClose(Env.getCtx(),
							Utilities.getCriticalTaskForClose(distribucion_aprobada.get_ID()),null);
						
					//Llama al proceso cerrar alerta de Asignación de precios de venta
					if(((distribucion_aprobada.getXX_DistributionStatus().equals("AC"))||(distribucion_aprobada.getXX_DistributionStatus().equals("AP")))&&(task.get_ID()!=0)&&(task.isActive()==true)&&((task.getXX_TypeTask().equals("AP"))||(task.getXX_TypeTask().equals("AP1"))))
					{						
						try
						{
							Utilities.closeAlert(task.get_ID());
						}
						catch(Exception e)
						{
							log.log(Level.SEVERE,e.getMessage());
						}
					}					
					
					Integer count = 0;
					
					if(distribucion_aprobada.getC_Order_ID()==0)
					{							
						count = getPendingLabelCDCount(distribucion_aprobada.get_ID());					
						
						if((count!=0)&&(distribucion_aprobada.isXX_Alert3()==false))
						{
							try
							{
								//Generacion de Alerta de Tarea Critica para Pedidos pendientes por etiquetar		
								Env.getCtx().setContext("#XX_TypeAlertEP2","EP2");
								Env.getCtx().setContext("#XX_OrderEP2CT",distribucion_aprobada.get_ID());
								
								Utilities.generatedAlert(distribucion_aprobada.get_ID());
							}
							catch(Exception e)
							{
								log.log(Level.SEVERE,e.getMessage());
							}
							
						}
					 
					}//fin RArvelo
					
					String mss = Msg.getMsg(Env.getCtx(), "XX_AprovedDistribution", 
							//Order					
							new String[] {""+distribucion_aprobada.getXX_VMR_DistributionHeader_ID()});					
					ADialog.info(1, new Container(), mss);
					return "";
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
		}			
		return "";
	}
	
	/**
	 * Genera los pedidos asociados a una distribucion por orden de compra 
	 * La misma tiene que estar CHEQUEADA
	 */
	public void generatePlacedOrdersFromCheckedPurchaseOrder (
			MVMRDistributionHeader distribucion_aprobada) {
				
		// Agregado por GMARQUES
		// Fecha del servidor
		Date serverDate = currentServerDate();
		
		//Si la distribucion puede continuar generar los pedidos
		Vector<Integer> numeros_tiendas = new Vector<Integer>();				
		Vector<Integer> numero_pedido = new Vector<Integer>();
		
		//Almacena la informacion de lotes y productos
		XX_BatchNumberInfo info = new XX_BatchNumberInfo();
		Calendar calendar = new GregorianCalendar();
		//calendar.setTime(new Date());  // COMENTADO POR GMARQUES. Fecha del servidor.
		calendar.setTime(serverDate);    // AGREGADO POR GMARQUES. Fecha del servidor.
		MOrder orden = new MOrder(Env.getCtx(), distribucion_aprobada.getC_Order_ID(), null);
		
		boolean worked = true;

		//Para cada uno de los productos DE LA DISTRIBUCION
		String sql4= "SELECT D.M_PRODUCT_ID, DD.M_WAREHOUSE_ID, D.C_TaxCategory_ID, D.XX_TaxAmount," +
				" D.XX_SalePrice, D.XX_SalePricePlusTax, D.PriceActual, D.XX_UnitPurchasePrice, DD.XX_ProductQuantity " +
				" FROM XX_VMR_PO_PRODUCTDISTRIB D JOIN XX_VMR_PO_DISTRIBDETAIL DD ON " +
				" (DD.XX_VMR_PO_PRODUCTDISTRIB_ID = D.XX_VMR_PO_PRODUCTDISTRIB_ID) " +
				" WHERE D.XX_VMR_DISTRIBUTIONHEADER_ID= " + distribucion_aprobada.getXX_VMR_DistributionHeader_ID();
		
		PreparedStatement prst4= DB.prepareStatement(sql4,get_TrxName());
		ResultSet rsProducts;
		try {			
			rsProducts = prst4.executeQuery();
			while(rsProducts.next()){ 
				
				info.get_put(rsProducts.getInt("M_Product_ID"), 0, get_Trx(), distribucion_aprobada.getM_Warehouse_ID());
									
				XX_BatchNumberInfo.helperClass whereTO = 
					info.data.get(rsProducts.getInt("M_Product_ID"));
	
				//GHUCHET - MODIFICADO PARA QUE REVISE SI EL ALMACEN ES CUALQUIER CD - PROYECTO CD VALENCIA
					if (Utilities.esCD(rsProducts.getInt("M_Warehouse_ID"))) {
													
						//Estos productos no generan pedido pq van a centro de distribucion van a la tabla stored product	
						X_XX_VMR_StoredProduct stored_p = new X_XX_VMR_StoredProduct(getCtx(), 0, null);
						stored_p.setC_Order_ID(distribucion_aprobada.getC_Order_ID());
						if (rsProducts.getInt("C_TaxCategory_ID")> 0)
							stored_p.setC_TaxCategory_ID(rsProducts.getInt("C_TaxCategory_ID"));
						if (rsProducts.getBigDecimal("XX_TaxAmount") != null)
							stored_p.setXX_TaxAmount(
								rsProducts.getBigDecimal("XX_TaxAmount").setScale(2, RoundingMode.HALF_EVEN));
						if (rsProducts.getBigDecimal("XX_SalePrice") != null)
							stored_p.setXX_SalePrice(
								rsProducts.getBigDecimal("XX_SalePrice").setScale(2, RoundingMode.HALF_EVEN));
						if (rsProducts.getBigDecimal("XX_SalePricePlusTax") != null)
							stored_p.setXX_SalePricePlusTax(
								rsProducts.getBigDecimal("XX_SalePricePlusTax").setScale(2, RoundingMode.HALF_EVEN));
															
						stored_p.setXX_ProductQuantity(rsProducts.getInt("XX_ProductQuantity"));
						stored_p.setM_Product_ID(rsProducts.getInt("M_Product_ID"));
						stored_p.setXX_VMR_DistributionHeader_ID(distribucion_aprobada.get_ID());
						stored_p.save(get_TrxName());																	
						continue;
					}			
					
					X_M_Warehouse tienda = new X_M_Warehouse(getCtx(), rsProducts.getInt("M_Warehouse_ID"), null);
					//Verificar cuantos productos hay para distribuir
					int distributed = rsProducts.getInt("XX_ProductQuantity"), useful;						
					boolean insertar_tienda = true;
					for (int i = 0; i < whereTO.remaining_quantity.size(); i++) {							
						
						//Obtener la tienda a la que pertenece el pedido 
						int index = -1;							
						for(int j=0; j< numeros_tiendas.size();j++){
							if(numeros_tiendas.elementAt(j).equals(tienda.get_ID())){
								insertar_tienda = false;
								index = j;
								break;
							}
						}

						//Verificar de donde obtener las cantidades de productos y sus lotes
						useful = whereTO.remaining_quantity.elementAt(i);		
						if (useful == 0) continue;
						if (distributed == 0) break;														

						//Crear los detalles del pedido
						X_XX_VMR_OrderRequestDetail detalle_pedido = new X_XX_VMR_OrderRequestDetail(getCtx(), 0 ,null);
						detalle_pedido.setM_Product_ID(rsProducts.getInt("M_Product_ID"));						
						if(insertar_tienda){	
							
							//Si no se ha creado un pedido previo para esa tienda, crearlo
							MVMROrder pedido = new MVMROrder(getCtx(),0, null);
							pedido.setXX_OrderBecoCorrelative(X_Ref_XX_OrderRequestType.PRE__DISTRIBUCIÓN.getValue()+tienda.getValue()+""+distribucion_aprobada.getXX_VMR_DistributionHeader_ID());							
							pedido.setXX_OrderRequestStatus(X_Ref_XX_VMR_OrderStatus.POR_ETIQUETAR.getValue());
							pedido.setXX_OrderRequestType(X_Ref_XX_OrderRequestType.PRE__DISTRIBUCIÓN.getValue());
							pedido.setC_Order_ID(distribucion_aprobada.getC_Order_ID());
							pedido.setM_Warehouse_ID(tienda.get_ID());
							pedido.setXX_VMR_DistributionHeader_ID(distribucion_aprobada.get_ID());
							//pedido.setXX_WeekCreated(calendar.get(Calendar.WEEK_OF_YEAR));	//COMENTADO GHUCHET	
							pedido.setXX_WeekCreated(Utilities.getWeekOfYear(calendar)); //AGREGADO GHUCHET
							pedido.save(get_TrxName());

							numeros_tiendas.add(tienda.get_ID());								
							numero_pedido.add(pedido.get_ID());
							detalle_pedido.setXX_VMR_Order_ID( pedido.get_ID());							
						} else {
							//sino usar uno creado previamente
							detalle_pedido.setXX_VMR_Order_ID(numero_pedido.get(index));							
						} 
						
						//colocar los atributos del pedido
						detalle_pedido.setXX_VMR_Category_ID(orden.getXX_Category_ID());
						detalle_pedido.setXX_VMR_Department_ID(orden.getXX_VMR_DEPARTMENT_ID());						
						detalle_pedido.setXX_VMA_Season_ID(orden.getXX_Season_ID());
						detalle_pedido.setXX_VMR_Collection_ID(orden.getXX_Collection_ID());
						detalle_pedido.setXX_VMR_Package_ID(orden.getXX_VMR_Package_ID());

						MProduct product = new MProduct(getCtx(), rsProducts.getInt("M_Product_ID") , null);
						
						detalle_pedido.setXX_VMR_Line_ID(product.getXX_VMR_Line_ID());
						detalle_pedido.setXX_VMR_Section_ID(product.getXX_VMR_Section_ID());
						
						if (rsProducts.getInt("C_TaxCategory_ID")> 0)
							detalle_pedido.setC_TaxCategory_ID(rsProducts.getInt("C_TaxCategory_ID"));
						if (rsProducts.getBigDecimal("XX_TaxAmount") != null)
							detalle_pedido.setXX_TaxAmount(
								rsProducts.getBigDecimal("XX_TaxAmount").setScale(2, RoundingMode.HALF_EVEN));
						if (rsProducts.getBigDecimal("XX_SalePrice") != null)
							detalle_pedido.setXX_SalePrice(
								rsProducts.getBigDecimal("XX_SalePrice").setScale(2, RoundingMode.HALF_EVEN));
						if (rsProducts.getBigDecimal("XX_SalePricePlusTax") != null)
							detalle_pedido.setXX_SalePricePlusTax(
								rsProducts.getBigDecimal("XX_SalePricePlusTax").setScale(2, RoundingMode.HALF_EVEN));						
						if (rsProducts.getBigDecimal("PriceActual") != null)
							detalle_pedido.setPriceActual(
								rsProducts.getBigDecimal("PriceActual").setScale(2, RoundingMode.HALF_EVEN));					
						if (rsProducts.getBigDecimal("XX_UnitPurchasePrice") != null)
							detalle_pedido.setXX_UnitPurchasePrice(
								rsProducts.getBigDecimal("XX_UnitPurchasePrice").setScale(2, RoundingMode.HALF_EVEN));
											
						//Determinar de donde se calculan los lotes, y que cantidades se usaran de cada uno
						if (useful >= distributed ) {								
							detalle_pedido.setXX_ProductQuantity( distributed );
							detalle_pedido.setQtyReserved(distributed); //Se almacena dos veces para tener la cantidad original
							detalle_pedido.setXX_ProductBatch_ID(whereTO.batchnumbers.get(i));
							whereTO.remaining_quantity.set(i, useful - distributed); 							
							detalle_pedido.save(get_TrxName());
							distributed = 0;
							break;								
						} else {
							detalle_pedido.setXX_ProductQuantity( useful );
							detalle_pedido.setQtyReserved(useful); //Se almacena dos veces para tener la cantidad original
							detalle_pedido.setXX_ProductBatch_ID(whereTO.batchnumbers.get(i));									
							detalle_pedido.save(get_TrxName());
							whereTO.remaining_quantity.set(i, 0);
							distributed -= useful;
						}							
					}
					if (distributed > 0 ) {
						worked = false;
					}						
				}				
			rsProducts.close();
			prst4.close();

			if (!worked) {
				ADialog.error(1, new Container(), "XX_InventoryInconsistency");					
				rollback();
				return;
			}
			
			//Verificar si la distribucion tiene distribuciones hermanas para crear las asociaciones de pedidos
			String sql_asociadas = 
				" SELECT XX_VMR_HEADERASSOCIATION_ID, XX_ASSOCIATIONNUMBER FROM XX_VMR_HEADERASSOCIATION " +
				" WHERE XX_VMR_DISTRIBUTIONHEADER_ID = " + distribucion_aprobada.get_ID();  
			int grupo_no = 0;					
			try {
				PreparedStatement ps = DB.prepareStatement(sql_asociadas, null);
				ResultSet rs = ps.executeQuery();
				X_XX_VMR_HeaderAssociation header_asoc = null;
				if (rs.next() ) { 
					
					//Indicar que esta distribucion fue correctamente aprobada
					header_asoc  = new X_XX_VMR_HeaderAssociation(getCtx(), rs.getInt(1), null);
					grupo_no = rs.getInt(2);
					header_asoc.setIsApproved(true);
					header_asoc.save(get_TrxName());	
				}
				rs.close();
				ps.close();
			} catch (SQLException e){
				e.printStackTrace();
				ADialog.error(1, new Container(), "XX_DatabaseAccessError");				
			}
			
			//Si la cabecera está asociada a alguna otra distribucion
			if (grupo_no > 0) {
				
				//Agregar todos los pedidos a mi grupo de asociacion
				for (int i = 0 ; i < numero_pedido.size() ; i++) {
					X_XX_VLO_PlacedOrderAs asociacion = 
						new X_XX_VLO_PlacedOrderAs(Env.getCtx(), 0 , null);
					asociacion.setXX_VMR_Order_ID(numero_pedido.get(i));
					asociacion.setXX_AssociationNumber(grupo_no);
					asociacion.save(get_TrxName());							
				}
				
				//Advertir al usuario que debe distribuir el resto
				String sql_asociadasaprobadas = 
					" SELECT XX_VMR_DISTRIBUTIONHEADER_ID, ISAPPROVED FROM " +
					" XX_VMR_HEADERASSOCIATION WHERE XX_ASSOCIATIONNUMBER = " + grupo_no +
					" AND XX_VMR_DISTRIBUTIONHEADER_ID <> " + distribucion_aprobada.get_ID();
				
				PreparedStatement ps = DB.prepareStatement(sql_asociadas, null);
				ps = DB.prepareStatement(sql_asociadasaprobadas, null);
				ResultSet rs = ps.executeQuery();
				Vector<String> distribuciones_faltantes = new Vector<String>();  					
				while (rs.next()) {
					if ( rs.getString(2) == null || !rs.getString(2).equals("Y")) {
						distribuciones_faltantes.add("" + rs.getInt(1));
					} 							
				}					
				rs.close();
				ps.close();						
				if (distribuciones_faltantes.size() > 0) {
					if (!distribucion_aprobada.getXX_DistributionStatus().equals(
							X_Ref_XX_DistributionStatus.APROBADA__PENDIENTE_POR_CHEQUEO_DE_LA_OC)) {
						String mss = Msg.getMsg(Env.getCtx(), "XX_AssociatedHeaders", 
								distribuciones_faltantes.toArray());					
						ADialog.info(1, new Container(), mss);
					}
				}
			}			
			distribucion_aprobada.setXX_DistributionStatus(X_Ref_XX_DistributionStatus.APROBADA.getValue());
			distribucion_aprobada.save(get_TrxName());
			commit();
			
			// GMARQUES Colocar la fecha del servidor
			updateDates(numero_pedido);
						
			//Realizado por Rosmaira Arvelo
			MVMRCriticalTaskForClose task = new MVMRCriticalTaskForClose(Env.getCtx(),Utilities.getCriticalTaskForClose(distribucion_aprobada.get_ID()),null);
				
			//Llama al proceso cerrar alerta de Asignación de precios de venta
			if(((distribucion_aprobada.getXX_DistributionStatus().equals("AC"))||(distribucion_aprobada.getXX_DistributionStatus().equals("AP")))&&(task.get_ID()!=0)&&(task.isActive()==true)&&((task.getXX_TypeTask().equals("AP"))||(task.getXX_TypeTask().equals("AP1"))))
			{							
				try
				{
					Utilities.closeAlert(task.get_ID());
				}
				catch(Exception e)
				{
					log.log(Level.SEVERE,e.getMessage());
				}
			}			
			
		} catch (SQLException e) {				
			e.printStackTrace();
			ADialog.error(1, new Container(), "XX_DatabaseAccessError");
		}		
		
		//Realizado por Rosmaira Arvelo
		Integer count = 0;
		
		if(distribucion_aprobada.getC_Order_ID()!=0)
		{	
			count = getPendingLabelCount(distribucion_aprobada.getC_Order_ID());
			
		    if((count!=0)&&(distribucion_aprobada.isXX_Alert3()==false))
			{
		    	try
		    	{
					//Generacion de Alerta de Tarea Critica para Pedidos pendientes por etiquetar		
					Env.getCtx().setContext("#XX_TypeAlertEP1","EP1");
					Env.getCtx().setContext("#XX_OrderEP1CT",distribucion_aprobada.get_ID());
					
					Utilities.generatedAlert(distribucion_aprobada.get_ID());
		    	}
				catch(Exception e)
				{
					log.log(Level.SEVERE,e.getMessage());
				}
				
			}
		}//fin RArvelo
	}

@Override
	protected void prepare() {
	
	}

	/**
	 * Jorge E. Pires G. --> Funcion 108. Envio de Correos para que fije Definitivamente el Precio 
	 * 
	 **/	
	private boolean EnviarCorreosFijacionDefinitiva(Integer BPartner, String NumeroOC , String XX_OrderBecoCorrelative , String Attachment, Integer IsPedido) {
		String sql = "SELECT u.AD_User_ID "
			+ "FROM AD_User u "
			+ "where IsActive = 'Y' and "
			+ "C_BPARTNER_ID = " + BPartner;
	
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
	
			while(rs.next()){
				Integer UserAuxID = rs.getInt("AD_User_ID");
				if (IsPedido == 1){
					String Mensaje = Msg.getMsg( getCtx(), 
							"XX_FixedDefinitivePrice", 
							new String[]{ XX_OrderBecoCorrelative, 
						NumeroOC
					});
					Utilities f = new Utilities(getCtx(), null,1000006, Mensaje, -1, 1019149 ,-1, UserAuxID, Attachment);
					f.ejecutarMail(); 
					f = null;
				}else{
					String Mensaje = Msg.getMsg( getCtx(), 
							"XX_FixedDefinitivePrice1", 
							new String[]{ XX_OrderBecoCorrelative
					});
					Utilities f = new Utilities(getCtx(), null,1000006, Mensaje, -1, 1019149 ,-1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;
				}
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
		return true;
	}//EnviarCorreosFijacionDefinitiva

	/*
	 * Fin Jorge E. Pires G.
	 */

	//Realizado por Rosmaira Arvelo
	/*
	 *	Obtengo la cantidad de pedidos PD por etiquetar segun la orden
	 */
	private Integer getPendingLabelCount(Integer order){
		
		Integer count=0;
		String SQL = "SELECT COUNT(XX_VMR_Order_ID) FROM XX_VMR_Order "
			   + "WHERE C_Order_ID="+order;
	try
	{
		PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
	    ResultSet rs = pstmt.executeQuery();			
	    
		while (rs.next())
		{				
			count = rs.getInt("COUNT(XX_VMR_Order_ID)");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return count;
	}
	
	/*
	 *	Obtengo la cantidad de pedidos CD por etiquetar segun la distribucion
	 */
	private Integer getPendingLabelCDCount(Integer distribution){
		
		int count=0;
		String SQL = "SELECT COUNT(XX_VMR_Order_ID) AS Cuenta FROM XX_VMR_Order "
			   + "WHERE SUBSTR(XX_OrderBecoCorrelative,5)='"+distribution+"'";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			while (rs.next())
			{				
				count = rs.getInt("Cuenta");
				}
				rs.close();
				pstmt.close();
			}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return count;
	}
	

	// Agregado por GMARQUES
	/**
	 * @return Fecha actual del servidor
	 */
	private Date currentServerDate() {
		Date sd = new Date();
		try {
			PreparedStatement pstmt = DB.prepareStatement("SELECT sysdate from dual", null); 
		    ResultSet rs = pstmt.executeQuery();			
			while (rs.next()) {				
				sd = rs.getDate(1);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return sd;
	}
	
	/* Agregado por GMARQUES
	 * 	 Actualiza las fechas de creación y actualización en XX_VMR_ORDER, a la fecha actual del servidor
	 */
	private void updateDates(Vector<Integer> pedido_ID) {
		String SQL = "UPDATE XX_VMR_ORDER " +
					" SET Created = (SELECT SYSDATE FROM DUAL)," +
						" Updated = (SELECT SYSDATE FROM DUAL) " +
				" WHERE XX_VMR_ORDER_ID in (" + pedido_ID.elementAt(0);
		//Agregar todos los pedidos 
		for (int i = 1 ; i < pedido_ID.size() ; i++) {
			SQL += 	", " + pedido_ID.elementAt(i);					
		}
		SQL += ")";

		DB.executeUpdate(null, SQL);
		
	}

	/**	Actualiza el ultimo precio de venta de los  productos de una redistribución
	 * @autor GHUCHET 
	 * */
	private void setLastSalePrice() {
	
		String sqlProd = "SELECT XX_VMR_DISTRIBPRODUCTDETAIL_ID FROM XX_VMR_DISTRIBPRODUCTDETAIL " +
				" WHERE XX_VMR_DISTRIBUTIONDETAIL_ID IN (" +
				" SELECT XX_VMR_DISTRIBUTIONDETAIL_ID " +
				" FROM XX_VMR_DISTRIBUTIONDETAIL WHERE XX_VMR_DISTRIBUTIONHEADER_ID ="+getRecord_ID()+")";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try{
				//System.out.println(sqlProd);
				pstmt = DB.prepareStatement(sqlProd, null);
				rs = pstmt.executeQuery();
				
				while(rs.next()){
					MVMRDistribProductDetail prodDetail = new MVMRDistribProductDetail(getCtx(),rs.getInt(1) , null);
					MProduct prod = new MProduct(getCtx(), prodDetail.getM_Product_ID(), get_Trx());
					String sqlPrice = "SELECT MAX(C.XX_SALEPRICE) "+
					"\nFROM XX_VMR_PRICECONSECUTIVE C JOIN  M_PRODUCT  P  ON (P.M_PRODUCT_ID = C.M_PRODUCT_ID) "+
					"\nWHERE  P.XX_VMR_VENDORPRODREF_ID = "+prod.getXX_VMR_VendorProdRef_ID()+ 
					"\nAND C.CREATED IN (SELECT MAX(C2.CREATED) "+
					"\nFROM XX_VMR_PRICECONSECUTIVE C2 JOIN  M_PRODUCT  P2  ON (P2.M_PRODUCT_ID = C2.M_PRODUCT_ID) "+
					"\nWHERE  P2.XX_VMR_VENDORPRODREF_ID = "+prod.getXX_VMR_VendorProdRef_ID()+
					"\nAND C2.XX_CONSECUTIVEORIGIN = 'P')";
						
					PreparedStatement pstmt2 = null;
					ResultSet rs2 = null;
					try{
						//System.out.println(sqlPrice);
						pstmt2 = DB.prepareStatement(sqlPrice, null);
						rs2 = pstmt2.executeQuery();
						
						if(rs2.next()){
							prodDetail.setXX_LastSalePrice(rs2.getBigDecimal(1));
							prodDetail.save();
						}		
					}
					catch (SQLException e){
						log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
					}finally{
						DB.closeResultSet(rs2);
						DB.closeStatement(pstmt2);
					}
				}		
			}
			catch (SQLException e){
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			}finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		} 
	
	
	/**	Actualiza el ultimo precio de venta de los  productos de una predistribución
	 * @autor GHUCHET 
	 * */
	private void setLastSalePriceOC() {
		String sqlProd = "SELECT XX_VMR_PO_PRODUCTDISTRIB_ID FROM XX_VMR_PO_PRODUCTDISTRIB " +
				" WHERE XX_VMR_DISTRIBUTIONHEADER_ID ="+getRecord_ID();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try{
				//System.out.println(sqlProd);
				pstmt = DB.prepareStatement(sqlProd, null);
				rs = pstmt.executeQuery();
				
				while(rs.next()){
					MVMRPOProductDistrib prodDetail = new MVMRPOProductDistrib(getCtx(), rs.getInt(1), null);
					MProduct prod = new MProduct(getCtx(), prodDetail.getM_Product_ID(), get_Trx());
					String sqlPrice = "SELECT MAX(C.XX_SALEPRICE) "+
					"\nFROM XX_VMR_PRICECONSECUTIVE C JOIN  M_PRODUCT  P  ON (P.M_PRODUCT_ID = C.M_PRODUCT_ID) "+
					"\nWHERE  P.XX_VMR_VENDORPRODREF_ID = "+prod.getXX_VMR_VendorProdRef_ID()+ 
					"\nAND C.CREATED IN (SELECT MAX(C2.CREATED) "+
					"\nFROM XX_VMR_PRICECONSECUTIVE C2 JOIN  M_PRODUCT  P2  ON (P2.M_PRODUCT_ID = C2.M_PRODUCT_ID) "+
					"\nWHERE  P2.XX_VMR_VENDORPRODREF_ID = "+prod.getXX_VMR_VendorProdRef_ID()+
					"\nAND C2.XX_CONSECUTIVEORIGIN = 'P')";
						
					PreparedStatement pstmt2 = null;
					ResultSet rs2 = null;
					try{
						//System.out.println(sqlPrice);
						pstmt2 = DB.prepareStatement(sqlPrice, null);
						rs2 = pstmt2.executeQuery();
						
						if(rs2.next()){
							prodDetail.setXX_LastSalePrice(rs2.getBigDecimal(1));
							prodDetail.save();
						}		
					}
					catch (SQLException e){
						log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
					}finally{
						DB.closeResultSet(rs2);
						DB.closeStatement(pstmt2);
							
					}
				}		
			}
			catch (SQLException e){
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			}finally{
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				
			}
		} 
	
	/**	Valida si las cantidades distribuidas a las tiendas son 
	 * consistentes con la cantidad a distribuir del producto 	
	 * @autor GHUCHET
	 * */
	private boolean isInconsistent(MVMRDistributionHeader dist) {
		
		boolean result =  false;
		String sql, sql2 ="";
		
		sql = "\nSELECT P.XX_VMR_DISTRIBPRODUCTDETAIL_ID, P.XX_QUANTITY  " +
				"\nFROM XX_VMR_DISTRIBPRODUCTDETAIL P JOIN XX_VMR_DISTRIBUTIONDETAIL D " +
				"\nON (P.XX_VMR_DISTRIBUTIONDETAIL_ID = D.XX_VMR_DISTRIBUTIONDETAIL_ID) " +
				"\nWHERE D.XX_VMR_DISTRIBUTIONHEADER_ID ="+dist.get_ID();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				sql2 = "\nSELECT  CASE WHEN SUM(XX_QUANTITY) = "+rs.getInt("XX_QUANTITY")+ " THEN 1 ELSE 0 END "+
				"\nFROM XX_VMR_DISTRIBPRODPERSTORE" +
				"\nWHERE XX_VMR_DISTRIBPRODUCTDETAIL_ID = "+rs.getInt("XX_VMR_DISTRIBPRODUCTDETAIL_ID");
				pstmt2 = DB.prepareStatement(sql2, null);
				rs2 = pstmt2.executeQuery();
				if(rs2.next()) {
					if(rs2.getInt(1) == 0){
						result = true;
						return result;
					}
				}
				DB.closeResultSet(rs2);
				DB.closeStatement(pstmt2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return result;
		
	}
	
	
	/**	Actualiza el mínimo precio de la competencia asociado a la línea y concepto de valor de los productos de una redistribución
	 * @autor GHUCHET 
	 * */
	private void setMinCompetitionPrice() {
	
		String sqlProd = "SELECT XX_VMR_DISTRIBPRODUCTDETAIL_ID FROM XX_VMR_DISTRIBPRODUCTDETAIL " +
				" WHERE XX_VMR_DISTRIBUTIONDETAIL_ID IN (" +
				" SELECT XX_VMR_DISTRIBUTIONDETAIL_ID " +
				" FROM XX_VMR_DISTRIBUTIONDETAIL WHERE XX_VMR_DISTRIBUTIONHEADER_ID ="+getRecord_ID()+")";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try{
				//System.out.println(sqlProd);
				pstmt = DB.prepareStatement(sqlProd, null);
				rs = pstmt.executeQuery();
				
				while(rs.next()){
					MVMRDistribProductDetail prodDetail = new MVMRDistribProductDetail(getCtx(),rs.getInt(1) , null);
					MProduct prod = new MProduct(getCtx(), prodDetail.getM_Product_ID(), get_Trx());
					
					String sqlPrice = "SELECT MIN(C.XX_MINPRICE) "+
					"\nFROM M_PRODUCT  P  " +
					"\nLEFT JOIN XX_VMR_COMPETITIONPRICE C  " +
					"\nON (C.XX_VMR_LINE_ID = P.XX_VMR_LINE_ID AND C.XX_VME_CONCEPTVALUE_ID = P.XX_VME_CONCEPTVALUE_ID) " +
					"\nWHERE P.M_PRODUCT_ID = " +prod.get_ID();
						
					PreparedStatement pstmt2 = null;
					ResultSet rs2 = null;
					try{
						pstmt2 = DB.prepareStatement(sqlPrice, null);
						rs2 = pstmt2.executeQuery();
						
						if(rs2.next()){
							prodDetail.setXX_MinCompetitionPrice(rs2.getBigDecimal(1));
							prodDetail.save();
						}		
					}
					catch (SQLException e){
						log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
					}finally{
						DB.closeResultSet(rs2);
						DB.closeStatement(pstmt2);
					}
				}		
			}
			catch (SQLException e){
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			}finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		} 
	
	
	/**	Actualiza el mínimo precio de la competencia asociado a la línea y concepto de valor de los productos de una predistribución
	 * @autor GHUCHET 
	 * */
	private void setMinCompetitionPriceOC() {
		String sqlProd = "SELECT XX_VMR_PO_PRODUCTDISTRIB_ID FROM XX_VMR_PO_PRODUCTDISTRIB " +
				" WHERE XX_VMR_DISTRIBUTIONHEADER_ID ="+getRecord_ID();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try{
				//System.out.println(sqlProd);
				pstmt = DB.prepareStatement(sqlProd, null);
				rs = pstmt.executeQuery();
				
				while(rs.next()){
					MVMRPOProductDistrib prodDetail = new MVMRPOProductDistrib(getCtx(), rs.getInt(1), null);
					MProduct prod = new MProduct(getCtx(), prodDetail.getM_Product_ID(), get_Trx());
					String sqlPrice = "SELECT MIN(C.XX_MINPRICE) "+
							"\nFROM M_PRODUCT  P  " +
							"\nLEFT JOIN XX_VMR_COMPETITIONPRICE C  " +
							"\nON (C.XX_VMR_LINE_ID = P.XX_VMR_LINE_ID AND C.XX_VME_CONCEPTVALUE_ID = P.XX_VME_CONCEPTVALUE_ID) " +
							"\nWHERE P.M_PRODUCT_ID = " +prod.get_ID();
						
					PreparedStatement pstmt2 = null;
					ResultSet rs2 = null;
					try{
						//System.out.println(sqlPrice);
						pstmt2 = DB.prepareStatement(sqlPrice, null);
						rs2 = pstmt2.executeQuery();
						
						if(rs2.next()){
							prodDetail.setXX_MinCompetitionPrice(rs2.getBigDecimal(1));
							prodDetail.save();
						}		
					}
					catch (SQLException e){
						log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
					}finally{
						DB.closeResultSet(rs2);
						DB.closeStatement(pstmt2);
							
					}
				}		
			}
			catch (SQLException e){
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			}finally{
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				
			}
		} 
	

	
}
