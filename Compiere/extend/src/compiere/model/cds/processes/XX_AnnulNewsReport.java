package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.model.MClient;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import compiere.model.cds.MOrder;
import compiere.model.cds.X_XX_VLO_NewsReport;
import compiere.model.cds.X_XX_VMR_Department;

public class XX_AnnulNewsReport extends SvrProcess {

	private String motive = ""; 
	Vector<String> emailRoles = null;
	
	@Override
	protected String doIt() throws Exception {
		
		X_XX_VLO_NewsReport newsReport = new X_XX_VLO_NewsReport( Env.getCtx(), getRecord_ID(), get_Trx());
		
		newsReport.setXX_Annul("Y");
		newsReport.setXX_AnnulMotive(motive);
		newsReport.setXX_Status("AN");
		
		if(newsReport.save())
			sendMail(newsReport);
		
		return "Proceso Finalizado";
	}

	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] parameter = getParameter();
		
		for (ProcessInfoParameter element : parameter) {
			
			String name = element.getParameterName();			
			if (element.getParameter() == null) ;
			else if (name.equals("XX_AnnulMotive")) { 
				motive = element.getParameter().toString();
			} else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);			
		}		
	}
	
	private boolean sendMail(X_XX_VLO_NewsReport newsReport){
		
		//Revisoria
		String emailTo = "revisoria@beco.com.ve";
			
		MClient m_client = MClient.get(Env.getCtx());
		
		MOrder order = new MOrder( Env.getCtx(), newsReport.getC_Order_ID(), null);
		X_XX_VMR_Department depart = new X_XX_VMR_Department( Env.getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);	
		
		String subject = "Producto Anulado de Reporte de Novedad de la O/C : " + order.getDocumentNo();
		String msg = "Se ha anulado la siguiente referencia del reporte de novedad de la O/C: " + order.getDocumentNo() +
					 " (Dpto: " + depart.getValue() +")";
		
		msg += "\n\nReferencia: " + newsReport.getXX_VendorReference() +
				"\nDescripción: " + newsReport.getDescription();
		
		msg += "\n\nPor el siguiente motivo:";
		msg += "\n" + motive;
		
			
		EMail email = m_client.createEMail(null, emailTo, "Revisoria", subject, msg);
		
		if (email != null)
		{			
				
			Vector<String> emails = getEmails();
			
			for(int i=0; i<emails.size(); i++){
		
				if(emails.get(i).contains("@")){
					email.addTo(emails.get(i), emailRoles.get(i));
				}				
			}

			String status = email.send();
			
			log.info("Email Send status: " + status);
				
			if (email.isSentOK())
			{
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}
	
	private Vector<String> getEmails(){
		
		Vector<String> emails = new Vector<String>();
		emailRoles =  new Vector<String>();
		
		String sqlEmails = "select email,c.name from ad_user a, AD_User_Roles b, AD_Role c " +
						   "where a.ad_user_id = b.ad_user_id and b.ISACTIVE = 'Y' " +
                           "and b.ad_role_id=c.ad_role_id and " +
                           "(b.AD_ROLE_ID = " + Env.getCtx().getContextAsInt("#XX_L_ROLECHECKUPCOORDINATOR_ID") +
                           " or b.AD_ROLE_ID = " + Env.getCtx().getContextAsInt("#XX_L_ROLEWAREHOUSECOORD_ID") +
                           ") order by b.AD_ROLE_ID";

		ResultSet rs_Email = null;
		PreparedStatement prst_Email = null;
		
		try {
		
			prst_Email = DB.prepareStatement(sqlEmails, null);
			rs_Email = prst_Email.executeQuery();
			
			while(rs_Email.next()){
				
				emails.add(rs_Email.getString(1));
				emailRoles.add(rs_Email.getString(2));
			}
			
		}catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
		}
		finally
		{
			DB.closeResultSet(rs_Email);
			DB.closeStatement(prst_Email);
		}
		
		return emails;
	}

}