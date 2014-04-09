package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.model.X_C_Invoice;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.MBPartner;
import compiere.model.cds.MInvoice;
import compiere.model.payments.processes.XX_SalePurchaseAccEnt;


public class XX_VoidInvoice extends SvrProcess {

	Integer orderID = 0;
	Integer singleID = 0;
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] parameter = getParameter();
		for (ProcessInfoParameter element : parameter) {
			
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("C_Order_ID"))
				orderID = element.getParameterAsInt();
			else if (name.equals("C_Invoice_ID"))
				singleID = element.getParameterAsInt();
			else 
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	@Override
	protected String doIt() throws Exception {
		
		if(orderID>0 && singleID>0)
			return "Solo puede indicar un valor";
		if(orderID == 0 && singleID == 0)
			return "Debe indicar al menos un valor";
		
		int qty = 0;
		
		//Si se invoca desde la ventana Facturas
		if(getRecord_ID()>0){
			
			MInvoice invoice = new MInvoice( Env.getCtx(), getRecord_ID(), get_Trx());
			
			if(invoice.getDocStatus().equalsIgnoreCase("VO"))
				return "Esta factura ya está anulada.";
			
			if(invoice.getXX_ApprovalDate()!=null)
				return "Esta factura ya está Aprobada, para anular todas las facturas asociadas (incluyendo esta) " +
					   "busque la opción de menú <Anular Facturacion>";
	
			invoice.setDocAction(X_C_Invoice.DOCACTION_Void);
			DocumentEngine.processIt(invoice, X_C_Invoice.DOCACTION_Void);
			invoice.save();
			
			//Se modifica el DocumentNo
			String docNo = "update C_Invoice " +
		  				   "set DocumentNo =  DocumentNo || '_" + X_C_Invoice.DOCACTION_Void +"'"
		  				 + "||(select count(*)+1 from c_invoice where isSOTRX='N' and DocumentNo like '"+invoice.getDocumentNo()+"_VO%') " +
		  				   "where C_INVOICE_ID = " + getRecord_ID();
			qty = DB.executeUpdate(get_Trx(), docNo);
				
			disassociateDistrib(getRecord_ID());
		}
		else{
			
			//Solo se puede reversar las facturas si estamos en el mismo periodo impositivo
			if(!samePeriod())
				return "Ya se pasó el periodo impositivo. No se puede anular esta facturación.";
			
			String sql = "select c_invoice_id, XX_AccountPayableStatus " + 
						 "from c_invoice " +
						 "where docstatus = 'CO' and isSoTrx='N' and XX_approvalDate is not null ";
						 
			if(orderID>0)
				sql += "and C_Order_ID = "+ orderID;
			else if(singleID>0)
				sql += "and C_Order_ID is null and C_Invoice_ID = "+ singleID;
			    		 
			if(isPaid(sql))
				return "Ya se pagó esta facturación. No se puede anular esta facturación.";
			
			PreparedStatement prst = DB.prepareStatement(sql, null);					
			ResultSet rs = prst.executeQuery();
			
			//Se anulan las facturas y Deb/Cred asociados
			int id = 0;
			
			while(rs.next()){
				
				id = rs.getInt("c_invoice_id");
				
				MInvoice invoice = new MInvoice( Env.getCtx(), id, get_Trx());
				invoice.setDocAction(X_C_Invoice.DOCACTION_Void);
				DocumentEngine.processIt(invoice, X_C_Invoice.DOCACTION_Void);
				invoice.save();
				
				//Se modifica el DocumentNo
				String docNo = "update C_Invoice " +
			  				   "set DocumentNo =  DocumentNo || '_" + X_C_Invoice.DOCACTION_Void +"'"
			  				 + "||(select count(*)+1 from c_invoice where isSOTRX='N' and DocumentNo like '"+invoice.getDocumentNo()+"_VO%') " +
			  				   "where C_INVOICE_ID = " + id;
				qty = DB.executeUpdate( get_Trx(), docNo);
				
				disassociateDistrib(id);
				
				//Anular el libro de compras de cada registro
				voidPB(id);
				
				//Se coloca el estado de la facturacion en AF si O/C
				if(orderID>0){
					String oc = "update C_Order " +
				  				   "set XX_InvoicingStatus = 'AF' "+
				  				   "where issotrx='N' and c_order_id = " + orderID;  
					DB.executeUpdate(get_Trx(), oc);
				}
			}
		}
		
		String response = "No se anularon facturas.";
		if(qty==1)
			response = "Se anuló: " + qty + " factura.";
		else if(qty > 1)
			response = "Se anularon: " + qty + " facturas.";
		
		return response;
	}

	private boolean samePeriod(){
		
		return true;
	}
	
	private void disassociateDistrib(int id){
		
		//Desasociar lineas de distribución (ByS)
	    String updateDis = "update XX_ProductPercentDistrib " +
	    				   "set C_INVOICE_ID = null, C_InvoiceLine_ID = null " +
	    				   "where C_INVOICE_ID = " + id;
	    
	    DB.executeUpdate(get_Trx(), updateDis);
	}
	
	/*
	 * Delete Purchase's Book registries
	 */
	private void voidPB(int id){
		
		//Desasociar lineas de distribución (ByS)
	    String voidPB = "update XX_VCN_PurchasesBook " +
	    				"set XX_Status = 'VO' " +
	    				"where C_INVOICE_ID = " + id;
	    
	    DB.executeUpdate(get_Trx(), voidPB);
	}
	
	private boolean isPaid(String sql){
		
		//Verificammos si las facturas estan pagadas
		boolean paid = false;
		int inv = 0;
		PreparedStatement prst = DB.prepareStatement(sql, null);					
		ResultSet rs;
		try {
			rs = prst.executeQuery();
			String status ="";
			while(rs.next()){
				status = rs.getString("XX_AccountPayableStatus");
				
				if(status!=null && status.equals("P")){
					paid = true;
					inv = rs.getInt("C_Invoice_ID");
					break;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Si la factura no es cuenta corriente entonces no se puede anular
		if(paid){
			MInvoice invoice = new MInvoice( getCtx(), inv, null);
			
			if(!invoice.get_ValueAsBoolean("XX_PaidInvoices"))
				return true;
		}
		
		return false;
	}
	
}
