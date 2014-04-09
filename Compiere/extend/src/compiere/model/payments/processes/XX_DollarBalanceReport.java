package compiere.model.payments.processes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import compiere.model.birt.BIRTReport;

public class XX_DollarBalanceReport extends SvrProcess {

	Calendar toDate = Calendar.getInstance();
	Calendar balanceDate = Calendar.getInstance();
	Calendar fromDate = Calendar.getInstance();
	
	@Override
	protected void prepare() {

		ProcessInfoParameter[] parameter = getParameter();
		Integer sMonth = 0;
		Integer sYear = 0;
		
		for (ProcessInfoParameter element : parameter) {
			
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("XX_Month"))
				sMonth = new Integer(element.getParameter().toString());
			else if (name.equals("XX_Year"))
				sYear = new Integer(element.getParameter().toString());
			else 
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
		sMonth = sMonth - 1;
		
		if(sMonth == 11){
					
			balanceDate.set(1, sYear);
			balanceDate.set(2, sMonth);
			toDate.set(1, sYear + 1);
			toDate.set(2, 1);
		}
		else{
			balanceDate.set(1, sYear);
			balanceDate.set(2, sMonth);
			toDate.set(1, sYear);
			toDate.set(2, sMonth + 1);
		}
				
		fromDate.setTime(balanceDate.getTime());
		fromDate.set(Calendar.DAY_OF_MONTH, 1);
		fromDate.set(Calendar.HOUR_OF_DAY, 0);
		fromDate.set(Calendar.MINUTE, 0);
		fromDate.set(Calendar.SECOND, 0);
		
		toDate.set(Calendar.DAY_OF_MONTH, 1);
		balanceDate.set(Calendar.DAY_OF_MONTH, balanceDate.getActualMaximum(Calendar.DAY_OF_MONTH));
	}

	@Override
	protected String doIt() throws Exception {

		String designName = "DollarBalance";

		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		
		//Formateo de fechas
		SimpleDateFormat df = new SimpleDateFormat();
		df.applyPattern("dd-MM-yyyy");

		
		//Agregar parametro
		myReport.parameterName.add("fromDate");
		myReport.parameterValue.add(df.format(fromDate.getTime()));
		
		myReport.parameterName.add("toDate");
		myReport.parameterValue.add(df.format(toDate.getTime()));
		
		myReport.parameterName.add("balanceDate");
		myReport.parameterValue.add(df.format(balanceDate.getTime()));
		
		
		//Correr Reporte
		myReport.runReport(designName,"xls");
		
		return "Reporte generado";
	}

}
