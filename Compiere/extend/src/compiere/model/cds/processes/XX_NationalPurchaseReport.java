


package compiere.model.cds.processes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import compiere.model.birt.BIRTReport;

public class XX_NationalPurchaseReport extends SvrProcess {
	
	Date FechaD = new Date();
	Date FechaH = new Date();
	

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();

		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null) {
				;
			} else if (name.equals("XX_Date1")) {
				FechaD = (Date) element.getParameter();
				
			}
			else if (name.equals("XX_Date2")) {
				FechaH = (Date) element.getParameter();
				
			}
						
		}
	}

	@Override
	protected String doIt() throws Exception {
		 DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		String designName = "NationalPurchase";
		String designName1 = "InternationalPurchase";

		// Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();

		// Agregar parametro
		myReport.parameterName.add("XX_Date1");
		myReport.parameterValue.add(formatter.format(FechaD));
		
		myReport.parameterName.add("XX_Date2");
		myReport.parameterValue.add(formatter.format(FechaH));

		
		// Correr Reporte O/C Nacionales
		myReport.runReport(designName, "xls");
		
		// Correr Reporte O/C Importadas
				myReport.runReport(designName1, "xls");
				
		return "Listo";
	}

}
