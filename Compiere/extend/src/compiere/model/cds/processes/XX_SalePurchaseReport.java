


package compiere.model.cds.processes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import compiere.model.birt.BIRTReport;

public class XX_SalePurchaseReport extends SvrProcess {
	
	int Anio = 0;
	int Mes = 0;
	String Tienda = "0";
	String Categoria = "0";
	String Departamento = "0";

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();

		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null) {
				;
			} else if (name.equals("XX_YEAR")) {
				Anio = element.getParameterAsInt();
				
			}
			else if (name.equals("XX_Month")) {
				Mes = element.getParameterAsInt();
				
			}
			else if (name.equals("Tienda")) {

				Tienda = "0"+ element.getParameter().toString();
				
			}
			else if (name.equals("Categoria")) {
				Categoria = element.getParameter().toString();
				
			}
			else if (name.equals("Departamento")) {
				Departamento = element.getParameter().toString();
				
			}
			
		}
	}

	@Override
	protected String doIt() throws Exception {
		 DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		String designName = "SalePurchaseReport";

		// Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();

		// Agregar parametro
		myReport.parameterName.add("XX_YEAR");
		myReport.parameterValue.add(Anio);
		
		myReport.parameterName.add("XX_Month");
		myReport.parameterValue.add(Mes);

		myReport.parameterName.add("Tienda");
		myReport.parameterValue.add(Tienda);
		
		myReport.parameterName.add("Categoria");
		myReport.parameterValue.add(Categoria);
		
		myReport.parameterName.add("Departamento");
		myReport.parameterValue.add(Departamento);
		// Correr Reporte
		myReport.runReport(designName, "xls");
		return "Listo";
	}

}
