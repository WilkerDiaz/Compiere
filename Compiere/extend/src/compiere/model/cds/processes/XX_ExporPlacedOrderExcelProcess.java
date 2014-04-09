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
public class XX_ExporPlacedOrderExcelProcess extends SvrProcess {
	
	StringBuilder query = new StringBuilder();
	private boolean errorArchivo = false; 
	private String nombreArchivo = null;
	static String select =  "SELECT * FROM XX_C_ORDER_TO_EXCEL_VIEW ";
	static String [] cabecera = {	
		"COD DEPARTAMENTO",
		"NOM DEPARTAMENTO",	
		"CODIGO LINEA",	
		"NOMBRE LINEA",       	
		"CODIGO SECCION",     	
		"NOMBRE SECCION",     	
		"NUM ORDEN COMPRA",   	
		"FECHA EST LLEGADA",  	
		"STATUS ORD COMPRA",  	
		"PEDIDO",	
		"FECHA RECEPCION N/",
		"GUIA DESPACHO",
		"FECHA GUIA DESPACHO",
		"FOLLETO",	
		"TEMPORADA",
		"CODIGO PROVEEDOR",
		"REF PROVEEDOR",
		"CARAC_PRINCIPAL",
		"NOMBRE PROVEEDOR",
		"TIENDA",
		"CODIGO PRODUCTO",
		"NOMBRE PRODUCTO",
		"CARACTERISTICA 1",
		"CARACTERISTICA 2",
		"TIPO INVENTARIO",
		"NRO PIEZAS",
		"MARCA PRODUCTO",
		"DESCRI MARCA",
		"COSTO EN DOLARES",
		"COSTO UNIT VENTA",
		"MONTO DE VENTA",
		"MARGEN SOBRE COMPRA",   //ANTES MARGEN
		"PAIS ORIGEN",
		"VIA DE DESPACHO",   
		"OBSERVACION DE LA IMPORTACION",
		"CONDICION DE EMBARQUE",
		"CONDICION DE PAGO",
		"FECHA DE ENVIO A TIENDA", //ANTES FECHA DE DESPACHO
		"COSTO UNIT. BS. A PVP",  //ANTES MONTO
		"TIPO ORDEN COMPRA",
		"TEMA",
		"COLECCION",
		"PAQUETE",
		"ESTADO PEDIDO",
		"TIPO DE DESPACHO",
		"FECHA DE CREACION",
		"CATEGORÍA",
		"FECHA CREACIÓN PEDIDO",
		"FECHA CHEQUEO OC",
		"FECHA_EST_LLEGADA_CD",
		"COMP_IMPORTADORA",
		"FECHA DESPACHO (IMPORTADAS)",  //AGREGADA	
		"COMPRA TOTAL BS.",				//AGREGADA
		"COMPRA TOTAL $/€"				//AGREGADA
		};
	
	static String [] detalle = {	
		"COD_DEPARTAMENTO", 		
		"NOM_DEPARTAMENTO",		
		"CODIGO_LINEA",	
		"NOM_LINEA",	
		"CODIGO_SECCION", 
		"NOMBRE_SECCION",	
		"NUM_ORDEN_COMPRA",
		"FECHA_EST_LLEGADA",
		"STATUS_ORD_COMPRA",
		"PEDIDO",	
		"FECHA_RECEPCION",
		"GUIA_DESPACHO",
		"FECHA_DE_DESPACHO", 
		"FOLLETO",	
		"TEMPORADA",
		"CODIGO_PROVEEDOR",
		"REF_PROVEEDOR",
		"CARAC_PRINCIPAL",
		"NOMBRE_PROVEEDOR",
		"TIENDA",
		"COD_PRODUCTO",
		"NOMBRE_PRODUCTO",
		"CARACTERISTICA_1",
		"CARACTERISTICA_2",
		"TIPO_INVENTARIO",
		"NRO_PIEZAS",
		"MARCA_PRODUCTO",
		"DESCRI_MARCA",
		"COSTO_EN_DOLARES",
		"COSTO_UNIT_VENTA",
		"MONTO_DE_VENTA",
		"MARGEN",
		"PAIS_DE_ORIGEN",
		"VIA_DE_DESPACHO",   
		"OBSERVACION_DE_LA_IMPORTACION",
		"INCOTERMS_COND_ENVIO",
		"CONDICION_DE_PAGO",
		"FECHA_DE_DESPACHO",
		"MNTO_VNTA_IMPUESTO",
		"TIPO_ORDEN_COMPRA",
		"TEMA",
		"COLECCION",
		"PAQUETE",
		"ESTADO_PEDIDO",
		"TIPO_DE_DESPACHO",
		"CREATED",
		"CATEGORIA",
		"FECHA_CREACION_PEDIDO",
		"FECHA_CHEQUEO",
		"FECHA_EST_LLEGADA_CD",
		"COMP_IMPORTADORA",
		"FECHA_DESPACHO_IMPORTADAS",	//AGREGADO
		"COMPRA_TOTAL_BS",				//AGREGADO
		"COMPRA_TOTAL_MONEDA_ORIG"};  	//AGREGADO
	
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
		true,
		true,
		true,
		true,
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
		false,	//AGREGADO
		true,	//AGREGADO
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
			else if (name.equals("XX_VMR_Category_ID")) {
				if (element.getParameter() != null) 
					if (query.length()  == 0) {
						query.append(" WHERE XX_VMR_Category_ID = " + element.getParameterAsInt());
					} else {
						query.append(" AND XX_VMR_Category_ID = " + element.getParameterAsInt());
					} 					
			} else if (name.equals("XX_VMR_Department_ID")) {
				if (element.getParameter() != null) 
					if (query.length()  == 0) {
						query.append(" WHERE XX_VMR_Department_ID = " + element.getParameterAsInt());
					} else {
						query.append(" AND XX_VMR_Department_ID = " + element.getParameterAsInt());
					}				
			} else if (name.equals("C_BPartner_ID")) {
				if (element.getParameter() != null) 
					if (query.length()  == 0) {
						query.append(" WHERE C_BPartner_ID = " + element.getParameterAsInt());
					} else {
						query.append(" AND C_BPartner_ID = " + element.getParameterAsInt());
					}					
			} else if (name.equals("C_Order_ID")) {
				if (element.getParameter() != null) 
					if (query.length()  == 0) {
						query.append(" WHERE C_Order_ID = " + element.getParameterAsInt());
					} else {
						query.append(" AND C_Order_ID = " + element.getParameterAsInt());
					}					
			} else if (name.equals("TIPO_ORDEN_COMPRA")) {
				if (element.getParameter() != null) 
					if (query.length()  == 0) {
						query.append(" WHERE TIPO_ORDEN_COMPRA = '" + element.getParameter() + "' ");
					} else {
						query.append(" AND TIPO_ORDEN_COMPRA = '" + element.getParameter() + "' ");
					}	
			} else if (name.equals("STATUS_ORD_COMPRA")) {
				if (element.getParameter() != null) 
					if (query.length()  == 0) {
						query.append(" WHERE STATUS_ORD_COMPRA = '" + element.getParameter() + "' ");
					} else {
						query.append(" AND STATUS_ORD_COMPRA = '" + element.getParameter() + "' ");
					}
			} else if (name.equals("Created")) {				
					if (element.getParameter() != null) 
						if (query.length()  == 0) 							
							query.append(" WHERE TRUNC(CREATED) >= " + DB.TO_DATE((Timestamp)element.getParameter(), true) );
						else 
							query.append(" AND TRUNC(CREATED) >= " + DB.TO_DATE((Timestamp)element.getParameter(), true) );	
					if (element.getParameter_To() != null) 
						if (query.length()  == 0) 							
							query.append(" WHERE TRUNC(CREATED) <= " + DB.TO_DATE((Timestamp)element.getParameter_To(), true) );
						else 
							query.append(" AND TRUNC(CREATED) <= " + DB.TO_DATE((Timestamp)element.getParameter_To(), true));					
			} else if (name.equals("XX_EstimatedDate")) {
				if (element.getParameter() != null) {
					
					if (query.length()  == 0)
						query.append(" WHERE ");
					else
						query.append(" AND ");
						
					query.append("(" 
							   + "(TRUNC(TO_DATE(FECHA_EST_LLEGADA)) >= " + DB.TO_DATE((Timestamp)element.getParameter(), true) + " AND TIPO_ORDEN_COMPRA = 'Nacional') "
							   + " OR "
							   + "(TRUNC(TO_DATE(XX_ESTIMATEDDATE)) >= "+ DB.TO_DATE((Timestamp)element.getParameter(), true) +" AND TIPO_ORDEN_COMPRA = 'Importada')" 
							   + ")");
				}
						
				if (element.getParameter_To() != null){
					
					if (query.length()  == 0)
						query.append(" WHERE ");
					else
						query.append(" AND ");
						
					query.append("(" 
							   + "(TRUNC(TO_DATE(FECHA_EST_LLEGADA)) <= " + DB.TO_DATE((Timestamp)element.getParameter_To(), true) + " AND TIPO_ORDEN_COMPRA = 'Nacional') "
							   + " OR "
							   + "(TRUNC(TO_DATE(XX_ESTIMATEDDATE)) <= "+ DB.TO_DATE((Timestamp)element.getParameter_To(), true) +" AND TIPO_ORDEN_COMPRA = 'Importada')" 
							   + ")");			
				}
			} else
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
							linea.append((rs.getString(detalle[i1])).trim());
										
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
				nombre
			});
			ADialog.info(1, new Container(), msg);			
			
		}  catch (FileNotFoundException fnfe) {
			log.log(Level.SEVERE, "(f) - " + fnfe.toString());
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}
}