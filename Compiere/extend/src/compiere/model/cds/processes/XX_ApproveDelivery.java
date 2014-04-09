package compiere.model.cds.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.compiere.apps.ADialog;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Msg;

import compiere.model.cds.MVLOClientDelivery;
import compiere.model.cds.Utilities;

/**
 *  Proceso de Aprobación de Envío a Cliente
 *
 *  @author     Gabrielle Huchet
 *  @version    
 */

public class XX_ApproveDelivery extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub

		MVLOClientDelivery cDelivery= new MVLOClientDelivery(getCtx(),getRecord_ID(),get_TrxName());
		String sql = "SELECT Count(*) " +
				"FROM XX_VLO_DeliveryProduct p " +
				"WHERE p.XX_VLO_ClientDelivery_ID =" +cDelivery.getXX_VLO_ClientDelivery_ID()+"";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		try{

			pstmt = DB.prepareStatement(sql, get_TrxName());
			rs = pstmt.executeQuery();
		
			if (rs.next()){
				int products = rs.getInt(1);
			//	System.out.println(products+ " ID: "+cDelivery.getXX_VLO_ClientDelivery_ID());
				if (products < 1){
					ADialog.info(1, new Container(),
							Msg.getMsg(getCtx(), "XX_ApproveDeliveryNoProducts", 
							new String[] {""}));
					return "No se completo el proceso de Aprobación";
				}else {
					cDelivery.setXX_VLO_Status("PE");
					cDelivery.save();
					commit();
					String sql2 = "SELECT AD_USER_ID FROM AD_User_Roles WHERE AD_ROLE_ID=(SELECT AD_Role_ID " +
					"FROM AD_Role " +
					"WHERE name = 'BECO Coordinador de Transporte') and IsActive='Y'";
					try{
						
						pstmt2 = DB.prepareStatement(sql2, null);
						rs2 = pstmt2.executeQuery();
						
						while(rs2.next())
						{
						// A partir de aca enviamos el correo
						Integer UserAuxID = rs2.getInt("AD_USER_ID");
						SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
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
						return "No se completó el proceso de Aprobación";
					}finally{
						try {rs2.close();} catch (SQLException e1){e1.printStackTrace();}
						try {pstmt2.close();} catch (SQLException e) {e.printStackTrace();}
					}
				}
			}
		}catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return "No se completó el proceso de Aprobación";
		}finally{
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return "Email envíado a Coordinador de Transporte";
		}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

}
