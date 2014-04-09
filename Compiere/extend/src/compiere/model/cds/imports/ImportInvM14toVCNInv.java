package compiere.model.cds.imports;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;

import compiere.model.cds.As400DbManager;
import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRPriceConsecutive;
import compiere.model.cds.X_XX_VCN_Inventory;

/** Permite importar la cantidad y monto inicial de los productos del inventario en un mes y año específico 
 * de la tabla INVM14 de AS400 a la tabla XX_VCN_INVENTORY de Compiere.
 *  
 *  @author Gabrielle Huchet
 */
public class ImportInvM14toVCNInv extends SvrProcess {

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
	Vector<Integer> productIDs = new Vector<Integer>();
	Vector<String> productCodes = new Vector<String>();
	int year = 0;
	int month = 0;
	boolean isOk =true;
	String myLog = "";
	int inserted=0;
	int i=0;
	Date actualDate= new Date();
	
	protected void prepare() {
		ProcessInfoParameter[] parameter = getParameter();

		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();		
			if (element.getParameter()!=null) {
				if (name.equals("month") ) {
					month = element.getParameterAsInt();
				}else if (name.equals("year") ) {
					year = element.getParameterAsInt();
				}
			}
		}
		
	}

	protected String doIt() throws Exception {

		int j = 0;
		int totalInserted = 0;
		//Borrado de los registros del mes
	
		String queryDelete = "DELETE FROM XX_VCN_INVENTORY WHERE XX_INVENTORYYEAR =" + year+ "AND XX_INVENTORYMONTH = "+month;
		
		PreparedStatement pstmtQuery = DB.prepareStatement(queryDelete, null);
	    int deleted = pstmtQuery.executeUpdate(queryDelete);
	    pstmtQuery.close();
	    
	    actualDate= new Date();
	    myLog += "\n" + "Antes de Cargar: " + actualDate.toString() + "\n";
		System.out.println(myLog);
		
		String sql = "SELECT  SUM(A.BS_INV) BS_INV , SUM(A.CAN_INV) CAN_INV, A.TIENDA, A.CODPRO, (CASE WHEN A.CONPRE >= 8000 THEN 0 ELSE A.CONPRE END) CONPRE, A.CODCAT, A.CODDEP, A.CODLIN, A.SECCIO, " +
					 "(CASE WHEN A.LOTE IS NULL THEN 0 ELSE A.LOTE END) LOTE "+
					 "FROM BECOFILE.INV_LOTE A " +
					 "WHERE (A.BS_INV != 0 OR A.CAN_INV !=0)" +
					 "GROUP BY  A.TIENDA, A.CODPRO, A.CODCAT, A.CODDEP, A.CODLIN, A.SECCIO, (CASE WHEN A.CONPRE >= 8000 THEN 0 ELSE A.CONPRE END), " +
					 "(CASE WHEN A.LOTE IS NULL THEN 0 ELSE A.LOTE END)";
		
		inicializar(sql);
	    j = i;
	    totalInserted = inserted;
	    
	    myLog += "\nRegistros borrados: " + deleted + " / Registros leidos: " + totalInserted + " / Registros cargados: " + j + "/ Año-Mes: " + year +"-"+ month;
        
	    calcularCostos(month, year);
        actualDate= new Date();  
        myLog += "\n" + "Fin de carga: " + actualDate.toString();
        
        sendMailCompleteProcess(isOk, myLog);
		return "Registros borrados: " + deleted + " / Registros leidos: " + totalInserted + " / Registros cargados: " + j + "/ Año-Mes: " + year +"-"+ month;

	}
	
	private void inicializar(String sql) throws Exception{
	     
		inserted=0;
		i=0;
			//Busqueda de los registros del mes
			As400DbManager As = new As400DbManager();
			As.conectar();
			
			Statement statementAS400=null;
			
			statementAS400 = As.conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = null;
			
			System.out.println(sql);
			try
	        {          
	 	
				rs= As.realizarConsulta(sql,statementAS400);
				
				int category=0;
				int dep=0;
				int line=0;
				int section=0;
				int store=0;
				int product=0;
				int priceConseID = 0;
				int priceConseNro = 0;
				int batch=0;
				
				while (rs.next())
				{
					inserted++;
					if(inserted==1){
						getAllCategorys();
						getAllDeparments();
						getAllLines();
						getAllSections();
						getAllStores();
						getAllProducts();

						actualDate= new Date();
						myLog += "Empezando a crear en Compiere: " + actualDate.toString() + "\n";
					}
					
					if(i%10000==0)
						System.out.println("Cargados: " + i);
					
					//Codigo de Tienda
					store = getStoreID(rs.getString("TIENDA"));
					if(store==0){
						System.out.println("TIENDA");
						System.out.println(rs.getString("TIENDA")+","+rs.getString("CODPRO")
								+","+rs.getString("CONPRE")+","+rs.getString("LOTE"));
						continue;
					}
					//Codigo de Categoria
					category = getCategoryID(rs.getString("CODCAT"));	
					if(category==0){
						System.out.println("CAT");
						System.out.println(rs.getString("TIENDA")+","+rs.getString("CODCAT")+","+
								","+rs.getString("CODDEP")+","+rs.getString("CODLIN")+","+rs.getString("SECCIO")+","+rs.getString("CODPRO")
								+","+rs.getString("CONPRE")+","+rs.getString("LOTE"));
						continue;
					}
					//Codigo de Departamento
					dep = getDepartmentID(rs.getString("CODDEP"));
					if(dep==0){
						System.out.println("DEP");
						System.out.println(rs.getString("TIENDA")+","+rs.getString("CODCAT")+","+
								","+rs.getString("CODDEP")+","+rs.getString("CODLIN")+","+rs.getString("SECCIO")+","+rs.getString("CODPRO")
								+","+rs.getString("CONPRE")+","+rs.getString("LOTE"));
						continue;
					}
					//Codigo de Linea
					line = getLineID(rs.getString("CODLIN"),dep);
					if(line==0){
						System.out.println("LIN");
						System.out.println(rs.getString("TIENDA")+","+rs.getString("CODCAT")+","+
								","+rs.getString("CODDEP")+","+rs.getString("CODLIN")+","+rs.getString("SECCIO")+","+rs.getString("CODPRO")
								+","+rs.getString("CONPRE")+","+rs.getString("LOTE"));
						continue;
					}
					
					//Codigo de Seccion
					section = getSectionID(rs.getString("SECCIO"), line);
					if(section==0){
						System.out.println("SEC");
						System.out.println(rs.getString("TIENDA")+","+rs.getString("CODCAT")+","+
								","+rs.getString("CODDEP")+","+rs.getString("CODLIN")+","+rs.getString("SECCIO")+","+rs.getString("CODPRO")
								+","+rs.getString("CONPRE")+","+rs.getString("LOTE"));
						continue;
					}
					//Codigo de Producto
					product = getProductID(rs.getString("CODPRO"));
					if(product==0){
						System.out.println("PRO");
						System.out.println(rs.getString("TIENDA")+","+rs.getString("CODPRO")
								+","+rs.getString("CONPRE")+","+rs.getString("LOTE"));
						continue;
					}
					
					//Consecutivo de Precio
					priceConseID= getPriceConsecutiveID(rs.getString("CONPRE"), product, rs.getString("LOTE"));
					if(priceConseID==0){
						priceConseID= getPriceConsecutiveID(rs.getString("CONPRE"), product);
					}

					if(priceConseID==0){
						batch = getBatch(rs.getString("LOTE"), product);
						priceConseNro = 0;
					}
					else {
						MVMRPriceConsecutive priceConse = new MVMRPriceConsecutive(getCtx(),priceConseID, null);
						batch = priceConse.getM_AttributeSetInstance_ID();
						priceConseNro = priceConse.getXX_PriceConsecutive();
					}

				    //Se genera el registro
				    X_XX_VCN_Inventory inv = new X_XX_VCN_Inventory(getCtx(),0,null);	
				    
				    inv.setM_Product_ID(product);
				    inv.setXX_ConsecutivePrice(new BigDecimal(priceConseNro));
					inv.setM_Warehouse_ID(store);
					inv.setXX_VMR_Category_ID(category);
					inv.setXX_VMR_Department_ID(dep);		
					inv.setXX_VMR_Line_ID(line);
					inv.setXX_VMR_Section_ID(section);
					if(batch>0){
						inv.setM_AttributeSetInstance_ID(batch);
					}
					inv.setXX_INVENTORYYEAR(new BigDecimal(year));
					inv.setXX_INVENTORYMONTH(new BigDecimal(month));
					inv.setXX_INITIALINVENTORYAMOUNT(rs.getBigDecimal("BS_INV"));
					inv.setXX_INITIALINVENTORYQUANTITY(rs.getBigDecimal("CAN_INV"));
				    inv.save();	
				    i++;
				}
				
	        }catch (Exception e) {
	        	e.printStackTrace();
	        	myLog += "Error: " + e.getMessage() + "\n";
	        	isOk = false;
			}finally {
				statementAS400.close();   	
	        	As.desconectar();
				rs.close();
			}
			
		
	}
	


	/** Actualiza costos  en productos del inventario del mes seleccionado */
	public void calcularCostos(int month, int year) throws Exception {
		PreparedStatement psUpdate =null;
		try {
			String sqlUpdate = "\nUPDATE XX_VCN_INVENTORY I SET I.XX_INITIALINVENTORYCOSTPRICE = "+
				"\nNVL((SELECT MAX(PC.XX_UNITPURCHASEPRICE) "+
					"\nFROM  XX_VMR_PRICECONSECUTIVE PC  " +
					"\nWHERE PC.M_PRODUCT_ID = I.M_PRODUCT_ID AND I.M_ATTRIBUTESETINSTANCE_ID = PC.M_ATTRIBUTESETINSTANCE_ID),0.01) "+
				"\nWHERE  I.XX_INVENTORYYEAR = " + year + " AND I.XX_INVENTORYMONTH = " + month;
	
			psUpdate = DB.prepareStatement(sqlUpdate,get_TrxName());
			psUpdate.execute();
			commit();
		}catch (Exception e) {
			 throw new Exception("Error actualizando costos faltantes de inventario mensual" +e.getMessage());
		}finally{
			psUpdate.close();
		}
		
		
		try {
			String sqlUpdate ="\nupdate XX_VCN_INVENTORY I SET I.XX_INITIALINVENTORYCOSTPRICE = "+
					"\n(select priceactual from M_ATTRIBUTESETINSTANCE asi where I.M_ATTRIBUTESETINSTANCE_ID = asi.M_ATTRIBUTESETINSTANCE_ID) "+
					"\nWHERE (I.XX_INITIALINVENTORYCOSTPRICE IS NULL OR I.XX_INITIALINVENTORYCOSTPRICE = 0.01) and "+
					"\nI.XX_INVENTORYYEAR = " + year + " AND I.XX_INVENTORYMONTH = " + month;
			
			psUpdate = DB.prepareStatement(sqlUpdate,get_TrxName());
			psUpdate.execute();
			commit();
		}catch (Exception e) {
			 throw new Exception("Error actualizando costos faltantes de inventario mensual" +e.getMessage());
		}finally{
			psUpdate.close();
		}

		
	}
	
	private int getBatch(String cellValue, int prod) {

		int result = 0;
		String sql = "SELECT A.M_ATTRIBUTESETINSTANCE_ID "+
			"FROM M_INOUTLINE A, M_INOUT B, M_PRODUCT C, M_ATTRIBUTESETINSTANCE D  "+
			"WHERE A.M_INOUT_ID = B.M_INOUT_ID AND A.M_PRODUCT_ID = C.M_PRODUCT_ID  "+
			"AND A.M_ATTRIBUTESETINSTANCE_ID = D.M_ATTRIBUTESETINSTANCE_ID "+
			"AND B.ISSOTRX = 'N' "+
			"AND C.M_PRODUCT_ID = "+prod+
			"AND D.LOT = "+cellValue+
			"AND B.AD_CLIENT_ID = " + getAD_Client_ID()+
			"AND ROWNUM =1 ";

		PreparedStatement prst = DB.prepareStatement(sql,null);
		ResultSet rs = null;
		try {
		rs = prst.executeQuery();
		
		while (rs.next()){
			result = rs.getInt(1);
		}

		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}finally {
			try {
				rs.close();
				prst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		return result;
	}

	
	private int getPriceConsecutiveID(String cellValue, int prod, String lote) {

		int result = 0;
		String sql = "\nSELECT PC.XX_VMR_PRICECONSECUTIVE_ID " +
	     		"\nFROM XX_VMR_PRICECONSECUTIVE PC, M_ATTRIBUTESETINSTANCE ASI " +
	     		"\nWHERE  PC.XX_PRICECONSECUTIVE = "+cellValue+" AND PC.M_PRODUCT_ID =" +prod+
	     		"\nAND PC.AD_CLIENT_ID = " + getAD_Client_ID()+
	     		"\nAND PC.M_ATTRIBUTESETINSTANCE_ID  = ASI.M_ATTRIBUTESETINSTANCE_ID " +
	     		"\n AND ASI.LOT = "+lote;

		PreparedStatement prst = DB.prepareStatement(sql,null);
		ResultSet rs = null;
		try {
		rs = prst.executeQuery();
		
		if (rs.next()){
			result = rs.getInt(1);
		}

		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}
		return result;
	}
	
	private int getPriceConsecutiveID(String cellValue, int prod) {

		int result = 0;
		String sql = "\nSELECT PC.XX_VMR_PRICECONSECUTIVE_ID " +
	     		"\nFROM XX_VMR_PRICECONSECUTIVE PC " +
	     		"\nWHERE  PC.XX_PRICECONSECUTIVE = "+cellValue+" AND PC.M_PRODUCT_ID =" +prod+
	     		"\nAND PC.AD_CLIENT_ID = " + getAD_Client_ID()+
	     		"\nAND PC.M_ATTRIBUTESETINSTANCE_ID  = (SELECT MAX(PC2.M_ATTRIBUTESETINSTANCE_ID) " +
	     		"\nfrom XX_VMR_PRICECONSECUTIVE PC2 WHERE PC2.XX_PRICECONSECUTIVE =  PC.XX_PRICECONSECUTIVE " +
	     		"\nAND PC2.M_PRODUCT_ID = PC.M_PRODUCT_ID ) ";

		PreparedStatement prst = DB.prepareStatement(sql,null);
		ResultSet rs = null;
		try {
		rs = prst.executeQuery();
		
		if (rs.next()){
			result = rs.getInt(1);
		}

		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}
		return result;
	}

	private void sendMailCompleteProcess(boolean complete, String log) {
	
		String emailTo = "iellehuchet@gmail.com";
		String subject = "", msg = "";
		if(complete){
			subject = "Inicialización de Inventario Completada: "+month+"-"+year;		
			msg = "Se Completo la sincronización de Inventario."+log;
		}
		else {
			subject = "Inicialización de Inventario Falló: "+month+"-"+year;
			msg = "No se pudo completar la sincronización de Inventario. " +log;

		}
		if(emailTo.contains("@")){
			MClient m_client = MClient.get(Env.getCtx());
				
			EMail email = m_client.createEMail(null, emailTo, " ", subject, msg);
			
			if (email != null){		
				email.send();
				if (email.isSentOK()){}
			}
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
	
	private int getProductID(String cellValue){
	
		int index=-1;
		if(cellValue.length()==1){
			cellValue = "0" + cellValue;
		}

		for(int i=0; i<productCodes.size(); i++){
			
			if(productCodes.get(i).equals(cellValue)){
				index=i;
				break;
			}
		}
		
		if(index!=-1)
			return productIDs.get(index);
		else
			return 0;
		/*
		int result = 0;
		String sql = "SELECT M_PRODUCT_ID " +
	     		"FROM M_PRODUCT " +
	     		"WHERE UPPER(VALUE) = UPPER("+cellValue+")" +
	     		"AND AD_CLIENT_ID = " + getAD_Client_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		ResultSet rs = null;
		try {
		rs = prst.executeQuery();
		
		while (rs.next()){
			result = rs.getInt(1);
		}

		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}
		return result;*/
	}
	
	private void getAllProducts() {
		String sql = "SELECT M_PRODUCT_ID, VALUE " +
	     		"FROM M_PRODUCT " +
	     		"WHERE  AD_CLIENT_ID = " + getAD_Client_ID();

	PreparedStatement prst = DB.prepareStatement(sql,null);
	ResultSet rs = null;
	try {
		rs = prst.executeQuery();
	
		while (rs.next()){
			productCodes.add(rs.getString("VALUE"));
			productIDs.add(rs.getInt("M_PRODUCT_ID"));
		}
		
		rs.close();
		prst.close();
	} catch (SQLException e){
		log.log(Level.SEVERE, e.getMessage());
	}finally{
		DB.closeResultSet(rs);
		DB.closeStatement(prst);
	}
	}
	
	private void getAllStores(){
		
		String sql = "SELECT VALUE, M_WAREHOUSE_ID " +
				     "FROM M_WAREHOUSE " +
				     "WHERE UPPER(VALUE) = LOWER(VALUE) " +
				     "AND AD_CLIENT_ID = " + getAD_Client_ID();
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
		ResultSet rs = null;
		try {
			rs = prst.executeQuery();
		
			while (rs.next()){
				storeCodes.add(rs.getString("VALUE"));
				storeIDs.add(rs.getInt("M_WAREHOUSE_ID"));
			}
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}
	}

	private void getAllCategorys(){
		
		String sql = "SELECT VALUE, XX_VMR_CATEGORY_ID " +
				     "FROM XX_VMR_CATEGORY WHERE " +
				     "AD_CLIENT_ID = " + getAD_Client_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		ResultSet rs = null;
		try {
			rs = prst.executeQuery();
		
			while (rs.next()){
				categoryCodes.add(rs.getString("VALUE"));
				categoryIDs.add(rs.getInt("XX_VMR_CATEGORY_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}
	}
	
	private void getAllDeparments(){
		
		String sql = "SELECT VALUE, XX_VMR_DEPARTMENT_ID " +
				     "FROM XX_VMR_DEPARTMENT WHERE " +
				     "AD_CLIENT_ID = " + getAD_Client_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		ResultSet rs = null;
		try {
			rs = prst.executeQuery();
		
		
			while (rs.next()){
				departmentCodes.add(rs.getString("VALUE"));
				departmentIDs.add(rs.getInt("XX_VMR_DEPARTMENT_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}
	}
	
	private void getAllLines(){
		
		String sql = "SELECT VALUE, XX_VMR_DEPARTMENT_ID, XX_VMR_LINE_ID " +
				     "FROM XX_VMR_LINE WHERE " +
				     "AD_CLIENT_ID = " + getAD_Client_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		ResultSet rs = null;
		try {
			rs = prst.executeQuery();
		
			while (rs.next()){
				lineCodes.add(rs.getString("VALUE"));
				lineIDs.add(rs.getInt("XX_VMR_LINE_ID"));
				lineDepIDs.add(rs.getInt("XX_VMR_DEPARTMENT_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}
	}
	
	private void getAllSections(){
		
		String sql = "SELECT XX_VMR_LINE_ID, VALUE, XX_VMR_SECTION_ID " +
				     "FROM XX_VMR_SECTION WHERE " + 
				     "AD_CLIENT_ID = " + getAD_Client_ID();

		PreparedStatement prst = DB.prepareStatement(sql,null);
		ResultSet rs = null;
		try {
			rs = prst.executeQuery();
		
			while (rs.next()){
				sectionCodes.add(rs.getString("VALUE"));
				sectionIDs.add(rs.getInt("XX_VMR_SECTION_ID"));
				sectionLinIDs.add(rs.getInt("XX_VMR_LINE_ID"));
			}
			
			rs.close();
			prst.close();
		} catch (SQLException e){
			log.log(Level.SEVERE, e.getMessage());
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(prst);
		}
	}
	
}
