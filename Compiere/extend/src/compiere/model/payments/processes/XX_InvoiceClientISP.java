package compiere.model.payments.processes;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.birt.BIRTReport;

/**
 * Se encarga de imprimir el reporte de facturación cliente, de Inversiones Uvalda C.A.
 * a través del documento de la factura
 * @author Jessica Mendoza
 *
 */
public class XX_InvoiceClientISP extends SvrProcess{

	private String documentNoInvoice; 
	
	@Override
	protected String doIt() throws Exception {

		String designName = "InvoiceClientISP"; 
		
		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("idInvoice");
		myReport.parameterValue.add(documentNoInvoice);
		
		//Correr Reporte
		myReport.runReport(designName, "pdf");
		
		return Msg.getMsg(Env.getCtx(), "XX_ProcessComplete");
	}

	@Override
	protected void prepare() {
		ProcessInfoParameter[] parametro = getParameter();
		for (int i = 0; i < parametro.length; i++) {
			String name = parametro[i].getParameterName();
			if (name.equals("InvoiceID"))
				documentNoInvoice = (parametro[i].getParameter().toString());
		}
	}
	
}
