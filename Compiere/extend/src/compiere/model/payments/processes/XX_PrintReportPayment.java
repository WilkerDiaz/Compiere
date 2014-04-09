package compiere.model.payments.processes;

import org.compiere.framework.PrintInfo;
import org.compiere.framework.Query;
import org.compiere.model.X_C_Payment;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.print.Viewer;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

import compiere.model.cds.MPayment;

/**
 * Se encarga de generar el reporte para las transferencias 
 * de pagos importados
 * @author Jessica Mendoza
 *
 */
public class XX_PrintReportPayment extends SvrProcess{

	@Override
	protected String doIt() throws Exception {
		
		MPayment pago = new MPayment(Env.getCtx(), getRecord_ID(), null);
		pago.setXX_UserPrintReport_ID(Env.getCtx().getAD_User_ID());
		pago.save();
		
		Query q = new Query("C_Payment_ID");
		q.addRestriction("C_Payment_ID", Query.EQUAL, "" + getRecord_ID());
		int table_ID = X_C_Payment.Table_ID;

		// Create the Process Info Instance to generate the report
		PrintInfo i = new PrintInfo("Partner Import", table_ID, getRecord_ID(), 0);
		MPrintFormat f = MPrintFormat.get (Env.getCtx(), 1000592, false);  //ID del formato de impresión de la integración: 1000592

		// Create the report
		ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i);
		new Viewer(re);
		
		return null; 
	}

	@Override
	protected void prepare() {		
		
	}

}
