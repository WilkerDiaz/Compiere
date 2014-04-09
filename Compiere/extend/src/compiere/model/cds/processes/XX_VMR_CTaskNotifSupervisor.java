package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.process.StateEngine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.wf.MWFActivity;

import compiere.model.cds.MBPartner;
import compiere.model.cds.MOrder;
import compiere.model.cds.MVMRDepartment;
import compiere.model.cds.MVMRDistributionHeader;
import compiere.model.cds.X_Ref_XX_DistributionStatus;
import compiere.model.cds.X_XX_VLO_NewsReport;
import compiere.model.cds.X_XX_VMR_Collection;
import compiere.model.cds.X_XX_VMR_Package;


/*
 * 		Proceso que envía notificaciones de tarea crítica no completada al supervisor
 * del responsable correspondiente, cuando se ha vencido el plazo estipulado para completarla.
 * 
 * @author Gabriela Marques.
 */

public class XX_VMR_CTaskNotifSupervisor extends SvrProcess {
//	int wf_process = 0;
	
	@Override
	protected String doIt() throws Exception {
//		String qPendientes = "SELECT XX_ASSOCIATEMANAGER_ID, XX_AssociateSupervisor_ID, ad_workflow_id, ad_wf_process_id, record_id" +
//				" FROM AD_WF_PROCESS join XX_VMR_CriticalTasks using (ad_workflow_id)" +
//				" WHERE WFSTATE in ('OS', 'OR') " +
//						" and xx_supervisoralert <> (select to_number(to_char(sysdate, 'DD')) from dual) " +
//						" and floor(((select sysdate from dual) - dateactualstart)*24) >= XX_TasksAlert " +
//						" and XX_VMR_CriticalTasks.isactive = 'Y' ";
		
		String qPendientes = "SELECT XX_ASSOCIATEMANAGER_ID, XX_AssociateSupervisor_ID, p.ad_workflow_id," +
				" p.ad_wf_process_id, p.record_id," +
				"          ad_role_id responsible" +
				"				 FROM AD_WF_PROCESS p join ad_wf_activity a on (p.ad_wf_process_id = a.ad_wf_process_id)" +
				"				 left join AD_WF_RESPONSIBLE r on  (a.AD_WF_RESPONSIBLE_id = r.AD_WF_RESPONSIBLE_id)" +
				"				 join XX_VMR_CriticalTasks c on (c.ad_workflow_id= p.ad_workflow_id)" +
				"				 WHERE p.WFSTATE in ('OS', 'OR') and a.WFSTATE in ('OS', 'OR')" +
				"			and xx_supervisoralert <> (select to_number(to_char(sysdate, 'DD')) from dual)" +
				"				and floor(((select sysdate from dual) - dateactualstart)*24) >= XX_TasksAlert" +
				"						and c.isactive = 'Y'" +
				"						and (ad_role_id is null OR ad_role_id= xx_associatemanager_id)";
		
//		System.out.println(Env.getCtx().getContextAsInt("#XX_WF_OCAPPROVAL"));

//		System.out.println(qPendientes);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(qPendientes, null);			
			rs = ps.executeQuery();
			while (rs.next()) {
//				System.out.println("*****************************\n" +rs.getInt("ad_workflow_id"));
				int process = rs.getInt("ad_workflow_id");
				int wf_process = rs.getInt("ad_wf_process_id");
				
				// Para cada Flujo de Trabajo, primero se verifica que en efecto no se haya realizado la acción y
				// se procede a enviar notificaciones a los respectivos responsables. En caso contrario, 
				// se coloca el flujo de trabajo como Completado y no se notifica.
				
				// Fijar precios en la distribución
				if (process == Env.getCtx().getContextAsInt("#XX_WF_DEFINITIVEPRICES") ) { 
//					System.out.println("Distribución "+rs.getInt("record_ID"));
					MVMRDistributionHeader distribucion = new MVMRDistributionHeader(getCtx(), rs.getInt("record_ID"), get_TrxName());
					// Verificar si realmente esta distribución se encuentra pendiente
					if (!distribucion.getXX_DistributionStatus().equals(X_Ref_XX_DistributionStatus.PENDIENTE_POR_FIJAR_PRECIOS_DEFINITIVOS.getValue())) { // Ya fueron fijados los precios
						completarWorkflow(wf_process);
						// Listo, no enviar notificación
//						System.out.println("Completar");
					} else {
						notifFijarPrecios(rs.getInt("XX_AssociateSupervisor_ID"), distribucion, wf_process);
					}
				} 
				
				
				// Completar reporte de novedad
				else if (process == Env.getCtx().getContextAsInt("#XX_WF_COMPLETENREPORT") ) { 
//					System.out.println("Reporte de Novedad "+rs.getInt("record_ID"));
					X_XX_VLO_NewsReport newsReport = 
						new X_XX_VLO_NewsReport( Env.getCtx(), rs.getInt("record_ID"), null);
					// Verificar si aún no ha sido completado ni anulado el reporte
					
					String query = " select * from xx_vlo_newsreport where xx_vlo_newsreport_id = " + rs.getInt("record_ID") +
					" and (xx_status = 'AN' OR XX_NewOrder_id is not null) ";
					PreparedStatement psReport = null;
					ResultSet rsReport = null;
					try {
						psReport = DB.prepareStatement(query, null);			
						rsReport = psReport.executeQuery();
						if (rsReport.next()) {
							completarWorkflow(wf_process);
//							 Listo, no enviar notificación
//							System.out.println("Completar");
						} else {
							// No se ha completado ni anulado
							// Notificar al supervisor
							notifReporteNovedad(rs.getInt("XX_AssociateSupervisor_ID"), newsReport, wf_process);
						}
					} catch (SQLException e) {			
						log.log(Level.SEVERE, query, e);
						e.printStackTrace();
					}  finally {
						DB.closeResultSet(rsReport);
						DB.closeStatement(psReport);
					}
							
				} 
				
				// Flujo aprobación OC
				else if (process == Env.getCtx().getContextAsInt("#XX_WF_OCAPPROVAL") )  { 
//					System.out.println("OC "+rs.getInt("record_ID"));
					MOrder orden= new MOrder(getCtx(), rs.getInt("record_ID"), get_TrxName());
					if (!(orden.getXX_OrderStatus().equals("PRO") && orden.isXX_OrderReadyStatus() ) ) { 
						completarWorkflow(wf_process);
						// Listo, no enviar notificación
//						System.out.println("Completar");
					} else {
						if (rs.getInt("XX_AssociateManager_ID") == Env.getCtx().getContextAsInt("#XX_L_ROLELOGISTIC_ID")
								|| rs.getInt("XX_AssociateManager_ID") == Env.getCtx().getContextAsInt("#XX_L_ROLEMERCHANDISING_ID") ) {
							notifOCDosGerentes(rs.getInt("record_ID"), orden, wf_process);
						} else {
						notifPendAprobarOC(rs.getInt("XX_AssociateManager_ID"), 
								rs.getInt("XX_AssociateSupervisor_ID"), orden, rs.getInt("record_ID"), wf_process);
						}
					}
				}
				
				//else if (process == Env.getCtx().getContextAsInt("#XX_WF_POAPPROVAL") ) { // Dos gerentes
//					System.out.println("OC - (2G) "+rs.getInt("record_ID"));
//					MOrder orden= new MOrder(getCtx(), rs.getInt("record_ID"), get_TrxName());
//					if (/*(orden.getXX_OrderStatus().equals("AP")  || orden.getXX_OrderStatus().equals("EP"))  // aprobada
//							|| !orden.isXX_OrderReadyStatus()  //  anulada
//							*/
//							!(orden.getXX_OrderStatus().equals("PRO") && orden.isXX_OrderReadyStatus() )
//						 ) { 
//						completarWorkflow(wf_process);
//						// Listo, no enviar notificación
//					} else {
////						notifOCDosGerentes(rs.getInt("record_ID"), orden, wf_process);
//					}
//				} else if (process == Env.getCtx().getContextAsInt("#XX_WF_POAPPROVAL_PLANNING") 
//						|| process ==  Env.getCtx().getContextAsInt("#XX_WF_POAPPROVAL_CATEGORY") ) { 
//					System.out.println("OC "+rs.getInt("record_ID"));
//					//Planificación o Jefe de Categoría
//					MOrder orden= new MOrder(getCtx(), rs.getInt("record_ID"), get_TrxName());
//					if (/*(orden.getXX_OrderStatus().equals("AP")  || orden.getXX_OrderStatus().equals("EP"))  // aprobada
//							|| !orden.isXX_OrderReadyStatus()  //  anulada
//							*/
//							!(orden.getXX_OrderStatus().equals("PRO") && orden.isXX_OrderReadyStatus() )
//						 ) { 
//						completarWorkflow(wf_process);
//						// Listo, no enviar notificación
//					} else {
////						notifPendAprobarOC(rs.getInt("XX_AssociateManager_ID"), 
////								rs.getInt("XX_AssociateSupervisor_ID"), orden, rs.getInt("record_ID"), wf_process);
//					}
//					
//					
//					
//				}
			}
		} catch (SQLException e) {			
			log.log(Level.SEVERE, qPendientes, e);
			e.printStackTrace();
		}  finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		
		return "ok";
		
	}

	@Override
	protected void prepare() {
		
		
	}
	
	protected ArrayList<String> usuarioArea(int rolUsuario, int depto, int item) {
		//item 0 = nombre, 1 = email
		ArrayList<String> usuarios = new ArrayList<String>();
		String query = " SELECT ";
		if (item == 0)	{
			query += " name as usuario from c_bpartner ";
		} else {
			query += " email as usuario from ad_user ";
		}
		query += " where c_bpartner_id in (";
		if (rolUsuario == Env.getCtx().getContextAsInt("#XX_L_ROLEBUYER_ID")) { // Comprador
			query += " SELECT XX_USERBUYER_ID FROM XX_VMR_Department where XX_VMR_Department_ID=" + depto;
		} else if (rolUsuario == Env.getCtx().getContextAsInt("#XX_L_ROLESCHEDULER_ID")) { // Planificador
			query += " SELECT XX_INVENTORYSCHEDULE_ID FROM XX_VMR_Department where XX_VMR_Department_ID=" + depto;
		} else if (rolUsuario == Env.getCtx().getContextAsInt("#XX_L_ROLECATEGORYMANAGER_ID")) { // Jefe de Categoría
			query += " SELECT XX_CATEGORYMANAGER_ID FROM XX_VMR_Category join XX_VMR_Department using (xx_vmr_category_id) " +
					" WHERE XX_VMR_Department_ID=" + depto;
		} else if (rolUsuario == Env.getCtx().getContextAsInt("#XX_L_ROLESCHEDULEMANAGER_ID")) { // Jefe de Planificación
			query += " SELECT c_bpartner_id " +
				"\n	from AD_User_Roles join ad_user a on (a.ad_user_id = ad_user_roles.ad_user_id)" +
				"\n	where ad_role_id = '" +  rolUsuario  + "' and a.isactive = 'Y' and ad_user_roles.isactive = 'Y' ";
		} else {
			return usuarios; // No implementado
		}
		query += " ) ";

		PreparedStatement ps = null;
		ResultSet rs2 = null;
		try {
			ps = DB.prepareStatement(query, null);			
			rs2 = ps.executeQuery();
			while (rs2.next()) {
				usuarios.add(rs2.getString("usuario"));
			}
		} catch (SQLException e) {			
			log.log(Level.SEVERE, query, e);
			e.printStackTrace();
		}  finally {
			DB.closeResultSet(rs2);
			DB.closeStatement(ps);
		}
		
		return usuarios;
		
	}
	
	protected void completarWorkflow(int processId) {
		String qActiv = " select AD_WF_ACTIVITY_ID from ad_wf_activity" +
				" where ad_wf_process_id = " + processId + " and WFState = 'OS' ";
		
		PreparedStatement ps = null;
		ResultSet rs2 = null;
		try {
			ps = DB.prepareStatement(qActiv, null);			
			rs2 = ps.executeQuery();
			while (rs2.next()) {
				MWFActivity activ = new MWFActivity(Env.getCtx(), rs2.getInt("AD_WF_ACTIVITY_ID"), get_TrxName()) ;
				activ.setWFState(StateEngine.STATE_Running);
				activ.setAD_User_ID(getAD_User_ID());
				activ.setWFState(StateEngine.STATE_Terminated);
			}
		} catch (SQLException e) {			
			log.log(Level.SEVERE, qActiv, e);
			e.printStackTrace();
		}  finally {
			DB.closeResultSet(rs2);
			DB.closeStatement(ps);
		}
		
		System.out.println("Proceso Terminado");
			
	}
	
	
	
	protected void notifFijarPrecios(int rolSupervisor, MVMRDistributionHeader distribucion, int wf_process) {
		// Con OC
		if ((distribucion.getC_Order_ID()!=0)
			&&(distribucion.getXX_DistributionStatus().equals(X_Ref_XX_DistributionStatus.PENDIENTE_POR_FIJAR_PRECIOS_DEFINITIVOS.getValue())) 
			) {
			try {
				MOrder order = new MOrder(Env.getCtx(),distribucion.getC_Order_ID(), null);
				X_XX_VMR_Package paquete = new X_XX_VMR_Package(getCtx(), order.getXX_VMR_Package_ID(), null);
				X_XX_VMR_Collection collection = new X_XX_VMR_Collection(getCtx(), paquete.getXX_VMR_Collection_ID(), null);
				MBPartner vendor = new MBPartner(getCtx(), order.getC_BPartner_ID(), null);
				MBPartner buyer = new MBPartner(getCtx(), order.getXX_UserBuyer_ID(), null);
				MVMRDepartment dep = new MVMRDepartment(Env.getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);
				
				String mensaje = Msg.getMsg( getCtx(), "XX_AssignPriceExpired", 
							new String[]{"Distribución pendiente por fijar precios definitivos.",
									buyer.getName(),""+distribucion.get_ID(),
									order.getDocumentNo(), vendor.getName(), 
									dep.getValue() + " " + dep.getName(), collection.getName(), });

				ArrayList<String> usuarios = usuarioArea(rolSupervisor, order.getXX_VMR_DEPARTMENT_ID(), 1);
				enviarNotificacion(usuarios, mensaje, wf_process);
			}
			catch(Exception e)	{
				log.log(Level.SEVERE,e.getMessage());
			}
		}
		//Por redist //AP1
		if ((distribucion.getC_Order_ID()==0)
			&&(distribucion.getXX_DistributionStatus().equals("FP")) ) {
			try {
				String query = " SELECT xx_vmr_department_id " +
						" FROM XX_VMR_DISTRIBUTIONDETAIL join XX_VMR_DEPARTMENT using (XX_VMR_DEPARTMENT_ID)  " +
						" WHERE xx_vmr_distributionheader_id = " + distribucion.get_ID() ;
				PreparedStatement ps = null;
				ResultSet rs = null;
				int depto = 1;
				try {
					ps = DB.prepareStatement(query, null);			
					rs = ps.executeQuery();
					if (rs.next()) {
						depto = rs.getInt("xx_vmr_department_id");
					}
				} catch (SQLException e) {			
					log.log(Level.SEVERE, query, e);
					e.printStackTrace();
				}  finally {
					DB.closeResultSet(rs);
					DB.closeStatement(ps);
				}
				
				//Comprador
				String qBuyer = "SELECT name from c_bpartner where c_bpartner_id = (" +
					" SELECT XX_USERBUYER_ID FROM XX_VMR_Department where XX_VMR_Department_ID="+ depto+" )";
				String buyer = "";
				ps = null;
				rs = null;
				
				try {
					ps = DB.prepareStatement(qBuyer, null);			
					rs = ps.executeQuery();
					if (rs.next()) {
						buyer = rs.getString("name");
					}
				} catch (SQLException e) {			
					log.log(Level.SEVERE, query, e);
					e.printStackTrace();
				}  finally {
					DB.closeResultSet(rs);
					DB.closeStatement(ps);
				}
				String mensaje = Msg.getMsg( getCtx(), "XX_AssignPriceNotOrderExpired",
							new String[]{"Distribución pendiente por fijar precios definitivos.",
								buyer, distribucion.get_ID()+"", distribucion.getXX_Department_Name()});
				
				ArrayList<String> usuarios = usuarioArea(rolSupervisor, depto, 1);
				enviarNotificacion(usuarios, mensaje, wf_process);
			}
			catch(Exception e) 	{
				log.log(Level.SEVERE,e.getMessage());
			}
		
		}
	}
	
	protected void notifReporteNovedad(int rolSupervisor, X_XX_VLO_NewsReport reporte, int wf_process) {
		
		MOrder order = new MOrder(Env.getCtx(), reporte.getC_Order_ID(), null);
//		X_XX_VMR_Package paquete = new X_XX_VMR_Package(getCtx(), order.getXX_VMR_Package_ID(), null);
//		X_XX_VMR_Collection collection = new X_XX_VMR_Collection(getCtx(), paquete.getXX_VMR_Collection_ID(), null);
		MBPartner vendor = new MBPartner(getCtx(), order.getC_BPartner_ID(), null);
		MBPartner buyer = new MBPartner(getCtx(), order.getXX_UserBuyer_ID(), null);
		MVMRDepartment dep = new MVMRDepartment(Env.getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);
		
		String mensaje = Msg.getMsg( getCtx(), "XX_NewsReportExpired", 
					new String[]{ "Completar Reporte de Novedad.",
							buyer.getName(), // Responsable
							reporte.getDocumentNo(), order.getDocumentNo(),  vendor.getName(),
							dep.getValue() + " " + dep.getName()});
		
		ArrayList<String> usuarios = usuarioArea(rolSupervisor, order.getXX_VMR_DEPARTMENT_ID(), 1);
		enviarNotificacion(usuarios, mensaje, wf_process);
		
	}
	
	
	protected void notifPendAprobarOC(int rolManager, int rolSupervisor, MOrder orden, int orden_id, int wf_process) { // ORDEN DE COMPRA
		X_XX_VMR_Package paquete = new X_XX_VMR_Package(getCtx(), orden.getXX_VMR_Package_ID(), null);
		X_XX_VMR_Collection collection = 
			new X_XX_VMR_Collection(getCtx(), paquete.getXX_VMR_Collection_ID(), null);
		MBPartner vendor = new MBPartner(getCtx(), orden.getC_BPartner_ID(), null);
		
		//Correo del Comprador
		String qBuyer = "SELECT email from AD_USER where c_bpartner_id = " + orden.getXX_UserBuyer_ID();
		String buyer = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(qBuyer, null);			
			rs = ps.executeQuery();
			if (rs.next()) {
				buyer = rs.getString("email");
			}
		} catch (SQLException e) {			
			log.log(Level.SEVERE, qBuyer, e);
			e.printStackTrace();
		}  finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}

//		MBPartner buyer = new MBPartner(getCtx(), orden.getXX_UserBuyer_ID(), null);
		MVMRDepartment dep = new MVMRDepartment( getCtx(), orden.getXX_VMR_DEPARTMENT_ID(), null);
		
		//Este método trae TODOS los usuarios que tengan ese cargo, eliminado.
//		String responsable = usuarioArea(rolManager, dep.getXX_VMR_Department_ID(), 0).toString();
//		responsable = responsable.replace("[", "");
//		responsable = responsable.replace("]", "");
		
		String responsable = nombreCargo (rolManager);
		String mensaje = 
			Msg.getMsg( getCtx(), "XX_OrderLimitExpired", new String[]
                  {"Aprobación de Orden de Compra.", responsable,
					orden.getDocumentNo(),vendor.getName(),dep.getValue()+" "+dep.getName(), collection.getName()});
	mensaje +=rolSupervisor;
		ArrayList<String> usuarios = usuarioArea(rolSupervisor, dep.getXX_VMR_Department_ID(), 1);
		// Enviar también al comprador de la orden (SOLICITADO)
		usuarios.add(buyer);
		enviarNotificacion(usuarios, mensaje, wf_process);
	}

	
	protected void notifOCDosGerentes(int orden_id, MOrder orden, int wf_process) { // ORDEN DE COMPRA
		X_XX_VMR_Package paquete = new X_XX_VMR_Package(getCtx(), orden.getXX_VMR_Package_ID(), null);
		X_XX_VMR_Collection collection = 
			new X_XX_VMR_Collection(getCtx(), paquete.getXX_VMR_Collection_ID(), null);
		MBPartner vendor = new MBPartner(getCtx(), orden.getC_BPartner_ID(), null);
		MVMRDepartment dep = new MVMRDepartment( getCtx(), orden.getXX_VMR_DEPARTMENT_ID(), null);
//		MBPartner buyer = new MBPartner(getCtx(), orden.getXX_UserBuyer_ID(), null);
		String mensaje = 
			Msg.getMsg( getCtx(), "XX_OrderLimitExpired", new String[]
                  {"Aprobación de Orden de Compra.", "Gerente de Logística - Gerente de Merchandising. ",
					orden.getDocumentNo(),vendor.getName(),dep.getValue()+" "+dep.getName(), collection.getName()});

		ArrayList<String> usuarios = new ArrayList<String>();
		
		String qGerentes = "select email, a.name name" +
				"\n	from AD_User_Roles join ad_user a on (a.ad_user_id = ad_user_roles.ad_user_id)" +
				"\n	where (ad_role_id = '" +  Env.getCtx().getContextAsInt("#XX_L_ROLELOGISTIC_ID")  + "' " +
					"\n OR ad_role_id = "+ Env.getCtx().getContextAsInt("#XX_L_ROLEMERCHANDISING_ID") +
					// Enviar también al comprador de la orden (SOLICITADO)
					"\n OR a.c_bpartner_id = (select XX_USERBUYER_ID from c_order where c_order_id ="+orden_id+"))" +
					"\n	and a.isactive = 'Y' and ad_user_roles.isactive = 'Y' ";

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(qGerentes, null);			
			rs = ps.executeQuery();
			while (rs.next()) {
				usuarios.add(rs.getString("email"));
			}
		} catch (SQLException e) {			
			log.log(Level.SEVERE, qGerentes, e);
			e.printStackTrace();
		}  finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		
		enviarNotificacion(usuarios, mensaje, wf_process);
		
	}

	protected String nombreCargo (int id) {
		
		String sql = "select name from AD_Role where AD_Role_ID= "+id;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("name");
			}
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
			e.printStackTrace();
		}  finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}		
		return "";
	}

	protected void enviarNotificacion(ArrayList<String> destinatarios, String msg, int wf_process) {
		
		String subject = "Notificación de Tarea Crítica: Vencimiento de Plazo."; 
		
		for (String correo :  destinatarios) {
//			System.out.println("\n\n"+correo + " - " + subject + " - " + msg);
			if(correo.contains("@")) {
				MClient m_client = MClient.get(Env.getCtx());
				EMail email = m_client.createEMail(null, correo, " ", subject, msg);
				if (email != null){		
					try {
						String status = email.send();
						log.info("Email Send status: " + status);
						if (email.isSentOK()){}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		// Registrar el envío de notificación
		Calendar calendar = Calendar.getInstance();
		String queryUpdate = " UPDATE AD_WF_PROCESS SET xx_supervisoralert = '"+ calendar.get(Calendar.DAY_OF_MONTH)
					+"' WHERE AD_WF_PROCESS_ID = "+wf_process;

		try {
			DB.executeUpdate(null, queryUpdate);
		} catch (Exception e) {
			 e.printStackTrace();	
		} 
	}
	

}