package compiere.model.cds.processes;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.compiere.framework.PrintInfo;
import org.compiere.framework.Query;
import org.compiere.model.MClient;
import org.compiere.model.X_C_Order;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MBPartner;
import compiere.model.cds.MOrder;
import compiere.model.cds.MUser;
import compiere.model.cds.X_Ref_XX_OrderType;
import compiere.model.cds.X_Ref_XX_Ref_ContactType;

/** Envía Email a Proveedor con el documento en formato PDF de una O/C en estado Proforma
 * @author Gabrielle Huchet
 */
public class XX_SendMailOCImpVendor extends SvrProcess {
	
	protected void prepare() {
		
	}

	@Override
	protected String doIt() throws Exception {
	
		MOrder order = new MOrder(getCtx(),getRecord_ID(), get_Trx());
		return sendEmailPOtoVendor(order);
	}

	/**
	 * 
	 * Este método crea el documento de la O/C Proforma en formato 
	 * PDF y la envía al proveedor
	 * 
	 */
	public String sendEmailPOtoVendor(MOrder ordenCompra){

		String subject = "";
		String msg = "";
		int printFormat = 0;
		
		if (ordenCompra.getXX_OrderType().equals(X_Ref_XX_OrderType.IMPORTADA.getValue())){
			printFormat = Env.getCtx().getContextAsInt("#XX_L_PF_OCIMPORTEDPRO_ID");
			subject = Msg.getMsg(Env.getCtx(), "XX_SentPOSubjectPro", new String[] { ordenCompra.getDocumentNo() });
			msg =  Msg.getMsg(Env.getCtx(), "XX_SentPurchaseOrderPro", new String[] { ordenCompra.getDocumentNo(), ordenCompra.getDocumentNo()});
		}else {
			printFormat = Env.getCtx().getContextAsInt("#XX_L_PF_OCNATIONALPRO_ID");
			subject = Msg.getMsg(Env.getCtx(), "XX_SentPOSubjectProNa", new String[] { ordenCompra.getDocumentNo() });
			msg =  Msg.getMsg(Env.getCtx(), "XX_SentPOMsgProNa", new String[] { ordenCompra.getDocumentNo(), ordenCompra.getDocumentNo()});
		}
			MPrintFormat f = null;
			int C_Order_ID = ordenCompra.getC_Order_ID();
			log.info("C_Order_ID" + C_Order_ID);
			if (C_Order_ID < 1)
				throw new IllegalArgumentException("@NotFound@ @C_Order_ID@");
			
			// Obtain the Active Record of M_Order Table
			Query q = new Query("C_Order_ID");
			q.addRestriction("C_Order_ID", Query.EQUAL, Integer.valueOf(C_Order_ID));
			int table_ID = X_C_Order.Table_ID;
			
			// Create the Process Info Instance to generate the report
			PrintInfo i = new PrintInfo("Orden de Compra", table_ID, C_Order_ID, 0);
			f = MPrintFormat.get (Env.getCtx(), printFormat, false);
			
			// Create the report
			ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i); 
			//new Viewer(re);
			
			// Generate the PDF file
			File attachment = null;		
			try
			{
				attachment = File.createTempFile("Purchase Order", ".pdf");
				re.getPDF(attachment);
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "", e);
			}
	
			// Send the PDF File by E-Mail 
			MBPartner socioComprador = new MBPartner(Env.getCtx(), ordenCompra.getXX_UserBuyer_ID(), null);
			/* Busco los usuarios de los Socio de Negocio
			 * */
			MUser userComprador = null;
			String SQL = "SELECT AD_USER_ID " +
						 "FROM AD_USER " +
						 "WHERE C_BPartner_ID = "+socioComprador.get_ID() +" ";
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = null;
		    	
			try{
				rs = pstmt.executeQuery();			
			    
				if (rs.next()){				
					userComprador = new MUser(Env.getCtx(), rs.getInt("AD_USER_ID"), null);
				}
			}
			catch (SQLException e){
				e.printStackTrace();
			} finally{			
				try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
				try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			}
			
			String comprador = userComprador.getEMail();
	
			MBPartner vendorAux = new MBPartner(getCtx(), ordenCompra.getC_BPartner_ID(), null);
			String toVendor = vendorAux.getXX_VendorEmail();	
			MClient m_client = MClient.get(Env.getCtx());
		
			EMail email = m_client.createEMail(null, toVendor, vendorAux.getName(), subject, msg);
			
			if (email != null){

				if(!comprador.isEmpty()){
					email.addTo(comprador, userComprador.getName());
					//System.out.println(userComprador.getName());
				}
				//Se envia correo a representantes comerciales, ventas y administrativos
				 MUser userRepre = null;
					SQL = "SELECT AD_USER_ID " +
						  "FROM AD_USER " +
						  "WHERE C_BPartner_ID = "+ordenCompra.getC_BPartner_ID() +" " +
						  "AND XX_ContactType IN ("+X_Ref_XX_Ref_ContactType.COMERCIAL.getValue()+","+
						  X_Ref_XX_Ref_ContactType.SALES.getValue()+","+X_Ref_XX_Ref_ContactType.ADMINISTRATIVE.getValue()+")";
								 		
					//System.out.println(SQL);
					pstmt = DB.prepareStatement(SQL, null); 
				    rs = null;
				    	
					try{
						rs = pstmt.executeQuery();			
					    
						while (rs.next()){				
							userRepre = new MUser(Env.getCtx(), rs.getInt("AD_USER_ID"), null);
							if(!userRepre.getEMail().isEmpty()){
								email.addTo(userRepre.getEMail(), userRepre.getName());
								//System.out.println("\n"+userRepre.getEMail()+","+ userRepre.getName()+","+userRepre.getXX_ContactType());
							}
						}
					}
					catch (SQLException e){
						e.printStackTrace();
					} finally{			
						try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
						try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
					}
					
				//	Attachment
				if (attachment != null && attachment.exists())
					email.addAttachment(attachment);
				
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
	
}
