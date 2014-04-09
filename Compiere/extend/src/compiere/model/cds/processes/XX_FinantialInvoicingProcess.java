package compiere.model.cds.processes;

import java.awt.Container;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.apps.ADialog;
import org.compiere.framework.PrintInfo;
import org.compiere.framework.Query;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.print.Viewer;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.cds.MBPartner;
import compiere.model.cds.MCreditNotifyReturn;
import compiere.model.cds.MInvoice;
import compiere.model.cds.MOrder;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_Invoicing_Status;
import compiere.model.cds.X_Ref_XX_OrderStatus;
import compiere.model.cds.X_XX_CreditNotifyReturn;
import compiere.model.cds.callouts.C_OrderCallout;

public class XX_FinantialInvoicingProcess extends SvrProcess {

	int cOrderID = 0;
	Ctx ctx = Env.getCtx();
	boolean deseaCorreo = false;
	
	
	public XX_FinantialInvoicingProcess()
	{}
	
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
		
		MOrder order = new MOrder(ctx, cOrderID, null);
		boolean printNote = false;
		boolean hasNotes = false;
		int noteCount = 0;
		
		if (order.getXX_IsPrintedNote()){
			deseaCorreo = ADialog.ask(1, new Container(), "XX_REPrintedNote");
		}else {
			deseaCorreo = ADialog.ask(1, new Container(), "XX_PrintedNote");
		}
		
		String resp = Msg.getMsg(getCtx(), "XX_FinantialNote");	
			
		if (order.getXX_OrderStatus().compareTo(X_Ref_XX_OrderStatus.CHEQUEADA.getValue())!=0 
					&& order.getXX_InvoicingStatus().compareTo(X_Ref_Invoicing_Status.APROBADA.getValue())!=0)
		{
			return resp = Msg.getMsg(getCtx(), "XX_FinantialStatus");
		}
		
		// Searching Notifies
		String SQL = "select c_invoice_id from c_invoice " +
					" where c_order_id = " + cOrderID + 
					" and C_DOCTYPETARGET_ID in (" +
					Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPECREDIT_ID") + ", " +
					Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEDEBIT_ID") + ")";
		
		Vector<File> attachments = new Vector<File>();
		Utilities u = null;
		try{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
	
			while (rs.next()){
				
				noteCount ++;
				
				MInvoice notify = new MInvoice(ctx, rs.getInt(1), null);
				
				u = new Utilities();
				attachments.add(u.reportDebCred(notify, true));
				hasNotes = true;
				 
				if (!(printNote))
					printNote = true;
			}
				
			rs.close();
			pstmt.close();
				
		} catch (SQLException e) {
			log.log(Level.SEVERE, SQL, e);
		}
		
		// Search for other notifies (Penalizacion por retraso)
		String SQL_Penalty = "select XX_CreditNotifyReturn_ID from XX_CreditNotifyReturn " +
							 "where XX_NotificationType = 'DEE' and c_order_id = " + cOrderID  + " AND AD_CLIENT_ID="+  getCtx().getAD_Client_ID();
								
		
		try{
			PreparedStatement pstmt = DB.prepareStatement(SQL_Penalty, null);
			ResultSet rs = pstmt.executeQuery();
	
			while (rs.next()){
				noteCount ++;
				
				MCreditNotifyReturn notice = new MCreditNotifyReturn(ctx, rs.getInt(1), null);
				
				attachments.add(reportPenalty(notice));
				hasNotes = true; 
				
				if (!(printNote))
					printNote = true;
			}
			
			rs.close();
			pstmt.close();
				
		} catch (SQLException e) {
			log.log(Level.SEVERE, SQL, e);
		}
		
		//Sql for trade agreements
		String SQL_ACC = "select XX_CreditNotifyReturn_ID from XX_CreditNotifyReturn " +
		 "where c_order_id = " + cOrderID +" and XX_NotificationType = 'ACC'"+
		 " AND AD_CLIENT_ID="+  getCtx().getAD_Client_ID();
		
		try{
			PreparedStatement pstmt2 = DB.prepareStatement(SQL_ACC, null);
			ResultSet rs2 = pstmt2.executeQuery();
	
			while (rs2.next()){
				
				MCreditNotifyReturn notice = new MCreditNotifyReturn(ctx, rs2.getInt(1), null);
				attachments.add(reportTradeAgreement(notice, cOrderID));
				hasNotes = true;
				
			}
			
			rs2.close();
			pstmt2.close();
				
		} catch (SQLException e) {
			log.log(Level.SEVERE, SQL, e);
		}
		
		if (printNote){
			order.setXX_IsPrintedNote(true);
			order.save();
		}
		
		if(hasNotes){
			if(noteCount==1)
				resp = noteCount + " Nota";
			else
				resp = noteCount + " Notas";
		}
		
		//Envio el correo si se desea y hay notas que enviar
		u = new Utilities();
		if(deseaCorreo && hasNotes)
			resp += ": " + u.sendEmail(order, new MBPartner( getCtx(), order.getC_BPartner_ID(), null), attachments, 0);
			
		return resp;
	}
	
	
	/**
	 * Method that generate the Penalty Credit Note
	 * @return attachment
	 */
	public File reportPenalty(MCreditNotifyReturn notice){
		
		int CreditNotifyReturn_ID = notice.getXX_CreditNotifyReturn_ID();
		log.info("CreditNotifyReturn_ID=" + CreditNotifyReturn_ID);
		
		if (CreditNotifyReturn_ID < 1)
			throw new IllegalArgumentException("@NotFound@ @MCreditNotifyReturn_ID@");
	
		// Obtain the Active Record of M_Invoice Table preguntar a Paty
		Query q = new Query("XX_CreditNotifyReturn");
		q.addRestriction("XX_CREDITNOTIFYRETURNVIEW_ID", Query.EQUAL, Integer.valueOf(CreditNotifyReturn_ID));
		int table_ID = X_XX_CreditNotifyReturn.Table_ID;

		MPrintFormat f = null ;
		//System.out.println("Asunto "+subject);
		// Create the Process Info Instance to generate the report
		PrintInfo i = new PrintInfo("Aviso  Credito", table_ID, CreditNotifyReturn_ID, 0);
				
		f = MPrintFormat.get (Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_REPORT_CNRETOC_ID"), false);
			
		// Create the report
		ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i);
		new Viewer(re);
		
		// Generate the PDF file
		File attachment = null;
		
		try
		{
			attachment = File.createTempFile("aviso_credito_por_retraso_en_entrega_", ".pdf");
			re.getPDF(attachment);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		
		return attachment;
	}
	

	/**
	 * Method that generate the Trade Agreement Credit Notify
	 * @return attachment
	 */
public File reportTradeAgreement(MCreditNotifyReturn notice, int order){
		
		int CreditNotifyReturn_ID = notice.getXX_CreditNotifyReturn_ID();
		log.info("CreditNotifyReturn_ID=" + CreditNotifyReturn_ID);
		
		if (CreditNotifyReturn_ID < 1)
			throw new IllegalArgumentException("@NotFound@ @MCreditNotifyReturn_ID@");
	

		Query q = new Query("XX_CreditNotifyReturn");
		q.addRestriction("C_ORDER_ID", Query.EQUAL, order);
		int table_ID = X_XX_CreditNotifyReturn.Table_ID;

		MPrintFormat f = null ;

		// Create the Process Info Instance to generate the report
		PrintInfo i = new PrintInfo("Aviso de Crédito", table_ID, order, 0);
				
		f = MPrintFormat.get (Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_REPORT_TRADEAG_ID"), false);
			
		// Create the report
		ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i);
		new Viewer(re);
		
		// Generate the PDF file
		File attachment = null;
		
		try
		{
			attachment = File.createTempFile("aviso_credito_por_acuerdo_comercial", ".pdf");
			re.getPDF(attachment);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		
		return attachment;
	}
	
}
