package compiere.model.cds.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.apps.ADialog;
import org.compiere.common.constants.EnvConstants;
import org.compiere.framework.PrintInfo;
import org.compiere.framework.Query;
import org.compiere.model.MSequence;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.print.Viewer;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.birt.BIRTReport;
import compiere.model.cds.MBPartner;
import compiere.model.cds.MInOut;
import compiere.model.cds.MInvoice;
import compiere.model.cds.MOrder;
import compiere.model.cds.MVCNPurchasesBook;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_OrderType;
import compiere.model.payments.X_XX_VCN_EstimatedAPayable;
import compiere.model.payments.X_XX_VCN_PaymentDollars;

/**
 * @author Patricia Ayuso M. Wdiaz
 *
 */
public class XX_ApprovePO_Invoicing extends SvrProcess {

	int cOrderID = 0;
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("C_Order_ID"))
				cOrderID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	}

	@Override
	protected String doIt() throws Exception {
				
		String sql = "select * from C_Invoice where C_Order_ID = "+cOrderID+" and DocStatus = '"+MInvoice.DOCSTATUS_Drafted+"' and C_DocTypeTarget_ID = "+Env.getCtx().getContext("#XX_C_DOCTYPE_ID")+"";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			   rs = pstmt.executeQuery();
			if (rs.next()){	
				throw new Exception(Msg.getMsg(getCtx(), "XX_FailureCompleteInvoice")+": "+rs.getString("DocumentNo")+"");
			}
			
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}finally{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		
		boolean resp = false;
		MOrder mOrder = new MOrder(getCtx(), cOrderID, get_Trx());
		
		/** Verifying if the user want to continue the process */
		boolean answer = ADialog.ask(EnvConstants.WINDOW_INFO, new Container(), 
				Msg.getMsg(Env.getCtx(), "XX_InvoicingApprovePO", new String[]{mOrder.getDocumentNo()}));
		if(!answer)
			return "";

		/** Updating O/C Invoicing Status */
		mOrder.setXX_InvoicingStatus("AP");
		mOrder.setXX_InvoiceDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		
		/****Jessica Mendoza****/
		//Setear en las cuentas por pagar, el usuario y la fecha de aprobación
		String sqlUpdate = "update C_Invoice set " +
						   "XX_ApprovalDate = " + DB.TO_DATE(new Timestamp(Calendar.getInstance().getTimeInMillis())) + ", " +
						   "XX_UserInvoiceApproval_ID = " + getAD_User_ID() + ", " +
						   "XX_AccountPayableStatus = (CASE WHEN XX_PaidInvoices = 'Y' THEN 'P' ELSE 'A' END) ";
		
		sqlUpdate += "where C_Order_ID = " + cOrderID;		 		
		
		DB.executeUpdate(get_Trx(), sqlUpdate);
		
		
		//Update
		
		//Crear registro en la tabla de pago dolar
		int idEstimated = idEstimatedPayable(mOrder.getC_Order_ID());
		X_XX_VCN_EstimatedAPayable estimated = new X_XX_VCN_EstimatedAPayable(Env.getCtx(), 
				idEstimated, get_Trx());
		X_XX_VCN_PaymentDollars payDollars = new X_XX_VCN_PaymentDollars(Env.getCtx(), 0, get_Trx());
		payDollars.setXX_Concept(namePartner(mOrder.getC_BPartner_ID()));
		payDollars.setDescription(estimated.getDescription());
		payDollars.setC_Order_ID(mOrder.getC_Order_ID());
		//cuenta contable
		payDollars.setXX_AmountForeign(estimated.getXX_AmountForeign());
		//tipo de tasa???
		//cambio promedio
		//monto local
		//saldos		
		
		//Eliminar las cuentas por pagar estimadas que contenga dicha O/C
		String sqlDelete = "delete from XX_VCN_EstimatedAPayable " +
					       "where XX_VCN_EstimatedAPayable_ID = " + idEstimated;
		DB.executeUpdate(get_Trx(), sqlDelete);
		/****Fin código - Jessica Mendoza****/

		if(mOrder.save()){
			resp = true;
			log.fine("Update Purchase Order's XX_InvoicingStatus ");			
		}
		
		Utilities util = new Utilities();
		util.generateInvoiceReport(cOrderID, 0, true);
		
		// Checking if there is a Reception
		/*String SQL = "Select M_INOUT_ID from M_INOUT " +
				"where C_ORDER_ID = " + cOrderID +" AND DOCSTATUS in ('CO', 'CL')";*/
		
		String SQL = "Select M_INOUT_ID from M_INOUT io join C_Order o " +
				     "on (io.C_ORDER_ID = o.C_ORDER_ID) where io.C_ORDER_ID = " + cOrderID +" " +
				     "AND o.XX_OrderType = '"+X_Ref_XX_OrderType.NACIONAL.getValue()+"' and io.DOCSTATUS in ('CO', 'CL')";

		 pstmt = null;
		    rs = null;
		try {
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();
			if (rs.next()){
				
				MInOut inOut = new MInOut( getCtx(), rs.getInt("M_INOUT_ID"), null);
				inOut.setXX_Status_Sinc(false);
				inOut.save();
				util.generateDebtCreditNotifies(cOrderID, get_Trx());
			}
	
		} catch (SQLException e) {
			log.log(Level.SEVERE, SQL, e);
		}finally{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		
		//Acuerdo comercial
		if(mOrder.getXX_POType().equals("POM"))
			util.tradeAgreement(cOrderID, get_Trx());
		
		//Número de comprobante de retención ISLR e impresion de los mismos
		if(!mOrder.getXX_POType().equals("POM"))
			ISLRReceipt(cOrderID);
		
		//Commit para mostrar reportes
		commit();
		
		showIVAReport(mOrder.get_ID());
		
		if(resp)
			return "Facturación de la OC Aprobada!";
		else
			return "Facturación de la OC NO Aprobada!";
	}
	
	/*
	 * Muestra los reportes de IVA (Si existen)
	 */
	private void showIVAReport(int orderID){
		
		String sql_pb = "";
 		PreparedStatement pstmt_pb = null;
 		ResultSet rs_pb = null;
 		MVCNPurchasesBook result = null;

 		sql_pb = "SELECT XX_VCN_PurchasesBook_ID FROM XX_VCN_PurchasesBook WHERE XX_Status='CO' AND XX_Withholding is not null and c_order_id = " + orderID;
 		
 		try{
 			pstmt_pb = DB.prepareStatement(sql_pb, null);
			rs_pb = pstmt_pb.executeQuery();
			
			if(rs_pb.next()){
				MVCNPurchasesBook pBook1 = new MVCNPurchasesBook( getCtx(), rs_pb.getInt("XX_VCN_PurchasesBook_ID"), null);
				result = pBook1;
			}
 		}
 		catch(SQLException e){
 			log.log(Level.SEVERE, sql_pb, e);
 		}
 		finally{
				DB.closeStatement(pstmt_pb);
				DB.closeResultSet(rs_pb);
		}
		
 		if(result!=null){
			
 			Calendar date = Calendar.getInstance();
				
			date.setTimeInMillis(result.getXX_DATE().getTime());
				
			Integer numMonth = date.get(Calendar.MONTH)+1;
			Integer numYear = date.get(Calendar.YEAR);
			String month = numMonth.toString();
			if(numMonth < 10)
				month = "0".concat(month);
			
			String year = numYear.toString();
			String yearMonth = (year.concat(month));
				
			String XX_NOCOMPROBANTE = yearMonth + result.getXX_Withholding();
						
			log.info("XX_NOCOMPROBANTE=" + XX_NOCOMPROBANTE);
			if (XX_NOCOMPROBANTE == "")
				throw new IllegalArgumentException("@NotFound@ @XX_NOCOMPROBANTE@");
				
			// Obtain the Active Record of M_Invoice Table
			Query q = new Query("XX_VCN_PURCHASESBOOK_ID");
			q.addRestriction("XX_VCN_PURCHASESBOOK_ID", Query.EQUAL, result.get_ID());
				
			// Create the Process Info Instance to generate the report
			PrintInfo i = new PrintInfo("Comprobante de Retención", 1000339, result.get_ID(), 0);
			MPrintFormat f = MPrintFormat.get (Env.getCtx(), 1000311, false);
				
			// Create the report
			ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i);
			new Viewer(re);
		}
	}
	
	/*
	 * Comprobante de Retencion ISLR
	 */
	private void ISLRReceipt(int order){
	
		//Número de comprobante de retención
		String ISLRReceipt = "SELECT b.C_Invoice_ID FROM XX_VCN_ISLRAMOUNT a, c_invoice b " + 
							 "WHERE b.docstatus = 'CO' and a.c_invoice_id = b.c_invoice_id " +
							 "AND b.c_order_id = " + order + " " +
							 "AND a.XX_RETAINEDAMOUNT > 0 " +
							 "GROUP BY b.c_invoice_id";
		
		PreparedStatement pstmt_ISLR = null;
		ResultSet rs_ISLR = null;
		Vector<Integer> invoices = new Vector<Integer>();
		
		try {
			
			pstmt_ISLR = DB.prepareStatement(ISLRReceipt, get_Trx());
			rs_ISLR = pstmt_ISLR.executeQuery();
			
			MSequence seq = new MSequence( Env.getCtx(), 1005014, null);
			int aux = -3;
		
			while (rs_ISLR.next()){
				
				String sqlUpdate = "update C_Invoice set " +
								   "XX_ISLRReceiptNo = " + seq.getCurrentNext() + " " +
								   "where C_Invoice_ID = " + rs_ISLR.getInt("C_Invoice_ID");		 
				
				aux = DB.executeUpdate(get_Trx(), sqlUpdate);
				
				if(aux>0){
					
					invoices.add(rs_ISLR.getInt("C_Invoice_ID"));
					
					//Aumenta la secuencia
					seq.setCurrentNext(seq.getCurrentNext() + seq.getIncrementNo());
					seq.save();	
				}
			}
	
		} catch (SQLException e) {
			log.log(Level.SEVERE, ISLRReceipt, e);
		}finally{
			
			try {
				rs_ISLR.close();
				pstmt_ISLR.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//Imprime los comprobantes
		for(int i=0; i<invoices.size(); i++)
			generateISLRReport(invoices.get(i).toString());
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
	
	/**
	 * Se encarga de buscar el nombre del proveedor asociado a la O/C
	 * @author Jessica Mendoza
	 * @param idPartner identificador del proveedor
	 * @return
	 */
	public String namePartner(int idPartner){
		String name = "";
		String sql = "select name from C_BPartner where C_BPartner_ID = " + idPartner;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()){	
				name = rs.getString(1);
			}
		}catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}finally{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return name;
	}
	
	/**
	 * Se encarga de buscar el identificador de la cuenta por 
	 * pagar estimada correspondiente a dicha O/C
	 * @author Jessica Mendoza
	 * @param idOrder identificador de la O/C
	 * @return
	 */
	public int idEstimatedPayable(int idOrder){
		int idEstimated = 0;
		String sql = "select XX_VCN_EstimatedAPayable_ID " +
					 "from XX_VCN_EstimatedAPayable " +
					 "where C_Order_ID = " + idOrder + " " +
					 "and XX_Dispensable = 'Y' ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()){	
				idEstimated = rs.getInt(1);
			}
		}catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}finally{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return idEstimated;
	}
		
}
