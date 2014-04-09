package compiere.model.cds.processes;
 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import compiere.model.cds.MOrder;
import compiere.model.cds.Utilities;

public class XX_SendInvoiceStoreReceipt extends SvrProcess {

	@Override
	protected String doIt() throws Exception {
		
		sendMailAdminManagers();
		
		return "";
	}

	@Override
	protected void prepare() {
	}

	private void sendMailAdminManagers()
	{
		//obtengo las ordenes que no tienen factura registrada y que ya fueron recibidas en tienda
		Vector<Integer> orders = getOrders();
		
		//A todas las O/C les busco el Administrador de Tienda y les mando el correo
		Integer adminManagerUser = 0;
		
		for( int i=0; i<orders.size(); i++){
		
			MOrder order = new MOrder(Env.getCtx(), new Integer(orders.get(i)), null);
					
			Integer adminManagerBP = getAdminManager(order);
			adminManagerUser = getAD_User_ID(adminManagerBP);
			
			String Mensaje = " <"+order.getDocumentNo()+"> fue recibida, debe hacer llegar la factura original a Boleíta.";
			
			if(adminManagerUser!=0){
		
				Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_SENDINVOICE_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, adminManagerUser,null);
		
				try {
					f.ejecutarMail();   
				} catch (Exception e) {
					e.printStackTrace();
				}
				f = null;
			}
		}
	}
	
	private Integer getAD_User_ID(Integer CBPartner)
	{
		Integer AD_User_ID=0;
		
		String SQL = "Select AD_USER_ID FROM AD_USER " +
					 "WHERE C_BPartner_ID IN "+CBPartner + " " +
					 "AND ISACTIVE='Y' " +
					 "AND AD_CLIENT_ID IN (0,"+getAD_Client_ID()+")";
		
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
	
	private Integer getAdminManager(MOrder order)
	{
		Integer adminManagerID=0;
		
		String SQL = "Select C_BPartner_ID FROM C_BPartner " +
					 "WHERE AD_ORG_ID=" + order.getAD_Org_ID() + " "+
					 "AND C_JOB_ID =" + Env.getCtx().getContextAsInt("#XX_L_JOBPOSITION_STOREMAN_ID")+ " "+
					 "AND ISACTIVE='Y' " +
					 "AND AD_CLIENT_ID IN (0,"+getAD_Client_ID()+")";
		
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
				 
			if(rs.next())
			{
				adminManagerID = rs.getInt("C_BPartner_ID");
			}
			
			rs.close();
			pstmt.close();
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
			}
		
		return adminManagerID;
	}

	private Vector<Integer> getOrders(){
		
		Vector<Integer> orders = new Vector<Integer>();
		
		String SQL = "SELECT io.C_ORDER_ID " +
					 "FROM M_INOUT io, C_ORDER po " +
					 "WHERE io.DOCSTATUS='CO' " +
					 "AND po.XX_VLO_TYPEDELIVERY = 'DD' " +
					 "AND po.C_ORDER_ID=io.C_ORDER_ID " +
					 "AND po.AD_ORG_ID<>" + getCtx().getContextAsInt("#XX_L_ORGANIZATIONCD_ID") +" "+ 
					 "AND io.C_ORDER_ID NOT IN (SELECT C_ORDER_ID FROM C_INVOICE WHERE ISACTIVE='Y' AND C_ORDER_ID IS NOT NULL) " +
					 "AND io.ISACTIVE='Y'";
		
		try{
		
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
				 
			while(rs.next())
			{
				orders.add(rs.getInt("C_ORDER_ID"));
			}
			
			rs.close();
			pstmt.close();
		  
		}catch (Exception e) {
			log.log(Level.SEVERE, SQL);
		}
				
		return orders;
	}
}
