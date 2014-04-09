package compiere.model.payments.processes;

import java.util.Calendar;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.birt.BIRTReport;
import compiere.model.cds.Utilities;
/**
 * Se encarga de generar el reporte de libro de compras por mes y año
 * @author Trinimar Acevedo
 *
 */
public class XX_ReportPurchaseBook extends SvrProcess{

	private String month;
	private String year;
	private String warehouse;
	private String fechaInicio;
	private String fechaFinal;
	
	@Override
	protected String doIt() throws Exception {

		String designName = ""; 
		int mes = Integer.parseInt(month);
		int anio = Integer.parseInt(year);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, mes-1);

		fechaInicio = "01/" + mes + "/" + year;
		fechaFinal = cal.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + mes + "/" + year;
		
		BIRTReport myReport = new BIRTReport();
		
		/**Agregar parametros**/
		if(warehouse.equals("") || warehouse.equals("00")){
			designName = "PurchaseBookConsolidated"; 
		}else{
			warehouse = "0"+warehouse;
			designName = "PurchaseBookWarehouse"; 
			myReport.parameterName.add("WAREHOUSE");
			myReport.parameterValue.add(warehouse);
		}
		myReport.parameterName.add("DATED");
		myReport.parameterValue.add(fechaInicio);
		myReport.parameterName.add("DATEH");
		myReport.parameterValue.add(fechaFinal);
		myReport.parameterName.add("MONTH");
		myReport.parameterValue.add(month);
		myReport.parameterName.add("YEAR");
		myReport.parameterValue.add(anio);

		/**Generar los reportes**/
		
		myReport.runReport(designName, "xls");
		return Msg.getMsg(Env.getCtx(), "XX_ProcessComplete");	
	}

	@Override
	/**Obtenemos los parametros del proceso**/
	protected void prepare() {
		ProcessInfoParameter[] parametro = getParameter();
		for (int i = 0; i < parametro.length; i++) {
			String name = parametro[i].getParameterName();
			if (name.equals("Month"))
				month = (parametro[i].getParameter().toString());
			if (name.equals("Year"))
				year = (parametro[i].getParameter().toString());
			if (name.equals("Warehouse"))
				warehouse = (parametro[i].getParameter().toString());
		}
	}

}
