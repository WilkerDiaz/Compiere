package compiere.model.payments.processes;

import org.compiere.process.SvrProcess;
import compiere.model.birt.BIRTReport;

public class XX_PrintPaymentAdv extends SvrProcess {

	@Override
	protected void prepare() {
		//DO NOTHING
	}

	@Override
	protected String doIt() throws Exception {

		String designName = "PaymentAdv";
		
		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();

		//Agregar parametro
		myReport.parameterName.add("payment");
		myReport.parameterValue.add(getRecord_ID());
		
		//Correr Reporte
		myReport.runReport(designName,"pdf");
		
		return "Revise su explorador";
	}

}
