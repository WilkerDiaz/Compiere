package compiere.model.payments.processes;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import compiere.model.birt.BIRTReport;
import compiere.model.cds.processes.XX_CalculationSalePurchase;

public class XX_SalesBook extends SvrProcess {

	int year = 0;
	int month = 0;
	@Override
	protected void prepare() {

		ProcessInfoParameter[] para = getParameter();
	    for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null){
				;
			}else if (name.equals("year")){
				year = new Integer(element.getParameter().toString());
			}else if (name.equals("month")){
				month = new Integer(element.getParameter().toString());
			}else {;}
		}
	}

	@Override
	protected String doIt() throws Exception {

		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("month");
		myReport.parameterValue.add(month);
		myReport.parameterName.add("year");
		myReport.parameterValue.add(year);
		myReport.parameterName.add("tenant");
		myReport.parameterValue.add(getAD_Client_ID());
		
		//Correr Reporte
		myReport.runReport("SalesBook","xls");
		
		return "Revise su explorador de Internet";
	}

}
