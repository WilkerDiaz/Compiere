package compiere.model.cds.processes;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.compiere.model.MRole;
import org.compiere.model.MWarehouse;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.cds.MVMRMinimumPieces;

/**
 *  Importar Carga de Piezas Mínimas de archivo Excel
 *
 *  @author     Gabrielle Huchet
 *  @version    
 */


public class XX_ImportMinimumPieces extends SvrProcess {

	static CLogger log = CLogger.getCLogger(XX_ImportPTransfer.class);
	private String archivo = null;
	private Vector<MWarehouse> stores = new Vector<MWarehouse>();
	
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
				getAllStores();
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
				if(!sheet.getCell(0, 0).getContents().equals("Dep") 
						|| !sheet.getCell(1, 0).getContents().equals("Lin")
						|| !sheet.getCell(2, 0).getContents().equals("Sec") 
						|| !sheet.getCell(3, 0).getContents().equals("Tda")	
						|| !sheet.getCell(4, 0).getContents().equals("CARGA D-L-S-T")
						|| !sheet.getCell(5, 0).getContents().equals("Tipo Inv")
						
				) {
					msg = Msg.translate( getCtx(), "Column Names");
					return msg;
				}	
			
				//El objeto XX_VMR_MinimumPiecesque q almacena los datos
				MVMRMinimumPieces  minimumPieces= null;
				
				for (int i = 1; i < sheet.getRows(); i++) {
					
					//Capturo el Departamento
					String dep_value = sheet.getCell(0, i).getContents();
					Integer dep = getDepartment(dep_value);
					if (dep == null){
						System.out.println(Msg.translate( getCtx(), "Cell A Error")+(i+1) + " " + sheet.getCell(0, i).getContents());
						continue;
					}
					//Capturo la Línea
					String lin_value = sheet.getCell(1, i).getContents();
					Integer lin = getLine(lin_value, dep);;
					if (lin== null){
						System.out.println(Msg.translate( getCtx(), "Cell B Error")+(i+1) + " " + sheet.getCell(1, i).getContents());
						continue;
					}
					
					//Capturo el Sección
					String sec_value = sheet.getCell(2, i).getContents();
					Integer sec = getSection(sec_value, lin);					
					if (sec == null ) {
						System.out.println(Msg.translate( getCtx(), "Cell C Error")+(i+1) + " " + sheet.getCell(2, i).getContents());						
						continue;
					}			
				
					//Capturo el Tienda
					String tda_value = sheet.getCell(3, i).getContents();
					Integer tda = getStore(tda_value);					
					if (tda == null ) {
						System.out.println(Msg.translate( getCtx(), "Cell D Error")+(i+1) + " " + sheet.getCell(3, i).getContents());						
						continue;
					}	
					
					//Capturo las cantidad de Piezas Mínimas
					String qty_value = sheet.getCell(4, i).getContents();
					Integer qty = null;
					try {
						if (sheet.getCell(4, i) != null)
						qty = Integer.parseInt(qty_value);
					} catch (NumberFormatException e) {
						System.out.println(Msg.translate( getCtx(), "Cell E Error")+(i+1) + " " + sheet.getCell(4, i).getContents());
						continue;
					}
					if (qty == null ) {
						System.out.println(Msg.translate( getCtx(), "Cell E Error")+(i+1) + " " + sheet.getCell(4, i).getContents());						
						continue;
					}	
					
					//Capturo el tipo de inventario
					String inv_value = "B";
					Integer inv = getTypeInventory(inv_value);			
					if (inv == null ) {
						System.out.println(Msg.translate( getCtx(), "Cell F Error")+(i+1) + " " + sheet.getCell(5, i).getContents());						
						continue;
					}	
					//agregarlo
					minimumPieces = new MVMRMinimumPieces(Env.getCtx(), 0 , get_TrxName());
					minimumPieces.setXX_VMR_Department_ID(dep);
					minimumPieces.setXX_VMR_Line_ID(lin);
					minimumPieces.setXX_VMR_Section_ID(sec);
					minimumPieces.setM_Warehouse_ID(tda);
					minimumPieces.setXX_VMR_MinimumPiecesQty(qty);
					minimumPieces.setXX_VMR_TypeInventory_ID(inv);
					if (!minimumPieces.save()){
						rollback();
					//	return "Proceso falló";*/
						System.out.println("Error en línea (i+1)");
						continue;
					}
					commit();
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
	
	/** Dado un codigo de tienda retorna un m_warehouse_id */
	private Integer getStore(String tda_value){
				
		Iterator<MWarehouse> storesIt = stores.iterator();
		Integer returned = null; 
		MWarehouse actual = null;
		
		if (tda_value == null)
			return returned; 
		
		while (storesIt.hasNext()) {
			actual = storesIt.next();
			if (Integer.parseInt(actual.getValue()) == Integer.parseInt(tda_value)) {
				returned = actual.getM_Warehouse_ID();
			}
		}
		return returned; 
	}
		
	/** Obtiene todos los almacenes disponibles*/
	private void getAllStores(){
		
		String sql = "SELECT M_Warehouse_ID FROM M_WAREHOUSE";
		sql = MRole.getDefault().addAccessSQL(sql, "", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();		
			while (rs.next()){
				MWarehouse store = new MWarehouse(Env.getCtx(), rs.getInt(1), null);
				stores.add(store);
				System.out.println(store.getValue());
			}

		} catch (SQLException e){			
			log.log(Level.SEVERE, e.getMessage());
		}finally{
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}	
	}
	private Integer getTypeInventory(String inv_value) {
		Integer returned = null;		
		if (inv_value == null) 
			return returned;
		
		String sql = "SELECT XX_VMR_TypeInventory_ID " +
		"FROM XX_VMR_TypeInventory  " +
		"WHERE value = " + "'"+inv_value+"'";
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
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return returned;
	}

	private Integer getSection(String sec_value, Integer lin) {
		Integer returned = null;		
		if (sec_value == null || lin ==null) 
			return returned;
		
		String sql = "SELECT s.XX_VMR_Section_ID " +
		"FROM XX_VMR_Section s " +
		"WHERE  s.XX_VMR_Line_ID = " + lin+
		"and s.value = " + sec_value;
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
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return returned;
	}

	private Integer getLine(String lin_value, Integer dep) {
		Integer returned = null;		
		if (lin_value == null || dep ==null) 
			return returned;
		
		String sql = "SELECT l.XX_VMR_Line_ID " +
		"FROM XX_VMR_Line l " +
		"WHERE  l.XX_VMR_Department_ID = " + dep+
		"and l.value = " + lin_value;
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
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return returned;
	}

	private Integer getDepartment(String dep_value) {
		
		Integer returned = null;		
		if (dep_value == null) 
			return returned;
		
		String sql = "SELECT d.XX_VMR_Department_ID " +
		"FROM XX_VMR_Department d " +
		"WHERE  d.value = " + dep_value;
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
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return returned;
	}
	
}
