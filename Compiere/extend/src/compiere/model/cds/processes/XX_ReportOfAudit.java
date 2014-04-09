


package compiere.model.cds.processes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import compiere.model.birt.BIRTReport;

public class XX_ReportOfAudit extends SvrProcess {
	
	String Anio = "0";
	String Mes = "0";
	

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();

		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null) {
				;
			} else if (name.equals("XX_Year")) {
				Anio = element.getParameter().toString();
				
			}
			else if (name.equals("XX_Month")) {
				Mes = element.getParameter().toString();
				
			}
			
						
		}
	}

	@Override
	protected String doIt() throws Exception {
		 DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		String designName = "AuditReport";

		// Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();

		// Agregar parametro
		myReport.parameterName.add("XX_Year");
		myReport.parameterValue.add(Anio);
		
		myReport.parameterName.add("XX_Month");
		myReport.parameterValue.add(Mes);

		
		// Correr Reporte
		myReport.runReport(designName, "xls");
		return "Listo";
	}

}
