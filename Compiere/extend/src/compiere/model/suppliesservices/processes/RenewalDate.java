package compiere.model.suppliesservices.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.model.MClient;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.Msg;


/** Purchase of Supplies and Services 
 * @author Maria Vintimilla Funcion 34
 * Notification via e-mail three months before the Renewal Date of a contract. 
 * Send mail to legal and the corresponding responsable **/
public class RenewalDate extends SvrProcess{

	@Override
	protected String doIt() throws Exception {

		String DateTo = "";
		String Renewal = "";
		String contract_ID = "";
		String ContractAmount = "";
		String Description = "";
		String BPName = "";
		String ManagementName = "";
		Vector<Integer> roles = new Vector<Integer>();

		String SQL1 = " SELECT " +
					" CASE WHEN O.Description is null THEN O.Name  " +
					" ELSE O.Description END Descrip, " +
					" C.Value ID, " +
					" TO_CHAR(C.XX_RenewalNotificationDate, 'DD/MM/YYYY') RENEWAL, " +
					" TO_CHAR(C.XX_DateTo, 'DD/MM/YYYY') DATETO, " +
					" C.Description DESCR, " +
					" C.XX_ContractAmount AMOUNT, " +
					" CB.Name BP," +
					" C.XX_Responsable_ID Resp "+
					" FROM XX_Contract C inner join C_BPartner CB " +
					" ON (C.C_Bpartner_ID = CB.C_BPartner_ID)" +
					" left outer join AD_Org O " +
					" ON (o.ad_org_id = C.XX_Management_ID)" +
					" WHERE C.XX_RenewalNotificationDate = add_months(TRUNC(sysdate),2)" +
					" AND C.AD_Client_ID = " + 
					Env.getCtx().getAD_Client_ID()+
					" ORDER BY C.XX_DateTo";
		//			System.out.println("SQL1: "+SQL1);

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(SQL1, null); 
			rs = pstmt.executeQuery();

			while(rs.next()){
				ManagementName = rs.getString("Descrip");
				contract_ID = rs.getString("ID");
				Renewal = rs.getString("RENEWAL");
				DateTo = rs.getString("DATETO");  
				Description = rs.getString("DESCR");
				ContractAmount = rs.getString("AMOUNT");
				BPName = rs.getString("BP");
				
				roles.add(Env.getCtx().getContextAsInt("XX_L_ROLELEGAL_ID"));
				roles.add(Env.getCtx().getContextAsInt("XX_L_ROLELEGALADVISOR_ID"));
				roles.add(rs.getInt("Resp"));

				// Send Email
				sendMail(DateTo, roles, contract_ID, Renewal, ContractAmount, Description, BPName, ManagementName);
			}	
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}//finally
		return "Fin del Proceso";
	}

	@Override
	protected void prepare() {
		//DO NOTHING
	}


	/** sendMail
	 * @param message E-mail's text
	 * @param Date Contract's DateTo
	 * @param roles
	 * @param Contract_ID Contract Code
	 * @param Renewal Cotnract's Renewal Date
	 * @param Recurrency Contract's Payment Recurrence
	 * @param ContractAmount Contract's Amount
	 * @param Description Contract's Description
	 * @param BPName Contract's Business Partner Name
	 *  */
	private void sendMail(String dateTo, 
						  Vector<Integer> roles, String Contract_ID, String Renewal, 
						  String ContractAmount, String Description, String BPName, 
						  String Gerencia)
	{
		if(roles.size()==0)
			return;
		
		String subject = "Notificación de fecha de renovación de contrato";
		
		String message = Msg.getMsg( Env.getCtx(), "XX_ContractNotification", 
				new String[]{dateTo,BPName, Description,Gerencia,ContractAmount.toString(),
				Renewal,Contract_ID.toString()});
			
		String sql = "SELECT a.email, c.NAME " +
					 "FROM AD_USER a, AD_User_Roles b, AD_ROLE C " +
					 "WHERE a.AD_USER_ID = b.AD_USER_ID AND b.AD_ROLE_ID = c.AD_ROLE_ID AND b.isActive = 'Y' " +
					 "AND a.AD_USER_ID <> 100 AND c.AD_ROLE_ID IN ("; 
		
					 for(int i=0; i<roles.size(); i++){
						 sql += roles.get(i);
						 if(i+1<roles.size())
							 sql += ",";
						 else
							 sql += ")";
					 }

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Vector<String> emails = new Vector<String>();
		Vector<String> emailRoles = new Vector<String>();
		
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				emails.add(rs.getString(1));
				emailRoles.add(rs.getString(2));
			}
		}
		catch (Exception e){
			log.log(Level.SEVERE, "ErrorSql ROLES EMAIL " + Msg.getMsg(Env.getCtx(), e.getMessage()));
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		MClient m_client = MClient.get(Env.getCtx());
		EMail email = m_client.createEMail(null, emails.get(0), emailRoles.get(0), subject, message);
		
		if (email != null)
		{			
			
			for(int i=0; i<emails.size(); i++){
		
				if(emails.get(i).contains("@")){
					email.addTo(emails.get(i), emailRoles.get(i));
				}				
			}

			String status = email.send();
			
			log.info("Email Send status: " + status);
		}
	}

}
