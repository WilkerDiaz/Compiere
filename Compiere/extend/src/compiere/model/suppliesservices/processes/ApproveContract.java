package compiere.model.suppliesservices.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.apps.ADialog;
import org.compiere.apps.ProcessCtl;
import org.compiere.model.MPInstance;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.cds.MProduct;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_ProductName;
import compiere.model.suppliesservices.X_Ref_XX_Status_LV;
import compiere.model.suppliesservices.X_XX_Contract;
import compiere.model.suppliesservices.X_XX_VCN_ContractInvoice;

public class ApproveContract extends SvrProcess{
	/** Purchase of Supplies and Services 
	 * Maria Vintimilla Funcion 33
	 * Approve a contract. 
	 * Send mail to legal **/
	Integer Legal_ID = 0;
	Integer Advisor_ID = 0;
	Integer Contract_ID = 0;
	Integer Role = 0;
	Integer Sales_ID = 0;
	Integer Merchandising_ID = 0;
	Integer Personal_ID = 0;
	Integer Logistic_ID = 0;
	Integer System_ID = 0;
	Integer Control_ID = 0;
	Integer Merchan_ID = 0;
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("XX_Contract_ID")) 
				Contract_ID = element.getParameterAsInt();
		}
		if(Contract_ID.equals(0)){
			Contract_ID = getRecord_ID();
		}
	}

	@Override
	protected String doIt() throws Exception {
		
		Calendar cal=Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
       	int month = cal.get(Calendar.MONTH);
       	int day = cal.get(Calendar.DATE);
      	int hour = cal.get(Calendar.HOUR);
       	int min = cal.get(Calendar.MINUTE);
       	int sec = cal.get(Calendar.SECOND);
       	int nano = cal.get(Calendar.SECOND);
       	Boolean bool = false;
       	Timestamp today = new Timestamp(year-1900,month,day,hour,min,sec,nano);
		String Message = "";
		try{
			X_XX_Contract contrato= 
				new X_XX_Contract(Env.getCtx(),Contract_ID,null);
			Contract_ID = contrato.get_ID();
			
			// Se verifica que el contrato esté listo para poder proceder con 
			// la aprobacion del contrato
			if(contrato.isXX_IsContractReady()){
				Role = Env.getCtx().getContextAsInt("#AD_Role_ID"); 
				Legal_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLELEGAL_ID"));
				Advisor_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLELEGALADVISOR_ID"));
				Sales_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLESALES_ID"));
				Merchandising_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLEMERCHANDISING_ID"));
				Personal_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLEPERSONAL_ID"));
				Logistic_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLELOGISTIC_ID"));
				System_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLESYSTEM_ID"));
				Control_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLECONTROL_ID"));
				Merchan_ID = Integer.parseInt(Env.getCtx().getContext("#XX_L_ROLEMERCHAN_ID"));
				
				// Contrato se intenta aprobar con rol de legal
				if(Role.compareTo(Advisor_ID) == 0){
					
					// Verificación de estados del contrato para aprobar 
					// (Revisión o Aprobado sin firma)
					if(contrato.getXX_Status().equals(X_Ref_XX_Status_LV.REVIEW.getValue()) ||
							contrato.getXX_Status().equals(X_Ref_XX_Status_LV.UNSIGNED_APPROVED.getValue())){

						contrato.setXX_Status("C");
						contrato.setXX_ContractApproval(today);
						contrato.setXX_UserContractApproval_ID(Env.getCtx().getContextAsInt("##AD_User_ID"));
						
						//PRODUCTO POR PESTAÑA DE FACTURACION
						bool = setProducts(contrato);
						
						if(bool)
							commit();
						else{	
							rollback();
							return "Error al generar los productos";
						}
						
						/****Se registra el contrato en las Cuentas por Pagar Estimadas****/				
						try{
							MPInstance mpi = new MPInstance(Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_ESTIMATEDCONTRACT_ID"), 0); 
							mpi.save();
							ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_ESTIMATEDCONTRACT_ID")); 
							pi.setRecord_ID(mpi.getRecord_ID());
							pi.setAD_PInstance_ID(mpi.get_ID());
							pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_ESTIMATEDCONTRACT_ID")); 
							pi.setClassName(""); 
							pi.setTitle(""); 
							ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
							list.add (new ProcessInfoParameter("XX_Contract_ID", String.valueOf(Contract_ID), null, String.valueOf(Contract_ID), null));
							ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
							list.toArray(pars);
							pi.setParameter(pars);
							ProcessCtl pc = new ProcessCtl(null ,pi,null); 
							pc.start();
							bool = true;
						}catch(Exception e){
							log.log(Level.SEVERE,e.getMessage());
						}
						/****Fin código - Jessica Mendoza****/
					}//Status
					else{
						log.saveError("Error", Msg.translate(Env.getCtx(), "XX_ApproveContract"));
						ADialog.error(1, new Container(), Msg.translate(Env.getCtx(), "XX_ApproveContract"));
					}
				}// Fin Legal
				
				// Contrato a ser aprobado por gerentes
				else if((Role.compareTo(Sales_ID) == 0 || Role.compareTo(Merchandising_ID) == 0 || 
						Role.compareTo(Personal_ID) == 0 || Role.compareTo(Logistic_ID) == 0 || 
						Role.compareTo(System_ID) == 0 || Role.compareTo(Control_ID) == 0 || 
						Role.compareTo(Control_ID) == 0 || Role.compareTo(Merchan_ID) == 0)){
					
					if(contrato.getXX_Status().equals(X_Ref_XX_Status_LV.REVIEW.getValue())){ 
						contrato.setXX_Status("V");
						contrato.setXX_ContractApproval(today);
						sendMailAP(Message, Legal_ID, Contract_ID);
						sendMailAP(Message, Advisor_ID, Contract_ID);
						bool = true;
					}//status
					else{
						log.saveError("Error", Msg.translate(Env.getCtx(), "XX_ApproveContract"));
						ADialog.error(1, new Container(), Msg.translate(Env.getCtx(), "XX_ApproveContract"));
					}					
				}//Fin Managers
				
				// Contrato se intenta aprobar sin rol permitido
				else { 
					log.saveError("Error", Msg.translate(Env.getCtx(), "XX_ApprovalDenied"));
					ADialog.error(1, new Container(), Msg.translate(Env.getCtx(), "XX_ApprovalDenied"));
				}

				if (bool)
					contrato.save();
			} // Fin contrato listo
			else {
				log.saveError("Error", Msg.translate(Env.getCtx(), "XX_ContractReadyApproval"));
				ADialog.error(1, new Container(), Msg.translate(Env.getCtx(), "XX_ContractReadyApproval"));
			}

			return "";
		}//Try
		catch(Exception e){
			return e.getMessage();
		}
	}// fin doIt

	/** sendMailAP
	 * @param message E-mail's text
	 * @param user_ID Recipient User
	 * @param Contract_ID Contract Code
	 *  */
	private void sendMailAP(String message, Integer user_ID, Integer Contract_ID){
		message = Msg.getMsg( Env.getCtx(), "XX_ContractApproval", 
				new String[]{Contract_ID.toString()});

		String sql = " SELECT AD_USER_ID " +
					" FROM AD_User_Roles" +
					" WHERE IsActive = 'Y' " +
					" AND AD_ROLE_ID = "+ user_ID +
					" AND AD_USER_ID <> 100 ";
		//System.out.println("sql role: "+sql);

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Utilities f = new Utilities(Env.getCtx(), null,
						Env.getCtx().getContextAsInt("#XX_L_MT_APPROVEC_ID"), message, -1, 
						Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, 
						rs.getInt("AD_USER_ID"), null);
				try { f.ejecutarMail(); } 
				catch (Exception e) { e.printStackTrace(); }
				f = null;
			}
		}//try
		catch (Exception e){
			log.saveError("ErrorSql Aprobar contrato", Msg.getMsg(Env.getCtx(), e.getMessage()));
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}//finally
		
	}// Fin sendMailAP
	
	/**
	 * Busca si existe el nombre de la descripcion del contrato en la tabla Product
	 * @author Jessica Mendoza
	 * @param name nombre del producto
	 * @return retorna el identificador del producto y su cuenta asociada
	 */
	private Vector<Integer> searchProduct(String name){
		Vector<Integer> infoProduct = new Vector<Integer>(2);
 		/*String sql = "select max(pro.M_Product_ID), max(pac.XX_PExpenses_ID) " +
					 "from M_Product pro, M_Product_Acct pac " +
					 "where pro.XX_ProductName_ID = " +
					 	"(select max(XX_ProductName_ID) " +
					 	"from XX_ProductName " +
					 	"where upper(XX_ProductName) like upper('" + name + "%')) " +
					 "and pro.M_Product_ID = pac.M_Product_ID " +
					 "or pro.name like '" + name + "%'";*/

 		String sql = "select pro.M_Product_ID, pac.XX_PExpenses_ID " +
 					 "from M_Product pro, M_Product_Acct pac " +
 					 "where pro.M_Product_ID = pac.M_Product_ID " +
 					 "and UPPER(TRIM(pro.name)) = UPPER(TRIM('" + name + "'))" +
 					 " AND pro.AD_Client_ID = " + 
					Env.getCtx().getAD_Client_ID();
 		
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				infoProduct.add(rs.getInt(1));
				infoProduct.add(rs.getInt(2));
			}else{
				infoProduct.add(0);
				infoProduct.add(0);
			}
		}catch (Exception e){
			log.log(Level.SEVERE, sql);
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return infoProduct;
	}
	
	/**
	 * Busca si existe el nombre de la descripcion del contrato en la tabla ProductName
	 * @author Jessica Mendoza
	 * @param name nombre del producto
	 * @return
	 */
	private int searchProductName(String name){
		int idProductName = 0;
		String sql = "select XX_ProductName_ID " +
					 "from XX_ProductName " +
					 "where upper(TRIM(name)) = upper(TRIM('" + name + "')) "+
					 " AND AD_Client_ID = " + 
						Env.getCtx().getAD_Client_ID() ;
		
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				idProductName = rs.getInt(1);
			}else{
				idProductName = 0;
			}
		}catch (Exception e){
			log.log(Level.SEVERE, sql);
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return idProductName;
	}
	
	/**
	 * Se encarga de crear el producto
	 * @author Jessica Mendoza
	 * @param name nombre del producto
	 * @param idPartner identificador del socio de negocio
	 * @param idElementValue identificador de la cuenta contable
	 * @param idCurrency identificador de la moneda
	 * @return identificador del producto creado
	 */
	private int createProduct(String name, int idPartner, int idElementValue, int idCurrency){		
		String update = "";
		int idProductName = searchProductName(name);
		if (idProductName == 0){
			//Crea el nombre del producto
			X_XX_ProductName productName = new X_XX_ProductName(Env.getCtx(),0,null);				
			productName.setName(name);
			productName.setDescription(name);
			//productName.setXX_ProductName(name);
			productName.save();
			idProductName = productName.getXX_ProductName_ID();
		}
		
		//Crea el producto
		MProduct product = new MProduct(Env.getCtx(), 0, get_Trx());
		product.setProductType("S");
		product.setName(name);
		product.setXX_ProductName_ID(idProductName);
		product.save();
		
		//Asigna al producto un proveedor
		/*MProductPO productPo = new MProductPO(Env.getCtx(), 0, get_Trx());
		productPo.setC_BPartner_ID(idPartner);
		productPo.setM_Product_ID(product.getM_Product_ID());
		productPo.setC_Currency_ID(idCurrency);
		productPo.save();*/
		update = "update M_Product_PO " +
				 "set C_BPartner_ID = " + idPartner + " " +
				 "where M_Product_ID = " + product.getM_Product_ID();
		try {
			DB.executeUpdateEx(update, get_Trx());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Asigna al producto una cuenta contable
		update = "update M_Product_Acct " + 
				 "set XX_PExpenses_ID = " + idElementValue + " " +
				 "where M_Product_ID = " + product.getM_Product_ID();
		try {
			DB.executeUpdateEx(update, get_Trx());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return product.getM_Product_ID();
	}
	
	/**
	 * Busca el nombre de la gerencia responsable
	 * @author Jessica Mendoza
	 * @param idOrg
	 * @return
	 */
	private String managementName(int idOrg){
		String name = "";
		String sql = "select name from AD_Org where AD_Org_ID = " + idOrg +
					" AND AD_Client_ID = " + 
					Env.getCtx().getAD_Client_ID();
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				name = rs.getString("name");
			}
		}catch (Exception e){
			log.log(Level.SEVERE, sql);
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return name;
	}
	
	private boolean setProducts(X_XX_Contract contrato){
		
		String invoiceLines = "SELECT XX_VCN_ContractInvoice_ID " +
							  "FROM XX_VCN_ContractInvoice " +
							  "WHERE XX_CONTRACT_ID = " + Contract_ID;
		
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		X_XX_VCN_ContractInvoice contractInv = null;
		int i = 0;
		
		try{
			
			pstmt = DB.prepareStatement(invoiceLines, null); 
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				i++;
				contractInv = new X_XX_VCN_ContractInvoice( Env.getCtx(), rs.getInt(1), get_Trx());
				
				//Busca si existe un producto con esa descripcion
				Vector<Integer> infoProduct = searchProduct(contractInv.getDescription());
				String nombreProducto = "";
				
				if (contractInv.getXX_PExpenses_ID() == 0){
		
					ADialog.error(0, new Container(), Msg.getMsg(Env.getCtx(), "XX_NotElementValue"));
					return false;
				}
				else{
					if (infoProduct.get(0) != 0){ //El producto existe
						if (infoProduct.get(1) == contractInv.getXX_PExpenses_ID())
							contractInv.setM_Product_ID(infoProduct.get(0));
						else{
							nombreProducto = contractInv.getDescription() + " (" + managementName(contrato.getXX_Management_ID()) + ")";
							contractInv.setM_Product_ID(createProduct(nombreProducto, contractInv.getC_BPartner_ID(),
									contractInv.getXX_PExpenses_ID(), contrato.getC_Currency_ID()));
						}
					}else{
						nombreProducto = contractInv.getDescription();
						contractInv.setM_Product_ID(createProduct(nombreProducto,contractInv.getC_BPartner_ID(),
								contractInv.getXX_PExpenses_ID(), contrato.getC_Currency_ID()));							
					}
				}
				
				contractInv.save();
			}
			
		}catch (Exception e){
			log.log(Level.SEVERE, invoiceLines);
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		if(i==0)
			return false;
		
		return true;
	}
	
}//Fin ApproveContract
