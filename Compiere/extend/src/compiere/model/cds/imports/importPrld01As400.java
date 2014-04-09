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
import compiere.model.cds.X_XX_VMR_Prld01;

public class importPrld01As400 extends SvrProcess {

	Vector<String> categoryCodes = new Vector<String>();
	Vector<Integer> categoryIDs = new Vector<Integer>();
	Vector<Integer> departmentIDs = new Vector<Integer>();
	Vector<String> departmentCodes = new Vector<String>();
	Vector<Integer> lineIDs = new Vector<Integer>();
	Vector<Integer> lineDepIDs = new Vector<Integer>();
	Vector<String> lineCodes = new Vector<String>();
	Vector<Integer> sectionIDs = new Vector<Integer>();
	Vector<Integer> sectionLinIDs = new Vector<Integer>();
	Vector<String> sectionCodes = new Vector<String>();
	Vector<String> storeCodes = new Vector<String>();
	Vector<Integer> storeIDs = new Vector<Integer>();
	
	@Override
	protected String doIt() throws Exception {

		String myLog = "";
		int inserted=0;
		
		//Borrado de los registros del mes
		Date actualDate= new Date();
		SimpleDateFormat formatoYM = new SimpleDateFormat("yyyyMM");
		String yearMonth = formatoYM.format(actualDate);
		
		String queryDelete = "DELETE FROM XX_VMR_PRLD01 WHERE XX_BUDGETYEARMONTH >=" + yearMonth;
		
		PreparedStatement pstmtQuery = DB.prepareStatement(queryDelete, null);
	    int deleted = pstmtQuery.executeUpdate(queryDelete);
	    pstmtQuery.close();

	    myLog += "\n" + "Antes de Cargar: " + actualDate.toString() + "\n";
	     
		//Busqueda de los registros del mes
		As400DbManager As = new As400DbManager();
		As.conectar();
		
		Statement statementAS400=null;
		
		statementAS400 = As.conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = null;
		
		String sql = "select * from becofile.prld01 " +
					 "WHERE AÑOMESPRE >= "+ yearMonth +" " +
					 "AND CODTIE not in ('005', '008', '012','999')" +
					 "AND CODCAT <> '99' and CODDEP <> '99' and CODLIN <> '99' and codsec <> '99' " +
					 "ORDER BY AÑOMESPRE";
		try
        {          
 	
			rs= As.realizarConsulta(sql,statementAS400);
			
			int category=0;
			int dep=0;
			int line=0;
			int section=0;
			int store=0;
			
			while (rs.next())
			{
				inserted++;
				if(inserted==1){
					getAllCategorys();
					getAllDeparments();
					getAllLines();
					getAllSections();
					getAllStores();

					actualDate= new Date();
					myLog += "Empezando a crear en Compiere: " + actualDate.toString() + "\n";
				}
				
				//if(i%10000==0)
					//System.out.println("Cargados: " + i);
				
				//Codigo de Tienda
				store = getStoreID(rs.getString("CODTIE"));
				if(store==0)
					continue;
					
				//Codigo de Categoria
				category = getCategoryID(rs.getString("CODCAT"));	
				if(category==0)
					continue;
						
				//Codigo de Departamento
				dep = getDepartmentID(rs.getString("CODDEP"));
				if(dep==0)
					continue;
				
				//Codigo de Linea
				line = getLineID(rs.getString("CODLIN"),dep);
				if(line==0)
					continue;
				
				//Codigo de Seccion
				section = getSectionID(rs.getString("CODSEC"), line);
				if(section==0)
					continue;

			    //Se genera el registro
			    X_XX_VMR_Prld01 prld01 = new X_XX_VMR_Prld01(getCtx(),0,null);
			    	
			    prld01.setXX_INVEFECBUDGETEDAMOUNT(rs.getBigDecimal("MOINVINPR"));
			   	prld01.setXX_INVAMOUNTORIGBUDGETED(rs.getBigDecimal("CAINVINPR")); 
			   	prld01.setXX_INIAMOUNTINVECOST(rs.getBigDecimal("MOINVINCO")); 
				prld01.setXX_AMOUNTINIINVEREAL(rs.getBigDecimal("MOINVINRE"));  
				prld01.setXX_NUMINIINVEREAL(rs.getBigDecimal("CAINVINRE")); 
				prld01.setXX_PURCHAMOUNTBUDGETED(rs.getBigDecimal("MONCOMPRE"));  
				prld01.setXX_QUANTBUDGETEDSHOPPING(rs.getBigDecimal("CANCOMPRE"));  
				prld01.setXX_AMOUNTPLACEDNACPURCHCOST(rs.getBigDecimal("MOCOMNCCO"));  
				prld01.setXX_AMOUNTPLACEDNACCAMPRA(rs.getBigDecimal("MOCOMNCOL"));  
				prld01.setXX_NUMNACSHOPPINGPLACED(rs.getBigDecimal("CACOMNCOL")); 
				prld01.setXX_NACPURCHAMOUNTRECEIVED(rs.getBigDecimal("MOCOMNREC"));  
				prld01.setXX_QUANTPURCHNAC(rs.getBigDecimal("CACOMNREC")); 
				prld01.setXX_PURCHAMOUNTPLACEDCOSTIMP(rs.getBigDecimal("MOCOMICCO")); 
				prld01.setXX_PURCHAMOUNTPLACEDIMPD(rs.getBigDecimal("MOCOMICOL")); 
				prld01.setXX_PURCHQUANTIMPDPLACED(rs.getBigDecimal("CACOMICOL")); 
				prld01.setXX_PURCHAMOUNTREVIMPD(rs.getBigDecimal("MOCOMIREC")); 
				prld01.setXX_QUANTPURCHAMOUNTSREV(rs.getBigDecimal("CACOMIREC")); 
				prld01.setXX_PURCHAMOUNTPLADPASTMONTHS(rs.getBigDecimal("MOCOMCOMA"));
				prld01.setXX_PURCHQUANTPLACEDMONTHS(rs.getBigDecimal("CACOMCOMA")); 
				prld01.setXX_PURCHAMOUNTREDPASTMONTHS(rs.getBigDecimal("MOCOMREMA")); 
				prld01.setXX_NUMMONTHSREDSHOP(rs.getBigDecimal("CACOMREMA")); 
				prld01.setXX_SALESAMOUNTBUD2(rs.getBigDecimal("MONVENPRE")); 
				prld01.setXX_SALESAMOUNTBUD(rs.getBigDecimal("CANVENPRE")); 
				prld01.setXX_AMOUNTSALECOST(rs.getBigDecimal("MONVENCOS")); 
				prld01.setXX_AMOUNTACTUALSALE(rs.getBigDecimal("MONVENREA")); 
				prld01.setXX_QUANTACTUALSALE(rs.getBigDecimal("CANVENREA"));  
				prld01.setXX_PROMSALEAMOUNTBUD(rs.getBigDecimal("MOREBPRPR")); 					
				prld01.setXX_PROMSALENUMBUD(rs.getBigDecimal("CAREBPRPR")); 
				prld01.setXX_PECTSALEPROMBUD(rs.getBigDecimal("POREBPRPR")); 
				prld01.setXX_ACTAMOUNTSALEPROM(rs.getBigDecimal("MOREBPRRE")); 
				prld01.setXX_AMOUNTSALEPROMINTERESTS(rs.getBigDecimal("CAREBPRRE"));  
				prld01.setXX_PECTSALEPROMINTERESTS(rs.getBigDecimal("POREBPRRE"));  //OJO
				//prld01.setXX_PERTSALEFRINTERESTS(rs.getBigDecimal("POREBPRRE"));  //OJO
				prld01.setXX_PERTSALEFRINTERESTS(rs.getBigDecimal("POREBFRRE"));  //OJO
				prld01.setXX_AMOUNTSALEFRBUD(rs.getBigDecimal("MOREBFRPR")); 
				prld01.setXX_BUDAMOUNTFRSALE(rs.getBigDecimal("CAREBFRPR")); 
				prld01.setXX_PORTSALEFRBUD(rs.getBigDecimal("POREBFRPR")); 
				prld01.setXX_ACTAMOUNTSALEFR(rs.getBigDecimal("MOREBFRRE")); 
				prld01.setXX_AMOUNTSALEFRINTERESTS(rs.getBigDecimal("CAREBFRRE")); 
				prld01.setXX_FINALSALEAMOUNTBUD(rs.getBigDecimal("MOREBDFPR")); 
				prld01.setXX_FINALBUDAMOUNTSALE(rs.getBigDecimal("CAREBDFPR")); 
				prld01.setXX_PERCENTSQALEFINALBUD(rs.getBigDecimal("POREBDFPR")); 
				prld01.setXX_FINALACTAMOUNTSALE(rs.getBigDecimal("MOREBDFRE")); 
				prld01.setXX_FINALSALEAMOUNTINTERESTS(rs.getBigDecimal("CAREBDFRE")); 
				prld01.setXX_PERCENTACTFINALSALE(rs.getBigDecimal("POREBDFRE")); 
				prld01.setXX_TRANSFAMOUNTSENT(rs.getBigDecimal("MONTRAENV")); 				
				prld01.setXX_NUMTRANSFSENT(rs.getBigDecimal("CANTRAENV")); 
				prld01.setXX_TRANSFAMOUNTRECEIVED(rs.getBigDecimal("MONTRAREC")); 
				prld01.setXX_NUMBTRANSFREV(rs.getBigDecimal("CANTRAREC")); 
				prld01.setXX_FINALINVAMOUNTBUD2(rs.getBigDecimal("MOINVFIPR")); 
				prld01.setXX_FINALINVAMOUNTBUD(rs.getBigDecimal("CAINVFIPR")); 
				prld01.setXX_INVAMOUNTFINALREAL(rs.getBigDecimal("MOINVFIRE")); 
				prld01.setXX_NUMREALFINALINV(rs.getBigDecimal("CAINVFIRE")); 
				prld01.setXX_FINALINVAMOUNTPROJD(rs.getBigDecimal("MOINVFIPY")); 
				prld01.setXX_NUMPROJDFINALINV(rs.getBigDecimal("CAINVFIPY")); 
				prld01.setXX_AMOUNTPURCHASELIMIT(rs.getBigDecimal("MONLIMCOM")); 
				prld01.setXX_QUANTITYPURCHLIMIT(rs.getBigDecimal("CANLIMCOM")); 
				prld01.setXX_ROTATIONBUD(rs.getBigDecimal("PORROTPRE")); 
				prld01.setXX_ROTATIONREAL(rs.getBigDecimal("PORROTREA")); 
				//prld01.setXX_PECTSALEPROMINTERESTS "+rs.getBigDecimal("PORCOBPRE"));  //OJO	
				prld01.setXX_PERCNBUDCOVERAGE(rs.getBigDecimal("PORCOBPRE")); 		//OJO
				prld01.setXX_REALPERCCOVERAGE(rs.getBigDecimal("PORCOBREA")); 
				prld01.setXX_MARGACCORDINGBUDPURCH(rs.getBigDecimal("POMARSCPR")); 
				prld01.setXX_MARGACCORDINGBUYREAL(rs.getBigDecimal("POMARSCRE")); 
				prld01.setXX_LISCKGROSSMARGPERCTBUD(rs.getBigDecimal("POMARBGPR")); 
				prld01.setXX_LISCKGROSSMARGPERTREAL(rs.getBigDecimal("POMARBGRE")); 
				prld01.setXX_NETMARGPERTCATTLEBUD(rs.getBigDecimal("POMARNGPR"));  
				prld01.setXX_NETMARGPERTROYALLIVESTOCK(rs.getBigDecimal("POMARNGRE"));
				prld01.setXX_BYWINMARGPERTBUD(rs.getBigDecimal("POMARPGPR")); 
				prld01.setXX_PERTWINGMARGREAL(rs.getBigDecimal("POMARPGRE")); 
				prld01.setXX_BUDDDECLINE(rs.getBigDecimal("MOMERMPRE")); 
				prld01.setXX_REALDECLINE(rs.getBigDecimal("MOMERMREA")); 	
				prld01.setM_Warehouse_ID(store);
				prld01.setXX_VMR_Category_ID(category);
				prld01.setXX_VMR_Department_ID(dep);		
				prld01.setXX_VMR_Line_ID(line);
				prld01.setXX_VMR_Section_ID(section);
				prld01.setXX_BUDGETYEARMONTH(rs.getInt("AÑOMESPRE"));
					
			    prld01.save();	
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
        
		return "Registros borrados: " + deleted + " / Registros cargados: " + inserted + " / AñoMes: " + yearMonth;
	}

	@Override
	protected void prepare() {
		
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
	
	private int getCategoryID(String cellValue){
		
		int index=-1;
		if(cellValue.length()==1){
			cellValue = "0" + cellValue;
		}
		
		for(int i=0; i<categoryCodes.size(); i++){
			
			if(categoryCodes.get(i).equals(cellValue)){
				index=i;
				break;
			}
		}
		
		if(index!=-1)
			return categoryIDs.get(index);
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
	
	private int getLineID(String cellValue, int depID){
		
		int index=-1;
		if(cellValue.length()==1){
			cellValue = "0" + cellValue;
		}
		
		for(int i=0; i<lineCodes.size(); i++){
			
			if(lineCodes.get(i).equals(cellValue) && lineDepIDs.get(i)==depID){
				index=i;
				break;
			}
		}
		
		if(index!=-1)
			return lineIDs.get(index);
		else
			return 0;
	}
	
	private int getSectionID(String cellValue, int lineID){
		
		int index=-1;
		if(cellValue.length()==1){
			cellValue = "0" + cellValue;
		}

		for(int i=0; i<sectionCodes.size(); i++){
			
			if(sectionCodes.get(i).equals(cellValue) && sectionLinIDs.get(i)==lineID){
				index=i;
				break;
			}
		}
		
		if(index!=-1)
			return sectionIDs.get(index);
		else
			return 0;
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

	private void getAllCategorys(){
		
		String sql = "SELECT VALUE, XX_VMR_CATEGORY_ID " +
				     "FROM XX_VMR_CATEGORY WHERE ISACTIVE='Y' " +
				     "AND AD_CLIENT_ID = " + getAD_Client_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				categoryCodes.add(rs.getString("VALUE"));
				categoryIDs.add(rs.getInt("XX_VMR_CATEGORY_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}	
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
	
	private void getAllLines(){
		
		String sql = "SELECT VALUE, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID " +
				     "FROM XX_VMR_LINE WHERE " +
				     //"ISACTIVE='Y' AND " + 
				     "AD_CLIENT_ID = " + getAD_Client_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				lineCodes.add(rs.getString("VALUE"));
				lineIDs.add(rs.getInt("XX_VMR_LINE_ID"));
				lineDepIDs.add(rs.getInt("XX_VMR_DEPARTMENT_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}	
	}
	
	private void getAllSections(){
		
		String sql = "SELECT XX_VMR_LINE_ID, VALUE, XX_VMR_SECTION_ID " +
				     "FROM XX_VMR_SECTION WHERE ISACTIVE='Y' " + 
				     "AND AD_CLIENT_ID = " + getAD_Client_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		
		try {
			ResultSet rs = prst.executeQuery();
		
			while (rs.next()){
				sectionCodes.add(rs.getString("VALUE"));
				sectionIDs.add(rs.getInt("XX_VMR_SECTION_ID"));
				sectionLinIDs.add(rs.getInt("XX_VMR_LINE_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}	
	}
	
}
