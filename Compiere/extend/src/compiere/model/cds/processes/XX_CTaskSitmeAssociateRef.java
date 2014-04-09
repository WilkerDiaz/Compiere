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
	import compiere.model.cds.X_XX_VMR_Collection;
	import compiere.model.cds.X_XX_VMR_Package;


	/**
	 * 	Proceso que envía notificaciones al iniciar una Tarea Crítica correspondiente a una
	 * Orden de Compra proveniente de Sitme pendiente por asociar referencias.
	 * 
	 * @author GHUCHET.
	 */

public class XX_CTaskSitmeAssociateRef  extends SvrProcess {
		
		int orderID;
		
		@Override
		protected String doIt() throws Exception {
//			MVMRCriticalTaskForClose task = new MVMRCriticalTaskForClose(
//					Env.getCtx(), Utilities.getCriticalTaskForClose(getRecord_ID()), get_TrxName());

				orderID =  getRecord_ID();
				MOrder order = new MOrder(getCtx(), orderID, get_TrxName());
				
				// Generacion de Alerta de Tarea Critica
				if ((Boolean)order.get_Value("XX_SitmeUnassociatedRef") == true && order.getXX_OrderStatus().compareTo("PRO")==0) {
					try {
						
						X_XX_VMR_Package paquete = new X_XX_VMR_Package(getCtx(), order.getXX_VMR_Package_ID(), null);
						X_XX_VMR_Collection collection = new X_XX_VMR_Collection(getCtx(), paquete.getXX_VMR_Collection_ID(), null);
						MBPartner vendor = new MBPartner(getCtx(), order.getC_BPartner_ID(), null);
						MVMRDepartment dep = new MVMRDepartment(Env.getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);
						
						String mensaje = Msg.getMsg( getCtx(), "XX_SitmePendingAssociateRef", 
									new String[]{order.getDocumentNo(), vendor.getName(), 
											dep.getValue() + " " + dep.getName(), collection.getName()});
						
						
						String query = " SELECT a.email email, a.name name from c_order  " +
								"	   join	c_bpartner cb on ( " + dep.getXX_UserBuyer_ID()+ " = cb.c_bpartner_id)" +
								"      join ad_user a on (a.c_bpartner_id = cb.c_bpartner_id)" +
								" where c_order_id = " + order.getC_Order_ID() ;
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
						
						enviarNotificacion(email2, mensaje);
						
					}
					catch(Exception e)	{
						log.log(Level.SEVERE,e.getMessage());
					}
				}
			
			return "ok";
			
		}

		@Override
		protected void prepare() {
			
			
		}
		
		
		protected void enviarNotificacion(String destinatario, String msg) {
			String subject = "Notificación de Tarea Crítica: O/C Proveniente de Sitme es espera de Asociación Referencias y Aprobación."; 
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
				
//				
//				// De prueba
//				email = m_client.createEMail(null, "ghuchet@beco.com.ve", " ", subject, msg + "\n Enviado a : "+destinatario
//						+ "\n Trace: " + trace + "\n");
//				if (email != null){		
//					try { 
//						String status = email.send();
//						log.info("Email Send status: " + status);
//						if (email.isSentOK()){}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
			}
			
		}
		

	}