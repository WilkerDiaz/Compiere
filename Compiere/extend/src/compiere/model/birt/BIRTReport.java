package compiere.model.birt;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;
import org.compiere.db.CConnection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class BIRTReport
{
	private static final long serialVersionUID = 1L;

	public Vector<String> parameterName = new Vector<String>();
	public Vector<Object> parameterValue = new Vector<Object>();
	String BIRTExtension = ".rptdesign";
	String serverIP = "";
	String serverPort = "9080";
	String reportDir = "report/";

	public void runReport(String designName, String format)
	{
		//Obtiene direccion ip de la base de datos
		serverIP = CConnection.get().getDbHost();
		
		String server = "http://" + serverIP + ":" + serverPort + "/birt/frameset?__locale=es_ES&__report=";
		
		try {
			
			//Agregar parámetros
			String parameters = "";
			for(int i=0; i < parameterName.size(); i++){
				parameters += "&" + parameterName.get(i);
				parameters += "=" + parameterValue.get(i);
			}
			
			//Abrir reporte en el navegador por defecto
			java.awt.Desktop.getDesktop().browse(new URI(server + reportDir + designName + BIRTExtension + parameters + "&__format=" + format));
		
		} catch (IOException e1) {

			e1.printStackTrace();
		} catch (URISyntaxException e1) {

			e1.printStackTrace();
		}
	}
	
	public File fileReport(String designName, String format)
	{	
		URL url = null;
		File report = null;
		//Obtiene direccion ip de la base de datos
		serverIP = CConnection.get().getDbHost();
		
		String server = "http://" + serverIP + ":" + serverPort + "/birt/frameset?__locale=es_ES&__report=";
		
		try {
			report = File.createTempFile(designName, "." + format);
			//Agregar parámetros
			String parameters = "";
			for(int i=0; i < parameterName.size(); i++){
				parameters += "&" + parameterName.get(i);
				parameters += "=" + parameterValue.get(i);
			}
			
			url = new URI(server + reportDir + designName + BIRTExtension + parameters + "&__format=" + format + "&__pageoverflow=0&__asattachment=true&__overwrite=false").toURL();
			InputStream webIS = url.openStream();

			FileOutputStream fo = new FileOutputStream(report);
			int c=0;
			do
			{
			    c = webIS.read();
			    //System.out.println("==============> "+c);
			    if(c!=-1) fo.write((byte)c);
			}while(c!=-1);

			webIS.close();
			fo.close();
		
		}
	 catch (IOException e1) {

			e1.printStackTrace();
		} catch (URISyntaxException e1) {

			e1.printStackTrace();
		} 
		
		return report;
	}
	
}
