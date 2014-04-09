package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import compiere.model.cds.MVMRDistributionHeader;
import compiere.model.cds.X_Ref_XX_DistributionStatus;
import compiere.model.cds.X_XX_VLO_NewsReport;
import compiere.model.cds.X_XX_VMR_Collection;
import compiere.model.cds.X_XX_VMR_Package;


/*
 * 		Proceso que envía notificaciones al iniciar una Tarea Crítica correspondiente a 
 * la creación de un reporte de novedad.
 * 
 * @author Gabriela Marques.
 */

public class XX_VMR_CTaskNewsReport extends SvrProcess {
	
	int newsreport;
	
	@Override
	protected String doIt() throws Exception {

			newsreport = getRecord_ID();
			X_XX_VLO_NewsReport newsReport = new X_XX_VLO_NewsReport( Env.getCtx(), newsreport, null);
			MOrder order = new MOrder(Env.getCtx(),newsReport.getC_Order_ID(), null);
//			X_XX_VMR_Package paquete = new X_XX_VMR_Package(getCtx(), order.getXX_VMR_Package_ID(), null);
//			X_XX_VMR_Collection collection = new X_XX_VMR_Collection(getCtx(), paquete.getXX_VMR_Collection_ID(), null);
			MBPartner vendor = new MBPartner(getCtx(), order.getC_BPartner_ID(), null);
			MBPartner buyer = new MBPartner(getCtx(), order.getXX_UserBuyer_ID(), null);
			MVMRDepartment dep = new MVMRDepartment(Env.getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);
			
			String mensaje = Msg.getMsg( getCtx(), "XX_CompleteNewsReport", 
								new String[]{newsReport.getDocumentNo(), order.getDocumentNo(), buyer.getName(), vendor.getName(),
								dep.getValue() + " " + dep.getName()});
			
			enviarNotificacion(/*buyer.getXX_VendorEmail()*/buyer.getName(), mensaje);
					
		
		return "ok";
		
	}

	@Override
	protected void prepare() {
		
		
	}
	
	
	protected void enviarNotificacion(String destinatario, String msg) {
		String subject = "Notificación de Tarea Crítica: Reporte de Novedad pendiente por completar."; 
		//prueba
		msg += "\n\n\n" + destinatario;
		destinatario = "gabymarquesp@gmail.com";
		//
		if(destinatario.contains("@")) {
			MClient m_client = MClient.get(Env.getCtx());
			EMail email = m_client.createEMail(null, destinatario, " ", subject, msg);
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