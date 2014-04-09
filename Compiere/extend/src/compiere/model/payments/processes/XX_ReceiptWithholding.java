package compiere.model.payments.processes;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.birt.BIRTReport;
import compiere.model.cds.Utilities;

/**
 * Se encarga de generar el reporte de comprobante de retención por mes y año
 * @author Jessica Mendoza
 *
 */
public class XX_ReceiptWithholding extends SvrProcess{

	private String month;
	private String year;
	private String fechaInicio;
	private String fechaFinal;
	
	@Override
	protected String doIt() throws Exception {

		String designName = "ReceiptWithholding"; 
		Utilities util = new Utilities();
		int mes = Integer.parseInt(month);

		month = String.valueOf(util.meses(mes));
		fechaInicio = "01/" + mes + "/" + year;
		fechaFinal = util.ultimoDiaMes(mes) + "/" + mes + "/" + year;
		
		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("fechaInicio");
		myReport.parameterValue.add(fechaInicio);
		myReport.parameterName.add("fechaFinal");
		myReport.parameterValue.add(fechaFinal);
		myReport.parameterName.add("mes");
		myReport.parameterValue.add(month);
		myReport.parameterName.add("year");
		myReport.parameterValue.add(year);

		//Correr Reporte
		myReport.runReport(designName, "pdf");
		myReport.runReport(designName, "xls");
		
		return Msg.getMsg(Env.getCtx(), "XX_ProcessComplete");	
	}

	@Override
	protected void prepare() {
		ProcessInfoParameter[] parametro = getParameter();
		for (int i = 0; i < parametro.length; i++) {
			String name = parametro[i].getParameterName();
			if (name.equals("Month"))
				month = (parametro[i].getParameter().toString());
			if (name.equals("Year"))
				year = (parametro[i].getParameter().toString());
		}
	}

}
