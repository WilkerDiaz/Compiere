package compiere.model.cds.processes;

import java.awt.Container;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.excel.Excel;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;


/**
 * Calcula la distribución 
 * @author Javier Pino.
 *
 */
public class XX_ExportBoardingAndOrderExcel extends SvrProcess {
	
	StringBuilder query = new StringBuilder();
	private boolean errorArchivo = false; 
	private String nombreArchivo = null;
	static String select =  "SELECT * FROM XX_BoardingGuideToExcel ";
	static String [] cabecera = {	
		"# ORDEN COMPRA",
		"# GUIA DE EMBARQUE",	
		"PROVEEDOR",	
		"PAIS",       	
		"CATEGORIA",     	
		"DEPARTAMENTO",     	
		"COLECCION",   	
		"CANTIDAD",  	
		"TIPO DE TRANSITO",  	
		"FACTOR DEFINITIVO",	
		"MONTO FACTURA PROVEEDOR",
		"COMPAÑIA IMPORTADORA",
		"FECHA ESTIMADA DE DESPACHO",
		"FECHA REAL DE DESPACHO",	
		"FECHA ESTIMADA DE ENTREGA AL AGENTE DE CARGA",
		"FECHA REAL DE ENTREGA AL AGENTE DE CARGA",
		"AGENTE DE CARGA",
		"FECHA ESTIMADA DE EMBARQUE",
		"FECHA REAL DE EMBARQUE",
		"FECHA ESTIMADA DE LLEGADA A VENEZUELA",
		"FECHA REAL DE LLEGADA A VENEZUELA",
		"FECHA ESTIMADA DE CANCELACION DE DERECHOS DE ADUANA",
		"FECHA REAL DE CANCELACION DE DERECHOS DE ADUANA",
		"AGENTE ADUANAL",
		"FECHA ESTIMADA DE LLEGADA A CD",
		"FECHA REAL DE LLEGADA A CD",
		"# FACTURA AGENTE DE ADUANA",
		"MONTO FACTURA AGENTE DE ADUANA",
		"FECHA EMISION FACTURA ADUANA",
		"FECHA RECEPCION FACTURA ADUANA",
		"# FACTURA AGENTE DE CARGA",
		"MONTO FACTURA AGENTE DE CARGA BS"
		};
	
	static String [] detalle = {	
		"ORDEN", 		
		"GUIA",		
		"PROVEEDOR",	
		"PAIS",	
		"CATEGORIA", 
		"DEPARTAMENTO",	
		"COLECCION",
		"CANTIDAD",
		"TIPO_TRANSITO",
		"FACTOR_DEFINITIVO",	
		"MONTO_FACTURA_PROVEEDOR",
		"COMPANIA_IMPORTADORA",
		"FE_ES_DESPACHO", 
		"FE_RE_DESPACHO",	
		"FE_ES_ENTREGA_AGENTE_CARGA",
		"FE_RE_ENTREGA_AGENTE_CARGA",
		"AGENTE_CARGA",
		"FE_ES_EMBARQUE",
		"FE_RE_EMBARQUE",
		"FE_ES_LLEGADA_VENEZUELA",
		"FE_RE_LLEGADA_VENEZUELA",
		"FE_ES_CANCEL_DERECHOS_ADUANA",
		"FE_RE_REGIS_CANCEL_DERE_ADUANA",
		"AGENTE_ADUANAL",
		"FECHA_ESTIMADA_LLEGADA_CD",
		"FE_RE_LLEGADA_CD",
		"NUM_FACTURA_AGENTE_ADUANA",
		"MON_FACTURA_AGENTE_ADUANA",
		"FE_EMISION_FACTURA_ADUANA",
		"FE_RECEPCION_FACTURA_ADUANA",
		"NUM_FACTURA_AGENTE_CARGA",
		"MONTO_FACTURA_AGENTE_CARGA_BS"};  	//AGREGADO
	
	static boolean [] numerico = {	
		false, 		
		false,		
		false,	
		false,	
		false, 
		false,	
		false,
		true,
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
		false,
		false,
		false,
		false,
		false,
		false,
		true,
		false,
		false,
		false,
		true};	//AGREGADO
	
	
	@Override
	protected String doIt() throws Exception {
		
		if (errorArchivo) {
			return "";
		}
		PreparedStatement ps = null;
		ResultSet rs = null;		
		query.insert(0, select);
		try {			
			ps = DB.prepareStatement(query.toString(), null);
			rs = ps.executeQuery();
			crearCSV(rs, nombreArchivo );
		} catch (SQLException e) {
			log.log(Level.SEVERE, "XX_DatabaseError" ,e);
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		return "";
	}

	@Override
	protected void prepare() {
				
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
					/*Hasta aqui agregado por GHUCHET*/
//					if (extension.indexOf(".") == -1) {
//						ADialog.error(1, new Container(), "Not Excel" );
//						errorArchivo = true;
//						return;					
//					} else {
//						extension = extension.substring(extension.indexOf("."));
//						if (!extension.equals(".xls")) {
//							ADialog.error(1, new Container(), "Not Excel" );
//							errorArchivo = true;
//							return;		
//						}
//					}	
					File archivoFisico = new File((String) element.getParameter());
					if (archivoFisico.exists()) {
						ADialog.error(1, new Container(), "XX_FileExist" );					
						errorArchivo = true;
						return;
					} 
					nombreArchivo = (String) element.getParameter();
				} 
			}
			else if (name.equals("C_ORDER_ID")) {
				if (element.getParameter() != null) 
					if (query.length()  == 0) {
						query.append(" WHERE C_ORDER_ID = " + element.getParameterAsInt());
					} else {
						query.append(" AND C_ORDER_ID = " + element.getParameterAsInt());
					} 					
			} else if (name.equals("XX_VLO_BOARDINGGUIDE_ID")) {
				if (element.getParameter() != null) 
					if (query.length()  == 0) {
						query.append(" WHERE XX_VLO_BOARDINGGUIDE_ID = " + element.getParameterAsInt());
					} else {
						query.append(" AND XX_VLO_BOARDINGGUIDE_ID = " + element.getParameterAsInt());
					}				
			} else if (name.equals("C_BPARTNER_ID")) {
				if (element.getParameter() != null) 
					if (query.length()  == 0) {
						query.append(" WHERE C_BPARTNER_ID = " + element.getParameterAsInt());
					} else {
						query.append(" AND C_BPARTNER_ID = " + element.getParameterAsInt());
					}					
			} else if (name.equals("C_COUNTRY_ID")) {
				if (element.getParameter() != null) 
					if (query.length()  == 0) {
						query.append(" WHERE C_COUNTRY_ID = " + element.getParameterAsInt());
					} else {
						query.append(" AND C_COUNTRY_ID = " + element.getParameterAsInt());
					}					
			} else if (name.equals("XX_VMR_CATEGORY_ID")) {
				if (element.getParameter() != null) 
					if (query.length()  == 0) {
						query.append(" WHERE XX_VMR_CATEGORY_ID = '" + element.getParameter() + "' ");
					} else {
						query.append(" AND XX_VMR_CATEGORY_ID = '" + element.getParameter() + "' ");
					}	
			} else if (name.equals("XX_VMR_DEPARTMENT_ID")) {
				if (element.getParameter() != null) 
					if (query.length()  == 0) {
						query.append(" WHERE XX_VMR_DEPARTMENT_ID = '" + element.getParameter() + "' ");
					} else {
						query.append(" AND XX_VMR_DEPARTMENT_ID = '" + element.getParameter() + "' ");
					}
			} else if (name.equals("XX_VMR_COLLECTION_ID")) {
				if (element.getParameter() != null) 
					if (query.length()  == 0) {
						query.append(" WHERE XX_VMR_COLLECTION_ID = '" + element.getParameter() + "' ");
					} else {
						query.append(" AND XX_VMR_COLLECTION_ID = '" + element.getParameter() + "' ");
					}
			} else if (name.equals("XX_VLO_DISPATCHROUTE_ID")) {
				if (element.getParameter() != null) 
					if (query.length()  == 0) {
						query.append(" WHERE XX_VLO_DISPATCHROUTE_ID = '" + element.getParameter() + "' ");
					} else {
						query.append(" AND XX_VLO_DISPATCHROUTE_ID = '" + element.getParameter() + "' ");
					}
			} else if (name.equals("XX_VSI_CLIENT_ID")) {
				if (element.getParameter() != null) 
					if (query.length()  == 0) {
						query.append(" WHERE XX_VSI_CLIENT_ID = '" + element.getParameter() + "' ");
					} else {
						query.append(" AND XX_VSI_CLIENT_ID = '" + element.getParameter() + "' ");
					}
			} else if (name.equals("ESTADO_ORDEN")) {
				if (element.getParameter() != null) 
					if (query.length()  == 0) {
						query.append(" WHERE ESTADO_ORDEN = '" + element.getParameter() + "' ");
					} else {
						query.append(" AND ESTADO_ORDEN = '" + element.getParameter() + "' ");
					}
			} else if (name.equals("XX_VLO_CARGOAGENT_ID")) {
				if (element.getParameter() != null) 
					if (query.length()  == 0) {
						query.append(" WHERE XX_VLO_CARGOAGENT_ID = '" + element.getParameter() + "' ");
					} else {
						query.append(" AND XX_VLO_CARGOAGENT_ID = '" + element.getParameter() + "' ");
					}
			} else if (name.equals("XX_VLO_CUSTOMAGENT_ID")) {
				if (element.getParameter() != null) 
					if (query.length()  == 0) {
						query.append(" WHERE XX_VLO_CUSTOMAGENT_ID = '" + element.getParameter() + "' ");
					} else {
						query.append(" AND XX_VLO_CUSTOMAGENT_ID = '" + element.getParameter() + "' ");
					}
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);			
		}
	}
	
	/** Crear un archivo csv dado una */
	public void crearCSV (ResultSet rs, String nombre) throws Exception {
		
		File archivo = new File(nombre);
		try {
			
			char delimitador = '\t';
			//Crear el archivo necesario
			FileWriter fw = new FileWriter (archivo, false);
			BufferedWriter writer = new BufferedWriter(fw);		
			
			//Escribir la cabecera
			StringBuffer linea = new StringBuffer();			
			for (int i = 0; i < cabecera.length; i++) {
				if (i > 0) {
					linea.append(delimitador);					
				}
				linea.append(cabecera[i]);
			}
			writer.write(linea.toString());
			writer.write(Env.NL);
			linea.setLength(0);
			
			while (rs.next()) {
				for (int i1 = 0; i1 < detalle.length; i1++) {
					if (i1 > 0) {
						linea.append(delimitador);					
					}
					if (numerico[i1]) {
						if (rs.getString(detalle[i1]) != null) {
							linea.append(DisplayType.getNumberFormat(Excel.DISPLAY_TYPE_NUMBER, Env.getLanguage(Env.getCtx())).format(rs.getBigDecimal(detalle[i1])));				
						}
					} else {
						if (rs.getString(detalle[i1]) == null)
							linea.append("");
						else 
							linea.append(rs.getString(detalle[i1]));
					}
				}
				writer.write(linea.toString());
				writer.write(Env.NL);
				linea.setLength(0);
			}
			writer.flush();
			writer.close();
			
			//El archivo fue creado
			String msg = Msg.getMsg(Env.getCtx(), "XX_FileCreated", new String [] {
				nombreArchivo
			});
			ADialog.info(1, new Container(), msg);			
			
		}  catch (FileNotFoundException fnfe) {
			log.log(Level.SEVERE, "(f) - " + fnfe.toString());
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}
}
