package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.compiere.model.MClient;
import org.compiere.model.MWarehouse;
import org.compiere.model.X_AD_User;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MOrder;

public class XX_SendEmailEntryAuthorization extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub

		SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
		String sql1 = "SELECT C_Order_ID " +
				"FROM C_Order " +
				"WHERE ISSOTRX = 'N' AND XX_Authorized = 'N' " +
				"AND UPPER(XX_ORDERSTATUS)=UPPER('CH') " +
				"AND UPPER(XX_ORDERTYPE)=UPPER('Nacional') " +
				"AND XX_VLO_TYPEDELIVERY = 'DD' " +
				"AND TRUNC(SYSDATE) = TRUNC(XX_EstimatedDate-10) " +
				"AND AD_CLIENT_ID = " +Env.getCtx().getAD_Client_ID()+" "+
				"AND IsActive='Y'";
		
		System.out.println(sql1);
		
		PreparedStatement pstmt1 =null;
		ResultSet rs1 = null;
		PreparedStatement pstmt2 =null;
		ResultSet rs2 = null;
		try{

			pstmt1 = DB.prepareStatement(sql1, null);
			rs1 = pstmt1.executeQuery();
			
			while(rs1.next())
			{
				MOrder order= new MOrder(getCtx(),rs1.getInt("C_Order_ID"),get_TrxName());
				String sql2 = "\nSELECT DISTINCT U.AD_USER_ID " +
					"\nFROM AD_User_Roles UR INNER JOIN AD_USER U ON (U.AD_USER_ID= UR.AD_USER_ID) " +
					"\nWHERE UR.AD_ROLE_ID IN (" +
					"\nSELECT AD_Role_ID FROM AD_Role WHERE name IN (" +
					"\n'BECO Asistente de Cuentas por Pagar','BECO Coordinador de Cuentas por Pagar'," +
					"\n'BECO Cuentas por Pagar') and IsActive='Y') and u.name <> 'SuperUser' and U.IsActive='Y' AND UR.IsActive='Y'";   
				try{
					
					pstmt2 = DB.prepareStatement(sql2, null);
					rs2 = pstmt2.executeQuery();
					
					while(rs2.next())
					{
						X_AD_User destinatario = new X_AD_User( Env.getCtx(), rs2.getInt(1), null);
						MWarehouse tienda = new MWarehouse(Env.getCtx(), order.getM_Warehouse_ID(), null);
						String emailTo = destinatario.getEMail();
						System.out.println("email: "+destinatario.getEMail()+" "+destinatario.getName());
						MClient m_client = MClient.get(Env.getCtx());
						
						String subject = "O/C de Despacho Directo Pendiente Autorizar Entrada" ;
						//String msg = "La O/C nro: {0} de Despacho Directo a la tienda {1} está pendiente por autorizar la entrada."
				
						String msg = Msg.getMsg(getCtx(), "DDOCEntranceAuthorization", 
								new String[] { "" + order.getDocumentNo(), "" +tienda.getValue()+"-"+tienda.getName()});
						
						EMail email = m_client.createEMail(null, emailTo, "Cuentas Por Pagar", subject, msg);
						
						if (email != null)
						{			
							String status = email.send();
						
							log.info("Email Send status: " + status);
							
							if (email.isSentOK()){}
							else
								return "Error al ejecutar el proceso";
						}
						else
							return "Error al ejecutar el proceso";	
					}
				rs2.close();
				pstmt2.close();
				}catch (Exception e){
					log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
					return "Error al ejecutar el proceso";
				}finally{
					try {rs2.close();} catch (SQLException e1){e1.printStackTrace();}
					try {pstmt2.close();} catch (SQLException e) {e.printStackTrace();}
				}
			}
		}catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return "Error al ejecutar el proceso";
		}finally{
			try {rs1.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt1.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return "Proceso Completado con Éxito. Email Pendientes Envíados a Cuentas por Pagar";
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

}
