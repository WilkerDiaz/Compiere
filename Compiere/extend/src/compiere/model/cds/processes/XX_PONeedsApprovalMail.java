package compiere.model.cds.processes;
 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import compiere.model.cds.MOrder;
import compiere.model.cds.MVMRCategory;
import compiere.model.cds.Utilities;

public class XX_PONeedsApprovalMail extends SvrProcess {

	private Vector<Integer> orders = new Vector<Integer>();
	
	@Override
	protected String doIt() throws Exception {
		
		orders = getOrders();
		
		//Envia un correo a cada Jefe de Departamento que tenga una orden de compra por aprobar
		sendMailImportedToCategoryManager();
		
		//Envia un correo a cada comprador que tenga una orden de compra por aprobar
		sendMailImportedToBuyer();
		
		return null;
	}

	@Override
	protected void prepare() {

	}

	/*	
	 * Envia correo al Jefe de Categoria
	 */
	private void sendMailImportedToCategoryManager()
	{

		for(int i=0; i<orders.size(); i++){
		
			MOrder order = new MOrder(Env.getCtx(), new Integer(orders.get(i)), null);
			
			MVMRCategory category = new MVMRCategory(Env.getCtx(), order.getXX_Category_ID(), null);
			
			Integer categoryManager = getAD_User_ID(category.getXX_CategoryManager_ID());
			
			String Mensaje = " <"+order.getDocumentNo()+"> ha sido autorizada y debe ser aprobada para poder continuar con el proceso de recepción.";
			
			Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_AUTHORIZEDORDER_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, categoryManager,null);
			try {
				f.ejecutarMail(); 
			} catch (Exception e) {
				e.printStackTrace();
			}
			f = null;
		}
	}
	
	/*	
	 * Envia correo al comprador
	 */
	private void sendMailImportedToBuyer()
	{

		for(int i=0; i<orders.size(); i++){
		
			MOrder order = new MOrder(Env.getCtx(), new Integer(orders.get(i)), null);
			
			Integer userBuyer = getAD_User_ID(order.getXX_UserBuyer_ID());
			
			String Mensaje = " <"+order.getDocumentNo()+"> ha sido autorizada y debe ser aprobada para poder continuar con el proceso de recepción.";
			
			Utilities f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_AUTHORIZEDORDER_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, userBuyer,null);
			try {
				f.ejecutarMail();
			} catch (Exception e) {
				e.printStackTrace();
			}
			f = null;
		}
	}
	
	/*
	 * Obtiene el AD_USER_ID del CBParter Indicado
	 */
	private Integer getAD_User_ID(Integer CBPartner)
	{
		Integer AD_User_ID=0;
		
		String SQL = "Select AD_USER_ID FROM AD_USER " +
					 "WHERE C_BPartner_ID IN "+CBPartner;
		
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
	
	
	/*
	 * Obtiene el AD_USER_ID del CBParter Indicado
	 */
	private Vector<Integer> getOrders()
	{
		Vector<Integer> orders = new Vector<Integer>();
		
		String SQL = "SELECT C_ORDER_ID " +
					 "FROM C_ORDER " +
					 "WHERE XX_ORDERTYPE ='Importada' " + 
					 "AND XX_AUTHORIZED='Y' " +
					 "AND XX_ORDERSTATUS!='AP' " +
					 "AND XX_ORDERSTATUS!='RE' " +
					 "AND XX_ORDERSTATUS!='CH'";
		
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
				 
			if(rs.next())
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
