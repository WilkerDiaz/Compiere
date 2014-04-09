package compiere.model.cds.processes;
 
import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.logging.Level;
import org.compiere.apps.ADialog;
import org.compiere.model.MCampaign;
import org.compiere.model.X_AD_Role;
import org.compiere.model.X_AD_User;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MBPartner;
import compiere.model.cds.MOrder;
import compiere.model.cds.MVMRCategory;
import compiere.model.cds.MVMRDepartment;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VMR_Collection;

public class XX_OrderNotReady extends SvrProcess {

	private String reason = "";
	private String vendor = "";
	private String department = "";
	private String collection = "";
	
	@Override
	protected String doIt() throws Exception {

		X_AD_User user = new X_AD_User(getCtx(), getCtx().getAD_User_ID(), null);
		
		Calendar c = Calendar.getInstance();
		String day = Integer.toString(c.get(Calendar.DATE));
		Integer month = c.get(Calendar.MONTH)+1;
		DecimalFormat format = new DecimalFormat("00");
		String year = Integer.toString(c.get(Calendar.YEAR));
		String date = day+"/"+format.format(month)+"/"+year;
		
		MOrder order = new MOrder(getCtx(), getRecord_ID(), get_Trx());
		String allReasons = order.getXX_OrderNotReadyReason();
		
		if(allReasons.length()==0){
			allReasons += "("+user.getName()+" - "+ date+"): "+reason;
			order.setXX_OrderNotReadyReason(allReasons);
		}
		else if((allReasons.length()+reason.length())>500){
			allReasons = "+...\n"+"("+user.getName()+" - "+ date+"): "+reason;
			order.setXX_OrderNotReadyReason(allReasons);
		}else{
			allReasons += "\n"+"("+user.getName()+" - "+ date+"): "+reason;
			order.setXX_OrderNotReadyReason(allReasons);	
		}
		
		//Cargamos el Proveedor, Departamento y la coleccion
		MBPartner vendorObj = new MBPartner( getCtx(), order.getC_BPartner_ID(), null);
		vendor = vendorObj.getName();
		
		MVMRDepartment departmentObj = new MVMRDepartment( getCtx(), order.getXX_VMR_DEPARTMENT_ID(), null);
		department = departmentObj.getValue();
		
		X_XX_VMR_Collection collectionObj = new X_XX_VMR_Collection( getCtx(), order.getXX_Collection_ID(), null);
		collection = collectionObj.getName();
		
		X_AD_Role role = new X_AD_Role(getCtx(), getCtx().getAD_Role_ID(), null);
		//Si soy planificador solo le envio correo al comprador (y jefe de categoria)
		if(getCtx().getAD_Role_ID()==getCtx().getContextAsInt("#XX_L_ROLESCHEDULER_ID")){
			
			ADialog.info(1, new Container(), "XX_OrderIsNotReady");
			order.setXX_OrderReadyStatus(false);
			order.setXX_OrderNotReady("Y");
			
			if(order.getXX_NotReadyCM_ID()!=0){
				order.setXX_NotReadyCM_ID(0);
			}
			order.setXX_FirstAppManager_ID(0);
			sendMailToBuyer();
			sendMailToCategoryManager();
		}
		//si soy jefe de planificacion le envio a planificador y comprador 
		else if(getCtx().getAD_Role_ID()==getCtx().getContextAsInt("#XX_L_ROLESCHEDULEMANAGER_ID") || role.getName().equalsIgnoreCase("BECO Jefe de Categoria")){
			
			ADialog.info(1, new Container(), "XX_OrderIsNotReady");
			order.setXX_OrderReadyStatus(false);
			order.setXX_OrderNotReady("Y");
			
			if(order.getXX_NotReadyCM_ID()!=0){
				order.setXX_NotReadyCM_ID(0);
			}
			order.setXX_FirstAppManager_ID(0);
			sendMailToScheduler();
			sendMailToBuyer();
		}		
		//si soy gerente le envio correos a jefes de categoria, jefe de planificacion, planificador y comprador
		else if (role.getName().equalsIgnoreCase("Beco AdminRole") || role.getName().equalsIgnoreCase("BECO Gerente Merchandising") || role.getName().equalsIgnoreCase("BECO Gerente Logistica") || role.getName().equalsIgnoreCase("BECO Gerente General")){
			
			ADialog.info(1, new Container(), "XX_OrderIsNotReady");
			order.setXX_OrderReadyStatus(false);
			order.setXX_OrderNotReady("Y");
			
			if(order.getXX_NotReadyCM_ID()!=0){
				order.setXX_NotReadyCM_ID(0);
			}
			order.setXX_FirstAppManager_ID(0);
			sendMailToSchedulerManager();
			sendMailToScheduler();
			sendMailToBuyer();
			sendMailToCategoryManager();
		}
		//Si soy gerente comercial
		else if (role.getName().equalsIgnoreCase("Beco AdminRole") || role.getName().equalsIgnoreCase("BECO Gerente Ventas") || role.getName().equalsIgnoreCase("BECO Gerente Mercadeo")){
			
			if(order.getXX_NotReadyCM_ID()==0){
				
				order.setXX_NotReadyCM_ID(role.get_ID());
			}
			else if(order.getXX_NotReadyCM_ID()==role.get_ID()){
				ADialog.info(1, new Container(), "Necesita el rechazo de otro gerente");
			}
			else
			{
				ADialog.info(1, new Container(), "XX_OrderIsNotReady");
				order.setXX_OrderReadyStatus(false);
				order.setXX_OrderNotReady("Y");
				order.setXX_NotReadyCM_ID(0);
				order.setXX_FirstAppManager_ID(0);
				sendMailToSchedulerManager();
				sendMailToScheduler();
				sendMailToBuyer();
				sendMailToCategoryManager();
			}
		}
		
		order.save();
		
		return "";
	}

	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] parameter = getParameter();
		for (ProcessInfoParameter element : parameter) {
			
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("XX_OrderNotReadyReason"))
				reason = element.getParameter().toString();
			else 
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			
		}
	}
	
	private void sendMailToCategoryManager()
	{
		
		MOrder order = new MOrder(Env.getCtx(), getRecord_ID(), null);
		
		MVMRCategory category = new MVMRCategory(Env.getCtx(), order.getXX_Category_ID(), null);
		
		Integer categoryManager = getAD_User_ID(category.getXX_CategoryManager_ID());
		
		String message = "<"+order.getDocumentNo()+">, "
		+Msg.getMsg(Env.getCtx(), "Following Reason", new String[] {vendor,department,collection})
		+"\n\n"+reason;
		
		Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_DENIEDAPPROVAL_ID"), message, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, categoryManager,null);

		try {
			f.ejecutarMail();  
		} catch (Exception e) {
			e.printStackTrace();
		}
		f = null;
	}
	
	private void sendMailToSchedulerManager()
	{
		MOrder order = new MOrder(getCtx(),getRecord_ID(),null);
		
		String sql = "SELECT C_BPartner_ID FROM C_BPartner " +
					 "WHERE ISACTIVE='Y' " +
					 "AND C_JOB_ID = " + getCtx().getContext("#XX_L_JOBPOSITION_PLANMAN_ID");

		try{
			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				Integer SchedulerManager = rs.getInt("C_BPartner_ID");
				// A partir de aca enviamos el correo
				Integer UserAuxID = getAD_User_ID(SchedulerManager);
			
				String message = "<"+order.getDocumentNo()+">, "
				+Msg.getMsg(Env.getCtx(), "Following Reason", new String[] {vendor,department,collection})
				+"\n\n"+reason;
				
				Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_DENIEDAPPROVAL_ID"), message, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, null);
				f.ejecutarMail();
				f = null;

			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
	}
	
	private void sendMailToScheduler()
	{
		// Primero buscamos quien es el planificador
		MOrder order = new MOrder(getCtx(),getRecord_ID(),null);
		
		String sql = "SELECT XX_InventorySchedule_ID " +
					 "FROM XX_VMR_Department " +
					 "WHERE XX_VMR_DEPARTMENT_ID=" + order.getXX_VMR_DEPARTMENT_ID();

		try{
			
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next())
			{
				Integer planificador = rs.getInt("XX_InventorySchedule_ID");
				// A partir de aca enviamos el correo
				Integer UserAuxID = getAD_User_ID(planificador);
			
				String message = "<"+order.getDocumentNo()+">, "
				+Msg.getMsg(Env.getCtx(), "Following Reason", new String[] {vendor,department,collection})
				+"\n\n"+reason;

				Utilities f = new Utilities(getCtx(), null,getCtx().getContextAsInt("#XX_L_MT_DENIEDAPPROVAL_ID"), message, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID") , -1, UserAuxID, null);
				f.ejecutarMail();
				f = null;

			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
	}
	
	private void sendMailToBuyer()
	{
		MOrder order = new MOrder(Env.getCtx(), getRecord_ID(), null);
		
		Integer userBuyer = getAD_User_ID(order.getXX_UserBuyer_ID());
		
		String message = "<"+order.getDocumentNo()+">, "
		+Msg.getMsg(Env.getCtx(), "Following Reason", new String[] {vendor,department,collection})
		+"\n\n"+reason;
		
		Utilities f = new Utilities(Env.getCtx(), null, getCtx().getContextAsInt("#XX_L_MT_DENIEDAPPROVAL_ID"), message, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, userBuyer,null);
		try {
			f.ejecutarMail();
		} catch (Exception e) {
			e.printStackTrace();
		}
		f = null;
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
}
