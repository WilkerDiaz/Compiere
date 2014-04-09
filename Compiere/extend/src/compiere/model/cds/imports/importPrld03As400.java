package compiere.model.cds.imports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import compiere.model.cds.As400DbManager;
import compiere.model.cds.X_XX_VMR_Prld03;

public class importPrld03As400 extends SvrProcess {

	Vector<Integer> departmentIDs = new Vector<Integer>();
	Vector<String> departmentCodes = new Vector<String>();
	Vector<String> storeCodes = new Vector<String>();
	Vector<Integer> storeIDs = new Vector<Integer>();
	
	@Override
	protected void prepare() {
	}

	@Override
	protected String doIt() throws Exception {

		String myLog = "";
		int inserted=0;
		
		//Borrado de los registros del mes
		Date actualDate= new Date();
		
		SimpleDateFormat formatoY = new SimpleDateFormat("yyyy");
		String year = formatoY.format(actualDate);
		
		SimpleDateFormat formatoM = new SimpleDateFormat("MM");
		String month = formatoM.format(actualDate);

	    myLog += "\n" + "Antes de Cargar: " + actualDate.toString() + "\n";
	     
		//Busqueda de los registros del mes
		As400DbManager As = new As400DbManager();
		As.conectar();
		
		Statement statementAS400=null;
		
		statementAS400 = As.conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = null;
		
		String sql = "SELECT * FROM becofile.PRLD03 " +
					 "WHERE AÑOPRE = "+ year +" AND MESPRE = " + month + " " +
					 "ORDER BY MESPRE";
		try
        {          
 	
			rs= As.realizarConsulta(sql,statementAS400);
			
			int dep=0;
			int store=0;
			
			while (rs.next())
			{
				inserted++;
				if(inserted==1){
					getAllDeparments();
					getAllStores();

					actualDate= new Date();
					myLog += "Empezando a crear en Compiere: " + actualDate.toString() + "\n";
				}
				
				//Codigo de Tienda
				store = getStoreID(rs.getString("TIENDA"));
				if(store==0)
					continue;
						
				//Codigo de Departamento
				dep = getDepartmentID(rs.getString("CODDEP"));
				if(dep==0)
					continue;
	
			    //Se genera el registro
			    X_XX_VMR_Prld03 prld03 = new X_XX_VMR_Prld03(getCtx(),0,null);
			    		
				prld03.setM_Warehouse_ID(store);
				prld03.setXX_VMR_Department_ID(dep);		
				prld03.setXX_YEARBUDGET(rs.getInt("AÑOPRE"));
				prld03.setXX_MONTHBUDGET(rs.getInt("MESPRE"));	
				prld03.setXX_BUDGETDAY(rs.getInt("DIAPRE"));
				prld03.setXX_MONESTIR(rs.getBigDecimal("MONEST"));
				prld03.setXX_MONEJE(rs.getBigDecimal("MONEJE"));
					
			    prld03.save();	
			}
			
			statementAS400.close();
			rs.close();
			As.desconectar();
			
        }catch (Exception e) {
        	statementAS400.close();
        	rs.close();
        	As.desconectar();
        	e.printStackTrace();
        	myLog += "Error: " + e.getMessage() + "\n";
		}
		
        actualDate= new Date();
        myLog += "Registros cargados: " + inserted;
        myLog += "\n" + "Fin de carga: " + actualDate.toString();
        
		return "Registros cargados: " + inserted + " / Año Mes: " + year + " "+month;
	}
	
	private void getAllDeparments(){
		
		String sql = "SELECT VALUE, XX_VMR_DEPARTMENT_ID " +
				     "FROM XX_VMR_DEPARTMENT WHERE ISACTIVE='Y' " +
				     "AND AD_CLIENT_ID = " + getAD_Client_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				departmentCodes.add(rs.getString("VALUE"));
				departmentIDs.add(rs.getInt("XX_VMR_DEPARTMENT_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}	
	}
	
	private void getAllStores(){
		
		String sql = "SELECT VALUE, M_WAREHOUSE_ID " +
				     "FROM M_WAREHOUSE " +
				     "WHERE UPPER(VALUE) = LOWER(VALUE) " +
				     "AND AD_CLIENT_ID = " + getAD_Client_ID();
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				storeCodes.add(rs.getString("VALUE"));
				storeIDs.add(rs.getInt("M_WAREHOUSE_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}	
	}
	
	private int getStoreID(String cellValue){
		
		int index=-1;  // OJO CON ESTO (ACTUALMENTE LAS TIENDAS TIENEN 3 DIGITOS Ejm: 002)
		if(cellValue.length()==1){
			cellValue = "00" + cellValue;
		}else if (cellValue.length()==2) {
			cellValue = "0" + cellValue;
		}
		
		for(int i=0; i<storeCodes.size(); i++){
			
			if(storeCodes.get(i).equals(cellValue)){
				index=i;
				break;
			}
		}
		
		if(index!=-1)
			return storeIDs.get(index);
		else
			return 0;
	}
	
	private int getDepartmentID(String cellValue){
		
		int index=-1;
		if(cellValue.length()==1){
			cellValue = "0" + cellValue;
		}
		
		for(int i=0; i<departmentCodes.size(); i++){
			
			if(departmentCodes.get(i).equals(cellValue)){
				index=i;
				break;
			}
		}
		
		if(index!=-1)
			return departmentIDs.get(index);
		else
			return 0;
	}
}
