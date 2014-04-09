package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.model.MClient;
import org.compiere.model.X_AD_User;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRDepartment;

	
/**•Proceso que envía email a comprador y jefe de categoría de los productos pertenecientes 
 * a reportes de novedad que tengan más de 5 días pendientes de asignar o/c 
 *  @author Gabrielle Huchet
 *  @version 
 */

public class XX_NewsReportPendingEmail extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		
		// mas de 5 dias menos de 10
		String sql1 = "\nSELECT P.XX_VMR_DEPARTMENT_ID, D.XX_USERBUYER_ID, C.XX_CATEGORYMANAGER_ID " +
				"\nFROM XX_VLO_NEWSREPORT R JOIN C_ORDER P ON (R.C_ORDER_ID = P.C_ORDER_ID) " +
				"\nJOIN XX_VMR_DEPARTMENT D ON (P.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID) " +
				"\nJOIN XX_VMR_CATEGORY C ON (C.XX_VMR_CATEGORY_ID = D.XX_VMR_CATEGORY_ID) " +
				"\nWHERE R.XX_STATUS= 'AO' AND R.XX_ANNUL = 'N' AND XX_NEWORDER_ID IS NULL AND TRUNC(SYSDATE) - TRUNC(R.CREATED)> 5 " +
				"AND TRUNC(SYSDATE) - TRUNC(R.CREATED)<= 10 AND R.ISACTIVE='Y' " +
				"\nGROUP BY P.XX_VMR_DEPARTMENT_ID, D.XX_USERBUYER_ID, C.XX_CATEGORYMANAGER_ID";
		
		PreparedStatement pstmt1 =null;
		ResultSet rs1 = null;
		PreparedStatement pstmt2 =null;
		ResultSet rs2 = null;
		
		try{

			pstmt1 = DB.prepareStatement(sql1, null);
			rs1 = pstmt1.executeQuery();
			
			while(rs1.next())
			{
				MVMRDepartment dep = new MVMRDepartment( Env.getCtx(),rs1.getInt(1) , null);
				int buyerID = getAD_User_ID(rs1.getInt(2));
				X_AD_User buyer = new X_AD_User( Env.getCtx(), buyerID, null);
				int categoryMan = getAD_User_ID(rs1.getInt(3));
				X_AD_User categoryChief = new X_AD_User( Env.getCtx(), categoryMan, null);

				String emailTo = buyer.getEMail();

				MClient m_client = MClient.get(Env.getCtx());
				
				String subject = "Pendiente Asignar O/C en Reportes de Novedad" ;
				
				String msg = "Los siguientes productos del Departamento: "+dep.getValue()+"-"+dep.getName()+ " poseen un reporte de novedad y están pendientes por asignar O/C: \n";

				String sql2 = "\nSELECT R.DESCRIPTION, O.DOCUMENTNO " +
							  "\nFROM XX_VLO_NEWSREPORT R JOIN C_ORDER O ON (R.C_ORDER_ID = O.C_ORDER_ID) " +
							  "\nJOIN XX_VMR_DEPARTMENT D ON (O.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID) " +
							  "\nWHERE R.XX_STATUS= 'AO' AND R.XX_ANNUL = 'N' AND XX_NEWORDER_ID IS NULL AND TRUNC(SYSDATE) - TRUNC(R.CREATED)> 5 " +
							  "\nAND TRUNC(SYSDATE) - TRUNC(R.CREATED)<= 10 AND R.ISACTIVE='Y' " +
							  "\nAND O.XX_VMR_DEPARTMENT_ID = " +rs1.getInt(1);
		
				try{
					
					pstmt2 = DB.prepareStatement(sql2, null);
					rs2 = pstmt2.executeQuery();
					while(rs2.next())
					{				
						msg += "\n- " + rs2.getString(1) + " proveniente de la O/C: "+rs2.getString(2);
					}
				}catch (Exception e){
					log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
					return "Error al ejecutar el proceso";
				}finally{
					try {rs2.close();} catch (SQLException e1){e1.printStackTrace();}
					try {pstmt2.close();} catch (SQLException e) {e.printStackTrace();}
				}
				
				EMail email = m_client.createEMail(null, emailTo, "Comprador", subject, msg);
				
				if (email != null)
				{			
			
					//Jefe de Categoria
					if(categoryChief.getEMail().contains("@")){
						email.addTo(categoryChief.getEMail(), "Jefe de Categoria");
					}
					
					String status = email.send();
				
					log.info("Email Send status: " + status);
					
					if (email.isSentOK()){}
					else
						return "Error al envíar email";
				}
				else
					return "Error al envíar email";
				
			}
		}catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return "Error al ejecutar el proceso";
		}finally{
			try {rs1.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt1.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		
		// mas de 10 dias
		String sqla = "\nSELECT P.XX_VMR_DEPARTMENT_ID, D.XX_USERBUYER_ID, C.XX_CATEGORYMANAGER_ID  " +
				"\nFROM XX_VLO_NEWSREPORT R JOIN C_ORDER P ON (R.C_ORDER_ID = P.C_ORDER_ID) " +
				"\nJOIN XX_VMR_DEPARTMENT D ON (P.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID) " +
				"\nJOIN XX_VMR_CATEGORY C ON (C.XX_VMR_CATEGORY_ID = D.XX_VMR_CATEGORY_ID) " +
				"\nWHERE R.XX_STATUS= 'AO' AND R.XX_ANNUL = 'N' AND XX_NEWORDER_ID IS NULL AND TRUNC(SYSDATE) - TRUNC(R.CREATED)> 10 " +
				"AND R.ISACTIVE='Y' " +
				"\nGROUP BY P.XX_VMR_DEPARTMENT_ID, D.XX_USERBUYER_ID, C.XX_CATEGORYMANAGER_ID";
		
		PreparedStatement pstmta =null;
		ResultSet rsa = null;
		PreparedStatement pstmtb =null;
		ResultSet rsb = null;
		
		try{

			pstmta = DB.prepareStatement(sqla, null);
			rsa = pstmta.executeQuery();
			
			while(rsa.next())
			{
				MVMRDepartment dep = new MVMRDepartment( Env.getCtx(),rsa.getInt(1) , null);
				int buyerID = getAD_User_ID(rsa.getInt(2));
				X_AD_User buyer = new X_AD_User( Env.getCtx(), buyerID, null);
				int categoryMan = getAD_User_ID(rsa.getInt(3));
				X_AD_User categoryChief = new X_AD_User( Env.getCtx(), categoryMan, null);

				String emailTo = buyer.getEMail();

				MClient m_client = MClient.get(Env.getCtx());
				
				String subject = "Pendiente Asignar O/C en Reportes de Novedad" ;
				
				String msg = "Los siguientes productos del Departamento: "+dep.getValue()+"-"+dep.getName()+ " poseen un reporte de novedad (con más de 10 dias de antigüedad) y están pendientes por asignar O/C: \n";

				//TODO
				String sqlb = "\nSELECT R.DESCRIPTION, O.DOCUMENTNO " +
							  "\nFROM XX_VLO_NEWSREPORT R JOIN C_ORDER O ON (R.C_ORDER_ID = O.C_ORDER_ID) " +
							  "\nJOIN XX_VMR_DEPARTMENT D ON (O.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID) " +
							  "\nWHERE R.XX_STATUS= 'AO' AND R.XX_ANNUL = 'N' AND XX_NEWORDER_ID IS NULL AND TRUNC(SYSDATE) - TRUNC(R.CREATED)> 10 AND R.ISACTIVE='Y'" +
							  "\nAND O.XX_VMR_DEPARTMENT_ID = " +rs1.getInt(1);
		
				try{
					
					pstmtb = DB.prepareStatement(sqlb, null);
					rsb = pstmtb.executeQuery();
					while(rsb.next())
					{					
						msg += "\n- " + rs2.getString(1) + " proveniente de la O/C: "+rsb.getString(2);
					}
				}catch (Exception e){
					log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
					return "Error al ejecutar el proceso";
				}finally{
					try {rsb.close();} catch (SQLException e1){e1.printStackTrace();}
					try {pstmtb.close();} catch (SQLException e) {e.printStackTrace();}
				}
				
				EMail email = m_client.createEMail(null, emailTo, "Comprador", subject, msg);
				
				if (email != null)
				{			
			
					//Jefe de Categoria
					if(categoryChief.getEMail().contains("@")){
						email.addTo(categoryChief.getEMail(), "Jefe de Categoria");
					}
					
					//Gerente Mechandising
					Vector<String> merchManagerEmail = getMerchManagerEmail();
					
					for(int i=0; i<merchManagerEmail.size(); i++){
				
						if(merchManagerEmail.get(i).contains("@")){
							email.addTo(merchManagerEmail.get(i), "Gerente de Merchandising");
						}				
					}
					
					String status = email.send();
				
					log.info("Email Send status: " + status);
					
					if (email.isSentOK()){}
					else
						return "Error al envíar email";
				}
				else
					return "Error al envíar email";
				
			}
		}catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return "Error al ejecutar el proceso";
		}finally{
			try {rsa.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmta.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return "Envíado Email de Reportes de Novedad Pendientes";
	}

	 
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}
	
	private Integer getAD_User_ID(Integer CBPartner)
	{
		Integer AD_User_ID=0;
		
		String SQL = "Select AD_USER_ID FROM AD_USER " +
					 "WHERE C_BPartner_ID IN "+CBPartner + " "+
					 "AND ISACTIVE='Y'";
		
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
				 
			if(rs.next())
			{
				AD_User_ID = rs.getInt("AD_USER_ID");
			}
			
			rs.close();
			pstmt.close();
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
			}
		
		return AD_User_ID;
	}
	
	private Vector<String> getMerchManagerEmail(){
		
		Vector<String> emails = new Vector<String>();
		
		String sqlEmails = "select email,c.name from ad_user a, AD_User_Roles b, AD_Role c " +
						   "where a.ad_user_id = b.ad_user_id and b.ISACTIVE = 'Y' " +
                           "and b.ad_role_id=c.ad_role_id and " +
                           "b.AD_ROLE_ID = 1000098 " +
                           "order by b.AD_ROLE_ID";

		ResultSet rs_Email = null;
		PreparedStatement prst_Email = null;
		
		try {
		
			prst_Email = DB.prepareStatement(sqlEmails, null);
			rs_Email = prst_Email.executeQuery();
			
			while(rs_Email.next()){
				
				emails.add(rs_Email.getString(1));
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


