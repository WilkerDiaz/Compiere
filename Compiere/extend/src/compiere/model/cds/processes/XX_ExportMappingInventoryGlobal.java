package compiere.model.cds.processes;

import java.awt.Container;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.birt.BIRTReport;

public class XX_ExportMappingInventoryGlobal extends SvrProcess {
	
	StringBuilder query = new StringBuilder();
	private boolean errorArchivo = false; 
	private String nombreArchivo = null;
	static String select =  "SELECT * FROM XX_ProductInventoryGlobal ORDER BY Codigo_Producto,  Ubicacion, Fecha_Llegada ";
	static String [] cabecera = {	
		"Ubicacion",
        "Codigo_Producto",   
        "Nombre_Producto",
        "Referencia_Proveedor",
        "Categoria",
        "Departamento", 
        "Linea",
        "Seccion",
        "Marca",
        "Codigo_Proveedor", 
        "Nombre_Proveedor", 
        "Fecha_Llegada",
        "Inventario",
        "OC"
	};
	
	static String [] detalle = {	
		"Ubicacion",
        "Codigo_Producto",   
        "Nombre_Producto",
        "Referencia_Proveedor",
        "Categoria",
        "Departamento", 
        "Linea",
        "Seccion",
        "Marca",
        "Codigo_Proveedor", 
        "Nombre_Proveedor", 
        "Fecha_Llegada",
        "Inventario",
        "OC"
	};
	
	static boolean [] numerico = {	
		false, 		
		false,		
		false,	
		false,	
		false, 
		false,	
		false,
		false,
		false,
		false,	
		false,
		false,
		false, 
		false
	};	

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
ProcessInfoParameter[] parameter = getParameter();
		
		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();			
			if (element.getParameter() == null) ;
			else if (name.equals("File")) { 
				if (element.getParameter() != null) {					
					String extension = (String)element.getParameter();
					
					/*Agregado por GHUCHET */
					if(extension.length()>4){
						extension = extension.substring(extension.length()-4, extension.length());
					}else {
						ADialog.error(1, new Container(), "Not Excel" );
						return;
					}
					
					if (!extension.equals(".xls")) {
							ADialog.error(1, new Container(), "Not Excel" );
							return;		
					}

	
					File archivoFisico = new File((String) element.getParameter());
					if (archivoFisico.exists()) {
						ADialog.error(1, new Container(), "XX_FileExist" );					
						errorArchivo = true;
						return;
					} 
					nombreArchivo = (String) element.getParameter();
				} 
			}							
		}
		
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		
		/*String designName = "GlobalInventoryMapping";

		//Intanciar reporte e iniciar plataforma
		BIRTReport myReport = new BIRTReport();
		
		//Correr Reporte
		myReport.runReport(designName,"xls");*/
		
		
		if (errorArchivo) {
			return "";
		}
	
		XX_ExporPlacedOrderExcelProcess.cabecera = cabecera;
		XX_ExporPlacedOrderExcelProcess.detalle = detalle;
		XX_ExporPlacedOrderExcelProcess.numerico = numerico;
		
		PreparedStatement ps = null;
		ResultSet rs = null;		
		query.insert(0, select);
		try {			
			ps = DB.prepareStatement(query.toString(), null);
			rs = ps.executeQuery();
			new XX_ExporPlacedOrderExcelProcess().crearCSV(rs, nombreArchivo );
			
		} catch (SQLException e) {
			log.log(Level.SEVERE, "XX_DatabaseError" ,e);
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		return "";
	}

}
