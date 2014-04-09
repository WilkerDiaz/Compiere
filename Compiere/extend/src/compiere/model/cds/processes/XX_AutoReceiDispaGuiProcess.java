package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;


import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;


import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_Ref_DispatchGuideStatus;
import compiere.model.cds.X_XX_VLO_DispatchGuide;
import compiere.model.cds.X_XX_VLO_Travel;

public class XX_AutoReceiDispaGuiProcess extends SvrProcess {
			 
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}
	
	@Override 
	// Realizado por Wdiaz y JPires
	// Cambia el pedido a En Tienda y actualiza todos los productos el inventario de Compiere 
	protected String doIt() throws Exception{
		String sqlAux = "SELECT XX_VLO_DISPATCHGUIDE_ID " +
						"from XX_VLO_DispatchGuide " +
						"where XX_DispatchGuideStatus = '"+X_Ref_XX_Ref_DispatchGuideStatus.EN_TRÁNSITO.getValue()+"'";

		PreparedStatement pstmtAux = DB.prepareStatement(sqlAux, get_TrxName());
		ResultSet rsAux = pstmtAux.executeQuery();
		
		Utilities procesoRecepcion = new Utilities();

		while(rsAux.next()){
			Integer tiempoDespachoRuta = null;
			X_XX_VLO_DispatchGuide guiaDespacho = new X_XX_VLO_DispatchGuide(getCtx(), rsAux.getInt("XX_VLO_DISPATCHGUIDE_ID"), get_TrxName());
			
			PreparedStatement pstmt10 = null;
			ResultSet rs10 = null;
			
			String sql10 = "SELECT XX_TIMEFORDISPATCH " +
						 "FROM XX_VLO_CIRCUITTOUR " +
						 "WHERE XX_DEPARTUREWAREHOUSE_ID = "+guiaDespacho.getXX_DepartureWarehouse_ID()+" " +
				 		 "AND XX_ARRIVALWAREHOUSE_ID = "+guiaDespacho.getXX_ArrivalWarehouse_ID()+" " +
				 		 "AND ISACTIVE = 'Y'";
  
			try{
				  pstmt10 = DB.prepareStatement(sql10, get_TrxName());
				rs10 = pstmt10.executeQuery();
				
				if(rs10.next()){
					tiempoDespachoRuta = rs10.getInt("XX_TIMEFORDISPATCH");
				}
				
				if(tiempoDespachoRuta == null){
					sql10 = "SELECT SUM(XX_TIMEFORDISPATCH) XX_TIMEFORDISPATCH " +
							 "FROM XX_VLO_CIRCUITTOUR " +
							 "WHERE (XX_DEPARTUREWAREHOUSE_ID = "+guiaDespacho.getXX_DepartureWarehouse_ID()+" " +
							 "AND XX_ARRIVALWAREHOUSE_ID = "+Env.getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID")+") " +
							 "OR (XX_DEPARTUREWAREHOUSE_ID = "+guiaDespacho.getXX_ArrivalWarehouse_ID()+" " +
							 "AND XX_ARRIVALWAREHOUSE_ID = "+Env.getCtx().getContextAsInt("#XX_L_WAREHOUSECENTRODIST_ID")+") " +
					 		 "AND ISACTIVE = 'Y'";
					
					pstmt10 = DB.prepareStatement(sql10, get_TrxName());
					rs10 = pstmt10.executeQuery();
					
					if(rs10.next()){
						tiempoDespachoRuta = rs10.getInt("XX_TIMEFORDISPATCH");
					}
				}
				
				
				
			}
			catch (SQLException e){
				e.getMessage();
			} finally{
				rs10.close();
				pstmt10.close();
			}

			X_XX_VLO_Travel viaje = new X_XX_VLO_Travel(getCtx(),guiaDespacho.getXX_VLO_Travel_ID(), get_TrxName());
			
			long fechaSalida = viaje.getXX_TravelDate().getTime();
			long fechaActual = new Date().getTime();			
			long resta = fechaActual - fechaSalida;						
			double minutos = resta / 60000;
			
			if (minutos >= tiempoDespachoRuta){
				procesoRecepcion.recibirGuiaDespacho(guiaDespacho.get_ID(), get_TrxName());
			}
			
		}
		
		pstmtAux.close();
		rsAux.close();
		
		return "";
	}


}
