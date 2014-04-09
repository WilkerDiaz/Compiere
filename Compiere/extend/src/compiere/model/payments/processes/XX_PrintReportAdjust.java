package compiere.model.payments.processes;

import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.birt.BIRTReport;
import compiere.model.importcost.X_XX_VLO_SUMMARY;

/**
 * Se encarga de imprimir el reporte de los ajustes de compras y ventas 
 * a partir del número de control
 * @author Jessica Mendoza
 *
 */
public class XX_PrintReportAdjust extends SvrProcess{

	@Override
	protected String doIt() throws Exception {

		String designName = "CheckSetting"; 
		X_XX_VLO_SUMMARY summary = new X_XX_VLO_SUMMARY(Env.getCtx(), getRecord_ID(), null);
		
		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("Invoice");
		myReport.parameterValue.add(summary.getC_Invoice_ID());
		
		//Correr Reporte
		myReport.runReport(designName, "pdf");
		
		return Msg.getMsg(Env.getCtx(), "XX_ProcessComplete");
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}
	
}
