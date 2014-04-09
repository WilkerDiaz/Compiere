package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MBPartner;
import compiere.model.cds.MOrder;
import compiere.model.cds.MVMRDepartment;
import compiere.model.cds.X_XX_VMR_Collection;
import compiere.model.cds.X_XX_VMR_Package;


/*
 * 		Proceso que envía notificaciones al iniciar una Tarea Crítica correspondiente a una
 * Orden de Compra pendiente por aprobar.
 * 
 * @author Gabriela Marques.
 */


public class XX_VMR_CTaskPOApproval extends SvrProcess {
	int order_no;
	
	@Override
	protected String doIt() throws Exception {
		
		MOrder orden= new MOrder(getCtx(),getRecord_ID(),get_TrxName());
		X_XX_VMR_Package paquete = new X_XX_VMR_Package(getCtx(), orden.getXX_VMR_Package_ID(), null);
		X_XX_VMR_Collection collection = 
			new X_XX_VMR_Collection(getCtx(), paquete.getXX_VMR_Collection_ID(), null);
		MBPartner vendor = new MBPartner(getCtx(), orden.getC_BPartner_ID(), null);
		MVMRDepartment dep = new MVMRDepartment( getCtx(), orden.getXX_VMR_DEPARTMENT_ID(), null);
		String mensaje = 
			Msg.getMsg( getCtx(), "XX_OrderLimit", new String[]
                  {orden.getDocumentNo(),vendor.getName(),dep.getValue()+" "+dep.getName(), collection.getName()});
		
		// Buscar el responsable de completar la tarea
		String qResponsible = "SELECT ad_role_id FROM AD_WF_PROCESS join ad_wf_responsible using (ad_wf_responsible_id) " +
				" where record_id ='" + getRecord_ID() + "' AND WFSTATE = 'ON'";
		
//		String qResponsible = "SELECT ad_role_id FROM (SELECT ad_role_id FROM XX_VMR_PO_ApprovalRole " +
//				" WHERE (SELECT max(XX_PercentageExcess) " +
//					" FROM xx_vmr_po_approval p2" +
//					" WHERE p2.c_order_id = " + getRecord_ID() + ") between XX_RankLow and XX_RankHigh " +
//				" ORDER by XX_RankHigh asc)" +
//			" WHERE rownum = 1 ";
//		System.out.println("Responsable :" +qResponsible);
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int resp = 0;
		try {
			ps = DB.prepareStatement(qResponsible, null);			
			rs = ps.executeQuery();
			if (rs.next()) {
				resp = rs.getInt("ad_role_id");
			}
		} catch (SQLException e) {			
			log.log(Level.SEVERE, qResponsible, e);
			e.printStackTrace();
		}  finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		
		String qCorreo = "";
		
		// Caso Jefe de Categoría
		if (resp == Env.getCtx().getContextAsInt("#XX_L_ROLECATEGORYMANAGER_ID")) {
			// Tomar solo categoria = categoria de la orden
			qCorreo = "select email, a.name name" +
					"\n        from AD_User_Roles join ad_user a on (a.ad_user_id = ad_user_roles.ad_user_id)" +
					"\n        join c_bpartner b on (b.c_bpartner_id = a.c_bpartner_id)" +
					"\n        where ad_role_id = '" + resp + "' " +
					"\n        and ( SELECT nvl(XX_VMR_CATEGORY_ID,-1) categoria " +
									" from XX_VMR_CATEGORY where XX_CATEGORYMANAGER_ID = a.c_bpartner_id) = '" +
									orden.getXX_Category_ID() + "' " +
					"\n		 and a.isactive = 'Y'";
		}
		
		// Si no se ponen dos pendientes para OC
		// Caso Jefe de Planificación
//		if (resp == Env.getCtx().getContextAsInt("#XX_L_ROLECATEGORYMANAGER_ID")) {
//			qCorreo = "select email, a.name name" +
//					"\n	from AD_User_Roles join ad_user a on (a.ad_user_id = ad_user_roles.ad_user_id)" +
//					"\n	where ad_role_id = '" + resp + "' " +
//						"\n	and a.isactive = 'Y'";
//			System.out.println("Correo :" +qCorreo);
//		}
//		// Caso dos Gerentes
//		// Buscar dos gerentes 
//		if (resp == Env.getCtx().getContextAsInt("#XX_L_ROLELOGISTIC_ID") 
//				| resp == Env.getCtx().getContextAsInt("#XX_L_ROLEMERCHANDISING_ID") ) {
//			// Tomar solo categoria = categoria de la orden
//			qCorreo = "select email, a.name name" +
//					"\n        from AD_User_Roles join ad_user a on (a.ad_user_id = ad_user_roles.ad_user_id)" +
//					"\n        join c_bpartner b on (b.c_bpartner_id = a.c_bpartner_id)" +
//					"\n        where ad_role_id = '" + resp + "' " +
//					"\n        and ( SELECT nvl(XX_VMR_CATEGORY_ID,-1) categoria " +
//									" from XX_VMR_CATEGORY where XX_CATEGORYMANAGER_ID = a.c_bpartner_id) = '" +
//									orden.getXX_Category_ID() + "' " +
//					"\n		 and a.isactive = 'Y'";
//			System.out.println("Correo :" + qCorreo);
//		}
		
		// Caso Jefe de Planificación, o dos gerentes (uno por cada actividad)
//		if (resp == Env.getCtx().getContextAsInt("#XX_L_ROLECATEGORYMANAGER_ID")
//				| resp == Env.getCtx().getContextAsInt("#XX_L_ROLELOGISTIC_ID") 
//				| resp == Env.getCtx().getContextAsInt("#XX_L_ROLEMERCHANDISING_ID")) {
//			qCorreo = "select email, a.name name" +
//					"\n	from AD_User_Roles join ad_user a on (a.ad_user_id = ad_user_roles.ad_user_id)" +
//					"\n	where ad_role_id = '" + resp + "' " +
//						"\n	and a.isactive = 'Y' and ad_user_roles.isactive = 'Y' ";
//			System.out.println("Correo :" +qCorreo);
//		}
		
		// Caso Jefe de Planificación
		if (resp == Env.getCtx().getContextAsInt("#XX_L_ROLESCHEDULEMANAGER_ID")) {
			qCorreo = "select email, a.name name" +
					"\n	from AD_User_Roles join ad_user a on (a.ad_user_id = ad_user_roles.ad_user_id)" +
					"\n	where ad_role_id = '" + resp + "' " +
						"\n	and a.isactive = 'Y' and ad_user_roles.isactive = 'Y' ";
		}
		
		// Caso dos gerentes
		if (resp == Env.getCtx().getContextAsInt("#XX_L_ROLELOGISTIC_ID") 
				| resp == Env.getCtx().getContextAsInt("#XX_L_ROLEMERCHANDISING_ID")) {
			qCorreo = "select email, a.name name" +
					"\n	from AD_User_Roles join ad_user a on (a.ad_user_id = ad_user_roles.ad_user_id)" +
					"\n	where (ad_role_id = '" +  Env.getCtx().getContextAsInt("#XX_L_ROLELOGISTIC_ID")  + "' " +
						"\n or ad_role_id = "+ Env.getCtx().getContextAsInt("#XX_L_ROLEMERCHANDISING_ID") +" )" +
						"\n	and a.isactive = 'Y' and ad_user_roles.isactive = 'Y' ";
		}
		
		ArrayList<String> correos = new ArrayList<String>(); // En caso de haber varios usuarios con ese cargo
		ps = null;
		rs = null;
		try {
			ps = DB.prepareStatement(qCorreo, null);			
			rs = ps.executeQuery();
			while (rs.next()) {
//				correos.add(rs.getString("name"));
				correos.add(rs.getString("email"));
//				System.out.println("envio a : " + rs.getString("name"));
			}
		} catch (SQLException e) {			
			log.log(Level.SEVERE, qResponsible, e);
			e.printStackTrace();
		}  finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		
		
		
		
		enviarNotificacion(correos, mensaje);
		
		return "ok";
		
	}

	@Override
	protected void prepare() {
		
		
	}
	
	//Enviar notificacion por correo
	protected void enviarNotificacion(ArrayList<String> destinatarios, String msg) {
		String subject = "Notificación de Tarea Crítica: Orden de Compra en espera de aprobación."; 
		
		for (String correo :  destinatarios) {
			String emailTo = correo; 
			if(emailTo.contains("@")) {
				MClient m_client = MClient.get(Env.getCtx());
				EMail email = m_client.createEMail(null, emailTo, " ", subject, msg);
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
		
	}

	
}