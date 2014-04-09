package compiere.model.payments.processes;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

import org.compiere.framework.PrintInfo;
import org.compiere.framework.Query;
import org.compiere.model.MClient;
import org.compiere.model.X_AD_Client;
import org.compiere.model.X_C_BP_BankAccount;
import org.compiere.model.X_C_Bank;
import org.compiere.model.X_C_BankAccount;
import org.compiere.model.X_C_PaySelection;
import org.compiere.model.X_C_Payment;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.birt.BIRTReport;
import compiere.model.cds.MBPartner;
import compiere.model.cds.MUser;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VCN_PurchasesBook;

/** Envía Email a Proveedor con la documentación relacionada a una orden de pago
 * que ya fue pagada a través de una transferencia bancaria
 * @author mvalero
 */

public class XX_SendMailPayDoc extends SvrProcess {
	X_C_PaySelection payS;
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();

		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null) {
				;
			} else if (name.equals("XX_PaySelection")) {
				payS = new X_C_PaySelection(getCtx(), element.getParameterAsInt(), get_Trx());
			}
		}
		
	}

	@Override
	protected String doIt() throws Exception {
		return sendEmailtoVendor(payS);
	}

	/**
	 * 
	 * Este método crea los documentos en formato
	 * PDF y los envía al proveedor 
	 * 
	 */
	public String sendEmailtoVendor(X_C_PaySelection payselection){

		String subject = "";
		String msg = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		X_C_Payment pay = new X_C_Payment(Env.getCtx(), idPayment(payselection.getC_PaySelection_ID()), null);		
		
		X_C_BP_BankAccount bpAccount = new X_C_BP_BankAccount(Env.getCtx(), pay.getC_BP_BankAccount_ID(), get_Trx());
		
		X_C_Bank bpBank = new X_C_Bank(Env.getCtx(), bpAccount.getC_Bank_ID(), get_Trx());
		
		X_AD_Client client = new X_AD_Client(Env.getCtx(), pay.getAD_Client_ID(), get_Trx());
		
		subject = Msg.getMsg(Env.getCtx(), "XX_SentPayDocSubject");
		msg =  Msg.getMsg(Env.getCtx(), "XX_SentPayDoc", new String[] { sdf.format(pay.get_Value("XX_DateFinalPay")), pay.getCheckNo(), bpBank.getName(), bpAccount.getAccountNo() , pay.getPayAmt().toString(), client.getName()});
		
			int C_PaySelection_ID = payselection.getC_PaySelection_ID();
			log.info("C_PaySelection_ID" + C_PaySelection_ID);
			if (C_PaySelection_ID < 1)
				throw new IllegalArgumentException("@NotFound@ @C_PaySelection_ID@");
			
			MBPartner vendorAux = new MBPartner(Env.getCtx(), pay.getC_BPartner_ID(), null);
			String toVendor = vendorAux.getXX_VendorEmail();	
			MClient m_client = MClient.get(Env.getCtx());
			
			MUser userAccount = null;
			String sql = " SELECT AD_USER_ID " +
			" FROM AD_User_Roles" +
			" WHERE IsActive = 'Y' " +
			" AND AD_ROLE_ID = "+ Env.getCtx().getContextAsInt("#XX_L_ROLE_ACCOUNTTOPAYCOORD_ID") +
			" AND AD_USER_ID <> 100 ";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				if(rs.next()){
					userAccount = new MUser(Env.getCtx(), rs.getInt("AD_USER_ID"), null);
				}
			}//try
			catch (Exception e){
				log.saveError("ErrorSql Fecha Expiracion RIF Proveedor", Msg.getMsg(Env.getCtx(), e.getMessage()));
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}//finally
			
			String account = userAccount.getEMail();
		
			EMail email = m_client.createEMail(null, toVendor , vendorAux.getName(), subject, msg); //
			
			
			if (email != null){
				
				if(!account.isEmpty()){
					email.addTo(account, userAccount.getName());
					email.addTo("ccapote@beco.com.ve", "CCapote");					
				}		
			
			//Query para obtener los Comprobantes de retención IVA que se deben incluir
			sql = "SELECT pb.AD_Client_ID, pb.AD_Org_ID, pb.C_ORDER_ID, pb.NAME, pb.C_BPARTNER_ID, CASE WHEN inv.XX_InvoiceType = 'S' THEN 'SERVICIO' WHEN inv.XX_InvoiceType = 'A' THEN 'BIENES' " 
						+ " WHEN inv.XX_InvoiceType = 'I' THEN 'PRODUCTOS PARA LA VENTA' ELSE '' END INVTYPE, pb.XX_WITHHOLDING, TO_CHAR(pb.XX_DATE,'DD-MM-YYYY') XX_DATE, pb.XX_NOCOMPROBANTE, pb.XX_MonthYear,"
						+ " ROUND(sum(pb.XX_TotalInvCost),2) XX_TotalInvCost, ROUND(sum(pb.XX_TaxableBase),2) XX_TaxableBase, ROUND(sum(pb.XX_TaxAmount),2) XX_TaxAmount, ROUND(sum(pb.XX_withholdingTax),2) XX_withholdingTax, MAX(pb.XX_VCN_PURCHASESBOOK_ID) XX_VCN_PURCHASESBOOK_ID" 
						+ " FROM XX_PurchaseFormView pb, C_INVOICE inv "
						+ " WHERE pb.XX_WITHHOLDING IS NOT NULL AND pb.C_INVOICE_ID = inv.C_INVOICE_ID AND pb.XX_WITHHOLDING != 0  AND pb.AD_Client_ID IN (0,1000012) "
						+ " and inv.C_Invoice_ID in "

						+ " (SELECT PSL.C_Invoice_ID FROM C_PAYSELECTIONLINE  PSL WHERE PSL.C_PAYSELECTION_ID = " + C_PaySelection_ID + ")"
						+ " GROUP BY pb.AD_Client_ID, pb.AD_Org_ID, pb.C_ORDER_ID, pb.NAME, pb.C_BPARTNER_ID, inv.XX_InvoiceType, pb.XX_WITHHOLDING, pb.XX_DATE, pb.XX_NOCOMPROBANTE, pb.XX_MonthYear"  
						+ " ORDER BY pb.XX_WITHHOLDING";
			
			pstmt = DB.prepareStatement(sql, null); 
			
		    try{
				rs = pstmt.executeQuery();			
			    
				while (rs.next()){	
					

					/*Comprobante IVA*/					
					String designISLR = "IVAReceipt";
					
					//Intanciar reporte e iniciar plataforma
					BIRTReport myReportIVA = new BIRTReport();
					
					//Agregar parametro
					myReportIVA.parameterName.add("PurchasesBookID");
					myReportIVA.parameterValue.add(rs.getInt("XX_VCN_PurchasesBook_ID"));
					
					
					try
					{
						//Adjuntar comprobante ISLR
						email.addAttachment(myReportIVA.fileReport(designISLR, "pdf"));
						
					}
					catch (Exception e)
					{
						log.log(Level.SEVERE, "", e);
					}
									
				}
			}
			catch (SQLException e){
				e.printStackTrace();
			} finally{			
				try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
				try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			}
			
			
			//Query para obtener los Comprobantes de retención ISLR que se deben incluir
			sql = "select distinct c_invoice_id from XX_VCN_ISLRAmount " 
				+ " where c_invoice_id in (select C_invoice_id from c_payselectionline " 
				+ " where c_payselection_id = '" + C_PaySelection_ID + "')";
			
			pstmt = DB.prepareStatement(sql, null); 
			
		    try{
				rs = pstmt.executeQuery();			
			    
				while (rs.next()){
					/*Comprobante ISLR*/					
					String designISLR = "ISLRReceipt";
					
					//Intanciar reporte e iniciar plataforma
					BIRTReport myReportISLR = new BIRTReport();
					
					//Agregar parametro
					myReportISLR.parameterName.add("invoiceID");
					myReportISLR.parameterValue.add(rs.getInt(1));
					
					
					try
					{
						//Adjuntar comprobante ISLR
						email.addAttachment(myReportISLR.fileReport(designISLR, "pdf"));
						
					}
					catch (Exception e)
					{
						log.log(Level.SEVERE, "", e);
					}
					
				}
			}
			catch (SQLException e){
				e.printStackTrace();
			} finally{			
				try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
				try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			}
			
			
			
			/*Especificación de pago*/
			String designName = "ReprintPaymentSpecification";
			
			//Intanciar reporte e iniciar plataforma
			BIRTReport myReport = new BIRTReport();

			//Agregar parametro
			myReport.parameterName.add("paySelection");
			myReport.parameterValue.add(payselection.getC_PaySelection_ID());
					
			try
			{
				//Adjuntar la especificación del pago
				email.addAttachment(myReport.fileReport(designName, "pdf"));
				
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "", e);
			}
			
			String status = email.send();
			log.info("Email Send status: " + status);
		
			if (email.isSentOK())
			{
				
				return "Mail Sent Sucessfully";
			}
			else
				return "Mail cannot be Sent";
		
			
			
			}

			else
				return "Cannot create mail";
	}
	
	/**
	 * Se encarga de buscar el identificador del pago
	 * @param idOrderPay id de la orden de pago
	 * @return
	 */
	public int idPayment(int idOrderPay){
		int id = 0;
		String sql = "select pay.C_Payment_ID " +
					 "from C_PaySelection pas " +
					 "inner join C_PaySelectionCheck psc on (psc.C_PaySelection_ID = pas.C_PaySelection_ID) " +
					 "inner join C_Payment pay on (psc.C_Payment_ID = pay.C_Payment_ID) " +
					 "where pas.C_PaySelection_ID = " + idOrderPay;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, get_Trx()); 
			rs = pstmt.executeQuery();
			if(rs.next()){		
				id = rs.getInt(1);
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally{
			try {
			rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return id;
	}
		
}
