package compiere.model.payments.processes;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.logging.Level;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import compiere.model.birt.BIRTReport;

public class XX_PrintARCV extends SvrProcess {

	String vendor = "";
	Integer month = 0;
	Integer year = 0;
	
	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] parameter = getParameter();
		
		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();			
			if (element.getParameter() == null) ;
			else if (name.equals("C_BPartner_ID")) { 
				 vendor = element.getParameter().toString();
			}
			else if (name.equals("Month")) {
				month = ((BigDecimal)element.getParameter()).intValue();
			}else if (name.equals("Year")) {
				year = ((BigDecimal)element.getParameter()).intValue();
			}else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	@Override
	protected String doIt() throws Exception {
		
		//Fechas
		Calendar myDate = Calendar.getInstance();
		myDate.set(year,month,1);
		myDate.add(Calendar.MONTH,11);
		
		Calendar myDate2 = Calendar.getInstance();
		myDate2.set( myDate.get(Calendar.YEAR), myDate.get(Calendar.MONTH)-1 , 1);
		
		String myMonth1 = "";
		String myMonth2 = "";
		Integer myYear2 = 0;
		if(month ==1){
			myYear2 = year;
		}else
			myYear2 = year + 1;
		
		int lastDayMonth2  = myDate2.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		if(month.toString().length()==1)
			myMonth1 = "0" + month.toString();
		else
			myMonth1 = month.toString();
		
		Integer auxMonth2 = myDate2.get(Calendar.MONTH)+1;
		
		if(auxMonth2.toString().length()==1)
			myMonth2 = "0" + auxMonth2.toString();
		else
			myMonth2 = auxMonth2.toString();
		
		String date1 = "01/" + myMonth1 + "/" + year;
		String date2 = lastDayMonth2+ "/" + myMonth2 + "/" + myYear2.toString();
		//Fin fechas
		
		//Mostrar Reporte
		generateARCVReport(vendor, date1, date2);
		
		return "";
	}
	
	/**
	 * Se encarga de generar el reporte ARCV
	 * @param vendor, id del proveedor
	 */
	public void generateARCVReport(String vendor, String date1, String date2){
		
		String designName = "ARCV";

		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("client");
		myReport.parameterValue.add(Env.getCtx().getAD_Client_ID());
		
		myReport.parameterName.add("vendor");
		myReport.parameterValue.add(vendor);
		
		myReport.parameterName.add("date1");
		myReport.parameterValue.add(date1);
		
		myReport.parameterName.add("date2");
		myReport.parameterValue.add(date2);
		
		//Correr Reporte
		myReport.runReport(designName,"pdf");
	}

}
