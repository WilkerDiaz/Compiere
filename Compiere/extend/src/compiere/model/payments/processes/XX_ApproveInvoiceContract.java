package compiere.model.payments.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.model.MSequence;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.birt.BIRTReport;
import compiere.model.cds.MBPartner;
import compiere.model.cds.MInvoice;
import compiere.model.cds.Utilities;
import compiere.model.suppliesservices.X_XX_Contract;


/**
 * Actualiza el estado de las cuentas por pagar en la factura al ejecutar el proceso
 * @author Jessica Mendoza
 *
 */
public class XX_ApproveInvoiceContract extends SvrProcess{

	Integer contractID; 
	String contractValue;
	Integer invoiceID;
	String invoiceDoc;
	Utilities util = new Utilities();
	
	@Override
	protected String doIt() throws Exception {
		Boolean bool = false;

		//Actualiza el estado de la facturación, la fecha de aprobación y el usuario de aprobación en la factura
		MInvoice invoice = new MInvoice(Env.getCtx(), invoiceID, get_Trx());
		invoice.setXX_ApprovalDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		invoice.setXX_InvoicingStatusContract("AP");
		invoice.setXX_UserInvoiceApproval_ID(Env.getCtx().getAD_User_ID());
		
		if(invoice.get_ValueAsBoolean("XX_PaidInvoices"))
			invoice.setXX_AccountPayableStatus("P");
			
		//Fecha de Vencimiento
		calculateDueDate(invoice);
		
		invoice.save();
		
		commit();
		
		X_XX_Contract contrat = new X_XX_Contract(Env.getCtx(), contractID, get_Trx());
			
		//Elimina dicho contrato en las cuentas por pagar estimadas
		String deleteCxPE = "delete from XX_VCN_EstimatedAPayable " +
							"where XX_Contract_ID = " + contractID + " AND C_BPartner_ID = " + invoice.getC_BPartner_ID() + 
							" and XX_DateEstimated = " + DB.TO_DATE(invoice.getXX_DueDate());
		DB.executeUpdate(get_Trx(), deleteCxPE);
					
		if (contrat.getXX_ContractType().equals("Nacional")){
			//Generación del reporte de retención de IVA
			bool = util.generateInvoiceReport(0, invoiceID, true); 
		}else
			bool = true;
		
		//Número de comprobante de retención ISLR e impresion de los mismos
		ISLRReceipt();

		if (bool)
			return Msg.getMsg(Env.getCtx(), "ProcessOK");
		else
			return "Error";
	}

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("XX_Contract_ID")){
				contractID = element.getParameterAsInt();
				contractValue = element.getInfo();
			}else if (name.equals("C_Invoice_ID")){
				invoiceID = element.getParameterAsInt();
				invoiceDoc = element.getInfo();
			}else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}
	
	private void ISLRReceipt(){
			
		//Número de comprobante de retención
		String ISLRReceipt = "SELECT * FROM XX_VCN_ISLRAMOUNT a " + 
							 "WHERE a.c_invoice_id = " + invoiceID + " " +
							 "AND a.XX_RETAINEDAMOUNT > 0 ";
				
		PreparedStatement pstmt_ISLR = null;
		ResultSet rs_ISLR = null;
				
		try {
					
			pstmt_ISLR = DB.prepareStatement(ISLRReceipt, get_Trx());
			rs_ISLR = pstmt_ISLR.executeQuery();
					
			MSequence seq = new MSequence( Env.getCtx(), 1005014, null);
			int aux = -3;
				
			if (rs_ISLR.next()){
		
				String sqlUpdate = "update C_Invoice set " +
								   "XX_ISLRReceiptNo = " + seq.getCurrentNext() +
								   "where C_Invoice_ID = " + invoiceID;		 
				
				aux = DB.executeUpdate(get_Trx(), sqlUpdate);
				
				if(aux>0){
					
					//Aumenta la secuencia
					seq.setCurrentNext(seq.getCurrentNext() + seq.getIncrementNo());
					seq.save();	
				}
				
				//Imprime los comprobantes
				generateISLRReport(invoiceID.toString());
			}
			
		} catch (SQLException e) {
			log.log(Level.SEVERE, ISLRReceipt, e);
		}finally{
			
			DB.closeResultSet(rs_ISLR);
			DB.closeStatement(pstmt_ISLR);
		}
	}
	
	/**
	 * Se encarga de generar el reporte de los comprobantes de retencion
	 * @param invoiceID id de la factura
	 */
	public void generateISLRReport(String invoiceID){
		
		String designName = "ISLRReceipt";

		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("invoiceID");
		myReport.parameterValue.add(invoiceID);
		
		//Correr Reporte
		myReport.runReport(designName,"pdf");
	}
	
	/*
	 * Calcula la fecha de vencimiento a partir de la fecha actual
	 */
	private void calculateDueDate(MInvoice invoice){
		
		int diasBeneficio = 0;
    	int totalDays = 0;
    	Vector<Integer> condicionPago = new Vector<Integer>(6);
    	Calendar dueDate = Calendar.getInstance();
		Timestamp dueDateFormat = new Timestamp(0);
		
		condicionPago = util.infoCondicionPago(invoice.getC_PaymentTerm_ID());					

		if(condicionPago.get(6)!=0)
			totalDays = condicionPago.get(7);
		else if (condicionPago.get(3)!=0)
			totalDays = condicionPago.get(2);
		else if(condicionPago.get(0)!=0)
			totalDays = condicionPago.get(1);
		
		if (invoice.getC_Currency_ID()==205){
			diasBeneficio = util.benefitVendorDayAdvance(invoice.getC_BPartner_ID());
			totalDays = totalDays - diasBeneficio;
		}
		
		if(totalDays<0)
			totalDays = 0;
		
	    dueDate.add(Calendar.DATE, totalDays);
	    dueDateFormat = new Timestamp(dueDate.getTimeInMillis());

		invoice.setXX_DueDate(dueDateFormat);
	}
}
