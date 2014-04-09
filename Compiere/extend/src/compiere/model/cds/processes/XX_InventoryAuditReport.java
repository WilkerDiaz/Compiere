package compiere.model.cds.processes;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import compiere.model.birt.BIRTReport;

public class XX_InventoryAuditReport extends SvrProcess {

	Integer year = 0;
	Integer month = 0;
	
	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();

		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null){
				;
			}else if (name.equals("month")){
				month =  element.getParameterAsInt();
			}else if (name.equals("year")){
				year =  element.getParameterAsInt();
			}
		}
	}

	@Override
	protected String doIt() throws Exception {
		
		String designName = "InventoryAudit";
		
		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("Month");
		myReport.parameterValue.add(month);
		myReport.parameterName.add("Year");
		myReport.parameterValue.add(year);
		myReport.parameterName.add("tenant");
		myReport.parameterValue.add(getAD_Client_ID());
		
		//Correr Reporte
		myReport.runReport(designName,"xls");
		
		return "";
	}

}
