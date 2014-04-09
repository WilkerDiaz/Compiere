package compiere.model.suppliesservices.processes;
import java.awt.Cursor;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import org.compiere.apps.ADialog;
import org.compiere.apps.form.FormFrame;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

import compiere.model.cds.MOrder;
import compiere.model.cds.MOrderLine;
import compiere.model.suppliesservices.X_XX_ProductPercentDistrib;

public class XX_SalesPercentage {
	// Tomado de Distribución (Javier Pino) y modificado para Bienes y Servicios
	private boolean allowed_to_process = false;
	private Hashtable<Integer, Hashtable<Integer, Integer> > size_references
					= new Hashtable<Integer, Hashtable<Integer,Integer>>();	
	private int windowNo = 0;
	private FormFrame frame = null;
	private MOrderLine detail;
	private MOrder order = null;
	private static int lines = 0;
	private static Trx trans ;
	
	/**
	 * safeDeleteRecords
	 * La linea esta ya distribuida, se solicita al usuario si desea eliminar
	 * esos valores
	 */
	private void safeDeleteRecords (int tipo) {	
		// Se pregunta sólo una vez si el usuario desea eliminar las distribuciones
		// (Para el caso de distribuir desde cabecera)
		if(tipo == 1){
			lines++;
		}
		
		if(lines <= 1){
			if (detail.isXX_IsDistribApplied()) {
				allowed_to_process = ADialog.ask(windowNo, frame, "XX_DeleteDistribution");			
			} 
			else allowed_to_process = true;
		}
		else {
			allowed_to_process = true;
		}
				
	} // Fin safeDeleteRecords
	
	/**
	 * deletePreviuosAndSaveNewPerc
	 * Elimina los porcentajes anteriores y crea los nuevos
	 * @param percentages Porcentajes/cantidades a ser aplicadas
	 * @param codes Codigos de los centros de costo
	 * @param tipo Tipo de distribucion (cabecera o linea)
	 * @return exito o fallo del proceso de distribucion
	 */
	private boolean deletePreviuosAndSaveNewPerc (Vector<? 
			extends Number> percentages,Vector<Integer> codes, int tipo) {	
		int result = 0;
		if (!allowed_to_process) return false;			

		String sql_deleteperc = "DELETE FROM XX_PRODUCTPERCENTDISTRIB " +
								" WHERE C_OrderLine_ID = " + detail.get_ID();
		
		try { result = DB.executeUpdateEx(sql_deleteperc, null); } 
		catch (SQLException e) { e.printStackTrace(); }

		if(result == -1){ return false; }
		
		order = new MOrder(Env.getCtx(), detail.getC_Order_ID(),null);
		
		// Crea las distribuciones para las lineas
		for (int iterator = 0; iterator < codes.size() ; iterator++) {	
			X_XX_ProductPercentDistrib store_percentage = new X_XX_ProductPercentDistrib (Env.getCtx(), 0, trans);
			store_percentage.setC_OrderLine_ID(detail.get_ID());
			
			// Se setean las tiendas o centros de distribucion dependiendo del 
			// tipo de distribucion aplicada (solo tiendas tienen ventas)
			if((tipo ==2 && detail.getXX_DistributionType().equals("SA")) || 
					(tipo == 1 &&  order.getXX_DistributionType().equals("SA"))){
				store_percentage.setM_Warehouse_ID(codes.get(iterator));}
			else{
				store_percentage.setXX_Org_ID(codes.get(iterator));
			}
			
			if (tipo == 1 && order.getXX_DistributionType().equals("AM")){
				detail.setXX_IsAmountDistrib(true);
			}
			
			double percentage = percentages.get(iterator).doubleValue();
			
			if (percentage != 0.0) {
				BigDecimal bd = new BigDecimal(percentage);
			    bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
			    if(detail.isXX_IsPiecesPercentage()){
			    	store_percentage.setXX_QuantityPerCC(bd);
			    }
			    else if(detail.isXX_IsAmountDistrib()){
			    	store_percentage.setXX_AmountPerCC(bd);
			    }
			    else {
			    	store_percentage.setXX_PercentagePerCC(bd);
				}	
				store_percentage.save();
			}			
		}
		detail.setXX_IsDistribApplied(true);
		detail.save(trans);
		return true;
	} // Fin deletePreviuosAndSaveNewPerc
	
	/**
	 * Aplica la distribución, guada los registros para cada producto  
	 * @param detail Detalle a ser distribuido
	 * @param percentages Vector que contiene la información de los porcentajes
	 * @param codes Vector que contiene los codigos de los centros de costo
	 */	
	public static boolean applyDistribution(MOrderLine detail, Vector<Double> percentages,
				Vector<Integer> warehouses, Vector<Integer> orgs, int windowNo, FormFrame frame, 
				int tipo, MOrder order) {
		Cursor wait_cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
		Cursor default_cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR); 
		frame.setCursor(wait_cursor);
		int zeros = 0;
			
		if (percentages.isEmpty()) {			
			frame.setCursor(default_cursor);
			ADialog.error(windowNo, frame, "XX_ThereAreNoValues");
			return false;			
		}

		for (int i = 0; i < percentages.size(); i++) {
			if (percentages.get(i).doubleValue() == 0) 
				zeros++;
		}
		if (zeros == percentages.size()) {
			frame.setCursor(default_cursor);
			ADialog.error(windowNo, frame, "XX_ThereAreNoValues");
			return false;			
		}
		
		if (warehouses.size() != percentages.size()) {
			frame.setCursor(default_cursor);
			ADialog.error(windowNo, frame, "XX_DistinctSizes");
			return false;
		}
		XX_SalesPercentage dist = new XX_SalesPercentage();
			
		dist.frame = frame;
		dist.windowNo = windowNo;
		dist.detail = detail;
		dist.trans = Trx.get("XX_CHANGE_DELETQTYS");
		dist.safeDeleteRecords(tipo);	
			
		// Se eliminan los valores previos y se aplica la distribucion
//		if (dist.deletePreviuosAndSaveNewPerc(percentages, warehouses, tipo)){
			if ( dist.percentualDistribution(percentages, warehouses, orgs, tipo, order) ) {
				frame.setCursor(default_cursor);
				return true;				
			} 
			else {
				frame.setCursor(default_cursor);
				return false;
			}
//		} // Fin If		
//		else {
//			frame.setCursor(default_cursor);
//			return false;		
//		}
	} // Fin applyDistribution

	
	/**
	 * Se aplica la distribucion  
	 * @param percentages Vector que contiene la información de los porcentajes
	 * @param codes Vector que contiene los codigos de los centros de costo
	 * */	
	private boolean percentualDistribution(Vector<Double> percentages, 
			Vector<Integer> codes, Vector<Integer> Orgs, int tipo, MOrder order) {							
		if (!allowed_to_process) return false;
		int result = 0, product = 0, xx_quantity = 0;
		PreparedStatement pstmt_a = null;
		ResultSet rs_a = null;
		
		// Elimina las distribuciones anteriores
		String SQL_t = " DELETE FROM XX_PRODUCTPERCENTDISTRIB " +
				" WHERE C_OrderLine_ID= " + detail.get_ID();
		
		try { result = DB.executeUpdateEx(SQL_t, null); } 
		catch (SQLException e) { e.printStackTrace(); }

		if(result == -1){ return false; }
	
		// Variables de distribucion
		int total = 0, most = 0; double most_value_percentage = 0;
		for (int i = 0 ; i < percentages.size() ; i++) {//Round up
			if (percentages.elementAt(i).doubleValue() > most_value_percentage) {
				most = i;
				most_value_percentage = percentages.elementAt(i).doubleValue();
			}
		}		
		
		// Se define el tipo de distribución para la linea de acuerdo a la opcion en cabecera
		if(tipo == 1){
			if(order.getXX_DistributionType().equals("PO")){
				detail.setXX_IsPiecesPercentage(false);
				detail.setXX_DistributionType("PO");
			}
			else if (order.getXX_DistributionType().equals("AM")){
				detail.setXX_IsAmountDistrib(true);
				detail.setXX_DistributionType("AM");
			}
			detail.setXX_ClearedDistrib(true);
		} // distribucion desde cabecera
		
		
		// Distribuye cada producto asociado a la OC
		String SQL_a = "SELECT M_PRODUCT_ID, QtyEntered XX_QTY " +
				" FROM C_OrderLine " +
				" WHERE C_OrderLine_ID = " + detail.get_ID()+ 
				" AND M_PRODUCT_ID IS NOT NULL ";
		//System.out.println(SQL_a);
		try { 
			pstmt_a = DB.prepareStatement(SQL_a, trans);
			rs_a = pstmt_a.executeQuery();				

			// Para cada producto
			if(rs_a.next()) {
				product = rs_a.getInt("M_PRODUCT_ID");									
				xx_quantity = rs_a.getInt("XX_QTY");
				// Relacionado a los valores previos				
				int distributed_quantity = 0;
				// Para cada organizacion							
				Vector<Integer> qtys = new Vector<Integer>();
				total = 0;
				float percentage = 0;
				int quantities = 0;
				
				// Calcula el monto a ser distribuido
				for (int i = 0; i < codes.size(); i++) {
					if(detail.isXX_IsPiecesPercentage()){
						quantities = Math.round(percentages.elementAt(i).floatValue());
						qtys.add(quantities);
						total += quantities;
					}//If
					else if(detail.isXX_IsAmountDistrib()){
//						distributed_quantity = Math.round(xx_quantity*percentage );
//						qtys.add(distributed_quantity);
//						total += distributed_quantity;
					}
					else {
						percentage = percentages.elementAt(i).floatValue()/100;
						distributed_quantity = Math.round(xx_quantity*percentage );
						qtys.add(distributed_quantity);
						total += distributed_quantity;
					}//Else
				}//For	
				
				if(!detail.isXX_IsAmountDistrib()){
					// Vector que contiene todas las tiendas que tienen que ser
					// cortadas de la curva 
					Vector<Integer> cut_down_stores = new Vector<Integer>();
					if (total > xx_quantity ) {  			
						int to_remove = total - xx_quantity;					
						// Se eliminan orgs hasta eliminar el exceso
						for (int i = qtys.size() - 1 ; i > 0  ; i--) { 							
							if (qtys.elementAt(i) > 0) 
								cut_down_stores.add(codes.elementAt(i));
							if ( qtys.elementAt(i) <  to_remove ) {
								to_remove = to_remove - qtys.elementAt(i);
								qtys.set(i, 0 );
							} else {								
								qtys.set(i, qtys.elementAt(i) - to_remove);
								to_remove = 0;
							}
							if (to_remove == 0) break;
						}
					} // If Total
					else if (total < xx_quantity ){ 				
						qtys.set(most, qtys.elementAt(most) + (xx_quantity - total));
						cut_down_stores.add(codes.elementAt(most));
					}
				}// otras distribuciones
				
				double totalPorcentaje=0.00;
				
				for (int i = 0; i < codes.size(); i++) {
					if (!detail.isXX_IsAmountDistrib() && qtys.elementAt(i) == 0) continue; 
					
					X_XX_ProductPercentDistrib prodPerStore = 
						new X_XX_ProductPercentDistrib(Env.getCtx(),0,null);
					prodPerStore.setC_Order_ID(detail.getC_Order_ID());
					prodPerStore.setC_OrderLine_ID(detail.get_ID());
					
					if (!detail.isXX_IsAmountDistrib()){
						prodPerStore.setXX_QuantityPerCC(new BigDecimal(qtys.elementAt(i)));	
						
						BigDecimal qtyBigDec = new BigDecimal(qtys.elementAt(i));
						qtyBigDec = qtyBigDec.multiply(detail.getPriceEntered());
						prodPerStore.setXX_AmountPerCC(qtyBigDec.setScale(2, BigDecimal.ROUND_HALF_UP));	
					}
					else {
						prodPerStore.setXX_AmountPerCC((new BigDecimal(percentages.get(i))).setScale(2, BigDecimal.ROUND_HALF_UP));	
					}
					
					// Si es distribucion por ventas, se setean los centros de costo 
					// (sólo tiendas tienen ventas) 
					// En otro caso, centros de costo
					if((tipo == 2 && detail.getXX_DistributionType().equals("SA")) 
							|| (tipo == 1 &&  order.getXX_DistributionType().equals("SA"))){
						prodPerStore.setM_Warehouse_ID(codes.get(i));
						prodPerStore.setXX_Org_ID(Orgs.get(i));
					}
					else {
						prodPerStore.setXX_Org_ID(codes.get(i));
					}
					
					// Si la distribución es por piezas (manual), se setean las 
					// cantidades, en caso contrario los porcentajes
					if(detail.isXX_IsPiecesPercentage()){
						prodPerStore.setXX_QuantityPerCC((new BigDecimal(
								(Double)percentages.elementAt(i)
								))/*.setScale(2)*/);
					}
					else if(detail.isXX_IsAmountDistrib()){
										
						prodPerStore.setXX_PercentagePerCC(new BigDecimal(
								(Double)percentages.elementAt(i)).multiply(new BigDecimal(100))
								.divide(detail.getPriceEntered(), 2, BigDecimal.ROUND_HALF_UP));
						
						//ACUNMULA LOS PORCENTAJES PARA VALIDAR QUE EL TOTAL SEA EL 100%
						BigDecimal elemento=new BigDecimal(
								(Double)percentages.elementAt(i)).multiply(new BigDecimal(100))
								.divide(detail.getPriceEntered(), 2, BigDecimal.ROUND_HALF_UP);
						totalPorcentaje= totalPorcentaje + elemento.doubleValue();
						
						//SE UTILIZA PARA RESTAR LAS DECIMAL DEL REDONDEO 
						if(i+1 == codes.size()){
								System.out.println("TP-> "+totalPorcentaje);
								System.out.println("E-> "+elemento);
							if(totalPorcentaje != 100.00){
								BigDecimal resta= new BigDecimal (totalPorcentaje-100.00);
								resta=elemento.subtract(resta);
								resta=resta.setScale(2, BigDecimal.ROUND_HALF_UP);
								prodPerStore.setXX_PercentagePerCC(resta);
								System.out.println("Total->"+elemento.subtract(resta));
							}
						}
						
					}
					else {
						prodPerStore.setXX_PercentagePerCC((new BigDecimal(
								(Double)percentages.elementAt(i)
								)).setScale(2,BigDecimal.ROUND_HALF_UP));
					}
					

					prodPerStore.save(trans);
					
				}// For
				
			}
			
			// La ditribucion se ha aplicado
			detail.setXX_IsDistribApplied(true);
			detail.save();
		} 
		catch (SQLException e) {			
			e.printStackTrace();
			ADialog.error(windowNo, frame,  "XX_DatabaseAccessError");
			return false;
		}
		finally{
			DB.closeResultSet(rs_a);
			DB.closeStatement(pstmt_a);
		}
	return trans.commit();
	}// Fin percentualDistribution 

}// Fin XX_SalesPercentage
