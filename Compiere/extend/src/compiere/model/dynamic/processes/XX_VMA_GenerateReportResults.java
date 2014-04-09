package compiere.model.dynamic.processes;

import java.awt.Container;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.excel.Excel;
import org.compiere.model.MCampaign;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.dynamic.XX_VMA_ReportResults;
import compiere.model.dynamic.X_XX_VMA_MarketingActivity;
import compiere.model.dynamic.X_XX_VMA_Season;

/** XX_VMA_GenerateReportResults (Función 1)
 * Proceso que se encarga de generar un reporte de análisis de resultados que permita 
 * evaluar el impacto de la dinámica comercial en sus distintos niveles: productos, 
 * referencias de proveedor, departamentos, categorías, líneas, secciones, grupos de 
 * productos, páginas y/o acción de mercadeo completa.
 * 
 * @author mvintimilla
 * */

public class XX_VMA_GenerateReportResults extends SvrProcess {

	// Variables parametros del proceso
	private Integer Season_ID = 0;
	private Integer Campaign_ID = 0;
	private Integer Activity_ID = 0;
	private Integer Brochure_ID = 0;
	private Integer BrochurePage_ID = 0;
	private Integer Element_ID = 0;
	private Integer Category_ID = 0;
	private Integer Department_ID = 0;
	private Integer Line_ID = 0;
	private Integer Section_ID = 0;
	
	// Variables necesarias para la creación de los items
	private String sqlWhere = "";
	private Double VentasB = 0.0; 
	private Integer VentasP = 0;
	private Double InvIniB = 0.0;
	private Integer InvIniP = 0;
	private Integer InvIniPresC = 0;
	private Integer InvFinPresC = 0;
	private Double Compras = 0.0;
	private Double PVP = 0.0;
	private Double Costo = 0.0;
	
	// Variables para la escritura del archivo
	private String nombreArchivo = "";
	private boolean errorArchivo = false;
	static String [] cabecera = {	
		"Niveles del Reporte",
		"Inv. Inicial (Bsf)",
		"Inv. Inicial (Piezas)",
		"Compras",
		"Venta (BsF)",	
		"Venta (Unidades)",	    	
		"Sell Trough (Unidades)",     	
		"Rotación",     	
		"Inversión (BsF)",   	
		"Margen (%)",	
		"GMROI"
	};
	private String Category = "";
	private String Department = "";
	private String Line = "";
	private String Section = "";
	
	@Override
	protected String doIt() throws Exception {
		Vector <XX_VMA_ReportResults> vendorResults = 
			new Vector <XX_VMA_ReportResults>();
		// Variables para la creación de consultas
		String sqlElements = "";
		String sqlUnion = " UNION ";
		String sqlGroup = "";
		String SQLResults = "";
		String SQLFechas = "";
		String productos = "";
		String SQLCompras = "";
		String SQLGroupVentas = "";
		String SQLGroupCompras = "";
		Date fechaActual = new Date();
		int mes = fechaActual.getMonth()+1;
		int anio = fechaActual.getYear()+1900;
		String SQLVentas = "";
		
		/** Consulta para obtener toda la información de necesaria y devolverla
		 * en el reporte de análisis de resultados */
		sqlElements = " SELECT S.XX_VMA_Season_ID Temporada, " +
					" S.NAME NOMTEM, " +
					" C.C_Campaign_ID Coleccion, " +
					" C.NAME NOMCAM, " +
					" TO_CHAR(M.STARTDATE, 'MM') MES, " +
					" TO_CHAR(M.STARTDATE, 'YYYY') ANIO," +
					" M.XX_VMA_MarketingActivity_ID Actv_Mercadeo, " +
					" M.NAME NOMACT, " +
					" B.XX_VMA_Brochure_ID Folleto, " +
					" B.NAME NOMBRE," +
					" BP.XX_VMA_BrochurePage_ID Pagina, " +
					" BP.NAME NOMPAG, " +
					" E.XX_VME_Element_ID Elementos, " +
					" P.VALUE||'-'||E.NAME NOMELE, " +
					" P.M_Product_ID Producto," +
					" D.XX_VMR_Category_ID Categoria, " +
					" E.XX_VMR_Department_ID Dpto, " +
					" E.XX_VMR_Line_ID Linea, " +
					" E.XX_VMR_Section_ID Seccion," +
					" VPR.XX_VMR_VendorProdRef_ID Ref_Prove," +
					" 'P' Tipo" +
					" FROM XX_VMA_Season S Inner Join C_Campaign C on " +
					" (S.XX_VMA_Season_ID = C.XX_VMA_Season_ID) " +
					" INNER JOIN XX_VMA_MarketingActivity M on " +
					" (M.C_Campaign_ID = C.C_Campaign_ID) " +
					" INNER JOIN XX_VMA_Brochure B on " +
					" (M.XX_VMA_Brochure_ID = B.XX_VMA_Brochure_ID) " +
					" INNER JOIN XX_VMA_BrochurePage BP on " +
					" (BP.XX_VMA_Brochure_ID = B.XX_VMA_Brochure_ID) " +
					" INNER JOIN XX_VME_Element E on " +
					" (E.XX_VMA_BrochurePage_ID = BP.XX_VMA_BrochurePage_ID) " +
					" INNER JOIN M_Product P on " +
					" (E.M_Product_ID = P.M_Product_ID) " +
					" INNER JOIN XX_VMR_VendorProdRef VPR on " +
					" (VPR.XX_VMR_VendorProdRef_ID = P.XX_VMR_VendorProdRef_ID)" +
					" INNER JOIN XX_VMR_Department D on " +
					" (E.XX_VMR_Department_ID = D.XX_VMR_Department_ID)" +
					" INNER JOIN xx_vmr_category ct on " +
					" (d.xx_vmr_category_id= ct.xx_vmr_category_id) " +
					" INNER JOIN xx_vmr_line l on " +
					" (d.xx_vmr_department_id=l.xx_vmr_department_id) " +
					" INNER JOIN xx_vmr_section st on " +
					" (st.xx_vmr_line_id= l.xx_vmr_line_id)";
		sqlElements += sqlWhere;
		sqlElements += "AND E.ISACTIVE = 'Y' ";
		sqlGroup = " SELECT S.XX_VMA_Season_ID Temporada, " +
					" S.NAME NOMTEM, " +
					" C.C_Campaign_ID Coleccion, " +
					" C.NAME NOMCAM, " +
					" TO_CHAR(M.STARTDATE, 'MM') MES, " +
					" TO_CHAR(M.STARTDATE, 'YYYY') ANIO," +
					" M.XX_VMA_MarketingActivity_ID Actv_Mercadeo, " +
					" M.NAME NOMACT, " +
					" B.XX_VMA_Brochure_ID Folleto, " +
					" B.NAME NOMBRE," +
					" BP.XX_VMA_BrochurePage_ID Pagina, " +
					" BP.NAME NOMPAG, " +
					" G.XX_VME_GroupOfProducts_ID Elementos, " +
					" P.VALUE||'-'||G.NAME2 NOMELE, " +
					" P.M_Product_ID Producto," +
					" D.XX_VMR_Category_ID Categoria, " +
					" G.XX_VMR_Department_ID Dpto," +
					" G.XX_VMR_Line_ID Linea, " +
					" G.XX_VMR_Section_ID Seccion," +
					" VPR.XX_VMR_VendorProdRef_ID Ref_Prove," +
					" 'G' Tipo" +
					" FROM XX_VMA_Season S Inner Join C_Campaign C " +
					" on (S.XX_VMA_Season_ID = C.XX_VMA_Season_ID) " +
					" INNER JOIN XX_VMA_MarketingActivity M " +
					" on (M.C_Campaign_ID = C.C_Campaign_ID) " +
					" INNER JOIN XX_VMA_Brochure B " +
					" on (M.XX_VMA_Brochure_ID = B.XX_VMA_Brochure_ID) " +
					" INNER JOIN XX_VMA_BrochurePage BP " +
					" on (BP.XX_VMA_Brochure_ID = B.XX_VMA_Brochure_ID) " +
					" INNER JOIN XX_VME_Element E " +
					" on (E.XX_VMA_BrochurePage_ID = BP.XX_VMA_BrochurePage_ID) " +
					" INNER JOIN XX_VME_GroupOfProducts G " +
					" on (E.XX_VME_Element_ID = G.XX_VME_Element_ID) " +
					" INNER JOIN M_Product P " +
					" on (G.M_Product_ID = P.M_Product_ID) " +
					" INNER JOIN XX_VMR_VendorProdRef VPR " +
					" on (VPR.XX_VMR_VendorProdRef_ID = P.XX_VMR_VendorProdRef_ID) " +
					" INNER JOIN XX_VMR_Department D " +
					" on (G.XX_VMR_Department_ID = D.XX_VMR_Department_ID)" +
					" INNER JOIN xx_vmr_category ct " +
					" on (d.xx_vmr_category_id= ct.xx_vmr_category_id) " +
					" INNER JOIN xx_vmr_line l " +
					" on (d.xx_vmr_department_id=l.xx_vmr_department_id) " +
					" INNER JOIN xx_vmr_section st " +
					" on (st.xx_vmr_line_id= l.xx_vmr_line_id)";
		sqlGroup += sqlWhere;
		sqlGroup += "AND G.ISACTIVE = 'Y' ";
		SQLResults += sqlElements + " \n" + sqlUnion + " \n" + sqlGroup;
		System.out.println("SQL Results: " + SQLResults);

		/** Se crea el vector con la informacion obtenida de la consulta anterior 
		 * para inicializar los valores que serán presentados en el reporte */
		PreparedStatement psQueryResults = null;
		ResultSet rsQueryResults = null; 
		try{
			psQueryResults = DB.prepareStatement(SQLResults, null);
			rsQueryResults = psQueryResults.executeQuery();
			while(rsQueryResults.next()){	
				XX_VMA_ReportResults resultados = 
					new XX_VMA_ReportResults(
						(Integer)rsQueryResults.getInt("Temporada"), 			// Temporada ID
						(Integer)rsQueryResults.getInt("Coleccion"),			// Campaña ID
						(Integer)rsQueryResults.getInt("Actv_Mercadeo"),		// AM ID
						(Integer)rsQueryResults.getInt("Folleto"),				// Folleto ID
						(Integer)rsQueryResults.getInt("Pagina"),				// Pagina ID
						(Integer)rsQueryResults.getInt("Elementos"),			// Elemento ID
						(Integer)rsQueryResults.getInt("Producto"),				// Producto ID
						(Integer)rsQueryResults.getInt("Categoria"),			// Categoria ID
						(Integer)rsQueryResults.getInt("Dpto"),					// Departamento ID
						(Integer)rsQueryResults.getInt("Linea"),				// Linea ID
						(Integer)rsQueryResults.getInt("Seccion"), 				// Seccion ID
						VentasB,												// Ventas en BsF
						VentasP,												// Ventas en Piezas
						InvIniB,  												// Inventario Inicial en BsF
						InvIniP, 												// Inventario Inicial en Piezas
						InvIniPresC, 											// Inventario Inicial Pres en BsF
						InvFinPresC, 											// Inventario Final Pres en BsF
						Compras, 												// Compras
						PVP, 													// PVP
						Costo, 													// Costo
						rsQueryResults.getInt("MES"), 							// Mes Inicio AM
						rsQueryResults.getInt("ANIO"), 							// Año Inicio AM
						rsQueryResults.getString("NOMTEM"),						// Nombre Temporada
						rsQueryResults.getString("NOMCAM"),						// Nombre Categoria
						rsQueryResults.getString("NOMACT"),						// Nombre AM
						rsQueryResults.getString("NOMBRE"), 					// Nombre Folleto
						rsQueryResults.getString("NOMPAG"),						// Nombre Pagina
						rsQueryResults.getString("NOMELE"),						// Nombre Elemento
						"", 													// Nombre Categoria
						"", 													// Nombre Departamento
						"", 													// Nombre Linea
						"", 													// Nombre Seccion 
						rsQueryResults.getString("TIPO"));						// Nombre Tipo
				vendorResults.add(resultados);
				
				/** Variable donde se guardan los distintos ID de los productos
				  * para consultas posteriores de compra y venta de los mismos */
				productos += rsQueryResults.getString("Producto");
				productos += ",";				
			}//WHILE		
		}//try		
		catch(SQLException e){	
			e.printStackTrace();
		}	
		finally {
			rsQueryResults.close();
			psQueryResults.close();
		}
		
		// Código de Productos que serán utilizados en los querys
		if (productos.length() > 7)
			productos = productos.substring(0, productos.length()-1);
		// Si no se trae productos entonces se coloca cero
		if(productos.length() < 1)
			productos += "0";

		/** Consulta para obtener el monto de las ventas y precio de venta 
		 * (priceactual que incluye los descuentos), piezas vendidas.
		 * Se requiere el monto en Bsf de las ventas y en unidades. */
		SQLVentas = " SELECT L.M_PRODUCT_ID PRODUCTO, " +
				" L.LINENETAMT AMT, " +
				" L.QTYENTERED CANTIDAD, " +
				" L.PRICEACTUAL PRICE " +
				" FROM C_ORDERLINE L INNER JOIN C_ORDER C " +
				" ON (L.C_ORDER_ID = C.C_ORDER_ID)" +
				" WHERE L.M_PRODUCT_ID IN ("+ productos +") " +
				" AND C.ISSOTRX = 'Y' " ;
		
		SQLFechas = verifyDates(0);
		SQLGroupVentas = " ORDER BY L.M_PRODUCT_ID ASC";
		SQLVentas += SQLFechas + SQLGroupVentas;
		//System.out.println("SQL Ventas: "+SQLVentas);
		
		PreparedStatement psQueryVentas = null;
		ResultSet rsQueryVentas = null;
		
		try{
			psQueryVentas = 
				DB.prepareStatement(SQLVentas, null);
			rsQueryVentas = psQueryVentas.executeQuery();
			while(rsQueryVentas.next()){	
				for(int i = 0; i < vendorResults.size(); i++){
					if(vendorResults.get(i).Product_ID != 
						rsQueryVentas.getInt("PRODUCTO")){
						continue;
					}
					else {
						vendorResults.get(i).VentasB = vendorResults.get(i).VentasB +
							rsQueryVentas.getDouble("AMT");
						vendorResults.get(i).VentasP = vendorResults.get(i).VentasP +
							rsQueryVentas.getInt("CANTIDAD");
						vendorResults.get(i).PVP = vendorResults.get(i).PVP +
							rsQueryVentas.getDouble("PRICE");
					}//if codigo
					break;
				}//for
			}//WHILE		
		}//try		
		catch(SQLException e){	
			e.printStackTrace();
		}	
		finally {
			rsQueryVentas.close();
			psQueryVentas.close();
		}
		
		/** Consulta para obtener las compras, se calcula el costo del producto y
		 * las cantidades reales */
		SQLCompras = " SELECT L.M_PRODUCT_ID PRODUCTO, " +
					" L.PRICEACTUAL PRICE, " +
					" M.MOVEMENTQTY QTY " +
					" FROM C_ORDERLINE L INNER JOIN C_ORDER C " +
					" ON (L.C_ORDER_ID = C.C_ORDER_ID)" +
					" INNER JOIN M_INOUTLINE M " +
					" ON (L.C_ORDERLINE_ID = M.C_ORDERLINE_ID)" +
					" WHERE L.M_PRODUCT_ID IN ("+ productos +") " +
					" AND C.ISSOTRX = 'N' ";
		
		SQLFechas = verifyDates(1); 
		SQLGroupCompras = " ORDER BY L.M_PRODUCT_ID ASC";
		SQLCompras += SQLFechas + SQLGroupCompras;
		//System.out.println("SQL compras: "+SQLCompras);
		
		PreparedStatement psQueryCompras = null;
		ResultSet rsQueryCompras = null;
		
		try{
			psQueryCompras = DB.prepareStatement(SQLCompras, null);
			rsQueryCompras = psQueryCompras.executeQuery();
			while(rsQueryCompras.next()){	
				for(int i = 0; i < vendorResults.size(); i++){
					if(vendorResults.get(i).Product_ID != 
						rsQueryCompras.getInt("PRODUCTO")){
						continue;
					}
					else {
						vendorResults.get(i).Compras = 
							rsQueryCompras.getDouble("PRICE") *
							rsQueryCompras.getInt("QTY");
						vendorResults.get(i).Costo = 
							vendorResults.get(i).Costo +
							rsQueryCompras.getDouble("PRICE");
					}//if codigo
					break;
				}//for
			}//WHILE		
		}//try		
		catch(SQLException e) {	
			e.printStackTrace();
		}	
		finally {
			rsQueryCompras.close();
			psQueryCompras.close();
		}
		
		/** Obtener el inventario del producto. Se requiere el inventario inicial
		 * en Bsf y en piezas. */
		for(int i = 0; i < vendorResults.size(); i++){
			obtainInventory(vendorResults.get(i).Mes, vendorResults.get(i).Anio, 
					vendorResults.get(i).Product_ID, vendorResults.get(i).Activity_ID,
					vendorResults.get(i).Element_ID, vendorResults, vendorResults.get(i).Tipo);
		} // for
		
		/** Obtener el nombre de la Categoria para escribirlo en el reporte */
		if(!Category_ID.equals(0)){
			String Categoria = "SELECT NAME " +
					" FROM XX_VMR_CATEGORY " +
					" WHERE XX_VMR_CATEGORY_ID = " + Category_ID;
			
			PreparedStatement psQueryCat = null;
			ResultSet rsQueryCat = null;
			try{
				psQueryCat = 
					DB.prepareStatement(Categoria, null);
				rsQueryCat = psQueryCat.executeQuery();
				while(rsQueryCat.next()){	
					Category = rsQueryCat.getString("NAME");
				}//WHILE		
			}//try		
			catch(SQLException e) {	
				e.printStackTrace();
			}	
			finally {
				rsQueryCat.close();
				psQueryCat.close();
			}
		}// Categoria
		
		/** Obtener el nombre del Departamento para escribirlo en el reporte */
		if(!Department_ID.equals(0)){
			String Departamento = "SELECT NAME  " +
					" FROM XX_VMR_DEPARTMENT " +
					" WHERE XX_VMR_DEPARTMENT_ID = " + Department_ID;
			PreparedStatement psQueryDep = null;
			ResultSet rsQueryDep = null;
			try{
				psQueryDep = DB.prepareStatement(Departamento, null);
				rsQueryDep = psQueryDep.executeQuery();
				while(rsQueryDep.next()){	
					Department = rsQueryDep.getString("NAME");
				}//WHILE		
			}//try		
			catch(SQLException e) {	
				e.printStackTrace();
			}	
			finally {
				rsQueryDep.close();
				psQueryDep.close();
			}
		}// Departamento
		
		/** Obtener el nombre de la Linea para escribirlo en el reporte */
		if(!Line_ID.equals(0)){
			String Linea = "SELECT NAME " +
					" FROM XX_VMR_LINE " +
					" WHERE XX_VMR_LINE_ID = " + Line_ID;
			PreparedStatement psQueryLin = null;
			ResultSet rsQueryLin = null;
			
			try{
				psQueryLin = DB.prepareStatement(Linea, null);
				rsQueryLin = psQueryLin.executeQuery();
				while(rsQueryLin.next()){	
					Line = rsQueryLin.getString("NAME");
				}//WHILE		
			}//try		
			catch(SQLException e) {	
				e.printStackTrace();
			}	
			finally {
				rsQueryLin.close();
				psQueryLin.close();
			}
		}// Línea
		
		/** Obtener el nombre de la Sección para escribirlo en el reporte */
		if(!Section_ID.equals(0)){
			String Seccion = "SELECT NAME " +
					" FROM XX_VMR_SECTION " +
					" WHERE XX_VMR_SECTION_ID = " + Section_ID;
			PreparedStatement psQuerySec = null;
			ResultSet rsQuerySec = null;
			try{
				psQuerySec = DB.prepareStatement(Seccion, null);
				rsQuerySec = psQuerySec.executeQuery();
				while(rsQuerySec.next()){	
					Section = rsQuerySec.getString("NAME");
				}//WHILE
			}//try		
			catch(SQLException e) {	
				e.printStackTrace();
			}	
			finally{
				rsQuerySec.close();
				psQuerySec.close();
			}
		}// Línea
		
		/** Se crea el reporte con la informacion recabada */
		crearCSV (vendorResults, nombreArchivo);
		
		return "";

	}// Fin doIt
	
	/** verifyDates
	 * Verifica que las fechas estén entre los límites de tiempo dependiendo del
	 * nivel del reporte
	 * @param type Tipo de verificación, 0 o/c 1 pedidos
	 * @return Fechas SQL con la condición para la consulta general con las fechas
	 * configuradas de acuerdo a la informacion solicitada
	 * */
	public String verifyDates(int type){
		String Fechas = "";
		//Date's From and To
		Timestamp dateTo = null;
		Timestamp dateFrom = null;
		Calendar newDate = Calendar.getInstance();
		Date aux = newDate.getTime();
		Timestamp dateActual = new Timestamp(aux.getTime());
		
		/** El usuario selecciona temporada solamente */
		if(Campaign_ID.equals(0) && Category_ID.equals(0)){
			X_XX_VMA_Season Temporada = 
				new X_XX_VMA_Season(Env.getCtx(), Season_ID, null);
			dateFrom = Temporada.getStartDate();
			dateTo = Temporada.getEndDate();
			Fechas = AuxDates(dateTo, dateFrom, dateActual, type);
		} // Fechas Temporada
		
		/** El usuario selecciona hasta la campaña */
		if (!Campaign_ID.equals(0) && Activity_ID.equals(0)){
			MCampaign Campaña = new MCampaign(Env.getCtx(), Campaign_ID, null);
			dateFrom = Campaña.getStartDate();
			dateTo = Campaña.getEndDate();
			Fechas = AuxDates(dateTo, dateFrom, dateActual, type);
		}// Fechas Campaña
		
		/** El usuario selecciona hasta la actividad de mercadeo */
		if (!Activity_ID.equals(0)){
			X_XX_VMA_MarketingActivity Actividad = 
				new X_XX_VMA_MarketingActivity(Env.getCtx(), Activity_ID, null);
			dateFrom = Actividad.getStartDate();
			dateTo = Actividad.getEndDate();
			Fechas = AuxDates(dateTo, dateFrom, dateActual, type);
		}// Fechas Actividad Mercadeo		
		
		return Fechas;
	}//verifyDates
	
	/** AuxDates
	 * Obtiene la parte del query que se refiere a las fechas para poder 
	 * realizar las búsquedas respectivas de acuerdo al tipo y el nivel 
	 * del reporte a generar
	 * @param dateTo Fecha Desde
	 * @param dateFrom Fecha Hasta
	 * @param dateActual Fecha Actual
	 * @param type Tipo de consulta (0 o/c 1 pedidos)
	 * @return Fechas SQL con la condición para el query general con las fechas
	 * configuradas de acuerdo a la informacion solicitada
	 * */
	public String AuxDates(Timestamp dateTo, Timestamp dateFrom, Timestamp 
			dateActual, int type){
		String SQL = "";
		/** La consulta se hace despues de la fecha de finalización */
		if(dateTo.after(dateActual)){
			if(type != 1){
				SQL = " AND TRUNC(L.DATEORDERED) >= "+ DB.TO_DATE(dateFrom)+
						" AND TRUNC(L.DATEORDERED) <= "+ DB.TO_DATE(dateActual);
			}
			else {
				SQL = " AND TRUNC(C.XX_ESTIMATEDDATE) >= "+ DB.TO_DATE(dateFrom)+
						" AND TRUNC(C.XX_ESTIMATEDDATE) <= "+ DB.TO_DATE(dateActual);
			}
		} // dateTo
		/** La consulta se hace antes de la fecha de finalización */
		else if (dateTo.before(dateActual)){
			if (type != 1){
				SQL = " AND TRUNC(L.DATEORDERED) >= "+ DB.TO_DATE(dateFrom)+
						" AND TRUNC(L.DATEORDERED) <= "+ DB.TO_DATE(dateTo);
			}
			else {
				SQL = " AND TRUNC(C.XX_ESTIMATEDDATE) >= "+ DB.TO_DATE(dateFrom)+
						" AND TRUNC(C.XX_ESTIMATEDDATE) <= "+ DB.TO_DATE(dateTo);
			}
		} // else 
		return SQL;
	} // AuxDates
	
	/** obtainInventory
	 * Obtener el inventario de acuerdo a los productos, mes y anio 
	 * para configurar el vector de resultados (monto inicial en piezas,
	 * monto inicial en BsF, cantidad final en piezas)
	 * @param mes Mes para consultar el inventario
	 * @param anio Año para consultar el inventario
	 * @param producto Identificador del producto 
	 * @param actividad Actividad de Mercadeo
	 * @elemento elemento Elemento a consultar
	 * @param vendorResults Vector con los items del Reporte
	 * */
	public void obtainInventory(int mes, int anio, Integer Product_ID, 
			Integer Activity_ID, Integer Element_ID, 
			Vector <XX_VMA_ReportResults> vendorResults, String Tipo){
		String SQLInventario = "";
		
		SQLInventario = obtainSQL(Product_ID, Activity_ID, mes, anio, Element_ID, Tipo);			
		PreparedStatement psQueryInventario = null;
		ResultSet rsQueryInventario = null;
		try{
			psQueryInventario = DB.prepareStatement(SQLInventario, null);
			rsQueryInventario = psQueryInventario.executeQuery();
			/*while*/if(rsQueryInventario.next()){	
				for(int i = 0; i < vendorResults.size(); i++){
					if(vendorResults.get(i).Product_ID != 
						rsQueryInventario.getInt("PRODUCTO")){
						continue;
					}
					else {
						vendorResults.get(i).InvIniB = 
							rsQueryInventario.getDouble("MONTOINI");
						vendorResults.get(i).InvIniP = 
							rsQueryInventario.getInt("CANTIDADINI");
						vendorResults.get(i).InvFinPresC = 
							rsQueryInventario.getInt("CANTIDADFIN");
					}//if codigo
					break;
				}//for
			}//WHILE		
		}//try		
		catch(SQLException e){	
			e.printStackTrace();
		}
		finally {
			try {
				rsQueryInventario.close();
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				psQueryInventario.close();
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		} // finally
	}//obtainInventory
	
	/** obtainSQL
	 * Se obtiene el sql correspondiente de acuerdo a las fechas de la actividad 
	 * de mercadeo y el tipo de elemento (Producto, grupo de productos o producto
	 * sin codigo). Cada query recoge información de sus columnas */
	public String obtainSQL (Integer Product_ID, Integer Activity_ID,
			int mes, int anio, Integer Element_ID, String Tipo){
		String sqlResult = "";
		Timestamp dateTo = null;
		Calendar newDate = Calendar.getInstance();
		Date aux = newDate.getTime();
		Timestamp dateActual = new Timestamp(aux.getTime());
		
		/** Se verifica la fecha de la Actividad de Mercadeo para poder proceder 
		 * con el cálculo del inventario */
		X_XX_VMA_MarketingActivity Actividad = 
			new X_XX_VMA_MarketingActivity(Env.getCtx(), Activity_ID, null);
		
		/** La Consulta se hace antes del final de la actividad */
		if (dateActual.after(Actividad.getStartDate()) &&  
				dateActual.compareTo(Actividad.getEndDate()) < 0){
			/** Query para obtener las cantidades y monto del inventario 
			 * cuando la consulta se realiza el día de cierre de la AM o antes.
			 * La cantidad final del inventario se calcula para la fecha */
			if(Tipo.equals("P")){
				sqlResult = " SELECT I.M_PRODUCT_ID PRODUCTO, " +
							" E.XX_VMA_INITIALINVENTORY CANTIDADINI, " +
							" E.XX_VMA_INITIALAMOUNT MONTOINI, " +
							" SUM(XX_INITIALINVENTORYQUANTITY + XX_SHOPPINGQUANTITY - " +
							" XX_SALESQUANTITY + XX_MOVEMENTQUANTITY + XX_ADJUSTMENTSQUANTITY" +
							" + XX_PREVIOUSADJUSTMENTSQUANTITY) AS CANTIDADFIN " +
							" FROM XX_VCN_INVENTORY I JOIN XX_VME_ELEMENT E " +
							" ON (I.M_PRODUCT_ID = E.M_PRODUCT_ID)" +
							" WHERE I.M_PRODUCT_ID = "+ Product_ID +
							" AND XX_INVENTORYMONTH = "+ mes +
							" AND XX_INVENTORYYEAR = "+ anio+
							" GROUP BY I.M_PRODUCT_ID, E.XX_VMA_INITIALINVENTORY, E.XX_VMA_INITIALAMOUNT " +
							" ORDER BY I.M_PRODUCT_ID ASC";
				//System.out.println("SQL Prod: "+sqlResult);
			} // Producto
			if(Tipo.equals("G")){
				sqlResult = " SELECT I.M_PRODUCT_ID PRODUCTO, " +
							" G.XX_VMA_INITIALINVENTORY CANTIDADINI, " +
							" G.XX_VMA_INITIALAMOUNT MONTOINI, " +
							" SUM(XX_INITIALINVENTORYQUANTITY + XX_SHOPPINGQUANTITY - " +
							" XX_SALESQUANTITY + XX_MOVEMENTQUANTITY + XX_ADJUSTMENTSQUANTITY" +
							" + XX_PREVIOUSADJUSTMENTSQUANTITY) AS CANTIDADFIN " +
							" FROM XX_VCN_INVENTORY I JOIN XX_VME_GROUPOFPRODUCTS G " +
							" ON (I.M_PRODUCT_ID = G.M_PRODUCT_ID)" +
							" WHERE I.M_PRODUCT_ID = "+ Product_ID +
							" AND XX_INVENTORYMONTH = "+ mes +
							" AND XX_INVENTORYYEAR = "+ anio+
							" GROUP BY I.M_PRODUCT_ID, G.XX_VMA_INITIALINVENTORY, G.XX_VMA_INITIALAMOUNT " +
							" ORDER BY I.M_PRODUCT_ID ASC";
				//System.out.println("SQL Grupo: "+sqlResult);
			} // Grupo			
		}// Antes de la fecha final
		/** La Consulta se hace después del final de la actividad */
		else if (dateActual.compareTo(Actividad.getEndDate()) >= 0){
			/** Query para obtener las cantidades y monto del inventario 
			 * cuando la consulta se realiza después del día de cierre de la 
			 * actividad de mercadeo. Todas las cantidades se obtienen de las
			 * columnas de las tablas que ya tienen valores */
			if(Tipo.equals("P")){
				sqlResult = " SELECT " +
							" E.M_PRODUCT_ID PRODUCTO, " +
							" E.XX_VMA_INITIALINVENTORY CANTIDADINI, " +
							" E.XX_VMA_FINALINVENTORY CANTIDADFIN, " +
							" E.XX_VMA_INITIALAMOUNT MONTOINI " +
							" FROM XX_VME_ELEMENT E " + 
							" WHERE E.M_PRODUCT_ID = " + Product_ID;
				//System.out.println("SQL Prod2: "+sqlResult);
			} // Producto
			if(Tipo.equals("G")){
				sqlResult = " SELECT " +
							" G.M_PRODUCT_ID PRODUCTO, " +
							" G.XX_VMA_INITIALINVENTORY CANTIDADINI, " +
							" G.XX_VMA_FINALINVENTORY CANTIDADFIN, " +
							" G.XX_VMA_INITIALAMOUNT MONTOINI " +
							" FROM  XX_VME_GroupOfProducts G " +
							" WHERE G.M_PRODUCT_ID =" + Product_ID;
				//System.out.println("SQL Grupo2: "+sqlResult);
			} // Grupo de productos						
		}// Despues de la fecha final
		return sqlResult;
	} //obtainSQL
	
	/** Crear un archivo csv
	 * Adaptado del proceso XX_ExporPlacedOrderExcelProcess 
	 * @param items Vector con los items que iran al reporte
	 * @param nombre Nombre del archivo a generar
	 */
	public void crearCSV (Vector<XX_VMA_ReportResults> items, String nombre) 
						throws Exception {
		Vector Cat = new Vector();
		Vector Dep = new Vector();
		Vector Lin = new Vector();
		Vector Sec = new Vector();
		Vector Tem = new Vector();
		File archivo = new File(nombre);
		BigDecimal STUni = new BigDecimal(0);
		Double Rotacion = 0.0;
		Double GMROI = 0.0;
		Double MargenP = 0.0;
		Double MargenB = 0.0;
		Double Inversion = 0.0;
		Double InvIniB = 0.0;
		Integer InvIniP = 0;
		Double Compras = 0.0;
		Double VentasB = 0.0; 
		Integer VentasP = 0;
				
		try {
			char delimitador = '\t';
			
			//Crear el archivo necesario
			FileWriter fw = new FileWriter (archivo, false);
			BufferedWriter writer = new BufferedWriter(fw);		
			
			/** Se escribe la cabecera */
			StringBuffer linea = new StringBuffer();			
			for (int i = 0; i < cabecera.length; i++) {
				if (i > 0) {
					linea.append(delimitador);					
				}
				linea.append(cabecera[i]);
			}// for
			
			writer.write(linea.toString());
			writer.write(Env.NL);
			linea.setLength(0);
			
			/** Se calculan los totales de acuerdo al nivel del reporte */
			if (!Season_ID.equals(0)){
				Tem = XX_VMA_ReportResults.obtainValues(items, 1, Season_ID, null);
			} // Temporada
			if (!Category_ID.equals(0)){
				Cat = XX_VMA_ReportResults.obtainValues(items, 7, Category_ID, null);
			} // Categoría
			if (!Department_ID.equals(0)){
				Dep = XX_VMA_ReportResults.obtainValues(items, 8, Department_ID, null);
			} // Departamento
			if (!Line_ID.equals(0)){
				Lin = XX_VMA_ReportResults.obtainValues(items, 9, Line_ID, null);
			} // Linea
			if (!Section_ID.equals(0)){
				Sec = XX_VMA_ReportResults.obtainValues(items, 10, Section_ID, null);
			} // Seccion
			
			/** Se empiezan a Escribir los detalles del folleto. Las primeras
			 * filas corresponderan al nivel del reporte que se haya especificado */
			for(int i = 0; i < items.size(); i++){
				// Analisis por Cat/Dep/Lin/Sec
				//Double InvIniB, Integer InvIniP, Double Compras, Double VentasB, Integer VentasP
				if (!Category_ID.equals(0) && (i==0)){
					linea.append("Categoría: "+ Category);
					calculateValues(linea, delimitador, writer, 
							STUni,Rotacion, GMROI, MargenP, MargenB, Inversion, 
							7, (Double)Cat.get(0), (Integer)Cat.get(1), 
							(Double)Cat.get(2), (Double)Cat.get(3), 
							(Integer)Cat.get(4), "");
				} // if categoria	
				
				if ((i==0) && !Department_ID.equals(0)){
					linea.append("Departamento: "+ Department);
					calculateValues(linea, delimitador, writer, 
							STUni, Rotacion, GMROI, MargenP, MargenB, Inversion, 
							8, (Double)Dep.get(0), (Integer)Dep.get(1), 
							(Double)Dep.get(2), (Double)Dep.get(3), 
							(Integer)Dep.get(4), "");
				} // if departamento
				
				if ((i==0) && !Line_ID.equals(0)){
					linea.append("Línea: "+ Line);
					calculateValues(linea, delimitador, writer, 
							STUni, Rotacion, GMROI, MargenP, MargenB, Inversion, 
							9, (Double)Lin.get(0), (Integer)Lin.get(1), 
							(Double)Lin.get(2), (Double)Lin.get(3), 
							(Integer)Lin.get(4), "");
				} // if linea
				
				if ((i==0)&& !Section_ID.equals(0)){
					linea.append("Sección: "+ Section);
					calculateValues(linea, delimitador, writer, 
							STUni, Rotacion, GMROI, MargenP, MargenB, Inversion, 
							10, (Double)Sec.get(0), (Integer)Sec.get(1), 
							(Double)Sec.get(2), (Double)Sec.get(3), 
							(Integer)Sec.get(4), "");
				} // if seccion
				
				if (i==0 && !Season_ID.equals(0)){
					// Nombre Temporada
					linea.append("Temporada: "+ items.get(i).NombreTemporada);
					calculateValues(linea, delimitador, writer, 
							STUni, Rotacion, GMROI, MargenP, MargenB, Inversion, 
							1, (Double)Tem.get(0), (Integer)Tem.get(1), 
							(Double)Tem.get(2), (Double)Tem.get(3), 
							(Integer)Tem.get(4), "");
				}//if temporada

				if ((i==0) || (!items.get(i).NombreCampana.
						equals(items.get(i-1).NombreCampana) )){
					Integer CampaignID = items.get(i).Campaign_ID;
					// Nombre Campaña
					linea.append("Campaña: "+ items.get(i).NombreCampana);
					Vector temp = new Vector();
					temp = XX_VMA_ReportResults.obtainValues(items, 2, CampaignID, items.get(i));
					//System.out.println("Campaña: "+(Double)temp.get(0)+" "+(Integer)temp.get(1)+" "+(Double)temp.get(2)+" "+(Double)temp.get(3)+" "+(Integer)temp.get(4));
					calculateValues(linea, delimitador, writer, STUni, 
							Rotacion, GMROI, MargenP, MargenB, Inversion, 2,
							(Double)temp.get(0), (Integer)temp.get(1), (Double)temp.get(2), (Double)temp.get(3), 
							(Integer)temp.get(4), "");
				} // if campaña
				
				if ((i==0) || (!items.get(i).NombreActividad.
						equals(items.get(i-1).NombreActividad) )){
					Integer ActivityID = items.get(i).Activity_ID;
					// Nombre Actividad
					linea.append("Actividad de Mercadeo: "
							+ items.get(i).NombreActividad);
					Vector temp = new Vector();
					temp = XX_VMA_ReportResults.obtainValues(items, 3, ActivityID, items.get(i));
					//System.out.println("Actividad: "+(Double)temp.get(0)+" "+(Integer)temp.get(1)+" "+(Double)temp.get(2)+" "+(Double)temp.get(3)+" "+(Integer)temp.get(4));
					calculateValues(linea, delimitador, writer, STUni, 
							Rotacion, GMROI, MargenP, MargenB, Inversion, 3, 
							(Double)temp.get(0), (Integer)temp.get(1), (Double)temp.get(2), (Double)temp.get(3), 
							(Integer)temp.get(4),"");
				} // if actividad
				
				if ((i==0) || (!items.get(i).NombreFolleto.
						equals(items.get(i-1).NombreFolleto) ) ){
					Integer BrochureID = items.get(i).Brochure_ID;
					// Nombre Folleto
					linea.append("Folleto: "+ items.get(i).NombreFolleto);
					Vector temp = new Vector();
					temp = XX_VMA_ReportResults.obtainValues(items, 4, BrochureID, items.get(i));
					//System.out.println("Folleto: "+(Double)temp.get(0)+" "+(Integer)temp.get(1)+" "+(Double)temp.get(2)+" "+(Double)temp.get(3)+" "+(Integer)temp.get(4));
					calculateValues(linea, delimitador, writer, STUni, 
							Rotacion, GMROI, MargenP, MargenB, Inversion, 4, 
							(Double)temp.get(0), (Integer)temp.get(1), (Double)temp.get(2), (Double)temp.get(3), 
							(Integer)temp.get(4), "");
				} // if folleto
					
				if ((i==0) || (!items.get(i).NombrePagina.
						equals(items.get(i-1).NombrePagina) )){
					Integer BrochurePageID = items.get(i).BrochurePage_ID;
					// Nombre Pagina
					linea.append("Página: " + items.get(i).NombrePagina);
					Vector temp = new Vector();
					temp = XX_VMA_ReportResults.obtainValues(items, 5, BrochurePageID, items.get(i));
					//System.out.println("Pagina: "+(Double)temp.get(0)+" "+(Integer)temp.get(1)+" "+(Double)temp.get(2)+" "+(Double)temp.get(3)+" "+(Integer)temp.get(4));
					calculateValues(linea, delimitador, writer, STUni, 
							Rotacion, GMROI, MargenP, MargenB, Inversion, 5, 
							(Double)temp.get(0), (Integer)temp.get(1), (Double)temp.get(2), (Double)temp.get(3), 
							(Integer)temp.get(4), "");
				} // if pagina
				
				// Se comienzan a escribir los productos correspondientes
				calculateValues(linea, delimitador, writer, STUni, 
						Rotacion, GMROI, MargenP, MargenB, Inversion, 6,
						items.get(i).InvIniB, items.get(i).InvIniP, 
						items.get(i).Compras,items.get(i).VentasB, 
						items.get(i).VentasP, items.get(i).NombreElemento);
			}//for
			writer.flush();
			writer.close();
			
			//El archivo fue creado
			String msg = Msg.getMsg(Env.getCtx(), "XX_FileCreated", new String [] {
				nombre
			});
			
			ADialog.info(1, new Container(), msg);			
			
		}  
		catch (FileNotFoundException fnfe) {
			log.log(Level.SEVERE, "(f) - " + fnfe.toString());
		} 
		catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}// Fin crearCSV
	
	/** calculateValues
	 * Se Escriben en el archivo los productos con sus datos y para las últimas
	 * 3 columnas del reporte se realizan los cálculos
	 * @param linea Linea a escribir
	 * @param items Item que se escribira
	 * @param delimitador Delimitar para separar las columnas
	 * @param writer BufferedWriter
	 * @param STUni STUni 
	 * @param Rotacion Rotacion
	 * @param GMROI GMROI
	 * @param MargenP MargenP
	 * @param MargenB MargenB
	 * @param Inversion Inversion
	 * @param tipo Tipo del nivel del reporte
	 * */
	public void calculateValues(StringBuffer linea, char delimitador, 
			BufferedWriter writer, BigDecimal STUni,Double Rotacion,Double GMROI, 
			Double MargenP, Double MargenB,	Double Inversion, int tipo, 
			Double InvIniB, Integer InvIniP, Double Compras, Double VentasB, 
			Integer VentasP, String NombreElemento){
		BigDecimal compras = new BigDecimal(0);
		BigDecimal ventas = new BigDecimal(0);
		// Se calcula/imprime los valores de un elemento
		if(tipo == 6){
			// Nombre del producto
			linea.append(NombreElemento);
			linea.append(delimitador);
		} // if tipo
		else {
			linea.append(delimitador);
		}
		
		// Inventario Inicial en Bsf: Suma(Inv Inicial Bsf)	
		linea.append(DisplayType.getNumberFormat(Excel.DISPLAY_TYPE_NUMBER,
				Env.getLanguage(Env.getCtx())).format(InvIniB));
		linea.append(delimitador);
		
		// Inventario Inicial en Piezas: Suma(Inv.Inicial Pzas)
		linea.append(DisplayType.getNumberFormat(Excel.DISPLAY_TYPE_NUMBER,
				Env.getLanguage(Env.getCtx())).format(InvIniP));
		linea.append(delimitador);
		
		// Compras: Suma(Compras)
		compras = new BigDecimal(Compras).setScale(2, BigDecimal.ROUND_HALF_UP);
		linea.append(DisplayType.getNumberFormat(Excel.DISPLAY_TYPE_NUMBER,
				Env.getLanguage(Env.getCtx())).format(compras));
		linea.append(delimitador);
		
		// Venta Bsf: Suma(Ventas en Bs)
		ventas = new BigDecimal(VentasB).setScale(2, BigDecimal.ROUND_HALF_UP);
		linea.append(DisplayType.getNumberFormat(Excel.DISPLAY_TYPE_NUMBER,
				Env.getLanguage(Env.getCtx())).format(ventas));
		linea.append(delimitador);
		
		// Venta Unidades: Suma(Ventas en Pzas)
		linea.append(DisplayType.getNumberFormat(Excel.DISPLAY_TYPE_NUMBER,
				Env.getLanguage(Env.getCtx())).format(VentasP));
		linea.append(delimitador);
		
		// Sell Trough Piezas: Suma(Ventas Pzas) / Inv.Inicial Pzas
		if (!InvIniP.equals(new Integer(0))){
			BigDecimal VenP = new BigDecimal(VentasP).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal IIP = new BigDecimal(InvIniP).setScale(2, BigDecimal.ROUND_HALF_UP);
			STUni = VenP.divide(IIP, BigDecimal.ROUND_HALF_UP);
			linea.append(DisplayType.getNumberFormat(Excel.DISPLAY_TYPE_NUMBER,
					Env.getLanguage(Env.getCtx())).format(STUni));
			linea.append(delimitador);
		}
		else {
			linea.append(DisplayType.getNumberFormat(Excel.DISPLAY_TYPE_NUMBER,
					Env.getLanguage(Env.getCtx())).format(0.0));
				linea.append(delimitador);
		}
		
		// Rotacion: (Ventas Reales Pzas * 12) / ((Inv.Inic.Real pzas + 
		// Inv.Final Real pzas) / 2)
		if (!InvIniB.equals(new Integer(0))){
			Rotacion = (VentasP*12.0)/((InvIniP+InvFinPresC)/2.0);
			linea.append(DisplayType.getNumberFormat(Excel.DISPLAY_TYPE_NUMBER,
				Env.getLanguage(Env.getCtx())).format(Rotacion));
			linea.append(delimitador);
		}
		else{
			linea.append(DisplayType.getNumberFormat(Excel.DISPLAY_TYPE_NUMBER,
					Env.getLanguage(Env.getCtx())).format(0.0));
				linea.append(delimitador);
		}
		
		// Inversion Bsf: ¿?
		if (!PVP.equals(0.0)){
			linea.append(DisplayType.getNumberFormat(Excel.DISPLAY_TYPE_NUMBER,
				Env.getLanguage(Env.getCtx())).format(0));
			linea.append(delimitador);
		}
		else {
			linea.append(DisplayType.getNumberFormat(Excel.DISPLAY_TYPE_NUMBER,
					Env.getLanguage(Env.getCtx())).format(0));
				linea.append(delimitador);
		}
		
		// Margen %: PVP - COSTO / PVP
		if (!PVP.equals(0.0)){
			MargenB = 1 - (Costo/PVP );
			linea.append(DisplayType.getNumberFormat(Excel.DISPLAY_TYPE_NUMBER,
				Env.getLanguage(Env.getCtx())).format(MargenB));
			linea.append(delimitador);
		}
		else {
			linea.append(DisplayType.getNumberFormat(Excel.DISPLAY_TYPE_NUMBER,
					Env.getLanguage(Env.getCtx())).format(0.0));
				linea.append(delimitador);
		}
		
		// GMROI: ROTACION * MARGEN% / (100-MARGEN%)
		if (!Rotacion.equals(0.0)){
			GMROI = Rotacion*MargenP / (100.0 - MargenP); 
			linea.append(DisplayType.getNumberFormat(Excel.DISPLAY_TYPE_NUMBER,
				Env.getLanguage(Env.getCtx())).format(GMROI));
			linea.append(delimitador);
		}
		else {
			linea.append(DisplayType.getNumberFormat(Excel.DISPLAY_TYPE_NUMBER,
					Env.getLanguage(Env.getCtx())).format(0.0));
				linea.append(delimitador);
		}
		try {
			writer.write(linea.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer.write(Env.NL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		linea.setLength(0);
	} // calculateValues
	
	@Override
	protected void prepare() {
				
		ProcessInfoParameter[] parameter = getParameter();
		
		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();
			String extension = "";
			if (element.getParameter() == null) ;
			// Archivo a Generar
			else if (name.equals("File")) { 
				if (element.getParameter() != null) {					
					String archivo = (String)element.getParameter();
					//System.out.println("Archivo: "+archivo);
					
					// Verifica si el archivo existe
					File archivoFisico = new File(archivo);
					if (archivoFisico.exists()){
						ADialog.error(1, new Container(), "XX_FileExist" );
						errorArchivo = true;
						return;	
					}
					
					// Se obtiene la extensión del archivo seleccionado
					extension = archivo.substring(archivo.length()-4, archivo.length());
					//System.out.println("Extension: "+extension);
					
					// Verifica la extensión del archivo seleccionado
					if (!extension.equals(".xls")) {
						ADialog.error(1, new Container(), "Not Excel" );
						errorArchivo = true;
						return;		
					}	
					
					nombreArchivo = (String) element.getParameter();
				}//getparameter 
			}//else if Archivo
			// Parámetros respectivos al Analisis
			else if (name.equals("XX_VMA_Season_ID")) {
				if (element.getParameter() != null) 
					Season_ID = element.getParameterAsInt();
				if (sqlWhere.length()  == 0) {
					sqlWhere += " WHERE S.XX_VMA_Season_ID = " + element.getParameterAsInt()+
								" AND S.IsActive = 'Y' ";
				} 
				else {
					sqlWhere += " AND S.XX_VMA_Season_ID = " + element.getParameterAsInt()+
								" AND S.IsActive = 'Y' ";
				} 
			} // Season
			else if (name.equals("C_Campaign_ID")) {
				if (element.getParameter() != null) 
					Campaign_ID = element.getParameterAsInt();	
				if (sqlWhere.length()  == 0) {
					sqlWhere += " WHERE C.C_Campaign_ID = " + element.getParameterAsInt() +
								" AND C.IsActive = 'Y' ";
				} 
				else {
					sqlWhere += " AND C.C_Campaign_ID = " + element.getParameterAsInt() +
								" AND C.IsActive = 'Y' ";
				} 
			} // Campaign
			else if (name.equals("XX_VMA_MarketingActivity_ID")) {
				if (element.getParameter() != null) 
					Activity_ID = element.getParameterAsInt();
				if (sqlWhere.length()  == 0) {
					sqlWhere += " WHERE M.XX_VMA_MarketingActivity_ID = " + element.getParameterAsInt() +
								" AND M.IsActive = 'Y' ";
				} 
				else {
					sqlWhere += " AND M.XX_VMA_MarketingActivity_ID = " + element.getParameterAsInt() +
								" AND M.IsActive = 'Y' ";
				} 
			} // Activity
			else if (name.equals("XX_VMA_Brochure_ID")) {
				if (element.getParameter() != null) 
					Brochure_ID = element.getParameterAsInt();	
				if (sqlWhere.length()  == 0) {
					sqlWhere += " WHERE B.XX_VMA_Brochure_ID = " + element.getParameterAsInt() +
								" AND B.IsActive = 'Y' ";
				} 
				else {
					sqlWhere += " AND B.XX_VMA_Brochure_ID = " + element.getParameterAsInt() +
							" AND B.IsActive = 'Y' ";
				} 
			} // Brochure
			else if (name.equals("XX_VMA_BrochurePage_ID")) {
				if (element.getParameter() != null) 
					BrochurePage_ID = element.getParameterAsInt();	
				if (sqlWhere.length()  == 0) {
					sqlWhere += " WHERE BP.XX_VMA_BrochurePage_ID = " + element.getParameterAsInt() +
								" AND BP.IsActive = 'Y' ";
				} 
				else {
					sqlWhere += " AND BP.XX_VMA_BrochurePage_ID = " + element.getParameterAsInt() +
								" AND BP.IsActive = 'Y' ";
				}
			} // BrochurePage
			else if (name.equals("XX_VME_Element_ID")) {
				if (element.getParameter() != null) 
					Element_ID = element.getParameterAsInt();
				if (sqlWhere.length()  == 0) {
					sqlWhere += " WHERE E.XX_VME_Element_ID = " + element.getParameterAsInt() +
								" AND E.IsActive = 'Y' ";
				} 
				else {
					sqlWhere += " AND E.XX_VME_Element_ID = " + element.getParameterAsInt() +
								" AND E.IsActive = 'Y' ";
				}
			} // Element
			else if (name.equals("XX_VMR_Category_ID")) {
				if (element.getParameter() != null) 
					Category_ID = element.getParameterAsInt();
				if (sqlWhere.length()  == 0) {
					sqlWhere += " WHERE Ct.XX_VMR_Category_ID = " + element.getParameterAsInt() +
								" AND Ct.IsActive = 'Y' ";
				} 
				else {
					sqlWhere += " AND Ct.XX_VMR_Category_ID = " + element.getParameterAsInt() +
								" AND Ct.IsActive = 'Y' ";
				}
			} // Category
			else if (name.equals("XX_VMR_Department_ID")) {
				if (element.getParameter() != null) 
					Department_ID = element.getParameterAsInt();
				if (sqlWhere.length()  == 0) {
					sqlWhere += " WHERE P.XX_VMR_Department_ID = " + element.getParameterAsInt() +
							" AND P.IsActive = 'Y' ";
				} 
				else {
					sqlWhere += " AND P.XX_VMR_Department_ID = " + element.getParameterAsInt() +
							" AND P.IsActive = 'Y' ";
				}
			} // Department
			else if (name.equals("XX_VMR_Line_ID")) {
				if (element.getParameter() != null) 
					Line_ID = element.getParameterAsInt();
				if (sqlWhere.length()  == 0) {
					sqlWhere += " WHERE P.XX_VMR_Line_ID = " + element.getParameterAsInt();
				} 
				else {
					sqlWhere += " AND P.XX_VMR_Line_ID = " + element.getParameterAsInt();
				}
			} // Line
			else if (name.equals("XX_VMR_Section_ID")) {
				if (element.getParameter() != null) 
					Section_ID = element.getParameterAsInt();
				if (sqlWhere.length()  == 0) {
					sqlWhere += " WHERE P.XX_VMR_Section_ID = " + element.getParameterAsInt();
				} 
				else {
					sqlWhere += " AND P.XX_VMR_Section_ID = " + element.getParameterAsInt();
				}
			} // Section
			// Desconocidos
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);			
		}// for		
	}// Fin prepare	

}// Fin XX_VMA_GenerateReportResults
