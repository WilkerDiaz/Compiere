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
import compiere.model.cds.X_XX_VMR_Collection;
import compiere.model.cds.X_XX_VMR_Package;


/*
 * 		Proceso que envía notificaciones al iniciar una Tarea Crítica correspondiente a una
 * distribución pendiente por asignación de precios de venta.
 * 
 * @author Gabriela Marques.
 */

public class XX_VMR_CTaskPendingPrices extends SvrProcess {
	
	int distribucion;
	
	@Override
	protected String doIt() throws Exception {
//		MVMRCriticalTaskForClose task = new MVMRCriticalTaskForClose(
//				Env.getCtx(), Utilities.getCriticalTaskForClose(getRecord_ID()), get_TrxName());

			distribucion = getRecord_ID();
			MVMRDistributionHeader distribucion_aprobada = new MVMRDistributionHeader(getCtx(), distribucion, get_TrxName());
			
			// Generacion de Alerta de Tarea Critica para Asignar precio de venta
			// Con OC
			if ((distribucion_aprobada.getC_Order_ID()!=0)
				&&(distribucion_aprobada.getXX_DistributionStatus().equals(X_Ref_XX_DistributionStatus.PENDIENTE_POR_FIJAR_PRECIOS_DEFINITIVOS.getValue())) 
				//&& (distribucion_aprobada.isXX_Alert() == false) Ya está creada la alerta
				) {
				try {
					// Creación de Tarea Crítica
	//				Env.getCtx().setContext("#XX_TypeAlertAP","AP");
	//				Env.getCtx().setContext("#XX_POProdDistriCT",distribucion_aprobada.get_ID());
	//				Utilities.generatedAlert(distribucion_aprobada.get_ID());
	
//					String mens = "La distribución N° " + distribucion_aprobada.get_ValueAsInt("DOCUMENTNO")
//					+ " se encuentra Pendiente por Fijar Precios Definitivos. ";

					MOrder order = new MOrder(Env.getCtx(),distribucion_aprobada.getC_Order_ID(), null);
					X_XX_VMR_Package paquete = new X_XX_VMR_Package(getCtx(), order.getXX_VMR_Package_ID(), null);
					X_XX_VMR_Collection collection = new X_XX_VMR_Collection(getCtx(), paquete.getXX_VMR_Collection_ID(), null);
					MBPartner vendor = new MBPartner(getCtx(), order.getC_BPartner_ID(), null);
//					MBPartner buyer = new MBPartner(getCtx(), order.getXX_UserBuyer_ID(), null);
					MVMRDepartment dep = new MVMRDepartment(Env.getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);
					
					String mensaje = Msg.getMsg( getCtx(), "XX_AssignPrice", 
								new String[]{order.getDocumentNo(), vendor.getName(), 
										dep.getValue() + " " + dep.getName(), collection.getName(), ""+distribucion});
					
					
					String query = " SELECT a.email email, a.name name from c_order  " +
							"	   join	c_bpartner cb on ( " + dep.getXX_UserBuyer_ID()+ " = cb.c_bpartner_id)" +
							"      join ad_user a on (a.c_bpartner_id = cb.c_bpartner_id)" +
							" where c_order_id = " + distribucion_aprobada.getC_Order_ID() ;
					PreparedStatement ps = null;
					ResultSet rs = null;
					String email2 = "", name = "";
					try {
						ps = DB.prepareStatement(query, null);			
						rs = ps.executeQuery();
						if (rs.next()) {
							email2 = rs.getString("email");
							name = rs.getString("name");
						}
					} catch (SQLException e) {			
						log.log(Level.SEVERE, query, e);
						e.printStackTrace();
					}  finally {
						DB.closeResultSet(rs);
						DB.closeStatement(ps);
					}
					
					System.out.println("Enviar notificación a: " + email2+ "\nMensaje: " + mensaje);
					
					enviarNotificacion(/*buyer.getName()*/email2, mensaje);
					
				}
				catch(Exception e)	{
					log.log(Level.SEVERE,e.getMessage());
				}
			}
			//Por redist //AP1
			if ((distribucion_aprobada.getC_Order_ID()==0)
				&&(distribucion_aprobada.getXX_DistributionStatus().equals("FP")) ) {
				try {
//					String mens = "La distribución N° " + distribucion_aprobada.get_ValueAsInt("DOCUMENTNO")
//						+ " se encuentra Pendiente por Fijar Precios Definitivos. ";
					
					String mensaje = Msg.getMsg( getCtx(), "XX_AssignPriceNotOrder",
								new String[]{distribucion+"", distribucion_aprobada.getXX_Department_Name()});
					
//					String query = " SELECT xx_vendoremail email, p.name name from c_bpartner p where c_bpartner_id = " +
//						" (SELECT xx_userbuyer_id " + //nvl(XX_VMR_DEPARTMENT_ID, -1) dept " +
//						"	from XX_VMR_DISTRIBUTIONDETAIL join XX_VMR_DEPARTMENT using (XX_VMR_DEPARTMENT_ID) " +
//						"	where xx_vmr_distributionheader_id = " + distribucion + ") ";
					String query = " SELECT email email, p.name name from ad_user join c_bpartner p using (c_bpartner_id)" +
							"      where c_bpartner_id = (" +
							"	      SELECT xx_userbuyer_id from XX_VMR_DISTRIBUTIONDETAIL" +
							"              join XX_VMR_DEPARTMENT using (XX_VMR_DEPARTMENT_ID)" +
							"				where xx_vmr_distributionheader_id = " + distribucion + "	) ";
					PreparedStatement ps = null;
					ResultSet rs = null;
					String email2 = "", name = "";
					try {
						ps = DB.prepareStatement(query, null);			
						rs = ps.executeQuery();
						if (rs.next()) {
							email2 = rs.getString("email");
							name = rs.getString("name");
						}
					} catch (SQLException e) {			
						log.log(Level.SEVERE, query, e);
						e.printStackTrace();
					}  finally {
						DB.closeResultSet(rs);
						DB.closeStatement(ps);
					}
					
					enviarNotificacion(email2, mensaje);
				}
				catch(Exception e) 	{
					log.log(Level.SEVERE,e.getMessage());
				}
			
			}
		
		return "ok";
		
	}

	@Override
	protected void prepare() {
		
		
	}
	
	
	protected void enviarNotificacion(String destinatario, String msg) {
		String subject = "Notificación de Tarea Crítica: Distribución en espera de precios definitivos."; 
		String trace = "";
		if(destinatario.contains("@")) {
			MClient m_client = MClient.get(Env.getCtx());
			EMail email = m_client.createEMail(null, destinatario, " ", subject, msg);
			if (email != null){		
				try { 
					String status = email.send();
					log.info("Email Send status: " + status);
					if (email.isSentOK()){}
				} catch (Exception e) {
					e.printStackTrace(); trace = e.toString();
				}
			}
		}
		
	}
	

}