package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Msg;

import compiere.model.cds.MVLOClientDelivery;
import compiere.model.cds.Utilities;

public class XX_ClientDeliverySendEmail extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub

		SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
		String sql1 = "SELECT XX_VLO_ClientDelivery_ID " +
				"FROM XX_VLO_ClientDelivery " +
				"WHERE XX_VLO_Status= 'PE' and IsActive='Y'";
		PreparedStatement pstmt1 =null;
		ResultSet rs1 = null;
		PreparedStatement pstmt2 =null;
		ResultSet rs2 = null;
		try{

			pstmt1 = DB.prepareStatement(sql1, null);
			rs1 = pstmt1.executeQuery();
			
			while(rs1.next())
			{
				MVLOClientDelivery cDelivery= new MVLOClientDelivery(getCtx(),rs1.getInt("XX_VLO_ClientDelivery_ID"),get_TrxName());
				String sql2 = "SELECT AD_USER_ID FROM AD_User_Roles " +
						"WHERE AD_ROLE_ID=(SELECT AD_Role_ID " +
						"FROM AD_Role " +
						"WHERE name = 'BECO Coordinador de Transporte') and IsActive='Y'";
				try{
					
					pstmt2 = DB.prepareStatement(sql2, null);
					rs2 = pstmt2.executeQuery();
					
					while(rs2.next())
					{
					// A partir de aca enviamos el correo
					Integer UserAuxID = rs2.getInt("AD_USER_ID");
					// Este es el mensaje que se le enviara al Coordinador de Transporte				
					String mss = Msg.getMsg(getCtx(), "XX_ClientDelivery", 
							new String[] { "" + cDelivery.getValue(), "" + fm.format(cDelivery.getUpdated())
						});
					Utilities f = new Utilities(getCtx(),null,getCtx().getContextAsInt("#XX_L_MT_ORDERDELIVERY_ID"), mss, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, null);
					f.ejecutarMail(); 
					f = null; 	
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
		
		return "Email envíados a Coordinador de Transporte";
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

}
