package compiere.model.payments.processes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import compiere.model.birt.BIRTReport;

/**
 * Proceso que se encarga de re-imprimir el reporte definitivo de la semana de pago
 * Recibiendo como parámetro la fecha en la cual se generó el definitivo, el tipo de 
 * factura (bienes, servicios, mercancía) y por último el tipo de orden que indica
 * si es nacional o importada.
 * @author Jessica Mendoza
 * 		
 *		compiere.model.payments.processes.XX_PrintReportDefinitivePP
 */
public class XX_PrintReportDefinitivePP extends SvrProcess {

	Date date = new Date();
	String invoiceType = "";
	String orderType = "";
	
	
	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] parameter = getParameter();
		
		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();			
			if (element.getParameter() == null) ;
			else if (name.equals("Date1")) { 
				 date = (Date) element.getParameter();
			}else if (name.equals("XX_InvoiceType")) {
				invoiceType = element.getParameter().toString();
			}else if (name.equals("XX_OrderType")) {
				orderType = element.getParameter().toString();
			}else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	@Override
	protected String doIt() throws Exception {		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String typeFac = "";
		typeFac = invoiceType;
		
		if(invoiceType.equals("S"))
			invoiceType = "Servicio";
		else if(invoiceType.equals("I"))
			invoiceType = "Mercancia";
		else
			invoiceType = "Bienes";
	
		//Mostrar Reporte Definitivo
		generateReportDefinitive(typeFac, sdf.format(date));
		
		//Mostrar Reporte de Especificacion de Pago
		generateReportPayment(typeFac, sdf.format(date));
		
		return "";
	}
	
	/**
	 * Se encarga de generar el reporte Definitivo
	 */
	public void generateReportDefinitive(String typeFac, String fecha){
		String designName = "ReprintDefinitive";
		
		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();

		//Agregar parametro
		myReport.parameterName.add("fechaHasta");
		myReport.parameterValue.add(fecha);
		myReport.parameterName.add("tipoFactura");
		myReport.parameterValue.add(typeFac);
		myReport.parameterName.add("tipoOrder");
		myReport.parameterValue.add(orderType);
		myReport.parameterName.add("tipofacturaTemp");
		myReport.parameterValue.add(invoiceType);
		
		//Correr Reporte
		myReport.runReport(designName,"pdf");
	}
	
	/**
	 * Se encarga de generar el reporte Especificacion de Pago
	 */
	public void generateReportPayment(String typeFac, String fecha){
		String designName = "ReprintPaymentDetail";
		
		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();

		//Agregar parametro
		myReport.parameterName.add("fechaHasta");
		myReport.parameterValue.add(fecha);
		myReport.parameterName.add("tipoFactura");
		myReport.parameterValue.add(typeFac);
		myReport.parameterName.add("tipoOrder");
		myReport.parameterValue.add(orderType);
		
		//Correr Reporte
		myReport.runReport(designName,"pdf");
	}

}
