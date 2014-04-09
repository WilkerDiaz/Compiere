package compiere.model.cds.processes;

import java.awt.Container;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.model.MClient;
import org.compiere.model.MWarehouse;
import org.compiere.model.X_AD_User;
import org.compiere.model.X_M_InOut;
import org.compiere.model.X_M_Movement;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MInOut;
import compiere.model.cds.MMovement;
import compiere.model.cds.MOrder;
import compiere.model.cds.MVMROrderRequestDetail;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_VMR_OrderStatus;
import compiere.model.cds.X_XX_VMR_Order;

/**
 * Recepción en Tienda de Orden de Compra de Despacho Directo
 * @author Gabrielle Huchet.
 *
 */

public class XX_ProcessReceptionInStoreDD extends SvrProcess{

	static CLogger  log = CLogger.getCLogger(XX_ProcessReceptionInStoreDD.class);
	private Integer bultosContados = 0;
	private Integer bultosFactura = 0;
	private Integer diasHolgura = 30;
	
	protected String doIt() throws Exception {
	
		MOrder order = new MOrder(Env.getCtx(),getRecord_ID(),null);
		boolean validDate = true;
		String msg="";
		validDate = ValidateReceptionDate(order);
		if (order.get_ValueAsString("XX_ProcessReceptionStore").compareTo("N") ==0 && validDate){
		
			try{
				//Busco ID del pedido asociado a la 0/C
				String sql = "\nSELECT M_InOut_ID FROM M_InOut" +
							 "\nWHERE C_ORDER_ID = "+order.get_ID()+" "+
							 "\nAND AD_CLIENT_ID = "+Env.getCtx().getAD_Client_ID();
				int M_InOut_ID = 0;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try
				{
					pstmt = DB.prepareStatement(sql, null);
					rs = pstmt.executeQuery();				
					while(rs.next()){
						M_InOut_ID = rs.getInt(1);
					}
		
				}
				catch (Exception e) {
					log.log(Level.SEVERE, sql,e.getMessage());
				}
				finally {
					try {
						rs.close();
						pstmt.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				MInOut io = new MInOut(Env.getCtx(),M_InOut_ID,null);
				io.set_Value("XX_CountedPackagesQty",bultosContados);
				io.set_Value("XX_InvPackageQty",bultosFactura);
				io.save();
				
				//Busco ID del pedido asociado a la 0/C
				sql = "\nSELECT XX_VMR_Order_ID" +
					"\nFROM XX_VMR_Order" +
					"\nWHERE C_ORDER_ID = "+order.get_ID()+" "+
				 	"\nAND AD_CLIENT_ID = "+Env.getCtx().getAD_Client_ID();
				int XX_VMR_Order_ID = 0;
				pstmt = null;
				rs = null;
				try
				{
					pstmt = DB.prepareStatement(sql, null);
					rs = pstmt.executeQuery();				
					while(rs.next()){
						XX_VMR_Order_ID = rs.getInt(1);
					}
		
				}
				catch (Exception e) {
					log.log(Level.SEVERE, sql,e.getMessage());
				}
				finally {
					try {
						rs.close();
						pstmt.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
	
				if (XX_VMR_Order_ID != 0){
				//PEDIDO
				X_XX_VMR_Order pedido = new X_XX_VMR_Order(Env.getCtx(),XX_VMR_Order_ID,null);
				pedido.setXX_OrderRequestStatus(X_Ref_XX_VMR_OrderStatus.EN_TIENDA.getValue());
		
				// actualiza la fecha del estado cuando se pone en tienda
				Calendar cal = Calendar.getInstance();
			    Timestamp t = new Timestamp(cal.getTime().getTime());									
				pedido.setXX_DateStatusOnStore(t);
				pedido.save();
				
				MWarehouse tienda = new MWarehouse(Env.getCtx(), pedido.getM_Warehouse_ID(),null);
				
				//Busco ID de las ubicaciones  en transito y en  tienda del almacen dado
				sql = "\nSELECT (CASE WHEN ISDEFAULT = 'N' THEN M_LOCATOR_ID ELSE 0 END) LOCATOR_DESDE, " +
					"\n(CASE WHEN ISDEFAULT = 'Y' THEN M_LOCATOR_ID ELSE 0 END)LOCATOR_HACIA " +
					"\nFROM M_LOCATOR " +
					"\nWHERE M_WAREHOUSE_ID= "+tienda.get_ID()+" "+
				 	"\nAND AD_CLIENT_ID = "+Env.getCtx().getAD_Client_ID();
				System.out.println(sql);
				int locator_Desde = 0;
				int locator_Hacia = 0;
				pstmt = null;
				rs = null;
				try
				{
					pstmt = DB.prepareStatement(sql, null);
					rs = pstmt.executeQuery();				
					while(rs.next()){
						Integer desde = rs.getInt(1);
						Integer hacia = rs.getInt(2);
						if(desde!= 0)
							locator_Desde = rs.getInt(1);
						if(hacia!=0)
							locator_Hacia = rs.getInt(2);
					}
		
				}
				catch (Exception e) {
					log.log(Level.SEVERE, sql,e.getMessage());
				}
				finally {
					try {
						rs.close();
						pstmt.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				Utilities u = new Utilities();
				MMovement movimiento = null;
				try {
					movimiento = u.GenerarMovimiento(locator_Desde,locator_Hacia,get_Trx());
				} catch (Exception e) {
					return e.getMessage();
				}
				//Busco ID de las lineas de detalle del pedido dado
				sql = "SELECT XX_VMR_ORDERREQUESTDETAIL_ID as detalle " +
				"FROM XX_VMR_ORDERREQUESTDETAIL WHERE XX_VMR_ORDER_ID = " + pedido.get_ID();
				System.out.println(sql);
				pstmt = null;
				rs = null;
				try
				{
					MVMROrderRequestDetail detallePedido = null;
					pstmt = DB.prepareStatement(sql, null);
					rs = pstmt.executeQuery();				
					while(rs.next()){
						detallePedido = new MVMROrderRequestDetail(Env.getCtx(),rs.getInt(1), get_Trx());
						u.GenerarLineaMov(locator_Desde,locator_Hacia,movimiento.getM_Movement_ID(),detallePedido,get_Trx());
					}
		
				}
				catch (Exception e) {
					log.log(Level.SEVERE, sql,e.getMessage());
					return e.getMessage();
				}
				finally {
					try {
						rs.close();
						pstmt.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				System.out.println( "mov id: " +movimiento.get_ID());
				movimiento.setDocAction(X_M_Movement.DOCACTION_Complete);
				DocumentEngine.processIt(movimiento, X_M_Movement.DOCACTION_Complete);
				movimiento.save();
				order.set_Value("XX_ProcessReceptionStore",'Y');
				if(order.save()){
					enviarCorreoFinanzas(order);
					enviarCorreoPlanificacion(order);
					enviarCorreoComprador(order);
				}
				
				}else {
					msg = "No se Encontró Pedido Asociado." +
							"\nContacte al Administrador del Sistema";
					ADialog.error(1, new Container(),msg);
					return msg;
				}
			}catch (Exception e) {
				e.printStackTrace();
				msg= "No se pudo completar el proceso. Contacte al administrador del sistema.";
				ADialog.error(1, new Container(),msg);
				//rollback();
			}
		}else if(order.get_ValueAsString("XX_ProcessReceptionStore").compareTo("N")!=0){
			msg ="El proceso ya se completó anteriormente";
			ADialog.error(1, new Container(), msg);
			return msg;
		}else if(!validDate) {
			msg= "No se pueden recibir los productos dado que la fecha actual " +
					"\nno coincide con la fecha estimada de llegada y la holgura otorgada de la O/C.";
			msg += "\n\nDirijase al Coordinador de Cuentas por Pagar.";
			ADialog.error(1, new Container(), msg);
			return msg;
		}
		msg= "Proceso completado con éxito";
		ADialog.info(1, new Container(), msg);
		return msg;
	}

	private boolean ValidateReceptionDate(MOrder order) {
		
		if(getCtx().getAD_Role_ID() == getCtx().getContextAsInt("#XX_L_ROLEACCOUNTSPAYABLE_ID")){
			return true;
		}
		Calendar fromDate = Calendar.getInstance();
		fromDate.setTime(order.getXX_EstimatedDate());
		System.out.println("FECHA DESDE 1: "+fromDate.getTime());
		Calendar toDate = Calendar.getInstance();
		toDate.setTime(order.getXX_EstimatedDate());
		System.out.println("FECHA HASTA 1: "+toDate.getTime());
		toDate.add(Calendar.DATE, diasHolgura);
		System.out.println("FECHA HASTA 2: "+toDate.getTime());
		Calendar today = Calendar.getInstance();
		today.set(Calendar.AM_PM, 0);
		today.set(Calendar.HOUR, 0);
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		int nonBusinessDays = lookupNonBusinessDay(fromDate, toDate);
		int weekendDays = lookupWeekend(fromDate);
		int totalDias = nonBusinessDays + weekendDays;
		toDate.add(Calendar.DATE, totalDias);
		System.out.println("FECHA DESDE 3: "+fromDate.getTime());
		System.out.println("FECHA HASTA 3: "+toDate.getTime());
		if(today.getTimeInMillis() < fromDate.getTimeInMillis() || today.getTimeInMillis() > toDate.getTimeInMillis()){
			return false;
		}
		return true;
		
	}
	private Integer lookupWeekend(Calendar date){
		Calendar dateTemp = Calendar.getInstance();
		dateTemp.setTime(date.getTime());
		dateTemp.add(Calendar.DATE, 2);
		System.out.println("FECHA DESDE: "+date.getTime());
		int dayOfWeek = dateTemp.get(Calendar.DAY_OF_WEEK);
		if(dayOfWeek == Calendar.SUNDAY){
			return 3;
//		}else if(dayOfWeek == Calendar.SATURDAY){
//			return 2;
		}else 
			return 0;
	}
	private Integer lookupNonBusinessDay(Calendar fromDate, Calendar toDate) {
		
		int dias = 0;
		Timestamp from = new Timestamp(fromDate.getTimeInMillis());
		Timestamp to= new Timestamp(toDate.getTimeInMillis());
		System.out.println("date from: "+ DB.TO_DATE(from, true));
		//Busco cuantos dias feriados hay entre los dias de holgura a la fecha de recepción
		String sql = "\nSELECT Count(*) dias " +
		"\nFROM C_NonBusinessDay WHERE AD_CLIENT_ID = " +Env.getCtx().getAD_Client_ID() +
		"\n AND trunc(DATE1) >= "+DB.TO_DATE(from, true)+
		"\n AND trunc(DATE1) <= "+DB.TO_DATE(to, true)+
		"\n AND to_char(DATE1,'d') NOT IN (1,7)";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();				
			while(rs.next()){
				dias = rs.getInt(1);
			}

		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql,e.getMessage());
		}
		finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return dias;
	}
	
	 /**
     * Llamada a la funcion para enviar el correo indicando que la orden de despacho directo a sido recibida en tienda 
     * y ya se puede procesar la factura
     * Envia correo a los asistentes y coordinador de cuentas por pagar
     */
	private boolean enviarCorreoFinanzas(MOrder order) {
		String sql1 = "\nSELECT DISTINCT U.AD_USER_ID " +
				"\nFROM AD_User_Roles UR INNER JOIN AD_USER U ON (U.AD_USER_ID= UR.AD_USER_ID) " +
				"\nWHERE UR.AD_ROLE_ID IN (" +
				"\nSELECT AD_Role_ID FROM AD_Role WHERE name IN (" +
				"\n'BECO Asistente de Cuentas por Pagar','BECO Coordinador de Cuentas por Pagar'," +
				"\n'BECO Cuentas por Pagar')) and U.IsActive='Y' ";                                       //REVISAR ROLES
		
		
		//OJO NO HAY NADIE CON EL ROL DE ASISTENTE Y CUENTAS POR PAGAR, SOLO HAY PARA COORDINADOR DE CUENTAS POR PAGAR
		System.out.println(sql1);
		
		PreparedStatement pstmt1 =null;
		ResultSet rs1 = null;
		try{
		
			pstmt1 = DB.prepareStatement(sql1, null);
			rs1 = pstmt1.executeQuery();
			
			while(rs1.next())
			{
				X_AD_User destinatario = new X_AD_User( Env.getCtx(), rs1.getInt(1), null);
				MWarehouse tienda = new MWarehouse(Env.getCtx(), order.getM_Warehouse_ID(), null);
				String emailTo = destinatario.getEMail();
				//System.out.println("email: "+destinatario.getEMail());
				MClient m_client = MClient.get(Env.getCtx());
				
//				String subject = "O/C de Despacho Directo Pendiente Procesar Factura" ;
				
				String subject = Msg.getMsg(getCtx(), "DDOCReceivedSubjet"); 	
				
//				String msg = "Los productos de la  O/C nro: {0} de Despacho Directo se recibieron en la tienda {1}. A partir de este momento puede procesar la factura correspondiente.
		
				String msg = Msg.getMsg(getCtx(), "DispatchDirectOCReceived", 
						new String[] { "" + order.getDocumentNo(), "" +tienda.getValue()+"-"+tienda.getName()});
				
				EMail email = m_client.createEMail(null, emailTo, "Finanzas", subject, msg);
				
				if (email != null)
				{			
					String status = email.send();
				
					log.info("Email Send status: " + status);
					
					if (email.isSentOK()){}
					else
						return false;
				}
				else
					return false;
				
			}
		}catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return false;
		}finally{
			try {rs1.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt1.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return true;
	}
	
	
	 /**
     * Llamada a la funcion para enviar el correo indicando que la orden de despacho directo a sido recibida en tienda 
     * Envia correo a Jefe de Planificación y Planificadores
     */
	private boolean enviarCorreoPlanificacion(MOrder order) {
		String sql1 = "SELECT DISTINCT AD_USER_ID FROM AD_User_Roles " +
		"WHERE AD_ROLE_ID IN (SELECT AD_Role_ID " +
		"FROM AD_Role " +
		"WHERE name IN ('BECO Jefe de Planificacion', 'BECO Planificador')) and IsActive='Y'";
		
//		System.out.println(sql1);
		
		PreparedStatement pstmt1 =null;
		ResultSet rs1 = null;
		try{
		
			pstmt1 = DB.prepareStatement(sql1, null);
			rs1 = pstmt1.executeQuery();
			
			while(rs1.next())
			{
				X_AD_User plan = new X_AD_User( Env.getCtx(), rs1.getInt(1), null);
				MWarehouse tienda = new MWarehouse(Env.getCtx(), order.getM_Warehouse_ID(), null);
				String emailTo = plan.getEMail();
//				System.out.println("Planificación: "+plan.getEMail());
				MClient m_client = MClient.get(Env.getCtx());
				
				String subject = "O/C de Despacho Directo Recibida en Tienda" ;
				
//				String msg = "Los productos de la  O/C nro: {0} de Despacho Directo se recibieron en la tienda {1};
		
				String msg = Msg.getMsg(getCtx(), "DDOCReceived", 
						new String[] { "" + order.getDocumentNo(), "" +tienda.getValue()+"-"+tienda.getName()});
				
				EMail email = m_client.createEMail(null, emailTo, "Planificación", subject, msg);
				
				if (email != null)
				{			
					String status = email.send();
				
					log.info("Email Send status: " + status);
					
					if (email.isSentOK()){}
					else
						return false;
				}
				else
					return false;
				
			}
		}catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return false;
		}finally{
			try {rs1.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt1.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return true;
	}
	
	 /**
     * Llamada a la funcion para enviar el correo indicando que la orden de despacho directo a sido recibida en tienda 
     * Envia correo a Comprador
     */
	private boolean enviarCorreoComprador(MOrder order) {
		String sql1 = "SELECT DISTINCT U.AD_USER_ID " +
				"FROM C_ORDER O JOIN C_BPARTNER P ON (O.XX_USERBUYER_ID = P.C_BPARTNER_ID) " +
				"JOIN AD_USER U ON (P.C_BPARTNER_ID = U.C_BPARTNER_ID) " +
				"WHERE  O.C_ORDER_ID = " +order.get_ID();
		
//		System.out.println(sql1);
		
		PreparedStatement pstmt1 =null;
		ResultSet rs1 = null;
		try{
		
			pstmt1 = DB.prepareStatement(sql1, null);
			rs1 = pstmt1.executeQuery();
			
			while(rs1.next())
			{
				X_AD_User comp = new X_AD_User( Env.getCtx(), rs1.getInt(1), null);
				MWarehouse tienda = new MWarehouse(Env.getCtx(), order.getM_Warehouse_ID(), null);
				String emailTo = comp.getEMail();
//				System.out.println("Planificación: "+plan.getEMail());
				MClient m_client = MClient.get(Env.getCtx());
				
				String subject = "O/C de Despacho Directo Recibida en Tienda" ;
				
//				String msg = "Los productos de la  O/C nro: {0} de Despacho Directo se recibieron en la tienda {1};
		
				String msg = Msg.getMsg(getCtx(), "DDOCReceived", 
						new String[] { "" + order.getDocumentNo(), "" +tienda.getValue()+"-"+tienda.getName()});
				
				EMail email = m_client.createEMail(null, emailTo, "Comprador", subject, msg);
				
				if (email != null)
				{			
					String status = email.send();
				
					log.info("Email Send status: " + status);
					
					if (email.isSentOK()){}
					else
						return false;
				}
				else
					return false;
				
			}
		}catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return false;
		}finally{
			try {rs1.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt1.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return true;
	}
	
	protected void prepare() {
		ProcessInfoParameter[] parameter = getParameter();

		for (ProcessInfoParameter element : parameter) {
			
			if (element.getParameter()!=null) {
				if (element.getParameterName().equals("XX_CountedPackagesQty")) 
					bultosContados = Integer.parseInt(element.getParameter().toString());
			}	
			if (element.getParameter()!=null) {
				if (element.getParameterName().equals("XX_InvPackageQty")) 
					bultosFactura = Integer.parseInt(element.getParameter().toString());
			}	
		}
	}

}
