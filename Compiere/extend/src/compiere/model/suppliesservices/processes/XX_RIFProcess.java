package compiere.model.suppliesservices.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MBPartner;
import compiere.model.cds.Utilities;

	/** Purchase of Supplies and Services 
	 * Maria Vintimilla Funcion 003
	 * Notification via e-mail two months before the RIF Expiration Date. 
	 * Send mail to accounts payable**/
	public class XX_RIFProcess extends SvrProcess{

	@Override
	protected String doIt() throws Exception {
		String message="";
		String ExpDate;
		int PARTNER_ID = 0;
		int Role1 = 0;
		int Role2 = 0;
		int Role3 = 0;
		int Role4 = 0;
		
		String SQL1 = " SELECT C_BPARTNER_ID ID, " +
					" TO_CHAR(XX_RIFEXPIRATIONDATE, 'DD/MM/YYYY') RIFDATE" +
					" FROM C_BPARTNER " +
					" WHERE XX_VendorClass != '"+
					Env.getCtx().getContextAsInt("#XX_L_VENDCLASSIMP")+"'" +
					" and TRUNC(XX_RIFEXPIRATIONDATE) - TRUNC(SYSDATE) = 60"+
//					" AND TRUNC(SYSDATE) - TRUNC(XX_RIFEXPIRATIONDATE) = 30"+
					" AND AD_Client_ID = " + 
					Env.getCtx().getAD_Client_ID();
		//System.out.println("SQL1: "+SQL1);
		
		PreparedStatement pstmt = DB.prepareStatement(SQL1, null); 
		ResultSet rs = pstmt.executeQuery();
			
		try {
			while (rs.next()){
				PARTNER_ID = rs.getInt("ID");
				ExpDate = rs.getString("RIFDATE");
				if(ExpDate != null){
					MBPartner Vendor= new MBPartner(Env.getCtx(),
							PARTNER_ID,get_TrxName());
					Role1 = Env.getCtx().getContextAsInt("#XX_L_ROLEFINANCIALMANAGER_ID");
					Role2 = Env.getCtx().getContextAsInt("#XX_L_ROLEACCPAYASSITANT_ID");
					Role3 = Env.getCtx().getContextAsInt("#XX_L_ROLEACCPAYANALIST_ID");
					Role4 = Env.getCtx().getContextAsInt("#XX_L_ROLE_ACCOUNTTOPAYCOORD_ID");
					sendMailAP(message, Vendor.getName(), ExpDate, PARTNER_ID, Role1);
					sendMailAP(message, Vendor.getName(), ExpDate, PARTNER_ID, Role2);
					sendMailAP(message, Vendor.getName(), ExpDate, PARTNER_ID, Role3);
					sendMailAP(message, Vendor.getName(), ExpDate, PARTNER_ID, Role4);
				}// Fin ExpDate
			}//While
		}
		catch (Exception e){
			e.printStackTrace();
		} 
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return "Notificacion via e-mail 2 meses antes de la expiración del RIF";
	}

	@Override
	protected void prepare() {
		
	}
	
	/** sendMailAP
	 * @param message E-mail's text
	 * @param vendor Vendor
	 * @param Date RIF expiration date
	 * @param vendor
	 *  */
	private void sendMailAP(String message, String vendor, String expiration, 
			int partner, int ROLE_ID){
		message = Msg.getMsg( Env.getCtx(), "XX_RIFDate", new String[]{vendor, expiration});
		String sql = " SELECT AD_USER_ID " +
				" FROM AD_User_Roles" +
				" WHERE IsActive = 'Y' " +
				" AND AD_ROLE_ID = "+ ROLE_ID +
				" AND AD_USER_ID <> 100 ";
		//System.out.println("sql role: "+sql);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Utilities f = new Utilities(Env.getCtx(), null,
						Env.getCtx().getContextAsInt("#XX_L_MT_RIFEXPDATE_ID"), message, -1, 
						Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, 
						rs.getInt("AD_USER_ID"), null);
				try {
					f.ejecutarMail();
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
				f = null;
			}
		}//try
		catch (Exception e){
			log.saveError("ErrorSql Fecha Expiracion RIF Proveedor", Msg.getMsg(Env.getCtx(), e.getMessage()));
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}//finally
	}// Fin sendMailAP
	
}// Fin XX_VerifyExpDateRIFProcess
