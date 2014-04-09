package compiere.model.suppliesservices.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MBPartner;
import compiere.model.cds.Utilities;

/** Purchase of Supplies and Services 
 * Maria Vintimilla Funcion 02
 * Auto-inactive vendor after one year and a half since the approval date 
 * for the PO or Contract. Send mail to accounts payable **/
public class XX_InactivVendor extends SvrProcess {

	private String message = "";
	private int Role1 = Env.getCtx().getContextAsInt("#XX_L_ROLEFINANCIALMANAGER_ID");
	private int Role2 = Env.getCtx().getContextAsInt("#XX_L_ROLEACCPAYASSISTANT_ID");
	private int Role3 = Env.getCtx().getContextAsInt("#XX_L_ROLEACCPAYANALIST_ID");
	private int Role4 = Env.getCtx().getContextAsInt("#XX_L_ROLE_ACCOUNTTOPAYCOORD_ID");

	@Override
	protected void prepare() {

	}

	@Override
	protected String doIt() throws Exception {
		Timestamp today;
		Integer AuxC_Bpartner_ID = new Integer(0);
		MBPartner Aux_MBPartner;

		//Get the Vendor's
		String SQLintersect = " SELECT DISTINCT(O.C_BPartner_ID) ID, " +
							" SYSDATE TODAY" +
							" FROM C_ORDER O " +
							" WHERE UPPER(XX_OrderStatus) IN ('AP') AND " +
							" ROUND(sysdate-XX_APPROVALDATE) >= 547 AND " +
							" XX_APPROVALDATE IS NOT NULL AND "+
							" XX_POType = '"+Env.getCtx().getContext("#XX_L_POTYPE")+"'" +
							" AND O.AD_Client_ID = " + 
							Env.getCtx().getAD_Client_ID()+
							" INTERSECT " +
							" SELECT DISTINCT(C.C_BPartner_ID) ID," +
							" SYSDATE TODAY" +
							" FROM XX_Contract C " +
							" WHERE C.XX_ContractApproval IS NOT NULL AND " +
							" ROUND(sysdate-C.XX_ContractApproval) >= 547 " +
							" AND AD_Client_ID = " + 
							Env.getCtx().getAD_Client_ID();
		//System.out.println("SQL2:"+SQLintersect );

		// Desactivar proveedor:
		PreparedStatement pstmt2 = DB.prepareStatement(SQLintersect, null);
		ResultSet rs2 = pstmt2.executeQuery();
		try {
			while (rs2.next()){
				AuxC_Bpartner_ID = rs2.getInt("ID");
				today = rs2.getTimestamp("TODAY");
				Aux_MBPartner = new MBPartner(Env.getCtx(), AuxC_Bpartner_ID, 
						get_TrxName());
				if(Aux_MBPartner.getisActive().equals("Y")){
					Aux_MBPartner.setIsActive(false);
					Aux_MBPartner.setXX_ExitDate(today);
					Aux_MBPartner.save(); 
					message = Msg.getMsg( Env.getCtx(), "XX_DeactivateVendorPO", 
							new String[]{Aux_MBPartner.getName()});
					//					System.out.println(message);
					sendMailAP(message, Aux_MBPartner.getName(), Role1);
					sendMailAP(message, Aux_MBPartner.getName(), Role2);
					sendMailAP(message, Aux_MBPartner.getName(), Role3);
					sendMailAP(message, Aux_MBPartner.getName(), Role4);
				}//If Active
			}//While
		}// try
		catch (Exception e){
			e.printStackTrace();
		}
		finally {
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);	
		}//finally

		return "Inactivación de proveedor sin O/c, contrato 1.5 años";
	}//doIt

	/** sendMailAP
	 * @param message E-mail's text
	 * @param vendor Vendor
	 * @param Last approval date
	 * @param vendor
	 *  */
	private void sendMailAP(String message, String vendor, Integer ROLE_ID){
		String message2 = Msg.getMsg( Env.getCtx(), "XX_DeactivateVendorPO", 
				new String[]{vendor});

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
						Env.getCtx().getContextAsInt("#XX_L_MT_VENDORDEACTIV_ID"), 
						message2, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), 
						-1, rs.getInt("AD_USER_ID"), null);
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

}// Fin XX_InactiveVendor
