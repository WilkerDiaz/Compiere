package compiere.model.cds.processes;


import java.awt.Container;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;

import jxl.*;
import jxl.read.biff.BiffException;

import org.compiere.apps.ADialog;
import org.compiere.common.constants.EnvConstants;
import org.compiere.model.MRole;
import org.compiere.model.MWarehouse;
import org.compiere.model.X_M_Movement;
import org.compiere.model.X_Ref_Quantity_Type;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MLocator;
import compiere.model.cds.MMovement;
import compiere.model.cds.MMovementLine;
import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRDepartment;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_XX_VMR_Department;
import compiere.model.cds.X_XX_VMR_DepartmentBudget;

public class XX_ImportInitialParameters extends SvrProcess {

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
			
			//Si la cantidad de columnas es 3
			if(sheet.getColumns()>=3 && sheet.getRows() > 1){
		
				//Valido que las cabeceras tengan el formato correcto	
				if(!sheet.getCell(0, 0).getContents().equals("DEPARTAMENTO") 
						|| !sheet.getCell(1, 0).getContents().equals("CARGA MINIMA BS")
						|| !sheet.getCell(2, 0).getContents().equals("MERMA") 					
				) {
					msg = Msg.translate( getCtx(), "Column Names");
					return msg;
				}	
			
				// Iteramos sobre cada linea del excel
				for (int i = 1; i < sheet.getRows() && (sheet.getCell(0, i).getContents())!=null; i++) {
					
					//Capturo el DEPARTAMENTO
					String departamento = sheet.getCell(0, i).getContents();
					// Capturo la carga
					BigDecimal carga = new BigDecimal(sheet.getCell(1, i).getContents());
					// capturo la merma
					BigDecimal merma = new BigDecimal(sheet.getCell(2, i).getContents());
					
					X_XX_VMR_DepartmentBudget detalle = new X_XX_VMR_DepartmentBudget(getCtx(), 0, get_Trx());
					detalle.setXX_VMR_Department_ID(getDepartment(departamento));
					detalle.setXX_Decrease(merma);
					detalle.setXX_MinAmount(carga);
					detalle.setXX_VMR_ComercialBudget_ID(getRecord_ID());
					detalle.save();
					
				}
			}
		} catch (Exception e)
		{
			
		}
		return "";
	}

	/** Retorna un producto de acuerdo a un value */
	private int getDepartment(String departamento) {
		
		int retornado = 0;		
		
		String sql_prod = "SELECT XX_VMR_Department_ID FROM XX_VMR_Department WHERE VALUE = " + departamento;		
		PreparedStatement ps = DB.prepareStatement(sql_prod, null);
		try {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				retornado = rs.getInt(1);			
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retornado;
	}
	
}