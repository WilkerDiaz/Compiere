package compiere.model.dynamic.processes;
import java.awt.Container;
import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.compiere.apps.ADialog;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.dynamic.XX_VME_GeneralFunctions;
import compiere.model.dynamic.X_XX_VMA_Brochure;
import compiere.model.dynamic.X_XX_VMA_BrochurePage;
import compiere.model.dynamic.X_XX_VMA_MarketingActivity;
import compiere.model.dynamic.X_XX_VME_Elements;
import compiere.model.dynamic.X_XX_VME_Reference;

/** XX_VME_ImportRefExcel
 * Proceso de importación de referencias a un elemento desde un archivo excel.
 * El usuario crea el elemento al cual las referencias y sus productos seran
 * asociados, posteriormente procede a la importación desde el archivo.
 * Funcion 13
 * @author mvintimilla
 * @version 1.0
 *  
 * **/
public class XX_VME_ImportRefExcel extends SvrProcess {
	private Integer BrochurePage_ID;
	private Integer elementID;
	private String nombreArchivo = "";
	private boolean errorArchivo = false;
	private boolean lectura = true;
	private Vector<Integer> dept = new Vector<Integer>();
	private Vector<Integer> prod = new Vector<Integer>();
	private Calendar cal = Calendar.getInstance();
	private int mes = cal.get(Calendar.MONTH)+1;
	private int ano = cal.get(Calendar.YEAR);

	@Override
	protected void prepare() {
		elementID = getRecord_ID();
		ProcessInfoParameter[] parameter = getParameter();
			
		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();
			String extension= "";
			if (element.getParameter() == null) ;
			
			// Parametro Archivo a ser importado
			else if (name.equals("File")) { 
				if (element.getParameter() != null) {					
					String archivo = (String)element.getParameter();

					// Se obtiene la extensión del archivo seleccionado
					extension = archivo.substring(archivo.length()-4, archivo.length());
					
					// Verifica la extensión del archivo seleccionado
					if (!extension.equals(".xls")) {
						ADialog.error(1, new Container(), "Not Excel" );
						errorArchivo = true;
						return;		
					}	
					nombreArchivo = (String) element.getParameter();
				} 
			}//else if Archivo
		}// For
	} // prepare

	
	@Override
	protected String doIt() throws Exception {

		File inputWorkbook = new File(nombreArchivo);
		Workbook w;
		int reference = 0;
		int totalRef = 0;
		int totalImported = 0;
		Vector<Integer> references = new Vector<Integer>();
		
		// Elemento, Folleto, pagina y accion
		X_XX_VME_Elements elemento = new X_XX_VME_Elements(Env.getCtx(), elementID, null);
		X_XX_VMA_BrochurePage page = new X_XX_VMA_BrochurePage(Env.getCtx(), elemento.getXX_VMA_BrochurePage_ID(), null);
		X_XX_VMA_Brochure brochure = new X_XX_VMA_Brochure(Env.getCtx(), page.getXX_VMA_Brochure_ID(), null);
		int actionID = XX_VME_GeneralFunctions.obtainAM(brochure.get_ID()); 
		X_XX_VMA_MarketingActivity action = new X_XX_VMA_MarketingActivity(Env.getCtx(), actionID, null);
		
		// Error en el archivo
		if(errorArchivo){
			return "";
		}
		/** Se crea el archivo para recorrerlo y obtener la información 
		 * de los productos a insertar en la página del folleto respectivo */
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(0);
			
			// Si la cantidad de columnas es 11
			if(sheet.getColumns() == 7 && sheet.getRows() > 1){
				Vector info = new Vector();
				/** Se valida que la cabecera del archivo tenga el formato 
				 * correcto */
				if(!sheet.getCell(0, 0).getContents().equals("CODCAT") ||  
					!sheet.getCell(1, 0).getContents().equals("CODDEP") ||
					!sheet.getCell(2, 0).getContents().equals("CODLIN") || 
					!sheet.getCell(3, 0).getContents().equals("CODSEC") ||
					!sheet.getCell(4, 0).getContents().equals("REFERENCIA") ||  
					!sheet.getCell(5, 0).getContents().equals("CANTIDAD") || 
					!sheet.getCell(6, 0).getContents().equals("MANTENER")
					) {	
					lectura = false;
				} // if columnas
					
				if(lectura){
					/** Se toman los departamentos de la página para verificar 
					 * con aquellos que se colocaron en el archivo para insertar
					 * los productos */
					String SQLPageDep = " SELECT" +
							" XX_VMR_DEPARTMENT_ID DEP " +
							" FROM XX_VMA_PAGEDEPT_V " +
							" WHERE XX_VMA_BROCHUREPAGE_ID = " + page.get_ID();
					//System.out.println("SQLPageDep: "+SQLPageDep);
					
					PreparedStatement psQueryPageDep = null;
					ResultSet rsQueryPageDep = null;
					try{
						psQueryPageDep = DB.prepareStatement(SQLPageDep, null);
						rsQueryPageDep = psQueryPageDep.executeQuery();
						while(rsQueryPageDep.next()){	
							dept.add(rsQueryPageDep.getInt("DEP"));
						}					
					}	
					catch(SQLException e){	e.getMessage(); }
					finally {
						DB.closeResultSet(rsQueryPageDep);
						DB.closeStatement(psQueryPageDep);
					}
					
					/** Se recorren las filas del archivo para obtener los 
					 * datos de los productos a importar */
					for (int i = 1; i < sheet.getRows(); i++) {
						String decimal = "";
						
						/** Se toma la categoria */
						String category = sheet.getCell(0, i).getContents();
						int categoryID = getCategoryID(category);
							
						if(categoryID == 0){
							/*ADialog.error(1, new Container(), 
									Msg.translate( Env.getCtx(), "XX_CatError" ));*/		
							//break;
							continue;
						} // cat
						else {
							totalRef = totalRef + 1;
						}
						
						/** Se toma el departamento */
						String department = sheet.getCell(1, i).getContents();
						int departmentID = getDepartmentID(department, categoryID);
							
						if(departmentID == 0){
							/*ADialog.info(1, new Container(), 
									Msg.translate( Env.getCtx(), "XX_DepDefError") );*/	
							continue;
						} // dep
						
						if(!dept.contains(departmentID)){
							ADialog.error(0, new Container(), 
									Msg.getMsg( Env.getCtx(), "XX_PageDepError",
											new String[] {department}));
							continue;
							//break;
						}

						/** Se toma la línea */
						String line = sheet.getCell(2, i).getContents();
						int lineID = getLineID(line,departmentID);
							
						if(lineID == 0){
							/*ADialog.error(1, new Container(), 
									Msg.translate( Env.getCtx(), "XX_LinError" ));		
							break;*/
							continue;
						} // lin
							
						/** Se toma la sección */ 
						String section = sheet.getCell(3, i).getContents();
						int sectionID = getSectionID(section,lineID);
							
						if(sectionID == 0){
							/*ADialog.error(1, new Container(), 
									Msg.translate( Env.getCtx(), "XX_SecError" ));	
							break;*/
							continue;
						} // sec
							
						/** Se toma el ID de la referencia */
						String referenceValue = sheet.getCell(4, i).getContents();
						int referenceID = 0;
						if(referenceValue.equals("")){
							ADialog.error(1, new Container(), 
									Msg.translate( Env.getCtx(), "XX_RefError" ));	
							break;
						} 
						else {
							// Obtener el ID de la referencia
							String SQLReference = " SELECT XX_VMR_VENDORPRODREF_ID ID" +
												" FROM XX_VMR_VENDORPRODREF" +
												" WHERE  value = '" + referenceValue + "'" +
												" AND XX_VMR_DEPARTMENT_ID = " + departmentID +
												" AND XX_VMR_LINE_ID = " + lineID +
												" AND XX_VMR_SECTION_ID = " + sectionID;
							//System.out.println("SQLReference:"+SQLReference);
							PreparedStatement psRef = null;
							ResultSet rsRef = null;
							references.clear();
							try {
								psRef = DB.prepareStatement(SQLReference, null);
								rsRef = psRef.executeQuery();
								while(rsRef.next()){
									references.add(rsRef.getInt("ID")); 
								}
							} // try
							catch (Exception e){ e.printStackTrace(); 	}
							finally {
								DB.closeResultSet(rsRef);
								DB.closeStatement(psRef);
							}
							
							/** Se toma la cantidad */
							boolean manual = true;
							BigDecimal quantity = new BigDecimal(0);
							String quantityR = sheet.getCell(5, i).getContents();
							totalImported++;
							if(quantityR.equals("")){
								manual = false;
							}
							else {
								quantityR = quantityR.trim();
								quantityR = quantityR.replace(",", ".");
								quantity = new BigDecimal(quantityR);
								quantity = quantity.setScale(0, BigDecimal.ROUND_HALF_UP);
								
								/** Se define Manual */
								if(quantity.compareTo(new BigDecimal(0)) < 0){
									totalImported--;
									continue;
								}
								else if(quantity.compareTo(new BigDecimal(0)) == 0){
									manual = false;
								}
							}//else qty
						
							// Se recorren las referencias
							for(int j = 0; j < references.size(); j++){
								// Verificar si la referencia ya está asociada al elemento
								reference = XX_VME_GeneralFunctions.obtainReference(
										references.get(j), elemento);
							
								/** Se toma Mantener */
								String mantener = sheet.getCell(6, i).getContents();
								if(mantener.equals("")){
									mantener = "N";
								} // Tendencia
								

								// La referencia a la que se refiere el producto no se encuentra
								// asociada al elemento
								if(reference == 0) {
									// Se crea la referencia 
									XX_VME_GeneralFunctions.createElemRef(elemento, 	// elemento 
											action, 									// accion de  mercadeo
											quantity,									// cantidad
											references.get(j),							// referencia
											categoryID, 								// categoria
											"R", 										// tipo
											manual/*.equalsIgnoreCase("Y")*/,				// manual o no
											mantener.equalsIgnoreCase("Y"),				// mantener
											0,											// producto
											false);										// manual o no (prod)
								} // if reference
								else {
									// La referencia existe, se actualiza la cantidad indepabis
									X_XX_VME_Reference ref = new X_XX_VME_Reference(Env.getCtx(), reference, null);
									
									ref.setXX_VME_IndepabisQty(quantity);
									
									// Se actualiza manual
									ref.setXX_VME_Manual(manual/*.equalsIgnoreCase("Y")*/);
									
									// Se actualiza mantener
									ref.setXX_VME_Mantain(mantener.equalsIgnoreCase("Y"));
									
									// Se redefinen las cantidades de los productos asociados
									XX_VME_GeneralFunctions.redefineQtyProd(reference,
											quantity, manual/*.equalsIgnoreCase("Y")*/);
									ref.save();
								}// else reference
							} // for de referencias
						} // else referencia
					} // for
					
					// Se redefinen las cantidades indepabis de las referencias asociadas al elemento
					XX_VME_GeneralFunctions.redefineQuantities(elemento);
					elemento.setXX_VME_Validated(false);
					elemento.save();
					
					// Ventana que informa la cantidad de registros importados
					ADialog.info(1, new Container(), 
							/*Msg.translate( Env.getCtx(), "XX_Msg" )*/
							"Se importaron: "+totalImported+
							" referencias de un total de "+totalRef+"  registros del archivo");
				} // if
				
			} // if columnas y filas
			else{
				ADialog.error(1, new Container(), 
						Msg.translate( Env.getCtx(), "XX_Msg" ));	
				lectura = false;
			}
				
		} // try 
		catch (BiffException e){
			log.log(Level.SEVERE, e.getMessage());
		}
		return "";

	} // doIt

	/** getCategoryID
	 * Obtiene el ID de una categoria basado en el valor proporcionado
	 * en el archivo de importación */
	private int getCategoryID(String category){
		int categoryID = 0;
		String SQLCat = "SELECT XX_VMR_CATEGORY_ID ID " +
						" FROM XX_VMR_CATEGORY " +
						" WHERE VALUE = '" + category + "'";
		//System.out.println("SQLCat: "+SQLCat);
		
		PreparedStatement psQueryCat = null;
		ResultSet rsQueryCat = null;
		try{
			psQueryCat = DB.prepareStatement(SQLCat, null);
			rsQueryCat = psQueryCat.executeQuery();
			if(rsQueryCat.next()){	
				categoryID = rsQueryCat.getInt("ID");
			}						
		}	
		catch(SQLException e){ e.getMessage(); 	}
		finally {
			DB.closeResultSet(rsQueryCat);
			DB.closeStatement(psQueryCat);
		}
		return categoryID;
	} // getCategoryID
	
	/** getDepartmentID
	 * Obtiene el ID de un departamento basado en el valor proporcionado
	 * en el archivo de importación */
	private int getDepartmentID(String department, int category){
		int depID = 0;
		String SQLDep = "SELECT XX_VMR_DEPARTMENT_ID ID " +
						" FROM XX_VMR_DEPARTMENT " +
						" WHERE VALUE = '" + department + "'" +
						" AND XX_VMR_CATEGORY_ID = " + category;
		//System.out.println("SQLDep: "+SQLDep);
		
		PreparedStatement psQueryDep = null;
		ResultSet rsQueryDep = null;
		try{
			psQueryDep = DB.prepareStatement(SQLDep, null);
			rsQueryDep = psQueryDep.executeQuery();
			if(rsQueryDep.next()){	
				depID = rsQueryDep.getInt("ID");
			}					
		}	
		catch(SQLException e){	e.getMessage(); }
		finally {
			DB.closeResultSet(rsQueryDep);
			DB.closeStatement(psQueryDep);
		}
		return depID;
	} // getDepartmentID
		
	/** getLineID
	 * Obtiene el ID de una línea basado en el valor proporcionado
	 * en el archivo de importación */
	private int getLineID(String line, int departmentID){
		int linID = 0;
		String SQLLin = "SELECT XX_VMR_LINE_ID ID " +
						" FROM XX_VMR_LINE " +
						" WHERE VALUE = '" + line + "'" +
						" AND XX_VMR_DEPARTMENT_ID = " + departmentID;
		//System.out.println("SQLLin: "+SQLLin);
		
		PreparedStatement psQueryLin = null;
		ResultSet rsQueryLin = null;
		try{
			psQueryLin = DB.prepareStatement(SQLLin, null);
			rsQueryLin = psQueryLin.executeQuery();
			if(rsQueryLin.next()){	
				linID = rsQueryLin.getInt("ID");
			}						
		}	
		catch(SQLException e){ e.getMessage(); }
		finally {
			DB.closeResultSet(rsQueryLin);
			DB.closeStatement(psQueryLin);
		}
		return linID;
	} // getLineID
		
	/** getSectionID
	 * Obtiene el ID de una sección basado en el valor proporcionado
	 * en el archivo de importación */
	private int getSectionID(String section, int lineID){
		int secID = 0;
		String SQLSec = "SELECT XX_VMR_SECTION_ID ID " +
						" FROM XX_VMR_SECTION " +
						" WHERE VALUE = '" + section + "'" +
						" AND XX_VMR_LINE_ID = " + lineID;
		//System.out.println("SQLSec: "+SQLSec);
		
		PreparedStatement psQuerySec = null; 
		ResultSet rsQuerySec = null;
		
		try{
			psQuerySec = DB.prepareStatement(SQLSec, null);
			rsQuerySec = psQuerySec.executeQuery();
			if(rsQuerySec.next()){	
				secID = rsQuerySec.getInt("ID");
			}					
		}	
		catch(SQLException e){ e.getMessage(); }
		finally {
			DB.closeResultSet(rsQuerySec);
			DB.closeStatement(psQuerySec);
		}
		return secID;
	} // getLineID
	
} // Fin XX_VME_ImportRefExcel
