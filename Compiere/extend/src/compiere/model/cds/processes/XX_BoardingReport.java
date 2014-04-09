package compiere.model.cds.processes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

import compiere.model.birt.BIRTReport;

public class XX_BoardingReport extends SvrProcess {


	Date dateFrom = new Date();
	Date dateTo = new Date();
	
	@Override
	protected void prepare() {

		ProcessInfoParameter[] parameter = getParameter();
		
		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();			
			if (element.getParameter() == null) ;
			else if (name.equals("dateFrom")) { 
				 dateFrom = (Date) element.getParameter();
			}else if (name.equals("dateTo")) {
				dateTo = (Date) element.getParameter();	
			}else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	@Override
	protected String doIt() throws Exception {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
		//Mostrar Reporte Definitivo
		generateReport(sdf.format(dateFrom), sdf.format(dateTo));
		
		return "Revise su explorador de internet para encontrar el archivo";
	}
	
	/**
	 * Se encarga de generar el reporte Excel
	 * @param fecha desde, fecha hasta
	 */
	public void generateReport(String from, String to){
		
		String designName = "ReporteEmbarques";
		
		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();

		//Agregar parametros
		myReport.parameterName.add("desde");
		myReport.parameterValue.add(from);
		myReport.parameterName.add("hasta");
		myReport.parameterValue.add(to);
		myReport.parameterName.add("company");
		myReport.parameterValue.add(Env.getCtx().getContextAsInt("#AD_Client_ID"));		
		
		//Correr Reporte
		myReport.runReport(designName,"xls");
	}
}
