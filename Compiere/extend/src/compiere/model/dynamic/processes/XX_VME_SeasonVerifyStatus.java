package compiere.model.dynamic.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.dynamic.XX_VME_GeneralFunctions;
import compiere.model.dynamic.X_C_Campaign;
import compiere.model.dynamic.X_XX_VMA_MarketingActivity;
import compiere.model.dynamic.X_XX_VMA_Season;

/**
 * 
 * Esta clase representa el proceso que se va a correr regularmente para chequear
 * los status de las temporadas, campañas comerciales y acciones de mercadeo. Una
 * vez que se han verificado los mismos se pueden cambiar a otro estado.
 * 
 * @author Alejandro Prieto
 * @author Maria Vintimilla
 * @version 2.0
 */

public class XX_VME_SeasonVerifyStatus extends SvrProcess{
	/**
	 * No se utiliza el prepare ya que el proceso no recibe parámetros.
	 */
	protected void prepare() {		
	}
	
	/**
	 * Realiza la verificación de los status de las temporadas, campañas comerciales y
	 * acciones de mercadeo. Se actualizan los estados de los mismos y además se actualizan 
	 * los datos de los folletos, entre otros.
	 * 
	 * @return String mensaje de notificación que se le envía al usuario
	 */
	protected String doIt() throws Exception {
		Timestamp dateActual = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String result = "";
		String docState = "";
		String sql = "";
		int brochure = 0;
//		Date inicio = new Date();
		
		sql= " SELECT XX_VMA_Season_ID," +
				" StartDate, " +
				" EndDate, " +
				" DocStatus " +
				" FROM XX_VMA_Season " +
				" WHERE (StartDate < sysdate AND DocStatus = 'AP') OR " +
				" (DocStatus = 'IP' AND sysdate > EndDate)" +
				" AND AD_CLIENT_ID = "+ Env.getCtx().getAD_Client_ID();
		//System.out.println("SQL: "+sql);
		
		// Se realizan las actualizaciones de los status de las temporadas 
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				X_XX_VMA_Season season = 
					new X_XX_VMA_Season(Env.getCtx(),rs.getInt("XX_VMA_Season_ID"),null);
				docState = rs.getString("DocStatus");
				if(docState.equals("AP")){
					season.setDocStatus("IP");
				}
				if(docState.equals("IP")){
					season.setDocStatus("CL");
				}
				season.save();
			} // while	
		} // finally
		catch (Exception e){ e.printStackTrace(); }
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		sql = " SELECT C_Campaign_ID, " +
				" trunc(StartDate), " +
				" trunc(EndDate), " +
				" DocStatus " +
				" FROM C_Campaign " +
				" WHERE (StartDate < sysdate AND DocStatus = 'AP') " +
				" OR (EndDate < sysdate AND DocStatus = 'IP')"+
				" AND AD_CLIENT_ID = "+ Env.getCtx().getAD_Client_ID();;
		//System.out.println("SQL: "+sql);
		
		// Se realizan las actualizaciones de los status de las campañas  comerciales
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				X_C_Campaign campaign = 
					new X_C_Campaign(Env.getCtx(),rs.getInt("C_Campaign_ID"),null);
				docState = rs.getString("DocStatus");
				if(docState.equals("AP")){
					campaign.setDocStatus("IP");
				}
				if(docState.equals("IP")){
					campaign.setDocStatus("CL");
				}
				campaign.save();
			} // while
		}
		catch (Exception e){ e.printStackTrace(); }
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		// Se realizan las actualizaciones de los status de las acciones de  mercadeo 
		sql = " SELECT XX_VMA_MarketingActivity_ID, " +
				" trunc(StartDate) ini, " +
				" trunc(EndDate) fin, " +
				" DocStatus, " +
				" trunc(sysdate) actualDate " +
				" FROM XX_VMA_MarketingActivity " +
				" WHERE (sysdate <= StartDate AND DocStatus = 'AP' AND sysdate <= EndDate) OR" +
				" (sysdate <= EndDate AND DocStatus = 'IP')" +
				" AND AD_CLIENT_ID = "+ Env.getCtx().getAD_Client_ID();
		//System.out.println("SQL: "+sql);

		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				X_XX_VMA_MarketingActivity activity = 
					new X_XX_VMA_MarketingActivity(Env.getCtx(),
						rs.getInt("XX_VMA_MarketingActivity_ID"),null);
				brochure = activity.getXX_VMA_Brochure_ID();
				docState = rs.getString("DocStatus");
				dateActual = rs.getTimestamp("actualDate");
				if(docState.equals("AP")){
//					activity.setDocStatus("IP");
					
					// Se verifica si la acción de mercadeo no ha sido aprobada 
					// inicialmente de ser así se desactiva la acción de 
					// mercadeo y su status se coloca en cerrada de manera 
					// automática
//					if(!activity.isXX_VMA_AprobFin()){
//						activity.setDocStatus("CL");
//						XX_VME_GeneralFunctions.processAM(activity.get_ID(), false, false);							
//					} // if aprobFin
//					else{
						// Se verifica que la accion de mercadeo sea de tipo folleto
						if(activity.getXX_VMA_ActivityType().equals("B")){
							// Se calculan los precios actuales y promocionales de los productos
							XX_VME_GeneralFunctions.actualizarPrecioPromocion(brochure, 
									activity.getStartDate(), activity.getEndDate());
							
							// Se calcula inventario inicial para los productos del folleto. 
							XX_VME_GeneralFunctions.calculateInventory(rs.getTimestamp("ini"), 
									rs.getTimestamp("fin"), activity.getXX_VMA_Brochure_ID(),
									activity.get_ID());
							
							// Si la fecha actual es igual a la de inicio de la actividad
							if(dateActual.compareTo(rs.getTimestamp("ini")) == 0){
								// Actualizar inventario según pedidos
								XX_VME_GeneralFunctions.updateInventory(brochure, activity);
							}
						} // folleto					
//					}
				} // if AP
				if(docState.equals("IP")){
////					activity.setDocStatus("CL");
//					// Se verifica que la accion de mercadeo sea de tipo folleto
					if(activity.getXX_VMA_ActivityType().equals("B")){
//						// Se calcula inventario final para los productos del folleto. 
//						// También se coloca al folleto como cerrado, es decir, que ha expirado.
						XX_VME_GeneralFunctions.calculateInventory(rs.getTimestamp("ini"), 
								rs.getTimestamp("fin"), activity.getXX_VMA_Brochure_ID(),
								activity.get_ID());
						
						// Actualizar inventario según pedidos
						XX_VME_GeneralFunctions.updateInventory(brochure, activity);
						
//						if(!activity.isXX_VMA_IsActivityApproved()){
//							X_XX_VMA_Brochure Brochure = new X_XX_VMA_Brochure(getCtx(),
//									activity.getXX_VMA_Brochure_ID(),null);
//							Brochure.setXX_VMA_Expired(true);
//							Brochure.save();
//						}
					}
				} // if IP
				activity.save();
			} // while
		} // try
		catch(Exception e){ e.printStackTrace(); }
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		// Actualizacion de inventarios de los elementos de tipo producto que 
		// se encuentran en acciones de mercadeo aprobadas.
//		sql = " SELECT XX_VMA_MarketingActivity_ID, " +
//				" trunc(StartDate) ini, " +
//				" trunc(EndDate) fin " +
//				" FROM XX_VMA_MarketingActivity " +
//				" WHERE XX_VMA_ActivityType = 'B' and " +
//				" sysdate < StartDate AND DocStatus = 'AP'";
//		//System.out.println("SQL: "+sql);
//		try{
//			pstmt=DB.prepareStatement(sql, null);
//			rs=pstmt.executeQuery();
//			while(rs.next()){
//				X_XX_VMA_MarketingActivity activity = 
//					new X_XX_VMA_MarketingActivity(Env.getCtx(),
//						rs.getInt(1),null);
//				brochure = activity.getXX_VMA_Brochure_ID();
//				//  Se actualiza el inventario de los productos asociados a un 
//				// folleto que a su vez está asociado a una acción de mercadeo
//				XX_VME_GeneralFunctions.calculateInventory(rs.getTimestamp("ini"), 
//						rs.getTimestamp("fin"), activity.getXX_VMA_Brochure_ID(), activity.get_ID());
//				// Actualizacion de los precios de promocion de cada uno de los 
//				// elementos asociados a un folleto que a su vez esta asociado 
//				// a una accion de mercadeo
//				if(!activity.isXX_VMA_IsActivityApproved()){
//					// Colocar proceso de actualización de precio actual de producto y
//					// precio promocional
////					XX_VME_GeneralFunctions.updateActualPrice(brochure);
//					XX_VME_GeneralFunctions.actualizarPrecioPromocion(brochure, 
//							activity.getStartDate(), activity.getEndDate());
//				}
//			} // while
//		}
//		catch(Exception e){ e.printStackTrace(); }
//		finally {
//			rs.close();
//			pstmt.close();
//		}
//		Date fin = new Date();
		System.out.println("FINALIZO EJECUCION DE ACTUALIZACION DE PRECIOS");
		
		return result;
	} // doIt

} // Fin XX_VME_SeasonVerifyStatus
