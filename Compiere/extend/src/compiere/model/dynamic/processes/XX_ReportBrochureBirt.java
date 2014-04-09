package compiere.model.dynamic.processes;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import compiere.model.birt.BIRTReport;


/** XX_ReportBrochureBirt (Funcion 11)
 * Proceso que permite la llamada al reporte en Birt creado para dar la vista
 * previa de la grilla.  
 * 
 * Actualizado: 17/11/2011
 * Genera reportes BIRT mediante Report Viewer ver: 2_6_1
 * 
 * @author jgraterol, mvintimilla, jtrias
 * */

public class XX_ReportBrochureBirt extends SvrProcess{
	private String format = "";
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] parameter = getParameter();
		
		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();			
			
			if(name.equalsIgnoreCase("XX_VSI_Extension")){
				
				format = (String) element.getParameter();
				
				if(!format.equalsIgnoreCase("pdf") && !format.equalsIgnoreCase("xls"))
					return;
			}
			else
				return;
		} // for parameter
	} // prepare
	
	@Override
	protected String doIt() throws Exception {
		
		String designName = "VisualizacionFolletoMarca";
		Integer brochure_ID = getRecord_ID();
		
		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("BROCHURE_ID");
		myReport.parameterValue.add(brochure_ID.toString());
		
		//Correr Reporte
		myReport.runReport(designName, format);
		
		return "El reporte se desplegará en el explorador web";
		
	}//doIt

}// XX_ReportBrochureBirt
