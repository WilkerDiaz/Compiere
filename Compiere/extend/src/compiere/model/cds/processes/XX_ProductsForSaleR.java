package compiere.model.cds.processes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import compiere.model.birt.BIRTReport;

public class XX_ProductsForSaleR extends SvrProcess {
	
	Date date = new Date();

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();

		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null) {
				;
			} else if (name.equals("FechaHasta")) {
				date = (Date) element.getParameter();
			}
		}
	}

	@Override
	protected String doIt() throws Exception {
		 DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		String designName = "ProductsForSale";

		// Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();

		// Agregar parametro
		myReport.parameterName.add("FechaHasta");
		myReport.parameterValue.add(formatter.format(date));

		// Correr Reporte
		myReport.runReport(designName, "xls");
		return "Listo";
	}

}
