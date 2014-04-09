package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.model.X_AD_User;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MBPartner;
import compiere.model.cds.MOrder;
import compiere.model.cds.MVMRCategory;
import compiere.model.cds.MVMRDepartment;

/** Envía Email indicando que las ordenes de compra que han sido autorizada y 
 * no han sido recibidas (a planificador del dpto de la OC y al jefe de planificación)
 * @author Gabrielle Huchet
 */
public class XX_SendMailPendingOCReception extends SvrProcess  {

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String doIt() throws Exception {
		
		String sql = "\nSELECT C_ORDER_ID " +
				"\nFROM C_ORDER " +
				"\nWHERE XX_AUTHORIZED = 'Y' AND XX_VLO_TYPEDELIVERY != 'DD' " +
				"\nAND XX_ORDERSTATUS !='RE' AND XX_ORDERSTATUS !='CH' AND XX_ORDERSTATUS !='AN' " +
				"\nAND ISSOTRX = 'N' AND XX_POTYPE = 'POM' ";
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		try{
			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				sendMail();
			}else return "No hay OC Pendientes";
		}catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return "Proceso Completado";
	}
	
	private boolean sendMail() {
		
		int plannerManagerID = getPlannerManager();
		X_AD_User plannerManager = new X_AD_User( Env.getCtx(), plannerManagerID, null);
		String emailTo = plannerManager.getEMail();
		
		if(emailTo.contains("@")){
			MClient m_client = MClient.get(Env.getCtx());
			String subject = "O/C Autorizadas y no Recibidas";		
			String msg = "Las siguientes ordenes de compra fueron autorizadas y no han sido recibidas: ";
				
			int orderID;
			MOrder order = null;
			MVMRDepartment dep = null;
			MBPartner vendor = null;
			String sql = "\nSELECT C_ORDER_ID " +
					"\nFROM C_ORDER " +
					"\nWHERE XX_AUTHORIZED = 'Y' AND XX_VLO_TYPEDELIVERY != 'DD' " +
					"\nAND XX_ORDERSTATUS !='RE' AND XX_ORDERSTATUS !='CH' AND XX_ORDERSTATUS !='AN' " +
					"\nAND ISSOTRX = 'N' AND XX_POTYPE = 'POM' ";
		
			PreparedStatement pstmt =null;
			ResultSet rs = null;
			try{
			
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				
				while(rs.next()){
					orderID = rs.getInt(1);
					Calendar calendar = Calendar.getInstance();
					order = new MOrder(getCtx(), orderID, null);
					dep = new MVMRDepartment( Env.getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);
					vendor = new MBPartner(getCtx(), order.getC_BPartner_ID(), null);
					calendar.setTime(order.getXX_EntranceDate());
					int month = calendar.get(Calendar.MONTH)+1;
					String date = calendar.get(Calendar.DAY_OF_MONTH)+"/"+month+"/"+calendar.get(Calendar.YEAR);
					msg +="\n\n Orden de Compra Nro: "+order.getDocumentNo()+", Dpto: "+dep.getValue()+"-"+dep.getName()+", Proveedor: "+vendor.getValue()+"-"+vendor.getName()+
						", Fecha de Autorización de Entrada: "+date;
				}
			}catch (Exception e){
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			}finally{
				try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
				try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			}
			
				
			EMail email = m_client.createEMail(null, emailTo, "Jefe de Planificación", subject, msg);
			System.out.println(emailTo);
			int AD_User_ID = 0;
			sql = "\nSELECT DISTINCT U.AD_USER_ID" +
			"\nFROM AD_USER_ROLES UR JOIN  AD_USER U ON (U.AD_USER_ID = UR.AD_USER_ID) " +
			"\nWHERE UR.AD_ROLE_ID  IN ("+Env.getCtx().getContextAsInt("#XX_L_ROLESCHEDULER_ID")+", "+//BECO Planificador
			+Env.getCtx().getContextAsInt("#XX_L_ROLESCHEDULEMANAGER_ID")+", "+  //BECO Jefe de Planificacion
			+Env.getCtx().getContextAsInt("#XX_L_ROLEDISTCENTERMANAGER_ID")+", " + //BECO Jefe de Centro de Distribución
			+Env.getCtx().getContextAsInt("#XX_L_ROLEWAREHOUSECOORD_ID")+" " + //BECO Coordinador de Almacén
					") "+ 
			"\nAND U.AD_USER_ID != "+plannerManagerID+" AND UR.isActive = 'Y'";
			//System.out.println(sql);
			try{
				
				pstmt = DB.prepareStatement(sql, null); 
				rs = pstmt.executeQuery();
					 
				while(rs.next()){
					AD_User_ID = rs.getInt("AD_USER_ID");
					X_AD_User planner = new X_AD_User( Env.getCtx(), AD_User_ID, null);
					String emailPlanner = planner.getEMail();
					if(emailPlanner != null && emailPlanner.contains("@")){
						email.addTo(emailPlanner, " ");
						System.out.println(emailPlanner);
					}
				} 
			}catch (Exception e) {
				log.log(Level.SEVERE, sql);
			}finally {
				try {
					rs.close();
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}	
			}	
			
			
			if (email != null){		
				String status = email.send();
				log.info("Email Send status: " + status);
				if (email.isSentOK()){}
					else return false;
				} else 
					return false;
		}
			
		return true;

	}


	private int getPlannerManager(){
		int AD_User_ID = 0;
		String sql = "\nSELECT DISTINCT U.AD_USER_ID" +
		"\nFROM AD_USER_ROLES UR JOIN  AD_USER U ON (U.AD_USER_ID = UR.AD_USER_ID) " +
		"\nWHERE UR.AD_ROLE_ID  = "+Env.getCtx().getContextAsInt("#XX_L_ROLESCHEDULEMANAGER_ID")+  //BECO Jefe de Planificacion
		"\nAND UR.isActive = 'Y'";
		//System.out.println(sql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
				 
			if(rs.next())
			{
				AD_User_ID = rs.getInt("AD_USER_ID");
			}	   
			}catch (Exception e) {
				log.log(Level.SEVERE, sql);
			}finally {
				try {
					rs.close();
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		
		return AD_User_ID;
	}
}
