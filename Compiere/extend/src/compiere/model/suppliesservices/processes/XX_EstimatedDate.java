package compiere.model.suppliesservices.processes;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.Utilities;

/** Purchase of Supplies and Services 
 * Maria Vintimilla Funcion 10
 * Notificacion via correo 2 días antes de la fecha estimada de una OC 
 * Envía correo al colaborador que creo la OC**/
public class XX_EstimatedDate extends SvrProcess {


	@Override
	protected String doIt() throws Exception {
		String message = "";
		String EstDate;
		Integer collaborator_ID = new Integer(0);
		String ORDER_ID = "";
		String sqlType = " SELECT DOCUMENTNO ID,  " +
						" TO_CHAR(XX_EstimatedDate, 'DD/MM/YYYY') ESTDATE,  " +
						" CreatedBy USERID" +
						" FROM C_Order " +
						" WHERE XX_POType = '"+
						Env.getCtx().getContext("#XX_L_POTYPE")+"' "+
						" AND TRUNC(sysdate) - TRUNC(XX_EstimatedDate) = 2 " +
						" AND XX_OrderStatus NOT IN ('AN', 'CH', 'RE', 'PRO')" +
						" AND AD_Client_ID = " + 
						Env.getCtx().getAD_Client_ID();
		//System.out.println("sqlType: "+sqlType);

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sqlType, null); 
			rs = pstmt.executeQuery(); 
			while (rs.next()){
				ORDER_ID = rs.getString("ID");
				EstDate = rs.getString("ESTDATE");
				collaborator_ID = rs.getInt("USERID");
				sendMailCol(message, ORDER_ID, EstDate, collaborator_ID);
			}
		}//try
		catch (Exception e) {
			log.saveError("ErrorSql", Msg.getMsg(Env.getCtx(), e.getMessage()));
		}	
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return "Notificacion via e-mail 2 días antes de la fecha estimada O/C";
	}// doIt

	@Override
	protected void prepare() {

	}//prepare

	/** sendMailCol
	 * @param message E-mail's text
	 * @param Order_ID Order_ID
	 * @param Estimated Estimated date
	 * @param collaborator_ID Collaborator ID
	 *  */
	private void sendMailCol(String message, String Order_ID, String estimated, 
			Integer collaborator_ID){
		message = Msg.getMsg( Env.getCtx(), "XX_EstimatedDate", 
				new String[]{Order_ID.toString(),estimated.toString()});
		Utilities f = new Utilities(Env.getCtx(), null,
				Env.getCtx().getContextAsInt("#XX_L_MT_ESTIMATEDDATE_ID"), message, -1, 
				Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, collaborator_ID, null);
		try {
			f.ejecutarMail();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		f = null;
	}// Fin sendMailCol		
}// Fin XX_EstimatedDate
