package compiere.model.cds.processes;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MVMRDepartment;
import compiere.model.cds.X_XX_VMR_Competition;
import compiere.model.cds.X_XX_VMR_CompetitionPrice;

/**
 *  Importar Precio mínimo y máximo de las marcas competidoras  de archivo Excel
 *
 *  @author     Gabrielle Huchet
 *  @version    
 */

public class XX_ImportCompetitionPrice  extends SvrProcess {
	
	static CLogger log = CLogger.getCLogger(XX_ImportPTransfer.class);
	private String archivo = null;
	
	@Override
	protected String doIt() throws Exception {
		String msg = "";
		if (archivo == null) {
			msg =  Msg.translate( getCtx(), "File Not Loaded");
		} else {
			
			if(!archivo.substring(archivo.length()-4, archivo.length()).equals(".xls")){
				if(archivo.substring(archivo.length()-5, archivo.length()).equals(".xlsx")){
					msg = Msg.translate( getCtx(), "Excel Earlier Format");;	
				}else{
					msg =Msg.translate( getCtx(), "Not Excel");
				}
			}else{	
				msg = readFile();
			}
		}
		return msg;
	}


	@Override
	/** Obtener los parametros */
	protected void prepare() {
		ProcessInfoParameter[] parameter = getParameter();

		for (ProcessInfoParameter element : parameter) {
			
			if (element.getParameter()!=null) {
				if (element.getParameterName().equals("File") ) {
					archivo = element.getParameter().toString();
				}
			}			
		}
	}
	

	public String readFile()  throws IOException  {
		
		File inputWorkbook = new File(archivo);
		Workbook w;
		try {
			String msg = "";
			w = Workbook.getWorkbook(inputWorkbook);
			// Get the first sheet
			Sheet sheet = w.getSheet(0);
	
			//int defaultRows = sheet.getRows();
			
			//Si la cantidad de columnas es 6
			if(sheet.getColumns()>=6 && sheet.getRows() > 1){
		
				//Valido que las cabeceras tengan el formato correcto	
				if(!sheet.getCell(0, 0).getContents().equals("Competencia") 
//						|| !sheet.getCell(1, 0).getContents().equals("Cat")
						|| !sheet.getCell(1, 0).getContents().equals("Dep") 
						|| !sheet.getCell(2, 0).getContents().equals("Lin")	
						|| !sheet.getCell(3, 0).getContents().equals("Concepto de Valor")
						|| !sheet.getCell(4, 0).getContents().equals("Precio Min")
						|| !sheet.getCell(5, 0).getContents().equals("Precio Max")
						
				) {
					msg = Msg.translate( getCtx(), "Column Names");
					return msg;
				}	
			
				//El objeto XX_VMR_CompetitionPrice que q almacena los datos
				X_XX_VMR_CompetitionPrice competitionPrice= null;
				
				for (int i = 1; i < sheet.getRows(); i++) {
					
					//Capturo el Competencia (Marca)
					String competition_value = sheet.getCell(0, i).getContents();
					Integer competition = getCompetition(competition_value);
					if (competition == null){
						System.out.println(Msg.translate( getCtx(), "Cell A Error")+(i+1) + " " + sheet.getCell(0, i).getContents());
						continue;
					}

					//Capturo el Departamento
					String dep_value = sheet.getCell(1, i).getContents();
					Integer dep = getDepartment(dep_value);
					if (dep == null){
						System.out.println(Msg.translate( getCtx(), "Cell B Error")+(i+1) + " " + sheet.getCell(1, i).getContents());
						continue;
					}
					
					//Capturo la Línea
					String lin_value = sheet.getCell(2, i).getContents();
					Integer lin = getLine(lin_value, dep);;
					if (lin== null){
						System.out.println(Msg.translate( getCtx(), "Cell C Error")+(i+1) + " " + sheet.getCell(2, i).getContents());
						continue;
					}
	
				
					//Capturo el Concepto de Valor
					String concept_value = sheet.getCell(3, i).getContents();
					Integer concept = getConceptValue(concept_value);					
					if (concept == null ) {
						System.out.println(Msg.translate( getCtx(), "Cell D Error")+(i+1) + " " + sheet.getCell(3, i).getContents());						
						continue;
					}	
					
					//Capturo Precio Mínimo
					String minPrice_value = sheet.getCell(4, i).getContents();
					BigDecimal minPrice = null;
					try {
						if (sheet.getCell(4, i) != null)
						minPrice = new BigDecimal(minPrice_value);
					} catch (NumberFormatException e) {
						System.out.println(Msg.translate( getCtx(), "Cell E Error")+(i+1) + " " + sheet.getCell(4, i).getContents());
						continue;
					}
					if (minPrice == null ) {
						System.out.println(Msg.translate( getCtx(), "Cell E Error")+(i+1) + " " + sheet.getCell(4, i).getContents());						
						continue;
					}	
					
					//Capturo Precio Máximo
					String maxPrice_value = sheet.getCell(5, i).getContents();
					BigDecimal maxPrice = null;
					try {
						if (sheet.getCell(5, i) != null)
						maxPrice = new BigDecimal(maxPrice_value);
					} catch (NumberFormatException e) {
						System.out.println(Msg.translate( getCtx(), "Cell F Error")+(i+1) + " " + sheet.getCell(5, i).getContents());
						continue;
					}
					if (maxPrice == null ) {
						System.out.println(Msg.translate( getCtx(), "Cell F Error")+(i+1) + " " + sheet.getCell(5, i).getContents());						
						continue;
					}	
					if(!exists(dep,lin,concept,competition, minPrice, maxPrice)){
						//agregarlo
						MVMRDepartment department = new MVMRDepartment(Env.getCtx(), dep, null);
						competitionPrice = new X_XX_VMR_CompetitionPrice(Env.getCtx(), 0 , get_TrxName());
						competitionPrice.setXX_VMR_Category_ID(department.getXX_VMR_Category_ID());
						competitionPrice.setXX_VMR_Department_ID(dep);
						competitionPrice.setXX_VMR_Line_ID(lin);
						competitionPrice.setXX_VME_ConceptValue_ID(concept);
						competitionPrice.setXX_VMR_Competition_ID(competition);
						competitionPrice.setXX_MinPrice(minPrice);
						competitionPrice.setXX_MaxPrice(maxPrice);
						if (!competitionPrice.save()){
							rollback();
						//	return "Proceso falló";*/
							System.out.println("Error en línea (i+1)");
							continue;
						}
						commit();
					}else {
						System.out.println("Se actualizó registro existente. Competencia: "+competition_value.toUpperCase()+
								" - Dpto: "+dep+" - Linea: "+lin+" - Concepto de Valor:"+concept_value.toUpperCase());
					}
				}
			
				return "Proceso completado";			

			}else {
				return Msg.translate( getCtx(), "6 Columns");
			}			
		} catch (BiffException e){		
			//REVISAR
			rollback();
			log.log(Level.SEVERE, e.getMessage());
		}
		return "";
	}
	
	private boolean exists(Integer dep, Integer lin, Integer concept,
			Integer competition, BigDecimal minPrice, BigDecimal maxPrice) {
		
		boolean returned = false;		
		
		String sql = "SELECT d.XX_VMR_COMPETITIONPRICE_ID " +
		"FROM XX_VMR_COMPETITIONPRICE d " +
		"WHERE d.XX_VMR_DEPARTMENT_ID = "+dep+"AND d.XX_VMR_LINE_ID = "+lin+" AND d.XX_VME_CONCEPTVALUE_ID ="+concept;
		PreparedStatement ps =null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			if (rs.next()) {
				returned = true;
				X_XX_VMR_CompetitionPrice competitionPrice = new X_XX_VMR_CompetitionPrice(Env.getCtx(), rs.getInt(1) , get_TrxName());
				competitionPrice.setXX_MinPrice(minPrice);
				competitionPrice.setXX_MaxPrice(maxPrice);
				if (!competitionPrice.save()){
					rollback();
				}
				commit();
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);	
		}
		
		return returned;
		
	}


	private Integer getConceptValue(String concept_value) {

		Integer returned = null;		
		if (concept_value == null) 
			return returned;
		
		String sql = "SELECT d.XX_VME_ConceptValue_ID " +
		"FROM XX_VME_ConceptValue d " +
		"WHERE UPPER(d.name) = '" + concept_value.toUpperCase()+"'";
		PreparedStatement ps =null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			if (rs.next()) {
				returned = rs.getInt(1);			
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);	
		}
		
		return returned;
	}


	private Integer getCompetition(String competition_value) {
		Integer returned = null;		
		if (competition_value == null) 
			return returned;
		
		String sql = "\nSELECT d.XX_VMR_Competition_ID " +
		"\nFROM XX_VMR_Competition d " +
		"\nWHERE  UPPER(d.name) = '" + competition_value.toUpperCase()+"'";
		PreparedStatement ps =null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			if (rs.next()) {
				returned = rs.getInt(1);			
			}else {
				X_XX_VMR_Competition comp = new X_XX_VMR_Competition(getCtx(), 0, null);
				comp.setName(competition_value.toUpperCase());
				comp.save();
				returned = comp.get_ID();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		
		return returned;
	}


	private Integer getLine(String lin_value, Integer dep) {
		Integer returned = null;		
		if (lin_value == null || dep ==null) 
			return returned;
		
		String sql = "\nSELECT l.XX_VMR_Line_ID " +
		"\nFROM XX_VMR_Line l " +
		"\nWHERE  l.XX_VMR_Department_ID = " + dep+
		"\nand l.value = " + lin_value;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			if (rs.next()) {
				returned = rs.getInt(1);			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);			
		}
		return returned;
	}

	private Integer getDepartment(String dep_value) {
		
		Integer returned = null;		
		if (dep_value == null) 
			return returned;
		
		String sql = "\nSELECT d.XX_VMR_Department_ID " +
		"\nFROM XX_VMR_Department d " +
		"\nWHERE  d.value = " + dep_value;
		PreparedStatement ps =null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			if (rs.next()) {
				returned = rs.getInt(1);			
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);	
		}
		
		return returned;
	}
	

}
