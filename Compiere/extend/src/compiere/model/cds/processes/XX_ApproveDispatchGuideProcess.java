package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.model.MWarehouse;
import org.compiere.model.X_C_Order;
import org.compiere.model.X_M_Movement;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MMovement;

import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_Ref_DispatchGuideStatus;
import compiere.model.cds.X_Ref_XX_Ref_OrderDetailPackageStatus;
import compiere.model.cds.X_Ref_XX_Ref_TypeDispatchGuide;
import compiere.model.cds.X_Ref_XX_TransferStatus;
import compiere.model.cds.X_Ref_XX_VMR_OrderStatus;
import compiere.model.cds.X_XX_VLO_DispatchGuide;
import compiere.model.cds.X_XX_VLO_OrderDetailPackage;
import compiere.model.cds.X_XX_VLO_Travel;
import compiere.model.cds.X_XX_VMR_Order;


public class XX_ApproveDispatchGuideProcess extends SvrProcess {
	
	private BigDecimal kmBeforeDispatch;
	
	private Boolean verificarPedido(Integer detallePedido){
		String sql = "SELECT ODP.XX_VLO_ORDERDETAILPACKAGE_ID " +
					 "FROM XX_VLO_ORDERDETAILPACKAGE ODP " +
					 "WHERE ODP.XX_VMR_ORDER_ID = "+detallePedido+"";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				X_XX_VLO_OrderDetailPackage detallePedidoPaquetes = new X_XX_VLO_OrderDetailPackage(getCtx(), rs.getInt("XX_VLO_ORDERDETAILPACKAGE_ID"), get_TrxName());
				
				if(!detallePedidoPaquetes.getXX_OrderDetailPackageStatus().equals(X_Ref_XX_Ref_OrderDetailPackageStatus.APROBADO.getValue())){
					return false;
				}
			
			}

		}catch (Exception e) {
			return false;
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return true;
	}
	
	private boolean verificarPedidoParaTienda(int pedido, int dispatchGuide) {
		String sql = "select XX_PACKAGEQUANTITY - " +
								       	"(select COALESCE (sum(dgd.XX_PACKAGESSENT), 0) " +
								       	"from XX_VLO_DISPATCHGUIDE DG, XX_VLO_DETAILDISPATCHGUIDE dgd " +
								       	"where DG.XX_VLO_DISPATCHGUIDE_ID = dgd.XX_VLO_DISPATCHGUIDE_ID " +
								       	"and ((DG.XX_DISPATCHGUIDESTATUS = '"+X_Ref_XX_Ref_DispatchGuideStatus.EN_TRÁNSITO.getValue()+"' AND dgd.XX_VMR_ORDER_ID = "+pedido+") " +
								       	"or (DG.XX_DISPATCHGUIDESTATUS = '"+X_Ref_XX_Ref_DispatchGuideStatus.EN_TIENDA.getValue()+"' AND dgd.XX_VMR_ORDER_ID = "+pedido+"))) as faltanBultos " +
			       	"from XX_VMR_ORDER where XX_VMR_ORDER_ID = "+pedido+"";

	       //System.out.println(sql);
	       
	       PreparedStatement pstmt = null;
			ResultSet rs = null;
			try{
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				
				if(rs.next()){
				  if(rs.getInt("faltanBultos")> 0)
					return false;
				  else
					return true;
				}

			}catch (Exception e) {
				return false;
			}finally{
				
				try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
				try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			}

			return true;

		}
	
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)	;
			else if (name.equals("XX_KMBeforeDispatch")) {				
				kmBeforeDispatch = (BigDecimal) element.getParameter();	
		    } else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}	
		
	}
	
	@Override
	protected String doIt() throws Exception {
		Date utilDate = new Date();
		long lnMilisegundos = utilDate.getTime();
		Timestamp fechaActual = new Timestamp(lnMilisegundos);
		
		X_XX_VLO_DispatchGuide guiaDespacho = new X_XX_VLO_DispatchGuide(getCtx(), getRecord_ID(), get_TrxName());
		if (guiaDespacho.getXX_DispatchGuideStatus().equals(X_Ref_XX_Ref_DispatchGuideStatus.SUGERIDO.getValue()) ){
			guiaDespacho.setXX_DispatchGuideStatus(X_Ref_XX_Ref_DispatchGuideStatus.PENDIENTE.getValue());
			guiaDespacho.setXX_ApproveDispatchGuide("Y");
			guiaDespacho.save();
		}else if (guiaDespacho.getXX_TotalPackages() > 0){ 
				if (guiaDespacho.getXX_DispatchGuideStatus().equals( X_Ref_XX_Ref_DispatchGuideStatus.PENDIENTE.getValue()) ){
				/*
				 * Tercer Cambio. Se pasa a tienda pero al locator en transito
				 * */
				guiaDespacho.setXX_DispatchGuideStatus(X_Ref_XX_Ref_DispatchGuideStatus.EN_TRÁNSITO.getValue());
				guiaDespacho.setXX_ApproveDispatchGuide("N");

				// Agregado por VLOMONACO
				// Actualiza la fecha en que se cambio el estado
				Calendar cal = Calendar.getInstance();
			    Timestamp t = new Timestamp(cal.getTime().getTime());
				guiaDespacho.setXX_DateStatusTransit(t);
				// fin VLOMONACO
				
				if(guiaDespacho.save()){
					commit();
					
					/*
					 * Actualiza el viaje para q no se vuelva a utilizar
					 */
					X_XX_VLO_Travel travel = new X_XX_VLO_Travel(getCtx(), guiaDespacho.getXX_VLO_Travel_ID(), null);
					travel.setXX_ArrivedAtDestination(true);
					travel.save();
					
					/*
					 * imprime los reportes
					 * */
					/*MODIFICADO GHUCHET - Si la tienda origen es un centro de distribución se genera reporte de autorización de  salida -PROYECTO CD VALENCIA*/
					if(Utilities.esCD(guiaDespacho.getXX_DepartureWarehouse_ID())){
						new compiere.model.cds.Utilities().showReportExitAuthorization(new X_XX_VLO_DispatchGuide(Env.getCtx(), getRecord_ID(), null));						
					}
					new compiere.model.cds.Utilities().showReportDispatchGuide(new X_XX_VLO_DispatchGuide(Env.getCtx(), getRecord_ID(), null));
					
					
					PreparedStatement ps = null, ps1 = null;
					ResultSet rs1 = null;
					X_XX_VMR_Order pedido1 = null;
					int locatorHacia;
					int orderAux = 0;
					
					String sql = "SELECT DDG.XX_VMR_ORDER_ID " +
								 "FROM XX_VLO_DETAILDISPATCHGUIDE DDG " +
								 "WHERE DDG.XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue()+"' " +
								 "AND DDG.XX_VLO_DISPATCHGUIDE_ID = "+guiaDespacho.get_ID()+"";
	
					try{
						PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
						ResultSet rs = pstmt.executeQuery();
						
						while(rs.next()){
							Integer pedido = rs.getInt("XX_VMR_ORDER_ID");
							
							
							orderAux = rs.getInt(1);
							new compiere.model.cds.Utilities().showReportPlacedOrder(new X_XX_VMR_Order(Env.getCtx(), orderAux, null));
			    			
							if(verificarPedidoParaTienda(orderAux, getRecord_ID())){
								pedido1 = new X_XX_VMR_Order(getCtx(), orderAux, get_TrxName());
								sql = "select M_Locator_ID from M_Locator where M_Warehouse_ID = "+pedido1.getM_Warehouse_ID()+" and upper(VALUE) like '%TRANSITO%'";
								ps1 = DB.prepareStatement(sql, get_TrxName());
								rs1 = ps1.executeQuery();
								if (rs1.next())
									locatorHacia = rs1.getInt(1);
								else{
									rs1.close();
									ps1.close();
									return "Store Locator Lacks or Not In Default";
								}
								/*AGREGADO GHUCHET Para actualizar el inventario del pedido se obtiene el locator en transito del CD asociado a la distribución
								 * del pedido -PROYECTO CD VALENCIA*/
								int warehouseCD =Utilities.obtenerDistribucionCD(pedido1.getXX_VMR_DistributionHeader_ID());
								int locatorDesde = Utilities.obtenerLocatorEnTransito(warehouseCD).get_ID();
								
								//new Utilities().ActuaInvPedido(pedido1, 1, Env.getCtx().getContextAsInt("#XX_L_LOCATORCDENTRANSITO_ID"), locatorHacia, get_TrxName());
								new Utilities().ActuaInvPedido(pedido1, 1,locatorDesde, locatorHacia, get_TrxName());
								
								rs1.close();
								ps1.close();
							}
							
							if(verificarPedido(pedido)){
								X_XX_VMR_Order pedidoAux = new X_XX_VMR_Order(getCtx(), pedido, get_TrxName());
								pedidoAux.setXX_OrderRequestStatus(X_Ref_XX_VMR_OrderStatus.EN_TRÁNSITO.getValue());
								// agregado por vlomonaco
								// actualiza la fecha del estado cuando se pone en transito
								Calendar cal2 = Calendar.getInstance();
							    Timestamp t2 = new Timestamp(cal.getTime().getTime());									
							    pedidoAux.setXX_DateStatusTransit(t2);
								// fin vlomonaco									
								pedidoAux.save();
							}					
						}
						rs.close();
						pstmt.close();
					}catch (Exception e) {
						e.printStackTrace();
						log.fine(e.getMessage());
						return "";
					}
					
					//En caso de que sea traspaso
					sql = "SELECT DDG.M_MOVEMENTT_ID " +
						  "FROM XX_VLO_DETAILDISPATCHGUIDE DDG " +
						  "WHERE DDG.XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.TRANSFERS_BETWEEN_STORES.getValue()+"' " +
						  "AND DDG.XX_VLO_DISPATCHGUIDE_ID = "+guiaDespacho.get_ID()+"";
				    boolean traspaso = false;
					try{
						PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
						ResultSet rs = pstmt.executeQuery();
						
						while(rs.next()){
							traspaso = true;
							new compiere.model.cds.Utilities().showReportTransfer(new MMovement(Env.getCtx(), rs.getInt("M_MOVEMENTT_ID"), null));
							Integer movimiento = rs.getInt("M_MOVEMENTT_ID");
							
							//Cambia las cantidades aprobadas a las cantidades de movimiento
							String update_ammounts = " UPDATE M_MOVEMENTLINE "+ 
													 " SET MOVEMENTQTY = XX_APPROVEDQTY " +
													 " WHERE M_MOVEMENT_ID = " + movimiento;						
							DB.executeUpdate(get_TrxName(), update_ammounts );
												
							MMovement movimientoAux = new MMovement(getCtx(), movimiento, get_TrxName());
							movimientoAux.setXX_DispatchDate(fechaActual);
							movimientoAux.setXX_Status(X_Ref_XX_TransferStatus.ENTRÁNSITO.getValue());
															
							//Aprueba el movimento para que que pase al locator en transito de la tienda
							movimientoAux.setDocAction(X_M_Movement.DOCACTION_Complete);
							DocumentEngine.processIt(movimientoAux, X_M_Movement.DOCACTION_Complete);			    
						    movimientoAux.save();				
						}
						rs.close();
						pstmt.close();
						
						/*
						 * Envia correo personal de tienda
						 * */
						if (traspaso)
							sendMailToStore(guiaDespacho);
					}catch (Exception e) {
						e.printStackTrace();
						log.fine(e.getMessage());
						return "";
					}
					
					boolean movi = false;
					
					//En caso de que sea movimiento de inventario entre cd
					sql = "SELECT DDG.M_MOVEMENTM_ID " +
						  "FROM XX_VLO_DETAILDISPATCHGUIDE DDG " +
						  "WHERE DDG.XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.INVENTORY_MOVEMENT.getValue()+"' " +
						  "AND DDG.XX_VLO_DISPATCHGUIDE_ID = "+guiaDespacho.get_ID()+"";
				
					try{
						PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
						ResultSet rs = pstmt.executeQuery();
						
						while(rs.next()){
							movi = true;
							new compiere.model.cds.Utilities().showReportMovement(new MMovement(Env.getCtx(), rs.getInt("M_MOVEMENTM_ID"), null));
							Integer movimiento = rs.getInt("M_MOVEMENTM_ID");
							
							//Cambia las cantidades aprobadas a las cantidades de movimiento
							String update_ammounts = " UPDATE M_MOVEMENTLINE "+ 
													 " SET MOVEMENTQTY = XX_APPROVEDQTY " +
													 " WHERE M_MOVEMENT_ID = " + movimiento;						
							DB.executeUpdate(get_TrxName(), update_ammounts );
												
							MMovement movimientoAux = new MMovement(getCtx(), movimiento, get_TrxName());
							movimientoAux.setXX_DispatchDate(fechaActual);
							movimientoAux.setXX_Status(X_Ref_XX_TransferStatus.ENTRÁNSITO.getValue());
															
							//Aprueba el movimento para que que pase al locator en transito de la tienda
							movimientoAux.setDocAction(X_M_Movement.DOCACTION_Complete);
							DocumentEngine.processIt(movimientoAux, X_M_Movement.DOCACTION_Complete);			    
						    movimientoAux.save();				
						}
						rs.close();
						pstmt.close();
						
						// Envio correo al CD
						if (movi)
							sendMailToWarehouse(guiaDespacho);
						
					}catch (Exception e) {
						e.printStackTrace();
						log.fine(e.getMessage());
						return "";
					}
					
					//en caso de que sea devoluciones
					sql = "SELECT DDG.M_MOVEMENTR_ID " +
						  "FROM XX_VLO_DETAILDISPATCHGUIDE DDG " +
						  "WHERE DDG.XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.STORE_RETURNS.getValue()+"' " +
						  "AND DDG.XX_VLO_DISPATCHGUIDE_ID = "+guiaDespacho.get_ID()+"";
					
					boolean dev = false;
				
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try{
						pstmt = DB.prepareStatement(sql, get_TrxName());
						rs = pstmt.executeQuery();
						
						while(rs.next()){
							dev = true;
							new compiere.model.cds.Utilities().showReportReturn(new MMovement(Env.getCtx(), rs.getInt("M_MOVEMENTR_ID"), null));
							Integer movimiento = rs.getInt("M_MOVEMENTR_ID");
							
							//Cambia las cantidades aprobadas a las cantidades de movimiento
							String update_ammounts = " UPDATE M_MOVEMENTLINE "+ 
													 " SET MOVEMENTQTY = XX_APPROVEDQTY " +
													 " WHERE M_MOVEMENT_ID = " + movimiento;					
							DB.executeUpdate(get_TrxName(), update_ammounts );
												
							MMovement movimientoAux = new MMovement(getCtx(), movimiento, get_TrxName());
							movimientoAux.setXX_DispatchDate(fechaActual);
							movimientoAux.setXX_Status(X_Ref_XX_TransferStatus.ENTRÁNSITO.getValue());
							
							//Aprueba el movimento para que que pase al locator en transito de la tienda
							movimientoAux.setDocAction(X_M_Movement.DOCACTION_Complete);
							DocumentEngine.processIt(movimientoAux, X_M_Movement.DOCACTION_Complete);			    
						    movimientoAux.save();	
						}
						
						/*
						 * Envia correo personal de tienda
						 * */
						if (dev)
							sendMailToStore(guiaDespacho);
					}catch (Exception e) {
						e.printStackTrace();
						e.getMessage();
						log.fine(e.getMessage());
						return "";
					}
					finally{
						DB.closeResultSet(rs);
						DB.closeStatement(pstmt);
					}
					

				}
				else{
					return Msg.translate(getCtx(), "XX_ErrorSaveDispatchGuide");
				}
			}
		}else{
			return Msg.translate(getCtx(), "XX_NotApproveDGwhit0pack");
		}
		/*
		 * Fin del Tercer Cambio
		 * */
		return "";
	}
	
	private void sendMailToWarehouse(X_XX_VLO_DispatchGuide guiaDespacho){		
		MWarehouse almaSalida = new MWarehouse(getCtx(), guiaDespacho.getXX_DepartureWarehouse_ID(), null);
		MWarehouse almaLleada = new MWarehouse(getCtx(), guiaDespacho.getXX_ArrivalWarehouse_ID(), null);
		X_XX_VLO_Travel viaje = new X_XX_VLO_Travel(getCtx(), guiaDespacho.getXX_VLO_Travel_ID(), null);
		
		/*
		 * Fecha
		 * */
		Date fechaViaje = new Date(viaje.getXX_TravelDate().getTime());
		SimpleDateFormat formatoY = new SimpleDateFormat("yyyy");
		String ano = formatoY.format(fechaViaje);
		
		SimpleDateFormat formatoM = new SimpleDateFormat("MM");
		String mes = formatoM.format(fechaViaje);
		
		SimpleDateFormat formatoD = new SimpleDateFormat("dd");
		String dia = formatoD.format(fechaViaje);
		
		
		String Mensaje = Msg.getMsg( getCtx(), 
				 "XX_MsgDGStore", 
				 new String[]{guiaDespacho.getDocumentNo(),
							  almaSalida.getValue()+"-"+almaSalida.getName(),
							  almaLleada.getValue()+"-"+almaLleada.getName(),
							  dia+"/"+mes+"/"+ano
		});
		
		//Al Jefe de CD
		//Selecciono el o los Jefes de CD
		String SQL = "select ad_user_id from AD_User_Roles where AD_Role_ID=1000102	";
		
    	Vector<Integer> storeManagers = new Vector<Integer>();
    	PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();
			
			while(rs.next()){	
				storeManagers.add(rs.getInt("AD_USER_ID"));
			}							
									
		}
		catch(Exception a){
			log.log(Level.SEVERE,SQL,a);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		//Envio correos a los jefes
		Utilities f = null;
		for(int i=0; i<storeManagers.size();i++){
	
			f = new Utilities(Env.getCtx(), null, 1000055, Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, storeManagers.get(i),null);
			try {
				f.ejecutarMail(); 
			} catch (Exception e) {
				e.printStackTrace();
			}
			f = null;
		}
		

	}//Fin del metodo
	
	private void sendMailToStore(X_XX_VLO_DispatchGuide guiaDespacho){		
		MWarehouse almaSalida = new MWarehouse(getCtx(), guiaDespacho.getXX_DepartureWarehouse_ID(), null);
		MWarehouse almaLleada = new MWarehouse(getCtx(), guiaDespacho.getXX_ArrivalWarehouse_ID(), null);
		X_XX_VLO_Travel viaje = new X_XX_VLO_Travel(getCtx(), guiaDespacho.getXX_VLO_Travel_ID(), null);
		
		/*
		 * Fecha
		 * */
		Date fechaViaje = new Date(viaje.getXX_TravelDate().getTime());
		SimpleDateFormat formatoY = new SimpleDateFormat("yyyy");
		String ano = formatoY.format(fechaViaje);
		
		SimpleDateFormat formatoM = new SimpleDateFormat("MM");
		String mes = formatoM.format(fechaViaje);
		
		SimpleDateFormat formatoD = new SimpleDateFormat("dd");
		String dia = formatoD.format(fechaViaje);
		
		
		String Mensaje = Msg.getMsg( getCtx(), 
				 "XX_MsgDGStore", 
				 new String[]{guiaDespacho.getDocumentNo(),
							  almaSalida.getValue()+"-"+almaSalida.getName(),
							  almaLleada.getValue()+"-"+almaLleada.getName(),
							  dia+"/"+mes+"/"+ano
		});
		
		//Al Gerente de Tienda
		//Selecciono el o los gerentes de Tienda
		String SQL = "SELECT AD_USER_ID FROM AD_USER WHERE ISACTIVE='Y' " +
					"AND C_BPARTNER_ID IN "+
					"("+
						"SELECT C_BPARTNER_ID " +
						"FROM C_BPARTNER WHERE isActive='Y' "+
						"AND C_JOB_ID =" + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_STOREMAN_ID")+" " +
						"AND M_WAREHOUSE_ID = "+guiaDespacho.getXX_ArrivalWarehouse_ID()+" " +
						"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")"+
					") "+
					"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")";		
		
    	Vector<Integer> storeManagers = new Vector<Integer>();
    	PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();
			
			while(rs.next()){	
				storeManagers.add(rs.getInt("AD_USER_ID"));
			}							
									
		}
		catch(Exception a){
			log.log(Level.SEVERE,SQL,a);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		//Envio correos a los gerentes
		Utilities f = null;
		for(int i=0; i<storeManagers.size();i++){
	
			f = new Utilities(Env.getCtx(), null, 1000055, Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, storeManagers.get(i),null);
			try {
				f.ejecutarMail(); 
			} catch (Exception e) {
				e.printStackTrace();
			}
			f = null;
		}
		
		//*********************************
		//Selecciono los asesores de almacen
    	SQL = "SELECT AD_USER_ID FROM AD_USER WHERE  ISACTIVE='Y' " +
    			"AND C_BPARTNER_ID IN "+
				"("+
					"SELECT C_BPARTNER_ID FROM C_BPARTNER WHERE isActive='Y' "+
					"AND C_JOB_ID =" + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_DEPASE_ID")+" " +
					"AND M_WAREHOUSE_ID = "+guiaDespacho.getXX_ArrivalWarehouse_ID()+" " +
					"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")"+
				") "+
				"AND AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")";			
		
    	Vector<Integer> warehouseAsessors = new Vector<Integer>();
		try 
		{
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				warehouseAsessors.add(rs.getInt("AD_USER_ID"));
			}
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,SQL,a);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		//Envio correos a los asesores
		Utilities m = null;
		for(int i=0; i<warehouseAsessors.size();i++){
			
			m = new Utilities(Env.getCtx(), null, 1000055, Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, warehouseAsessors.get(i),null);
			try {
				m.ejecutarMail();
			} catch (Exception e) {
				e.printStackTrace();
			}
			m = null;
		}	
		
		
		//*********************************
		//Selecciono los Gerentes de Area de la tienda
		String job = "GERENTE DE AREA";
		
		SQL = "SELECT us.AD_USER_ID " +
			  "FROM C_BPARTNER cb,M_WAREHOUSE wh, C_JOB jb, AD_USER us " +
		      "WHERE cb.isActive='Y' AND us.isActive='Y' " +
		      "AND wh.m_warehouse_id=cb.m_warehouse_id " +
		      "AND cb.C_JOB_ID = jb.C_JOB_ID " +
		      "AND cb.C_BPARTNER_ID = us.C_BPARTNER_ID " +
		      "AND jb.NAME like '%"+ job +"%' " +
		      "AND cb.M_WAREHOUSE_ID = "+guiaDespacho.getXX_ArrivalWarehouse_ID()+" " +
		      "AND cb.AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+") " +
			  "AND us.AD_Client_ID IN (0,"+Env.getCtx().getAD_Client_ID()+")";
		
    	Vector<Integer> managerArea = new Vector<Integer>();
		try 
		{
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				managerArea.add(rs.getInt("AD_USER_ID"));
			}
		}
		catch(Exception a)
		{	
			log.log(Level.SEVERE,SQL,a);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		//Envio correos a los gtes area
		Utilities u = null;
		for(int i=0; i<managerArea.size();i++){
			
			u = new Utilities(Env.getCtx(), null, 1000055, Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, managerArea.get(i),null);
			try {
				u.ejecutarMail();
			} catch (Exception e) {
				e.printStackTrace();
			}
			m = null;
		}	
		
	}//Fin del metodo
}
