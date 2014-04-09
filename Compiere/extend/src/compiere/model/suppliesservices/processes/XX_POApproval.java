package compiere.model.suppliesservices.processes;

import java.awt.Container;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.apps.ADialog;
import org.compiere.framework.PrintInfo;
import org.compiere.framework.Query;
import org.compiere.model.MClient;
import org.compiere.model.X_C_Order;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.process.DocumentEngine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.cds.MBPartner;
import compiere.model.cds.MOrder;
import compiere.model.cds.MUser;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_OrderType;
import compiere.model.cds.X_Ref_XX_Ref_ContactType;
import compiere.model.cds.X_XX_VMR_PO_ApprovalRole;

public class XX_POApproval extends SvrProcess{
	/** Purchase of Supplies and Services 
	 * Maria Vintimilla Funcion 9
	 * Aprobación de OC de Bienes y Servicios. 
	 * Send mail **/
	@Override
	protected String doIt() throws Exception {
		int Role_ID = Env.getCtx().getContextAsInt("#AD_Role_ID");
				
		try {
			MOrder Order = new MOrder(Env.getCtx(),getRecord_ID(),get_TrxName());
			
			// Se verifica que la orden esté lista para poder ser aprobada
			if(!Order.isXX_OrderReadyStatus()){
				ADialog.info(1, new Container(),Msg.translate(Env.getCtx(), "XX_OCReady"));
			}
			else{
				approvePo(Order, Role_ID, getRecord_ID());
			}
			
		}//try
		catch(Exception e){
			return e.getMessage();
		}//catch
		return "";
	}// Fin doIt
	
	@Override
	protected void prepare() {
		
	}// Fin prepare
	
	/** get_UT Get the Order Amount in U.T.
	 * @param Amount Order Amount
	 * @return Order Amount in U.T.
	 *  */
	private BigDecimal get_UT (BigDecimal Amount){
		BigDecimal UT = new BigDecimal(0);
		BigDecimal UTA = new BigDecimal(0);
		String sql = "SELECT XX_UT"
					+" FROM AD_CLIENT "
					+" WHERE XX_UT IS NOT NULL " 
					+" AND AD_Client_ID = "  + Env.getCtx().getAD_Client_ID()
					+" ORDER BY CREATED DESC";
		//System.out.println(sql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if(rs.next()){
				UT = rs.getBigDecimal("XX_UT"); 
			}// while
		}
		catch (Exception e){
			log.saveError("ErrorSql Obtencion de UT", Msg.getMsg(Env.getCtx(), e.getMessage()));
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}		
		UTA = Amount.divide(UT, 0, RoundingMode.HALF_UP);
		return UTA;
		
	}// Fin get_Distribution
	
	/** get_Limit
	 * Se obtiene la información necesaria para obtener los roles de aprobacion
	 * asi como los limites 
	 * @param Role_ID Rol del usuario que desea aprobar la OC
	 * @return ApprovalRole Informacion sobre aprobacion de OC
	 *  */
	private X_XX_VMR_PO_ApprovalRole get_Limit(int Role_ID){
		X_XX_VMR_PO_ApprovalRole approvalRole = null;
		PreparedStatement pstmtRole = null;
		ResultSet rsRole = null;

		String sqlRole = " SELECT XX_VMR_PO_ApprovalRole_ID IDROLE" +
				" FROM XX_VMR_PO_ApprovalRole " +
				" WHERE AD_ROLE_ID = " + Role_ID +
				" AND AD_Client_ID = " + Env.getCtx().getAD_Client_ID();
		
		try {
			pstmtRole = DB.prepareStatement(sqlRole, null); 
			rsRole = pstmtRole.executeQuery();
			
			if(rsRole.next()) {
				approvalRole = new X_XX_VMR_PO_ApprovalRole( getCtx(), rsRole.getInt("IDROLE"), null);
			}
		
		}
		catch (Exception e){
			log.saveError("ErrorSql Verificacion de aprobacion de OC BYS", 
					Msg.getMsg(Env.getCtx(), e.getMessage()));
		}
		finally {
			DB.closeResultSet(rsRole);
			DB.closeStatement(pstmtRole);
		}
		
		return approvalRole;
	
	}// Fin get_Limit
	
	
	/** approvePo Get the Order Amount in U.T.
	 * @param Order Purchase Order
	 * @param Distribution Determine if the PO is already distributed
	 * @param Role_ID Role of the user that is approving de PO
	 * @param Order_ID Order ID to be used in the mail
	 * @param needSuperior If the user is Level 2 or not
	 * @param User_ID 
	 * @return Order Amount in U.T.
	 *  */
	private void approvePo (MOrder Order,int Role_ID, int Order_ID){
		
		String Message = "";
		String OrderType = Order.getXX_OrderType();
		Date utilDate = new Date(); 
		BigDecimal AmountUt = new BigDecimal(0);
		BigDecimal OrderAmount = Order.getGrandTotal();
		//int Finance_ID = Env.getCtx().getContextAsInt("#XX_L_ROLEFINANCIALMANAGER_ID");
		int Import_ID = Env.getCtx().getContextAsInt("#XX_L_ROLEIMPORTMANAGER_ID");
		long lnMilisegundos = utilDate.getTime();
		Timestamp TodayTimestamp = new Timestamp(lnMilisegundos);
		X_XX_VMR_PO_ApprovalRole approbalRole = get_Limit(Role_ID);

		if(approbalRole != null){
			// Se obtiene el monto de la orden en base a las Unidades Tributarias
			AmountUt = get_UT(OrderAmount);
			
			Vector<Integer> condicionPago = new Vector<Integer>(6);
			Utilities util = new Utilities();
			condicionPago = util.infoCondicionPago(Order.getC_PaymentTerm_ID());
			
			if(condicionPago.size()==0){
				ADialog.error(1, new Container(), "Error en la condicion de pago");
				return;
			}
			
			// Solicitud de aprobación si se excede del monto límite y necesita aprobación de un superior
			if((AmountUt.compareTo(approbalRole.getXX_UT()) == 1)){ 
				ADialog.info(1, new Container(),Msg.translate(Env.getCtx(), "XX_ApproveLimits"));
				
				// Notificación vía e-mail al superior del usuario
				Message = Msg.getMsg( Env.getCtx(), "XX_OrderLimitBYS", 
						new String[]{Order.getDocumentNo()});
				
				sendMail(Message, approbalRole.getXX_NotifyingRole_ID(), Order_ID, "Sup");
			}// if OrderAmount
			// Solicitud no excede del monto limite
			else {
				// Internacional: Estatus es EP y no puede ser modificado
				if (OrderType.equals("Importada")){ 
					Order.setXX_OrderStatus("EP");
					Order.setXX_ApprovalDate(TodayTimestamp);
					
					Message = Msg.getMsg( Env.getCtx(), "XX_ApprovePOImpBYS", 
							new String[]{Order.getDocumentNo(), "Producción" });
						
					// Notificación vía e-mail a Planificación Financiera
					//sendMail(Message, Finance_ID, Order_ID, "Fin"); SOLICITADO POR OCAVALIERI (NO RECIBIR CORREO)
					// Notificación vía e-mail a Importación
					sendMail(Message, Import_ID, Order_ID, "Imp");
				}//fin			 
				else {
					// Nacional: Asignar estatus AP
					Order.setXX_OrderStatus("AP");
					Order.setXX_ApprovalDate(TodayTimestamp);
					Message = Msg.getMsg( Env.getCtx(), "XX_ApprovePOBYS", 
							new String[]{Order.getDocumentNo(), "Aprobada"});
				
					//sendMail(Message, Finance_ID, Order_ID, "Fin"); SOLICITADO POR OCAVALIERI (NO RECIBIR CORREO)
					
				}//else
				
				if (Order.getXX_PurchaseType().equals("SE"))
					Order.setXX_OrderStatus("CH");
				
				if(Order.save())
					commit();
				else{
					rollback();
					ADialog.error(1, new Container(), "XX_InvalidPurchaseOrder");
					return;
				}
				
				// Función 105: Manejo de estado de la OC en Inválido
				if (Order.getDocStatus().equals("IN")) {
				   	rollback();
				   	ADialog.error(1, new Container(), "XX_InvalidPurchaseOrder");
				   	return;
				}
				else{
					/****Jessica Mendoza****/
					/****Proceso para crear la OC en la tabla de Estimacion de Cuentas por Pagar, una vez aprobada****/
					boolean flag = false;
					int diasBeneficio = 0;
					int diasTotalesI = 0;
					int diasTotalesII = 0;
					int diasTotalesIII = 0;
					Calendar calendarEstimadaI = Calendar.getInstance();
					Calendar calendarEstimadaII = Calendar.getInstance();
					Calendar calendarEstimadaIII = Calendar.getInstance();
					Timestamp fechaEstimada = new Timestamp(0);
					Timestamp fechaEstimadaS = new Timestamp(0);
					Timestamp fechaEstimadaT = new Timestamp(0);
					Vector<Timestamp> vector = new Vector<Timestamp>(3);
					MBPartner vendor = new MBPartner(Env.getCtx(), Order.getC_BPartner_ID(), null);	    	

					String sqlEst = "select * " +
					"from XX_VCN_EstimatedAPayable where C_Order_ID = " + Order.getC_Order_ID();
					PreparedStatement pstmtEst = null;
					ResultSet rsEst = null;
					try{
						pstmtEst = DB.prepareStatement(sqlEst, null);
						rsEst = pstmtEst.executeQuery();

						if (!rsEst.next()){
							BigDecimal subTotal1 = new BigDecimal(0);
							BigDecimal subTotal2 = new BigDecimal(0);
							diasBeneficio = util.benefitVendorDayAdvance(Order.getC_BPartner_ID());								

							if (Order.getXX_OrderType().equals("Nacional")){
								diasTotalesI = condicionPago.get(1) - diasBeneficio;
								diasTotalesII = condicionPago.get(4) - diasBeneficio;
								diasTotalesIII = condicionPago.get(7) - diasBeneficio;
							}else{
								diasTotalesI = condicionPago.get(1);
								diasTotalesII = condicionPago.get(4);
								diasTotalesIII = condicionPago.get(7);
							}

							vector = util.calcularFecha(Order.getC_PaymentTerm_ID(),Order.getC_Order_ID(),"estimada");						
							fechaEstimada = vector.get(0);
							calendarEstimadaI.setTimeInMillis(fechaEstimada.getTime());
							calendarEstimadaI.add(Calendar.DATE, diasTotalesI);
							fechaEstimada = new Timestamp(calendarEstimadaI.getTimeInMillis());
							if (vector.get(1) != null){
								fechaEstimadaS = vector.get(1);
								calendarEstimadaII.setTimeInMillis(fechaEstimadaS.getTime());
								calendarEstimadaII.add(Calendar.DATE, diasTotalesII);
								fechaEstimadaS = new Timestamp(calendarEstimadaII.getTimeInMillis());
							}
							if (vector.get(2) != null){
								fechaEstimadaT = vector.get(3);
								calendarEstimadaIII.setTimeInMillis(fechaEstimadaT.getTime());
								calendarEstimadaIII.add(Calendar.DATE, diasTotalesIII);
								fechaEstimadaT = new Timestamp(calendarEstimadaIII.getTimeInMillis());
							}

							Vector<Integer> vecI = new Vector<Integer>(9);
							Vector<String> vecS = new Vector<String>(2);
							vecI.add(Order.getC_Order_ID());
							vecI.add(Order.getC_BPartner_ID());
							vecI.add(Order.getC_PaymentTerm_ID());
							vecI.add(Order.getC_Currency_ID());
							if (vendor.getXX_VendorType_ID() != null)
								vecI.add(vendor.getXX_VendorType_ID());
							else
								vecI.add(0);
							vecS.add(Order.getXX_POType());
							vecI.add(Order.getXX_VMR_DEPARTMENT_ID());
							vecS.add(Order.getXX_OrderType());
							vecI.add(Order.getC_Country_ID());
							vecI.add(Order.getXX_Category_ID()); 
							vecI.add(Order.getXX_ImportingCompany_ID());

							String tipoCompra = Order.getXX_PurchaseType();
							if (tipoCompra.equals("SU"))
								tipoCompra = "Suministros y Materiales";
							else if (tipoCompra.equals("SE"))
								tipoCompra = "Servicios";
							else 
								tipoCompra = "Activos Fijos";

							if (condicionPago.get(0) == 100){ 
								util.crearEstimacion(vecI, vecS, Order.getGrandTotal(),fechaEstimada,"Orden",tipoCompra, 0);
								flag = true;
							}else{
								BigDecimal total = new BigDecimal(0);
								if (Order.getXX_POType().equals("POM")){
									total = Order.getTotalLines();
								}else{
									total = Order.getGrandTotal();
								}
								if (condicionPago.get(6) != 0){
									subTotal1 = Order.getTotalLines().multiply(new BigDecimal(condicionPago.get(0))).divide(new BigDecimal(100));
									subTotal2 = Order.getTotalLines().multiply(new BigDecimal(condicionPago.get(3))).divide(new BigDecimal(100));
									util.crearEstimacion(vecI, vecS, subTotal1,fechaEstimada,"Orden",tipoCompra, 0);
									util.crearEstimacion(vecI, vecS, subTotal2,fechaEstimadaS,"Orden",tipoCompra, 0);
									util.crearEstimacion(vecI, vecS, total.subtract(subTotal1.add(subTotal2)),fechaEstimadaT,"Orden",tipoCompra, 0);
									flag = true;
								}else{
									subTotal1 = Order.getTotalLines().multiply(new BigDecimal(condicionPago.get(0))).divide(new BigDecimal(100));
									util.crearEstimacion(vecI, vecS, subTotal1,fechaEstimada,"Orden",tipoCompra, 0);
									util.crearEstimacion(vecI, vecS, total.subtract(subTotal1),fechaEstimadaS,"Orden",tipoCompra, 0);
									flag = true;
								}
							}
						}
					}catch (SQLException e){
						log.log(Level.SEVERE, sqlEst, e);
					}finally{
						DB.closeResultSet(rsEst);
						DB.closeStatement(pstmtEst);
					}
					/****Fin codigo - Jessica Mendoza***/
					
					if (flag){
						Order.setXX_Authorized(true);
						Order.setXX_EntranceDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
						Order.setDocAction(X_C_Order.DOCACTION_Complete);
						DocumentEngine.processIt (Order, X_C_Order.DOCACTION_Complete); 
						Order.save();
						commit();
						
						//Envia correo al proveedor
						if (!Order.getDocStatus().equals("IN")){
							SendMailThread myThread = new SendMailThread();
							myThread.orden = Order;
							myThread.start();
						}
						
					}else{
						ADialog.info(0, new Container(), "Error al generar cuenta por pagar estimada");
						rollback();
						commit();
						Order.setXX_OrderStatus("PRO");
						Order.save();
					}
				} // PS status != IN

			}//else OrderAmount
		}
		else {
			ADialog.info(1, new Container(),Msg.translate(Env.getCtx(), "XX_RoleApprovePO"));
		}
	}// Fin approvePo
	
	/** sendMail
	 * @param message E-mail's text
	 * @param user_ID Recipient User
	 * @param Order_ID Order Id
	 * @param Notification
	 *  */
	private void sendMail(String message, int user_ID, int Order_ID, String Notification){
		message += "\nNota: No responda este mensaje";
		int MailTemplate = 0;
		
		// Se define la plantilla de correo que corresponda dependiendo del usuario
		if (Notification.equals("Imp")){
			MailTemplate = Env.getCtx().getContextAsInt("#XX_L_MT_NOTIFIMPORT_ID");
		}
		else if (Notification.equals("Sup")){
			MailTemplate = Env.getCtx().getContextAsInt("#XX_L_MT_NOTIFSUPERIOR_ID");
		}
		else if(Notification.equals("Fin")){
			MailTemplate = Env.getCtx().getContextAsInt("#XX_L_MT_NOTIFFINANCE_ID");
		}
		
		// Se definen los usuarios que recibirán el correo según su rol
		String sql2 = " SELECT A.AD_User_ID USER_ID "+
					" FROM AD_USER A JOIN AD_USER_ROLES U ON (A.AD_USER_ID = U.AD_USER_ID) "+
					" WHERE U.AD_ROLE_ID = " + user_ID +
					" AND U.ISACTIVE = 'Y'";
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		try {
			pstmt2 = DB.prepareStatement(sql2, null);
			rs2 = pstmt2.executeQuery();
			while(rs2.next()){
				Utilities f = new Utilities(getCtx(), null, MailTemplate, message, -1, 
						getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, rs2.getInt("USER_ID"), null);
				
				try { f.ejecutarMail(); } 
				catch (Exception e) { e.printStackTrace(); }
				f = null;
			}
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(Env.getCtx(), e.getMessage()));
		}
		finally {
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
		}
		
	}// Fin sendMailAP
	

	/*
	 * Hilo email
	 */
	public class SendMailThread extends Thread{
		
		MOrder orden = null;
		
		public void run(){
			try {
				sendEmailToVendor(orden);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Este metodo crea la O/C en PDF y la envia al
		 * proveedor
		 */
		public String sendEmailToVendor(MOrder ordenCompra){
			
			MPrintFormat f = null;
			int C_Order_ID = ordenCompra.getC_Order_ID();
			log.info("C_Order_ID" + C_Order_ID);
			if (C_Order_ID < 1)
				throw new IllegalArgumentException("@NotFound@ @C_Order_ID@");
			
			// Obtain the Active Record of M_Order Table
			Query q = new Query("C_Order_ID");
			q.addRestriction("C_Order_ID", Query.EQUAL, Integer.valueOf(C_Order_ID));
			int table_ID = X_C_Order.Table_ID;
			
			// Create the Process Info Instance to generate the report
			PrintInfo i = new PrintInfo("Orden de Compra", table_ID, C_Order_ID, 0);
			 if (ordenCompra.getXX_OrderType().equals(X_Ref_XX_OrderType.IMPORTADA.getValue()))
				 f = MPrintFormat.get (Env.getCtx(), 1008649, false);
			 else
				 f = MPrintFormat.get (Env.getCtx(), 1008648, false);
			
			// Create the report
			ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i); 
			//new Viewer(re);
			
			// Generate the PDF file
			File attachment = null;		
			try
			{
				attachment = File.createTempFile("Purchase Order", ".pdf");
				re.getPDF(attachment);
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "", e);
			}

			MBPartner vendorAux = new MBPartner(getCtx(), ordenCompra.getC_BPartner_ID(), null);
			String toVendor = vendorAux.getXX_VendorEmail();	
			MClient m_client = MClient.get(Env.getCtx());
			
			EMail email = m_client.createEMail(null, toVendor, vendorAux.getName(),
					Msg.getMsg(Env.getCtx(), "XX_SentPOSubject", 
							new String[] { ordenCompra.getDocumentNo() }), 
					Msg.getMsg(Env.getCtx(), "XX_SentPurchaseOrder", 
							new String[] { ordenCompra.getDocumentNo(), vendorAux.getName()}));
			
			if (email != null){
				
				//email.addTo("cuentasporpagar@beco.com.ve", "Cuentas Por Pagar");
				
				//Se envia correo a representantes comerciales, ventas y administrativos
				 MUser userRepre = null;
				String SQL = "SELECT AD_USER_ID " +
							 "FROM AD_USER " +
							 "WHERE C_BPartner_ID = "+ordenCompra.getC_BPartner_ID() +" ";
				
				//Para O/C Importada se envia correo a representantes comerciales
				if (ordenCompra.getXX_OrderType().equals(X_Ref_XX_OrderType.IMPORTADA.getValue())){
					SQL +="AND XX_ContactType IN ("+X_Ref_XX_Ref_ContactType.COMERCIAL.getValue()+","+
					 	X_Ref_XX_Ref_ContactType.SALES.getValue()+","+X_Ref_XX_Ref_ContactType.ADMINISTRATIVE.getValue()+")";
				//Para O/C Importada NO se envia correo a representantes comerciales
				}else if(ordenCompra.getXX_OrderType().equals(X_Ref_XX_OrderType.NACIONAL.getValue())){
					SQL +="AND XX_ContactType IN ("+X_Ref_XX_Ref_ContactType.SALES.getValue()+
						","+X_Ref_XX_Ref_ContactType.ADMINISTRATIVE.getValue()+")";
				}
				
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try{
					pstmt = DB.prepareStatement(SQL, null); 
					rs = pstmt.executeQuery();			
						    
					while (rs.next()){				
						userRepre = new MUser(Env.getCtx(), rs.getInt("AD_USER_ID"), null);
						if(!userRepre.getEMail().isEmpty())
							email.addTo(userRepre.getEMail(), userRepre.getName());
						System.out.println(userRepre.getEMail()+","+userRepre.getName()+","+userRepre.getXX_ContactType());
						}
					}
				catch (SQLException e){
					e.printStackTrace();
				} finally{			
					try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
					try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
				}
				
				//	Attachment
				if (attachment != null && attachment.exists()){
					email.addAttachment(attachment);
				}
				else
				{  
					ADialog.info(1, new Container(), "No se pudo adjuntar la orden de compra en el correo al proveedor. Por favor envíela por correo manualmente");
				}
					
				
				String status = email.send();
				log.info("Email Send status: " + status);
			
				if (email.isSentOK())
				{
					return "Mail Sent sucessfully";
				}
				else{
					ADialog.info(1, new Container(), "No se pudo enviar el correo al proveedor. Por favor envíelo manualmente");
					return "Mail cannot be Sent";
				}
			}
			else
				return "Cannot create mail";
		}
	}

}// Fin XX_POApproval
